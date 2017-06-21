package org.rp.util;

public class GetNextNumberNo4s {

	public static long next(long thisNum)
	{
		while (true)
		{
			thisNum++;
			String str = Long.toString(thisNum);
			if (!str.contains("4"))
				break;
		}
		return thisNum;
	}
}
