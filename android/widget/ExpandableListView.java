package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.BaseSavedState;
import com.android.internal.R.styleable;
import java.util.ArrayList;

public class ExpandableListView
  extends ListView
{
  public static final int CHILD_INDICATOR_INHERIT = -1;
  private static final int[] CHILD_LAST_STATE_SET = { 16842918 };
  private static final int[] EMPTY_STATE_SET = new int[0];
  private static final int[] GROUP_EMPTY_STATE_SET;
  private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET;
  private static final int[] GROUP_EXPANDED_STATE_SET = { 16842920 };
  private static final int[][] GROUP_STATE_SETS;
  private static final int INDICATOR_UNDEFINED = -2;
  private static final long PACKED_POSITION_INT_MASK_CHILD = -1L;
  private static final long PACKED_POSITION_INT_MASK_GROUP = 2147483647L;
  private static final long PACKED_POSITION_MASK_CHILD = 4294967295L;
  private static final long PACKED_POSITION_MASK_GROUP = 9223372032559808512L;
  private static final long PACKED_POSITION_MASK_TYPE = Long.MIN_VALUE;
  private static final long PACKED_POSITION_SHIFT_GROUP = 32L;
  private static final long PACKED_POSITION_SHIFT_TYPE = 63L;
  public static final int PACKED_POSITION_TYPE_CHILD = 1;
  public static final int PACKED_POSITION_TYPE_GROUP = 0;
  public static final int PACKED_POSITION_TYPE_NULL = 2;
  public static final long PACKED_POSITION_VALUE_NULL = 4294967295L;
  private ExpandableListAdapter mAdapter;
  private Drawable mChildDivider;
  private Drawable mChildIndicator;
  private int mChildIndicatorEnd;
  private int mChildIndicatorLeft;
  private int mChildIndicatorRight;
  private int mChildIndicatorStart;
  private ExpandableListConnector mConnector;
  private Drawable mGroupIndicator;
  private int mIndicatorEnd;
  private int mIndicatorLeft;
  private final Rect mIndicatorRect = new Rect();
  private int mIndicatorRight;
  private int mIndicatorStart;
  private OnChildClickListener mOnChildClickListener;
  private OnGroupClickListener mOnGroupClickListener;
  private OnGroupCollapseListener mOnGroupCollapseListener;
  private OnGroupExpandListener mOnGroupExpandListener;
  
  static
  {
    GROUP_EMPTY_STATE_SET = new int[] { 16842921 };
    GROUP_EXPANDED_EMPTY_STATE_SET = new int[] { 16842920, 16842921 };
    GROUP_STATE_SETS = new int[][] { EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET, GROUP_EMPTY_STATE_SET, GROUP_EXPANDED_EMPTY_STATE_SET };
  }
  
  public ExpandableListView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ExpandableListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842863);
  }
  
  public ExpandableListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ExpandableListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ExpandableListView, paramInt1, paramInt2);
    mGroupIndicator = paramContext.getDrawable(0);
    mChildIndicator = paramContext.getDrawable(1);
    mIndicatorLeft = paramContext.getDimensionPixelSize(2, 0);
    mIndicatorRight = paramContext.getDimensionPixelSize(3, 0);
    if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
      mIndicatorRight = (mIndicatorLeft + mGroupIndicator.getIntrinsicWidth());
    }
    mChildIndicatorLeft = paramContext.getDimensionPixelSize(4, -1);
    mChildIndicatorRight = paramContext.getDimensionPixelSize(5, -1);
    mChildDivider = paramContext.getDrawable(6);
    if (!isRtlCompatibilityMode())
    {
      mIndicatorStart = paramContext.getDimensionPixelSize(7, -2);
      mIndicatorEnd = paramContext.getDimensionPixelSize(8, -2);
      mChildIndicatorStart = paramContext.getDimensionPixelSize(9, -1);
      mChildIndicatorEnd = paramContext.getDimensionPixelSize(10, -1);
    }
    paramContext.recycle();
  }
  
  private int getAbsoluteFlatPosition(int paramInt)
  {
    return getHeaderViewsCount() + paramInt;
  }
  
  private long getChildOrGroupId(ExpandableListPosition paramExpandableListPosition)
  {
    if (type == 1) {
      return mAdapter.getChildId(groupPos, childPos);
    }
    return mAdapter.getGroupId(groupPos);
  }
  
  private int getFlatPositionForConnector(int paramInt)
  {
    return paramInt - getHeaderViewsCount();
  }
  
  private Drawable getIndicator(ExpandableListConnector.PositionMetadata paramPositionMetadata)
  {
    int i = position.type;
    int j = 2;
    Drawable localDrawable1;
    Drawable localDrawable2;
    if (i == 2)
    {
      localDrawable1 = mGroupIndicator;
      localDrawable2 = localDrawable1;
      if (localDrawable1 != null)
      {
        localDrawable2 = localDrawable1;
        if (localDrawable1.isStateful())
        {
          if ((groupMetadata != null) && (groupMetadata.lastChildFlPos != groupMetadata.flPos)) {
            i = 0;
          } else {
            i = 1;
          }
          int k = paramPositionMetadata.isExpanded();
          if (i != 0) {
            i = j;
          } else {
            i = 0;
          }
          localDrawable1.setState(GROUP_STATE_SETS[(i | k)]);
          localDrawable2 = localDrawable1;
        }
      }
    }
    else
    {
      localDrawable1 = mChildIndicator;
      localDrawable2 = localDrawable1;
      if (localDrawable1 != null)
      {
        localDrawable2 = localDrawable1;
        if (localDrawable1.isStateful())
        {
          if (position.flatListPos == groupMetadata.lastChildFlPos) {
            paramPositionMetadata = CHILD_LAST_STATE_SET;
          } else {
            paramPositionMetadata = EMPTY_STATE_SET;
          }
          localDrawable1.setState(paramPositionMetadata);
          localDrawable2 = localDrawable1;
        }
      }
    }
    return localDrawable2;
  }
  
  public static int getPackedPositionChild(long paramLong)
  {
    if (paramLong == 4294967295L) {
      return -1;
    }
    if ((paramLong & 0x8000000000000000) != Long.MIN_VALUE) {
      return -1;
    }
    return (int)(0xFFFFFFFF & paramLong);
  }
  
  public static long getPackedPositionForChild(int paramInt1, int paramInt2)
  {
    return (paramInt1 & 0x7FFFFFFF) << 32 | 0x8000000000000000 | paramInt2 & 0xFFFFFFFFFFFFFFFF;
  }
  
  public static long getPackedPositionForGroup(int paramInt)
  {
    return (paramInt & 0x7FFFFFFF) << 32;
  }
  
  public static int getPackedPositionGroup(long paramLong)
  {
    if (paramLong == 4294967295L) {
      return -1;
    }
    return (int)((0x7FFFFFFF00000000 & paramLong) >> 32);
  }
  
  public static int getPackedPositionType(long paramLong)
  {
    if (paramLong == 4294967295L) {
      return 2;
    }
    int i;
    if ((paramLong & 0x8000000000000000) == Long.MIN_VALUE) {
      i = 1;
    } else {
      i = 0;
    }
    return i;
  }
  
  private boolean hasRtlSupport()
  {
    return mContext.getApplicationInfo().hasRtlSupport();
  }
  
  private boolean isHeaderOrFooterPosition(int paramInt)
  {
    int i = mItemCount;
    int j = getFooterViewsCount();
    boolean bool;
    if ((paramInt >= getHeaderViewsCount()) && (paramInt < i - j)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isRtlCompatibilityMode()
  {
    boolean bool;
    if ((mContext.getApplicationInfo().targetSdkVersion >= 17) && (hasRtlSupport())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void resolveChildIndicator()
  {
    if (isLayoutRtl())
    {
      if (mChildIndicatorStart >= -1) {
        mChildIndicatorRight = mChildIndicatorStart;
      }
      if (mChildIndicatorEnd >= -1) {
        mChildIndicatorLeft = mChildIndicatorEnd;
      }
    }
    else
    {
      if (mChildIndicatorStart >= -1) {
        mChildIndicatorLeft = mChildIndicatorStart;
      }
      if (mChildIndicatorEnd >= -1) {
        mChildIndicatorRight = mChildIndicatorEnd;
      }
    }
  }
  
  private void resolveIndicator()
  {
    if (isLayoutRtl())
    {
      if (mIndicatorStart >= 0) {
        mIndicatorRight = mIndicatorStart;
      }
      if (mIndicatorEnd >= 0) {
        mIndicatorLeft = mIndicatorEnd;
      }
    }
    else
    {
      if (mIndicatorStart >= 0) {
        mIndicatorLeft = mIndicatorStart;
      }
      if (mIndicatorEnd >= 0) {
        mIndicatorRight = mIndicatorEnd;
      }
    }
    if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
      mIndicatorRight = (mIndicatorLeft + mGroupIndicator.getIntrinsicWidth());
    }
  }
  
  public boolean collapseGroup(int paramInt)
  {
    boolean bool = mConnector.collapseGroup(paramInt);
    if (mOnGroupCollapseListener != null) {
      mOnGroupCollapseListener.onGroupCollapse(paramInt);
    }
    return bool;
  }
  
  ContextMenu.ContextMenuInfo createContextMenuInfo(View paramView, int paramInt, long paramLong)
  {
    if (isHeaderOrFooterPosition(paramInt)) {
      return new AdapterView.AdapterContextMenuInfo(paramView, paramInt, paramLong);
    }
    paramInt = getFlatPositionForConnector(paramInt);
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getUnflattenedPos(paramInt);
    ExpandableListPosition localExpandableListPosition = position;
    long l = getChildOrGroupId(localExpandableListPosition);
    paramLong = localExpandableListPosition.getPackedPosition();
    localPositionMetadata.recycle();
    return new ExpandableListContextMenuInfo(paramView, paramLong, l);
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if ((mChildIndicator == null) && (mGroupIndicator == null)) {
      return;
    }
    int i = 0;
    int j;
    if ((mGroupFlags & 0x22) == 34) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0)
    {
      i = paramCanvas.save();
      k = mScrollX;
      m = mScrollY;
      paramCanvas.clipRect(mPaddingLeft + k, mPaddingTop + m, mRight + k - mLeft - mPaddingRight, mBottom + m - mTop - mPaddingBottom);
    }
    int n = getHeaderViewsCount();
    int i1 = mItemCount - getFooterViewsCount() - n - 1;
    int i2 = mBottom;
    int k = -4;
    Rect localRect = mIndicatorRect;
    int i3 = getChildCount();
    int i4 = 0;
    for (int m = mFirstPosition - n; i4 < i3; m++)
    {
      if (m >= 0)
      {
        int i5;
        int i6;
        do
        {
          if (m > i1) {
            break label567;
          }
          localObject = getChildAt(i4);
          i5 = ((View)localObject).getTop();
          i6 = ((View)localObject).getBottom();
          if (i6 < 0) {
            break;
          }
        } while (i5 > i2);
        Object localObject = mConnector.getUnflattenedPos(m);
        boolean bool = isLayoutRtl();
        int i7 = getWidth();
        if (position.type != k)
        {
          if (position.type == 1)
          {
            if (mChildIndicatorLeft == -1) {
              k = mIndicatorLeft;
            } else {
              k = mChildIndicatorLeft;
            }
            left = k;
            if (mChildIndicatorRight == -1) {
              k = mIndicatorRight;
            } else {
              k = mChildIndicatorRight;
            }
            right = k;
          }
          else
          {
            left = mIndicatorLeft;
            right = mIndicatorRight;
          }
          if (bool)
          {
            k = left;
            left = (i7 - right);
            right = (i7 - k);
            left -= mPaddingRight;
            right -= mPaddingRight;
          }
          else
          {
            left += mPaddingLeft;
            right += mPaddingLeft;
          }
          k = position.type;
        }
        if (left != right)
        {
          if (mStackFromBottom)
          {
            top = i5;
            bottom = i6;
          }
          else
          {
            top = i5;
            bottom = i6;
          }
          Drawable localDrawable = getIndicator((ExpandableListConnector.PositionMetadata)localObject);
          if (localDrawable != null)
          {
            localDrawable.setBounds(localRect);
            localDrawable.draw(paramCanvas);
          }
        }
        ((ExpandableListConnector.PositionMetadata)localObject).recycle();
      }
      i4++;
    }
    label567:
    if (j != 0) {
      paramCanvas.restoreToCount(i);
    }
  }
  
  void drawDivider(Canvas paramCanvas, Rect paramRect, int paramInt)
  {
    paramInt = mFirstPosition + paramInt;
    if (paramInt >= 0)
    {
      int i = getFlatPositionForConnector(paramInt);
      ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getUnflattenedPos(i);
      if ((position.type != 1) && ((!localPositionMetadata.isExpanded()) || (groupMetadata.lastChildFlPos == groupMetadata.flPos)))
      {
        localPositionMetadata.recycle();
      }
      else
      {
        Drawable localDrawable = mChildDivider;
        localDrawable.setBounds(paramRect);
        localDrawable.draw(paramCanvas);
        localPositionMetadata.recycle();
        return;
      }
    }
    super.drawDivider(paramCanvas, paramRect, paramInt);
  }
  
  public boolean expandGroup(int paramInt)
  {
    return expandGroup(paramInt, false);
  }
  
  public boolean expandGroup(int paramInt, boolean paramBoolean)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtain(2, paramInt, -1, -1);
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getFlattenedPos(localExpandableListPosition);
    localExpandableListPosition.recycle();
    boolean bool = mConnector.expandGroup(localPositionMetadata);
    if (mOnGroupExpandListener != null) {
      mOnGroupExpandListener.onGroupExpand(paramInt);
    }
    if (paramBoolean)
    {
      int i = position.flatListPos;
      i = getHeaderViewsCount() + i;
      smoothScrollToPosition(mAdapter.getChildrenCount(paramInt) + i, i);
    }
    localPositionMetadata.recycle();
    return bool;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ExpandableListView.class.getName();
  }
  
  public ListAdapter getAdapter()
  {
    return super.getAdapter();
  }
  
  public ExpandableListAdapter getExpandableListAdapter()
  {
    return mAdapter;
  }
  
  public long getExpandableListPosition(int paramInt)
  {
    if (isHeaderOrFooterPosition(paramInt)) {
      return 4294967295L;
    }
    paramInt = getFlatPositionForConnector(paramInt);
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getUnflattenedPos(paramInt);
    long l = position.getPackedPosition();
    localPositionMetadata.recycle();
    return l;
  }
  
  public int getFlatListPosition(long paramLong)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtainPosition(paramLong);
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getFlattenedPos(localExpandableListPosition);
    localExpandableListPosition.recycle();
    int i = position.flatListPos;
    localPositionMetadata.recycle();
    return getAbsoluteFlatPosition(i);
  }
  
  public long getSelectedId()
  {
    long l = getSelectedPosition();
    if (l == 4294967295L) {
      return -1L;
    }
    int i = getPackedPositionGroup(l);
    if (getPackedPositionType(l) == 0) {
      return mAdapter.getGroupId(i);
    }
    return mAdapter.getChildId(i, getPackedPositionChild(l));
  }
  
  public long getSelectedPosition()
  {
    return getExpandableListPosition(getSelectedItemPosition());
  }
  
  boolean handleItemClick(View paramView, int paramInt, long paramLong)
  {
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getUnflattenedPos(paramInt);
    paramLong = getChildOrGroupId(position);
    paramInt = position.type;
    boolean bool = false;
    if (paramInt == 2)
    {
      if ((mOnGroupClickListener != null) && (mOnGroupClickListener.onGroupClick(this, paramView, position.groupPos, paramLong)))
      {
        localPositionMetadata.recycle();
        return true;
      }
      if (localPositionMetadata.isExpanded())
      {
        mConnector.collapseGroup(localPositionMetadata);
        playSoundEffect(0);
        if (mOnGroupCollapseListener != null) {
          mOnGroupCollapseListener.onGroupCollapse(position.groupPos);
        }
      }
      else
      {
        mConnector.expandGroup(localPositionMetadata);
        playSoundEffect(0);
        if (mOnGroupExpandListener != null) {
          mOnGroupExpandListener.onGroupExpand(position.groupPos);
        }
        paramInt = position.groupPos;
        int i = position.flatListPos;
        i = getHeaderViewsCount() + i;
        smoothScrollToPosition(mAdapter.getChildrenCount(paramInt) + i, i);
      }
      bool = true;
    }
    else if (mOnChildClickListener != null)
    {
      playSoundEffect(0);
      return mOnChildClickListener.onChildClick(this, paramView, position.groupPos, position.childPos, paramLong);
    }
    localPositionMetadata.recycle();
    return bool;
  }
  
  public boolean isGroupExpanded(int paramInt)
  {
    return mConnector.isGroupExpanded(paramInt);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if ((mConnector != null) && (expandedGroupMetadataList != null)) {
      mConnector.setExpandedGroupMetadataList(expandedGroupMetadataList);
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    resolveIndicator();
    resolveChildIndicator();
  }
  
  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    ArrayList localArrayList;
    if (mConnector != null) {
      localArrayList = mConnector.getExpandedGroupMetadataList();
    } else {
      localArrayList = null;
    }
    return new SavedState(localParcelable, localArrayList);
  }
  
  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    if (isHeaderOrFooterPosition(paramInt)) {
      return super.performItemClick(paramView, paramInt, paramLong);
    }
    return handleItemClick(paramView, getFlatPositionForConnector(paramInt), paramLong);
  }
  
  public void setAdapter(ExpandableListAdapter paramExpandableListAdapter)
  {
    mAdapter = paramExpandableListAdapter;
    if (paramExpandableListAdapter != null) {
      mConnector = new ExpandableListConnector(paramExpandableListAdapter);
    } else {
      mConnector = null;
    }
    super.setAdapter(mConnector);
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    throw new RuntimeException("For ExpandableListView, use setAdapter(ExpandableListAdapter) instead of setAdapter(ListAdapter)");
  }
  
  public void setChildDivider(Drawable paramDrawable)
  {
    mChildDivider = paramDrawable;
  }
  
  public void setChildIndicator(Drawable paramDrawable)
  {
    mChildIndicator = paramDrawable;
  }
  
  public void setChildIndicatorBounds(int paramInt1, int paramInt2)
  {
    mChildIndicatorLeft = paramInt1;
    mChildIndicatorRight = paramInt2;
    resolveChildIndicator();
  }
  
  public void setChildIndicatorBoundsRelative(int paramInt1, int paramInt2)
  {
    mChildIndicatorStart = paramInt1;
    mChildIndicatorEnd = paramInt2;
    resolveChildIndicator();
  }
  
  public void setGroupIndicator(Drawable paramDrawable)
  {
    mGroupIndicator = paramDrawable;
    if ((mIndicatorRight == 0) && (mGroupIndicator != null)) {
      mIndicatorRight = (mIndicatorLeft + mGroupIndicator.getIntrinsicWidth());
    }
  }
  
  public void setIndicatorBounds(int paramInt1, int paramInt2)
  {
    mIndicatorLeft = paramInt1;
    mIndicatorRight = paramInt2;
    resolveIndicator();
  }
  
  public void setIndicatorBoundsRelative(int paramInt1, int paramInt2)
  {
    mIndicatorStart = paramInt1;
    mIndicatorEnd = paramInt2;
    resolveIndicator();
  }
  
  public void setOnChildClickListener(OnChildClickListener paramOnChildClickListener)
  {
    mOnChildClickListener = paramOnChildClickListener;
  }
  
  public void setOnGroupClickListener(OnGroupClickListener paramOnGroupClickListener)
  {
    mOnGroupClickListener = paramOnGroupClickListener;
  }
  
  public void setOnGroupCollapseListener(OnGroupCollapseListener paramOnGroupCollapseListener)
  {
    mOnGroupCollapseListener = paramOnGroupCollapseListener;
  }
  
  public void setOnGroupExpandListener(OnGroupExpandListener paramOnGroupExpandListener)
  {
    mOnGroupExpandListener = paramOnGroupExpandListener;
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    super.setOnItemClickListener(paramOnItemClickListener);
  }
  
  public boolean setSelectedChild(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtainChildPosition(paramInt1, paramInt2);
    ExpandableListConnector.PositionMetadata localPositionMetadata1 = mConnector.getFlattenedPos(localExpandableListPosition);
    ExpandableListConnector.PositionMetadata localPositionMetadata2 = localPositionMetadata1;
    if (localPositionMetadata1 == null)
    {
      if (!paramBoolean) {
        return false;
      }
      expandGroup(paramInt1);
      localPositionMetadata2 = mConnector.getFlattenedPos(localExpandableListPosition);
      if (localPositionMetadata2 == null) {
        throw new IllegalStateException("Could not find child");
      }
    }
    super.setSelection(getAbsoluteFlatPosition(position.flatListPos));
    localExpandableListPosition.recycle();
    localPositionMetadata2.recycle();
    return true;
  }
  
  public void setSelectedGroup(int paramInt)
  {
    ExpandableListPosition localExpandableListPosition = ExpandableListPosition.obtainGroupPosition(paramInt);
    ExpandableListConnector.PositionMetadata localPositionMetadata = mConnector.getFlattenedPos(localExpandableListPosition);
    localExpandableListPosition.recycle();
    super.setSelection(getAbsoluteFlatPosition(position.flatListPos));
    localPositionMetadata.recycle();
  }
  
  public static class ExpandableListContextMenuInfo
    implements ContextMenu.ContextMenuInfo
  {
    public long id;
    public long packedPosition;
    public View targetView;
    
    public ExpandableListContextMenuInfo(View paramView, long paramLong1, long paramLong2)
    {
      targetView = paramView;
      packedPosition = paramLong1;
      id = paramLong2;
    }
  }
  
  public static abstract interface OnChildClickListener
  {
    public abstract boolean onChildClick(ExpandableListView paramExpandableListView, View paramView, int paramInt1, int paramInt2, long paramLong);
  }
  
  public static abstract interface OnGroupClickListener
  {
    public abstract boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int paramInt, long paramLong);
  }
  
  public static abstract interface OnGroupCollapseListener
  {
    public abstract void onGroupCollapse(int paramInt);
  }
  
  public static abstract interface OnGroupExpandListener
  {
    public abstract void onGroupExpand(int paramInt);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ExpandableListView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ExpandableListView.SavedState(paramAnonymousParcel, null);
      }
      
      public ExpandableListView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ExpandableListView.SavedState[paramAnonymousInt];
      }
    };
    ArrayList<ExpandableListConnector.GroupMetadata> expandedGroupMetadataList;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      expandedGroupMetadataList = new ArrayList();
      paramParcel.readList(expandedGroupMetadataList, ExpandableListConnector.class.getClassLoader());
    }
    
    SavedState(Parcelable paramParcelable, ArrayList<ExpandableListConnector.GroupMetadata> paramArrayList)
    {
      super();
      expandedGroupMetadataList = paramArrayList;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeList(expandedGroupMetadataList);
    }
  }
}
