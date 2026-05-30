//package com.nacl.gloop.block;
//
//import com.nacl.gloop.Gloop;
//import net.minecraft.util.valueproviders.UniformInt;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.DropExperienceBlock;
//import net.minecraft.world.level.block.SoundType;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.registries.DeferredBlock;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//import java.util.function.Supplier;
//
//public class ModBlocks {
//    public static final DeferredRegister.Blocks BLOCKS =
//            DeferredRegister.createBlocks(Gloop.MODID);
//
//    public static final DeferredBlock<Block> TUFFMACHINECASING = registerBlock("tuff_machine_casing",
//            () -> new Block(BlockBehaviour.Properties.of()
//                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)));
////    public static final DeferredBlock<Block> ANTICOMPARATOR = registerBlock("anti_comparator",
////            () -> new AntiComparator(BlockBehaviour.Properties.of()
////                    .strength(0.4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
//
//
//
//    // Simpler helper method: just registers the block
//    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
//        return BLOCKS.register(name, block);
//    }
//
//    public static void register(IEventBus eventBus) {
//        BLOCKS.register(eventBus);
//    }
//}