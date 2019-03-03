package android.text;

public final class SpannedString
  extends SpannableStringInternal
  implements CharSequence, GetChars, Spanned
{
  public SpannedString(CharSequence paramCharSequence)
  {
    this(paramCharSequence, false);
  }
  
  private SpannedString(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    super(paramCharSequence, paramInt1, paramInt2, false);
  }
  
  public SpannedString(CharSequence paramCharSequence, boolean paramBoolean)
  {
    super(paramCharSequence, 0, paramCharSequence.length(), paramBoolean);
  }
  
  public static SpannedString valueOf(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof SpannedString)) {
      return (SpannedString)paramCharSequence;
    }
    return new SpannedString(paramCharSequence);
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2)
  {
    return new SpannedString(this, paramInt1, paramInt2);
  }
}
