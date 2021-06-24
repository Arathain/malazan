package net.arathain.malazan.common.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class FlareEntity extends HostileEntity {
    public FlareEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void initGoals() {
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));


        this.targetSelector.add(2, new FollowTargetGoal(this, LivingEntity.class, true));
    }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20).add(EntityAttributes.GENERIC_ARMOR, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 350.0);
    }
    @Override
    public void tick() {
        super.tick();
        if (!world.isClient()) {
            for (int i = 0; i < (16); i++) {
                this.world.addParticle(ParticleTypes.FLAME,
                        this.getParticleX(1),
                        this.getPos().y,
                        this.getParticleZ(1),
                        random.nextGaussian() / 16, random.nextGaussian() / 4, random.nextGaussian() / 16);
            }
            BlockPos funnypos = this.getBlockPos();
            this.setOnFireFor(10);
            this.clearStatusEffects();
            if (this.getBlockStateAtPos() == Blocks.AIR.getDefaultState()) {
                world.setBlockState(funnypos, Blocks.FIRE.getDefaultState());
            }
            Vec3d pos = this.getPos();
            List<LivingEntity> entities = this.getEntityWorld().getEntitiesByClass(
                    LivingEntity.class,
                    new Box(
                            pos.getX() - 4, pos.getY() - 4, pos.getZ() - 4,
                            pos.getX() + 4, pos.getY() + 6, pos.getZ() + 4
                    ), (LivingEntity) -> true
            );
            for (LivingEntity nearbyEntity : entities) {
                if (!(nearbyEntity instanceof FlareEntity)) {
                    this.explode();
                }
            }

        }
    }
    public void explode() {
        world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 3.0f, true, Explosion.DestructionType.DESTROY);
        Vec3d pos = this.getPos();
        List<LivingEntity> entities = this.getEntityWorld().getEntitiesByClass(
                LivingEntity.class,
                new Box(
                        pos.getX() - 6, pos.getY() - 6, pos.getZ() - 6,
                        pos.getX() + 6, pos.getY() + 8, pos.getZ() + 6
                ), (LivingEntity) -> true
        );
        world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 1, 0);
        for (LivingEntity nearbyEntity : entities) {

            nearbyEntity.clearStatusEffects();
            nearbyEntity.setOnFireFor(10);
            BlockPos entitypos = nearbyEntity.getBlockPos();
            if (nearbyEntity.getBlockStateAtPos() == Blocks.AIR.getDefaultState()) {
                world.setBlockState(entitypos, Blocks.FIRE.getDefaultState());
            }

        }
        this.remove(RemovalReason.KILLED);
    }
}
