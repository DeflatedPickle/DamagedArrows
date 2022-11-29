/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.damagedarrows.mixin;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"rawtypes", "UnusedMixin"})
@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager {
  @Inject(
      method =
          "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              shift = At.Shift.AFTER,
              target = "Ljava/util/Map$Entry;getKey()Ljava/lang/Object;"),
      locals = LocalCapture.CAPTURE_FAILSOFT)
  public void apply(
      Map<Identifier, JsonElement> map,
      ResourceManager resourceManager,
      Profiler profiler,
      CallbackInfo ci,
      Map map2,
      Builder builder,
      Iterator var6,
      Entry entry) {
    if (((Identifier) entry.getKey()).getPath().contains("arrow")) {
      JsonObject value = (JsonObject) entry.getValue();

      if (value != null) {
        JsonObject result = (JsonObject) value.get("result");

        if (result != null) {
          result.add("count", new JsonPrimitive(1));
        }
      }
    }
  }
}
