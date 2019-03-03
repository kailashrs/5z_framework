package android.telecom;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SystemApi
public class ParcelableCallAnalytics
  implements Parcelable
{
  public static final int CALLTYPE_INCOMING = 1;
  public static final int CALLTYPE_OUTGOING = 2;
  public static final int CALLTYPE_UNKNOWN = 0;
  public static final int CDMA_PHONE = 1;
  public static final Parcelable.Creator<ParcelableCallAnalytics> CREATOR = new Parcelable.Creator()
  {
    public ParcelableCallAnalytics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableCallAnalytics(paramAnonymousParcel);
    }
    
    public ParcelableCallAnalytics[] newArray(int paramAnonymousInt)
    {
      return new ParcelableCallAnalytics[paramAnonymousInt];
    }
  };
  public static final int GSM_PHONE = 2;
  public static final int IMS_PHONE = 4;
  public static final long MILLIS_IN_1_SECOND = 1000L;
  public static final long MILLIS_IN_5_MINUTES = 300000L;
  public static final int SIP_PHONE = 8;
  public static final int STILL_CONNECTED = -1;
  public static final int THIRD_PARTY_PHONE = 16;
  private final List<AnalyticsEvent> analyticsEvents;
  private final long callDurationMillis;
  private final int callTechnologies;
  private final int callTerminationCode;
  private final int callType;
  private final String connectionService;
  private final List<EventTiming> eventTimings;
  private final boolean isAdditionalCall;
  private final boolean isCreatedFromExistingConnection;
  private final boolean isEmergencyCall;
  private final boolean isInterrupted;
  private boolean isVideoCall = false;
  private final long startTimeMillis;
  private List<VideoEvent> videoEvents;
  
  public ParcelableCallAnalytics(long paramLong1, long paramLong2, int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3, boolean paramBoolean3, String paramString, boolean paramBoolean4, List<AnalyticsEvent> paramList, List<EventTiming> paramList1)
  {
    startTimeMillis = paramLong1;
    callDurationMillis = paramLong2;
    callType = paramInt1;
    isAdditionalCall = paramBoolean1;
    isInterrupted = paramBoolean2;
    callTechnologies = paramInt2;
    callTerminationCode = paramInt3;
    isEmergencyCall = paramBoolean3;
    connectionService = paramString;
    isCreatedFromExistingConnection = paramBoolean4;
    analyticsEvents = paramList;
    eventTimings = paramList1;
  }
  
  public ParcelableCallAnalytics(Parcel paramParcel)
  {
    startTimeMillis = paramParcel.readLong();
    callDurationMillis = paramParcel.readLong();
    callType = paramParcel.readInt();
    isAdditionalCall = readByteAsBoolean(paramParcel);
    isInterrupted = readByteAsBoolean(paramParcel);
    callTechnologies = paramParcel.readInt();
    callTerminationCode = paramParcel.readInt();
    isEmergencyCall = readByteAsBoolean(paramParcel);
    connectionService = paramParcel.readString();
    isCreatedFromExistingConnection = readByteAsBoolean(paramParcel);
    analyticsEvents = new ArrayList();
    paramParcel.readTypedList(analyticsEvents, AnalyticsEvent.CREATOR);
    eventTimings = new ArrayList();
    paramParcel.readTypedList(eventTimings, EventTiming.CREATOR);
    isVideoCall = readByteAsBoolean(paramParcel);
    videoEvents = new LinkedList();
    paramParcel.readTypedList(videoEvents, VideoEvent.CREATOR);
  }
  
  private static boolean readByteAsBoolean(Parcel paramParcel)
  {
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private static void writeBooleanAsByte(Parcel paramParcel, boolean paramBoolean)
  {
    paramParcel.writeByte((byte)paramBoolean);
  }
  
  public List<AnalyticsEvent> analyticsEvents()
  {
    return analyticsEvents;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getCallDurationMillis()
  {
    return callDurationMillis;
  }
  
  public int getCallTechnologies()
  {
    return callTechnologies;
  }
  
  public int getCallTerminationCode()
  {
    return callTerminationCode;
  }
  
  public int getCallType()
  {
    return callType;
  }
  
  public String getConnectionService()
  {
    return connectionService;
  }
  
  public List<EventTiming> getEventTimings()
  {
    return eventTimings;
  }
  
  public long getStartTimeMillis()
  {
    return startTimeMillis;
  }
  
  public List<VideoEvent> getVideoEvents()
  {
    return videoEvents;
  }
  
  public boolean isAdditionalCall()
  {
    return isAdditionalCall;
  }
  
  public boolean isCreatedFromExistingConnection()
  {
    return isCreatedFromExistingConnection;
  }
  
  public boolean isEmergencyCall()
  {
    return isEmergencyCall;
  }
  
  public boolean isInterrupted()
  {
    return isInterrupted;
  }
  
  public boolean isVideoCall()
  {
    return isVideoCall;
  }
  
  public void setIsVideoCall(boolean paramBoolean)
  {
    isVideoCall = paramBoolean;
  }
  
  public void setVideoEvents(List<VideoEvent> paramList)
  {
    videoEvents = paramList;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(startTimeMillis);
    paramParcel.writeLong(callDurationMillis);
    paramParcel.writeInt(callType);
    writeBooleanAsByte(paramParcel, isAdditionalCall);
    writeBooleanAsByte(paramParcel, isInterrupted);
    paramParcel.writeInt(callTechnologies);
    paramParcel.writeInt(callTerminationCode);
    writeBooleanAsByte(paramParcel, isEmergencyCall);
    paramParcel.writeString(connectionService);
    writeBooleanAsByte(paramParcel, isCreatedFromExistingConnection);
    paramParcel.writeTypedList(analyticsEvents);
    paramParcel.writeTypedList(eventTimings);
    writeBooleanAsByte(paramParcel, isVideoCall);
    paramParcel.writeTypedList(videoEvents);
  }
  
  public static final class AnalyticsEvent
    implements Parcelable
  {
    public static final int AUDIO_ROUTE_BT = 204;
    public static final int AUDIO_ROUTE_EARPIECE = 205;
    public static final int AUDIO_ROUTE_HEADSET = 206;
    public static final int AUDIO_ROUTE_SPEAKER = 207;
    public static final int BIND_CS = 5;
    public static final int BLOCK_CHECK_FINISHED = 105;
    public static final int BLOCK_CHECK_INITIATED = 104;
    public static final int CONFERENCE_WITH = 300;
    public static final Parcelable.Creator<AnalyticsEvent> CREATOR = new Parcelable.Creator()
    {
      public ParcelableCallAnalytics.AnalyticsEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ParcelableCallAnalytics.AnalyticsEvent(paramAnonymousParcel);
      }
      
      public ParcelableCallAnalytics.AnalyticsEvent[] newArray(int paramAnonymousInt)
      {
        return new ParcelableCallAnalytics.AnalyticsEvent[paramAnonymousInt];
      }
    };
    public static final int CS_BOUND = 6;
    public static final int DIRECT_TO_VM_FINISHED = 103;
    public static final int DIRECT_TO_VM_INITIATED = 102;
    public static final int FILTERING_COMPLETED = 107;
    public static final int FILTERING_INITIATED = 106;
    public static final int FILTERING_TIMED_OUT = 108;
    public static final int MUTE = 202;
    public static final int REMOTELY_HELD = 402;
    public static final int REMOTELY_UNHELD = 403;
    public static final int REQUEST_ACCEPT = 7;
    public static final int REQUEST_HOLD = 400;
    public static final int REQUEST_PULL = 500;
    public static final int REQUEST_REJECT = 8;
    public static final int REQUEST_UNHOLD = 401;
    public static final int SCREENING_COMPLETED = 101;
    public static final int SCREENING_SENT = 100;
    public static final int SET_ACTIVE = 1;
    public static final int SET_DIALING = 4;
    public static final int SET_DISCONNECTED = 2;
    public static final int SET_HOLD = 404;
    public static final int SET_PARENT = 302;
    public static final int SET_SELECT_PHONE_ACCOUNT = 0;
    public static final int SILENCE = 201;
    public static final int SKIP_RINGING = 200;
    public static final int SPLIT_CONFERENCE = 301;
    public static final int START_CONNECTION = 3;
    public static final int SWAP = 405;
    public static final int UNMUTE = 203;
    private int mEventName;
    private long mTimeSinceLastEvent;
    
    public AnalyticsEvent(int paramInt, long paramLong)
    {
      mEventName = paramInt;
      mTimeSinceLastEvent = paramLong;
    }
    
    AnalyticsEvent(Parcel paramParcel)
    {
      mEventName = paramParcel.readInt();
      mTimeSinceLastEvent = paramParcel.readLong();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getEventName()
    {
      return mEventName;
    }
    
    public long getTimeSinceLastEvent()
    {
      return mTimeSinceLastEvent;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mEventName);
      paramParcel.writeLong(mTimeSinceLastEvent);
    }
  }
  
  public static final class EventTiming
    implements Parcelable
  {
    public static final int ACCEPT_TIMING = 0;
    public static final int BIND_CS_TIMING = 6;
    public static final int BLOCK_CHECK_FINISHED_TIMING = 9;
    public static final Parcelable.Creator<EventTiming> CREATOR = new Parcelable.Creator()
    {
      public ParcelableCallAnalytics.EventTiming createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ParcelableCallAnalytics.EventTiming(paramAnonymousParcel, null);
      }
      
      public ParcelableCallAnalytics.EventTiming[] newArray(int paramAnonymousInt)
      {
        return new ParcelableCallAnalytics.EventTiming[paramAnonymousInt];
      }
    };
    public static final int DIRECT_TO_VM_FINISHED_TIMING = 8;
    public static final int DISCONNECT_TIMING = 2;
    public static final int FILTERING_COMPLETED_TIMING = 10;
    public static final int FILTERING_TIMED_OUT_TIMING = 11;
    public static final int HOLD_TIMING = 3;
    public static final int INVALID = 999999;
    public static final int OUTGOING_TIME_TO_DIALING_TIMING = 5;
    public static final int REJECT_TIMING = 1;
    public static final int SCREENING_COMPLETED_TIMING = 7;
    public static final int UNHOLD_TIMING = 4;
    private int mName;
    private long mTime;
    
    public EventTiming(int paramInt, long paramLong)
    {
      mName = paramInt;
      mTime = paramLong;
    }
    
    private EventTiming(Parcel paramParcel)
    {
      mName = paramParcel.readInt();
      mTime = paramParcel.readLong();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getName()
    {
      return mName;
    }
    
    public long getTime()
    {
      return mTime;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mName);
      paramParcel.writeLong(mTime);
    }
  }
  
  public static final class VideoEvent
    implements Parcelable
  {
    public static final Parcelable.Creator<VideoEvent> CREATOR = new Parcelable.Creator()
    {
      public ParcelableCallAnalytics.VideoEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ParcelableCallAnalytics.VideoEvent(paramAnonymousParcel);
      }
      
      public ParcelableCallAnalytics.VideoEvent[] newArray(int paramAnonymousInt)
      {
        return new ParcelableCallAnalytics.VideoEvent[paramAnonymousInt];
      }
    };
    public static final int RECEIVE_REMOTE_SESSION_MODIFY_REQUEST = 2;
    public static final int RECEIVE_REMOTE_SESSION_MODIFY_RESPONSE = 3;
    public static final int SEND_LOCAL_SESSION_MODIFY_REQUEST = 0;
    public static final int SEND_LOCAL_SESSION_MODIFY_RESPONSE = 1;
    private int mEventName;
    private long mTimeSinceLastEvent;
    private int mVideoState;
    
    public VideoEvent(int paramInt1, long paramLong, int paramInt2)
    {
      mEventName = paramInt1;
      mTimeSinceLastEvent = paramLong;
      mVideoState = paramInt2;
    }
    
    VideoEvent(Parcel paramParcel)
    {
      mEventName = paramParcel.readInt();
      mTimeSinceLastEvent = paramParcel.readLong();
      mVideoState = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getEventName()
    {
      return mEventName;
    }
    
    public long getTimeSinceLastEvent()
    {
      return mTimeSinceLastEvent;
    }
    
    public int getVideoState()
    {
      return mVideoState;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mEventName);
      paramParcel.writeLong(mTimeSinceLastEvent);
      paramParcel.writeInt(mVideoState);
    }
  }
}
