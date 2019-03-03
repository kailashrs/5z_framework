package android.os;

import android.annotation.SystemApi;
import libcore.util.NativeAllocationRegistry;

@SystemApi
public class HwBlob
{
  private static final String TAG = "HwBlob";
  private static final NativeAllocationRegistry sNativeRegistry;
  private long mNativeContext;
  
  static
  {
    long l = native_init();
    sNativeRegistry = new NativeAllocationRegistry(HwBlob.class.getClassLoader(), l, 128L);
  }
  
  public HwBlob(int paramInt)
  {
    native_setup(paramInt);
    sNativeRegistry.registerNativeAllocation(this, mNativeContext);
  }
  
  private static final native long native_init();
  
  private final native void native_setup(int paramInt);
  
  public static Boolean[] wrapArray(boolean[] paramArrayOfBoolean)
  {
    int i = paramArrayOfBoolean.length;
    Boolean[] arrayOfBoolean = new Boolean[i];
    for (int j = 0; j < i; j++) {
      arrayOfBoolean[j] = Boolean.valueOf(paramArrayOfBoolean[j]);
    }
    return arrayOfBoolean;
  }
  
  public static Byte[] wrapArray(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    Byte[] arrayOfByte = new Byte[i];
    for (int j = 0; j < i; j++) {
      arrayOfByte[j] = Byte.valueOf(paramArrayOfByte[j]);
    }
    return arrayOfByte;
  }
  
  public static Double[] wrapArray(double[] paramArrayOfDouble)
  {
    int i = paramArrayOfDouble.length;
    Double[] arrayOfDouble = new Double[i];
    for (int j = 0; j < i; j++) {
      arrayOfDouble[j] = Double.valueOf(paramArrayOfDouble[j]);
    }
    return arrayOfDouble;
  }
  
  public static Float[] wrapArray(float[] paramArrayOfFloat)
  {
    int i = paramArrayOfFloat.length;
    Float[] arrayOfFloat = new Float[i];
    for (int j = 0; j < i; j++) {
      arrayOfFloat[j] = Float.valueOf(paramArrayOfFloat[j]);
    }
    return arrayOfFloat;
  }
  
  public static Integer[] wrapArray(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    Integer[] arrayOfInteger = new Integer[i];
    for (int j = 0; j < i; j++) {
      arrayOfInteger[j] = Integer.valueOf(paramArrayOfInt[j]);
    }
    return arrayOfInteger;
  }
  
  public static Long[] wrapArray(long[] paramArrayOfLong)
  {
    int i = paramArrayOfLong.length;
    Long[] arrayOfLong = new Long[i];
    for (int j = 0; j < i; j++) {
      arrayOfLong[j] = Long.valueOf(paramArrayOfLong[j]);
    }
    return arrayOfLong;
  }
  
  public static Short[] wrapArray(short[] paramArrayOfShort)
  {
    int i = paramArrayOfShort.length;
    Short[] arrayOfShort = new Short[i];
    for (int j = 0; j < i; j++) {
      arrayOfShort[j] = Short.valueOf(paramArrayOfShort[j]);
    }
    return arrayOfShort;
  }
  
  public final native void copyToBoolArray(long paramLong, boolean[] paramArrayOfBoolean, int paramInt);
  
  public final native void copyToDoubleArray(long paramLong, double[] paramArrayOfDouble, int paramInt);
  
  public final native void copyToFloatArray(long paramLong, float[] paramArrayOfFloat, int paramInt);
  
  public final native void copyToInt16Array(long paramLong, short[] paramArrayOfShort, int paramInt);
  
  public final native void copyToInt32Array(long paramLong, int[] paramArrayOfInt, int paramInt);
  
  public final native void copyToInt64Array(long paramLong, long[] paramArrayOfLong, int paramInt);
  
  public final native void copyToInt8Array(long paramLong, byte[] paramArrayOfByte, int paramInt);
  
  public final native boolean getBool(long paramLong);
  
  public final native double getDouble(long paramLong);
  
  public final native float getFloat(long paramLong);
  
  public final native short getInt16(long paramLong);
  
  public final native int getInt32(long paramLong);
  
  public final native long getInt64(long paramLong);
  
  public final native byte getInt8(long paramLong);
  
  public final native String getString(long paramLong);
  
  public final native long handle();
  
  public final native void putBlob(long paramLong, HwBlob paramHwBlob);
  
  public final native void putBool(long paramLong, boolean paramBoolean);
  
  public final native void putBoolArray(long paramLong, boolean[] paramArrayOfBoolean);
  
  public final native void putDouble(long paramLong, double paramDouble);
  
  public final native void putDoubleArray(long paramLong, double[] paramArrayOfDouble);
  
  public final native void putFloat(long paramLong, float paramFloat);
  
  public final native void putFloatArray(long paramLong, float[] paramArrayOfFloat);
  
  public final native void putInt16(long paramLong, short paramShort);
  
  public final native void putInt16Array(long paramLong, short[] paramArrayOfShort);
  
  public final native void putInt32(long paramLong, int paramInt);
  
  public final native void putInt32Array(long paramLong, int[] paramArrayOfInt);
  
  public final native void putInt64(long paramLong1, long paramLong2);
  
  public final native void putInt64Array(long paramLong, long[] paramArrayOfLong);
  
  public final native void putInt8(long paramLong, byte paramByte);
  
  public final native void putInt8Array(long paramLong, byte[] paramArrayOfByte);
  
  public final native void putString(long paramLong, String paramString);
}
