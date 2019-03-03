package android.net.wifi;

import android.os.Parcel;
import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class ParcelUtil
{
  public ParcelUtil() {}
  
  public static X509Certificate readCertificate(Parcel paramParcel)
  {
    byte[] arrayOfByte = paramParcel.createByteArray();
    if (arrayOfByte == null) {
      return null;
    }
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      paramParcel = new java/io/ByteArrayInputStream;
      paramParcel.<init>(arrayOfByte);
      paramParcel = (X509Certificate)localCertificateFactory.generateCertificate(paramParcel);
      return paramParcel;
    }
    catch (CertificateException paramParcel) {}
    return null;
  }
  
  public static X509Certificate[] readCertificates(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if (i == 0) {
      return null;
    }
    X509Certificate[] arrayOfX509Certificate = new X509Certificate[i];
    for (int j = 0; j < i; j++) {
      arrayOfX509Certificate[j] = readCertificate(paramParcel);
    }
    return arrayOfX509Certificate;
  }
  
  public static PrivateKey readPrivateKey(Parcel paramParcel)
  {
    Object localObject = paramParcel.readString();
    if (localObject == null) {
      return null;
    }
    paramParcel = paramParcel.createByteArray();
    try
    {
      KeyFactory localKeyFactory = KeyFactory.getInstance((String)localObject);
      localObject = new java/security/spec/PKCS8EncodedKeySpec;
      ((PKCS8EncodedKeySpec)localObject).<init>(paramParcel);
      paramParcel = localKeyFactory.generatePrivate((KeySpec)localObject);
      return paramParcel;
    }
    catch (NoSuchAlgorithmException|InvalidKeySpecException paramParcel) {}
    return null;
  }
  
  public static void writeCertificate(Parcel paramParcel, X509Certificate paramX509Certificate)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (paramX509Certificate != null) {
      try
      {
        localObject2 = paramX509Certificate.getEncoded();
      }
      catch (CertificateEncodingException paramX509Certificate)
      {
        localObject2 = localObject1;
      }
    }
    paramParcel.writeByteArray((byte[])localObject2);
  }
  
  public static void writeCertificates(Parcel paramParcel, X509Certificate[] paramArrayOfX509Certificate)
  {
    int i = 0;
    if ((paramArrayOfX509Certificate != null) && (paramArrayOfX509Certificate.length != 0))
    {
      paramParcel.writeInt(paramArrayOfX509Certificate.length);
      while (i < paramArrayOfX509Certificate.length)
      {
        writeCertificate(paramParcel, paramArrayOfX509Certificate[i]);
        i++;
      }
      return;
    }
    paramParcel.writeInt(0);
  }
  
  public static void writePrivateKey(Parcel paramParcel, PrivateKey paramPrivateKey)
  {
    if (paramPrivateKey == null)
    {
      paramParcel.writeString(null);
      return;
    }
    paramParcel.writeString(paramPrivateKey.getAlgorithm());
    paramParcel.writeByteArray(paramPrivateKey.getEncoded());
  }
}
