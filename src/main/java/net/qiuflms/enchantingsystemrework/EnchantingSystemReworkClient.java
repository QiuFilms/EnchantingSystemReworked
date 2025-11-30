package net.qiuflms.enchantingsystemrework;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.qiuflms.enchantingsystemrework.block.entity.ModBlockEntities;
import net.qiuflms.enchantingsystemrework.block.entity.renderer.CustomEnchantingTableBlockEntityRenderer;
import net.qiuflms.enchantingsystemrework.screen.ModScreenHandlers;
import net.qiuflms.enchantingsystemrework.screen.custom.CustomEnchantingTableScreen;

public class EnchantingSystemReworkClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CUSTOM_ENCHANTING_TABLE_SCREEN_HANDLER, CustomEnchantingTableScreen::new);
        BlockEntityRendererFactories.register(
                ModBlockEntities.CUSTOM_ENCHANTING_TABLE,
                CustomEnchantingTableBlockEntityRenderer::new
        );
    }
}
