package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class ChangeSizeCommand extends AbstractExtendedCommand {

    private WorldManager worldManager;
    private final Plugin plugin;
    private final int speed;

    public ChangeSizeCommand(String syntax, String arguments, String node, WorldManager worldManager, Plugin plugin, int speed) {
        super(Main.name, syntax, arguments, node);
        this.worldManager = worldManager;
        this.plugin = plugin;
        this.speed = speed;
        this.description = "Veraendert die maximal erlaubte Weltgroesse";
    }

    public void execute(String[] args, Player player) {
        changeSize(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        changeSize(args, console);
    }

    private void changeSize(String[] args, CommandSender sender) {

        String worldName = args[0];

        if (Main.chunkThread != null) {
            ChatUtils.writeError(sender, pluginName, "Generationthread laeuft gerade. Erst '/gm stop' eingeben!");
            return;
        }

        Integer newSize = Integer.valueOf(0);
        try {
            newSize = Integer.valueOf(Integer.parseInt(args[1]));
        } catch (Exception e) {
            ChatUtils.writeError(sender, pluginName, getHelpMessage());
            return;
        }

        if (newSize.intValue() <= 0) {
            ChatUtils.writeError(sender, pluginName, "Bitte nur positive Zahlen nehmen...");
            return;
        }

        if (!worldManager.worldExists(worldName)) {
            ChatUtils.writeError(sender, pluginName, "Keine Welt namens '" + worldName + "' gefunden!");
            return;
        }

        worldManager.getGMWorld(worldName).getWorldSettings().setMaxSize(newSize.intValue());
        worldManager.getGMWorld(worldName).getWorldSettings().saveSettings(worldName, worldManager.getDataFolder());

        ChatUtils.writeSuccess(sender, pluginName, "Groesse erfolgreich geaendert!");
        if ((args.length >= 3) && (args[2].equalsIgnoreCase("f"))) {
            Main.chunkThread = new ChunkGenerationThread(worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize(), worldName, worldManager);

            int pSpeed = speed;
            if (args.length == 4) {
                try {
                    pSpeed = Integer.parseInt(args[3]);
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
}