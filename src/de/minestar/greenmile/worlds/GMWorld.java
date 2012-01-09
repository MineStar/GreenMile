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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

public class GMWorld {
    private String worldName = "";
    private Environment environment;
    private long seed = 1337l;
    private GMWorldSettings worldSettings = null;

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
    public GMWorld(String worldName, String environment, long seed) {
        this.worldName = worldName;

        this.environment = Environment.valueOf(environment);
        if (this.environment == null)
            this.environment = Environment.NORMAL;

        this.seed = seed;
        this.worldSettings = new GMWorldSettings(this);
    }

    /**
     * Constructor
     * 
     * @param worldName
     *            : the worldname as a string
     * @param environment
     *            : the environment as Bukkit.Environment
     * @param seed
     *            : the levelseed as a long
     */
    public GMWorld(String worldName, Environment environment, long seed) {
        this.worldName = worldName;
        this.environment = environment;
        this.seed = seed;
        this.worldSettings = new GMWorldSettings(this);
    }

    /**
     * getWorldSettings()
     * 
     * @return the worldsettings
     */
    public GMWorldSettings getWorldSettings() {
        return this.worldSettings;
    }

    /**
     * setWorldSettings(GMWorldSettings worldSettings)
     * 
     * @param worldSettings
     *            : the worldsettings to set
     */
    public void setWorldSettings(GMWorldSettings worldSettings) {
        this.worldSettings = worldSettings;
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
     * Attempts to <b>load or create</b> the world on the server
     * 
     * @return <b>true</b> : if the world was loaded/created<br />
     *         <b>false</b> : if the world already exists
     */
    public boolean loadOrCreateBukkitWorld() {
        World world = this.getBukkitWorld();
        if (world != null)
            return false;

        WorldCreator generator = new WorldCreator(this.worldName);
        generator.environment(this.environment);
        generator.seed(this.seed);
        generator.createWorld();
        return true;
    }

    /**
     * Attempts to load the world on the server, only if the world already
     * exists.
     * 
     * @return <b>true</b> : if the world was loaded<br />
     *         <b>false</b> : if the world was not found
     */
    public boolean loadBukkitWorld() {
        World world = this.getBukkitWorld();
        if (world == null)
            return false;

        WorldCreator generator = new WorldCreator(this.worldName);
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

    /**
     * getEnvironment()
     * 
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * setEnvironment(Environment environment)
     * 
     * @param environment
     *            : the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * getSeed()
     * 
     * @return the levelseed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * setSeed(long seed)
     * 
     * @param seed
     *            : the levelseed to set
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
