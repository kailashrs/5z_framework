package com.android.internal.app.procstats;

import android.os.Build;
import android.os.Parcel;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.util.GrowingArrayUtils;
import java.util.ArrayList;
import libcore.util.EmptyArray;

public class SparseMappingTable
{
  private static final int ARRAY_MASK = 255;
  private static final int ARRAY_SHIFT = 8;
  public static final int ARRAY_SIZE = 4096;
  private static final int ID_MASK = 255;
  private static final int ID_SHIFT = 0;
  private static final int INDEX_MASK = 65535;
  private static final int INDEX_SHIFT = 16;
  public static final int INVALID_KEY = -1;
  private static final String TAG = "SparseMappingTable";
  private final ArrayList<long[]> mLongs = new ArrayList();
  private int mNextIndex;
  private int mSequence;
  
  public SparseMappingTable()
  {
    mLongs.add(new long['က']);
  }
  
  public static int getArrayFromKey(int paramInt)
  {
    return paramInt >> 8 & 0xFF;
  }
  
  public static byte getIdFromKey(int paramInt)
  {
    return (byte)(paramInt >> 0 & 0xFF);
  }
  
  public static int getIndexFromKey(int paramInt)
  {
    return paramInt >> 16 & 0xFFFF;
  }
  
  private static void logOrThrow(String paramString)
  {
    logOrThrow(paramString, new RuntimeException("Stack trace"));
  }
  
  private static void logOrThrow(String paramString, Throwable paramThrowable)
  {
    Slog.e("SparseMappingTable", paramString, paramThrowable);
    if (!Build.IS_ENG) {
      return;
    }
    throw new RuntimeException(paramString, paramThrowable);
  }
  
