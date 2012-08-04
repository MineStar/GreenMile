/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of GreenMile.
 * 
 * GreenMile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * GreenMile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GreenMile.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.greenmile;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.greenmile.core.GreenMileCore;
import de.minestar.greenmile.listener.EventBlockListener;
import de.minestar.greenmile.listener.EventEntityListener;
import de.minestar.greenmile.listener.EventPlayerListener;
public class Settings {

    private String CONFIG_FILE = "config.yml";
    private File DATAFOLDER;

    //@formatter:off

   // private Event.Priority MONITOR_LEVEL        = Event.Priority.Lowest;
    
    // MONITOR SETTINGS - BLOCKLISTENER
    private boolean monitorBlockBreak           = true;
    private boolean monitorBlockBurn            = true;
    private boolean monitorBlockFade            = true;
    private boolean monitorBlockForm            = true;
    private boolean monitorBlockFromTo          = true;
    private boolean monitorBlockIgnite          = true;
    private boolean monitorBlockPhysics         = true;
    private boolean monitorBlockPistonExtend    = true;
    private boolean monitorBlockPistonRetract   = true;
    private boolean monitorBlockPlace           = true;
    private boolean monitorBlockSpread          = true;
    private boolean monitorBlockLeavesDecay     = true;
    
    // MONITOR SETTINGS - ENTITYLISTENER
    private boolean monitorEntityEndermanPickup     = true;
    private boolean monitorEntityEndermanPlace      = true;
    private boolean monitorEntityDamage             = true;
    private boolean monitorEntityExplode            = true;
    private boolean monitorEntityRegainHealth       = true;
    private boolean monitorEntityFoodLevelChange    = true;
    
    // MONITOR SETTINGS - PLAYERLISTENER
    private boolean monitorPlayerBucketEmpty    = true;
    private boolean monitorPlayerBucketFill     = true;
    private boolean monitorPlayerInteract       = true;
    private boolean monitorPlayerInteractEntity = true;
    
    //@formatter:on

    /**
     * Constructor
     * 
     * @param configFile
     * @param dataFolder
     */
    public Settings(String configFile, File dataFolder) {
        this.CONFIG_FILE = configFile;
        this.DATAFOLDER = dataFolder;
    }

    /**
     * Register all events
     */
    public void registerAllEvents() {
        EventBlockListener bListener = new EventBlockListener();
        EventEntityListener eListener = new EventEntityListener();
        EventPlayerListener pListener = new EventPlayerListener();

        Bukkit.getPluginManager().registerEvents(bListener, GreenMileCore.INSTANCE);
        Bukkit.getPluginManager().registerEvents(eListener, GreenMileCore.INSTANCE);
        Bukkit.getPluginManager().registerEvents(pListener, GreenMileCore.INSTANCE);
//        
//        //@formatter:off
//        // BLOCKLISTENER
//        
//        this.registerEvent(this.monitorBlockBreak,          bListener, Event.Type.BLOCK_BREAK);
//        this.registerEvent(this.monitorBlockBurn,           bListener, Event.Type.BLOCK_BURN);
//        this.registerEvent(this.monitorBlockFade,           bListener, Event.Type.BLOCK_FADE);
//        this.registerEvent(this.monitorBlockForm,           bListener, Event.Type.BLOCK_FORM);
//        this.registerEvent(this.monitorBlockFromTo,         bListener, Event.Type.BLOCK_FROMTO);
//        this.registerEvent(this.monitorBlockIgnite,         bListener, Event.Type.BLOCK_IGNITE);
//        this.registerEvent(this.monitorBlockPhysics,        bListener, Event.Type.BLOCK_PHYSICS);
//        this.registerEvent(this.monitorBlockPistonExtend,   bListener, Event.Type.BLOCK_PISTON_EXTEND);
//        this.registerEvent(this.monitorBlockPistonRetract,  bListener, Event.Type.BLOCK_PISTON_RETRACT);
//        this.registerEvent(this.monitorBlockPlace,          bListener, Event.Type.BLOCK_PLACE);
//        this.registerEvent(this.monitorBlockSpread,         bListener, Event.Type.BLOCK_SPREAD);
//        this.registerEvent(this.monitorBlockLeavesDecay,    bListener, Event.Type.LEAVES_DECAY);
//        
//        // ENTITYLISTENER
//        this.registerEvent(this.monitorEntityEndermanPickup,    eListener, Event.Type.ENDERMAN_PICKUP);
//        this.registerEvent(this.monitorEntityEndermanPlace,     eListener, Event.Type.ENDERMAN_PLACE);
//        this.registerEvent(this.monitorEntityDamage,            eListener, Event.Type.ENTITY_DAMAGE);
//        this.registerEvent(this.monitorEntityExplode,           eListener, Event.Type.ENTITY_EXPLODE);
//        this.registerEvent(this.monitorEntityRegainHealth,      eListener, Event.Type.ENTITY_REGAIN_HEALTH);
//        this.registerEvent(this.monitorEntityFoodLevelChange,   eListener, Event.Type.FOOD_LEVEL_CHANGE);
//        
//        // PLAYERLISTENER
//        this.registerEvent(this.monitorPlayerBucketEmpty,       pListener, Event.Type.PLAYER_BUCKET_EMPTY);
//        this.registerEvent(this.monitorPlayerBucketFill,        pListener, Event.Type.PLAYER_BUCKET_FILL);
//        this.registerEvent(this.monitorPlayerInteract,          pListener, Event.Type.PLAYER_INTERACT);
//        this.registerEvent(this.monitorPlayerInteractEntity,    pListener, Event.Type.PLAYER_INTERACT_ENTITY);      
//        //@formatter:on
    }

//    /**
//     * Register a single event
//     * 
//     * @param registerListener
//     * @param listener
//     * @param eventType
//     */
//    private void registerEvent(boolean registerListener, Listener listener, Event.Type eventType) {
//        if (!registerListener)
//            return;
//
//        Bukkit.getPluginManager().registerEvent(eventType, listener, this.MONITOR_LEVEL, Main.getInstance());
//    }

