package net.qiuflms.enchantingsystemrework.loot_function.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;

public class SimpleScrollEnchant implements LootFunction {
    public static final MapCodec<SimpleScrollEnchant> CODEC = MapCodec.unit(new SimpleScrollEnchant());

    @Override
    public LootFunctionType<SimpleScrollEnchant> getType() {
        return ModLootFunctions.SIMPLE_SCROLL_ENCHANT;
    }

    public static Builder builder() {
        return SimpleScrollEnchant::new;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        if (!stack.isOf(ModItems.ENCHANTED_SCROLL) && !stack.isOf(Items.BOOK)) return stack;

        if(stack.isOf(Items.BOOK)){
            stack = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
        }

        Random random = context.getRandom();
        ServerWorld world = context.getWorld();
        double luck = context.getLuck();

        EnchantmentEntry enchantment = EnchantmentsHelper.generateSingleEnchantmentWeighted(world.getRegistryKey(), random, luck);
        int level = EnchantmentsHelper.generateEnchantmentLevel(world.getRegistryKey(), random ,enchantment);


        EnchantmentsHelper.applyEnchantmentComponent(stack, enchantment, level);

        return stack;
    }
}