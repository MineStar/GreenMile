package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMTeleportCommand extends Command {
    private WorldManager worldManager;

    public GMTeleportCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Teleport to a world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, CommandSender sender) {
        String worldName = args[0];

        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(sender, this.pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        World bukkitWorld = this.worldManager.getGMWorld(worldName).getBukkitWorld();
        if (bukkitWorld == null) {
            ChatUtils.printError(sender, this.pluginName, "Bukkitworld '" + worldName + "' does not exist!");
            return;
        }

        player.teleport(this.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn());
        ChatUtils.printInfo(sender, this.pluginName, ChatColor.AQUA, "Welcome to '" + worldName + "'!");
    }
}