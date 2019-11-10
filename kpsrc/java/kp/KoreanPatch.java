package kp;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import kp.forge.MainContainer;

public class KoreanPatch
{
	public static final String ICON_URL = "textures/gui/kpicons.png";
	public static final String BLOG_URL = "http://blog.naver.com/won983212";
	public static final String VERSION = "b84_1121f";
	public static final boolean USE_DEOBFUSCATE = true;
	public static final boolean USE_TRANSFORMED_CLASS_SAVE = false;
	public static final String CLASS_LOG_PATH = System.getProperty("user.home");

	public static void init() throws IOException
	{
		MainContainer.showTutorial = !Config.loadConfig();
	}

	public static void openURL(String url)
	{
		try
		{
			Desktop.getDesktop().browse(URI.create(url));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
