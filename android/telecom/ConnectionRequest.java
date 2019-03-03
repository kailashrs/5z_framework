package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ConnectionRequest
  implements Parcelable
{
  public static final Parcelable.Creator<ConnectionRequest> CREATOR = new Parcelable.Creator()
  {
    public ConnectionRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConnectionRequest(paramAnonymousParcel, null);
    }
    
    public ConnectionRequest[] newArray(int paramAnonymousInt)
    {
      return new ConnectionRequest[paramAnonymousInt];
    }
  };
  private final PhoneAccountHandle mAccountHandle;
  private final Uri mAddress;
  private final Bundle mExtras;
  private final ParcelFileDescriptor mRttPipeFromInCall;
  private final ParcelFileDescriptor mRttPipeToInCall;
  private Connection.RttTextStream mRttTextStream;
  private final boolean mShouldShowIncomingCallUi;
  private final String mTelecomCallId;
  private final int mVideoState;
  
  private ConnectionRequest(Parcel paramParcel)
  {
    mAccountHandle = ((PhoneAccountHandle)paramParcel.readParcelable(getClass().getClassLoader()));
    mAddress = ((Uri)paramParcel.readParcelable(getClass().getClassLoader()));
    mExtras = ((Bundle)paramParcel.readParcelable(getClass().getClassLoader()));
    mVideoState = paramParcel.readInt();
    mTelecomCallId = paramParcel.readString();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mShouldShowIncomingCallUi = bool;
    mRttPipeFromInCall = ((ParcelFileDescriptor)paramParcel.readParcelable(getClass().getClassLoader()));
    mRttPipeToInCall = ((ParcelFileDescriptor)paramParcel.readParcelable(getClass().getClassLoader()));
  }
  
  public ConnectionRequest(PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri, Bundle paramBundle)
  {
    this(paramPhoneAccountHandle, paramUri, paramBundle, 0, null, false, null, null);
  }
  
  public ConnectionRequest(PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri, Bundle paramBundle, int paramInt)
  {
    this(paramPhoneAccountHandle, paramUri, paramBundle, paramInt, null, false, null, null);
  }
  
  public ConnectionRequest(PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri, Bundle paramBundle, int paramInt, String paramString, boolean paramBoolean)
  {
    this(paramPhoneAccountHandle, paramUri, paramBundle, paramInt, paramString, paramBoolean, null, null);
  }
  
  private ConnectionRequest(PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri, Bundle paramBundle, int paramInt, String paramString, boolean paramBoolean, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    mAccountHandle = paramPhoneAccountHandle;
    mAddress = paramUri;
    mExtras = paramBundle;
    mVideoState = paramInt;
    mTelecomCallId = paramString;
    mShouldShowIncomingCallUi = paramBoolean;
    mRttPipeFromInCall = paramParcelFileDescriptor1;
    mRttPipeToInCall = paramParcelFileDescriptor2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PhoneAccountHandle getAccountHandle()
  {
    return mAccountHandle;
  }
  
  public Uri getAddress()
  {
    return mAddress;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public ParcelFileDescriptor getRttPipeFromInCall()
  {
    return mRttPipeFromInCall;
  }
  
  public ParcelFileDescriptor getRttPipeToInCall()
  {
    return mRttPipeToInCall;
  }
  
  public Connection.RttTextStream getRttTextStream()
  {
    if (isRequestingRtt())
    {
      if (mRttTextStream == null) {
        mRttTextStream = new Connection.RttTextStream(mRttPipeToInCall, mRttPipeFromInCall);
      }
      return mRttTextStream;
    }
    return null;
  }
  
  public String getTelecomCallId()
  {
    return mTelecomCallId;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public boolean isRequestingRtt()
  {
    boolean bool;
    if ((mRttPipeFromInCall != null) && (mRttPipeToInCall != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean shouldShowIncomingCallUi()
  {
    return mShouldShowIncomingCallUi;
  }
  
  public String toString()
  {
    Object localObject1;
    if (mAddress == null) {
      localObject1 = Uri.EMPTY;
    } else {
      localObject1 = Connection.toLogSafePhoneNumber(mAddress.toString());
    }
    Object localObject2;
    if (mExtras == null) {
      localObject2 = "";
    } else {
      localObject2 = mExtras;
    }
    return String.format("ConnectionRequest %s %s", new Object[] { localObject1, localObject2 });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mAccountHandle, 0);
    paramParcel.writeParcelable(mAddress, 0);
    paramParcel.writeParcelable(mExtras, 0);
    paramParcel.writeInt(mVideoState);
    paramParcel.writeString(mTelecomCallId);
    paramParcel.writeInt(mShouldShowIncomingCallUi);
    paramParcel.writeParcelable(mRttPipeFromInCall, 0);
    paramParcel.writeParcelable(mRttPipeToInCall, 0);
  }
  
  public static final class Builder
  {
    private PhoneAccountHandle mAccountHandle;
    private Uri mAddress;
    private Bundle mExtras;
    private ParcelFileDescriptor mRttPipeFromInCall;
    private ParcelFileDescriptor mRttPipeToInCall;
    private boolean mShouldShowIncomingCallUi = false;
    private String mTelecomCallId;
    private int mVideoState = 0;
    
    public Builder() {}
    
    public ConnectionRequest build()
    {
      return new ConnectionRequest(mAccountHandle, mAddress, mExtras, mVideoState, mTelecomCallId, mShouldShowIncomingCallUi, mRttPipeFromInCall, mRttPipeToInCall, null);
    }
    
    public Builder setAccountHandle(PhoneAccountHandle paramPhoneAccountHandle)
    {
      mAccountHandle = paramPhoneAccountHandle;
      return this;
    }
    
    public Builder setAddress(Uri paramUri)
    {
      mAddress = paramUri;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mExtras = paramBundle;
      return this;
    }
    
    public Builder setRttPipeFromInCall(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      mRttPipeFromInCall = paramParcelFileDescriptor;
      return this;
    }
    
    public Builder setRttPipeToInCall(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      mRttPipeToInCall = paramParcelFileDescriptor;
      return this;
    }
    
    public Builder setShouldShowIncomingCallUi(boolean paramBoolean)
    {
      mShouldShowIncomingCallUi = paramBoolean;
      return this;
    }
    
    public Builder setTelecomCallId(String paramString)
    {
      mTelecomCallId = paramString;
      return this;
    }
    
    public Builder setVideoState(int paramInt)
    {
      mVideoState = paramInt;
      return this;
    }
  }
}
