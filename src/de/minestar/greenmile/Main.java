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
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import de.minestar.greenmile.commands.*;

import de.minestar.greenmile.commands.CommandList;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.ChunkGenerationThread;

public class Main extends JavaPlugin {

    public static ChunkGenerationThread chunkThread = null;
    private GMPListener pListener = null;
    private static HashMap<String, Integer> map;

    private CommandList cmdList;

    public static void printToConsole(String msg) {
        System.out.println("[ GreenMile ] : " + msg);
    }

    @Override
    public void onDisable() {
        if (chunkThread != null)
            chunkThread.saveConfig();

        cmdList = null;
        printToConsole("Disabled!");
    }

    private void initCommandList() {
        // @formatter:off
        cmdList = new CommandList(new Command[]{new GreenMileCommand("/gm", "", "gm.status", new Command[]{new StartCommand("start", "<WorldName>", "gm.start", map, this), new StopCommand("stop", "", "gm.stop"), new StatusCommand("status", "", "gm.status")})});
        // @formatter:on
    }

    @Override
    public void onEnable() {
        checkConfig();
        Server server = getServer();
        map = loadWorldSettings();
        if (map == null)
            return;

        initCommandList();

        Runnable borderThread = new BorderThread(map, server);
        server.getScheduler().scheduleSyncRepeatingTask(this, borderThread, 20 * 15, 20 * 5);

        pListener = new GMPListener();
        server.getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, pListener, Event.Priority.Highest, this);

        printToConsole("Enabled!");
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> loadWorldSettings() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (!(new File("plugins/GreenMile/config.yml")).exists()) {
                printToConsole("config.yml not found, plugin is disabled!");
                this.setEnabled(false);
                return null;
            }

            config.load("plugins/GreenMile/config.yml");
            List<String> worlds = config.getList("worlds");
            if (worlds == null) {
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
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        cmdList.handleCommand(sender, label, args);
        return true; 
    }

    public static HashMap<String, Integer> getWorldSettings() {
        return map;
    }
}
