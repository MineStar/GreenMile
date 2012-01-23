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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;

@SuppressWarnings("deprecation")
public class EventEntityListener extends EntityListener {
    private final WorldManager worldManager;

    public EventEntityListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void onEndermanPickup(EndermanPickupEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockEndermanPickUp());
        world = null;
    }

    @Override
    public void onEndermanPlace(EndermanPlaceEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockEndermanPlace());
        world = null;
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
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

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
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

    @Override
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockRegainHealth());
        world = null;
    }

    @Override
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getEntity());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowFoodLevelChange());
        world = null;
    }
}
