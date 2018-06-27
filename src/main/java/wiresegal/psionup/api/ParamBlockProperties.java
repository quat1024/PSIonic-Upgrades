
package wiresegal.psionup.api;

import vazkii.psi.api.spell.param.ParamSpecific;
import wiresegal.psionup.common.lib.LibMisc;

/**
 * @author WireSegal
 *         Created at 4:23 PM on 5/13/17.
 */
public class ParamBlockProperties extends ParamSpecific {
    public static final String GENERIC_NAME_MASK = LibMisc.MOD_ID + ".spellparam.mask";
    
    public ParamBlockProperties(String name, int color, boolean canDisable) {
        super(name, color, canDisable, false);
    }

    @Override
    protected Class<?> getRequiredType() {
        return BlockProperties.class;
    }
}
