package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

class ExtractButton
  extends Button
{
  public ExtractButton(Context paramContext)
  {
    super(paramContext, null);
  }
  
  public ExtractButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 16842824);
  }
  
  public ExtractButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ExtractButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public boolean hasWindowFocus()
  {
    boolean bool;
    if ((isEnabled()) && (getVisibility() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
