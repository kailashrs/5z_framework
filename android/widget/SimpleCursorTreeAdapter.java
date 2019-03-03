package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

public abstract class SimpleCursorTreeAdapter
  extends ResourceCursorTreeAdapter
{
  private int[] mChildFrom;
  private String[] mChildFromNames;
  private int[] mChildTo;
  private int[] mGroupFrom;
  private String[] mGroupFromNames;
  private int[] mGroupTo;
  private ViewBinder mViewBinder;
  
  public SimpleCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2, String[] paramArrayOfString1, int[] paramArrayOfInt1, int paramInt3, int paramInt4, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    super(paramContext, paramCursor, paramInt1, paramInt2, paramInt3, paramInt4);
    init(paramArrayOfString1, paramArrayOfInt1, paramArrayOfString2, paramArrayOfInt2);
  }
  
  public SimpleCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, int paramInt2, String[] paramArrayOfString1, int[] paramArrayOfInt1, int paramInt3, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    super(paramContext, paramCursor, paramInt1, paramInt2, paramInt3);
    init(paramArrayOfString1, paramArrayOfInt1, paramArrayOfString2, paramArrayOfInt2);
  }
  
  public SimpleCursorTreeAdapter(Context paramContext, Cursor paramCursor, int paramInt1, String[] paramArrayOfString1, int[] paramArrayOfInt1, int paramInt2, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    super(paramContext, paramCursor, paramInt1, paramInt2);
    init(paramArrayOfString1, paramArrayOfInt1, paramArrayOfString2, paramArrayOfInt2);
  }
  
  private void bindView(View paramView, Context paramContext, Cursor paramCursor, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    ViewBinder localViewBinder = mViewBinder;
    for (int i = 0; i < paramArrayOfInt2.length; i++)
    {
      View localView = paramView.findViewById(paramArrayOfInt2[i]);
      if (localView != null)
      {
        boolean bool = false;
        if (localViewBinder != null) {
          bool = localViewBinder.setViewValue(localView, paramCursor, paramArrayOfInt1[i]);
        }
        if (!bool)
        {
          String str = paramCursor.getString(paramArrayOfInt1[i]);
          paramContext = str;
          if (str == null) {
            paramContext = "";
          }
          if ((localView instanceof TextView)) {
            setViewText((TextView)localView, paramContext);
          } else if ((localView instanceof ImageView)) {
            setViewImage((ImageView)localView, paramContext);
          } else {
            throw new IllegalStateException("SimpleCursorTreeAdapter can bind values only to TextView and ImageView!");
          }
        }
      }
    }
  }
  
  private void init(String[] paramArrayOfString1, int[] paramArrayOfInt1, String[] paramArrayOfString2, int[] paramArrayOfInt2)
  {
    mGroupFromNames = paramArrayOfString1;
    mGroupTo = paramArrayOfInt1;
    mChildFromNames = paramArrayOfString2;
    mChildTo = paramArrayOfInt2;
  }
  
  private void initFromColumns(Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    for (int i = paramArrayOfString.length - 1; i >= 0; i--) {
      paramArrayOfInt[i] = paramCursor.getColumnIndexOrThrow(paramArrayOfString[i]);
    }
  }
  
  protected void bindChildView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean)
  {
    if (mChildFrom == null)
    {
      mChildFrom = new int[mChildFromNames.length];
      initFromColumns(paramCursor, mChildFromNames, mChildFrom);
    }
    bindView(paramView, paramContext, paramCursor, mChildFrom, mChildTo);
  }
  
  protected void bindGroupView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean)
  {
    if (mGroupFrom == null)
    {
      mGroupFrom = new int[mGroupFromNames.length];
      initFromColumns(paramCursor, mGroupFromNames, mGroupFrom);
    }
    bindView(paramView, paramContext, paramCursor, mGroupFrom, mGroupTo);
  }
  
  public ViewBinder getViewBinder()
  {
    return mViewBinder;
  }
  
  public void setViewBinder(ViewBinder paramViewBinder)
  {
    mViewBinder = paramViewBinder;
  }
  
  protected void setViewImage(ImageView paramImageView, String paramString)
  {
    try
    {
      paramImageView.setImageResource(Integer.parseInt(paramString));
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramImageView.setImageURI(Uri.parse(paramString));
    }
  }
  
  public void setViewText(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
  }
  
  public static abstract interface ViewBinder
  {
    public abstract boolean setViewValue(View paramView, Cursor paramCursor, int paramInt);
  }
}
