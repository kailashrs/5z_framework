package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.security.KeyStore;
import android.security.keystore.AndroidKeyStoreProvider;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.ILockSettings.Stub;
import java.security.Key;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SystemApi
public class RecoveryController
{
  public static final int ERROR_BAD_CERTIFICATE_FORMAT = 25;
  public static final int ERROR_DECRYPTION_FAILED = 26;
  public static final int ERROR_DOWNGRADE_CERTIFICATE = 29;
  public static final int ERROR_INSECURE_USER = 23;
  public static final int ERROR_INVALID_CERTIFICATE = 28;
  public static final int ERROR_INVALID_KEY_FORMAT = 27;
  public static final int ERROR_NO_SNAPSHOT_PENDING = 21;
  public static final int ERROR_SERVICE_INTERNAL_ERROR = 22;
  public static final int ERROR_SESSION_EXPIRED = 24;
  public static final int RECOVERY_STATUS_PERMANENT_FAILURE = 3;
  public static final int RECOVERY_STATUS_SYNCED = 0;
  public static final int RECOVERY_STATUS_SYNC_IN_PROGRESS = 1;
  private static final String TAG = "RecoveryController";
  private final ILockSettings mBinder;
  private final KeyStore mKeyStore;
  
  private RecoveryController(ILockSettings paramILockSettings, KeyStore paramKeyStore)
  {
    mBinder = paramILockSettings;
    mKeyStore = paramKeyStore;
  }
  
