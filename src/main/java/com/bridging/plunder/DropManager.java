package com.bridging.plunder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DropManager
{
    private Configuration configuration;
    private DropOdds dropOdds;
    private ParticleManager particleManager;

    // Configuration Values
    private int pickUpTime;
    private boolean bypassKeepInventory;
    private boolean pvpDamage;
    private boolean cactusDamage;
    private boolean explosionDamage;
    private boolean fallDamage;
    private boolean fireDamage;
    private boolean mobDamage;
    private boolean drowningDamage;
    private boolean suffocationDamage;
    private boolean otherDamage;
    private double armorDropChance;
    private double toolDropChance;
    private double itemDropChance;
    private double maxDropAmount;
    private boolean enableParticles;

    public DropManager(Configuration configuration, DropOdds dropOdds, ParticleManager particleManager){
        this.configuration = configuration;
        this.dropOdds = dropOdds;
        this.particleManager = particleManager;

        this.pickUpTime = Integer.parseInt(configuration.getProperty("pickUpTime"));
        this.bypassKeepInventory = Boolean.parseBoolean(configuration.getProperty("bypassKeepInventory"));
        this.pvpDamage = Boolean.parseBoolean(configuration.getProperty("pvpDamage"));
        this.cactusDamage = Boolean.parseBoolean(configuration.getProperty("cactusDamage"));
        this.explosionDamage = Boolean.parseBoolean(configuration.getProperty("explosionDamage"));
        this.fallDamage = Boolean.parseBoolean(configuration.getProperty("fallDamage"));
        this.fireDamage = Boolean.parseBoolean(configuration.getProperty("fireDamage"));
        this.mobDamage = Boolean.parseBoolean(configuration.getProperty("mobDamage"));
        this.drowningDamage = Boolean.parseBoolean(configuration.getProperty("drowningDamage"));
        this.suffocationDamage = Boolean.parseBoolean(configuration.getProperty("suffocationDamage"));
        this.otherDamage = Boolean.parseBoolean(configuration.getProperty("otherDamage"));
        this.armorDropChance = Double.parseDouble(configuration.getProperty("armorDropChance"));
        this.toolDropChance = Double.parseDouble(configuration.getProperty("toolDropChance"));
        this.itemDropChance = Double.parseDouble(configuration.getProperty("itemDropChance"));
        this.maxDropAmount = Double.parseDouble(configuration.getProperty("maxDropAmount"));
        this.enableParticles = Boolean.parseBoolean(configuration.getProperty("enableParticles"));
    }

    public void updateConfig(Configuration configuration, DropOdds dropOdds){
        this.pickUpTime = Integer.parseInt(configuration.getProperty("pickUpTime"));
        this.bypassKeepInventory = Boolean.parseBoolean(configuration.getProperty("bypassKeepInventory"));
        this.pvpDamage = Boolean.parseBoolean(configuration.getProperty("pvpDamage"));
        this.cactusDamage = Boolean.parseBoolean(configuration.getProperty("cactusDamage"));
        this.explosionDamage = Boolean.parseBoolean(configuration.getProperty("explosionDamage"));
        this.fallDamage = Boolean.parseBoolean(configuration.getProperty("fallDamage"));
        this.fireDamage = Boolean.parseBoolean(configuration.getProperty("fireDamage"));
        this.mobDamage = Boolean.parseBoolean(configuration.getProperty("mobDamage"));
        this.drowningDamage = Boolean.parseBoolean(configuration.getProperty("drowningDamage"));
        this.suffocationDamage = Boolean.parseBoolean(configuration.getProperty("suffocationDamage"));
        this.otherDamage = Boolean.parseBoolean(configuration.getProperty("otherDamage"));
        this.armorDropChance = Double.parseDouble(configuration.getProperty("armorDropChance"));
        this.toolDropChance = Double.parseDouble(configuration.getProperty("toolDropChance"));
        this.itemDropChance = Double.parseDouble(configuration.getProperty("itemDropChance"));
        this.maxDropAmount = Double.parseDouble(configuration.getProperty("maxDropAmount"));
        this.enableParticles = Boolean.parseBoolean(configuration.getProperty("enableParticles"));
    }

    public void dropFrom(Entity entity, DamageSource source, boolean blocked){
        if (blocked) { return; }
        int keepInventory = Objects.requireNonNull(entity.level().getServer()).getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) ? 1 : 0;
        if (!bypassKeepInventory && keepInventory == 1) { return; }

        String sourceType = source.type().msgId();

        // MANAGE CONFIG SETTINGS
        if (!pvpDamage && sourceType.equals("player")) { return; }
        if (!cactusDamage && sourceType.equals("cactus")) { return; }
        if (!explosionDamage && sourceType.contains("explosion")) { return; }
        if (!fallDamage && sourceType.equals("fall")) { return; }
        if (!fireDamage && (sourceType.equals("inFire") || sourceType.equals("onFire") || sourceType.equals("lava"))) { return; }
        if (!mobDamage && sourceType.equals("mob")) { return; }
        if (!drowningDamage && sourceType.equals("drown")) { return; }
        if (!suffocationDamage && sourceType.equals("inWall")) { return; }
        if (!otherDamage && !sourceType.equals("player") && !sourceType.equals("cactus") &&
                !sourceType.contains("explosion") && !sourceType.equals("fall") && !sourceType.equals("inFire") &&
                !sourceType.equals("onFire") && !sourceType.equals("lava") && !sourceType.equals("mob") &&
                !sourceType.equals("drown") && !sourceType.equals("inWall")) {
            return;
        }

        // DROP ITEMS
        ServerPlayer player = (ServerPlayer) entity;
        List<ItemStack> toDrop = new ArrayList<>();

        player.getInventory().items.forEach(itemStack -> {
            if ((itemStack.is(Items.AIR)) || (itemStack.isEmpty()))  { return; }

            String itemName = itemStack.getItem().toString();
            itemName = itemName.substring(itemName.indexOf(":") + 1);
            boolean isArmor = itemName.contains("helmet") || itemName.contains("chestplate") || itemName.contains("leggings") || itemName.contains("boots");
            boolean isTool = itemName.contains("sword") || itemName.contains("pickaxe") || itemName.contains("axe") || itemName.contains("shovel") || itemName.contains("hoe") || itemName.contains("shears") || itemName.contains("trident") || itemName.contains("bow");

            if (isArmor){
                double dropChance = dropOdds.getDropChance(itemName).orElse(armorDropChance);
                if (Math.random() < dropChance){
                    toDrop.add(itemStack);
                }
            }
            else if(isTool){
                double dropChance = dropOdds.getDropChance(itemName).orElse(toolDropChance);
                if (Math.random() < dropChance){
                    toDrop.add(itemStack);
                }
            }
            else {
                double dropChance = dropOdds.getDropChance(itemName).orElse(itemDropChance);
                if (Math.random() < dropChance){
                    toDrop.add(itemStack);
                }
            }
        });

        toDrop.forEach(itemStack -> {
            int dropAmount = Math.min(itemStack.getCount(), (int) maxDropAmount);
            ItemStack dropStack = itemStack.split(dropAmount);
            ItemEntity itemEntity = player.drop(dropStack, true, false);
            player.getInventory().removeItem(dropStack);
            // SET COOLDOWN FOR PICKING UP
            itemEntity.setPickUpDelay(pickUpTime);
            // PARTICLES
            if (enableParticles){
                particleManager.startParticles(player);
            }
        });


    }
}
