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
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.greenmile.Main;
import de.minestar.minstarlibrary.commands.ExtendedCommand;

public class CreateWorldCommand extends ExtendedCommand {

    public CreateWorldCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Create a new world";
    }

    @Override
    public void execute(String[] args, Player player) {
        Long seed = 1337l;
        Environment environment = Environment.NORMAL;

        // GET WORLDNAME
        String worldName = args[0];

        // CATCH ENVIRONMENT
        if (args.length >= 2) {
            try {
                environment = Environment.valueOf(args[1]);
                // IS ENVIRONMENT CORRECT?
            } catch (Exception e) {
                ChatUtils.printInfo(player, "[GreenMile]", ChatColor.GRAY, "Environment '" + args[1] + "' not found");
                return;
            }
        }

        // CATCH LEVELSEED
        if (args.length >= 3) {
            try {
                seed = Long.parseLong(args[2]);
            } catch (Exception e) {
                seed = (long) args[2].hashCode();
            }
        }

        // CALL WORLDMANAGER TO CHECK IF WORLD ALREADY EXISTS?
        // IF SO: RETURN WITH ERROR
        if (Main.getInstance().getWorldManager().worldExists(worldName) || Bukkit.getServer().getWorld(worldName) != null) {
            ChatUtils.printError(player, pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.printInfo(player, pluginName, ChatColor.GRAY, "A world with that name does already exist.");
            return;
        }

        // CALL WORLDMANAGER TO CREATE WORLD
        boolean result = Main.getInstance().getWorldManager().createWorld(worldName, environment, seed);

        if (result) {
            ChatUtils.printSuccess(player, pluginName, "World '" + worldName + "' created!");
        } else {
            ChatUtils.printError(player, pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.printInfo(player, pluginName, ChatColor.GRAY, "There was an internal error while creating the worldsettings.");
        }
    }

}
