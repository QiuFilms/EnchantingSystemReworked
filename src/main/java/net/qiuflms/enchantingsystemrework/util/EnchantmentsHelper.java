package net.qiuflms.enchantingsystemrework.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EnchantmentsHelper {
    private static MinecraftServer server;
    private static List<EnchantmentEntry> ENCHANTMENTS;

    public static void onServerStarted(MinecraftServer server) {
        EnchantmentsHelper.server = server;

        ENCHANTMENTS = List.of(
                createPairLockedToWorld(Enchantments.DEPTH_STRIDER, World.OVERWORLD, Rarity.UNCOMMON),
                createPairLockedToWorld(Enchantments.AQUA_AFFINITY, World.OVERWORLD, Rarity.UNCOMMON),
                createPairLockedToWorld(Enchantments.FROST_WALKER, World.OVERWORLD, Rarity.UNCOMMON),
                createPairLockedToWorld(Enchantments.RESPIRATION, World.OVERWORLD, Rarity.UNCOMMON),
//                createPairLockedToWorld(Enchantments.SWIFT_SNEAK, World.OVERWORLD),
//                createPairLockedToWorld(Enchantments.WIND_BURST, World.OVERWORLD),
                createPairLockedToWorld(Enchantments.CHANNELING, World.OVERWORLD, Rarity.RARE),
                createPairLockedToWorld(Enchantments.IMPALING, World.OVERWORLD, Rarity.RARE),
                createPairLockedToWorld(Enchantments.LOYALTY, World.OVERWORLD, Rarity.RARE),
                createPairLockedToWorld(Enchantments.RIPTIDE, World.OVERWORLD, Rarity.RARE),
                createPairLockedToWorld(Enchantments.LUCK_OF_THE_SEA, World.OVERWORLD, Rarity.RARE),
                createPairLockedToWorld(Enchantments.LURE, World.OVERWORLD, Rarity.RARE),


//                createPairLockedToWorld(Enchantments.SOUL_SPEED, World.NETHER),
                createPairLockedToWorld(Enchantments.FIRE_ASPECT, World.NETHER, Rarity.UNCOMMON),
                createPairLockedToWorld(Enchantments.FLAME, World.NETHER, Rarity.UNCOMMON),

                createPairLockedToWorld(Enchantments.MENDING, World.END, Rarity.RARE),

                createPair(Enchantments.VANISHING_CURSE, Rarity.RARE),
                createPair(Enchantments.BINDING_CURSE, Rarity.RARE),

                createPair(Enchantments.UNBREAKING, Rarity.UNCOMMON),
                createPair(Enchantments.BLAST_PROTECTION, Rarity.COMMON),
                createPair(Enchantments.FEATHER_FALLING, Rarity.COMMON),
                createPair(Enchantments.PROJECTILE_PROTECTION, Rarity.COMMON),
                createPair(Enchantments.FIRE_PROTECTION, Rarity.COMMON),
                createPair(Enchantments.PROTECTION, Rarity.COMMON),
                createPair(Enchantments.THORNS, Rarity.COMMON),
                createPair(Enchantments.BANE_OF_ARTHROPODS, Rarity.COMMON),
                createPair(Enchantments.BREACH, Rarity.UNCOMMON),
                createPair(Enchantments.DENSITY, Rarity.UNCOMMON),
                createPair(Enchantments.EFFICIENCY, Rarity.UNCOMMON),
                createPair(Enchantments.LOOTING, Rarity.UNIQUE),
                createPair(Enchantments.KNOCKBACK, Rarity.COMMON),
                createPair(Enchantments.SHARPNESS, Rarity.COMMON),
                createPair(Enchantments.SMITE, Rarity.COMMON),
                createPair(Enchantments.SWEEPING_EDGE, Rarity.COMMON),
                createPair(Enchantments.INFINITY, Rarity.UNCOMMON),
                createPair(Enchantments.MULTISHOT, Rarity.COMMON),
                createPair(Enchantments.PIERCING, Rarity.UNCOMMON),
                createPair(Enchantments.POWER, Rarity.UNCOMMON),
                createPair(Enchantments.PUNCH, Rarity.UNCOMMON),
                createPair(Enchantments.QUICK_CHARGE, Rarity.UNCOMMON),
                createPair(Enchantments.FORTUNE, Rarity.UNIQUE),
                createPair(Enchantments.SILK_TOUCH, Rarity.RARE)
        );
    }


    public static MinecraftServer getWorld(){
        return server;
    }

    private static EnchantmentLevel calculateBasicEnchantmentLevels(RegistryKey<Enchantment> enchantmentRegistryKey){
        Enchantment enchantment = EnchantmentEntry.getEntry(enchantmentRegistryKey).value();

        if(enchantment.getMaxLevel() == 1){
            return new EnchantmentLevel(1,1,1);
        }

        if(enchantment.getMaxLevel() == 2){
            return new EnchantmentLevel(1,1,2);
        }
        return new EnchantmentLevel(enchantment.getMaxLevel() - 2,enchantment.getMaxLevel() - 1, enchantment.getMaxLevel());
    }


    private static EnchantmentEntry createPair(RegistryKey<Enchantment> enchantmentRegistryKey, Rarity rarity){
        return new EnchantmentEntry(enchantmentRegistryKey, calculateBasicEnchantmentLevels(enchantmentRegistryKey), rarity);
    }


    private static EnchantmentEntry createPairLockedToWorld(RegistryKey<Enchantment> enchantmentRegistryKey, RegistryKey<World> world, Rarity rarity){
        if(world == World.OVERWORLD){
            return new EnchantmentEntry(enchantmentRegistryKey,
                    EnchantmentEntry.getEntry(enchantmentRegistryKey).value().getMaxLevel(), 0, 0,
                    rarity);
        }

        if(world == World.NETHER){
            return new EnchantmentEntry(enchantmentRegistryKey,
                    0, EnchantmentEntry.getEntry(enchantmentRegistryKey).value().getMaxLevel(), 0,
                    rarity);
        }

        return new EnchantmentEntry(enchantmentRegistryKey,
                0, 0, EnchantmentEntry.getEntry(enchantmentRegistryKey).value().getMaxLevel(),
                rarity);

    }


    private static List<EnchantmentEntry> acceptableEnchantments(ItemStack item){
        List<EnchantmentEntry> filteredList = new ArrayList<>();

        for(EnchantmentEntry enchantment:ENCHANTMENTS){
            if(enchantment.getEnchantment().isAcceptableItem(item)){
                filteredList.add(enchantment);
            }
        }
        return filteredList;
    }

    public static EnchantmentEntry generateSingleTradeableEnchantment(RegistryKey<World> world, Random random){
        List<EnchantmentEntry> generatedEnchantments = getWorldEnchantments(world);
        return generateSingleTradeableEnchantment(random, generatedEnchantments);
    }


    private static EnchantmentEntry generateSingleTradeableEnchantment(Random random, List<EnchantmentEntry> enchantments){
        return generateSingleEnchantment(random, filterTradeableEnchantments(enchantments));
    }

    public static List<EnchantmentEntry> generateMultipleTradeableEnchantment(RegistryKey<World> world, Random random, ItemStack item, List<Float> chances){
        List<EnchantmentEntry> generatedEnchantments = getWorldEnchantments(world, item);
        return generateMultipleTradeableEnchantment(random, filterTradeableEnchantments(generatedEnchantments), chances);
    }

    private static List<EnchantmentEntry> generateMultipleTradeableEnchantment(Random random, List<EnchantmentEntry> enchantments, List<Float> chances){
        return generateMultipleEnchantments(random, enchantments, chances);
    }

    public static List<EnchantmentEntry> filterTradeableEnchantments(List<EnchantmentEntry> enchantments){
        enchantments.removeIf(enchantment -> !enchantment.getEntry().isIn(EnchantmentTags.TRADEABLE));

        return enchantments;
    }

    public static List<EnchantmentEntry> filterTableEnchantments(List<EnchantmentEntry> enchantments){
        enchantments.removeIf(enchantment -> !enchantment.getEntry().isIn(EnchantmentTags.IN_ENCHANTING_TABLE));

        return enchantments;
    }



    public static List<EnchantmentEntry> getWorldEnchantments(RegistryKey<World> world){
        List<EnchantmentEntry> list = new ArrayList<>();

        for(EnchantmentEntry enchantment:ENCHANTMENTS){
            if(enchantment.getLevel().getDimensionLevel(world) != 0){
                list.add(enchantment);
            }
        }
        return list;
    }

    public static List<EnchantmentEntry> getWorldEnchantments(RegistryKey<World> world, ItemStack item){
        List<EnchantmentEntry> list = new ArrayList<>();

        for(EnchantmentEntry enchantment:acceptableEnchantments(item)){
            if(enchantment.getLevel().getDimensionLevel(world) != 0){
                list.add(enchantment);
            }
        }
        return list;
    }


    public static void applyEnchantmentComponent(ItemStack item, EnchantmentEntry enchantment, int level){
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        builder.add(enchantment.getEntry(), level);
        item.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
    }


    public static void applyEnchantment(ItemStack item, EnchantmentEntry enchantment, int level){
        item.addEnchantment(enchantment.getEntry(), level);
    }

    public static void applyEnchantment(ItemStack item, List<EnchantmentEntry> enchantments, List<Integer> levels){
        for (int i = 0; i < enchantments.size(); i++) {
            applyEnchantment(item, enchantments.get(i), levels.get(i));
        }
    }


    private static List<EnchantmentEntry> generateMultipleEnchantments(Random random, List<EnchantmentEntry> enchantments, List<Float> chances){
        List<EnchantmentEntry> result = new ArrayList<>();

        for(float chance:chances){
            if(random.nextFloat() < chance){
                for (int i = 0; i < enchantments.size(); i++) {
                    if(canBeCombined(result, enchantments.get(i))){
                        result.add(enchantments.get(i));
                        break;
                    }else{
                        enchantments.remove(i);
                        i--;
                    }
                }
            }
        }
        return result;
    }


    public static List<EnchantmentEntry> generateMultipleEnchantmentsWeighted(RegistryKey<World> world, Random random, ItemStack item, List<Float> chances, double luck){
        List<EnchantmentEntry> generatedEnchantments = getWorldEnchantments(world, item);
        return generateMultipleEnchantmentsWeighted(random, generatedEnchantments, chances, luck);
    }

    public static List<EnchantmentEntry> generateMultipleEnchantmentsWeightedWithoutCurses(RegistryKey<World> world, Random random, ItemStack item, List<Float> chances, double luck){
        List<EnchantmentEntry> generatedEnchantments = removeCurses(getWorldEnchantments(world, item));
        return generateMultipleEnchantmentsWeighted(random, generatedEnchantments, chances, luck);
    }

    private static List<EnchantmentEntry> generateMultipleEnchantmentsWeighted(Random random, List<EnchantmentEntry> enchantments, List<Float> chances, double luck){
        List<EnchantmentEntry> result = new ArrayList<>();

        for(float chance:chances){
            if(random.nextFloat() < chance+(luck*.15f)){
                int value = random.nextInt(calculateWeights(enchantments, luck));
                for (int i = 0; i < enchantments.size(); i++) {
                    value -= enchantments.get(i).getRarity().value + enchantments.get(i).getRarity().bonus(luck);
                    if(value <= 0){
                        result.add(enchantments.get(i));
                        removeIncompatible(enchantments, enchantments.get(i));
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static void removeIncompatible(List<EnchantmentEntry> enchantments, EnchantmentEntry enchantment){
        enchantments.removeIf(entry-> !Enchantment.canBeCombined(entry.getEntry(), enchantment.getEntry()));
    }



    private static EnchantmentEntry generateSingleEnchantment(Random random, List<EnchantmentEntry> enchantments){
        return enchantments.get(random.nextInt(enchantments.size()));
    }

    public static EnchantmentEntry generateSingleEnchantmentWeighted(RegistryKey<World> world, Random random, double luck){
        List<EnchantmentEntry> enchantments = getWorldEnchantments(world);
        return generateSingleEnchantmentWeighted(random, enchantments, luck);
    }

    private static EnchantmentEntry generateSingleEnchantmentWeighted(Random random, List<EnchantmentEntry> enchantments, double luck){
        int value = random.nextInt(calculateWeights(enchantments, luck));

        for(EnchantmentEntry enchantment:enchantments){
            value -= enchantment.getRarity().value + enchantment.getRarity().bonus(luck);
            if(value <= 0){
                return enchantment;
            }
        }
        return enchantments.get(random.nextInt(enchantments.size()));
    }


    public static int generateEnchantmentLevel(RegistryKey<World> world, Random random, EnchantmentEntry enchantment){
        return random.nextBetween(enchantment.getLevel().getMinLevel(world), enchantment.getLevel().getDimensionLevel(world));
    }

    public static List<Integer> generateEnchantmentLevel(RegistryKey<World> world, Random random, List<EnchantmentEntry> enchantments){
        List<Integer> result = new ArrayList<>();
        for(EnchantmentEntry enchantment:enchantments){
            result.add(generateEnchantmentLevel(world, random, enchantment));
        }
        return result;
    }

    private static List<Integer> splitIntoThree(int maxLevel, int buttonLevel) {
        List<Integer> source = IntStream.range(1, maxLevel+1).boxed().toList();

        int size = source.size();
        int split1 = (int) Math.round(size / 3.0);
        int split2 = (int) Math.round(2 * size / 3.0);


        if(source.size() == 1){
            return List.of(1);
        }


        if(buttonLevel == 1){
            if(source.size() == 2){
                return List.of(1);
            }
            return new ArrayList<>(source.subList(0, split1));
        }

        if(buttonLevel == 2){
            if(source.size() == 2){
                return List.of(1,2);
            }
            return new ArrayList<>(source.subList(split1, split2));
        }

        if(buttonLevel == 3){
            if(source.size() == 2){
                return List.of(2);
            }
            List<Integer> result = new ArrayList<>(source.subList(split1, split2));
            result.addAll(source.subList(split2, size));
            return result;
        }

        return new ArrayList<>();
    }


    private static int generateEnchantmentLevel(RegistryKey<World> world, Random random, EnchantmentEntry enchantment, int buttonLevel, double luck){
        List<Integer> levels = splitIntoThree(enchantment.getLevel().getDimensionLevel(world), buttonLevel);

        int totalWeight = 0;
        int size = levels.size();

        for (int i = 0; i < size; i++) {
            totalWeight += size * (size - i) - i + ((int) luck) * i;
        }

        int randomValue = random.nextInt(totalWeight) + 1;

        for (int i = 0; i < size; i++) {
            int weight = size * (size - i) - i + ((int) luck) * i;

            if(randomValue <= weight){
                return levels.get(i);
            }
            randomValue -= weight;
        }

        return 0;
    }


    public static List<Integer> generateTableLevelEnchantments(RegistryKey<World> world, Random random, List<EnchantmentEntry> enchantments, int buttonLevel, double luck){
        List<Integer> result = new ArrayList<>();

        for(EnchantmentEntry enchantment:enchantments){
            result.add(generateTableEnchantmentLevel(world, random, enchantment, luck));
        }

        return result;
    }


    public static int generateTableEnchantmentLevel(RegistryKey<World> world, Random random, EnchantmentEntry enchantment, double luck){
        int maxLevel = enchantment.getEnchantment().getMaxLevel();

        if(maxLevel == 1){
            return 1;
        }

        int dimensionModifier = World.OVERWORLD == world ? 0 : World.NETHER == world ? 1 : 2;

        int weights = 0;
        double step = 5f / (maxLevel - 1);

        for (int i = 0; i < maxLevel; i++) {
            if(luck > 2){
                weights += weightFunction(luck, step*i, dimensionModifier);
            }else{
                weights += weightFunction(luck, -5 + step*i, dimensionModifier);

            }
        }

        int ticket = random.nextInt(weights + 1);


        for (int i = 0; i < maxLevel; i++) {
            int value;

            if(luck > 2){
                value= weightFunction(luck, step*i, dimensionModifier);
            }else{
                value= weightFunction(luck, -5 + step*i, dimensionModifier);

            }

            if(ticket < value){
                return i+1;
            }
            ticket -= value;
        }
        return 1;
    }

    private static int weightFunction(double luck, double x, int dimensionModifier){
        luck = luck + dimensionModifier;

        return (int) Math.round((((luck - 3) * 1f/4) * x + 1));
    }


    private static boolean canBeCombined(List<EnchantmentEntry> enchantments, EnchantmentEntry enchantment){
        for(EnchantmentEntry listedEnchantments:enchantments){
            if(!Enchantment.canBeCombined(listedEnchantments.getEntry(), enchantment.getEntry())) return false;

        }
        return true;
    }

    private static int calculateWeights(List<EnchantmentEntry> enchantments, double luck){
        int weights = 0;
        for(EnchantmentEntry enchantment:enchantments){
            weights += enchantment.getRarity().value + enchantment.getRarity().bonus(luck);
        }

        return weights;
    }



    public static List<EnchantmentLevelEntry> generateCustomEnchantments(
            Random random,
            ItemStack stack,
            int buttonLevel,
            List<EnchantmentEntry> allowedEnchantments,
            RegistryKey<World> world
    ) {
        List<EnchantmentLevelEntry> result = new ArrayList<>();
        int enchantability = stack.getItem().getDefaultStack().get(DataComponentTypes.ENCHANTABLE).value();

        int modifiedLevel = calculateModifiedLevel(random, buttonLevel, enchantability);

        if (modifiedLevel <= 0) return result;


        List<EnchantmentLevelEntry> candidates = getCandidates(modifiedLevel, stack, allowedEnchantments, world);

        if (candidates.isEmpty()) {
            return result;
        }

        EnchantmentLevelEntry firstEnchant = pickWeighted(random, candidates);

        if (firstEnchant != null) {
            result.add(firstEnchant);
            removeIncompatible(candidates, firstEnchant);
        }

        while (random.nextInt(50) <= modifiedLevel) {
            if (candidates.isEmpty()) break;

            modifiedLevel /= 2;

            EnchantmentLevelEntry extraEnchant = pickWeighted(random, candidates);

            if (extraEnchant != null) {
                result.add(extraEnchant);
                removeIncompatible(candidates, extraEnchant);
            }
        }

        return result;
    }



    private static int calculateModifiedLevel(Random random, int base, int enchantability) {
        if (enchantability <= 0) return Math.max(1, base);

        int i = enchantability / 4 + 1;
        int j = enchantability / 4 + 1;
        int bonus = random.nextInt(i) + random.nextInt(j);

        int level = base + bonus + 1;

        // Waniliowy "Fuzz Factor" (wahanie +/- 15%)
        float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
        level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE);

        return level;
    }



    private static List<EnchantmentLevelEntry> getCandidates(int power, ItemStack stack, List<EnchantmentEntry> allowed, RegistryKey<World> world) {
        List<EnchantmentLevelEntry> candidates = new ArrayList<>();

        for (EnchantmentEntry entry : allowed) {
            Enchantment enchantment = entry.getEnchantment();

            // A. Czy pasuje do przedmiotu? (Lub czy to książka)
            if (!enchantment.isAcceptableItem(stack) && !stack.isOf(Items.BOOK)) {
                continue;
            }


            for (int i = entry.getLevel().getDimensionLevel(world); i >= 1; i--) {

                int minCost = enchantment.definition().minCost().forLevel(i);
                int maxCost = enchantment.definition().maxCost().forLevel(i);

                if (power >= minCost && power <= maxCost) {
                    candidates.add(new EnchantmentLevelEntry(entry.getEntry(), i));
                    break;
                }
            }
        }
        return candidates;
    }

    private static EnchantmentLevelEntry pickWeighted(Random random, List<EnchantmentLevelEntry> list) {
        if (list.isEmpty()) return null;

        int totalWeight = 0;
        for (EnchantmentLevelEntry entry : list) {
            totalWeight += entry.enchantment().value().getWeight();
        }

        if (totalWeight <= 0) return null;

        int ticket = random.nextInt(totalWeight);

        for (EnchantmentLevelEntry entry : list) {
            ticket -= entry.enchantment().value().getWeight();
            if (ticket < 0) {
                return entry;
            }
        }
        return list.getFirst();
    }

    private static void removeIncompatible(List<EnchantmentLevelEntry> candidates, EnchantmentLevelEntry chosen) {
        candidates.removeIf(entry -> {
            if (entry.enchantment().equals(chosen.enchantment())) return true;

            return !Enchantment.canBeCombined(entry.enchantment(), chosen.enchantment());
        });
    }

    public static List<EnchantmentEntry> removeCurses(List<EnchantmentEntry> list){
        list.removeIf(entry -> entry.getKey() == Enchantments.BINDING_CURSE || entry.getKey() == Enchantments.VANISHING_CURSE);
        return list;
    }


    public enum Rarity{
        COMMON(20),
        UNCOMMON(15),
        RARE(12),
        UNIQUE(8);

        public final int value;

        Rarity(int value) {
            this.value = value;
        }

        public int bonus(double luck){
            if(luck < 0) return value;
            if(this == UNIQUE || this == RARE) return (int) (((luck * value)/2));
            return value;
        }
    }

}
