package android.media.update;

import android.media.MediaSession2.ControllerInfo;
import android.os.Bundle;

public abstract interface MediaLibraryService2Provider
  extends MediaSessionService2Provider
{
  public static abstract interface LibraryRootProvider
  {
    public abstract Bundle getExtras_impl();
    
    public abstract String getRootId_impl();
  }
  
  public static abstract interface MediaLibrarySessionProvider
    extends MediaSession2Provider
  {
    public abstract void notifyChildrenChanged_impl(MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt, Bundle paramBundle);
    
    public abstract void notifyChildrenChanged_impl(String paramString, int paramInt, Bundle paramBundle);
    
    public abstract void notifySearchResultChanged_impl(MediaSession2.ControllerInfo paramControllerInfo, String paramString, int paramInt, Bundle paramBundle);
  }
}
