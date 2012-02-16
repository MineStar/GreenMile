package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class StopCommand extends AbstractCommand {

    public StopCommand(String syntax, String arguments, String node) {
        super(Main.NAME, syntax, arguments, node);
        this.description = "Stopt den Renderthread";
    }

    public void execute(String[] args, Player player) {
        stopThread(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        stopThread(args, console);
    }

    private void stopThread(String[] args, CommandSender sender) {
        if (Main.chunkThread == null)
            ChatUtils.writeError(sender, pluginName, "Es existiert kein Thread!");
        else {
            Bukkit.getServer().getScheduler().cancelTask(Main.chunkThread.getTaskID());
            Main.chunkThread.saveConfig();
            Main.chunkThread = null;
            ChatUtils.writeError(sender, pluginName, "Thread angehalten!");
        }
    }
}