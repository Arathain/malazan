package net.arathain.malazan.common.item;

import net.arathain.malazan.common.util.interfaces.Talent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        if (!world.isClient()) {
            if ((world.getBlockState(user.getBlockPos()) == Blocks.FIRE.getDefaultState() || world.getBlockState(user.getBlockPos()) == Blocks.CAMPFIRE.getDefaultState()) && user.isOnFire() && user.getArmor() < 4 && ((Talent) user).getTelas() == 0) {

                    this.getDefaultStack().decrement(1);
                    ((Talent) user).setTelas(1);
                    return TypedActionResult.success(user.getStackInHand(hand));

            }
            if ((world.getBlockState(user.getBlockPos()) == Blocks.FIRE.getDefaultState() || world.getBlockState(user.getBlockPos()) == Blocks.CAMPFIRE.getDefaultState()) && (world.getBlockState(user.getBlockPos().offset(Direction.DOWN)) == Blocks.NETHERITE_BLOCK.getDefaultState() || world.getBlockState(user.getBlockPos().offset(Direction.DOWN)) == Blocks.ANCIENT_DEBRIS.getDefaultState()) && user.isOnFire() && user.getArmor() < 4 && ((Talent) user).getTelas() == 1 && user.getOffHandStack().getItem() == Items.BLAZE_ROD) {


                this.getDefaultStack().decrement(1);
                ((Talent) user).setTelas(2);
                user.clearStatusEffects();
            }
            return TypedActionResult.success(user.getStackInHand(hand));

        } else {
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
