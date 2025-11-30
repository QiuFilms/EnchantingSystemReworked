package net.qiuflms.enchantingsystemrework.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.qiuflms.enchantingsystemrework.EnchantingSystemRework;
import net.qiuflms.enchantingsystemrework.screen.custom.CustomEnchantingTableScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<CustomEnchantingTableScreenHandler> CUSTOM_ENCHANTING_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(EnchantingSystemRework.MOD_ID, "custom_enchanting_table_screen_handler"),
                    new ScreenHandlerType<>(CustomEnchantingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));


    public static void registerScreenHandlers() {
        EnchantingSystemRework.LOGGER.info("Registering Screen Handlers ");
    }
}
