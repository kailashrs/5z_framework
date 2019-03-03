package android.widget;

import android.app.IServiceConnection;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import com.android.internal.widget.IRemoteViewsFactory;
import com.android.internal.widget.IRemoteViewsFactory.Stub;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executor;

public class RemoteViewsAdapter
  extends BaseAdapter
  implements Handler.Callback
{
  private static final int DEFAULT_CACHE_SIZE = 40;
  private static final int DEFAULT_LOADING_VIEW_HEIGHT = 50;
  static final int MSG_LOAD_NEXT_ITEM = 3;
  private static final int MSG_MAIN_HANDLER_COMMIT_METADATA = 1;
  private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_CONNECTED = 3;
  private static final int MSG_MAIN_HANDLER_REMOTE_ADAPTER_DISCONNECTED = 4;
  private static final int MSG_MAIN_HANDLER_REMOTE_VIEWS_LOADED = 5;
  private static final int MSG_MAIN_HANDLER_SUPER_NOTIFY_DATA_SET_CHANGED = 2;
  static final int MSG_NOTIFY_DATA_SET_CHANGED = 2;
  static final int MSG_REQUEST_BIND = 1;
  static final int MSG_UNBIND_SERVICE = 4;
  private static final int REMOTE_VIEWS_CACHE_DURATION = 5000;
  private static final String TAG = "RemoteViewsAdapter";
  private static final int UNBIND_SERVICE_DELAY = 5000;
  private static Handler sCacheRemovalQueue;
  private static HandlerThread sCacheRemovalThread;
  private static final HashMap<RemoteViewsCacheKey, FixedSizeRemoteViewsCache> sCachedRemoteViewsCaches = new HashMap();
  private static final HashMap<RemoteViewsCacheKey, Runnable> sRemoteViewsCacheRemoveRunnables = new HashMap();
  private final int mAppWidgetId;
  private final Executor mAsyncViewLoadExecutor;
  private final FixedSizeRemoteViewsCache mCache;
  private final RemoteAdapterConnectionCallback mCallback;
  private final Context mContext;
  private boolean mDataReady = false;
  private final Intent mIntent;
  private ApplicationInfo mLastRemoteViewAppInfo;
  private final Handler mMainHandler;
  private RemoteViews.OnClickHandler mRemoteViewsOnClickHandler;
  private RemoteViewsFrameLayoutRefSet mRequestedViews;
  private final RemoteServiceHandler mServiceHandler;
  private int mVisibleWindowLowerBound;
  private int mVisibleWindowUpperBound;
  private final HandlerThread mWorkerThread;
  
  public RemoteViewsAdapter(Context arg1, Intent arg2, RemoteAdapterConnectionCallback paramRemoteAdapterConnectionCallback, boolean paramBoolean)
  {
    mContext = ???;
    mIntent = ???;
    if (mIntent != null)
    {
      mAppWidgetId = ???.getIntExtra("remoteAdapterAppWidgetId", -1);
      Object localObject = null;
      mRequestedViews = new RemoteViewsFrameLayoutRefSet(null);
      if (???.hasExtra("remoteAdapterAppWidgetId")) {
        ???.removeExtra("remoteAdapterAppWidgetId");
      }
      mWorkerThread = new HandlerThread("RemoteViewsCache-loader");
      mWorkerThread.start();
      mMainHandler = new Handler(Looper.myLooper(), this);
      mServiceHandler = new RemoteServiceHandler(mWorkerThread.getLooper(), this, ???.getApplicationContext());
      ??? = localObject;
      if (paramBoolean) {
        ??? = new HandlerThreadExecutor(mWorkerThread);
      }
      mAsyncViewLoadExecutor = ???;
      mCallback = paramRemoteAdapterConnectionCallback;
      if (sCacheRemovalThread == null)
      {
        sCacheRemovalThread = new HandlerThread("RemoteViewsAdapter-cachePruner");
        sCacheRemovalThread.start();
        sCacheRemovalQueue = new Handler(sCacheRemovalThread.getLooper());
      }
      ??? = new RemoteViewsCacheKey(new Intent.FilterComparison(mIntent), mAppWidgetId);
      synchronized (sCachedRemoteViewsCaches)
      {
        if (sCachedRemoteViewsCaches.containsKey(???))
        {
          mCache = ((FixedSizeRemoteViewsCache)sCachedRemoteViewsCaches.get(???));
          synchronized (mCache.mMetaData)
          {
            if (mCache.mMetaData.count > 0) {
              mDataReady = true;
            }
          }
        }
        ??? = new android/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
        ???.<init>(40);
        mCache = ???;
        if (!mDataReady) {
          requestBindService();
        }
        return;
      }
    }
    throw new IllegalArgumentException("Non-null Intent must be specified.");
  }
  
  private int[] getVisibleWindow(int paramInt)
  {
    int i = mVisibleWindowLowerBound;
    int j = mVisibleWindowUpperBound;
    int k = 0;
    int m = 0;
    if (((i != 0) || (j != 0)) && (i >= 0) && (j >= 0))
    {
      int[] arrayOfInt2;
      if (i <= j)
      {
        arrayOfInt1 = new int[j + 1 - i];
        for (paramInt = m;; paramInt++)
        {
          arrayOfInt2 = arrayOfInt1;
          if (i > j) {
            break;
          }
          arrayOfInt1[paramInt] = i;
          i++;
        }
      }
      m = Math.max(paramInt, i);
      int[] arrayOfInt1 = new int[m - i + j + 1];
      for (paramInt = 0; k <= j; paramInt++)
      {
        arrayOfInt1[paramInt] = k;
        k++;
      }
      for (;;)
      {
        arrayOfInt2 = arrayOfInt1;
        if (i >= m) {
          break;
        }
        arrayOfInt1[paramInt] = i;
        i++;
        paramInt++;
      }
      return arrayOfInt2;
    }
    return new int[0];
  }
  
  private void requestBindService()
  {
    mServiceHandler.removeMessages(4);
    Message.obtain(mServiceHandler, 1, mAppWidgetId, 0, mIntent).sendToTarget();
  }
  
  private void updateRemoteViews(IRemoteViewsFactory arg1, int paramInt, boolean paramBoolean)
  {
    try
    {
      RemoteViews localRemoteViews = ???.getViewAt(paramInt);
      long l = ???.getItemId(paramInt);
      if (localRemoteViews != null)
      {
        if (mApplication != null) {
          if ((mLastRemoteViewAppInfo != null) && (localRemoteViews.hasSameAppInfo(mLastRemoteViewAppInfo))) {
            mApplication = mLastRemoteViewAppInfo;
          } else {
            mLastRemoteViewAppInfo = mApplication;
          }
        }
        int i = localRemoteViews.getLayoutId();
        synchronized (mCache.getMetaData())
        {
          boolean bool = ???.isViewTypeInRange(i);
          i = mCache.mMetaData.count;
          ??? = mCache;
          if (bool) {
            try
            {
              int[] arrayOfInt = getVisibleWindow(i);
              mCache.insert(paramInt, localRemoteViews, l, arrayOfInt);
              if (paramBoolean) {
                Message.obtain(mMainHandler, 5, paramInt, 0, localRemoteViews).sendToTarget();
              }
            }
            finally
            {
              break label183;
            }
          }
          Log.e("RemoteViewsAdapter", "Error: widget's RemoteViewsFactory returns more view types than  indicated by getViewTypeCount() ");
          return;
          label183:
          throw localObject1;
        }
      }
      ??? = new java/lang/RuntimeException;
      ???.<init>("Null remoteViews");
      throw ???;
    }
    catch (RemoteException|RuntimeException ???)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error in updateRemoteViews(");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("): ");
      localStringBuilder.append(???.getMessage());
      Log.e("RemoteViewsAdapter", localStringBuilder.toString());
    }
  }
  
  /* Error */
  private void updateTemporaryMetaData(IRemoteViewsFactory arg1)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface 370 1 0
    //   6: istore_2
    //   7: aload_1
    //   8: invokeinterface 373 1 0
    //   13: istore_3
    //   14: aload_1
    //   15: invokeinterface 376 1 0
    //   20: istore 4
    //   22: new 19	android/widget/RemoteViewsAdapter$LoadingViewTemplate
    //   25: astore 5
    //   27: aload 5
    //   29: aload_1
    //   30: invokeinterface 380 1 0
    //   35: aload_0
    //   36: getfield 121	android/widget/RemoteViewsAdapter:mContext	Landroid/content/Context;
    //   39: invokespecial 383	android/widget/RemoteViewsAdapter$LoadingViewTemplate:<init>	(Landroid/widget/RemoteViews;Landroid/content/Context;)V
    //   42: iload 4
    //   44: ifle +54 -> 98
    //   47: aload 5
    //   49: getfield 387	android/widget/RemoteViewsAdapter$LoadingViewTemplate:remoteViews	Landroid/widget/RemoteViews;
    //   52: ifnonnull +46 -> 98
    //   55: aload_1
    //   56: iconst_0
    //   57: invokeinterface 301 2 0
    //   62: astore 6
    //   64: aload 6
    //   66: ifnull +32 -> 98
    //   69: aload_0
    //   70: getfield 121	android/widget/RemoteViewsAdapter:mContext	Landroid/content/Context;
    //   73: astore_1
    //   74: new 16	android/widget/RemoteViewsAdapter$HandlerThreadExecutor
    //   77: astore 7
    //   79: aload 7
    //   81: aload_0
    //   82: getfield 154	android/widget/RemoteViewsAdapter:mWorkerThread	Landroid/os/HandlerThread;
    //   85: invokespecial 187	android/widget/RemoteViewsAdapter$HandlerThreadExecutor:<init>	(Landroid/os/HandlerThread;)V
    //   88: aload 5
    //   90: aload 6
    //   92: aload_1
    //   93: aload 7
    //   95: invokevirtual 391	android/widget/RemoteViewsAdapter$LoadingViewTemplate:loadFirstViewHeight	(Landroid/widget/RemoteViews;Landroid/content/Context;Ljava/util/concurrent/Executor;)V
    //   98: aload_0
    //   99: getfield 218	android/widget/RemoteViewsAdapter:mCache	Landroid/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
    //   102: invokevirtual 394	android/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache:getTemporaryMetaData	()Landroid/widget/RemoteViewsAdapter$RemoteViewsMetaData;
    //   105: astore_1
    //   106: aload_1
    //   107: monitorenter
    //   108: aload_1
    //   109: iload_2
    //   110: putfield 396	android/widget/RemoteViewsAdapter$RemoteViewsMetaData:hasStableIds	Z
    //   113: aload_1
    //   114: iload_3
    //   115: iconst_1
    //   116: iadd
    //   117: putfield 399	android/widget/RemoteViewsAdapter$RemoteViewsMetaData:viewTypeCount	I
    //   120: aload_1
    //   121: iload 4
    //   123: putfield 225	android/widget/RemoteViewsAdapter$RemoteViewsMetaData:count	I
    //   126: aload_1
    //   127: aload 5
    //   129: putfield 403	android/widget/RemoteViewsAdapter$RemoteViewsMetaData:loadingTemplate	Landroid/widget/RemoteViewsAdapter$LoadingViewTemplate;
    //   132: aload_1
    //   133: monitorexit
    //   134: goto +97 -> 231
    //   137: astore 5
    //   139: aload_1
    //   140: monitorexit
    //   141: aload 5
    //   143: athrow
    //   144: astore_1
    //   145: new 345	java/lang/StringBuilder
    //   148: dup
    //   149: invokespecial 346	java/lang/StringBuilder:<init>	()V
    //   152: astore 5
    //   154: aload 5
    //   156: ldc_w 405
    //   159: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: aload 5
    //   165: aload_1
    //   166: invokevirtual 363	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   169: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: ldc 68
    //   175: aload 5
    //   177: invokevirtual 366	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   180: invokestatic 340	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   183: pop
    //   184: aload_0
    //   185: getfield 218	android/widget/RemoteViewsAdapter:mCache	Landroid/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
    //   188: invokevirtual 324	android/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache:getMetaData	()Landroid/widget/RemoteViewsAdapter$RemoteViewsMetaData;
    //   191: astore_1
    //   192: aload_1
    //   193: monitorenter
    //   194: aload_0
    //   195: getfield 218	android/widget/RemoteViewsAdapter:mCache	Landroid/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
    //   198: invokevirtual 324	android/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache:getMetaData	()Landroid/widget/RemoteViewsAdapter$RemoteViewsMetaData;
    //   201: invokevirtual 408	android/widget/RemoteViewsAdapter$RemoteViewsMetaData:reset	()V
    //   204: aload_1
    //   205: monitorexit
    //   206: aload_0
    //   207: getfield 218	android/widget/RemoteViewsAdapter:mCache	Landroid/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
    //   210: astore_1
    //   211: aload_1
    //   212: monitorenter
    //   213: aload_0
    //   214: getfield 218	android/widget/RemoteViewsAdapter:mCache	Landroid/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache;
    //   217: invokevirtual 409	android/widget/RemoteViewsAdapter$FixedSizeRemoteViewsCache:reset	()V
    //   220: aload_1
    //   221: monitorexit
    //   222: aload_0
    //   223: getfield 170	android/widget/RemoteViewsAdapter:mMainHandler	Landroid/os/Handler;
    //   226: iconst_2
    //   227: invokevirtual 412	android/os/Handler:sendEmptyMessage	(I)Z
    //   230: pop
    //   231: return
    //   232: astore 5
    //   234: aload_1
    //   235: monitorexit
    //   236: aload 5
    //   238: athrow
    //   239: astore 5
    //   241: aload_1
    //   242: monitorexit
    //   243: aload 5
    //   245: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	246	0	this	RemoteViewsAdapter
    //   6	104	2	bool	boolean
    //   13	104	3	i	int
    //   20	102	4	j	int
    //   25	103	5	localLoadingViewTemplate	LoadingViewTemplate
    //   137	5	5	localObject1	Object
    //   152	24	5	localStringBuilder	StringBuilder
    //   232	5	5	localObject2	Object
    //   239	5	5	localObject3	Object
    //   62	29	6	localRemoteViews	RemoteViews
    //   77	17	7	localHandlerThreadExecutor	HandlerThreadExecutor
    // Exception table:
    //   from	to	target	type
    //   108	134	137	finally
    //   139	141	137	finally
    //   0	42	144	android/os/RemoteException
    //   0	42	144	java/lang/RuntimeException
    //   47	64	144	android/os/RemoteException
    //   47	64	144	java/lang/RuntimeException
    //   69	98	144	android/os/RemoteException
    //   69	98	144	java/lang/RuntimeException
    //   98	108	144	android/os/RemoteException
    //   98	108	144	java/lang/RuntimeException
    //   141	144	144	android/os/RemoteException
    //   141	144	144	java/lang/RuntimeException
    //   213	222	232	finally
    //   234	236	232	finally
    //   194	206	239	finally
    //   241	243	239	finally
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      mServiceHandler.unbindNow();
      mWorkerThread.quit();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getCount()
  {
    synchronized (mCache.getMetaData())
    {
      int i = count;
      return i;
    }
  }
  
  public Object getItem(int paramInt)
  {
    return null;
  }
  
  public long getItemId(int paramInt)
  {
    synchronized (mCache)
    {
      if (mCache.containsMetaDataAt(paramInt))
      {
        long l = mCache.getMetaDataAt(paramInt).itemId;
        return l;
      }
      return 0L;
    }
  }
  
  public int getItemViewType(int paramInt)
  {
    synchronized (mCache)
    {
      if (mCache.containsMetaDataAt(paramInt))
      {
        paramInt = mCache.getMetaDataAt(paramInt).typeId;
        synchronized (mCache.getMetaData())
        {
          paramInt = ???.getMappedViewType(paramInt);
          return paramInt;
        }
      }
      return 0;
    }
  }
  
  public Intent getRemoteViewsServiceIntent()
  {
    return mIntent;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    synchronized (mCache)
    {
      RemoteViews localRemoteViews = mCache.getRemoteViewsAt(paramInt);
      int i;
      if (localRemoteViews != null) {
        i = 1;
      } else {
        i = 0;
      }
      boolean bool = false;
      if ((paramView != null) && ((paramView instanceof RemoteViewsFrameLayout))) {
        mRequestedViews.removeView((RemoteViewsFrameLayout)paramView);
      }
      if (i == 0) {
        requestBindService();
      } else {
        bool = mCache.queuePositionsToBePreloadedFromRequestedPosition(paramInt);
      }
      if ((paramView instanceof RemoteViewsFrameLayout))
      {
        paramView = (RemoteViewsFrameLayout)paramView;
      }
      else
      {
        paramView = new android/widget/RemoteViewsAdapter$RemoteViewsFrameLayout;
        paramView.<init>(paramViewGroup.getContext(), mCache);
        paramView.setExecutor(mAsyncViewLoadExecutor);
      }
      if (i != 0)
      {
        paramView.onRemoteViewsLoaded(localRemoteViews, mRemoteViewsOnClickHandler, false);
        if (bool) {
          mServiceHandler.sendEmptyMessage(3);
        }
      }
      else
      {
        paramView.onRemoteViewsLoaded(mCache.getMetaData().getLoadingTemplate(mContext).remoteViews, mRemoteViewsOnClickHandler, false);
        mRequestedViews.add(paramInt, paramView);
        mCache.queueRequestedPositionToLoad(paramInt);
        mServiceHandler.sendEmptyMessage(3);
      }
      return paramView;
    }
  }
  
  public int getViewTypeCount()
  {
    synchronized (mCache.getMetaData())
    {
      int i = viewTypeCount;
      return i;
    }
  }
  
  public boolean handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      return false;
    case 5: 
      mRequestedViews.notifyOnRemoteViewsLoaded(arg1, (RemoteViews)obj);
      return true;
    case 4: 
      if (mCallback != null) {
        mCallback.onRemoteAdapterDisconnected();
      }
      return true;
    case 3: 
      if (mCallback != null) {
        mCallback.onRemoteAdapterConnected();
      }
      return true;
    case 2: 
      superNotifyDataSetChanged();
      return true;
    }
    mCache.commitTemporaryMetaData();
    return true;
  }
  
  public boolean hasStableIds()
  {
    synchronized (mCache.getMetaData())
    {
      boolean bool = hasStableIds;
      return bool;
    }
  }
  
  public boolean isDataReady()
  {
    return mDataReady;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (getCount() <= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void notifyDataSetChanged()
  {
    mServiceHandler.removeMessages(4);
    mServiceHandler.sendEmptyMessage(2);
  }
  
  public void saveRemoteViewsCache()
  {
    RemoteViewsCacheKey localRemoteViewsCacheKey = new RemoteViewsCacheKey(new Intent.FilterComparison(mIntent), mAppWidgetId);
    synchronized (sCachedRemoteViewsCaches)
    {
      if (sRemoteViewsCacheRemoveRunnables.containsKey(localRemoteViewsCacheKey))
      {
        sCacheRemovalQueue.removeCallbacks((Runnable)sRemoteViewsCacheRemoveRunnables.get(localRemoteViewsCacheKey));
        sRemoteViewsCacheRemoveRunnables.remove(localRemoteViewsCacheKey);
      }
      synchronized (mCache.mMetaData)
      {
        int i = mCache.mMetaData.count;
        synchronized (mCache)
        {
          int j = mCache.mIndexRemoteViews.size();
          if ((i > 0) && (j > 0)) {
            sCachedRemoteViewsCaches.put(localRemoteViewsCacheKey, mCache);
          }
          ??? = new android/widget/_$$Lambda$RemoteViewsAdapter$_xHEGE7CkOWJ8u7GAjsH_hc_iiA;
          ((_..Lambda.RemoteViewsAdapter._xHEGE7CkOWJ8u7GAjsH_hc_iiA)???).<init>(localRemoteViewsCacheKey);
          sRemoteViewsCacheRemoveRunnables.put(localRemoteViewsCacheKey, ???);
          sCacheRemovalQueue.postDelayed((Runnable)???, 5000L);
          return;
        }
      }
    }
  }
  
  public void setRemoteViewsOnClickHandler(RemoteViews.OnClickHandler paramOnClickHandler)
  {
    mRemoteViewsOnClickHandler = paramOnClickHandler;
  }
  
  public void setVisibleRangeHint(int paramInt1, int paramInt2)
  {
    mVisibleWindowLowerBound = paramInt1;
    mVisibleWindowUpperBound = paramInt2;
  }
  
  void superNotifyDataSetChanged()
  {
    super.notifyDataSetChanged();
  }
  
  public static class AsyncRemoteAdapterAction
    implements Runnable
  {
    private final RemoteViewsAdapter.RemoteAdapterConnectionCallback mCallback;
    private final Intent mIntent;
    
    public AsyncRemoteAdapterAction(RemoteViewsAdapter.RemoteAdapterConnectionCallback paramRemoteAdapterConnectionCallback, Intent paramIntent)
    {
      mCallback = paramRemoteAdapterConnectionCallback;
      mIntent = paramIntent;
    }
    
    public void run()
    {
      mCallback.setRemoteViewsAdapter(mIntent, true);
    }
  }
  
  private static class FixedSizeRemoteViewsCache
  {
    private static final float sMaxCountSlackPercent = 0.75F;
    private static final int sMaxMemoryLimitInBytes = 2097152;
    private final SparseArray<RemoteViewsAdapter.RemoteViewsIndexMetaData> mIndexMetaData = new SparseArray();
    private final SparseArray<RemoteViews> mIndexRemoteViews = new SparseArray();
    private final SparseBooleanArray mIndicesToLoad = new SparseBooleanArray();
    private int mLastRequestedIndex;
    private final int mMaxCount;
    private final int mMaxCountSlack;
    private final RemoteViewsAdapter.RemoteViewsMetaData mMetaData = new RemoteViewsAdapter.RemoteViewsMetaData();
    private int mPreloadLowerBound;
    private int mPreloadUpperBound;
    private final RemoteViewsAdapter.RemoteViewsMetaData mTemporaryMetaData = new RemoteViewsAdapter.RemoteViewsMetaData();
    
    public FixedSizeRemoteViewsCache(int paramInt)
    {
      mMaxCount = paramInt;
      mMaxCountSlack = Math.round(0.75F * (mMaxCount / 2));
      mPreloadLowerBound = 0;
      mPreloadUpperBound = -1;
      mLastRequestedIndex = -1;
    }
    
    private int getFarthestPositionFrom(int paramInt, int[] paramArrayOfInt)
    {
      int i = 0;
      int j = -1;
      int k = 0;
      int m = -1;
      int n = mIndexRemoteViews.size() - 1;
      while (n >= 0)
      {
        int i1 = mIndexRemoteViews.keyAt(n);
        int i2 = Math.abs(i1 - paramInt);
        int i3 = k;
        int i4 = m;
        if (i2 > k)
        {
          i3 = k;
          i4 = m;
          if (Arrays.binarySearch(paramArrayOfInt, i1) < 0)
          {
            i4 = i1;
            i3 = i2;
          }
        }
        m = i;
        if (i2 >= i)
        {
          j = i1;
          m = i2;
        }
        n--;
        i = m;
        k = i3;
        m = i4;
      }
      if (m > -1) {
        return m;
      }
      return j;
    }
    
    private int getRemoteViewsBitmapMemoryUsage()
    {
      int i = 0;
      int j = mIndexRemoteViews.size() - 1;
      while (j >= 0)
      {
        RemoteViews localRemoteViews = (RemoteViews)mIndexRemoteViews.valueAt(j);
        int k = i;
        if (localRemoteViews != null) {
          k = i + localRemoteViews.estimateMemoryUsage();
        }
        j--;
        i = k;
      }
      return i;
    }
    
    public void commitTemporaryMetaData()
    {
      synchronized (mTemporaryMetaData)
      {
        synchronized (mMetaData)
        {
          mMetaData.set(mTemporaryMetaData);
          return;
        }
      }
    }
    
    public boolean containsMetaDataAt(int paramInt)
    {
      boolean bool;
      if (mIndexMetaData.indexOfKey(paramInt) >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean containsRemoteViewAt(int paramInt)
    {
      boolean bool;
      if (mIndexRemoteViews.indexOfKey(paramInt) >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public RemoteViewsAdapter.RemoteViewsMetaData getMetaData()
    {
      return mMetaData;
    }
    
    public RemoteViewsAdapter.RemoteViewsIndexMetaData getMetaDataAt(int paramInt)
    {
      return (RemoteViewsAdapter.RemoteViewsIndexMetaData)mIndexMetaData.get(paramInt);
    }
    
    public int getNextIndexToLoad()
    {
      synchronized (mIndicesToLoad)
      {
        int i = mIndicesToLoad.indexOfValue(true);
        int j = i;
        if (i < 0) {
          j = mIndicesToLoad.indexOfValue(false);
        }
        if (j < 0) {
          return -1;
        }
        i = mIndicesToLoad.keyAt(j);
        mIndicesToLoad.removeAt(j);
        return i;
      }
    }
    
    public RemoteViews getRemoteViewsAt(int paramInt)
    {
      return (RemoteViews)mIndexRemoteViews.get(paramInt);
    }
    
    public RemoteViewsAdapter.RemoteViewsMetaData getTemporaryMetaData()
    {
      return mTemporaryMetaData;
    }
    
    public void insert(int paramInt, RemoteViews paramRemoteViews, long paramLong, int[] paramArrayOfInt)
    {
      if (mIndexRemoteViews.size() >= mMaxCount) {
        mIndexRemoteViews.remove(getFarthestPositionFrom(paramInt, paramArrayOfInt));
      }
      int i;
      if (mLastRequestedIndex > -1) {
        i = mLastRequestedIndex;
      } else {
        i = paramInt;
      }
      while (getRemoteViewsBitmapMemoryUsage() >= 2097152)
      {
        int j = getFarthestPositionFrom(i, paramArrayOfInt);
        if (j < 0) {
          break;
        }
        mIndexRemoteViews.remove(j);
      }
      paramArrayOfInt = (RemoteViewsAdapter.RemoteViewsIndexMetaData)mIndexMetaData.get(paramInt);
      if (paramArrayOfInt != null) {
        paramArrayOfInt.set(paramRemoteViews, paramLong);
      } else {
        mIndexMetaData.put(paramInt, new RemoteViewsAdapter.RemoteViewsIndexMetaData(paramRemoteViews, paramLong));
      }
      mIndexRemoteViews.put(paramInt, paramRemoteViews);
    }
    
    public boolean queuePositionsToBePreloadedFromRequestedPosition(int paramInt)
    {
      if ((mPreloadLowerBound <= paramInt) && (paramInt <= mPreloadUpperBound) && (Math.abs(paramInt - (mPreloadUpperBound + mPreloadLowerBound) / 2) < mMaxCountSlack)) {
        return false;
      }
      synchronized (mMetaData)
      {
        int i = mMetaData.count;
        synchronized (mIndicesToLoad)
        {
          for (int j = mIndicesToLoad.size() - 1; j >= 0; j--) {
            if (!mIndicesToLoad.valueAt(j)) {
              mIndicesToLoad.removeAt(j);
            }
          }
          j = mMaxCount / 2;
          mPreloadLowerBound = (paramInt - j);
          mPreloadUpperBound = (paramInt + j);
          paramInt = Math.max(0, mPreloadLowerBound);
          j = Math.min(mPreloadUpperBound, i - 1);
          while (paramInt <= j)
          {
            if ((mIndexRemoteViews.indexOfKey(paramInt) < 0) && (!mIndicesToLoad.get(paramInt))) {
              mIndicesToLoad.put(paramInt, false);
            }
            paramInt++;
          }
          return true;
        }
      }
    }
    
    public void queueRequestedPositionToLoad(int paramInt)
    {
      mLastRequestedIndex = paramInt;
      synchronized (mIndicesToLoad)
      {
        mIndicesToLoad.put(paramInt, true);
        return;
      }
    }
    
    public void reset()
    {
      mPreloadLowerBound = 0;
      mPreloadUpperBound = -1;
      mLastRequestedIndex = -1;
      mIndexRemoteViews.clear();
      mIndexMetaData.clear();
      synchronized (mIndicesToLoad)
      {
        mIndicesToLoad.clear();
        return;
      }
    }
  }
  
  private static class HandlerThreadExecutor
    implements Executor
  {
    private final HandlerThread mThread;
    
    HandlerThreadExecutor(HandlerThread paramHandlerThread)
    {
      mThread = paramHandlerThread;
    }
    
    public void execute(Runnable paramRunnable)
    {
      if (Thread.currentThread().getId() == mThread.getId()) {
        paramRunnable.run();
      } else {
        new Handler(mThread.getLooper()).post(paramRunnable);
      }
    }
  }
  
  private static class LoadingViewTemplate
  {
    public int defaultHeight;
    public final RemoteViews remoteViews;
    
    LoadingViewTemplate(RemoteViews paramRemoteViews, Context paramContext)
    {
      remoteViews = paramRemoteViews;
      defaultHeight = Math.round(50.0F * getResourcesgetDisplayMetricsdensity);
    }
    
    public void loadFirstViewHeight(RemoteViews paramRemoteViews, Context paramContext, Executor paramExecutor)
    {
      paramRemoteViews.applyAsync(paramContext, new RemoteViewsAdapter.RemoteViewsFrameLayout(paramContext, null), paramExecutor, new RemoteViews.OnViewAppliedListener()
      {
        public void onError(Exception paramAnonymousException)
        {
          Log.w("RemoteViewsAdapter", "Error inflating first RemoteViews", paramAnonymousException);
        }
        
        public void onViewApplied(View paramAnonymousView)
        {
          try
          {
            paramAnonymousView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            defaultHeight = paramAnonymousView.getMeasuredHeight();
          }
          catch (Exception paramAnonymousView)
          {
            onError(paramAnonymousView);
          }
        }
      });
    }
  }
  
  public static abstract interface RemoteAdapterConnectionCallback
  {
    public abstract void deferNotifyDataSetChanged();
    
    public abstract boolean onRemoteAdapterConnected();
    
    public abstract void onRemoteAdapterDisconnected();
    
    public abstract void setRemoteViewsAdapter(Intent paramIntent, boolean paramBoolean);
  }
  
  private static class RemoteServiceHandler
    extends Handler
    implements ServiceConnection
  {
    private final WeakReference<RemoteViewsAdapter> mAdapter;
    private boolean mBindRequested = false;
    private final Context mContext;
    private boolean mNotifyDataSetChangedPending = false;
    private IRemoteViewsFactory mRemoteViewsFactory;
    
    RemoteServiceHandler(Looper paramLooper, RemoteViewsAdapter paramRemoteViewsAdapter, Context paramContext)
    {
      super();
      mAdapter = new WeakReference(paramRemoteViewsAdapter);
      mContext = paramContext;
    }
    
    private void enqueueDeferredUnbindServiceMessage()
    {
      removeMessages(4);
      sendEmptyMessageDelayed(4, 5000L);
    }
    
    private boolean sendNotifyDataSetChange(boolean paramBoolean)
    {
      if (!paramBoolean) {
        try
        {
          if (mRemoteViewsFactory.isCreated()) {
            break label32;
          }
        }
        catch (RemoteException|RuntimeException localRemoteException)
        {
          break label34;
        }
      }
      mRemoteViewsFactory.onDataSetChanged();
      label32:
      return true;
      label34:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error in updateNotifyDataSetChanged(): ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.e("RemoteViewsAdapter", localStringBuilder.toString());
      return false;
    }
    
    public void handleMessage(Message arg1)
    {
      RemoteViewsAdapter localRemoteViewsAdapter = (RemoteViewsAdapter)mAdapter.get();
      switch (what)
      {
      default: 
        return;
      case 4: 
        unbindNow();
        return;
      case 3: 
        if ((localRemoteViewsAdapter != null) && (mRemoteViewsFactory != null))
        {
          removeMessages(4);
          i = mCache.getNextIndexToLoad();
          if (i > -1)
          {
            localRemoteViewsAdapter.updateRemoteViews(mRemoteViewsFactory, i, true);
            sendEmptyMessage(3);
          }
          else
          {
            enqueueDeferredUnbindServiceMessage();
          }
          return;
        }
        return;
      case 2: 
        enqueueDeferredUnbindServiceMessage();
        if (localRemoteViewsAdapter == null) {
          return;
        }
        if (mRemoteViewsFactory == null)
        {
          mNotifyDataSetChangedPending = true;
          localRemoteViewsAdapter.requestBindService();
          return;
        }
        if (!sendNotifyDataSetChange(true)) {
          return;
        }
        synchronized (mCache)
        {
          mCache.reset();
          localRemoteViewsAdapter.updateTemporaryMetaData(mRemoteViewsFactory);
          synchronized (mCache.getTemporaryMetaData())
          {
            int j = mCache.getTemporaryMetaData().count;
            for (int m : localRemoteViewsAdapter.getVisibleWindow(j)) {
              if (m < j) {
                localRemoteViewsAdapter.updateRemoteViews(mRemoteViewsFactory, m, false);
              }
            }
            mMainHandler.sendEmptyMessage(1);
            mMainHandler.sendEmptyMessage(2);
            return;
          }
        }
      }
      if ((localObject2 == null) || (mRemoteViewsFactory != null)) {
        enqueueDeferredUnbindServiceMessage();
      }
      if (mBindRequested) {
        return;
      }
      ??? = mContext.getServiceDispatcher(this, this, 33554433);
      Intent localIntent = (Intent)obj;
      int i = arg1;
      mBindRequested = AppWidgetManager.getInstance(mContext).bindRemoteViewsService(mContext, i, localIntent, (IServiceConnection)???, 33554433);
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      mRemoteViewsFactory = IRemoteViewsFactory.Stub.asInterface(paramIBinder);
      enqueueDeferredUnbindServiceMessage();
      paramComponentName = (RemoteViewsAdapter)mAdapter.get();
      if (paramComponentName == null) {
        return;
      }
      if (mNotifyDataSetChangedPending)
      {
        mNotifyDataSetChangedPending = false;
        paramComponentName = Message.obtain(this, 2);
        handleMessage(paramComponentName);
        paramComponentName.recycle();
      }
      else
      {
        if (!sendNotifyDataSetChange(false)) {
          return;
        }
        paramComponentName.updateTemporaryMetaData(mRemoteViewsFactory);
        mMainHandler.sendEmptyMessage(1);
        mMainHandler.sendEmptyMessage(3);
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      mRemoteViewsFactory = null;
      paramComponentName = (RemoteViewsAdapter)mAdapter.get();
      if (paramComponentName != null) {
        mMainHandler.sendEmptyMessage(4);
      }
    }
    
    protected void unbindNow()
    {
      if (mBindRequested)
      {
        mBindRequested = false;
        mContext.unbindService(this);
      }
      mRemoteViewsFactory = null;
    }
  }
  
  static class RemoteViewsCacheKey
  {
    final Intent.FilterComparison filter;
    final int widgetId;
    
    RemoteViewsCacheKey(Intent.FilterComparison paramFilterComparison, int paramInt)
    {
      filter = paramFilterComparison;
      widgetId = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof RemoteViewsCacheKey;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (RemoteViewsCacheKey)paramObject;
      bool1 = bool2;
      if (filter.equals(filter))
      {
        bool1 = bool2;
        if (widgetId == widgetId) {
          bool1 = true;
        }
      }
      return bool1;
    }
    
    public int hashCode()
    {
      int i;
      if (filter == null) {
        i = 0;
      } else {
        i = filter.hashCode();
      }
      return i ^ widgetId << 2;
    }
  }
  
  static class RemoteViewsFrameLayout
    extends AppWidgetHostView
  {
    public int cacheIndex = -1;
    private final RemoteViewsAdapter.FixedSizeRemoteViewsCache mCache;
    
    public RemoteViewsFrameLayout(Context paramContext, RemoteViewsAdapter.FixedSizeRemoteViewsCache paramFixedSizeRemoteViewsCache)
    {
      super();
      mCache = paramFixedSizeRemoteViewsCache;
    }
    
    protected View getDefaultView()
    {
      int i = mCache.getMetaData().getLoadingTemplate(getContext()).defaultHeight;
      TextView localTextView = (TextView)LayoutInflater.from(getContext()).inflate(17367278, this, false);
      localTextView.setHeight(i);
      return localTextView;
    }
    
    protected View getErrorView()
    {
      return getDefaultView();
    }
    
    protected Context getRemoteContext()
    {
      return null;
    }
    
    public void onRemoteViewsLoaded(RemoteViews paramRemoteViews, RemoteViews.OnClickHandler paramOnClickHandler, boolean paramBoolean)
    {
      setOnClickHandler(paramOnClickHandler);
      if ((!paramBoolean) && ((paramRemoteViews == null) || (!paramRemoteViews.prefersAsyncApply()))) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      }
      applyRemoteViews(paramRemoteViews, paramBoolean);
    }
  }
  
  private class RemoteViewsFrameLayoutRefSet
    extends SparseArray<LinkedList<RemoteViewsAdapter.RemoteViewsFrameLayout>>
  {
    private RemoteViewsFrameLayoutRefSet() {}
    
    public void add(int paramInt, RemoteViewsAdapter.RemoteViewsFrameLayout paramRemoteViewsFrameLayout)
    {
      LinkedList localLinkedList1 = (LinkedList)get(paramInt);
      LinkedList localLinkedList2 = localLinkedList1;
      if (localLinkedList1 == null)
      {
        localLinkedList2 = new LinkedList();
        put(paramInt, localLinkedList2);
      }
      cacheIndex = paramInt;
      localLinkedList2.add(paramRemoteViewsFrameLayout);
    }
    
    public void notifyOnRemoteViewsLoaded(int paramInt, RemoteViews paramRemoteViews)
    {
      if (paramRemoteViews == null) {
        return;
      }
      Object localObject = (LinkedList)removeReturnOld(paramInt);
      if (localObject != null)
      {
        localObject = ((LinkedList)localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          ((RemoteViewsAdapter.RemoteViewsFrameLayout)((Iterator)localObject).next()).onRemoteViewsLoaded(paramRemoteViews, mRemoteViewsOnClickHandler, true);
        }
      }
    }
    
    public void removeView(RemoteViewsAdapter.RemoteViewsFrameLayout paramRemoteViewsFrameLayout)
    {
      if (cacheIndex < 0) {
        return;
      }
      LinkedList localLinkedList = (LinkedList)get(cacheIndex);
      if (localLinkedList != null) {
        localLinkedList.remove(paramRemoteViewsFrameLayout);
      }
      cacheIndex = -1;
    }
  }
  
  private static class RemoteViewsIndexMetaData
  {
    long itemId;
    int typeId;
    
    public RemoteViewsIndexMetaData(RemoteViews paramRemoteViews, long paramLong)
    {
      set(paramRemoteViews, paramLong);
    }
    
    public void set(RemoteViews paramRemoteViews, long paramLong)
    {
      itemId = paramLong;
      if (paramRemoteViews != null) {
        typeId = paramRemoteViews.getLayoutId();
      } else {
        typeId = 0;
      }
    }
  }
  
  private static class RemoteViewsMetaData
  {
    int count;
    boolean hasStableIds;
    RemoteViewsAdapter.LoadingViewTemplate loadingTemplate;
    private final SparseIntArray mTypeIdIndexMap = new SparseIntArray();
    int viewTypeCount;
    
    public RemoteViewsMetaData()
    {
      reset();
    }
    
    public RemoteViewsAdapter.LoadingViewTemplate getLoadingTemplate(Context paramContext)
    {
      try
      {
        if (loadingTemplate == null)
        {
          RemoteViewsAdapter.LoadingViewTemplate localLoadingViewTemplate = new android/widget/RemoteViewsAdapter$LoadingViewTemplate;
          localLoadingViewTemplate.<init>(null, paramContext);
          loadingTemplate = localLoadingViewTemplate;
        }
        paramContext = loadingTemplate;
        return paramContext;
      }
      finally {}
    }
    
    public int getMappedViewType(int paramInt)
    {
      int i = mTypeIdIndexMap.get(paramInt, -1);
      int j = i;
      if (i == -1)
      {
        j = mTypeIdIndexMap.size() + 1;
        mTypeIdIndexMap.put(paramInt, j);
      }
      return j;
    }
    
    public boolean isViewTypeInRange(int paramInt)
    {
      boolean bool;
      if (getMappedViewType(paramInt) < viewTypeCount) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void reset()
    {
      count = 0;
      viewTypeCount = 1;
      hasStableIds = true;
      loadingTemplate = null;
      mTypeIdIndexMap.clear();
    }
    
    public void set(RemoteViewsMetaData paramRemoteViewsMetaData)
    {
      try
      {
        count = count;
        viewTypeCount = viewTypeCount;
        hasStableIds = hasStableIds;
        loadingTemplate = loadingTemplate;
        return;
      }
      finally {}
    }
  }
}
