package com.android.internal.util;

public class ObjectUtils
{
  private ObjectUtils() {}
  
  public static <T extends Comparable> int compare(T paramT1, T paramT2)
  {
    int i;
    if (paramT1 != null)
    {
      if (paramT2 != null) {
        i = paramT1.compareTo(paramT2);
      } else {
        i = 1;
      }
      return i;
    }
    if (paramT2 != null) {
      i = -1;
    } else {
      i = 0;
    }
    return i;
  }
  
  public static <T> T firstNotNull(T paramT1, T paramT2)
  {
    if (paramT1 == null) {
      paramT1 = Preconditions.checkNotNull(paramT2);
    }
    return paramT1;
  }
}
