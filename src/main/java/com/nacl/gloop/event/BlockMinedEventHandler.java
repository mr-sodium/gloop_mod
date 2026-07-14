package com.nacl.gloop.event;

import com.nacl.gloop.block.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@EventBusSubscriber(modid = "gloop", bus = EventBusSubscriber.Bus.GAME)
public class BlockMinedEventHandler {

    @SubscribeEvent
    public static void onBlockMined(BlockEvent.BreakEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState originalState = event.getState();
        Player player = event.getPlayer();

        if (!level.isClientSide()) {
            if (originalState.is(Blocks.COAL_ORE) || originalState.is(Blocks.DEEPSLATE_COAL_ORE)) {

                if (!player.isCreative() && level instanceof ServerLevel serverLevel) {
                    // Check if they are using a valid tool (pickaxe)
                    if (player.hasCorrectToolForDrops(originalState)) {
                        // Get the item currently in the player's hand (with its enchantments)
                        ItemStack tool = player.getMainHandItem();

                        // Calculate drops using the block's loot table + tool modifiers (Fortune)
                        Block.getDrops(originalState, serverLevel, pos, null, player, tool).forEach(stack -> {
                            Block.popResource(serverLevel, pos, stack);
                        });
                    }
                }

                // Replace with Coal Gas
                level.setBlock(pos, ModBlocks.COAL_GAS.get().defaultBlockState(), 3);

                event.setCanceled(true);
            }
        }
    }
}