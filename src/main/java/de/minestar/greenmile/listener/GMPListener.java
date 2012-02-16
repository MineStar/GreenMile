package de.minestar.greenmile.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class GMPListener implements Listener {
    private final WorldManager worldManager;

    /**
     * Constructor
     * 
     * @param worldManager
     */
    public GMPListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    /**
     * ON PLAYER TELEPORT
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        String worldName = event.getTo().getWorld().getName();

        // IF WORLD IS NOT COVERED BY GREENMILE => RETURN
        if (!this.worldManager.worldExists(worldName)) {
            return;
        }

        // GET CURRENT SPAWN AND MAXIMUM SIZE OF THE WORLD
        Location worldSpawn = this.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();
        int maxSize = this.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize();

        // CHECK IF THE TO-LOCATION IS INSIDE OF THE BORDER
        if ((!BorderThread.isInside(event.getTo().getBlockX(), event.getTo().getBlockZ(), worldSpawn.getBlockX(), worldSpawn.getBlockZ(), maxSize)) && (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName()))) {
            event.setTo(worldSpawn.clone());
            Player player = event.getPlayer();
            PlayerUtils.sendError(player, Main.NAME, "Du hast die maximale Grenze der Map erreicht!");
            PlayerUtils.sendInfo(player, Main.NAME, "Du wurdest zum Spawn zurück teleportiert!");
        }
    }
}