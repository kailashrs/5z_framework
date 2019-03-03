package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class WifiEnterpriseConfig
  implements Parcelable
{
  public static final String ALTSUBJECT_MATCH_KEY = "altsubject_match";
  public static final String ANON_IDENTITY_KEY = "anonymous_identity";
  public static final String CA_CERT_ALIAS_DELIMITER = " ";
  public static final String CA_CERT_KEY = "ca_cert";
  public static final String CA_CERT_PREFIX = "keystore://CACERT_";
  public static final String CA_PATH_KEY = "ca_path";
  public static final String CLIENT_CERT_KEY = "client_cert";
  public static final String CLIENT_CERT_PREFIX = "keystore://USRCERT_";
  public static final Parcelable.Creator<WifiEnterpriseConfig> CREATOR = new Parcelable.Creator()
  {
    public WifiEnterpriseConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiEnterpriseConfig localWifiEnterpriseConfig = new WifiEnterpriseConfig();
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++)
      {
        String str1 = paramAnonymousParcel.readString();
        String str2 = paramAnonymousParcel.readString();
        mFields.put(str1, str2);
      }
      WifiEnterpriseConfig.access$102(localWifiEnterpriseConfig, paramAnonymousParcel.readInt());
      WifiEnterpriseConfig.access$202(localWifiEnterpriseConfig, paramAnonymousParcel.readInt());
      WifiEnterpriseConfig.access$302(localWifiEnterpriseConfig, ParcelUtil.readCertificates(paramAnonymousParcel));
      WifiEnterpriseConfig.access$402(localWifiEnterpriseConfig, ParcelUtil.readPrivateKey(paramAnonymousParcel));
      WifiEnterpriseConfig.access$502(localWifiEnterpriseConfig, ParcelUtil.readCertificates(paramAnonymousParcel));
      return localWifiEnterpriseConfig;
    }
    
    public WifiEnterpriseConfig[] newArray(int paramAnonymousInt)
    {
      return new WifiEnterpriseConfig[paramAnonymousInt];
    }
  };
  public static final String DOM_SUFFIX_MATCH_KEY = "domain_suffix_match";
  public static final String EAP_ERP = "eap_erp";
  public static final String EAP_KEY = "eap";
  public static final String EMPTY_VALUE = "NULL";
  public static final String ENGINE_DISABLE = "0";
  public static final String ENGINE_ENABLE = "1";
  public static final String ENGINE_ID_KEY = "engine_id";
  public static final String ENGINE_ID_KEYSTORE = "keystore";
  public static final String ENGINE_KEY = "engine";
  public static final String IDENTITY_KEY = "identity";
  public static final String KEYSTORES_URI = "keystores://";
  public static final String KEYSTORE_URI = "keystore://";
  public static final String KEY_SIMNUM = "sim_num";
  public static final String OPP_KEY_CACHING = "proactive_key_caching";
  public static final String PASSWORD_KEY = "password";
  public static final String PHASE2_KEY = "phase2";
  public static final String PLMN_KEY = "plmn";
  public static final String PRIVATE_KEY_ID_KEY = "key_id";
  public static final String REALM_KEY = "realm";
  public static final String SUBJECT_MATCH_KEY = "subject_match";
  private static final String[] SUPPLICANT_CONFIG_KEYS = { "identity", "anonymous_identity", "password", "client_cert", "ca_cert", "subject_match", "engine", "engine_id", "key_id", "altsubject_match", "domain_suffix_match", "ca_path" };
  private static final String TAG = "WifiEnterpriseConfig";
  private static final List<String> UNQUOTED_KEYS = Arrays.asList(new String[] { "engine", "proactive_key_caching", "eap_erp" });
  private X509Certificate[] mCaCerts;
  private X509Certificate[] mClientCertificateChain;
  private PrivateKey mClientPrivateKey;
  private int mEapMethod = -1;
  private HashMap<String, String> mFields = new HashMap();
  private int mPhase2Method = 0;
  
  public WifiEnterpriseConfig() {}
  
  public WifiEnterpriseConfig(WifiEnterpriseConfig paramWifiEnterpriseConfig)
  {
    copyFrom(paramWifiEnterpriseConfig, false, "");
  }
  
  private String convertToQuotedString(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("\"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\"");
    return localStringBuilder.toString();
  }
  
  private void copyFrom(WifiEnterpriseConfig paramWifiEnterpriseConfig, boolean paramBoolean, String paramString)
  {
    Iterator localIterator = mFields.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if ((!paramBoolean) || (!str.equals("password")) || (!TextUtils.equals((CharSequence)mFields.get(str), paramString))) {
        mFields.put(str, (String)mFields.get(str));
      }
    }
    if (mCaCerts != null) {
      mCaCerts = ((X509Certificate[])Arrays.copyOf(mCaCerts, mCaCerts.length));
    } else {
      mCaCerts = null;
    }
    mClientPrivateKey = mClientPrivateKey;
    if (mClientCertificateChain != null) {
      mClientCertificateChain = ((X509Certificate[])Arrays.copyOf(mClientCertificateChain, mClientCertificateChain.length));
    } else {
      mClientCertificateChain = null;
    }
    mEapMethod = mEapMethod;
    mPhase2Method = mPhase2Method;
  }
  
  public static String decodeCaCertificateAlias(String paramString)
  {
    Object localObject = new byte[paramString.length() >> 1];
    int i = 0;
    for (int j = 0; i < paramString.length(); j++)
    {
      localObject[j] = ((byte)(byte)Integer.parseInt(paramString.substring(i, i + 2), 16));
      i += 2;
    }
    try
    {
      localObject = new String((byte[])localObject, StandardCharsets.UTF_8);
      return localObject;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      localNumberFormatException.printStackTrace();
    }
    return paramString;
  }
  
  public static String encodeCaCertificateAlias(String paramString)
  {
    paramString = paramString.getBytes(StandardCharsets.UTF_8);
    StringBuilder localStringBuilder = new StringBuilder(paramString.length * 2);
    int i = paramString.length;
    for (int j = 0; j < i; j++) {
      localStringBuilder.append(String.format("%02x", new Object[] { Integer.valueOf(paramString[j] & 0xFF) }));
    }
    return localStringBuilder.toString();
  }
  
  private String getFieldValue(String paramString1, String paramString2)
  {
    paramString1 = (String)mFields.get(paramString1);
    if ((!TextUtils.isEmpty(paramString1)) && (!"NULL".equals(paramString1)))
    {
      paramString1 = removeDoubleQuotes(paramString1);
      if (paramString1.startsWith(paramString2)) {
        return paramString1.substring(paramString2.length());
      }
      return paramString1;
    }
    return "";
  }
  
  private int getStringIndex(String[] paramArrayOfString, String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramInt;
    }
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (paramString.equals(paramArrayOfString[i])) {
        return i;
      }
    }
    return paramInt;
  }
  
  private boolean isEapMethodValid()
  {
    if (mEapMethod == -1)
    {
      Log.e("WifiEnterpriseConfig", "WiFi enterprise configuration is invalid as it supplies no EAP method.");
      return false;
    }
    if ((mEapMethod >= 0) && (mEapMethod < Eap.strings.length))
    {
      if ((mPhase2Method >= 0) && (mPhase2Method < Phase2.strings.length)) {
        return true;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("mPhase2Method is invald for WiFi enterprise configuration: ");
      localStringBuilder.append(mPhase2Method);
      Log.e("WifiEnterpriseConfig", localStringBuilder.toString());
      return false;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mEapMethod is invald for WiFi enterprise configuration: ");
    localStringBuilder.append(mEapMethod);
    Log.e("WifiEnterpriseConfig", localStringBuilder.toString());
    return false;
  }
  
  private String removeDoubleQuotes(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return "";
    }
    int i = paramString.length();
    if ((i > 1) && (paramString.charAt(0) == '"') && (paramString.charAt(i - 1) == '"')) {
      return paramString.substring(1, i - 1);
    }
    return paramString;
  }
  
  private void setFieldValue(String paramString1, String paramString2, String paramString3)
  {
    if (TextUtils.isEmpty(paramString2))
    {
      mFields.put(paramString1, "NULL");
    }
    else
    {
      StringBuilder localStringBuilder;
      if (!UNQUOTED_KEYS.contains(paramString1))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString3);
        localStringBuilder.append(paramString2);
        paramString2 = convertToQuotedString(localStringBuilder.toString());
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString3);
        localStringBuilder.append(paramString2);
        paramString2 = localStringBuilder.toString();
      }
      mFields.put(paramString1, paramString2);
    }
  }
  
  public void copyFromExternal(WifiEnterpriseConfig paramWifiEnterpriseConfig, String paramString)
  {
    copyFrom(paramWifiEnterpriseConfig, true, convertToQuotedString(paramString));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getAltSubjectMatch()
  {
    return getFieldValue("altsubject_match");
  }
  
  public String getAnonymousIdentity()
  {
    return getFieldValue("anonymous_identity");
  }
  
  public X509Certificate getCaCertificate()
  {
    if ((mCaCerts != null) && (mCaCerts.length > 0)) {
      return mCaCerts[0];
    }
    return null;
  }
  
  public String getCaCertificateAlias()
  {
    return getFieldValue("ca_cert", "keystore://CACERT_");
  }
  
  public String[] getCaCertificateAliases()
  {
    String str = getFieldValue("ca_cert");
    boolean bool = str.startsWith("keystore://CACERT_");
    int i = 0;
    if (bool) {
      return new String[] { getFieldValue("ca_cert", "keystore://CACERT_") };
    }
    bool = str.startsWith("keystores://");
    String[] arrayOfString1 = null;
    String[] arrayOfString2 = null;
    if (bool)
    {
      arrayOfString1 = TextUtils.split(str.substring("keystores://".length()), " ");
      while (i < arrayOfString1.length)
      {
        arrayOfString1[i] = decodeCaCertificateAlias(arrayOfString1[i]);
        if (arrayOfString1[i].startsWith("CACERT_")) {
          arrayOfString1[i] = arrayOfString1[i].substring("CACERT_".length());
        }
        i++;
      }
      if (arrayOfString1.length != 0) {
        arrayOfString2 = arrayOfString1;
      }
      return arrayOfString2;
    }
    if (TextUtils.isEmpty(str))
    {
      arrayOfString2 = arrayOfString1;
    }
    else
    {
      arrayOfString2 = new String[1];
      arrayOfString2[0] = str;
    }
    return arrayOfString2;
  }
  
  public X509Certificate[] getCaCertificates()
  {
    if ((mCaCerts != null) && (mCaCerts.length > 0)) {
      return mCaCerts;
    }
    return null;
  }
  
  public String getCaPath()
  {
    return getFieldValue("ca_path");
  }
  
  public X509Certificate getClientCertificate()
  {
    if ((mClientCertificateChain != null) && (mClientCertificateChain.length > 0)) {
      return mClientCertificateChain[0];
    }
    return null;
  }
  
  public String getClientCertificateAlias()
  {
    return getFieldValue("client_cert", "keystore://USRCERT_");
  }
  
  public X509Certificate[] getClientCertificateChain()
  {
    if ((mClientCertificateChain != null) && (mClientCertificateChain.length > 0)) {
      return mClientCertificateChain;
    }
    return null;
  }
  
  public PrivateKey getClientPrivateKey()
  {
    return mClientPrivateKey;
  }
  
  public String getDomainSuffixMatch()
  {
    return getFieldValue("domain_suffix_match");
  }
  
  public int getEapMethod()
  {
    return mEapMethod;
  }
  
  public String getFieldValue(String paramString)
  {
    return getFieldValue(paramString, "");
  }
  
  public String getIdentity()
  {
    return getFieldValue("identity");
  }
  
  public String getKeyId(WifiEnterpriseConfig paramWifiEnterpriseConfig)
  {
    if (mEapMethod == -1)
    {
      if (paramWifiEnterpriseConfig != null) {
        paramWifiEnterpriseConfig = paramWifiEnterpriseConfig.getKeyId(null);
      } else {
        paramWifiEnterpriseConfig = "NULL";
      }
      return paramWifiEnterpriseConfig;
    }
    if (!isEapMethodValid()) {
      return "NULL";
    }
    paramWifiEnterpriseConfig = new StringBuilder();
    paramWifiEnterpriseConfig.append(Eap.strings[mEapMethod]);
    paramWifiEnterpriseConfig.append("_");
    paramWifiEnterpriseConfig.append(Phase2.strings[mPhase2Method]);
    return paramWifiEnterpriseConfig.toString();
  }
  
  public String getPassword()
  {
    return getFieldValue("password");
  }
  
  public int getPhase2Method()
  {
    return mPhase2Method;
  }
  
  public String getPlmn()
  {
    return getFieldValue("plmn");
  }
  
  public String getRealm()
  {
    return getFieldValue("realm");
  }
  
  public String getSimNum()
  {
    return getFieldValue("sim_num");
  }
  
  public String getSubjectMatch()
  {
    return getFieldValue("subject_match");
  }
  
  public void loadFromSupplicant(SupplicantLoader paramSupplicantLoader)
  {
    for (String str1 : SUPPLICANT_CONFIG_KEYS)
    {
      String str2 = paramSupplicantLoader.loadValue(str1);
      if (str2 == null) {
        mFields.put(str1, "NULL");
      } else {
        mFields.put(str1, str2);
      }
    }
    ??? = paramSupplicantLoader.loadValue("eap");
    mEapMethod = getStringIndex(Eap.strings, (String)???, -1);
    ??? = removeDoubleQuotes(paramSupplicantLoader.loadValue("phase2"));
    if (((String)???).startsWith("auth="))
    {
      paramSupplicantLoader = ((String)???).substring("auth=".length());
    }
    else
    {
      paramSupplicantLoader = (SupplicantLoader)???;
      if (((String)???).startsWith("autheap=")) {
        paramSupplicantLoader = ((String)???).substring("autheap=".length());
      }
    }
    mPhase2Method = getStringIndex(Phase2.strings, paramSupplicantLoader, 0);
  }
  
  public void resetCaCertificate()
  {
    mCaCerts = null;
  }
  
  public void resetClientKeyEntry()
  {
    mClientPrivateKey = null;
    mClientCertificateChain = null;
  }
  
  public boolean saveToSupplicant(SupplicantSaver paramSupplicantSaver)
  {
    boolean bool = isEapMethodValid();
    int i = 0;
    if (!bool) {
      return false;
    }
    int j;
    if ((mEapMethod != 4) && (mEapMethod != 5) && (mEapMethod != 6)) {
      j = 0;
    } else {
      j = 1;
    }
    Object localObject1 = mFields.keySet().iterator();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (String)((Iterator)localObject1).next();
      if ((j == 0) || (!"anonymous_identity".equals(localObject2))) {
        if (!paramSupplicantSaver.saveValue((String)localObject2, (String)mFields.get(localObject2))) {
          return false;
        }
      }
    }
    if (!paramSupplicantSaver.saveValue("eap", Eap.strings[mEapMethod])) {
      return false;
    }
    if ((mEapMethod != 1) && (mPhase2Method != 0))
    {
      j = i;
      if (mEapMethod == 2)
      {
        j = i;
        if (mPhase2Method == 4) {
          j = 1;
        }
      }
      if (j != 0) {
        localObject1 = "autheap=";
      } else {
        localObject1 = "auth=";
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(Phase2.strings[mPhase2Method]);
      return paramSupplicantSaver.saveValue("phase2", convertToQuotedString(((StringBuilder)localObject2).toString()));
    }
    if (mPhase2Method == 0) {
      return paramSupplicantSaver.saveValue("phase2", null);
    }
    Log.e("WifiEnterpriseConfig", "WiFi enterprise configuration is invalid as it supplies a phase 2 method but the phase1 method does not support it.");
    return false;
  }
  
  public void setAltSubjectMatch(String paramString)
  {
    setFieldValue("altsubject_match", paramString);
  }
  
  public void setAnonymousIdentity(String paramString)
  {
    setFieldValue("anonymous_identity", paramString);
  }
  
  public void setCaCertificate(X509Certificate paramX509Certificate)
  {
    if (paramX509Certificate != null)
    {
      if (paramX509Certificate.getBasicConstraints() >= 0) {
        mCaCerts = new X509Certificate[] { paramX509Certificate };
      } else {
        throw new IllegalArgumentException("Not a CA certificate");
      }
    }
    else {
      mCaCerts = null;
    }
  }
  
  public void setCaCertificateAlias(String paramString)
  {
    setFieldValue("ca_cert", paramString, "keystore://CACERT_");
  }
  
  public void setCaCertificateAliases(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null)
    {
      setFieldValue("ca_cert", null, "keystore://CACERT_");
    }
    else
    {
      int i = paramArrayOfString.length;
      int j = 0;
      if (i == 1)
      {
        setCaCertificateAlias(paramArrayOfString[0]);
      }
      else
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        while (j < paramArrayOfString.length)
        {
          if (j > 0) {
            localStringBuilder1.append(" ");
          }
          StringBuilder localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append("CACERT_");
          localStringBuilder2.append(paramArrayOfString[j]);
          localStringBuilder1.append(encodeCaCertificateAlias(localStringBuilder2.toString()));
          j++;
        }
        setFieldValue("ca_cert", localStringBuilder1.toString(), "keystores://");
      }
    }
  }
  
  public void setCaCertificates(X509Certificate[] paramArrayOfX509Certificate)
  {
    if (paramArrayOfX509Certificate != null)
    {
      X509Certificate[] arrayOfX509Certificate = new X509Certificate[paramArrayOfX509Certificate.length];
      int i = 0;
      while (i < paramArrayOfX509Certificate.length) {
        if (paramArrayOfX509Certificate[i].getBasicConstraints() >= 0)
        {
          arrayOfX509Certificate[i] = paramArrayOfX509Certificate[i];
          i++;
        }
        else
        {
          throw new IllegalArgumentException("Not a CA certificate");
        }
      }
      mCaCerts = arrayOfX509Certificate;
    }
    else
    {
      mCaCerts = null;
    }
  }
  
  public void setCaPath(String paramString)
  {
    setFieldValue("ca_path", paramString);
  }
  
  public void setClientCertificateAlias(String paramString)
  {
    setFieldValue("client_cert", paramString, "keystore://USRCERT_");
    setFieldValue("key_id", paramString, "USRPKEY_");
    if (TextUtils.isEmpty(paramString))
    {
      setFieldValue("engine", "0");
      setFieldValue("engine_id", "");
    }
    else
    {
      setFieldValue("engine", "1");
      setFieldValue("engine_id", "keystore");
    }
  }
  
  public void setClientKeyEntry(PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
  {
    X509Certificate[] arrayOfX509Certificate = null;
    if (paramX509Certificate != null) {
      arrayOfX509Certificate = new X509Certificate[] { paramX509Certificate };
    }
    setClientKeyEntryWithCertificateChain(paramPrivateKey, arrayOfX509Certificate);
  }
  
  public void setClientKeyEntryWithCertificateChain(PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (paramArrayOfX509Certificate != null)
    {
      localObject2 = localObject1;
      if (paramArrayOfX509Certificate.length > 0) {
        if (paramArrayOfX509Certificate[0].getBasicConstraints() == -1)
        {
          int i = 1;
          while (i < paramArrayOfX509Certificate.length) {
            if (paramArrayOfX509Certificate[i].getBasicConstraints() != -1) {
              i++;
            } else {
              throw new IllegalArgumentException("All certificates following the first must be CA certificates");
            }
          }
          localObject2 = (X509Certificate[])Arrays.copyOf(paramArrayOfX509Certificate, paramArrayOfX509Certificate.length);
          if (paramPrivateKey != null)
          {
            if (paramPrivateKey.getEncoded() == null) {
              throw new IllegalArgumentException("Private key cannot be encoded");
            }
          }
          else {
            throw new IllegalArgumentException("Client cert without a private key");
          }
        }
        else
        {
          throw new IllegalArgumentException("First certificate in the chain must be a client end certificate");
        }
      }
    }
    mClientPrivateKey = paramPrivateKey;
    mClientCertificateChain = ((X509Certificate[])localObject2);
  }
  
  public void setDomainSuffixMatch(String paramString)
  {
    setFieldValue("domain_suffix_match", paramString);
  }
  
  public void setEapMethod(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Unknown EAP method");
    case 1: 
    case 7: 
      setPhase2Method(0);
    }
    mEapMethod = paramInt;
    setFieldValue("proactive_key_caching", "1");
  }
  
  public void setFieldValue(String paramString1, String paramString2)
  {
    setFieldValue(paramString1, paramString2, "");
  }
  
  public void setIdentity(String paramString)
  {
    setFieldValue("identity", paramString, "");
  }
  
  public void setPassword(String paramString)
  {
    setFieldValue("password", paramString);
  }
  
  public void setPhase2Method(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Unknown Phase 2 method");
    }
    mPhase2Method = paramInt;
  }
  
  public void setPlmn(String paramString)
  {
    setFieldValue("plmn", paramString);
  }
  
  public void setRealm(String paramString)
  {
    setFieldValue("realm", paramString);
  }
  
  public void setSimNum(int paramInt)
  {
    setFieldValue("sim_num", Integer.toString(paramInt));
  }
  
  public void setSubjectMatch(String paramString)
  {
    setFieldValue("subject_match", paramString);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = mFields.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2;
      if ("password".equals(str1)) {
        str2 = "<removed>";
      } else {
        str2 = (String)mFields.get(str1);
      }
      localStringBuffer.append(str1);
      localStringBuffer.append(" ");
      localStringBuffer.append(str2);
      localStringBuffer.append("\n");
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFields.size());
    Iterator localIterator = mFields.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)localEntry.getKey());
      paramParcel.writeString((String)localEntry.getValue());
    }
    paramParcel.writeInt(mEapMethod);
    paramParcel.writeInt(mPhase2Method);
    ParcelUtil.writeCertificates(paramParcel, mCaCerts);
    ParcelUtil.writePrivateKey(paramParcel, mClientPrivateKey);
    ParcelUtil.writeCertificates(paramParcel, mClientCertificateChain);
  }
  
  public static final class Eap
  {
    public static final int AKA = 5;
    public static final int AKA_PRIME = 6;
    public static final int NONE = -1;
    public static final int PEAP = 0;
    public static final int PWD = 3;
    public static final int SIM = 4;
    public static final int TLS = 1;
    public static final int TTLS = 2;
    public static final int UNAUTH_TLS = 7;
    public static final String[] strings = { "PEAP", "TLS", "TTLS", "PWD", "SIM", "AKA", "AKA'", "WFA-UNAUTH-TLS" };
    
    private Eap() {}
  }
  
  public static final class Phase2
  {
    public static final int AKA = 6;
    public static final int AKA_PRIME = 7;
    private static final String AUTHEAP_PREFIX = "autheap=";
    private static final String AUTH_PREFIX = "auth=";
    public static final int GTC = 4;
    public static final int MSCHAP = 2;
    public static final int MSCHAPV2 = 3;
    public static final int NONE = 0;
    public static final int PAP = 1;
    public static final int SIM = 5;
    public static final String[] strings = { "NULL", "PAP", "MSCHAP", "MSCHAPV2", "GTC", "SIM", "AKA", "AKA'" };
    
    private Phase2() {}
  }
  
  public static abstract interface SupplicantLoader
  {
    public abstract String loadValue(String paramString);
  }
  
  public static abstract interface SupplicantSaver
  {
    public abstract boolean saveValue(String paramString1, String paramString2);
  }
}
