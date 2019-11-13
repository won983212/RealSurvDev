package won983212.korpatch.engine;

import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.util.Locale;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.ChatAllowedCharacters;
import won983212.korpatch.wrapper.IInputWrapper;

public class KoreanInputEngine extends InputEngine {
	private static final String KeyMap = "`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?";
	private static final String TranslatedKey = "`1234567890-=\\ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅋㅌㅊㅍㅠㅜㅡ/~!@#$%^&*()_+|ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ<>?";
	private static final String Chosung = "rRseEfaqQtTdwWczxvg";
	private static final String[] Jungsung = "k;o;i;O;j;p;u;P;h;hk;ho;hl;y;n;nj;np;nl;b;m;ml;l".split(";");
	private static final String[] Jongsung = ";r;R;rt;s;sw;sg;e;f;fr;fa;fq;ft;fx;fv;fg;a;q;qt;t;T;d;w;c;z;x;v;g".split(";");
	private static boolean isKorean = true;

	private int cho = -1;
	private int jung = -1;
	private int jong = 0;

	public KoreanInputEngine(IInputWrapper wrapper) {
		super(wrapper);
	}

	public boolean handleKeyTyped(char c, int i) {
		if (ChatAllowedCharacters.isAllowedCharacter(c)) {
			if (isKorean) {
				if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
					if ("qwertyuiopasdfghjklzxcvbnmQWERTOP".indexOf(c) == -1)
						c = Character.toLowerCase(c);
					if (cho == -1) {
						cho = Chosung.indexOf(c);
						write(translate(c));
						if (cho != -1) {
							input.setMovingCursor(input.getMovingCursor() - 1);
						}
					} else if (jung == -1) {
						jung = findArray(Jungsung, c);
						if (jung == -1) {
							cho = Chosung.indexOf(c);
							input.setMovingCursor(input.getAnchorCursor());
							write(translate(c));
							input.setMovingCursor(input.getMovingCursor() - 1);
						} else {
							writeAssembled();
						}
					} else if (jong == 0) {
						int newJung = findArray(Jungsung, Jungsung[jung] + String.valueOf(c));
						if (newJung != -1) {
							jung = newJung;
							writeAssembled();
						} else {
							int idx = findArray(Jongsung, c);
							if (idx != -1) {
								jong = idx;
								writeAssembled();
							} else {
								cancelAssemble();
								write(translate(c));
							}
						}
					} else {
						int newJong = findArray(Jongsung, Jongsung[jong] + String.valueOf(c));
						if (newJong != -1) {
							jong = newJong;
							writeAssembled();
						} else {
							int newCho = Chosung.indexOf(c);
							if (newCho != -1) {
								cancelAssemble();
								cho = newCho;
								write(translate(c));
								input.setMovingCursor(input.getMovingCursor() - 1);
							} else {
								char lastChar;
								if (Jongsung[jong].length() == 2) {
									lastChar = Jongsung[jong].charAt(1);
								} else {
									lastChar = Jongsung[jong].charAt(0);
								}
								backspaceKorean();
								cancelAssemble();
								cho = Chosung.indexOf(lastChar);
								jung = findArray(Jungsung, c);
								input.setMovingCursor(input.getAnchorCursor());
								writeAssembled();
							}
						}
					}
				} else {
					if (isAssemble())
						cancelAssemble();
					write(c);
				}
			} else {
				write(c);
			}
			return true;
		} else if (i == Keyboard.KEY_BACK) {
			if (!isKorean)
				return false;
			backspaceKorean();
			return true;
		} else if (i == Keyboard.KEY_LCONTROL) {
			isKorean = !isKorean;
			cancelAssemble();
			return true;
		}
		return false;
	}

	private int findArray(String[] arr, String value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(value))
				return i;
		}
		return -1;
	}

	private int findArray(String[] arr, char c) {
		return findArray(arr, String.valueOf(c));
	}

	private void backspaceKorean() {
		if (cho == -1) { // isn't assembling. backspace normally.
			backspace();
		} else if (jung == -1) { // chosung only. just remove 1 char.
			backspace();
			cancelAssemble();
		} else if (jong == 0) {
			if (Jungsung[jung].length() == 2) {
				jung = findArray(Jungsung, Jungsung[jung].charAt(0));
				write(getAssembledChar(cho, jung, jong));
			} else {
				jung = -1;
				write(translate(Chosung.charAt(cho)));
			}
		} else {
			if (Jongsung[jong].length() == 2) {
				jong = findArray(Jongsung, Jongsung[jong].charAt(0));
			} else {
				jong = 0;
			}
			write(getAssembledChar(cho, jung, jong));
		}
		if (cho != -1) {
			input.setMovingCursor(input.getAnchorCursor() - 1);
		}
	}

	private char getAssembledChar(int cho, int jung, int jong) {
		return (char) (0xAC00 + (cho * 21 + jung) * 28 + jong);
	}

	private void writeAssembled() {
		write(getAssembledChar(cho, jung, jong));
		input.setMovingCursor(input.getAnchorCursor() - 1);
	}

	private char translate(char en) {
		int idx = KeyMap.indexOf(en);
		if (idx != -1)
			return TranslatedKey.charAt(idx);
		return en;
	}

	public boolean isAssemble() {
		return cho != -1;
	}

	public void cancelAssemble() {
		cho = -1;
		jung = -1;
		jong = 0;
		input.setMovingCursor(input.getAnchorCursor());
	}

	public static boolean isKorMode() {
		return isKorean;
	}
}