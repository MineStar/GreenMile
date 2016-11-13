package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.threading.EntityTeleportThread;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class GMTeleportCommand extends AbstractCommand {
    private WorldManager worldManager;

    public GMTeleportCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(GreenMileCore.NAME, syntax, arguments, node);
        this.description = "Teleport to a world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        String worldName = args[0];

        GMWorld gmWorld = worldManager.getGMWorld(worldName);
        if (gmWorld == null) {
            PlayerUtils.sendError(player, pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        World bukkitWorld = gmWorld.getBukkitWorld();
        if (bukkitWorld == null) {
            PlayerUtils.sendError(player, pluginName, "Bukkitworld '" + worldName + "' does not exist!");
            return;
        }

        if (player.isInsideVehicle()) {
    		if (player.getVehicle() instanceof Animals) {
    			// get the animal
                Entity entity = player.getVehicle();

                // leave it
                player.leaveVehicle();
    			
                // load the chunk
                gmWorld.getWorldSettings().getWorldSpawn().getChunk().load(true);

                // teleport the animal
                entity.teleport(gmWorld.getWorldSettings().getWorldSpawn());
                
                // create a Thread
                EntityTeleportThread thread = new EntityTeleportThread(player.getName(), entity);
                Bukkit.getScheduler().scheduleSyncDelayedTask(GreenMileCore.getPlugin(), thread, 10L);
    		}
    		else {
    			// get the Vehicle (Cart/Boat)
                Entity entity = player.getVehicle();
    			
                // leave it
                player.leaveVehicle();
                
                // destroy the vehicle
                entity.remove();
    		}
    	}
        
        player.teleport(gmWorld.getWorldSettings().getWorldSpawn());
        PlayerUtils.sendInfo(player, pluginName, "Welcome to '" + worldName + "'!");
    }
}