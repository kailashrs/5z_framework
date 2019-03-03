package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ConferenceParticipant
  implements Parcelable
{
  public static final Parcelable.Creator<ConferenceParticipant> CREATOR = new Parcelable.Creator()
  {
    public ConferenceParticipant createFromParcel(Parcel paramAnonymousParcel)
    {
      ClassLoader localClassLoader = ParcelableCall.class.getClassLoader();
      return new ConferenceParticipant((Uri)paramAnonymousParcel.readParcelable(localClassLoader), paramAnonymousParcel.readString(), (Uri)paramAnonymousParcel.readParcelable(localClassLoader), paramAnonymousParcel.readInt());
    }
    
    public ConferenceParticipant[] newArray(int paramAnonymousInt)
    {
      return new ConferenceParticipant[paramAnonymousInt];
    }
  };
  private final String mDisplayName;
  private final Uri mEndpoint;
  private final Uri mHandle;
  private final int mState;
  
  public ConferenceParticipant(Uri paramUri1, String paramString, Uri paramUri2, int paramInt)
  {
    mHandle = paramUri1;
    mDisplayName = paramString;
    mEndpoint = paramUri2;
    mState = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getDisplayName()
  {
    return mDisplayName;
  }
  
  public Uri getEndpoint()
  {
    return mEndpoint;
  }
  
  public Uri getHandle()
  {
    return mHandle;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ConferenceParticipant Handle: ");
    localStringBuilder.append(Log.pii(mHandle));
    localStringBuilder.append(" DisplayName: ");
    localStringBuilder.append(Log.pii(mDisplayName));
    localStringBuilder.append(" Endpoint: ");
    localStringBuilder.append(Log.pii(mEndpoint));
    localStringBuilder.append(" State: ");
    localStringBuilder.append(Connection.stateToString(mState));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mHandle, 0);
    paramParcel.writeString(mDisplayName);
    paramParcel.writeParcelable(mEndpoint, 0);
    paramParcel.writeInt(mState);
  }
}
