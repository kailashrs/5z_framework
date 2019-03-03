package android.view.textservice;

import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.textservice.ISpellCheckerSession;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import com.android.internal.textservice.ISpellCheckerSessionListener.Stub;
import com.android.internal.textservice.ITextServicesManager;
import com.android.internal.textservice.ITextServicesSessionListener;
import com.android.internal.textservice.ITextServicesSessionListener.Stub;
import dalvik.system.CloseGuard;
import java.util.LinkedList;
import java.util.Queue;

public class SpellCheckerSession
{
  private static final boolean DBG = false;
  private static final int MSG_ON_GET_SUGGESTION_MULTIPLE = 1;
  private static final int MSG_ON_GET_SUGGESTION_MULTIPLE_FOR_SENTENCE = 2;
  public static final String SERVICE_META_DATA = "android.view.textservice.scs";
  private static final String TAG = SpellCheckerSession.class.getSimpleName();
  private final CloseGuard mGuard = CloseGuard.get();
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 2: 
        SpellCheckerSession.this.handleOnGetSentenceSuggestionsMultiple((SentenceSuggestionsInfo[])obj);
        break;
      case 1: 
        SpellCheckerSession.this.handleOnGetSuggestionsMultiple((SuggestionsInfo[])obj);
      }
    }
  };
  private final InternalListener mInternalListener;
  private final SpellCheckerInfo mSpellCheckerInfo;
  private final SpellCheckerSessionListener mSpellCheckerSessionListener;
  private final SpellCheckerSessionListenerImpl mSpellCheckerSessionListenerImpl;
  private final ITextServicesManager mTextServicesManager;
  
  public SpellCheckerSession(SpellCheckerInfo paramSpellCheckerInfo, ITextServicesManager paramITextServicesManager, SpellCheckerSessionListener paramSpellCheckerSessionListener)
  {
    if ((paramSpellCheckerInfo != null) && (paramSpellCheckerSessionListener != null) && (paramITextServicesManager != null))
    {
      mSpellCheckerInfo = paramSpellCheckerInfo;
      mSpellCheckerSessionListenerImpl = new SpellCheckerSessionListenerImpl(mHandler);
      mInternalListener = new InternalListener(mSpellCheckerSessionListenerImpl);
      mTextServicesManager = paramITextServicesManager;
      mSpellCheckerSessionListener = paramSpellCheckerSessionListener;
      mGuard.open("finishSession");
      return;
    }
    throw new NullPointerException();
  }
  
  private void handleOnGetSentenceSuggestionsMultiple(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo)
  {
    mSpellCheckerSessionListener.onGetSentenceSuggestions(paramArrayOfSentenceSuggestionsInfo);
  }
  
  private void handleOnGetSuggestionsMultiple(SuggestionsInfo[] paramArrayOfSuggestionsInfo)
  {
    mSpellCheckerSessionListener.onGetSuggestions(paramArrayOfSuggestionsInfo);
  }
  
  public void cancel()
  {
    mSpellCheckerSessionListenerImpl.cancel();
  }
  
  public void close()
  {
    mGuard.close();
    try
    {
      mSpellCheckerSessionListenerImpl.close();
      mTextServicesManager.finishSpellCheckerService(mSpellCheckerSessionListenerImpl);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGuard != null)
      {
        mGuard.warnIfOpen();
        close();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void getSentenceSuggestions(TextInfo[] paramArrayOfTextInfo, int paramInt)
  {
    mSpellCheckerSessionListenerImpl.getSentenceSuggestionsMultiple(paramArrayOfTextInfo, paramInt);
  }
  
  public SpellCheckerInfo getSpellChecker()
  {
    return mSpellCheckerInfo;
  }
  
  public ISpellCheckerSessionListener getSpellCheckerSessionListener()
  {
    return mSpellCheckerSessionListenerImpl;
  }
  
  @Deprecated
  public void getSuggestions(TextInfo paramTextInfo, int paramInt)
  {
    getSuggestions(new TextInfo[] { paramTextInfo }, paramInt, false);
  }
  
  @Deprecated
  public void getSuggestions(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
  {
    mSpellCheckerSessionListenerImpl.getSuggestionsMultiple(paramArrayOfTextInfo, paramInt, paramBoolean);
  }
  
  public ITextServicesSessionListener getTextServicesSessionListener()
  {
    return mInternalListener;
  }
  
  public boolean isSessionDisconnected()
  {
    return mSpellCheckerSessionListenerImpl.isDisconnected();
  }
  
  private static final class InternalListener
    extends ITextServicesSessionListener.Stub
  {
    private final SpellCheckerSession.SpellCheckerSessionListenerImpl mParentSpellCheckerSessionListenerImpl;
    
    public InternalListener(SpellCheckerSession.SpellCheckerSessionListenerImpl paramSpellCheckerSessionListenerImpl)
    {
      mParentSpellCheckerSessionListenerImpl = paramSpellCheckerSessionListenerImpl;
    }
    
    public void onServiceConnected(ISpellCheckerSession paramISpellCheckerSession)
    {
      mParentSpellCheckerSessionListenerImpl.onServiceConnected(paramISpellCheckerSession);
    }
  }
  
  public static abstract interface SpellCheckerSessionListener
  {
    public abstract void onGetSentenceSuggestions(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo);
    
    public abstract void onGetSuggestions(SuggestionsInfo[] paramArrayOfSuggestionsInfo);
  }
  
  private static final class SpellCheckerSessionListenerImpl
    extends ISpellCheckerSessionListener.Stub
  {
    private static final int STATE_CLOSED_AFTER_CONNECTION = 2;
    private static final int STATE_CLOSED_BEFORE_CONNECTION = 3;
    private static final int STATE_CONNECTED = 1;
    private static final int STATE_WAIT_CONNECTION = 0;
    private static final int TASK_CANCEL = 1;
    private static final int TASK_CLOSE = 3;
    private static final int TASK_GET_SUGGESTIONS_MULTIPLE = 2;
    private static final int TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE = 4;
    private Handler mAsyncHandler;
    private Handler mHandler;
    private ISpellCheckerSession mISpellCheckerSession;
    private final Queue<SpellCheckerParams> mPendingTasks = new LinkedList();
    private int mState = 0;
    private HandlerThread mThread;
    
    public SpellCheckerSessionListenerImpl(Handler paramHandler)
    {
      mHandler = paramHandler;
    }
    
    private void processCloseLocked()
    {
      mISpellCheckerSession = null;
      if (mThread != null) {
        mThread.quit();
      }
      mHandler = null;
      mPendingTasks.clear();
      mThread = null;
      mAsyncHandler = null;
      switch (mState)
      {
      default: 
        String str = SpellCheckerSession.TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("processCloseLocked is called unexpectedly. mState=");
        localStringBuilder.append(stateToString(mState));
        Log.e(str, localStringBuilder.toString());
        break;
      case 1: 
        mState = 2;
        break;
      case 0: 
        mState = 3;
      }
    }
    
    private void processOrEnqueueTask(SpellCheckerParams paramSpellCheckerParams)
    {
      try
      {
        if ((mWhat == 3) && ((mState == 2) || (mState == 3))) {
          return;
        }
        Object localObject1;
        if ((mState != 0) && (mState != 1))
        {
          localObject1 = SpellCheckerSession.TAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("ignoring processOrEnqueueTask due to unexpected mState=");
          ((StringBuilder)localObject2).append(stateToString(mState));
          ((StringBuilder)localObject2).append(" scp.mWhat=");
          ((StringBuilder)localObject2).append(taskToString(mWhat));
          Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
          return;
        }
        if (mState == 0)
        {
          if (mWhat == 3)
          {
            processCloseLocked();
            return;
          }
          localObject1 = null;
          localObject2 = null;
          if (mWhat == 1) {
            for (;;)
            {
              localObject1 = localObject2;
              if (mPendingTasks.isEmpty()) {
                break;
              }
              localObject1 = (SpellCheckerParams)mPendingTasks.poll();
              if (mWhat == 3) {
                localObject2 = localObject1;
              }
            }
          }
          mPendingTasks.offer(paramSpellCheckerParams);
          if (localObject1 != null) {
            mPendingTasks.offer(localObject1);
          }
          return;
        }
        Object localObject2 = mISpellCheckerSession;
        processTask((ISpellCheckerSession)localObject2, paramSpellCheckerParams, false);
        return;
      }
      finally {}
    }
    
    private void processTask(ISpellCheckerSession paramISpellCheckerSession, SpellCheckerParams paramSpellCheckerParams, boolean paramBoolean)
    {
      if ((!paramBoolean) && (mAsyncHandler != null))
      {
        mSession = paramISpellCheckerSession;
        mAsyncHandler.sendMessage(Message.obtain(mAsyncHandler, 1, paramSpellCheckerParams));
      }
      else
      {
        Object localObject;
        StringBuilder localStringBuilder2;
        switch (mWhat)
        {
        default: 
          break;
        case 4: 
          try
          {
            paramISpellCheckerSession.onGetSentenceSuggestionsMultiple(mTextInfos, mSuggestionsLimit);
          }
          catch (RemoteException paramISpellCheckerSession)
          {
            localObject = SpellCheckerSession.TAG;
            StringBuilder localStringBuilder1 = new StringBuilder();
            localStringBuilder1.append("Failed to get suggestions ");
            localStringBuilder1.append(paramISpellCheckerSession);
            Log.e((String)localObject, localStringBuilder1.toString());
          }
        case 3: 
          try
          {
            paramISpellCheckerSession.onClose();
          }
          catch (RemoteException localRemoteException2)
          {
            paramISpellCheckerSession = SpellCheckerSession.TAG;
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Failed to close ");
            ((StringBuilder)localObject).append(localRemoteException2);
            Log.e(paramISpellCheckerSession, ((StringBuilder)localObject).toString());
          }
        case 2: 
          try
          {
            paramISpellCheckerSession.onGetSuggestionsMultiple(mTextInfos, mSuggestionsLimit, mSequentialWords);
          }
          catch (RemoteException localRemoteException1)
          {
            paramISpellCheckerSession = SpellCheckerSession.TAG;
            localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append("Failed to get suggestions ");
            localStringBuilder2.append(localRemoteException1);
            Log.e(paramISpellCheckerSession, localStringBuilder2.toString());
          }
        case 1: 
          try
          {
            paramISpellCheckerSession.onCancel();
          }
          catch (RemoteException paramISpellCheckerSession)
          {
            String str = SpellCheckerSession.TAG;
            localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append("Failed to cancel ");
            localStringBuilder2.append(paramISpellCheckerSession);
            Log.e(str, localStringBuilder2.toString());
          }
        }
      }
      if (mWhat == 3) {
        try
        {
          processCloseLocked();
        }
        finally {}
      }
    }
    
    private static String stateToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected state=");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 3: 
        return "STATE_CLOSED_BEFORE_CONNECTION";
      case 2: 
        return "STATE_CLOSED_AFTER_CONNECTION";
      case 1: 
        return "STATE_CONNECTED";
      }
      return "STATE_WAIT_CONNECTION";
    }
    
    private static String taskToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected task=");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 4: 
        return "TASK_GET_SUGGESTIONS_MULTIPLE_FOR_SENTENCE";
      case 3: 
        return "TASK_CLOSE";
      case 2: 
        return "TASK_GET_SUGGESTIONS_MULTIPLE";
      }
      return "TASK_CANCEL";
    }
    
    public void cancel()
    {
      processOrEnqueueTask(new SpellCheckerParams(1, null, 0, false));
    }
    
    public void close()
    {
      processOrEnqueueTask(new SpellCheckerParams(3, null, 0, false));
    }
    
    public void getSentenceSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt)
    {
      processOrEnqueueTask(new SpellCheckerParams(4, paramArrayOfTextInfo, paramInt, false));
    }
    
    public void getSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
    {
      processOrEnqueueTask(new SpellCheckerParams(2, paramArrayOfTextInfo, paramInt, paramBoolean));
    }
    
    public boolean isDisconnected()
    {
      try
      {
        int i = mState;
        boolean bool = true;
        if (i == 1) {
          bool = false;
        }
        return bool;
      }
      finally {}
    }
    
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo)
    {
      try
      {
        if (mHandler != null) {
          mHandler.sendMessage(Message.obtain(mHandler, 2, paramArrayOfSentenceSuggestionsInfo));
        }
        return;
      }
      finally {}
    }
    
    public void onGetSuggestions(SuggestionsInfo[] paramArrayOfSuggestionsInfo)
    {
      try
      {
        if (mHandler != null) {
          mHandler.sendMessage(Message.obtain(mHandler, 1, paramArrayOfSuggestionsInfo));
        }
        return;
      }
      finally {}
    }
    
    public void onServiceConnected(ISpellCheckerSession paramISpellCheckerSession)
    {
      try
      {
        int i = mState;
        Object localObject;
        if (i != 0)
        {
          if (i != 3)
          {
            localObject = SpellCheckerSession.TAG;
            paramISpellCheckerSession = new java/lang/StringBuilder;
            paramISpellCheckerSession.<init>();
            paramISpellCheckerSession.append("ignoring onServiceConnected due to unexpected mState=");
            paramISpellCheckerSession.append(stateToString(mState));
            Log.e((String)localObject, paramISpellCheckerSession.toString());
            return;
          }
          return;
        }
        if (paramISpellCheckerSession == null)
        {
          Log.e(SpellCheckerSession.TAG, "ignoring onServiceConnected due to session=null");
          return;
        }
        mISpellCheckerSession = paramISpellCheckerSession;
        if (((paramISpellCheckerSession.asBinder() instanceof Binder)) && (mThread == null))
        {
          localObject = new android/os/HandlerThread;
          ((HandlerThread)localObject).<init>("SpellCheckerSession", 10);
          mThread = ((HandlerThread)localObject);
          mThread.start();
          localObject = new android/view/textservice/SpellCheckerSession$SpellCheckerSessionListenerImpl$1;
          ((1)localObject).<init>(this, mThread.getLooper());
          mAsyncHandler = ((Handler)localObject);
        }
        mState = 1;
        while (!mPendingTasks.isEmpty()) {
          processTask(paramISpellCheckerSession, (SpellCheckerParams)mPendingTasks.poll(), false);
        }
        return;
      }
      finally {}
    }
    
    private static class SpellCheckerParams
    {
      public final boolean mSequentialWords;
      public ISpellCheckerSession mSession;
      public final int mSuggestionsLimit;
      public final TextInfo[] mTextInfos;
      public final int mWhat;
      
      public SpellCheckerParams(int paramInt1, TextInfo[] paramArrayOfTextInfo, int paramInt2, boolean paramBoolean)
      {
        mWhat = paramInt1;
        mTextInfos = paramArrayOfTextInfo;
        mSuggestionsLimit = paramInt2;
        mSequentialWords = paramBoolean;
      }
    }
  }
}
