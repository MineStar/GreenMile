/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of GreenMile.
 * 
 * GreenMile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * GreenMile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GreenMile.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.greenmile.commands.gm;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.commands.ExtendedCommand;
import de.minestar.greenmile.threading.ChunkGenerationThread;

public class ChangeSizeCommand extends ExtendedCommand {

    private HashMap<String, Integer> map;
    private final Plugin plugin;
    private final int speed;

    public ChangeSizeCommand(String syntax, String arguments, String node, HashMap<String, Integer> map, Plugin plugin, int speed) {
        super(syntax, arguments, node);
        this.map = map;
        this.plugin = plugin;
        this.speed = speed;
        this.description = "Veraendert die maximal erlaubte Weltgroesse";
    }

    @Override
    public void execute(String[] args, Player player) {

        String worldName = args[0].toLowerCase();

        if (Main.chunkThread != null) {
            player.sendMessage(ChatColor.RED + "Generationthread lauft gerade. Erst /gm stop " + worldName + " eingeben!");
            return;
        }

        Integer newSize = 0;
        try {
            newSize = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(this.getHelpMessage());
        }

        if (newSize <= 0) {
            player.sendMessage(ChatColor.RED + "Bitte nur positive Zahlen nehmen...");
            return;
        }

        World world = player.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(ChatColor.RED + "Keine Welt mit Namen " + worldName + " gefunden!");
            return;
        }

        map.put(worldName, newSize);
        updateConfig(worldName, newSize);
        player.sendMessage(ChatColor.GREEN + "Groesse erfolgreich geaendert!");
        if (args.length >= 3 && args[2].equalsIgnoreCase("f")) {

            Main.chunkThread = new ChunkGenerationThread(newSize, worldName);

            // WHEN /gm start world SPEED was used read out the speed
            int pSpeed = speed;
            if (args.length == 4) {
                try {
                    pSpeed = Integer.parseInt(args[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "Fehlerhafte Zahl, Standardgeschwindigkeit von " + pSpeed + " wird genutzt!");
                }
            }

            Main.chunkThread.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, Main.chunkThread, 0L, pSpeed));
            player.sendMessage(ChatColor.GREEN + "[GreenMile] Rendering of world '" + worldName + "' started with speed " + speed + "!");
            player.sendMessage(ChatColor.GRAY + "Type '/gm stop' to stop the thread.");
        }
    }

    private void updateConfig(String worldName, Integer newSize) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            File f = new File("plugins/GreenMile/config.yml");

            config.load(f);
            config.set("worlds." + worldName, newSize);
            config.save(f);

            f = new File("plugins/GreenMile/worlds", worldName + ".yml");
            config.load(f);
            config.set("lastRenderedChunk.X", null);
            config.set("lastRenderedChunk.Y", null);
            config.set("status,", null);
            config.save(f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
