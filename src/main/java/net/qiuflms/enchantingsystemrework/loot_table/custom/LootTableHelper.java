package net.qiuflms.enchantingsystemrework.loot_table.custom;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.loot_function.custom.ItemEnchant;
import net.qiuflms.enchantingsystemrework.loot_function.custom.MendingEnchantment;
import net.qiuflms.enchantingsystemrework.loot_function.custom.SimpleScrollEnchant;
import net.qiuflms.enchantingsystemrework.mixin.*;
import net.qiuflms.enchantingsystemrework.potions.ModPotions;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LootTableHelper {
    private static final List<RegistryKey<Enchantment>> STRUCTURE_ENCHANTS = List.of(
            Enchantments.SOUL_SPEED,
            Enchantments.SWIFT_SNEAK,
            Enchantments.WIND_BURST
    );

    private static LootPool.Builder luckPotionPool = LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .conditionally(RandomChanceLootCondition.builder(0.15f))
            .with(ItemEntry.builder(Items.POTION)
                    .weight(3)
                    .apply(SetPotionLootFunction.builder(ModPotions.LUCK_3))
            )
            .with(ItemEntry.builder(Items.POTION)
                    .weight(2)
                    .apply(SetPotionLootFunction.builder(ModPotions.LUCK_4))
            )
            .with(ItemEntry.builder(Items.POTION)
                    .weight(1)
                    .apply(SetPotionLootFunction.builder(ModPotions.LUCK_5))
            );


    public static void registerLootTable(){
        LootTableEvents.REPLACE.register((key, original, source, registries) -> {
            if(key.equals(LootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE_CHEST)) return original;

            addPool(original, luckPotionPool.build());
            List<LootPool> originalPools = ((LootTableAccessor) original).getPools();

            for (LootPool pool : originalPools) {
                for (int i = 0; i < pool.entries.toArray().length; i++) {
                    if (pool.entries.get(i) instanceof ItemEntry) {
                        Item item = ((ItemEntryAccessor) pool.entries.get(i)).getItem().value();


                        if(isEnchantable(((LeafEntryAccessor) pool.entries.get(i)).getFunctions())){
                            if(item == Items.BOOK){
                                List<LootPoolEntry> entriesList = new ArrayList<>(pool.entries);

                                if(((LeafEntryAccessor) pool.entries.get(i)).getWeight() < 5){
                                    entriesList.remove(i);
                                }else{
                                    LootPoolEntry newBookEntry = createBookEntry(pool, i);
                                    entriesList.set(i, newBookEntry);
                                }

                                LootPoolEntry newScrollEntry = createScrollEntry(pool, i);
                                entriesList.add(newScrollEntry);

                                ((LootPoolAccessor) pool).setEntries(entriesList);
                                continue;
                            }

                            if(item.getDefaultStack().isEnchantable()){
                                List<LootPoolEntry> entriesList = new ArrayList<>(pool.entries);

                                LootPoolEntry newItem = createEnchantableItemEntry(pool, i, item);
                                entriesList.set(i, newItem);

                                ((LootPoolAccessor) pool).setEntries(entriesList);
                            }
                        }

                    }
                }
            }

            if(LootTables.END_CITY_TREASURE_CHEST.equals(key)){
                LootPool.Builder newPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.ENCHANTED_SCROLL)
                                .weight(5)
                                .apply(MendingEnchantment::new))
                        .with(ItemEntry.builder(Items.BOOK)
                                .weight(1)
                                .apply(MendingEnchantment::new))
                        .conditionally(RandomChanceLootCondition.builder(0.20f));

                addPool(original, newPool.build());
            }

            return original;
        });
    }



    private static void addPool(LootTable table, LootPool pool) {
        LootTableAccessor accessor = (LootTableAccessor) table;
        List<LootPool> mutablePools = new ArrayList<>(accessor.getPools());

        mutablePools.add(pool);

        accessor.setPools(mutablePools);
    }

    private static LootPoolEntry createScrollEntry(LootPool pool, int index){
        return ItemEntry.builder(ModItems.ENCHANTED_SCROLL)
                .weight(((LeafEntryAccessor) pool.entries.get(index)).getWeight())
                .apply(SimpleScrollEnchant::new).build();
    }

    private static LootPoolEntry createBookEntry(LootPool pool, int index){
        return ItemEntry.builder(Items.BOOK)
                .weight(((LeafEntryAccessor) pool.entries.get(index)).getWeight() % 5)
                .apply(SimpleScrollEnchant::new).build();
    }

    private static LootPoolEntry createEnchantableItemEntry(LootPool pool, int index, Item item){
        return ItemEntry.builder(item)
                .weight(((LeafEntryAccessor) pool.entries.get(index)).getWeight())
                .apply(ItemEnchant::new).build();
    }

    private static boolean isEnchantable(List<LootFunction> functions){
        for (LootFunction func : functions) {
            if (func instanceof EnchantRandomlyLootFunction) {
                return !isStructureBound((EnchantRandomlyLootFunction) func);
            }

            if (func instanceof net.minecraft.loot.function.EnchantWithLevelsLootFunction) {
                return true;
            }
        }
        return false;
    }

    private static boolean isStructureBound(EnchantRandomlyLootFunction func) {
        Optional<RegistryEntryList<Enchantment>> options = ((EnchantRandomlyAccessor) func).getOptions();

        if (options.isEmpty()) {
            return true;
        }

        RegistryEntryList<Enchantment> allowedEnchants = options.get();

        for (RegistryEntry<Enchantment> entry : allowedEnchants) {
            for (RegistryKey<Enchantment> forbiddenKey : STRUCTURE_ENCHANTS) {
                if (entry.matchesKey(forbiddenKey)) {
                    return true;
                }
            }
        }

        return false;
    }
}
