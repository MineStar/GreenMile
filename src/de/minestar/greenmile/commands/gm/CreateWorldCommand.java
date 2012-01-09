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
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.greenmile.commands.ExtendedCommand;

public class CreateWorldCommand extends ExtendedCommand {

    public CreateWorldCommand(String syntax, String arguments, String node, HashMap<String, Integer> worldSettings) {
        super(syntax, arguments, node);
    }

    @Override
    public void execute(String[] args, Player player) {
        String worldName = args[0];
        Long seed = 1337l;
        Environment env = Environment.NORMAL;
        if (args.length > 1) {
            env = Environment.valueOf(args[1]);
            ChatUtils.printInfo(player, "[GreenMile]", ChatColor.GRAY, "Environment not found. Using NORMAL.");
        }

        if (env == null)
            env = Environment.NORMAL;

        if (args.length > 2) {
            try {
                seed = Long.parseLong(args[3]);
            } catch (Exception e) {
                ChatUtils.printInfo(player, "[GreenMile]", ChatColor.GRAY, "Could not parse levelseed. Using 1337l as seed.");
            }
        }

        // TODO: CHECK: DOES WORLD EXIST?
        // IF SO: RETURN WITH ERROR

        // TODO: CALL WORLDMANAGER TO CREATE WORLD

        // TODO: SAVE WORLD TO YML
    }

}
