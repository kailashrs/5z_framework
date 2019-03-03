package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@Deprecated
public abstract class FragmentHostCallback<E>
  extends FragmentContainer
{
  private final Activity mActivity;
  private ArrayMap<String, LoaderManager> mAllLoaderManagers;
  private boolean mCheckedForLoaderManager;
  final Context mContext;
  final FragmentManagerImpl mFragmentManager = new FragmentManagerImpl();
  private final Handler mHandler;
  private LoaderManagerImpl mLoaderManager;
  private boolean mLoadersStarted;
  private boolean mRetainLoaders;
  final int mWindowAnimations;
  
  FragmentHostCallback(Activity paramActivity)
  {
    this(paramActivity, paramActivity, mHandler, 0);
  }
  
  FragmentHostCallback(Activity paramActivity, Context paramContext, Handler paramHandler, int paramInt)
  {
    mActivity = paramActivity;
    mContext = paramContext;
    mHandler = paramHandler;
    mWindowAnimations = paramInt;
  }
  
  public FragmentHostCallback(Context paramContext, Handler paramHandler, int paramInt)
  {
    this(localActivity, paramContext, chooseHandler(paramContext, paramHandler), paramInt);
  }
  
  private static Handler chooseHandler(Context paramContext, Handler paramHandler)
  {
    if ((paramHandler == null) && ((paramContext instanceof Activity))) {
      return mHandler;
    }
    return paramHandler;
  }
  
  void doLoaderDestroy()
  {
    if (mLoaderManager == null) {
      return;
    }
    mLoaderManager.doDestroy();
  }
  
  void doLoaderRetain()
  {
    if (mLoaderManager == null) {
      return;
    }
    mLoaderManager.doRetain();
  }
  
  void doLoaderStart()
  {
    if (mLoadersStarted) {
      return;
    }
    mLoadersStarted = true;
    if (mLoaderManager != null) {
      mLoaderManager.doStart();
    } else if (!mCheckedForLoaderManager) {
      mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
    }
    mCheckedForLoaderManager = true;
  }
  
  void doLoaderStop(boolean paramBoolean)
  {
    mRetainLoaders = paramBoolean;
    if (mLoaderManager == null) {
      return;
    }
    if (!mLoadersStarted) {
      return;
    }
    mLoadersStarted = false;
    if (paramBoolean) {
      mLoaderManager.doRetain();
    } else {
      mLoaderManager.doStop();
    }
  }
  
  void dumpLoaders(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mLoadersStarted=");
    paramPrintWriter.println(mLoadersStarted);
    if (mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Loader Manager ");
      paramPrintWriter.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
      paramPrintWriter.println(":");
      LoaderManagerImpl localLoaderManagerImpl = mLoaderManager;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("  ");
      localLoaderManagerImpl.dump(localStringBuilder.toString(), paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  Activity getActivity()
  {
    return mActivity;
  }
  
  Context getContext()
  {
    return mContext;
  }
  
  FragmentManagerImpl getFragmentManagerImpl()
  {
    return mFragmentManager;
  }
  
  Handler getHandler()
  {
    return mHandler;
  }
  
  LoaderManagerImpl getLoaderManager(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mAllLoaderManagers == null) {
      mAllLoaderManagers = new ArrayMap();
    }
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)mAllLoaderManagers.get(paramString);
    if ((localLoaderManagerImpl == null) && (paramBoolean2))
    {
      localLoaderManagerImpl = new LoaderManagerImpl(paramString, this, paramBoolean1);
      mAllLoaderManagers.put(paramString, localLoaderManagerImpl);
      paramString = localLoaderManagerImpl;
    }
    else
    {
      paramString = localLoaderManagerImpl;
      if (paramBoolean1)
      {
        paramString = localLoaderManagerImpl;
        if (localLoaderManagerImpl != null)
        {
          paramString = localLoaderManagerImpl;
          if (!mStarted)
          {
            localLoaderManagerImpl.doStart();
            paramString = localLoaderManagerImpl;
          }
        }
      }
    }
    return paramString;
  }
  
  LoaderManagerImpl getLoaderManagerImpl()
  {
    if (mLoaderManager != null) {
      return mLoaderManager;
    }
    mCheckedForLoaderManager = true;
    mLoaderManager = getLoaderManager("(root)", mLoadersStarted, true);
    return mLoaderManager;
  }
  
  boolean getRetainLoaders()
  {
    return mRetainLoaders;
  }
  
  void inactivateFragment(String paramString)
  {
    if (mAllLoaderManagers != null)
    {
      LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)mAllLoaderManagers.get(paramString);
      if ((localLoaderManagerImpl != null) && (!mRetaining))
      {
        localLoaderManagerImpl.doDestroy();
        mAllLoaderManagers.remove(paramString);
      }
    }
  }
  
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onDump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  public <T extends View> T onFindViewById(int paramInt)
  {
    return null;
  }
  
  public abstract E onGetHost();
  
  public LayoutInflater onGetLayoutInflater()
  {
    return (LayoutInflater)mContext.getSystemService("layout_inflater");
  }
  
  public int onGetWindowAnimations()
  {
    return mWindowAnimations;
  }
  
  public boolean onHasView()
  {
    return true;
  }
  
  public boolean onHasWindowAnimations()
  {
    return true;
  }
  
  public void onInvalidateOptionsMenu() {}
  
  public void onRequestPermissionsFromFragment(Fragment paramFragment, String[] paramArrayOfString, int paramInt) {}
  
  public boolean onShouldSaveFragmentState(Fragment paramFragment)
  {
    return true;
  }
  
  public void onStartActivityAsUserFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (paramInt == -1)
    {
      mContext.startActivityAsUser(paramIntent, paramUserHandle);
      return;
    }
    throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
  }
  
  public void onStartActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    if (paramInt == -1)
    {
      mContext.startActivity(paramIntent);
      return;
    }
    throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
  }
  
  public void onStartIntentSenderFromFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    if (paramInt1 == -1)
    {
      mContext.startIntentSender(paramIntentSender, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
    }
    throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
  }
  
  public boolean onUseFragmentManagerInflaterFactory()
  {
    return false;
  }
  
  void reportLoaderStart()
  {
    if (mAllLoaderManagers != null)
    {
      int i = mAllLoaderManagers.size();
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[i];
      for (int j = i - 1; j >= 0; j--) {
        arrayOfLoaderManagerImpl[j] = ((LoaderManagerImpl)mAllLoaderManagers.valueAt(j));
      }
      for (j = 0; j < i; j++)
      {
        LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[j];
        localLoaderManagerImpl.finishRetain();
        localLoaderManagerImpl.doReportStart();
      }
    }
  }
  
  void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> paramArrayMap)
  {
    if (paramArrayMap != null)
    {
      int i = 0;
      int j = paramArrayMap.size();
      while (i < j)
      {
        ((LoaderManagerImpl)paramArrayMap.valueAt(i)).updateHostController(this);
        i++;
      }
    }
    mAllLoaderManagers = paramArrayMap;
  }
  
  ArrayMap<String, LoaderManager> retainLoaderNonConfig()
  {
    int i = 0;
    int j = 0;
    if (mAllLoaderManagers != null)
    {
      int k = mAllLoaderManagers.size();
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[k];
      for (int m = k - 1; m >= 0; m--) {
        arrayOfLoaderManagerImpl[m] = ((LoaderManagerImpl)mAllLoaderManagers.valueAt(m));
      }
      boolean bool = getRetainLoaders();
      int n = 0;
      m = j;
      for (;;)
      {
        i = m;
        if (n >= k) {
          break;
        }
        LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[n];
        if ((!mRetaining) && (bool))
        {
          if (!mStarted) {
            localLoaderManagerImpl.doStart();
          }
          localLoaderManagerImpl.doRetain();
        }
        if (mRetaining)
        {
          m = 1;
        }
        else
        {
          localLoaderManagerImpl.doDestroy();
          mAllLoaderManagers.remove(mWho);
        }
        n++;
      }
    }
    if (i != 0) {
      return mAllLoaderManagers;
    }
    return null;
  }
}
