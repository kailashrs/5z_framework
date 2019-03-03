package android.inputmethodservice;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputMethodSession;
import android.view.inputmethod.InputMethodSession.EventCallback;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputMethodSession.Stub;

class IInputMethodSessionWrapper
  extends IInputMethodSession.Stub
  implements HandlerCaller.Callback
{
  private static final int DO_APP_PRIVATE_COMMAND = 100;
  private static final int DO_DISPLAY_COMPLETIONS = 65;
  private static final int DO_FINISH_INPUT = 60;
  private static final int DO_FINISH_SESSION = 110;
  private static final int DO_TOGGLE_SOFT_INPUT = 105;
  private static final int DO_UPDATE_CURSOR = 95;
  private static final int DO_UPDATE_CURSOR_ANCHOR_INFO = 99;
  private static final int DO_UPDATE_EXTRACTED_TEXT = 67;
  private static final int DO_UPDATE_SELECTION = 90;
  private static final int DO_VIEW_CLICKED = 115;
  private static final String TAG = "InputMethodWrapper";
  HandlerCaller mCaller = new HandlerCaller(paramContext, null, this, true);
  InputChannel mChannel;
  InputMethodSession mInputMethodSession;
  ImeInputEventReceiver mReceiver;
  
  public IInputMethodSessionWrapper(Context paramContext, InputMethodSession paramInputMethodSession, InputChannel paramInputChannel)
  {
    mInputMethodSession = paramInputMethodSession;
    mChannel = paramInputChannel;
    if (paramInputChannel != null) {
      mReceiver = new ImeInputEventReceiver(paramInputChannel, paramContext.getMainLooper());
    }
  }
  
  private void doFinishSession()
  {
    mInputMethodSession = null;
    if (mReceiver != null)
    {
      mReceiver.dispose();
      mReceiver = null;
    }
    if (mChannel != null)
    {
      mChannel.dispose();
      mChannel = null;
    }
  }
  
  public void appPrivateCommand(String paramString, Bundle paramBundle)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageOO(100, paramString, paramBundle));
  }
  
  public void displayCompletions(CompletionInfo[] paramArrayOfCompletionInfo)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(65, paramArrayOfCompletionInfo));
  }
  
  public void executeMessage(Message paramMessage)
  {
    if (mInputMethodSession == null)
    {
      i = what;
      if ((i == 90) || (i == 100)) {
        ((SomeArgs)obj).recycle();
      }
      return;
    }
    int i = what;
    if (i != 60)
    {
      if (i != 65)
      {
        if (i != 67)
        {
          if (i != 90)
          {
            if (i != 95)
            {
              if (i != 105)
              {
                if (i != 110)
                {
                  if (i != 115)
                  {
                    switch (i)
                    {
                    default: 
                      localObject = new StringBuilder();
                      ((StringBuilder)localObject).append("Unhandled message code: ");
                      ((StringBuilder)localObject).append(what);
                      Log.w("InputMethodWrapper", ((StringBuilder)localObject).toString());
                      return;
                    case 100: 
                      paramMessage = (SomeArgs)obj;
                      mInputMethodSession.appPrivateCommand((String)arg1, (Bundle)arg2);
                      paramMessage.recycle();
                      return;
                    }
                    mInputMethodSession.updateCursorAnchorInfo((CursorAnchorInfo)obj);
                    return;
                  }
                  Object localObject = mInputMethodSession;
                  i = arg1;
                  boolean bool = true;
                  if (i != 1) {
                    bool = false;
                  }
                  ((InputMethodSession)localObject).viewClicked(bool);
                  return;
                }
                doFinishSession();
                return;
              }
              mInputMethodSession.toggleSoftInput(arg1, arg2);
              return;
            }
            mInputMethodSession.updateCursor((Rect)obj);
            return;
          }
          paramMessage = (SomeArgs)obj;
          mInputMethodSession.updateSelection(argi1, argi2, argi3, argi4, argi5, argi6);
          paramMessage.recycle();
          return;
        }
        mInputMethodSession.updateExtractedText(arg1, (ExtractedText)obj);
        return;
      }
      mInputMethodSession.displayCompletions((CompletionInfo[])obj);
      return;
    }
    mInputMethodSession.finishInput();
  }
  
  public void finishInput()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(60));
  }
  
  public void finishSession()
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessage(110));
  }
  
  public InputMethodSession getInternalInputMethodSession()
  {
    return mInputMethodSession;
  }
  
  public void toggleSoftInput(int paramInt1, int paramInt2)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageII(105, paramInt1, paramInt2));
  }
  
  public void updateCursor(Rect paramRect)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(95, paramRect));
  }
  
  public void updateCursorAnchorInfo(CursorAnchorInfo paramCursorAnchorInfo)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageO(99, paramCursorAnchorInfo));
  }
  
  public void updateExtractedText(int paramInt, ExtractedText paramExtractedText)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageIO(67, paramInt, paramExtractedText));
  }
  
  public void updateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageIIIIII(90, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
  }
  
  public void viewClicked(boolean paramBoolean)
  {
    mCaller.executeOrSendMessage(mCaller.obtainMessageI(115, paramBoolean));
  }
  
  private final class ImeInputEventReceiver
    extends InputEventReceiver
    implements InputMethodSession.EventCallback
  {
    private final SparseArray<InputEvent> mPendingEvents = new SparseArray();
    
    public ImeInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper)
    {
      super(paramLooper);
    }
    
    public void finishedEvent(int paramInt, boolean paramBoolean)
    {
      paramInt = mPendingEvents.indexOfKey(paramInt);
      if (paramInt >= 0)
      {
        InputEvent localInputEvent = (InputEvent)mPendingEvents.valueAt(paramInt);
        mPendingEvents.removeAt(paramInt);
        finishInputEvent(localInputEvent, paramBoolean);
      }
    }
    
    public void onInputEvent(InputEvent paramInputEvent, int paramInt)
    {
      if (mInputMethodSession == null)
      {
        finishInputEvent(paramInputEvent, false);
        return;
      }
      paramInt = paramInputEvent.getSequenceNumber();
      mPendingEvents.put(paramInt, paramInputEvent);
      if ((paramInputEvent instanceof KeyEvent))
      {
        paramInputEvent = (KeyEvent)paramInputEvent;
        mInputMethodSession.dispatchKeyEvent(paramInt, paramInputEvent, this);
      }
      else
      {
        paramInputEvent = (MotionEvent)paramInputEvent;
        if (paramInputEvent.isFromSource(4)) {
          mInputMethodSession.dispatchTrackballEvent(paramInt, paramInputEvent, this);
        } else {
          mInputMethodSession.dispatchGenericMotionEvent(paramInt, paramInputEvent, this);
        }
      }
    }
  }
}
