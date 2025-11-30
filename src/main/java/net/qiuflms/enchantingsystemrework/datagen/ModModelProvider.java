package net.qiuflms.enchantingsystemrework.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.block.ModBlocks;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {


    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        Identifier vanillaModelId = ModelIds.getBlockModelId(Blocks.ENCHANTING_TABLE);

        // Rejestrujemy Twój blok, mówiąc mu, żeby używał TEGO waniliowego ID
        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(
                        ModBlocks.CUSTOM_ENCHANTING_TABLE,
                        BlockStateModelGenerator.createWeightedVariant(vanillaModelId)
                )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ENCHANTED_SCROLL, Models.GENERATED);


        Identifier vanillaModelId = ModelIds.getBlockModelId(Blocks.ENCHANTING_TABLE);
        itemModelGenerator.register(
                ModBlocks.CUSTOM_ENCHANTING_TABLE.asItem(),
                new Model(Optional.of(vanillaModelId), Optional.empty())
        );
    }
}
