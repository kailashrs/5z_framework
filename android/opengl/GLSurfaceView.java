package android.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceView;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceView
  extends SurfaceView
  implements SurfaceHolder.Callback2
{
  public static final int DEBUG_CHECK_GL_ERROR = 1;
  public static final int DEBUG_LOG_GL_CALLS = 2;
  private static final boolean LOG_ATTACH_DETACH = false;
  private static final boolean LOG_EGL = false;
  private static final boolean LOG_PAUSE_RESUME = false;
  private static final boolean LOG_RENDERER = false;
  private static final boolean LOG_RENDERER_DRAW_FRAME = false;
  private static final boolean LOG_SURFACE = false;
  private static final boolean LOG_THREADS = false;
  public static final int RENDERMODE_CONTINUOUSLY = 1;
  public static final int RENDERMODE_WHEN_DIRTY = 0;
  private static final String TAG = "GLSurfaceView";
  private static final GLThreadManager sGLThreadManager = new GLThreadManager(null);
  private int mDebugFlags;
  private boolean mDetached;
  private EGLConfigChooser mEGLConfigChooser;
  private int mEGLContextClientVersion;
  private EGLContextFactory mEGLContextFactory;
  private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
  private GLThread mGLThread;
  private GLWrapper mGLWrapper;
  private boolean mPreserveEGLContextOnPause;
  private Renderer mRenderer;
  private final WeakReference<GLSurfaceView> mThisWeakRef = new WeakReference(this);
  
  public GLSurfaceView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public GLSurfaceView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void checkRenderThreadState()
  {
    if (mGLThread == null) {
      return;
    }
    throw new IllegalStateException("setRenderer has already been called for this instance.");
  }
  
  private void init()
  {
    getHolder().addCallback(this);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGLThread != null) {
        mGLThread.requestExitAndWait();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getDebugFlags()
  {
    return mDebugFlags;
  }
  
  public boolean getPreserveEGLContextOnPause()
  {
    return mPreserveEGLContextOnPause;
  }
  
  public int getRenderMode()
  {
    return mGLThread.getRenderMode();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if ((mDetached) && (mRenderer != null))
    {
      int i = 1;
      if (mGLThread != null) {
        i = mGLThread.getRenderMode();
      }
      mGLThread = new GLThread(mThisWeakRef);
      if (i != 1) {
        mGLThread.setRenderMode(i);
      }
      mGLThread.start();
    }
    mDetached = false;
  }
  
  protected void onDetachedFromWindow()
  {
    if (mGLThread != null) {
      mGLThread.requestExitAndWait();
    }
    mDetached = true;
    super.onDetachedFromWindow();
  }
  
  public void onPause()
  {
    mGLThread.onPause();
  }
  
  public void onResume()
  {
    mGLThread.onResume();
  }
  
  public void queueEvent(Runnable paramRunnable)
  {
    mGLThread.queueEvent(paramRunnable);
  }
  
  public void requestRender()
  {
    mGLThread.requestRender();
  }
  
  public void setDebugFlags(int paramInt)
  {
    mDebugFlags = paramInt;
  }
  
  public void setEGLConfigChooser(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    setEGLConfigChooser(new ComponentSizeChooser(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
  }
  
  public void setEGLConfigChooser(EGLConfigChooser paramEGLConfigChooser)
  {
    checkRenderThreadState();
    mEGLConfigChooser = paramEGLConfigChooser;
  }
  
  public void setEGLConfigChooser(boolean paramBoolean)
  {
    setEGLConfigChooser(new SimpleEGLConfigChooser(paramBoolean));
  }
  
  public void setEGLContextClientVersion(int paramInt)
  {
    checkRenderThreadState();
    mEGLContextClientVersion = paramInt;
  }
  
  public void setEGLContextFactory(EGLContextFactory paramEGLContextFactory)
  {
    checkRenderThreadState();
    mEGLContextFactory = paramEGLContextFactory;
  }
  
  public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory paramEGLWindowSurfaceFactory)
  {
    checkRenderThreadState();
    mEGLWindowSurfaceFactory = paramEGLWindowSurfaceFactory;
  }
  
  public void setGLWrapper(GLWrapper paramGLWrapper)
  {
    mGLWrapper = paramGLWrapper;
  }
  
  public void setPreserveEGLContextOnPause(boolean paramBoolean)
  {
    mPreserveEGLContextOnPause = paramBoolean;
  }
  
  public void setRenderMode(int paramInt)
  {
    mGLThread.setRenderMode(paramInt);
  }
  
  public void setRenderer(Renderer paramRenderer)
  {
    checkRenderThreadState();
    if (mEGLConfigChooser == null) {
      mEGLConfigChooser = new SimpleEGLConfigChooser(true);
    }
    if (mEGLContextFactory == null) {
      mEGLContextFactory = new DefaultContextFactory(null);
    }
    if (mEGLWindowSurfaceFactory == null) {
      mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory(null);
    }
    mRenderer = paramRenderer;
    mGLThread = new GLThread(mThisWeakRef);
    mGLThread.start();
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    mGLThread.onWindowResize(paramInt2, paramInt3);
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    mGLThread.surfaceCreated();
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    mGLThread.surfaceDestroyed();
  }
  
  @Deprecated
  public void surfaceRedrawNeeded(SurfaceHolder paramSurfaceHolder) {}
  
  public void surfaceRedrawNeededAsync(SurfaceHolder paramSurfaceHolder, Runnable paramRunnable)
  {
    if (mGLThread != null) {
      mGLThread.requestRenderAndNotify(paramRunnable);
    }
  }
  
  private abstract class BaseConfigChooser
    implements GLSurfaceView.EGLConfigChooser
  {
    protected int[] mConfigSpec = filterConfigSpec(paramArrayOfInt);
    
    public BaseConfigChooser(int[] paramArrayOfInt) {}
    
    private int[] filterConfigSpec(int[] paramArrayOfInt)
    {
      if ((mEGLContextClientVersion != 2) && (mEGLContextClientVersion != 3)) {
        return paramArrayOfInt;
      }
      int i = paramArrayOfInt.length;
      int[] arrayOfInt = new int[i + 2];
      System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i - 1);
      arrayOfInt[(i - 1)] = 12352;
      if (mEGLContextClientVersion == 2) {
        arrayOfInt[i] = 4;
      } else {
        arrayOfInt[i] = 64;
      }
      arrayOfInt[(i + 1)] = 12344;
      return arrayOfInt;
    }
    
    public EGLConfig chooseConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay)
    {
      int[] arrayOfInt = new int[1];
      if (paramEGL10.eglChooseConfig(paramEGLDisplay, mConfigSpec, null, 0, arrayOfInt))
      {
        int i = arrayOfInt[0];
        if (i > 0)
        {
          EGLConfig[] arrayOfEGLConfig = new EGLConfig[i];
          if (paramEGL10.eglChooseConfig(paramEGLDisplay, mConfigSpec, arrayOfEGLConfig, i, arrayOfInt))
          {
            paramEGL10 = chooseConfig(paramEGL10, paramEGLDisplay, arrayOfEGLConfig);
            if (paramEGL10 != null) {
              return paramEGL10;
            }
            throw new IllegalArgumentException("No config chosen");
          }
          throw new IllegalArgumentException("eglChooseConfig#2 failed");
        }
        throw new IllegalArgumentException("No configs match configSpec");
      }
      throw new IllegalArgumentException("eglChooseConfig failed");
    }
    
    abstract EGLConfig chooseConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig);
  }
  
  private class ComponentSizeChooser
    extends GLSurfaceView.BaseConfigChooser
  {
    protected int mAlphaSize;
    protected int mBlueSize;
    protected int mDepthSize;
    protected int mGreenSize;
    protected int mRedSize;
    protected int mStencilSize;
    private int[] mValue = new int[1];
    
    public ComponentSizeChooser(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      super(new int[] { 12324, paramInt1, 12323, paramInt2, 12322, paramInt3, 12321, paramInt4, 12325, paramInt5, 12326, paramInt6, 12344 });
      mRedSize = paramInt1;
      mGreenSize = paramInt2;
      mBlueSize = paramInt3;
      mAlphaSize = paramInt4;
      mDepthSize = paramInt5;
      mStencilSize = paramInt6;
    }
    
    private int findConfigAttrib(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int paramInt1, int paramInt2)
    {
      if (paramEGL10.eglGetConfigAttrib(paramEGLDisplay, paramEGLConfig, paramInt1, mValue)) {
        return mValue[0];
      }
      return paramInt2;
    }
    
    public EGLConfig chooseConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig)
    {
      int i = paramArrayOfEGLConfig.length;
      for (int j = 0; j < i; j++)
      {
        EGLConfig localEGLConfig = paramArrayOfEGLConfig[j];
        int k = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12325, 0);
        int m = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12326, 0);
        if ((k >= mDepthSize) && (m >= mStencilSize))
        {
          int n = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12324, 0);
          int i1 = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12323, 0);
          k = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12322, 0);
          m = findConfigAttrib(paramEGL10, paramEGLDisplay, localEGLConfig, 12321, 0);
          if ((n == mRedSize) && (i1 == mGreenSize) && (k == mBlueSize) && (m == mAlphaSize)) {
            return localEGLConfig;
          }
        }
      }
      return null;
    }
  }
  
  private class DefaultContextFactory
    implements GLSurfaceView.EGLContextFactory
  {
    private int EGL_CONTEXT_CLIENT_VERSION = 12440;
    
    private DefaultContextFactory() {}
    
    public EGLContext createContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig)
    {
      int i = EGL_CONTEXT_CLIENT_VERSION;
      int j = mEGLContextClientVersion;
      EGLContext localEGLContext = EGL10.EGL_NO_CONTEXT;
      int[] arrayOfInt;
      if (mEGLContextClientVersion != 0) {
        arrayOfInt = new int[] { i, j, 12344 };
      } else {
        arrayOfInt = null;
      }
      return paramEGL10.eglCreateContext(paramEGLDisplay, paramEGLConfig, localEGLContext, arrayOfInt);
    }
    
    public void destroyContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLContext paramEGLContext)
    {
      if (!paramEGL10.eglDestroyContext(paramEGLDisplay, paramEGLContext))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("display:");
        localStringBuilder.append(paramEGLDisplay);
        localStringBuilder.append(" context: ");
        localStringBuilder.append(paramEGLContext);
        Log.e("DefaultContextFactory", localStringBuilder.toString());
        GLSurfaceView.EglHelper.throwEglException("eglDestroyContex", paramEGL10.eglGetError());
      }
    }
  }
  
  private static class DefaultWindowSurfaceFactory
    implements GLSurfaceView.EGLWindowSurfaceFactory
  {
    private DefaultWindowSurfaceFactory() {}
    
    public EGLSurface createWindowSurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject)
    {
      Object localObject = null;
      try
      {
        paramEGL10 = paramEGL10.eglCreateWindowSurface(paramEGLDisplay, paramEGLConfig, paramObject, null);
      }
      catch (IllegalArgumentException paramEGL10)
      {
        Log.e("GLSurfaceView", "eglCreateWindowSurface", paramEGL10);
        paramEGL10 = localObject;
      }
      return paramEGL10;
    }
    
    public void destroySurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface)
    {
      paramEGL10.eglDestroySurface(paramEGLDisplay, paramEGLSurface);
    }
  }
  
  public static abstract interface EGLConfigChooser
  {
    public abstract EGLConfig chooseConfig(EGL10 paramEGL10, EGLDisplay paramEGLDisplay);
  }
  
  public static abstract interface EGLContextFactory
  {
    public abstract EGLContext createContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig);
    
    public abstract void destroyContext(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLContext paramEGLContext);
  }
  
  public static abstract interface EGLWindowSurfaceFactory
  {
    public abstract EGLSurface createWindowSurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject);
    
    public abstract void destroySurface(EGL10 paramEGL10, EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface);
  }
  
  private static class EglHelper
  {
    EGL10 mEgl;
    EGLConfig mEglConfig;
    EGLContext mEglContext;
    EGLDisplay mEglDisplay;
    EGLSurface mEglSurface;
    private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;
    
    public EglHelper(WeakReference<GLSurfaceView> paramWeakReference)
    {
      mGLSurfaceViewWeakRef = paramWeakReference;
    }
    
    private void destroySurfaceImp()
    {
      if ((mEglSurface != null) && (mEglSurface != EGL10.EGL_NO_SURFACE))
      {
        mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        GLSurfaceView localGLSurfaceView = (GLSurfaceView)mGLSurfaceViewWeakRef.get();
        if (localGLSurfaceView != null) {
          mEGLWindowSurfaceFactory.destroySurface(mEgl, mEglDisplay, mEglSurface);
        }
        mEglSurface = null;
      }
    }
    
    public static String formatEglError(String paramString, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" failed: ");
      localStringBuilder.append(EGLLogWrapper.getErrorString(paramInt));
      return localStringBuilder.toString();
    }
    
    public static void logEglErrorAsWarning(String paramString1, String paramString2, int paramInt)
    {
      Log.w(paramString1, formatEglError(paramString2, paramInt));
    }
    
    private void throwEglException(String paramString)
    {
      throwEglException(paramString, mEgl.eglGetError());
    }
    
    public static void throwEglException(String paramString, int paramInt)
    {
      throw new RuntimeException(formatEglError(paramString, paramInt));
    }
    
    GL createGL()
    {
      GL localGL1 = mEglContext.getGL();
      GLSurfaceView localGLSurfaceView = (GLSurfaceView)mGLSurfaceViewWeakRef.get();
      Object localObject = localGL1;
      if (localGLSurfaceView != null)
      {
        GL localGL2 = localGL1;
        if (mGLWrapper != null) {
          localGL2 = mGLWrapper.wrap(localGL1);
        }
        localObject = localGL2;
        if ((mDebugFlags & 0x3) != 0)
        {
          int i = 0;
          localObject = null;
          if ((mDebugFlags & 0x1) != 0) {
            i = 0x0 | 0x1;
          }
          if ((mDebugFlags & 0x2) != 0) {
            localObject = new GLSurfaceView.LogWriter();
          }
          localObject = GLDebugHelper.wrap(localGL2, i, (Writer)localObject);
        }
      }
      return localObject;
    }
    
    public boolean createSurface()
    {
      if (mEgl != null)
      {
        if (mEglDisplay != null)
        {
          if (mEglConfig != null)
          {
            destroySurfaceImp();
            GLSurfaceView localGLSurfaceView = (GLSurfaceView)mGLSurfaceViewWeakRef.get();
            if (localGLSurfaceView != null) {
              mEglSurface = mEGLWindowSurfaceFactory.createWindowSurface(mEgl, mEglDisplay, mEglConfig, localGLSurfaceView.getHolder());
            } else {
              mEglSurface = null;
            }
            if ((mEglSurface != null) && (mEglSurface != EGL10.EGL_NO_SURFACE))
            {
              if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext))
              {
                logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", mEgl.eglGetError());
                return false;
              }
              return true;
            }
            if (mEgl.eglGetError() == 12299) {
              Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
          }
          throw new RuntimeException("mEglConfig not initialized");
        }
        throw new RuntimeException("eglDisplay not initialized");
      }
      throw new RuntimeException("egl not initialized");
    }
    
    public void destroySurface()
    {
      destroySurfaceImp();
    }
    
    public void finish()
    {
      if (mEglContext != null)
      {
        GLSurfaceView localGLSurfaceView = (GLSurfaceView)mGLSurfaceViewWeakRef.get();
        if (localGLSurfaceView != null) {
          mEGLContextFactory.destroyContext(mEgl, mEglDisplay, mEglContext);
        }
        mEglContext = null;
      }
      if (mEglDisplay != null)
      {
        mEgl.eglTerminate(mEglDisplay);
        mEglDisplay = null;
      }
    }
    
    public void start()
    {
      mEgl = ((EGL10)EGLContext.getEGL());
      mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      if (mEglDisplay != EGL10.EGL_NO_DISPLAY)
      {
        Object localObject = new int[2];
        if (mEgl.eglInitialize(mEglDisplay, (int[])localObject))
        {
          localObject = (GLSurfaceView)mGLSurfaceViewWeakRef.get();
          if (localObject == null)
          {
            mEglConfig = null;
            mEglContext = null;
          }
          else
          {
            mEglConfig = mEGLConfigChooser.chooseConfig(mEgl, mEglDisplay);
            mEglContext = mEGLContextFactory.createContext(mEgl, mEglDisplay, mEglConfig);
          }
          if ((mEglContext == null) || (mEglContext == EGL10.EGL_NO_CONTEXT))
          {
            mEglContext = null;
            throwEglException("createContext");
          }
          mEglSurface = null;
          return;
        }
        throw new RuntimeException("eglInitialize failed");
      }
      throw new RuntimeException("eglGetDisplay failed");
    }
    
    public int swap()
    {
      if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
        return mEgl.eglGetError();
      }
      return 12288;
    }
  }
  
  static class GLThread
    extends Thread
  {
    private GLSurfaceView.EglHelper mEglHelper;
    private ArrayList<Runnable> mEventQueue = new ArrayList();
    private boolean mExited;
    private Runnable mFinishDrawingRunnable = null;
    private boolean mFinishedCreatingEglSurface;
    private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;
    private boolean mHasSurface;
    private boolean mHaveEglContext;
    private boolean mHaveEglSurface;
    private int mHeight = 0;
    private boolean mPaused;
    private boolean mRenderComplete;
    private int mRenderMode = 1;
    private boolean mRequestPaused;
    private boolean mRequestRender = true;
    private boolean mShouldExit;
    private boolean mShouldReleaseEglContext;
    private boolean mSizeChanged = true;
    private boolean mSurfaceIsBad;
    private boolean mWaitingForSurface;
    private boolean mWantRenderNotification = false;
    private int mWidth = 0;
    
    GLThread(WeakReference<GLSurfaceView> paramWeakReference)
    {
      mGLSurfaceViewWeakRef = paramWeakReference;
    }
    
    /* Error */
    private void guardedRun()
      throws InterruptedException
    {
      // Byte code:
      //   0: aload_0
      //   1: new 77	android/opengl/GLSurfaceView$EglHelper
      //   4: dup
      //   5: aload_0
      //   6: getfield 63	android/opengl/GLSurfaceView$GLThread:mGLSurfaceViewWeakRef	Ljava/lang/ref/WeakReference;
      //   9: invokespecial 79	android/opengl/GLSurfaceView$EglHelper:<init>	(Ljava/lang/ref/WeakReference;)V
      //   12: putfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   15: aload_0
      //   16: iconst_0
      //   17: putfield 83	android/opengl/GLSurfaceView$GLThread:mHaveEglContext	Z
      //   20: aload_0
      //   21: iconst_0
      //   22: putfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   25: aload_0
      //   26: iconst_0
      //   27: putfield 61	android/opengl/GLSurfaceView$GLThread:mWantRenderNotification	Z
      //   30: iconst_0
      //   31: istore_1
      //   32: iconst_0
      //   33: istore_2
      //   34: iconst_0
      //   35: istore_3
      //   36: iconst_0
      //   37: istore 4
      //   39: iconst_0
      //   40: istore 5
      //   42: iconst_0
      //   43: istore 6
      //   45: iconst_0
      //   46: istore 7
      //   48: iconst_0
      //   49: istore 8
      //   51: aconst_null
      //   52: astore 9
      //   54: iconst_0
      //   55: istore 10
      //   57: iconst_0
      //   58: istore 11
      //   60: aconst_null
      //   61: astore 12
      //   63: aconst_null
      //   64: astore 13
      //   66: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   69: astore 14
      //   71: aload 14
      //   73: monitorenter
      //   74: iload 5
      //   76: istore 15
      //   78: aload_0
      //   79: getfield 91	android/opengl/GLSurfaceView$GLThread:mShouldExit	Z
      //   82: ifeq +34 -> 116
      //   85: aload 14
      //   87: monitorexit
      //   88: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   91: astore 13
      //   93: aload 13
      //   95: monitorenter
      //   96: aload_0
      //   97: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   100: aload_0
      //   101: invokespecial 97	android/opengl/GLSurfaceView$GLThread:stopEglContextLocked	()V
      //   104: aload 13
      //   106: monitorexit
      //   107: return
      //   108: astore 16
      //   110: aload 13
      //   112: monitorexit
      //   113: aload 16
      //   115: athrow
      //   116: aload_0
      //   117: getfield 47	android/opengl/GLSurfaceView$GLThread:mEventQueue	Ljava/util/ArrayList;
      //   120: invokevirtual 101	java/util/ArrayList:isEmpty	()Z
      //   123: ifne +35 -> 158
      //   126: aload_0
      //   127: getfield 47	android/opengl/GLSurfaceView$GLThread:mEventQueue	Ljava/util/ArrayList;
      //   130: iconst_0
      //   131: invokevirtual 105	java/util/ArrayList:remove	(I)Ljava/lang/Object;
      //   134: checkcast 107	java/lang/Runnable
      //   137: astore 17
      //   139: iload 15
      //   141: istore 5
      //   143: iload 6
      //   145: istore 18
      //   147: iload 7
      //   149: istore 19
      //   151: iload 8
      //   153: istore 20
      //   155: goto +578 -> 733
      //   158: aload_0
      //   159: getfield 109	android/opengl/GLSurfaceView$GLThread:mPaused	Z
      //   162: istore 21
      //   164: iconst_0
      //   165: istore 22
      //   167: iload 21
      //   169: aload_0
      //   170: getfield 111	android/opengl/GLSurfaceView$GLThread:mRequestPaused	Z
      //   173: if_icmpeq +23 -> 196
      //   176: aload_0
      //   177: getfield 111	android/opengl/GLSurfaceView$GLThread:mRequestPaused	Z
      //   180: istore 22
      //   182: aload_0
      //   183: aload_0
      //   184: getfield 111	android/opengl/GLSurfaceView$GLThread:mRequestPaused	Z
      //   187: putfield 109	android/opengl/GLSurfaceView$GLThread:mPaused	Z
      //   190: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   193: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   196: iload 6
      //   198: istore 5
      //   200: aload_0
      //   201: getfield 118	android/opengl/GLSurfaceView$GLThread:mShouldReleaseEglContext	Z
      //   204: ifeq +19 -> 223
      //   207: aload_0
      //   208: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   211: aload_0
      //   212: invokespecial 97	android/opengl/GLSurfaceView$GLThread:stopEglContextLocked	()V
      //   215: aload_0
      //   216: iconst_0
      //   217: putfield 118	android/opengl/GLSurfaceView$GLThread:mShouldReleaseEglContext	Z
      //   220: iconst_1
      //   221: istore 5
      //   223: iload_2
      //   224: istore 23
      //   226: iload_2
      //   227: ifeq +14 -> 241
      //   230: aload_0
      //   231: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   234: aload_0
      //   235: invokespecial 97	android/opengl/GLSurfaceView$GLThread:stopEglContextLocked	()V
      //   238: iconst_0
      //   239: istore 23
      //   241: iload 22
      //   243: ifeq +14 -> 257
      //   246: aload_0
      //   247: getfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   250: ifeq +7 -> 257
      //   253: aload_0
      //   254: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   257: iload 22
      //   259: ifeq +49 -> 308
      //   262: aload_0
      //   263: getfield 83	android/opengl/GLSurfaceView$GLThread:mHaveEglContext	Z
      //   266: ifeq +42 -> 308
      //   269: aload_0
      //   270: getfield 63	android/opengl/GLSurfaceView$GLThread:mGLSurfaceViewWeakRef	Ljava/lang/ref/WeakReference;
      //   273: invokevirtual 124	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
      //   276: checkcast 6	android/opengl/GLSurfaceView
      //   279: astore 16
      //   281: aload 16
      //   283: ifnonnull +9 -> 292
      //   286: iconst_0
      //   287: istore 22
      //   289: goto +10 -> 299
      //   292: aload 16
      //   294: invokestatic 128	android/opengl/GLSurfaceView:access$900	(Landroid/opengl/GLSurfaceView;)Z
      //   297: istore 22
      //   299: iload 22
      //   301: ifne +7 -> 308
      //   304: aload_0
      //   305: invokespecial 97	android/opengl/GLSurfaceView$GLThread:stopEglContextLocked	()V
      //   308: aload_0
      //   309: getfield 130	android/opengl/GLSurfaceView$GLThread:mHasSurface	Z
      //   312: ifne +37 -> 349
      //   315: aload_0
      //   316: getfield 132	android/opengl/GLSurfaceView$GLThread:mWaitingForSurface	Z
      //   319: ifne +30 -> 349
      //   322: aload_0
      //   323: getfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   326: ifeq +7 -> 333
      //   329: aload_0
      //   330: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   333: aload_0
      //   334: iconst_1
      //   335: putfield 132	android/opengl/GLSurfaceView$GLThread:mWaitingForSurface	Z
      //   338: aload_0
      //   339: iconst_0
      //   340: putfield 134	android/opengl/GLSurfaceView$GLThread:mSurfaceIsBad	Z
      //   343: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   346: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   349: aload_0
      //   350: getfield 130	android/opengl/GLSurfaceView$GLThread:mHasSurface	Z
      //   353: ifeq +21 -> 374
      //   356: aload_0
      //   357: getfield 132	android/opengl/GLSurfaceView$GLThread:mWaitingForSurface	Z
      //   360: ifeq +14 -> 374
      //   363: aload_0
      //   364: iconst_0
      //   365: putfield 132	android/opengl/GLSurfaceView$GLThread:mWaitingForSurface	Z
      //   368: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   371: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   374: iload 15
      //   376: istore 24
      //   378: iload 15
      //   380: ifeq +22 -> 402
      //   383: aload_0
      //   384: iconst_0
      //   385: putfield 61	android/opengl/GLSurfaceView$GLThread:mWantRenderNotification	Z
      //   388: iconst_0
      //   389: istore 24
      //   391: aload_0
      //   392: iconst_1
      //   393: putfield 136	android/opengl/GLSurfaceView$GLThread:mRenderComplete	Z
      //   396: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   399: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   402: aload 13
      //   404: astore 16
      //   406: aload_0
      //   407: getfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   410: ifnull +14 -> 424
      //   413: aload_0
      //   414: getfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   417: astore 16
      //   419: aload_0
      //   420: aconst_null
      //   421: putfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   424: aload_0
      //   425: invokespecial 139	android/opengl/GLSurfaceView$GLThread:readyToDraw	()Z
      //   428: ifeq +837 -> 1265
      //   431: aload_0
      //   432: getfield 83	android/opengl/GLSurfaceView$GLThread:mHaveEglContext	Z
      //   435: istore 22
      //   437: iload 11
      //   439: istore 6
      //   441: iload 5
      //   443: istore 15
      //   445: iload 22
      //   447: ifne +58 -> 505
      //   450: iload 5
      //   452: ifeq +13 -> 465
      //   455: iconst_0
      //   456: istore 15
      //   458: iload 11
      //   460: istore 6
      //   462: goto +43 -> 505
      //   465: aload_0
      //   466: getfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   469: invokevirtual 142	android/opengl/GLSurfaceView$EglHelper:start	()V
      //   472: aload_0
      //   473: iconst_1
      //   474: putfield 83	android/opengl/GLSurfaceView$GLThread:mHaveEglContext	Z
      //   477: iconst_1
      //   478: istore 6
      //   480: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   483: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   486: iload 5
      //   488: istore 15
      //   490: goto +15 -> 505
      //   493: astore 13
      //   495: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   498: aload_0
      //   499: invokevirtual 148	android/opengl/GLSurfaceView$GLThreadManager:releaseEglContextLocked	(Landroid/opengl/GLSurfaceView$GLThread;)V
      //   502: aload 13
      //   504: athrow
      //   505: iload_1
      //   506: istore 25
      //   508: iload_3
      //   509: istore_2
      //   510: iload 10
      //   512: istore 5
      //   514: aload_0
      //   515: getfield 83	android/opengl/GLSurfaceView$GLThread:mHaveEglContext	Z
      //   518: ifeq +32 -> 550
      //   521: iload_1
      //   522: istore 25
      //   524: iload_3
      //   525: istore_2
      //   526: iload 10
      //   528: istore 5
      //   530: aload_0
      //   531: getfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   534: ifne +16 -> 550
      //   537: aload_0
      //   538: iconst_1
      //   539: putfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   542: iconst_1
      //   543: istore 5
      //   545: iconst_1
      //   546: istore 25
      //   548: iconst_1
      //   549: istore_2
      //   550: aload 16
      //   552: astore 13
      //   554: iload 6
      //   556: istore 19
      //   558: iload 25
      //   560: istore 20
      //   562: iload_2
      //   563: istore 26
      //   565: iload 15
      //   567: istore 18
      //   569: iload 5
      //   571: istore 27
      //   573: aload_0
      //   574: getfield 85	android/opengl/GLSurfaceView$GLThread:mHaveEglSurface	Z
      //   577: ifeq +751 -> 1328
      //   580: iload_2
      //   581: istore 27
      //   583: iload 8
      //   585: istore 26
      //   587: iload 5
      //   589: istore 8
      //   591: aload_0
      //   592: getfield 49	android/opengl/GLSurfaceView$GLThread:mSizeChanged	Z
      //   595: ifeq +31 -> 626
      //   598: iconst_1
      //   599: istore 27
      //   601: aload_0
      //   602: getfield 53	android/opengl/GLSurfaceView$GLThread:mWidth	I
      //   605: istore 7
      //   607: aload_0
      //   608: getfield 55	android/opengl/GLSurfaceView$GLThread:mHeight	I
      //   611: istore 26
      //   613: aload_0
      //   614: iconst_1
      //   615: putfield 61	android/opengl/GLSurfaceView$GLThread:mWantRenderNotification	Z
      //   618: iconst_1
      //   619: istore 8
      //   621: aload_0
      //   622: iconst_0
      //   623: putfield 49	android/opengl/GLSurfaceView$GLThread:mSizeChanged	Z
      //   626: aload_0
      //   627: iconst_0
      //   628: putfield 57	android/opengl/GLSurfaceView$GLThread:mRequestRender	Z
      //   631: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   634: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   637: aload_0
      //   638: getfield 61	android/opengl/GLSurfaceView$GLThread:mWantRenderNotification	Z
      //   641: istore 22
      //   643: aload 16
      //   645: astore 13
      //   647: iload 6
      //   649: istore 11
      //   651: iload 25
      //   653: istore_1
      //   654: iload 23
      //   656: istore_2
      //   657: iload 27
      //   659: istore_3
      //   660: iload 24
      //   662: istore 5
      //   664: iload 15
      //   666: istore 18
      //   668: iload 7
      //   670: istore 19
      //   672: iload 26
      //   674: istore 20
      //   676: aload 9
      //   678: astore 17
      //   680: iload 8
      //   682: istore 10
      //   684: iload 22
      //   686: ifeq +47 -> 733
      //   689: iconst_1
      //   690: istore 4
      //   692: iload 8
      //   694: istore 10
      //   696: aload 9
      //   698: astore 17
      //   700: iload 26
      //   702: istore 20
      //   704: iload 7
      //   706: istore 19
      //   708: iload 15
      //   710: istore 18
      //   712: iload 24
      //   714: istore 5
      //   716: iload 27
      //   718: istore_3
      //   719: iload 23
      //   721: istore_2
      //   722: iload 25
      //   724: istore_1
      //   725: iload 6
      //   727: istore 11
      //   729: aload 16
      //   731: astore 13
      //   733: aload 14
      //   735: monitorexit
      //   736: aload 17
      //   738: ifnull +28 -> 766
      //   741: aload 17
      //   743: invokeinterface 151 1 0
      //   748: aconst_null
      //   749: astore 9
      //   751: iload 18
      //   753: istore 6
      //   755: iload 19
      //   757: istore 7
      //   759: iload 20
      //   761: istore 8
      //   763: goto -697 -> 66
      //   766: iload 10
      //   768: istore 6
      //   770: iload 10
      //   772: ifeq +111 -> 883
      //   775: aload_0
      //   776: getfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   779: invokevirtual 154	android/opengl/GLSurfaceView$EglHelper:createSurface	()Z
      //   782: ifeq +39 -> 821
      //   785: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   788: astore 16
      //   790: aload 16
      //   792: monitorenter
      //   793: aload_0
      //   794: iconst_1
      //   795: putfield 156	android/opengl/GLSurfaceView$GLThread:mFinishedCreatingEglSurface	Z
      //   798: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   801: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   804: aload 16
      //   806: monitorexit
      //   807: iconst_0
      //   808: istore 6
      //   810: goto +73 -> 883
      //   813: astore 13
      //   815: aload 16
      //   817: monitorexit
      //   818: aload 13
      //   820: athrow
      //   821: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   824: astore 16
      //   826: aload 16
      //   828: monitorenter
      //   829: aload_0
      //   830: iconst_1
      //   831: putfield 156	android/opengl/GLSurfaceView$GLThread:mFinishedCreatingEglSurface	Z
      //   834: aload_0
      //   835: iconst_1
      //   836: putfield 134	android/opengl/GLSurfaceView$GLThread:mSurfaceIsBad	Z
      //   839: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   842: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   845: aload 16
      //   847: monitorexit
      //   848: iload 11
      //   850: istore 15
      //   852: iload 15
      //   854: istore 11
      //   856: iload 18
      //   858: istore 6
      //   860: iload 19
      //   862: istore 7
      //   864: iload 20
      //   866: istore 8
      //   868: aload 17
      //   870: astore 9
      //   872: goto -806 -> 66
      //   875: astore 13
      //   877: aload 16
      //   879: monitorexit
      //   880: aload 13
      //   882: athrow
      //   883: iload_1
      //   884: istore 10
      //   886: iload_1
      //   887: ifeq +18 -> 905
      //   890: aload_0
      //   891: getfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   894: invokevirtual 160	android/opengl/GLSurfaceView$EglHelper:createGL	()Ljavax/microedition/khronos/opengles/GL;
      //   897: checkcast 162	javax/microedition/khronos/opengles/GL10
      //   900: astore 12
      //   902: iconst_0
      //   903: istore 10
      //   905: iload 11
      //   907: istore_1
      //   908: iload 11
      //   910: ifeq +69 -> 979
      //   913: aload_0
      //   914: getfield 63	android/opengl/GLSurfaceView$GLThread:mGLSurfaceViewWeakRef	Ljava/lang/ref/WeakReference;
      //   917: invokevirtual 124	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
      //   920: checkcast 6	android/opengl/GLSurfaceView
      //   923: astore 16
      //   925: aload 16
      //   927: ifnull +50 -> 977
      //   930: ldc2_w 163
      //   933: ldc -90
      //   935: invokestatic 172	android/os/Trace:traceBegin	(JLjava/lang/String;)V
      //   938: aload 16
      //   940: invokestatic 176	android/opengl/GLSurfaceView:access$1000	(Landroid/opengl/GLSurfaceView;)Landroid/opengl/GLSurfaceView$Renderer;
      //   943: aload 12
      //   945: aload_0
      //   946: getfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   949: getfield 180	android/opengl/GLSurfaceView$EglHelper:mEglConfig	Ljavax/microedition/khronos/egl/EGLConfig;
      //   952: invokeinterface 185 3 0
      //   957: ldc2_w 163
      //   960: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   963: goto +14 -> 977
      //   966: astore 13
      //   968: ldc2_w 163
      //   971: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   974: aload 13
      //   976: athrow
      //   977: iconst_0
      //   978: istore_1
      //   979: iload_3
      //   980: ifeq +72 -> 1052
      //   983: aload_0
      //   984: getfield 63	android/opengl/GLSurfaceView$GLThread:mGLSurfaceViewWeakRef	Ljava/lang/ref/WeakReference;
      //   987: invokevirtual 124	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
      //   990: checkcast 6	android/opengl/GLSurfaceView
      //   993: astore 16
      //   995: aload 16
      //   997: ifnull +50 -> 1047
      //   1000: ldc2_w 163
      //   1003: ldc -65
      //   1005: invokestatic 172	android/os/Trace:traceBegin	(JLjava/lang/String;)V
      //   1008: aload 16
      //   1010: invokestatic 176	android/opengl/GLSurfaceView:access$1000	(Landroid/opengl/GLSurfaceView;)Landroid/opengl/GLSurfaceView$Renderer;
      //   1013: aload 12
      //   1015: iload 19
      //   1017: iload 20
      //   1019: invokeinterface 194 4 0
      //   1024: ldc2_w 163
      //   1027: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   1030: goto +17 -> 1047
      //   1033: astore 13
      //   1035: goto +3 -> 1038
      //   1038: ldc2_w 163
      //   1041: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   1044: aload 13
      //   1046: athrow
      //   1047: iconst_0
      //   1048: istore_3
      //   1049: goto +3 -> 1052
      //   1052: aload_0
      //   1053: getfield 63	android/opengl/GLSurfaceView$GLThread:mGLSurfaceViewWeakRef	Ljava/lang/ref/WeakReference;
      //   1056: invokevirtual 124	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
      //   1059: checkcast 6	android/opengl/GLSurfaceView
      //   1062: astore 16
      //   1064: aload 16
      //   1066: ifnull +90 -> 1156
      //   1069: ldc2_w 163
      //   1072: ldc -60
      //   1074: invokestatic 172	android/os/Trace:traceBegin	(JLjava/lang/String;)V
      //   1077: aload_0
      //   1078: getfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   1081: ifnull +17 -> 1098
      //   1084: aload_0
      //   1085: getfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   1088: astore 13
      //   1090: aload_0
      //   1091: aconst_null
      //   1092: putfield 51	android/opengl/GLSurfaceView$GLThread:mFinishDrawingRunnable	Ljava/lang/Runnable;
      //   1095: goto +3 -> 1098
      //   1098: aload 16
      //   1100: invokestatic 176	android/opengl/GLSurfaceView:access$1000	(Landroid/opengl/GLSurfaceView;)Landroid/opengl/GLSurfaceView$Renderer;
      //   1103: aload 12
      //   1105: invokeinterface 199 2 0
      //   1110: aload 13
      //   1112: astore 16
      //   1114: aload 13
      //   1116: ifnull +13 -> 1129
      //   1119: aload 13
      //   1121: invokeinterface 151 1 0
      //   1126: aconst_null
      //   1127: astore 16
      //   1129: ldc2_w 163
      //   1132: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   1135: aload 16
      //   1137: astore 13
      //   1139: goto +17 -> 1156
      //   1142: astore 13
      //   1144: goto +3 -> 1147
      //   1147: ldc2_w 163
      //   1150: invokestatic 189	android/os/Trace:traceEnd	(J)V
      //   1153: aload 13
      //   1155: athrow
      //   1156: aload_0
      //   1157: getfield 81	android/opengl/GLSurfaceView$GLThread:mEglHelper	Landroid/opengl/GLSurfaceView$EglHelper;
      //   1160: invokevirtual 203	android/opengl/GLSurfaceView$EglHelper:swap	()I
      //   1163: istore 11
      //   1165: iload 11
      //   1167: sipush 12288
      //   1170: if_icmpeq +58 -> 1228
      //   1173: iload 11
      //   1175: sipush 12302
      //   1178: if_icmpeq +45 -> 1223
      //   1181: ldc -52
      //   1183: ldc -50
      //   1185: iload 11
      //   1187: invokestatic 210	android/opengl/GLSurfaceView$EglHelper:logEglErrorAsWarning	(Ljava/lang/String;Ljava/lang/String;I)V
      //   1190: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   1193: astore 16
      //   1195: aload 16
      //   1197: monitorenter
      //   1198: aload_0
      //   1199: iconst_1
      //   1200: putfield 134	android/opengl/GLSurfaceView$GLThread:mSurfaceIsBad	Z
      //   1203: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   1206: invokevirtual 116	java/lang/Object:notifyAll	()V
      //   1209: aload 16
      //   1211: monitorexit
      //   1212: goto +16 -> 1228
      //   1215: astore 13
      //   1217: aload 16
      //   1219: monitorexit
      //   1220: aload 13
      //   1222: athrow
      //   1223: iconst_1
      //   1224: istore_2
      //   1225: goto +3 -> 1228
      //   1228: iload 4
      //   1230: istore 11
      //   1232: iload 4
      //   1234: ifeq +9 -> 1243
      //   1237: iconst_0
      //   1238: istore 11
      //   1240: iconst_1
      //   1241: istore 5
      //   1243: iload_1
      //   1244: istore 15
      //   1246: iload 10
      //   1248: istore_1
      //   1249: iload 11
      //   1251: istore 4
      //   1253: iload 6
      //   1255: istore 10
      //   1257: goto -405 -> 852
      //   1260: astore 13
      //   1262: goto +102 -> 1364
      //   1265: aload 16
      //   1267: astore 13
      //   1269: iload 11
      //   1271: istore 19
      //   1273: iload_1
      //   1274: istore 20
      //   1276: iload_3
      //   1277: istore 26
      //   1279: iload 5
      //   1281: istore 18
      //   1283: iload 10
      //   1285: istore 27
      //   1287: aload 16
      //   1289: ifnull +39 -> 1328
      //   1292: ldc -44
      //   1294: ldc -42
      //   1296: invokestatic 220	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   1299: pop
      //   1300: aload 16
      //   1302: invokeinterface 151 1 0
      //   1307: aconst_null
      //   1308: astore 13
      //   1310: iload 10
      //   1312: istore 27
      //   1314: iload 5
      //   1316: istore 18
      //   1318: iload_3
      //   1319: istore 26
      //   1321: iload_1
      //   1322: istore 20
      //   1324: iload 11
      //   1326: istore 19
      //   1328: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   1331: invokevirtual 223	java/lang/Object:wait	()V
      //   1334: iload 19
      //   1336: istore 11
      //   1338: iload 20
      //   1340: istore_1
      //   1341: iload 23
      //   1343: istore_2
      //   1344: iload 26
      //   1346: istore_3
      //   1347: iload 24
      //   1349: istore 15
      //   1351: iload 18
      //   1353: istore 6
      //   1355: iload 27
      //   1357: istore 10
      //   1359: goto -1281 -> 78
      //   1362: astore 13
      //   1364: aload 14
      //   1366: monitorexit
      //   1367: aload 13
      //   1369: athrow
      //   1370: astore 16
      //   1372: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   1375: astore 13
      //   1377: aload 13
      //   1379: monitorenter
      //   1380: aload_0
      //   1381: invokespecial 94	android/opengl/GLSurfaceView$GLThread:stopEglSurfaceLocked	()V
      //   1384: aload_0
      //   1385: invokespecial 97	android/opengl/GLSurfaceView$GLThread:stopEglContextLocked	()V
      //   1388: aload 13
      //   1390: monitorexit
      //   1391: aload 16
      //   1393: athrow
      //   1394: astore 16
      //   1396: aload 13
      //   1398: monitorexit
      //   1399: aload 16
      //   1401: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	1402	0	this	GLThread
      //   31	1310	1	i	int
      //   33	1311	2	j	int
      //   35	1312	3	k	int
      //   37	1215	4	m	int
      //   40	1275	5	n	int
      //   43	1311	6	i1	int
      //   46	817	7	i2	int
      //   49	818	8	i3	int
      //   52	819	9	localObject1	Object
      //   55	1303	10	i4	int
      //   58	1279	11	i5	int
      //   61	1043	12	localGL10	GL10
      //   64	339	13	localGLThreadManager1	GLSurfaceView.GLThreadManager
      //   493	10	13	localRuntimeException	RuntimeException
      //   552	180	13	localObject2	Object
      //   813	6	13	localObject3	Object
      //   875	6	13	localObject4	Object
      //   966	9	13	localObject5	Object
      //   1033	12	13	localObject6	Object
      //   1088	50	13	localObject7	Object
      //   1142	12	13	localObject8	Object
      //   1215	6	13	localObject9	Object
      //   1260	1	13	localObject10	Object
      //   1267	42	13	localObject11	Object
      //   1362	6	13	localObject12	Object
      //   69	1296	14	localGLThreadManager3	GLSurfaceView.GLThreadManager
      //   76	1274	15	i6	int
      //   108	6	16	localObject13	Object
      //   1370	22	16	localObject15	Object
      //   1394	6	16	localObject16	Object
      //   137	732	17	localObject17	Object
      //   145	1207	18	i7	int
      //   149	1186	19	i8	int
      //   153	1186	20	i9	int
      //   162	12	21	bool1	boolean
      //   165	520	22	bool2	boolean
      //   224	1118	23	i10	int
      //   376	972	24	i11	int
      //   506	217	25	i12	int
      //   563	782	26	i13	int
      //   571	785	27	i14	int
      // Exception table:
      //   from	to	target	type
      //   96	107	108	finally
      //   110	113	108	finally
      //   465	472	493	java/lang/RuntimeException
      //   793	807	813	finally
      //   815	818	813	finally
      //   829	848	875	finally
      //   877	880	875	finally
      //   930	957	966	finally
      //   1000	1024	1033	finally
      //   1069	1095	1142	finally
      //   1098	1110	1142	finally
      //   1119	1126	1142	finally
      //   1198	1212	1215	finally
      //   1217	1220	1215	finally
      //   733	736	1260	finally
      //   78	88	1362	finally
      //   116	139	1362	finally
      //   158	164	1362	finally
      //   167	196	1362	finally
      //   200	220	1362	finally
      //   230	238	1362	finally
      //   246	257	1362	finally
      //   262	281	1362	finally
      //   292	299	1362	finally
      //   304	308	1362	finally
      //   308	333	1362	finally
      //   333	349	1362	finally
      //   349	374	1362	finally
      //   383	388	1362	finally
      //   391	402	1362	finally
      //   406	424	1362	finally
      //   424	437	1362	finally
      //   465	472	1362	finally
      //   472	477	1362	finally
      //   480	486	1362	finally
      //   495	505	1362	finally
      //   514	521	1362	finally
      //   530	542	1362	finally
      //   573	580	1362	finally
      //   591	598	1362	finally
      //   601	618	1362	finally
      //   621	626	1362	finally
      //   626	643	1362	finally
      //   1292	1307	1362	finally
      //   1328	1334	1362	finally
      //   1364	1367	1362	finally
      //   66	74	1370	finally
      //   741	748	1370	finally
      //   775	793	1370	finally
      //   818	821	1370	finally
      //   821	829	1370	finally
      //   880	883	1370	finally
      //   890	902	1370	finally
      //   913	925	1370	finally
      //   957	963	1370	finally
      //   968	977	1370	finally
      //   983	995	1370	finally
      //   1024	1030	1370	finally
      //   1038	1047	1370	finally
      //   1052	1064	1370	finally
      //   1129	1135	1370	finally
      //   1147	1156	1370	finally
      //   1156	1165	1370	finally
      //   1181	1198	1370	finally
      //   1220	1223	1370	finally
      //   1367	1370	1370	finally
      //   1380	1391	1394	finally
      //   1396	1399	1394	finally
    }
    
    private boolean readyToDraw()
    {
      boolean bool1 = mPaused;
      boolean bool2 = true;
      if ((bool1) || (!mHasSurface) || (mSurfaceIsBad) || (mWidth <= 0) || (mHeight <= 0) || ((mRequestRender) || (mRenderMode != 1))) {
        bool2 = false;
      }
      return bool2;
    }
    
    private void stopEglContextLocked()
    {
      if (mHaveEglContext)
      {
        mEglHelper.finish();
        mHaveEglContext = false;
        GLSurfaceView.sGLThreadManager.releaseEglContextLocked(this);
      }
    }
    
    private void stopEglSurfaceLocked()
    {
      if (mHaveEglSurface)
      {
        mHaveEglSurface = false;
        mEglHelper.destroySurface();
      }
    }
    
    public boolean ableToDraw()
    {
      boolean bool;
      if ((mHaveEglContext) && (mHaveEglSurface) && (readyToDraw())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getRenderMode()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        int i = mRenderMode;
        return i;
      }
    }
    
    public void onPause()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mRequestPaused = true;
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          if (!mExited)
          {
            boolean bool = mPaused;
            if (!bool) {
              try
              {
                GLSurfaceView.sGLThreadManager.wait();
              }
              catch (InterruptedException localInterruptedException)
              {
                for (;;)
                {
                  Thread.currentThread().interrupt();
                }
              }
            }
          }
        }
        return;
      }
    }
    
    public void onResume()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mRequestPaused = false;
        mRequestRender = true;
        mRenderComplete = false;
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          if ((!mExited) && (mPaused))
          {
            boolean bool = mRenderComplete;
            if (!bool) {
              try
              {
                GLSurfaceView.sGLThreadManager.wait();
              }
              catch (InterruptedException localInterruptedException)
              {
                for (;;)
                {
                  Thread.currentThread().interrupt();
                }
              }
            }
          }
        }
        return;
      }
    }
    
    public void onWindowResize(int paramInt1, int paramInt2)
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mWidth = paramInt1;
        mHeight = paramInt2;
        mSizeChanged = true;
        mRequestRender = true;
        mRenderComplete = false;
        if (Thread.currentThread() == this) {
          return;
        }
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          if ((!mExited) && (!mPaused) && (!mRenderComplete))
          {
            boolean bool = ableToDraw();
            if (bool) {
              try
              {
                GLSurfaceView.sGLThreadManager.wait();
              }
              catch (InterruptedException localInterruptedException)
              {
                for (;;)
                {
                  Thread.currentThread().interrupt();
                }
              }
            }
          }
        }
        return;
      }
    }
    
    public void queueEvent(Runnable paramRunnable)
    {
      if (paramRunnable != null) {
        synchronized (GLSurfaceView.sGLThreadManager)
        {
          mEventQueue.add(paramRunnable);
          GLSurfaceView.sGLThreadManager.notifyAll();
          return;
        }
      }
      throw new IllegalArgumentException("r must not be null");
    }
    
    public void requestExitAndWait()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mShouldExit = true;
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          boolean bool = mExited;
          if (!bool) {
            try
            {
              GLSurfaceView.sGLThreadManager.wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;)
              {
                Thread.currentThread().interrupt();
              }
            }
          }
        }
        return;
      }
    }
    
    public void requestReleaseEglContextLocked()
    {
      mShouldReleaseEglContext = true;
      GLSurfaceView.sGLThreadManager.notifyAll();
    }
    
    public void requestRender()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mRequestRender = true;
        GLSurfaceView.sGLThreadManager.notifyAll();
        return;
      }
    }
    
    public void requestRenderAndNotify(Runnable paramRunnable)
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        if (Thread.currentThread() == this) {
          return;
        }
        mWantRenderNotification = true;
        mRequestRender = true;
        mRenderComplete = false;
        mFinishDrawingRunnable = paramRunnable;
        GLSurfaceView.sGLThreadManager.notifyAll();
        return;
      }
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: new 264	java/lang/StringBuilder
      //   3: dup
      //   4: invokespecial 265	java/lang/StringBuilder:<init>	()V
      //   7: astore_1
      //   8: aload_1
      //   9: ldc_w 267
      //   12: invokevirtual 271	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   15: pop
      //   16: aload_1
      //   17: aload_0
      //   18: invokevirtual 275	android/opengl/GLSurfaceView$GLThread:getId	()J
      //   21: invokevirtual 278	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   24: pop
      //   25: aload_0
      //   26: aload_1
      //   27: invokevirtual 282	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   30: invokevirtual 285	android/opengl/GLSurfaceView$GLThread:setName	(Ljava/lang/String;)V
      //   33: aload_0
      //   34: invokespecial 287	android/opengl/GLSurfaceView$GLThread:guardedRun	()V
      //   37: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   40: aload_0
      //   41: invokevirtual 290	android/opengl/GLSurfaceView$GLThreadManager:threadExiting	(Landroid/opengl/GLSurfaceView$GLThread;)V
      //   44: goto +17 -> 61
      //   47: astore_1
      //   48: invokestatic 89	android/opengl/GLSurfaceView:access$800	()Landroid/opengl/GLSurfaceView$GLThreadManager;
      //   51: aload_0
      //   52: invokevirtual 290	android/opengl/GLSurfaceView$GLThreadManager:threadExiting	(Landroid/opengl/GLSurfaceView$GLThread;)V
      //   55: aload_1
      //   56: athrow
      //   57: astore_1
      //   58: goto -21 -> 37
      //   61: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	62	0	this	GLThread
      //   7	20	1	localStringBuilder	StringBuilder
      //   47	9	1	localObject	Object
      //   57	1	1	localInterruptedException	InterruptedException
      // Exception table:
      //   from	to	target	type
      //   33	37	47	finally
      //   33	37	57	java/lang/InterruptedException
    }
    
    public void setRenderMode(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 1)) {
        synchronized (GLSurfaceView.sGLThreadManager)
        {
          mRenderMode = paramInt;
          GLSurfaceView.sGLThreadManager.notifyAll();
          return;
        }
      }
      throw new IllegalArgumentException("renderMode");
    }
    
    public void surfaceCreated()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mHasSurface = true;
        mFinishedCreatingEglSurface = false;
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          if ((mWaitingForSurface) && (!mFinishedCreatingEglSurface))
          {
            boolean bool = mExited;
            if (!bool) {
              try
              {
                GLSurfaceView.sGLThreadManager.wait();
              }
              catch (InterruptedException localInterruptedException)
              {
                for (;;)
                {
                  Thread.currentThread().interrupt();
                }
              }
            }
          }
        }
        return;
      }
    }
    
    public void surfaceDestroyed()
    {
      synchronized (GLSurfaceView.sGLThreadManager)
      {
        mHasSurface = false;
        GLSurfaceView.sGLThreadManager.notifyAll();
        for (;;)
        {
          if (!mWaitingForSurface)
          {
            boolean bool = mExited;
            if (!bool) {
              try
              {
                GLSurfaceView.sGLThreadManager.wait();
              }
              catch (InterruptedException localInterruptedException)
              {
                for (;;)
                {
                  Thread.currentThread().interrupt();
                }
              }
            }
          }
        }
        return;
      }
    }
  }
  
  private static class GLThreadManager
  {
    private static String TAG = "GLThreadManager";
    
    private GLThreadManager() {}
    
    public void releaseEglContextLocked(GLSurfaceView.GLThread paramGLThread)
    {
      notifyAll();
    }
    
    public void threadExiting(GLSurfaceView.GLThread paramGLThread)
    {
      try
      {
        GLSurfaceView.GLThread.access$1102(paramGLThread, true);
        notifyAll();
        return;
      }
      finally
      {
        paramGLThread = finally;
        throw paramGLThread;
      }
    }
  }
  
  public static abstract interface GLWrapper
  {
    public abstract GL wrap(GL paramGL);
  }
  
  static class LogWriter
    extends Writer
  {
    private StringBuilder mBuilder = new StringBuilder();
    
    LogWriter() {}
    
    private void flushBuilder()
    {
      if (mBuilder.length() > 0)
      {
        Log.v("GLSurfaceView", mBuilder.toString());
        mBuilder.delete(0, mBuilder.length());
      }
    }
    
    public void close()
    {
      flushBuilder();
    }
    
    public void flush()
    {
      flushBuilder();
    }
    
    public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      for (int i = 0; i < paramInt2; i++)
      {
        char c = paramArrayOfChar[(paramInt1 + i)];
        if (c == '\n') {
          flushBuilder();
        } else {
          mBuilder.append(c);
        }
      }
    }
  }
  
  public static abstract interface Renderer
  {
    public abstract void onDrawFrame(GL10 paramGL10);
    
    public abstract void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2);
    
    public abstract void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig);
  }
  
  private class SimpleEGLConfigChooser
    extends GLSurfaceView.ComponentSizeChooser
  {
    public SimpleEGLConfigChooser(boolean paramBoolean)
    {
      super(8, 8, 8, 0, i, 0);
    }
  }
}
