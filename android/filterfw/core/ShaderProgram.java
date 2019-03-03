package android.filterfw.core;

import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
import android.opengl.GLES20;

public class ShaderProgram
  extends Program
{
  private GLEnvironment mGLEnvironment;
  private int mMaxTileSize = 0;
  private StopWatchMap mTimer = null;
  private int shaderProgramId;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  private ShaderProgram() {}
  
  public ShaderProgram(FilterContext paramFilterContext, String paramString)
  {
    mGLEnvironment = getGLEnvironment(paramFilterContext);
    allocate(mGLEnvironment, null, paramString);
    if (compileAndLink())
    {
      setTimer();
      return;
    }
    throw new RuntimeException("Could not compile and link shader!");
  }
  
  public ShaderProgram(FilterContext paramFilterContext, String paramString1, String paramString2)
  {
    mGLEnvironment = getGLEnvironment(paramFilterContext);
    allocate(mGLEnvironment, paramString1, paramString2);
    if (compileAndLink())
    {
      setTimer();
      return;
    }
    throw new RuntimeException("Could not compile and link shader!");
  }
  
  private ShaderProgram(NativeAllocatorTag paramNativeAllocatorTag) {}
  
  private native boolean allocate(GLEnvironment paramGLEnvironment, String paramString1, String paramString2);
  
  private native boolean beginShaderDrawing();
  
  private native boolean compileAndLink();
  
  public static ShaderProgram createIdentity(FilterContext paramFilterContext)
  {
    paramFilterContext = nativeCreateIdentity(getGLEnvironment(paramFilterContext));
    paramFilterContext.setTimer();
    return paramFilterContext;
  }
  
  private native boolean deallocate();
  
  private static GLEnvironment getGLEnvironment(FilterContext paramFilterContext)
  {
    if (paramFilterContext != null) {
      paramFilterContext = paramFilterContext.getGLEnvironment();
    } else {
      paramFilterContext = null;
    }
    if (paramFilterContext != null) {
      return paramFilterContext;
    }
    throw new NullPointerException("Attempting to create ShaderProgram with no GL environment in place!");
  }
  
  private native Object getUniformValue(String paramString);
  
  private static native ShaderProgram nativeCreateIdentity(GLEnvironment paramGLEnvironment);
  
  private native boolean setShaderAttributeValues(String paramString, float[] paramArrayOfFloat, int paramInt);
  
  private native boolean setShaderAttributeVertexFrame(String paramString, VertexFrame paramVertexFrame, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  private native boolean setShaderBlendEnabled(boolean paramBoolean);
  
  private native boolean setShaderBlendFunc(int paramInt1, int paramInt2);
  
  private native boolean setShaderClearColor(float paramFloat1, float paramFloat2, float paramFloat3);
  
  private native boolean setShaderClearsOutput(boolean paramBoolean);
  
  private native boolean setShaderDrawMode(int paramInt);
  
  private native boolean setShaderTileCounts(int paramInt1, int paramInt2);
  
  private native boolean setShaderVertexCount(int paramInt);
  
  private native boolean setTargetRegion(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8);
  
  private void setTimer()
  {
    mTimer = new StopWatchMap();
  }
  
  private native boolean setUniformValue(String paramString, Object paramObject);
  
  private native boolean shaderProcess(GLFrame[] paramArrayOfGLFrame, GLFrame paramGLFrame);
  
  public void beginDrawing()
  {
    if (beginShaderDrawing()) {
      return;
    }
    throw new RuntimeException("Could not prepare shader-program for drawing!");
  }
  
  protected void finalize()
    throws Throwable
  {
    deallocate();
  }
  
  public GLEnvironment getGLEnvironment()
  {
    return mGLEnvironment;
  }
  
  public Object getHostValue(String paramString)
  {
    return getUniformValue(paramString);
  }
  
  public void process(Frame[] paramArrayOfFrame, Frame paramFrame)
  {
    if (mTimer.LOG_MFF_RUNNING_TIMES)
    {
      mTimer.start("glFinish");
      GLES20.glFinish();
      mTimer.stop("glFinish");
    }
    GLFrame[] arrayOfGLFrame = new GLFrame[paramArrayOfFrame.length];
    int i = 0;
    while (i < paramArrayOfFrame.length) {
      if ((paramArrayOfFrame[i] instanceof GLFrame))
      {
        arrayOfGLFrame[i] = ((GLFrame)paramArrayOfFrame[i]);
        i++;
      }
      else
      {
        paramArrayOfFrame = new StringBuilder();
        paramArrayOfFrame.append("ShaderProgram got non-GL frame as input ");
        paramArrayOfFrame.append(i);
        paramArrayOfFrame.append("!");
        throw new RuntimeException(paramArrayOfFrame.toString());
      }
    }
    if ((paramFrame instanceof GLFrame))
    {
      paramArrayOfFrame = (GLFrame)paramFrame;
      if (mMaxTileSize > 0) {
        setShaderTileCounts((paramFrame.getFormat().getWidth() + mMaxTileSize - 1) / mMaxTileSize, (paramFrame.getFormat().getHeight() + mMaxTileSize - 1) / mMaxTileSize);
      }
      if (shaderProcess(arrayOfGLFrame, paramArrayOfFrame))
      {
        if (mTimer.LOG_MFF_RUNNING_TIMES) {
          GLES20.glFinish();
        }
        return;
      }
      throw new RuntimeException("Error executing ShaderProgram!");
    }
    throw new RuntimeException("ShaderProgram got non-GL output frame!");
  }
  
  public void setAttributeValues(String paramString, VertexFrame paramVertexFrame, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    if (setShaderAttributeVertexFrame(paramString, paramVertexFrame, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean)) {
      return;
    }
    paramVertexFrame = new StringBuilder();
    paramVertexFrame.append("Error setting attribute value for attribute '");
    paramVertexFrame.append(paramString);
    paramVertexFrame.append("'!");
    throw new RuntimeException(paramVertexFrame.toString());
  }
  
  public void setAttributeValues(String paramString, float[] paramArrayOfFloat, int paramInt)
  {
    if (setShaderAttributeValues(paramString, paramArrayOfFloat, paramInt)) {
      return;
    }
    paramArrayOfFloat = new StringBuilder();
    paramArrayOfFloat.append("Error setting attribute value for attribute '");
    paramArrayOfFloat.append(paramString);
    paramArrayOfFloat.append("'!");
    throw new RuntimeException(paramArrayOfFloat.toString());
  }
  
  public void setBlendEnabled(boolean paramBoolean)
  {
    if (setShaderBlendEnabled(paramBoolean)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set Blending ");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setBlendFunc(int paramInt1, int paramInt2)
  {
    if (setShaderBlendFunc(paramInt1, paramInt2)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set BlendFunc ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(",");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setClearColor(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (setShaderClearColor(paramFloat1, paramFloat2, paramFloat3)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set clear color to ");
    localStringBuilder.append(paramFloat1);
    localStringBuilder.append(",");
    localStringBuilder.append(paramFloat2);
    localStringBuilder.append(",");
    localStringBuilder.append(paramFloat3);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setClearsOutput(boolean paramBoolean)
  {
    if (setShaderClearsOutput(paramBoolean)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set clears-output flag to ");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setDrawMode(int paramInt)
  {
    if (setShaderDrawMode(paramInt)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set GL draw-mode to ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void setHostValue(String paramString, Object paramObject)
  {
    if (setUniformValue(paramString, paramObject)) {
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append("Error setting uniform value for variable '");
    paramObject.append(paramString);
    paramObject.append("'!");
    throw new RuntimeException(paramObject.toString());
  }
  
  public void setMaximumTileSize(int paramInt)
  {
    mMaxTileSize = paramInt;
  }
  
  public void setSourceRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    setSourceRegion(paramFloat1, paramFloat2, paramFloat1 + paramFloat3, paramFloat2, paramFloat1, paramFloat2 + paramFloat4, paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
  }
  
  public void setSourceRegion(Quad paramQuad)
  {
    setSourceRegion(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
  }
  
  public native boolean setSourceRegion(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8);
  
  public void setTargetRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    setTargetRegion(paramFloat1, paramFloat2, paramFloat1 + paramFloat3, paramFloat2, paramFloat1, paramFloat2 + paramFloat4, paramFloat1 + paramFloat3, paramFloat2 + paramFloat4);
  }
  
  public void setTargetRegion(Quad paramQuad)
  {
    setTargetRegion(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
  }
  
  public void setVertexCount(int paramInt)
  {
    if (setShaderVertexCount(paramInt)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not set GL vertex count to ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
}