    /**
     * SAVE SETTINGS
     * 
     * @return <b>true</b> if saving was successful, otherwise <b>false</b>
     */
    private boolean saveSettings() {
        try {
            File file = new File(this.DATAFOLDER, this.CONFIG_FILE);
            boolean flagExists = true;
            if (!file.exists()) {
                flagExists = false;
            }

            YamlConfiguration config = new YamlConfiguration();
            if (flagExists)
                config.load(file);

            // @formatter:off
            // MONITOR SETTINGS - LEVEL
            //config.set("monitor.level",                     this.MONITOR_LEVEL.toString());
            
            // MONITOR SETTINGS - BLOCKLISTENER
            config.set("monitor.block.break",               this.monitorBlockBreak);
            config.set("monitor.block.burn",                this.monitorBlockBurn);
            config.set("monitor.block.fade",                this.monitorBlockFade);
            config.set("monitor.block.form",                this.monitorBlockForm);
            config.set("monitor.block.fromTo",              this.monitorBlockFromTo);
            config.set("monitor.block.ignite",              this.monitorBlockIgnite);
            config.set("monitor.block.physics",             this.monitorBlockPhysics);
            config.set("monitor.block.pistonExtend",        this.monitorBlockPistonExtend);
            config.set("monitor.block.pistonRetract",       this.monitorBlockPistonRetract);
            config.set("monitor.block.place",               this.monitorBlockPlace);
            config.set("monitor.block.spread",              this.monitorBlockSpread);
            config.set("monitor.block.leavesDecay",         this.monitorBlockLeavesDecay);

            // MONITOR SETTINGS - ENTITYLISTENER
            config.set("monitor.entity.endermanPickup",     this.monitorEntityEndermanPickup);
            config.set("monitor.entity.endermanPlace",      this.monitorEntityEndermanPlace);
            config.set("monitor.entity.damage",             this.monitorEntityDamage);
            config.set("monitor.entity.explode",            this.monitorEntityExplode);
            config.set("monitor.entity.regainHealth",       this.monitorEntityRegainHealth);
            config.set("monitor.entity.foodLevelChange",    this.monitorEntityFoodLevelChange);

            // MONITOR SETTINGS - PLAYERLISTENER
            config.set("monitor.player.bucketEmpty",        this.monitorPlayerBucketEmpty);
            config.set("monitor.player.bucketFill",         this.monitorPlayerBucketFill);
            config.set("monitor.player.interact",           this.monitorPlayerInteract);
            config.set("monitor.player.interactEntity",     this.monitorPlayerInteractEntity);
            // @formatter:on

            config.save(file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * LOAD SETTINGS
     * 
     * @return <b>true</b> if loading was successful, otherwise <b>false</b>
     */
    public boolean loadSettings(boolean firstTry) {
        try {
            File file = new File(this.DATAFOLDER, this.CONFIG_FILE);
            // FILE DOES NOT EXIST => TRY TO CREATE IT
            if (!file.exists()) {
                // SAVING FAILS => LOADING FAILES TOO
                if (!this.saveSettings())
                    return false;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            // @formatter:off
            // MONITOR SETTINGS - LEVEL
//            Priority level = EnumHelper.getPriority(config.getString("monitor.level"));
//            if(level == null) {
//                throw new Exception("Priority not found!");
//            }
//            this.setMONITOR_LEVEL(level);
            
            // MONITOR SETTINGS - BLOCKLISTENER
            this.setMonitorBlockBreak(config.getBoolean("monitor.block.break"));
            this.setMonitorBlockBurn(config.getBoolean("monitor.block.burn"));
            this.setMonitorBlockFade(config.getBoolean("monitor.block.fade"));
            this.setMonitorBlockForm(config.getBoolean("monitor.block.form"));
            this.setMonitorBlockFromTo(config.getBoolean("monitor.block.fromTo"));
            this.setMonitorBlockIgnite(config.getBoolean("monitor.block.ignite"));
            this.setMonitorBlockPhysics(config.getBoolean("monitor.block.physics"));
            this.setMonitorBlockPistonExtend(config.getBoolean("monitor.block.pistonExtend"));
            this.setMonitorBlockPistonRetract(config.getBoolean("monitor.block.pistonRetract"));
            this.setMonitorBlockPlace(config.getBoolean("monitor.block.place"));
            this.setMonitorBlockSpread(config.getBoolean("monitor.block.spread"));
            this.setMonitorBlockLeavesDecay(config.getBoolean("monitor.block.leavesDecay"));
      
            // MONITOR SETTINGS - ENTITYLISTENER
            this.setMonitorEntityEndermanPickup(config.getBoolean("monitor.entity.endermanPickup"));
            this.setMonitorEntityEndermanPlace(config.getBoolean("monitor.entity.endermanPlace"));
            this.setMonitorEntityDamage(config.getBoolean("monitor.entity.damage"));
            this.setMonitorEntityExplode(config.getBoolean("monitor.entity.explode"));
            this.setMonitorEntityRegainHealth(config.getBoolean("monitor.entity.regainHealth"));
            this.setMonitorEntityFoodLevelChange(config.getBoolean("monitor.entity.foodLevelChange"));
            
            // MONITOR SETTINGS - PLAYERLISTENER
            this.setMonitorPlayerBucketEmpty(config.getBoolean("monitor.player.bucketEmpty"));
            this.setMonitorPlayerBucketFill(config.getBoolean("monitor.player.bucketFill"));
            this.setMonitorPlayerInteract(config.getBoolean("monitor.player.interact"));
            this.setMonitorPlayerInteractEntity(config.getBoolean("monitor.player.interactEntity"));
            // @formatter:on

            return true;
        } catch (Exception e) {
            // SAVING FAILS => LOADING FAILES TOO
            if (!this.saveSettings())
                return false;

            // TRY TO LOAD AGAIN
            if (firstTry)
                return this.loadSettings(false);
            else
                return false;
        }
    }

//    /**
//     * @param mONITOR_LEVEL
//     *            the mONITOR_LEVEL to set
//     */
//    private void setMONITOR_LEVEL(Event.Priority mONITOR_LEVEL) {
//         MONITOR_LEVEL = mONITOR_LEVEL;
//    }

    /**
     * @param monitorBlockBreak
     *            the monitorBlockBreak to set
     */
    private void setMonitorBlockBreak(boolean monitorBlockBreak) {
        this.monitorBlockBreak = monitorBlockBreak;
    }

    /**
     * @param monitorBlockBurn
     *            the monitorBlockBurn to set
     */
    private void setMonitorBlockBurn(boolean monitorBlockBurn) {
        this.monitorBlockBurn = monitorBlockBurn;
    }

    /**
     * @param monitorBlockFade
     *            the monitorBlockFade to set
     */
    private void setMonitorBlockFade(boolean monitorBlockFade) {
        this.monitorBlockFade = monitorBlockFade;
    }

    /**
     * @param monitorBlockForm
     *            the monitorBlockForm to set
     */
    private void setMonitorBlockForm(boolean monitorBlockForm) {
        this.monitorBlockForm = monitorBlockForm;
    }

    /**
     * @param monitorBlockFromTo
     *            the monitorBlockFromTo to set
     */
    private void setMonitorBlockFromTo(boolean monitorBlockFromTo) {
        this.monitorBlockFromTo = monitorBlockFromTo;
    }

    /**
     * @param monitorBlockIgnite
     *            the monitorBlockIgnite to set
     */
    private void setMonitorBlockIgnite(boolean monitorBlockIgnite) {
        this.monitorBlockIgnite = monitorBlockIgnite;
    }

    /**
     * @param monitorBlockPhysics
     *            the monitorBlockPhysics to set
     */
    private void setMonitorBlockPhysics(boolean monitorBlockPhysics) {
        this.monitorBlockPhysics = monitorBlockPhysics;
    }

    /**
     * @param monitorBlockPistonExtend
     *            the monitorBlockPistonExtend to set
     */
    private void setMonitorBlockPistonExtend(boolean monitorBlockPistonExtend) {
        this.monitorBlockPistonExtend = monitorBlockPistonExtend;
    }

    /**
     * @param monitorBlockPistonRetract
     *            the monitorBlockPistonRetract to set
     */
    private void setMonitorBlockPistonRetract(boolean monitorBlockPistonRetract) {
        this.monitorBlockPistonRetract = monitorBlockPistonRetract;
    }

    /**
     * @param monitorBlockPlace
     *            the monitorBlockPlace to set
     */
    private void setMonitorBlockPlace(boolean monitorBlockPlace) {
        this.monitorBlockPlace = monitorBlockPlace;
    }

    /**
     * @param monitorBlockSpread
     *            the monitorBlockSpread to set
     */
    private void setMonitorBlockSpread(boolean monitorBlockSpread) {
        this.monitorBlockSpread = monitorBlockSpread;
    }

    /**
     * @param monitorBlockLeavesDecay
     *            the monitorBlockLeavesDecay to set
     */
    private void setMonitorBlockLeavesDecay(boolean monitorBlockLeavesDecay) {
        this.monitorBlockLeavesDecay = monitorBlockLeavesDecay;
    }

    /**
     * @param monitorEntityEndermanPickup
     *            the monitorEntityEndermanPickup to set
     */
    private void setMonitorEntityEndermanPickup(boolean monitorEntityEndermanPickup) {
        this.monitorEntityEndermanPickup = monitorEntityEndermanPickup;
    }

    /**
     * @param monitorEntityEndermanPlace
     *            the monitorEntityEndermanPlace to set
     */
    private void setMonitorEntityEndermanPlace(boolean monitorEntityEndermanPlace) {
        this.monitorEntityEndermanPlace = monitorEntityEndermanPlace;
    }

    /**
     * @param monitorEntityDamage
     *            the monitorEntityDamage to set
     */
    private void setMonitorEntityDamage(boolean monitorEntityDamage) {
        this.monitorEntityDamage = monitorEntityDamage;
    }

    /**
     * @param monitorEntityExplode
     *            the monitorEntityExplode to set
     */
    private void setMonitorEntityExplode(boolean monitorEntityExplode) {
        this.monitorEntityExplode = monitorEntityExplode;
    }

    /**
     * @param monitorEntityRegainHealth
     *            the monitorEntityRegainHealth to set
     */
    private void setMonitorEntityRegainHealth(boolean monitorEntityRegainHealth) {
        this.monitorEntityRegainHealth = monitorEntityRegainHealth;
    }

    /**
     * @param monitorEntityFoodLevelChange
     *            the monitorEntityFoodLevelChange to set
     */
    private void setMonitorEntityFoodLevelChange(boolean monitorEntityFoodLevelChange) {
        this.monitorEntityFoodLevelChange = monitorEntityFoodLevelChange;
    }

    /**
     * @param monitorPlayerBucketEmpty
     *            the monitorPlayerBucketEmpty to set
     */
    private void setMonitorPlayerBucketEmpty(boolean monitorPlayerBucketEmpty) {
        this.monitorPlayerBucketEmpty = monitorPlayerBucketEmpty;
    }

    /**
     * @param monitorPlayerBucketFill
     *            the monitorPlayerBucketFill to set
     */
    private void setMonitorPlayerBucketFill(boolean monitorPlayerBucketFill) {
        this.monitorPlayerBucketFill = monitorPlayerBucketFill;
    }

    /**
     * @param monitorPlayerInteract
     *            the monitorPlayerInteract to set
     */
    private void setMonitorPlayerInteract(boolean monitorPlayerInteract) {
        this.monitorPlayerInteract = monitorPlayerInteract;
    }

    /**
     * @param monitorPlayerInteractEntity
     *            the monitorPlayerInteractEntity to set
     */
    private void setMonitorPlayerInteractEntity(boolean monitorPlayerInteractEntity) {
        this.monitorPlayerInteractEntity = monitorPlayerInteractEntity;
    }
}
