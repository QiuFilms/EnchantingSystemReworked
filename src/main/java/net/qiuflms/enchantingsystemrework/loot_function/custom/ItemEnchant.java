package net.qiuflms.enchantingsystemrework.loot_function.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;

import java.util.List;

public class ItemEnchant implements LootFunction {
    public static final MapCodec<ItemEnchant> CODEC = MapCodec.unit(new ItemEnchant());

    @Override
    public LootFunctionType<ItemEnchant> getType() {
        return ModLootFunctions.ITEM_ENCHANT;
    }

    public static Builder builder() {
        return ItemEnchant::new;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        EnchantingSystemRework.LOGGER.info("Item enchant: " + stack.getItem().getName());

        Random random = context.getRandom();
        ServerWorld world = context.getWorld();

        double luck = context.getLuck();

        List<EnchantmentEntry> enchantments = EnchantmentsHelper.generateMultipleEnchantmentsWeighted(world.getRegistryKey(), random, stack,
                List.of(1f, .5f, .25f), luck);
        List<Integer> level = EnchantmentsHelper.generateEnchantmentLevel(world.getRegistryKey(), random, enchantments);

        EnchantmentsHelper.applyEnchantment(stack, enchantments, level);
        return stack;
    }
}