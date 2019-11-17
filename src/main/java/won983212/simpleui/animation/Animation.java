package won983212.simpleui.animation;

import net.minecraft.util.math.MathHelper;

public abstract class Animation<T> {
	public static final int MOUSELEAVE_DURATION = 200;
	public static final int FOCUS_DURATION = 200;
	public static final int MENUOPEN_DURATION = 200;
	public static final int STATECHANGE_DURATION = 200;
	
	private int duration = 0;
	private float time = 0; // [0;1]
	private long lastTick = 0;
	private boolean playMode = true; // 0:playToEnd, 1:playToStart
	
	public Animation(int duration) {
		this.duration = duration;
	}
	
	public void setDuration(int millis) {
		this.duration = millis;
	}
	
	public void setTime(float time) {
		if(time < 0)
			time = 0;
		if(time > 1)
			time = 1;
		lastTick = System.currentTimeMillis() - (long) (time * duration);
	}
	
	public boolean isAnimating() {
		update();
		return time > 0 && time < 1;
	}
	
	public void playForward() {
		play(false);
	}
	
	public void playReverse() {
		play(true);
	}
	
	public void play(boolean reverse) {
		update();
		float time = reverse ? (1 - this.time) : this.time;
		lastTick = System.currentTimeMillis() - (long) (time * duration);
		playMode = reverse;
	}
	
	private void update() {
		time = (System.currentTimeMillis() - lastTick) / (float) duration;
		time = playMode ? (1 - time) : time;
		time = MathHelper.clamp(time, 0, 1);
	}
	
	public T get() {
		update();
		return getValueAt(f(time));
	}
	
	protected abstract T getValueAt(float x); 
	
	private static float f(float t) {
		if (t <= 0.5f)
			return 2.0f * t * t;
		t -= 0.5f;
		return 2.0f * t * (1.0f - t) + 0.5f;
	}
}
