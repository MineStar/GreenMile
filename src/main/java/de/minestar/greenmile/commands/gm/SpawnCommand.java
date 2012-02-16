package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class SpawnCommand extends AbstractExtendedCommand {

    private WorldManager worldManager;

    public SpawnCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.NAME, syntax, arguments, node);
        this.description = "Teleport to the given spawn";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {

        String worldName = null;
        if (args.length > 0)
            worldName = args[0];
        else
            worldName = Bukkit.getWorlds().get(0).getName();

        GMWorld world = worldManager.getGMWorld(worldName);
        if (world == null) {
            PlayerUtils.sendError(player, pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        if (UtilPermissions.playerCanUseCommand(player, "greenmile.spawn." + worldName)) {
            player.teleport(world.getWorldSettings().getWorldSpawn());
            PlayerUtils.sendSuccess(player, pluginName, "Welcome to the spawn of '" + worldName + "'!");
        } else
            PlayerUtils.sendError(player, pluginName, "You are not allowed to teleport to '" + worldName + "'!");
    }
}