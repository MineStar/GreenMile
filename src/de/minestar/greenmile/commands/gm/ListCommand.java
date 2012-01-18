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

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class ListCommand extends Command {

    private HashMap<String, Integer> worldSettings;

    public ListCommand(String pluginName, String syntax, String arguments, String node, HashMap<String, Integer> worldSettings) {
        super(pluginName, syntax, arguments, node);
        this.worldSettings = worldSettings;
    }

    @Override
    public void execute(String[] args, Player player) {
        if (worldSettings.isEmpty()) {
            ChatUtils.printError(player, Main.name, "GreenMile ueberwacht keine Welt!");
            return;
        }
        ChatUtils.printInfo(player, Main.name, ChatColor.GOLD, "GreenMile ueberwacht folgende Welten:");
        for (Entry<String, Integer> entry : worldSettings.entrySet())
            ChatUtils.printInfo(player, Main.name, ChatColor.GREEN, "'" + entry.getKey() + "' : " + entry.getValue());

    }

}
