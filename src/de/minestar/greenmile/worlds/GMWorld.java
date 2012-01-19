package de.minestar.greenmile.worlds;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMWorld {
    private String worldName;
    private GMWorldSettings settings;
    private GMWorldEventOptions eventSettings;

    public GMWorld(String worldName, File dataFolder) {
        this.worldName = worldName;
        this.eventSettings = new GMWorldEventOptions(worldName, dataFolder);
    }

    public void loadSettings(File dataFolder) {
        this.settings = new GMWorldSettings(getWorldName(), dataFolder);
    }

    public void createSettings(long levelSeed, World.Environment environment, File dataFolder) {
        this.settings = new GMWorldSettings(getWorldName(), levelSeed, environment, dataFolder);
    }

    public GMWorldSettings getWorldSettings() {
        return this.settings;
    }

    public void setWorldSettings(GMWorldSettings settings) {
        this.settings = settings;
    }

    public World getBukkitWorld() {
        System.out.println("worldname: " + this.worldName);
        return Bukkit.getServer().getWorld(this.worldName);
    }

    public static boolean createBukkitWorld(String worldName, World.Environment environment, long levelSeed) {
        if (Bukkit.getWorld(worldName) != null) {
            return false;
        }

        WorldCreator generator = new WorldCreator(worldName);
        generator.environment(environment);
        generator.seed(levelSeed);
        generator.createWorld();
        return true;
    }

    public boolean loadBukkitWorld() {
        if (this.settings == null) {
            loadSettings(Main.getInstance().getDataFolder());
        }

        if (!this.settings.isInitialized()) {
            return false;
        }

        WorldCreator generator = new WorldCreator(this.worldName);
        generator.environment(this.settings.getEnvironment());
        generator.seed(this.settings.getLevelSeed());
        generator.createWorld();

        return true;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public GMWorldEventOptions getEventSettings() {
        return this.eventSettings;
    }

    public void updateBukkitWorld() {
        World bukkitWorld = Bukkit.getServer().getWorld(this.worldName);
        if (bukkitWorld == null) {
            ChatUtils.printConsoleError("Can't access world '" + this.worldName + "'!", Main.name);
            return;
        }

        bukkitWorld.setSpawnFlags(this.settings.isSpawnMonsters(), this.settings.isSpawnAnimals());
        bukkitWorld.setAutoSave(this.settings.isAutoSave());
        bukkitWorld.setDifficulty(this.settings.getDifficulty());
        bukkitWorld.setKeepSpawnInMemory(this.settings.isKeepSpawnLoaded());
        bukkitWorld.setSpawnLocation(this.settings.getWorldSpawn().getBlockX(), this.settings.getWorldSpawn().getBlockY(), this.settings.getWorldSpawn().getBlockZ());
    }
}
/*
 * Location: C:\Users\Chris\Desktop\GreenMile.jar Qualified Name:
 * de.minestar.greenmile.worlds.GMWorld JD-Core Version: 0.6.0
 */