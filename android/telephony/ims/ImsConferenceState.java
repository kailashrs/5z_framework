package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

@SystemApi
public final class ImsConferenceState
  implements Parcelable
{
  public static final Parcelable.Creator<ImsConferenceState> CREATOR = new Parcelable.Creator()
  {
    public ImsConferenceState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsConferenceState(paramAnonymousParcel, null);
    }
    
    public ImsConferenceState[] newArray(int paramAnonymousInt)
    {
      return new ImsConferenceState[paramAnonymousInt];
    }
  };
  public static final String DISPLAY_TEXT = "display-text";
  public static final String ENDPOINT = "endpoint";
  public static final String SIP_STATUS_CODE = "sipstatuscode";
  public static final String STATUS = "status";
  public static final String STATUS_ALERTING = "alerting";
  public static final String STATUS_CONNECTED = "connected";
  public static final String STATUS_CONNECT_FAIL = "connect-fail";
  public static final String STATUS_DIALING_IN = "dialing-in";
  public static final String STATUS_DIALING_OUT = "dialing-out";
  public static final String STATUS_DISCONNECTED = "disconnected";
  public static final String STATUS_DISCONNECTING = "disconnecting";
  public static final String STATUS_MUTED_VIA_FOCUS = "muted-via-focus";
  public static final String STATUS_ON_HOLD = "on-hold";
  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_SEND_ONLY = "sendonly";
  public static final String STATUS_SEND_RECV = "sendrecv";
  public static final String USER = "user";
  public final HashMap<String, Bundle> mParticipants = new HashMap();
  
  public ImsConferenceState() {}
  
  private ImsConferenceState(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static int getConnectionStateForStatus(String paramString)
  {
    if (paramString.equals("pending")) {
      return 0;
    }
    if (paramString.equals("dialing-in")) {
      return 2;
    }
    if ((!paramString.equals("alerting")) && (!paramString.equals("dialing-out")))
    {
      if ((!paramString.equals("on-hold")) && (!paramString.equals("sendonly")))
      {
        if ((!paramString.equals("connected")) && (!paramString.equals("muted-via-focus")) && (!paramString.equals("disconnecting")) && (!paramString.equals("sendrecv")))
        {
          if (paramString.equals("disconnected")) {
            return 6;
          }
          return 4;
        }
        return 4;
      }
      return 5;
    }
    return 3;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      String str = paramParcel.readString();
      Bundle localBundle = (Bundle)paramParcel.readParcelable(null);
      mParticipants.put(str, localBundle);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(ImsConferenceState.class.getSimpleName());
    localStringBuilder.append(" ");
    if (mParticipants.size() > 0)
    {
      Object localObject1 = mParticipants.entrySet();
      if (localObject1 != null)
      {
        localObject1 = ((Set)localObject1).iterator();
        localStringBuilder.append("<");
        while (((Iterator)localObject1).hasNext())
        {
          Object localObject2 = (Map.Entry)((Iterator)localObject1).next();
          localStringBuilder.append((String)((Map.Entry)localObject2).getKey());
          localStringBuilder.append(": ");
          Bundle localBundle = (Bundle)((Map.Entry)localObject2).getValue();
          localObject2 = localBundle.keySet().iterator();
          while (((Iterator)localObject2).hasNext())
          {
            String str = (String)((Iterator)localObject2).next();
            localStringBuilder.append(str);
            localStringBuilder.append("=");
            if ((!"endpoint".equals(str)) && (!"user".equals(str))) {
              localStringBuilder.append(localBundle.get(str));
            } else {
              localStringBuilder.append(Log.pii(localBundle.get(str)));
            }
            localStringBuilder.append(", ");
          }
        }
        localStringBuilder.append(">");
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mParticipants.size());
    if (mParticipants.size() > 0)
    {
      Object localObject = mParticipants.entrySet();
      if (localObject != null)
      {
        Iterator localIterator = ((Set)localObject).iterator();
        while (localIterator.hasNext())
        {
          localObject = (Map.Entry)localIterator.next();
          paramParcel.writeString((String)((Map.Entry)localObject).getKey());
          paramParcel.writeParcelable((Parcelable)((Map.Entry)localObject).getValue(), 0);
        }
      }
    }
  }
}
