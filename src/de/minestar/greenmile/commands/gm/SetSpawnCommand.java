package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class SetSpawnCommand extends Command {
    private WorldManager worldManager;

    public SetSpawnCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Set the spawn of the current world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, CommandSender sender) {
        if (!(sender instanceof Player)) {
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "This is only an ingamecommand!");
            return;
        }

        Player player = (Player) sender;
        String worldName = player.getWorld().getName();
        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(player, this.pluginName, "You need to import this world!");
            return;
        }
        GMWorld thisWorld = this.worldManager.getGMWorld(worldName);
        thisWorld.getWorldSettings().setWorldSpawn(player.getLocation());
        thisWorld.updateBukkitWorld();
        if (thisWorld.getWorldSettings().saveSettings(worldName, this.worldManager.getDataFolder()))
            ChatUtils.printSuccess(player, this.pluginName, "Spawn set.");
        else
            ChatUtils.printError(player, this.pluginName, "Error while saving settings.");
    }
}