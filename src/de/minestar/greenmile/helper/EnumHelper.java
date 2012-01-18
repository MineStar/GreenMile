/*
 * Copyright (C) 2012 MineStar.de 
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

package de.minestar.greenmile.helper;

import org.bukkit.Difficulty;
import org.bukkit.World.Environment;

public class EnumHelper {

    /**
     * Get Environment by Name
     * 
     * @param text
     * @return the environment, or NULL if not found
     */
    public static Environment getEnvironment(String text) {
        for (Environment environment : Environment.values()) {
            if (text.equalsIgnoreCase(environment.toString()))
                return environment;
        }
        return null;
    }

    /**
     * Get Difficulty by Name
     * 
     * @param text
     * @return the difficulty, or NULL if not found
     */
    public static Difficulty getDifficulty(String text) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (text.equalsIgnoreCase(difficulty.toString()))
                return difficulty;
        }
        return null;
    }
}
