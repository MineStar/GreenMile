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

package de.minestar.greenmile.commands.gm;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.commands.ExtendedCommand;
import de.minestar.greenmile.threading.ChunkGenerationThread;

public class StartCommand extends ExtendedCommand {

    private HashMap<String, Integer> map;
    private final Plugin plugin;
    private final int speed;

    public StartCommand(String syntax, String arguments, String node, HashMap<String, Integer> map, Plugin plugin, int speed) {
        super(syntax, arguments, node);
        this.map = map;
        this.plugin = plugin;
        this.speed = speed;
    }

    @Override
    public void execute(String[] args, Player player) {
        String worldName = args[0].toLowerCase();
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null || map.get(worldName) == null) {
            player.sendMessage(ChatColor.RED + "[GreenMile] World '" + worldName + "' was not found!");
            return;
        }

        if (Main.chunkThread != null) {
            player.sendMessage(ChatColor.RED + "[GreenMile] Already running a thread!");
            player.sendMessage(ChatColor.GRAY + "Type '/gm stop' to stop the thread.");
            return;
        }

        Main.chunkThread = new ChunkGenerationThread(map.get(worldName), world.getName());

        // WHEN /gm start world SPEED was used read out the speed
        int pSpeed = speed;
        if (args.length == 2) {
            try {
                pSpeed = Integer.parseInt(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Fehlerhafte Zahl, Standardgeschwindigkeit von " + pSpeed + " wird genutzt!");
            }
        }
        Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, Main.chunkThread, 0L, pSpeed));
        player.sendMessage(ChatColor.GREEN + "[GreenMile] Rendering of world '" + worldName + "' started with speed " + pSpeed + "!");
        player.sendMessage(ChatColor.GRAY + "Type '/gm stop' to stop the thread.");
    }
}
