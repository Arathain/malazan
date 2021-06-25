package net.arathain.malazan;

import net.arathain.malazan.common.biome.MalazanBiomes;
import net.arathain.malazan.common.casting.MalazanSpells;
import net.arathain.malazan.common.entity.FlareEntity;
import net.arathain.malazan.common.entity.MalazanEntities;
import net.arathain.malazan.common.entity.PortalEntity;
import net.arathain.malazan.common.item.MalazanThingamajigs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Malazan implements ModInitializer {
	public static final String MOD_ID = "malazan";
	public static final Identifier TELAS_KEYBIND_UNO = new Identifier(MOD_ID, "telas1");
	public static final String WARREN = "Warren";
	public static final String MALAZAN_DATA = "MalazData";
	public static final EntityType<FlareEntity> FLARE = createEntity("flare", FlareEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FlareEntity::new).dimensions(EntityDimensions.fixed(0.6f, 1.9f)).fireImmune().build());
	public static final EntityType<PortalEntity> PORTAL = createEntity("portal", FlareEntity.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, PortalEntity::new).dimensions(EntityDimensions.fixed(0.6f, 1.9f)).fireImmune().build());
	public static final Identifier TELAS_DIMENSION_ID = new Identifier(MOD_ID, "telas");
	public static final RegistryKey<World> TELAS_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, TELAS_DIMENSION_ID);

	@Override
	public void onInitialize() {
		MalazanThingamajigs.init();
		MalazanSpells.init();
		Registry.register(BuiltinRegistries.BIOME, MalazanBiomes.TELAS_KEY.getValue(), MalazanBiomes.TELAS);
		MalazanEntities.init();
	}

	private static <T extends LivingEntity> EntityType<T> createEntity(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
		FabricDefaultAttributeRegistry.register(type, attributes);

		return type;
	}

}
