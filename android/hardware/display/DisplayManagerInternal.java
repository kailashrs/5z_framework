package android.hardware.display;

import android.hardware.SensorManager;
import android.os.Handler;
import android.util.IntArray;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl.Transaction;

public abstract class DisplayManagerInternal
{
  public DisplayManagerInternal() {}
  
  public abstract DisplayInfo getDisplayInfo(int paramInt);
  
  public abstract void getNonOverrideDisplayInfo(int paramInt, DisplayInfo paramDisplayInfo);
  
  public abstract void initPowerManagement(DisplayPowerCallbacks paramDisplayPowerCallbacks, Handler paramHandler, SensorManager paramSensorManager);
  
  public abstract boolean isProximitySensorAvailable();
  
  public abstract boolean isUidPresentOnDisplay(int paramInt1, int paramInt2);
  
  public abstract void onOverlayChanged();
  
  public abstract void performTraversal(SurfaceControl.Transaction paramTransaction);
  
  public abstract void persistBrightnessTrackerState();
  
  public abstract void registerDisplayTransactionListener(DisplayTransactionListener paramDisplayTransactionListener);
  
  public abstract boolean requestPowerState(DisplayPowerRequest paramDisplayPowerRequest, boolean paramBoolean);
  
  public abstract void setDisplayAccessUIDs(SparseArray<IntArray> paramSparseArray);
  
  public abstract void setDisplayInfoOverrideFromWindowManager(int paramInt, DisplayInfo paramDisplayInfo);
  
  public abstract void setDisplayOffsets(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void setDisplayProperties(int paramInt1, boolean paramBoolean1, float paramFloat, int paramInt2, boolean paramBoolean2);
  
  public abstract void setUnblockScreenDelay(boolean paramBoolean);
  
  public abstract void unregisterDisplayTransactionListener(DisplayTransactionListener paramDisplayTransactionListener);
  
  public static abstract interface DisplayPowerCallbacks
  {
    public abstract void acquireSuspendBlocker();
    
    public abstract void onDisplayStateChange(int paramInt);
    
    public abstract void onProximityNegative();
    
    public abstract void onProximityPositive();
    
    public abstract void onStateChanged();
    
    public abstract void releaseSuspendBlocker();
  }
  
  public static final class DisplayPowerRequest
  {
    public static final int POLICY_BRIGHT = 3;
    public static final int POLICY_DIM = 2;
    public static final int POLICY_DOZE = 1;
    public static final int POLICY_OFF = 0;
    public static final int POLICY_VR = 4;
    public boolean blockScreenOn;
    public boolean boostScreenBrightness;
    public int dozeScreenBrightness;
    public int dozeScreenState;
    public boolean lowPowerMode;
    public int policy;
    public float screenAutoBrightnessAdjustmentOverride;
    public int screenBrightnessOverride;
    public float screenLowPowerBrightnessFactor;
    public boolean useAutoBrightness;
    public boolean useProximitySensor;
    
    public DisplayPowerRequest()
    {
      policy = 3;
      useProximitySensor = false;
      screenBrightnessOverride = -1;
      useAutoBrightness = false;
      screenAutoBrightnessAdjustmentOverride = NaN.0F;
      screenLowPowerBrightnessFactor = 0.5F;
      blockScreenOn = false;
      dozeScreenBrightness = -1;
      dozeScreenState = 0;
    }
    
    public DisplayPowerRequest(DisplayPowerRequest paramDisplayPowerRequest)
    {
      copyFrom(paramDisplayPowerRequest);
    }
    
    private boolean floatEquals(float paramFloat1, float paramFloat2)
    {
      boolean bool;
      if ((paramFloat1 != paramFloat2) && ((!Float.isNaN(paramFloat1)) || (!Float.isNaN(paramFloat2)))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public static String policyToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return Integer.toString(paramInt);
      case 4: 
        return "VR";
      case 3: 
        return "BRIGHT";
      case 2: 
        return "DIM";
      case 1: 
        return "DOZE";
      }
      return "OFF";
    }
    
    public void copyFrom(DisplayPowerRequest paramDisplayPowerRequest)
    {
      policy = policy;
      useProximitySensor = useProximitySensor;
      screenBrightnessOverride = screenBrightnessOverride;
      useAutoBrightness = useAutoBrightness;
      screenAutoBrightnessAdjustmentOverride = screenAutoBrightnessAdjustmentOverride;
      screenLowPowerBrightnessFactor = screenLowPowerBrightnessFactor;
      blockScreenOn = blockScreenOn;
      lowPowerMode = lowPowerMode;
      boostScreenBrightness = boostScreenBrightness;
      dozeScreenBrightness = dozeScreenBrightness;
      dozeScreenState = dozeScreenState;
    }
    
    public boolean equals(DisplayPowerRequest paramDisplayPowerRequest)
    {
      boolean bool;
      if ((paramDisplayPowerRequest != null) && (policy == policy) && (useProximitySensor == useProximitySensor) && (screenBrightnessOverride == screenBrightnessOverride) && (useAutoBrightness == useAutoBrightness) && (floatEquals(screenAutoBrightnessAdjustmentOverride, screenAutoBrightnessAdjustmentOverride)) && (screenLowPowerBrightnessFactor == screenLowPowerBrightnessFactor) && (blockScreenOn == blockScreenOn) && (lowPowerMode == lowPowerMode) && (boostScreenBrightness == boostScreenBrightness) && (dozeScreenBrightness == dozeScreenBrightness) && (dozeScreenState == dozeScreenState)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool;
      if (((paramObject instanceof DisplayPowerRequest)) && (equals((DisplayPowerRequest)paramObject))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return 0;
    }
    
    public boolean isBrightOrDim()
    {
      boolean bool;
      if ((policy != 3) && (policy != 2)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean isVr()
    {
      boolean bool;
      if (policy == 4) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("policy=");
      localStringBuilder.append(policyToString(policy));
      localStringBuilder.append(", useProximitySensor=");
      localStringBuilder.append(useProximitySensor);
      localStringBuilder.append(", screenBrightnessOverride=");
      localStringBuilder.append(screenBrightnessOverride);
      localStringBuilder.append(", useAutoBrightness=");
      localStringBuilder.append(useAutoBrightness);
      localStringBuilder.append(", screenAutoBrightnessAdjustmentOverride=");
      localStringBuilder.append(screenAutoBrightnessAdjustmentOverride);
      localStringBuilder.append(", screenLowPowerBrightnessFactor=");
      localStringBuilder.append(screenLowPowerBrightnessFactor);
      localStringBuilder.append(", blockScreenOn=");
      localStringBuilder.append(blockScreenOn);
      localStringBuilder.append(", lowPowerMode=");
      localStringBuilder.append(lowPowerMode);
      localStringBuilder.append(", boostScreenBrightness=");
      localStringBuilder.append(boostScreenBrightness);
      localStringBuilder.append(", dozeScreenBrightness=");
      localStringBuilder.append(dozeScreenBrightness);
      localStringBuilder.append(", dozeScreenState=");
      localStringBuilder.append(Display.stateToString(dozeScreenState));
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface DisplayTransactionListener
  {
    public abstract void onDisplayTransaction();
  }
}
