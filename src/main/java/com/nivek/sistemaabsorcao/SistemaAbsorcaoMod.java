package com.nivek.sistemaabsorcao;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SistemaAbsorcaoMod.MODID)
public class SistemaAbsorcaoMod {
    public static final String MODID = "sistemaabsorcao";

    public SistemaAbsorcaoMod(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(new AbsorptionEvents());
        ModCommands.register();
    }
}
