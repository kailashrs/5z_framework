package android.view.inputmethod;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class InputConnectionWrapper
  implements InputConnection
{
  private int mMissingMethodFlags;
  final boolean mMutable;
  private InputConnection mTarget;
  
  public InputConnectionWrapper(InputConnection paramInputConnection, boolean paramBoolean)
  {
    mMutable = paramBoolean;
    mTarget = paramInputConnection;
    mMissingMethodFlags = InputConnectionInspector.getMissingMethodFlags(paramInputConnection);
  }
  
  public boolean beginBatchEdit()
  {
    return mTarget.beginBatchEdit();
  }
  
  public boolean clearMetaKeyStates(int paramInt)
  {
    return mTarget.clearMetaKeyStates(paramInt);
  }
  
  public void closeConnection()
  {
    mTarget.closeConnection();
  }
  
  public boolean commitCompletion(CompletionInfo paramCompletionInfo)
  {
    return mTarget.commitCompletion(paramCompletionInfo);
  }
  
  public boolean commitContent(InputContentInfo paramInputContentInfo, int paramInt, Bundle paramBundle)
  {
    return mTarget.commitContent(paramInputContentInfo, paramInt, paramBundle);
  }
  
  public boolean commitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    return mTarget.commitCorrection(paramCorrectionInfo);
  }
  
  public boolean commitText(CharSequence paramCharSequence, int paramInt)
  {
    return mTarget.commitText(paramCharSequence, paramInt);
  }
  
  public boolean deleteSurroundingText(int paramInt1, int paramInt2)
  {
    return mTarget.deleteSurroundingText(paramInt1, paramInt2);
  }
  
  public boolean deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
  {
    return mTarget.deleteSurroundingTextInCodePoints(paramInt1, paramInt2);
  }
  
  public boolean endBatchEdit()
  {
    return mTarget.endBatchEdit();
  }
  
  public boolean finishComposingText()
  {
    return mTarget.finishComposingText();
  }
  
  public int getCursorCapsMode(int paramInt)
  {
    return mTarget.getCursorCapsMode(paramInt);
  }
  
  public ExtractedText getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt)
  {
    return mTarget.getExtractedText(paramExtractedTextRequest, paramInt);
  }
  
  public Handler getHandler()
  {
    return mTarget.getHandler();
  }
  
  public int getMissingMethodFlags()
  {
    return mMissingMethodFlags;
  }
  
  public CharSequence getSelectedText(int paramInt)
  {
    return mTarget.getSelectedText(paramInt);
  }
  
  public CharSequence getTextAfterCursor(int paramInt1, int paramInt2)
  {
    return mTarget.getTextAfterCursor(paramInt1, paramInt2);
  }
  
  public CharSequence getTextBeforeCursor(int paramInt1, int paramInt2)
  {
    return mTarget.getTextBeforeCursor(paramInt1, paramInt2);
  }
  
  public boolean performContextMenuAction(int paramInt)
  {
    return mTarget.performContextMenuAction(paramInt);
  }
  
  public boolean performEditorAction(int paramInt)
  {
    return mTarget.performEditorAction(paramInt);
  }
  
  public boolean performPrivateCommand(String paramString, Bundle paramBundle)
  {
    return mTarget.performPrivateCommand(paramString, paramBundle);
  }
  
  public boolean reportFullscreenMode(boolean paramBoolean)
  {
    return mTarget.reportFullscreenMode(paramBoolean);
  }
  
  public boolean requestCursorUpdates(int paramInt)
  {
    return mTarget.requestCursorUpdates(paramInt);
  }
  
  public boolean sendKeyEvent(KeyEvent paramKeyEvent)
  {
    return mTarget.sendKeyEvent(paramKeyEvent);
  }
  
  public boolean setComposingRegion(int paramInt1, int paramInt2)
  {
    return mTarget.setComposingRegion(paramInt1, paramInt2);
  }
  
  public boolean setComposingText(CharSequence paramCharSequence, int paramInt)
  {
    return mTarget.setComposingText(paramCharSequence, paramInt);
  }
  
  public boolean setSelection(int paramInt1, int paramInt2)
  {
    return mTarget.setSelection(paramInt1, paramInt2);
  }
  
  public void setTarget(InputConnection paramInputConnection)
  {
    if ((mTarget != null) && (!mMutable)) {
      throw new SecurityException("not mutable");
    }
    mTarget = paramInputConnection;
    mMissingMethodFlags = InputConnectionInspector.getMissingMethodFlags(paramInputConnection);
  }
}
