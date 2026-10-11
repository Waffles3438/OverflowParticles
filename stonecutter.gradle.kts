plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3" /* [SC] DO NOT EDIT */

stonecutter {
    tasks {
        order("publishModrinth")
    }

    parameters {
        replacements {
            regex(eval(current.version, "= 1.8.9")) {
                replace("(?<![\\w.])ParticleEngine\\b", "ParticleManager", "(?<![\\w.])ParticleManager\\b", "ParticleEngine")
                replace("(?<![\\w.])TrackingEmitter\\b", "EmitterParticle", "(?<![\\w.])EmitterParticle\\b", "TrackingEmitter")
                replace("(?<![\\w.])ClientLevel\\b", "ClientWorld", "(?<![\\w.])ClientWorld\\b", "ClientLevel")
                // Match Kotlin imports with either LF or CRLF line endings.
                replace("(?m)^import net.minecraft.core.particles.ParticleType(?=\\r?$)", "import org.polyfrost.overflowparticles.utils.ParticleType", "(?m)^import org.polyfrost.overflowparticles.utils.ParticleType(?=\\r?$)", "import net.minecraft.core.particles.ParticleType")
                replace("(?m)^import net.minecraft.core.particles.ParticleTypes(?=\\r?$)", "import net.minecraft.entity.particle.ParticleType as ParticleTypes", "(?m)^import net.minecraft.entity.particle.ParticleType as ParticleTypes(?=\\r?$)", "import net.minecraft.core.particles.ParticleTypes")
                replace("(?m)^import net.minecraft.resources.ResourceLocation(?=\\r?$)", "import net.minecraft.resource.Identifier as ResourceLocation", "(?m)^import net.minecraft.resource.Identifier as ResourceLocation(?=\\r?$)", "import net.minecraft.resources.ResourceLocation")
                replace("(?m)^import net.minecraft.world.entity.player.Player(?=\\r?$)", "import net.minecraft.entity.living.player.PlayerEntity as Player", "(?m)^import net.minecraft.entity.living.player.PlayerEntity as Player(?=\\r?$)", "import net.minecraft.world.entity.player.Player")
                replace("(?m)^import net.minecraft.network.protocol.Packet(?=\\r?$)", "import net.minecraft.network.packet.Packet", "(?m)^import net.minecraft.network.packet.Packet(?=\\r?$)", "import net.minecraft.network.protocol.Packet")
                replace("(?m)^import net.minecraft.network.protocol.game.ClientboundEntityEventPacket(?=\\r?$)", "import net.minecraft.network.packet.s2c.play.EntityEventS2CPacket as ClientboundEntityEventPacket", "(?m)^import net.minecraft.network.packet.s2c.play.EntityEventS2CPacket as ClientboundEntityEventPacket(?=\\r?$)", "import net.minecraft.network.protocol.game.ClientboundEntityEventPacket")
            }
            string(eval(current.version, "= 1.8.9")) {
                replace(
                    "net.minecraft.client.particle.Particle",
                    "net.minecraft.client.entity.particle.Particle"
                )
                replace(
                    "net.minecraft.client.particle.ParticleEngine",
                    "net.minecraft.client.ParticleManager"
                )
                replace(
                    "net.minecraft.client.particle.TrackingEmitter",
                    "net.minecraft.client.entity.particle.EmitterParticle"
                )
                replace(
                    "net.minecraft.client.multiplayer.ClientLevel",
                    "net.minecraft.client.world.ClientWorld"
                )
                replace(
                    "net.minecraft.world.entity.Entity",
                    "net.minecraft.entity.Entity"
                )
                replace(
                    "net.minecraft.world.entity.LivingEntity",
                    "net.minecraft.entity.living.LivingEntity"
                )
                replace(
                    "net.minecraft.server.Bootstrap",
                    "net.minecraft.Bootstrap"
                )
            }
        }
    }
}
