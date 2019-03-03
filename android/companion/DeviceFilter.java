package android.companion;

import android.os.Parcelable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract interface DeviceFilter<D extends Parcelable>
  extends Parcelable
{
  public static final int MEDIUM_TYPE_BLUETOOTH = 0;
  public static final int MEDIUM_TYPE_BLUETOOTH_LE = 1;
  public static final int MEDIUM_TYPE_WIFI = 2;
  
  public static <D extends Parcelable> boolean matches(DeviceFilter<D> paramDeviceFilter, D paramD)
  {
    boolean bool;
    if ((paramDeviceFilter != null) && (!paramDeviceFilter.matches(paramD))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public abstract String getDeviceDisplayName(D paramD);
  
  public abstract int getMediumType();
  
  public abstract boolean matches(D paramD);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MediumType {}
}
