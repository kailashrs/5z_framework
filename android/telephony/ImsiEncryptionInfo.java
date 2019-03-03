package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public final class ImsiEncryptionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ImsiEncryptionInfo> CREATOR = new Parcelable.Creator()
  {
    public ImsiEncryptionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsiEncryptionInfo(paramAnonymousParcel);
    }
    
    public ImsiEncryptionInfo[] newArray(int paramAnonymousInt)
    {
      return new ImsiEncryptionInfo[paramAnonymousInt];
    }
  };
  private static final String LOG_TAG = "ImsiEncryptionInfo";
  private final Date expirationTime;
  private final String keyIdentifier;
  private final int keyType;
  private final String mcc;
  private final String mnc;
  private final PublicKey publicKey;
  
  public ImsiEncryptionInfo(Parcel paramParcel)
  {
    byte[] arrayOfByte = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(arrayOfByte);
    publicKey = makeKeyObject(arrayOfByte);
    mcc = paramParcel.readString();
    mnc = paramParcel.readString();
    keyIdentifier = paramParcel.readString();
    keyType = paramParcel.readInt();
    expirationTime = new Date(paramParcel.readLong());
  }
  
  public ImsiEncryptionInfo(String paramString1, String paramString2, int paramInt, String paramString3, PublicKey paramPublicKey, Date paramDate)
  {
    mcc = paramString1;
    mnc = paramString2;
    keyType = paramInt;
    publicKey = paramPublicKey;
    keyIdentifier = paramString3;
    expirationTime = paramDate;
  }
  
  public ImsiEncryptionInfo(String paramString1, String paramString2, int paramInt, String paramString3, byte[] paramArrayOfByte, Date paramDate)
  {
    this(paramString1, paramString2, paramInt, paramString3, makeKeyObject(paramArrayOfByte), paramDate);
  }
  
  private static PublicKey makeKeyObject(byte[] paramArrayOfByte)
  {
    try
    {
      X509EncodedKeySpec localX509EncodedKeySpec = new java/security/spec/X509EncodedKeySpec;
      localX509EncodedKeySpec.<init>(paramArrayOfByte);
      paramArrayOfByte = KeyFactory.getInstance("RSA").generatePublic(localX509EncodedKeySpec);
      return paramArrayOfByte;
    }
    catch (InvalidKeySpecException|NoSuchAlgorithmException paramArrayOfByte)
    {
      Log.e("ImsiEncryptionInfo", "Error makeKeyObject: unable to convert into PublicKey", paramArrayOfByte);
      throw new IllegalArgumentException();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Date getExpirationTime()
  {
    return expirationTime;
  }
  
  public String getKeyIdentifier()
  {
    return keyIdentifier;
  }
  
  public int getKeyType()
  {
    return keyType;
  }
  
  public String getMcc()
  {
    return mcc;
  }
  
  public String getMnc()
  {
    return mnc;
  }
  
  public PublicKey getPublicKey()
  {
    return publicKey;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ImsiEncryptionInfo mcc=");
    localStringBuilder.append(mcc);
    localStringBuilder.append("mnc=");
    localStringBuilder.append(mnc);
    localStringBuilder.append("publicKey=");
    localStringBuilder.append(publicKey);
    localStringBuilder.append(", keyIdentifier=");
    localStringBuilder.append(keyIdentifier);
    localStringBuilder.append(", keyType=");
    localStringBuilder.append(keyType);
    localStringBuilder.append(", expirationTime=");
    localStringBuilder.append(expirationTime);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    byte[] arrayOfByte = publicKey.getEncoded();
    paramParcel.writeInt(arrayOfByte.length);
    paramParcel.writeByteArray(arrayOfByte);
    paramParcel.writeString(mcc);
    paramParcel.writeString(mnc);
    paramParcel.writeString(keyIdentifier);
    paramParcel.writeInt(keyType);
    paramParcel.writeLong(expirationTime.getTime());
  }
}
