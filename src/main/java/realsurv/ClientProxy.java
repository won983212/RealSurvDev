package realsurv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import won983212.guitoolkit.font.FontFactory;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		MinecraftForge.EVENT_BUS.register(ClientEventHandler.instance);
		ModelResourceLocation loc = new ModelResourceLocation("realsurv:tablet", "inventory");
		ModelLoader.setCustomModelResourceLocation(CommonProxy.tablet, 0, loc);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		Minecraft.getMinecraft().fontRenderer = FontFactory.makeMinecraftFont("¸¼Àº °íµñ", 14);
		ClientEventHandler.initTabletOS();
	}
}
