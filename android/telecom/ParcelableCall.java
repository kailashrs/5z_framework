package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.IVideoProvider.Stub;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ParcelableCall
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableCall> CREATOR = new Parcelable.Creator()
  {
    public ParcelableCall createFromParcel(Parcel paramAnonymousParcel)
    {
      ClassLoader localClassLoader = ParcelableCall.class.getClassLoader();
      String str1 = paramAnonymousParcel.readString();
      int i = paramAnonymousParcel.readInt();
      DisconnectCause localDisconnectCause = (DisconnectCause)paramAnonymousParcel.readParcelable(localClassLoader);
      ArrayList localArrayList1 = new ArrayList();
      paramAnonymousParcel.readList(localArrayList1, localClassLoader);
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      long l = paramAnonymousParcel.readLong();
      Uri localUri = (Uri)paramAnonymousParcel.readParcelable(localClassLoader);
      int m = paramAnonymousParcel.readInt();
      String str2 = paramAnonymousParcel.readString();
      int n = paramAnonymousParcel.readInt();
      GatewayInfo localGatewayInfo = (GatewayInfo)paramAnonymousParcel.readParcelable(localClassLoader);
      PhoneAccountHandle localPhoneAccountHandle = (PhoneAccountHandle)paramAnonymousParcel.readParcelable(localClassLoader);
      boolean bool1;
      if (paramAnonymousParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      IVideoProvider localIVideoProvider = IVideoProvider.Stub.asInterface(paramAnonymousParcel.readStrongBinder());
      String str3 = paramAnonymousParcel.readString();
      ArrayList localArrayList2 = new ArrayList();
      paramAnonymousParcel.readList(localArrayList2, localClassLoader);
      StatusHints localStatusHints = (StatusHints)paramAnonymousParcel.readParcelable(localClassLoader);
      int i1 = paramAnonymousParcel.readInt();
      ArrayList localArrayList3 = new ArrayList();
      paramAnonymousParcel.readList(localArrayList3, localClassLoader);
      Bundle localBundle1 = paramAnonymousParcel.readBundle(localClassLoader);
      Bundle localBundle2 = paramAnonymousParcel.readBundle(localClassLoader);
      int i2 = paramAnonymousParcel.readInt();
      boolean bool2;
      if (paramAnonymousParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      return new ParcelableCall(str1, i, localDisconnectCause, localArrayList1, j, k, i2, l, localUri, m, str2, n, localGatewayInfo, localPhoneAccountHandle, bool1, localIVideoProvider, bool2, (ParcelableRttCall)paramAnonymousParcel.readParcelable(localClassLoader), str3, localArrayList2, localStatusHints, i1, localArrayList3, localBundle1, localBundle2, paramAnonymousParcel.readLong());
    }
    
    public ParcelableCall[] newArray(int paramAnonymousInt)
    {
      return new ParcelableCall[paramAnonymousInt];
    }
  };
  private final PhoneAccountHandle mAccountHandle;
  private final String mCallerDisplayName;
  private final int mCallerDisplayNamePresentation;
  private final List<String> mCannedSmsResponses;
  private final int mCapabilities;
  private final List<String> mChildCallIds;
  private final List<String> mConferenceableCallIds;
  private final long mConnectTimeMillis;
  private final long mCreationTimeMillis;
  private final DisconnectCause mDisconnectCause;
  private final Bundle mExtras;
  private final GatewayInfo mGatewayInfo;
  private final Uri mHandle;
  private final int mHandlePresentation;
  private final String mId;
  private final Bundle mIntentExtras;
  private final boolean mIsRttCallChanged;
  private final boolean mIsVideoCallProviderChanged;
  private final String mParentCallId;
  private final int mProperties;
  private final ParcelableRttCall mRttCall;
  private final int mState;
  private final StatusHints mStatusHints;
  private final int mSupportedAudioRoutes;
  private VideoCallImpl mVideoCall;
  private final IVideoProvider mVideoCallProvider;
  private final int mVideoState;
  
  public ParcelableCall(String paramString1, int paramInt1, DisconnectCause paramDisconnectCause, List<String> paramList1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, Uri paramUri, int paramInt5, String paramString2, int paramInt6, GatewayInfo paramGatewayInfo, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean1, IVideoProvider paramIVideoProvider, boolean paramBoolean2, ParcelableRttCall paramParcelableRttCall, String paramString3, List<String> paramList2, StatusHints paramStatusHints, int paramInt7, List<String> paramList3, Bundle paramBundle1, Bundle paramBundle2, long paramLong2)
  {
    mId = paramString1;
    mState = paramInt1;
    mDisconnectCause = paramDisconnectCause;
    mCannedSmsResponses = paramList1;
    mCapabilities = paramInt2;
    mProperties = paramInt3;
    mSupportedAudioRoutes = paramInt4;
    mConnectTimeMillis = paramLong1;
    mHandle = paramUri;
    mHandlePresentation = paramInt5;
    mCallerDisplayName = paramString2;
    mCallerDisplayNamePresentation = paramInt6;
    mGatewayInfo = paramGatewayInfo;
    mAccountHandle = paramPhoneAccountHandle;
    mIsVideoCallProviderChanged = paramBoolean1;
    mVideoCallProvider = paramIVideoProvider;
    mIsRttCallChanged = paramBoolean2;
    mRttCall = paramParcelableRttCall;
    mParentCallId = paramString3;
    mChildCallIds = paramList2;
    mStatusHints = paramStatusHints;
    mVideoState = paramInt7;
    mConferenceableCallIds = Collections.unmodifiableList(paramList3);
    mIntentExtras = paramBundle1;
    mExtras = paramBundle2;
    mCreationTimeMillis = paramLong2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PhoneAccountHandle getAccountHandle()
  {
    return mAccountHandle;
  }
  
  public String getCallerDisplayName()
  {
    return mCallerDisplayName;
  }
  
  public int getCallerDisplayNamePresentation()
  {
    return mCallerDisplayNamePresentation;
  }
  
  public List<String> getCannedSmsResponses()
  {
    return mCannedSmsResponses;
  }
  
  public int getCapabilities()
  {
    return mCapabilities;
  }
  
  public List<String> getChildCallIds()
  {
    return mChildCallIds;
  }
  
  public List<String> getConferenceableCallIds()
  {
    return mConferenceableCallIds;
  }
  
  public long getConnectTimeMillis()
  {
    return mConnectTimeMillis;
  }
  
  public long getCreationTimeMillis()
  {
    return mCreationTimeMillis;
  }
  
  public DisconnectCause getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public GatewayInfo getGatewayInfo()
  {
    return mGatewayInfo;
  }
  
  public Uri getHandle()
  {
    return mHandle;
  }
  
  public int getHandlePresentation()
  {
    return mHandlePresentation;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public Bundle getIntentExtras()
  {
    return mIntentExtras;
  }
  
  public boolean getIsRttCallChanged()
  {
    return mIsRttCallChanged;
  }
  
  public ParcelableRttCall getParcelableRttCall()
  {
    return mRttCall;
  }
  
  public String getParentCallId()
  {
    return mParentCallId;
  }
  
  public int getProperties()
  {
    return mProperties;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public int getSupportedAudioRoutes()
  {
    return mSupportedAudioRoutes;
  }
  
  public VideoCallImpl getVideoCallImpl(String paramString, int paramInt)
  {
    if ((mVideoCall == null) && (mVideoCallProvider != null)) {
      try
      {
        VideoCallImpl localVideoCallImpl = new android/telecom/VideoCallImpl;
        localVideoCallImpl.<init>(mVideoCallProvider, paramString, paramInt);
        mVideoCall = localVideoCallImpl;
      }
      catch (RemoteException paramString) {}
    }
    return mVideoCall;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public boolean isVideoCallProviderChanged()
  {
    return mIsVideoCallProviderChanged;
  }
  
  public String toString()
  {
    return String.format("[%s, parent:%s, children:%s]", new Object[] { mId, mParentCallId, mChildCallIds });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeInt(mState);
    paramParcel.writeParcelable(mDisconnectCause, 0);
    paramParcel.writeList(mCannedSmsResponses);
    paramParcel.writeInt(mCapabilities);
    paramParcel.writeInt(mProperties);
    paramParcel.writeLong(mConnectTimeMillis);
    paramParcel.writeParcelable(mHandle, 0);
    paramParcel.writeInt(mHandlePresentation);
    paramParcel.writeString(mCallerDisplayName);
    paramParcel.writeInt(mCallerDisplayNamePresentation);
    paramParcel.writeParcelable(mGatewayInfo, 0);
    paramParcel.writeParcelable(mAccountHandle, 0);
    paramParcel.writeByte((byte)mIsVideoCallProviderChanged);
    IBinder localIBinder;
    if (mVideoCallProvider != null) {
      localIBinder = mVideoCallProvider.asBinder();
    } else {
      localIBinder = null;
    }
    paramParcel.writeStrongBinder(localIBinder);
    paramParcel.writeString(mParentCallId);
    paramParcel.writeList(mChildCallIds);
    paramParcel.writeParcelable(mStatusHints, 0);
    paramParcel.writeInt(mVideoState);
    paramParcel.writeList(mConferenceableCallIds);
    paramParcel.writeBundle(mIntentExtras);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeInt(mSupportedAudioRoutes);
    paramParcel.writeByte((byte)mIsRttCallChanged);
    paramParcel.writeParcelable(mRttCall, 0);
    paramParcel.writeLong(mCreationTimeMillis);
  }
}
