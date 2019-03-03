package android.view;

import android.graphics.drawable.Drawable;

public abstract interface ContextMenu
  extends Menu
{
  public abstract void clearHeader();
  
  public abstract ContextMenu setHeaderIcon(int paramInt);
  
  public abstract ContextMenu setHeaderIcon(Drawable paramDrawable);
  
  public abstract ContextMenu setHeaderTitle(int paramInt);
  
  public abstract ContextMenu setHeaderTitle(CharSequence paramCharSequence);
  
  public abstract ContextMenu setHeaderView(View paramView);
  
  public static abstract interface ContextMenuInfo {}
}
