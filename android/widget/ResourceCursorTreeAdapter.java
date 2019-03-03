package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceCursorTreeAdapter
  extends CursorTreeAdapter
{
  private int mChildLayout;
  private int mCollapsedGroupLayout;
  private int mExpandedGroupLayout;
  private LayoutInflater mInflater;
  private int mLastChildLayout;
  
  public ResourceCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2)
  {
    this(paramContext, paramCursor, paramInt1, paramInt1, paramInt2, paramInt2);
  }
  
  public ResourceCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext, paramCursor, paramInt1, paramInt2, paramInt3, paramInt3);
  }
  
  public ResourceCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super(paramCursor, paramContext);
    mCollapsedGroupLayout = paramInt1;
    mExpandedGroupLayout = paramInt2;
    mChildLayout = paramInt3;
    mLastChildLayout = paramInt4;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
  }
  
  public View newChildView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup)
  {
    paramContext = mInflater;
    int i;
    if (paramBoolean) {
      i = mLastChildLayout;
    } else {
      i = mChildLayout;
    }
    return paramContext.inflate(i, paramViewGroup, false);
  }
  
  public View newGroupView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup)
  {
    paramContext = mInflater;
    int i;
    if (paramBoolean) {
      i = mExpandedGroupLayout;
    } else {
      i = mCollapsedGroupLayout;
    }
    return paramContext.inflate(i, paramViewGroup, false);
  }
}
