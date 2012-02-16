package de.minestar.greenmile.commands.gm;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.AbstractSuperCommand;

public class GreenMileCommand extends AbstractSuperCommand {

    public GreenMileCommand(String syntax, String arguments, String node, AbstractCommand... subCommands) {
        super(Main.NAME, syntax, arguments, node, false, subCommands);
    }

    public void execute(String[] args, Player player) {
        // Do nothing
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        // Do nothing
    }
}