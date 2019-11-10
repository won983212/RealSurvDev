package kp.forge;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import kp.KoreanPatch;
import kp.gui.screen.dialog.DialogFirstRun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainContainer extends DummyModContainer
{
	private boolean runFirst = false;
	public static boolean showTutorial = false;

	public MainContainer()
	{
		super(new ModMetadata());

		System.out.println("MODe");
		ModMetadata meta = getMetadata();
		meta.modId = "KoreanPatch";
		meta.name = "한글 패치";
		meta.version = KoreanPatch.VERSION;
		meta.credits = "룻트(won983212)";
		meta.authorList = Arrays.asList(new String[] { "won983212" });
		meta.description = "한글 입력을 돕고 몇 가지 부가 기능을 제공합니다.";
		meta.url = KoreanPatch.BLOG_URL;
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}

	@Subscribe
	public void init(FMLInitializationEvent e)
	{
		try
		{
			KoreanPatch.init();
			MinecraftForge.EVENT_BUS.register(this);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onGui(InitGuiEvent.Post e)
	{
		if (!this.runFirst && showTutorial && e.getGui() instanceof GuiMainMenu)
		{
			this.runFirst = true;
			Minecraft.getMinecraft().displayGuiScreen(new DialogFirstRun(e.getGui()));
		}
	}
}
