package net.arathain.malazan.common.block;

import net.arathain.malazan.Malazan;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class MalazanThingamajigs {


    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Malazan.MOD_ID + ":" + name, item);
        return item;
    }

}
