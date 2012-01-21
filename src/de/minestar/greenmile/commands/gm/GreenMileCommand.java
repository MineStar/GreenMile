package de.minestar.greenmile.commands.gm;

import org.bukkit.command.CommandSender;

import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.commands.SuperCommand;

public class GreenMileCommand extends SuperCommand {
    public GreenMileCommand(String pluginName, String syntax, String arguments, String node, Command[] subCommands) {
        super(syntax, arguments, node, subCommands);
    }

    public void execute(String[] args, CommandSender sender) {
    }
}