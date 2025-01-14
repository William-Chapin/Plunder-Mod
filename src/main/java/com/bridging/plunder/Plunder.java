package com.bridging.plunder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Plunder implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("[Plunder v1.1] - Plunder has started.");
        registerLivingEntityHurtEvent();
    }

    private void registerLivingEntityHurtEvent() {
        ServerTickEvents.END_SERVER_TICK.register(server ->{
            server.getPlayerList().getPlayers().forEach(player -> {
                if (player.hurtMarked){
                    System.out.println("DAMAGE TAKEN");
                }
            });
        });
    }
}