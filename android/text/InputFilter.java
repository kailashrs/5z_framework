package android.text;

import com.android.internal.util.Preconditions;
import java.util.Locale;

public abstract interface InputFilter
{
  public abstract CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4);
  
  public static class AllCaps
    implements InputFilter
  {
    private final Locale mLocale;
    
    public AllCaps()
    {
      mLocale = null;
    }
    
    public AllCaps(Locale paramLocale)
    {
      Preconditions.checkNotNull(paramLocale);
      mLocale = paramLocale;
    }
    
    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      paramSpanned = new CharSequenceWrapper(paramCharSequence, paramInt1, paramInt2);
      int i = 0;
      paramInt3 = 0;
      for (;;)
      {
        paramInt4 = i;
        if (paramInt3 >= paramInt2 - paramInt1) {
          break label74;
        }
        paramInt4 = Character.codePointAt(paramSpanned, paramInt3);
        if ((Character.isLowerCase(paramInt4)) || (Character.isTitleCase(paramInt4))) {
          break;
        }
        paramInt3 += Character.charCount(paramInt4);
      }
      paramInt4 = 1;
      label74:
      if (paramInt4 == 0) {
        return null;
      }
      boolean bool = paramCharSequence instanceof Spanned;
      paramCharSequence = TextUtils.toUpperCase(mLocale, paramSpanned, bool);
      if (paramCharSequence == paramSpanned) {
        return null;
      }
      if (bool) {
        paramCharSequence = new SpannableString(paramCharSequence);
      } else {
        paramCharSequence = paramCharSequence.toString();
      }
      return paramCharSequence;
    }
    
    private static class CharSequenceWrapper
      implements CharSequence, Spanned
    {
      private final int mEnd;
      private final int mLength;
      private final CharSequence mSource;
      private final int mStart;
      
      CharSequenceWrapper(CharSequence paramCharSequence, int paramInt1, int paramInt2)
      {
        mSource = paramCharSequence;
        mStart = paramInt1;
        mEnd = paramInt2;
        mLength = (paramInt2 - paramInt1);
      }
      
      public char charAt(int paramInt)
      {
        if ((paramInt >= 0) && (paramInt < mLength)) {
          return mSource.charAt(mStart + paramInt);
        }
        throw new IndexOutOfBoundsException();
      }
      
      public int getSpanEnd(Object paramObject)
      {
        return ((Spanned)mSource).getSpanEnd(paramObject) - mStart;
      }
      
      public int getSpanFlags(Object paramObject)
      {
        return ((Spanned)mSource).getSpanFlags(paramObject);
      }
      
      public int getSpanStart(Object paramObject)
      {
        return ((Spanned)mSource).getSpanStart(paramObject) - mStart;
      }
      
      public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass)
      {
        return ((Spanned)mSource).getSpans(mStart + paramInt1, mStart + paramInt2, paramClass);
      }
      
      public int length()
      {
        return mLength;
      }
      
      public int nextSpanTransition(int paramInt1, int paramInt2, Class paramClass)
      {
        return ((Spanned)mSource).nextSpanTransition(mStart + paramInt1, mStart + paramInt2, paramClass) - mStart;
      }
      
      public CharSequence subSequence(int paramInt1, int paramInt2)
      {
        if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt2 <= mLength) && (paramInt1 <= paramInt2)) {
          return new CharSequenceWrapper(mSource, mStart + paramInt1, mStart + paramInt2);
        }
        throw new IndexOutOfBoundsException();
      }
      
      public String toString()
      {
        return mSource.subSequence(mStart, mEnd).toString();
      }
    }
  }
  
  public static class LengthFilter
    implements InputFilter
  {
    private final int mMax;
    
    public LengthFilter(int paramInt)
    {
      mMax = paramInt;
    }
    
    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      paramInt3 = mMax - (paramSpanned.length() - (paramInt4 - paramInt3));
      if (paramInt3 <= 0) {
        return "";
      }
      if (paramInt3 >= paramInt2 - paramInt1) {
        return null;
      }
      paramInt3 += paramInt1;
      paramInt2 = paramInt3;
      if (Character.isHighSurrogate(paramCharSequence.charAt(paramInt3 - 1)))
      {
        paramInt3--;
        paramInt2 = paramInt3;
        if (paramInt3 == paramInt1) {
          return "";
        }
      }
      return paramCharSequence.subSequence(paramInt1, paramInt2);
    }
    
    public int getMax()
    {
      return mMax;
    }
  }
}
