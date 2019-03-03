package android.text.method;

import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.GetChars;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UpdateLayout;
import android.view.View;
import java.lang.ref.WeakReference;

public class PasswordTransformationMethod
  implements TransformationMethod, TextWatcher
{
  private static char DOT = (char)'•';
  private static PasswordTransformationMethod sInstance;
  
  public PasswordTransformationMethod() {}
  
  public static PasswordTransformationMethod getInstance()
  {
    if (sInstance != null) {
      return sInstance;
    }
    sInstance = new PasswordTransformationMethod();
    return sInstance;
  }
  
  private static void removeVisibleSpans(Spannable paramSpannable)
  {
    int i = paramSpannable.length();
    int j = 0;
    Visible[] arrayOfVisible = (Visible[])paramSpannable.getSpans(0, i, Visible.class);
    while (j < arrayOfVisible.length)
    {
      paramSpannable.removeSpan(arrayOfVisible[j]);
      j++;
    }
  }
  
  public void afterTextChanged(Editable paramEditable) {}
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
  {
    if ((paramCharSequence instanceof Spannable))
    {
      Spannable localSpannable = (Spannable)paramCharSequence;
      ViewReference[] arrayOfViewReference = (ViewReference[])localSpannable.getSpans(0, localSpannable.length(), ViewReference.class);
      for (int i = 0; i < arrayOfViewReference.length; i++) {
        localSpannable.removeSpan(arrayOfViewReference[i]);
      }
      removeVisibleSpans(localSpannable);
      localSpannable.setSpan(new ViewReference(paramView), 0, 0, 34);
    }
    return new PasswordCharSequence(paramCharSequence);
  }
  
  public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if ((!paramBoolean) && ((paramCharSequence instanceof Spannable))) {
      removeVisibleSpans((Spannable)paramCharSequence);
    }
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramCharSequence instanceof Spannable))
    {
      Spannable localSpannable = (Spannable)paramCharSequence;
      int i = paramCharSequence.length();
      paramInt2 = 0;
      ViewReference[] arrayOfViewReference = (ViewReference[])localSpannable.getSpans(0, i, ViewReference.class);
      if (arrayOfViewReference.length == 0) {
        return;
      }
      paramCharSequence = null;
      while ((paramCharSequence == null) && (paramInt2 < arrayOfViewReference.length))
      {
        paramCharSequence = (View)arrayOfViewReference[paramInt2].get();
        paramInt2++;
      }
      if (paramCharSequence == null) {
        return;
      }
      if (((TextKeyListener.getInstance().getPrefs(paramCharSequence.getContext()) & 0x8) != 0) && (paramInt3 > 0))
      {
        removeVisibleSpans(localSpannable);
        if (paramInt3 == 1) {
          localSpannable.setSpan(new Visible(localSpannable, this), paramInt1, paramInt1 + paramInt3, 33);
        }
      }
    }
  }
  
  private static class PasswordCharSequence
    implements CharSequence, GetChars
  {
    private CharSequence mSource;
    
    public PasswordCharSequence(CharSequence paramCharSequence)
    {
      mSource = paramCharSequence;
    }
    
    public char charAt(int paramInt)
    {
      if ((mSource instanceof Spanned))
      {
        Spanned localSpanned = (Spanned)mSource;
        int i = localSpanned.getSpanStart(TextKeyListener.ACTIVE);
        int j = localSpanned.getSpanEnd(TextKeyListener.ACTIVE);
        if ((paramInt >= i) && (paramInt < j)) {
          return mSource.charAt(paramInt);
        }
        j = localSpanned.length();
        i = 0;
        PasswordTransformationMethod.Visible[] arrayOfVisible = (PasswordTransformationMethod.Visible[])localSpanned.getSpans(0, j, PasswordTransformationMethod.Visible.class);
        while (i < arrayOfVisible.length)
        {
          if (localSpanned.getSpanStart(PasswordTransformationMethod.Visible.access$000(arrayOfVisible[i])) >= 0)
          {
            j = localSpanned.getSpanStart(arrayOfVisible[i]);
            int k = localSpanned.getSpanEnd(arrayOfVisible[i]);
            if ((paramInt >= j) && (paramInt < k)) {
              return mSource.charAt(paramInt);
            }
          }
          i++;
        }
      }
      return PasswordTransformationMethod.DOT;
    }
    
    public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
    {
      TextUtils.getChars(mSource, paramInt1, paramInt2, paramArrayOfChar, paramInt3);
      int i = -1;
      int j = -1;
      int k = 0;
      Object localObject1 = null;
      Object localObject2 = null;
      int m;
      int n;
      int i1;
      if ((mSource instanceof Spanned))
      {
        Spanned localSpanned = (Spanned)mSource;
        m = localSpanned.getSpanStart(TextKeyListener.ACTIVE);
        n = localSpanned.getSpanEnd(TextKeyListener.ACTIVE);
        PasswordTransformationMethod.Visible[] arrayOfVisible = (PasswordTransformationMethod.Visible[])localSpanned.getSpans(0, localSpanned.length(), PasswordTransformationMethod.Visible.class);
        i1 = arrayOfVisible.length;
        int[] arrayOfInt1 = new int[i1];
        int[] arrayOfInt2 = new int[i1];
        for (i2 = 0;; i2++)
        {
          i = m;
          j = n;
          k = i1;
          localObject1 = arrayOfInt1;
          localObject2 = arrayOfInt2;
          if (i2 >= i1) {
            break;
          }
          if (localSpanned.getSpanStart(PasswordTransformationMethod.Visible.access$000(arrayOfVisible[i2])) >= 0)
          {
            arrayOfInt1[i2] = localSpanned.getSpanStart(arrayOfVisible[i2]);
            arrayOfInt2[i2] = localSpanned.getSpanEnd(arrayOfVisible[i2]);
          }
        }
      }
      for (int i2 = paramInt1; i2 < paramInt2; i2++) {
        if ((i2 < i) || (i2 >= j))
        {
          m = 0;
          for (i1 = 0;; i1++)
          {
            n = m;
            if (i1 >= k) {
              break;
            }
            if ((i2 >= localObject1[i1]) && (i2 < localObject2[i1]))
            {
              n = 1;
              break;
            }
          }
          if (n == 0) {
            paramArrayOfChar[(i2 - paramInt1 + paramInt3)] = PasswordTransformationMethod.DOT;
          }
        }
      }
    }
    
    public int length()
    {
      return mSource.length();
    }
    
    public CharSequence subSequence(int paramInt1, int paramInt2)
    {
      char[] arrayOfChar = new char[paramInt2 - paramInt1];
      getChars(paramInt1, paramInt2, arrayOfChar, 0);
      return new String(arrayOfChar);
    }
    
    public String toString()
    {
      return subSequence(0, length()).toString();
    }
  }
  
  private static class ViewReference
    extends WeakReference<View>
    implements NoCopySpan
  {
    public ViewReference(View paramView)
    {
      super();
    }
  }
  
  private static class Visible
    extends Handler
    implements UpdateLayout, Runnable
  {
    private Spannable mText;
    private PasswordTransformationMethod mTransformer;
    
    public Visible(Spannable paramSpannable, PasswordTransformationMethod paramPasswordTransformationMethod)
    {
      mText = paramSpannable;
      mTransformer = paramPasswordTransformationMethod;
      postAtTime(this, SystemClock.uptimeMillis() + 1500L);
    }
    
    public void run()
    {
      mText.removeSpan(this);
    }
  }
}
