package com.bridging.plunder;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EntityType;

public class EventHandler
{

    private final DropManager dropManager;
    private final DropOdds dropOdds;

    public EventHandler(Configuration configuration, DropOdds dropOdds){
        this.dropManager = new DropManager(configuration, dropOdds);
        this.dropOdds = dropOdds;
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
