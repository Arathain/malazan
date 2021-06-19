package net.arathain.malazan.common.util;

import net.arathain.malazan.Malazan;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MalazanRegistries {

    public static final Registry<Warren> WARRENS = FabricRegistryBuilder.createSimple(Warren.class, new Identifier(Malazan.MOD_ID, "warrens")).buildAndRegister();

}
