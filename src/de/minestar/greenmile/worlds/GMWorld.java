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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMWorld {

    private String worldName;
    private GMWorldSettings settings;

    /**
     * Constructor
     * 
     * @param worldName
     *            : worldname as a string
     * @param environment
     *            : environment as a string (default environment is NORMAL)
     * @param seed
     *            : the levelseed as a long
     */
    public GMWorld(String worldName) {
        this.worldName = worldName;
    }

    public void loadSettings(File dataFolder) {
        this.settings = new GMWorldSettings(this.getWorldName(), dataFolder);
    }

    public void createSettings(long levelSeed, Environment environment, File dataFolder) {
        this.settings = new GMWorldSettings(this.getWorldName(), levelSeed, environment, dataFolder);
    }

    /**
     * getWorldSettings()
     * 
     * @return the worldsettings
     */
    public GMWorldSettings getWorldSettings() {
        return settings;
    }

    /**
     * setWorldSettings(GMWorldSettings worldSettings)
     * 
     * @param worldSettings
     *            : the worldsettings to set
     */
    public void setWorldSettings(GMWorldSettings settings) {
        this.settings = settings;
    }

    /**
     * getBukkitWorld()
     * 
     * @return the BukkitWorld (can also be null)
     */
    public World getBukkitWorld() {
        return Bukkit.getServer().getWorld(this.worldName);
    }

    /**
     * Attempts to <b>create</b> the world on the server
     * 
     * @return <b>true</b> : if the world was created<br />
     *         <b>false</b> : if the world already exists or there was an
     *         internal error
     */
    public static boolean createBukkitWorld(String worldName, Environment environment, long levelSeed) {
        // WORLD ALREADY EXISTS
        if (Bukkit.getWorld(worldName) != null)
            return false;

        // FINALLY LOAD THE WORLD
        WorldCreator generator = new WorldCreator(worldName);
        generator.environment(environment);
        generator.seed(levelSeed);
        generator.createWorld();
        return true;
    }

    /**
     * Attempts to <b>load</b> the world on the server, only if the world
     * already exists.
     * 
     * @return <b>true</b> : if the world was loaded<br />
     *         <b>false</b> : if the world was not found
     */
    public boolean loadBukkitWorld() {
        // WORLD DOES NOT EXIST
        if (Bukkit.getWorld(this.worldName) == null)
            return false;

        // LOAD SETTINGS, IF THEY WERE NULL
        if (this.settings == null) {
            this.loadSettings(Main.getInstance().getDataFolder());
        }

        // SETTINGS ARE INITIALIZED COMPLETELY
        if (!this.settings.isInitialized())
            return false;

        // FINALLY LOAD THE WORLD
        WorldCreator generator = new WorldCreator(this.worldName);
        generator.environment(this.settings.getEnvironment());
        generator.seed(this.settings.getLevelSeed());
        generator.createWorld();

        return true;
    }

    /**
     * getWorldName()
     * 
     * @return the worldname
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * setWorldName(String worldName)
     * 
     * @param worldName
     *            : the worldname to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void updateWorld() {
        World bukkitWorld = Bukkit.getServer().getWorld(worldName);
        if (bukkitWorld == null) {
            ChatUtils.printConsoleError("Can't access world '" + worldName + "'!", Main.name);
            return;
        }

        // UPDATE THE WORLD
        bukkitWorld.setSpawnFlags(settings.isSpawnMonsters(), settings.isSpawnAnimals());
        bukkitWorld.setAutoSave(settings.isAutoSave());
        bukkitWorld.setDifficulty(settings.getDifficulty());
        bukkitWorld.setKeepSpawnInMemory(settings.isKeepSpawnLoaded());
        bukkitWorld.setSpawnLocation(this.settings.getWorldSpawn().getBlockX(), this.settings.getWorldSpawn().getBlockY(), this.settings.getWorldSpawn().getBlockZ());
    }
}
