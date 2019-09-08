package realsurv;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = RealSurvMod.MODID, name = RealSurvMod.NAME, version = RealSurvMod.VERSION)
public class RealSurvMod {
	public static final String MODID = "realsurvmod";
	public static final String NAME = "realsurvmod";
	public static final String VERSION = "0.1";

	@Mod.Instance(RealSurvMod.MODID)
	public static RealSurvMod instance;

	@SidedProxy(clientSide = "realsurv.ClientProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
