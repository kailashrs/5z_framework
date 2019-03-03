package android.content.res;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;

class DrawableCache
  extends ThemedResourceCache<Drawable.ConstantState>
{
  DrawableCache() {}
  
  public Drawable getInstance(long paramLong, Resources paramResources, Resources.Theme paramTheme)
  {
    Drawable.ConstantState localConstantState = (Drawable.ConstantState)get(paramLong, paramTheme);
    if (localConstantState != null) {
      return localConstantState.newDrawable(paramResources, paramTheme);
    }
    return null;
  }
  
  public boolean shouldInvalidateEntry(Drawable.ConstantState paramConstantState, int paramInt)
  {
    return Configuration.needNewResources(paramInt, paramConstantState.getChangingConfigurations());
  }
}
