package dev.wj.client.module;

import dev.wj.client.module.impl.AutoJumpModule;
import dev.wj.client.module.impl.SprintModule;
import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ModuleManager {
    private static final Map<String, Module> MODULES = new LinkedHashMap<>();
    private static boolean initialized;

    private ModuleManager() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        register(new SprintModule());
        register(new AutoJumpModule());
        initialized = true;
    }

    public static void register(Module module) {
        Objects.requireNonNull(module, "module");
        String key = module.getName().toLowerCase(java.util.Locale.ROOT);
        if (MODULES.putIfAbsent(key, module) != null) {
            throw new IllegalArgumentException("A module named '" + module.getName() + "' is already registered");
        }
    }

    public static Module get(String name) {
        if (name == null) {
            return null;
        }
        return MODULES.get(name.toLowerCase(java.util.Locale.ROOT));
    }

    public static Collection<Module> getModules() {
        return Collections.unmodifiableCollection(MODULES.values());
    }

    public static void tick(Minecraft client) {
        for (Module module : MODULES.values()) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }
}
