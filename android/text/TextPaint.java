package android.text;

import android.graphics.Paint;

public class TextPaint
  extends Paint
{
  public int baselineShift;
  public int bgColor;
  public float density = 1.0F;
  public int[] drawableState;
  public int linkColor;
  public int underlineColor = 0;
  public float underlineThickness;
  
  public TextPaint() {}
  
  public TextPaint(int paramInt)
  {
    super(paramInt);
  }
  
  public TextPaint(Paint paramPaint)
  {
    super(paramPaint);
  }
  
  public float getUnderlineThickness()
  {
    if (underlineColor != 0) {
      return underlineThickness;
    }
    return super.getUnderlineThickness();
  }
  
  public boolean hasEqualAttributes(TextPaint paramTextPaint)
  {
    boolean bool;
    if ((bgColor == bgColor) && (baselineShift == baselineShift) && (linkColor == linkColor) && (drawableState == drawableState) && (density == density) && (underlineColor == underlineColor) && (underlineThickness == underlineThickness) && (super.hasEqualAttributes(paramTextPaint))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void set(TextPaint paramTextPaint)
  {
    super.set(paramTextPaint);
    bgColor = bgColor;
    baselineShift = baselineShift;
    linkColor = linkColor;
    drawableState = drawableState;
    density = density;
    underlineColor = underlineColor;
    underlineThickness = underlineThickness;
  }
  
  public void setUnderlineText(int paramInt, float paramFloat)
  {
    underlineColor = paramInt;
    underlineThickness = paramFloat;
  }
}
