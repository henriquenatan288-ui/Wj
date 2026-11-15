package dev.wj.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.wj.client.gui.WJClickGui;
import dev.wj.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class WJClient implements ClientModInitializer {
    private static KeyMapping openClickGui;

    @Override
    public void onInitializeClient() {
        ModuleManager.initialize();

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(WJ.MOD_ID, "main")
        );
        openClickGui = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.wj.open_clickgui",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openClickGui.consumeClick()) {
                if (client.gui.screen() == null) {
                    client.gui.setScreen(new WJClickGui());
                }
            }
            ModuleManager.tick(client);
        });

        WJ.LOGGER.info("WJ Client initialized.");
    }
}
