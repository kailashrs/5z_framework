package android.text.style;

import android.graphics.Rasterizer;
import android.text.TextPaint;

public class RasterizerSpan
  extends CharacterStyle
  implements UpdateAppearance
{
  private Rasterizer mRasterizer;
  
  public RasterizerSpan(Rasterizer paramRasterizer)
  {
    mRasterizer = paramRasterizer;
  }
  
  public Rasterizer getRasterizer()
  {
    return mRasterizer;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setRasterizer(mRasterizer);
  }
}
