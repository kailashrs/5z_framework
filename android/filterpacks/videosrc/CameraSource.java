package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.opengl.Matrix;
import android.util.Log;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CameraSource
  extends Filter
{
  private static final int NEWFRAME_TIMEOUT = 100;
  private static final int NEWFRAME_TIMEOUT_REPEAT = 10;
  private static final String TAG = "CameraSource";
  private static final String mFrameShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n";
  private static final float[] mSourceCoords = { 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F };
  private Camera mCamera;
  private GLFrame mCameraFrame;
  @GenerateFieldPort(hasDefault=true, name="id")
  private int mCameraId = 0;
  private Camera.Parameters mCameraParameters;
  private float[] mCameraTransform = new float[16];
  @GenerateFieldPort(hasDefault=true, name="framerate")
  private int mFps = 30;
  private ShaderProgram mFrameExtractor;
  @GenerateFieldPort(hasDefault=true, name="height")
  private int mHeight = 240;
  private final boolean mLogVerbose = Log.isLoggable("CameraSource", 2);
  private float[] mMappedCoords = new float[16];
  private boolean mNewFrameAvailable;
  private MutableFrameFormat mOutputFormat;
  private SurfaceTexture mSurfaceTexture;
  @GenerateFinalPort(hasDefault=true, name="waitForNewFrame")
  private boolean mWaitForNewFrame = true;
  @GenerateFieldPort(hasDefault=true, name="width")
  private int mWidth = 320;
  private SurfaceTexture.OnFrameAvailableListener onCameraFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      if (mLogVerbose) {
        Log.v("CameraSource", "New frame from camera");
      }
      synchronized (CameraSource.this)
      {
        CameraSource.access$102(CameraSource.this, true);
        notify();
        return;
      }
    }
  };
  
  public CameraSource(String paramString)
  {
    super(paramString);
  }
  
  private void createFormats()
  {
    mOutputFormat = ImageFormat.create(mWidth, mHeight, 3, 3);
  }
  
  private int[] findClosestFpsRange(int paramInt, Camera.Parameters paramParameters)
  {
    Object localObject = paramParameters.getSupportedPreviewFpsRange();
    paramParameters = (int[])((List)localObject).get(0);
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      int[] arrayOfInt = (int[])localIterator.next();
      localObject = paramParameters;
      if (arrayOfInt[0] < paramInt * 1000)
      {
        localObject = paramParameters;
        if (arrayOfInt[1] > paramInt * 1000)
        {
          localObject = paramParameters;
          if (arrayOfInt[0] > paramParameters[0])
          {
            localObject = paramParameters;
            if (arrayOfInt[1] < paramParameters[1]) {
              localObject = arrayOfInt;
            }
          }
        }
      }
      paramParameters = (Camera.Parameters)localObject;
    }
    if (mLogVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Requested fps: ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(".Closest frame rate range: [");
      ((StringBuilder)localObject).append(paramParameters[0] / 1000.0D);
      ((StringBuilder)localObject).append(",");
      ((StringBuilder)localObject).append(paramParameters[1] / 1000.0D);
      ((StringBuilder)localObject).append("]");
      Log.v("CameraSource", ((StringBuilder)localObject).toString());
    }
    return paramParameters;
  }
  
  private int[] findClosestSize(int paramInt1, int paramInt2, Camera.Parameters paramParameters)
  {
    paramParameters = paramParameters.getSupportedPreviewSizes();
    int i = -1;
    int j = -1;
    int k = get0width;
    int m = get0height;
    Iterator localIterator = paramParameters.iterator();
    while (localIterator.hasNext())
    {
      paramParameters = (Camera.Size)localIterator.next();
      int n = i;
      i1 = j;
      if (width <= paramInt1)
      {
        n = i;
        i1 = j;
        if (height <= paramInt2)
        {
          n = i;
          i1 = j;
          if (width >= i)
          {
            n = i;
            i1 = j;
            if (height >= j)
            {
              n = width;
              i1 = height;
            }
          }
        }
      }
      int i2 = k;
      int i3 = m;
      if (width < k)
      {
        i2 = k;
        i3 = m;
        if (height < m)
        {
          i2 = width;
          i3 = height;
        }
      }
      i = n;
      j = i1;
      k = i2;
      m = i3;
    }
    int i1 = i;
    if (i == -1)
    {
      j = m;
      i1 = k;
    }
    if (mLogVerbose)
    {
      paramParameters = new StringBuilder();
      paramParameters.append("Requested resolution: (");
      paramParameters.append(paramInt1);
      paramParameters.append(", ");
      paramParameters.append(paramInt2);
      paramParameters.append("). Closest match: (");
      paramParameters.append(i1);
      paramParameters.append(", ");
      paramParameters.append(j);
      paramParameters.append(").");
      Log.v("CameraSource", paramParameters.toString());
    }
    return new int[] { i1, j };
  }
  
  public void close(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("CameraSource", "Closing");
    }
    mCamera.release();
    mCamera = null;
    mSurfaceTexture.release();
    mSurfaceTexture = null;
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (paramString.equals("framerate"))
    {
      getCameraParameters();
      paramString = findClosestFpsRange(mFps, mCameraParameters);
      mCameraParameters.setPreviewFpsRange(paramString[0], paramString[1]);
      mCamera.setParameters(mCameraParameters);
    }
  }
  
  public Camera.Parameters getCameraParameters()
  {
    int i = 0;
    try
    {
      if (mCameraParameters == null)
      {
        if (mCamera == null)
        {
          mCamera = Camera.open(mCameraId);
          i = 1;
        }
        mCameraParameters = mCamera.getParameters();
        if (i != 0)
        {
          mCamera.release();
          mCamera = null;
        }
      }
      Object localObject1 = findClosestSize(mWidth, mHeight, mCameraParameters);
      mWidth = localObject1[0];
      mHeight = localObject1[1];
      mCameraParameters.setPreviewSize(mWidth, mHeight);
      localObject1 = findClosestFpsRange(mFps, mCameraParameters);
      mCameraParameters.setPreviewFpsRange(localObject1[0], localObject1[1]);
      localObject1 = mCameraParameters;
      return localObject1;
    }
    finally {}
  }
  
  public void open(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("CameraSource", "Opening");
    }
    mCamera = Camera.open(mCameraId);
    getCameraParameters();
    mCamera.setParameters(mCameraParameters);
    createFormats();
    mCameraFrame = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(mOutputFormat, 104, 0L));
    mSurfaceTexture = new SurfaceTexture(mCameraFrame.getTextureId());
    try
    {
      mCamera.setPreviewTexture(mSurfaceTexture);
      mSurfaceTexture.setOnFrameAvailableListener(onCameraFrameAvailableListener);
      mNewFrameAvailable = false;
      mCamera.startPreview();
      return;
    }
    catch (IOException paramFilterContext)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not bind camera surface texture: ");
      localStringBuilder.append(paramFilterContext.getMessage());
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("CameraSource", "Preparing");
    }
    mFrameExtractor = new ShaderProgram(paramFilterContext, "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n");
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("CameraSource", "Processing new frame");
    }
    if (mWaitForNewFrame)
    {
      int i = 0;
      while (!mNewFrameAvailable) {
        if (i != 10) {
          try
          {
            wait(100L);
          }
          catch (InterruptedException localInterruptedException)
          {
            for (;;)
            {
              if (mLogVerbose) {
                Log.v("CameraSource", "Interrupted while waiting for new frame");
              }
            }
          }
        } else {
          throw new RuntimeException("Timeout waiting for new frame");
        }
      }
      mNewFrameAvailable = false;
      if (mLogVerbose) {
        Log.v("CameraSource", "Got new frame");
      }
    }
    mSurfaceTexture.updateTexImage();
    if (mLogVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Using frame extractor in thread: ");
      ((StringBuilder)localObject).append(Thread.currentThread());
      Log.v("CameraSource", ((StringBuilder)localObject).toString());
    }
    mSurfaceTexture.getTransformMatrix(mCameraTransform);
    Matrix.multiplyMM(mMappedCoords, 0, mCameraTransform, 0, mSourceCoords, 0);
    mFrameExtractor.setSourceRegion(mMappedCoords[0], mMappedCoords[1], mMappedCoords[4], mMappedCoords[5], mMappedCoords[8], mMappedCoords[9], mMappedCoords[12], mMappedCoords[13]);
    Object localObject = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
    mFrameExtractor.process(mCameraFrame, (Frame)localObject);
    long l = mSurfaceTexture.getTimestamp();
    if (mLogVerbose)
    {
      paramFilterContext = new StringBuilder();
      paramFilterContext.append("Timestamp: ");
      paramFilterContext.append(l / 1.0E9D);
      paramFilterContext.append(" s");
      Log.v("CameraSource", paramFilterContext.toString());
    }
    ((Frame)localObject).setTimestamp(l);
    pushOutput("video", (Frame)localObject);
    ((Frame)localObject).release();
    if (mLogVerbose) {
      Log.v("CameraSource", "Done processing new frame");
    }
  }
  
  public void setCameraParameters(Camera.Parameters paramParameters)
  {
    try
    {
      paramParameters.setPreviewSize(mWidth, mHeight);
      mCameraParameters = paramParameters;
      if (isOpen()) {
        mCamera.setParameters(mCameraParameters);
      }
      return;
    }
    finally
    {
      paramParameters = finally;
      throw paramParameters;
    }
  }
  
  public void setupPorts()
  {
    addOutputPort("video", ImageFormat.create(3, 3));
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mCameraFrame != null) {
      mCameraFrame.release();
    }
  }
}
