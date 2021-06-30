package net.arathain.malazan.common.item;

import net.arathain.malazan.Malazan;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class MalazanThingamajigs {

    public static void init() {
        Item TELAS_TABLET = registerItem(new TelasTablet( new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).group(ItemGroup.MISC)), "telas_tablet");
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter) -> {
            Identifier bastion_treasure = new Identifier(Malazan.MOD_ID, "inject/bastion_treasure");
            if (LootTables.BASTION_TREASURE_CHEST.equals(identifier)) {
                fabricLootSupplierBuilder.withPool(LootPool.builder().with(LootTableEntry.builder(bastion_treasure).weight(1)).build());
            }
        });
    }
    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Malazan.MOD_ID + ":" + name, item);
        return item;
    }


}
