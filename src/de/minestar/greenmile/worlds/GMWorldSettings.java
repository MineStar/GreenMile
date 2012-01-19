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

    public GMWorldSettings(String worldName, long levelSeed, World.Environment environment, File dataFolder) {
        this.worldSpawn = Bukkit.getWorld(worldName).getSpawnLocation();
        this.levelSeed = levelSeed;
        this.environment = environment;
        this.initialized = saveSettings(worldName, dataFolder);
    }

    public GMWorldSettings(String worldName, File dataFolder) {
        this.initialized = loadSettings(worldName, dataFolder);
    }

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

    private boolean loadSettings(String worldName, File dataFolder) {
        try {
            File file = new File(dataFolder, "config_" + worldName + ".yml");

            if ((!file.exists()) && (!saveSettings(worldName, dataFolder))) {
                return false;
            }
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
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

    public boolean isInitialized() {
        return this.initialized;
    }

    public long getLevelSeed() {
        return this.levelSeed;
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public void setLevelSeed(long levelSeed) {
        this.levelSeed = levelSeed;
    }

    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    public boolean isSpawnMonsters() {
        return this.spawnMonsters;
    }

    public void setSpawnMonsters(boolean spawnMonsters) {
        this.spawnMonsters = spawnMonsters;
    }

    public boolean isSpawnAnimals() {
        return this.spawnAnimals;
    }

    public void setSpawnAnimals(boolean spawnAnimals) {
        this.spawnAnimals = spawnAnimals;
    }

    public boolean isAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isKeepSpawnLoaded() {
        return this.keepSpawnLoaded;
    }

    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.keepSpawnLoaded = keepSpawnLoaded;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Location getWorldSpawn() {
        return this.worldSpawn;
    }

    public void setWorldSpawn(Location worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    public Point getLastRenderedPosition() {
        return this.lastRenderedPosition;
    }

    public void setLastRenderedPosition(Point lastRenderedPosition) {
        this.lastRenderedPosition = lastRenderedPosition;
    }
}