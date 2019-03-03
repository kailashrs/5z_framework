package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ExtractEditText
  extends EditText
{
  private InputMethodService mIME;
  private int mSettingExtractedText;
  
  public ExtractEditText(Context paramContext)
  {
    super(paramContext, null);
  }
  
  public ExtractEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 16842862);
  }
  
  public ExtractEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ExtractEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected void deleteText_internal(int paramInt1, int paramInt2)
  {
    mIME.onExtractedDeleteText(paramInt1, paramInt2);
  }
  
  public void finishInternalChanges()
  {
    mSettingExtractedText -= 1;
  }
  
  public boolean hasFocus()
  {
    return isEnabled();
  }
  
  public boolean hasVerticalScrollBar()
  {
    boolean bool;
    if (computeVerticalScrollRange() > computeVerticalScrollExtent()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasWindowFocus()
  {
    return isEnabled();
  }
  
  public boolean isFocused()
  {
    return isEnabled();
  }
  
  public boolean isInExtractedMode()
  {
    return true;
  }
  
  public boolean isInputMethodTarget()
  {
    return true;
  }
  
  protected void onSelectionChanged(int paramInt1, int paramInt2)
  {
    if ((mSettingExtractedText == 0) && (mIME != null) && (paramInt1 >= 0) && (paramInt2 >= 0)) {
      mIME.onExtractedSelectionChanged(paramInt1, paramInt2);
    }
  }
  
  public boolean onTextContextMenuItem(int paramInt)
  {
    if ((paramInt != 16908319) && (paramInt != 16908340))
    {
      if ((mIME != null) && (mIME.onExtractTextContextMenuItem(paramInt)))
      {
        if ((paramInt == 16908321) || (paramInt == 16908322)) {
          stopTextActionMode();
        }
        return true;
      }
      return super.onTextContextMenuItem(paramInt);
    }
    return super.onTextContextMenuItem(paramInt);
  }
  
  public boolean performClick()
  {
    if ((!super.performClick()) && (mIME != null))
    {
      mIME.onExtractedTextClicked();
      return true;
    }
    return false;
  }
  
  protected void replaceText_internal(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    mIME.onExtractedReplaceText(paramInt1, paramInt2, paramCharSequence);
  }
  
  protected void setCursorPosition_internal(int paramInt1, int paramInt2)
  {
    mIME.onExtractedSelectionChanged(paramInt1, paramInt2);
  }
  
  public void setExtractedText(ExtractedText paramExtractedText)
  {
    try
    {
      mSettingExtractedText += 1;
      super.setExtractedText(paramExtractedText);
      return;
    }
    finally
    {
      mSettingExtractedText -= 1;
    }
  }
  
  void setIME(InputMethodService paramInputMethodService)
  {
    mIME = paramInputMethodService;
  }
  
  protected void setSpan_internal(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    mIME.onExtractedSetSpan(paramObject, paramInt1, paramInt2, paramInt3);
  }
  
  public void startInternalChanges()
  {
    mSettingExtractedText += 1;
  }
  
  protected void viewClicked(InputMethodManager paramInputMethodManager)
  {
    if (mIME != null) {
      mIME.onViewClicked(false);
    }
  }
}
