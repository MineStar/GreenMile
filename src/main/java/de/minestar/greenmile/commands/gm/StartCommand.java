package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class StartCommand extends AbstractExtendedCommand {

    private final WorldManager worldManager;
    private final Plugin plugin;
    private final int speed;

    public StartCommand(String syntax, String arguments, String node, WorldManager worldManager, Plugin plugin, int speed) {
        super(Main.name, syntax, arguments, node);
        this.plugin = plugin;
        this.speed = speed;
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        startThread(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        startThread(args, console);
    }

    private void startThread(String[] args, CommandSender sender) {

        String worldName = args[0];
        World world = Bukkit.getServer().getWorld(worldName);
        if (!worldManager.worldExists(worldName)) {
            ChatUtils.writeError(sender, pluginName, "Welt '" + worldName + "' nicht gefunden!");
            return;
        }

        if (Main.chunkThread != null) {
            ChatUtils.writeError(sender, pluginName, "Es läuft bereits ein Erzeugungsthread!");
            return;
        }

        Main.chunkThread = new ChunkGenerationThread(worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize(), world.getName(), worldManager);

        int pSpeed = speed;
        if (args.length == 2) {
            try {
                pSpeed = Integer.parseInt(args[1]);
                if (pSpeed < 0) {
                    ChatUtils.writeError(sender, pluginName, "Bitte nur positive Zahlen nehmen...");
                    pSpeed = speed;
                }
            } catch (NumberFormatException e) {
                ChatUtils.writeError(sender, pluginName, "Fehlerhafte Zahl, Standardgeschwindigkeit von " + speed + " wird genutzt!");
                pSpeed = speed;
            }
        }
        Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, Main.chunkThread, 0L, pSpeed));
        ChatUtils.writeSuccess(sender, pluginName, "Die Welt '" + worldName + "' wird nun mit einer Geschwindigkeit von " + pSpeed + " erzeugt!");
        ChatUtils.writeInfo(sender, pluginName, "'/gm stop' hält den Thread an!");
    }

}