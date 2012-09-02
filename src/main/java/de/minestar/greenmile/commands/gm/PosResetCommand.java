package de.minestar.greenmile.commands.gm;

import java.io.File;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class PosResetCommand extends AbstractCommand {

    public PosResetCommand(String syntax, String arguments, String node) {
        super(GreenMileCore.NAME, syntax, arguments, node);
    }

    public void execute(String[] args, Player player) {
        File folder = new File(MinestarCore.dataFolder, "playerdata");

        File files[] = folder.listFiles();
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            if (!file.getName().endsWith("dat")) {
                continue;
            }

            MinestarPlayer msPlayer = MinestarCore.getPlayer(file.getName().replace(".dat", ""));
            msPlayer.setBoolean("main.wasHere", false);
            count++;
        }

        PlayerUtils.sendSuccess(player, GreenMileCore.NAME, count + " Positions resetted!");
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        File folder = new File(MinestarCore.dataFolder, "playerdata");

        File files[] = folder.listFiles();
        int count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            if (!file.getName().endsWith("dat")) {
                continue;
            }

            MinestarPlayer msPlayer = MinestarCore.getPlayer(file.getName().replace(".dat", ""));
            msPlayer.setBoolean("main.wasHere", false);
            count++;
        }

        ConsoleUtils.printInfo(GreenMileCore.NAME, count + " Positions resetted!");
    }
}