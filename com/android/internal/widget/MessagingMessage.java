package com.android.internal.widget;

import android.app.ActivityManager;
import android.app.Notification.MessagingStyle.Message;
import android.view.View;
import java.util.Objects;

public abstract interface MessagingMessage
  extends MessagingLinearLayout.MessagingChild
{
  public static final String IMAGE_MIME_TYPE_PREFIX = "image/";
  
  public static MessagingMessage createMessage(MessagingLayout paramMessagingLayout, Notification.MessagingStyle.Message paramMessage)
  {
    if ((hasImage(paramMessage)) && (!ActivityManager.isLowRamDeviceStatic())) {
      return MessagingImageMessage.createMessage(paramMessagingLayout, paramMessage);
    }
    return MessagingTextMessage.createMessage(paramMessagingLayout, paramMessage);
  }
  
  public static void dropCache()
  {
    MessagingTextMessage.dropCache();
    MessagingImageMessage.dropCache();
  }
  
  public static boolean hasImage(Notification.MessagingStyle.Message paramMessage)
  {
    boolean bool;
    if ((paramMessage.getDataUri() != null) && (paramMessage.getDataMimeType() != null) && (paramMessage.getDataMimeType().startsWith("image/"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public MessagingGroup getGroup()
  {
    return getState().getGroup();
  }
  
  public Notification.MessagingStyle.Message getMessage()
  {
    return getState().getMessage();
  }
  
  public abstract MessagingMessageState getState();
  
  public View getView()
  {
    return (View)this;
  }
  
  public abstract int getVisibility();
  
  public boolean hasOverlappingRendering()
  {
    return false;
  }
  
  public void hideAnimated()
  {
    setIsHidingAnimated(true);
    getGroup().performRemoveAnimation(getView(), new _..Lambda.MessagingMessage.goi5oiwdlMBbUvfJzNl7fGbZ_K0(this));
  }
  
  public boolean isHidingAnimated()
  {
    return getState().isHidingAnimated();
  }
  
  public void recycle()
  {
    getState().recycle();
  }
  
  public void removeMessage()
  {
    getGroup().removeMessage(this);
  }
  
  public boolean sameAs(Notification.MessagingStyle.Message paramMessage)
  {
    Notification.MessagingStyle.Message localMessage = getMessage();
    if (!Objects.equals(paramMessage.getText(), localMessage.getText())) {
      return false;
    }
    if (!Objects.equals(paramMessage.getSender(), localMessage.getSender())) {
      return false;
    }
    int i;
    if (paramMessage.isRemoteInputHistory() != localMessage.isRemoteInputHistory()) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i == 0) && (!Objects.equals(Long.valueOf(paramMessage.getTimestamp()), Long.valueOf(localMessage.getTimestamp())))) {
      return false;
    }
    if (!Objects.equals(paramMessage.getDataMimeType(), localMessage.getDataMimeType())) {
      return false;
    }
    return Objects.equals(paramMessage.getDataUri(), localMessage.getDataUri());
  }
  
  public boolean sameAs(MessagingMessage paramMessagingMessage)
  {
    return sameAs(paramMessagingMessage.getMessage());
  }
  
  public void setColor(int paramInt) {}
  
  public void setIsHidingAnimated(boolean paramBoolean)
  {
    getState().setIsHidingAnimated(paramBoolean);
  }
  
  public void setIsHistoric(boolean paramBoolean)
  {
    getState().setIsHistoric(paramBoolean);
  }
  
  public boolean setMessage(Notification.MessagingStyle.Message paramMessage)
  {
    getState().setMessage(paramMessage);
    return true;
  }
  
  public void setMessagingGroup(MessagingGroup paramMessagingGroup)
  {
    getState().setGroup(paramMessagingGroup);
  }
  
  public abstract void setVisibility(int paramInt);
}
