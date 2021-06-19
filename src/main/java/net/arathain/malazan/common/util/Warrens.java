package net.arathain.malazan.common.util;

import net.arathain.malazan.Malazan;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Warrens {
    public static final Warren TELAS = new Warren(new Identifier(Malazan.MOD_ID, "telas"), ParticleTypes.FLAME);

    public static void init() {
        register(TELAS);
    }

    private static void register(Warren warren) {
        Registry.register(MalazanRegistries.WARRENS, warren.getId(), warren);
    }
}
