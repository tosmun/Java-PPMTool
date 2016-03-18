package com.ppm;

/**
 * Represents all supported edge detection algorithms that
 * can be used when performing edge detection against PPM content.
 * @author taylor.osmun
 */
public enum EdgeDetectionAlgorithm
{
	SOBEL;
	/**
	 * @return A concise string representation of all supported
	 * edge detection algorithms
	 */
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
