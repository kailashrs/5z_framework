package android.view;

import android.content.Context;

public class ViewGroupOverlay
  extends ViewOverlay
{
  ViewGroupOverlay(Context paramContext, View paramView)
  {
    super(paramContext, paramView);
  }
  
  public void add(View paramView)
  {
    mOverlayViewGroup.add(paramView);
  }
  
  public void remove(View paramView)
  {
    mOverlayViewGroup.remove(paramView);
  }
}
