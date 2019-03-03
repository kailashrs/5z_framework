package android.security.keystore.recovery;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public final class RecoveryCertPath
  implements Parcelable
{
  private static final String CERT_PATH_ENCODING = "PkiPath";
  public static final Parcelable.Creator<RecoveryCertPath> CREATOR = new Parcelable.Creator()
  {
    public RecoveryCertPath createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RecoveryCertPath(paramAnonymousParcel, null);
    }
    
    public RecoveryCertPath[] newArray(int paramAnonymousInt)
    {
      return new RecoveryCertPath[paramAnonymousInt];
    }
  };
  private final byte[] mEncodedCertPath;
  
  private RecoveryCertPath(Parcel paramParcel)
  {
    mEncodedCertPath = paramParcel.createByteArray();
  }
  
  private RecoveryCertPath(byte[] paramArrayOfByte)
  {
    mEncodedCertPath = ((byte[])Preconditions.checkNotNull(paramArrayOfByte));
  }
  
  public static RecoveryCertPath createRecoveryCertPath(CertPath paramCertPath)
    throws CertificateException
  {
    try
    {
      paramCertPath = new RecoveryCertPath(encodeCertPath(paramCertPath));
      return paramCertPath;
    }
    catch (CertificateEncodingException paramCertPath)
    {
      throw new CertificateException("Failed to encode the given CertPath", paramCertPath);
    }
  }
  
  private static CertPath decodeCertPath(byte[] paramArrayOfByte)
    throws CertificateException
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      return localCertificateFactory.generateCertPath(new ByteArrayInputStream(paramArrayOfByte), "PkiPath");
    }
    catch (CertificateException paramArrayOfByte)
    {
      throw new RuntimeException(paramArrayOfByte);
    }
  }
  
  private static byte[] encodeCertPath(CertPath paramCertPath)
    throws CertificateEncodingException
  {
    Preconditions.checkNotNull(paramCertPath);
    return paramCertPath.getEncoded("PkiPath");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CertPath getCertPath()
    throws CertificateException
  {
    return decodeCertPath(mEncodedCertPath);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mEncodedCertPath);
  }
}
