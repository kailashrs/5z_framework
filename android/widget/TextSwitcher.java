package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class TextSwitcher
  extends ViewSwitcher
{
  public TextSwitcher(Context paramContext)
  {
    super(paramContext);
  }
  
  public TextSwitcher(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramView instanceof TextView))
    {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    }
    throw new IllegalArgumentException("TextSwitcher children must be instances of TextView");
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return TextSwitcher.class.getName();
  }
  
  public void setCurrentText(CharSequence paramCharSequence)
  {
    ((TextView)getCurrentView()).setText(paramCharSequence);
  }
  
  public void setText(CharSequence paramCharSequence)
  {
    ((TextView)getNextView()).setText(paramCharSequence);
    showNext();
  }
}
