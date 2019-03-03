package android.inputmethodservice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.InputChannel;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethod.SessionCallback;
import android.view.inputmethod.InputMethodSession;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputContext.Stub;
import com.android.internal.view.IInputMethod.Stub;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.IInputSessionCallback;
import com.android.internal.view.InputConnectionWrapper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class IInputMethodWrapper
  extends IInputMethod.Stub
  implements HandlerCaller.Callback
{
  private static final int DO_ATTACH_TOKEN = 10;
  private static final int DO_CHANGE_INPUTMETHOD_SUBTYPE = 80;
  private static final int DO_CREATE_SESSION = 40;
  private static final int DO_DUMP = 1;
  private static final int DO_HIDE_SOFT_INPUT = 70;
  private static final int DO_REVOKE_SESSION = 50;
  private static final int DO_SET_INPUT_CONTEXT = 20;
  private static final int DO_SET_SESSION_ENABLED = 45;
  private static final int DO_SHOW_SOFT_INPUT = 60;
  private static final int DO_START_INPUT = 32;
  private static final int DO_UNSET_INPUT_CONTEXT = 30;
  private static final String TAG = "InputMethodWrapper";
  final HandlerCaller mCaller;
  final Context mContext;
  final WeakReference<InputMethod> mInputMethod;
  AtomicBoolean mIsUnbindIssued = null;
  final WeakReference<AbstractInputMethodService> mTarget;
  final int mTargetSdkVersion;
  
  public IInputMethodWrapper(AbstractInputMethodService paramAbstractInputMethodService, InputMethod paramInputMethod)
  {
    mTarget = new WeakReference(paramAbstractInputMethodService);
    mContext = paramAbstractInputMethodService.getApplicationContext();
    mCaller = new HandlerCaller(mContext, null, this, true);
    mInputMethod = new WeakReference(paramInputMethod);
    mTargetSdkVersion = getApplicationInfotargetSdkVersion;
  }
  
  public void attachToken(IBinder paramIBinder)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(10, paramIBinder));
  }
  
  public void bindInput(InputBinding paramInputBinding)
  {
    if (mIsUnbindIssued != null) {
      Log.e("InputMethodWrapper", "bindInput must be paired with unbindInput.");
    }
    mIsUnbindIssued = new AtomicBoolean();
    paramInputBinding = new InputBinding(new InputConnectionWrapper(mTarget, IInputContext.Stub.asInterface(paramInputBinding.getConnectionToken()), 0, mIsUnbindIssued), paramInputBinding);
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(20, paramInputBinding));
  }
  
  public void changeInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(80, paramInputMethodSubtype));
  }
  
  public void createSession(InputChannel paramInputChannel, IInputSessionCallback paramIInputSessionCallback)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(40, paramInputChannel, paramIInputSessionCallback));
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    Object localObject = (AbstractInputMethodService)mTarget.get();
    if (localObject == null) {
      return;
    }
    if (((AbstractInputMethodService)localObject).checkCallingOrSelfPermission("android.permission.DUMP") != 0)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("Permission Denial: can't dump InputMethodManager from from pid=");
      paramFileDescriptor.append(Binder.getCallingPid());
      paramFileDescriptor.append(", uid=");
      paramFileDescriptor.append(Binder.getCallingUid());
      paramPrintWriter.println(paramFileDescriptor.toString());
      return;
    }
    localObject = new CountDownLatch(1);
    mCaller.executeOrSendMessage(mCaller.obtainMessageOOOO(1, paramFileDescriptor, paramPrintWriter, paramArrayOfString, localObject));
    try
    {
      if (!((CountDownLatch)localObject).await(5L, TimeUnit.SECONDS)) {
        paramPrintWriter.println("Timeout waiting for dump");
      }
    }
    catch (InterruptedException paramFileDescriptor)
    {
      paramPrintWriter.println("Interrupted waiting for dump");
    }
  }
  
  public void executeMessage(Message paramMessage)
  {
    ??? = (InputMethod)mInputMethod.get();
    boolean bool1 = true;
    boolean bool2 = true;
    if ((??? == null) && (what != 1))
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Input method reference was null, ignoring message: ");
      ((StringBuilder)???).append(what);
      Log.w("InputMethodWrapper", ((StringBuilder)???).toString());
      return;
    }
    Object localObject2;
    Object localObject3;
    switch (what)
    {
    default: 
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Unhandled message code: ");
      ((StringBuilder)???).append(what);
      Log.w("InputMethodWrapper", ((StringBuilder)???).toString());
      return;
    case 80: 
      ((InputMethod)???).changeInputMethodSubtype((InputMethodSubtype)obj);
      return;
    case 70: 
      ((InputMethod)???).hideSoftInput(arg1, (ResultReceiver)obj);
      return;
    case 60: 
      ((InputMethod)???).showSoftInput(arg1, (ResultReceiver)obj);
      return;
    case 50: 
      ((InputMethod)???).revokeSession((InputMethodSession)obj);
      return;
    case 45: 
      localObject2 = (InputMethodSession)obj;
      if (arg1 == 0) {
        bool2 = false;
      }
      ((InputMethod)???).setSessionEnabled((InputMethodSession)localObject2, bool2);
      return;
    case 40: 
      paramMessage = (SomeArgs)obj;
      ((InputMethod)???).createSession(new InputMethodSessionCallbackWrapper(mContext, (InputChannel)arg1, (IInputSessionCallback)arg2));
      paramMessage.recycle();
      return;
    case 32: 
      localObject2 = (SomeArgs)obj;
      int i = arg1;
      if (arg2 != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      IBinder localIBinder = (IBinder)arg1;
      IInputContext localIInputContext = (IInputContext)arg2;
      localObject3 = (EditorInfo)arg3;
      paramMessage = (AtomicBoolean)arg4;
      if (localIInputContext != null) {
        paramMessage = new InputConnectionWrapper(mTarget, localIInputContext, i, paramMessage);
      } else {
        paramMessage = null;
      }
      ((EditorInfo)localObject3).makeCompatible(mTargetSdkVersion);
      ((InputMethod)???).dispatchStartInputWithToken(paramMessage, (EditorInfo)localObject3, bool2, localIBinder);
      ((SomeArgs)localObject2).recycle();
      return;
    case 30: 
      ((InputMethod)???).unbindInput();
      return;
    case 20: 
      ((InputMethod)???).bindInput((InputBinding)obj);
      return;
    case 10: 
      ((InputMethod)???).attachToken((IBinder)obj);
      return;
    }
    ??? = (AbstractInputMethodService)mTarget.get();
    if (??? == null) {
      return;
    }
    paramMessage = (SomeArgs)obj;
    try
    {
      ((AbstractInputMethodService)???).dump((FileDescriptor)arg1, (PrintWriter)arg2, (String[])arg3);
    }
    catch (RuntimeException localRuntimeException)
    {
      localObject3 = (PrintWriter)arg2;
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Exception: ");
      ((StringBuilder)???).append(localRuntimeException);
      ((PrintWriter)localObject3).println(((StringBuilder)???).toString());
    }
    synchronized (arg4)
    {
      ((CountDownLatch)arg4).countDown();
      paramMessage.recycle();
      return;
    }
  }
  
  public void hideSoftInput(int paramInt, ResultReceiver paramResultReceiver)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageIO(70, paramInt, paramResultReceiver));
  }
  
  public void revokeSession(IInputMethodSession paramIInputMethodSession)
  {
    try
    {
      localObject = ((IInputMethodSessionWrapper)paramIInputMethodSession).getInternalInputMethodSession();
      if (localObject == null)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Session is already finished: ");
        ((StringBuilder)localObject).append(paramIInputMethodSession);
        Log.w("InputMethodWrapper", ((StringBuilder)localObject).toString());
        return;
      }
      mCaller.executeOrSendMessage(mCaller.obtainMessageO(50, localObject));
    }
    catch (ClassCastException localClassCastException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Incoming session not of correct type: ");
      ((StringBuilder)localObject).append(paramIInputMethodSession);
      Log.w("InputMethodWrapper", ((StringBuilder)localObject).toString(), localClassCastException);
    }
  }
  
  public void setSessionEnabled(IInputMethodSession paramIInputMethodSession, boolean paramBoolean)
  {
    try
    {
      localObject = ((IInputMethodSessionWrapper)paramIInputMethodSession).getInternalInputMethodSession();
      if (localObject == null)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Session is already finished: ");
        ((StringBuilder)localObject).append(paramIInputMethodSession);
        Log.w("InputMethodWrapper", ((StringBuilder)localObject).toString());
        return;
      }
      HandlerCaller localHandlerCaller1 = mCaller;
      HandlerCaller localHandlerCaller2 = mCaller;
      localHandlerCaller1.executeOrSendMessage(localHandlerCaller2.obtainMessageIO(45, paramBoolean, localObject));
    }
    catch (ClassCastException localClassCastException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Incoming session not of correct type: ");
      ((StringBuilder)localObject).append(paramIInputMethodSession);
      Log.w("InputMethodWrapper", ((StringBuilder)localObject).toString(), localClassCastException);
    }
  }
  
  public void showSoftInput(int paramInt, ResultReceiver paramResultReceiver)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageIO(60, paramInt, paramResultReceiver));
  }
  
  public void startInput(IBinder paramIBinder, IInputContext paramIInputContext, int paramInt, EditorInfo paramEditorInfo, boolean paramBoolean)
  {
    if (mIsUnbindIssued == null)
    {
      Log.e("InputMethodWrapper", "startInput must be called after bindInput.");
      mIsUnbindIssued = new AtomicBoolean();
    }
    mCaller.executeOrSendMessage(mCaller.obtainMessageIIOOOO(32, paramInt, paramBoolean, paramIBinder, paramIInputContext, paramEditorInfo, mIsUnbindIssued));
  }
  
  public void unbindInput()
  {
    if (mIsUnbindIssued != null)
    {
      mIsUnbindIssued.set(true);
      mIsUnbindIssued = null;
    }
    else
    {
      Log.e("InputMethodWrapper", "unbindInput must be paired with bindInput.");
    }
    mCaller.executeOrSendMessage(mCaller.obtainMessage(30));
  }
  
  static final class InputMethodSessionCallbackWrapper
    implements InputMethod.SessionCallback
  {
    final IInputSessionCallback mCb;
    final InputChannel mChannel;
    final Context mContext;
    
    InputMethodSessionCallbackWrapper(Context paramContext, InputChannel paramInputChannel, IInputSessionCallback paramIInputSessionCallback)
    {
      mContext = paramContext;
      mChannel = paramInputChannel;
      mCb = paramIInputSessionCallback;
    }
    
    public void sessionCreated(InputMethodSession paramInputMethodSession)
    {
      if (paramInputMethodSession != null)
      {
        try
        {
          IInputMethodSessionWrapper localIInputMethodSessionWrapper = new android/inputmethodservice/IInputMethodSessionWrapper;
          localIInputMethodSessionWrapper.<init>(mContext, paramInputMethodSession, mChannel);
          mCb.sessionCreated(localIInputMethodSessionWrapper);
        }
        catch (RemoteException paramInputMethodSession)
        {
          return;
        }
      }
      else
      {
        if (mChannel != null) {
          mChannel.dispose();
        }
        mCb.sessionCreated(null);
      }
    }
  }
}
