package android.text.method;

import android.text.Spannable;
import android.view.KeyCharacterMap.KeyData;
import android.view.KeyEvent;

public class DialerKeyListener
  extends NumberKeyListener
{
  public static final char[] CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 35, 42, 43, 45, 40, 41, 44, 47, 78, 46, 32, 59 };
  private static DialerKeyListener sInstance;
  
  public DialerKeyListener() {}
  
  public static DialerKeyListener getInstance()
  {
    if (sInstance != null) {
      return sInstance;
    }
    sInstance = new DialerKeyListener();
    return sInstance;
  }
  
  protected char[] getAcceptedChars()
  {
    return CHARACTERS;
  }
  
  public int getInputType()
  {
    return 3;
  }
  
  protected int lookup(KeyEvent paramKeyEvent, Spannable paramSpannable)
  {
    int i = getMetaState(paramSpannable, paramKeyEvent);
    int j = paramKeyEvent.getNumber();
    if (((i & 0x3) == 0) && (j != 0)) {
      return j;
    }
    int k = super.lookup(paramKeyEvent, paramSpannable);
    if (k != 0) {
      return k;
    }
    if (i != 0)
    {
      KeyCharacterMap.KeyData localKeyData = new KeyCharacterMap.KeyData();
      paramSpannable = getAcceptedChars();
      if (paramKeyEvent.getKeyData(localKeyData)) {
        for (k = 1; k < meta.length; k++) {
          if (ok(paramSpannable, meta[k])) {
            return meta[k];
          }
        }
      }
    }
    return j;
  }
}
