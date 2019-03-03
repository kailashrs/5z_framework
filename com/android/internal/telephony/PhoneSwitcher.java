package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkCapabilities;
import android.net.NetworkFactory;
import android.net.NetworkRequest;
import android.net.StringNetworkSpecifier;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.util.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.dataconnection.DcRequest;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PhoneSwitcher
  extends Handler
{
  protected static final int EVENT_DEFAULT_SUBSCRIPTION_CHANGED = 101;
  private static final int EVENT_EMERGENCY_TOGGLE = 105;
  protected static final int EVENT_OEM_HOOK_SERVICE_READY = 109;
  private static final int EVENT_RELEASE_NETWORK = 104;
  private static final int EVENT_REQUEST_NETWORK = 103;
  private static final int EVENT_RESEND_DATA_ALLOWED = 106;
  protected static final int EVENT_SUBSCRIPTION_CHANGED = 102;
  protected static final int EVENT_UNSOL_MAX_DATA_ALLOWED_CHANGED = 108;
  protected static final int EVENT_VOICE_CALL_ENDED = 107;
  protected static final String LOG_TAG = "PhoneSwitcher";
  private static final int MAX_LOCAL_LOG_LINES = 30;
  private static final boolean REQUESTS_CHANGED = true;
  protected static final boolean REQUESTS_UNCHANGED = false;
  protected static final boolean VDBG = false;
  protected final RegistrantList[] mActivePhoneRegistrants;
  protected final CommandsInterface[] mCommandsInterfaces;
  protected final Context mContext;
  private final BroadcastReceiver mDefaultDataChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      obtainMessage(101).sendToTarget();
    }
  };
  protected int mDefaultDataSubscription;
  private final LocalLog mLocalLog;
  protected int mMaxActivePhones;
  protected final int mNumPhones;
  protected final PhoneState[] mPhoneStates;
  protected final int[] mPhoneSubscriptions;
  private final Phone[] mPhones;
  protected final List<DcRequest> mPrioritizedDcRequests = new ArrayList();
  protected final SubscriptionController mSubscriptionController;
  private final IOnSubscriptionsChangedListener mSubscriptionsChangedListener = new IOnSubscriptionsChangedListener.Stub()
  {
    public void onSubscriptionsChanged()
    {
      obtainMessage(102).sendToTarget();
    }
  };
  
  public PhoneSwitcher(int paramInt1, int paramInt2, Context paramContext, SubscriptionController paramSubscriptionController, Looper paramLooper, ITelephonyRegistry paramITelephonyRegistry, CommandsInterface[] paramArrayOfCommandsInterface, Phone[] paramArrayOfPhone)
  {
    super(paramLooper);
    mContext = paramContext;
    mNumPhones = paramInt2;
    mPhones = paramArrayOfPhone;
    mPhoneSubscriptions = new int[paramInt2];
    mMaxActivePhones = paramInt1;
    mLocalLog = new LocalLog(30);
    mSubscriptionController = paramSubscriptionController;
    mActivePhoneRegistrants = new RegistrantList[paramInt2];
    mPhoneStates = new PhoneState[paramInt2];
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      mActivePhoneRegistrants[paramInt1] = new RegistrantList();
      mPhoneStates[paramInt1] = new PhoneState();
      if (mPhones[paramInt1] != null) {
        mPhones[paramInt1].registerForEmergencyCallToggle(this, 105, null);
      }
    }
    mCommandsInterfaces = paramArrayOfCommandsInterface;
    try
    {
      paramITelephonyRegistry.addOnSubscriptionsChangedListener(paramContext.getOpPackageName(), mSubscriptionsChangedListener);
    }
    catch (RemoteException paramSubscriptionController) {}
    mContext.registerReceiver(mDefaultDataChangedReceiver, new IntentFilter("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"));
    paramSubscriptionController = new NetworkCapabilities();
    paramSubscriptionController.addTransportType(0);
    paramSubscriptionController.addCapability(0);
    paramSubscriptionController.addCapability(1);
    paramSubscriptionController.addCapability(2);
    paramSubscriptionController.addCapability(3);
    paramSubscriptionController.addCapability(4);
    paramSubscriptionController.addCapability(5);
    paramSubscriptionController.addCapability(7);
    paramSubscriptionController.addCapability(8);
    paramSubscriptionController.addCapability(9);
    paramSubscriptionController.addCapability(10);
    paramSubscriptionController.addCapability(13);
    paramSubscriptionController.addCapability(12);
    paramSubscriptionController.setNetworkSpecifier(new MatchAllNetworkSpecifier());
    paramContext = new PhoneSwitcherNetworkRequestListener(paramLooper, paramContext, paramSubscriptionController, this);
    paramContext.setScoreFilter(101);
    paramContext.register();
    log("PhoneSwitcher started");
  }
  
  @VisibleForTesting
  public PhoneSwitcher(Looper paramLooper)
  {
    super(paramLooper);
    mMaxActivePhones = 0;
    mSubscriptionController = null;
    mPhoneSubscriptions = null;
    mCommandsInterfaces = null;
    mContext = null;
    mPhoneStates = null;
    mPhones = null;
    mLocalLog = null;
    mActivePhoneRegistrants = null;
    mNumPhones = 0;
  }
  
  private void onReleaseNetwork(NetworkRequest paramNetworkRequest)
  {
    paramNetworkRequest = new DcRequest(paramNetworkRequest, mContext);
    if (mPrioritizedDcRequests.remove(paramNetworkRequest)) {
      onEvaluate(true, "netReleased");
    }
  }
  
  private void onRequestNetwork(NetworkRequest paramNetworkRequest)
  {
    paramNetworkRequest = new DcRequest(paramNetworkRequest, mContext);
    if (!mPrioritizedDcRequests.contains(paramNetworkRequest))
    {
      mPrioritizedDcRequests.add(paramNetworkRequest);
      Collections.sort(mPrioritizedDcRequests);
      onEvaluate(true, "netRequest");
    }
  }
  
  private void validatePhoneId(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mNumPhones)) {
      return;
    }
    throw new IllegalArgumentException("Invalid PhoneId");
  }
  
  protected void activate(int paramInt)
  {
    PhoneState localPhoneState = mPhoneStates[paramInt];
    if (active == true) {
      return;
    }
    active = true;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("activate ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    lastRequested = System.currentTimeMillis();
    if (mNumPhones > 1) {
      mCommandsInterfaces[paramInt].setDataAllowed(true, null);
    }
    mActivePhoneRegistrants[paramInt].notifyRegistrants();
  }
  
  protected void deactivate(int paramInt)
  {
    PhoneState localPhoneState = mPhoneStates[paramInt];
    if (!active) {
      return;
    }
    active = false;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("deactivate ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    lastRequested = System.currentTimeMillis();
    if (mNumPhones > 1) {
      mCommandsInterfaces[paramInt].setDataAllowed(false, null);
    }
    mActivePhoneRegistrants[paramInt].notifyRegistrants();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    localIndentingPrintWriter.println("PhoneSwitcher:");
    Calendar localCalendar = Calendar.getInstance();
    for (int i = 0; i < mNumPhones; i++)
    {
      paramPrintWriter = mPhoneStates[i];
      localCalendar.setTimeInMillis(lastRequested);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PhoneId(");
      localStringBuilder.append(i);
      localStringBuilder.append(") active=");
      localStringBuilder.append(active);
      localStringBuilder.append(", lastRequest=");
      if (lastRequested == 0L) {
        paramPrintWriter = "never";
      } else {
        paramPrintWriter = String.format("%tm-%td %tH:%tM:%tS.%tL", new Object[] { localCalendar, localCalendar, localCalendar, localCalendar, localCalendar, localCalendar });
      }
      localStringBuilder.append(paramPrintWriter);
      localIndentingPrintWriter.println(localStringBuilder.toString());
    }
    localIndentingPrintWriter.increaseIndent();
    mLocalLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      break;
    case 106: 
      onResendDataAllowed(paramMessage);
      break;
    case 105: 
      onEvaluate(true, "emergencyToggle");
      break;
    case 104: 
      onReleaseNetwork((NetworkRequest)obj);
      break;
    case 103: 
      onRequestNetwork((NetworkRequest)obj);
      break;
    case 102: 
      onEvaluate(false, "subChanged");
      break;
    case 101: 
      onEvaluate(false, "defaultChanged");
    }
  }
  
  protected boolean isEmergency()
  {
    Phone[] arrayOfPhone = mPhones;
    int i = arrayOfPhone.length;
    int j = 0;
    while (j < i)
    {
      Phone localPhone = arrayOfPhone[j];
      if ((localPhone == null) || ((!localPhone.isInEcm()) && (!localPhone.isInEmergencyCall()))) {
        j++;
      } else {
        return true;
      }
    }
    return false;
  }
  
  public boolean isPhoneActive(int paramInt)
  {
    validatePhoneId(paramInt);
    return mPhoneStates[paramInt].active;
  }
  
  protected void log(String paramString)
  {
    Rlog.d("PhoneSwitcher", paramString);
    mLocalLog.log(paramString);
  }
  
  protected void onEvaluate(boolean paramBoolean, String paramString)
  {
    paramString = new StringBuilder(paramString);
    if (isEmergency())
    {
      log("onEvalute aborted due to Emergency");
      return;
    }
    int i = mSubscriptionController.getDefaultDataSubId();
    if (i != mDefaultDataSubscription)
    {
      paramString.append(" default ");
      paramString.append(mDefaultDataSubscription);
      paramString.append("->");
      paramString.append(i);
      mDefaultDataSubscription = i;
      paramBoolean = true;
    }
    int j = 0;
    for (i = 0; i < mNumPhones; i++)
    {
      int k = mSubscriptionController.getSubIdUsingPhoneId(i);
      if (k != mPhoneSubscriptions[i])
      {
        paramString.append(" phone[");
        paramString.append(i);
        paramString.append("] ");
        paramString.append(mPhoneSubscriptions[i]);
        paramString.append("->");
        paramString.append(k);
        mPhoneSubscriptions[i] = k;
        paramBoolean = true;
      }
    }
    if (paramBoolean)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("evaluating due to ");
      ((StringBuilder)localObject).append(paramString.toString());
      log(((StringBuilder)localObject).toString());
      localObject = new ArrayList();
      paramString = mPrioritizedDcRequests.iterator();
      while (paramString.hasNext())
      {
        i = phoneIdForRequest(nextnetworkRequest);
        if ((i != -1) && (!((List)localObject).contains(Integer.valueOf(i))))
        {
          ((List)localObject).add(Integer.valueOf(i));
          if (((List)localObject).size() >= mMaxActivePhones) {
            break;
          }
        }
      }
      for (i = j; i < mNumPhones; i++) {
        if (!((List)localObject).contains(Integer.valueOf(i))) {
          deactivate(i);
        }
      }
      paramString = ((List)localObject).iterator();
      while (paramString.hasNext()) {
        activate(((Integer)paramString.next()).intValue());
      }
    }
  }
  
  protected void onResendDataAllowed(Message paramMessage)
  {
    int i = arg1;
    if (mNumPhones > 1) {
      mCommandsInterfaces[i].setDataAllowed(mPhoneStates[i].active, null);
    }
  }
  
  protected int phoneIdForRequest(NetworkRequest paramNetworkRequest)
  {
    paramNetworkRequest = networkCapabilities.getNetworkSpecifier();
    int i;
    if (paramNetworkRequest == null) {
      i = mDefaultDataSubscription;
    } else if ((paramNetworkRequest instanceof StringNetworkSpecifier)) {
      try
      {
        i = Integer.parseInt(specifier);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("NumberFormatException on ");
        localStringBuilder.append(specifier);
        Rlog.e("PhoneSwitcher", localStringBuilder.toString());
        i = -1;
      }
    } else {
      i = -1;
    }
    int j = -1;
    if (i == -1) {
      return -1;
    }
    int m;
    for (int k = 0;; k++)
    {
      m = j;
      if (k >= mNumPhones) {
        break;
      }
      if (mPhoneSubscriptions[k] == i)
      {
        m = k;
        break;
      }
    }
    return m;
  }
  
  public void registerForActivePhoneSwitch(int paramInt1, Handler paramHandler, int paramInt2, Object paramObject)
  {
    validatePhoneId(paramInt1);
    paramHandler = new Registrant(paramHandler, paramInt2, paramObject);
    mActivePhoneRegistrants[paramInt1].add(paramHandler);
    paramHandler.notifyRegistrant();
  }
  
  public void resendDataAllowed(int paramInt)
  {
    validatePhoneId(paramInt);
    Message localMessage = obtainMessage(106);
    arg1 = paramInt;
    localMessage.sendToTarget();
  }
  
  public void unregisterForActivePhoneSwitch(int paramInt, Handler paramHandler)
  {
    validatePhoneId(paramInt);
    mActivePhoneRegistrants[paramInt].remove(paramHandler);
  }
  
  protected static class PhoneState
  {
    public volatile boolean active = false;
    public long lastRequested = 0L;
    
    protected PhoneState() {}
  }
  
  private static class PhoneSwitcherNetworkRequestListener
    extends NetworkFactory
  {
    private final PhoneSwitcher mPhoneSwitcher;
    
    public PhoneSwitcherNetworkRequestListener(Looper paramLooper, Context paramContext, NetworkCapabilities paramNetworkCapabilities, PhoneSwitcher paramPhoneSwitcher)
    {
      super(paramContext, "PhoneSwitcherNetworkRequstListener", paramNetworkCapabilities);
      mPhoneSwitcher = paramPhoneSwitcher;
    }
    
    protected void needNetworkFor(NetworkRequest paramNetworkRequest, int paramInt)
    {
      Message localMessage = mPhoneSwitcher.obtainMessage(103);
      obj = paramNetworkRequest;
      localMessage.sendToTarget();
    }
    
    protected void releaseNetworkFor(NetworkRequest paramNetworkRequest)
    {
      Message localMessage = mPhoneSwitcher.obtainMessage(104);
      obj = paramNetworkRequest;
      localMessage.sendToTarget();
    }
  }
}
