package com.android.internal.telephony.test;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import com.android.internal.telephony.DriverCall;
import java.util.ArrayList;
import java.util.List;

class SimulatedGsmCallState
  extends Handler
{
  static final int CONNECTING_PAUSE_MSEC = 500;
  static final int EVENT_PROGRESS_CALL_STATE = 1;
  static final int MAX_CALLS = 7;
  private boolean mAutoProgressConnecting = true;
  CallInfo[] mCalls = new CallInfo[7];
  private boolean mNextDialFailImmediately;
  
  public SimulatedGsmCallState(Looper paramLooper)
  {
    super(paramLooper);
  }
  
  private int countActiveLines()
    throws InvalidStateEx
  {
    int i = 0;
    int j = 0;
    int k = 0;
    boolean bool1 = false;
    int m = 0;
    boolean bool2 = false;
    int n = 0;
    while (n < mCalls.length)
    {
      CallInfo localCallInfo = mCalls[n];
      int i1 = i;
      boolean bool3 = bool2;
      int i2 = m;
      boolean bool4 = bool1;
      i3 = j;
      int i4 = k;
      if (localCallInfo != null)
      {
        i4 = 1;
        if ((!bool2) && (mIsMpty))
        {
          if (mState == CallInfo.State.HOLDING) {
            j = 1;
          } else {
            j = 0;
          }
          i3 = j;
        }
        else
        {
          if ((mIsMpty) && (j != 0) && (mState == CallInfo.State.ACTIVE))
          {
            Rlog.e("ModelInterpreter", "Invalid state");
            throw new InvalidStateEx();
          }
          i3 = j;
          if (!mIsMpty)
          {
            i3 = j;
            if (bool2)
            {
              i3 = j;
              if (j != 0) {
                if (mState != CallInfo.State.HOLDING)
                {
                  i3 = j;
                }
                else
                {
                  Rlog.e("ModelInterpreter", "Invalid state");
                  throw new InvalidStateEx();
                }
              }
            }
          }
        }
        bool3 = bool2 | mIsMpty;
        if (mState == CallInfo.State.HOLDING) {
          j = 1;
        } else {
          j = 0;
        }
        i1 = i | j;
        if (mState == CallInfo.State.ACTIVE) {
          j = i4;
        } else {
          j = 0;
        }
        i2 = m | j;
        bool4 = bool1 | localCallInfo.isConnecting();
        i4 = k | localCallInfo.isRinging();
      }
      n++;
      i = i1;
      bool2 = bool3;
      m = i2;
      bool1 = bool4;
      j = i3;
      k = i4;
    }
    int i3 = 0;
    if (i != 0) {
      i3 = 0 + 1;
    }
    j = i3;
    if (m != 0) {
      j = i3 + 1;
    }
    i3 = j;
    if (bool1) {
      i3 = j + 1;
    }
    j = i3;
    if (k != 0) {
      j = i3 + 1;
    }
    return j;
  }
  
  public boolean conference()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    CallInfo localCallInfo;
    while (k < mCalls.length)
    {
      localCallInfo = mCalls[k];
      m = j;
      if (localCallInfo != null)
      {
        m = j + 1;
        if (localCallInfo.isConnecting()) {
          return false;
        }
      }
      k++;
      j = m;
    }
    for (int m = i; m < mCalls.length; m++)
    {
      localCallInfo = mCalls[m];
      if (localCallInfo != null)
      {
        mState = CallInfo.State.ACTIVE;
        if (j > 0) {
          mIsMpty = true;
        }
      }
    }
    return true;
  }
  
  public boolean explicitCallTransfer()
  {
    int i = 0;
    int j = 0;
    while (j < mCalls.length)
    {
      CallInfo localCallInfo = mCalls[j];
      int k = i;
      if (localCallInfo != null)
      {
        k = i + 1;
        if (localCallInfo.isConnecting()) {
          return false;
        }
      }
      j++;
      i = k;
    }
    return triggerHangupAll();
  }
  
  public List<String> getClccLines()
  {
    ArrayList localArrayList = new ArrayList(mCalls.length);
    for (int i = 0; i < mCalls.length; i++)
    {
      CallInfo localCallInfo = mCalls[i];
      if (localCallInfo != null) {
        localArrayList.add(localCallInfo.toCLCCLine(i + 1));
      }
    }
    return localArrayList;
  }
  
  public List<DriverCall> getDriverCalls()
  {
    ArrayList localArrayList = new ArrayList(mCalls.length);
    for (int i = 0; i < mCalls.length; i++)
    {
      localObject = mCalls[i];
      if (localObject != null) {
        localArrayList.add(((CallInfo)localObject).toDriverCall(i + 1));
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("SC< getDriverCalls ");
    ((StringBuilder)localObject).append(localArrayList);
    Rlog.d("GSM", ((StringBuilder)localObject).toString());
    return localArrayList;
  }
  
  public void handleMessage(Message paramMessage)
  {
    try
    {
      if (what == 1) {
        progressConnectingCallState();
      }
      return;
    }
    finally {}
  }
  
  public boolean onAnswer()
  {
    int i = 0;
    try
    {
      while (i < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[i];
        if ((localCallInfo != null) && ((mState == CallInfo.State.INCOMING) || (mState == CallInfo.State.WAITING)))
        {
          boolean bool = switchActiveAndHeldOrWaiting();
          return bool;
        }
        i++;
      }
      return false;
    }
    finally {}
  }
  
  public boolean onChld(char paramChar1, char paramChar2)
  {
    int i = 0;
    boolean bool = false;
    if (paramChar2 != 0)
    {
      int j = paramChar2 - '1';
      if (j >= 0)
      {
        i = j;
        if (j < mCalls.length) {}
      }
      else
      {
        return false;
      }
    }
    switch (paramChar1)
    {
    default: 
      break;
    case '5': 
      bool = false;
      break;
    case '4': 
      bool = explicitCallTransfer();
      break;
    case '3': 
      bool = conference();
      break;
    case '2': 
      if (paramChar2 <= 0) {
        bool = switchActiveAndHeldOrWaiting();
      } else {
        bool = separateCall(i);
      }
      break;
    case '1': 
      if (paramChar2 <= 0) {
        bool = releaseActiveAcceptHeldOrWaiting();
      }
      break;
    }
    for (;;)
    {
      break;
      if (mCalls[i] == null)
      {
        bool = false;
      }
      else
      {
        mCalls[i] = null;
        bool = true;
        break;
        bool = releaseHeldOrUDUB();
      }
    }
    return bool;
  }
  
  public boolean onDial(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SC> dial '");
    localStringBuilder.append(paramString);
    localStringBuilder.append("'");
    Rlog.d("GSM", localStringBuilder.toString());
    if (mNextDialFailImmediately)
    {
      mNextDialFailImmediately = false;
      Rlog.d("GSM", "SC< dial fail (per request)");
      return false;
    }
    paramString = PhoneNumberUtils.extractNetworkPortion(paramString);
    if (paramString.length() == 0)
    {
      Rlog.d("GSM", "SC< dial fail (invalid ph num)");
      return false;
    }
    if ((paramString.startsWith("*99")) && (paramString.endsWith("#")))
    {
      Rlog.d("GSM", "SC< dial ignored (gprs)");
      return true;
    }
    try
    {
      if (countActiveLines() > 1)
      {
        Rlog.d("GSM", "SC< dial fail (invalid call state)");
        return false;
      }
      int i = -1;
      int j = 0;
      while (j < mCalls.length)
      {
        int k = i;
        if (i < 0)
        {
          k = i;
          if (mCalls[j] == null) {
            k = j;
          }
        }
        if ((mCalls[j] != null) && (!mCalls[j].isActiveOrHeld()))
        {
          Rlog.d("GSM", "SC< dial fail (invalid call state)");
          return false;
        }
        if ((mCalls[j] != null) && (mCalls[j].mState == CallInfo.State.ACTIVE)) {
          mCalls[j].mState = CallInfo.State.HOLDING;
        }
        j++;
        i = k;
      }
      if (i < 0)
      {
        Rlog.d("GSM", "SC< dial fail (invalid call state)");
        return false;
      }
      mCalls[i] = CallInfo.createOutgoingCall(paramString);
      if (mAutoProgressConnecting) {
        sendMessageDelayed(obtainMessage(1, mCalls[i]), 500L);
      }
      paramString = new StringBuilder();
      paramString.append("SC< dial (slot = ");
      paramString.append(i);
      paramString.append(")");
      Rlog.d("GSM", paramString.toString());
      return true;
    }
    catch (InvalidStateEx paramString)
    {
      Rlog.d("GSM", "SC< dial fail (invalid call state)");
    }
    return false;
  }
  
  public boolean onHangup()
  {
    boolean bool1 = false;
    int i = 0;
    while (i < mCalls.length)
    {
      CallInfo localCallInfo = mCalls[i];
      boolean bool2 = bool1;
      if (localCallInfo != null)
      {
        bool2 = bool1;
        if (mState != CallInfo.State.WAITING)
        {
          mCalls[i] = null;
          bool2 = true;
        }
      }
      i++;
      bool1 = bool2;
    }
    return bool1;
  }
  
  public void progressConnectingCallState()
  {
    int i = 0;
    try
    {
      while (i < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[i];
        if ((localCallInfo != null) && (mState == CallInfo.State.DIALING))
        {
          mState = CallInfo.State.ALERTING;
          if (!mAutoProgressConnecting) {
            break;
          }
          sendMessageDelayed(obtainMessage(1, localCallInfo), 500L);
          break;
        }
        if ((localCallInfo != null) && (mState == CallInfo.State.ALERTING))
        {
          mState = CallInfo.State.ACTIVE;
          break;
        }
        i++;
      }
      return;
    }
    finally {}
  }
  
  public void progressConnectingToActive()
  {
    int i = 0;
    try
    {
      while (i < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[i];
        if ((localCallInfo != null) && ((mState == CallInfo.State.DIALING) || (mState == CallInfo.State.ALERTING)))
        {
          mState = CallInfo.State.ACTIVE;
          break;
        }
        i++;
      }
      return;
    }
    finally {}
  }
  
  public boolean releaseActiveAcceptHeldOrWaiting()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    CallInfo localCallInfo;
    while (k < mCalls.length)
    {
      localCallInfo = mCalls[k];
      m = j;
      if (localCallInfo != null)
      {
        m = j;
        if (mState == CallInfo.State.ACTIVE)
        {
          mCalls[k] = null;
          m = 1;
        }
      }
      k++;
      j = m;
    }
    if (j == 0) {
      for (j = 0; j < mCalls.length; j++)
      {
        localCallInfo = mCalls[j];
        if ((localCallInfo != null) && ((mState == CallInfo.State.DIALING) || (mState == CallInfo.State.ALERTING))) {
          mCalls[j] = null;
        }
      }
    }
    j = 0;
    int m = 0;
    while (m < mCalls.length)
    {
      localCallInfo = mCalls[m];
      k = j;
      if (localCallInfo != null)
      {
        k = j;
        if (mState == CallInfo.State.HOLDING)
        {
          mState = CallInfo.State.ACTIVE;
          k = 1;
        }
      }
      m++;
      j = k;
    }
    if (j != 0) {
      return true;
    }
    for (j = i; j < mCalls.length; j++)
    {
      localCallInfo = mCalls[j];
      if ((localCallInfo != null) && (localCallInfo.isRinging()))
      {
        mState = CallInfo.State.ACTIVE;
        return true;
      }
    }
    return true;
  }
  
  public boolean releaseHeldOrUDUB()
  {
    int i = 0;
    int j = 0;
    int m;
    CallInfo localCallInfo;
    for (int k = 0;; k++)
    {
      m = i;
      if (k >= mCalls.length) {
        break;
      }
      localCallInfo = mCalls[k];
      if ((localCallInfo != null) && (localCallInfo.isRinging()))
      {
        m = 1;
        mCalls[k] = null;
        break;
      }
    }
    if (m == 0) {
      for (k = j; k < mCalls.length; k++)
      {
        localCallInfo = mCalls[k];
        if ((localCallInfo != null) && (mState == CallInfo.State.HOLDING)) {
          mCalls[k] = null;
        }
      }
    }
    return true;
  }
  
  public boolean separateCall(int paramInt)
  {
    try
    {
      CallInfo localCallInfo = mCalls[paramInt];
      if ((localCallInfo != null) && (!localCallInfo.isConnecting()) && (countActiveLines() == 1))
      {
        mState = CallInfo.State.ACTIVE;
        mIsMpty = false;
        for (int i = 0; i < mCalls.length; i++)
        {
          int j = 0;
          int k = 0;
          int m = j;
          int n = k;
          if (i != paramInt)
          {
            localCallInfo = mCalls[i];
            m = j;
            n = k;
            if (localCallInfo != null)
            {
              m = j;
              n = k;
              if (mState == CallInfo.State.ACTIVE)
              {
                mState = CallInfo.State.HOLDING;
                m = 0 + 1;
                n = i;
              }
            }
          }
          if (m == 1) {
            mCalls[n].mIsMpty = false;
          }
        }
        return true;
      }
      return false;
    }
    catch (InvalidStateEx localInvalidStateEx) {}
    return false;
  }
  
  public void setAutoProgressConnectingCall(boolean paramBoolean)
  {
    mAutoProgressConnecting = paramBoolean;
  }
  
  public void setNextDialFailImmediately(boolean paramBoolean)
  {
    mNextDialFailImmediately = paramBoolean;
  }
  
  public boolean switchActiveAndHeldOrWaiting()
  {
    int i = 0;
    int j = 0;
    int m;
    CallInfo localCallInfo;
    for (int k = 0;; k++)
    {
      m = i;
      if (k >= mCalls.length) {
        break;
      }
      localCallInfo = mCalls[k];
      if ((localCallInfo != null) && (mState == CallInfo.State.HOLDING))
      {
        m = 1;
        break;
      }
    }
    for (k = j; k < mCalls.length; k++)
    {
      localCallInfo = mCalls[k];
      if (localCallInfo != null) {
        if (mState == CallInfo.State.ACTIVE) {
          mState = CallInfo.State.HOLDING;
        } else if (mState == CallInfo.State.HOLDING) {
          mState = CallInfo.State.ACTIVE;
        } else if ((m == 0) && (localCallInfo.isRinging())) {
          mState = CallInfo.State.ACTIVE;
        }
      }
    }
    return true;
  }
  
  public boolean triggerHangupAll()
  {
    boolean bool = false;
    int i = 0;
    try
    {
      while (i < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[i];
        if (mCalls[i] != null) {
          bool = true;
        }
        mCalls[i] = null;
        i++;
      }
      return bool;
    }
    finally {}
  }
  
  public boolean triggerHangupBackground()
  {
    boolean bool1 = false;
    int i = 0;
    try
    {
      while (i < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[i];
        boolean bool2 = bool1;
        if (localCallInfo != null)
        {
          bool2 = bool1;
          if (mState == CallInfo.State.HOLDING)
          {
            mCalls[i] = null;
            bool2 = true;
          }
        }
        i++;
        bool1 = bool2;
      }
      return bool1;
    }
    finally {}
  }
  
  public boolean triggerHangupForeground()
  {
    int i = 0;
    boolean bool1 = false;
    int j = 0;
    try
    {
      CallInfo localCallInfo;
      while (j < mCalls.length)
      {
        localCallInfo = mCalls[j];
        bool2 = bool1;
        if (localCallInfo != null) {
          if (mState != CallInfo.State.INCOMING)
          {
            bool2 = bool1;
            if (mState != CallInfo.State.WAITING) {}
          }
          else
          {
            mCalls[j] = null;
            bool2 = true;
          }
        }
        j++;
        bool1 = bool2;
      }
      boolean bool2 = bool1;
      j = i;
      while (j < mCalls.length)
      {
        localCallInfo = mCalls[j];
        bool1 = bool2;
        if (localCallInfo != null) {
          if ((mState != CallInfo.State.DIALING) && (mState != CallInfo.State.ACTIVE))
          {
            bool1 = bool2;
            if (mState != CallInfo.State.ALERTING) {}
          }
          else
          {
            mCalls[j] = null;
            bool1 = true;
          }
        }
        j++;
        bool2 = bool1;
      }
      return bool2;
    }
    finally {}
  }
  
  public boolean triggerRing(String paramString)
  {
    int i = 0;
    int j = -1;
    int k = 0;
    try
    {
      while (k < mCalls.length)
      {
        CallInfo localCallInfo = mCalls[k];
        int m;
        if ((localCallInfo == null) && (j < 0))
        {
          m = k;
        }
        else
        {
          if ((localCallInfo != null) && ((mState == CallInfo.State.INCOMING) || (mState == CallInfo.State.WAITING)))
          {
            Rlog.w("ModelInterpreter", "triggerRing failed; phone already ringing");
            return false;
          }
          m = j;
          if (localCallInfo != null)
          {
            i = 1;
            m = j;
          }
        }
        k++;
        j = m;
      }
      if (j < 0)
      {
        Rlog.w("ModelInterpreter", "triggerRing failed; all full");
        return false;
      }
      mCalls[j] = CallInfo.createIncomingCall(PhoneNumberUtils.extractNetworkPortion(paramString));
      if (i != 0) {
        mCalls[j].mState = CallInfo.State.WAITING;
      }
      return true;
    }
    finally {}
  }
}
