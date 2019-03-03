package android.inputmethodservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethod.SessionCallback;
import android.view.inputmethod.InputMethodSession;
import android.view.inputmethod.InputMethodSession.EventCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class AbstractInputMethodService
  extends Service
  implements KeyEvent.Callback
{
  final KeyEvent.DispatcherState mDispatcherState = new KeyEvent.DispatcherState();
  private InputMethod mInputMethod;
  
  public AbstractInputMethodService() {}
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  public void exposeContent(InputContentInfo paramInputContentInfo, InputConnection paramInputConnection) {}
  
  public KeyEvent.DispatcherState getKeyDispatcherState()
  {
    return mDispatcherState;
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    if (mInputMethod == null) {
      mInputMethod = onCreateInputMethodInterface();
    }
    return new IInputMethodWrapper(this, mInputMethod);
  }
  
  public abstract AbstractInputMethodImpl onCreateInputMethodInterface();
  
  public abstract AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface();
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public abstract class AbstractInputMethodImpl
    implements InputMethod
  {
    public AbstractInputMethodImpl() {}
    
    public void createSession(InputMethod.SessionCallback paramSessionCallback)
    {
      paramSessionCallback.sessionCreated(onCreateInputMethodSessionInterface());
    }
    
    public void revokeSession(InputMethodSession paramInputMethodSession)
    {
      ((AbstractInputMethodService.AbstractInputMethodSessionImpl)paramInputMethodSession).revokeSelf();
    }
    
    public void setSessionEnabled(InputMethodSession paramInputMethodSession, boolean paramBoolean)
    {
      ((AbstractInputMethodService.AbstractInputMethodSessionImpl)paramInputMethodSession).setEnabled(paramBoolean);
    }
  }
  
  public abstract class AbstractInputMethodSessionImpl
    implements InputMethodSession
  {
    boolean mEnabled = true;
    boolean mRevoked;
    
    public AbstractInputMethodSessionImpl() {}
    
    public void dispatchGenericMotionEvent(int paramInt, MotionEvent paramMotionEvent, InputMethodSession.EventCallback paramEventCallback)
    {
      boolean bool = onGenericMotionEvent(paramMotionEvent);
      if (paramEventCallback != null) {
        paramEventCallback.finishedEvent(paramInt, bool);
      }
    }
    
    public void dispatchKeyEvent(int paramInt, KeyEvent paramKeyEvent, InputMethodSession.EventCallback paramEventCallback)
    {
      boolean bool = paramKeyEvent.dispatch(AbstractInputMethodService.this, mDispatcherState, this);
      if (paramEventCallback != null) {
        paramEventCallback.finishedEvent(paramInt, bool);
      }
    }
    
    public void dispatchTrackballEvent(int paramInt, MotionEvent paramMotionEvent, InputMethodSession.EventCallback paramEventCallback)
    {
      boolean bool = onTrackballEvent(paramMotionEvent);
      if (paramEventCallback != null) {
        paramEventCallback.finishedEvent(paramInt, bool);
      }
    }
    
    public boolean isEnabled()
    {
      return mEnabled;
    }
    
    public boolean isRevoked()
    {
      return mRevoked;
    }
    
    public void revokeSelf()
    {
      mRevoked = true;
      mEnabled = false;
    }
    
    public void setEnabled(boolean paramBoolean)
    {
      if (!mRevoked) {
        mEnabled = paramBoolean;
      }
    }
  }
}
