package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.ArrayUtils;
import java.io.ByteArrayInputStream;
import java.lang.ref.SoftReference;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Signature
  implements Parcelable
{
  public static final Parcelable.Creator<Signature> CREATOR = new Parcelable.Creator()
  {
    public Signature createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Signature(paramAnonymousParcel, null);
    }
    
    public Signature[] newArray(int paramAnonymousInt)
    {
      return new Signature[paramAnonymousInt];
    }
  };
  private Certificate[] mCertificateChain;
  private int mHashCode;
  private boolean mHaveHashCode;
  private final byte[] mSignature;
  private SoftReference<String> mStringRef;
  
  private Signature(Parcel paramParcel)
  {
    mSignature = paramParcel.createByteArray();
  }
  
  public Signature(String paramString)
  {
    paramString = paramString.getBytes();
    int i = paramString.length;
    if (i % 2 == 0)
    {
      byte[] arrayOfByte = new byte[i / 2];
      int j = 0;
      int k = 0;
      while (k < i)
      {
        int m = k + 1;
        arrayOfByte[j] = ((byte)(byte)(parseHexDigit(paramString[k]) << 4 | parseHexDigit(paramString[m])));
        k = m + 1;
        j++;
      }
      mSignature = arrayOfByte;
      return;
    }
    paramString = new StringBuilder();
    paramString.append("text size ");
    paramString.append(i);
    paramString.append(" is not even");
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public Signature(byte[] paramArrayOfByte)
  {
    mSignature = ((byte[])paramArrayOfByte.clone());
    mCertificateChain = null;
  }
  
  public Signature(Certificate[] paramArrayOfCertificate)
    throws CertificateEncodingException
  {
    mSignature = paramArrayOfCertificate[0].getEncoded();
    if (paramArrayOfCertificate.length > 1) {
      mCertificateChain = ((Certificate[])Arrays.copyOfRange(paramArrayOfCertificate, 1, paramArrayOfCertificate.length));
    }
  }
  
  public static boolean areEffectiveMatch(Signature paramSignature1, Signature paramSignature2)
    throws CertificateException
  {
    CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
    return bounce(localCertificateFactory, paramSignature1).equals(bounce(localCertificateFactory, paramSignature2));
  }
  
  public static boolean areEffectiveMatch(Signature[] paramArrayOfSignature1, Signature[] paramArrayOfSignature2)
    throws CertificateException
  {
    CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
    Signature[] arrayOfSignature = new Signature[paramArrayOfSignature1.length];
    int i = 0;
    for (int j = 0; j < paramArrayOfSignature1.length; j++) {
      arrayOfSignature[j] = bounce(localCertificateFactory, paramArrayOfSignature1[j]);
    }
    paramArrayOfSignature1 = new Signature[paramArrayOfSignature2.length];
    for (j = i; j < paramArrayOfSignature2.length; j++) {
      paramArrayOfSignature1[j] = bounce(localCertificateFactory, paramArrayOfSignature2[j]);
    }
    return areExactMatch(arrayOfSignature, paramArrayOfSignature1);
  }
  
  public static boolean areExactMatch(Signature[] paramArrayOfSignature1, Signature[] paramArrayOfSignature2)
  {
    boolean bool;
    if ((paramArrayOfSignature1.length == paramArrayOfSignature2.length) && (ArrayUtils.containsAll(paramArrayOfSignature1, paramArrayOfSignature2)) && (ArrayUtils.containsAll(paramArrayOfSignature2, paramArrayOfSignature1))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static Signature bounce(CertificateFactory paramCertificateFactory, Signature paramSignature)
    throws CertificateException
  {
    Signature localSignature = new Signature(((X509Certificate)paramCertificateFactory.generateCertificate(new ByteArrayInputStream(mSignature))).getEncoded());
    if (Math.abs(mSignature.length - mSignature.length) <= 2) {
      return localSignature;
    }
    paramCertificateFactory = new StringBuilder();
    paramCertificateFactory.append("Bounced cert length looks fishy; before ");
    paramCertificateFactory.append(mSignature.length);
    paramCertificateFactory.append(", after ");
    paramCertificateFactory.append(mSignature.length);
    throw new CertificateException(paramCertificateFactory.toString());
  }
  
  private static final int parseHexDigit(int paramInt)
  {
    if ((48 <= paramInt) && (paramInt <= 57)) {
      return paramInt - 48;
    }
    if ((97 <= paramInt) && (paramInt <= 102)) {
      return paramInt - 97 + 10;
    }
    if ((65 <= paramInt) && (paramInt <= 70)) {
      return paramInt - 65 + 10;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid character ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" in hex string");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject != null) {
      try
      {
        paramObject = (Signature)paramObject;
        if (this != paramObject)
        {
          boolean bool2 = Arrays.equals(mSignature, mSignature);
          if (!bool2) {
            break label40;
          }
        }
        bool1 = true;
        label40:
        return bool1;
      }
      catch (ClassCastException paramObject) {}
    }
    return false;
  }
  
  public Signature[] getChainSignatures()
    throws CertificateEncodingException
  {
    Certificate[] arrayOfCertificate = mCertificateChain;
    int i = 0;
    if (arrayOfCertificate == null) {
      return new Signature[] { this };
    }
    Signature[] arrayOfSignature = new Signature[1 + mCertificateChain.length];
    arrayOfSignature[0] = this;
    int j = 1;
    arrayOfCertificate = mCertificateChain;
    int k = arrayOfCertificate.length;
    while (i < k)
    {
      arrayOfSignature[j] = new Signature(arrayOfCertificate[i].getEncoded());
      i++;
      j++;
    }
    return arrayOfSignature;
  }
  
  public PublicKey getPublicKey()
    throws CertificateException
  {
    return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(mSignature)).getPublicKey();
  }
  
  public int hashCode()
  {
    if (mHaveHashCode) {
      return mHashCode;
    }
    mHashCode = Arrays.hashCode(mSignature);
    mHaveHashCode = true;
    return mHashCode;
  }
  
  public byte[] toByteArray()
  {
    byte[] arrayOfByte = new byte[mSignature.length];
    System.arraycopy(mSignature, 0, arrayOfByte, 0, mSignature.length);
    return arrayOfByte;
  }
  
  public char[] toChars()
  {
    return toChars(null, null);
  }
  
  public char[] toChars(char[] paramArrayOfChar, int[] paramArrayOfInt)
  {
    byte[] arrayOfByte = mSignature;
    int i = arrayOfByte.length;
    int j = i * 2;
    if ((paramArrayOfChar != null) && (j <= paramArrayOfChar.length)) {
      break label37;
    }
    paramArrayOfChar = new char[j];
    label37:
    for (j = 0; j < i; j++)
    {
      int k = arrayOfByte[j];
      int m = k >> 4 & 0xF;
      if (m >= 10) {
        m = 97 + m - 10;
      } else {
        m += 48;
      }
      paramArrayOfChar[(j * 2)] = ((char)(char)m);
      m = k & 0xF;
      if (m >= 10) {
        m = 97 + m - 10;
      } else {
        m += 48;
      }
      paramArrayOfChar[(j * 2 + 1)] = ((char)(char)m);
    }
    if (paramArrayOfInt != null) {
      paramArrayOfInt[0] = i;
    }
    return paramArrayOfChar;
  }
  
  public String toCharsString()
  {
    if (mStringRef == null) {
      str = null;
    } else {
      str = (String)mStringRef.get();
    }
    if (str != null) {
      return str;
    }
    String str = new String(toChars());
    mStringRef = new SoftReference(str);
    return str;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mSignature);
  }
}
