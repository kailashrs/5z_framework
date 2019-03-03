package com.android.internal.telephony.dataconnection;

import android.content.Context;
import android.net.NetworkCapabilities;
import android.net.NetworkFactory;
import android.net.NetworkRequest;
import android.net.StringNetworkSpecifier;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.util.LocalLog;
import com.android.internal.telephony.PhoneSwitcher;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.SubscriptionMonitor;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class TelephonyNetworkFactory
  extends NetworkFactory
{
  protected static final boolean DBG = true;
  private static final int EVENT_ACTIVE_PHONE_SWITCH = 1;
  private static final int EVENT_DEFAULT_SUBSCRIPTION_CHANGED = 3;
  private static final int EVENT_NETWORK_RELEASE = 5;
  private static final int EVENT_NETWORK_REQUEST = 4;
  private static final int EVENT_SUBSCRIPTION_CHANGED = 2;
  private static final boolean RELEASE = false;
  private static final boolean REQUEST = true;
  private static final int REQUEST_LOG_SIZE = 40;
  private static final int TELEPHONY_NETWORK_SCORE = 50;
  public final String LOG_TAG;
  private Context mContext;
  private final DcTracker mDcTracker;
  private final HashMap<NetworkRequest, LocalLog> mDefaultRequests = new HashMap();
  private final Handler mInternalHandler;
  private boolean mIsActive;
  private boolean mIsDefault;
  private int mPhoneId;
  private final PhoneSwitcher mPhoneSwitcher;
  private final HashMap<NetworkRequest, LocalLog> mSpecificRequests = new HashMap();
  private final SubscriptionController mSubscriptionController;
  private int mSubscriptionId;
  private final SubscriptionMonitor mSubscriptionMonitor;
  
  public TelephonyNetworkFactory(PhoneSwitcher paramPhoneSwitcher, SubscriptionController paramSubscriptionController, SubscriptionMonitor paramSubscriptionMonitor, Looper paramLooper, Context paramContext, int paramInt, DcTracker paramDcTracker)
  {
    super(paramLooper, paramContext, localStringBuilder.toString(), null);
    mContext = paramContext;
    mInternalHandler = new InternalHandler(paramLooper);
    setCapabilityFilter(makeNetworkFilter(paramSubscriptionController, paramInt));
    setScoreFilter(50);
    mPhoneSwitcher = paramPhoneSwitcher;
    mSubscriptionController = paramSubscriptionController;
    mSubscriptionMonitor = paramSubscriptionMonitor;
    mPhoneId = paramInt;
    paramPhoneSwitcher = new StringBuilder();
    paramPhoneSwitcher.append("TelephonyNetworkFactory[");
    paramPhoneSwitcher.append(paramInt);
    paramPhoneSwitcher.append("]");
    LOG_TAG = paramPhoneSwitcher.toString();
    mDcTracker = paramDcTracker;
    mIsActive = false;
    mPhoneSwitcher.registerForActivePhoneSwitch(mPhoneId, mInternalHandler, 1, null);
    mSubscriptionId = -1;
    mSubscriptionMonitor.registerForSubscriptionChanged(mPhoneId, mInternalHandler, 2, null);
    mIsDefault = false;
    mSubscriptionMonitor.registerForDefaultDataSubscriptionChanged(mPhoneId, mInternalHandler, 3, null);
    register();
  }
  
  private void applyRequests(HashMap<NetworkRequest, LocalLog> paramHashMap, boolean paramBoolean, String paramString)
  {
    Iterator localIterator = paramHashMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      NetworkRequest localNetworkRequest = (NetworkRequest)localIterator.next();
      LocalLog localLocalLog = (LocalLog)paramHashMap.get(localNetworkRequest);
      localLocalLog.log(paramString);
      if (paramBoolean == true) {
        mDcTracker.requestNetwork(localNetworkRequest, localLocalLog);
      } else {
        mDcTracker.releaseNetwork(localNetworkRequest, localLocalLog);
      }
    }
  }
  
  private NetworkCapabilities makeNetworkFilter(int paramInt)
  {
    NetworkCapabilities localNetworkCapabilities = new NetworkCapabilities();
    localNetworkCapabilities.addTransportType(0);
    localNetworkCapabilities.addCapability(0);
    localNetworkCapabilities.addCapability(1);
    localNetworkCapabilities.addCapability(2);
    localNetworkCapabilities.addCapability(3);
    localNetworkCapabilities.addCapability(4);
    localNetworkCapabilities.addCapability(5);
    localNetworkCapabilities.addCapability(7);
    localNetworkCapabilities.addCapability(8);
    localNetworkCapabilities.addCapability(9);
    localNetworkCapabilities.addCapability(10);
    localNetworkCapabilities.addCapability(13);
    localNetworkCapabilities.addCapability(12);
    localNetworkCapabilities.setNetworkSpecifier(new StringNetworkSpecifier(String.valueOf(paramInt)));
    return localNetworkCapabilities;
  }
  
  private NetworkCapabilities makeNetworkFilter(SubscriptionController paramSubscriptionController, int paramInt)
  {
    return makeNetworkFilter(paramSubscriptionController.getSubIdUsingPhoneId(paramInt));
  }
  
  private void onActivePhoneSwitch()
  {
    mIsActive = mPhoneSwitcher.isPhoneActive(mPhoneId);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("onActivePhoneSwitch(");
    ((StringBuilder)localObject).append(mIsActive);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(mIsDefault);
    ((StringBuilder)localObject).append(")");
    localObject = ((StringBuilder)localObject).toString();
    log((String)localObject);
    if (mIsDefault) {
      applyRequests(mDefaultRequests, mIsActive, (String)localObject);
    }
    applyRequests(mSpecificRequests, mIsActive, (String)localObject);
  }
  
  private void onDefaultChange()
  {
    boolean bool;
    if (mSubscriptionController.getDefaultDataSubId() == mSubscriptionId) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != mIsDefault)
    {
      mIsDefault = bool;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onDefaultChange(");
      ((StringBuilder)localObject).append(mIsActive);
      ((StringBuilder)localObject).append(",");
      ((StringBuilder)localObject).append(mIsDefault);
      ((StringBuilder)localObject).append(")");
      localObject = ((StringBuilder)localObject).toString();
      log((String)localObject);
      if (!mIsActive) {
        return;
      }
      if (mSubscriptionController.getActiveSubInfoCount(mContext.getOpPackageName()) == 1) {
        applyRequests(mDefaultRequests, mIsDefault, (String)localObject);
      } else if (!mIsDefault) {
        applyRequests(mDefaultRequests, false, (String)localObject);
      }
    }
  }
  
  private void onNeedNetworkFor(Message paramMessage)
  {
    NetworkRequest localNetworkRequest = (NetworkRequest)obj;
    boolean bool = false;
    Object localObject;
    if (networkCapabilities.getNetworkSpecifier() == null)
    {
      localObject = (LocalLog)mDefaultRequests.get(localNetworkRequest);
      paramMessage = (Message)localObject;
      if (localObject == null)
      {
        paramMessage = new LocalLog(40);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("created for ");
        ((StringBuilder)localObject).append(localNetworkRequest);
        paramMessage.log(((StringBuilder)localObject).toString());
        mDefaultRequests.put(localNetworkRequest, paramMessage);
        bool = mIsDefault;
      }
    }
    else
    {
      localObject = (LocalLog)mSpecificRequests.get(localNetworkRequest);
      paramMessage = (Message)localObject;
      if (localObject == null)
      {
        paramMessage = new LocalLog(40);
        mSpecificRequests.put(localNetworkRequest, paramMessage);
        bool = true;
      }
    }
    if ((mIsActive) && (bool))
    {
      paramMessage.log("onNeedNetworkFor");
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onNeedNetworkFor");
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(localNetworkRequest);
      log(((StringBuilder)localObject).toString());
      mDcTracker.requestNetwork(localNetworkRequest, paramMessage);
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("not acting - isApp=");
      ((StringBuilder)localObject).append(bool);
      ((StringBuilder)localObject).append(", isAct=");
      ((StringBuilder)localObject).append(mIsActive);
      localObject = ((StringBuilder)localObject).toString();
      paramMessage.log((String)localObject);
      paramMessage = new StringBuilder();
      paramMessage.append((String)localObject);
      paramMessage.append(" ");
      paramMessage.append(localNetworkRequest);
      log(paramMessage.toString());
    }
  }
  
  private void onReleaseNetworkFor(Message paramMessage)
  {
    NetworkRequest localNetworkRequest = (NetworkRequest)obj;
    paramMessage = networkCapabilities.getNetworkSpecifier();
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramMessage == null)
    {
      paramMessage = (LocalLog)mDefaultRequests.remove(localNetworkRequest);
      bool1 = bool2;
      if (paramMessage != null)
      {
        bool1 = bool2;
        if (mIsDefault) {
          bool1 = true;
        }
      }
    }
    else
    {
      paramMessage = (LocalLog)mSpecificRequests.remove(localNetworkRequest);
      if (paramMessage != null) {
        bool1 = true;
      }
    }
    Object localObject;
    if ((mIsActive) && (bool1))
    {
      paramMessage.log("onReleaseNetworkFor");
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onReleaseNetworkFor");
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(localNetworkRequest);
      log(((StringBuilder)localObject).toString());
      mDcTracker.releaseNetwork(localNetworkRequest, paramMessage);
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("not releasing - isApp=");
      ((StringBuilder)localObject).append(bool1);
      ((StringBuilder)localObject).append(", isAct=");
      ((StringBuilder)localObject).append(mIsActive);
      localObject = ((StringBuilder)localObject).toString();
      paramMessage.log((String)localObject);
      paramMessage = new StringBuilder();
      paramMessage.append((String)localObject);
      paramMessage.append(" ");
      paramMessage.append(localNetworkRequest);
      log(paramMessage.toString());
    }
  }
  
  private void onSubIdChange()
  {
    int i = mSubscriptionController.getSubIdUsingPhoneId(mPhoneId);
    if (mSubscriptionId != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onSubIdChange ");
      localStringBuilder.append(mSubscriptionId);
      localStringBuilder.append("->");
      localStringBuilder.append(i);
      log(localStringBuilder.toString());
      mSubscriptionId = i;
      setCapabilityFilter(makeNetworkFilter(mSubscriptionId));
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(LOG_TAG);
    ((StringBuilder)localObject).append(" mSubId=");
    ((StringBuilder)localObject).append(mSubscriptionId);
    ((StringBuilder)localObject).append(" mIsActive=");
    ((StringBuilder)localObject).append(mIsActive);
    ((StringBuilder)localObject).append(" mIsDefault=");
    ((StringBuilder)localObject).append(mIsDefault);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    paramPrintWriter.println("Default Requests:");
    paramPrintWriter.increaseIndent();
    localObject = mDefaultRequests.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      NetworkRequest localNetworkRequest = (NetworkRequest)((Iterator)localObject).next();
      paramPrintWriter.println(localNetworkRequest);
      paramPrintWriter.increaseIndent();
      ((LocalLog)mDefaultRequests.get(localNetworkRequest)).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.decreaseIndent();
    }
    paramPrintWriter.decreaseIndent();
  }
  
  protected void log(String paramString)
  {
    Rlog.d(LOG_TAG, paramString);
  }
  
  public void needNetworkFor(NetworkRequest paramNetworkRequest, int paramInt)
  {
    Message localMessage = mInternalHandler.obtainMessage(4);
    obj = paramNetworkRequest;
    localMessage.sendToTarget();
  }
  
  public void releaseNetworkFor(NetworkRequest paramNetworkRequest)
  {
    Message localMessage = mInternalHandler.obtainMessage(5);
    obj = paramNetworkRequest;
    localMessage.sendToTarget();
  }
  
  private class InternalHandler
    extends Handler
  {
    public InternalHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 5: 
        TelephonyNetworkFactory.this.onReleaseNetworkFor(paramMessage);
        break;
      case 4: 
        TelephonyNetworkFactory.this.onNeedNetworkFor(paramMessage);
        break;
      case 3: 
        TelephonyNetworkFactory.this.onDefaultChange();
        break;
      case 2: 
        TelephonyNetworkFactory.this.onSubIdChange();
        break;
      case 1: 
        TelephonyNetworkFactory.this.onActivePhoneSwitch();
      }
    }
  }
}
