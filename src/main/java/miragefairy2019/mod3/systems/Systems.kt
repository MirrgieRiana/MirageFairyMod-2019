package miragefairy2019.mod3.systems

import miragefairy2019.libkt.module

object Systems {
    val module = module {
        DamageSource.module(this)
        ManualRepair.module(this)
        Combine.module(this)
        VanillaItemBlocking.module(this)
        VanillaOreNames.module(this)
        Ae2SpatialIoBlocking.module(this)
    }
}
