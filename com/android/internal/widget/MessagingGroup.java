package com.android.internal.widget;

import android.app.Notification.MessagingStyle.Message;
import android.app.Person;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pools.SimplePool;
import android.util.Pools.SynchronizedPool;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews.RemoteView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RemoteViews.RemoteView
public class MessagingGroup
  extends LinearLayout
  implements MessagingLinearLayout.MessagingChild
{
  private static Pools.SimplePool<MessagingGroup> sInstancePool = new Pools.SynchronizedPool(10);
  private ArrayList<MessagingMessage> mAddedMessages = new ArrayList();
  private Icon mAvatarIcon;
  private CharSequence mAvatarName = "";
  private String mAvatarSymbol = "";
  private ImageView mAvatarView;
  private Point mDisplaySize = new Point();
  private boolean mFirstLayout;
  private ViewGroup mImageContainer;
  private boolean mImagesAtEnd;
  private boolean mIsHidingAnimated;
  private MessagingImageMessage mIsolatedMessage;
  private int mLayoutColor;
  private MessagingLinearLayout mMessageContainer;
  private List<MessagingMessage> mMessages;
  private boolean mNeedsGeneratedAvatar;
  private Person mSender;
  private ImageFloatingTextView mSenderName;
  private ProgressBar mSendingSpinner;
  private View mSendingSpinnerContainer;
  private int mSendingTextColor;
  private int mTextColor;
  private boolean mTransformingImages;
  
  public MessagingGroup(Context paramContext)
  {
    super(paramContext);
  }
  
  public MessagingGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public MessagingGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public MessagingGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private int calculateSendingTextColor()
  {
    TypedValue localTypedValue = new TypedValue();
    mContext.getResources().getValue(17105354, localTypedValue, true);
    float f = localTypedValue.getFloat();
    return Color.valueOf(Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor), f).toArgb();
  }
  
  static MessagingGroup createGroup(MessagingLinearLayout paramMessagingLinearLayout)
  {
    MessagingGroup localMessagingGroup1 = (MessagingGroup)sInstancePool.acquire();
    MessagingGroup localMessagingGroup2 = localMessagingGroup1;
    if (localMessagingGroup1 == null)
    {
      localMessagingGroup2 = (MessagingGroup)LayoutInflater.from(paramMessagingLinearLayout.getContext()).inflate(17367227, paramMessagingLinearLayout, false);
      localMessagingGroup2.addOnLayoutChangeListener(MessagingLayout.MESSAGING_PROPERTY_ANIMATOR);
    }
    paramMessagingLinearLayout.addView(localMessagingGroup2);
    return localMessagingGroup2;
  }
  
  public static void dropCache()
  {
    sInstancePool = new Pools.SynchronizedPool(10);
  }
  
  private int getDistanceFromParent(View paramView, ViewGroup paramViewGroup)
  {
    int i = 0;
    while (paramView != paramViewGroup)
    {
      i = (int)(i + (paramView.getTop() + paramView.getTranslationY()));
      paramView = (View)paramView.getParent();
    }
    return i;
  }
  
  private void performRemoveAnimation(View paramView, int paramInt, Runnable paramRunnable)
  {
    MessagingPropertyAnimator.startLocalTranslationTo(paramView, paramInt, MessagingLayout.FAST_OUT_LINEAR_IN);
    MessagingPropertyAnimator.fadeOut(paramView, paramRunnable);
  }
  
  private boolean removeFromParentIfDifferent(MessagingMessage paramMessagingMessage, ViewGroup paramViewGroup)
  {
    ViewParent localViewParent = paramMessagingMessage.getView().getParent();
    if (localViewParent != paramViewGroup)
    {
      if ((localViewParent instanceof ViewGroup)) {
        ((ViewGroup)localViewParent).removeView(paramMessagingMessage.getView());
      }
      return true;
    }
    return false;
  }
  
  private void setIsHidingAnimated(boolean paramBoolean)
  {
    ViewParent localViewParent = getParent();
    mIsHidingAnimated = paramBoolean;
    invalidate();
    if ((localViewParent instanceof ViewGroup)) {
      ((ViewGroup)localViewParent).invalidate();
    }
  }
  
  private void updateImageContainerVisibility()
  {
    ViewGroup localViewGroup = mImageContainer;
    int i;
    if ((mIsolatedMessage != null) && (mImagesAtEnd)) {
      i = 0;
    } else {
      i = 8;
    }
    localViewGroup.setVisibility(i);
  }
  
  private void updateMessageColor()
  {
    if (mMessages != null)
    {
      int i;
      if (mSendingSpinnerContainer.getVisibility() == 0) {
        i = mSendingTextColor;
      } else {
        i = mTextColor;
      }
      Iterator localIterator = mMessages.iterator();
      while (localIterator.hasNext())
      {
        MessagingMessage localMessagingMessage = (MessagingMessage)localIterator.next();
        int j;
        if (localMessagingMessage.getMessage().isRemoteInputHistory()) {
          j = i;
        } else {
          j = mTextColor;
        }
        localMessagingMessage.setColor(j);
      }
    }
  }
  
  public int calculateGroupCompatibility(MessagingGroup paramMessagingGroup)
  {
    boolean bool = TextUtils.equals(getSenderName(), paramMessagingGroup.getSenderName());
    int i = 0;
    if (bool)
    {
      int j = 1;
      while ((i < mMessages.size()) && (i < mMessages.size()))
      {
        if (!((MessagingMessage)mMessages.get(mMessages.size() - 1 - i)).sameAs((MessagingMessage)mMessages.get(mMessages.size() - 1 - i))) {
          return j;
        }
        j++;
        i++;
      }
      return j;
    }
    return 0;
  }
  
  public View getAvatar()
  {
    return mAvatarView;
  }
  
  public Icon getAvatarSymbolIfMatching(CharSequence paramCharSequence, String paramString, int paramInt)
  {
    if ((mAvatarName.equals(paramCharSequence)) && (mAvatarSymbol.equals(paramString)) && (paramInt == mLayoutColor)) {
      return mAvatarIcon;
    }
    return null;
  }
  
  public int getConsumedLines()
  {
    int i = 0;
    int j = 0;
    while (j < mMessageContainer.getChildCount())
    {
      View localView = mMessageContainer.getChildAt(j);
      int k = i;
      if ((localView instanceof MessagingLinearLayout.MessagingChild)) {
        k = i + ((MessagingLinearLayout.MessagingChild)localView).getConsumedLines();
      }
      j++;
      i = k;
    }
    if (mIsolatedMessage != null) {
      i = Math.max(i, 1);
    }
    return i + 1;
  }
  
  public MessagingImageMessage getIsolatedMessage()
  {
    return mIsolatedMessage;
  }
  
  public int getMeasuredType()
  {
    if (mIsolatedMessage != null) {
      return 1;
    }
    int i = 0;
    int j = mMessageContainer.getChildCount() - 1;
    for (;;)
    {
      int k = 0;
      if (j < 0) {
        break;
      }
      View localView = mMessageContainer.getChildAt(j);
      int m;
      if (localView.getVisibility() == 8)
      {
        m = i;
      }
      else
      {
        m = i;
        if ((localView instanceof MessagingLinearLayout.MessagingChild))
        {
          int n = ((MessagingLinearLayout.MessagingChild)localView).getMeasuredType();
          m = k;
          if (n == 2) {
            m = 1;
          }
          if ((m | getLayoutParamshide) != 0)
          {
            if (i != 0) {
              return 1;
            }
            return 2;
          }
          if (n == 1) {
            return 1;
          }
          m = 1;
        }
      }
      j--;
      i = m;
    }
    return 0;
  }
  
  public MessagingLinearLayout getMessageContainer()
  {
    return mMessageContainer;
  }
  
  public List<MessagingMessage> getMessages()
  {
    return mMessages;
  }
  
  public Person getSender()
  {
    return mSender;
  }
  
  public CharSequence getSenderName()
  {
    return mSenderName.getText();
  }
  
  public View getSenderView()
  {
    return mSenderName;
  }
  
  public boolean hasOverlappingRendering()
  {
    return false;
  }
  
  public void hideAnimated()
  {
    setIsHidingAnimated(true);
    removeGroupAnimated(new _..Lambda.MessagingGroup.buM2CBWR7uz4neT0lee_MKMDx5M(this));
  }
  
  public boolean isHidingAnimated()
  {
    return mIsHidingAnimated;
  }
  
  public boolean needsGeneratedAvatar()
  {
    return mNeedsGeneratedAvatar;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mMessageContainer = ((MessagingLinearLayout)findViewById(16908991));
    mSenderName = ((ImageFloatingTextView)findViewById(16909121));
    mAvatarView = ((ImageView)findViewById(16909120));
    mImageContainer = ((ViewGroup)findViewById(16909123));
    mSendingSpinner = ((ProgressBar)findViewById(16909124));
    mSendingSpinnerContainer = findViewById(16909125);
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    mDisplaySize.x = widthPixels;
    mDisplaySize.y = heightPixels;
  }
  
  protected void onLayout(final boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!mAddedMessages.isEmpty())
    {
      paramBoolean = mFirstLayout;
      getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
      {
        public boolean onPreDraw()
        {
          Iterator localIterator = mAddedMessages.iterator();
          while (localIterator.hasNext())
          {
            MessagingMessage localMessagingMessage = (MessagingMessage)localIterator.next();
            if (localMessagingMessage.getView().isShown())
            {
              MessagingPropertyAnimator.fadeIn(localMessagingMessage.getView());
              if (!paramBoolean) {
                MessagingPropertyAnimator.startLocalTranslationFrom(localMessagingMessage.getView(), localMessagingMessage.getView().getHeight(), MessagingLayout.LINEAR_OUT_SLOW_IN);
              }
            }
          }
          mAddedMessages.clear();
          getViewTreeObserver().removeOnPreDrawListener(this);
          return true;
        }
      });
    }
    mFirstLayout = false;
    updateClipRect();
  }
  
  public void performRemoveAnimation(View paramView, Runnable paramRunnable)
  {
    performRemoveAnimation(paramView, -paramView.getHeight(), paramRunnable);
  }
  
  public void recycle()
  {
    if (mIsolatedMessage != null) {
      mImageContainer.removeView(mIsolatedMessage);
    }
    for (int i = 0; i < mMessages.size(); i++)
    {
      MessagingMessage localMessagingMessage = (MessagingMessage)mMessages.get(i);
      mMessageContainer.removeView(localMessagingMessage.getView());
      localMessagingMessage.recycle();
    }
    setAvatar(null);
    mAvatarView.setAlpha(1.0F);
    mAvatarView.setTranslationY(0.0F);
    mSenderName.setAlpha(1.0F);
    mSenderName.setTranslationY(0.0F);
    setAlpha(1.0F);
    mIsolatedMessage = null;
    mMessages = null;
    mAddedMessages.clear();
    mFirstLayout = true;
    MessagingPropertyAnimator.recycle(this);
    sInstancePool.release(this);
  }
  
  public void removeGroupAnimated(Runnable paramRunnable)
  {
    performRemoveAnimation(this, new _..Lambda.MessagingGroup.QKnXYzCylYJqF8wEQG98SXlcu2M(this, paramRunnable));
  }
  
  public void removeMessage(MessagingMessage paramMessagingMessage)
  {
    View localView = paramMessagingMessage.getView();
    boolean bool = localView.isShown();
    ViewGroup localViewGroup = (ViewGroup)localView.getParent();
    if (localViewGroup == null) {
      return;
    }
    localViewGroup.removeView(localView);
    paramMessagingMessage = new _..Lambda.MessagingGroup.uEKViIlAuE6AYNmbbTgLGe5mU7I(localViewGroup, localView, paramMessagingMessage);
    if ((bool) && (!MessagingLinearLayout.isGone(localView)))
    {
      localViewGroup.addTransientView(localView, 0);
      performRemoveAnimation(localView, paramMessagingMessage);
    }
    else
    {
      paramMessagingMessage.run();
    }
  }
  
  public void setAvatar(Icon paramIcon)
  {
    mAvatarIcon = paramIcon;
    mAvatarView.setImageIcon(paramIcon);
    mAvatarSymbol = "";
    mAvatarName = "";
  }
  
  public void setCreatedAvatar(Icon paramIcon, CharSequence paramCharSequence, String paramString, int paramInt)
  {
    if ((!mAvatarName.equals(paramCharSequence)) || (!mAvatarSymbol.equals(paramString)) || (paramInt != mLayoutColor))
    {
      setAvatar(paramIcon);
      mAvatarSymbol = paramString;
      setLayoutColor(paramInt);
      mAvatarName = paramCharSequence;
    }
  }
  
  public void setDisplayImagesAtEnd(boolean paramBoolean)
  {
    if (mImagesAtEnd != paramBoolean)
    {
      mImagesAtEnd = paramBoolean;
      updateImageContainerVisibility();
    }
  }
  
  public void setLayoutColor(int paramInt)
  {
    if (paramInt != mLayoutColor)
    {
      mLayoutColor = paramInt;
      mSendingSpinner.setIndeterminateTintList(ColorStateList.valueOf(mLayoutColor));
    }
  }
  
  public void setMaxDisplayedLines(int paramInt)
  {
    mMessageContainer.setMaxDisplayedLines(paramInt);
  }
  
  public void setMessages(List<MessagingMessage> paramList)
  {
    MessagingImageMessage localMessagingImageMessage = null;
    int i = 0;
    for (int j = 0; j < paramList.size(); j++)
    {
      MessagingMessage localMessagingMessage = (MessagingMessage)paramList.get(j);
      if (localMessagingMessage.getGroup() != this)
      {
        localMessagingMessage.setMessagingGroup(this);
        mAddedMessages.add(localMessagingMessage);
      }
      boolean bool = localMessagingMessage instanceof MessagingImageMessage;
      if ((mImagesAtEnd) && (bool))
      {
        localMessagingImageMessage = (MessagingImageMessage)localMessagingMessage;
      }
      else
      {
        if (removeFromParentIfDifferent(localMessagingMessage, mMessageContainer))
        {
          ViewGroup.LayoutParams localLayoutParams = localMessagingMessage.getView().getLayoutParams();
          if ((localLayoutParams != null) && (!(localLayoutParams instanceof MessagingLinearLayout.LayoutParams))) {
            localMessagingMessage.getView().setLayoutParams(mMessageContainer.generateDefaultLayoutParams());
          }
          mMessageContainer.addView(localMessagingMessage.getView(), i);
        }
        if (bool) {
          ((MessagingImageMessage)localMessagingMessage).setIsolated(false);
        }
        if (i != mMessageContainer.indexOfChild(localMessagingMessage.getView()))
        {
          mMessageContainer.removeView(localMessagingMessage.getView());
          mMessageContainer.addView(localMessagingMessage.getView(), i);
        }
        i++;
      }
    }
    if (localMessagingImageMessage != null)
    {
      if (removeFromParentIfDifferent(localMessagingImageMessage, mImageContainer))
      {
        mImageContainer.removeAllViews();
        mImageContainer.addView(localMessagingImageMessage.getView());
      }
      localMessagingImageMessage.setIsolated(true);
    }
    else if (mIsolatedMessage != null)
    {
      mImageContainer.removeAllViews();
    }
    mIsolatedMessage = localMessagingImageMessage;
    updateImageContainerVisibility();
    mMessages = paramList;
    updateMessageColor();
  }
  
  public void setSender(Person paramPerson, CharSequence paramCharSequence)
  {
    mSender = paramPerson;
    CharSequence localCharSequence = paramCharSequence;
    if (paramCharSequence == null) {
      localCharSequence = paramPerson.getName();
    }
    mSenderName.setText(localCharSequence);
    paramCharSequence = paramPerson.getIcon();
    int i = 0;
    boolean bool;
    if (paramCharSequence == null) {
      bool = true;
    } else {
      bool = false;
    }
    mNeedsGeneratedAvatar = bool;
    if (!mNeedsGeneratedAvatar) {
      setAvatar(paramPerson.getIcon());
    }
    mAvatarView.setVisibility(0);
    paramPerson = mSenderName;
    if (TextUtils.isEmpty(localCharSequence)) {
      i = 8;
    }
    paramPerson.setVisibility(i);
  }
  
  public void setSending(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 8;
    }
    if (mSendingSpinnerContainer.getVisibility() != i)
    {
      mSendingSpinnerContainer.setVisibility(i);
      updateMessageColor();
    }
  }
  
  public void setTextColors(int paramInt1, int paramInt2)
  {
    mTextColor = paramInt2;
    mSendingTextColor = calculateSendingTextColor();
    updateMessageColor();
    mSenderName.setTextColor(paramInt1);
  }
  
  public void setTransformingImages(boolean paramBoolean)
  {
    mTransformingImages = paramBoolean;
  }
  
  public void updateClipRect()
  {
    Object localObject;
    if ((mSenderName.getVisibility() != 8) && (!mTransformingImages))
    {
      localObject = (ViewGroup)mSenderName.getParent();
      int i = getDistanceFromParent(mSenderName, (ViewGroup)localObject);
      int j = getDistanceFromParent(mMessageContainer, (ViewGroup)localObject);
      int k = mSenderName.getHeight();
      int m = Math.max(mDisplaySize.x, mDisplaySize.y);
      localObject = new Rect(0, i - j + k, m, m);
    }
    else
    {
      localObject = null;
    }
    mMessageContainer.setClipBounds((Rect)localObject);
  }
}
