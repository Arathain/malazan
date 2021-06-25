package net.arathain.malazan.common.util;

import com.google.common.primitives.Doubles;
import net.arathain.malazan.Malazan;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Predicate;

public class MalazanUtil {
    public static EntityHitResult hitscanEntity(World world, LivingEntity user, double distance, Predicate<Entity> targetPredicate){
        Vec3d vec3d = user.getCameraPosVec(1);
        Vec3d vec3d2 = user.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 16, vec3d2.y * 16, vec3d2.z * 16);
        double squareDistance = Math.pow(distance, 2);
        return ProjectileUtil.getEntityCollision(world, user, vec3d, vec3d3, user.getBoundingBox().stretch(vec3d2.multiply(squareDistance)).expand(1.0D, 1.0D, 1.0D), targetPredicate);
    }
    public static BlockHitResult hitscanBlock(World world, LivingEntity user, double distance, RaycastContext.FluidHandling fluidHandling, Predicate<Block> targetPredicate){
        Vec3d vec3d = user.getCameraPosVec(1);
        Vec3d vec3d2 = user.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 16, vec3d2.y * 16, vec3d2.z * 16);
        double squareDistance = Math.pow(distance, 2);
        return world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, fluidHandling, user));
    }
    public static void enterDim(Entity entity){
        //Note, the player does not hold the previous dimension oddly enough.
        Vec3d destinationPosition;

        if (entity instanceof ServerPlayerEntity) {
            MinecraftServer minecraftServer = entity.getServer(); // the server itself
            assert minecraftServer != null;
            ServerWorld telas = ((MinecraftServer) minecraftServer).getWorld(Malazan.TELAS_WORLD_KEY);
            RegistryKey<World> world_key = entity.world.getRegistryKey();

            // Prevent crash due to mojang bug that makes mod's json dimensions not exist upload first creation of world on server. A restart fixes this.
            if(telas == null){
                return;
            }

            ServerWorld serverWorld = minecraftServer.getWorld(world_key);
            if(serverWorld == null){
                serverWorld = minecraftServer.getWorld(World.OVERWORLD);
            }
            destinationPosition = teleport((PlayerEntity) entity, serverWorld, true);
            ((ServerPlayerEntity)entity).teleport(
                    telas,
                    destinationPosition.x,
                    destinationPosition.y,
                    destinationPosition.z,
                    entity.getYaw(),
                    entity.getPitch()
            );
        }
    }


    public static void exitDim(Entity entity, ServerWorld destination){
        boolean upwardChecking = entity.getY() > 0;
        Vec3d destinationPosition;

        if (entity instanceof ServerPlayerEntity) {
            destinationPosition = teleport((PlayerEntity) entity, destination, upwardChecking);

            ((ServerPlayerEntity)entity).teleport(
                    destination,
                    destinationPosition.x,
                    destinationPosition.y,
                    destinationPosition.z,
                    entity.getYaw(),
                    entity.getPitch()
            );
        }
    }
    private static Vec3d teleport(PlayerEntity playerEntity, ServerWorld destinationWorld, boolean checkingUpward) {
        double coordinateScale = playerEntity.getEntityWorld().getDimension().getCoordinateScale() / destinationWorld.getDimension().getCoordinateScale();
        BlockPos validBlockPos = null;

        Vec3d playerPos = playerEntity.getPos();
        if(playerPos != null){
            validBlockPos = new BlockPos(
                    Doubles.constrainToRange(playerEntity.getPos().getX() * coordinateScale, -29999936D, 29999936D),
                    playerEntity.getPos().getY()+30,
                    Doubles.constrainToRange(playerEntity.getPos().getZ() * coordinateScale, -29999936D, 29999936D));

        }


        assert validBlockPos != null;
        if (destinationWorld.getBlockState(validBlockPos.up()).isOpaque()) {
            destinationWorld.setBlockState(validBlockPos, Blocks.AIR.getDefaultState(), 3);
            destinationWorld.setBlockState(validBlockPos.up(), Blocks.AIR.getDefaultState(), 3);
        }
        return new Vec3d(
                validBlockPos.getX() + 0.5D,
                validBlockPos.getY() + 1,
                validBlockPos.getZ() + 0.5D
        );
    }
}
