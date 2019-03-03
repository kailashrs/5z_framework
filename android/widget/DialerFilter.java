package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.AllCaps;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;

@Deprecated
public class DialerFilter
  extends RelativeLayout
{
  public static final int DIGITS_AND_LETTERS = 1;
  public static final int DIGITS_AND_LETTERS_NO_DIGITS = 2;
  public static final int DIGITS_AND_LETTERS_NO_LETTERS = 3;
  public static final int DIGITS_ONLY = 4;
  public static final int LETTERS_ONLY = 5;
  EditText mDigits;
  EditText mHint;
  ImageView mIcon;
  InputFilter[] mInputFilters;
  private boolean mIsQwerty;
  EditText mLetters;
  int mMode;
  EditText mPrimary;
  
  public DialerFilter(Context paramContext)
  {
    super(paramContext);
  }
  
  public DialerFilter(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void makeDigitsPrimary()
  {
    if (mPrimary == mLetters) {
      swapPrimaryAndHint(false);
    }
  }
  
  private void makeLettersPrimary()
  {
    if (mPrimary == mDigits) {
      swapPrimaryAndHint(true);
    }
  }
  
  private void swapPrimaryAndHint(boolean paramBoolean)
  {
    Editable localEditable1 = mLetters.getText();
    Editable localEditable2 = mDigits.getText();
    Object localObject = mLetters.getKeyListener();
    KeyListener localKeyListener = mDigits.getKeyListener();
    if (paramBoolean)
    {
      mLetters = mPrimary;
      mDigits = mHint;
    }
    else
    {
      mLetters = mHint;
      mDigits = mPrimary;
    }
    mLetters.setKeyListener((KeyListener)localObject);
    mLetters.setText(localEditable1);
    localObject = mLetters.getText();
    Selection.setSelection((Spannable)localObject, ((Editable)localObject).length());
    mDigits.setKeyListener(localKeyListener);
    mDigits.setText(localEditable2);
    localEditable2 = mDigits.getText();
    Selection.setSelection(localEditable2, localEditable2.length());
    mPrimary.setFilters(mInputFilters);
    mHint.setFilters(mInputFilters);
  }
  
  public void append(String paramString)
  {
    switch (mMode)
    {
    default: 
      break;
    case 3: 
    case 4: 
      mDigits.getText().append(paramString);
      break;
    case 2: 
    case 5: 
      mLetters.getText().append(paramString);
      break;
    case 1: 
      mDigits.getText().append(paramString);
      mLetters.getText().append(paramString);
    }
  }
  
  public void clearText()
  {
    mLetters.getText().clear();
    mDigits.getText().clear();
    if (mIsQwerty) {
      setMode(1);
    } else {
      setMode(4);
    }
  }
  
  public CharSequence getDigits()
  {
    if (mDigits.getVisibility() == 0) {
      return mDigits.getText();
    }
    return "";
  }
  
  public CharSequence getFilterText()
  {
    if (mMode != 4) {
      return getLetters();
    }
    return getDigits();
  }
  
  public CharSequence getLetters()
  {
    if (mLetters.getVisibility() == 0) {
      return mLetters.getText();
    }
    return "";
  }
  
  public int getMode()
  {
    return mMode;
  }
  
  public boolean isQwertyKeyboard()
  {
    return mIsQwerty;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mInputFilters = new InputFilter[] { new InputFilter.AllCaps() };
    mHint = ((EditText)findViewById(16908293));
    if (mHint != null)
    {
      mHint.setFilters(mInputFilters);
      mLetters = mHint;
      mLetters.setKeyListener(TextKeyListener.getInstance());
      mLetters.setMovementMethod(null);
      mLetters.setFocusable(false);
      mPrimary = ((EditText)findViewById(16908300));
      if (mPrimary != null)
      {
        mPrimary.setFilters(mInputFilters);
        mDigits = mPrimary;
        mDigits.setKeyListener(DialerKeyListener.getInstance());
        mDigits.setMovementMethod(null);
        mDigits.setFocusable(false);
        mIcon = ((ImageView)findViewById(16908294));
        setFocusable(true);
        mIsQwerty = true;
        setMode(1);
        return;
      }
      throw new IllegalStateException("DialerFilter must have a child EditText named primary");
    }
    throw new IllegalStateException("DialerFilter must have a child EditText named hint");
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (mIcon != null)
    {
      paramRect = mIcon;
      if (paramBoolean) {
        paramInt = 0;
      } else {
        paramInt = 8;
      }
      paramRect.setVisibility(paramInt);
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (mMode)
        {
        default: 
          bool2 = bool1;
          break;
        case 3: 
        case 4: 
          bool2 = mDigits.onKeyDown(paramInt, paramKeyEvent);
          break;
        case 2: 
        case 5: 
          bool2 = mLetters.onKeyDown(paramInt, paramKeyEvent);
          break;
        case 1: 
          bool1 = mLetters.onKeyDown(paramInt, paramKeyEvent);
          if (KeyEvent.isModifierKey(paramInt))
          {
            mDigits.onKeyDown(paramInt, paramKeyEvent);
            bool2 = true;
          }
          else if ((!paramKeyEvent.isPrintingKey()) && (paramInt != 62))
          {
            bool2 = bool1;
            if (paramInt != 61) {
              break;
            }
          }
          else if (paramKeyEvent.getMatch(DialerKeyListener.CHARACTERS) != 0)
          {
            bool2 = bool1 & mDigits.onKeyDown(paramInt, paramKeyEvent);
          }
          else
          {
            setMode(2);
            bool2 = bool1;
          }
          break;
        }
        break;
      case 67: 
        switch (mMode)
        {
        default: 
          break;
        case 5: 
          bool2 = mLetters.onKeyDown(paramInt, paramKeyEvent);
          break;
        case 4: 
          bool2 = mDigits.onKeyDown(paramInt, paramKeyEvent);
          break;
        case 3: 
          if (mDigits.getText().length() == mLetters.getText().length())
          {
            mLetters.onKeyDown(paramInt, paramKeyEvent);
            setMode(1);
          }
          bool2 = mDigits.onKeyDown(paramInt, paramKeyEvent);
          break;
        case 2: 
          bool1 = mLetters.onKeyDown(paramInt, paramKeyEvent);
          bool2 = bool1;
          if (mLetters.getText().length() == mDigits.getText().length())
          {
            setMode(1);
            bool2 = bool1;
          }
          break;
        case 1: 
          bool2 = mDigits.onKeyDown(paramInt, paramKeyEvent) & mLetters.onKeyDown(paramInt, paramKeyEvent);
        }
        break;
      }
      break;
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
      bool2 = bool1;
    }
    if (!bool2) {
      return super.onKeyDown(paramInt, paramKeyEvent);
    }
    return true;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool1 = mLetters.onKeyUp(paramInt, paramKeyEvent);
    boolean bool2 = mDigits.onKeyUp(paramInt, paramKeyEvent);
    if ((!bool1) && (!bool2)) {
      bool1 = false;
    } else {
      bool1 = true;
    }
    return bool1;
  }
  
  protected void onModeChange(int paramInt1, int paramInt2) {}
  
  public void removeFilterWatcher(TextWatcher paramTextWatcher)
  {
    Editable localEditable;
    if (mMode != 4) {
      localEditable = mLetters.getText();
    } else {
      localEditable = mDigits.getText();
    }
    localEditable.removeSpan(paramTextWatcher);
  }
  
  public void setDigitsWatcher(TextWatcher paramTextWatcher)
  {
    Editable localEditable = mDigits.getText();
    ((Spannable)localEditable).setSpan(paramTextWatcher, 0, localEditable.length(), 18);
  }
  
  public void setFilterWatcher(TextWatcher paramTextWatcher)
  {
    if (mMode != 4) {
      setLettersWatcher(paramTextWatcher);
    } else {
      setDigitsWatcher(paramTextWatcher);
    }
  }
  
  public void setLettersWatcher(TextWatcher paramTextWatcher)
  {
    Editable localEditable = mLetters.getText();
    ((Spannable)localEditable).setSpan(paramTextWatcher, 0, localEditable.length(), 18);
  }
  
  public void setMode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 5: 
      makeLettersPrimary();
      mLetters.setVisibility(0);
      mDigits.setVisibility(8);
      break;
    case 4: 
      makeDigitsPrimary();
      mLetters.setVisibility(8);
      mDigits.setVisibility(0);
      break;
    case 3: 
      makeDigitsPrimary();
      mLetters.setVisibility(4);
      mDigits.setVisibility(0);
      break;
    case 2: 
      makeLettersPrimary();
      mLetters.setVisibility(0);
      mDigits.setVisibility(4);
      break;
    case 1: 
      makeDigitsPrimary();
      mLetters.setVisibility(0);
      mDigits.setVisibility(0);
    }
    int i = mMode;
    mMode = paramInt;
    onModeChange(i, paramInt);
  }
}
