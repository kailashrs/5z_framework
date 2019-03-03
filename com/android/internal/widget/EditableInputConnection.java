package com.android.internal.widget;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.text.style.SuggestionSpan;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class EditableInputConnection
  extends BaseInputConnection
{
  private static final boolean DEBUG = false;
  private static final String TAG = "EditableInputConnection";
  private int mBatchEditNesting;
  private final TextView mTextView;
  
  public EditableInputConnection(TextView paramTextView)
  {
    super(paramTextView, true);
    mTextView = paramTextView;
  }
  
  public boolean beginBatchEdit()
  {
    try
    {
      if (mBatchEditNesting >= 0)
      {
        mTextView.beginBatchEdit();
        mBatchEditNesting += 1;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public boolean clearMetaKeyStates(int paramInt)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return false;
    }
    KeyListener localKeyListener = mTextView.getKeyListener();
    if (localKeyListener != null) {
      try
      {
        localKeyListener.clearMetaKeyState(mTextView, localEditable, paramInt);
      }
      catch (AbstractMethodError localAbstractMethodError) {}
    }
    return true;
  }
  
  public void closeConnection()
  {
    super.closeConnection();
    try
    {
      while (mBatchEditNesting > 0) {
        endBatchEdit();
      }
      mBatchEditNesting = -1;
      return;
    }
    finally {}
  }
  
  public boolean commitCompletion(CompletionInfo paramCompletionInfo)
  {
    mTextView.beginBatchEdit();
    mTextView.onCommitCompletion(paramCompletionInfo);
    mTextView.endBatchEdit();
    return true;
  }
  
  public boolean commitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    mTextView.beginBatchEdit();
    mTextView.onCommitCorrection(paramCorrectionInfo);
    mTextView.endBatchEdit();
    return true;
  }
  
  public boolean commitText(CharSequence paramCharSequence, int paramInt)
  {
    if (mTextView == null) {
      return super.commitText(paramCharSequence, paramInt);
    }
    if ((paramCharSequence instanceof Spanned))
    {
      SuggestionSpan[] arrayOfSuggestionSpan = (SuggestionSpan[])((Spanned)paramCharSequence).getSpans(0, paramCharSequence.length(), SuggestionSpan.class);
      mIMM.registerSuggestionSpansForNotification(arrayOfSuggestionSpan);
    }
    mTextView.resetErrorChangedFlag();
    boolean bool = super.commitText(paramCharSequence, paramInt);
    mTextView.hideErrorIfUnchanged();
    return bool;
  }
  
  public boolean endBatchEdit()
  {
    try
    {
      if (mBatchEditNesting > 0)
      {
        mTextView.endBatchEdit();
        mBatchEditNesting -= 1;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public Editable getEditable()
  {
    TextView localTextView = mTextView;
    if (localTextView != null) {
      return localTextView.getEditableText();
    }
    return null;
  }
  
  public ExtractedText getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt)
  {
    if (mTextView != null)
    {
      ExtractedText localExtractedText = new ExtractedText();
      if (mTextView.extractText(paramExtractedTextRequest, localExtractedText))
      {
        if ((paramInt & 0x1) != 0) {
          mTextView.setExtracting(paramExtractedTextRequest);
        }
        return localExtractedText;
      }
    }
    return null;
  }
  
  public boolean performContextMenuAction(int paramInt)
  {
    mTextView.beginBatchEdit();
    mTextView.onTextContextMenuItem(paramInt);
    mTextView.endBatchEdit();
    return true;
  }
  
  public boolean performEditorAction(int paramInt)
  {
    mTextView.onEditorAction(paramInt);
    return true;
  }
  
  public boolean performPrivateCommand(String paramString, Bundle paramBundle)
  {
    mTextView.onPrivateIMECommand(paramString, paramBundle);
    return true;
  }
  
  public boolean requestCursorUpdates(int paramInt)
  {
    if ((paramInt & 0xFFFFFFFC) != 0) {
      return false;
    }
    if (mIMM == null) {
      return false;
    }
    mIMM.setUpdateCursorAnchorInfoMode(paramInt);
    if (((paramInt & 0x1) != 0) && (mTextView != null) && (!mTextView.isInLayout())) {
      mTextView.requestLayout();
    }
    return true;
  }
}
