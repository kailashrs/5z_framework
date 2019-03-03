package android.view.accessibility;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.LongArray;
import android.util.Pools.SynchronizedPool;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class AccessibilityWindowInfo
  implements Parcelable
{
  public static final int ACTIVE_WINDOW_ID = Integer.MAX_VALUE;
  public static final int ANY_WINDOW_ID = -2;
  private static final int BOOLEAN_PROPERTY_ACCESSIBILITY_FOCUSED = 4;
  private static final int BOOLEAN_PROPERTY_ACTIVE = 1;
  private static final int BOOLEAN_PROPERTY_FOCUSED = 2;
  private static final int BOOLEAN_PROPERTY_PICTURE_IN_PICTURE = 8;
  public static final Parcelable.Creator<AccessibilityWindowInfo> CREATOR = new Parcelable.Creator()
  {
    public AccessibilityWindowInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      AccessibilityWindowInfo localAccessibilityWindowInfo = AccessibilityWindowInfo.obtain();
      localAccessibilityWindowInfo.initFromParcel(paramAnonymousParcel);
      return localAccessibilityWindowInfo;
    }
    
    public AccessibilityWindowInfo[] newArray(int paramAnonymousInt)
    {
      return new AccessibilityWindowInfo[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  private static final int MAX_POOL_SIZE = 10;
  public static final int PICTURE_IN_PICTURE_ACTION_REPLACER_WINDOW_ID = -3;
  public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
  public static final int TYPE_APPLICATION = 1;
  public static final int TYPE_INPUT_METHOD = 2;
  public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
  public static final int TYPE_SYSTEM = 3;
  public static final int UNDEFINED_WINDOW_ID = -1;
  private static AtomicInteger sNumInstancesInUse;
  private static final Pools.SynchronizedPool<AccessibilityWindowInfo> sPool = new Pools.SynchronizedPool(10);
  private long mAnchorId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
  private int mBooleanProperties;
  private final Rect mBoundsInScreen = new Rect();
  private LongArray mChildIds;
  private int mConnectionId = -1;
  private int mId = -1;
  private int mLayer = -1;
  private int mParentId = -1;
  private CharSequence mTitle;
  private int mType = -1;
  
  private AccessibilityWindowInfo() {}
  
  private void clear()
  {
    mType = -1;
    mLayer = -1;
    mBooleanProperties = 0;
    mId = -1;
    mParentId = -1;
    mBoundsInScreen.setEmpty();
    if (mChildIds != null) {
      mChildIds.clear();
    }
    mConnectionId = -1;
    mAnchorId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
    mTitle = null;
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
  
  private void initFromParcel(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mLayer = paramParcel.readInt();
    mBooleanProperties = paramParcel.readInt();
    mId = paramParcel.readInt();
    mParentId = paramParcel.readInt();
    mBoundsInScreen.readFromParcel(paramParcel);
    mTitle = paramParcel.readCharSequence();
    mAnchorId = paramParcel.readLong();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      if (mChildIds == null) {
        mChildIds = new LongArray(i);
      }
      for (int j = 0; j < i; j++)
      {
        int k = paramParcel.readInt();
        mChildIds.add(k);
      }
    }
    mConnectionId = paramParcel.readInt();
  }
  
  public static AccessibilityWindowInfo obtain()
  {
    AccessibilityWindowInfo localAccessibilityWindowInfo1 = (AccessibilityWindowInfo)sPool.acquire();
    AccessibilityWindowInfo localAccessibilityWindowInfo2 = localAccessibilityWindowInfo1;
    if (localAccessibilityWindowInfo1 == null) {
      localAccessibilityWindowInfo2 = new AccessibilityWindowInfo();
    }
    if (sNumInstancesInUse != null) {
      sNumInstancesInUse.incrementAndGet();
    }
    return localAccessibilityWindowInfo2;
  }
  
  public static AccessibilityWindowInfo obtain(AccessibilityWindowInfo paramAccessibilityWindowInfo)
  {
    AccessibilityWindowInfo localAccessibilityWindowInfo = obtain();
    mType = mType;
    mLayer = mLayer;
    mBooleanProperties = mBooleanProperties;
    mId = mId;
    mParentId = mParentId;
    mBoundsInScreen.set(mBoundsInScreen);
    mTitle = mTitle;
    mAnchorId = mAnchorId;
    if ((mChildIds != null) && (mChildIds.size() > 0)) {
      if (mChildIds == null) {
        mChildIds = mChildIds.clone();
      } else {
        mChildIds.addAll(mChildIds);
      }
    }
    mConnectionId = mConnectionId;
    return localAccessibilityWindowInfo;
  }
  
  private void setBooleanProperty(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      mBooleanProperties |= paramInt;
    } else {
      mBooleanProperties &= paramInt;
    }
  }
  
  public static void setNumInstancesInUseCounter(AtomicInteger paramAtomicInteger)
  {
    if (sNumInstancesInUse != null) {
      sNumInstancesInUse = paramAtomicInteger;
    }
  }
  
  private static String typeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "<UNKNOWN>";
    case 5: 
      return "TYPE_SPLIT_SCREEN_DIVIDER";
    case 4: 
      return "TYPE_ACCESSIBILITY_OVERLAY";
    case 3: 
      return "TYPE_SYSTEM";
    case 2: 
      return "TYPE_INPUT_METHOD";
    }
    return "TYPE_APPLICATION";
  }
  
  public void addChild(int paramInt)
  {
    if (mChildIds == null) {
      mChildIds = new LongArray();
    }
    mChildIds.add(paramInt);
  }
  
  public boolean changed(AccessibilityWindowInfo paramAccessibilityWindowInfo)
  {
    if (mId == mId)
    {
      if (mType == mType)
      {
        if (!mBoundsInScreen.equals(mBoundsInScreen)) {
          return true;
        }
        if (mLayer != mLayer) {
          return true;
        }
        if (mBooleanProperties != mBooleanProperties) {
          return true;
        }
        if (mParentId != mParentId) {
          return true;
        }
        if (mChildIds == null)
        {
          if (mChildIds != null) {
            return true;
          }
        }
        else if (!mChildIds.equals(mChildIds)) {
          return true;
        }
        return false;
      }
      throw new IllegalArgumentException("Not same type.");
    }
    throw new IllegalArgumentException("Not same window.");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int differenceFrom(AccessibilityWindowInfo paramAccessibilityWindowInfo)
  {
    if (mId == mId)
    {
      if (mType == mType)
      {
        int i = 0;
        if (!TextUtils.equals(mTitle, mTitle)) {
          i = 0x0 | 0x4;
        }
        int j = i;
        if (!mBoundsInScreen.equals(mBoundsInScreen)) {
          j = i | 0x8;
        }
        i = j;
        if (mLayer != mLayer) {
          i = j | 0x10;
        }
        j = i;
        if (getBooleanProperty(1) != paramAccessibilityWindowInfo.getBooleanProperty(1)) {
          j = i | 0x20;
        }
        i = j;
        if (getBooleanProperty(2) != paramAccessibilityWindowInfo.getBooleanProperty(2)) {
          i = j | 0x40;
        }
        j = i;
        if (getBooleanProperty(4) != paramAccessibilityWindowInfo.getBooleanProperty(4)) {
          j = i | 0x80;
        }
        i = j;
        if (getBooleanProperty(8) != paramAccessibilityWindowInfo.getBooleanProperty(8)) {
          i = j | 0x400;
        }
        j = i;
        if (mParentId != mParentId) {
          j = i | 0x100;
        }
        i = j;
        if (!Objects.equals(mChildIds, mChildIds)) {
          i = j | 0x200;
        }
        return i;
      }
      throw new IllegalArgumentException("Not same type.");
    }
    throw new IllegalArgumentException("Not same window.");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AccessibilityWindowInfo)paramObject;
    if (mId != mId) {
      bool = false;
    }
    return bool;
  }
  
  public AccessibilityNodeInfo getAnchor()
  {
    if ((mConnectionId != -1) && (mAnchorId != AccessibilityNodeInfo.UNDEFINED_NODE_ID) && (mParentId != -1)) {
      return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mParentId, mAnchorId, true, 0, null);
    }
    return null;
  }
  
  public void getBoundsInScreen(Rect paramRect)
  {
    paramRect.set(mBoundsInScreen);
  }
  
  public AccessibilityWindowInfo getChild(int paramInt)
  {
    if (mChildIds != null)
    {
      if (mConnectionId == -1) {
        return null;
      }
      paramInt = (int)mChildIds.get(paramInt);
      return AccessibilityInteractionClient.getInstance().getWindow(mConnectionId, paramInt);
    }
    throw new IndexOutOfBoundsException();
  }
  
  public int getChildCount()
  {
    int i;
    if (mChildIds != null) {
      i = mChildIds.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getLayer()
  {
    return mLayer;
  }
  
  public AccessibilityWindowInfo getParent()
  {
    if ((mConnectionId != -1) && (mParentId != -1)) {
      return AccessibilityInteractionClient.getInstance().getWindow(mConnectionId, mParentId);
    }
    return null;
  }
  
  public AccessibilityNodeInfo getRoot()
  {
    if (mConnectionId == -1) {
      return null;
    }
    return AccessibilityInteractionClient.getInstance().findAccessibilityNodeInfoByAccessibilityId(mConnectionId, mId, AccessibilityNodeInfo.ROOT_NODE_ID, true, 4, null);
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return mId;
  }
  
  public boolean isAccessibilityFocused()
  {
    return getBooleanProperty(4);
  }
  
  public boolean isActive()
  {
    return getBooleanProperty(1);
  }
  
  public boolean isFocused()
  {
    return getBooleanProperty(2);
  }
  
  public boolean isInPictureInPictureMode()
  {
    return getBooleanProperty(8);
  }
  
  public void recycle()
  {
    clear();
    sPool.release(this);
    if (sNumInstancesInUse != null) {
      sNumInstancesInUse.decrementAndGet();
    }
  }
  
  public void setAccessibilityFocused(boolean paramBoolean)
  {
    setBooleanProperty(4, paramBoolean);
  }
  
  public void setActive(boolean paramBoolean)
  {
    setBooleanProperty(1, paramBoolean);
  }
  
  public void setAnchorId(long paramLong)
  {
    mAnchorId = paramLong;
  }
  
  public void setBoundsInScreen(Rect paramRect)
  {
    mBoundsInScreen.set(paramRect);
  }
  
  public void setConnectionId(int paramInt)
  {
    mConnectionId = paramInt;
  }
  
  public void setFocused(boolean paramBoolean)
  {
    setBooleanProperty(2, paramBoolean);
  }
  
  public void setId(int paramInt)
  {
    mId = paramInt;
  }
  
  public void setLayer(int paramInt)
  {
    mLayer = paramInt;
  }
  
  public void setParentId(int paramInt)
  {
    mParentId = paramInt;
  }
  
  public void setPictureInPicture(boolean paramBoolean)
  {
    setBooleanProperty(8, paramBoolean);
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
  }
  
  public void setType(int paramInt)
  {
    mType = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AccessibilityWindowInfo[");
    localStringBuilder.append("title=");
    localStringBuilder.append(mTitle);
    localStringBuilder.append(", id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", type=");
    localStringBuilder.append(typeToString(mType));
    localStringBuilder.append(", layer=");
    localStringBuilder.append(mLayer);
    localStringBuilder.append(", bounds=");
    localStringBuilder.append(mBoundsInScreen);
    localStringBuilder.append(", focused=");
    localStringBuilder.append(isFocused());
    localStringBuilder.append(", active=");
    localStringBuilder.append(isActive());
    localStringBuilder.append(", pictureInPicture=");
    localStringBuilder.append(isInPictureInPictureMode());
    localStringBuilder.append(", hasParent=");
    int i = mParentId;
    boolean bool1 = false;
    boolean bool2;
    if (i != -1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    localStringBuilder.append(bool2);
    localStringBuilder.append(", isAnchored=");
    if (mAnchorId != AccessibilityNodeInfo.UNDEFINED_NODE_ID) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    localStringBuilder.append(bool2);
    localStringBuilder.append(", hasChildren=");
    if ((mChildIds != null) && (mChildIds.size() > 0)) {
      bool2 = true;
    } else {
      bool2 = bool1;
    }
    localStringBuilder.append(bool2);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mLayer);
    paramParcel.writeInt(mBooleanProperties);
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mParentId);
    mBoundsInScreen.writeToParcel(paramParcel, paramInt);
    paramParcel.writeCharSequence(mTitle);
    paramParcel.writeLong(mAnchorId);
    LongArray localLongArray = mChildIds;
    paramInt = 0;
    if (localLongArray == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      int i = localLongArray.size();
      paramParcel.writeInt(i);
      while (paramInt < i)
      {
        paramParcel.writeInt((int)localLongArray.get(paramInt));
        paramInt++;
      }
    }
    paramParcel.writeInt(mConnectionId);
  }
}
