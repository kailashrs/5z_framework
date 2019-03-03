package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Registrant;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.UiccCardApplication;

public class GsmCdmaConnection
  extends Connection
{
  private static final boolean DBG = true;
  static final int EVENT_DTMF_DELAY_DONE = 5;
  static final int EVENT_DTMF_DONE = 1;
  static final int EVENT_NEXT_POST_DIAL = 3;
  static final int EVENT_PAUSE_DONE = 2;
  static final int EVENT_WAKE_LOCK_TIMEOUT = 4;
  private static final String LOG_TAG = "GsmCdmaConnection";
  static final int PAUSE_DELAY_MILLIS_CDMA = 2000;
  static final int PAUSE_DELAY_MILLIS_GSM = 3000;
  private static final boolean VDBG = false;
  static final int WAKE_LOCK_TIMEOUT_MILLIS = 60000;
  long mDisconnectTime;
  boolean mDisconnected;
  private int mDtmfToneDelay = 0;
  Handler mHandler;
  int mIndex;
  private boolean mIsEmergencyCall = false;
  Connection mOrigConnection;
  GsmCdmaCallTracker mOwner;
  GsmCdmaCall mParent;
  private PowerManager.WakeLock mPartialWakeLock;
  int mPreciseCause = 0;
  UUSInfo mUusInfo;
  String mVendorCause;
  
  public GsmCdmaConnection(Context paramContext, CdmaCallWaitingNotification paramCdmaCallWaitingNotification, GsmCdmaCallTracker paramGsmCdmaCallTracker, GsmCdmaCall paramGsmCdmaCall)
  {
    super(paramGsmCdmaCall.getPhone().getPhoneType());
    createWakeLock(paramContext);
    acquireWakeLock();
    mOwner = paramGsmCdmaCallTracker;
    mHandler = new MyHandler(mOwner.getLooper());
    mAddress = number;
    mNumberPresentation = numberPresentation;
    mCnapName = name;
    mCnapNamePresentation = namePresentation;
    mIndex = -1;
    mIsIncoming = true;
    mCreateTime = System.currentTimeMillis();
    mConnectTime = 0L;
    mParent = paramGsmCdmaCall;
    paramGsmCdmaCall.attachFake(this, Call.State.WAITING);
  }
  
  public GsmCdmaConnection(GsmCdmaPhone paramGsmCdmaPhone, DriverCall paramDriverCall, GsmCdmaCallTracker paramGsmCdmaCallTracker, int paramInt)
  {
    super(paramGsmCdmaPhone.getPhoneType());
    createWakeLock(paramGsmCdmaPhone.getContext());
    acquireWakeLock();
    mOwner = paramGsmCdmaCallTracker;
    mHandler = new MyHandler(mOwner.getLooper());
    mAddress = number;
    mIsEmergencyCall = PhoneNumberUtils.isLocalEmergencyNumber(paramGsmCdmaPhone.getContext(), mAddress);
    mIsIncoming = isMT;
    mCreateTime = System.currentTimeMillis();
    mCnapName = name;
    mCnapNamePresentation = namePresentation;
    mNumberPresentation = numberPresentation;
    mUusInfo = uusInfo;
    mIndex = paramInt;
    mParent = parentFromDCState(state);
    mParent.attach(this, paramDriverCall);
    fetchDtmfToneDelay(paramGsmCdmaPhone);
    setAudioQuality(getAudioQualityFromDC(audioQuality));
  }
  
  public GsmCdmaConnection(GsmCdmaPhone paramGsmCdmaPhone, String paramString, GsmCdmaCallTracker paramGsmCdmaCallTracker, GsmCdmaCall paramGsmCdmaCall, boolean paramBoolean)
  {
    super(paramGsmCdmaPhone.getPhoneType());
    createWakeLock(paramGsmCdmaPhone.getContext());
    acquireWakeLock();
    mOwner = paramGsmCdmaCallTracker;
    mHandler = new MyHandler(mOwner.getLooper());
    mDialString = paramString;
    paramGsmCdmaCallTracker = paramString;
    if (!isPhoneTypeGsm())
    {
      paramGsmCdmaCallTracker = new StringBuilder();
      paramGsmCdmaCallTracker.append("[GsmCdmaConn] GsmCdmaConnection: dialString=");
      paramGsmCdmaCallTracker.append(maskDialString(paramString));
      Rlog.d("GsmCdmaConnection", paramGsmCdmaCallTracker.toString());
      paramGsmCdmaCallTracker = formatDialString(paramString);
      paramString = new StringBuilder();
      paramString.append("[GsmCdmaConn] GsmCdmaConnection:formated dialString=");
      paramString.append(maskDialString(paramGsmCdmaCallTracker));
      Rlog.d("GsmCdmaConnection", paramString.toString());
    }
    mAddress = PhoneNumberUtils.extractNetworkPortionAlt(paramGsmCdmaCallTracker);
    mIsEmergencyCall = paramBoolean;
    mPostDialString = PhoneNumberUtils.extractPostDialPortion(paramGsmCdmaCallTracker);
    mIndex = -1;
    mIsIncoming = false;
    mCnapName = null;
    mCnapNamePresentation = 1;
    mNumberPresentation = 1;
    mCreateTime = System.currentTimeMillis();
    if (paramGsmCdmaCall != null)
    {
      mParent = paramGsmCdmaCall;
      if (isPhoneTypeGsm()) {
        paramGsmCdmaCall.attachFake(this, Call.State.DIALING);
      } else if (mState == Call.State.ACTIVE) {
        paramGsmCdmaCall.attachFake(this, Call.State.ACTIVE);
      } else {
        paramGsmCdmaCall.attachFake(this, Call.State.DIALING);
      }
    }
    fetchDtmfToneDelay(paramGsmCdmaPhone);
  }
  
  private void acquireWakeLock()
  {
    if (mPartialWakeLock != null) {
      synchronized (mPartialWakeLock)
      {
        log("acquireWakeLock");
        mPartialWakeLock.acquire();
      }
    }
  }
  
  private void createWakeLock(Context paramContext)
  {
    mPartialWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "GsmCdmaConnection");
  }
  
  private void doDisconnect()
  {
    mIndex = -1;
    mDisconnectTime = System.currentTimeMillis();
    mDuration = (SystemClock.elapsedRealtime() - mConnectTimeReal);
    mDisconnected = true;
    clearPostDialListeners();
  }
  
  static boolean equalsBaseDialString(String paramString1, String paramString2)
  {
    boolean bool = false;
    if (paramString1 == null) {
      if (paramString2 != null) {}
    }
    for (;;)
    {
      bool = true;
      break;
      do
      {
        break;
      } while ((paramString2 == null) || (!paramString1.startsWith(paramString2)));
    }
    return bool;
  }
  
  static boolean equalsHandlesNulls(Object paramObject1, Object paramObject2)
  {
    boolean bool;
    if (paramObject1 == null)
    {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else {
      bool = paramObject1.equals(paramObject2);
    }
    return bool;
  }
  
  private void fetchDtmfToneDelay(GsmCdmaPhone paramGsmCdmaPhone)
  {
    PersistableBundle localPersistableBundle = ((CarrierConfigManager)paramGsmCdmaPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(paramGsmCdmaPhone.getSubId());
    if (localPersistableBundle != null) {
      mDtmfToneDelay = localPersistableBundle.getInt(paramGsmCdmaPhone.getDtmfToneDelayKey());
    }
  }
  
  private static int findNextPCharOrNonPOrNonWCharIndex(String paramString, int paramInt)
  {
    boolean bool1 = isWait(paramString.charAt(paramInt));
    int i = paramInt + 1;
    int j = paramString.length();
    boolean bool2;
    for (;;)
    {
      bool2 = bool1;
      if (i >= j) {
        break;
      }
      char c = paramString.charAt(i);
      if (isWait(c)) {
        bool1 = true;
      }
      if ((!isWait(c)) && (!isPause(c)))
      {
        bool2 = bool1;
        break;
      }
      i++;
    }
    if ((i < j) && (i > paramInt + 1) && (!bool2) && (isPause(paramString.charAt(paramInt)))) {
      return paramInt + 1;
    }
    return i;
  }
  
  private static char findPOrWCharToAppend(String paramString, int paramInt1, int paramInt2)
  {
    int i;
    if (isPause(paramString.charAt(paramInt1))) {
      i = 44;
    } else {
      i = 59;
    }
    int j = i;
    if (paramInt2 > paramInt1 + 1)
    {
      paramInt1 = 59;
      j = paramInt1;
    }
    return j;
  }
  
  public static String formatDialString(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder();
    int k;
    for (int j = 0; j < i; j = k + 1)
    {
      char c = paramString.charAt(j);
      if ((!isPause(c)) && (!isWait(c)))
      {
        localStringBuilder.append(c);
        k = j;
      }
      else
      {
        k = j;
        if (j < i - 1)
        {
          int m = findNextPCharOrNonPOrNonWCharIndex(paramString, j);
          if (m < i)
          {
            localStringBuilder.append(findPOrWCharToAppend(paramString, j, m));
            k = j;
            if (m > j + 1) {
              k = m - 1;
            }
            j = k;
          }
          else if (m == i)
          {
            j = i - 1;
          }
          k = j;
        }
      }
    }
    return PhoneNumberUtils.cdmaCheckAndProcessPlusCode(localStringBuilder.toString());
  }
  
  private int getAudioQualityFromDC(int paramInt)
  {
    if ((paramInt != 2) && (paramInt != 9)) {
      return 1;
    }
    return 2;
  }
  
  private boolean isConnectingInOrOut()
  {
    boolean bool;
    if ((mParent != null) && (mParent != mOwner.mRingingCall) && (mParent.mState != Call.State.DIALING) && (mParent.mState != Call.State.ALERTING)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isPause(char paramChar)
  {
    boolean bool;
    if (paramChar == ',') {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isPhoneTypeGsm()
  {
    int i = mOwner.getPhone().getPhoneType();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isWait(char paramChar)
  {
    boolean bool;
    if (paramChar == ';') {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isWild(char paramChar)
  {
    boolean bool;
    if (paramChar == 'N') {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[GsmCdmaConn] ");
    localStringBuilder.append(paramString);
    Rlog.d("GsmCdmaConnection", localStringBuilder.toString());
  }
  
  private String maskDialString(String paramString)
  {
    return "<MASKED>";
  }
  
  private GsmCdmaCall parentFromDCState(DriverCall.State paramState)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$DriverCall$State[paramState.ordinal()])
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("illegal call state: ");
      localStringBuilder.append(paramState);
      throw new RuntimeException(localStringBuilder.toString());
    case 5: 
    case 6: 
      return mOwner.mRingingCall;
    case 4: 
      return mOwner.mBackgroundCall;
    }
    return mOwner.mForegroundCall;
  }
  
  private void processNextPostDialChar()
  {
    if (mPostDialState == Connection.PostDialState.CANCELLED)
    {
      releaseWakeLock();
      return;
    }
    int i;
    char c2;
    if ((mPostDialString != null) && (mPostDialString.length() > mNextPostDialChar))
    {
      setPostDialState(Connection.PostDialState.STARTED);
      localObject = mPostDialString;
      i = mNextPostDialChar;
      mNextPostDialChar = (i + 1);
      char c1 = ((String)localObject).charAt(i);
      c2 = c1;
      if (!processPostDialChar(c1))
      {
        mHandler.obtainMessage(3).sendToTarget();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("processNextPostDialChar: c=");
        ((StringBuilder)localObject).append(c1);
        ((StringBuilder)localObject).append(" isn't valid!");
        Rlog.e("GsmCdmaConnection", ((StringBuilder)localObject).toString());
      }
    }
    else
    {
      setPostDialState(Connection.PostDialState.COMPLETE);
      releaseWakeLock();
      i = 0;
      c2 = i;
    }
    notifyPostDialListenersNextChar(c2);
    Object localObject = mOwner.getPhone().getPostDialHandler();
    if (localObject != null)
    {
      localObject = ((Registrant)localObject).messageForRegistrant();
      if (localObject != null)
      {
        Connection.PostDialState localPostDialState = mPostDialState;
        AsyncResult localAsyncResult = AsyncResult.forMessage((Message)localObject);
        result = this;
        userObj = localPostDialState;
        arg1 = c2;
        ((Message)localObject).sendToTarget();
      }
    }
  }
  
  private boolean processPostDialChar(char paramChar)
  {
    if (PhoneNumberUtils.is12Key(paramChar))
    {
      mOwner.mCi.sendDtmf(paramChar, mHandler.obtainMessage(1));
    }
    else if (isPause(paramChar))
    {
      if (!isPhoneTypeGsm()) {
        setPostDialState(Connection.PostDialState.PAUSE);
      }
      Handler localHandler = mHandler;
      Message localMessage = mHandler.obtainMessage(2);
      long l;
      if (isPhoneTypeGsm()) {
        l = 3000L;
      } else {
        l = 2000L;
      }
      localHandler.sendMessageDelayed(localMessage, l);
    }
    else if (isWait(paramChar))
    {
      setPostDialState(Connection.PostDialState.WAIT);
    }
    else
    {
      if (!isWild(paramChar)) {
        break label130;
      }
      setPostDialState(Connection.PostDialState.WILD);
    }
    return true;
    label130:
    return false;
  }
  
  private void releaseAllWakeLocks()
  {
    if (mPartialWakeLock != null) {
      synchronized (mPartialWakeLock)
      {
        while (mPartialWakeLock.isHeld()) {
          mPartialWakeLock.release();
        }
      }
    }
  }
  
  private void releaseWakeLock()
  {
    if (mPartialWakeLock != null) {
      synchronized (mPartialWakeLock)
      {
        if (mPartialWakeLock.isHeld())
        {
          log("releaseWakeLock");
          mPartialWakeLock.release();
        }
      }
    }
  }
  
  private void setPostDialState(Connection.PostDialState paramPostDialState)
  {
    if ((paramPostDialState != Connection.PostDialState.STARTED) && (paramPostDialState != Connection.PostDialState.PAUSE))
    {
      mHandler.removeMessages(4);
      releaseWakeLock();
    }
    synchronized (mPartialWakeLock)
    {
      if (mPartialWakeLock.isHeld()) {
        mHandler.removeMessages(4);
      } else {
        acquireWakeLock();
      }
      Message localMessage = mHandler.obtainMessage(4);
      mHandler.sendMessageDelayed(localMessage, 60000L);
      mPostDialState = paramPostDialState;
      notifyPostDialListeners();
      return;
    }
  }
  
  public void cancelPostDial()
  {
    setPostDialState(Connection.PostDialState.CANCELLED);
  }
  
  boolean compareTo(DriverCall paramDriverCall)
  {
    boolean bool1 = mIsIncoming;
    boolean bool2 = true;
    if ((!bool1) && (!isMT)) {
      return true;
    }
    if ((isPhoneTypeGsm()) && (mOrigConnection != null)) {
      return true;
    }
    String str = PhoneNumberUtils.stringFromStringAndTOA(number, TOA);
    if ((mIsIncoming != isMT) || (!equalsHandlesNulls(mAddress, str))) {
      bool2 = false;
    }
    return bool2;
  }
  
  public void deflect(String paramString)
    throws CallStateException
  {
    throw new CallStateException("deflect is not supported for CS");
  }
  
  int disconnectCauseFromCode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            switch (paramInt)
            {
            default: 
              switch (paramInt)
              {
              default: 
                switch (paramInt)
                {
                default: 
                  switch (paramInt)
                  {
                  default: 
                    switch (paramInt)
                    {
                    default: 
                      switch (paramInt)
                      {
                      default: 
                        switch (paramInt)
                        {
                        default: 
                          switch (paramInt)
                          {
                          default: 
                            switch (paramInt)
                            {
                            default: 
                              GsmCdmaPhone localGsmCdmaPhone = mOwner.getPhone();
                              int i = localGsmCdmaPhone.getServiceState().getState();
                              Object localObject = localGsmCdmaPhone.getUiccCardApplication();
                              if (localObject != null) {
                                localObject = ((UiccCardApplication)localObject).getState();
                              } else {
                                localObject = IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN;
                              }
                              if (i == 3) {
                                return 17;
                              }
                              if (!mIsEmergencyCall) {
                                if ((i != 1) && (i != 2))
                                {
                                  if (localObject != IccCardApplicationStatus.AppState.APPSTATE_READY)
                                  {
                                    if (isPhoneTypeGsm()) {
                                      return 19;
                                    }
                                    if (mCdmaSubscriptionSource == 0) {
                                      return 19;
                                    }
                                  }
                                }
                                else {
                                  return 18;
                                }
                              }
                              if ((isPhoneTypeGsm()) && (paramInt == 65535))
                              {
                                if (mSST.mRestrictedState.isCsRestricted()) {
                                  return 22;
                                }
                                if (mSST.mRestrictedState.isCsEmergencyRestricted())
                                {
                                  if (localGsmCdmaPhone.getPhoneId() == 0) {
                                    localObject = "ril.ecclist";
                                  } else {
                                    localObject = "ril.ecclist1";
                                  }
                                  localObject = SystemProperties.get((String)localObject, "").split(",");
                                  i = localObject.length;
                                  for (paramInt = 0; paramInt < i; paramInt++) {
                                    if (localObject[paramInt].equals(mDialString)) {
                                      return 24;
                                    }
                                  }
                                  return 43;
                                }
                                if (mSST.mRestrictedState.isCsNormalRestricted()) {
                                  return 23;
                                }
                              }
                              if (paramInt == 16) {
                                return 2;
                              }
                              return 36;
                            case 127: 
                              return 93;
                            case 111: 
                              return 92;
                            case 91: 
                              return 83;
                            case 81: 
                              return 80;
                            case 79: 
                              return 79;
                            case 65: 
                              return 76;
                            case 63: 
                              return 75;
                            case 55: 
                              return 73;
                            case 47: 
                              return 114;
                            case 38: 
                              return 109;
                            case 34: 
                              return 96;
                            case 8: 
                              return 98;
                            case 6: 
                              return 103;
                            case 3: 
                              return 97;
                            }
                            return 25;
                          case 1009: 
                            return 35;
                          case 1008: 
                            return 34;
                          case 1007: 
                            return 33;
                          case 1006: 
                            return 32;
                          case 1005: 
                            return 31;
                          case 1004: 
                            return 30;
                          case 1003: 
                            return 29;
                          case 1002: 
                            return 28;
                          case 1001: 
                            return 27;
                          }
                          return 26;
                        case 326: 
                          return 64;
                        }
                        return 63;
                      case 246: 
                        return 48;
                      case 245: 
                        return 47;
                      case 244: 
                        return 46;
                      }
                      return 58;
                    case 241: 
                      return 21;
                    }
                    return 20;
                  case 102: 
                    return 91;
                  case 101: 
                    return 90;
                  case 100: 
                    return 89;
                  case 99: 
                    return 88;
                  case 98: 
                    return 87;
                  case 97: 
                    return 86;
                  case 96: 
                    return 85;
                  }
                  return 84;
                case 88: 
                  return 82;
                }
                return 81;
              case 70: 
                return 78;
              case 69: 
                return 77;
              }
              return 15;
            case 58: 
              return 74;
            }
            return 102;
          case 50: 
            return 72;
          }
          return 71;
        case 44: 
          return 113;
        case 43: 
          return 112;
        case 42: 
          return 111;
        }
        return 110;
      case 31: 
        return 65;
      case 30: 
        return 108;
      case 29: 
        return 107;
      case 28: 
        return 7;
      case 27: 
        return 101;
      case 26: 
        return 116;
      }
      return 106;
    case 22: 
      return 105;
    case 21: 
      return 104;
    case 20: 
      return 100;
    case 19: 
      return 13;
    case 18: 
      return 99;
    }
    return 4;
  }
  
  public void dispose()
  {
    clearPostDialListeners();
    if (mParent != null) {
      mParent.detach(this);
    }
    releaseAllWakeLocks();
  }
  
  void fakeHoldBeforeDial()
  {
    if (mParent != null) {
      mParent.detach(this);
    }
    mParent = mOwner.mBackgroundCall;
    mParent.attachFake(this, Call.State.HOLDING);
    onStartedHolding();
  }
  
  protected void finalize()
  {
    if ((mPartialWakeLock != null) && (mPartialWakeLock.isHeld())) {
      Rlog.e("GsmCdmaConnection", "UNEXPECTED; mPartialWakeLock is held when finalizing.");
    }
    clearPostDialListeners();
    releaseWakeLock();
  }
  
  public GsmCdmaCall getCall()
  {
    return mParent;
  }
  
  public long getDisconnectTime()
  {
    return mDisconnectTime;
  }
  
  int getGsmCdmaIndex()
    throws CallStateException
  {
    if (mIndex >= 0) {
      return mIndex + 1;
    }
    throw new CallStateException("GsmCdma index not yet assigned");
  }
  
  public long getHoldDurationMillis()
  {
    if (getState() != Call.State.HOLDING) {
      return 0L;
    }
    return SystemClock.elapsedRealtime() - mHoldingStartTime;
  }
  
  public int getNumberPresentation()
  {
    return mNumberPresentation;
  }
  
  public Connection getOrigConnection()
  {
    return mOrigConnection;
  }
  
  public String getOrigDialString()
  {
    return mDialString;
  }
  
  public int getPreciseDisconnectCause()
  {
    return mPreciseCause;
  }
  
  public String getRemainingPostDialString()
  {
    String str1 = super.getRemainingPostDialString();
    String str2 = str1;
    if (!isPhoneTypeGsm())
    {
      str2 = str1;
      if (!TextUtils.isEmpty(str1))
      {
        int i = str1.indexOf(';');
        int j = str1.indexOf(',');
        if ((i > 0) && ((i < j) || (j <= 0)))
        {
          str2 = str1.substring(0, i);
        }
        else
        {
          str2 = str1;
          if (j > 0) {
            str2 = str1.substring(0, j);
          }
        }
      }
    }
    return str2;
  }
  
  public Call.State getState()
  {
    if (mDisconnected) {
      return Call.State.DISCONNECTED;
    }
    return super.getState();
  }
  
  public UUSInfo getUUSInfo()
  {
    return mUusInfo;
  }
  
  public String getVendorDisconnectCause()
  {
    return mVendorCause;
  }
  
  public void hangup()
    throws CallStateException
  {
    if (!mDisconnected)
    {
      mOwner.hangup(this);
      return;
    }
    throw new CallStateException("disconnected");
  }
  
  public boolean isMultiparty()
  {
    if (mOrigConnection != null) {
      return mOrigConnection.isMultiparty();
    }
    return false;
  }
  
  public void migrateFrom(Connection paramConnection)
  {
    if (paramConnection == null) {
      return;
    }
    super.migrateFrom(paramConnection);
    mUusInfo = paramConnection.getUUSInfo();
    setUserData(paramConnection.getUserData());
  }
  
  void onConnectedInOrOut()
  {
    mConnectTime = System.currentTimeMillis();
    mConnectTimeReal = SystemClock.elapsedRealtime();
    mDuration = 0L;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onConnectedInOrOut: connectTime=");
    localStringBuilder.append(mConnectTime);
    log(localStringBuilder.toString());
    if (!mIsIncoming) {
      processNextPostDialChar();
    } else {
      releaseWakeLock();
    }
  }
  
  public boolean onDisconnect(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    mCause = paramInt;
    if (!mDisconnected)
    {
      doDisconnect();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDisconnect: cause=");
      localStringBuilder.append(paramInt);
      Rlog.d("GsmCdmaConnection", localStringBuilder.toString());
      mOwner.getPhone().notifyDisconnect(this);
      notifyDisconnect(paramInt);
      bool1 = bool2;
      if (mParent != null) {
        bool1 = mParent.connectionDisconnected(this);
      }
      mOrigConnection = null;
    }
    clearPostDialListeners();
    releaseWakeLock();
    return bool1;
  }
  
  void onHangupLocal()
  {
    mCause = 3;
    mPreciseCause = 0;
    mVendorCause = null;
  }
  
  void onLocalDisconnect()
  {
    if (!mDisconnected)
    {
      doDisconnect();
      if (mParent != null) {
        mParent.detach(this);
      }
    }
    releaseWakeLock();
  }
  
  void onRemoteDisconnect(int paramInt, String paramString)
  {
    mPreciseCause = paramInt;
    mVendorCause = paramString;
    onDisconnect(disconnectCauseFromCode(paramInt));
  }
  
  void onStartedHolding()
  {
    mHoldingStartTime = SystemClock.elapsedRealtime();
  }
  
  public void proceedAfterWaitChar()
  {
    if (mPostDialState != Connection.PostDialState.WAIT)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GsmCdmaConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WAIT but was ");
      localStringBuilder.append(mPostDialState);
      Rlog.w("GsmCdmaConnection", localStringBuilder.toString());
      return;
    }
    setPostDialState(Connection.PostDialState.STARTED);
    processNextPostDialChar();
  }
  
  public void proceedAfterWildChar(String paramString)
  {
    if (mPostDialState != Connection.PostDialState.WILD)
    {
      paramString = new StringBuilder();
      paramString.append("GsmCdmaConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WILD but was ");
      paramString.append(mPostDialState);
      Rlog.w("GsmCdmaConnection", paramString.toString());
      return;
    }
    setPostDialState(Connection.PostDialState.STARTED);
    paramString = new StringBuilder(paramString);
    paramString.append(mPostDialString.substring(mNextPostDialChar));
    mPostDialString = paramString.toString();
    mNextPostDialChar = 0;
    paramString = new StringBuilder();
    paramString.append("proceedAfterWildChar: new postDialString is ");
    paramString.append(mPostDialString);
    log(paramString.toString());
    processNextPostDialChar();
  }
  
  public void separate()
    throws CallStateException
  {
    if (!mDisconnected)
    {
      mOwner.separate(this);
      return;
    }
    throw new CallStateException("disconnected");
  }
  
  public boolean update(DriverCall paramDriverCall)
  {
    int i = 0;
    boolean bool1 = isConnectingInOrOut();
    Object localObject1 = getState();
    Object localObject2 = Call.State.HOLDING;
    boolean bool2 = false;
    boolean bool3;
    if (localObject1 == localObject2) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    localObject2 = parentFromDCState(state);
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("parent= ");
    ((StringBuilder)localObject1).append(mParent);
    ((StringBuilder)localObject1).append(", newParent= ");
    ((StringBuilder)localObject1).append(localObject2);
    log(((StringBuilder)localObject1).toString());
    int j;
    if ((isPhoneTypeGsm()) && (mOrigConnection != null))
    {
      log("update: mOrigConnection is not null");
      j = i;
    }
    else
    {
      j = i;
      if (isIncoming())
      {
        j = i;
        if (!equalsBaseDialString(mAddress, number)) {
          if (mNumberConverted)
          {
            j = i;
            if (equalsBaseDialString(mConvertedNumber, number)) {}
          }
          else
          {
            log("update: phone # changed!");
            mAddress = number;
            j = 1;
          }
        }
      }
    }
    i = getAudioQualityFromDC(audioQuality);
    if (getAudioQuality() != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("update: audioQuality # changed!:  ");
      if (i == 2) {
        localObject1 = "high";
      } else {
        localObject1 = "standard";
      }
      localStringBuilder.append((String)localObject1);
      log(localStringBuilder.toString());
      setAudioQuality(i);
      j = 1;
    }
    if (TextUtils.isEmpty(name))
    {
      if (!TextUtils.isEmpty(mCnapName))
      {
        j = 1;
        mCnapName = "";
      }
    }
    else if (!name.equals(mCnapName))
    {
      j = 1;
      mCnapName = name;
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("--dssds----");
    ((StringBuilder)localObject1).append(mCnapName);
    log(((StringBuilder)localObject1).toString());
    mCnapNamePresentation = namePresentation;
    mNumberPresentation = numberPresentation;
    boolean bool4;
    if (localObject2 != mParent)
    {
      if (mParent != null) {
        mParent.detach(this);
      }
      ((GsmCdmaCall)localObject2).attach(this, paramDriverCall);
      mParent = ((GsmCdmaCall)localObject2);
      bool4 = true;
    }
    else
    {
      bool4 = mParent.update(this, paramDriverCall);
      if ((j == 0) && (!bool4)) {
        bool4 = false;
      } else {
        bool4 = true;
      }
    }
    paramDriverCall = new StringBuilder();
    paramDriverCall.append("update: parent=");
    paramDriverCall.append(mParent);
    paramDriverCall.append(", hasNewParent=");
    if (localObject2 != mParent) {
      bool2 = true;
    }
    paramDriverCall.append(bool2);
    paramDriverCall.append(", wasConnectingInOrOut=");
    paramDriverCall.append(bool1);
    paramDriverCall.append(", wasHolding=");
    paramDriverCall.append(bool3);
    paramDriverCall.append(", isConnectingInOrOut=");
    paramDriverCall.append(isConnectingInOrOut());
    paramDriverCall.append(", changed=");
    paramDriverCall.append(bool4);
    log(paramDriverCall.toString());
    if ((bool1) && (!isConnectingInOrOut())) {
      onConnectedInOrOut();
    }
    if ((bool4) && (!bool3) && (getState() == Call.State.HOLDING)) {
      onStartedHolding();
    }
    return bool4;
  }
  
  public void updateParent(GsmCdmaCall paramGsmCdmaCall1, GsmCdmaCall paramGsmCdmaCall2)
  {
    if (paramGsmCdmaCall2 != paramGsmCdmaCall1)
    {
      if (paramGsmCdmaCall1 != null) {
        paramGsmCdmaCall1.detach(this);
      }
      paramGsmCdmaCall2.attachFake(this, Call.State.ACTIVE);
      mParent = paramGsmCdmaCall2;
    }
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        GsmCdmaConnection.this.releaseWakeLock();
        break;
      case 2: 
      case 3: 
      case 5: 
        GsmCdmaConnection.this.processNextPostDialChar();
        break;
      case 1: 
        mHandler.sendMessageDelayed(mHandler.obtainMessage(5), mDtmfToneDelay);
      }
    }
  }
}
