package net.arathain.malazan.common.util;

import net.minecraft.client.particle.Particle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;

public class Warren {
    protected Identifier id;
    protected DefaultParticleType particles;
    protected int level;

    public Warren(Identifier id, DefaultParticleType particles){
        this.id = id;
        this.particles = particles;
    }

    public Identifier getId() {
        return id;
    }

    public DefaultParticleType getParticles() {
        return particles;
    }

    public void setLevel(int lvl) {
        level = lvl;
    }
    public int getLevel() {
        return level;
    }
}
