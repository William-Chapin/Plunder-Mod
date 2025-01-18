package com.bridging.plunder;

import com.bridging.plunder.commands.ReloadCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;


public class Plunder implements ModInitializer {

    public static final String messagePrefix = "[Plunder v1.1] - ";

    @Override
    public void onInitialize() {
        System.out.println("[Plunder v1.1] - Plunder has started.");

        Configuration configuration = new Configuration();
        DropOdds dropOdds = new DropOdds();
        ParticleManager particleManager = new ParticleManager();
        EventHandler eventHandler = new EventHandler(configuration, dropOdds, particleManager);
        eventHandler.registerAll();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReloadCommand.register(dispatcher, configuration, dropOdds, eventHandler.getDropManager());
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerLevel serverLevel){
                particleManager.update(serverLevel);
            }
        });
    }


}