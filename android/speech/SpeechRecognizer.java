package android.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.util.SeempLog;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SpeechRecognizer
{
  public static final String CONFIDENCE_SCORES = "confidence_scores";
  private static final boolean DBG = false;
  public static final int ERROR_AUDIO = 3;
  public static final int ERROR_CLIENT = 5;
  public static final int ERROR_INSUFFICIENT_PERMISSIONS = 9;
  public static final int ERROR_NETWORK = 2;
  public static final int ERROR_NETWORK_TIMEOUT = 1;
  public static final int ERROR_NO_MATCH = 7;
  public static final int ERROR_RECOGNIZER_BUSY = 8;
  public static final int ERROR_SERVER = 4;
  public static final int ERROR_SPEECH_TIMEOUT = 6;
  private static final int MSG_CANCEL = 3;
  private static final int MSG_CHANGE_LISTENER = 4;
  private static final int MSG_START = 1;
  private static final int MSG_STOP = 2;
  public static final String RESULTS_RECOGNITION = "results_recognition";
  private static final String TAG = "SpeechRecognizer";
  private Connection mConnection;
  private final Context mContext;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        SpeechRecognizer.this.handleChangeListener((RecognitionListener)obj);
        break;
      case 3: 
        SpeechRecognizer.this.handleCancelMessage();
        break;
      case 2: 
        SpeechRecognizer.this.handleStopMessage();
        break;
      case 1: 
        SpeechRecognizer.this.handleStartListening((Intent)obj);
      }
    }
  };
  private final InternalListener mListener = new InternalListener(null);
  private final Queue<Message> mPendingTasks = new LinkedList();
  private IRecognitionService mService;
  private final ComponentName mServiceComponent;
  
  private SpeechRecognizer(Context paramContext, ComponentName paramComponentName)
  {
    mContext = paramContext;
    mServiceComponent = paramComponentName;
  }
  
  private static void checkIsCalledFromMainThread()
  {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      return;
    }
    throw new RuntimeException("SpeechRecognizer should be used only from the application's main thread");
  }
  
  private boolean checkOpenConnection()
  {
    if (mService != null) {
      return true;
    }
    mListener.onError(5);
    Log.e("SpeechRecognizer", "not connected to the recognition service");
    return false;
  }
  
  public static SpeechRecognizer createSpeechRecognizer(Context paramContext)
  {
    return createSpeechRecognizer(paramContext, null);
  }
  
  public static SpeechRecognizer createSpeechRecognizer(Context paramContext, ComponentName paramComponentName)
  {
    if (paramContext != null)
    {
      checkIsCalledFromMainThread();
      return new SpeechRecognizer(paramContext, paramComponentName);
    }
    throw new IllegalArgumentException("Context cannot be null)");
  }
  
  private void handleCancelMessage()
  {
    if (!checkOpenConnection()) {
      return;
    }
    try
    {
      mService.cancel(mListener);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SpeechRecognizer", "cancel() failed", localRemoteException);
      mListener.onError(5);
    }
  }
  
  private void handleChangeListener(RecognitionListener paramRecognitionListener)
  {
    InternalListener.access$1002(mListener, paramRecognitionListener);
  }
  
  private void handleStartListening(Intent paramIntent)
  {
    if (!checkOpenConnection()) {
      return;
    }
    try
    {
      mService.startListening(paramIntent, mListener);
    }
    catch (RemoteException paramIntent)
    {
      Log.e("SpeechRecognizer", "startListening() failed", paramIntent);
      mListener.onError(5);
    }
  }
  
  private void handleStopMessage()
  {
    if (!checkOpenConnection()) {
      return;
    }
    try
    {
      mService.stopListening(mListener);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SpeechRecognizer", "stopListening() failed", localRemoteException);
      mListener.onError(5);
    }
  }
  
  public static boolean isRecognitionAvailable(Context paramContext)
  {
    paramContext = paramContext.getPackageManager();
    Intent localIntent = new Intent("android.speech.RecognitionService");
    boolean bool1 = false;
    paramContext = paramContext.queryIntentServices(localIntent, 0);
    boolean bool2 = bool1;
    if (paramContext != null)
    {
      bool2 = bool1;
      if (paramContext.size() != 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private void putMessage(Message paramMessage)
  {
    if (mService == null) {
      mPendingTasks.offer(paramMessage);
    } else {
      mHandler.sendMessage(paramMessage);
    }
  }
  
  public void cancel()
  {
    checkIsCalledFromMainThread();
    putMessage(Message.obtain(mHandler, 3));
  }
  
  public void destroy()
  {
    if (mService != null) {
      try
      {
        mService.cancel(mListener);
      }
      catch (RemoteException localRemoteException) {}
    }
    if (mConnection != null) {
      mContext.unbindService(mConnection);
    }
    mPendingTasks.clear();
    mService = null;
    mConnection = null;
    InternalListener.access$1002(mListener, null);
  }
  
  public void setRecognitionListener(RecognitionListener paramRecognitionListener)
  {
    checkIsCalledFromMainThread();
    putMessage(Message.obtain(mHandler, 4, paramRecognitionListener));
  }
  
  public void startListening(Intent paramIntent)
  {
    SeempLog.record(72);
    if (paramIntent != null)
    {
      checkIsCalledFromMainThread();
      if (mConnection == null)
      {
        mConnection = new Connection(null);
        Intent localIntent = new Intent("android.speech.RecognitionService");
        if (mServiceComponent == null)
        {
          String str = Settings.Secure.getString(mContext.getContentResolver(), "voice_recognition_service");
          if (TextUtils.isEmpty(str))
          {
            Log.e("SpeechRecognizer", "no selected voice recognition service");
            mListener.onError(5);
            return;
          }
          localIntent.setComponent(ComponentName.unflattenFromString(str));
        }
        else
        {
          localIntent.setComponent(mServiceComponent);
        }
        if (!mContext.bindService(localIntent, mConnection, 1))
        {
          Log.e("SpeechRecognizer", "bind to recognition service failed");
          mConnection = null;
          mService = null;
          mListener.onError(5);
          return;
        }
      }
      putMessage(Message.obtain(mHandler, 1, paramIntent));
      return;
    }
    throw new IllegalArgumentException("intent must not be null");
  }
  
  public void stopListening()
  {
    checkIsCalledFromMainThread();
    putMessage(Message.obtain(mHandler, 2));
  }
  
  private class Connection
    implements ServiceConnection
  {
    private Connection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      SpeechRecognizer.access$502(SpeechRecognizer.this, IRecognitionService.Stub.asInterface(paramIBinder));
      while (!mPendingTasks.isEmpty()) {
        mHandler.sendMessage((Message)mPendingTasks.poll());
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      SpeechRecognizer.access$502(SpeechRecognizer.this, null);
      SpeechRecognizer.access$802(SpeechRecognizer.this, null);
      mPendingTasks.clear();
    }
  }
  
  private static class InternalListener
    extends IRecognitionListener.Stub
  {
    private static final int MSG_BEGINNING_OF_SPEECH = 1;
    private static final int MSG_BUFFER_RECEIVED = 2;
    private static final int MSG_END_OF_SPEECH = 3;
    private static final int MSG_ERROR = 4;
    private static final int MSG_ON_EVENT = 9;
    private static final int MSG_PARTIAL_RESULTS = 7;
    private static final int MSG_READY_FOR_SPEECH = 5;
    private static final int MSG_RESULTS = 6;
    private static final int MSG_RMS_CHANGED = 8;
    private final Handler mInternalHandler = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        if (mInternalListener == null) {
          return;
        }
        switch (what)
        {
        default: 
          break;
        case 9: 
          mInternalListener.onEvent(arg1, (Bundle)obj);
          break;
        case 8: 
          mInternalListener.onRmsChanged(((Float)obj).floatValue());
          break;
        case 7: 
          mInternalListener.onPartialResults((Bundle)obj);
          break;
        case 6: 
          mInternalListener.onResults((Bundle)obj);
          break;
        case 5: 
          mInternalListener.onReadyForSpeech((Bundle)obj);
          break;
        case 4: 
          mInternalListener.onError(((Integer)obj).intValue());
          break;
        case 3: 
          mInternalListener.onEndOfSpeech();
          break;
        case 2: 
          mInternalListener.onBufferReceived((byte[])obj);
          break;
        case 1: 
          mInternalListener.onBeginningOfSpeech();
        }
      }
    };
    private RecognitionListener mInternalListener;
    
    private InternalListener() {}
    
    public void onBeginningOfSpeech()
    {
      Message.obtain(mInternalHandler, 1).sendToTarget();
    }
    
    public void onBufferReceived(byte[] paramArrayOfByte)
    {
      Message.obtain(mInternalHandler, 2, paramArrayOfByte).sendToTarget();
    }
    
    public void onEndOfSpeech()
    {
      Message.obtain(mInternalHandler, 3).sendToTarget();
    }
    
    public void onError(int paramInt)
    {
      Message.obtain(mInternalHandler, 4, Integer.valueOf(paramInt)).sendToTarget();
    }
    
    public void onEvent(int paramInt, Bundle paramBundle)
    {
      Message.obtain(mInternalHandler, 9, paramInt, paramInt, paramBundle).sendToTarget();
    }
    
    public void onPartialResults(Bundle paramBundle)
    {
      Message.obtain(mInternalHandler, 7, paramBundle).sendToTarget();
    }
    
    public void onReadyForSpeech(Bundle paramBundle)
    {
      Message.obtain(mInternalHandler, 5, paramBundle).sendToTarget();
    }
    
    public void onResults(Bundle paramBundle)
    {
      Message.obtain(mInternalHandler, 6, paramBundle).sendToTarget();
    }
    
    public void onRmsChanged(float paramFloat)
    {
      Message.obtain(mInternalHandler, 8, Float.valueOf(paramFloat)).sendToTarget();
    }
  }
}
