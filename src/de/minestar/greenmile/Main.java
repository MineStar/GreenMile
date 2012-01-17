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
package de.minestar.greenmile;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.greenmile.commands.gm.ChangeSizeCommand;
import de.minestar.greenmile.commands.gm.GreenMileCommand;
import de.minestar.greenmile.commands.gm.ListCommand;
import de.minestar.greenmile.commands.gm.StartCommand;
import de.minestar.greenmile.commands.gm.StatusCommand;
import de.minestar.greenmile.commands.gm.StopCommand;
import de.minestar.greenmile.listener.GMPListener;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.commands.CommandList;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class Main extends JavaPlugin {

    private static Main instance;
    public static ChunkGenerationThread chunkThread = null;
    private GMPListener pListener = null;
    private HashMap<String, Integer> map;

    private CommandList cmdList;

    public static String name;

    public Main() {
        instance = this;
    }

    @Override
    public void onDisable() {
        if (chunkThread != null)
            chunkThread.saveConfig();

        cmdList = null;
        ChatUtils.printConsoleInfo("Disabled!", name);
    }

    @Override
    public void onEnable() {

        name = getDescription().getName();

        // create specific plugin folder
        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        Server server = getServer();
//        map = loadWorldSettings();
//        if (map == null)
//            return;

        initCommandList();

        Runnable borderThread = new BorderThread(map, server);
        server.getScheduler().scheduleSyncRepeatingTask(this, borderThread, 20 * 15, 20 * 5);

        pListener = new GMPListener(map);
        server.getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, pListener, Event.Priority.Highest, this);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!", name);
    }

    private void initCommandList() {

        int speed = getConfig().getInt("speed", 5);
        ChatUtils.printConsoleInfo("Default speed of generation thread is " + speed, name);

        // @formatter:off
        cmdList = new CommandList(new Command[]{
                new GreenMileCommand("/gm", "", "gm.status", new Command[]{
                        new StartCommand("start", "<WorldName>", "gm.start", map, this, speed),
                        new StopCommand("stop", "", "gm.stop"),
                        new StatusCommand("status", "", "gm.status"),
                        new ChangeSizeCommand("change","<WorldName> <Size>","gm.change", map, this, speed),
                        new ListCommand("list", "", "gm.list", map)
                })
        });
        // @formatter:on

    }
    // TODO: Transfer to WorldHandler
    // private HashMap<String, Integer> loadWorldSettings() {
//        HashMap<String, Integer> map = new HashMap<String, Integer>();
//        try {
//            YamlConfiguration config = new YamlConfiguration();
//            File f = new File("plugins/GreenMile/config.yml");
//            if (!f.exists()) {
//                printToConsole("config.yml not found, plugin is disabled!");
//                this.setEnabled(false);
//                return null;
//            }
//
//            config.load(f);
//
//            if (config.get("worlds") == null) {
//                printToConsole("config.yml is corrupt (no worlds found), plugin is disabled!");
//                this.setEnabled(false);
//                return null;
//            }
//            Map<String, Object> worlds = config.getConfigurationSection("worlds").getValues(false);
//            for (Entry<String, Object> entry : worlds.entrySet()) {
//                printToConsole("Loaded world '" + entry.getKey() + "' with maxSize = " + entry.getValue());
//                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
//            }
//
//            if (map.isEmpty()) {
//                printToConsole("No map found, plugin is disabled!");
//                this.setEnabled(false);
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        cmdList.handleCommand(sender, label, args);
        return true;
    }

    /**
     * @return the instance
     */
    public static Main getInstance() {
        return instance;
    }
}
