package android.text.style;

import android.text.TextPaint;

public abstract class MetricAffectingSpan
  extends CharacterStyle
  implements UpdateLayout
{
  public MetricAffectingSpan() {}
  
  public MetricAffectingSpan getUnderlying()
  {
    return this;
  }
  
  public abstract void updateMeasureState(TextPaint paramTextPaint);
  
  static class Passthrough
    extends MetricAffectingSpan
  {
    private MetricAffectingSpan mStyle;
    
    Passthrough(MetricAffectingSpan paramMetricAffectingSpan)
    {
      mStyle = paramMetricAffectingSpan;
    }
    
    public MetricAffectingSpan getUnderlying()
    {
      return mStyle.getUnderlying();
    }
    
    public void updateDrawState(TextPaint paramTextPaint)
    {
      mStyle.updateDrawState(paramTextPaint);
    }
    
    public void updateMeasureState(TextPaint paramTextPaint)
    {
      mStyle.updateMeasureState(paramTextPaint);
    }
  }
}
