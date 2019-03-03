package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Annotation;

public class WindowConfiguration
  implements Parcelable, Comparable<WindowConfiguration>
{
  public static final int ACTIVITY_TYPE_ASSISTANT = 4;
  public static final int ACTIVITY_TYPE_HOME = 2;
  public static final int ACTIVITY_TYPE_RECENTS = 3;
  public static final int ACTIVITY_TYPE_STANDARD = 1;
  public static final int ACTIVITY_TYPE_UNDEFINED = 0;
  public static final Parcelable.Creator<WindowConfiguration> CREATOR = new Parcelable.Creator()
  {
    public WindowConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WindowConfiguration(paramAnonymousParcel, null);
    }
    
    public WindowConfiguration[] newArray(int paramAnonymousInt)
    {
      return new WindowConfiguration[paramAnonymousInt];
    }
  };
  public static final int PINNED_WINDOWING_MODE_ELEVATION_IN_DIP = 5;
  public static final int WINDOWING_MODE_FREEFORM = 5;
  public static final int WINDOWING_MODE_FULLSCREEN = 1;
  public static final int WINDOWING_MODE_FULLSCREEN_OR_SPLIT_SCREEN_SECONDARY = 4;
  public static final int WINDOWING_MODE_PINNED = 2;
  public static final int WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = 3;
  public static final int WINDOWING_MODE_SPLIT_SCREEN_SECONDARY = 4;
  public static final int WINDOWING_MODE_UNDEFINED = 0;
  public static final int WINDOW_CONFIG_ACTIVITY_TYPE = 8;
  public static final int WINDOW_CONFIG_APP_BOUNDS = 2;
  public static final int WINDOW_CONFIG_BOUNDS = 1;
  public static final int WINDOW_CONFIG_WINDOWING_MODE = 4;
  @ActivityType
  private int mActivityType;
  private Rect mAppBounds;
  private Rect mBounds = new Rect();
  @WindowingMode
  private int mWindowingMode;
  
  public WindowConfiguration()
  {
    unset();
  }
  
  public WindowConfiguration(WindowConfiguration paramWindowConfiguration)
  {
    setTo(paramWindowConfiguration);
  }
  
  private WindowConfiguration(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static String activityTypeToString(@ActivityType int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return String.valueOf(paramInt);
    case 4: 
      return "assistant";
    case 3: 
      return "recents";
    case 2: 
      return "home";
    case 1: 
      return "standard";
    }
    return "undefined";
  }
  
  public static boolean isFloating(int paramInt)
  {
    boolean bool;
    if ((paramInt != 5) && (paramInt != 2)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mBounds = ((Rect)paramParcel.readParcelable(Rect.class.getClassLoader()));
    mAppBounds = ((Rect)paramParcel.readParcelable(Rect.class.getClassLoader()));
    mWindowingMode = paramParcel.readInt();
    mActivityType = paramParcel.readInt();
  }
  
  public static boolean supportSplitScreenWindowingMode(int paramInt)
  {
    boolean bool;
    if (paramInt != 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String windowingModeToString(@WindowingMode int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return String.valueOf(paramInt);
    case 5: 
      return "freeform";
    case 4: 
      return "split-screen-secondary";
    case 3: 
      return "split-screen-primary";
    case 2: 
      return "pinned";
    case 1: 
      return "fullscreen";
    }
    return "undefined";
  }
  
  public boolean canReceiveKeys()
  {
    boolean bool;
    if (mWindowingMode != 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canResizeTask()
  {
    boolean bool;
    if (mWindowingMode == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int compareTo(WindowConfiguration paramWindowConfiguration)
  {
    if ((mAppBounds == null) && (mAppBounds != null)) {
      return 1;
    }
    if ((mAppBounds != null) && (mAppBounds == null)) {
      return -1;
    }
    if ((mAppBounds != null) && (mAppBounds != null))
    {
      i = mAppBounds.left - mAppBounds.left;
      if (i != 0) {
        return i;
      }
      i = mAppBounds.top - mAppBounds.top;
      if (i != 0) {
        return i;
      }
      i = mAppBounds.right - mAppBounds.right;
      if (i != 0) {
        return i;
      }
      i = mAppBounds.bottom - mAppBounds.bottom;
      if (i != 0) {
        return i;
      }
    }
    int i = mBounds.left - mBounds.left;
    if (i != 0) {
      return i;
    }
    i = mBounds.top - mBounds.top;
    if (i != 0) {
      return i;
    }
    i = mBounds.right - mBounds.right;
    if (i != 0) {
      return i;
    }
    i = mBounds.bottom - mBounds.bottom;
    if (i != 0) {
      return i;
    }
    i = mWindowingMode - mWindowingMode;
    if (i != 0) {
      return i;
    }
    i = mActivityType - mActivityType;
    if (i != 0) {
      return i;
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @WindowConfig
  public long diff(WindowConfiguration paramWindowConfiguration, boolean paramBoolean)
  {
    long l1 = 0L;
    if (!mBounds.equals(mBounds)) {
      l1 = 0L | 1L;
    }
    long l2;
    if (!paramBoolean)
    {
      l2 = l1;
      if (mAppBounds == null) {}
    }
    else
    {
      l2 = l1;
      if (mAppBounds != mAppBounds) {
        if (mAppBounds != null)
        {
          l2 = l1;
          if (mAppBounds.equals(mAppBounds)) {}
        }
        else
        {
          l2 = l1 | 0x2;
        }
      }
    }
    if (!paramBoolean)
    {
      l1 = l2;
      if (mWindowingMode == 0) {}
    }
    else
    {
      l1 = l2;
      if (mWindowingMode != mWindowingMode) {
        l1 = l2 | 0x4;
      }
    }
    if (!paramBoolean)
    {
      l2 = l1;
      if (mActivityType == 0) {}
    }
    else
    {
      l2 = l1;
      if (mActivityType != mActivityType) {
        l2 = l1 | 0x8;
      }
    }
    return l2;
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
    if (!(paramObject instanceof WindowConfiguration)) {
      return false;
    }
    if (compareTo((WindowConfiguration)paramObject) == 0) {
      bool = true;
    }
    return bool;
  }
  
  @ActivityType
  public int getActivityType()
  {
    return mActivityType;
  }
  
  public Rect getAppBounds()
  {
    return mAppBounds;
  }
  
  public Rect getBounds()
  {
    return mBounds;
  }
  
  @WindowingMode
  public int getWindowingMode()
  {
    return mWindowingMode;
  }
  
  public boolean hasMovementAnimations()
  {
    boolean bool;
    if (mWindowingMode != 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasWindowDecorCaption()
  {
    boolean bool;
    if (mWindowingMode == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasWindowShadow()
  {
    return tasksAreFloating();
  }
  
  public int hashCode()
  {
    int i = 0;
    if (mAppBounds != null) {
      i = 31 * 0 + mAppBounds.hashCode();
    }
    return 31 * (31 * (31 * i + mBounds.hashCode()) + mWindowingMode) + mActivityType;
  }
  
  public boolean isAlwaysOnTop()
  {
    boolean bool;
    if (mWindowingMode == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean keepVisibleDeadAppWindowOnScreen()
  {
    boolean bool;
    if (mWindowingMode != 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean persistTaskBounds()
  {
    boolean bool;
    if (mWindowingMode == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setActivityType(@ActivityType int paramInt)
  {
    if (mActivityType == paramInt) {
      return;
    }
    if ((ActivityThread.isSystem()) && (mActivityType != 0) && (paramInt != 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Can't change activity type once set: ");
      localStringBuilder.append(this);
      localStringBuilder.append(" activityType=");
      localStringBuilder.append(activityTypeToString(paramInt));
      throw new IllegalStateException(localStringBuilder.toString());
    }
    mActivityType = paramInt;
  }
  
  public void setAppBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mAppBounds == null) {
      mAppBounds = new Rect();
    }
    mAppBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setAppBounds(Rect paramRect)
  {
    if (paramRect == null)
    {
      mAppBounds = null;
      return;
    }
    setAppBounds(left, top, right, bottom);
  }
  
  public void setBounds(Rect paramRect)
  {
    if (paramRect == null)
    {
      mBounds.setEmpty();
      return;
    }
    mBounds.set(paramRect);
  }
  
  public void setTo(WindowConfiguration paramWindowConfiguration)
  {
    setBounds(mBounds);
    setAppBounds(mAppBounds);
    setWindowingMode(mWindowingMode);
    setActivityType(mActivityType);
  }
  
  public void setToDefaults()
  {
    setAppBounds(null);
    setBounds(null);
    setWindowingMode(0);
    setActivityType(0);
  }
  
  public void setWindowingMode(@WindowingMode int paramInt)
  {
    mWindowingMode = paramInt;
  }
  
  public boolean supportSplitScreenWindowingMode()
  {
    return supportSplitScreenWindowingMode(mActivityType);
  }
  
  public boolean tasksAreFloating()
  {
    return isFloating(mWindowingMode);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ mBounds=");
    localStringBuilder.append(mBounds);
    localStringBuilder.append(" mAppBounds=");
    localStringBuilder.append(mAppBounds);
    localStringBuilder.append(" mWindowingMode=");
    localStringBuilder.append(windowingModeToString(mWindowingMode));
    localStringBuilder.append(" mActivityType=");
    localStringBuilder.append(activityTypeToString(mActivityType));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void unset()
  {
    setToDefaults();
  }
  
  @WindowConfig
  public int updateFrom(WindowConfiguration paramWindowConfiguration)
  {
    int i = 0;
    int j = i;
    if (!mBounds.isEmpty())
    {
      j = i;
      if (!mBounds.equals(mBounds))
      {
        j = 0x0 | 0x1;
        setBounds(mBounds);
      }
    }
    i = j;
    if (mAppBounds != null)
    {
      i = j;
      if (!mAppBounds.equals(mAppBounds))
      {
        i = j | 0x2;
        setAppBounds(mAppBounds);
      }
    }
    j = i;
    if (mWindowingMode != 0)
    {
      j = i;
      if (mWindowingMode != mWindowingMode)
      {
        j = i | 0x4;
        setWindowingMode(mWindowingMode);
      }
    }
    i = j;
    if (mActivityType != 0)
    {
      i = j;
      if (mActivityType != mActivityType)
      {
        i = j | 0x8;
        setActivityType(mActivityType);
      }
    }
    return i;
  }
  
  public boolean useWindowFrameForBackdrop()
  {
    boolean bool;
    if ((mWindowingMode != 5) && (mWindowingMode != 2)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean windowsAreScaleable()
  {
    boolean bool;
    if (mWindowingMode == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mBounds, paramInt);
    paramParcel.writeParcelable(mAppBounds, paramInt);
    paramParcel.writeInt(mWindowingMode);
    paramParcel.writeInt(mActivityType);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (mAppBounds != null) {
      mAppBounds.writeToProto(paramProtoOutputStream, 1146756268033L);
    }
    paramProtoOutputStream.write(1120986464258L, mWindowingMode);
    paramProtoOutputStream.write(1120986464259L, mActivityType);
    paramProtoOutputStream.end(paramLong);
  }
  
  public static @interface ActivityType {}
  
  public static @interface WindowConfig {}
  
  public static @interface WindowingMode {}
}
