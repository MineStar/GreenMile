package de.minestar.greenmile;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.greenmile.commands.gm.ChangeSizeCommand;
import de.minestar.greenmile.commands.gm.CreateWorldCommand;
import de.minestar.greenmile.commands.gm.GMTeleportCommand;
import de.minestar.greenmile.commands.gm.GreenMileCommand;
import de.minestar.greenmile.commands.gm.ImportWorldCommand;
import de.minestar.greenmile.commands.gm.ListCommand;
import de.minestar.greenmile.commands.gm.SetSpawnCommand;
import de.minestar.greenmile.commands.gm.SpawnCommand;
import de.minestar.greenmile.commands.gm.StartCommand;
import de.minestar.greenmile.commands.gm.StatusCommand;
import de.minestar.greenmile.commands.gm.StopCommand;
import de.minestar.greenmile.listener.GMPListener;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Main extends JavaPlugin {

    public final static String NAME = "GreenMile";

    private static Main instance;
    public static ChunkGenerationThread chunkThread = null;
    private GMPListener pListener = null;
    private CommandList cmdList;
    private WorldManager worldManager;
    private Settings gmSettings;

    /**
     * Constructor
     */
    public Main() {
        if (instance == null)
            instance = this;
    }

    /**
     * ON DISABLE
     */
    public void onDisable() {
        if (chunkThread != null) {
            chunkThread.saveConfig();
        }
        this.cmdList = null;
        ConsoleUtils.printInfo(NAME, "Disabled!");
    }

    /**
     * ON ENABLE
     */
    public void onEnable() {

        // CREATE DATAFOLDER
        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        // INIT WORLDMANAGER
        this.worldManager = new WorldManager(this.getDataFolder());

        this.gmSettings = new Settings("config.yml", this.getDataFolder(), worldManager);
        if (gmSettings.loadSettings(true)) {
            gmSettings.registerAllEvents();
        } else {
            ConsoleUtils.printError(Main.NAME, "Could not load settings!\n\n GREENMILE WILL BE DISABLED !!! \n\n");
            this.setEnabled(false);
        }

        // INIT COMMANDLIST
        initCommandList();

        // CREATE BORDERTHREAD
        Runnable borderThread = new BorderThread(this.worldManager);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, borderThread, 300L, 100L);

        // CREATE LISTENER
        this.pListener = new GMPListener(this.worldManager);
        Bukkit.getPluginManager().registerEvents(this.pListener, this);

        // PRINT INFO
        ConsoleUtils.printInfo(NAME, "Version " + getDescription().getVersion() + " enabled!");
    }

    /**
     * INIT COMMANDLIST
     */
    private void initCommandList() {
        int speed = getConfig().getInt("speed", 5);
        ConsoleUtils.printInfo(NAME, "Default speed of generation thread is " + speed);

        //@formatter:off;
        this.cmdList = new CommandList(
                new GreenMileCommand    ("/gm", "", "gm.status",
                        new GMTeleportCommand   ("tp", "<WorldName>", "greenmile.teleport", worldManager),
                        new CreateWorldCommand  ("create", "<WorldName> [Environment [levelseed]]", "greenmile.createworld", worldManager),
                        new ImportWorldCommand  ("import", "<WorldName>", "greenmile.importworld", worldManager),
                        new SetSpawnCommand     ("setspawn", "", "greenmile.setspawn", worldManager),
                        new StartCommand        ("start", "<WorldName> [Speed]", "greenmile.start", worldManager, this, speed),
                        new StopCommand         ("stop", "", "gm.stop"),
                        new StatusCommand       ("status", "", "greenmile.status"),
                        new ChangeSizeCommand   ("size", "<WorldName> <Size> [f [Speed]", "greenmile.change", worldManager, this, speed),
                        new ListCommand         ("list", "", "greenmile.list", worldManager)                
                ),

                new SpawnCommand("/spawn", "[WorldName]", "", worldManager)
         );
        // @formatter: on;
    }
    
    /**
     * HANDLE COMMANDS
     */
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(this.cmdList != null)
            this.cmdList.handleCommand(sender, label, args);
        return true;
    }

    /**
     * GET PLUGIN-INSTANCE
     * 
     * @return the Maininstance of GreenMile
     */
    public static Main getInstance() {
        return instance;
    }
}