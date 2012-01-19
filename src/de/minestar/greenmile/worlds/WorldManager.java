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

    public WorldManager(File dataFolder) {
        this.dataFolder = dataFolder;
        loadWorlds();
    }

    public void addWorld(GMWorld world) {
        if (worldExists(world.getWorldName())) {
            ChatUtils.printConsoleWarning("World '" + world.getWorldName() + "' already exists!", Main.name);
            return;
        }
        this.worldList.add(world);
    }

    public boolean createWorld(String worldName, World.Environment environment, long seed, File dataFolder) {
        if (worldExists(worldName)) {
            return false;
        }
        GMWorld newWorld = new GMWorld(worldName, dataFolder);
        boolean result = GMWorld.createBukkitWorld(worldName, environment, seed);
        newWorld.createSettings(seed, environment, this.dataFolder);
        addWorld(newWorld);
        return result;
    }

    public boolean importWorld(String worldName) {
        if (worldExists(worldName)) {
            return false;
        }

        World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) {
            return false;
        }

        GMWorld newWorld = new GMWorld(worldName, this.dataFolder);
        newWorld.createSettings(bukkitWorld.getSeed(), bukkitWorld.getEnvironment(), this.dataFolder);
        newWorld.loadBukkitWorld();
        newWorld.updateBukkitWorld();
        addWorld(newWorld);
        return true;
    }

    public boolean worldExists(String worldName) {
        for (GMWorld world : this.worldList) {
            if (world.getWorldName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }

    public boolean worldExists(Block block) {
        return worldExists(block.getWorld().getName());
    }

    public boolean worldExists(Player player) {
        return worldExists(player.getWorld().getName());
    }

    public GMWorld getGMWorld(String worldName) {
        for (GMWorld world : this.worldList) {
            if (world.getWorldName().equals(worldName)) {
                return world;
            }
        }
        return null;
    }

    public GMWorld getGMWorld(Block block) {
        return getGMWorld(block.getWorld().getName());
    }

    public GMWorld getGMWorld(Player player) {
        return getGMWorld(player.getWorld().getName());
    }

    private void loadWorlds() {
        this.worldList = new ArrayList<GMWorld>();
        try {
            File[] files = this.dataFolder.listFiles();
            for (File f : files) {
                String fileName = f.getName().trim();
                if ((fileName.endsWith(".yml")) && (fileName.startsWith("config_"))) {
                    String worldName = fileName.replace("config_", "");
                    worldName = worldName.substring(0, worldName.length() - 4);
                    GMWorld world = new GMWorld(worldName, this.dataFolder);
                    world.loadSettings(this.dataFolder);
                    if ((!world.getWorldSettings().isInitialized()) || (!world.loadBukkitWorld()))
                        continue;
                    addWorld(world);
                    world.updateBukkitWorld();
                }
            }
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load worlds from " + this.dataFolder, Main.name);
        }
    }

    public File getDataFolder() {
        return this.dataFolder;
    }

    public ArrayList<GMWorld> getWorldList() {
        return this.worldList;
    }
}