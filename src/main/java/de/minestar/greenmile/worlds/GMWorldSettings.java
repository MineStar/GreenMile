package de.minestar.greenmile.worlds;

import java.awt.Point;
import java.io.File;

import net.minecraft.server.v1_5_R2.WorldData;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.helper.EnumHelper;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class GMWorldSettings {
    private final boolean initialized;
    private long levelSeed = 1337L;
    private World.Environment environment = World.Environment.NORMAL;

    private Location worldSpawn = null;
    private boolean spawnMonsters = true;
    private boolean spawnAnimals = true;
    private boolean autoSave = true;
    private boolean keepSpawnLoaded = true;
    private Difficulty difficulty = Difficulty.NORMAL;
    private int maxSize = 1000;
    private Point lastRenderedPosition = null;

    /**
     * This constructor is used when a world is newly created
     * 
     * @param worldName
     * @param levelSeed
     * @param environment
     * @param dataFolder
     */
    public GMWorldSettings(String worldName, long levelSeed, World.Environment environment, File dataFolder) {
        if (Bukkit.getWorld(worldName) != null) {
            this.worldSpawn = Bukkit.getWorld(worldName).getSpawnLocation();
        }
        this.levelSeed = levelSeed;
        this.environment = environment;
        this.initialized = saveSettings(worldName, dataFolder);
    }

    /**
     * This constructor is used when a world is newly created
     * 
     * @param worldName
     * @param levelSeed
     * @param environment
     * @param dataFolder
     */
    public GMWorldSettings(WorldData worldData, File dataFolder) {
        this.levelSeed = worldData.getSeed();
        // i() == Normal or Nether or The End
        this.environment = World.Environment.getEnvironment(worldData.j());
        this.initialized = saveSettings(worldData.getName(), dataFolder);
    }

    /**
     * This constructor is used when a world is loaded
     * 
     * @param worldName
     * @param dataFolder
     */
    public GMWorldSettings(String worldName, File dataFolder) {
        this.initialized = loadMainSettings(worldName, dataFolder);
    }

    /**
     * SAVE SETTINGS
     * 
     * @param worldName
     * @param dataFolder
     * @return <b>true</b> if saving was successful, otherwise <b>false</b>
     */
    public boolean saveSettings(String worldName, File dataFolder) {
        File file = new File(dataFolder, "config_" + worldName + ".yml");

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
            if (this.worldSpawn != null) {
                config.set("settings.spawn.x", this.worldSpawn.getX());
                config.set("settings.spawn.y", this.worldSpawn.getY());
                config.set("settings.spawn.z", this.worldSpawn.getZ());
                config.set("settings.spawn.pitch", this.worldSpawn.getPitch());
                config.set("settings.spawn.yaw", this.worldSpawn.getYaw());
            }
            if (this.lastRenderedPosition != null) {
                config.set("render.x", this.lastRenderedPosition.x);
                config.set("render.y", this.lastRenderedPosition.y);
            } else {
                config.set("lastRenderedChunk.x", "NULL");
                config.set("lastRenderedChunk.y", "NULL");
            }
            config.save(file);
        } catch (Exception e) {
            ConsoleUtils.printException(e, GreenMileCore.NAME, "Can't save settings for world " + worldName + "!");
            return false;
        }
        return true;
    }

    /**
     * LOAD SETTINGS
     * 
     * @param worldName
     * @param dataFolder
     * @return <b>true</b> if loading was successful, otherwise <b>false</b>
     */
    private boolean loadMainSettings(String worldName, File dataFolder) {
        try {
            File file = new File(dataFolder, "config_" + worldName + ".yml");

            // SETTINGS DO NOT EXIST => TRY TO CREATE THEM
            if (!file.exists()) {
                // IF SAVE FAILS, LOADING FAILS TOO
                if (!saveSettings(worldName, dataFolder)) {
                    return false;
                }
            }

            // LOAD YAML
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            // GET VARS
            World.Environment environment = EnumHelper.getEnvironment(config.getString("settings.environment", this.environment.toString()));
            if (environment == null) {
                throw new Exception("Environment not found!");
            }

            setLevelSeed(config.getLong("settings.levelSeed", this.levelSeed));
            setEnvironment(environment);

            ConsoleUtils.printInfo(GreenMileCore.NAME, "Worldsettings for '" + worldName + "' loaded!"); // \nlevelSeed = " + this.levelSeed + "\nEnvironment = " + environment.toString());
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, GreenMileCore.NAME, "Can't load worldsettings for world " + worldName + "!");
        }
        return false;
    }

    public boolean loadWorldSettings(String worldName, File dataFolder) {
        try {
            File file = new File(dataFolder, "config_" + worldName + ".yml");

            // SETTINGS DO NOT EXIST => TRY TO CREATE THEM
            if (!file.exists()) {
                // IF SAVE FAILS, LOADING FAILS TOO
                if (!saveSettings(worldName, dataFolder)) {
                    return false;
                }
            }

            // LOAD YAML
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            // GET VARS
            Difficulty difficulty = EnumHelper.getDifficulty(config.getString("settings.difficulty", this.difficulty.toString()));

            if (difficulty == null) {
                throw new Exception("Difficulty not found!");
            }

            setSpawnMonsters(config.getBoolean("settings.spawnMonsters", this.spawnMonsters));
            setSpawnAnimals(config.getBoolean("settings.spawnAnimals", this.spawnAnimals));
            setAutoSave(config.getBoolean("settings.autoSave", this.autoSave));
            setKeepSpawnLoaded(config.getBoolean("settings.keepSpawnLoaded", this.keepSpawnLoaded));
            setDifficulty(difficulty);
            setMaxSize(config.getInt("settings.maxSize", this.maxSize));
            setWorldSpawn(new Location(Bukkit.getWorld(worldName), config.getDouble("settings.spawn.x", 0.0D), config.getDouble("settings.spawn.y", 128.0D), config.getDouble("settings.spawn.z", 0.0D), (float) config.getDouble("settings.spawn.yaw", 0.0D), (float) config.getDouble("settings.spawn.pitch", 0.0D)));

            String lastR = config.getString("lastRenderedChunk.x");
            if (lastR != null) {
                if (!lastR.equalsIgnoreCase("NULL")) {
                    setLastRenderedPosition(new Point(config.getInt("lastRenderedChunk.x"), config.getInt("lastRenderedChunk.y")));
                }
            }

            ConsoleUtils.printInfo(GreenMileCore.NAME, "Specific settings for '" + worldName + "' loaded!"); // \nSpawnMonster = " + this.spawnMonsters + "\nSpawnAnimals = " + this.spawnAnimals + "\nAutoSave = " + this.autoSave + "\nKeepSpawnLoaded = " + this.keepSpawnLoaded + "\nDifficulty = " + difficulty.toString() + "\nMaxSize = " + this.maxSize);
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, GreenMileCore.NAME, "Can't load worldspecific settings for world " + worldName + "!");
        }
        return false;
    }

    /**
     * isInitzialized()
     * 
     * @return <b>true</b> if initialization was successful, otherwise <b>false</b>
     */
    public boolean isInitialized() {
        return this.initialized;
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
    public World.Environment getEnvironment() {
        return environment;
    }

    /**
     * @return the worldSpawn
     */
    public Location getWorldSpawn() {
        return worldSpawn;
    }

    /**
     * @return the spawnMonsters
     */
    public boolean isSpawnMonsters() {
        return spawnMonsters;
    }

    /**
     * @return the spawnAnimals
     */
    public boolean isSpawnAnimals() {
        return spawnAnimals;
    }

    /**
     * @return the autoSave
     */
    public boolean isAutoSave() {
        return autoSave;
    }

    /**
     * @return the keepSpawnLoaded
     */
    public boolean isKeepSpawnLoaded() {
        return keepSpawnLoaded;
    }

    /**
     * @return the difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return the maxSize
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @return the lastRenderedPosition
     */
    public Point getLastRenderedPosition() {
        return lastRenderedPosition;
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
    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    /**
     * @param worldSpawn
     *            the worldSpawn to set
     */
    public void setWorldSpawn(Location worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    /**
     * @param spawnMonsters
     *            the spawnMonsters to set
     */
    public void setSpawnMonsters(boolean spawnMonsters) {
        this.spawnMonsters = spawnMonsters;
    }

    /**
     * @param spawnAnimals
     *            the spawnAnimals to set
     */
    public void setSpawnAnimals(boolean spawnAnimals) {
        this.spawnAnimals = spawnAnimals;
    }

    /**
     * @param autoSave
     *            the autoSave to set
     */
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * @param keepSpawnLoaded
     *            the keepSpawnLoaded to set
     */
    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.keepSpawnLoaded = keepSpawnLoaded;
    }

    /**
     * @param difficulty
     *            the difficulty to set
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @param maxSize
     *            the maxSize to set
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @param lastRenderedPosition
     *            the lastRenderedPosition to set
     */
    public void setLastRenderedPosition(Point lastRenderedPosition) {
        this.lastRenderedPosition = lastRenderedPosition;
    }
}