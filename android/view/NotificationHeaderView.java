package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.widget.CachingIconView;
import java.util.ArrayList;

@RemoteViews.RemoteView
public class NotificationHeaderView
  extends ViewGroup
{
  public static final int NO_COLOR = 1;
  private boolean mAcceptAllTouches;
  private View mAppName;
  private View mAppOps;
  private View.OnClickListener mAppOpsListener;
  private Drawable mBackground;
  private View mCameraIcon;
  private final int mChildMinWidth;
  private final int mContentEndMargin;
  private boolean mEntireHeaderClickable;
  private ImageView mExpandButton;
  private View.OnClickListener mExpandClickListener;
  private boolean mExpandOnlyOnButton;
  private boolean mExpanded;
  private final int mGravity;
  private View mHeaderText;
  private CachingIconView mIcon;
  private int mIconColor;
  private View mMicIcon;
  private int mOriginalNotificationColor;
  private View mOverlayIcon;
  private View mProfileBadge;
  ViewOutlineProvider mProvider = new ViewOutlineProvider()
  {
    public void getOutline(View paramAnonymousView, Outline paramAnonymousOutline)
    {
      if (mBackground != null)
      {
        paramAnonymousOutline.setRect(0, 0, getWidth(), getHeight());
        paramAnonymousOutline.setAlpha(1.0F);
      }
    }
  };
  private View mSecondaryHeaderText;
  private boolean mShowExpandButtonAtEnd;
  private boolean mShowWorkBadgeAtEnd;
  private int mTotalWidth;
  private HeaderTouchListener mTouchListener = new HeaderTouchListener();
  
  public NotificationHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public NotificationHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public NotificationHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public NotificationHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    Resources localResources = getResources();
    mChildMinWidth = localResources.getDimensionPixelSize(17105338);
    mContentEndMargin = localResources.getDimensionPixelSize(17105317);
    mEntireHeaderClickable = localResources.getBoolean(17957002);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, new int[] { 16842927 }, paramInt1, paramInt2);
    mGravity = paramContext.getInt(0, 0);
    paramContext.recycle();
  }
  
  private View getFirstChildNotGone()
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() != 8) {
        return localView;
      }
    }
    return this;
  }
  
  private int shrinkViewForOverflow(int paramInt1, int paramInt2, View paramView, int paramInt3)
  {
    int i = paramView.getMeasuredWidth();
    int j = paramInt2;
    if (paramInt2 > 0)
    {
      j = paramInt2;
      if (paramView.getVisibility() != 8)
      {
        j = paramInt2;
        if (i > paramInt3)
        {
          paramInt3 = Math.max(paramInt3, i - paramInt2);
          paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt3, Integer.MIN_VALUE), paramInt1);
          j = paramInt2 - (i - paramInt3);
        }
      }
    }
    return j;
  }
  
  private void updateExpandButton()
  {
    int i;
    int j;
    if (mExpanded)
    {
      i = 17302582;
      j = 17039940;
    }
    else
    {
      i = 17302641;
      j = 17039939;
    }
    mExpandButton.setImageDrawable(getContext().getDrawable(i));
    mExpandButton.setColorFilter(mOriginalNotificationColor);
    mExpandButton.setContentDescription(mContext.getText(j));
  }
  
  private void updateTouchListener()
  {
    if ((mExpandClickListener == null) && (mAppOpsListener == null))
    {
      setOnTouchListener(null);
      return;
    }
    setOnTouchListener(mTouchListener);
    mTouchListener.bindTouchRects();
  }
  
  protected void drawableStateChanged()
  {
    if ((mBackground != null) && (mBackground.isStateful())) {
      mBackground.setState(getDrawableState());
    }
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  public ImageView getExpandButton()
  {
    return mExpandButton;
  }
  
  public CachingIconView getIcon()
  {
    return mIcon;
  }
  
  public int getOriginalIconColor()
  {
    return mIconColor;
  }
  
  public int getOriginalNotificationColor()
  {
    return mOriginalNotificationColor;
  }
  
  public View getWorkProfileIcon()
  {
    return mProfileBadge;
  }
  
  public boolean hasOverlappingRendering()
  {
    return false;
  }
  
  public boolean isInTouchRect(float paramFloat1, float paramFloat2)
  {
    if (mExpandClickListener == null) {
      return false;
    }
    return mTouchListener.isInside(paramFloat1, paramFloat2);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (mBackground != null)
    {
      mBackground.setBounds(0, 0, getWidth(), getHeight());
      mBackground.draw(paramCanvas);
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mAppName = findViewById(16908744);
    mHeaderText = findViewById(16908998);
    mSecondaryHeaderText = findViewById(16909000);
    mExpandButton = ((ImageView)findViewById(16908922));
    mIcon = ((CachingIconView)findViewById(16908294));
    mProfileBadge = findViewById(16909262);
    mCameraIcon = findViewById(16908839);
    mMicIcon = findViewById(16909126);
    mOverlayIcon = findViewById(16909213);
    mAppOps = findViewById(16908745);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt4 = getPaddingStart();
    paramInt3 = getMeasuredWidth();
    paramInt1 = mGravity;
    int i = 0;
    if ((paramInt1 & 0x1) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    paramInt2 = paramInt4;
    if (paramInt1 != 0) {
      paramInt2 = paramInt4 + (getMeasuredWidth() / 2 - mTotalWidth / 2);
    }
    int j = getChildCount();
    int k = getMeasuredHeight();
    int m = getPaddingTop();
    int n = getPaddingBottom();
    paramInt1 = paramInt3;
    while (i < j)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() != 8)
      {
        int i1 = localView.getMeasuredHeight();
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)localView.getLayoutParams();
        int i2 = paramInt2 + localMarginLayoutParams.getMarginStart();
        int i3 = localView.getMeasuredWidth() + i2;
        int i4 = (int)(getPaddingTop() + (k - m - n - i1) / 2.0F);
        int i5 = i3;
        paramInt2 = paramInt1;
        paramInt3 = i2;
        paramInt4 = i5;
        if (localView == mExpandButton)
        {
          paramInt2 = paramInt1;
          paramInt3 = i2;
          paramInt4 = i5;
          if (mShowExpandButtonAtEnd)
          {
            paramInt4 = paramInt1 - mContentEndMargin;
            paramInt2 = paramInt4 - localView.getMeasuredWidth();
            paramInt3 = paramInt2;
          }
        }
        if (localView == mProfileBadge)
        {
          paramInt1 = getPaddingEnd();
          if (mShowWorkBadgeAtEnd) {
            paramInt1 = mContentEndMargin;
          }
          paramInt4 = paramInt2 - paramInt1;
          paramInt3 = paramInt4 - localView.getMeasuredWidth();
          i5 = paramInt3;
        }
        else
        {
          i5 = paramInt3;
          paramInt3 = paramInt2;
        }
        paramInt1 = paramInt3;
        paramInt2 = i5;
        if (localView == mAppOps)
        {
          paramInt4 = paramInt3 - mContentEndMargin;
          paramInt1 = paramInt4 - localView.getMeasuredWidth();
          paramInt2 = paramInt1;
        }
        i5 = paramInt2;
        paramInt3 = paramInt4;
        if (getLayoutDirection() == 1)
        {
          i5 = getWidth() - paramInt4;
          paramInt3 = getWidth() - paramInt2;
        }
        localView.layout(i5, i4, paramInt3, i4 + i1);
        paramInt2 = i3 + localMarginLayoutParams.getMarginEnd();
      }
      i++;
    }
    updateTouchListener();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE);
    int m = View.MeasureSpec.makeMeasureSpec(j, Integer.MIN_VALUE);
    paramInt2 = getPaddingStart() + getPaddingEnd();
    for (paramInt1 = 0; paramInt1 < getChildCount(); paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      if (localView.getVisibility() != 8)
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)localView.getLayoutParams();
        localView.measure(getChildMeasureSpec(k, leftMargin + rightMargin, width), getChildMeasureSpec(m, topMargin + bottomMargin, height));
        paramInt2 += leftMargin + rightMargin + localView.getMeasuredWidth();
      }
    }
    if (paramInt2 > i) {
      shrinkViewForOverflow(m, shrinkViewForOverflow(m, shrinkViewForOverflow(m, paramInt2 - i, mAppName, mChildMinWidth), mHeaderText, 0), mSecondaryHeaderText, 0);
    }
    mTotalWidth = Math.min(paramInt2, i);
    setMeasuredDimension(i, j);
  }
  
  @RemotableViewMethod
  public void setAcceptAllTouches(boolean paramBoolean)
  {
    if ((!mEntireHeaderClickable) && (!paramBoolean)) {
      paramBoolean = false;
    } else {
      paramBoolean = true;
    }
    mAcceptAllTouches = paramBoolean;
  }
  
  public void setAppOpsOnClickListener(View.OnClickListener paramOnClickListener)
  {
    mAppOpsListener = paramOnClickListener;
    mAppOps.setOnClickListener(mAppOpsListener);
    mCameraIcon.setOnClickListener(mAppOpsListener);
    mMicIcon.setOnClickListener(mAppOpsListener);
    mOverlayIcon.setOnClickListener(mAppOpsListener);
    updateTouchListener();
  }
  
  @RemotableViewMethod
  public void setExpandOnlyOnButton(boolean paramBoolean)
  {
    mExpandOnlyOnButton = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setExpanded(boolean paramBoolean)
  {
    mExpanded = paramBoolean;
    updateExpandButton();
  }
  
  public void setHeaderBackgroundDrawable(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      setWillNotDraw(false);
      mBackground = paramDrawable;
      mBackground.setCallback(this);
      setOutlineProvider(mProvider);
    }
    else
    {
      setWillNotDraw(true);
      mBackground = null;
      setOutlineProvider(null);
    }
    invalidate();
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    mExpandClickListener = paramOnClickListener;
    mExpandButton.setOnClickListener(mExpandClickListener);
    updateTouchListener();
  }
  
  @RemotableViewMethod
  public void setOriginalIconColor(int paramInt)
  {
    mIconColor = paramInt;
  }
  
  @RemotableViewMethod
  public void setOriginalNotificationColor(int paramInt)
  {
    mOriginalNotificationColor = paramInt;
  }
  
  public void setShowExpandButtonAtEnd(boolean paramBoolean)
  {
    if (paramBoolean != mShowExpandButtonAtEnd)
    {
      setClipToPadding(paramBoolean ^ true);
      mShowExpandButtonAtEnd = paramBoolean;
    }
  }
  
  public void setShowWorkBadgeAtEnd(boolean paramBoolean)
  {
    if (paramBoolean != mShowWorkBadgeAtEnd)
    {
      setClipToPadding(paramBoolean ^ true);
      mShowWorkBadgeAtEnd = paramBoolean;
    }
  }
  
  public void showAppOpsIcons(ArraySet<Integer> paramArraySet)
  {
    if ((mOverlayIcon != null) && (mCameraIcon != null) && (mMicIcon != null) && (paramArraySet != null))
    {
      View localView = mOverlayIcon;
      boolean bool = paramArraySet.contains(Integer.valueOf(24));
      int i = 8;
      int j;
      if (bool) {
        j = 0;
      } else {
        j = 8;
      }
      localView.setVisibility(j);
      localView = mCameraIcon;
      if (paramArraySet.contains(Integer.valueOf(26))) {
        j = 0;
      } else {
        j = 8;
      }
      localView.setVisibility(j);
      localView = mMicIcon;
      if (paramArraySet.contains(Integer.valueOf(27))) {
        j = 0;
      } else {
        j = i;
      }
      localView.setVisibility(j);
      return;
    }
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != mBackground)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public class HeaderTouchListener
    implements View.OnTouchListener
  {
    private Rect mAppOpsRect;
    private float mDownX;
    private float mDownY;
    private Rect mExpandButtonRect;
    private final ArrayList<Rect> mTouchRects = new ArrayList();
    private int mTouchSlop;
    private boolean mTrackGesture;
    
    public HeaderTouchListener() {}
    
    private Rect addRectAroundView(View paramView)
    {
      paramView = getRectAroundView(paramView);
      mTouchRects.add(paramView);
      return paramView;
    }
    
    private void addWidthRect()
    {
      Rect localRect = new Rect();
      top = 0;
      bottom = ((int)(32.0F * getResources().getDisplayMetrics().density));
      left = 0;
      right = getWidth();
      mTouchRects.add(localRect);
    }
    
    private Rect getRectAroundView(View paramView)
    {
      float f1 = 48.0F * getResources().getDisplayMetrics().density;
      float f2 = Math.max(f1, paramView.getWidth());
      f1 = Math.max(f1, paramView.getHeight());
      Rect localRect = new Rect();
      if (paramView.getVisibility() == 8)
      {
        paramView = NotificationHeaderView.this.getFirstChildNotGone();
        left = ((int)(paramView.getLeft() - f2 / 2.0F));
      }
      else
      {
        left = ((int)((paramView.getLeft() + paramView.getRight()) / 2.0F - f2 / 2.0F));
      }
      top = ((int)((paramView.getTop() + paramView.getBottom()) / 2.0F - f1 / 2.0F));
      bottom = ((int)(top + f1));
      right = ((int)(left + f2));
      return localRect;
    }
    
    private boolean isInside(float paramFloat1, float paramFloat2)
    {
      if (mAcceptAllTouches) {
        return true;
      }
      if (mExpandOnlyOnButton) {
        return mExpandButtonRect.contains((int)paramFloat1, (int)paramFloat2);
      }
      for (int i = 0; i < mTouchRects.size(); i++) {
        if (((Rect)mTouchRects.get(i)).contains((int)paramFloat1, (int)paramFloat2)) {
          return true;
        }
      }
      return false;
    }
    
    public void bindTouchRects()
    {
      mTouchRects.clear();
      addRectAroundView(mIcon);
      mExpandButtonRect = addRectAroundView(mExpandButton);
      mAppOpsRect = addRectAroundView(mAppOps);
      addWidthRect();
      mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      switch (paramMotionEvent.getActionMasked() & 0xFF)
      {
      default: 
        break;
      case 2: 
        if ((mTrackGesture) && ((Math.abs(mDownX - f1) > mTouchSlop) || (Math.abs(mDownY - f2) > mTouchSlop))) {
          mTrackGesture = false;
        }
        break;
      case 1: 
        if (mTrackGesture)
        {
          if ((mAppOps.isVisibleToUser()) && ((mAppOpsRect.contains((int)f1, (int)f2)) || (mAppOpsRect.contains((int)mDownX, (int)mDownY))))
          {
            mAppOps.performClick();
            return true;
          }
          mExpandButton.performClick();
        }
        break;
      case 0: 
        mTrackGesture = false;
        if (isInside(f1, f2))
        {
          mDownX = f1;
          mDownY = f2;
          mTrackGesture = true;
          return true;
        }
        break;
      }
      return mTrackGesture;
    }
  }
}
