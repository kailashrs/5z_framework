package com.android.internal.widget;

import android.app.Notification.MessagingStyle.Message;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pools.SimplePool;
import android.util.Pools.SynchronizedPool;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;
import java.io.IOException;

@RemoteViews.RemoteView
public class MessagingImageMessage
  extends ImageView
  implements MessagingMessage
{
  private static final String TAG = "MessagingImageMessage";
  private static Pools.SimplePool<MessagingImageMessage> sInstancePool = new Pools.SynchronizedPool(10);
  private int mActualHeight;
  private int mActualWidth;
  private float mAspectRatio;
  private Drawable mDrawable;
  private final int mExtraSpacing;
  private final int mImageRounding;
  private boolean mIsIsolated;
  private final int mIsolatedSize;
  private final int mMaxImageHeight;
  private final int mMinImageHeight;
  private final Path mPath = new Path();
  private final MessagingMessageState mState = new MessagingMessageState(this);
  
  public MessagingImageMessage(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MessagingImageMessage(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public MessagingImageMessage(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MessagingImageMessage(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mMinImageHeight = paramContext.getResources().getDimensionPixelSize(17105295);
    mMaxImageHeight = paramContext.getResources().getDimensionPixelSize(17105294);
    mImageRounding = paramContext.getResources().getDimensionPixelSize(17105296);
    mExtraSpacing = paramContext.getResources().getDimensionPixelSize(17105293);
    setMaxHeight(mMaxImageHeight);
    mIsolatedSize = getResources().getDimensionPixelSize(17105291);
  }
  
  static MessagingMessage createMessage(MessagingLayout paramMessagingLayout, Notification.MessagingStyle.Message paramMessage)
  {
    MessagingLinearLayout localMessagingLinearLayout = paramMessagingLayout.getMessagingLinearLayout();
    MessagingImageMessage localMessagingImageMessage1 = (MessagingImageMessage)sInstancePool.acquire();
    MessagingImageMessage localMessagingImageMessage2 = localMessagingImageMessage1;
    if (localMessagingImageMessage1 == null)
    {
      localMessagingImageMessage2 = (MessagingImageMessage)LayoutInflater.from(paramMessagingLayout.getContext()).inflate(17367228, localMessagingLinearLayout, false);
      localMessagingImageMessage2.addOnLayoutChangeListener(MessagingLayout.MESSAGING_PROPERTY_ANIMATOR);
    }
    if (!localMessagingImageMessage2.setMessage(paramMessage))
    {
      localMessagingImageMessage2.recycle();
      return MessagingTextMessage.createMessage(paramMessagingLayout, paramMessage);
    }
    return localMessagingImageMessage2;
  }
  
  public static void dropCache()
  {
    sInstancePool = new Pools.SynchronizedPool(10);
  }
  
  public int getActualHeight()
  {
    return mActualHeight;
  }
  
  public int getActualWidth()
  {
    return mActualWidth;
  }
  
  public int getConsumedLines()
  {
    return 3;
  }
  
  public int getExtraSpacing()
  {
    return mExtraSpacing;
  }
  
  public int getMeasuredType()
  {
    int i = getMeasuredHeight();
    int j;
    if (mIsIsolated) {
      j = mIsolatedSize;
    } else {
      j = mMinImageHeight;
    }
    if ((i < j) && (i != mDrawable.getIntrinsicHeight())) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0) {
      return 2;
    }
    if ((!mIsIsolated) && (i != mDrawable.getIntrinsicHeight())) {
      return 1;
    }
    return 0;
  }
  
  public Path getRoundedRectPath()
  {
    int i = getActualWidth();
    int j = getActualHeight();
    mPath.reset();
    float f1 = mImageRounding;
    float f2 = mImageRounding;
    f1 = Math.min((i - 0) / 2, f1);
    f2 = Math.min((j - 0) / 2, f2);
    mPath.moveTo(0, 0 + f2);
    mPath.quadTo(0, 0, 0 + f1, 0);
    mPath.lineTo(i - f1, 0);
    mPath.quadTo(i, 0, i, 0 + f2);
    mPath.lineTo(i, j - f2);
    mPath.quadTo(i, j, i - f1, j);
    mPath.lineTo(0 + f1, j);
    mPath.quadTo(0, j, 0, j - f2);
    mPath.close();
    return mPath;
  }
  
  public MessagingMessageState getState()
  {
    return mState;
  }
  
  public int getStaticWidth()
  {
    if (mIsIsolated) {
      return getWidth();
    }
    return (int)(getHeight() * mAspectRatio);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    paramCanvas.save();
    paramCanvas.clipPath(getRoundedRectPath());
    int i = (int)Math.max(getActualWidth(), getActualHeight() * mAspectRatio);
    int j = (int)(i / mAspectRatio);
    int k = (int)((getActualWidth() - i) / 2.0F);
    mDrawable.setBounds(k, 0, k + i, j);
    mDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    setActualWidth(getStaticWidth());
    setActualHeight(getHeight());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (mIsIsolated) {
      setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
    }
  }
  
  public void recycle()
  {
    super.recycle();
    setImageBitmap(null);
    mDrawable = null;
    sInstancePool.release(this);
  }
  
  public void setActualHeight(int paramInt)
  {
    mActualHeight = paramInt;
    invalidate();
  }
  
  public void setActualWidth(int paramInt)
  {
    mActualWidth = paramInt;
    invalidate();
  }
  
  public void setIsolated(boolean paramBoolean)
  {
    if (mIsIsolated != paramBoolean)
    {
      mIsIsolated = paramBoolean;
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)getLayoutParams();
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = mExtraSpacing;
      }
      topMargin = i;
      setLayoutParams(localMarginLayoutParams);
    }
  }
  
  public void setMaxDisplayedLines(int paramInt) {}
  
  public boolean setMessage(Notification.MessagingStyle.Message paramMessage)
  {
    super.setMessage(paramMessage);
    try
    {
      Drawable localDrawable = LocalImageResolver.resolveImage(paramMessage.getDataUri(), getContext());
      int i = localDrawable.getIntrinsicHeight();
      if (i == 0)
      {
        Log.w("MessagingImageMessage", "Drawable with 0 intrinsic height was returned");
        return false;
      }
      mDrawable = localDrawable;
      mAspectRatio = (mDrawable.getIntrinsicWidth() / i);
      setImageDrawable(localDrawable);
      setContentDescription(paramMessage.getText());
      return true;
    }
    catch (IOException|SecurityException paramMessage)
    {
      paramMessage.printStackTrace();
    }
    return false;
  }
}
