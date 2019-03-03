package android.filterfw.core;

import android.filterfw.format.ObjectFormat;
import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SerializedFrame
  extends Frame
{
  private static final int INITIAL_CAPACITY = 64;
  private DirectByteOutputStream mByteOutputStream;
  private ObjectOutputStream mObjectOut;
  
  SerializedFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    super(paramFrameFormat, paramFrameManager);
    setReusable(false);
    try
    {
      paramFrameFormat = new android/filterfw/core/SerializedFrame$DirectByteOutputStream;
      paramFrameFormat.<init>(this, 64);
      mByteOutputStream = paramFrameFormat;
      paramFrameFormat = new java/io/ObjectOutputStream;
      paramFrameFormat.<init>(mByteOutputStream);
      mObjectOut = paramFrameFormat;
      mByteOutputStream.markHeaderEnd();
      return;
    }
    catch (IOException paramFrameFormat)
    {
      throw new RuntimeException("Could not create serialization streams for SerializedFrame!", paramFrameFormat);
    }
  }
  
  private final Object deserializeObjectValue()
  {
    try
    {
      DirectByteInputStream localDirectByteInputStream = mByteOutputStream.getInputStream();
      localObject = new java/io/ObjectInputStream;
      ((ObjectInputStream)localObject).<init>(localDirectByteInputStream);
      localObject = ((ObjectInputStream)localObject).readObject();
      return localObject;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unable to deserialize object of unknown class in ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      throw new RuntimeException(((StringBuilder)localObject).toString(), localClassNotFoundException);
    }
    catch (IOException localIOException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Could not deserialize object in ");
      ((StringBuilder)localObject).append(this);
      ((StringBuilder)localObject).append("!");
      throw new RuntimeException(((StringBuilder)localObject).toString(), localIOException);
    }
  }
  
  private final void serializeObjectValue(Object paramObject)
  {
    try
    {
      mByteOutputStream.reset();
      mObjectOut.writeObject(paramObject);
      mObjectOut.flush();
      mObjectOut.close();
      return;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not serialize object ");
      localStringBuilder.append(paramObject);
      localStringBuilder.append(" in ");
      localStringBuilder.append(this);
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString(), localIOException);
    }
  }
  
  static SerializedFrame wrapObject(Object paramObject, FrameManager paramFrameManager)
  {
    paramFrameManager = new SerializedFrame(ObjectFormat.fromObject(paramObject, 1), paramFrameManager);
    paramFrameManager.setObjectValue(paramObject);
    return paramFrameManager;
  }
  
  public Bitmap getBitmap()
  {
    Object localObject = deserializeObjectValue();
    if ((localObject instanceof Bitmap)) {
      localObject = (Bitmap)localObject;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public ByteBuffer getData()
  {
    Object localObject = deserializeObjectValue();
    if ((localObject instanceof ByteBuffer)) {
      localObject = (ByteBuffer)localObject;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public float[] getFloats()
  {
    Object localObject = deserializeObjectValue();
    if ((localObject instanceof float[])) {
      localObject = (float[])localObject;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public int[] getInts()
  {
    Object localObject = deserializeObjectValue();
    if ((localObject instanceof int[])) {
      localObject = (int[])localObject;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public Object getObjectValue()
  {
    return deserializeObjectValue();
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
    serializeObjectValue(paramObject);
  }
  
  public void setInts(int[] paramArrayOfInt)
  {
    assertFrameMutable();
    setGenericObjectValue(paramArrayOfInt);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SerializedFrame (");
    localStringBuilder.append(getFormat());
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  private class DirectByteInputStream
    extends InputStream
  {
    private byte[] mBuffer;
    private int mPos = 0;
    private int mSize;
    
    public DirectByteInputStream(byte[] paramArrayOfByte, int paramInt)
    {
      mBuffer = paramArrayOfByte;
      mSize = paramInt;
    }
    
    public final int available()
    {
      return mSize - mPos;
    }
    
    public final int read()
    {
      int i;
      if (mPos < mSize)
      {
        byte[] arrayOfByte = mBuffer;
        i = mPos;
        mPos = (i + 1);
        i = arrayOfByte[i] & 0xFF;
      }
      else
      {
        i = -1;
      }
      return i;
    }
    
    public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if (mPos >= mSize) {
        return -1;
      }
      int i = paramInt2;
      if (mPos + paramInt2 > mSize) {
        i = mSize - mPos;
      }
      System.arraycopy(mBuffer, mPos, paramArrayOfByte, paramInt1, i);
      mPos += i;
      return i;
    }
    
    public final long skip(long paramLong)
    {
      long l = paramLong;
      if (mPos + paramLong > mSize) {
        l = mSize - mPos;
      }
      if (l < 0L) {
        return 0L;
      }
      mPos = ((int)(mPos + l));
      return l;
    }
  }
  
  private class DirectByteOutputStream
    extends OutputStream
  {
    private byte[] mBuffer = null;
    private int mDataOffset = 0;
    private int mOffset = 0;
    
    public DirectByteOutputStream(int paramInt)
    {
      mBuffer = new byte[paramInt];
    }
    
    private final void ensureFit(int paramInt)
    {
      if (mOffset + paramInt > mBuffer.length)
      {
        byte[] arrayOfByte = mBuffer;
        mBuffer = new byte[Math.max(mOffset + paramInt, mBuffer.length * 2)];
        System.arraycopy(arrayOfByte, 0, mBuffer, 0, mOffset);
      }
    }
    
    public byte[] getByteArray()
    {
      return mBuffer;
    }
    
    public final SerializedFrame.DirectByteInputStream getInputStream()
    {
      return new SerializedFrame.DirectByteInputStream(SerializedFrame.this, mBuffer, mOffset);
    }
    
    public final int getSize()
    {
      return mOffset;
    }
    
    public final void markHeaderEnd()
    {
      mDataOffset = mOffset;
    }
    
    public final void reset()
    {
      mOffset = mDataOffset;
    }
    
    public final void write(int paramInt)
    {
      ensureFit(1);
      byte[] arrayOfByte = mBuffer;
      int i = mOffset;
      mOffset = (i + 1);
      arrayOfByte[i] = ((byte)(byte)paramInt);
    }
    
    public final void write(byte[] paramArrayOfByte)
    {
      write(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public final void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      ensureFit(paramInt2);
      System.arraycopy(paramArrayOfByte, paramInt1, mBuffer, mOffset, paramInt2);
      mOffset += paramInt2;
    }
  }
}
