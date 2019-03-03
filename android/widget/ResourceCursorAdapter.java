package android.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceCursorAdapter
  extends CursorAdapter
{
  private LayoutInflater mDropDownInflater;
  private int mDropDownLayout;
  private LayoutInflater mInflater;
  private int mLayout;
  
  @Deprecated
  public ResourceCursorAdapter(Context paramContext, int paramInt, Cursor paramCursor)
  {
    super(paramContext, paramCursor);
    mDropDownLayout = paramInt;
    mLayout = paramInt;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    mDropDownInflater = mInflater;
  }
  
  public ResourceCursorAdapter(Context paramContext, int paramInt1, Cursor paramCursor, int paramInt2)
  {
    super(paramContext, paramCursor, paramInt2);
    mDropDownLayout = paramInt1;
    mLayout = paramInt1;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    mDropDownInflater = mInflater;
  }
  
  public ResourceCursorAdapter(Context paramContext, int paramInt, Cursor paramCursor, boolean paramBoolean)
  {
    super(paramContext, paramCursor, paramBoolean);
    mDropDownLayout = paramInt;
    mLayout = paramInt;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    mDropDownInflater = mInflater;
  }
  
  public View newDropDownView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return mDropDownInflater.inflate(mDropDownLayout, paramViewGroup, false);
  }
  
  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return mInflater.inflate(mLayout, paramViewGroup, false);
  }
  
  public void setDropDownViewResource(int paramInt)
  {
    mDropDownLayout = paramInt;
  }
  
  public void setDropDownViewTheme(Resources.Theme paramTheme)
  {
    super.setDropDownViewTheme(paramTheme);
    if (paramTheme == null) {
      mDropDownInflater = null;
    } else if (paramTheme == mInflater.getContext().getTheme()) {
      mDropDownInflater = mInflater;
    } else {
      mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(mContext, paramTheme));
    }
  }
  
  public void setViewResource(int paramInt)
  {
    mLayout = paramInt;
  }
}
