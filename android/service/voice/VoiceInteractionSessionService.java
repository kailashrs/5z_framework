package android.service.voice;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.app.IVoiceInteractionManagerService.Stub;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class VoiceInteractionSessionService
  extends Service
{
  static final int MSG_NEW_SESSION = 1;
  HandlerCaller mHandlerCaller;
  final HandlerCaller.Callback mHandlerCallerCallback = new HandlerCaller.Callback()
  {
    public void executeMessage(Message paramAnonymousMessage)
    {
      SomeArgs localSomeArgs = (SomeArgs)obj;
      if (what == 1) {
        doNewSession((IBinder)arg1, (Bundle)arg2, argi1);
      }
    }
  };
  IVoiceInteractionSessionService mInterface = new IVoiceInteractionSessionService.Stub()
  {
    public void newSession(IBinder paramAnonymousIBinder, Bundle paramAnonymousBundle, int paramAnonymousInt)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(1, paramAnonymousInt, paramAnonymousIBinder, paramAnonymousBundle));
    }
  };
  VoiceInteractionSession mSession;
  IVoiceInteractionManagerService mSystemService;
  
  public VoiceInteractionSessionService() {}
  
  void doNewSession(IBinder paramIBinder, Bundle paramBundle, int paramInt)
  {
    if (mSession != null)
    {
      mSession.doDestroy();
      mSession = null;
    }
    mSession = onNewSession(paramBundle);
    try
    {
      mSystemService.deliverNewSession(paramIBinder, mSession.mSession, mSession.mInteractor);
      mSession.doCreate(mSystemService, paramIBinder);
    }
    catch (RemoteException paramIBinder) {}
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (mSession == null)
    {
      paramPrintWriter.println("(no active session)");
    }
    else
    {
      paramPrintWriter.println("VoiceInteractionSession:");
      mSession.dump("  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mInterface.asBinder();
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    if (mSession != null) {
      mSession.onConfigurationChanged(paramConfiguration);
    }
  }
  
  public void onCreate()
  {
    super.onCreate();
    mSystemService = IVoiceInteractionManagerService.Stub.asInterface(ServiceManager.getService("voiceinteraction"));
    mHandlerCaller = new HandlerCaller(this, Looper.myLooper(), mHandlerCallerCallback, true);
  }
  
  public void onLowMemory()
  {
    super.onLowMemory();
    if (mSession != null) {
      mSession.onLowMemory();
    }
  }
  
  public abstract VoiceInteractionSession onNewSession(Bundle paramBundle);
  
  public void onTrimMemory(int paramInt)
  {
    super.onTrimMemory(paramInt);
    if (mSession != null) {
      mSession.onTrimMemory(paramInt);
    }
  }
}
