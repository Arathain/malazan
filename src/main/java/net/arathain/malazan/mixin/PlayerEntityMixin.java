package net.arathain.malazan.mixin;

import io.netty.buffer.Unpooled;
import net.arathain.malazan.Malazan;
import net.arathain.malazan.MalazanClient;
import net.arathain.malazan.common.util.MalazanUtil;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {

        if (world.isClient()) {
            if(MalazanClient.spellbind1.isPressed()){
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(this.getBlockPos());

                if (this.random.nextBoolean()) {
                    ClientSidePacketRegistry.INSTANCE.sendToServer(Malazan.TELAS_KEYBIND_UNO, passedData);
                }
            }
            if(MalazanClient.spellbind2.isPressed()){
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(this.getBlockPos());

                if (this.random.nextBoolean()) {
                    ClientSidePacketRegistry.INSTANCE.sendToServer(Malazan.TELAS_KEYBIND_DOS, passedData);
                }
            }


        } else {
            if (this.getEntityWorld().getServer() != null && this.getEntityWorld() == this.getEntityWorld().getServer().getWorld(Malazan.TELAS_WORLD_KEY)) {
                this.setOnFireFor(1);
            }
        }
    }
}
