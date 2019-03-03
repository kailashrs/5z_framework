package android.hardware.display;

import android.annotation.SystemApi;
import android.content.Context;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Display;
import android.view.Surface;
import java.util.ArrayList;
import java.util.List;

public final class DisplayManager
{
  public static final String ACTION_WIFI_DISPLAY_STATUS_CHANGED = "android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED";
  private static final boolean DEBUG = false;
  public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
  public static final String EXTRA_WIFI_DISPLAY_STATUS = "android.hardware.display.extra.WIFI_DISPLAY_STATUS";
  private static final String TAG = "DisplayManager";
  public static final int VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR = 16;
  public static final int VIRTUAL_DISPLAY_FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD = 32;
  public static final int VIRTUAL_DISPLAY_FLAG_DESTROY_CONTENT_ON_REMOVAL = 256;
  public static final int VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY = 8;
  public static final int VIRTUAL_DISPLAY_FLAG_PRESENTATION = 2;
  public static final int VIRTUAL_DISPLAY_FLAG_PUBLIC = 1;
  public static final int VIRTUAL_DISPLAY_FLAG_ROTATES_WITH_CONTENT = 128;
  public static final int VIRTUAL_DISPLAY_FLAG_SECURE = 4;
  public static final int VIRTUAL_DISPLAY_FLAG_SUPPORTS_TOUCH = 64;
  private final Context mContext;
  private final SparseArray<Display> mDisplays = new SparseArray();
  private final DisplayManagerGlobal mGlobal;
  private final Object mLock = new Object();
  private final ArrayList<Display> mTempDisplays = new ArrayList();
  
  public DisplayManager(Context paramContext)
  {
    mContext = paramContext;
    mGlobal = DisplayManagerGlobal.getInstance();
  }
  
