package de.minestar.greenmile.commands.gm;

import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.commands.SuperCommand;
import org.bukkit.entity.Player;

public class GreenMileCommand extends SuperCommand {
    public GreenMileCommand(String pluginName, String syntax, String arguments, String node, Command[] subCommands) {
        super(syntax, arguments, node, subCommands);
    }

    public void execute(String[] args, Player player) {
    }
}