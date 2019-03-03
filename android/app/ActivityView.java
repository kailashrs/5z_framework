package android.app;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.input.InputManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import dalvik.system.CloseGuard;
import java.util.List;

public class ActivityView
  extends ViewGroup
{
  private static final String DISPLAY_NAME = "ActivityViewVirtualDisplay";
  private static final String TAG = "ActivityView";
  private IActivityManager mActivityManager = ActivityManager.getService();
  private StateCallback mActivityViewCallback;
  private final CloseGuard mGuard = CloseGuard.get();
  private IInputForwarder mInputForwarder;
  private final int[] mLocationOnScreen = new int[2];
  private boolean mOpened;
  private Surface mSurface;
  private final SurfaceCallback mSurfaceCallback;
  private final SurfaceView mSurfaceView;
  private TaskStackListener mTaskStackListener;
  private VirtualDisplay mVirtualDisplay;
  
  public ActivityView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActivityView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ActivityView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    mSurfaceView = new SurfaceView(paramContext);
    mSurfaceCallback = new SurfaceCallback(null);
    mSurfaceView.getHolder().addCallback(mSurfaceCallback);
    addView(mSurfaceView);
    mOpened = true;
    mGuard.open("release");
  }
  
  private void cleanTapExcludeRegion()
  {
    try
    {
      WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), 0, 0, 0, 0);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  private int getBaseDisplayDensity()
  {
    WindowManager localWindowManager = (WindowManager)mContext.getSystemService(WindowManager.class);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    return densityDpi;
  }
  
  private void initVirtualDisplay()
  {
    if (mVirtualDisplay == null)
    {
      int i = mSurfaceView.getWidth();
      int j = mSurfaceView.getHeight();
      DisplayManager localDisplayManager = (DisplayManager)mContext.getSystemService(DisplayManager.class);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ActivityViewVirtualDisplay@");
      ((StringBuilder)localObject).append(System.identityHashCode(this));
      mVirtualDisplay = localDisplayManager.createVirtualDisplay(((StringBuilder)localObject).toString(), i, j, getBaseDisplayDensity(), mSurface, 9);
      if (mVirtualDisplay == null)
      {
        Log.e("ActivityView", "Failed to initialize ActivityView");
        return;
      }
      i = mVirtualDisplay.getDisplay().getDisplayId();
      localObject = WindowManagerGlobal.getWindowManagerService();
      try
      {
        ((IWindowManager)localObject).dontOverrideDisplayInfo(i);
      }
      catch (RemoteException localRemoteException1)
      {
        localRemoteException1.rethrowAsRuntimeException();
      }
      mInputForwarder = InputManager.getInstance().createInputForwarder(i);
      mTaskStackListener = new TaskStackListenerImpl(null);
      try
      {
        mActivityManager.registerTaskStackListener(mTaskStackListener);
      }
      catch (RemoteException localRemoteException2)
      {
        Log.e("ActivityView", "Failed to register task stack listener", localRemoteException2);
      }
      return;
    }
    throw new IllegalStateException("Trying to initialize for the second time.");
  }
  
  private boolean injectInputEvent(InputEvent paramInputEvent)
  {
    if (mInputForwarder != null) {
      try
      {
        boolean bool = mInputForwarder.forwardEvent(paramInputEvent);
        return bool;
      }
      catch (RemoteException paramInputEvent)
      {
        paramInputEvent.rethrowAsRuntimeException();
      }
    }
    return false;
  }
  
  private void performRelease()
  {
    if (!mOpened) {
      return;
    }
    mSurfaceView.getHolder().removeCallback(mSurfaceCallback);
    if (mInputForwarder != null) {
      mInputForwarder = null;
    }
    cleanTapExcludeRegion();
    if (mTaskStackListener != null)
    {
      try
      {
        mActivityManager.unregisterTaskStackListener(mTaskStackListener);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("ActivityView", "Failed to unregister task stack listener", localRemoteException);
      }
      mTaskStackListener = null;
    }
    int i;
    if (mVirtualDisplay != null)
    {
      mVirtualDisplay.release();
      mVirtualDisplay = null;
      i = 1;
    }
    else
    {
      i = 0;
    }
    if (mSurface != null)
    {
      mSurface.release();
      mSurface = null;
    }
    if ((i != 0) && (mActivityViewCallback != null)) {
      mActivityViewCallback.onActivityViewDestroyed(this);
    }
    mGuard.close();
    mOpened = false;
  }
  
  private ActivityOptions prepareActivityOptions()
  {
    if (mVirtualDisplay != null)
    {
      ActivityOptions localActivityOptions = ActivityOptions.makeBasic();
      localActivityOptions.setLaunchDisplayId(mVirtualDisplay.getDisplay().getDisplayId());
      return localActivityOptions;
    }
    throw new IllegalStateException("Trying to start activity before ActivityView is ready.");
  }
  
  private void updateLocation()
  {
    try
    {
      getLocationOnScreen(mLocationOnScreen);
      WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), mLocationOnScreen[0], mLocationOnScreen[1], getWidth(), getHeight());
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGuard != null)
      {
        mGuard.warnIfOpen();
        performRelease();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.isFromSource(2)) && (injectInputEvent(paramMotionEvent))) {
      return true;
    }
    return super.onGenericMotionEvent(paramMotionEvent);
  }
  
  public void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mSurfaceView.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
  }
  
  public void onLocationChanged()
  {
    updateLocation();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if ((!injectInputEvent(paramMotionEvent)) && (!super.onTouchEvent(paramMotionEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void release()
  {
    if (mVirtualDisplay != null)
    {
      performRelease();
      return;
    }
    throw new IllegalStateException("Trying to release container that is not initialized.");
  }
  
  public void setCallback(StateCallback paramStateCallback)
  {
    mActivityViewCallback = paramStateCallback;
    if ((mVirtualDisplay != null) && (mActivityViewCallback != null)) {
      mActivityViewCallback.onActivityViewReady(this);
    }
  }
  
  public void startActivity(PendingIntent paramPendingIntent)
  {
    ActivityOptions localActivityOptions = prepareActivityOptions();
    try
    {
      paramPendingIntent.send(null, 0, null, null, null, null, localActivityOptions.toBundle());
      return;
    }
    catch (PendingIntent.CanceledException paramPendingIntent)
    {
      throw new RuntimeException(paramPendingIntent);
    }
  }
  
  public void startActivity(Intent paramIntent)
  {
    ActivityOptions localActivityOptions = prepareActivityOptions();
    getContext().startActivity(paramIntent, localActivityOptions.toBundle());
  }
  
  public void startActivity(Intent paramIntent, UserHandle paramUserHandle)
  {
    ActivityOptions localActivityOptions = prepareActivityOptions();
    getContext().startActivityAsUser(paramIntent, localActivityOptions.toBundle(), paramUserHandle);
  }
  
  public static abstract class StateCallback
  {
    public StateCallback() {}
    
    public abstract void onActivityViewDestroyed(ActivityView paramActivityView);
    
    public abstract void onActivityViewReady(ActivityView paramActivityView);
    
    public void onTaskMovedToFront(ActivityManager.StackInfo paramStackInfo) {}
  }
  
  private class SurfaceCallback
    implements SurfaceHolder.Callback
  {
    private SurfaceCallback() {}
    
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
    {
      if (mVirtualDisplay != null) {
        mVirtualDisplay.resize(paramInt2, paramInt3, ActivityView.this.getBaseDisplayDensity());
      }
      ActivityView.this.updateLocation();
    }
    
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
    {
      ActivityView.access$102(ActivityView.this, mSurfaceView.getHolder().getSurface());
      if (mVirtualDisplay == null)
      {
        ActivityView.this.initVirtualDisplay();
        if ((mVirtualDisplay != null) && (mActivityViewCallback != null)) {
          mActivityViewCallback.onActivityViewReady(ActivityView.this);
        }
      }
      else
      {
        mVirtualDisplay.setSurface(paramSurfaceHolder.getSurface());
      }
      ActivityView.this.updateLocation();
    }
    
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
    {
      mSurface.release();
      ActivityView.access$102(ActivityView.this, null);
      if (mVirtualDisplay != null) {
        mVirtualDisplay.setSurface(null);
      }
      ActivityView.this.cleanTapExcludeRegion();
    }
  }
  
  private class TaskStackListenerImpl
    extends TaskStackListener
  {
    private TaskStackListenerImpl() {}
    
    private ActivityManager.StackInfo getTopMostStackInfo()
      throws RemoteException
    {
      int i = mVirtualDisplay.getDisplay().getDisplayId();
      List localList = mActivityManager.getAllStackInfos();
      int j = localList.size();
      int k = 0;
      while (k < j)
      {
        ActivityManager.StackInfo localStackInfo = (ActivityManager.StackInfo)localList.get(k);
        if (displayId != i) {
          k++;
        } else {
          return localStackInfo;
        }
      }
      return null;
    }
    
    public void onTaskDescriptionChanged(int paramInt, ActivityManager.TaskDescription paramTaskDescription)
      throws RemoteException
    {
      if (mVirtualDisplay == null) {
        return;
      }
      ActivityManager.StackInfo localStackInfo = getTopMostStackInfo();
      if (localStackInfo == null) {
        return;
      }
      if (paramInt == taskIds[(taskIds.length - 1)]) {
        mSurfaceView.setResizeBackgroundColor(paramTaskDescription.getBackgroundColor());
      }
    }
    
    public void onTaskMovedToFront(int paramInt)
      throws RemoteException
    {
      if (mActivityViewCallback != null)
      {
        ActivityManager.StackInfo localStackInfo = getTopMostStackInfo();
        if ((localStackInfo != null) && (paramInt == taskIds[(taskIds.length - 1)])) {
          mActivityViewCallback.onTaskMovedToFront(localStackInfo);
        }
      }
    }
  }
}
