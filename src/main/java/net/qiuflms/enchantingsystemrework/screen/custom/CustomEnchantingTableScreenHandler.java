package net.qiuflms.enchantingsystemrework.screen.custom;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.block.ModBlocks;
import net.qiuflms.enchantingsystemrework.block.custom.CustomEnchantingTable;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.screen.ModScreenHandlers;
import net.qiuflms.enchantingsystemrework.util.EnchantmentEntry;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CustomEnchantingTableScreenHandler extends ScreenHandler {
    static final Identifier EMPTY_LAPIS_LAZULI_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/lapis_lazuli");
    static final Identifier EMPTY_SCROLL_SLOT = Identifier.of(EnchantingSystemRework.MOD_ID,"container/slot/icon_scroll");
    private final PlayerEntity player;

    private final ScreenHandlerContext context;
    private final Random random = Random.create();
    private final Property seed = Property.create();
    public final int[] enchantmentPower = new int[3];
    public final int[] enchantmentId = new int[]{-1, -1, -1};
    public final int[] enchantmentLevel = new int[]{-1, -1, -1};

    private final Inventory inventory = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            CustomEnchantingTableScreenHandler.this.onContentChanged(this);
        }
    };


    public CustomEnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public CustomEnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.CUSTOM_ENCHANTING_TABLE_SCREEN_HANDLER, syncId);
        this.context = context;
        this.player = playerInventory.player;

        this.addSlot(new Slot(this.inventory, 0, 11, 36) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.inventory, 1, 35, 47) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }

            @Override
            public Identifier getBackgroundSprite() {
                return CustomEnchantingTableScreenHandler.EMPTY_LAPIS_LAZULI_SLOT_TEXTURE;
            }
        });

        this.addSlot(new Slot(this.inventory, 2, 35, 23) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.ENCHANTED_SCROLL);
            }

            @Override
            public Identifier getBackgroundSprite() {
                return CustomEnchantingTableScreenHandler.EMPTY_SCROLL_SLOT;
            }
        });

        this.addPlayerSlots(playerInventory, 8, 84);
        this.addProperty(Property.create(this.enchantmentPower, 0));
        this.addProperty(Property.create(this.enchantmentPower, 1));
        this.addProperty(Property.create(this.enchantmentPower, 2));
        this.addProperty(this.seed).set(playerInventory.player.getEnchantingTableSeed());
        this.addProperty(Property.create(this.enchantmentId, 0));
        this.addProperty(Property.create(this.enchantmentId, 1));
        this.addProperty(Property.create(this.enchantmentId, 2));
        this.addProperty(Property.create(this.enchantmentLevel, 0));
        this.addProperty(Property.create(this.enchantmentLevel, 1));
        this.addProperty(Property.create(this.enchantmentLevel, 2));
    }

    private boolean canBeEnchanted(){
        ItemStack targetItem = this.inventory.getStack(0);
        ItemStack scrollStack = this.inventory.getStack(2);

        if (targetItem.isEmpty()) return false;
        if (targetItem.getItem() == Items.BOOK) return false;


        if (scrollStack.isEmpty() || !scrollStack.isOf(ModItems.ENCHANTED_SCROLL)) {
            return true;
        }

        var scrollEnchants = scrollStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
        if (scrollEnchants == null || scrollEnchants.isEmpty()) return true;

        for (var entry : scrollEnchants.getEnchantmentEntries()) {
            Enchantment enchantment = entry.getKey().value();

            return enchantment.isAcceptableItem(targetItem) || targetItem.isOf(Items.BOOK);
        }

        return true;
    }


    @Override
    public void onContentChanged(Inventory inventory) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {

                this.context.run((world, pos) -> {
                    IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();
                    int ix = 0;

                    for (BlockPos blockPos : CustomEnchantingTable.POWER_PROVIDER_OFFSETS) {
                        if (CustomEnchantingTable.canAccessPowerProvider(world, pos, blockPos)) {
                            ix++;
                        }
                    }

                    this.random.setSeed(this.seed.get());


                    int button = world.getRegistryKey() == World.OVERWORLD ? 0 : world.getRegistryKey() == World.NETHER ? 1 : 2;


                    for (int j = 0; j < 3; j++) {
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;

                        if(j == button){
                            this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, 2, ix, itemStack);
                        }else{
                            this.enchantmentPower[j] = 0;

                        }

                    }

                    if(!canBeEnchanted()) {
                        for (int j = 0; j < 3; j++) {
                            this.enchantmentPower[j] = 0;
                        }
                    }
                    for (int jx = 0; jx < 3; jx++) {
                        if (this.enchantmentPower[jx] > 0) {
                            List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, jx + 1, world.getRegistryKey(), this.player.getLuck());
                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry)list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[jx] = indexedIterable.getRawId(enchantmentLevelEntry.enchantment());
                                this.enchantmentLevel[jx] = enchantmentLevelEntry.level();
                            }
                        }
                    }

                    this.sendContentUpdates();
                });
            } else {
                for (int i = 0; i < 3; i++) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
    }


    private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, RegistryKey<World> world, double luck){
        int levelRequirement = this.enchantmentPower[slot - 1];

        if (slot == 3 && levelRequirement < 30) {
            this.enchantmentPower[2] = 0;
            return List.of();
        }


        if (slot == 2 && levelRequirement < 9) {
            this.enchantmentPower[1] = 0;
            return List.of();
        }

        this.random.setSeed(this.seed.get() + (long) slot);

        List<EnchantmentLevelEntry> enchantments = new ArrayList<>();

        List<EnchantmentEntry> generatedEnchantments = EnchantmentsHelper.generateMultipleEnchantmentsWeightedWithoutCurses(
                world,
                random,
                stack,
                List.of(1f, .5f, .25f),
                luck
        );

        if (generatedEnchantments.isEmpty()) {
            return List.of();
        }

        List<Integer> levels = EnchantmentsHelper.generateTableLevelEnchantments(
                world,
                random, generatedEnchantments, slot, luck);



        ItemStack scrollStack = this.inventory.getStack(2);
        if (!scrollStack.isEmpty() && scrollStack.isOf(ModItems.ENCHANTED_SCROLL)) {

            ItemEnchantmentsComponent scrollEnchants = scrollStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (scrollEnchants != null) {
                EnchantingSystemRework.LOGGER.info("asdasdasd");

                for (var entry : scrollEnchants.getEnchantmentEntries()) {
                    EnchantmentLevelEntry scrollEntry = new EnchantmentLevelEntry(entry.getKey(), entry.getIntValue());
                    if(!scrollEntry.enchantment().value().isAcceptableItem(this.inventory.getStack(0))){
                        return List.of();
                    }


                    boolean isInGenerated = false;
                    for (int i = 0; i < generatedEnchantments.size(); i++) {
                        if(generatedEnchantments.get(i).getEntry() == scrollEntry.enchantment()){
                            levels.set(i, scrollEntry.level());
                            isInGenerated = true;
                        }
                    }

                    if(!isInGenerated){
                        enchantments.add(scrollEntry);
                    }
                }
            }
        }

        for (int i = 0; i < generatedEnchantments.size(); i++) {
            enchantments.add(new EnchantmentLevelEntry(generatedEnchantments.get(i).getEntry(), levels.get(i)));
        }

        EnchantingSystemRework.LOGGER.info(String.valueOf(enchantments.size()));
        return enchantments;
    }

    public int getLapisCount() {
        ItemStack itemStack = this.inventory.getStack(1);
        return itemStack.isEmpty() ? 0 : itemStack.getCount();
    }

    public int getSeed() {
        return this.seed.get();
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.CUSTOM_ENCHANTING_TABLE);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < this.enchantmentPower.length) {
            ItemStack itemStack = this.inventory.getStack(0);
            ItemStack itemStack2 = this.inventory.getStack(1);
            ItemStack itemStack3 = this.inventory.getStack(2);

            int i = id + 1;
            if ((itemStack2.isEmpty() || itemStack2.getCount() < i) && !player.isInCreativeMode()) {
                return false;
            } else if (this.enchantmentPower[id] <= 0
                    || itemStack.isEmpty()
                    || (player.experienceLevel < i || player.experienceLevel < this.enchantmentPower[id]) && !player.isInCreativeMode()) {
                return false;
            } else {
                this.context.run((world, pos) -> {
                    ItemStack itemStack4 = itemStack;
                    List<EnchantmentLevelEntry> list = this.generateEnchantments(
                            itemStack,
                            i,
                            world.getRegistryKey(),
                            player.getLuck()
                    );

                    if (!list.isEmpty()) {
                        player.applyEnchantmentCosts(itemStack, i);
                        if (itemStack.isOf(Items.BOOK)) {
                            itemStack4 = itemStack.withItem(Items.ENCHANTED_BOOK);
                            this.inventory.setStack(0, itemStack4);
                        }

                        for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
                            itemStack4.addEnchantment(enchantmentLevelEntry.enchantment(), enchantmentLevelEntry.level());
                        }

                        itemStack2.decrementUnlessCreative(i, player);
                        if (itemStack2.isEmpty()) {
                            this.inventory.setStack(1, ItemStack.EMPTY);
                        }

                        itemStack3.decrementUnlessCreative(1, player);
                        if (itemStack3.isEmpty()) {
                            this.inventory.setStack(2, ItemStack.EMPTY);
                        }

                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack4, i);
                        }

                        this.inventory.markDirty();
                        this.seed.set(player.getEnchantingTableSeed());
                        this.onContentChanged(this.inventory);
                        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        } else {
            Util.logErrorOrPause(player.getStringifiedName() + " pressed invalid button id: " + id);
            return false;
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0 || slot == 1 || slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                    if (!this.insertItem(itemStack2, 1, 2, true)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (itemStack2.isOf(ModItems.ENCHANTED_SCROLL)) {
                    if (!this.insertItem(itemStack2, 2, 3, true)) {
                        return ItemStack.EMPTY;
                    }
                }

                else {
                    if (this.slots.get(0).hasStack() || !this.slots.get(0).canInsert(itemStack2)) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack oneItem = itemStack2.copyWithCount(1);
                    itemStack2.decrement(1);
                    this.slots.get(0).setStack(oneItem);
                }
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

}
