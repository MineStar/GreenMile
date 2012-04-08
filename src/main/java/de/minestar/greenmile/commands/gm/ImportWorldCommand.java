package de.minestar.greenmile.commands.gm;

import java.io.File;

import net.minecraft.server.WorldData;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class ImportWorldCommand extends AbstractCommand {

    private WorldManager worldManager;

    public ImportWorldCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.NAME, syntax, arguments, node);
        this.description = "Create a new world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, Player player) {
        importWorld(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        importWorld(args, console);
    }

    private void importWorld(String[] args, CommandSender sender) {
        String worldName = args[0];

        if (worldManager.worldExists(worldName)) {
            ChatUtils.writeError(sender, pluginName, "This world does already exist!");
            return;
        }

        File levelFile = new File(worldName + "/level.dat");
        WorldData worldData;
        if (!levelFile.exists() || (worldData = this.worldManager.getWorldData(levelFile)) == null) {
            ChatUtils.writeError(sender, pluginName, "World '" + worldName + "' does not exist!");
            return;
        }

        // CREATE THE WORLD
        WorldCreator generator = new WorldCreator(worldName);
        generator.environment(World.Environment.getEnvironment(worldData.g()));
        generator.seed(worldData.g());
        generator.createWorld();

        boolean result = worldManager.importWorld(worldData);

        if (result) {
            ChatUtils.writeSuccess(sender, pluginName, "World '" + worldName + "' imported!");
        } else {
            ChatUtils.writeError(sender, pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.writeError(sender, pluginName, "There was an internal error while creating the worldsettings.");
        }
    }
}