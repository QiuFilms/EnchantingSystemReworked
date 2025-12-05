package net.qiuflms.enchantingsystemrework.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilRecipeMixin extends ForgingScreenHandler {
    @Shadow private int repairItemUsage;
    @Shadow private Property levelCost;
    @Shadow private String newItemName;

    public AnvilRecipeMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, null);
    }

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void decorheaven$addCustomAnvilRecipes(CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0);
        ItemStack rightStack = this.input.getStack(1);

        if (leftStack.isOf(ModItems.ENCHANTED_SCROLL) && rightStack.isOf(ModItems.ENCHANTED_SCROLL)) {
            RegistryEntry<Enchantment> left = leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                    .getEnchantmentEntries().iterator().next().getKey();
            RegistryEntry<Enchantment> right = rightStack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                    .getEnchantmentEntries().iterator().next().getKey();

            if(left == right && leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(left) == rightStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(right)){


                ItemStack outputStack = new ItemStack(ModItems.ENCHANTED_SCROLL);

                if (this.newItemName != null && !this.newItemName.isBlank()) {
                    if (!this.newItemName.equals(leftStack.getName().getString())) {
                        outputStack.set(DataComponentTypes.CUSTOM_NAME, net.minecraft.text.Text.literal(this.newItemName));
                        this.levelCost.set(this.levelCost.get() + 1);
                    }
                }

                int level = leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(left) + 1;
                int maxLevel = left.value().getMaxLevel();



                if(!(leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(left) == maxLevel)){
                    ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
                    builder.add(left, Math.min(level, maxLevel));
                    outputStack.set(DataComponentTypes.STORED_ENCHANTMENTS, builder.build());
                    this.output.setStack(2, outputStack);

                    this.levelCost.set(Math.min(level, maxLevel)*3);

                    this.sendContentUpdates();
                    ci.cancel();
                }

            }
        }

    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
    private void decorheaven$takeItems(PlayerEntity player, ItemStack outputStack, CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0);
        ItemStack rightStack = this.input.getStack(1);
        if (leftStack.isOf(ModItems.ENCHANTED_SCROLL) && rightStack.isOf(ModItems.ENCHANTED_SCROLL)) {
            RegistryEntry<Enchantment> left = leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                    .getEnchantmentEntries().iterator().next().getKey();
            RegistryEntry<Enchantment> right = rightStack.get(DataComponentTypes.STORED_ENCHANTMENTS)
                    .getEnchantmentEntries().iterator().next().getKey();

            if(left == right && leftStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(left) == rightStack.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(right)){
                leftStack.decrement(1);
                rightStack.decrement(1);

                this.context.run((world, pos) -> {
                    world.syncWorldEvent(1044, pos, 0);
                });

                ci.cancel();
            }


        }
    }
}