package android.media;

import android.content.Context;
import android.media.update.ApiLoader;
import android.media.update.MediaBrowser2Provider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.util.List;
import java.util.concurrent.Executor;

public class MediaBrowser2
  extends MediaController2
{
  private final MediaBrowser2Provider mProvider = (MediaBrowser2Provider)getProvider();
  
  public MediaBrowser2(Context paramContext, SessionToken2 paramSessionToken2, Executor paramExecutor, BrowserCallback paramBrowserCallback)
  {
    super(paramContext, paramSessionToken2, paramExecutor, paramBrowserCallback);
  }
  
  MediaBrowser2Provider createProvider(Context paramContext, SessionToken2 paramSessionToken2, Executor paramExecutor, MediaController2.ControllerCallback paramControllerCallback)
  {
    return ApiLoader.getProvider().createMediaBrowser2(paramContext, this, paramSessionToken2, paramExecutor, (BrowserCallback)paramControllerCallback);
  }
  
  public void getChildren(String paramString, int paramInt1, int paramInt2, Bundle paramBundle)
  {
    mProvider.getChildren_impl(paramString, paramInt1, paramInt2, paramBundle);
  }
  
  public void getItem(String paramString)
  {
    mProvider.getItem_impl(paramString);
  }
  
  public void getLibraryRoot(Bundle paramBundle)
  {
    mProvider.getLibraryRoot_impl(paramBundle);
  }
  
  public void getSearchResult(String paramString, int paramInt1, int paramInt2, Bundle paramBundle)
  {
    mProvider.getSearchResult_impl(paramString, paramInt1, paramInt2, paramBundle);
  }
  
  public void search(String paramString, Bundle paramBundle)
  {
    mProvider.search_impl(paramString, paramBundle);
  }
  
  public void subscribe(String paramString, Bundle paramBundle)
  {
    mProvider.subscribe_impl(paramString, paramBundle);
  }
  
  public void unsubscribe(String paramString)
  {
    mProvider.unsubscribe_impl(paramString);
  }
  
  public static class BrowserCallback
    extends MediaController2.ControllerCallback
  {
    public BrowserCallback() {}
    
    public void onChildrenChanged(MediaBrowser2 paramMediaBrowser2, String paramString, int paramInt, Bundle paramBundle) {}
    
    public void onGetChildrenDone(MediaBrowser2 paramMediaBrowser2, String paramString, int paramInt1, int paramInt2, List<MediaItem2> paramList, Bundle paramBundle) {}
    
    public void onGetItemDone(MediaBrowser2 paramMediaBrowser2, String paramString, MediaItem2 paramMediaItem2) {}
    
    public void onGetLibraryRootDone(MediaBrowser2 paramMediaBrowser2, Bundle paramBundle1, String paramString, Bundle paramBundle2) {}
    
    public void onGetSearchResultDone(MediaBrowser2 paramMediaBrowser2, String paramString, int paramInt1, int paramInt2, List<MediaItem2> paramList, Bundle paramBundle) {}
    
    public void onSearchResultChanged(MediaBrowser2 paramMediaBrowser2, String paramString, int paramInt, Bundle paramBundle) {}
  }
}
