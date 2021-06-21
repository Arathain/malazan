package net.arathain.malazan.mixin;

import net.arathain.malazan.common.util.Warren;
import net.arathain.malazan.common.util.Warrens;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements Talent {
    public int telas = 0;

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.getInt("telas");
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("telas", getTelas());
    }

    @Override
    public int getTelas() {
        return telas;
    }

    @Override
    public void setTelas(int level) {
        telas = level;
    }

    @Override
    public Warren getWarren() {
        if (telas > 0) {
            return Warrens.TELAS;
        }
        return null;
    }

    @Override
    public void setWarren(Warren warren) {
        if (warren == Warrens.TELAS) {
            setTelas(1);
        }
    }

}
