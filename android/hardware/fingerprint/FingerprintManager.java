package android.hardware.fingerprint;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback;
import android.hardware.biometrics.BiometricAuthenticator.AuthenticationResult;
import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.CryptoObject;
import android.hardware.biometrics.IBiometricPromptReceiver;
import android.os.Binder;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Slog;
import java.security.Signature;
import java.util.List;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@Deprecated
public class FingerprintManager
  implements BiometricFingerprintConstants
{
  private static final boolean DEBUG = true;
  private static final int MSG_ACQUIRED = 101;
  private static final int MSG_AUTHENTICATION_FAILED = 103;
  private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
  private static final int MSG_ENROLL_RESULT = 100;
  private static final int MSG_ENUMERATED = 106;
  private static final int MSG_ERROR = 104;
  private static final int MSG_REMOVED = 105;
  private static final String TAG = "FingerprintManager";
  private BiometricAuthenticator.AuthenticationCallback mAuthenticationCallback;
  private Context mContext;
  private CryptoObject mCryptoObject;
  private EnrollmentCallback mEnrollmentCallback;
  private EnumerateCallback mEnumerateCallback;
  private Executor mExecutor;
  private Handler mHandler;
  private RemovalCallback mRemovalCallback;
  private Fingerprint mRemovalFingerprint;
  private IFingerprintService mService;
  private IFingerprintServiceReceiver mServiceReceiver = new IFingerprintServiceReceiver.Stub()
  {
    public void onAcquired(long paramAnonymousLong, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (mExecutor != null) {
        mExecutor.execute(new _..Lambda.FingerprintManager.2._CkUh5EAfiFsfsEamQtkeaLZq6M(this, paramAnonymousLong, paramAnonymousInt1, paramAnonymousInt2));
      } else {
        mHandler.obtainMessage(101, paramAnonymousInt1, paramAnonymousInt2, Long.valueOf(paramAnonymousLong)).sendToTarget();
      }
    }
    
    public void onAuthenticationFailed(long paramAnonymousLong)
    {
      if (mExecutor != null) {
        mExecutor.execute(new _..Lambda.FingerprintManager.2.ycpCnXGQKksU_rpxKvBm1XDbloE(this));
      } else {
        mHandler.obtainMessage(103).sendToTarget();
      }
    }
    
    public void onAuthenticationSucceeded(long paramAnonymousLong, Fingerprint paramAnonymousFingerprint, int paramAnonymousInt)
    {
      if (mExecutor != null) {
        mExecutor.execute(new _..Lambda.FingerprintManager.2.O5sigT8DLDwmCzdvD_k13MacOBU(this, paramAnonymousFingerprint, paramAnonymousInt));
      } else {
        mHandler.obtainMessage(102, paramAnonymousInt, 0, paramAnonymousFingerprint).sendToTarget();
      }
    }
    
    public void onEnrollResult(long paramAnonymousLong, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      mHandler.obtainMessage(100, paramAnonymousInt3, 0, new Fingerprint(null, paramAnonymousInt2, paramAnonymousInt1, paramAnonymousLong)).sendToTarget();
    }
    
    public void onEnumerated(long paramAnonymousLong, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      mHandler.obtainMessage(106, paramAnonymousInt1, paramAnonymousInt2, Long.valueOf(paramAnonymousLong)).sendToTarget();
    }
    
    public void onError(long paramAnonymousLong, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (mExecutor != null)
      {
        if ((paramAnonymousInt1 != 10) && (paramAnonymousInt1 != 5)) {
          mHandler.postDelayed(new _..Lambda.FingerprintManager.2.n67wlbYWr0PNZwBB3xLLO4RgAq4(this, paramAnonymousLong, paramAnonymousInt1, paramAnonymousInt2), 2000L);
        } else {
          mExecutor.execute(new _..Lambda.FingerprintManager.2.iiSGvjInjtzVqJ_wXw_4RQIjKDs(this, paramAnonymousLong, paramAnonymousInt1, paramAnonymousInt2));
        }
      }
      else {
        mHandler.obtainMessage(104, paramAnonymousInt1, paramAnonymousInt2, Long.valueOf(paramAnonymousLong)).sendToTarget();
      }
    }
    
    public void onRemoved(long paramAnonymousLong, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      mHandler.obtainMessage(105, paramAnonymousInt3, 0, new Fingerprint(null, paramAnonymousInt2, paramAnonymousInt1, paramAnonymousLong)).sendToTarget();
    }
  };
  private IBinder mToken = new Binder();
  
  public FingerprintManager(Context paramContext, IFingerprintService paramIFingerprintService)
  {
    mContext = paramContext;
    mService = paramIFingerprintService;
    if (mService == null) {
      Slog.v("FingerprintManager", "FingerprintManagerService was null");
    }
    mHandler = new MyHandler(paramContext, null);
    paramContext = mContext.getPackageManager();
    if (paramContext != null) {
      Build.FEATURES.ENABLE_FINGERPRINT_EARLYWAKEUP = paramContext.hasSystemFeature("asus.hardware.fingerprint_earlywakeup");
    }
  }
  
  private void authenticate(int paramInt, CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, Bundle paramBundle, Executor paramExecutor, IBiometricPromptReceiver paramIBiometricPromptReceiver, BiometricAuthenticator.AuthenticationCallback paramAuthenticationCallback)
  {
    mCryptoObject = paramCryptoObject;
    if (paramCancellationSignal.isCanceled())
    {
      Slog.w("FingerprintManager", "authentication already canceled");
      return;
    }
    paramCancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(paramCryptoObject));
    if (mService != null) {
      try
      {
        mExecutor = paramExecutor;
        mAuthenticationCallback = paramAuthenticationCallback;
        long l;
        if (paramCryptoObject != null) {
          l = paramCryptoObject.getOpId();
        } else {
          l = 0L;
        }
        mService.authenticate(mToken, l, paramInt, mServiceReceiver, 0, mContext.getOpPackageName(), paramBundle, paramIBiometricPromptReceiver);
      }
      catch (RemoteException paramCryptoObject)
      {
        Slog.w("FingerprintManager", "Remote exception while authenticating", paramCryptoObject);
        mExecutor.execute(new _..Lambda.FingerprintManager.0Q_OnkqSSy_nQ9iUWqvqVi6QjNE(this, paramAuthenticationCallback));
      }
    }
  }
  
  private void cancelAuthentication(CryptoObject paramCryptoObject)
  {
    if (mService != null) {
      try
      {
        mService.cancelAuthentication(mToken, mContext.getOpPackageName());
      }
      catch (RemoteException paramCryptoObject)
      {
        throw paramCryptoObject.rethrowFromSystemServer();
      }
    }
  }
  
  private void cancelEnrollment()
  {
    if (mService != null) {
      try
      {
        mService.cancelEnrollment(mToken);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  private int getCurrentUserId()
  {
    try
    {
      int i = getServicegetCurrentUserid;
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private void sendAcquiredResult(long paramLong, int paramInt1, int paramInt2)
  {
    if (mAuthenticationCallback != null) {
      if (Build.FEATURES.ENABLE_FINGERPRINT_EARLYWAKEUP)
      {
        int i;
        if (paramInt1 == 6) {
          i = paramInt2 + 1000;
        } else {
          i = paramInt1;
        }
        mAuthenticationCallback.onAuthenticationAcquired(i);
      }
      else
      {
        mAuthenticationCallback.onAuthenticationAcquired(paramInt1);
      }
    }
    String str = getAcquiredString(paramInt1, paramInt2);
    if (str == null) {
      return;
    }
    if (paramInt1 == 6) {
      paramInt1 = paramInt2 + 1000;
    }
    if (mEnrollmentCallback != null) {
      mEnrollmentCallback.onEnrollmentHelp(paramInt1, str);
    } else if (mAuthenticationCallback != null) {
      mAuthenticationCallback.onAuthenticationHelp(paramInt1, str);
    }
  }
  
  private void sendAuthenticatedFailed()
  {
    if (mAuthenticationCallback != null) {
      mAuthenticationCallback.onAuthenticationFailed();
    }
  }
  
  private void sendAuthenticatedSucceeded(Fingerprint paramFingerprint, int paramInt)
  {
    if (mAuthenticationCallback != null)
    {
      paramFingerprint = new BiometricAuthenticator.AuthenticationResult(mCryptoObject, paramFingerprint, paramInt);
      mAuthenticationCallback.onAuthenticationSucceeded(paramFingerprint);
    }
  }
  
  private void sendErrorResult(long paramLong, int paramInt1, int paramInt2)
  {
    int i;
    if (paramInt1 == 8) {
      i = paramInt2 + 1000;
    } else {
      i = paramInt1;
    }
    if (mEnrollmentCallback != null) {
      mEnrollmentCallback.onEnrollmentError(i, getErrorString(paramInt1, paramInt2));
    } else if (mAuthenticationCallback != null) {
      mAuthenticationCallback.onAuthenticationError(i, getErrorString(paramInt1, paramInt2));
    } else if (mRemovalCallback != null) {
      mRemovalCallback.onRemovalError(mRemovalFingerprint, i, getErrorString(paramInt1, paramInt2));
    } else if (mEnumerateCallback != null) {
      mEnumerateCallback.onEnumerateError(i, getErrorString(paramInt1, paramInt2));
    }
  }
  
  private void useHandler(Handler paramHandler)
  {
    if (paramHandler != null) {
      mHandler = new MyHandler(paramHandler.getLooper(), null);
    } else if (mHandler.getLooper() != mContext.getMainLooper()) {
      mHandler = new MyHandler(mContext.getMainLooper(), null);
    }
  }
  
  public void addLockoutResetCallback(LockoutResetCallback paramLockoutResetCallback)
  {
    if (mService != null) {
      try
      {
        PowerManager localPowerManager = (PowerManager)mContext.getSystemService(PowerManager.class);
        IFingerprintService localIFingerprintService = mService;
        IFingerprintServiceLockoutResetCallback.Stub local1 = new android/hardware/fingerprint/FingerprintManager$1;
        local1.<init>(this, localPowerManager, paramLockoutResetCallback);
        localIFingerprintService.addLockoutResetCallback(local1);
      }
      catch (RemoteException paramLockoutResetCallback)
      {
        throw paramLockoutResetCallback.rethrowFromSystemServer();
      }
    } else {
      Slog.w("FingerprintManager", "addLockoutResetCallback(): Service not connected!");
    }
  }
  
  public void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, Bundle paramBundle, Executor paramExecutor, IBiometricPromptReceiver paramIBiometricPromptReceiver, BiometricAuthenticator.AuthenticationCallback paramAuthenticationCallback)
  {
    if (paramCryptoObject != null)
    {
      if (paramCancellationSignal != null)
      {
        if (paramBundle != null)
        {
          if (paramExecutor != null)
          {
            if (paramIBiometricPromptReceiver != null)
            {
              if (paramAuthenticationCallback != null)
              {
                authenticate(mContext.getUserId(), paramCryptoObject, paramCancellationSignal, paramBundle, paramExecutor, paramIBiometricPromptReceiver, paramAuthenticationCallback);
                return;
              }
              throw new IllegalArgumentException("Must supply a callback");
            }
            throw new IllegalArgumentException("Must supply a receiver");
          }
          throw new IllegalArgumentException("Must supply an executor");
        }
        throw new IllegalArgumentException("Must supply a bundle");
      }
      throw new IllegalArgumentException("Must supply a cancellation signal");
    }
    throw new IllegalArgumentException("Must supply a crypto object");
  }
  
  @Deprecated
  public void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, int paramInt, AuthenticationCallback paramAuthenticationCallback, Handler paramHandler)
  {
    authenticate(paramCryptoObject, paramCancellationSignal, paramInt, paramAuthenticationCallback, paramHandler, mContext.getUserId());
  }
  
  public void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, int paramInt1, AuthenticationCallback paramAuthenticationCallback, Handler paramHandler, int paramInt2)
  {
    if (paramAuthenticationCallback != null)
    {
      if (paramCancellationSignal != null)
      {
        if (paramCancellationSignal.isCanceled())
        {
          Slog.w("FingerprintManager", "authentication already canceled");
          return;
        }
        paramCancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(paramCryptoObject));
      }
      if (mService != null) {
        try
        {
          useHandler(paramHandler);
          mAuthenticationCallback = paramAuthenticationCallback;
          mCryptoObject = paramCryptoObject;
          long l;
          if (paramCryptoObject != null) {
            l = paramCryptoObject.getOpId();
          } else {
            l = 0L;
          }
          mService.authenticate(mToken, l, paramInt2, mServiceReceiver, paramInt1, mContext.getOpPackageName(), null, null);
        }
        catch (RemoteException paramCryptoObject)
        {
          Slog.w("FingerprintManager", "Remote exception while authenticating: ", paramCryptoObject);
          if (paramAuthenticationCallback != null) {
            paramAuthenticationCallback.onAuthenticationError(1, getErrorString(1, 0));
          }
        }
      }
      return;
    }
    throw new IllegalArgumentException("Must supply an authentication callback");
  }
  
  public void authenticate(CancellationSignal paramCancellationSignal, Bundle paramBundle, Executor paramExecutor, IBiometricPromptReceiver paramIBiometricPromptReceiver, BiometricAuthenticator.AuthenticationCallback paramAuthenticationCallback)
  {
    if (paramCancellationSignal != null)
    {
      if (paramBundle != null)
      {
        if (paramExecutor != null)
        {
          if (paramIBiometricPromptReceiver != null)
          {
            if (paramAuthenticationCallback != null)
            {
              authenticate(mContext.getUserId(), null, paramCancellationSignal, paramBundle, paramExecutor, paramIBiometricPromptReceiver, paramAuthenticationCallback);
              return;
            }
            throw new IllegalArgumentException("Must supply a calback");
          }
          throw new IllegalArgumentException("Must supply a receiver");
        }
        throw new IllegalArgumentException("Must supply an executor");
      }
      throw new IllegalArgumentException("Must supply a bundle");
    }
    throw new IllegalArgumentException("Must supply a cancellation signal");
  }
  
  public void enroll(byte[] paramArrayOfByte, CancellationSignal paramCancellationSignal, int paramInt1, int paramInt2, EnrollmentCallback paramEnrollmentCallback)
  {
    int i = paramInt2;
    if (paramInt2 == -2) {
      i = getCurrentUserId();
    }
    if (paramEnrollmentCallback != null)
    {
      if (paramCancellationSignal != null)
      {
        if (paramCancellationSignal.isCanceled())
        {
          Slog.w("FingerprintManager", "enrollment already canceled");
          return;
        }
        paramCancellationSignal.setOnCancelListener(new OnEnrollCancelListener(null));
      }
      if (mService != null) {
        try
        {
          mEnrollmentCallback = paramEnrollmentCallback;
          mService.enroll(mToken, paramArrayOfByte, i, mServiceReceiver, paramInt1, mContext.getOpPackageName());
        }
        catch (RemoteException paramArrayOfByte)
        {
          Slog.w("FingerprintManager", "Remote exception in enroll: ", paramArrayOfByte);
          if (paramEnrollmentCallback != null) {
            paramEnrollmentCallback.onEnrollmentError(1, getErrorString(1, 0));
          }
        }
      }
      return;
    }
    throw new IllegalArgumentException("Must supply an enrollment callback");
  }
  
  public void enumerate(int paramInt, EnumerateCallback paramEnumerateCallback)
  {
    if (mService != null) {
      try
      {
        mEnumerateCallback = paramEnumerateCallback;
        mService.enumerate(mToken, paramInt, mServiceReceiver);
      }
      catch (RemoteException localRemoteException)
      {
        Slog.w("FingerprintManager", "Remote exception in enumerate: ", localRemoteException);
        if (paramEnumerateCallback != null) {
          paramEnumerateCallback.onEnumerateError(1, getErrorString(1, 0));
        }
      }
    }
  }
  
  public String getAcquiredString(int paramInt1, int paramInt2)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Get acquire info: ");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", vendor code: ");
    ((StringBuilder)localObject).append(paramInt2);
    Slog.i("FingerprintManager", ((StringBuilder)localObject).toString());
    switch (paramInt1)
    {
    default: 
      break;
    case 6: 
      int i = paramInt2 + 1000 - 2000;
      if (i >= 0)
      {
        localObject = mContext.getResources().getStringArray(17236063);
        if (i < localObject.length) {
          return localObject[i];
        }
      }
      break;
    case 5: 
      return mContext.getString(17040000);
    case 4: 
      return mContext.getString(17040001);
    case 3: 
      return mContext.getString(17039997);
    case 2: 
      return mContext.getString(17039998);
    case 1: 
      return mContext.getString(17039999);
    case 0: 
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid acquired message: ");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt2);
    Slog.w("FingerprintManager", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public long getAuthenticatorId()
  {
    if (mService != null) {
      try
      {
        long l = mService.getAuthenticatorId(mContext.getOpPackageName());
        return l;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Slog.w("FingerprintManager", "getAuthenticatorId(): Service not connected!");
    return 0L;
  }
  
  public List<Fingerprint> getEnrolledFingerprints()
  {
    return getEnrolledFingerprints(mContext.getUserId());
  }
  
  public List<Fingerprint> getEnrolledFingerprints(int paramInt)
  {
    if (mService != null) {
      try
      {
        List localList = mService.getEnrolledFingerprints(paramInt, mContext.getOpPackageName());
        return localList;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public String getErrorString(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 6: 
    default: 
      break;
    case 12: 
      return mContext.getString(17040005);
    case 11: 
      return mContext.getString(17040008);
    case 10: 
      return mContext.getString(17040012);
    case 9: 
      return mContext.getString(17040007);
    case 8: 
      localObject = mContext.getResources().getStringArray(17236064);
      if (paramInt2 < localObject.length) {
        return localObject[paramInt2];
      }
      break;
    case 7: 
      return mContext.getString(17040006);
    case 5: 
      return mContext.getString(17040003);
    case 4: 
      return mContext.getString(17040009);
    case 3: 
      return mContext.getString(17040010);
    case 2: 
      return mContext.getString(17040011);
    case 1: 
      return mContext.getString(17040004);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid error message: ");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt2);
    Slog.w("FingerprintManager", ((StringBuilder)localObject).toString());
    return null;
  }
  
  @Deprecated
  public boolean hasEnrolledFingerprints()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.hasEnrolledFingerprints(mContext.getUserId(), mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean hasEnrolledFingerprints(int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.hasEnrolledFingerprints(paramInt, mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  @Deprecated
  public boolean isHardwareDetected()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isHardwareDetected(0L, mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Slog.w("FingerprintManager", "isFingerprintHardwareDetected(): Service not connected!");
    return false;
  }
  
  public int postEnroll()
  {
    int i = 0;
    if (mService != null) {
      try
      {
        i = mService.postEnroll(mToken);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return i;
  }
  
  public long preEnroll()
  {
    long l = 0L;
    if (mService != null) {
      try
      {
        l = mService.preEnroll(mToken);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return l;
  }
  
  public void remove(Fingerprint paramFingerprint, int paramInt, RemovalCallback paramRemovalCallback)
  {
    if (mService != null) {
      try
      {
        mRemovalCallback = paramRemovalCallback;
        mRemovalFingerprint = paramFingerprint;
        mService.remove(mToken, paramFingerprint.getFingerId(), paramFingerprint.getGroupId(), paramInt, mServiceReceiver);
      }
      catch (RemoteException localRemoteException)
      {
        Slog.w("FingerprintManager", "Remote exception in remove: ", localRemoteException);
        if (paramRemovalCallback != null) {
          paramRemovalCallback.onRemovalError(paramFingerprint, 1, getErrorString(1, 0));
        }
      }
    }
  }
  
  public void rename(int paramInt1, int paramInt2, String paramString)
  {
    if (mService != null) {
      try
      {
        mService.rename(paramInt1, paramInt2, paramString);
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    } else {
      Slog.w("FingerprintManager", "rename(): Service not connected!");
    }
  }
  
  public void resetTimeout(byte[] paramArrayOfByte)
  {
    if (mService != null) {
      try
      {
        mService.resetTimeout(paramArrayOfByte);
      }
      catch (RemoteException paramArrayOfByte)
      {
        throw paramArrayOfByte.rethrowFromSystemServer();
      }
    } else {
      Slog.w("FingerprintManager", "resetTimeout(): Service not connected!");
    }
  }
  
  public void setActiveUser(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setActiveUser(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public static abstract class AuthenticationCallback
    extends BiometricAuthenticator.AuthenticationCallback
  {
    public AuthenticationCallback() {}
    
    public void onAuthenticationAcquired(int paramInt) {}
    
    public void onAuthenticationError(int paramInt, CharSequence paramCharSequence) {}
    
    public void onAuthenticationFailed() {}
    
    public void onAuthenticationHelp(int paramInt, CharSequence paramCharSequence) {}
    
    public void onAuthenticationSucceeded(BiometricAuthenticator.AuthenticationResult paramAuthenticationResult)
    {
      onAuthenticationSucceeded(new FingerprintManager.AuthenticationResult((FingerprintManager.CryptoObject)paramAuthenticationResult.getCryptoObject(), (Fingerprint)paramAuthenticationResult.getId(), paramAuthenticationResult.getUserId()));
    }
    
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult paramAuthenticationResult) {}
  }
  
  @Deprecated
  public static class AuthenticationResult
  {
    private FingerprintManager.CryptoObject mCryptoObject;
    private Fingerprint mFingerprint;
    private int mUserId;
    
    public AuthenticationResult(FingerprintManager.CryptoObject paramCryptoObject, Fingerprint paramFingerprint, int paramInt)
    {
      mCryptoObject = paramCryptoObject;
      mFingerprint = paramFingerprint;
      mUserId = paramInt;
    }
    
    public FingerprintManager.CryptoObject getCryptoObject()
    {
      return mCryptoObject;
    }
    
    public Fingerprint getFingerprint()
    {
      return mFingerprint;
    }
    
    public int getUserId()
    {
      return mUserId;
    }
  }
  
  @Deprecated
  public static final class CryptoObject
    extends CryptoObject
  {
    public CryptoObject(Signature paramSignature)
    {
      super();
    }
    
    public CryptoObject(Cipher paramCipher)
    {
      super();
    }
    
    public CryptoObject(Mac paramMac)
    {
      super();
    }
    
    public Cipher getCipher()
    {
      return super.getCipher();
    }
    
    public Mac getMac()
    {
      return super.getMac();
    }
    
    public Signature getSignature()
    {
      return super.getSignature();
    }
  }
  
  public static abstract class EnrollmentCallback
  {
    public EnrollmentCallback() {}
    
    public void onEnrollmentError(int paramInt, CharSequence paramCharSequence) {}
    
    public void onEnrollmentHelp(int paramInt, CharSequence paramCharSequence) {}
    
    public void onEnrollmentProgress(int paramInt) {}
  }
  
  public static abstract class EnumerateCallback
  {
    public EnumerateCallback() {}
    
    public void onEnumerate(Fingerprint paramFingerprint) {}
    
    public void onEnumerateError(int paramInt, CharSequence paramCharSequence) {}
  }
  
  public static abstract class LockoutResetCallback
  {
    public LockoutResetCallback() {}
    
    public void onLockoutReset() {}
  }
  
  private class MyHandler
    extends Handler
  {
    private MyHandler(Context paramContext)
    {
      super();
    }
    
    private MyHandler(Looper paramLooper)
    {
      super();
    }
    
    private void sendEnrollResult(Fingerprint paramFingerprint, int paramInt)
    {
      if (mEnrollmentCallback != null) {
        mEnrollmentCallback.onEnrollmentProgress(paramInt);
      }
    }
    
    private void sendEnumeratedResult(long paramLong, int paramInt1, int paramInt2)
    {
      if (mEnumerateCallback != null) {
        mEnumerateCallback.onEnumerate(new Fingerprint(null, paramInt2, paramInt1, paramLong));
      }
    }
    
    private void sendRemovedResult(Fingerprint paramFingerprint, int paramInt)
    {
      if (mRemovalCallback == null) {
        return;
      }
      if (paramFingerprint == null)
      {
        Slog.e("FingerprintManager", "Received MSG_REMOVED, but fingerprint is null");
        return;
      }
      int i = paramFingerprint.getFingerId();
      int j = mRemovalFingerprint.getFingerId();
      if ((j != 0) && (i != 0) && (i != j))
      {
        paramFingerprint = new StringBuilder();
        paramFingerprint.append("Finger id didn't match: ");
        paramFingerprint.append(i);
        paramFingerprint.append(" != ");
        paramFingerprint.append(j);
        Slog.w("FingerprintManager", paramFingerprint.toString());
        return;
      }
      j = paramFingerprint.getGroupId();
      i = mRemovalFingerprint.getGroupId();
      if (j != i)
      {
        paramFingerprint = new StringBuilder();
        paramFingerprint.append("Group id didn't match: ");
        paramFingerprint.append(j);
        paramFingerprint.append(" != ");
        paramFingerprint.append(i);
        Slog.w("FingerprintManager", paramFingerprint.toString());
        return;
      }
      mRemovalCallback.onRemovalSucceeded(paramFingerprint, paramInt);
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 106: 
        sendEnumeratedResult(((Long)obj).longValue(), arg1, arg2);
        break;
      case 105: 
        sendRemovedResult((Fingerprint)obj, arg1);
        break;
      case 104: 
        FingerprintManager.this.sendErrorResult(((Long)obj).longValue(), arg1, arg2);
        break;
      case 103: 
        FingerprintManager.this.sendAuthenticatedFailed();
        break;
      case 102: 
        FingerprintManager.this.sendAuthenticatedSucceeded((Fingerprint)obj, arg1);
        break;
      case 101: 
        FingerprintManager.this.sendAcquiredResult(((Long)obj).longValue(), arg1, arg2);
        break;
      case 100: 
        sendEnrollResult((Fingerprint)obj, arg1);
      }
    }
  }
  
  private class OnAuthenticationCancelListener
    implements CancellationSignal.OnCancelListener
  {
    private CryptoObject mCrypto;
    
    public OnAuthenticationCancelListener(CryptoObject paramCryptoObject)
    {
      mCrypto = paramCryptoObject;
    }
    
    public void onCancel()
    {
      FingerprintManager.this.cancelAuthentication(mCrypto);
    }
  }
  
  private class OnEnrollCancelListener
    implements CancellationSignal.OnCancelListener
  {
    private OnEnrollCancelListener() {}
    
    public void onCancel()
    {
      FingerprintManager.this.cancelEnrollment();
    }
  }
  
  public static abstract class RemovalCallback
  {
    public RemovalCallback() {}
    
    public void onRemovalError(Fingerprint paramFingerprint, int paramInt, CharSequence paramCharSequence) {}
    
    public void onRemovalSucceeded(Fingerprint paramFingerprint, int paramInt) {}
  }
}
