package com.ppm;

public enum EdgeDetectionAlgorithm
{
	SOBEL;
	public static String allToString()
	{
		final StringBuilder sb = new StringBuilder("[");
		final EdgeDetectionAlgorithm[] values = EdgeDetectionAlgorithm.values();
		for(int i=0; i<values.length; i++)
		{
			sb.append(values[i]);
			if(i>0)
				sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}
}
