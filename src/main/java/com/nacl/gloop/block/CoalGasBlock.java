package com.nacl.gloop.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Explosion;
import java.util.function.BiConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import java.util.ArrayList;
import java.util.List;

public class CoalGasBlock extends Block {
    public static final IntegerProperty GAS_LEVEL = IntegerProperty.create("gas_level", 0, 3);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public CoalGasBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(GAS_LEVEL, 0));
    }

    // --- NO HITBOX & REPLACEABLE LIKE GRASS ---

    @Override
    protected boolean canBeReplaced(BlockState state, net.minecraft.world.item.context.BlockPlaceContext useContext) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction direction) {
        if (adjacentBlockState.is(this)) {
            int ourLevel = state.getValue(GAS_LEVEL);
            int neighborLevel = adjacentBlockState.getValue(GAS_LEVEL);

            if (direction == Direction.UP || direction == Direction.DOWN) {
                return ourLevel == 3 && neighborLevel == 3;
            }
            return ourLevel == neighborLevel;
        }
        return super.skipRendering(state, adjacentBlockState, direction);
    }

    @Override
    public float getExplosionResistance() {
        return 0.0F;
    }

    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (level instanceof ServerLevel serverLevel) {
            int ourUnits = state.getValue(GAS_LEVEL) + 1;
            triggerExplosion(serverLevel, pos, ourUnits);
        }
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 2 + level.random.nextInt(2));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 2 + level.getRandom().nextInt(2));
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private boolean isIgnitionSource(BlockState state) {
        return state.is(Blocks.FIRE)
                || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.TORCH)
                || state.is(Blocks.WALL_TORCH)
                || state.is(Blocks.SOUL_TORCH)
                || state.is(Blocks.SOUL_WALL_TORCH)
                || state.is(Blocks.LAVA)
                || state.is(BlockTags.FIRE)
                || state.is(BlockTags.CAMPFIRES);
    }

    private void triggerExplosion(ServerLevel level, BlockPos pos, int ourUnits) {
        if (!level.getBlockState(pos).is(this)) return;

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        float explosionPower = ourUnits * 0.75F;

        level.explode(null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, explosionPower, Level.ExplosionInteraction.TNT);

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.is(this)) {
                int neighborUnits = neighborState.getValue(GAS_LEVEL) + 1;
                triggerExplosion(level, neighborPos, neighborUnits);
            }
        }
    }

    private boolean isReplaceableSpace(BlockState state) {
        return state.isAir() || state.canBeReplaced();
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos).is(this)) return;

        int ourLevel = state.getValue(GAS_LEVEL);
        int ourUnits = ourLevel + 1;

        // 1. Ignition Scan
        for (Direction dir : Direction.values()) {
            if (dir == Direction.UP) continue;
            if (isIgnitionSource(level.getBlockState(pos.relative(dir)))) {
                triggerExplosion(level, pos, ourUnits);
                return;
            }
        }

        // 2. Gravity Check (Stacking Upward)
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (isReplaceableSpace(belowState) && !belowState.is(this)) {
            level.setBlock(belowPos, this.defaultBlockState().setValue(GAS_LEVEL, ourUnits - 1), 3);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        else if (belowState.is(this)) {
            int belowUnits = belowState.getValue(GAS_LEVEL) + 1;
            if (belowUnits < 4) {
                int roomAvailable = 4 - belowUnits;
                int unitsToDrop = Math.min(ourUnits, roomAvailable);

                level.setBlock(belowPos, belowState.setValue(GAS_LEVEL, (belowUnits + unitsToDrop) - 1), 3);
                int remaining = ourUnits - unitsToDrop;

                if (remaining > 0) {
                    level.setBlock(pos, state.setValue(GAS_LEVEL, remaining - 1), 3);
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
                return;
            }
        }

        // 3. Pyramid Flow Mechanics (Pressurized horizontal flow)
        Direction[] horizontalDirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction randomDir = horizontalDirs[random.nextInt(horizontalDirs.length)];

        BlockPos targetPos = pos.relative(randomDir);
        BlockState targetState = level.getBlockState(targetPos);

        if (isReplaceableSpace(targetState) && !targetState.is(this)) {
            // If we are at level 0 (1 unit), we can't spread without disappearing
            if (ourUnits > 1) {
                // To create a step-down pyramid, we only spill into air if we have high enough pressure
                // This forces a natural slope (e.g., Level 3 can spill to make a Level 0 neighbor)
                if (ourLevel >= 1) {
                    level.setBlock(pos, state.setValue(GAS_LEVEL, ourLevel - 1), 3);
                    level.setBlock(targetPos, this.defaultBlockState().setValue(GAS_LEVEL, 0), 3);
                }
            }
        }
        else if (targetState.is(this)) {
            int targetLevel = targetState.getValue(GAS_LEVEL);

            // CRITICAL PYRAMID RULE: Only flow if our level is at least 2 steps higher than the neighbor
            // This stops blocks from flattening into perfectly flat sheets.
            // It forces a strict step-down slope: Center (3) -> Middle (1) -> Edge (0)
            if (ourLevel > targetLevel + 1) {
                level.setBlock(pos, state.setValue(GAS_LEVEL, ourLevel - 1), 3);
                level.setBlock(targetPos, targetState.setValue(GAS_LEVEL, targetLevel + 1), 3);
            }
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int ourLevel = state.getValue(GAS_LEVEL);
        if (random.nextInt(18) == 0) {
            if (ourLevel > 0) {
                level.setBlock(pos, state.setValue(GAS_LEVEL, ourLevel - 1), 3);
            } else {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GAS_LEVEL);
    }
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            double eyeY = livingEntity.getEyeY();
            if (eyeY >= pos.getY() && eyeY <= pos.getY() + 1.0D) {

                net.minecraft.nbt.CompoundTag data = livingEntity.getPersistentData();
                int gasTicks = data.getInt("CoalGasTicks");

                // 20 ticks = 1 second
                if (gasTicks >= 20) {
                    // Apply Blindness for 5 seconds (100 ticks).
                    // Ambient = true, visible particles = false makes it look like environmental blindness
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0, true, false));
                } else {
                    data.putInt("CoalGasTicks", gasTicks + 1);
                }
            }
        }
    }
}