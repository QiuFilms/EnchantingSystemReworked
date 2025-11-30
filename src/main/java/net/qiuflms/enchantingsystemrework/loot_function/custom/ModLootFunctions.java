package net.qiuflms.enchantingsystemrework.loot_function.custom;

import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;

public class ModLootFunctions {
    public static LootFunctionType<SimpleScrollEnchant> SIMPLE_SCROLL_ENCHANT = Registry.register(
            Registries.LOOT_FUNCTION_TYPE,
            Identifier.of(EnchantingSystemRework.MOD_ID, "simple_scroll_enchant"),
            new LootFunctionType<>(SimpleScrollEnchant.CODEC)
    );

    public static LootFunctionType<MendingEnchantment> MENDING_ENCHANT = Registry.register(
            Registries.LOOT_FUNCTION_TYPE,
            Identifier.of(EnchantingSystemRework.MOD_ID, "mending_enchant"),
            new LootFunctionType<>(MendingEnchantment.CODEC)
    );

    public static LootFunctionType<ItemEnchant> ITEM_ENCHANT = Registry.register(
            Registries.LOOT_FUNCTION_TYPE,
            Identifier.of(EnchantingSystemRework.MOD_ID, "item_enchant"),
            new LootFunctionType<>(ItemEnchant.CODEC)
    );

    public static void registerModLootFunctions(){
        EnchantingSystemRework.LOGGER.info("Mod loot tables");
    }
}
