package android.text;

public abstract interface TextDirectionHeuristic
{
  public abstract boolean isRtl(CharSequence paramCharSequence, int paramInt1, int paramInt2);
  
  public abstract boolean isRtl(char[] paramArrayOfChar, int paramInt1, int paramInt2);
}
