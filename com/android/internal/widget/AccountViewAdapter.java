package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public class AccountViewAdapter
  extends BaseAdapter
{
  private Context mContext;
  private List<AccountElements> mData;
  
  public AccountViewAdapter(Context paramContext, List<AccountElements> paramList)
  {
    mContext = paramContext;
    mData = paramList;
  }
  
  public int getCount()
  {
    return mData.size();
  }
  
  public Object getItem(int paramInt)
  {
    return mData.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = new AccountItemView(mContext);
    } else {
      paramView = (AccountItemView)paramView;
    }
    paramView.setViewItem((AccountElements)getItem(paramInt));
    return paramView;
  }
  
  public void updateData(List<AccountElements> paramList)
  {
    mData = paramList;
    notifyDataSetChanged();
  }
  
  public static class AccountElements
  {
    private Drawable mDrawable;
    private int mIcon;
    private String mName;
    private String mNumber;
    
    private AccountElements(int paramInt, Drawable paramDrawable, String paramString1, String paramString2)
    {
      mIcon = paramInt;
      mDrawable = paramDrawable;
      mName = paramString1;
      mNumber = paramString2;
    }
    
    public AccountElements(int paramInt, String paramString1, String paramString2)
    {
      this(paramInt, null, paramString1, paramString2);
    }
    
    public AccountElements(Drawable paramDrawable, String paramString1, String paramString2)
    {
      this(0, paramDrawable, paramString1, paramString2);
    }
    
    public Drawable getDrawable()
    {
      return mDrawable;
    }
    
    public int getIcon()
    {
      return mIcon;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public String getNumber()
    {
      return mNumber;
    }
  }
}
