package android.hardware.display;

import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.DisplayInfo;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DisplayManagerGlobal
{
  private static final boolean DEBUG = false;
  public static final int EVENT_DISPLAY_ADDED = 1;
  public static final int EVENT_DISPLAY_CHANGED = 2;
  public static final int EVENT_DISPLAY_REMOVED = 3;
  private static final String TAG = "DisplayManager";
  private static final boolean USE_CACHE = false;
  private static DisplayManagerGlobal sInstance;
  private DisplayManagerCallback mCallback;
  private int[] mDisplayIdCache;
  private final SparseArray<DisplayInfo> mDisplayInfoCache = new SparseArray();
  private final ArrayList<DisplayListenerDelegate> mDisplayListeners = new ArrayList();
  private final IDisplayManager mDm;
  private final Object mLock = new Object();
  private int mWifiDisplayScanNestCount;
  
  private DisplayManagerGlobal(IDisplayManager paramIDisplayManager)
  {
    mDm = paramIDisplayManager;
  }
  
  private int findDisplayListenerLocked(DisplayManager.DisplayListener paramDisplayListener)
  {
    int i = mDisplayListeners.size();
    for (int j = 0; j < i; j++) {
      if (mDisplayListeners.get(j)).mListener == paramDisplayListener) {
        return j;
      }
    }
    return -1;
  }
  
  public static DisplayManagerGlobal getInstance()
  {
    try
    {
      if (sInstance == null)
      {
        IBinder localIBinder = ServiceManager.getService("display");
        if (localIBinder != null)
        {
          localDisplayManagerGlobal = new android/hardware/display/DisplayManagerGlobal;
          localDisplayManagerGlobal.<init>(IDisplayManager.Stub.asInterface(localIBinder));
          sInstance = localDisplayManagerGlobal;
        }
      }
      DisplayManagerGlobal localDisplayManagerGlobal = sInstance;
      return localDisplayManagerGlobal;
    }
    finally {}
  }
  
  private void handleDisplayEvent(int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      int i = mDisplayListeners.size();
      for (int j = 0; j < i; j++) {
        ((DisplayListenerDelegate)mDisplayListeners.get(j)).sendDisplayEvent(paramInt1, paramInt2);
      }
      return;
    }
  }
  
  private void registerCallbackIfNeededLocked()
  {
    if (mCallback == null)
    {
      mCallback = new DisplayManagerCallback(null);
      try
      {
        mDm.registerCallback(mCallback);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void connectWifiDisplay(String paramString)
  {
    if (paramString != null) {
      try
      {
        mDm.connectWifiDisplay(paramString);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("deviceAddress must not be null");
  }
  
  public VirtualDisplay createVirtualDisplay(Context paramContext, MediaProjection paramMediaProjection, String paramString1, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4, VirtualDisplay.Callback paramCallback, Handler paramHandler, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if ((paramInt1 > 0) && (paramInt2 > 0) && (paramInt3 > 0))
      {
        paramCallback = new VirtualDisplayCallback(paramCallback, paramHandler);
        if (paramMediaProjection != null) {
          paramMediaProjection = paramMediaProjection.getProjection();
        } else {
          paramMediaProjection = null;
        }
        try
        {
          paramHandler = mDm;
          paramContext = paramContext.getPackageName();
          try
          {
            paramInt1 = paramHandler.createVirtualDisplay(paramCallback, paramMediaProjection, paramContext, paramString1, paramInt1, paramInt2, paramInt3, paramSurface, paramInt4, paramString2);
            if (paramInt1 < 0)
            {
              paramContext = new StringBuilder();
              paramContext.append("Could not create virtual display: ");
              paramContext.append(paramString1);
              Log.e("DisplayManager", paramContext.toString());
              return null;
            }
            paramContext = getRealDisplay(paramInt1);
            if (paramContext == null)
            {
              paramContext = new StringBuilder();
              paramContext.append("Could not obtain display info for newly created virtual display: ");
              paramContext.append(paramString1);
              Log.wtf("DisplayManager", paramContext.toString());
              try
              {
                mDm.releaseVirtualDisplay(paramCallback);
                return null;
              }
              catch (RemoteException paramContext)
              {
                throw paramContext.rethrowFromSystemServer();
              }
            }
            return new VirtualDisplay(this, paramContext, paramCallback, paramSurface);
          }
          catch (RemoteException paramContext) {}
          throw paramContext.rethrowFromSystemServer();
        }
        catch (RemoteException paramContext) {}
      }
      throw new IllegalArgumentException("width, height, and densityDpi must be greater than 0");
    }
    throw new IllegalArgumentException("name must be non-null and non-empty");
  }
  
  public void disconnectWifiDisplay()
  {
    try
    {
      mDm.disconnectWifiDisplay();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void forgetWifiDisplay(String paramString)
  {
    if (paramString != null) {
      try
      {
        mDm.forgetWifiDisplay(paramString);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("deviceAddress must not be null");
  }
  
  public List<AmbientBrightnessDayStats> getAmbientBrightnessStats()
  {
    try
    {
      Object localObject = mDm.getAmbientBrightnessStats();
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public BrightnessConfiguration getBrightnessConfigurationForUser(int paramInt)
  {
    try
    {
      BrightnessConfiguration localBrightnessConfiguration = mDm.getBrightnessConfigurationForUser(paramInt);
      return localBrightnessConfiguration;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<BrightnessChangeEvent> getBrightnessEvents(String paramString)
  {
    try
    {
      paramString = mDm.getBrightnessEvents(paramString);
      if (paramString == null) {
        return Collections.emptyList();
      }
      paramString = paramString.getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Display getCompatibleDisplay(int paramInt, Resources paramResources)
  {
    DisplayInfo localDisplayInfo = getDisplayInfo(paramInt);
    if (localDisplayInfo == null) {
      return null;
    }
    return new Display(this, paramInt, localDisplayInfo, paramResources);
  }
  
  public Display getCompatibleDisplay(int paramInt, DisplayAdjustments paramDisplayAdjustments)
  {
    DisplayInfo localDisplayInfo = getDisplayInfo(paramInt);
    if (localDisplayInfo == null) {
      return null;
    }
    return new Display(this, paramInt, localDisplayInfo, paramDisplayAdjustments);
  }
  
  public BrightnessConfiguration getDefaultBrightnessConfiguration()
  {
    try
    {
      BrightnessConfiguration localBrightnessConfiguration = mDm.getDefaultBrightnessConfiguration();
      return localBrightnessConfiguration;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public int[] getDisplayIds()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 57	android/hardware/display/DisplayManagerGlobal:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 69	android/hardware/display/DisplayManagerGlobal:mDm	Landroid/hardware/display/IDisplayManager;
    //   11: invokeinterface 263 1 0
    //   16: astore_2
    //   17: aload_0
    //   18: invokespecial 265	android/hardware/display/DisplayManagerGlobal:registerCallbackIfNeededLocked	()V
    //   21: aload_1
    //   22: monitorexit
    //   23: aload_2
    //   24: areturn
    //   25: astore_2
    //   26: aload_1
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    //   30: astore_1
    //   31: aload_1
    //   32: invokevirtual 131	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	DisplayManagerGlobal
    //   30	2	1	localRemoteException	RemoteException
    //   16	8	2	arrayOfInt	int[]
    //   25	4	2	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   7	23	25	finally
    //   26	28	25	finally
    //   0	7	30	android/os/RemoteException
    //   28	30	30	android/os/RemoteException
  }
  
  /* Error */
  public DisplayInfo getDisplayInfo(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 57	android/hardware/display/DisplayManagerGlobal:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 69	android/hardware/display/DisplayManagerGlobal:mDm	Landroid/hardware/display/IDisplayManager;
    //   11: iload_1
    //   12: invokeinterface 266 2 0
    //   17: astore_3
    //   18: aload_3
    //   19: ifnonnull +7 -> 26
    //   22: aload_2
    //   23: monitorexit
    //   24: aconst_null
    //   25: areturn
    //   26: aload_0
    //   27: invokespecial 265	android/hardware/display/DisplayManagerGlobal:registerCallbackIfNeededLocked	()V
    //   30: aload_2
    //   31: monitorexit
    //   32: aload_3
    //   33: areturn
    //   34: astore_3
    //   35: aload_2
    //   36: monitorexit
    //   37: aload_3
    //   38: athrow
    //   39: astore_2
    //   40: aload_2
    //   41: invokevirtual 131	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	DisplayManagerGlobal
    //   0	45	1	paramInt	int
    //   39	2	2	localRemoteException	RemoteException
    //   17	16	3	localDisplayInfo	DisplayInfo
    //   34	4	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	34	finally
    //   22	24	34	finally
    //   26	32	34	finally
    //   35	37	34	finally
    //   0	7	39	android/os/RemoteException
    //   37	39	39	android/os/RemoteException
  }
  
  public Pair<float[], float[]> getMinimumBrightnessCurve()
  {
    try
    {
      Object localObject = mDm.getMinimumBrightnessCurve();
      localObject = Pair.create(((Curve)localObject).getX(), ((Curve)localObject).getY());
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Display getRealDisplay(int paramInt)
  {
    return getCompatibleDisplay(paramInt, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
  }
  
  public Point getStableDisplaySize()
  {
    try
    {
      Point localPoint = mDm.getStableDisplaySize();
      return localPoint;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public WifiDisplayStatus getWifiDisplayStatus()
  {
    try
    {
      WifiDisplayStatus localWifiDisplayStatus = mDm.getWifiDisplayStatus();
      return localWifiDisplayStatus;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void pauseWifiDisplay()
  {
    try
    {
      mDm.pauseWifiDisplay();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerDisplayListener(DisplayManager.DisplayListener paramDisplayListener, Handler paramHandler)
  {
    if (paramDisplayListener != null) {
      synchronized (mLock)
      {
        if (findDisplayListenerLocked(paramDisplayListener) < 0)
        {
          ArrayList localArrayList = mDisplayListeners;
          DisplayListenerDelegate localDisplayListenerDelegate = new android/hardware/display/DisplayManagerGlobal$DisplayListenerDelegate;
          localDisplayListenerDelegate.<init>(paramDisplayListener, paramHandler);
          localArrayList.add(localDisplayListenerDelegate);
          registerCallbackIfNeededLocked();
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public void releaseVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback)
  {
    try
    {
      mDm.releaseVirtualDisplay(paramIVirtualDisplayCallback);
      return;
    }
    catch (RemoteException paramIVirtualDisplayCallback)
    {
      throw paramIVirtualDisplayCallback.rethrowFromSystemServer();
    }
  }
  
  public void renameWifiDisplay(String paramString1, String paramString2)
  {
    if (paramString1 != null) {
      try
      {
        mDm.renameWifiDisplay(paramString1, paramString2);
        return;
      }
      catch (RemoteException paramString1)
      {
        throw paramString1.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("deviceAddress must not be null");
  }
  
  public void requestColorMode(int paramInt1, int paramInt2)
  {
    try
    {
      mDm.requestColorMode(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void resizeVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mDm.resizeVirtualDisplay(paramIVirtualDisplayCallback, paramInt1, paramInt2, paramInt3);
      return;
    }
    catch (RemoteException paramIVirtualDisplayCallback)
    {
      throw paramIVirtualDisplayCallback.rethrowFromSystemServer();
    }
  }
  
  public void resumeWifiDisplay()
  {
    try
    {
      mDm.resumeWifiDisplay();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setBrightnessConfigurationForUser(BrightnessConfiguration paramBrightnessConfiguration, int paramInt, String paramString)
  {
    try
    {
      mDm.setBrightnessConfigurationForUser(paramBrightnessConfiguration, paramInt, paramString);
      return;
    }
    catch (RemoteException paramBrightnessConfiguration)
    {
      throw paramBrightnessConfiguration.rethrowFromSystemServer();
    }
  }
  
  public void setSaturationLevel(float paramFloat)
  {
    try
    {
      mDm.setSaturationLevel(paramFloat);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setTemporaryAutoBrightnessAdjustment(float paramFloat)
  {
    try
    {
      mDm.setTemporaryAutoBrightnessAdjustment(paramFloat);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setTemporaryBrightness(int paramInt)
  {
    try
    {
      mDm.setTemporaryBrightness(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setVirtualDisplaySurface(IVirtualDisplayCallback paramIVirtualDisplayCallback, Surface paramSurface)
  {
    try
    {
      mDm.setVirtualDisplaySurface(paramIVirtualDisplayCallback, paramSurface);
      return;
    }
    catch (RemoteException paramIVirtualDisplayCallback)
    {
      throw paramIVirtualDisplayCallback.rethrowFromSystemServer();
    }
  }
  
  public void startWifiDisplayScan()
  {
    synchronized (mLock)
    {
      int i = mWifiDisplayScanNestCount;
      mWifiDisplayScanNestCount = (i + 1);
      if (i == 0)
      {
        registerCallbackIfNeededLocked();
        try
        {
          mDm.startWifiDisplayScan();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  public void stopWifiDisplayScan()
  {
    synchronized (mLock)
    {
      int i = mWifiDisplayScanNestCount - 1;
      mWifiDisplayScanNestCount = i;
      if (i == 0)
      {
        try
        {
          mDm.stopWifiDisplayScan();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      else if (mWifiDisplayScanNestCount < 0)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Wifi display scan nest count became negative: ");
        localStringBuilder.append(mWifiDisplayScanNestCount);
        Log.wtf("DisplayManager", localStringBuilder.toString());
        mWifiDisplayScanNestCount = 0;
      }
      return;
    }
  }
  
  public void unregisterDisplayListener(DisplayManager.DisplayListener paramDisplayListener)
  {
    if (paramDisplayListener != null) {
      synchronized (mLock)
      {
        int i = findDisplayListenerLocked(paramDisplayListener);
        if (i >= 0)
        {
          ((DisplayListenerDelegate)mDisplayListeners.get(i)).clearEvents();
          mDisplayListeners.remove(i);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  private static final class DisplayListenerDelegate
    extends Handler
  {
    public final DisplayManager.DisplayListener mListener;
    
    public DisplayListenerDelegate(DisplayManager.DisplayListener paramDisplayListener, Handler paramHandler)
    {
      super(null, true);
      mListener = paramDisplayListener;
    }
    
    public void clearEvents()
    {
      removeCallbacksAndMessages(null);
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        mListener.onDisplayRemoved(arg1);
        break;
      case 2: 
        mListener.onDisplayChanged(arg1);
        break;
      case 1: 
        mListener.onDisplayAdded(arg1);
      }
    }
    
    public void sendDisplayEvent(int paramInt1, int paramInt2)
    {
      sendMessage(obtainMessage(paramInt2, paramInt1, 0));
    }
  }
  
  private final class DisplayManagerCallback
    extends IDisplayManagerCallback.Stub
  {
    private DisplayManagerCallback() {}
    
    public void onDisplayEvent(int paramInt1, int paramInt2)
    {
      DisplayManagerGlobal.this.handleDisplayEvent(paramInt1, paramInt2);
    }
  }
  
  private static final class VirtualDisplayCallback
    extends IVirtualDisplayCallback.Stub
  {
    private DisplayManagerGlobal.VirtualDisplayCallbackDelegate mDelegate;
    
    public VirtualDisplayCallback(VirtualDisplay.Callback paramCallback, Handler paramHandler)
    {
      if (paramCallback != null) {
        mDelegate = new DisplayManagerGlobal.VirtualDisplayCallbackDelegate(paramCallback, paramHandler);
      }
    }
    
    public void onPaused()
    {
      if (mDelegate != null) {
        mDelegate.sendEmptyMessage(0);
      }
    }
    
    public void onResumed()
    {
      if (mDelegate != null) {
        mDelegate.sendEmptyMessage(1);
      }
    }
    
    public void onStopped()
    {
      if (mDelegate != null) {
        mDelegate.sendEmptyMessage(2);
      }
    }
  }
  
  private static final class VirtualDisplayCallbackDelegate
    extends Handler
  {
    public static final int MSG_DISPLAY_PAUSED = 0;
    public static final int MSG_DISPLAY_RESUMED = 1;
    public static final int MSG_DISPLAY_STOPPED = 2;
    private final VirtualDisplay.Callback mCallback;
    
    public VirtualDisplayCallbackDelegate(VirtualDisplay.Callback paramCallback, Handler paramHandler)
    {
      super(null, true);
      mCallback = paramCallback;
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 2: 
        mCallback.onStopped();
        break;
      case 1: 
        mCallback.onResumed();
        break;
      case 0: 
        mCallback.onPaused();
      }
    }
  }
}
