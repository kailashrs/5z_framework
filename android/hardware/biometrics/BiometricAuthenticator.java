package android.hardware.biometrics;

import android.os.CancellationSignal;
import android.os.Parcelable;
import java.util.concurrent.Executor;

public abstract interface BiometricAuthenticator
{
  public abstract void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, Executor paramExecutor, AuthenticationCallback paramAuthenticationCallback);
  
  public abstract void authenticate(CancellationSignal paramCancellationSignal, Executor paramExecutor, AuthenticationCallback paramAuthenticationCallback);
  
  public static abstract class AuthenticationCallback
  {
    public AuthenticationCallback() {}
    
    public void onAuthenticationAcquired(int paramInt) {}
    
    public void onAuthenticationError(int paramInt, CharSequence paramCharSequence) {}
    
    public void onAuthenticationFailed() {}
    
    public void onAuthenticationHelp(int paramInt, CharSequence paramCharSequence) {}
    
    public void onAuthenticationSucceeded(BiometricAuthenticator.AuthenticationResult paramAuthenticationResult) {}
  }
  
  public static class AuthenticationResult
  {
    private CryptoObject mCryptoObject;
    private BiometricAuthenticator.BiometricIdentifier mIdentifier;
    private int mUserId;
    
    public AuthenticationResult() {}
    
    public AuthenticationResult(CryptoObject paramCryptoObject, BiometricAuthenticator.BiometricIdentifier paramBiometricIdentifier, int paramInt)
    {
      mCryptoObject = paramCryptoObject;
      mIdentifier = paramBiometricIdentifier;
      mUserId = paramInt;
    }
    
    public CryptoObject getCryptoObject()
    {
      return mCryptoObject;
    }
    
    public BiometricAuthenticator.BiometricIdentifier getId()
    {
      return mIdentifier;
    }
    
    public int getUserId()
    {
      return mUserId;
    }
  }
  
  public static abstract class BiometricIdentifier
    implements Parcelable
  {
    public BiometricIdentifier() {}
  }
}
