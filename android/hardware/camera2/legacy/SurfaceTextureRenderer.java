package android.hardware.camera2.legacy;

import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Environment;
import android.os.SystemProperties;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SurfaceTextureRenderer
{
  private static final boolean DEBUG = false;
  private static final int EGL_COLOR_BITLENGTH = 8;
  private static final int EGL_RECORDABLE_ANDROID = 12610;
  private static final int FLIP_TYPE_BOTH = 3;
  private static final int FLIP_TYPE_HORIZONTAL = 1;
  private static final int FLIP_TYPE_NONE = 0;
  private static final int FLIP_TYPE_VERTICAL = 2;
  private static final int FLOAT_SIZE_BYTES = 4;
  private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
  private static final int GLES_VERSION = 2;
  private static final int GL_MATRIX_SIZE = 16;
  private static final String LEGACY_PERF_PROPERTY = "persist.camera.legacy_perf";
  private static final int PBUFFER_PIXEL_BYTES = 4;
  private static final String TAG = SurfaceTextureRenderer.class.getSimpleName();
  private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
  private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 20;
  private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
  private static final int VERTEX_POS_SIZE = 3;
  private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
  private static final int VERTEX_UV_SIZE = 2;
  private static final float[] sBothFlipTriangleVertices = { -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, -1.0F, 0.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
  private static final float[] sHorizontalFlipTriangleVertices = { -1.0F, -1.0F, 0.0F, 1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F };
  private static final float[] sRegularTriangleVertices = { -1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
  private static final float[] sVerticalFlipTriangleVertices = { -1.0F, -1.0F, 0.0F, 0.0F, 1.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F };
  private FloatBuffer mBothFlipTriangleVertices;
  private EGLConfig mConfigs;
  private List<EGLSurfaceHolder> mConversionSurfaces = new ArrayList();
  private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
  private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
  private final int mFacing;
  private FloatBuffer mHorizontalFlipTriangleVertices;
  private float[] mMVPMatrix = new float[16];
  private ByteBuffer mPBufferPixels;
  private PerfMeasurement mPerfMeasurer = null;
  private int mProgram;
  private FloatBuffer mRegularTriangleVertices;
  private float[] mSTMatrix = new float[16];
  private volatile SurfaceTexture mSurfaceTexture;
  private List<EGLSurfaceHolder> mSurfaces = new ArrayList();
  private int mTextureID = 0;
  private FloatBuffer mVerticalFlipTriangleVertices;
  private int maPositionHandle;
  private int maTextureHandle;
  private int muMVPMatrixHandle;
  private int muSTMatrixHandle;
  
  public SurfaceTextureRenderer(int paramInt)
  {
    mFacing = paramInt;
    mRegularTriangleVertices = ByteBuffer.allocateDirect(sRegularTriangleVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mRegularTriangleVertices.put(sRegularTriangleVertices).position(0);
    mHorizontalFlipTriangleVertices = ByteBuffer.allocateDirect(sHorizontalFlipTriangleVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mHorizontalFlipTriangleVertices.put(sHorizontalFlipTriangleVertices).position(0);
    mVerticalFlipTriangleVertices = ByteBuffer.allocateDirect(sVerticalFlipTriangleVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mVerticalFlipTriangleVertices.put(sVerticalFlipTriangleVertices).position(0);
    mBothFlipTriangleVertices = ByteBuffer.allocateDirect(sBothFlipTriangleVertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mBothFlipTriangleVertices.put(sBothFlipTriangleVertices).position(0);
    android.opengl.Matrix.setIdentityM(mSTMatrix, 0);
  }
  
  private void addGlTimestamp(long paramLong)
  {
    if (mPerfMeasurer == null) {
      return;
    }
    mPerfMeasurer.addTimestamp(paramLong);
  }
  
  private void beginGlTiming()
  {
    if (mPerfMeasurer == null) {
      return;
    }
    mPerfMeasurer.startTimer();
  }
  
  private void checkEglError(String paramString)
  {
    int i = EGL14.eglGetError();
    if (i == 12288) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(": EGL error: 0x");
    localStringBuilder.append(Integer.toHexString(i));
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void checkGlDrawError(String paramString)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    int i = 0;
    int j = 0;
    int k;
    for (;;)
    {
      k = GLES20.glGetError();
      if (k == 0) {
        break;
      }
      if (k == 1285) {
        i = 1;
      } else {
        j = 1;
      }
    }
    if (j == 0)
    {
      if (i == 0) {
        return;
      }
      throw new LegacyExceptionUtils.BufferQueueAbandonedException();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(": GLES20 error: 0x");
    localStringBuilder.append(Integer.toHexString(k));
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void checkGlError(String paramString)
  {
    int i = GLES20.glGetError();
    if (i == 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(": GLES20 error: 0x");
    localStringBuilder.append(Integer.toHexString(i));
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void clearState()
  {
    mSurfaces.clear();
    Iterator localIterator = mConversionSurfaces.iterator();
    while (localIterator.hasNext())
    {
      EGLSurfaceHolder localEGLSurfaceHolder = (EGLSurfaceHolder)localIterator.next();
      try
      {
        LegacyCameraDevice.disconnectSurface(surface);
      }
      catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
      {
        Log.w(TAG, "Surface abandoned, skipping...", localBufferQueueAbandonedException);
      }
    }
    mConversionSurfaces.clear();
    mPBufferPixels = null;
    if (mSurfaceTexture != null) {
      mSurfaceTexture.release();
    }
    mSurfaceTexture = null;
  }
  
  private void configureEGLContext()
  {
    mEGLDisplay = EGL14.eglGetDisplay(0);
    if (mEGLDisplay != EGL14.EGL_NO_DISPLAY)
    {
      Object localObject = new int[2];
      if (EGL14.eglInitialize(mEGLDisplay, (int[])localObject, 0, (int[])localObject, 1))
      {
        localObject = new EGLConfig[1];
        int[] arrayOfInt = new int[1];
        EGLDisplay localEGLDisplay = mEGLDisplay;
        int i = localObject.length;
        EGL14.eglChooseConfig(localEGLDisplay, new int[] { 12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12339, 5, 12344 }, 0, (EGLConfig[])localObject, 0, i, arrayOfInt, 0);
        checkEglError("eglCreateContext RGB888+recordable ES2");
        mConfigs = localObject[0];
        mEGLContext = EGL14.eglCreateContext(mEGLDisplay, localObject[0], EGL14.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 }, 0);
        checkEglError("eglCreateContext");
        if (mEGLContext != EGL14.EGL_NO_CONTEXT) {
          return;
        }
        throw new IllegalStateException("No EGLContext could be made");
      }
      throw new IllegalStateException("Cannot initialize EGL14");
    }
    throw new IllegalStateException("No EGL14 display");
  }
  
  private void configureEGLOutputSurfaces(Collection<EGLSurfaceHolder> paramCollection)
  {
    if ((paramCollection != null) && (paramCollection.size() != 0))
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        paramCollection = (EGLSurfaceHolder)localIterator.next();
        eglSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, mConfigs, surface, new int[] { 12344 }, 0);
        checkEglError("eglCreateWindowSurface");
      }
      return;
    }
    throw new IllegalStateException("No Surfaces were provided to draw to");
  }
  
  private void configureEGLPbufferSurfaces(Collection<EGLSurfaceHolder> paramCollection)
  {
    if ((paramCollection != null) && (paramCollection.size() != 0))
    {
      int i = 0;
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext())
      {
        EGLSurfaceHolder localEGLSurfaceHolder = (EGLSurfaceHolder)paramCollection.next();
        int j = width * height;
        if (j > i) {
          i = j;
        }
        j = width;
        int k = height;
        eglSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, mConfigs, new int[] { 12375, j, 12374, k, 12344 }, 0);
        checkEglError("eglCreatePbufferSurface");
      }
      mPBufferPixels = ByteBuffer.allocateDirect(i * 4).order(ByteOrder.nativeOrder());
      return;
    }
    throw new IllegalStateException("No Surfaces were provided to draw to");
  }
  
  private int createProgram(String paramString1, String paramString2)
  {
    int i = loadShader(35633, paramString1);
    if (i == 0) {
      return 0;
    }
    int j = loadShader(35632, paramString2);
    if (j == 0) {
      return 0;
    }
    int k = GLES20.glCreateProgram();
    checkGlError("glCreateProgram");
    if (k == 0) {
      Log.e(TAG, "Could not create program");
    }
    GLES20.glAttachShader(k, i);
    checkGlError("glAttachShader");
    GLES20.glAttachShader(k, j);
    checkGlError("glAttachShader");
    GLES20.glLinkProgram(k);
    paramString1 = new int[1];
    GLES20.glGetProgramiv(k, 35714, paramString1, 0);
    if (paramString1[0] == 1) {
      return k;
    }
    Log.e(TAG, "Could not link program: ");
    Log.e(TAG, GLES20.glGetProgramInfoLog(k));
    GLES20.glDeleteProgram(k);
    throw new IllegalStateException("Could not link program");
  }
  
  private void drawFrame(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2, int paramInt3)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    checkGlError("onDrawFrame start");
    paramSurfaceTexture.getTransformMatrix(mSTMatrix);
    android.opengl.Matrix.setIdentityM(mMVPMatrix, 0);
    try
    {
      paramSurfaceTexture = LegacyCameraDevice.getTextureSize(paramSurfaceTexture);
      float f1 = paramSurfaceTexture.getWidth();
      float f2 = paramSurfaceTexture.getHeight();
      if ((f1 > 0.0F) && (f2 > 0.0F))
      {
        RectF localRectF1 = new RectF(0.0F, 0.0F, f1, f2);
        RectF localRectF2 = new RectF(0.0F, 0.0F, paramInt1, paramInt2);
        paramSurfaceTexture = new android.graphics.Matrix();
        paramSurfaceTexture.setRectToRect(localRectF2, localRectF1, Matrix.ScaleToFit.CENTER);
        paramSurfaceTexture.mapRect(localRectF2);
        f2 = localRectF1.width() / localRectF2.width();
        f1 = localRectF1.height() / localRectF2.height();
        android.opengl.Matrix.scaleM(mMVPMatrix, 0, f2, f1, 1.0F);
        GLES20.glViewport(0, 0, paramInt1, paramInt2);
        GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, mTextureID);
        switch (paramInt3)
        {
        default: 
          paramSurfaceTexture = mRegularTriangleVertices;
          break;
        case 3: 
          paramSurfaceTexture = mBothFlipTriangleVertices;
          break;
        case 2: 
          paramSurfaceTexture = mVerticalFlipTriangleVertices;
          break;
        case 1: 
          paramSurfaceTexture = mHorizontalFlipTriangleVertices;
        }
        paramSurfaceTexture.position(0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, 5126, false, 20, paramSurfaceTexture);
        checkGlError("glVertexAttribPointer maPosition");
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        checkGlError("glEnableVertexAttribArray maPositionHandle");
        paramSurfaceTexture.position(3);
        GLES20.glVertexAttribPointer(maTextureHandle, 2, 5126, false, 20, paramSurfaceTexture);
        checkGlError("glVertexAttribPointer maTextureHandle");
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        checkGlError("glEnableVertexAttribArray maTextureHandle");
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);
        GLES20.glDrawArrays(5, 0, 4);
        checkGlDrawError("glDrawArrays");
        return;
      }
      throw new IllegalStateException("Illegal intermediate texture with dimension of 0");
    }
    catch (LegacyExceptionUtils.BufferQueueAbandonedException paramSurfaceTexture)
    {
      throw new IllegalStateException("Surface abandoned, skipping drawFrame...", paramSurfaceTexture);
    }
  }
  
  private void dumpGlTiming()
  {
    if (mPerfMeasurer == null) {
      return;
    }
    Object localObject1 = new File(Environment.getExternalStorageDirectory(), "CameraLegacy");
    if ((!((File)localObject1).exists()) && (!((File)localObject1).mkdirs()))
    {
      Log.e(TAG, "Failed to create directory for data dump");
      return;
    }
    localObject1 = new StringBuilder(((File)localObject1).getPath());
    ((StringBuilder)localObject1).append(File.separator);
    ((StringBuilder)localObject1).append("durations_");
    Object localObject2 = new Time();
    ((Time)localObject2).setToNow();
    ((StringBuilder)localObject1).append(((Time)localObject2).format2445());
    ((StringBuilder)localObject1).append("_S");
    localObject2 = mSurfaces.iterator();
    EGLSurfaceHolder localEGLSurfaceHolder;
    while (((Iterator)localObject2).hasNext())
    {
      localEGLSurfaceHolder = (EGLSurfaceHolder)((Iterator)localObject2).next();
      ((StringBuilder)localObject1).append(String.format("_%d_%d", new Object[] { Integer.valueOf(width), Integer.valueOf(height) }));
    }
    ((StringBuilder)localObject1).append("_C");
    localObject2 = mConversionSurfaces.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localEGLSurfaceHolder = (EGLSurfaceHolder)((Iterator)localObject2).next();
      ((StringBuilder)localObject1).append(String.format("_%d_%d", new Object[] { Integer.valueOf(width), Integer.valueOf(height) }));
    }
    ((StringBuilder)localObject1).append(".txt");
    mPerfMeasurer.dumpPerformanceData(((StringBuilder)localObject1).toString());
  }
  
  private void endGlTiming()
  {
    if (mPerfMeasurer == null) {
      return;
    }
    mPerfMeasurer.stopTimer();
  }
  
  private int getTextureId()
  {
    return mTextureID;
  }
  
  private void initializeGLState()
  {
    mProgram = createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
    if (mProgram != 0)
    {
      maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
      checkGlError("glGetAttribLocation aPosition");
      if (maPositionHandle != -1)
      {
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle != -1)
        {
          muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
          checkGlError("glGetUniformLocation uMVPMatrix");
          if (muMVPMatrixHandle != -1)
          {
            muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
            checkGlError("glGetUniformLocation uSTMatrix");
            if (muSTMatrixHandle != -1)
            {
              int[] arrayOfInt = new int[1];
              GLES20.glGenTextures(1, arrayOfInt, 0);
              mTextureID = arrayOfInt[0];
              GLES20.glBindTexture(36197, mTextureID);
              checkGlError("glBindTexture mTextureID");
              GLES20.glTexParameterf(36197, 10241, 9728.0F);
              GLES20.glTexParameterf(36197, 10240, 9729.0F);
              GLES20.glTexParameteri(36197, 10242, 33071);
              GLES20.glTexParameteri(36197, 10243, 33071);
              checkGlError("glTexParameter");
              return;
            }
            throw new IllegalStateException("Could not get attrib location for uSTMatrix");
          }
          throw new IllegalStateException("Could not get attrib location for uMVPMatrix");
        }
        throw new IllegalStateException("Could not get attrib location for aTextureCoord");
      }
      throw new IllegalStateException("Could not get attrib location for aPosition");
    }
    throw new IllegalStateException("failed creating program");
  }
  
  private int loadShader(int paramInt, String paramString)
  {
    int i = GLES20.glCreateShader(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("glCreateShader type=");
    localStringBuilder.append(paramInt);
    checkGlError(localStringBuilder.toString());
    GLES20.glShaderSource(i, paramString);
    GLES20.glCompileShader(i);
    paramString = new int[1];
    GLES20.glGetShaderiv(i, 35713, paramString, 0);
    if (paramString[0] != 0) {
      return i;
    }
    paramString = TAG;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not compile shader ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(":");
    Log.e(paramString, localStringBuilder.toString());
    paramString = TAG;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" ");
    localStringBuilder.append(GLES20.glGetShaderInfoLog(i));
    Log.e(paramString, localStringBuilder.toString());
    GLES20.glDeleteShader(i);
    paramString = new StringBuilder();
    paramString.append("Could not compile shader ");
    paramString.append(paramInt);
    throw new IllegalStateException(paramString.toString());
  }
  
  private void makeCurrent(EGLSurface paramEGLSurface)
  {
    EGL14.eglMakeCurrent(mEGLDisplay, paramEGLSurface, paramEGLSurface, mEGLContext);
    checkEglError("makeCurrent");
  }
  
  private void releaseEGLContext()
  {
    if (mEGLDisplay != EGL14.EGL_NO_DISPLAY)
    {
      EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
      dumpGlTiming();
      Iterator localIterator;
      EGLSurfaceHolder localEGLSurfaceHolder;
      if (mSurfaces != null)
      {
        localIterator = mSurfaces.iterator();
        while (localIterator.hasNext())
        {
          localEGLSurfaceHolder = (EGLSurfaceHolder)localIterator.next();
          if (eglSurface != null) {
            EGL14.eglDestroySurface(mEGLDisplay, eglSurface);
          }
        }
      }
      if (mConversionSurfaces != null)
      {
        localIterator = mConversionSurfaces.iterator();
        while (localIterator.hasNext())
        {
          localEGLSurfaceHolder = (EGLSurfaceHolder)localIterator.next();
          if (eglSurface != null) {
            EGL14.eglDestroySurface(mEGLDisplay, eglSurface);
          }
        }
      }
      EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
      EGL14.eglReleaseThread();
      EGL14.eglTerminate(mEGLDisplay);
    }
    mConfigs = null;
    mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    mEGLContext = EGL14.EGL_NO_CONTEXT;
    clearState();
  }
  
  private void setupGlTiming()
  {
    if (PerfMeasurement.isGlTimingSupported())
    {
      Log.d(TAG, "Enabling GL performance measurement");
      mPerfMeasurer = new PerfMeasurement();
    }
    else
    {
      Log.d(TAG, "GL performance measurement not supported on this device");
      mPerfMeasurer = null;
    }
  }
  
  private boolean swapBuffers(EGLSurface paramEGLSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    boolean bool = EGL14.eglSwapBuffers(mEGLDisplay, paramEGLSurface);
    int i = EGL14.eglGetError();
    if (i != 12288)
    {
      if ((i != 12299) && (i != 12301))
      {
        paramEGLSurface = new StringBuilder();
        paramEGLSurface.append("swapBuffers: EGL error: 0x");
        paramEGLSurface.append(Integer.toHexString(i));
        throw new IllegalStateException(paramEGLSurface.toString());
      }
      throw new LegacyExceptionUtils.BufferQueueAbandonedException();
    }
    return bool;
  }
  
  public void cleanupEGLContext()
  {
    releaseEGLContext();
  }
  
  public void configureSurfaces(Collection<Pair<Surface, Size>> paramCollection)
  {
    releaseEGLContext();
    if ((paramCollection != null) && (paramCollection.size() != 0))
    {
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext())
      {
        Object localObject = (Pair)paramCollection.next();
        Surface localSurface = (Surface)first;
        Size localSize = (Size)second;
        try
        {
          localObject = new android/hardware/camera2/legacy/SurfaceTextureRenderer$EGLSurfaceHolder;
          ((EGLSurfaceHolder)localObject).<init>(this, null);
          surface = localSurface;
          width = localSize.getWidth();
          height = localSize.getHeight();
          if (LegacyCameraDevice.needsConversion(localSurface))
          {
            mConversionSurfaces.add(localObject);
            LegacyCameraDevice.connectSurface(localSurface);
          }
          else
          {
            mSurfaces.add(localObject);
          }
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
        {
          Log.w(TAG, "Surface abandoned, skipping configuration... ", localBufferQueueAbandonedException);
        }
      }
      configureEGLContext();
      if (mSurfaces.size() > 0) {
        configureEGLOutputSurfaces(mSurfaces);
      }
      if (mConversionSurfaces.size() > 0) {
        configureEGLPbufferSurfaces(mConversionSurfaces);
      }
      if (mSurfaces.size() > 0) {
        paramCollection = mSurfaces.get(0)).eglSurface;
      } else {
        paramCollection = mConversionSurfaces.get(0)).eglSurface;
      }
      makeCurrent(paramCollection);
      initializeGLState();
      mSurfaceTexture = new SurfaceTexture(getTextureId());
      if (SystemProperties.getBoolean("persist.camera.legacy_perf", false)) {
        setupGlTiming();
      }
      return;
    }
    Log.w(TAG, "No output surfaces configured for GL drawing.");
  }
  
  public void drawIntoSurfaces(CaptureCollector paramCaptureCollector)
  {
    if (((mSurfaces != null) && (mSurfaces.size() != 0)) || ((mConversionSurfaces != null) && (mConversionSurfaces.size() != 0)))
    {
      boolean bool = paramCaptureCollector.hasPendingPreviewCaptures();
      checkGlError("before updateTexImage");
      if (bool) {
        beginGlTiming();
      }
      mSurfaceTexture.updateTexImage();
      long l = mSurfaceTexture.getTimestamp();
      Pair localPair = paramCaptureCollector.previewCaptured(l);
      if (localPair == null)
      {
        if (bool) {
          endGlTiming();
        }
        return;
      }
      RequestHolder localRequestHolder = (RequestHolder)first;
      Object localObject1 = localRequestHolder.getHolderTargets();
      if (bool) {
        addGlTimestamp(l);
      }
      Object localObject2 = new ArrayList();
      try
      {
        localObject1 = LegacyCameraDevice.getSurfaceIds((Collection)localObject1);
        localObject2 = localObject1;
      }
      catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException1)
      {
        Log.w(TAG, "Surface abandoned, dropping frame. ", localBufferQueueAbandonedException1);
        localRequestHolder.setOutputAbandoned();
      }
      Iterator localIterator = mSurfaces.iterator();
      Object localObject3;
      int i;
      int j;
      int k;
      while (localIterator.hasNext())
      {
        localObject3 = (EGLSurfaceHolder)localIterator.next();
        if (LegacyCameraDevice.containsSurfaceId(surface, (Collection)localObject2)) {
          try
          {
            LegacyCameraDevice.setSurfaceDimens(surface, width, height);
            makeCurrent(eglSurface);
            LegacyCameraDevice.setNextTimestamp(surface, ((Long)second).longValue());
            SurfaceTexture localSurfaceTexture = mSurfaceTexture;
            i = width;
            j = height;
            if (mFacing == 0) {
              k = 1;
            } else {
              k = 0;
            }
            drawFrame(localSurfaceTexture, i, j, k);
            swapBuffers(eglSurface);
          }
          catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException2)
          {
            Log.w(TAG, "Surface abandoned, dropping frame. ", localBufferQueueAbandonedException2);
            localRequestHolder.setOutputAbandoned();
          }
        }
      }
      localIterator = mConversionSurfaces.iterator();
      for (;;)
      {
        if (localIterator.hasNext())
        {
          EGLSurfaceHolder localEGLSurfaceHolder = (EGLSurfaceHolder)localIterator.next();
          if (LegacyCameraDevice.containsSurfaceId(surface, (Collection)localObject2))
          {
            makeCurrent(eglSurface);
            try
            {
              localObject3 = mSurfaceTexture;
              i = width;
              j = height;
              if (mFacing == 0) {
                k = 3;
              } else {
                k = 2;
              }
              drawFrame((SurfaceTexture)localObject3, i, j, k);
              mPBufferPixels.clear();
              GLES20.glReadPixels(0, 0, width, height, 6408, 5121, mPBufferPixels);
              checkGlError("glReadPixels");
              try
              {
                k = LegacyCameraDevice.detectSurfaceType(surface);
                LegacyCameraDevice.setSurfaceDimens(surface, width, height);
                LegacyCameraDevice.setNextTimestamp(surface, ((Long)second).longValue());
                LegacyCameraDevice.produceFrame(surface, mPBufferPixels.array(), width, height, k);
              }
              catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException3)
              {
                Log.w(TAG, "Surface abandoned, dropping frame. ", localBufferQueueAbandonedException3);
                localRequestHolder.setOutputAbandoned();
              }
            }
            catch (LegacyExceptionUtils.BufferQueueAbandonedException paramCaptureCollector)
            {
              throw new IllegalStateException("Surface abandoned, skipping drawFrame...", paramCaptureCollector);
            }
          }
        }
      }
      paramCaptureCollector.previewProduced();
      if (bool) {
        endGlTiming();
      }
      return;
    }
  }
  
  public void flush()
  {
    Log.e(TAG, "Flush not yet implemented.");
  }
  
  public SurfaceTexture getSurfaceTexture()
  {
    return mSurfaceTexture;
  }
  
  private class EGLSurfaceHolder
  {
    EGLSurface eglSurface;
    int height;
    Surface surface;
    int width;
    
    private EGLSurfaceHolder() {}
  }
}
