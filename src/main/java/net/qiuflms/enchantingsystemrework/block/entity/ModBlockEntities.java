package net.qiuflms.enchantingsystemrework.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.block.ModBlocks;
import net.qiuflms.enchantingsystemrework.block.entity.custom.CustomEnchantingBlockEntity;

public class ModBlockEntities {
    public static BlockEntityType<CustomEnchantingBlockEntity> CUSTOM_ENCHANTING_TABLE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(EnchantingSystemRework.MOD_ID, "custom_enchanting_table_entity"),
            FabricBlockEntityTypeBuilder.create(CustomEnchantingBlockEntity::new, ModBlocks.CUSTOM_ENCHANTING_TABLE).build()
    );



    public static void registerBlockEntities() {
        EnchantingSystemRework.LOGGER.info("BlockEntities");
    }

}
