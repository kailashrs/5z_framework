package com.android.internal.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import com.android.internal.R.styleable;

public class ActionBarPolicy
{
  private Context mContext;
  
  private ActionBarPolicy(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public static ActionBarPolicy get(Context paramContext)
  {
    return new ActionBarPolicy(paramContext);
  }
  
  public boolean enableHomeButtonByDefault()
  {
    boolean bool;
    if (mContext.getApplicationInfo().targetSdkVersion < 14) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getEmbeddedMenuWidthLimit()
  {
    return mContext.getResources().getDisplayMetrics().widthPixels / 2;
  }
  
  public int getMaxActionButtons()
  {
    Configuration localConfiguration = mContext.getResources().getConfiguration();
    int i = screenWidthDp;
    int j = screenHeightDp;
    if ((smallestScreenWidthDp <= 600) && ((i <= 960) || (j <= 720)) && ((i <= 720) || (j <= 960)))
    {
      if ((i < 500) && ((i <= 640) || (j <= 480)) && ((i <= 480) || (j <= 640)))
      {
        if (i >= 360) {
          return 3;
        }
        return 2;
      }
      return 4;
    }
    return 5;
  }
  
  public int getStackedTabMaxWidth()
  {
    return mContext.getResources().getDimensionPixelSize(17104917);
  }
  
  public int getTabContainerHeight()
  {
    TypedArray localTypedArray = mContext.obtainStyledAttributes(null, R.styleable.ActionBar, 16843470, 0);
    int i = localTypedArray.getLayoutDimension(4, 0);
    Resources localResources = mContext.getResources();
    int j = i;
    if (!hasEmbeddedTabs()) {
      j = Math.min(i, localResources.getDimensionPixelSize(17104916));
    }
    localTypedArray.recycle();
    return j;
  }
  
  public boolean hasEmbeddedTabs()
  {
    if (mContext.getApplicationInfo().targetSdkVersion >= 16) {
      return mContext.getResources().getBoolean(17956865);
    }
    Configuration localConfiguration = mContext.getResources().getConfiguration();
    int i = screenWidthDp;
    int j = screenHeightDp;
    boolean bool;
    if ((orientation != 2) && (i < 480) && ((i < 640) || (j < 480))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean showsOverflowMenuButton()
  {
    return true;
  }
}
