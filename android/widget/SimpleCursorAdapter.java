package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

public class SimpleCursorAdapter
  extends ResourceCursorAdapter
{
  private CursorToStringConverter mCursorToStringConverter;
  protected int[] mFrom;
  String[] mOriginalFrom;
  private int mStringConversionColumn = -1;
  protected int[] mTo;
  private ViewBinder mViewBinder;
  
  @Deprecated
  public SimpleCursorAdapter(Context paramContext, int paramInt, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    super(paramContext, paramInt, paramCursor);
    mTo = paramArrayOfInt;
    mOriginalFrom = paramArrayOfString;
    findColumns(paramCursor, paramArrayOfString);
  }
  
  public SimpleCursorAdapter(Context paramContext, int paramInt1, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt2)
  {
    super(paramContext, paramInt1, paramCursor, paramInt2);
    mTo = paramArrayOfInt;
    mOriginalFrom = paramArrayOfString;
    findColumns(paramCursor, paramArrayOfString);
  }
  
  private void findColumns(Cursor paramCursor, String[] paramArrayOfString)
  {
    if (paramCursor != null)
    {
      int i = paramArrayOfString.length;
      if ((mFrom == null) || (mFrom.length != i)) {
        mFrom = new int[i];
      }
      for (int j = 0; j < i; j++) {
        mFrom[j] = paramCursor.getColumnIndexOrThrow(paramArrayOfString[j]);
      }
    }
    else
    {
      mFrom = null;
    }
  }
  
  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    ViewBinder localViewBinder = mViewBinder;
    int i = mTo.length;
    int[] arrayOfInt1 = mFrom;
    int[] arrayOfInt2 = mTo;
    for (int j = 0; j < i; j++)
    {
      View localView = paramView.findViewById(arrayOfInt2[j]);
      if (localView != null)
      {
        boolean bool = false;
        if (localViewBinder != null) {
          bool = localViewBinder.setViewValue(localView, paramCursor, arrayOfInt1[j]);
        }
        if (!bool)
        {
          String str = paramCursor.getString(arrayOfInt1[j]);
          paramContext = str;
          if (str == null) {
            paramContext = "";
          }
          if ((localView instanceof TextView))
          {
            setViewText((TextView)localView, paramContext);
          }
          else if ((localView instanceof ImageView))
          {
            setViewImage((ImageView)localView, paramContext);
          }
          else
          {
            paramView = new StringBuilder();
            paramView.append(localView.getClass().getName());
            paramView.append(" is not a  view that can be bounds by this SimpleCursorAdapter");
            throw new IllegalStateException(paramView.toString());
          }
        }
      }
    }
  }
  
  public void changeCursorAndColumns(Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    mOriginalFrom = paramArrayOfString;
    mTo = paramArrayOfInt;
    findColumns(paramCursor, mOriginalFrom);
    super.changeCursor(paramCursor);
  }
  
  public CharSequence convertToString(Cursor paramCursor)
  {
    if (mCursorToStringConverter != null) {
      return mCursorToStringConverter.convertToString(paramCursor);
    }
    if (mStringConversionColumn > -1) {
      return paramCursor.getString(mStringConversionColumn);
    }
    return super.convertToString(paramCursor);
  }
  
  public CursorToStringConverter getCursorToStringConverter()
  {
    return mCursorToStringConverter;
  }
  
  public int getStringConversionColumn()
  {
    return mStringConversionColumn;
  }
  
  public ViewBinder getViewBinder()
  {
    return mViewBinder;
  }
  
  public void setCursorToStringConverter(CursorToStringConverter paramCursorToStringConverter)
  {
    mCursorToStringConverter = paramCursorToStringConverter;
  }
  
  public void setStringConversionColumn(int paramInt)
  {
    mStringConversionColumn = paramInt;
  }
  
  public void setViewBinder(ViewBinder paramViewBinder)
  {
    mViewBinder = paramViewBinder;
  }
  
  public void setViewImage(ImageView paramImageView, String paramString)
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
  
  public Cursor swapCursor(Cursor paramCursor)
  {
    findColumns(paramCursor, mOriginalFrom);
    return super.swapCursor(paramCursor);
  }
  
  public static abstract interface CursorToStringConverter
  {
    public abstract CharSequence convertToString(Cursor paramCursor);
  }
  
  public static abstract interface ViewBinder
  {
    public abstract boolean setViewValue(View paramView, Cursor paramCursor, int paramInt);
  }
}
