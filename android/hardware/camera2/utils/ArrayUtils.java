package android.hardware.camera2.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ArrayUtils
{
  private static final boolean DEBUG = false;
  private static final String TAG = "ArrayUtils";
  
  private ArrayUtils()
  {
    throw new AssertionError();
  }
  
  public static boolean contains(int[] paramArrayOfInt, int paramInt)
  {
    boolean bool;
    if (getArrayIndex(paramArrayOfInt, paramInt) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static <T> boolean contains(T[] paramArrayOfT, T paramT)
  {
    boolean bool;
    if (getArrayIndex(paramArrayOfT, paramT) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int[] convertStringListToIntArray(List<String> paramList, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if (paramList == null) {
      return null;
    }
    paramArrayOfString = convertStringListToIntList(paramList, paramArrayOfString, paramArrayOfInt);
    paramList = new int[paramArrayOfString.size()];
    for (int i = 0; i < paramList.length; i++) {
      paramList[i] = ((Integer)paramArrayOfString.get(i)).intValue();
    }
    return paramList;
  }
  
  public static List<Integer> convertStringListToIntList(List<String> paramList, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if (paramList == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      int i = getArrayIndex(paramArrayOfString, (String)paramList.next());
      if (i >= 0) {
        if (i < paramArrayOfInt.length) {
          localArrayList.add(Integer.valueOf(paramArrayOfInt[i]));
        }
      }
    }
    return localArrayList;
  }
  
  public static int getArrayIndex(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt == null) {
      return -1;
    }
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i] == paramInt) {
        return i;
      }
    }
    return -1;
  }
  
  public static <T> int getArrayIndex(T[] paramArrayOfT, T paramT)
  {
    if (paramArrayOfT == null) {
      return -1;
    }
    int i = 0;
    int j = paramArrayOfT.length;
    for (int k = 0; k < j; k++)
    {
      if (Objects.equals(paramArrayOfT[k], paramT)) {
        return i;
      }
      i++;
    }
    return -1;
  }
  
  public static int[] toIntArray(List<Integer> paramList)
  {
    if (paramList == null) {
      return null;
    }
    int[] arrayOfInt = new int[paramList.size()];
    int i = 0;
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      arrayOfInt[i] = ((Integer)paramList.next()).intValue();
      i++;
    }
    return arrayOfInt;
  }
}
