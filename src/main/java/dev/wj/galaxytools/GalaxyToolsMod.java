package dev.wj.galaxytools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class GalaxyToolsMod implements ModInitializer {
    public static final String MOD_ID = "wjgalaxytools";

    @Override
    public void onInitialize() {
        var container = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .orElseThrow(() -> new IllegalStateException(
                        "WJ Galaxy Tools mod container was not found"
                ));

        boolean registered = ResourceLoader.registerBuiltinPack(
                Identifier.fromNamespaceAndPath(MOD_ID, "galaxy_tools"),
                container,
                Component.literal("WJ Galaxy Tools"),
                PackActivationType.DEFAULT_ENABLED
        );

        if (!registered) {
            throw new IllegalStateException("Could not register the WJ Galaxy Tools resource pack");
        }
    }
}
