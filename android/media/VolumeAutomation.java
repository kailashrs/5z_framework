package android.media;

public abstract interface VolumeAutomation
{
  public abstract VolumeShaper createVolumeShaper(VolumeShaper.Configuration paramConfiguration);
}
