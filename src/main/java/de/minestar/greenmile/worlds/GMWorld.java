package de.minestar.greenmile.worlds;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import de.minestar.greenmile.Main;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class GMWorld {
    private String worldName;
    private GMWorldSettings worldSettings;
    private GMWorldEventOptions eventSettings;

    /**
     * Constructor
     * 
     * @param worldName
     * @param dataFolder
     */
    public GMWorld(String worldName, File dataFolder) {
        this.worldName = worldName;
        this.eventSettings = new GMWorldEventOptions(worldName, dataFolder);
    }

    /**
     * LOAD SETTINGS
     * 
     * @param dataFolder
     */
    public void loadSettings(File dataFolder) {
        this.worldSettings = new GMWorldSettings(getWorldName(), dataFolder);
    }

    /**
     * CREATE SETTINGS
     * 
     * @param levelSeed
     * @param environment
     * @param dataFolder
     */
    public void createSettings(long levelSeed, Environment environment, File dataFolder) {
        this.worldSettings = new GMWorldSettings(getWorldName(), levelSeed, environment, dataFolder);
        this.eventSettings.saveSettings();
    }

    /**
     * GET BUKKITWORLD
     * 
     * @return the bukkitworld, of null if not found
     */
    public World getBukkitWorld() {
        return Bukkit.getServer().getWorld(this.worldName);
    }

    public static boolean loadOrCreateBukkitWorld(String worldName, World.Environment environment, long levelSeed) {
        // GENERATE THE WORLD
        try {
            WorldCreator generator = new WorldCreator(worldName);
            generator.environment(environment);
            generator.seed(levelSeed);
            generator.createWorld();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return the worldname
     */
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * @return the eventSettings
     */
    public GMWorldEventOptions getEventSettings() {
        return this.eventSettings;
    }

    /**
     * UPDATE BUKKITWORLD
     */
    public void updateBukkitWorld() {
        // IS THERE A BUKKIT WORLD? IF NOT => RETURN
        World bukkitWorld = Bukkit.getServer().getWorld(this.worldName);
        if (bukkitWorld == null) {
            ConsoleUtils.printError(Main.NAME, "Can't find bukkitworld '" + this.worldName + "'!");
            return;
        }

        // UPDATE THE SETTINGS
        bukkitWorld.setSpawnFlags(this.worldSettings.isSpawnMonsters(), this.worldSettings.isSpawnAnimals());
        bukkitWorld.setAutoSave(this.worldSettings.isAutoSave());
        bukkitWorld.setDifficulty(this.worldSettings.getDifficulty());
        bukkitWorld.setKeepSpawnInMemory(this.worldSettings.isKeepSpawnLoaded());
        bukkitWorld.setSpawnLocation(this.worldSettings.getWorldSpawn().getBlockX(), this.worldSettings.getWorldSpawn().getBlockY(), this.worldSettings.getWorldSpawn().getBlockZ());
    }

    /**
     * @return the worldSettings
     */
    public GMWorldSettings getWorldSettings() {
        return worldSettings;
    }
}
