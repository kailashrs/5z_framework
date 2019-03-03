package android.companion;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public final class CompanionDeviceManager
{
  public static final String COMPANION_DEVICE_DISCOVERY_PACKAGE_NAME = "com.android.companiondevicemanager";
  private static final boolean DEBUG = false;
  public static final String EXTRA_DEVICE = "android.companion.extra.DEVICE";
  private static final String LOG_TAG = "CompanionDeviceManager";
  private final Context mContext;
  private final ICompanionDeviceManager mService;
  
  public CompanionDeviceManager(ICompanionDeviceManager paramICompanionDeviceManager, Context paramContext)
  {
    mService = paramICompanionDeviceManager;
    mContext = paramContext;
  }
  
  private boolean checkFeaturePresent()
  {
    boolean bool;
    if (mService != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private Activity getActivity()
  {
    return (Activity)mContext;
  }
  
  private String getCallingPackage()
  {
    return mContext.getPackageName();
  }
  
  public void associate(AssociationRequest paramAssociationRequest, Callback paramCallback, Handler paramHandler)
  {
    if (!checkFeaturePresent()) {
      return;
    }
    Preconditions.checkNotNull(paramAssociationRequest, "Request cannot be null");
    Preconditions.checkNotNull(paramCallback, "Callback cannot be null");
    try
    {
      ICompanionDeviceManager localICompanionDeviceManager = mService;
      CallbackProxy localCallbackProxy = new android/companion/CompanionDeviceManager$CallbackProxy;
      localCallbackProxy.<init>(this, paramAssociationRequest, paramCallback, Handler.mainIfNull(paramHandler), null);
      localICompanionDeviceManager.associate(paramAssociationRequest, localCallbackProxy, getCallingPackage());
      return;
    }
    catch (RemoteException paramAssociationRequest)
    {
      throw paramAssociationRequest.rethrowFromSystemServer();
    }
  }
  
  public void disassociate(String paramString)
  {
    if (!checkFeaturePresent()) {
      return;
    }
    try
    {
      mService.disassociate(paramString, getCallingPackage());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<String> getAssociations()
  {
    if (!checkFeaturePresent()) {
      return Collections.emptyList();
    }
    try
    {
      List localList = mService.getAssociations(getCallingPackage(), mContext.getUserId());
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean hasNotificationAccess(ComponentName paramComponentName)
  {
    if (!checkFeaturePresent()) {
      return false;
    }
    try
    {
      boolean bool = mService.hasNotificationAccess(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void requestNotificationAccess(ComponentName paramComponentName)
  {
    if (!checkFeaturePresent()) {
      return;
    }
    try
    {
      paramComponentName = mService.requestNotificationAccess(paramComponentName).getIntentSender();
      mContext.startIntentSender(paramComponentName, null, 0, 0, 0);
      return;
    }
    catch (IntentSender.SendIntentException paramComponentName)
    {
      throw new RuntimeException(paramComponentName);
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onDeviceFound(IntentSender paramIntentSender);
    
    public abstract void onFailure(CharSequence paramCharSequence);
  }
  
  private class CallbackProxy
    extends IFindDeviceCallback.Stub
    implements Application.ActivityLifecycleCallbacks
  {
    private CompanionDeviceManager.Callback mCallback;
    private Handler mHandler;
    final Object mLock = new Object();
    private AssociationRequest mRequest;
    
    private CallbackProxy(AssociationRequest paramAssociationRequest, CompanionDeviceManager.Callback paramCallback, Handler paramHandler)
    {
      mCallback = paramCallback;
      mHandler = paramHandler;
      mRequest = paramAssociationRequest;
      CompanionDeviceManager.this.getActivity().getApplication().registerActivityLifecycleCallbacks(this);
    }
    
    <T> void lockAndPost(BiConsumer<CompanionDeviceManager.Callback, T> paramBiConsumer, T paramT)
    {
      synchronized (mLock)
      {
        if (mHandler != null)
        {
          Handler localHandler = mHandler;
          _..Lambda.CompanionDeviceManager.CallbackProxy.gkUVA3m3QgEEk8G84_kcBFARHvo localGkUVA3m3QgEEk8G84_kcBFARHvo = new android/companion/_$$Lambda$CompanionDeviceManager$CallbackProxy$gkUVA3m3QgEEk8G84_kcBFARHvo;
          localGkUVA3m3QgEEk8G84_kcBFARHvo.<init>(this, paramBiConsumer, paramT);
          localHandler.post(localGkUVA3m3QgEEk8G84_kcBFARHvo);
        }
        return;
      }
    }
    
    public void onActivityCreated(Activity paramActivity, Bundle paramBundle) {}
    
    public void onActivityDestroyed(Activity paramActivity)
    {
      synchronized (mLock)
      {
        if (paramActivity != CompanionDeviceManager.this.getActivity()) {
          return;
        }
        try
        {
          mService.stopScan(mRequest, this, CompanionDeviceManager.this.getCallingPackage());
        }
        catch (RemoteException paramActivity)
        {
          paramActivity.rethrowFromSystemServer();
        }
        CompanionDeviceManager.this.getActivity().getApplication().unregisterActivityLifecycleCallbacks(this);
        mCallback = null;
        mHandler = null;
        mRequest = null;
        return;
      }
    }
    
    public void onActivityPaused(Activity paramActivity) {}
    
    public void onActivityResumed(Activity paramActivity) {}
    
    public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle) {}
    
    public void onActivityStarted(Activity paramActivity) {}
    
    public void onActivityStopped(Activity paramActivity) {}
    
    public void onFailure(CharSequence paramCharSequence)
    {
      lockAndPost(_..Lambda.ZUPGnRMz08ZrG1ogNO_2O5Hso3I.INSTANCE, paramCharSequence);
    }
    
    public void onSuccess(PendingIntent paramPendingIntent)
    {
      lockAndPost(_..Lambda.OThxsns9MAD5QsKURFQAFbt_3qc.INSTANCE, paramPendingIntent.getIntentSender());
    }
  }
}
