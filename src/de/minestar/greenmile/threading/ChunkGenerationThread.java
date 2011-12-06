/*
 * Copyright (C) 2011 MineStar.de 
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

package de.minestar.greenmile.threading;

import java.awt.Point;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.Main;

public class ChunkGenerationThread implements Runnable {

    private World world = null;
    private int worldSizeInBlocks = 0;
    private int worldSizeInChunks = 0;
    private int BorderSizeInChunks = 20;
    private Point spawnChunk = null;
    private Point maxVars = null, minVars = null;
    private Point lastRenderedChunk = null;
    private int TaskID = -1;

    private long status = 0L;
    private long maxChunks;

    private List<Chunk> bufferChunks = new LinkedList<Chunk>();

    public ChunkGenerationThread(int worldSize, String worldName) {
        this.world = Bukkit.getServer().getWorld(worldName);
        this.worldSizeInBlocks = worldSize;
        this.worldSizeInChunks = (int) ((double) worldSizeInBlocks / 16.0) + BorderSizeInChunks;
        this.maxChunks = worldSizeInChunks * worldSizeInChunks * 4;

        if (world == null) {
            Main.printToConsole("############################################");
            Main.printToConsole("ERROR: World '" + worldName + "' was not found!");
            Main.printToConsole("############################################");
            return;
        }

        Location spawnLoc = world.getSpawnLocation();
        this.spawnChunk = new Point((int) (spawnLoc.getBlockX() / 16), (int) (spawnLoc.getBlockZ() / 16));
        this.maxVars = new Point(this.spawnChunk.x + this.worldSizeInChunks, this.spawnChunk.y + this.worldSizeInChunks);
        this.minVars = new Point(this.spawnChunk.x - this.worldSizeInChunks, this.spawnChunk.y - this.worldSizeInChunks);

        this.loadConfig();
        Main.printToConsole(status + " of " + maxChunks);
    }

    public void setTaskID(int ID) {
        this.TaskID = ID;
    }

    public int getTaskID() {
        return this.TaskID;
    }

    public long getStatsAsInt() {
        return status;
    }

    public String getStatus() {
        return ((status * 100) / maxChunks) + "%";
    }

    private void loadConfig() {
        File file = new File("plugins/GreenMile/worlds/", world.getName() + ".yml");
        if (!file.exists()) {
            lastRenderedChunk = new Point(maxVars.x, maxVars.y);
            return;
        }

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            int lastX = config.getInt("lastRenderedChunk.X", this.maxVars.x);
            int lastZ = config.getInt("lastRenderedChunk.Y", this.maxVars.y);
            this.lastRenderedChunk = new Point(lastX, lastZ);
            this.status = config.getInt("status", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        File file = new File("plugins/GreenMile/worlds/", world.getName() + ".yml");
        if (file.exists())
            file.delete();

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("lastRenderedChunk.X", lastRenderedChunk.x);
            config.set("lastRenderedChunk.Y", lastRenderedChunk.y + 1);
            config.set("status", status);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetRenderPosition() {
        File file = new File("plugins/GreenMile/worlds/", world.getName() + ".yml");
        if (!file.exists())
            return;

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            config.set("lastRenderedChunk.X", null);
            config.set("lastRenderedChunk.Y", null);
            config.set("status", 0);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        if (this.world == null) {
            return;
        }
        while (!renderChunk());
    }

    public boolean renderChunk() {
        ++status;
        if (lastRenderedChunk.y <= minVars.y) {
            --lastRenderedChunk.x;
            lastRenderedChunk.y = maxVars.y;
        }

        if (lastRenderedChunk.x <= minVars.x) {
            Main.printToConsole("############################################");
            Main.printToConsole("RENDERING OF WORLD '" + world.getName() + "' FINISHED!");
            Main.printToConsole("############################################");

            // RESET THREAD
            resetRenderPosition();

            // CANCEL TASK
            Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
            world.save();
            Main.chunkThread = null;

            return true;
        }

        // Remember kids : A loaded chunk is a generated chunk!
        if (world.isChunkLoaded(lastRenderedChunk.x, lastRenderedChunk.y)) {
            --lastRenderedChunk.y;
            return false;
        }
        world.loadChunk(lastRenderedChunk.x, lastRenderedChunk.y);
        bufferChunks.add(world.getChunkAt(lastRenderedChunk.x, lastRenderedChunk.y));

        // Remember kids : Only free memory is good memory!
        if (bufferChunks.size() > 50) {
            unloadChunks();
            Main.printToConsole(getStatus());
        }
        --lastRenderedChunk.y;
        return true;
    }

    /**
     * Unload all loaded chunks to free memory
     */
    private void unloadChunks() {
        for (Chunk c : bufferChunks)
            c.unload();
        bufferChunks.clear();
    }
}
