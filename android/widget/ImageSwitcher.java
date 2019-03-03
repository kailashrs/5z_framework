package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

public class ImageSwitcher
  extends ViewSwitcher
{
  public ImageSwitcher(Context paramContext)
  {
    super(paramContext);
  }
  
  public ImageSwitcher(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ImageSwitcher.class.getName();
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    ((ImageView)getNextView()).setImageDrawable(paramDrawable);
    showNext();
  }
  
  public void setImageResource(int paramInt)
  {
    ((ImageView)getNextView()).setImageResource(paramInt);
    showNext();
  }
  
  public void setImageURI(Uri paramUri)
  {
    ((ImageView)getNextView()).setImageURI(paramUri);
    showNext();
  }
}
