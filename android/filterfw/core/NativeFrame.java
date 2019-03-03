package android.filterfw.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;

public class NativeFrame
  extends Frame
{
  private int nativeFrameId = -1;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  NativeFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    super(paramFrameFormat, paramFrameManager);
    int i = paramFrameFormat.getSize();
    nativeAllocate(i);
    boolean bool;
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    }
    setReusable(bool);
  }
  
  private native boolean getNativeBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2);
  
  private native boolean getNativeBuffer(NativeBuffer paramNativeBuffer);
  
  private native int getNativeCapacity();
  
  private native byte[] getNativeData(int paramInt);
  
  private native float[] getNativeFloats(int paramInt);
  
  private native int[] getNativeInts(int paramInt);
  
  private native boolean nativeAllocate(int paramInt);
  
  private native boolean nativeCopyFromGL(GLFrame paramGLFrame);
  
  private native boolean nativeCopyFromNative(NativeFrame paramNativeFrame);
  
  private native boolean nativeDeallocate();
  
  private static native int nativeFloatSize();
  
  private static native int nativeIntSize();
  
  private native boolean setNativeBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2);
  
  private native boolean setNativeData(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private native boolean setNativeFloats(float[] paramArrayOfFloat);
  
  private native boolean setNativeInts(int[] paramArrayOfInt);
  
  public Bitmap getBitmap()
  {
    if (getFormat().getNumberOfDimensions() == 2)
    {
      Bitmap localBitmap = Bitmap.createBitmap(getFormat().getWidth(), getFormat().getHeight(), Bitmap.Config.ARGB_8888);
      if (getNativeBitmap(localBitmap, localBitmap.getByteCount(), getFormat().getBytesPerSample())) {
        return localBitmap;
      }
      throw new RuntimeException("Could not get bitmap data from native frame!");
    }
    throw new RuntimeException("Attempting to get Bitmap for non 2-dimensional native frame!");
  }
  
  public int getCapacity()
  {
    return getNativeCapacity();
  }
  
  public ByteBuffer getData()
  {
    Object localObject = getNativeData(getFormat().getSize());
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = ByteBuffer.wrap((byte[])localObject);
    }
    return localObject;
  }
  
  public float[] getFloats()
  {
    return getNativeFloats(getFormat().getSize());
  }
  
  public int[] getInts()
  {
    return getNativeInts(getFormat().getSize());
  }
  
  public Object getObjectValue()
  {
    if (getFormat().getBaseType() != 8) {
      return getData();
    }
    Class localClass = getFormat().getObjectClass();
    if (localClass != null)
    {
      if (NativeBuffer.class.isAssignableFrom(localClass)) {
        try
        {
          NativeBuffer localNativeBuffer = (NativeBuffer)localClass.newInstance();
          if (getNativeBuffer(localNativeBuffer))
          {
            localNativeBuffer.attachToFrame(this);
            return localNativeBuffer;
          }
          throw new RuntimeException("Could not get the native structured data for frame!");
        }
        catch (Exception localException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Could not instantiate new structure instance of type '");
          localStringBuilder.append(localClass);
          localStringBuilder.append("'!");
          throw new RuntimeException(localStringBuilder.toString());
        }
      }
      throw new RuntimeException("NativeFrame object class must be a subclass of NativeBuffer!");
    }
    throw new RuntimeException("Attempting to get object data from frame that does not specify a structure object class!");
  }
  
  protected boolean hasNativeAllocation()
  {
    try
    {
      int i = nativeFrameId;
      boolean bool;
      if (i != -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected void releaseNativeAllocation()
  {
    try
    {
      nativeDeallocate();
      nativeFrameId = -1;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setBitmap(Bitmap paramBitmap)
  {
    assertFrameMutable();
    if (getFormat().getNumberOfDimensions() == 2)
    {
      if ((getFormat().getWidth() == paramBitmap.getWidth()) && (getFormat().getHeight() == paramBitmap.getHeight()))
      {
        paramBitmap = convertBitmapToRGBA(paramBitmap);
        if (setNativeBitmap(paramBitmap, paramBitmap.getByteCount(), getFormat().getBytesPerSample())) {
          return;
        }
        throw new RuntimeException("Could not set native frame bitmap data!");
      }
      throw new RuntimeException("Bitmap dimensions do not match native frame dimensions!");
    }
    throw new RuntimeException("Attempting to set Bitmap for non 2-dimensional native frame!");
  }
  
  public void setData(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    assertFrameMutable();
    Object localObject = paramByteBuffer.array();
    if (paramInt2 + paramInt1 <= paramByteBuffer.limit())
    {
      if (getFormat().getSize() == paramInt2)
      {
        if (setNativeData((byte[])localObject, paramInt1, paramInt2)) {
          return;
        }
        throw new RuntimeException("Could not set native frame data!");
      }
      paramByteBuffer = new StringBuilder();
      paramByteBuffer.append("Data size in setData does not match native frame size: Frame size is ");
      paramByteBuffer.append(getFormat().getSize());
      paramByteBuffer.append(" bytes, but ");
      paramByteBuffer.append(paramInt2);
      paramByteBuffer.append(" bytes given!");
      throw new RuntimeException(paramByteBuffer.toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Offset and length exceed buffer size in native setData: ");
    ((StringBuilder)localObject).append(paramInt2 + paramInt1);
    ((StringBuilder)localObject).append(" bytes given, but only ");
    ((StringBuilder)localObject).append(paramByteBuffer.limit());
    ((StringBuilder)localObject).append(" bytes available!");
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public void setDataFromFrame(Frame paramFrame)
  {
    if (getFormat().getSize() >= paramFrame.getFormat().getSize())
    {
      if ((paramFrame instanceof NativeFrame)) {
        nativeCopyFromNative((NativeFrame)paramFrame);
      } else if ((paramFrame instanceof GLFrame)) {
        nativeCopyFromGL((GLFrame)paramFrame);
      } else if ((paramFrame instanceof SimpleFrame)) {
        setObjectValue(paramFrame.getObjectValue());
      } else {
        super.setDataFromFrame(paramFrame);
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attempting to assign frame of size ");
    localStringBuilder.append(paramFrame.getFormat().getSize());
    localStringBuilder.append(" to smaller native frame of size ");
    localStringBuilder.append(getFormat().getSize());
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setFloats(float[] paramArrayOfFloat)
  {
    assertFrameMutable();
    if (paramArrayOfFloat.length * nativeFloatSize() <= getFormat().getSize())
    {
      if (setNativeFloats(paramArrayOfFloat)) {
        return;
      }
      throw new RuntimeException("Could not set int values for native frame!");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NativeFrame cannot hold ");
    localStringBuilder.append(paramArrayOfFloat.length);
    localStringBuilder.append(" floats. (Can only hold ");
    localStringBuilder.append(getFormat().getSize() / nativeFloatSize());
    localStringBuilder.append(" floats).");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setInts(int[] paramArrayOfInt)
  {
    assertFrameMutable();
    if (paramArrayOfInt.length * nativeIntSize() <= getFormat().getSize())
    {
      if (setNativeInts(paramArrayOfInt)) {
        return;
      }
      throw new RuntimeException("Could not set int values for native frame!");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NativeFrame cannot hold ");
    localStringBuilder.append(paramArrayOfInt.length);
    localStringBuilder.append(" integers. (Can only hold ");
    localStringBuilder.append(getFormat().getSize() / nativeIntSize());
    localStringBuilder.append(" integers).");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NativeFrame id: ");
    localStringBuilder.append(nativeFrameId);
    localStringBuilder.append(" (");
    localStringBuilder.append(getFormat());
    localStringBuilder.append(") of size ");
    localStringBuilder.append(getCapacity());
    return localStringBuilder.toString();
  }
}
