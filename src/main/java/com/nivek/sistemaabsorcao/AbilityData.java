package com.nivek.sistemaabsorcao;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public final class AbilityData {
    private static final String ROOT = "sistemaabsorcao";
    private static final String KILLS = "kills";
    private static final String LEVELS = "levels";
    private static final String EQUIPPED = "equipped";

    private AbilityData() {}

    private static CompoundTag root(Player p) {
        CompoundTag pd = p.getPersistentData();
        if (!pd.contains(ROOT)) pd.put(ROOT, new CompoundTag());
        return pd.getCompound(ROOT);
    }

    public static int kills(Player p, String id) {
        return root(p).getCompound(KILLS).getInt(id);
    }

    public static int level(Player p, String id) {
        return root(p).getCompound(LEVELS).getInt(id);
    }

    public static boolean equipped(Player p, String id) {
        return root(p).getCompound(EQUIPPED).getBoolean(id);
    }

    public static void absorb(Player p, String id) {
        CompoundTag r = root(p);
        CompoundTag ks = r.getCompound(KILLS);
        CompoundTag ls = r.getCompound(LEVELS);
        int k = ks.getInt(id) + 1;
        ks.putInt(id, k);
        int newLevel = Math.min(5, 1 + k / 10);
        if (newLevel > ls.getInt(id)) ls.putInt(id, newLevel);
        r.put(KILLS, ks);
        r.put(LEVELS, ls);
    }

    public static void toggle(Player p, String id) {
        if (level(p, id) <= 0) return;
        CompoundTag eq = root(p).getCompound(EQUIPPED);
        eq.putBoolean(id, !eq.getBoolean(id));
        root(p).put(EQUIPPED, eq);
    }

    public static List<String> known() {
        return List.of("rabbit_jump", "cow_resistance", "chicken_feather", "wolf_instinct", "turtle_defense");
    }

    public static String fromMob(String mobId) {
        return switch (mobId) {
            case "minecraft:rabbit" -> "rabbit_jump";
            case "minecraft:cow" -> "cow_resistance";
            case "minecraft:chicken" -> "chicken_feather";
            case "minecraft:wolf" -> "wolf_instinct";
            case "minecraft:turtle" -> "turtle_defense";
            default -> null;
        };
    }

    public static String name(String id) {
        return switch (id) {
            case "rabbit_jump" -> "Super Pulo";
            case "cow_resistance" -> "Resistência";
            case "chicken_feather" -> "Queda Suave";
            case "wolf_instinct" -> "Instinto";
            case "turtle_defense" -> "Defesa";
            default -> id;
        };
    }

    public static double value(String id, int level) {
        return switch (id) {
            case "rabbit_jump" -> Math.min(0.42 + level * 0.04, 0.62);
            case "cow_resistance" -> Math.min(level * 0.04, 0.20);
            case "chicken_feather" -> Math.min(level * 0.20, 1.0);
            case "wolf_instinct" -> Math.min(level * 0.02, 0.10);
            case "turtle_defense" -> Math.min(level * 0.03, 0.15);
            default -> 0;
        };
    }
}
