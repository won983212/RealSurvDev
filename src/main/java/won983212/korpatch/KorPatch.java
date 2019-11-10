package won983212.korpatch;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = KorPatch.MODID, name = KorPatch.NAME, version = KorPatch.VERSION)
public class KorPatch {
	public static final String MODID = "lootkorpatch";
	public static final String NAME = "Loot's korean patch";
	public static final String VERSION = "191110_1122";
	
	@Mod.Instance(MODID)
	public static KorPatch instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(ClientSideEventHandler.instance);
	}
}
