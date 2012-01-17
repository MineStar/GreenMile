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
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.Command;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMTeleportCommand extends Command {

    public GMTeleportCommand(String syntax, String arguments, String node) {
        super(syntax, arguments, node);
        this.description = "Teleport to a world";
    }

    @Override
    public void execute(String[] args, Player player) {
        String worldName = args[0];

        WorldManager manager = Main.getInstance().getWorldManager();

        // THE WORLD DOES NOT EXIST => RETURN
        if (!manager.worldExists(worldName)) {
            ChatUtils.printError(player, pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        // GET BUKKITWORLD
        World bukkitWorld = manager.getGMWorld(worldName).getBukkitWorld();
        if (bukkitWorld == null) {
            ChatUtils.printError(player, pluginName, "Bukkitworld '" + worldName + "' does not exist!");
            return;
        }

        // TP TO WORLD
        player.teleport(bukkitWorld.getSpawnLocation());
        ChatUtils.printInfo(player, pluginName, ChatColor.AQUA, "Welcome to '" + worldName + "'!");
    }
}
