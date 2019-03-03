package com.android.internal.telephony;

import android.hardware.radio.V1_2.DataRegStateResult;
import android.hardware.radio.V1_2.VoiceRegStateResult;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RegistrantList;
import android.telephony.Rlog;
import com.android.internal.telephony.uicc.IccCardStatus.CardState;
import java.util.Arrays;

public class DnoHandler
  extends Handler
{
  static final int DATA_STATE_FIELDS = 11;
  static final int DELAY_SERVICE_TIMEOUT = 45;
  static final int DELAY_SERVICE_TIMEOUT_MO = 20;
  protected static final int EVENT_CGREG_TIMER_EXPIRE = 1002;
  protected static final int EVENT_CREG_TIMER_EXPIRE = 1001;
  static final String LOG_TAG = "DNO";
  static final int OPERAROR_FIELDS = 3;
  private static final int SUBSCRIPTION_ACTIVATED = 1;
  private static final int SUBSCRIPTION_DEACTIVATED = 0;
  static final int VOICE_STATE_FIELDS = 15;
  private Object delay_cs_signal = new Object();
  private IccCardStatus.CardState mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
  private int mDataRegState = 4;
  private boolean mHasVoiceCall = false;
  protected Integer mInstanceId;
  private int mNumDataCalls = 0;
  private boolean mPinLockState = false;
  private CommandsInterface.RadioState mRadioState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
  private int mRegState = 4;
  private int mSubStatus = 1;
  private RegistrantList mVoiceNetworkStateRegistrants;
  private int mVoiceTech = 0;
  private Object ril_cache_lock = new Object();
  private ril_snapshot_asus ril_snapshot_asus_t = new ril_snapshot_asus();
  
  public DnoHandler(RegistrantList paramRegistrantList, Integer paramInteger)
  {
    mVoiceNetworkStateRegistrants = paramRegistrantList;
    mInstanceId = paramInteger;
  }
  
  public static boolean isRegistered(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 5) {
      if (paramInt == 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DNO[");
    localStringBuilder.append(mInstanceId);
    localStringBuilder.append("]");
    Rlog.e(localStringBuilder.toString(), paramString);
  }
  
  private void logi(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DNO[");
    localStringBuilder.append(mInstanceId);
    localStringBuilder.append("]");
    Rlog.i(localStringBuilder.toString(), paramString);
  }
  
  public int getVoiceTech()
  {
    return mVoiceTech;
  }
  
  public boolean get_cgreg_timer_running()
  {
    synchronized (ril_cache_lock)
    {
      boolean bool = ril_snapshot_asus_t.cgreg_timer_running;
      return bool;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleMessage ");
    localStringBuilder.append(what);
    logi(localStringBuilder.toString());
    switch (what)
    {
    default: 
      break;
    case 1002: 
      handle_cgreg_timer_expire();
      break;
    case 1001: 
      handle_creg_timer_expire();
    }
  }
  
  public void handle_cgreg_timer_expire()
  {
    synchronized (ril_cache_lock)
    {
      ril_snapshot_asus.access$102(ril_snapshot_asus_t, false);
      ril_snapshot_asus.access$602(ril_snapshot_asus_t, true);
      if (!ril_snapshot_asus_t.creg_valid) {
        ril_snapshot_asus.access$902(ril_snapshot_asus_t, false);
      }
      boolean bool = ril_snapshot_asus_t.cgreg_valid;
      ril_snapshot_asus.access$502(ril_snapshot_asus_t, false);
      if (bool)
      {
        logi("Delay PS timer expire");
        mVoiceNetworkStateRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
      }
      return;
    }
  }
  
  public void handle_creg_timer_expire()
  {
    synchronized (ril_cache_lock)
    {
      ril_snapshot_asus.access$002(ril_snapshot_asus_t, false);
      ril_snapshot_asus.access$302(ril_snapshot_asus_t, true);
      if (!ril_snapshot_asus_t.cgreg_valid) {
        ril_snapshot_asus.access$902(ril_snapshot_asus_t, false);
      }
      boolean bool = ril_snapshot_asus_t.creg_valid;
      ril_snapshot_asus.access$202(ril_snapshot_asus_t, false);
      synchronized (delay_cs_signal)
      {
        delay_cs_signal.notify();
        if (bool)
        {
          logi("Delay CS timer expire");
          mVoiceNetworkStateRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
        }
        return;
      }
    }
  }
  
  public void hanupWaitingCall()
  {
    logi("hanupWaitingCall");
    if (ril_snapshot_asus_t.creg_timer_running) {
      synchronized (delay_cs_signal)
      {
        delay_cs_signal.notify();
      }
    }
  }
  
  public boolean isConsideredRegistered()
  {
    boolean bool;
    if ((!isRegistered(mRegState)) && (!isRegistered(mDataRegState))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isPinLock()
  {
    return mPinLockState;
  }
  
  public boolean is_delay_no_service_pending()
  {
    boolean bool;
    if ((!isRegistered(mRegState)) && (ril_snapshot_asus_t.creg_timer_running) && (ril_snapshot_asus_t.creg_valid)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean is_ril_snapshot_valid()
  {
    IccCardStatus.CardState localCardState1 = mCardState;
    IccCardStatus.CardState localCardState2 = IccCardStatus.CardState.CARDSTATE_PRESENT;
    boolean bool = true;
    if ((localCardState1 != localCardState2) || (mRadioState != CommandsInterface.RadioState.RADIO_ON) || (mSubStatus != 1)) {
      bool = false;
    }
    return bool;
  }
  
  public DataRegStateResult response_data_registration_state(DataRegStateResult paramDataRegStateResult)
  {
    Object localObject = ril_cache_lock;
    int i = 0;
    try
    {
      if ((mDataRegState == 5) || (mDataRegState == 1)) {
        i = 1;
      }
      if (i != 0)
      {
        if (ril_snapshot_asus_t.cgreg_timer_running)
        {
          logi("Cancel Delay PS");
          removeMessages(1002);
          ril_snapshot_asus.access$102(ril_snapshot_asus_t, false);
          ril_snapshot_asus.access$502(ril_snapshot_asus_t, false);
        }
        ril_snapshot_asus.access$602(ril_snapshot_asus_t, false);
      }
      else if ((is_ril_snapshot_valid()) && (!ril_snapshot_asus_t.cgreg_timer_running) && (!ril_snapshot_asus_t.cgreg_timer_expire) && (ril_snapshot_asus_t.cgreg_valid) && (mNumDataCalls > 0) && (!mHasVoiceCall) && (!isPinLock()))
      {
        logi("Setup Delay PS with 45");
        ril_snapshot_asus.access$102(ril_snapshot_asus_t, sendMessageDelayed(obtainMessage(1002), 45000L));
      }
      if (is_ril_snapshot_valid()) {
        if ((mDataRegState != 5) && (mDataRegState != 1))
        {
          if ((mNumDataCalls > 0) && (!mHasVoiceCall) && (ril_snapshot_asus_t.cgreg_timer_running) && (ril_snapshot_asus_t.cgreg_valid))
          {
            logi("Use cgreg snapshot");
            new DataRegStateResult();
            paramDataRegStateResult = ril_snapshot_asus_t.cgreg;
            return paramDataRegStateResult;
          }
        }
        else
        {
          ril_snapshot_asus.access$702(ril_snapshot_asus_t, paramDataRegStateResult);
          ril_snapshot_asus.access$502(ril_snapshot_asus_t, true);
        }
      }
      return null;
    }
    finally {}
  }
  
  public String[] response_operator(String[] paramArrayOfString)
  {
    synchronized (ril_cache_lock)
    {
      if (is_ril_snapshot_valid()) {
        if ((paramArrayOfString.length > 0) && (!paramArrayOfString[0].equals("")))
        {
          System.arraycopy(paramArrayOfString, 0, ril_snapshot_asus_t.cops, 0, paramArrayOfString.length);
          ril_snapshot_asus.access$902(ril_snapshot_asus_t, true);
        }
        else if (((ril_snapshot_asus_t.creg_timer_running) || (ril_snapshot_asus_t.cgreg_timer_running)) && (ril_snapshot_asus_t.cops_valid))
        {
          logi("Use cops snapshot");
          paramArrayOfString = new String[3];
          System.arraycopy(ril_snapshot_asus_t.cops, 0, paramArrayOfString, 0, ril_snapshot_asus_t.cops.length);
          return paramArrayOfString;
        }
      }
      return null;
    }
  }
  
  public VoiceRegStateResult response_registration_state(VoiceRegStateResult paramVoiceRegStateResult)
  {
    Object localObject1 = ril_cache_lock;
    int i = 0;
    try
    {
      if ((mRegState == 5) || (mRegState == 1)) {
        i = 1;
      }
      if (i != 0)
      {
        if (ril_snapshot_asus_t.creg_timer_running)
        {
          logi("Cancel Delay CS");
          removeMessages(1001);
          ril_snapshot_asus.access$002(ril_snapshot_asus_t, false);
          ril_snapshot_asus.access$202(ril_snapshot_asus_t, false);
          synchronized (delay_cs_signal)
          {
            delay_cs_signal.notify();
          }
        }
        ril_snapshot_asus.access$302(ril_snapshot_asus_t, false);
      }
      else if ((is_ril_snapshot_valid()) && (!ril_snapshot_asus_t.creg_timer_running) && (!ril_snapshot_asus_t.creg_timer_expire) && (ril_snapshot_asus_t.creg_valid) && (!isPinLock()))
      {
        logi("Setup Delay CS with 45");
        ril_snapshot_asus.access$002(ril_snapshot_asus_t, sendMessageDelayed(obtainMessage(1001), 45000L));
      }
      if (is_ril_snapshot_valid()) {
        if ((mRegState != 5) && (mRegState != 1))
        {
          if ((ril_snapshot_asus_t.creg_timer_running) && (ril_snapshot_asus_t.creg_valid))
          {
            logi("Use creg snapshot");
            new VoiceRegStateResult();
            paramVoiceRegStateResult = ril_snapshot_asus_t.creg;
            return paramVoiceRegStateResult;
          }
        }
        else
        {
          ril_snapshot_asus.access$402(ril_snapshot_asus_t, paramVoiceRegStateResult);
          ril_snapshot_asus.access$202(ril_snapshot_asus_t, true);
        }
      }
      return null;
    }
    finally {}
  }
  
  public void ril_snapshot_clear_all()
  {
    logi("ril_snapshot_clear_all");
    synchronized (ril_cache_lock)
    {
      if (ril_snapshot_asus_t.creg_timer_running)
      {
        removeMessages(1001);
        synchronized (delay_cs_signal)
        {
          delay_cs_signal.notify();
        }
      }
      if (ril_snapshot_asus_t.cgreg_timer_running) {
        removeMessages(1002);
      }
      ril_snapshot_asus_t.reset();
      return;
    }
  }
  
  public void ril_snapshot_clear_cgreg()
  {
    logi("ril_snapshot_clear_cgreg");
    synchronized (ril_cache_lock)
    {
      if (ril_snapshot_asus_t.cgreg_timer_running) {
        removeMessages(1002);
      }
      ril_snapshot_asus_t.reset_cgreg();
      return;
    }
  }
  
  public void ril_snapshot_clear_creg()
  {
    logi("ril_snapshot_clear_creg");
    synchronized (ril_cache_lock)
    {
      if (ril_snapshot_asus_t.creg_timer_running)
      {
        removeMessages(1001);
        synchronized (delay_cs_signal)
        {
          delay_cs_signal.notify();
        }
      }
      ril_snapshot_asus_t.reset_creg();
      return;
    }
  }
  
  public void setCardState(IccCardStatus.CardState paramCardState)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setCardState:");
    localStringBuilder.append(paramCardState);
    logi(localStringBuilder.toString());
    if ((paramCardState == IccCardStatus.CardState.CARDSTATE_ABSENT) && (mCardState == IccCardStatus.CardState.CARDSTATE_PRESENT))
    {
      ril_snapshot_clear_all();
      mVoiceNetworkStateRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    mCardState = paramCardState;
  }
  
  public void setDataRegState(int paramInt)
  {
    mDataRegState = paramInt;
  }
  
  public void setHasVoiceCall(boolean paramBoolean)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("setHasVoiceCall:");
    ((StringBuilder)???).append(paramBoolean);
    logi(((StringBuilder)???).toString());
    if (mHasVoiceCall == paramBoolean) {
      return;
    }
    mHasVoiceCall = paramBoolean;
    if (mHasVoiceCall) {
      synchronized (ril_cache_lock)
      {
        if (ril_snapshot_asus_t.cgreg_timer_running)
        {
          logi("Cancel Delay PS because of CS call");
          removeMessages(1002);
          RegistrantList localRegistrantList = mVoiceNetworkStateRegistrants;
          AsyncResult localAsyncResult = new android/os/AsyncResult;
          localAsyncResult.<init>(null, null, null);
          localRegistrantList.notifyRegistrants(localAsyncResult);
        }
        ril_snapshot_asus_t.reset_cgreg();
      }
    }
  }
  
  public void setNetworkSelection()
  {
    logi("setNetworkSelection");
    ril_snapshot_clear_all();
  }
  
  public void setNumDataCalls(int paramInt)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("setNumDataCalls:");
    ((StringBuilder)???).append(paramInt);
    logi(((StringBuilder)???).toString());
    mNumDataCalls = paramInt;
    if (mNumDataCalls == 0) {
      synchronized (ril_cache_lock)
      {
        if (ril_snapshot_asus_t.cgreg_timer_running)
        {
          logi("Cancel Delay PS because number of data calls is zero");
          removeMessages(1002);
          RegistrantList localRegistrantList = mVoiceNetworkStateRegistrants;
          AsyncResult localAsyncResult = new android/os/AsyncResult;
          localAsyncResult.<init>(null, null, null);
          localRegistrantList.notifyRegistrants(localAsyncResult);
        }
        ril_snapshot_asus_t.reset_cgreg();
      }
    }
  }
  
  public void setPinLock(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setPinLock isLock=");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append(" mPinLockState=");
    localStringBuilder.append(mPinLockState);
    logi(localStringBuilder.toString());
    if (mPinLockState != paramBoolean)
    {
      if (paramBoolean == true) {
        ril_snapshot_clear_all();
      }
      mPinLockState = paramBoolean;
    }
  }
  
  public void setRadioState(CommandsInterface.RadioState paramRadioState)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setRadioState:");
    localStringBuilder.append(paramRadioState);
    logi(localStringBuilder.toString());
    if (paramRadioState == mRadioState) {
      return;
    }
    mRadioState = paramRadioState;
    if (mRadioState != CommandsInterface.RadioState.RADIO_ON) {
      ril_snapshot_clear_all();
    }
  }
  
  public void setRegState(int paramInt)
  {
    mRegState = paramInt;
  }
  
  public void setSubStatus(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
    {
      int i;
      if ((!ril_snapshot_asus_t.creg_timer_running) && (!ril_snapshot_asus_t.cgreg_timer_running)) {
        i = 0;
      } else {
        i = 1;
      }
      ril_snapshot_clear_all();
      if (i != 0)
      {
        logi("setSubStatus: DNO Cancel and Poll State[CS/PS/OP..] again");
        mVoiceNetworkStateRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setSubStatus[slot");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append("]:");
    localStringBuilder.append(paramInt2);
    logi(localStringBuilder.toString());
    mSubStatus = paramInt2;
  }
  
  public void setVoiceTech(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setVoiceTech:");
    localStringBuilder.append(paramInt);
    logi(localStringBuilder.toString());
    mVoiceTech = paramInt;
  }
  
  public boolean wait_cs_pending_event()
  {
    try
    {
      synchronized (delay_cs_signal)
      {
        delay_cs_signal.wait(20000L);
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      loge("InterruptedException");
      return isRegistered(mRegState);
    }
  }
  
  public class ril_snapshot_asus
  {
    private DataRegStateResult cgreg = new DataRegStateResult();
    private boolean cgreg_timer_expire = false;
    private boolean cgreg_timer_running = false;
    private boolean cgreg_valid = false;
    private String[] cops = new String[3];
    private boolean cops_valid = false;
    private VoiceRegStateResult creg = new VoiceRegStateResult();
    private boolean creg_timer_expire = false;
    private boolean creg_timer_running = false;
    private boolean creg_valid = false;
    
    ril_snapshot_asus() {}
    
    public void reset()
    {
      reset_creg();
      reset_cgreg();
      cops_valid = false;
      Arrays.fill(cops, null);
    }
    
    public void reset_cgreg()
    {
      cgreg_valid = false;
      cgreg = null;
      cgreg_timer_expire = false;
      cgreg_timer_running = false;
    }
    
    public void reset_creg()
    {
      creg_valid = false;
      creg = null;
      creg_timer_expire = false;
      creg_timer_running = false;
    }
  }
}
