# OverflowParticles Companion

A client-side companion for [Polyfrost's OverflowParticles](https://github.com/Polyfrost/OverflowParticles). Install it alongside the original mod. The companion reuses the original OneConfig settings and saved configuration; it does not bundle or replace OverflowParticles.

## Added customization

- Footstep particles on Ornithe 1.8.9: enable/disable, custom color in Multiply or Override mode, size up to 5×, amount multiplier, and adjustable fade.
- Block particles: custom color, size up to 5×, amount multiplier, and enable/disable alongside the existing digging/running/falling controls. Existing block fade settings remain bound to the original configuration.
- Modern Minecraft: restore Cloud settings that the original removed-footstep placeholder hides.

Footstep fading uses the footprint's own lifetime and does not accumulate each render frame. The original mod's general fading and fractional multiplier behavior are otherwise unchanged.

## Installation

1. Install the matching Ornithe or Fabric loader for your Minecraft version.
2. Install **OverflowParticles 1.2.3**, **OneConfig 1.4.0 or later**, and Fabric Language Kotlin. Keep the original mod's other platform dependencies, including Fabric API where applicable.
3. Add `overflowparticlescompanion-1.0.0+<minecraft-version>.jar` to the same `mods` folder.
4. Open the existing **OverflowParticles** settings in OneConfig. The added controls appear in its Footstep, Blocks, and Cloud sections as appropriate.

For Ornithe 1.8.9, use the Ornithe original jar and Java 25, which the original mod requires. The old Forge 1.8.9 release is incompatible with this companion. Remove any previous modified OverflowParticles jar before installing the official original; keep one original jar plus one companion jar.

The companion requires exactly OverflowParticles 1.2.3 because its mixins patch that release's internals. Future original releases need a compatibility check before changing the dependency constraint.

## Building and testing

On Windows:

```powershell
.\gradlew.bat :1.8.9:build
```

On Linux/macOS:

```sh
./gradlew :1.8.9:build
```

Artifacts are written to `versions/<minecraft-version>/build/libs/`. The build downloads the published original mod for compilation and integration tests. It is a runtime dependency, not an included jar.

Supported targets: Ornithe 1.8.9; Fabric 1.21.1, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1, 26.2, and 26.3. Integration tests audit both mods' mixins together and check particle registration, settings visibility, preserved block fade controls, and footstep color/fade behavior.

## Source layout

- `src/main`: the companion's mixins, small helpers, and mod metadata.
- `src/test`: tests against the actual published original mod.

Stonecutter processes these sources for each Minecraft version. Only classes in `org.waffles.overflowparticlescompanion` are packaged.
