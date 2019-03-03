package android.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.autofill.AutofillManager;

public abstract class AdapterView<T extends Adapter>
  extends ViewGroup
{
  public static final int INVALID_POSITION = -1;
  public static final long INVALID_ROW_ID = Long.MIN_VALUE;
  public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;
  public static final int ITEM_VIEW_TYPE_IGNORE = -1;
  static final int SYNC_FIRST_POSITION = 1;
  static final int SYNC_MAX_DURATION_MILLIS = 100;
  static final int SYNC_SELECTED_POSITION = 0;
  boolean mBlockLayoutRequests = false;
  boolean mDataChanged;
  private boolean mDesiredFocusableInTouchModeState;
  private int mDesiredFocusableState = 16;
  private View mEmptyView;
  @ViewDebug.ExportedProperty(category="scrolling")
  int mFirstPosition = 0;
  boolean mInLayout = false;
  @ViewDebug.ExportedProperty(category="list")
  int mItemCount;
  private int mLayoutHeight;
  boolean mNeedSync = false;
  @ViewDebug.ExportedProperty(category="list")
  int mNextSelectedPosition = -1;
  long mNextSelectedRowId = Long.MIN_VALUE;
  int mOldItemCount;
  int mOldSelectedPosition = -1;
  long mOldSelectedRowId = Long.MIN_VALUE;
  OnItemClickListener mOnItemClickListener;
  OnItemLongClickListener mOnItemLongClickListener;
  OnItemSelectedListener mOnItemSelectedListener;
  private AdapterView<T>.SelectionNotifier mPendingSelectionNotifier;
  @ViewDebug.ExportedProperty(category="list")
  int mSelectedPosition = -1;
  long mSelectedRowId = Long.MIN_VALUE;
  private AdapterView<T>.SelectionNotifier mSelectionNotifier;
  int mSpecificTop;
  long mSyncHeight;
  int mSyncMode;
  int mSyncPosition;
  long mSyncRowId = Long.MIN_VALUE;
  
  public AdapterView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AdapterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AdapterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AdapterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
    mDesiredFocusableState = getFocusable();
    if (mDesiredFocusableState == 16) {
      super.setFocusable(0);
    }
  }
  
  private void dispatchOnItemSelected()
  {
    fireOnSelected();
    performAccessibilityActionsOnSelected();
  }
  
  private void fireOnSelected()
  {
    if (mOnItemSelectedListener == null) {
      return;
    }
    int i = getSelectedItemPosition();
    if (i >= 0)
    {
      View localView = getSelectedView();
      mOnItemSelectedListener.onItemSelected(this, localView, i, getAdapter().getItemId(i));
    }
    else
    {
      mOnItemSelectedListener.onNothingSelected(this);
    }
  }
  
  private boolean isScrollableForAccessibility()
  {
    Adapter localAdapter = getAdapter();
    boolean bool = false;
    if (localAdapter != null)
    {
      int i = localAdapter.getCount();
      if ((i > 0) && ((getFirstVisiblePosition() > 0) || (getLastVisiblePosition() < i - 1))) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  private void performAccessibilityActionsOnSelected()
  {
    if (!AccessibilityManager.getInstance(mContext).isEnabled()) {
      return;
    }
    if (getSelectedItemPosition() >= 0) {
      sendAccessibilityEvent(4);
    }
  }
  
  private void updateEmptyStatus(boolean paramBoolean)
  {
    if (isInFilterMode()) {
      paramBoolean = false;
    }
    if (paramBoolean)
    {
      if (mEmptyView != null)
      {
        mEmptyView.setVisibility(0);
        setVisibility(8);
      }
      else
      {
        setVisibility(0);
      }
      if (mDataChanged) {
        onLayout(false, mLeft, mTop, mRight, mBottom);
      }
    }
    else
    {
      if (mEmptyView != null) {
        mEmptyView.setVisibility(8);
      }
      setVisibility(0);
    }
  }
  
  public void addView(View paramView)
  {
    throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
  }
  
  public void addView(View paramView, int paramInt)
  {
    throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
  }
  
  protected boolean canAnimate()
  {
    boolean bool;
    if ((super.canAnimate()) && (mItemCount > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void checkFocus()
  {
    Adapter localAdapter = getAdapter();
    boolean bool1 = true;
    int i;
    if ((localAdapter != null) && (localAdapter.getCount() != 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((i != 0) && (!isInFilterMode())) {
      i = 0;
    } else {
      i = 1;
    }
    boolean bool2;
    if ((i != 0) && (mDesiredFocusableInTouchModeState)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    super.setFocusableInTouchMode(bool2);
    if (i != 0) {
      i = mDesiredFocusableState;
    } else {
      i = 0;
    }
    super.setFocusable(i);
    if (mEmptyView != null)
    {
      bool2 = bool1;
      if (localAdapter != null) {
        if (localAdapter.isEmpty()) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      updateEmptyStatus(bool2);
    }
  }
  
  void checkSelectionChanged()
  {
    if ((mSelectedPosition != mOldSelectedPosition) || (mSelectedRowId != mOldSelectedRowId))
    {
      selectionChanged();
      mOldSelectedPosition = mSelectedPosition;
      mOldSelectedRowId = mSelectedRowId;
    }
    if (mPendingSelectionNotifier != null) {
      mPendingSelectionNotifier.run();
    }
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    View localView = getSelectedView();
    return (localView != null) && (localView.getVisibility() == 0) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent));
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("scrolling:firstPosition", mFirstPosition);
    paramViewHierarchyEncoder.addProperty("list:nextSelectedPosition", mNextSelectedPosition);
    paramViewHierarchyEncoder.addProperty("list:nextSelectedRowId", (float)mNextSelectedRowId);
    paramViewHierarchyEncoder.addProperty("list:selectedPosition", mSelectedPosition);
    paramViewHierarchyEncoder.addProperty("list:itemCount", mItemCount);
  }
  
  int findSyncPosition()
  {
    int i = mItemCount;
    if (i == 0) {
      return -1;
    }
    long l1 = mSyncRowId;
    int j = mSyncPosition;
    if (l1 == Long.MIN_VALUE) {
      return -1;
    }
    j = Math.min(i - 1, Math.max(0, j));
    long l2 = SystemClock.uptimeMillis();
    int k = j;
    int m = j;
    int n = 0;
    Adapter localAdapter = getAdapter();
    if (localAdapter == null) {
      return -1;
    }
    while (SystemClock.uptimeMillis() <= l2 + 100L)
    {
      if (localAdapter.getItemId(j) == l1) {
        return j;
      }
      int i1 = 1;
      int i2;
      if (m == i - 1) {
        i2 = 1;
      } else {
        i2 = 0;
      }
      if (k != 0) {
        i1 = 0;
      }
      if ((i2 != 0) && (i1 != 0)) {
        break;
      }
      if ((i1 == 0) && ((n == 0) || (i2 != 0)))
      {
        if ((i2 != 0) || ((n == 0) && (i1 == 0)))
        {
          k--;
          j = k;
          n = 1;
        }
      }
      else
      {
        m++;
        j = m;
        n = 0;
      }
    }
    return -1;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return AdapterView.class.getName();
  }
  
  public abstract T getAdapter();
  
  @ViewDebug.CapturedViewProperty
  public int getCount()
  {
    return mItemCount;
  }
  
  public View getEmptyView()
  {
    return mEmptyView;
  }
  
  public int getFirstVisiblePosition()
  {
    return mFirstPosition;
  }
  
  public Object getItemAtPosition(int paramInt)
  {
    Object localObject = getAdapter();
    if ((localObject != null) && (paramInt >= 0)) {
      localObject = ((Adapter)localObject).getItem(paramInt);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public long getItemIdAtPosition(int paramInt)
  {
    Adapter localAdapter = getAdapter();
    long l;
    if ((localAdapter != null) && (paramInt >= 0)) {
      l = localAdapter.getItemId(paramInt);
    } else {
      l = Long.MIN_VALUE;
    }
    return l;
  }
  
  public int getLastVisiblePosition()
  {
    return mFirstPosition + getChildCount() - 1;
  }
  
  public final OnItemClickListener getOnItemClickListener()
  {
    return mOnItemClickListener;
  }
  
  public final OnItemLongClickListener getOnItemLongClickListener()
  {
    return mOnItemLongClickListener;
  }
  
  public final OnItemSelectedListener getOnItemSelectedListener()
  {
    return mOnItemSelectedListener;
  }
  
  public int getPositionForView(View paramView)
  {
    try
    {
      for (;;)
      {
        View localView = (View)paramView.getParent();
        if (localView == null) {
          break;
        }
        boolean bool = localView.equals(this);
        if (bool) {
          break;
        }
        paramView = localView;
      }
      if (paramView != null)
      {
        int i = getChildCount();
        for (int j = 0; j < i; j++) {
          if (getChildAt(j).equals(paramView)) {
            return mFirstPosition + j;
          }
        }
      }
      return -1;
    }
    catch (ClassCastException paramView) {}
    return -1;
  }
  
  public Object getSelectedItem()
  {
    Adapter localAdapter = getAdapter();
    int i = getSelectedItemPosition();
    if ((localAdapter != null) && (localAdapter.getCount() > 0) && (i >= 0)) {
      return localAdapter.getItem(i);
    }
    return null;
  }
  
  @ViewDebug.CapturedViewProperty
  public long getSelectedItemId()
  {
    return mNextSelectedRowId;
  }
  
  @ViewDebug.CapturedViewProperty
  public int getSelectedItemPosition()
  {
    return mNextSelectedPosition;
  }
  
  public abstract View getSelectedView();
  
  void handleDataChanged()
  {
    int i = mItemCount;
    int j = 0;
    int k = 0;
    if (i > 0)
    {
      int m = k;
      if (mNeedSync)
      {
        mNeedSync = false;
        j = findSyncPosition();
        m = k;
        if (j >= 0)
        {
          m = k;
          if (lookForSelectablePosition(j, true) == j)
          {
            setNextSelectedPositionInt(j);
            m = 1;
          }
        }
      }
      j = m;
      if (m == 0)
      {
        k = getSelectedItemPosition();
        j = k;
        if (k >= i) {
          j = i - 1;
        }
        k = j;
        if (j < 0) {
          k = 0;
        }
        j = lookForSelectablePosition(k, true);
        i = j;
        if (j < 0) {
          i = lookForSelectablePosition(k, false);
        }
        j = m;
        if (i >= 0)
        {
          setNextSelectedPositionInt(i);
          checkSelectionChanged();
          j = 1;
        }
      }
    }
    if (j == 0)
    {
      mSelectedPosition = -1;
      mSelectedRowId = Long.MIN_VALUE;
      mNextSelectedPosition = -1;
      mNextSelectedRowId = Long.MIN_VALUE;
      mNeedSync = false;
      checkSelectionChanged();
    }
    notifySubtreeAccessibilityStateChangedIfNeeded();
  }
  
  boolean isInFilterMode()
  {
    return false;
  }
  
  int lookForSelectablePosition(int paramInt, boolean paramBoolean)
  {
    return paramInt;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks(mSelectionNotifier);
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    paramAccessibilityEvent.setScrollable(isScrollableForAccessibility());
    View localView = getSelectedView();
    if (localView != null) {
      paramAccessibilityEvent.setEnabled(localView.isEnabled());
    }
    paramAccessibilityEvent.setCurrentItemIndex(getSelectedItemPosition());
    paramAccessibilityEvent.setFromIndex(getFirstVisiblePosition());
    paramAccessibilityEvent.setToIndex(getLastVisiblePosition());
    paramAccessibilityEvent.setItemCount(getCount());
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setScrollable(isScrollableForAccessibility());
    View localView = getSelectedView();
    if (localView != null) {
      paramAccessibilityNodeInfo.setEnabled(localView.isEnabled());
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mLayoutHeight = getHeight();
  }
  
  public void onProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    super.onProvideAutofillStructure(paramViewStructure, paramInt);
    Object localObject = getAdapter();
    if (localObject == null) {
      return;
    }
    localObject = ((Adapter)localObject).getAutofillOptions();
    if (localObject != null) {
      paramViewStructure.setAutofillOptions((CharSequence[])localObject);
    }
  }
  
  public boolean onRequestSendAccessibilityEventInternal(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    if (super.onRequestSendAccessibilityEventInternal(paramView, paramAccessibilityEvent))
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
      onInitializeAccessibilityEvent(localAccessibilityEvent);
      paramView.dispatchPopulateAccessibilityEvent(localAccessibilityEvent);
      paramAccessibilityEvent.appendRecord(localAccessibilityEvent);
      return true;
    }
    return false;
  }
  
  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    OnItemClickListener localOnItemClickListener = mOnItemClickListener;
    boolean bool = false;
    if (localOnItemClickListener != null)
    {
      playSoundEffect(0);
      mOnItemClickListener.onItemClick(this, paramView, paramInt, paramLong);
      bool = true;
    }
    if (paramView != null) {
      paramView.sendAccessibilityEvent(1);
    }
    return bool;
  }
  
  void rememberSyncState()
  {
    if (getChildCount() > 0)
    {
      mNeedSync = true;
      mSyncHeight = mLayoutHeight;
      Object localObject;
      if (mSelectedPosition >= 0)
      {
        localObject = getChildAt(mSelectedPosition - mFirstPosition);
        mSyncRowId = mNextSelectedRowId;
        mSyncPosition = mNextSelectedPosition;
        if (localObject != null) {
          mSpecificTop = ((View)localObject).getTop();
        }
        mSyncMode = 0;
      }
      else
      {
        View localView = getChildAt(0);
        localObject = getAdapter();
        if ((mFirstPosition >= 0) && (mFirstPosition < ((Adapter)localObject).getCount())) {
          mSyncRowId = ((Adapter)localObject).getItemId(mFirstPosition);
        } else {
          mSyncRowId = -1L;
        }
        mSyncPosition = mFirstPosition;
        if (localView != null) {
          mSpecificTop = localView.getTop();
        }
        mSyncMode = 1;
      }
    }
  }
  
  public void removeAllViews()
  {
    throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
  }
  
  public void removeView(View paramView)
  {
    throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
  }
  
  public void removeViewAt(int paramInt)
  {
    throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
  }
  
  void selectionChanged()
  {
    mPendingSelectionNotifier = null;
    if ((mOnItemSelectedListener != null) || (AccessibilityManager.getInstance(mContext).isEnabled())) {
      if ((!mInLayout) && (!mBlockLayoutRequests))
      {
        dispatchOnItemSelected();
      }
      else
      {
        if (mSelectionNotifier == null) {
          mSelectionNotifier = new SelectionNotifier(null);
        } else {
          removeCallbacks(mSelectionNotifier);
        }
        post(mSelectionNotifier);
      }
    }
    AutofillManager localAutofillManager = (AutofillManager)mContext.getSystemService(AutofillManager.class);
    if (localAutofillManager != null) {
      localAutofillManager.notifyValueChanged(this);
    }
  }
  
  public abstract void setAdapter(T paramT);
  
  @RemotableViewMethod
  public void setEmptyView(View paramView)
  {
    mEmptyView = paramView;
    boolean bool1 = true;
    if ((paramView != null) && (paramView.getImportantForAccessibility() == 0)) {
      paramView.setImportantForAccessibility(1);
    }
    paramView = getAdapter();
    boolean bool2 = bool1;
    if (paramView != null) {
      if (paramView.isEmpty()) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    updateEmptyStatus(bool2);
  }
  
  public void setFocusable(int paramInt)
  {
    Adapter localAdapter = getAdapter();
    int i = 0;
    int j;
    if ((localAdapter != null) && (localAdapter.getCount() != 0)) {
      j = 0;
    } else {
      j = 1;
    }
    mDesiredFocusableState = paramInt;
    if ((paramInt & 0x11) == 0) {
      mDesiredFocusableInTouchModeState = false;
    }
    if ((j != 0) && (!isInFilterMode())) {
      paramInt = i;
    }
    super.setFocusable(paramInt);
  }
  
  public void setFocusableInTouchMode(boolean paramBoolean)
  {
    Adapter localAdapter = getAdapter();
    boolean bool1 = false;
    int i;
    if ((localAdapter != null) && (localAdapter.getCount() != 0)) {
      i = 0;
    } else {
      i = 1;
    }
    mDesiredFocusableInTouchModeState = paramBoolean;
    if (paramBoolean) {
      mDesiredFocusableState = 1;
    }
    boolean bool2 = bool1;
    if (paramBoolean) {
      if (i != 0)
      {
        bool2 = bool1;
        if (!isInFilterMode()) {}
      }
      else
      {
        bool2 = true;
      }
    }
    super.setFocusableInTouchMode(bool2);
  }
  
  void setNextSelectedPositionInt(int paramInt)
  {
    mNextSelectedPosition = paramInt;
    mNextSelectedRowId = getItemIdAtPosition(paramInt);
    if ((mNeedSync) && (mSyncMode == 0) && (paramInt >= 0))
    {
      mSyncPosition = paramInt;
      mSyncRowId = mNextSelectedRowId;
    }
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
  }
  
  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    mOnItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemLongClickListener(OnItemLongClickListener paramOnItemLongClickListener)
  {
    if (!isLongClickable()) {
      setLongClickable(true);
    }
    mOnItemLongClickListener = paramOnItemLongClickListener;
  }
  
  public void setOnItemSelectedListener(OnItemSelectedListener paramOnItemSelectedListener)
  {
    mOnItemSelectedListener = paramOnItemSelectedListener;
  }
  
  void setSelectedPositionInt(int paramInt)
  {
    mSelectedPosition = paramInt;
    mSelectedRowId = getItemIdAtPosition(paramInt);
  }
  
  public abstract void setSelection(int paramInt);
  
  public static class AdapterContextMenuInfo
    implements ContextMenu.ContextMenuInfo
  {
    public long id;
    public int position;
    public View targetView;
    
    public AdapterContextMenuInfo(View paramView, int paramInt, long paramLong)
    {
      targetView = paramView;
      position = paramInt;
      id = paramLong;
    }
  }
  
  class AdapterDataSetObserver
    extends DataSetObserver
  {
    private Parcelable mInstanceState = null;
    
    AdapterDataSetObserver() {}
    
    public void clearSavedState()
    {
      mInstanceState = null;
    }
    
    public void onChanged()
    {
      mDataChanged = true;
      mOldItemCount = mItemCount;
      mItemCount = getAdapter().getCount();
      if ((getAdapter().hasStableIds()) && (mInstanceState != null) && (mOldItemCount == 0) && (mItemCount > 0))
      {
        onRestoreInstanceState(mInstanceState);
        mInstanceState = null;
      }
      else
      {
        rememberSyncState();
      }
      checkFocus();
      requestLayout();
    }
    
    public void onInvalidated()
    {
      mDataChanged = true;
      if (getAdapter().hasStableIds()) {
        mInstanceState = onSaveInstanceState();
      }
      mOldItemCount = mItemCount;
      mItemCount = 0;
      mSelectedPosition = -1;
      mSelectedRowId = Long.MIN_VALUE;
      mNextSelectedPosition = -1;
      mNextSelectedRowId = Long.MIN_VALUE;
      mNeedSync = false;
      checkFocus();
      requestLayout();
    }
  }
  
  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong);
  }
  
  public static abstract interface OnItemLongClickListener
  {
    public abstract boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong);
  }
  
  public static abstract interface OnItemSelectedListener
  {
    public abstract void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong);
    
    public abstract void onNothingSelected(AdapterView<?> paramAdapterView);
  }
  
  private class SelectionNotifier
    implements Runnable
  {
    private SelectionNotifier() {}
    
    public void run()
    {
      AdapterView.access$202(AdapterView.this, null);
      if ((mDataChanged) && (getViewRootImpl() != null) && (getViewRootImpl().isLayoutRequested()))
      {
        if (getAdapter() != null) {
          AdapterView.access$202(AdapterView.this, this);
        }
      }
      else {
        AdapterView.this.dispatchOnItemSelected();
      }
    }
  }
}
