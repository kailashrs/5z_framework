package android.telecom;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.IVideoProvider.Stub;
import java.util.ArrayList;
import java.util.List;

public final class ParcelableConference
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableConference> CREATOR = new Parcelable.Creator()
  {
    public ParcelableConference createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject = ParcelableConference.class.getClassLoader();
      PhoneAccountHandle localPhoneAccountHandle = (PhoneAccountHandle)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      ArrayList localArrayList = new ArrayList(2);
      paramAnonymousParcel.readList(localArrayList, (ClassLoader)localObject);
      long l = paramAnonymousParcel.readLong();
      IVideoProvider localIVideoProvider = IVideoProvider.Stub.asInterface(paramAnonymousParcel.readStrongBinder());
      int k = paramAnonymousParcel.readInt();
      StatusHints localStatusHints = (StatusHints)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      localObject = paramAnonymousParcel.readBundle((ClassLoader)localObject);
      return new ParcelableConference(localPhoneAccountHandle, i, j, paramAnonymousParcel.readInt(), localArrayList, localIVideoProvider, k, l, paramAnonymousParcel.readLong(), localStatusHints, (Bundle)localObject);
    }
    
    public ParcelableConference[] newArray(int paramAnonymousInt)
    {
      return new ParcelableConference[paramAnonymousInt];
    }
  };
  private long mConnectElapsedTimeMillis = 0L;
  private long mConnectTimeMillis = 0L;
  private int mConnectionCapabilities;
  private List<String> mConnectionIds;
  private int mConnectionProperties;
  private Bundle mExtras;
  private PhoneAccountHandle mPhoneAccount;
  private int mState;
  private StatusHints mStatusHints;
  private final IVideoProvider mVideoProvider;
  private final int mVideoState;
  
  public ParcelableConference(PhoneAccountHandle paramPhoneAccountHandle, int paramInt1, int paramInt2, int paramInt3, List<String> paramList, IVideoProvider paramIVideoProvider, int paramInt4, long paramLong1, long paramLong2, StatusHints paramStatusHints, Bundle paramBundle)
  {
    mPhoneAccount = paramPhoneAccountHandle;
    mState = paramInt1;
    mConnectionCapabilities = paramInt2;
    mConnectionProperties = paramInt3;
    mConnectionIds = paramList;
    mVideoProvider = paramIVideoProvider;
    mVideoState = paramInt4;
    mConnectTimeMillis = paramLong1;
    mStatusHints = paramStatusHints;
    mExtras = paramBundle;
    mConnectElapsedTimeMillis = paramLong2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getConnectElapsedTimeMillis()
  {
    return mConnectElapsedTimeMillis;
  }
  
  public long getConnectTimeMillis()
  {
    return mConnectTimeMillis;
  }
  
  public int getConnectionCapabilities()
  {
    return mConnectionCapabilities;
  }
  
  public List<String> getConnectionIds()
  {
    return mConnectionIds;
  }
  
  public int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public PhoneAccountHandle getPhoneAccount()
  {
    return mPhoneAccount;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public IVideoProvider getVideoProvider()
  {
    return mVideoProvider;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("account: ");
    localStringBuffer.append(mPhoneAccount);
    localStringBuffer.append(", state: ");
    localStringBuffer.append(Connection.stateToString(mState));
    localStringBuffer.append(", capabilities: ");
    localStringBuffer.append(Connection.capabilitiesToString(mConnectionCapabilities));
    localStringBuffer.append(", properties: ");
    localStringBuffer.append(Connection.propertiesToString(mConnectionProperties));
    localStringBuffer.append(", connectTime: ");
    localStringBuffer.append(mConnectTimeMillis);
    localStringBuffer.append(", children: ");
    localStringBuffer.append(mConnectionIds);
    localStringBuffer.append(", VideoState: ");
    localStringBuffer.append(mVideoState);
    localStringBuffer.append(", VideoProvider: ");
    localStringBuffer.append(mVideoProvider);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mPhoneAccount, 0);
    paramParcel.writeInt(mState);
    paramParcel.writeInt(mConnectionCapabilities);
    paramParcel.writeList(mConnectionIds);
    paramParcel.writeLong(mConnectTimeMillis);
    IBinder localIBinder;
    if (mVideoProvider != null) {
      localIBinder = mVideoProvider.asBinder();
    } else {
      localIBinder = null;
    }
    paramParcel.writeStrongBinder(localIBinder);
    paramParcel.writeInt(mVideoState);
    paramParcel.writeParcelable(mStatusHints, 0);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeInt(mConnectionProperties);
    paramParcel.writeLong(mConnectElapsedTimeMillis);
  }
}
