package net.qiuflms.enchantingsystemrework.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public interface LootTableAccessor {
    @Accessor("pools")
    @Mutable
    List<LootPool> getPools();

    @Mutable
    @Accessor("pools")
    void setPools(List<LootPool> pools);
}