package android.view;

import android.app.WindowConfiguration;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build.FEATURES;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.proto.ProtoOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public final class DisplayInfo
  implements Parcelable
{
  public static final Parcelable.Creator<DisplayInfo> CREATOR = new Parcelable.Creator()
  {
    public DisplayInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DisplayInfo(paramAnonymousParcel, null);
    }
    
    public DisplayInfo[] newArray(int paramAnonymousInt)
    {
      return new DisplayInfo[paramAnonymousInt];
    }
  };
  public String address;
  public int appHeight;
  public long appVsyncOffsetNanos;
  public int appWidth;
  public int colorMode;
  public int defaultModeId;
  public DisplayCutout displayCutout;
  public int flags;
  public Display.HdrCapabilities hdrCapabilities;
  public int largestNominalAppHeight;
  public int largestNominalAppWidth;
  public int layerStack;
  public int logicalDensityDpi;
  public int logicalHeight;
  public int logicalWidth;
  public int modeId;
  public String name;
  public int overscanBottom;
  public int overscanLeft;
  public int overscanRight;
  public int overscanTop;
  public String ownerPackageName;
  public int ownerUid;
  public float physicalXDpi;
  public float physicalYDpi;
  public long presentationDeadlineNanos;
  public int removeMode = 0;
  public int rotation;
  public int smallestNominalAppHeight;
  public int smallestNominalAppWidth;
  public int state;
  public int[] supportedColorModes = { 0 };
  public Display.Mode[] supportedModes = Display.Mode.EMPTY_ARRAY;
  public int type;
  public String uniqueId;
  
  public DisplayInfo() {}
  
  private DisplayInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public DisplayInfo(DisplayInfo paramDisplayInfo)
  {
    copyFrom(paramDisplayInfo);
  }
  
  private Display.Mode findMode(int paramInt)
  {
    for (int i = 0; i < supportedModes.length; i++) {
      if (supportedModes[i].getModeId() == paramInt) {
        return supportedModes[i];
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unable to locate mode ");
    localStringBuilder.append(paramInt);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private static String flagsToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramInt & 0x2) != 0) {
      localStringBuilder.append(", FLAG_SECURE");
    }
    if ((paramInt & 0x1) != 0) {
      localStringBuilder.append(", FLAG_SUPPORTS_PROTECTED_BUFFERS");
    }
    if ((paramInt & 0x4) != 0) {
      localStringBuilder.append(", FLAG_PRIVATE");
    }
    if ((paramInt & 0x8) != 0) {
      localStringBuilder.append(", FLAG_PRESENTATION");
    }
    if ((0x40000000 & paramInt) != 0) {
      localStringBuilder.append(", FLAG_SCALING_DISABLED");
    }
    if ((paramInt & 0x10) != 0) {
      localStringBuilder.append(", FLAG_ROUND");
    }
    return localStringBuilder.toString();
  }
  
  private void getMetricsWithSize(DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo, Configuration paramConfiguration, int paramInt1, int paramInt2)
  {
    int i = logicalDensityDpi;
    noncompatDensityDpi = i;
    densityDpi = i;
    float f = logicalDensityDpi * 0.00625F;
    noncompatDensity = f;
    density = f;
    f = density;
    noncompatScaledDensity = f;
    scaledDensity = f;
    f = physicalXDpi;
    noncompatXdpi = f;
    xdpi = f;
    f = physicalYDpi;
    noncompatYdpi = f;
    ydpi = f;
    if (paramConfiguration != null) {
      paramConfiguration = windowConfiguration.getAppBounds();
    } else {
      paramConfiguration = null;
    }
    if (paramConfiguration != null) {
      paramInt1 = paramConfiguration.width();
    }
    if (paramConfiguration != null) {
      paramInt2 = paramConfiguration.height();
    }
    widthPixels = paramInt1;
    noncompatWidthPixels = paramInt1;
    heightPixels = paramInt2;
    noncompatHeightPixels = paramInt2;
    if (!paramCompatibilityInfo.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
      paramCompatibilityInfo.applyToDisplayMetrics(paramDisplayMetrics);
    }
  }
  
  public void copyFrom(DisplayInfo paramDisplayInfo)
  {
    layerStack = layerStack;
    flags = flags;
    type = type;
    address = address;
    name = name;
    uniqueId = uniqueId;
    appWidth = appWidth;
    appHeight = appHeight;
    smallestNominalAppWidth = smallestNominalAppWidth;
    smallestNominalAppHeight = smallestNominalAppHeight;
    largestNominalAppWidth = largestNominalAppWidth;
    largestNominalAppHeight = largestNominalAppHeight;
    logicalWidth = logicalWidth;
    logicalHeight = logicalHeight;
    overscanLeft = overscanLeft;
    overscanTop = overscanTop;
    overscanRight = overscanRight;
    overscanBottom = overscanBottom;
    displayCutout = displayCutout;
    rotation = rotation;
    modeId = modeId;
    defaultModeId = defaultModeId;
    supportedModes = ((Display.Mode[])Arrays.copyOf(supportedModes, supportedModes.length));
    colorMode = colorMode;
    supportedColorModes = Arrays.copyOf(supportedColorModes, supportedColorModes.length);
    hdrCapabilities = hdrCapabilities;
    logicalDensityDpi = logicalDensityDpi;
    physicalXDpi = physicalXDpi;
    physicalYDpi = physicalYDpi;
    appVsyncOffsetNanos = appVsyncOffsetNanos;
    presentationDeadlineNanos = presentationDeadlineNanos;
    state = state;
    ownerUid = ownerUid;
    ownerPackageName = ownerPackageName;
    removeMode = removeMode;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(DisplayInfo paramDisplayInfo)
  {
    boolean bool;
    if ((paramDisplayInfo != null) && (layerStack == layerStack) && (flags == flags) && (type == type) && (Objects.equals(address, address)) && (Objects.equals(uniqueId, uniqueId)) && (appWidth == appWidth) && (appHeight == appHeight) && (smallestNominalAppWidth == smallestNominalAppWidth) && (smallestNominalAppHeight == smallestNominalAppHeight) && (largestNominalAppWidth == largestNominalAppWidth) && (largestNominalAppHeight == largestNominalAppHeight) && (logicalWidth == logicalWidth) && (logicalHeight == logicalHeight) && (overscanLeft == overscanLeft) && (overscanTop == overscanTop) && (overscanRight == overscanRight) && (overscanBottom == overscanBottom) && (Objects.equals(displayCutout, displayCutout)) && (rotation == rotation) && (modeId == modeId) && (defaultModeId == defaultModeId) && (colorMode == colorMode) && (Arrays.equals(supportedColorModes, supportedColorModes)) && (Objects.equals(hdrCapabilities, hdrCapabilities)) && (logicalDensityDpi == logicalDensityDpi) && (physicalXDpi == physicalXDpi) && (physicalYDpi == physicalYDpi) && (appVsyncOffsetNanos == appVsyncOffsetNanos) && (presentationDeadlineNanos == presentationDeadlineNanos) && (state == state) && (ownerUid == ownerUid) && (Objects.equals(ownerPackageName, ownerPackageName)) && (removeMode == removeMode)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof DisplayInfo)) && (equals((DisplayInfo)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int findDefaultModeByRefreshRate(float paramFloat)
  {
    Display.Mode[] arrayOfMode = supportedModes;
    Display.Mode localMode = getDefaultMode();
    for (int i = 0; i < arrayOfMode.length; i++) {
      if (arrayOfMode[i].matches(localMode.getPhysicalWidth(), localMode.getPhysicalHeight(), paramFloat)) {
        return arrayOfMode[i].getModeId();
      }
    }
    return 0;
  }
  
  public void getAppMetrics(DisplayMetrics paramDisplayMetrics)
  {
    getAppMetrics(paramDisplayMetrics, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, null);
  }
  
  public void getAppMetrics(DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo, Configuration paramConfiguration)
  {
    getMetricsWithSize(paramDisplayMetrics, paramCompatibilityInfo, paramConfiguration, appWidth, appHeight);
  }
  
  public void getAppMetrics(DisplayMetrics paramDisplayMetrics, DisplayAdjustments paramDisplayAdjustments)
  {
    getMetricsWithSize(paramDisplayMetrics, paramDisplayAdjustments.getCompatibilityInfo(), paramDisplayAdjustments.getConfiguration(), appWidth, appHeight);
  }
  
  public Display.Mode getDefaultMode()
  {
    return findMode(defaultModeId);
  }
  
  public float[] getDefaultRefreshRates()
  {
    Object localObject1 = supportedModes;
    Object localObject2 = new ArraySet();
    Display.Mode localMode = getDefaultMode();
    for (int i = 0; i < localObject1.length; i++)
    {
      Object localObject3 = localObject1[i];
      if ((localObject3.getPhysicalWidth() == localMode.getPhysicalWidth()) && (localObject3.getPhysicalHeight() == localMode.getPhysicalHeight())) {
        ((ArraySet)localObject2).add(Float.valueOf(localObject3.getRefreshRate()));
      }
    }
    localObject1 = new float[((ArraySet)localObject2).size()];
    i = 0;
    localObject2 = ((ArraySet)localObject2).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1[i] = ((Float)((Iterator)localObject2).next()).floatValue();
      i++;
    }
    return localObject1;
  }
  
  public void getLogicalMetrics(DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo, Configuration paramConfiguration)
  {
    getMetricsWithSize(paramDisplayMetrics, paramCompatibilityInfo, paramConfiguration, logicalWidth, logicalHeight);
  }
  
  public Display.Mode getMode()
  {
    return findMode(modeId);
  }
  
  public int getNaturalHeight()
  {
    int i;
    if ((rotation != 0) && (rotation != 2)) {
      i = logicalWidth;
    } else {
      i = logicalHeight;
    }
    return i;
  }
  
  public int getNaturalWidth()
  {
    int i;
    if ((rotation != 0) && (rotation != 2)) {
      i = logicalHeight;
    } else {
      i = logicalWidth;
    }
    return i;
  }
  
  public boolean hasAccess(int paramInt)
  {
    return Display.hasAccess(paramInt, flags, ownerUid);
  }
  
  public int hashCode()
  {
    return 0;
  }
  
  public boolean isHdr()
  {
    int[] arrayOfInt;
    if (hdrCapabilities != null) {
      arrayOfInt = hdrCapabilities.getSupportedHdrTypes();
    } else {
      arrayOfInt = null;
    }
    boolean bool;
    if ((arrayOfInt != null) && (arrayOfInt.length > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWideColorGamut()
  {
    int i = Display.splendidMode;
    if ((Build.FEATURES.ENABLE_SPLENDID_WIDECOLORGAMUT) && (i != -1))
    {
      if ((i == 1) || (i == 3) || (i == 6)) {
        return true;
      }
    }
    else
    {
      int[] arrayOfInt = supportedColorModes;
      int j = arrayOfInt.length;
      i = 0;
      while (i < j)
      {
        int k = arrayOfInt[i];
        if ((k != 6) && (k <= 7)) {
          i++;
        } else {
          return true;
        }
      }
    }
    return false;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    layerStack = paramParcel.readInt();
    flags = paramParcel.readInt();
    type = paramParcel.readInt();
    address = paramParcel.readString();
    name = paramParcel.readString();
    appWidth = paramParcel.readInt();
    appHeight = paramParcel.readInt();
    smallestNominalAppWidth = paramParcel.readInt();
    smallestNominalAppHeight = paramParcel.readInt();
    largestNominalAppWidth = paramParcel.readInt();
    largestNominalAppHeight = paramParcel.readInt();
    logicalWidth = paramParcel.readInt();
    logicalHeight = paramParcel.readInt();
    overscanLeft = paramParcel.readInt();
    overscanTop = paramParcel.readInt();
    overscanRight = paramParcel.readInt();
    overscanBottom = paramParcel.readInt();
    displayCutout = DisplayCutout.ParcelableWrapper.readCutoutFromParcel(paramParcel);
    rotation = paramParcel.readInt();
    modeId = paramParcel.readInt();
    defaultModeId = paramParcel.readInt();
    int i = paramParcel.readInt();
    supportedModes = new Display.Mode[i];
    int j = 0;
    for (int k = 0; k < i; k++) {
      supportedModes[k] = ((Display.Mode)Display.Mode.CREATOR.createFromParcel(paramParcel));
    }
    colorMode = paramParcel.readInt();
    i = paramParcel.readInt();
    supportedColorModes = new int[i];
    for (k = j; k < i; k++) {
      supportedColorModes[k] = paramParcel.readInt();
    }
    hdrCapabilities = ((Display.HdrCapabilities)paramParcel.readParcelable(null));
    logicalDensityDpi = paramParcel.readInt();
    physicalXDpi = paramParcel.readFloat();
    physicalYDpi = paramParcel.readFloat();
    appVsyncOffsetNanos = paramParcel.readLong();
    presentationDeadlineNanos = paramParcel.readLong();
    state = paramParcel.readInt();
    ownerUid = paramParcel.readInt();
    ownerPackageName = paramParcel.readString();
    uniqueId = paramParcel.readString();
    removeMode = paramParcel.readInt();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DisplayInfo{\"");
    localStringBuilder.append(name);
    localStringBuilder.append("\", uniqueId \"");
    localStringBuilder.append(uniqueId);
    localStringBuilder.append("\", app ");
    localStringBuilder.append(appWidth);
    localStringBuilder.append(" x ");
    localStringBuilder.append(appHeight);
    localStringBuilder.append(", real ");
    localStringBuilder.append(logicalWidth);
    localStringBuilder.append(" x ");
    localStringBuilder.append(logicalHeight);
    if ((overscanLeft != 0) || (overscanTop != 0) || (overscanRight != 0) || (overscanBottom != 0))
    {
      localStringBuilder.append(", overscan (");
      localStringBuilder.append(overscanLeft);
      localStringBuilder.append(",");
      localStringBuilder.append(overscanTop);
      localStringBuilder.append(",");
      localStringBuilder.append(overscanRight);
      localStringBuilder.append(",");
      localStringBuilder.append(overscanBottom);
      localStringBuilder.append(")");
    }
    localStringBuilder.append(", largest app ");
    localStringBuilder.append(largestNominalAppWidth);
    localStringBuilder.append(" x ");
    localStringBuilder.append(largestNominalAppHeight);
    localStringBuilder.append(", smallest app ");
    localStringBuilder.append(smallestNominalAppWidth);
    localStringBuilder.append(" x ");
    localStringBuilder.append(smallestNominalAppHeight);
    localStringBuilder.append(", mode ");
    localStringBuilder.append(modeId);
    localStringBuilder.append(", defaultMode ");
    localStringBuilder.append(defaultModeId);
    localStringBuilder.append(", modes ");
    localStringBuilder.append(Arrays.toString(supportedModes));
    localStringBuilder.append(", colorMode ");
    localStringBuilder.append(colorMode);
    localStringBuilder.append(", supportedColorModes ");
    localStringBuilder.append(Arrays.toString(supportedColorModes));
    localStringBuilder.append(", hdrCapabilities ");
    localStringBuilder.append(hdrCapabilities);
    localStringBuilder.append(", rotation ");
    localStringBuilder.append(rotation);
    localStringBuilder.append(", density ");
    localStringBuilder.append(logicalDensityDpi);
    localStringBuilder.append(" (");
    localStringBuilder.append(physicalXDpi);
    localStringBuilder.append(" x ");
    localStringBuilder.append(physicalYDpi);
    localStringBuilder.append(") dpi, layerStack ");
    localStringBuilder.append(layerStack);
    localStringBuilder.append(", appVsyncOff ");
    localStringBuilder.append(appVsyncOffsetNanos);
    localStringBuilder.append(", presDeadline ");
    localStringBuilder.append(presentationDeadlineNanos);
    localStringBuilder.append(", type ");
    localStringBuilder.append(Display.typeToString(type));
    if (address != null)
    {
      localStringBuilder.append(", address ");
      localStringBuilder.append(address);
    }
    localStringBuilder.append(", state ");
    localStringBuilder.append(Display.stateToString(state));
    if ((ownerUid != 0) || (ownerPackageName != null))
    {
      localStringBuilder.append(", owner ");
      localStringBuilder.append(ownerPackageName);
      localStringBuilder.append(" (uid ");
      localStringBuilder.append(ownerUid);
      localStringBuilder.append(")");
    }
    localStringBuilder.append(flagsToString(flags));
    localStringBuilder.append(", removeMode ");
    localStringBuilder.append(removeMode);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(layerStack);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(type);
    paramParcel.writeString(address);
    paramParcel.writeString(name);
    paramParcel.writeInt(appWidth);
    paramParcel.writeInt(appHeight);
    paramParcel.writeInt(smallestNominalAppWidth);
    paramParcel.writeInt(smallestNominalAppHeight);
    paramParcel.writeInt(largestNominalAppWidth);
    paramParcel.writeInt(largestNominalAppHeight);
    paramParcel.writeInt(logicalWidth);
    paramParcel.writeInt(logicalHeight);
    paramParcel.writeInt(overscanLeft);
    paramParcel.writeInt(overscanTop);
    paramParcel.writeInt(overscanRight);
    paramParcel.writeInt(overscanBottom);
    DisplayCutout.ParcelableWrapper.writeCutoutToParcel(displayCutout, paramParcel, paramInt);
    paramParcel.writeInt(rotation);
    paramParcel.writeInt(modeId);
    paramParcel.writeInt(defaultModeId);
    paramParcel.writeInt(supportedModes.length);
    int i = 0;
    for (int j = 0; j < supportedModes.length; j++) {
      supportedModes[j].writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeInt(colorMode);
    paramParcel.writeInt(supportedColorModes.length);
    for (j = i; j < supportedColorModes.length; j++) {
      paramParcel.writeInt(supportedColorModes[j]);
    }
    paramParcel.writeParcelable(hdrCapabilities, paramInt);
    paramParcel.writeInt(logicalDensityDpi);
    paramParcel.writeFloat(physicalXDpi);
    paramParcel.writeFloat(physicalYDpi);
    paramParcel.writeLong(appVsyncOffsetNanos);
    paramParcel.writeLong(presentationDeadlineNanos);
    paramParcel.writeInt(state);
    paramParcel.writeInt(ownerUid);
    paramParcel.writeString(ownerPackageName);
    paramParcel.writeString(uniqueId);
    paramParcel.writeInt(removeMode);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, logicalWidth);
    paramProtoOutputStream.write(1120986464258L, logicalHeight);
    paramProtoOutputStream.write(1120986464259L, appWidth);
    paramProtoOutputStream.write(1120986464260L, appHeight);
    paramProtoOutputStream.write(1138166333445L, name);
    paramProtoOutputStream.end(paramLong);
  }
}
