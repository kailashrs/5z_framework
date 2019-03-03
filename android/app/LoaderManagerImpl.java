package android.app;

import android.content.Loader;
import android.content.Loader.OnLoadCanceledListener;
import android.content.Loader.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.DebugUtils;
import android.util.Log;
import android.util.SparseArray;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl
  extends LoaderManager
{
  static boolean DEBUG = false;
  static final String TAG = "LoaderManager";
  boolean mCreatingLoader;
  private FragmentHostCallback mHost;
  final SparseArray<LoaderInfo> mInactiveLoaders = new SparseArray(0);
  final SparseArray<LoaderInfo> mLoaders = new SparseArray(0);
  boolean mRetaining;
  boolean mRetainingStarted;
  boolean mStarted;
  final String mWho;
  
  LoaderManagerImpl(String paramString, FragmentHostCallback paramFragmentHostCallback, boolean paramBoolean)
  {
    mWho = paramString;
    mHost = paramFragmentHostCallback;
    mStarted = paramBoolean;
  }
  
  private LoaderInfo createAndInstallLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    try
    {
      mCreatingLoader = true;
      paramBundle = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
      installLoader(paramBundle);
      return paramBundle;
    }
    finally
    {
      mCreatingLoader = false;
    }
  }
  
  private LoaderInfo createLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    LoaderInfo localLoaderInfo = new LoaderInfo(paramInt, paramBundle, paramLoaderCallbacks);
    mLoader = paramLoaderCallbacks.onCreateLoader(paramInt, paramBundle);
    return localLoaderInfo;
  }
  
  public void destroyLoader(int paramInt)
  {
    if (!mCreatingLoader)
    {
      Object localObject;
      if (DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("destroyLoader in ");
        ((StringBuilder)localObject).append(this);
        ((StringBuilder)localObject).append(" of ");
        ((StringBuilder)localObject).append(paramInt);
        Log.v("LoaderManager", ((StringBuilder)localObject).toString());
      }
      int i = mLoaders.indexOfKey(paramInt);
      if (i >= 0)
      {
        localObject = (LoaderInfo)mLoaders.valueAt(i);
        mLoaders.removeAt(i);
        ((LoaderInfo)localObject).destroy();
      }
      paramInt = mInactiveLoaders.indexOfKey(paramInt);
      if (paramInt >= 0)
      {
        localObject = (LoaderInfo)mInactiveLoaders.valueAt(paramInt);
        mInactiveLoaders.removeAt(paramInt);
        ((LoaderInfo)localObject).destroy();
      }
      if ((mHost != null) && (!hasRunningLoaders())) {
        mHost.mFragmentManager.startPendingDeferredFragments();
      }
      return;
    }
    throw new IllegalStateException("Called while creating a loader");
  }
  
  void doDestroy()
  {
    StringBuilder localStringBuilder;
    if (!mRetaining)
    {
      if (DEBUG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Destroying Active in ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      for (i = mLoaders.size() - 1; i >= 0; i--) {
        ((LoaderInfo)mLoaders.valueAt(i)).destroy();
      }
      mLoaders.clear();
    }
    if (DEBUG)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Destroying Inactive in ");
      localStringBuilder.append(this);
      Log.v("LoaderManager", localStringBuilder.toString());
    }
    for (int i = mInactiveLoaders.size() - 1; i >= 0; i--) {
      ((LoaderInfo)mInactiveLoaders.valueAt(i)).destroy();
    }
    mInactiveLoaders.clear();
    mHost = null;
  }
  
  void doReportNextStart()
  {
    for (int i = mLoaders.size() - 1; i >= 0; i--) {
      mLoaders.valueAt(i)).mReportNextStart = true;
    }
  }
  
  void doReportStart()
  {
    for (int i = mLoaders.size() - 1; i >= 0; i--) {
      ((LoaderInfo)mLoaders.valueAt(i)).reportStart();
    }
  }
  
  void doRetain()
  {
    Object localObject;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Retaining in ");
      ((StringBuilder)localObject).append(this);
      Log.v("LoaderManager", ((StringBuilder)localObject).toString());
    }
    if (!mStarted)
    {
      localObject = new RuntimeException("here");
      ((RuntimeException)localObject).fillInStackTrace();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Called doRetain when not started: ");
      localStringBuilder.append(this);
      Log.w("LoaderManager", localStringBuilder.toString(), (Throwable)localObject);
      return;
    }
    mRetaining = true;
    mStarted = false;
    for (int i = mLoaders.size() - 1; i >= 0; i--) {
      ((LoaderInfo)mLoaders.valueAt(i)).retain();
    }
  }
  
  void doStart()
  {
    Object localObject;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Starting in ");
      ((StringBuilder)localObject).append(this);
      Log.v("LoaderManager", ((StringBuilder)localObject).toString());
    }
    if (mStarted)
    {
      localObject = new RuntimeException("here");
      ((RuntimeException)localObject).fillInStackTrace();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Called doStart when already started: ");
      localStringBuilder.append(this);
      Log.w("LoaderManager", localStringBuilder.toString(), (Throwable)localObject);
      return;
    }
    mStarted = true;
    for (int i = mLoaders.size() - 1; i >= 0; i--) {
      ((LoaderInfo)mLoaders.valueAt(i)).start();
    }
  }
  
  void doStop()
  {
    Object localObject;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Stopping in ");
      ((StringBuilder)localObject).append(this);
      Log.v("LoaderManager", ((StringBuilder)localObject).toString());
    }
    if (!mStarted)
    {
      localObject = new RuntimeException("here");
      ((RuntimeException)localObject).fillInStackTrace();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Called doStop when not started: ");
      localStringBuilder.append(this);
      Log.w("LoaderManager", localStringBuilder.toString(), (Throwable)localObject);
      return;
    }
    for (int i = mLoaders.size() - 1; i >= 0; i--) {
      ((LoaderInfo)mLoaders.valueAt(i)).stop();
    }
    mStarted = false;
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    int i = mLoaders.size();
    int j = 0;
    Object localObject1;
    Object localObject2;
    if (i > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active Loaders:");
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("    ");
      localObject2 = ((StringBuilder)localObject1).toString();
      for (i = 0; i < mLoaders.size(); i++)
      {
        localObject1 = (LoaderInfo)mLoaders.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(mLoaders.keyAt(i));
        paramPrintWriter.print(": ");
        paramPrintWriter.println(((LoaderInfo)localObject1).toString());
        ((LoaderInfo)localObject1).dump((String)localObject2, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    if (mInactiveLoaders.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Inactive Loaders:");
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("    ");
      localObject1 = ((StringBuilder)localObject1).toString();
      for (i = j; i < mInactiveLoaders.size(); i++)
      {
        localObject2 = (LoaderInfo)mInactiveLoaders.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(mInactiveLoaders.keyAt(i));
        paramPrintWriter.print(": ");
        paramPrintWriter.println(((LoaderInfo)localObject2).toString());
        ((LoaderInfo)localObject2).dump((String)localObject1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
  }
  
  void finishRetain()
  {
    if (mRetaining)
    {
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Finished Retaining in ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      mRetaining = false;
      for (int i = mLoaders.size() - 1; i >= 0; i--) {
        ((LoaderInfo)mLoaders.valueAt(i)).finishRetain();
      }
    }
  }
  
  public FragmentHostCallback getFragmentHostCallback()
  {
    return mHost;
  }
  
  public <D> Loader<D> getLoader(int paramInt)
  {
    if (!mCreatingLoader)
    {
      LoaderInfo localLoaderInfo = (LoaderInfo)mLoaders.get(paramInt);
      if (localLoaderInfo != null)
      {
        if (mPendingLoader != null) {
          return mPendingLoader.mLoader;
        }
        return mLoader;
      }
      return null;
    }
    throw new IllegalStateException("Called while creating a loader");
  }
  
  public boolean hasRunningLoaders()
  {
    int i = mLoaders.size();
    boolean bool1 = false;
    for (int j = 0; j < i; j++)
    {
      LoaderInfo localLoaderInfo = (LoaderInfo)mLoaders.valueAt(j);
      boolean bool2;
      if ((mStarted) && (!mDeliveredData)) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      bool1 |= bool2;
    }
    return bool1;
  }
  
  public <D> Loader<D> initLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (!mCreatingLoader)
    {
      LoaderInfo localLoaderInfo = (LoaderInfo)mLoaders.get(paramInt);
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("initLoader in ");
        localStringBuilder.append(this);
        localStringBuilder.append(": args=");
        localStringBuilder.append(paramBundle);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      if (localLoaderInfo == null)
      {
        paramLoaderCallbacks = createAndInstallLoader(paramInt, paramBundle, paramLoaderCallbacks);
        paramBundle = paramLoaderCallbacks;
        if (DEBUG)
        {
          paramBundle = new StringBuilder();
          paramBundle.append("  Created new loader ");
          paramBundle.append(paramLoaderCallbacks);
          Log.v("LoaderManager", paramBundle.toString());
          paramBundle = paramLoaderCallbacks;
        }
      }
      else
      {
        if (DEBUG)
        {
          paramBundle = new StringBuilder();
          paramBundle.append("  Re-using existing loader ");
          paramBundle.append(localLoaderInfo);
          Log.v("LoaderManager", paramBundle.toString());
        }
        mCallbacks = paramLoaderCallbacks;
        paramBundle = localLoaderInfo;
      }
      if ((mHaveData) && (mStarted)) {
        paramBundle.callOnLoadFinished(mLoader, mData);
      }
      return mLoader;
    }
    throw new IllegalStateException("Called while creating a loader");
  }
  
  void installLoader(LoaderInfo paramLoaderInfo)
  {
    mLoaders.put(mId, paramLoaderInfo);
    if (mStarted) {
      paramLoaderInfo.start();
    }
  }
  
  public <D> Loader<D> restartLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (!mCreatingLoader)
    {
      LoaderInfo localLoaderInfo1 = (LoaderInfo)mLoaders.get(paramInt);
      StringBuilder localStringBuilder;
      if (DEBUG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("restartLoader in ");
        localStringBuilder.append(this);
        localStringBuilder.append(": args=");
        localStringBuilder.append(paramBundle);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      if (localLoaderInfo1 != null)
      {
        LoaderInfo localLoaderInfo2 = (LoaderInfo)mInactiveLoaders.get(paramInt);
        if (localLoaderInfo2 != null)
        {
          if (mHaveData)
          {
            if (DEBUG)
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("  Removing last inactive loader: ");
              localStringBuilder.append(localLoaderInfo1);
              Log.v("LoaderManager", localStringBuilder.toString());
            }
            mDeliveredData = false;
            localLoaderInfo2.destroy();
            mLoader.abandon();
            mInactiveLoaders.put(paramInt, localLoaderInfo1);
          }
          else if (!localLoaderInfo1.cancel())
          {
            if (DEBUG) {
              Log.v("LoaderManager", "  Current loader is stopped; replacing");
            }
            mLoaders.put(paramInt, null);
            localLoaderInfo1.destroy();
          }
          else
          {
            if (DEBUG) {
              Log.v("LoaderManager", "  Current loader is running; configuring pending loader");
            }
            if (mPendingLoader != null)
            {
              if (DEBUG)
              {
                localStringBuilder = new StringBuilder();
                localStringBuilder.append("  Removing pending loader: ");
                localStringBuilder.append(mPendingLoader);
                Log.v("LoaderManager", localStringBuilder.toString());
              }
              mPendingLoader.destroy();
              mPendingLoader = null;
            }
            if (DEBUG) {
              Log.v("LoaderManager", "  Enqueuing as new pending loader");
            }
            mPendingLoader = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
            return mPendingLoader.mLoader;
          }
        }
        else
        {
          if (DEBUG)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("  Making last loader inactive: ");
            localStringBuilder.append(localLoaderInfo1);
            Log.v("LoaderManager", localStringBuilder.toString());
          }
          mLoader.abandon();
          mInactiveLoaders.put(paramInt, localLoaderInfo1);
        }
      }
      return createAndInstallLoadermLoader;
    }
    throw new IllegalStateException("Called while creating a loader");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("LoaderManager{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" in ");
    DebugUtils.buildShortClassTag(mHost, localStringBuilder);
    localStringBuilder.append("}}");
    return localStringBuilder.toString();
  }
  
  void updateHostController(FragmentHostCallback paramFragmentHostCallback)
  {
    mHost = paramFragmentHostCallback;
  }
  
  final class LoaderInfo
    implements Loader.OnLoadCompleteListener<Object>, Loader.OnLoadCanceledListener<Object>
  {
    final Bundle mArgs;
    LoaderManager.LoaderCallbacks<Object> mCallbacks;
    Object mData;
    boolean mDeliveredData;
    boolean mDestroyed;
    boolean mHaveData;
    final int mId;
    boolean mListenerRegistered;
    Loader<Object> mLoader;
    LoaderInfo mPendingLoader;
    boolean mReportNextStart;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    
    public LoaderInfo(Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
    {
      mId = paramBundle;
      mArgs = paramLoaderCallbacks;
      Object localObject;
      mCallbacks = localObject;
    }
    
    void callOnLoadFinished(Loader<Object> paramLoader, Object paramObject)
    {
      if (mCallbacks != null)
      {
        String str = null;
        if (mHost != null)
        {
          str = mHost.mFragmentManager.mNoTransactionsBecause;
          mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
        }
        try
        {
          if (LoaderManagerImpl.DEBUG)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("  onLoadFinished in ");
            localStringBuilder.append(paramLoader);
            localStringBuilder.append(": ");
            localStringBuilder.append(paramLoader.dataToString(paramObject));
            Log.v("LoaderManager", localStringBuilder.toString());
          }
          mCallbacks.onLoadFinished(paramLoader, paramObject);
          if (mHost != null) {
            mHost.mFragmentManager.mNoTransactionsBecause = str;
          }
          mDeliveredData = true;
        }
        finally
        {
          if (mHost != null) {
            mHost.mFragmentManager.mNoTransactionsBecause = str;
          }
        }
      }
    }
    
    boolean cancel()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("  Canceling: ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      if ((mStarted) && (mLoader != null) && (mListenerRegistered))
      {
        boolean bool = mLoader.cancelLoad();
        if (!bool) {
          onLoadCanceled(mLoader);
        }
        return bool;
      }
      return false;
    }
    
    void destroy()
    {
      Object localObject1;
      if (LoaderManagerImpl.DEBUG)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("  Destroying: ");
        ((StringBuilder)localObject1).append(this);
        Log.v("LoaderManager", ((StringBuilder)localObject1).toString());
      }
      mDestroyed = true;
      boolean bool = mDeliveredData;
      mDeliveredData = false;
      if ((mCallbacks != null) && (mLoader != null) && (mHaveData) && (bool))
      {
        if (LoaderManagerImpl.DEBUG)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("  Reseting: ");
          ((StringBuilder)localObject1).append(this);
          Log.v("LoaderManager", ((StringBuilder)localObject1).toString());
        }
        localObject1 = null;
        if (mHost != null)
        {
          localObject1 = mHost.mFragmentManager.mNoTransactionsBecause;
          mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
        }
      }
      try
      {
        mCallbacks.onLoaderReset(mLoader);
        if (mHost != null) {
          mHost.mFragmentManager.mNoTransactionsBecause = ((String)localObject1);
        }
      }
      finally
      {
        if (mHost != null) {
          mHost.mFragmentManager.mNoTransactionsBecause = ((String)localObject1);
        }
      }
      mHaveData = false;
      if (mLoader != null)
      {
        if (mListenerRegistered)
        {
          mListenerRegistered = false;
          mLoader.unregisterListener(this);
          mLoader.unregisterOnLoadCanceledListener(this);
        }
        mLoader.reset();
      }
      if (mPendingLoader != null) {
        mPendingLoader.destroy();
      }
    }
    
    public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mId=");
      paramPrintWriter.print(mId);
      paramPrintWriter.print(" mArgs=");
      paramPrintWriter.println(mArgs);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCallbacks=");
      paramPrintWriter.println(mCallbacks);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mLoader=");
      paramPrintWriter.println(mLoader);
      Object localObject;
      StringBuilder localStringBuilder;
      if (mLoader != null)
      {
        localObject = mLoader;
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("  ");
        ((Loader)localObject).dump(localStringBuilder.toString(), paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      if ((mHaveData) || (mDeliveredData))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mHaveData=");
        paramPrintWriter.print(mHaveData);
        paramPrintWriter.print("  mDeliveredData=");
        paramPrintWriter.println(mDeliveredData);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mData=");
        paramPrintWriter.println(mData);
      }
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStarted=");
      paramPrintWriter.print(mStarted);
      paramPrintWriter.print(" mReportNextStart=");
      paramPrintWriter.print(mReportNextStart);
      paramPrintWriter.print(" mDestroyed=");
      paramPrintWriter.println(mDestroyed);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mRetaining=");
      paramPrintWriter.print(mRetaining);
      paramPrintWriter.print(" mRetainingStarted=");
      paramPrintWriter.print(mRetainingStarted);
      paramPrintWriter.print(" mListenerRegistered=");
      paramPrintWriter.println(mListenerRegistered);
      if (mPendingLoader != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Pending Loader ");
        paramPrintWriter.print(mPendingLoader);
        paramPrintWriter.println(":");
        localObject = mPendingLoader;
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("  ");
        ((LoaderInfo)localObject).dump(localStringBuilder.toString(), paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    
    void finishRetain()
    {
      if (mRetaining)
      {
        if (LoaderManagerImpl.DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("  Finished Retaining: ");
          localStringBuilder.append(this);
          Log.v("LoaderManager", localStringBuilder.toString());
        }
        mRetaining = false;
        if ((mStarted != mRetainingStarted) && (!mStarted)) {
          stop();
        }
      }
      if ((mStarted) && (mHaveData) && (!mReportNextStart)) {
        callOnLoadFinished(mLoader, mData);
      }
    }
    
    public void onLoadCanceled(Loader<Object> paramLoader)
    {
      if (LoaderManagerImpl.DEBUG)
      {
        paramLoader = new StringBuilder();
        paramLoader.append("onLoadCanceled: ");
        paramLoader.append(this);
        Log.v("LoaderManager", paramLoader.toString());
      }
      if (mDestroyed)
      {
        if (LoaderManagerImpl.DEBUG) {
          Log.v("LoaderManager", "  Ignoring load canceled -- destroyed");
        }
        return;
      }
      if (mLoaders.get(mId) != this)
      {
        if (LoaderManagerImpl.DEBUG) {
          Log.v("LoaderManager", "  Ignoring load canceled -- not active");
        }
        return;
      }
      paramLoader = mPendingLoader;
      if (paramLoader != null)
      {
        if (LoaderManagerImpl.DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("  Switching to pending loader: ");
          localStringBuilder.append(paramLoader);
          Log.v("LoaderManager", localStringBuilder.toString());
        }
        mPendingLoader = null;
        mLoaders.put(mId, null);
        destroy();
        installLoader(paramLoader);
      }
    }
    
    public void onLoadComplete(Loader<Object> paramLoader, Object paramObject)
    {
      if (LoaderManagerImpl.DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("onLoadComplete: ");
        ((StringBuilder)localObject).append(this);
        Log.v("LoaderManager", ((StringBuilder)localObject).toString());
      }
      if (mDestroyed)
      {
        if (LoaderManagerImpl.DEBUG) {
          Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
        }
        return;
      }
      if (mLoaders.get(mId) != this)
      {
        if (LoaderManagerImpl.DEBUG) {
          Log.v("LoaderManager", "  Ignoring load complete -- not active");
        }
        return;
      }
      Object localObject = mPendingLoader;
      if (localObject != null)
      {
        if (LoaderManagerImpl.DEBUG)
        {
          paramLoader = new StringBuilder();
          paramLoader.append("  Switching to pending loader: ");
          paramLoader.append(localObject);
          Log.v("LoaderManager", paramLoader.toString());
        }
        mPendingLoader = null;
        mLoaders.put(mId, null);
        destroy();
        installLoader((LoaderInfo)localObject);
        return;
      }
      if ((mData != paramObject) || (!mHaveData))
      {
        mData = paramObject;
        mHaveData = true;
        if (mStarted) {
          callOnLoadFinished(paramLoader, paramObject);
        }
      }
      paramLoader = (LoaderInfo)mInactiveLoaders.get(mId);
      if ((paramLoader != null) && (paramLoader != this))
      {
        mDeliveredData = false;
        paramLoader.destroy();
        mInactiveLoaders.remove(mId);
      }
      if ((mHost != null) && (!hasRunningLoaders())) {
        mHost.mFragmentManager.startPendingDeferredFragments();
      }
    }
    
    void reportStart()
    {
      if ((mStarted) && (mReportNextStart))
      {
        mReportNextStart = false;
        if ((mHaveData) && (!mRetaining)) {
          callOnLoadFinished(mLoader, mData);
        }
      }
    }
    
    void retain()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("  Retaining: ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      mRetaining = true;
      mRetainingStarted = mStarted;
      mStarted = false;
      mCallbacks = null;
    }
    
    void start()
    {
      if ((mRetaining) && (mRetainingStarted))
      {
        mStarted = true;
        return;
      }
      if (mStarted) {
        return;
      }
      mStarted = true;
      StringBuilder localStringBuilder;
      if (LoaderManagerImpl.DEBUG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("  Starting: ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      if ((mLoader == null) && (mCallbacks != null)) {
        mLoader = mCallbacks.onCreateLoader(mId, mArgs);
      }
      if (mLoader != null)
      {
        if ((mLoader.getClass().isMemberClass()) && (!Modifier.isStatic(mLoader.getClass().getModifiers())))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
          localStringBuilder.append(mLoader);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        if (!mListenerRegistered)
        {
          mLoader.registerListener(mId, this);
          mLoader.registerOnLoadCanceledListener(this);
          mListenerRegistered = true;
        }
        mLoader.startLoading();
      }
    }
    
    void stop()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("  Stopping: ");
        localStringBuilder.append(this);
        Log.v("LoaderManager", localStringBuilder.toString());
      }
      mStarted = false;
      if ((!mRetaining) && (mLoader != null) && (mListenerRegistered))
      {
        mListenerRegistered = false;
        mLoader.unregisterListener(this);
        mLoader.unregisterOnLoadCanceledListener(this);
        mLoader.stopLoading();
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(64);
      localStringBuilder.append("LoaderInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" #");
      localStringBuilder.append(mId);
      localStringBuilder.append(" : ");
      DebugUtils.buildShortClassTag(mLoader, localStringBuilder);
      localStringBuilder.append("}}");
      return localStringBuilder.toString();
    }
  }
}
