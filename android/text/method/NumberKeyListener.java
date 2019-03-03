package android.text.method;

import android.icu.text.DecimalFormatSymbols;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import libcore.icu.LocaleData;

public abstract class NumberKeyListener
  extends BaseKeyListener
  implements InputFilter
{
  private static final String DATE_TIME_FORMAT_SYMBOLS = "GyYuUrQqMLlwWdDFgEecabBhHKkjJCmsSAzZOvVXx";
  private static final char SINGLE_QUOTE = '\'';
  
  public NumberKeyListener() {}
  
  static boolean addAmPmChars(Collection<Character> paramCollection, Locale paramLocale)
  {
    if (paramLocale == null) {
      return false;
    }
    paramLocale = getamPm;
    for (int i = 0; i < paramLocale.length; i++)
    {
      int j = 0;
      while (j < paramLocale[i].length())
      {
        char c = paramLocale[i].charAt(j);
        if (Character.isBmpCodePoint(c))
        {
          paramCollection.add(Character.valueOf(c));
          j++;
        }
        else
        {
          return false;
        }
      }
    }
    return true;
  }
  
  static boolean addDigits(Collection<Character> paramCollection, Locale paramLocale)
  {
    if (paramLocale == null) {
      return false;
    }
    paramLocale = DecimalFormatSymbols.getInstance(paramLocale).getDigitStrings();
    for (int i = 0; i < 10; i++)
    {
      if (paramLocale[i].length() > 1) {
        return false;
      }
      paramCollection.add(Character.valueOf(paramLocale[i].charAt(0)));
    }
    return true;
  }
  
  static boolean addFormatCharsFromSkeleton(Collection<Character> paramCollection, Locale paramLocale, String paramString1, String paramString2)
  {
    if (paramLocale == null) {
      return false;
    }
    paramLocale = DateFormat.getBestDateTimePattern(paramLocale, paramString1);
    int i = 1;
    for (int j = 0;; j++)
    {
      int k = paramLocale.length();
      int m = 1;
      if (j >= k) {
        break;
      }
      char c = paramLocale.charAt(j);
      if (Character.isSurrogate(c)) {
        return false;
      }
      k = i;
      if (c == '\'')
      {
        if (i != 0) {
          m = 0;
        }
        i = m;
        if (j == 0) {
          continue;
        }
        k = m;
        if (paramLocale.charAt(j - 1) != '\'')
        {
          i = m;
          continue;
        }
      }
      if (k != 0)
      {
        if (paramString2.indexOf(c) != -1)
        {
          i = k;
          continue;
        }
        if ("GyYuUrQqMLlwWdDFgEecabBhHKkjJCmsSAzZOvVXx".indexOf(c) != -1) {
          return false;
        }
      }
      paramCollection.add(Character.valueOf(c));
      i = k;
    }
    return true;
  }
  
  static boolean addFormatCharsFromSkeletons(Collection<Character> paramCollection, Locale paramLocale, String[] paramArrayOfString, String paramString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (!addFormatCharsFromSkeleton(paramCollection, paramLocale, paramArrayOfString[i], paramString)) {
        return false;
      }
    }
    return true;
  }
  
  static char[] collectionToArray(Collection<Character> paramCollection)
  {
    char[] arrayOfChar = new char[paramCollection.size()];
    int i = 0;
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      arrayOfChar[i] = ((Character)paramCollection.next()).charValue();
      i++;
    }
    return arrayOfChar;
  }
  
  protected static boolean ok(char[] paramArrayOfChar, char paramChar)
  {
    for (int i = paramArrayOfChar.length - 1; i >= 0; i--) {
      if (paramArrayOfChar[i] == paramChar) {
        return true;
      }
    }
    return false;
  }
  
  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
  {
    paramSpanned = getAcceptedChars();
    for (paramInt3 = paramInt1; (paramInt3 < paramInt2) && (ok(paramSpanned, paramCharSequence.charAt(paramInt3))); paramInt3++) {}
    if (paramInt3 == paramInt2) {
      return null;
    }
    if (paramInt2 - paramInt1 == 1) {
      return "";
    }
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence, paramInt1, paramInt2);
    paramInt2 -= paramInt1;
    paramInt2--;
    while (paramInt2 >= paramInt3 - paramInt1)
    {
      if (!ok(paramSpanned, paramCharSequence.charAt(paramInt2))) {
        localSpannableStringBuilder.delete(paramInt2, paramInt2 + 1);
      }
      paramInt2--;
    }
    return localSpannableStringBuilder;
  }
  
  protected abstract char[] getAcceptedChars();
  
  protected int lookup(KeyEvent paramKeyEvent, Spannable paramSpannable)
  {
    return paramKeyEvent.getMatch(getAcceptedChars(), getMetaState(paramSpannable, paramKeyEvent));
  }
  
  public boolean onKeyDown(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = Selection.getSelectionStart(paramEditable);
    int j = Selection.getSelectionEnd(paramEditable);
    int k = Math.min(i, j);
    i = Math.max(i, j);
    int m = 0;
    if (k >= 0)
    {
      j = i;
      if (i >= 0) {}
    }
    else
    {
      j = 0;
      k = 0;
      Selection.setSelection(paramEditable, 0);
    }
    if (paramKeyEvent != null) {
      i = lookup(paramKeyEvent, paramEditable);
    } else {
      i = 0;
    }
    if (paramKeyEvent != null) {
      m = paramKeyEvent.getRepeatCount();
    }
    if (m == 0)
    {
      if (i != 0)
      {
        if (k != j) {
          Selection.setSelection(paramEditable, j);
        }
        paramEditable.replace(k, j, String.valueOf((char)i));
        adjustMetaAfterKeypress(paramEditable);
        return true;
      }
    }
    else if ((i == 48) && (m == 1) && (k == j) && (j > 0) && (paramEditable.charAt(k - 1) == '0'))
    {
      paramEditable.replace(k - 1, j, String.valueOf('+'));
      adjustMetaAfterKeypress(paramEditable);
      return true;
    }
    adjustMetaAfterKeypress(paramEditable);
    return super.onKeyDown(paramView, paramEditable, paramInt, paramKeyEvent);
  }
}
