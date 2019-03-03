package com.android.internal.globalactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
import java.util.function.BooleanSupplier;

public class ActionsAdapter
  extends BaseAdapter
{
  private final Context mContext;
  private final BooleanSupplier mDeviceProvisioned;
  private final List<Action> mItems;
  private final BooleanSupplier mKeyguardShowing;
  
  public ActionsAdapter(Context paramContext, List<Action> paramList, BooleanSupplier paramBooleanSupplier1, BooleanSupplier paramBooleanSupplier2)
  {
    mContext = paramContext;
    mItems = paramList;
    mDeviceProvisioned = paramBooleanSupplier1;
    mKeyguardShowing = paramBooleanSupplier2;
  }
  
  public boolean areAllItemsEnabled()
  {
    return false;
  }
  
  public int getCount()
  {
    boolean bool1 = mKeyguardShowing.getAsBoolean();
    boolean bool2 = mDeviceProvisioned.getAsBoolean();
    int i = 0;
    for (int j = 0; j < mItems.size(); j++)
    {
      Action localAction = (Action)mItems.get(j);
      if (((!bool1) || (localAction.showDuringKeyguard())) && ((bool2) || (localAction.showBeforeProvisioning()))) {
        i++;
      }
    }
    return i;
  }
  
  public Action getItem(int paramInt)
  {
    boolean bool1 = mKeyguardShowing.getAsBoolean();
    boolean bool2 = mDeviceProvisioned.getAsBoolean();
    int i = 0;
    for (int j = 0; j < mItems.size(); j++)
    {
      localObject = (Action)mItems.get(j);
      if (((!bool1) || (((Action)localObject).showDuringKeyguard())) && ((bool2) || (((Action)localObject).showBeforeProvisioning())))
      {
        if (i == paramInt) {
          return localObject;
        }
        i++;
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("position ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" out of range of showable actions, filtered count=");
    ((StringBuilder)localObject).append(getCount());
    ((StringBuilder)localObject).append(", keyguardshowing=");
    ((StringBuilder)localObject).append(bool1);
    ((StringBuilder)localObject).append(", provisioned=");
    ((StringBuilder)localObject).append(bool2);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return getItem(paramInt).create(mContext, paramView, paramViewGroup, LayoutInflater.from(mContext));
  }
  
  public boolean isEnabled(int paramInt)
  {
    return getItem(paramInt).isEnabled();
  }
}
