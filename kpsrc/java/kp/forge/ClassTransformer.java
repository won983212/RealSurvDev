package kp.forge;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import kp.KoreanPatch;
import kp.utils.Log;
import kp.wrapper.AdvancedTransformer;
import kp.wrapper.FontRendererWrapper;
import kp.wrapper.GuiChatWrapper;
import kp.wrapper.GuiTextfieldWrapper;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer
{
	private HashMap<String, AdvancedTransformer> transformers = new HashMap<String, AdvancedTransformer>();
	private String[] patchClasses = new String[] { "net/minecraft/client/gui/GuiScreenBook", "net/minecraft/client/gui/inventory/GuiEditSign", "net/minecraft/client/gui/GuiTextField" };

	public ClassTransformer()
	{
		registerTransformer("net/minecraft/client/gui/GuiChat", new GuiChatWrapper.Transformer());
		//registerTransformer("net/minecraft/client/gui/GuiTextField", new GuiTextfieldWrapper.Transformer());
		registerTransformer("net/minecraft/client/gui/FontRenderer", new FontRendererWrapper.Transformer());
	}

	private void registerTransformer(String clsName, AdvancedTransformer transformer)
	{
		if (KoreanPatch.USE_DEOBFUSCATE)
		{
			clsName = AdvancedTransformer.getDeobfuscated(clsName);
		}

		transformers.put(clsName.replaceAll("/", "."), transformer);
	}

	private boolean isPatchClasses(String name)
	{
		for (String classname : patchClasses)
		{
			if (KoreanPatch.USE_DEOBFUSCATE)
			{
				classname = AdvancedTransformer.getDeobfuscated(classname);
			}

			if (classname.equals(name))
				return true;
		}

		return false;
	}

	public byte[] transform(String arg0, String arg1, byte[] arg2)
	{
		AdvancedTransformer t = transformers.get(arg0);

		if (t != null)
		{
			Log.i(arg0 + " patch start!");

			try
			{
				ClassNode cls = new ClassNode();
				ClassReader reader = new ClassReader(arg2);
				reader.accept(cls, 0);

				t.transform(cls);

				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
				cls.accept(writer);

				Log.i(arg0 + " is patched!");

				if (KoreanPatch.USE_TRANSFORMED_CLASS_SAVE)
				{
					try
					{
						File file = new File(KoreanPatch.CLASS_LOG_PATH, arg0 + ".class");
						if (!file.getParentFile().exists())
						{
							file.getParentFile().mkdirs();
						}

						FileOutputStream fos = new FileOutputStream(file);
						fos.write(writer.toByteArray());
						fos.close();
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				return writer.toByteArray();
			}
			catch (Exception e)
			{
				Log.error(arg0 + " patch failed.");
				e.printStackTrace();
			}
		}
		else if (isPatchClasses(arg0))
		{
			return patching(arg0, arg1, arg2, KoreanPatchPlugin.modFile);
		}

		return arg2;
	}

	private byte[] patching(String rebname, String name, byte[] data, File f)
	{
		try
		{
			ZipFile zip = new ZipFile(f);
			ZipEntry e = zip.getEntry(rebname.replace('.', '/') + ".class");

			if (e == null)
			{
				e = zip.getEntry(name.replace('.', '/') + ".class");
				if (e == null)
				{
					Log.error("Not found class: " + name + "(" + rebname + ")");
					zip.close();
					return data;
				}
			}

			InputStream is = zip.getInputStream(e);
			data = rwAll(is);
			Log.i(rebname + " Patch complete!");

			is.close();
			zip.close();
		}
		catch (ZipException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}

	private byte[] rwAll(InputStream is) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[256];
		int len = -1;

		while ((len = is.read(buffer)) != -1)
		{
			bos.write(buffer, 0, len);
		}

		return bos.toByteArray();
	}
}
