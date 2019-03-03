package android.text.style;

import android.text.TextPaint;
import android.view.View;

public abstract class ClickableSpan
  extends CharacterStyle
  implements UpdateAppearance
{
  private static int sIdCounter = 0;
  private int mId;
  
  public ClickableSpan()
  {
    int i = sIdCounter;
    sIdCounter = i + 1;
    mId = i;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public abstract void onClick(View paramView);
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setColor(linkColor);
    paramTextPaint.setUnderlineText(true);
  }
}
