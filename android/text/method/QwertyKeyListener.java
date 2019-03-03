package android.text.method;

import android.text.AutoText;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

public class QwertyKeyListener
  extends BaseKeyListener
{
  private static SparseArray<String> PICKER_SETS;
  private static QwertyKeyListener sFullKeyboardInstance;
  private static QwertyKeyListener[] sInstance = new QwertyKeyListener[TextKeyListener.Capitalize.values().length * 2];
  private TextKeyListener.Capitalize mAutoCap;
  private boolean mAutoText;
  private boolean mFullKeyboard;
  
  static
  {
    PICKER_SETS = new SparseArray();
    PICKER_SETS.put(65, "ÀÁÂÄÆÃÅĄĀ");
    PICKER_SETS.put(67, "ÇĆČ");
    PICKER_SETS.put(68, "Ď");
    PICKER_SETS.put(69, "ÈÉÊËĘĚĒ");
    PICKER_SETS.put(71, "Ğ");
    PICKER_SETS.put(76, "Ł");
    PICKER_SETS.put(73, "ÌÍÎÏĪİ");
    PICKER_SETS.put(78, "ÑŃŇ");
    PICKER_SETS.put(79, "ØŒÕÒÓÔÖŌ");
    PICKER_SETS.put(82, "Ř");
    PICKER_SETS.put(83, "ŚŠŞ");
    PICKER_SETS.put(84, "Ť");
    PICKER_SETS.put(85, "ÙÚÛÜŮŪ");
    PICKER_SETS.put(89, "ÝŸ");
    PICKER_SETS.put(90, "ŹŻŽ");
    PICKER_SETS.put(97, "àáâäæãåąā");
    PICKER_SETS.put(99, "çćč");
    PICKER_SETS.put(100, "ď");
    PICKER_SETS.put(101, "èéêëęěē");
    PICKER_SETS.put(103, "ğ");
    PICKER_SETS.put(105, "ìíîïīı");
    PICKER_SETS.put(108, "ł");
    PICKER_SETS.put(110, "ñńň");
    PICKER_SETS.put(111, "øœõòóôöō");
    PICKER_SETS.put(114, "ř");
    PICKER_SETS.put(115, "§ßśšş");
    PICKER_SETS.put(116, "ť");
    PICKER_SETS.put(117, "ùúûüůū");
    PICKER_SETS.put(121, "ýÿ");
    PICKER_SETS.put(122, "źżž");
    PICKER_SETS.put(61185, "…¥•®©±[]{}\\|");
    PICKER_SETS.put(47, "\\");
    PICKER_SETS.put(49, "¹½⅓¼⅛");
    PICKER_SETS.put(50, "²⅔");
    PICKER_SETS.put(51, "³¾⅜");
    PICKER_SETS.put(52, "⁴");
    PICKER_SETS.put(53, "⅝");
    PICKER_SETS.put(55, "⅞");
    PICKER_SETS.put(48, "ⁿ∅");
    PICKER_SETS.put(36, "¢£€¥₣₤₱");
    PICKER_SETS.put(37, "‰");
    PICKER_SETS.put(42, "†‡");
    PICKER_SETS.put(45, "–—");
    PICKER_SETS.put(43, "±");
    PICKER_SETS.put(40, "[{<");
    PICKER_SETS.put(41, "]}>");
    PICKER_SETS.put(33, "¡");
    PICKER_SETS.put(34, "“”«»˝");
    PICKER_SETS.put(63, "¿");
    PICKER_SETS.put(44, "‚„");
    PICKER_SETS.put(61, "≠≈∞");
    PICKER_SETS.put(60, "≤«‹");
    PICKER_SETS.put(62, "≥»›");
  }
  
  public QwertyKeyListener(TextKeyListener.Capitalize paramCapitalize, boolean paramBoolean)
  {
    this(paramCapitalize, paramBoolean, false);
  }
  
  private QwertyKeyListener(TextKeyListener.Capitalize paramCapitalize, boolean paramBoolean1, boolean paramBoolean2)
  {
    mAutoCap = paramCapitalize;
    mAutoText = paramBoolean1;
    mFullKeyboard = paramBoolean2;
  }
  
  public static QwertyKeyListener getInstance(boolean paramBoolean, TextKeyListener.Capitalize paramCapitalize)
  {
    int i = paramCapitalize.ordinal() * 2 + paramBoolean;
    if (sInstance[i] == null) {
      sInstance[i] = new QwertyKeyListener(paramCapitalize, paramBoolean);
    }
    return sInstance[i];
  }
  
  public static QwertyKeyListener getInstanceForFullKeyboard()
  {
    if (sFullKeyboardInstance == null) {
      sFullKeyboardInstance = new QwertyKeyListener(TextKeyListener.Capitalize.NONE, false, true);
    }
    return sFullKeyboardInstance;
  }
  
  private String getReplacement(CharSequence paramCharSequence, int paramInt1, int paramInt2, View paramView)
  {
    int i = paramInt2 - paramInt1;
    int j = 0;
    String str = AutoText.get(paramCharSequence, paramInt1, paramInt2, paramView);
    Object localObject = str;
    if (str == null)
    {
      paramView = AutoText.get(TextUtils.substring(paramCharSequence, paramInt1, paramInt2).toLowerCase(), 0, paramInt2 - paramInt1, paramView);
      j = 1;
      localObject = paramView;
      if (paramView == null) {
        return null;
      }
    }
    int k = 0;
    if (j != 0)
    {
      j = 0;
      k = paramInt1;
      while (k < paramInt2)
      {
        int m = j;
        if (Character.isUpperCase(paramCharSequence.charAt(k))) {
          m = j + 1;
        }
        k++;
        j = m;
      }
      k = j;
    }
    if (k == 0) {
      paramView = (View)localObject;
    }
    for (;;)
    {
      break;
      if (k == 1) {
        paramView = toTitleCase((String)localObject);
      } else if (k == i) {
        paramView = ((String)localObject).toUpperCase();
      } else {
        paramView = toTitleCase((String)localObject);
      }
    }
    if ((paramView.length() == i) && (TextUtils.regionMatches(paramCharSequence, paramInt1, paramView, 0, i))) {
      return null;
    }
    return paramView;
  }
  
  public static void markAsReplaced(Spannable paramSpannable, int paramInt1, int paramInt2, String paramString)
  {
    Object localObject = (Replaced[])paramSpannable.getSpans(0, paramSpannable.length(), Replaced.class);
    for (int i = 0; i < localObject.length; i++) {
      paramSpannable.removeSpan(localObject[i]);
    }
    i = paramString.length();
    localObject = new char[i];
    paramString.getChars(0, i, (char[])localObject, 0);
    paramSpannable.setSpan(new Replaced((char[])localObject), paramInt1, paramInt2, 33);
  }
  
  private boolean showCharacterPicker(View paramView, Editable paramEditable, char paramChar, boolean paramBoolean, int paramInt)
  {
    String str = (String)PICKER_SETS.get(paramChar);
    if (str == null) {
      return false;
    }
    if (paramInt == 1) {
      new CharacterPickerDialog(paramView.getContext(), paramView, paramEditable, str, paramBoolean).show();
    }
    return true;
  }
  
  private static String toTitleCase(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Character.toUpperCase(paramString.charAt(0)));
    localStringBuilder.append(paramString.substring(1));
    return localStringBuilder.toString();
  }
  
  public int getInputType()
  {
    return makeTextContentType(mAutoCap, mAutoText);
  }
  
  public boolean onKeyDown(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = 0;
    if (paramView != null) {
      i = TextKeyListener.getInstance().getPrefs(paramView.getContext());
    }
    int j = Selection.getSelectionStart(paramEditable);
    int k = Selection.getSelectionEnd(paramEditable);
    int m = Math.min(j, k);
    k = Math.max(j, k);
    if (m >= 0)
    {
      j = k;
      if (k < 0) {}
    }
    for (;;)
    {
      break;
      j = 0;
      m = 0;
      Selection.setSelection(paramEditable, 0, 0);
    }
    int n = paramEditable.getSpanStart(TextKeyListener.ACTIVE);
    int i1 = paramEditable.getSpanEnd(TextKeyListener.ACTIVE);
    int i2 = paramKeyEvent.getUnicodeChar(getMetaState(paramEditable, paramKeyEvent));
    if (!mFullKeyboard)
    {
      k = paramKeyEvent.getRepeatCount();
      if ((k > 0) && (m == j) && (m > 0))
      {
        int i3 = paramEditable.charAt(m - 1);
        if ((i3 != i2) && (i3 != Character.toUpperCase(i2))) {
          break label209;
        }
        if ((paramView != null) && (showCharacterPicker(paramView, paramEditable, i3, false, k)))
        {
          resetMetaState(paramEditable);
          return true;
        }
      }
    }
    label209:
    if (i2 == 61185)
    {
      if (paramView != null) {
        showCharacterPicker(paramView, paramEditable, 61185, true, 1);
      }
      resetMetaState(paramEditable);
      return true;
    }
    int i6;
    if (i2 == 61184)
    {
      if (m == j) {
        for (i2 = j;; i2--)
        {
          k = i2;
          if (i2 <= 0) {
            break;
          }
          k = i2;
          if (j - i2 >= 4) {
            break;
          }
          k = i2;
          if (Character.digit(paramEditable.charAt(i2 - 1), 16) < 0) {
            break;
          }
        }
      }
      k = m;
      i6 = -1;
      try
      {
        i2 = Integer.parseInt(TextUtils.substring(paramEditable, k, j), 16);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        i2 = i6;
      }
      if (i2 >= 0)
      {
        Selection.setSelection(paramEditable, k, j);
        m = k;
      }
      else
      {
        i2 = 0;
      }
    }
    if (i2 != 0)
    {
      k = 0;
      paramInt = i2;
      if ((0x80000000 & i2) != 0)
      {
        k = 1;
        paramInt = i2 & 0x7FFFFFFF;
      }
      int i7 = k;
      i2 = paramInt;
      int i8 = m;
      if (n == m)
      {
        i7 = k;
        i2 = paramInt;
        i8 = m;
        if (i1 == j)
        {
          i7 = 0;
          i1 = k;
          n = i7;
          i6 = paramInt;
          if (j - m - 1 == 0)
          {
            i2 = KeyEvent.getDeadChar(paramEditable.charAt(m), paramInt);
            i1 = k;
            n = i7;
            i6 = paramInt;
            if (i2 != 0)
            {
              i6 = i2;
              n = 1;
              i1 = 0;
            }
          }
          i7 = i1;
          i2 = i6;
          i8 = m;
          if (n == 0)
          {
            Selection.setSelection(paramEditable, j);
            paramEditable.removeSpan(TextKeyListener.ACTIVE);
            i8 = j;
            i2 = i6;
            i7 = i1;
          }
        }
      }
      paramInt = i2;
      if ((i & 0x1) != 0)
      {
        paramInt = i2;
        if (Character.isLowerCase(i2))
        {
          paramInt = i2;
          if (TextKeyListener.shouldCap(mAutoCap, paramEditable, i8))
          {
            paramInt = paramEditable.getSpanEnd(TextKeyListener.CAPPED);
            k = paramEditable.getSpanFlags(TextKeyListener.CAPPED);
            if ((paramInt == i8) && ((k >> 16 & 0xFFFF) == i2))
            {
              paramEditable.removeSpan(TextKeyListener.CAPPED);
              paramInt = i2;
            }
            else
            {
              k = i2 << 16;
              paramInt = Character.toUpperCase(i2);
              if (i8 == 0) {
                paramEditable.setSpan(TextKeyListener.CAPPED, 0, 0, 0x11 | k);
              } else {
                paramEditable.setSpan(TextKeyListener.CAPPED, i8 - 1, i8, 0x21 | k);
              }
            }
          }
        }
      }
      if (i8 != j) {
        Selection.setSelection(paramEditable, j);
      }
      paramEditable.setSpan(OLD_SEL_START, i8, i8, 17);
      paramEditable.replace(i8, j, String.valueOf((char)paramInt));
      k = paramEditable.getSpanStart(OLD_SEL_START);
      j = Selection.getSelectionEnd(paramEditable);
      if (k < j)
      {
        paramEditable.setSpan(TextKeyListener.LAST_TYPED, k, j, 33);
        if (i7 != 0)
        {
          Selection.setSelection(paramEditable, k, j);
          paramEditable.setSpan(TextKeyListener.ACTIVE, k, j, 33);
        }
      }
      adjustMetaAfterKeypress(paramEditable);
      if (((i & 0x2) != 0) && (mAutoText))
      {
        if ((paramInt != 32) && (paramInt != 9) && (paramInt != 10) && (paramInt != 44) && (paramInt != 46) && (paramInt != 33) && (paramInt != 63) && (paramInt != 34) && (Character.getType(paramInt) != 22)) {
          break label1049;
        }
        if (paramEditable.getSpanEnd(TextKeyListener.INHIBIT_REPLACEMENT) != k)
        {
          for (paramInt = k; paramInt > 0; paramInt--)
          {
            char c = paramEditable.charAt(paramInt - 1);
            if ((c != '\'') && (!Character.isLetter(c))) {
              break;
            }
          }
          paramView = getReplacement(paramEditable, paramInt, k, paramView);
          if (paramView != null)
          {
            paramKeyEvent = (Replaced[])paramEditable.getSpans(0, paramEditable.length(), Replaced.class);
            for (j = 0; j < paramKeyEvent.length; j++) {
              paramEditable.removeSpan(paramKeyEvent[j]);
            }
            paramKeyEvent = new char[k - paramInt];
            TextUtils.getChars(paramEditable, paramInt, k, paramKeyEvent, 0);
            paramEditable.setSpan(new Replaced(paramKeyEvent), paramInt, k, 33);
            paramEditable.replace(paramInt, k, paramView);
          }
        }
      }
      label1049:
      if (((i & 0x4) != 0) && (mAutoText))
      {
        j = Selection.getSelectionEnd(paramEditable);
        if ((j - 3 >= 0) && (paramEditable.charAt(j - 1) == ' ') && (paramEditable.charAt(j - 2) == ' '))
        {
          k = paramEditable.charAt(j - 3);
          paramInt = j - 3;
          int i5;
          for (int i4 = k; paramInt > 0; i5 = k)
          {
            if (i4 != 34) {
              if (Character.getType(i4) != 22) {
                break;
              }
            }
            k = paramEditable.charAt(paramInt - 1);
            paramInt--;
          }
          if ((Character.isLetter(i5)) || (Character.isDigit(i5))) {
            paramEditable.replace(j - 2, j - 1, ".");
          }
        }
      }
      return true;
    }
    if (paramInt == 67)
    {
      if ((!paramKeyEvent.hasNoModifiers()) && (!paramKeyEvent.hasModifiers(2))) {
        break label1464;
      }
      if (m == j)
      {
        k = 1;
        j = k;
        if (paramEditable.getSpanEnd(TextKeyListener.LAST_TYPED) == m)
        {
          j = k;
          if (paramEditable.charAt(m - 1) != '\n') {
            j = 2;
          }
        }
        Replaced[] arrayOfReplaced = (Replaced[])paramEditable.getSpans(m - j, m, Replaced.class);
        if (arrayOfReplaced.length > 0)
        {
          j = paramEditable.getSpanStart(arrayOfReplaced[0]);
          k = paramEditable.getSpanEnd(arrayOfReplaced[0]);
          String str = new String(0mText);
          paramEditable.removeSpan(arrayOfReplaced[0]);
          if (m >= k)
          {
            paramEditable.setSpan(TextKeyListener.INHIBIT_REPLACEMENT, k, k, 34);
            paramEditable.replace(j, k, str);
            paramInt = paramEditable.getSpanStart(TextKeyListener.INHIBIT_REPLACEMENT);
            if (paramInt - 1 >= 0) {
              paramEditable.setSpan(TextKeyListener.INHIBIT_REPLACEMENT, paramInt - 1, paramInt, 33);
            } else {
              paramEditable.removeSpan(TextKeyListener.INHIBIT_REPLACEMENT);
            }
            adjustMetaAfterKeypress(paramEditable);
            return true;
          }
          adjustMetaAfterKeypress(paramEditable);
          return super.onKeyDown(paramView, paramEditable, paramInt, paramKeyEvent);
        }
      }
    }
    label1464:
    return super.onKeyDown(paramView, paramEditable, paramInt, paramKeyEvent);
  }
  
  static class Replaced
    implements NoCopySpan
  {
    private char[] mText;
    
    public Replaced(char[] paramArrayOfChar)
    {
      mText = paramArrayOfChar;
    }
  }
}
