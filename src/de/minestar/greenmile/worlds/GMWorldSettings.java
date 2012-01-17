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

import org.bukkit.Difficulty;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMWorldSettings {

    private boolean spawnMonsters = true;
    private boolean spawnAnimals = true;
    private boolean autoSave = true;
    private boolean keepSpawnLoaded = true;

    // the maximum size of the map
    private int maxSize;

    private Difficulty difficulty;

    public GMWorldSettings(String worldName, File dataFolder) {
        loadSettings(worldName, dataFolder);
    }

    private void loadSettings(String worldName, File dataFolder) {
        try {
            // File existing because WorldManager loading world names from
            // filenames in the datafolder!
            YamlConfiguration config = new YamlConfiguration();
            config.load(new File(dataFolder, worldName + ".yml"));
            spawnMonsters = config.getBoolean("spawnMonsters");
            spawnAnimals = config.getBoolean("spawnAnimals");
            autoSave = config.getBoolean("autoSave");
            keepSpawnLoaded = config.getBoolean("keepSpawnLoaded");
            String diffi = config.getString("difficulty");

            difficulty = Difficulty.valueOf(diffi);
            if (difficulty == null) {
                ChatUtils.printConsoleError("Difficulty '" + diffi + "' from world '" + worldName + "' doesn't find a value of Difficulty!", Main.name);
                return;
            }

            maxSize = config.getInt("size");

            ChatUtils.printConsoleInfo("World '" + worldName + "' loaded: SpawnMonster=" + spawnMonsters + ",SpawnAnimals=" + spawnAnimals + ",AutoSave=" + autoSave + ",KeepSpawnLoaded=" + keepSpawnLoaded + ",Difficulty=" + difficulty.toString() + ",Size=" + maxSize, Main.name);
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load settings for world " + worldName + "!", Main.name);
        }
    }
    public boolean isSpawnMonsters() {
        return spawnMonsters;
    }

    public void setSpawnMonsters(boolean spawnMonsters) {
        this.spawnMonsters = spawnMonsters;
    }

    public boolean isSpawnAnimals() {
        return spawnAnimals;
    }

    public void setSpawnAnimals(boolean spawnAnimals) {
        this.spawnAnimals = spawnAnimals;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isKeepSpawnLoaded() {
        return keepSpawnLoaded;
    }

    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.keepSpawnLoaded = keepSpawnLoaded;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

}
