# WJ Galaxy Tools

An original Fabric client-side visual mod for Minecraft **26.2**.

It gives Netherite tools an animated, Avaritia-inspired cosmic appearance: deep-space black, violet and blue nebulae, cyan highlights and small moving stars. The effect is generated in the fragment shader, not baked into a static galaxy image.

## Included tools

- Netherite Pickaxe
- Netherite Axe
- Netherite Shovel
- Netherite Hoe

The replacement textures are alpha masks. Only pixels inside each tool silhouette receive the galaxy; the area around the tool remains transparent. Other items keep their normal appearance.

## Technical details

- Minecraft 26.2
- Java 25
- Fabric Loader 0.20.1+
- Fabric API 0.161.0+26.2
- The shader is shipped as a built-in resource pack, enabled by default.
- The animation uses Minecraft's `GameTime` uniform, so it does not require a client tick loop.

**Compatibility note:** this effect replaces Minecraft's `core/item.fsh` shader through a resource pack. Core-shader replacement is an advanced, unsupported resource-pack technique and Mojang may change it between game versions. Another pack that replaces the same shader can conflict with this one.

## Build

The GitHub Actions workflow is manual only. To build after reviewing the source:

1. Open **Actions**.
2. Select **Build WJ Galaxy Tools**.
3. Choose **Run workflow**.

The workflow produces the mod JAR in the Actions artifacts. No build is started automatically by pushing commits.
