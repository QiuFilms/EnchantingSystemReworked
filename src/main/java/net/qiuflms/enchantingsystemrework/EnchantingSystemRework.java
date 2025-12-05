package net.qiuflms.enchantingsystemrework;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.qiuflms.enchantingsystemrework.block.ModBlocks;
import net.qiuflms.enchantingsystemrework.block.entity.ModBlockEntities;
import net.qiuflms.enchantingsystemrework.item.ModItems;
import net.qiuflms.enchantingsystemrework.loot_table.custom.LootTableHelper;
import net.qiuflms.enchantingsystemrework.potions.ModPotions;
import net.qiuflms.enchantingsystemrework.recipe.ModRecipes;
import net.qiuflms.enchantingsystemrework.screen.ModScreenHandlers;
import net.qiuflms.enchantingsystemrework.util.EnchantmentsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantingSystemRework implements ModInitializer {
	public static final String MOD_ID = "enchantingsystemrework";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();


		ModRecipes.registerRecipes();

		ModPotions.registerPotions();
		ModRecipes.registerBrewingRecipes();

		LootTableHelper.registerLootTable();
		ServerLifecycleEvents.SERVER_STARTED.register(EnchantmentsHelper::onServerStarted);

	}
}