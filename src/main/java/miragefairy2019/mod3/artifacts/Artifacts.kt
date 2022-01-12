package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.mod3.fairy.BakedFairy

object Artifacts {
    val module: Module = {
        Dish.module(this)
        Fertilizer.module(this)
        TwinkleStone.module(this)
        FairyCollectionBox.module(this)
        FairyWoodLog.module(this)
        FairyBox.module(this)
        FairyResinTapper.module(this)
        BakedFairy.module(this)
    }
}
