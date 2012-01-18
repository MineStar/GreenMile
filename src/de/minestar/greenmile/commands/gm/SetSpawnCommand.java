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

import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.ExtendedCommand;

public class SetSpawnCommand extends ExtendedCommand {

    public SetSpawnCommand(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Set the spawn of the current world";
    }
    @Override
    public void execute(String[] args, Player player) {
        String worldName = player.getWorld().getName();
        WorldManager manager = Main.getInstance().getWorldManager();
        if (!manager.worldExists(worldName)) {
            ChatUtils.printError(player, pluginName, "You need to import this world!");
            return;
        }
        GMWorld thisWorld = manager.getGMWorld(worldName);
        thisWorld.getWorldSettings().setWorldSpawn(player.getLocation());
        thisWorld.updateWorld();
        if (thisWorld.getWorldSettings().saveSettings(worldName, manager.getDataFolder()))
            ChatUtils.printSuccess(player, pluginName, "Spawn set.");
        else
            ChatUtils.printError(player, pluginName, "Error while saving settings.");
    }
}
