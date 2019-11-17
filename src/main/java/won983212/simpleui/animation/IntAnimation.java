package won983212.simpleui.animation;

public class IntAnimation extends Animation<Integer> {
	private int start = 0;
	private int end = 0;
	
	public IntAnimation(int duration) {
		super(duration);
	}

	public void setRange(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void setInitialValue(int value) {
		setRange(value, value);
	}
	
	@Override
	protected Integer getValueAt(float x) {
		return (int)((end - start) * x) + start;
	}
}
