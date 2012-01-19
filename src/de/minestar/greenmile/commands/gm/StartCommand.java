package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.ExtendedCommand;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class StartCommand extends ExtendedCommand {
    private final WorldManager worldManager;
    private final Plugin plugin;
    private final int speed;

    public StartCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager, Plugin plugin, int speed) {
        super(pluginName, syntax, arguments, node);
        this.plugin = plugin;
        this.speed = speed;
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        String worldName = args[0];
        World world = Bukkit.getServer().getWorld(worldName);
        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(player, this.pluginName, " Welt '" + worldName + "' nicht gefunden!");
            return;
        }

        if (Main.chunkThread != null) {
            ChatUtils.printError(player, this.pluginName, "Es läuft bereits ein Erzeugungsthread!");
            ChatUtils.printInfo(player, this.pluginName, ChatColor.GRAY, "Es läuft bereits ein Erzeugungsthread!");
            return;
        }

        Main.chunkThread = new ChunkGenerationThread(this.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize(), world.getName(), this.worldManager);

        int pSpeed = this.speed;
        if (args.length == 2) {
            try {
                pSpeed = Integer.parseInt(args[1]);
                if (pSpeed < 0) {
                    ChatUtils.printError(player, this.pluginName, "Bitte nur positive Zahlen nehmen...");
                    pSpeed = this.speed;
                }
            } catch (NumberFormatException e) {
                ChatUtils.printError(player, this.pluginName, "Fehlerhafte Zahl, Standardgeschwindigkeit von " + pSpeed + " wird genutzt!");
            }
        }
        Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, Main.chunkThread, 0L, pSpeed));
        ChatUtils.printSuccess(player, this.pluginName, "Die Welt '" + worldName + "' wird nun mit einer Geschwindigkeit von " + pSpeed + " erzeugt!");
        ChatUtils.printInfo(player, this.pluginName, ChatColor.GRAY, "'/gm stop' hält den Thread an!");
    }
}