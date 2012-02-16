package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.helper.EnumHelper;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class CreateWorldCommand extends AbstractExtendedCommand {

    private WorldManager worldManager;

    public CreateWorldCommand(String syntax, String arguments, String node, WorldManager worldManager) {
        super(Main.name, syntax, arguments, node);
        this.description = "Create a new world";
        this.worldManager = worldManager;
    }

    @Override
    public void execute(String[] args, Player player) {
        createWorld(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        createWorld(args, console);
    }

    private void createWorld(String[] args, CommandSender sender) {
        Long seed = 1337l;
        Environment environment = Environment.NORMAL;

        String worldName = args[0];

        if (args.length >= 2) {
            environment = EnumHelper.getEnvironment(args[1]);
            if (environment == null) {
                ChatUtils.writeError(sender, pluginName, "Environment '" + args[1] + "' not found");
                return;
            }
        }

        if (args.length >= 3) {
            try {
                seed = Long.valueOf(Long.parseLong(args[2]));
            } catch (Exception e) {
                seed = Long.valueOf(args[2].hashCode());
            }

        }

        // World already existing
        if ((worldManager.worldExists(worldName)) || (Bukkit.getServer().getWorld(worldName) != null)) {
            ChatUtils.writeError(sender, pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.writeError(sender, pluginName, "A world with that name does already exist!");
            return;
        }

        boolean result = worldManager.createWorld(worldName, environment, seed.longValue(), worldManager.getDataFolder());

        if (result)
            ChatUtils.writeSuccess(sender, pluginName, "World '" + worldName + "' created!");
        else {
            ChatUtils.writeError(sender, pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.writeError(sender, pluginName, "There was an internal error while creating the worldsettings.");
        }
    }
}