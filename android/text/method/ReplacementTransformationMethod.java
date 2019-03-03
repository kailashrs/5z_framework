package android.text.method;

import android.graphics.Rect;
import android.text.Editable;
import android.text.GetChars;
import android.text.Spannable;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;

public abstract class ReplacementTransformationMethod
  implements TransformationMethod
{
  public ReplacementTransformationMethod() {}
  
  protected abstract char[] getOriginal();
  
  protected abstract char[] getReplacement();
  
  public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
  {
    char[] arrayOfChar = getOriginal();
    paramView = getReplacement();
    if (!(paramCharSequence instanceof Editable))
    {
      int i = 1;
      int j = arrayOfChar.length;
      int m;
      for (int k = 0;; k++)
      {
        m = i;
        if (k >= j) {
          break;
        }
        if (TextUtils.indexOf(paramCharSequence, arrayOfChar[k]) >= 0)
        {
          m = 0;
          break;
        }
      }
      if (m != 0) {
        return paramCharSequence;
      }
      if (!(paramCharSequence instanceof Spannable))
      {
        if ((paramCharSequence instanceof Spanned)) {
          return new SpannedString(new SpannedReplacementCharSequence((Spanned)paramCharSequence, arrayOfChar, paramView));
        }
        return new ReplacementCharSequence(paramCharSequence, arrayOfChar, paramView).toString();
      }
    }
    if ((paramCharSequence instanceof Spanned)) {
      return new SpannedReplacementCharSequence((Spanned)paramCharSequence, arrayOfChar, paramView);
    }
    return new ReplacementCharSequence(paramCharSequence, arrayOfChar, paramView);
  }
  
  public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect) {}
  
  private static class ReplacementCharSequence
    implements CharSequence, GetChars
  {
    private char[] mOriginal;
    private char[] mReplacement;
    private CharSequence mSource;
    
    public ReplacementCharSequence(CharSequence paramCharSequence, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
    {
      mSource = paramCharSequence;
      mOriginal = paramArrayOfChar1;
      mReplacement = paramArrayOfChar2;
    }
    
    public char charAt(int paramInt)
    {
      char c1 = mSource.charAt(paramInt);
      int i = mOriginal.length;
      paramInt = 0;
      for (char c2 = c1; paramInt < i; c2 = c1)
      {
        c1 = c2;
        if (c2 == mOriginal[paramInt]) {
          c1 = mReplacement[paramInt];
        }
        paramInt++;
      }
      return c2;
    }
    
    public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
    {
      TextUtils.getChars(mSource, paramInt1, paramInt2, paramArrayOfChar, paramInt3);
      int i = mOriginal.length;
      for (int j = paramInt3; j < paramInt2 - paramInt1 + paramInt3; j++)
      {
        int k = paramArrayOfChar[j];
        for (int m = 0; m < i; m++) {
          if (k == mOriginal[m]) {
            paramArrayOfChar[j] = ((char)mReplacement[m]);
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
      char[] arrayOfChar = new char[length()];
      getChars(0, length(), arrayOfChar, 0);
      return new String(arrayOfChar);
    }
  }
  
  private static class SpannedReplacementCharSequence
    extends ReplacementTransformationMethod.ReplacementCharSequence
    implements Spanned
  {
    private Spanned mSpanned;
    
    public SpannedReplacementCharSequence(Spanned paramSpanned, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
    {
      super(paramArrayOfChar1, paramArrayOfChar2);
      mSpanned = paramSpanned;
    }
    
    public int getSpanEnd(Object paramObject)
    {
      return mSpanned.getSpanEnd(paramObject);
    }
    
    public int getSpanFlags(Object paramObject)
    {
      return mSpanned.getSpanFlags(paramObject);
    }
    
    public int getSpanStart(Object paramObject)
    {
      return mSpanned.getSpanStart(paramObject);
    }
    
    public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass)
    {
      return mSpanned.getSpans(paramInt1, paramInt2, paramClass);
    }
    
    public int nextSpanTransition(int paramInt1, int paramInt2, Class paramClass)
    {
      return mSpanned.nextSpanTransition(paramInt1, paramInt2, paramClass);
    }
    
    public CharSequence subSequence(int paramInt1, int paramInt2)
    {
      return new SpannedString(this).subSequence(paramInt1, paramInt2);
    }
  }
}
