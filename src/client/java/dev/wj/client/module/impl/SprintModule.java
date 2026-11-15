package dev.wj.client.module.impl;

import dev.wj.client.module.Category;
import dev.wj.client.module.Module;
import net.minecraft.client.Minecraft;

public final class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", Category.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null) {
            return;
        }

        if (client.options.keyUp.isDown() && !client.player.isShiftKeyDown()) {
            client.player.setSprinting(true);
        }
    }
}
