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
package de.minestar.greenmile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.ChunkGenerationThread;

public class Main extends JavaPlugin {

    private ChunkGenerationThread chunkThread = null;
    private HashMap<String, Integer> map;

    public static void printToConsole(String msg) {
        System.out.println("[ GreenMile ] : " + msg);
    }

    @Override
    public void onDisable() {
        if (chunkThread != null)
            chunkThread.saveConfig();

        printToConsole("Disabled!");
    }

    @Override
    public void onEnable() {
        checkConfig();
        Server server = getServer();
        map = loadWorldSettings();
        if (map == null)
            return;

        Runnable borderThread = new BorderThread(map, server);
        server.getScheduler().scheduleSyncRepeatingTask(this, borderThread, 20 * 15, 20 * 5);

        printToConsole("Enabled!");
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> loadWorldSettings() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        try {
            YamlConfiguration config = new YamlConfiguration();
            if(!(new File("plugins/GreenMile/config.yml")).exists()) {
                printToConsole("config.yml not found, plugin is disabled!");
                this.setEnabled(false);
                return null;
            }
            
            config.load("plugins/GreenMile/config.yml");            
            List<String> worlds = config.getList("worlds");
            if(worlds == null) {
                printToConsole("config.yml is corrupt (no worlds found), plugin is disabled!");
                this.setEnabled(false);
                return null;
            }
            
            String[] split = null;
            String worldname = null;
            Integer maxSize = null;
            for (String world : worlds) {
                split = world.split("=");
                worldname = split[0].toLowerCase();
                maxSize = Integer.parseInt(split[1]);
                printToConsole("Loaded world '" + worldname + "' with maxSize = " + maxSize);
                map.put(worldname, maxSize);
            }

            if (map.isEmpty()) {
                printToConsole("No map found, plugin is disabled!");
                this.setEnabled(false);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void checkConfig() {

        File pluginDir = getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();

        File configFile = new File(pluginDir.getAbsolutePath().concat("/config.yml"));

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp())
            return true;

        if (label.equalsIgnoreCase("gm")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
                if (chunkThread == null) {
                    sender.sendMessage(ChatColor.RED + "[GreenMile] No thread found!");
                    return true;
                }
                Bukkit.getServer().getScheduler().cancelTask(chunkThread.getTaskID());
                chunkThread.saveConfig();
                chunkThread = null;
                sender.sendMessage(ChatColor.GREEN + "[GreenMile] Rendering stopped!");
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
                if (chunkThread == null) {
                    sender.sendMessage(ChatColor.RED + "[GreenMile] No thread found!");
                    return true;
                }
                sender.sendMessage(ChatColor.GRAY + "[GreenMile] Status: " + chunkThread.getStatus());
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
                World world = Bukkit.getServer().getWorld(args[1]);
                if (world == null || map.get(args[1].toLowerCase()) == null) {
                    sender.sendMessage(ChatColor.RED + "[GreenMile] World '" + args[1] + "' was not found!");
                    return true;
                }

                if (chunkThread != null) {
                    sender.sendMessage(ChatColor.RED + "[GreenMile] Already running a thread!");
                    sender.sendMessage(ChatColor.GRAY + "Type '/gm stop' to stop the thread.");
                    return true;
                }

                chunkThread = new ChunkGenerationThread(map.get(args[1].toLowerCase()), world.getName());
                chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, chunkThread, 0, 5));
                sender.sendMessage(ChatColor.GREEN + "[GreenMile] Rendering of world '" + args[1] + "' started!");
                sender.sendMessage(ChatColor.GRAY + "Type '/gm stop' to stop the thread.");
            }
        }
        return true;
    }
}
