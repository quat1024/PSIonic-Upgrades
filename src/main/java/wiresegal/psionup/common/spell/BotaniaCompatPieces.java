package wiresegal.psionup.common.spell;

import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.spell.trick.botania.PieceTrickBotaniaDrum;

public class BotaniaCompatPieces {
	static void init() {
		ModPieces.register(PieceTrickBotaniaDrum.DootGrass.class, LibNames.Spell.WILD_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.DootLeaves.class, LibNames.Spell.CANOPY_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.DootSnow.class, LibNames.Spell.COVERING_HORN, LibNames.PieceGroups.MANA_PSIONICS);
		ModPieces.register(PieceTrickBotaniaDrum.ShearDrum.class, LibNames.Spell.GATHERING_DRUM, LibNames.PieceGroups.MANA_PSIONICS);
	}
}
