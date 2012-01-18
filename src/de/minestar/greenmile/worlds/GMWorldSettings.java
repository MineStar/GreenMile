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
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.helper.EnumHelper;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMWorldSettings {
    private final boolean initialized;

    private long levelSeed = 1337l;
    private Environment environment = Environment.NORMAL;

    private Location worldSpawn = null;
    private boolean spawnMonsters = true;
    private boolean spawnAnimals = true;
    private boolean autoSave = true;
    private boolean keepSpawnLoaded = true;
    private Difficulty difficulty = Difficulty.NORMAL;
    private int maxSize = 1000;

    /**
     * This constructor is used when the world is newly created
     * 
     * @param gmWorld
     * @param levelSeed
     * @param environment
     */
    public GMWorldSettings(String worldName, long levelSeed, Environment environment, File dataFolder) {
        this.worldSpawn = Bukkit.getWorld(worldName).getSpawnLocation();
        this.levelSeed = levelSeed;
        this.environment = environment;
        this.initialized = this.saveSettings(worldName, dataFolder);
    }

    /**
     * This constructor is used when the world is loaded
     * 
     * @param gmWorld
     * @param dataFolder
     */
    public GMWorldSettings(String worldName, File dataFolder) {
        this.initialized = this.loadSettings(worldName, dataFolder);
    }

    public boolean saveSettings(String worldName, File dataFolder) {
        File file = new File(dataFolder, "config_" + worldName + ".yml");
        // DELETE FILE IF IT EXISTS
        if (file.exists())
            file.delete();

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("settings.levelSeed", this.levelSeed);
            config.set("settings.environment", this.environment.toString());
            config.set("settings.spawnMonsters", this.spawnMonsters);
            config.set("settings.spawnAnimals", this.spawnAnimals);
            config.set("settings.difficulty", this.difficulty.toString());
            config.set("settings.autoSave", this.autoSave);
            config.set("settings.keepSpawnLoaded", this.keepSpawnLoaded);
            config.set("settings.maxSize", this.maxSize);
            config.set("settings.spawn.x", this.worldSpawn.getX());
            config.set("settings.spawn.y", this.worldSpawn.getY());
            config.set("settings.spawn.z", this.worldSpawn.getZ());
            config.set("settings.spawn.pitch", this.worldSpawn.getPitch());
            config.set("settings.spawn.yaw", this.worldSpawn.getYaw());
            config.save(file);
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't save settings for world " + worldName + "!", Main.name);
            return false;
        }
        return true;
    }

    private boolean loadSettings(String worldName, File dataFolder) {
        try {
            File file = new File(dataFolder, "config_" + worldName + ".yml");
            // CREATE FILE IF IT NOT EXISTS
            if (!file.exists())
                if (!this.saveSettings(worldName, dataFolder))
                    return false;

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            Environment environment = EnumHelper.getEnvironment(config.getString("settings.environment", this.environment.toString()));
            Difficulty difficulty = EnumHelper.getDifficulty(config.getString("settings.difficulty", this.difficulty.toString()));
            if (environment == null) {
                throw new Exception("Environment not found!");
            }
            if (difficulty == null) {
                throw new Exception("Difficulty not found!");
            }

            this.setLevelSeed(config.getLong("settings.levelSeed", this.levelSeed));
            this.setEnvironment(environment);
            this.setSpawnMonsters(config.getBoolean("settings.spawnMonsters", this.spawnMonsters));
            this.setSpawnAnimals(config.getBoolean("settings.spawnAnimals", this.spawnAnimals));
            this.setAutoSave(config.getBoolean("settings.autoSave", this.autoSave));
            this.setKeepSpawnLoaded(config.getBoolean("settings.keepSpawnLoaded", this.keepSpawnLoaded));
            this.setDifficulty(difficulty);
            this.setMaxSize(config.getInt("settings.maxSize", this.maxSize));
            this.setWorldSpawn(new Location(Bukkit.getWorld(worldName), config.getDouble("settings.spawn.x", 0d), config.getDouble("settings.spawn.y", 128), config.getDouble("settings.spawn.z", 0d), (float) config.getDouble("settings.spawn.yaw", 0d), (float) config.getDouble("settings.spawn.pitch", 0d)));
            ChatUtils.printConsoleInfo("World '" + worldName + "' loaded: levelSeed=" + levelSeed + ", Difficulty=" + difficulty.toString() + ", SpawnMonster=" + spawnMonsters + ", SpawnAnimals=" + spawnAnimals + ", AutoSave=" + autoSave + ", KeepSpawnLoaded=" + keepSpawnLoaded + ", Difficulty=" + difficulty.toString() + ", MaxSize=" + maxSize, Main.name);
            return true;
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load settings for world " + worldName + "!", Main.name);
            return false;
        }
    }
    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @return the levelSeed
     */
    public long getLevelSeed() {
        return levelSeed;
    }

    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param levelSeed
     *            the levelSeed to set
     */
    public void setLevelSeed(long levelSeed) {
        this.levelSeed = levelSeed;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
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

    /**
     * @return the worldSpawn
     */
    public Location getWorldSpawn() {
        return worldSpawn;
    }

    /**
     * @param worldSpawn
     *            the worldSpawn to set
     */
    public void setWorldSpawn(Location worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

}
