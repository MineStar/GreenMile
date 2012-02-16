package de.minestar.greenmile.threading;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class BorderThread implements Runnable {
    private HashMap<String, Location> lastPosition = new HashMap<String, Location>();
    private final WorldManager worldManager;

    public BorderThread(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    public void run() {
        Location loc = null;

        int xP = 0;
        int zP = 0;

        int xW = 0;
        int zW = 0;

        int maxSize = 0;

        String worldName = null;
        for (World world : Bukkit.getWorlds()) {
            worldName = world.getName();

            if (!this.worldManager.worldExists(worldName)) {
                continue;
            }

            loc = this.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();

            xW = loc.getBlockX();
            zW = loc.getBlockZ();

            maxSize = this.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize();

            for (Player player : Bukkit.getWorld(worldName).getPlayers()) {
                if ((player.isDead()) || (!player.isOnline())) {
                    continue;
                }
                loc = player.getLocation();
                xP = loc.getBlockX();
                zP = loc.getBlockZ();

                if (!isInside(xP, zP, xW, zW, maxSize)) {
                    loc = (Location) this.lastPosition.get(player.getName());
                    if ((loc == null) || (!isInside(loc.getBlockX(), loc.getBlockZ(), xW, zW, maxSize))) {
                        loc = this.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();
                    }
                    player.teleport(loc);
                    PlayerUtils.sendError(player, Main.name, "Du hast die maximale Grenze der Map erreicht!");
                } else {
                    this.lastPosition.put(player.getName(), loc);
                }
            }
        }
    }

    public static boolean isInside(int x, int z, int x1, int z1, int width) {
        return (x < x1 + width) && (x > x1 - width) && (z < z1 + width) && (z > z1 - width);
    }
}