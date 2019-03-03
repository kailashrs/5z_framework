package com.android.internal.globalactions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public abstract class SinglePressAction
  implements Action
{
  private final Drawable mIcon;
  private final int mIconResId;
  private final CharSequence mMessage;
  private final int mMessageResId;
  
  protected SinglePressAction(int paramInt1, int paramInt2)
  {
    mIconResId = paramInt1;
    mMessageResId = paramInt2;
    mMessage = null;
    mIcon = null;
  }
  
  protected SinglePressAction(int paramInt, Drawable paramDrawable, CharSequence paramCharSequence)
  {
    mIconResId = paramInt;
    mMessageResId = 0;
    mMessage = paramCharSequence;
    mIcon = paramDrawable;
  }
  
  public View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    View localView = paramLayoutInflater.inflate(17367178, paramViewGroup, false);
    ImageView localImageView = (ImageView)localView.findViewById(16908294);
    paramLayoutInflater = (TextView)localView.findViewById(16908299);
    paramView = (TextView)localView.findViewById(16909410);
    paramViewGroup = getStatus();
    if (paramView != null) {
      if (!TextUtils.isEmpty(paramViewGroup)) {
        paramView.setText(paramViewGroup);
      } else {
        paramView.setVisibility(8);
      }
    }
    if (localImageView != null) {
      if (mIcon != null)
      {
        localImageView.setImageDrawable(mIcon);
        localImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      }
      else if (mIconResId != 0)
      {
        localImageView.setImageDrawable(paramContext.getDrawable(mIconResId));
      }
    }
    if (paramLayoutInflater != null) {
      if (mMessage != null) {
        paramLayoutInflater.setText(mMessage);
      } else {
        paramLayoutInflater.setText(mMessageResId);
      }
    }
    return localView;
  }
  
  public CharSequence getLabelForAccessibility(Context paramContext)
  {
    if (mMessage != null) {
      return mMessage;
    }
    return paramContext.getString(mMessageResId);
  }
  
  public String getStatus()
  {
    return null;
  }
  
  public boolean isEnabled()
  {
    return true;
  }
  
  public abstract void onPress();
}
