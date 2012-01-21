package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class StopCommand extends Command {
    public StopCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Stopt den Renderthread";
    }

    public void execute(String[] args, CommandSender sender) {
        if (Main.chunkThread == null) {
            ChatUtils.printError(sender, this.pluginName, "Es existiert kein Thread!");
        } else {
            Bukkit.getServer().getScheduler().cancelTask(Main.chunkThread.getTaskID());
            Main.chunkThread.saveConfig();
            Main.chunkThread = null;
            ChatUtils.printSuccess(sender, this.pluginName, "Thread angehalten!");
        }
    }
}