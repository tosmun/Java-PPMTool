package com.ppm;

import com.ppm.utils.Utils;

/**
 * Represents a single pixel in a PPM object, backed
 * by an RGB value.
 * The actual value is stored in the range 0-1, which means
 * some loss of percision is likely to occur, but this allows us
 * to fully support PPM in any format.
 * @author taylor.osmun
 */
public class Color
{
	private double r, g, b;
	/**
	 * A new color object from RGB values
	 * @param r Red. Must be 0-1
	 * @param g Green. Must be 0-1
	 * @param b Blue. Must be 0-1
	 * @throws IllegalPPMFormatException If any rgb value is not within the
	 * expected range
	 */
	Color(final double r, final double g, final double b) throws IllegalPPMFormatException
	{
		if(r < 0 || r > 1)
			throw new IllegalPPMFormatException("Expecting r to be >= 0 and <= 1, got: " + r);
		if(g < 0 || g > 1)
			throw new IllegalPPMFormatException("Expecting g to be >= 0 and <= 1, got: " + g);
		if(b < 0 || b > 1)
			throw new IllegalPPMFormatException("Expecting b to be >= 0 and <= 1, got: " + b);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	/**
	 * @return The red value, between 0-1
	 */
	public double getRed() { return this.r; }
	/**
	 * @param colorFactor Values are stored in 0-1 by default, so 1 will
	 * give you the same value. However, you could pass 255 for example, to get a range between 0-255.
	 * @return The red value, with the applied color factor
	 */
	public long getRed(final int colorFactor) { return getColor(getRed(), colorFactor); }
	/**
	 * @return The green value, between 0-1
	 */
	public double getGreen() { return this.g; }
	/**
	 * @param colorFactor Values are stored in 0-1 by default, so 1 will
	 * give you the same value. However, you could pass 255 for example, to get a range between 0-255.
	 * @return The green value, with the applied color factor
	 */
	public long getGreen(final int colorFactor) { return getColor(getGreen(), colorFactor); }
	/**
	 * @return The blue value, between 0-1
	 */
	public double getBlue() { return this.b; }
	/**
	 * @param colorFactor Values are stored in 0-1 by default, so 1 will
	 * give you the same value. However, you could pass 255 for example, to get a range between 0-255.
	 * @return The blue value, with the applied color factor
	 */
	public long getBlue(final int colorFactor) { return getColor(getBlue(), colorFactor); }
	/**
	 * @param value The color value to apply the colorFactor to
	 * @param colorFactor The colorFactor to apply
	 * @return The given colorFactor to the color value 
	 */
	private long getColor(final double value, final int colorFactor) { return Math.round(value * colorFactor); }
	/**
	 * @return A complete clone of this Color object
	 */
	public Color clone()
	{
		try
		{
			return new Color(getRed(1), getGreen(1), getBlue(1));
		}
		catch(IllegalPPMFormatException e)
		{
			throw new RuntimeException("Unexpected exception during clone() operation. This should not have happened to a valid " + Color.class.getName() + " object", e);
		}
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = Utils.hashCode(this.getRed());
		result = (result * prime) + Utils.hashCode(this.getGreen());
		result = (result * prime) + Utils.hashCode(this.getBlue());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		else if(! (obj instanceof Color))
			return false;
		final Color other = (Color) obj;
		if(! Utils.equals(this.getRed(), other.getRed()))
			return false;
		if(! Utils.equals(this.getGreen(), other.getGreen()))
			return false;
		if(! Utils.equals(this.getBlue(), other.getBlue()))
			return false;
		return true;
	}
	@Override
	/**
	 * @return The string representing of this Color using 255 as
	 * the color factor.
	 */
	public String toString() { return toString(255); }
	/**
	 * @param maxColor The color factor to use
	 * @return The string representation of this Color using
	 * the given color factor (maxColor).
	 */
	public String toString(int maxColor)
	{
		return new StringBuilder()
			.append(this.getRed(maxColor)).append(" ")
			.append(this.getGreen(maxColor)).append(" ")
			.append(this.getBlue(maxColor)).append(" ")
			.toString();
	}
}
