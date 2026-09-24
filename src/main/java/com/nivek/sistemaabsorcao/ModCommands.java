package com.nivek.sistemaabsorcao;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModCommands {
    private ModCommands() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.register(ModCommands.class);
    }

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("absorcao_toggle")
                .then(Commands.argument("ability", StringArgumentType.word())
                    .executes(ctx -> {
                        var p = ctx.getSource().getPlayerOrException();
                        String id = StringArgumentType.getString(ctx, "ability");
                        if (!AbilityData.known().contains(id)) return 0;
                        AbilityData.toggle(p, id);
                        boolean on = AbilityData.equipped(p, id);
                        p.displayClientMessage(
                            net.minecraft.network.chat.Component.literal(
                                "§b" + AbilityData.name(id) + ": " + (on ? "§aEQUIPADA" : "§cDESEQUIPADA")
                            ), true);
                        return 1;
                    })
                )
        );
    }
}
