package wiresegal.psionup.common.spell;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibPieceGroups;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.spell.trick.botania.PieceTrickBotaniaDrum;
import wiresegal.psionup.common.spell.trick.botania.PieceTrickFormBurst;

public class BotaniaCompatPieces {
	static void init() {
		PsiAPI.setGroupRequirements(LibNames.PieceGroups.MANA_PSIONICS, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS);
		
		ModPieces.register(PieceTrickBotaniaDrum.DootGrass.class, LibNames.Spell.WILD_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.DootLeaves.class, LibNames.Spell.CANOPY_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.DootSnow.class, LibNames.Spell.COVERING_HORN, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.ShearDrum.class, LibNames.Spell.GATHERING_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
		
		ModPieces.register(PieceTrickFormBurst.class, LibNames.Spell.MAKE_BURST, LibNames.PieceGroups.MANA_PSIONICS, true);
	}
}
