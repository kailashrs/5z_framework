package android.media;

import android.media.update.ApiLoader;
import android.media.update.StaticProvider;
import android.media.update.VolumeProvider2Provider;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class VolumeProvider2
{
  public static final int VOLUME_CONTROL_ABSOLUTE = 2;
  public static final int VOLUME_CONTROL_FIXED = 0;
  public static final int VOLUME_CONTROL_RELATIVE = 1;
  private final VolumeProvider2Provider mProvider;
  
  public VolumeProvider2(int paramInt1, int paramInt2, int paramInt3)
  {
    mProvider = ApiLoader.getProvider().createVolumeProvider2(this, paramInt1, paramInt2, paramInt3);
  }
  
  public final int getControlType()
  {
    return mProvider.getControlType_impl();
  }
  
  public final int getCurrentVolume()
  {
    return mProvider.getCurrentVolume_impl();
  }
  
  public final int getMaxVolume()
  {
    return mProvider.getMaxVolume_impl();
  }
  
  public VolumeProvider2Provider getProvider()
  {
    return mProvider;
  }
  
  public void onAdjustVolume(int paramInt) {}
  
  public void onSetVolumeTo(int paramInt) {}
  
  public final void setCurrentVolume(int paramInt)
  {
    mProvider.setCurrentVolume_impl(paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ControlType {}
}
