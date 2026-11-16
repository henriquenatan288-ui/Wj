#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

float hash21(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise2d(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));

    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;

    for (int i = 0; i < 4; i++) {
        value += noise2d(p) * amplitude;
        p = p * 2.03 + vec2(17.1, 9.2);
        amplitude *= 0.5;
    }

    return value;
}

vec3 galaxyColor(vec2 atlasPixel) {
    // GameTime completes one cycle every 24,000 ticks.
    // Ten cycles create a slow, continuous two-minute drift.
    float time = GameTime * 62.831853;

    vec2 p = atlasPixel * 0.43;
    vec2 drift = vec2(time * 0.012, -time * 0.009);

    float warpX = fbm(p + drift);
    float warpY = fbm(p + vec2(4.7, 8.3) - drift * 0.83);
    vec2 warped = p + (vec2(warpX, warpY) - 0.5) * 1.75;

    float cloudA = fbm(warped * 1.15 + drift * 0.7);
    float cloudB = fbm(warped * 2.15 - drift * 1.2 + vec2(2.4, -3.1));
    float cloudC = fbm(warped * 0.62 + vec2(-6.2, 1.8) + drift * 0.35);

    vec3 deepSpace = vec3(0.003, 0.004, 0.018);
    vec3 indigo = vec3(0.035, 0.018, 0.16);
    vec3 violet = vec3(0.28, 0.045, 0.53);
    vec3 blue = vec3(0.045, 0.22, 0.78);
    vec3 cyan = vec3(0.025, 0.62, 0.92);
    vec3 magenta = vec3(0.58, 0.055, 0.48);

    float nebulaMask = smoothstep(0.27, 0.76, cloudA + cloudC * 0.16);
    float blueMask = smoothstep(0.43, 0.83, cloudB);
    float violetMask = smoothstep(0.38, 0.78, cloudC);

    vec3 color = mix(deepSpace, indigo, nebulaMask);
    color = mix(color, violet, smoothstep(0.48, 0.84, cloudA) * 0.88);
    color = mix(color, blue, blueMask * 0.8);
    color = mix(color, magenta, violetMask * 0.38);

    float cyanVeil = smoothstep(0.72, 0.96, cloudA * 0.58 + cloudB * 0.42);
    color += cyan * cyanVeil * 0.42;

    float filamentWave = sin(warped.x * 2.6 + warped.y * 1.45 + cloudB * 4.2 + time * 0.10);
    float filament = pow(max(0.0, 1.0 - abs(filamentWave)), 10.0);
    color += vec3(0.17, 0.12, 0.52) * filament * 0.24;

    // Small sparse stars, with a gentle time-based twinkle.
    vec2 starGrid = atlasPixel * 0.82;
    vec2 cell = floor(starGrid);
    vec2 local = fract(starGrid) - 0.5;
    float seed = hash21(cell);
    vec2 starOffset = vec2(hash21(cell + 13.7), hash21(cell + 41.3)) * 0.62 - 0.31;
    float starDistance = length(local - starOffset);
    float starShape = 1.0 - smoothstep(0.018, 0.075, starDistance);
    float starPresence = step(0.885, seed);
    float twinkle = 0.68 + 0.32 * sin(time * 1.7 + seed * 6.2831853);
    float stars = starShape * starPresence * twinkle;
    color += vec3(0.58, 0.78, 1.0) * stars * 1.05;

    return clamp(color * 1.28, 0.0, 1.0);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);

#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
#endif

    // The four replacement tool textures share this exact cyan marker.
    // All other item textures stay on the vanilla rendering path.
    bool galaxyTool = color.a > 0.001
            && color.r < 0.015
            && color.g > 0.88
            && color.b > 0.94;

    if (galaxyTool) {
        vec2 atlasPixel = texCoord0 * vec2(textureSize(Sampler0, 0));
        color.rgb = galaxyColor(atlasPixel);
    }

    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    fragColor = apply_fog(
            color,
            sphericalVertexDistance,
            cylindricalVertexDistance,
            FogEnvironmentalStart,
            FogEnvironmentalEnd,
            FogRenderDistanceStart,
            FogRenderDistanceEnd,
            FogColor
    );
}
