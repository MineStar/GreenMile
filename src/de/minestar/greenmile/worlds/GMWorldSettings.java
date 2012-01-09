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

import org.bukkit.Difficulty;
import org.bukkit.World;

public class GMWorldSettings {
    private GMWorld world = null;
    private World BukkitWorld;
    private boolean spawnMonsters = true;
    private boolean spawnAnimals = true;
    private boolean autoSave = true;
    private boolean keepSpawnLoaded = true;
    private Difficulty difficulty = Difficulty.NORMAL;

    public GMWorldSettings(GMWorld world) {
        this.world = world;
    }

    public boolean isSpawnMonsters() {
        return spawnMonsters;
    }

    public void setSpawnMonsters(boolean spawnMonsters) {
        this.spawnMonsters = spawnMonsters;
        this.updateBukkitWorld();
    }

    public boolean isSpawnAnimals() {
        return spawnAnimals;
    }

    public void setSpawnAnimals(boolean spawnAnimals) {
        this.spawnAnimals = spawnAnimals;
        this.updateBukkitWorld();
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
        this.updateBukkitWorld();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.updateBukkitWorld();
    }

    public boolean isKeepSpawnLoaded() {
        return keepSpawnLoaded;
    }

    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.keepSpawnLoaded = keepSpawnLoaded;
        this.updateBukkitWorld();
    }

    /**
     * updateBukkitWorld() : refreshes the worldsettings of the BukkitWorld
     * 
     * @return <b>true</b> : if the world was found<br />
     *         <b>false</b> : if the world was not found
     */
    private boolean updateBukkitWorld() {
        // CHECK FOR THE WORLD-EXISTANCE EVERYTIME WE ACCESS IT
        this.BukkitWorld = world.getBukkitWorld();
        if (BukkitWorld == null)
            return false;

        // UPDATE THE WORLD
        BukkitWorld.setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
        BukkitWorld.setAutoSave(this.autoSave);
        BukkitWorld.setDifficulty(this.difficulty);
        BukkitWorld.setKeepSpawnInMemory(this.keepSpawnLoaded);

        // RETURN
        return true;
    }
}
