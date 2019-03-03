package com.android.internal.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.widget.TextView;

public class TooltipPopup
{
  private static final String TAG = "TooltipPopup";
  private final View mContentView;
  private final Context mContext;
  private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
  private final TextView mMessageView;
  private final int[] mTmpAnchorPos = new int[2];
  private final int[] mTmpAppPos = new int[2];
  private final Rect mTmpDisplayFrame = new Rect();
  
  public TooltipPopup(Context paramContext)
  {
    mContext = paramContext;
    mContentView = LayoutInflater.from(mContext).inflate(17367347, null);
    mMessageView = ((TextView)mContentView.findViewById(16908299));
    mLayoutParams.setTitle(mContext.getString(17041128));
    mLayoutParams.packageName = mContext.getOpPackageName();
    mLayoutParams.type = 1005;
    mLayoutParams.width = -2;
    mLayoutParams.height = -2;
    mLayoutParams.format = -3;
    mLayoutParams.windowAnimations = 16974596;
    mLayoutParams.flags = 24;
  }
  
  private void computePosition(View paramView, int paramInt1, int paramInt2, boolean paramBoolean, WindowManager.LayoutParams paramLayoutParams)
  {
    token = paramView.getApplicationWindowToken();
    int i = mContext.getResources().getDimensionPixelOffset(17105484);
    if (paramView.getWidth() < i) {
      paramInt1 = paramView.getWidth() / 2;
    }
    if (paramView.getHeight() >= i)
    {
      i = mContext.getResources().getDimensionPixelOffset(17105483);
      j = paramInt2 + i;
      i = paramInt2 - i;
      paramInt2 = j;
    }
    else
    {
      paramInt2 = paramView.getHeight();
      i = 0;
    }
    gravity = 49;
    Object localObject = mContext.getResources();
    if (paramBoolean) {
      j = 17105487;
    } else {
      j = 17105486;
    }
    int j = ((Resources)localObject).getDimensionPixelOffset(j);
    localObject = WindowManagerGlobal.getInstance().getWindowView(paramView.getApplicationWindowToken());
    if (localObject == null)
    {
      Slog.e("TooltipPopup", "Cannot find app view");
      return;
    }
    ((View)localObject).getWindowVisibleDisplayFrame(mTmpDisplayFrame);
    ((View)localObject).getLocationOnScreen(mTmpAppPos);
    paramView.getLocationOnScreen(mTmpAnchorPos);
    paramView = mTmpAnchorPos;
    paramView[0] -= mTmpAppPos[0];
    paramView = mTmpAnchorPos;
    paramView[1] -= mTmpAppPos[1];
    x = (mTmpAnchorPos[0] + paramInt1 - ((View)localObject).getWidth() / 2);
    paramInt1 = View.MeasureSpec.makeMeasureSpec(0, 0);
    mContentView.measure(paramInt1, paramInt1);
    paramInt1 = mContentView.getMeasuredHeight();
    i = mTmpAnchorPos[1] + i - j - paramInt1;
    paramInt2 = mTmpAnchorPos[1] + paramInt2 + j;
    if (paramBoolean)
    {
      if (i >= 0) {
        y = i;
      } else {
        y = paramInt2;
      }
    }
    else if (paramInt2 + paramInt1 <= mTmpDisplayFrame.height()) {
      y = paramInt2;
    } else {
      y = i;
    }
  }
  
  public View getContentView()
  {
    return mContentView;
  }
  
  public void hide()
  {
    if (!isShowing()) {
      return;
    }
    ((WindowManager)mContext.getSystemService("window")).removeView(mContentView);
  }
  
  public boolean isShowing()
  {
    boolean bool;
    if (mContentView.getParent() != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void show(View paramView, int paramInt1, int paramInt2, boolean paramBoolean, CharSequence paramCharSequence)
  {
    if (isShowing()) {
      hide();
    }
    mMessageView.setText(paramCharSequence);
    computePosition(paramView, paramInt1, paramInt2, paramBoolean, mLayoutParams);
    ((WindowManager)mContext.getSystemService("window")).addView(mContentView, mLayoutParams);
  }
}
