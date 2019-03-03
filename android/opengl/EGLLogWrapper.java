package android.opengl;

import java.io.IOException;
import java.io.Writer;
import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

class EGLLogWrapper
  implements EGL11
{
  private int mArgCount;
  boolean mCheckError;
  private EGL10 mEgl10;
  Writer mLog;
  boolean mLogArgumentNames;
  
  public EGLLogWrapper(EGL paramEGL, int paramInt, Writer paramWriter)
  {
    mEgl10 = ((EGL10)paramEGL);
    mLog = paramWriter;
    boolean bool1 = false;
    if ((0x4 & paramInt) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mLogArgumentNames = bool2;
    boolean bool2 = bool1;
    if ((0x1 & paramInt) != 0) {
      bool2 = true;
    }
    mCheckError = bool2;
  }
  
  private void arg(String paramString, int paramInt)
  {
    arg(paramString, Integer.toString(paramInt));
  }
  
  private void arg(String paramString, Object paramObject)
  {
    arg(paramString, toString(paramObject));
  }
  
  private void arg(String paramString1, String paramString2)
  {
    int i = mArgCount;
    mArgCount = (i + 1);
    if (i > 0) {
      log(", ");
    }
    if (mLogArgumentNames)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append("=");
      log(localStringBuilder.toString());
    }
    log(paramString2);
  }
  
  private void arg(String paramString, EGLContext paramEGLContext)
  {
    if (paramEGLContext == EGL10.EGL_NO_CONTEXT) {
      arg(paramString, "EGL10.EGL_NO_CONTEXT");
    } else {
      arg(paramString, toString(paramEGLContext));
    }
  }
  
  private void arg(String paramString, EGLDisplay paramEGLDisplay)
  {
    if (paramEGLDisplay == EGL10.EGL_DEFAULT_DISPLAY) {
      arg(paramString, "EGL10.EGL_DEFAULT_DISPLAY");
    } else if (paramEGLDisplay == EGL_NO_DISPLAY) {
      arg(paramString, "EGL10.EGL_NO_DISPLAY");
    } else {
      arg(paramString, toString(paramEGLDisplay));
    }
  }
  
  private void arg(String paramString, EGLSurface paramEGLSurface)
  {
    if (paramEGLSurface == EGL10.EGL_NO_SURFACE) {
      arg(paramString, "EGL10.EGL_NO_SURFACE");
    } else {
      arg(paramString, toString(paramEGLSurface));
    }
  }
  
  private void arg(String paramString, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      arg(paramString, "null");
    } else {
      arg(paramString, toString(paramArrayOfInt.length, paramArrayOfInt, 0));
    }
  }
  
  private void arg(String paramString, Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject == null) {
      arg(paramString, "null");
    } else {
      arg(paramString, toString(paramArrayOfObject.length, paramArrayOfObject, 0));
    }
  }
  
  private void begin(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append('(');
    log(localStringBuilder.toString());
    mArgCount = 0;
  }
  
  private void checkError()
  {
    int i = mEgl10.eglGetError();
    if (i != 12288)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("eglError: ");
      ((StringBuilder)localObject).append(getErrorString(i));
      localObject = ((StringBuilder)localObject).toString();
      logLine((String)localObject);
      if (mCheckError) {
        throw new GLException(i, (String)localObject);
      }
    }
  }
  
  private void end()
  {
    log(");\n");
    flush();
  }
  
  private void flush()
  {
    try
    {
      mLog.flush();
    }
    catch (IOException localIOException)
    {
      mLog = null;
    }
  }
  
  public static String getErrorString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return getHex(paramInt);
    case 12302: 
      return "EGL_CONTEXT_LOST";
    case 12301: 
      return "EGL_BAD_SURFACE";
    case 12300: 
      return "EGL_BAD_PARAMETER";
    case 12299: 
      return "EGL_BAD_NATIVE_WINDOW";
    case 12298: 
      return "EGL_BAD_NATIVE_PIXMAP";
    case 12297: 
      return "EGL_BAD_MATCH";
    case 12296: 
      return "EGL_BAD_DISPLAY";
    case 12295: 
      return "EGL_BAD_CURRENT_SURFACE";
    case 12294: 
      return "EGL_BAD_CONTEXT";
    case 12293: 
      return "EGL_BAD_CONFIG";
    case 12292: 
      return "EGL_BAD_ATTRIBUTE";
    case 12291: 
      return "EGL_BAD_ALLOC";
    case 12290: 
      return "EGL_BAD_ACCESS";
    case 12289: 
      return "EGL_NOT_INITIALIZED";
    }
    return "EGL_SUCCESS";
  }
  
  private static String getHex(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
  
  private void log(String paramString)
  {
    try
    {
      mLog.write(paramString);
    }
    catch (IOException paramString) {}
  }
  
  private void logLine(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append('\n');
    log(localStringBuilder.toString());
  }
  
  private void returns(int paramInt)
  {
    returns(Integer.toString(paramInt));
  }
  
  private void returns(Object paramObject)
  {
    returns(toString(paramObject));
  }
  
  private void returns(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" returns ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(";\n");
    log(localStringBuilder.toString());
    flush();
  }
  
  private void returns(boolean paramBoolean)
  {
    returns(Boolean.toString(paramBoolean));
  }
  
  private String toString(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("{\n");
    int i = paramArrayOfInt.length;
    for (int j = 0; j < paramInt1; j++)
    {
      int k = paramInt2 + j;
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" [");
      localStringBuilder2.append(k);
      localStringBuilder2.append("] = ");
      localStringBuilder1.append(localStringBuilder2.toString());
      if ((k >= 0) && (k < i)) {
        localStringBuilder1.append(paramArrayOfInt[k]);
      } else {
        localStringBuilder1.append("out of bounds");
      }
      localStringBuilder1.append('\n');
    }
    localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }
  
  private String toString(int paramInt1, Object[] paramArrayOfObject, int paramInt2)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("{\n");
    int i = paramArrayOfObject.length;
    for (int j = 0; j < paramInt1; j++)
    {
      int k = paramInt2 + j;
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" [");
      localStringBuilder2.append(k);
      localStringBuilder2.append("] = ");
      localStringBuilder1.append(localStringBuilder2.toString());
      if ((k >= 0) && (k < i)) {
        localStringBuilder1.append(paramArrayOfObject[k]);
      } else {
        localStringBuilder1.append("out of bounds");
      }
      localStringBuilder1.append('\n');
    }
    localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }
  
  private String toString(Object paramObject)
  {
    if (paramObject == null) {
      return "null";
    }
    return paramObject.toString();
  }
  
  public boolean eglChooseConfig(EGLDisplay paramEGLDisplay, int[] paramArrayOfInt1, EGLConfig[] paramArrayOfEGLConfig, int paramInt, int[] paramArrayOfInt2)
  {
    begin("eglChooseConfig");
    arg("display", paramEGLDisplay);
    arg("attrib_list", paramArrayOfInt1);
    arg("config_size", paramInt);
    end();
    boolean bool = mEgl10.eglChooseConfig(paramEGLDisplay, paramArrayOfInt1, paramArrayOfEGLConfig, paramInt, paramArrayOfInt2);
    arg("configs", paramArrayOfEGLConfig);
    arg("num_config", paramArrayOfInt2);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglCopyBuffers(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface, Object paramObject)
  {
    begin("eglCopyBuffers");
    arg("display", paramEGLDisplay);
    arg("surface", paramEGLSurface);
    arg("native_pixmap", paramObject);
    end();
    boolean bool = mEgl10.eglCopyBuffers(paramEGLDisplay, paramEGLSurface, paramObject);
    returns(bool);
    checkError();
    return bool;
  }
  
  public EGLContext eglCreateContext(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, EGLContext paramEGLContext, int[] paramArrayOfInt)
  {
    begin("eglCreateContext");
    arg("display", paramEGLDisplay);
    arg("config", paramEGLConfig);
    arg("share_context", paramEGLContext);
    arg("attrib_list", paramArrayOfInt);
    end();
    paramEGLDisplay = mEgl10.eglCreateContext(paramEGLDisplay, paramEGLConfig, paramEGLContext, paramArrayOfInt);
    returns(paramEGLDisplay);
    checkError();
    return paramEGLDisplay;
  }
  
  public EGLSurface eglCreatePbufferSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int[] paramArrayOfInt)
  {
    begin("eglCreatePbufferSurface");
    arg("display", paramEGLDisplay);
    arg("config", paramEGLConfig);
    arg("attrib_list", paramArrayOfInt);
    end();
    paramEGLDisplay = mEgl10.eglCreatePbufferSurface(paramEGLDisplay, paramEGLConfig, paramArrayOfInt);
    returns(paramEGLDisplay);
    checkError();
    return paramEGLDisplay;
  }
  
  public EGLSurface eglCreatePixmapSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt)
  {
    begin("eglCreatePixmapSurface");
    arg("display", paramEGLDisplay);
    arg("config", paramEGLConfig);
    arg("native_pixmap", paramObject);
    arg("attrib_list", paramArrayOfInt);
    end();
    paramEGLDisplay = mEgl10.eglCreatePixmapSurface(paramEGLDisplay, paramEGLConfig, paramObject, paramArrayOfInt);
    returns(paramEGLDisplay);
    checkError();
    return paramEGLDisplay;
  }
  
  public EGLSurface eglCreateWindowSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt)
  {
    begin("eglCreateWindowSurface");
    arg("display", paramEGLDisplay);
    arg("config", paramEGLConfig);
    arg("native_window", paramObject);
    arg("attrib_list", paramArrayOfInt);
    end();
    paramEGLDisplay = mEgl10.eglCreateWindowSurface(paramEGLDisplay, paramEGLConfig, paramObject, paramArrayOfInt);
    returns(paramEGLDisplay);
    checkError();
    return paramEGLDisplay;
  }
  
  public boolean eglDestroyContext(EGLDisplay paramEGLDisplay, EGLContext paramEGLContext)
  {
    begin("eglDestroyContext");
    arg("display", paramEGLDisplay);
    arg("context", paramEGLContext);
    end();
    boolean bool = mEgl10.eglDestroyContext(paramEGLDisplay, paramEGLContext);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglDestroySurface(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface)
  {
    begin("eglDestroySurface");
    arg("display", paramEGLDisplay);
    arg("surface", paramEGLSurface);
    end();
    boolean bool = mEgl10.eglDestroySurface(paramEGLDisplay, paramEGLSurface);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglGetConfigAttrib(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int paramInt, int[] paramArrayOfInt)
  {
    begin("eglGetConfigAttrib");
    arg("display", paramEGLDisplay);
    arg("config", paramEGLConfig);
    arg("attribute", paramInt);
    end();
    boolean bool = mEgl10.eglGetConfigAttrib(paramEGLDisplay, paramEGLConfig, paramInt, paramArrayOfInt);
    arg("value", paramArrayOfInt);
    returns(bool);
    checkError();
    return false;
  }
  
  public boolean eglGetConfigs(EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig, int paramInt, int[] paramArrayOfInt)
  {
    begin("eglGetConfigs");
    arg("display", paramEGLDisplay);
    arg("config_size", paramInt);
    end();
    boolean bool = mEgl10.eglGetConfigs(paramEGLDisplay, paramArrayOfEGLConfig, paramInt, paramArrayOfInt);
    arg("configs", paramArrayOfEGLConfig);
    arg("num_config", paramArrayOfInt);
    returns(bool);
    checkError();
    return bool;
  }
  
  public EGLContext eglGetCurrentContext()
  {
    begin("eglGetCurrentContext");
    end();
    EGLContext localEGLContext = mEgl10.eglGetCurrentContext();
    returns(localEGLContext);
    checkError();
    return localEGLContext;
  }
  
  public EGLDisplay eglGetCurrentDisplay()
  {
    begin("eglGetCurrentDisplay");
    end();
    EGLDisplay localEGLDisplay = mEgl10.eglGetCurrentDisplay();
    returns(localEGLDisplay);
    checkError();
    return localEGLDisplay;
  }
  
  public EGLSurface eglGetCurrentSurface(int paramInt)
  {
    begin("eglGetCurrentSurface");
    arg("readdraw", paramInt);
    end();
    EGLSurface localEGLSurface = mEgl10.eglGetCurrentSurface(paramInt);
    returns(localEGLSurface);
    checkError();
    return localEGLSurface;
  }
  
  public EGLDisplay eglGetDisplay(Object paramObject)
  {
    begin("eglGetDisplay");
    arg("native_display", paramObject);
    end();
    paramObject = mEgl10.eglGetDisplay(paramObject);
    returns(paramObject);
    checkError();
    return paramObject;
  }
  
  public int eglGetError()
  {
    begin("eglGetError");
    end();
    int i = mEgl10.eglGetError();
    returns(getErrorString(i));
    return i;
  }
  
  public boolean eglInitialize(EGLDisplay paramEGLDisplay, int[] paramArrayOfInt)
  {
    begin("eglInitialize");
    arg("display", paramEGLDisplay);
    end();
    boolean bool = mEgl10.eglInitialize(paramEGLDisplay, paramArrayOfInt);
    returns(bool);
    arg("major_minor", paramArrayOfInt);
    checkError();
    return bool;
  }
  
  public boolean eglMakeCurrent(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface1, EGLSurface paramEGLSurface2, EGLContext paramEGLContext)
  {
    begin("eglMakeCurrent");
    arg("display", paramEGLDisplay);
    arg("draw", paramEGLSurface1);
    arg("read", paramEGLSurface2);
    arg("context", paramEGLContext);
    end();
    boolean bool = mEgl10.eglMakeCurrent(paramEGLDisplay, paramEGLSurface1, paramEGLSurface2, paramEGLContext);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglQueryContext(EGLDisplay paramEGLDisplay, EGLContext paramEGLContext, int paramInt, int[] paramArrayOfInt)
  {
    begin("eglQueryContext");
    arg("display", paramEGLDisplay);
    arg("context", paramEGLContext);
    arg("attribute", paramInt);
    end();
    boolean bool = mEgl10.eglQueryContext(paramEGLDisplay, paramEGLContext, paramInt, paramArrayOfInt);
    returns(paramArrayOfInt[0]);
    returns(bool);
    checkError();
    return bool;
  }
  
  public String eglQueryString(EGLDisplay paramEGLDisplay, int paramInt)
  {
    begin("eglQueryString");
    arg("display", paramEGLDisplay);
    arg("name", paramInt);
    end();
    paramEGLDisplay = mEgl10.eglQueryString(paramEGLDisplay, paramInt);
    returns(paramEGLDisplay);
    checkError();
    return paramEGLDisplay;
  }
  
  public boolean eglQuerySurface(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface, int paramInt, int[] paramArrayOfInt)
  {
    begin("eglQuerySurface");
    arg("display", paramEGLDisplay);
    arg("surface", paramEGLSurface);
    arg("attribute", paramInt);
    end();
    boolean bool = mEgl10.eglQuerySurface(paramEGLDisplay, paramEGLSurface, paramInt, paramArrayOfInt);
    returns(paramArrayOfInt[0]);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglReleaseThread()
  {
    begin("eglReleaseThread");
    end();
    boolean bool = mEgl10.eglReleaseThread();
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglSwapBuffers(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface)
  {
    begin("eglSwapBuffers");
    arg("display", paramEGLDisplay);
    arg("surface", paramEGLSurface);
    end();
    boolean bool = mEgl10.eglSwapBuffers(paramEGLDisplay, paramEGLSurface);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglTerminate(EGLDisplay paramEGLDisplay)
  {
    begin("eglTerminate");
    arg("display", paramEGLDisplay);
    end();
    boolean bool = mEgl10.eglTerminate(paramEGLDisplay);
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglWaitGL()
  {
    begin("eglWaitGL");
    end();
    boolean bool = mEgl10.eglWaitGL();
    returns(bool);
    checkError();
    return bool;
  }
  
  public boolean eglWaitNative(int paramInt, Object paramObject)
  {
    begin("eglWaitNative");
    arg("engine", paramInt);
    arg("bindTarget", paramObject);
    end();
    boolean bool = mEgl10.eglWaitNative(paramInt, paramObject);
    returns(bool);
    checkError();
    return bool;
  }
}
