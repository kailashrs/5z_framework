package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@SystemApi
public final class KeyChainProtectionParams
  implements Parcelable
{
  public static final Parcelable.Creator<KeyChainProtectionParams> CREATOR = new Parcelable.Creator()
  {
    public KeyChainProtectionParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyChainProtectionParams(paramAnonymousParcel);
    }
    
    public KeyChainProtectionParams[] newArray(int paramAnonymousInt)
    {
      return new KeyChainProtectionParams[paramAnonymousInt];
    }
  };
  public static final int TYPE_LOCKSCREEN = 100;
  public static final int UI_FORMAT_PASSWORD = 2;
  public static final int UI_FORMAT_PATTERN = 3;
  public static final int UI_FORMAT_PIN = 1;
  private KeyDerivationParams mKeyDerivationParams;
  private Integer mLockScreenUiFormat;
  private byte[] mSecret;
  private Integer mUserSecretType;
  
  private KeyChainProtectionParams() {}
  
  protected KeyChainProtectionParams(Parcel paramParcel)
  {
    mUserSecretType = Integer.valueOf(paramParcel.readInt());
    mLockScreenUiFormat = Integer.valueOf(paramParcel.readInt());
    mKeyDerivationParams = ((KeyDerivationParams)paramParcel.readTypedObject(KeyDerivationParams.CREATOR));
    mSecret = paramParcel.createByteArray();
  }
  
  public void clearSecret()
  {
    Arrays.fill(mSecret, (byte)0);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public KeyDerivationParams getKeyDerivationParams()
  {
    return mKeyDerivationParams;
  }
  
  public int getLockScreenUiFormat()
  {
    return mLockScreenUiFormat.intValue();
  }
  
  public byte[] getSecret()
  {
    return mSecret;
  }
  
  public int getUserSecretType()
  {
    return mUserSecretType.intValue();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mUserSecretType.intValue());
    paramParcel.writeInt(mLockScreenUiFormat.intValue());
    paramParcel.writeTypedObject(mKeyDerivationParams, paramInt);
    paramParcel.writeByteArray(mSecret);
  }
  
  public static class Builder
  {
    private KeyChainProtectionParams mInstance = new KeyChainProtectionParams(null);
    
    public Builder() {}
    
    public KeyChainProtectionParams build()
    {
      if (mInstance.mUserSecretType == null) {
        KeyChainProtectionParams.access$102(mInstance, Integer.valueOf(100));
      }
      Preconditions.checkNotNull(mInstance.mLockScreenUiFormat);
      Preconditions.checkNotNull(mInstance.mKeyDerivationParams);
      if (mInstance.mSecret == null) {
        KeyChainProtectionParams.access$402(mInstance, new byte[0]);
      }
      return mInstance;
    }
    
    public Builder setKeyDerivationParams(KeyDerivationParams paramKeyDerivationParams)
    {
      KeyChainProtectionParams.access$302(mInstance, paramKeyDerivationParams);
      return this;
    }
    
    public Builder setLockScreenUiFormat(int paramInt)
    {
      KeyChainProtectionParams.access$202(mInstance, Integer.valueOf(paramInt));
      return this;
    }
    
    public Builder setSecret(byte[] paramArrayOfByte)
    {
      KeyChainProtectionParams.access$402(mInstance, paramArrayOfByte);
      return this;
    }
    
    public Builder setUserSecretType(int paramInt)
    {
      KeyChainProtectionParams.access$102(mInstance, Integer.valueOf(paramInt));
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LockScreenUiFormat {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UserSecretType {}
}
