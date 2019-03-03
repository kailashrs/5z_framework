package android.text.style;

import android.graphics.MaskFilter;
import android.text.TextPaint;

public class MaskFilterSpan
  extends CharacterStyle
  implements UpdateAppearance
{
  private MaskFilter mFilter;
  
  public MaskFilterSpan(MaskFilter paramMaskFilter)
  {
    mFilter = paramMaskFilter;
  }
  
  public MaskFilter getMaskFilter()
  {
    return mFilter;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setMaskFilter(mFilter);
  }
}
