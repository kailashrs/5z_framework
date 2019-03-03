package com.android.internal.util;

import android.util.ArraySet;
import dalvik.system.VMRuntime;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import libcore.util.EmptyArray;

public class ArrayUtils
{
  private static final int CACHE_SIZE = 73;
  private static Object[] sCache = new Object[73];
  
  private ArrayUtils() {}
  
  public static <T> ArraySet<T> add(ArraySet<T> paramArraySet, T paramT)
  {
    Object localObject = paramArraySet;
    if (paramArraySet == null) {
      localObject = new ArraySet();
    }
    ((ArraySet)localObject).add(paramT);
    return localObject;
  }
  
  public static <T> ArrayList<T> add(ArrayList<T> paramArrayList, T paramT)
  {
    Object localObject = paramArrayList;
    if (paramArrayList == null) {
      localObject = new ArrayList();
    }
    ((ArrayList)localObject).add(paramT);
    return localObject;
  }
  
  public static <T> T[] appendElement(Class<T> paramClass, T[] paramArrayOfT, T paramT)
  {
    return appendElement(paramClass, paramArrayOfT, paramT, false);
  }
  
  public static <T> T[] appendElement(Class<T> paramClass, T[] paramArrayOfT, T paramT, boolean paramBoolean)
  {
    int i;
    if (paramArrayOfT != null)
    {
      if ((!paramBoolean) && (contains(paramArrayOfT, paramT))) {
        return paramArrayOfT;
      }
      i = paramArrayOfT.length;
      paramClass = (Object[])Array.newInstance(paramClass, i + 1);
      System.arraycopy(paramArrayOfT, 0, paramClass, 0, i);
    }
    else
    {
      i = 0;
      paramClass = (Object[])Array.newInstance(paramClass, 1);
    }
    paramClass[i] = paramT;
    return paramClass;
  }
  
  public static int[] appendInt(int[] paramArrayOfInt, int paramInt)
  {
    return appendInt(paramArrayOfInt, paramInt, false);
  }
  
  public static int[] appendInt(int[] paramArrayOfInt, int paramInt, boolean paramBoolean)
  {
    if (paramArrayOfInt == null) {
      return new int[] { paramInt };
    }
    int i = paramArrayOfInt.length;
    if (!paramBoolean) {
      for (int j = 0; j < i; j++) {
        if (paramArrayOfInt[j] == paramInt) {
          return paramArrayOfInt;
        }
      }
    }
    int[] arrayOfInt = new int[i + 1];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    arrayOfInt[i] = paramInt;
    return arrayOfInt;
  }
  
  public static long[] appendLong(long[] paramArrayOfLong, long paramLong)
  {
    return appendLong(paramArrayOfLong, paramLong, false);
  }
  
  public static long[] appendLong(long[] paramArrayOfLong, long paramLong, boolean paramBoolean)
  {
    if (paramArrayOfLong == null) {
      return new long[] { paramLong };
    }
    int i = paramArrayOfLong.length;
    if (!paramBoolean) {
      for (int j = 0; j < i; j++) {
        if (paramArrayOfLong[j] == paramLong) {
          return paramArrayOfLong;
        }
      }
    }
    long[] arrayOfLong = new long[i + 1];
    System.arraycopy(paramArrayOfLong, 0, arrayOfLong, 0, i);
    arrayOfLong[i] = paramLong;
    return arrayOfLong;
  }
  
  public static <T> ArraySet<T> cloneOrNull(ArraySet<T> paramArraySet)
  {
    if (paramArraySet != null) {
      paramArraySet = new ArraySet(paramArraySet);
    } else {
      paramArraySet = null;
    }
    return paramArraySet;
  }
  
  public static long[] cloneOrNull(long[] paramArrayOfLong)
  {
    if (paramArrayOfLong != null) {
      paramArrayOfLong = (long[])paramArrayOfLong.clone();
    } else {
      paramArrayOfLong = null;
    }
    return paramArrayOfLong;
  }
  
