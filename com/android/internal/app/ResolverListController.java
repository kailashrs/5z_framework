package com.android.internal.app;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ResolverListController
{
  private static final boolean DEBUG = false;
  private static final String TAG = "ResolverListController";
  private boolean isComputed = false;
  private final Context mContext;
  private final int mLaunchedFromUid;
  private final String mReferrerPackage;
  private ResolverComparator mResolverComparator;
  private final Intent mTargetIntent;
  private final PackageManager mpm;
  
  public ResolverListController(Context paramContext, PackageManager paramPackageManager, Intent paramIntent, String paramString, int paramInt)
  {
    mContext = paramContext;
    mpm = paramPackageManager;
    mLaunchedFromUid = paramInt;
    mTargetIntent = paramIntent;
    mReferrerPackage = paramString;
    mResolverComparator = new ResolverComparator(mContext, mTargetIntent, mReferrerPackage, null);
  }
  
  private static boolean isSameResolvedComponent(ResolveInfo paramResolveInfo, ResolverActivity.ResolvedComponentInfo paramResolvedComponentInfo)
  {
    paramResolveInfo = activityInfo;
    boolean bool;
    if ((packageName.equals(name.getPackageName())) && (name.equals(name.getClassName()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public void addResolveListDedupe(List<ResolverActivity.ResolvedComponentInfo> paramList, Intent paramIntent, List<ResolveInfo> paramList1)
  {
    int i = paramList1.size();
    int j = paramList.size();
    for (int k = 0; k < i; k++)
    {
      Object localObject1 = (ResolveInfo)paramList1.get(k);
      int m = 0;
      int i1;
      Object localObject2;
      for (int n = 0;; n++)
      {
        i1 = m;
        if (n >= j) {
          break;
        }
        localObject2 = (ResolverActivity.ResolvedComponentInfo)paramList.get(n);
        if (isSameResolvedComponent((ResolveInfo)localObject1, (ResolverActivity.ResolvedComponentInfo)localObject2))
        {
          i1 = 1;
          ((ResolverActivity.ResolvedComponentInfo)localObject2).add(paramIntent, (ResolveInfo)localObject1);
          break;
        }
      }
      if (i1 == 0)
      {
        localObject2 = new ComponentName(activityInfo.packageName, activityInfo.name);
        localObject1 = new ResolverActivity.ResolvedComponentInfo((ComponentName)localObject2, paramIntent, (ResolveInfo)localObject1);
        ((ResolverActivity.ResolvedComponentInfo)localObject1).setPinned(isComponentPinned((ComponentName)localObject2));
        paramList.add(localObject1);
      }
    }
  }
  
  public void destroy()
  {
    mResolverComparator.destroy();
  }
  
  @VisibleForTesting
  public ArrayList<ResolverActivity.ResolvedComponentInfo> filterIneligibleActivities(List<ResolverActivity.ResolvedComponentInfo> paramList, boolean paramBoolean)
  {
    Object localObject1 = null;
    int i = paramList.size() - 1;
    while (i >= 0)
    {
      Object localObject2 = (ResolverActivity.ResolvedComponentInfo)paramList.get(i);
      int j = 0;
      ActivityInfo localActivityInfo = getResolveInfoAt0activityInfo;
      int k = ActivityManager.checkComponentPermission(permission, mLaunchedFromUid, applicationInfo.uid, exported);
      if ((applicationInfo.flags & 0x40000000) != 0) {
        j = 1;
      }
      if ((k == 0) && (j == 0))
      {
        localObject2 = localObject1;
        if (!isComponentFiltered(localActivityInfo.getComponentName())) {}
      }
      else
      {
        localObject2 = localObject1;
        if (paramBoolean)
        {
          localObject2 = localObject1;
          if (localObject1 == null) {
            localObject2 = new ArrayList(paramList);
          }
        }
        paramList.remove(i);
      }
      i--;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  @VisibleForTesting
  public ArrayList<ResolverActivity.ResolvedComponentInfo> filterLowPriority(List<ResolverActivity.ResolvedComponentInfo> paramList, boolean paramBoolean)
  {
    Object localObject1 = null;
    ResolveInfo localResolveInfo1 = ((ResolverActivity.ResolvedComponentInfo)paramList.get(0)).getResolveInfoAt(0);
    int i = paramList.size();
    int j = 1;
    while (j < i)
    {
      ResolveInfo localResolveInfo2 = ((ResolverActivity.ResolvedComponentInfo)paramList.get(j)).getResolveInfoAt(0);
      Object localObject2 = localObject1;
      int k = i;
      Object localObject3;
      int m;
      if (priority == priority)
      {
        localObject3 = localObject1;
        m = i;
        if (isDefault != isDefault) {
          k = i;
        }
      }
      else
      {
        for (localObject2 = localObject1;; localObject2 = localObject1)
        {
          localObject3 = localObject2;
          m = k;
          if (j >= k) {
            break;
          }
          localObject1 = localObject2;
          if (paramBoolean)
          {
            localObject1 = localObject2;
            if (localObject2 == null) {
              localObject1 = new ArrayList(paramList);
            }
          }
          paramList.remove(j);
          k--;
        }
      }
      j++;
      localObject1 = localObject3;
      i = m;
    }
    return localObject1;
  }
  
  @VisibleForTesting
  public ResolveInfo getLastChosen()
    throws RemoteException
  {
    return AppGlobals.getPackageManager().getLastChosenActivity(mTargetIntent, mTargetIntent.resolveTypeIfNeeded(mContext.getContentResolver()), 65536);
  }
  
  @VisibleForTesting
  public List<ResolverActivity.ResolvedComponentInfo> getResolversForIntent(boolean paramBoolean1, boolean paramBoolean2, List<Intent> paramList)
  {
    Object localObject1 = null;
    int i = 0;
    int j = paramList.size();
    while (i < j)
    {
      Intent localIntent = (Intent)paramList.get(i);
      boolean bool = mpm.hasSystemFeature("asus.software.twinapps");
      int k = 0;
      if (bool) {
        m = 536870912;
      } else {
        m = 0;
      }
      if (paramBoolean1) {
        n = 64;
      } else {
        n = 0;
      }
      if (paramBoolean2) {
        k = 128;
      }
      int n = 0x10000 | m | n | k;
      if (!localIntent.isWebIntent())
      {
        m = n;
        if ((localIntent.getFlags() & 0x800) == 0) {}
      }
      else
      {
        m = n | 0x800000;
      }
      List localList = mpm.queryIntentActivities(localIntent, m);
      for (int m = localList.size() - 1; m >= 0; m--)
      {
        localObject2 = (ResolveInfo)localList.get(m);
        if ((activityInfo != null) && (!activityInfo.exported)) {
          localList.remove(m);
        }
      }
      Object localObject2 = localObject1;
      if (localList != null)
      {
        localObject2 = localObject1;
        if (localObject1 == null) {
          localObject2 = new ArrayList();
        }
        addResolveListDedupe((List)localObject2, localIntent, localList);
      }
      i++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  @VisibleForTesting
  public float getScore(ResolverActivity.DisplayResolveInfo paramDisplayResolveInfo)
  {
    return mResolverComparator.getScore(paramDisplayResolveInfo.getResolvedComponentName());
  }
  
  boolean isComponentFiltered(ComponentName paramComponentName)
  {
    return false;
  }
  
  boolean isComponentPinned(ComponentName paramComponentName)
  {
    return false;
  }
  
  @VisibleForTesting
  public void setLastChosen(Intent paramIntent, IntentFilter paramIntentFilter, int paramInt)
    throws RemoteException
  {
    AppGlobals.getPackageManager().setLastChosenActivity(paramIntent, paramIntent.resolveType(mContext.getContentResolver()), 65536, paramIntentFilter, paramInt, paramIntent.getComponent());
  }
  
  @VisibleForTesting
  public void sort(List<ResolverActivity.ResolvedComponentInfo> paramList)
  {
    if (mResolverComparator == null)
    {
      Log.d("ResolverListController", "Comparator has already been destroyed; skipped.");
      return;
    }
    try
    {
      System.currentTimeMillis();
      if (!isComputed)
      {
        CountDownLatch localCountDownLatch = new java/util/concurrent/CountDownLatch;
        localCountDownLatch.<init>(1);
        localObject = new com/android/internal/app/ResolverListController$ComputeCallback;
        ((ComputeCallback)localObject).<init>(this, localCountDownLatch);
        mResolverComparator.setCallBack((ResolverComparator.AfterCompute)localObject);
        mResolverComparator.compute(paramList);
        localCountDownLatch.await();
        isComputed = true;
      }
      Collections.sort(paramList, mResolverComparator);
      System.currentTimeMillis();
    }
    catch (InterruptedException paramList)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Compute & Sort was interrupted: ");
      ((StringBuilder)localObject).append(paramList);
      Log.e("ResolverListController", ((StringBuilder)localObject).toString());
    }
  }
  
  public void updateChooserCounts(String paramString1, int paramInt, String paramString2)
  {
    mResolverComparator.updateChooserCounts(paramString1, paramInt, paramString2);
  }
  
  public void updateModel(ComponentName paramComponentName)
  {
    mResolverComparator.updateModel(paramComponentName);
  }
  
  private class ComputeCallback
    implements ResolverComparator.AfterCompute
  {
    private CountDownLatch mFinishComputeSignal;
    
    public ComputeCallback(CountDownLatch paramCountDownLatch)
    {
      mFinishComputeSignal = paramCountDownLatch;
    }
    
    public void afterCompute()
    {
      mFinishComputeSignal.countDown();
    }
  }
}
