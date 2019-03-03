package com.android.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;
import java.util.Objects;

@RemoteViews.RemoteView
public class CachingIconView
  extends ImageView
{
  private int mDesiredVisibility;
  private boolean mForceHidden;
  private boolean mInternalSetDrawable;
  private String mLastPackage;
  private int mLastResId;
  
  public CachingIconView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private String normalizeIconPackage(Icon paramIcon)
  {
    if (paramIcon == null) {
      return null;
    }
    paramIcon = paramIcon.getResPackage();
    if (TextUtils.isEmpty(paramIcon)) {
      return null;
    }
    if (paramIcon.equals(mContext.getPackageName())) {
      return null;
    }
    return paramIcon;
  }
  
  private void resetCache()
  {
    try
    {
      mLastResId = 0;
      mLastPackage = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private boolean testAndSetCache(int paramInt)
  {
    if (paramInt != 0) {
      try
      {
        if (mLastResId != 0) {
          if ((paramInt == mLastResId) && (mLastPackage == null)) {
            bool = true;
          } else {
            bool = false;
          }
        }
      }
      finally
      {
        break label61;
      }
    }
    boolean bool = false;
    mLastPackage = null;
    mLastResId = paramInt;
    return bool;
    label61:
    throw localObject;
  }
  
  private boolean testAndSetCache(Icon paramIcon)
  {
    boolean bool1 = false;
    if (paramIcon != null) {
      try
      {
        if (paramIcon.getType() == 2)
        {
          String str = normalizeIconPackage(paramIcon);
          boolean bool2 = bool1;
          if (mLastResId != 0)
          {
            bool2 = bool1;
            if (paramIcon.getResId() == mLastResId)
            {
              bool2 = bool1;
              if (Objects.equals(str, mLastPackage)) {
                bool2 = true;
              }
            }
          }
          mLastPackage = str;
          mLastResId = paramIcon.getResId();
          return bool2;
        }
      }
      finally
      {
        break label93;
      }
    }
    resetCache();
    return false;
    label93:
    throw paramIcon;
  }
  
  private void updateVisibility()
  {
    int i;
    if ((mDesiredVisibility == 0) && (mForceHidden)) {
      i = 4;
    } else {
      i = mDesiredVisibility;
    }
    super.setVisibility(i);
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    resetCache();
  }
  
  public void setForceHidden(boolean paramBoolean)
  {
    mForceHidden = paramBoolean;
    updateVisibility();
  }
  
  @RemotableViewMethod
  public void setImageBitmap(Bitmap paramBitmap)
  {
    resetCache();
    super.setImageBitmap(paramBitmap);
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    if (!mInternalSetDrawable) {
      resetCache();
    }
    super.setImageDrawable(paramDrawable);
  }
  
  @RemotableViewMethod(asyncImpl="setImageIconAsync")
  public void setImageIcon(Icon paramIcon)
  {
    if (!testAndSetCache(paramIcon))
    {
      mInternalSetDrawable = true;
      super.setImageIcon(paramIcon);
      mInternalSetDrawable = false;
    }
  }
  
  public Runnable setImageIconAsync(Icon paramIcon)
  {
    resetCache();
    return super.setImageIconAsync(paramIcon);
  }
  
  @RemotableViewMethod(asyncImpl="setImageResourceAsync")
  public void setImageResource(int paramInt)
  {
    if (!testAndSetCache(paramInt))
    {
      mInternalSetDrawable = true;
      super.setImageResource(paramInt);
      mInternalSetDrawable = false;
    }
  }
  
  public Runnable setImageResourceAsync(int paramInt)
  {
    resetCache();
    return super.setImageResourceAsync(paramInt);
  }
  
  @RemotableViewMethod(asyncImpl="setImageURIAsync")
  public void setImageURI(Uri paramUri)
  {
    resetCache();
    super.setImageURI(paramUri);
  }
  
  public Runnable setImageURIAsync(Uri paramUri)
  {
    resetCache();
    return super.setImageURIAsync(paramUri);
  }
  
  @RemotableViewMethod
  public void setVisibility(int paramInt)
  {
    mDesiredVisibility = paramInt;
    updateVisibility();
  }
}
