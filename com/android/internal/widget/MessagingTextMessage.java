package com.android.internal.widget;

import android.app.Notification.MessagingStyle.Message;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Pools.SimplePool;
import android.util.Pools.SynchronizedPool;
import android.view.LayoutInflater;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class MessagingTextMessage
  extends ImageFloatingTextView
  implements MessagingMessage
{
  private static Pools.SimplePool<MessagingTextMessage> sInstancePool = new Pools.SynchronizedPool(20);
  private final MessagingMessageState mState = new MessagingMessageState(this);
  
  public MessagingTextMessage(Context paramContext)
  {
    super(paramContext);
  }
  
  public MessagingTextMessage(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public MessagingTextMessage(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public MessagingTextMessage(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  static MessagingMessage createMessage(MessagingLayout paramMessagingLayout, Notification.MessagingStyle.Message paramMessage)
  {
    MessagingLinearLayout localMessagingLinearLayout = paramMessagingLayout.getMessagingLinearLayout();
    MessagingTextMessage localMessagingTextMessage1 = (MessagingTextMessage)sInstancePool.acquire();
    MessagingTextMessage localMessagingTextMessage2 = localMessagingTextMessage1;
    if (localMessagingTextMessage1 == null)
    {
      localMessagingTextMessage2 = (MessagingTextMessage)LayoutInflater.from(paramMessagingLayout.getContext()).inflate(17367229, localMessagingLinearLayout, false);
      localMessagingTextMessage2.addOnLayoutChangeListener(MessagingLayout.MESSAGING_PROPERTY_ANIMATOR);
    }
    localMessagingTextMessage2.setMessage(paramMessage);
    return localMessagingTextMessage2;
  }
  
  public static void dropCache()
  {
    sInstancePool = new Pools.SynchronizedPool(10);
  }
  
  public int getConsumedLines()
  {
    return getLineCount();
  }
  
  public int getLayoutHeight()
  {
    Layout localLayout = getLayout();
    if (localLayout == null) {
      return 0;
    }
    return localLayout.getHeight();
  }
  
  public int getMeasuredType()
  {
    int i;
    if (getMeasuredHeight() < getLayoutHeight() + getPaddingTop() + getPaddingBottom()) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) && (getLineCount() <= 1)) {
      return 2;
    }
    Layout localLayout = getLayout();
    if (localLayout == null) {
      return 2;
    }
    if (localLayout.getEllipsisCount(localLayout.getLineCount() - 1) > 0) {
      return 1;
    }
    return 0;
  }
  
  public MessagingMessageState getState()
  {
    return mState;
  }
  
  public void recycle()
  {
    super.recycle();
    sInstancePool.release(this);
  }
  
  public void setColor(int paramInt)
  {
    setTextColor(paramInt);
  }
  
  public void setMaxDisplayedLines(int paramInt)
  {
    setMaxLines(paramInt);
  }
  
  public boolean setMessage(Notification.MessagingStyle.Message paramMessage)
  {
    super.setMessage(paramMessage);
    setText(paramMessage.getText());
    return true;
  }
}
