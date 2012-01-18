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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class StopCommand extends Command {

    public StopCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Stopt den Renderthread";
    }

    @Override
    public void execute(String[] args, Player player) {

        if (Main.chunkThread == null)
            ChatUtils.printError(player, Main.name, "Es existiert kein Thread!");

        else {
            Bukkit.getServer().getScheduler().cancelTask(Main.chunkThread.getTaskID());
            Main.chunkThread.saveConfig();
            Main.chunkThread = null;
            ChatUtils.printSuccess(player, Main.name, "Thread angehalten!");
        }
    }

}
