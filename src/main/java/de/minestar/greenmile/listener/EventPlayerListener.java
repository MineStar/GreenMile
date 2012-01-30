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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;

public class EventPlayerListener implements Listener {
    private final WorldManager worldManager;

    public EventPlayerListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getPlayer());
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

        GMWorld world = this.worldManager.getGMWorld(event.getPlayer());
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

        GMWorld world = this.worldManager.getGMWorld(event.getPlayer());
        if (world == null) {
            return;
        }

        int ID = event.getClickedBlock().getTypeId();
        if (ID == Material.CHEST.getId()) {
            event.setCancelled(!world.getEventSettings().isAllowOpenChest());
            world = null;
            return;
        } else if (ID == Material.WORKBENCH.getId()) {
            event.setCancelled(!world.getEventSettings().isAllowOpenWorkbench());
            world = null;
            return;
        } else if (ID == Material.FURNACE.getId() || ID == Material.BURNING_FURNACE.getId()) {
            event.setCancelled(!world.getEventSettings().isAllowOpenFurnace());
            world = null;
            return;
        } else if (ID == Material.DISPENSER.getId()) {
            event.setCancelled(!world.getEventSettings().isAllowOpenDispenser());
            world = null;
            return;
        }
        world = null;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String worldName = Bukkit.getWorlds().get(0).getName();
        if (!worldManager.worldExists(worldName)) {
            worldName = event.getPlayer().getWorld().getName();
            if (!worldManager.worldExists(worldName)) {
                worldName = null;
                return;
            }
        }

        // UPDATE THE SPAWN POSITION
        event.setRespawnLocation(worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn());
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getPlayer());
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
