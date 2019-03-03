package android.hardware.camera2.utils;

import java.util.Iterator;
import java.util.List;

public class ListUtils
{
  private ListUtils()
  {
    throw new AssertionError();
  }
  
  public static <T> boolean listContains(List<T> paramList, T paramT)
  {
    if (paramList == null) {
      return false;
    }
    return paramList.contains(paramT);
  }
  
  public static <T> boolean listElementsEqualTo(List<T> paramList, T paramT)
  {
    boolean bool1 = false;
    if (paramList == null) {
      return false;
    }
    boolean bool2 = bool1;
    if (paramList.size() == 1)
    {
      bool2 = bool1;
      if (paramList.contains(paramT)) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public static <T> T listSelectFirstFrom(List<T> paramList, T[] paramArrayOfT)
  {
    if (paramList == null) {
      return null;
    }
    int i = paramArrayOfT.length;
    for (int j = 0; j < i; j++)
    {
      T ? = paramArrayOfT[j];
      if (paramList.contains(?)) {
        return ?;
      }
    }
    return null;
  }
  
  public static <T> String listToString(List<T> paramList)
  {
    if (paramList == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    int i = paramList.size();
    int j = 0;
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      localStringBuilder.append(paramList.next());
      if (j != i - 1) {
        localStringBuilder.append(',');
      }
      j++;
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}
