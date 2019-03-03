package android.view.inputmethod;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Editable.Factory;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;

public class BaseInputConnection
  implements InputConnection
{
  static final Object COMPOSING = new ComposingText();
  private static final boolean DEBUG = false;
  private static int INVALID_INDEX = -1;
  private static final String TAG = "BaseInputConnection";
  private Object[] mDefaultComposingSpans;
  final boolean mDummyMode;
  Editable mEditable;
  protected final InputMethodManager mIMM;
  KeyCharacterMap mKeyCharacterMap;
  final View mTargetView;
  
  public BaseInputConnection(View paramView, boolean paramBoolean)
  {
    mIMM = ((InputMethodManager)paramView.getContext().getSystemService("input_method"));
    mTargetView = paramView;
    mDummyMode = (paramBoolean ^ true);
  }
  
  BaseInputConnection(InputMethodManager paramInputMethodManager, boolean paramBoolean)
  {
    mIMM = paramInputMethodManager;
    mTargetView = null;
    mDummyMode = (paramBoolean ^ true);
  }
  
  private void ensureDefaultComposingSpans()
  {
    if (mDefaultComposingSpans == null)
    {
      Object localObject;
      if (mTargetView != null) {
        localObject = mTargetView.getContext();
      }
      for (;;)
      {
        break;
        if (mIMM.mServedView != null) {
          localObject = mIMM.mServedView.getContext();
        } else {
          localObject = null;
        }
      }
      if (localObject != null)
      {
        localObject = ((Context)localObject).getTheme().obtainStyledAttributes(new int[] { 16843312 });
        CharSequence localCharSequence = ((TypedArray)localObject).getText(0);
        ((TypedArray)localObject).recycle();
        if ((localCharSequence != null) && ((localCharSequence instanceof Spanned))) {
          mDefaultComposingSpans = ((Spanned)localCharSequence).getSpans(0, localCharSequence.length(), Object.class);
        }
      }
    }
  }
  
  private static int findIndexBackward(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = paramCharSequence.length();
    if ((paramInt1 >= 0) && (i >= paramInt1))
    {
      if (paramInt2 < 0) {
        return INVALID_INDEX;
      }
      int j = 0;
      i = paramInt1;
      paramInt1 = j;
      for (;;)
      {
        if (paramInt2 == 0) {
          return i;
        }
        i--;
        if (i < 0)
        {
          if (paramInt1 != 0) {
            return INVALID_INDEX;
          }
          return 0;
        }
        char c = paramCharSequence.charAt(i);
        if (paramInt1 != 0)
        {
          if (!Character.isHighSurrogate(c)) {
            return INVALID_INDEX;
          }
          paramInt1 = 0;
          paramInt2--;
        }
        else if (!Character.isSurrogate(c))
        {
          paramInt2--;
        }
        else
        {
          if (Character.isHighSurrogate(c)) {
            return INVALID_INDEX;
          }
          paramInt1 = 1;
        }
      }
    }
    return INVALID_INDEX;
  }
  
  private static int findIndexForward(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = paramCharSequence.length();
    if ((paramInt1 >= 0) && (i >= paramInt1))
    {
      if (paramInt2 < 0) {
        return INVALID_INDEX;
      }
      int j = 0;
      for (;;)
      {
        if (paramInt2 == 0) {
          return paramInt1;
        }
        if (paramInt1 >= i)
        {
          if (j != 0) {
            return INVALID_INDEX;
          }
          return i;
        }
        char c = paramCharSequence.charAt(paramInt1);
        if (j != 0)
        {
          if (!Character.isLowSurrogate(c)) {
            return INVALID_INDEX;
          }
          paramInt2--;
          j = 0;
          paramInt1++;
        }
        else if (!Character.isSurrogate(c))
        {
          paramInt2--;
          paramInt1++;
        }
        else
        {
          if (Character.isLowSurrogate(c)) {
            return INVALID_INDEX;
          }
          j = 1;
          paramInt1++;
        }
      }
    }
    return INVALID_INDEX;
  }
  
  public static int getComposingSpanEnd(Spannable paramSpannable)
  {
    return paramSpannable.getSpanEnd(COMPOSING);
  }
  
  public static int getComposingSpanStart(Spannable paramSpannable)
  {
    return paramSpannable.getSpanStart(COMPOSING);
  }
  
  public static final void removeComposingSpans(Spannable paramSpannable)
  {
    paramSpannable.removeSpan(COMPOSING);
    Object[] arrayOfObject = paramSpannable.getSpans(0, paramSpannable.length(), Object.class);
    if (arrayOfObject != null) {
      for (int i = arrayOfObject.length - 1; i >= 0; i--)
      {
        Object localObject = arrayOfObject[i];
        if ((paramSpannable.getSpanFlags(localObject) & 0x100) != 0) {
          paramSpannable.removeSpan(localObject);
        }
      }
    }
  }
  
  private void replaceText(CharSequence paramCharSequence, int paramInt, boolean paramBoolean)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return;
    }
    beginBatchEdit();
    int i = getComposingSpanStart(localEditable);
    int j = getComposingSpanEnd(localEditable);
    int k = i;
    int m = j;
    if (j < i)
    {
      k = j;
      m = i;
    }
    if ((k != -1) && (m != -1))
    {
      removeComposingSpans(localEditable);
    }
    else
    {
      j = Selection.getSelectionStart(localEditable);
      k = Selection.getSelectionEnd(localEditable);
      i = j;
      if (j < 0) {
        i = 0;
      }
      j = k;
      if (k < 0) {
        j = 0;
      }
      k = i;
      m = j;
      if (j < i)
      {
        m = i;
        k = j;
      }
    }
    Object localObject1 = paramCharSequence;
    if (paramBoolean)
    {
      Object localObject2;
      if (!(paramCharSequence instanceof Spannable))
      {
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
        localObject1 = localSpannableStringBuilder;
        ensureDefaultComposingSpans();
        localObject2 = localSpannableStringBuilder;
        paramCharSequence = (CharSequence)localObject1;
        if (mDefaultComposingSpans != null) {
          for (i = 0;; i++)
          {
            localObject2 = localSpannableStringBuilder;
            paramCharSequence = (CharSequence)localObject1;
            if (i >= mDefaultComposingSpans.length) {
              break;
            }
            localSpannableStringBuilder.setSpan(mDefaultComposingSpans[i], 0, localSpannableStringBuilder.length(), 289);
          }
        }
      }
      else
      {
        localObject2 = (Spannable)paramCharSequence;
      }
      setComposingSpans((Spannable)localObject2);
      localObject1 = paramCharSequence;
    }
    if (paramInt > 0) {
      i = paramInt + (m - 1);
    } else {
      i = paramInt + k;
    }
    paramInt = i;
    if (i < 0) {
      paramInt = 0;
    }
    i = paramInt;
    if (paramInt > localEditable.length()) {
      i = localEditable.length();
    }
    Selection.setSelection(localEditable, i);
    localEditable.replace(k, m, (CharSequence)localObject1);
    endBatchEdit();
  }
  
  private void sendCurrentText()
  {
    if (!mDummyMode) {
      return;
    }
    Editable localEditable = getEditable();
    if (localEditable != null)
    {
      int i = localEditable.length();
      if (i == 0) {
        return;
      }
      if (i == 1)
      {
        if (mKeyCharacterMap == null) {
          mKeyCharacterMap = KeyCharacterMap.load(-1);
        }
        Object localObject = new char[1];
        i = 0;
        localEditable.getChars(0, 1, (char[])localObject, 0);
        localObject = mKeyCharacterMap.getEvents((char[])localObject);
        if (localObject != null)
        {
          while (i < localObject.length)
          {
            sendKeyEvent(localObject[i]);
            i++;
          }
          localEditable.clear();
          return;
        }
      }
      sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), localEditable.toString(), -1, 0));
      localEditable.clear();
    }
  }
  
  public static void setComposingSpans(Spannable paramSpannable)
  {
    setComposingSpans(paramSpannable, 0, paramSpannable.length());
  }
  
  public static void setComposingSpans(Spannable paramSpannable, int paramInt1, int paramInt2)
  {
    Object[] arrayOfObject = paramSpannable.getSpans(paramInt1, paramInt2, Object.class);
    if (arrayOfObject != null) {
      for (int i = arrayOfObject.length - 1; i >= 0; i--)
      {
        Object localObject = arrayOfObject[i];
        if (localObject == COMPOSING)
        {
          paramSpannable.removeSpan(localObject);
        }
        else
        {
          int j = paramSpannable.getSpanFlags(localObject);
          if ((j & 0x133) != 289) {
            paramSpannable.setSpan(localObject, paramSpannable.getSpanStart(localObject), paramSpannable.getSpanEnd(localObject), j & 0xFFFFFFCC | 0x100 | 0x21);
          }
        }
      }
    }
    paramSpannable.setSpan(COMPOSING, paramInt1, paramInt2, 289);
  }
  
  public boolean beginBatchEdit()
  {
    return false;
  }
  
  public boolean clearMetaKeyStates(int paramInt)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return false;
    }
    MetaKeyKeyListener.clearMetaKeyState(localEditable, paramInt);
    return true;
  }
  
  public void closeConnection()
  {
    finishComposingText();
  }
  
  public boolean commitCompletion(CompletionInfo paramCompletionInfo)
  {
    return false;
  }
  
  public boolean commitContent(InputContentInfo paramInputContentInfo, int paramInt, Bundle paramBundle)
  {
    return false;
  }
  
  public boolean commitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    return false;
  }
  
  public boolean commitText(CharSequence paramCharSequence, int paramInt)
  {
    replaceText(paramCharSequence, paramInt, false);
    sendCurrentText();
    return true;
  }
  
  public boolean deleteSurroundingText(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return false;
    }
    beginBatchEdit();
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = i;
    int m = j;
    if (i > j)
    {
      k = j;
      m = i;
    }
    int n = getComposingSpanStart(localEditable);
    int i1 = getComposingSpanEnd(localEditable);
    j = n;
    i = i1;
    if (i1 < n)
    {
      j = i1;
      i = n;
    }
    int i2 = k;
    i1 = m;
    if (j != -1)
    {
      i2 = k;
      i1 = m;
      if (i != -1)
      {
        n = k;
        if (j < k) {
          n = j;
        }
        i2 = n;
        i1 = m;
        if (i > m)
        {
          i1 = i;
          i2 = n;
        }
      }
    }
    k = 0;
    if (paramInt1 > 0)
    {
      k = i2 - paramInt1;
      paramInt1 = k;
      if (k < 0) {
        paramInt1 = 0;
      }
      localEditable.delete(paramInt1, i2);
      k = i2 - paramInt1;
    }
    if (paramInt2 > 0)
    {
      k = i1 - k;
      paramInt2 = k + paramInt2;
      paramInt1 = paramInt2;
      if (paramInt2 > localEditable.length()) {
        paramInt1 = localEditable.length();
      }
      localEditable.delete(k, paramInt1);
    }
    endBatchEdit();
    return true;
  }
  
  public boolean deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return false;
    }
    beginBatchEdit();
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = i;
    int m = j;
    if (i > j)
    {
      k = j;
      m = i;
    }
    int n = getComposingSpanStart(localEditable);
    int i1 = getComposingSpanEnd(localEditable);
    j = n;
    i = i1;
    if (i1 < n)
    {
      j = i1;
      i = n;
    }
    i1 = k;
    int i2 = m;
    if (j != -1)
    {
      i1 = k;
      i2 = m;
      if (i != -1)
      {
        n = k;
        if (j < k) {
          n = j;
        }
        i1 = n;
        i2 = m;
        if (i > m)
        {
          i2 = i;
          i1 = n;
        }
      }
    }
    if ((i1 >= 0) && (i2 >= 0))
    {
      paramInt1 = findIndexBackward(localEditable, i1, Math.max(paramInt1, 0));
      if (paramInt1 != INVALID_INDEX)
      {
        paramInt2 = findIndexForward(localEditable, i2, Math.max(paramInt2, 0));
        if (paramInt2 != INVALID_INDEX)
        {
          k = i1 - paramInt1;
          if (k > 0) {
            localEditable.delete(paramInt1, i1);
          }
          if (paramInt2 - i2 > 0) {
            localEditable.delete(i2 - k, paramInt2 - k);
          }
        }
      }
    }
    endBatchEdit();
    return true;
  }
  
  public boolean endBatchEdit()
  {
    return false;
  }
  
  public boolean finishComposingText()
  {
    Editable localEditable = getEditable();
    if (localEditable != null)
    {
      beginBatchEdit();
      removeComposingSpans(localEditable);
      sendCurrentText();
      endBatchEdit();
    }
    return true;
  }
  
  public int getCursorCapsMode(int paramInt)
  {
    if (mDummyMode) {
      return 0;
    }
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return 0;
    }
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = i;
    if (i > j) {
      k = j;
    }
    return TextUtils.getCapsMode(localEditable, k, paramInt);
  }
  
  public Editable getEditable()
  {
    if (mEditable == null)
    {
      mEditable = Editable.Factory.getInstance().newEditable("");
      Selection.setSelection(mEditable, 0);
    }
    return mEditable;
  }
  
  public ExtractedText getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt)
  {
    return null;
  }
  
  public Handler getHandler()
  {
    return null;
  }
  
  public CharSequence getSelectedText(int paramInt)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return null;
    }
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = i;
    int m = j;
    if (i > j)
    {
      k = j;
      m = i;
    }
    if ((k != m) && (k >= 0))
    {
      if ((paramInt & 0x1) != 0) {
        return localEditable.subSequence(k, m);
      }
      return TextUtils.substring(localEditable, k, m);
    }
    return null;
  }
  
  public CharSequence getTextAfterCursor(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return null;
    }
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = j;
    if (i > j) {
      k = i;
    }
    j = k;
    if (k < 0) {
      j = 0;
    }
    k = paramInt1;
    if (j + paramInt1 > localEditable.length()) {
      k = localEditable.length() - j;
    }
    if ((paramInt2 & 0x1) != 0) {
      return localEditable.subSequence(j, j + k);
    }
    return TextUtils.substring(localEditable, j, j + k);
  }
  
  public CharSequence getTextBeforeCursor(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return null;
    }
    int i = Selection.getSelectionStart(localEditable);
    int j = Selection.getSelectionEnd(localEditable);
    int k = i;
    if (i > j) {
      k = j;
    }
    if (k <= 0) {
      return "";
    }
    i = paramInt1;
    if (paramInt1 > k) {
      i = k;
    }
    if ((paramInt2 & 0x1) != 0) {
      return localEditable.subSequence(k - i, k);
    }
    return TextUtils.substring(localEditable, k - i, k);
  }
  
  public boolean performContextMenuAction(int paramInt)
  {
    return false;
  }
  
  public boolean performEditorAction(int paramInt)
  {
    long l = SystemClock.uptimeMillis();
    sendKeyEvent(new KeyEvent(l, l, 0, 66, 0, 0, -1, 0, 22));
    sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), l, 1, 66, 0, 0, -1, 0, 22));
    return true;
  }
  
  public boolean performPrivateCommand(String paramString, Bundle paramBundle)
  {
    return false;
  }
  
  public boolean reportFullscreenMode(boolean paramBoolean)
  {
    return true;
  }
  
  public boolean requestCursorUpdates(int paramInt)
  {
    return false;
  }
  
  public boolean sendKeyEvent(KeyEvent paramKeyEvent)
  {
    mIMM.dispatchKeyEventFromInputMethod(mTargetView, paramKeyEvent);
    return false;
  }
  
  public boolean setComposingRegion(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable != null)
    {
      beginBatchEdit();
      removeComposingSpans(localEditable);
      int i = paramInt1;
      int j = paramInt2;
      paramInt2 = i;
      paramInt1 = j;
      if (i > j)
      {
        paramInt2 = j;
        paramInt1 = i;
      }
      j = localEditable.length();
      i = paramInt2;
      if (paramInt2 < 0) {
        i = 0;
      }
      paramInt2 = paramInt1;
      if (paramInt1 < 0) {
        paramInt2 = 0;
      }
      paramInt1 = i;
      if (i > j) {
        paramInt1 = j;
      }
      i = paramInt2;
      if (paramInt2 > j) {
        i = j;
      }
      ensureDefaultComposingSpans();
      if (mDefaultComposingSpans != null) {
        for (paramInt2 = 0; paramInt2 < mDefaultComposingSpans.length; paramInt2++) {
          localEditable.setSpan(mDefaultComposingSpans[paramInt2], paramInt1, i, 289);
        }
      }
      localEditable.setSpan(COMPOSING, paramInt1, i, 289);
      sendCurrentText();
      endBatchEdit();
    }
    return true;
  }
  
  public boolean setComposingText(CharSequence paramCharSequence, int paramInt)
  {
    replaceText(paramCharSequence, paramInt, true);
    return true;
  }
  
  public boolean setSelection(int paramInt1, int paramInt2)
  {
    Editable localEditable = getEditable();
    if (localEditable == null) {
      return false;
    }
    int i = localEditable.length();
    if ((paramInt1 <= i) && (paramInt2 <= i) && (paramInt1 >= 0) && (paramInt2 >= 0))
    {
      if ((paramInt1 == paramInt2) && (MetaKeyKeyListener.getMetaState(localEditable, 2048) != 0)) {
        Selection.extendSelection(localEditable, paramInt1);
      } else {
        Selection.setSelection(localEditable, paramInt1, paramInt2);
      }
      return true;
    }
    return true;
  }
}
