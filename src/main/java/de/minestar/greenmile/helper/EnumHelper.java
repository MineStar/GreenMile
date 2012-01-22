package de.minestar.greenmile.helper;

import org.bukkit.Difficulty;
import org.bukkit.World.Environment;
import org.bukkit.event.Event.Priority;

public class EnumHelper {
    /**
     * Get the World.Environment by the name
     * 
     * @param text
     * @return the environment, or null if not found
     */
    public static Environment getEnvironment(String text) {
        for (Environment environment : Environment.values()) {
            if (text.equalsIgnoreCase(environment.toString()))
                return environment;
        }
        return null;
    }

    /**
     * Get the World.Difficulty by the name
     * 
     * @param text
     * @return the difficulty, or null if not found
     */
    public static Difficulty getDifficulty(String text) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (text.equalsIgnoreCase(difficulty.toString()))
                return difficulty;
        }
        return null;
    }

    /**
     * Get the Event.Priority by the name
     * 
     * @param text
     * @return the priority, or null if not found
     */
    public static Priority getPriority(String text) {
        for (Priority priority : Priority.values()) {
            if (text.equalsIgnoreCase(priority.toString()))
                return priority;
        }
        return null;
    }
}