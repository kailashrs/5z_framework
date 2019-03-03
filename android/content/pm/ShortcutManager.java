package android.content.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;

public class ShortcutManager
{
  private static final String TAG = "ShortcutManager";
  private final Context mContext;
  private final IShortcutService mService;
  
  public ShortcutManager(Context paramContext)
  {
    this(paramContext, IShortcutService.Stub.asInterface(ServiceManager.getService("shortcut")));
  }
  
  public ShortcutManager(Context paramContext, IShortcutService paramIShortcutService)
  {
    mContext = paramContext;
    mService = paramIShortcutService;
  }
  
  public boolean addDynamicShortcuts(List<ShortcutInfo> paramList)
  {
    try
    {
      IShortcutService localIShortcutService = mService;
      String str = mContext.getPackageName();
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(paramList);
      boolean bool = localIShortcutService.addDynamicShortcuts(str, localParceledListSlice, injectMyUserId());
      return bool;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public Intent createShortcutResultIntent(ShortcutInfo paramShortcutInfo)
  {
    try
    {
      paramShortcutInfo = mService.createShortcutResultIntent(mContext.getPackageName(), paramShortcutInfo, injectMyUserId());
      return paramShortcutInfo;
    }
    catch (RemoteException paramShortcutInfo)
    {
      throw paramShortcutInfo.rethrowFromSystemServer();
    }
  }
  
  public void disableShortcuts(List<String> paramList)
  {
    try
    {
      mService.disableShortcuts(mContext.getPackageName(), paramList, null, 0, injectMyUserId());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void disableShortcuts(List<String> paramList, int paramInt)
  {
    try
    {
      mService.disableShortcuts(mContext.getPackageName(), paramList, null, paramInt, injectMyUserId());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void disableShortcuts(List<String> paramList, CharSequence paramCharSequence)
  {
    try
    {
      mService.disableShortcuts(mContext.getPackageName(), paramList, paramCharSequence, 0, injectMyUserId());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void disableShortcuts(List<String> paramList, String paramString)
  {
    disableShortcuts(paramList, paramString);
  }
  
  public void enableShortcuts(List<String> paramList)
  {
    try
    {
      mService.enableShortcuts(mContext.getPackageName(), paramList, injectMyUserId());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public List<ShortcutInfo> getDynamicShortcuts()
  {
    try
    {
      List localList = mService.getDynamicShortcuts(mContext.getPackageName(), injectMyUserId()).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getIconMaxHeight()
  {
    try
    {
      int i = mService.getIconMaxDimensions(mContext.getPackageName(), injectMyUserId());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getIconMaxWidth()
  {
    try
    {
      int i = mService.getIconMaxDimensions(mContext.getPackageName(), injectMyUserId());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ShortcutInfo> getManifestShortcuts()
  {
    try
    {
      List localList = mService.getManifestShortcuts(mContext.getPackageName(), injectMyUserId()).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getMaxShortcutCountForActivity()
  {
    return getMaxShortcutCountPerActivity();
  }
  
  public int getMaxShortcutCountPerActivity()
  {
    try
    {
      int i = mService.getMaxShortcutCountPerActivity(mContext.getPackageName(), injectMyUserId());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ShortcutInfo> getPinnedShortcuts()
  {
    try
    {
      List localList = mService.getPinnedShortcuts(mContext.getPackageName(), injectMyUserId()).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getRateLimitResetTime()
  {
    try
    {
      long l = mService.getRateLimitResetTime(mContext.getPackageName(), injectMyUserId());
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getRemainingCallCount()
  {
    try
    {
      int i = mService.getRemainingCallCount(mContext.getPackageName(), injectMyUserId());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @VisibleForTesting
  protected int injectMyUserId()
  {
    return mContext.getUserId();
  }
  
  public boolean isRateLimitingActive()
  {
    try
    {
      int i = mService.getRemainingCallCount(mContext.getPackageName(), injectMyUserId());
      boolean bool;
      if (i == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isRequestPinShortcutSupported()
  {
    try
    {
      boolean bool = mService.isRequestPinItemSupported(injectMyUserId(), 1);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void onApplicationActive(String paramString, int paramInt)
  {
    try
    {
      mService.onApplicationActive(paramString, paramInt);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeAllDynamicShortcuts()
  {
    try
    {
      mService.removeAllDynamicShortcuts(mContext.getPackageName(), injectMyUserId());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void removeDynamicShortcuts(List<String> paramList)
  {
    try
    {
      mService.removeDynamicShortcuts(mContext.getPackageName(), paramList, injectMyUserId());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void reportShortcutUsed(String paramString)
  {
    try
    {
      mService.reportShortcutUsed(mContext.getPackageName(), paramString, injectMyUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean requestPinShortcut(ShortcutInfo paramShortcutInfo, IntentSender paramIntentSender)
  {
    try
    {
      boolean bool = mService.requestPinShortcut(mContext.getPackageName(), paramShortcutInfo, paramIntentSender, injectMyUserId());
      return bool;
    }
    catch (RemoteException paramShortcutInfo)
    {
      throw paramShortcutInfo.rethrowFromSystemServer();
    }
  }
  
  public boolean setDynamicShortcuts(List<ShortcutInfo> paramList)
  {
    try
    {
      IShortcutService localIShortcutService = mService;
      String str = mContext.getPackageName();
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(paramList);
      boolean bool = localIShortcutService.setDynamicShortcuts(str, localParceledListSlice, injectMyUserId());
      return bool;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public boolean updateShortcuts(List<ShortcutInfo> paramList)
  {
    try
    {
      IShortcutService localIShortcutService = mService;
      String str = mContext.getPackageName();
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(paramList);
      boolean bool = localIShortcutService.updateShortcuts(str, localParceledListSlice, injectMyUserId());
      return bool;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
}
