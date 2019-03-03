package android.speech;

import android.app.Service;
import android.content.Intent;
import android.content.PermissionChecker;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class RecognitionService
  extends Service
{
  private static final boolean DBG = false;
  private static final int MSG_CANCEL = 3;
  private static final int MSG_RESET = 4;
  private static final int MSG_START_LISTENING = 1;
  private static final int MSG_STOP_LISTENING = 2;
  public static final String SERVICE_INTERFACE = "android.speech.RecognitionService";
  public static final String SERVICE_META_DATA = "android.speech";
  private static final String TAG = "RecognitionService";
  private RecognitionServiceBinder mBinder = new RecognitionServiceBinder(this);
  private Callback mCurrentCallback = null;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        RecognitionService.this.dispatchClearCallback();
        break;
      case 3: 
        RecognitionService.this.dispatchCancel((IRecognitionListener)obj);
        break;
      case 2: 
        RecognitionService.this.dispatchStopListening((IRecognitionListener)obj);
        break;
      case 1: 
        paramAnonymousMessage = (RecognitionService.StartListeningArgs)obj;
        RecognitionService.this.dispatchStartListening(mIntent, mListener, mCallingUid);
      }
    }
  };
  
  public RecognitionService() {}
  
  private boolean checkPermissions(IRecognitionListener paramIRecognitionListener)
  {
    if (PermissionChecker.checkCallingOrSelfPermission(this, "android.permission.RECORD_AUDIO") == 0) {
      return true;
    }
    try
    {
      Log.e("RecognitionService", "call for recognition service without RECORD_AUDIO permissions");
      paramIRecognitionListener.onError(9);
    }
    catch (RemoteException paramIRecognitionListener)
    {
      Log.e("RecognitionService", "sending ERROR_INSUFFICIENT_PERMISSIONS message failed", paramIRecognitionListener);
    }
    return false;
  }
  
  private void dispatchCancel(IRecognitionListener paramIRecognitionListener)
  {
    if (mCurrentCallback != null) {
      if (mCurrentCallback.mListener.asBinder() != paramIRecognitionListener.asBinder())
      {
        Log.w("RecognitionService", "cancel called by client who did not call startListening - ignoring");
      }
      else
      {
        onCancel(mCurrentCallback);
        mCurrentCallback = null;
      }
    }
  }
  
  private void dispatchClearCallback()
  {
    mCurrentCallback = null;
  }
  
  private void dispatchStartListening(Intent paramIntent, IRecognitionListener paramIRecognitionListener, int paramInt)
  {
    if (mCurrentCallback == null)
    {
      try
      {
        IBinder localIBinder = paramIRecognitionListener.asBinder();
        IBinder.DeathRecipient local2 = new android/speech/RecognitionService$2;
        local2.<init>(this, paramIRecognitionListener);
        localIBinder.linkToDeath(local2, 0);
        mCurrentCallback = new Callback(paramIRecognitionListener, paramInt, null);
        onStartListening(paramIntent, mCurrentCallback);
      }
      catch (RemoteException paramIntent)
      {
        Log.e("RecognitionService", "dead listener on startListening");
        return;
      }
    }
    else
    {
      try
      {
        paramIRecognitionListener.onError(8);
      }
      catch (RemoteException paramIntent)
      {
        Log.d("RecognitionService", "onError call from startListening failed");
      }
      Log.i("RecognitionService", "concurrent startListening received - ignoring this call");
    }
  }
  
  private void dispatchStopListening(IRecognitionListener paramIRecognitionListener)
  {
    try
    {
      if (mCurrentCallback == null)
      {
        paramIRecognitionListener.onError(5);
        Log.w("RecognitionService", "stopListening called with no preceding startListening - ignoring");
      }
      else if (mCurrentCallback.mListener.asBinder() != paramIRecognitionListener.asBinder())
      {
        paramIRecognitionListener.onError(8);
        Log.w("RecognitionService", "stopListening called by other caller than startListening - ignoring");
      }
      else
      {
        onStopListening(mCurrentCallback);
      }
    }
    catch (RemoteException paramIRecognitionListener)
    {
      Log.d("RecognitionService", "onError call from stopListening failed");
    }
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mBinder;
  }
  
  protected abstract void onCancel(Callback paramCallback);
  
  public void onDestroy()
  {
    mCurrentCallback = null;
    mBinder.clearReference();
    super.onDestroy();
  }
  
  protected abstract void onStartListening(Intent paramIntent, Callback paramCallback);
  
  protected abstract void onStopListening(Callback paramCallback);
  
  public class Callback
  {
    private final int mCallingUid;
    private final IRecognitionListener mListener;
    
    private Callback(IRecognitionListener paramIRecognitionListener, int paramInt)
    {
      mListener = paramIRecognitionListener;
      mCallingUid = paramInt;
    }
    
    public void beginningOfSpeech()
      throws RemoteException
    {
      mListener.onBeginningOfSpeech();
    }
    
    public void bufferReceived(byte[] paramArrayOfByte)
      throws RemoteException
    {
      mListener.onBufferReceived(paramArrayOfByte);
    }
    
    public void endOfSpeech()
      throws RemoteException
    {
      mListener.onEndOfSpeech();
    }
    
    public void error(int paramInt)
      throws RemoteException
    {
      Message.obtain(mHandler, 4).sendToTarget();
      mListener.onError(paramInt);
    }
    
    public int getCallingUid()
    {
      return mCallingUid;
    }
    
    public void partialResults(Bundle paramBundle)
      throws RemoteException
    {
      mListener.onPartialResults(paramBundle);
    }
    
    public void readyForSpeech(Bundle paramBundle)
      throws RemoteException
    {
      mListener.onReadyForSpeech(paramBundle);
    }
    
    public void results(Bundle paramBundle)
      throws RemoteException
    {
      Message.obtain(mHandler, 4).sendToTarget();
      mListener.onResults(paramBundle);
    }
    
    public void rmsChanged(float paramFloat)
      throws RemoteException
    {
      mListener.onRmsChanged(paramFloat);
    }
  }
  
  private static final class RecognitionServiceBinder
    extends IRecognitionService.Stub
  {
    private final WeakReference<RecognitionService> mServiceRef;
    
    public RecognitionServiceBinder(RecognitionService paramRecognitionService)
    {
      mServiceRef = new WeakReference(paramRecognitionService);
    }
    
    public void cancel(IRecognitionListener paramIRecognitionListener)
    {
      RecognitionService localRecognitionService = (RecognitionService)mServiceRef.get();
      if ((localRecognitionService != null) && (localRecognitionService.checkPermissions(paramIRecognitionListener))) {
        mHandler.sendMessage(Message.obtain(mHandler, 3, paramIRecognitionListener));
      }
    }
    
    public void clearReference()
    {
      mServiceRef.clear();
    }
    
    public void startListening(Intent paramIntent, IRecognitionListener paramIRecognitionListener)
    {
      RecognitionService localRecognitionService = (RecognitionService)mServiceRef.get();
      if ((localRecognitionService != null) && (localRecognitionService.checkPermissions(paramIRecognitionListener)))
      {
        Handler localHandler1 = mHandler;
        Handler localHandler2 = mHandler;
        Objects.requireNonNull(localRecognitionService);
        localHandler1.sendMessage(Message.obtain(localHandler2, 1, new RecognitionService.StartListeningArgs(localRecognitionService, paramIntent, paramIRecognitionListener, Binder.getCallingUid())));
      }
    }
    
    public void stopListening(IRecognitionListener paramIRecognitionListener)
    {
      RecognitionService localRecognitionService = (RecognitionService)mServiceRef.get();
      if ((localRecognitionService != null) && (localRecognitionService.checkPermissions(paramIRecognitionListener))) {
        mHandler.sendMessage(Message.obtain(mHandler, 2, paramIRecognitionListener));
      }
    }
  }
  
  private class StartListeningArgs
  {
    public final int mCallingUid;
    public final Intent mIntent;
    public final IRecognitionListener mListener;
    
    public StartListeningArgs(Intent paramIntent, IRecognitionListener paramIRecognitionListener, int paramInt)
    {
      mIntent = paramIntent;
      mListener = paramIRecognitionListener;
      mCallingUid = paramInt;
    }
  }
}
