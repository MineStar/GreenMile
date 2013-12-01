/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of GreenMile.
 * 
 * GreenMile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * GreenMile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GreenMile.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.greenmile.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.worlds.GMWorld;

public class EventPlayerListener implements Listener {

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockPlace());
        world = null;
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockBreak());
        world = null;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        if (!event.hasBlock())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }

        Material mat = event.getClickedBlock().getType();
        switch (mat) {
            case CHEST :
                event.setCancelled(!world.getEventSettings().isAllowOpenChest());
                world = null;
                break;
            case WORKBENCH :
                event.setCancelled(!world.getEventSettings().isAllowOpenWorkbench());
                world = null;
                break;
            case FURNACE :
            case BURNING_FURNACE :
                event.setCancelled(!world.getEventSettings().isAllowOpenFurnace());
                world = null;
                break;
            case DISPENSER :
                event.setCancelled(!world.getEventSettings().isAllowOpenDispenser());
                world = null;
                break;
            default :
                world = null;
                break;
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String worldName = Bukkit.getWorlds().get(0).getName();
        if (!GreenMileCore.worldManager.worldExists(worldName)) {
            worldName = event.getPlayer().getWorld().getName();
            if (!GreenMileCore.worldManager.worldExists(worldName)) {
                worldName = null;
                return;
            }
        }

        // UPDATE THE SPAWN POSITION
        if (event.getPlayer().getBedSpawnLocation() == null) {
            event.setRespawnLocation(GreenMileCore.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn());
        } else {
            event.setRespawnLocation(event.getPlayer().getBedSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }

        World oppositeWorld = null;
        String worldName = event.getFrom().getWorld().getName();
        final Environment fromEnvironment = event.getFrom().getWorld().getEnvironment();
        final TeleportCause cause = event.getCause();
        double blockRatio = 1;
        switch (fromEnvironment) {
            case NORMAL : {
                if (cause.equals(TeleportCause.NETHER_PORTAL)) {
                    oppositeWorld = Bukkit.getWorld(worldName + "_nether");
                    blockRatio = 0.125;
                    break;
                } else if (cause.equals(TeleportCause.END_PORTAL)) {
                    oppositeWorld = Bukkit.getWorld(worldName + "_the_end");
                    break;
                }
                break;
            }
            case NETHER : {
                if (cause.equals(TeleportCause.NETHER_PORTAL)) {
                    oppositeWorld = Bukkit.getWorld(worldName.replace("_nether", ""));
                    blockRatio = 8;
                    break;
                }
                break;
            }
            case THE_END : {
                if (cause.equals(TeleportCause.END_PORTAL)) {
                    oppositeWorld = Bukkit.getWorld(worldName.replace("_the_end", ""));
                    break;
                }
                break;
            }
        }

        if (oppositeWorld != null) {
            Player player = event.getPlayer();
            // event.setCancelled(true);
            Location toLocation = new Location(oppositeWorld, (player.getLocation().getX() * blockRatio), player.getLocation().getY(), (player.getLocation().getZ() * blockRatio), player.getLocation().getYaw(), player.getLocation().getPitch());
            event.setTo(toLocation);
            // player.teleport(toLocation, cause);
            return;
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }

        if (!world.getEventSettings().isPvpEnabled()) {
            if (event.getRightClicked() instanceof Player) {
                event.setCancelled(true);
            }
        }
        world = null;
    }
}
