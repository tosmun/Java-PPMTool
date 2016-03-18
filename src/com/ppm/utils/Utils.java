package com.ppm.utils;

/**
 * Collection of utilities used throughout this library
 * @author taylor.osmun
 */
public class Utils
{
	/**
	 * @param obj The object to check
	 * @param clazz (Optional) The clazz type of the object
	 * @param name (Optional) The parameter name (or some kind of identifier at least)
	 * @throws NullPointerException If the given object is null.
	 * Message is populated based on clazz and name
	 */
	public static void throwNPEIfNull(final Object obj, final Class<?> clazz, final String name) throws NullPointerException
	{
		if(obj == null)
			throw new NullPointerException("Expecting non-null " + (clazz==null?java.lang.Object.class.getName():clazz.getName()) + " (" + String.valueOf(name));
	}
	/**
	 * @param obj The object to check
	 * @param clazz (Optional) The clazz type of the object
	 * @param name (Optional) The parameter name (or some kind of identifier at least)
	 * @throws IllegalArgumentException If the given object is null.
	 * Message is populated based on clazz and name
	 */
	public static void throwIAEIfNull(final Object obj, final Class<?> clazz, final String name) throws IllegalArgumentException
	{
		if(obj == null)
			throw new IllegalArgumentException("Expecting non-null " + (clazz==null?java.lang.Object.class.getName():clazz.getName()) + " (" + String.valueOf(name));
	}
	/**
	 * @param obj The object to return the hash code for
	 * @return The hash code for the given object. If null, 0 is used.
	 */
	public static int hashCode(final Object obj)
	{
		return (obj == null ? 0 : obj.hashCode());
	}
	/**
	 * @param obj1 The first object (equals will be called for it)
	 * @param obj2 The second object
	 * @return True IFF both objects are equal.
	 * If both are null, returns true.
	 * If only one is null, returns false.
	 * Otherwise, return true IFF obj1.equals(obj2)
	 */
	public static boolean equals(final Object obj1, final Object obj2)
	{
		if(obj1 == obj2)
			return true;
		else if(obj1 == null)
			return (obj2 == null);
		else if(obj2 == null)
			return false;
		return obj1.equals(obj2);
	}
}
