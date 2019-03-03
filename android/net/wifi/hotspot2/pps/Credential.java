package android.net.wifi.hotspot2.pps;

import android.net.wifi.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Credential
  implements Parcelable
{
  public static final Parcelable.Creator<Credential> CREATOR = new Parcelable.Creator()
  {
    public Credential createFromParcel(Parcel paramAnonymousParcel)
    {
      Credential localCredential = new Credential();
      localCredential.setCreationTimeInMillis(paramAnonymousParcel.readLong());
      localCredential.setExpirationTimeInMillis(paramAnonymousParcel.readLong());
      localCredential.setRealm(paramAnonymousParcel.readString());
      boolean bool;
      if (paramAnonymousParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      localCredential.setCheckAaaServerCertStatus(bool);
      localCredential.setUserCredential((Credential.UserCredential)paramAnonymousParcel.readParcelable(null));
      localCredential.setCertCredential((Credential.CertificateCredential)paramAnonymousParcel.readParcelable(null));
      localCredential.setSimCredential((Credential.SimCredential)paramAnonymousParcel.readParcelable(null));
      localCredential.setCaCertificate(ParcelUtil.readCertificate(paramAnonymousParcel));
      localCredential.setClientCertificateChain(ParcelUtil.readCertificates(paramAnonymousParcel));
      localCredential.setClientPrivateKey(ParcelUtil.readPrivateKey(paramAnonymousParcel));
      return localCredential;
    }
    
    public Credential[] newArray(int paramAnonymousInt)
    {
      return new Credential[paramAnonymousInt];
    }
  };
  private static final int MAX_REALM_BYTES = 253;
  private static final String TAG = "Credential";
  private X509Certificate mCaCertificate = null;
  private CertificateCredential mCertCredential = null;
  private boolean mCheckAaaServerCertStatus = false;
  private X509Certificate[] mClientCertificateChain = null;
  private PrivateKey mClientPrivateKey = null;
  private long mCreationTimeInMillis = Long.MIN_VALUE;
  private long mExpirationTimeInMillis = Long.MIN_VALUE;
  private String mRealm = null;
  private SimCredential mSimCredential = null;
  private UserCredential mUserCredential = null;
  
  public Credential() {}
  
  public Credential(Credential paramCredential)
  {
    if (paramCredential != null)
    {
      mCreationTimeInMillis = mCreationTimeInMillis;
      mExpirationTimeInMillis = mExpirationTimeInMillis;
      mRealm = mRealm;
      mCheckAaaServerCertStatus = mCheckAaaServerCertStatus;
      if (mUserCredential != null) {
        mUserCredential = new UserCredential(mUserCredential);
      }
      if (mCertCredential != null) {
        mCertCredential = new CertificateCredential(mCertCredential);
      }
      if (mSimCredential != null) {
        mSimCredential = new SimCredential(mSimCredential);
      }
      if (mClientCertificateChain != null) {
        mClientCertificateChain = ((X509Certificate[])Arrays.copyOf(mClientCertificateChain, mClientCertificateChain.length));
      }
      mCaCertificate = mCaCertificate;
      mClientPrivateKey = mClientPrivateKey;
    }
  }
  
  private static boolean isPrivateKeyEquals(PrivateKey paramPrivateKey1, PrivateKey paramPrivateKey2)
  {
    boolean bool = true;
    if ((paramPrivateKey1 == null) && (paramPrivateKey2 == null)) {
      return true;
    }
    if ((paramPrivateKey1 != null) && (paramPrivateKey2 != null))
    {
      if ((!TextUtils.equals(paramPrivateKey1.getAlgorithm(), paramPrivateKey2.getAlgorithm())) || (!Arrays.equals(paramPrivateKey1.getEncoded(), paramPrivateKey2.getEncoded()))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  private static boolean isX509CertificateEquals(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
  {
    if ((paramX509Certificate1 == null) && (paramX509Certificate2 == null)) {
      return true;
    }
    boolean bool1 = false;
    if ((paramX509Certificate1 != null) && (paramX509Certificate2 != null))
    {
      try
      {
        boolean bool2 = Arrays.equals(paramX509Certificate1.getEncoded(), paramX509Certificate2.getEncoded());
        bool1 = bool2;
      }
      catch (CertificateEncodingException paramX509Certificate1) {}
      return bool1;
    }
    return false;
  }
  
  private static boolean isX509CertificatesEquals(X509Certificate[] paramArrayOfX509Certificate1, X509Certificate[] paramArrayOfX509Certificate2)
  {
    if ((paramArrayOfX509Certificate1 == null) && (paramArrayOfX509Certificate2 == null)) {
      return true;
    }
    if ((paramArrayOfX509Certificate1 != null) && (paramArrayOfX509Certificate2 != null))
    {
      if (paramArrayOfX509Certificate1.length != paramArrayOfX509Certificate2.length) {
        return false;
      }
      for (int i = 0; i < paramArrayOfX509Certificate1.length; i++) {
        if (!isX509CertificateEquals(paramArrayOfX509Certificate1[i], paramArrayOfX509Certificate2[i])) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  private boolean verifyCertCredential()
  {
    if (mCertCredential == null)
    {
      Log.d("Credential", "Missing certificate credential");
      return false;
    }
    if ((mUserCredential == null) && (mSimCredential == null))
    {
      if (!mCertCredential.validate()) {
        return false;
      }
      if (mCaCertificate == null)
      {
        Log.d("Credential", "Missing CA Certificate for certificate credential");
        return false;
      }
      if (mClientPrivateKey == null)
      {
        Log.d("Credential", "Missing client private key for certificate credential");
        return false;
      }
      try
      {
        if (!verifySha256Fingerprint(mClientCertificateChain, mCertCredential.getCertSha256Fingerprint()))
        {
          Log.d("Credential", "SHA-256 fingerprint mismatch");
          return false;
        }
        return true;
      }
      catch (NoSuchAlgorithmException|CertificateEncodingException localNoSuchAlgorithmException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to verify SHA-256 fingerprint: ");
        localStringBuilder.append(localNoSuchAlgorithmException.getMessage());
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
    }
    Log.d("Credential", "Contained more than one type of credential");
    return false;
  }
  
  private static boolean verifySha256Fingerprint(X509Certificate[] paramArrayOfX509Certificate, byte[] paramArrayOfByte)
    throws NoSuchAlgorithmException, CertificateEncodingException
  {
    if (paramArrayOfX509Certificate == null) {
      return false;
    }
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
    int i = paramArrayOfX509Certificate.length;
    for (int j = 0; j < i; j++)
    {
      X509Certificate localX509Certificate = paramArrayOfX509Certificate[j];
      localMessageDigest.reset();
      if (Arrays.equals(paramArrayOfByte, localMessageDigest.digest(localX509Certificate.getEncoded()))) {
        return true;
      }
    }
    return false;
  }
  
  private boolean verifySimCredential()
  {
    if (mSimCredential == null)
    {
      Log.d("Credential", "Missing SIM credential");
      return false;
    }
    if ((mUserCredential == null) && (mCertCredential == null)) {
      return mSimCredential.validate();
    }
    Log.d("Credential", "Contained more than one type of credential");
    return false;
  }
  
  private boolean verifyUserCredential()
  {
    if (mUserCredential == null)
    {
      Log.d("Credential", "Missing user credential");
      return false;
    }
    if ((mCertCredential == null) && (mSimCredential == null))
    {
      if (!mUserCredential.validate()) {
        return false;
      }
      if (mCaCertificate == null)
      {
        Log.d("Credential", "Missing CA Certificate for user credential");
        return false;
      }
      return true;
    }
    Log.d("Credential", "Contained more than one type of credential");
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof Credential)) {
      return false;
    }
    paramObject = (Credential)paramObject;
    if ((!TextUtils.equals(mRealm, mRealm)) || (mCreationTimeInMillis != mCreationTimeInMillis) || (mExpirationTimeInMillis != mExpirationTimeInMillis) || (mCheckAaaServerCertStatus != mCheckAaaServerCertStatus) || (mUserCredential == null ? mUserCredential != null : !mUserCredential.equals(mUserCredential)) || (mCertCredential == null ? mCertCredential != null : !mCertCredential.equals(mCertCredential)) || (mSimCredential == null ? mSimCredential != null : !mSimCredential.equals(mSimCredential)) || (!isX509CertificateEquals(mCaCertificate, mCaCertificate)) || (!isX509CertificatesEquals(mClientCertificateChain, mClientCertificateChain)) || (!isPrivateKeyEquals(mClientPrivateKey, mClientPrivateKey))) {
      bool = false;
    }
    return bool;
  }
  
  public X509Certificate getCaCertificate()
  {
    return mCaCertificate;
  }
  
  public CertificateCredential getCertCredential()
  {
    return mCertCredential;
  }
  
  public boolean getCheckAaaServerCertStatus()
  {
    return mCheckAaaServerCertStatus;
  }
  
  public X509Certificate[] getClientCertificateChain()
  {
    return mClientCertificateChain;
  }
  
  public PrivateKey getClientPrivateKey()
  {
    return mClientPrivateKey;
  }
  
  public long getCreationTimeInMillis()
  {
    return mCreationTimeInMillis;
  }
  
  public long getExpirationTimeInMillis()
  {
    return mExpirationTimeInMillis;
  }
  
  public String getRealm()
  {
    return mRealm;
  }
  
  public SimCredential getSimCredential()
  {
    return mSimCredential;
  }
  
  public UserCredential getUserCredential()
  {
    return mUserCredential;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mRealm, Long.valueOf(mCreationTimeInMillis), Long.valueOf(mExpirationTimeInMillis), Boolean.valueOf(mCheckAaaServerCertStatus), mUserCredential, mCertCredential, mSimCredential, mCaCertificate, mClientCertificateChain, mClientPrivateKey });
  }
  
  public void setCaCertificate(X509Certificate paramX509Certificate)
  {
    mCaCertificate = paramX509Certificate;
  }
  
  public void setCertCredential(CertificateCredential paramCertificateCredential)
  {
    mCertCredential = paramCertificateCredential;
  }
  
  public void setCheckAaaServerCertStatus(boolean paramBoolean)
  {
    mCheckAaaServerCertStatus = paramBoolean;
  }
  
  public void setClientCertificateChain(X509Certificate[] paramArrayOfX509Certificate)
  {
    mClientCertificateChain = paramArrayOfX509Certificate;
  }
  
  public void setClientPrivateKey(PrivateKey paramPrivateKey)
  {
    mClientPrivateKey = paramPrivateKey;
  }
  
  public void setCreationTimeInMillis(long paramLong)
  {
    mCreationTimeInMillis = paramLong;
  }
  
  public void setExpirationTimeInMillis(long paramLong)
  {
    mExpirationTimeInMillis = paramLong;
  }
  
  public void setRealm(String paramString)
  {
    mRealm = paramString;
  }
  
  public void setSimCredential(SimCredential paramSimCredential)
  {
    mSimCredential = paramSimCredential;
  }
  
  public void setUserCredential(UserCredential paramUserCredential)
  {
    mUserCredential = paramUserCredential;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Realm: ");
    localStringBuilder.append(mRealm);
    localStringBuilder.append("\n");
    localStringBuilder.append("CreationTime: ");
    Object localObject;
    if (mCreationTimeInMillis != Long.MIN_VALUE) {
      localObject = new Date(mCreationTimeInMillis);
    } else {
      localObject = "Not specified";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("ExpirationTime: ");
    if (mExpirationTimeInMillis != Long.MIN_VALUE) {
      localObject = new Date(mExpirationTimeInMillis);
    } else {
      localObject = "Not specified";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("CheckAAAServerStatus: ");
    localStringBuilder.append(mCheckAaaServerCertStatus);
    localStringBuilder.append("\n");
    if (mUserCredential != null)
    {
      localStringBuilder.append("UserCredential Begin ---\n");
      localStringBuilder.append(mUserCredential);
      localStringBuilder.append("UserCredential End ---\n");
    }
    if (mCertCredential != null)
    {
      localStringBuilder.append("CertificateCredential Begin ---\n");
      localStringBuilder.append(mCertCredential);
      localStringBuilder.append("CertificateCredential End ---\n");
    }
    if (mSimCredential != null)
    {
      localStringBuilder.append("SIMCredential Begin ---\n");
      localStringBuilder.append(mSimCredential);
      localStringBuilder.append("SIMCredential End ---\n");
    }
    return localStringBuilder.toString();
  }
  
  public boolean validate()
  {
    if (TextUtils.isEmpty(mRealm))
    {
      Log.d("Credential", "Missing realm");
      return false;
    }
    if (mRealm.getBytes(StandardCharsets.UTF_8).length > 253)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("realm exceeding maximum length: ");
      localStringBuilder.append(mRealm.getBytes(StandardCharsets.UTF_8).length);
      Log.d("Credential", localStringBuilder.toString());
      return false;
    }
    if (mUserCredential != null)
    {
      if (!verifyUserCredential()) {
        return false;
      }
    }
    else if (mCertCredential != null)
    {
      if (!verifyCertCredential()) {
        return false;
      }
    }
    else
    {
      if (mSimCredential == null) {
        break label132;
      }
      if (!verifySimCredential()) {
        return false;
      }
    }
    return true;
    label132:
    Log.d("Credential", "Missing required credential");
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mCreationTimeInMillis);
    paramParcel.writeLong(mExpirationTimeInMillis);
    paramParcel.writeString(mRealm);
    paramParcel.writeInt(mCheckAaaServerCertStatus);
    paramParcel.writeParcelable(mUserCredential, paramInt);
    paramParcel.writeParcelable(mCertCredential, paramInt);
    paramParcel.writeParcelable(mSimCredential, paramInt);
    ParcelUtil.writeCertificate(paramParcel, mCaCertificate);
    ParcelUtil.writeCertificates(paramParcel, mClientCertificateChain);
    ParcelUtil.writePrivateKey(paramParcel, mClientPrivateKey);
  }
  
  public static final class CertificateCredential
    implements Parcelable
  {
    private static final int CERT_SHA256_FINGER_PRINT_LENGTH = 32;
    public static final String CERT_TYPE_X509V3 = "x509v3";
    public static final Parcelable.Creator<CertificateCredential> CREATOR = new Parcelable.Creator()
    {
      public Credential.CertificateCredential createFromParcel(Parcel paramAnonymousParcel)
      {
        Credential.CertificateCredential localCertificateCredential = new Credential.CertificateCredential();
        localCertificateCredential.setCertType(paramAnonymousParcel.readString());
        localCertificateCredential.setCertSha256Fingerprint(paramAnonymousParcel.createByteArray());
        return localCertificateCredential;
      }
      
      public Credential.CertificateCredential[] newArray(int paramAnonymousInt)
      {
        return new Credential.CertificateCredential[paramAnonymousInt];
      }
    };
    private byte[] mCertSha256Fingerprint = null;
    private String mCertType = null;
    
    public CertificateCredential() {}
    
    public CertificateCredential(CertificateCredential paramCertificateCredential)
    {
      if (paramCertificateCredential != null)
      {
        mCertType = mCertType;
        if (mCertSha256Fingerprint != null) {
          mCertSha256Fingerprint = Arrays.copyOf(mCertSha256Fingerprint, mCertSha256Fingerprint.length);
        }
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof CertificateCredential)) {
        return false;
      }
      paramObject = (CertificateCredential)paramObject;
      if ((!TextUtils.equals(mCertType, mCertType)) || (!Arrays.equals(mCertSha256Fingerprint, mCertSha256Fingerprint))) {
        bool = false;
      }
      return bool;
    }
    
    public byte[] getCertSha256Fingerprint()
    {
      return mCertSha256Fingerprint;
    }
    
    public String getCertType()
    {
      return mCertType;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mCertType, mCertSha256Fingerprint });
    }
    
    public void setCertSha256Fingerprint(byte[] paramArrayOfByte)
    {
      mCertSha256Fingerprint = paramArrayOfByte;
    }
    
    public void setCertType(String paramString)
    {
      mCertType = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CertificateType: ");
      localStringBuilder.append(mCertType);
      localStringBuilder.append("\n");
      return localStringBuilder.toString();
    }
    
    public boolean validate()
    {
      if (!TextUtils.equals("x509v3", mCertType))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported certificate type: ");
        localStringBuilder.append(mCertType);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      if ((mCertSha256Fingerprint != null) && (mCertSha256Fingerprint.length == 32)) {
        return true;
      }
      Log.d("Credential", "Invalid SHA-256 fingerprint");
      return false;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mCertType);
      paramParcel.writeByteArray(mCertSha256Fingerprint);
    }
  }
  
  public static final class SimCredential
    implements Parcelable
  {
    public static final Parcelable.Creator<SimCredential> CREATOR = new Parcelable.Creator()
    {
      public Credential.SimCredential createFromParcel(Parcel paramAnonymousParcel)
      {
        Credential.SimCredential localSimCredential = new Credential.SimCredential();
        localSimCredential.setImsi(paramAnonymousParcel.readString());
        localSimCredential.setEapType(paramAnonymousParcel.readInt());
        return localSimCredential;
      }
      
      public Credential.SimCredential[] newArray(int paramAnonymousInt)
      {
        return new Credential.SimCredential[paramAnonymousInt];
      }
    };
    private static final int MAX_IMSI_LENGTH = 15;
    private int mEapType = Integer.MIN_VALUE;
    private String mImsi = null;
    
    public SimCredential() {}
    
    public SimCredential(SimCredential paramSimCredential)
    {
      if (paramSimCredential != null)
      {
        mImsi = mImsi;
        mEapType = mEapType;
      }
    }
    
    private boolean verifyImsi()
    {
      if (TextUtils.isEmpty(mImsi))
      {
        Log.d("Credential", "Missing IMSI");
        return false;
      }
      if (mImsi.length() > 15)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("IMSI exceeding maximum length: ");
        localStringBuilder.append(mImsi.length());
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      int i = 0;
      int k;
      for (int j = 0;; j++)
      {
        k = i;
        if (j >= mImsi.length()) {
          break;
        }
        i = mImsi.charAt(j);
        k = i;
        if (i < 48) {
          break;
        }
        if (i > 57)
        {
          k = i;
          break;
        }
      }
      if (j == mImsi.length()) {
        return true;
      }
      return (j == mImsi.length() - 1) && (k == 42);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof SimCredential)) {
        return false;
      }
      paramObject = (SimCredential)paramObject;
      if ((!TextUtils.equals(mImsi, mImsi)) || (mEapType != mEapType)) {
        bool = false;
      }
      return bool;
    }
    
    public int getEapType()
    {
      return mEapType;
    }
    
    public String getImsi()
    {
      return mImsi;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mImsi, Integer.valueOf(mEapType) });
    }
    
    public void setEapType(int paramInt)
    {
      mEapType = paramInt;
    }
    
    public void setImsi(String paramString)
    {
      mImsi = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("IMSI: ");
      localStringBuilder.append(mImsi);
      localStringBuilder.append("\n");
      localStringBuilder.append("EAPType: ");
      localStringBuilder.append(mEapType);
      localStringBuilder.append("\n");
      return localStringBuilder.toString();
    }
    
    public boolean validate()
    {
      if (!verifyImsi()) {
        return false;
      }
      if ((mEapType != 18) && (mEapType != 23) && (mEapType != 50))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid EAP Type for SIM credential: ");
        localStringBuilder.append(mEapType);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      return true;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mImsi);
      paramParcel.writeInt(mEapType);
    }
  }
  
  public static final class UserCredential
    implements Parcelable
  {
    public static final String AUTH_METHOD_MSCHAP = "MS-CHAP";
    public static final String AUTH_METHOD_MSCHAPV2 = "MS-CHAP-V2";
    public static final String AUTH_METHOD_PAP = "PAP";
    public static final Parcelable.Creator<UserCredential> CREATOR = new Parcelable.Creator()
    {
      public Credential.UserCredential createFromParcel(Parcel paramAnonymousParcel)
      {
        Credential.UserCredential localUserCredential = new Credential.UserCredential();
        localUserCredential.setUsername(paramAnonymousParcel.readString());
        localUserCredential.setPassword(paramAnonymousParcel.readString());
        int i = paramAnonymousParcel.readInt();
        boolean bool1 = false;
        if (i != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        localUserCredential.setMachineManaged(bool2);
        localUserCredential.setSoftTokenApp(paramAnonymousParcel.readString());
        boolean bool2 = bool1;
        if (paramAnonymousParcel.readInt() != 0) {
          bool2 = true;
        }
        localUserCredential.setAbleToShare(bool2);
        localUserCredential.setEapType(paramAnonymousParcel.readInt());
        localUserCredential.setNonEapInnerMethod(paramAnonymousParcel.readString());
        return localUserCredential;
      }
      
      public Credential.UserCredential[] newArray(int paramAnonymousInt)
      {
        return new Credential.UserCredential[paramAnonymousInt];
      }
    };
    private static final int MAX_PASSWORD_BYTES = 255;
    private static final int MAX_USERNAME_BYTES = 63;
    private static final Set<String> SUPPORTED_AUTH = new HashSet(Arrays.asList(new String[] { "PAP", "MS-CHAP", "MS-CHAP-V2" }));
    private boolean mAbleToShare = false;
    private int mEapType = Integer.MIN_VALUE;
    private boolean mMachineManaged = false;
    private String mNonEapInnerMethod = null;
    private String mPassword = null;
    private String mSoftTokenApp = null;
    private String mUsername = null;
    
    public UserCredential() {}
    
    public UserCredential(UserCredential paramUserCredential)
    {
      if (paramUserCredential != null)
      {
        mUsername = mUsername;
        mPassword = mPassword;
        mMachineManaged = mMachineManaged;
        mSoftTokenApp = mSoftTokenApp;
        mAbleToShare = mAbleToShare;
        mEapType = mEapType;
        mNonEapInnerMethod = mNonEapInnerMethod;
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof UserCredential)) {
        return false;
      }
      paramObject = (UserCredential)paramObject;
      if ((!TextUtils.equals(mUsername, mUsername)) || (!TextUtils.equals(mPassword, mPassword)) || (mMachineManaged != mMachineManaged) || (!TextUtils.equals(mSoftTokenApp, mSoftTokenApp)) || (mAbleToShare != mAbleToShare) || (mEapType != mEapType) || (!TextUtils.equals(mNonEapInnerMethod, mNonEapInnerMethod))) {
        bool = false;
      }
      return bool;
    }
    
    public boolean getAbleToShare()
    {
      return mAbleToShare;
    }
    
    public int getEapType()
    {
      return mEapType;
    }
    
    public boolean getMachineManaged()
    {
      return mMachineManaged;
    }
    
    public String getNonEapInnerMethod()
    {
      return mNonEapInnerMethod;
    }
    
    public String getPassword()
    {
      return mPassword;
    }
    
    public String getSoftTokenApp()
    {
      return mSoftTokenApp;
    }
    
    public String getUsername()
    {
      return mUsername;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mUsername, mPassword, Boolean.valueOf(mMachineManaged), mSoftTokenApp, Boolean.valueOf(mAbleToShare), Integer.valueOf(mEapType), mNonEapInnerMethod });
    }
    
    public void setAbleToShare(boolean paramBoolean)
    {
      mAbleToShare = paramBoolean;
    }
    
    public void setEapType(int paramInt)
    {
      mEapType = paramInt;
    }
    
    public void setMachineManaged(boolean paramBoolean)
    {
      mMachineManaged = paramBoolean;
    }
    
    public void setNonEapInnerMethod(String paramString)
    {
      mNonEapInnerMethod = paramString;
    }
    
    public void setPassword(String paramString)
    {
      mPassword = paramString;
    }
    
    public void setSoftTokenApp(String paramString)
    {
      mSoftTokenApp = paramString;
    }
    
    public void setUsername(String paramString)
    {
      mUsername = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Username: ");
      localStringBuilder.append(mUsername);
      localStringBuilder.append("\n");
      localStringBuilder.append("MachineManaged: ");
      localStringBuilder.append(mMachineManaged);
      localStringBuilder.append("\n");
      localStringBuilder.append("SoftTokenApp: ");
      localStringBuilder.append(mSoftTokenApp);
      localStringBuilder.append("\n");
      localStringBuilder.append("AbleToShare: ");
      localStringBuilder.append(mAbleToShare);
      localStringBuilder.append("\n");
      localStringBuilder.append("EAPType: ");
      localStringBuilder.append(mEapType);
      localStringBuilder.append("\n");
      localStringBuilder.append("AuthMethod: ");
      localStringBuilder.append(mNonEapInnerMethod);
      localStringBuilder.append("\n");
      return localStringBuilder.toString();
    }
    
    public boolean validate()
    {
      if (TextUtils.isEmpty(mUsername))
      {
        Log.d("Credential", "Missing username");
        return false;
      }
      StringBuilder localStringBuilder;
      if (mUsername.getBytes(StandardCharsets.UTF_8).length > 63)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("username exceeding maximum length: ");
        localStringBuilder.append(mUsername.getBytes(StandardCharsets.UTF_8).length);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      if (TextUtils.isEmpty(mPassword))
      {
        Log.d("Credential", "Missing password");
        return false;
      }
      if (mPassword.getBytes(StandardCharsets.UTF_8).length > 255)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("password exceeding maximum length: ");
        localStringBuilder.append(mPassword.getBytes(StandardCharsets.UTF_8).length);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      if (mEapType != 21)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid EAP Type for user credential: ");
        localStringBuilder.append(mEapType);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      if (!SUPPORTED_AUTH.contains(mNonEapInnerMethod))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid non-EAP inner method for EAP-TTLS: ");
        localStringBuilder.append(mNonEapInnerMethod);
        Log.d("Credential", localStringBuilder.toString());
        return false;
      }
      return true;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mUsername);
      paramParcel.writeString(mPassword);
      paramParcel.writeInt(mMachineManaged);
      paramParcel.writeString(mSoftTokenApp);
      paramParcel.writeInt(mAbleToShare);
      paramParcel.writeInt(mEapType);
      paramParcel.writeString(mNonEapInnerMethod);
    }
  }
}
