package com.nacl.gloop.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.Tags;
import java.util.List;

import static net.minecraft.world.entity.ai.attributes.Attributes.BLOCK_INTERACTION_RANGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;

public class DaggerItemClass extends Item {

    private static final ResourceLocation ATTACK_RANGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_range");
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "dagger_damage");
    private static final ResourceLocation ATTACK_SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_speed");
    private static final ResourceLocation BLOCK_INTERACTION_RANGE_ID = ResourceLocation.fromNamespaceAndPath("gloop", "scythe_range");


    public DaggerItemClass(Properties properties) {
        super(properties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY.withTooltip(false)));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return super.canPerformAction(stack, itemAbility);
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getCommandSenderWorld().isClientSide()) {
            if(!attacker.getOffhandItem().isEmpty() && attacker.getMainHandItem().is(attacker.getOffhandItem().getItem())){
                target.invulnerableTime = 0;
                target.hurtDuration = 0;

                float separateDamage = (float) (getDaggerDamageModifier(attacker.getMainHandItem()) * 0.5);
                target.hurt(attacker.damageSources().mobAttack(attacker), separateDamage);

            }
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

        double damageValue = getDaggerDamageModifier(stack);

        builder.add(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, damageValue, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        builder.add(Attributes.ATTACK_SPEED,
                new AttributeModifier(ATTACK_SPEED_MODIFIER_ID, -2.1D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        builder.add(Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(ATTACK_RANGE_MODIFIER_ID, -1.0D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND);


        builder.add(Attributes.BLOCK_INTERACTION_RANGE,
                new AttributeModifier(BLOCK_INTERACTION_RANGE_ID, -1.0D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );

        return builder.build();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int totalDamageDisplay = (int) (getDaggerDamageModifier(stack) + 1);

        tooltipComponents.add(Component.literal("When in Main Hand:").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal(" " + totalDamageDisplay + " Attack Damage").withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.literal(" 0.8 Attack Speed").withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(Component.literal(" -1 reach").withStyle(ChatFormatting.DARK_RED));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public double getDaggerDamageModifier(ItemStack stack) {
        Item item = stack.getItem();

        if (item == ModItems.Wooden_dagger.get()) {
            return 2.5D;
        } else if (item == ModItems.Stone_dagger.get()) {
            return 3.0D;
        } else if (item == ModItems.Iron_dagger.get()) {
            return 5.0D;
        } else if (item == ModItems.Golden_dagger.get()) {
            return 3.0D;
        } else if (item == ModItems.Diamond_dagger.get()) {
            return 5.0D;
        } else if (item == ModItems.Netherite_dagger.get()) {
            return 6.0D;
        }
        return 0.0D;
    }
//    public void BlockBreak(Player player,)
}