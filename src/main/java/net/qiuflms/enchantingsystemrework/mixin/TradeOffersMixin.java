package net.qiuflms.enchantingsystemrework.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.qiuflms.enchantingsystemrework.trade_offers.EnchantableItemWrapperFactory;
import net.qiuflms.enchantingsystemrework.trade_offers.ScrollWrapperFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TradeOffers.class)
public class TradeOffersMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void decorheaven$modifyLibrarian(CallbackInfo ci) {
        Int2ObjectMap<TradeOffers.Factory[]> librarianTrades =
                TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.LIBRARIAN);

        if (librarianTrades != null) {
            for (int level = 1; level <= 5; level++) {
                TradeOffers.Factory[] currentLevelTrades = librarianTrades.get(level);
                if (currentLevelTrades == null) continue;

                ArrayList<TradeOffers.Factory> modifiedList = new ArrayList<>(currentLevelTrades.length);

                for (TradeOffers.Factory factory : currentLevelTrades) {
                    if (factory instanceof TradeOffers.EnchantBookFactory) {
                        modifiedList.add(new ScrollWrapperFactory(factory));
                    } else {
                        modifiedList.add(factory);
                    }
                }

                librarianTrades.put(level, modifiedList.toArray(new TradeOffers.Factory[0]));
            }
        }
    }

    private static List<RegistryKey<VillagerProfession>> professionsToModify = List.of(
            VillagerProfession.TOOLSMITH,
            VillagerProfession.ARMORER,
            VillagerProfession.WEAPONSMITH
    );


    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void decorheaven$modifyEnchantantableItemsTrades(CallbackInfo ci) {
        for(RegistryKey<VillagerProfession> profession:professionsToModify){
            modifyProfessionsWithEnchantableItems(profession);
        }
    }

    private static void modifyProfessionsWithEnchantableItems(RegistryKey<VillagerProfession> profession){
        Int2ObjectMap<TradeOffers.Factory[]> villagerTrades =
                TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(profession);

        if (villagerTrades != null) {
            for (int level = 1; level <= 5; level++) {
                TradeOffers.Factory[] currentLevelTrades = villagerTrades.get(level);
                if (currentLevelTrades == null) continue;

                ArrayList<TradeOffers.Factory> modifiedList = new ArrayList<>(currentLevelTrades.length);

                for (TradeOffers.Factory factory : currentLevelTrades) {
                    if (factory instanceof TradeOffers.SellEnchantedToolFactory) {
                        modifiedList.add(new EnchantableItemWrapperFactory(factory));
                    } else {
                        modifiedList.add(factory);
                    }
                }

                villagerTrades.put(level, modifiedList.toArray(new TradeOffers.Factory[0]));
            }
        }
    }
}
