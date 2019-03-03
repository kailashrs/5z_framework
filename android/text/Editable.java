package android.text;

public abstract interface Editable
  extends CharSequence, GetChars, Spannable, Appendable
{
  public abstract Editable append(char paramChar);
  
  public abstract Editable append(CharSequence paramCharSequence);
  
  public abstract Editable append(CharSequence paramCharSequence, int paramInt1, int paramInt2);
  
  public abstract void clear();
  
  public abstract void clearSpans();
  
  public abstract Editable delete(int paramInt1, int paramInt2);
  
  public abstract InputFilter[] getFilters();
  
  public abstract Editable insert(int paramInt, CharSequence paramCharSequence);
  
  public abstract Editable insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3);
  
  public abstract Editable replace(int paramInt1, int paramInt2, CharSequence paramCharSequence);
  
  public abstract Editable replace(int paramInt1, int paramInt2, CharSequence paramCharSequence, int paramInt3, int paramInt4);
  
  public abstract void setFilters(InputFilter[] paramArrayOfInputFilter);
  
  public static class Factory
  {
    private static Factory sInstance = new Factory();
    
    public Factory() {}
    
    public static Factory getInstance()
    {
      return sInstance;
    }
    
    public Editable newEditable(CharSequence paramCharSequence)
    {
      return new SpannableStringBuilder(paramCharSequence);
    }
  }
}
