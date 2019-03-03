package org.codeaurora.ims.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class QtiCarrierConfigHelper
{
  private static int PHONE_COUNT = TelephonyManager.getDefault().getPhoneCount();
  static final String TAG = QtiCarrierConfigHelper.class.getSimpleName();
  CarrierConfigManager mCarrierConfigManager;
  Map<Integer, PersistableBundle> mConfigsMap = new ConcurrentHashMap();
  private Context mContext;
  private AtomicBoolean mInitialized = new AtomicBoolean(false);
  private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener = new SubscriptionManager.OnSubscriptionsChangedListener()
  {
    public void onSubscriptionsChanged()
    {
      if (mSubscriptionManager != null)
      {
        Object localObject = mSubscriptionManager.getActiveSubscriptionInfoList();
        if (localObject != null)
        {
          localObject = ((List)localObject).iterator();
          while (((Iterator)localObject).hasNext())
          {
            SubscriptionInfo localSubscriptionInfo = (SubscriptionInfo)((Iterator)localObject).next();
            String str = QtiCarrierConfigHelper.TAG;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Reload carrier configs on sub Id due sub changed: ");
            localStringBuilder.append(localSubscriptionInfo.getSubscriptionId());
            Log.d(str, localStringBuilder.toString());
            loadConfigsForSubInfo(localSubscriptionInfo);
          }
        }
      }
    }
  };
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ((paramAnonymousIntent != null) && (paramAnonymousIntent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")))
      {
        int i = paramAnonymousIntent.getIntExtra("subscription", -1);
        if (mSubscriptionManager != null)
        {
          paramAnonymousContext = mSubscriptionManager.getActiveSubscriptionInfo(i);
          if (paramAnonymousContext != null)
          {
            paramAnonymousIntent = QtiCarrierConfigHelper.TAG;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Reload carrier configs on sub Id: ");
            localStringBuilder.append(i);
            Log.d(paramAnonymousIntent, localStringBuilder.toString());
            loadConfigsForSubInfo(paramAnonymousContext);
          }
          else
          {
            int j = paramAnonymousIntent.getIntExtra("phone", -1);
            if ((mCarrierConfigManager != null) && (mCarrierConfigManager.getConfigForSubId(i) == null))
            {
              mConfigsMap.remove(Integer.valueOf(j));
              paramAnonymousContext = QtiCarrierConfigHelper.TAG;
              paramAnonymousIntent = new StringBuilder();
              paramAnonymousIntent.append("Clear carrier configs on phone Id: ");
              paramAnonymousIntent.append(j);
              Log.d(paramAnonymousContext, paramAnonymousIntent.toString());
            }
          }
        }
      }
    }
  };
  SubscriptionManager mSubscriptionManager;
  
  private QtiCarrierConfigHelper() {}
  
  public static QtiCarrierConfigHelper getInstance()
  {
    return SingletonHolder.sInstance;
  }
  
  private void sanityCheckConfigsLoaded(Context paramContext, int paramInt)
  {
    if ((paramContext != null) && (mInitialized.compareAndSet(false, true))) {
      setup(paramContext);
    }
  }
  
  public boolean getBoolean(int paramInt, String paramString)
  {
    if (!isValidPhoneId(paramInt))
    {
      localObject = TAG;
      paramString = new StringBuilder();
      paramString.append("Invalid phone ID: ");
      paramString.append(paramInt);
      Log.d((String)localObject, paramString.toString());
      return false;
    }
    Object localObject = (PersistableBundle)mConfigsMap.get(Integer.valueOf(paramInt));
    if (localObject != null) {
      return ((PersistableBundle)localObject).getBoolean(paramString, false);
    }
    if (!mInitialized.get())
    {
      Log.d(TAG, "WARNING, Don't set up yet.");
      return false;
    }
    paramString = TAG;
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("WARNING, no carrier configs on phone Id: ");
    ((StringBuilder)localObject).append(paramInt);
    Log.d(paramString, ((StringBuilder)localObject).toString());
    return false;
  }
  
  public boolean getBoolean(Context paramContext, int paramInt, String paramString)
  {
    if (!isValidPhoneId(paramInt))
    {
      paramContext = TAG;
      paramString = new StringBuilder();
      paramString.append("Invalid phone ID: ");
      paramString.append(paramInt);
      Log.d(paramContext, paramString.toString());
      return false;
    }
    sanityCheckConfigsLoaded(paramContext, paramInt);
    paramContext = (PersistableBundle)mConfigsMap.get(Integer.valueOf(paramInt));
    if (paramContext != null) {
      return paramContext.getBoolean(paramString, false);
    }
    paramContext = TAG;
    paramString = new StringBuilder();
    paramString.append("WARNING, no carrier configs on phone Id: ");
    paramString.append(paramInt);
    Log.d(paramContext, paramString.toString());
    return false;
  }
  
  public boolean isValidPhoneId(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < PHONE_COUNT)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void loadConfigsForSubInfo(SubscriptionInfo paramSubscriptionInfo)
  {
    if ((paramSubscriptionInfo != null) && (mCarrierConfigManager != null))
    {
      Object localObject = mCarrierConfigManager.getConfigForSubId(paramSubscriptionInfo.getSubscriptionId());
      String str;
      if (localObject != null)
      {
        str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Load carrier configs on sub Id: ");
        localStringBuilder.append(paramSubscriptionInfo.getSubscriptionId());
        localStringBuilder.append(" slot Id: ");
        localStringBuilder.append(paramSubscriptionInfo.getSimSlotIndex());
        Log.d(str, localStringBuilder.toString());
        mConfigsMap.put(Integer.valueOf(paramSubscriptionInfo.getSimSlotIndex()), localObject);
      }
      else
      {
        str = TAG;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("No configs on sub Id: ");
        ((StringBuilder)localObject).append(paramSubscriptionInfo.getSubscriptionId());
        Log.d(str, ((StringBuilder)localObject).toString());
        mConfigsMap.put(Integer.valueOf(paramSubscriptionInfo.getSimSlotIndex()), null);
      }
    }
  }
  
  public void setup(Context paramContext)
  {
    if (paramContext == null) {
      return;
    }
    mContext = paramContext.getApplicationContext();
    if (mContext != null)
    {
      mInitialized.set(true);
      mSubscriptionManager = ((SubscriptionManager)mContext.getSystemService("telephony_subscription_service"));
      mCarrierConfigManager = ((CarrierConfigManager)mContext.getSystemService("carrier_config"));
      paramContext = mSubscriptionManager.getActiveSubscriptionInfoList();
      if (paramContext != null)
      {
        paramContext = paramContext.iterator();
        while (paramContext.hasNext()) {
          loadConfigsForSubInfo((SubscriptionInfo)paramContext.next());
        }
      }
      paramContext = new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED");
      mContext.registerReceiver(mReceiver, paramContext);
      mSubscriptionManager.addOnSubscriptionsChangedListener(mOnSubscriptionsChangeListener);
    }
  }
  
  public void teardown()
  {
    mConfigsMap.clear();
    mInitialized.set(false);
    if (mContext != null)
    {
      mContext.unregisterReceiver(mReceiver);
      if (mSubscriptionManager != null) {
        mSubscriptionManager.removeOnSubscriptionsChangedListener(mOnSubscriptionsChangeListener);
      }
    }
  }
  
  private static class SingletonHolder
  {
    public static final QtiCarrierConfigHelper sInstance = new QtiCarrierConfigHelper(null);
    
    private SingletonHolder() {}
  }
}
