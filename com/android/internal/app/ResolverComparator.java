package com.android.internal.app;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.resolver.IResolverRankerResult;
import android.service.resolver.IResolverRankerResult.Stub;
import android.service.resolver.IResolverRankerService;
import android.service.resolver.IResolverRankerService.Stub;
import android.service.resolver.ResolverTarget;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ResolverComparator
  implements Comparator<ResolverActivity.ResolvedComponentInfo>
{
  private static final int CONNECTION_COST_TIMEOUT_MILLIS = 200;
  private static final boolean DEBUG = false;
  private static final int NUM_OF_TOP_ANNOTATIONS_TO_USE = 3;
  private static final float RECENCY_MULTIPLIER = 2.0F;
  private static final long RECENCY_TIME_PERIOD = 43200000L;
  private static final int RESOLVER_RANKER_RESULT_TIMEOUT = 1;
  private static final int RESOLVER_RANKER_SERVICE_RESULT = 0;
  private static final String TAG = "ResolverComparator";
  private static final long USAGE_STATS_PERIOD = 604800000L;
  private static final int WATCHDOG_TIMEOUT_MILLIS = 500;
  private String mAction;
  private AfterCompute mAfterCompute;
  private String[] mAnnotations;
  private final Collator mCollator;
  private CountDownLatch mConnectSignal;
  private ResolverRankerServiceConnection mConnection;
  private String mContentType;
  private Context mContext;
  private final long mCurrentTime;
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      int i = what;
      int j = 0;
      switch (i)
      {
      default: 
        super.handleMessage(paramAnonymousMessage);
        break;
      case 1: 
        mHandler.removeMessages(0);
        mAfterCompute.afterCompute();
        break;
      case 0: 
        if (mHandler.hasMessages(1))
        {
          if (obj != null)
          {
            paramAnonymousMessage = (List)obj;
            if ((paramAnonymousMessage != null) && (mTargets != null) && (paramAnonymousMessage.size() == mTargets.size()))
            {
              int k = mTargets.size();
              i = 0;
              while (j < k)
              {
                float f = ((ResolverTarget)paramAnonymousMessage.get(j)).getSelectProbability();
                if (f != ((ResolverTarget)mTargets.get(j)).getSelectProbability())
                {
                  ((ResolverTarget)mTargets.get(j)).setSelectProbability(f);
                  i = 1;
                }
                j++;
              }
              if (i != 0) {
                ResolverComparator.access$202(ResolverComparator.this, mResolvedRankerName);
              }
            }
            else
            {
              Log.e("ResolverComparator", "Sizes of sent and received ResolverTargets diff.");
            }
          }
          else
          {
            Log.e("ResolverComparator", "Receiving null prediction results.");
          }
          mHandler.removeMessages(1);
          mAfterCompute.afterCompute();
        }
        break;
      }
    }
  };
  private final boolean mHttp;
  private final Object mLock = new Object();
  private final PackageManager mPm;
  private IResolverRankerService mRanker;
  private ComponentName mRankerServiceName;
  private final String mReferrerPackage;
  private ComponentName mResolvedRankerName;
  private final long mSinceTime;
  private final Map<String, UsageStats> mStats;
  private ArrayList<ResolverTarget> mTargets;
  private final LinkedHashMap<ComponentName, ResolverTarget> mTargetsDict = new LinkedHashMap();
  private final UsageStatsManager mUsm;
  
  public ResolverComparator(Context paramContext, Intent paramIntent, String paramString, AfterCompute paramAfterCompute)
  {
    mCollator = Collator.getInstance(getResourcesgetConfigurationlocale);
    String str = paramIntent.getScheme();
    boolean bool;
    if ((!"http".equals(str)) && (!"https".equals(str))) {
      bool = false;
    } else {
      bool = true;
    }
    mHttp = bool;
    mReferrerPackage = paramString;
    mAfterCompute = paramAfterCompute;
    mContext = paramContext;
    mPm = paramContext.getPackageManager();
    mUsm = ((UsageStatsManager)paramContext.getSystemService("usagestats"));
    mCurrentTime = System.currentTimeMillis();
    mSinceTime = (mCurrentTime - 604800000L);
    mStats = mUsm.queryAndAggregateUsageStats(mSinceTime, mCurrentTime);
    mContentType = paramIntent.getType();
    getContentAnnotations(paramIntent);
    mAction = paramIntent.getAction();
    mRankerServiceName = new ComponentName(mContext, getClass());
  }
  
  private void addDefaultSelectProbability(ResolverTarget paramResolverTarget)
  {
    paramResolverTarget.setSelectProbability((float)(1.0D / (Math.exp(1.6568F - (2.5543F * paramResolverTarget.getLaunchScore() + 2.8412F * paramResolverTarget.getTimeSpentScore() + 0.269F * paramResolverTarget.getRecencyScore() + 4.2222F * paramResolverTarget.getChooserScore())) + 1.0D)));
  }
  
  private void initRanker(Context paramContext)
  {
    synchronized (mLock)
    {
      if ((mConnection != null) && (mRanker != null)) {
        return;
      }
      ??? = resolveRankerService();
      if (??? == null) {
        return;
      }
      mConnectSignal = new CountDownLatch(1);
      mConnection = new ResolverRankerServiceConnection(mConnectSignal);
      paramContext.bindServiceAsUser((Intent)???, mConnection, 1, UserHandle.SYSTEM);
      return;
    }
  }
  
  static boolean isPersistentProcess(ResolverActivity.ResolvedComponentInfo paramResolvedComponentInfo)
  {
    boolean bool = false;
    if ((paramResolvedComponentInfo != null) && (paramResolvedComponentInfo.getCount() > 0))
    {
      if ((getResolveInfoAt0activityInfo.applicationInfo.flags & 0x8) != 0) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  private void logMetrics(int paramInt)
  {
    if (mRankerServiceName != null)
    {
      MetricsLogger localMetricsLogger = new MetricsLogger();
      LogMaker localLogMaker = new LogMaker(1085);
      localLogMaker.setComponentName(mRankerServiceName);
      int i;
      if (mAnnotations == null) {
        i = 0;
      } else {
        i = 1;
      }
      localLogMaker.addTaggedData(1086, Integer.valueOf(i));
      localLogMaker.addTaggedData(1087, Integer.valueOf(paramInt));
      localMetricsLogger.write(localLogMaker);
    }
  }
  
  private void predictSelectProbabilities(List<ResolverTarget> paramList)
  {
    if (mConnection != null) {
      try
      {
        mConnectSignal.await(200L, TimeUnit.MILLISECONDS);
        synchronized (mLock)
        {
          if (mRanker != null)
          {
            mRanker.predict(paramList, mConnection.resolverRankerResult);
            return;
          }
        }
        if (mAfterCompute == null) {
          return;
        }
      }
      catch (RemoteException paramList)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Error in Predict: ");
        ((StringBuilder)???).append(paramList);
        Log.e("ResolverComparator", ((StringBuilder)???).toString());
      }
      catch (InterruptedException paramList)
      {
        Log.e("ResolverComparator", "Error in Wait for Service Connection.");
      }
    }
    mAfterCompute.afterCompute();
  }
  
  private void reset()
  {
    mTargetsDict.clear();
    mTargets = null;
    mRankerServiceName = new ComponentName(mContext, getClass());
    mResolvedRankerName = null;
    startWatchDog(500);
    initRanker(mContext);
  }
  
  private Intent resolveRankerService()
  {
    Intent localIntent = new Intent("android.service.resolver.ResolverRankerService");
    Iterator localIterator = mPm.queryIntentServices(localIntent, 0).iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (ResolveInfo)localIterator.next();
      if ((localObject != null) && (serviceInfo != null) && (serviceInfo.applicationInfo != null))
      {
        ComponentName localComponentName = new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
        try
        {
          if (!"android.permission.BIND_RESOLVER_RANKER_SERVICE".equals(mPm.getServiceInfo(localComponentName, 0).permission))
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("ResolverRankerService ");
            ((StringBuilder)localObject).append(localComponentName);
            ((StringBuilder)localObject).append(" does not require permission ");
            ((StringBuilder)localObject).append("android.permission.BIND_RESOLVER_RANKER_SERVICE");
            ((StringBuilder)localObject).append(" - this service will not be queried for ResolverComparator. add android:permission=\"");
            ((StringBuilder)localObject).append("android.permission.BIND_RESOLVER_RANKER_SERVICE");
            ((StringBuilder)localObject).append("\" to the <service> tag for ");
            ((StringBuilder)localObject).append(localComponentName);
            ((StringBuilder)localObject).append(" in the manifest.");
            Log.w("ResolverComparator", ((StringBuilder)localObject).toString());
          }
          else if (mPm.checkPermission("android.permission.PROVIDE_RESOLVER_RANKER_SERVICE", serviceInfo.packageName) != 0)
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("ResolverRankerService ");
            ((StringBuilder)localObject).append(localComponentName);
            ((StringBuilder)localObject).append(" does not hold permission ");
            ((StringBuilder)localObject).append("android.permission.PROVIDE_RESOLVER_RANKER_SERVICE");
            ((StringBuilder)localObject).append(" - this service will not be queried for ResolverComparator.");
            Log.w("ResolverComparator", ((StringBuilder)localObject).toString());
          }
          else
          {
            mResolvedRankerName = localComponentName;
            localIntent.setComponent(localComponentName);
            return localIntent;
          }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Could not look up service ");
          localStringBuilder.append(localComponentName);
          localStringBuilder.append("; component name not found");
          Log.e("ResolverComparator", localStringBuilder.toString());
        }
      }
    }
    return null;
  }
  
  private void setFeatures(ResolverTarget paramResolverTarget, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramResolverTarget.setRecencyScore(paramFloat1);
    paramResolverTarget.setLaunchScore(paramFloat2);
    paramResolverTarget.setTimeSpentScore(paramFloat3);
    paramResolverTarget.setChooserScore(paramFloat4);
  }
  
  private void startWatchDog(int paramInt)
  {
    if (mHandler == null) {
      Log.d("ResolverComparator", "Error: Handler is Null; Needs to be initialized.");
    }
    mHandler.sendEmptyMessageDelayed(1, paramInt);
  }
  
  public int compare(ResolverActivity.ResolvedComponentInfo paramResolvedComponentInfo1, ResolverActivity.ResolvedComponentInfo paramResolvedComponentInfo2)
  {
    int i = 0;
    Object localObject = paramResolvedComponentInfo1.getResolveInfoAt(0);
    ResolveInfo localResolveInfo = paramResolvedComponentInfo2.getResolveInfoAt(0);
    if (targetUserId != -2)
    {
      if (targetUserId == -2) {
        i = 1;
      }
      return i;
    }
    int j = targetUserId;
    i = -1;
    if (j != -2) {
      return -1;
    }
    if (mHttp)
    {
      bool1 = ResolverActivity.isSpecificUriMatch(match);
      if (bool1 != ResolverActivity.isSpecificUriMatch(match))
      {
        if (!bool1) {
          i = 1;
        }
        return i;
      }
    }
    boolean bool1 = paramResolvedComponentInfo1.isPinned();
    boolean bool2 = paramResolvedComponentInfo2.isPinned();
    if ((bool1) && (!bool2)) {
      return -1;
    }
    if ((!bool1) && (bool2)) {
      return 1;
    }
    if ((!bool1) && (!bool2) && (mStats != null))
    {
      paramResolvedComponentInfo2 = (ResolverTarget)mTargetsDict.get(new ComponentName(activityInfo.packageName, activityInfo.name));
      paramResolvedComponentInfo1 = (ResolverTarget)mTargetsDict.get(new ComponentName(activityInfo.packageName, activityInfo.name));
      if ((paramResolvedComponentInfo2 != null) && (paramResolvedComponentInfo1 != null))
      {
        j = Float.compare(paramResolvedComponentInfo1.getSelectProbability(), paramResolvedComponentInfo2.getSelectProbability());
        if (j != 0)
        {
          if (j > 0) {
            i = 1;
          }
          return i;
        }
      }
    }
    paramResolvedComponentInfo2 = ((ResolveInfo)localObject).loadLabel(mPm);
    paramResolvedComponentInfo1 = paramResolvedComponentInfo2;
    if (paramResolvedComponentInfo2 == null) {
      paramResolvedComponentInfo1 = activityInfo.name;
    }
    localObject = localResolveInfo.loadLabel(mPm);
    paramResolvedComponentInfo2 = (ResolverActivity.ResolvedComponentInfo)localObject;
    if (localObject == null) {
      paramResolvedComponentInfo2 = activityInfo.name;
    }
    return mCollator.compare(paramResolvedComponentInfo1.toString().trim(), paramResolvedComponentInfo2.toString().trim());
  }
  
  public void compute(List<ResolverActivity.ResolvedComponentInfo> paramList)
  {
    reset();
    long l = mCurrentTime;
    paramList = paramList.iterator();
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = 1.0F;
    float f6;
    float f9;
    for (float f4 = 1.0F; paramList.hasNext(); f4 = f9)
    {
      ResolverActivity.ResolvedComponentInfo localResolvedComponentInfo = (ResolverActivity.ResolvedComponentInfo)paramList.next();
      ResolverTarget localResolverTarget = new ResolverTarget();
      mTargetsDict.put(name, localResolverTarget);
      localObject = (UsageStats)mStats.get(name.getPackageName());
      float f5;
      float f7;
      float f8;
      if (localObject != null)
      {
        if ((!name.getPackageName().equals(mReferrerPackage)) && (!isPersistentProcess(localResolvedComponentInfo)))
        {
          f5 = (float)Math.max(((UsageStats)localObject).getLastTimeUsed() - (l - 43200000L), 0L);
          localResolverTarget.setRecencyScore(f5);
          f6 = f1;
          if (f5 > f1) {
            f6 = f5;
          }
        }
        else
        {
          f6 = f1;
        }
        f5 = (float)((UsageStats)localObject).getTotalTimeInForeground();
        localResolverTarget.setTimeSpentScore(f5);
        f1 = f2;
        if (f5 > f2) {
          f1 = f5;
        }
        f5 = mLaunchCount;
        localResolverTarget.setLaunchScore(f5);
        f2 = f3;
        if (f5 > f3) {
          f2 = f5;
        }
        f3 = 0.0F;
        if ((mChooserCounts != null) && (mAction != null) && (mChooserCounts.get(mAction) != null))
        {
          f3 = ((Integer)((ArrayMap)mChooserCounts.get(mAction)).getOrDefault(mContentType, Integer.valueOf(0))).intValue();
          if (mAnnotations != null)
          {
            int i = mAnnotations.length;
            for (int j = 0; j < i; j++) {
              f3 += ((Integer)((ArrayMap)mChooserCounts.get(mAction)).getOrDefault(mAnnotations[j], Integer.valueOf(0))).intValue();
            }
          }
        }
        localResolverTarget.setChooserScore(f3);
        f7 = f6;
        f8 = f1;
        f5 = f2;
        f9 = f4;
        if (f3 > f4)
        {
          f7 = f6;
          f8 = f1;
          f5 = f2;
          f9 = f3;
        }
      }
      else
      {
        f9 = f4;
        f5 = f3;
        f8 = f2;
        f7 = f1;
      }
      f1 = f7;
      f2 = f8;
      f3 = f5;
    }
    mTargets = new ArrayList(mTargetsDict.values());
    Object localObject = mTargets.iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramList = (ResolverTarget)((Iterator)localObject).next();
      f6 = paramList.getRecencyScore() / f1;
      setFeatures(paramList, f6 * f6 * 2.0F, paramList.getLaunchScore() / f3, paramList.getTimeSpentScore() / f2, paramList.getChooserScore() / f4);
      addDefaultSelectProbability(paramList);
    }
    predictSelectProbabilities(mTargets);
  }
  
  public void destroy()
  {
    mHandler.removeMessages(0);
    mHandler.removeMessages(1);
    if (mConnection != null)
    {
      mContext.unbindService(mConnection);
      mConnection.destroy();
    }
    if (mAfterCompute != null) {
      mAfterCompute.afterCompute();
    }
  }
  
  public void getContentAnnotations(Intent paramIntent)
  {
    paramIntent = paramIntent.getStringArrayListExtra("android.intent.extra.CONTENT_ANNOTATIONS");
    if (paramIntent != null)
    {
      int i = paramIntent.size();
      int j = i;
      if (i > 3) {
        j = 3;
      }
      mAnnotations = new String[j];
      for (i = 0; i < j; i++) {
        mAnnotations[i] = ((String)paramIntent.get(i));
      }
    }
  }
  
  public float getScore(ComponentName paramComponentName)
  {
    paramComponentName = (ResolverTarget)mTargetsDict.get(paramComponentName);
    if (paramComponentName != null) {
      return paramComponentName.getSelectProbability();
    }
    return 0.0F;
  }
  
  public void setCallBack(AfterCompute paramAfterCompute)
  {
    mAfterCompute = paramAfterCompute;
  }
  
  public void updateChooserCounts(String paramString1, int paramInt, String paramString2)
  {
    if (mUsm != null) {
      mUsm.reportChooserSelection(paramString1, paramInt, mContentType, mAnnotations, paramString2);
    }
  }
  
  public void updateModel(ComponentName paramComponentName)
  {
    synchronized (mLock)
    {
      Object localObject2 = mRanker;
      if (localObject2 != null) {
        try
        {
          localObject2 = new java/util/ArrayList;
          ((ArrayList)localObject2).<init>(mTargetsDict.keySet());
          int i = ((ArrayList)localObject2).indexOf(paramComponentName);
          if ((i >= 0) && (mTargets != null))
          {
            float f = getScore(paramComponentName);
            int j = 0;
            paramComponentName = mTargets.iterator();
            while (paramComponentName.hasNext())
            {
              int k = j;
              if (((ResolverTarget)paramComponentName.next()).getSelectProbability() > f) {
                k = j + 1;
              }
              j = k;
            }
            logMetrics(j);
            mRanker.train(mTargets, i);
          }
        }
        catch (RemoteException localRemoteException)
        {
          paramComponentName = new java/lang/StringBuilder;
          paramComponentName.<init>();
          paramComponentName.append("Error in Train: ");
          paramComponentName.append(localRemoteException);
          Log.e("ResolverComparator", paramComponentName.toString());
        }
      }
      return;
    }
  }
  
  public static abstract interface AfterCompute
  {
    public abstract void afterCompute();
  }
  
  private class ResolverRankerServiceConnection
    implements ServiceConnection
  {
    private final CountDownLatch mConnectSignal;
    public final IResolverRankerResult resolverRankerResult = new IResolverRankerResult.Stub()
    {
      public void sendResult(List<ResolverTarget> paramAnonymousList)
        throws RemoteException
      {
        synchronized (mLock)
        {
          Message localMessage = Message.obtain();
          what = 0;
          obj = paramAnonymousList;
          mHandler.sendMessage(localMessage);
          return;
        }
      }
    };
    
    public ResolverRankerServiceConnection(CountDownLatch paramCountDownLatch)
    {
      mConnectSignal = paramCountDownLatch;
    }
    
    public void destroy()
    {
      synchronized (mLock)
      {
        ResolverComparator.access$602(ResolverComparator.this, null);
        return;
      }
    }
    
    public void onServiceConnected(ComponentName arg1, IBinder paramIBinder)
    {
      synchronized (mLock)
      {
        ResolverComparator.access$602(ResolverComparator.this, IResolverRankerService.Stub.asInterface(paramIBinder));
        mConnectSignal.countDown();
        return;
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      synchronized (mLock)
      {
        destroy();
        return;
      }
    }
  }
}
