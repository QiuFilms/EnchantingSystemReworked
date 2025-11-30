package net.qiuflms.enchantingsystemrework.mixin;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.loot.entry.ItemEntry.class)
public interface ItemEntryAccessor {
    @Accessor("item")
    RegistryEntry<Item> getItem();
}
