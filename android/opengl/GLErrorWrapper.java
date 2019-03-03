package android.opengl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

class GLErrorWrapper
  extends GLWrapperBase
{
  boolean mCheckError;
  boolean mCheckThread;
  Thread mOurThread;
  
  public GLErrorWrapper(GL paramGL, int paramInt)
  {
    super(paramGL);
    boolean bool1 = false;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mCheckError = bool2;
    boolean bool2 = bool1;
    if ((paramInt & 0x2) != 0) {
      bool2 = true;
    }
    mCheckThread = bool2;
  }
  
  private void checkError()
  {
    if (mCheckError)
    {
      int i = mgl.glGetError();
      if (i != 0) {
        throw new GLException(i);
      }
    }
  }
  
  private void checkThread()
  {
    if (mCheckThread)
    {
      Thread localThread = Thread.currentThread();
      if (mOurThread == null) {
        mOurThread = localThread;
      } else if (!mOurThread.equals(localThread)) {
        throw new GLException(28672, "OpenGL method called from wrong thread.");
      }
    }
  }
  
  public void glActiveTexture(int paramInt)
  {
    checkThread();
    mgl.glActiveTexture(paramInt);
    checkError();
  }
  
  public void glAlphaFunc(int paramInt, float paramFloat)
  {
    checkThread();
    mgl.glAlphaFunc(paramInt, paramFloat);
    checkError();
  }
  
  public void glAlphaFuncx(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glAlphaFuncx(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBindBuffer(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl11.glBindBuffer(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBindFramebufferOES(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glBindFramebufferOES(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBindRenderbufferOES(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glBindRenderbufferOES(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBindTexture(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glBindTexture(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBlendEquation(int paramInt)
  {
    checkThread();
    mgl11ExtensionPack.glBlendEquation(paramInt);
    checkError();
  }
  
  public void glBlendEquationSeparate(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glBlendEquationSeparate(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBlendFunc(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glBlendFunc(paramInt1, paramInt2);
    checkError();
  }
  
  public void glBlendFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11ExtensionPack.glBlendFuncSeparate(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glBufferData(int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3)
  {
    checkThread();
    mgl11.glBufferData(paramInt1, paramInt2, paramBuffer, paramInt3);
    checkError();
  }
  
  public void glBufferSubData(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl11.glBufferSubData(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public int glCheckFramebufferStatusOES(int paramInt)
  {
    checkThread();
    paramInt = mgl11ExtensionPack.glCheckFramebufferStatusOES(paramInt);
    checkError();
    return paramInt;
  }
  
  public void glClear(int paramInt)
  {
    checkThread();
    mgl.glClear(paramInt);
    checkError();
  }
  
  public void glClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkThread();
    mgl.glClearColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    checkError();
  }
  
  public void glClearColorx(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl.glClearColorx(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glClearDepthf(float paramFloat)
  {
    checkThread();
    mgl.glClearDepthf(paramFloat);
    checkError();
  }
  
  public void glClearDepthx(int paramInt)
  {
    checkThread();
    mgl.glClearDepthx(paramInt);
    checkError();
  }
  
  public void glClearStencil(int paramInt)
  {
    checkThread();
    mgl.glClearStencil(paramInt);
    checkError();
  }
  
  public void glClientActiveTexture(int paramInt)
  {
    checkThread();
    mgl.glClientActiveTexture(paramInt);
    checkError();
  }
  
  public void glClipPlanef(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glClipPlanef(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glClipPlanef(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl11.glClipPlanef(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glClipPlanex(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glClipPlanex(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glClipPlanex(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glClipPlanex(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glColor4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkThread();
    mgl.glColor4f(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    checkError();
  }
  
  public void glColor4ub(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
  {
    checkThread();
    mgl11.glColor4ub(paramByte1, paramByte2, paramByte3, paramByte4);
    checkError();
  }
  
  public void glColor4x(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl.glColor4x(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glColorMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    checkThread();
    mgl.glColorMask(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
    checkError();
  }
  
  public void glColorPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11.glColorPointer(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glColorPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl.glColorPointer(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public void glCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Buffer paramBuffer)
  {
    checkThread();
    mgl.glCompressedTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramBuffer);
    checkError();
  }
  
  public void glCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer)
  {
    checkThread();
    mgl.glCompressedTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBuffer);
    checkError();
  }
  
  public void glCopyTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    checkThread();
    mgl.glCopyTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    checkError();
  }
  
  public void glCopyTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    checkThread();
    mgl.glCopyTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    checkError();
  }
  
  public void glCullFace(int paramInt)
  {
    checkThread();
    mgl.glCullFace(paramInt);
    checkError();
  }
  
  public void glCurrentPaletteMatrixOES(int paramInt)
  {
    checkThread();
    mgl11Ext.glCurrentPaletteMatrixOES(paramInt);
    checkError();
  }
  
  public void glDeleteBuffers(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glDeleteBuffers(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glDeleteBuffers(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glDeleteBuffers(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glDeleteFramebuffersOES(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glDeleteFramebuffersOES(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glDeleteFramebuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glDeleteFramebuffersOES(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glDeleteRenderbuffersOES(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glDeleteRenderbuffersOES(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glDeleteRenderbuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glDeleteRenderbuffersOES(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glDeleteTextures(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glDeleteTextures(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glDeleteTextures(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl.glDeleteTextures(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glDepthFunc(int paramInt)
  {
    checkThread();
    mgl.glDepthFunc(paramInt);
    checkError();
  }
  
  public void glDepthMask(boolean paramBoolean)
  {
    checkThread();
    mgl.glDepthMask(paramBoolean);
    checkError();
  }
  
  public void glDepthRangef(float paramFloat1, float paramFloat2)
  {
    checkThread();
    mgl.glDepthRangef(paramFloat1, paramFloat2);
    checkError();
  }
  
  public void glDepthRangex(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glDepthRangex(paramInt1, paramInt2);
    checkError();
  }
  
  public void glDisable(int paramInt)
  {
    checkThread();
    mgl.glDisable(paramInt);
    checkError();
  }
  
  public void glDisableClientState(int paramInt)
  {
    checkThread();
    mgl.glDisableClientState(paramInt);
    checkError();
  }
  
  public void glDrawArrays(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glDrawArrays(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glDrawElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11.glDrawElements(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glDrawElements(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl.glDrawElements(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public void glDrawTexfOES(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    checkThread();
    mgl11Ext.glDrawTexfOES(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5);
    checkError();
  }
  
  public void glDrawTexfvOES(FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11Ext.glDrawTexfvOES(paramFloatBuffer);
    checkError();
  }
  
  public void glDrawTexfvOES(float[] paramArrayOfFloat, int paramInt)
  {
    checkThread();
    mgl11Ext.glDrawTexfvOES(paramArrayOfFloat, paramInt);
    checkError();
  }
  
  public void glDrawTexiOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    checkThread();
    mgl11Ext.glDrawTexiOES(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    checkError();
  }
  
  public void glDrawTexivOES(IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11Ext.glDrawTexivOES(paramIntBuffer);
    checkError();
  }
  
  public void glDrawTexivOES(int[] paramArrayOfInt, int paramInt)
  {
    checkThread();
    mgl11Ext.glDrawTexivOES(paramArrayOfInt, paramInt);
    checkError();
  }
  
  public void glDrawTexsOES(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5)
  {
    checkThread();
    mgl11Ext.glDrawTexsOES(paramShort1, paramShort2, paramShort3, paramShort4, paramShort5);
    checkError();
  }
  
  public void glDrawTexsvOES(ShortBuffer paramShortBuffer)
  {
    checkThread();
    mgl11Ext.glDrawTexsvOES(paramShortBuffer);
    checkError();
  }
  
  public void glDrawTexsvOES(short[] paramArrayOfShort, int paramInt)
  {
    checkThread();
    mgl11Ext.glDrawTexsvOES(paramArrayOfShort, paramInt);
    checkError();
  }
  
  public void glDrawTexxOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    checkThread();
    mgl11Ext.glDrawTexxOES(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    checkError();
  }
  
  public void glDrawTexxvOES(IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11Ext.glDrawTexxvOES(paramIntBuffer);
    checkError();
  }
  
  public void glDrawTexxvOES(int[] paramArrayOfInt, int paramInt)
  {
    checkThread();
    mgl11Ext.glDrawTexxvOES(paramArrayOfInt, paramInt);
    checkError();
  }
  
  public void glEnable(int paramInt)
  {
    checkThread();
    mgl.glEnable(paramInt);
    checkError();
  }
  
  public void glEnableClientState(int paramInt)
  {
    checkThread();
    mgl.glEnableClientState(paramInt);
    checkError();
  }
  
  public void glFinish()
  {
    checkThread();
    mgl.glFinish();
    checkError();
  }
  
  public void glFlush()
  {
    checkThread();
    mgl.glFlush();
    checkError();
  }
  
  public void glFogf(int paramInt, float paramFloat)
  {
    checkThread();
    mgl.glFogf(paramInt, paramFloat);
    checkError();
  }
  
  public void glFogfv(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glFogfv(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glFogfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl.glFogfv(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glFogx(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glFogx(paramInt1, paramInt2);
    checkError();
  }
  
  public void glFogxv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glFogxv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glFogxv(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl.glFogxv(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glFramebufferRenderbufferOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11ExtensionPack.glFramebufferRenderbufferOES(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glFramebufferTexture2DOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    checkThread();
    mgl11ExtensionPack.glFramebufferTexture2DOES(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    checkError();
  }
  
  public void glFrontFace(int paramInt)
  {
    checkThread();
    mgl.glFrontFace(paramInt);
    checkError();
  }
  
  public void glFrustumf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    checkThread();
    mgl.glFrustumf(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    checkError();
  }
  
  public void glFrustumx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    checkThread();
    mgl.glFrustumx(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    checkError();
  }
  
  public void glGenBuffers(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGenBuffers(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGenBuffers(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glGenBuffers(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGenFramebuffersOES(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGenFramebuffersOES(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGenFramebuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glGenFramebuffersOES(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGenRenderbuffersOES(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGenRenderbuffersOES(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGenRenderbuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11ExtensionPack.glGenRenderbuffersOES(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGenTextures(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glGenTextures(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGenTextures(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl.glGenTextures(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGenerateMipmapOES(int paramInt)
  {
    checkThread();
    mgl11ExtensionPack.glGenerateMipmapOES(paramInt);
    checkError();
  }
  
  public void glGetBooleanv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetBooleanv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGetBooleanv(int paramInt1, boolean[] paramArrayOfBoolean, int paramInt2)
  {
    checkThread();
    mgl11.glGetBooleanv(paramInt1, paramArrayOfBoolean, paramInt2);
    checkError();
  }
  
  public void glGetBufferParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetBufferParameteriv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetBufferParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetBufferParameteriv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetClipPlanef(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glGetClipPlanef(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glGetClipPlanef(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl11.glGetClipPlanef(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glGetClipPlanex(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetClipPlanex(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGetClipPlanex(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glGetClipPlanex(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public int glGetError()
  {
    checkThread();
    return mgl.glGetError();
  }
  
  public void glGetFixedv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetFixedv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGetFixedv(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glGetFixedv(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGetFloatv(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glGetFloatv(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glGetFloatv(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl11.glGetFloatv(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glGetFramebufferAttachmentParameterivOES(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGetFramebufferAttachmentParameterivOES(paramInt1, paramInt2, paramInt3, paramIntBuffer);
    checkError();
  }
  
  public void glGetFramebufferAttachmentParameterivOES(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4)
  {
    checkThread();
    mgl11ExtensionPack.glGetFramebufferAttachmentParameterivOES(paramInt1, paramInt2, paramInt3, paramArrayOfInt, paramInt4);
    checkError();
  }
  
  public void glGetIntegerv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glGetIntegerv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glGetIntegerv(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl.glGetIntegerv(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glGetLightfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glGetLightfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glGetLightfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11.glGetLightfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glGetLightxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetLightxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetLightxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetLightxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetMaterialfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glGetMaterialfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glGetMaterialfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11.glGetMaterialfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glGetMaterialxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetMaterialxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetMaterialxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetMaterialxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetPointerv(int paramInt, Buffer[] paramArrayOfBuffer)
  {
    checkThread();
    mgl11.glGetPointerv(paramInt, paramArrayOfBuffer);
    checkError();
  }
  
  public void glGetRenderbufferParameterivOES(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGetRenderbufferParameterivOES(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetRenderbufferParameterivOES(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glGetRenderbufferParameterivOES(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public String glGetString(int paramInt)
  {
    checkThread();
    String str = mgl.glGetString(paramInt);
    checkError();
    return str;
  }
  
  public void glGetTexEnviv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetTexEnviv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexEnviv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetTexEnviv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetTexEnvxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetTexEnvxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexEnvxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetTexEnvxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetTexGenfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGenfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glGetTexGenfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGenfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glGetTexGeniv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGeniv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexGeniv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGeniv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetTexGenxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGenxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexGenxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glGetTexGenxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetTexParameterfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glGetTexParameterfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glGetTexParameterfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11.glGetTexParameterfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glGetTexParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetTexParameteriv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetTexParameteriv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glGetTexParameterxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glGetTexParameterxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glGetTexParameterxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glGetTexParameterxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glHint(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glHint(paramInt1, paramInt2);
    checkError();
  }
  
  public boolean glIsBuffer(int paramInt)
  {
    checkThread();
    boolean bool = mgl11.glIsBuffer(paramInt);
    checkError();
    return bool;
  }
  
  public boolean glIsEnabled(int paramInt)
  {
    checkThread();
    boolean bool = mgl11.glIsEnabled(paramInt);
    checkError();
    return bool;
  }
  
  public boolean glIsFramebufferOES(int paramInt)
  {
    checkThread();
    boolean bool = mgl11ExtensionPack.glIsFramebufferOES(paramInt);
    checkError();
    return bool;
  }
  
  public boolean glIsRenderbufferOES(int paramInt)
  {
    checkThread();
    mgl11ExtensionPack.glIsRenderbufferOES(paramInt);
    checkError();
    return false;
  }
  
  public boolean glIsTexture(int paramInt)
  {
    checkThread();
    boolean bool = mgl11.glIsTexture(paramInt);
    checkError();
    return bool;
  }
  
  public void glLightModelf(int paramInt, float paramFloat)
  {
    checkThread();
    mgl.glLightModelf(paramInt, paramFloat);
    checkError();
  }
  
  public void glLightModelfv(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glLightModelfv(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glLightModelfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl.glLightModelfv(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glLightModelx(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glLightModelx(paramInt1, paramInt2);
    checkError();
  }
  
  public void glLightModelxv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glLightModelxv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glLightModelxv(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl.glLightModelxv(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glLightf(int paramInt1, int paramInt2, float paramFloat)
  {
    checkThread();
    mgl.glLightf(paramInt1, paramInt2, paramFloat);
    checkError();
  }
  
  public void glLightfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glLightfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glLightfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl.glLightfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glLightx(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glLightx(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glLightxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glLightxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glLightxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl.glLightxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glLineWidth(float paramFloat)
  {
    checkThread();
    mgl.glLineWidth(paramFloat);
    checkError();
  }
  
  public void glLineWidthx(int paramInt)
  {
    checkThread();
    mgl.glLineWidthx(paramInt);
    checkError();
  }
  
  public void glLoadIdentity()
  {
    checkThread();
    mgl.glLoadIdentity();
    checkError();
  }
  
  public void glLoadMatrixf(FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glLoadMatrixf(paramFloatBuffer);
    checkError();
  }
  
  public void glLoadMatrixf(float[] paramArrayOfFloat, int paramInt)
  {
    checkThread();
    mgl.glLoadMatrixf(paramArrayOfFloat, paramInt);
    checkError();
  }
  
  public void glLoadMatrixx(IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glLoadMatrixx(paramIntBuffer);
    checkError();
  }
  
  public void glLoadMatrixx(int[] paramArrayOfInt, int paramInt)
  {
    checkThread();
    mgl.glLoadMatrixx(paramArrayOfInt, paramInt);
    checkError();
  }
  
  public void glLoadPaletteFromModelViewMatrixOES()
  {
    checkThread();
    mgl11Ext.glLoadPaletteFromModelViewMatrixOES();
    checkError();
  }
  
  public void glLogicOp(int paramInt)
  {
    checkThread();
    mgl.glLogicOp(paramInt);
    checkError();
  }
  
  public void glMaterialf(int paramInt1, int paramInt2, float paramFloat)
  {
    checkThread();
    mgl.glMaterialf(paramInt1, paramInt2, paramFloat);
    checkError();
  }
  
  public void glMaterialfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glMaterialfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glMaterialfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl.glMaterialfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glMaterialx(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glMaterialx(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glMaterialxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glMaterialxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glMaterialxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl.glMaterialxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glMatrixIndexPointerOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11Ext.glMatrixIndexPointerOES(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glMatrixIndexPointerOES(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl11Ext.glMatrixIndexPointerOES(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public void glMatrixMode(int paramInt)
  {
    checkThread();
    mgl.glMatrixMode(paramInt);
    checkError();
  }
  
  public void glMultMatrixf(FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glMultMatrixf(paramFloatBuffer);
    checkError();
  }
  
  public void glMultMatrixf(float[] paramArrayOfFloat, int paramInt)
  {
    checkThread();
    mgl.glMultMatrixf(paramArrayOfFloat, paramInt);
    checkError();
  }
  
  public void glMultMatrixx(IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glMultMatrixx(paramIntBuffer);
    checkError();
  }
  
  public void glMultMatrixx(int[] paramArrayOfInt, int paramInt)
  {
    checkThread();
    mgl.glMultMatrixx(paramArrayOfInt, paramInt);
    checkError();
  }
  
  public void glMultiTexCoord4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkThread();
    mgl.glMultiTexCoord4f(paramInt, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    checkError();
  }
  
  public void glMultiTexCoord4x(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    checkThread();
    mgl.glMultiTexCoord4x(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    checkError();
  }
  
  public void glNormal3f(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    checkThread();
    mgl.glNormal3f(paramFloat1, paramFloat2, paramFloat3);
    checkError();
  }
  
  public void glNormal3x(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glNormal3x(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glNormalPointer(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl11.glNormalPointer(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glNormalPointer(int paramInt1, int paramInt2, Buffer paramBuffer)
  {
    checkThread();
    mgl.glNormalPointer(paramInt1, paramInt2, paramBuffer);
    checkError();
  }
  
  public void glOrthof(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    checkThread();
    mgl.glOrthof(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
    checkError();
  }
  
  public void glOrthox(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    checkThread();
    mgl.glOrthox(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    checkError();
  }
  
  public void glPixelStorei(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glPixelStorei(paramInt1, paramInt2);
    checkError();
  }
  
  public void glPointParameterf(int paramInt, float paramFloat)
  {
    checkThread();
    mgl11.glPointParameterf(paramInt, paramFloat);
    checkError();
  }
  
  public void glPointParameterfv(int paramInt, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glPointParameterfv(paramInt, paramFloatBuffer);
    checkError();
  }
  
  public void glPointParameterfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    checkThread();
    mgl11.glPointParameterfv(paramInt1, paramArrayOfFloat, paramInt2);
    checkError();
  }
  
  public void glPointParameterx(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl11.glPointParameterx(paramInt1, paramInt2);
    checkError();
  }
  
  public void glPointParameterxv(int paramInt, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glPointParameterxv(paramInt, paramIntBuffer);
    checkError();
  }
  
  public void glPointParameterxv(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    checkThread();
    mgl11.glPointParameterxv(paramInt1, paramArrayOfInt, paramInt2);
    checkError();
  }
  
  public void glPointSize(float paramFloat)
  {
    checkThread();
    mgl.glPointSize(paramFloat);
    checkError();
  }
  
  public void glPointSizePointerOES(int paramInt1, int paramInt2, Buffer paramBuffer)
  {
    checkThread();
    mgl11.glPointSizePointerOES(paramInt1, paramInt2, paramBuffer);
    checkError();
  }
  
  public void glPointSizex(int paramInt)
  {
    checkThread();
    mgl.glPointSizex(paramInt);
    checkError();
  }
  
  public void glPolygonOffset(float paramFloat1, float paramFloat2)
  {
    checkThread();
    mgl.glPolygonOffset(paramFloat1, paramFloat2);
    checkError();
  }
  
  public void glPolygonOffsetx(int paramInt1, int paramInt2)
  {
    checkThread();
    mgl.glPolygonOffsetx(paramInt1, paramInt2);
    checkError();
  }
  
  public void glPopMatrix()
  {
    checkThread();
    mgl.glPopMatrix();
    checkError();
  }
  
  public void glPushMatrix()
  {
    checkThread();
    mgl.glPushMatrix();
    checkError();
  }
  
  public int glQueryMatrixxOES(IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2)
  {
    checkThread();
    int i = mgl10Ext.glQueryMatrixxOES(paramIntBuffer1, paramIntBuffer2);
    checkError();
    return i;
  }
  
  public int glQueryMatrixxOES(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2)
  {
    checkThread();
    paramInt1 = mgl10Ext.glQueryMatrixxOES(paramArrayOfInt1, paramInt1, paramArrayOfInt2, paramInt2);
    checkError();
    return paramInt1;
  }
  
  public void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Buffer paramBuffer)
  {
    checkThread();
    mgl.glReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBuffer);
    checkError();
  }
  
  public void glRenderbufferStorageOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11ExtensionPack.glRenderbufferStorageOES(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glRotatef(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    checkThread();
    mgl.glRotatef(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    checkError();
  }
  
  public void glRotatex(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl.glRotatex(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glSampleCoverage(float paramFloat, boolean paramBoolean)
  {
    checkThread();
    mgl.glSampleCoverage(paramFloat, paramBoolean);
    checkError();
  }
  
  public void glSampleCoveragex(int paramInt, boolean paramBoolean)
  {
    checkThread();
    mgl.glSampleCoveragex(paramInt, paramBoolean);
    checkError();
  }
  
  public void glScalef(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    checkThread();
    mgl.glScalef(paramFloat1, paramFloat2, paramFloat3);
    checkError();
  }
  
  public void glScalex(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glScalex(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl.glScissor(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glShadeModel(int paramInt)
  {
    checkThread();
    mgl.glShadeModel(paramInt);
    checkError();
  }
  
  public void glStencilFunc(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glStencilFunc(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glStencilMask(int paramInt)
  {
    checkThread();
    mgl.glStencilMask(paramInt);
    checkError();
  }
  
  public void glStencilOp(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glStencilOp(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11.glTexCoordPointer(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl.glTexCoordPointer(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public void glTexEnvf(int paramInt1, int paramInt2, float paramFloat)
  {
    checkThread();
    mgl.glTexEnvf(paramInt1, paramInt2, paramFloat);
    checkError();
  }
  
  public void glTexEnvfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl.glTexEnvfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glTexEnvfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl.glTexEnvfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glTexEnvi(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl11.glTexEnvi(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexEnviv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glTexEnviv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexEnviv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glTexEnviv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexEnvx(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glTexEnvx(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexEnvxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl.glTexEnvxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexEnvxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl.glTexEnvxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexGenf(int paramInt1, int paramInt2, float paramFloat)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenf(paramInt1, paramInt2, paramFloat);
    checkError();
  }
  
  public void glTexGenfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glTexGenfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glTexGeni(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glTexGeni(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexGeniv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glTexGeniv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexGeniv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glTexGeniv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexGenx(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenx(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexGenxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexGenxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11ExtensionPack.glTexGenxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer)
  {
    checkThread();
    mgl.glTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBuffer);
    checkError();
  }
  
  public void glTexParameterf(int paramInt1, int paramInt2, float paramFloat)
  {
    checkThread();
    mgl.glTexParameterf(paramInt1, paramInt2, paramFloat);
    checkError();
  }
  
  public void glTexParameterfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer)
  {
    checkThread();
    mgl11.glTexParameterfv(paramInt1, paramInt2, paramFloatBuffer);
    checkError();
  }
  
  public void glTexParameterfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
  {
    checkThread();
    mgl11.glTexParameterfv(paramInt1, paramInt2, paramArrayOfFloat, paramInt3);
    checkError();
  }
  
  public void glTexParameteri(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl11.glTexParameteri(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glTexParameteriv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glTexParameteriv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexParameterx(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glTexParameterx(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glTexParameterxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer)
  {
    checkThread();
    mgl11.glTexParameterxv(paramInt1, paramInt2, paramIntBuffer);
    checkError();
  }
  
  public void glTexParameterxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    checkThread();
    mgl11.glTexParameterxv(paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    checkError();
  }
  
  public void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer)
  {
    checkThread();
    mgl.glTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBuffer);
    checkError();
  }
  
  public void glTranslatef(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    checkThread();
    mgl.glTranslatef(paramFloat1, paramFloat2, paramFloat3);
    checkError();
  }
  
  public void glTranslatex(int paramInt1, int paramInt2, int paramInt3)
  {
    checkThread();
    mgl.glTranslatex(paramInt1, paramInt2, paramInt3);
    checkError();
  }
  
  public void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11.glVertexPointer(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl.glVertexPointer(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
  
  public void glViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl.glViewport(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glWeightPointerOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkThread();
    mgl11Ext.glWeightPointerOES(paramInt1, paramInt2, paramInt3, paramInt4);
    checkError();
  }
  
  public void glWeightPointerOES(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    checkThread();
    mgl11Ext.glWeightPointerOES(paramInt1, paramInt2, paramInt3, paramBuffer);
    checkError();
  }
}
