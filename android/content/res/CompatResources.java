package android.content.res;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

public class CompatResources
  extends Resources
{
  private WeakReference<Context> mContext = new WeakReference(null);
  
  public CompatResources(ClassLoader paramClassLoader)
  {
    super(paramClassLoader);
  }
  
  private Resources.Theme getTheme()
  {
    Object localObject = (Context)mContext.get();
    if (localObject != null) {
      localObject = ((Context)localObject).getTheme();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public int getColor(int paramInt)
    throws Resources.NotFoundException
  {
    return getColor(paramInt, getTheme());
  }
  
  public ColorStateList getColorStateList(int paramInt)
    throws Resources.NotFoundException
  {
    return getColorStateList(paramInt, getTheme());
  }
  
  public Drawable getDrawable(int paramInt)
    throws Resources.NotFoundException
  {
    return getDrawable(paramInt, getTheme());
  }
  
  public Drawable getDrawableForDensity(int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return getDrawableForDensity(paramInt1, paramInt2, getTheme());
  }
  
  public void setContext(Context paramContext)
  {
    mContext = new WeakReference(paramContext);
  }
}
