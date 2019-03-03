package android.view;

import android.graphics.Outline;
import android.graphics.drawable.Drawable;

public abstract class ViewOutlineProvider
{
  public static final ViewOutlineProvider BACKGROUND = new ViewOutlineProvider()
  {
    public void getOutline(View paramAnonymousView, Outline paramAnonymousOutline)
    {
      Drawable localDrawable = paramAnonymousView.getBackground();
      if (localDrawable != null)
      {
        localDrawable.getOutline(paramAnonymousOutline);
      }
      else
      {
        paramAnonymousOutline.setRect(0, 0, paramAnonymousView.getWidth(), paramAnonymousView.getHeight());
        paramAnonymousOutline.setAlpha(0.0F);
      }
    }
  };
  public static final ViewOutlineProvider BOUNDS = new ViewOutlineProvider()
  {
    public void getOutline(View paramAnonymousView, Outline paramAnonymousOutline)
    {
      paramAnonymousOutline.setRect(0, 0, paramAnonymousView.getWidth(), paramAnonymousView.getHeight());
    }
  };
  public static final ViewOutlineProvider PADDED_BOUNDS = new ViewOutlineProvider()
  {
    public void getOutline(View paramAnonymousView, Outline paramAnonymousOutline)
    {
      paramAnonymousOutline.setRect(paramAnonymousView.getPaddingLeft(), paramAnonymousView.getPaddingTop(), paramAnonymousView.getWidth() - paramAnonymousView.getPaddingRight(), paramAnonymousView.getHeight() - paramAnonymousView.getPaddingBottom());
    }
  };
  
  public ViewOutlineProvider() {}
  
  public abstract void getOutline(View paramView, Outline paramOutline);
}
