package de.minestar.greenmile.threading;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.GMWorldSettings;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class ChunkGenerationThread implements Runnable {
    private World world = null;
    private int worldSizeInBlocks = 0;
    private int worldSizeInChunks = 0;
    private int BorderSizeInChunks = 20;
    private Point spawnChunk = null;
    private Point maxVars = null;
    private Point minVars = null;
    private Point lastRenderedChunk = null;
    private int TaskID = -1;
    private WorldManager worldManager;
    private long status = 0L;
    private long maxChunks;
    private List<Chunk> bufferChunks = new LinkedList<Chunk>();

    public ChunkGenerationThread(int worldSize, String worldName, WorldManager worldManager) {
        this.world = Bukkit.getServer().getWorld(worldName);
        this.worldSizeInBlocks = worldSize;
        this.worldSizeInChunks = ((int) (this.worldSizeInBlocks / 16.0D) + this.BorderSizeInChunks);
        this.maxChunks = (this.worldSizeInChunks * this.worldSizeInChunks * 4);
        this.worldManager = worldManager;

        if (this.world == null) {
            ConsoleUtils.printError(Main.NAME, "############################################");
            ConsoleUtils.printError(Main.NAME, "World '" + worldName + "' was not found!");
            ConsoleUtils.printError(Main.NAME, "############################################");
            return;
        }

        Location spawnLoc = this.world.getSpawnLocation();
        this.spawnChunk = new Point(spawnLoc.getBlockX() / 16, spawnLoc.getBlockZ() / 16);
        this.maxVars = new Point(this.spawnChunk.x + this.worldSizeInChunks, this.spawnChunk.y + this.worldSizeInChunks);
        this.minVars = new Point(this.spawnChunk.x - this.worldSizeInChunks, this.spawnChunk.y - this.worldSizeInChunks);
        this.status = 0L;

        loadConfig();

        ConsoleUtils.printInfo(Main.NAME, this.status + " of " + this.maxChunks);
    }

    public void setTaskID(int ID) {
        this.TaskID = ID;
    }

    public int getTaskID() {
        return this.TaskID;
    }

    public long getStatsAsInt() {
        return this.status;
    }

    public String getStatus() {
        return this.status * 100L / this.maxChunks + "%";
    }

    public void run() {
        if (this.world == null) {
            return;
        }
        while (!renderChunk());
    }

    public boolean renderChunk() {
        this.status += 1L;
        if (this.lastRenderedChunk.y <= this.minVars.y) {
            this.lastRenderedChunk.x -= 1;
            this.lastRenderedChunk.y = this.maxVars.y;
        }

        if (this.lastRenderedChunk.x <= this.minVars.x) {
            ConsoleUtils.printInfo(Main.NAME, "############################################");
            ConsoleUtils.printInfo(Main.NAME, "RENDERING OF WORLD '" + this.world.getName() + "' FINISHED!");
            ConsoleUtils.printInfo(Main.NAME, "############################################");

            Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
            this.world.save();
            Main.chunkThread = null;

            resetConfig();
            return true;
        }

        if (this.world.isChunkLoaded(this.lastRenderedChunk.x, this.lastRenderedChunk.y)) {
            this.lastRenderedChunk.y -= 1;
            return false;
        }
        this.world.loadChunk(this.lastRenderedChunk.x, this.lastRenderedChunk.y);
        this.bufferChunks.add(this.world.getChunkAt(this.lastRenderedChunk.x, this.lastRenderedChunk.y));

        if (this.bufferChunks.size() > 50) {
            // unloadChunks();
            ConsoleUtils.printInfo(Main.NAME, getStatus());
        }
        this.lastRenderedChunk.y -= 1;
        return true;
    }

    private void resetConfig() {
        GMWorldSettings settings = this.worldManager.getGMWorld(this.world.getName()).getWorldSettings();
        settings.setLastRenderedPosition(null);
        settings.saveSettings(this.world.getName(), this.worldManager.getDataFolder());
    }

    private void loadConfig() {
        GMWorldSettings settings = this.worldManager.getGMWorld(this.world.getName()).getWorldSettings();
        if (settings.getLastRenderedPosition() != null)
            this.lastRenderedChunk = settings.getLastRenderedPosition();
        else
            this.lastRenderedChunk = ((Point) this.maxVars.clone());
    }

    public void saveConfig() {
        GMWorldSettings settings = this.worldManager.getGMWorld(this.world.getName()).getWorldSettings();
        settings.setLastRenderedPosition(this.lastRenderedChunk);
        settings.saveSettings(this.world.getName(), this.worldManager.getDataFolder());
    }

//    private void unloadChunks() {
//        for (Chunk c : this.bufferChunks)
//            c.unload();
//        this.bufferChunks.clear();
//    }
}