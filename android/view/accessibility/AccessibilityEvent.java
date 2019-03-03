package android.view.accessibility;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.BitUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public final class AccessibilityEvent
  extends AccessibilityRecord
  implements Parcelable
{
  public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
  public static final int CONTENT_CHANGE_TYPE_PANE_APPEARED = 16;
  public static final int CONTENT_CHANGE_TYPE_PANE_DISAPPEARED = 32;
  public static final int CONTENT_CHANGE_TYPE_PANE_TITLE = 8;
  public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
  public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
  public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
  public static final Parcelable.Creator<AccessibilityEvent> CREATOR = new Parcelable.Creator()
  {
    public AccessibilityEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
      localAccessibilityEvent.initFromParcel(paramAnonymousParcel);
      return localAccessibilityEvent;
    }
    
    public AccessibilityEvent[] newArray(int paramAnonymousInt)
    {
      return new AccessibilityEvent[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  public static final boolean DEBUG_ORIGIN = false;
  public static final int INVALID_POSITION = -1;
  private static final int MAX_POOL_SIZE = 10;
  @Deprecated
  public static final int MAX_TEXT_LENGTH = 500;
  public static final int TYPES_ALL_MASK = -1;
  public static final int TYPE_ANNOUNCEMENT = 16384;
  public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
  public static final int TYPE_GESTURE_DETECTION_END = 524288;
  public static final int TYPE_GESTURE_DETECTION_START = 262144;
  public static final int TYPE_NOTIFICATION_STATE_CHANGED = 64;
  public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
  public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
  public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
  public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
  public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
  public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
  public static final int TYPE_VIEW_CLICKED = 1;
  public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
  public static final int TYPE_VIEW_FOCUSED = 8;
  public static final int TYPE_VIEW_HOVER_ENTER = 128;
  public static final int TYPE_VIEW_HOVER_EXIT = 256;
  public static final int TYPE_VIEW_LONG_CLICKED = 2;
  public static final int TYPE_VIEW_SCROLLED = 4096;
  public static final int TYPE_VIEW_SELECTED = 4;
  public static final int TYPE_VIEW_TEXT_CHANGED = 16;
  public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
  public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
  public static final int TYPE_WINDOWS_CHANGED = 4194304;
  public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;
  public static final int TYPE_WINDOW_STATE_CHANGED = 32;
  public static final int WINDOWS_CHANGE_ACCESSIBILITY_FOCUSED = 128;
  public static final int WINDOWS_CHANGE_ACTIVE = 32;
  public static final int WINDOWS_CHANGE_ADDED = 1;
  public static final int WINDOWS_CHANGE_BOUNDS = 8;
  public static final int WINDOWS_CHANGE_CHILDREN = 512;
  public static final int WINDOWS_CHANGE_FOCUSED = 64;
  public static final int WINDOWS_CHANGE_LAYER = 16;
  public static final int WINDOWS_CHANGE_PARENT = 256;
  public static final int WINDOWS_CHANGE_PIP = 1024;
  public static final int WINDOWS_CHANGE_REMOVED = 2;
  public static final int WINDOWS_CHANGE_TITLE = 4;
  private static final Pools.SynchronizedPool<AccessibilityEvent> sPool = new Pools.SynchronizedPool(10);
  int mAction;
  int mContentChangeTypes;
  private long mEventTime;
  private int mEventType;
  int mMovementGranularity;
  private CharSequence mPackageName;
  private ArrayList<AccessibilityRecord> mRecords;
  int mWindowChangeTypes;
  public StackTraceElement[] originStackTrace = null;
  
  private AccessibilityEvent() {}
  
  private static String contentChangeTypesToString(int paramInt)
  {
    return BitUtils.flagsToString(paramInt, _..Lambda.AccessibilityEvent.gjyLj65KEDUo5PJZiVYxPrd2Vug.INSTANCE);
  }
  
  public static String eventTypeToString(int paramInt)
  {
    if (paramInt == -1) {
      return "TYPES_ALL_MASK";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramInt;
    for (paramInt = 0; i != 0; paramInt++)
    {
      int j = 1 << Integer.numberOfTrailingZeros(i);
      i &= j;
      if (paramInt > 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(singleEventTypeToString(j));
    }
    if (paramInt > 1)
    {
      localStringBuilder.insert(0, '[');
      localStringBuilder.append(']');
    }
    return localStringBuilder.toString();
  }
  
  public static AccessibilityEvent obtain()
  {
    AccessibilityEvent localAccessibilityEvent1 = (AccessibilityEvent)sPool.acquire();
    AccessibilityEvent localAccessibilityEvent2 = localAccessibilityEvent1;
    if (localAccessibilityEvent1 == null) {
      localAccessibilityEvent2 = new AccessibilityEvent();
    }
    return localAccessibilityEvent2;
  }
  
  public static AccessibilityEvent obtain(int paramInt)
  {
    AccessibilityEvent localAccessibilityEvent = obtain();
    localAccessibilityEvent.setEventType(paramInt);
    return localAccessibilityEvent;
  }
  
  public static AccessibilityEvent obtain(AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityEvent localAccessibilityEvent = obtain();
    localAccessibilityEvent.init(paramAccessibilityEvent);
    if (mRecords != null)
    {
      int i = mRecords.size();
      mRecords = new ArrayList(i);
      for (int j = 0; j < i; j++)
      {
        AccessibilityRecord localAccessibilityRecord = AccessibilityRecord.obtain((AccessibilityRecord)mRecords.get(j));
        mRecords.add(localAccessibilityRecord);
      }
    }
    return localAccessibilityEvent;
  }
  
  public static AccessibilityEvent obtainWindowsChangedEvent(int paramInt1, int paramInt2)
  {
    AccessibilityEvent localAccessibilityEvent = obtain(4194304);
    localAccessibilityEvent.setWindowId(paramInt1);
    localAccessibilityEvent.setWindowChanges(paramInt2);
    localAccessibilityEvent.setImportantForAccessibility(true);
    return localAccessibilityEvent;
  }
  
  private void readAccessibilityRecordFromParcel(AccessibilityRecord paramAccessibilityRecord, Parcel paramParcel)
  {
    mBooleanProperties = paramParcel.readInt();
    mCurrentItemIndex = paramParcel.readInt();
    mItemCount = paramParcel.readInt();
    mFromIndex = paramParcel.readInt();
    mToIndex = paramParcel.readInt();
    mScrollX = paramParcel.readInt();
    mScrollY = paramParcel.readInt();
    mScrollDeltaX = paramParcel.readInt();
    mScrollDeltaY = paramParcel.readInt();
    mMaxScrollX = paramParcel.readInt();
    mMaxScrollY = paramParcel.readInt();
    mAddedCount = paramParcel.readInt();
    mRemovedCount = paramParcel.readInt();
    mClassName = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mContentDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mBeforeText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mParcelableData = paramParcel.readParcelable(null);
    paramParcel.readList(mText, null);
    mSourceWindowId = paramParcel.readInt();
    mSourceNodeId = paramParcel.readLong();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mSealed = bool;
  }
  
  private static String singleContentChangeTypeToString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        switch (paramInt)
        {
        default: 
          return Integer.toHexString(paramInt);
        case 2: 
          return "CONTENT_CHANGE_TYPE_TEXT";
        case 1: 
          return "CONTENT_CHANGE_TYPE_SUBTREE";
        }
        return "CONTENT_CHANGE_TYPE_UNDEFINED";
      }
      return "CONTENT_CHANGE_TYPE_PANE_TITLE";
    }
    return "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION";
  }
  
  private static String singleEventTypeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toHexString(paramInt);
    case 16777216: 
      return "TYPE_ASSIST_READING_CONTEXT";
    case 8388608: 
      return "TYPE_VIEW_CONTEXT_CLICKED";
    case 4194304: 
      return "TYPE_WINDOWS_CHANGED";
    case 2097152: 
      return "TYPE_TOUCH_INTERACTION_END";
    case 1048576: 
      return "TYPE_TOUCH_INTERACTION_START";
    case 524288: 
      return "TYPE_GESTURE_DETECTION_END";
    case 262144: 
      return "TYPE_GESTURE_DETECTION_START";
    case 131072: 
      return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
    case 65536: 
      return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
    case 32768: 
      return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
    case 16384: 
      return "TYPE_ANNOUNCEMENT";
    case 8192: 
      return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
    case 4096: 
      return "TYPE_VIEW_SCROLLED";
    case 2048: 
      return "TYPE_WINDOW_CONTENT_CHANGED";
    case 1024: 
      return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
    case 512: 
      return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
    case 256: 
      return "TYPE_VIEW_HOVER_EXIT";
    case 128: 
      return "TYPE_VIEW_HOVER_ENTER";
    case 64: 
      return "TYPE_NOTIFICATION_STATE_CHANGED";
    case 32: 
      return "TYPE_WINDOW_STATE_CHANGED";
    case 16: 
      return "TYPE_VIEW_TEXT_CHANGED";
    case 8: 
      return "TYPE_VIEW_FOCUSED";
    case 4: 
      return "TYPE_VIEW_SELECTED";
    case 2: 
      return "TYPE_VIEW_LONG_CLICKED";
    }
    return "TYPE_VIEW_CLICKED";
  }
  
  private static String singleWindowChangeTypeToString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          if (paramInt != 32)
          {
            if (paramInt != 64)
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
                      return "WINDOWS_CHANGE_REMOVED";
                    }
                    return "WINDOWS_CHANGE_ADDED";
                  }
                  return "WINDOWS_CHANGE_CHILDREN";
                }
                return "WINDOWS_CHANGE_PARENT";
              }
              return "WINDOWS_CHANGE_ACCESSIBILITY_FOCUSED";
            }
            return "WINDOWS_CHANGE_FOCUSED";
          }
          return "WINDOWS_CHANGE_ACTIVE";
        }
        return "WINDOWS_CHANGE_LAYER";
      }
      return "WINDOWS_CHANGE_BOUNDS";
    }
    return "WINDOWS_CHANGE_TITLE";
  }
  
  private static String windowChangeTypesToString(int paramInt)
  {
    return BitUtils.flagsToString(paramInt, _..Lambda.AccessibilityEvent.c6ikd5OkCnJv2aVsheVXIxBvSTk.INSTANCE);
  }
  
  private void writeAccessibilityRecordToParcel(AccessibilityRecord paramAccessibilityRecord, Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mBooleanProperties);
    paramParcel.writeInt(mCurrentItemIndex);
    paramParcel.writeInt(mItemCount);
    paramParcel.writeInt(mFromIndex);
    paramParcel.writeInt(mToIndex);
    paramParcel.writeInt(mScrollX);
    paramParcel.writeInt(mScrollY);
    paramParcel.writeInt(mScrollDeltaX);
    paramParcel.writeInt(mScrollDeltaY);
    paramParcel.writeInt(mMaxScrollX);
    paramParcel.writeInt(mMaxScrollY);
    paramParcel.writeInt(mAddedCount);
    paramParcel.writeInt(mRemovedCount);
    TextUtils.writeToParcel(mClassName, paramParcel, paramInt);
    TextUtils.writeToParcel(mContentDescription, paramParcel, paramInt);
    TextUtils.writeToParcel(mBeforeText, paramParcel, paramInt);
    paramParcel.writeParcelable(mParcelableData, paramInt);
    paramParcel.writeList(mText);
    paramParcel.writeInt(mSourceWindowId);
    paramParcel.writeLong(mSourceNodeId);
    paramParcel.writeInt(mSealed);
  }
  
  public void appendRecord(AccessibilityRecord paramAccessibilityRecord)
  {
    enforceNotSealed();
    if (mRecords == null) {
      mRecords = new ArrayList();
    }
    mRecords.add(paramAccessibilityRecord);
  }
  
  protected void clear()
  {
    super.clear();
    mEventType = 0;
    mMovementGranularity = 0;
    mAction = 0;
    mContentChangeTypes = 0;
    mWindowChangeTypes = 0;
    mPackageName = null;
    mEventTime = 0L;
    if (mRecords != null) {
      while (!mRecords.isEmpty()) {
        ((AccessibilityRecord)mRecords.remove(0)).recycle();
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAction()
  {
    return mAction;
  }
  
  public int getContentChangeTypes()
  {
    return mContentChangeTypes;
  }
  
  public long getEventTime()
  {
    return mEventTime;
  }
  
  public int getEventType()
  {
    return mEventType;
  }
  
  public int getMovementGranularity()
  {
    return mMovementGranularity;
  }
  
  public CharSequence getPackageName()
  {
    return mPackageName;
  }
  
  public AccessibilityRecord getRecord(int paramInt)
  {
    if (mRecords != null) {
      return (AccessibilityRecord)mRecords.get(paramInt);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid index ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(", size is 0");
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public int getRecordCount()
  {
    int i;
    if (mRecords == null) {
      i = 0;
    } else {
      i = mRecords.size();
    }
    return i;
  }
  
  public int getWindowChanges()
  {
    return mWindowChangeTypes;
  }
  
  void init(AccessibilityEvent paramAccessibilityEvent)
  {
    super.init(paramAccessibilityEvent);
    mEventType = mEventType;
    mMovementGranularity = mMovementGranularity;
    mAction = mAction;
    mContentChangeTypes = mContentChangeTypes;
    mWindowChangeTypes = mWindowChangeTypes;
    mEventTime = mEventTime;
    mPackageName = mPackageName;
  }
  
  public void initFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    int j = 0;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mSealed = bool;
    mEventType = paramParcel.readInt();
    mMovementGranularity = paramParcel.readInt();
    mAction = paramParcel.readInt();
    mContentChangeTypes = paramParcel.readInt();
    mWindowChangeTypes = paramParcel.readInt();
    mPackageName = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mEventTime = paramParcel.readLong();
    mConnectionId = paramParcel.readInt();
    readAccessibilityRecordFromParcel(this, paramParcel);
    i = paramParcel.readInt();
    if (i > 0)
    {
      mRecords = new ArrayList(i);
      while (j < i)
      {
        AccessibilityRecord localAccessibilityRecord = AccessibilityRecord.obtain();
        readAccessibilityRecordFromParcel(localAccessibilityRecord, paramParcel);
        mConnectionId = mConnectionId;
        mRecords.add(localAccessibilityRecord);
        j++;
      }
    }
  }
  
  public void recycle()
  {
    clear();
    sPool.release(this);
  }
  
  public void setAction(int paramInt)
  {
    enforceNotSealed();
    mAction = paramInt;
  }
  
  public void setContentChangeTypes(int paramInt)
  {
    enforceNotSealed();
    mContentChangeTypes = paramInt;
  }
  
  public void setEventTime(long paramLong)
  {
    enforceNotSealed();
    mEventTime = paramLong;
  }
  
  public void setEventType(int paramInt)
  {
    enforceNotSealed();
    mEventType = paramInt;
  }
  
  public void setMovementGranularity(int paramInt)
  {
    enforceNotSealed();
    mMovementGranularity = paramInt;
  }
  
  public void setPackageName(CharSequence paramCharSequence)
  {
    enforceNotSealed();
    mPackageName = paramCharSequence;
  }
  
  public void setSealed(boolean paramBoolean)
  {
    super.setSealed(paramBoolean);
    ArrayList localArrayList = mRecords;
    if (localArrayList != null)
    {
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((AccessibilityRecord)localArrayList.get(j)).setSealed(paramBoolean);
      }
    }
  }
  
  public void setWindowChanges(int paramInt)
  {
    mWindowChangeTypes = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("EventType: ");
    localStringBuilder.append(eventTypeToString(mEventType));
    localStringBuilder.append("; EventTime: ");
    localStringBuilder.append(mEventTime);
    localStringBuilder.append("; PackageName: ");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append("; MovementGranularity: ");
    localStringBuilder.append(mMovementGranularity);
    localStringBuilder.append("; Action: ");
    localStringBuilder.append(mAction);
    localStringBuilder.append("; ContentChangeTypes: ");
    localStringBuilder.append(contentChangeTypesToString(mContentChangeTypes));
    localStringBuilder.append("; WindowChangeTypes: ");
    localStringBuilder.append(windowChangeTypesToString(mWindowChangeTypes));
    super.appendTo(localStringBuilder);
    localStringBuilder.append("; recordCount: ");
    localStringBuilder.append(getRecordCount());
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(isSealed());
    paramParcel.writeInt(mEventType);
    paramParcel.writeInt(mMovementGranularity);
    paramParcel.writeInt(mAction);
    paramParcel.writeInt(mContentChangeTypes);
    paramParcel.writeInt(mWindowChangeTypes);
    CharSequence localCharSequence = mPackageName;
    int i = 0;
    TextUtils.writeToParcel(localCharSequence, paramParcel, 0);
    paramParcel.writeLong(mEventTime);
    paramParcel.writeInt(mConnectionId);
    writeAccessibilityRecordToParcel(this, paramParcel, paramInt);
    int j = getRecordCount();
    paramParcel.writeInt(j);
    while (i < j)
    {
      writeAccessibilityRecordToParcel((AccessibilityRecord)mRecords.get(i), paramParcel, paramInt);
      i++;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ContentChangeTypes {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EventType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface WindowsChangeTypes {}
}
