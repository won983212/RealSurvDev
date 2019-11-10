package kp.wrapper;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.HashMap;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import kp.KoreanPatch;
import kp.utils.Log;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public abstract class AdvancedTransformer
{
	private static final HashMap<String, String> MCP_NAME_MAP = new HashMap<String, String>();

	public abstract void transform(ClassNode cls);

	public static String getDeobfuscated(String type)
	{
		if (!KoreanPatch.USE_DEOBFUSCATE)
		{
			return type;
		}

		String deobfus = MCP_NAME_MAP.get(type);
		if (deobfus == null)
		{
			deobfus = type;
		}

		return deobfus;
	}

	/**
	 * 
	 * @param name name must be Deobfuscated name.
	 */
	protected MethodNode getMethodByMcpName(ClassNode cls, String name, String desc)
	{
		for (MethodNode method : cls.methods)
		{
			if (method.desc.equals(desc) && method.name.equals(getDeobfuscated(name))) return method;
		}

		Log.error("Can't find method \'" + name + "\' # " + desc);
		return null;
	}

	protected boolean isMatchedMethod(MethodInsnNode method, String name, String desc)
	{
		return method.name.equals(name) && method.desc.equals(desc);
	}

	protected boolean isMatchedField(FieldInsnNode field, String owner, String name, String desc)
	{
		return field.name.equals(name) && field.desc.equals(desc) && field.owner.equals(owner);
	}

	/**
	 * 생성자에서 해당 타겟 클래스의 인스턴스를 넘겨야함.
	 */
	protected InsnList buildNewInstance_Wrapper(ClassNode classNode, String wrapper)
	{
		InsnList insns = new InsnList();
		insns.add(new VarInsnNode(ALOAD, 0));
		insns.add(new TypeInsnNode(NEW, wrapper));
		insns.add(new InsnNode(DUP));
		insns.add(new VarInsnNode(ALOAD, 0));
		insns.add(new MethodInsnNode(INVOKESPECIAL, wrapper, "<init>", "(L" + classNode.name + ";)V", false));
		insns.add(new FieldInsnNode(PUTFIELD, classNode.name, "wrapper", "L" + wrapper + ";"));

		return insns;
	}

	protected void patchGetter(ClassNode classNode, String methodname, String fieldName, String fieldDesc, int returnType)
	{
		MethodVisitor m = classNode.visitMethod(ACC_PUBLIC, methodname, "()" + fieldDesc, null, null);
		m.visitCode();
		m.visitVarInsn(ALOAD, 0);
		m.visitFieldInsn(GETFIELD, classNode.name, fieldName, fieldDesc);
		m.visitInsn(returnType);
		m.visitEnd();
	}

	protected void patchSetter(ClassNode classNode, String name, String fieldName, String fieldDesc)
	{
		MethodVisitor m = classNode.visitMethod(ACC_PUBLIC, name, "(" + fieldDesc + ")V", null, null);
		m.visitCode();
		m.visitVarInsn(ALOAD, 0);
		if (fieldDesc.equals("Z")) m.visitVarInsn(ILOAD, 1);
		else m.visitVarInsn(ALOAD, 1);
		m.visitFieldInsn(PUTFIELD, classNode.name, fieldName, fieldDesc);
		m.visitInsn(RETURN);
		m.visitEnd();
	}

	static
	{
		MCP_NAME_MAP.put("net/minecraft/client/settings/GameSettings", "beu");
		MCP_NAME_MAP.put("net/minecraft/util/ResourceLocation", "kq");
		MCP_NAME_MAP.put("net/minecraft/client/renderer/texture/TextureManager", "bza");
		MCP_NAME_MAP.put("net/minecraft/client/gui/GuiChat", "bgr");
		MCP_NAME_MAP.put("net/minecraft/client/gui/GuiTextField", "bfq");
		MCP_NAME_MAP.put("net/minecraft/util/TabCompleter", "bhu");
		MCP_NAME_MAP.put("net/minecraft/client/gui/FontRenderer", "bfg");
		MCP_NAME_MAP.put("net/minecraft/client/gui/GuiScreenBook", "bij");
		MCP_NAME_MAP.put("net/minecraft/client/gui/inventory/GuiEditSign", "bjb");

		MCP_NAME_MAP.put("drawHoveringText", "a");
		MCP_NAME_MAP.put("initGui", "b");
		MCP_NAME_MAP.put("keyTyped", "a");
		MCP_NAME_MAP.put("mouseClicked", "a");
		MCP_NAME_MAP.put("drawScreen", "a");
		MCP_NAME_MAP.put("renderStringAtPos", "a");
		MCP_NAME_MAP.put("renderString", "b");
		MCP_NAME_MAP.put("drawString", "a");
		MCP_NAME_MAP.put("bidiReorder", "c");
		MCP_NAME_MAP.put("getStringWidth", "a");
		MCP_NAME_MAP.put("trimStringToWidth", "a");
		MCP_NAME_MAP.put("sizeStringToWidth", "e");
		MCP_NAME_MAP.put("drawTextBox", "g");
		MCP_NAME_MAP.put("setFocused", "b");
		MCP_NAME_MAP.put("textboxKeyTyped", "a");
		MCP_NAME_MAP.put("readGlyphSizes", "d");

		MCP_NAME_MAP.put("defaultInputFieldText", "r");
		MCP_NAME_MAP.put("inputField", "a");
		MCP_NAME_MAP.put("tabCompleter", "i");
		MCP_NAME_MAP.put("colorCode", "f");
		MCP_NAME_MAP.put("posX", "i");
		MCP_NAME_MAP.put("lineScrollOffset", "r");
		MCP_NAME_MAP.put("isFocused", "p");
	}
}
