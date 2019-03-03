package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class KeyDerivationParams
  implements Parcelable
{
  public static final int ALGORITHM_SCRYPT = 2;
  public static final int ALGORITHM_SHA256 = 1;
  public static final Parcelable.Creator<KeyDerivationParams> CREATOR = new Parcelable.Creator()
  {
    public KeyDerivationParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyDerivationParams(paramAnonymousParcel);
    }
    
    public KeyDerivationParams[] newArray(int paramAnonymousInt)
    {
      return new KeyDerivationParams[paramAnonymousInt];
    }
  };
  private final int mAlgorithm;
  private final int mMemoryDifficulty;
  private final byte[] mSalt;
  
  private KeyDerivationParams(int paramInt, byte[] paramArrayOfByte)
  {
    this(paramInt, paramArrayOfByte, -1);
  }
  
  private KeyDerivationParams(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    mAlgorithm = paramInt1;
    mSalt = ((byte[])Preconditions.checkNotNull(paramArrayOfByte));
    mMemoryDifficulty = paramInt2;
  }
  
  protected KeyDerivationParams(Parcel paramParcel)
  {
    mAlgorithm = paramParcel.readInt();
    mSalt = paramParcel.createByteArray();
    mMemoryDifficulty = paramParcel.readInt();
  }
  
  public static KeyDerivationParams createScryptParams(byte[] paramArrayOfByte, int paramInt)
  {
    return new KeyDerivationParams(2, paramArrayOfByte, paramInt);
  }
  
  public static KeyDerivationParams createSha256Params(byte[] paramArrayOfByte)
  {
    return new KeyDerivationParams(1, paramArrayOfByte);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAlgorithm()
  {
    return mAlgorithm;
  }
  
  public int getMemoryDifficulty()
  {
    return mMemoryDifficulty;
  }
  
  public byte[] getSalt()
  {
    return mSalt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAlgorithm);
    paramParcel.writeByteArray(mSalt);
    paramParcel.writeInt(mMemoryDifficulty);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface KeyDerivationAlgorithm {}
}
