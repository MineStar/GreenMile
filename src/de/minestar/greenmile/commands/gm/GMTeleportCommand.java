package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.World;
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

    public void execute(String[] args, Player player) {
        String worldName = args[0];

        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(player, this.pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        World bukkitWorld = this.worldManager.getGMWorld(worldName).getBukkitWorld();
        if (bukkitWorld == null) {
            ChatUtils.printError(player, this.pluginName, "Bukkitworld '" + worldName + "' does not exist!");
            return;
        }

        player.teleport(bukkitWorld.getSpawnLocation());
        ChatUtils.printInfo(player, this.pluginName, ChatColor.AQUA, "Welcome to '" + worldName + "'!");
    }
}