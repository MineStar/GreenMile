package de.minestar.greenmile.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.EntityTeleportThread;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class GMPListener implements Listener {

    /**
     * ON PLAYER TELEPORT
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	Player player = event.getPlayer();
    	
        // workaround for players that are completetly new to the server
        MinestarPlayer msPlayer = MinestarCore.getPlayer(player);
        Boolean bool = msPlayer.getBoolean("main.wasHere");
        if (bool == null || !bool) {
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
            player.setGameMode(GameMode.SURVIVAL);
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
        	if (player.isInsideVehicle()) {
        		if (player.getVehicle() instanceof Animals) {
        			// get the animal
                    Entity entity = player.getVehicle();

                    // leave it
                    player.leaveVehicle();
        			
                    // load the chunk
                    worldSpawn.getChunk().load(true);

                    // teleport the animal
                    entity.teleport(worldSpawn);
                    
                    // create a Thread
                    EntityTeleportThread thread = new EntityTeleportThread(player.getName(), entity);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GreenMileCore.getPlugin(), thread, 10L);
        		}
        	}
        	event.setTo(worldSpawn.clone());      	
            PlayerUtils.sendError(player, GreenMileCore.NAME, "Du hast die maximale Grenze der Map erreicht!");
            PlayerUtils.sendInfo(player, GreenMileCore.NAME, "Du wurdest zum Spawn zurück teleportiert!");
        }
    }
}
