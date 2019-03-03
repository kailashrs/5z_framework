package android.os;

import android.annotation.SystemApi;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import libcore.util.NativeAllocationRegistry;

@SystemApi
public class HwParcel
{
  public static final int STATUS_SUCCESS = 0;
  private static final String TAG = "HwParcel";
  private static final NativeAllocationRegistry sNativeRegistry;
  private long mNativeContext;
  
  static
  {
    long l = native_init();
    sNativeRegistry = new NativeAllocationRegistry(HwParcel.class.getClassLoader(), l, 128L);
  }
  
  public HwParcel()
  {
    native_setup(true);
    sNativeRegistry.registerNativeAllocation(this, mNativeContext);
  }
  
  private HwParcel(boolean paramBoolean)
  {
    native_setup(paramBoolean);
    sNativeRegistry.registerNativeAllocation(this, mNativeContext);
  }
  
  private static final native long native_init();
  
  private final native void native_setup(boolean paramBoolean);
  
  private final native boolean[] readBoolVectorAsArray();
  
  private final native double[] readDoubleVectorAsArray();
  
  private final native float[] readFloatVectorAsArray();
  
  private final native short[] readInt16VectorAsArray();
  
  private final native int[] readInt32VectorAsArray();
  
  private final native long[] readInt64VectorAsArray();
  
  private final native byte[] readInt8VectorAsArray();
  
  private final native String[] readStringVectorAsArray();
  
  private final native void writeBoolVector(boolean[] paramArrayOfBoolean);
  
  private final native void writeDoubleVector(double[] paramArrayOfDouble);
  
  private final native void writeFloatVector(float[] paramArrayOfFloat);
  
  private final native void writeInt16Vector(short[] paramArrayOfShort);
  
  private final native void writeInt32Vector(int[] paramArrayOfInt);
  
  private final native void writeInt64Vector(long[] paramArrayOfLong);
  
  private final native void writeInt8Vector(byte[] paramArrayOfByte);
  
  private final native void writeStringVector(String[] paramArrayOfString);
  
  public final native void enforceInterface(String paramString);
  
  public final native boolean readBool();
  
  public final ArrayList<Boolean> readBoolVector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readBoolVectorAsArray())));
  }
  
  public final native HwBlob readBuffer(long paramLong);
  
  public final native double readDouble();
  
  public final ArrayList<Double> readDoubleVector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readDoubleVectorAsArray())));
  }
  
  public final native HwBlob readEmbeddedBuffer(long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean);
  
  public final native float readFloat();
  
  public final ArrayList<Float> readFloatVector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readFloatVectorAsArray())));
  }
  
  public final native short readInt16();
  
  public final ArrayList<Short> readInt16Vector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readInt16VectorAsArray())));
  }
  
  public final native int readInt32();
  
  public final ArrayList<Integer> readInt32Vector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readInt32VectorAsArray())));
  }
  
  public final native long readInt64();
  
  public final ArrayList<Long> readInt64Vector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readInt64VectorAsArray())));
  }
  
  public final native byte readInt8();
  
  public final ArrayList<Byte> readInt8Vector()
  {
    return new ArrayList(Arrays.asList(HwBlob.wrapArray(readInt8VectorAsArray())));
  }
  
  public final native String readString();
  
  public final ArrayList<String> readStringVector()
  {
    return new ArrayList(Arrays.asList(readStringVectorAsArray()));
  }
  
  public final native IHwBinder readStrongBinder();
  
  public final native void release();
  
  public final native void releaseTemporaryStorage();
  
  public final native void send();
  
  public final native void verifySuccess();
  
  public final native void writeBool(boolean paramBoolean);
  
  public final void writeBoolVector(ArrayList<Boolean> paramArrayList)
  {
    int i = paramArrayList.size();
    boolean[] arrayOfBoolean = new boolean[i];
    for (int j = 0; j < i; j++) {
      arrayOfBoolean[j] = ((Boolean)paramArrayList.get(j)).booleanValue();
    }
    writeBoolVector(arrayOfBoolean);
  }
  
  public final native void writeBuffer(HwBlob paramHwBlob);
  
  public final native void writeDouble(double paramDouble);
  
  public final void writeDoubleVector(ArrayList<Double> paramArrayList)
  {
    int i = paramArrayList.size();
    double[] arrayOfDouble = new double[i];
    for (int j = 0; j < i; j++) {
      arrayOfDouble[j] = ((Double)paramArrayList.get(j)).doubleValue();
    }
    writeDoubleVector(arrayOfDouble);
  }
  
  public final native void writeFloat(float paramFloat);
  
  public final void writeFloatVector(ArrayList<Float> paramArrayList)
  {
    int i = paramArrayList.size();
    float[] arrayOfFloat = new float[i];
    for (int j = 0; j < i; j++) {
      arrayOfFloat[j] = ((Float)paramArrayList.get(j)).floatValue();
    }
    writeFloatVector(arrayOfFloat);
  }
  
  public final native void writeInt16(short paramShort);
  
  public final void writeInt16Vector(ArrayList<Short> paramArrayList)
  {
    int i = paramArrayList.size();
    short[] arrayOfShort = new short[i];
    for (int j = 0; j < i; j++) {
      arrayOfShort[j] = ((Short)paramArrayList.get(j)).shortValue();
    }
    writeInt16Vector(arrayOfShort);
  }
  
  public final native void writeInt32(int paramInt);
  
  public final void writeInt32Vector(ArrayList<Integer> paramArrayList)
  {
    int i = paramArrayList.size();
    int[] arrayOfInt = new int[i];
    for (int j = 0; j < i; j++) {
      arrayOfInt[j] = ((Integer)paramArrayList.get(j)).intValue();
    }
    writeInt32Vector(arrayOfInt);
  }
  
  public final native void writeInt64(long paramLong);
  
  public final void writeInt64Vector(ArrayList<Long> paramArrayList)
  {
    int i = paramArrayList.size();
    long[] arrayOfLong = new long[i];
    for (int j = 0; j < i; j++) {
      arrayOfLong[j] = ((Long)paramArrayList.get(j)).longValue();
    }
    writeInt64Vector(arrayOfLong);
  }
  
  public final native void writeInt8(byte paramByte);
  
  public final void writeInt8Vector(ArrayList<Byte> paramArrayList)
  {
    int i = paramArrayList.size();
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; j < i; j++) {
      arrayOfByte[j] = ((Byte)paramArrayList.get(j)).byteValue();
    }
    writeInt8Vector(arrayOfByte);
  }
  
  public final native void writeInterfaceToken(String paramString);
  
  public final native void writeStatus(int paramInt);
  
  public final native void writeString(String paramString);
  
  public final void writeStringVector(ArrayList<String> paramArrayList)
  {
    writeStringVector((String[])paramArrayList.toArray(new String[paramArrayList.size()]));
  }
  
  public final native void writeStrongBinder(IHwBinder paramIHwBinder);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Status {}
}
