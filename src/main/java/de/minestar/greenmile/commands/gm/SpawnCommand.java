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

        String worldName = Bukkit.getWorlds().get(0).getName();
        if (args.length > 0) {
            worldName = args[0];
        }

        if (!this.worldManager.worldExists(worldName)) {
            PlayerUtils.sendError(player, this.pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        if (UtilPermissions.playerCanUseCommand(player, "greenmile.spawn." + worldName)) {
            GMWorld thisWorld = this.worldManager.getGMWorld(worldName);
            player.teleport(thisWorld.getWorldSettings().getWorldSpawn());
            PlayerUtils.sendSuccess(player, this.pluginName, "Welcome to the spawn of '" + worldName + "'!");
            return;
        }
        PlayerUtils.sendError(player, this.pluginName, "You are not allowed to teleport to '" + worldName + "'!");
    }
}