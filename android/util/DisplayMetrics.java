package android.util;

import android.os.SystemProperties;

public class DisplayMetrics
{
  public static final int DENSITY_260 = 260;
  public static final int DENSITY_280 = 280;
  public static final int DENSITY_300 = 300;
  public static final int DENSITY_340 = 340;
  public static final int DENSITY_360 = 360;
  public static final int DENSITY_400 = 400;
  public static final int DENSITY_420 = 420;
  public static final int DENSITY_440 = 440;
  public static final int DENSITY_560 = 560;
  public static final int DENSITY_DEFAULT = 160;
  public static final float DENSITY_DEFAULT_SCALE = 0.00625F;
  @Deprecated
  public static int DENSITY_DEVICE = ;
  public static final int DENSITY_DEVICE_STABLE = getDeviceDensity();
  public static final int DENSITY_HIGH = 240;
  public static final int DENSITY_LOW = 120;
  public static final int DENSITY_MEDIUM = 160;
  public static final int DENSITY_TV = 213;
  public static final int DENSITY_XHIGH = 320;
  public static final int DENSITY_XXHIGH = 480;
  public static final int DENSITY_XXXHIGH = 640;
  public float density;
  public int densityDpi;
  public int heightPixels;
  public float noncompatDensity;
  public int noncompatDensityDpi;
  public int noncompatHeightPixels;
  public float noncompatScaledDensity;
  public int noncompatWidthPixels;
  public float noncompatXdpi;
  public float noncompatYdpi;
  public float scaledDensity;
  public int widthPixels;
  public float xdpi;
  public float ydpi;
  
  public DisplayMetrics() {}
  
  private static int getDeviceDensity()
  {
    return SystemProperties.getInt("qemu.sf.lcd_density", SystemProperties.getInt("ro.sf.lcd_density", 160));
  }
  
  public boolean equals(DisplayMetrics paramDisplayMetrics)
  {
    boolean bool;
    if ((equalsPhysical(paramDisplayMetrics)) && (scaledDensity == scaledDensity) && (noncompatScaledDensity == noncompatScaledDensity)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof DisplayMetrics)) && (equals((DisplayMetrics)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equalsPhysical(DisplayMetrics paramDisplayMetrics)
  {
    boolean bool;
    if ((paramDisplayMetrics != null) && (widthPixels == widthPixels) && (heightPixels == heightPixels) && (density == density) && (densityDpi == densityDpi) && (xdpi == xdpi) && (ydpi == ydpi) && (noncompatWidthPixels == noncompatWidthPixels) && (noncompatHeightPixels == noncompatHeightPixels) && (noncompatDensity == noncompatDensity) && (noncompatDensityDpi == noncompatDensityDpi) && (noncompatXdpi == noncompatXdpi) && (noncompatYdpi == noncompatYdpi)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return widthPixels * heightPixels * densityDpi;
  }
  
  public void setTo(DisplayMetrics paramDisplayMetrics)
  {
    if (this == paramDisplayMetrics) {
      return;
    }
    widthPixels = widthPixels;
    heightPixels = heightPixels;
    density = density;
    densityDpi = densityDpi;
    scaledDensity = scaledDensity;
    xdpi = xdpi;
    ydpi = ydpi;
    noncompatWidthPixels = noncompatWidthPixels;
    noncompatHeightPixels = noncompatHeightPixels;
    noncompatDensity = noncompatDensity;
    noncompatDensityDpi = noncompatDensityDpi;
    noncompatScaledDensity = noncompatScaledDensity;
    noncompatXdpi = noncompatXdpi;
    noncompatYdpi = noncompatYdpi;
  }
  
  public void setToDefaults()
  {
    widthPixels = 0;
    heightPixels = 0;
    density = (DENSITY_DEVICE / 160.0F);
    densityDpi = DENSITY_DEVICE;
    scaledDensity = density;
    xdpi = DENSITY_DEVICE;
    ydpi = DENSITY_DEVICE;
    noncompatWidthPixels = widthPixels;
    noncompatHeightPixels = heightPixels;
    noncompatDensity = density;
    noncompatDensityDpi = densityDpi;
    noncompatScaledDensity = scaledDensity;
    noncompatXdpi = xdpi;
    noncompatYdpi = ydpi;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DisplayMetrics{density=");
    localStringBuilder.append(density);
    localStringBuilder.append(", width=");
    localStringBuilder.append(widthPixels);
    localStringBuilder.append(", height=");
    localStringBuilder.append(heightPixels);
    localStringBuilder.append(", scaledDensity=");
    localStringBuilder.append(scaledDensity);
    localStringBuilder.append(", xdpi=");
    localStringBuilder.append(xdpi);
    localStringBuilder.append(", ydpi=");
    localStringBuilder.append(ydpi);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
