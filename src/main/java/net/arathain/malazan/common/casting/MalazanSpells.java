package net.arathain.malazan.common.casting;

import net.arathain.malazan.Malazan;
import net.arathain.malazan.common.util.MalazanUtil;
import net.arathain.malazan.common.util.interfaces.Talent;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

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
                if(world.isChunkLoaded(pos) && !user.isSneaking() && ((Talent) user).getTelas() > 0){
                    BlockHitResult hitResult = MalazanUtil.hitscanBlock(world, user, 30, RaycastContext.FluidHandling.NONE, (target) -> !target.equals(Blocks.AIR));
                    EntityHitResult hit = MalazanUtil.hitscanEntity(world, user, 30, (target) -> target instanceof LivingEntity && !target.isSpectator() && user.canSee(target));
                    if (hit !=null) {
                        world.setBlockState(hit.getEntity().getBlockPos(), Blocks.FIRE.getDefaultState());
                        (hit).getEntity().setOnFireFor(20);
                    }
                    if (hit == null) {
                        hitResult.getPos();
                        BlazeEntity cursedshit = new BlazeEntity(EntityType.BLAZE, world);
                        cursedshit.setPos((float)hitResult.getPos().x, (float)hitResult.getPos().y, (float)hitResult.getPos().z);
                        world.spawnEntity(cursedshit);
                        BlockPos blockPos = cursedshit.getBlockPos();
                        cursedshit.remove(Entity.RemovalReason.DISCARDED);
                        BlockState blockState = world.getBlockState(blockPos);
                        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
                            BlockPos blockPos2 = blockPos;
                            if (AbstractFireBlock.canPlaceAt(world, blockPos2, user == null ? Direction.NORTH : user.getHorizontalFacing())) {
                                world.playSound(user, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                                world.setBlockState(blockPos2, blockState2, 11);
                                world.emitGameEvent(user, GameEvent.BLOCK_PLACE, blockPos);
                                ItemStack itemStack = user.getStackInHand(user.getActiveHand());
                                if (user instanceof ServerPlayerEntity) {
                                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos2, itemStack);
                                }

                            }
                        } else {
                            world.playSound(user, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                            world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true), 11);
                            world.emitGameEvent(user, GameEvent.BLOCK_PLACE, blockPos);

                            }
                        }

                    }
                });

            });
        //TODO: add greater Telas and retreat to Telas
    }
    }

