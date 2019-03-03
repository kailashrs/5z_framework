package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

@SystemApi
public final class WrappedApplicationKey
  implements Parcelable
{
  public static final Parcelable.Creator<WrappedApplicationKey> CREATOR = new Parcelable.Creator()
  {
    public WrappedApplicationKey createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WrappedApplicationKey(paramAnonymousParcel);
    }
    
    public WrappedApplicationKey[] newArray(int paramAnonymousInt)
    {
      return new WrappedApplicationKey[paramAnonymousInt];
    }
  };
  private String mAlias;
  private byte[] mEncryptedKeyMaterial;
  
  private WrappedApplicationKey() {}
  
  protected WrappedApplicationKey(Parcel paramParcel)
  {
    mAlias = paramParcel.readString();
    mEncryptedKeyMaterial = paramParcel.createByteArray();
  }
  
  public WrappedApplicationKey(String paramString, byte[] paramArrayOfByte)
  {
    mAlias = ((String)Preconditions.checkNotNull(paramString));
    mEncryptedKeyMaterial = ((byte[])Preconditions.checkNotNull(paramArrayOfByte));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Deprecated
  public byte[] getAccount()
  {
    throw new UnsupportedOperationException();
  }
  
  public String getAlias()
  {
    return mAlias;
  }
  
  public byte[] getEncryptedKeyMaterial()
  {
    return mEncryptedKeyMaterial;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mAlias);
    paramParcel.writeByteArray(mEncryptedKeyMaterial);
  }
  
  public static class Builder
  {
    private WrappedApplicationKey mInstance = new WrappedApplicationKey(null);
    
    public Builder() {}
    
    public WrappedApplicationKey build()
    {
      Preconditions.checkNotNull(mInstance.mAlias);
      Preconditions.checkNotNull(mInstance.mEncryptedKeyMaterial);
      return mInstance;
    }
    
    @Deprecated
    public Builder setAccount(byte[] paramArrayOfByte)
    {
      throw new UnsupportedOperationException();
    }
    
    public Builder setAlias(String paramString)
    {
      WrappedApplicationKey.access$102(mInstance, paramString);
      return this;
    }
    
    public Builder setEncryptedKeyMaterial(byte[] paramArrayOfByte)
    {
      WrappedApplicationKey.access$202(mInstance, paramArrayOfByte);
      return this;
    }
  }
}
