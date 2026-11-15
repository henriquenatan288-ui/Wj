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
        boolean shouldJump = client.player != null
                && client.options.keyUp.isDown()
                && client.player.onGround();
        client.options.keyJump.setDown(shouldJump);
    }

    @Override
    protected void onDisable() {
        Minecraft.getInstance().options.keyJump.setDown(false);
    }
}
