package org.polyfrost.overflowparticles.config

import cc.polyfrost.oneconfig.config.elements.SubConfig
import cc.polyfrost.oneconfig.internal.config.core.ConfigCore
import org.polyfrost.overflowparticles.OverflowParticles

class ParticleConfig(val name: String, val id: Int) : SubConfig(name, "") {

    val entry: ParticleEntry
        get() {
            ModConfig.particles[name] ?: ModConfig.particles.put(name, ParticleEntry())
            return ModConfig.particles[name]!!
        }

    override fun initialize() {
        mod.config = this
        generateOptionList(entry, mod.defaultPage, mod, false)
        ConfigCore.mods.add(this.mod)
        addDependency("color", "customColor")
        addDependency("colorMode", "customColor")
        addDependency("hideMode", "hideRunning")
        val colors = listOf("customColor", "colorMode", "color")
    }

    override fun reInitialize() {
    }

    override fun load() {
    }

    override fun save() {
        entry.active = enabled
    }

}