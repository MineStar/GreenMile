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

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.greenmile.commands.ExtendedCommand;

public class CreateWorldCommand extends ExtendedCommand {

    public CreateWorldCommand(String syntax, String arguments, String node, HashMap<String, Integer> worldSettings) {
        super(syntax, arguments, node);
    }

    @Override
    public void execute(String[] args, Player player) {
        Long seed = 1337l;
        Environment env = Environment.NORMAL;

        // GET WORLDNAME
        String worldName = args[0];

        // CATCH ENVIRONMENT
        if (args.length >= 2) {
            try {
                env = Environment.valueOf(args[1]);
                // IS ENVIRONMENT CORRECT?
            } catch (Exception e) {
                ChatUtils.printInfo(player, "[GreenMile]", ChatColor.GRAY, "Environment '" + args[1] + "' not found");
                return;
            }
        }

        // CATCH LEVELSEED
        if (args.length >= 3) {
            try {
                seed = Long.parseLong(args[3]);
            } catch (Exception e) {
                ChatUtils.printError(player, "[GreenMile]", "Could not parse levelseed '" + args[3] + "'.");
                return;
            }
        }

        // TODO: CALL WORLDMANAGER TO CHECK IF WORLD ALREADY EXISTS?
        // IF SO: RETURN WITH ERROR

        // TODO: CALL WORLDMANAGER TO CREATE WORLD

        // TODO: SAVE WORLD TO YML
    }

}
