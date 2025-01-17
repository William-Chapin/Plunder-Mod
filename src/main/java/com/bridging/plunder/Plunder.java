package com.bridging.plunder;

import net.fabricmc.api.ModInitializer;
import java.util.Map;

public class Plunder implements ModInitializer {

    private Map<String, Double> dropOddsMap;

    @Override
    public void onInitialize() {
        System.out.println("[Plunder v1.1] - Plunder has started.");

        Configuration configuration = new Configuration();
        DropOdds dropOdds = new DropOdds();
        EventHandler eventHandler = new EventHandler(configuration, dropOdds);
        eventHandler.registerAll();
    }

}