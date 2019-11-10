package kp.wrapper;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import kp.Config;
import kp.gui.screen.GuiConfig;
import kp.inputs.KoreanInput;
import kp.utils.IconLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

public class GuiChatWrapper
{
	private double acceledValue = 2.0D;
	private GuiChat gui;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiChatWrapper(GuiChat ui)
	{
		this.gui = ui;
	}

	public void initGui(GuiTextField inputField)
	{
		GuiTextfieldWrapper.PrivateMethodAccessor textfield = inputField;
		
		textfield.getHanjaInput().setXY(2, gui.height);
		textfield.getHanjaInput().setSize(gui.width, gui.height);
		textfield.getColorInput().setXY(4, gui.height - 14);
	}

	public boolean keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == Config.getKey(Config.KEY_OPTION).getKeyCode())
		{
			mc.displayGuiScreen(new GuiConfig());
			return true;
		}

		return false;
	}

	public void mouseClicked(int mouseX, int mouseY)
	{
		if (mouseX < 25 && mouseY < 10)
		{
			mc.displayGuiScreen(new GuiConfig());
		}
	}

	public String applyPrefixSuffix(String s)
	{
		if (Config.getBool(Config.USE_PREFIX_OR_SUFFIX))
		{
			if (Config.getBool(Config.PREFIX_ON_SLASH) && s.startsWith("/"))
			{
				s = "/" + Config.getString(Config.PREFIX) + s.substring(1) + Config.getString(Config.SUFFIX);
			}
			else
			{
				s = Config.getString(Config.PREFIX) + s + Config.getString(Config.SUFFIX);
			}
		}

		return s;
	}

	public void drawScreen(GuiTextField inputField, int mouseX, int mouseY)
	{
		FontRenderer fr = mc.fontRenderer;

		if (Config.getBool(Config.SHOW_OPTION_KEY))
		{
			fr.drawString(": " + Keyboard.getKeyName(Config.getKey(Config.KEY_OPTION).getKeyCode()), 24, 0, -1);
			GlStateManager.scale(0.5, 0.5, 0.5);
			mc.getTextureManager().bindTexture(IconLoader.TEXTURE_GUI);
			gui.drawTexturedModalRect(2, 2, 1, 53, 40, 15);
			GlStateManager.scale(2, 2, 2);
		}

		if (Config.getBool(Config.USE_TARNSLATE) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			ITextComponent comp = mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
			if (comp != null)
			{
				ArrayList<String> lines = new ArrayList<String>();
				lines.add(KoreanInput.getSharedInstance().make(comp.getUnformattedText()));
				((PrivateMethodAccessor) gui).drawHoveringTextAccessor(lines, mouseX, mouseY);
			}
		}

		if (Config.getBool(Config.USE_INFO_PANEL))
		{
			String s = (KoreanInput.isKor() ? "§6한글" : "§a영문") + (Config.getBool(Config.SHOW_CHAT_LENGTH) ? ("§r[" + String.valueOf(inputField.getText().length()) + "]") : "");
			int x = 2;
			int w = fr.getStringWidth(s);

			if (inputField.getText().length() >= inputField.getCursorPosition())
			{
				x += fr.getStringWidth(inputField.getText().substring(0, inputField.getCursorPosition()));
			}

			if (Config.getBool(Config.USE_FOLLOWING_INFO_PANEL))
			{
				if (x + w > gui.width)
				{
					x = gui.width - w - 2;
				}
				this.acceledValue += (x - this.acceledValue) * 0.1D;
			}

			gui.drawRect((int) this.acceledValue, gui.height - 25, (int) this.acceledValue + w + 2, gui.height - 15, Config.getInt(Config.COLOR_CHAT_BACK));
			fr.drawString(s, (int) this.acceledValue + 1, gui.height - 24, -1);
		}
	}

	public static interface PrivateMethodAccessor
	{
		public void drawHoveringTextAccessor(List<String> lines, int x, int y);
	}

	public static class Transformer extends AdvancedTransformer
	{
		@Override
		public void transform(ClassNode cls)
		{
			InsnList insns = new InsnList();

			cls.interfaces.add("kp/wrapper/GuiChatWrapper$PrivateMethodAccessor");
			cls.fields.add(new FieldNode(ACC_PUBLIC, "wrapper", "Lkp/wrapper/GuiChatWrapper;", null, null));

			String guichat = getDeobfuscated("net/minecraft/client/gui/GuiChat");
			String guitextfield = getDeobfuscated("net/minecraft/client/gui/GuiTextField");

			// interface 'drawHoveringTextAccessor' patch
			MethodVisitor mv = cls.visitMethod(ACC_PUBLIC, "drawHoveringTextAccessor", "(Ljava/util/List;II)V", "(Ljava/util/List<Ljava/lang/String;>;II)V", null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, guichat, getDeobfuscated("drawHoveringText"), "(Ljava/util/List;II)V", false);
			mv.visitInsn(RETURN);
			mv.visitEnd();

			// init 'wrapper' patch
			MethodNode method = getMethodByMcpName(cls, "<init>", "(Ljava/lang/String;)V");
			Iterator<AbstractInsnNode> iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof FieldInsnNode)
				{
					if (isMatchedField((FieldInsnNode) node, guichat, getDeobfuscated("defaultInputFieldText"), "Ljava/lang/String;"))
					{
						method.instructions.insert(node, buildNewInstance_Wrapper(cls, "kp/wrapper/GuiChatWrapper"));
						break;
					}
				}
			}

			method = getMethodByMcpName(cls, "<init>", "()V");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof FieldInsnNode)
				{
					if (isMatchedField((FieldInsnNode) node, guichat, getDeobfuscated("defaultInputFieldText"), "Ljava/lang/String;"))
					{
						method.instructions.insert(node, buildNewInstance_Wrapper(cls, "kp/wrapper/GuiChatWrapper"));
						break;
					}
				}
			}

			// wrapper.initGui(inputField);
			method = getMethodByMcpName(cls, "initGui", "()V");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof FieldInsnNode)
				{
					if (isMatchedField((FieldInsnNode) node, guichat, getDeobfuscated("tabCompleter"), "L" + getDeobfuscated("net/minecraft/util/TabCompleter") + ";"))
					{
						insns.clear();
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guichat, "wrapper", "Lkp/wrapper/GuiChatWrapper;"));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guichat, getDeobfuscated("inputField"), "L" + guitextfield + ";"));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiChatWrapper", "initGui", "(L" + guitextfield + ";)V", false));
						method.instructions.insert(node, insns);
						break;
					}
				}
			}

			// s = wrapper.applyPrefixSuffix(s);
			method = getMethodByMcpName(cls, "keyTyped", "(CI)V");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof MethodInsnNode)
				{
					if (isMatchedMethod((MethodInsnNode) node, "trim", "()Ljava/lang/String;"))
					{
						VarInsnNode astore = (VarInsnNode) iter.next();
						insns.clear();
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guichat, "wrapper", "Lkp/wrapper/GuiChatWrapper;"));
						insns.add(new VarInsnNode(ALOAD, 3));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiChatWrapper", "applyPrefixSuffix", "(Ljava/lang/String;)Ljava/lang/String;", false));
						insns.add(new VarInsnNode(ASTORE, 3));
						method.instructions.insert(astore, insns);
						break;
					}
				}
			}

			// wrapper.keyTyped(typedChar, keyCode);
			method.instructions.insert(buildWrapperCaller(null, "keyTyped", "(CI)Z"));

			// wrapper.mouseClicked(mouseX, mouseY);
			method = getMethodByMcpName(cls, "mouseClicked", "(III)V");
			method.instructions.insert(buildWrapperCaller(null, "mouseClicked", "(II)V"));

			// wrapper.drawScreen(inputField, mouseX, mouseY);
			method = getMethodByMcpName(cls, "drawScreen", "(IIF)V");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, guichat, getDeobfuscated("inputField"), "L" + guitextfield + ";"));
			method.instructions.insert(buildWrapperCaller(insns, "drawScreen", "(L" + guitextfield + ";II)V"));
		}

		private InsnList buildWrapperCaller(InsnList appended, String methodname, String desc)
		{
			InsnList insns = new InsnList();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, getDeobfuscated("net/minecraft/client/gui/GuiChat"), "wrapper", "Lkp/wrapper/GuiChatWrapper;"));
			if (appended != null)
			{
				insns.add(appended);
			}
			insns.add(new VarInsnNode(ILOAD, 1));
			insns.add(new VarInsnNode(ILOAD, 2));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiChatWrapper", methodname, desc, false));

			return insns;
		}
	}
}
