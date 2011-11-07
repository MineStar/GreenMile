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
        int maxSize = getConfig().getInt("maxSize");
        printToConsole("Maximum map size is " + maxSize);

        Server server = getServer();
        Runnable borderThread = new BorderThread(maxSize, server);

        server.getScheduler().scheduleSyncRepeatingTask(this, borderThread,
                20 * 15, 20 * 5);
        printToConsole("Enabled!");
    }

    private void checkConfig() {

        File pluginDir = getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();

        File configFile = new File(pluginDir.getAbsolutePath().concat(
                "/config.yml"));

        if (!configFile.exists()) {
            getConfig().addDefault("maxSize", 500);
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

}
