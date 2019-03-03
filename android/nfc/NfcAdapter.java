package android.nfc;

import android.annotation.SystemApi;
import android.app.Activity;
import android.app.ActivityThread;
import android.app.OnActivityPausedListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.HashMap;

public final class NfcAdapter
{
  public static final String ACTION_ADAPTER_STATE_CHANGED = "android.nfc.action.ADAPTER_STATE_CHANGED";
  public static final String ACTION_HANDOVER_TRANSFER_DONE = "android.nfc.action.HANDOVER_TRANSFER_DONE";
  public static final String ACTION_HANDOVER_TRANSFER_STARTED = "android.nfc.action.HANDOVER_TRANSFER_STARTED";
  public static final String ACTION_NDEF_DISCOVERED = "android.nfc.action.NDEF_DISCOVERED";
  public static final String ACTION_TAG_DISCOVERED = "android.nfc.action.TAG_DISCOVERED";
  public static final String ACTION_TAG_LEFT_FIELD = "android.nfc.action.TAG_LOST";
  public static final String ACTION_TECH_DISCOVERED = "android.nfc.action.TECH_DISCOVERED";
  public static final String ACTION_TRANSACTION_DETECTED = "android.nfc.action.TRANSACTION_DETECTED";
  public static final String EXTRA_ADAPTER_STATE = "android.nfc.extra.ADAPTER_STATE";
  public static final String EXTRA_AID = "android.nfc.extra.AID";
  public static final String EXTRA_DATA = "android.nfc.extra.DATA";
  public static final String EXTRA_HANDOVER_TRANSFER_STATUS = "android.nfc.extra.HANDOVER_TRANSFER_STATUS";
  public static final String EXTRA_HANDOVER_TRANSFER_URI = "android.nfc.extra.HANDOVER_TRANSFER_URI";
  public static final String EXTRA_ID = "android.nfc.extra.ID";
  public static final String EXTRA_NDEF_MESSAGES = "android.nfc.extra.NDEF_MESSAGES";
  public static final String EXTRA_READER_PRESENCE_CHECK_DELAY = "presence";
  public static final String EXTRA_SECURE_ELEMENT_NAME = "android.nfc.extra.SECURE_ELEMENT_NAME";
  public static final String EXTRA_TAG = "android.nfc.extra.TAG";
  @SystemApi
  public static final int FLAG_NDEF_PUSH_NO_CONFIRM = 1;
  public static final int FLAG_READER_NFC_A = 1;
  public static final int FLAG_READER_NFC_B = 2;
  public static final int FLAG_READER_NFC_BARCODE = 16;
  public static final int FLAG_READER_NFC_F = 4;
  public static final int FLAG_READER_NFC_V = 8;
  public static final int FLAG_READER_NO_PLATFORM_SOUNDS = 256;
  public static final int FLAG_READER_SKIP_NDEF_CHECK = 128;
  public static final int HANDOVER_TRANSFER_STATUS_FAILURE = 1;
  public static final int HANDOVER_TRANSFER_STATUS_SUCCESS = 0;
  public static final int STATE_OFF = 1;
  public static final int STATE_ON = 3;
  public static final int STATE_TURNING_OFF = 4;
  public static final int STATE_TURNING_ON = 2;
  static final String TAG = "NFC";
  static INfcCardEmulation sCardEmulationService;
  static boolean sHasNfcFeature;
  static boolean sIsInitialized = false;
  static HashMap<Context, NfcAdapter> sNfcAdapters = new HashMap();
  static INfcFCardEmulation sNfcFCardEmulationService;
  static NfcAdapter sNullContextNfcAdapter;
  static INfcAdapter sService;
  static INfcTag sTagService;
  final Context mContext;
  OnActivityPausedListener mForegroundDispatchListener = new OnActivityPausedListener()
  {
    public void onPaused(Activity paramAnonymousActivity)
    {
      disableForegroundDispatchInternal(paramAnonymousActivity, true);
    }
  };
  final Object mLock;
  final NfcActivityManager mNfcActivityManager;
  final HashMap<NfcUnlockHandler, INfcUnlockHandler> mNfcUnlockHandlers;
  ITagRemovedCallback mTagRemovedListener;
  
