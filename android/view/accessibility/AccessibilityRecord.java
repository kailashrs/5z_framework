package android.view.accessibility;

import android.os.Parcelable;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityRecord
{
  protected static final boolean DEBUG_CONCISE_TOSTRING = false;
  private static final int GET_SOURCE_PREFETCH_FLAGS = 7;
  private static final int MAX_POOL_SIZE = 10;
  private static final int PROPERTY_CHECKED = 1;
  private static final int PROPERTY_ENABLED = 2;
  private static final int PROPERTY_FULL_SCREEN = 128;
  private static final int PROPERTY_IMPORTANT_FOR_ACCESSIBILITY = 512;
  private static final int PROPERTY_PASSWORD = 4;
  private static final int PROPERTY_SCROLLABLE = 256;
  private static final int UNDEFINED = -1;
  private static AccessibilityRecord sPool;
  private static final Object sPoolLock = new Object();
  private static int sPoolSize;
  int mAddedCount = -1;
  CharSequence mBeforeText;
  int mBooleanProperties = 0;
  CharSequence mClassName;
  int mConnectionId = -1;
  CharSequence mContentDescription;
  int mCurrentItemIndex = -1;
  int mFromIndex = -1;
  private boolean mIsInPool;
  int mItemCount = -1;
  int mMaxScrollX = -1;
  int mMaxScrollY = -1;
  private AccessibilityRecord mNext;
  Parcelable mParcelableData;
  int mRemovedCount = -1;
  int mScrollDeltaX = -1;
  int mScrollDeltaY = -1;
  int mScrollX = -1;
  int mScrollY = -1;
  boolean mSealed;
  long mSourceNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
  int mSourceWindowId = -1;
  final List<CharSequence> mText = new ArrayList();
  int mToIndex = -1;
  
  AccessibilityRecord() {}
  
  private void append(StringBuilder paramStringBuilder, String paramString, int paramInt)
  {
    appendPropName(paramStringBuilder, paramString).append(paramInt);
  }
  
  private void append(StringBuilder paramStringBuilder, String paramString, Object paramObject)
  {
    appendPropName(paramStringBuilder, paramString).append(paramObject);
  }
  
  private StringBuilder appendPropName(StringBuilder paramStringBuilder, String paramString)
  {
    paramStringBuilder.append("; ");
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(": ");
    return paramStringBuilder;
  }
  
  private void appendUnless(boolean paramBoolean, int paramInt, StringBuilder paramStringBuilder)
  {
    paramBoolean = getBooleanProperty(paramInt);
    appendPropName(paramStringBuilder, singleBooleanPropertyToString(paramInt)).append(paramBoolean);
  }
  
  private boolean getBooleanProperty(int paramInt)
  {
    boolean bool;
    if ((mBooleanProperties & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static AccessibilityRecord obtain()
  {
    synchronized (sPoolLock)
    {
      if (sPool != null)
      {
        localAccessibilityRecord = sPool;
        sPool = sPoolmNext;
        sPoolSize -= 1;
        mNext = null;
        mIsInPool = false;
        return localAccessibilityRecord;
      }
      AccessibilityRecord localAccessibilityRecord = new android/view/accessibility/AccessibilityRecord;
      localAccessibilityRecord.<init>();
      return localAccessibilityRecord;
    }
  }
  
  public static AccessibilityRecord obtain(AccessibilityRecord paramAccessibilityRecord)
  {
    AccessibilityRecord localAccessibilityRecord = obtain();
    localAccessibilityRecord.init(paramAccessibilityRecord);
    return localAccessibilityRecord;
  }
  
  private void setBooleanProperty(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      mBooleanProperties |= paramInt;
    } else {
      mBooleanProperties &= paramInt;
    }
  }
  
  private static String singleBooleanPropertyToString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 128)
      {
        if (paramInt != 256)
        {
          if (paramInt != 512)
          {
            switch (paramInt)
            {
            default: 
              return Integer.toHexString(paramInt);
            case 2: 
              return "Enabled";
            }
            return "Checked";
          }
          return "ImportantForAccessibility";
        }
        return "Scrollable";
      }
      return "FullScreen";
    }
    return "Password";
  }
  
  StringBuilder appendTo(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append(" [ ClassName: ");
    paramStringBuilder.append(mClassName);
    appendPropName(paramStringBuilder, "Text").append(mText);
    append(paramStringBuilder, "ContentDescription", mContentDescription);
    append(paramStringBuilder, "ItemCount", mItemCount);
    append(paramStringBuilder, "CurrentItemIndex", mCurrentItemIndex);
    appendUnless(true, 2, paramStringBuilder);
    appendUnless(false, 4, paramStringBuilder);
    appendUnless(false, 1, paramStringBuilder);
    appendUnless(false, 128, paramStringBuilder);
    appendUnless(false, 256, paramStringBuilder);
    append(paramStringBuilder, "BeforeText", mBeforeText);
    append(paramStringBuilder, "FromIndex", mFromIndex);
    append(paramStringBuilder, "ToIndex", mToIndex);
    append(paramStringBuilder, "ScrollX", mScrollX);
    append(paramStringBuilder, "ScrollY", mScrollY);
    append(paramStringBuilder, "MaxScrollX", mMaxScrollX);
    append(paramStringBuilder, "MaxScrollY", mMaxScrollY);
    append(paramStringBuilder, "AddedCount", mAddedCount);
    append(paramStringBuilder, "RemovedCount", mRemovedCount);
    append(paramStringBuilder, "ParcelableData", mParcelableData);
    paramStringBuilder.append(" ]");
    return paramStringBuilder;
  }
  
  void clear()
  {
    mSealed = false;
    mBooleanProperties = 0;
    mCurrentItemIndex = -1;
    mItemCount = -1;
    mFromIndex = -1;
    mToIndex = -1;
    mScrollX = -1;
    mScrollY = -1;
    mMaxScrollX = -1;
    mMaxScrollY = -1;
    mAddedCount = -1;
    mRemovedCount = -1;
    mClassName = null;
    mContentDescription = null;
    mBeforeText = null;
    mParcelableData = null;
    mText.clear();
    mSourceNodeId = 2147483647L;
    mSourceWindowId = -1;
    mConnectionId = -1;
  }
  
  void enforceNotSealed()
  {
    if (!isSealed()) {
      return;
    }
    throw new IllegalStateException("Cannot perform this action on a sealed instance.");
  }
  
  void enforceSealed()
  {
    if (isSealed()) {
      return;
    }
    throw new IllegalStateException("Cannot perform this action on a not sealed instance.");
  }
  
  public int getAddedCount()
  {
    return mAddedCount;
  }
  
  public CharSequence getBeforeText()
  {
    return mBeforeText;
  }
  
  public CharSequence getClassName()
  {
    return mClassName;
  }
  
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  public int getCurrentItemIndex()
  {
    return mCurrentItemIndex;
  }
  
  public int getFromIndex()
  {
    return mFromIndex;
  }
  
  public int getItemCount()
  {
    return mItemCount;
  }
  
  public int getMaxScrollX()
  {
    return mMaxScrollX;
  }
  
  public int getMaxScrollY()
  {
    return mMaxScrollY;
  }
  
  public Parcelable getParcelableData()
  {
    return mParcelableData;
  }
  
  public int getRemovedCount()
  {
    return mRemovedCount;
  }
  
  public int getScrollDeltaX()
  {
    return mScrollDeltaX;
  }
  
  public int getScrollDeltaY()
  {
    return mScrollDeltaY;
  }
  
  public int getScrollX()
  {
    return mScrollX;
  }
  
  public int getScrollY()
  {
    return mScrollY;
  }
  
  public AccessibilityNodeInfo getSource()
  {
    enforceSealed();
    if ((mConnectionId != -1) && (mSourceWindowId != -1) && (AccessibilityNodeInfo.getAccessibilityViewId(mSourceNodeId) != Integer.MAX_VALUE)) {
      return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mSourceWindowId, mSourceNodeId, false, 7, null);
    }
    return null;
  }
  
  public long getSourceNodeId()
  {
    return mSourceNodeId;
  }
  
  public List<CharSequence> getText()
  {
    return mText;
  }
  
  public int getToIndex()
  {
    return mToIndex;
  }
  
  public int getWindowId()
  {
    return mSourceWindowId;
  }
  
  void init(AccessibilityRecord paramAccessibilityRecord)
  {
    mSealed = mSealed;
    mBooleanProperties = mBooleanProperties;
    mCurrentItemIndex = mCurrentItemIndex;
    mItemCount = mItemCount;
    mFromIndex = mFromIndex;
    mToIndex = mToIndex;
    mScrollX = mScrollX;
    mScrollY = mScrollY;
    mMaxScrollX = mMaxScrollX;
    mMaxScrollY = mMaxScrollY;
    mAddedCount = mAddedCount;
    mRemovedCount = mRemovedCount;
    mClassName = mClassName;
    mContentDescription = mContentDescription;
    mBeforeText = mBeforeText;
    mParcelableData = mParcelableData;
    mText.addAll(mText);
    mSourceWindowId = mSourceWindowId;
    mSourceNodeId = mSourceNodeId;
    mConnectionId = mConnectionId;
  }
  
  public boolean isChecked()
  {
    return getBooleanProperty(1);
  }
  
  public boolean isEnabled()
  {
    return getBooleanProperty(2);
  }
  
  public boolean isFullScreen()
  {
    return getBooleanProperty(128);
  }
  
  public boolean isImportantForAccessibility()
  {
    return getBooleanProperty(512);
  }
  
  public boolean isPassword()
  {
    return getBooleanProperty(4);
  }
  
  public boolean isScrollable()
  {
    return getBooleanProperty(256);
  }
  
  boolean isSealed()
  {
    return mSealed;
  }
  
  public void recycle()
  {
    if (!mIsInPool)
    {
      clear();
      synchronized (sPoolLock)
      {
        if (sPoolSize <= 10)
        {
          mNext = sPool;
          sPool = this;
          mIsInPool = true;
          sPoolSize += 1;
        }
        return;
      }
    }
    throw new IllegalStateException("Record already recycled!");
  }
  
  public void setAddedCount(int paramInt)
  {
    enforceNotSealed();
    mAddedCount = paramInt;
  }
  
  public void setBeforeText(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = paramCharSequence.subSequence(0, paramCharSequence.length());
    }
    mBeforeText = paramCharSequence;
  }
  
  public void setChecked(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(1, paramBoolean);
  }
  
  public void setClassName(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    mClassName = paramCharSequence;
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
  
  public void setCurrentItemIndex(int paramInt)
  {
    enforceNotSealed();
    mCurrentItemIndex = paramInt;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(2, paramBoolean);
  }
  
  public void setFromIndex(int paramInt)
  {
    enforceNotSealed();
    mFromIndex = paramInt;
  }
  
  public void setFullScreen(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(128, paramBoolean);
  }
  
  public void setImportantForAccessibility(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(512, paramBoolean);
  }
  
  public void setItemCount(int paramInt)
  {
    enforceNotSealed();
    mItemCount = paramInt;
  }
  
  public void setMaxScrollX(int paramInt)
  {
    enforceNotSealed();
    mMaxScrollX = paramInt;
  }
  
  public void setMaxScrollY(int paramInt)
  {
    enforceNotSealed();
    mMaxScrollY = paramInt;
  }
  
  public void setParcelableData(Parcelable paramParcelable)
  {
    enforceNotSealed();
    mParcelableData = paramParcelable;
  }
  
  public void setPassword(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(4, paramBoolean);
  }
  
  public void setRemovedCount(int paramInt)
  {
    enforceNotSealed();
    mRemovedCount = paramInt;
  }
  
  public void setScrollDeltaX(int paramInt)
  {
    enforceNotSealed();
    mScrollDeltaX = paramInt;
  }
  
  public void setScrollDeltaY(int paramInt)
  {
    enforceNotSealed();
    mScrollDeltaY = paramInt;
  }
  
  public void setScrollX(int paramInt)
  {
    enforceNotSealed();
    mScrollX = paramInt;
  }
  
  public void setScrollY(int paramInt)
  {
    enforceNotSealed();
    mScrollY = paramInt;
  }
  
  public void setScrollable(boolean paramBoolean)
  {
    enforceNotSealed();
    setBooleanProperty(256, paramBoolean);
  }
  
  public void setSealed(boolean paramBoolean)
  {
    mSealed = paramBoolean;
  }
  
  public void setSource(View paramView)
  {
    setSource(paramView, -1);
  }
  
  public void setSource(View paramView, int paramInt)
  {
    enforceNotSealed();
    boolean bool = true;
    int i = Integer.MAX_VALUE;
    mSourceWindowId = -1;
    if (paramView != null)
    {
      bool = paramView.isImportantForAccessibility();
      i = paramView.getAccessibilityViewId();
      mSourceWindowId = paramView.getAccessibilityWindowId();
    }
    setBooleanProperty(512, bool);
    mSourceNodeId = AccessibilityNodeInfo.makeNodeId(i, paramInt);
  }
  
  public void setSourceNodeId(long paramLong)
  {
    mSourceNodeId = paramLong;
  }
  
  public void setToIndex(int paramInt)
  {
    enforceNotSealed();
    mToIndex = paramInt;
  }
  
  public void setWindowId(int paramInt)
  {
    mSourceWindowId = paramInt;
  }
  
  public String toString()
  {
    return appendTo(new StringBuilder()).toString();
  }
}
