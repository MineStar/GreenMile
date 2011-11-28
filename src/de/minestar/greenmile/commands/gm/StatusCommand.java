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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.commands.Command;

public class StatusCommand extends Command {

    public StatusCommand(String syntax, String arguments, String node) {
        super(syntax, arguments, node);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(String[] args, Player player) {
        if (Main.chunkThread == null) {
            player.sendMessage(ChatColor.RED + "[GreenMile] No thread found!");
            return;
        }
        player.sendMessage(ChatColor.GRAY + "[GreenMile] Status: " + Main.chunkThread.getStatus());

    }

}
