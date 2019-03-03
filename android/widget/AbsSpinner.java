package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.autofill.AutofillValue;
import com.android.internal.R.styleable;

public abstract class AbsSpinner
  extends AdapterView<SpinnerAdapter>
{
  private static final String LOG_TAG = AbsSpinner.class.getSimpleName();
  SpinnerAdapter mAdapter;
  private DataSetObserver mDataSetObserver;
  int mHeightMeasureSpec;
  final RecycleBin mRecycler = new RecycleBin();
  int mSelectionBottomPadding = 0;
  int mSelectionLeftPadding = 0;
  int mSelectionRightPadding = 0;
  int mSelectionTopPadding = 0;
  final Rect mSpinnerPadding = new Rect();
  private Rect mTouchFrame;
  int mWidthMeasureSpec;
  
  public AbsSpinner(Context paramContext)
  {
    super(paramContext);
    initAbsSpinner();
  }
  
  public AbsSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AbsSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AbsSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (getImportantForAutofill() == 0) {
      setImportantForAutofill(1);
    }
    initAbsSpinner();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AbsSpinner, paramInt1, paramInt2);
    CharSequence[] arrayOfCharSequence = paramAttributeSet.getTextArray(0);
    if (arrayOfCharSequence != null)
    {
      paramContext = new ArrayAdapter(paramContext, 17367048, arrayOfCharSequence);
      paramContext.setDropDownViewResource(17367049);
      setAdapter(paramContext);
    }
    paramAttributeSet.recycle();
  }
  
  private void initAbsSpinner()
  {
    setFocusable(true);
    setWillNotDraw(false);
  }
  
  public void autofill(AutofillValue paramAutofillValue)
  {
    if (!isEnabled()) {
      return;
    }
    if (!paramAutofillValue.isList())
    {
      String str = LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramAutofillValue);
      localStringBuilder.append(" could not be autofilled into ");
      localStringBuilder.append(this);
      Log.w(str, localStringBuilder.toString());
      return;
    }
    setSelection(paramAutofillValue.getListValue());
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    super.dispatchRestoreInstanceState(paramSparseArray);
    handleDataChanged();
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.LayoutParams(-1, -2);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return AbsSpinner.class.getName();
  }
  
  public SpinnerAdapter getAdapter()
  {
    return mAdapter;
  }
  
  public int getAutofillType()
  {
    int i;
    if (isEnabled()) {
      i = 3;
    } else {
      i = 0;
    }
    return i;
  }
  
  public AutofillValue getAutofillValue()
  {
    AutofillValue localAutofillValue;
    if (isEnabled()) {
      localAutofillValue = AutofillValue.forList(getSelectedItemPosition());
    } else {
      localAutofillValue = null;
    }
    return localAutofillValue;
  }
  
  int getChildHeight(View paramView)
  {
    return paramView.getMeasuredHeight();
  }
  
  int getChildWidth(View paramView)
  {
    return paramView.getMeasuredWidth();
  }
  
  public int getCount()
  {
    return mItemCount;
  }
  
  public View getSelectedView()
  {
    if ((mItemCount > 0) && (mSelectedPosition >= 0)) {
      return getChildAt(mSelectedPosition - mFirstPosition);
    }
    return null;
  }
  
  abstract void layout(int paramInt, boolean paramBoolean);
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    Object localObject = mSpinnerPadding;
    if (mPaddingLeft > mSelectionLeftPadding) {
      j = mPaddingLeft;
    } else {
      j = mSelectionLeftPadding;
    }
    left = j;
    localObject = mSpinnerPadding;
    if (mPaddingTop > mSelectionTopPadding) {
      j = mPaddingTop;
    } else {
      j = mSelectionTopPadding;
    }
    top = j;
    localObject = mSpinnerPadding;
    if (mPaddingRight > mSelectionRightPadding) {
      j = mPaddingRight;
    } else {
      j = mSelectionRightPadding;
    }
    right = j;
    localObject = mSpinnerPadding;
    if (mPaddingBottom > mSelectionBottomPadding) {
      j = mPaddingBottom;
    } else {
      j = mSelectionBottomPadding;
    }
    bottom = j;
    if (mDataChanged) {
      handleDataChanged();
    }
    int k = 0;
    int m = 0;
    int n = 1;
    int i1 = getSelectedItemPosition();
    int i2 = k;
    int j = m;
    int i3 = n;
    if (i1 >= 0)
    {
      i2 = k;
      j = m;
      i3 = n;
      if (mAdapter != null)
      {
        i2 = k;
        j = m;
        i3 = n;
        if (i1 < mAdapter.getCount())
        {
          View localView = mRecycler.get(i1);
          localObject = localView;
          if (localView == null)
          {
            localView = mAdapter.getView(i1, null, this);
            localObject = localView;
            if (localView.getImportantForAccessibility() == 0)
            {
              localView.setImportantForAccessibility(1);
              localObject = localView;
            }
          }
          i2 = k;
          j = m;
          i3 = n;
          if (localObject != null)
          {
            mRecycler.put(i1, (View)localObject);
            if (((View)localObject).getLayoutParams() == null)
            {
              mBlockLayoutRequests = true;
              ((View)localObject).setLayoutParams(generateDefaultLayoutParams());
              mBlockLayoutRequests = false;
            }
            measureChild((View)localObject, paramInt1, paramInt2);
            i2 = getChildHeight((View)localObject) + mSpinnerPadding.top + mSpinnerPadding.bottom;
            j = getChildWidth((View)localObject) + mSpinnerPadding.left + mSpinnerPadding.right;
            i3 = 0;
          }
        }
      }
    }
    k = i2;
    i2 = j;
    if (i3 != 0)
    {
      i3 = mSpinnerPadding.top + mSpinnerPadding.bottom;
      k = i3;
      i2 = j;
      if (i == 0)
      {
        i2 = mSpinnerPadding.left + mSpinnerPadding.right;
        k = i3;
      }
    }
    j = Math.max(k, getSuggestedMinimumHeight());
    i2 = Math.max(i2, getSuggestedMinimumWidth());
    j = resolveSizeAndState(j, paramInt2, 0);
    setMeasuredDimension(resolveSizeAndState(i2, paramInt1, 0), j);
    mHeightMeasureSpec = paramInt2;
    mWidthMeasureSpec = paramInt1;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if (selectedId >= 0L)
    {
      mDataChanged = true;
      mNeedSync = true;
      mSyncRowId = selectedId;
      mSyncPosition = position;
      mSyncMode = 0;
      requestLayout();
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    selectedId = getSelectedItemId();
    if (selectedId >= 0L) {
      position = getSelectedItemPosition();
    } else {
      position = -1;
    }
    return localSavedState;
  }
  
  public int pointToPosition(int paramInt1, int paramInt2)
  {
    Object localObject1 = mTouchFrame;
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      mTouchFrame = new Rect();
      localObject2 = mTouchFrame;
    }
    for (int i = getChildCount() - 1; i >= 0; i--)
    {
      localObject1 = getChildAt(i);
      if (((View)localObject1).getVisibility() == 0)
      {
        ((View)localObject1).getHitRect((Rect)localObject2);
        if (((Rect)localObject2).contains(paramInt1, paramInt2)) {
          return mFirstPosition + i;
        }
      }
    }
    return -1;
  }
  
  void recycleAllViews()
  {
    int i = getChildCount();
    RecycleBin localRecycleBin = mRecycler;
    int j = mFirstPosition;
    for (int k = 0; k < i; k++) {
      localRecycleBin.put(j + k, getChildAt(k));
    }
  }
  
  public void requestLayout()
  {
    if (!mBlockLayoutRequests) {
      super.requestLayout();
    }
  }
  
  void resetList()
  {
    mDataChanged = false;
    mNeedSync = false;
    removeAllViewsInLayout();
    mOldSelectedPosition = -1;
    mOldSelectedRowId = Long.MIN_VALUE;
    setSelectedPositionInt(-1);
    setNextSelectedPositionInt(-1);
    invalidate();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    if (mAdapter != null)
    {
      mAdapter.unregisterDataSetObserver(mDataSetObserver);
      resetList();
    }
    mAdapter = paramSpinnerAdapter;
    int i = -1;
    mOldSelectedPosition = -1;
    mOldSelectedRowId = Long.MIN_VALUE;
    if (mAdapter != null)
    {
      mOldItemCount = mItemCount;
      mItemCount = mAdapter.getCount();
      checkFocus();
      mDataSetObserver = new AdapterView.AdapterDataSetObserver(this);
      mAdapter.registerDataSetObserver(mDataSetObserver);
      if (mItemCount > 0) {
        i = 0;
      }
      setSelectedPositionInt(i);
      setNextSelectedPositionInt(i);
      if (mItemCount == 0) {
        checkSelectionChanged();
      }
    }
    else
    {
      checkFocus();
      resetList();
      checkSelectionChanged();
    }
    requestLayout();
  }
  
  public void setSelection(int paramInt)
  {
    setNextSelectedPositionInt(paramInt);
    requestLayout();
    invalidate();
  }
  
  public void setSelection(int paramInt, boolean paramBoolean)
  {
    boolean bool = true;
    if ((paramBoolean) && (mFirstPosition <= paramInt) && (paramInt <= mFirstPosition + getChildCount() - 1)) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    }
    setSelectionInt(paramInt, paramBoolean);
  }
  
  void setSelectionInt(int paramInt, boolean paramBoolean)
  {
    if (paramInt != mOldSelectedPosition)
    {
      mBlockLayoutRequests = true;
      int i = mSelectedPosition;
      setNextSelectedPositionInt(paramInt);
      layout(paramInt - i, paramBoolean);
      mBlockLayoutRequests = false;
    }
  }
  
  class RecycleBin
  {
    private final SparseArray<View> mScrapHeap = new SparseArray();
    
    RecycleBin() {}
    
    void clear()
    {
      SparseArray localSparseArray = mScrapHeap;
      int i = localSparseArray.size();
      for (int j = 0; j < i; j++)
      {
        View localView = (View)localSparseArray.valueAt(j);
        if (localView != null) {
          removeDetachedView(localView, true);
        }
      }
      localSparseArray.clear();
    }
    
    View get(int paramInt)
    {
      View localView = (View)mScrapHeap.get(paramInt);
      if (localView != null) {
        mScrapHeap.delete(paramInt);
      }
      return localView;
    }
    
    public void put(int paramInt, View paramView)
    {
      mScrapHeap.put(paramInt, paramView);
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public AbsSpinner.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AbsSpinner.SavedState(paramAnonymousParcel);
      }
      
      public AbsSpinner.SavedState[] newArray(int paramAnonymousInt)
      {
        return new AbsSpinner.SavedState[paramAnonymousInt];
      }
    };
    int position;
    long selectedId;
    
    SavedState(Parcel paramParcel)
    {
      super();
      selectedId = paramParcel.readLong();
      position = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AbsSpinner.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" selectedId=");
      localStringBuilder.append(selectedId);
      localStringBuilder.append(" position=");
      localStringBuilder.append(position);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeLong(selectedId);
      paramParcel.writeInt(position);
    }
  }
}
