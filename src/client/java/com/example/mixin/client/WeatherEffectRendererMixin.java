package com.example.mixin.client;

import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Begrenze topY beim Erzeugen von Rain ColumnInstances auf max 192,
 * damit kein Regen über der Wolkenhöhe gerendert wird.
 */
@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

    /**
     * In collectColumnInstances wird createRainColumnInstance(randomSource, i, o, q, r, n, u, f)
     * aufgerufen. Argument-Index 4 ist das 'r' (topY).
     * Wir ändern dieses Argument und clampen auf 192.
     */
    @ModifyArg(
        method = "collectColumnInstances",
        at = @At(
            value = "INVOKE",
            // Signatur: createRainColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
            target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;createRainColumnInstance(Lnet/minecraft/util/RandomSource;IIIIIIF)Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;"
        ),
        index = 4 // das 5. Argument (0-based) ist topY (r)
    )
    private int clampRainTopY(int originalTopY) {
        final int CLOUD_HEIGHT = 192;
        if (originalTopY > CLOUD_HEIGHT) {
            return CLOUD_HEIGHT;
        }
        return originalTopY;
    }
}
