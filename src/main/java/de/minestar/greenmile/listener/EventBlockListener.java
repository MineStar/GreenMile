package de.minestar.greenmile.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.GMWorldEventOptions;
import de.minestar.greenmile.worlds.WorldManager;

public class EventBlockListener extends BlockListener {
    private final WorldManager worldManager;

    public EventBlockListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockBreak());
        world = null;
    }

    @Override
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockFire());
        world = null;
    }

    @Override
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceMelt() && event.getBlock().getTypeId() == Material.ICE.getId()) || (options.isBlockSnowMelt() && (event.getBlock().getTypeId() == Material.SNOW_BLOCK.getId() || event.getBlock().getTypeId() == Material.SNOW.getId()))) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }

    @Override
    public void onBlockForm(BlockFormEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceForm() && event.getNewState().getTypeId() == Material.ICE.getId()) || (options.isBlockSnowForm() && (event.getNewState().getTypeId() == Material.SNOW_BLOCK.getId() || event.getNewState().getTypeId() == Material.SNOW.getId()))) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }

    @Override
    public void onBlockFromTo(BlockFromToEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        boolean isWater = (event.getBlock().getTypeId() == Material.WATER.getId()) || (event.getBlock().getTypeId() == Material.STATIONARY_WATER.getId());
        if (options.isEnableSponge() && isWater) {
            Block block = event.getBlock();
            for (int x = -(options.getSpongeRadius() + 1); x <= (options.getSpongeRadius() + 1); x++) {
                for (int z = -(options.getSpongeRadius() + 1); z <= (options.getSpongeRadius() + 1); z++) {
                    for (int y = -(options.getSpongeRadius() + 1); y <= (options.getSpongeRadius() + 1); y++) {
                        block = event.getBlock().getRelative(x, y, z);
                        if (block.getTypeId() == Material.SPONGE.getId()) {
                            event.setCancelled(true);
                            options = null;
                            world = null;
                            return;
                        }
                    }
                }
            }
            block = null;
        }
        options = null;
        world = null;
    }

    @Override
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled())
            return;

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
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if (options.isBlockLeavesDecay()) {
            Block block = event.getBlock();
            int type = 0;
            for (int x = -options.getBlockLeavesDecayRadius(); x <= options.getBlockLeavesDecayRadius(); x++) {
                for (int y = -options.getBlockLeavesDecayRadius(); y <= options.getBlockLeavesDecayRadius(); y++) {
                    for (int z = -options.getBlockLeavesDecayRadius(); z <= options.getBlockLeavesDecayRadius(); z++) {
                        type = block.getRelative(x, y, z).getTypeId();
                        if (type == Material.WOOD.getId() || type == Material.LOG.getId() || type == Material.FENCE.getId() || type == Material.BOOKSHELF.getId() || type == Material.CHEST.getId() || type == Material.TRAP_DOOR.getId() || type == Material.JUKEBOX.getId() || type == Material.WORKBENCH.getId() || type == Material.NOTE_BLOCK.getId() || type == Material.SIGN_POST.getId() || type == Material.WALL_SIGN.getId() || type == Material.WOOD_STAIRS.getId() || type == Material.WOOD_DOOR.getId()) {
                            event.setCancelled(true);
                            block = null;
                            options = null;
                            world = null;
                            return;
                        }
                    }
                }
            }
            block = null;
        }
        options = null;
        world = null;
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        // CHECK ALLOW PORTAL ANYWHERE
        if (options.isAllowPortalAnywhere() && event.getBlock().getTypeId() == Material.PORTAL.getId()) {
            event.setCancelled(true);
            options = null;
            world = null;
            return;
        }

        // CHECK REDSTONE
        if (!options.isRedstoneEnabled() && event.getBlock().getTypeId() == Material.REDSTONE_WIRE.getId()) {
            event.setCancelled(true);
            options = null;
            world = null;
            return;
        }
        options = null;
        world = null;
    }

    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled())
            return;

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
        if (event.isCancelled())
            return;

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
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp())
            return;

        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(!world.getEventSettings().isAllowBlockPlace());
        world = null;
    }

    @Override
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = this.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceForm() && event.getNewState().getTypeId() == Material.ICE.getId()) || (options.isBlockSnowForm() && (event.getNewState().getTypeId() == Material.SNOW_BLOCK.getId() || event.getNewState().getTypeId() == Material.SNOW.getId())) || (options.isBlockFire() && event.getNewState().getTypeId() == Material.FIRE.getId())) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }
}