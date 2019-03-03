package android.security;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.org.bouncycastle.util.io.pem.PemObject;
import com.android.org.bouncycastle.util.io.pem.PemReader;
import com.android.org.bouncycastle.util.io.pem.PemWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class Credentials
{
  public static final String CA_CERTIFICATE = "CACERT_";
  public static final String EXTENSION_CER = ".cer";
  public static final String EXTENSION_CRT = ".crt";
  public static final String EXTENSION_P12 = ".p12";
  public static final String EXTENSION_PFX = ".pfx";
  public static final String EXTRA_CA_CERTIFICATES_DATA = "ca_certificates_data";
  public static final String EXTRA_CA_CERTIFICATES_NAME = "ca_certificates_name";
  public static final String EXTRA_INSTALL_AS_UID = "install_as_uid";
  public static final String EXTRA_PRIVATE_KEY = "PKEY";
  public static final String EXTRA_PUBLIC_KEY = "KEY";
  public static final String EXTRA_USER_CERTIFICATE_DATA = "user_certificate_data";
  public static final String EXTRA_USER_CERTIFICATE_NAME = "user_certificate_name";
  public static final String EXTRA_USER_PRIVATE_KEY_DATA = "user_private_key_data";
  public static final String EXTRA_USER_PRIVATE_KEY_NAME = "user_private_key_name";
  public static final String INSTALL_ACTION = "android.credentials.INSTALL";
  public static final String INSTALL_AS_USER_ACTION = "android.credentials.INSTALL_AS_USER";
  public static final String LOCKDOWN_VPN = "LOCKDOWN_VPN";
  private static final String LOGTAG = "Credentials";
  public static final String UNLOCK_ACTION = "com.android.credentials.UNLOCK";
  public static final String USER_CERTIFICATE = "USRCERT_";
  public static final String USER_PRIVATE_KEY = "USRPKEY_";
  public static final String USER_SECRET_KEY = "USRSKEY_";
  public static final String VPN = "VPN_";
  public static final String WIFI = "WIFI_";
  private static Credentials singleton;
  
  public Credentials() {}
  
  public static List<X509Certificate> convertFromPem(byte[] paramArrayOfByte)
    throws IOException, CertificateException
  {
    paramArrayOfByte = new PemReader(new InputStreamReader(new ByteArrayInputStream(paramArrayOfByte), StandardCharsets.US_ASCII));
    try
    {
      Object localObject1 = CertificateFactory.getInstance("X509");
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      PemObject localPemObject;
      for (;;)
      {
        localPemObject = paramArrayOfByte.readPemObject();
        if (localPemObject == null) {
          break label137;
        }
        if (!localPemObject.getType().equals("CERTIFICATE")) {
          break;
        }
        localObject3 = new java/io/ByteArrayInputStream;
        ((ByteArrayInputStream)localObject3).<init>(localPemObject.getContent());
        localArrayList.add((X509Certificate)((CertificateFactory)localObject1).generateCertificate((InputStream)localObject3));
      }
      localObject1 = new java/lang/IllegalArgumentException;
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("Unknown type ");
      ((StringBuilder)localObject3).append(localPemObject.getType());
      ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject3).toString());
      throw ((Throwable)localObject1);
      label137:
      return localArrayList;
    }
    finally
    {
      paramArrayOfByte.close();
    }
  }
  
  public static byte[] convertToPem(Certificate... paramVarArgs)
    throws IOException, CertificateEncodingException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    PemWriter localPemWriter = new PemWriter(new OutputStreamWriter(localByteArrayOutputStream, StandardCharsets.US_ASCII));
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      localPemWriter.writeObject(new PemObject("CERTIFICATE", paramVarArgs[j].getEncoded()));
    }
    localPemWriter.close();
    return localByteArrayOutputStream.toByteArray();
  }
  
  public static boolean deleteAllTypesForAlias(KeyStore paramKeyStore, String paramString)
  {
    return deleteAllTypesForAlias(paramKeyStore, paramString, -1);
  }
  
  public static boolean deleteAllTypesForAlias(KeyStore paramKeyStore, String paramString, int paramInt)
  {
    return deleteUserKeyTypeForAlias(paramKeyStore, paramString, paramInt) & deleteCertificateTypesForAlias(paramKeyStore, paramString, paramInt);
  }
  
  public static boolean deleteCertificateTypesForAlias(KeyStore paramKeyStore, String paramString)
  {
    return deleteCertificateTypesForAlias(paramKeyStore, paramString, -1);
  }
  
  public static boolean deleteCertificateTypesForAlias(KeyStore paramKeyStore, String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("USRCERT_");
    localStringBuilder.append(paramString);
    boolean bool = paramKeyStore.delete(localStringBuilder.toString(), paramInt);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("CACERT_");
    localStringBuilder.append(paramString);
    return bool & paramKeyStore.delete(localStringBuilder.toString(), paramInt);
  }
  
  public static boolean deleteLegacyKeyForAlias(KeyStore paramKeyStore, String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("USRSKEY_");
    localStringBuilder.append(paramString);
    return paramKeyStore.delete(localStringBuilder.toString(), paramInt);
  }
  
  public static boolean deleteUserKeyTypeForAlias(KeyStore paramKeyStore, String paramString)
  {
    return deleteUserKeyTypeForAlias(paramKeyStore, paramString, -1);
  }
  
  public static boolean deleteUserKeyTypeForAlias(KeyStore paramKeyStore, String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("USRPKEY_");
    localStringBuilder.append(paramString);
    if (!paramKeyStore.delete(localStringBuilder.toString(), paramInt))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("USRSKEY_");
      localStringBuilder.append(paramString);
      if (!paramKeyStore.delete(localStringBuilder.toString(), paramInt))
      {
        bool = false;
        break label78;
      }
    }
    boolean bool = true;
    label78:
    return bool;
  }
  
  public static Credentials getInstance()
  {
    if (singleton == null) {
      singleton = new Credentials();
    }
    return singleton;
  }
  
  public void install(Context paramContext)
  {
    try
    {
      paramContext.startActivity(KeyChain.createInstallIntent());
    }
    catch (ActivityNotFoundException paramContext)
    {
      Log.w("Credentials", paramContext.toString());
    }
  }
  
  public void install(Context paramContext, String paramString, byte[] paramArrayOfByte)
  {
    try
    {
      Intent localIntent = KeyChain.createInstallIntent();
      localIntent.putExtra(paramString, paramArrayOfByte);
      paramContext.startActivity(localIntent);
    }
    catch (ActivityNotFoundException paramContext)
    {
      Log.w("Credentials", paramContext.toString());
    }
  }
  
  public void install(Context paramContext, KeyPair paramKeyPair)
  {
    try
    {
      Intent localIntent = KeyChain.createInstallIntent();
      localIntent.putExtra("PKEY", paramKeyPair.getPrivate().getEncoded());
      localIntent.putExtra("KEY", paramKeyPair.getPublic().getEncoded());
      paramContext.startActivity(localIntent);
    }
    catch (ActivityNotFoundException paramContext)
    {
      Log.w("Credentials", paramContext.toString());
    }
  }
  
  public void unlock(Context paramContext)
  {
    try
    {
      Intent localIntent = new android/content/Intent;
      localIntent.<init>("com.android.credentials.UNLOCK");
      paramContext.startActivity(localIntent);
    }
    catch (ActivityNotFoundException paramContext)
    {
      Log.w("Credentials", paramContext.toString());
    }
  }
}
