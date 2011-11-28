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

package de.minestar.greenmile.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.commands.Command;
import de.minestar.greenmile.threading.ChunkGenerationThread;

public class StartCommand extends Command {

    private HashMap<String, Integer> map;
    private JavaPlugin plugin;

    public StartCommand(String syntax, String arguments, String node,
            HashMap<String, Integer> map, JavaPlugin plugin) {
        super(syntax, arguments, node);
        this.map = map;
        this.plugin = plugin;
    }

    @Override
    public void execute(String[] args, Player player) {
        World world = Bukkit.getServer().getWorld(args[1]);
        if (world == null || map.get(args[1].toLowerCase()) == null) {
            player.sendMessage(ChatColor.RED + "[GreenMile] World '" + args[1]
                    + "' was not found!");
            return;
        }

        if (Main.chunkThread != null) {
            player.sendMessage(ChatColor.RED
                    + "[GreenMile] Already running a thread!");
            player.sendMessage(ChatColor.GRAY
                    + "Type '/gm stop' to stop the thread.");
            return;
        }

        Main.chunkThread = new ChunkGenerationThread(map.get(args[1]
                .toLowerCase()), world.getName());
        Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler()
                .scheduleSyncRepeatingTask(plugin, Main.chunkThread, 0l, 5l));
        player.sendMessage(ChatColor.GREEN + "[GreenMile] Rendering of world '"
                + args[1] + "' started!");
        player.sendMessage(ChatColor.GRAY
                + "Type '/gm stop' to stop the thread.");
    }

}
