package org.waffles.overflowparticlescompanion.test

//? if >1.8.9 {
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import net.minecraft.core.particles.ParticleTypes
//?} else {
/*import net.fabricmc.loader.api.FabricLoader
import net.ornithemc.osl.entrypoints.api.ModInitializer
import net.minecraft.Bootstrap
*///?}
import net.fabricmc.loader.api.FabricLoader as Loader
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.collect.impl.OneConfigCollector
import org.polyfrost.overflowparticles.client.config.PerParticleConfigManager
import org.polyfrost.overflowparticles.client.config.OverflowParticlesConfig
import org.polyfrost.overflowparticles.client.particles.ParticleRegistry
import org.polyfrost.overflowparticles.client.particles.VanillaParticles
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.transformer.IMixinTransformer
import org.waffles.overflowparticlescompanion.CompanionColors
import org.waffles.overflowparticlescompanion.CompanionConfig

class CompanionIntegrationTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setupEnvironment() {
            //? if >1.8.9 {
            SharedConstants.tryDetectVersion()
            Bootstrap.bootStrap()
            //?} else {
            /*FabricLoader.getInstance().invokeEntrypoints(
                ModInitializer.ENTRYPOINT_KEY, ModInitializer::class.java, ModInitializer::init
            )
            Bootstrap.init()
            *///?}
            VanillaParticles.preload()
            PerParticleConfigManager.fillConfigs()
        }
    }

    @Test
    fun `original mod and companion mixins load together`() {
        assertTrue(Loader.getInstance().isModLoaded("overflowparticles"))
        assertTrue(Loader.getInstance().isModLoaded("overflowparticlescompanion"))
        val environment = MixinEnvironment.getCurrentEnvironment()
        assertInstanceOf(IMixinTransformer::class.java, environment.activeTransformer)
        environment.setOption(MixinEnvironment.Option.REFMAP_REMAP, false)
        environment.audit()
    }

    @Test
    fun `additional particle settings stay visible`() {
        //? if =1.8.9 {
        /*val particle = VanillaParticles.FOOTSTEP
        assertFalse(particle.isIgnored)
        assertFalse(particle.isUnfair)
        *///?} else {
        val particle = ParticleRegistry.of(ParticleTypes.CLOUD)!!
        assertEquals("Cloud", particle.name)
        assertFalse(particle.isIgnored)
        //?}
        val config = PerParticleConfigManager.getConfigByType(particle)
        val tree = Tree.tree("particle-controls")
        OneConfigCollector().handle(tree, config, 0)
        config.handle(tree)
        for (key in listOf("customColor", "colorMode", "color", "size", "multiplier")) {
            assertNotNull(tree.getProp(key), key)
        }
        assertEquals(5f, tree.getProp("size").getMetadata<Number>("max")?.toFloat())
    }

    @Test
    fun `block controls preserve the original fade settings`() {
        assertFalse(VanillaParticles.BLOCKS.isUnfair)
        val root = Tree.tree("root")
        val blockTree = Tree.tree(ParticleRegistry.location(VanillaParticles.BLOCKS.id).toString())
        OneConfigCollector().handle(blockTree, PerParticleConfigManager.blockSetting, 0)
        root.put(blockTree)
        val fade = blockTree.getProp("fade")
        val fadeStart = blockTree.getProp("fadeStart")
        CompanionConfig.addBlockControls(root)
        assertSame(fade, blockTree.getProp("fade"))
        assertSame(fadeStart, blockTree.getProp("fadeStart"))
        for (key in listOf("hideDigging", "hideRunning", "customColor", "colorMode", "color", "size", "multiplier", "enabled")) {
            assertNotNull(blockTree.getProp(key), key)
        }
        val actualTree = OverflowParticlesConfig::class.java.getDeclaredMethod("makeTree")
            .apply { isAccessible = true }.invoke(OverflowParticlesConfig) as Tree
        val actualBlocks = actualTree.getChild(blockTree.id)
        assertNotNull(actualBlocks)
        assertNotNull(actualBlocks.getProp("multiplier"))
        assertNotNull(actualBlocks.getProp("hideDigging"))
    }

    @Test
    fun `block burst controls respect disabled particles and the global toggle`() {
        val config = PerParticleConfigManager.getConfigByType(VanillaParticles.BLOCKS)
        val previousMultiplier = config.multiplier
        val previousEnabled = config.enabled
        val previousGlobal = OverflowParticlesConfig.modEnabled
        val previousHide = PerParticleConfigManager.blockSetting.hideDigging
        try {
            OverflowParticlesConfig.modEnabled = true
            config.enabled = true
            PerParticleConfigManager.blockSetting.hideDigging = false
            config.multiplier = 0f
            assertEquals(0, CompanionConfig.blockBurstCount())
            config.multiplier = 2.5f
            assertEquals(3, CompanionConfig.blockBurstCount())
            config.enabled = false
            assertEquals(0, CompanionConfig.blockBurstCount())
            config.enabled = true
            PerParticleConfigManager.blockSetting.hideDigging = true
            assertEquals(0, CompanionConfig.blockBurstCount())
            OverflowParticlesConfig.modEnabled = false
            assertEquals(1, CompanionConfig.blockBurstCount())
        } finally {
            config.multiplier = previousMultiplier
            config.enabled = previousEnabled
            OverflowParticlesConfig.modEnabled = previousGlobal
            PerParticleConfigManager.blockSetting.hideDigging = previousHide
        }
    }

    @Test
    fun `footstep fade remains active when colors override alpha`() {
        val config = PerParticleConfigManager.getConfigByType(VanillaParticles.BLOCKS)
        val previousCustomColor = config.customColor
        val previousMode = config.colorMode
        try {
            config.customColor = false
            assertEquals(0.4f, CompanionColors.adjustLegacy(config, 0.8f, 3, 0.5f))
            config.customColor = true
            config.colorMode = 1
            assertEquals(config.color.alpha / 255f * 0.5f, CompanionColors.adjustLegacy(config, 0.8f, 3, 0.5f))
        } finally {
            config.customColor = previousCustomColor
            config.colorMode = previousMode
        }
        assertEquals(0.8f, CompanionColors.adjustLegacy(null, 0.8f, 3, 0.5f))
    }

    @Test
    fun `footstep fade does not accumulate across repeated frames`() {
        repeat(100) { assertEquals(0.5f, CompanionConfig.fadeFactor(150f, 200, 0.5f)) }
        assertEquals(1f, CompanionConfig.fadeFactor(50f, 200, 0.5f))
        assertEquals(0f, CompanionConfig.fadeFactor(200f, 200, 0.5f))
    }
}
