/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.damagedarrows.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"WrongEntityDataParameterClass", "UnusedMixin"})
@Mixin(ArrowEntity.class)
public abstract class MixinArrowEntity extends PersistentProjectileEntity {
  private static final TrackedData<Integer> STACK_DAMAGE =
      DataTracker.registerData(ArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);

  protected MixinArrowEntity(
      EntityType<? extends PersistentProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "initFromStack", at = @At("TAIL"))
  public void initFromStack(ItemStack stack, CallbackInfo ci) {
    setStackDamage(stack.getDamage());
  }

  @Override
  protected void onEntityHit(EntityHitResult entityHitResult) {
    setStackDamage(getStackDamage() + 1);
  }

  @Override
  protected void onBlockCollision(BlockState state) {
    setStackDamage(getStackDamage() + 1);
  }

  @Inject(method = "asItemStack", at = @At("RETURN"))
  public void asItemStack(CallbackInfoReturnable<ItemStack> cir) {
    cir.getReturnValue().setDamage(getStackDamage());
  }

  @Inject(method = "initDataTracker", at = @At("TAIL"))
  public void initDataTracker(CallbackInfo ci) {
    dataTracker.startTracking(STACK_DAMAGE, 0);
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putInt("StackDamage", getStackDamage());
  }

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    setStackDamage(nbt.getInt("StackDamage"));
  }

  public int getStackDamage() {
    return this.dataTracker.get(STACK_DAMAGE);
  }

  public void setStackDamage(int stackDamage) {
    this.dataTracker.set(STACK_DAMAGE, stackDamage);
  }
}
