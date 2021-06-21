package net.arathain.malazan;

import net.arathain.malazan.common.casting.MalazanSpells;
import net.arathain.malazan.common.item.MalazanThingamajigs;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Malazan implements ModInitializer {
	public static final String MOD_ID = "malazan";
	public static final Identifier TELAS_KEYBIND_UNO = new Identifier(MOD_ID, "telas1");
	public static final String WARREN = "Warren";
	public static final String MALAZAN_DATA = "MalazData";
	@Override
	public void onInitialize() {
		MalazanThingamajigs.init();
		MalazanSpells.init();
	}

}
