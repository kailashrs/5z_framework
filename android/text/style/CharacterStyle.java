package android.text.style;

import android.text.TextPaint;

public abstract class CharacterStyle
{
  public CharacterStyle() {}
  
  public static CharacterStyle wrap(CharacterStyle paramCharacterStyle)
  {
    if ((paramCharacterStyle instanceof MetricAffectingSpan)) {
      return new MetricAffectingSpan.Passthrough((MetricAffectingSpan)paramCharacterStyle);
    }
    return new Passthrough(paramCharacterStyle);
  }
  
  public CharacterStyle getUnderlying()
  {
    return this;
  }
  
  public abstract void updateDrawState(TextPaint paramTextPaint);
  
  private static class Passthrough
    extends CharacterStyle
  {
    private CharacterStyle mStyle;
    
    public Passthrough(CharacterStyle paramCharacterStyle)
    {
      mStyle = paramCharacterStyle;
    }
    
    public CharacterStyle getUnderlying()
    {
      return mStyle.getUnderlying();
    }
    
    public void updateDrawState(TextPaint paramTextPaint)
    {
      mStyle.updateDrawState(paramTextPaint);
    }
  }
}
