package com.bridging.plunder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DropManager
{
    public void dropFrom(Entity entity, float damageTaken){
        ServerPlayer player = (ServerPlayer) entity;
        List<ItemStack> items = new ArrayList<>();
        player.getInventory().items.forEach(itemStack -> {
            if ((itemStack.is(Items.AIR)) || (itemStack.isEmpty()))  { return; }
            // 10% chance to add to drop list
            if (Math.random() < 0.5){
                items.add(itemStack);
            }
        });
        items.forEach(itemStack -> {
            System.out.println("Dropping: " + itemStack.getItem());
            player.getInventory().removeItem(itemStack);
            CreativeModeTab creativeTab = getCreativeTab(itemStack);
            System.out.println("Creative Tab: " + creativeTab);
//            double[] scatter = getScatter();
            player.drop(itemStack, true, false);
        });
    }

    public static CreativeModeTab getCreativeTab(ItemStack itemStack) {
        for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
            Collection<ItemStack> displayItems = tab.getDisplayItems();
            for (ItemStack displayItem : displayItems) {
                if (ItemStack.isSameItem(itemStack, displayItem)) {
                    return tab;
                }
            }
        }
        return null;
    }
}
