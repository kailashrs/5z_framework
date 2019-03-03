package android.media.update;

import android.os.Bundle;

public abstract interface MediaBrowser2Provider
  extends MediaController2Provider
{
  public abstract void getChildren_impl(String paramString, int paramInt1, int paramInt2, Bundle paramBundle);
  
  public abstract void getItem_impl(String paramString);
  
  public abstract void getLibraryRoot_impl(Bundle paramBundle);
  
  public abstract void getSearchResult_impl(String paramString, int paramInt1, int paramInt2, Bundle paramBundle);
  
  public abstract void search_impl(String paramString, Bundle paramBundle);
  
  public abstract void subscribe_impl(String paramString, Bundle paramBundle);
  
  public abstract void unsubscribe_impl(String paramString);
}
