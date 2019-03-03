package android.filterfw.core;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

public class VertexFrame
  extends Frame
{
  private int vertexFrameId = -1;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  VertexFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    super(paramFrameFormat, paramFrameManager);
    if (getFormat().getSize() > 0)
    {
      if (nativeAllocate(getFormat().getSize())) {
        return;
      }
      throw new RuntimeException("Could not allocate vertex frame!");
    }
    throw new IllegalArgumentException("Initializing vertex frame with zero size!");
  }
  
  private native int getNativeVboId();
  
  private native boolean nativeAllocate(int paramInt);
  
  private native boolean nativeDeallocate();
  
  private native boolean setNativeData(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private native boolean setNativeFloats(float[] paramArrayOfFloat);
  
  private native boolean setNativeInts(int[] paramArrayOfInt);
  
  public Bitmap getBitmap()
  {
    throw new RuntimeException("Vertex frames do not support reading data!");
  }
  
  public ByteBuffer getData()
  {
    throw new RuntimeException("Vertex frames do not support reading data!");
  }
  
  public float[] getFloats()
  {
    throw new RuntimeException("Vertex frames do not support reading data!");
  }
  
  public int[] getInts()
  {
    throw new RuntimeException("Vertex frames do not support reading data!");
  }
  
  public Object getObjectValue()
  {
    throw new RuntimeException("Vertex frames do not support reading data!");
  }
  
  public int getVboId()
  {
    return getNativeVboId();
  }
  
  protected boolean hasNativeAllocation()
  {
    try
    {
      int i = vertexFrameId;
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
      vertexFrameId = -1;
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
    throw new RuntimeException("Unsupported: Cannot set vertex frame bitmap value!");
  }
  
  public void setData(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    assertFrameMutable();
    paramByteBuffer = paramByteBuffer.array();
    if (getFormat().getSize() == paramByteBuffer.length)
    {
      if (setNativeData(paramByteBuffer, paramInt1, paramInt2)) {
        return;
      }
      throw new RuntimeException("Could not set vertex frame data!");
    }
    throw new RuntimeException("Data size in setData does not match vertex frame size!");
  }
  
  public void setDataFromFrame(Frame paramFrame)
  {
    super.setDataFromFrame(paramFrame);
  }
  
  public void setFloats(float[] paramArrayOfFloat)
  {
    assertFrameMutable();
    if (setNativeFloats(paramArrayOfFloat)) {
      return;
    }
    throw new RuntimeException("Could not set int values for vertex frame!");
  }
  
  public void setInts(int[] paramArrayOfInt)
  {
    assertFrameMutable();
    if (setNativeInts(paramArrayOfInt)) {
      return;
    }
    throw new RuntimeException("Could not set int values for vertex frame!");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VertexFrame (");
    localStringBuilder.append(getFormat());
    localStringBuilder.append(") with VBO ID ");
    localStringBuilder.append(getVboId());
    return localStringBuilder.toString();
  }
}
