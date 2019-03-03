package android.text;

public class AlteredCharSequence
  implements CharSequence, GetChars
{
  private char[] mChars;
  private int mEnd;
  private CharSequence mSource;
  private int mStart;
  
  private AlteredCharSequence(CharSequence paramCharSequence, char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    mSource = paramCharSequence;
    mChars = paramArrayOfChar;
    mStart = paramInt1;
    mEnd = paramInt2;
  }
  
  public static AlteredCharSequence make(CharSequence paramCharSequence, char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if ((paramCharSequence instanceof Spanned)) {
      return new AlteredSpanned(paramCharSequence, paramArrayOfChar, paramInt1, paramInt2, null);
    }
    return new AlteredCharSequence(paramCharSequence, paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public char charAt(int paramInt)
  {
    if ((paramInt >= mStart) && (paramInt < mEnd)) {
      return mChars[(paramInt - mStart)];
    }
    return mSource.charAt(paramInt);
  }
  
  public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
  {
    TextUtils.getChars(mSource, paramInt1, paramInt2, paramArrayOfChar, paramInt3);
    paramInt1 = Math.max(mStart, paramInt1);
    paramInt2 = Math.min(mEnd, paramInt2);
    if (paramInt1 > paramInt2) {
      System.arraycopy(mChars, paramInt1 - mStart, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
    }
  }
  
  public int length()
  {
    return mSource.length();
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2)
  {
    return make(mSource.subSequence(paramInt1, paramInt2), mChars, mStart - paramInt1, mEnd - paramInt1);
  }
  
  public String toString()
  {
    int i = length();
    char[] arrayOfChar = new char[i];
    getChars(0, i, arrayOfChar, 0);
    return String.valueOf(arrayOfChar);
  }
  
  void update(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    mChars = paramArrayOfChar;
    mStart = paramInt1;
    mEnd = paramInt2;
  }
  
  private static class AlteredSpanned
    extends AlteredCharSequence
    implements Spanned
  {
    private Spanned mSpanned;
    
    private AlteredSpanned(CharSequence paramCharSequence, char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      super(paramArrayOfChar, paramInt1, paramInt2, null);
      mSpanned = ((Spanned)paramCharSequence);
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
  }
}
