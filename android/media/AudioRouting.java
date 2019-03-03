package android.media;

import android.os.Handler;

public abstract interface AudioRouting
{
  public abstract void addOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener, Handler paramHandler);
  
  public abstract AudioDeviceInfo getPreferredDevice();
  
  public abstract AudioDeviceInfo getRoutedDevice();
  
  public abstract void removeOnRoutingChangedListener(OnRoutingChangedListener paramOnRoutingChangedListener);
  
  public abstract boolean setPreferredDevice(AudioDeviceInfo paramAudioDeviceInfo);
  
  public static abstract interface OnRoutingChangedListener
  {
    public abstract void onRoutingChanged(AudioRouting paramAudioRouting);
  }
}
