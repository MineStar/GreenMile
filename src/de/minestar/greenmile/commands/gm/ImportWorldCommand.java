package de.minestar.greenmile.commands.gm;

import com.bukkit.gemo.utils.ChatUtils;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ImportWorldCommand extends Command {
    private WorldManager worldManager;

    public ImportWorldCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Create a new world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        String worldName = args[0];

        if (this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(player, this.pluginName, "This world does already exist!");
            return;
        }

        if (Bukkit.getWorld(worldName) == null) {
            ChatUtils.printError(player, this.pluginName, "This bukkitworld does not exist!");
            return;
        }

        boolean result = this.worldManager.importWorld(worldName);

        if (result) {
            ChatUtils.printSuccess(player, this.pluginName, "World '" + worldName + "' imported!");
        } else {
            ChatUtils.printError(player, this.pluginName, "Error while importing world '" + worldName + "'!");
            ChatUtils.printInfo(player, this.pluginName, ChatColor.GRAY, "There was an internal error while importing the worldsettings.");
        }
    }
}