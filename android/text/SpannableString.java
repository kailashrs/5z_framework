package android.text;

public class SpannableString
  extends SpannableStringInternal
  implements CharSequence, GetChars, Spannable
{
  public SpannableString(CharSequence paramCharSequence)
  {
    this(paramCharSequence, false);
  }
  
  private SpannableString(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    super(paramCharSequence, paramInt1, paramInt2, false);
  }
  
  public SpannableString(CharSequence paramCharSequence, boolean paramBoolean)
  {
    super(paramCharSequence, 0, paramCharSequence.length(), paramBoolean);
  }
  
  public static SpannableString valueOf(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof SpannableString)) {
      return (SpannableString)paramCharSequence;
    }
    return new SpannableString(paramCharSequence);
  }
  
  public void removeSpan(Object paramObject)
  {
    super.removeSpan(paramObject);
  }
  
  public void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    super.setSpan(paramObject, paramInt1, paramInt2, paramInt3);
  }
  
  public final CharSequence subSequence(int paramInt1, int paramInt2)
  {
    return new SpannableString(this, paramInt1, paramInt2);
  }
}
