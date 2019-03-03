package android.media;

import android.annotation.SystemApi;
import android.os.RemoteException;

@SystemApi
public class PlayerProxy
{
  private static final boolean DEBUG = false;
  private static final String TAG = "PlayerProxy";
  private final AudioPlaybackConfiguration mConf;
  
  PlayerProxy(AudioPlaybackConfiguration paramAudioPlaybackConfiguration)
  {
    if (paramAudioPlaybackConfiguration != null)
    {
      mConf = paramAudioPlaybackConfiguration;
      return;
    }
    throw new IllegalArgumentException("Illegal null AudioPlaybackConfiguration");
  }
  
  public void applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
  {
    try
    {
      mConf.getIPlayer().applyVolumeShaper(paramConfiguration, paramOperation);
      return;
    }
    catch (NullPointerException|RemoteException paramConfiguration)
    {
      throw new IllegalStateException("No player to proxy for applyVolumeShaper operation, player already released?", paramConfiguration);
    }
  }
  
  @SystemApi
  public void pause()
  {
    try
    {
      mConf.getIPlayer().pause();
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for pause operation, player already released?", localNullPointerException);
    }
  }
  
  @SystemApi
  public void setPan(float paramFloat)
  {
    try
    {
      mConf.getIPlayer().setPan(paramFloat);
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for setPan operation, player already released?", localNullPointerException);
    }
  }
  
  @SystemApi
  public void setStartDelayMs(int paramInt)
  {
    try
    {
      mConf.getIPlayer().setStartDelayMs(paramInt);
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for setStartDelayMs operation, player already released?", localNullPointerException);
    }
  }
  
  @SystemApi
  public void setVolume(float paramFloat)
  {
    try
    {
      mConf.getIPlayer().setVolume(paramFloat);
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for setVolume operation, player already released?", localNullPointerException);
    }
  }
  
  @SystemApi
  public void start()
  {
    try
    {
      mConf.getIPlayer().start();
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for start operation, player already released?", localNullPointerException);
    }
  }
  
  @SystemApi
  public void stop()
  {
    try
    {
      mConf.getIPlayer().stop();
      return;
    }
    catch (NullPointerException|RemoteException localNullPointerException)
    {
      throw new IllegalStateException("No player to proxy for stop operation, player already released?", localNullPointerException);
    }
  }
}
