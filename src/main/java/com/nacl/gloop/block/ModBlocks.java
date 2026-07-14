package com.nacl.gloop.block;

import com.nacl.gloop.Gloop;
import com.nacl.gloop.Item.ModItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Gloop.MODID);

    public static final DeferredBlock<Block> TUFF_MACHINE_CASING = registerBlock("tuff_machine_casing",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
            ));
    public static final DeferredBlock<Block> GLOOP_BLOCK = registerBlock("gloop_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.01f)
                    .sound(SoundType.SLIME_BLOCK)
                    .instabreak()
                    .noOcclusion()
            ));

    // Updated to use CoalGasBlock and added .noCollision()
    public static final DeferredBlock<CoalGasBlock> COAL_GAS = registerBlock("coal_gas",
            () -> new CoalGasBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noCollission()
                    .mapColor(MapColor.COLOR_PINK)
            ));


    public static final DeferredBlock<SlabBlock> MUD_SLAB = registerBlock("mud_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F, 2.0F)
                    .sound(SoundType.MUD)
            ));
    public static final DeferredBlock<SlabBlock> PACKED_MUD_SLAB = registerBlock("packed_mud_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F, 2.0F)
                    .sound(SoundType.PACKED_MUD)
            ));



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