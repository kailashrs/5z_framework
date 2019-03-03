package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ShowableListMenu;

public class ListPopupWindow
  implements ShowableListMenu
{
  private static final boolean DEBUG = false;
  private static final int EXPAND_LIST_TIMEOUT = 250;
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  public static final int INPUT_METHOD_NEEDED = 1;
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  public static final int MATCH_PARENT = -1;
  public static final int POSITION_PROMPT_ABOVE = 0;
  public static final int POSITION_PROMPT_BELOW = 1;
  private static final String TAG = "ListPopupWindow";
  public static final int WRAP_CONTENT = -2;
  private ListAdapter mAdapter;
  private Context mContext;
  private boolean mDropDownAlwaysVisible = false;
  private View mDropDownAnchorView;
  private int mDropDownGravity = 0;
  private int mDropDownHeight = -2;
  private int mDropDownHorizontalOffset;
  private DropDownListView mDropDownList;
  private Drawable mDropDownListHighlight;
  private int mDropDownVerticalOffset;
  private boolean mDropDownVerticalOffsetSet;
  private int mDropDownWidth = -2;
  private int mDropDownWindowLayoutType = 1002;
  private Rect mEpicenterBounds;
  private boolean mForceIgnoreOutsideTouch = false;
  private final Handler mHandler;
  private final ListSelectorHider mHideSelector = new ListSelectorHider(null);
  private boolean mIsAnimatedFromAnchor = true;
  private AdapterView.OnItemClickListener mItemClickListener;
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  int mListItemExpandMaximum = Integer.MAX_VALUE;
  private boolean mModal;
  private DataSetObserver mObserver;
  private boolean mOverlapAnchor;
  private boolean mOverlapAnchorSet;
  PopupWindow mPopup;
  private int mPromptPosition = 0;
  private View mPromptView;
  private final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable(null);
  private final PopupScrollListener mScrollListener = new PopupScrollListener(null);
  private Runnable mShowDropDownRunnable;
  private final Rect mTempRect = new Rect();
  private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor(null);
  
  public ListPopupWindow(Context paramContext)
  {
    this(paramContext, null, 16843519, 0);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843519, 0);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    mContext = paramContext;
    mHandler = new Handler(paramContext.getMainLooper());
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPopupWindow, paramInt1, paramInt2);
    mDropDownHorizontalOffset = localTypedArray.getDimensionPixelOffset(0, 0);
    mDropDownVerticalOffset = localTypedArray.getDimensionPixelOffset(1, 0);
    if (mDropDownVerticalOffset != 0) {
      mDropDownVerticalOffsetSet = true;
    }
    localTypedArray.recycle();
    mPopup = new PopupWindow(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mPopup.setInputMethodMode(1);
  }
  
  private int buildDropDown()
  {
    int i = 0;
    int j = 0;
    Object localObject1 = mDropDownList;
    boolean bool = false;
    Object localObject3;
    int k;
    if (localObject1 == null)
    {
      Object localObject2 = mContext;
      mShowDropDownRunnable = new Runnable()
      {
        public void run()
        {
          View localView = getAnchorView();
          if ((localView != null) && (localView.getWindowToken() != null)) {
            show();
          }
        }
      };
      mDropDownList = createDropDownListView((Context)localObject2, mModal ^ true);
      if (mDropDownListHighlight != null) {
        mDropDownList.setSelector(mDropDownListHighlight);
      }
      mDropDownList.setAdapter(mAdapter);
      mDropDownList.setOnItemClickListener(mItemClickListener);
      mDropDownList.setFocusable(true);
      mDropDownList.setFocusableInTouchMode(true);
      mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
        public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          if (paramAnonymousInt != -1)
          {
            paramAnonymousAdapterView = mDropDownList;
            if (paramAnonymousAdapterView != null) {
              paramAnonymousAdapterView.setListSelectionHidden(false);
            }
          }
        }
        
        public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {}
      });
      mDropDownList.setOnScrollListener(mScrollListener);
      if (mItemSelectedListener != null) {
        mDropDownList.setOnItemSelectedListener(mItemSelectedListener);
      }
      localObject3 = mDropDownList;
      View localView = mPromptView;
      localObject1 = localObject3;
      if (localView != null)
      {
        localObject1 = new LinearLayout((Context)localObject2);
        ((LinearLayout)localObject1).setOrientation(1);
        localObject2 = new LinearLayout.LayoutParams(-1, 0, 1.0F);
        switch (mPromptPosition)
        {
        default: 
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Invalid hint position ");
          ((StringBuilder)localObject3).append(mPromptPosition);
          Log.e("ListPopupWindow", ((StringBuilder)localObject3).toString());
          break;
        case 1: 
          ((LinearLayout)localObject1).addView((View)localObject3, (ViewGroup.LayoutParams)localObject2);
          ((LinearLayout)localObject1).addView(localView);
          break;
        case 0: 
          ((LinearLayout)localObject1).addView(localView);
          ((LinearLayout)localObject1).addView((View)localObject3, (ViewGroup.LayoutParams)localObject2);
        }
        if (mDropDownWidth >= 0)
        {
          j = Integer.MIN_VALUE;
          i = mDropDownWidth;
        }
        else
        {
          j = 0;
          i = 0;
        }
        localView.measure(View.MeasureSpec.makeMeasureSpec(i, j), 0);
        localObject3 = (LinearLayout.LayoutParams)localView.getLayoutParams();
        j = localView.getMeasuredHeight();
        i = topMargin;
        k = bottomMargin;
        j = j + i + k;
      }
      mPopup.setContentView((View)localObject1);
    }
    else
    {
      localObject1 = mPromptView;
      j = i;
      if (localObject1 != null)
      {
        localObject3 = (LinearLayout.LayoutParams)((View)localObject1).getLayoutParams();
        j = ((View)localObject1).getMeasuredHeight() + topMargin + bottomMargin;
      }
    }
    localObject1 = mPopup.getBackground();
    if (localObject1 != null)
    {
      ((Drawable)localObject1).getPadding(mTempRect);
      i = mTempRect.top + mTempRect.bottom;
      k = i;
      if (!mDropDownVerticalOffsetSet)
      {
        mDropDownVerticalOffset = (-mTempRect.top);
        k = i;
      }
    }
    else
    {
      mTempRect.setEmpty();
      k = 0;
    }
    if (mPopup.getInputMethodMode() == 2) {
      bool = true;
    }
    int m = mPopup.getMaxAvailableHeight(getAnchorView(), mDropDownVerticalOffset, bool);
    if ((!mDropDownAlwaysVisible) && (mDropDownHeight != -1))
    {
      switch (mDropDownWidth)
      {
      default: 
        i = View.MeasureSpec.makeMeasureSpec(mDropDownWidth, 1073741824);
      }
      for (;;)
      {
        break;
        i = View.MeasureSpec.makeMeasureSpec(mContext.getResources().getDisplayMetrics().widthPixels - (mTempRect.left + mTempRect.right), 1073741824);
        continue;
        i = View.MeasureSpec.makeMeasureSpec(mContext.getResources().getDisplayMetrics().widthPixels - (mTempRect.left + mTempRect.right), Integer.MIN_VALUE);
      }
      m = mDropDownList.measureHeightOfChildren(i, 0, -1, m - j, -1);
      i = j;
      if (m > 0) {
        i = j + (k + (mDropDownList.getPaddingTop() + mDropDownList.getPaddingBottom()));
      }
      return m + i;
    }
    return m + k;
  }
  
  private void removePromptView()
  {
    if (mPromptView != null)
    {
      ViewParent localViewParent = mPromptView.getParent();
      if ((localViewParent instanceof ViewGroup)) {
        ((ViewGroup)localViewParent).removeView(mPromptView);
      }
    }
  }
  
  public void clearListSelection()
  {
    DropDownListView localDropDownListView = mDropDownList;
    if (localDropDownListView != null)
    {
      localDropDownListView.setListSelectionHidden(true);
      localDropDownListView.hideSelector();
      localDropDownListView.requestLayout();
    }
  }
  
  public View.OnTouchListener createDragToOpenListener(View paramView)
  {
    new ForwardingListener(paramView)
    {
      public ShowableListMenu getPopup()
      {
        return ListPopupWindow.this;
      }
    };
  }
  
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean)
  {
    return new DropDownListView(paramContext, paramBoolean);
  }
  
  public void dismiss()
  {
    mPopup.dismiss();
    removePromptView();
    mPopup.setContentView(null);
    mDropDownList = null;
    mHandler.removeCallbacks(mResizePopupRunnable);
  }
  
  public View getAnchorView()
  {
    return mDropDownAnchorView;
  }
  
  public int getAnimationStyle()
  {
    return mPopup.getAnimationStyle();
  }
  
  public Drawable getBackground()
  {
    return mPopup.getBackground();
  }
  
  public int getHeight()
  {
    return mDropDownHeight;
  }
  
  public int getHorizontalOffset()
  {
    return mDropDownHorizontalOffset;
  }
  
  public int getInputMethodMode()
  {
    return mPopup.getInputMethodMode();
  }
  
  public ListView getListView()
  {
    return mDropDownList;
  }
  
  public int getPromptPosition()
  {
    return mPromptPosition;
  }
  
  public Object getSelectedItem()
  {
    if (!isShowing()) {
      return null;
    }
    return mDropDownList.getSelectedItem();
  }
  
  public long getSelectedItemId()
  {
    if (!isShowing()) {
      return Long.MIN_VALUE;
    }
    return mDropDownList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition()
  {
    if (!isShowing()) {
      return -1;
    }
    return mDropDownList.getSelectedItemPosition();
  }
  
  public View getSelectedView()
  {
    if (!isShowing()) {
      return null;
    }
    return mDropDownList.getSelectedView();
  }
  
  public int getSoftInputMode()
  {
    return mPopup.getSoftInputMode();
  }
  
  public int getVerticalOffset()
  {
    if (!mDropDownVerticalOffsetSet) {
      return 0;
    }
    return mDropDownVerticalOffset;
  }
  
  public int getWidth()
  {
    return mDropDownWidth;
  }
  
  public boolean isDropDownAlwaysVisible()
  {
    return mDropDownAlwaysVisible;
  }
  
  public boolean isInputMethodNotNeeded()
  {
    boolean bool;
    if (mPopup.getInputMethodMode() == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isModal()
  {
    return mModal;
  }
  
  public boolean isShowing()
  {
    return mPopup.isShowing();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((isShowing()) && (paramInt != 62) && ((mDropDownList.getSelectedItemPosition() >= 0) || (!KeyEvent.isConfirmKey(paramInt))))
    {
      int i = mDropDownList.getSelectedItemPosition();
      boolean bool1 = mPopup.isAboveAnchor() ^ true;
      ListAdapter localListAdapter = mAdapter;
      int j = Integer.MAX_VALUE;
      int k = Integer.MIN_VALUE;
      if (localListAdapter != null)
      {
        boolean bool2 = localListAdapter.areAllItemsEnabled();
        if (bool2) {
          k = 0;
        } else {
          k = mDropDownList.lookForSelectablePosition(0, true);
        }
        j = k;
        if (bool2) {
          k = localListAdapter.getCount() - 1;
        } else {
          k = mDropDownList.lookForSelectablePosition(localListAdapter.getCount() - 1, false);
        }
      }
      if (((bool1) && (paramInt == 19) && (i <= j)) || ((!bool1) && (paramInt == 20) && (i >= k)))
      {
        clearListSelection();
        mPopup.setInputMethodMode(1);
        show();
        return true;
      }
      mDropDownList.setListSelectionHidden(false);
      if (mDropDownList.onKeyDown(paramInt, paramKeyEvent))
      {
        mPopup.setInputMethodMode(2);
        mDropDownList.requestFocusFromTouch();
        show();
        if ((paramInt != 23) && (paramInt != 66)) {}
        switch (paramInt)
        {
        default: 
          break;
        case 19: 
        case 20: 
          return true;
        }
      }
      else if ((bool1) && (paramInt == 20))
      {
        if (i == k) {
          return true;
        }
      }
      else if ((!bool1) && (paramInt == 19) && (i == j))
      {
        return true;
      }
    }
    return false;
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (isShowing()))
    {
      Object localObject = mDropDownAnchorView;
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        localObject = ((View)localObject).getKeyDispatcherState();
        if (localObject != null) {
          ((KeyEvent.DispatcherState)localObject).startTracking(paramKeyEvent, this);
        }
        return true;
      }
      if (paramKeyEvent.getAction() == 1)
      {
        localObject = ((View)localObject).getKeyDispatcherState();
        if (localObject != null) {
          ((KeyEvent.DispatcherState)localObject).handleUpEvent(paramKeyEvent);
        }
        if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
        {
          dismiss();
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((isShowing()) && (mDropDownList.getSelectedItemPosition() >= 0))
    {
      boolean bool = mDropDownList.onKeyUp(paramInt, paramKeyEvent);
      if ((bool) && (KeyEvent.isConfirmKey(paramInt))) {
        dismiss();
      }
      return bool;
    }
    return false;
  }
  
  public boolean performItemClick(int paramInt)
  {
    if (isShowing())
    {
      if (mItemClickListener != null)
      {
        DropDownListView localDropDownListView = mDropDownList;
        View localView = localDropDownListView.getChildAt(paramInt - localDropDownListView.getFirstVisiblePosition());
        ListAdapter localListAdapter = localDropDownListView.getAdapter();
        mItemClickListener.onItemClick(localDropDownListView, localView, paramInt, localListAdapter.getItemId(paramInt));
      }
      return true;
    }
    return false;
  }
  
  public void postShow()
  {
    mHandler.post(mShowDropDownRunnable);
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (mObserver == null) {
      mObserver = new PopupDataSetObserver(null);
    } else if (mAdapter != null) {
      mAdapter.unregisterDataSetObserver(mObserver);
    }
    mAdapter = paramListAdapter;
    if (mAdapter != null) {
      paramListAdapter.registerDataSetObserver(mObserver);
    }
    if (mDropDownList != null) {
      mDropDownList.setAdapter(mAdapter);
    }
  }
  
  public void setAnchorView(View paramView)
  {
    mDropDownAnchorView = paramView;
  }
  
  public void setAnimationStyle(int paramInt)
  {
    mPopup.setAnimationStyle(paramInt);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setContentWidth(int paramInt)
  {
    Drawable localDrawable = mPopup.getBackground();
    if (localDrawable != null)
    {
      localDrawable.getPadding(mTempRect);
      mDropDownWidth = (mTempRect.left + mTempRect.right + paramInt);
    }
    else
    {
      setWidth(paramInt);
    }
  }
  
  public void setDropDownAlwaysVisible(boolean paramBoolean)
  {
    mDropDownAlwaysVisible = paramBoolean;
  }
  
  public void setDropDownGravity(int paramInt)
  {
    mDropDownGravity = paramInt;
  }
  
  public void setEpicenterBounds(Rect paramRect)
  {
    mEpicenterBounds = paramRect;
  }
  
  public void setForceIgnoreOutsideTouch(boolean paramBoolean)
  {
    mForceIgnoreOutsideTouch = paramBoolean;
  }
  
  public void setHeight(int paramInt)
  {
    if ((paramInt < 0) && (-2 != paramInt) && (-1 != paramInt)) {
      if (mContext.getApplicationInfo().targetSdkVersion < 26)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Negative value ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" passed to ListPopupWindow#setHeight produces undefined results");
        Log.e("ListPopupWindow", localStringBuilder.toString());
      }
      else
      {
        throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
      }
    }
    mDropDownHeight = paramInt;
  }
  
  public void setHorizontalOffset(int paramInt)
  {
    mDropDownHorizontalOffset = paramInt;
  }
  
  public void setInputMethodMode(int paramInt)
  {
    mPopup.setInputMethodMode(paramInt);
  }
  
  void setListItemExpandMax(int paramInt)
  {
    mListItemExpandMaximum = paramInt;
  }
  
  public void setListSelector(Drawable paramDrawable)
  {
    mDropDownListHighlight = paramDrawable;
  }
  
  public void setModal(boolean paramBoolean)
  {
    mModal = paramBoolean;
    mPopup.setFocusable(paramBoolean);
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mPopup.setOnDismissListener(paramOnDismissListener);
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setOverlapAnchor(boolean paramBoolean)
  {
    mOverlapAnchorSet = true;
    mOverlapAnchor = paramBoolean;
  }
  
  public void setPromptPosition(int paramInt)
  {
    mPromptPosition = paramInt;
  }
  
  public void setPromptView(View paramView)
  {
    boolean bool = isShowing();
    if (bool) {
      removePromptView();
    }
    mPromptView = paramView;
    if (bool) {
      show();
    }
  }
  
  public void setSelection(int paramInt)
  {
    DropDownListView localDropDownListView = mDropDownList;
    if ((isShowing()) && (localDropDownListView != null))
    {
      localDropDownListView.setListSelectionHidden(false);
      localDropDownListView.setSelection(paramInt);
      if (localDropDownListView.getChoiceMode() != 0) {
        localDropDownListView.setItemChecked(paramInt, true);
      }
    }
  }
  
  public void setSoftInputMode(int paramInt)
  {
    mPopup.setSoftInputMode(paramInt);
  }
  
  public void setVerticalOffset(int paramInt)
  {
    mDropDownVerticalOffset = paramInt;
    mDropDownVerticalOffsetSet = true;
  }
  
  public void setWidth(int paramInt)
  {
    mDropDownWidth = paramInt;
  }
  
  public void setWindowLayoutType(int paramInt)
  {
    mDropDownWindowLayoutType = paramInt;
  }
  
  public void show()
  {
    int i = buildDropDown();
    boolean bool1 = isInputMethodNotNeeded();
    mPopup.setAllowScrollingAnchorParent(bool1 ^ true);
    mPopup.setWindowLayoutType(mDropDownWindowLayoutType);
    boolean bool2 = mPopup.isShowing();
    boolean bool3 = true;
    boolean bool4 = true;
    int j;
    Object localObject;
    if (bool2)
    {
      if (!getAnchorView().isAttachedToWindow()) {
        return;
      }
      if (mDropDownWidth == -1) {
        j = -1;
      }
      for (;;)
      {
        break;
        if (mDropDownWidth == -2) {
          j = getAnchorView().getWidth();
        } else {
          j = mDropDownWidth;
        }
      }
      if (mDropDownHeight == -1)
      {
        if (!bool1) {
          i = -1;
        }
        if (bool1)
        {
          localObject = mPopup;
          if (mDropDownWidth == -1) {
            k = -1;
          } else {
            k = 0;
          }
          ((PopupWindow)localObject).setWidth(k);
          mPopup.setHeight(0);
        }
        else
        {
          localObject = mPopup;
          if (mDropDownWidth == -1) {
            k = -1;
          } else {
            k = 0;
          }
          ((PopupWindow)localObject).setWidth(k);
          mPopup.setHeight(-1);
        }
      }
      else if (mDropDownHeight != -2)
      {
        i = mDropDownHeight;
      }
      localObject = mPopup;
      if ((mForceIgnoreOutsideTouch) || (mDropDownAlwaysVisible)) {
        bool4 = false;
      }
      ((PopupWindow)localObject).setOutsideTouchable(bool4);
      PopupWindow localPopupWindow = mPopup;
      localObject = getAnchorView();
      int m = mDropDownHorizontalOffset;
      int k = mDropDownVerticalOffset;
      if (j < 0) {
        j = -1;
      }
      if (i < 0) {
        i = -1;
      }
      localPopupWindow.update((View)localObject, m, k, j, i);
      mPopup.getContentView().restoreDefaultFocus();
    }
    else
    {
      if (mDropDownWidth == -1) {
        j = -1;
      }
      for (;;)
      {
        break;
        if (mDropDownWidth == -2) {
          j = getAnchorView().getWidth();
        } else {
          j = mDropDownWidth;
        }
      }
      if (mDropDownHeight == -1) {
        i = -1;
      }
      for (;;)
      {
        break;
        if (mDropDownHeight != -2) {
          i = mDropDownHeight;
        }
      }
      mPopup.setWidth(j);
      mPopup.setHeight(i);
      mPopup.setClipToScreenEnabled(true);
      localObject = mPopup;
      if ((!mForceIgnoreOutsideTouch) && (!mDropDownAlwaysVisible)) {
        bool4 = bool3;
      } else {
        bool4 = false;
      }
      ((PopupWindow)localObject).setOutsideTouchable(bool4);
      mPopup.setTouchInterceptor(mTouchInterceptor);
      mPopup.setEpicenterBounds(mEpicenterBounds);
      if (mOverlapAnchorSet) {
        mPopup.setOverlapAnchor(mOverlapAnchor);
      }
      mPopup.showAsDropDown(getAnchorView(), mDropDownHorizontalOffset, mDropDownVerticalOffset, mDropDownGravity);
      mDropDownList.setSelection(-1);
      mPopup.getContentView().restoreDefaultFocus();
      if ((!mModal) || (mDropDownList.isInTouchMode())) {
        clearListSelection();
      }
      if (!mModal) {
        mHandler.post(mHideSelector);
      }
    }
  }
  
  private class ListSelectorHider
    implements Runnable
  {
    private ListSelectorHider() {}
    
    public void run()
    {
      clearListSelection();
    }
  }
  
  private class PopupDataSetObserver
    extends DataSetObserver
  {
    private PopupDataSetObserver() {}
    
    public void onChanged()
    {
      if (isShowing()) {
        show();
      }
    }
    
    public void onInvalidated()
    {
      dismiss();
    }
  }
  
  private class PopupScrollListener
    implements AbsListView.OnScrollListener
  {
    private PopupScrollListener() {}
    
    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
    {
      if ((paramInt == 1) && (!isInputMethodNotNeeded()) && (mPopup.getContentView() != null))
      {
        mHandler.removeCallbacks(mResizePopupRunnable);
        mResizePopupRunnable.run();
      }
    }
  }
  
  private class PopupTouchInterceptor
    implements View.OnTouchListener
  {
    private PopupTouchInterceptor() {}
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      if ((i == 0) && (mPopup != null) && (mPopup.isShowing()) && (j >= 0) && (j < mPopup.getWidth()) && (k >= 0) && (k < mPopup.getHeight())) {
        mHandler.postDelayed(mResizePopupRunnable, 250L);
      } else if (i == 1) {
        mHandler.removeCallbacks(mResizePopupRunnable);
      }
      return false;
    }
  }
  
  private class ResizePopupRunnable
    implements Runnable
  {
    private ResizePopupRunnable() {}
    
    public void run()
    {
      if ((mDropDownList != null) && (mDropDownList.isAttachedToWindow()) && (mDropDownList.getCount() > mDropDownList.getChildCount()) && (mDropDownList.getChildCount() <= mListItemExpandMaximum))
      {
        mPopup.setInputMethodMode(2);
        show();
      }
    }
  }
}
