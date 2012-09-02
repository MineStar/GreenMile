package de.minestar.greenmile.core;

import java.io.File;

import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.greenmile.Settings;
import de.minestar.greenmile.commands.gm.ChangeSizeCommand;
import de.minestar.greenmile.commands.gm.CreateWorldCommand;
import de.minestar.greenmile.commands.gm.GMTeleportCommand;
import de.minestar.greenmile.commands.gm.GreenMileCommand;
import de.minestar.greenmile.commands.gm.ImportWorldCommand;
import de.minestar.greenmile.commands.gm.ListCommand;
import de.minestar.greenmile.commands.gm.PosResetCommand;
import de.minestar.greenmile.commands.gm.SetSpawnCommand;
import de.minestar.greenmile.commands.gm.SpawnCommand;
import de.minestar.greenmile.commands.gm.StartCommand;
import de.minestar.greenmile.commands.gm.StatusCommand;
import de.minestar.greenmile.commands.gm.StopCommand;
import de.minestar.greenmile.listener.GMPListener;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.threading.ChunkGenerationThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class GreenMileCore extends AbstractCore {

    /* NAME */
    public final static String NAME = "GreenMile";

    /* INSTANCE */
    public static GreenMileCore INSTANCE;

    /* THREADS */
    public static ChunkGenerationThread chunkThread;
    public static BorderThread borderThread;

    /* LISTENER */
    private GMPListener pListener;

    /* MANAGER */
    public static WorldManager worldManager;

    /* SETTINGS */
    public static Settings gmSettings;

    public GreenMileCore() {
        super(NAME);
        INSTANCE = this;
    }

    @Override
    protected boolean commonDisable() {
        if (chunkThread != null)
            chunkThread.saveConfig();

        return super.commonDisable();
    }

    @Override
    protected boolean loadingConfigs(File dataFolder) {

        gmSettings = new Settings("config.yml", dataFolder);
        if (gmSettings.loadSettings(true))
            gmSettings.registerAllEvents();
        else {
            ConsoleUtils.printError(GreenMileCore.NAME, "Could not load settings!\n\n GREENMILE WILL BE DISABLED !!! \n\n");
            this.setEnabled(false);
        }

        return true;
    }

    @Override
    protected boolean createManager() {
        worldManager = new WorldManager(this.getDataFolder());
        worldManager.init();
        return true;
    }

    @Override
    protected boolean createThreads() {
        borderThread = new BorderThread();

        return true;
    }

    @Override
    protected boolean startThreads(BukkitScheduler scheduler) {
        scheduler.scheduleSyncRepeatingTask(this, borderThread, 20L * 15L, 20L * 5L);
        return true;
    }

    @Override
    protected boolean createListener() {
        pListener = new GMPListener();
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(this.pListener, this);

        return true;
    }

    @Override
    protected boolean createCommands() {
        int speed = getConfig().getInt("speed", 5);
        ConsoleUtils.printInfo(NAME, "Default speed of generation thread is " + speed);

        //@formatter:off;
        this.cmdList = new CommandList(
                new GreenMileCommand    ("/gm", "", "gm.status",
                        new GMTeleportCommand   ("tp", "<WorldName>", "greenmile.teleport", worldManager),
                        new PosResetCommand  ("posreset", "", "greenmile.posreset"),
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
        
        return true;
    }
}