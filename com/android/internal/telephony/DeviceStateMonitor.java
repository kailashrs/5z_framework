package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.LocalLog;
import android.util.SparseIntArray;
import android.view.Display;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DeviceStateMonitor
  extends Handler
{
  protected static final boolean DBG = false;
  private static final int EVENT_CHARGING_STATE_CHANGED = 4;
  private static final int EVENT_POWER_SAVE_MODE_CHANGED = 3;
  private static final int EVENT_RIL_CONNECTED = 0;
  private static final int EVENT_SCREEN_STATE_CHANGED = 2;
  private static final int EVENT_TETHERING_STATE_CHANGED = 5;
  private static final int EVENT_UPDATE_MODE_CHANGED = 1;
  private static final int HYSTERESIS_KBPS = 50;
  private static final int[] LINK_CAPACITY_DOWNLINK_THRESHOLDS = { 500, 1000, 5000, 10000, 20000 };
  private static final int[] LINK_CAPACITY_UPLINK_THRESHOLDS = { 100, 500, 1000, 5000, 10000 };
  protected static final String TAG = DeviceStateMonitor.class.getSimpleName();
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = DeviceStateMonitor.this;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("received: ");
      ((StringBuilder)localObject).append(paramAnonymousIntent);
      localObject = ((StringBuilder)localObject).toString();
      int i = 1;
      paramAnonymousContext.log((String)localObject, true);
      paramAnonymousContext = paramAnonymousIntent.getAction();
      int j = paramAnonymousContext.hashCode();
      if (j != -1754841973)
      {
        if (j != -54942926)
        {
          if (j != 948344062)
          {
            if ((j == 1779291251) && (paramAnonymousContext.equals("android.os.action.POWER_SAVE_MODE_CHANGED")))
            {
              j = 0;
              break label145;
            }
          }
          else if (paramAnonymousContext.equals("android.os.action.CHARGING"))
          {
            j = 1;
            break label145;
          }
        }
        else if (paramAnonymousContext.equals("android.os.action.DISCHARGING"))
        {
          j = 2;
          break label145;
        }
      }
      else if (paramAnonymousContext.equals("android.net.conn.TETHER_STATE_CHANGED"))
      {
        j = 3;
        break label145;
      }
      j = -1;
      switch (j)
      {
      default: 
        localObject = DeviceStateMonitor.this;
        paramAnonymousContext = new StringBuilder();
        paramAnonymousContext.append("Unexpected broadcast intent: ");
        paramAnonymousContext.append(paramAnonymousIntent);
        ((DeviceStateMonitor)localObject).log(paramAnonymousContext.toString(), false);
        return;
      case 3: 
        paramAnonymousContext = paramAnonymousIntent.getStringArrayListExtra("tetherArray");
        if ((paramAnonymousContext != null) && (paramAnonymousContext.size() > 0)) {
          j = 1;
        } else {
          j = 0;
        }
        localObject = DeviceStateMonitor.this;
        paramAnonymousIntent = new StringBuilder();
        paramAnonymousIntent.append("Tethering ");
        if (j != 0) {
          paramAnonymousContext = "on";
        } else {
          paramAnonymousContext = "off";
        }
        paramAnonymousIntent.append(paramAnonymousContext);
        ((DeviceStateMonitor)localObject).log(paramAnonymousIntent.toString(), true);
        paramAnonymousContext = obtainMessage(5);
        if (j != 0) {
          j = i;
        } else {
          j = 0;
        }
        arg1 = j;
        break;
      case 2: 
        paramAnonymousContext = obtainMessage(4);
        arg1 = 0;
        break;
      case 1: 
        paramAnonymousContext = obtainMessage(4);
        arg1 = 1;
        break;
      case 0: 
        label145:
        paramAnonymousIntent = obtainMessage(3);
        arg1 = DeviceStateMonitor.this.isPowerSaveModeOn();
        DeviceStateMonitor localDeviceStateMonitor = DeviceStateMonitor.this;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Power Save mode ");
        if (arg1 == 1) {
          paramAnonymousContext = "on";
        } else {
          paramAnonymousContext = "off";
        }
        ((StringBuilder)localObject).append(paramAnonymousContext);
        localDeviceStateMonitor.log(((StringBuilder)localObject).toString(), true);
        paramAnonymousContext = paramAnonymousIntent;
      }
      sendMessage(paramAnonymousContext);
    }
  };
  private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener()
  {
    public void onDisplayAdded(int paramAnonymousInt) {}
    
    public void onDisplayChanged(int paramAnonymousInt)
    {
      paramAnonymousInt = DeviceStateMonitor.this.isScreenOn();
      Message localMessage = obtainMessage(2);
      arg1 = paramAnonymousInt;
      sendMessage(localMessage);
    }
    
    public void onDisplayRemoved(int paramAnonymousInt) {}
  };
  private boolean mIsCharging;
  private boolean mIsLowDataExpected;
  private boolean mIsPowerSaveOn;
  private boolean mIsScreenOn;
  private boolean mIsTetheringOn;
  private final LocalLog mLocalLog = new LocalLog(100);
  private final Phone mPhone;
  private int mUnsolicitedResponseFilter = -1;
  private SparseIntArray mUpdateModes = new SparseIntArray();
  
  public DeviceStateMonitor(Phone paramPhone)
  {
    mPhone = paramPhone;
    ((DisplayManager)paramPhone.getContext().getSystemService("display")).registerDisplayListener(mDisplayListener, null);
    mIsPowerSaveOn = isPowerSaveModeOn();
    mIsCharging = isDeviceCharging();
    mIsScreenOn = isScreenOn();
    mIsTetheringOn = false;
    mIsLowDataExpected = false;
    paramPhone = new StringBuilder();
    paramPhone.append("DeviceStateMonitor mIsPowerSaveOn=");
    paramPhone.append(mIsPowerSaveOn);
    paramPhone.append(",mIsScreenOn=");
    paramPhone.append(mIsScreenOn);
    paramPhone.append(",mIsCharging=");
    paramPhone.append(mIsCharging);
    log(paramPhone.toString(), false);
    paramPhone = new IntentFilter();
    paramPhone.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
    paramPhone.addAction("android.os.action.CHARGING");
    paramPhone.addAction("android.os.action.DISCHARGING");
    paramPhone.addAction("android.net.conn.TETHER_STATE_CHANGED");
    mPhone.getContext().registerReceiver(mBroadcastReceiver, paramPhone, null, mPhone);
    mPhone.mCi.registerForRilConnected(this, 0, null);
  }
  
  private String deviceTypeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 2: 
      return "LOW_DATA_EXPECTED";
    case 1: 
      return "CHARGING_STATE";
    }
    return "POWER_SAVE_MODE";
  }
  
  private boolean isDeviceCharging()
  {
    return ((BatteryManager)mPhone.getContext().getSystemService("batterymanager")).isCharging();
  }
  
  private boolean isLowDataExpected()
  {
    boolean bool;
    if ((!mIsCharging) && (!mIsTetheringOn) && (!mIsScreenOn)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isPowerSaveModeOn()
  {
    return ((PowerManager)mPhone.getContext().getSystemService("power")).isPowerSaveMode();
  }
  
  private boolean isScreenOn()
  {
    Object localObject1 = ((DisplayManager)mPhone.getContext().getSystemService("display")).getDisplays();
    if (localObject1 != null)
    {
      int i = localObject1.length;
      for (int j = 0; j < i; j++)
      {
        Object localObject2 = localObject1[j];
        if (localObject2.getState() == 2)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Screen ");
          ((StringBuilder)localObject1).append(Display.typeToString(localObject2.getType()));
          ((StringBuilder)localObject1).append(" on");
          log(((StringBuilder)localObject1).toString(), true);
          return true;
        }
      }
      log("Screens all off", true);
      return false;
    }
    log("No displays found", true);
    return false;
  }
  
  private void log(String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      mLocalLog.log(paramString);
    }
  }
  
  private void onRilConnected()
  {
    log("RIL connected.", true);
    sendDeviceState(1, mIsCharging);
    sendDeviceState(2, mIsLowDataExpected);
    sendDeviceState(0, mIsPowerSaveOn);
    setUnsolResponseFilter(mUnsolicitedResponseFilter, true);
    setSignalStrengthReportingCriteria();
    setLinkCapacityReportingCriteria();
  }
  
  private void onSetIndicationUpdateMode(int paramInt1, int paramInt2)
  {
    if ((paramInt1 & 0x1) != 0) {
      mUpdateModes.put(1, paramInt2);
    }
    if ((paramInt1 & 0x2) != 0) {
      mUpdateModes.put(2, paramInt2);
    }
    if ((paramInt1 & 0x4) != 0) {
      mUpdateModes.put(4, paramInt2);
    }
    if ((paramInt1 & 0x8) != 0) {
      mUpdateModes.put(8, paramInt2);
    }
    if ((paramInt1 & 0x10) != 0) {
      mUpdateModes.put(16, paramInt2);
    }
  }
  
  private void onUpdateDeviceState(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    default: 
      return;
    case 5: 
      if (mIsTetheringOn == paramBoolean) {
        return;
      }
      mIsTetheringOn = paramBoolean;
      break;
    case 4: 
      if (mIsCharging == paramBoolean) {
        return;
      }
      mIsCharging = paramBoolean;
      sendDeviceState(1, mIsCharging);
      break;
    case 3: 
      if (mIsPowerSaveOn == paramBoolean) {
        return;
      }
      mIsPowerSaveOn = paramBoolean;
      sendDeviceState(0, mIsPowerSaveOn);
      break;
    case 2: 
      if (mIsScreenOn == paramBoolean) {
        return;
      }
      mIsScreenOn = paramBoolean;
      mPhone.mCi.setScreenState(mIsScreenOn);
    }
    if (mIsLowDataExpected != isLowDataExpected())
    {
      mIsLowDataExpected = (true ^ mIsLowDataExpected);
      sendDeviceState(2, mIsLowDataExpected);
    }
    paramInt = 0;
    if (!shouldTurnOffSignalStrength()) {
      paramInt = 0x0 | 0x1;
    }
    int i = paramInt;
    if (!shouldTurnOffFullNetworkUpdate()) {
      i = paramInt | 0x2;
    }
    paramInt = i;
    if (!shouldTurnOffDormancyUpdate()) {
      paramInt = i | 0x4;
    }
    i = paramInt;
    if (!shouldTurnOffLinkCapacityEstimate()) {
      i = paramInt | 0x8;
    }
    paramInt = i;
    if (!shouldTurnOffPhysicalChannelConfig()) {
      paramInt = i | 0x10;
    }
    setUnsolResponseFilter(paramInt, false);
  }
  
  private void sendDeviceState(int paramInt, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("send type: ");
    localStringBuilder.append(deviceTypeToString(paramInt));
    localStringBuilder.append(", state=");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString(), true);
    mPhone.mCi.sendDeviceState(paramInt, paramBoolean, null);
  }
  
  private void setLinkCapacityReportingCriteria()
  {
    mPhone.setLinkCapacityReportingCriteria(LINK_CAPACITY_DOWNLINK_THRESHOLDS, LINK_CAPACITY_UPLINK_THRESHOLDS, 1);
    mPhone.setLinkCapacityReportingCriteria(LINK_CAPACITY_DOWNLINK_THRESHOLDS, LINK_CAPACITY_UPLINK_THRESHOLDS, 2);
    mPhone.setLinkCapacityReportingCriteria(LINK_CAPACITY_DOWNLINK_THRESHOLDS, LINK_CAPACITY_UPLINK_THRESHOLDS, 3);
    mPhone.setLinkCapacityReportingCriteria(LINK_CAPACITY_DOWNLINK_THRESHOLDS, LINK_CAPACITY_UPLINK_THRESHOLDS, 4);
  }
  
  private void setSignalStrengthReportingCriteria()
  {
    mPhone.setSignalStrengthReportingCriteria(AccessNetworkThresholds.GERAN, 1);
    mPhone.setSignalStrengthReportingCriteria(AccessNetworkThresholds.UTRAN, 2);
    mPhone.setSignalStrengthReportingCriteria(AccessNetworkThresholds.EUTRAN, 3);
    mPhone.setSignalStrengthReportingCriteria(AccessNetworkThresholds.CDMA2000, 4);
  }
  
  private void setUnsolResponseFilter(int paramInt, boolean paramBoolean)
  {
    if ((paramBoolean) || (paramInt != mUnsolicitedResponseFilter))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("old filter: ");
      localStringBuilder.append(mUnsolicitedResponseFilter);
      localStringBuilder.append(", new filter: ");
      localStringBuilder.append(paramInt);
      log(localStringBuilder.toString(), true);
      mPhone.mCi.setUnsolResponseFilter(paramInt, null);
      mUnsolicitedResponseFilter = paramInt;
    }
  }
  
  private boolean shouldTurnOffDormancyUpdate()
  {
    return (!mIsCharging) && (!mIsScreenOn) && (!mIsTetheringOn) && (mUpdateModes.get(4) != 2);
  }
  
  private boolean shouldTurnOffFullNetworkUpdate()
  {
    return (!mIsCharging) && (!mIsScreenOn) && (!mIsTetheringOn) && (mUpdateModes.get(2) != 2);
  }
  
  private boolean shouldTurnOffLinkCapacityEstimate()
  {
    return (!mIsCharging) && (!mIsScreenOn) && (!mIsTetheringOn) && (mUpdateModes.get(8) != 2);
  }
  
  private boolean shouldTurnOffPhysicalChannelConfig()
  {
    return (!mIsCharging) && (!mIsScreenOn) && (!mIsTetheringOn) && (mUpdateModes.get(16) != 2);
  }
  
  private boolean shouldTurnOffSignalStrength()
  {
    return (!mIsCharging) && (!mIsScreenOn) && (mUpdateModes.get(1) != 2);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.increaseIndent();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mIsTetheringOn=");
    localStringBuilder.append(mIsTetheringOn);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("mIsScreenOn=");
    localStringBuilder.append(mIsScreenOn);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("mIsCharging=");
    localStringBuilder.append(mIsCharging);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("mIsPowerSaveOn=");
    localStringBuilder.append(mIsPowerSaveOn);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("mIsLowDataExpected=");
    localStringBuilder.append(mIsLowDataExpected);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("mUnsolicitedResponseFilter=");
    localStringBuilder.append(mUnsolicitedResponseFilter);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.println("Local logs:");
    paramPrintWriter.increaseIndent();
    mLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.flush();
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("handleMessage msg=");
    ((StringBuilder)localObject).append(paramMessage);
    localObject = ((StringBuilder)localObject).toString();
    boolean bool = false;
    log((String)localObject, false);
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unexpected message arrives. msg = ");
      ((StringBuilder)localObject).append(what);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      int i = what;
      if (arg1 != 0) {
        bool = true;
      }
      onUpdateDeviceState(i, bool);
      break;
    case 1: 
      onSetIndicationUpdateMode(arg1, arg2);
      break;
    case 0: 
      onRilConnected();
    }
  }
  
  public void setIndicationUpdateMode(int paramInt1, int paramInt2)
  {
    sendMessage(obtainMessage(1, paramInt1, paramInt2));
  }
  
  private static final class AccessNetworkThresholds
  {
    public static final int[] CDMA2000 = new int[0];
    public static final int[] EUTRAN;
    public static final int[] GERAN = { -114, -106, -101, -96, -90, -80, -70, -60 };
    public static final int[] UTRAN = { -114, -106, -101, -96, -89, -79, -69, -59 };
    
    static
    {
      EUTRAN = new int[] { -126, -119, -114, -109, -100, -90, -80, -70 };
    }
    
    private AccessNetworkThresholds() {}
  }
}