  private void addAllDisplaysLocked(ArrayList<Display> paramArrayList, int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      Display localDisplay = getOrCreateDisplayLocked(paramArrayOfInt[i], true);
      if (localDisplay != null) {
        paramArrayList.add(localDisplay);
      }
    }
  }
  
  private void addPresentationDisplaysLocked(ArrayList<Display> paramArrayList, int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      Display localDisplay = getOrCreateDisplayLocked(paramArrayOfInt[i], true);
      if ((localDisplay != null) && ((localDisplay.getFlags() & 0x8) != 0) && (localDisplay.getType() == paramInt)) {
        paramArrayList.add(localDisplay);
      }
    }
  }
  
  private Display getOrCreateDisplayLocked(int paramInt, boolean paramBoolean)
  {
    Display localDisplay = (Display)mDisplays.get(paramInt);
    Object localObject;
    if (localDisplay == null)
    {
      if (mContext.getDisplay().getDisplayId() == paramInt) {
        localObject = mContext;
      } else {
        localObject = mContext.getApplicationContext();
      }
      localObject = mGlobal.getCompatibleDisplay(paramInt, ((Context)localObject).getResources());
      if (localObject != null) {
        mDisplays.put(paramInt, localObject);
      }
    }
    else
    {
      localObject = localDisplay;
      if (!paramBoolean)
      {
        localObject = localDisplay;
        if (!localDisplay.isValid()) {
          localObject = null;
        }
      }
    }
    return localObject;
  }
  
  public void connectWifiDisplay(String paramString)
  {
    mGlobal.connectWifiDisplay(paramString);
  }
  
  public VirtualDisplay createVirtualDisplay(MediaProjection paramMediaProjection, String paramString1, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4, VirtualDisplay.Callback paramCallback, Handler paramHandler, String paramString2)
  {
    return mGlobal.createVirtualDisplay(mContext, paramMediaProjection, paramString1, paramInt1, paramInt2, paramInt3, paramSurface, paramInt4, paramCallback, paramHandler, paramString2);
  }
  
  public VirtualDisplay createVirtualDisplay(String paramString, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4)
  {
    return createVirtualDisplay(paramString, paramInt1, paramInt2, paramInt3, paramSurface, paramInt4, null, null);
  }
  
  public VirtualDisplay createVirtualDisplay(String paramString, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4, VirtualDisplay.Callback paramCallback, Handler paramHandler)
  {
    return createVirtualDisplay(null, paramString, paramInt1, paramInt2, paramInt3, paramSurface, paramInt4, paramCallback, paramHandler, null);
  }
  
  public void disconnectWifiDisplay()
  {
    mGlobal.disconnectWifiDisplay();
  }
  
  public void forgetWifiDisplay(String paramString)
  {
    mGlobal.forgetWifiDisplay(paramString);
  }
  
  @SystemApi
  public List<AmbientBrightnessDayStats> getAmbientBrightnessStats()
  {
    return mGlobal.getAmbientBrightnessStats();
  }
  
  @SystemApi
  public BrightnessConfiguration getBrightnessConfiguration()
  {
    return getBrightnessConfigurationForUser(mContext.getUserId());
  }
  
  public BrightnessConfiguration getBrightnessConfigurationForUser(int paramInt)
  {
    return mGlobal.getBrightnessConfigurationForUser(paramInt);
  }
  
  @SystemApi
  public List<BrightnessChangeEvent> getBrightnessEvents()
  {
    return mGlobal.getBrightnessEvents(mContext.getOpPackageName());
  }
  
  @SystemApi
  public BrightnessConfiguration getDefaultBrightnessConfiguration()
  {
    return mGlobal.getDefaultBrightnessConfiguration();
  }
  
  public Display getDisplay(int paramInt)
  {
    synchronized (mLock)
    {
      Display localDisplay = getOrCreateDisplayLocked(paramInt, false);
      return localDisplay;
    }
  }
  
  public Display[] getDisplays()
  {
    return getDisplays(null);
  }
  
  public Display[] getDisplays(String paramString)
  {
    int[] arrayOfInt = mGlobal.getDisplayIds();
    localObject = mLock;
    if (paramString == null)
    {
      try
      {
        addAllDisplaysLocked(mTempDisplays, arrayOfInt);
      }
      finally
      {
        break label116;
      }
    }
    else if (paramString.equals("android.hardware.display.category.PRESENTATION"))
    {
      addPresentationDisplaysLocked(mTempDisplays, arrayOfInt, 3);
      addPresentationDisplaysLocked(mTempDisplays, arrayOfInt, 2);
      addPresentationDisplaysLocked(mTempDisplays, arrayOfInt, 4);
      addPresentationDisplaysLocked(mTempDisplays, arrayOfInt, 5);
    }
    paramString = (Display[])mTempDisplays.toArray(new Display[mTempDisplays.size()]);
    label116:
    try
    {
      mTempDisplays.clear();
      return paramString;
    }
    finally {}
    mTempDisplays.clear();
    throw paramString;
  }
  
  @SystemApi
  public Pair<float[], float[]> getMinimumBrightnessCurve()
  {
    return mGlobal.getMinimumBrightnessCurve();
  }
  
  @SystemApi
  public Point getStableDisplaySize()
  {
    return mGlobal.getStableDisplaySize();
  }
  
  public WifiDisplayStatus getWifiDisplayStatus()
  {
    return mGlobal.getWifiDisplayStatus();
  }
  
  public void pauseWifiDisplay()
  {
    mGlobal.pauseWifiDisplay();
  }
  
  public void registerDisplayListener(DisplayListener paramDisplayListener, Handler paramHandler)
  {
    mGlobal.registerDisplayListener(paramDisplayListener, paramHandler);
  }
  
  public void renameWifiDisplay(String paramString1, String paramString2)
  {
    mGlobal.renameWifiDisplay(paramString1, paramString2);
  }
  
  public void resumeWifiDisplay()
  {
    mGlobal.resumeWifiDisplay();
  }
  
  @SystemApi
  public void setBrightnessConfiguration(BrightnessConfiguration paramBrightnessConfiguration)
  {
    setBrightnessConfigurationForUser(paramBrightnessConfiguration, mContext.getUserId(), mContext.getPackageName());
  }
  
  public void setBrightnessConfigurationForUser(BrightnessConfiguration paramBrightnessConfiguration, int paramInt, String paramString)
  {
    mGlobal.setBrightnessConfigurationForUser(paramBrightnessConfiguration, paramInt, paramString);
  }
  
  @SystemApi
  public void setSaturationLevel(float paramFloat)
  {
    mGlobal.setSaturationLevel(paramFloat);
  }
  
  public void setTemporaryAutoBrightnessAdjustment(float paramFloat)
  {
    mGlobal.setTemporaryAutoBrightnessAdjustment(paramFloat);
  }
  
  public void setTemporaryBrightness(int paramInt)
  {
    mGlobal.setTemporaryBrightness(paramInt);
  }
  
  public void startWifiDisplayScan()
  {
    mGlobal.startWifiDisplayScan();
  }
  
  public void stopWifiDisplayScan()
  {
    mGlobal.stopWifiDisplayScan();
  }
  
  public void unregisterDisplayListener(DisplayListener paramDisplayListener)
  {
    mGlobal.unregisterDisplayListener(paramDisplayListener);
  }
  
  public static abstract interface DisplayListener
  {
    public abstract void onDisplayAdded(int paramInt);
    
    public abstract void onDisplayChanged(int paramInt);
    
    public abstract void onDisplayRemoved(int paramInt);
  }
}
