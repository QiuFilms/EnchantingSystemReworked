package net.qiuflms.enchantingsystemrework.trade_offers;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;
import org.jetbrains.annotations.Nullable;


import java.util.List;

public class EnchantableItemWrapperFactory implements TradeOffers.Factory {
    private final TradeOffers.Factory originalFactory;

    public EnchantableItemWrapperFactory(TradeOffers.Factory original) {
        this.originalFactory = original;
    }

    @Override
    public @Nullable TradeOffer create(Entity entity, Random random) {
        TradeOffer originalOffer = this.originalFactory.create(entity, random);

        if (originalOffer == null) {
            return null;
        }

        ItemStack item = new ItemStack(originalOffer.getSellItem().getItem());

        List<EnchantmentEntry> enchantments = EnchantmentsHelper.removeCurses(EnchantmentsHelper.generateMultipleTradeableEnchantment(World.OVERWORLD, random, item,
                List.of(1f, .4f, .15f)));
        List<Integer> level = EnchantmentsHelper.generateEnchantmentLevel(World.OVERWORLD, random, enchantments);

        EnchantmentsHelper.applyEnchantment(item, enchantments, level);

       return new TradeOffer(
                originalOffer.getFirstBuyItem(),
                originalOffer.getSecondBuyItem(),
                item,
                originalOffer.getMaxUses(),
                originalOffer.getMerchantExperience(),
                originalOffer.getPriceMultiplier()
        );
    }
}
