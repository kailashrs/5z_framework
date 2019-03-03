package android.net.http;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.android.internal.util.HexDump;
import com.android.org.bouncycastle.asn1.x509.X509Name;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class SslCertificate
{
  private static String ISO_8601_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
  private static final String ISSUED_BY = "issued-by";
  private static final String ISSUED_TO = "issued-to";
  private static final String VALID_NOT_AFTER = "valid-not-after";
  private static final String VALID_NOT_BEFORE = "valid-not-before";
  private static final String X509_CERTIFICATE = "x509-certificate";
  private final DName mIssuedBy;
  private final DName mIssuedTo;
  private final Date mValidNotAfter;
  private final Date mValidNotBefore;
  private final X509Certificate mX509Certificate;
  
  @Deprecated
  public SslCertificate(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this(paramString1, paramString2, parseDate(paramString3), parseDate(paramString4), null);
  }
  
  @Deprecated
  public SslCertificate(String paramString1, String paramString2, Date paramDate1, Date paramDate2)
  {
    this(paramString1, paramString2, paramDate1, paramDate2, null);
  }
  
  private SslCertificate(String paramString1, String paramString2, Date paramDate1, Date paramDate2, X509Certificate paramX509Certificate)
  {
    mIssuedTo = new DName(paramString1);
    mIssuedBy = new DName(paramString2);
    mValidNotBefore = cloneDate(paramDate1);
    mValidNotAfter = cloneDate(paramDate2);
    mX509Certificate = paramX509Certificate;
  }
  
  public SslCertificate(X509Certificate paramX509Certificate)
  {
    this(paramX509Certificate.getSubjectDN().getName(), paramX509Certificate.getIssuerDN().getName(), paramX509Certificate.getNotBefore(), paramX509Certificate.getNotAfter(), paramX509Certificate);
  }
  
  private static Date cloneDate(Date paramDate)
  {
    if (paramDate == null) {
      return null;
    }
    return (Date)paramDate.clone();
  }
  
  private static final String fingerprint(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      HexDump.appendByteAsHex(localStringBuilder, paramArrayOfByte[i], true);
      if (i + 1 != paramArrayOfByte.length) {
        localStringBuilder.append(':');
      }
    }
    return localStringBuilder.toString();
  }
  
  private String formatCertificateDate(Context paramContext, Date paramDate)
  {
    if (paramDate == null) {
      return "";
    }
    return android.text.format.DateFormat.getMediumDateFormat(paramContext).format(paramDate);
  }
  
  private static String formatDate(Date paramDate)
  {
    if (paramDate == null) {
      return "";
    }
    return new SimpleDateFormat(ISO_8601_DATE_FORMAT).format(paramDate);
  }
  
  private static String getDigest(X509Certificate paramX509Certificate, String paramString)
  {
    if (paramX509Certificate == null) {
      return "";
    }
    try
    {
      paramX509Certificate = paramX509Certificate.getEncoded();
      paramX509Certificate = fingerprint(MessageDigest.getInstance(paramString).digest(paramX509Certificate));
      return paramX509Certificate;
    }
    catch (NoSuchAlgorithmException paramX509Certificate)
    {
      return "";
    }
    catch (CertificateEncodingException paramX509Certificate) {}
    return "";
  }
  
  private static String getSerialNumber(X509Certificate paramX509Certificate)
  {
    if (paramX509Certificate == null) {
      return "";
    }
    paramX509Certificate = paramX509Certificate.getSerialNumber();
    if (paramX509Certificate == null) {
      return "";
    }
    return fingerprint(paramX509Certificate.toByteArray());
  }
  
  private static Date parseDate(String paramString)
  {
    try
    {
      SimpleDateFormat localSimpleDateFormat = new java/text/SimpleDateFormat;
      localSimpleDateFormat.<init>(ISO_8601_DATE_FORMAT);
      paramString = localSimpleDateFormat.parse(paramString);
      return paramString;
    }
    catch (ParseException paramString) {}
    return null;
  }
  
  public static SslCertificate restoreState(Bundle paramBundle)
  {
    Object localObject1 = null;
    if (paramBundle == null) {
      return null;
    }
    byte[] arrayOfByte = paramBundle.getByteArray("x509-certificate");
    if (arrayOfByte == null) {
      localObject1 = null;
    }
    for (;;)
    {
      break;
      try
      {
        Object localObject2 = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
        localByteArrayInputStream.<init>(arrayOfByte);
        localObject2 = (X509Certificate)((CertificateFactory)localObject2).generateCertificate(localByteArrayInputStream);
        localObject1 = localObject2;
      }
      catch (CertificateException localCertificateException) {}
    }
    return new SslCertificate(paramBundle.getString("issued-to"), paramBundle.getString("issued-by"), parseDate(paramBundle.getString("valid-not-before")), parseDate(paramBundle.getString("valid-not-after")), localObject1);
  }
  
  public static Bundle saveState(SslCertificate paramSslCertificate)
  {
    if (paramSslCertificate == null) {
      return null;
    }
    Bundle localBundle = new Bundle();
    localBundle.putString("issued-to", paramSslCertificate.getIssuedTo().getDName());
    localBundle.putString("issued-by", paramSslCertificate.getIssuedBy().getDName());
    localBundle.putString("valid-not-before", paramSslCertificate.getValidNotBefore());
    localBundle.putString("valid-not-after", paramSslCertificate.getValidNotAfter());
    paramSslCertificate = mX509Certificate;
    if (paramSslCertificate != null) {
      try
      {
        localBundle.putByteArray("x509-certificate", paramSslCertificate.getEncoded());
      }
      catch (CertificateEncodingException paramSslCertificate) {}
    }
    return localBundle;
  }
  
  public DName getIssuedBy()
  {
    return mIssuedBy;
  }
  
  public DName getIssuedTo()
  {
    return mIssuedTo;
  }
  
  @Deprecated
  public String getValidNotAfter()
  {
    return formatDate(mValidNotAfter);
  }
  
  public Date getValidNotAfterDate()
  {
    return cloneDate(mValidNotAfter);
  }
  
  @Deprecated
  public String getValidNotBefore()
  {
    return formatDate(mValidNotBefore);
  }
  
  public Date getValidNotBeforeDate()
  {
    return cloneDate(mValidNotBefore);
  }
  
  public View inflateCertificateView(Context paramContext)
  {
    View localView = LayoutInflater.from(paramContext).inflate(17367322, null);
    Object localObject = getIssuedTo();
    if (localObject != null)
    {
      ((TextView)localView.findViewById(16909482)).setText(((DName)localObject).getCName());
      ((TextView)localView.findViewById(16909484)).setText(((DName)localObject).getOName());
      ((TextView)localView.findViewById(16909486)).setText(((DName)localObject).getUName());
    }
    ((TextView)localView.findViewById(16909345)).setText(getSerialNumber(mX509Certificate));
    localObject = getIssuedBy();
    if (localObject != null)
    {
      ((TextView)localView.findViewById(16908831)).setText(((DName)localObject).getCName());
      ((TextView)localView.findViewById(16908833)).setText(((DName)localObject).getOName());
      ((TextView)localView.findViewById(16908835)).setText(((DName)localObject).getUName());
    }
    localObject = formatCertificateDate(paramContext, getValidNotBeforeDate());
    ((TextView)localView.findViewById(16909060)).setText((CharSequence)localObject);
    paramContext = formatCertificateDate(paramContext, getValidNotAfterDate());
    ((TextView)localView.findViewById(16908924)).setText(paramContext);
    ((TextView)localView.findViewById(16909352)).setText(getDigest(mX509Certificate, "SHA256"));
    ((TextView)localView.findViewById(16909350)).setText(getDigest(mX509Certificate, "SHA1"));
    return localView;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Issued to: ");
    localStringBuilder.append(mIssuedTo.getDName());
    localStringBuilder.append(";\nIssued by: ");
    localStringBuilder.append(mIssuedBy.getDName());
    localStringBuilder.append(";\n");
    return localStringBuilder.toString();
  }
  
  public class DName
  {
    private String mCName;
    private String mDName;
    private String mOName;
    private String mUName;
    
    public DName(String paramString)
    {
      if (paramString != null)
      {
        mDName = paramString;
        try
        {
          X509Name localX509Name = new com/android/org/bouncycastle/asn1/x509/X509Name;
          localX509Name.<init>(paramString);
          this$1 = localX509Name.getValues();
          paramString = localX509Name.getOIDs();
          for (int i = 0; i < paramString.size(); i++) {
            if (paramString.elementAt(i).equals(X509Name.CN))
            {
              if (mCName == null) {
                mCName = ((String)elementAt(i));
              }
            }
            else if ((paramString.elementAt(i).equals(X509Name.O)) && (mOName == null)) {
              mOName = ((String)elementAt(i));
            } else if ((paramString.elementAt(i).equals(X509Name.OU)) && (mUName == null)) {
              mUName = ((String)elementAt(i));
            }
          }
        }
        catch (IllegalArgumentException this$1) {}
      }
    }
    
    public String getCName()
    {
      String str;
      if (mCName != null) {
        str = mCName;
      } else {
        str = "";
      }
      return str;
    }
    
    public String getDName()
    {
      String str;
      if (mDName != null) {
        str = mDName;
      } else {
        str = "";
      }
      return str;
    }
    
    public String getOName()
    {
      String str;
      if (mOName != null) {
        str = mOName;
      } else {
        str = "";
      }
      return str;
    }
    
    public String getUName()
    {
      String str;
      if (mUName != null) {
        str = mUName;
      } else {
        str = "";
      }
      return str;
    }
  }
}
