package com.nacl.gloop.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = "gloop")
public class ModEnchantmentEvents {

    // Tracks temporary protection expiration timestamps (in milliseconds) for Ground Burst triggers
    private static final Map<UUID, Long> GROUND_BURST_PROTECTION = new HashMap<>();

    @SubscribeEvent
    public static void onEntityHit(LivingIncomingDamageEvent event) {
        // --- GROUND BURST PROTECTION CHECK ---
        // If a player takes damage, check if they are immune to explosion or fall damage due to a recent burst
        if (event.getEntity() instanceof Player player) {
            UUID playerUUID = player.getUUID();
            if (GROUND_BURST_PROTECTION.containsKey(playerUUID)) {
                if (System.currentTimeMillis() < GROUND_BURST_PROTECTION.get(playerUUID)) {
                    var source = event.getSource();
                    if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.FALL)) {
                        event.setCanceled(true);
                        return; // Exit early so protected players don't process further logic for this hit
                    }
                } else {
                    GROUND_BURST_PROTECTION.remove(playerUUID); // Clean up expired protections
                }
            }
        }

        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        ItemStack tool = player.getMainHandItem();

        int level = tool.getEnchantmentLevel(player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ModEnchantments.BACKSTAB));
        if (level > 0) {
            float originalDamage = event.getAmount();
            if(player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())){
                event.setAmount(originalDamage * 0.1F);
                return;
            }
            double playerYaw = player.getYRot();
            double targetYaw = event.getEntity().getYRot();

            playerYaw = (playerYaw % 360 + 360) % 360;
            targetYaw = (targetYaw % 360 + 360) % 360;

            double diff = Math.abs(targetYaw - playerYaw);
            double shortestDist = Math.min(diff, 360 - diff);

            if (shortestDist <= 60) {
                player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 400);
                event.setAmount(originalDamage * 3.0F);
            }
        }
    }

    private static final Map<UUID, ResourceLocation> LAST_MINED_BLOCK = new HashMap<>();
    private static final Map<UUID, Integer> MINING_COMBO = new HashMap<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide()) return;

        ItemStack tool = player.getMainHandItem();
        Level level = (Level) event.getLevel();

        var enchantmentRegistry = player.level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
        if (enchantmentRegistry == null) return;

        // --- GROUND BURST LOGIC ---
        var chatEnchantmentHolder = enchantmentRegistry.get(ModEnchantments.GROUNDBURST).orElse(null);
        if (chatEnchantmentHolder != null) {
            int groundBurstLevel = tool.getEnchantmentLevel(chatEnchantmentHolder);
            if (groundBurstLevel > 0) {
                if (Math.floor(Math.random() * 250) + 1 == 1) {
                    UUID playerUUID = player.getUUID();

                    GROUND_BURST_PROTECTION.put(playerUUID, System.currentTimeMillis() + 3500);

                    float explosionRadius = 0.5f + (groundBurstLevel * 2.0f);

                    if (level instanceof ServerLevel serverLevel) {
                        // CHANGED: Use ExplosionInteraction.TNT instead of BLOCK.
                        // TNT interaction guarantees a 100% drop rate for all destroyed blocks.
                        serverLevel.explode(
                                player,
                                event.getPos().getX() + 0.5,
                                event.getPos().getY() + 0.5,
                                event.getPos().getZ() + 0.5,
                                explosionRadius,
                                false,
                                Level.ExplosionInteraction.TNT
                        );
                    }
                }
            }
        }

        var accelerationHolder = enchantmentRegistry.get(ModEnchantments.ACCELERATION).orElse(null);
        if (accelerationHolder == null) return;

        int enchantLevel = tool.getEnchantmentLevel(accelerationHolder);

        if (enchantLevel > 0) {
            UUID playerUUID = player.getUUID();
            BlockState state = event.getState();

            ResourceLocation currentBlockKey = state.getBlockHolder().unwrapKey().map(net.minecraft.resources.ResourceKey::location).orElse(null);
            ResourceLocation lastBlockKey = LAST_MINED_BLOCK.get(playerUUID);

            if (currentBlockKey != null && currentBlockKey.equals(lastBlockKey)) {
                int currentCombo = MINING_COMBO.getOrDefault(playerUUID, 0);
                if (currentCombo < (enchantLevel * 10)) {
                    MINING_COMBO.put(playerUUID, currentCombo + 1);
                }
            } else {
                MINING_COMBO.put(playerUUID, 1);
            }

            LAST_MINED_BLOCK.put(playerUUID, currentBlockKey);
        } else {
            clearPlayerData(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack tool = player.getMainHandItem();

        var enchantmentRegistry = player.level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
        if (enchantmentRegistry == null) return;

        var enchantmentHolder = enchantmentRegistry.get(ModEnchantments.ACCELERATION).orElse(null);
        if (enchantmentHolder == null) return;

        int enchantLevel = tool.getEnchantmentLevel(enchantmentHolder);

        if (enchantLevel > 0) {
            UUID playerUUID = player.getUUID();
            BlockState currentTargetState = event.getState();
            if (currentTargetState == null) return;

            ResourceLocation targetBlockKey = currentTargetState.getBlockHolder().unwrapKey().map(net.minecraft.resources.ResourceKey::location).orElse(null);
            ResourceLocation lastMinedKey = LAST_MINED_BLOCK.get(playerUUID);

            int combo = MINING_COMBO.getOrDefault(playerUUID, 0);

            if (targetBlockKey != null && targetBlockKey.equals(lastMinedKey)) {
                if (combo < (enchantLevel * 10)) {
                    combo += 1;
                }
            } else if (lastMinedKey != null) {
                combo = 0;
            }

            // Starts at a 20% penalty (0.8f). Each combo stack adds 30% speed.
            float speedMultiplier = 0.8f + (combo * 0.30f);
            event.setNewSpeed(event.getOriginalSpeed() * speedMultiplier);

        } else {
            if (!player.level().isClientSide()) {
                clearPlayerData(player.getUUID());
            }
        }
    }

    private static void clearPlayerData(UUID uuid) {
        LAST_MINED_BLOCK.remove(uuid);
        MINING_COMBO.remove(uuid);
    }
}