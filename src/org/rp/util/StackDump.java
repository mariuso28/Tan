package org.rp.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StackDump {

	public static String toString(Exception e)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		return baos.toString();
	}
}
