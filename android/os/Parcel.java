package android.os;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.ExceptionUtils;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.VMRuntime;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import libcore.util.SneakyThrow;

public final class Parcel
{
  private static final boolean DEBUG_ARRAY_MAP = false;
  private static final boolean DEBUG_RECYCLE = false;
  private static final int EX_BAD_PARCELABLE = -2;
  private static final int EX_HAS_REPLY_HEADER = -128;
  private static final int EX_ILLEGAL_ARGUMENT = -3;
  private static final int EX_ILLEGAL_STATE = -5;
  private static final int EX_NETWORK_MAIN_THREAD = -6;
  private static final int EX_NULL_POINTER = -4;
  private static final int EX_PARCELABLE = -9;
  private static final int EX_SECURITY = -1;
  private static final int EX_SERVICE_SPECIFIC = -8;
  private static final int EX_TRANSACTION_FAILED = -129;
  private static final int EX_UNSUPPORTED_OPERATION = -7;
  private static final int POOL_SIZE = 6;
  public static final Parcelable.Creator<String> STRING_CREATOR = new Parcelable.Creator()
  {
    public String createFromParcel(Parcel paramAnonymousParcel)
    {
      return paramAnonymousParcel.readString();
    }
    
    public String[] newArray(int paramAnonymousInt)
    {
      return new String[paramAnonymousInt];
    }
  };
  private static final String TAG = "Parcel";
  private static final int VAL_BOOLEAN = 9;
  private static final int VAL_BOOLEANARRAY = 23;
  private static final int VAL_BUNDLE = 3;
  private static final int VAL_BYTE = 20;
  private static final int VAL_BYTEARRAY = 13;
  private static final int VAL_CHARSEQUENCE = 10;
  private static final int VAL_CHARSEQUENCEARRAY = 24;
  private static final int VAL_DOUBLE = 8;
  private static final int VAL_DOUBLEARRAY = 28;
  private static final int VAL_FLOAT = 7;
  private static final int VAL_IBINDER = 15;
  private static final int VAL_INTARRAY = 18;
  private static final int VAL_INTEGER = 1;
  private static final int VAL_LIST = 11;
  private static final int VAL_LONG = 6;
  private static final int VAL_LONGARRAY = 19;
  private static final int VAL_MAP = 2;
  private static final int VAL_NULL = -1;
  private static final int VAL_OBJECTARRAY = 17;
  private static final int VAL_PARCELABLE = 4;
  private static final int VAL_PARCELABLEARRAY = 16;
  private static final int VAL_PERSISTABLEBUNDLE = 25;
  private static final int VAL_SERIALIZABLE = 21;
  private static final int VAL_SHORT = 5;
  private static final int VAL_SIZE = 26;
  private static final int VAL_SIZEF = 27;
  private static final int VAL_SPARSEARRAY = 12;
  private static final int VAL_SPARSEBOOLEANARRAY = 22;
  private static final int VAL_STRING = 0;
  private static final int VAL_STRINGARRAY = 14;
  private static final int WRITE_EXCEPTION_STACK_TRACE_THRESHOLD_MS = 1000;
  private static final HashMap<ClassLoader, HashMap<String, Parcelable.Creator<?>>> mCreators = new HashMap();
  private static final Parcel[] sHolderPool;
  private static volatile long sLastWriteExceptionStackTrace;
  private static final Parcel[] sOwnedPool = new Parcel[6];
  private static boolean sParcelExceptionStackTrace;
  private ArrayMap<Class, Object> mClassCookies;
  private long mNativePtr;
  private long mNativeSize;
  private boolean mOwnsNativeParcelObject;
  private ReadWriteHelper mReadWriteHelper = ReadWriteHelper.DEFAULT;
  private RuntimeException mStack;
  
  static
  {
    sHolderPool = new Parcel[6];
  }
  
  private Parcel(long paramLong)
  {
    init(paramLong);
  }
  
  @Deprecated
  static native void closeFileDescriptor(FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private Exception createException(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    default: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown exception code: ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" msg ");
      localStringBuilder.append(paramString);
      return new RuntimeException(localStringBuilder.toString());
    case -1: 
      return new SecurityException(paramString);
    case -2: 
      return new BadParcelableException(paramString);
    case -3: 
      return new IllegalArgumentException(paramString);
    case -4: 
      return new NullPointerException(paramString);
    case -5: 
      return new IllegalStateException(paramString);
    case -6: 
      return new NetworkOnMainThreadException();
    case -7: 
      return new UnsupportedOperationException(paramString);
    case -8: 
      return new ServiceSpecificException(readInt(), paramString);
    }
    if (readInt() > 0) {
      return (Exception)readParcelable(Parcelable.class.getClassLoader());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [missing Parcelable]");
    return new RuntimeException(localStringBuilder.toString());
  }
  
  private void destroy()
  {
    if (mNativePtr != 0L)
    {
      if (mOwnsNativeParcelObject)
      {
        nativeDestroy(mNativePtr);
        updateNativeSize(0L);
      }
      mNativePtr = 0L;
    }
    mReadWriteHelper = null;
  }
  
