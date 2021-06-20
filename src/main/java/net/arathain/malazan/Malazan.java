package net.arathain.malazan;

import net.arathain.malazan.common.item.MalazanThingamajigs;
import net.fabricmc.api.ModInitializer;

public class Malazan implements ModInitializer {
	public static final String MOD_ID = "malazan";
	public static final String WARREN = "Warren";
	public static final String MALAZAN_DATA = "MalazData";
	@Override
	public void onInitialize() {
		MalazanThingamajigs.init();
	}
}
