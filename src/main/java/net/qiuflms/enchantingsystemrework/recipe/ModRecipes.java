package net.qiuflms.enchantingsystemrework.recipe;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.potions.ModPotions;
import net.qiuflms.enchantingsystemrework.recipe.custom.ScrollToBookRecipe;

public class ModRecipes {
    public static RecipeSerializer<ScrollToBookRecipe> SCROLL_TO_BOOK_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(EnchantingSystemRework.MOD_ID, "crafting_special_scroll_to_book"),
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(ScrollToBookRecipe::new)
    );;


    public static void registerBrewingRecipes() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    Items.GOLDEN_APPLE,
                    Potions.LUCK
            );

            builder.registerPotionRecipe(
                    Potions.LUCK,
                    Items.GOLD_INGOT,
                    ModPotions.LUCK_2
            );
        });
    }



    public static void registerRecipes() {
        EnchantingSystemRework.LOGGER.info("Registering special recipes");
    }
}
