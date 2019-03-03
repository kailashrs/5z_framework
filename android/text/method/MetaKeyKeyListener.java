package android.text.method;

import android.text.Editable;
import android.text.NoCopySpan.Concrete;
import android.text.Spannable;
import android.text.Spanned;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;

public abstract class MetaKeyKeyListener
{
  private static final Object ALT;
  private static final Object CAP = new NoCopySpan.Concrete();
  private static final int LOCKED = 67108881;
  private static final int LOCKED_RETURN_VALUE = 2;
  public static final int META_ALT_LOCKED = 512;
  private static final long META_ALT_MASK = 565157566611970L;
  public static final int META_ALT_ON = 2;
  private static final long META_ALT_PRESSED = 2199023255552L;
  private static final long META_ALT_RELEASED = 562949953421312L;
  private static final long META_ALT_USED = 8589934592L;
  public static final int META_CAP_LOCKED = 256;
  private static final long META_CAP_PRESSED = 1099511627776L;
  private static final long META_CAP_RELEASED = 281474976710656L;
  private static final long META_CAP_USED = 4294967296L;
  public static final int META_SELECTING = 2048;
  private static final long META_SHIFT_MASK = 282578783305985L;
  public static final int META_SHIFT_ON = 1;
  public static final int META_SYM_LOCKED = 1024;
  private static final long META_SYM_MASK = 1130315133223940L;
  public static final int META_SYM_ON = 4;
  private static final long META_SYM_PRESSED = 4398046511104L;
  private static final long META_SYM_RELEASED = 1125899906842624L;
  private static final long META_SYM_USED = 17179869184L;
  private static final int PRESSED = 16777233;
  private static final int PRESSED_RETURN_VALUE = 1;
  private static final int RELEASED = 33554449;
  private static final Object SELECTING = new NoCopySpan.Concrete();
  private static final Object SYM;
  private static final int USED = 50331665;
  
  static
  {
    ALT = new NoCopySpan.Concrete();
    SYM = new NoCopySpan.Concrete();
  }
  
  public MetaKeyKeyListener() {}
  
  private static void adjust(Spannable paramSpannable, Object paramObject)
  {
    int i = paramSpannable.getSpanFlags(paramObject);
    if (i == 16777233) {
      paramSpannable.setSpan(paramObject, 0, 0, 50331665);
    } else if (i == 33554449) {
      paramSpannable.removeSpan(paramObject);
    }
  }
  
  public static long adjustMetaAfterKeypress(long paramLong)
  {
    long l;
    if ((0x10000000000 & paramLong) != 0L)
    {
      l = paramLong & 0xFFFEFEFEFFFFFEFE | 1L | 0x100000000;
    }
    else
    {
      l = paramLong;
      if ((0x1000000000000 & paramLong) != 0L) {
        l = paramLong & 0xFFFEFEFEFFFFFEFE;
      }
    }
    if ((0x20000000000 & l) != 0L)
    {
      paramLong = l & 0xFFFDFDFDFFFFFDFD | 0x2 | 0x200000000;
    }
    else
    {
      paramLong = l;
      if ((0x2000000000000 & l) != 0L) {
        paramLong = l & 0xFFFDFDFDFFFFFDFD;
      }
    }
    if ((0x40000000000 & paramLong) != 0L)
    {
      l = paramLong & 0xFFFBFBFBFFFFFBFB | 0x4 | 0x400000000;
    }
    else
    {
      l = paramLong;
      if ((0x4000000000000 & paramLong) != 0L) {
        l = paramLong & 0xFFFBFBFBFFFFFBFB;
      }
    }
    return l;
  }
  
  public static void adjustMetaAfterKeypress(Spannable paramSpannable)
  {
    adjust(paramSpannable, CAP);
    adjust(paramSpannable, ALT);
    adjust(paramSpannable, SYM);
  }
  
  public static void clearMetaKeyState(Editable paramEditable, int paramInt)
  {
    if ((paramInt & 0x1) != 0) {
      paramEditable.removeSpan(CAP);
    }
    if ((paramInt & 0x2) != 0) {
      paramEditable.removeSpan(ALT);
    }
    if ((paramInt & 0x4) != 0) {
      paramEditable.removeSpan(SYM);
    }
    if ((paramInt & 0x800) != 0) {
      paramEditable.removeSpan(SELECTING);
    }
  }
  
  private static int getActive(CharSequence paramCharSequence, Object paramObject, int paramInt1, int paramInt2)
  {
    if (!(paramCharSequence instanceof Spanned)) {
      return 0;
    }
    int i = ((Spanned)paramCharSequence).getSpanFlags(paramObject);
    if (i == 67108881) {
      return paramInt2;
    }
    if (i != 0) {
      return paramInt1;
    }
    return 0;
  }
  
