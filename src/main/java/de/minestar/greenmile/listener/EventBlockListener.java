package de.minestar.greenmile.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.worlds.GMWorld;
import de.minestar.greenmile.worlds.GMWorldEventOptions;

public class EventBlockListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        if (!event.getPlayer().isOp()) {
            event.setCancelled(!world.getEventSettings().isAllowBlockBreak());
        }

        // CANCEL ICE-BREAK
        if (!event.isCancelled() && event.getBlock().getType().equals(Material.ICE)) {
            if (world.getEventSettings().isBlockIceMelt()) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                ItemStack item = new ItemStack(Material.ICE, 1);
                event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
            }
        }
        world = null;
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        event.setCancelled(world.getEventSettings().isBlockFire());
        world = null;
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceMelt() && event.getBlock().getType().equals(Material.ICE)) || (options.isBlockSnowMelt() && (event.getBlock().getType().equals(Material.SNOW_BLOCK) || event.getBlock().getType().equals(Material.SNOW)))) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceForm() && event.getNewState().getType().equals(Material.ICE)) || (options.isBlockSnowForm() && (event.getNewState().getType().equals(Material.SNOW_BLOCK) || event.getNewState().getType().equals(Material.SNOW)))) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        boolean isWater = (event.getBlock().getType().equals(Material.WATER) || (event.getBlock().getType().equals(Material.STATIONARY_WATER)));
        if (options.isEnableSponge() && isWater) {
            Block block = event.getBlock();
            for (int x = -(options.getSpongeRadius() + 1); x <= (options.getSpongeRadius() + 1); x++) {
                for (int z = -(options.getSpongeRadius() + 1); z <= (options.getSpongeRadius() + 1); z++) {
                    for (int y = -(options.getSpongeRadius() + 1); y <= (options.getSpongeRadius() + 1); y++) {
                        block = event.getBlock().getRelative(x, y, z);
                        if (block.getType().equals(Material.SPONGE)) {
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

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        if (world.getEventSettings().isBlockFire()) {
            IgniteCause cause = event.getCause();
            if (cause.equals(IgniteCause.LAVA) || cause.equals(IgniteCause.SPREAD) || cause.equals(IgniteCause.LIGHTNING) || cause.equals(IgniteCause.FIREBALL)) {
                event.setCancelled(true);
            }
            cause = null;
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if (options.isBlockLeavesDecay()) {
            Block block = event.getBlock();
            for (int x = -options.getBlockLeavesDecayRadius(); x <= options.getBlockLeavesDecayRadius(); x++) {
                for (int y = -options.getBlockLeavesDecayRadius(); y <= options.getBlockLeavesDecayRadius(); y++) {
                    for (int z = -options.getBlockLeavesDecayRadius(); z <= options.getBlockLeavesDecayRadius(); z++) {

                        Material mat = block.getRelative(x, x, z).getType();
                        switch (mat) {
                            case WOOD :
                            case LOG :
                            case FENCE :
                            case BOOKSHELF :
                            case CHEST :
                            case TRAP_DOOR :
                            case JUKEBOX :
                            case WORKBENCH :
                            case NOTE_BLOCK :
                            case SIGN_POST :
                            case WALL_SIGN :
                            case WOOD_STAIRS :
                            case WOOD_DOOR :
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

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock().getWorld().getName());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        // CHECK ALLOW PORTAL ANYWHERE
        if (options.isAllowPortalAnywhere() && event.getBlock().getType().equals(Material.PORTAL)) {
            event.setCancelled(true);
            options = null;
            world = null;
            return;
        }

        // CHECK REDSTONE
        if (!options.isRedstoneEnabled() && event.getBlock().getType().equals(Material.REDSTONE_WIRE)) {
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

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
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

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }
        if (!event.getPlayer().isOp()) {
            event.setCancelled(!world.getEventSettings().isAllowBlockPlace());
        }

        // SPONGE
        if (!event.isCancelled() && event.getBlockPlaced().getType().equals(Material.SPONGE)) {
            if (world.getEventSettings().isEnableSponge()) {
                Block block = event.getBlock();
                GMWorldEventOptions options = world.getEventSettings();
                for (int x = -(options.getSpongeRadius() + 1); x <= (options.getSpongeRadius() + 1); x++) {
                    for (int z = -(options.getSpongeRadius() + 1); z <= (options.getSpongeRadius() + 1); z++) {
                        for (int y = -(options.getSpongeRadius() + 1); y <= (options.getSpongeRadius() + 1); y++) {
                            block = event.getBlock().getRelative(x, y, z);

                            switch (block.getType()) {
                                case WATER :
                                case STATIONARY_WATER :
                                    block.setType(Material.AIR);
                                    break;
                            }
                        }
                    }
                }
                options = null;
                block = null;
            }
        }
        world = null;
    }
    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.isCancelled())
            return;

        GMWorld world = GreenMileCore.worldManager.getGMWorld(event.getBlock());
        if (world == null) {
            return;
        }

        GMWorldEventOptions options = world.getEventSettings();
        if ((options.isBlockIceForm() && event.getNewState().getType().equals(Material.ICE)) || (options.isBlockSnowForm() && (event.getNewState().getType().equals(Material.SNOW_BLOCK) || event.getNewState().getType().equals(Material.SNOW))) || (options.isBlockFire() && event.getNewState().getType().equals(Material.FIRE))) {
            event.setCancelled(true);
        }
        options = null;
        world = null;
    }
}