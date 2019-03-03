package android.opengl;

import java.io.Writer;
import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.opengles.GL;

public class GLDebugHelper
{
  public static final int CONFIG_CHECK_GL_ERROR = 1;
  public static final int CONFIG_CHECK_THREAD = 2;
  public static final int CONFIG_LOG_ARGUMENT_NAMES = 4;
  public static final int ERROR_WRONG_THREAD = 28672;
  
  public GLDebugHelper() {}
  
  public static EGL wrap(EGL paramEGL, int paramInt, Writer paramWriter)
  {
    Object localObject = paramEGL;
    if (paramWriter != null) {
      localObject = new EGLLogWrapper(paramEGL, paramInt, paramWriter);
    }
    return localObject;
  }
  
  public static GL wrap(GL paramGL, int paramInt, Writer paramWriter)
  {
    Object localObject = paramGL;
    if (paramInt != 0) {
      localObject = new GLErrorWrapper(paramGL, paramInt);
    }
    paramGL = (GL)localObject;
    if (paramWriter != null)
    {
      boolean bool;
      if ((0x4 & paramInt) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramGL = new GLLogWrapper((GL)localObject, paramWriter, bool);
    }
    return paramGL;
  }
}
