package de.minestar.greenmile.commands.gm;

import org.bukkit.entity.Player;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SetSpawnCommand extends AbstractCommand {

    private WorldManager worldManager;

    public SetSpawnCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(GreenMileCore.NAME, syntax, arguments, node);
        this.description = "Set the spawn of the current world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {

        String worldName = player.getWorld().getName();
        GMWorld thisWorld = worldManager.getGMWorld(worldName);
        if (thisWorld == null) {
            PlayerUtils.sendError(player, pluginName, "You need to import this world!");
            return;
        }

        thisWorld.getWorldSettings().setWorldSpawn(player.getLocation());
        thisWorld.updateBukkitWorld();
        if (thisWorld.getWorldSettings().saveSettings(worldName, worldManager.getDataFolder()))
            PlayerUtils.sendSuccess(player, pluginName, "Spawn set.");
        else
            PlayerUtils.sendError(player, pluginName, "Error while saving settings.");
    }
}