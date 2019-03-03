package android.hardware.fingerprint;

import android.hardware.biometrics.BiometricAuthenticator.BiometricIdentifier;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class Fingerprint
  extends BiometricAuthenticator.BiometricIdentifier
{
  public static final Parcelable.Creator<Fingerprint> CREATOR = new Parcelable.Creator()
  {
    public Fingerprint createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Fingerprint(paramAnonymousParcel, null);
    }
    
    public Fingerprint[] newArray(int paramAnonymousInt)
    {
      return new Fingerprint[paramAnonymousInt];
    }
  };
  private long mDeviceId;
  private int mFingerId;
  private int mGroupId;
  private CharSequence mName;
  
  private Fingerprint(Parcel paramParcel)
  {
    mName = paramParcel.readString();
    mGroupId = paramParcel.readInt();
    mFingerId = paramParcel.readInt();
    mDeviceId = paramParcel.readLong();
  }
  
  public Fingerprint(CharSequence paramCharSequence, int paramInt1, int paramInt2, long paramLong)
  {
    mName = paramCharSequence;
    mGroupId = paramInt1;
    mFingerId = paramInt2;
    mDeviceId = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getDeviceId()
  {
    return mDeviceId;
  }
  
  public int getFingerId()
  {
    return mFingerId;
  }
  
  public int getGroupId()
  {
    return mGroupId;
  }
  
  public CharSequence getName()
  {
    return mName;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName.toString());
    paramParcel.writeInt(mGroupId);
    paramParcel.writeInt(mFingerId);
    paramParcel.writeLong(mDeviceId);
  }
}
