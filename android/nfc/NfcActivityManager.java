package android.nfc;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class NfcActivityManager
  extends IAppCallback.Stub
  implements Application.ActivityLifecycleCallbacks
{
  static final Boolean DBG = Boolean.valueOf(false);
  static final String TAG = "NFC";
  final List<NfcActivityState> mActivities;
  final NfcAdapter mAdapter;
  final List<NfcApplicationState> mApps;
  
  public NfcActivityManager(NfcAdapter paramNfcAdapter)
  {
    mAdapter = paramNfcAdapter;
    mActivities = new LinkedList();
    mApps = new ArrayList(1);
  }
  
  /* Error */
  public BeamShareData createBeamShareData(byte paramByte)
  {
    // Byte code:
    //   0: new 58	android/nfc/NfcEvent
    //   3: dup
    //   4: aload_0
    //   5: getfield 42	android/nfc/NfcActivityManager:mAdapter	Landroid/nfc/NfcAdapter;
    //   8: iload_1
    //   9: invokespecial 61	android/nfc/NfcEvent:<init>	(Landroid/nfc/NfcAdapter;B)V
    //   12: astore_2
    //   13: aload_0
    //   14: monitorenter
    //   15: aload_0
    //   16: invokevirtual 65	android/nfc/NfcActivityManager:findResumedActivityState	()Landroid/nfc/NfcActivityManager$NfcActivityState;
    //   19: astore_3
    //   20: aload_3
    //   21: ifnonnull +12 -> 33
    //   24: aload_0
    //   25: monitorexit
    //   26: aconst_null
    //   27: areturn
    //   28: astore 4
    //   30: goto +324 -> 354
    //   33: aload_3
    //   34: getfield 69	android/nfc/NfcActivityManager$NfcActivityState:ndefMessageCallback	Landroid/nfc/NfcAdapter$CreateNdefMessageCallback;
    //   37: astore 5
    //   39: aload_3
    //   40: getfield 73	android/nfc/NfcActivityManager$NfcActivityState:uriCallback	Landroid/nfc/NfcAdapter$CreateBeamUrisCallback;
    //   43: astore 6
    //   45: aload_3
    //   46: getfield 77	android/nfc/NfcActivityManager$NfcActivityState:ndefMessage	Landroid/nfc/NdefMessage;
    //   49: astore 7
    //   51: aload_3
    //   52: getfield 81	android/nfc/NfcActivityManager$NfcActivityState:uris	[Landroid/net/Uri;
    //   55: astore 4
    //   57: aload_3
    //   58: getfield 85	android/nfc/NfcActivityManager$NfcActivityState:flags	I
    //   61: istore 8
    //   63: aload_3
    //   64: getfield 89	android/nfc/NfcActivityManager$NfcActivityState:activity	Landroid/app/Activity;
    //   67: astore_3
    //   68: aload_0
    //   69: monitorexit
    //   70: invokestatic 95	android/os/Binder:clearCallingIdentity	()J
    //   73: lstore 9
    //   75: aload 5
    //   77: ifnull +21 -> 98
    //   80: aload 5
    //   82: aload_2
    //   83: invokeinterface 101 2 0
    //   88: astore 7
    //   90: goto +8 -> 98
    //   93: astore 4
    //   95: goto +226 -> 321
    //   98: aload 6
    //   100: ifnull +175 -> 275
    //   103: aload_2
    //   104: astore 4
    //   106: aload 6
    //   108: aload_2
    //   109: invokeinterface 107 2 0
    //   114: astore 6
    //   116: aload 6
    //   118: astore 4
    //   120: aload 6
    //   122: ifnull +153 -> 275
    //   125: aload_2
    //   126: astore 4
    //   128: new 49	java/util/ArrayList
    //   131: astore 5
    //   133: aload_2
    //   134: astore 4
    //   136: aload 5
    //   138: invokespecial 108	java/util/ArrayList:<init>	()V
    //   141: aload_2
    //   142: astore 4
    //   144: aload 6
    //   146: arraylength
    //   147: istore 11
    //   149: iconst_0
    //   150: istore 12
    //   152: iload 12
    //   154: iload 11
    //   156: if_icmpge +93 -> 249
    //   159: aload 6
    //   161: iload 12
    //   163: aaload
    //   164: astore 4
    //   166: aload 4
    //   168: ifnonnull +14 -> 182
    //   171: ldc 18
    //   173: ldc 110
    //   175: invokestatic 116	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   178: pop
    //   179: goto +64 -> 243
    //   182: aload 4
    //   184: invokevirtual 122	android/net/Uri:getScheme	()Ljava/lang/String;
    //   187: astore 13
    //   189: aload 13
    //   191: ifnull +44 -> 235
    //   194: aload 13
    //   196: ldc 124
    //   198: invokevirtual 130	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   201: ifne +16 -> 217
    //   204: aload 13
    //   206: ldc -124
    //   208: invokevirtual 130	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   211: ifne +6 -> 217
    //   214: goto +21 -> 235
    //   217: aload 5
    //   219: aload 4
    //   221: aload_3
    //   222: invokevirtual 138	android/app/Activity:getUserId	()I
    //   225: invokestatic 144	android/content/ContentProvider:maybeAddUserId	(Landroid/net/Uri;I)Landroid/net/Uri;
    //   228: invokevirtual 148	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   231: pop
    //   232: goto +11 -> 243
    //   235: ldc 18
    //   237: ldc -106
    //   239: invokestatic 116	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   242: pop
    //   243: iinc 12 1
    //   246: goto -94 -> 152
    //   249: aload 5
    //   251: aload 5
    //   253: invokevirtual 153	java/util/ArrayList:size	()I
    //   256: anewarray 118	android/net/Uri
    //   259: invokevirtual 157	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   262: checkcast 158	[Landroid/net/Uri;
    //   265: astore 4
    //   267: goto +8 -> 275
    //   270: astore 4
    //   272: goto +49 -> 321
    //   275: aload 4
    //   277: ifnull +52 -> 329
    //   280: aload 4
    //   282: arraylength
    //   283: ifle +46 -> 329
    //   286: aload 4
    //   288: arraylength
    //   289: istore 11
    //   291: iconst_0
    //   292: istore 12
    //   294: iload 12
    //   296: iload 11
    //   298: if_icmpge +31 -> 329
    //   301: aload_3
    //   302: ldc -96
    //   304: aload 4
    //   306: iload 12
    //   308: aaload
    //   309: iconst_1
    //   310: invokevirtual 164	android/app/Activity:grantUriPermission	(Ljava/lang/String;Landroid/net/Uri;I)V
    //   313: iinc 12 1
    //   316: goto -22 -> 294
    //   319: astore 4
    //   321: lload 9
    //   323: invokestatic 168	android/os/Binder:restoreCallingIdentity	(J)V
    //   326: aload 4
    //   328: athrow
    //   329: lload 9
    //   331: invokestatic 168	android/os/Binder:restoreCallingIdentity	(J)V
    //   334: new 170	android/nfc/BeamShareData
    //   337: dup
    //   338: aload 7
    //   340: aload 4
    //   342: aload_3
    //   343: invokevirtual 174	android/app/Activity:getUser	()Landroid/os/UserHandle;
    //   346: iload 8
    //   348: invokespecial 177	android/nfc/BeamShareData:<init>	(Landroid/nfc/NdefMessage;[Landroid/net/Uri;Landroid/os/UserHandle;I)V
    //   351: areturn
    //   352: astore 4
    //   354: aload_0
    //   355: monitorexit
    //   356: aload 4
    //   358: athrow
    //   359: astore 4
    //   361: goto -7 -> 354
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	364	0	this	NfcActivityManager
    //   0	364	1	paramByte	byte
    //   12	130	2	localNfcEvent	NfcEvent
    //   19	324	3	localObject1	Object
    //   28	1	4	localObject2	Object
    //   55	1	4	arrayOfUri1	Uri[]
    //   93	1	4	localObject3	Object
    //   104	162	4	localObject4	Object
    //   270	35	4	localObject5	Object
    //   319	22	4	arrayOfUri2	Uri[]
    //   352	5	4	localObject6	Object
    //   359	1	4	localObject7	Object
    //   37	215	5	localObject8	Object
    //   43	117	6	localObject9	Object
    //   49	290	7	localNdefMessage	NdefMessage
    //   61	286	8	i	int
    //   73	257	9	l	long
    //   147	152	11	j	int
    //   150	164	12	k	int
    //   187	18	13	str	String
    // Exception table:
    //   from	to	target	type
    //   24	26	28	finally
    //   80	90	93	finally
    //   106	116	270	finally
    //   128	133	270	finally
    //   136	141	270	finally
    //   144	149	270	finally
    //   171	179	319	finally
    //   182	189	319	finally
    //   194	214	319	finally
    //   217	232	319	finally
    //   235	243	319	finally
    //   249	267	319	finally
    //   280	291	319	finally
    //   301	313	319	finally
    //   15	20	352	finally
    //   33	70	352	finally
    //   354	356	359	finally
  }
  
  void destroyActivityState(Activity paramActivity)
  {
    try
    {
      paramActivity = findActivityState(paramActivity);
      if (paramActivity != null)
      {
        paramActivity.destroy();
        mActivities.remove(paramActivity);
      }
      return;
    }
    finally {}
  }
  
  public void disableReaderMode(Activity paramActivity)
  {
    try
    {
      NfcActivityState localNfcActivityState = getActivityState(paramActivity);
      readerCallback = null;
      readerModeFlags = 0;
      readerModeExtras = null;
      paramActivity = token;
      boolean bool = resumed;
      if (bool) {
        setReaderMode(paramActivity, 0, null);
      }
      return;
    }
    finally {}
  }
  
  public void enableReaderMode(Activity paramActivity, NfcAdapter.ReaderCallback paramReaderCallback, int paramInt, Bundle paramBundle)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      readerCallback = paramReaderCallback;
      readerModeFlags = paramInt;
      readerModeExtras = paramBundle;
      paramReaderCallback = token;
      boolean bool = resumed;
      if (bool) {
        setReaderMode(paramReaderCallback, paramInt, paramBundle);
      }
      return;
    }
    finally {}
  }
  
  NfcActivityState findActivityState(Activity paramActivity)
  {
    try
    {
      Iterator localIterator = mActivities.iterator();
      while (localIterator.hasNext())
      {
        NfcActivityState localNfcActivityState = (NfcActivityState)localIterator.next();
        Activity localActivity = activity;
        if (localActivity == paramActivity) {
          return localNfcActivityState;
        }
      }
      return null;
    }
    finally {}
  }
  
  NfcApplicationState findAppState(Application paramApplication)
  {
    Iterator localIterator = mApps.iterator();
    while (localIterator.hasNext())
    {
      NfcApplicationState localNfcApplicationState = (NfcApplicationState)localIterator.next();
      if (app == paramApplication) {
        return localNfcApplicationState;
      }
    }
    return null;
  }
  
  NfcActivityState findResumedActivityState()
  {
    try
    {
      Iterator localIterator = mActivities.iterator();
      while (localIterator.hasNext())
      {
        NfcActivityState localNfcActivityState = (NfcActivityState)localIterator.next();
        boolean bool = resumed;
        if (bool) {
          return localNfcActivityState;
        }
      }
      return null;
    }
    finally {}
  }
  
  NfcActivityState getActivityState(Activity paramActivity)
  {
    try
    {
      NfcActivityState localNfcActivityState1 = findActivityState(paramActivity);
      NfcActivityState localNfcActivityState2 = localNfcActivityState1;
      if (localNfcActivityState1 == null)
      {
        localNfcActivityState2 = new android/nfc/NfcActivityManager$NfcActivityState;
        localNfcActivityState2.<init>(this, paramActivity);
        mActivities.add(localNfcActivityState2);
      }
      return localNfcActivityState2;
    }
    finally {}
  }
  
  public void onActivityCreated(Activity paramActivity, Bundle paramBundle) {}
  
  public void onActivityDestroyed(Activity paramActivity)
  {
    try
    {
      NfcActivityState localNfcActivityState = findActivityState(paramActivity);
      if (DBG.booleanValue())
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onDestroy() for ");
        localStringBuilder.append(paramActivity);
        localStringBuilder.append(" ");
        localStringBuilder.append(localNfcActivityState);
        Log.d("NFC", localStringBuilder.toString());
      }
      if (localNfcActivityState != null) {
        destroyActivityState(paramActivity);
      }
      return;
    }
    finally {}
  }
  
  public void onActivityPaused(Activity paramActivity)
  {
    try
    {
      NfcActivityState localNfcActivityState = findActivityState(paramActivity);
      if (DBG.booleanValue())
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onPause() for ");
        localStringBuilder.append(paramActivity);
        localStringBuilder.append(" ");
        localStringBuilder.append(localNfcActivityState);
        Log.d("NFC", localStringBuilder.toString());
      }
      if (localNfcActivityState == null) {
        return;
      }
      resumed = false;
      paramActivity = token;
      int i;
      if (readerModeFlags != 0) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        setReaderMode(paramActivity, 0, null);
      }
      return;
    }
    finally {}
  }
  
  public void onActivityResumed(Activity paramActivity)
  {
    try
    {
      Object localObject = findActivityState(paramActivity);
      if (DBG.booleanValue())
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onResume() for ");
        localStringBuilder.append(paramActivity);
        localStringBuilder.append(" ");
        localStringBuilder.append(localObject);
        Log.d("NFC", localStringBuilder.toString());
      }
      if (localObject == null) {
        return;
      }
      resumed = true;
      paramActivity = token;
      int i = readerModeFlags;
      localObject = readerModeExtras;
      if (i != 0) {
        setReaderMode(paramActivity, i, (Bundle)localObject);
      }
      requestNfcServiceCallback();
      return;
    }
    finally {}
  }
  
  public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle) {}
  
  public void onActivityStarted(Activity paramActivity) {}
  
  public void onActivityStopped(Activity paramActivity) {}
  
  public void onNdefPushComplete(byte paramByte)
  {
    try
    {
      Object localObject1 = findResumedActivityState();
      if (localObject1 == null) {
        return;
      }
      localObject1 = onNdefPushCompleteCallback;
      NfcEvent localNfcEvent = new NfcEvent(mAdapter, paramByte);
      if (localObject1 != null) {
        ((NfcAdapter.OnNdefPushCompleteCallback)localObject1).onNdefPushComplete(localNfcEvent);
      }
      return;
    }
    finally {}
  }
  
  public void onTagDiscovered(Tag paramTag)
    throws RemoteException
  {
    try
    {
      Object localObject = findResumedActivityState();
      if (localObject == null) {
        return;
      }
      localObject = readerCallback;
      if (localObject != null) {
        ((NfcAdapter.ReaderCallback)localObject).onTagDiscovered(paramTag);
      }
      return;
    }
    finally {}
  }
  
  void registerApplication(Application paramApplication)
  {
    NfcApplicationState localNfcApplicationState1 = findAppState(paramApplication);
    NfcApplicationState localNfcApplicationState2 = localNfcApplicationState1;
    if (localNfcApplicationState1 == null)
    {
      localNfcApplicationState2 = new NfcApplicationState(paramApplication);
      mApps.add(localNfcApplicationState2);
    }
    localNfcApplicationState2.register();
  }
  
  void requestNfcServiceCallback()
  {
    try
    {
      NfcAdapter.sService.setAppCallback(this);
    }
    catch (RemoteException localRemoteException)
    {
      mAdapter.attemptDeadServiceRecovery(localRemoteException);
    }
  }
  
  public void setNdefPushContentUri(Activity paramActivity, Uri[] paramArrayOfUri)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      uris = paramArrayOfUri;
      boolean bool = resumed;
      if (bool) {
        requestNfcServiceCallback();
      } else {
        verifyNfcPermission();
      }
      return;
    }
    finally {}
  }
  
  public void setNdefPushContentUriCallback(Activity paramActivity, NfcAdapter.CreateBeamUrisCallback paramCreateBeamUrisCallback)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      uriCallback = paramCreateBeamUrisCallback;
      boolean bool = resumed;
      if (bool) {
        requestNfcServiceCallback();
      } else {
        verifyNfcPermission();
      }
      return;
    }
    finally {}
  }
  
  public void setNdefPushMessage(Activity paramActivity, NdefMessage paramNdefMessage, int paramInt)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      ndefMessage = paramNdefMessage;
      flags = paramInt;
      boolean bool = resumed;
      if (bool) {
        requestNfcServiceCallback();
      } else {
        verifyNfcPermission();
      }
      return;
    }
    finally {}
  }
  
  public void setNdefPushMessageCallback(Activity paramActivity, NfcAdapter.CreateNdefMessageCallback paramCreateNdefMessageCallback, int paramInt)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      ndefMessageCallback = paramCreateNdefMessageCallback;
      flags = paramInt;
      boolean bool = resumed;
      if (bool) {
        requestNfcServiceCallback();
      } else {
        verifyNfcPermission();
      }
      return;
    }
    finally {}
  }
  
  public void setOnNdefPushCompleteCallback(Activity paramActivity, NfcAdapter.OnNdefPushCompleteCallback paramOnNdefPushCompleteCallback)
  {
    try
    {
      paramActivity = getActivityState(paramActivity);
      onNdefPushCompleteCallback = paramOnNdefPushCompleteCallback;
      boolean bool = resumed;
      if (bool) {
        requestNfcServiceCallback();
      } else {
        verifyNfcPermission();
      }
      return;
    }
    finally {}
  }
  
  public void setReaderMode(Binder paramBinder, int paramInt, Bundle paramBundle)
  {
    if (DBG.booleanValue()) {
      Log.d("NFC", "Setting reader mode");
    }
    try
    {
      NfcAdapter.sService.setReaderMode(paramBinder, this, paramInt, paramBundle);
    }
    catch (RemoteException paramBinder)
    {
      mAdapter.attemptDeadServiceRecovery(paramBinder);
    }
  }
  
  void unregisterApplication(Application paramApplication)
  {
    Object localObject = findAppState(paramApplication);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("app was not registered ");
      ((StringBuilder)localObject).append(paramApplication);
      Log.e("NFC", ((StringBuilder)localObject).toString());
      return;
    }
    ((NfcApplicationState)localObject).unregister();
  }
  
  void verifyNfcPermission()
  {
    try
    {
      NfcAdapter.sService.verifyNfcPermission();
    }
    catch (RemoteException localRemoteException)
    {
      mAdapter.attemptDeadServiceRecovery(localRemoteException);
    }
  }
  
  class NfcActivityState
  {
    Activity activity;
    int flags = 0;
    NdefMessage ndefMessage = null;
    NfcAdapter.CreateNdefMessageCallback ndefMessageCallback = null;
    NfcAdapter.OnNdefPushCompleteCallback onNdefPushCompleteCallback = null;
    NfcAdapter.ReaderCallback readerCallback = null;
    Bundle readerModeExtras = null;
    int readerModeFlags = 0;
    boolean resumed = false;
    Binder token;
    NfcAdapter.CreateBeamUrisCallback uriCallback = null;
    Uri[] uris = null;
    
    public NfcActivityState(Activity paramActivity)
    {
      if (!paramActivity.getWindow().isDestroyed())
      {
        resumed = paramActivity.isResumed();
        activity = paramActivity;
        token = new Binder();
        registerApplication(paramActivity.getApplication());
        return;
      }
      throw new IllegalStateException("activity is already destroyed");
    }
    
    public void destroy()
    {
      unregisterApplication(activity.getApplication());
      resumed = false;
      activity = null;
      ndefMessage = null;
      ndefMessageCallback = null;
      onNdefPushCompleteCallback = null;
      uriCallback = null;
      uris = null;
      readerModeFlags = 0;
      token = null;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("[").append(" ");
      localStringBuilder.append(ndefMessage);
      localStringBuilder.append(" ");
      localStringBuilder.append(ndefMessageCallback);
      localStringBuilder.append(" ");
      localStringBuilder.append(uriCallback);
      localStringBuilder.append(" ");
      if (uris != null) {
        for (Uri localUri : uris)
        {
          localStringBuilder.append(onNdefPushCompleteCallback);
          localStringBuilder.append(" ");
          localStringBuilder.append(localUri);
          localStringBuilder.append("]");
        }
      }
      return localStringBuilder.toString();
    }
  }
  
  class NfcApplicationState
  {
    final Application app;
    int refCount = 0;
    
    public NfcApplicationState(Application paramApplication)
    {
      app = paramApplication;
    }
    
    public void register()
    {
      refCount += 1;
      if (refCount == 1) {
        app.registerActivityLifecycleCallbacks(NfcActivityManager.this);
      }
    }
    
    public void unregister()
    {
      refCount -= 1;
      if (refCount == 0)
      {
        app.unregisterActivityLifecycleCallbacks(NfcActivityManager.this);
      }
      else if (refCount < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("-ve refcount for ");
        localStringBuilder.append(app);
        Log.e("NFC", localStringBuilder.toString());
      }
    }
  }
}
