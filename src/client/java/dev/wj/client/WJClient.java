package dev.wj.client;

import dev.wj.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class WJClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.initialize();
        ClientTickEvents.END_CLIENT_TICK.register(client -> ModuleManager.tick());
        WJ.LOGGER.info("WJ Client initialized.");
    }
}
