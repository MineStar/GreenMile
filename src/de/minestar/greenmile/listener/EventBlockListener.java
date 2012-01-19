package de.minestar.greenmile.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.WorldManager;

public class EventBlockListener extends BlockListener {
    private final WorldManager worldManager;

    public EventBlockListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockBreak());
        world = null;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockPlace());
        world = null;
    }

    @Override
    public void onBlockIgnite(BlockIgniteEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        if (world.getEventSettings().isBlockFire()) {
            IgniteCause cause = event.getCause();
            if (cause.equals(IgniteCause.LAVA) || cause.equals(IgniteCause.SPREAD) || cause.equals(IgniteCause.LIGHTNING)) {
                event.setCancelled(true);
            }
            cause = null;
        }
    }

    @Override
    public void onBlockBurn(BlockBurnEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockFire());
        world = null;
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        if (event.isSticky()) {
            event.setCancelled(world.getEventSettings().isBlockStickyPistons());
        } else {
            event.setCancelled(world.getEventSettings().isBlockNormalPistons());
        }
        world = null;
    }

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        if (event.isSticky()) {
            event.setCancelled(world.getEventSettings().isBlockStickyPistons());
        } else {
            event.setCancelled(world.getEventSettings().isBlockNormalPistons());
        }
        world = null;
    }
}