  public static final int getMetaState(long paramLong)
  {
    int i = 0;
    if ((0x100 & paramLong) != 0L) {
      i = 0x0 | 0x100;
    } else if ((1L & paramLong) != 0L) {
      i = 0x0 | 0x1;
    }
    int j;
    if ((0x200 & paramLong) != 0L)
    {
      j = i | 0x200;
    }
    else
    {
      j = i;
      if ((0x2 & paramLong) != 0L) {
        j = i | 0x2;
      }
    }
    if ((0x400 & paramLong) != 0L)
    {
      i = j | 0x400;
    }
    else
    {
      i = j;
      if ((0x4 & paramLong) != 0L) {
        i = j | 0x4;
      }
    }
    return i;
  }
  
  public static final int getMetaState(long paramLong, int paramInt)
  {
    if (paramInt != 4)
    {
      switch (paramInt)
      {
      default: 
        return 0;
      case 2: 
        if ((0x200 & paramLong) != 0L) {
          return 2;
        }
        if ((0x2 & paramLong) != 0L) {
          return 1;
        }
        return 0;
      }
      if ((0x100 & paramLong) != 0L) {
        return 2;
      }
      if ((1L & paramLong) != 0L) {
        return 1;
      }
      return 0;
    }
    if ((0x400 & paramLong) != 0L) {
      return 2;
    }
    if ((0x4 & paramLong) != 0L) {
      return 1;
    }
    return 0;
  }
  
  public static final int getMetaState(CharSequence paramCharSequence)
  {
    return getActive(paramCharSequence, CAP, 1, 256) | getActive(paramCharSequence, ALT, 2, 512) | getActive(paramCharSequence, SYM, 4, 1024) | getActive(paramCharSequence, SELECTING, 2048, 2048);
  }
  
