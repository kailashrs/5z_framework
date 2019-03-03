package com.android.internal.telephony.uicc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class UiccCarrierPrivilegeRules
  extends Handler
{
  private static final int ARAD = 0;
  private static final String ARAD_AID = "A00000015144414300";
  private static final int ARAM = 1;
  private static final String ARAM_AID = "A00000015141434C00";
  private static final String CARRIER_PRIVILEGE_AID = "FFFFFFFFFFFF";
  private static final int CLA = 128;
  private static final int COMMAND = 202;
  private static final String DATA = "";
  private static final boolean DBG = false;
  private static final int EVENT_CLOSE_LOGICAL_CHANNEL_DONE = 3;
  private static final int EVENT_OPEN_LOGICAL_CHANNEL_DONE = 1;
  private static final int EVENT_PKCS15_READ_DONE = 4;
  private static final int EVENT_TRANSMIT_LOGICAL_CHANNEL_DONE = 2;
  private static final String LOG_TAG = "UiccCarrierPrivilegeRules";
  private static final int MAX_RETRY = 1;
  private static final int P1 = 255;
  private static final int P2 = 64;
  private static final int P2_EXTENDED_DATA = 96;
  private static final int P3 = 0;
  private static final int RETRY_INTERVAL_MS = 10000;
  private static final int STATE_ERROR = 2;
  private static final int STATE_LOADED = 1;
  private static final int STATE_LOADING = 0;
  private static final String TAG_AID_REF_DO = "4F";
  private static final String TAG_ALL_REF_AR_DO = "FF40";
  private static final String TAG_AR_DO = "E3";
  private static final String TAG_DEVICE_APP_ID_REF_DO = "C1";
  private static final String TAG_PERM_AR_DO = "DB";
  private static final String TAG_PKG_REF_DO = "CA";
  private static final String TAG_REF_AR_DO = "E2";
  private static final String TAG_REF_DO = "E1";
  private int mAIDInUse;
  private List<UiccAccessRule> mAccessRules;
  private int mChannelId;
  private boolean mCheckedRules = false;
  private Message mLoadedCallback;
  private int mRetryCount;
  private final Runnable mRetryRunnable = new Runnable()
  {
    public void run()
    {
      UiccCarrierPrivilegeRules.this.openChannel(mAIDInUse);
    }
  };
  private String mRules;
  private AtomicInteger mState;
  private String mStatusMessage;
  private UiccPkcs15 mUiccPkcs15;
  private UiccProfile mUiccProfile;
  
  public UiccCarrierPrivilegeRules(UiccProfile paramUiccProfile, Message paramMessage)
  {
    log("Creating UiccCarrierPrivilegeRules");
    mUiccProfile = paramUiccProfile;
    mState = new AtomicInteger(0);
    mStatusMessage = "Not loaded.";
    mLoadedCallback = paramMessage;
    mRules = "";
    mAccessRules = new ArrayList();
    mAIDInUse = 0;
    openChannel(mAIDInUse);
  }
  
  private String getPackageName(ResolveInfo paramResolveInfo)
  {
    if (activityInfo != null) {
      return activityInfo.packageName;
    }
    if (serviceInfo != null) {
      return serviceInfo.packageName;
    }
    if (providerInfo != null) {
      return providerInfo.packageName;
    }
    return null;
  }
  
  private String getStateString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 2: 
      return "STATE_ERROR";
    case 1: 
      return "STATE_LOADED";
    }
    return "STATE_LOADING";
  }
  
  private boolean isDataComplete()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isDataComplete mRules:");
    ((StringBuilder)localObject).append(mRules);
    log(((StringBuilder)localObject).toString());
    if (mRules.startsWith("FF40"))
    {
      TLV localTLV = new TLV("FF40");
      localObject = localTLV.parseLength(mRules);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("isDataComplete lengthBytes: ");
      localStringBuilder.append((String)localObject);
      log(localStringBuilder.toString());
      if (mRules.length() == "FF40".length() + ((String)localObject).length() + length.intValue())
      {
        log("isDataComplete yes");
        return true;
      }
      log("isDataComplete no");
      return false;
    }
    throw new IllegalArgumentException("Tags don't match.");
  }
  
  private static void log(String paramString) {}
  
  private void openChannel(int paramInt)
  {
    String str;
    if (paramInt == 0) {
      str = "A00000015144414300";
    } else {
      str = "A00000015141434C00";
    }
    mUiccProfile.iccOpenLogicalChannel(str, 0, obtainMessage(1, 0, paramInt, null));
  }
  
  private static UiccAccessRule parseRefArdo(String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Got rule: ");
    ((StringBuilder)localObject1).append(paramString);
    log(((StringBuilder)localObject1).toString());
    Object localObject2 = null;
    localObject1 = null;
    while (!paramString.isEmpty())
    {
      String str;
      if (paramString.startsWith("E1"))
      {
        localObject2 = new TLV("E1");
        str = ((TLV)localObject2).parse(paramString, false);
        paramString = new TLV("C1");
        if (value.startsWith("4F"))
        {
          localObject1 = new TLV("4F");
          localObject2 = ((TLV)localObject1).parse(value, false);
          if ((lengthBytes.equals("06")) && (value.equals("FFFFFFFFFFFF")) && (!((String)localObject2).isEmpty()) && (((String)localObject2).startsWith("C1")))
          {
            localObject1 = paramString.parse((String)localObject2, false);
            paramString = value;
          }
          else
          {
            return null;
          }
        }
        else
        {
          if (!value.startsWith("C1")) {
            break label257;
          }
          localObject1 = paramString.parse(value, false);
          paramString = value;
        }
        if (!((String)localObject1).isEmpty())
        {
          if (!((String)localObject1).startsWith("CA")) {
            return null;
          }
          localObject2 = new TLV("CA");
          ((TLV)localObject2).parse((String)localObject1, true);
          localObject1 = new String(IccUtils.hexStringToBytes(value));
        }
        else
        {
          localObject1 = null;
        }
        localObject2 = paramString;
        paramString = str;
        continue;
        label257:
        return null;
      }
      else if (paramString.startsWith("E3"))
      {
        TLV localTLV = new TLV("E3");
        str = localTLV.parse(paramString, false);
        for (paramString = value; (!paramString.isEmpty()) && (!paramString.startsWith("DB")); paramString = new TLV(paramString.substring(0, 2)).parse(paramString, false)) {}
        if (paramString.isEmpty()) {
          return null;
        }
        new TLV("DB").parse(paramString, true);
        paramString = str;
      }
      else
      {
        throw new RuntimeException("Invalid Rule type");
      }
    }
    return new UiccAccessRule(IccUtils.hexStringToBytes((String)localObject2), (String)localObject1, 0L);
  }
  
  private static List<UiccAccessRule> parseRules(String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Got rules: ");
    ((StringBuilder)localObject1).append(paramString);
    log(((StringBuilder)localObject1).toString());
    localObject1 = new TLV("FF40");
    ((TLV)localObject1).parse(paramString, true);
    paramString = value;
    localObject1 = new ArrayList();
    while (!paramString.isEmpty())
    {
      TLV localTLV = new TLV("E2");
      paramString = localTLV.parse(paramString, false);
      Object localObject2 = parseRefArdo(value);
      if (localObject2 != null)
      {
        ((List)localObject1).add(localObject2);
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Skip unrecognized rule.");
        ((StringBuilder)localObject2).append(value);
        Rlog.e("UiccCarrierPrivilegeRules", ((StringBuilder)localObject2).toString());
      }
    }
    return localObject1;
  }
  
  private void updateState(int paramInt, String paramString)
  {
    mState.set(paramInt);
    if (mLoadedCallback != null) {
      mLoadedCallback.sendToTarget();
    }
    mStatusMessage = paramString;
  }
  
  public boolean areCarrierPriviligeRulesLoaded()
  {
    boolean bool;
    if (mState.get() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("UiccCarrierPrivilegeRules: ");
    ((StringBuilder)localObject).append(this);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mState=");
    ((StringBuilder)localObject).append(getStateString(mState.get()));
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mStatusMessage='");
    ((StringBuilder)localObject).append(mStatusMessage);
    ((StringBuilder)localObject).append("'");
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    if (mAccessRules != null)
    {
      paramPrintWriter.println(" mAccessRules: ");
      localObject = mAccessRules.iterator();
      while (((Iterator)localObject).hasNext())
      {
        UiccAccessRule localUiccAccessRule = (UiccAccessRule)((Iterator)localObject).next();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("  rule='");
        localStringBuilder.append(localUiccAccessRule);
        localStringBuilder.append("'");
        paramPrintWriter.println(localStringBuilder.toString());
      }
    }
    paramPrintWriter.println(" mAccessRules: null");
    if (mUiccPkcs15 != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" mUiccPkcs15: ");
      ((StringBuilder)localObject).append(mUiccPkcs15);
      paramPrintWriter.println(((StringBuilder)localObject).toString());
      mUiccPkcs15.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    else
    {
      paramPrintWriter.println(" mUiccPkcs15: null");
    }
    paramPrintWriter.flush();
  }
  
  public List<UiccAccessRule> getAccessRules()
  {
    if (mAccessRules == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(mAccessRules);
  }
  
  public List<String> getCarrierPackageNamesForIntent(PackageManager paramPackageManager, Intent paramIntent)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = new ArrayList();
    ((List)localObject).addAll(paramPackageManager.queryBroadcastReceivers(paramIntent, 0));
    ((List)localObject).addAll(paramPackageManager.queryIntentContentProviders(paramIntent, 0));
    ((List)localObject).addAll(paramPackageManager.queryIntentActivities(paramIntent, 0));
    ((List)localObject).addAll(paramPackageManager.queryIntentServices(paramIntent, 0));
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramIntent = getPackageName((ResolveInfo)((Iterator)localObject).next());
      if (paramIntent != null)
      {
        int i = getCarrierPrivilegeStatus(paramPackageManager, paramIntent);
        if (i == 1) {
          localArrayList.add(paramIntent);
        } else if (i != 0) {
          return null;
        }
      }
    }
    return localArrayList;
  }
  
  public int getCarrierPrivilegeStatus(PackageInfo paramPackageInfo)
  {
    int i = mState.get();
    if (i == 0) {
      return -1;
    }
    if (i == 2) {
      return -2;
    }
    Iterator localIterator = mAccessRules.iterator();
    while (localIterator.hasNext())
    {
      i = ((UiccAccessRule)localIterator.next()).getCarrierPrivilegeStatus(paramPackageInfo);
      if (i != 0) {
        return i;
      }
    }
    return 0;
  }
  
  public int getCarrierPrivilegeStatus(PackageManager paramPackageManager, String paramString)
  {
    try
    {
      if (!hasCarrierPrivilegeRules())
      {
        i = mState.get();
        if (i == 0) {
          return -1;
        }
        if (i == 2) {
          return -2;
        }
        return 0;
      }
      int i = getCarrierPrivilegeStatus(paramPackageManager.getPackageInfo(paramString, 32832));
      return i;
    }
    catch (PackageManager.NameNotFoundException paramPackageManager)
    {
      paramPackageManager = new StringBuilder();
      paramPackageManager.append("Package ");
      paramPackageManager.append(paramString);
      paramPackageManager.append(" not found for carrier privilege status check");
      log(paramPackageManager.toString());
    }
    return 0;
  }
  
  public int getCarrierPrivilegeStatus(Signature paramSignature, String paramString)
  {
    int i = mState.get();
    if (i == 0) {
      return -1;
    }
    if (i == 2) {
      return -2;
    }
    Iterator localIterator = mAccessRules.iterator();
    while (localIterator.hasNext())
    {
      i = ((UiccAccessRule)localIterator.next()).getCarrierPrivilegeStatus(paramSignature, paramString);
      if (i != 0) {
        return i;
      }
    }
    return 0;
  }
  
  public int getCarrierPrivilegeStatusForCurrentTransaction(PackageManager paramPackageManager)
  {
    return getCarrierPrivilegeStatusForUid(paramPackageManager, Binder.getCallingUid());
  }
  
  public int getCarrierPrivilegeStatusForUid(PackageManager paramPackageManager, int paramInt)
  {
    String[] arrayOfString = paramPackageManager.getPackagesForUid(paramInt);
    int i = arrayOfString.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      int j = getCarrierPrivilegeStatus(paramPackageManager, arrayOfString[paramInt]);
      if (j != 0) {
        return j;
      }
    }
    return 0;
  }
  
  public List<String> getPackageNames()
  {
    ArrayList localArrayList = new ArrayList();
    if (mAccessRules != null)
    {
      Iterator localIterator = mAccessRules.iterator();
      while (localIterator.hasNext())
      {
        UiccAccessRule localUiccAccessRule = (UiccAccessRule)localIterator.next();
        if (!TextUtils.isEmpty(localUiccAccessRule.getPackageName())) {
          localArrayList.add(localUiccAccessRule.getPackageName());
        }
      }
    }
    return localArrayList;
  }
  
  public void handleMessage(Message paramMessage)
  {
    mAIDInUse = arg2;
    Object localObject;
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown event ");
      ((StringBuilder)localObject).append(what);
      Rlog.e("UiccCarrierPrivilegeRules", ((StringBuilder)localObject).toString());
      break;
    case 4: 
      log("EVENT_PKCS15_READ_DONE");
      if ((mUiccPkcs15 != null) && (mUiccPkcs15.getRules() != null))
      {
        localObject = mUiccPkcs15.getRules().iterator();
        while (((Iterator)localObject).hasNext())
        {
          paramMessage = new UiccAccessRule(IccUtils.hexStringToBytes((String)((Iterator)localObject).next()), "", 0L);
          mAccessRules.add(paramMessage);
        }
        updateState(1, "Success!");
      }
      else
      {
        updateState(2, "No ARA or ARF.");
      }
      break;
    case 3: 
      log("EVENT_CLOSE_LOGICAL_CHANNEL_DONE");
      if (mAIDInUse == 0)
      {
        mRules = "";
        openChannel(1);
      }
      break;
    case 2: 
      log("EVENT_TRANSMIT_LOGICAL_CHANNEL_DONE");
      paramMessage = (AsyncResult)obj;
      if ((exception == null) && (result != null))
      {
        paramMessage = (IccIoResult)result;
        if ((sw1 == 144) && (sw2 == 0) && (payload != null) && (payload.length > 0))
        {
          try
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append(mRules);
            ((StringBuilder)localObject).append(IccUtils.bytesToHexString(payload).toUpperCase(Locale.US));
            mRules = ((StringBuilder)localObject).toString();
            if (isDataComplete())
            {
              mAccessRules.addAll(parseRules(mRules));
              if (mAIDInUse == 0) {
                mCheckedRules = true;
              } else {
                updateState(1, "Success!");
              }
            }
            else
            {
              mUiccProfile.iccTransmitApduLogicalChannel(mChannelId, 128, 202, 255, 96, 0, "", obtainMessage(2, mChannelId, mAIDInUse));
            }
          }
          catch (IllegalArgumentException|IndexOutOfBoundsException localIllegalArgumentException)
          {
            if (mAIDInUse == 1)
            {
              paramMessage = new StringBuilder();
              paramMessage.append("Error parsing rules: ");
              paramMessage.append(localIllegalArgumentException);
              updateState(2, paramMessage.toString());
            }
          }
        }
        else if (mAIDInUse == 1)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid response: payload=");
          localStringBuilder.append(payload);
          localStringBuilder.append(" sw1=");
          localStringBuilder.append(sw1);
          localStringBuilder.append(" sw2=");
          localStringBuilder.append(sw2);
          updateState(2, localStringBuilder.toString());
        }
      }
      else if (mAIDInUse == 1)
      {
        updateState(2, "Error reading value from SIM.");
      }
      mUiccProfile.iccCloseLogicalChannel(mChannelId, obtainMessage(3, 0, mAIDInUse));
      mChannelId = -1;
      break;
    case 1: 
      log("EVENT_OPEN_LOGICAL_CHANNEL_DONE");
      paramMessage = (AsyncResult)obj;
      if ((exception == null) && (result != null))
      {
        mChannelId = ((int[])result)[0];
        mUiccProfile.iccTransmitApduLogicalChannel(mChannelId, 128, 202, 255, 64, 0, "", obtainMessage(2, mChannelId, mAIDInUse));
      }
      else if (((exception instanceof CommandException)) && (mRetryCount < 1) && (((CommandException)exception).getCommandError() == CommandException.Error.MISSING_RESOURCE))
      {
        mRetryCount += 1;
        removeCallbacks(mRetryRunnable);
        postDelayed(mRetryRunnable, 10000L);
      }
      else
      {
        if (mAIDInUse == 0)
        {
          mRules = "";
          openChannel(1);
        }
        if (mAIDInUse == 1) {
          if (mCheckedRules)
          {
            updateState(1, "Success!");
          }
          else
          {
            log("No ARA, try ARF next.");
            mUiccPkcs15 = new UiccPkcs15(mUiccProfile, obtainMessage(4));
          }
        }
      }
      break;
    }
  }
  
  public boolean hasCarrierPrivilegeRules()
  {
    boolean bool;
    if ((mState.get() != 0) && (mAccessRules != null) && (mAccessRules.size() > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static class TLV
  {
    private static final int SINGLE_BYTE_MAX_LENGTH = 128;
    private Integer length;
    private String lengthBytes;
    private String tag;
    private String value;
    
    public TLV(String paramString)
    {
      tag = paramString;
    }
    
    public String getValue()
    {
      if (value == null) {
        return "";
      }
      return value;
    }
    
    public String parse(String paramString, boolean paramBoolean)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Parse TLV: ");
      localStringBuilder.append(tag);
      UiccCarrierPrivilegeRules.log(localStringBuilder.toString());
      if (paramString.startsWith(tag))
      {
        int i = tag.length();
        if (i + 2 <= paramString.length())
        {
          parseLength(paramString);
          i += lengthBytes.length();
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("index=");
          localStringBuilder.append(i);
          localStringBuilder.append(" length=");
          localStringBuilder.append(length);
          localStringBuilder.append("data.length=");
          localStringBuilder.append(paramString.length());
          UiccCarrierPrivilegeRules.log(localStringBuilder.toString());
          int j = paramString.length() - (length.intValue() + i);
          if (j >= 0)
          {
            if ((paramBoolean) && (j != 0)) {
              throw new IllegalArgumentException("Did not consume all.");
            }
            value = paramString.substring(i, length.intValue() + i);
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Got TLV: ");
            localStringBuilder.append(tag);
            localStringBuilder.append(",");
            localStringBuilder.append(length);
            localStringBuilder.append(",");
            localStringBuilder.append(value);
            UiccCarrierPrivilegeRules.log(localStringBuilder.toString());
            return paramString.substring(length.intValue() + i);
          }
          throw new IllegalArgumentException("Not enough data.");
        }
        throw new IllegalArgumentException("No length.");
      }
      throw new IllegalArgumentException("Tags don't match.");
    }
    
    public String parseLength(String paramString)
    {
      int i = tag.length();
      int j = Integer.parseInt(paramString.substring(i, i + 2), 16);
      if (j < 128)
      {
        length = Integer.valueOf(j * 2);
        lengthBytes = paramString.substring(i, i + 2);
      }
      else
      {
        j -= 128;
        length = Integer.valueOf(Integer.parseInt(paramString.substring(i + 2, i + 2 + j * 2), 16) * 2);
        lengthBytes = paramString.substring(i, i + 2 + j * 2);
      }
      paramString = new StringBuilder();
      paramString.append("TLV parseLength length=");
      paramString.append(length);
      paramString.append("lenghtBytes: ");
      paramString.append(lengthBytes);
      UiccCarrierPrivilegeRules.log(paramString.toString());
      return lengthBytes;
    }
  }
}
