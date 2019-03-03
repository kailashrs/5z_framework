package android.telephony;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import com.android.i18n.phonenumbers.AsYouTypeFormatter;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import java.util.Locale;

public class PhoneNumberFormattingTextWatcher
  implements TextWatcher
{
  private AsYouTypeFormatter mFormatter;
  private boolean mSelfChange = false;
  private boolean mStopFormatting;
  
  public PhoneNumberFormattingTextWatcher()
  {
    this(Locale.getDefault().getCountry());
  }
  
  public PhoneNumberFormattingTextWatcher(String paramString)
  {
    if (paramString != null)
    {
      mFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(paramString);
      return;
    }
    throw new IllegalArgumentException();
  }
  
  private String getFormattedNumber(char paramChar, boolean paramBoolean)
  {
    String str;
    if (paramBoolean) {
      str = mFormatter.inputDigitAndRememberPosition(paramChar);
    } else {
      str = mFormatter.inputDigit(paramChar);
    }
    return str;
  }
  
  private boolean hasSeparator(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
      if (!PhoneNumberUtils.isNonSeparator(paramCharSequence.charAt(i))) {
        return true;
      }
    }
    return false;
  }
  
  private String reformat(CharSequence paramCharSequence, int paramInt)
  {
    Object localObject1 = null;
    mFormatter.clear();
    char c1 = '\000';
    boolean bool1 = false;
    int i = paramCharSequence.length();
    int j = 0;
    for (char c2 = c1; j < i; c2 = c1)
    {
      char c3 = paramCharSequence.charAt(j);
      Object localObject2 = localObject1;
      c1 = c2;
      boolean bool2 = bool1;
      if (PhoneNumberUtils.isNonSeparator(c3))
      {
        bool2 = bool1;
        if (c2 != 0)
        {
          localObject1 = getFormattedNumber(c2, bool1);
          bool2 = false;
        }
        c1 = c3;
        localObject2 = localObject1;
      }
      bool1 = bool2;
      if (j == paramInt - 1) {
        bool1 = true;
      }
      j++;
      localObject1 = localObject2;
    }
    if (c2 != 0) {
      localObject1 = getFormattedNumber(c2, bool1);
    }
    return localObject1;
  }
  
  private void stopFormatting()
  {
    mStopFormatting = true;
    mFormatter.clear();
  }
  
  public void afterTextChanged(Editable paramEditable)
  {
    try
    {
      boolean bool1 = mStopFormatting;
      boolean bool2 = true;
      if (bool1)
      {
        if (paramEditable.length() == 0) {
          bool2 = false;
        }
        mStopFormatting = bool2;
        return;
      }
      bool2 = mSelfChange;
      if (bool2) {
        return;
      }
      String str = reformat(paramEditable, Selection.getSelectionEnd(paramEditable));
      if (str != null)
      {
        int i = mFormatter.getRememberedPosition();
        mSelfChange = true;
        paramEditable.replace(0, paramEditable.length(), str, 0, str.length());
        if (str.equals(paramEditable.toString())) {
          Selection.setSelection(paramEditable, i);
        }
        mSelfChange = false;
      }
      PhoneNumberUtils.ttsSpanAsPhoneNumber(paramEditable, 0, paramEditable.length());
      return;
    }
    finally {}
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!mSelfChange) && (!mStopFormatting))
    {
      if ((paramInt2 > 0) && (hasSeparator(paramCharSequence, paramInt1, paramInt2))) {
        stopFormatting();
      }
      return;
    }
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!mSelfChange) && (!mStopFormatting))
    {
      if ((paramInt3 > 0) && (hasSeparator(paramCharSequence, paramInt1, paramInt3))) {
        stopFormatting();
      }
      return;
    }
  }
}
