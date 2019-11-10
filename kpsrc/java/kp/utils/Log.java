package kp.utils;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Log
{
	private static final PrintStream printstream = new PrintStream(new FileOutputStream(FileDescriptor.out));
	private static final PrintStream errorstream = new PrintStream(new FileOutputStream(FileDescriptor.err));

	public static void i(Object s)
	{
		printstream.print("[KoreanPatch] ");
		printstream.println(s);
	}

	public static void error(Object s)
	{
		errorstream.print("[KoreanPatch][ERROR] ");
		errorstream.println(s);
	}
}
