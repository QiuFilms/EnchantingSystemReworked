package net.qiuflms.enchantingsystemrework.recipe.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.recipe.ModRecipes;

public class ScrollToBookRecipe extends SpecialCraftingRecipe {
    public ScrollToBookRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int books = 0;
        int scrolls = 0;
        ItemEnchantmentsComponent expectedEnchantment = null;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);

            if (stack.isEmpty()) continue;

            if (stack.isOf(Items.BOOK)) {
                books++;
            } else if (stack.isOf(ModItems.ENCHANTED_SCROLL)) {
                if(expectedEnchantment == null){
                    expectedEnchantment = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
                    scrolls++;
                }else{
                    RegistryEntry<Enchantment> expected = stack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                            .getEnchantmentEntries().iterator().next().getKey();
                    RegistryEntry<Enchantment> current = stack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                            .getEnchantmentEntries().iterator().next().getKey();

                    if(expected.getKey().get() != current.getKey().get() || expectedEnchantment.getLevel(expected) != stack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(current)) return false;
                    scrolls++;
                }
            } else {
                return false;
            }
        }
        return books == 1 && scrolls == 3;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack resultBook = new ItemStack(Items.ENCHANTED_BOOK, 1);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if(!stack.isOf(ModItems.ENCHANTED_SCROLL)) continue;


            ItemEnchantmentsComponent scrollEnchantment = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            RegistryEntry<Enchantment> entry = scrollEnchantment.getEnchantmentEntries().iterator().next().getKey();

            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
            builder.add(entry, scrollEnchantment.getLevel(entry));
            resultBook.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
        }
        return resultBook;

    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.SCROLL_TO_BOOK_SERIALIZER;
    }
}
