package wiresegal.psionup.common.lib;

public class LibMisc {
	public static final String MOD_NAME = "PSIonic Upgrades";
    public static final String MOD_ID = "psionup";

    public static final String BUILD = "GRADLE:BUILD";
    public static final String VERSIONID = "GRADLE:VERSION";
    public static final String VERSION = VERSIONID + BUILD;
    public static final String DEPENDENCIES = "required-after:forge@[GRADLE:FORGE_VERSION,);required-after:psi;after:botania;";

    public static final String VERSIONS = "GRADLE:MC_VERSION";

    public static final String PROXY_COMMON = "wiresegal.psionup.common.core.CommonProxy";
    public static final String PROXY_CLIENT = "wiresegal.psionup.client.core.ClientProxy";
}