  NfcAdapter(Context paramContext)
  {
    mContext = paramContext;
    mNfcActivityManager = new NfcActivityManager(this);
    mNfcUnlockHandlers = new HashMap();
    mTagRemovedListener = null;
    mLock = new Object();
  }
  
  @Deprecated
  public static NfcAdapter getDefaultAdapter()
  {
    Log.w("NFC", "WARNING: NfcAdapter.getDefaultAdapter() is deprecated, use NfcAdapter.getDefaultAdapter(Context) instead", new Exception());
    return getNfcAdapter(null);
  }
  
  public static NfcAdapter getDefaultAdapter(Context paramContext)
  {
    if (paramContext != null)
    {
      paramContext = paramContext.getApplicationContext();
      if (paramContext != null)
      {
        paramContext = (NfcManager)paramContext.getSystemService("nfc");
        if (paramContext == null) {
          return null;
        }
        return paramContext.getDefaultAdapter();
      }
      throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
    }
    throw new IllegalArgumentException("context cannot be null");
  }
  
  public static NfcAdapter getNfcAdapter(Context paramContext)
  {
    try
    {
      if (!sIsInitialized)
      {
        sHasNfcFeature = hasNfcFeature();
        boolean bool1 = hasNfcHceFeature();
        if ((!sHasNfcFeature) && (!bool1))
        {
          Log.v("NFC", "this device does not have NFC support");
          paramContext = new java/lang/UnsupportedOperationException;
          paramContext.<init>();
          throw paramContext;
        }
        sService = getServiceInterface();
        if (sService != null)
        {
          boolean bool2 = sHasNfcFeature;
          if (bool2) {
            try
            {
              sTagService = sService.getNfcTagInterface();
            }
            catch (RemoteException paramContext)
            {
              Log.e("NFC", "could not retrieve NFC Tag service");
              paramContext = new java/lang/UnsupportedOperationException;
              paramContext.<init>();
              throw paramContext;
            }
          }
          if (bool1) {
            try
            {
              sNfcFCardEmulationService = sService.getNfcFCardEmulationInterface();
              try
              {
                sCardEmulationService = sService.getNfcCardEmulationInterface();
              }
              catch (RemoteException paramContext)
              {
                Log.e("NFC", "could not retrieve card emulation service");
                paramContext = new java/lang/UnsupportedOperationException;
                paramContext.<init>();
                throw paramContext;
              }
              sIsInitialized = true;
            }
            catch (RemoteException paramContext)
            {
              Log.e("NFC", "could not retrieve NFC-F card emulation service");
              paramContext = new java/lang/UnsupportedOperationException;
              paramContext.<init>();
              throw paramContext;
            }
          }
        }
        else
        {
          Log.e("NFC", "could not retrieve NFC service");
          paramContext = new java/lang/UnsupportedOperationException;
          paramContext.<init>();
          throw paramContext;
        }
      }
      if (paramContext == null)
      {
        if (sNullContextNfcAdapter == null)
        {
          paramContext = new android/nfc/NfcAdapter;
          paramContext.<init>(null);
          sNullContextNfcAdapter = paramContext;
        }
        paramContext = sNullContextNfcAdapter;
        return paramContext;
      }
      NfcAdapter localNfcAdapter1 = (NfcAdapter)sNfcAdapters.get(paramContext);
      NfcAdapter localNfcAdapter2 = localNfcAdapter1;
      if (localNfcAdapter1 == null)
      {
        localNfcAdapter2 = new android/nfc/NfcAdapter;
        localNfcAdapter2.<init>(paramContext);
        sNfcAdapters.put(paramContext, localNfcAdapter2);
      }
      return localNfcAdapter2;
    }
    finally {}
  }
  
