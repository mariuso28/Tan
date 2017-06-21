package org.rp.util;

public class NumberUtil {


	public static double trimDecimals2(double value)
	{
		int iValue = (int) (value * 100.0);
		return iValue/100.0;
	}
	
	public static int parseIntInRange(String value,int r1,int r2) throws NumberFormatException
	{
		int val = Integer.parseInt(value);
		if (val<r1 || val>r2)
			throw new NumberFormatException("Number " + val + " out of range : " + r1 + " - " +r2);
		return val;
	}
}
