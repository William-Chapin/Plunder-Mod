package com.bridging.plunder;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EntityType;

public class EventHandler
{

    private final DropManager dropManager;

    public EventHandler(){
        this.dropManager = new DropManager();
    }

    public void registerAll() {
        RegisterDamage();
    }

    public void RegisterDamage(){
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.getType() == EntityType.PLAYER){
                dropManager.dropFrom(entity, damageTaken);
            }
        });
    }
}
