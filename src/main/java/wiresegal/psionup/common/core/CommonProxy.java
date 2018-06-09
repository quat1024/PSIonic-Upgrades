package wiresegal.psionup.common.core;

import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	public void pre(FMLPreInitializationEvent e) {
		//TODO Liblib config stuff here
		
		//original used a bunch of kt initializer blocks
		//which i have transcribed as "init()"
		//ModPotions.init(); //TODO
		//ModItems.init(); //TODO move to registry event
		//ModBlocks.init(); //TODO move to registry event
		//ModPieces.init();
		//ModEntities.init(); //TODO entity entry instead?
		
		//FlowColors.EventHandler.init(); //TODO eventbussubscriber?
		
		//internal property comparator thingie
		//TODO unravel wire's crazy block layout
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
	}
	
	public void post(FMLPostInitializationEvent e) {
		//No-op
	}
}
