package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class BluetoothLeUtils
{
  public BluetoothLeUtils() {}
  
  static void checkAdapterStateOn(BluetoothAdapter paramBluetoothAdapter)
  {
    if ((paramBluetoothAdapter != null) && (paramBluetoothAdapter.isLeEnabled())) {
      return;
    }
    throw new IllegalStateException("BT Adapter is not turned ON");
  }
  
  static boolean equals(SparseArray<byte[]> paramSparseArray1, SparseArray<byte[]> paramSparseArray2)
  {
    if (paramSparseArray1 == paramSparseArray2) {
      return true;
    }
    if ((paramSparseArray1 != null) && (paramSparseArray2 != null))
    {
      if (paramSparseArray1.size() != paramSparseArray2.size()) {
        return false;
      }
      int i = 0;
      while (i < paramSparseArray1.size()) {
        if ((paramSparseArray1.keyAt(i) == paramSparseArray2.keyAt(i)) && (Arrays.equals((byte[])paramSparseArray1.valueAt(i), (byte[])paramSparseArray2.valueAt(i)))) {
          i++;
        } else {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  static <T> boolean equals(Map<T, byte[]> paramMap1, Map<T, byte[]> paramMap2)
  {
    if (paramMap1 == paramMap2) {
      return true;
    }
    if ((paramMap1 != null) && (paramMap2 != null))
    {
      if (paramMap1.size() != paramMap2.size()) {
        return false;
      }
      Object localObject = paramMap1.keySet();
      if (!((Set)localObject).equals(paramMap2.keySet())) {
        return false;
      }
      Iterator localIterator = ((Set)localObject).iterator();
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        if (!Objects.deepEquals(paramMap1.get(localObject), paramMap2.get(localObject))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  static <T> boolean equals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte1 == paramArrayOfByte2) {
      return true;
    }
    if ((paramArrayOfByte1 != null) && (paramArrayOfByte2 != null))
    {
      if (paramArrayOfByte1.length != paramArrayOfByte2.length) {
        return false;
      }
      return Objects.deepEquals(paramArrayOfByte1, paramArrayOfByte2);
    }
    return false;
  }
  
  static String toString(SparseArray<byte[]> paramSparseArray)
  {
    if (paramSparseArray == null) {
      return "null";
    }
    if (paramSparseArray.size() == 0) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('{');
    for (int i = 0; i < paramSparseArray.size(); i++)
    {
      localStringBuilder.append(paramSparseArray.keyAt(i));
      localStringBuilder.append("=");
      localStringBuilder.append(Arrays.toString((byte[])paramSparseArray.valueAt(i)));
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  static <T> String toString(Map<T, byte[]> paramMap)
  {
    if (paramMap == null) {
      return "null";
    }
    if (paramMap.isEmpty()) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('{');
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = ((Map.Entry)localIterator.next()).getKey();
      localStringBuilder.append(localObject);
      localStringBuilder.append("=");
      localStringBuilder.append(Arrays.toString((byte[])paramMap.get(localObject)));
      if (localIterator.hasNext()) {
        localStringBuilder.append(", ");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  static <T> String toString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return "null";
    }
    if (paramArrayOfByte.length == 0) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('{');
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      localStringBuilder.append(paramArrayOfByte[i]);
      if (i + 1 < paramArrayOfByte.length) {
        localStringBuilder.append(", ");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
