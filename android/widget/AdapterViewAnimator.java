package android.widget;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class AdapterViewAnimator
  extends AdapterView<Adapter>
  implements RemoteViewsAdapter.RemoteAdapterConnectionCallback, Advanceable
{
  private static final int DEFAULT_ANIMATION_DURATION = 200;
  private static final String TAG = "RemoteViewAnimator";
  static final int TOUCH_MODE_DOWN_IN_CURRENT_VIEW = 1;
  static final int TOUCH_MODE_HANDLED = 2;
  static final int TOUCH_MODE_NONE = 0;
  int mActiveOffset = 0;
  Adapter mAdapter;
  boolean mAnimateFirstTime = true;
  int mCurrentWindowEnd = -1;
  int mCurrentWindowStart = 0;
  int mCurrentWindowStartUnbounded = 0;
  AdapterView<Adapter>.AdapterDataSetObserver mDataSetObserver;
  boolean mDeferNotifyDataSetChanged = false;
  boolean mFirstTime = true;
  ObjectAnimator mInAnimation;
  boolean mLoopViews = true;
  int mMaxNumActiveViews = 1;
  ObjectAnimator mOutAnimation;
  private Runnable mPendingCheckForTap;
  ArrayList<Integer> mPreviousViews;
  int mReferenceChildHeight = -1;
  int mReferenceChildWidth = -1;
  RemoteViewsAdapter mRemoteViewsAdapter;
  private int mRestoreWhichChild = -1;
  private int mTouchMode = 0;
  HashMap<Integer, ViewAndMetaData> mViewsMap = new HashMap();
  int mWhichChild = 0;
  
  public AdapterViewAnimator(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AdapterViewAnimator(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AdapterViewAnimator(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AdapterViewAnimator(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AdapterViewAnimator, paramInt1, paramInt2);
    paramInt1 = paramAttributeSet.getResourceId(0, 0);
    if (paramInt1 > 0) {
      setInAnimation(paramContext, paramInt1);
    } else {
      setInAnimation(getDefaultInAnimation());
    }
    paramInt1 = paramAttributeSet.getResourceId(1, 0);
    if (paramInt1 > 0) {
      setOutAnimation(paramContext, paramInt1);
    } else {
      setOutAnimation(getDefaultOutAnimation());
    }
    setAnimateFirstView(paramAttributeSet.getBoolean(2, true));
    mLoopViews = paramAttributeSet.getBoolean(3, false);
    paramAttributeSet.recycle();
    initViewAnimator();
  }
  
  private void addChild(View paramView)
  {
    addViewInLayout(paramView, -1, createOrReuseLayoutParams(paramView));
    if ((mReferenceChildWidth == -1) || (mReferenceChildHeight == -1))
    {
      int i = View.MeasureSpec.makeMeasureSpec(0, 0);
      paramView.measure(i, i);
      mReferenceChildWidth = paramView.getMeasuredWidth();
      mReferenceChildHeight = paramView.getMeasuredHeight();
    }
  }
  
  private ViewAndMetaData getMetaDataForChild(View paramView)
  {
    Iterator localIterator = mViewsMap.values().iterator();
    while (localIterator.hasNext())
    {
      ViewAndMetaData localViewAndMetaData = (ViewAndMetaData)localIterator.next();
      if (view == paramView) {
        return localViewAndMetaData;
      }
    }
    return null;
  }
  
  private void initViewAnimator()
  {
    mPreviousViews = new ArrayList();
  }
  
  private void measureChildren()
  {
    int i = getChildCount();
    int j = getMeasuredWidth();
    int k = mPaddingLeft;
    int m = mPaddingRight;
    int n = getMeasuredHeight();
    int i1 = mPaddingTop;
    int i2 = mPaddingBottom;
    for (int i3 = 0; i3 < i; i3++) {
      getChildAt(i3).measure(View.MeasureSpec.makeMeasureSpec(j - k - m, 1073741824), View.MeasureSpec.makeMeasureSpec(n - i1 - i2, 1073741824));
    }
  }
  
  private void setDisplayedChild(int paramInt, boolean paramBoolean)
  {
    if (mAdapter != null)
    {
      mWhichChild = paramInt;
      int i = getWindowSize();
      int j = 1;
      if (paramInt >= i)
      {
        if (mLoopViews) {
          paramInt = 0;
        } else {
          paramInt = getWindowSize() - 1;
        }
        mWhichChild = paramInt;
      }
      else if (paramInt < 0)
      {
        if (mLoopViews) {
          paramInt = getWindowSize() - 1;
        } else {
          paramInt = 0;
        }
        mWhichChild = paramInt;
      }
      if (getFocusedChild() != null) {
        paramInt = j;
      } else {
        paramInt = 0;
      }
      showOnly(mWhichChild, paramBoolean);
      if (paramInt != 0) {
        requestFocus(2);
      }
    }
  }
  
  public void advance()
  {
    showNext();
  }
  
  void applyTransformForChildAtIndex(View paramView, int paramInt) {}
  
  void cancelHandleClick()
  {
    View localView = getCurrentView();
    if (localView != null) {
      hideTapFeedback(localView);
    }
    mTouchMode = 0;
  }
  
  void checkForAndHandleDataChanged()
  {
    if (mDataChanged) {
      post(new Runnable()
      {
        public void run()
        {
          handleDataChanged();
          if (mWhichChild >= getWindowSize())
          {
            mWhichChild = 0;
            showOnly(mWhichChild, false);
          }
          else if (mOldItemCount != getCount())
          {
            showOnly(mWhichChild, false);
          }
          refreshChildren();
          requestLayout();
        }
      });
    }
    mDataChanged = false;
  }
  
  void configureViewAnimator(int paramInt1, int paramInt2)
  {
    mMaxNumActiveViews = paramInt1;
    mActiveOffset = paramInt2;
    mPreviousViews.clear();
    mViewsMap.clear();
    removeAllViewsInLayout();
    mCurrentWindowStart = 0;
    mCurrentWindowEnd = -1;
  }
  
  ViewGroup.LayoutParams createOrReuseLayoutParams(View paramView)
  {
    paramView = paramView.getLayoutParams();
    if (paramView != null) {
      return paramView;
    }
    return new ViewGroup.LayoutParams(0, 0);
  }
  
  public void deferNotifyDataSetChanged()
  {
    mDeferNotifyDataSetChanged = true;
  }
  
  public void fyiWillBeAdvancedByHostKThx() {}
  
  public CharSequence getAccessibilityClassName()
  {
    return AdapterViewAnimator.class.getName();
  }
  
  public Adapter getAdapter()
  {
    return mAdapter;
  }
  
  public int getBaseline()
  {
    int i;
    if (getCurrentView() != null) {
      i = getCurrentView().getBaseline();
    } else {
      i = super.getBaseline();
    }
    return i;
  }
  
  public View getCurrentView()
  {
    return getViewAtRelativeIndex(mActiveOffset);
  }
  
  ObjectAnimator getDefaultInAnimation()
  {
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(null, "alpha", new float[] { 0.0F, 1.0F });
    localObjectAnimator.setDuration(200L);
    return localObjectAnimator;
  }
  
  ObjectAnimator getDefaultOutAnimation()
  {
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(null, "alpha", new float[] { 1.0F, 0.0F });
    localObjectAnimator.setDuration(200L);
    return localObjectAnimator;
  }
  
  public int getDisplayedChild()
  {
    return mWhichChild;
  }
  
  FrameLayout getFrameForChild()
  {
    return new FrameLayout(mContext);
  }
  
  public ObjectAnimator getInAnimation()
  {
    return mInAnimation;
  }
  
  int getNumActiveViews()
  {
    if (mAdapter != null) {
      return Math.min(getCount() + 1, mMaxNumActiveViews);
    }
    return mMaxNumActiveViews;
  }
  
  public ObjectAnimator getOutAnimation()
  {
    return mOutAnimation;
  }
  
  public View getSelectedView()
  {
    return getViewAtRelativeIndex(mActiveOffset);
  }
  
  View getViewAtRelativeIndex(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= getNumActiveViews() - 1) && (mAdapter != null))
    {
      paramInt = modulo(mCurrentWindowStartUnbounded + paramInt, getWindowSize());
      if (mViewsMap.get(Integer.valueOf(paramInt)) != null) {
        return mViewsMap.get(Integer.valueOf(paramInt))).view;
      }
    }
    return null;
  }
  
  int getWindowSize()
  {
    if (mAdapter != null)
    {
      int i = getCount();
      if ((i <= getNumActiveViews()) && (mLoopViews)) {
        return mMaxNumActiveViews * i;
      }
      return i;
    }
    return 0;
  }
  
  void hideTapFeedback(View paramView)
  {
    paramView.setPressed(false);
  }
  
  int modulo(int paramInt1, int paramInt2)
  {
    if (paramInt2 > 0) {
      return (paramInt1 % paramInt2 + paramInt2) % paramInt2;
    }
    return 0;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkForAndHandleDataChanged();
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      int i = mPaddingLeft;
      paramInt4 = localView.getMeasuredWidth();
      int j = mPaddingTop;
      paramInt3 = localView.getMeasuredHeight();
      localView.layout(mPaddingLeft, mPaddingTop, i + paramInt4, j + paramInt3);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = mReferenceChildWidth;
    paramInt2 = 0;
    int n;
    if ((paramInt1 != -1) && (mReferenceChildHeight != -1)) {
      n = 1;
    } else {
      n = 0;
    }
    if (m == 0)
    {
      if (n != 0) {
        paramInt1 = mReferenceChildHeight + mPaddingTop + mPaddingBottom;
      } else {
        paramInt1 = 0;
      }
    }
    else
    {
      paramInt1 = j;
      if (m == Integer.MIN_VALUE)
      {
        paramInt1 = j;
        if (n != 0)
        {
          paramInt1 = mReferenceChildHeight + mPaddingTop + mPaddingBottom;
          if (paramInt1 > j) {
            paramInt1 = j | 0x1000000;
          }
        }
      }
    }
    if (k == 0)
    {
      if (n != 0)
      {
        n = mReferenceChildWidth;
        paramInt2 = mPaddingLeft;
        paramInt2 = mPaddingRight + (n + paramInt2);
      }
    }
    else
    {
      paramInt2 = i;
      if (m == Integer.MIN_VALUE)
      {
        paramInt2 = i;
        if (n != 0)
        {
          paramInt2 = mReferenceChildWidth + mPaddingLeft + mPaddingRight;
          if (paramInt2 > i) {
            paramInt2 = i | 0x1000000;
          }
        }
      }
    }
    setMeasuredDimension(paramInt2, paramInt1);
    measureChildren();
  }
  
  public boolean onRemoteAdapterConnected()
  {
    if (mRemoteViewsAdapter != mAdapter)
    {
      setAdapter(mRemoteViewsAdapter);
      if (mDeferNotifyDataSetChanged)
      {
        mRemoteViewsAdapter.notifyDataSetChanged();
        mDeferNotifyDataSetChanged = false;
      }
      if (mRestoreWhichChild > -1)
      {
        setDisplayedChild(mRestoreWhichChild, false);
        mRestoreWhichChild = -1;
      }
      return false;
    }
    if (mRemoteViewsAdapter != null)
    {
      mRemoteViewsAdapter.superNotifyDataSetChanged();
      return true;
    }
    return false;
  }
  
  public void onRemoteAdapterDisconnected() {}
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mWhichChild = whichChild;
    if ((mRemoteViewsAdapter != null) && (mAdapter == null)) {
      mRestoreWhichChild = mWhichChild;
    } else {
      setDisplayedChild(mWhichChild, false);
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    if (mRemoteViewsAdapter != null) {
      mRemoteViewsAdapter.saveRemoteViewsCache();
    }
    return new SavedState(localParcelable, mWhichChild);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3;
    if (i != 6)
    {
      Object localObject;
      switch (i)
      {
      default: 
        bool3 = bool1;
        break;
      case 3: 
        paramMotionEvent = getCurrentView();
        if (paramMotionEvent != null) {
          hideTapFeedback(paramMotionEvent);
        }
        mTouchMode = 0;
        bool3 = bool1;
        break;
      case 2: 
        bool3 = bool1;
        break;
      case 1: 
        bool3 = bool2;
        if (mTouchMode == 1)
        {
          final View localView = getCurrentView();
          localObject = getMetaDataForChild(localView);
          bool3 = bool2;
          if (localView != null)
          {
            bool3 = bool2;
            if (isTransformedTouchPointInView(paramMotionEvent.getX(), paramMotionEvent.getY(), localView, null))
            {
              paramMotionEvent = getHandler();
              if (paramMotionEvent != null) {
                paramMotionEvent.removeCallbacks(mPendingCheckForTap);
              }
              showTapFeedback(localView);
              postDelayed(new Runnable()
              {
                public void run()
                {
                  hideTapFeedback(localView);
                  post(new Runnable()
                  {
                    public void run()
                    {
                      if (val$viewData != null) {
                        performItemClick(val$v, val$viewData.adapterPosition, val$viewData.itemId);
                      } else {
                        performItemClick(val$v, 0, 0L);
                      }
                    }
                  });
                }
              }, ViewConfiguration.getPressedStateDuration());
              bool3 = true;
            }
          }
        }
        mTouchMode = 0;
        break;
      case 0: 
        localObject = getCurrentView();
        bool3 = bool1;
        if (localObject == null) {
          break;
        }
        bool3 = bool1;
        if (!isTransformedTouchPointInView(paramMotionEvent.getX(), paramMotionEvent.getY(), (View)localObject, null)) {
          break;
        }
        if (mPendingCheckForTap == null) {
          mPendingCheckForTap = new CheckForTap();
        }
        mTouchMode = 1;
        postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
        bool3 = bool1;
        break;
      }
    }
    else
    {
      bool3 = bool1;
    }
    return bool3;
  }
  
  void refreshChildren()
  {
    if (mAdapter == null) {
      return;
    }
    for (int i = mCurrentWindowStart; i <= mCurrentWindowEnd; i++)
    {
      int j = modulo(i, getWindowSize());
      int k = getCount();
      View localView = mAdapter.getView(modulo(i, k), null, this);
      if (localView.getImportantForAccessibility() == 0) {
        localView.setImportantForAccessibility(1);
      }
      if (mViewsMap.containsKey(Integer.valueOf(j)))
      {
        FrameLayout localFrameLayout = (FrameLayout)mViewsMap.get(Integer.valueOf(j))).view;
        if (localView != null)
        {
          localFrameLayout.removeAllViewsInLayout();
          localFrameLayout.addView(localView);
        }
      }
    }
  }
  
  public void setAdapter(Adapter paramAdapter)
  {
    if ((mAdapter != null) && (mDataSetObserver != null)) {
      mAdapter.unregisterDataSetObserver(mDataSetObserver);
    }
    mAdapter = paramAdapter;
    checkFocus();
    if (mAdapter != null)
    {
      mDataSetObserver = new AdapterView.AdapterDataSetObserver(this);
      mAdapter.registerDataSetObserver(mDataSetObserver);
      mItemCount = mAdapter.getCount();
    }
    setFocusable(true);
    mWhichChild = 0;
    showOnly(mWhichChild, false);
  }
  
  public void setAnimateFirstView(boolean paramBoolean)
  {
    mAnimateFirstTime = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setDisplayedChild(int paramInt)
  {
    setDisplayedChild(paramInt, true);
  }
  
  public void setInAnimation(ObjectAnimator paramObjectAnimator)
  {
    mInAnimation = paramObjectAnimator;
  }
  
  public void setInAnimation(Context paramContext, int paramInt)
  {
    setInAnimation((ObjectAnimator)AnimatorInflater.loadAnimator(paramContext, paramInt));
  }
  
  public void setOutAnimation(ObjectAnimator paramObjectAnimator)
  {
    mOutAnimation = paramObjectAnimator;
  }
  
  public void setOutAnimation(Context paramContext, int paramInt)
  {
    setOutAnimation((ObjectAnimator)AnimatorInflater.loadAnimator(paramContext, paramInt));
  }
  
  @RemotableViewMethod(asyncImpl="setRemoteViewsAdapterAsync")
  public void setRemoteViewsAdapter(Intent paramIntent)
  {
    setRemoteViewsAdapter(paramIntent, false);
  }
  
  public void setRemoteViewsAdapter(Intent paramIntent, boolean paramBoolean)
  {
    if ((mRemoteViewsAdapter != null) && (new Intent.FilterComparison(paramIntent).equals(new Intent.FilterComparison(mRemoteViewsAdapter.getRemoteViewsServiceIntent())))) {
      return;
    }
    mDeferNotifyDataSetChanged = false;
    mRemoteViewsAdapter = new RemoteViewsAdapter(getContext(), paramIntent, this, paramBoolean);
    if (mRemoteViewsAdapter.isDataReady()) {
      setAdapter(mRemoteViewsAdapter);
    }
  }
  
  public Runnable setRemoteViewsAdapterAsync(Intent paramIntent)
  {
    return new RemoteViewsAdapter.AsyncRemoteAdapterAction(this, paramIntent);
  }
  
  public void setRemoteViewsOnClickHandler(RemoteViews.OnClickHandler paramOnClickHandler)
  {
    if (mRemoteViewsAdapter != null) {
      mRemoteViewsAdapter.setRemoteViewsOnClickHandler(paramOnClickHandler);
    }
  }
  
  public void setSelection(int paramInt)
  {
    setDisplayedChild(paramInt);
  }
  
  public void showNext()
  {
    setDisplayedChild(mWhichChild + 1);
  }
  
  void showOnly(int paramInt, boolean paramBoolean)
  {
    if (mAdapter == null) {
      return;
    }
    int i = getCount();
    if (i == 0) {
      return;
    }
    View localView;
    for (int j = 0; j < mPreviousViews.size(); j++)
    {
      localView = mViewsMap.get(mPreviousViews.get(j))).view;
      mViewsMap.remove(mPreviousViews.get(j));
      localView.clearAnimation();
      if ((localView instanceof ViewGroup)) {
        ((ViewGroup)localView).removeAllViewsInLayout();
      }
      applyTransformForChildAtIndex(localView, -1);
      removeViewInLayout(localView);
    }
    mPreviousViews.clear();
    int k = paramInt - mActiveOffset;
    int m = getNumActiveViews() + k - 1;
    paramInt = Math.max(0, k);
    j = Math.min(i - 1, m);
    if (mLoopViews)
    {
      paramInt = k;
      j = m;
    }
    int n = j;
    int i1 = modulo(paramInt, getWindowSize());
    int i2 = modulo(n, getWindowSize());
    int i3 = 0;
    if (i1 > i2) {
      i3 = 1;
    }
    Object localObject = mViewsMap.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      Integer localInteger = (Integer)((Iterator)localObject).next();
      i4 = 0;
      if ((i3 == 0) && ((localInteger.intValue() < i1) || (localInteger.intValue() > i2)))
      {
        j = 1;
      }
      else
      {
        j = i4;
        if (i3 != 0)
        {
          j = i4;
          if (localInteger.intValue() > i2)
          {
            j = i4;
            if (localInteger.intValue() < i1) {
              j = 1;
            }
          }
        }
      }
      if (j != 0)
      {
        localView = mViewsMap.get(localInteger)).view;
        j = mViewsMap.get(localInteger)).relativeIndex;
        mPreviousViews.add(localInteger);
        transformViewForTransition(j, -1, localView, paramBoolean);
      }
    }
    if ((paramInt == mCurrentWindowStart) && (n == mCurrentWindowEnd) && (k == mCurrentWindowStartUnbounded)) {
      break label804;
    }
    int i4 = paramInt;
    j = k;
    k = i;
    i3 = paramInt;
    paramInt = i1;
    i = i2;
    while (i4 <= n)
    {
      int i5 = modulo(i4, getWindowSize());
      if (mViewsMap.containsKey(Integer.valueOf(i5))) {
        i2 = mViewsMap.get(Integer.valueOf(i5))).relativeIndex;
      } else {
        i2 = -1;
      }
      int i6 = i4 - j;
      if ((mViewsMap.containsKey(Integer.valueOf(i5))) && (!mPreviousViews.contains(Integer.valueOf(i5)))) {
        i1 = 1;
      } else {
        i1 = 0;
      }
      if (i1 != 0)
      {
        localView = mViewsMap.get(Integer.valueOf(i5))).view;
        mViewsMap.get(Integer.valueOf(i5))).relativeIndex = i6;
        applyTransformForChildAtIndex(localView, i6);
        transformViewForTransition(i2, i6, localView, paramBoolean);
      }
      else
      {
        i2 = modulo(i4, k);
        localView = mAdapter.getView(i2, null, this);
        long l = mAdapter.getItemId(i2);
        localObject = getFrameForChild();
        if (localView != null) {
          ((FrameLayout)localObject).addView(localView);
        }
        mViewsMap.put(Integer.valueOf(i5), new ViewAndMetaData((View)localObject, i6, i2, l));
        addChild((View)localObject);
        applyTransformForChildAtIndex((View)localObject, i6);
        transformViewForTransition(-1, i6, (View)localObject, paramBoolean);
      }
      mViewsMap.get(Integer.valueOf(i5))).view.bringToFront();
      i4++;
    }
    mCurrentWindowStart = i3;
    mCurrentWindowEnd = n;
    mCurrentWindowStartUnbounded = j;
    if (mRemoteViewsAdapter != null)
    {
      paramInt = modulo(mCurrentWindowStart, k);
      j = modulo(mCurrentWindowEnd, k);
      mRemoteViewsAdapter.setVisibleRangeHint(paramInt, j);
    }
    label804:
    requestLayout();
    invalidate();
  }
  
  public void showPrevious()
  {
    setDisplayedChild(mWhichChild - 1);
  }
  
  void showTapFeedback(View paramView)
  {
    paramView.setPressed(true);
  }
  
  void transformViewForTransition(int paramInt1, int paramInt2, View paramView, boolean paramBoolean)
  {
    if (paramInt1 == -1)
    {
      mInAnimation.setTarget(paramView);
      mInAnimation.start();
    }
    else if (paramInt2 == -1)
    {
      mOutAnimation.setTarget(paramView);
      mOutAnimation.start();
    }
  }
  
  final class CheckForTap
    implements Runnable
  {
    CheckForTap() {}
    
    public void run()
    {
      if (mTouchMode == 1)
      {
        View localView = getCurrentView();
        showTapFeedback(localView);
      }
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public AdapterViewAnimator.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AdapterViewAnimator.SavedState(paramAnonymousParcel, null);
      }
      
      public AdapterViewAnimator.SavedState[] newArray(int paramAnonymousInt)
      {
        return new AdapterViewAnimator.SavedState[paramAnonymousInt];
      }
    };
    int whichChild;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      whichChild = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable, int paramInt)
    {
      super();
      whichChild = paramInt;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AdapterViewAnimator.SavedState{ whichChild = ");
      localStringBuilder.append(whichChild);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(whichChild);
    }
  }
  
  class ViewAndMetaData
  {
    int adapterPosition;
    long itemId;
    int relativeIndex;
    View view;
    
    ViewAndMetaData(View paramView, int paramInt1, int paramInt2, long paramLong)
    {
      view = paramView;
      relativeIndex = paramInt1;
      adapterPosition = paramInt2;
      itemId = paramLong;
    }
  }
}
