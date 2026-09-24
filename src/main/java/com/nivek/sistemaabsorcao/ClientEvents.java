package com.nivek.sistemaabsorcao;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SistemaAbsorcaoMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    public static final KeyMapping OPEN = new KeyMapping(
        "key.sistemaabsorcao.abilities", GLFW.GLFW_KEY_G, "key.categories.sistemaabsorcao");

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(OPEN);
    }

    @Mod.EventBusSubscriber(modid = SistemaAbsorcaoMod.MODID, value = Dist.CLIENT)
    public static class ClientBus {
        @SubscribeEvent
        public static void tick(TickEvent.ClientTickEvent e) {
            if (e.phase != TickEvent.Phase.END) return;
            if (OPEN.consumeClick() && Minecraft.getInstance().screen == null) {
                Minecraft.getInstance().setScreen(new AbilityScreen());
            }
        }
    }

    public static class AbilityScreen extends Screen {
        public AbilityScreen() {
            super(net.minecraft.network.chat.Component.literal("Absorção de Habilidades"));
        }

        @Override protected void init() {
            int y = 45;
            for (String id : AbilityData.known()) {
                final String ability = id;
                addRenderableWidget(Button.builder(
                    net.minecraft.network.chat.Component.literal("Alternar: " + AbilityData.name(id)),
                    b -> {
                        var player = Minecraft.getInstance().player;
                        if (player != null) {
                            player.connection.sendCommand("absorcao_toggle " + ability);
                        }
                    }).bounds(20, y, 220, 20).build());
                y += 25;
            }
            addRenderableWidget(Button.builder(
                net.minecraft.network.chat.Component.literal("Fechar"),
                b -> onClose()).bounds(20, y + 10, 100, 20).build());
        }

        @Override public void render(GuiGraphics g, int mx, int my, float pt) {
            renderBackground(g, mx, my, pt);
            g.drawString(font, title, 20, 20, 0xFFFFFF);
            g.drawString(font, "G abre este menu. Cada botão equipa/desequipa.", 20, 32, 0xAAAAAA);
            super.render(g, mx, my, pt);
        }
    }
}
