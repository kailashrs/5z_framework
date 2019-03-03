package com.android.internal.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.Preconditions;
import java.util.HashSet;

public abstract class PackageMonitor
  extends BroadcastReceiver
{
  public static final int PACKAGE_PERMANENT_CHANGE = 3;
  public static final int PACKAGE_TEMPORARY_CHANGE = 2;
  public static final int PACKAGE_UNCHANGED = 0;
  public static final int PACKAGE_UPDATING = 1;
  static final IntentFilter sExternalFilt;
  static final IntentFilter sNonDataFilt;
  static final IntentFilter sPackageFilt = new IntentFilter();
  String[] mAppearingPackages;
  int mChangeType;
  int mChangeUserId = 55536;
  String[] mDisappearingPackages;
  String[] mModifiedComponents;
  String[] mModifiedPackages;
  Context mRegisteredContext;
  Handler mRegisteredHandler;
  boolean mSomePackagesChanged;
  String[] mTempArray = new String[1];
  final HashSet<String> mUpdatingPackages = new HashSet();
  
  static
  {
    sNonDataFilt = new IntentFilter();
    sExternalFilt = new IntentFilter();
    sPackageFilt.addAction("android.intent.action.PACKAGE_ADDED");
    sPackageFilt.addAction("android.intent.action.PACKAGE_REMOVED");
    sPackageFilt.addAction("android.intent.action.PACKAGE_CHANGED");
    sPackageFilt.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
    sPackageFilt.addAction("android.intent.action.PACKAGE_RESTARTED");
    sPackageFilt.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
    sPackageFilt.addDataScheme("package");
    sNonDataFilt.addAction("android.intent.action.UID_REMOVED");
    sNonDataFilt.addAction("android.intent.action.USER_STOPPED");
    sNonDataFilt.addAction("android.intent.action.PACKAGES_SUSPENDED");
    sNonDataFilt.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
    sExternalFilt.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
    sExternalFilt.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
  }
  
  public PackageMonitor() {}
  
  public boolean anyPackagesAppearing()
  {
    boolean bool;
    if (mAppearingPackages != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean anyPackagesDisappearing()
  {
    boolean bool;
    if (mDisappearingPackages != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean didSomePackagesChange()
  {
    return mSomePackagesChanged;
  }
  
  public int getChangingUserId()
  {
    return mChangeUserId;
  }
  
  String getPackageName(Intent paramIntent)
  {
    paramIntent = paramIntent.getData();
    if (paramIntent != null) {
      paramIntent = paramIntent.getSchemeSpecificPart();
    } else {
      paramIntent = null;
    }
    return paramIntent;
  }
  
  public Handler getRegisteredHandler()
  {
    return mRegisteredHandler;
  }
  
  public boolean isComponentModified(String paramString)
  {
    if ((paramString != null) && (mModifiedComponents != null))
    {
      for (int i = mModifiedComponents.length - 1; i >= 0; i--) {
        if (paramString.equals(mModifiedComponents[i])) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  public int isPackageAppearing(String paramString)
  {
    if (mAppearingPackages != null) {
      for (int i = mAppearingPackages.length - 1; i >= 0; i--) {
        if (paramString.equals(mAppearingPackages[i])) {
          return mChangeType;
        }
      }
    }
    return 0;
  }
  
  public int isPackageDisappearing(String paramString)
  {
    if (mDisappearingPackages != null) {
      for (int i = mDisappearingPackages.length - 1; i >= 0; i--) {
        if (paramString.equals(mDisappearingPackages[i])) {
          return mChangeType;
        }
      }
    }
    return 0;
  }
  
  public boolean isPackageModified(String paramString)
  {
    if (mModifiedPackages != null) {
      for (int i = mModifiedPackages.length - 1; i >= 0; i--) {
        if (paramString.equals(mModifiedPackages[i])) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean isPackageUpdating(String paramString)
  {
    synchronized (mUpdatingPackages)
    {
      boolean bool = mUpdatingPackages.contains(paramString);
      return bool;
    }
  }
  
  public boolean isReplacing()
  {
    int i = mChangeType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public void onBeginPackageChanges() {}
  
  public void onFinishPackageChanges() {}
  
  public boolean onHandleForceStop(Intent paramIntent, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
  {
    return false;
  }
  
  public void onHandleUserStop(Intent paramIntent, int paramInt) {}
  
  public void onPackageAdded(String paramString, int paramInt) {}
  
  public void onPackageAppeared(String paramString, int paramInt) {}
  
  public boolean onPackageChanged(String paramString, int paramInt, String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        if (paramString.equals(paramArrayOfString[paramInt])) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void onPackageDataCleared(String paramString, int paramInt) {}
  
  public void onPackageDisappeared(String paramString, int paramInt) {}
  
  public void onPackageModified(String paramString) {}
  
  public void onPackageRemoved(String paramString, int paramInt) {}
  
  public void onPackageRemovedAllUsers(String paramString, int paramInt) {}
  
  public void onPackageUpdateFinished(String paramString, int paramInt) {}
  
  public void onPackageUpdateStarted(String paramString, int paramInt) {}
  
  public void onPackagesAvailable(String[] paramArrayOfString) {}
  
  public void onPackagesSuspended(String[] paramArrayOfString) {}
  
  public void onPackagesSuspended(String[] paramArrayOfString, Bundle paramBundle)
  {
    onPackagesSuspended(paramArrayOfString);
  }
  
  public void onPackagesUnavailable(String[] paramArrayOfString) {}
  
  public void onPackagesUnsuspended(String[] paramArrayOfString) {}
  
  public void onReceive(Context paramContext, Intent arg2)
  {
    mChangeUserId = ???.getIntExtra("android.intent.extra.user_handle", 55536);
    if (mChangeUserId == 55536)
    {
      paramContext = new StringBuilder();
      paramContext.append("Intent broadcast does not contain user handle: ");
      paramContext.append(???);
      Slog.w("PackageMonitor", paramContext.toString());
      return;
    }
    onBeginPackageChanges();
    mAppearingPackages = null;
    mDisappearingPackages = null;
    int i = 0;
    int j = 0;
    mSomePackagesChanged = false;
    mModifiedComponents = null;
    paramContext = ???.getAction();
    int k;
    if ("android.intent.action.PACKAGE_ADDED".equals(paramContext))
    {
      paramContext = getPackageName(???);
      k = ???.getIntExtra("android.intent.extra.UID", 0);
      mSomePackagesChanged = true;
      if (paramContext != null)
      {
        mAppearingPackages = mTempArray;
        mTempArray[0] = paramContext;
        if (???.getBooleanExtra("android.intent.extra.REPLACING", false))
        {
          mModifiedPackages = mTempArray;
          mChangeType = 1;
          onPackageUpdateFinished(paramContext, k);
          onPackageModified(paramContext);
        }
        else
        {
          mChangeType = 3;
          onPackageAdded(paramContext, k);
        }
        onPackageAppeared(paramContext, mChangeType);
        if (mChangeType == 1) {
          synchronized (mUpdatingPackages)
          {
            mUpdatingPackages.remove(paramContext);
          }
        }
      }
    }
    else if ("android.intent.action.PACKAGE_REMOVED".equals(paramContext))
    {
      paramContext = getPackageName(???);
      k = ???.getIntExtra("android.intent.extra.UID", 0);
      if (paramContext != null)
      {
        mDisappearingPackages = mTempArray;
        mTempArray[0] = paramContext;
        if (???.getBooleanExtra("android.intent.extra.REPLACING", false))
        {
          mChangeType = 1;
          synchronized (mUpdatingPackages)
          {
            onPackageUpdateStarted(paramContext, k);
          }
          throw paramContext;
        }
        mChangeType = 3;
        mSomePackagesChanged = true;
        onPackageRemoved(paramContext, k);
        if (???.getBooleanExtra("android.intent.extra.REMOVED_FOR_ALL_USERS", false)) {
          onPackageRemovedAllUsers(paramContext, k);
        }
        onPackageDisappeared(paramContext, mChangeType);
      }
    }
    else if ("android.intent.action.PACKAGE_CHANGED".equals(paramContext))
    {
      paramContext = getPackageName(???);
      k = ???.getIntExtra("android.intent.extra.UID", 0);
      mModifiedComponents = ???.getStringArrayExtra("android.intent.extra.changed_component_name_list");
      if (paramContext != null)
      {
        mModifiedPackages = mTempArray;
        mTempArray[0] = paramContext;
        mChangeType = 3;
        if (onPackageChanged(paramContext, k, mModifiedComponents)) {
          mSomePackagesChanged = true;
        }
        onPackageModified(paramContext);
      }
    }
    else if ("android.intent.action.PACKAGE_DATA_CLEARED".equals(paramContext))
    {
      paramContext = getPackageName(???);
      k = ???.getIntExtra("android.intent.extra.UID", 0);
      if (paramContext != null) {
        onPackageDataCleared(paramContext, k);
      }
    }
    else
    {
      boolean bool = "android.intent.action.QUERY_PACKAGE_RESTART".equals(paramContext);
      k = 2;
      if (bool)
      {
        mDisappearingPackages = ???.getStringArrayExtra("android.intent.extra.PACKAGES");
        mChangeType = 2;
        if (onHandleForceStop(???, mDisappearingPackages, ???.getIntExtra("android.intent.extra.UID", 0), false)) {
          setResultCode(-1);
        }
      }
      else if ("android.intent.action.PACKAGE_RESTARTED".equals(paramContext))
      {
        mDisappearingPackages = new String[] { getPackageName(???) };
        mChangeType = 2;
        onHandleForceStop(???, mDisappearingPackages, ???.getIntExtra("android.intent.extra.UID", 0), true);
      }
      else if ("android.intent.action.UID_REMOVED".equals(paramContext))
      {
        onUidRemoved(???.getIntExtra("android.intent.extra.UID", 0));
      }
      else if ("android.intent.action.USER_STOPPED".equals(paramContext))
      {
        if (???.hasExtra("android.intent.extra.user_handle")) {
          onHandleUserStop(???, ???.getIntExtra("android.intent.extra.user_handle", 0));
        }
      }
      else if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(paramContext))
      {
        paramContext = ???.getStringArrayExtra("android.intent.extra.changed_package_list");
        mAppearingPackages = paramContext;
        if (???.getBooleanExtra("android.intent.extra.REPLACING", false)) {
          k = 1;
        }
        mChangeType = k;
        mSomePackagesChanged = true;
        if (paramContext != null)
        {
          onPackagesAvailable(paramContext);
          for (k = j; k < paramContext.length; k++) {
            onPackageAppeared(paramContext[k], mChangeType);
          }
        }
      }
      else if ("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(paramContext))
      {
        paramContext = ???.getStringArrayExtra("android.intent.extra.changed_package_list");
        mDisappearingPackages = paramContext;
        if (???.getBooleanExtra("android.intent.extra.REPLACING", false)) {
          k = 1;
        }
        mChangeType = k;
        mSomePackagesChanged = true;
        if (paramContext != null)
        {
          onPackagesUnavailable(paramContext);
          for (k = i; k < paramContext.length; k++) {
            onPackageDisappeared(paramContext[k], mChangeType);
          }
        }
      }
      else if ("android.intent.action.PACKAGES_SUSPENDED".equals(paramContext))
      {
        paramContext = ???.getStringArrayExtra("android.intent.extra.changed_package_list");
        ??? = ???.getBundleExtra("android.intent.extra.LAUNCHER_EXTRAS");
        mSomePackagesChanged = true;
        onPackagesSuspended(paramContext, ???);
      }
      else if ("android.intent.action.PACKAGES_UNSUSPENDED".equals(paramContext))
      {
        paramContext = ???.getStringArrayExtra("android.intent.extra.changed_package_list");
        mSomePackagesChanged = true;
        onPackagesUnsuspended(paramContext);
      }
    }
    if (mSomePackagesChanged) {
      onSomePackagesChanged();
    }
    onFinishPackageChanges();
    mChangeUserId = 55536;
  }
  
  public void onSomePackagesChanged() {}
  
  public void onUidRemoved(int paramInt) {}
  
  public void register(Context paramContext, Looper paramLooper, UserHandle paramUserHandle, boolean paramBoolean)
  {
    if (paramLooper == null) {
      paramLooper = BackgroundThread.getHandler();
    } else {
      paramLooper = new Handler(paramLooper);
    }
    register(paramContext, paramUserHandle, paramBoolean, paramLooper);
  }
  
  public void register(Context paramContext, Looper paramLooper, boolean paramBoolean)
  {
    register(paramContext, paramLooper, null, paramBoolean);
  }
  
  public void register(Context paramContext, UserHandle paramUserHandle, boolean paramBoolean, Handler paramHandler)
  {
    if (mRegisteredContext == null)
    {
      mRegisteredContext = paramContext;
      mRegisteredHandler = ((Handler)Preconditions.checkNotNull(paramHandler));
      if (paramUserHandle != null)
      {
        paramContext.registerReceiverAsUser(this, paramUserHandle, sPackageFilt, null, mRegisteredHandler);
        paramContext.registerReceiverAsUser(this, paramUserHandle, sNonDataFilt, null, mRegisteredHandler);
        if (paramBoolean) {
          paramContext.registerReceiverAsUser(this, paramUserHandle, sExternalFilt, null, mRegisteredHandler);
        }
      }
      else
      {
        paramContext.registerReceiver(this, sPackageFilt, null, mRegisteredHandler);
        paramContext.registerReceiver(this, sNonDataFilt, null, mRegisteredHandler);
        if (paramBoolean) {
          paramContext.registerReceiver(this, sExternalFilt, null, mRegisteredHandler);
        }
      }
      return;
    }
    throw new IllegalStateException("Already registered");
  }
  
  public void unregister()
  {
    if (mRegisteredContext != null)
    {
      mRegisteredContext.unregisterReceiver(this);
      mRegisteredContext = null;
      return;
    }
    throw new IllegalStateException("Not registered");
  }
}
