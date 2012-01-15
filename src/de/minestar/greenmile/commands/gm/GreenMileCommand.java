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

import org.bukkit.entity.Player;

import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.commands.SuperCommand;

public class GreenMileCommand extends SuperCommand {

    public GreenMileCommand(String syntax, String arguments, String node, Command[] subCommands) {
        super(syntax, arguments, node, subCommands);
    }

    @Override
    public void execute(String[] args, Player player) {
        // Do nothing
    }

}
