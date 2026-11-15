package dev.wj.client.module.impl;

import dev.wj.client.module.Category;
import dev.wj.client.module.Module;
import net.minecraft.client.Minecraft;

public final class AutoJumpModule extends Module {
    public AutoJumpModule() {
        super("AutoJump", Category.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null) {
            return;
        }

        if (client.options.keyUp.isDown() && client.player.onGround()) {
            client.player.jump();
        }
    }
}
