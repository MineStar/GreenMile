package de.minestar.greenmile.commands.gm;

import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class StatusCommand extends AbstractCommand {

    public StatusCommand(String syntax, String arguments, String node) {
        super(Main.name, syntax, arguments, node);
        this.description = "Zeigt Status des Threads an";
    }

    public void execute(String[] args, Player player) {
        // TODO: Server command
        if (Main.chunkThread == null)
            PlayerUtils.sendError(player, this.pluginName, "Es existiert kein Thread!");
        else
            PlayerUtils.sendInfo(player, this.pluginName, "Status: " + Main.chunkThread.getStatus());
    }
}