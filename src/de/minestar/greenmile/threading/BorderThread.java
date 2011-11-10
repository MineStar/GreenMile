/*
 * Copyright (C) 2011 MineStar.de 
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
package de.minestar.greenmile.threading;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BorderThread implements Runnable {

    private final Server server;

    // stores the last position of an player to reset him
    private HashMap<String, Location> lastPosition = new HashMap<String, Location>();

    // store the allowed sizes of an map
    private final HashMap<String, Integer> worldSizes;

    public BorderThread(HashMap<String, Integer> worldSizes, Server server) {
        this.worldSizes = worldSizes;
        this.server = server;
    }

    @Override
    public void run() {

        Location loc = null;
        int x = 0;
        int z = 0;
        int maxSize = 0;
        List<World> worlds = server.getWorlds();
        List<Player> players = null;
        Rectangle rec = null;

        for (World world : worlds) {

            // world hasn't a border
            if (!worldSizes.containsKey(world.getName().toLowerCase()))
                continue;

            players = world.getPlayers();
            loc = world.getSpawnLocation();

            maxSize = worldSizes.get(world.getName().toLowerCase());

            // the allowed area
            rec = new Rectangle(loc.getBlockX(), loc.getBlockZ(), maxSize,
                    maxSize);

            for (Player player : players) {
                // maybe the player is dead or isn't online?
                if (player.isDead() || !player.isOnline())
                    continue;

                loc = player.getLocation();
                x = loc.getBlockX();
                z = loc.getBlockZ();

                // if player has left the are
                if (!rec.contains(x, z)) {

                    loc = lastPosition.get(player.getName());
                    // if no position is found, the player will took to spawn
                    // position
                    if (loc == null)
                        loc = player.getWorld().getSpawnLocation();

                    player.teleport(loc);
                    player.sendMessage(ChatColor.RED
                            + "Du hast die maximale Grenze der Map erreicht!");
                }
                else
                    lastPosition.put(player.getName(), loc);
            }
        }
    }
}