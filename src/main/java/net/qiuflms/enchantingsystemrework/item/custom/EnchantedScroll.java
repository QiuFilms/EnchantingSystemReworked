package net.qiuflms.enchantingsystemrework.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public class EnchantedScroll extends Item {
    private int Level;
    private RegistryKey<Enchantment> Type;

    public EnchantedScroll(Settings settings) {
        super(settings);


    }
}
