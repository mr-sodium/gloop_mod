package com.nacl.gloop.Items;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;

public class ScytheItemClass extends Item {

    private static final ResourceLocation ATTACK_RANGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_range");
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_damage");
    private static final ResourceLocation ATTACK_SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_speed");

    public ScytheItemClass(Properties properties) {
        super(properties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY.withTooltip(false)));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        if (itemAbility == ItemAbilities.SWORD_SWEEP) {
            return true;
        }
        return super.canPerformAction(stack, itemAbility);
    }
    @Override
    public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        int vanillaLevel = super.getEnchantmentLevel(stack, enchantment);
        if (enchantment.is(Enchantments.SWEEPING_EDGE)) {
            return vanillaLevel + 1;
        }

        return vanillaLevel;
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getCommandSenderWorld().isClientSide()) {



            double attackerX = attacker.getX();
            double attackerZ = attacker.getZ();

            double targetX = target.getX();
            double targetZ = target.getZ();

            double currentMovementX = target.getDeltaMovement().x;
            double currentMovementY = target.getDeltaMovement().y;
            double currentMovementZ = target.getDeltaMovement().z;
            double knockbackstat = target.getAttributeValue(KNOCKBACK_RESISTANCE) - 1;

//            attacker.sendSystemMessage(Component.literal("Variable value: " + knockbackstat ));
            if(!(target.getAttributeValue(KNOCKBACK_RESISTANCE) > 1) && !(target.getType().is(Tags.EntityTypes.BOSSES))) {
                target.setDeltaMovement(
                        (currentMovementX+(((targetX-attackerX) * 0.4)*knockbackstat)),
                        (currentMovementY+0.09),
                        (currentMovementZ+(((targetZ-attackerZ) * 0.4)*knockbackstat))
                );
            }
            target.hurtMarked = true;

            if (attacker instanceof ServerPlayer serverPlayer) {
                stack.hurtAndBreak(1, serverPlayer, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers originalModifiers = super.getDefaultAttributeModifiers(stack);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        originalModifiers.modifiers().forEach(entry -> builder.add(entry.attribute(), entry.modifier(), entry.slot()));

        double damageValue = getScytheDamageModifier(stack);

        builder.add(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, damageValue, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        builder.add(Attributes.ATTACK_SPEED,
                new AttributeModifier(ATTACK_SPEED_MODIFIER_ID, -3.2D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        builder.add(Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(ATTACK_RANGE_MODIFIER_ID, 2.5D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        return builder.build();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int totalDamageDisplay = (int) (getScytheDamageModifier(stack) + 1);

        tooltipComponents.add(Component.literal("When in Main Hand:").withStyle(net.minecraft.ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal(" " + totalDamageDisplay + " Attack Damage").withStyle(net.minecraft.ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.literal(" 0.8 Attack Speed").withStyle(net.minecraft.ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.literal(" +2.5 Entity Attack Range").withStyle(net.minecraft.ChatFormatting.BLUE));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private double getScytheDamageModifier(ItemStack stack) {
        Item item = stack.getItem();

        if (item == ModItems.wooden_scythe.get()) {
            return 2.0D;
        } else if (item == ModItems.stone_scythe.get()) {
            return 3.0D;
        } else if (item == ModItems.iron_scythe.get()) {
            return 5.0D;
        } else if (item == ModItems.golden_scythe.get()) {
            return 3.0D;
        } else if (item == ModItems.diamond_scythe.get()) {
            return 5.0D;
        } else if (item == ModItems.netherite_scythe.get()) {
            return 6.0D;
        }

        return 0.0D;
    }
//    public void BlockBreak(Player player,)
}