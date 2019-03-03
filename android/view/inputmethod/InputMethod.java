package android.view.inputmethod;

import android.os.IBinder;
import android.os.ResultReceiver;

public abstract interface InputMethod
{
  public static final String SERVICE_INTERFACE = "android.view.InputMethod";
  public static final String SERVICE_META_DATA = "android.view.im";
  public static final int SHOW_EXPLICIT = 1;
  public static final int SHOW_FORCED = 2;
  
  public abstract void attachToken(IBinder paramIBinder);
  
  public abstract void bindInput(InputBinding paramInputBinding);
  
  public abstract void changeInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype);
  
  public abstract void createSession(SessionCallback paramSessionCallback);
  
  public void dispatchStartInputWithToken(InputConnection paramInputConnection, EditorInfo paramEditorInfo, boolean paramBoolean, IBinder paramIBinder)
  {
    if (paramBoolean) {
      restartInput(paramInputConnection, paramEditorInfo);
    } else {
      startInput(paramInputConnection, paramEditorInfo);
    }
  }
  
  public abstract void hideSoftInput(int paramInt, ResultReceiver paramResultReceiver);
  
  public abstract void restartInput(InputConnection paramInputConnection, EditorInfo paramEditorInfo);
  
  public abstract void revokeSession(InputMethodSession paramInputMethodSession);
  
  public abstract void setSessionEnabled(InputMethodSession paramInputMethodSession, boolean paramBoolean);
  
  public abstract void showSoftInput(int paramInt, ResultReceiver paramResultReceiver);
  
  public abstract void startInput(InputConnection paramInputConnection, EditorInfo paramEditorInfo);
  
  public abstract void unbindInput();
  
  public static abstract interface SessionCallback
  {
    public abstract void sessionCreated(InputMethodSession paramInputMethodSession);
  }
}
