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
    private boolean allowOpenWorkbench = true;
    private boolean blockEndermanPickUp = true;
    private boolean blockEndermanPlace = true;
    private boolean redstoneEnabled = true;

    private int blockLeavesDecayRadius = 3;
    private int SpongeRadius = 3;
    private String worldName;
    private File dataFolder;

    public GMWorldEventOptions(String worldName, File dataFolder) {
        this.worldName = worldName;
        this.dataFolder = dataFolder;
        loadOrCreateSettings();
    }

    private void loadOrCreateSettings() {
        File file = new File(this.dataFolder, "config_" + this.worldName + ".yml");
        if (!file.exists()) {
            saveSettings();
            return;
        }
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            this.blockRegainHealth = config.getBoolean("events.player.blockRegainHealth", this.blockRegainHealth);

            this.blockFire = config.getBoolean("events.world.blockFire", this.blockFire);
            this.blockIceForm = config.getBoolean("events.world.blockIceForm", this.blockIceForm);
            this.blockIceMelt = config.getBoolean("events.world.blockIceMelt", this.blockIceMelt);
            this.blockSnowForm = config.getBoolean("events.world.blockSnowForm", this.blockSnowForm);
            this.blockSnowMelt = config.getBoolean("events.world.blockSnowMelt", this.blockSnowMelt);

            this.blockLeavesDecay = config.getBoolean("events.physics.leaves.blockDecay", this.blockLeavesDecay);
            this.blockLeavesDecayRadius = config.getInt("events.physics.leaves.radius", this.blockLeavesDecayRadius);
            this.allowPortalAnywhere = config.getBoolean("events.physics.portal.allowAnywhere", this.allowPortalAnywhere);
            this.enableSponge = config.getBoolean("events.physics.sponge.enabled", this.enableSponge);
            this.SpongeRadius = config.getInt("events.physics.sponge.radius", this.SpongeRadius);
            this.redstoneEnabled = config.getBoolean("events.physics.redstone.enabled", this.redstoneEnabled);

            this.blockStickyPistons = config.getBoolean("events.pistons.blockSticky", this.blockStickyPistons);
            this.blockNormalPistons = config.getBoolean("events.pistons.blockNormal", this.blockNormalPistons);

            this.allowBlockPlace = config.getBoolean("events.block.allowPlace", this.allowBlockPlace);
            this.allowBlockBreak = config.getBoolean("events.block.allowBreak", this.allowBlockBreak);

            this.allowOpenChest = config.getBoolean("events.use.chest", this.allowOpenChest);
            this.allowOpenFurnace = config.getBoolean("events.use.furnace", this.allowOpenFurnace);
            this.allowOpenWorkbench = config.getBoolean("events.use.workbench", this.allowOpenWorkbench);

            this.blockTNT = config.getBoolean("events.entity.tnt.blockExplosion", this.blockTNT);
            this.blockCreeperExplosions = config.getBoolean("events.entity.creeper.blockExplosion", this.blockCreeperExplosions);
            this.blockEndermanPickUp = config.getBoolean("events.entity.enderman.blockPickUp", this.blockEndermanPickUp);
            this.blockEndermanPlace = config.getBoolean("events.entity.enderman.blockPlace", this.blockEndermanPlace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSettings() {
        File file = new File(this.dataFolder, "config_" + this.worldName + ".yml");
        if (!file.exists()) {
            file.delete();
            return;
        }
        try {
            YamlConfiguration config = new YamlConfiguration();

            config.set("events.player.blockRegainHealth", Boolean.valueOf(this.blockRegainHealth));

            config.set("events.world.blockFire", Boolean.valueOf(this.blockFire));
            config.set("events.world.blockIceForm", Boolean.valueOf(this.blockIceForm));
            config.set("events.world.blockIceMelt", Boolean.valueOf(this.blockIceMelt));
            config.set("events.world.blockSnowForm", Boolean.valueOf(this.blockSnowForm));
            config.set("events.world.blockSnowMelt", Boolean.valueOf(this.blockSnowMelt));

            config.set("events.physics.leaves.blockDecay", Boolean.valueOf(this.blockLeavesDecay));
            config.set("events.physics.leaves.radius", Integer.valueOf(this.blockLeavesDecayRadius));
            config.set("events.physics.portal.allowAnywhere", Boolean.valueOf(this.allowPortalAnywhere));
            config.set("events.physics.sponge.enable", Boolean.valueOf(this.enableSponge));
            config.set("events.physics.sponge.radius", Integer.valueOf(this.SpongeRadius));
            config.set("events.physics.redstone.enabled", Boolean.valueOf(this.redstoneEnabled));

            config.set("events.pistons.blockSticky", Boolean.valueOf(this.blockStickyPistons));
            config.set("events.pistons.blockNormal", Boolean.valueOf(this.blockNormalPistons));

            config.set("events.block.allowPlace", Boolean.valueOf(this.allowBlockPlace));
            config.set("events.block.allowBreak", Boolean.valueOf(this.allowBlockBreak));

            config.set("events.use.chest", Boolean.valueOf(this.allowOpenChest));
            config.set("events.use.furnace", Boolean.valueOf(this.allowOpenFurnace));
            config.set("events.use.workbench", Boolean.valueOf(this.allowOpenWorkbench));

            config.set("events.entity.tnt.blockExplosion", Boolean.valueOf(this.blockTNT));
            config.set("events.entity.creeper.blockExplosion", Boolean.valueOf(this.blockCreeperExplosions));
            config.set("events.entity.enderman.blockPickUp", Boolean.valueOf(this.blockEndermanPickUp));
            config.set("events.entity.enderman.blockPlace", Boolean.valueOf(this.blockEndermanPlace));

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBlockFire() {
        return this.blockFire;
    }

    public void setBlockFire(boolean blockFire) {
        this.blockFire = blockFire;
    }

    public boolean isBlockLeavesDecay() {
        return this.blockLeavesDecay;
    }

    public void setBlockLeavesDecay(boolean blockLeavesDecay) {
        this.blockLeavesDecay = blockLeavesDecay;
    }

    public boolean isBlockCreeperExplosions() {
        return this.blockCreeperExplosions;
    }

    public void setBlockCreeperExplosions(boolean blockCreeperExplosions) {
        this.blockCreeperExplosions = blockCreeperExplosions;
    }

    public boolean isBlockRegainHealth() {
        return this.blockRegainHealth;
    }

    public void setBlockRegainHealth(boolean blockRegainHealth) {
        this.blockRegainHealth = blockRegainHealth;
    }

    public int getBlockLeavesDecayRadius() {
        return this.blockLeavesDecayRadius;
    }

    public void setBlockLeavesDecayRadius(int blockLeavesDecayRadius) {
        this.blockLeavesDecayRadius = blockLeavesDecayRadius;
    }

    public boolean isBlockIceForm() {
        return this.blockIceForm;
    }

    public void setBlockIceForm(boolean blockIceForm) {
        this.blockIceForm = blockIceForm;
    }

    public boolean isBlockIceMelt() {
        return this.blockIceMelt;
    }

    public void setBlockIceMelt(boolean blockIceMelt) {
        this.blockIceMelt = blockIceMelt;
    }

    public boolean isBlockSnowForm() {
        return this.blockSnowForm;
    }

    public void setBlockSnowForm(boolean blockSnowForm) {
        this.blockSnowForm = blockSnowForm;
    }

    public boolean isBlockSnowMelt() {
        return this.blockSnowMelt;
    }

    public void setBlockSnowMelt(boolean blockSnowMelt) {
        this.blockSnowMelt = blockSnowMelt;
    }

    public boolean isAllowPortalAnywhere() {
        return this.allowPortalAnywhere;
    }

    public void setAllowPortalAnywhere(boolean allowPortalAnywhere) {
        this.allowPortalAnywhere = allowPortalAnywhere;
    }

    public int getSpongeRadius() {
        return this.SpongeRadius;
    }

    public void setSpongeRadius(int spongeRadius) {
        this.SpongeRadius = spongeRadius;
    }

    public boolean isBlockTNT() {
        return this.blockTNT;
    }

    public void setBlockTNT(boolean blockTNT) {
        this.blockTNT = blockTNT;
    }

    public boolean isAllowBlockPlace() {
        return this.allowBlockPlace;
    }

    public void setAllowBlockPlace(boolean allowBlockPlace) {
        this.allowBlockPlace = allowBlockPlace;
    }

    public boolean isAllowBlockBreak() {
        return this.allowBlockBreak;
    }

    public void setAllowBlockBreak(boolean allowBlockBreak) {
        this.allowBlockBreak = allowBlockBreak;
    }

    public boolean isAllowOpenChest() {
        return this.allowOpenChest;
    }

    public void setAllowOpenChest(boolean allowOpenChest) {
        this.allowOpenChest = allowOpenChest;
    }

    public boolean isAllowOpenFurnace() {
        return this.allowOpenFurnace;
    }

    public void setAllowOpenFurnace(boolean allowOpenFurnace) {
        this.allowOpenFurnace = allowOpenFurnace;
    }

    public boolean isAllowOpenWorkbench() {
        return this.allowOpenWorkbench;
    }

    public void setAllowOpenWorkbench(boolean allowOpenWorkbench) {
        this.allowOpenWorkbench = allowOpenWorkbench;
    }

    public boolean isBlockEndermanPickUp() {
        return this.blockEndermanPickUp;
    }

    public void setBlockEndermanPickUp(boolean blockEndermanPickUp) {
        this.blockEndermanPickUp = blockEndermanPickUp;
    }

    public boolean isBlockEndermanPlace() {
        return this.blockEndermanPlace;
    }

    public void setBlockEndermanPlace(boolean blockEndermanPlace) {
        this.blockEndermanPlace = blockEndermanPlace;
    }

    public boolean isBlockStickyPistons() {
        return this.blockStickyPistons;
    }

    public boolean isBlockNormalPistons() {
        return this.blockNormalPistons;
    }

    public void setBlockStickyPistons(boolean blockStickyPistons) {
        this.blockStickyPistons = blockStickyPistons;
    }

    public void setBlockNormalPistons(boolean blockNormalPistons) {
        this.blockNormalPistons = blockNormalPistons;
    }

    public boolean isEnableSponge() {
        return this.enableSponge;
    }

    public void setEnableSponge(boolean enableSponge) {
        this.enableSponge = enableSponge;
    }

    public boolean isRedstoneEnabled() {
        return this.redstoneEnabled;
    }

    public void setRedstoneEnabled(boolean redstoneEnabled) {
        this.redstoneEnabled = redstoneEnabled;
    }
}