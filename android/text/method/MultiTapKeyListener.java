package android.text.method;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

public class MultiTapKeyListener
  extends BaseKeyListener
  implements SpanWatcher
{
  private static MultiTapKeyListener[] sInstance = new MultiTapKeyListener[TextKeyListener.Capitalize.values().length * 2];
  private static final SparseArray<String> sRecs = new SparseArray();
  private boolean mAutoText;
  private TextKeyListener.Capitalize mCapitalize;
  
  static
  {
    sRecs.put(8, ".,1!@#$%^&*:/?'=()");
    sRecs.put(9, "abc2ABC");
    sRecs.put(10, "def3DEF");
    sRecs.put(11, "ghi4GHI");
    sRecs.put(12, "jkl5JKL");
    sRecs.put(13, "mno6MNO");
    sRecs.put(14, "pqrs7PQRS");
    sRecs.put(15, "tuv8TUV");
    sRecs.put(16, "wxyz9WXYZ");
    sRecs.put(7, "0+");
    sRecs.put(18, " ");
  }
  
  public MultiTapKeyListener(TextKeyListener.Capitalize paramCapitalize, boolean paramBoolean)
  {
    mCapitalize = paramCapitalize;
    mAutoText = paramBoolean;
  }
  
  public static MultiTapKeyListener getInstance(boolean paramBoolean, TextKeyListener.Capitalize paramCapitalize)
  {
    int i = paramCapitalize.ordinal() * 2 + paramBoolean;
    if (sInstance[i] == null) {
      sInstance[i] = new MultiTapKeyListener(paramCapitalize, paramBoolean);
    }
    return sInstance[i];
  }
  
  private static void removeTimeouts(Spannable paramSpannable)
  {
    int i = paramSpannable.length();
    int j = 0;
    Timeout[] arrayOfTimeout = (Timeout[])paramSpannable.getSpans(0, i, Timeout.class);
    while (j < arrayOfTimeout.length)
    {
      Timeout localTimeout = arrayOfTimeout[j];
      localTimeout.removeCallbacks(localTimeout);
      Timeout.access$002(localTimeout, null);
      paramSpannable.removeSpan(localTimeout);
      j++;
    }
  }
  
  public int getInputType()
  {
    return makeTextContentType(mCapitalize, mAutoText);
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
    int n = Math.max(j, k);
    k = paramEditable.getSpanStart(TextKeyListener.ACTIVE);
    j = paramEditable.getSpanEnd(TextKeyListener.ACTIVE);
    int i1 = (paramEditable.getSpanFlags(TextKeyListener.ACTIVE) & 0xFF000000) >>> 24;
    if ((k == m) && (j == n) && (n - m == 1) && (i1 >= 0) && (i1 < sRecs.size()))
    {
      if (paramInt == 17)
      {
        char c = paramEditable.charAt(m);
        if (Character.isLowerCase(c))
        {
          paramEditable.replace(m, n, String.valueOf(c).toUpperCase());
          removeTimeouts(paramEditable);
          new Timeout(paramEditable);
          return true;
        }
        if (Character.isUpperCase(c))
        {
          paramEditable.replace(m, n, String.valueOf(c).toLowerCase());
          removeTimeouts(paramEditable);
          new Timeout(paramEditable);
          return true;
        }
      }
      if (sRecs.indexOfKey(paramInt) == i1)
      {
        String str = (String)sRecs.valueAt(i1);
        k = str.indexOf(paramEditable.charAt(m));
        if (k >= 0)
        {
          paramInt = (k + 1) % str.length();
          paramEditable.replace(m, n, str, paramInt, paramInt + 1);
          removeTimeouts(paramEditable);
          new Timeout(paramEditable);
          return true;
        }
      }
      j = sRecs.indexOfKey(paramInt);
      k = j;
      if (j >= 0)
      {
        Selection.setSelection(paramEditable, n, n);
        k = j;
        j = n;
        break label368;
      }
    }
    else
    {
      k = sRecs.indexOfKey(paramInt);
    }
    j = m;
    label368:
    if (k >= 0)
    {
      paramView = (String)sRecs.valueAt(k);
      i1 = 0;
      m = i1;
      if ((i & 0x1) != 0)
      {
        m = i1;
        if (TextKeyListener.shouldCap(mCapitalize, paramEditable, j)) {
          for (paramInt = 0;; paramInt++)
          {
            m = i1;
            if (paramInt >= paramView.length()) {
              break;
            }
            if (Character.isUpperCase(paramView.charAt(paramInt)))
            {
              m = paramInt;
              break;
            }
          }
        }
      }
      if (j != n) {
        Selection.setSelection(paramEditable, n);
      }
      paramEditable.setSpan(OLD_SEL_START, j, j, 17);
      paramEditable.replace(j, n, paramView, m, m + 1);
      i = paramEditable.getSpanStart(OLD_SEL_START);
      paramInt = Selection.getSelectionEnd(paramEditable);
      if (paramInt != i)
      {
        Selection.setSelection(paramEditable, i, paramInt);
        paramEditable.setSpan(TextKeyListener.LAST_TYPED, i, paramInt, 33);
        paramEditable.setSpan(TextKeyListener.ACTIVE, i, paramInt, 0x21 | k << 24);
      }
      removeTimeouts(paramEditable);
      new Timeout(paramEditable);
      if (paramEditable.getSpanStart(this) < 0)
      {
        paramView = (KeyListener[])paramEditable.getSpans(0, paramEditable.length(), KeyListener.class);
        k = paramView.length;
        for (paramInt = 0; paramInt < k; paramInt++) {
          paramEditable.removeSpan(paramView[paramInt]);
        }
        paramEditable.setSpan(this, 0, paramEditable.length(), 18);
      }
      return true;
    }
    return super.onKeyDown(paramView, paramEditable, paramInt, paramKeyEvent);
  }
  
  public void onSpanAdded(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2) {}
  
  public void onSpanChanged(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramObject == Selection.SELECTION_END)
    {
      paramSpannable.removeSpan(TextKeyListener.ACTIVE);
      removeTimeouts(paramSpannable);
    }
  }
  
  public void onSpanRemoved(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2) {}
  
  private class Timeout
    extends Handler
    implements Runnable
  {
    private Editable mBuffer;
    
    public Timeout(Editable paramEditable)
    {
      mBuffer = paramEditable;
      mBuffer.setSpan(this, 0, mBuffer.length(), 18);
      postAtTime(this, SystemClock.uptimeMillis() + 2000L);
    }
    
    public void run()
    {
      Editable localEditable = mBuffer;
      if (localEditable != null)
      {
        int i = Selection.getSelectionStart(localEditable);
        int j = Selection.getSelectionEnd(localEditable);
        int k = localEditable.getSpanStart(TextKeyListener.ACTIVE);
        int m = localEditable.getSpanEnd(TextKeyListener.ACTIVE);
        if ((i == k) && (j == m)) {
          Selection.setSelection(localEditable, Selection.getSelectionEnd(localEditable));
        }
        localEditable.removeSpan(this);
      }
    }
  }
}
