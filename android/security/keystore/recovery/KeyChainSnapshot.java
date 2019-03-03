package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.util.List;

@SystemApi
public final class KeyChainSnapshot
  implements Parcelable
{
  public static final Parcelable.Creator<KeyChainSnapshot> CREATOR = new Parcelable.Creator()
  {
    public KeyChainSnapshot createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyChainSnapshot(paramAnonymousParcel);
    }
    
    public KeyChainSnapshot[] newArray(int paramAnonymousInt)
    {
      return new KeyChainSnapshot[paramAnonymousInt];
    }
  };
  private static final long DEFAULT_COUNTER_ID = 1L;
  private static final int DEFAULT_MAX_ATTEMPTS = 10;
  private RecoveryCertPath mCertPath;
  private long mCounterId = 1L;
  private byte[] mEncryptedRecoveryKeyBlob;
  private List<WrappedApplicationKey> mEntryRecoveryData;
  private List<KeyChainProtectionParams> mKeyChainProtectionParams;
  private int mMaxAttempts = 10;
  private byte[] mServerParams;
  private int mSnapshotVersion;
  
  private KeyChainSnapshot() {}
  
  protected KeyChainSnapshot(Parcel paramParcel)
  {
    mSnapshotVersion = paramParcel.readInt();
    mKeyChainProtectionParams = paramParcel.createTypedArrayList(KeyChainProtectionParams.CREATOR);
    mEncryptedRecoveryKeyBlob = paramParcel.createByteArray();
    mEntryRecoveryData = paramParcel.createTypedArrayList(WrappedApplicationKey.CREATOR);
    mMaxAttempts = paramParcel.readInt();
    mCounterId = paramParcel.readLong();
    mServerParams = paramParcel.createByteArray();
    mCertPath = ((RecoveryCertPath)paramParcel.readTypedObject(RecoveryCertPath.CREATOR));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getCounterId()
  {
    return mCounterId;
  }
  
  public byte[] getEncryptedRecoveryKeyBlob()
  {
    return mEncryptedRecoveryKeyBlob;
  }
  
  public List<KeyChainProtectionParams> getKeyChainProtectionParams()
  {
    return mKeyChainProtectionParams;
  }
  
  public int getMaxAttempts()
  {
    return mMaxAttempts;
  }
  
  public byte[] getServerParams()
  {
    return mServerParams;
  }
  
  public int getSnapshotVersion()
  {
    return mSnapshotVersion;
  }
  
  public CertPath getTrustedHardwareCertPath()
  {
    try
    {
      CertPath localCertPath = mCertPath.getCertPath();
      return localCertPath;
    }
    catch (CertificateException localCertificateException)
    {
      throw new BadParcelableException(localCertificateException);
    }
  }
  
  @Deprecated
  public byte[] getTrustedHardwarePublicKey()
  {
    throw new UnsupportedOperationException();
  }
  
  public List<WrappedApplicationKey> getWrappedApplicationKeys()
  {
    return mEntryRecoveryData;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSnapshotVersion);
    paramParcel.writeTypedList(mKeyChainProtectionParams);
    paramParcel.writeByteArray(mEncryptedRecoveryKeyBlob);
    paramParcel.writeTypedList(mEntryRecoveryData);
    paramParcel.writeInt(mMaxAttempts);
    paramParcel.writeLong(mCounterId);
    paramParcel.writeByteArray(mServerParams);
    paramParcel.writeTypedObject(mCertPath, 0);
  }
  
  public static class Builder
  {
    private KeyChainSnapshot mInstance = new KeyChainSnapshot(null);
    
    public Builder() {}
    
    public KeyChainSnapshot build()
    {
      Preconditions.checkCollectionElementsNotNull(mInstance.mKeyChainProtectionParams, "keyChainProtectionParams");
      Preconditions.checkCollectionElementsNotNull(mInstance.mEntryRecoveryData, "entryRecoveryData");
      Preconditions.checkNotNull(mInstance.mEncryptedRecoveryKeyBlob);
      Preconditions.checkNotNull(mInstance.mServerParams);
      Preconditions.checkNotNull(mInstance.mCertPath);
      return mInstance;
    }
    
    public Builder setCounterId(long paramLong)
    {
      KeyChainSnapshot.access$302(mInstance, paramLong);
      return this;
    }
    
    public Builder setEncryptedRecoveryKeyBlob(byte[] paramArrayOfByte)
    {
      KeyChainSnapshot.access$802(mInstance, paramArrayOfByte);
      return this;
    }
    
    public Builder setKeyChainProtectionParams(List<KeyChainProtectionParams> paramList)
    {
      KeyChainSnapshot.access$602(mInstance, paramList);
      return this;
    }
    
    public Builder setMaxAttempts(int paramInt)
    {
      KeyChainSnapshot.access$202(mInstance, paramInt);
      return this;
    }
    
    public Builder setServerParams(byte[] paramArrayOfByte)
    {
      KeyChainSnapshot.access$402(mInstance, paramArrayOfByte);
      return this;
    }
    
    public Builder setSnapshotVersion(int paramInt)
    {
      KeyChainSnapshot.access$102(mInstance, paramInt);
      return this;
    }
    
    public Builder setTrustedHardwareCertPath(CertPath paramCertPath)
      throws CertificateException
    {
      KeyChainSnapshot.access$502(mInstance, RecoveryCertPath.createRecoveryCertPath(paramCertPath));
      return this;
    }
    
    @Deprecated
    public Builder setTrustedHardwarePublicKey(byte[] paramArrayOfByte)
    {
      throw new UnsupportedOperationException();
    }
    
    public Builder setWrappedApplicationKeys(List<WrappedApplicationKey> paramList)
    {
      KeyChainSnapshot.access$702(mInstance, paramList);
      return this;
    }
  }
}