  public static <T> boolean contains(Collection<T> paramCollection, T paramT)
  {
    boolean bool;
    if (paramCollection != null) {
      bool = paramCollection.contains(paramT);
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean contains(char[] paramArrayOfChar, char paramChar)
  {
    if (paramArrayOfChar == null) {
      return false;
    }
    int i = paramArrayOfChar.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfChar[j] == paramChar) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean contains(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt == null) {
      return false;
    }
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfInt[j] == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean contains(long[] paramArrayOfLong, long paramLong)
  {
    if (paramArrayOfLong == null) {
      return false;
    }
    int i = paramArrayOfLong.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfLong[j] == paramLong) {
        return true;
      }
    }
    return false;
  }
  
  public static <T> boolean contains(T[] paramArrayOfT, T paramT)
  {
    boolean bool;
    if (indexOf(paramArrayOfT, paramT) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static <T> boolean containsAll(char[] paramArrayOfChar1, char[] paramArrayOfChar2)
  {
    if (paramArrayOfChar2 == null) {
      return true;
    }
    int i = paramArrayOfChar2.length;
    for (int j = 0; j < i; j++) {
      if (!contains(paramArrayOfChar1, paramArrayOfChar2[j])) {
        return false;
      }
    }
    return true;
  }
  
  public static <T> boolean containsAll(T[] paramArrayOfT1, T[] paramArrayOfT2)
  {
    if (paramArrayOfT2 == null) {
      return true;
    }
    int i = paramArrayOfT2.length;
    for (int j = 0; j < i; j++) {
      if (!contains(paramArrayOfT1, paramArrayOfT2[j])) {
        return false;
      }
    }
    return true;
  }
  
  public static <T> boolean containsAny(T[] paramArrayOfT1, T[] paramArrayOfT2)
  {
    if (paramArrayOfT2 == null) {
      return false;
    }
    int i = paramArrayOfT2.length;
    for (int j = 0; j < i; j++) {
      if (contains(paramArrayOfT1, paramArrayOfT2[j])) {
        return true;
      }
    }
    return false;
  }
  
  public static int[] convertToIntArray(List<Integer> paramList)
  {
    int[] arrayOfInt = new int[paramList.size()];
    for (int i = 0; i < paramList.size(); i++) {
      arrayOfInt[i] = ((Integer)paramList.get(i)).intValue();
    }
    return arrayOfInt;
  }
  
  public static long[] convertToLongArray(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    long[] arrayOfLong = new long[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfLong[i] = paramArrayOfInt[i];
    }
    return arrayOfLong;
  }
  
  public static int[] defeatNullable(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      paramArrayOfInt = EmptyArray.INT;
    }
    return paramArrayOfInt;
  }
  
  public static String[] defeatNullable(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      paramArrayOfString = EmptyArray.STRING;
    }
    return paramArrayOfString;
  }
  
  public static <T> T[] emptyArray(Class<T> paramClass)
  {
    if (paramClass == Object.class) {
      return EmptyArray.OBJECT;
    }
    int i = (paramClass.hashCode() & 0x7FFFFFFF) % 73;
    Object localObject1 = sCache[i];
    Object localObject2;
    if (localObject1 != null)
    {
      localObject2 = localObject1;
      if (localObject1.getClass().getComponentType() == paramClass) {}
    }
    else
    {
      localObject2 = Array.newInstance(paramClass, 0);
      sCache[i] = localObject2;
    }
    return (Object[])localObject2;
  }
  
