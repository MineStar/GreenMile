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

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BorderThread implements Runnable {

    private final Server server;

    private HashMap<String, Location> lastPosition = new HashMap<String, Location>();

    private final HashMap<String, Integer> worldSizes;

    public BorderThread(HashMap<String, Integer> worldSizes, Server server) {
        this.worldSizes = worldSizes;
        this.server = server;
    }

    @Override
    public void run() {

        Location loc = null;
        int x1 = 0;
        int z1 = 0;
        int z2 = 0;
        int x2 = 0;
        int maxSize = 0;
        List<World> worlds = server.getWorlds();
        List<Player> players = null;

        for (World world : worlds) {
            if (!worldSizes.containsKey(world.getName().toLowerCase()))
                continue;
            players = world.getPlayers();
            loc = world.getSpawnLocation();
            x2 = loc.getBlockX();
            z2 = loc.getBlockZ();
            maxSize = worldSizes.get(world.getName().toLowerCase());
            maxSize *= maxSize;

            for (Player player : players) {
                if (player.isDead() || !player.isOnline())
                    continue;

                loc = player.getLocation();
                x1 = loc.getBlockX();
                z1 = loc.getBlockZ();
                if (maxSize <= ((x1 - x2) * (x1 - x2))
                        + ((z1 - z2) * (z1 - z2))) {
                    loc = lastPosition.get(player.getName());
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
