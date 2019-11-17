package won983212.simpleui.animation;

public class ColorAnimation extends Animation<Integer> {
	private int startColor = 0;
	private int endColor = 0;
	
	public ColorAnimation(int duration) {
		super(duration);
	}
	
	public void setRange(int start, int end) {
		this.startColor = start;
		this.endColor = end;
	}
	
	public void setInitialValue(int value) {
		setRange(value, value);
	}
	
	@Override
	protected Integer getValueAt(float x) {
		return getY(24, x) + getY(16, x) + getY(8, x) + getY(0, x);
	}
	
	private int getY(int offset, float x) {
		int start = (startColor >> offset) & 0xff;
		int end = (endColor >> offset) & 0xff;
		return (int)((end - start) * x + start) << offset;
	}
}
