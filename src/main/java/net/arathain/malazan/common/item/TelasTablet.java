package net.arathain.malazan.common.item;

import net.arathain.malazan.common.util.interfaces.Talent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;


public class TelasTablet extends Item {
    public TelasTablet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if ((world.getBlockState(user.getBlockPos()) == Blocks.FIRE.getDefaultState() || world.getBlockState(user.getBlockPos()) == Blocks.CAMPFIRE.getDefaultState()) && user.isOnFire() && (user.getStatusEffect(StatusEffects.FIRE_RESISTANCE) == null) && user.getArmor() < 4) {
            if (world.isClient()) {
                for (int i = 0; i < (2); i++) {
                    world.addParticle(ParticleTypes.FLAME,
                            user.getX() + user.getRandom().nextGaussian() * 0.2,
                            user.getY() + user.getRandom().nextGaussian() * 0.5 + 1,
                            user.getZ() + user.getRandom().nextGaussian() * 0.2,
                            0, 0.5, 0);
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            if (!world.isClient()) {
                this.getDefaultStack().decrement(1);
                ((Talent) user).setTelas(1);
                return TypedActionResult.success(user.getStackInHand(hand));
            }


        }
        if ((world.getBlockState(user.getBlockPos().offset(Direction.DOWN)) == Blocks.NETHERITE_BLOCK.getDefaultState() || world.getBlockState(user.getBlockPos().offset(Direction.DOWN)) == Blocks.ANCIENT_DEBRIS.getDefaultState()) && user.isOnFire() && (user.getStatusEffect(StatusEffects.FIRE_RESISTANCE) == null) && user.getArmor() < 4 && ((Talent) user).getTelas() == 1) {

            if (world.isClient()) {
                for (int i = 0; i < (16); i++) {
                    world.addParticle(ParticleTypes.FLAME,
                            user.getX() + user.getRandom().nextGaussian() * 0.2,
                            user.getY() + user.getRandom().nextGaussian() * 0.5 + 1,
                            user.getZ() + user.getRandom().nextGaussian() * 0.2,
                            0, 0.5, 0);
                }
            }
            if (!world.isClient()) {
                this.getDefaultStack().decrement(1);
                ((Talent) user).setTelas(2);
            }
            return TypedActionResult.success(user.getStackInHand(hand));

        }
        else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        super.onItemEntityDestroyed(entity);
        if (entity.wasOnFire) {
            entity.getEntityWorld().createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 3.0f, true, Explosion.DestructionType.NONE);
        }

    }
}
