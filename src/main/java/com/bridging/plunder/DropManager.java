package com.bridging.plunder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class DropManager
{
    private final Configuration configuration;
    private final DropOdds dropOdds;

    public DropManager(Configuration configuration, DropOdds dropOdds){
        this.configuration = configuration;
        this.dropOdds = dropOdds;
    }

    public void dropFrom(Entity entity, float damageTaken){
        ServerPlayer player = (ServerPlayer) entity;
        List<ItemStack> toDrop = new ArrayList<>();

        // loop inventory and determine what blocks to drop
        player.getInventory().items.forEach(itemStack -> {
            if ((itemStack.is(Items.AIR)) || (itemStack.isEmpty()))  { return; }

            // get the drop chance from the odds.json  (ADD DEFAULT ODDS)
            String itemName = itemStack.getItem().toString();
            itemName = itemName.substring(itemName.indexOf(":") + 1);
            double dropChance = dropOdds.getDropChance(itemName).orElse(0.0);
            System.out.println("Drop chance: " + dropChance);
            if (Math.random() < dropChance){
                toDrop.add(itemStack);
            }
        });

        // drop all the items
        toDrop.forEach(itemStack -> {
            player.getInventory().removeItem(itemStack);
            // remove cooldown for picking up
            ItemEntity itemEntity = player.drop(itemStack, true, false);
            int pickUpTime = Integer.parseInt(configuration.getProperty("pickUpTime"));
            itemEntity.setPickUpDelay(pickUpTime);
        });
    }
}
