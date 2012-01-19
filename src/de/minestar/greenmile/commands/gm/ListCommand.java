package de.minestar.greenmile.commands.gm;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class ListCommand extends Command {
    private WorldManager worldManager;

    public ListCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        if (this.worldManager.getWorldList().size() < 1) {
            ChatUtils.printError(player, this.pluginName, "GreenMile ueberwacht keine Welt!");
            return;
        }

        ChatUtils.printInfo(player, this.pluginName, ChatColor.GOLD, "GreenMile ueberwacht folgende Welten:");
        for (GMWorld world : this.worldManager.getWorldList())
            ChatUtils.printInfo(player, this.pluginName, ChatColor.GREEN, "'" + world.getWorldName() + "' : " + world.getWorldSettings().getMaxSize());
    }
}