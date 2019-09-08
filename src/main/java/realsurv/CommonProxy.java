package realsurv;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import realsurv.items.ItemTablet;

public class CommonProxy {
	public static ItemTablet tablet;
	
	public void preInit(FMLPreInitializationEvent e) {
		tablet = (ItemTablet) new ItemTablet().setUnlocalizedName("tablet").setRegistryName("tablet");
		ForgeRegistries.ITEMS.register(tablet);
	}
	
	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
}
