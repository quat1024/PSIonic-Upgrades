package wiresegal.psionup.common.spell

import vazkii.psi.api.PsiAPI
import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces
import wiresegal.psionup.common.lib.LibNames
import wiresegal.psionup.common.spell.trick.botania.PieceTrickFormBurst
import wiresegal.psionup.common.spell.trick.botania.PieceTrickBotaniaDrum

/**
 * @author WireSegal
 * Created at 9:01 AM on 4/1/16.
 */
object CompatTricksKt {
    var isInitialized = false

    lateinit var makeBurst: ModSpellPieces.PieceContainer
    lateinit var wildDrum: ModSpellPieces.PieceContainer
    lateinit var canopyDrum: ModSpellPieces.PieceContainer
    lateinit var gatheringDrum: ModSpellPieces.PieceContainer

    fun init() {
        isInitialized = true

        PsiAPI.setGroupRequirements(LibNames.PieceGroups.MANA_PSIONICS, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS)

        makeBurst = ModPieces.register(PieceTrickFormBurst::class.java, LibNames.Spell.MAKE_BURST, LibNames.PieceGroups.MANA_PSIONICS, true)

        wildDrum = ModPieces.register(PieceTrickBotaniaDrum::class.java, LibNames.Spell.WILD_DRUM, LibNames.PieceGroups.MANA_PSIONICS)
        canopyDrum = ModPieces.register(PieceTrickBotaniaDrum.Companion.PieceTrickLeafDrum::class.java, LibNames.Spell.CANOPY_DRUM, LibNames.PieceGroups.MANA_PSIONICS)
        gatheringDrum = ModPieces.register(PieceTrickBotaniaDrum.Companion.PieceTrickShearDrum::class.java, LibNames.Spell.GATHERING_DRUM, LibNames.PieceGroups.MANA_PSIONICS)
    }
}
