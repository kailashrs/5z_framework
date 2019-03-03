package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PreciseCallState
  implements Parcelable
{
  public static final Parcelable.Creator<PreciseCallState> CREATOR = new Parcelable.Creator()
  {
    public PreciseCallState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PreciseCallState(paramAnonymousParcel, null);
    }
    
    public PreciseCallState[] newArray(int paramAnonymousInt)
    {
      return new PreciseCallState[paramAnonymousInt];
    }
  };
  public static final int PRECISE_CALL_STATE_ACTIVE = 1;
  public static final int PRECISE_CALL_STATE_ALERTING = 4;
  public static final int PRECISE_CALL_STATE_DIALING = 3;
  public static final int PRECISE_CALL_STATE_DISCONNECTED = 7;
  public static final int PRECISE_CALL_STATE_DISCONNECTING = 8;
  public static final int PRECISE_CALL_STATE_HOLDING = 2;
  public static final int PRECISE_CALL_STATE_IDLE = 0;
  public static final int PRECISE_CALL_STATE_INCOMING = 5;
  public static final int PRECISE_CALL_STATE_NOT_VALID = -1;
  public static final int PRECISE_CALL_STATE_WAITING = 6;
  private int mBackgroundCallState = -1;
  private int mDisconnectCause = -1;
  private int mForegroundCallState = -1;
  private int mPreciseDisconnectCause = -1;
  private int mRingingCallState = -1;
  
  public PreciseCallState() {}
  
  public PreciseCallState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mRingingCallState = paramInt1;
    mForegroundCallState = paramInt2;
    mBackgroundCallState = paramInt3;
    mDisconnectCause = paramInt4;
    mPreciseDisconnectCause = paramInt5;
  }
  
  private PreciseCallState(Parcel paramParcel)
  {
    mRingingCallState = paramParcel.readInt();
    mForegroundCallState = paramParcel.readInt();
    mBackgroundCallState = paramParcel.readInt();
    mDisconnectCause = paramParcel.readInt();
    mPreciseDisconnectCause = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PreciseCallState)paramObject;
    if ((mRingingCallState == mRingingCallState) || (mForegroundCallState == mForegroundCallState) || (mBackgroundCallState == mBackgroundCallState) || (mDisconnectCause == mDisconnectCause) || (mPreciseDisconnectCause == mPreciseDisconnectCause)) {
      bool = false;
    }
    return bool;
  }
  
  public int getBackgroundCallState()
  {
    return mBackgroundCallState;
  }
  
  public int getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public int getForegroundCallState()
  {
    return mForegroundCallState;
  }
  
  public int getPreciseDisconnectCause()
  {
    return mPreciseDisconnectCause;
  }
  
  public int getRingingCallState()
  {
    return mRingingCallState;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * 1 + mRingingCallState) + mForegroundCallState) + mBackgroundCallState) + mDisconnectCause) + mPreciseDisconnectCause;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Ringing call state: ");
    localStringBuilder.append(mRingingCallState);
    localStringBuffer.append(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(", Foreground call state: ");
    localStringBuilder.append(mForegroundCallState);
    localStringBuffer.append(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(", Background call state: ");
    localStringBuilder.append(mBackgroundCallState);
    localStringBuffer.append(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(", Disconnect cause: ");
    localStringBuilder.append(mDisconnectCause);
    localStringBuffer.append(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(", Precise disconnect cause: ");
    localStringBuilder.append(mPreciseDisconnectCause);
    localStringBuffer.append(localStringBuilder.toString());
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRingingCallState);
    paramParcel.writeInt(mForegroundCallState);
    paramParcel.writeInt(mBackgroundCallState);
    paramParcel.writeInt(mDisconnectCause);
    paramParcel.writeInt(mPreciseDisconnectCause);
  }
}
