package de.minestar.greenmile.worlds;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class WorldManager {
    private ArrayList<GMWorld> worldList;
    private File dataFolder;

    /**
     * Constructor
     * 
     * @param dataFolder
     */
    public WorldManager(File dataFolder) {
        this.dataFolder = dataFolder;
        loadWorlds();
    }

    /**
     * ADD A WORLD TO THE WORLDLIST
     * 
     * @param world
     */
    private void addWorld(GMWorld world) {
        if (worldExists(world.getWorldName())) {
            ChatUtils.printConsoleWarning("World '" + world.getWorldName() + "' already exists!", Main.name);
            return;
        }
        this.worldList.add(world);
    }

    /**
     * CREATE GMWORLD AND BUKKITWORLD
     * 
     * @param worldName
     * @param environment
     * @param seed
     * @param dataFolder
     * @return
     */
    public boolean createWorld(String worldName, World.Environment environment, long seed, File dataFolder) {
        if (worldExists(worldName)) {
            return false;
        }
        GMWorld newWorld = new GMWorld(worldName, dataFolder);
        boolean result = GMWorld.loadOrCreateBukkitWorld(worldName, environment, seed);
        if (result) {
            newWorld.createSettings(seed, environment, this.dataFolder);
            addWorld(newWorld);
        }
        return result;
    }

    /**
     * IMPORT WORLD
     * 
     * @param worldName
     * @return<b>true</b> if the import was successful, otherwise <b>false</b>
     */
    public boolean importWorld(String worldName) {
        // WE CAN ONLY IMPORT, IF THE WORLD IS NOT COVERED BY GREENMILE
        if (worldExists(worldName)) {
            return false;
        }

        // WE CAN ONLY IMPORT, IF THE BUKKITWORLD IS FOUND
        World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) {
            return false;
        }

        // FINALLY IMPORT THE WORLD
        GMWorld newWorld = new GMWorld(worldName, this.dataFolder);
        newWorld.createSettings(bukkitWorld.getSeed(), bukkitWorld.getEnvironment(), this.dataFolder);
        newWorld.updateBukkitWorld();
        addWorld(newWorld);
        return true;
    }

    /**
     * Check if GMWorld exists (by worldName)
     * 
     * @param worldName
     * @return <b>true</b> if the world exists, otherwise <b>false</b>
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
     * Check if GMWorld exists (by block)
     * 
     * @param worldName
     * @return <b>true</b> if the world exists, otherwise <b>false</b>
     */
    public boolean worldExists(Block block) {
        return worldExists(block.getWorld().getName());
    }

    /**
     * Check if GMWorld exists (by player)
     * 
     * @param worldName
     * @return <b>true</b> if the world exists, otherwise <b>false</b>
     */
    public boolean worldExists(Player player) {
        return worldExists(player.getWorld().getName());
    }

    /**
     * Get the GMWorld by the worldname
     * 
     * @param worldName
     * @return the GMWorld, or null if not found
     */
    public GMWorld getGMWorld(String worldName) {
        for (GMWorld world : this.worldList) {
            if (world.getWorldName().equals(worldName)) {
                return world;
            }
        }
        return null;
    }

    /**
     * Get the GMWorld by a block
     * 
     * @param worldName
     * @return the GMWorld, or null if not found
     */
    public GMWorld getGMWorld(Block block) {
        return getGMWorld(block.getWorld().getName());
    }

    /**
     * Get the GMWorld by a player
     * 
     * @param worldName
     * @return the GMWorld, or null if not found
     */
    public GMWorld getGMWorld(Player player) {
        return getGMWorld(player.getWorld().getName());
    }

    /**
     * LOAD WORLDS
     */
    private void loadWorlds() {
        this.worldList = new ArrayList<GMWorld>();
        try {
            File[] files = this.dataFolder.listFiles();
            for (File f : files) {
                String fileName = f.getName().trim();
                // FILESYNTAX: config_%worldname%.yml
                if ((fileName.endsWith(".yml")) && (fileName.startsWith("config_"))) {
                    // GET WORLDNAME
                    String worldName = fileName.replace("config_", "");
                    worldName = worldName.substring(0, worldName.length() - 4);
                    // CREATE GMWORLD
                    GMWorld world = new GMWorld(worldName, this.dataFolder);
                    world.loadSettings(this.dataFolder);
                    if (!world.getWorldSettings().isInitialized())
                        continue;

                    if (GMWorld.loadOrCreateBukkitWorld(worldName, world.getWorldSettings().getEnvironment(), world.getWorldSettings().getLevelSeed())) {
                        // ADD WORLD TO WORLDLIST
                        addWorld(world);
                        // LOAD WORLDSPECIFIC SETTINGS
                        world.getWorldSettings().loadWorldSettings(worldName, this.getDataFolder());
                        // UPDATE BUKKIT WORLD
                        world.updateBukkitWorld();
                    }
                }
            }
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load worlds from " + this.dataFolder, Main.name);
        }
    }

    /**
     * @return the DataFolder
     */
    public File getDataFolder() {
        return this.dataFolder;
    }

    /**
     * @return the Worldlist
     */
    public ArrayList<GMWorld> getWorldList() {
        return this.worldList;
    }
}