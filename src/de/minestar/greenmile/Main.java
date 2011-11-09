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
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.greenmile.threading.BorderThread;

public class Main extends JavaPlugin {

    public static void printToConsole(String msg) {
        System.out.println("[ GreenMile ] : " + msg);
    }

    @Override
    public void onDisable() {

        printToConsole("Disabled!");
    }

    @Override
    public void onEnable() {

        checkConfig();

        Server server = getServer();

        HashMap<String, Integer> map = loadWorldSettings();
        if (map == null)
            return;

        Runnable borderThread = new BorderThread(map, server);

        server.getScheduler().scheduleSyncRepeatingTask(this, borderThread,
                20 * 15, 20 * 5);
        printToConsole("Enabled!");

    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> loadWorldSettings() {

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        List<String> worlds = getConfig().getList("worlds");
        String[] split = null;
        String worldname = null;
        Integer maxSize = null;
        for (String world : worlds) {
            split = world.split("=");
            worldname = split[0].toLowerCase();
            maxSize = Integer.parseInt(split[1]);
            printToConsole("Loaded world '" + worldname + "' with maxSize = "
                    + maxSize);
            map.put(worldname, maxSize);
        }

        if (map.isEmpty()) {
            printToConsole("No map found, plugin is disabled!");
            this.setEnabled(false);
            return null;
        }
        return map;
    }

    private void checkConfig() {

        File pluginDir = getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();

        File configFile = new File(pluginDir.getAbsolutePath().concat(
                "/config.yml"));

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
