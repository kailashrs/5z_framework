package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.widget.ILockSettings;
import java.security.Key;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SystemApi
public class RecoverySession
  implements AutoCloseable
{
  private static final int SESSION_ID_LENGTH_BYTES = 16;
  private static final String TAG = "RecoverySession";
  private final RecoveryController mRecoveryController;
  private final String mSessionId;
  
  private RecoverySession(RecoveryController paramRecoveryController, String paramString)
  {
    mRecoveryController = paramRecoveryController;
    mSessionId = paramString;
  }
  
  private Map<String, Key> getKeysFromGrants(Map<String, String> paramMap)
    throws InternalRecoveryServiceException
  {
    ArrayMap localArrayMap = new ArrayMap(paramMap.size());
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = (String)paramMap.get(str1);
      try
      {
        Key localKey = mRecoveryController.getKeyFromGrant(str2);
        localArrayMap.put(str1, localKey);
      }
      catch (UnrecoverableKeyException paramMap)
      {
        throw new InternalRecoveryServiceException(String.format(Locale.US, "Failed to get key '%s' from grant '%s'", new Object[] { str1, str2 }), paramMap);
      }
    }
    return localArrayMap;
  }
  
  static RecoverySession newInstance(RecoveryController paramRecoveryController)
  {
    return new RecoverySession(paramRecoveryController, newSessionId());
  }
  
  private static String newSessionId()
  {
    Object localObject = new SecureRandom();
    byte[] arrayOfByte = new byte[16];
    ((SecureRandom)localObject).nextBytes(arrayOfByte);
    localObject = new StringBuilder();
    int i = arrayOfByte.length;
    for (int j = 0; j < i; j++) {
      ((StringBuilder)localObject).append(Byte.toHexString(arrayOfByte[j], false));
    }
    return ((StringBuilder)localObject).toString();
  }
  
  public void close()
  {
    try
    {
      mRecoveryController.getBinder().closeSession(mSessionId);
    }
    catch (RemoteException|ServiceSpecificException localRemoteException)
    {
      Log.e("RecoverySession", "Unexpected error trying to close session", localRemoteException);
    }
  }
  
  String getSessionId()
  {
    return mSessionId;
  }
  
  public Map<String, Key> recoverKeyChainSnapshot(byte[] paramArrayOfByte, List<WrappedApplicationKey> paramList)
    throws SessionExpiredException, DecryptionFailedException, InternalRecoveryServiceException
  {
    try
    {
      paramArrayOfByte = getKeysFromGrants(mRecoveryController.getBinder().recoverKeyChainSnapshot(mSessionId, paramArrayOfByte, paramList));
      return paramArrayOfByte;
    }
    catch (ServiceSpecificException paramArrayOfByte)
    {
      if (errorCode != 26)
      {
        if (errorCode == 24) {
          throw new SessionExpiredException(paramArrayOfByte.getMessage());
        }
        throw mRecoveryController.wrapUnexpectedServiceSpecificException(paramArrayOfByte);
      }
      throw new DecryptionFailedException(paramArrayOfByte.getMessage());
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public Map<String, byte[]> recoverKeys(byte[] paramArrayOfByte, List<WrappedApplicationKey> paramList)
    throws SessionExpiredException, DecryptionFailedException, InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  public byte[] start(String paramString, CertPath paramCertPath, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List<KeyChainProtectionParams> paramList)
    throws CertificateException, InternalRecoveryServiceException
  {
    paramCertPath = RecoveryCertPath.createRecoveryCertPath(paramCertPath);
    try
    {
      paramString = mRecoveryController.getBinder().startRecoverySessionWithCertPath(mSessionId, paramString, paramCertPath, paramArrayOfByte1, paramArrayOfByte2, paramList);
      return paramString;
    }
    catch (ServiceSpecificException paramString)
    {
      if ((errorCode != 25) && (errorCode != 28)) {
        throw mRecoveryController.wrapUnexpectedServiceSpecificException(paramString);
      }
      throw new CertificateException("Invalid certificate for recovery session", paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public byte[] start(CertPath paramCertPath, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List<KeyChainProtectionParams> paramList)
    throws CertificateException, InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public byte[] start(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, List<KeyChainProtectionParams> paramList)
    throws CertificateException, InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
}
