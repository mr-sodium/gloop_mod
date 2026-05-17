package com.nacl.gloop.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;

public class MagicMirrorItemClass extends Item {

    public MagicMirrorItemClass(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {



                if (itemstack.getDamageValue() != 3) {

                    ResourceKey<Level> respawnDim = serverPlayer.getRespawnDimension();
                    BlockPos spawnPos = serverPlayer.getRespawnPosition();
                    ServerLevel targetLevel = serverPlayer.getServer().getLevel(respawnDim);


                    if (spawnPos == null || targetLevel == null) {
                        targetLevel = serverPlayer.getServer().getLevel(Level.OVERWORLD);
                        if (targetLevel != null) {
                            spawnPos = targetLevel.getSharedSpawnPos();
                        }
                    }


                    if (targetLevel != null && spawnPos != null) {

                        serverPlayer.fallDistance = 0;

                        serverPlayer.teleportTo(
                                targetLevel,
                                spawnPos.getX() + 0.5,
                                spawnPos.getY() + 0.7,
                                spawnPos.getZ() + 0.5,
                                serverPlayer.getYRot(),
                                serverPlayer.getXRot()
                        );


                        if (!player.hasInfiniteMaterials()) {


                            EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                            itemstack.hurtAndBreak(1, serverPlayer, slot);

                            player.getCooldowns().addCooldown(this, 6); //600 past testing (spud)
                        }


                        targetLevel.playSound(null, spawnPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}