  public static boolean equals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    if (paramInt >= 0)
    {
      if (paramArrayOfByte1 == paramArrayOfByte2) {
        return true;
      }
      if ((paramArrayOfByte1 != null) && (paramArrayOfByte2 != null) && (paramArrayOfByte1.length >= paramInt) && (paramArrayOfByte2.length >= paramInt))
      {
        for (int i = 0; i < paramInt; i++) {
          if (paramArrayOfByte1[i] != paramArrayOfByte2[i]) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    throw new IllegalArgumentException();
  }
  
  public static <T> int indexOf(T[] paramArrayOfT, T paramT)
  {
    if (paramArrayOfT == null) {
      return -1;
    }
    for (int i = 0; i < paramArrayOfT.length; i++) {
      if (Objects.equals(paramArrayOfT[i], paramT)) {
        return i;
      }
    }
    return -1;
  }
  
  public static boolean isEmpty(Collection<?> paramCollection)
  {
    boolean bool;
    if ((paramCollection != null) && (!paramCollection.isEmpty())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmpty(Map<?, ?> paramMap)
  {
    boolean bool;
    if ((paramMap != null) && (!paramMap.isEmpty())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmpty(byte[] paramArrayOfByte)
  {
    boolean bool;
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmpty(int[] paramArrayOfInt)
  {
    boolean bool;
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmpty(long[] paramArrayOfLong)
  {
    boolean bool;
    if ((paramArrayOfLong != null) && (paramArrayOfLong.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static <T> boolean isEmpty(T[] paramArrayOfT)
  {
    boolean bool;
    if ((paramArrayOfT != null) && (paramArrayOfT.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isEmpty(boolean[] paramArrayOfBoolean)
  {
    boolean bool;
    if ((paramArrayOfBoolean != null) && (paramArrayOfBoolean.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static <T> T[] newUnpaddedArray(Class<T> paramClass, int paramInt)
  {
    return (Object[])VMRuntime.getRuntime().newUnpaddedArray(paramClass, paramInt);
  }
  
  public static boolean[] newUnpaddedBooleanArray(int paramInt)
  {
    return (boolean[])VMRuntime.getRuntime().newUnpaddedArray(Boolean.TYPE, paramInt);
  }
  
  public static byte[] newUnpaddedByteArray(int paramInt)
  {
    return (byte[])VMRuntime.getRuntime().newUnpaddedArray(Byte.TYPE, paramInt);
  }
  
  public static char[] newUnpaddedCharArray(int paramInt)
  {
    return (char[])VMRuntime.getRuntime().newUnpaddedArray(Character.TYPE, paramInt);
  }
  
  public static float[] newUnpaddedFloatArray(int paramInt)
  {
    return (float[])VMRuntime.getRuntime().newUnpaddedArray(Float.TYPE, paramInt);
  }
  
  public static int[] newUnpaddedIntArray(int paramInt)
  {
    return (int[])VMRuntime.getRuntime().newUnpaddedArray(Integer.TYPE, paramInt);
  }
  
  public static long[] newUnpaddedLongArray(int paramInt)
  {
    return (long[])VMRuntime.getRuntime().newUnpaddedArray(Long.TYPE, paramInt);
  }
  
  public static Object[] newUnpaddedObjectArray(int paramInt)
  {
    return (Object[])VMRuntime.getRuntime().newUnpaddedArray(Object.class, paramInt);
  }
  
  public static <T> boolean referenceEquals(ArrayList<T> paramArrayList1, ArrayList<T> paramArrayList2)
  {
    boolean bool = true;
    if (paramArrayList1 == paramArrayList2) {
      return true;
    }
    int i = paramArrayList1.size();
    int j = paramArrayList2.size();
    if ((paramArrayList1 != null) && (paramArrayList2 != null) && (i == j))
    {
      int k = 0;
      for (j = 0; (j < i) && (k == 0); j++)
      {
        int m;
        if (paramArrayList1.get(j) != paramArrayList2.get(j)) {
          m = 1;
        } else {
          m = 0;
        }
        k |= m;
      }
      if (k != 0) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public static <T> ArraySet<T> remove(ArraySet<T> paramArraySet, T paramT)
  {
    if (paramArraySet == null) {
      return null;
    }
    paramArraySet.remove(paramT);
    if (paramArraySet.isEmpty()) {
      return null;
    }
    return paramArraySet;
  }
  
  public static <T> ArrayList<T> remove(ArrayList<T> paramArrayList, T paramT)
  {
    if (paramArrayList == null) {
      return null;
    }
    paramArrayList.remove(paramT);
    if (paramArrayList.isEmpty()) {
      return null;
    }
    return paramArrayList;
  }
  
  public static <T> T[] removeElement(Class<T> paramClass, T[] paramArrayOfT, T paramT)
  {
    if (paramArrayOfT != null)
    {
      if (!contains(paramArrayOfT, paramT)) {
        return paramArrayOfT;
      }
      int i = paramArrayOfT.length;
      for (int j = 0; j < i; j++) {
        if (Objects.equals(paramArrayOfT[j], paramT))
        {
          if (i == 1) {
            return null;
          }
          paramClass = (Object[])Array.newInstance(paramClass, i - 1);
          System.arraycopy(paramArrayOfT, 0, paramClass, 0, j);
          System.arraycopy(paramArrayOfT, j + 1, paramClass, j, i - j - 1);
          return paramClass;
        }
      }
    }
    return paramArrayOfT;
  }
  
  public static int[] removeInt(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfInt[j] == paramInt)
      {
        int[] arrayOfInt = new int[i - 1];
        if (j > 0) {
          System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, j);
        }
        if (j < i - 1) {
          System.arraycopy(paramArrayOfInt, j + 1, arrayOfInt, j, i - j - 1);
        }
        return arrayOfInt;
      }
    }
    return paramArrayOfInt;
  }
  
  public static long[] removeLong(long[] paramArrayOfLong, long paramLong)
  {
    if (paramArrayOfLong == null) {
      return null;
    }
    int i = paramArrayOfLong.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfLong[j] == paramLong)
      {
        long[] arrayOfLong = new long[i - 1];
        if (j > 0) {
          System.arraycopy(paramArrayOfLong, 0, arrayOfLong, 0, j);
        }
        if (j < i - 1) {
          System.arraycopy(paramArrayOfLong, j + 1, arrayOfLong, j, i - j - 1);
        }
        return arrayOfLong;
      }
    }
    return paramArrayOfLong;
  }
  
  public static String[] removeString(String[] paramArrayOfString, String paramString)
  {
    if (paramArrayOfString == null) {
      return null;
    }
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (Objects.equals(paramArrayOfString[j], paramString))
      {
        paramString = new String[i - 1];
        if (j > 0) {
          System.arraycopy(paramArrayOfString, 0, paramString, 0, j);
        }
        if (j < i - 1) {
          System.arraycopy(paramArrayOfString, j + 1, paramString, j, i - j - 1);
        }
        return paramString;
      }
    }
    return paramArrayOfString;
  }
  
  public static int size(Collection<?> paramCollection)
  {
    int i;
    if (paramCollection == null) {
      i = 0;
    } else {
      i = paramCollection.size();
    }
    return i;
  }
  
  public static int size(Object[] paramArrayOfObject)
  {
    int i;
    if (paramArrayOfObject == null) {
      i = 0;
    } else {
      i = paramArrayOfObject.length;
    }
    return i;
  }
  
  public static long total(long[] paramArrayOfLong)
  {
    long l1 = 0L;
    long l2 = l1;
    if (paramArrayOfLong != null)
    {
      int i = paramArrayOfLong.length;
      for (int j = 0;; j++)
      {
        l2 = l1;
        if (j >= i) {
          break;
        }
        l1 += paramArrayOfLong[j];
      }
    }
    return l2;
  }
  
  public static <T> T[] trimToSize(T[] paramArrayOfT, int paramInt)
  {
    if ((paramArrayOfT != null) && (paramInt != 0))
    {
      if (paramArrayOfT.length == paramInt) {
        return paramArrayOfT;
      }
      return Arrays.copyOf(paramArrayOfT, paramInt);
    }
    return null;
  }
  
  public static <T> int unstableRemoveIf(ArrayList<T> paramArrayList, Predicate<T> paramPredicate)
  {
    if (paramArrayList == null) {
      return 0;
    }
    int i = paramArrayList.size();
    int j = 0;
    int m;
    for (int k = i - 1;; k = m - 1)
    {
      m = j;
      if (j > k) {
        break;
      }
      for (;;)
      {
        m = k;
        if (j >= i) {
          break;
        }
        m = k;
        if (paramPredicate.test(paramArrayList.get(j))) {
          break;
        }
        j++;
      }
      while ((m > j) && (paramPredicate.test(paramArrayList.get(m)))) {
        m--;
      }
      if (j >= m)
      {
        m = j;
        break;
      }
      Collections.swap(paramArrayList, j, m);
      j++;
    }
    for (j = i - 1; j >= m; j--) {
      paramArrayList.remove(j);
    }
    return i - m;
  }
}
