package net.qiuflms.enchantingsystemrework.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.ComplexRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.block.ModBlocks;
import net.qiuflms.enchantingsystemrework.recipe.custom.ScrollToBookRecipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.DECORATIONS, ModBlocks.CUSTOM_ENCHANTING_TABLE)
                        .pattern(" B ")
                        .pattern("D#D")
                        .pattern("###")
                        .input('B', Items.BOOK)
                        .input('D', Items.DIAMOND)
                        .input('#', Items.OBSIDIAN)
                        .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                        .offerTo(exporter);


                ComplexRecipeJsonBuilder.create(ScrollToBookRecipe::new)
                        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(EnchantingSystemRework.MOD_ID, "scroll_to_book")));
            }
        };
    }

    @Override
    public String getName() {
        return "";
    }
}
