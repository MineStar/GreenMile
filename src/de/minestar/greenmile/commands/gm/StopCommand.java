package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class StopCommand extends Command {
    public StopCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Stopt den Renderthread";
    }

    public void execute(String[] args, Player player) {
        if (Main.chunkThread == null) {
            ChatUtils.printError(player, this.pluginName, "Es existiert kein Thread!");
        } else {
            Bukkit.getServer().getScheduler().cancelTask(Main.chunkThread.getTaskID());
            Main.chunkThread.saveConfig();
            Main.chunkThread = null;
            ChatUtils.printSuccess(player, this.pluginName, "Thread angehalten!");
        }
    }
}