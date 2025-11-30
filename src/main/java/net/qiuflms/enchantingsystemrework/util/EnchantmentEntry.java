package net.qiuflms.enchantingsystemrework.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper.Rarity;

public class EnchantmentEntry {
    private final RegistryKey<Enchantment> registryKey;
    private final EnchantmentLevel level;
    private final Rarity rarity;

    public EnchantmentEntry(RegistryKey<Enchantment> enchantment, int overworld, int nether, int end, Rarity rarity){
        this(enchantment, new EnchantmentLevel(overworld, nether, end),rarity);
    }

    public EnchantmentEntry(RegistryKey<Enchantment> registryKey, EnchantmentLevel level, Rarity rarity){
        this.registryKey = registryKey;
        this.level = level;
        this.rarity = rarity;
    }


    public Rarity getRarity() {
        return rarity;
    }

    public EnchantmentLevel getLevel() {
        return level;
    }

    public RegistryKey<Enchantment> getKey() {
        return registryKey;
    }

    public Enchantment getEnchantment(){
        return getEntry().value();
    }

    public RegistryEntry<Enchantment> getEntry(){
        return EnchantmentsHelper.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(registryKey);

    }

    public static RegistryEntry<Enchantment> getEntry(RegistryKey<Enchantment> enchantmentRegistryKey){
        return EnchantmentsHelper.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantmentRegistryKey);
    }
}
