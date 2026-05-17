package com.nacl.gloop.Items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class ScytheItemClass extends Item {

    private static final ResourceLocation ATTACK_RANGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_range");
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_damage");
    private static final ResourceLocation ATTACK_SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_speed");

    public ScytheItemClass(Properties properties) {
        super(properties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY.withTooltip(false)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getCommandSenderWorld().isClientSide()) {
            double attackerX = attacker.getX();
            double attackerY = attacker.getY();
            double attackerZ = attacker.getZ();

            double targetX = target.getX();
            double targetY = target.getY();
            double targetZ = target.getZ();

            double currentMovementX = target.getDeltaMovement().x;
            double currentMovementY = target.getDeltaMovement().y;
            double currentMovementZ = target.getDeltaMovement().z;

            target.setDeltaMovement(
                    (currentMovementX + ((targetX - attackerX) * -0.4)),
                    (currentMovementY + 0.09),
                    (currentMovementZ + ((targetZ - attackerZ) * -0.4))
            );
            target.hurtMarked = true;
            stack.hurtAndBreak(1, attacker, stack.getEquipmentSlot());
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

    /**
     * Helper method to determine the base attack damage modifier depending on the item variant.
     * Compares the stack's Item instance directly against your registered items.
     */
    private double getScytheDamageModifier(ItemStack stack) {
        Item item = stack.getItem();

        if (item == ModItems.wooden_scythe.get()) {
            return 2.0D; //total damage of sword alternitive - 2
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

        return 0.0D; // Default fallback if item variant doesn't match
    }
}