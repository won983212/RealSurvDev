package kp.inputs.keyboard;

public class KeyboardQwerty extends KeyboardArray
{
	public String getKeyboardArray()
	{
		return "`1234567890-=\\ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅋㅌㅊㅍㅠㅜㅡ/~!@#$%^&*()_+|ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ<>?";
	}

	public String getChosung()
	{
		return "rRseEfaqQtTdwWczxvg";
	}

	public String[] getJungsung()
	{
		return "k,o,i,O,j,p,u,P,h,hk,ho,hl,y,n,nj,np,nl,b,m,ml,l".split(",");
	}

	public String[] getJongsung()
	{
		return ",r,R,rt,s,sw,sg,e,f,fr,fa,fq,ft,fx,fv,fg,a,q,qt,t,T,d,w,c,z,x,v,g".split(",");
	}

	public String getLetter()
	{
		return "";
	}

	public String keyboardName()
	{
		return "두벌식";
	}
}
