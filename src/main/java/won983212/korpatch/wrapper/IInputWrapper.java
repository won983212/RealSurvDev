package won983212.korpatch.wrapper;

public interface IInputWrapper {
	public void setText(String text);

	public String getText();

	public int getAnchorCursor();

	public int getMovingCursor();

	public void setAnchorCursor(int cursor);

	public void setMovingCursor(int cursor);
}