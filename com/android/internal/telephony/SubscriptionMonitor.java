package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.util.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class SubscriptionMonitor
{
  private static final String LOG_TAG = "SubscriptionMonitor";
  private static final int MAX_LOGLINES = 100;
  private static final boolean VDBG = true;
  private final Context mContext;
  private int mDefaultDataPhoneId;
  private final RegistrantList[] mDefaultDataSubChangedRegistrants;
  private int mDefaultDataSubId;
  private final BroadcastReceiver mDefaultDataSubscriptionChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context arg1, Intent paramAnonymousIntent)
    {
      int i = mSubscriptionController.getDefaultDataSubId();
      synchronized (mLock)
      {
        if (mDefaultDataSubId != i)
        {
          Object localObject = SubscriptionMonitor.this;
          paramAnonymousIntent = new java/lang/StringBuilder;
          paramAnonymousIntent.<init>();
          paramAnonymousIntent.append("Default changed ");
          paramAnonymousIntent.append(mDefaultDataSubId);
          paramAnonymousIntent.append("->");
          paramAnonymousIntent.append(i);
          ((SubscriptionMonitor)localObject).log(paramAnonymousIntent.toString());
          int j = mDefaultDataPhoneId;
          SubscriptionMonitor.access$502(SubscriptionMonitor.this, i);
          int k = mSubscriptionController.getPhoneId(-1);
          int m = 0;
          int n = k;
          int i1;
          if (i != -1) {
            for (i1 = 0;; i1++)
            {
              n = k;
              if (i1 >= mPhoneSubId.length) {
                break;
              }
              if (mPhoneSubId[i1] == i)
              {
                localObject = SubscriptionMonitor.this;
                paramAnonymousIntent = new java/lang/StringBuilder;
                paramAnonymousIntent.<init>();
                paramAnonymousIntent.append("newDefaultDataPhoneId=");
                paramAnonymousIntent.append(i1);
                ((SubscriptionMonitor)localObject).log(paramAnonymousIntent.toString());
                n = i1;
                break;
              }
            }
          }
          if (n != j)
          {
            paramAnonymousIntent = SubscriptionMonitor.this;
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("Default phoneId changed ");
            ((StringBuilder)localObject).append(j);
            ((StringBuilder)localObject).append("->");
            ((StringBuilder)localObject).append(n);
            ((StringBuilder)localObject).append(", ");
            if (SubscriptionMonitor.this.invalidPhoneId(j)) {
              i1 = 0;
            } else {
              i1 = mDefaultDataSubChangedRegistrants[j].size();
            }
            ((StringBuilder)localObject).append(i1);
            ((StringBuilder)localObject).append(",");
            if (SubscriptionMonitor.this.invalidPhoneId(n)) {
              i1 = m;
            } else {
              i1 = mDefaultDataSubChangedRegistrants[n].size();
            }
            ((StringBuilder)localObject).append(i1);
            ((StringBuilder)localObject).append(" registrants");
            paramAnonymousIntent.log(((StringBuilder)localObject).toString());
            SubscriptionMonitor.access$702(SubscriptionMonitor.this, n);
            if (!SubscriptionMonitor.this.invalidPhoneId(j)) {
              mDefaultDataSubChangedRegistrants[j].notifyRegistrants();
            }
            if (!SubscriptionMonitor.this.invalidPhoneId(n)) {
              mDefaultDataSubChangedRegistrants[n].notifyRegistrants();
            }
          }
        }
        return;
      }
    }
  };
  private final LocalLog mLocalLog = new LocalLog(100);
  private final Object mLock = new Object();
  private final int[] mPhoneSubId;
  private final SubscriptionController mSubscriptionController;
  private final IOnSubscriptionsChangedListener mSubscriptionsChangedListener = new IOnSubscriptionsChangedListener.Stub()
  {
    public void onSubscriptionsChanged()
    {
      Object localObject1 = mLock;
      int i = -1;
      int j = 0;
      try
      {
        while (j < mPhoneSubId.length)
        {
          int k = mSubscriptionController.getSubIdUsingPhoneId(j);
          int m = mPhoneSubId[j];
          if (m != k)
          {
            Object localObject2 = SubscriptionMonitor.this;
            Object localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Phone[");
            ((StringBuilder)localObject3).append(j);
            ((StringBuilder)localObject3).append("] subId changed ");
            ((StringBuilder)localObject3).append(m);
            ((StringBuilder)localObject3).append("->");
            ((StringBuilder)localObject3).append(k);
            ((StringBuilder)localObject3).append(", ");
            ((StringBuilder)localObject3).append(mSubscriptionsChangedRegistrants[j].size());
            ((StringBuilder)localObject3).append(" registrants");
            ((SubscriptionMonitor)localObject2).log(((StringBuilder)localObject3).toString());
            mPhoneSubId[j] = k;
            mSubscriptionsChangedRegistrants[j].notifyRegistrants();
            if (mDefaultDataSubId == -1) {
              break label332;
            }
            if ((k == mDefaultDataSubId) || (m == mDefaultDataSubId))
            {
              localObject3 = SubscriptionMonitor.this;
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("mDefaultDataSubId = ");
              ((StringBuilder)localObject2).append(mDefaultDataSubId);
              ((StringBuilder)localObject2).append(", ");
              ((StringBuilder)localObject2).append(mDefaultDataSubChangedRegistrants[j].size());
              ((StringBuilder)localObject2).append(" registrants");
              ((SubscriptionMonitor)localObject3).log(((StringBuilder)localObject2).toString());
              mDefaultDataSubChangedRegistrants[j].notifyRegistrants();
            }
          }
          if (k == mDefaultDataSubId) {
            i = j;
          }
          label332:
          j++;
        }
        SubscriptionMonitor.access$702(SubscriptionMonitor.this, i);
        return;
      }
      finally {}
    }
  };
  private final RegistrantList[] mSubscriptionsChangedRegistrants;
  
  @VisibleForTesting
  public SubscriptionMonitor()
  {
    mSubscriptionsChangedRegistrants = null;
    mDefaultDataSubChangedRegistrants = null;
    mSubscriptionController = null;
    mContext = null;
    mPhoneSubId = null;
  }
  
  public SubscriptionMonitor(ITelephonyRegistry paramITelephonyRegistry, Context paramContext, SubscriptionController paramSubscriptionController, int paramInt)
  {
    mSubscriptionController = paramSubscriptionController;
    mContext = paramContext;
    mSubscriptionsChangedRegistrants = new RegistrantList[paramInt];
    mDefaultDataSubChangedRegistrants = new RegistrantList[paramInt];
    mPhoneSubId = new int[paramInt];
    mDefaultDataSubId = mSubscriptionController.getDefaultDataSubId();
    mDefaultDataPhoneId = mSubscriptionController.getPhoneId(mDefaultDataSubId);
    for (int i = 0; i < paramInt; i++)
    {
      mSubscriptionsChangedRegistrants[i] = new RegistrantList();
      mDefaultDataSubChangedRegistrants[i] = new RegistrantList();
      mPhoneSubId[i] = mSubscriptionController.getSubIdUsingPhoneId(i);
    }
    try
    {
      paramITelephonyRegistry.addOnSubscriptionsChangedListener(paramContext.getOpPackageName(), mSubscriptionsChangedListener);
    }
    catch (RemoteException paramITelephonyRegistry) {}
    mContext.registerReceiver(mDefaultDataSubscriptionChangedReceiver, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
  }
  
  private boolean invalidPhoneId(int paramInt)
  {
    return (paramInt < 0) || (paramInt >= mPhoneSubId.length);
  }
  
  private void log(String paramString)
  {
    Rlog.d("SubscriptionMonitor", paramString);
    mLocalLog.log(paramString);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    synchronized (mLock)
    {
      mLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      return;
    }
  }
  
  public void registerForDefaultDataSubscriptionChanged(int paramInt1, Handler paramHandler, int paramInt2, Object paramObject)
  {
    if (!invalidPhoneId(paramInt1))
    {
      paramHandler = new Registrant(paramHandler, paramInt2, paramObject);
      mDefaultDataSubChangedRegistrants[paramInt1].add(paramHandler);
      paramHandler.notifyRegistrant();
      return;
    }
    throw new IllegalArgumentException("Invalid PhoneId");
  }
  
  public void registerForSubscriptionChanged(int paramInt1, Handler paramHandler, int paramInt2, Object paramObject)
  {
    if (!invalidPhoneId(paramInt1))
    {
      paramHandler = new Registrant(paramHandler, paramInt2, paramObject);
      mSubscriptionsChangedRegistrants[paramInt1].add(paramHandler);
      paramHandler.notifyRegistrant();
      return;
    }
    throw new IllegalArgumentException("Invalid PhoneId");
  }
  
  public void unregisterForDefaultDataSubscriptionChanged(int paramInt, Handler paramHandler)
  {
    if (!invalidPhoneId(paramInt))
    {
      mDefaultDataSubChangedRegistrants[paramInt].remove(paramHandler);
      return;
    }
    throw new IllegalArgumentException("Invalid PhoneId");
  }
  
  public void unregisterForSubscriptionChanged(int paramInt, Handler paramHandler)
  {
    if (!invalidPhoneId(paramInt))
    {
      mSubscriptionsChangedRegistrants[paramInt].remove(paramHandler);
      return;
    }
    throw new IllegalArgumentException("Invalid PhoneId");
  }
}
