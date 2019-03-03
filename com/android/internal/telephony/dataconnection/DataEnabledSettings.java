package com.android.internal.telephony.dataconnection;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.LocalLog;
import android.util.Pair;
import com.android.internal.telephony.Phone;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DataEnabledSettings
{
  private static final String LOG_TAG = "DataEnabledSettings";
  public static final int REASON_DATA_ENABLED_BY_CARRIER = 4;
  public static final int REASON_INTERNAL_DATA_ENABLED = 1;
  public static final int REASON_POLICY_DATA_ENABLED = 3;
  public static final int REASON_REGISTERED = 0;
  public static final int REASON_USER_DATA_ENABLED = 2;
  private boolean mCarrierDataEnabled = true;
  private final RegistrantList mDataEnabledChangedRegistrants = new RegistrantList();
  private boolean mInternalDataEnabled = true;
  private Phone mPhone = null;
  private boolean mPolicyDataEnabled = true;
  private ContentResolver mResolver = null;
  private final LocalLog mSettingChangeLocalLog = new LocalLog(50);
  
  public DataEnabledSettings(Phone paramPhone)
  {
    mPhone = paramPhone;
    mResolver = mPhone.getContext().getContentResolver();
  }
  
  private String getMobileDataSettingName()
  {
    int i = mPhone.getPhoneId();
    if ((TelephonyManager.getDefault().getSimCount() != 1) && (SubscriptionManager.isValidSlotIndex(i)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("mobile_data");
      localStringBuilder.append(i);
      return localStringBuilder.toString();
    }
    return "mobile_data";
  }
  
  private void localLog(String paramString, boolean paramBoolean)
  {
    LocalLog localLocalLog = mSettingChangeLocalLog;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" change to ");
    localStringBuilder.append(paramBoolean);
    localLocalLog.log(localStringBuilder.toString());
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.d("DataEnabledSettings", localStringBuilder.toString());
  }
  
  private void notifyDataEnabledChanged(boolean paramBoolean, int paramInt)
  {
    mDataEnabledChangedRegistrants.notifyResult(new Pair(Boolean.valueOf(paramBoolean), Integer.valueOf(paramInt)));
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println(" DataEnabledSettings=");
    mSettingChangeLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public boolean isCarrierDataEnabled()
  {
    try
    {
      boolean bool = mCarrierDataEnabled;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isDataEnabled()
  {
    try
    {
      if (isProvisioning())
      {
        bool = isProvisioningDataEnabled();
        return bool;
      }
      if ((mInternalDataEnabled) && (isUserDataEnabled()) && (mPolicyDataEnabled))
      {
        bool = mCarrierDataEnabled;
        if (bool)
        {
          bool = true;
          break label55;
        }
      }
      boolean bool = false;
      label55:
      return bool;
    }
    finally {}
  }
  
  public boolean isInternalDataEnabled()
  {
    try
    {
      boolean bool = mInternalDataEnabled;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isPolicyDataEnabled()
  {
    try
    {
      boolean bool = mPolicyDataEnabled;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isProvisioning()
  {
    ContentResolver localContentResolver = mResolver;
    boolean bool = false;
    if (Settings.Global.getInt(localContentResolver, "device_provisioned", 0) == 0) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isProvisioningDataEnabled()
  {
    String str = SystemProperties.get("ro.com.android.prov_mobiledata", "false");
    int i = "true".equalsIgnoreCase(str);
    Object localObject = mResolver;
    int j = Settings.Global.getInt((ContentResolver)localObject, "device_provisioning_mobile_data", i);
    boolean bool;
    if (j != 0) {
      bool = true;
    } else {
      bool = false;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getDataEnabled during provisioning retVal=");
    ((StringBuilder)localObject).append(bool);
    ((StringBuilder)localObject).append(" - (");
    ((StringBuilder)localObject).append(str);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(j);
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    return bool;
  }
  
  public boolean isUserDataEnabled()
  {
    try
    {
      boolean bool1 = "true".equalsIgnoreCase(SystemProperties.get("ro.com.android.mobiledata", "true"));
      ContentResolver localContentResolver = mResolver;
      String str = getMobileDataSettingName();
      boolean bool2 = false;
      if (bool1) {
        i = 1;
      } else {
        i = 0;
      }
      int i = Settings.Global.getInt(localContentResolver, str, i);
      if (i != 0) {
        bool2 = true;
      }
      return bool2;
    }
    finally {}
  }
  
  public void registerForDataEnabledChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDataEnabledChangedRegistrants.addUnique(paramHandler, paramInt, paramObject);
    notifyDataEnabledChanged(isDataEnabled(), 0);
  }
  
  public void setCarrierDataEnabled(boolean paramBoolean)
  {
    try
    {
      localLog("CarrierDataEnabled", paramBoolean);
      boolean bool = isDataEnabled();
      mCarrierDataEnabled = paramBoolean;
      if (bool != isDataEnabled())
      {
        if (!bool) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        notifyDataEnabledChanged(paramBoolean, 4);
      }
      return;
    }
    finally {}
  }
  
  public void setDefaultMobileDataEnabled()
  {
    String str = getMobileDataSettingName();
    int i = 0;
    try
    {
      Settings.Global.getInt(mResolver, str);
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
      i = 1;
    }
    if (i != 0)
    {
      i = "true".equalsIgnoreCase(SystemProperties.get("ro.com.android.mobiledata", "true"));
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("etDefaultMobileDataEnabled ");
      localStringBuilder.append(str);
      localStringBuilder.append("default value: ");
      localStringBuilder.append(i);
      log(localStringBuilder.toString());
      Settings.Global.putInt(mResolver, str, i);
    }
  }
  
  public void setInternalDataEnabled(boolean paramBoolean)
  {
    try
    {
      localLog("InternalDataEnabled", paramBoolean);
      boolean bool = isDataEnabled();
      mInternalDataEnabled = paramBoolean;
      if (bool != isDataEnabled())
      {
        if (!bool) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        notifyDataEnabledChanged(paramBoolean, 1);
      }
      return;
    }
    finally {}
  }
  
  public void setPolicyDataEnabled(boolean paramBoolean)
  {
    try
    {
      localLog("PolicyDataEnabled", paramBoolean);
      boolean bool = isDataEnabled();
      mPolicyDataEnabled = paramBoolean;
      if (bool != isDataEnabled())
      {
        if (!bool) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        notifyDataEnabledChanged(paramBoolean, 3);
      }
      return;
    }
    finally {}
  }
  
  public void setUserDataEnabled(boolean paramBoolean)
  {
    try
    {
      localLog("UserDataEnabled", paramBoolean);
      boolean bool1 = isDataEnabled();
      Settings.Global.putInt(mResolver, getMobileDataSettingName(), paramBoolean);
      int i = mPhone.getPhoneId();
      int j = TelephonyManager.getDefault().getSimCount();
      boolean bool2 = false;
      if ((j > 1) && (SubscriptionManager.isValidSlotIndex(i)))
      {
        StringBuilder localStringBuilder;
        if (i == SystemProperties.getInt("persist.asus.mobile_slot", 0))
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("[ABSP][setUserDataEnabled] mPhone.getPhoneId(");
          localStringBuilder.append(i);
          localStringBuilder.append(") == persist.asus.mobile_slot");
          log(localStringBuilder.toString());
          Settings.Global.putInt(mResolver, "mobile_data", paramBoolean);
        }
        else
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("[ABSP][setUserDataEnabled] mPhone.getPhoneId(");
          localStringBuilder.append(i);
          localStringBuilder.append(") != persist.asus.mobile_slot");
          log(localStringBuilder.toString());
        }
      }
      if (bool1 != isDataEnabled())
      {
        if (!bool1) {
          bool2 = true;
        }
        notifyDataEnabledChanged(bool2, 2);
      }
      return;
    }
    finally {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[mInternalDataEnabled=");
    localStringBuilder.append(mInternalDataEnabled);
    localStringBuilder.append(", isUserDataEnabled=");
    localStringBuilder.append(isUserDataEnabled());
    localStringBuilder.append(", isProvisioningDataEnabled=");
    localStringBuilder.append(isProvisioningDataEnabled());
    localStringBuilder.append(", mPolicyDataEnabled=");
    localStringBuilder.append(mPolicyDataEnabled);
    localStringBuilder.append(", mCarrierDataEnabled=");
    localStringBuilder.append(mCarrierDataEnabled);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void unregisterForDataEnabledChanged(Handler paramHandler)
  {
    mDataEnabledChangedRegistrants.remove(paramHandler);
  }
}
