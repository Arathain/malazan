package net.arathain.malazan.common.casting;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntList;
import net.arathain.malazan.Malazan;
import net.arathain.malazan.common.entity.PortalEntity;
import net.arathain.malazan.common.util.MalazanUtil;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.stream.Stream;

public class MalazanSpells {
    public static void init() {
        //LESSER TELAS
        ServerSidePacketRegistry.INSTANCE.register(Malazan.TELAS_KEYBIND_UNO, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier in the IO thread
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread
                PlayerEntity user = packetContext.getPlayer();
                World world = user.world;
                // ALWAYS validate that the information received is valid in a C2S packet!
                if(world.isChunkLoaded(pos) && ((Talent) user).getTelas() > 0) {
                    BlockHitResult hitResult = MalazanUtil.hitscanBlock(world, user, 30, RaycastContext.FluidHandling.NONE, (target) -> !target.equals(Blocks.AIR));
                    EntityHitResult hit = MalazanUtil.hitscanEntity(world, user, 30, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
                    if (user.isSneaking()) {
                        if (hit != null) {
                            user.addExhaustion(8);
                            PortalEntity portal = new PortalEntity(Malazan.PORTAL, world);
                            portal.setPos((float) hit.getPos().x, (float) hit.getPos().y, (float) hit.getPos().z);
                            portal.setOwner(user);
                            world.spawnEntity(portal);
                        }
                        if (hit == null) {
                            user.addExhaustion(8);
                            PortalEntity portal = new PortalEntity(Malazan.PORTAL, world);
                            portal.setPos((float) hitResult.getPos().x, (float) hitResult.getPos().y, (float) hitResult.getPos().z);
                            portal.setOwner(user);
                            world.spawnEntity(portal);
                        }
                    }
                    else {
                        if (hit != null) {
                            (hit).getEntity().setOnFireFor(20);
                            if (!(((Talent) user).getTelas() > 1)) {
                                user.addExhaustion(1);
                            }
                        }
                        if (hit == null) {
                            hitResult.getPos();
                            BlazeEntity cursedshit = new BlazeEntity(EntityType.BLAZE, world);
                            cursedshit.setPos((float) hitResult.getPos().x, (float) hitResult.getPos().y, (float) hitResult.getPos().z);
                            world.spawnEntity(cursedshit);
                            BlockPos blockPos = cursedshit.getBlockPos();
                            cursedshit.remove(Entity.RemovalReason.DISCARDED);
                            BlockState blockState = world.getBlockState(blockPos);
                            if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
                                if (AbstractFireBlock.canPlaceAt(world, blockPos, user == null ? Direction.NORTH : user.getHorizontalFacing())) {
                                    world.playSound(user, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                                    BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
                                    world.setBlockState(blockPos, blockState2, 11);
                                    world.emitGameEvent(user, GameEvent.BLOCK_PLACE, blockPos);
                                    ItemStack itemStack = user.getStackInHand(user.getActiveHand());
                                    if (user instanceof ServerPlayerEntity) {
                                        Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, blockPos, itemStack);
                                    }

                                }
                            } else {
                                world.playSound(user, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                                world.setBlockState(blockPos, (BlockState) blockState.with(Properties.LIT, true), 11);
                                world.emitGameEvent(user, GameEvent.BLOCK_PLACE, blockPos);

                            }
                            if (!(((Talent) user).getTelas() > 1)) {
                                user.addExhaustion(1);
                            }
                        }

                    }
                }
                });

            });
        //TODO: add greater Telas and retreat to Telas
        ServerSidePacketRegistry.INSTANCE.register(Malazan.TELAS_KEYBIND_DOS, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier in the IO thread
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread
                PlayerEntity user = packetContext.getPlayer();
                World world = user.world;
                // ALWAYS validate that the information received is valid in a C2S packet!
                if(world.isChunkLoaded(pos) && ((Talent) user).getTelas() > 1) {
                    BlockHitResult hitResult = MalazanUtil.hitscanBlock(world, user, 30, RaycastContext.FluidHandling.NONE, (target) -> !target.equals(Blocks.AIR));
                    EntityHitResult hit = MalazanUtil.hitscanEntity(world, user, 30, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
                    if (user.isSneaking()) {
                        user.addExhaustion(2);
                        
                            FireballEntity fireball = new FireballEntity(world, user, user.getRotationVector().x + user.getRandom().nextGaussian() / 16, user.getRotationVector().y + user.getRandom().nextGaussian() / 16, user.getRotationVector().z + user.getRandom().nextGaussian() / 16, 4);
                            fireball.setOwner(user);
                        if (hit != null) {
                            fireball.setPos(hit.getPos().x + user.getRandom().nextGaussian(), hit.getPos().y + user.getRandom().nextGaussian() / 8, hit.getPos().z + user.getRandom().nextGaussian());
                        } else {
                            fireball.setPos(hitResult.getPos().x + user.getRandom().nextGaussian(), hitResult.getPos().y + user.getRandom().nextGaussian() / 8, hitResult.getPos().z + user.getRandom().nextGaussian());
                        }
                            world.spawnEntity(fireball);
                        
                        if (user.getHungerManager().getFoodLevel() < 8) {
                            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 10));
                            user.disableShield(false);
                            user.setHealth(4);
                        }
                        }
                    if (hit != null) {
                        Vec3d burnmpos = hit.getPos();
                        if (!world.isClient()) {
                            List<LivingEntity> entities = user.getEntityWorld().getEntitiesByClass(
                                    LivingEntity.class,
                                    new Box(
                                            burnmpos.getX() - 20, burnmpos.getY() - 20, burnmpos.getZ() - 20,
                                            burnmpos.getX() + 20, burnmpos.getY() + 20, burnmpos.getZ() + 20
                                    ), (LivingEntity) -> true
                            );

                            for (LivingEntity nearbyEntity : entities) {
                                nearbyEntity.setOnFireFor(2);
                                if ((world.getBlockState(nearbyEntity.getBlockPos().offset(Direction.DOWN)) == Blocks.GRASS_BLOCK.getDefaultState())) {
                                    world.setBlockState(nearbyEntity.getBlockPos().offset(Direction.DOWN), Blocks.COARSE_DIRT.getDefaultState());
                                }
                            }
                            List<PersistentProjectileEntity> arrows = user.getEntityWorld().getEntitiesByClass(
                                    PersistentProjectileEntity.class,
                                    new Box(
                                            burnmpos.getX() - 5, burnmpos.getY() - 5, burnmpos.getZ() - 5,
                                            burnmpos.getX() + 5, burnmpos.getY() + 5, burnmpos.getZ() + 5
                                    ), (PersistentProjectileEntity) -> true
                            );

                            for (PersistentProjectileEntity nearbyArrow : arrows) {

                                nearbyArrow.remove(Entity.RemovalReason.KILLED);
                            }
                        }
                        if (!(((Talent) user).getTelas() > 1)) {
                            user.addExhaustion(1);
                        }
                    }
                    if (hit == null) {
                        Vec3d burnmpos = hitResult.getPos();
                        if (!world.isClient()) {
                            List<LivingEntity> entities = user.getEntityWorld().getEntitiesByClass(
                                    LivingEntity.class,
                                    new Box(
                                            burnmpos.getX() - 20, burnmpos.getY() - 20, burnmpos.getZ() - 20,
                                            burnmpos.getX() + 20, burnmpos.getY() + 20, burnmpos.getZ() + 20
                                    ), (LivingEntity) -> true
                            );

                            for (LivingEntity nearbyEntity : entities) {
                                nearbyEntity.setOnFireFor(2);
                                if ((world.getBlockState(nearbyEntity.getBlockPos().offset(Direction.DOWN)) == Blocks.GRASS_BLOCK.getDefaultState())) {
                                    world.setBlockState(nearbyEntity.getBlockPos().offset(Direction.DOWN), Blocks.COARSE_DIRT.getDefaultState());
                                }
                            }
                            List<PersistentProjectileEntity> arrows = user.getEntityWorld().getEntitiesByClass(
                                    PersistentProjectileEntity.class,
                                    new Box(
                                            pos.getX() - 5, pos.getY() - 5, burnmpos.getZ() - 5,
                                            pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5
                                    ), (PersistentProjectileEntity) -> true
                            );

                            for (PersistentProjectileEntity nearbyArrow : arrows) {

                                nearbyArrow.remove(Entity.RemovalReason.KILLED);
                            }
                        }
                        if (!(((Talent) user).getTelas() > 1)) {
                            user.addExhaustion(1);
                        }
                    }
                    user.setOnFireFor(2);
                    Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);

                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    float x = (float) user.getPos().getX();
                    float y = (float) user.getPos().getY();
                    float z = (float) user.getPos().getZ();
                    NbtCompound targetPos = new NbtCompound();
                    targetPos.putFloat("X", x);
                    targetPos.putFloat("Y", y);
                    targetPos.putFloat("Z", z);
                    passedData.writeNbt(targetPos);

                    watchingPlayers.forEach(player ->
                            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,Malazan.TELAS_PARTICLE_ID,passedData));





                }
            });

        });

    }

    }

