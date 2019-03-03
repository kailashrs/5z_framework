package android.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.R.styleable;

@Deprecated
public class FragmentBreadCrumbs
  extends ViewGroup
  implements FragmentManager.OnBackStackChangedListener
{
  private static final int DEFAULT_GRAVITY = 8388627;
  Activity mActivity;
  LinearLayout mContainer;
  private int mGravity;
  LayoutInflater mInflater;
  private int mLayoutResId;
  int mMaxVisible = -1;
  private OnBreadCrumbClickListener mOnBreadCrumbClickListener;
  private View.OnClickListener mOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if ((paramAnonymousView.getTag() instanceof FragmentManager.BackStackEntry))
      {
        FragmentManager.BackStackEntry localBackStackEntry = (FragmentManager.BackStackEntry)paramAnonymousView.getTag();
        if (localBackStackEntry == mParentEntry)
        {
          if (mParentClickListener != null) {
            mParentClickListener.onClick(paramAnonymousView);
          }
        }
        else
        {
          if (mOnBreadCrumbClickListener != null)
          {
            FragmentBreadCrumbs.OnBreadCrumbClickListener localOnBreadCrumbClickListener = mOnBreadCrumbClickListener;
            if (localBackStackEntry == mTopEntry) {
              paramAnonymousView = null;
            } else {
              paramAnonymousView = localBackStackEntry;
            }
            if (localOnBreadCrumbClickListener.onBreadCrumbClick(paramAnonymousView, 0)) {
              return;
            }
          }
          if (localBackStackEntry == mTopEntry) {
            mActivity.getFragmentManager().popBackStack();
          } else {
            mActivity.getFragmentManager().popBackStack(localBackStackEntry.getId(), 0);
          }
        }
      }
    }
  };
  private View.OnClickListener mParentClickListener;
  BackStackRecord mParentEntry;
  private int mTextColor;
  BackStackRecord mTopEntry;
  
  public FragmentBreadCrumbs(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public FragmentBreadCrumbs(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 17891394);
  }
  
  public FragmentBreadCrumbs(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public FragmentBreadCrumbs(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FragmentBreadCrumbs, paramInt1, paramInt2);
    mGravity = paramContext.getInt(0, 8388627);
    mLayoutResId = paramContext.getResourceId(2, 17367174);
    mTextColor = paramContext.getColor(1, 0);
    paramContext.recycle();
  }
  
  private BackStackRecord createBackStackEntry(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (paramCharSequence1 == null) {
      return null;
    }
    BackStackRecord localBackStackRecord = new BackStackRecord((FragmentManagerImpl)mActivity.getFragmentManager());
    localBackStackRecord.setBreadCrumbTitle(paramCharSequence1);
    localBackStackRecord.setBreadCrumbShortTitle(paramCharSequence2);
    return localBackStackRecord;
  }
  
  private FragmentManager.BackStackEntry getPreEntry(int paramInt)
  {
    if (mParentEntry != null)
    {
      BackStackRecord localBackStackRecord;
      if (paramInt == 0) {
        localBackStackRecord = mParentEntry;
      } else {
        localBackStackRecord = mTopEntry;
      }
      return localBackStackRecord;
    }
    return mTopEntry;
  }
  
  private int getPreEntryCount()
  {
    BackStackRecord localBackStackRecord = mTopEntry;
    int i = 0;
    int j;
    if (localBackStackRecord != null) {
      j = 1;
    } else {
      j = 0;
    }
    if (mParentEntry != null) {
      i = 1;
    }
    return j + i;
  }
  
  public void onBackStackChanged()
  {
    updateCrumbs();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getChildCount() == 0) {
      return;
    }
    View localView = getChildAt(0);
    paramInt4 = mPaddingTop;
    int i = mPaddingTop;
    int j = localView.getMeasuredHeight();
    int k = mPaddingBottom;
    paramInt1 = getLayoutDirection();
    paramInt1 = Gravity.getAbsoluteGravity(mGravity & 0x800007, paramInt1);
    if (paramInt1 != 1)
    {
      if (paramInt1 != 5)
      {
        paramInt2 = mPaddingLeft;
        paramInt1 = localView.getMeasuredWidth() + paramInt2;
      }
      else
      {
        paramInt1 = mRight - mLeft - mPaddingRight;
        paramInt2 = paramInt1 - localView.getMeasuredWidth();
      }
    }
    else
    {
      paramInt2 = mPaddingLeft + (mRight - mLeft - localView.getMeasuredWidth()) / 2;
      paramInt1 = localView.getMeasuredWidth() + paramInt2;
    }
    paramInt3 = paramInt2;
    if (paramInt2 < mPaddingLeft) {
      paramInt3 = mPaddingLeft;
    }
    paramInt2 = paramInt1;
    if (paramInt1 > mRight - mLeft - mPaddingRight) {
      paramInt2 = mRight - mLeft - mPaddingRight;
    }
    localView.layout(paramInt3, paramInt4, paramInt2, i + j - k);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    while (n < i)
    {
      View localView = getChildAt(n);
      int i1 = j;
      int i2 = k;
      i3 = m;
      if (localView.getVisibility() != 8)
      {
        measureChild(localView, paramInt1, paramInt2);
        i2 = Math.max(k, localView.getMeasuredWidth());
        i1 = Math.max(j, localView.getMeasuredHeight());
        i3 = combineMeasuredStates(m, localView.getMeasuredState());
      }
      n++;
      j = i1;
      k = i2;
      m = i3;
    }
    n = mPaddingLeft;
    int i3 = mPaddingRight;
    j = Math.max(j + (mPaddingTop + mPaddingBottom), getSuggestedMinimumHeight());
    setMeasuredDimension(resolveSizeAndState(Math.max(k + (n + i3), getSuggestedMinimumWidth()), paramInt1, m), resolveSizeAndState(j, paramInt2, m << 16));
  }
  
  public void setActivity(Activity paramActivity)
  {
    mActivity = paramActivity;
    mInflater = ((LayoutInflater)paramActivity.getSystemService("layout_inflater"));
    mContainer = ((LinearLayout)mInflater.inflate(17367176, this, false));
    addView(mContainer);
    paramActivity.getFragmentManager().addOnBackStackChangedListener(this);
    updateCrumbs();
    setLayoutTransition(new LayoutTransition());
  }
  
  public void setMaxVisible(int paramInt)
  {
    if (paramInt >= 1)
    {
      mMaxVisible = paramInt;
      return;
    }
    throw new IllegalArgumentException("visibleCrumbs must be greater than zero");
  }
  
  public void setOnBreadCrumbClickListener(OnBreadCrumbClickListener paramOnBreadCrumbClickListener)
  {
    mOnBreadCrumbClickListener = paramOnBreadCrumbClickListener;
  }
  
  public void setParentTitle(CharSequence paramCharSequence1, CharSequence paramCharSequence2, View.OnClickListener paramOnClickListener)
  {
    mParentEntry = createBackStackEntry(paramCharSequence1, paramCharSequence2);
    mParentClickListener = paramOnClickListener;
    updateCrumbs();
  }
  
  public void setTitle(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    mTopEntry = createBackStackEntry(paramCharSequence1, paramCharSequence2);
    updateCrumbs();
  }
  
  void updateCrumbs()
  {
    Object localObject1 = mActivity.getFragmentManager();
    int i = ((FragmentManager)localObject1).getBackStackEntryCount();
    int j = getPreEntryCount();
    int k = mContainer.getChildCount();
    int m = 0;
    Object localObject2;
    int n;
    while (m < i + j)
    {
      if (m < j) {
        localObject2 = getPreEntry(m);
      } else {
        localObject2 = ((FragmentManager)localObject1).getBackStackEntryAt(m - j);
      }
      n = k;
      if (m < k)
      {
        n = k;
        if (mContainer.getChildAt(m).getTag() != localObject2)
        {
          for (n = m; n < k; n++) {
            mContainer.removeViewAt(m);
          }
          n = m;
        }
      }
      if (m >= n)
      {
        View localView = mInflater.inflate(mLayoutResId, this, false);
        TextView localTextView = (TextView)localView.findViewById(16908310);
        localTextView.setText(((FragmentManager.BackStackEntry)localObject2).getBreadCrumbTitle());
        localTextView.setTag(localObject2);
        localTextView.setTextColor(mTextColor);
        if (m == 0) {
          localView.findViewById(16909084).setVisibility(8);
        }
        mContainer.addView(localView);
        localTextView.setOnClickListener(mOnClickListener);
      }
      m++;
      k = n;
    }
    for (m = mContainer.getChildCount(); m > i + j; m--) {
      mContainer.removeViewAt(m - 1);
    }
    for (k = 0; k < m; k++)
    {
      localObject2 = mContainer.getChildAt(k);
      localObject1 = ((View)localObject2).findViewById(16908310);
      boolean bool;
      if (k < m - 1) {
        bool = true;
      } else {
        bool = false;
      }
      ((View)localObject1).setEnabled(bool);
      if (mMaxVisible > 0)
      {
        if (k < m - mMaxVisible) {
          n = 8;
        } else {
          n = 0;
        }
        ((View)localObject2).setVisibility(n);
        localObject2 = ((View)localObject2).findViewById(16909084);
        if ((k > m - mMaxVisible) && (k != 0)) {
          n = 0;
        } else {
          n = 8;
        }
        ((View)localObject2).setVisibility(n);
      }
    }
  }
  
  @Deprecated
  public static abstract interface OnBreadCrumbClickListener
  {
    public abstract boolean onBreadCrumbClick(FragmentManager.BackStackEntry paramBackStackEntry, int paramInt);
  }
}
