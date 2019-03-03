package android.hardware.biometrics;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import java.security.Signature;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class BiometricPrompt
  implements BiometricAuthenticator, BiometricConstants
{
  public static final int DISMISSED_REASON_NEGATIVE = 2;
  public static final int DISMISSED_REASON_POSITIVE = 1;
  public static final int DISMISSED_REASON_USER_CANCEL = 3;
  public static final int HIDE_DIALOG_DELAY = 2000;
  public static final String KEY_DESCRIPTION = "description";
  public static final String KEY_NEGATIVE_TEXT = "negative_text";
  public static final String KEY_POSITIVE_TEXT = "positive_text";
  public static final String KEY_SUBTITLE = "subtitle";
  public static final String KEY_TITLE = "title";
  private Bundle mBundle;
  IBiometricPromptReceiver mDialogReceiver = new IBiometricPromptReceiver.Stub()
  {
    public void onDialogDismissed(int paramAnonymousInt)
    {
      if (paramAnonymousInt == 1) {
        mPositiveButtonInfo.executor.execute(new _..Lambda.BiometricPrompt.1.C3fuslKNv7eJTZG9_jFRfCo5_Y4(this));
      } else if (paramAnonymousInt == 2) {
        mNegativeButtonInfo.executor.execute(new _..Lambda.BiometricPrompt.1.J5PqpiT8xZNiNN1gy9VraVgknaQ(this));
      }
    }
  };
  private FingerprintManager mFingerprintManager;
  private ButtonInfo mNegativeButtonInfo;
  private PackageManager mPackageManager;
  private ButtonInfo mPositiveButtonInfo;
  
  private BiometricPrompt(Context paramContext, Bundle paramBundle, ButtonInfo paramButtonInfo1, ButtonInfo paramButtonInfo2)
  {
    mBundle = paramBundle;
    mPositiveButtonInfo = paramButtonInfo1;
    mNegativeButtonInfo = paramButtonInfo2;
    mFingerprintManager = ((FingerprintManager)paramContext.getSystemService(FingerprintManager.class));
    mPackageManager = paramContext.getPackageManager();
  }
  
  private boolean handlePreAuthenticationErrors(AuthenticationCallback paramAuthenticationCallback, Executor paramExecutor)
  {
    if (!mPackageManager.hasSystemFeature("android.hardware.fingerprint"))
    {
      sendError(12, paramAuthenticationCallback, paramExecutor);
      return true;
    }
    if (!mFingerprintManager.isHardwareDetected())
    {
      sendError(1, paramAuthenticationCallback, paramExecutor);
      return true;
    }
    if (!mFingerprintManager.hasEnrolledFingerprints())
    {
      sendError(11, paramAuthenticationCallback, paramExecutor);
      return true;
    }
    return false;
  }
  
  private void sendError(int paramInt, AuthenticationCallback paramAuthenticationCallback, Executor paramExecutor)
  {
    paramExecutor.execute(new _..Lambda.BiometricPrompt.HqBGXtBUWNc_v8NoHYsj2gLfaRw(this, paramAuthenticationCallback, paramInt));
  }
  
  public void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, Executor paramExecutor, AuthenticationCallback paramAuthenticationCallback)
  {
    if (handlePreAuthenticationErrors(paramAuthenticationCallback, paramExecutor)) {
      return;
    }
    mFingerprintManager.authenticate(paramCryptoObject, paramCancellationSignal, mBundle, paramExecutor, mDialogReceiver, paramAuthenticationCallback);
  }
  
  public void authenticate(CryptoObject paramCryptoObject, CancellationSignal paramCancellationSignal, Executor paramExecutor, BiometricAuthenticator.AuthenticationCallback paramAuthenticationCallback)
  {
    if ((paramAuthenticationCallback instanceof AuthenticationCallback))
    {
      authenticate(paramCryptoObject, paramCancellationSignal, paramExecutor, (AuthenticationCallback)paramAuthenticationCallback);
      return;
    }
    throw new IllegalArgumentException("Callback cannot be casted");
  }
  
  public void authenticate(CancellationSignal paramCancellationSignal, Executor paramExecutor, BiometricAuthenticator.AuthenticationCallback paramAuthenticationCallback)
  {
    if ((paramAuthenticationCallback instanceof AuthenticationCallback))
    {
      authenticate(paramCancellationSignal, paramExecutor, (AuthenticationCallback)paramAuthenticationCallback);
      return;
    }
    throw new IllegalArgumentException("Callback cannot be casted");
  }
  
  public void authenticate(CancellationSignal paramCancellationSignal, Executor paramExecutor, AuthenticationCallback paramAuthenticationCallback)
  {
    if (handlePreAuthenticationErrors(paramAuthenticationCallback, paramExecutor)) {
      return;
    }
    mFingerprintManager.authenticate(paramCancellationSignal, mBundle, paramExecutor, mDialogReceiver, paramAuthenticationCallback);
  }
  
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
      onAuthenticationSucceeded(new BiometricPrompt.AuthenticationResult((BiometricPrompt.CryptoObject)paramAuthenticationResult.getCryptoObject(), paramAuthenticationResult.getId(), paramAuthenticationResult.getUserId()));
    }
    
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult paramAuthenticationResult) {}
  }
  
  public static class AuthenticationResult
    extends BiometricAuthenticator.AuthenticationResult
  {
    public AuthenticationResult(BiometricPrompt.CryptoObject paramCryptoObject, BiometricAuthenticator.BiometricIdentifier paramBiometricIdentifier, int paramInt)
    {
      super(paramBiometricIdentifier, paramInt);
    }
    
    public BiometricPrompt.CryptoObject getCryptoObject()
    {
      return (BiometricPrompt.CryptoObject)super.getCryptoObject();
    }
  }
  
  public static class Builder
  {
    private final Bundle mBundle = new Bundle();
    private Context mContext;
    private BiometricPrompt.ButtonInfo mNegativeButtonInfo;
    private BiometricPrompt.ButtonInfo mPositiveButtonInfo;
    
    public Builder(Context paramContext)
    {
      mContext = paramContext;
    }
    
    public BiometricPrompt build()
    {
      CharSequence localCharSequence1 = mBundle.getCharSequence("title");
      CharSequence localCharSequence2 = mBundle.getCharSequence("negative_text");
      if (!TextUtils.isEmpty(localCharSequence1))
      {
        if (!TextUtils.isEmpty(localCharSequence2)) {
          return new BiometricPrompt(mContext, mBundle, mPositiveButtonInfo, mNegativeButtonInfo, null);
        }
        throw new IllegalArgumentException("Negative text must be set and non-empty");
      }
      throw new IllegalArgumentException("Title must be set and non-empty");
    }
    
    public Builder setDescription(CharSequence paramCharSequence)
    {
      mBundle.putCharSequence("description", paramCharSequence);
      return this;
    }
    
    public Builder setNegativeButton(CharSequence paramCharSequence, Executor paramExecutor, DialogInterface.OnClickListener paramOnClickListener)
    {
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        if (paramExecutor != null)
        {
          if (paramOnClickListener != null)
          {
            mBundle.putCharSequence("negative_text", paramCharSequence);
            mNegativeButtonInfo = new BiometricPrompt.ButtonInfo(paramExecutor, paramOnClickListener);
            return this;
          }
          throw new IllegalArgumentException("Listener must not be null");
        }
        throw new IllegalArgumentException("Executor must not be null");
      }
      throw new IllegalArgumentException("Text must be set and non-empty");
    }
    
    public Builder setPositiveButton(CharSequence paramCharSequence, Executor paramExecutor, DialogInterface.OnClickListener paramOnClickListener)
    {
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        if (paramExecutor != null)
        {
          if (paramOnClickListener != null)
          {
            mBundle.putCharSequence("positive_text", paramCharSequence);
            mPositiveButtonInfo = new BiometricPrompt.ButtonInfo(paramExecutor, paramOnClickListener);
            return this;
          }
          throw new IllegalArgumentException("Listener must not be null");
        }
        throw new IllegalArgumentException("Executor must not be null");
      }
      throw new IllegalArgumentException("Text must be set and non-empty");
    }
    
    public Builder setSubtitle(CharSequence paramCharSequence)
    {
      mBundle.putCharSequence("subtitle", paramCharSequence);
      return this;
    }
    
    public Builder setTitle(CharSequence paramCharSequence)
    {
      mBundle.putCharSequence("title", paramCharSequence);
      return this;
    }
  }
  
  private static class ButtonInfo
  {
    Executor executor;
    DialogInterface.OnClickListener listener;
    
    ButtonInfo(Executor paramExecutor, DialogInterface.OnClickListener paramOnClickListener)
    {
      executor = paramExecutor;
      listener = paramOnClickListener;
    }
  }
  
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
}
