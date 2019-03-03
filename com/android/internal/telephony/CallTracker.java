package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class CallTracker
  extends Handler
{
  private static final boolean DBG_POLL = false;
  protected static final int EVENT_CALL_STATE_CHANGE = 2;
  protected static final int EVENT_CALL_WAITING_INFO_CDMA = 15;
  protected static final int EVENT_CONFERENCE_RESULT = 11;
  protected static final int EVENT_ECT_RESULT = 13;
  protected static final int EVENT_EXIT_ECM_RESPONSE_CDMA = 14;
  protected static final int EVENT_GET_LAST_CALL_FAIL_CAUSE = 5;
  protected static final int EVENT_OPERATION_COMPLETE = 4;
  protected static final int EVENT_POLL_CALLS_RESULT = 1;
  protected static final int EVENT_RADIO_AVAILABLE = 9;
  protected static final int EVENT_RADIO_NOT_AVAILABLE = 10;
  protected static final int EVENT_REPOLL_AFTER_DELAY = 3;
  protected static final int EVENT_SEPARATE_RESULT = 12;
  protected static final int EVENT_SWITCH_RESULT = 8;
  protected static final int EVENT_THREE_WAY_DIAL_BLANK_FLASH = 20;
  protected static final int EVENT_THREE_WAY_DIAL_L2_RESULT_CDMA = 16;
  static final int POLL_DELAY_MSEC = 250;
  private final int VALID_COMPARE_LENGTH = 3;
  public CommandsInterface mCi;
  protected ArrayList<Connection> mHandoverConnections = new ArrayList();
  protected Message mLastRelevantPoll;
  protected boolean mNeedsPoll;
  protected boolean mNumberConverted = false;
  protected int mPendingOperations;
  
  public CallTracker() {}
  
  private boolean checkNoOperationsPending()
  {
    boolean bool;
    if (mPendingOperations == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean compareGid1(Phone paramPhone, String paramString)
  {
    String str = paramPhone.getGroupIdLevel1();
    int i = paramString.length();
    boolean bool = true;
    if ((paramString != null) && (!paramString.equals("")))
    {
      if ((str == null) || (str.length() < i) || (!str.substring(0, i).equalsIgnoreCase(paramString)))
      {
        paramPhone = new StringBuilder();
        paramPhone.append(" gid1 ");
        paramPhone.append(str);
        paramPhone.append(" serviceGid1 ");
        paramPhone.append(paramString);
        log(paramPhone.toString());
        bool = false;
      }
      paramString = new StringBuilder();
      paramString.append("compareGid1 is ");
      if (bool) {
        paramPhone = "Same";
      } else {
        paramPhone = "Different";
      }
      paramString.append(paramPhone);
      log(paramString.toString());
      return bool;
    }
    paramPhone = new StringBuilder();
    paramPhone.append("compareGid1 serviceGid is empty, return ");
    paramPhone.append(true);
    log(paramPhone.toString());
    return true;
  }
  
  protected String checkForTestEmergencyNumber(String paramString)
  {
    Object localObject1 = SystemProperties.get("ril.test.emergencynumber");
    Object localObject2 = paramString;
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      localObject1 = ((String)localObject1).split(":");
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("checkForTestEmergencyNumber: values.length=");
      ((StringBuilder)localObject2).append(localObject1.length);
      log(((StringBuilder)localObject2).toString());
      localObject2 = paramString;
      if (localObject1.length == 2)
      {
        localObject2 = paramString;
        if (localObject1[0].equals(PhoneNumberUtils.stripSeparators(paramString)))
        {
          if (mCi != null) {
            mCi.testingEmergencyCall();
          }
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("checkForTestEmergencyNumber: remap ");
          ((StringBuilder)localObject2).append(paramString);
          ((StringBuilder)localObject2).append(" to ");
          ((StringBuilder)localObject2).append(localObject1[1]);
          log(((StringBuilder)localObject2).toString());
          localObject2 = localObject1[1];
        }
      }
    }
    return localObject2;
  }
  
  public void cleanupCalls() {}
  
  protected String convertNumberIfNecessary(Phone paramPhone, String paramString)
  {
    if (paramString == null) {
      return paramString;
    }
    Object localObject1 = null;
    Object localObject2 = ((CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config")).getConfig();
    if (localObject2 != null) {
      localObject1 = ((PersistableBundle)localObject2).getStringArray("dial_string_replace_string_array");
    }
    if (localObject1 == null)
    {
      log("convertNumberIfNecessary convertMaps is null");
      return paramString;
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("convertNumberIfNecessary Roaming convertMaps.length ");
    ((StringBuilder)localObject2).append(localObject1.length);
    ((StringBuilder)localObject2).append(" dialNumber.length() ");
    ((StringBuilder)localObject2).append(paramString.length());
    log(((StringBuilder)localObject2).toString());
    if ((localObject1.length >= 1) && (paramString.length() >= 3))
    {
      String str = "";
      int i = localObject1.length;
      for (int j = 0;; j++)
      {
        localObject2 = str;
        if (j >= i) {
          break;
        }
        localObject2 = localObject1[j];
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("convertNumberIfNecessary: ");
        localStringBuilder.append((String)localObject2);
        log(localStringBuilder.toString());
        localObject2 = ((String)localObject2).split(":");
        if ((localObject2 != null) && (localObject2.length > 1))
        {
          localStringBuilder = localObject2[0];
          localObject2 = localObject2[1];
          if ((!TextUtils.isEmpty(localStringBuilder)) && (paramString.equals(localStringBuilder)))
          {
            if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (((String)localObject2).endsWith("MDN")))
            {
              localObject1 = paramPhone.getLine1Number();
              paramPhone = str;
              if (!TextUtils.isEmpty((CharSequence)localObject1)) {
                if (((String)localObject1).startsWith("+"))
                {
                  paramPhone = (Phone)localObject1;
                }
                else
                {
                  paramPhone = new StringBuilder();
                  paramPhone.append(((String)localObject2).substring(0, ((String)localObject2).length() - 3));
                  paramPhone.append((String)localObject1);
                  paramPhone = paramPhone.toString();
                }
              }
              localObject2 = paramPhone;
              break;
            }
            break;
          }
        }
      }
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        log("convertNumberIfNecessary: convert service number");
        mNumberConverted = true;
        return localObject2;
      }
      return paramString;
    }
    return paramString;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("CallTracker:");
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPendingOperations=");
    paramFileDescriptor.append(mPendingOperations);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNeedsPoll=");
    paramFileDescriptor.append(mNeedsPoll);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mLastRelevantPoll=");
    paramFileDescriptor.append(mLastRelevantPoll);
    paramPrintWriter.println(paramFileDescriptor.toString());
  }
  
  protected Connection getHoConnection(DriverCall paramDriverCall)
  {
    Iterator localIterator = mHandoverConnections.iterator();
    Connection localConnection;
    StringBuilder localStringBuilder;
    while (localIterator.hasNext())
    {
      localConnection = (Connection)localIterator.next();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("getHoConnection - compare number: hoConn= ");
      localStringBuilder.append(localConnection.toString());
      log(localStringBuilder.toString());
      if ((localConnection.getAddress() != null) && (localConnection.getAddress().contains(number)))
      {
        paramDriverCall = new StringBuilder();
        paramDriverCall.append("getHoConnection: Handover connection match found = ");
        paramDriverCall.append(localConnection.toString());
        log(paramDriverCall.toString());
        return localConnection;
      }
    }
    localIterator = mHandoverConnections.iterator();
    while (localIterator.hasNext())
    {
      localConnection = (Connection)localIterator.next();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("getHoConnection: compare state hoConn= ");
      localStringBuilder.append(localConnection.toString());
      log(localStringBuilder.toString());
      if (localConnection.getStateBeforeHandover() == Call.stateFromDCState(state))
      {
        paramDriverCall = new StringBuilder();
        paramDriverCall.append("getHoConnection: Handover connection match found = ");
        paramDriverCall.append(localConnection.toString());
        log(paramDriverCall.toString());
        return localConnection;
      }
    }
    return null;
  }
  
  public abstract PhoneConstants.State getState();
  
  public abstract void handleMessage(Message paramMessage);
  
  protected abstract void handlePollCalls(AsyncResult paramAsyncResult);
  
  protected void handleRadioAvailable()
  {
    pollCallsWhenSafe();
  }
  
  protected boolean isCommandExceptionRadioNotAvailable(Throwable paramThrowable)
  {
    boolean bool;
    if ((paramThrowable != null) && ((paramThrowable instanceof CommandException)) && (((CommandException)paramThrowable).getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected abstract void log(String paramString);
  
  protected void notifySrvccState(Call.SrvccState paramSrvccState, ArrayList<Connection> paramArrayList)
  {
    if ((paramSrvccState == Call.SrvccState.STARTED) && (paramArrayList != null)) {
      mHandoverConnections.addAll(paramArrayList);
    } else if (paramSrvccState != Call.SrvccState.COMPLETED) {
      mHandoverConnections.clear();
    }
    paramSrvccState = new StringBuilder();
    paramSrvccState.append("notifySrvccState: mHandoverConnections= ");
    paramSrvccState.append(mHandoverConnections.toString());
    log(paramSrvccState.toString());
  }
  
  protected Message obtainNoPollCompleteMessage(int paramInt)
  {
    mPendingOperations += 1;
    mLastRelevantPoll = null;
    return obtainMessage(paramInt);
  }
  
  protected void pollCallsAfterDelay()
  {
    Message localMessage = obtainMessage();
    what = 3;
    sendMessageDelayed(localMessage, 250L);
  }
  
  protected void pollCallsWhenSafe()
  {
    mNeedsPoll = true;
    if (checkNoOperationsPending())
    {
      log("pollCallsWhenSafe(): EVENT_POLL_CALLS_RESULT");
      mLastRelevantPoll = obtainMessage(1);
      mCi.getCurrentCalls(mLastRelevantPoll);
    }
  }
  
  public abstract void registerForVoiceCallEnded(Handler paramHandler, int paramInt, Object paramObject);
  
  public abstract void registerForVoiceCallStarted(Handler paramHandler, int paramInt, Object paramObject);
  
  public abstract void unregisterForVoiceCallEnded(Handler paramHandler);
  
  public abstract void unregisterForVoiceCallStarted(Handler paramHandler);
}
