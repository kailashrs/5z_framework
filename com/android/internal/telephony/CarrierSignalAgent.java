package com.android.internal.telephony;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.LocalLog;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CarrierSignalAgent
  extends Handler
{
  private static final String CARRIER_SIGNAL_DELIMITER = "\\s*,\\s*";
  private static final String COMPONENT_NAME_DELIMITER = "\\s*:\\s*";
  private static final boolean DBG = true;
  private static final int EVENT_REGISTER_DEFAULT_NETWORK_AVAIL = 0;
  private static final String LOG_TAG = CarrierSignalAgent.class.getSimpleName();
  private static final boolean NO_WAKE = false;
  private static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);
  private static final boolean WAKE = true;
  private Map<String, Set<ComponentName>> mCachedNoWakeSignalConfigs = new HashMap();
  private Map<String, Set<ComponentName>> mCachedWakeSignalConfigs = new HashMap();
  private final Set<String> mCarrierSignalList = new HashSet(Arrays.asList(new String[] { "com.android.internal.telephony.CARRIER_SIGNAL_PCO_VALUE", "com.android.internal.telephony.CARRIER_SIGNAL_REDIRECTED", "com.android.internal.telephony.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED", "com.android.internal.telephony.CARRIER_SIGNAL_RESET", "com.android.internal.telephony.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE" }));
  private boolean mDefaultNetworkAvail;
  private final LocalLog mErrorLocalLog = new LocalLog(20);
  private ConnectivityManager.NetworkCallback mNetworkCallback;
  private final Phone mPhone;
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousIntent = paramAnonymousIntent.getAction();
      paramAnonymousContext = CarrierSignalAgent.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CarrierSignalAgent receiver action: ");
      localStringBuilder.append(paramAnonymousIntent);
      paramAnonymousContext.log(localStringBuilder.toString());
      if (paramAnonymousIntent.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
        CarrierSignalAgent.this.loadCarrierConfig();
      }
    }
  };
  
  public CarrierSignalAgent(Phone paramPhone)
  {
    mPhone = paramPhone;
    loadCarrierConfig();
    mPhone.getContext().registerReceiver(mReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    mPhone.getCarrierActionAgent().registerForCarrierAction(3, this, 0, null, false);
  }
  
  private void broadcast(Intent paramIntent, Set<ComponentName> paramSet, boolean paramBoolean)
  {
    PackageManager localPackageManager = mPhone.getContext().getPackageManager();
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (ComponentName)localIterator.next();
      paramSet = new Intent(paramIntent);
      paramSet.setComponent((ComponentName)localObject);
      if ((paramBoolean) && (localPackageManager.queryBroadcastReceivers(paramSet, 65536).isEmpty()))
      {
        paramIntent = new StringBuilder();
        paramIntent.append("Carrier signal receivers are configured but unavailable: ");
        paramIntent.append(paramSet.getComponent());
        loge(paramIntent.toString());
        return;
      }
      if ((!paramBoolean) && (!localPackageManager.queryBroadcastReceivers(paramSet, 65536).isEmpty()))
      {
        paramIntent = new StringBuilder();
        paramIntent.append("Runtime signals shouldn't be configured in Manifest: ");
        paramIntent.append(paramSet.getComponent());
        loge(paramIntent.toString());
        return;
      }
      paramSet.putExtra("subscription", mPhone.getSubId());
      paramSet.addFlags(268435456);
      if (!paramBoolean) {
        paramSet.setFlags(16);
      }
      try
      {
        mPhone.getContext().sendBroadcast(paramSet);
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Sending signal ");
        ((StringBuilder)localObject).append(paramSet.getAction());
        if (paramSet.getComponent() != null)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append(" to the carrier signal receiver: ");
          localStringBuilder.append(paramSet.getComponent());
          paramSet = localStringBuilder.toString();
        }
        else
        {
          paramSet = "";
        }
        ((StringBuilder)localObject).append(paramSet);
        log(((StringBuilder)localObject).toString());
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        paramSet = new StringBuilder();
        paramSet.append("Send broadcast failed: ");
        paramSet.append(localActivityNotFoundException);
        loge(paramSet.toString());
      }
    }
  }
  
  private void loadCarrierConfig()
  {
    ??? = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    Object localObject2 = null;
    if (??? != null) {
      localObject2 = ((CarrierConfigManager)???).getConfig();
    }
    if (localObject2 != null) {
      synchronized (mCachedWakeSignalConfigs)
      {
        log("Loading carrier config: carrier_app_wake_signal_config");
        Map localMap = parseAndCache(((PersistableBundle)localObject2).getStringArray("carrier_app_wake_signal_config"));
        if ((!mCachedWakeSignalConfigs.isEmpty()) && (!localMap.equals(mCachedWakeSignalConfigs)))
        {
          if (VDBG) {
            log("carrier config changed, reset receivers from old config");
          }
          mPhone.getCarrierActionAgent().sendEmptyMessage(2);
        }
        mCachedWakeSignalConfigs = localMap;
        synchronized (mCachedNoWakeSignalConfigs)
        {
          log("Loading carrier config: carrier_app_no_wake_signal_config");
          localObject2 = parseAndCache(((PersistableBundle)localObject2).getStringArray("carrier_app_no_wake_signal_config"));
          if ((!mCachedNoWakeSignalConfigs.isEmpty()) && (!((Map)localObject2).equals(mCachedNoWakeSignalConfigs)))
          {
            if (VDBG) {
              log("carrier config changed, reset receivers from old config");
            }
            mPhone.getCarrierActionAgent().sendEmptyMessage(2);
          }
          mCachedNoWakeSignalConfigs = ((Map)localObject2);
        }
      }
    }
  }
  
  private void log(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.d(str, localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    mErrorLocalLog.log(paramString);
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.e(str, localStringBuilder.toString());
  }
  
  private void logv(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.v(str, localStringBuilder.toString());
  }
  
  private Map<String, Set<ComponentName>> parseAndCache(String[] paramArrayOfString)
  {
    HashMap localHashMap = new HashMap();
    if (!ArrayUtils.isEmpty(paramArrayOfString))
    {
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        Object localObject1 = paramArrayOfString[j];
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          Object localObject2 = ((String)localObject1).trim().split("\\s*:\\s*", 2);
          if (localObject2.length == 2)
          {
            ComponentName localComponentName = ComponentName.unflattenFromString(localObject2[0]);
            if (localComponentName == null)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Invalid component name: ");
              ((StringBuilder)localObject1).append(localObject2[0]);
              loge(((StringBuilder)localObject1).toString());
            }
            else
            {
              for (String str : localObject2[1].split("\\s*,\\s*")) {
                if (!mCarrierSignalList.contains(str))
                {
                  localObject2 = new StringBuilder();
                  ((StringBuilder)localObject2).append("Invalid signal name: ");
                  ((StringBuilder)localObject2).append(str);
                  loge(((StringBuilder)localObject2).toString());
                }
                else
                {
                  localObject1 = (Set)localHashMap.get(str);
                  localObject2 = localObject1;
                  if (localObject1 == null)
                  {
                    localObject2 = new HashSet();
                    localHashMap.put(str, localObject2);
                  }
                  ((Set)localObject2).add(localComponentName);
                  if (VDBG)
                  {
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append("Add config {signal: ");
                    ((StringBuilder)localObject2).append(str);
                    ((StringBuilder)localObject2).append(" componentName: ");
                    ((StringBuilder)localObject2).append(localComponentName);
                    ((StringBuilder)localObject2).append("}");
                    logv(((StringBuilder)localObject2).toString());
                  }
                }
              }
            }
          }
          else
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("invalid config format: ");
            ((StringBuilder)localObject2).append((String)localObject1);
            loge(((StringBuilder)localObject2).toString());
          }
        }
      }
    }
    return localHashMap;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.println("mCachedWakeSignalConfigs:");
    localIndentingPrintWriter.increaseIndent();
    Iterator localIterator = mCachedWakeSignalConfigs.entrySet().iterator();
    Object localObject1;
    while (localIterator.hasNext())
    {
      localObject1 = (Map.Entry)localIterator.next();
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("signal: ");
      ((StringBuilder)localObject2).append((String)((Map.Entry)localObject1).getKey());
      ((StringBuilder)localObject2).append(" componentName list: ");
      ((StringBuilder)localObject2).append(((Map.Entry)localObject1).getValue());
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
    }
    localIndentingPrintWriter.decreaseIndent();
    paramPrintWriter.println("mCachedNoWakeSignalConfigs:");
    localIndentingPrintWriter.increaseIndent();
    localIterator = mCachedNoWakeSignalConfigs.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localObject2 = (Map.Entry)localIterator.next();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("signal: ");
      ((StringBuilder)localObject1).append((String)((Map.Entry)localObject2).getKey());
      ((StringBuilder)localObject1).append(" componentName list: ");
      ((StringBuilder)localObject1).append(((Map.Entry)localObject2).getValue());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
    }
    localIndentingPrintWriter.decreaseIndent();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("mDefaultNetworkAvail: ");
    ((StringBuilder)localObject2).append(mDefaultNetworkAvail);
    paramPrintWriter.println(((StringBuilder)localObject2).toString());
    paramPrintWriter.println("error log:");
    localIndentingPrintWriter.increaseIndent();
    mErrorLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (what == 0)
    {
      paramMessage = (AsyncResult)obj;
      if (exception != null)
      {
        String str = LOG_TAG;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Register default network exception: ");
        ((StringBuilder)localObject).append(exception);
        Rlog.e(str, ((StringBuilder)localObject).toString());
        return;
      }
      Object localObject = ConnectivityManager.from(mPhone.getContext());
      if (((Boolean)result).booleanValue())
      {
        mNetworkCallback = new ConnectivityManager.NetworkCallback()
        {
          public void onAvailable(Network paramAnonymousNetwork)
          {
            if (!mDefaultNetworkAvail)
            {
              CarrierSignalAgent localCarrierSignalAgent = CarrierSignalAgent.this;
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("Default network available: ");
              localStringBuilder.append(paramAnonymousNetwork);
              localCarrierSignalAgent.log(localStringBuilder.toString());
              paramAnonymousNetwork = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
              paramAnonymousNetwork.putExtra("defaultNetworkAvailable", true);
              notifyCarrierSignalReceivers(paramAnonymousNetwork);
              CarrierSignalAgent.access$202(CarrierSignalAgent.this, true);
            }
          }
          
          public void onLost(Network paramAnonymousNetwork)
          {
            CarrierSignalAgent localCarrierSignalAgent = CarrierSignalAgent.this;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Default network lost: ");
            localStringBuilder.append(paramAnonymousNetwork);
            localCarrierSignalAgent.log(localStringBuilder.toString());
            paramAnonymousNetwork = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_DEFAULT_NETWORK_AVAILABLE");
            paramAnonymousNetwork.putExtra("defaultNetworkAvailable", false);
            notifyCarrierSignalReceivers(paramAnonymousNetwork);
            CarrierSignalAgent.access$202(CarrierSignalAgent.this, false);
          }
        };
        ((ConnectivityManager)localObject).registerDefaultNetworkCallback(mNetworkCallback, mPhone);
        log("Register default network");
      }
      else if (mNetworkCallback != null)
      {
        ((ConnectivityManager)localObject).unregisterNetworkCallback(mNetworkCallback);
        mNetworkCallback = null;
        mDefaultNetworkAvail = false;
        log("unregister default network");
      }
    }
  }
  
  public boolean hasRegisteredReceivers(String paramString)
  {
    boolean bool;
    if ((!mCachedWakeSignalConfigs.containsKey(paramString)) && (!mCachedNoWakeSignalConfigs.containsKey(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void notifyCarrierSignalReceivers(Intent paramIntent)
  {
    synchronized (mCachedWakeSignalConfigs)
    {
      Set localSet = (Set)mCachedWakeSignalConfigs.get(paramIntent.getAction());
      if (!ArrayUtils.isEmpty(localSet)) {
        broadcast(paramIntent, localSet, true);
      }
      synchronized (mCachedNoWakeSignalConfigs)
      {
        localSet = (Set)mCachedNoWakeSignalConfigs.get(paramIntent.getAction());
        if (!ArrayUtils.isEmpty(localSet)) {
          broadcast(paramIntent, localSet, false);
        }
        return;
      }
    }
  }
}
