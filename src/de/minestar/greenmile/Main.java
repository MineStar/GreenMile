package de.minestar.greenmile;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
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
import de.minestar.minstarlibrary.commands.CommandList;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class Main extends JavaPlugin {
    private static Main instance;
    public static ChunkGenerationThread chunkThread = null;
    private GMPListener pListener = null;
    private CommandList cmdList;
    public static String name;
    private WorldManager worldManager;

    public Main() {
        instance = this;
    }

    public void onDisable() {
        if (chunkThread != null) {
            chunkThread.saveConfig();
        }
        this.cmdList = null;
        ChatUtils.printConsoleInfo("Disabled!", name);
    }

    public void onEnable() {
        name = "[" + getDescription().getName() + "]";

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        this.worldManager = new WorldManager(getDataFolder());

        initCommandList();

        Runnable borderThread = new BorderThread(this.worldManager);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, borderThread, 300L, 100L);

        this.pListener = new GMPListener(this.worldManager);
        Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, this.pListener, Event.Priority.Highest, this);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!", name);
    }

    private void initCommandList() {
        int speed = getConfig().getInt("speed", 5);
        ChatUtils.printConsoleInfo("Default speed of generation thread is " + speed, name);

        this.cmdList = new CommandList(new de.minestar.minstarlibrary.commands.Command[]{new GreenMileCommand("[GreenMile]", "/gm", "", "gm.status", new de.minestar.minstarlibrary.commands.Command[]{new GMTeleportCommand("[GreenMile]", "tp", "<WorldName>", "gm.teleport", this.worldManager), new CreateWorldCommand("[GreenMile]", "createworld", "<WorldName> [Environment [levelseed]]", "gm.createworld", this.worldManager), new ImportWorldCommand("[GreenMile]", "importworld", "<WorldName>", "gm.importworld", this.worldManager), new SetSpawnCommand("[GreenMile]", "setspawn", "", "gm.setspawn", this.worldManager), new StartCommand("[GreenMile]", "start", "<WorldName>", "gm.start", this.worldManager, this, speed), new StopCommand("[GreenMile]", "stop", "", "gm.stop"), new StatusCommand("[GreenMile]", "status", "", "gm.status"), new ChangeSizeCommand("[GreenMile]", "change", "<WorldName> <Size>", "gm.change", this.worldManager, this, speed), new ListCommand("[GreenMile]", "list", "", "gm.list", this.worldManager)}), new SpawnCommand("[GreenMile]", "/spawn", "[WorldName]", "", this.worldManager)});
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.cmdList.handleCommand(sender, label, args);
        return true;
    }

    public static Main getInstance() {
        return instance;
    }
}