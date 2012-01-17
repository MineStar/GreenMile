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

package de.minestar.greenmile.worlds;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.World.Environment;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class WorldManager {
    private ArrayList<GMWorld> worldList;

    private File dataFolder;

    /**
     * Constructor
     */
    public WorldManager(File dataFolder) {
        this.worldList = new ArrayList<GMWorld>();
        this.loadWorlds();
        this.dataFolder = dataFolder;
    }

    /**
     * Add a world to the current worlds
     * 
     * @param worldName
     * @param environment
     * @param seed
     */
    public void addWorld(GMWorld world) {
        if (worldExists(world.getWorldName())) {
            ChatUtils.printConsoleWarning("World '" + world.getWorldName() + "' already exists!", Main.name);
            return;
        }
        worldList.add(world);
    }

    /**
     * 
     * @param worldName
     *            : the worldname of the world
     * @param environment
     *            : the worldenvironment
     * @param seed
     *            : the levelseed
     * @param dataFolder
     *            : the datafolder of this plugin
     * @return <b>true</b> : the creation was successful<br />
     *         <b>false</b> : the world already exists or the initialization of
     *         settings went wrong
     */
    public boolean createWorld(String worldName, Environment environment, long seed, File dataFolder) {
        // WORLD EXISTS?
        if (worldExists(worldName))
            return false;

        GMWorld newWorld = new GMWorld(worldName);
        newWorld.createSettings(seed, environment, dataFolder);
        return newWorld.createBukkitWorld();
    }

    /**
     * worldExists(String worldName)
     * 
     * @param worldName
     *            : the name of the world to check
     * @return <b>true</b> : if world exists<br />
     *         <b>false</b> : if world not exists
     */
    public boolean worldExists(String worldName) {
        for (GMWorld world : this.worldList) {
            if (world.getWorldName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load worlds
     */
    private void loadWorlds() {
        try {
            File[] files = dataFolder.listFiles();
            for (File f : files) {
                String fileName = f.getName().toLowerCase();
                if (fileName.endsWith(".yml") && !fileName.startsWith("config")) {
                    GMWorld world = new GMWorld(fileName.substring(0, fileName.length() - 4));
                    world.loadSettings(dataFolder);
                    if (world.getWorldSettings().isInitialized()) {
                        this.addWorld(world);
                        world.loadBukkitWorld();
                        world.updateWorld();
                    }
                }
            }
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load worlds from " + dataFolder, Main.name);
        }
    }
}
