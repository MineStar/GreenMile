package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class ImportWorldCommand extends Command {
    private WorldManager worldManager;

    public ImportWorldCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Create a new world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, CommandSender sender) {
        String worldName = args[0];

        if (this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(sender, this.pluginName, "This world does already exist!");
            return;
        }

        if (Bukkit.getWorld(worldName) == null) {
            ChatUtils.printError(sender, this.pluginName, "This bukkitworld does not exist!");
            return;
        }

        boolean result = this.worldManager.importWorld(worldName);

        if (result) {
            ChatUtils.printSuccess(sender, this.pluginName, "World '" + worldName + "' imported!");
        } else {
            ChatUtils.printError(sender, this.pluginName, "Error while importing world '" + worldName + "'!");
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "There was an internal error while importing the worldsettings.");
        }
    }
}