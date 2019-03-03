package android.text.method;

import android.graphics.Paint;
import android.icu.lang.UCharacter;
import android.text.Editable;
import android.text.Emoji;
import android.text.Layout;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.Spanned;
import android.text.style.ReplacementSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.android.internal.annotations.GuardedBy;

public abstract class BaseKeyListener
  extends MetaKeyKeyListener
  implements KeyListener
{
  private static final int CARRIAGE_RETURN = 13;
  private static final int LINE_FEED = 10;
  static final Object OLD_SEL_START = new NoCopySpan.Concrete();
  @GuardedBy("mLock")
  static Paint sCachedPaint = null;
  private final Object mLock = new Object();
  
  public BaseKeyListener() {}
  
  private static int adjustReplacementSpan(CharSequence paramCharSequence, int paramInt, boolean paramBoolean)
  {
    if (!(paramCharSequence instanceof Spanned)) {
      return paramInt;
    }
    ReplacementSpan[] arrayOfReplacementSpan = (ReplacementSpan[])((Spanned)paramCharSequence).getSpans(paramInt, paramInt, ReplacementSpan.class);
    int i = 0;
    for (int j = paramInt; i < arrayOfReplacementSpan.length; j = paramInt)
    {
      int k = ((Spanned)paramCharSequence).getSpanStart(arrayOfReplacementSpan[i]);
      int m = ((Spanned)paramCharSequence).getSpanEnd(arrayOfReplacementSpan[i]);
      paramInt = j;
      if (k < j)
      {
        paramInt = j;
        if (m > j) {
          if (paramBoolean) {
            paramInt = k;
          } else {
            paramInt = m;
          }
        }
      }
      i++;
    }
    return j;
  }
  
  private boolean backspaceOrForwardDelete(View paramView, Editable paramEditable, int paramInt, KeyEvent arg4, boolean paramBoolean)
  {
    if (!KeyEvent.metaStateHasNoModifiers(???.getMetaState() & 0x8F0C)) {
      return false;
    }
    if (deleteSelection(paramView, paramEditable)) {
      return true;
    }
    if ((???.getMetaState() & 0x1000) != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    if (getMetaState(paramEditable, 1, ???) == 1) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if (getMetaState(paramEditable, 2, ???) == 1) {
      j = 1;
    } else {
      j = 0;
    }
    if (paramInt != 0)
    {
      if ((j == 0) && (i == 0)) {
        return deleteUntilWordBoundary(paramView, paramEditable, paramBoolean);
      }
      return false;
    }
    if ((j != 0) && (deleteLine(paramView, paramEditable))) {
      return true;
    }
    int i = Selection.getSelectionEnd(paramEditable);
    if (paramBoolean)
    {
      if ((paramView instanceof TextView)) {
        paramView = ((TextView)paramView).getPaint();
      }
      synchronized (mLock)
      {
        if (sCachedPaint == null)
        {
          paramView = new android/graphics/Paint;
          paramView.<init>();
          sCachedPaint = paramView;
        }
        paramView = sCachedPaint;
        paramInt = getOffsetForForwardDeleteKey(paramEditable, i, paramView);
      }
    }
    paramInt = getOffsetForBackspaceKey(paramEditable, i);
    if (i != paramInt)
    {
      paramEditable.delete(Math.min(i, paramInt), Math.max(i, paramInt));
      return true;
    }
    return false;
  }
  
  private boolean deleteLine(View paramView, Editable paramEditable)
  {
    if ((paramView instanceof TextView))
    {
      paramView = ((TextView)paramView).getLayout();
      if (paramView != null)
      {
        int i = paramView.getLineForOffset(Selection.getSelectionStart(paramEditable));
        int j = paramView.getLineStart(i);
        i = paramView.getLineEnd(i);
        if (i != j)
        {
          paramEditable.delete(j, i);
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean deleteSelection(View paramView, Editable paramEditable)
  {
    int i = Selection.getSelectionStart(paramEditable);
    int j = Selection.getSelectionEnd(paramEditable);
    int k = i;
    int m = j;
    if (j < i)
    {
      m = i;
      k = j;
    }
    if (k != m)
    {
      paramEditable.delete(k, m);
      return true;
    }
    return false;
  }
  
  private boolean deleteUntilWordBoundary(View paramView, Editable paramEditable, boolean paramBoolean)
  {
    int i = Selection.getSelectionStart(paramEditable);
    if (i != Selection.getSelectionEnd(paramEditable)) {
      return false;
    }
    if (((!paramBoolean) && (i == 0)) || ((paramBoolean) && (i == paramEditable.length()))) {
      return false;
    }
    WordIterator localWordIterator = null;
    if ((paramView instanceof TextView)) {
      localWordIterator = ((TextView)paramView).getWordIterator();
    }
    paramView = localWordIterator;
    if (localWordIterator == null) {
      paramView = new WordIterator();
    }
    int j;
    int k;
    int m;
    if (paramBoolean)
    {
      j = i;
      paramView.setCharSequence(paramEditable, j, paramEditable.length());
      k = paramView.following(i);
      m = j;
      i = k;
      if (k == -1)
      {
        i = paramEditable.length();
        m = j;
      }
    }
    else
    {
      j = i;
      paramView.setCharSequence(paramEditable, 0, j);
      k = paramView.preceding(i);
      m = k;
      i = j;
      if (k == -1)
      {
        m = 0;
        i = j;
      }
    }
    paramEditable.delete(m, i);
    return true;
  }
  
  private static int getOffsetForBackspaceKey(CharSequence paramCharSequence, int paramInt)
  {
    if (paramInt <= 1) {
      return 0;
    }
    int i = 0;
    int j = 1;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = paramInt;
    for (;;)
    {
      int i2 = Character.codePointBefore(paramCharSequence, i1);
      i1 -= Character.charCount(i2);
      int i3;
      switch (k)
      {
      default: 
        paramCharSequence = new StringBuilder();
        paramCharSequence.append("state ");
        paramCharSequence.append(k);
        paramCharSequence.append(" is unknown");
        throw new IllegalArgumentException(paramCharSequence.toString());
      case 12: 
        if (Emoji.isTagSpecChar(i2))
        {
          n += 2;
          i3 = m;
        }
        else if (Emoji.isEmoji(i2))
        {
          n += Character.charCount(i2);
          k = 13;
          i3 = m;
        }
        else
        {
          n = 2;
          k = 13;
          i3 = m;
        }
        break;
      case 11: 
        if (Emoji.isRegionalIndicatorSymbol(i2))
        {
          n -= 2;
          k = 10;
          i3 = m;
        }
        else
        {
          k = 13;
          i3 = m;
        }
        break;
      case 10: 
        if (Emoji.isRegionalIndicatorSymbol(i2))
        {
          n += 2;
          k = 11;
          i3 = m;
        }
        else
        {
          k = 13;
          i3 = m;
        }
        break;
      case 9: 
        if (Emoji.isEmoji(i2))
        {
          n += m + 1 + Character.charCount(i2);
          i3 = 0;
          k = 7;
        }
        else
        {
          k = 13;
          i3 = m;
        }
        break;
      case 8: 
        if (Emoji.isEmoji(i2))
        {
          n += Character.charCount(i2) + 1;
          if (Emoji.isEmojiModifier(i2)) {
            k = 4;
          } else {
            k = 7;
          }
          i3 = m;
        }
        else if (isVariationSelector(i2))
        {
          i3 = Character.charCount(i2);
          k = 9;
        }
        else
        {
          k = 13;
          i3 = m;
        }
        break;
      case 7: 
        if (i2 == Emoji.ZERO_WIDTH_JOINER)
        {
          k = 8;
          i3 = m;
        }
        else
        {
          k = 13;
          i3 = m;
        }
        break;
      case 6: 
        if (Emoji.isEmoji(i2))
        {
          n += Character.charCount(i2);
          k = 7;
          i3 = m;
        }
        else
        {
          i3 = n;
          if (!isVariationSelector(i2))
          {
            i3 = n;
            if (UCharacter.getCombiningClass(i2) == 0) {
              i3 = n + Character.charCount(i2);
            }
          }
          k = 13;
          n = i3;
          i3 = m;
        }
        break;
      case 5: 
        i3 = n;
        if (Emoji.isEmojiModifierBase(i2)) {
          i3 = n + (m + Character.charCount(i2));
        }
        k = 13;
        n = i3;
        i3 = m;
        break;
      case 4: 
        if (isVariationSelector(i2))
        {
          i3 = Character.charCount(i2);
          k = 5;
        }
        else
        {
          i3 = n;
          if (Emoji.isEmojiModifierBase(i2)) {
            i3 = n + Character.charCount(i2);
          }
          k = 13;
          n = i3;
          i3 = m;
        }
        break;
      case 3: 
        i3 = n;
        if (Emoji.isKeycapBase(i2)) {
          i3 = n + (m + Character.charCount(i2));
        }
        k = 13;
        n = i3;
        i3 = m;
        break;
      case 2: 
        if (isVariationSelector(i2))
        {
          i3 = Character.charCount(i2);
          k = 3;
        }
        else
        {
          i3 = n;
          if (Emoji.isKeycapBase(i2)) {
            i3 = n + Character.charCount(i2);
          }
          k = 13;
          n = i3;
          i3 = m;
        }
        break;
      case 1: 
        i3 = n;
        if (i2 == 13) {
          i3 = n + 1;
        }
        k = 13;
        n = i3;
        i3 = m;
        break;
      }
      n = Character.charCount(i2);
      if (i2 == 10) {
        k = 1;
      }
      for (;;)
      {
        i3 = m;
        break;
        if (isVariationSelector(i2)) {
          k = 6;
        } else if (Emoji.isRegionalIndicatorSymbol(i2)) {
          k = 10;
        } else if (Emoji.isEmojiModifier(i2)) {
          k = 4;
        } else if (i2 == Emoji.COMBINING_ENCLOSING_KEYCAP) {
          k = 2;
        } else if (Emoji.isEmoji(i2)) {
          k = 7;
        } else if (i2 == Emoji.CANCEL_TAG) {
          k = 12;
        } else {
          k = 13;
        }
      }
      if ((i1 <= 0) || (k == 13)) {
        break;
      }
      m = i3;
    }
    return adjustReplacementSpan(paramCharSequence, paramInt - n, true);
  }
  
  private static int getOffsetForForwardDeleteKey(CharSequence paramCharSequence, int paramInt, Paint paramPaint)
  {
    int i = paramCharSequence.length();
    if (paramInt >= i - 1) {
      return i;
    }
    return adjustReplacementSpan(paramCharSequence, paramPaint.getTextRunCursor(paramCharSequence, paramInt, i, 0, paramInt, 0), false);
  }
  
  private static boolean isVariationSelector(int paramInt)
  {
    return UCharacter.hasBinaryProperty(paramInt, 36);
  }
  
  static int makeTextContentType(TextKeyListener.Capitalize paramCapitalize, boolean paramBoolean)
  {
    int i = 1;
    switch (1.$SwitchMap$android$text$method$TextKeyListener$Capitalize[paramCapitalize.ordinal()])
    {
    default: 
      break;
    case 3: 
      i = 0x1 | 0x4000;
      break;
    case 2: 
      i = 0x1 | 0x2000;
      break;
    case 1: 
      i = 0x1 | 0x1000;
    }
    int j = i;
    if (paramBoolean) {
      j = i | 0x8000;
    }
    return j;
  }
  
  public boolean backspace(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    return backspaceOrForwardDelete(paramView, paramEditable, paramInt, paramKeyEvent, false);
  }
  
  public boolean forwardDelete(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    return backspaceOrForwardDelete(paramView, paramEditable, paramInt, paramKeyEvent, true);
  }
  
  public boolean onKeyDown(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (paramInt != 67)
    {
      if (paramInt != 112) {
        bool = false;
      } else {
        bool = forwardDelete(paramView, paramEditable, paramInt, paramKeyEvent);
      }
    }
    else {
      bool = backspace(paramView, paramEditable, paramInt, paramKeyEvent);
    }
    if (bool)
    {
      adjustMetaAfterKeypress(paramEditable);
      return true;
    }
    return super.onKeyDown(paramView, paramEditable, paramInt, paramKeyEvent);
  }
  
  public boolean onKeyOther(View paramView, Editable paramEditable, KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getAction() == 2) && (paramKeyEvent.getKeyCode() == 0))
    {
      int i = Selection.getSelectionStart(paramEditable);
      int j = Selection.getSelectionEnd(paramEditable);
      int k = i;
      int m = j;
      if (j < i)
      {
        m = i;
        k = j;
      }
      paramView = paramKeyEvent.getCharacters();
      if (paramView == null) {
        return false;
      }
      paramEditable.replace(k, m, paramView);
      return true;
    }
    return false;
  }
}
