package com.bridging.plunder;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EntityType;

public class EventHandler
{

    private final DropManager dropManager;
    private final DropOdds dropOdds;
    private final ParticleManager particleManager;

    public EventHandler(Configuration configuration, DropOdds dropOdds, ParticleManager particleManager){
        this.dropManager = new DropManager(configuration, dropOdds, particleManager);
        this.dropOdds = dropOdds;
        this.particleManager = particleManager;
    }

    public void registerAll() {
        RegisterDamage();
    }

    public void RegisterDamage(){
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.getType() == EntityType.PLAYER){
                dropManager.dropFrom(entity, source, blocked);
            }
        });
    }

    public DropManager getDropManager(){
        return dropManager;
    }
}
