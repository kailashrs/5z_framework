package android.view.inputmethod;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.Trace;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.autofill.AutofillManager;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputConnectionWrapper;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodClient.Stub;
import com.android.internal.view.IInputMethodManager;
import com.android.internal.view.IInputMethodManager.Stub;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.InputBindResult;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class InputMethodManager
{
  public static final int CONTROL_START_INITIAL = 256;
  public static final int CONTROL_WINDOW_FIRST = 4;
  public static final int CONTROL_WINDOW_IS_TEXT_EDITOR = 2;
  public static final int CONTROL_WINDOW_VIEW_HAS_FOCUS = 1;
  static final boolean DEBUG = false;
  public static final int DISPATCH_HANDLED = 1;
  public static final int DISPATCH_IN_PROGRESS = -1;
  public static final int DISPATCH_NOT_HANDLED = 0;
  public static final int HIDE_IMPLICIT_ONLY = 1;
  public static final int HIDE_NOT_ALWAYS = 2;
  static final long INPUT_METHOD_NOT_RESPONDING_TIMEOUT = 2500L;
  static final int MSG_BIND = 2;
  static final int MSG_DUMP = 1;
  static final int MSG_FLUSH_INPUT_EVENT = 7;
  static final int MSG_REPORT_FULLSCREEN_MODE = 10;
  static final int MSG_SEND_INPUT_EVENT = 5;
  static final int MSG_SET_ACTIVE = 4;
  static final int MSG_SET_USER_ACTION_NOTIFICATION_SEQUENCE_NUMBER = 9;
  static final int MSG_TIMEOUT_INPUT_EVENT = 6;
  static final int MSG_UNBIND = 3;
  private static final int NOT_AN_ACTION_NOTIFICATION_SEQUENCE_NUMBER = -1;
  static final String PENDING_EVENT_COUNTER = "aq:imm";
  private static final int REQUEST_UPDATE_CURSOR_ANCHOR_INFO_NONE = 0;
  public static final int RESULT_HIDDEN = 3;
  public static final int RESULT_SHOWN = 2;
  public static final int RESULT_UNCHANGED_HIDDEN = 1;
  public static final int RESULT_UNCHANGED_SHOWN = 0;
  public static final int SHOW_FORCED = 2;
  public static final int SHOW_IMPLICIT = 1;
  public static final int SHOW_IM_PICKER_MODE_AUTO = 0;
  public static final int SHOW_IM_PICKER_MODE_EXCLUDE_AUXILIARY_SUBTYPES = 2;
  public static final int SHOW_IM_PICKER_MODE_INCLUDE_AUXILIARY_SUBTYPES = 1;
  static final String TAG = "InputMethodManager";
  static InputMethodManager sInstance;
  boolean mActive = false;
  int mBindSequence = -1;
  final IInputMethodClient.Stub mClient = new IInputMethodClient.Stub()
  {
    protected void dump(FileDescriptor paramAnonymousFileDescriptor, PrintWriter paramAnonymousPrintWriter, String[] paramAnonymousArrayOfString)
    {
      CountDownLatch localCountDownLatch = new CountDownLatch(1);
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramAnonymousFileDescriptor;
      arg2 = paramAnonymousPrintWriter;
      arg3 = paramAnonymousArrayOfString;
      arg4 = localCountDownLatch;
      mH.sendMessage(mH.obtainMessage(1, localSomeArgs));
      try
      {
        if (!localCountDownLatch.await(5L, TimeUnit.SECONDS)) {
          paramAnonymousPrintWriter.println("Timeout waiting for dump");
        }
      }
      catch (InterruptedException paramAnonymousFileDescriptor)
      {
        paramAnonymousPrintWriter.println("Interrupted waiting for dump");
      }
    }
    
    public void onBindMethod(InputBindResult paramAnonymousInputBindResult)
    {
      mH.obtainMessage(2, paramAnonymousInputBindResult).sendToTarget();
    }
    
    public void onUnbindMethod(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      mH.obtainMessage(3, paramAnonymousInt1, paramAnonymousInt2).sendToTarget();
    }
    
    public void reportFullscreenMode(boolean paramAnonymousBoolean)
    {
      mH.obtainMessage(10, paramAnonymousBoolean, 0).sendToTarget();
    }
    
    public void setActive(boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2)
    {
      mH.obtainMessage(4, paramAnonymousBoolean1, paramAnonymousBoolean2).sendToTarget();
    }
    
    public void setUserActionNotificationSequenceNumber(int paramAnonymousInt)
    {
      mH.obtainMessage(9, paramAnonymousInt, 0).sendToTarget();
    }
    
    public void setUsingInputMethod(boolean paramAnonymousBoolean) {}
  };
  CompletionInfo[] mCompletions;
  InputChannel mCurChannel;
  String mCurId;
  IInputMethodSession mCurMethod;
  View mCurRootView;
  ImeInputEventSender mCurSender;
  EditorInfo mCurrentTextBoxAttribute;
  private CursorAnchorInfo mCursorAnchorInfo = null;
  int mCursorCandEnd;
  int mCursorCandStart;
  Rect mCursorRect = new Rect();
  int mCursorSelEnd;
  int mCursorSelStart;
  final InputConnection mDummyInputConnection = new BaseInputConnection(this, false);
  boolean mFullscreenMode;
  final H mH;
  final IInputContext mIInputContext;
  private int mLastSentUserActionNotificationSequenceNumber = -1;
  final Looper mMainLooper;
  View mNextServedView;
  private int mNextUserActionNotificationSequenceNumber = -1;
  final Pools.Pool<PendingEvent> mPendingEventPool = new Pools.SimplePool(20);
  final SparseArray<PendingEvent> mPendingEvents = new SparseArray(20);
  private int mRequestUpdateCursorAnchorInfoMonitorMode = 0;
  boolean mRestartOnNextWindowFocus = true;
  boolean mServedConnecting;
  ControlledInputConnectionWrapper mServedInputConnectionWrapper;
  View mServedView;
  final IInputMethodManager mService;
  Rect mTmpCursorRect = new Rect();
  
  InputMethodManager(Looper paramLooper)
    throws ServiceManager.ServiceNotFoundException
  {
    this(IInputMethodManager.Stub.asInterface(ServiceManager.getServiceOrThrow("input_method")), paramLooper);
  }
  
  InputMethodManager(IInputMethodManager paramIInputMethodManager, Looper paramLooper)
  {
    mService = paramIInputMethodManager;
    mMainLooper = paramLooper;
    mH = new H(paramLooper);
    mIInputContext = new ControlledInputConnectionWrapper(paramLooper, mDummyInputConnection, this);
  }
  
  private static boolean canStartInput(View paramView)
  {
    boolean bool;
    if ((!paramView.hasWindowFocus()) && (!isAutofillUIShowing(paramView))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean checkFocusNoStartInput(boolean paramBoolean)
  {
    if ((mServedView == mNextServedView) && (!paramBoolean)) {
      return false;
    }
    synchronized (mH)
    {
      if ((mServedView == mNextServedView) && (!paramBoolean)) {
        return false;
      }
      if (mNextServedView == null)
      {
        finishInputLocked();
        closeCurrentInput();
        return false;
      }
      ControlledInputConnectionWrapper localControlledInputConnectionWrapper = mServedInputConnectionWrapper;
      mServedView = mNextServedView;
      mCurrentTextBoxAttribute = null;
      mCompletions = null;
      mServedConnecting = true;
      if (localControlledInputConnectionWrapper != null) {
        localControlledInputConnectionWrapper.finishComposingText();
      }
      return true;
    }
  }
  
  private static String dumpViewInfo(View paramView)
  {
    if (paramView == null) {
      return "null";
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append(paramView);
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(",focus=");
    localStringBuilder2.append(paramView.hasFocus());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(",windowFocus=");
    localStringBuilder2.append(paramView.hasWindowFocus());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(",autofillUiShowing=");
    localStringBuilder2.append(isAutofillUIShowing(paramView));
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(",window=");
    localStringBuilder2.append(paramView.getWindowToken());
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(",temporaryDetach=");
    localStringBuilder2.append(paramView.isTemporarilyDetached());
    localStringBuilder1.append(localStringBuilder2.toString());
    return localStringBuilder1.toString();
  }
  
  private void flushPendingEventsLocked()
  {
    mH.removeMessages(7);
    int i = mPendingEvents.size();
    for (int j = 0; j < i; j++)
    {
      int k = mPendingEvents.keyAt(j);
      Message localMessage = mH.obtainMessage(7, k, 0);
      localMessage.setAsynchronous(true);
      localMessage.sendToTarget();
    }
  }
  
  public static InputMethodManager getInstance()
  {
    try
    {
      Object localObject1 = sInstance;
      if (localObject1 == null) {
        try
        {
          localObject1 = new android/view/inputmethod/InputMethodManager;
          ((InputMethodManager)localObject1).<init>(Looper.getMainLooper());
          sInstance = (InputMethodManager)localObject1;
        }
        catch (ServiceManager.ServiceNotFoundException localServiceNotFoundException)
        {
          localObject1 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject1).<init>(localServiceNotFoundException);
          throw ((Throwable)localObject1);
        }
      }
      localObject1 = sInstance;
      return localObject1;
    }
    finally {}
  }
  
  private static boolean isAutofillUIShowing(View paramView)
  {
    paramView = (AutofillManager)paramView.getContext().getSystemService(AutofillManager.class);
    boolean bool;
    if ((paramView != null) && (paramView.isAutofillUiShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private PendingEvent obtainPendingEventLocked(InputEvent paramInputEvent, Object paramObject, String paramString, FinishedInputEventCallback paramFinishedInputEventCallback, Handler paramHandler)
  {
    PendingEvent localPendingEvent1 = (PendingEvent)mPendingEventPool.acquire();
    PendingEvent localPendingEvent2 = localPendingEvent1;
    if (localPendingEvent1 == null) {
      localPendingEvent2 = new PendingEvent(null);
    }
    mEvent = paramInputEvent;
    mToken = paramObject;
    mInputMethodId = paramString;
    mCallback = paramFinishedInputEventCallback;
    mHandler = paramHandler;
    return localPendingEvent2;
  }
  
  public static InputMethodManager peekInstance()
  {
    return sInstance;
  }
  
  private void recyclePendingEventLocked(PendingEvent paramPendingEvent)
  {
    paramPendingEvent.recycle();
    mPendingEventPool.release(paramPendingEvent);
  }
  
  static void scheduleCheckFocusLocked(View paramView)
  {
    paramView = paramView.getViewRootImpl();
    if (paramView != null) {
      paramView.dispatchCheckFocus();
    }
  }
  
  private void showInputMethodPickerLocked()
  {
    try
    {
      mService.showInputMethodPickerFromClient(mClient, 0);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void checkFocus()
  {
    if (checkFocusNoStartInput(false)) {
      startInputInner(4, null, 0, 0, 0);
    }
  }
  
  void clearBindingLocked()
  {
    clearConnectionLocked();
    setInputChannelLocked(null);
    mBindSequence = -1;
    mCurId = null;
    mCurMethod = null;
  }
  
  void clearConnectionLocked()
  {
    mCurrentTextBoxAttribute = null;
    if (mServedInputConnectionWrapper != null)
    {
      mServedInputConnectionWrapper.deactivate();
      mServedInputConnectionWrapper = null;
    }
  }
  
  /* Error */
  public void clearLastInputMethodWindowForTransition(IBinder paramIBinder)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: invokeinterface 451 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: goto +9 -> 30
    //   24: astore_1
    //   25: aload_1
    //   26: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   29: athrow
    //   30: aload_2
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	34	0	this	InputMethodManager
    //   0	34	1	paramIBinder	IBinder
    //   4	27	2	localH	H
    // Exception table:
    //   from	to	target	type
    //   7	17	20	finally
    //   17	19	20	finally
    //   25	30	20	finally
    //   30	32	20	finally
    //   7	17	24	android/os/RemoteException
  }
  
  void closeCurrentInput()
  {
    try
    {
      mService.hideSoftInput(mClient, 2, null);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int dispatchInputEvent(InputEvent paramInputEvent, Object paramObject, FinishedInputEventCallback paramFinishedInputEventCallback, Handler paramHandler)
  {
    synchronized (mH)
    {
      if (mCurMethod != null)
      {
        if ((paramInputEvent instanceof KeyEvent))
        {
          KeyEvent localKeyEvent = (KeyEvent)paramInputEvent;
          if ((localKeyEvent.getAction() == 0) && (localKeyEvent.getKeyCode() == 63) && (localKeyEvent.getRepeatCount() == 0))
          {
            showInputMethodPickerLocked();
            return 1;
          }
        }
        paramInputEvent = obtainPendingEventLocked(paramInputEvent, paramObject, mCurId, paramFinishedInputEventCallback, paramHandler);
        if (mMainLooper.isCurrentThread())
        {
          int i = sendInputEventOnMainLooperLocked(paramInputEvent);
          return i;
        }
        paramInputEvent = mH.obtainMessage(5, paramInputEvent);
        paramInputEvent.setAsynchronous(true);
        mH.sendMessage(paramInputEvent);
        return -1;
      }
      return 0;
    }
  }
  
  public void dispatchKeyEventFromInputMethod(View paramView, KeyEvent paramKeyEvent)
  {
    H localH = mH;
    if (paramView != null) {
      try
      {
        paramView = paramView.getViewRootImpl();
      }
      finally
      {
        break label65;
      }
    } else {
      paramView = null;
    }
    Object localObject = paramView;
    if (paramView == null)
    {
      localObject = paramView;
      if (mServedView != null) {
        localObject = mServedView.getViewRootImpl();
      }
    }
    if (localObject != null) {
      ((ViewRootImpl)localObject).dispatchKeyFromIme(paramKeyEvent);
    }
    return;
    label65:
    throw paramView;
  }
  
  public void displayCompletions(View paramView, CompletionInfo[] paramArrayOfCompletionInfo)
  {
    checkFocus();
    synchronized (mH)
    {
      if ((mServedView != paramView) && ((mServedView == null) || (!mServedView.checkInputConnectionProxy(paramView)))) {
        return;
      }
      mCompletions = paramArrayOfCompletionInfo;
      paramView = mCurMethod;
      if (paramView != null) {
        try
        {
          mCurMethod.displayCompletions(mCompletions);
        }
        catch (RemoteException paramView) {}
      }
      return;
    }
  }
  
  void doDump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new PrintWriterPrinter(paramPrintWriter);
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("Input method client state for ");
    paramPrintWriter.append(this);
    paramPrintWriter.append(":");
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mService=");
    paramPrintWriter.append(mService);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mMainLooper=");
    paramPrintWriter.append(mMainLooper);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mIInputContext=");
    paramPrintWriter.append(mIInputContext);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mActive=");
    paramPrintWriter.append(mActive);
    paramPrintWriter.append(" mRestartOnNextWindowFocus=");
    paramPrintWriter.append(mRestartOnNextWindowFocus);
    paramPrintWriter.append(" mBindSequence=");
    paramPrintWriter.append(mBindSequence);
    paramPrintWriter.append(" mCurId=");
    paramPrintWriter.append(mCurId);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mFullscreenMode=");
    paramPrintWriter.append(mFullscreenMode);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCurMethod=");
    paramPrintWriter.append(mCurMethod);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCurRootView=");
    paramPrintWriter.append(mCurRootView);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mServedView=");
    paramPrintWriter.append(mServedView);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mNextServedView=");
    paramPrintWriter.append(mNextServedView);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mServedConnecting=");
    paramPrintWriter.append(mServedConnecting);
    paramFileDescriptor.println(paramPrintWriter.toString());
    if (mCurrentTextBoxAttribute != null)
    {
      paramFileDescriptor.println("  mCurrentTextBoxAttribute:");
      mCurrentTextBoxAttribute.dump(paramFileDescriptor, "    ");
    }
    else
    {
      paramFileDescriptor.println("  mCurrentTextBoxAttribute: null");
    }
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mServedInputConnectionWrapper=");
    paramPrintWriter.append(mServedInputConnectionWrapper);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCompletions=");
    paramPrintWriter.append(Arrays.toString(mCompletions));
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCursorRect=");
    paramPrintWriter.append(mCursorRect);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCursorSelStart=");
    paramPrintWriter.append(mCursorSelStart);
    paramPrintWriter.append(" mCursorSelEnd=");
    paramPrintWriter.append(mCursorSelEnd);
    paramPrintWriter.append(" mCursorCandStart=");
    paramPrintWriter.append(mCursorCandStart);
    paramPrintWriter.append(" mCursorCandEnd=");
    paramPrintWriter.append(mCursorCandEnd);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mNextUserActionNotificationSequenceNumber=");
    paramPrintWriter.append(mNextUserActionNotificationSequenceNumber);
    paramPrintWriter.append(" mLastSentUserActionNotificationSequenceNumber=");
    paramPrintWriter.append(mLastSentUserActionNotificationSequenceNumber);
    paramFileDescriptor.println(paramPrintWriter.toString());
  }
  
  public void exposeContent(IBinder paramIBinder, InputContentInfo paramInputContentInfo, EditorInfo paramEditorInfo)
  {
    Uri localUri = paramInputContentInfo.getContentUri();
    try
    {
      paramIBinder = mService.createInputContentUriToken(paramIBinder, localUri, packageName);
      if (paramIBinder == null) {
        return;
      }
      paramInputContentInfo.setUriToken(paramIBinder);
      return;
    }
    catch (RemoteException paramInputContentInfo)
    {
      paramIBinder = new StringBuilder();
      paramIBinder.append("createInputContentAccessToken failed. contentUri=");
      paramIBinder.append(localUri.toString());
      paramIBinder.append(" packageName=");
      paramIBinder.append(packageName);
      Log.e("InputMethodManager", paramIBinder.toString(), paramInputContentInfo);
    }
  }
  
  void finishInputLocked()
  {
    mNextServedView = null;
    if (mServedView != null)
    {
      if (mCurrentTextBoxAttribute != null) {
        try
        {
          mService.finishInput(mClient);
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      mServedView = null;
      mCompletions = null;
      mServedConnecting = false;
      clearConnectionLocked();
    }
  }
  
  void finishedInputEvent(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (mH)
    {
      paramInt = mPendingEvents.indexOfKey(paramInt);
      if (paramInt < 0) {
        return;
      }
      PendingEvent localPendingEvent = (PendingEvent)mPendingEvents.valueAt(paramInt);
      mPendingEvents.removeAt(paramInt);
      Trace.traceCounter(4L, "aq:imm", mPendingEvents.size());
      if (paramBoolean2)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Timeout waiting for IME to handle input event after 2500 ms: ");
        localStringBuilder.append(mInputMethodId);
        Log.w("InputMethodManager", localStringBuilder.toString());
      }
      else
      {
        mH.removeMessages(6, localPendingEvent);
      }
      invokeFinishedInputEventCallback(localPendingEvent, paramBoolean1);
      return;
    }
  }
  
  public void focusIn(View paramView)
  {
    synchronized (mH)
    {
      focusInLocked(paramView);
      return;
    }
  }
  
  void focusInLocked(View paramView)
  {
    if ((paramView != null) && (paramView.isTemporarilyDetached())) {
      return;
    }
    if (mCurRootView != paramView.getRootView()) {
      return;
    }
    mNextServedView = paramView;
    scheduleCheckFocusLocked(paramView);
  }
  
  public void focusOut(View arg1)
  {
    synchronized (mH)
    {
      View localView = mServedView;
      return;
    }
  }
  
  public IInputMethodClient getClient()
  {
    return mClient;
  }
  
  public InputMethodSubtype getCurrentInputMethodSubtype()
  {
    try
    {
      InputMethodSubtype localInputMethodSubtype = mService.getCurrentInputMethodSubtype();
      return localInputMethodSubtype;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<InputMethodInfo> getEnabledInputMethodList()
  {
    try
    {
      List localList = mService.getEnabledInputMethodList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(InputMethodInfo paramInputMethodInfo, boolean paramBoolean)
  {
    try
    {
      IInputMethodManager localIInputMethodManager = mService;
      if (paramInputMethodInfo == null) {
        paramInputMethodInfo = null;
      } else {
        paramInputMethodInfo = paramInputMethodInfo.getId();
      }
      paramInputMethodInfo = localIInputMethodManager.getEnabledInputMethodSubtypeList(paramInputMethodInfo, paramBoolean);
      return paramInputMethodInfo;
    }
    catch (RemoteException paramInputMethodInfo)
    {
      throw paramInputMethodInfo.rethrowFromSystemServer();
    }
  }
  
  public IInputContext getInputContext()
  {
    return mIInputContext;
  }
  
  public List<InputMethodInfo> getInputMethodList()
  {
    try
    {
      List localList = mService.getInputMethodList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public int getInputMethodWindowVisibleHeight()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: invokeinterface 708 1 0
    //   16: istore_2
    //   17: aload_1
    //   18: monitorexit
    //   19: iload_2
    //   20: ireturn
    //   21: astore_3
    //   22: goto +9 -> 31
    //   25: astore_3
    //   26: aload_3
    //   27: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   30: athrow
    //   31: aload_1
    //   32: monitorexit
    //   33: aload_3
    //   34: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	35	0	this	InputMethodManager
    //   4	28	1	localH	H
    //   16	4	2	i	int
    //   21	1	3	localObject	Object
    //   25	9	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   7	17	21	finally
    //   17	19	21	finally
    //   26	31	21	finally
    //   31	33	21	finally
    //   7	17	25	android/os/RemoteException
  }
  
  /* Error */
  public InputMethodSubtype getLastInputMethodSubtype()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: invokeinterface 711 1 0
    //   16: astore_2
    //   17: aload_1
    //   18: monitorexit
    //   19: aload_2
    //   20: areturn
    //   21: astore_2
    //   22: goto +9 -> 31
    //   25: astore_2
    //   26: aload_2
    //   27: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   30: athrow
    //   31: aload_1
    //   32: monitorexit
    //   33: aload_2
    //   34: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	35	0	this	InputMethodManager
    //   4	28	1	localH	H
    //   16	4	2	localInputMethodSubtype	InputMethodSubtype
    //   21	1	2	localObject	Object
    //   25	9	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   7	17	21	finally
    //   17	19	21	finally
    //   26	31	21	finally
    //   31	33	21	finally
    //   7	17	25	android/os/RemoteException
  }
  
  public Map<InputMethodInfo, List<InputMethodSubtype>> getShortcutInputMethodsAndSubtypes()
  {
    synchronized (mH)
    {
      HashMap localHashMap = new java/util/HashMap;
      localHashMap.<init>();
      try
      {
        List localList = mService.getShortcutInputMethodsAndSubtypes();
        Object localObject1 = null;
        if ((localList != null) && (!localList.isEmpty()))
        {
          int i = localList.size();
          int j = 0;
          while (j < i)
          {
            Object localObject3 = localList.get(j);
            Object localObject4;
            if ((localObject3 instanceof InputMethodInfo))
            {
              if (localHashMap.containsKey(localObject3))
              {
                Log.e("InputMethodManager", "IMI list already contains the same InputMethod.");
                break;
              }
              localObject4 = new java/util/ArrayList;
              ((ArrayList)localObject4).<init>();
              localHashMap.put((InputMethodInfo)localObject3, localObject4);
            }
            else
            {
              localObject4 = localObject1;
              if (localObject1 != null)
              {
                localObject4 = localObject1;
                if ((localObject3 instanceof InputMethodSubtype))
                {
                  localObject1.add((InputMethodSubtype)localObject3);
                  localObject4 = localObject1;
                }
              }
            }
            j++;
            localObject1 = localObject4;
          }
        }
        return localHashMap;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public List<InputMethodInfo> getVrInputMethodList()
  {
    try
    {
      List localList = mService.getVrInputMethodList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void hideSoftInputFromInputMethod(IBinder paramIBinder, int paramInt)
  {
    hideSoftInputFromInputMethodInternal(paramIBinder, paramInt);
  }
  
  public void hideSoftInputFromInputMethodInternal(IBinder paramIBinder, int paramInt)
  {
    try
    {
      mService.hideMySoftInput(paramIBinder, paramInt);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public boolean hideSoftInputFromWindow(IBinder paramIBinder, int paramInt)
  {
    return hideSoftInputFromWindow(paramIBinder, paramInt, null);
  }
  
  public boolean hideSoftInputFromWindow(IBinder paramIBinder, int paramInt, ResultReceiver paramResultReceiver)
  {
    checkFocus();
    synchronized (mH)
    {
      if (mServedView != null)
      {
        IBinder localIBinder = mServedView.getWindowToken();
        if (localIBinder == paramIBinder) {
          try
          {
            boolean bool = mService.hideSoftInput(mClient, paramInt, paramResultReceiver);
            return bool;
          }
          catch (RemoteException paramIBinder)
          {
            throw paramIBinder.rethrowFromSystemServer();
          }
        }
      }
      return false;
    }
  }
  
  @Deprecated
  public void hideStatusIcon(IBinder paramIBinder)
  {
    hideStatusIconInternal(paramIBinder);
  }
  
  public void hideStatusIconInternal(IBinder paramIBinder)
  {
    try
    {
      mService.updateStatusIcon(paramIBinder, null, 0);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  void invokeFinishedInputEventCallback(PendingEvent paramPendingEvent, boolean paramBoolean)
  {
    mHandled = paramBoolean;
    if (mHandler.getLooper().isCurrentThread())
    {
      paramPendingEvent.run();
    }
    else
    {
      paramPendingEvent = Message.obtain(mHandler, paramPendingEvent);
      paramPendingEvent.setAsynchronous(true);
      paramPendingEvent.sendToTarget();
    }
  }
  
  public boolean isAcceptingText()
  {
    checkFocus();
    boolean bool;
    if ((mServedInputConnectionWrapper != null) && (mServedInputConnectionWrapper.getInputConnection() != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isActive()
  {
    checkFocus();
    synchronized (mH)
    {
      boolean bool;
      if ((mServedView != null) && (mCurrentTextBoxAttribute != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean isActive(View paramView)
  {
    checkFocus();
    synchronized (mH)
    {
      boolean bool;
      if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean isCursorAnchorInfoEnabled()
  {
    synchronized (mH)
    {
      int i = mRequestUpdateCursorAnchorInfoMonitorMode;
      boolean bool1 = true;
      if ((i & 0x1) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if ((mRequestUpdateCursorAnchorInfoMonitorMode & 0x2) != 0) {
        j = 1;
      } else {
        j = 0;
      }
      boolean bool2 = bool1;
      if (i == 0) {
        if (j != 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
  }
  
  public boolean isFullscreenMode()
  {
    synchronized (mH)
    {
      boolean bool = mFullscreenMode;
      return bool;
    }
  }
  
  public boolean isInputMethodPickerShown()
  {
    try
    {
      boolean bool = mService.isInputMethodPickerShownForTest();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean isWatchingCursor(View paramView)
  {
    return false;
  }
  
  public void notifySuggestionPicked(SuggestionSpan paramSuggestionSpan, String paramString, int paramInt)
  {
    try
    {
      mService.notifySuggestionPicked(paramSuggestionSpan, paramString, paramInt);
      return;
    }
    catch (RemoteException paramSuggestionSpan)
    {
      throw paramSuggestionSpan.rethrowFromSystemServer();
    }
  }
  
  public void notifyUserAction()
  {
    synchronized (mH)
    {
      if (mLastSentUserActionNotificationSequenceNumber == mNextUserActionNotificationSequenceNumber) {
        return;
      }
      try
      {
        mService.notifyUserAction(mNextUserActionNotificationSequenceNumber);
        mLastSentUserActionNotificationSequenceNumber = mNextUserActionNotificationSequenceNumber;
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  /* Error */
  public void onPostWindowFocus(View paramView1, View paramView2, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore 6
    //   6: aload 6
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 160	android/view/inputmethod/InputMethodManager:mRestartOnNextWindowFocus	Z
    //   13: ifeq +14 -> 27
    //   16: aload_0
    //   17: iconst_0
    //   18: putfield 160	android/view/inputmethod/InputMethodManager:mRestartOnNextWindowFocus	Z
    //   21: iconst_1
    //   22: istore 7
    //   24: goto +6 -> 30
    //   27: iconst_0
    //   28: istore 7
    //   30: aload_2
    //   31: ifnull +9 -> 40
    //   34: aload_2
    //   35: astore 8
    //   37: goto +6 -> 43
    //   40: aload_1
    //   41: astore 8
    //   43: aload_0
    //   44: aload 8
    //   46: invokevirtual 670	android/view/inputmethod/InputMethodManager:focusInLocked	(Landroid/view/View;)V
    //   49: aload 6
    //   51: monitorexit
    //   52: iconst_0
    //   53: istore 9
    //   55: aload_2
    //   56: ifnull +25 -> 81
    //   59: iconst_0
    //   60: iconst_1
    //   61: ior
    //   62: istore 10
    //   64: iload 10
    //   66: istore 9
    //   68: aload_2
    //   69: invokevirtual 814	android/view/View:onCheckIsTextEditor	()Z
    //   72: ifeq +9 -> 81
    //   75: iload 10
    //   77: iconst_2
    //   78: ior
    //   79: istore 9
    //   81: iload 9
    //   83: istore 10
    //   85: iload 4
    //   87: ifeq +9 -> 96
    //   90: iload 9
    //   92: iconst_4
    //   93: ior
    //   94: istore 10
    //   96: aload_0
    //   97: iload 7
    //   99: invokespecial 228	android/view/inputmethod/InputMethodManager:checkFocusNoStartInput	(Z)Z
    //   102: ifeq +21 -> 123
    //   105: aload_0
    //   106: iconst_1
    //   107: aload_1
    //   108: invokevirtual 301	android/view/View:getWindowToken	()Landroid/os/IBinder;
    //   111: iload 10
    //   113: iload_3
    //   114: iload 5
    //   116: invokevirtual 369	android/view/inputmethod/InputMethodManager:startInputInner	(ILandroid/os/IBinder;III)Z
    //   119: ifeq +4 -> 123
    //   122: return
    //   123: aload_0
    //   124: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   127: astore_2
    //   128: aload_2
    //   129: monitorenter
    //   130: aload_0
    //   131: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   134: iconst_2
    //   135: aload_0
    //   136: getfield 194	android/view/inputmethod/InputMethodManager:mClient	Lcom/android/internal/view/IInputMethodClient$Stub;
    //   139: aload_1
    //   140: invokevirtual 301	android/view/View:getWindowToken	()Landroid/os/IBinder;
    //   143: iload 10
    //   145: iload_3
    //   146: iload 5
    //   148: aconst_null
    //   149: aconst_null
    //   150: iconst_0
    //   151: aload_1
    //   152: invokevirtual 352	android/view/View:getContext	()Landroid/content/Context;
    //   155: invokevirtual 818	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   158: getfield 823	android/content/pm/ApplicationInfo:targetSdkVersion	I
    //   161: invokeinterface 827 11 0
    //   166: pop
    //   167: aload_2
    //   168: monitorexit
    //   169: return
    //   170: astore_1
    //   171: goto +9 -> 180
    //   174: astore_1
    //   175: aload_1
    //   176: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   179: athrow
    //   180: aload_2
    //   181: monitorexit
    //   182: aload_1
    //   183: athrow
    //   184: astore_1
    //   185: goto +4 -> 189
    //   188: astore_1
    //   189: aload 6
    //   191: monitorexit
    //   192: aload_1
    //   193: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	194	0	this	InputMethodManager
    //   0	194	1	paramView1	View
    //   0	194	2	paramView2	View
    //   0	194	3	paramInt1	int
    //   0	194	4	paramBoolean	boolean
    //   0	194	5	paramInt2	int
    //   4	186	6	localH	H
    //   22	76	7	bool	boolean
    //   35	10	8	localView	View
    //   53	41	9	i	int
    //   62	82	10	j	int
    // Exception table:
    //   from	to	target	type
    //   130	167	170	finally
    //   167	169	170	finally
    //   175	180	170	finally
    //   180	182	170	finally
    //   130	167	174	android/os/RemoteException
    //   43	52	184	finally
    //   9	21	188	finally
    //   189	192	188	finally
  }
  
  public void onPreWindowFocus(View paramView, boolean paramBoolean)
  {
    H localH = mH;
    if (paramView == null) {
      try
      {
        mCurRootView = null;
      }
      finally
      {
        break label51;
      }
    }
    if (paramBoolean) {
      mCurRootView = paramView;
    } else if (paramView == mCurRootView) {
      mCurRootView = null;
    }
    return;
    label51:
    throw paramView;
  }
  
  public void onViewDetachedFromWindow(View paramView)
  {
    synchronized (mH)
    {
      if (mServedView == paramView)
      {
        mNextServedView = null;
        scheduleCheckFocusLocked(paramView);
      }
      return;
    }
  }
  
  public void registerSuggestionSpansForNotification(SuggestionSpan[] paramArrayOfSuggestionSpan)
  {
    try
    {
      mService.registerSuggestionSpansForNotification(paramArrayOfSuggestionSpan);
      return;
    }
    catch (RemoteException paramArrayOfSuggestionSpan)
    {
      throw paramArrayOfSuggestionSpan.rethrowFromSystemServer();
    }
  }
  
  public void reportFullscreenMode(IBinder paramIBinder, boolean paramBoolean)
  {
    try
    {
      mService.reportFullscreenMode(paramIBinder, paramBoolean);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void restartInput(View paramView)
  {
    checkFocus();
    synchronized (mH)
    {
      if ((mServedView != paramView) && ((mServedView == null) || (!mServedView.checkInputConnectionProxy(paramView)))) {
        return;
      }
      mServedConnecting = true;
      startInputInner(3, null, 0, 0, 0);
      return;
    }
  }
  
  public void sendAppPrivateCommand(View paramView, String paramString, Bundle paramBundle)
  {
    checkFocus();
    synchronized (mH)
    {
      if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null))
      {
        paramView = mCurMethod;
        if (paramView != null)
        {
          try
          {
            mCurMethod.appPrivateCommand(paramString, paramBundle);
          }
          catch (RemoteException paramString)
          {
            paramView = new java/lang/StringBuilder;
            paramView.<init>();
            paramView.append("IME died: ");
            paramView.append(mCurId);
            Log.w("InputMethodManager", paramView.toString(), paramString);
          }
          return;
        }
      }
      return;
    }
  }
  
  void sendInputEventAndReportResultOnMainLooper(PendingEvent paramPendingEvent)
  {
    synchronized (mH)
    {
      int i = sendInputEventOnMainLooperLocked(paramPendingEvent);
      if (i == -1) {
        return;
      }
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      invokeFinishedInputEventCallback(paramPendingEvent, bool);
      return;
    }
  }
  
  int sendInputEventOnMainLooperLocked(PendingEvent paramPendingEvent)
  {
    if (mCurChannel != null)
    {
      if (mCurSender == null) {
        mCurSender = new ImeInputEventSender(mCurChannel, mH.getLooper());
      }
      InputEvent localInputEvent = mEvent;
      int i = localInputEvent.getSequenceNumber();
      if (mCurSender.sendInputEvent(i, localInputEvent))
      {
        mPendingEvents.put(i, paramPendingEvent);
        Trace.traceCounter(4L, "aq:imm", mPendingEvents.size());
        paramPendingEvent = mH.obtainMessage(6, i, 0, paramPendingEvent);
        paramPendingEvent.setAsynchronous(true);
        mH.sendMessageDelayed(paramPendingEvent, 2500L);
        return -1;
      }
      paramPendingEvent = new StringBuilder();
      paramPendingEvent.append("Unable to send input event to IME: ");
      paramPendingEvent.append(mCurId);
      paramPendingEvent.append(" dropping: ");
      paramPendingEvent.append(localInputEvent);
      Log.w("InputMethodManager", paramPendingEvent.toString());
    }
    return 0;
  }
  
  /* Error */
  public void setAdditionalInputMethodSubtypes(String paramString, InputMethodSubtype[] paramArrayOfInputMethodSubtype)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: aload_2
    //   13: invokeinterface 884 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: goto +9 -> 31
    //   25: astore_1
    //   26: aload_1
    //   27: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   30: athrow
    //   31: aload_3
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	35	0	this	InputMethodManager
    //   0	35	1	paramString	String
    //   0	35	2	paramArrayOfInputMethodSubtype	InputMethodSubtype[]
    //   4	28	3	localH	H
    // Exception table:
    //   from	to	target	type
    //   7	18	21	finally
    //   18	20	21	finally
    //   26	31	21	finally
    //   31	33	21	finally
    //   7	18	25	android/os/RemoteException
  }
  
  /* Error */
  public boolean setCurrentInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: invokeinterface 888 2 0
    //   17: istore_3
    //   18: aload_2
    //   19: monitorexit
    //   20: iload_3
    //   21: ireturn
    //   22: astore_1
    //   23: goto +9 -> 32
    //   26: astore_1
    //   27: aload_1
    //   28: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   31: athrow
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	InputMethodManager
    //   0	36	1	paramInputMethodSubtype	InputMethodSubtype
    //   4	29	2	localH	H
    //   17	4	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   7	18	22	finally
    //   18	20	22	finally
    //   27	32	22	finally
    //   32	34	22	finally
    //   7	18	26	android/os/RemoteException
  }
  
  public void setImeWindowStatus(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2)
  {
    try
    {
      mService.setImeWindowStatus(paramIBinder1, paramIBinder2, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException paramIBinder1)
    {
      throw paramIBinder1.rethrowFromSystemServer();
    }
  }
  
  void setInputChannelLocked(InputChannel paramInputChannel)
  {
    if (mCurChannel != paramInputChannel)
    {
      if (mCurSender != null)
      {
        flushPendingEventsLocked();
        mCurSender.dispose();
        mCurSender = null;
      }
      if (mCurChannel != null) {
        mCurChannel.dispose();
      }
      mCurChannel = paramInputChannel;
    }
  }
  
  @Deprecated
  public void setInputMethod(IBinder paramIBinder, String paramString)
  {
    setInputMethodInternal(paramIBinder, paramString);
  }
  
  @Deprecated
  public void setInputMethodAndSubtype(IBinder paramIBinder, String paramString, InputMethodSubtype paramInputMethodSubtype)
  {
    setInputMethodAndSubtypeInternal(paramIBinder, paramString, paramInputMethodSubtype);
  }
  
  public void setInputMethodAndSubtypeInternal(IBinder paramIBinder, String paramString, InputMethodSubtype paramInputMethodSubtype)
  {
    try
    {
      mService.setInputMethodAndSubtype(paramIBinder, paramString, paramInputMethodSubtype);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void setInputMethodInternal(IBinder paramIBinder, String paramString)
  {
    try
    {
      mService.setInputMethod(paramIBinder, paramString);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void setUpdateCursorAnchorInfoMode(int paramInt)
  {
    synchronized (mH)
    {
      mRequestUpdateCursorAnchorInfoMonitorMode = paramInt;
      return;
    }
  }
  
  @Deprecated
  public boolean shouldOfferSwitchingToNextInputMethod(IBinder paramIBinder)
  {
    return shouldOfferSwitchingToNextInputMethodInternal(paramIBinder);
  }
  
  /* Error */
  public boolean shouldOfferSwitchingToNextInputMethodInternal(IBinder paramIBinder)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: invokeinterface 922 2 0
    //   17: istore_3
    //   18: aload_2
    //   19: monitorexit
    //   20: iload_3
    //   21: ireturn
    //   22: astore_1
    //   23: goto +9 -> 32
    //   26: astore_1
    //   27: aload_1
    //   28: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   31: athrow
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	InputMethodManager
    //   0	36	1	paramIBinder	IBinder
    //   4	29	2	localH	H
    //   17	4	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   7	18	22	finally
    //   18	20	22	finally
    //   27	32	22	finally
    //   32	34	22	finally
    //   7	18	26	android/os/RemoteException
  }
  
  /* Error */
  public void showInputMethodAndSubtypeEnabler(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_0
    //   12: getfield 194	android/view/inputmethod/InputMethodManager:mClient	Lcom/android/internal/view/IInputMethodClient$Stub;
    //   15: aload_1
    //   16: invokeinterface 927 3 0
    //   21: aload_2
    //   22: monitorexit
    //   23: return
    //   24: astore_1
    //   25: goto +9 -> 34
    //   28: astore_1
    //   29: aload_1
    //   30: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   33: athrow
    //   34: aload_2
    //   35: monitorexit
    //   36: aload_1
    //   37: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	38	0	this	InputMethodManager
    //   0	38	1	paramString	String
    //   4	31	2	localH	H
    // Exception table:
    //   from	to	target	type
    //   7	21	24	finally
    //   21	23	24	finally
    //   29	34	24	finally
    //   34	36	24	finally
    //   7	21	28	android/os/RemoteException
  }
  
  public void showInputMethodPicker()
  {
    synchronized (mH)
    {
      showInputMethodPickerLocked();
      return;
    }
  }
  
  /* Error */
  public void showInputMethodPicker(boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: iload_1
    //   8: ifeq +8 -> 16
    //   11: iconst_1
    //   12: istore_3
    //   13: goto +5 -> 18
    //   16: iconst_2
    //   17: istore_3
    //   18: aload_0
    //   19: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   22: aload_0
    //   23: getfield 194	android/view/inputmethod/InputMethodManager:mClient	Lcom/android/internal/view/IInputMethodClient$Stub;
    //   26: iload_3
    //   27: invokeinterface 427 3 0
    //   32: aload_2
    //   33: monitorexit
    //   34: return
    //   35: astore 4
    //   37: goto +11 -> 48
    //   40: astore 4
    //   42: aload 4
    //   44: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   47: athrow
    //   48: aload_2
    //   49: monitorexit
    //   50: aload 4
    //   52: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	53	0	this	InputMethodManager
    //   0	53	1	paramBoolean	boolean
    //   4	45	2	localH	H
    //   12	15	3	i	int
    //   35	1	4	localObject	Object
    //   40	11	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   18	32	35	finally
    //   32	34	35	finally
    //   42	48	35	finally
    //   48	50	35	finally
    //   18	32	40	android/os/RemoteException
  }
  
  public boolean showSoftInput(View paramView, int paramInt)
  {
    return showSoftInput(paramView, paramInt, null);
  }
  
  public boolean showSoftInput(View paramView, int paramInt, ResultReceiver paramResultReceiver)
  {
    checkFocus();
    synchronized (mH)
    {
      if ((mServedView != paramView) && ((mServedView == null) || (!mServedView.checkInputConnectionProxy(paramView)))) {
        return false;
      }
      try
      {
        boolean bool = mService.showSoftInput(mClient, paramInt, paramResultReceiver);
        return bool;
      }
      catch (RemoteException paramView)
      {
        throw paramView.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void showSoftInputFromInputMethod(IBinder paramIBinder, int paramInt)
  {
    showSoftInputFromInputMethodInternal(paramIBinder, paramInt);
  }
  
  public void showSoftInputFromInputMethodInternal(IBinder paramIBinder, int paramInt)
  {
    try
    {
      mService.showMySoftInput(paramIBinder, paramInt);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void showSoftInputUnchecked(int paramInt, ResultReceiver paramResultReceiver)
  {
    try
    {
      Log.w("InputMethodManager", "showSoftInputUnchecked() is a hidden method, which will be removed soon. If you are using android.support.v7.widget.SearchView, please update to version 26.0 or newer version.");
      mService.showSoftInput(mClient, paramInt, paramResultReceiver);
      return;
    }
    catch (RemoteException paramResultReceiver)
    {
      throw paramResultReceiver.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void showStatusIcon(IBinder paramIBinder, String paramString, int paramInt)
  {
    showStatusIconInternal(paramIBinder, paramString, paramInt);
  }
  
  public void showStatusIconInternal(IBinder paramIBinder, String paramString, int paramInt)
  {
    try
    {
      mService.updateStatusIcon(paramIBinder, paramString, paramInt);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  boolean startInputInner(int paramInt1, IBinder paramIBinder, int paramInt2, int paramInt3, int paramInt4)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore 6
    //   6: aload 6
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 246	android/view/inputmethod/InputMethodManager:mServedView	Landroid/view/View;
    //   13: astore 7
    //   15: aload 7
    //   17: ifnonnull +8 -> 25
    //   20: aload 6
    //   22: monitorexit
    //   23: iconst_0
    //   24: ireturn
    //   25: aload 6
    //   27: monitorexit
    //   28: aload 7
    //   30: invokevirtual 954	android/view/View:getHandler	()Landroid/os/Handler;
    //   33: astore 8
    //   35: aload 8
    //   37: ifnonnull +9 -> 46
    //   40: aload_0
    //   41: invokevirtual 254	android/view/inputmethod/InputMethodManager:closeCurrentInput	()V
    //   44: iconst_0
    //   45: ireturn
    //   46: aload 8
    //   48: invokevirtual 781	android/os/Handler:getLooper	()Landroid/os/Looper;
    //   51: invokestatic 957	android/os/Looper:myLooper	()Landroid/os/Looper;
    //   54: if_acmpeq +20 -> 74
    //   57: aload 8
    //   59: new 959	android/view/inputmethod/_$$Lambda$InputMethodManager$jNoqB3BbMToNjx3pS_WwvtHoFfg
    //   62: dup
    //   63: aload_0
    //   64: iload_1
    //   65: invokespecial 961	android/view/inputmethod/_$$Lambda$InputMethodManager$jNoqB3BbMToNjx3pS_WwvtHoFfg:<init>	(Landroid/view/inputmethod/InputMethodManager;I)V
    //   68: invokevirtual 965	android/os/Handler:post	(Ljava/lang/Runnable;)Z
    //   71: pop
    //   72: iconst_0
    //   73: ireturn
    //   74: new 560	android/view/inputmethod/EditorInfo
    //   77: dup
    //   78: invokespecial 966	android/view/inputmethod/EditorInfo:<init>	()V
    //   81: astore 9
    //   83: aload 9
    //   85: aload 7
    //   87: invokevirtual 352	android/view/View:getContext	()Landroid/content/Context;
    //   90: invokevirtual 969	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   93: putfield 608	android/view/inputmethod/EditorInfo:packageName	Ljava/lang/String;
    //   96: aload 9
    //   98: aload 7
    //   100: invokevirtual 971	android/view/View:getId	()I
    //   103: putfield 974	android/view/inputmethod/EditorInfo:fieldId	I
    //   106: aload 7
    //   108: aload 9
    //   110: invokevirtual 978	android/view/View:onCreateInputConnection	(Landroid/view/inputmethod/EditorInfo;)Landroid/view/inputmethod/InputConnection;
    //   113: astore 10
    //   115: aload_0
    //   116: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   119: astore 6
    //   121: aload 6
    //   123: monitorenter
    //   124: aload_0
    //   125: getfield 246	android/view/inputmethod/InputMethodManager:mServedView	Landroid/view/View;
    //   128: aload 7
    //   130: if_acmpne +642 -> 772
    //   133: aload_0
    //   134: getfield 262	android/view/inputmethod/InputMethodManager:mServedConnecting	Z
    //   137: ifne +6 -> 143
    //   140: goto +632 -> 772
    //   143: aload_0
    //   144: getfield 258	android/view/inputmethod/InputMethodManager:mCurrentTextBoxAttribute	Landroid/view/inputmethod/EditorInfo;
    //   147: astore 11
    //   149: aload 11
    //   151: ifnonnull +12 -> 163
    //   154: iload_3
    //   155: sipush 256
    //   158: ior
    //   159: istore_3
    //   160: goto +3 -> 163
    //   163: aload_0
    //   164: aload 9
    //   166: putfield 258	android/view/inputmethod/InputMethodManager:mCurrentTextBoxAttribute	Landroid/view/inputmethod/EditorInfo;
    //   169: aload_0
    //   170: iconst_0
    //   171: putfield 262	android/view/inputmethod/InputMethodManager:mServedConnecting	Z
    //   174: aload_0
    //   175: getfield 256	android/view/inputmethod/InputMethodManager:mServedInputConnectionWrapper	Landroid/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper;
    //   178: astore 11
    //   180: aload 11
    //   182: ifnull +22 -> 204
    //   185: aload_0
    //   186: getfield 256	android/view/inputmethod/InputMethodManager:mServedInputConnectionWrapper	Landroid/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper;
    //   189: invokevirtual 447	android/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper:deactivate	()V
    //   192: aload_0
    //   193: aconst_null
    //   194: putfield 256	android/view/inputmethod/InputMethodManager:mServedInputConnectionWrapper	Landroid/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper;
    //   197: goto +7 -> 204
    //   200: astore_2
    //   201: goto +581 -> 782
    //   204: aload 10
    //   206: ifnull +113 -> 319
    //   209: aload_0
    //   210: aload 9
    //   212: getfield 981	android/view/inputmethod/EditorInfo:initialSelStart	I
    //   215: putfield 581	android/view/inputmethod/InputMethodManager:mCursorSelStart	I
    //   218: aload_0
    //   219: aload 9
    //   221: getfield 984	android/view/inputmethod/EditorInfo:initialSelEnd	I
    //   224: putfield 585	android/view/inputmethod/InputMethodManager:mCursorSelEnd	I
    //   227: aload_0
    //   228: iconst_m1
    //   229: putfield 589	android/view/inputmethod/InputMethodManager:mCursorCandStart	I
    //   232: aload_0
    //   233: iconst_m1
    //   234: putfield 593	android/view/inputmethod/InputMethodManager:mCursorCandEnd	I
    //   237: aload_0
    //   238: getfield 167	android/view/inputmethod/InputMethodManager:mCursorRect	Landroid/graphics/Rect;
    //   241: invokevirtual 987	android/graphics/Rect:setEmpty	()V
    //   244: aload_0
    //   245: aconst_null
    //   246: putfield 173	android/view/inputmethod/InputMethodManager:mCursorAnchorInfo	Landroid/view/inputmethod/CursorAnchorInfo;
    //   249: aload 10
    //   251: invokestatic 993	android/view/inputmethod/InputConnectionInspector:getMissingMethodFlags	(Landroid/view/inputmethod/InputConnection;)I
    //   254: istore 12
    //   256: iload 12
    //   258: bipush 32
    //   260: iand
    //   261: ifeq +9 -> 270
    //   264: aconst_null
    //   265: astore 11
    //   267: goto +12 -> 279
    //   270: aload 10
    //   272: invokeinterface 996 1 0
    //   277: astore 11
    //   279: new 8	android/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper
    //   282: astore 13
    //   284: aload 11
    //   286: ifnull +13 -> 299
    //   289: aload 11
    //   291: invokevirtual 781	android/os/Handler:getLooper	()Landroid/os/Looper;
    //   294: astore 11
    //   296: goto +10 -> 306
    //   299: aload 8
    //   301: invokevirtual 781	android/os/Handler:getLooper	()Landroid/os/Looper;
    //   304: astore 11
    //   306: aload 13
    //   308: aload 11
    //   310: aload 10
    //   312: aload_0
    //   313: invokespecial 213	android/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper:<init>	(Landroid/os/Looper;Landroid/view/inputmethod/InputConnection;Landroid/view/inputmethod/InputMethodManager;)V
    //   316: goto +9 -> 325
    //   319: iconst_0
    //   320: istore 12
    //   322: aconst_null
    //   323: astore 13
    //   325: aload_0
    //   326: aload 13
    //   328: putfield 256	android/view/inputmethod/InputMethodManager:mServedInputConnectionWrapper	Landroid/view/inputmethod/InputMethodManager$ControlledInputConnectionWrapper;
    //   331: aload_0
    //   332: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   335: astore 8
    //   337: aload_0
    //   338: getfield 194	android/view/inputmethod/InputMethodManager:mClient	Lcom/android/internal/view/IInputMethodClient$Stub;
    //   341: astore 10
    //   343: aload 7
    //   345: invokevirtual 352	android/view/View:getContext	()Landroid/content/Context;
    //   348: invokevirtual 818	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   351: getfield 823	android/content/pm/ApplicationInfo:targetSdkVersion	I
    //   354: istore 14
    //   356: aload 6
    //   358: astore 7
    //   360: aload 7
    //   362: astore 11
    //   364: aload 8
    //   366: iload_1
    //   367: aload 10
    //   369: aload_2
    //   370: iload_3
    //   371: iload 4
    //   373: iload 5
    //   375: aload 9
    //   377: aload 13
    //   379: iload 12
    //   381: iload 14
    //   383: invokeinterface 827 11 0
    //   388: astore_2
    //   389: aload_2
    //   390: ifnonnull +115 -> 505
    //   393: aload 7
    //   395: astore 11
    //   397: new 271	java/lang/StringBuilder
    //   400: astore_2
    //   401: aload 7
    //   403: astore 11
    //   405: aload_2
    //   406: invokespecial 272	java/lang/StringBuilder:<init>	()V
    //   409: aload 7
    //   411: astore 11
    //   413: aload_2
    //   414: ldc_w 998
    //   417: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   420: pop
    //   421: aload 7
    //   423: astore 11
    //   425: aload_2
    //   426: iload_1
    //   427: invokestatic 1004	com/android/internal/view/InputMethodClient:getStartInputReason	(I)Ljava/lang/String;
    //   430: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   433: pop
    //   434: aload 7
    //   436: astore 11
    //   438: aload_2
    //   439: ldc_w 1006
    //   442: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: pop
    //   446: aload 7
    //   448: astore 11
    //   450: aload_2
    //   451: aload 9
    //   453: invokevirtual 276	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   456: pop
    //   457: aload 7
    //   459: astore 11
    //   461: aload_2
    //   462: ldc_w 1008
    //   465: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   468: pop
    //   469: aload 7
    //   471: astore 11
    //   473: aload_2
    //   474: iload_3
    //   475: invokestatic 1013	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   478: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   481: pop
    //   482: aload 7
    //   484: astore 11
    //   486: ldc 76
    //   488: aload_2
    //   489: invokevirtual 291	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   492: invokestatic 1016	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   495: pop
    //   496: aload 7
    //   498: astore 11
    //   500: aload 7
    //   502: monitorexit
    //   503: iconst_0
    //   504: ireturn
    //   505: aload 7
    //   507: astore 11
    //   509: aload_2
    //   510: getfield 1021	com/android/internal/view/InputBindResult:id	Ljava/lang/String;
    //   513: ifnull +66 -> 579
    //   516: aload 7
    //   518: astore 11
    //   520: aload_0
    //   521: aload_2
    //   522: getfield 1024	com/android/internal/view/InputBindResult:channel	Landroid/view/InputChannel;
    //   525: invokevirtual 440	android/view/inputmethod/InputMethodManager:setInputChannelLocked	(Landroid/view/InputChannel;)V
    //   528: aload 7
    //   530: astore 11
    //   532: aload_0
    //   533: aload_2
    //   534: getfield 1027	com/android/internal/view/InputBindResult:sequence	I
    //   537: putfield 175	android/view/inputmethod/InputMethodManager:mBindSequence	I
    //   540: aload 7
    //   542: astore 11
    //   544: aload_0
    //   545: aload_2
    //   546: getfield 1030	com/android/internal/view/InputBindResult:method	Lcom/android/internal/view/IInputMethodSession;
    //   549: putfield 444	android/view/inputmethod/InputMethodManager:mCurMethod	Lcom/android/internal/view/IInputMethodSession;
    //   552: aload 7
    //   554: astore 11
    //   556: aload_0
    //   557: aload_2
    //   558: getfield 1021	com/android/internal/view/InputBindResult:id	Ljava/lang/String;
    //   561: putfield 442	android/view/inputmethod/InputMethodManager:mCurId	Ljava/lang/String;
    //   564: aload 7
    //   566: astore 11
    //   568: aload_0
    //   569: aload_2
    //   570: getfield 1033	com/android/internal/view/InputBindResult:userActionNotificationSequenceNumber	I
    //   573: putfield 169	android/view/inputmethod/InputMethodManager:mNextUserActionNotificationSequenceNumber	I
    //   576: goto +40 -> 616
    //   579: aload 7
    //   581: astore 11
    //   583: aload_2
    //   584: getfield 1024	com/android/internal/view/InputBindResult:channel	Landroid/view/InputChannel;
    //   587: ifnull +29 -> 616
    //   590: aload 7
    //   592: astore 11
    //   594: aload_2
    //   595: getfield 1024	com/android/internal/view/InputBindResult:channel	Landroid/view/InputChannel;
    //   598: aload_0
    //   599: getfield 852	android/view/inputmethod/InputMethodManager:mCurChannel	Landroid/view/InputChannel;
    //   602: if_acmpeq +14 -> 616
    //   605: aload 7
    //   607: astore 11
    //   609: aload_2
    //   610: getfield 1024	com/android/internal/view/InputBindResult:channel	Landroid/view/InputChannel;
    //   613: invokevirtual 900	android/view/InputChannel:dispose	()V
    //   616: aload 7
    //   618: astore 11
    //   620: aload_2
    //   621: getfield 1036	com/android/internal/view/InputBindResult:result	I
    //   624: bipush 11
    //   626: if_icmpeq +6 -> 632
    //   629: goto +12 -> 641
    //   632: aload 7
    //   634: astore 11
    //   636: aload_0
    //   637: iconst_1
    //   638: putfield 160	android/view/inputmethod/InputMethodManager:mRestartOnNextWindowFocus	Z
    //   641: aload 7
    //   643: astore 11
    //   645: aload_0
    //   646: getfield 444	android/view/inputmethod/InputMethodManager:mCurMethod	Lcom/android/internal/view/IInputMethodSession;
    //   649: ifnull +37 -> 686
    //   652: aload 7
    //   654: astore 11
    //   656: aload_0
    //   657: getfield 260	android/view/inputmethod/InputMethodManager:mCompletions	[Landroid/view/inputmethod/CompletionInfo;
    //   660: astore_2
    //   661: aload_2
    //   662: ifnull +24 -> 686
    //   665: aload 7
    //   667: astore 11
    //   669: aload_0
    //   670: getfield 444	android/view/inputmethod/InputMethodManager:mCurMethod	Lcom/android/internal/view/IInputMethodSession;
    //   673: aload_0
    //   674: getfield 260	android/view/inputmethod/InputMethodManager:mCompletions	[Landroid/view/inputmethod/CompletionInfo;
    //   677: invokeinterface 504 2 0
    //   682: goto +4 -> 686
    //   685: astore_2
    //   686: goto +73 -> 759
    //   689: astore_2
    //   690: goto +4 -> 694
    //   693: astore_2
    //   694: aload 6
    //   696: astore 7
    //   698: aload 7
    //   700: astore 11
    //   702: new 271	java/lang/StringBuilder
    //   705: astore 13
    //   707: aload 7
    //   709: astore 11
    //   711: aload 13
    //   713: invokespecial 272	java/lang/StringBuilder:<init>	()V
    //   716: aload 7
    //   718: astore 11
    //   720: aload 13
    //   722: ldc_w 847
    //   725: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   728: pop
    //   729: aload 7
    //   731: astore 11
    //   733: aload 13
    //   735: aload_0
    //   736: getfield 442	android/view/inputmethod/InputMethodManager:mCurId	Ljava/lang/String;
    //   739: invokevirtual 281	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   742: pop
    //   743: aload 7
    //   745: astore 11
    //   747: ldc 76
    //   749: aload 13
    //   751: invokevirtual 291	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   754: aload_2
    //   755: invokestatic 849	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   758: pop
    //   759: aload 6
    //   761: astore 11
    //   763: aload 6
    //   765: monitorexit
    //   766: iconst_1
    //   767: ireturn
    //   768: astore_2
    //   769: goto +13 -> 782
    //   772: aload 6
    //   774: monitorexit
    //   775: iconst_0
    //   776: ireturn
    //   777: astore_2
    //   778: goto +4 -> 782
    //   781: astore_2
    //   782: aload 6
    //   784: astore 11
    //   786: aload 6
    //   788: monitorexit
    //   789: aload_2
    //   790: athrow
    //   791: astore_2
    //   792: aload 11
    //   794: astore 6
    //   796: goto -14 -> 782
    //   799: astore_2
    //   800: aload 6
    //   802: monitorexit
    //   803: aload_2
    //   804: athrow
    //   805: astore_2
    //   806: goto -6 -> 800
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	809	0	this	InputMethodManager
    //   0	809	1	paramInt1	int
    //   0	809	2	paramIBinder	IBinder
    //   0	809	3	paramInt2	int
    //   0	809	4	paramInt3	int
    //   0	809	5	paramInt4	int
    //   4	797	6	localObject1	Object
    //   13	731	7	localObject2	Object
    //   33	332	8	localObject3	Object
    //   81	371	9	localEditorInfo	EditorInfo
    //   113	255	10	localObject4	Object
    //   147	646	11	localObject5	Object
    //   254	126	12	i	int
    //   282	468	13	localObject6	Object
    //   354	28	14	j	int
    // Exception table:
    //   from	to	target	type
    //   185	197	200	finally
    //   209	256	200	finally
    //   270	279	200	finally
    //   279	284	200	finally
    //   289	296	200	finally
    //   299	306	200	finally
    //   306	316	200	finally
    //   669	682	685	android/os/RemoteException
    //   364	389	689	android/os/RemoteException
    //   397	401	689	android/os/RemoteException
    //   405	409	689	android/os/RemoteException
    //   413	421	689	android/os/RemoteException
    //   425	434	689	android/os/RemoteException
    //   438	446	689	android/os/RemoteException
    //   450	457	689	android/os/RemoteException
    //   461	469	689	android/os/RemoteException
    //   473	482	689	android/os/RemoteException
    //   486	496	689	android/os/RemoteException
    //   509	516	689	android/os/RemoteException
    //   520	528	689	android/os/RemoteException
    //   532	540	689	android/os/RemoteException
    //   544	552	689	android/os/RemoteException
    //   556	564	689	android/os/RemoteException
    //   568	576	689	android/os/RemoteException
    //   583	590	689	android/os/RemoteException
    //   594	605	689	android/os/RemoteException
    //   609	616	689	android/os/RemoteException
    //   620	629	689	android/os/RemoteException
    //   636	641	689	android/os/RemoteException
    //   645	652	689	android/os/RemoteException
    //   656	661	689	android/os/RemoteException
    //   331	356	693	android/os/RemoteException
    //   163	180	768	finally
    //   325	331	768	finally
    //   331	356	768	finally
    //   772	775	777	finally
    //   124	140	781	finally
    //   143	149	781	finally
    //   364	389	791	finally
    //   397	401	791	finally
    //   405	409	791	finally
    //   413	421	791	finally
    //   425	434	791	finally
    //   438	446	791	finally
    //   450	457	791	finally
    //   461	469	791	finally
    //   473	482	791	finally
    //   486	496	791	finally
    //   500	503	791	finally
    //   509	516	791	finally
    //   520	528	791	finally
    //   532	540	791	finally
    //   544	552	791	finally
    //   556	564	791	finally
    //   568	576	791	finally
    //   583	590	791	finally
    //   594	605	791	finally
    //   609	616	791	finally
    //   620	629	791	finally
    //   636	641	791	finally
    //   645	652	791	finally
    //   656	661	791	finally
    //   669	682	791	finally
    //   702	707	791	finally
    //   711	716	791	finally
    //   720	729	791	finally
    //   733	743	791	finally
    //   747	759	791	finally
    //   763	766	791	finally
    //   786	789	791	finally
    //   9	15	799	finally
    //   20	23	799	finally
    //   25	28	799	finally
    //   800	803	805	finally
  }
  
  @Deprecated
  public boolean switchToLastInputMethod(IBinder paramIBinder)
  {
    return switchToPreviousInputMethodInternal(paramIBinder);
  }
  
  @Deprecated
  public boolean switchToNextInputMethod(IBinder paramIBinder, boolean paramBoolean)
  {
    return switchToNextInputMethodInternal(paramIBinder, paramBoolean);
  }
  
  /* Error */
  public boolean switchToNextInputMethodInternal(IBinder paramIBinder, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: iload_2
    //   13: invokeinterface 1047 3 0
    //   18: istore_2
    //   19: aload_3
    //   20: monitorexit
    //   21: iload_2
    //   22: ireturn
    //   23: astore_1
    //   24: goto +9 -> 33
    //   27: astore_1
    //   28: aload_1
    //   29: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   32: athrow
    //   33: aload_3
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	37	0	this	InputMethodManager
    //   0	37	1	paramIBinder	IBinder
    //   0	37	2	paramBoolean	boolean
    //   4	30	3	localH	H
    // Exception table:
    //   from	to	target	type
    //   7	19	23	finally
    //   19	21	23	finally
    //   28	33	23	finally
    //   33	35	23	finally
    //   7	19	27	android/os/RemoteException
  }
  
  /* Error */
  public boolean switchToPreviousInputMethodInternal(IBinder paramIBinder)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 210	android/view/inputmethod/InputMethodManager:mH	Landroid/view/inputmethod/InputMethodManager$H;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 203	android/view/inputmethod/InputMethodManager:mService	Lcom/android/internal/view/IInputMethodManager;
    //   11: aload_1
    //   12: invokeinterface 1050 2 0
    //   17: istore_3
    //   18: aload_2
    //   19: monitorexit
    //   20: iload_3
    //   21: ireturn
    //   22: astore_1
    //   23: goto +9 -> 32
    //   26: astore_1
    //   27: aload_1
    //   28: invokevirtual 431	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   31: athrow
    //   32: aload_2
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	InputMethodManager
    //   0	36	1	paramIBinder	IBinder
    //   4	29	2	localH	H
    //   17	4	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   7	18	22	finally
    //   18	20	22	finally
    //   27	32	22	finally
    //   32	34	22	finally
    //   7	18	26	android/os/RemoteException
  }
  
  public void toggleSoftInput(int paramInt1, int paramInt2)
  {
    if (mCurMethod != null) {
      try
      {
        mCurMethod.toggleSoftInput(paramInt1, paramInt2);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void toggleSoftInputFromWindow(IBinder paramIBinder, int paramInt1, int paramInt2)
  {
    synchronized (mH)
    {
      if ((mServedView != null) && (mServedView.getWindowToken() == paramIBinder))
      {
        paramIBinder = mCurMethod;
        if (paramIBinder != null) {
          try
          {
            mCurMethod.toggleSoftInput(paramInt1, paramInt2);
          }
          catch (RemoteException paramIBinder) {}
        }
        return;
      }
      return;
    }
  }
  
  @Deprecated
  public void updateCursor(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkFocus();
    synchronized (mH)
    {
      if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null) && (mCurMethod != null))
      {
        mTmpCursorRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
        boolean bool = mCursorRect.equals(mTmpCursorRect);
        if (!bool) {
          try
          {
            mCurMethod.updateCursor(mTmpCursorRect);
            mCursorRect.set(mTmpCursorRect);
          }
          catch (RemoteException localRemoteException)
          {
            paramView = new java/lang/StringBuilder;
            paramView.<init>();
            paramView.append("IME died: ");
            paramView.append(mCurId);
            Log.w("InputMethodManager", paramView.toString(), localRemoteException);
          }
        }
        return;
      }
      return;
    }
  }
  
  public void updateCursorAnchorInfo(View paramView, CursorAnchorInfo paramCursorAnchorInfo)
  {
    if ((paramView != null) && (paramCursorAnchorInfo != null))
    {
      checkFocus();
      synchronized (mH)
      {
        if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null) && (mCurMethod != null))
        {
          int i = mRequestUpdateCursorAnchorInfoMonitorMode;
          int j = 1;
          if ((i & 0x1) == 0) {
            j = 0;
          }
          if ((j == 0) && (Objects.equals(mCursorAnchorInfo, paramCursorAnchorInfo))) {
            return;
          }
          try
          {
            mCurMethod.updateCursorAnchorInfo(paramCursorAnchorInfo);
            mCursorAnchorInfo = paramCursorAnchorInfo;
            mRequestUpdateCursorAnchorInfoMonitorMode &= 0xFFFFFFFE;
          }
          catch (RemoteException paramCursorAnchorInfo)
          {
            paramView = new java/lang/StringBuilder;
            paramView.<init>();
            paramView.append("IME died: ");
            paramView.append(mCurId);
            Log.w("InputMethodManager", paramView.toString(), paramCursorAnchorInfo);
          }
          return;
        }
        return;
      }
    }
  }
  
  public void updateExtractedText(View paramView, int paramInt, ExtractedText paramExtractedText)
  {
    checkFocus();
    synchronized (mH)
    {
      if ((mServedView != paramView) && ((mServedView == null) || (!mServedView.checkInputConnectionProxy(paramView)))) {
        return;
      }
      paramView = mCurMethod;
      if (paramView != null) {
        try
        {
          mCurMethod.updateExtractedText(paramInt, paramExtractedText);
        }
        catch (RemoteException paramView) {}
      }
      return;
    }
  }
  
  public void updateSelection(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkFocus();
    synchronized (mH)
    {
      if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null) && (mCurMethod != null))
      {
        int i;
        if ((mCursorSelStart == paramInt1) && (mCursorSelEnd == paramInt2) && (mCursorCandStart == paramInt3))
        {
          i = mCursorCandEnd;
          if (i == paramInt4) {}
        }
        else
        {
          try
          {
            int j = mCursorSelStart;
            i = mCursorSelEnd;
            mCursorSelStart = paramInt1;
            mCursorSelEnd = paramInt2;
            mCursorCandStart = paramInt3;
            mCursorCandEnd = paramInt4;
            mCurMethod.updateSelection(j, i, paramInt1, paramInt2, paramInt3, paramInt4);
          }
          catch (RemoteException paramView)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("IME died: ");
            localStringBuilder.append(mCurId);
            Log.w("InputMethodManager", localStringBuilder.toString(), paramView);
          }
        }
        return;
      }
      return;
    }
  }
  
  public void viewClicked(View paramView)
  {
    boolean bool;
    if (mServedView != mNextServedView) {
      bool = true;
    } else {
      bool = false;
    }
    checkFocus();
    synchronized (mH)
    {
      if (((mServedView == paramView) || ((mServedView != null) && (mServedView.checkInputConnectionProxy(paramView)))) && (mCurrentTextBoxAttribute != null))
      {
        paramView = mCurMethod;
        if (paramView != null)
        {
          try
          {
            mCurMethod.viewClicked(bool);
          }
          catch (RemoteException paramView)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("IME died: ");
            localStringBuilder.append(mCurId);
            Log.w("InputMethodManager", localStringBuilder.toString(), paramView);
          }
          return;
        }
      }
      return;
    }
  }
  
  public void windowDismissed(IBinder paramIBinder)
  {
    checkFocus();
    synchronized (mH)
    {
      if ((mServedView != null) && (mServedView.getWindowToken() == paramIBinder)) {
        finishInputLocked();
      }
      return;
    }
  }
  
  private static class ControlledInputConnectionWrapper
    extends IInputConnectionWrapper
  {
    private final InputMethodManager mParentInputMethodManager;
    
    public ControlledInputConnectionWrapper(Looper paramLooper, InputConnection paramInputConnection, InputMethodManager paramInputMethodManager)
    {
      super(paramInputConnection);
      mParentInputMethodManager = paramInputMethodManager;
    }
    
    void deactivate()
    {
      if (isFinished()) {
        return;
      }
      closeConnection();
    }
    
    public boolean isActive()
    {
      boolean bool;
      if ((mParentInputMethodManager.mActive) && (!isFinished())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    protected void onUserAction()
    {
      mParentInputMethodManager.notifyUserAction();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ControlledInputConnectionWrapper{connection=");
      localStringBuilder.append(getInputConnection());
      localStringBuilder.append(" finished=");
      localStringBuilder.append(isFinished());
      localStringBuilder.append(" mParentInputMethodManager.mActive=");
      localStringBuilder.append(mParentInputMethodManager.mActive);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface FinishedInputEventCallback
  {
    public abstract void onFinishedInputEvent(Object paramObject, boolean paramBoolean);
  }
  
  class H
    extends Handler
  {
    H(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message arg1)
    {
      int i = what;
      boolean bool1 = true;
      boolean bool2 = false;
      switch (i)
      {
      case 8: 
      default: 
        return;
      case 10: 
        if (arg1 == 0) {
          bool1 = false;
        }
        ??? = null;
        synchronized (mH)
        {
          mFullscreenMode = bool1;
          if (mServedInputConnectionWrapper != null) {
            ??? = mServedInputConnectionWrapper.getInputConnection();
          }
          if (??? != null) {
            ???.reportFullscreenMode(bool1);
          }
          return;
        }
      case 9: 
        synchronized (mH)
        {
          InputMethodManager.access$302(InputMethodManager.this, arg1);
          return;
        }
      case 7: 
        finishedInputEvent(arg1, false, false);
        return;
      case 6: 
        finishedInputEvent(arg1, false, true);
        return;
      case 5: 
        sendInputEventAndReportResultOnMainLooper((InputMethodManager.PendingEvent)obj);
        return;
      case 4: 
        if (arg1 != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if (arg2 != 0) {
          bool2 = true;
        }
        synchronized (mH)
        {
          mActive = bool1;
          mFullscreenMode = bool2;
          if (!bool1)
          {
            mRestartOnNextWindowFocus = true;
            try
            {
              mIInputContext.finishComposingText();
            }
            catch (RemoteException localRemoteException) {}
          }
          if ((mServedView != null) && (InputMethodManager.canStartInput(mServedView)) && (InputMethodManager.this.checkFocusNoStartInput(mRestartOnNextWindowFocus)))
          {
            if (bool1) {}
            for (i = 7;; i = 8) {
              break;
            }
            startInputInner(i, null, 0, 0, 0);
          }
          return;
        }
      case 3: 
        int j = arg1;
        i = arg2;
        synchronized (mH)
        {
          if (mBindSequence != j) {
            return;
          }
          clearBindingLocked();
          if ((mServedView != null) && (mServedView.isFocused())) {
            mServedConnecting = true;
          }
          bool1 = mActive;
          if (bool1) {
            startInputInner(6, null, 0, 0, 0);
          }
          return;
        }
      case 2: 
        InputBindResult localInputBindResult = (InputBindResult)obj;
        synchronized (mH)
        {
          if ((mBindSequence >= 0) && (mBindSequence == sequence))
          {
            InputMethodManager.access$002(InputMethodManager.this, 0);
            setInputChannelLocked(channel);
            mCurMethod = method;
            mCurId = id;
            mBindSequence = sequence;
            startInputInner(5, null, 0, 0, 0);
            return;
          }
          ??? = new java/lang/StringBuilder;
          ((StringBuilder)???).<init>();
          ((StringBuilder)???).append("Ignoring onBind: cur seq=");
          ((StringBuilder)???).append(mBindSequence);
          ((StringBuilder)???).append(", given seq=");
          ((StringBuilder)???).append(sequence);
          Log.w("InputMethodManager", ((StringBuilder)???).toString());
          if ((channel != null) && (channel != mCurChannel)) {
            channel.dispose();
          }
          return;
        }
      }
      ??? = (SomeArgs)obj;
      try
      {
        doDump((FileDescriptor)arg1, (PrintWriter)arg2, (String[])arg3);
      }
      catch (RuntimeException localRuntimeException)
      {
        PrintWriter localPrintWriter = (PrintWriter)arg2;
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Exception: ");
        ((StringBuilder)???).append(localRuntimeException);
        localPrintWriter.println(((StringBuilder)???).toString());
      }
      synchronized (arg4)
      {
        ((CountDownLatch)arg4).countDown();
        ???.recycle();
        return;
      }
    }
  }
  
  private final class ImeInputEventSender
    extends InputEventSender
  {
    public ImeInputEventSender(InputChannel paramInputChannel, Looper paramLooper)
    {
      super(paramLooper);
    }
    
    public void onInputEventFinished(int paramInt, boolean paramBoolean)
    {
      finishedInputEvent(paramInt, paramBoolean, false);
    }
  }
  
  private final class PendingEvent
    implements Runnable
  {
    public InputMethodManager.FinishedInputEventCallback mCallback;
    public InputEvent mEvent;
    public boolean mHandled;
    public Handler mHandler;
    public String mInputMethodId;
    public Object mToken;
    
    private PendingEvent() {}
    
    public void recycle()
    {
      mEvent = null;
      mToken = null;
      mInputMethodId = null;
      mCallback = null;
      mHandler = null;
      mHandled = false;
    }
    
    public void run()
    {
      mCallback.onFinishedInputEvent(mToken, mHandled);
      synchronized (mH)
      {
        InputMethodManager.this.recyclePendingEventLocked(this);
        return;
      }
    }
  }
}