  public static RecoveryController getInstance(Context paramContext)
  {
    return new RecoveryController(ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings")), KeyStore.getInstance());
  }
  
  public static boolean isRecoverableKeyStoreEnabled(Context paramContext)
  {
    paramContext = (KeyguardManager)paramContext.getSystemService(KeyguardManager.class);
    boolean bool;
    if ((paramContext != null) && (paramContext.isDeviceSecure())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public RecoverySession createRecoverySession()
  {
    return RecoverySession.newInstance(this);
  }
  
  @Deprecated
  public byte[] generateAndStoreKey(String paramString, byte[] paramArrayOfByte)
    throws InternalRecoveryServiceException, LockScreenRequiredException
  {
    throw new UnsupportedOperationException("Operation is not supported, use generateKey");
  }
  
  public Key generateKey(String paramString)
    throws InternalRecoveryServiceException, LockScreenRequiredException
  {
    try
    {
      paramString = mBinder.generateKey(paramString);
      if (paramString != null) {
        return getKeyFromGrant(paramString);
      }
      paramString = new android/security/keystore/recovery/InternalRecoveryServiceException;
      paramString.<init>("null grant alias");
      throw paramString;
    }
    catch (ServiceSpecificException paramString)
    {
      if (errorCode == 23) {
        throw new LockScreenRequiredException(paramString.getMessage());
      }
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (UnrecoverableKeyException paramString)
    {
      throw new InternalRecoveryServiceException("Failed to get key from keystore", paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public Key generateKey(String paramString, byte[] paramArrayOfByte)
    throws InternalRecoveryServiceException, LockScreenRequiredException
  {
    throw new UnsupportedOperationException();
  }
  
  public List<String> getAliases()
    throws InternalRecoveryServiceException
  {
    try
    {
      ArrayList localArrayList = new ArrayList(mBinder.getRecoveryStatus().keySet());
      return localArrayList;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw wrapUnexpectedServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public List<String> getAliases(String paramString)
    throws InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  ILockSettings getBinder()
  {
    return mBinder;
  }
  
  public Key getKey(String paramString)
    throws InternalRecoveryServiceException, UnrecoverableKeyException
  {
    try
    {
      paramString = mBinder.getKey(paramString);
      if ((paramString != null) && (!"".equals(paramString)))
      {
        paramString = getKeyFromGrant(paramString);
        return paramString;
      }
      return null;
    }
    catch (ServiceSpecificException paramString)
    {
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public KeyChainSnapshot getKeyChainSnapshot()
    throws InternalRecoveryServiceException
  {
    try
    {
      KeyChainSnapshot localKeyChainSnapshot = mBinder.getKeyChainSnapshot();
      return localKeyChainSnapshot;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      if (errorCode == 21) {
        return null;
      }
      throw wrapUnexpectedServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  Key getKeyFromGrant(String paramString)
    throws UnrecoverableKeyException
  {
    return AndroidKeyStoreProvider.loadAndroidKeyStoreKeyFromKeystore(mKeyStore, paramString, -1);
  }
  
  @Deprecated
  public KeyChainSnapshot getRecoveryData()
    throws InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  public int[] getRecoverySecretTypes()
    throws InternalRecoveryServiceException
  {
    try
    {
      int[] arrayOfInt = mBinder.getRecoverySecretTypes();
      return arrayOfInt;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw wrapUnexpectedServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getRecoveryStatus(String paramString)
    throws InternalRecoveryServiceException
  {
    try
    {
      paramString = (Integer)mBinder.getRecoveryStatus().get(paramString);
      if (paramString == null) {
        return 3;
      }
      int i = paramString.intValue();
      return i;
    }
    catch (ServiceSpecificException paramString)
    {
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int getRecoveryStatus(String paramString1, String paramString2)
    throws InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  public Map<String, X509Certificate> getRootCertificates()
  {
    return TrustedRootCertificates.getRootCertificates();
  }
  
  public Key importKey(String paramString, byte[] paramArrayOfByte)
    throws InternalRecoveryServiceException, LockScreenRequiredException
  {
    try
    {
      paramString = mBinder.importKey(paramString, paramArrayOfByte);
      if (paramString != null) {
        return getKeyFromGrant(paramString);
      }
      paramString = new android/security/keystore/recovery/InternalRecoveryServiceException;
      paramString.<init>("Null grant alias");
      throw paramString;
    }
    catch (ServiceSpecificException paramString)
    {
      if (errorCode == 23) {
        throw new LockScreenRequiredException(paramString.getMessage());
      }
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (UnrecoverableKeyException paramString)
    {
      throw new InternalRecoveryServiceException("Failed to get key from keystore", paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void initRecoveryService(String paramString, byte[] paramArrayOfByte)
    throws CertificateException, InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  public void initRecoveryService(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws CertificateException, InternalRecoveryServiceException
  {
    try
    {
      mBinder.initRecoveryServiceWithSigFile(paramString, paramArrayOfByte1, paramArrayOfByte2);
      return;
    }
    catch (ServiceSpecificException paramString)
    {
      if ((errorCode != 25) && (errorCode != 28))
      {
        if (errorCode == 29) {
          throw new CertificateException("Downgrading certificate serial version isn't supported.", paramString);
        }
        throw wrapUnexpectedServiceSpecificException(paramString);
      }
      throw new CertificateException("Invalid certificate for recovery service", paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeKey(String paramString)
    throws InternalRecoveryServiceException
  {
    try
    {
      mBinder.removeKey(paramString);
      return;
    }
    catch (ServiceSpecificException paramString)
    {
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setRecoverySecretTypes(int[] paramArrayOfInt)
    throws InternalRecoveryServiceException
  {
    try
    {
      mBinder.setRecoverySecretTypes(paramArrayOfInt);
      return;
    }
    catch (ServiceSpecificException paramArrayOfInt)
    {
      throw wrapUnexpectedServiceSpecificException(paramArrayOfInt);
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public void setRecoveryStatus(String paramString, int paramInt)
    throws InternalRecoveryServiceException
  {
    try
    {
      mBinder.setRecoveryStatus(paramString, paramInt);
      return;
    }
    catch (ServiceSpecificException paramString)
    {
      throw wrapUnexpectedServiceSpecificException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setRecoveryStatus(String paramString1, String paramString2, int paramInt)
    throws PackageManager.NameNotFoundException, InternalRecoveryServiceException
  {
    throw new UnsupportedOperationException();
  }
  
  public void setServerParams(byte[] paramArrayOfByte)
    throws InternalRecoveryServiceException
  {
    try
    {
      mBinder.setServerParams(paramArrayOfByte);
      return;
    }
    catch (ServiceSpecificException paramArrayOfByte)
    {
      throw wrapUnexpectedServiceSpecificException(paramArrayOfByte);
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public void setSnapshotCreatedPendingIntent(PendingIntent paramPendingIntent)
    throws InternalRecoveryServiceException
  {
    try
    {
      mBinder.setSnapshotCreatedPendingIntent(paramPendingIntent);
      return;
    }
    catch (ServiceSpecificException paramPendingIntent)
    {
      throw wrapUnexpectedServiceSpecificException(paramPendingIntent);
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  InternalRecoveryServiceException wrapUnexpectedServiceSpecificException(ServiceSpecificException paramServiceSpecificException)
  {
    if (errorCode == 22) {
      return new InternalRecoveryServiceException(paramServiceSpecificException.getMessage());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected error code for method: ");
    localStringBuilder.append(errorCode);
    return new InternalRecoveryServiceException(localStringBuilder.toString(), paramServiceSpecificException);
  }
}
