package android.service.voice;

import android.R.styleable;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.app.VoiceInteractor.Prompt;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.SoftInputWindow;
import android.inputmethodservice.SoftInputWindow.Callback;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractor.Stub;
import com.android.internal.app.IVoiceInteractorCallback;
import com.android.internal.app.IVoiceInteractorRequest;
import com.android.internal.app.IVoiceInteractorRequest.Stub;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;

public class VoiceInteractionSession
  implements KeyEvent.Callback, ComponentCallbacks2
{
  static final boolean DEBUG = false;
  static final int MSG_CANCEL = 7;
  static final int MSG_CLOSE_SYSTEM_DIALOGS = 102;
  static final int MSG_DESTROY = 103;
  static final int MSG_HANDLE_ASSIST = 104;
  static final int MSG_HANDLE_SCREENSHOT = 105;
  static final int MSG_HIDE = 107;
  static final int MSG_ON_LOCKSCREEN_SHOWN = 108;
  static final int MSG_SHOW = 106;
  static final int MSG_START_ABORT_VOICE = 4;
  static final int MSG_START_COMMAND = 5;
  static final int MSG_START_COMPLETE_VOICE = 3;
  static final int MSG_START_CONFIRMATION = 1;
  static final int MSG_START_PICK_OPTION = 2;
  static final int MSG_SUPPORTS_COMMANDS = 6;
  static final int MSG_TASK_FINISHED = 101;
  static final int MSG_TASK_STARTED = 100;
  public static final int SHOW_SOURCE_ACTIVITY = 16;
  public static final int SHOW_SOURCE_APPLICATION = 8;
  public static final int SHOW_SOURCE_ASSIST_GESTURE = 4;
  public static final int SHOW_WITH_ASSIST = 1;
  public static final int SHOW_WITH_SCREENSHOT = 2;
  static final String TAG = "VoiceInteractionSession";
  final ArrayMap<IBinder, Request> mActiveRequests = new ArrayMap();
  final MyCallbacks mCallbacks = new MyCallbacks();
  FrameLayout mContentFrame;
  final Context mContext;
  final KeyEvent.DispatcherState mDispatcherState = new KeyEvent.DispatcherState();
  final HandlerCaller mHandlerCaller;
  boolean mInShowWindow;
  LayoutInflater mInflater;
  boolean mInitialized;
  final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new ViewTreeObserver.OnComputeInternalInsetsListener()
  {
    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo paramAnonymousInternalInsetsInfo)
    {
      onComputeInsets(mTmpInsets);
      contentInsets.set(mTmpInsets.contentInsets);
      visibleInsets.set(mTmpInsets.contentInsets);
      touchableRegion.set(mTmpInsets.touchableRegion);
      paramAnonymousInternalInsetsInfo.setTouchableInsets(mTmpInsets.touchableInsets);
    }
  };
  final IVoiceInteractor mInteractor = new IVoiceInteractor.Stub()
  {
    public IVoiceInteractorRequest startAbortVoice(String paramAnonymousString, IVoiceInteractorCallback paramAnonymousIVoiceInteractorCallback, VoiceInteractor.Prompt paramAnonymousPrompt, Bundle paramAnonymousBundle)
    {
      paramAnonymousString = new VoiceInteractionSession.AbortVoiceRequest(paramAnonymousString, Binder.getCallingUid(), paramAnonymousIVoiceInteractorCallback, VoiceInteractionSession.this, paramAnonymousPrompt, paramAnonymousBundle);
      addRequest(paramAnonymousString);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(4, paramAnonymousString));
      return mInterface;
    }
    
    public IVoiceInteractorRequest startCommand(String paramAnonymousString1, IVoiceInteractorCallback paramAnonymousIVoiceInteractorCallback, String paramAnonymousString2, Bundle paramAnonymousBundle)
    {
      paramAnonymousString1 = new VoiceInteractionSession.CommandRequest(paramAnonymousString1, Binder.getCallingUid(), paramAnonymousIVoiceInteractorCallback, VoiceInteractionSession.this, paramAnonymousString2, paramAnonymousBundle);
      addRequest(paramAnonymousString1);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(5, paramAnonymousString1));
      return mInterface;
    }
    
    public IVoiceInteractorRequest startCompleteVoice(String paramAnonymousString, IVoiceInteractorCallback paramAnonymousIVoiceInteractorCallback, VoiceInteractor.Prompt paramAnonymousPrompt, Bundle paramAnonymousBundle)
    {
      paramAnonymousString = new VoiceInteractionSession.CompleteVoiceRequest(paramAnonymousString, Binder.getCallingUid(), paramAnonymousIVoiceInteractorCallback, VoiceInteractionSession.this, paramAnonymousPrompt, paramAnonymousBundle);
      addRequest(paramAnonymousString);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(3, paramAnonymousString));
      return mInterface;
    }
    
    public IVoiceInteractorRequest startConfirmation(String paramAnonymousString, IVoiceInteractorCallback paramAnonymousIVoiceInteractorCallback, VoiceInteractor.Prompt paramAnonymousPrompt, Bundle paramAnonymousBundle)
    {
      paramAnonymousString = new VoiceInteractionSession.ConfirmationRequest(paramAnonymousString, Binder.getCallingUid(), paramAnonymousIVoiceInteractorCallback, VoiceInteractionSession.this, paramAnonymousPrompt, paramAnonymousBundle);
      addRequest(paramAnonymousString);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(1, paramAnonymousString));
      return mInterface;
    }
    
    public IVoiceInteractorRequest startPickOption(String paramAnonymousString, IVoiceInteractorCallback paramAnonymousIVoiceInteractorCallback, VoiceInteractor.Prompt paramAnonymousPrompt, VoiceInteractor.PickOptionRequest.Option[] paramAnonymousArrayOfOption, Bundle paramAnonymousBundle)
    {
      paramAnonymousString = new VoiceInteractionSession.PickOptionRequest(paramAnonymousString, Binder.getCallingUid(), paramAnonymousIVoiceInteractorCallback, VoiceInteractionSession.this, paramAnonymousPrompt, paramAnonymousArrayOfOption, paramAnonymousBundle);
      addRequest(paramAnonymousString);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(2, paramAnonymousString));
      return mInterface;
    }
    
    public boolean[] supportsCommands(String paramAnonymousString, String[] paramAnonymousArrayOfString)
    {
      paramAnonymousString = mHandlerCaller.obtainMessageIOO(6, 0, paramAnonymousArrayOfString, null);
      paramAnonymousString = mHandlerCaller.sendMessageAndWait(paramAnonymousString);
      if (paramAnonymousString != null)
      {
        paramAnonymousArrayOfString = (boolean[])arg1;
        paramAnonymousString.recycle();
        return paramAnonymousArrayOfString;
      }
      return new boolean[paramAnonymousArrayOfString.length];
    }
  };
  View mRootView;
  final IVoiceInteractionSession mSession = new IVoiceInteractionSession.Stub()
  {
    public void closeSystemDialogs()
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(102));
    }
    
    public void destroy()
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(103));
    }
    
    public void handleAssist(final Bundle paramAnonymousBundle, final AssistStructure paramAnonymousAssistStructure, final AssistContent paramAnonymousAssistContent, final int paramAnonymousInt1, final int paramAnonymousInt2)
    {
      new Thread("AssistStructure retriever")
      {
        public void run()
        {
          AssistStructure localAssistStructure1 = null;
          AssistStructure localAssistStructure2 = localAssistStructure1;
          if (paramAnonymousAssistStructure != null) {
            try
            {
              paramAnonymousAssistStructure.ensureData();
              localAssistStructure2 = localAssistStructure1;
            }
            catch (Throwable localThrowable)
            {
              Log.w("VoiceInteractionSession", "Failure retrieving AssistStructure", localThrowable);
            }
          }
          HandlerCaller localHandlerCaller1 = mHandlerCaller;
          HandlerCaller localHandlerCaller2 = mHandlerCaller;
          Bundle localBundle = paramAnonymousBundle;
          if (localThrowable == null) {
            localAssistStructure1 = paramAnonymousAssistStructure;
          } else {
            localAssistStructure1 = null;
          }
          localHandlerCaller1.sendMessage(localHandlerCaller2.obtainMessageOOOOII(104, localBundle, localAssistStructure1, localThrowable, paramAnonymousAssistContent, paramAnonymousInt1, paramAnonymousInt2));
        }
      }.start();
    }
    
    public void handleScreenshot(Bitmap paramAnonymousBitmap)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(105, paramAnonymousBitmap));
    }
    
    public void hide()
    {
      mHandlerCaller.removeMessages(106);
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(107));
    }
    
    public void onLockscreenShown()
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessage(108));
    }
    
    public void show(Bundle paramAnonymousBundle, int paramAnonymousInt, IVoiceInteractionSessionShowCallback paramAnonymousIVoiceInteractionSessionShowCallback)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIOO(106, paramAnonymousInt, paramAnonymousBundle, paramAnonymousIVoiceInteractionSessionShowCallback));
    }
    
    public void taskFinished(Intent paramAnonymousIntent, int paramAnonymousInt)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIO(101, paramAnonymousInt, paramAnonymousIntent));
    }
    
    public void taskStarted(Intent paramAnonymousIntent, int paramAnonymousInt)
    {
      mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageIO(100, paramAnonymousInt, paramAnonymousIntent));
    }
  };
  IVoiceInteractionManagerService mSystemService;
  int mTheme = 0;
  TypedArray mThemeAttrs;
  final Insets mTmpInsets = new Insets();
  IBinder mToken;
  boolean mUiEnabled = true;
  final WeakReference<VoiceInteractionSession> mWeakRef = new WeakReference(this);
  SoftInputWindow mWindow;
  boolean mWindowAdded;
  boolean mWindowVisible;
  boolean mWindowWasVisible;
  
  public VoiceInteractionSession(Context paramContext)
  {
    this(paramContext, new Handler());
  }
  
  public VoiceInteractionSession(Context paramContext, Handler paramHandler)
  {
    mContext = paramContext;
    mHandlerCaller = new HandlerCaller(paramContext, paramHandler.getLooper(), mCallbacks, true);
  }
  
  private void doOnCreate()
  {
    int i;
    if (mTheme != 0) {
      i = mTheme;
    } else {
      i = 16975041;
    }
    mTheme = i;
  }
  
  void addRequest(Request paramRequest)
  {
    try
    {
      mActiveRequests.put(mInterface.asBinder(), paramRequest);
      return;
    }
    finally {}
  }
  
  public void closeSystemDialogs()
  {
    if (mToken != null)
    {
      try
      {
        mSystemService.closeSystemDialogs(mToken);
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  void doCreate(IVoiceInteractionManagerService paramIVoiceInteractionManagerService, IBinder paramIBinder)
  {
    mSystemService = paramIVoiceInteractionManagerService;
    mToken = paramIBinder;
    onCreate();
  }
  
  void doDestroy()
  {
    onDestroy();
    if (mInitialized)
    {
      mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
      if (mWindowAdded)
      {
        mWindow.dismiss();
        mWindowAdded = false;
      }
      mInitialized = false;
    }
  }
  
  void doHide()
  {
    if (mWindowVisible)
    {
      ensureWindowHidden();
      mWindowVisible = false;
      onHide();
    }
  }
  
  void doOnHandleAssist(Bundle paramBundle, AssistStructure paramAssistStructure, Throwable paramThrowable, AssistContent paramAssistContent)
  {
    if (paramThrowable != null) {
      onAssistStructureFailure(paramThrowable);
    }
    onHandleAssist(paramBundle, paramAssistStructure, paramAssistContent);
  }
  
  void doOnHandleAssistSecondary(Bundle paramBundle, AssistStructure paramAssistStructure, Throwable paramThrowable, AssistContent paramAssistContent, int paramInt1, int paramInt2)
  {
    if (paramThrowable != null) {
      onAssistStructureFailure(paramThrowable);
    }
    onHandleAssistSecondary(paramBundle, paramAssistStructure, paramAssistContent, paramInt1, paramInt2);
  }
  
  void doShow(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback)
  {
    if (mInShowWindow)
    {
      Log.w("VoiceInteractionSession", "Re-entrance in to showWindow");
      return;
    }
    try
    {
      mInShowWindow = true;
      onPrepareShow(paramBundle, paramInt);
      if (!mWindowVisible) {
        ensureWindowAdded();
      }
      onShow(paramBundle, paramInt);
      if (!mWindowVisible)
      {
        mWindowVisible = true;
        if (mUiEnabled) {
          mWindow.show();
        }
      }
      if (paramIVoiceInteractionSessionShowCallback != null) {
        if (mUiEnabled)
        {
          mRootView.invalidate();
          paramBundle = mRootView.getViewTreeObserver();
          ViewTreeObserver.OnPreDrawListener local4 = new android/service/voice/VoiceInteractionSession$4;
          local4.<init>(this, paramIVoiceInteractionSessionShowCallback);
          paramBundle.addOnPreDrawListener(local4);
        }
        else
        {
          try
          {
            paramIVoiceInteractionSessionShowCallback.onShown();
          }
          catch (RemoteException paramBundle)
          {
            Log.w("VoiceInteractionSession", "Error calling onShown", paramBundle);
          }
        }
      }
      return;
    }
    finally
    {
      mWindowWasVisible = true;
      mInShowWindow = false;
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mToken=");
    paramPrintWriter.println(mToken);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mTheme=#");
    paramPrintWriter.println(Integer.toHexString(mTheme));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUiEnabled=");
    paramPrintWriter.println(mUiEnabled);
    paramPrintWriter.print(" mInitialized=");
    paramPrintWriter.println(mInitialized);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mWindowAdded=");
    paramPrintWriter.print(mWindowAdded);
    paramPrintWriter.print(" mWindowVisible=");
    paramPrintWriter.println(mWindowVisible);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mWindowWasVisible=");
    paramPrintWriter.print(mWindowWasVisible);
    paramPrintWriter.print(" mInShowWindow=");
    paramPrintWriter.println(mInShowWindow);
    if (mActiveRequests.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active requests:");
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("    ");
      String str = ((StringBuilder)localObject).toString();
      for (int i = 0; i < mActiveRequests.size(); i++)
      {
        localObject = (Request)mActiveRequests.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(i);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(localObject);
        ((Request)localObject).dump(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
  }
  
  void ensureWindowAdded()
  {
    if ((mUiEnabled) && (!mWindowAdded))
    {
      mWindowAdded = true;
      ensureWindowCreated();
      View localView = onCreateContentView();
      if (localView != null) {
        setContentView(localView);
      }
    }
  }
  
  void ensureWindowCreated()
  {
    if (mInitialized) {
      return;
    }
    if (mUiEnabled)
    {
      mInitialized = true;
      mInflater = ((LayoutInflater)mContext.getSystemService("layout_inflater"));
      mWindow = new SoftInputWindow(mContext, "VoiceInteractionSession", mTheme, mCallbacks, this, mDispatcherState, 2031, 80, true);
      mWindow.getWindow().addFlags(16843008);
      mThemeAttrs = mContext.obtainStyledAttributes(R.styleable.VoiceInteractionSession);
      mRootView = mInflater.inflate(17367354, null);
      mRootView.setSystemUiVisibility(1792);
      mWindow.setContentView(mRootView);
      mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(mInsetsComputer);
      mContentFrame = ((FrameLayout)mRootView.findViewById(16908290));
      mWindow.getWindow().setLayout(-1, -1);
      mWindow.setToken(mToken);
      return;
    }
    throw new IllegalStateException("setUiEnabled is false");
  }
  
  void ensureWindowHidden()
  {
    if (mWindow != null) {
      mWindow.hide();
    }
  }
  
  public void finish()
  {
    if (mToken != null)
    {
      try
      {
        mSystemService.finish(mToken);
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  public Context getContext()
  {
    return mContext;
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
  
  public LayoutInflater getLayoutInflater()
  {
    ensureWindowCreated();
    return mInflater;
  }
  
  public int getUserDisabledShowContext()
  {
    try
    {
      int i = mSystemService.getUserDisabledShowContext();
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return 0;
  }
  
  public Dialog getWindow()
  {
    ensureWindowCreated();
    return mWindow;
  }
  
  public void hide()
  {
    if (mToken != null)
    {
      try
      {
        mSystemService.hideSessionFromSession(mToken);
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  boolean isRequestActive(IBinder paramIBinder)
  {
    try
    {
      boolean bool = mActiveRequests.containsKey(paramIBinder);
      return bool;
    }
    finally {}
  }
  
  public void onAssistStructureFailure(Throwable paramThrowable) {}
  
  public void onBackPressed()
  {
    hide();
  }
  
  public void onCancelRequest(Request paramRequest) {}
  
  public void onCloseSystemDialogs()
  {
    hide();
  }
  
  public void onComputeInsets(Insets paramInsets)
  {
    contentInsets.left = 0;
    contentInsets.bottom = 0;
    contentInsets.right = 0;
    View localView = getWindow().getWindow().getDecorView();
    contentInsets.top = localView.getHeight();
    touchableInsets = 0;
    touchableRegion.setEmpty();
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public void onCreate()
  {
    doOnCreate();
  }
  
  public View onCreateContentView()
  {
    return null;
  }
  
  public void onDestroy() {}
  
  public boolean[] onGetSupportedCommands(String[] paramArrayOfString)
  {
    return new boolean[paramArrayOfString.length];
  }
  
  public void onHandleAssist(Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent) {}
  
  public void onHandleAssistSecondary(Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent, int paramInt1, int paramInt2) {}
  
  public void onHandleScreenshot(Bitmap paramBitmap) {}
  
  public void onHide() {}
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public void onLockscreenShown()
  {
    hide();
  }
  
  public void onLowMemory() {}
  
  public void onPrepareShow(Bundle paramBundle, int paramInt) {}
  
  public void onRequestAbortVoice(AbortVoiceRequest paramAbortVoiceRequest) {}
  
  public void onRequestCommand(CommandRequest paramCommandRequest) {}
  
  public void onRequestCompleteVoice(CompleteVoiceRequest paramCompleteVoiceRequest) {}
  
  public void onRequestConfirmation(ConfirmationRequest paramConfirmationRequest) {}
  
  public void onRequestPickOption(PickOptionRequest paramPickOptionRequest) {}
  
  public void onShow(Bundle paramBundle, int paramInt) {}
  
  public void onTaskFinished(Intent paramIntent, int paramInt)
  {
    hide();
  }
  
  public void onTaskStarted(Intent paramIntent, int paramInt) {}
  
  public void onTrimMemory(int paramInt) {}
  
  Request removeRequest(IBinder paramIBinder)
  {
    try
    {
      paramIBinder = (Request)mActiveRequests.remove(paramIBinder);
      return paramIBinder;
    }
    finally {}
  }
  
  public void setContentView(View paramView)
  {
    ensureWindowCreated();
    mContentFrame.removeAllViews();
    mContentFrame.addView(paramView, new FrameLayout.LayoutParams(-1, -1));
    mContentFrame.requestApplyInsets();
  }
  
  public void setDisabledShowContext(int paramInt)
  {
    try
    {
      mSystemService.setDisabledShowContext(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setKeepAwake(boolean paramBoolean)
  {
    if (mToken != null)
    {
      try
      {
        mSystemService.setKeepAwake(mToken, paramBoolean);
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  public void setTheme(int paramInt)
  {
    if (mWindow == null)
    {
      mTheme = paramInt;
      return;
    }
    throw new IllegalStateException("Must be called before onCreate()");
  }
  
  public void setUiEnabled(boolean paramBoolean)
  {
    if (mUiEnabled != paramBoolean)
    {
      mUiEnabled = paramBoolean;
      if (mWindowVisible) {
        if (paramBoolean)
        {
          ensureWindowAdded();
          mWindow.show();
        }
        else
        {
          ensureWindowHidden();
        }
      }
    }
  }
  
  public void show(Bundle paramBundle, int paramInt)
  {
    if (mToken != null)
    {
      try
      {
        mSystemService.showSessionFromSession(mToken, paramBundle, paramInt);
      }
      catch (RemoteException paramBundle) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  public void startAssistantActivity(Intent paramIntent)
  {
    if (mToken != null)
    {
      try
      {
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(mContext);
        Instrumentation.checkStartActivityResult(mSystemService.startAssistantActivity(mToken, paramIntent, paramIntent.resolveType(mContext.getContentResolver())), paramIntent);
      }
      catch (RemoteException paramIntent) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  public void startVoiceActivity(Intent paramIntent)
  {
    if (mToken != null)
    {
      try
      {
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(mContext);
        Instrumentation.checkStartActivityResult(mSystemService.startVoiceActivity(mToken, paramIntent, paramIntent.resolveType(mContext.getContentResolver())), paramIntent);
      }
      catch (RemoteException paramIntent) {}
      return;
    }
    throw new IllegalStateException("Can't call before onCreate()");
  }
  
  public static final class AbortVoiceRequest
    extends VoiceInteractionSession.Request
  {
    final VoiceInteractor.Prompt mPrompt;
    
    AbortVoiceRequest(String paramString, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      super(paramInt, paramIVoiceInteractorCallback, paramVoiceInteractionSession, paramBundle);
      mPrompt = paramPrompt;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
    }
    
    @Deprecated
    public CharSequence getMessage()
    {
      CharSequence localCharSequence;
      if (mPrompt != null) {
        localCharSequence = mPrompt.getVoicePromptAt(0);
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public VoiceInteractor.Prompt getVoicePrompt()
    {
      return mPrompt;
    }
    
    public void sendAbortResult(Bundle paramBundle)
    {
      try
      {
        finishRequest();
        mCallback.deliverAbortVoiceResult(mInterface, paramBundle);
      }
      catch (RemoteException paramBundle) {}
    }
  }
  
  public static final class CommandRequest
    extends VoiceInteractionSession.Request
  {
    final String mCommand;
    
    CommandRequest(String paramString1, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, String paramString2, Bundle paramBundle)
    {
      super(paramInt, paramIVoiceInteractorCallback, paramVoiceInteractionSession, paramBundle);
      mCommand = paramString2;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCommand=");
      paramPrintWriter.println(mCommand);
    }
    
    public String getCommand()
    {
      return mCommand;
    }
    
    void sendCommandResult(boolean paramBoolean, Bundle paramBundle)
    {
      if (paramBoolean) {
        try
        {
          finishRequest();
        }
        catch (RemoteException paramBundle)
        {
          return;
        }
      }
      mCallback.deliverCommandResult(mInterface, paramBoolean, paramBundle);
    }
    
    public void sendIntermediateResult(Bundle paramBundle)
    {
      sendCommandResult(false, paramBundle);
    }
    
    public void sendResult(Bundle paramBundle)
    {
      sendCommandResult(true, paramBundle);
    }
  }
  
  public static final class CompleteVoiceRequest
    extends VoiceInteractionSession.Request
  {
    final VoiceInteractor.Prompt mPrompt;
    
    CompleteVoiceRequest(String paramString, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      super(paramInt, paramIVoiceInteractorCallback, paramVoiceInteractionSession, paramBundle);
      mPrompt = paramPrompt;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
    }
    
    @Deprecated
    public CharSequence getMessage()
    {
      CharSequence localCharSequence;
      if (mPrompt != null) {
        localCharSequence = mPrompt.getVoicePromptAt(0);
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public VoiceInteractor.Prompt getVoicePrompt()
    {
      return mPrompt;
    }
    
    public void sendCompleteResult(Bundle paramBundle)
    {
      try
      {
        finishRequest();
        mCallback.deliverCompleteVoiceResult(mInterface, paramBundle);
      }
      catch (RemoteException paramBundle) {}
    }
  }
  
  public static final class ConfirmationRequest
    extends VoiceInteractionSession.Request
  {
    final VoiceInteractor.Prompt mPrompt;
    
    ConfirmationRequest(String paramString, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    {
      super(paramInt, paramIVoiceInteractorCallback, paramVoiceInteractionSession, paramBundle);
      mPrompt = paramPrompt;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
    }
    
    @Deprecated
    public CharSequence getPrompt()
    {
      CharSequence localCharSequence;
      if (mPrompt != null) {
        localCharSequence = mPrompt.getVoicePromptAt(0);
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public VoiceInteractor.Prompt getVoicePrompt()
    {
      return mPrompt;
    }
    
    public void sendConfirmationResult(boolean paramBoolean, Bundle paramBundle)
    {
      try
      {
        finishRequest();
        mCallback.deliverConfirmationResult(mInterface, paramBoolean, paramBundle);
      }
      catch (RemoteException paramBundle) {}
    }
  }
  
  public static final class Insets
  {
    public static final int TOUCHABLE_INSETS_CONTENT = 1;
    public static final int TOUCHABLE_INSETS_FRAME = 0;
    public static final int TOUCHABLE_INSETS_REGION = 3;
    public final Rect contentInsets = new Rect();
    public int touchableInsets;
    public final Region touchableRegion = new Region();
    
    public Insets() {}
  }
  
  class MyCallbacks
    implements HandlerCaller.Callback, SoftInputWindow.Callback
  {
    MyCallbacks() {}
    
    public void executeMessage(Message paramMessage)
    {
      SomeArgs localSomeArgs = null;
      int i = what;
      switch (i)
      {
      default: 
        switch (i)
        {
        default: 
          paramMessage = localSomeArgs;
          break;
        case 108: 
          onLockscreenShown();
          paramMessage = localSomeArgs;
          break;
        case 107: 
          doHide();
          paramMessage = localSomeArgs;
          break;
        case 106: 
          localSomeArgs = (SomeArgs)obj;
          doShow((Bundle)arg1, arg1, (IVoiceInteractionSessionShowCallback)arg2);
          paramMessage = localSomeArgs;
          break;
        case 105: 
          onHandleScreenshot((Bitmap)obj);
          paramMessage = localSomeArgs;
          break;
        case 104: 
          paramMessage = (SomeArgs)obj;
          if (argi5 == 0) {
            doOnHandleAssist((Bundle)arg1, (AssistStructure)arg2, (Throwable)arg3, (AssistContent)arg4);
          } else {
            doOnHandleAssistSecondary((Bundle)arg1, (AssistStructure)arg2, (Throwable)arg3, (AssistContent)arg4, argi5, argi6);
          }
          break;
        case 103: 
          doDestroy();
          paramMessage = localSomeArgs;
          break;
        case 102: 
          onCloseSystemDialogs();
          paramMessage = localSomeArgs;
          break;
        case 101: 
          onTaskFinished((Intent)obj, arg1);
          paramMessage = localSomeArgs;
          break;
        case 100: 
          onTaskStarted((Intent)obj, arg1);
          paramMessage = localSomeArgs;
        }
        break;
      case 7: 
        onCancelRequest((VoiceInteractionSession.Request)obj);
        paramMessage = localSomeArgs;
        break;
      case 6: 
        paramMessage = (SomeArgs)obj;
        arg1 = onGetSupportedCommands((String[])arg1);
        paramMessage.complete();
        paramMessage = null;
        break;
      case 5: 
        onRequestCommand((VoiceInteractionSession.CommandRequest)obj);
        paramMessage = localSomeArgs;
        break;
      case 4: 
        onRequestAbortVoice((VoiceInteractionSession.AbortVoiceRequest)obj);
        paramMessage = localSomeArgs;
        break;
      case 3: 
        onRequestCompleteVoice((VoiceInteractionSession.CompleteVoiceRequest)obj);
        paramMessage = localSomeArgs;
        break;
      case 2: 
        onRequestPickOption((VoiceInteractionSession.PickOptionRequest)obj);
        paramMessage = localSomeArgs;
        break;
      case 1: 
        onRequestConfirmation((VoiceInteractionSession.ConfirmationRequest)obj);
        paramMessage = localSomeArgs;
      }
      if (paramMessage != null) {
        paramMessage.recycle();
      }
    }
    
    public void onBackPressed()
    {
      VoiceInteractionSession.this.onBackPressed();
    }
  }
  
  public static final class PickOptionRequest
    extends VoiceInteractionSession.Request
  {
    final VoiceInteractor.PickOptionRequest.Option[] mOptions;
    final VoiceInteractor.Prompt mPrompt;
    
    PickOptionRequest(String paramString, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, VoiceInteractor.Prompt paramPrompt, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    {
      super(paramInt, paramIVoiceInteractorCallback, paramVoiceInteractionSession, paramBundle);
      mPrompt = paramPrompt;
      mOptions = paramArrayOfOption;
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mPrompt=");
      paramPrintWriter.println(mPrompt);
      if (mOptions != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Options:");
        for (int i = 0; i < mOptions.length; i++)
        {
          paramFileDescriptor = mOptions[i];
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(i);
          paramPrintWriter.println(":");
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("    mLabel=");
          paramPrintWriter.println(paramFileDescriptor.getLabel());
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("    mIndex=");
          paramPrintWriter.println(paramFileDescriptor.getIndex());
          if (paramFileDescriptor.countSynonyms() > 0)
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.println("    Synonyms:");
            for (int j = 0; j < paramFileDescriptor.countSynonyms(); j++)
            {
              paramPrintWriter.print(paramString);
              paramPrintWriter.print("      #");
              paramPrintWriter.print(j);
              paramPrintWriter.print(": ");
              paramPrintWriter.println(paramFileDescriptor.getSynonymAt(j));
            }
          }
          if (paramFileDescriptor.getExtras() != null)
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("    mExtras=");
            paramPrintWriter.println(paramFileDescriptor.getExtras());
          }
        }
      }
    }
    
    public VoiceInteractor.PickOptionRequest.Option[] getOptions()
    {
      return mOptions;
    }
    
    @Deprecated
    public CharSequence getPrompt()
    {
      CharSequence localCharSequence;
      if (mPrompt != null) {
        localCharSequence = mPrompt.getVoicePromptAt(0);
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public VoiceInteractor.Prompt getVoicePrompt()
    {
      return mPrompt;
    }
    
    public void sendIntermediatePickOptionResult(VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    {
      sendPickOptionResult(false, paramArrayOfOption, paramBundle);
    }
    
    void sendPickOptionResult(boolean paramBoolean, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    {
      if (paramBoolean) {
        try
        {
          finishRequest();
        }
        catch (RemoteException paramArrayOfOption)
        {
          return;
        }
      }
      mCallback.deliverPickOptionResult(mInterface, paramBoolean, paramArrayOfOption, paramBundle);
    }
    
    public void sendPickOptionResult(VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    {
      sendPickOptionResult(true, paramArrayOfOption, paramBundle);
    }
  }
  
  public static class Request
  {
    final IVoiceInteractorCallback mCallback;
    final String mCallingPackage;
    final int mCallingUid;
    final Bundle mExtras;
    final IVoiceInteractorRequest mInterface = new IVoiceInteractorRequest.Stub()
    {
      public void cancel()
        throws RemoteException
      {
        VoiceInteractionSession localVoiceInteractionSession = (VoiceInteractionSession)mSession.get();
        if (localVoiceInteractionSession != null) {
          mHandlerCaller.sendMessage(mHandlerCaller.obtainMessageO(7, VoiceInteractionSession.Request.this));
        }
      }
    };
    final WeakReference<VoiceInteractionSession> mSession;
    
    Request(String paramString, int paramInt, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractionSession paramVoiceInteractionSession, Bundle paramBundle)
    {
      mCallingPackage = paramString;
      mCallingUid = paramInt;
      mCallback = paramIVoiceInteractorCallback;
      mSession = mWeakRef;
      mExtras = paramBundle;
    }
    
    public void cancel()
    {
      try
      {
        finishRequest();
        mCallback.deliverCancel(mInterface);
      }
      catch (RemoteException localRemoteException) {}
    }
    
    void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mInterface=");
      paramPrintWriter.println(mInterface.asBinder());
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCallingPackage=");
      paramPrintWriter.print(mCallingPackage);
      paramPrintWriter.print(" mCallingUid=");
      UserHandle.formatUid(paramPrintWriter, mCallingUid);
      paramPrintWriter.println();
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCallback=");
      paramPrintWriter.println(mCallback.asBinder());
      if (mExtras != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mExtras=");
        paramPrintWriter.println(mExtras);
      }
    }
    
    void finishRequest()
    {
      Object localObject = (VoiceInteractionSession)mSession.get();
      if (localObject != null)
      {
        Request localRequest = ((VoiceInteractionSession)localObject).removeRequest(mInterface.asBinder());
        if (localRequest != null)
        {
          if (localRequest == this) {
            return;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Current active request ");
          ((StringBuilder)localObject).append(localRequest);
          ((StringBuilder)localObject).append(" not same as calling request ");
          ((StringBuilder)localObject).append(this);
          throw new IllegalStateException(((StringBuilder)localObject).toString());
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Request not active: ");
        ((StringBuilder)localObject).append(this);
        throw new IllegalStateException(((StringBuilder)localObject).toString());
      }
      throw new IllegalStateException("VoiceInteractionSession has been destroyed");
    }
    
    public String getCallingPackage()
    {
      return mCallingPackage;
    }
    
    public int getCallingUid()
    {
      return mCallingUid;
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public boolean isActive()
    {
      VoiceInteractionSession localVoiceInteractionSession = (VoiceInteractionSession)mSession.get();
      if (localVoiceInteractionSession == null) {
        return false;
      }
      return localVoiceInteractionSession.isRequestActive(mInterface.asBinder());
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      DebugUtils.buildShortClassTag(this, localStringBuilder);
      localStringBuilder.append(" ");
      localStringBuilder.append(mInterface.asBinder());
      localStringBuilder.append(" pkg=");
      localStringBuilder.append(mCallingPackage);
      localStringBuilder.append(" uid=");
      UserHandle.formatUid(localStringBuilder, mCallingUid);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
