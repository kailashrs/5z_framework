package android.widget;

import android.content.Context;
import android.util.AttributeSet;

public class RadioButton
  extends CompoundButton
{
  public RadioButton(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public RadioButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842878);
  }
  
  public RadioButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public RadioButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return RadioButton.class.getName();
  }
  
  public void toggle()
  {
    if (!isChecked()) {
      super.toggle();
    }
  }
}
