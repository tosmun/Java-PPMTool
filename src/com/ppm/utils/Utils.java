package com.ppm.utils;

public class Utils
{
	public static void throwNPEIfNull(final Object obj, final Class<?> clazz, final String name) throws NullPointerException
	{
		if(obj == null)
			throw new NullPointerException("Expecting non-null " + (clazz==null?java.lang.Object.class.getName():clazz.getName()) + " (" + String.valueOf(name));
	}
	public static void throwIAEIfNull(final Object obj, final Class<?> clazz, final String name) throws IllegalArgumentException
	{
		if(obj == null)
			throw new IllegalArgumentException("Expecting non-null " + (clazz==null?java.lang.Object.class.getName():clazz.getName()) + " (" + String.valueOf(name));
	}
	public static int hashCode(final Object obj)
	{
		return (obj == null ? 0 : obj.hashCode());
	}
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
