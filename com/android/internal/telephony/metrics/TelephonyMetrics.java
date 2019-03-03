package com.android.internal.telephony.metrics;

import android.hardware.radio.V1_0.SetupDataCallResult;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyHistogram;
import android.telephony.data.DataCallResponse;
import android.telephony.ims.ImsCallSession;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.MmTelFeature.MmTelCapabilities;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.nano.TelephonyProto.ImsCapabilities;
import com.android.internal.telephony.nano.TelephonyProto.ImsConnectionState;
import com.android.internal.telephony.nano.TelephonyProto.ImsReasonInfo;
import com.android.internal.telephony.nano.TelephonyProto.ModemPowerStats;
import com.android.internal.telephony.nano.TelephonyProto.RilDataCall;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession.Event;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession.Event.CBMessage;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession.Event;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession.Event.RilCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.CarrierIdMatching;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.CarrierIdMatchingResult;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.CarrierKeyChange;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.ModemRestart;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilDeactivateDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilSetupDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilSetupDataCallResponse;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyHistogram;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyLog;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyServiceState;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyServiceState.TelephonyOperator;
import com.android.internal.telephony.nano.TelephonyProto.TelephonySettings;
import com.android.internal.telephony.nano.TelephonyProto.Time;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class TelephonyMetrics
{
  private static final boolean DBG = true;
  private static final int MAX_COMPLETED_CALL_SESSIONS = 50;
  private static final int MAX_COMPLETED_SMS_SESSIONS = 500;
  private static final int MAX_TELEPHONY_EVENTS = 1000;
  private static final int SESSION_START_PRECISION_MINUTES = 5;
  private static final String TAG = TelephonyMetrics.class.getSimpleName();
  private static final boolean VDBG = false;
  private static TelephonyMetrics sInstance;
  private final Deque<TelephonyProto.TelephonyCallSession> mCompletedCallSessions = new ArrayDeque();
  private final Deque<TelephonyProto.SmsSession> mCompletedSmsSessions = new ArrayDeque();
  private final SparseArray<InProgressCallSession> mInProgressCallSessions = new SparseArray();
  private final SparseArray<InProgressSmsSession> mInProgressSmsSessions = new SparseArray();
  private final SparseArray<TelephonyProto.ImsCapabilities> mLastImsCapabilities = new SparseArray();
  private final SparseArray<TelephonyProto.ImsConnectionState> mLastImsConnectionState = new SparseArray();
  private final SparseArray<TelephonyProto.TelephonyServiceState> mLastServiceState = new SparseArray();
  private final SparseArray<TelephonyProto.TelephonySettings> mLastSettings = new SparseArray();
  private long mStartElapsedTimeMs;
  private long mStartSystemTimeMs;
  private final Deque<TelephonyProto.TelephonyEvent> mTelephonyEvents = new ArrayDeque();
  private boolean mTelephonyEventsDropped = false;
  
  public TelephonyMetrics()
  {
    reset();
  }
  
  private void addTelephonyEvent(TelephonyProto.TelephonyEvent paramTelephonyEvent)
  {
    try
    {
      if (mTelephonyEvents.size() >= 1000)
      {
        mTelephonyEvents.removeFirst();
        mTelephonyEventsDropped = true;
      }
      mTelephonyEvents.add(paramTelephonyEvent);
      return;
    }
    finally {}
  }
  
  private void annotateInProgressCallSession(long paramLong, int paramInt, CallSessionEventBuilder paramCallSessionEventBuilder)
  {
    try
    {
      InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt);
      if (localInProgressCallSession != null) {
        localInProgressCallSession.addEvent(paramLong, paramCallSessionEventBuilder);
      }
      return;
    }
    finally {}
  }
  
  private void annotateInProgressSmsSession(long paramLong, int paramInt, SmsSessionEventBuilder paramSmsSessionEventBuilder)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = (InProgressSmsSession)mInProgressSmsSessions.get(paramInt);
      if (localInProgressSmsSession != null) {
        localInProgressSmsSession.addEvent(paramLong, paramSmsSessionEventBuilder);
      }
      return;
    }
    finally {}
  }
  
  private TelephonyProto.TelephonyLog buildProto()
  {
    try
    {
      TelephonyProto.TelephonyLog localTelephonyLog = new com/android/internal/telephony/nano/TelephonyProto$TelephonyLog;
      localTelephonyLog.<init>();
      events = new TelephonyProto.TelephonyEvent[mTelephonyEvents.size()];
      mTelephonyEvents.toArray(events);
      eventsDropped = mTelephonyEventsDropped;
      callSessions = new TelephonyProto.TelephonyCallSession[mCompletedCallSessions.size()];
      mCompletedCallSessions.toArray(callSessions);
      smsSessions = new TelephonyProto.SmsSession[mCompletedSmsSessions.size()];
      mCompletedSmsSessions.toArray(smsSessions);
      Object localObject2 = RIL.getTelephonyRILTimingHistograms();
      histograms = new TelephonyProto.TelephonyHistogram[((List)localObject2).size()];
      for (int i = 0; i < ((List)localObject2).size(); i++)
      {
        Object localObject3 = histograms;
        Object localObject4 = new com/android/internal/telephony/nano/TelephonyProto$TelephonyHistogram;
        ((TelephonyProto.TelephonyHistogram)localObject4).<init>();
        localObject3[i] = localObject4;
        localObject4 = (TelephonyHistogram)((List)localObject2).get(i);
        localObject3 = histograms[i];
        category = ((TelephonyHistogram)localObject4).getCategory();
        id = ((TelephonyHistogram)localObject4).getId();
        minTimeMillis = ((TelephonyHistogram)localObject4).getMinTime();
        maxTimeMillis = ((TelephonyHistogram)localObject4).getMaxTime();
        avgTimeMillis = ((TelephonyHistogram)localObject4).getAverageTime();
        count = ((TelephonyHistogram)localObject4).getSampleCount();
        bucketCount = ((TelephonyHistogram)localObject4).getBucketCount();
        bucketEndPoints = ((TelephonyHistogram)localObject4).getBucketEndPoints();
        bucketCounters = ((TelephonyHistogram)localObject4).getBucketCounters();
      }
      localObject2 = new com/android/internal/telephony/metrics/ModemPowerMetrics;
      ((ModemPowerMetrics)localObject2).<init>();
      modemPowerStats = ((ModemPowerMetrics)localObject2).buildProto();
      localObject2 = new com/android/internal/telephony/nano/TelephonyProto$Time;
      ((TelephonyProto.Time)localObject2).<init>();
      startTime = ((TelephonyProto.Time)localObject2);
      startTime.systemTimestampMillis = mStartSystemTimeMs;
      startTime.elapsedTimestampMillis = mStartElapsedTimeMs;
      localObject2 = new com/android/internal/telephony/nano/TelephonyProto$Time;
      ((TelephonyProto.Time)localObject2).<init>();
      endTime = ((TelephonyProto.Time)localObject2);
      endTime.systemTimestampMillis = System.currentTimeMillis();
      endTime.elapsedTimestampMillis = SystemClock.elapsedRealtime();
      return localTelephonyLog;
    }
    finally {}
  }
  
  private static String callSessionEventToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 21: 
      return "NITZ_TIME";
    case 20: 
      return "PHONE_STATE_CHANGED";
    case 19: 
      return "IMS_CALL_HANDOVER_FAILED";
    case 18: 
      return "IMS_CALL_HANDOVER";
    case 17: 
      return "IMS_CALL_TERMINATED";
    case 16: 
      return "IMS_CALL_STATE_CHANGED";
    case 15: 
      return "IMS_CALL_RECEIVE";
    case 14: 
      return "IMS_COMMAND_COMPLETE";
    case 13: 
      return "IMS_COMMAND_FAILED";
    case 12: 
      return "IMS_COMMAND_RECEIVED";
    case 11: 
      return "IMS_COMMAND";
    case 10: 
      return "RIL_CALL_LIST_CHANGED";
    case 9: 
      return "RIL_CALL_SRVCC";
    case 8: 
      return "RIL_CALL_RING";
    case 7: 
      return "RIL_RESPONSE";
    case 6: 
      return "RIL_REQUEST";
    case 5: 
      return "DATA_CALL_LIST_CHANGED";
    case 4: 
      return "IMS_CAPABILITIES_CHANGED";
    case 3: 
      return "IMS_CONNECTION_STATE_CHANGED";
    case 2: 
      return "RIL_SERVICE_STATE_CHANGED";
    case 1: 
      return "SETTINGS_CHANGED";
    }
    return "EVENT_UNKNOWN";
  }
  
  private void convertConnectionToRilCall(GsmCdmaConnection paramGsmCdmaConnection, TelephonyProto.TelephonyCallSession.Event.RilCall paramRilCall)
  {
    if (paramGsmCdmaConnection.isIncoming()) {
      type = 2;
    } else {
      type = 1;
    }
    switch (1.$SwitchMap$com$android$internal$telephony$Call$State[paramGsmCdmaConnection.getState().ordinal()])
    {
    default: 
      state = 0;
      break;
    case 9: 
      state = 9;
      break;
    case 8: 
      state = 8;
      break;
    case 7: 
      state = 7;
      break;
    case 6: 
      state = 6;
      break;
    case 5: 
      state = 5;
      break;
    case 4: 
      state = 4;
      break;
    case 3: 
      state = 3;
      break;
    case 2: 
      state = 2;
      break;
    case 1: 
      state = 1;
    }
    callEndReason = paramGsmCdmaConnection.getDisconnectCause();
    isMultiparty = paramGsmCdmaConnection.isMultiparty();
  }
  
  private TelephonyProto.TelephonyCallSession.Event.RilCall[] convertConnectionsToRilCalls(ArrayList<GsmCdmaConnection> paramArrayList)
  {
    TelephonyProto.TelephonyCallSession.Event.RilCall[] arrayOfRilCall = new TelephonyProto.TelephonyCallSession.Event.RilCall[paramArrayList.size()];
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      arrayOfRilCall[i] = new TelephonyProto.TelephonyCallSession.Event.RilCall();
      index = i;
      convertConnectionToRilCall((GsmCdmaConnection)paramArrayList.get(i), arrayOfRilCall[i]);
    }
    return arrayOfRilCall;
  }
  
  private static String convertProtoToBase64String(TelephonyProto.TelephonyLog paramTelephonyLog)
  {
    return Base64.encodeToString(TelephonyProto.TelephonyLog.toByteArray(paramTelephonyLog), 0);
  }
  
  private boolean disconnectReasonsKnown(TelephonyProto.TelephonyCallSession.Event.RilCall[] paramArrayOfRilCall)
  {
    int i = paramArrayOfRilCall.length;
    for (int j = 0; j < i; j++) {
      if (callEndReason == 0) {
        return false;
      }
    }
    return true;
  }
  
  private void finishCallSession(InProgressCallSession paramInProgressCallSession)
  {
    try
    {
      TelephonyProto.TelephonyCallSession localTelephonyCallSession = new com/android/internal/telephony/nano/TelephonyProto$TelephonyCallSession;
      localTelephonyCallSession.<init>();
      events = new TelephonyProto.TelephonyCallSession.Event[events.size()];
      events.toArray(events);
      startTimeMinutes = startSystemTimeMin;
      phoneId = phoneId;
      eventsDropped = paramInProgressCallSession.isEventsDropped();
      if (mCompletedCallSessions.size() >= 50) {
        mCompletedCallSessions.removeFirst();
      }
      mCompletedCallSessions.add(localTelephonyCallSession);
      mInProgressCallSessions.remove(phoneId);
      return;
    }
    finally {}
  }
  
  private void finishSmsSessionIfNeeded(InProgressSmsSession paramInProgressSmsSession)
  {
    try
    {
      if (paramInProgressSmsSession.getNumExpectedResponses() == 0)
      {
        TelephonyProto.SmsSession localSmsSession = new com/android/internal/telephony/nano/TelephonyProto$SmsSession;
        localSmsSession.<init>();
        events = new TelephonyProto.SmsSession.Event[events.size()];
        events.toArray(events);
        startTimeMinutes = startSystemTimeMin;
        phoneId = phoneId;
        eventsDropped = paramInProgressSmsSession.isEventsDropped();
        if (mCompletedSmsSessions.size() >= 500) {
          mCompletedSmsSessions.removeFirst();
        }
        mCompletedSmsSessions.add(localSmsSession);
        mInProgressSmsSessions.remove(phoneId);
      }
      return;
    }
    finally {}
  }
  
  private int getCallId(ImsCallSession paramImsCallSession)
  {
    if (paramImsCallSession == null) {
      return -1;
    }
    try
    {
      int i = Integer.parseInt(paramImsCallSession.getCallId());
      return i;
    }
    catch (NumberFormatException paramImsCallSession) {}
    return -1;
  }
  
  public static TelephonyMetrics getInstance()
  {
    try
    {
      if (sInstance == null)
      {
        localTelephonyMetrics = new com/android/internal/telephony/metrics/TelephonyMetrics;
        localTelephonyMetrics.<init>();
        sInstance = localTelephonyMetrics;
      }
      TelephonyMetrics localTelephonyMetrics = sInstance;
      return localTelephonyMetrics;
    }
    finally {}
  }
  
  private void printAllMetrics(PrintWriter paramPrintWriter)
  {
    try
    {
      IndentingPrintWriter localIndentingPrintWriter = new com/android/internal/util/IndentingPrintWriter;
      localIndentingPrintWriter.<init>(paramPrintWriter, "  ");
      localIndentingPrintWriter.println("Telephony metrics proto:");
      localIndentingPrintWriter.println("------------------------------------------");
      localIndentingPrintWriter.println("Telephony events:");
      localIndentingPrintWriter.increaseIndent();
      Object localObject1 = mTelephonyEvents.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (TelephonyProto.TelephonyEvent)((Iterator)localObject1).next();
        localIndentingPrintWriter.print(timestampMillis);
        localIndentingPrintWriter.print(" [");
        localIndentingPrintWriter.print(phoneId);
        localIndentingPrintWriter.print("] ");
        localIndentingPrintWriter.print("T=");
        if (type == 2)
        {
          paramPrintWriter = new java/lang/StringBuilder;
          paramPrintWriter.<init>();
          paramPrintWriter.append(telephonyEventToString(type));
          paramPrintWriter.append("(");
          paramPrintWriter.append(serviceState.dataRat);
          paramPrintWriter.append(")");
          localIndentingPrintWriter.print(paramPrintWriter.toString());
        }
        else
        {
          localIndentingPrintWriter.print(telephonyEventToString(type));
        }
        localIndentingPrintWriter.println("");
      }
      localIndentingPrintWriter.decreaseIndent();
      localIndentingPrintWriter.println("Call sessions:");
      localIndentingPrintWriter.increaseIndent();
      paramPrintWriter = mCompletedCallSessions.iterator();
      while (paramPrintWriter.hasNext())
      {
        localObject2 = (TelephonyProto.TelephonyCallSession)paramPrintWriter.next();
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Start time in minutes: ");
        ((StringBuilder)localObject1).append(startTimeMinutes);
        localIndentingPrintWriter.println(((StringBuilder)localObject1).toString());
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Events dropped: ");
        ((StringBuilder)localObject1).append(eventsDropped);
        localIndentingPrintWriter.println(((StringBuilder)localObject1).toString());
        localIndentingPrintWriter.println("Events: ");
        localIndentingPrintWriter.increaseIndent();
        for (localObject1 : events)
        {
          localIndentingPrintWriter.print(delay);
          localIndentingPrintWriter.print(" T=");
          StringBuilder localStringBuilder1;
          if (type == 2)
          {
            localStringBuilder1 = new java/lang/StringBuilder;
            localStringBuilder1.<init>();
            localStringBuilder1.append(callSessionEventToString(type));
            localStringBuilder1.append("(");
            localStringBuilder1.append(serviceState.dataRat);
            localStringBuilder1.append(")");
            localIndentingPrintWriter.println(localStringBuilder1.toString());
          }
          else if (type == 10)
          {
            localIndentingPrintWriter.println(callSessionEventToString(type));
            localIndentingPrintWriter.increaseIndent();
            for (localStringBuilder1 : calls)
            {
              StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
              localStringBuilder2.<init>();
              localStringBuilder2.append(index);
              localStringBuilder2.append(". Type = ");
              localStringBuilder2.append(type);
              localStringBuilder2.append(" State = ");
              localStringBuilder2.append(state);
              localStringBuilder2.append(" End Reason ");
              localStringBuilder2.append(callEndReason);
              localStringBuilder2.append(" isMultiparty = ");
              localStringBuilder2.append(isMultiparty);
              localIndentingPrintWriter.println(localStringBuilder2.toString());
            }
            localIndentingPrintWriter.decreaseIndent();
          }
          else
          {
            localIndentingPrintWriter.println(callSessionEventToString(type));
          }
        }
        localIndentingPrintWriter.decreaseIndent();
      }
      localIndentingPrintWriter.decreaseIndent();
      localIndentingPrintWriter.println("Sms sessions:");
      localIndentingPrintWriter.increaseIndent();
      ??? = 0;
      paramPrintWriter = mCompletedSmsSessions.iterator();
      while (paramPrintWriter.hasNext())
      {
        localObject2 = (TelephonyProto.SmsSession)paramPrintWriter.next();
        ??? = ??? + 1;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("[");
        ((StringBuilder)localObject1).append(???);
        ((StringBuilder)localObject1).append("] Start time in minutes: ");
        ((StringBuilder)localObject1).append(startTimeMinutes);
        localIndentingPrintWriter.print(((StringBuilder)localObject1).toString());
        if (eventsDropped)
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append(", events dropped: ");
          ((StringBuilder)localObject1).append(eventsDropped);
          localIndentingPrintWriter.println(((StringBuilder)localObject1).toString());
        }
        localIndentingPrintWriter.println("Events: ");
        localIndentingPrintWriter.increaseIndent();
        for (localObject1 : events)
        {
          localIndentingPrintWriter.print(delay);
          localIndentingPrintWriter.print(" T=");
          localIndentingPrintWriter.println(smsSessionEventToString(type));
        }
        localIndentingPrintWriter.decreaseIndent();
        ??? = ???;
      }
      localIndentingPrintWriter.decreaseIndent();
      localIndentingPrintWriter.println("Modem power stats:");
      localIndentingPrintWriter.increaseIndent();
      paramPrintWriter = new com/android/internal/telephony/metrics/ModemPowerMetrics;
      paramPrintWriter.<init>();
      paramPrintWriter = paramPrintWriter.buildProto();
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Power log duration (battery time) (ms): ");
      ((StringBuilder)localObject2).append(loggingDurationMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Energy consumed by modem (mAh): ");
      ((StringBuilder)localObject2).append(energyConsumedMah);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Number of packets sent (tx): ");
      ((StringBuilder)localObject2).append(numPacketsTx);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time kernel is active because of cellular data (ms): ");
      ((StringBuilder)localObject2).append(cellularKernelActiveTimeMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time spent in very poor rx signal level (ms): ");
      ((StringBuilder)localObject2).append(timeInVeryPoorRxSignalLevelMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time modem is in sleep (ms): ");
      ((StringBuilder)localObject2).append(sleepTimeMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time modem is in idle (ms): ");
      ((StringBuilder)localObject2).append(idleTimeMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time modem is in rx (ms): ");
      ((StringBuilder)localObject2).append(rxTimeMs);
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Amount of time modem is in tx (ms): ");
      ((StringBuilder)localObject2).append(Arrays.toString(txTimeMs));
      localIndentingPrintWriter.println(((StringBuilder)localObject2).toString());
      localIndentingPrintWriter.decreaseIndent();
      return;
    }
    finally {}
  }
  
  private void reset()
  {
    try
    {
      mTelephonyEvents.clear();
      mCompletedCallSessions.clear();
      mCompletedSmsSessions.clear();
      int i = 0;
      mTelephonyEventsDropped = false;
      mStartSystemTimeMs = System.currentTimeMillis();
      mStartElapsedTimeMs = SystemClock.elapsedRealtime();
      int k;
      TelephonyEventBuilder localTelephonyEventBuilder;
      for (int j = 0; j < mLastServiceState.size(); j++)
      {
        k = mLastServiceState.keyAt(j);
        localTelephonyEventBuilder = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
        localTelephonyEventBuilder.<init>(mStartElapsedTimeMs, k);
        addTelephonyEvent(localTelephonyEventBuilder.setServiceState((TelephonyProto.TelephonyServiceState)mLastServiceState.get(k)).build());
      }
      for (j = 0; j < mLastImsCapabilities.size(); j++)
      {
        k = mLastImsCapabilities.keyAt(j);
        localTelephonyEventBuilder = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
        localTelephonyEventBuilder.<init>(mStartElapsedTimeMs, k);
        addTelephonyEvent(localTelephonyEventBuilder.setImsCapabilities((TelephonyProto.ImsCapabilities)mLastImsCapabilities.get(k)).build());
      }
      for (j = i; j < mLastImsConnectionState.size(); j++)
      {
        i = mLastImsConnectionState.keyAt(j);
        localTelephonyEventBuilder = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
        localTelephonyEventBuilder.<init>(mStartElapsedTimeMs, i);
        addTelephonyEvent(localTelephonyEventBuilder.setImsConnectionState((TelephonyProto.ImsConnectionState)mLastImsConnectionState.get(i)).build());
      }
      return;
    }
    finally {}
  }
  
  static int roundSessionStart(long paramLong)
  {
    return (int)(paramLong / 300000L * 5L);
  }
  
  private static String smsSessionEventToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 8: 
      return "SMS_RECEIVED";
    case 7: 
      return "SMS_SEND_RESULT";
    case 6: 
      return "SMS_SEND";
    case 5: 
      return "DATA_CALL_LIST_CHANGED";
    case 4: 
      return "IMS_CAPABILITIES_CHANGED";
    case 3: 
      return "IMS_CONNECTION_STATE_CHANGED";
    case 2: 
      return "RIL_SERVICE_STATE_CHANGED";
    case 1: 
      return "SETTINGS_CHANGED";
    }
    return "EVENT_UNKNOWN";
  }
  
  private InProgressCallSession startNewCallSessionIfNeeded(int paramInt)
  {
    try
    {
      InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt);
      Object localObject2 = localInProgressCallSession;
      if (localInProgressCallSession == null)
      {
        localInProgressCallSession = new com/android/internal/telephony/metrics/InProgressCallSession;
        localInProgressCallSession.<init>(paramInt);
        mInProgressCallSessions.append(paramInt, localInProgressCallSession);
        Object localObject3 = (TelephonyProto.TelephonyServiceState)mLastServiceState.get(paramInt);
        long l;
        if (localObject3 != null)
        {
          l = startElapsedTimeMs;
          localObject2 = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
          ((CallSessionEventBuilder)localObject2).<init>(2);
          localInProgressCallSession.addEvent(l, ((CallSessionEventBuilder)localObject2).setServiceState((TelephonyProto.TelephonyServiceState)localObject3));
        }
        localObject2 = (TelephonyProto.ImsCapabilities)mLastImsCapabilities.get(paramInt);
        if (localObject2 != null)
        {
          l = startElapsedTimeMs;
          localObject3 = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
          ((CallSessionEventBuilder)localObject3).<init>(4);
          localInProgressCallSession.addEvent(l, ((CallSessionEventBuilder)localObject3).setImsCapabilities((TelephonyProto.ImsCapabilities)localObject2));
        }
        localObject3 = (TelephonyProto.ImsConnectionState)mLastImsConnectionState.get(paramInt);
        localObject2 = localInProgressCallSession;
        if (localObject3 != null)
        {
          l = startElapsedTimeMs;
          localObject2 = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
          ((CallSessionEventBuilder)localObject2).<init>(3);
          localInProgressCallSession.addEvent(l, ((CallSessionEventBuilder)localObject2).setImsConnectionState((TelephonyProto.ImsConnectionState)localObject3));
          localObject2 = localInProgressCallSession;
        }
      }
      return localObject2;
    }
    finally {}
  }
  
  private InProgressSmsSession startNewSmsSessionIfNeeded(int paramInt)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = (InProgressSmsSession)mInProgressSmsSessions.get(paramInt);
      Object localObject2 = localInProgressSmsSession;
      if (localInProgressSmsSession == null)
      {
        localInProgressSmsSession = new com/android/internal/telephony/metrics/InProgressSmsSession;
        localInProgressSmsSession.<init>(paramInt);
        mInProgressSmsSessions.append(paramInt, localInProgressSmsSession);
        localObject2 = (TelephonyProto.TelephonyServiceState)mLastServiceState.get(paramInt);
        long l;
        if (localObject2 != null)
        {
          l = startElapsedTimeMs;
          localObject3 = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
          ((SmsSessionEventBuilder)localObject3).<init>(2);
          localInProgressSmsSession.addEvent(l, ((SmsSessionEventBuilder)localObject3).setServiceState((TelephonyProto.TelephonyServiceState)localObject2));
        }
        Object localObject3 = (TelephonyProto.ImsCapabilities)mLastImsCapabilities.get(paramInt);
        if (localObject3 != null)
        {
          l = startElapsedTimeMs;
          localObject2 = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
          ((SmsSessionEventBuilder)localObject2).<init>(4);
          localInProgressSmsSession.addEvent(l, ((SmsSessionEventBuilder)localObject2).setImsCapabilities((TelephonyProto.ImsCapabilities)localObject3));
        }
        localObject3 = (TelephonyProto.ImsConnectionState)mLastImsConnectionState.get(paramInt);
        localObject2 = localInProgressSmsSession;
        if (localObject3 != null)
        {
          l = startElapsedTimeMs;
          localObject2 = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
          ((SmsSessionEventBuilder)localObject2).<init>(3);
          localInProgressSmsSession.addEvent(l, ((SmsSessionEventBuilder)localObject2).setImsConnectionState((TelephonyProto.ImsConnectionState)localObject3));
          localObject2 = localInProgressSmsSession;
        }
      }
      return localObject2;
    }
    finally {}
  }
  
  private static String telephonyEventToString(int paramInt)
  {
    switch (paramInt)
    {
    case 12: 
    default: 
      return Integer.toString(paramInt);
    case 13: 
      return "CARRIER_ID_MATCHING";
    case 11: 
      return "MODEM_RESTART";
    case 10: 
      return "DATA_STALL_ACTION";
    case 9: 
      return "DATA_CALL_DEACTIVATE_RESPONSE";
    case 8: 
      return "DATA_CALL_DEACTIVATE";
    case 7: 
      return "DATA_CALL_LIST_CHANGED";
    case 6: 
      return "DATA_CALL_SETUP_RESPONSE";
    case 5: 
      return "DATA_CALL_SETUP";
    case 4: 
      return "IMS_CAPABILITIES_CHANGED";
    case 3: 
      return "IMS_CONNECTION_STATE_CHANGED";
    case 2: 
      return "RIL_SERVICE_STATE_CHANGED";
    case 1: 
      return "SETTINGS_CHANGED";
    }
    return "UNKNOWN";
  }
  
  private int toCallSessionRilRequest(int paramInt)
  {
    if (paramInt != 10)
    {
      if (paramInt != 36)
      {
        if (paramInt != 40)
        {
          if (paramInt != 84)
          {
            switch (paramInt)
            {
            default: 
              String str = TAG;
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unknown RIL request: ");
              localStringBuilder.append(paramInt);
              Rlog.e(str, localStringBuilder.toString());
              return 0;
            case 16: 
              return 7;
            case 15: 
              return 5;
            }
            return 3;
          }
          return 6;
        }
        return 2;
      }
      return 4;
    }
    return 1;
  }
  
  private TelephonyProto.ImsReasonInfo toImsReasonInfoProto(ImsReasonInfo paramImsReasonInfo)
  {
    TelephonyProto.ImsReasonInfo localImsReasonInfo = new TelephonyProto.ImsReasonInfo();
    if (paramImsReasonInfo != null)
    {
      reasonCode = paramImsReasonInfo.getCode();
      extraCode = paramImsReasonInfo.getExtraCode();
      paramImsReasonInfo = paramImsReasonInfo.getExtraMessage();
      if (paramImsReasonInfo != null) {
        extraMessage = paramImsReasonInfo;
      }
    }
    return localImsReasonInfo;
  }
  
  private int toPdpType(String paramString)
  {
    int i = paramString.hashCode();
    if (i != -2128542875)
    {
      if (i != 2343)
      {
        if (i != 79440)
        {
          if ((i == 2254343) && (paramString.equals("IPV6")))
          {
            i = 1;
            break label98;
          }
        }
        else if (paramString.equals("PPP"))
        {
          i = 3;
          break label98;
        }
      }
      else if (paramString.equals("IP"))
      {
        i = 0;
        break label98;
      }
    }
    else if (paramString.equals("IPV4V6"))
    {
      i = 2;
      break label98;
    }
    i = -1;
    switch (i)
    {
    default: 
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown type: ");
      localStringBuilder.append(paramString);
      Rlog.e(str, localStringBuilder.toString());
      return 0;
    case 3: 
      return 4;
    case 2: 
      return 3;
    case 1: 
      label98:
      return 2;
    }
    return 1;
  }
  
  static int toPrivacyFuzzedTimeInterval(long paramLong1, long paramLong2)
  {
    paramLong1 = paramLong2 - paramLong1;
    if (paramLong1 < 0L) {
      return 0;
    }
    if (paramLong1 <= 10L) {
      return 1;
    }
    if (paramLong1 <= 20L) {
      return 2;
    }
    if (paramLong1 <= 50L) {
      return 3;
    }
    if (paramLong1 <= 100L) {
      return 4;
    }
    if (paramLong1 <= 200L) {
      return 5;
    }
    if (paramLong1 <= 500L) {
      return 6;
    }
    if (paramLong1 <= 1000L) {
      return 7;
    }
    if (paramLong1 <= 2000L) {
      return 8;
    }
    if (paramLong1 <= 5000L) {
      return 9;
    }
    if (paramLong1 <= 10000L) {
      return 10;
    }
    if (paramLong1 <= 30000L) {
      return 11;
    }
    if (paramLong1 <= 60000L) {
      return 12;
    }
    if (paramLong1 <= 180000L) {
      return 13;
    }
    if (paramLong1 <= 600000L) {
      return 14;
    }
    if (paramLong1 <= 1800000L) {
      return 15;
    }
    if (paramLong1 <= 3600000L) {
      return 16;
    }
    if (paramLong1 <= 7200000L) {
      return 17;
    }
    if (paramLong1 <= 14400000L) {
      return 18;
    }
    return 19;
  }
  
  private TelephonyProto.TelephonyServiceState toServiceStateProto(ServiceState paramServiceState)
  {
    TelephonyProto.TelephonyServiceState localTelephonyServiceState = new TelephonyProto.TelephonyServiceState();
    voiceRoamingType = paramServiceState.getVoiceRoamingType();
    dataRoamingType = paramServiceState.getDataRoamingType();
    voiceOperator = new TelephonyProto.TelephonyServiceState.TelephonyOperator();
    if (paramServiceState.getVoiceOperatorAlphaLong() != null) {
      voiceOperator.alphaLong = paramServiceState.getVoiceOperatorAlphaLong();
    }
    if (paramServiceState.getVoiceOperatorAlphaShort() != null) {
      voiceOperator.alphaShort = paramServiceState.getVoiceOperatorAlphaShort();
    }
    if (paramServiceState.getVoiceOperatorNumeric() != null) {
      voiceOperator.numeric = paramServiceState.getVoiceOperatorNumeric();
    }
    dataOperator = new TelephonyProto.TelephonyServiceState.TelephonyOperator();
    if (paramServiceState.getDataOperatorAlphaLong() != null) {
      dataOperator.alphaLong = paramServiceState.getDataOperatorAlphaLong();
    }
    if (paramServiceState.getDataOperatorAlphaShort() != null) {
      dataOperator.alphaShort = paramServiceState.getDataOperatorAlphaShort();
    }
    if (paramServiceState.getDataOperatorNumeric() != null) {
      dataOperator.numeric = paramServiceState.getDataOperatorNumeric();
    }
    voiceRat = paramServiceState.getRilVoiceRadioTechnology();
    dataRat = paramServiceState.getRilDataRadioTechnology();
    return localTelephonyServiceState;
  }
  
  private void writeOnCallSolicitedResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "writeOnCallSolicitedResponse: Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(7).setRilRequest(toCallSessionRilRequest(paramInt4)).setRilRequestId(paramInt2).setRilError(paramInt3 + 1));
    }
  }
  
  private void writeOnDeactivateDataCallResponse(int paramInt1, int paramInt2)
  {
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setDeactivateDataCallResponse(paramInt2 + 1).build());
  }
  
  private void writeOnSetupDataCallResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4, SetupDataCallResult paramSetupDataCallResult)
  {
    TelephonyProto.TelephonyEvent.RilSetupDataCallResponse localRilSetupDataCallResponse = new TelephonyProto.TelephonyEvent.RilSetupDataCallResponse();
    TelephonyProto.RilDataCall localRilDataCall = new TelephonyProto.RilDataCall();
    if (paramSetupDataCallResult != null)
    {
      if (status == 0) {
        paramInt2 = 1;
      } else {
        paramInt2 = status;
      }
      status = paramInt2;
      suggestedRetryTimeMillis = suggestedRetryTime;
      cid = cid;
      if (!TextUtils.isEmpty(type)) {
        type = toPdpType(type);
      }
      if (!TextUtils.isEmpty(ifname)) {
        iframe = ifname;
      }
    }
    call = localRilDataCall;
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setSetupDataCallResponse(localRilSetupDataCallResponse).build());
  }
  
  private void writeOnSmsSolicitedResponse(int paramInt1, int paramInt2, int paramInt3, SmsResponse paramSmsResponse)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = (InProgressSmsSession)mInProgressSmsSessions.get(paramInt1);
      if (localInProgressSmsSession == null)
      {
        Rlog.e(TAG, "SMS session is missing");
      }
      else
      {
        paramInt1 = 0;
        if (paramSmsResponse != null) {
          paramInt1 = mErrorCode;
        }
        paramSmsResponse = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
        paramSmsResponse.<init>(7);
        localInProgressSmsSession.addEvent(paramSmsResponse.setErrorCode(paramInt1).setRilErrno(paramInt3 + 1).setRilRequestId(paramInt2));
        localInProgressSmsSession.decreaseExpectedResponse();
        finishSmsSessionIfNeeded(localInProgressSmsSession);
      }
      return;
    }
    finally {}
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      int i = 0;
      paramFileDescriptor = paramArrayOfString[0];
      int j = paramFileDescriptor.hashCode();
      if (j != -1953159389)
      {
        if ((j == 950313125) && (paramFileDescriptor.equals("--metricsproto")))
        {
          i = 1;
          break label73;
        }
      }
      else {
        if (paramFileDescriptor.equals("--metrics")) {
          break label73;
        }
      }
      i = -1;
      switch (i)
      {
      default: 
        break;
      case 1: 
        paramPrintWriter.println(convertProtoToBase64String(buildProto()));
        reset();
        break;
      case 0: 
        label73:
        printAllMetrics(paramPrintWriter);
      }
    }
  }
  
  public void writeCarrierIdMatchingEvent(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    TelephonyProto.TelephonyEvent.CarrierIdMatching localCarrierIdMatching = new TelephonyProto.TelephonyEvent.CarrierIdMatching();
    TelephonyProto.TelephonyEvent.CarrierIdMatchingResult localCarrierIdMatchingResult = new TelephonyProto.TelephonyEvent.CarrierIdMatchingResult();
    if (paramInt3 != -1)
    {
      carrierId = paramInt3;
      if (paramString2 != null)
      {
        mccmnc = paramString1;
        gid1 = paramString2;
      }
    }
    else if (paramString1 != null)
    {
      mccmnc = paramString1;
    }
    cidTableVersion = paramInt2;
    result = localCarrierIdMatchingResult;
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setCarrierIdMatching(localCarrierIdMatching).build());
  }
  
  public void writeCarrierKeyEvent(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    TelephonyProto.TelephonyEvent.CarrierKeyChange localCarrierKeyChange = new TelephonyProto.TelephonyEvent.CarrierKeyChange();
    keyType = paramInt2;
    isDownloadSuccessful = paramBoolean;
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setCarrierKeyChange(localCarrierKeyChange).build());
  }
  
  public void writeDataStallEvent(int paramInt1, int paramInt2)
  {
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setDataStallRecoveryAction(paramInt2).build());
  }
  
  public void writeImsCallState(int paramInt, ImsCallSession paramImsCallSession, Call.State paramState)
  {
    int i;
    switch (1.$SwitchMap$com$android$internal$telephony$Call$State[paramState.ordinal()])
    {
    default: 
      i = 0;
      break;
    case 9: 
      i = 9;
      break;
    case 8: 
      i = 8;
      break;
    case 7: 
      i = 7;
      break;
    case 6: 
      i = 6;
      break;
    case 5: 
      i = 5;
      break;
    case 4: 
      i = 4;
      break;
    case 3: 
      i = 3;
      break;
    case 2: 
      i = 2;
      break;
    case 1: 
      i = 1;
    }
    paramState = (InProgressCallSession)mInProgressCallSessions.get(paramInt);
    if (paramState == null) {
      Rlog.e(TAG, "Call session is missing");
    } else {
      paramState.addEvent(new CallSessionEventBuilder(16).setCallIndex(getCallId(paramImsCallSession)).setCallState(i));
    }
  }
  
  public void writeImsSetFeatureValue(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    TelephonyProto.TelephonySettings localTelephonySettings = new TelephonyProto.TelephonySettings();
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (paramInt3 == 0) {
      switch (paramInt2)
      {
      default: 
        break;
      case 2: 
        if (paramInt4 != 0) {
          bool4 = true;
        }
        isVtOverLteEnabled = bool4;
        break;
      case 1: 
        bool4 = bool1;
        if (paramInt4 != 0) {
          bool4 = true;
        }
        isEnhanced4GLteModeEnabled = bool4;
      }
    } else if (paramInt3 == 1) {
      switch (paramInt2)
      {
      default: 
        break;
      case 2: 
        bool4 = bool2;
        if (paramInt4 != 0) {
          bool4 = true;
        }
        isVtOverWifiEnabled = bool4;
        break;
      case 1: 
        bool4 = bool3;
        if (paramInt4 != 0) {
          bool4 = true;
        }
        isWifiCallingEnabled = bool4;
      }
    }
    if ((mLastSettings.get(paramInt1) != null) && (Arrays.equals(TelephonyProto.TelephonySettings.toByteArray((MessageNano)mLastSettings.get(paramInt1)), TelephonyProto.TelephonySettings.toByteArray(localTelephonySettings)))) {
      return;
    }
    mLastSettings.put(paramInt1, localTelephonySettings);
    TelephonyProto.TelephonyEvent localTelephonyEvent = new TelephonyEventBuilder(paramInt1).setSettings(localTelephonySettings).build();
    addTelephonyEvent(localTelephonyEvent);
    annotateInProgressCallSession(timestampMillis, paramInt1, new CallSessionEventBuilder(1).setSettings(localTelephonySettings));
    annotateInProgressSmsSession(timestampMillis, paramInt1, new SmsSessionEventBuilder(1).setSettings(localTelephonySettings));
  }
  
  public void writeModemRestartEvent(int paramInt, String paramString)
  {
    TelephonyProto.TelephonyEvent.ModemRestart localModemRestart = new TelephonyProto.TelephonyEvent.ModemRestart();
    String str = Build.getRadioVersion();
    if (str != null) {
      basebandVersion = str;
    }
    if (paramString != null) {
      reason = paramString;
    }
    addTelephonyEvent(new TelephonyEventBuilder(paramInt).setModemRestart(localModemRestart).build());
  }
  
  public void writeNITZEvent(int paramInt, long paramLong)
  {
    TelephonyProto.TelephonyEvent localTelephonyEvent = new TelephonyEventBuilder(paramInt).setNITZ(paramLong).build();
    addTelephonyEvent(localTelephonyEvent);
    annotateInProgressCallSession(timestampMillis, paramInt, new CallSessionEventBuilder(21).setNITZ(paramLong));
  }
  
  public void writeNewCBSms(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = startNewSmsSessionIfNeeded(paramInt1);
      if (paramBoolean1) {
        paramInt1 = 2;
      }
      for (;;)
      {
        break;
        if (paramBoolean2) {
          paramInt1 = 1;
        } else {
          paramInt1 = 3;
        }
      }
      TelephonyProto.SmsSession.Event.CBMessage localCBMessage = new com/android/internal/telephony/nano/TelephonyProto$SmsSession$Event$CBMessage;
      localCBMessage.<init>();
      msgFormat = paramInt2;
      msgPriority = (paramInt3 + 1);
      msgType = paramInt1;
      serviceCategory = paramInt4;
      SmsSessionEventBuilder localSmsSessionEventBuilder = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      localSmsSessionEventBuilder.<init>(9);
      localInProgressSmsSession.addEvent(localSmsSessionEventBuilder.setCellBroadcastMessage(localCBMessage));
      finishSmsSessionIfNeeded(localInProgressSmsSession);
      return;
    }
    finally {}
  }
  
  public void writeOnImsCallHandoverEvent(int paramInt1, int paramInt2, ImsCallSession paramImsCallSession, int paramInt3, int paramInt4, ImsReasonInfo paramImsReasonInfo)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(paramInt2).setCallIndex(getCallId(paramImsCallSession)).setSrcAccessTech(paramInt3).setTargetAccessTech(paramInt4).setImsReasonInfo(toImsReasonInfoProto(paramImsReasonInfo)));
    }
  }
  
  public void writeOnImsCallHeld(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallHoldFailed(int paramInt, ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
  
  public void writeOnImsCallHoldReceived(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallProgressing(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallReceive(int paramInt, ImsCallSession paramImsCallSession)
  {
    startNewCallSessionIfNeeded(paramInt).addEvent(new CallSessionEventBuilder(15).setCallIndex(getCallId(paramImsCallSession)));
  }
  
  public void writeOnImsCallResumeFailed(int paramInt, ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
  
  public void writeOnImsCallResumeReceived(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallResumed(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallStart(int paramInt, ImsCallSession paramImsCallSession)
  {
    startNewCallSessionIfNeeded(paramInt).addEvent(new CallSessionEventBuilder(11).setCallIndex(getCallId(paramImsCallSession)).setImsCommand(1));
  }
  
  public void writeOnImsCallStartFailed(int paramInt, ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
  
  public void writeOnImsCallStarted(int paramInt, ImsCallSession paramImsCallSession) {}
  
  public void writeOnImsCallTerminated(int paramInt, ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(17).setCallIndex(getCallId(paramImsCallSession)).setImsReasonInfo(toImsReasonInfoProto(paramImsReasonInfo)));
    }
  }
  
  public void writeOnImsCapabilities(int paramInt1, int paramInt2, MmTelFeature.MmTelCapabilities paramMmTelCapabilities)
  {
    try
    {
      Object localObject = new com/android/internal/telephony/nano/TelephonyProto$ImsCapabilities;
      ((TelephonyProto.ImsCapabilities)localObject).<init>();
      if (paramInt2 == 0)
      {
        voiceOverLte = paramMmTelCapabilities.isCapable(1);
        videoOverLte = paramMmTelCapabilities.isCapable(2);
        utOverLte = paramMmTelCapabilities.isCapable(4);
      }
      else if (paramInt2 == 1)
      {
        voiceOverWifi = paramMmTelCapabilities.isCapable(1);
        videoOverWifi = paramMmTelCapabilities.isCapable(2);
        utOverWifi = paramMmTelCapabilities.isCapable(4);
      }
      paramMmTelCapabilities = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
      paramMmTelCapabilities.<init>(paramInt1);
      paramMmTelCapabilities = paramMmTelCapabilities.setImsCapabilities((TelephonyProto.ImsCapabilities)localObject).build();
      if (mLastImsCapabilities.get(paramInt1) != null)
      {
        boolean bool = Arrays.equals(TelephonyProto.ImsCapabilities.toByteArray((MessageNano)mLastImsCapabilities.get(paramInt1)), TelephonyProto.ImsCapabilities.toByteArray((MessageNano)localObject));
        if (bool) {
          return;
        }
      }
      mLastImsCapabilities.put(paramInt1, localObject);
      addTelephonyEvent(paramMmTelCapabilities);
      long l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
      ((CallSessionEventBuilder)localObject).<init>(4);
      annotateInProgressCallSession(l, paramInt1, ((CallSessionEventBuilder)localObject).setImsCapabilities(imsCapabilities));
      l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      ((SmsSessionEventBuilder)localObject).<init>(4);
      annotateInProgressSmsSession(l, paramInt1, ((SmsSessionEventBuilder)localObject).setImsCapabilities(imsCapabilities));
      return;
    }
    finally {}
  }
  
  public void writeOnImsCommand(int paramInt1, ImsCallSession paramImsCallSession, int paramInt2)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(11).setCallIndex(getCallId(paramImsCallSession)).setImsCommand(paramInt2));
    }
  }
  
  public void writeOnImsConnectionState(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      Object localObject = new com/android/internal/telephony/nano/TelephonyProto$ImsConnectionState;
      ((TelephonyProto.ImsConnectionState)localObject).<init>();
      state = paramInt2;
      if (paramImsReasonInfo != null)
      {
        TelephonyProto.ImsReasonInfo localImsReasonInfo = new com/android/internal/telephony/nano/TelephonyProto$ImsReasonInfo;
        localImsReasonInfo.<init>();
        reasonCode = paramImsReasonInfo.getCode();
        extraCode = paramImsReasonInfo.getExtraCode();
        paramImsReasonInfo = paramImsReasonInfo.getExtraMessage();
        if (paramImsReasonInfo != null) {
          extraMessage = paramImsReasonInfo;
        }
        reasonInfo = localImsReasonInfo;
      }
      if (mLastImsConnectionState.get(paramInt1) != null)
      {
        boolean bool = Arrays.equals(TelephonyProto.ImsConnectionState.toByteArray((MessageNano)mLastImsConnectionState.get(paramInt1)), TelephonyProto.ImsConnectionState.toByteArray((MessageNano)localObject));
        if (bool) {
          return;
        }
      }
      mLastImsConnectionState.put(paramInt1, localObject);
      paramImsReasonInfo = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
      paramImsReasonInfo.<init>(paramInt1);
      paramImsReasonInfo = paramImsReasonInfo.setImsConnectionState((TelephonyProto.ImsConnectionState)localObject).build();
      addTelephonyEvent(paramImsReasonInfo);
      long l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
      ((CallSessionEventBuilder)localObject).<init>(3);
      annotateInProgressCallSession(l, paramInt1, ((CallSessionEventBuilder)localObject).setImsConnectionState(imsConnectionState));
      l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      ((SmsSessionEventBuilder)localObject).<init>(3);
      annotateInProgressSmsSession(l, paramInt1, ((SmsSessionEventBuilder)localObject).setImsConnectionState(imsConnectionState));
      return;
    }
    finally {}
  }
  
  public void writeOnRilSolicitedResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
  {
    switch (paramInt4)
    {
    default: 
      break;
    case 41: 
      writeOnDeactivateDataCallResponse(paramInt1, paramInt3);
      break;
    case 27: 
      writeOnSetupDataCallResponse(paramInt1, paramInt2, paramInt3, paramInt4, (SetupDataCallResult)paramObject);
      break;
    case 25: 
    case 26: 
    case 87: 
    case 113: 
      writeOnSmsSolicitedResponse(paramInt1, paramInt2, paramInt3, (SmsResponse)paramObject);
      break;
    case 10: 
    case 12: 
    case 13: 
    case 14: 
    case 40: 
      writeOnCallSolicitedResponse(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void writeOnRilTimeoutResponse(int paramInt1, int paramInt2, int paramInt3) {}
  
  public void writePhoneState(int paramInt, PhoneConstants.State paramState)
  {
    int i;
    switch (1.$SwitchMap$com$android$internal$telephony$PhoneConstants$State[paramState.ordinal()])
    {
    default: 
      i = 0;
      break;
    case 3: 
      i = 3;
      break;
    case 2: 
      i = 2;
      break;
    case 1: 
      i = 1;
    }
    paramState = (InProgressCallSession)mInProgressCallSessions.get(paramInt);
    if (paramState == null)
    {
      Rlog.e(TAG, "writePhoneState: Call session is missing");
    }
    else
    {
      paramState.setLastKnownPhoneState(i);
      if ((i == 1) && (!paramState.containsCsCalls())) {
        finishCallSession(paramState);
      }
      paramState.addEvent(new CallSessionEventBuilder(20).setPhoneState(i));
    }
  }
  
  public void writeRilAnswer(int paramInt1, int paramInt2)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "writeRilAnswer: Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(6).setRilRequest(2).setRilRequestId(paramInt2));
    }
  }
  
  public void writeRilCallList(int paramInt, ArrayList<GsmCdmaConnection> paramArrayList)
  {
    InProgressCallSession localInProgressCallSession = startNewCallSessionIfNeeded(paramInt);
    if (localInProgressCallSession == null)
    {
      Rlog.e(TAG, "writeRilCallList: Call session is missing");
    }
    else
    {
      paramArrayList = convertConnectionsToRilCalls(paramArrayList);
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(10).setRilCalls(paramArrayList));
      if ((localInProgressCallSession.isPhoneIdle()) && (disconnectReasonsKnown(paramArrayList))) {
        finishCallSession(localInProgressCallSession);
      }
    }
  }
  
  public void writeRilCallRing(int paramInt, char[] paramArrayOfChar)
  {
    paramArrayOfChar = startNewCallSessionIfNeeded(paramInt);
    paramArrayOfChar.addEvent(startElapsedTimeMs, new CallSessionEventBuilder(8));
  }
  
  public void writeRilDataCallList(int paramInt, ArrayList<DataCallResponse> paramArrayList)
  {
    TelephonyProto.RilDataCall[] arrayOfRilDataCall = new TelephonyProto.RilDataCall[paramArrayList.size()];
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      arrayOfRilDataCall[i] = new TelephonyProto.RilDataCall();
      cid = ((DataCallResponse)paramArrayList.get(i)).getCallId();
      if (!TextUtils.isEmpty(((DataCallResponse)paramArrayList.get(i)).getIfname())) {
        iframe = ((DataCallResponse)paramArrayList.get(i)).getIfname();
      }
      if (!TextUtils.isEmpty(((DataCallResponse)paramArrayList.get(i)).getType())) {
        type = toPdpType(((DataCallResponse)paramArrayList.get(i)).getType());
      }
    }
    addTelephonyEvent(new TelephonyEventBuilder(paramInt).setDataCalls(arrayOfRilDataCall).build());
  }
  
  public void writeRilDeactivateDataCall(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    TelephonyProto.TelephonyEvent.RilDeactivateDataCall localRilDeactivateDataCall = new TelephonyProto.TelephonyEvent.RilDeactivateDataCall();
    cid = paramInt3;
    switch (paramInt4)
    {
    default: 
      reason = 0;
      break;
    case 3: 
      reason = 4;
      break;
    case 2: 
      reason = 2;
      break;
    case 1: 
      reason = 1;
    }
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setDeactivateDataCall(localRilDeactivateDataCall).build());
  }
  
  public void writeRilDial(int paramInt1, GsmCdmaConnection paramGsmCdmaConnection, int paramInt2, UUSInfo paramUUSInfo)
  {
    paramUUSInfo = startNewCallSessionIfNeeded(paramInt1);
    if (paramUUSInfo == null)
    {
      Rlog.e(TAG, "writeRilDial: Call session is missing");
    }
    else
    {
      TelephonyProto.TelephonyCallSession.Event.RilCall[] arrayOfRilCall = new TelephonyProto.TelephonyCallSession.Event.RilCall[1];
      arrayOfRilCall[0] = new TelephonyProto.TelephonyCallSession.Event.RilCall();
      0index = -1;
      convertConnectionToRilCall(paramGsmCdmaConnection, arrayOfRilCall[0]);
      paramUUSInfo.addEvent(startElapsedTimeMs, new CallSessionEventBuilder(6).setRilRequest(1).setRilCalls(arrayOfRilCall));
    }
  }
  
  public void writeRilHangup(int paramInt1, GsmCdmaConnection paramGsmCdmaConnection, int paramInt2)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null)
    {
      Rlog.e(TAG, "writeRilHangup: Call session is missing");
    }
    else
    {
      TelephonyProto.TelephonyCallSession.Event.RilCall[] arrayOfRilCall = new TelephonyProto.TelephonyCallSession.Event.RilCall[1];
      arrayOfRilCall[0] = new TelephonyProto.TelephonyCallSession.Event.RilCall();
      0index = paramInt2;
      convertConnectionToRilCall(paramGsmCdmaConnection, arrayOfRilCall[0]);
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(6).setRilRequest(3).setRilCalls(arrayOfRilCall));
    }
  }
  
  public void writeRilNewSms(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = startNewSmsSessionIfNeeded(paramInt1);
      SmsSessionEventBuilder localSmsSessionEventBuilder = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      localSmsSessionEventBuilder.<init>(8);
      localInProgressSmsSession.addEvent(localSmsSessionEventBuilder.setTech(paramInt2).setFormat(paramInt3));
      finishSmsSessionIfNeeded(localInProgressSmsSession);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void writeRilSendSms(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    try
    {
      InProgressSmsSession localInProgressSmsSession = startNewSmsSessionIfNeeded(paramInt1);
      SmsSessionEventBuilder localSmsSessionEventBuilder = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      localSmsSessionEventBuilder.<init>(6);
      localInProgressSmsSession.addEvent(localSmsSessionEventBuilder.setTech(paramInt3).setRilRequestId(paramInt2).setFormat(paramInt4));
      localInProgressSmsSession.increaseExpectedResponse();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void writeRilSrvcc(int paramInt1, int paramInt2)
  {
    InProgressCallSession localInProgressCallSession = (InProgressCallSession)mInProgressCallSessions.get(paramInt1);
    if (localInProgressCallSession == null) {
      Rlog.e(TAG, "writeRilSrvcc: Call session is missing");
    } else {
      localInProgressCallSession.addEvent(new CallSessionEventBuilder(9).setSrvccState(paramInt2 + 1));
    }
  }
  
  public void writeServiceStateChanged(int paramInt, ServiceState paramServiceState)
  {
    try
    {
      Object localObject = new com/android/internal/telephony/metrics/TelephonyEventBuilder;
      ((TelephonyEventBuilder)localObject).<init>(paramInt);
      paramServiceState = ((TelephonyEventBuilder)localObject).setServiceState(toServiceStateProto(paramServiceState)).build();
      if (mLastServiceState.get(paramInt) != null)
      {
        boolean bool = Arrays.equals(TelephonyProto.TelephonyServiceState.toByteArray((MessageNano)mLastServiceState.get(paramInt)), TelephonyProto.TelephonyServiceState.toByteArray(serviceState));
        if (bool) {
          return;
        }
      }
      mLastServiceState.put(paramInt, serviceState);
      addTelephonyEvent(paramServiceState);
      long l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/CallSessionEventBuilder;
      ((CallSessionEventBuilder)localObject).<init>(2);
      annotateInProgressCallSession(l, paramInt, ((CallSessionEventBuilder)localObject).setServiceState(serviceState));
      l = timestampMillis;
      localObject = new com/android/internal/telephony/metrics/SmsSessionEventBuilder;
      ((SmsSessionEventBuilder)localObject).<init>(2);
      annotateInProgressSmsSession(l, paramInt, ((SmsSessionEventBuilder)localObject).setServiceState(serviceState));
      return;
    }
    finally {}
  }
  
  public void writeSetPreferredNetworkType(int paramInt1, int paramInt2)
  {
    TelephonyProto.TelephonySettings localTelephonySettings = new TelephonyProto.TelephonySettings();
    preferredNetworkMode = (paramInt2 + 1);
    if ((mLastSettings.get(paramInt1) != null) && (Arrays.equals(TelephonyProto.TelephonySettings.toByteArray((MessageNano)mLastSettings.get(paramInt1)), TelephonyProto.TelephonySettings.toByteArray(localTelephonySettings)))) {
      return;
    }
    mLastSettings.put(paramInt1, localTelephonySettings);
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setSettings(localTelephonySettings).build());
  }
  
  public void writeSetupDataCall(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    TelephonyProto.TelephonyEvent.RilSetupDataCall localRilSetupDataCall = new TelephonyProto.TelephonyEvent.RilSetupDataCall();
    rat = paramInt2;
    dataProfile = (paramInt3 + 1);
    if (paramString1 != null) {
      apn = paramString1;
    }
    if (paramString2 != null) {
      type = toPdpType(paramString2);
    }
    addTelephonyEvent(new TelephonyEventBuilder(paramInt1).setSetupDataCall(localRilSetupDataCall).build());
  }
}
