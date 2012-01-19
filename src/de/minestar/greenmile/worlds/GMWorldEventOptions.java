package de.minestar.greenmile.worlds;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class GMWorldEventOptions {
    private boolean blockFire = true;
    private boolean blockLeavesDecay = true;
    private boolean blockCreeperExplosions = true;
    private boolean blockRegainHealth = true;
    private boolean blockIceForm = true;
    private boolean blockIceMelt = true;
    private boolean blockSnowForm = true;
    private boolean blockSnowMelt = true;
    private boolean blockTNT = false;
    private boolean blockStickyPistons = true;
    private boolean blockNormalPistons = true;
    private boolean allowPortalAnywhere = true;
    private boolean enableSponge = false;
    private boolean allowBlockPlace = true;
    private boolean allowBlockBreak = true;
    private boolean allowOpenChest = true;
    private boolean allowOpenFurnace = true;
    private boolean allowOpenDispenser = true;
    private boolean allowOpenWorkbench = true;
    private boolean blockEndermanPickUp = true;
    private boolean blockEndermanPlace = true;
    private boolean redstoneEnabled = true;
    private boolean pvpEnabled = true;

    private int blockLeavesDecayRadius = 3;
    private int SpongeRadius = 3;
    private String worldName;
    private File dataFolder;

    /**
     * Constructor
     * 
     * @param worldName
     * @param dataFolder
     */
    public GMWorldEventOptions(String worldName, File dataFolder) {
        this.worldName = worldName;
        this.dataFolder = dataFolder;
        loadSettings();
    }

    /**
     * LOAD SETTINGS
     */
    private boolean loadSettings() {
        File file = new File(this.dataFolder, "config_" + this.worldName + ".yml");

        if (!file.exists()) {
            // IF SAVE FAILS, LOADING FAILS TOO
            if (!saveSettings()) {
                return false;
            }
        }

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            // PLAYER
            this.blockRegainHealth = config.getBoolean("events.player.blockAutoRegainHealth", this.blockRegainHealth);
            this.pvpEnabled = config.getBoolean("events.player.pvpEnabled", this.pvpEnabled);

            // GENERAL
            this.blockFire = config.getBoolean("events.world.blockFire", this.blockFire);
            this.blockIceForm = config.getBoolean("events.world.blockIceForm", this.blockIceForm);
            this.blockIceMelt = config.getBoolean("events.world.blockIceMelt", this.blockIceMelt);
            this.blockSnowForm = config.getBoolean("events.world.blockSnowForm", this.blockSnowForm);
            this.blockSnowMelt = config.getBoolean("events.world.blockSnowMelt", this.blockSnowMelt);

            // BLOCK-PHYSICS
            this.blockLeavesDecay = config.getBoolean("events.physics.leaves.blockDecay", this.blockLeavesDecay);
            this.blockLeavesDecayRadius = config.getInt("events.physics.leaves.radius", this.blockLeavesDecayRadius);
            this.allowPortalAnywhere = config.getBoolean("events.physics.portal.allowAnywhere", this.allowPortalAnywhere);
            this.enableSponge = config.getBoolean("events.physics.sponge.enabled", this.enableSponge);
            this.SpongeRadius = config.getInt("events.physics.sponge.radius", this.SpongeRadius);
            this.redstoneEnabled = config.getBoolean("events.physics.redstone.enabled", this.redstoneEnabled);

            // PISTONS
            this.blockStickyPistons = config.getBoolean("events.pistons.blockSticky", this.blockStickyPistons);
            this.blockNormalPistons = config.getBoolean("events.pistons.blockNormal", this.blockNormalPistons);

            // GENERAL BLOCK-PLACE/BREAK
            this.allowBlockPlace = config.getBoolean("events.block.allowPlace", this.allowBlockPlace);
            this.allowBlockBreak = config.getBoolean("events.block.allowBreak", this.allowBlockBreak);

            // INTERACT
            this.allowOpenChest = config.getBoolean("events.use.chest", this.allowOpenChest);
            this.allowOpenFurnace = config.getBoolean("events.use.furnace", this.allowOpenFurnace);
            this.allowOpenDispenser = config.getBoolean("events.use.dispenser", this.allowOpenDispenser);
            this.allowOpenWorkbench = config.getBoolean("events.use.workbench", this.allowOpenWorkbench);

            // ENTITYS
            this.blockTNT = config.getBoolean("events.entity.tnt.blockExplosion", this.blockTNT);
            this.blockCreeperExplosions = config.getBoolean("events.entity.creeper.blockExplosion", this.blockCreeperExplosions);
            this.blockEndermanPickUp = config.getBoolean("events.entity.enderman.blockPickUp", this.blockEndermanPickUp);
            this.blockEndermanPlace = config.getBoolean("events.entity.enderman.blockPlace", this.blockEndermanPlace);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SAVE SETTINGS
     */
    public boolean saveSettings() {
        File file = new File(this.dataFolder, "config_" + this.worldName + ".yml");

        try {
            YamlConfiguration config = new YamlConfiguration();

            // PLAYER
            config.set("events.player.blockAutoRegainHealth", this.blockRegainHealth);
            config.set("events.player.pvpEnabled", this.pvpEnabled);

            // GENERAL
            config.set("events.world.blockFire", this.blockFire);
            config.set("events.world.blockIceForm", this.blockIceForm);
            config.set("events.world.blockIceMelt", this.blockIceMelt);
            config.set("events.world.blockSnowForm", this.blockSnowForm);
            config.set("events.world.blockSnowMelt", this.blockSnowMelt);

            // BLOCK-PHYSICS
            config.set("events.physics.leaves.blockDecay", this.blockLeavesDecay);
            config.set("events.physics.leaves.radius", this.blockLeavesDecayRadius);
            config.set("events.physics.portal.allowAnywhere", this.allowPortalAnywhere);
            config.set("events.physics.sponge.enable", this.enableSponge);
            config.set("events.physics.sponge.radius", this.SpongeRadius);
            config.set("events.physics.redstone.enabled", this.redstoneEnabled);

            // PISTONS
            config.set("events.pistons.blockSticky", this.blockStickyPistons);
            config.set("events.pistons.blockNormal", this.blockNormalPistons);

            // GENERAL BLOCK-PLACE/BREAK
            config.set("events.block.allowPlace", this.allowBlockPlace);
            config.set("events.block.allowBreak", this.allowBlockBreak);

            // INTERACT
            config.set("events.use.chest", this.allowOpenChest);
            config.set("events.use.furnace", this.allowOpenFurnace);
            config.set("events.use.dispenser", this.allowOpenDispenser);
            config.set("events.use.workbench", this.allowOpenWorkbench);

            // ENTITYS
            config.set("events.entity.tnt.blockExplosion", this.blockTNT);
            config.set("events.entity.creeper.blockExplosion", this.blockCreeperExplosions);
            config.set("events.entity.enderman.blockPickUp", this.blockEndermanPickUp);
            config.set("events.entity.enderman.blockPlace", this.blockEndermanPlace);

            // SAVE FILE
            config.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return the blockFire
     */
    public boolean isBlockFire() {
        return blockFire;
    }

    /**
     * @return the blockLeavesDecay
     */
    public boolean isBlockLeavesDecay() {
        return blockLeavesDecay;
    }

    /**
     * @return the blockCreeperExplosions
     */
    public boolean isBlockCreeperExplosions() {
        return blockCreeperExplosions;
    }

    /**
     * @return the blockRegainHealth
     */
    public boolean isBlockRegainHealth() {
        return blockRegainHealth;
    }

    /**
     * @return the blockIceForm
     */
    public boolean isBlockIceForm() {
        return blockIceForm;
    }

    /**
     * @return the blockIceMelt
     */
    public boolean isBlockIceMelt() {
        return blockIceMelt;
    }

    /**
     * @return the blockSnowForm
     */
    public boolean isBlockSnowForm() {
        return blockSnowForm;
    }

    /**
     * @return the blockSnowMelt
     */
    public boolean isBlockSnowMelt() {
        return blockSnowMelt;
    }

    /**
     * @return the blockTNT
     */
    public boolean isBlockTNT() {
        return blockTNT;
    }

    /**
     * @return the blockStickyPistons
     */
    public boolean isBlockStickyPistons() {
        return blockStickyPistons;
    }

    /**
     * @return the blockNormalPistons
     */
    public boolean isBlockNormalPistons() {
        return blockNormalPistons;
    }

    /**
     * @return the allowPortalAnywhere
     */
    public boolean isAllowPortalAnywhere() {
        return allowPortalAnywhere;
    }

    /**
     * @return the enableSponge
     */
    public boolean isEnableSponge() {
        return enableSponge;
    }

    /**
     * @return the allowBlockPlace
     */
    public boolean isAllowBlockPlace() {
        return allowBlockPlace;
    }

    /**
     * @return the allowBlockBreak
     */
    public boolean isAllowBlockBreak() {
        return allowBlockBreak;
    }

    /**
     * @return the allowOpenChest
     */
    public boolean isAllowOpenChest() {
        return allowOpenChest;
    }

    /**
     * @return the allowOpenFurnace
     */
    public boolean isAllowOpenFurnace() {
        return allowOpenFurnace;
    }

    /**
     * @return the allowOpenDispenser
     */
    public boolean isAllowOpenDispenser() {
        return allowOpenDispenser;
    }

    /**
     * @return the allowOpenWorkbench
     */
    public boolean isAllowOpenWorkbench() {
        return allowOpenWorkbench;
    }

    /**
     * @return the blockEndermanPickUp
     */
    public boolean isBlockEndermanPickUp() {
        return blockEndermanPickUp;
    }

    /**
     * @return the blockEndermanPlace
     */
    public boolean isBlockEndermanPlace() {
        return blockEndermanPlace;
    }

    /**
     * @return the pvpEnabled
     */
    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    /**
     * @return the redstoneEnabled
     */
    public boolean isRedstoneEnabled() {
        return redstoneEnabled;
    }

    /**
     * @return the blockLeavesDecayRadius
     */
    public int getBlockLeavesDecayRadius() {
        return blockLeavesDecayRadius;
    }

    /**
     * @return the spongeRadius
     */
    public int getSpongeRadius() {
        return SpongeRadius;
    }

    /**
     * @param blockFire
     *            the blockFire to set
     */
    public void setBlockFire(boolean blockFire) {
        this.blockFire = blockFire;
    }

    /**
     * @param blockLeavesDecay
     *            the blockLeavesDecay to set
     */
    public void setBlockLeavesDecay(boolean blockLeavesDecay) {
        this.blockLeavesDecay = blockLeavesDecay;
    }

    /**
     * @param blockCreeperExplosions
     *            the blockCreeperExplosions to set
     */
    public void setBlockCreeperExplosions(boolean blockCreeperExplosions) {
        this.blockCreeperExplosions = blockCreeperExplosions;
    }

    /**
     * @param blockRegainHealth
     *            the blockRegainHealth to set
     */
    public void setBlockRegainHealth(boolean blockRegainHealth) {
        this.blockRegainHealth = blockRegainHealth;
    }

    /**
     * @param blockIceForm
     *            the blockIceForm to set
     */
    public void setBlockIceForm(boolean blockIceForm) {
        this.blockIceForm = blockIceForm;
    }

    /**
     * @param blockIceMelt
     *            the blockIceMelt to set
     */
    public void setBlockIceMelt(boolean blockIceMelt) {
        this.blockIceMelt = blockIceMelt;
    }

    /**
     * @param blockSnowForm
     *            the blockSnowForm to set
     */
    public void setBlockSnowForm(boolean blockSnowForm) {
        this.blockSnowForm = blockSnowForm;
    }

    /**
     * @param blockSnowMelt
     *            the blockSnowMelt to set
     */
    public void setBlockSnowMelt(boolean blockSnowMelt) {
        this.blockSnowMelt = blockSnowMelt;
    }

    /**
     * @param blockTNT
     *            the blockTNT to set
     */
    public void setBlockTNT(boolean blockTNT) {
        this.blockTNT = blockTNT;
    }

    /**
     * @param blockStickyPistons
     *            the blockStickyPistons to set
     */
    public void setBlockStickyPistons(boolean blockStickyPistons) {
        this.blockStickyPistons = blockStickyPistons;
    }

    /**
     * @param blockNormalPistons
     *            the blockNormalPistons to set
     */
    public void setBlockNormalPistons(boolean blockNormalPistons) {
        this.blockNormalPistons = blockNormalPistons;
    }

    /**
     * @param allowPortalAnywhere
     *            the allowPortalAnywhere to set
     */
    public void setAllowPortalAnywhere(boolean allowPortalAnywhere) {
        this.allowPortalAnywhere = allowPortalAnywhere;
    }

    /**
     * @param enableSponge
     *            the enableSponge to set
     */
    public void setEnableSponge(boolean enableSponge) {
        this.enableSponge = enableSponge;
    }

    /**
     * @param allowBlockPlace
     *            the allowBlockPlace to set
     */
    public void setAllowBlockPlace(boolean allowBlockPlace) {
        this.allowBlockPlace = allowBlockPlace;
    }

    /**
     * @param allowBlockBreak
     *            the allowBlockBreak to set
     */
    public void setAllowBlockBreak(boolean allowBlockBreak) {
        this.allowBlockBreak = allowBlockBreak;
    }

    /**
     * @param allowOpenChest
     *            the allowOpenChest to set
     */
    public void setAllowOpenChest(boolean allowOpenChest) {
        this.allowOpenChest = allowOpenChest;
    }

    /**
     * @param allowOpenFurnace
     *            the allowOpenFurnace to set
     */
    public void setAllowOpenFurnace(boolean allowOpenFurnace) {
        this.allowOpenFurnace = allowOpenFurnace;
    }

    /**
     * @param allowOpenDispenser
     *            the allowOpenDispenser to set
     */
    public void setAllowOpenDispenser(boolean allowOpenDispenser) {
        this.allowOpenDispenser = allowOpenDispenser;
    }

    /**
     * @param allowOpenWorkbench
     *            the allowOpenWorkbench to set
     */
    public void setAllowOpenWorkbench(boolean allowOpenWorkbench) {
        this.allowOpenWorkbench = allowOpenWorkbench;
    }

    /**
     * @param blockEndermanPickUp
     *            the blockEndermanPickUp to set
     */
    public void setBlockEndermanPickUp(boolean blockEndermanPickUp) {
        this.blockEndermanPickUp = blockEndermanPickUp;
    }

    /**
     * @param blockEndermanPlace
     *            the blockEndermanPlace to set
     */
    public void setBlockEndermanPlace(boolean blockEndermanPlace) {
        this.blockEndermanPlace = blockEndermanPlace;
    }

    /**
     * @param pvpEnabled
     *            the pvpEnabled to set
     */
    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    /**
     * @param redstoneEnabled
     *            the redstoneEnabled to set
     */
    public void setRedstoneEnabled(boolean redstoneEnabled) {
        this.redstoneEnabled = redstoneEnabled;
    }

    /**
     * @param blockLeavesDecayRadius
     *            the blockLeavesDecayRadius to set
     */
    public void setBlockLeavesDecayRadius(int blockLeavesDecayRadius) {
        this.blockLeavesDecayRadius = blockLeavesDecayRadius;
    }

    /**
     * @param spongeRadius
     *            the spongeRadius to set
     */
    public void setSpongeRadius(int spongeRadius) {
        SpongeRadius = spongeRadius;
    }
}