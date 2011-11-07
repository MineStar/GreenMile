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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class BorderThread implements Runnable {

    private final int maxSize;
    private final Server server;

    private HashMap<String, Location> lastPosition = new HashMap<String, Location>();

    public BorderThread(int maxSize, Server server) {
        this.maxSize = maxSize * maxSize;
        this.server = server;
    }

    @Override
    public void run() {

        Player[] onlinePlayer = server.getOnlinePlayers();
        Location loc = null;
        int x1 = 0;
        int z1 = 0;
        for (Player player : onlinePlayer) {
            if (player.isDead() && !player.isOnline())
                continue;
            loc = player.getLocation();
            x1 = loc.getBlockX();
            z1 = loc.getBlockZ();
            if (maxSize <= ((x1 * x1) + (z1 * z1))) {
                player.teleport(lastPosition.get(player.getName()));
                player.sendMessage(ChatColor.RED
                        + "Du hast die maximale Grenze der Map erreicht!");
            }
            else
                lastPosition.put(player.getName(), loc);
        }
    }

}
