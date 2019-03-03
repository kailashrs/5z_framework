package android.app;

import android.content.Loader;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@Deprecated
public abstract class LoaderManager
{
  public LoaderManager() {}
  
  public static void enableDebugLogging(boolean paramBoolean)
  {
    LoaderManagerImpl.DEBUG = paramBoolean;
  }
  
  public abstract void destroyLoader(int paramInt);
  
  public abstract void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString);
  
  public FragmentHostCallback getFragmentHostCallback()
  {
    return null;
  }
  
  public abstract <D> Loader<D> getLoader(int paramInt);
  
  public abstract <D> Loader<D> initLoader(int paramInt, Bundle paramBundle, LoaderCallbacks<D> paramLoaderCallbacks);
  
  public abstract <D> Loader<D> restartLoader(int paramInt, Bundle paramBundle, LoaderCallbacks<D> paramLoaderCallbacks);
  
  @Deprecated
  public static abstract interface LoaderCallbacks<D>
  {
    public abstract Loader<D> onCreateLoader(int paramInt, Bundle paramBundle);
    
    public abstract void onLoadFinished(Loader<D> paramLoader, D paramD);
    
    public abstract void onLoaderReset(Loader<D> paramLoader);
  }
}
