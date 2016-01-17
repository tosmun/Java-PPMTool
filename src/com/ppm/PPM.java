package com.ppm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import com.ppm.utils.Utils;

public class PPM
{
	public static final String MAGIC_NUMBER = "P3";
	public static final int MAX_MAX_COLOR_VALUE = 65536;
	public static final double GREYSCALE_RED_FACTOR = 0.2126;
	public static final double GREYSCALE_GREEN_FACTOR = 0.7152;
	public static final double GREYSCALE_BLUE_FACTOR = 0.0722; 
	private Color[][] colorMap;
	public PPM(final BufferedReader r) throws NullPointerException, IOException, IllegalPPMFormatException
	{
		Utils.throwNPEIfNull(r, BufferedReader.class, "r");
		{
			final String typeLine = readNextWord(r);
			if(typeLine == null)
				throw new IllegalPPMFormatException("No magic number specification");
			else if(! typeLine.trim().equals(MAGIC_NUMBER))
				throw new IllegalPPMFormatException("Unsupported magic number: " + typeLine + ". This implementaiton only supports " + MAGIC_NUMBER);
		}
		final Integer width = readNextInteger(r);
		if(width == null)
			throw new IllegalPPMFormatException("No width specification");
		else if(width < 0)
			throw new IllegalPPMFormatException("Expecting width >= 0. Was: " + width);
		final Integer height = readNextInteger(r);
		if(height == null)
			throw new IllegalPPMFormatException("No height specification");
		else if(height < 0)
			throw new IllegalPPMFormatException("Expecting height >= 0. Was: " + height);
		final Integer maxColor = readNextInteger(r);
		if(maxColor == null)
			throw new IllegalPPMFormatException("No max color specification");
		try { verifyMaxColor(maxColor); } 
		catch(IllegalArgumentException e) { throw new IllegalPPMFormatException(e.getMessage()); }
		this.colorMap = new Color[width][height];
		for(int y=0; y<height; y++)
		{
			for(int x=0; x<width; x++)
			{
				final Integer red = readNextInteger(r);
				if(red == null)
					throw new IllegalPPMFormatException("Not enough RGB values");
				final Integer green = readNextInteger(r);
				if(green == null)
					throw new IllegalPPMFormatException("Not enough RGB values");
				final Integer blue = readNextInteger(r);
				if(blue == null)
					throw new IllegalPPMFormatException("Not enough RGB values");
				this.colorMap[x][y] = new Color(((double) red)/maxColor, ((double) green)/maxColor, ((double) blue)/maxColor);
			}
		}
	}
	private PPM(final Color[][] colorMap)
	{
		Utils.throwNPEIfNull(colorMap, Color[][].class, "colorMap");
		this.colorMap = colorMap;
	}
	public Color[][] getColorMap()
	{
		return this.colorMap;
	}
	public int getWidth()
	{
		return this.colorMap.length;
	}
	public int getHeight()
	{
		return (this.colorMap.length <= 0 ? 0 : this.colorMap[0].length);
	}
	public Color getColor(final int x, final int y) throws IllegalArgumentException
	{
		if(x < 0 || x > getWidth())
			throw new IllegalArgumentException("x is out of range. Valid Range for this " + PPM.class.getSimpleName() + " is: 0-" + getWidth() + ". Given: " + x);
		else if(y < 0 || y > getHeight())
			throw new IllegalArgumentException("y is out of range. Valid Range for this " + PPM.class.getSimpleName() + " is: 0-" + getHeight() + ". Given: " + y);
		return this.colorMap[x][y];
	}
	public PPM clone()
	{
		final int width = getWidth();
		final int height = getHeight();
		final Color[][] newColorMap = new Color[width][height];
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				newColorMap[x][y] = this.colorMap[x][y].clone();
		return new PPM(newColorMap);
	}
	public void writeToStreams(final int maxColor, final OutputStream ... outs) throws IllegalArgumentException, IOException
	{
		verifyMaxColor(maxColor);
		if(outs != null && outs.length > 0)
		{
			final int width = getWidth();
			final int height = getHeight();
			for(final OutputStream out : outs)
			{
				if(out != null)
				{
					//Magic Number
					out.write((MAGIC_NUMBER + " ").getBytes());
					//Width & Height
					out.write((String.valueOf(width) + " " + String.valueOf(height) + " ").getBytes());
					//Max color
					out.write((String.valueOf(maxColor) + " ").getBytes());
				}
			}
			for(int y=0; y<height; y++)
			{
				for(int x=0; x<width; x++)
				{
					for(final OutputStream out : outs)
					{
						if(out != null)
							out.write(getColor(x, y).toString(maxColor).getBytes());
					}
				}
			}
		}
	}
	public void greyscale()
	{
		final int width = getWidth();
		final int height = getHeight();
		for(int x=0; x<width; x++)
		{
			for(int y=0; y<height; y++)
			{
				final Color c = getColor(x, y);
				final double newValue = (c.getRed()*GREYSCALE_RED_FACTOR)+(c.getGreen()*GREYSCALE_GREEN_FACTOR)+(c.getBlue()*GREYSCALE_BLUE_FACTOR);
				try { this.colorMap[x][y] = new Color(newValue, newValue, newValue); }
				catch(IllegalPPMFormatException e) { throw new RuntimeException("Unexpected exception", e); }
			}
		}
	}
	public void detectEdges(final EdgeDetectionAlgorithm edgeDetectionAlgorithm) throws IllegalArgumentException
	{
		Utils.throwIAEIfNull(edgeDetectionAlgorithm, EdgeDetectionAlgorithm.class, "edgeDetectionAlgorithm");
		final Color[][] newColorMap;
		//Sobel
		if(edgeDetectionAlgorithm == EdgeDetectionAlgorithm.SOBEL)
			newColorMap = detectSobelEdges();
		else
			throw new RuntimeException("Unrecognized " + EdgeDetectionAlgorithm.class.getSimpleName() + ": " + edgeDetectionAlgorithm);
		this.colorMap = newColorMap;
	}
	private Color[][] detectSobelEdges()
	{
		final int width = getWidth();
		final int height = getHeight();
		final Color[][] ret = new Color[width][height];
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				//top horizontal
				double th_r = 0;
				double th_g = 0;
				double th_b = 0;
				//left vertical
				double lv_r = 0;
				double lv_g = 0;
				double lv_b = 0;
				//bottom horizontal
				double bh_r = 0;
				double bh_g = 0;
				double bh_b = 0;
				//right vertical
				double rv_r = 0;
				double rv_g = 0;
				double rv_b = 0;
				for(int ny=Math.max(y-1, 0);ny<=Math.min(y+1, height-1);ny++)
				{
					for(int nx=Math.max(x-1, 0);nx<=Math.min(x+1, width-1);nx++)
					{
						final Color value = this.colorMap[nx][ny];
						final double r = value.getRed();
						final double g = value.getGreen();
						final double b = value.getBlue();
						//top left
						if(nx < x && ny < y)
						{
							th_r += r;
							th_g += g;
							th_b += b;
							lv_r += r;
							lv_g += g;
							lv_b += b;
						}
						//top middle
						else if(nx < x && ny == y)
						{
							th_r += (r * 2);
							th_g += (g * 2);
							th_b += (b * 2);
						}
						//top right
						else if(nx < x && ny > y)
						{
							th_r += r;
							th_g += g;
							th_b += b;
							rv_r += r;
							rv_g += g;
							rv_b += b;
						}
						//middle left
						else if(nx == x && ny < y)
						{
							lv_r += (r * 2);
							lv_g += (g * 2);
							lv_b += (b * 2);
						}
						//middle right
						else if(nx == x && ny > y)
						{
							rv_r += (r * 2);
							rv_g += (g * 2);
							rv_b += (b * 2);
						}
						//bottom left
						else if(nx > x && ny < y)
						{
							bh_r += r;
							bh_g += g;
							bh_b += b;
							lv_r += r;
							lv_g += g;
							lv_b += b;
						}
						//bottom middle
						else if(nx > x && ny == y)
						{
							bh_r += (r * 2);
							bh_g += (g * 2);
							bh_b += (b * 2);
						}
						//bottom right
						else if(nx > x && ny > y)
						{
							bh_r += r;
							bh_g += g;
							bh_b += b;
							rv_r += r;
							rv_g += g;
							rv_b += b;
						}
					}
				}
				final double edgeHorizontal_r = th_r - bh_r;
				final double edgeHorizontal_g = th_g - bh_g;
				final double edgeHorizontal_b = th_b - bh_b;
				final double edgeVertical_r = lv_r - rv_r;
				final double edgeVertical_g = lv_g - rv_g;
				final double edgeVertical_b = lv_b - rv_b;
				final double edge_r = Math.sqrt((edgeHorizontal_r*edgeHorizontal_r)+(edgeVertical_r*edgeVertical_r));
				final double edge_g = Math.sqrt((edgeHorizontal_g*edgeHorizontal_g)+(edgeVertical_g*edgeVertical_g));
				final double edge_b = Math.sqrt((edgeHorizontal_b*edgeHorizontal_b)+(edgeVertical_b*edgeVertical_b));
				try { ret[x][y] = new Color(Math.min(edge_r, 1), Math.min(edge_g, 1), Math.min(edge_b, 1)); }
				catch(IllegalPPMFormatException e) { throw new RuntimeException("Unexpected exception", e); }
			}
		}
		return ret;
	}
	private Integer readNextInteger(final BufferedReader r) throws IOException, IllegalPPMFormatException
	{
		final String word = readNextWord(r);
		if(word == null)
			return null;
		try
		{
			return Integer.parseInt(word);
		}
		catch(NumberFormatException e)
		{
			throw new IllegalPPMFormatException("Expected Integer, but could not parse: " + word, e);
		}
	}
	private String readNextWord(final BufferedReader r) throws IOException
	{
		String ret = null;
		while(true)
		{
			final int v = r.read();
			if(v <= -1)
				break;
			//TODO comments
			final char c = (char) v;
			if(c == ' ' || c == '\t' || c == '\r' || c == '\n')
			{
				if(ret != null)
					break;
				continue;
			}
			if(ret == null)
				ret = "";
			ret += c;
		}
		return ret;
	}
	private static void verifyMaxColor(final int maxColor) throws IllegalArgumentException
	{
		if(maxColor < 0 || maxColor > MAX_MAX_COLOR_VALUE)
			throw new IllegalArgumentException("Max color specification out of range. Expecting: >= 0 && <= " + MAX_MAX_COLOR_VALUE + ". Given: " + maxColor);
	}
}
