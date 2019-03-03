package android.filterfw.core;

import android.filterfw.format.ObjectFormat;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;

public class SimpleFrame
  extends Frame
{
  private Object mObject;
  
  SimpleFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    super(paramFrameFormat, paramFrameManager);
    initWithFormat(paramFrameFormat);
    setReusable(false);
  }
  
  private void initWithFormat(FrameFormat paramFrameFormat)
  {
    int i = paramFrameFormat.getLength();
    switch (paramFrameFormat.getBaseType())
    {
    default: 
      mObject = null;
      break;
    case 6: 
      mObject = new double[i];
      break;
    case 5: 
      mObject = new float[i];
      break;
    case 4: 
      mObject = new int[i];
      break;
    case 3: 
      mObject = new short[i];
      break;
    case 2: 
      mObject = new byte[i];
    }
  }
  
  private void setFormatObjectClass(Class paramClass)
  {
    MutableFrameFormat localMutableFrameFormat = getFormat().mutableCopy();
    localMutableFrameFormat.setObjectClass(paramClass);
    setFormat(localMutableFrameFormat);
  }
  
  static SimpleFrame wrapObject(Object paramObject, FrameManager paramFrameManager)
  {
    paramFrameManager = new SimpleFrame(ObjectFormat.fromObject(paramObject, 1), paramFrameManager);
    paramFrameManager.setObjectValue(paramObject);
    return paramFrameManager;
  }
  
  public Bitmap getBitmap()
  {
    Bitmap localBitmap;
    if ((mObject instanceof Bitmap)) {
      localBitmap = (Bitmap)mObject;
    } else {
      localBitmap = null;
    }
    return localBitmap;
  }
  
  public ByteBuffer getData()
  {
    ByteBuffer localByteBuffer;
    if ((mObject instanceof ByteBuffer)) {
      localByteBuffer = (ByteBuffer)mObject;
    } else {
      localByteBuffer = null;
    }
    return localByteBuffer;
  }
  
  public float[] getFloats()
  {
    float[] arrayOfFloat;
    if ((mObject instanceof float[])) {
      arrayOfFloat = (float[])mObject;
    } else {
      arrayOfFloat = null;
    }
    return arrayOfFloat;
  }
  
  public int[] getInts()
  {
    int[] arrayOfInt;
    if ((mObject instanceof int[])) {
      arrayOfInt = (int[])mObject;
    } else {
      arrayOfInt = null;
    }
    return arrayOfInt;
  }
  
  public Object getObjectValue()
  {
    return mObject;
  }
  
  protected boolean hasNativeAllocation()
  {
    return false;
  }
  
  protected void releaseNativeAllocation() {}
  
  public void setBitmap(Bitmap paramBitmap)
  {
    assertFrameMutable();
    setGenericObjectValue(paramBitmap);
  }
  
  public void setData(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    assertFrameMutable();
    setGenericObjectValue(ByteBuffer.wrap(paramByteBuffer.array(), paramInt1, paramInt2));
  }
  
  public void setFloats(float[] paramArrayOfFloat)
  {
    assertFrameMutable();
    setGenericObjectValue(paramArrayOfFloat);
  }
  
  protected void setGenericObjectValue(Object paramObject)
  {
    FrameFormat localFrameFormat = getFormat();
    if (localFrameFormat.getObjectClass() == null) {
      setFormatObjectClass(paramObject.getClass());
    } else {
      if (!localFrameFormat.getObjectClass().isAssignableFrom(paramObject.getClass())) {
        break label43;
      }
    }
    mObject = paramObject;
    return;
    label43:
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attempting to set object value of type '");
    localStringBuilder.append(paramObject.getClass());
    localStringBuilder.append("' on SimpleFrame of type '");
    localStringBuilder.append(localFrameFormat.getObjectClass());
    localStringBuilder.append("'!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setInts(int[] paramArrayOfInt)
  {
    assertFrameMutable();
    setGenericObjectValue(paramArrayOfInt);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SimpleFrame (");
    localStringBuilder.append(getFormat());
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
