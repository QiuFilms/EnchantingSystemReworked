package net.qiuflms.enchantingsystemrework.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;


import java.util.function.Function;


public class ModItems {
    public static final Item ENCHANTED_SCROLL = registerItem("enchanted_scroll",
            settings -> new Item(settings
                    .maxCount(1)
                    .rarity(Rarity.RARE)
                    .component(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
                    .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)

            ));

    public static void registerModItems(){
        EnchantingSystemRework.LOGGER.info("Mod Items");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ENCHANTED_SCROLL);

            RegistryWrapper.WrapperLookup lookup = entries.getContext().lookup();
            var enchantmentRegistry = lookup.getOrThrow(RegistryKeys.ENCHANTMENT);

            enchantmentRegistry.streamEntries().forEach(entry -> {
                int level = entry.value().getMaxLevel();

                for (int i = 1; i <= level; i++) {
                    ItemStack scrollStack = new ItemStack(ModItems.ENCHANTED_SCROLL);

                    ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
                    builder.set(entry, i);

                    scrollStack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());

                    entries.add(scrollStack);
                }
            });
        });

    }

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(EnchantingSystemRework.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(EnchantingSystemRework.MOD_ID, name)))));
    }
}
