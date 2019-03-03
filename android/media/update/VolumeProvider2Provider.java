package android.media.update;

public abstract interface VolumeProvider2Provider
{
  public abstract int getControlType_impl();
  
  public abstract int getCurrentVolume_impl();
  
  public abstract int getMaxVolume_impl();
  
  public abstract void setCurrentVolume_impl(int paramInt);
}
