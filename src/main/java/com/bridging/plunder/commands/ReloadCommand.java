package com.bridging.plunder.commands;

import com.bridging.plunder.Configuration;
import com.bridging.plunder.DropManager;
import com.bridging.plunder.DropOdds;
import com.bridging.plunder.Plunder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadCommand implements Command<CommandSourceStack> {
    private final Configuration configuration;
    private final DropOdds dropOdds;
    private final DropManager dropManager;

    public ReloadCommand(Configuration configuration, DropOdds dropOdds, DropManager dropManager) {
        this.configuration = configuration;
        this.dropOdds = dropOdds;
        this.dropManager = dropManager;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context){
        try{
            configuration.loadConfig();
            dropOdds.loadConfig();
            dropManager.updateConfig(configuration, dropOdds);
            context.getSource().sendSuccess(() -> Component.literal(Plunder.messagePrefix + "Configuration reloaded."), true);
        } catch (Exception e){
            context.getSource().sendSuccess(() -> Component.literal(Plunder.messagePrefix + "Failed to reload configuration."), true);
        }
        return 1;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Configuration configuration, DropOdds dropOdds, DropManager dropManager){
        dispatcher.register(Commands.literal("plunderreload")
                .requires(source -> source.hasPermission(2))
                .executes(new ReloadCommand(configuration,dropOdds, dropManager)));
    }
}
