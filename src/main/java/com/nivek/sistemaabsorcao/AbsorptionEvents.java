package com.nivek.sistemaabsorcao;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AbsorptionEvents {
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        ResourceLocation id = event.getEntity().getType().builtInRegistryHolder().key().location();
        String ability = AbilityData.fromMob(id.toString());
        if (ability == null) return;

        AbilityData.absorb(player, ability);
        int level = AbilityData.level(player, ability);
        int kills = AbilityData.kills(player, ability);
        player.displayClientMessage(
            net.minecraft.network.chat.Component.literal(
                "§aAbsorveu §f" + AbilityData.name(ability) +
                " §7— nível " + level + " §8(" + kills + " derrotas)"
            ), true);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        var p = event.player;

        if (AbilityData.equipped(p, "cow_resistance")) {
            int lvl = AbilityData.level(p, "cow_resistance");
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, Math.max(0, Math.min(3, lvl / 2)), true, false, false));
        }

        if (AbilityData.equipped(p, "wolf_instinct")) {
            int lvl = AbilityData.level(p, "wolf_instinct");
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, Math.max(0, Math.min(2, lvl / 2)), true, false, false));
        }

        if (AbilityData.equipped(p, "turtle_defense")) {
            int lvl = AbilityData.level(p, "turtle_defense");
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, Math.max(0, Math.min(3, lvl / 2)), true, false, false));
        }

        if (AbilityData.equipped(p, "chicken_feather")) {
            int lvl = AbilityData.level(p, "chicken_feather");
            if (p.fallDistance > 2.0F) {
                p.fallDistance = Math.max(0, p.fallDistance * (float)(1.0 - AbilityData.value("chicken_feather", lvl)));
            }
        }
    }

    @SubscribeEvent
    public void onJump(net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer p)) return;
        if (!AbilityData.equipped(p, "rabbit_jump")) return;
        int lvl = AbilityData.level(p, "rabbit_jump");
        double bonus = AbilityData.value("rabbit_jump", lvl);
        p.setDeltaMovement(p.getDeltaMovement().x(), p.getDeltaMovement().y() + bonus, p.getDeltaMovement().z());
        p.hurtMarked = true;
    }
}
