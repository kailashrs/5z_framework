package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountItemView
  extends LinearLayout
{
  private ImageView mAccountIcon;
  private TextView mAccountName;
  private TextView mAccountNumber;
  
  public AccountItemView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AccountItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(17367310, null);
    addView(paramContext);
    initViewItem(paramContext);
  }
  
  private void initViewItem(View paramView)
  {
    mAccountIcon = ((ImageView)paramView.findViewById(16908294));
    mAccountName = ((TextView)paramView.findViewById(16908310));
    mAccountNumber = ((TextView)paramView.findViewById(16908304));
  }
  
  private void setText(TextView paramTextView, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      paramTextView.setVisibility(8);
    }
    else
    {
      paramTextView.setText(paramString);
      paramTextView.setVisibility(0);
    }
  }
  
  public void setAccountIcon(int paramInt)
  {
    mAccountIcon.setImageResource(paramInt);
  }
  
  public void setAccountIcon(Drawable paramDrawable)
  {
    mAccountIcon.setBackgroundDrawable(paramDrawable);
  }
  
  public void setAccountName(String paramString)
  {
    setText(mAccountName, paramString);
  }
  
  public void setAccountNumber(String paramString)
  {
    setText(mAccountNumber, paramString);
  }
  
  public void setViewItem(AccountViewAdapter.AccountElements paramAccountElements)
  {
    Drawable localDrawable = paramAccountElements.getDrawable();
    if (localDrawable != null) {
      setAccountIcon(localDrawable);
    } else {
      setAccountIcon(paramAccountElements.getIcon());
    }
    setAccountName(paramAccountElements.getName());
    setAccountNumber(paramAccountElements.getNumber());
  }
}
