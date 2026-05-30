package com.nacl.gloop.Item;

import com.nacl.gloop.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class MagicMirrorItem extends Item {

    public MagicMirrorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        int NHT = Config.MagicMirrorTicks.get();
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {

                if (itemstack.getDamageValue() != 3) {

                    ResourceKey<Level> respawnDim = serverPlayer.getRespawnDimension();
                    BlockPos bedPos = serverPlayer.getRespawnPosition();
                    float respawnAngle = serverPlayer.getRespawnAngle();
                    boolean forcedSpawn = serverPlayer.isRespawnForced();

                    ServerLevel targetLevel = serverPlayer.getServer().getLevel(respawnDim);

                    if (targetLevel == null) {
                        targetLevel = serverPlayer.getServer().getLevel(Level.OVERWORLD);
                    }

                    if (targetLevel != null) {
                        Vec3 actualSpawnPos = null;

                        // 1. Check for valid bed/anchor placement and obstructions using stable 1.21.1 block-checking logic
                        if (bedPos != null) {
                            // Get the state of the block at the player's saved spawn point
                            net.minecraft.world.level.block.state.BlockState blockState = targetLevel.getBlockState(bedPos);
                            net.minecraft.world.level.block.Block block = blockState.getBlock();

                            // If it's a bed, use the bed's native obstruction/standing space check
                            if (block instanceof net.minecraft.world.level.block.BedBlock) {
                                Optional<Vec3> optionalSpawn = net.minecraft.world.level.block.BedBlock.findStandUpPosition(
                                        net.minecraft.world.entity.EntityType.PLAYER, targetLevel, bedPos, blockState.getValue(net.minecraft.world.level.block.BedBlock.FACING), respawnAngle
                                );
                                if (optionalSpawn.isPresent()) {
                                    actualSpawnPos = optionalSpawn.get();
                                }
                            }
                            // If it's a respawn anchor, handle its native logic
                            else if (block instanceof net.minecraft.world.level.block.RespawnAnchorBlock) {
                                Optional<Vec3> optionalSpawn = net.minecraft.world.level.block.RespawnAnchorBlock.findStandUpPosition(
                                        net.minecraft.world.entity.EntityType.PLAYER, targetLevel, bedPos
                                );
                                if (optionalSpawn.isPresent()) {
                                    actualSpawnPos = optionalSpawn.get();
                                }
                            }
                        }
                        // 2. Fallback: If no bed exists, or it is completely obstructed
                        if (actualSpawnPos == null) {
                            BlockPos sharedSpawn = targetLevel.getSharedSpawnPos();
                            actualSpawnPos = new Vec3(sharedSpawn.getX() + 0.5, sharedSpawn.getY() + 0.1, sharedSpawn.getZ() + 0.5);
                        }

                        // 3. Teleport the player safely
                        serverPlayer.fallDistance = 0;
                        serverPlayer.teleportTo(
                                targetLevel,
                                actualSpawnPos.x,
                                actualSpawnPos.y,
                                actualSpawnPos.z,
                                serverPlayer.getYRot(),
                                serverPlayer.getXRot()
                        );

                        if (!player.hasInfiniteMaterials()) {
                            EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                            itemstack.hurtAndBreak(1, serverPlayer, slot);

                            player.getCooldowns().addCooldown(this, NHT);
                        }

                        BlockPos soundPos = BlockPos.containing(actualSpawnPos);
                        targetLevel.playSound(null, soundPos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.PLAYERS, 1.0F, 1.3F);
                        targetLevel.playSound(null, soundPos, SoundEvents.BONE_BLOCK_BREAK, SoundSource.PLAYERS, 3.0F, 2.0F);
                        targetLevel.playSound(null, soundPos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0F, 0.2F);
                    }
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (player.hurtTime > 0 && player.getLastDamageSource() != null) {
                if (player.getLastDamageSource().getEntity() != null) {
                    if (!player.getCooldowns().isOnCooldown(this)) {
                        int HTS = Config.MagicMirrorHitTicks.get();
                        player.getCooldowns().addCooldown(this, HTS);
                    }
                }
            }
        }
    }
}