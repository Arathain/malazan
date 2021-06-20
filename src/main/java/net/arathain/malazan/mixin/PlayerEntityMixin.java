package net.arathain.malazan.mixin;

import net.arathain.malazan.common.util.Warren;
import net.arathain.malazan.common.util.Warrens;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Talent {
    private static final TrackedData<Boolean> TELAS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    Warren warrenType;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(TELAS, false);
    }

    @Override
    public Warren getWarren() {
        return warrenType;
    }

    @Override
    public void setWarren(Warren warren) {
        warrenType = warren;
        if (warrenType == Warrens.TELAS) {
            dataTracker.set(TELAS, true);
        }
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (this.dataTracker.get(TELAS)) {
            for (int i = 0; i < (2); i++) {
                world.addParticle(warrenType.getParticles(),
                        getX(),
                        getY(),
                        getZ(),
                        random.nextGaussian() / 10, random.nextGaussian() / 5, random.nextGaussian() / 10);
            }
        }
    }


}
