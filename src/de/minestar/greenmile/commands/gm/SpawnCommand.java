package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.ExtendedCommand;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class SpawnCommand extends ExtendedCommand {
    private WorldManager worldManager;

    public SpawnCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Teleport to the given spawn";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, CommandSender sender) {
        if (!(sender instanceof Player)) {
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "This is only an ingamecommand!");
            return;
        }

        Player player = (Player) sender;

        String worldName = ((World) Bukkit.getServer().getWorlds().get(0)).getName();
        if (args.length > 0) {
            worldName = args[0];
        }

        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(sender, this.pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        if (UtilPermissions.playerCanUseCommand(player, "greenmile.spawn." + worldName)) {
            GMWorld thisWorld = this.worldManager.getGMWorld(worldName);
            player.teleport(thisWorld.getWorldSettings().getWorldSpawn());
            ChatUtils.printSuccess(sender, this.pluginName, "Welcome to the spawn of '" + worldName + "'!");
            return;
        }
        ChatUtils.printError(sender, this.pluginName, "You are not allowed to teleport to '" + worldName + "'!");
    }
}