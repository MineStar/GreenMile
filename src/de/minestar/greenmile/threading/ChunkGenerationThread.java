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

import org.bukkit.Bukkit;
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
    private String lastStatus = "";
    private int TaskID = -1;

    public ChunkGenerationThread(int worldSize, String worldName) {
        this.world = Bukkit.getServer().getWorld(worldName);
        this.worldSizeInBlocks = worldSize;
        this.worldSizeInChunks = (int) (worldSizeInBlocks / 16) + BorderSizeInChunks;

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
        this.lastRenderedChunk = new Point(maxVars.x, maxVars.y);
        this.loadConfig();
    }

    public void setTaskID(int ID) {
        this.TaskID = ID;
    }

    public int getTaskID() {
        return this.TaskID;
    }

    public String getStatus() {
        return this.lastStatus;
    }

    private void loadConfig() {
        YamlConfiguration config = null;
        File dir = new File("plugins/GreenMile/worlds/");
        dir.mkdirs();

        File file = new File("plugins/GreenMile/worlds/" + this.world.getName() + ".yml");
        if (!file.exists())
            return;

        try {
            config = new YamlConfiguration();
            config.load(file);
            int lastX = config.getInt("lastRenderedChunk.X", this.maxVars.x);
            int lastZ = config.getInt("lastRenderedChunk.Y", this.maxVars.y);
            this.lastRenderedChunk = new Point(lastX, lastZ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        YamlConfiguration config = null;
        File file = new File("plugins/GreenMile/worlds/" + this.world.getName() + ".yml");
        if (file.exists())
            file.delete();

        try {
            config = new YamlConfiguration();
            config.set("lastRenderedChunk.X", lastRenderedChunk.x);
            config.set("lastRenderedChunk.Y", lastRenderedChunk.y + 1);
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
        if (lastRenderedChunk.y < minVars.y) {
            --lastRenderedChunk.x;
            lastRenderedChunk.y = maxVars.y;
        }

        if (lastRenderedChunk.x < minVars.x) {
            Main.printToConsole("############################################");
            lastStatus = "RENDERING OF WORLD '" + world.getName() + "' FINISHED!";
            Main.printToConsole(lastStatus);
            Main.printToConsole("############################################");

            // DELETE FILE
            File file = new File("plugins/GreenMile/worlds/" + this.world.getName() + ".yml");
            if (file.exists()) {
                if (!file.delete())
                    file.deleteOnExit();
                Main.printToConsole("Reset render position");
            }

            // CANCEL TASK
            Bukkit.getServer().getScheduler().cancelTask(this.TaskID);
            return true;
        }

        // CHUNK = NULL -> CHUNK EXISTS -> GO TO NEXT CHUNK
        if (world.getChunkAt(lastRenderedChunk.x, lastRenderedChunk.y) != null) {
            --lastRenderedChunk.y;
            return false;
        }

        lastStatus = "Rendering chunk: " + lastRenderedChunk.x + " / " + lastRenderedChunk.y;
        world.loadChunk(lastRenderedChunk.x, lastRenderedChunk.y);
        world.unloadChunk(lastRenderedChunk.x, lastRenderedChunk.y);
        Main.printToConsole(lastStatus);
        --lastRenderedChunk.y;

        return true;
    }
}
