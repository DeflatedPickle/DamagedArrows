/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.damagedarrows.mixin;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "ConstantConditions"})
@Mixin(Item.class)
public abstract class MixinItem {
  @Mutable @Shadow @Final private int maxDamage;
  @Mutable @Shadow @Final private int maxCount;

  @Inject(method = "<init>", at = @At("RETURN"))
  public void init(Settings settings, CallbackInfo ci) {
    if ((Object) this instanceof ArrowItem) {
      this.maxCount = 1;
      this.maxDamage = 32;
    }
  }

  @Inject(method = "canRepair", at = @At("RETURN"), cancellable = true)
  public void canRepair(
      ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
    if (stack.getItem() instanceof ArrowItem) {
      cir.setReturnValue(ingredient.getItem() == Items.FLINT);
    }
  }
}
