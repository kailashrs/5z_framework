package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.internal.R.styleable;
import java.util.ArrayList;

public final class IconMenuView
  extends ViewGroup
  implements MenuBuilder.ItemInvoker, MenuView, Runnable
{
  private static final int ITEM_CAPTION_CYCLE_DELAY = 1000;
  private int mAnimations;
  private boolean mHasStaleChildren;
  private Drawable mHorizontalDivider;
  private int mHorizontalDividerHeight;
  private ArrayList<Rect> mHorizontalDividerRects;
  private Drawable mItemBackground;
  private boolean mLastChildrenCaptionMode;
  private int[] mLayout;
  private int mLayoutNumRows;
  private int mMaxItems;
  private int mMaxItemsPerRow;
  private int mMaxRows;
  private MenuBuilder mMenu;
  private boolean mMenuBeingLongpressed = false;
  private Drawable mMoreIcon;
  private int mNumActualItemsShown;
  private int mRowHeight;
  private Drawable mVerticalDivider;
  private ArrayList<Rect> mVerticalDividerRects;
  private int mVerticalDividerWidth;
  
  public IconMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.IconMenuView, 0, 0);
    mRowHeight = localTypedArray.getDimensionPixelSize(0, 64);
    mMaxRows = localTypedArray.getInt(1, 2);
    mMaxItems = localTypedArray.getInt(4, 6);
    mMaxItemsPerRow = localTypedArray.getInt(2, 3);
    mMoreIcon = localTypedArray.getDrawable(3);
    localTypedArray.recycle();
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuView, 0, 0);
    mItemBackground = paramContext.getDrawable(5);
    mHorizontalDivider = paramContext.getDrawable(2);
    mHorizontalDividerRects = new ArrayList();
    mVerticalDivider = paramContext.getDrawable(3);
    mVerticalDividerRects = new ArrayList();
    mAnimations = paramContext.getResourceId(0, 0);
    paramContext.recycle();
    if (mHorizontalDivider != null)
    {
      mHorizontalDividerHeight = mHorizontalDivider.getIntrinsicHeight();
      if (mHorizontalDividerHeight == -1) {
        mHorizontalDividerHeight = 1;
      }
    }
    if (mVerticalDivider != null)
    {
      mVerticalDividerWidth = mVerticalDivider.getIntrinsicWidth();
      if (mVerticalDividerWidth == -1) {
        mVerticalDividerWidth = 1;
      }
    }
    mLayout = new int[mMaxRows];
    setWillNotDraw(false);
    setFocusableInTouchMode(true);
    setDescendantFocusability(262144);
  }
  
  private void calculateItemFittingMetadata(int paramInt)
  {
    int i = mMaxItemsPerRow;
    int j = getChildCount();
    for (int k = 0; k < j; k++)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(k).getLayoutParams();
      maxNumItemsOnRow = 1;
      for (int m = i; m > 0; m--) {
        if (desiredWidth < paramInt / m)
        {
          maxNumItemsOnRow = m;
          break;
        }
      }
    }
  }
  
  private boolean doItemsFit()
  {
    int[] arrayOfInt = mLayout;
    int i = mLayoutNumRows;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = arrayOfInt[k];
      if (m == 1)
      {
        j++;
      }
      else
      {
        int n = m;
        while (n > 0)
        {
          if (getChildAtgetLayoutParamsmaxNumItemsOnRow < m) {
            return false;
          }
          n--;
          j++;
        }
      }
    }
    return true;
  }
  
  private void layoutItems(int paramInt)
  {
    int i = getChildCount();
    if (i == 0)
    {
      mLayoutNumRows = 0;
      return;
    }
    for (paramInt = Math.min((int)Math.ceil(i / mMaxItemsPerRow), mMaxRows); paramInt <= mMaxRows; paramInt++)
    {
      layoutItemsUsingGravity(paramInt, i);
      if ((paramInt >= i) || (doItemsFit())) {
        break;
      }
    }
  }
  
  private void layoutItemsUsingGravity(int paramInt1, int paramInt2)
  {
    int i = paramInt2 / paramInt1;
    int[] arrayOfInt = mLayout;
    for (int j = 0; j < paramInt1; j++)
    {
      arrayOfInt[j] = i;
      if (j >= paramInt1 - paramInt2 % paramInt1) {
        arrayOfInt[j] += 1;
      }
    }
    mLayoutNumRows = paramInt1;
  }
  
  private void positionChildren(int paramInt1, int paramInt2)
  {
    if (mHorizontalDivider != null) {
      mHorizontalDividerRects.clear();
    }
    if (mVerticalDivider != null) {
      mVerticalDividerRects.clear();
    }
    int i = mLayoutNumRows;
    int[] arrayOfInt = mLayout;
    Object localObject = null;
    float f1 = (paramInt2 - mHorizontalDividerHeight * (i - 1)) / i;
    float f2 = 0.0F;
    int j = 0;
    int k = 0;
    paramInt2 = i;
    while (k < paramInt2)
    {
      float f3 = (paramInt1 - mVerticalDividerWidth * (arrayOfInt[k] - 1)) / arrayOfInt[k];
      float f4 = 0.0F;
      for (int m = 0; m < arrayOfInt[k]; m++)
      {
        localObject = getChildAt(j);
        ((View)localObject).measure(View.MeasureSpec.makeMeasureSpec((int)f3, 1073741824), View.MeasureSpec.makeMeasureSpec((int)f1, 1073741824));
        localObject = (LayoutParams)((View)localObject).getLayoutParams();
        left = ((int)f4);
        right = ((int)(f4 + f3));
        top = ((int)f2);
        bottom = ((int)(f2 + f1));
        f4 += f3;
        j++;
        if (mVerticalDivider != null) {
          mVerticalDividerRects.add(new Rect((int)f4, (int)f2, (int)(mVerticalDividerWidth + f4), (int)(f2 + f1)));
        }
        f4 += mVerticalDividerWidth;
      }
      if (localObject != null) {
        right = paramInt1;
      }
      f2 += f1;
      if ((mHorizontalDivider != null) && (k < i - 1))
      {
        mHorizontalDividerRects.add(new Rect(0, (int)f2, paramInt1, (int)(mHorizontalDividerHeight + f2)));
        f2 += mHorizontalDividerHeight;
      }
      k++;
    }
  }
  
  private void setChildrenCaptionMode(boolean paramBoolean)
  {
    mLastChildrenCaptionMode = paramBoolean;
    for (int i = getChildCount() - 1; i >= 0; i--) {
      ((IconMenuItemView)getChildAt(i)).setCaptionMode(paramBoolean);
    }
  }
  
  private void setCycleShortcutCaptionMode(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      removeCallbacks(this);
      setChildrenCaptionMode(false);
      mMenuBeingLongpressed = false;
    }
    else
    {
      setChildrenCaptionMode(true);
    }
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  IconMenuItemView createMoreItemView()
  {
    Context localContext = getContext();
    IconMenuItemView localIconMenuItemView = (IconMenuItemView)LayoutInflater.from(localContext).inflate(17367183, null);
    localIconMenuItemView.initialize(localContext.getResources().getText(17040455), mMoreIcon);
    localIconMenuItemView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        mMenu.changeMenuMode();
      }
    });
    return localIconMenuItemView;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 82) {
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        removeCallbacks(this);
        postDelayed(this, ViewConfiguration.getLongPressTimeout());
      }
      else if (paramKeyEvent.getAction() == 1)
      {
        if (mMenuBeingLongpressed)
        {
          setCycleShortcutCaptionMode(false);
          return true;
        }
        removeCallbacks(this);
      }
    }
    return super.dispatchKeyEvent(paramKeyEvent);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  Drawable getItemBackgroundDrawable()
  {
    return mItemBackground.getConstantState().newDrawable(getContext().getResources());
  }
  
  public int[] getLayout()
  {
    return mLayout;
  }
  
  public int getLayoutNumRows()
  {
    return mLayoutNumRows;
  }
  
  int getMaxItems()
  {
    return mMaxItems;
  }
  
  int getNumActualItemsShown()
  {
    return mNumActualItemsShown;
  }
  
  public int getWindowAnimations()
  {
    return mAnimations;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder)
  {
    mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl)
  {
    return mMenu.performItemAction(paramMenuItemImpl, 0);
  }
  
  void markStaleChildren()
  {
    if (!mHasStaleChildren)
    {
      mHasStaleChildren = true;
      requestLayout();
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    requestFocus();
  }
  
  protected void onDetachedFromWindow()
  {
    setCycleShortcutCaptionMode(false);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    Object localObject1 = mHorizontalDivider;
    int i;
    if (localObject1 != null)
    {
      localObject2 = mHorizontalDividerRects;
      for (i = ((ArrayList)localObject2).size() - 1; i >= 0; i--)
      {
        ((Drawable)localObject1).setBounds((Rect)((ArrayList)localObject2).get(i));
        ((Drawable)localObject1).draw(paramCanvas);
      }
    }
    Object localObject2 = mVerticalDivider;
    if (localObject2 != null)
    {
      localObject1 = mVerticalDividerRects;
      for (i = ((ArrayList)localObject1).size() - 1; i >= 0; i--)
      {
        ((Drawable)localObject2).setBounds((Rect)((ArrayList)localObject1).get(i));
        ((Drawable)localObject2).draw(paramCanvas);
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    for (paramInt1 = getChildCount() - 1; paramInt1 >= 0; paramInt1--)
    {
      View localView = getChildAt(paramInt1);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      localView.layout(left, top, right, bottom);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = resolveSize(Integer.MAX_VALUE, paramInt1);
    calculateItemFittingMetadata(i);
    layoutItems(i);
    int j = mLayoutNumRows;
    int k = mRowHeight;
    paramInt1 = mHorizontalDividerHeight;
    int m = mHorizontalDividerHeight;
    setMeasuredDimension(i, resolveSize((k + paramInt1) * j - m, paramInt2));
    if (j > 0) {
      positionChildren(getMeasuredWidth(), getMeasuredHeight());
    }
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if (focusedPosition >= getChildCount()) {
      return;
    }
    paramParcelable = getChildAt(focusedPosition);
    if (paramParcelable != null) {
      paramParcelable.requestFocus();
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    View localView = getFocusedChild();
    for (int i = getChildCount() - 1; i >= 0; i--) {
      if (getChildAt(i) == localView) {
        return new SavedState(localParcelable, i);
      }
    }
    return new SavedState(localParcelable, -1);
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    if (!paramBoolean) {
      setCycleShortcutCaptionMode(false);
    }
    super.onWindowFocusChanged(paramBoolean);
  }
  
  public void run()
  {
    if (mMenuBeingLongpressed)
    {
      setChildrenCaptionMode(mLastChildrenCaptionMode ^ true);
    }
    else
    {
      mMenuBeingLongpressed = true;
      setCycleShortcutCaptionMode(true);
    }
    postDelayed(this, 1000L);
  }
  
  void setNumActualItemsShown(int paramInt)
  {
    mNumActualItemsShown = paramInt;
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    int bottom;
    int desiredWidth;
    int left;
    int maxNumItemsOnRow;
    int right;
    int top;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
  }
  
  private static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public IconMenuView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new IconMenuView.SavedState(paramAnonymousParcel, null);
      }
      
      public IconMenuView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new IconMenuView.SavedState[paramAnonymousInt];
      }
    };
    int focusedPosition;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      focusedPosition = paramParcel.readInt();
    }
    
    public SavedState(Parcelable paramParcelable, int paramInt)
    {
      super();
      focusedPosition = paramInt;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(focusedPosition);
    }
  }
}
