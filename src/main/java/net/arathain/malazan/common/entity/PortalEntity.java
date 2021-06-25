package net.arathain.malazan.common.entity;

import net.arathain.malazan.Malazan;
import net.arathain.malazan.common.util.MalazanUtil;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PortalEntity extends HostileEntity {
    private UUID ownerUuid;
    private int ownerEntityId;
    public PortalEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    //haha this shouldn't exist
    @Override
    public void initGoals() {}
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1000).add(EntityAttributes.GENERIC_ARMOR, 6).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }
    @Override
    public void tick() {
        super.tick();
        if (!world.isClient()) {
            for (int i = 0; i < (96); i++) {
                    this.world.addParticle(ParticleTypes.FLAME,
                            this.getParticleX(1),
                            this.getPos().y,
                            this.getParticleZ(1),
                            random.nextGaussian() / 16, random.nextGaussian() / 4, random.nextGaussian() / 16);

            }
            BlockPos funnypos = this.getBlockPos();
            this.setOnFireFor(5);
            if (this.getBlockStateAtPos() == Blocks.AIR.getDefaultState()) {
                world.setBlockState(funnypos, Blocks.FIRE.getDefaultState());
            }
            Vec3d pos = this.getPos();
            List<LivingEntity> entities = this.getEntityWorld().getEntitiesByClass(
                    LivingEntity.class,
                    new Box(
                            pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5,
                            pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5
                    ), (LivingEntity) -> true
            );
            for (LivingEntity nearbyEntity : entities) {
                if (!(nearbyEntity instanceof FlareEntity)) {
                    if (nearbyEntity.getEntityWorld() == Objects.requireNonNull(world.getServer()).getWorld(Malazan.TELAS_WORLD_KEY)) {
                        MalazanUtil.exitDim(nearbyEntity, Objects.requireNonNull(world.getServer().getOverworld()));
                    } else {
                        MalazanUtil.exitDim(nearbyEntity, Objects.requireNonNull(world.getServer()).getWorld(Malazan.TELAS_WORLD_KEY));
                    }
                }
            }
            long time = world.getTime();
            if (time > 0 && time % 20 == 0 && world.getRandom().nextInt(3) == 0) {
                this.remove(RemovalReason.DISCARDED);
            }

        }
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.ownerEntityId = entity.getId();
        }

    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUuid != null && this.world instanceof ServerWorld) {
            return ((ServerWorld)this.world).getEntity(this.ownerUuid);
        } else {
            return this.ownerEntityId != 0 ? this.world.getEntityById(this.ownerEntityId) : null;
        }
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        if (this.ownerUuid != null) {
            tag.putUuid("Owner", this.ownerUuid);
        }

    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        if (tag.containsUuid("Owner")) {
            this.ownerUuid = tag.getUuid("Owner");
        }

    }
}
