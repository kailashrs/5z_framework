package com.android.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class UiccCardApplication
{
  public static final int AUTH_CONTEXT_EAP_AKA = 129;
  public static final int AUTH_CONTEXT_EAP_SIM = 128;
  public static final int AUTH_CONTEXT_UNDEFINED = -1;
  private static final boolean DBG = true;
  private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 5;
  private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 7;
  private static final int EVENT_CHANGE_PIN1_DONE = 2;
  private static final int EVENT_CHANGE_PIN2_DONE = 3;
  private static final int EVENT_PIN1_PUK1_DONE = 1;
  private static final int EVENT_PIN2_PUK2_DONE = 8;
  private static final int EVENT_QUERY_FACILITY_FDN_DONE = 4;
  private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 6;
  private static final int EVENT_RADIO_UNAVAILABLE = 9;
  private static final String LOG_TAG = "UiccCardApplication";
  private String mAid;
  private String mAppLabel;
  private IccCardApplicationStatus.AppState mAppState;
  private IccCardApplicationStatus.AppType mAppType;
  private int mAuthContext;
  private CommandsInterface mCi;
  private Context mContext;
  private boolean mDesiredFdnEnabled;
  private boolean mDesiredPinLocked;
  private boolean mDestroyed;
  private Handler mHandler;
  private boolean mIccFdnAvailable;
  private boolean mIccFdnEnabled;
  private IccFileHandler mIccFh;
  private boolean mIccLockEnabled;
  private IccRecords mIccRecords;
  private boolean mIgnoreApp;
  private final Object mLock = new Object();
  private RegistrantList mNetworkLockedRegistrants;
  private IccCardApplicationStatus.PersoSubState mPersoSubState;
  private boolean mPin1Replaced;
  private IccCardStatus.PinState mPin1State;
  private IccCardStatus.PinState mPin2State;
  private RegistrantList mPinLockedRegistrants;
  private RegistrantList mReadyRegistrants;
  private UiccProfile mUiccProfile;
  
  public UiccCardApplication(UiccProfile paramUiccProfile, IccCardApplicationStatus paramIccCardApplicationStatus, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    boolean bool = true;
    mIccFdnAvailable = true;
    mReadyRegistrants = new RegistrantList();
    mPinLockedRegistrants = new RegistrantList();
    mNetworkLockedRegistrants = new RegistrantList();
    mHandler = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        Object localObject1;
        Object localObject2;
        if (mDestroyed)
        {
          localObject1 = UiccCardApplication.this;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Received message ");
          ((StringBuilder)localObject2).append(paramAnonymousMessage);
          ((StringBuilder)localObject2).append("[");
          ((StringBuilder)localObject2).append(what);
          ((StringBuilder)localObject2).append("] while being destroyed. Ignoring.");
          ((UiccCardApplication)localObject1).loge(((StringBuilder)localObject2).toString());
          return;
        }
        switch (what)
        {
        default: 
          localObject2 = UiccCardApplication.this;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown Event ");
          ((StringBuilder)localObject1).append(what);
          ((UiccCardApplication)localObject2).loge(((StringBuilder)localObject1).toString());
          break;
        case 9: 
          UiccCardApplication.this.log("handleMessage (EVENT_RADIO_UNAVAILABLE)");
          UiccCardApplication.access$802(UiccCardApplication.this, IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN);
          break;
        case 7: 
          paramAnonymousMessage = (AsyncResult)obj;
          UiccCardApplication.this.onChangeFacilityLock(paramAnonymousMessage);
          break;
        case 6: 
          paramAnonymousMessage = (AsyncResult)obj;
          UiccCardApplication.this.onQueryFacilityLock(paramAnonymousMessage);
          break;
        case 5: 
          paramAnonymousMessage = (AsyncResult)obj;
          UiccCardApplication.this.onChangeFdnDone(paramAnonymousMessage);
          break;
        case 4: 
          paramAnonymousMessage = (AsyncResult)obj;
          UiccCardApplication.this.onQueryFdnEnabled(paramAnonymousMessage);
          break;
        case 1: 
        case 2: 
        case 3: 
        case 8: 
          localObject1 = (AsyncResult)obj;
          int i = UiccCardApplication.this.parsePinPukErrorResult((AsyncResult)localObject1);
          paramAnonymousMessage = (Message)userObj;
          forMessageexception = exception;
          arg1 = i;
          paramAnonymousMessage.sendToTarget();
        }
      }
    };
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Creating UiccApp: ");
    localStringBuilder.append(paramIccCardApplicationStatus);
    log(localStringBuilder.toString());
    mUiccProfile = paramUiccProfile;
    mAppState = app_state;
    mAppType = app_type;
    mAuthContext = getAuthContext(mAppType);
    mPersoSubState = perso_substate;
    mAid = aid;
    mAppLabel = app_label;
    if (pin1_replaced == 0) {
      bool = false;
    }
    mPin1Replaced = bool;
    mPin1State = pin1;
    mPin2State = pin2;
    mIgnoreApp = false;
    mContext = paramContext;
    mCi = paramCommandsInterface;
    if (mUiccProfile.getPhoneId() == 0) {
      SystemProperties.set("gsm.sim1.present", "1");
    } else {
      SystemProperties.set("gsm.sim2.present", "1");
    }
    mIccFh = createIccFileHandler(app_type);
    mIccRecords = createIccRecords(app_type, mContext, mCi);
    if (mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY)
    {
      queryFdn();
      queryPin1State();
    }
    mCi.registerForNotAvailable(mHandler, 9, null);
  }
  
  private IccFileHandler createIccFileHandler(IccCardApplicationStatus.AppType paramAppType)
  {
    switch (2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[paramAppType.ordinal()])
    {
    default: 
      return null;
    case 5: 
      return new IsimFileHandler(this, mAid, mCi);
    case 4: 
      return new CsimFileHandler(this, mAid, mCi);
    case 3: 
      return new UsimFileHandler(this, mAid, mCi);
    case 2: 
      return new RuimFileHandler(this, mAid, mCi);
    }
    return new SIMFileHandler(this, mAid, mCi);
  }
  
  private IccRecords createIccRecords(IccCardApplicationStatus.AppType paramAppType, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    if ((paramAppType != IccCardApplicationStatus.AppType.APPTYPE_USIM) && (paramAppType != IccCardApplicationStatus.AppType.APPTYPE_SIM))
    {
      if ((paramAppType != IccCardApplicationStatus.AppType.APPTYPE_RUIM) && (paramAppType != IccCardApplicationStatus.AppType.APPTYPE_CSIM))
      {
        if (paramAppType == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
          return new IsimUiccRecords(this, paramContext, paramCommandsInterface);
        }
        return null;
      }
      return new RuimRecords(this, paramContext, paramCommandsInterface);
    }
    return new SIMRecords(this, paramContext, paramCommandsInterface);
  }
  
  private static int getAuthContext(IccCardApplicationStatus.AppType paramAppType)
  {
    int i = 2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[paramAppType.ordinal()];
    if (i != 1)
    {
      if (i != 3) {
        i = -1;
      } else {
        i = 129;
      }
    }
    else {
      i = 128;
    }
    return i;
  }
  
  private void log(String paramString)
  {
    Rlog.d("UiccCardApplication", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("UiccCardApplication", paramString);
  }
  
  private void notifyNetworkLockedRegistrantsIfNeeded(Registrant paramRegistrant)
  {
    if (mDestroyed) {
      return;
    }
    if ((mAppState == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO) && (isPersoLocked()))
    {
      AsyncResult localAsyncResult = new AsyncResult(null, Integer.valueOf(mPersoSubState.ordinal()), null);
      if (paramRegistrant == null)
      {
        log("Notifying registrants: NETWORK_LOCKED");
        mNetworkLockedRegistrants.notifyRegistrants(localAsyncResult);
      }
      else
      {
        log("Notifying 1 registrant: NETWORK_LOCED");
        paramRegistrant.notifyRegistrant(localAsyncResult);
      }
    }
  }
  
  private void notifyPinLockedRegistrantsIfNeeded(Registrant paramRegistrant)
  {
    if (mDestroyed) {
      return;
    }
    if ((mAppState == IccCardApplicationStatus.AppState.APPSTATE_PIN) || (mAppState == IccCardApplicationStatus.AppState.APPSTATE_PUK))
    {
      if ((mPin1State == IccCardStatus.PinState.PINSTATE_ENABLED_VERIFIED) || (mPin1State == IccCardStatus.PinState.PINSTATE_DISABLED)) {
        break label94;
      }
      if (paramRegistrant == null)
      {
        log("Notifying registrants: LOCKED");
        mPinLockedRegistrants.notifyRegistrants();
      }
      else
      {
        log("Notifying 1 registrant: LOCKED");
        paramRegistrant.notifyRegistrant(new AsyncResult(null, null, null));
      }
    }
    return;
    label94:
    loge("Sanity check failed! APPSTATE is locked while PIN1 is not!!!");
  }
  
  private void notifyReadyRegistrantsIfNeeded(Registrant paramRegistrant)
  {
    if (mDestroyed) {
      return;
    }
    if (mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY) {
      if ((mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED) && (mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED) && (mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED))
      {
        if (paramRegistrant == null)
        {
          log("Notifying registrants: READY");
          mReadyRegistrants.notifyRegistrants();
        }
        else
        {
          log("Notifying 1 registrant: READY");
          paramRegistrant.notifyRegistrant(new AsyncResult(null, null, null));
        }
      }
      else
      {
        loge("Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
        return;
      }
    }
  }
  
  private void onChangeFacilityLock(AsyncResult paramAsyncResult)
  {
    Object localObject1 = mLock;
    int i = -1;
    try
    {
      if (exception == null)
      {
        mIccLockEnabled = mDesiredPinLocked;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("EVENT_CHANGE_FACILITY_LOCK_DONE: mIccLockEnabled= ");
        ((StringBuilder)localObject2).append(mIccLockEnabled);
        log(((StringBuilder)localObject2).toString());
      }
      else
      {
        i = parsePinPukErrorResult(paramAsyncResult);
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Error change facility lock with exception ");
        ((StringBuilder)localObject2).append(exception);
        loge(((StringBuilder)localObject2).toString());
      }
      Object localObject2 = (Message)userObj;
      forMessageexception = exception;
      arg1 = i;
      ((Message)localObject2).sendToTarget();
      return;
    }
    finally {}
  }
  
  private void onChangeFdnDone(AsyncResult paramAsyncResult)
  {
    Object localObject1 = mLock;
    int i = -1;
    try
    {
      if (exception == null)
      {
        mIccFdnEnabled = mDesiredFdnEnabled;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("EVENT_CHANGE_FACILITY_FDN_DONE: mIccFdnEnabled=");
        ((StringBuilder)localObject2).append(mIccFdnEnabled);
        log(((StringBuilder)localObject2).toString());
      }
      else
      {
        i = parsePinPukErrorResult(paramAsyncResult);
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Error change facility fdn with exception ");
        ((StringBuilder)localObject2).append(exception);
        loge(((StringBuilder)localObject2).toString());
      }
      Object localObject2 = (Message)userObj;
      arg1 = i;
      forMessageexception = exception;
      ((Message)localObject2).sendToTarget();
      return;
    }
    finally {}
  }
  
  private void onQueryFacilityLock(AsyncResult paramAsyncResult)
  {
    synchronized (mLock)
    {
      StringBuilder localStringBuilder;
      if (exception != null)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Error in querying facility lock:");
        localStringBuilder.append(exception);
        log(localStringBuilder.toString());
        return;
      }
      paramAsyncResult = (int[])result;
      if (paramAsyncResult.length != 0)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Query facility lock : ");
        boolean bool = false;
        localStringBuilder.append(paramAsyncResult[0]);
        log(localStringBuilder.toString());
        if (paramAsyncResult[0] != 0) {
          bool = true;
        }
        mIccLockEnabled = bool;
        if (mIccLockEnabled) {
          mPinLockedRegistrants.notifyRegistrants();
        }
        switch (2.$SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[mPin1State.ordinal()])
        {
        default: 
          break;
        case 2: 
        case 3: 
        case 4: 
        case 5: 
          if (!mIccLockEnabled) {
            loge("QUERY_FACILITY_LOCK:disabled GET_SIM_STATUS.Pin1:enabled. Fixme");
          }
          break;
        case 1: 
          if (!mIccLockEnabled) {
            break label252;
          }
          loge("QUERY_FACILITY_LOCK:enabled GET_SIM_STATUS.Pin1:disabled. Fixme");
          break;
        }
        paramAsyncResult = new java/lang/StringBuilder;
        paramAsyncResult.<init>();
        paramAsyncResult.append("Ignoring: pin1state=");
        paramAsyncResult.append(mPin1State);
        log(paramAsyncResult.toString());
      }
      else
      {
        loge("Bogus facility lock response");
      }
      label252:
      return;
    }
  }
  
  private void onQueryFdnEnabled(AsyncResult paramAsyncResult)
  {
    synchronized (mLock)
    {
      if (exception != null)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Error in querying facility lock:");
        localStringBuilder.append(exception);
        log(localStringBuilder.toString());
        return;
      }
      paramAsyncResult = (int[])result;
      if (paramAsyncResult.length != 0)
      {
        boolean bool = false;
        if (paramAsyncResult[0] == 2)
        {
          mIccFdnEnabled = false;
          mIccFdnAvailable = false;
        }
        else
        {
          if (paramAsyncResult[0] == 1) {
            bool = true;
          }
          mIccFdnEnabled = bool;
          mIccFdnAvailable = true;
        }
        paramAsyncResult = new java/lang/StringBuilder;
        paramAsyncResult.<init>();
        paramAsyncResult.append("Query facility FDN : FDN service available: ");
        paramAsyncResult.append(mIccFdnAvailable);
        paramAsyncResult.append(" enabled: ");
        paramAsyncResult.append(mIccFdnEnabled);
        log(paramAsyncResult.toString());
      }
      else
      {
        loge("Bogus facility lock response");
      }
      return;
    }
  }
  
  private int parsePinPukErrorResult(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = (int[])result;
    if (paramAsyncResult == null) {
      return -1;
    }
    int i = paramAsyncResult.length;
    int j = -1;
    if (i > 0) {
      j = paramAsyncResult[0];
    }
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("parsePinPukErrorResult: attemptsRemaining=");
    paramAsyncResult.append(j);
    log(paramAsyncResult.toString());
    return j;
  }
  
  private void queryPin1State()
  {
    mCi.queryFacilityLockForApp("SC", "", 7, mAid, mHandler.obtainMessage(6));
  }
  
  public void changeIccFdnPassword(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      log("changeIccFdnPassword");
      mCi.changeIccPin2ForApp(paramString1, paramString2, mAid, mHandler.obtainMessage(3, paramMessage));
      return;
    }
  }
  
  public void changeIccLockPassword(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      log("changeIccLockPassword");
      mCi.changeIccPinForApp(paramString1, paramString2, mAid, mHandler.obtainMessage(2, paramMessage));
      return;
    }
  }
  
  void dispose()
  {
    synchronized (mLock)
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(mAppType);
      localStringBuilder.append(" being Disposed");
      log(localStringBuilder.toString());
      mDestroyed = true;
      if (mIccRecords != null) {
        mIccRecords.dispose();
      }
      if (mIccFh != null) {
        mIccFh.dispose();
      }
      mIccRecords = null;
      mIccFh = null;
      mCi.unregisterForNotAvailable(mHandler);
      return;
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("UiccCardApplication: ");
    paramFileDescriptor.append(this);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mUiccProfile=");
    paramFileDescriptor.append(mUiccProfile);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mAppState=");
    paramFileDescriptor.append(mAppState);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mAppType=");
    paramFileDescriptor.append(mAppType);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPersoSubState=");
    paramFileDescriptor.append(mPersoSubState);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mAid=");
    paramFileDescriptor.append(mAid);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mAppLabel=");
    paramFileDescriptor.append(mAppLabel);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPin1Replaced=");
    paramFileDescriptor.append(mPin1Replaced);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPin1State=");
    paramFileDescriptor.append(mPin1State);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPin2State=");
    paramFileDescriptor.append(mPin2State);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccFdnEnabled=");
    paramFileDescriptor.append(mIccFdnEnabled);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDesiredFdnEnabled=");
    paramFileDescriptor.append(mDesiredFdnEnabled);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccLockEnabled=");
    paramFileDescriptor.append(mIccLockEnabled);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDesiredPinLocked=");
    paramFileDescriptor.append(mDesiredPinLocked);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCi=");
    paramFileDescriptor.append(mCi);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccRecords=");
    paramFileDescriptor.append(mIccRecords);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccFh=");
    paramFileDescriptor.append(mIccFh);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDestroyed=");
    paramFileDescriptor.append(mDestroyed);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mReadyRegistrants: size=");
    paramFileDescriptor.append(mReadyRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    int i = 0;
    for (int j = 0; j < mReadyRegistrants.size(); j++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mReadyRegistrants[");
      paramFileDescriptor.append(j);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mReadyRegistrants.get(j)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPinLockedRegistrants: size=");
    paramFileDescriptor.append(mPinLockedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (j = 0; j < mPinLockedRegistrants.size(); j++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mPinLockedRegistrants[");
      paramFileDescriptor.append(j);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mPinLockedRegistrants.get(j)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNetworkLockedRegistrants: size=");
    paramFileDescriptor.append(mNetworkLockedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (j = i; j < mNetworkLockedRegistrants.size(); j++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mNetworkLockedRegistrants[");
      paramFileDescriptor.append(j);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mNetworkLockedRegistrants.get(j)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramPrintWriter.flush();
  }
  
  public String getAid()
  {
    synchronized (mLock)
    {
      String str = mAid;
      return str;
    }
  }
  
  public String getAppLabel()
  {
    return mAppLabel;
  }
  
  public int getAuthContext()
  {
    synchronized (mLock)
    {
      int i = mAuthContext;
      return i;
    }
  }
  
  public boolean getIccFdnAvailable()
  {
    return mIccFdnAvailable;
  }
  
  public boolean getIccFdnEnabled()
  {
    synchronized (mLock)
    {
      boolean bool = mIccFdnEnabled;
      return bool;
    }
  }
  
  public IccFileHandler getIccFileHandler()
  {
    synchronized (mLock)
    {
      IccFileHandler localIccFileHandler = mIccFh;
      return localIccFileHandler;
    }
  }
  
  public boolean getIccLockEnabled()
  {
    return mIccLockEnabled;
  }
  
  public boolean getIccPin2Blocked()
  {
    synchronized (mLock)
    {
      boolean bool;
      if (mPin2State == IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean getIccPuk2Blocked()
  {
    synchronized (mLock)
    {
      boolean bool;
      if (mPin2State == IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public IccRecords getIccRecords()
  {
    synchronized (mLock)
    {
      IccRecords localIccRecords = mIccRecords;
      return localIccRecords;
    }
  }
  
  public IccCardApplicationStatus.PersoSubState getPersoSubState()
  {
    synchronized (mLock)
    {
      IccCardApplicationStatus.PersoSubState localPersoSubState = mPersoSubState;
      return localPersoSubState;
    }
  }
  
  public int getPhoneId()
  {
    return mUiccProfile.getPhoneId();
  }
  
  public IccCardStatus.PinState getPin1State()
  {
    synchronized (mLock)
    {
      if (mPin1Replaced)
      {
        localPinState = mUiccProfile.getUniversalPinState();
        return localPinState;
      }
      IccCardStatus.PinState localPinState = mPin1State;
      return localPinState;
    }
  }
  
  public IccCardApplicationStatus.AppState getState()
  {
    synchronized (mLock)
    {
      IccCardApplicationStatus.AppState localAppState = mAppState;
      return localAppState;
    }
  }
  
  public IccCardApplicationStatus.AppType getType()
  {
    synchronized (mLock)
    {
      IccCardApplicationStatus.AppType localAppType = mAppType;
      return localAppType;
    }
  }
  
  protected UiccProfile getUiccProfile()
  {
    return mUiccProfile;
  }
  
  public boolean isAppIgnored()
  {
    return mIgnoreApp;
  }
  
  public boolean isPersoLocked()
  {
    switch (2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[mPersoSubState.ordinal()])
    {
    default: 
      return true;
    }
    return false;
  }
  
  public boolean isReady()
  {
    synchronized (mLock)
    {
      if (mAppState != IccCardApplicationStatus.AppState.APPSTATE_READY) {
        return false;
      }
      if ((mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED) && (mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED) && (mPin1State != IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED)) {
        return true;
      }
      loge("Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
      return false;
    }
  }
  
  public void queryFdn()
  {
    mCi.queryFacilityLockForApp("FD", "", 7, mAid, mHandler.obtainMessage(4));
  }
  
  protected void registerForLocked(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mPinLockedRegistrants.add(localRegistrant);
      notifyPinLockedRegistrantsIfNeeded(localRegistrant);
      return;
    }
  }
  
  protected void registerForNetworkLocked(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mNetworkLockedRegistrants.add(localRegistrant);
      notifyNetworkLockedRegistrantsIfNeeded(localRegistrant);
      return;
    }
  }
  
  public void registerForReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mReadyRegistrants.add(localRegistrant);
      notifyReadyRegistrantsIfNeeded(localRegistrant);
      return;
    }
  }
  
  public void setAppIgnoreState(boolean paramBoolean)
  {
    mIgnoreApp = paramBoolean;
  }
  
  public void setIccFdnEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      mDesiredFdnEnabled = paramBoolean;
      mCi.setFacilityLockForApp("FD", paramBoolean, paramString, 15, mAid, mHandler.obtainMessage(5, paramMessage));
      return;
    }
  }
  
  public void setIccLockEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      mDesiredPinLocked = paramBoolean;
      mCi.setFacilityLockForApp("SC", paramBoolean, paramString, 7, mAid, mHandler.obtainMessage(7, paramMessage));
      return;
    }
  }
  
  public void supplyNetworkDepersonalization(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      log("supplyNetworkDepersonalization");
      mCi.supplyNetworkDepersonalization(paramString, paramMessage);
      return;
    }
  }
  
  public void supplyPin(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      mCi.supplyIccPinForApp(paramString, mAid, mHandler.obtainMessage(1, paramMessage));
      return;
    }
  }
  
  public void supplyPin2(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      mCi.supplyIccPin2ForApp(paramString, mAid, mHandler.obtainMessage(8, paramMessage));
      return;
    }
  }
  
  public void supplyPuk(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      mCi.supplyIccPukForApp(paramString1, paramString2, mAid, mHandler.obtainMessage(1, paramMessage));
      return;
    }
  }
  
  public void supplyPuk2(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      mCi.supplyIccPuk2ForApp(paramString1, paramString2, mAid, mHandler.obtainMessage(8, paramMessage));
      return;
    }
  }
  
  protected void unregisterForLocked(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mPinLockedRegistrants.remove(paramHandler);
      return;
    }
  }
  
  protected void unregisterForNetworkLocked(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mNetworkLockedRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForReady(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mReadyRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void update(IccCardApplicationStatus paramIccCardApplicationStatus, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    synchronized (mLock)
    {
      if (mDestroyed)
      {
        loge("Application updated after destroyed! Fix me!");
        return;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append(mAppType);
      ((StringBuilder)localObject2).append(" update. New ");
      ((StringBuilder)localObject2).append(paramIccCardApplicationStatus);
      log(((StringBuilder)localObject2).toString());
      mContext = paramContext;
      mCi = paramCommandsInterface;
      localObject2 = mAppType;
      IccCardApplicationStatus.AppState localAppState = mAppState;
      IccCardApplicationStatus.PersoSubState localPersoSubState = mPersoSubState;
      mAppType = app_type;
      mAuthContext = getAuthContext(mAppType);
      mAppState = app_state;
      mPersoSubState = perso_substate;
      mAid = aid;
      mAppLabel = app_label;
      boolean bool;
      if (pin1_replaced != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mPin1Replaced = bool;
      mPin1State = pin1;
      mPin2State = pin2;
      if (mAppType != localObject2)
      {
        if (mIccFh != null) {
          mIccFh.dispose();
        }
        if (mIccRecords != null) {
          mIccRecords.dispose();
        }
        mIccFh = createIccFileHandler(app_type);
        mIccRecords = createIccRecords(app_type, paramContext, paramCommandsInterface);
      }
      if ((mPersoSubState != localPersoSubState) && (isPersoLocked())) {
        notifyNetworkLockedRegistrantsIfNeeded(null);
      }
      if (mAppState != localAppState)
      {
        paramIccCardApplicationStatus = new java/lang/StringBuilder;
        paramIccCardApplicationStatus.<init>();
        paramIccCardApplicationStatus.append(localObject2);
        paramIccCardApplicationStatus.append(" changed state: ");
        paramIccCardApplicationStatus.append(localAppState);
        paramIccCardApplicationStatus.append(" -> ");
        paramIccCardApplicationStatus.append(mAppState);
        log(paramIccCardApplicationStatus.toString());
        if (mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY)
        {
          queryFdn();
          queryPin1State();
        }
        notifyPinLockedRegistrantsIfNeeded(null);
        notifyReadyRegistrantsIfNeeded(null);
      }
      return;
    }
  }
}