  @Deprecated
  static native FileDescriptor dupFileDescriptor(FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private void freeBuffer()
  {
    if (mOwnsNativeParcelObject) {
      updateNativeSize(nativeFreeBuffer(mNativePtr));
    }
    mReadWriteHelper = ReadWriteHelper.DEFAULT;
  }
  
  public static native long getGlobalAllocCount();
  
  public static native long getGlobalAllocSize();
  
  private void init(long paramLong)
  {
    if (paramLong != 0L)
    {
      mNativePtr = paramLong;
      mOwnsNativeParcelObject = false;
    }
    else
    {
      mNativePtr = nativeCreate();
      mOwnsNativeParcelObject = true;
    }
  }
  
  private static native long nativeAppendFrom(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  private static native int nativeCompareData(long paramLong1, long paramLong2);
  
  private static native long nativeCreate();
  
  private static native byte[] nativeCreateByteArray(long paramLong);
  
  @CriticalNative
  private static native int nativeDataAvail(long paramLong);
  
  @CriticalNative
  private static native int nativeDataCapacity(long paramLong);
  
  @CriticalNative
  private static native int nativeDataPosition(long paramLong);
  
  @CriticalNative
  private static native int nativeDataSize(long paramLong);
  
  private static native void nativeDestroy(long paramLong);
  
  private static native void nativeEnforceInterface(long paramLong, String paramString);
  
  private static native long nativeFreeBuffer(long paramLong);
  
  @CriticalNative
  private static native long nativeGetBlobAshmemSize(long paramLong);
  
  @CriticalNative
  private static native boolean nativeHasFileDescriptors(long paramLong);
  
  private static native byte[] nativeMarshall(long paramLong);
  
  @CriticalNative
  private static native boolean nativePushAllowFds(long paramLong, boolean paramBoolean);
  
  private static native byte[] nativeReadBlob(long paramLong);
  
  private static native boolean nativeReadByteArray(long paramLong, byte[] paramArrayOfByte, int paramInt);
  
  @CriticalNative
  private static native double nativeReadDouble(long paramLong);
  
  private static native FileDescriptor nativeReadFileDescriptor(long paramLong);
  
  @CriticalNative
  private static native float nativeReadFloat(long paramLong);
  
  @CriticalNative
  private static native int nativeReadInt(long paramLong);
  
  @CriticalNative
  private static native long nativeReadLong(long paramLong);
  
  static native String nativeReadString(long paramLong);
  
  private static native IBinder nativeReadStrongBinder(long paramLong);
  
  @CriticalNative
  private static native void nativeRestoreAllowFds(long paramLong, boolean paramBoolean);
  
  @FastNative
  private static native void nativeSetDataCapacity(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeSetDataPosition(long paramLong, int paramInt);
  
  @FastNative
  private static native long nativeSetDataSize(long paramLong, int paramInt);
  
  private static native long nativeUnmarshall(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static native void nativeWriteBlob(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static native void nativeWriteByteArray(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  @FastNative
  private static native void nativeWriteDouble(long paramLong, double paramDouble);
  
  private static native long nativeWriteFileDescriptor(long paramLong, FileDescriptor paramFileDescriptor);
  
  @FastNative
  private static native void nativeWriteFloat(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nativeWriteInt(long paramLong, int paramInt);
  
  private static native void nativeWriteInterfaceToken(long paramLong, String paramString);
  
  @FastNative
  private static native void nativeWriteLong(long paramLong1, long paramLong2);
  
  static native void nativeWriteString(long paramLong, String paramString);
  
  private static native void nativeWriteStrongBinder(long paramLong, IBinder paramIBinder);
  
  public static Parcel obtain()
  {
    Parcel[] arrayOfParcel = sOwnedPool;
    for (int i = 0;; i++)
    {
      if (i >= 6) {
        break label47;
      }
      Parcel localParcel = arrayOfParcel[i];
      if (localParcel != null) {
        arrayOfParcel[i] = null;
      }
      try
      {
        mReadWriteHelper = ReadWriteHelper.DEFAULT;
        return localParcel;
      }
      finally
      {
        break label58;
      }
    }
    label47:
    return new Parcel(0L);
    label58:
    throw localObject;
  }
  
  protected static final Parcel obtain(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  protected static final Parcel obtain(long paramLong)
  {
    Parcel[] arrayOfParcel = sHolderPool;
    for (int i = 0;; i++)
    {
      if (i >= 6) {
        break label50;
      }
      Parcel localParcel = arrayOfParcel[i];
      if (localParcel != null) {
        arrayOfParcel[i] = null;
      }
      try
      {
        localParcel.init(paramLong);
        return localParcel;
      }
      finally
      {
        break label61;
      }
    }
    label50:
    return new Parcel(paramLong);
    label61:
    throw localObject;
  }
  
  @Deprecated
  static native FileDescriptor openFileDescriptor(String paramString, int paramInt)
    throws FileNotFoundException;
  
  private void readArrayInternal(Object[] paramArrayOfObject, int paramInt, ClassLoader paramClassLoader)
  {
    for (int i = 0; i < paramInt; i++) {
      paramArrayOfObject[i] = readValue(paramClassLoader);
    }
  }
  
  private void readListInternal(List paramList, int paramInt, ClassLoader paramClassLoader)
  {
    while (paramInt > 0)
    {
      paramList.add(readValue(paramClassLoader));
      paramInt--;
    }
  }
  
  private final Serializable readSerializable(ClassLoader paramClassLoader)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(createByteArray());
    try
    {
      localObject = new android/os/Parcel$2;
      ((2)localObject).<init>(this, localByteArrayInputStream, paramClassLoader);
      paramClassLoader = (Serializable)((ObjectInputStream)localObject).readObject();
      return paramClassLoader;
    }
    catch (ClassNotFoundException paramClassLoader)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Parcelable encountered ClassNotFoundException reading a Serializable object (name = ");
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append(")");
      throw new RuntimeException(((StringBuilder)localObject).toString(), paramClassLoader);
    }
    catch (IOException paramClassLoader)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Parcelable encountered IOException reading a Serializable object (name = ");
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append(")");
      throw new RuntimeException(((StringBuilder)localObject).toString(), paramClassLoader);
    }
  }
  
  private void readSparseArrayInternal(SparseArray paramSparseArray, int paramInt, ClassLoader paramClassLoader)
  {
    while (paramInt > 0)
    {
      paramSparseArray.append(readInt(), readValue(paramClassLoader));
      paramInt--;
    }
  }
  
  private void readSparseBooleanArrayInternal(SparseBooleanArray paramSparseBooleanArray, int paramInt)
  {
    while (paramInt > 0)
    {
      int i = readInt();
      int j = readByte();
      boolean bool = true;
      if (j != 1) {
        bool = false;
      }
      paramSparseBooleanArray.append(i, bool);
      paramInt--;
    }
  }
  
  private void readSparseIntArrayInternal(SparseIntArray paramSparseIntArray, int paramInt)
  {
    while (paramInt > 0)
    {
      paramSparseIntArray.append(readInt(), readInt());
      paramInt--;
    }
  }
  
  public static void setStackTraceParceling(boolean paramBoolean)
  {
    sParcelExceptionStackTrace = paramBoolean;
  }
  
  private void updateNativeSize(long paramLong)
  {
    if (mOwnsNativeParcelObject)
    {
      long l = paramLong;
      if (paramLong > 2147483647L) {
        l = 2147483647L;
      }
      if (l != mNativeSize)
      {
        int i = (int)(l - mNativeSize);
        if (i > 0) {
          VMRuntime.getRuntime().registerNativeAllocation(i);
        } else {
          VMRuntime.getRuntime().registerNativeFree(-i);
        }
        mNativeSize = l;
      }
    }
  }
  
  public final void adoptClassCookies(Parcel paramParcel)
  {
    mClassCookies = mClassCookies;
  }
  
  public final void appendFrom(Parcel paramParcel, int paramInt1, int paramInt2)
  {
    updateNativeSize(nativeAppendFrom(mNativePtr, mNativePtr, paramInt1, paramInt2));
  }
  
  public final int compareData(Parcel paramParcel)
  {
    return nativeCompareData(mNativePtr, mNativePtr);
  }
  
  public Map<Class, Object> copyClassCookies()
  {
    return new ArrayMap(mClassCookies);
  }
  
  public final IBinder[] createBinderArray()
  {
    int i = readInt();
    if (i >= 0)
    {
      IBinder[] arrayOfIBinder = new IBinder[i];
      for (int j = 0; j < i; j++) {
        arrayOfIBinder[j] = readStrongBinder();
      }
      return arrayOfIBinder;
    }
    return null;
  }
  
  public final ArrayList<IBinder> createBinderArrayList()
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    while (i > 0)
    {
      localArrayList.add(readStrongBinder());
      i--;
    }
    return localArrayList;
  }
  
  public final boolean[] createBooleanArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 2))
    {
      boolean[] arrayOfBoolean = new boolean[i];
      for (int j = 0; j < i; j++)
      {
        int k;
        if (readInt() != 0) {
          k = 1;
        } else {
          k = 0;
        }
        arrayOfBoolean[j] = k;
      }
      return arrayOfBoolean;
    }
    return null;
  }
  
  public final byte[] createByteArray()
  {
    return nativeCreateByteArray(mNativePtr);
  }
  
  public final char[] createCharArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 2))
    {
      char[] arrayOfChar = new char[i];
      for (int j = 0; j < i; j++) {
        arrayOfChar[j] = ((char)(char)readInt());
      }
      return arrayOfChar;
    }
    return null;
  }
  
