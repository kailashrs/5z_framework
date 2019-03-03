package android.filterfw.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.opengl.GLES20;
import java.nio.ByteBuffer;

public class GLFrame
  extends Frame
{
  public static final int EXISTING_FBO_BINDING = 101;
  public static final int EXISTING_TEXTURE_BINDING = 100;
  public static final int EXTERNAL_TEXTURE = 104;
  public static final int NEW_FBO_BINDING = 103;
  public static final int NEW_TEXTURE_BINDING = 102;
  private int glFrameId = -1;
  private GLEnvironment mGLEnvironment;
  private boolean mOwnsTexture = true;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  GLFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager)
  {
    super(paramFrameFormat, paramFrameManager);
  }
  
  GLFrame(FrameFormat paramFrameFormat, FrameManager paramFrameManager, int paramInt, long paramLong)
  {
    super(paramFrameFormat, paramFrameManager, paramInt, paramLong);
  }
  
  private void assertGLEnvValid()
  {
    if (!mGLEnvironment.isContextActive())
    {
      if (GLEnvironment.isAnyContextActive())
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Attempting to access ");
        localStringBuilder.append(this);
        localStringBuilder.append(" with foreign GL context active!");
        throw new RuntimeException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Attempting to access ");
      localStringBuilder.append(this);
      localStringBuilder.append(" with no GL context  active!");
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  private native boolean generateNativeMipMap();
  
  private native boolean getNativeBitmap(Bitmap paramBitmap);
  
  private native byte[] getNativeData();
  
  private native int getNativeFboId();
  
  private native float[] getNativeFloats();
  
  private native int[] getNativeInts();
  
  private native int getNativeTextureId();
  
  private void initNew(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!nativeAllocateExternal(mGLEnvironment)) {
        throw new RuntimeException("Could not allocate external GL frame!");
      }
    }
    else {
      if (!nativeAllocate(mGLEnvironment, getFormat().getWidth(), getFormat().getHeight())) {
        break label54;
      }
    }
    return;
    label54:
    throw new RuntimeException("Could not allocate GL frame!");
  }
  
  private void initWithFbo(int paramInt)
  {
    int i = getFormat().getWidth();
    int j = getFormat().getHeight();
    if (nativeAllocateWithFbo(mGLEnvironment, paramInt, i, j)) {
      return;
    }
    throw new RuntimeException("Could not allocate FBO backed GL frame!");
  }
  
  private void initWithTexture(int paramInt)
  {
    int i = getFormat().getWidth();
    int j = getFormat().getHeight();
    if (nativeAllocateWithTexture(mGLEnvironment, paramInt, i, j))
    {
      mOwnsTexture = false;
      markReadOnly();
      return;
    }
    throw new RuntimeException("Could not allocate texture backed GL frame!");
  }
  
  private native boolean nativeAllocate(GLEnvironment paramGLEnvironment, int paramInt1, int paramInt2);
  
  private native boolean nativeAllocateExternal(GLEnvironment paramGLEnvironment);
  
  private native boolean nativeAllocateWithFbo(GLEnvironment paramGLEnvironment, int paramInt1, int paramInt2, int paramInt3);
  
  private native boolean nativeAllocateWithTexture(GLEnvironment paramGLEnvironment, int paramInt1, int paramInt2, int paramInt3);
  
  private native boolean nativeCopyFromGL(GLFrame paramGLFrame);
  
  private native boolean nativeCopyFromNative(NativeFrame paramNativeFrame);
  
  private native boolean nativeDeallocate();
  
  private native boolean nativeDetachTexFromFbo();
  
  private native boolean nativeFocus();
  
  private native boolean nativeReattachTexToFbo();
  
  private native boolean nativeResetParams();
  
  private native boolean setNativeBitmap(Bitmap paramBitmap, int paramInt);
  
  private native boolean setNativeData(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private native boolean setNativeFloats(float[] paramArrayOfFloat);
  
  private native boolean setNativeInts(int[] paramArrayOfInt);
  
  private native boolean setNativeTextureParam(int paramInt1, int paramInt2);
  
  private native boolean setNativeViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void flushGPU(String paramString)
  {
    StopWatchMap localStopWatchMap = GLFrameTimer.get();
    if (LOG_MFF_RUNNING_TIMES)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("glFinish ");
      localStringBuilder.append(paramString);
      localStopWatchMap.start(localStringBuilder.toString());
      GLES20.glFinish();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("glFinish ");
      localStringBuilder.append(paramString);
      localStopWatchMap.stop(localStringBuilder.toString());
    }
  }
  
  public void focus()
  {
    if (nativeFocus()) {
      return;
    }
    throw new RuntimeException("Could not focus on GLFrame for drawing!");
  }
  
  public void generateMipMap()
  {
    assertFrameMutable();
    assertGLEnvValid();
    if (generateNativeMipMap()) {
      return;
    }
    throw new RuntimeException("Could not generate mip-map for GL frame!");
  }
  
  public Bitmap getBitmap()
  {
    assertGLEnvValid();
    flushGPU("getBitmap");
    Bitmap localBitmap = Bitmap.createBitmap(getFormat().getWidth(), getFormat().getHeight(), Bitmap.Config.ARGB_8888);
    if (getNativeBitmap(localBitmap)) {
      return localBitmap;
    }
    throw new RuntimeException("Could not get bitmap data from GL frame!");
  }
  
  public ByteBuffer getData()
  {
    assertGLEnvValid();
    flushGPU("getData");
    return ByteBuffer.wrap(getNativeData());
  }
  
  public int getFboId()
  {
    return getNativeFboId();
  }
  
  public float[] getFloats()
  {
    assertGLEnvValid();
    flushGPU("getFloats");
    return getNativeFloats();
  }
  
  public GLEnvironment getGLEnvironment()
  {
    return mGLEnvironment;
  }
  
  public int[] getInts()
  {
    assertGLEnvValid();
    flushGPU("getInts");
    return getNativeInts();
  }
  
  public Object getObjectValue()
  {
    assertGLEnvValid();
    return ByteBuffer.wrap(getNativeData());
  }
  
  public int getTextureId()
  {
    return getNativeTextureId();
  }
  
  protected boolean hasNativeAllocation()
  {
    try
    {
      int i = glFrameId;
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
  
  void init(GLEnvironment paramGLEnvironment)
  {
    FrameFormat localFrameFormat = getFormat();
    mGLEnvironment = paramGLEnvironment;
    if (localFrameFormat.getBytesPerSample() == 4)
    {
      if (localFrameFormat.getDimensionCount() == 2)
      {
        if (getFormat().getSize() >= 0)
        {
          int i = getBindingType();
          boolean bool = true;
          if (i == 0)
          {
            initNew(false);
          }
          else if (i == 104)
          {
            initNew(true);
            bool = false;
          }
          else if (i == 100)
          {
            initWithTexture((int)getBindingId());
          }
          else if (i == 101)
          {
            initWithFbo((int)getBindingId());
          }
          else if (i == 102)
          {
            initWithTexture((int)getBindingId());
          }
          else
          {
            if (i != 103) {
              break label149;
            }
            initWithFbo((int)getBindingId());
          }
          setReusable(bool);
          return;
          label149:
          paramGLEnvironment = new StringBuilder();
          paramGLEnvironment.append("Attempting to create GL frame with unknown binding type ");
          paramGLEnvironment.append(i);
          paramGLEnvironment.append("!");
          throw new RuntimeException(paramGLEnvironment.toString());
        }
        throw new IllegalArgumentException("Initializing GL frame with zero size!");
      }
      throw new IllegalArgumentException("GL frames must be 2-dimensional!");
    }
    throw new IllegalArgumentException("GL frames must have 4 bytes per sample!");
  }
  
  protected void onFrameFetch()
  {
    if (!mOwnsTexture) {
      nativeReattachTexToFbo();
    }
  }
  
  protected void onFrameStore()
  {
    if (!mOwnsTexture) {
      nativeDetachTexFromFbo();
    }
  }
  
  protected void releaseNativeAllocation()
  {
    try
    {
      nativeDeallocate();
      glFrameId = -1;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected void reset(FrameFormat paramFrameFormat)
  {
    if (nativeResetParams())
    {
      super.reset(paramFrameFormat);
      return;
    }
    throw new RuntimeException("Could not reset GLFrame texture parameters!");
  }
  
  public void setBitmap(Bitmap paramBitmap)
  {
    assertFrameMutable();
    assertGLEnvValid();
    if ((getFormat().getWidth() == paramBitmap.getWidth()) && (getFormat().getHeight() == paramBitmap.getHeight()))
    {
      paramBitmap = convertBitmapToRGBA(paramBitmap);
      if (setNativeBitmap(paramBitmap, paramBitmap.getByteCount())) {
        return;
      }
      throw new RuntimeException("Could not set GL frame bitmap data!");
    }
    throw new RuntimeException("Bitmap dimensions do not match GL frame dimensions!");
  }
  
  public void setData(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    assertFrameMutable();
    assertGLEnvValid();
    paramByteBuffer = paramByteBuffer.array();
    if (getFormat().getSize() == paramByteBuffer.length)
    {
      if (setNativeData(paramByteBuffer, paramInt1, paramInt2)) {
        return;
      }
      throw new RuntimeException("Could not set GL frame data!");
    }
    throw new RuntimeException("Data size in setData does not match GL frame size!");
  }
  
  public void setDataFromFrame(Frame paramFrame)
  {
    assertGLEnvValid();
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
    localStringBuilder.append(" to smaller GL frame of size ");
    localStringBuilder.append(getFormat().getSize());
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setFloats(float[] paramArrayOfFloat)
  {
    assertFrameMutable();
    assertGLEnvValid();
    if (setNativeFloats(paramArrayOfFloat)) {
      return;
    }
    throw new RuntimeException("Could not set int values for GL frame!");
  }
  
  public void setInts(int[] paramArrayOfInt)
  {
    assertFrameMutable();
    assertGLEnvValid();
    if (setNativeInts(paramArrayOfInt)) {
      return;
    }
    throw new RuntimeException("Could not set int values for GL frame!");
  }
  
  public void setTextureParameter(int paramInt1, int paramInt2)
  {
    assertFrameMutable();
    assertGLEnvValid();
    if (setNativeTextureParam(paramInt1, paramInt2)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set texture value ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" = ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(" for GLFrame!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    assertFrameMutable();
    setNativeViewport(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setViewport(Rect paramRect)
  {
    assertFrameMutable();
    setNativeViewport(left, top, right - left, bottom - top);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("GLFrame id: ");
    localStringBuilder.append(glFrameId);
    localStringBuilder.append(" (");
    localStringBuilder.append(getFormat());
    localStringBuilder.append(") with texture ID ");
    localStringBuilder.append(getTextureId());
    localStringBuilder.append(", FBO ID ");
    localStringBuilder.append(getFboId());
    return localStringBuilder.toString();
  }
}
