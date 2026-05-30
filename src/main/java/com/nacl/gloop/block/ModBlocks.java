package com.nacl.gloop.block;

import com.nacl.gloop.Gloop;
import com.nacl.gloop.Item.ModItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
   public static final DeferredRegister.Blocks BLOCKS =
           DeferredRegister.createBlocks(Gloop.MODID);

   public static final DeferredBlock<Block> TUFF_MACHINE_CASING = registerBlock("tuff_machine_casing",
    () -> new Block(BlockBehaviour.Properties.of()
        .noOcclusion()
        .strength(4f)
    ));
//    public static final DeferredBlock<Block> ANTICOMPARATOR = registerBlock("anti_comparator",
//            () -> new AntiComparator(BlockBehaviour.Properties.of()
//                    .strength(0.4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

   private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}