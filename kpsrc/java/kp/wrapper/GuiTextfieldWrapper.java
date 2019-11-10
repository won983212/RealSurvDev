package kp.wrapper;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import kp.Config;
import kp.inputs.ColorChooserInput;
import kp.inputs.HanjaInput;
import kp.inputs.Inputer;
import kp.inputs.KoreanInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiTextfieldWrapper
{
	private KoreanInput kr;
	public ColorChooserInput colorin;
	public HanjaInput hanjain;
	private GuiTextField field;

	public GuiTextfieldWrapper(GuiTextField field)
	{
		this.field = field;
		init();
	}

	public void init()
	{
		this.kr = new KoreanInput();
		this.colorin = new ColorChooserInput();
		this.colorin.setXY(field.x, field.y);
		this.colorin.setWidth(field.x + field.width);
		this.colorin.setPrefix(Config.getBool(Config.USE_COLOR_MODIFIER) ? '&' : '§');
		this.hanjain = new HanjaInput();
	}

	public void cancelEditing()
	{
		kr.cancelEdit();
	}

	public void draw(int x, int y, int offset)
	{
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		kr.draw((Inputer) field, fr, field, x, y, offset);
		colorin.draw((Inputer) field, field, fr);
		hanjain.draw((Inputer) field, field, fr);
	}

	public String getText()
	{
		return field.getText();
	}

	public void setText(String text)
	{
		field.setText(text);
	}

	public int getCursorPosition()
	{
		return field.getCursorPosition();
	}

	public void setCursorPosition(int pos)
	{
		field.setCursorPosition(pos);
	}

	private boolean isFieldEnabled()
	{
		return ((PrivateMethodAccessor) field).isEnabled();
	}

	public boolean keyTyped(char typedChar, int keyCode)
	{
		// start kp
		if (field.isFocused() && this.colorin.typed((Inputer) field, typedChar, keyCode)) return true;
		if (field.isFocused() && this.hanjain.typed((Inputer) field, typedChar, keyCode)) return true;
		// end kp

		if (!field.isFocused())
		{
			return false;
		}
		else if (GuiScreen.isKeyComboCtrlA(keyCode))
		{
			field.setCursorPositionEnd();
			field.setSelectionPos(0);
			return true;
		}
		else if (GuiScreen.isKeyComboCtrlC(keyCode))
		{
			GuiScreen.setClipboardString(field.getSelectedText());
			return true;
		}
		else if (GuiScreen.isKeyComboCtrlV(keyCode))
		{
			if (isFieldEnabled())
			{
				field.writeText(GuiScreen.getClipboardString());
			}

			return true;
		}
		else if (GuiScreen.isKeyComboCtrlX(keyCode))
		{
			GuiScreen.setClipboardString(field.getSelectedText());

			if (isFieldEnabled())
			{
				field.writeText("");
			}

			return true;
		}
		else
		{
			switch (keyCode)
			{
			case 14:

				if (GuiScreen.isCtrlKeyDown())
				{
					if (isFieldEnabled())
					{
						field.deleteWords(-1);
					}
				}
				else if (isFieldEnabled() && (field.getCursorPosition() != field.getSelectionEnd())) // kp
				{
					field.deleteFromCursor(-1);
					return true;
				}

				break;
			case 199:
				if (GuiScreen.isShiftKeyDown())
				{
					field.setSelectionPos(0);
				}
				else
				{
					field.setCursorPositionZero();
				}

				return true;
			case 203:
				if (GuiScreen.isShiftKeyDown())
				{
					if (GuiScreen.isCtrlKeyDown())
					{
						field.setSelectionPos(field.getNthWordFromPos(-1, field.getSelectionEnd()));
					}
					else
					{
						field.setSelectionPos(field.getSelectionEnd() - 1);
					}
				}
				else if (GuiScreen.isCtrlKeyDown())
				{
					field.setCursorPosition(field.getNthWordFromCursor(-1));
				}
				else
				{
					field.moveCursorBy(-1);
				}

				cancelEditing();
				return true;
			case 205:
				if (GuiScreen.isShiftKeyDown())
				{
					if (GuiScreen.isCtrlKeyDown())
					{
						field.setSelectionPos(field.getNthWordFromPos(1, field.getSelectionEnd()));
					}
					else
					{
						field.setSelectionPos(field.getSelectionEnd() + 1);
					}
				}
				else if (GuiScreen.isCtrlKeyDown())
				{
					field.setCursorPosition(field.getNthWordFromCursor(1));
				}
				else
				{
					field.moveCursorBy(1);
				}

				cancelEditing();
				return true;
			case 207:

				if (GuiScreen.isShiftKeyDown())
				{
					field.setSelectionPos(field.getText().length());
				}
				else
				{
					field.setCursorPositionEnd();
				}

				return true;
			case 211:

				if (GuiScreen.isCtrlKeyDown())
				{
					if (isFieldEnabled())
					{
						field.deleteWords(1);
					}
				}
				else if (isFieldEnabled())
				{
					field.deleteFromCursor(1);
				}

				return true;
			}
		}

		if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && field.getCursorPosition() != field.getSelectionEnd())
		{
			if (isFieldEnabled())
			{
				field.writeText(Character.toString(this.kr.applyKor(typedChar)));
				if (KoreanInput.isKor())
				{
					kr.startEdit();
				}
			}
		}
		else
		{
			return kr.typed((Inputer) field, typedChar, keyCode);
		}

		return true;
	}

	public static interface PrivateMethodAccessor extends Inputer
	{
		public boolean isEnabled();

		public HanjaInput getHanjaInput();

		public ColorChooserInput getColorInput();
	}

	public static class Transformer extends AdvancedTransformer
	{
		@Override
		public void transform(ClassNode cls)
		{
			InsnList insns = new InsnList();

			cls.interfaces.add("kp/wrapper/GuiTextfieldWrapper$PrivateMethodAccessor");
			cls.fields.add(new FieldNode(ACC_PRIVATE, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;", null, null));

			String fontRenderer = getDeobfuscated("net/minecraft/client/gui/FontRenderer");
			String guitextfield = getDeobfuscated("net/minecraft/client/gui/GuiTextField");

			// init 'wrapper' patch
			MethodNode method = getMethodByMcpName(cls, "<init>", "(IL" + fontRenderer + ";IIII)V");
			Iterator<AbstractInsnNode> iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof FieldInsnNode)
				{
					if (isMatchedField((FieldInsnNode) node, guitextfield, getDeobfuscated("height"), "I"))
					{
						insns.clear();
						insns.add(buildNewInstance_Wrapper(cls, "kp/wrapper/GuiTextfieldWrapper"));
						method.instructions.insert(node, insns);

						break;
					}
				}
			}

			// 'mouseClicked # wrapper.cancelEditing();' patch
			method = getMethodByMcpName(cls, "mouseClicked", "(III)V");
			iter = method.instructions.iterator();
			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();
				if (node instanceof MethodInsnNode)
				{
					if (isMatchedMethod((MethodInsnNode) node, getDeobfuscated("setCursorPosition"), "(I)V"))
					{
						insns.clear();
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;"));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "cancelEditing", "()V", false));
						method.instructions.insert(node, insns);

						break;
					}
				}
			}

			// 'drawTextBox # wrapper.draw();' patch
			method = getMethodByMcpName(cls, "drawTextBox", "()V");
			iter = method.instructions.iterator();
			LabelNode label = new LabelNode();

			while (iter.hasNext())
			{
				AbstractInsnNode node = iter.next();

				if (node instanceof VarInsnNode)
				{
					VarInsnNode var = (VarInsnNode) node;
					if (var.var == 2 && var.getOpcode() == ILOAD)
					{
						if (iter.hasNext() && (node = iter.next()) instanceof JumpInsnNode)
						{
							JumpInsnNode jump = (JumpInsnNode) node;
							if (jump.getOpcode() == IF_ICMPEQ)
							{
								jump.label = label;
							}
						}
					}
				}

				if (node instanceof MethodInsnNode)
				{
					if (isMatchedMethod((MethodInsnNode) node, getDeobfuscated("drawCursorVertical"), "(IIII)V"))
					{
						insns.clear();
						insns.add(label);
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;"));
						insns.add(new VarInsnNode(ILOAD, 7));
						insns.add(new VarInsnNode(ILOAD, 8));
						insns.add(new VarInsnNode(ALOAD, 0));
						insns.add(new FieldInsnNode(GETFIELD, guitextfield, getDeobfuscated("lineScrollOffset"), "I"));
						insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "draw", "(III)V", false));
						method.instructions.insert(node, insns);

						break;
					}
				}
			}

			// setFocused # if (!isFocused) { wrapper.cancelEditing(); }
			method = getMethodByMcpName(cls, "setFocused", "(Z)V");
			insns.clear();
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, guitextfield, getDeobfuscated("isFocused"), "Z"));
			insns.add(new JumpInsnNode(IFNE, (LabelNode) method.instructions.get(0)));
			insns.add(new VarInsnNode(ALOAD, 0));
			insns.add(new FieldInsnNode(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;"));
			insns.add(new MethodInsnNode(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "cancelEditing", "()V", false));
			method.instructions.insert(insns);

			// textboxKeyTyped # wrapper.keyTyped();
			method = getMethodByMcpName(cls, "textboxKeyTyped", "(CI)Z");
			cls.methods.remove(method);

			MethodVisitor mv = cls.visitMethod(ACC_PUBLIC, getDeobfuscated("textboxKeyTyped"), "(CI)Z", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "keyTyped", "(CI)Z", false);
			mv.visitInsn(IRETURN);

			// getCursorPos # wrapper.getCursorPosition()
			mv = cls.visitMethod(ACC_PUBLIC, "getCursorPos", "()I", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "getCursorPosition", "()I", false);
			mv.visitInsn(IRETURN);
			mv.visitEnd();

			// setCursorPos # wrapper.setCursorPosition()
			mv = cls.visitMethod(ACC_PUBLIC, "setCursorPos", "(I)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "setCursorPosition", "(I)V", false);
			mv.visitInsn(RETURN);
			mv.visitEnd();

			// getTargetText # wrapper.getText()
			mv = cls.visitMethod(ACC_PUBLIC, "getTargetText", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "getText", "()Ljava/lang/String;", false);
			mv.visitInsn(ARETURN);
			mv.visitEnd();

			// setTargetText # wrapper.setText()
			mv = cls.visitMethod(ACC_PUBLIC, "setTargetText", "(Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "kp/wrapper/GuiTextfieldWrapper", "setText", "(Ljava/lang/String;)V", false);
			mv.visitInsn(RETURN);

			// isEnabled
			patchGetter(cls, "isEnabled", "isEnabled", "Z", IRETURN);

			// getHanjaInput
			mv = cls.visitMethod(ACC_PUBLIC, "getHanjaInput", "()Lkp/inputs/HanjaInput;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitFieldInsn(GETFIELD, "kp/wrapper/GuiTextfieldWrapper", "hanjain", "Lkp/inputs/HanjaInput;");
			mv.visitInsn(ARETURN);

			// getColorInput
			mv = cls.visitMethod(ACC_PUBLIC, "getColorInput", "()Lkp/inputs/ColorChooserInput;", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, guitextfield, "wrapper", "Lkp/wrapper/GuiTextfieldWrapper;");
			mv.visitFieldInsn(GETFIELD, "kp/wrapper/GuiTextfieldWrapper", "colorin", "Lkp/inputs/ColorChooserInput;");
			mv.visitInsn(ARETURN);
		}
	}
}
