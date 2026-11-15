package dev.wj.client.module;

import java.util.Objects;

public abstract class Module {
    private final String name;
    private final Category category;
    private boolean enabled;

    protected Module(String name, Category category) {
        this.name = Objects.requireNonNull(name, "name");
        this.category = Objects.requireNonNull(category, "category");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Module name cannot be blank");
        }
    }

    public final String getName() {
        return name;
    }

    public final Category getCategory() {
        return category;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public void onTick() {
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }
}
