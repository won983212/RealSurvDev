package kp.wrapper;

import static org.objectweb.asm.Opcodes.*;

import java.util.Iterator;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import kp.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.betterfonts.StringCache;
import net.minecraft.util.ResourceLocation;

public class FontRendererWrapper
{
	private PrivateMethodAccessor fontrenderer;

	public FontRendererWrapper(FontRenderer fr)
	{
		this.fontrenderer = (PrivateMethodAccessor) fr;
	}

	public void init(ResourceLocation location, TextureManager textureManagerIn, int[] colorCode)
	{
		if (location.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && textureManagerIn != null && fontrenderer.getStringCache() == null)
		{
			fontrenderer.setStringCache(new StringCache(colorCode));
		}
	}

	public boolean isUseFont()
	{
		return Config.getBool(Config.USE_FONT) && fontrenderer.getStringCache() != null;
	}

	public static interface PrivateMethodAccessor
	{
		public StringCache getStringCache();

		public void setStringCache(StringCache arg);

		public void setShadowMode(boolean mode);
	}

	public static class Transformer extends AdvancedTransformer
	{
		@Override
		public void transform(ClassNode cls)
		{
			InsnList insns = new InsnList();

			cls.interfaces.add("kp/wrapper/FontRendererWrapper$PrivateMethodAccessor");
			cls.fields.add(new FieldNode(ACC_PRIVATE, "wrapper", "Lkp/wrapper/FontRendererWrapper;", null, null));
			cls.fields.add(new FieldNode(ACC_PRIVATE, "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;", null, null));
			cls.fields.add(new FieldNode(ACC_PRIVATE, "dropShadowEnabled", "Z", null, true));

			patchGetter(cls, "getStringCache", "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;", ARETURN);
			patchSetter(cls, "setStringCache", "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;");
			patchSetter(cls, "setShadowMode", "dropShadowEnabled", "Z");

			String gamesetting = getDeobfuscated("net/minecraft/client/settings/GameSettings");
			String resourcelocation = getDeobfuscated("net/minecraft/util/ResourceLocation");
			String texturemanager = getDeobfuscated("net/minecraft/client/renderer/texture/TextureManager");
			String fontRenderer = getDeobfuscated("net/minecraft/client/gui/FontRenderer");

			// init 'wrapper' & call wrapper.init() patch
			MethodNode method = getMethodByMcpName(cls, "<init>", "(L" + gamesetting + ";L" + resourcelocation + ";L" + texturemanager + ";Z)V");
			Iterator<AbstractInsnNode> iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof MethodInsnNode)
				{
					if (isMatchedMethod((MethodInsnNode) node, getDeobfuscated("readGlyphSizes"), "()V"))
					{
						insns.clear();
						insns.add(buildNewInstance_Wrapper(cls, "kp/wrapper/FontRendererWrapper"));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "wrapper", "Lkp/wrapper/FontRendererWrapper;"));
						insns.add(new VarInsnNode(ALOAD, 2));
						insns.add(new VarInsnNode(ALOAD, 3));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, fontRenderer, getDeobfuscated("colorCode"), "[I"));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/FontRendererWrapper", "init", "(L" + resourcelocation + ";L" + texturemanager + ";[I)V", false));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new InsnNode(ICONST_1));
						insns.add(new FieldInsnNode(PUTFIELD, fontRenderer, "dropShadowEnabled", "Z"));
						method.instructions.insert(node, insns);

						break;
					}
				}
			}

			// append '&& dropShadowEnabled'
			method = getMethodByMcpName(cls, "drawString", "(Ljava/lang/String;FFIZ)I");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof VarInsnNode)
				{
					VarInsnNode varNode = (VarInsnNode) node;
					if (varNode.getOpcode() == ILOAD && varNode.var == 5)
					{
						JumpInsnNode jumpNode = (JumpInsnNode) iter.next();

						insns.clear();
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, cls.name, "dropShadowEnabled", "Z"));
						insns.add(new JumpInsnNode(IFEQ, jumpNode.label));
						method.instructions.insert(jumpNode, insns);

						break;
					}
				}
			}

			// renderString patch
			method = getMethodByMcpName(cls, "renderString", "(Ljava/lang/String;FFIZ)I");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof MethodInsnNode)
				{
					MethodInsnNode methodNode = (MethodInsnNode) node;
					if (isMatchedMethod((MethodInsnNode) node, getDeobfuscated("renderStringAtPos"), "(Ljava/lang/String;Z)V"))
					{
						AbstractInsnNode firstNode = methodNode.getPrevious().getPrevious().getPrevious();
						LabelNode end = (LabelNode) methodNode.getNext();

						Label label = new Label();
						LabelNode labelNode = new LabelNode(label);
						method.instructions.insertBefore(firstNode, labelNode);

						insns.clear();
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "wrapper", "Lkp/wrapper/FontRendererWrapper;"));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/FontRendererWrapper", "isUseFont", "()Z", false));
						insns.add(new JumpInsnNode(IFEQ, labelNode));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new InsnNode(DUP));
						insns.add(new FieldInsnNode(GETFIELD, fontRenderer, getDeobfuscated("posX"), "F"));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;"));
						insns.add(new VarInsnNode(ALOAD, 1));
						insns.add(new VarInsnNode(FLOAD, 2));
						insns.add(new VarInsnNode(FLOAD, 3));
						insns.add(new VarInsnNode(ILOAD, 4));
						insns.add(new VarInsnNode(ILOAD, 5));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/src/betterfonts/StringCache", "renderString", "(Ljava/lang/String;FFIZ)I", false));
						insns.add(new InsnNode(I2F));
						insns.add(new InsnNode(FADD));
						insns.add(new FieldInsnNode(PUTFIELD, fontRenderer, getDeobfuscated("posX"), "F"));
						insns.add(new JumpInsnNode(GOTO, end));
						insns.add(new FrameNode(F_SAME, 0, null, 0, null));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new VarInsnNode(ALOAD, 1));
						insns.add(new VarInsnNode(ILOAD, 5));
						insns.add(new MethodInsnNode(INVOKESPECIAL, fontRenderer, getDeobfuscated("renderStringAtPos"), "(Ljava/lang/String;Z)V", false));

						method.instructions.insertBefore(labelNode, insns);
					}
				}
			}

			// if(wrapper.isUseFont()){return text;}
			method = getMethodByMcpName(cls, "bidiReorder", "(Ljava/lang/String;)Ljava/lang/String;");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 1));
			method.instructions.insert(buildWrapperCaller(insns, method, ARETURN));

			// if(wrapper.isUseFont()){return stringcache.getStringWidth();}
			method = getMethodByMcpName(cls, "getStringWidth", "(Ljava/lang/String;)I");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;"));
			insns.add(new VarInsnNode(ALOAD, 1));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/src/betterfonts/StringCache", "getStringWidth", "(Ljava/lang/String;)I", false));
			method.instructions.insert(buildWrapperCaller(insns, method, IRETURN));

			// if(wrapper.isUseFont()){return stringcache.trimStringToWidth();}
			method = getMethodByMcpName(cls, "trimStringToWidth", "(Ljava/lang/String;IZ)Ljava/lang/String;");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;"));
			insns.add(new VarInsnNode(ALOAD, 1));
			insns.add(new VarInsnNode(ILOAD, 2));
			insns.add(new VarInsnNode(ILOAD, 3));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/src/betterfonts/StringCache", "trimStringToWidth", "(Ljava/lang/String;IZ)Ljava/lang/String;", false));
			method.instructions.insert(buildWrapperCaller(insns, method, ARETURN));

			// if(wrapper.isUseFont()){return stringcache.sizeStringToWidth();}
			method = getMethodByMcpName(cls, "sizeStringToWidth", "(Ljava/lang/String;I)I");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, fontRenderer, "stringcache", "Lnet/minecraft/src/betterfonts/StringCache;"));
			insns.add(new VarInsnNode(ALOAD, 1));
			insns.add(new VarInsnNode(ILOAD, 2));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/src/betterfonts/StringCache", "sizeStringToWidth", "(Ljava/lang/String;I)I", false));
			method.instructions.insert(buildWrapperCaller(insns, method, IRETURN));
		}

		private InsnList buildWrapperCaller(InsnList appended, MethodNode method, int rettype)
		{
			InsnList insns = new InsnList();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, getDeobfuscated("net/minecraft/client/gui/FontRenderer"), "wrapper", "Lkp/wrapper/FontRendererWrapper;"));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/FontRendererWrapper", "isUseFont", "()Z", false));
			insns.add(new JumpInsnNode(IFEQ, (LabelNode) method.instructions.get(0)));
			if (appended != null)
			{
				insns.add(appended);
			}
			insns.add(new InsnNode(rettype));

			return insns;
		}
	}
}
