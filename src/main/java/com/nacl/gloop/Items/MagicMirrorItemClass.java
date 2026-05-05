package com.nacl.gloop.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
<<<<<<< Updated upstream
=======
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
>>>>>>> Stashed changes
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class MagicMirrorItemClass extends Item {

    public MagicMirrorItemClass(Properties properties) {
        super(properties);
    }

    @Override
<<<<<<< Updated upstream
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        double x = 0.7;
=======
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (player.hurtTime > 0 && player.getLastDamageSource() != null) {
                if (player.getLastDamageSource().getEntity() != null) {
                    if (!player.getCooldowns().isOnCooldown(this)) {
                        player.getCooldowns().addCooldown(this, 400);
                    }
                }
            }
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

>>>>>>> Stashed changes
        if (!level.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {

                // Get respawn dimension + position
                ResourceKey<Level> respawnDim = serverPlayer.getRespawnDimension();
                BlockPos spawnPos = serverPlayer.getRespawnPosition();
                player.getCooldowns().addCooldown(this, 20);
                ServerLevel targetLevel = serverPlayer.getServer().getLevel(respawnDim);
                if (spawnPos == null) {
                    respawnDim = Level.OVERWORLD;
                    spawnPos = targetLevel.getSharedSpawnPos();
                }

                if (targetLevel != null ) {
//                    level.getBlockState(spawnPos).getBlock()
                    serverPlayer.teleportTo(
                            targetLevel,
                            spawnPos.getX() + 0.5,
                            spawnPos.getY() + x,
                            spawnPos.getZ() + 0.5,
                            serverPlayer.getYRot(),
                            serverPlayer.getXRot()
                    );
                }

                ItemStack dirt = new ItemStack(Blocks.DIRT);
                serverPlayer.setItemInHand(hand, dirt);
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}