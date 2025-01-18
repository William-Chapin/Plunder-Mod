package com.bridging.plunder;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParticleManager {
    private final Map<ServerPlayer, Integer> playerParticleTicks = new HashMap<>();

    public void update(ServerLevel level){
        Iterator<Map.Entry<ServerPlayer, Integer>> iterator = playerParticleTicks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ServerPlayer, Integer> entry = iterator.next();
            ServerPlayer player = entry.getKey();
            int ticks = entry.getValue();
            if (ticks > 0){
                spawnParticles(player);
                playerParticleTicks.put(player, ticks - 1);
            } else {
                iterator.remove();
            }
        }
    }

    public void startParticles(ServerPlayer player){
        playerParticleTicks.put(player, 5);
    }

    public void spawnParticles(ServerPlayer player){
        Vec3 position = player.position().add(0, -0.4, 0);
        ServerLevel level = (ServerLevel) player.level();
        double radius = 1;
        double particleAmount = 5;
        for (int i = 0; i < particleAmount; i++){
            double angle = 2 * Math.PI * i / particleAmount;
            double xof = radius * Math.cos(angle);
            double zof = radius * Math.sin(angle);
            Vec3 particlePos = position.add(xof, 0, zof);
            Vec3 velo = new Vec3(xof, 0, zof).normalize().scale(0.05);
            level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, particlePos.x, particlePos.y, particlePos.z, 1, velo.x, 0.1, velo.z, 0.1);
        }
    }
}
