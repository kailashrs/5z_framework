package android.telecom;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class TelecomAnalytics
  implements Parcelable
{
  public static final Parcelable.Creator<TelecomAnalytics> CREATOR = new Parcelable.Creator()
  {
    public TelecomAnalytics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TelecomAnalytics(paramAnonymousParcel, null);
    }
    
    public TelecomAnalytics[] newArray(int paramAnonymousInt)
    {
      return new TelecomAnalytics[paramAnonymousInt];
    }
  };
  private List<ParcelableCallAnalytics> mCallAnalytics;
  private List<SessionTiming> mSessionTimings;
  
  private TelecomAnalytics(Parcel paramParcel)
  {
    mSessionTimings = new ArrayList();
    paramParcel.readTypedList(mSessionTimings, SessionTiming.CREATOR);
    mCallAnalytics = new ArrayList();
    paramParcel.readTypedList(mCallAnalytics, ParcelableCallAnalytics.CREATOR);
  }
  
  public TelecomAnalytics(List<SessionTiming> paramList, List<ParcelableCallAnalytics> paramList1)
  {
    mSessionTimings = paramList;
    mCallAnalytics = paramList1;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<ParcelableCallAnalytics> getCallAnalytics()
  {
    return mCallAnalytics;
  }
  
  public List<SessionTiming> getSessionTimings()
  {
    return mSessionTimings;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(mSessionTimings);
    paramParcel.writeTypedList(mCallAnalytics);
  }
  
  public static final class SessionTiming
    extends TimedEvent<Integer>
    implements Parcelable
  {
    public static final Parcelable.Creator<SessionTiming> CREATOR = new Parcelable.Creator()
    {
      public TelecomAnalytics.SessionTiming createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TelecomAnalytics.SessionTiming(paramAnonymousParcel, null);
      }
      
      public TelecomAnalytics.SessionTiming[] newArray(int paramAnonymousInt)
      {
        return new TelecomAnalytics.SessionTiming[paramAnonymousInt];
      }
    };
    public static final int CSW_ADD_CONFERENCE_CALL = 108;
    public static final int CSW_HANDLE_CREATE_CONNECTION_COMPLETE = 100;
    public static final int CSW_REMOVE_CALL = 106;
    public static final int CSW_SET_ACTIVE = 101;
    public static final int CSW_SET_DIALING = 103;
    public static final int CSW_SET_DISCONNECTED = 104;
    public static final int CSW_SET_IS_CONFERENCED = 107;
    public static final int CSW_SET_ON_HOLD = 105;
    public static final int CSW_SET_RINGING = 102;
    public static final int ICA_ANSWER_CALL = 1;
    public static final int ICA_CONFERENCE = 8;
    public static final int ICA_DISCONNECT_CALL = 3;
    public static final int ICA_HOLD_CALL = 4;
    public static final int ICA_MUTE = 6;
    public static final int ICA_REJECT_CALL = 2;
    public static final int ICA_SET_AUDIO_ROUTE = 7;
    public static final int ICA_UNHOLD_CALL = 5;
    private int mId;
    private long mTime;
    
    public SessionTiming(int paramInt, long paramLong)
    {
      mId = paramInt;
      mTime = paramLong;
    }
    
    private SessionTiming(Parcel paramParcel)
    {
      mId = paramParcel.readInt();
      mTime = paramParcel.readLong();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Integer getKey()
    {
      return Integer.valueOf(mId);
    }
    
    public long getTime()
    {
      return mTime;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mId);
      paramParcel.writeLong(mTime);
    }
  }
}
