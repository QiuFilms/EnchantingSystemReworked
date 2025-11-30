package net.qiuflms.enchantingsystemrework.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.block.custom.CustomEnchantingTable;

import java.util.function.Function;

public class ModBlocks {
    public static final Block CUSTOM_ENCHANTING_TABLE= registerBlock("custom_enchanting_table",
            properties -> new CustomEnchantingTable(properties.strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.STONE)));


    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(EnchantingSystemRework.MOD_ID, name));
        AbstractBlock.Settings settings = AbstractBlock.Settings.create();

        Block block = blockFactory.apply(settings.registryKey(blockKey));

        registerBlockItem(name, block);

        return Registry.register(Registries.BLOCK, Identifier.of(EnchantingSystemRework.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(EnchantingSystemRework.MOD_ID, name),
                new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(EnchantingSystemRework.MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        EnchantingSystemRework.LOGGER.info("Registering Mod Blocks for " + EnchantingSystemRework.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> entries.add(CUSTOM_ENCHANTING_TABLE));
    }
}
