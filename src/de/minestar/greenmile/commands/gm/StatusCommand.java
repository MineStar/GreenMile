package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class StatusCommand extends Command {
    public StatusCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Zeigt Status des Threads an";
    }

    public void execute(String[] args, CommandSender sender) {
        if (Main.chunkThread == null)
            ChatUtils.printError(sender, this.pluginName, "Es existiert kein Thread!");
        else
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "Status: " + Main.chunkThread.getStatus());
    }
}