  private static INfcAdapter getServiceInterface()
  {
    IBinder localIBinder = ServiceManager.getService("nfc");
    if (localIBinder == null) {
      return null;
    }
    return INfcAdapter.Stub.asInterface(localIBinder);
  }
  
  private static boolean hasNfcFeature()
  {
    IPackageManager localIPackageManager = ActivityThread.getPackageManager();
    if (localIPackageManager == null)
    {
      Log.e("NFC", "Cannot get package manager, assuming no NFC feature");
      return false;
    }
    try
    {
      boolean bool = localIPackageManager.hasSystemFeature("android.hardware.nfc", 0);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "Package manager query failed, assuming no NFC feature", localRemoteException);
    }
    return false;
  }
  
  private static boolean hasNfcHceFeature()
  {
    IPackageManager localIPackageManager = ActivityThread.getPackageManager();
    boolean bool1 = false;
    if (localIPackageManager == null)
    {
      Log.e("NFC", "Cannot get package manager, assuming no NFC feature");
      return false;
    }
    try
    {
      if (!localIPackageManager.hasSystemFeature("android.hardware.nfc.hce", 0))
      {
        boolean bool2 = localIPackageManager.hasSystemFeature("android.hardware.nfc.hcef", 0);
        if (!bool2) {
          break label57;
        }
      }
      bool1 = true;
      label57:
      return bool1;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "Package manager query failed, assuming no NFC feature", localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean addNfcUnlockHandler(NfcUnlockHandler paramNfcUnlockHandler, String[] paramArrayOfString)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramArrayOfString.length == 0) {
          return false;
        }
        try
        {
          synchronized (mLock)
          {
            if (mNfcUnlockHandlers.containsKey(paramNfcUnlockHandler))
            {
              sService.removeNfcUnlockHandler((INfcUnlockHandler)mNfcUnlockHandlers.get(paramNfcUnlockHandler));
              mNfcUnlockHandlers.remove(paramNfcUnlockHandler);
            }
            INfcUnlockHandler.Stub local3 = new android/nfc/NfcAdapter$3;
            local3.<init>(this, paramNfcUnlockHandler);
            sService.addNfcUnlockHandler(local3, Tag.getTechCodesFromStrings(paramArrayOfString));
            mNfcUnlockHandlers.put(paramNfcUnlockHandler, local3);
            return true;
          }
          paramNfcUnlockHandler = new java/lang/UnsupportedOperationException;
        }
        catch (IllegalArgumentException paramNfcUnlockHandler)
        {
          Log.e("NFC", "Unable to register LockscreenDispatch", paramNfcUnlockHandler);
          return false;
        }
        catch (RemoteException paramNfcUnlockHandler)
        {
          attemptDeadServiceRecovery(paramNfcUnlockHandler);
          return false;
        }
      }
      paramNfcUnlockHandler.<init>();
      throw paramNfcUnlockHandler;
    }
    finally {}
  }
  
  public void attemptDeadServiceRecovery(Exception paramException)
  {
    Log.e("NFC", "NFC service dead - attempting to recover", paramException);
    INfcAdapter localINfcAdapter = getServiceInterface();
    if (localINfcAdapter == null)
    {
      Log.e("NFC", "could not retrieve NFC service during service recovery");
      return;
    }
    sService = localINfcAdapter;
    try
    {
      sTagService = localINfcAdapter.getNfcTagInterface();
      try
      {
        sCardEmulationService = localINfcAdapter.getNfcCardEmulationInterface();
      }
      catch (RemoteException paramException)
      {
        Log.e("NFC", "could not retrieve NFC card emulation service during service recovery");
      }
      try
      {
        sNfcFCardEmulationService = localINfcAdapter.getNfcFCardEmulationInterface();
      }
      catch (RemoteException paramException)
      {
        Log.e("NFC", "could not retrieve NFC-F card emulation service during service recovery");
      }
      return;
    }
    catch (RemoteException paramException)
    {
      Log.e("NFC", "could not retrieve NFC tag service during service recovery");
    }
  }
  
  @SystemApi
  public boolean disable()
  {
    try
    {
      boolean bool = sService.disable(true);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
    return false;
  }
  
  @SystemApi
  public boolean disable(boolean paramBoolean)
  {
    try
    {
      paramBoolean = sService.disable(paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
    return false;
  }
  
  public void disableForegroundDispatch(Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        ActivityThread.currentActivityThread().unregisterOnActivityPausedListener(paramActivity, mForegroundDispatchListener);
        disableForegroundDispatchInternal(paramActivity, false);
        return;
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  void disableForegroundDispatchInternal(Activity paramActivity, boolean paramBoolean)
  {
    try
    {
      sService.setForegroundDispatch(null, null, null);
      if ((!paramBoolean) && (!paramActivity.isResumed()))
      {
        paramActivity = new java/lang/IllegalStateException;
        paramActivity.<init>("You must disable foreground dispatching while your activity is still resumed");
        throw paramActivity;
      }
    }
    catch (RemoteException paramActivity)
    {
      attemptDeadServiceRecovery(paramActivity);
    }
  }
  
  @Deprecated
  public void disableForegroundNdefPush(Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramActivity != null)
        {
          enforceResumed(paramActivity);
          mNfcActivityManager.setNdefPushMessage(paramActivity, null, 0);
          mNfcActivityManager.setNdefPushMessageCallback(paramActivity, null, 0);
          mNfcActivityManager.setOnNdefPushCompleteCallback(paramActivity, null);
          return;
        }
        throw new NullPointerException();
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  @SystemApi
  public boolean disableNdefPush()
  {
    try
    {
      if (sHasNfcFeature) {
        try
        {
          boolean bool = sService.disableNdefPush();
          return bool;
        }
        catch (RemoteException localRemoteException)
        {
          attemptDeadServiceRecovery(localRemoteException);
          return false;
        }
      }
      UnsupportedOperationException localUnsupportedOperationException = new java/lang/UnsupportedOperationException;
      localUnsupportedOperationException.<init>();
      throw localUnsupportedOperationException;
    }
    finally {}
  }
  
  public void disableReaderMode(Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        mNfcActivityManager.disableReaderMode(paramActivity);
        return;
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  public void dispatch(Tag paramTag)
  {
    if (paramTag != null)
    {
      try
      {
        sService.dispatch(paramTag);
      }
      catch (RemoteException paramTag)
      {
        attemptDeadServiceRecovery(paramTag);
      }
      return;
    }
    throw new NullPointerException("tag cannot be null");
  }
  
  @SystemApi
  public boolean enable()
  {
    try
    {
      boolean bool = sService.enable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
    return false;
  }
  
  public void enableForegroundDispatch(Activity paramActivity, PendingIntent paramPendingIntent, IntentFilter[] paramArrayOfIntentFilter, String[][] paramArrayOfString)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if ((paramActivity != null) && (paramPendingIntent != null))
        {
          if (paramActivity.isResumed())
          {
            Object localObject1 = null;
            Object localObject2 = localObject1;
            if (paramArrayOfString != null)
            {
              localObject2 = localObject1;
              try
              {
                if (paramArrayOfString.length > 0)
                {
                  localObject2 = new android/nfc/TechListParcel;
                  ((TechListParcel)localObject2).<init>(paramArrayOfString);
                }
              }
              catch (RemoteException paramActivity)
              {
                break label94;
              }
            }
            ActivityThread.currentActivityThread().registerOnActivityPausedListener(paramActivity, mForegroundDispatchListener);
            sService.setForegroundDispatch(paramPendingIntent, paramArrayOfIntentFilter, (TechListParcel)localObject2);
            break label99;
            label94:
            attemptDeadServiceRecovery(paramActivity);
            label99:
            return;
          }
          throw new IllegalStateException("Foreground dispatch can only be enabled when your activity is resumed");
        }
        throw new NullPointerException();
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  @Deprecated
  public void enableForegroundNdefPush(Activity paramActivity, NdefMessage paramNdefMessage)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if ((paramActivity != null) && (paramNdefMessage != null))
        {
          enforceResumed(paramActivity);
          mNfcActivityManager.setNdefPushMessage(paramActivity, paramNdefMessage, 0);
          return;
        }
        throw new NullPointerException();
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  @SystemApi
  public boolean enableNdefPush()
  {
    if (sHasNfcFeature) {
      try
      {
        boolean bool = sService.enableNdefPush();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        attemptDeadServiceRecovery(localRemoteException);
        return false;
      }
    }
    throw new UnsupportedOperationException();
  }
  
  public void enableReaderMode(Activity paramActivity, ReaderCallback paramReaderCallback, int paramInt, Bundle paramBundle)
  {
    try
    {
      if (sHasNfcFeature)
      {
        mNfcActivityManager.enableReaderMode(paramActivity, paramReaderCallback, paramInt, paramBundle);
        return;
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  void enforceResumed(Activity paramActivity)
  {
    if (paramActivity.isResumed()) {
      return;
    }
    throw new IllegalStateException("API cannot be called while activity is paused");
  }
  
  public int getAdapterState()
  {
    try
    {
      int i = sService.getState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
    return 1;
  }
  
  public INfcCardEmulation getCardEmulationService()
  {
    isEnabled();
    return sCardEmulationService;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public INfcAdapterExtras getNfcAdapterExtrasInterface()
  {
    if (mContext != null) {
      try
      {
        INfcAdapterExtras localINfcAdapterExtras = sService.getNfcAdapterExtrasInterface(mContext.getPackageName());
        return localINfcAdapterExtras;
      }
      catch (RemoteException localRemoteException)
      {
        attemptDeadServiceRecovery(localRemoteException);
        return null;
      }
    }
    throw new UnsupportedOperationException("You need a context on NfcAdapter to use the  NFC extras APIs");
  }
  
  public INfcDta getNfcDtaInterface()
  {
    if (mContext != null) {
      try
      {
        INfcDta localINfcDta = sService.getNfcDtaInterface(mContext.getPackageName());
        return localINfcDta;
      }
      catch (RemoteException localRemoteException)
      {
        attemptDeadServiceRecovery(localRemoteException);
        return null;
      }
    }
    throw new UnsupportedOperationException("You need a context on NfcAdapter to use the  NFC extras APIs");
  }
  
  public INfcFCardEmulation getNfcFCardEmulationService()
  {
    isEnabled();
    return sNfcFCardEmulationService;
  }
  
  int getSdkVersion()
  {
    if (mContext == null) {
      return 9;
    }
    return mContext.getApplicationInfo().targetSdkVersion;
  }
  
  public INfcAdapter getService()
  {
    isEnabled();
    return sService;
  }
  
  public INfcTag getTagService()
  {
    isEnabled();
    return sTagService;
  }
  
  public boolean ignore(Tag paramTag, int paramInt, OnTagRemovedListener arg3, final Handler paramHandler)
  {
    ITagRemovedCallback.Stub local2 = null;
    if (??? != null) {
      local2 = new ITagRemovedCallback.Stub()
      {
        public void onTagRemoved()
          throws RemoteException
        {
          if (paramHandler != null) {
            paramHandler.post(new Runnable()
            {
              public void run()
              {
                val$tagRemovedListener.onTagRemoved();
              }
            });
          } else {
            paramOnTagRemovedListener.onTagRemoved();
          }
          synchronized (mLock)
          {
            mTagRemovedListener = null;
            return;
          }
        }
      };
    }
    synchronized (mLock)
    {
      mTagRemovedListener = local2;
      try
      {
        boolean bool = sService.ignore(paramTag.getServiceHandle(), paramInt, local2);
        return bool;
      }
      catch (RemoteException paramTag)
      {
        return false;
      }
    }
  }
  
  public boolean invokeBeam(Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramActivity != null)
        {
          enforceResumed(paramActivity);
          try
          {
            sService.invokeBeam();
            return true;
          }
          catch (RemoteException paramActivity)
          {
            Log.e("NFC", "invokeBeam: NFC process has died.");
            attemptDeadServiceRecovery(paramActivity);
            return false;
          }
        }
        throw new NullPointerException("activity may not be null.");
      }
      paramActivity = new java/lang/UnsupportedOperationException;
      paramActivity.<init>();
      throw paramActivity;
    }
    finally {}
  }
  
  public boolean invokeBeam(BeamShareData paramBeamShareData)
  {
    try
    {
      Log.e("NFC", "invokeBeamInternal()");
      sService.invokeBeamInternal(paramBeamShareData);
      return true;
    }
    catch (RemoteException paramBeamShareData)
    {
      Log.e("NFC", "invokeBeam: NFC process has died.");
      attemptDeadServiceRecovery(paramBeamShareData);
    }
    return false;
  }
  
  public boolean isEnabled()
  {
    boolean bool = false;
    try
    {
      int i = sService.getState();
      if (i == 3) {
        bool = true;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
    return false;
  }
  
  public boolean isNdefPushEnabled()
  {
    try
    {
      if (sHasNfcFeature) {
        try
        {
          boolean bool = sService.isNdefPushEnabled();
          return bool;
        }
        catch (RemoteException localRemoteException)
        {
          attemptDeadServiceRecovery(localRemoteException);
          return false;
        }
      }
      UnsupportedOperationException localUnsupportedOperationException = new java/lang/UnsupportedOperationException;
      localUnsupportedOperationException.<init>();
      throw localUnsupportedOperationException;
    }
    finally {}
  }
  
  public void pausePolling(int paramInt)
  {
    try
    {
      sService.pausePolling(paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
  }
  
  @SystemApi
  public boolean removeNfcUnlockHandler(NfcUnlockHandler paramNfcUnlockHandler)
  {
    try
    {
      if (sHasNfcFeature) {
        try
        {
          synchronized (mLock)
          {
            if (mNfcUnlockHandlers.containsKey(paramNfcUnlockHandler)) {
              sService.removeNfcUnlockHandler((INfcUnlockHandler)mNfcUnlockHandlers.remove(paramNfcUnlockHandler));
            }
            return true;
          }
          paramNfcUnlockHandler = new java/lang/UnsupportedOperationException;
        }
        catch (RemoteException paramNfcUnlockHandler)
        {
          attemptDeadServiceRecovery(paramNfcUnlockHandler);
          return false;
        }
      }
      paramNfcUnlockHandler.<init>();
      throw paramNfcUnlockHandler;
    }
    finally {}
  }
  
  public void resumePolling()
  {
    try
    {
      sService.resumePolling();
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
  }
  
  public void setBeamPushUris(Uri[] paramArrayOfUri, Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramActivity != null)
        {
          if (paramArrayOfUri != null)
          {
            int i = paramArrayOfUri.length;
            int j = 0;
            while (j < i)
            {
              Object localObject = paramArrayOfUri[j];
              if (localObject != null)
              {
                localObject = ((Uri)localObject).getScheme();
                if ((localObject != null) && ((((String)localObject).equalsIgnoreCase("file")) || (((String)localObject).equalsIgnoreCase("content")))) {
                  j++;
                } else {
                  throw new IllegalArgumentException("URI needs to have either scheme file or scheme content");
                }
              }
              else
              {
                throw new NullPointerException("Uri not allowed to be null");
              }
            }
          }
          mNfcActivityManager.setNdefPushContentUri(paramActivity, paramArrayOfUri);
          return;
        }
        throw new NullPointerException("activity cannot be null");
      }
      paramArrayOfUri = new java/lang/UnsupportedOperationException;
      paramArrayOfUri.<init>();
      throw paramArrayOfUri;
    }
    finally {}
  }
  
  public void setBeamPushUrisCallback(CreateBeamUrisCallback paramCreateBeamUrisCallback, Activity paramActivity)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramActivity != null)
        {
          mNfcActivityManager.setNdefPushContentUriCallback(paramActivity, paramCreateBeamUrisCallback);
          return;
        }
        throw new NullPointerException("activity cannot be null");
      }
      paramCreateBeamUrisCallback = new java/lang/UnsupportedOperationException;
      paramCreateBeamUrisCallback.<init>();
      throw paramCreateBeamUrisCallback;
    }
    finally {}
  }
  
  @SystemApi
  public void setNdefPushMessage(NdefMessage paramNdefMessage, Activity paramActivity, int paramInt)
  {
    try
    {
      if (sHasNfcFeature)
      {
        if (paramActivity != null)
        {
          mNfcActivityManager.setNdefPushMessage(paramActivity, paramNdefMessage, paramInt);
          return;
        }
        throw new NullPointerException("activity cannot be null");
      }
      paramNdefMessage = new java/lang/UnsupportedOperationException;
      paramNdefMessage.<init>();
      throw paramNdefMessage;
    }
    finally {}
  }
  
  public void setNdefPushMessage(NdefMessage paramNdefMessage, Activity paramActivity, Activity... paramVarArgs)
  {
    try
    {
      int i;
      if (sHasNfcFeature)
      {
        i = getSdkVersion();
        if (paramActivity != null) {}
        try
        {
          mNfcActivityManager.setNdefPushMessage(paramActivity, paramNdefMessage, 0);
          int j = paramVarArgs.length;
          int k = 0;
          while (k < j)
          {
            paramActivity = paramVarArgs[k];
            if (paramActivity != null)
            {
              mNfcActivityManager.setNdefPushMessage(paramActivity, paramNdefMessage, 0);
              k++;
            }
            else
            {
              paramNdefMessage = new java/lang/NullPointerException;
              paramNdefMessage.<init>("activities cannot contain null");
              throw paramNdefMessage;
            }
          }
        }
        catch (IllegalStateException paramNdefMessage)
        {
          if (i >= 16) {
            break label119;
          }
          Log.e("NFC", "Cannot call API with Activity that has already been destroyed", paramNdefMessage);
          return;
          throw paramNdefMessage;
        }
        paramNdefMessage = new java/lang/NullPointerException;
        paramNdefMessage.<init>("activity cannot be null");
        throw paramNdefMessage;
      }
      label119:
      paramNdefMessage = new java/lang/UnsupportedOperationException;
      paramNdefMessage.<init>();
      throw paramNdefMessage;
    }
    finally {}
  }
  
  public void setNdefPushMessageCallback(CreateNdefMessageCallback paramCreateNdefMessageCallback, Activity paramActivity, int paramInt)
  {
    if (paramActivity != null)
    {
      mNfcActivityManager.setNdefPushMessageCallback(paramActivity, paramCreateNdefMessageCallback, paramInt);
      return;
    }
    throw new NullPointerException("activity cannot be null");
  }
  
  public void setNdefPushMessageCallback(CreateNdefMessageCallback paramCreateNdefMessageCallback, Activity paramActivity, Activity... paramVarArgs)
  {
    try
    {
      int i;
      if (sHasNfcFeature)
      {
        i = getSdkVersion();
        if (paramActivity != null) {}
        try
        {
          mNfcActivityManager.setNdefPushMessageCallback(paramActivity, paramCreateNdefMessageCallback, 0);
          int j = paramVarArgs.length;
          int k = 0;
          while (k < j)
          {
            paramActivity = paramVarArgs[k];
            if (paramActivity != null)
            {
              mNfcActivityManager.setNdefPushMessageCallback(paramActivity, paramCreateNdefMessageCallback, 0);
              k++;
            }
            else
            {
              paramCreateNdefMessageCallback = new java/lang/NullPointerException;
              paramCreateNdefMessageCallback.<init>("activities cannot contain null");
              throw paramCreateNdefMessageCallback;
            }
          }
        }
        catch (IllegalStateException paramCreateNdefMessageCallback)
        {
          if (i >= 16) {
            break label119;
          }
          Log.e("NFC", "Cannot call API with Activity that has already been destroyed", paramCreateNdefMessageCallback);
          return;
          throw paramCreateNdefMessageCallback;
        }
        paramCreateNdefMessageCallback = new java/lang/NullPointerException;
        paramCreateNdefMessageCallback.<init>("activity cannot be null");
        throw paramCreateNdefMessageCallback;
      }
      label119:
      paramCreateNdefMessageCallback = new java/lang/UnsupportedOperationException;
      paramCreateNdefMessageCallback.<init>();
      throw paramCreateNdefMessageCallback;
    }
    finally {}
  }
  
  public void setOnNdefPushCompleteCallback(OnNdefPushCompleteCallback paramOnNdefPushCompleteCallback, Activity paramActivity, Activity... paramVarArgs)
  {
    try
    {
      int i;
      if (sHasNfcFeature)
      {
        i = getSdkVersion();
        if (paramActivity != null) {}
        try
        {
          mNfcActivityManager.setOnNdefPushCompleteCallback(paramActivity, paramOnNdefPushCompleteCallback);
          int j = paramVarArgs.length;
          int k = 0;
          while (k < j)
          {
            paramActivity = paramVarArgs[k];
            if (paramActivity != null)
            {
              mNfcActivityManager.setOnNdefPushCompleteCallback(paramActivity, paramOnNdefPushCompleteCallback);
              k++;
            }
            else
            {
              paramOnNdefPushCompleteCallback = new java/lang/NullPointerException;
              paramOnNdefPushCompleteCallback.<init>("activities cannot contain null");
              throw paramOnNdefPushCompleteCallback;
            }
          }
        }
        catch (IllegalStateException paramOnNdefPushCompleteCallback)
        {
          if (i >= 16) {
            break label117;
          }
          Log.e("NFC", "Cannot call API with Activity that has already been destroyed", paramOnNdefPushCompleteCallback);
          return;
          throw paramOnNdefPushCompleteCallback;
        }
        paramOnNdefPushCompleteCallback = new java/lang/NullPointerException;
        paramOnNdefPushCompleteCallback.<init>("activity cannot be null");
        throw paramOnNdefPushCompleteCallback;
      }
      label117:
      paramOnNdefPushCompleteCallback = new java/lang/UnsupportedOperationException;
      paramOnNdefPushCompleteCallback.<init>();
      throw paramOnNdefPushCompleteCallback;
    }
    finally {}
  }
  
  public void setP2pModes(int paramInt1, int paramInt2)
  {
    try
    {
      sService.setP2pModes(paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException)
    {
      attemptDeadServiceRecovery(localRemoteException);
    }
  }
  
  public static abstract interface CreateBeamUrisCallback
  {
    public abstract Uri[] createBeamUris(NfcEvent paramNfcEvent);
  }
  
  public static abstract interface CreateNdefMessageCallback
  {
    public abstract NdefMessage createNdefMessage(NfcEvent paramNfcEvent);
  }
  
  @SystemApi
  public static abstract interface NfcUnlockHandler
  {
    public abstract boolean onUnlockAttempted(Tag paramTag);
  }
  
  public static abstract interface OnNdefPushCompleteCallback
  {
    public abstract void onNdefPushComplete(NfcEvent paramNfcEvent);
  }
  
  public static abstract interface OnTagRemovedListener
  {
    public abstract void onTagRemoved();
  }
  
  public static abstract interface ReaderCallback
  {
    public abstract void onTagDiscovered(Tag paramTag);
  }
}
