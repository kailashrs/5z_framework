package com.android.internal.view;

import android.inputmethodservice.AbstractInputMethodService;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionInspector;
import android.view.inputmethod.InputContentInfo;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputConnectionWrapper
  implements InputConnection
{
  private static final int MAX_WAIT_TIME_MILLIS = 2000;
  private final IInputContext mIInputContext;
  private final WeakReference<AbstractInputMethodService> mInputMethodService;
  private final AtomicBoolean mIsUnbindIssued;
  private final int mMissingMethods;
  
  public InputConnectionWrapper(WeakReference<AbstractInputMethodService> paramWeakReference, IInputContext paramIInputContext, int paramInt, AtomicBoolean paramAtomicBoolean)
  {
    mInputMethodService = paramWeakReference;
    mIInputContext = paramIInputContext;
    mMissingMethods = paramInt;
    mIsUnbindIssued = paramAtomicBoolean;
  }
  
  private boolean isMethodMissing(int paramInt)
  {
    boolean bool;
    if ((mMissingMethods & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean beginBatchEdit()
  {
    try
    {
      mIInputContext.beginBatchEdit();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean clearMetaKeyStates(int paramInt)
  {
    try
    {
      mIInputContext.clearMetaKeyStates(paramInt);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void closeConnection() {}
  
  public boolean commitCompletion(CompletionInfo paramCompletionInfo)
  {
    if (isMethodMissing(4)) {
      return false;
    }
    try
    {
      mIInputContext.commitCompletion(paramCompletionInfo);
      return true;
    }
    catch (RemoteException paramCompletionInfo) {}
    return false;
  }
  
  public boolean commitContent(InputContentInfo paramInputContentInfo, int paramInt, Bundle paramBundle)
  {
    if (mIsUnbindIssued.get()) {
      return false;
    }
    boolean bool = false;
    if (isMethodMissing(128)) {
      return false;
    }
    if ((paramInt & 0x1) != 0) {
      try
      {
        localObject = (AbstractInputMethodService)mInputMethodService.get();
        if (localObject == null) {
          return false;
        }
        ((AbstractInputMethodService)localObject).exposeContent(paramInputContentInfo, this);
      }
      catch (RemoteException paramInputContentInfo)
      {
        break label130;
      }
    }
    Object localObject = InputContextCallback.access$000();
    mIInputContext.commitContent(paramInputContentInfo, paramInt, paramBundle, mSeq, (IInputContextCallback)localObject);
    try
    {
      ((InputContextCallback)localObject).waitForResultLocked();
      if (mHaveValue) {
        bool = mCommitContentResult;
      }
      ((InputContextCallback)localObject).dispose();
      return bool;
    }
    finally {}
    label130:
    return false;
  }
  
  public boolean commitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    try
    {
      mIInputContext.commitCorrection(paramCorrectionInfo);
      return true;
    }
    catch (RemoteException paramCorrectionInfo) {}
    return false;
  }
  
  public boolean commitText(CharSequence paramCharSequence, int paramInt)
  {
    try
    {
      mIInputContext.commitText(paramCharSequence, paramInt);
      return true;
    }
    catch (RemoteException paramCharSequence) {}
    return false;
  }
  
  public boolean deleteSurroundingText(int paramInt1, int paramInt2)
  {
    try
    {
      mIInputContext.deleteSurroundingText(paramInt1, paramInt2);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
  {
    if (isMethodMissing(16)) {
      return false;
    }
    try
    {
      mIInputContext.deleteSurroundingTextInCodePoints(paramInt1, paramInt2);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean endBatchEdit()
  {
    try
    {
      mIInputContext.endBatchEdit();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean finishComposingText()
  {
    try
    {
      mIInputContext.finishComposingText();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public int getCursorCapsMode(int paramInt)
  {
    if (mIsUnbindIssued.get()) {
      return 0;
    }
    int i = 0;
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.getCursorCapsMode(paramInt, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        paramInt = i;
        if (mHaveValue) {
          paramInt = mCursorCapsMode;
        }
        localInputContextCallback.dispose();
        return paramInt;
      }
      finally {}
      return 0;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public ExtractedText getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt)
  {
    if (mIsUnbindIssued.get()) {
      return null;
    }
    Object localObject = null;
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.getExtractedText(paramExtractedTextRequest, paramInt, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        paramExtractedTextRequest = localObject;
        if (mHaveValue) {
          paramExtractedTextRequest = mExtractedText;
        }
        localInputContextCallback.dispose();
        return paramExtractedTextRequest;
      }
      finally {}
      return null;
    }
    catch (RemoteException paramExtractedTextRequest) {}
  }
  
  public Handler getHandler()
  {
    return null;
  }
  
  public CharSequence getSelectedText(int paramInt)
  {
    if (mIsUnbindIssued.get()) {
      return null;
    }
    if (isMethodMissing(1)) {
      return null;
    }
    CharSequence localCharSequence = null;
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.getSelectedText(paramInt, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        if (mHaveValue) {
          localCharSequence = mSelectedText;
        }
        localInputContextCallback.dispose();
        return localCharSequence;
      }
      finally {}
      return null;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public CharSequence getTextAfterCursor(int paramInt1, int paramInt2)
  {
    if (mIsUnbindIssued.get()) {
      return null;
    }
    CharSequence localCharSequence = null;
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.getTextAfterCursor(paramInt1, paramInt2, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        if (mHaveValue) {
          localCharSequence = mTextAfterCursor;
        }
        localInputContextCallback.dispose();
        return localCharSequence;
      }
      finally {}
      return null;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public CharSequence getTextBeforeCursor(int paramInt1, int paramInt2)
  {
    if (mIsUnbindIssued.get()) {
      return null;
    }
    CharSequence localCharSequence = null;
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.getTextBeforeCursor(paramInt1, paramInt2, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        if (mHaveValue) {
          localCharSequence = mTextBeforeCursor;
        }
        localInputContextCallback.dispose();
        return localCharSequence;
      }
      finally {}
      return null;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public boolean performContextMenuAction(int paramInt)
  {
    try
    {
      mIInputContext.performContextMenuAction(paramInt);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean performEditorAction(int paramInt)
  {
    try
    {
      mIInputContext.performEditorAction(paramInt);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean performPrivateCommand(String paramString, Bundle paramBundle)
  {
    try
    {
      mIInputContext.performPrivateCommand(paramString, paramBundle);
      return true;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public boolean reportFullscreenMode(boolean paramBoolean)
  {
    return false;
  }
  
  public boolean requestCursorUpdates(int paramInt)
  {
    if (mIsUnbindIssued.get()) {
      return false;
    }
    boolean bool = false;
    if (isMethodMissing(8)) {
      return false;
    }
    try
    {
      InputContextCallback localInputContextCallback = InputContextCallback.access$000();
      mIInputContext.requestUpdateCursorAnchorInfo(paramInt, mSeq, localInputContextCallback);
      try
      {
        localInputContextCallback.waitForResultLocked();
        if (mHaveValue) {
          bool = mRequestUpdateCursorAnchorInfoResult;
        }
        localInputContextCallback.dispose();
        return bool;
      }
      finally {}
      return false;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public boolean sendKeyEvent(KeyEvent paramKeyEvent)
  {
    try
    {
      mIInputContext.sendKeyEvent(paramKeyEvent);
      return true;
    }
    catch (RemoteException paramKeyEvent) {}
    return false;
  }
  
  public boolean setComposingRegion(int paramInt1, int paramInt2)
  {
    if (isMethodMissing(2)) {
      return false;
    }
    try
    {
      mIInputContext.setComposingRegion(paramInt1, paramInt2);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean setComposingText(CharSequence paramCharSequence, int paramInt)
  {
    try
    {
      mIInputContext.setComposingText(paramCharSequence, paramInt);
      return true;
    }
    catch (RemoteException paramCharSequence) {}
    return false;
  }
  
  public boolean setSelection(int paramInt1, int paramInt2)
  {
    try
    {
      mIInputContext.setSelection(paramInt1, paramInt2);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("InputConnectionWrapper{idHash=#");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" mMissingMethods=");
    localStringBuilder.append(InputConnectionInspector.getMissingMethodFlagsAsString(mMissingMethods));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  static class InputContextCallback
    extends IInputContextCallback.Stub
  {
    private static final String TAG = "InputConnectionWrapper.ICC";
    private static InputContextCallback sInstance = new InputContextCallback();
    private static int sSequenceNumber = 1;
    public boolean mCommitContentResult;
    public int mCursorCapsMode;
    public ExtractedText mExtractedText;
    public boolean mHaveValue;
    public boolean mRequestUpdateCursorAnchorInfoResult;
    public CharSequence mSelectedText;
    public int mSeq;
    public CharSequence mTextAfterCursor;
    public CharSequence mTextBeforeCursor;
    
    InputContextCallback() {}
    
    private void dispose()
    {
      try
      {
        if (sInstance == null)
        {
          mTextAfterCursor = null;
          mTextBeforeCursor = null;
          mExtractedText = null;
          sInstance = this;
        }
        return;
      }
      finally {}
    }
    
    private static InputContextCallback getInstance()
    {
      try
      {
        InputContextCallback localInputContextCallback;
        if (sInstance != null)
        {
          localInputContextCallback = sInstance;
          sInstance = null;
          mHaveValue = false;
        }
        else
        {
          localInputContextCallback = new InputContextCallback();
        }
        int i = sSequenceNumber;
        sSequenceNumber = i + 1;
        mSeq = i;
        return localInputContextCallback;
      }
      finally {}
    }
    
    public void setCommitContentResult(boolean paramBoolean, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mCommitContentResult = paramBoolean;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Got out-of-sequence callback ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" (expected ");
          localStringBuilder.append(mSeq);
          localStringBuilder.append(") in setCommitContentResult, ignoring.");
          Log.i("InputConnectionWrapper.ICC", localStringBuilder.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setCursorCapsMode(int paramInt1, int paramInt2)
    {
      try
      {
        if (paramInt2 == mSeq)
        {
          mCursorCapsMode = paramInt1;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Got out-of-sequence callback ");
          localStringBuilder.append(paramInt2);
          localStringBuilder.append(" (expected ");
          localStringBuilder.append(mSeq);
          localStringBuilder.append(") in setCursorCapsMode, ignoring.");
          Log.i("InputConnectionWrapper.ICC", localStringBuilder.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setExtractedText(ExtractedText paramExtractedText, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mExtractedText = paramExtractedText;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          paramExtractedText = new java/lang/StringBuilder;
          paramExtractedText.<init>();
          paramExtractedText.append("Got out-of-sequence callback ");
          paramExtractedText.append(paramInt);
          paramExtractedText.append(" (expected ");
          paramExtractedText.append(mSeq);
          paramExtractedText.append(") in setExtractedText, ignoring.");
          Log.i("InputConnectionWrapper.ICC", paramExtractedText.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setRequestUpdateCursorAnchorInfoResult(boolean paramBoolean, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mRequestUpdateCursorAnchorInfoResult = paramBoolean;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Got out-of-sequence callback ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" (expected ");
          localStringBuilder.append(mSeq);
          localStringBuilder.append(") in setCursorAnchorInfoRequestResult, ignoring.");
          Log.i("InputConnectionWrapper.ICC", localStringBuilder.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setSelectedText(CharSequence paramCharSequence, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mSelectedText = paramCharSequence;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          paramCharSequence = new java/lang/StringBuilder;
          paramCharSequence.<init>();
          paramCharSequence.append("Got out-of-sequence callback ");
          paramCharSequence.append(paramInt);
          paramCharSequence.append(" (expected ");
          paramCharSequence.append(mSeq);
          paramCharSequence.append(") in setSelectedText, ignoring.");
          Log.i("InputConnectionWrapper.ICC", paramCharSequence.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setTextAfterCursor(CharSequence paramCharSequence, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mTextAfterCursor = paramCharSequence;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          paramCharSequence = new java/lang/StringBuilder;
          paramCharSequence.<init>();
          paramCharSequence.append("Got out-of-sequence callback ");
          paramCharSequence.append(paramInt);
          paramCharSequence.append(" (expected ");
          paramCharSequence.append(mSeq);
          paramCharSequence.append(") in setTextAfterCursor, ignoring.");
          Log.i("InputConnectionWrapper.ICC", paramCharSequence.toString());
        }
        return;
      }
      finally {}
    }
    
    public void setTextBeforeCursor(CharSequence paramCharSequence, int paramInt)
    {
      try
      {
        if (paramInt == mSeq)
        {
          mTextBeforeCursor = paramCharSequence;
          mHaveValue = true;
          notifyAll();
        }
        else
        {
          paramCharSequence = new java/lang/StringBuilder;
          paramCharSequence.<init>();
          paramCharSequence.append("Got out-of-sequence callback ");
          paramCharSequence.append(paramInt);
          paramCharSequence.append(" (expected ");
          paramCharSequence.append(mSeq);
          paramCharSequence.append(") in setTextBeforeCursor, ignoring.");
          Log.i("InputConnectionWrapper.ICC", paramCharSequence.toString());
        }
        return;
      }
      finally {}
    }
    
    void waitForResultLocked()
    {
      long l1 = SystemClock.uptimeMillis();
      while (!mHaveValue)
      {
        long l2 = 2000L + l1 - SystemClock.uptimeMillis();
        if (l2 <= 0L)
        {
          Log.w("InputConnectionWrapper.ICC", "Timed out waiting on IInputContextCallback");
          return;
        }
        try
        {
          wait(l2);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
  }
}
