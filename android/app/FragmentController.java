package android.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

@Deprecated
public class FragmentController
{
  private final FragmentHostCallback<?> mHost;
  
  private FragmentController(FragmentHostCallback<?> paramFragmentHostCallback)
  {
    mHost = paramFragmentHostCallback;
  }
  
  public static final FragmentController createController(FragmentHostCallback<?> paramFragmentHostCallback)
  {
    return new FragmentController(paramFragmentHostCallback);
  }
  
  public void attachHost(Fragment paramFragment)
  {
    mHost.mFragmentManager.attachController(mHost, mHost, paramFragment);
  }
  
  public void dispatchActivityCreated()
  {
    mHost.mFragmentManager.dispatchActivityCreated();
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    mHost.mFragmentManager.dispatchConfigurationChanged(paramConfiguration);
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem)
  {
    return mHost.mFragmentManager.dispatchContextItemSelected(paramMenuItem);
  }
  
  public void dispatchCreate()
  {
    mHost.mFragmentManager.dispatchCreate();
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    return mHost.mFragmentManager.dispatchCreateOptionsMenu(paramMenu, paramMenuInflater);
  }
  
  public void dispatchDestroy()
  {
    mHost.mFragmentManager.dispatchDestroy();
  }
  
  public void dispatchDestroyView()
  {
    mHost.mFragmentManager.dispatchDestroyView();
  }
  
  public void dispatchLowMemory()
  {
    mHost.mFragmentManager.dispatchLowMemory();
  }
  
  @Deprecated
  public void dispatchMultiWindowModeChanged(boolean paramBoolean)
  {
    mHost.mFragmentManager.dispatchMultiWindowModeChanged(paramBoolean);
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    mHost.mFragmentManager.dispatchMultiWindowModeChanged(paramBoolean, paramConfiguration);
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem)
  {
    return mHost.mFragmentManager.dispatchOptionsItemSelected(paramMenuItem);
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu)
  {
    mHost.mFragmentManager.dispatchOptionsMenuClosed(paramMenu);
  }
  
  public void dispatchPause()
  {
    mHost.mFragmentManager.dispatchPause();
  }
  
  @Deprecated
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean)
  {
    mHost.mFragmentManager.dispatchPictureInPictureModeChanged(paramBoolean);
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    mHost.mFragmentManager.dispatchPictureInPictureModeChanged(paramBoolean, paramConfiguration);
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu)
  {
    return mHost.mFragmentManager.dispatchPrepareOptionsMenu(paramMenu);
  }
  
  public void dispatchResume()
  {
    mHost.mFragmentManager.dispatchResume();
  }
  
  public void dispatchStart()
  {
    mHost.mFragmentManager.dispatchStart();
  }
  
  public void dispatchStop()
  {
    mHost.mFragmentManager.dispatchStop();
  }
  
  public void dispatchTrimMemory(int paramInt)
  {
    mHost.mFragmentManager.dispatchTrimMemory(paramInt);
  }
  
  public void doLoaderDestroy()
  {
    mHost.doLoaderDestroy();
  }
  
  public void doLoaderStart()
  {
    mHost.doLoaderStart();
  }
  
  public void doLoaderStop(boolean paramBoolean)
  {
    mHost.doLoaderStop(paramBoolean);
  }
  
  public void dumpLoaders(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    mHost.dumpLoaders(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public boolean execPendingActions()
  {
    return mHost.mFragmentManager.execPendingActions();
  }
  
  public Fragment findFragmentByWho(String paramString)
  {
    return mHost.mFragmentManager.findFragmentByWho(paramString);
  }
  
  public FragmentManager getFragmentManager()
  {
    return mHost.getFragmentManagerImpl();
  }
  
  public LoaderManager getLoaderManager()
  {
    return mHost.getLoaderManagerImpl();
  }
  
  public void noteStateNotSaved()
  {
    mHost.mFragmentManager.noteStateNotSaved();
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return mHost.mFragmentManager.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  public void reportLoaderStart()
  {
    mHost.reportLoaderStart();
  }
  
  public void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig)
  {
    mHost.mFragmentManager.restoreAllState(paramParcelable, paramFragmentManagerNonConfig);
  }
  
  @Deprecated
  public void restoreAllState(Parcelable paramParcelable, List<Fragment> paramList)
  {
    mHost.mFragmentManager.restoreAllState(paramParcelable, new FragmentManagerNonConfig(paramList, null));
  }
  
  public void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> paramArrayMap)
  {
    mHost.restoreLoaderNonConfig(paramArrayMap);
  }
  
  public ArrayMap<String, LoaderManager> retainLoaderNonConfig()
  {
    return mHost.retainLoaderNonConfig();
  }
  
  public FragmentManagerNonConfig retainNestedNonConfig()
  {
    return mHost.mFragmentManager.retainNonConfig();
  }
  
  @Deprecated
  public List<Fragment> retainNonConfig()
  {
    return mHost.mFragmentManager.retainNonConfig().getFragments();
  }
  
  public Parcelable saveAllState()
  {
    return mHost.mFragmentManager.saveAllState();
  }
}
