package android.service.voice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.soundtrigger.KeyphraseEnrollmentInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.app.IVoiceInteractionManagerService.Stub;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Locale;

public class VoiceInteractionService
  extends Service
{
  static final int MSG_LAUNCH_VOICE_ASSIST_FROM_KEYGUARD = 4;
  static final int MSG_READY = 1;
  static final int MSG_SHUTDOWN = 2;
  static final int MSG_SOUND_MODELS_CHANGED = 3;
  public static final String SERVICE_INTERFACE = "android.service.voice.VoiceInteractionService";
  public static final String SERVICE_META_DATA = "android.voice_interaction";
  MyHandler mHandler;
  private AlwaysOnHotwordDetector mHotwordDetector;
  IVoiceInteractionService mInterface = new IVoiceInteractionService.Stub()
  {
    public void launchVoiceAssistFromKeyguard()
      throws RemoteException
    {
      mHandler.sendEmptyMessage(4);
    }
    
    public void ready()
    {
      mHandler.sendEmptyMessage(1);
    }
    
    public void shutdown()
    {
      mHandler.sendEmptyMessage(2);
    }
    
    public void soundModelsChanged()
    {
      mHandler.sendEmptyMessage(3);
    }
  };
  private KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;
  private final Object mLock = new Object();
  IVoiceInteractionManagerService mSystemService;
  
  public VoiceInteractionService() {}
  
  public static boolean isActiveService(Context paramContext, ComponentName paramComponentName)
  {
    paramContext = Settings.Secure.getString(paramContext.getContentResolver(), "voice_interaction_service");
    if ((paramContext != null) && (!paramContext.isEmpty()))
    {
      paramContext = ComponentName.unflattenFromString(paramContext);
      if (paramContext == null) {
        return false;
      }
      return paramContext.equals(paramComponentName);
    }
    return false;
  }
  
  private void onShutdownInternal()
  {
    onShutdown();
    safelyShutdownHotwordDetector();
  }
  
  private void onSoundModelsChangedInternal()
  {
    try
    {
      if (mHotwordDetector != null) {
        mHotwordDetector.onSoundModelsChanged();
      }
      return;
    }
    finally {}
  }
  
  private void safelyShutdownHotwordDetector()
  {
    try
    {
      synchronized (mLock)
      {
        if (mHotwordDetector != null)
        {
          mHotwordDetector.stopRecognition();
          mHotwordDetector.invalidate();
          mHotwordDetector = null;
        }
      }
      return;
    }
    catch (Exception localException) {}
  }
  
  public final AlwaysOnHotwordDetector createAlwaysOnHotwordDetector(String paramString, Locale paramLocale, AlwaysOnHotwordDetector.Callback paramCallback)
  {
    if (mSystemService != null) {
      synchronized (mLock)
      {
        safelyShutdownHotwordDetector();
        AlwaysOnHotwordDetector localAlwaysOnHotwordDetector = new android/service/voice/AlwaysOnHotwordDetector;
        localAlwaysOnHotwordDetector.<init>(paramString, paramLocale, paramCallback, mKeyphraseEnrollmentInfo, mInterface, mSystemService);
        mHotwordDetector = localAlwaysOnHotwordDetector;
        return mHotwordDetector;
      }
    }
    throw new IllegalStateException("Not available until onReady() is called");
  }
  
  protected void dump(FileDescriptor arg1, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("VOICE INTERACTION");
    synchronized (mLock)
    {
      paramPrintWriter.println("  AlwaysOnHotwordDetector");
      if (mHotwordDetector == null) {
        paramPrintWriter.println("    NULL");
      } else {
        mHotwordDetector.dump("    ", paramPrintWriter);
      }
      return;
    }
  }
  
  public int getDisabledShowContext()
  {
    try
    {
      int i = mSystemService.getDisabledShowContext();
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return 0;
  }
  
  @VisibleForTesting
  protected final KeyphraseEnrollmentInfo getKeyphraseEnrollmentInfo()
  {
    return mKeyphraseEnrollmentInfo;
  }
  
  public final boolean isKeyphraseAndLocaleSupportedForHotword(String paramString, Locale paramLocale)
  {
    KeyphraseEnrollmentInfo localKeyphraseEnrollmentInfo = mKeyphraseEnrollmentInfo;
    boolean bool = false;
    if (localKeyphraseEnrollmentInfo == null) {
      return false;
    }
    if (mKeyphraseEnrollmentInfo.getKeyphraseMetadata(paramString, paramLocale) != null) {
      bool = true;
    }
    return bool;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.service.voice.VoiceInteractionService".equals(paramIntent.getAction())) {
      return mInterface.asBinder();
    }
    return null;
  }
  
  public void onCreate()
  {
    super.onCreate();
    mHandler = new MyHandler();
  }
  
  public void onLaunchVoiceAssistFromKeyguard() {}
  
  public void onReady()
  {
    mSystemService = IVoiceInteractionManagerService.Stub.asInterface(ServiceManager.getService("voiceinteraction"));
    mKeyphraseEnrollmentInfo = new KeyphraseEnrollmentInfo(getPackageManager());
  }
  
  public void onShutdown() {}
  
  public void setDisabledShowContext(int paramInt)
  {
    try
    {
      mSystemService.setDisabledShowContext(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void showSession(Bundle paramBundle, int paramInt)
  {
    if (mSystemService != null)
    {
      try
      {
        mSystemService.showSession(mInterface, paramBundle, paramInt);
      }
      catch (RemoteException paramBundle) {}
      return;
    }
    throw new IllegalStateException("Not available until onReady() is called");
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        super.handleMessage(paramMessage);
        break;
      case 4: 
        onLaunchVoiceAssistFromKeyguard();
        break;
      case 3: 
        VoiceInteractionService.this.onSoundModelsChangedInternal();
        break;
      case 2: 
        VoiceInteractionService.this.onShutdownInternal();
        break;
      case 1: 
        onReady();
      }
    }
  }
}
