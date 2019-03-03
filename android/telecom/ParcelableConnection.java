package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.IVideoProvider.Stub;
import java.util.ArrayList;
import java.util.List;

public final class ParcelableConnection
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableConnection> CREATOR = new Parcelable.Creator()
  {
    public ParcelableConnection createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject = ParcelableConnection.class.getClassLoader();
      PhoneAccountHandle localPhoneAccountHandle = (PhoneAccountHandle)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      Uri localUri = (Uri)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      int k = paramAnonymousParcel.readInt();
      String str1 = paramAnonymousParcel.readString();
      int m = paramAnonymousParcel.readInt();
      IVideoProvider localIVideoProvider = IVideoProvider.Stub.asInterface(paramAnonymousParcel.readStrongBinder());
      int n = paramAnonymousParcel.readInt();
      boolean bool1;
      if (paramAnonymousParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramAnonymousParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      long l = paramAnonymousParcel.readLong();
      StatusHints localStatusHints = (StatusHints)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      DisconnectCause localDisconnectCause = (DisconnectCause)paramAnonymousParcel.readParcelable((ClassLoader)localObject);
      ArrayList localArrayList = new ArrayList();
      paramAnonymousParcel.readStringList(localArrayList);
      localObject = Bundle.setDefusable(paramAnonymousParcel.readBundle((ClassLoader)localObject), true);
      int i1 = paramAnonymousParcel.readInt();
      int i2 = paramAnonymousParcel.readInt();
      String str2 = paramAnonymousParcel.readString();
      return new ParcelableConnection(localPhoneAccountHandle, i, j, i1, i2, localUri, k, str1, m, localIVideoProvider, n, bool1, bool2, l, paramAnonymousParcel.readLong(), localStatusHints, localDisconnectCause, localArrayList, (Bundle)localObject, str2);
    }
    
    public ParcelableConnection[] newArray(int paramAnonymousInt)
    {
      return new ParcelableConnection[paramAnonymousInt];
    }
  };
  private final Uri mAddress;
  private final int mAddressPresentation;
  private final String mCallerDisplayName;
  private final int mCallerDisplayNamePresentation;
  private final List<String> mConferenceableConnectionIds;
  private final long mConnectElapsedTimeMillis;
  private final long mConnectTimeMillis;
  private final int mConnectionCapabilities;
  private final int mConnectionProperties;
  private final DisconnectCause mDisconnectCause;
  private final Bundle mExtras;
  private final boolean mIsVoipAudioMode;
  private String mParentCallId;
  private final PhoneAccountHandle mPhoneAccount;
  private final boolean mRingbackRequested;
  private final int mState;
  private final StatusHints mStatusHints;
  private final int mSupportedAudioRoutes;
  private final IVideoProvider mVideoProvider;
  private final int mVideoState;
  
  public ParcelableConnection(PhoneAccountHandle paramPhoneAccountHandle, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Uri paramUri, int paramInt5, String paramString, int paramInt6, IVideoProvider paramIVideoProvider, int paramInt7, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, long paramLong2, StatusHints paramStatusHints, DisconnectCause paramDisconnectCause, List<String> paramList, Bundle paramBundle)
  {
    mPhoneAccount = paramPhoneAccountHandle;
    mState = paramInt1;
    mConnectionCapabilities = paramInt2;
    mConnectionProperties = paramInt3;
    mSupportedAudioRoutes = paramInt4;
    mAddress = paramUri;
    mAddressPresentation = paramInt5;
    mCallerDisplayName = paramString;
    mCallerDisplayNamePresentation = paramInt6;
    mVideoProvider = paramIVideoProvider;
    mVideoState = paramInt7;
    mRingbackRequested = paramBoolean1;
    mIsVoipAudioMode = paramBoolean2;
    mConnectTimeMillis = paramLong1;
    mConnectElapsedTimeMillis = paramLong2;
    mStatusHints = paramStatusHints;
    mDisconnectCause = paramDisconnectCause;
    mConferenceableConnectionIds = paramList;
    mExtras = paramBundle;
    mParentCallId = null;
  }
  
  public ParcelableConnection(PhoneAccountHandle paramPhoneAccountHandle, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Uri paramUri, int paramInt5, String paramString1, int paramInt6, IVideoProvider paramIVideoProvider, int paramInt7, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, long paramLong2, StatusHints paramStatusHints, DisconnectCause paramDisconnectCause, List<String> paramList, Bundle paramBundle, String paramString2)
  {
    this(paramPhoneAccountHandle, paramInt1, paramInt2, paramInt3, paramInt4, paramUri, paramInt5, paramString1, paramInt6, paramIVideoProvider, paramInt7, paramBoolean1, paramBoolean2, paramLong1, paramLong2, paramStatusHints, paramDisconnectCause, paramList, paramBundle);
    mParentCallId = paramString2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCallerDisplayName()
  {
    return mCallerDisplayName;
  }
  
  public int getCallerDisplayNamePresentation()
  {
    return mCallerDisplayNamePresentation;
  }
  
  public final List<String> getConferenceableConnectionIds()
  {
    return mConferenceableConnectionIds;
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
  
  public int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  public final DisconnectCause getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public final Bundle getExtras()
  {
    return mExtras;
  }
  
  public Uri getHandle()
  {
    return mAddress;
  }
  
  public int getHandlePresentation()
  {
    return mAddressPresentation;
  }
  
  public boolean getIsVoipAudioMode()
  {
    return mIsVoipAudioMode;
  }
  
  public final String getParentCallId()
  {
    return mParentCallId;
  }
  
  public PhoneAccountHandle getPhoneAccount()
  {
    return mPhoneAccount;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public final StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public int getSupportedAudioRoutes()
  {
    return mSupportedAudioRoutes;
  }
  
  public IVideoProvider getVideoProvider()
  {
    return mVideoProvider;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public boolean isRingbackRequested()
  {
    return mRingbackRequested;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ParcelableConnection [act:");
    localStringBuilder.append(mPhoneAccount);
    localStringBuilder.append("], state:");
    localStringBuilder.append(mState);
    localStringBuilder.append(", capabilities:");
    localStringBuilder.append(Connection.capabilitiesToString(mConnectionCapabilities));
    localStringBuilder.append(", properties:");
    localStringBuilder.append(Connection.propertiesToString(mConnectionProperties));
    localStringBuilder.append(", extras:");
    localStringBuilder.append(mExtras);
    localStringBuilder.append(", parent:");
    localStringBuilder.append(mParentCallId);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mPhoneAccount, 0);
    paramParcel.writeInt(mState);
    paramParcel.writeInt(mConnectionCapabilities);
    paramParcel.writeParcelable(mAddress, 0);
    paramParcel.writeInt(mAddressPresentation);
    paramParcel.writeString(mCallerDisplayName);
    paramParcel.writeInt(mCallerDisplayNamePresentation);
    IBinder localIBinder;
    if (mVideoProvider != null) {
      localIBinder = mVideoProvider.asBinder();
    } else {
      localIBinder = null;
    }
    paramParcel.writeStrongBinder(localIBinder);
    paramParcel.writeInt(mVideoState);
    paramParcel.writeByte((byte)mRingbackRequested);
    paramParcel.writeByte((byte)mIsVoipAudioMode);
    paramParcel.writeLong(mConnectTimeMillis);
    paramParcel.writeParcelable(mStatusHints, 0);
    paramParcel.writeParcelable(mDisconnectCause, 0);
    paramParcel.writeStringList(mConferenceableConnectionIds);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeInt(mConnectionProperties);
    paramParcel.writeInt(mSupportedAudioRoutes);
    paramParcel.writeString(mParentCallId);
    paramParcel.writeLong(mConnectElapsedTimeMillis);
  }
}
