package de.minestar.greenmile.worlds;

import java.awt.Point;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.helper.EnumHelper;
import de.minestar.minstarlibrary.utils.ChatUtils;

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
        this.worldSpawn = Bukkit.getWorld(worldName).getSpawnLocation();
        this.levelSeed = levelSeed;
        this.environment = environment;
        this.initialized = saveSettings(worldName, dataFolder);
    }

    /**
     * This constructor is used when a world is loaded
     * 
     * @param worldName
     * @param dataFolder
     */
    public GMWorldSettings(String worldName, File dataFolder) {
        this.initialized = loadSettings(worldName, dataFolder);
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

        if (file.exists())
            file.delete();
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("settings.levelSeed", Long.valueOf(this.levelSeed));
            config.set("settings.environment", this.environment.toString());
            config.set("settings.spawnMonsters", Boolean.valueOf(this.spawnMonsters));
            config.set("settings.spawnAnimals", Boolean.valueOf(this.spawnAnimals));
            config.set("settings.difficulty", this.difficulty.toString());
            config.set("settings.autoSave", Boolean.valueOf(this.autoSave));
            config.set("settings.keepSpawnLoaded", Boolean.valueOf(this.keepSpawnLoaded));
            config.set("settings.maxSize", Integer.valueOf(this.maxSize));
            config.set("settings.spawn.x", Double.valueOf(this.worldSpawn.getX()));
            config.set("settings.spawn.y", Double.valueOf(this.worldSpawn.getY()));
            config.set("settings.spawn.z", Double.valueOf(this.worldSpawn.getZ()));
            config.set("settings.spawn.pitch", Float.valueOf(this.worldSpawn.getPitch()));
            config.set("settings.spawn.yaw", Float.valueOf(this.worldSpawn.getYaw()));
            if (this.lastRenderedPosition != null) {
                config.set("render.x", Integer.valueOf(this.lastRenderedPosition.x));
                config.set("render.y", Integer.valueOf(this.lastRenderedPosition.y));
            } else {
                config.set("lastRenderedChunk.x", "NULL");
                config.set("lastRenderedChunk.y", "NULL");
            }
            config.save(file);
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't save settings for world " + worldName + "!", Main.name);
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
    private boolean loadSettings(String worldName, File dataFolder) {
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
            Difficulty difficulty = EnumHelper.getDifficulty(config.getString("settings.difficulty", this.difficulty.toString()));
            if (environment == null) {
                throw new Exception("Environment not found!");
            }
            if (difficulty == null) {
                throw new Exception("Difficulty not found!");
            }

            setLevelSeed(config.getLong("settings.levelSeed", this.levelSeed));
            setEnvironment(environment);
            setSpawnMonsters(config.getBoolean("settings.spawnMonsters", this.spawnMonsters));
            setSpawnAnimals(config.getBoolean("settings.spawnAnimals", this.spawnAnimals));
            setAutoSave(config.getBoolean("settings.autoSave", this.autoSave));
            setKeepSpawnLoaded(config.getBoolean("settings.keepSpawnLoaded", this.keepSpawnLoaded));
            setDifficulty(difficulty);
            setMaxSize(config.getInt("settings.maxSize", this.maxSize));
            setWorldSpawn(new Location(Bukkit.getWorld(worldName), config.getDouble("settings.spawn.x", 0.0D), config.getDouble("settings.spawn.y", 128.0D), config.getDouble("settings.spawn.z", 0.0D), (float) config.getDouble("settings.spawn.yaw", 0.0D), (float) config.getDouble("settings.spawn.pitch", 0.0D)));

            if (!config.getString("lastRenderedChunk.x").equalsIgnoreCase("NULL")) {
                setLastRenderedPosition(new Point(config.getInt("lastRenderedChunk.x"), config.getInt("lastRenderedChunk.y")));
            }

            ChatUtils.printConsoleInfo("World '" + worldName + "' loaded!\nlevelSeed = " + this.levelSeed + "\nEnvironment = " + environment.toString() + "\nSpawnMonster = " + this.spawnMonsters + "\nSpawnAnimals = " + this.spawnAnimals + "\nAutoSave = " + this.autoSave + "\nKeepSpawnLoaded = " + this.keepSpawnLoaded + "\nDifficulty = " + difficulty.toString() + "\nMaxSize = " + this.maxSize, Main.name);
            return true;
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load settings for world " + worldName + "!", Main.name);
        }
        return false;
    }

    /**
     * isInitzialized()
     * 
     * @return <b>true</b> if initialization was successful, otherwise
     *         <b>false</b>
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