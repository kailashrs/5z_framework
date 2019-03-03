package com.android.internal.widget;

import android.app.Notification.MessagingStyle.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class MessagingMessageState
{
  private MessagingGroup mGroup;
  private final View mHostView;
  private boolean mIsHidingAnimated;
  private boolean mIsHistoric;
  private Notification.MessagingStyle.Message mMessage;
  
  MessagingMessageState(View paramView)
  {
    mHostView = paramView;
  }
  
  public MessagingGroup getGroup()
  {
    return mGroup;
  }
  
  public View getHostView()
  {
    return mHostView;
  }
  
  public Notification.MessagingStyle.Message getMessage()
  {
    return mMessage;
  }
  
  public boolean isHidingAnimated()
  {
    return mIsHidingAnimated;
  }
  
  public void recycle()
  {
    mHostView.setAlpha(1.0F);
    mHostView.setTranslationY(0.0F);
    MessagingPropertyAnimator.recycle(mHostView);
    mIsHidingAnimated = false;
    mIsHistoric = false;
    mGroup = null;
    mMessage = null;
  }
  
  public void setGroup(MessagingGroup paramMessagingGroup)
  {
    mGroup = paramMessagingGroup;
  }
  
  public void setIsHidingAnimated(boolean paramBoolean)
  {
    ViewParent localViewParent = mHostView.getParent();
    mIsHidingAnimated = paramBoolean;
    mHostView.invalidate();
    if ((localViewParent instanceof ViewGroup)) {
      ((ViewGroup)localViewParent).invalidate();
    }
  }
  
  public void setIsHistoric(boolean paramBoolean)
  {
    mIsHistoric = paramBoolean;
  }
  
  public void setMessage(Notification.MessagingStyle.Message paramMessage)
  {
    mMessage = paramMessage;
  }
}
