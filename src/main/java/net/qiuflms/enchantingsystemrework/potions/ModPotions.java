package net.qiuflms.enchantingsystemrework.potions;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;


public class ModPotions {
    public static RegistryEntry<Potion> LUCK_2;
    public static RegistryEntry<Potion> LUCK_3;
    public static RegistryEntry<Potion> LUCK_4;
    public static RegistryEntry<Potion> LUCK_5;

    private static RegistryEntry<Potion> register(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(EnchantingSystemRework.MOD_ID, name), potion);
    }

    public static void registerPotions(){
        EnchantingSystemRework.LOGGER.info("Registering potions");

        LUCK_2 = register("luck_2", new Potion("luck",
                new StatusEffectInstance(StatusEffects.LUCK, 3600, 1)));

        LUCK_3 = register("luck_3", new Potion("luck",
                new StatusEffectInstance(StatusEffects.LUCK, 3600, 2)));

        LUCK_4 = register("luck_4", new Potion("luck",
                new StatusEffectInstance(StatusEffects.LUCK, 3600, 3)));

        LUCK_5 = register("luck_5", new Potion("luck",
                new StatusEffectInstance(StatusEffects.LUCK, 3600, 4)));
    }
}