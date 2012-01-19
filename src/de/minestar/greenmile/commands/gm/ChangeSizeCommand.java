package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.ExtendedCommand;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class ChangeSizeCommand extends ExtendedCommand {
    private WorldManager worldManager;
    private final Plugin plugin;
    private final int speed;

    public ChangeSizeCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager, Plugin plugin, int speed) {
        super(pluginName, syntax, arguments, node);
        this.worldManager = worldManager;
        this.plugin = plugin;
        this.speed = speed;
        this.description = "Veraendert die maximal erlaubte Weltgroesse";
    }

    public void execute(String[] args, Player player) {
        String worldName = args[0];

        if (Main.chunkThread != null) {
            ChatUtils.printError(player, this.pluginName, "Generationthread lauft gerade. Erst '/gm stop' eingeben!");
            return;
        }

        Integer newSize = Integer.valueOf(0);
        try {
            newSize = Integer.valueOf(Integer.parseInt(args[1]));
        } catch (Exception e) {
            ChatUtils.printError(player, this.pluginName, getHelpMessage());
        }

        if (newSize.intValue() <= 0) {
            ChatUtils.printError(player, this.pluginName, "Bitte nur positive Zahlen nehmen...");
            return;
        }

        if (!this.worldManager.worldExists(worldName)) {
            ChatUtils.printError(player, this.pluginName, "Keine Welt namens '" + worldName + "' gefunden!");
            return;
        }

        this.worldManager.getGMWorld(worldName).getWorldSettings().setMaxSize(newSize.intValue());
        this.worldManager.getGMWorld(worldName).getWorldSettings().saveSettings(worldName, this.worldManager.getDataFolder());

        ChatUtils.printSuccess(player, this.pluginName, "Groesse erfolgreich geaendert!");
        if ((args.length >= 3) && (args[2].equalsIgnoreCase("f"))) {
            Main.chunkThread = new ChunkGenerationThread(this.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize(), worldName, this.worldManager);

            int pSpeed = this.speed;
            if (args.length == 4) {
                try {
                    pSpeed = Integer.parseInt(args[3]);
                    if (pSpeed < 0) {
                        ChatUtils.printError(player, this.pluginName, "Bitte nur positive Zahlen nehmen...");
                        pSpeed = this.speed;
                    }
                } catch (NumberFormatException e) {
                    ChatUtils.printError(player, this.pluginName, "Fehlerhafte Zahl, Standardgeschwindigkeit von " + pSpeed + " wird genutzt!");
                }
            }

            Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, Main.chunkThread, 0L, pSpeed));
            ChatUtils.printSuccess(player, Main.name, "Die Welt '" + worldName + "' wird nun mit einer Geschwindigkeit von " + pSpeed + " erzeugt!");
            ChatUtils.printInfo(player, Main.name, ChatColor.GRAY, "'/gm stop' hält den Thread an!");
        }
    }
}