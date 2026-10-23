plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            regex(eval(current.version, "= 1.8.9")) {
                replace("(?<![\\w.])ParticleEngine\\b", "ParticleManager", "(?<![\\w.])ParticleManager\\b", "ParticleEngine")
                replace("(?<![\\w.])ClientLevel\\b", "ClientWorld", "(?<![\\w.])ClientWorld\\b", "ClientLevel")
                // Match Kotlin imports with either LF or CRLF line endings.
                replace("(?m)^import net.minecraft.core.particles.ParticleTypes(?=\\r?$)", "import net.minecraft.entity.particle.ParticleType as ParticleTypes", "(?m)^import net.minecraft.entity.particle.ParticleType as ParticleTypes(?=\\r?$)", "import net.minecraft.core.particles.ParticleTypes")
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
                    "net.minecraft.client.multiplayer.ClientLevel",
                    "net.minecraft.client.world.ClientWorld"
                )
                replace(
                    "net.minecraft.world.entity.Entity",
                    "net.minecraft.entity.Entity"
                )
                replace(
                    "net.minecraft.server.Bootstrap",
                    "net.minecraft.Bootstrap"
                )
            }
        }
    }
}
