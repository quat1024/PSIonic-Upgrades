package wiresegal.psionup.common.crafting.factory;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import wiresegal.psionup.common.core.ConfigHandler;

import java.util.function.BooleanSupplier;

public class InlineCasterConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json, "value", true);
		return () -> ConfigHandler.enableInline == value;
	}
}
