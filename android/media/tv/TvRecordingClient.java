package android.media.tv;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class TvRecordingClient
{
  private static final boolean DEBUG = false;
  private static final String TAG = "TvRecordingClient";
  private final RecordingCallback mCallback;
  private final Handler mHandler;
  private boolean mIsRecordingStarted;
  private boolean mIsTuned;
  private final Queue<Pair<String, Bundle>> mPendingAppPrivateCommands = new ArrayDeque();
  private TvInputManager.Session mSession;
  private MySessionCallback mSessionCallback;
  private final TvInputManager mTvInputManager;
  
  public TvRecordingClient(Context paramContext, String paramString, RecordingCallback paramRecordingCallback, Handler paramHandler)
  {
    mCallback = paramRecordingCallback;
    if (paramHandler == null) {
      paramHandler = new Handler(Looper.getMainLooper());
    }
    mHandler = paramHandler;
    mTvInputManager = ((TvInputManager)paramContext.getSystemService("tv_input"));
  }
  
  private void resetInternal()
  {
    mSessionCallback = null;
    mPendingAppPrivateCommands.clear();
    if (mSession != null)
    {
      mSession.release();
      mSession = null;
    }
  }
  
  public void release()
  {
    resetInternal();
  }
  
  public void sendAppPrivateCommand(String paramString, Bundle paramBundle)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (mSession != null)
      {
        mSession.sendAppPrivateCommand(paramString, paramBundle);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sendAppPrivateCommand - session not yet created (action \"");
        localStringBuilder.append(paramString);
        localStringBuilder.append("\" pending)");
        Log.w("TvRecordingClient", localStringBuilder.toString());
        mPendingAppPrivateCommands.add(Pair.create(paramString, paramBundle));
      }
      return;
    }
    throw new IllegalArgumentException("action cannot be null or an empty string");
  }
  
  public void startRecording(Uri paramUri)
  {
    if (mIsTuned)
    {
      if (mSession != null)
      {
        mSession.startRecording(paramUri);
        mIsRecordingStarted = true;
      }
      return;
    }
    throw new IllegalStateException("startRecording failed - not yet tuned");
  }
  
  public void stopRecording()
  {
    if (!mIsRecordingStarted) {
      Log.w("TvRecordingClient", "stopRecording failed - recording not yet started");
    }
    if (mSession != null) {
      mSession.stopRecording();
    }
  }
  
  public void tune(String paramString, Uri paramUri)
  {
    tune(paramString, paramUri, null);
  }
  
  public void tune(String paramString, Uri paramUri, Bundle paramBundle)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (!mIsRecordingStarted)
      {
        if ((mSessionCallback != null) && (TextUtils.equals(mSessionCallback.mInputId, paramString)))
        {
          if (mSession != null)
          {
            mSession.tune(paramUri, paramBundle);
          }
          else
          {
            mSessionCallback.mChannelUri = paramUri;
            mSessionCallback.mConnectionParams = paramBundle;
          }
        }
        else
        {
          resetInternal();
          mSessionCallback = new MySessionCallback(paramString, paramUri, paramBundle);
          if (mTvInputManager != null) {
            mTvInputManager.createRecordingSession(paramString, mSessionCallback, mHandler);
          }
        }
        return;
      }
      throw new IllegalStateException("tune failed - recording already started");
    }
    throw new IllegalArgumentException("inputId cannot be null or an empty string");
  }
  
  private class MySessionCallback
    extends TvInputManager.SessionCallback
  {
    Uri mChannelUri;
    Bundle mConnectionParams;
    final String mInputId;
    
    MySessionCallback(String paramString, Uri paramUri, Bundle paramBundle)
    {
      mInputId = paramString;
      mChannelUri = paramUri;
      mConnectionParams = paramBundle;
    }
    
    public void onError(TvInputManager.Session paramSession, int paramInt)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onError - session not created");
        return;
      }
      mCallback.onError(paramInt);
    }
    
    public void onRecordingStopped(TvInputManager.Session paramSession, Uri paramUri)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onRecordingStopped - session not created");
        return;
      }
      TvRecordingClient.access$502(TvRecordingClient.this, false);
      mCallback.onRecordingStopped(paramUri);
    }
    
    public void onSessionCreated(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onSessionCreated - session already created");
        if (paramSession != null) {
          paramSession.release();
        }
        return;
      }
      TvRecordingClient.access$102(TvRecordingClient.this, paramSession);
      if (paramSession != null)
      {
        paramSession = mPendingAppPrivateCommands.iterator();
        while (paramSession.hasNext())
        {
          Pair localPair = (Pair)paramSession.next();
          mSession.sendAppPrivateCommand((String)first, (Bundle)second);
        }
        mPendingAppPrivateCommands.clear();
        mSession.tune(mChannelUri, mConnectionParams);
      }
      else
      {
        TvRecordingClient.access$002(TvRecordingClient.this, null);
        if (mCallback != null) {
          mCallback.onConnectionFailed(mInputId);
        }
      }
    }
    
    public void onSessionEvent(TvInputManager.Session paramSession, String paramString, Bundle paramBundle)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onSessionEvent - session not created");
        return;
      }
      if (mCallback != null) {
        mCallback.onEvent(mInputId, paramString, paramBundle);
      }
    }
    
    public void onSessionReleased(TvInputManager.Session paramSession)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onSessionReleased - session not created");
        return;
      }
      TvRecordingClient.access$402(TvRecordingClient.this, false);
      TvRecordingClient.access$502(TvRecordingClient.this, false);
      TvRecordingClient.access$002(TvRecordingClient.this, null);
      TvRecordingClient.access$102(TvRecordingClient.this, null);
      if (mCallback != null) {
        mCallback.onDisconnected(mInputId);
      }
    }
    
    void onTuned(TvInputManager.Session paramSession, Uri paramUri)
    {
      if (this != mSessionCallback)
      {
        Log.w("TvRecordingClient", "onTuned - session not created");
        return;
      }
      TvRecordingClient.access$402(TvRecordingClient.this, true);
      mCallback.onTuned(paramUri);
    }
  }
  
  public static abstract class RecordingCallback
  {
    public RecordingCallback() {}
    
    public void onConnectionFailed(String paramString) {}
    
    public void onDisconnected(String paramString) {}
    
    public void onError(int paramInt) {}
    
    @SystemApi
    public void onEvent(String paramString1, String paramString2, Bundle paramBundle) {}
    
    public void onRecordingStopped(Uri paramUri) {}
    
    public void onTuned(Uri paramUri) {}
  }
}
