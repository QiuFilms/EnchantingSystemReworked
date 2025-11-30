package net.qiuflms.enchantingsystemrework.trade_offers;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ScrollWrapperFactory implements TradeOffers.Factory {
    private final TradeOffers.Factory originalFactory;

    public ScrollWrapperFactory(TradeOffers.Factory original) {
        this.originalFactory = original;
    }

    private ItemStack generateScroll(Random random){
        ItemStack scrollStack = new ItemStack(ModItems.ENCHANTED_SCROLL);
        EnchantmentEntry enchantment = EnchantmentsHelper.generateSingleTradeableEnchantment(World.OVERWORLD, random);
        int level = EnchantmentsHelper.generateEnchantmentLevel(World.OVERWORLD, random, enchantment);

        EnchantmentsHelper.applyEnchantmentComponent(scrollStack, enchantment, level);

        return scrollStack;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {

        // 1. Wywołaj oryginalną fabrykę, aby stworzyła ofertę z książką
        TradeOffer originalOffer = this.originalFactory.create(entity, random);

        if (originalOffer == null) {
            return null; // Zabezpieczenie
        }

        // 2. Pobierz stos zaklętej książki z tej oferty
        ItemStack bookStack = originalOffer.getSellItem();

        // 3. Sprawdź, czy to na pewno książka (powinna być)
        if (!bookStack.isOf(Items.ENCHANTED_BOOK)) {
            return originalOffer; // Jeśli nie, na wszelki wypadek zwróć oryginał
        }


        ItemStack scrollStack = generateScroll(random);
        scrollStack.setCount(bookStack.getCount());

        return new TradeOffer(
                originalOffer.getFirstBuyItem(),
                Optional.of(new TradedItem(Items.PAPER, 5)),
                scrollStack,
                originalOffer.getMaxUses(),
                originalOffer.getMerchantExperience(),
                originalOffer.getPriceMultiplier()
        );

    }
}