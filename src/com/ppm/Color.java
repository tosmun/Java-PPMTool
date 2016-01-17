package com.ppm;

import com.ppm.utils.Utils;

public class Color
{
	private double r, g, b;
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
	public double getRed() { return this.r; }
	public long getRed(final int colorFactor) throws IllegalArgumentException { return getColor(getRed(), colorFactor); }
	public double getGreen() { return this.g; }
	public long getGreen(final int colorFactor) throws IllegalArgumentException { return getColor(getGreen(), colorFactor); }
	public double getBlue() { return this.b; }
	public long getBlue(final int colorFactor) throws IllegalArgumentException { return getColor(getBlue(), colorFactor); }
	private long getColor(final double value, final int colorFactor) throws IllegalArgumentException { return Math.round(value * colorFactor); }
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
	public String toString() { return toString(255); }
	public String toString(int maxColor) throws IllegalArgumentException
	{
		return new StringBuilder()
			.append(this.getRed(maxColor)).append(" ")
			.append(this.getGreen(maxColor)).append(" ")
			.append(this.getBlue(maxColor)).append(" ")
			.toString();
	}
}
