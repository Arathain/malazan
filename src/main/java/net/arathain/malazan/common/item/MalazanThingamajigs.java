package net.arathain.malazan.common.item;

import net.arathain.malazan.Malazan;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class MalazanThingamajigs {

    public static void init() {
        Item TELAS_TABLET = registerItem(new TelasTablet( new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON).group(ItemGroup.MISC)), "telas_tablet");
    }
    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Malazan.MOD_ID + ":" + name, item);
        return item;
    }

}
