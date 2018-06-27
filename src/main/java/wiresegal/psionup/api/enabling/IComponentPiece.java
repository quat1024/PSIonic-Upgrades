package wiresegal.psionup.api.enabling;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author WireSegal
 *         Created at 1:40 PM on 4/4/16.
 *
 * Most mods shouldn't need to use this, except for detection.
 * The abstract classes prodvided in this package should be sufficient.
 */
public interface IComponentPiece {
    static
    @Nullable
    ItemStack firstMatchingPiece(ItemStack cad, Predicate<ItemStack> pred) {
        if (!(cad.getItem() instanceof ICAD))
            return null;
        ICAD cadItem = (ICAD) cad.getItem();
        for (EnumCADComponent component : EnumCADComponent.values()) {
            ItemStack compStack = cadItem.getComponentInSlot(cad, component);
            if (compStack != null) {
                if (pred.test(compStack)) return compStack;
            }
        }
        return null;
    }

    static
    @Nullable
    Object execute(SpellPiece piece, SpellContext context) throws SpellRuntimeException {
        if (!(piece instanceof IComponentPiece))
            return null;
        IComponentPiece componentPiece = (IComponentPiece) piece;

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad.isEmpty() || !(cad.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD item = (ICAD) cad.getItem();

        boolean flag = false;

        for (EnumCADComponent type : EnumCADComponent.values()) {
            ItemStack component = item.getComponentInSlot(cad, type);

            if (component != null && component.getItem() instanceof ICADComponent) {
                ICADComponent compItem = (ICADComponent) component.getItem();
                if (compItem.getCADStatValue(component, EnumCADStat.POTENCY) < 0)
                    return componentPiece.executeIfAllowed(context);

                ITrickEnablerComponent.EnableResult result = componentPiece.acceptsPiece(component, cad, context, piece.spell, piece.x, piece.y);
                switch (result) {
                    case MISSING_REQUIREMENT:
                        flag = true;
                        break;
                    case SUCCESS:
                        return componentPiece.executeIfAllowed(context);
                    case NOT_ENABLED:
                        if (component.getItem() instanceof ITrickEnablerComponent) {
                            ITrickEnablerComponent compEnablerItem = (ITrickEnablerComponent) component.getItem();
                            result = compEnablerItem.enablePiece(context.caster, component, cad, context, piece.spell, piece.x, piece.y);
                            switch (result) {
                                case MISSING_REQUIREMENT:
                                    flag = true;
                                    break;
                                case SUCCESS:
                                    return componentPiece.executeIfAllowed(context);
                            }
                        }
                        break;
                }
            }
        }

        if (flag)
            throw new SpellRuntimeException("psionup.spellerror.trickdisabled");
        throw new SpellRuntimeException("psionup.spellerror.tricknotenabled");
    }

    static void addToTooltip(SpellPiece piece, List<String> tooltip) {
        if (!(piece instanceof IComponentPiece))
            return;
        IComponentPiece componentPiece = (IComponentPiece) piece;

        TooltipHelper.addToTooltip(tooltip, TextFormatting.GRAY + "%s", piece.getUnlocalizedDesc());

        for (String obj : componentPiece.requiredObjects())
            TooltipHelper.addToTooltip(tooltip, "psionup.spelldesc.requires", obj);

        TooltipHelper.addToTooltip(tooltip, "");
        String eval = piece.getEvaluationTypeString();
        TooltipHelper.addToTooltip(tooltip, "<- " + TextFormatting.GOLD + eval);

        for (SpellParam param : piece.paramSides.keySet()) {
            String pName = I18n.format(param.name);
            String pEval = param.getRequiredTypeString();
            TooltipHelper.addToTooltip(tooltip, (param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
        }

    }

    Object executeIfAllowed(SpellContext context) throws SpellRuntimeException;

    String[] requiredObjects();

    default ITrickEnablerComponent.EnableResult acceptsPiece(ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
        return ITrickEnablerComponent.EnableResult.NOT_ENABLED;
    }
}
