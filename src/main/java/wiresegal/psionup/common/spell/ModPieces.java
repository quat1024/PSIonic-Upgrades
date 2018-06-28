package wiresegal.psionup.common.spell;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.spell.operator.*;
import wiresegal.psionup.common.spell.operator.block.*;
import wiresegal.psionup.common.spell.trick.*;

public class ModPieces {
	public static void init() {
		PsiAPI.setGroupRequirements(LibNames.PieceGroups.ALTERNATE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION);
		PsiAPI.setGroupRequirements(LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY);
		PsiAPI.setGroupRequirements(LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY);
		PsiAPI.setGroupRequirements(LibNames.PieceGroups.BLOCK_PROPERTIES, 21, LibPieceGroups.BLOCK_CONJURATION);
		
		register(PieceTrickConjurePulsar.class,
						LibNames.Spell.CONJURE_PULSAR,
						LibNames.PieceGroups.ALTERNATE_CONJURATION,
						true);
		register(PieceTrickConjurePulsarSequence.class,
						LibNames.Spell.CONJURE_PULSAR_SEQUENCE,
						LibNames.PieceGroups.ALTERNATE_CONJURATION);
		register(PieceTrickConjurePulsarLight.class,
						LibNames.Spell.CONJURE_PULSAR_LIGHT,
						LibNames.PieceGroups.ALTERNATE_CONJURATION);
		
		register(PieceOperatorPlanarNorm.class,
						LibNames.Spell.PLANAR_NORMAL_VECTOR,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS,
						true);
		register(PieceOperatorVectorRotate.class,
						LibNames.Spell.VECTOR_ROTATE,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS);
		
		register(PieceOperatorVectorStrongRaycast.class,
						LibNames.Spell.STRONG_VECTOR_RAYCAST,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS);
		register(PieceOperatorVectorStrongRaycastAxis.class,
						LibNames.Spell.STRONG_VECTOR_RAYCAST_AXIS,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS);
		
		register(PieceOperatorVectorFallback.class,
						LibNames.Spell.VECTOR_FALLBACK,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS);
		
		register(PieceTrickParticleTrail.class,
						LibNames.Spell.PARTICLE_TRAIL,
						LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS);
		
		register(PieceTrickConjureStar.class,
						LibNames.Spell.CONJURE_CRACKLE,
						LibNames.PieceGroups.ALTERNATE_CONJURATION);
		
		register(PieceTrickBreakLoop.class,
						LibNames.Spell.BREAK_LOOP,
						LibPieceGroups.FLOW_CONTROL);
		
		register(PieceOperatorListSize.class,
						LibNames.Spell.LIST_SIZE,
						LibPieceGroups.ENTITIES_INTRO);
		
		register(PieceOperatorGetBlockProperties.class,
						LibNames.Spell.GET_PROPERTIES,
						LibNames.PieceGroups.BLOCK_PROPERTIES,
						true);
		
		register(PieceOperatorGetBlockHardness.class,
						LibNames.Spell.GET_HARDNESS,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceOperatorGetBlockLight.class,
						LibNames.Spell.GET_LIGHT,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceOperatorGetBlockSolidity.class,
						LibNames.Spell.GET_SOLIDITY,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceOperatorGetBlockComparatorStrength.class,
						LibNames.Spell.GET_COMPARATOR,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceOperatorEquality.class,
						LibNames.Spell.EQUALITY,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		
		register(PieceTrickBreakBox.class,
						LibNames.Spell.BREAK_BOX,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceTrickCloneBox.class,
						LibNames.Spell.CLONE_BOX,
						LibNames.PieceGroups.BLOCK_PROPERTIES);
		
		register(PieceTrickDebugSpamless.class,
						LibNames.Spell.SPAMLESS,
						LibPieceGroups.TUTORIAL_1);
		
		if(Loader.isModLoaded("botania")) {
			BotaniaCompatPieces.init();
		}
	}
	
	static void register(Class<? extends SpellPiece> pieceClass, String name, String group) {
		register(pieceClass, name, group, false);
	}
	
	static void register(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
		//PsiAPI.registerSpellPieceAndTexture(LibMisc.MOD_ID + "." + name, pieceClass);
		String key = LibMisc.MOD_ID + "." + name;
		PsiAPI.registerSpellPiece(key, pieceClass);
		PsiAPI.simpleSpellTextures.put(key, new ResourceLocation(LibMisc.MOD_ID, "textures/spell/" + name + ".png"));
		PsiAPI.addPieceToGroup(pieceClass, group, main);
		//No return value. TODO does the original actually use the return values?
	}
	
	//TODO: PsiAPI seems to now include a registerSpellPieceAndTexture.
	//Does the original's reimplementation add anything useful, or is it cruft?
}
