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

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.worlds.GMWorld;

public class EventEntityListener implements Listener {

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        if (event.isCancelled())
            return;

        if (event.getEntityType() == EntityType.ENDERMAN) {
            if (event.getTo().getId() == Material.AIR.getId())
                this.onEndermanPickup(event);
            else
                this.onEndermanPlace(event);
        }
    }

    public void onEndermanPickup(EntityChangeBlockEvent event) {
        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockEndermanPickUp());
        world = null;
    }

    public void onEndermanPlace(EntityChangeBlockEvent event) {
        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockEndermanPlace());
        world = null;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }

        if (event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
            // WE HAVE AN ENTITY EXPLOSION HERE (normally: Creepers)
            event.setCancelled(world.getEventSettings().isBlockCreeperExplosions());
        } else if (event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
            // WE HAVE A BLOCK EXPLOSION HERE (normally: TNT)
            event.setCancelled(world.getEventSettings().isBlockTNT());
        }
        world = null;
        return;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }

        if (event.getEntity() instanceof LivingEntity) {
            // WE HAVE AN ENTITY HERE (normally: Creepers)
            event.setCancelled(world.getEventSettings().isBlockCreeperExplosions());
        } else {
            // WE HAVE SOMETHING ELSE HERE (normally: TNT)
            event.setCancelled(world.getEventSettings().isBlockTNT());
        }
        world = null;
        return;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }

        if (event.getDamager().getType() == EntityType.PRIMED_TNT) {
            // WE HAVE TNT HERE
            event.setCancelled(world.getEventSettings().isBlockTNT());
        } else if (event.getDamager().getType() == EntityType.CREEPER) {
            // WE HAVE A CREEPER HERE
            event.setCancelled(world.getEventSettings().isBlockCreeperExplosions());
        }
    }

    @EventHandler
    public void onPaintingBreakByEntity(PaintingBreakByEntityEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getRemover());
        if (world == null) {
            return;
        }

        if (event.getRemover().getType() == EntityType.PRIMED_TNT) {
            // WE HAVE TNT HERE
            event.setCancelled(world.getEventSettings().isBlockTNT());
        } else if (event.getRemover().getType() == EntityType.CREEPER) {
            // WE HAVE A CREEPER HERE
            event.setCancelled(world.getEventSettings().isBlockCreeperExplosions());
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockRegainHealth());
        world = null;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowFoodLevelChange());
        world = null;
    }
}
