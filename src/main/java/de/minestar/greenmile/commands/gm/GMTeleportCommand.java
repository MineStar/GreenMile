package de.minestar.greenmile.commands.gm;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class GMTeleportCommand extends AbstractCommand {
    private WorldManager worldManager;

    public GMTeleportCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.name, syntax, arguments, node);
        this.description = "Teleport to a world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        String worldName = args[0];

        if (!worldManager.worldExists(worldName)) {
            PlayerUtils.sendError(player, pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        World bukkitWorld = worldManager.getGMWorld(worldName).getBukkitWorld();
        if (bukkitWorld == null) {
            PlayerUtils.sendError(player, pluginName, "Bukkitworld '" + worldName + "' does not exist!");
            return;
        }

        player.teleport(worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn());
        PlayerUtils.sendInfo(player, pluginName, "Welcome to '" + worldName + "'!");
    }
}