package android.content.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LauncherApps
{
  public static final String ACTION_CONFIRM_PIN_APPWIDGET = "android.content.pm.action.CONFIRM_PIN_APPWIDGET";
  public static final String ACTION_CONFIRM_PIN_SHORTCUT = "android.content.pm.action.CONFIRM_PIN_SHORTCUT";
  static final boolean DEBUG = false;
  public static final String EXTRA_PIN_ITEM_REQUEST = "android.content.pm.extra.PIN_ITEM_REQUEST";
  static final String TAG = "LauncherApps";
  private IOnAppsChangedListener.Stub mAppsChangedListener = new IOnAppsChangedListener.Stub()
  {
    public void onPackageAdded(UserHandle paramAnonymousUserHandle, String paramAnonymousString)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackageAdded(paramAnonymousString, paramAnonymousUserHandle);
        }
        return;
      }
    }
    
    public void onPackageChanged(UserHandle paramAnonymousUserHandle, String paramAnonymousString)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackageChanged(paramAnonymousString, paramAnonymousUserHandle);
        }
        return;
      }
    }
    
    public void onPackageRemoved(UserHandle paramAnonymousUserHandle, String paramAnonymousString)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackageRemoved(paramAnonymousString, paramAnonymousUserHandle);
        }
        return;
      }
    }
    
    public void onPackagesAvailable(UserHandle paramAnonymousUserHandle, String[] paramAnonymousArrayOfString, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackagesAvailable(paramAnonymousArrayOfString, paramAnonymousUserHandle, paramAnonymousBoolean);
        }
        return;
      }
    }
    
    public void onPackagesSuspended(UserHandle paramAnonymousUserHandle, String[] paramAnonymousArrayOfString, Bundle paramAnonymousBundle)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackagesSuspended(paramAnonymousArrayOfString, paramAnonymousBundle, paramAnonymousUserHandle);
        }
        return;
      }
    }
    
    public void onPackagesUnavailable(UserHandle paramAnonymousUserHandle, String[] paramAnonymousArrayOfString, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackagesUnavailable(paramAnonymousArrayOfString, paramAnonymousUserHandle, paramAnonymousBoolean);
        }
        return;
      }
    }
    
    public void onPackagesUnsuspended(UserHandle paramAnonymousUserHandle, String[] paramAnonymousArrayOfString)
      throws RemoteException
    {
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnPackagesUnsuspended(paramAnonymousArrayOfString, paramAnonymousUserHandle);
        }
        return;
      }
    }
    
    public void onShortcutChanged(UserHandle paramAnonymousUserHandle, String paramAnonymousString, ParceledListSlice arg3)
    {
      List localList = ???.getList();
      synchronized (LauncherApps.this)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((LauncherApps.CallbackMessageHandler)localIterator.next()).postOnShortcutChanged(paramAnonymousString, paramAnonymousUserHandle, localList);
        }
        return;
      }
    }
  };
  private List<CallbackMessageHandler> mCallbacks = new ArrayList();
  private final Context mContext;
  private final PackageManager mPm;
  private final ILauncherApps mService;
  private final UserManager mUserManager;
  
  public LauncherApps(Context paramContext)
  {
    this(paramContext, ILauncherApps.Stub.asInterface(ServiceManager.getService("launcherapps")));
  }
  
  public LauncherApps(Context paramContext, ILauncherApps paramILauncherApps)
  {
    mContext = paramContext;
    mService = paramILauncherApps;
    mPm = paramContext.getPackageManager();
    mUserManager = ((UserManager)paramContext.getSystemService(UserManager.class));
  }
  
  private void addCallbackLocked(Callback paramCallback, Handler paramHandler)
  {
    removeCallbackLocked(paramCallback);
    Handler localHandler = paramHandler;
    if (paramHandler == null) {
      localHandler = new Handler();
    }
    paramCallback = new CallbackMessageHandler(localHandler.getLooper(), paramCallback);
    mCallbacks.add(paramCallback);
  }
  
  private List<LauncherActivityInfo> convertToActivityList(ParceledListSlice<ResolveInfo> paramParceledListSlice, UserHandle paramUserHandle)
  {
    if (paramParceledListSlice == null) {
      return Collections.EMPTY_LIST;
    }
    ArrayList localArrayList = new ArrayList();
    paramParceledListSlice = paramParceledListSlice.getList().iterator();
    while (paramParceledListSlice.hasNext())
    {
      ResolveInfo localResolveInfo = (ResolveInfo)paramParceledListSlice.next();
      localArrayList.add(new LauncherActivityInfo(mContext, activityInfo, paramUserHandle));
    }
    return localArrayList;
  }
  
  private int findCallbackLocked(Callback paramCallback)
  {
    if (paramCallback != null)
    {
      int i = mCallbacks.size();
      for (int j = 0; j < i; j++) {
        if (mCallbacks.get(j)).mCallback == paramCallback) {
          return j;
        }
      }
      return -1;
    }
    throw new IllegalArgumentException("Callback cannot be null");
  }
  
  private ParcelFileDescriptor getShortcutIconFd(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      paramString1 = mService.getShortcutIconFd(mContext.getPackageName(), paramString1, paramString2, paramInt);
      return paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  private Drawable loadDrawableResourceFromPackage(String paramString, int paramInt1, UserHandle paramUserHandle, int paramInt2)
  {
    if (paramInt1 == 0) {
      return null;
    }
    try
    {
      paramString = getApplicationInfo(paramString, 0, paramUserHandle);
      paramString = mContext.getPackageManager().getResourcesForApplication(paramString).getDrawableForDensity(paramInt1, paramInt2);
      return paramString;
    }
    catch (PackageManager.NameNotFoundException|Resources.NotFoundException paramString) {}
    return null;
  }
  
  private void logErrorForInvalidProfileAccess(UserHandle paramUserHandle)
  {
    if ((UserHandle.myUserId() != paramUserHandle.getIdentifier()) && (mUserManager.isManagedProfile())) {
      Log.w("LauncherApps", "Accessing other profiles/users from managed profile is no longer allowed.");
    }
  }
  
  private List<ShortcutInfo> maybeUpdateDisabledMessage(List<ShortcutInfo> paramList)
  {
    if (paramList == null) {
      return null;
    }
    for (int i = paramList.size() - 1; i >= 0; i--)
    {
      ShortcutInfo localShortcutInfo = (ShortcutInfo)paramList.get(i);
      String str = ShortcutInfo.getDisabledReasonForRestoreIssue(mContext, localShortcutInfo.getDisabledReason());
      if (str != null) {
        localShortcutInfo.setDisabledMessage(str);
      }
    }
    return paramList;
  }
  
  private void removeCallbackLocked(Callback paramCallback)
  {
    int i = findCallbackLocked(paramCallback);
    if (i >= 0) {
      mCallbacks.remove(i);
    }
  }
  
  private void startShortcut(String paramString1, String paramString2, Rect paramRect, Bundle paramBundle, int paramInt)
  {
    try
    {
      if (mService.startShortcut(mContext.getPackageName(), paramString1, paramString2, paramRect, paramBundle, paramInt)) {
        return;
      }
      paramString1 = new android/content/ActivityNotFoundException;
      paramString1.<init>("Shortcut could not be started");
      throw paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public List<LauncherActivityInfo> getActivityList(String paramString, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      paramString = convertToActivityList(mService.getLauncherActivities(mContext.getPackageName(), paramString, paramUserHandle), paramUserHandle);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public ApplicationInfo getApplicationInfo(String paramString, int paramInt, UserHandle paramUserHandle)
    throws PackageManager.NameNotFoundException
  {
    Preconditions.checkNotNull(paramString, "packageName");
    Preconditions.checkNotNull(paramUserHandle, "user");
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      Object localObject = mService.getApplicationInfo(mContext.getPackageName(), paramString, paramInt, paramUserHandle);
      if (localObject != null) {
        return localObject;
      }
      localObject = new android/content/pm/PackageManager$NameNotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Package ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" not found for user ");
      localStringBuilder.append(paramUserHandle.getIdentifier());
      ((PackageManager.NameNotFoundException)localObject).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public PinItemRequest getPinItemRequest(Intent paramIntent)
  {
    return (PinItemRequest)paramIntent.getParcelableExtra("android.content.pm.extra.PIN_ITEM_REQUEST");
  }
  
  public List<UserHandle> getProfiles()
  {
    if (mUserManager.isManagedProfile())
    {
      ArrayList localArrayList = new ArrayList(1);
      localArrayList.add(Process.myUserHandle());
      return localArrayList;
    }
    return mUserManager.getUserProfiles();
  }
  
  public Drawable getShortcutBadgedIconDrawable(ShortcutInfo paramShortcutInfo, int paramInt)
  {
    Drawable localDrawable = getShortcutIconDrawable(paramShortcutInfo, paramInt);
    if (localDrawable == null) {
      paramShortcutInfo = null;
    } else {
      paramShortcutInfo = mContext.getPackageManager().getUserBadgedIcon(localDrawable, paramShortcutInfo.getUserHandle());
    }
    return paramShortcutInfo;
  }
  
  public IntentSender getShortcutConfigActivityIntent(LauncherActivityInfo paramLauncherActivityInfo)
  {
    try
    {
      paramLauncherActivityInfo = mService.getShortcutConfigActivityIntent(mContext.getPackageName(), paramLauncherActivityInfo.getComponentName(), paramLauncherActivityInfo.getUser());
      return paramLauncherActivityInfo;
    }
    catch (RemoteException paramLauncherActivityInfo)
    {
      throw paramLauncherActivityInfo.rethrowFromSystemServer();
    }
  }
  
  public List<LauncherActivityInfo> getShortcutConfigActivityList(String paramString, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      paramString = convertToActivityList(mService.getShortcutConfigActivities(mContext.getPackageName(), paramString, paramUserHandle), paramUserHandle);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Drawable getShortcutIconDrawable(ShortcutInfo paramShortcutInfo, int paramInt)
  {
    if (paramShortcutInfo.hasIconFile())
    {
      ParcelFileDescriptor localParcelFileDescriptor = getShortcutIconFd(paramShortcutInfo);
      if (localParcelFileDescriptor == null) {
        return null;
      }
      try
      {
        Bitmap localBitmap = BitmapFactory.decodeFileDescriptor(localParcelFileDescriptor.getFileDescriptor());
        if (localBitmap != null)
        {
          BitmapDrawable localBitmapDrawable = new android/graphics/drawable/BitmapDrawable;
          localBitmapDrawable.<init>(mContext.getResources(), localBitmap);
          if (paramShortcutInfo.hasAdaptiveBitmap())
          {
            paramShortcutInfo = new AdaptiveIconDrawable(null, localBitmapDrawable);
            return paramShortcutInfo;
          }
          return localBitmapDrawable;
        }
        return null;
      }
      finally
      {
        try
        {
          localIOException1.close();
        }
        catch (IOException localIOException2) {}
      }
    }
    if (paramShortcutInfo.hasIconResource()) {
      return loadDrawableResourceFromPackage(paramShortcutInfo.getPackage(), paramShortcutInfo.getIconResourceId(), paramShortcutInfo.getUserHandle(), paramInt);
    }
    if (paramShortcutInfo.getIcon() != null)
    {
      Icon localIcon = paramShortcutInfo.getIcon();
      int i = localIcon.getType();
      if (i != 5) {
        switch (i)
        {
        default: 
          return null;
        case 2: 
          return loadDrawableResourceFromPackage(paramShortcutInfo.getPackage(), localIcon.getResId(), paramShortcutInfo.getUserHandle(), paramInt);
        }
      }
      return localIcon.loadDrawable(mContext);
    }
    return null;
  }
  
  public ParcelFileDescriptor getShortcutIconFd(ShortcutInfo paramShortcutInfo)
  {
    return getShortcutIconFd(paramShortcutInfo.getPackage(), paramShortcutInfo.getId(), paramShortcutInfo.getUserId());
  }
  
  public ParcelFileDescriptor getShortcutIconFd(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    return getShortcutIconFd(paramString1, paramString2, paramUserHandle.getIdentifier());
  }
  
  @Deprecated
  public int getShortcutIconResId(ShortcutInfo paramShortcutInfo)
  {
    return paramShortcutInfo.getIconResourceId();
  }
  
  @Deprecated
  public int getShortcutIconResId(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    ShortcutQuery localShortcutQuery = new ShortcutQuery();
    localShortcutQuery.setPackage(paramString1);
    int i = 0;
    localShortcutQuery.setShortcutIds(Arrays.asList(new String[] { paramString2 }));
    localShortcutQuery.setQueryFlags(11);
    paramString1 = getShortcuts(localShortcutQuery, paramUserHandle);
    if (paramString1.size() > 0) {
      i = ((ShortcutInfo)paramString1.get(0)).getIconResourceId();
    }
    return i;
  }
  
  @Deprecated
  public List<ShortcutInfo> getShortcutInfo(String paramString, List<String> paramList, UserHandle paramUserHandle)
  {
    ShortcutQuery localShortcutQuery = new ShortcutQuery();
    localShortcutQuery.setPackage(paramString);
    localShortcutQuery.setShortcutIds(paramList);
    localShortcutQuery.setQueryFlags(11);
    return getShortcuts(localShortcutQuery, paramUserHandle);
  }
  
  public List<ShortcutInfo> getShortcuts(ShortcutQuery paramShortcutQuery, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      paramShortcutQuery = maybeUpdateDisabledMessage(mService.getShortcuts(mContext.getPackageName(), mChangedSince, mPackage, mShortcutIds, mActivity, mQueryFlags, paramUserHandle).getList());
      return paramShortcutQuery;
    }
    catch (RemoteException paramShortcutQuery)
    {
      throw paramShortcutQuery.rethrowFromSystemServer();
    }
  }
  
  public Bundle getSuspendedPackageLauncherExtras(String paramString, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      paramString = mService.getSuspendedPackageLauncherExtras(paramString, paramUserHandle);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean hasShortcutHostPermission()
  {
    try
    {
      boolean bool = mService.hasShortcutHostPermission(mContext.getPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isActivityEnabled(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      boolean bool = mService.isActivityEnabled(mContext.getPackageName(), paramComponentName, paramUserHandle);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isPackageEnabled(String paramString, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      boolean bool = mService.isPackageEnabled(mContext.getPackageName(), paramString, paramUserHandle);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void pinShortcuts(String paramString, List<String> paramList, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      mService.pinShortcuts(mContext.getPackageName(), paramString, paramList, paramUserHandle);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, null);
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null) {
      try
      {
        if (findCallbackLocked(paramCallback) < 0)
        {
          int i;
          if (mCallbacks.size() == 0) {
            i = 1;
          } else {
            i = 0;
          }
          addCallbackLocked(paramCallback, paramHandler);
          if (i != 0) {
            try
            {
              mService.addOnAppsChangedListener(mContext.getPackageName(), mAppsChangedListener);
            }
            catch (RemoteException paramCallback)
            {
              throw paramCallback.rethrowFromSystemServer();
            }
          }
        }
      }
      finally
      {
        break label79;
      }
    }
    return;
    label79:
    throw paramCallback;
  }
  
  public LauncherActivityInfo resolveActivity(Intent paramIntent, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      paramIntent = mService.resolveActivity(mContext.getPackageName(), paramIntent.getComponent(), paramUserHandle);
      if (paramIntent != null)
      {
        paramIntent = new LauncherActivityInfo(mContext, paramIntent, paramUserHandle);
        return paramIntent;
      }
      return null;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void startAppDetailsActivity(ComponentName paramComponentName, UserHandle paramUserHandle, Rect paramRect, Bundle paramBundle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      mService.showAppDetailsAsUser(mContext.getIApplicationThread(), mContext.getPackageName(), paramComponentName, paramRect, paramBundle, paramUserHandle);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void startMainActivity(ComponentName paramComponentName, UserHandle paramUserHandle, Rect paramRect, Bundle paramBundle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    try
    {
      mService.startActivityAsUser(mContext.getIApplicationThread(), mContext.getPackageName(), paramComponentName, paramRect, paramBundle, paramUserHandle);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void startShortcut(ShortcutInfo paramShortcutInfo, Rect paramRect, Bundle paramBundle)
  {
    startShortcut(paramShortcutInfo.getPackage(), paramShortcutInfo.getId(), paramRect, paramBundle, paramShortcutInfo.getUserId());
  }
  
  public void startShortcut(String paramString1, String paramString2, Rect paramRect, Bundle paramBundle, UserHandle paramUserHandle)
  {
    logErrorForInvalidProfileAccess(paramUserHandle);
    startShortcut(paramString1, paramString2, paramRect, paramBundle, paramUserHandle.getIdentifier());
  }
  
  public void unregisterCallback(Callback paramCallback)
  {
    try
    {
      removeCallbackLocked(paramCallback);
      int i = mCallbacks.size();
      if (i == 0) {
        try
        {
          mService.removeOnAppsChangedListener(mAppsChangedListener);
        }
        catch (RemoteException paramCallback)
        {
          throw paramCallback.rethrowFromSystemServer();
        }
      }
      return;
    }
    finally {}
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onPackageAdded(String paramString, UserHandle paramUserHandle);
    
    public abstract void onPackageChanged(String paramString, UserHandle paramUserHandle);
    
    public abstract void onPackageRemoved(String paramString, UserHandle paramUserHandle);
    
    public abstract void onPackagesAvailable(String[] paramArrayOfString, UserHandle paramUserHandle, boolean paramBoolean);
    
    public void onPackagesSuspended(String[] paramArrayOfString, UserHandle paramUserHandle) {}
    
    public void onPackagesSuspended(String[] paramArrayOfString, UserHandle paramUserHandle, Bundle paramBundle)
    {
      onPackagesSuspended(paramArrayOfString, paramUserHandle);
    }
    
    public abstract void onPackagesUnavailable(String[] paramArrayOfString, UserHandle paramUserHandle, boolean paramBoolean);
    
    public void onPackagesUnsuspended(String[] paramArrayOfString, UserHandle paramUserHandle) {}
    
    public void onShortcutsChanged(String paramString, List<ShortcutInfo> paramList, UserHandle paramUserHandle) {}
  }
  
  private static class CallbackMessageHandler
    extends Handler
  {
    private static final int MSG_ADDED = 1;
    private static final int MSG_AVAILABLE = 4;
    private static final int MSG_CHANGED = 3;
    private static final int MSG_REMOVED = 2;
    private static final int MSG_SHORTCUT_CHANGED = 8;
    private static final int MSG_SUSPENDED = 6;
    private static final int MSG_UNAVAILABLE = 5;
    private static final int MSG_UNSUSPENDED = 7;
    private LauncherApps.Callback mCallback;
    
    public CallbackMessageHandler(Looper paramLooper, LauncherApps.Callback paramCallback)
    {
      super(null, true);
      mCallback = paramCallback;
    }
    
    public void handleMessage(Message paramMessage)
    {
      if ((mCallback != null) && ((obj instanceof CallbackInfo)))
      {
        CallbackInfo localCallbackInfo = (CallbackInfo)obj;
        switch (what)
        {
        default: 
          break;
        case 8: 
          mCallback.onShortcutsChanged(packageName, shortcuts, user);
          break;
        case 7: 
          mCallback.onPackagesUnsuspended(packageNames, user);
          break;
        case 6: 
          mCallback.onPackagesSuspended(packageNames, user, launcherExtras);
          break;
        case 5: 
          mCallback.onPackagesUnavailable(packageNames, user, replacing);
          break;
        case 4: 
          mCallback.onPackagesAvailable(packageNames, user, replacing);
          break;
        case 3: 
          mCallback.onPackageChanged(packageName, user);
          break;
        case 2: 
          mCallback.onPackageRemoved(packageName, user);
          break;
        case 1: 
          mCallback.onPackageAdded(packageName, user);
        }
        return;
      }
    }
    
    public void postOnPackageAdded(String paramString, UserHandle paramUserHandle)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageName = paramString;
      user = paramUserHandle;
      obtainMessage(1, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackageChanged(String paramString, UserHandle paramUserHandle)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageName = paramString;
      user = paramUserHandle;
      obtainMessage(3, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackageRemoved(String paramString, UserHandle paramUserHandle)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageName = paramString;
      user = paramUserHandle;
      obtainMessage(2, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackagesAvailable(String[] paramArrayOfString, UserHandle paramUserHandle, boolean paramBoolean)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageNames = paramArrayOfString;
      replacing = paramBoolean;
      user = paramUserHandle;
      obtainMessage(4, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackagesSuspended(String[] paramArrayOfString, Bundle paramBundle, UserHandle paramUserHandle)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageNames = paramArrayOfString;
      user = paramUserHandle;
      launcherExtras = paramBundle;
      obtainMessage(6, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackagesUnavailable(String[] paramArrayOfString, UserHandle paramUserHandle, boolean paramBoolean)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageNames = paramArrayOfString;
      replacing = paramBoolean;
      user = paramUserHandle;
      obtainMessage(5, localCallbackInfo).sendToTarget();
    }
    
    public void postOnPackagesUnsuspended(String[] paramArrayOfString, UserHandle paramUserHandle)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageNames = paramArrayOfString;
      user = paramUserHandle;
      obtainMessage(7, localCallbackInfo).sendToTarget();
    }
    
    public void postOnShortcutChanged(String paramString, UserHandle paramUserHandle, List<ShortcutInfo> paramList)
    {
      CallbackInfo localCallbackInfo = new CallbackInfo(null);
      packageName = paramString;
      user = paramUserHandle;
      shortcuts = paramList;
      obtainMessage(8, localCallbackInfo).sendToTarget();
    }
    
    private static class CallbackInfo
    {
      Bundle launcherExtras;
      String packageName;
      String[] packageNames;
      boolean replacing;
      List<ShortcutInfo> shortcuts;
      UserHandle user;
      
      private CallbackInfo() {}
    }
  }
  
  public static final class PinItemRequest
    implements Parcelable
  {
    public static final Parcelable.Creator<PinItemRequest> CREATOR = new Parcelable.Creator()
    {
      public LauncherApps.PinItemRequest createFromParcel(Parcel paramAnonymousParcel)
      {
        return new LauncherApps.PinItemRequest(paramAnonymousParcel, null);
      }
      
      public LauncherApps.PinItemRequest[] newArray(int paramAnonymousInt)
      {
        return new LauncherApps.PinItemRequest[paramAnonymousInt];
      }
    };
    public static final int REQUEST_TYPE_APPWIDGET = 2;
    public static final int REQUEST_TYPE_SHORTCUT = 1;
    private final IPinItemRequest mInner;
    private final int mRequestType;
    
    public PinItemRequest(IPinItemRequest paramIPinItemRequest, int paramInt)
    {
      mInner = paramIPinItemRequest;
      mRequestType = paramInt;
    }
    
    private PinItemRequest(Parcel paramParcel)
    {
      getClass().getClassLoader();
      mRequestType = paramParcel.readInt();
      mInner = IPinItemRequest.Stub.asInterface(paramParcel.readStrongBinder());
    }
    
    public boolean accept()
    {
      return accept(null);
    }
    
    public boolean accept(Bundle paramBundle)
    {
      try
      {
        boolean bool = mInner.accept(paramBundle);
        return bool;
      }
      catch (RemoteException paramBundle)
      {
        throw paramBundle.rethrowFromSystemServer();
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public AppWidgetProviderInfo getAppWidgetProviderInfo(Context paramContext)
    {
      try
      {
        AppWidgetProviderInfo localAppWidgetProviderInfo = mInner.getAppWidgetProviderInfo();
        if (localAppWidgetProviderInfo == null) {
          return null;
        }
        localAppWidgetProviderInfo.updateDimensions(paramContext.getResources().getDisplayMetrics());
        return localAppWidgetProviderInfo;
      }
      catch (RemoteException paramContext)
      {
        throw paramContext.rethrowAsRuntimeException();
      }
    }
    
    public Bundle getExtras()
    {
      try
      {
        Bundle localBundle = mInner.getExtras();
        return localBundle;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowAsRuntimeException();
      }
    }
    
    public int getRequestType()
    {
      return mRequestType;
    }
    
    public ShortcutInfo getShortcutInfo()
    {
      try
      {
        ShortcutInfo localShortcutInfo = mInner.getShortcutInfo();
        return localShortcutInfo;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowAsRuntimeException();
      }
    }
    
    public boolean isValid()
    {
      try
      {
        boolean bool = mInner.isValid();
        return bool;
      }
      catch (RemoteException localRemoteException) {}
      return false;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mRequestType);
      paramParcel.writeStrongBinder(mInner.asBinder());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface RequestType {}
  }
  
  public static class ShortcutQuery
  {
    @Deprecated
    public static final int FLAG_GET_ALL_KINDS = 11;
    @Deprecated
    public static final int FLAG_GET_DYNAMIC = 1;
    public static final int FLAG_GET_KEY_FIELDS_ONLY = 4;
    @Deprecated
    public static final int FLAG_GET_MANIFEST = 8;
    @Deprecated
    public static final int FLAG_GET_PINNED = 2;
    public static final int FLAG_MATCH_ALL_KINDS = 11;
    public static final int FLAG_MATCH_ALL_KINDS_WITH_ALL_PINNED = 1035;
    public static final int FLAG_MATCH_DYNAMIC = 1;
    public static final int FLAG_MATCH_MANIFEST = 8;
    public static final int FLAG_MATCH_PINNED = 2;
    public static final int FLAG_MATCH_PINNED_BY_ANY_LAUNCHER = 1024;
    ComponentName mActivity;
    long mChangedSince;
    String mPackage;
    int mQueryFlags;
    List<String> mShortcutIds;
    
    public ShortcutQuery() {}
    
    public ShortcutQuery setActivity(ComponentName paramComponentName)
    {
      mActivity = paramComponentName;
      return this;
    }
    
    public ShortcutQuery setChangedSince(long paramLong)
    {
      mChangedSince = paramLong;
      return this;
    }
    
    public ShortcutQuery setPackage(String paramString)
    {
      mPackage = paramString;
      return this;
    }
    
    public ShortcutQuery setQueryFlags(int paramInt)
    {
      mQueryFlags = paramInt;
      return this;
    }
    
    public ShortcutQuery setShortcutIds(List<String> paramList)
    {
      mShortcutIds = paramList;
      return this;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface QueryFlags {}
  }
}
