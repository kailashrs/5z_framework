package android.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ShowableListMenu;

public class Spinner
  extends AbsSpinner
  implements DialogInterface.OnClickListener
{
  private static final int MAX_ITEMS_MEASURED = 15;
  public static final int MODE_DIALOG = 0;
  public static final int MODE_DROPDOWN = 1;
  private static final int MODE_THEME = -1;
  private static final String TAG = "Spinner";
  private boolean mDisableChildrenWhenDisabled;
  int mDropDownWidth;
  private ForwardingListener mForwardingListener;
  private int mGravity;
  private SpinnerPopup mPopup;
  private final Context mPopupContext;
  private SpinnerAdapter mTempAdapter;
  private final Rect mTempRect = new Rect();
  
  public Spinner(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Spinner(Context paramContext, int paramInt)
  {
    this(paramContext, null, 16842881, paramInt);
  }
  
  public Spinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842881);
  }
  
  public Spinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0, -1);
  }
  
  public Spinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    this(paramContext, paramAttributeSet, paramInt1, 0, paramInt2);
  }
  
  public Spinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, paramInt3, null);
  }
  
  public Spinner(final Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, int paramInt3, Resources.Theme paramTheme)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Spinner, paramInt1, paramInt2);
    if (paramTheme != null)
    {
      mPopupContext = new ContextThemeWrapper(paramContext, paramTheme);
    }
    else
    {
      int i = localTypedArray.getResourceId(7, 0);
      if (i != 0) {
        mPopupContext = new ContextThemeWrapper(paramContext, i);
      } else {
        mPopupContext = paramContext;
      }
    }
    if (paramInt3 == -1) {
      paramInt3 = localTypedArray.getInt(5, 0);
    }
    switch (paramInt3)
    {
    default: 
      break;
    case 1: 
      paramContext = new DropdownPopup(mPopupContext, paramAttributeSet, paramInt1, paramInt2);
      paramAttributeSet = mPopupContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Spinner, paramInt1, paramInt2);
      mDropDownWidth = paramAttributeSet.getLayoutDimension(4, -2);
      if (paramAttributeSet.hasValueOrEmpty(1)) {
        paramContext.setListSelector(paramAttributeSet.getDrawable(1));
      }
      paramContext.setBackgroundDrawable(paramAttributeSet.getDrawable(2));
      paramContext.setPromptText(localTypedArray.getString(3));
      paramAttributeSet.recycle();
      mPopup = paramContext;
      mForwardingListener = new ForwardingListener(this)
      {
        public ShowableListMenu getPopup()
        {
          return paramContext;
        }
        
        public boolean onForwardingStarted()
        {
          if (!mPopup.isShowing()) {
            mPopup.show(getTextDirection(), getTextAlignment());
          }
          return true;
        }
      };
      break;
    case 0: 
      mPopup = new DialogPopup(null);
      mPopup.setPromptText(localTypedArray.getString(3));
    }
    mGravity = localTypedArray.getInt(0, 17);
    mDisableChildrenWhenDisabled = localTypedArray.getBoolean(8, false);
    localTypedArray.recycle();
    if (mTempAdapter != null)
    {
      setAdapter(mTempAdapter);
      mTempAdapter = null;
    }
  }
  
  private View makeView(int paramInt, boolean paramBoolean)
  {
    if (!mDataChanged)
    {
      localView = mRecycler.get(paramInt);
      if (localView != null)
      {
        setUpChild(localView, paramBoolean);
        return localView;
      }
    }
    View localView = mAdapter.getView(paramInt, null, this);
    setUpChild(localView, paramBoolean);
    return localView;
  }
  
  private void setUpChild(View paramView, boolean paramBoolean)
  {
    ViewGroup.LayoutParams localLayoutParams1 = paramView.getLayoutParams();
    ViewGroup.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = generateDefaultLayoutParams();
    }
    addViewInLayout(paramView, 0, localLayoutParams2);
    paramView.setSelected(hasFocus());
    if (mDisableChildrenWhenDisabled) {
      paramView.setEnabled(isEnabled());
    }
    int i = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, height);
    paramView.measure(ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, width), i);
    int j = mSpinnerPadding.top + (getMeasuredHeight() - mSpinnerPadding.bottom - mSpinnerPadding.top - paramView.getMeasuredHeight()) / 2;
    i = paramView.getMeasuredHeight();
    paramView.layout(0, j, 0 + paramView.getMeasuredWidth(), i + j);
    if (!paramBoolean) {
      removeViewInLayout(paramView);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return Spinner.class.getName();
  }
  
  public int getBaseline()
  {
    Object localObject1 = null;
    Object localObject2;
    if (getChildCount() > 0)
    {
      localObject2 = getChildAt(0);
    }
    else
    {
      localObject2 = localObject1;
      if (mAdapter != null)
      {
        localObject2 = localObject1;
        if (mAdapter.getCount() > 0)
        {
          localObject2 = makeView(0, false);
          mRecycler.put(0, (View)localObject2);
        }
      }
    }
    int i = -1;
    if (localObject2 != null)
    {
      int j = ((View)localObject2).getBaseline();
      if (j >= 0) {
        i = ((View)localObject2).getTop() + j;
      }
      return i;
    }
    return -1;
  }
  
  public int getDropDownHorizontalOffset()
  {
    return mPopup.getHorizontalOffset();
  }
  
  public int getDropDownVerticalOffset()
  {
    return mPopup.getVerticalOffset();
  }
  
  public int getDropDownWidth()
  {
    return mDropDownWidth;
  }
  
  public int getGravity()
  {
    return mGravity;
  }
  
  public Drawable getPopupBackground()
  {
    return mPopup.getBackground();
  }
  
  public Context getPopupContext()
  {
    return mPopupContext;
  }
  
  public CharSequence getPrompt()
  {
    return mPopup.getHintText();
  }
  
  public boolean isPopupShowing()
  {
    boolean bool;
    if ((mPopup != null) && (mPopup.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void layout(int paramInt, boolean paramBoolean)
  {
    int i = mSpinnerPadding.left;
    int j = mRight - mLeft - mSpinnerPadding.left - mSpinnerPadding.right;
    if (mDataChanged) {
      handleDataChanged();
    }
    if (mItemCount == 0)
    {
      resetList();
      return;
    }
    if (mNextSelectedPosition >= 0) {
      setSelectedPositionInt(mNextSelectedPosition);
    }
    recycleAllViews();
    removeAllViewsInLayout();
    mFirstPosition = mSelectedPosition;
    if (mAdapter != null)
    {
      View localView = makeView(mSelectedPosition, true);
      int k = localView.getMeasuredWidth();
      paramInt = i;
      int m = getLayoutDirection();
      m = Gravity.getAbsoluteGravity(mGravity, m) & 0x7;
      if (m != 1)
      {
        if (m == 5) {
          paramInt = i + j - k;
        }
      }
      else {
        paramInt = j / 2 + i - k / 2;
      }
      localView.offsetLeftAndRight(paramInt);
    }
    mRecycler.clear();
    invalidate();
    checkSelectionChanged();
    mDataChanged = false;
    mNeedSync = false;
    setNextSelectedPositionInt(mSelectedPosition);
  }
  
  int measureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable)
  {
    if (paramSpinnerAdapter == null) {
      return 0;
    }
    View localView = null;
    int i = 0;
    int j = View.MeasureSpec.makeSafeMeasureSpec(getMeasuredWidth(), 0);
    int k = View.MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
    int m = Math.max(0, getSelectedItemPosition());
    int n = Math.min(paramSpinnerAdapter.getCount(), m + 15);
    int i1 = Math.max(0, m - (15 - (n - m)));
    m = 0;
    while (i1 < n)
    {
      int i2 = paramSpinnerAdapter.getItemViewType(i1);
      int i3 = i;
      if (i2 != i)
      {
        i3 = i2;
        localView = null;
      }
      localView = paramSpinnerAdapter.getView(i1, localView, this);
      if (localView.getLayoutParams() == null) {
        localView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
      }
      localView.measure(j, k);
      m = Math.max(m, localView.getMeasuredWidth());
      i1++;
      i = i3;
    }
    i1 = m;
    if (paramDrawable != null)
    {
      paramDrawable.getPadding(mTempRect);
      i1 = m + (mTempRect.left + mTempRect.right);
    }
    return i1;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    setSelection(paramInt);
    paramDialogInterface.dismiss();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if ((mPopup != null) && (mPopup.isShowing())) {
      mPopup.dismiss();
    }
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (mAdapter != null) {
      paramAccessibilityNodeInfo.setCanOpenPopup(true);
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    mInLayout = true;
    layout(0, false);
    mInLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if ((mPopup != null) && (View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE))
    {
      paramInt2 = getMeasuredWidth();
      setMeasuredDimension(Math.min(Math.max(paramInt2, measureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight());
    }
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if ((getPointerIcon() == null) && (isClickable()) && (isEnabled())) {
      return PointerIcon.getSystemIcon(getContext(), 1002);
    }
    return super.onResolvePointerIcon(paramMotionEvent, paramInt);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if (showDropdown)
    {
      paramParcelable = getViewTreeObserver();
      if (paramParcelable != null) {
        paramParcelable.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            if (!mPopup.isShowing()) {
              mPopup.show(getTextDirection(), getTextAlignment());
            }
            ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
            if (localViewTreeObserver != null) {
              localViewTreeObserver.removeOnGlobalLayoutListener(this);
            }
          }
        });
      }
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    boolean bool;
    if ((mPopup != null) && (mPopup.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    showDropdown = bool;
    return localSavedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((mForwardingListener != null) && (mForwardingListener.onTouch(this, paramMotionEvent))) {
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick()
  {
    boolean bool1 = super.performClick();
    boolean bool2 = bool1;
    if (!bool1)
    {
      bool1 = true;
      bool2 = bool1;
      if (!mPopup.isShowing())
      {
        mPopup.show(getTextDirection(), getTextAlignment());
        bool2 = bool1;
      }
    }
    return bool2;
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    if (mPopup == null)
    {
      mTempAdapter = paramSpinnerAdapter;
      return;
    }
    super.setAdapter(paramSpinnerAdapter);
    mRecycler.clear();
    if ((mContext.getApplicationInfo().targetSdkVersion >= 21) && (paramSpinnerAdapter != null) && (paramSpinnerAdapter.getViewTypeCount() != 1)) {
      throw new IllegalArgumentException("Spinner adapter view type count must be 1");
    }
    Context localContext;
    if (mPopupContext == null) {
      localContext = mContext;
    } else {
      localContext = mPopupContext;
    }
    mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, localContext.getTheme()));
  }
  
  public void setDropDownHorizontalOffset(int paramInt)
  {
    mPopup.setHorizontalOffset(paramInt);
  }
  
  public void setDropDownVerticalOffset(int paramInt)
  {
    mPopup.setVerticalOffset(paramInt);
  }
  
  public void setDropDownWidth(int paramInt)
  {
    if (!(mPopup instanceof DropdownPopup))
    {
      Log.e("Spinner", "Cannot set dropdown width for MODE_DIALOG, ignoring");
      return;
    }
    mDropDownWidth = paramInt;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (mDisableChildrenWhenDisabled)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        getChildAt(j).setEnabled(paramBoolean);
      }
    }
  }
  
  public void setGravity(int paramInt)
  {
    if (mGravity != paramInt)
    {
      int i = paramInt;
      if ((paramInt & 0x7) == 0) {
        i = paramInt | 0x800003;
      }
      mGravity = i;
      requestLayout();
    }
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    throw new RuntimeException("setOnItemClickListener cannot be used with a spinner.");
  }
  
  public void setOnItemClickListenerInt(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    super.setOnItemClickListener(paramOnItemClickListener);
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable)
  {
    if (!(mPopup instanceof DropdownPopup))
    {
      Log.e("Spinner", "setPopupBackgroundDrawable: incompatible spinner mode; ignoring...");
      return;
    }
    mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setPopupBackgroundResource(int paramInt)
  {
    setPopupBackgroundDrawable(getPopupContext().getDrawable(paramInt));
  }
  
  public void setPrompt(CharSequence paramCharSequence)
  {
    mPopup.setPromptText(paramCharSequence);
  }
  
  public void setPromptId(int paramInt)
  {
    setPrompt(getContext().getText(paramInt));
  }
  
  private class DialogPopup
    implements Spinner.SpinnerPopup, DialogInterface.OnClickListener
  {
    private ListAdapter mListAdapter;
    private AlertDialog mPopup;
    private CharSequence mPrompt;
    
    private DialogPopup() {}
    
    public void dismiss()
    {
      if (mPopup != null)
      {
        mPopup.dismiss();
        mPopup = null;
      }
    }
    
    public Drawable getBackground()
    {
      return null;
    }
    
    public CharSequence getHintText()
    {
      return mPrompt;
    }
    
    public int getHorizontalOffset()
    {
      return 0;
    }
    
    public int getVerticalOffset()
    {
      return 0;
    }
    
    public boolean isShowing()
    {
      boolean bool;
      if (mPopup != null) {
        bool = mPopup.isShowing();
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      setSelection(paramInt);
      if (mOnItemClickListener != null) {
        performItemClick(null, paramInt, mListAdapter.getItemId(paramInt));
      }
      dismiss();
    }
    
    public void setAdapter(ListAdapter paramListAdapter)
    {
      mListAdapter = paramListAdapter;
    }
    
    public void setBackgroundDrawable(Drawable paramDrawable)
    {
      Log.e("Spinner", "Cannot set popup background for MODE_DIALOG, ignoring");
    }
    
    public void setHorizontalOffset(int paramInt)
    {
      Log.e("Spinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
    }
    
    public void setPromptText(CharSequence paramCharSequence)
    {
      mPrompt = paramCharSequence;
    }
    
    public void setVerticalOffset(int paramInt)
    {
      Log.e("Spinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
    }
    
    public void show(int paramInt1, int paramInt2)
    {
      if (mListAdapter == null) {
        return;
      }
      Object localObject = new AlertDialog.Builder(getPopupContext());
      if (mPrompt != null) {
        ((AlertDialog.Builder)localObject).setTitle(mPrompt);
      }
      mPopup = ((AlertDialog.Builder)localObject).setSingleChoiceItems(mListAdapter, getSelectedItemPosition(), this).create();
      localObject = mPopup.getListView();
      ((ListView)localObject).setTextDirection(paramInt1);
      ((ListView)localObject).setTextAlignment(paramInt2);
      mPopup.show();
    }
  }
  
  private static class DropDownAdapter
    implements ListAdapter, SpinnerAdapter
  {
    private SpinnerAdapter mAdapter;
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(SpinnerAdapter paramSpinnerAdapter, Resources.Theme paramTheme)
    {
      mAdapter = paramSpinnerAdapter;
      if ((paramSpinnerAdapter instanceof ListAdapter)) {
        mListAdapter = ((ListAdapter)paramSpinnerAdapter);
      }
      if ((paramTheme != null) && ((paramSpinnerAdapter instanceof ThemedSpinnerAdapter)))
      {
        paramSpinnerAdapter = (ThemedSpinnerAdapter)paramSpinnerAdapter;
        if (paramSpinnerAdapter.getDropDownViewTheme() == null) {
          paramSpinnerAdapter.setDropDownViewTheme(paramTheme);
        }
      }
    }
    
    public boolean areAllItemsEnabled()
    {
      ListAdapter localListAdapter = mListAdapter;
      if (localListAdapter != null) {
        return localListAdapter.areAllItemsEnabled();
      }
      return true;
    }
    
    public int getCount()
    {
      int i;
      if (mAdapter == null) {
        i = 0;
      } else {
        i = mAdapter.getCount();
      }
      return i;
    }
    
    public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (mAdapter == null) {
        paramView = null;
      } else {
        paramView = mAdapter.getDropDownView(paramInt, paramView, paramViewGroup);
      }
      return paramView;
    }
    
    public Object getItem(int paramInt)
    {
      Object localObject;
      if (mAdapter == null) {
        localObject = null;
      } else {
        localObject = mAdapter.getItem(paramInt);
      }
      return localObject;
    }
    
    public long getItemId(int paramInt)
    {
      long l;
      if (mAdapter == null) {
        l = -1L;
      } else {
        l = mAdapter.getItemId(paramInt);
      }
      return l;
    }
    
    public int getItemViewType(int paramInt)
    {
      return 0;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return getDropDownView(paramInt, paramView, paramViewGroup);
    }
    
    public int getViewTypeCount()
    {
      return 1;
    }
    
    public boolean hasStableIds()
    {
      boolean bool;
      if ((mAdapter != null) && (mAdapter.hasStableIds())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if (getCount() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isEnabled(int paramInt)
    {
      ListAdapter localListAdapter = mListAdapter;
      if (localListAdapter != null) {
        return localListAdapter.isEnabled(paramInt);
      }
      return true;
    }
    
    public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      if (mAdapter != null) {
        mAdapter.registerDataSetObserver(paramDataSetObserver);
      }
    }
    
    public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      if (mAdapter != null) {
        mAdapter.unregisterDataSetObserver(paramDataSetObserver);
      }
    }
  }
  
  private class DropdownPopup
    extends ListPopupWindow
    implements Spinner.SpinnerPopup
  {
    private ListAdapter mAdapter;
    private CharSequence mHintText;
    
    public DropdownPopup(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      super(paramAttributeSet, paramInt1, paramInt2);
      setAnchorView(Spinner.this);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          setSelection(paramAnonymousInt);
          if (mOnItemClickListener != null) {
            performItemClick(paramAnonymousView, paramAnonymousInt, mAdapter.getItemId(paramAnonymousInt));
          }
          dismiss();
        }
      });
    }
    
    void computeContentWidth()
    {
      Object localObject = getBackground();
      int i = 0;
      if (localObject != null)
      {
        ((Drawable)localObject).getPadding(mTempRect);
        if (isLayoutRtl()) {
          i = mTempRect.right;
        } else {
          i = -mTempRect.left;
        }
      }
      else
      {
        localObject = mTempRect;
        mTempRect.right = 0;
        left = 0;
      }
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getWidth();
      if (mDropDownWidth == -2)
      {
        int n = measureContentWidth((SpinnerAdapter)mAdapter, getBackground());
        int i1 = mContext.getResources().getDisplayMetrics().widthPixels - mTempRect.left - mTempRect.right;
        int i2 = n;
        if (n > i1) {
          i2 = i1;
        }
        setContentWidth(Math.max(i2, m - j - k));
      }
      else if (mDropDownWidth == -1)
      {
        setContentWidth(m - j - k);
      }
      else
      {
        setContentWidth(mDropDownWidth);
      }
      if (isLayoutRtl()) {
        i += m - k - getWidth();
      } else {
        i += j;
      }
      setHorizontalOffset(i);
    }
    
    public CharSequence getHintText()
    {
      return mHintText;
    }
    
    public void setAdapter(ListAdapter paramListAdapter)
    {
      super.setAdapter(paramListAdapter);
      mAdapter = paramListAdapter;
    }
    
    public void setPromptText(CharSequence paramCharSequence)
    {
      mHintText = paramCharSequence;
    }
    
    public void show(int paramInt1, int paramInt2)
    {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      super.show();
      Object localObject = getListView();
      ((ListView)localObject).setChoiceMode(1);
      ((ListView)localObject).setTextDirection(paramInt1);
      ((ListView)localObject).setTextAlignment(paramInt2);
      setSelection(getSelectedItemPosition());
      if (bool) {
        return;
      }
      ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
      if (localViewTreeObserver != null)
      {
        localObject = new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            if (!isVisibleToUser())
            {
              dismiss();
            }
            else
            {
              computeContentWidth();
              Spinner.DropdownPopup.this.show();
            }
          }
        };
        localViewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)localObject);
        setOnDismissListener(new PopupWindow.OnDismissListener()
        {
          public void onDismiss()
          {
            ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
            if (localViewTreeObserver != null) {
              localViewTreeObserver.removeOnGlobalLayoutListener(val$layoutListener);
            }
          }
        });
      }
    }
  }
  
  static class SavedState
    extends AbsSpinner.SavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public Spinner.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Spinner.SavedState(paramAnonymousParcel, null);
      }
      
      public Spinner.SavedState[] newArray(int paramAnonymousInt)
      {
        return new Spinner.SavedState[paramAnonymousInt];
      }
    };
    boolean showDropdown;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      boolean bool;
      if (paramParcel.readByte() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      showDropdown = bool;
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeByte((byte)showDropdown);
    }
  }
  
  private static abstract interface SpinnerPopup
  {
    public abstract void dismiss();
    
    public abstract Drawable getBackground();
    
    public abstract CharSequence getHintText();
    
    public abstract int getHorizontalOffset();
    
    public abstract int getVerticalOffset();
    
    public abstract boolean isShowing();
    
    public abstract void setAdapter(ListAdapter paramListAdapter);
    
    public abstract void setBackgroundDrawable(Drawable paramDrawable);
    
    public abstract void setHorizontalOffset(int paramInt);
    
    public abstract void setPromptText(CharSequence paramCharSequence);
    
    public abstract void setVerticalOffset(int paramInt);
    
    public abstract void show(int paramInt1, int paramInt2);
  }
}
