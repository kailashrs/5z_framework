package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.net.wifi.hotspot2.pps.Credential;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ConfigParser
{
  private static final String BOUNDARY = "boundary=";
  private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String ENCODING_BASE64 = "base64";
  private static final String TAG = "ConfigParser";
  private static final String TYPE_CA_CERT = "application/x-x509-ca-cert";
  private static final String TYPE_MULTIPART_MIXED = "multipart/mixed";
  private static final String TYPE_PASSPOINT_PROFILE = "application/x-passpoint-profile";
  private static final String TYPE_PKCS12 = "application/x-pkcs12";
  private static final String TYPE_WIFI_CONFIG = "application/x-wifi-config";
  
  public ConfigParser() {}
  
  private static PasspointConfiguration createPasspointConfig(Map<String, byte[]> paramMap)
    throws IOException
  {
    Object localObject = (byte[])paramMap.get("application/x-passpoint-profile");
    if (localObject != null)
    {
      localObject = PpsMoParser.parseMoText(new String((byte[])localObject));
      if (localObject != null)
      {
        if (((PasspointConfiguration)localObject).getCredential() != null)
        {
          byte[] arrayOfByte = (byte[])paramMap.get("application/x-x509-ca-cert");
          if (arrayOfByte != null) {
            try
            {
              ((PasspointConfiguration)localObject).getCredential().setCaCertificate(parseCACert(arrayOfByte));
            }
            catch (CertificateException paramMap)
            {
              throw new IOException("Failed to parse CA Certificate");
            }
          }
          paramMap = (byte[])paramMap.get("application/x-pkcs12");
          if (paramMap != null) {
            try
            {
              paramMap = parsePkcs12(paramMap);
              ((PasspointConfiguration)localObject).getCredential().setClientPrivateKey((PrivateKey)first);
              ((PasspointConfiguration)localObject).getCredential().setClientCertificateChain((X509Certificate[])((List)second).toArray(new X509Certificate[((List)second).size()]));
            }
            catch (GeneralSecurityException|IOException paramMap)
            {
              throw new IOException("Failed to parse PCKS12 string");
            }
          }
          return localObject;
        }
        throw new IOException("Passpoint profile missing credential");
      }
      throw new IOException("Failed to parse Passpoint profile");
    }
    throw new IOException("Missing Passpoint Profile");
  }
  
  private static X509Certificate parseCACert(byte[] paramArrayOfByte)
    throws CertificateException
  {
    return (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(paramArrayOfByte));
  }
  
  private static Pair<String, String> parseContentType(String paramString)
    throws IOException
  {
    String[] arrayOfString = paramString.split(";");
    if (arrayOfString.length >= 1)
    {
      String str = arrayOfString[0].trim();
      paramString = null;
      for (int i = 1; i < arrayOfString.length; i++)
      {
        localObject = arrayOfString[i].trim();
        if (!((String)localObject).startsWith("boundary="))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Ignore Content-Type attribute: ");
          ((StringBuilder)localObject).append(arrayOfString[i]);
          Log.d("ConfigParser", ((StringBuilder)localObject).toString());
        }
        else
        {
          localObject = ((String)localObject).substring("boundary=".length());
          paramString = (String)localObject;
          if (((String)localObject).length() > 1)
          {
            paramString = (String)localObject;
            if (((String)localObject).startsWith("\""))
            {
              paramString = (String)localObject;
              if (((String)localObject).endsWith("\"")) {
                paramString = ((String)localObject).substring(1, ((String)localObject).length() - 1);
              }
            }
          }
        }
      }
      return new Pair(str, paramString);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid Content-Type: ");
    ((StringBuilder)localObject).append(paramString);
    throw new IOException(((StringBuilder)localObject).toString());
  }
  
  private static MimeHeader parseHeaders(LineNumberReader paramLineNumberReader)
    throws IOException
  {
    MimeHeader localMimeHeader = new MimeHeader(null);
    paramLineNumberReader = readHeaders(paramLineNumberReader).entrySet().iterator();
    while (paramLineNumberReader.hasNext())
    {
      Object localObject1 = (Map.Entry)paramLineNumberReader.next();
      Object localObject2 = (String)((Map.Entry)localObject1).getKey();
      int i = -1;
      int j = ((String)localObject2).hashCode();
      if (j != 747297921)
      {
        if ((j == 949037134) && (((String)localObject2).equals("Content-Type"))) {
          i = 0;
        }
      }
      else if (((String)localObject2).equals("Content-Transfer-Encoding")) {
        i = 1;
      }
      switch (i)
      {
      default: 
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Ignore header: ");
        ((StringBuilder)localObject2).append((String)((Map.Entry)localObject1).getKey());
        Log.d("ConfigParser", ((StringBuilder)localObject2).toString());
        break;
      case 1: 
        encodingType = ((String)((Map.Entry)localObject1).getValue());
        break;
      case 0: 
        localObject1 = parseContentType((String)((Map.Entry)localObject1).getValue());
        contentType = ((String)first);
        boundary = ((String)second);
      }
    }
    return localMimeHeader;
  }
  
  private static Map<String, byte[]> parseMimeMultipartMessage(LineNumberReader paramLineNumberReader)
    throws IOException
  {
    Object localObject1 = parseHeaders(paramLineNumberReader);
    if (TextUtils.equals(contentType, "multipart/mixed"))
    {
      if (!TextUtils.isEmpty(boundary))
      {
        if (TextUtils.equals(encodingType, "base64"))
        {
          for (;;)
          {
            Object localObject2 = paramLineNumberReader.readLine();
            if (localObject2 == null) {
              break;
            }
            Object localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("--");
            ((StringBuilder)localObject3).append(boundary);
            if (((String)localObject2).equals(((StringBuilder)localObject3).toString()))
            {
              localObject3 = new HashMap();
              do
              {
                localObject2 = parseMimePart(paramLineNumberReader, boundary);
                ((Map)localObject3).put(type, data);
              } while (!isLast);
              return localObject3;
            }
          }
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unexpected EOF before first boundary @ ");
          ((StringBuilder)localObject1).append(paramLineNumberReader.getLineNumber());
          throw new IOException(((StringBuilder)localObject1).toString());
        }
        paramLineNumberReader = new StringBuilder();
        paramLineNumberReader.append("Unexpected encoding: ");
        paramLineNumberReader.append(encodingType);
        throw new IOException(paramLineNumberReader.toString());
      }
      throw new IOException("Missing boundary string");
    }
    paramLineNumberReader = new StringBuilder();
    paramLineNumberReader.append("Invalid content type: ");
    paramLineNumberReader.append(contentType);
    throw new IOException(paramLineNumberReader.toString());
  }
  
  private static MimePart parseMimePart(LineNumberReader paramLineNumberReader, String paramString)
    throws IOException
  {
    MimeHeader localMimeHeader = parseHeaders(paramLineNumberReader);
    if (TextUtils.equals(encodingType, "base64"))
    {
      if ((!TextUtils.equals(contentType, "application/x-passpoint-profile")) && (!TextUtils.equals(contentType, "application/x-x509-ca-cert")) && (!TextUtils.equals(contentType, "application/x-pkcs12")))
      {
        paramLineNumberReader = new StringBuilder();
        paramLineNumberReader.append("Unexpected content type: ");
        paramLineNumberReader.append(contentType);
        throw new IOException(paramLineNumberReader.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      boolean bool = false;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("--");
      ((StringBuilder)localObject).append(paramString);
      paramString = ((StringBuilder)localObject).toString();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("--");
      String str = ((StringBuilder)localObject).toString();
      for (;;)
      {
        localObject = paramLineNumberReader.readLine();
        if (localObject == null) {
          break;
        }
        if (((String)localObject).startsWith(paramString))
        {
          if (((String)localObject).equals(str)) {
            bool = true;
          }
          paramLineNumberReader = new MimePart(null);
          type = contentType;
          data = Base64.decode(localStringBuilder.toString(), 0);
          isLast = bool;
          return paramLineNumberReader;
        }
        localStringBuilder.append((String)localObject);
      }
      paramString = new StringBuilder();
      paramString.append("Unexpected EOF file in body @ ");
      paramString.append(paramLineNumberReader.getLineNumber());
      throw new IOException(paramString.toString());
    }
    paramLineNumberReader = new StringBuilder();
    paramLineNumberReader.append("Unexpected encoding type: ");
    paramLineNumberReader.append(encodingType);
    throw new IOException(paramLineNumberReader.toString());
  }
  
  public static PasspointConfiguration parsePasspointConfig(String paramString, byte[] paramArrayOfByte)
  {
    if (!TextUtils.equals(paramString, "application/x-wifi-config"))
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unexpected MIME type: ");
      paramArrayOfByte.append(paramString);
      Log.e("ConfigParser", paramArrayOfByte.toString());
      return null;
    }
    try
    {
      paramString = new java/lang/String;
      paramString.<init>(paramArrayOfByte, StandardCharsets.ISO_8859_1);
      byte[] arrayOfByte = Base64.decode(paramString, 0);
      paramArrayOfByte = new java/io/LineNumberReader;
      InputStreamReader localInputStreamReader = new java/io/InputStreamReader;
      paramString = new java/io/ByteArrayInputStream;
      paramString.<init>(arrayOfByte);
      localInputStreamReader.<init>(paramString, StandardCharsets.ISO_8859_1);
      paramArrayOfByte.<init>(localInputStreamReader);
      paramString = createPasspointConfig(parseMimeMultipartMessage(paramArrayOfByte));
      return paramString;
    }
    catch (IOException|IllegalArgumentException paramString)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Failed to parse installation file: ");
      paramArrayOfByte.append(paramString.getMessage());
      Log.e("ConfigParser", paramArrayOfByte.toString());
    }
    return null;
  }
  
  private static Pair<PrivateKey, List<X509Certificate>> parsePkcs12(byte[] paramArrayOfByte)
    throws GeneralSecurityException, IOException
  {
    Object localObject1 = KeyStore.getInstance("PKCS12");
    paramArrayOfByte = new ByteArrayInputStream(paramArrayOfByte);
    int i = 0;
    ((KeyStore)localObject1).load(paramArrayOfByte, new char[0]);
    paramArrayOfByte.close();
    if (((KeyStore)localObject1).size() == 1)
    {
      Object localObject2 = (String)((KeyStore)localObject1).aliases().nextElement();
      if (localObject2 != null)
      {
        PrivateKey localPrivateKey = (PrivateKey)((KeyStore)localObject1).getKey((String)localObject2, null);
        paramArrayOfByte = null;
        localObject2 = ((KeyStore)localObject1).getCertificateChain((String)localObject2);
        if (localObject2 != null)
        {
          localObject1 = new ArrayList();
          int j = localObject2.length;
          for (;;)
          {
            paramArrayOfByte = (byte[])localObject1;
            if (i >= j) {
              break label163;
            }
            paramArrayOfByte = localObject2[i];
            if (!(paramArrayOfByte instanceof X509Certificate)) {
              break;
            }
            ((List)localObject1).add((X509Certificate)paramArrayOfByte);
            i++;
          }
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unexpceted certificate type: ");
          ((StringBuilder)localObject1).append(paramArrayOfByte.getClass());
          throw new IOException(((StringBuilder)localObject1).toString());
        }
        label163:
        return new Pair(localPrivateKey, paramArrayOfByte);
      }
      throw new IOException("No alias found");
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Unexpected key size: ");
    paramArrayOfByte.append(((KeyStore)localObject1).size());
    throw new IOException(paramArrayOfByte.toString());
  }
  
  private static Map<String, String> readHeaders(LineNumberReader paramLineNumberReader)
    throws IOException
  {
    HashMap localHashMap = new HashMap();
    Object localObject = null;
    StringBuilder localStringBuilder = null;
    String str;
    for (;;)
    {
      str = paramLineNumberReader.readLine();
      if (str == null) {
        break label273;
      }
      if ((str.length() == 0) || (str.trim().length() == 0)) {
        break label255;
      }
      int i = str.indexOf(':');
      if (i < 0)
      {
        if (localStringBuilder != null)
        {
          localStringBuilder.append(' ');
          localStringBuilder.append(str.trim());
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Bad header line: '");
          localStringBuilder.append(str);
          localStringBuilder.append("' @ ");
          localStringBuilder.append(paramLineNumberReader.getLineNumber());
          throw new IOException(localStringBuilder.toString());
        }
      }
      else
      {
        if (Character.isWhitespace(str.charAt(0))) {
          break;
        }
        if (localObject != null) {
          localHashMap.put(localObject, localStringBuilder.toString());
        }
        localObject = str.substring(0, i).trim();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(str.substring(i + 1).trim());
      }
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Illegal blank prefix in header line '");
    localStringBuilder.append(str);
    localStringBuilder.append("' @ ");
    localStringBuilder.append(paramLineNumberReader.getLineNumber());
    throw new IOException(localStringBuilder.toString());
    label255:
    if (localObject != null) {
      localHashMap.put(localObject, localStringBuilder.toString());
    }
    return localHashMap;
    label273:
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Missing line @ ");
    localStringBuilder.append(paramLineNumberReader.getLineNumber());
    throw new IOException(localStringBuilder.toString());
  }
  
  private static class MimeHeader
  {
    public String boundary = null;
    public String contentType = null;
    public String encodingType = null;
    
    private MimeHeader() {}
  }
  
  private static class MimePart
  {
    public byte[] data = null;
    public boolean isLast = false;
    public String type = null;
    
    private MimePart() {}
  }
}
