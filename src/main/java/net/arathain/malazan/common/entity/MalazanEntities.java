package net.arathain.malazan.common.entity;

import net.arathain.malazan.Malazan;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MalazanEntities {
    public static void init() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Malazan.MOD_ID, "flare"), Malazan.FLARE);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Malazan.MOD_ID, "portal"), Malazan.PORTAL);
    }
}
