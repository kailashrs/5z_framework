package android.view;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Build.FEATURES;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class Display
{
  private static final int CACHED_APP_SIZE_DURATION_MILLIS = 20;
  public static final int COLOR_MODE_ADOBE_RGB = 8;
  public static final int COLOR_MODE_BT601_525 = 3;
  public static final int COLOR_MODE_BT601_525_UNADJUSTED = 4;
  public static final int COLOR_MODE_BT601_625 = 1;
  public static final int COLOR_MODE_BT601_625_UNADJUSTED = 2;
  public static final int COLOR_MODE_BT709 = 5;
  public static final int COLOR_MODE_DCI_P3 = 6;
  public static final int COLOR_MODE_DEFAULT = 0;
  public static final int COLOR_MODE_DISPLAY_P3 = 9;
  public static final int COLOR_MODE_INVALID = -1;
  public static final int COLOR_MODE_SRGB = 7;
  private static final boolean DEBUG = false;
  public static final int DEFAULT_DISPLAY = 0;
  public static final int FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD = 32;
  public static final int FLAG_PRESENTATION = 8;
  public static final int FLAG_PRIVATE = 4;
  public static final int FLAG_ROUND = 16;
  public static final int FLAG_SCALING_DISABLED = 1073741824;
  public static final int FLAG_SECURE = 2;
  public static final int FLAG_SUPPORTS_PROTECTED_BUFFERS = 1;
  public static final int INVALID_DISPLAY = -1;
  public static final int REMOVE_MODE_DESTROY_CONTENT = 1;
  public static final int REMOVE_MODE_MOVE_CONTENT_TO_PRIMARY = 0;
  public static final int STATE_DOZE = 3;
  public static final int STATE_DOZE_SUSPEND = 4;
  public static final int STATE_OFF = 1;
  public static final int STATE_ON = 2;
  public static final int STATE_ON_SUSPEND = 6;
  public static final int STATE_UNKNOWN = 0;
  public static final int STATE_VR = 5;
  private static final String TAG = "Display";
  public static final int TYPE_BUILT_IN = 1;
  public static final int TYPE_HDMI = 2;
  public static final int TYPE_OVERLAY = 4;
  public static final int TYPE_UNKNOWN = 0;
  public static final int TYPE_VIRTUAL = 5;
  public static final int TYPE_WIFI = 3;
  public static int splendidMode = -1;
  private final String mAddress;
  private int mCachedAppHeightCompat;
  private int mCachedAppWidthCompat;
  private DisplayAdjustments mDisplayAdjustments;
  private final int mDisplayId;
  private DisplayInfo mDisplayInfo;
  private final int mFlags;
  private final DisplayManagerGlobal mGlobal;
  private boolean mIsValid;
  private long mLastCachedAppSizeUpdate;
  private final int mLayerStack;
  private final String mOwnerPackageName;
  private final int mOwnerUid;
  private final Resources mResources;
  private final DisplayMetrics mTempMetrics = new DisplayMetrics();
  private final int mType;
  
  public Display(DisplayManagerGlobal paramDisplayManagerGlobal, int paramInt, DisplayInfo paramDisplayInfo, Resources paramResources)
  {
    this(paramDisplayManagerGlobal, paramInt, paramDisplayInfo, null, paramResources);
  }
  
  public Display(DisplayManagerGlobal paramDisplayManagerGlobal, int paramInt, DisplayInfo paramDisplayInfo, DisplayAdjustments paramDisplayAdjustments)
  {
    this(paramDisplayManagerGlobal, paramInt, paramDisplayInfo, paramDisplayAdjustments, null);
  }
  
  private Display(DisplayManagerGlobal paramDisplayManagerGlobal, int paramInt, DisplayInfo paramDisplayInfo, DisplayAdjustments paramDisplayAdjustments, Resources paramResources)
  {
    mGlobal = paramDisplayManagerGlobal;
    mDisplayId = paramInt;
    mDisplayInfo = paramDisplayInfo;
    mResources = paramResources;
    if (mResources != null) {
      paramDisplayManagerGlobal = new DisplayAdjustments(mResources.getConfiguration());
    } else if (paramDisplayAdjustments != null) {
      paramDisplayManagerGlobal = new DisplayAdjustments(paramDisplayAdjustments);
    } else {
      paramDisplayManagerGlobal = null;
    }
    mDisplayAdjustments = paramDisplayManagerGlobal;
    mIsValid = true;
    mLayerStack = layerStack;
    mFlags = flags;
    mType = type;
    mAddress = address;
    mOwnerUid = ownerUid;
    mOwnerPackageName = ownerPackageName;
  }
  
  private void adjustDisplayMetrics(DisplayMetrics paramDisplayMetrics)
  {
    if ((widthPixels > heightPixels) && (mDisplayInfo != null) && (mDisplayInfo.displayCutout != null) && (widthPixels == mDisplayInfo.logicalWidth))
    {
      boolean bool1 = false;
      int i = 0;
      Object localObject = ActivityThread.currentActivityThread();
      int j;
      if (localObject != null)
      {
        j = ((ActivityThread)localObject).getResumedActivityCutoutMode();
        if ((j >= 0) && (j != 1)) {
          bool1 = ((ActivityThread)localObject).isResumedActivityFullscreen();
        } else {
          return;
        }
      }
      try
      {
        localObject = ActivityThread.getPackageManager().getPackageInfo(ActivityThread.currentPackageName(), 16384, UserHandle.myUserId());
        if ((localObject != null) && (applicationInfo != null) && (!applicationInfo.fillNotchRegion))
        {
          j = i;
          if (reqFeatures != null)
          {
            localObject = reqFeatures;
            int k = localObject.length;
            for (int m = 0;; m++)
            {
              j = i;
              if (m >= k) {
                break;
              }
              boolean bool2 = "android.software.vr.mode".equals(name);
              if (bool2)
              {
                j = 1;
                break;
              }
            }
          }
          if ((!bool1) && (j == 0)) {
            return;
          }
          Rect localRect1 = new Rect(0, 0, widthPixels, heightPixels);
          localObject = new Rect(0, 0, noncompatWidthPixels, noncompatHeightPixels);
          Rect localRect2 = mDisplayInfo.displayCutout.getSafeInsets();
          localRect1.inset(localRect2);
          widthPixels = localRect1.width();
          heightPixels = localRect1.height();
          ((Rect)localObject).inset(localRect2);
          noncompatWidthPixels = ((Rect)localObject).width();
          return;
        }
        return;
      }
      catch (Exception paramDisplayMetrics) {}
    }
  }
  
  private void adjustDisplaySize(Point paramPoint)
  {
    Rect localRect = new Rect(0, 0, x, y);
    adjustDisplaySize(localRect);
    x = localRect.width();
    y = localRect.height();
  }
  
  private void adjustDisplaySize(Rect paramRect)
  {
    if ((paramRect.width() > paramRect.height()) && (mDisplayInfo != null) && (mDisplayInfo.displayCutout != null) && (paramRect.width() == mDisplayInfo.logicalWidth))
    {
      boolean bool = false;
      Object localObject = ActivityThread.currentActivityThread();
      if (localObject != null)
      {
        int i = ((ActivityThread)localObject).getResumedActivityCutoutMode();
        if ((i >= 0) && (i != 1)) {
          bool = ((ActivityThread)localObject).isResumedActivityFullscreen();
        } else {
          return;
        }
      }
      if (!bool) {
        return;
      }
      try
      {
        localObject = ActivityThread.getPackageManager().getPackageInfo(ActivityThread.currentPackageName(), 16384, UserHandle.myUserId());
        if ((localObject != null) && (applicationInfo != null))
        {
          bool = applicationInfo.fillNotchRegion;
          if (!bool)
          {
            paramRect.inset(mDisplayInfo.displayCutout.getSafeInsets());
            return;
          }
        }
        return;
      }
      catch (Exception paramRect) {}
    }
  }
  
  public static boolean hasAccess(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    if (((paramInt2 & 0x4) != 0) && (paramInt1 != paramInt3) && (paramInt1 != 1000) && (paramInt1 != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isDozeState(int paramInt)
  {
    boolean bool;
    if ((paramInt != 3) && (paramInt != 4)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isSuspendedState(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1)
    {
      bool2 = bool1;
      if (paramInt != 4) {
        if (paramInt == 6) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public static void setSplendidMode(int paramInt)
  {
    splendidMode = paramInt;
  }
  
  public static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 6: 
      return "ON_SUSPEND";
    case 5: 
      return "VR";
    case 4: 
      return "DOZE_SUSPEND";
    case 3: 
      return "DOZE";
    case 2: 
      return "ON";
    case 1: 
      return "OFF";
    }
    return "UNKNOWN";
  }
  
  public static String typeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 5: 
      return "VIRTUAL";
    case 4: 
      return "OVERLAY";
    case 3: 
      return "WIFI";
    case 2: 
      return "HDMI";
    case 1: 
      return "BUILT_IN";
    }
    return "UNKNOWN";
  }
  
  private void updateCachedAppSizeIfNeededLocked()
  {
    long l = SystemClock.uptimeMillis();
    if (l > mLastCachedAppSizeUpdate + 20L)
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getAppMetrics(mTempMetrics, getDisplayAdjustments());
      mCachedAppWidthCompat = mTempMetrics.widthPixels;
      mCachedAppHeightCompat = mTempMetrics.heightPixels;
      mLastCachedAppSizeUpdate = l;
    }
  }
  
  private void updateDisplayInfoLocked()
  {
    DisplayInfo localDisplayInfo = mGlobal.getDisplayInfo(mDisplayId);
    if (localDisplayInfo == null)
    {
      if (mIsValid) {
        mIsValid = false;
      }
    }
    else
    {
      mDisplayInfo = localDisplayInfo;
      if (!mIsValid) {
        mIsValid = true;
      }
    }
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public long getAppVsyncOffsetNanos()
  {
    try
    {
      updateDisplayInfoLocked();
      long l = mDisplayInfo.appVsyncOffsetNanos;
      return l;
    }
    finally {}
  }
  
  public int getColorMode()
  {
    try
    {
      updateDisplayInfoLocked();
      int i = mDisplayInfo.colorMode;
      return i;
    }
    finally {}
  }
  
  public void getCurrentSizeRange(Point paramPoint1, Point paramPoint2)
  {
    try
    {
      updateDisplayInfoLocked();
      x = mDisplayInfo.smallestNominalAppWidth;
      y = mDisplayInfo.smallestNominalAppHeight;
      x = mDisplayInfo.largestNominalAppWidth;
      y = mDisplayInfo.largestNominalAppHeight;
      return;
    }
    finally {}
  }
  
  public DisplayAdjustments getDisplayAdjustments()
  {
    if (mResources != null)
    {
      DisplayAdjustments localDisplayAdjustments = mResources.getDisplayAdjustments();
      if (!mDisplayAdjustments.equals(localDisplayAdjustments)) {
        mDisplayAdjustments = new DisplayAdjustments(localDisplayAdjustments);
      }
    }
    return mDisplayAdjustments;
  }
  
  public int getDisplayId()
  {
    return mDisplayId;
  }
  
  public boolean getDisplayInfo(DisplayInfo paramDisplayInfo)
  {
    try
    {
      updateDisplayInfoLocked();
      paramDisplayInfo.copyFrom(mDisplayInfo);
      boolean bool = mIsValid;
      return bool;
    }
    finally {}
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public HdrCapabilities getHdrCapabilities()
  {
    try
    {
      updateDisplayInfoLocked();
      HdrCapabilities localHdrCapabilities = mDisplayInfo.hdrCapabilities;
      return localHdrCapabilities;
    }
    finally {}
  }
  
  @Deprecated
  public int getHeight()
  {
    try
    {
      updateCachedAppSizeIfNeededLocked();
      int i = mCachedAppHeightCompat;
      return i;
    }
    finally {}
  }
  
  public int getLayerStack()
  {
    return mLayerStack;
  }
  
  public int getMaximumSizeDimension()
  {
    try
    {
      updateDisplayInfoLocked();
      int i = Math.max(mDisplayInfo.logicalWidth, mDisplayInfo.logicalHeight);
      return i;
    }
    finally {}
  }
  
  public void getMetrics(DisplayMetrics paramDisplayMetrics)
  {
    try
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getAppMetrics(paramDisplayMetrics, getDisplayAdjustments());
      if ((Build.FEATURES.ENABLE_NOTCH_UI) && (mDisplayId == 0)) {
        adjustDisplayMetrics(paramDisplayMetrics);
      }
      return;
    }
    finally {}
  }
  
  public Mode getMode()
  {
    try
    {
      updateDisplayInfoLocked();
      Mode localMode = mDisplayInfo.getMode();
      return localMode;
    }
    finally {}
  }
  
  public String getName()
  {
    try
    {
      updateDisplayInfoLocked();
      String str = mDisplayInfo.name;
      return str;
    }
    finally {}
  }
  
  @Deprecated
  public int getOrientation()
  {
    return getRotation();
  }
  
  public void getOverscanInsets(Rect paramRect)
  {
    try
    {
      updateDisplayInfoLocked();
      paramRect.set(mDisplayInfo.overscanLeft, mDisplayInfo.overscanTop, mDisplayInfo.overscanRight, mDisplayInfo.overscanBottom);
      return;
    }
    finally {}
  }
  
  public String getOwnerPackageName()
  {
    return mOwnerPackageName;
  }
  
  public int getOwnerUid()
  {
    return mOwnerUid;
  }
  
  @Deprecated
  public int getPixelFormat()
  {
    return 1;
  }
  
  public long getPresentationDeadlineNanos()
  {
    try
    {
      updateDisplayInfoLocked();
      long l = mDisplayInfo.presentationDeadlineNanos;
      return l;
    }
    finally {}
  }
  
  public void getRealMetrics(DisplayMetrics paramDisplayMetrics)
  {
    try
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getLogicalMetrics(paramDisplayMetrics, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, null);
      if ((Build.FEATURES.ENABLE_NOTCH_UI) && (mDisplayId == 0)) {
        adjustDisplayMetrics(paramDisplayMetrics);
      }
      return;
    }
    finally {}
  }
  
  public void getRealSize(Point paramPoint)
  {
    try
    {
      updateDisplayInfoLocked();
      x = mDisplayInfo.logicalWidth;
      y = mDisplayInfo.logicalHeight;
      if ((Build.FEATURES.ENABLE_NOTCH_UI) && (mDisplayId == 0)) {
        adjustDisplaySize(paramPoint);
      }
      return;
    }
    finally {}
  }
  
  public void getRectSize(Rect paramRect)
  {
    try
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getAppMetrics(mTempMetrics, getDisplayAdjustments());
      paramRect.set(0, 0, mTempMetrics.widthPixels, mTempMetrics.heightPixels);
      if ((Build.FEATURES.ENABLE_NOTCH_UI) && (mDisplayId == 0)) {
        adjustDisplaySize(paramRect);
      }
      return;
    }
    finally {}
  }
  
  public float getRefreshRate()
  {
    try
    {
      updateDisplayInfoLocked();
      float f = mDisplayInfo.getMode().getRefreshRate();
      return f;
    }
    finally {}
  }
  
  public int getRemoveMode()
  {
    return mDisplayInfo.removeMode;
  }
  
  public int getRotation()
  {
    try
    {
      updateDisplayInfoLocked();
      int i = mDisplayInfo.rotation;
      return i;
    }
    finally {}
  }
  
  public void getSize(Point paramPoint)
  {
    try
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getAppMetrics(mTempMetrics, getDisplayAdjustments());
      x = mTempMetrics.widthPixels;
      y = mTempMetrics.heightPixels;
      if ((Build.FEATURES.ENABLE_NOTCH_UI) && (mDisplayId == 0)) {
        adjustDisplaySize(paramPoint);
      }
      return;
    }
    finally {}
  }
  
  public int getState()
  {
    try
    {
      updateDisplayInfoLocked();
      int i;
      if (mIsValid) {
        i = mDisplayInfo.state;
      } else {
        i = 0;
      }
      return i;
    }
    finally {}
  }
  
  public int[] getSupportedColorModes()
  {
    try
    {
      updateDisplayInfoLocked();
      int[] arrayOfInt = mDisplayInfo.supportedColorModes;
      arrayOfInt = Arrays.copyOf(arrayOfInt, arrayOfInt.length);
      return arrayOfInt;
    }
    finally {}
  }
  
  public Mode[] getSupportedModes()
  {
    try
    {
      updateDisplayInfoLocked();
      Mode[] arrayOfMode = mDisplayInfo.supportedModes;
      arrayOfMode = (Mode[])Arrays.copyOf(arrayOfMode, arrayOfMode.length);
      return arrayOfMode;
    }
    finally {}
  }
  
  @Deprecated
  public float[] getSupportedRefreshRates()
  {
    try
    {
      updateDisplayInfoLocked();
      float[] arrayOfFloat = mDisplayInfo.getDefaultRefreshRates();
      return arrayOfFloat;
    }
    finally {}
  }
  
  public int getType()
  {
    return mType;
  }
  
  @Deprecated
  public int getWidth()
  {
    try
    {
      updateCachedAppSizeIfNeededLocked();
      int i = mCachedAppWidthCompat;
      return i;
    }
    finally {}
  }
  
  public boolean hasAccess(int paramInt)
  {
    return hasAccess(paramInt, mFlags, mOwnerUid);
  }
  
  public boolean isHdr()
  {
    try
    {
      updateDisplayInfoLocked();
      boolean bool = mDisplayInfo.isHdr();
      return bool;
    }
    finally {}
  }
  
  public boolean isPublicPresentation()
  {
    boolean bool;
    if ((mFlags & 0xC) == 8) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isValid()
  {
    try
    {
      updateDisplayInfoLocked();
      boolean bool = mIsValid;
      return bool;
    }
    finally {}
  }
  
  public boolean isWideColorGamut()
  {
    try
    {
      updateDisplayInfoLocked();
      boolean bool = mDisplayInfo.isWideColorGamut();
      return bool;
    }
    finally {}
  }
  
  public void requestColorMode(int paramInt)
  {
    mGlobal.requestColorMode(mDisplayId, paramInt);
  }
  
  public String toString()
  {
    try
    {
      updateDisplayInfoLocked();
      mDisplayInfo.getAppMetrics(mTempMetrics, getDisplayAdjustments());
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Display id ");
      ((StringBuilder)localObject1).append(mDisplayId);
      ((StringBuilder)localObject1).append(": ");
      ((StringBuilder)localObject1).append(mDisplayInfo);
      ((StringBuilder)localObject1).append(", ");
      ((StringBuilder)localObject1).append(mTempMetrics);
      ((StringBuilder)localObject1).append(", isValid=");
      ((StringBuilder)localObject1).append(mIsValid);
      localObject1 = ((StringBuilder)localObject1).toString();
      return localObject1;
    }
    finally {}
  }
  
  public static final class HdrCapabilities
    implements Parcelable
  {
    public static final Parcelable.Creator<HdrCapabilities> CREATOR = new Parcelable.Creator()
    {
      public Display.HdrCapabilities createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Display.HdrCapabilities(paramAnonymousParcel, null);
      }
      
      public Display.HdrCapabilities[] newArray(int paramAnonymousInt)
      {
        return new Display.HdrCapabilities[paramAnonymousInt];
      }
    };
    public static final int HDR_TYPE_DOLBY_VISION = 1;
    public static final int HDR_TYPE_HDR10 = 2;
    public static final int HDR_TYPE_HLG = 3;
    public static final float INVALID_LUMINANCE = -1.0F;
    private float mMaxAverageLuminance = -1.0F;
    private float mMaxLuminance = -1.0F;
    private float mMinLuminance = -1.0F;
    private int[] mSupportedHdrTypes = new int[0];
    
    public HdrCapabilities() {}
    
    private HdrCapabilities(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public HdrCapabilities(int[] paramArrayOfInt, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      mSupportedHdrTypes = paramArrayOfInt;
      mMaxLuminance = paramFloat1;
      mMaxAverageLuminance = paramFloat2;
      mMinLuminance = paramFloat3;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof HdrCapabilities)) {
        return false;
      }
      paramObject = (HdrCapabilities)paramObject;
      if ((!Arrays.equals(mSupportedHdrTypes, mSupportedHdrTypes)) || (mMaxLuminance != mMaxLuminance) || (mMaxAverageLuminance != mMaxAverageLuminance) || (mMinLuminance != mMinLuminance)) {
        bool = false;
      }
      return bool;
    }
    
    public float getDesiredMaxAverageLuminance()
    {
      return mMaxAverageLuminance;
    }
    
    public float getDesiredMaxLuminance()
    {
      return mMaxLuminance;
    }
    
    public float getDesiredMinLuminance()
    {
      return mMinLuminance;
    }
    
    public int[] getSupportedHdrTypes()
    {
      return mSupportedHdrTypes;
    }
    
    public int hashCode()
    {
      return (((23 * 17 + Arrays.hashCode(mSupportedHdrTypes)) * 17 + Float.floatToIntBits(mMaxLuminance)) * 17 + Float.floatToIntBits(mMaxAverageLuminance)) * 17 + Float.floatToIntBits(mMinLuminance);
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      mSupportedHdrTypes = new int[i];
      for (int j = 0; j < i; j++) {
        mSupportedHdrTypes[j] = paramParcel.readInt();
      }
      mMaxLuminance = paramParcel.readFloat();
      mMaxAverageLuminance = paramParcel.readFloat();
      mMinLuminance = paramParcel.readFloat();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mSupportedHdrTypes.length);
      for (paramInt = 0; paramInt < mSupportedHdrTypes.length; paramInt++) {
        paramParcel.writeInt(mSupportedHdrTypes[paramInt]);
      }
      paramParcel.writeFloat(mMaxLuminance);
      paramParcel.writeFloat(mMaxAverageLuminance);
      paramParcel.writeFloat(mMinLuminance);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface HdrType {}
  }
  
  public static final class Mode
    implements Parcelable
  {
    public static final Parcelable.Creator<Mode> CREATOR = new Parcelable.Creator()
    {
      public Display.Mode createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Display.Mode(paramAnonymousParcel, null);
      }
      
      public Display.Mode[] newArray(int paramAnonymousInt)
      {
        return new Display.Mode[paramAnonymousInt];
      }
    };
    public static final Mode[] EMPTY_ARRAY = new Mode[0];
    private final int mHeight;
    private final int mModeId;
    private final float mRefreshRate;
    private final int mWidth;
    
    public Mode(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
    {
      mModeId = paramInt1;
      mWidth = paramInt2;
      mHeight = paramInt3;
      mRefreshRate = paramFloat;
    }
    
    private Mode(Parcel paramParcel)
    {
      this(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readFloat());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof Mode)) {
        return false;
      }
      paramObject = (Mode)paramObject;
      if ((mModeId != mModeId) || (!matches(mWidth, mHeight, mRefreshRate))) {
        bool = false;
      }
      return bool;
    }
    
    public int getModeId()
    {
      return mModeId;
    }
    
    public int getPhysicalHeight()
    {
      return mHeight;
    }
    
    public int getPhysicalWidth()
    {
      return mWidth;
    }
    
    public float getRefreshRate()
    {
      return mRefreshRate;
    }
    
    public int hashCode()
    {
      return (((1 * 17 + mModeId) * 17 + mWidth) * 17 + mHeight) * 17 + Float.floatToIntBits(mRefreshRate);
    }
    
    public boolean matches(int paramInt1, int paramInt2, float paramFloat)
    {
      boolean bool;
      if ((mWidth == paramInt1) && (mHeight == paramInt2) && (Float.floatToIntBits(mRefreshRate) == Float.floatToIntBits(paramFloat))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("{");
      localStringBuilder.append("id=");
      localStringBuilder.append(mModeId);
      localStringBuilder.append(", width=");
      localStringBuilder.append(mWidth);
      localStringBuilder.append(", height=");
      localStringBuilder.append(mHeight);
      localStringBuilder.append(", fps=");
      localStringBuilder.append(mRefreshRate);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mModeId);
      paramParcel.writeInt(mWidth);
      paramParcel.writeInt(mHeight);
      paramParcel.writeFloat(mRefreshRate);
    }
  }
}