  private static void readCompactedLongArray(Parcel paramParcel, long[] paramArrayOfLong, int paramInt)
  {
    int i = paramArrayOfLong.length;
    if (paramInt > i)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("bad array lengths: got ");
      paramParcel.append(paramInt);
      paramParcel.append(" array is ");
      paramParcel.append(i);
      logOrThrow(paramParcel.toString());
      return;
    }
    int k;
    for (int j = 0;; j++)
    {
      k = j;
      if (j >= paramInt) {
        break;
      }
      k = paramParcel.readInt();
      if (k >= 0)
      {
        paramArrayOfLong[j] = k;
      }
      else
      {
        int m = paramParcel.readInt();
        paramArrayOfLong[j] = (k << 32 | m);
      }
    }
    while (k < i)
    {
      paramArrayOfLong[k] = 0L;
      k++;
    }
  }
  
  private static void writeCompactedLongArray(Parcel paramParcel, long[] paramArrayOfLong, int paramInt)
  {
    for (int i = 0; i < paramInt; i++)
    {
      long l1 = paramArrayOfLong[i];
      long l2 = l1;
      if (l1 < 0L)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Time val negative: ");
        localStringBuilder.append(l1);
        Slog.w("SparseMappingTable", localStringBuilder.toString());
        l2 = 0L;
      }
      if (l2 <= 2147483647L)
      {
        paramParcel.writeInt((int)l2);
      }
      else
      {
        int j = (int)(0x7FFFFFFF & l2 >> 32);
        int k = (int)(0xFFFFFFFF & l2);
        paramParcel.writeInt(j);
        paramParcel.writeInt(k);
      }
    }
  }
  
  public String dumpInternalState(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SparseMappingTable{");
    localStringBuilder.append("mSequence=");
    localStringBuilder.append(mSequence);
    localStringBuilder.append(" mNextIndex=");
    localStringBuilder.append(mNextIndex);
    localStringBuilder.append(" mLongs.size=");
    int i = mLongs.size();
    localStringBuilder.append(i);
    localStringBuilder.append("\n");
    if (paramBoolean) {
      for (int j = 0; j < i; j++)
      {
        long[] arrayOfLong = (long[])mLongs.get(j);
        for (int k = 0; (k < arrayOfLong.length) && ((j != i - 1) || (k != mNextIndex)); k++) {
          localStringBuilder.append(String.format(" %4d %d 0x%016x %-19d\n", new Object[] { Integer.valueOf(j), Integer.valueOf(k), Long.valueOf(arrayOfLong[k]), Long.valueOf(arrayOfLong[k]) }));
        }
      }
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mSequence = paramParcel.readInt();
    mNextIndex = paramParcel.readInt();
    mLongs.clear();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      int k = paramParcel.readInt();
      long[] arrayOfLong = new long[k];
      readCompactedLongArray(paramParcel, arrayOfLong, k);
      mLongs.add(arrayOfLong);
    }
    if ((i > 0) && (((long[])mLongs.get(i - 1)).length != mNextIndex))
    {
      EventLog.writeEvent(1397638484, new Object[] { "73252178", Integer.valueOf(-1), "" });
      paramParcel = new StringBuilder();
      paramParcel.append("Expected array of length ");
      paramParcel.append(mNextIndex);
      paramParcel.append(" but was ");
      paramParcel.append(((long[])mLongs.get(i - 1)).length);
      throw new IllegalStateException(paramParcel.toString());
    }
  }
  
  public void reset()
  {
    mLongs.clear();
    mLongs.add(new long['က']);
    mNextIndex = 0;
    mSequence += 1;
  }
  
  public void writeToParcel(Parcel paramParcel)
  {
    paramParcel.writeInt(mSequence);
    paramParcel.writeInt(mNextIndex);
    int i = mLongs.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i - 1; j++)
    {
      arrayOfLong = (long[])mLongs.get(j);
      paramParcel.writeInt(arrayOfLong.length);
      writeCompactedLongArray(paramParcel, arrayOfLong, arrayOfLong.length);
    }
    long[] arrayOfLong = (long[])mLongs.get(i - 1);
    paramParcel.writeInt(mNextIndex);
    writeCompactedLongArray(paramParcel, arrayOfLong, mNextIndex);
  }
  
  public static class Table
  {
    private SparseMappingTable mParent;
    private int mSequence = 1;
    private int mSize;
    private int[] mTable;
    
    public Table(SparseMappingTable paramSparseMappingTable)
    {
      mParent = paramSparseMappingTable;
      mSequence = mSequence;
    }
    
    private void assertConsistency() {}
    
    private int binarySearch(byte paramByte)
    {
      int i = 0;
      int j = mSize - 1;
      while (i <= j)
      {
        int k = i + j >>> 1;
        byte b = (byte)(mTable[k] >> 0 & 0xFF);
        if (b < paramByte)
        {
          i = k + 1;
        }
        else
        {
          if (b <= paramByte) {
            break label65;
          }
          j = k - 1;
        }
        continue;
        label65:
        return k;
      }
      return i;
    }
    
    private boolean validateKeys(boolean paramBoolean)
    {
      Object localObject = mParent.mLongs;
      int i = ((ArrayList)localObject).size();
      int j = mSize;
      int k = 0;
      while (k < j)
      {
        int m = mTable[k];
        int n = SparseMappingTable.getArrayFromKey(m);
        m = SparseMappingTable.getIndexFromKey(m);
        if ((n < i) && (m < ((long[])((ArrayList)localObject).get(n)).length))
        {
          k++;
        }
        else
        {
          if (paramBoolean)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Invalid stats at index ");
            ((StringBuilder)localObject).append(k);
            ((StringBuilder)localObject).append(" -- ");
            ((StringBuilder)localObject).append(dumpInternalState());
            Slog.w("SparseMappingTable", ((StringBuilder)localObject).toString());
          }
          return false;
        }
      }
      return true;
    }
    
    public void copyFrom(Table paramTable, int paramInt)
    {
      mTable = null;
      int i = 0;
      mSize = 0;
      int j = paramTable.getKeyCount();
      while (i < j)
      {
        int k = paramTable.getKeyAt(i);
        long[] arrayOfLong1 = (long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(k));
        int m = getOrAddKey(SparseMappingTable.getIdFromKey(k), paramInt);
        long[] arrayOfLong2 = (long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(m));
        System.arraycopy(arrayOfLong1, SparseMappingTable.getIndexFromKey(k), arrayOfLong2, SparseMappingTable.getIndexFromKey(m), paramInt);
        i++;
      }
    }
    
    public String dumpInternalState()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SparseMappingTable.Table{mSequence=");
      localStringBuilder.append(mSequence);
      localStringBuilder.append(" mParent.mSequence=");
      localStringBuilder.append(mParent.mSequence);
      localStringBuilder.append(" mParent.mLongs.size()=");
      localStringBuilder.append(mParent.mLongs.size());
      localStringBuilder.append(" mSize=");
      localStringBuilder.append(mSize);
      localStringBuilder.append(" mTable=");
      if (mTable == null)
      {
        localStringBuilder.append("null");
      }
      else
      {
        int i = mTable.length;
        localStringBuilder.append('[');
        for (int j = 0; j < i; j++)
        {
          int k = mTable[j];
          localStringBuilder.append("0x");
          localStringBuilder.append(Integer.toHexString(k >> 0 & 0xFF));
          localStringBuilder.append("/0x");
          localStringBuilder.append(Integer.toHexString(k >> 8 & 0xFF));
          localStringBuilder.append("/0x");
          localStringBuilder.append(Integer.toHexString(k >> 16 & 0xFFFF));
          if (j != i - 1) {
            localStringBuilder.append(", ");
          }
        }
        localStringBuilder.append(']');
      }
      localStringBuilder.append(" clazz=");
      localStringBuilder.append(getClass().getName());
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public long[] getArrayForKey(int paramInt)
    {
      assertConsistency();
      return (long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(paramInt));
    }
    
    public int getKey(byte paramByte)
    {
      assertConsistency();
      int i = binarySearch(paramByte);
      if (i >= 0) {
        return mTable[i];
      }
      return -1;
    }
    
    public int getKeyAt(int paramInt)
    {
      return mTable[paramInt];
    }
    
    public int getKeyCount()
    {
      return mSize;
    }
    
    public int getOrAddKey(byte paramByte, int paramInt)
    {
      assertConsistency();
      int i = binarySearch(paramByte);
      if (i >= 0) {
        return mTable[i];
      }
      ArrayList localArrayList = mParent.mLongs;
      int j = localArrayList.size() - 1;
      Object localObject = (long[])localArrayList.get(j);
      int k = j;
      if (mParent.mNextIndex + paramInt > localObject.length)
      {
        localArrayList.add(new long['က']);
        k = j + 1;
        SparseMappingTable.access$202(mParent, 0);
      }
      k = k << 8 | mParent.mNextIndex << 16 | paramByte << 0;
      SparseMappingTable.access$212(mParent, paramInt);
      if (mTable != null) {
        localObject = mTable;
      } else {
        localObject = EmptyArray.INT;
      }
      mTable = GrowingArrayUtils.insert((int[])localObject, mSize, i, k);
      mSize += 1;
      return k;
    }
    
    public long getValue(int paramInt)
    {
      return getValue(paramInt, 0);
    }
    
    public long getValue(int paramInt1, int paramInt2)
    {
      assertConsistency();
      try
      {
        long l = ((long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(paramInt1)))[(SparseMappingTable.getIndexFromKey(paramInt1) + paramInt2)];
        return l;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("key=0x");
        localStringBuilder.append(Integer.toHexString(paramInt1));
        localStringBuilder.append(" index=");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" -- ");
        localStringBuilder.append(dumpInternalState());
        SparseMappingTable.logOrThrow(localStringBuilder.toString(), localIndexOutOfBoundsException);
      }
      return 0L;
    }
    
    public long getValueForId(byte paramByte)
    {
      return getValueForId(paramByte, 0);
    }
    
    public long getValueForId(byte paramByte, int paramInt)
    {
      assertConsistency();
      int i = binarySearch(paramByte);
      if (i >= 0)
      {
        int j = mTable[i];
        try
        {
          long l = ((long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(j)))[(SparseMappingTable.getIndexFromKey(j) + paramInt)];
          return l;
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("id=0x");
          localStringBuilder.append(Integer.toHexString(paramByte));
          localStringBuilder.append(" idx=");
          localStringBuilder.append(i);
          localStringBuilder.append(" key=0x");
          localStringBuilder.append(Integer.toHexString(j));
          localStringBuilder.append(" index=");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" -- ");
          localStringBuilder.append(dumpInternalState());
          SparseMappingTable.logOrThrow(localStringBuilder.toString(), localIndexOutOfBoundsException);
          return 0L;
        }
      }
      return 0L;
    }
    
    public boolean readFromParcel(Parcel paramParcel)
    {
      mSequence = paramParcel.readInt();
      mSize = paramParcel.readInt();
      if (mSize != 0)
      {
        mTable = new int[mSize];
        for (int i = 0; i < mSize; i++) {
          mTable[i] = paramParcel.readInt();
        }
      }
      mTable = null;
      if (validateKeys(true)) {
        return true;
      }
      mSize = 0;
      mTable = null;
      return false;
    }
    
    public void resetTable()
    {
      mTable = null;
      mSize = 0;
      mSequence = mParent.mSequence;
    }
    
    public void setValue(int paramInt1, int paramInt2, long paramLong)
    {
      assertConsistency();
      StringBuilder localStringBuilder;
      if (paramLong < 0L)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("can't store negative values key=0x");
        localStringBuilder.append(Integer.toHexString(paramInt1));
        localStringBuilder.append(" index=");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" value=");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" -- ");
        localStringBuilder.append(dumpInternalState());
        SparseMappingTable.logOrThrow(localStringBuilder.toString());
        return;
      }
      try
      {
        ((long[])mParent.mLongs.get(SparseMappingTable.getArrayFromKey(paramInt1)))[(SparseMappingTable.getIndexFromKey(paramInt1) + paramInt2)] = paramLong;
        return;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("key=0x");
        localStringBuilder.append(Integer.toHexString(paramInt1));
        localStringBuilder.append(" index=");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" value=");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" -- ");
        localStringBuilder.append(dumpInternalState());
        SparseMappingTable.logOrThrow(localStringBuilder.toString(), localIndexOutOfBoundsException);
      }
    }
    
    public void setValue(int paramInt, long paramLong)
    {
      setValue(paramInt, 0, paramLong);
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(mSequence);
      paramParcel.writeInt(mSize);
      for (int i = 0; i < mSize; i++) {
        paramParcel.writeInt(mTable[i]);
      }
    }
  }
}
