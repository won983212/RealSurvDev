package kp.inputs;

public abstract interface Inputer
{
	public abstract int getCursorPos();

	public abstract void setCursorPos(int paramInt);

	public abstract String getTargetText();

	public abstract void setTargetText(String paramString);
}
