package android.view.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AccessibilityClickableSpan;
import android.text.style.AccessibilityURLSpan;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.ArraySet;
import android.util.LongArray;
import android.util.Pools.SynchronizedPool;
import android.view.View;
import com.android.internal.util.BitUtils;
import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessibilityNodeInfo
  implements Parcelable
{
  public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
  public static final String ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN = "android.view.accessibility.action.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN";
  public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
  public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
  public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
  public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
  public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
  public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
  public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
  public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
  public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
  public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
  public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
  public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
  public static final int ACTION_CLEAR_FOCUS = 2;
  public static final int ACTION_CLEAR_SELECTION = 8;
  public static final int ACTION_CLICK = 16;
  public static final int ACTION_COLLAPSE = 524288;
  public static final int ACTION_COPY = 16384;
  public static final int ACTION_CUT = 65536;
  public static final int ACTION_DISMISS = 1048576;
  public static final int ACTION_EXPAND = 262144;
  public static final int ACTION_FOCUS = 1;
  public static final int ACTION_LONG_CLICK = 32;
  public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
  public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
  public static final int ACTION_PASTE = 32768;
  public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
  public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
  public static final int ACTION_SCROLL_BACKWARD = 8192;
  public static final int ACTION_SCROLL_FORWARD = 4096;
  public static final int ACTION_SELECT = 4;
  public static final int ACTION_SET_SELECTION = 131072;
  public static final int ACTION_SET_TEXT = 2097152;
  private static final int ACTION_TYPE_MASK = -16777216;
  private static final int BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED = 1024;
  private static final int BOOLEAN_PROPERTY_CHECKABLE = 1;
  private static final int BOOLEAN_PROPERTY_CHECKED = 2;
  private static final int BOOLEAN_PROPERTY_CLICKABLE = 32;
  private static final int BOOLEAN_PROPERTY_CONTENT_INVALID = 65536;
  private static final int BOOLEAN_PROPERTY_CONTEXT_CLICKABLE = 131072;
  private static final int BOOLEAN_PROPERTY_DISMISSABLE = 16384;
  private static final int BOOLEAN_PROPERTY_EDITABLE = 4096;
  private static final int BOOLEAN_PROPERTY_ENABLED = 128;
  private static final int BOOLEAN_PROPERTY_FOCUSABLE = 4;
  private static final int BOOLEAN_PROPERTY_FOCUSED = 8;
  private static final int BOOLEAN_PROPERTY_IMPORTANCE = 262144;
  private static final int BOOLEAN_PROPERTY_IS_HEADING = 2097152;
  private static final int BOOLEAN_PROPERTY_IS_SHOWING_HINT = 1048576;
  private static final int BOOLEAN_PROPERTY_LONG_CLICKABLE = 64;
  private static final int BOOLEAN_PROPERTY_MULTI_LINE = 32768;
  private static final int BOOLEAN_PROPERTY_OPENS_POPUP = 8192;
  private static final int BOOLEAN_PROPERTY_PASSWORD = 256;
  private static final int BOOLEAN_PROPERTY_SCREEN_READER_FOCUSABLE = 524288;
  private static final int BOOLEAN_PROPERTY_SCROLLABLE = 512;
  private static final int BOOLEAN_PROPERTY_SELECTED = 16;
  private static final int BOOLEAN_PROPERTY_VISIBLE_TO_USER = 2048;
  public static final Parcelable.Creator<AccessibilityNodeInfo> CREATOR = new Parcelable.Creator()
  {
    public AccessibilityNodeInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
      localAccessibilityNodeInfo.initFromParcel(paramAnonymousParcel);
      return localAccessibilityNodeInfo;
    }
    
    public AccessibilityNodeInfo[] newArray(int paramAnonymousInt)
    {
      return new AccessibilityNodeInfo[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  private static final AccessibilityNodeInfo DEFAULT;
  public static final String EXTRA_DATA_REQUESTED_KEY = "android.view.accessibility.AccessibilityNodeInfo.extra_data_requested";
  public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH";
  public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX";
  public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_KEY";
  public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 8;
  public static final int FLAG_PREFETCH_DESCENDANTS = 4;
  public static final int FLAG_PREFETCH_PREDECESSORS = 1;
  public static final int FLAG_PREFETCH_SIBLINGS = 2;
  public static final int FLAG_REPORT_VIEW_IDS = 16;
  public static final int FOCUS_ACCESSIBILITY = 2;
  public static final int FOCUS_INPUT = 1;
  public static final int LAST_LEGACY_STANDARD_ACTION = 2097152;
  private static final int MAX_POOL_SIZE = 50;
  public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
  public static final int MOVEMENT_GRANULARITY_LINE = 4;
  public static final int MOVEMENT_GRANULARITY_PAGE = 16;
  public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
  public static final int MOVEMENT_GRANULARITY_WORD = 2;
  public static final int ROOT_ITEM_ID = 2147483646;
  public static final long ROOT_NODE_ID;
  public static final int UNDEFINED_CONNECTION_ID = -1;
  public static final int UNDEFINED_ITEM_ID = Integer.MAX_VALUE;
  public static final long UNDEFINED_NODE_ID = makeNodeId(Integer.MAX_VALUE, Integer.MAX_VALUE);
  public static final int UNDEFINED_SELECTION_INDEX = -1;
  private static final long VIRTUAL_DESCENDANT_ID_MASK = -4294967296L;
  private static final int VIRTUAL_DESCENDANT_ID_SHIFT = 32;
  private static AtomicInteger sNumInstancesInUse;
  private static final Pools.SynchronizedPool<AccessibilityNodeInfo> sPool;
  private ArrayList<AccessibilityAction> mActions;
  private int mBooleanProperties;
  private final Rect mBoundsInParent = new Rect();
  private final Rect mBoundsInScreen = new Rect();
  private LongArray mChildNodeIds;
  private CharSequence mClassName;
  private CollectionInfo mCollectionInfo;
  private CollectionItemInfo mCollectionItemInfo;
  private int mConnectionId = -1;
  private CharSequence mContentDescription;
  private int mDrawingOrderInParent;
  private CharSequence mError;
  private ArrayList<String> mExtraDataKeys;
  private Bundle mExtras;
  private CharSequence mHintText;
  private int mInputType = 0;
  private long mLabelForId = UNDEFINED_NODE_ID;
  private long mLabeledById = UNDEFINED_NODE_ID;
  private int mLiveRegion = 0;
  private int mMaxTextLength = -1;
  private int mMovementGranularities;
  private CharSequence mOriginalText;
  private CharSequence mPackageName;
  private CharSequence mPaneTitle;
  private long mParentNodeId = UNDEFINED_NODE_ID;
  private RangeInfo mRangeInfo;
  private boolean mSealed;
  private long mSourceNodeId = UNDEFINED_NODE_ID;
  private CharSequence mText;
  private int mTextSelectionEnd = -1;
  private int mTextSelectionStart = -1;
  private CharSequence mTooltipText;
  private long mTraversalAfter = UNDEFINED_NODE_ID;
  private long mTraversalBefore = UNDEFINED_NODE_ID;
  private String mViewIdResourceName;
  private int mWindowId = -1;
  
  static
  {
    ROOT_NODE_ID = makeNodeId(2147483646, -1);
    sPool = new Pools.SynchronizedPool(50);
    DEFAULT = new AccessibilityNodeInfo();
  }
  
  private AccessibilityNodeInfo() {}
  
  private void addActionUnchecked(AccessibilityAction paramAccessibilityAction)
  {
    if (paramAccessibilityAction == null) {
      return;
    }
    if (mActions == null) {
      mActions = new ArrayList();
    }
    mActions.remove(paramAccessibilityAction);
    mActions.add(paramAccessibilityAction);
  }
  
  private void addChildInternal(View paramView, int paramInt, boolean paramBoolean)
  {
    enforceNotSealed();
    if (mChildNodeIds == null) {
      mChildNodeIds = new LongArray();
    }
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    long l = makeNodeId(i, paramInt);
    if ((paramBoolean) && (mChildNodeIds.indexOf(l) >= 0)) {
      return;
    }
    mChildNodeIds.add(l);
  }
  
  private void addStandardActions(long paramLong)
  {
    while (paramLong > 0L)
    {
      long l = 1L << Long.numberOfTrailingZeros(paramLong);
      paramLong &= l;
      addAction(getActionSingletonBySerializationFlag(l));
    }
  }
  
  private boolean canPerformRequestOverConnection(long paramLong)
  {
    boolean bool;
    if ((mWindowId != -1) && (getAccessibilityViewId(paramLong) != Integer.MAX_VALUE) && (mConnectionId != -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void clear()
  {
    init(DEFAULT);
  }
  
  private void enforceValidFocusDirection(int paramInt)
  {
    if ((paramInt != 17) && (paramInt != 33) && (paramInt != 66) && (paramInt != 130)) {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown direction: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
  }
  
  private void enforceValidFocusType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown focus type: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  public static int getAccessibilityViewId(long paramLong)
  {
    return (int)paramLong;
  }
  
  private static AccessibilityAction getActionSingleton(int paramInt)
  {
    int i = AccessibilityAction.sStandardActions.size();
    for (int j = 0; j < i; j++)
    {
      AccessibilityAction localAccessibilityAction = (AccessibilityAction)AccessibilityAction.sStandardActions.valueAt(j);
      if (paramInt == localAccessibilityAction.getId()) {
        return localAccessibilityAction;
      }
    }
    return null;
  }
  
  private static AccessibilityAction getActionSingletonBySerializationFlag(long paramLong)
  {
    int i = AccessibilityAction.sStandardActions.size();
    for (int j = 0; j < i; j++)
    {
      AccessibilityAction localAccessibilityAction = (AccessibilityAction)AccessibilityAction.sStandardActions.valueAt(j);
      if (paramLong == mSerializationFlag) {
        return localAccessibilityAction;
      }
    }
    return null;
  }
  
  private static String getActionSymbolicName(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            return "ACTION_UNKNOWN";
          case 2097152: 
            return "ACTION_SET_TEXT";
          case 1048576: 
            return "ACTION_DISMISS";
          case 524288: 
            return "ACTION_COLLAPSE";
          case 262144: 
            return "ACTION_EXPAND";
          case 131072: 
            return "ACTION_SET_SELECTION";
          case 65536: 
            return "ACTION_CUT";
          case 32768: 
            return "ACTION_PASTE";
          case 16384: 
            return "ACTION_COPY";
          case 8192: 
            return "ACTION_SCROLL_BACKWARD";
          case 4096: 
            return "ACTION_SCROLL_FORWARD";
          case 2048: 
            return "ACTION_PREVIOUS_HTML_ELEMENT";
          case 1024: 
            return "ACTION_NEXT_HTML_ELEMENT";
          case 512: 
            return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
          case 256: 
            return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
          case 128: 
            return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
          case 64: 
            return "ACTION_ACCESSIBILITY_FOCUS";
          case 32: 
            return "ACTION_LONG_CLICK";
          case 16: 
            return "ACTION_CLICK";
          case 8: 
            return "ACTION_CLEAR_SELECTION";
          }
          return "ACTION_SELECT";
        case 16908357: 
          return "ACTION_HIDE_TOOLTIP";
        }
        return "ACTION_SHOW_TOOLTIP";
      case 16908349: 
        return "ACTION_SET_PROGRESS";
      case 16908348: 
        return "ACTION_CONTEXT_CLICK";
      case 16908347: 
        return "ACTION_SCROLL_RIGHT";
      case 16908346: 
        return "ACTION_SCROLL_DOWN";
      case 16908345: 
        return "ACTION_SCROLL_LEFT";
      case 16908344: 
        return "ACTION_SCROLL_UP";
      case 16908343: 
        return "ACTION_SCROLL_TO_POSITION";
      }
      return "ACTION_SHOW_ON_SCREEN";
    case 2: 
      return "ACTION_CLEAR_FOCUS";
    }
    return "ACTION_FOCUS";
  }
  
  private boolean getBooleanProperty(int paramInt)
  {
    boolean bool;
    if ((mBooleanProperties & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static String getMovementGranularitySymbolicName(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unknown movement granularity: ");
            localStringBuilder.append(paramInt);
            throw new IllegalArgumentException(localStringBuilder.toString());
          case 2: 
            return "MOVEMENT_GRANULARITY_WORD";
          }
          return "MOVEMENT_GRANULARITY_CHARACTER";
        }
        return "MOVEMENT_GRANULARITY_PAGE";
      }
      return "MOVEMENT_GRANULARITY_PARAGRAPH";
    }
    return "MOVEMENT_GRANULARITY_LINE";
  }
  
  private AccessibilityNodeInfo getNodeForAccessibilityId(long paramLong)
  {
    if (!canPerformRequestOverConnection(paramLong)) {
      return null;
    }
    return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mWindowId, paramLong, false, 7, null);
  }
  
  public static int getVirtualDescendantId(long paramLong)
  {
    return (int)((0xFFFFFFFF00000000 & paramLong) >> 32);
  }
  
  private static String idItemToString(int paramInt)
  {
    if (paramInt != -1)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 2147483647: 
        return "UNDEFINED";
      }
      return "ROOT";
    }
    return "HOST";
  }
  
  public static String idToString(long paramLong)
  {
    int i = getAccessibilityViewId(paramLong);
    int j = getVirtualDescendantId(paramLong);
    Object localObject;
    if (j == -1)
    {
      localObject = idItemToString(i);
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(idItemToString(i));
      ((StringBuilder)localObject).append(":");
      ((StringBuilder)localObject).append(idItemToString(j));
      localObject = ((StringBuilder)localObject).toString();
    }
    return localObject;
  }
  
  private void init(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    mSealed = mSealed;
    mSourceNodeId = mSourceNodeId;
    mParentNodeId = mParentNodeId;
    mLabelForId = mLabelForId;
    mLabeledById = mLabeledById;
    mTraversalBefore = mTraversalBefore;
    mTraversalAfter = mTraversalAfter;
    mWindowId = mWindowId;
    mConnectionId = mConnectionId;
    mBoundsInParent.set(mBoundsInParent);
    mBoundsInScreen.set(mBoundsInScreen);
    mPackageName = mPackageName;
    mClassName = mClassName;
    mText = mText;
    mOriginalText = mOriginalText;
    mHintText = mHintText;
    mError = mError;
    mContentDescription = mContentDescription;
    mPaneTitle = mPaneTitle;
    mTooltipText = mTooltipText;
    mViewIdResourceName = mViewIdResourceName;
    if (mActions != null) {
      mActions.clear();
    }
    Object localObject1 = mActions;
    if ((localObject1 != null) && (((ArrayList)localObject1).size() > 0)) {
      if (mActions == null) {
        mActions = new ArrayList((Collection)localObject1);
      } else {
        mActions.addAll(mActions);
      }
    }
    mBooleanProperties = mBooleanProperties;
    mMaxTextLength = mMaxTextLength;
    mMovementGranularities = mMovementGranularities;
    if (mChildNodeIds != null) {
      mChildNodeIds.clear();
    }
    localObject1 = mChildNodeIds;
    if ((localObject1 != null) && (((LongArray)localObject1).size() > 0)) {
      if (mChildNodeIds == null) {
        mChildNodeIds = ((LongArray)localObject1).clone();
      } else {
        mChildNodeIds.addAll((LongArray)localObject1);
      }
    }
    mTextSelectionStart = mTextSelectionStart;
    mTextSelectionEnd = mTextSelectionEnd;
    mInputType = mInputType;
    mLiveRegion = mLiveRegion;
    mDrawingOrderInParent = mDrawingOrderInParent;
    mExtraDataKeys = mExtraDataKeys;
    localObject1 = mExtras;
    Object localObject2 = null;
    if (localObject1 != null) {
      localObject1 = new Bundle(mExtras);
    } else {
      localObject1 = null;
    }
    mExtras = ((Bundle)localObject1);
    if (mRangeInfo != null) {
      mRangeInfo.recycle();
    }
    if (mRangeInfo != null) {
      localObject1 = RangeInfo.obtain(mRangeInfo);
    } else {
      localObject1 = null;
    }
    mRangeInfo = ((RangeInfo)localObject1);
    if (mCollectionInfo != null) {
      mCollectionInfo.recycle();
    }
    if (mCollectionInfo != null) {
      localObject1 = CollectionInfo.obtain(mCollectionInfo);
    } else {
      localObject1 = null;
    }
    mCollectionInfo = ((CollectionInfo)localObject1);
    if (mCollectionItemInfo != null) {
      mCollectionItemInfo.recycle();
    }
    localObject1 = localObject2;
    if (mCollectionItemInfo != null) {
      localObject1 = CollectionItemInfo.obtain(mCollectionItemInfo);
    }
    mCollectionItemInfo = ((CollectionItemInfo)localObject1);
  }
  
  private void initFromParcel(Parcel paramParcel)
  {
    long l1 = paramParcel.readLong();
    int i = 0 + 1;
    boolean bool1;
    if (BitUtils.isBitSet(l1, 0))
    {
      if (paramParcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
    }
    else {
      bool1 = DEFAULTmSealed;
    }
    int j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mSourceNodeId = paramParcel.readLong();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mWindowId = paramParcel.readInt();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mParentNodeId = paramParcel.readLong();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mLabelForId = paramParcel.readLong();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mLabeledById = paramParcel.readLong();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mTraversalBefore = paramParcel.readLong();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mTraversalAfter = paramParcel.readLong();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mConnectionId = paramParcel.readInt();
    }
    j = i + 1;
    boolean bool2 = BitUtils.isBitSet(l1, i);
    Object localObject1 = null;
    if (bool2)
    {
      k = paramParcel.readInt();
      if (k <= 0)
      {
        mChildNodeIds = null;
      }
      else
      {
        mChildNodeIds = new LongArray(k);
        for (i = 0; i < k; i++)
        {
          long l2 = paramParcel.readLong();
          mChildNodeIds.add(l2);
        }
      }
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j))
    {
      mBoundsInParent.top = paramParcel.readInt();
      mBoundsInParent.bottom = paramParcel.readInt();
      mBoundsInParent.left = paramParcel.readInt();
      mBoundsInParent.right = paramParcel.readInt();
    }
    int k = i + 1;
    if (BitUtils.isBitSet(l1, i))
    {
      mBoundsInScreen.top = paramParcel.readInt();
      mBoundsInScreen.bottom = paramParcel.readInt();
      mBoundsInScreen.left = paramParcel.readInt();
      mBoundsInScreen.right = paramParcel.readInt();
    }
    j = k + 1;
    if (BitUtils.isBitSet(l1, k))
    {
      addStandardActions(paramParcel.readLong());
      k = paramParcel.readInt();
      for (i = 0; i < k; i++) {
        addActionUnchecked(new AccessibilityAction(paramParcel.readInt(), paramParcel.readCharSequence()));
      }
    }
    k = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mMaxTextLength = paramParcel.readInt();
    }
    i = k + 1;
    if (BitUtils.isBitSet(l1, k)) {
      mMovementGranularities = paramParcel.readInt();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mBooleanProperties = paramParcel.readInt();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mPackageName = paramParcel.readCharSequence();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mClassName = paramParcel.readCharSequence();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mText = paramParcel.readCharSequence();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mHintText = paramParcel.readCharSequence();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mError = paramParcel.readCharSequence();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mContentDescription = paramParcel.readCharSequence();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mPaneTitle = paramParcel.readCharSequence();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mTooltipText = paramParcel.readCharSequence();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mViewIdResourceName = paramParcel.readString();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mTextSelectionStart = paramParcel.readInt();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mTextSelectionEnd = paramParcel.readInt();
    }
    k = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      mInputType = paramParcel.readInt();
    }
    j = k + 1;
    if (BitUtils.isBitSet(l1, k)) {
      mLiveRegion = paramParcel.readInt();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      mDrawingOrderInParent = paramParcel.readInt();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      localObject2 = paramParcel.createStringArrayList();
    } else {
      localObject2 = null;
    }
    mExtraDataKeys = ((ArrayList)localObject2);
    i = j + 1;
    if (BitUtils.isBitSet(l1, j)) {
      localObject2 = paramParcel.readBundle();
    } else {
      localObject2 = null;
    }
    mExtras = ((Bundle)localObject2);
    if (mRangeInfo != null) {
      mRangeInfo.recycle();
    }
    j = i + 1;
    if (BitUtils.isBitSet(l1, i)) {
      localObject2 = RangeInfo.obtain(paramParcel.readInt(), paramParcel.readFloat(), paramParcel.readFloat(), paramParcel.readFloat());
    } else {
      localObject2 = null;
    }
    mRangeInfo = ((RangeInfo)localObject2);
    if (mCollectionInfo != null) {
      mCollectionInfo.recycle();
    }
    i = j + 1;
    if (BitUtils.isBitSet(l1, j))
    {
      j = paramParcel.readInt();
      k = paramParcel.readInt();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      localObject2 = CollectionInfo.obtain(j, k, bool2, paramParcel.readInt());
    }
    else
    {
      localObject2 = null;
    }
    mCollectionInfo = ((CollectionInfo)localObject2);
    if (mCollectionItemInfo != null) {
      mCollectionItemInfo.recycle();
    }
    Object localObject2 = localObject1;
    if (BitUtils.isBitSet(l1, i))
    {
      k = paramParcel.readInt();
      j = paramParcel.readInt();
      i = paramParcel.readInt();
      int m = paramParcel.readInt();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramParcel.readInt() == 1) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      localObject2 = CollectionItemInfo.obtain(k, j, i, m, bool2, bool3);
    }
    mCollectionItemInfo = ((CollectionItemInfo)localObject2);
    mSealed = bool1;
  }
  
  private static boolean isDefaultStandardAction(AccessibilityAction paramAccessibilityAction)
  {
    boolean bool;
    if ((mSerializationFlag != -1L) && (TextUtils.isEmpty(paramAccessibilityAction.getLabel()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static long makeNodeId(int paramInt1, int paramInt2)
  {
    return paramInt2 << 32 | paramInt1;
  }
  
  public static AccessibilityNodeInfo obtain()
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)sPool.acquire();
    if (sNumInstancesInUse != null) {
      sNumInstancesInUse.incrementAndGet();
    }
    if (localAccessibilityNodeInfo == null) {
      localAccessibilityNodeInfo = new AccessibilityNodeInfo();
    }
    return localAccessibilityNodeInfo;
  }
  
  public static AccessibilityNodeInfo obtain(View paramView)
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = obtain();
    localAccessibilityNodeInfo.setSource(paramView);
    return localAccessibilityNodeInfo;
  }
  
  public static AccessibilityNodeInfo obtain(View paramView, int paramInt)
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = obtain();
    localAccessibilityNodeInfo.setSource(paramView, paramInt);
    return localAccessibilityNodeInfo;
  }
  
  public static AccessibilityNodeInfo obtain(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = obtain();
    localAccessibilityNodeInfo.init(paramAccessibilityNodeInfo);
    return localAccessibilityNodeInfo;
  }
  
  private void setBooleanProperty(int paramInt, boolean paramBoolean)
  {
    enforceNotSealed();
    if (paramBoolean) {
      mBooleanProperties |= paramInt;
    } else {
      mBooleanProperties &= paramInt;
    }
  }
  
  public static void setNumInstancesInUseCounter(AtomicInteger paramAtomicInteger)
  {
    sNumInstancesInUse = paramAtomicInteger;
  }
  
  @Deprecated
  public void addAction(int paramInt)
  {
    enforceNotSealed();
    if ((0xFF000000 & paramInt) == 0)
    {
      addStandardActions(paramInt);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Action is not a combination of the standard actions: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void addAction(AccessibilityAction paramAccessibilityAction)
  {
    enforceNotSealed();
    addActionUnchecked(paramAccessibilityAction);
  }
  
  public void addChild(View paramView)
  {
    addChildInternal(paramView, -1, true);
  }
  
  public void addChild(View paramView, int paramInt)
  {
    addChildInternal(paramView, paramInt, true);
  }
  
  public void addChildUnchecked(View paramView)
  {
    addChildInternal(paramView, -1, false);
  }
  
  public boolean canOpenPopup()
  {
    return getBooleanProperty(8192);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  protected void enforceNotSealed()
  {
    if (!isSealed()) {
      return;
    }
    throw new IllegalStateException("Cannot perform this action on a sealed instance.");
  }
  
  protected void enforceSealed()
  {
    if (isSealed()) {
      return;
    }
    throw new IllegalStateException("Cannot perform this action on a not sealed instance.");
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AccessibilityNodeInfo)paramObject;
    if (mSourceNodeId != mSourceNodeId) {
      return false;
    }
    return mWindowId == mWindowId;
  }
  
  public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String paramString)
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return Collections.emptyList();
    }
    return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfosByText(mConnectionId, mWindowId, mSourceNodeId, paramString);
  }
  
  public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(String paramString)
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return Collections.emptyList();
    }
    return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfosByViewId(mConnectionId, mWindowId, mSourceNodeId, paramString);
  }
  
  public AccessibilityNodeInfo findFocus(int paramInt)
  {
    enforceSealed();
    enforceValidFocusType(paramInt);
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return null;
    }
    return AccessibilityInteractionClient.getInstance().findFocus(mConnectionId, mWindowId, mSourceNodeId, paramInt);
  }
  
  public AccessibilityNodeInfo focusSearch(int paramInt)
  {
    enforceSealed();
    enforceValidFocusDirection(paramInt);
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return null;
    }
    return AccessibilityInteractionClient.getInstance().focusSearch(mConnectionId, mWindowId, mSourceNodeId, paramInt);
  }
  
  public List<AccessibilityAction> getActionList()
  {
    return CollectionUtils.emptyIfNull(mActions);
  }
  
  @Deprecated
  public int getActions()
  {
    int i = 0;
    if (mActions == null) {
      return 0;
    }
    int j = mActions.size();
    int k = 0;
    while (k < j)
    {
      int m = ((AccessibilityAction)mActions.get(k)).getId();
      int n = i;
      if (m <= 2097152) {
        n = i | m;
      }
      k++;
      i = n;
    }
    return i;
  }
  
  public List<String> getAvailableExtraData()
  {
    if (mExtraDataKeys != null) {
      return Collections.unmodifiableList(mExtraDataKeys);
    }
    return Collections.EMPTY_LIST;
  }
  
  public void getBoundsInParent(Rect paramRect)
  {
    paramRect.set(mBoundsInParent.left, mBoundsInParent.top, mBoundsInParent.right, mBoundsInParent.bottom);
  }
  
  public Rect getBoundsInScreen()
  {
    return mBoundsInScreen;
  }
  
  public void getBoundsInScreen(Rect paramRect)
  {
    paramRect.set(mBoundsInScreen.left, mBoundsInScreen.top, mBoundsInScreen.right, mBoundsInScreen.bottom);
  }
  
  public AccessibilityNodeInfo getChild(int paramInt)
  {
    enforceSealed();
    if (mChildNodeIds == null) {
      return null;
    }
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return null;
    }
    long l = mChildNodeIds.get(paramInt);
    return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mWindowId, l, false, 4, null);
  }
  
  public int getChildCount()
  {
    int i;
    if (mChildNodeIds == null) {
      i = 0;
    } else {
      i = mChildNodeIds.size();
    }
    return i;
  }
  
  public long getChildId(int paramInt)
  {
    if (mChildNodeIds != null) {
      return mChildNodeIds.get(paramInt);
    }
    throw new IndexOutOfBoundsException();
  }
  
  public LongArray getChildNodeIds()
  {
    return mChildNodeIds;
  }
  
  public CharSequence getClassName()
  {
    return mClassName;
  }
  
  public CollectionInfo getCollectionInfo()
  {
    return mCollectionInfo;
  }
  
  public CollectionItemInfo getCollectionItemInfo()
  {
    return mCollectionItemInfo;
  }
  
  public int getConnectionId()
  {
    return mConnectionId;
  }
  
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  public int getDrawingOrder()
  {
    return mDrawingOrderInParent;
  }
  
  public CharSequence getError()
  {
    return mError;
  }
  
  public Bundle getExtras()
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    return mExtras;
  }
  
  public CharSequence getHintText()
  {
    return mHintText;
  }
  
  public int getInputType()
  {
    return mInputType;
  }
  
  public AccessibilityNodeInfo getLabelFor()
  {
    enforceSealed();
    return getNodeForAccessibilityId(mLabelForId);
  }
  
  public AccessibilityNodeInfo getLabeledBy()
  {
    enforceSealed();
    return getNodeForAccessibilityId(mLabeledById);
  }
  
  public int getLiveRegion()
  {
    return mLiveRegion;
  }
  
  public int getMaxTextLength()
  {
    return mMaxTextLength;
  }
  
  public int getMovementGranularities()
  {
    return mMovementGranularities;
  }
  
  public CharSequence getOriginalText()
  {
    return mOriginalText;
  }
  
  public CharSequence getPackageName()
  {
    return mPackageName;
  }
  
  public CharSequence getPaneTitle()
  {
    return mPaneTitle;
  }
  
  public AccessibilityNodeInfo getParent()
  {
    enforceSealed();
    return getNodeForAccessibilityId(mParentNodeId);
  }
  
  public long getParentNodeId()
  {
    return mParentNodeId;
  }
  
  public RangeInfo getRangeInfo()
  {
    return mRangeInfo;
  }
  
  public long getSourceNodeId()
  {
    return mSourceNodeId;
  }
  
  public CharSequence getText()
  {
    if ((mText instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)mText;
      int i = mText.length();
      int j = 0;
      Object localObject = (AccessibilityClickableSpan[])localSpanned.getSpans(0, i, AccessibilityClickableSpan.class);
      for (i = 0; i < localObject.length; i++) {
        localObject[i].copyConnectionDataFrom(this);
      }
      localObject = (AccessibilityURLSpan[])localSpanned.getSpans(0, mText.length(), AccessibilityURLSpan.class);
      for (i = j; i < localObject.length; i++) {
        localObject[i].copyConnectionDataFrom(this);
      }
    }
    return mText;
  }
  
  public int getTextSelectionEnd()
  {
    return mTextSelectionEnd;
  }
  
  public int getTextSelectionStart()
  {
    return mTextSelectionStart;
  }
  
  public CharSequence getTooltipText()
  {
    return mTooltipText;
  }
  
  public AccessibilityNodeInfo getTraversalAfter()
  {
    enforceSealed();
    return getNodeForAccessibilityId(mTraversalAfter);
  }
  
  public AccessibilityNodeInfo getTraversalBefore()
  {
    enforceSealed();
    return getNodeForAccessibilityId(mTraversalBefore);
  }
  
  public String getViewIdResourceName()
  {
    return mViewIdResourceName;
  }
  
  public AccessibilityWindowInfo getWindow()
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return null;
    }
    return AccessibilityInteractionClient.getInstance().getWindow(mConnectionId, mWindowId);
  }
  
  public int getWindowId()
  {
    return mWindowId;
  }
  
  public boolean hasExtras()
  {
    boolean bool;
    if (mExtras != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * 1 + getAccessibilityViewId(mSourceNodeId)) + getVirtualDescendantId(mSourceNodeId)) + mWindowId;
  }
  
  public boolean isAccessibilityFocused()
  {
    return getBooleanProperty(1024);
  }
  
  public boolean isCheckable()
  {
    return getBooleanProperty(1);
  }
  
  public boolean isChecked()
  {
    return getBooleanProperty(2);
  }
  
  public boolean isClickable()
  {
    return getBooleanProperty(32);
  }
  
  public boolean isContentInvalid()
  {
    return getBooleanProperty(65536);
  }
  
  public boolean isContextClickable()
  {
    return getBooleanProperty(131072);
  }
  
  public boolean isDismissable()
  {
    return getBooleanProperty(16384);
  }
  
  public boolean isEditable()
  {
    return getBooleanProperty(4096);
  }
  
  public boolean isEnabled()
  {
    return getBooleanProperty(128);
  }
  
  public boolean isFocusable()
  {
    return getBooleanProperty(4);
  }
  
  public boolean isFocused()
  {
    return getBooleanProperty(8);
  }
  
  public boolean isHeading()
  {
    boolean bool1 = getBooleanProperty(2097152);
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    CollectionItemInfo localCollectionItemInfo = getCollectionItemInfo();
    if ((localCollectionItemInfo == null) || (!mHeading)) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean isImportantForAccessibility()
  {
    return getBooleanProperty(262144);
  }
  
  public boolean isLongClickable()
  {
    return getBooleanProperty(64);
  }
  
  public boolean isMultiLine()
  {
    return getBooleanProperty(32768);
  }
  
  public boolean isPassword()
  {
    return getBooleanProperty(256);
  }
  
  public boolean isScreenReaderFocusable()
  {
    return getBooleanProperty(524288);
  }
  
  public boolean isScrollable()
  {
    return getBooleanProperty(512);
  }
  
  public boolean isSealed()
  {
    return mSealed;
  }
  
  public boolean isSelected()
  {
    return getBooleanProperty(16);
  }
  
  public boolean isShowingHintText()
  {
    return getBooleanProperty(1048576);
  }
  
  public boolean isVisibleToUser()
  {
    return getBooleanProperty(2048);
  }
  
  public boolean performAction(int paramInt)
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return false;
    }
    return AccessibilityInteractionClient.getInstance().performAccessibilityAction(mConnectionId, mWindowId, mSourceNodeId, paramInt, null);
  }
  
  public boolean performAction(int paramInt, Bundle paramBundle)
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return false;
    }
    return AccessibilityInteractionClient.getInstance().performAccessibilityAction(mConnectionId, mWindowId, mSourceNodeId, paramInt, paramBundle);
  }
  
  public void recycle()
  {
    clear();
    sPool.release(this);
    if (sNumInstancesInUse != null) {
      sNumInstancesInUse.decrementAndGet();
    }
  }
  
  public boolean refresh()
  {
    return refresh(null, true);
  }
  
  public boolean refresh(Bundle paramBundle, boolean paramBoolean)
  {
    enforceSealed();
    if (!canPerformRequestOverConnection(mSourceNodeId)) {
      return false;
    }
    paramBundle = AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mWindowId, mSourceNodeId, paramBoolean, 0, paramBundle);
    if (paramBundle == null) {
      return false;
    }
    enforceSealed();
    init(paramBundle);
    paramBundle.recycle();
    return true;
  }
  
  public boolean refreshWithExtraData(String paramString, Bundle paramBundle)
  {
    paramBundle.putString("android.view.accessibility.AccessibilityNodeInfo.extra_data_requested", paramString);
    return refresh(paramBundle, true);
  }
  
  @Deprecated
  public void removeAction(int paramInt)
  {
    enforceNotSealed();
    removeAction(getActionSingleton(paramInt));
  }
  
  public boolean removeAction(AccessibilityAction paramAccessibilityAction)
  {
    enforceNotSealed();
    if ((mActions != null) && (paramAccessibilityAction != null)) {
      return mActions.remove(paramAccessibilityAction);
    }
    return false;
  }
  
  public void removeAllActions()
  {
    if (mActions != null) {
      mActions.clear();
    }
  }
  
  public boolean removeChild(View paramView)
  {
    return removeChild(paramView, -1);
  }
  
  public boolean removeChild(View paramView, int paramInt)
  {
    enforceNotSealed();
    LongArray localLongArray = mChildNodeIds;
    if (localLongArray == null) {
      return false;
    }
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    paramInt = localLongArray.indexOf(makeNodeId(i, paramInt));
    if (paramInt < 0) {
      return false;
    }
    localLongArray.remove(paramInt);
    return true;
  }
  
  public void setAccessibilityFocused(boolean paramBoolean)
  {
    setBooleanProperty(1024, paramBoolean);
  }
  
  public void setAvailableExtraData(List<String> paramList)
  {
    enforceNotSealed();
    mExtraDataKeys = new ArrayList(paramList);
  }
  
  public void setBoundsInParent(Rect paramRect)
  {
    enforceNotSealed();
    mBoundsInParent.set(left, top, right, bottom);
  }
  
  public void setBoundsInScreen(Rect paramRect)
  {
    enforceNotSealed();
    mBoundsInScreen.set(left, top, right, bottom);
  }
  
  public void setCanOpenPopup(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(8192, paramBoolean);
  }
  
  public void setCheckable(boolean paramBoolean)
  {
    setBooleanProperty(1, paramBoolean);
  }
  
  public void setChecked(boolean paramBoolean)
  {
    setBooleanProperty(2, paramBoolean);
  }
  
  public void setClassName(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    mClassName = paramCharSequence;
  }
  
  public void setClickable(boolean paramBoolean)
  {
    setBooleanProperty(32, paramBoolean);
  }
  
  public void setCollectionInfo(CollectionInfo paramCollectionInfo)
  {
    enforceNotSealed();
    mCollectionInfo = paramCollectionInfo;
  }
  
  public void setCollectionItemInfo(CollectionItemInfo paramCollectionItemInfo)
  {
    enforceNotSealed();
    mCollectionItemInfo = paramCollectionItemInfo;
  }
  
  public void setConnectionId(int paramInt)
  {
    enforceNotSealed();
    mConnectionId = paramInt;
  }
  
  public void setContentDescription(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mContentDescription = paramCharSequence;
  }
  
  public void setContentInvalid(boolean paramBoolean)
  {
    setBooleanProperty(65536, paramBoolean);
  }
  
  public void setContextClickable(boolean paramBoolean)
  {
    setBooleanProperty(131072, paramBoolean);
  }
  
  public void setDismissable(boolean paramBoolean)
  {
    setBooleanProperty(16384, paramBoolean);
  }
  
  public void setDrawingOrder(int paramInt)
  {
    enforceNotSealed();
    mDrawingOrderInParent = paramInt;
  }
  
  public void setEditable(boolean paramBoolean)
  {
    setBooleanProperty(4096, paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    setBooleanProperty(128, paramBoolean);
  }
  
  public void setError(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mError = paramCharSequence;
  }
  
  public void setFocusable(boolean paramBoolean)
  {
    setBooleanProperty(4, paramBoolean);
  }
  
  public void setFocused(boolean paramBoolean)
  {
    setBooleanProperty(8, paramBoolean);
  }
  
  public void setHeading(boolean paramBoolean)
  {
    setBooleanProperty(2097152, paramBoolean);
  }
  
  public void setHintText(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mHintText = paramCharSequence;
  }
  
  public void setImportantForAccessibility(boolean paramBoolean)
  {
    setBooleanProperty(262144, paramBoolean);
  }
  
  public void setInputType(int paramInt)
  {
    enforceNotSealed();
    mInputType = paramInt;
  }
  
  public void setLabelFor(View paramView)
  {
    setLabelFor(paramView, -1);
  }
  
  public void setLabelFor(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    mLabelForId = makeNodeId(i, paramInt);
  }
  
  public void setLabeledBy(View paramView)
  {
    setLabeledBy(paramView, -1);
  }
  
  public void setLabeledBy(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    mLabeledById = makeNodeId(i, paramInt);
  }
  
  public void setLiveRegion(int paramInt)
  {
    enforceNotSealed();
    mLiveRegion = paramInt;
  }
  
  public void setLongClickable(boolean paramBoolean)
  {
    setBooleanProperty(64, paramBoolean);
  }
  
  public void setMaxTextLength(int paramInt)
  {
    enforceNotSealed();
    mMaxTextLength = paramInt;
  }
  
  public void setMovementGranularities(int paramInt)
  {
    enforceNotSealed();
    mMovementGranularities = paramInt;
  }
  
  public void setMultiLine(boolean paramBoolean)
  {
    setBooleanProperty(32768, paramBoolean);
  }
  
  public void setPackageName(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    mPackageName = paramCharSequence;
  }
  
  public void setPaneTitle(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mPaneTitle = paramCharSequence;
  }
  
  public void setParent(View paramView)
  {
    setParent(paramView, -1);
  }
  
  public void setParent(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    mParentNodeId = makeNodeId(i, paramInt);
  }
  
  public void setPassword(boolean paramBoolean)
  {
    setBooleanProperty(256, paramBoolean);
  }
  
  public void setRangeInfo(RangeInfo paramRangeInfo)
  {
    enforceNotSealed();
    mRangeInfo = paramRangeInfo;
  }
  
  public void setScreenReaderFocusable(boolean paramBoolean)
  {
    setBooleanProperty(524288, paramBoolean);
  }
  
  public void setScrollable(boolean paramBoolean)
  {
    setBooleanProperty(512, paramBoolean);
  }
  
  public void setSealed(boolean paramBoolean)
  {
    mSealed = paramBoolean;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    setBooleanProperty(16, paramBoolean);
  }
  
  public void setShowingHintText(boolean paramBoolean)
  {
    setBooleanProperty(1048576, paramBoolean);
  }
  
  public void setSource(View paramView)
  {
    setSource(paramView, -1);
  }
  
  public void setSource(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i = Integer.MAX_VALUE;
    if (paramView != null) {
      j = paramView.getAccessibilityWindowId();
    } else {
      j = Integer.MAX_VALUE;
    }
    mWindowId = j;
    int j = i;
    if (paramView != null) {
      j = paramView.getAccessibilityViewId();
    }
    mSourceNodeId = makeNodeId(j, paramInt);
  }
  
  public void setSourceNodeId(long paramLong, int paramInt)
  {
    enforceNotSealed();
    mSourceNodeId = paramLong;
    mWindowId = paramInt;
  }
  
  public void setText(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    mOriginalText = paramCharSequence;
    boolean bool = paramCharSequence instanceof Spanned;
    int i = 0;
    if (bool)
    {
      ClickableSpan[] arrayOfClickableSpan = (ClickableSpan[])((Spanned)paramCharSequence).getSpans(0, paramCharSequence.length(), ClickableSpan.class);
      if (arrayOfClickableSpan.length > 0)
      {
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
        while (i < arrayOfClickableSpan.length)
        {
          paramCharSequence = arrayOfClickableSpan[i];
          if (((paramCharSequence instanceof AccessibilityClickableSpan)) || ((paramCharSequence instanceof AccessibilityURLSpan))) {
            break;
          }
          int j = localSpannableStringBuilder.getSpanStart(paramCharSequence);
          int k = localSpannableStringBuilder.getSpanEnd(paramCharSequence);
          int m = localSpannableStringBuilder.getSpanFlags(paramCharSequence);
          localSpannableStringBuilder.removeSpan(paramCharSequence);
          if ((paramCharSequence instanceof URLSpan)) {
            paramCharSequence = new AccessibilityURLSpan((URLSpan)paramCharSequence);
          } else {
            paramCharSequence = new AccessibilityClickableSpan(paramCharSequence.getId());
          }
          localSpannableStringBuilder.setSpan(paramCharSequence, j, k, m);
          i++;
        }
        mText = localSpannableStringBuilder;
        return;
      }
    }
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mText = paramCharSequence;
  }
  
  public void setTextSelection(int paramInt1, int paramInt2)
  {
    enforceNotSealed();
    mTextSelectionStart = paramInt1;
    mTextSelectionEnd = paramInt2;
  }
  
  public void setTooltipText(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mTooltipText = paramCharSequence;
  }
  
  public void setTraversalAfter(View paramView)
  {
    setTraversalAfter(paramView, -1);
  }
  
  public void setTraversalAfter(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    mTraversalAfter = makeNodeId(i, paramInt);
  }
  
  public void setTraversalBefore(View paramView)
  {
    setTraversalBefore(paramView, -1);
  }
  
  public void setTraversalBefore(View paramView, int paramInt)
  {
    enforceNotSealed();
    int i;
    if (paramView != null) {
      i = paramView.getAccessibilityViewId();
    } else {
      i = Integer.MAX_VALUE;
    }
    mTraversalBefore = makeNodeId(i, paramInt);
  }
  
  public void setViewIdResourceName(String paramString)
  {
    enforceNotSealed();
    mViewIdResourceName = paramString;
  }
  
  public void setVisibleToUser(boolean paramBoolean)
  {
    setBooleanProperty(2048, paramBoolean);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append("; boundsInParent: ");
    localStringBuilder.append(mBoundsInParent);
    localStringBuilder.append("; boundsInScreen: ");
    localStringBuilder.append(mBoundsInScreen);
    localStringBuilder.append("; packageName: ");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append("; className: ");
    localStringBuilder.append(mClassName);
    localStringBuilder.append("; text: ");
    localStringBuilder.append(mText);
    localStringBuilder.append("; error: ");
    localStringBuilder.append(mError);
    localStringBuilder.append("; maxTextLength: ");
    localStringBuilder.append(mMaxTextLength);
    localStringBuilder.append("; contentDescription: ");
    localStringBuilder.append(mContentDescription);
    localStringBuilder.append("; tooltipText: ");
    localStringBuilder.append(mTooltipText);
    localStringBuilder.append("; viewIdResName: ");
    localStringBuilder.append(mViewIdResourceName);
    localStringBuilder.append("; checkable: ");
    localStringBuilder.append(isCheckable());
    localStringBuilder.append("; checked: ");
    localStringBuilder.append(isChecked());
    localStringBuilder.append("; focusable: ");
    localStringBuilder.append(isFocusable());
    localStringBuilder.append("; focused: ");
    localStringBuilder.append(isFocused());
    localStringBuilder.append("; selected: ");
    localStringBuilder.append(isSelected());
    localStringBuilder.append("; clickable: ");
    localStringBuilder.append(isClickable());
    localStringBuilder.append("; longClickable: ");
    localStringBuilder.append(isLongClickable());
    localStringBuilder.append("; contextClickable: ");
    localStringBuilder.append(isContextClickable());
    localStringBuilder.append("; enabled: ");
    localStringBuilder.append(isEnabled());
    localStringBuilder.append("; password: ");
    localStringBuilder.append(isPassword());
    localStringBuilder.append("; scrollable: ");
    localStringBuilder.append(isScrollable());
    localStringBuilder.append("; importantForAccessibility: ");
    localStringBuilder.append(isImportantForAccessibility());
    localStringBuilder.append("; visible: ");
    localStringBuilder.append(isVisibleToUser());
    localStringBuilder.append("; actions: ");
    localStringBuilder.append(mActions);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelNoRecycle(paramParcel, paramInt);
    recycle();
  }
  
  public void writeToParcelNoRecycle(Parcel paramParcel, int paramInt)
  {
    long l1 = 0L;
    if (isSealed() != DEFAULT.isSealed()) {
      l1 = 0L | BitUtils.bitAt(0);
    }
    paramInt = 0 + 1;
    long l2 = l1;
    if (mSourceNodeId != DEFAULTmSourceNodeId) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mWindowId != DEFAULTmWindowId) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    long l3 = l1;
    if (mParentNodeId != DEFAULTmParentNodeId) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (mLabelForId != DEFAULTmLabelForId) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l2;
    if (mLabeledById != DEFAULTmLabeledById) {
      l3 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l3;
    if (mTraversalBefore != DEFAULTmTraversalBefore) {
      l1 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (mTraversalAfter != DEFAULTmTraversalAfter) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mConnectionId != DEFAULTmConnectionId) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l1;
    if (!LongArray.elementsEqual(mChildNodeIds, DEFAULTmChildNodeIds)) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (!Objects.equals(mBoundsInParent, DEFAULTmBoundsInParent)) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mBoundsInScreen, DEFAULTmBoundsInScreen)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (!Objects.equals(mActions, DEFAULTmActions)) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mMaxTextLength != DEFAULTmMaxTextLength) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (mMovementGranularities != DEFAULTmMovementGranularities) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mBooleanProperties != DEFAULTmBooleanProperties) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (!Objects.equals(mPackageName, DEFAULTmPackageName)) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mClassName, DEFAULTmClassName)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l1;
    if (!Objects.equals(mText, DEFAULTmText)) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (!Objects.equals(mHintText, DEFAULTmHintText)) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mError, DEFAULTmError)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l1;
    if (!Objects.equals(mContentDescription, DEFAULTmContentDescription)) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (!Objects.equals(mPaneTitle, DEFAULTmPaneTitle)) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mTooltipText, DEFAULTmTooltipText)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l1;
    if (!Objects.equals(mViewIdResourceName, DEFAULTmViewIdResourceName)) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (mTextSelectionStart != DEFAULTmTextSelectionStart) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mTextSelectionEnd != DEFAULTmTextSelectionEnd) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (mInputType != DEFAULTmInputType) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (mLiveRegion != DEFAULTmLiveRegion) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l1;
    if (mDrawingOrderInParent != DEFAULTmDrawingOrderInParent) {
      l2 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mExtraDataKeys, DEFAULTmExtraDataKeys)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l3 = l1;
    if (!Objects.equals(mExtras, DEFAULTmExtras)) {
      l3 = l1 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l2 = l3;
    if (!Objects.equals(mRangeInfo, DEFAULTmRangeInfo)) {
      l2 = l3 | BitUtils.bitAt(paramInt);
    }
    paramInt++;
    l1 = l2;
    if (!Objects.equals(mCollectionInfo, DEFAULTmCollectionInfo)) {
      l1 = l2 | BitUtils.bitAt(paramInt);
    }
    l2 = l1;
    if (!Objects.equals(mCollectionItemInfo, DEFAULTmCollectionItemInfo)) {
      l2 = l1 | BitUtils.bitAt(paramInt + 1);
    }
    paramParcel.writeLong(l2);
    int i = 0 + 1;
    if (BitUtils.isBitSet(l2, 0)) {
      paramParcel.writeInt(isSealed());
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeLong(mSourceNodeId);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeInt(mWindowId);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeLong(mParentNodeId);
    }
    int j = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeLong(mLabelForId);
    }
    i = j + 1;
    if (BitUtils.isBitSet(l2, j)) {
      paramParcel.writeLong(mLabeledById);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeLong(mTraversalBefore);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeLong(mTraversalAfter);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeInt(mConnectionId);
    }
    i = paramInt + 1;
    boolean bool = BitUtils.isBitSet(l2, paramInt);
    j = 0;
    Object localObject;
    if (bool)
    {
      localObject = mChildNodeIds;
      if (localObject == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        k = ((LongArray)localObject).size();
        paramParcel.writeInt(k);
        for (paramInt = 0; paramInt < k; paramInt++) {
          paramParcel.writeLong(((LongArray)localObject).get(paramInt));
        }
      }
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i))
    {
      paramParcel.writeInt(mBoundsInParent.top);
      paramParcel.writeInt(mBoundsInParent.bottom);
      paramParcel.writeInt(mBoundsInParent.left);
      paramParcel.writeInt(mBoundsInParent.right);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt))
    {
      paramParcel.writeInt(mBoundsInScreen.top);
      paramParcel.writeInt(mBoundsInScreen.bottom);
      paramParcel.writeInt(mBoundsInScreen.left);
      paramParcel.writeInt(mBoundsInScreen.right);
    }
    int k = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      if ((mActions != null) && (!mActions.isEmpty()))
      {
        int m = mActions.size();
        l1 = 0L;
        i = 0;
        for (paramInt = 0; paramInt < m; paramInt++)
        {
          localObject = (AccessibilityAction)mActions.get(paramInt);
          if (isDefaultStandardAction((AccessibilityAction)localObject)) {
            l1 |= mSerializationFlag;
          } else {
            i++;
          }
        }
        paramParcel.writeLong(l1);
        paramParcel.writeInt(i);
        for (paramInt = j; paramInt < m; paramInt++)
        {
          localObject = (AccessibilityAction)mActions.get(paramInt);
          if (!isDefaultStandardAction((AccessibilityAction)localObject))
          {
            paramParcel.writeInt(((AccessibilityAction)localObject).getId());
            paramParcel.writeCharSequence(((AccessibilityAction)localObject).getLabel());
          }
        }
      }
      else
      {
        paramParcel.writeLong(0L);
        paramParcel.writeInt(0);
      }
    }
    i = k + 1;
    if (BitUtils.isBitSet(l2, k)) {
      paramParcel.writeInt(mMaxTextLength);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeInt(mMovementGranularities);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeInt(mBooleanProperties);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeCharSequence(mPackageName);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeCharSequence(mClassName);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeCharSequence(mText);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeCharSequence(mHintText);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeCharSequence(mError);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeCharSequence(mContentDescription);
    }
    j = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeCharSequence(mPaneTitle);
    }
    paramInt = j + 1;
    if (BitUtils.isBitSet(l2, j)) {
      paramParcel.writeCharSequence(mTooltipText);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeString(mViewIdResourceName);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeInt(mTextSelectionStart);
    }
    j = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeInt(mTextSelectionEnd);
    }
    i = j + 1;
    if (BitUtils.isBitSet(l2, j)) {
      paramParcel.writeInt(mInputType);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeInt(mLiveRegion);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeInt(mDrawingOrderInParent);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i)) {
      paramParcel.writeStringList(mExtraDataKeys);
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt)) {
      paramParcel.writeBundle(mExtras);
    }
    paramInt = i + 1;
    if (BitUtils.isBitSet(l2, i))
    {
      paramParcel.writeInt(mRangeInfo.getType());
      paramParcel.writeFloat(mRangeInfo.getMin());
      paramParcel.writeFloat(mRangeInfo.getMax());
      paramParcel.writeFloat(mRangeInfo.getCurrent());
    }
    i = paramInt + 1;
    if (BitUtils.isBitSet(l2, paramInt))
    {
      paramParcel.writeInt(mCollectionInfo.getRowCount());
      paramParcel.writeInt(mCollectionInfo.getColumnCount());
      paramParcel.writeInt(mCollectionInfo.isHierarchical());
      paramParcel.writeInt(mCollectionInfo.getSelectionMode());
    }
    if (BitUtils.isBitSet(l2, i))
    {
      paramParcel.writeInt(mCollectionItemInfo.getRowIndex());
      paramParcel.writeInt(mCollectionItemInfo.getRowSpan());
      paramParcel.writeInt(mCollectionItemInfo.getColumnIndex());
      paramParcel.writeInt(mCollectionItemInfo.getColumnSpan());
      paramParcel.writeInt(mCollectionItemInfo.isHeading());
      paramParcel.writeInt(mCollectionItemInfo.isSelected());
    }
  }
  
  public static final class AccessibilityAction
  {
    public static final AccessibilityAction ACTION_ACCESSIBILITY_FOCUS;
    public static final AccessibilityAction ACTION_CLEAR_ACCESSIBILITY_FOCUS;
    public static final AccessibilityAction ACTION_CLEAR_FOCUS;
    public static final AccessibilityAction ACTION_CLEAR_SELECTION;
    public static final AccessibilityAction ACTION_CLICK;
    public static final AccessibilityAction ACTION_COLLAPSE;
    public static final AccessibilityAction ACTION_CONTEXT_CLICK;
    public static final AccessibilityAction ACTION_COPY;
    public static final AccessibilityAction ACTION_CUT;
    public static final AccessibilityAction ACTION_DISMISS;
    public static final AccessibilityAction ACTION_EXPAND;
    public static final AccessibilityAction ACTION_FOCUS;
    public static final AccessibilityAction ACTION_HIDE_TOOLTIP = new AccessibilityAction(16908357);
    public static final AccessibilityAction ACTION_LONG_CLICK;
    public static final AccessibilityAction ACTION_MOVE_WINDOW;
    public static final AccessibilityAction ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    public static final AccessibilityAction ACTION_NEXT_HTML_ELEMENT;
    public static final AccessibilityAction ACTION_PASTE;
    public static final AccessibilityAction ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
    public static final AccessibilityAction ACTION_PREVIOUS_HTML_ELEMENT;
    public static final AccessibilityAction ACTION_SCROLL_BACKWARD;
    public static final AccessibilityAction ACTION_SCROLL_DOWN;
    public static final AccessibilityAction ACTION_SCROLL_FORWARD;
    public static final AccessibilityAction ACTION_SCROLL_LEFT;
    public static final AccessibilityAction ACTION_SCROLL_RIGHT;
    public static final AccessibilityAction ACTION_SCROLL_TO_POSITION;
    public static final AccessibilityAction ACTION_SCROLL_UP;
    public static final AccessibilityAction ACTION_SELECT;
    public static final AccessibilityAction ACTION_SET_PROGRESS;
    public static final AccessibilityAction ACTION_SET_SELECTION;
    public static final AccessibilityAction ACTION_SET_TEXT;
    public static final AccessibilityAction ACTION_SHOW_ON_SCREEN;
    public static final AccessibilityAction ACTION_SHOW_TOOLTIP;
    public static final ArraySet<AccessibilityAction> sStandardActions = new ArraySet();
    private final int mActionId;
    private final CharSequence mLabel;
    public long mSerializationFlag = -1L;
    
    static
    {
      ACTION_FOCUS = new AccessibilityAction(1);
      ACTION_CLEAR_FOCUS = new AccessibilityAction(2);
      ACTION_SELECT = new AccessibilityAction(4);
      ACTION_CLEAR_SELECTION = new AccessibilityAction(8);
      ACTION_CLICK = new AccessibilityAction(16);
      ACTION_LONG_CLICK = new AccessibilityAction(32);
      ACTION_ACCESSIBILITY_FOCUS = new AccessibilityAction(64);
      ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityAction(128);
      ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityAction(256);
      ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityAction(512);
      ACTION_NEXT_HTML_ELEMENT = new AccessibilityAction(1024);
      ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityAction(2048);
      ACTION_SCROLL_FORWARD = new AccessibilityAction(4096);
      ACTION_SCROLL_BACKWARD = new AccessibilityAction(8192);
      ACTION_COPY = new AccessibilityAction(16384);
      ACTION_PASTE = new AccessibilityAction(32768);
      ACTION_CUT = new AccessibilityAction(65536);
      ACTION_SET_SELECTION = new AccessibilityAction(131072);
      ACTION_EXPAND = new AccessibilityAction(262144);
      ACTION_COLLAPSE = new AccessibilityAction(524288);
      ACTION_DISMISS = new AccessibilityAction(1048576);
      ACTION_SET_TEXT = new AccessibilityAction(2097152);
      ACTION_SHOW_ON_SCREEN = new AccessibilityAction(16908342);
      ACTION_SCROLL_TO_POSITION = new AccessibilityAction(16908343);
      ACTION_SCROLL_UP = new AccessibilityAction(16908344);
      ACTION_SCROLL_LEFT = new AccessibilityAction(16908345);
      ACTION_SCROLL_DOWN = new AccessibilityAction(16908346);
      ACTION_SCROLL_RIGHT = new AccessibilityAction(16908347);
      ACTION_CONTEXT_CLICK = new AccessibilityAction(16908348);
      ACTION_SET_PROGRESS = new AccessibilityAction(16908349);
      ACTION_MOVE_WINDOW = new AccessibilityAction(16908354);
      ACTION_SHOW_TOOLTIP = new AccessibilityAction(16908356);
    }
    
    private AccessibilityAction(int paramInt)
    {
      this(paramInt, null);
      sStandardActions.add(this);
    }
    
    public AccessibilityAction(int paramInt, CharSequence paramCharSequence)
    {
      if (((0xFF000000 & paramInt) == 0) && (Integer.bitCount(paramInt) != 1)) {
        throw new IllegalArgumentException("Invalid standard action id");
      }
      mActionId = paramInt;
      mLabel = paramCharSequence;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if (paramObject == null) {
        return false;
      }
      if (paramObject == this) {
        return true;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      if (mActionId == mActionId) {
        bool = true;
      }
      return bool;
    }
    
    public int getId()
    {
      return mActionId;
    }
    
    public CharSequence getLabel()
    {
      return mLabel;
    }
    
    public int hashCode()
    {
      return mActionId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AccessibilityAction: ");
      localStringBuilder.append(AccessibilityNodeInfo.getActionSymbolicName(mActionId));
      localStringBuilder.append(" - ");
      localStringBuilder.append(mLabel);
      return localStringBuilder.toString();
    }
  }
  
  public static final class CollectionInfo
  {
    private static final int MAX_POOL_SIZE = 20;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    public static final int SELECTION_MODE_NONE = 0;
    public static final int SELECTION_MODE_SINGLE = 1;
    private static final Pools.SynchronizedPool<CollectionInfo> sPool = new Pools.SynchronizedPool(20);
    private int mColumnCount;
    private boolean mHierarchical;
    private int mRowCount;
    private int mSelectionMode;
    
    private CollectionInfo(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    {
      mRowCount = paramInt1;
      mColumnCount = paramInt2;
      mHierarchical = paramBoolean;
      mSelectionMode = paramInt3;
    }
    
    private void clear()
    {
      mRowCount = 0;
      mColumnCount = 0;
      mHierarchical = false;
      mSelectionMode = 0;
    }
    
    public static CollectionInfo obtain(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      return obtain(paramInt1, paramInt2, paramBoolean, 0);
    }
    
    public static CollectionInfo obtain(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    {
      CollectionInfo localCollectionInfo = (CollectionInfo)sPool.acquire();
      if (localCollectionInfo == null) {
        return new CollectionInfo(paramInt1, paramInt2, paramBoolean, paramInt3);
      }
      mRowCount = paramInt1;
      mColumnCount = paramInt2;
      mHierarchical = paramBoolean;
      mSelectionMode = paramInt3;
      return localCollectionInfo;
    }
    
    public static CollectionInfo obtain(CollectionInfo paramCollectionInfo)
    {
      return obtain(mRowCount, mColumnCount, mHierarchical, mSelectionMode);
    }
    
    public int getColumnCount()
    {
      return mColumnCount;
    }
    
    public int getRowCount()
    {
      return mRowCount;
    }
    
    public int getSelectionMode()
    {
      return mSelectionMode;
    }
    
    public boolean isHierarchical()
    {
      return mHierarchical;
    }
    
    void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
  
  public static final class CollectionItemInfo
  {
    private static final int MAX_POOL_SIZE = 20;
    private static final Pools.SynchronizedPool<CollectionItemInfo> sPool = new Pools.SynchronizedPool(20);
    private int mColumnIndex;
    private int mColumnSpan;
    private boolean mHeading;
    private int mRowIndex;
    private int mRowSpan;
    private boolean mSelected;
    
    private CollectionItemInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
    {
      mRowIndex = paramInt1;
      mRowSpan = paramInt2;
      mColumnIndex = paramInt3;
      mColumnSpan = paramInt4;
      mHeading = paramBoolean1;
      mSelected = paramBoolean2;
    }
    
    private void clear()
    {
      mColumnIndex = 0;
      mColumnSpan = 0;
      mRowIndex = 0;
      mRowSpan = 0;
      mHeading = false;
      mSelected = false;
    }
    
    public static CollectionItemInfo obtain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
    {
      return obtain(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean, false);
    }
    
    public static CollectionItemInfo obtain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
    {
      CollectionItemInfo localCollectionItemInfo = (CollectionItemInfo)sPool.acquire();
      if (localCollectionItemInfo == null) {
        return new CollectionItemInfo(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean1, paramBoolean2);
      }
      mRowIndex = paramInt1;
      mRowSpan = paramInt2;
      mColumnIndex = paramInt3;
      mColumnSpan = paramInt4;
      mHeading = paramBoolean1;
      mSelected = paramBoolean2;
      return localCollectionItemInfo;
    }
    
    public static CollectionItemInfo obtain(CollectionItemInfo paramCollectionItemInfo)
    {
      return obtain(mRowIndex, mRowSpan, mColumnIndex, mColumnSpan, mHeading, mSelected);
    }
    
    public int getColumnIndex()
    {
      return mColumnIndex;
    }
    
    public int getColumnSpan()
    {
      return mColumnSpan;
    }
    
    public int getRowIndex()
    {
      return mRowIndex;
    }
    
    public int getRowSpan()
    {
      return mRowSpan;
    }
    
    public boolean isHeading()
    {
      return mHeading;
    }
    
    public boolean isSelected()
    {
      return mSelected;
    }
    
    void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
  
  public static final class RangeInfo
  {
    private static final int MAX_POOL_SIZE = 10;
    public static final int RANGE_TYPE_FLOAT = 1;
    public static final int RANGE_TYPE_INT = 0;
    public static final int RANGE_TYPE_PERCENT = 2;
    private static final Pools.SynchronizedPool<RangeInfo> sPool = new Pools.SynchronizedPool(10);
    private float mCurrent;
    private float mMax;
    private float mMin;
    private int mType;
    
    private RangeInfo(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      mType = paramInt;
      mMin = paramFloat1;
      mMax = paramFloat2;
      mCurrent = paramFloat3;
    }
    
    private void clear()
    {
      mType = 0;
      mMin = 0.0F;
      mMax = 0.0F;
      mCurrent = 0.0F;
    }
    
    public static RangeInfo obtain(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      RangeInfo localRangeInfo = (RangeInfo)sPool.acquire();
      if (localRangeInfo == null) {
        return new RangeInfo(paramInt, paramFloat1, paramFloat2, paramFloat3);
      }
      mType = paramInt;
      mMin = paramFloat1;
      mMax = paramFloat2;
      mCurrent = paramFloat3;
      return localRangeInfo;
    }
    
    public static RangeInfo obtain(RangeInfo paramRangeInfo)
    {
      return obtain(mType, mMin, mMax, mCurrent);
    }
    
    public float getCurrent()
    {
      return mCurrent;
    }
    
    public float getMax()
    {
      return mMax;
    }
    
    public float getMin()
    {
      return mMin;
    }
    
    public int getType()
    {
      return mType;
    }
    
    void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
}
