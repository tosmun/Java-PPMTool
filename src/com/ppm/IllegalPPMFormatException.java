package com.ppm;

public class IllegalPPMFormatException extends Exception
{
	private static final long serialVersionUID = 1L;
	private static final String MSG_FOMRAT = "Illegal PPM format: %s";
	public IllegalPPMFormatException(final String msg)
	{
		this(msg, null);
	}
	public IllegalPPMFormatException(final String msg, final Throwable t)
	{
		super(String.format(MSG_FOMRAT, msg), t);
	}
}
