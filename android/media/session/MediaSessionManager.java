package android.media.session;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.media.IRemoteVolumeController;
import android.media.ISessionTokensListener.Stub;
import android.media.SessionToken2;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public final class MediaSessionManager
{
  public static final int RESULT_MEDIA_KEY_HANDLED = 1;
  public static final int RESULT_MEDIA_KEY_NOT_HANDLED = 0;
  private static final String TAG = "SessionManager";
  private CallbackImpl mCallback;
  private Context mContext;
  private final ArrayMap<OnActiveSessionsChangedListener, SessionsChangedWrapper> mListeners = new ArrayMap();
  private final Object mLock = new Object();
  private OnMediaKeyListenerImpl mOnMediaKeyListener;
  private OnVolumeKeyLongPressListenerImpl mOnVolumeKeyLongPressListener;
  private final ISessionManager mService;
  private final ArrayMap<OnSessionTokensChangedListener, SessionTokensChangedWrapper> mSessionTokensListener = new ArrayMap();
  
  public MediaSessionManager(Context paramContext)
  {
    mContext = paramContext;
    mService = ISessionManager.Stub.asInterface(ServiceManager.getService("media_session"));
  }
  
  /* Error */
  private void dispatchMediaKeyEventInternal(boolean paramBoolean1, KeyEvent paramKeyEvent, boolean paramBoolean2)
  {
    // Byte code:
    //   0: invokestatic 129	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore 4
    //   5: aload_0
    //   6: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   9: aload_0
    //   10: getfield 97	android/media/session/MediaSessionManager:mContext	Landroid/content/Context;
    //   13: invokevirtual 135	android/content/Context:getPackageName	()Ljava/lang/String;
    //   16: iload_1
    //   17: aload_2
    //   18: iload_3
    //   19: invokeinterface 141 5 0
    //   24: lload 4
    //   26: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   29: goto +20 -> 49
    //   32: astore_2
    //   33: goto +17 -> 50
    //   36: astore_2
    //   37: ldc 64
    //   39: ldc -109
    //   41: aload_2
    //   42: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   45: pop
    //   46: goto -22 -> 24
    //   49: return
    //   50: lload 4
    //   52: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   55: aload_2
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	MediaSessionManager
    //   0	57	1	paramBoolean1	boolean
    //   0	57	2	paramKeyEvent	KeyEvent
    //   0	57	3	paramBoolean2	boolean
    //   3	48	4	l	long
    // Exception table:
    //   from	to	target	type
    //   5	24	32	finally
    //   37	46	32	finally
    //   5	24	36	android/os/RemoteException
  }
  
  /* Error */
  private void dispatchVolumeKeyEventInternal(boolean paramBoolean1, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean2)
  {
    // Byte code:
    //   0: invokestatic 129	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore 5
    //   5: aload_0
    //   6: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   9: aload_0
    //   10: getfield 97	android/media/session/MediaSessionManager:mContext	Landroid/content/Context;
    //   13: invokevirtual 135	android/content/Context:getPackageName	()Ljava/lang/String;
    //   16: iload_1
    //   17: aload_2
    //   18: iload_3
    //   19: iload 4
    //   21: invokeinterface 159 6 0
    //   26: lload 5
    //   28: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   31: goto +20 -> 51
    //   34: astore_2
    //   35: goto +17 -> 52
    //   38: astore_2
    //   39: ldc 64
    //   41: ldc -95
    //   43: aload_2
    //   44: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   47: pop
    //   48: goto -22 -> 26
    //   51: return
    //   52: lload 5
    //   54: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   57: aload_2
    //   58: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	59	0	this	MediaSessionManager
    //   0	59	1	paramBoolean1	boolean
    //   0	59	2	paramKeyEvent	KeyEvent
    //   0	59	3	paramInt	int
    //   0	59	4	paramBoolean2	boolean
    //   3	50	5	l	long
    // Exception table:
    //   from	to	target	type
    //   5	26	34	finally
    //   39	48	34	finally
    //   5	26	38	android/os/RemoteException
  }
  
  private static List<SessionToken2> toTokenList(List<Bundle> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramList != null) {
      for (int i = 0; i < paramList.size(); i++)
      {
        SessionToken2 localSessionToken2 = SessionToken2.fromBundle((Bundle)paramList.get(i));
        if (localSessionToken2 != null) {
          localArrayList.add(localSessionToken2);
        }
      }
    }
    return localArrayList;
  }
  
  public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener paramOnActiveSessionsChangedListener, ComponentName paramComponentName)
  {
    addOnActiveSessionsChangedListener(paramOnActiveSessionsChangedListener, paramComponentName, null);
  }
  
  public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener paramOnActiveSessionsChangedListener, ComponentName paramComponentName, int paramInt, Handler paramHandler)
  {
    if (paramOnActiveSessionsChangedListener != null)
    {
      if (paramHandler == null) {
        paramHandler = new Handler();
      }
      synchronized (mLock)
      {
        if (mListeners.get(paramOnActiveSessionsChangedListener) != null)
        {
          Log.w("SessionManager", "Attempted to add session listener twice, ignoring.");
          return;
        }
        SessionsChangedWrapper localSessionsChangedWrapper = new android/media/session/MediaSessionManager$SessionsChangedWrapper;
        localSessionsChangedWrapper.<init>(mContext, paramOnActiveSessionsChangedListener, paramHandler);
        try
        {
          mService.addSessionsListener(mStub, paramComponentName, paramInt);
          mListeners.put(paramOnActiveSessionsChangedListener, localSessionsChangedWrapper);
        }
        catch (RemoteException paramOnActiveSessionsChangedListener)
        {
          Log.e("SessionManager", "Error in addOnActiveSessionsChangedListener.", paramOnActiveSessionsChangedListener);
        }
        return;
      }
    }
    throw new IllegalArgumentException("listener may not be null");
  }
  
  public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener paramOnActiveSessionsChangedListener, ComponentName paramComponentName, Handler paramHandler)
  {
    addOnActiveSessionsChangedListener(paramOnActiveSessionsChangedListener, paramComponentName, UserHandle.myUserId(), paramHandler);
  }
  
  public void addOnSessionTokensChangedListener(int paramInt, Executor paramExecutor, OnSessionTokensChangedListener paramOnSessionTokensChangedListener)
  {
    if (paramExecutor != null)
    {
      if (paramOnSessionTokensChangedListener != null) {
        synchronized (mLock)
        {
          if (mSessionTokensListener.get(paramOnSessionTokensChangedListener) != null)
          {
            Log.w("SessionManager", "Attempted to add session listener twice, ignoring.");
            return;
          }
          SessionTokensChangedWrapper localSessionTokensChangedWrapper = new android/media/session/MediaSessionManager$SessionTokensChangedWrapper;
          localSessionTokensChangedWrapper.<init>(mContext, paramExecutor, paramOnSessionTokensChangedListener);
          try
          {
            mService.addSessionTokensListener(mStub, paramInt, mContext.getPackageName());
            mSessionTokensListener.put(paramOnSessionTokensChangedListener, localSessionTokensChangedWrapper);
          }
          catch (RemoteException paramExecutor)
          {
            Log.e("SessionManager", "Error in addSessionTokensListener.", paramExecutor);
          }
          return;
        }
      }
      throw new IllegalArgumentException("listener may not be null");
    }
    throw new IllegalArgumentException("executor may not be null");
  }
  
  public void addOnSessionTokensChangedListener(Executor paramExecutor, OnSessionTokensChangedListener paramOnSessionTokensChangedListener)
  {
    addOnSessionTokensChangedListener(UserHandle.myUserId(), paramExecutor, paramOnSessionTokensChangedListener);
  }
  
  public ISession createSession(MediaSession.CallbackStub paramCallbackStub, String paramString, int paramInt)
    throws RemoteException
  {
    return mService.createSession(mContext.getPackageName(), paramCallbackStub, paramString, paramInt);
  }
  
  public boolean createSession2(SessionToken2 paramSessionToken2)
  {
    if (paramSessionToken2 == null) {
      return false;
    }
    try
    {
      boolean bool = mService.createSession2(paramSessionToken2.toBundle());
      return bool;
    }
    catch (RemoteException paramSessionToken2)
    {
      Log.wtf("SessionManager", "Cannot communicate with the service.", paramSessionToken2);
    }
    return false;
  }
  
  public void destroySession2(SessionToken2 paramSessionToken2)
  {
    if (paramSessionToken2 == null) {
      return;
    }
    try
    {
      mService.destroySession2(paramSessionToken2.toBundle());
    }
    catch (RemoteException paramSessionToken2)
    {
      Log.wtf("SessionManager", "Cannot communicate with the service.", paramSessionToken2);
    }
  }
  
  /* Error */
  public void dispatchAdjustVolume(int paramInt1, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: invokestatic 129	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore 4
    //   5: aload_0
    //   6: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   9: aload_0
    //   10: getfield 97	android/media/session/MediaSessionManager:mContext	Landroid/content/Context;
    //   13: invokevirtual 135	android/content/Context:getPackageName	()Ljava/lang/String;
    //   16: iload_1
    //   17: iload_2
    //   18: iload_3
    //   19: invokeinterface 287 5 0
    //   24: lload 4
    //   26: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   29: goto +24 -> 53
    //   32: astore 6
    //   34: goto +20 -> 54
    //   37: astore 6
    //   39: ldc 64
    //   41: ldc_w 289
    //   44: aload 6
    //   46: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   49: pop
    //   50: goto -26 -> 24
    //   53: return
    //   54: lload 4
    //   56: invokestatic 145	android/os/Binder:restoreCallingIdentity	(J)V
    //   59: aload 6
    //   61: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	62	0	this	MediaSessionManager
    //   0	62	1	paramInt1	int
    //   0	62	2	paramInt2	int
    //   0	62	3	paramInt3	int
    //   3	52	4	l	long
    //   32	1	6	localObject	Object
    //   37	23	6	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   5	24	32	finally
    //   39	50	32	finally
    //   5	24	37	android/os/RemoteException
  }
  
  public void dispatchMediaKeyEvent(KeyEvent paramKeyEvent)
  {
    dispatchMediaKeyEvent(paramKeyEvent, false);
  }
  
  public void dispatchMediaKeyEvent(KeyEvent paramKeyEvent, boolean paramBoolean)
  {
    dispatchMediaKeyEventInternal(false, paramKeyEvent, paramBoolean);
  }
  
  public void dispatchMediaKeyEventAsSystemService(KeyEvent paramKeyEvent)
  {
    dispatchMediaKeyEventInternal(true, paramKeyEvent, false);
  }
  
  public void dispatchVolumeKeyEvent(KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
  {
    dispatchVolumeKeyEventInternal(false, paramKeyEvent, paramInt, paramBoolean);
  }
  
  public void dispatchVolumeKeyEventAsSystemService(KeyEvent paramKeyEvent, int paramInt)
  {
    dispatchVolumeKeyEventInternal(true, paramKeyEvent, paramInt, false);
  }
  
  public List<SessionToken2> getActiveSessionTokens()
  {
    try
    {
      List localList = toTokenList(mService.getSessionTokens(true, false, mContext.getPackageName()));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("SessionManager", "Cannot communicate with the service.", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public List<MediaController> getActiveSessions(ComponentName paramComponentName)
  {
    return getActiveSessionsForUser(paramComponentName, UserHandle.myUserId());
  }
  
  public List<MediaController> getActiveSessionsForUser(ComponentName paramComponentName, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      paramComponentName = mService.getSessions(paramComponentName, paramInt);
      int i = paramComponentName.size();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        MediaController localMediaController = new android/media/session/MediaController;
        localMediaController.<init>(mContext, ISessionController.Stub.asInterface((IBinder)paramComponentName.get(paramInt)));
        localArrayList.add(localMediaController);
      }
    }
    catch (RemoteException paramComponentName)
    {
      Log.e("SessionManager", "Failed to get active sessions: ", paramComponentName);
    }
    return localArrayList;
  }
  
  public List<SessionToken2> getAllSessionTokens()
  {
    try
    {
      List localList = toTokenList(mService.getSessionTokens(false, false, mContext.getPackageName()));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("SessionManager", "Cannot communicate with the service.", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public List<SessionToken2> getSessionServiceTokens()
  {
    try
    {
      List localList = toTokenList(mService.getSessionTokens(false, true, mContext.getPackageName()));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      Log.wtf("SessionManager", "Cannot communicate with the service.", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public boolean isGlobalPriorityActive()
  {
    try
    {
      boolean bool = mService.isGlobalPriorityActive();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SessionManager", "Failed to check if the global priority is active.", localRemoteException);
    }
    return false;
  }
  
  public boolean isTrustedForMediaControl(RemoteUserInfo paramRemoteUserInfo)
  {
    if (paramRemoteUserInfo != null)
    {
      if (paramRemoteUserInfo.getPackageName() == null) {
        return false;
      }
      try
      {
        boolean bool = mService.isTrusted(paramRemoteUserInfo.getPackageName(), paramRemoteUserInfo.getPid(), paramRemoteUserInfo.getUid());
        return bool;
      }
      catch (RemoteException paramRemoteUserInfo)
      {
        Log.wtf("SessionManager", "Cannot communicate with the service.", paramRemoteUserInfo);
        return false;
      }
    }
    throw new IllegalArgumentException("userInfo may not be null");
  }
  
  /* Error */
  public void removeOnActiveSessionsChangedListener(OnActiveSessionsChangedListener paramOnActiveSessionsChangedListener)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +82 -> 83
    //   4: aload_0
    //   5: getfield 95	android/media/session/MediaSessionManager:mLock	Ljava/lang/Object;
    //   8: astore_2
    //   9: aload_2
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 91	android/media/session/MediaSessionManager:mListeners	Landroid/util/ArrayMap;
    //   15: aload_1
    //   16: invokevirtual 367	android/util/ArrayMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast 50	android/media/session/MediaSessionManager$SessionsChangedWrapper
    //   22: astore_1
    //   23: aload_1
    //   24: ifnull +51 -> 75
    //   27: aload_0
    //   28: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   31: aload_1
    //   32: invokestatic 213	android/media/session/MediaSessionManager$SessionsChangedWrapper:access$000	(Landroid/media/session/MediaSessionManager$SessionsChangedWrapper;)Landroid/media/session/IActiveSessionsListener$Stub;
    //   35: invokeinterface 371 2 0
    //   40: aload_1
    //   41: invokestatic 375	android/media/session/MediaSessionManager$SessionsChangedWrapper:access$100	(Landroid/media/session/MediaSessionManager$SessionsChangedWrapper;)V
    //   44: goto +31 -> 75
    //   47: astore_3
    //   48: goto +21 -> 69
    //   51: astore_3
    //   52: ldc 64
    //   54: ldc_w 377
    //   57: aload_3
    //   58: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   61: pop
    //   62: aload_1
    //   63: invokestatic 375	android/media/session/MediaSessionManager$SessionsChangedWrapper:access$100	(Landroid/media/session/MediaSessionManager$SessionsChangedWrapper;)V
    //   66: goto -22 -> 44
    //   69: aload_1
    //   70: invokestatic 375	android/media/session/MediaSessionManager$SessionsChangedWrapper:access$100	(Landroid/media/session/MediaSessionManager$SessionsChangedWrapper;)V
    //   73: aload_3
    //   74: athrow
    //   75: aload_2
    //   76: monitorexit
    //   77: return
    //   78: astore_1
    //   79: aload_2
    //   80: monitorexit
    //   81: aload_1
    //   82: athrow
    //   83: new 225	java/lang/IllegalArgumentException
    //   86: dup
    //   87: ldc -29
    //   89: invokespecial 230	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   92: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	93	0	this	MediaSessionManager
    //   0	93	1	paramOnActiveSessionsChangedListener	OnActiveSessionsChangedListener
    //   8	72	2	localObject1	Object
    //   47	1	3	localObject2	Object
    //   51	23	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   27	40	47	finally
    //   52	62	47	finally
    //   27	40	51	android/os/RemoteException
    //   11	23	78	finally
    //   40	44	78	finally
    //   62	66	78	finally
    //   69	75	78	finally
    //   75	77	78	finally
    //   79	81	78	finally
  }
  
  /* Error */
  public void removeOnSessionTokensChangedListener(OnSessionTokensChangedListener paramOnSessionTokensChangedListener)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +89 -> 90
    //   4: aload_0
    //   5: getfield 95	android/media/session/MediaSessionManager:mLock	Ljava/lang/Object;
    //   8: astore_2
    //   9: aload_2
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 93	android/media/session/MediaSessionManager:mSessionTokensListener	Landroid/util/ArrayMap;
    //   15: aload_1
    //   16: invokevirtual 367	android/util/ArrayMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast 45	android/media/session/MediaSessionManager$SessionTokensChangedWrapper
    //   22: astore_1
    //   23: aload_1
    //   24: ifnull +58 -> 82
    //   27: aload_0
    //   28: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   31: aload_1
    //   32: invokestatic 246	android/media/session/MediaSessionManager$SessionTokensChangedWrapper:access$200	(Landroid/media/session/MediaSessionManager$SessionTokensChangedWrapper;)Landroid/media/ISessionTokensListener$Stub;
    //   35: aload_0
    //   36: getfield 97	android/media/session/MediaSessionManager:mContext	Landroid/content/Context;
    //   39: invokevirtual 135	android/content/Context:getPackageName	()Ljava/lang/String;
    //   42: invokeinterface 383 3 0
    //   47: aload_1
    //   48: invokestatic 387	android/media/session/MediaSessionManager$SessionTokensChangedWrapper:access$300	(Landroid/media/session/MediaSessionManager$SessionTokensChangedWrapper;)V
    //   51: goto +22 -> 73
    //   54: astore_3
    //   55: goto +21 -> 76
    //   58: astore_3
    //   59: ldc 64
    //   61: ldc_w 389
    //   64: aload_3
    //   65: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   68: pop
    //   69: aload_1
    //   70: invokestatic 387	android/media/session/MediaSessionManager$SessionTokensChangedWrapper:access$300	(Landroid/media/session/MediaSessionManager$SessionTokensChangedWrapper;)V
    //   73: goto +9 -> 82
    //   76: aload_1
    //   77: invokestatic 387	android/media/session/MediaSessionManager$SessionTokensChangedWrapper:access$300	(Landroid/media/session/MediaSessionManager$SessionTokensChangedWrapper;)V
    //   80: aload_3
    //   81: athrow
    //   82: aload_2
    //   83: monitorexit
    //   84: return
    //   85: astore_1
    //   86: aload_2
    //   87: monitorexit
    //   88: aload_1
    //   89: athrow
    //   90: new 225	java/lang/IllegalArgumentException
    //   93: dup
    //   94: ldc -29
    //   96: invokespecial 230	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   99: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	this	MediaSessionManager
    //   0	100	1	paramOnSessionTokensChangedListener	OnSessionTokensChangedListener
    //   8	79	2	localObject1	Object
    //   54	1	3	localObject2	Object
    //   58	23	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   27	47	54	finally
    //   59	69	54	finally
    //   27	47	58	android/os/RemoteException
    //   11	23	85	finally
    //   47	51	85	finally
    //   69	73	85	finally
    //   76	82	85	finally
    //   82	84	85	finally
    //   86	88	85	finally
  }
  
  /* Error */
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 95	android/media/session/MediaSessionManager:mLock	Ljava/lang/Object;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_1
    //   8: ifnonnull +29 -> 37
    //   11: aload_0
    //   12: aconst_null
    //   13: putfield 393	android/media/session/MediaSessionManager:mCallback	Landroid/media/session/MediaSessionManager$CallbackImpl;
    //   16: aload_0
    //   17: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   20: aconst_null
    //   21: invokeinterface 396 2 0
    //   26: goto +57 -> 83
    //   29: astore_1
    //   30: goto +69 -> 99
    //   33: astore_1
    //   34: goto +52 -> 86
    //   37: aload_2
    //   38: astore 4
    //   40: aload_2
    //   41: ifnonnull +13 -> 54
    //   44: new 196	android/os/Handler
    //   47: astore 4
    //   49: aload 4
    //   51: invokespecial 197	android/os/Handler:<init>	()V
    //   54: new 9	android/media/session/MediaSessionManager$CallbackImpl
    //   57: astore_2
    //   58: aload_2
    //   59: aload_1
    //   60: aload 4
    //   62: invokespecial 398	android/media/session/MediaSessionManager$CallbackImpl:<init>	(Landroid/media/session/MediaSessionManager$Callback;Landroid/os/Handler;)V
    //   65: aload_0
    //   66: aload_2
    //   67: putfield 393	android/media/session/MediaSessionManager:mCallback	Landroid/media/session/MediaSessionManager$CallbackImpl;
    //   70: aload_0
    //   71: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   74: aload_0
    //   75: getfield 393	android/media/session/MediaSessionManager:mCallback	Landroid/media/session/MediaSessionManager$CallbackImpl;
    //   78: invokeinterface 396 2 0
    //   83: goto +13 -> 96
    //   86: ldc 64
    //   88: ldc_w 400
    //   91: aload_1
    //   92: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   95: pop
    //   96: aload_3
    //   97: monitorexit
    //   98: return
    //   99: aload_3
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	this	MediaSessionManager
    //   0	103	1	paramCallback	Callback
    //   0	103	2	paramHandler	Handler
    //   4	96	3	localObject	Object
    //   38	23	4	localHandler	Handler
    // Exception table:
    //   from	to	target	type
    //   11	26	29	finally
    //   44	54	29	finally
    //   54	83	29	finally
    //   86	96	29	finally
    //   96	98	29	finally
    //   99	101	29	finally
    //   11	26	33	android/os/RemoteException
    //   44	54	33	android/os/RemoteException
    //   54	83	33	android/os/RemoteException
  }
  
  /* Error */
  @SystemApi
  public void setOnMediaKeyListener(OnMediaKeyListener paramOnMediaKeyListener, Handler paramHandler)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 95	android/media/session/MediaSessionManager:mLock	Ljava/lang/Object;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_1
    //   8: ifnonnull +29 -> 37
    //   11: aload_0
    //   12: aconst_null
    //   13: putfield 405	android/media/session/MediaSessionManager:mOnMediaKeyListener	Landroid/media/session/MediaSessionManager$OnMediaKeyListenerImpl;
    //   16: aload_0
    //   17: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   20: aconst_null
    //   21: invokeinterface 408 2 0
    //   26: goto +57 -> 83
    //   29: astore_1
    //   30: goto +69 -> 99
    //   33: astore_1
    //   34: goto +52 -> 86
    //   37: aload_2
    //   38: astore 4
    //   40: aload_2
    //   41: ifnonnull +13 -> 54
    //   44: new 196	android/os/Handler
    //   47: astore 4
    //   49: aload 4
    //   51: invokespecial 197	android/os/Handler:<init>	()V
    //   54: new 26	android/media/session/MediaSessionManager$OnMediaKeyListenerImpl
    //   57: astore_2
    //   58: aload_2
    //   59: aload_1
    //   60: aload 4
    //   62: invokespecial 410	android/media/session/MediaSessionManager$OnMediaKeyListenerImpl:<init>	(Landroid/media/session/MediaSessionManager$OnMediaKeyListener;Landroid/os/Handler;)V
    //   65: aload_0
    //   66: aload_2
    //   67: putfield 405	android/media/session/MediaSessionManager:mOnMediaKeyListener	Landroid/media/session/MediaSessionManager$OnMediaKeyListenerImpl;
    //   70: aload_0
    //   71: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   74: aload_0
    //   75: getfield 405	android/media/session/MediaSessionManager:mOnMediaKeyListener	Landroid/media/session/MediaSessionManager$OnMediaKeyListenerImpl;
    //   78: invokeinterface 408 2 0
    //   83: goto +13 -> 96
    //   86: ldc 64
    //   88: ldc_w 412
    //   91: aload_1
    //   92: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   95: pop
    //   96: aload_3
    //   97: monitorexit
    //   98: return
    //   99: aload_3
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	this	MediaSessionManager
    //   0	103	1	paramOnMediaKeyListener	OnMediaKeyListener
    //   0	103	2	paramHandler	Handler
    //   4	96	3	localObject	Object
    //   38	23	4	localHandler	Handler
    // Exception table:
    //   from	to	target	type
    //   11	26	29	finally
    //   44	54	29	finally
    //   54	83	29	finally
    //   86	96	29	finally
    //   96	98	29	finally
    //   99	101	29	finally
    //   11	26	33	android/os/RemoteException
    //   44	54	33	android/os/RemoteException
    //   54	83	33	android/os/RemoteException
  }
  
  /* Error */
  @SystemApi
  public void setOnVolumeKeyLongPressListener(OnVolumeKeyLongPressListener paramOnVolumeKeyLongPressListener, Handler paramHandler)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 95	android/media/session/MediaSessionManager:mLock	Ljava/lang/Object;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_1
    //   8: ifnonnull +29 -> 37
    //   11: aload_0
    //   12: aconst_null
    //   13: putfield 417	android/media/session/MediaSessionManager:mOnVolumeKeyLongPressListener	Landroid/media/session/MediaSessionManager$OnVolumeKeyLongPressListenerImpl;
    //   16: aload_0
    //   17: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   20: aconst_null
    //   21: invokeinterface 420 2 0
    //   26: goto +57 -> 83
    //   29: astore_1
    //   30: goto +69 -> 99
    //   33: astore_1
    //   34: goto +52 -> 86
    //   37: aload_2
    //   38: astore 4
    //   40: aload_2
    //   41: ifnonnull +13 -> 54
    //   44: new 196	android/os/Handler
    //   47: astore 4
    //   49: aload 4
    //   51: invokespecial 197	android/os/Handler:<init>	()V
    //   54: new 37	android/media/session/MediaSessionManager$OnVolumeKeyLongPressListenerImpl
    //   57: astore_2
    //   58: aload_2
    //   59: aload_1
    //   60: aload 4
    //   62: invokespecial 422	android/media/session/MediaSessionManager$OnVolumeKeyLongPressListenerImpl:<init>	(Landroid/media/session/MediaSessionManager$OnVolumeKeyLongPressListener;Landroid/os/Handler;)V
    //   65: aload_0
    //   66: aload_2
    //   67: putfield 417	android/media/session/MediaSessionManager:mOnVolumeKeyLongPressListener	Landroid/media/session/MediaSessionManager$OnVolumeKeyLongPressListenerImpl;
    //   70: aload_0
    //   71: getfield 113	android/media/session/MediaSessionManager:mService	Landroid/media/session/ISessionManager;
    //   74: aload_0
    //   75: getfield 417	android/media/session/MediaSessionManager:mOnVolumeKeyLongPressListener	Landroid/media/session/MediaSessionManager$OnVolumeKeyLongPressListenerImpl;
    //   78: invokeinterface 420 2 0
    //   83: goto +13 -> 96
    //   86: ldc 64
    //   88: ldc_w 424
    //   91: aload_1
    //   92: invokestatic 153	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   95: pop
    //   96: aload_3
    //   97: monitorexit
    //   98: return
    //   99: aload_3
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	this	MediaSessionManager
    //   0	103	1	paramOnVolumeKeyLongPressListener	OnVolumeKeyLongPressListener
    //   0	103	2	paramHandler	Handler
    //   4	96	3	localObject	Object
    //   38	23	4	localHandler	Handler
    // Exception table:
    //   from	to	target	type
    //   11	26	29	finally
    //   44	54	29	finally
    //   54	83	29	finally
    //   86	96	29	finally
    //   96	98	29	finally
    //   99	101	29	finally
    //   11	26	33	android/os/RemoteException
    //   44	54	33	android/os/RemoteException
    //   54	83	33	android/os/RemoteException
  }
  
  public void setRemoteVolumeController(IRemoteVolumeController paramIRemoteVolumeController)
  {
    try
    {
      mService.setRemoteVolumeController(paramIRemoteVolumeController);
    }
    catch (RemoteException paramIRemoteVolumeController)
    {
      Log.e("SessionManager", "Error in setRemoteVolumeController.", paramIRemoteVolumeController);
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onAddressedPlayerChanged(ComponentName paramComponentName);
    
    public abstract void onAddressedPlayerChanged(MediaSession.Token paramToken);
    
    public abstract void onMediaKeyEventDispatched(KeyEvent paramKeyEvent, ComponentName paramComponentName);
    
    public abstract void onMediaKeyEventDispatched(KeyEvent paramKeyEvent, MediaSession.Token paramToken);
  }
  
  private static final class CallbackImpl
    extends ICallback.Stub
  {
    private final MediaSessionManager.Callback mCallback;
    private final Handler mHandler;
    
    public CallbackImpl(MediaSessionManager.Callback paramCallback, Handler paramHandler)
    {
      mCallback = paramCallback;
      mHandler = paramHandler;
    }
    
    public void onAddressedPlayerChangedToMediaButtonReceiver(final ComponentName paramComponentName)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onAddressedPlayerChanged(paramComponentName);
        }
      });
    }
    
    public void onAddressedPlayerChangedToMediaSession(final MediaSession.Token paramToken)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onAddressedPlayerChanged(paramToken);
        }
      });
    }
    
    public void onMediaKeyEventDispatchedToMediaButtonReceiver(final KeyEvent paramKeyEvent, final ComponentName paramComponentName)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onMediaKeyEventDispatched(paramKeyEvent, paramComponentName);
        }
      });
    }
    
    public void onMediaKeyEventDispatchedToMediaSession(final KeyEvent paramKeyEvent, final MediaSession.Token paramToken)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onMediaKeyEventDispatched(paramKeyEvent, paramToken);
        }
      });
    }
  }
  
  public static abstract interface OnActiveSessionsChangedListener
  {
    public abstract void onActiveSessionsChanged(List<MediaController> paramList);
  }
  
  @SystemApi
  public static abstract interface OnMediaKeyListener
  {
    public abstract boolean onMediaKey(KeyEvent paramKeyEvent);
  }
  
  private static final class OnMediaKeyListenerImpl
    extends IOnMediaKeyListener.Stub
  {
    private Handler mHandler;
    private MediaSessionManager.OnMediaKeyListener mListener;
    
    public OnMediaKeyListenerImpl(MediaSessionManager.OnMediaKeyListener paramOnMediaKeyListener, Handler paramHandler)
    {
      mListener = paramOnMediaKeyListener;
      mHandler = paramHandler;
    }
    
    public void onMediaKey(final KeyEvent paramKeyEvent, final ResultReceiver paramResultReceiver)
    {
      if ((mListener != null) && (mHandler != null))
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            int i = mListener.onMediaKey(paramKeyEvent);
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("The media key listener is returned ");
            localStringBuilder.append(i);
            Log.d("SessionManager", localStringBuilder.toString());
            if (paramResultReceiver != null) {
              paramResultReceiver.send(i, null);
            }
          }
        });
        return;
      }
      Log.w("SessionManager", "Failed to call media key listener. Either mListener or mHandler is null");
    }
  }
  
  public static abstract interface OnSessionTokensChangedListener
  {
    public abstract void onSessionTokensChanged(List<SessionToken2> paramList);
  }
  
  @SystemApi
  public static abstract interface OnVolumeKeyLongPressListener
  {
    public abstract void onVolumeKeyLongPress(KeyEvent paramKeyEvent);
  }
  
  private static final class OnVolumeKeyLongPressListenerImpl
    extends IOnVolumeKeyLongPressListener.Stub
  {
    private Handler mHandler;
    private MediaSessionManager.OnVolumeKeyLongPressListener mListener;
    
    public OnVolumeKeyLongPressListenerImpl(MediaSessionManager.OnVolumeKeyLongPressListener paramOnVolumeKeyLongPressListener, Handler paramHandler)
    {
      mListener = paramOnVolumeKeyLongPressListener;
      mHandler = paramHandler;
    }
    
    public void onVolumeKeyLongPress(final KeyEvent paramKeyEvent)
    {
      if ((mListener != null) && (mHandler != null))
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mListener.onVolumeKeyLongPress(paramKeyEvent);
          }
        });
        return;
      }
      Log.w("SessionManager", "Failed to call volume key long-press listener. Either mListener or mHandler is null");
    }
  }
  
  public static final class RemoteUserInfo
  {
    private final IBinder mCallerBinder;
    private final String mPackageName;
    private final int mPid;
    private final int mUid;
    
    public RemoteUserInfo(String paramString, int paramInt1, int paramInt2)
    {
      this(paramString, paramInt1, paramInt2, null);
    }
    
    public RemoteUserInfo(String paramString, int paramInt1, int paramInt2, IBinder paramIBinder)
    {
      mPackageName = paramString;
      mPid = paramInt1;
      mUid = paramInt2;
      mCallerBinder = paramIBinder;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof RemoteUserInfo;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      if (this == paramObject) {
        return true;
      }
      paramObject = (RemoteUserInfo)paramObject;
      bool1 = bool2;
      if (mCallerBinder != null) {
        if (mCallerBinder == null) {
          bool1 = bool2;
        } else {
          bool1 = mCallerBinder.equals(mCallerBinder);
        }
      }
      return bool1;
    }
    
    public String getPackageName()
    {
      return mPackageName;
    }
    
    public int getPid()
    {
      return mPid;
    }
    
    public int getUid()
    {
      return mUid;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mPackageName, Integer.valueOf(mPid), Integer.valueOf(mUid) });
    }
  }
  
  private static final class SessionTokensChangedWrapper
  {
    private Context mContext;
    private Executor mExecutor;
    private MediaSessionManager.OnSessionTokensChangedListener mListener;
    private final ISessionTokensListener.Stub mStub = new ISessionTokensListener.Stub()
    {
      public void onSessionTokensChanged(List<Bundle> paramAnonymousList)
      {
        Executor localExecutor = mExecutor;
        if (localExecutor != null) {
          localExecutor.execute(new _..Lambda.MediaSessionManager.SessionTokensChangedWrapper.1.wkYv3P0_Sdm0wRGnCFHp_AGf3Dw(this, paramAnonymousList));
        }
      }
    };
    
    public SessionTokensChangedWrapper(Context paramContext, Executor paramExecutor, MediaSessionManager.OnSessionTokensChangedListener paramOnSessionTokensChangedListener)
    {
      mContext = paramContext;
      mExecutor = paramExecutor;
      mListener = paramOnSessionTokensChangedListener;
    }
    
    private void release()
    {
      mListener = null;
      mContext = null;
      mExecutor = null;
    }
  }
  
  private static final class SessionsChangedWrapper
  {
    private Context mContext;
    private Handler mHandler;
    private MediaSessionManager.OnActiveSessionsChangedListener mListener;
    private final IActiveSessionsListener.Stub mStub = new IActiveSessionsListener.Stub()
    {
      public void onActiveSessionsChanged(final List<MediaSession.Token> paramAnonymousList)
      {
        Handler localHandler = mHandler;
        if (localHandler != null) {
          localHandler.post(new Runnable()
          {
            public void run()
            {
              Object localObject = mContext;
              if (localObject != null)
              {
                ArrayList localArrayList = new ArrayList();
                int i = paramAnonymousList.size();
                for (int j = 0; j < i; j++) {
                  localArrayList.add(new MediaController((Context)localObject, (MediaSession.Token)paramAnonymousList.get(j)));
                }
                localObject = mListener;
                if (localObject != null) {
                  ((MediaSessionManager.OnActiveSessionsChangedListener)localObject).onActiveSessionsChanged(localArrayList);
                }
              }
            }
          });
        }
      }
    };
    
    public SessionsChangedWrapper(Context paramContext, MediaSessionManager.OnActiveSessionsChangedListener paramOnActiveSessionsChangedListener, Handler paramHandler)
    {
      mContext = paramContext;
      mListener = paramOnActiveSessionsChangedListener;
      mHandler = paramHandler;
    }
    
    private void release()
    {
      mListener = null;
      mContext = null;
      mHandler = null;
    }
  }
}
