package net.arathain.malazan.mixin;

import io.netty.buffer.Unpooled;
import net.arathain.malazan.Malazan;
import net.arathain.malazan.MalazanClient;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {

        if (world.isClient()) {
            MalazanClient.tickPlayer((PlayerEntity) (Object) this, this.random);
        } else {
            if (this.getEntityWorld().getServer() != null && this.getEntityWorld() == this.getEntityWorld().getServer().getWorld(Malazan.TELAS_WORLD_KEY)) {
                this.setOnFireFor(1);
            }
        }
    }
    @Inject(method = "Lnet/minecraft/entity/player/PlayerEntity;isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", at = @At("RETURN"), cancellable = true)
    public void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.isFire() && ((Talent) this).getTelas() > 2) {
            cir.setReturnValue(true);
        }
    }
}
