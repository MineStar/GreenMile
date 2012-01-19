package de.minestar.greenmile.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;

public class EventBlockListener extends BlockListener {
    private final WorldManager worldManager;

    public EventBlockListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockBreak());
        world = null;
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockPlace());
        world = null;
    }
}