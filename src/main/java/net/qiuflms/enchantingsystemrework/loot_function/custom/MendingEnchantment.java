package net.qiuflms.enchantingsystemrework.loot_function.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;

public class MendingEnchantment implements LootFunction {
    public static final MapCodec<MendingEnchantment> CODEC = MapCodec.unit(new MendingEnchantment());

    @Override
    public LootFunctionType<MendingEnchantment> getType() {
        return ModLootFunctions.MENDING_ENCHANT;
    }

    public static Builder builder() {
        return MendingEnchantment::new;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        if (!stack.isOf(ModItems.ENCHANTED_SCROLL) && !stack.isOf(Items.BOOK)) return stack;

        if(stack.isOf(Items.BOOK)){
            stack = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
        }

        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(stack.getEnchantments());
        builder.set(EnchantmentEntry.getEntry(Enchantments.MENDING), 1);
        stack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());

        return stack;
    }
}