  public final double[] createDoubleArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 3))
    {
      double[] arrayOfDouble = new double[i];
      for (int j = 0; j < i; j++) {
        arrayOfDouble[j] = readDouble();
      }
      return arrayOfDouble;
    }
    return null;
  }
  
  public final float[] createFloatArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 2))
    {
      float[] arrayOfFloat = new float[i];
      for (int j = 0; j < i; j++) {
        arrayOfFloat[j] = readFloat();
      }
      return arrayOfFloat;
    }
    return null;
  }
  
  public final int[] createIntArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 2))
    {
      int[] arrayOfInt = new int[i];
      for (int j = 0; j < i; j++) {
        arrayOfInt[j] = readInt();
      }
      return arrayOfInt;
    }
    return null;
  }
  
  public final long[] createLongArray()
  {
    int i = readInt();
    if ((i >= 0) && (i <= dataAvail() >> 3))
    {
      long[] arrayOfLong = new long[i];
      for (int j = 0; j < i; j++) {
        arrayOfLong[j] = readLong();
      }
      return arrayOfLong;
    }
    return null;
  }
  
  public final FileDescriptor[] createRawFileDescriptorArray()
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    FileDescriptor[] arrayOfFileDescriptor = new FileDescriptor[i];
    for (int j = 0; j < i; j++) {
      arrayOfFileDescriptor[j] = readRawFileDescriptor();
    }
    return arrayOfFileDescriptor;
  }
  
  public final String[] createStringArray()
  {
    int i = readInt();
    if (i >= 0)
    {
      String[] arrayOfString = new String[i];
      for (int j = 0; j < i; j++) {
        arrayOfString[j] = readString();
      }
      return arrayOfString;
    }
    return null;
  }
  
  public final ArrayList<String> createStringArrayList()
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    while (i > 0)
    {
      localArrayList.add(readString());
      i--;
    }
    return localArrayList;
  }
  
  public final <T> T[] createTypedArray(Parcelable.Creator<T> paramCreator)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    Object[] arrayOfObject = paramCreator.newArray(i);
    for (int j = 0; j < i; j++) {
      arrayOfObject[j] = readTypedObject(paramCreator);
    }
    return arrayOfObject;
  }
  
  public final <T> ArrayList<T> createTypedArrayList(Parcelable.Creator<T> paramCreator)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    while (i > 0)
    {
      localArrayList.add(readTypedObject(paramCreator));
      i--;
    }
    return localArrayList;
  }
  
  public final int dataAvail()
  {
    return nativeDataAvail(mNativePtr);
  }
  
  public final int dataCapacity()
  {
    return nativeDataCapacity(mNativePtr);
  }
  
  public final int dataPosition()
  {
    return nativeDataPosition(mNativePtr);
  }
  
  public final int dataSize()
  {
    return nativeDataSize(mNativePtr);
  }
  
  public final void enforceInterface(String paramString)
  {
    nativeEnforceInterface(mNativePtr, paramString);
  }
  
  protected void finalize()
    throws Throwable
  {
    destroy();
  }
  
  public long getBlobAshmemSize()
  {
    return nativeGetBlobAshmemSize(mNativePtr);
  }
  
  public final Object getClassCookie(Class paramClass)
  {
    if (mClassCookies != null) {
      paramClass = mClassCookies.get(paramClass);
    } else {
      paramClass = null;
    }
    return paramClass;
  }
  
  public final boolean hasFileDescriptors()
  {
    return nativeHasFileDescriptors(mNativePtr);
  }
  
  public boolean hasReadWriteHelper()
  {
    boolean bool;
    if ((mReadWriteHelper != null) && (mReadWriteHelper != ReadWriteHelper.DEFAULT)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final byte[] marshall()
  {
    return nativeMarshall(mNativePtr);
  }
  
  public final boolean pushAllowFds(boolean paramBoolean)
  {
    return nativePushAllowFds(mNativePtr, paramBoolean);
  }
  
  public void putClassCookies(Map<Class, Object> paramMap)
  {
    if (paramMap == null) {
      return;
    }
    if (mClassCookies == null) {
      mClassCookies = new ArrayMap();
    }
    mClassCookies.putAll(paramMap);
  }
  
  public final Object[] readArray(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    Object[] arrayOfObject = new Object[i];
    readArrayInternal(arrayOfObject, i, paramClassLoader);
    return arrayOfObject;
  }
  
  public final ArrayList readArrayList(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(i);
    readListInternal(localArrayList, i, paramClassLoader);
    return localArrayList;
  }
  
  public void readArrayMap(ArrayMap paramArrayMap, ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return;
    }
    readArrayMapInternal(paramArrayMap, i, paramClassLoader);
  }
  
  void readArrayMapInternal(ArrayMap paramArrayMap, int paramInt, ClassLoader paramClassLoader)
  {
    while (paramInt > 0)
    {
      paramArrayMap.append(readString(), readValue(paramClassLoader));
      paramInt--;
    }
    paramArrayMap.validate();
  }
  
  void readArrayMapSafelyInternal(ArrayMap paramArrayMap, int paramInt, ClassLoader paramClassLoader)
  {
    while (paramInt > 0)
    {
      paramArrayMap.put(readString(), readValue(paramClassLoader));
      paramInt--;
    }
  }
  
  public ArraySet<? extends Object> readArraySet(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    ArraySet localArraySet = new ArraySet(i);
    for (int j = 0; j < i; j++) {
      localArraySet.append(readValue(paramClassLoader));
    }
    return localArraySet;
  }
  
  public final void readBinderArray(IBinder[] paramArrayOfIBinder)
  {
    int i = readInt();
    if (i == paramArrayOfIBinder.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfIBinder[j] = readStrongBinder();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final void readBinderList(List<IBinder> paramList)
  {
    int i = paramList.size();
    int j = readInt();
    int m;
    for (int k = 0;; k++)
    {
      m = k;
      if (k >= i) {
        break;
      }
      m = k;
      if (k >= j) {
        break;
      }
      paramList.set(k, readStrongBinder());
    }
    for (;;)
    {
      k = m;
      if (m >= j) {
        break;
      }
      paramList.add(readStrongBinder());
      m++;
    }
    while (k < i)
    {
      paramList.remove(j);
      k++;
    }
  }
  
  public final byte[] readBlob()
  {
    return nativeReadBlob(mNativePtr);
  }
  
  public final boolean readBoolean()
  {
    boolean bool;
    if (readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final void readBooleanArray(boolean[] paramArrayOfBoolean)
  {
    int i = readInt();
    if (i == paramArrayOfBoolean.length)
    {
      for (int j = 0; j < i; j++)
      {
        int k;
        if (readInt() != 0) {
          k = 1;
        } else {
          k = 0;
        }
        paramArrayOfBoolean[j] = k;
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final Bundle readBundle()
  {
    return readBundle(null);
  }
  
  public final Bundle readBundle(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    Bundle localBundle = new Bundle(this, i);
    if (paramClassLoader != null) {
      localBundle.setClassLoader(paramClassLoader);
    }
    return localBundle;
  }
  
  public final byte readByte()
  {
    return (byte)(readInt() & 0xFF);
  }
  
  public final void readByteArray(byte[] paramArrayOfByte)
  {
    long l = mNativePtr;
    int i;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    } else {
      i = 0;
    }
    if (nativeReadByteArray(l, paramArrayOfByte, i)) {
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final void readCharArray(char[] paramArrayOfChar)
  {
    int i = readInt();
    if (i == paramArrayOfChar.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfChar[j] = ((char)(char)readInt());
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final CharSequence readCharSequence()
  {
    return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this);
  }
  
  public final CharSequence[] readCharSequenceArray()
  {
    Object localObject = null;
    int i = readInt();
    if (i >= 0)
    {
      CharSequence[] arrayOfCharSequence = new CharSequence[i];
      for (int j = 0;; j++)
      {
        localObject = arrayOfCharSequence;
        if (j >= i) {
          break;
        }
        arrayOfCharSequence[j] = readCharSequence();
      }
    }
    return localObject;
  }
  
  public final ArrayList<CharSequence> readCharSequenceList()
  {
    Object localObject = null;
    int i = readInt();
    if (i >= 0)
    {
      ArrayList localArrayList = new ArrayList(i);
      for (int j = 0;; j++)
      {
        localObject = localArrayList;
        if (j >= i) {
          break;
        }
        localArrayList.add(readCharSequence());
      }
    }
    return localObject;
  }
  
  public final <T extends Parcelable> T readCreator(Parcelable.Creator<?> paramCreator, ClassLoader paramClassLoader)
  {
    if ((paramCreator instanceof Parcelable.ClassLoaderCreator)) {
      return (Parcelable)((Parcelable.ClassLoaderCreator)paramCreator).createFromParcel(this, paramClassLoader);
    }
    return (Parcelable)paramCreator.createFromParcel(this);
  }
  
  public final double readDouble()
  {
    return nativeReadDouble(mNativePtr);
  }
  
  public final void readDoubleArray(double[] paramArrayOfDouble)
  {
    int i = readInt();
    if (i == paramArrayOfDouble.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfDouble[j] = readDouble();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final void readException()
  {
    int i = readExceptionCode();
    if (i != 0) {
      readException(i, readString());
    }
  }
  
  public final void readException(int paramInt, String paramString)
  {
    Object localObject1 = null;
    if (readInt() > 0) {
      localObject1 = readString();
    }
    paramString = createException(paramInt, paramString);
    if (localObject1 != null)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Remote stack trace:\n");
      ((StringBuilder)localObject2).append((String)localObject1);
      localObject1 = new RemoteException(((StringBuilder)localObject2).toString(), null, false, false);
      try
      {
        localObject2 = ExceptionUtils.getRootCause(paramString);
        if (localObject2 != null) {
          ((Throwable)localObject2).initCause((Throwable)localObject1);
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Cannot set cause ");
        ((StringBuilder)localObject2).append(localObject1);
        ((StringBuilder)localObject2).append(" for ");
        ((StringBuilder)localObject2).append(paramString);
        Log.e("Parcel", ((StringBuilder)localObject2).toString(), localRuntimeException);
      }
    }
    SneakyThrow.sneakyThrow(paramString);
  }
  
  public final int readExceptionCode()
  {
    int i = readInt();
    if (i == -128)
    {
      if (readInt() == 0) {
        Log.e("Parcel", "Unexpected zero-sized Parcel reply header.");
      } else {
        StrictMode.readAndHandleBinderCallViolations(this);
      }
      return 0;
    }
    return i;
  }
  
  public final ParcelFileDescriptor readFileDescriptor()
  {
    Object localObject = nativeReadFileDescriptor(mNativePtr);
    if (localObject != null) {
      localObject = new ParcelFileDescriptor((FileDescriptor)localObject);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public final float readFloat()
  {
    return nativeReadFloat(mNativePtr);
  }
  
  public final void readFloatArray(float[] paramArrayOfFloat)
  {
    int i = readInt();
    if (i == paramArrayOfFloat.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfFloat[j] = readFloat();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final HashMap readHashMap(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    HashMap localHashMap = new HashMap(i);
    readMapInternal(localHashMap, i, paramClassLoader);
    return localHashMap;
  }
  
  public final int readInt()
  {
    return nativeReadInt(mNativePtr);
  }
  
  public final void readIntArray(int[] paramArrayOfInt)
  {
    int i = readInt();
    if (i == paramArrayOfInt.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfInt[j] = readInt();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final void readList(List paramList, ClassLoader paramClassLoader)
  {
    readListInternal(paramList, readInt(), paramClassLoader);
  }
  
  public final long readLong()
  {
    return nativeReadLong(mNativePtr);
  }
  
  public final void readLongArray(long[] paramArrayOfLong)
  {
    int i = readInt();
    if (i == paramArrayOfLong.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfLong[j] = readLong();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final void readMap(Map paramMap, ClassLoader paramClassLoader)
  {
    readMapInternal(paramMap, readInt(), paramClassLoader);
  }
  
  void readMapInternal(Map paramMap, int paramInt, ClassLoader paramClassLoader)
  {
    while (paramInt > 0)
    {
      paramMap.put(readValue(paramClassLoader), readValue(paramClassLoader));
      paramInt--;
    }
  }
  
  public final <T extends Parcelable> T readParcelable(ClassLoader paramClassLoader)
  {
    Parcelable.Creator localCreator = readParcelableCreator(paramClassLoader);
    if (localCreator == null) {
      return null;
    }
    if ((localCreator instanceof Parcelable.ClassLoaderCreator)) {
      return (Parcelable)((Parcelable.ClassLoaderCreator)localCreator).createFromParcel(this, paramClassLoader);
    }
    return (Parcelable)localCreator.createFromParcel(this);
  }
  
  public final Parcelable[] readParcelableArray(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    Parcelable[] arrayOfParcelable = new Parcelable[i];
    for (int j = 0; j < i; j++) {
      arrayOfParcelable[j] = readParcelable(paramClassLoader);
    }
    return arrayOfParcelable;
  }
  
  public final <T extends Parcelable> T[] readParcelableArray(ClassLoader paramClassLoader, Class<T> paramClass)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    paramClass = (Parcelable[])Array.newInstance(paramClass, i);
    for (int j = 0; j < i; j++) {
      paramClass[j] = readParcelable(paramClassLoader);
    }
    return paramClass;
  }
  
  public final Parcelable.Creator<?> readParcelableCreator(ClassLoader paramClassLoader)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    synchronized (mCreators)
    {
      Object localObject1 = (HashMap)mCreators.get(paramClassLoader);
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = new java/util/HashMap;
        ((HashMap)localObject2).<init>();
        mCreators.put(paramClassLoader, localObject2);
      }
      Parcelable.Creator localCreator = (Parcelable.Creator)((HashMap)localObject2).get(str);
      localObject1 = localCreator;
      if (localCreator == null)
      {
        if (paramClassLoader == null) {
          try
          {
            paramClassLoader = getClass().getClassLoader();
          }
          catch (NoSuchFieldException paramClassLoader)
          {
            break label343;
          }
          catch (ClassNotFoundException localClassNotFoundException)
          {
            break label382;
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            break label457;
          }
        }
        paramClassLoader = Class.forName(str, false, paramClassLoader);
        Object localObject3;
        if (Parcelable.class.isAssignableFrom(paramClassLoader))
        {
          paramClassLoader = paramClassLoader.getField("CREATOR");
          if ((paramClassLoader.getModifiers() & 0x8) != 0)
          {
            if (Parcelable.Creator.class.isAssignableFrom(paramClassLoader.getType()))
            {
              localObject1 = (Parcelable.Creator)paramClassLoader.get(null);
              if (localObject1 != null)
              {
                localIllegalAccessException.put(str, localObject1);
              }
              else
              {
                paramClassLoader = new android/os/BadParcelableException;
                localObject3 = new java/lang/StringBuilder;
                ((StringBuilder)localObject3).<init>();
                ((StringBuilder)localObject3).append("Parcelable protocol requires a non-null Parcelable.Creator object called CREATOR on class ");
                ((StringBuilder)localObject3).append(str);
                paramClassLoader.<init>(((StringBuilder)localObject3).toString());
                throw paramClassLoader;
              }
            }
            else
            {
              localObject3 = new android/os/BadParcelableException;
              paramClassLoader = new java/lang/StringBuilder;
              paramClassLoader.<init>();
              paramClassLoader.append("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class ");
              paramClassLoader.append(str);
              ((BadParcelableException)localObject3).<init>(paramClassLoader.toString());
              throw ((Throwable)localObject3);
            }
          }
          else
          {
            paramClassLoader = new android/os/BadParcelableException;
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Parcelable protocol requires the CREATOR object to be static on class ");
            ((StringBuilder)localObject3).append(str);
            paramClassLoader.<init>(((StringBuilder)localObject3).toString());
            throw paramClassLoader;
          }
        }
        else
        {
          paramClassLoader = new android/os/BadParcelableException;
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("Parcelable protocol requires subclassing from Parcelable on class ");
          ((StringBuilder)localObject3).append(str);
          paramClassLoader.<init>(((StringBuilder)localObject3).toString());
          throw paramClassLoader;
          label343:
          localObject3 = new android/os/BadParcelableException;
          paramClassLoader = new java/lang/StringBuilder;
          paramClassLoader.<init>();
          paramClassLoader.append("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class ");
          paramClassLoader.append(str);
          ((BadParcelableException)localObject3).<init>(paramClassLoader.toString());
          throw ((Throwable)localObject3);
          label382:
          paramClassLoader = new java/lang/StringBuilder;
          paramClassLoader.<init>();
          paramClassLoader.append("Class not found when unmarshalling: ");
          paramClassLoader.append(str);
          Log.e("Parcel", paramClassLoader.toString(), (Throwable)localObject3);
          paramClassLoader = new android/os/BadParcelableException;
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("ClassNotFoundException when unmarshalling: ");
          ((StringBuilder)localObject3).append(str);
          paramClassLoader.<init>(((StringBuilder)localObject3).toString());
          throw paramClassLoader;
          label457:
          paramClassLoader = new java/lang/StringBuilder;
          paramClassLoader.<init>();
          paramClassLoader.append("Illegal access when unmarshalling: ");
          paramClassLoader.append(str);
          Log.e("Parcel", paramClassLoader.toString(), (Throwable)localObject3);
          localObject3 = new android/os/BadParcelableException;
          paramClassLoader = new java/lang/StringBuilder;
          paramClassLoader.<init>();
          paramClassLoader.append("IllegalAccessException when unmarshalling: ");
          paramClassLoader.append(str);
          ((BadParcelableException)localObject3).<init>(paramClassLoader.toString());
          throw ((Throwable)localObject3);
        }
      }
      return localObject1;
    }
  }
  
  public final <T extends Parcelable> List<T> readParcelableList(List<T> paramList, ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i == -1)
    {
      paramList.clear();
      return paramList;
    }
    int j = paramList.size();
    int m;
    for (int k = 0;; k++)
    {
      m = k;
      if (k >= j) {
        break;
      }
      m = k;
      if (k >= i) {
        break;
      }
      paramList.set(k, readParcelable(paramClassLoader));
    }
    for (;;)
    {
      k = m;
      if (m >= i) {
        break;
      }
      paramList.add(readParcelable(paramClassLoader));
      m++;
    }
    while (k < j)
    {
      paramList.remove(i);
      k++;
    }
    return paramList;
  }
  
  public final PersistableBundle readPersistableBundle()
  {
    return readPersistableBundle(null);
  }
  
  public final PersistableBundle readPersistableBundle(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    PersistableBundle localPersistableBundle = new PersistableBundle(this, i);
    if (paramClassLoader != null) {
      localPersistableBundle.setClassLoader(paramClassLoader);
    }
    return localPersistableBundle;
  }
  
  public final FileDescriptor readRawFileDescriptor()
  {
    return nativeReadFileDescriptor(mNativePtr);
  }
  
  public final void readRawFileDescriptorArray(FileDescriptor[] paramArrayOfFileDescriptor)
  {
    int i = readInt();
    if (i == paramArrayOfFileDescriptor.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfFileDescriptor[j] = readRawFileDescriptor();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final Serializable readSerializable()
  {
    return readSerializable(null);
  }
  
  public final Size readSize()
  {
    return new Size(readInt(), readInt());
  }
  
  public final SizeF readSizeF()
  {
    return new SizeF(readFloat(), readFloat());
  }
  
  public final SparseArray readSparseArray(ClassLoader paramClassLoader)
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    SparseArray localSparseArray = new SparseArray(i);
    readSparseArrayInternal(localSparseArray, i, paramClassLoader);
    return localSparseArray;
  }
  
  public final SparseBooleanArray readSparseBooleanArray()
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray(i);
    readSparseBooleanArrayInternal(localSparseBooleanArray, i);
    return localSparseBooleanArray;
  }
  
  public final SparseIntArray readSparseIntArray()
  {
    int i = readInt();
    if (i < 0) {
      return null;
    }
    SparseIntArray localSparseIntArray = new SparseIntArray(i);
    readSparseIntArrayInternal(localSparseIntArray, i);
    return localSparseIntArray;
  }
  
  public final String readString()
  {
    return mReadWriteHelper.readString(this);
  }
  
  public final void readStringArray(String[] paramArrayOfString)
  {
    int i = readInt();
    if (i == paramArrayOfString.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfString[j] = readString();
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  public final String[] readStringArray()
  {
    Object localObject = null;
    int i = readInt();
    if (i >= 0)
    {
      String[] arrayOfString = new String[i];
      for (int j = 0;; j++)
      {
        localObject = arrayOfString;
        if (j >= i) {
          break;
        }
        arrayOfString[j] = readString();
      }
    }
    return localObject;
  }
  
  public final void readStringList(List<String> paramList)
  {
    int i = paramList.size();
    int j = readInt();
    int m;
    for (int k = 0;; k++)
    {
      m = k;
      if (k >= i) {
        break;
      }
      m = k;
      if (k >= j) {
        break;
      }
      paramList.set(k, readString());
    }
    for (;;)
    {
      k = m;
      if (m >= j) {
        break;
      }
      paramList.add(readString());
      m++;
    }
    while (k < i)
    {
      paramList.remove(j);
      k++;
    }
  }
  
  public String readStringNoHelper()
  {
    return nativeReadString(mNativePtr);
  }
  
  public final IBinder readStrongBinder()
  {
    return nativeReadStrongBinder(mNativePtr);
  }
  
  public final <T> void readTypedArray(T[] paramArrayOfT, Parcelable.Creator<T> paramCreator)
  {
    int i = readInt();
    if (i == paramArrayOfT.length)
    {
      for (int j = 0; j < i; j++) {
        paramArrayOfT[j] = readTypedObject(paramCreator);
      }
      return;
    }
    throw new RuntimeException("bad array lengths");
  }
  
  @Deprecated
  public final <T> T[] readTypedArray(Parcelable.Creator<T> paramCreator)
  {
    return createTypedArray(paramCreator);
  }
  
  public final <T> void readTypedList(List<T> paramList, Parcelable.Creator<T> paramCreator)
  {
    int i = paramList.size();
    int j = readInt();
    int m;
    for (int k = 0;; k++)
    {
      m = k;
      if (k >= i) {
        break;
      }
      m = k;
      if (k >= j) {
        break;
      }
      paramList.set(k, readTypedObject(paramCreator));
    }
    for (;;)
    {
      k = m;
      if (m >= j) {
        break;
      }
      paramList.add(readTypedObject(paramCreator));
      m++;
    }
    while (k < i)
    {
      paramList.remove(j);
      k++;
    }
  }
  
  public final <T> T readTypedObject(Parcelable.Creator<T> paramCreator)
  {
    if (readInt() != 0) {
      return paramCreator.createFromParcel(this);
    }
    return null;
  }
  
  public final Object readValue(ClassLoader paramClassLoader)
  {
    int i = readInt();
    switch (i)
    {
    default: 
      int j = dataPosition();
      paramClassLoader = new StringBuilder();
      paramClassLoader.append("Parcel ");
      paramClassLoader.append(this);
      paramClassLoader.append(": Unmarshalling unknown type code ");
      paramClassLoader.append(i);
      paramClassLoader.append(" at offset ");
      paramClassLoader.append(j - 4);
      throw new RuntimeException(paramClassLoader.toString());
    case 28: 
      return createDoubleArray();
    case 27: 
      return readSizeF();
    case 26: 
      return readSize();
    case 25: 
      return readPersistableBundle(paramClassLoader);
    case 24: 
      return readCharSequenceArray();
    case 23: 
      return createBooleanArray();
    case 22: 
      return readSparseBooleanArray();
    case 21: 
      return readSerializable(paramClassLoader);
    case 20: 
      return Byte.valueOf(readByte());
    case 19: 
      return createLongArray();
    case 18: 
      return createIntArray();
    case 17: 
      return readArray(paramClassLoader);
    case 16: 
      return readParcelableArray(paramClassLoader);
    case 15: 
      return readStrongBinder();
    case 14: 
      return readStringArray();
    case 13: 
      return createByteArray();
    case 12: 
      return readSparseArray(paramClassLoader);
    case 11: 
      return readArrayList(paramClassLoader);
    case 10: 
      return readCharSequence();
    case 9: 
      i = readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      return Boolean.valueOf(bool);
    case 8: 
      return Double.valueOf(readDouble());
    case 7: 
      return Float.valueOf(readFloat());
    case 6: 
      return Long.valueOf(readLong());
    case 5: 
      return Short.valueOf((short)readInt());
    case 4: 
      return readParcelable(paramClassLoader);
    case 3: 
      return readBundle(paramClassLoader);
    case 2: 
      return readHashMap(paramClassLoader);
    case 1: 
      return Integer.valueOf(readInt());
    case 0: 
      return readString();
    }
    return null;
  }
  
  /* Error */
  public final void recycle()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 972	android/os/Parcel:freeBuffer	()V
    //   4: aload_0
    //   5: getfield 235	android/os/Parcel:mOwnsNativeParcelObject	Z
    //   8: ifeq +10 -> 18
    //   11: getstatic 129	android/os/Parcel:sOwnedPool	[Landroid/os/Parcel;
    //   14: astore_1
    //   15: goto +12 -> 27
    //   18: aload_0
    //   19: lconst_0
    //   20: putfield 156	android/os/Parcel:mNativePtr	J
    //   23: getstatic 131	android/os/Parcel:sHolderPool	[Landroid/os/Parcel;
    //   26: astore_1
    //   27: aload_1
    //   28: monitorenter
    //   29: iconst_0
    //   30: istore_2
    //   31: iload_2
    //   32: bipush 6
    //   34: if_icmpge +26 -> 60
    //   37: aload_1
    //   38: iload_2
    //   39: aaload
    //   40: ifnonnull +10 -> 50
    //   43: aload_1
    //   44: iload_2
    //   45: aload_0
    //   46: aastore
    //   47: aload_1
    //   48: monitorexit
    //   49: return
    //   50: iinc 2 1
    //   53: goto -22 -> 31
    //   56: astore_3
    //   57: goto +6 -> 63
    //   60: aload_1
    //   61: monitorexit
    //   62: return
    //   63: aload_1
    //   64: monitorexit
    //   65: aload_3
    //   66: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	Parcel
    //   14	50	1	arrayOfParcel	Parcel[]
    //   30	21	2	i	int
    //   56	10	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   47	49	56	finally
    //   60	62	56	finally
    //   63	65	56	finally
  }
  
  public final void restoreAllowFds(boolean paramBoolean)
  {
    nativeRestoreAllowFds(mNativePtr, paramBoolean);
  }
  
  public final void setClassCookie(Class paramClass, Object paramObject)
  {
    if (mClassCookies == null) {
      mClassCookies = new ArrayMap();
    }
    mClassCookies.put(paramClass, paramObject);
  }
  
  public final void setDataCapacity(int paramInt)
  {
    nativeSetDataCapacity(mNativePtr, paramInt);
  }
  
  public final void setDataPosition(int paramInt)
  {
    nativeSetDataPosition(mNativePtr, paramInt);
  }
  
  public final void setDataSize(int paramInt)
  {
    updateNativeSize(nativeSetDataSize(mNativePtr, paramInt));
  }
  
  public void setReadWriteHelper(ReadWriteHelper paramReadWriteHelper)
  {
    if (paramReadWriteHelper == null) {
      paramReadWriteHelper = ReadWriteHelper.DEFAULT;
    }
    mReadWriteHelper = paramReadWriteHelper;
  }
  
  public final void unmarshall(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    updateNativeSize(nativeUnmarshall(mNativePtr, paramArrayOfByte, paramInt1, paramInt2));
  }
  
  public final void writeArray(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramArrayOfObject.length;
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeValue(paramArrayOfObject[j]);
      j++;
    }
  }
  
  public void writeArrayMap(ArrayMap<String, Object> paramArrayMap)
  {
    writeArrayMapInternal(paramArrayMap);
  }
  
  void writeArrayMapInternal(ArrayMap<String, Object> paramArrayMap)
  {
    if (paramArrayMap == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramArrayMap.size();
    writeInt(i);
    for (int j = 0; j < i; j++)
    {
      writeString((String)paramArrayMap.keyAt(j));
      writeValue(paramArrayMap.valueAt(j));
    }
  }
  
  public void writeArraySet(ArraySet<? extends Object> paramArraySet)
  {
    int i;
    if (paramArraySet != null) {
      i = paramArraySet.size();
    } else {
      i = -1;
    }
    writeInt(i);
    for (int j = 0; j < i; j++) {
      writeValue(paramArraySet.valueAt(j));
    }
  }
  
  public final void writeBinderArray(IBinder[] paramArrayOfIBinder)
  {
    if (paramArrayOfIBinder != null)
    {
      int i = paramArrayOfIBinder.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeStrongBinder(paramArrayOfIBinder[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeBinderList(List<IBinder> paramList)
  {
    if (paramList == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramList.size();
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeStrongBinder((IBinder)paramList.get(j));
      j++;
    }
  }
  
  public final void writeBlob(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    } else {
      i = 0;
    }
    writeBlob(paramArrayOfByte, 0, i);
  }
  
  public final void writeBlob(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null)
    {
      writeInt(-1);
      return;
    }
    Arrays.checkOffsetAndCount(paramArrayOfByte.length, paramInt1, paramInt2);
    nativeWriteBlob(mNativePtr, paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public final void writeBoolean(boolean paramBoolean)
  {
    writeInt(paramBoolean);
  }
  
  public final void writeBooleanArray(boolean[] paramArrayOfBoolean)
  {
    if (paramArrayOfBoolean != null)
    {
      int i = paramArrayOfBoolean.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeInt(paramArrayOfBoolean[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeBundle(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      writeInt(-1);
      return;
    }
    paramBundle.writeToParcel(this, 0);
  }
  
  public final void writeByte(byte paramByte)
  {
    writeInt(paramByte);
  }
  
  public final void writeByteArray(byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    } else {
      i = 0;
    }
    writeByteArray(paramArrayOfByte, 0, i);
  }
  
  public final void writeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null)
    {
      writeInt(-1);
      return;
    }
    Arrays.checkOffsetAndCount(paramArrayOfByte.length, paramInt1, paramInt2);
    nativeWriteByteArray(mNativePtr, paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public final void writeCharArray(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar != null)
    {
      int i = paramArrayOfChar.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeInt(paramArrayOfChar[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeCharSequence(CharSequence paramCharSequence)
  {
    TextUtils.writeToParcel(paramCharSequence, this, 0);
  }
  
  public final void writeCharSequenceArray(CharSequence[] paramArrayOfCharSequence)
  {
    if (paramArrayOfCharSequence != null)
    {
      int i = paramArrayOfCharSequence.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeCharSequence(paramArrayOfCharSequence[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeCharSequenceList(ArrayList<CharSequence> paramArrayList)
  {
    if (paramArrayList != null)
    {
      int i = paramArrayList.size();
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeCharSequence((CharSequence)paramArrayList.get(j));
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeDouble(double paramDouble)
  {
    nativeWriteDouble(mNativePtr, paramDouble);
  }
  
  public final void writeDoubleArray(double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble != null)
    {
      int i = paramArrayOfDouble.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeDouble(paramArrayOfDouble[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeException(Exception paramException)
  {
    int i = 0;
    if (((paramException instanceof Parcelable)) && (paramException.getClass().getClassLoader() == Parcelable.class.getClassLoader())) {
      i = -9;
    } else if ((paramException instanceof SecurityException)) {
      i = -1;
    } else if ((paramException instanceof BadParcelableException)) {
      i = -2;
    } else if ((paramException instanceof IllegalArgumentException)) {
      i = -3;
    } else if ((paramException instanceof NullPointerException)) {
      i = -4;
    } else if ((paramException instanceof IllegalStateException)) {
      i = -5;
    } else if ((paramException instanceof NetworkOnMainThreadException)) {
      i = -6;
    } else if ((paramException instanceof UnsupportedOperationException)) {
      i = -7;
    } else if ((paramException instanceof ServiceSpecificException)) {
      i = -8;
    }
    writeInt(i);
    StrictMode.clearGatheredViolations();
    if (i == 0)
    {
      if ((paramException instanceof RuntimeException)) {
        throw ((RuntimeException)paramException);
      }
      throw new RuntimeException(paramException);
    }
    writeString(paramException.getMessage());
    long l;
    if (sParcelExceptionStackTrace) {
      l = SystemClock.elapsedRealtime();
    } else {
      l = 0L;
    }
    int m;
    if ((sParcelExceptionStackTrace) && (l - sLastWriteExceptionStackTrace > 1000L))
    {
      sLastWriteExceptionStackTrace = l;
      int j = dataPosition();
      writeInt(0);
      StackTraceElement[] arrayOfStackTraceElement = paramException.getStackTrace();
      int k = Math.min(arrayOfStackTraceElement.length, 5);
      StringBuilder localStringBuilder = new StringBuilder();
      for (m = 0; m < k; m++)
      {
        localStringBuilder.append("\tat ");
        localStringBuilder.append(arrayOfStackTraceElement[m]);
        localStringBuilder.append('\n');
      }
      writeString(localStringBuilder.toString());
      m = dataPosition();
      setDataPosition(j);
      writeInt(m - j);
      setDataPosition(m);
    }
    else
    {
      writeInt(0);
    }
    switch (i)
    {
    default: 
      break;
    case -8: 
      writeInt(errorCode);
      break;
    case -9: 
      m = dataPosition();
      writeInt(0);
      writeParcelable((Parcelable)paramException, 1);
      i = dataPosition();
      setDataPosition(m);
      writeInt(i - m);
      setDataPosition(i);
    }
  }
  
  public final void writeFileDescriptor(FileDescriptor paramFileDescriptor)
  {
    updateNativeSize(nativeWriteFileDescriptor(mNativePtr, paramFileDescriptor));
  }
  
  public final void writeFloat(float paramFloat)
  {
    nativeWriteFloat(mNativePtr, paramFloat);
  }
  
  public final void writeFloatArray(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat != null)
    {
      int i = paramArrayOfFloat.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeFloat(paramArrayOfFloat[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeInt(int paramInt)
  {
    nativeWriteInt(mNativePtr, paramInt);
  }
  
  public final void writeIntArray(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeInt(paramArrayOfInt[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeInterfaceToken(String paramString)
  {
    nativeWriteInterfaceToken(mNativePtr, paramString);
  }
  
  public final void writeList(List paramList)
  {
    if (paramList == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramList.size();
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeValue(paramList.get(j));
      j++;
    }
  }
  
  public final void writeLong(long paramLong)
  {
    nativeWriteLong(mNativePtr, paramLong);
  }
  
  public final void writeLongArray(long[] paramArrayOfLong)
  {
    if (paramArrayOfLong != null)
    {
      int i = paramArrayOfLong.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeLong(paramArrayOfLong[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeMap(Map paramMap)
  {
    writeMapInternal(paramMap);
  }
  
  void writeMapInternal(Map<String, Object> paramMap)
  {
    if (paramMap == null)
    {
      writeInt(-1);
      return;
    }
    paramMap = paramMap.entrySet();
    int i = paramMap.size();
    writeInt(i);
    Iterator localIterator = paramMap.iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      writeValue(paramMap.getKey());
      writeValue(paramMap.getValue());
      i--;
    }
    if (i == 0) {
      return;
    }
    throw new BadParcelableException("Map size does not match number of entries!");
  }
  
  public final void writeNoException()
  {
    if (StrictMode.hasGatheredViolations())
    {
      writeInt(-128);
      int i = dataPosition();
      writeInt(0);
      StrictMode.writeGatheredViolationsToParcel(this);
      int j = dataPosition();
      setDataPosition(i);
      writeInt(j - i);
      setDataPosition(j);
    }
    else
    {
      writeInt(0);
    }
  }
  
  public final void writeParcelable(Parcelable paramParcelable, int paramInt)
  {
    if (paramParcelable == null)
    {
      writeString(null);
      return;
    }
    writeParcelableCreator(paramParcelable);
    paramParcelable.writeToParcel(this, paramInt);
  }
  
  public final <T extends Parcelable> void writeParcelableArray(T[] paramArrayOfT, int paramInt)
  {
    if (paramArrayOfT != null)
    {
      int i = paramArrayOfT.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeParcelable(paramArrayOfT[j], paramInt);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeParcelableCreator(Parcelable paramParcelable)
  {
    writeString(paramParcelable.getClass().getName());
  }
  
  public final <T extends Parcelable> void writeParcelableList(List<T> paramList, int paramInt)
  {
    if (paramList == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramList.size();
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeParcelable((Parcelable)paramList.get(j), paramInt);
      j++;
    }
  }
  
  public final void writePersistableBundle(PersistableBundle paramPersistableBundle)
  {
    if (paramPersistableBundle == null)
    {
      writeInt(-1);
      return;
    }
    paramPersistableBundle.writeToParcel(this, 0);
  }
  
  public final void writeRawFileDescriptor(FileDescriptor paramFileDescriptor)
  {
    nativeWriteFileDescriptor(mNativePtr, paramFileDescriptor);
  }
  
  public final void writeRawFileDescriptorArray(FileDescriptor[] paramArrayOfFileDescriptor)
  {
    if (paramArrayOfFileDescriptor != null)
    {
      int i = paramArrayOfFileDescriptor.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeRawFileDescriptor(paramArrayOfFileDescriptor[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeSerializable(Serializable paramSerializable)
  {
    if (paramSerializable == null)
    {
      writeString(null);
      return;
    }
    String str = paramSerializable.getClass().getName();
    writeString(str);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      ObjectOutputStream localObjectOutputStream = new java/io/ObjectOutputStream;
      localObjectOutputStream.<init>(localByteArrayOutputStream);
      localObjectOutputStream.writeObject(paramSerializable);
      localObjectOutputStream.close();
      writeByteArray(localByteArrayOutputStream.toByteArray());
      return;
    }
    catch (IOException localIOException)
    {
      paramSerializable = new StringBuilder();
      paramSerializable.append("Parcelable encountered IOException writing serializable object (name = ");
      paramSerializable.append(str);
      paramSerializable.append(")");
      throw new RuntimeException(paramSerializable.toString(), localIOException);
    }
  }
  
  public final void writeSize(Size paramSize)
  {
    writeInt(paramSize.getWidth());
    writeInt(paramSize.getHeight());
  }
  
  public final void writeSizeF(SizeF paramSizeF)
  {
    writeFloat(paramSizeF.getWidth());
    writeFloat(paramSizeF.getHeight());
  }
  
  public final void writeSparseArray(SparseArray<Object> paramSparseArray)
  {
    if (paramSparseArray == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramSparseArray.size();
    writeInt(i);
    for (int j = 0; j < i; j++)
    {
      writeInt(paramSparseArray.keyAt(j));
      writeValue(paramSparseArray.valueAt(j));
    }
  }
  
  public final void writeSparseBooleanArray(SparseBooleanArray paramSparseBooleanArray)
  {
    if (paramSparseBooleanArray == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramSparseBooleanArray.size();
    writeInt(i);
    for (int j = 0; j < i; j++)
    {
      writeInt(paramSparseBooleanArray.keyAt(j));
      writeByte((byte)paramSparseBooleanArray.valueAt(j));
    }
  }
  
  public final void writeSparseIntArray(SparseIntArray paramSparseIntArray)
  {
    if (paramSparseIntArray == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramSparseIntArray.size();
    writeInt(i);
    for (int j = 0; j < i; j++)
    {
      writeInt(paramSparseIntArray.keyAt(j));
      writeInt(paramSparseIntArray.valueAt(j));
    }
  }
  
  public final void writeString(String paramString)
  {
    mReadWriteHelper.writeString(this, paramString);
  }
  
  public final void writeStringArray(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeString(paramArrayOfString[j]);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final void writeStringList(List<String> paramList)
  {
    if (paramList == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramList.size();
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeString((String)paramList.get(j));
      j++;
    }
  }
  
  public void writeStringNoHelper(String paramString)
  {
    nativeWriteString(mNativePtr, paramString);
  }
  
  public final void writeStrongBinder(IBinder paramIBinder)
  {
    nativeWriteStrongBinder(mNativePtr, paramIBinder);
  }
  
  public final void writeStrongInterface(IInterface paramIInterface)
  {
    if (paramIInterface == null) {
      paramIInterface = null;
    } else {
      paramIInterface = paramIInterface.asBinder();
    }
    writeStrongBinder(paramIInterface);
  }
  
  public final <T extends Parcelable> void writeTypedArray(T[] paramArrayOfT, int paramInt)
  {
    if (paramArrayOfT != null)
    {
      int i = paramArrayOfT.length;
      writeInt(i);
      for (int j = 0; j < i; j++) {
        writeTypedObject(paramArrayOfT[j], paramInt);
      }
    }
    else
    {
      writeInt(-1);
    }
  }
  
  public final <T extends Parcelable> void writeTypedList(List<T> paramList)
  {
    writeTypedList(paramList, 0);
  }
  
  public <T extends Parcelable> void writeTypedList(List<T> paramList, int paramInt)
  {
    if (paramList == null)
    {
      writeInt(-1);
      return;
    }
    int i = paramList.size();
    int j = 0;
    writeInt(i);
    while (j < i)
    {
      writeTypedObject((Parcelable)paramList.get(j), paramInt);
      j++;
    }
  }
  
  public final <T extends Parcelable> void writeTypedObject(T paramT, int paramInt)
  {
    if (paramT != null)
    {
      writeInt(1);
      paramT.writeToParcel(this, paramInt);
    }
    else
    {
      writeInt(0);
    }
  }
  
  public final void writeValue(Object paramObject)
  {
    if (paramObject == null)
    {
      writeInt(-1);
    }
    else if ((paramObject instanceof String))
    {
      writeInt(0);
      writeString((String)paramObject);
    }
    else if ((paramObject instanceof Integer))
    {
      writeInt(1);
      writeInt(((Integer)paramObject).intValue());
    }
    else if ((paramObject instanceof Map))
    {
      writeInt(2);
      writeMap((Map)paramObject);
    }
    else if ((paramObject instanceof Bundle))
    {
      writeInt(3);
      writeBundle((Bundle)paramObject);
    }
    else if ((paramObject instanceof PersistableBundle))
    {
      writeInt(25);
      writePersistableBundle((PersistableBundle)paramObject);
    }
    else if ((paramObject instanceof Parcelable))
    {
      writeInt(4);
      writeParcelable((Parcelable)paramObject, 0);
    }
    else if ((paramObject instanceof Short))
    {
      writeInt(5);
      writeInt(((Short)paramObject).intValue());
    }
    else if ((paramObject instanceof Long))
    {
      writeInt(6);
      writeLong(((Long)paramObject).longValue());
    }
    else if ((paramObject instanceof Float))
    {
      writeInt(7);
      writeFloat(((Float)paramObject).floatValue());
    }
    else if ((paramObject instanceof Double))
    {
      writeInt(8);
      writeDouble(((Double)paramObject).doubleValue());
    }
    else if ((paramObject instanceof Boolean))
    {
      writeInt(9);
      writeInt(((Boolean)paramObject).booleanValue());
    }
    else if ((paramObject instanceof CharSequence))
    {
      writeInt(10);
      writeCharSequence((CharSequence)paramObject);
    }
    else if ((paramObject instanceof List))
    {
      writeInt(11);
      writeList((List)paramObject);
    }
    else if ((paramObject instanceof SparseArray))
    {
      writeInt(12);
      writeSparseArray((SparseArray)paramObject);
    }
    else if ((paramObject instanceof boolean[]))
    {
      writeInt(23);
      writeBooleanArray((boolean[])paramObject);
    }
    else if ((paramObject instanceof byte[]))
    {
      writeInt(13);
      writeByteArray((byte[])paramObject);
    }
    else if ((paramObject instanceof String[]))
    {
      writeInt(14);
      writeStringArray((String[])paramObject);
    }
    else if ((paramObject instanceof CharSequence[]))
    {
      writeInt(24);
      writeCharSequenceArray((CharSequence[])paramObject);
    }
    else if ((paramObject instanceof IBinder))
    {
      writeInt(15);
      writeStrongBinder((IBinder)paramObject);
    }
    else if ((paramObject instanceof Parcelable[]))
    {
      writeInt(16);
      writeParcelableArray((Parcelable[])paramObject, 0);
    }
    else if ((paramObject instanceof int[]))
    {
      writeInt(18);
      writeIntArray((int[])paramObject);
    }
    else if ((paramObject instanceof long[]))
    {
      writeInt(19);
      writeLongArray((long[])paramObject);
    }
    else if ((paramObject instanceof Byte))
    {
      writeInt(20);
      writeInt(((Byte)paramObject).byteValue());
    }
    else if ((paramObject instanceof Size))
    {
      writeInt(26);
      writeSize((Size)paramObject);
    }
    else if ((paramObject instanceof SizeF))
    {
      writeInt(27);
      writeSizeF((SizeF)paramObject);
    }
    else if ((paramObject instanceof double[]))
    {
      writeInt(28);
      writeDoubleArray((double[])paramObject);
    }
    else
    {
      localObject = paramObject.getClass();
      if ((((Class)localObject).isArray()) && (((Class)localObject).getComponentType() == Object.class))
      {
        writeInt(17);
        writeArray((Object[])paramObject);
      }
      else
      {
        if (!(paramObject instanceof Serializable)) {
          break label713;
        }
        writeInt(21);
        writeSerializable((Serializable)paramObject);
      }
    }
    return;
    label713:
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Parcel: unable to marshal value ");
    ((StringBuilder)localObject).append(paramObject);
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public static class ReadWriteHelper
  {
    public static final ReadWriteHelper DEFAULT = new ReadWriteHelper();
    
    public ReadWriteHelper() {}
    
    public String readString(Parcel paramParcel)
    {
      return Parcel.nativeReadString(mNativePtr);
    }
    
    public void writeString(Parcel paramParcel, String paramString)
    {
      Parcel.nativeWriteString(mNativePtr, paramString);
    }
  }
}
