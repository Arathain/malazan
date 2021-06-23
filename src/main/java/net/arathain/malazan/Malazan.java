package net.arathain.malazan;

import net.arathain.malazan.common.biome.MalazanBiomes;
import net.arathain.malazan.common.casting.MalazanSpells;
import net.arathain.malazan.common.item.MalazanThingamajigs;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class Malazan implements ModInitializer {
	public static final String MOD_ID = "malazan";
	public static final Identifier TELAS_KEYBIND_UNO = new Identifier(MOD_ID, "telas1");
	public static final String WARREN = "Warren";
	public static final String MALAZAN_DATA = "MalazData";
	@Override
	public void onInitialize() {
		MalazanThingamajigs.init();
		MalazanSpells.init();
		Registry.register(BuiltinRegistries.BIOME, MalazanBiomes.TELAS_KEY.getValue(), MalazanBiomes.TELAS);
	}

}
