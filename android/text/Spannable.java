package android.text;

public abstract interface Spannable
  extends Spanned
{
  public abstract void removeSpan(Object paramObject);
  
  public void removeSpan(Object paramObject, int paramInt)
  {
    removeSpan(paramObject);
  }
  
  public abstract void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3);
  
  public static class Factory
  {
    private static Factory sInstance = new Factory();
    
    public Factory() {}
    
    public static Factory getInstance()
    {
      return sInstance;
    }
    
    public Spannable newSpannable(CharSequence paramCharSequence)
    {
      return new SpannableString(paramCharSequence);
    }
  }
}
