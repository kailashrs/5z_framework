package android.telephony.ims;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import android.telephony.Rlog;

@SystemApi
public final class ImsExternalCallState
  implements Parcelable
{
  public static final int CALL_STATE_CONFIRMED = 1;
  public static final int CALL_STATE_TERMINATED = 2;
  public static final Parcelable.Creator<ImsExternalCallState> CREATOR = new Parcelable.Creator()
  {
    public ImsExternalCallState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsExternalCallState(paramAnonymousParcel);
    }
    
    public ImsExternalCallState[] newArray(int paramAnonymousInt)
    {
      return new ImsExternalCallState[paramAnonymousInt];
    }
  };
  private static final String TAG = "ImsExternalCallState";
  private Uri mAddress;
  private int mCallId;
  private int mCallState;
  private int mCallType;
  private boolean mIsHeld;
  private boolean mIsPullable;
  private Uri mLocalAddress;
  
  public ImsExternalCallState() {}
  
  public ImsExternalCallState(int paramInt1, Uri paramUri1, Uri paramUri2, boolean paramBoolean1, int paramInt2, int paramInt3, boolean paramBoolean2)
  {
    mCallId = paramInt1;
    mAddress = paramUri1;
    mLocalAddress = paramUri2;
    mIsPullable = paramBoolean1;
    mCallState = paramInt2;
    mCallType = paramInt3;
    mIsHeld = paramBoolean2;
    paramUri1 = new StringBuilder();
    paramUri1.append("ImsExternalCallState = ");
    paramUri1.append(this);
    Rlog.d("ImsExternalCallState", paramUri1.toString());
  }
  
  public ImsExternalCallState(int paramInt1, Uri paramUri, boolean paramBoolean1, int paramInt2, int paramInt3, boolean paramBoolean2)
  {
    mCallId = paramInt1;
    mAddress = paramUri;
    mIsPullable = paramBoolean1;
    mCallState = paramInt2;
    mCallType = paramInt3;
    mIsHeld = paramBoolean2;
    paramUri = new StringBuilder();
    paramUri.append("ImsExternalCallState = ");
    paramUri.append(this);
    Rlog.d("ImsExternalCallState", paramUri.toString());
  }
  
  public ImsExternalCallState(Parcel paramParcel)
  {
    mCallId = paramParcel.readInt();
    ClassLoader localClassLoader = ImsExternalCallState.class.getClassLoader();
    mAddress = ((Uri)paramParcel.readParcelable(localClassLoader));
    mLocalAddress = ((Uri)paramParcel.readParcelable(localClassLoader));
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsPullable = bool2;
    mCallState = paramParcel.readInt();
    mCallType = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mIsHeld = bool2;
    paramParcel = new StringBuilder();
    paramParcel.append("ImsExternalCallState const = ");
    paramParcel.append(this);
    Rlog.d("ImsExternalCallState", paramParcel.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Uri getAddress()
  {
    return mAddress;
  }
  
  public int getCallId()
  {
    return mCallId;
  }
  
  public int getCallState()
  {
    return mCallState;
  }
  
  public int getCallType()
  {
    return mCallType;
  }
  
  public Uri getLocalAddress()
  {
    return mLocalAddress;
  }
  
  public boolean isCallHeld()
  {
    return mIsHeld;
  }
  
  public boolean isCallPullable()
  {
    return mIsPullable;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsExternalCallState { mCallId = ");
    localStringBuilder.append(mCallId);
    localStringBuilder.append(", mAddress = ");
    localStringBuilder.append(Log.pii(mAddress));
    localStringBuilder.append(", mLocalAddress = ");
    localStringBuilder.append(Log.pii(mLocalAddress));
    localStringBuilder.append(", mIsPullable = ");
    localStringBuilder.append(mIsPullable);
    localStringBuilder.append(", mCallState = ");
    localStringBuilder.append(mCallState);
    localStringBuilder.append(", mCallType = ");
    localStringBuilder.append(mCallType);
    localStringBuilder.append(", mIsHeld = ");
    localStringBuilder.append(mIsHeld);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCallId);
    paramParcel.writeParcelable(mAddress, 0);
    paramParcel.writeParcelable(mLocalAddress, 0);
    paramParcel.writeInt(mIsPullable);
    paramParcel.writeInt(mCallState);
    paramParcel.writeInt(mCallType);
    paramParcel.writeInt(mIsHeld);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsExternalCallState writeToParcel = ");
    localStringBuilder.append(paramParcel.toString());
    Rlog.d("ImsExternalCallState", localStringBuilder.toString());
  }
}
