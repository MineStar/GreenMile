package de.minestar.greenmile.commands.gm;

import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SetSpawnCommand extends AbstractCommand {
    private WorldManager worldManager;

    public SetSpawnCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.NAME, syntax, arguments, node);
        this.description = "Set the spawn of the current world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {

        String worldName = player.getWorld().getName();
        if (!this.worldManager.worldExists(worldName)) {
            PlayerUtils.sendError(player, this.pluginName, "You need to import this world!");
            return;
        }
        GMWorld thisWorld = this.worldManager.getGMWorld(worldName);
        thisWorld.getWorldSettings().setWorldSpawn(player.getLocation());
        thisWorld.updateBukkitWorld();
        if (thisWorld.getWorldSettings().saveSettings(worldName, this.worldManager.getDataFolder()))
            PlayerUtils.sendSuccess(player, this.pluginName, "Spawn set.");
        else
            PlayerUtils.sendError(player, this.pluginName, "Error while saving settings.");
    }
}