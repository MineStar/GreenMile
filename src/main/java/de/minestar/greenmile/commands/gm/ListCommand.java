package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class ListCommand extends AbstractCommand {

    private WorldManager worldManager;

    public ListCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.name, syntax, arguments, node);
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        listWorlds(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        listWorlds(args, console);
    }

    private void listWorlds(String[] args, CommandSender sender) {

        if (worldManager.getWorldList().size() < 1) {
            ChatUtils.writeError(sender, pluginName, "GreenMile ueberwacht keine Welt!");
            return;
        }

        ChatUtils.writeColoredMessage(sender, pluginName, ChatColor.GOLD, "GreenMile ueberwacht folgende Welten:");
        for (GMWorld world : worldManager.getWorldList())
            ChatUtils.writeColoredMessage(sender, pluginName, ChatColor.GREEN, "'" + world.getWorldName() + "' : " + world.getWorldSettings().getMaxSize());
    }
}