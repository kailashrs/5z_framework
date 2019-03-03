package android.database;

import android.content.res.Resources;
import android.database.sqlite.SQLiteClosable;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseIntArray;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.CloseGuard;

public class CursorWindow
  extends SQLiteClosable
  implements Parcelable
{
  public static final Parcelable.Creator<CursorWindow> CREATOR = new Parcelable.Creator()
  {
    public CursorWindow createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CursorWindow(paramAnonymousParcel, null);
    }
    
    public CursorWindow[] newArray(int paramAnonymousInt)
    {
      return new CursorWindow[paramAnonymousInt];
    }
  };
  private static final String STATS_TAG = "CursorWindowStats";
  private static int sCursorWindowSize = -1;
  private static final LongSparseArray<Integer> sWindowToPidMap = new LongSparseArray();
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final String mName;
  private int mStartPos;
  public long mWindowPtr;
  
  private CursorWindow(Parcel paramParcel)
  {
    mStartPos = paramParcel.readInt();
    mWindowPtr = nativeCreateFromParcel(paramParcel);
    if (mWindowPtr != 0L)
    {
      mName = nativeGetName(mWindowPtr);
      mCloseGuard.open("close");
      return;
    }
    throw new CursorWindowAllocationException("Cursor window could not be created from binder.");
  }
  
  public CursorWindow(String paramString)
  {
    this(paramString, getCursorWindowSize());
  }
  
  public CursorWindow(String paramString, long paramLong)
  {
    mStartPos = 0;
    if ((paramString == null) || (paramString.length() == 0)) {
      paramString = "<unnamed>";
    }
    mName = paramString;
    mWindowPtr = nativeCreate(mName, (int)paramLong);
    if (mWindowPtr != 0L)
    {
      mCloseGuard.open("close");
      recordNewWindow(Binder.getCallingPid(), mWindowPtr);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Cursor window allocation of ");
    paramString.append(paramLong);
    paramString.append(" bytes failed. ");
    paramString.append(printStats());
    throw new CursorWindowAllocationException(paramString.toString());
  }
  
  @Deprecated
  public CursorWindow(boolean paramBoolean)
  {
    this((String)null);
  }
  
  private void dispose()
  {
    if (mCloseGuard != null) {
      mCloseGuard.close();
    }
    if (mWindowPtr != 0L)
    {
      recordClosingOfWindow(mWindowPtr);
      nativeDispose(mWindowPtr);
      mWindowPtr = 0L;
    }
  }
  
  private static int getCursorWindowSize()
  {
    if (sCursorWindowSize < 0) {
      sCursorWindowSize = Resources.getSystem().getInteger(17694761) * 1024;
    }
    return sCursorWindowSize;
  }
  
  @FastNative
  private static native boolean nativeAllocRow(long paramLong);
  
  @FastNative
  private static native void nativeClear(long paramLong);
  
  private static native void nativeCopyStringToBuffer(long paramLong, int paramInt1, int paramInt2, CharArrayBuffer paramCharArrayBuffer);
  
  private static native long nativeCreate(String paramString, int paramInt);
  
  private static native long nativeCreateFromParcel(Parcel paramParcel);
  
  private static native void nativeDispose(long paramLong);
  
  @FastNative
  private static native void nativeFreeLastRow(long paramLong);
  
  private static native byte[] nativeGetBlob(long paramLong, int paramInt1, int paramInt2);
  
  @FastNative
  private static native double nativeGetDouble(long paramLong, int paramInt1, int paramInt2);
  
  @FastNative
  private static native long nativeGetLong(long paramLong, int paramInt1, int paramInt2);
  
  private static native String nativeGetName(long paramLong);
  
  @FastNative
  private static native int nativeGetNumRows(long paramLong);
  
  private static native String nativeGetString(long paramLong, int paramInt1, int paramInt2);
  
  @FastNative
  private static native int nativeGetType(long paramLong, int paramInt1, int paramInt2);
  
  private static native boolean nativePutBlob(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  @FastNative
  private static native boolean nativePutDouble(long paramLong, double paramDouble, int paramInt1, int paramInt2);
  
  @FastNative
  private static native boolean nativePutLong(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  @FastNative
  private static native boolean nativePutNull(long paramLong, int paramInt1, int paramInt2);
  
  private static native boolean nativePutString(long paramLong, String paramString, int paramInt1, int paramInt2);
  
  @FastNative
  private static native boolean nativeSetNumColumns(long paramLong, int paramInt);
  
  private static native void nativeWriteToParcel(long paramLong, Parcel paramParcel);
  
  public static CursorWindow newFromParcel(Parcel paramParcel)
  {
    return (CursorWindow)CREATOR.createFromParcel(paramParcel);
  }
  
  private String printStats()
  {
    Object localObject1 = new StringBuilder();
    int i = Process.myPid();
    Object localObject3 = new SparseIntArray();
    synchronized (sWindowToPidMap)
    {
      int j = sWindowToPidMap.size();
      if (j == 0) {
        return "";
      }
      for (int k = 0; k < j; k++)
      {
        m = ((Integer)sWindowToPidMap.valueAt(k)).intValue();
        ((SparseIntArray)localObject3).put(m, ((SparseIntArray)localObject3).get(m) + 1);
      }
      int m = ((SparseIntArray)localObject3).size();
      k = 0;
      for (j = 0; j < m; j++)
      {
        ((StringBuilder)localObject1).append(" (# cursors opened by ");
        int n = ((SparseIntArray)localObject3).keyAt(j);
        if (n == i)
        {
          ((StringBuilder)localObject1).append("this proc=");
        }
        else
        {
          ??? = new StringBuilder();
          ((StringBuilder)???).append("pid ");
          ((StringBuilder)???).append(n);
          ((StringBuilder)???).append("=");
          ((StringBuilder)localObject1).append(((StringBuilder)???).toString());
        }
        n = ((SparseIntArray)localObject3).get(n);
        ??? = new StringBuilder();
        ((StringBuilder)???).append(n);
        ((StringBuilder)???).append(")");
        ((StringBuilder)localObject1).append(((StringBuilder)???).toString());
        k += n;
      }
      if (((StringBuilder)localObject1).length() > 980) {
        localObject1 = ((StringBuilder)localObject1).substring(0, 980);
      } else {
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("# Open Cursors=");
      ((StringBuilder)localObject3).append(k);
      ((StringBuilder)localObject3).append((String)localObject1);
      return ((StringBuilder)localObject3).toString();
    }
  }
  
  private void recordClosingOfWindow(long paramLong)
  {
    synchronized (sWindowToPidMap)
    {
      if (sWindowToPidMap.size() == 0) {
        return;
      }
      sWindowToPidMap.delete(paramLong);
      return;
    }
  }
  
  private void recordNewWindow(int paramInt, long paramLong)
  {
    synchronized (sWindowToPidMap)
    {
      sWindowToPidMap.put(paramLong, Integer.valueOf(paramInt));
      if (Log.isLoggable("CursorWindowStats", 2))
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Created a new Cursor. ");
        localStringBuilder.append(printStats());
        Log.i("CursorWindowStats", localStringBuilder.toString());
      }
      return;
    }
  }
  
  public boolean allocRow()
  {
    acquireReference();
    try
    {
      boolean bool = nativeAllocRow(mWindowPtr);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void clear()
  {
    acquireReference();
    try
    {
      mStartPos = 0;
      nativeClear(mWindowPtr);
      return;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void copyStringToBuffer(int paramInt1, int paramInt2, CharArrayBuffer paramCharArrayBuffer)
  {
    if (paramCharArrayBuffer != null)
    {
      acquireReference();
      try
      {
        nativeCopyStringToBuffer(mWindowPtr, paramInt1 - mStartPos, paramInt2, paramCharArrayBuffer);
        return;
      }
      finally
      {
        releaseReference();
      }
    }
    throw new IllegalArgumentException("CharArrayBuffer should not be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      dispose();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void freeLastRow()
  {
    acquireReference();
    try
    {
      nativeFreeLastRow(mWindowPtr);
      return;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public byte[] getBlob(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      byte[] arrayOfByte = nativeGetBlob(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return arrayOfByte;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public double getDouble(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      double d = nativeGetDouble(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return d;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public float getFloat(int paramInt1, int paramInt2)
  {
    return (float)getDouble(paramInt1, paramInt2);
  }
  
  public int getInt(int paramInt1, int paramInt2)
  {
    return (int)getLong(paramInt1, paramInt2);
  }
  
  public long getLong(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      long l = nativeGetLong(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return l;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getNumRows()
  {
    acquireReference();
    try
    {
      int i = nativeGetNumRows(mWindowPtr);
      return i;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public short getShort(int paramInt1, int paramInt2)
  {
    return (short)(int)getLong(paramInt1, paramInt2);
  }
  
  public int getStartPosition()
  {
    return mStartPos;
  }
  
  public String getString(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      String str = nativeGetString(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return str;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public int getType(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      paramInt1 = nativeGetType(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return paramInt1;
    }
    finally
    {
      releaseReference();
    }
  }
  
  @Deprecated
  public boolean isBlob(int paramInt1, int paramInt2)
  {
    paramInt1 = getType(paramInt1, paramInt2);
    boolean bool;
    if ((paramInt1 != 4) && (paramInt1 != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isFloat(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (getType(paramInt1, paramInt2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isLong(int paramInt1, int paramInt2)
  {
    paramInt1 = getType(paramInt1, paramInt2);
    boolean bool = true;
    if (paramInt1 != 1) {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isNull(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (getType(paramInt1, paramInt2) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isString(int paramInt1, int paramInt2)
  {
    paramInt1 = getType(paramInt1, paramInt2);
    boolean bool;
    if ((paramInt1 != 3) && (paramInt1 != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected void onAllReferencesReleased()
  {
    dispose();
  }
  
  public boolean putBlob(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      boolean bool = nativePutBlob(mWindowPtr, paramArrayOfByte, paramInt1 - mStartPos, paramInt2);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public boolean putDouble(double paramDouble, int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      boolean bool = nativePutDouble(mWindowPtr, paramDouble, paramInt1 - mStartPos, paramInt2);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public boolean putLong(long paramLong, int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      boolean bool = nativePutLong(mWindowPtr, paramLong, paramInt1 - mStartPos, paramInt2);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public boolean putNull(int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      boolean bool = nativePutNull(mWindowPtr, paramInt1 - mStartPos, paramInt2);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public boolean putString(String paramString, int paramInt1, int paramInt2)
  {
    acquireReference();
    try
    {
      boolean bool = nativePutString(mWindowPtr, paramString, paramInt1 - mStartPos, paramInt2);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public boolean setNumColumns(int paramInt)
  {
    acquireReference();
    try
    {
      boolean bool = nativeSetNumColumns(mWindowPtr, paramInt);
      return bool;
    }
    finally
    {
      releaseReference();
    }
  }
  
  public void setStartPosition(int paramInt)
  {
    mStartPos = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getName());
    localStringBuilder.append(" {");
    localStringBuilder.append(Long.toHexString(mWindowPtr));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    acquireReference();
    try
    {
      paramParcel.writeInt(mStartPos);
      nativeWriteToParcel(mWindowPtr, paramParcel);
      releaseReference();
      if ((paramInt & 0x1) != 0) {}
      return;
    }
    finally
    {
      releaseReference();
    }
  }
}
