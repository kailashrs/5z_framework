package android.net;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.HexDump;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class IpSecAlgorithm
  implements Parcelable
{
  public static final String AUTH_CRYPT_AES_GCM = "rfc4106(gcm(aes))";
  public static final String AUTH_HMAC_MD5 = "hmac(md5)";
  public static final String AUTH_HMAC_SHA1 = "hmac(sha1)";
  public static final String AUTH_HMAC_SHA256 = "hmac(sha256)";
  public static final String AUTH_HMAC_SHA384 = "hmac(sha384)";
  public static final String AUTH_HMAC_SHA512 = "hmac(sha512)";
  public static final Parcelable.Creator<IpSecAlgorithm> CREATOR = new Parcelable.Creator()
  {
    public IpSecAlgorithm createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecAlgorithm(paramAnonymousParcel.readString(), paramAnonymousParcel.createByteArray(), paramAnonymousParcel.readInt());
    }
    
    public IpSecAlgorithm[] newArray(int paramAnonymousInt)
    {
      return new IpSecAlgorithm[paramAnonymousInt];
    }
  };
  public static final String CRYPT_AES_CBC = "cbc(aes)";
  public static final String CRYPT_NULL = "ecb(cipher_null)";
  private static final String TAG = "IpSecAlgorithm";
  private final byte[] mKey;
  private final String mName;
  private final int mTruncLenBits;
  
  public IpSecAlgorithm(String paramString, byte[] paramArrayOfByte)
  {
    this(paramString, paramArrayOfByte, 0);
  }
  
  public IpSecAlgorithm(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    mName = paramString;
    mKey = ((byte[])paramArrayOfByte.clone());
    mTruncLenBits = paramInt;
    checkValidOrThrow(mName, mKey.length * 8, mTruncLenBits);
  }
  
  private static void checkValidOrThrow(String paramString, int paramInt1, int paramInt2)
  {
    int i = 1;
    int j = paramString.hashCode();
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    switch (j)
    {
    default: 
      break;
    case 2065384259: 
      if (paramString.equals("hmac(sha1)")) {
        j = 2;
      }
      break;
    case 759177996: 
      if (paramString.equals("hmac(md5)")) {
        j = 1;
      }
      break;
    case 559510590: 
      if (paramString.equals("hmac(sha512)")) {
        j = 5;
      }
      break;
    case 559457797: 
      if (paramString.equals("hmac(sha384)")) {
        j = 4;
      }
      break;
    case 559425185: 
      if (paramString.equals("hmac(sha256)")) {
        j = 3;
      }
      break;
    case 394796030: 
      if (paramString.equals("cbc(aes)")) {
        j = 0;
      }
      break;
    case -1137603038: 
      if (paramString.equals("rfc4106(gcm(aes))")) {
        j = 6;
      }
      break;
    }
    j = -1;
    switch (j)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't find an algorithm: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 6: 
      if ((paramInt1 != 160) && (paramInt1 != 224) && (paramInt1 != 288)) {
        j = 0;
      } else {
        j = 1;
      }
      i = j;
      if ((paramInt2 != 64) && (paramInt2 != 96) && (paramInt2 != 128)) {
        j = i4;
      } else {
        j = 1;
      }
      k = j;
      j = i;
      i = k;
      break;
    case 5: 
      if (paramInt1 == 512) {
        j = 1;
      } else {
        j = 0;
      }
      i = j;
      j = k;
      if (paramInt2 >= 256)
      {
        j = k;
        if (paramInt2 <= 512) {
          j = 1;
        }
      }
      k = j;
      j = i;
      i = k;
      break;
    case 4: 
      if (paramInt1 == 384) {
        j = 1;
      } else {
        j = 0;
      }
      i = j;
      j = m;
      if (paramInt2 >= 192)
      {
        j = m;
        if (paramInt2 <= 384) {
          j = 1;
        }
      }
      k = j;
      j = i;
      i = k;
      break;
    case 3: 
      if (paramInt1 == 256) {
        j = 1;
      } else {
        j = 0;
      }
      i = j;
      j = n;
      if (paramInt2 >= 96)
      {
        j = n;
        if (paramInt2 <= 256) {
          j = 1;
        }
      }
      k = j;
      j = i;
      i = k;
      break;
    case 2: 
      if (paramInt1 == 160) {
        j = 1;
      } else {
        j = 0;
      }
      i = j;
      j = i1;
      if (paramInt2 >= 96)
      {
        j = i1;
        if (paramInt2 <= 160) {
          j = 1;
        }
      }
      k = j;
      j = i;
      i = k;
      break;
    case 1: 
      if (paramInt1 == 128) {
        j = 1;
      } else {
        j = 0;
      }
      i = j;
      j = i2;
      if (paramInt2 >= 96)
      {
        j = i2;
        if (paramInt2 <= 128) {
          j = 1;
        }
      }
      k = j;
      j = i;
      i = k;
      break;
    case 0: 
      if ((paramInt1 != 128) && (paramInt1 != 192) && (paramInt1 != 256)) {
        j = i3;
      } else {
        j = 1;
      }
      break;
    }
    if (j != 0)
    {
      if (i != 0) {
        return;
      }
      paramString = new StringBuilder();
      paramString.append("Invalid truncation keyLength: ");
      paramString.append(paramInt2);
      throw new IllegalArgumentException(paramString.toString());
    }
    paramString = new StringBuilder();
    paramString.append("Invalid key material keyLength: ");
    paramString.append(paramInt1);
    throw new IllegalArgumentException(paramString.toString());
  }
  
  @VisibleForTesting
  public static boolean equals(IpSecAlgorithm paramIpSecAlgorithm1, IpSecAlgorithm paramIpSecAlgorithm2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if ((paramIpSecAlgorithm1 != null) && (paramIpSecAlgorithm2 != null))
    {
      if ((mName.equals(mName)) && (Arrays.equals(mKey, mKey)) && (mTruncLenBits == mTruncLenBits)) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (paramIpSecAlgorithm1 == paramIpSecAlgorithm2) {
      bool2 = true;
    }
    return bool2;
  }
  
  private static boolean isUnsafeBuild()
  {
    boolean bool;
    if ((Build.IS_DEBUGGABLE) && (Build.IS_ENG)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getKey()
  {
    return (byte[])mKey.clone();
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getTruncationLengthBits()
  {
    return mTruncLenBits;
  }
  
  public boolean isAead()
  {
    return getName().equals("rfc4106(gcm(aes))");
  }
  
  public boolean isAuthentication()
  {
    String str = getName();
    switch (str.hashCode())
    {
    default: 
      break;
    case 2065384259: 
      if (str.equals("hmac(sha1)")) {
        i = 1;
      }
      break;
    case 759177996: 
      if (str.equals("hmac(md5)")) {
        i = 0;
      }
      break;
    case 559510590: 
      if (str.equals("hmac(sha512)")) {
        i = 4;
      }
      break;
    case 559457797: 
      if (str.equals("hmac(sha384)")) {
        i = 3;
      }
      break;
    case 559425185: 
      if (str.equals("hmac(sha256)")) {
        i = 2;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return false;
    }
    return true;
  }
  
  public boolean isEncryption()
  {
    return getName().equals("cbc(aes)");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(", mKey=");
    String str;
    if (isUnsafeBuild()) {
      str = HexDump.toHexString(mKey);
    } else {
      str = "<hidden>";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", mTruncLenBits=");
    localStringBuilder.append(mTruncLenBits);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeByteArray(mKey);
    paramParcel.writeInt(mTruncLenBits);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AlgorithmName {}
}
