package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaSession2Provider.CommandGroupProvider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.util.Set;

public final class SessionCommandGroup2
{
  private final MediaSession2Provider.CommandGroupProvider mProvider;
  
  public SessionCommandGroup2()
  {
    mProvider = ApiLoader.getProvider().createMediaSession2CommandGroup(this, null);
  }
  
  public SessionCommandGroup2(SessionCommandGroup2 paramSessionCommandGroup2)
  {
    mProvider = ApiLoader.getProvider().createMediaSession2CommandGroup(this, paramSessionCommandGroup2);
  }
  
  public SessionCommandGroup2(MediaSession2Provider.CommandGroupProvider paramCommandGroupProvider)
  {
    mProvider = paramCommandGroupProvider;
  }
  
  public static SessionCommandGroup2 fromBundle(Bundle paramBundle)
  {
    return ApiLoader.getProvider().fromBundle_MediaSession2CommandGroup(paramBundle);
  }
  
  public void addAllPredefinedCommands()
  {
    mProvider.addAllPredefinedCommands_impl();
  }
  
  public void addCommand(int paramInt) {}
  
  public void addCommand(SessionCommand2 paramSessionCommand2)
  {
    mProvider.addCommand_impl(paramSessionCommand2);
  }
  
  public Set<SessionCommand2> getCommands()
  {
    return mProvider.getCommands_impl();
  }
  
  public MediaSession2Provider.CommandGroupProvider getProvider()
  {
    return mProvider;
  }
  
  public boolean hasCommand(int paramInt)
  {
    return mProvider.hasCommand_impl(paramInt);
  }
  
  public boolean hasCommand(SessionCommand2 paramSessionCommand2)
  {
    return mProvider.hasCommand_impl(paramSessionCommand2);
  }
  
  public void removeCommand(int paramInt) {}
  
  public void removeCommand(SessionCommand2 paramSessionCommand2)
  {
    mProvider.removeCommand_impl(paramSessionCommand2);
  }
  
  public Bundle toBundle()
  {
    return mProvider.toBundle_impl();
  }
}
