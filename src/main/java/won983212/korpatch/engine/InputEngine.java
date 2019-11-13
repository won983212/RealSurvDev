package won983212.korpatch.engine;

import won983212.korpatch.wrapper.IInputWrapper;

public class InputEngine {
	protected IInputWrapper input;

	protected InputEngine(IInputWrapper input) {
		this.input = input;
	}

	public boolean equalsWrapper(IInputWrapper wrapper) {
		return input.equals(wrapper);
	}
	
	protected int getStartCursor() {
		return Math.min(input.getAnchorCursor(), input.getMovingCursor());
	}

	protected int getEndCursor() {
		return Math.max(input.getAnchorCursor(), input.getMovingCursor());
	}

	protected boolean isSelectionEnabled() {
		return input.getAnchorCursor() != input.getMovingCursor();
	}

	protected void setCursor(int cursor) {
		input.setAnchorCursor(cursor);
		input.setMovingCursor(cursor);
	}

	protected void write(char c) {
		int start = getStartCursor();
		String s1 = input.getText().substring(0, start);
		String s2 = input.getText().substring(getEndCursor());
		input.setText(s1 + c + s2);
		setCursor(start + 1);
	}

	protected void write(String text) {
		int start = getStartCursor();
		String s1 = input.getText().substring(0, start);
		String s2 = input.getText().substring(getEndCursor());
		input.setText(s1 + text + s2);
		setCursor(start + text.length());
	}

	protected void backspace() {
		int start = getStartCursor();
		String s1 = input.getText().substring(0, start);
		String s2 = input.getText().substring(getEndCursor());
		if (input.getMovingCursor() != input.getAnchorCursor()) {
			input.setText(s1 + s2);
			setCursor(start);
		} else if (start > 0) {
			input.setText(s1.substring(0, s1.length() - 1) + s2);
			setCursor(start - 1);
		}
	}
}
