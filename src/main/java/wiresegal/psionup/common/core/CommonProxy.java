package wiresegal.psionup.common.core;

import net.minecraftforge.fml.common.event.*;
import wiresegal.psionup.common.effect.ModPotions;
import wiresegal.psionup.common.entity.ModEntities;
import wiresegal.psionup.common.network.PsionicPacketHandler;
import wiresegal.psionup.common.spell.ModPieces;

public class CommonProxy {
	public void pre(FMLPreInitializationEvent e) {
		ConfigHandler.initConfig(e.getSuggestedConfigurationFile());
		
		ModPotions.init();
		ModPieces.init();
		
		PsionicPacketHandler.initPackets();
		
		//internal property comparator thingie
		/*
		PsionicAPI.setInternalPropertyComparator { (properties, side) ->
            if (side.axis == EnumFacing.Axis.Y) {
                EnumFacing.HORIZONTALS.map {
                    PsionicMethodHandles.calculateInputStrength(properties.world, properties.pos, it)
                }.max() ?: 0
            } else
                PsionicMethodHandles.calculateInputStrength(properties.world, properties.pos, side)
        }
		 */
	}
	
	public void init(FMLInitializationEvent e) {
		//ModRecipes.init(); //TODO Json Recipes
		ModEntities.createWatchers();
	}
	
	public void post(FMLPostInitializationEvent e) {
		//No-op
	}
}
