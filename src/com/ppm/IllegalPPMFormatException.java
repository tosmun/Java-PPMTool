package com.ppm;

/**
 * Represents the exceptional case when
 * the given PPM content is not valid in the current state.
 * @author taylor.osmun
 */
public class IllegalPPMFormatException extends Exception
{
	private static final long serialVersionUID = 1L;
	private static final String MSG_FOMRAT = "Illegal PPM format: %s";
	/**
	 * @param msg The error message, will be prefixed with additional content
	 */
	public IllegalPPMFormatException(final String msg)
	{
		this(msg, null);
	}
	/**
	 * @param msg The error message, will be prefixed with additional content
	 * @param t The cause
	 */
	public IllegalPPMFormatException(final String msg, final Throwable t)
	{
		super(String.format(MSG_FOMRAT, msg), t);
	}
}