  public static final int getMetaState(CharSequence paramCharSequence, int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 2048)
      {
        switch (paramInt)
        {
        default: 
          return 0;
        case 2: 
          return getActive(paramCharSequence, ALT, 1, 2);
        }
        return getActive(paramCharSequence, CAP, 1, 2);
      }
      return getActive(paramCharSequence, SELECTING, 1, 2);
    }
    return getActive(paramCharSequence, SYM, 1, 2);
  }
  
  public static final int getMetaState(CharSequence paramCharSequence, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getMetaState();
    int j = i;
    if (paramKeyEvent.getKeyCharacterMap().getModifierBehavior() == 1) {
      j = i | getMetaState(paramCharSequence);
    }
    if (2048 == paramInt)
    {
      if ((j & 0x800) != 0) {
        return 1;
      }
      return 0;
    }
    return getMetaState(j, paramInt);
  }
  
  public static final int getMetaState(CharSequence paramCharSequence, KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getMetaState();
    int j = i;
    if (paramKeyEvent.getKeyCharacterMap().getModifierBehavior() == 1) {
      j = i | getMetaState(paramCharSequence);
    }
    return j;
  }
  
  public static long handleKeyDown(long paramLong, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 59) && (paramInt != 60))
    {
      if ((paramInt != 57) && (paramInt != 58) && (paramInt != 78))
      {
        if (paramInt == 63) {
          return press(paramLong, 4, 1130315133223940L, 1024L, 4398046511104L, 1125899906842624L, 17179869184L);
        }
        return paramLong;
      }
      return press(paramLong, 2, 565157566611970L, 512L, 2199023255552L, 562949953421312L, 8589934592L);
    }
    return press(paramLong, 1, 282578783305985L, 256L, 1099511627776L, 281474976710656L, 4294967296L);
  }
  
  public static long handleKeyUp(long paramLong, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 59) && (paramInt != 60))
    {
      if ((paramInt != 57) && (paramInt != 58) && (paramInt != 78))
      {
        if (paramInt == 63) {
          return release(paramLong, 4, 1130315133223940L, 4398046511104L, 1125899906842624L, 17179869184L, paramKeyEvent);
        }
        return paramLong;
      }
      return release(paramLong, 2, 565157566611970L, 2199023255552L, 562949953421312L, 8589934592L, paramKeyEvent);
    }
    return release(paramLong, 1, 282578783305985L, 1099511627776L, 281474976710656L, 4294967296L, paramKeyEvent);
  }
  
  public static boolean isMetaTracker(CharSequence paramCharSequence, Object paramObject)
  {
    boolean bool;
    if ((paramObject != CAP) && (paramObject != ALT) && (paramObject != SYM) && (paramObject != SELECTING)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isSelectingMetaTracker(CharSequence paramCharSequence, Object paramObject)
  {
    boolean bool;
    if (paramObject == SELECTING) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static long press(long paramLong1, int paramInt, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    if ((paramLong1 & paramLong4) == 0L) {
      if ((paramLong1 & paramLong5) != 0L) {
        paramLong1 = paramLong2 & paramLong1 | paramInt | paramLong3;
      }
    }
    for (;;)
    {
      break;
      if ((paramLong1 & paramLong6) != 0L) {
        break;
      }
      if ((paramLong1 & paramLong3) != 0L) {
        paramLong1 &= paramLong2;
      } else {
        paramLong1 |= paramInt | paramLong4;
      }
    }
    return paramLong1;
  }
  
  private void press(Editable paramEditable, Object paramObject)
  {
    int i = paramEditable.getSpanFlags(paramObject);
    if (i != 16777233) {
      if (i == 33554449) {
        paramEditable.setSpan(paramObject, 0, 0, 67108881);
      } else if (i != 50331665) {
        if (i == 67108881) {
          paramEditable.removeSpan(paramObject);
        } else {
          paramEditable.setSpan(paramObject, 0, 0, 16777233);
        }
      }
    }
  }
  
  private static long release(long paramLong1, int paramInt, long paramLong2, long paramLong3, long paramLong4, long paramLong5, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCharacterMap().getModifierBehavior() != 1)
    {
      paramLong2 = paramLong1 & paramLong2;
    }
    else if ((paramLong1 & paramLong5) != 0L)
    {
      paramLong2 = paramLong1 & paramLong2;
    }
    else
    {
      paramLong2 = paramLong1;
      if ((paramLong1 & paramLong3) != 0L) {
        paramLong2 = paramLong1 | paramInt | paramLong4;
      }
    }
    return paramLong2;
  }
  
  private void release(Editable paramEditable, Object paramObject, KeyEvent paramKeyEvent)
  {
    int i = paramEditable.getSpanFlags(paramObject);
    if (paramKeyEvent.getKeyCharacterMap().getModifierBehavior() != 1) {
      paramEditable.removeSpan(paramObject);
    } else if (i == 50331665) {
      paramEditable.removeSpan(paramObject);
    } else if (i == 16777233) {
      paramEditable.setSpan(paramObject, 0, 0, 33554449);
    }
  }
  
  private static void resetLock(Spannable paramSpannable, Object paramObject)
  {
    if (paramSpannable.getSpanFlags(paramObject) == 67108881) {
      paramSpannable.removeSpan(paramObject);
    }
  }
  
  public static long resetLockedMeta(long paramLong)
  {
    long l = paramLong;
    if ((0x100 & paramLong) != 0L) {
      l = paramLong & 0xFFFEFEFEFFFFFEFE;
    }
    paramLong = l;
    if ((0x200 & l) != 0L) {
      paramLong = l & 0xFFFDFDFDFFFFFDFD;
    }
    l = paramLong;
    if ((0x400 & paramLong) != 0L) {
      l = paramLong & 0xFFFBFBFBFFFFFBFB;
    }
    return l;
  }
  
  protected static void resetLockedMeta(Spannable paramSpannable)
  {
    resetLock(paramSpannable, CAP);
    resetLock(paramSpannable, ALT);
    resetLock(paramSpannable, SYM);
    resetLock(paramSpannable, SELECTING);
  }
  
  public static void resetMetaState(Spannable paramSpannable)
  {
    paramSpannable.removeSpan(CAP);
    paramSpannable.removeSpan(ALT);
    paramSpannable.removeSpan(SYM);
    paramSpannable.removeSpan(SELECTING);
  }
  
  public static void startSelecting(View paramView, Spannable paramSpannable)
  {
    paramSpannable.setSpan(SELECTING, 0, 0, 16777233);
  }
  
  public static void stopSelecting(View paramView, Spannable paramSpannable)
  {
    paramSpannable.removeSpan(SELECTING);
  }
  
  public long clearMetaKeyState(long paramLong, int paramInt)
  {
    long l = paramLong;
    if ((paramInt & 0x1) != 0)
    {
      l = paramLong;
      if ((0x100 & paramLong) != 0L) {
        l = paramLong & 0xFFFEFEFEFFFFFEFE;
      }
    }
    paramLong = l;
    if ((paramInt & 0x2) != 0)
    {
      paramLong = l;
      if ((0x200 & l) != 0L) {
        paramLong = l & 0xFFFDFDFDFFFFFDFD;
      }
    }
    l = paramLong;
    if ((paramInt & 0x4) != 0)
    {
      l = paramLong;
      if ((0x400 & paramLong) != 0L) {
        l = paramLong & 0xFFFBFBFBFFFFFBFB;
      }
    }
    return l;
  }
  
  public void clearMetaKeyState(View paramView, Editable paramEditable, int paramInt)
  {
    clearMetaKeyState(paramEditable, paramInt);
  }
  
  public boolean onKeyDown(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 59) && (paramInt != 60))
    {
      if ((paramInt != 57) && (paramInt != 58) && (paramInt != 78))
      {
        if (paramInt == 63)
        {
          press(paramEditable, SYM);
          return true;
        }
        return false;
      }
      press(paramEditable, ALT);
      return true;
    }
    press(paramEditable, CAP);
    return true;
  }
  
  public boolean onKeyUp(View paramView, Editable paramEditable, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 59) && (paramInt != 60))
    {
      if ((paramInt != 57) && (paramInt != 58) && (paramInt != 78))
      {
        if (paramInt == 63)
        {
          release(paramEditable, SYM, paramKeyEvent);
          return true;
        }
        return false;
      }
      release(paramEditable, ALT, paramKeyEvent);
      return true;
    }
    release(paramEditable, CAP, paramKeyEvent);
    return true;
  }
}
