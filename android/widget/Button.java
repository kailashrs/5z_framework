package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.PointerIcon;

@RemoteViews.RemoteView
public class Button
  extends TextView
{
  public Button(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Button(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842824);
  }
  
  public Button(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Button(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return Button.class.getName();
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if ((getPointerIcon() == null) && (isClickable()) && (isEnabled())) {
      return PointerIcon.getSystemIcon(getContext(), 1002);
    }
    return super.onResolvePointerIcon(paramMotionEvent, paramInt);
  }
}
