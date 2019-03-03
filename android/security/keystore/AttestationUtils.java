package android.security.keystore;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyStore;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterCertificateChain;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SystemApi
public abstract class AttestationUtils
{
  public static final int ID_TYPE_IMEI = 2;
  public static final int ID_TYPE_MEID = 3;
  public static final int ID_TYPE_SERIAL = 1;
  
  private AttestationUtils() {}
  
  public static X509Certificate[] attestDeviceIds(Context paramContext, int[] paramArrayOfInt, byte[] paramArrayOfByte)
    throws DeviceIdAttestationException
  {
    paramContext = prepareAttestationArgumentsForDeviceId(paramContext, paramArrayOfInt, paramArrayOfByte);
    paramArrayOfInt = new KeymasterCertificateChain();
    int i = KeyStore.getInstance().attestDeviceIds(paramContext, paramArrayOfInt);
    if (i == 1) {
      try
      {
        paramContext = parseCertificateChain(paramArrayOfInt);
        return paramContext;
      }
      catch (KeyAttestationException paramContext)
      {
        throw new DeviceIdAttestationException(paramContext.getMessage(), paramContext);
      }
    }
    throw new DeviceIdAttestationException("Unable to perform attestation", KeyStore.getKeyStoreException(i));
  }
  
  public static boolean isChainValid(KeymasterCertificateChain paramKeymasterCertificateChain)
  {
    boolean bool;
    if ((paramKeymasterCertificateChain != null) && (paramKeymasterCertificateChain.getCertificates().size() >= 2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static X509Certificate[] parseCertificateChain(KeymasterCertificateChain paramKeymasterCertificateChain)
    throws KeyAttestationException
  {
    Object localObject = paramKeymasterCertificateChain.getCertificates();
    if (((Collection)localObject).size() >= 2)
    {
      paramKeymasterCertificateChain = new ByteArrayOutputStream();
      try
      {
        localObject = ((Collection)localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          paramKeymasterCertificateChain.write((byte[])((Iterator)localObject).next());
        }
        localObject = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
        localByteArrayInputStream.<init>(paramKeymasterCertificateChain.toByteArray());
        paramKeymasterCertificateChain = (X509Certificate[])((CertificateFactory)localObject).generateCertificates(localByteArrayInputStream).toArray(new X509Certificate[0]);
        return paramKeymasterCertificateChain;
      }
      catch (Exception paramKeymasterCertificateChain)
      {
        throw new KeyAttestationException("Unable to construct certificate chain", paramKeymasterCertificateChain);
      }
    }
    paramKeymasterCertificateChain = new StringBuilder();
    paramKeymasterCertificateChain.append("Attestation certificate chain contained ");
    paramKeymasterCertificateChain.append(((Collection)localObject).size());
    paramKeymasterCertificateChain.append(" entries. At least two are required.");
    throw new KeyAttestationException(paramKeymasterCertificateChain.toString());
  }
  
  public static KeymasterArguments prepareAttestationArguments(Context paramContext, int[] paramArrayOfInt, byte[] paramArrayOfByte)
    throws DeviceIdAttestationException
  {
    if (paramArrayOfByte != null)
    {
      KeymasterArguments localKeymasterArguments = new KeymasterArguments();
      localKeymasterArguments.addBytes(-1879047484, paramArrayOfByte);
      if (paramArrayOfInt == null) {
        return localKeymasterArguments;
      }
      paramArrayOfByte = new ArraySet(paramArrayOfInt.length);
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        paramArrayOfByte.add(Integer.valueOf(paramArrayOfInt[j]));
      }
      paramArrayOfInt = null;
      if ((paramArrayOfByte.contains(Integer.valueOf(2))) || (paramArrayOfByte.contains(Integer.valueOf(3))))
      {
        paramArrayOfInt = (TelephonyManager)paramContext.getSystemService("phone");
        if (paramArrayOfInt == null) {}
      }
      else
      {
        paramContext = paramArrayOfByte.iterator();
        while (paramContext.hasNext())
        {
          paramArrayOfByte = (Integer)paramContext.next();
          switch (paramArrayOfByte.intValue())
          {
          default: 
            paramContext = new StringBuilder();
            paramContext.append("Unknown device ID type ");
            paramContext.append(paramArrayOfByte);
            throw new IllegalArgumentException(paramContext.toString());
          case 3: 
            paramArrayOfByte = paramArrayOfInt.getMeid(0);
            if (paramArrayOfByte != null) {
              localKeymasterArguments.addBytes(-1879047477, paramArrayOfByte.getBytes(StandardCharsets.UTF_8));
            } else {
              throw new DeviceIdAttestationException("Unable to retrieve MEID");
            }
            break;
          case 2: 
            paramArrayOfByte = paramArrayOfInt.getImei(0);
            if (paramArrayOfByte != null) {
              localKeymasterArguments.addBytes(-1879047478, paramArrayOfByte.getBytes(StandardCharsets.UTF_8));
            } else {
              throw new DeviceIdAttestationException("Unable to retrieve IMEI");
            }
            break;
          case 1: 
            localKeymasterArguments.addBytes(-1879047479, Build.getSerial().getBytes(StandardCharsets.UTF_8));
          }
        }
        localKeymasterArguments.addBytes(-1879047482, Build.BRAND.getBytes(StandardCharsets.UTF_8));
        localKeymasterArguments.addBytes(-1879047481, Build.DEVICE.getBytes(StandardCharsets.UTF_8));
        localKeymasterArguments.addBytes(-1879047480, Build.PRODUCT.getBytes(StandardCharsets.UTF_8));
        localKeymasterArguments.addBytes(-1879047476, Build.MANUFACTURER.getBytes(StandardCharsets.UTF_8));
        localKeymasterArguments.addBytes(-1879047475, Build.MODEL.getBytes(StandardCharsets.UTF_8));
        return localKeymasterArguments;
      }
      throw new DeviceIdAttestationException("Unable to access telephony service");
    }
    throw new NullPointerException("Missing attestation challenge");
  }
  
  private static KeymasterArguments prepareAttestationArgumentsForDeviceId(Context paramContext, int[] paramArrayOfInt, byte[] paramArrayOfByte)
    throws DeviceIdAttestationException
  {
    if (paramArrayOfInt != null) {
      return prepareAttestationArguments(paramContext, paramArrayOfInt, paramArrayOfByte);
    }
    throw new NullPointerException("Missing id types");
  }
}
