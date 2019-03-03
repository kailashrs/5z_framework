package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Collection;

public class GLThreadManager
{
  private static final boolean DEBUG = false;
  private static final int MSG_ALLOW_FRAMES = 5;
  private static final int MSG_CLEANUP = 3;
  private static final int MSG_DROP_FRAMES = 4;
  private static final int MSG_NEW_CONFIGURATION = 1;
  private static final int MSG_NEW_FRAME = 2;
  private final String TAG;
  private CaptureCollector mCaptureCollector;
  private final CameraDeviceState mDeviceState;
  private final Handler.Callback mGLHandlerCb = new Handler.Callback()
  {
    private boolean mCleanup = false;
    private boolean mConfigured = false;
    private boolean mDroppingFrames = false;
    
    public boolean handleMessage(Message paramAnonymousMessage)
    {
      if (mCleanup) {
        return true;
      }
      try
      {
        int i = what;
        if (i != -1) {
          switch (i)
          {
          default: 
            String str = TAG;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Unhandled message ");
            localStringBuilder.append(what);
            localStringBuilder.append(" on GLThread.");
            Log.e(str, localStringBuilder.toString());
            break;
          case 5: 
            mDroppingFrames = false;
            break;
          case 4: 
            mDroppingFrames = true;
            break;
          case 3: 
            mTextureRenderer.cleanupEGLContext();
            mCleanup = true;
            mConfigured = false;
            break;
          case 2: 
            if (mDroppingFrames)
            {
              Log.w(TAG, "Ignoring frame.");
            }
            else
            {
              if (!mConfigured) {
                Log.e(TAG, "Dropping frame, EGL context not configured!");
              }
              mTextureRenderer.drawIntoSurfaces(mCaptureCollector);
            }
            break;
          case 1: 
            paramAnonymousMessage = (GLThreadManager.ConfigureHolder)obj;
            mTextureRenderer.cleanupEGLContext();
            mTextureRenderer.configureSurfaces(surfaces);
            GLThreadManager.access$102(GLThreadManager.this, (CaptureCollector)Preconditions.checkNotNull(collector));
            condition.open();
            mConfigured = true;
          }
        }
      }
      catch (Exception paramAnonymousMessage)
      {
        Log.e(TAG, "Received exception on GL render thread: ", paramAnonymousMessage);
        mDeviceState.setError(1);
      }
      return true;
    }
  };
  private final RequestHandlerThread mGLHandlerThread;
  private final RequestThreadManager.FpsCounter mPrevCounter = new RequestThreadManager.FpsCounter("GL Preview Producer");
  private final SurfaceTextureRenderer mTextureRenderer;
  
  public GLThreadManager(int paramInt1, int paramInt2, CameraDeviceState paramCameraDeviceState)
  {
    mTextureRenderer = new SurfaceTextureRenderer(paramInt2);
    TAG = String.format("CameraDeviceGLThread-%d", new Object[] { Integer.valueOf(paramInt1) });
    mGLHandlerThread = new RequestHandlerThread(TAG, mGLHandlerCb);
    mDeviceState = paramCameraDeviceState;
  }
  
  public void allowNewFrames()
  {
    mGLHandlerThread.getHandler().sendEmptyMessage(5);
  }
  
  public SurfaceTexture getCurrentSurfaceTexture()
  {
    return mTextureRenderer.getSurfaceTexture();
  }
  
  public void ignoreNewFrames()
  {
    mGLHandlerThread.getHandler().sendEmptyMessage(4);
  }
  
  public void queueNewFrame()
  {
    Handler localHandler = mGLHandlerThread.getHandler();
    if (!localHandler.hasMessages(2)) {
      localHandler.sendMessage(localHandler.obtainMessage(2));
    } else {
      Log.e(TAG, "GLThread dropping frame.  Not consuming frames quickly enough!");
    }
  }
  
  public void quit()
  {
    Handler localHandler = mGLHandlerThread.getHandler();
    localHandler.sendMessageAtFrontOfQueue(localHandler.obtainMessage(3));
    mGLHandlerThread.quitSafely();
    try
    {
      mGLHandlerThread.join();
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.e(TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[] { mGLHandlerThread.getName(), Long.valueOf(mGLHandlerThread.getId()) }));
    }
  }
  
  public void setConfigurationAndWait(Collection<Pair<Surface, Size>> paramCollection, CaptureCollector paramCaptureCollector)
  {
    Preconditions.checkNotNull(paramCaptureCollector, "collector must not be null");
    Handler localHandler = mGLHandlerThread.getHandler();
    ConditionVariable localConditionVariable = new ConditionVariable(false);
    localHandler.sendMessage(localHandler.obtainMessage(1, 0, 0, new ConfigureHolder(localConditionVariable, paramCollection, paramCaptureCollector)));
    localConditionVariable.block();
  }
  
  public void start()
  {
    mGLHandlerThread.start();
  }
  
  public void waitUntilIdle()
  {
    mGLHandlerThread.waitUntilIdle();
  }
  
  public void waitUntilStarted()
  {
    mGLHandlerThread.waitUntilStarted();
  }
  
  private static class ConfigureHolder
  {
    public final CaptureCollector collector;
    public final ConditionVariable condition;
    public final Collection<Pair<Surface, Size>> surfaces;
    
    public ConfigureHolder(ConditionVariable paramConditionVariable, Collection<Pair<Surface, Size>> paramCollection, CaptureCollector paramCaptureCollector)
    {
      condition = paramConditionVariable;
      surfaces = paramCollection;
      collector = paramCaptureCollector;
    }
  }
}
