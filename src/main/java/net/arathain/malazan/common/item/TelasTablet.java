package net.arathain.malazan.common.item;

import net.arathain.malazan.common.util.interfaces.Talent;
import net.arathain.malazan.mixin.ServerPlayerEntityMixin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class TelasTablet extends Item {
    public TelasTablet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && (world.getBlockState(user.getBlockPos()) == Blocks.FIRE.getDefaultState()) && user.isOnFire()) {
            for (int i = 0; i < (2); i++) {
            world.addParticle(ParticleTypes.FLAME,
                    user.getX() + user.getRandom().nextGaussian() * 0.2,
                    user.getY() + user.getRandom().nextGaussian() * 0.5 + 1,
                    user.getZ() + user.getRandom().nextGaussian() * 0.2,
                    0, 0.5, 0);
            }
            this.getDefaultStack().decrement(1);
            ((Talent) user).setTelas(1);
            return TypedActionResult.success(user.getStackInHand(hand));

        }
        else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }
}
