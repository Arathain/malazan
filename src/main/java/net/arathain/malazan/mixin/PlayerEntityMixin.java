package net.arathain.malazan.mixin;

import io.netty.buffer.Unpooled;
import net.arathain.malazan.Malazan;
import net.arathain.malazan.MalazanClient;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (world.isClient()) {
            // We are in the client now, can't do anything server-sided

            // See the keybindings tutorial for information about this if statement (the keybindings tutorial calls this variable "keyBinding")
            if(MalazanClient.spellbind1.isPressed()){
                // Pass the `BlockPos` information
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(this.getBlockPos());
                // Send packet to server to change the block for us
                ClientSidePacketRegistry.INSTANCE.sendToServer(Malazan.TELAS_KEYBIND_UNO, passedData);
            }

        }
    }
}
