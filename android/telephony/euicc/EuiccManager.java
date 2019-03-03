package android.telephony.euicc;

import android.annotation.SystemApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.euicc.IEuiccController;
import com.android.internal.telephony.euicc.IEuiccController.Stub;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EuiccManager
{
  public static final String ACTION_MANAGE_EMBEDDED_SUBSCRIPTIONS = "android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS";
  public static final String ACTION_NOTIFY_CARRIER_SETUP_INCOMPLETE = "android.telephony.euicc.action.NOTIFY_CARRIER_SETUP_INCOMPLETE";
  @SystemApi
  public static final String ACTION_OTA_STATUS_CHANGED = "android.telephony.euicc.action.OTA_STATUS_CHANGED";
  @SystemApi
  public static final String ACTION_PROVISION_EMBEDDED_SUBSCRIPTION = "android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION";
  public static final String ACTION_RESOLVE_ERROR = "android.telephony.euicc.action.RESOLVE_ERROR";
  public static final int EMBEDDED_SUBSCRIPTION_RESULT_ERROR = 2;
  public static final int EMBEDDED_SUBSCRIPTION_RESULT_OK = 0;
  public static final int EMBEDDED_SUBSCRIPTION_RESULT_RESOLVABLE_ERROR = 1;
  @SystemApi
  public static final int EUICC_OTA_FAILED = 2;
  @SystemApi
  public static final int EUICC_OTA_IN_PROGRESS = 1;
  @SystemApi
  public static final int EUICC_OTA_NOT_NEEDED = 4;
  @SystemApi
  public static final int EUICC_OTA_STATUS_UNAVAILABLE = 5;
  @SystemApi
  public static final int EUICC_OTA_SUCCEEDED = 3;
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_DETAILED_CODE = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE";
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION";
  @SystemApi
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTIONS = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTIONS";
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_RESOLUTION_ACTION = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_ACTION";
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT";
  public static final String EXTRA_EMBEDDED_SUBSCRIPTION_RESOLUTION_INTENT = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_INTENT";
  public static final String EXTRA_FORCE_PROVISION = "android.telephony.euicc.extra.FORCE_PROVISION";
  public static final String META_DATA_CARRIER_ICON = "android.telephony.euicc.carriericon";
  private final Context mContext;
  
  public EuiccManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private static IEuiccController getIEuiccController()
  {
    return IEuiccController.Stub.asInterface(ServiceManager.getService("econtroller"));
  }
  
  private static void sendUnavailableError(PendingIntent paramPendingIntent)
  {
    try
    {
      paramPendingIntent.send(2);
    }
    catch (PendingIntent.CanceledException paramPendingIntent) {}
  }
  
  @SystemApi
  public void continueOperation(Intent paramIntent, Bundle paramBundle)
  {
    if (!isEnabled())
    {
      paramIntent = (PendingIntent)paramIntent.getParcelableExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT");
      if (paramIntent != null) {
        sendUnavailableError(paramIntent);
      }
      return;
    }
    try
    {
      getIEuiccController().continueOperation(paramIntent, paramBundle);
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void deleteSubscription(int paramInt, PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().deleteSubscription(paramInt, mContext.getOpPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().downloadSubscription(paramDownloadableSubscription, paramBoolean, mContext.getOpPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramDownloadableSubscription)
    {
      throw paramDownloadableSubscription.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void eraseSubscriptions(PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().eraseSubscriptions(paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void getDefaultDownloadableSubscriptionList(PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().getDefaultDownloadableSubscriptionList(mContext.getOpPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().getDownloadableSubscriptionMetadata(paramDownloadableSubscription, mContext.getOpPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramDownloadableSubscription)
    {
      throw paramDownloadableSubscription.rethrowFromSystemServer();
    }
  }
  
  public String getEid()
  {
    if (!isEnabled()) {
      return null;
    }
    try
    {
      String str = getIEuiccController().getEid();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public EuiccInfo getEuiccInfo()
  {
    if (!isEnabled()) {
      return null;
    }
    try
    {
      EuiccInfo localEuiccInfo = getIEuiccController().getEuiccInfo();
      return localEuiccInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public int getOtaStatus()
  {
    if (!isEnabled()) {
      return 5;
    }
    try
    {
      int i = getIEuiccController().getOtaStatus();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if (getIEuiccController() != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void retainSubscriptionsForFactoryReset(PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().retainSubscriptionsForFactoryReset(paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void startResolutionActivity(Activity paramActivity, int paramInt, Intent paramIntent, PendingIntent paramPendingIntent)
    throws IntentSender.SendIntentException
  {
    PendingIntent localPendingIntent = (PendingIntent)paramIntent.getParcelableExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_INTENT");
    if (localPendingIntent != null)
    {
      paramIntent = new Intent();
      paramIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT", paramPendingIntent);
      paramActivity.startIntentSenderForResult(localPendingIntent.getIntentSender(), paramInt, paramIntent, 0, 0, 0);
      return;
    }
    throw new IllegalArgumentException("Invalid result intent");
  }
  
  public void switchToSubscription(int paramInt, PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().switchToSubscription(paramInt, mContext.getOpPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void updateSubscriptionNickname(int paramInt, String paramString, PendingIntent paramPendingIntent)
  {
    if (!isEnabled())
    {
      sendUnavailableError(paramPendingIntent);
      return;
    }
    try
    {
      getIEuiccController().updateSubscriptionNickname(paramInt, paramString, paramPendingIntent);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OtaStatus {}
}
