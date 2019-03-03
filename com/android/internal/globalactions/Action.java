package com.android.internal.globalactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract interface Action
{
  public abstract View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater);
  
  public abstract CharSequence getLabelForAccessibility(Context paramContext);
  
  public abstract boolean isEnabled();
  
  public abstract void onPress();
  
  public abstract boolean showBeforeProvisioning();
  
  public abstract boolean showDuringKeyguard();
}
