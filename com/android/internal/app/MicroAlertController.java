package com.android.internal.app;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MicroAlertController
  extends AlertController
{
  public MicroAlertController(Context paramContext, DialogInterface paramDialogInterface, Window paramWindow)
  {
    super(paramContext, paramDialogInterface, paramWindow);
  }
  
  protected void setupButtons(ViewGroup paramViewGroup)
  {
    super.setupButtons(paramViewGroup);
    if (paramViewGroup.getVisibility() == 8) {
      paramViewGroup.setVisibility(4);
    }
  }
  
  protected void setupContent(ViewGroup paramViewGroup)
  {
    mScrollView = ((ScrollView)mWindow.findViewById(16909316));
    mMessageView = ((TextView)paramViewGroup.findViewById(16908299));
    if (mMessageView == null) {
      return;
    }
    if (mMessage != null)
    {
      mMessageView.setText(mMessage);
    }
    else
    {
      mMessageView.setVisibility(8);
      paramViewGroup.removeView(mMessageView);
      if (mListView != null)
      {
        paramViewGroup = mScrollView.findViewById(16909491);
        ((ViewGroup)paramViewGroup.getParent()).removeView(paramViewGroup);
        Object localObject1 = new FrameLayout.LayoutParams(paramViewGroup.getLayoutParams());
        gravity = 48;
        paramViewGroup.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        localObject1 = mScrollView.findViewById(16908826);
        ((ViewGroup)((View)localObject1).getParent()).removeView((View)localObject1);
        Object localObject2 = new FrameLayout.LayoutParams(((View)localObject1).getLayoutParams());
        gravity = 80;
        ((View)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        localObject2 = (ViewGroup)mScrollView.getParent();
        ((ViewGroup)localObject2).removeViewAt(((ViewGroup)localObject2).indexOfChild(mScrollView));
        ((ViewGroup)localObject2).addView(mListView, new ViewGroup.LayoutParams(-1, -1));
        ((ViewGroup)localObject2).addView(paramViewGroup);
        ((ViewGroup)localObject2).addView((View)localObject1);
      }
      else
      {
        paramViewGroup.setVisibility(8);
      }
    }
  }
  
  protected void setupTitle(ViewGroup paramViewGroup)
  {
    super.setupTitle(paramViewGroup);
    if (paramViewGroup.getVisibility() == 8) {
      paramViewGroup.setVisibility(4);
    }
  }
}
