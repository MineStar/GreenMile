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

import java.util.ArrayList;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;

public class WorldManager {
    private ArrayList<GMWorld> worldList;

    /**
     * Constructor
     */
    public WorldManager() {
        this.worldList = new ArrayList<GMWorld>();
        this.loadWorlds();
    }

    /**
     * 
     * @param worldName
     *            : the worldname of the world
     * @param environment
     *            : the worldenvironment as a string
     * @param seed
     *            : the levelseed
     */
    public void addWorld(String worldName, String environment, long seed) {
        if (worldExists(worldName))
            return;

        // GET ENVIRONMENT FROM STRING
        Environment env = Environment.valueOf(environment);
        if (env == null)
            return;

        // ADD WORLD TO LIST
        worldList.add(new GMWorld(worldName, env, seed));
    }

    /**
     * 
     * @param worldName
     *            : the worldname of the world
     * @param environment
     *            : the worldenvironment
     * @param seed
     *            : the levelseed
     */
    public void createWorld(String worldName, Environment environment, long seed) {
        // WORLD EXISTS?
        if (worldExists(worldName))
            return;

        // ADD WORLD TO LIST
        this.addWorld(worldName, environment.toString(), seed);

        // CREATE WORLD
        WorldCreator generator = new WorldCreator(worldName);
        generator.seed(seed);
        generator.environment(environment);
        generator.createWorld();

        // TODO: START THREAD TO START GENERATION
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
        // TODO: LOAD WORLDS FROM YML WITH LOWERCASE
    }
}
