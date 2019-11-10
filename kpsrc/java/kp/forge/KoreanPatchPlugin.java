package kp.forge;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class KoreanPatchPlugin implements IFMLLoadingPlugin
{
	public static File modFile;

	public String[] getASMTransformerClass()
	{
		System.out.println("ASM");
		return new String[] { ClassTransformer.class.getName() };
	}

	public String getModContainerClass()
	{
		return MainContainer.class.getName();
	}

	public String getSetupClass()
	{
		return null;
	}

	public void injectData(Map<String, Object> data)
	{
		modFile = (File) data.get("coremodLocation");
	}

	public String getAccessTransformerClass()
	{
		return null;
	}
}
