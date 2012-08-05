package de.minestar.greenmile.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class GMPListener implements Listener {

    /**
     * ON PLAYER TELEPORT
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {

        // workaround for players that are completetly new to the server
        MinestarPlayer msPlayer = MinestarCore.getPlayer(event.getPlayer());
        if (msPlayer.getBoolean("main.wasHere") == null) {
            msPlayer.setBoolean("main.wasHere", true);

            // get the world
            String worldName = event.getTo().getWorld().getName();

            // IF WORLD IS NOT COVERED BY GREENMILE => RETURN
            if (!GreenMileCore.worldManager.worldExists(worldName)) {
                return;
            }

            // GET CURRENT SPAWN AND MAXIMUM SIZE OF THE WORLD
            Location worldSpawn = GreenMileCore.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();
            event.setTo(worldSpawn.clone());
            return;
        }
        // END: workaround

        String worldName = event.getTo().getWorld().getName();

        // IF WORLD IS NOT COVERED BY GREENMILE => RETURN
        if (!GreenMileCore.worldManager.worldExists(worldName)) {
            return;
        }

        // GET CURRENT SPAWN AND MAXIMUM SIZE OF THE WORLD
        Location worldSpawn = GreenMileCore.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();
        int maxSize = GreenMileCore.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize();

        // CHECK IF THE TO-LOCATION IS INSIDE OF THE BORDER
        if ((!BorderThread.isInside(event.getTo().getBlockX(), event.getTo().getBlockZ(), worldSpawn.getBlockX(), worldSpawn.getBlockZ(), maxSize)) && (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName()))) {
            event.setTo(worldSpawn.clone());
            Player player = event.getPlayer();
            PlayerUtils.sendError(player, GreenMileCore.NAME, "Du hast die maximale Grenze der Map erreicht!");
            PlayerUtils.sendInfo(player, GreenMileCore.NAME, "Du wurdest zum Spawn zurück teleportiert!");
        }
    }
}