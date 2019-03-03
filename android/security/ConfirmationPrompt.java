package android.security;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
import java.util.concurrent.Executor;

public class ConfirmationPrompt
{
  private static final String TAG = "ConfirmationPrompt";
  private static final int UI_OPTION_ACCESSIBILITY_INVERTED_FLAG = 1;
  private static final int UI_OPTION_ACCESSIBILITY_MAGNIFIED_FLAG = 2;
  private ConfirmationCallback mCallback;
  private final IBinder mCallbackBinder = new IConfirmationPromptCallback.Stub()
  {
    public void onConfirmationPromptCompleted(final int paramAnonymousInt, final byte[] paramAnonymousArrayOfByte)
      throws RemoteException
    {
      if (mCallback != null)
      {
        final ConfirmationCallback localConfirmationCallback = mCallback;
        Executor localExecutor = mExecutor;
        ConfirmationPrompt.access$002(ConfirmationPrompt.this, null);
        ConfirmationPrompt.access$102(ConfirmationPrompt.this, null);
        if (localExecutor == null) {
          ConfirmationPrompt.this.doCallback(paramAnonymousInt, paramAnonymousArrayOfByte, localConfirmationCallback);
        } else {
          localExecutor.execute(new Runnable()
          {
            public void run()
            {
              ConfirmationPrompt.this.doCallback(paramAnonymousInt, paramAnonymousArrayOfByte, localConfirmationCallback);
            }
          });
        }
      }
    }
  };
  private Context mContext;
  private Executor mExecutor;
  private byte[] mExtraData;
  private final KeyStore mKeyStore = KeyStore.getInstance();
  private CharSequence mPromptText;
  
  private ConfirmationPrompt(Context paramContext, CharSequence paramCharSequence, byte[] paramArrayOfByte)
  {
    mContext = paramContext;
    mPromptText = paramCharSequence;
    mExtraData = paramArrayOfByte;
  }
  
  private void doCallback(int paramInt, byte[] paramArrayOfByte, ConfirmationCallback paramConfirmationCallback)
  {
    if (paramInt != 5) {
      switch (paramInt)
      {
      default: 
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Unexpected responseCode=");
        paramArrayOfByte.append(paramInt);
        paramArrayOfByte.append(" from onConfirmtionPromptCompleted() callback.");
        paramConfirmationCallback.onError(new Exception(paramArrayOfByte.toString()));
        break;
      case 2: 
        paramConfirmationCallback.onCanceled();
        break;
      case 1: 
        paramConfirmationCallback.onDismissed();
        break;
      case 0: 
        paramConfirmationCallback.onConfirmed(paramArrayOfByte);
        break;
      }
    } else {
      paramConfirmationCallback.onError(new Exception("System error returned by ConfirmationUI."));
    }
  }
  
  private int getUiOptionsAsFlags()
  {
    int i = 0;
    int j = 0;
    int k = i;
    try
    {
      ContentResolver localContentResolver = mContext.getContentResolver();
      k = i;
      if (Settings.Secure.getInt(localContentResolver, "accessibility_display_inversion_enabled") == 1) {
        j = 0x0 | 0x1;
      }
      k = j;
      float f = Settings.System.getFloat(localContentResolver, "font_scale");
      k = j;
      if (f > 1.0D) {
        k = j | 0x2;
      }
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
      Log.w("ConfirmationPrompt", "Unexpected SettingNotFoundException");
    }
    return k;
  }
  
  private static boolean isAccessibilityServiceRunning(Context paramContext)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      int i = Settings.Secure.getInt(paramContext.getContentResolver(), "accessibility_enabled");
      if (i == 1) {
        bool2 = true;
      }
    }
    catch (Settings.SettingNotFoundException paramContext)
    {
      Log.w("ConfirmationPrompt", "Unexpected SettingNotFoundException");
      paramContext.printStackTrace();
      bool2 = bool1;
    }
    return bool2;
  }
  
  public static boolean isSupported(Context paramContext)
  {
    if (isAccessibilityServiceRunning(paramContext)) {
      return false;
    }
    return KeyStore.getInstance().isConfirmationPromptSupported();
  }
  
  public void cancelPrompt()
  {
    int i = mKeyStore.cancelConfirmationPrompt(mCallbackBinder);
    if (i == 0) {
      return;
    }
    if (i == 3) {
      throw new IllegalStateException();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected responseCode=");
    localStringBuilder.append(i);
    localStringBuilder.append(" from cancelConfirmationPrompt() call.");
    Log.w("ConfirmationPrompt", localStringBuilder.toString());
    throw new IllegalStateException();
  }
  
  public void presentPrompt(Executor paramExecutor, ConfirmationCallback paramConfirmationCallback)
    throws ConfirmationAlreadyPresentingException, ConfirmationNotAvailableException
  {
    if (mCallback == null)
    {
      if (!isAccessibilityServiceRunning(mContext))
      {
        mCallback = paramConfirmationCallback;
        mExecutor = paramExecutor;
        int i = getUiOptionsAsFlags();
        paramExecutor = Locale.getDefault().toLanguageTag();
        i = mKeyStore.presentConfirmationPrompt(mCallbackBinder, mPromptText.toString(), mExtraData, paramExecutor, i);
        if (i != 0)
        {
          if (i != 3)
          {
            if (i != 6)
            {
              if (i != 65536)
              {
                paramExecutor = new StringBuilder();
                paramExecutor.append("Unexpected responseCode=");
                paramExecutor.append(i);
                paramExecutor.append(" from presentConfirmationPrompt() call.");
                Log.w("ConfirmationPrompt", paramExecutor.toString());
                throw new IllegalArgumentException();
              }
              throw new IllegalArgumentException();
            }
            throw new ConfirmationNotAvailableException();
          }
          throw new ConfirmationAlreadyPresentingException();
        }
        return;
      }
      throw new ConfirmationNotAvailableException();
    }
    throw new ConfirmationAlreadyPresentingException();
  }
  
  public static final class Builder
  {
    private Context mContext;
    private byte[] mExtraData;
    private CharSequence mPromptText;
    
    public Builder(Context paramContext)
    {
      mContext = paramContext;
    }
    
    public ConfirmationPrompt build()
    {
      if (!TextUtils.isEmpty(mPromptText))
      {
        if (mExtraData != null) {
          return new ConfirmationPrompt(mContext, mPromptText, mExtraData, null);
        }
        throw new IllegalArgumentException("extraData must be set");
      }
      throw new IllegalArgumentException("prompt text must be set and non-empty");
    }
    
    public Builder setExtraData(byte[] paramArrayOfByte)
    {
      mExtraData = paramArrayOfByte;
      return this;
    }
    
    public Builder setPromptText(CharSequence paramCharSequence)
    {
      mPromptText = paramCharSequence;
      return this;
    }
  }
}
