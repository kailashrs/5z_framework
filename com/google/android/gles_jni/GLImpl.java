package com.google.android.gles_jni;

import android.app.AppGlobals;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

public class GLImpl
  implements GL10, GL10Ext, GL11, GL11Ext, GL11ExtensionPack
{
  Buffer _colorPointer = null;
  Buffer _matrixIndexPointerOES = null;
  Buffer _normalPointer = null;
  Buffer _pointSizePointerOES = null;
  Buffer _texCoordPointer = null;
  Buffer _vertexPointer = null;
  Buffer _weightPointerOES = null;
  private boolean haveCheckedExtensions;
  private boolean have_OES_blend_equation_separate;
  private boolean have_OES_blend_subtract;
  private boolean have_OES_framebuffer_object;
  private boolean have_OES_texture_cube_map;
  
  static {}
  
  public GLImpl() {}
  
  private static native void _nativeClassInit();
  
  private static boolean allowIndirectBuffers(String paramString)
  {
    boolean bool = false;
    int i = 0;
    int j = 0;
    Object localObject = AppGlobals.getPackageManager();
    try
    {
      localObject = ((IPackageManager)localObject).getApplicationInfo(paramString, 0, UserHandle.myUserId());
      if (localObject != null) {
        j = targetSdkVersion;
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    Log.e("OpenGLES", String.format("Application %s (SDK target %d) called a GL11 Pointer method with an indirect Buffer.", new Object[] { paramString, Integer.valueOf(j) }));
    if (j <= 3) {
      bool = true;
    }
    return bool;
  }
  
  private native void glColorPointerBounds(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4);
  
  private native void glMatrixIndexPointerOESBounds(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4);
  
  private native void glNormalPointerBounds(int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3);
  
  private native void glPointSizePointerOESBounds(int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3);
  
  private native void glTexCoordPointerBounds(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4);
  
  private native void glVertexPointerBounds(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4);
  
  private native void glWeightPointerOESBounds(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4);
  
  public native String _glGetString(int paramInt);
  
  public native void glActiveTexture(int paramInt);
  
  public native void glAlphaFunc(int paramInt, float paramFloat);
  
  public native void glAlphaFuncx(int paramInt1, int paramInt2);
  
  public native void glBindBuffer(int paramInt1, int paramInt2);
  
  public native void glBindFramebufferOES(int paramInt1, int paramInt2);
  
  public native void glBindRenderbufferOES(int paramInt1, int paramInt2);
  
  public native void glBindTexture(int paramInt1, int paramInt2);
  
  public native void glBlendEquation(int paramInt);
  
  public native void glBlendEquationSeparate(int paramInt1, int paramInt2);
  
  public native void glBlendFunc(int paramInt1, int paramInt2);
  
  public native void glBlendFuncSeparate(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glBufferData(int paramInt1, int paramInt2, Buffer paramBuffer, int paramInt3);
  
  public native void glBufferSubData(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer);
  
  public native int glCheckFramebufferStatusOES(int paramInt);
  
  public native void glClear(int paramInt);
  
  public native void glClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public native void glClearColorx(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glClearDepthf(float paramFloat);
  
  public native void glClearDepthx(int paramInt);
  
  public native void glClearStencil(int paramInt);
  
  public native void glClientActiveTexture(int paramInt);
  
  public native void glClipPlanef(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glClipPlanef(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glClipPlanex(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glClipPlanex(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glColor4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public native void glColor4ub(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4);
  
  public native void glColor4x(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glColorMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4);
  
  public native void glColorPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public void glColorPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    glColorPointerBounds(paramInt1, paramInt2, paramInt3, paramBuffer, paramBuffer.remaining());
    if ((paramInt1 == 4) && ((paramInt2 == 5126) || (paramInt2 == 5121) || (paramInt2 == 5132)) && (paramInt3 >= 0)) {
      _colorPointer = paramBuffer;
    }
  }
  
  public native void glCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Buffer paramBuffer);
  
  public native void glCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer);
  
  public native void glCopyTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  
  public native void glCopyTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  
  public native void glCullFace(int paramInt);
  
  public native void glCurrentPaletteMatrixOES(int paramInt);
  
  public native void glDeleteBuffers(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glDeleteBuffers(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glDeleteFramebuffersOES(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glDeleteFramebuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glDeleteRenderbuffersOES(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glDeleteRenderbuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glDeleteTextures(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glDeleteTextures(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glDepthFunc(int paramInt);
  
  public native void glDepthMask(boolean paramBoolean);
  
  public native void glDepthRangef(float paramFloat1, float paramFloat2);
  
  public native void glDepthRangex(int paramInt1, int paramInt2);
  
  public native void glDisable(int paramInt);
  
  public native void glDisableClientState(int paramInt);
  
  public native void glDrawArrays(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glDrawElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glDrawElements(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer);
  
  public native void glDrawTexfOES(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5);
  
  public native void glDrawTexfvOES(FloatBuffer paramFloatBuffer);
  
  public native void glDrawTexfvOES(float[] paramArrayOfFloat, int paramInt);
  
  public native void glDrawTexiOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public native void glDrawTexivOES(IntBuffer paramIntBuffer);
  
  public native void glDrawTexivOES(int[] paramArrayOfInt, int paramInt);
  
  public native void glDrawTexsOES(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5);
  
  public native void glDrawTexsvOES(ShortBuffer paramShortBuffer);
  
  public native void glDrawTexsvOES(short[] paramArrayOfShort, int paramInt);
  
  public native void glDrawTexxOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public native void glDrawTexxvOES(IntBuffer paramIntBuffer);
  
  public native void glDrawTexxvOES(int[] paramArrayOfInt, int paramInt);
  
  public native void glEnable(int paramInt);
  
  public native void glEnableClientState(int paramInt);
  
  public native void glFinish();
  
  public native void glFlush();
  
  public native void glFogf(int paramInt, float paramFloat);
  
  public native void glFogfv(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glFogfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glFogx(int paramInt1, int paramInt2);
  
  public native void glFogxv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glFogxv(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glFramebufferRenderbufferOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glFramebufferTexture2DOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public native void glFrontFace(int paramInt);
  
  public native void glFrustumf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
  
  public native void glFrustumx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public native void glGenBuffers(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGenBuffers(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGenFramebuffersOES(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGenFramebuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGenRenderbuffersOES(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGenRenderbuffersOES(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGenTextures(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGenTextures(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGenerateMipmapOES(int paramInt);
  
  public native void glGetBooleanv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGetBooleanv(int paramInt1, boolean[] paramArrayOfBoolean, int paramInt2);
  
  public native void glGetBufferParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetBufferParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetClipPlanef(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glGetClipPlanef(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glGetClipPlanex(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGetClipPlanex(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native int glGetError();
  
  public native void glGetFixedv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGetFixedv(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGetFloatv(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glGetFloatv(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glGetFramebufferAttachmentParameterivOES(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer);
  
  public native void glGetFramebufferAttachmentParameterivOES(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4);
  
  public native void glGetIntegerv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glGetIntegerv(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glGetLightfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glGetLightfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glGetLightxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetLightxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetMaterialfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glGetMaterialfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glGetMaterialxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetMaterialxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public void glGetPointerv(int paramInt, Buffer[] paramArrayOfBuffer)
  {
    throw new UnsupportedOperationException("glGetPointerv");
  }
  
  public native void glGetRenderbufferParameterivOES(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetRenderbufferParameterivOES(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public String glGetString(int paramInt)
  {
    return _glGetString(paramInt);
  }
  
  public native void glGetTexEnviv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexEnviv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetTexEnvxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexEnvxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetTexGenfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glGetTexGenfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glGetTexGeniv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexGeniv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetTexGenxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexGenxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetTexParameterfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glGetTexParameterfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glGetTexParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glGetTexParameterxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glGetTexParameterxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glHint(int paramInt1, int paramInt2);
  
  public native boolean glIsBuffer(int paramInt);
  
  public native boolean glIsEnabled(int paramInt);
  
  public native boolean glIsFramebufferOES(int paramInt);
  
  public native boolean glIsRenderbufferOES(int paramInt);
  
  public native boolean glIsTexture(int paramInt);
  
  public native void glLightModelf(int paramInt, float paramFloat);
  
  public native void glLightModelfv(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glLightModelfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glLightModelx(int paramInt1, int paramInt2);
  
  public native void glLightModelxv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glLightModelxv(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glLightf(int paramInt1, int paramInt2, float paramFloat);
  
  public native void glLightfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glLightfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glLightx(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glLightxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glLightxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glLineWidth(float paramFloat);
  
  public native void glLineWidthx(int paramInt);
  
  public native void glLoadIdentity();
  
  public native void glLoadMatrixf(FloatBuffer paramFloatBuffer);
  
  public native void glLoadMatrixf(float[] paramArrayOfFloat, int paramInt);
  
  public native void glLoadMatrixx(IntBuffer paramIntBuffer);
  
  public native void glLoadMatrixx(int[] paramArrayOfInt, int paramInt);
  
  public native void glLoadPaletteFromModelViewMatrixOES();
  
  public native void glLogicOp(int paramInt);
  
  public native void glMaterialf(int paramInt1, int paramInt2, float paramFloat);
  
  public native void glMaterialfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glMaterialfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glMaterialx(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glMaterialxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glMaterialxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glMatrixIndexPointerOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public void glMatrixIndexPointerOES(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    glMatrixIndexPointerOESBounds(paramInt1, paramInt2, paramInt3, paramBuffer, paramBuffer.remaining());
    if (((paramInt1 == 2) || (paramInt1 == 3) || (paramInt1 == 4)) && ((paramInt2 == 5126) || (paramInt2 == 5120) || (paramInt2 == 5122) || (paramInt2 == 5132)) && (paramInt3 >= 0)) {
      _matrixIndexPointerOES = paramBuffer;
    }
  }
  
  public native void glMatrixMode(int paramInt);
  
  public native void glMultMatrixf(FloatBuffer paramFloatBuffer);
  
  public native void glMultMatrixf(float[] paramArrayOfFloat, int paramInt);
  
  public native void glMultMatrixx(IntBuffer paramIntBuffer);
  
  public native void glMultMatrixx(int[] paramArrayOfInt, int paramInt);
  
  public native void glMultiTexCoord4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public native void glMultiTexCoord4x(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public native void glNormal3f(float paramFloat1, float paramFloat2, float paramFloat3);
  
  public native void glNormal3x(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glNormalPointer(int paramInt1, int paramInt2, int paramInt3);
  
  public void glNormalPointer(int paramInt1, int paramInt2, Buffer paramBuffer)
  {
    glNormalPointerBounds(paramInt1, paramInt2, paramBuffer, paramBuffer.remaining());
    if (((paramInt1 == 5126) || (paramInt1 == 5120) || (paramInt1 == 5122) || (paramInt1 == 5132)) && (paramInt2 >= 0)) {
      _normalPointer = paramBuffer;
    }
  }
  
  public native void glOrthof(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
  
  public native void glOrthox(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public native void glPixelStorei(int paramInt1, int paramInt2);
  
  public native void glPointParameterf(int paramInt, float paramFloat);
  
  public native void glPointParameterfv(int paramInt, FloatBuffer paramFloatBuffer);
  
  public native void glPointParameterfv(int paramInt1, float[] paramArrayOfFloat, int paramInt2);
  
  public native void glPointParameterx(int paramInt1, int paramInt2);
  
  public native void glPointParameterxv(int paramInt, IntBuffer paramIntBuffer);
  
  public native void glPointParameterxv(int paramInt1, int[] paramArrayOfInt, int paramInt2);
  
  public native void glPointSize(float paramFloat);
  
  public void glPointSizePointerOES(int paramInt1, int paramInt2, Buffer paramBuffer)
  {
    glPointSizePointerOESBounds(paramInt1, paramInt2, paramBuffer, paramBuffer.remaining());
    if (((paramInt1 == 5126) || (paramInt1 == 5132)) && (paramInt2 >= 0)) {
      _pointSizePointerOES = paramBuffer;
    }
  }
  
  public native void glPointSizex(int paramInt);
  
  public native void glPolygonOffset(float paramFloat1, float paramFloat2);
  
  public native void glPolygonOffsetx(int paramInt1, int paramInt2);
  
  public native void glPopMatrix();
  
  public native void glPushMatrix();
  
  public native int glQueryMatrixxOES(IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  public native int glQueryMatrixxOES(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2);
  
  public native void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Buffer paramBuffer);
  
  public native void glRenderbufferStorageOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glRotatef(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public native void glRotatex(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glSampleCoverage(float paramFloat, boolean paramBoolean);
  
  public native void glSampleCoveragex(int paramInt, boolean paramBoolean);
  
  public native void glScalef(float paramFloat1, float paramFloat2, float paramFloat3);
  
  public native void glScalex(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glShadeModel(int paramInt);
  
  public native void glStencilFunc(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glStencilMask(int paramInt);
  
  public native void glStencilOp(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    glTexCoordPointerBounds(paramInt1, paramInt2, paramInt3, paramBuffer, paramBuffer.remaining());
    if (((paramInt1 == 2) || (paramInt1 == 3) || (paramInt1 == 4)) && ((paramInt2 == 5126) || (paramInt2 == 5120) || (paramInt2 == 5122) || (paramInt2 == 5132)) && (paramInt3 >= 0)) {
      _texCoordPointer = paramBuffer;
    }
  }
  
  public native void glTexEnvf(int paramInt1, int paramInt2, float paramFloat);
  
  public native void glTexEnvfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glTexEnvfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glTexEnvi(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexEnviv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexEnviv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexEnvx(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexEnvxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexEnvxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexGenf(int paramInt1, int paramInt2, float paramFloat);
  
  public native void glTexGenfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glTexGenfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glTexGeni(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexGeniv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexGeniv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexGenx(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexGenxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexGenxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer);
  
  public native void glTexParameterf(int paramInt1, int paramInt2, float paramFloat);
  
  public native void glTexParameterfv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer);
  
  public native void glTexParameterfv(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3);
  
  public native void glTexParameteri(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexParameteriv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexParameteriv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexParameterx(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glTexParameterxv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer);
  
  public native void glTexParameterxv(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
  
  public native void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Buffer paramBuffer);
  
  public native void glTranslatef(float paramFloat1, float paramFloat2, float paramFloat3);
  
  public native void glTranslatex(int paramInt1, int paramInt2, int paramInt3);
  
  public native void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    glVertexPointerBounds(paramInt1, paramInt2, paramInt3, paramBuffer, paramBuffer.remaining());
    if (((paramInt1 == 2) || (paramInt1 == 3) || (paramInt1 == 4)) && ((paramInt2 == 5126) || (paramInt2 == 5120) || (paramInt2 == 5122) || (paramInt2 == 5132)) && (paramInt3 >= 0)) {
      _vertexPointer = paramBuffer;
    }
  }
  
  public native void glViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public native void glWeightPointerOES(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public void glWeightPointerOES(int paramInt1, int paramInt2, int paramInt3, Buffer paramBuffer)
  {
    glWeightPointerOESBounds(paramInt1, paramInt2, paramInt3, paramBuffer, paramBuffer.remaining());
  }
}
