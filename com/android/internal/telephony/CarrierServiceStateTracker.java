package com.android.internal.telephony;

import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Global;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codeaurora.internal.IExtTelephony;
import org.codeaurora.internal.IExtTelephony.Stub;

public class CarrierServiceStateTracker
  extends Handler
{
  protected static final int CARRIER_EVENT_BASE = 100;
  protected static final int CARRIER_EVENT_DATA_DEREGISTRATION = 104;
  protected static final int CARRIER_EVENT_DATA_REGISTRATION = 103;
  protected static final int CARRIER_EVENT_VOICE_DEREGISTRATION = 102;
  protected static final int CARRIER_EVENT_VOICE_REGISTRATION = 101;
  private static final String LOG_TAG = "CSST";
  public static final int NOTIFICATION_EMERGENCY_NETWORK = 1001;
  public static final int NOTIFICATION_PREF_NETWORK = 1000;
  private static final int UNINITIALIZED_DELAY_VALUE = -1;
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousIntent = ((CarrierConfigManager)paramAnonymousContext.getSystemService("carrier_config")).getConfigForSubId(mPhone.getSubId());
      paramAnonymousContext = mNotificationTypeMap.entrySet().iterator();
      while (paramAnonymousContext.hasNext()) {
        ((CarrierServiceStateTracker.NotificationType)((Map.Entry)paramAnonymousContext.next()).getValue()).setDelay(paramAnonymousIntent);
      }
      CarrierServiceStateTracker.this.handleConfigChanges();
    }
  };
  private final Map<Integer, NotificationType> mNotificationTypeMap = new HashMap();
  private Phone mPhone;
  private ContentObserver mPrefNetworkModeObserver = new ContentObserver(this)
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      CarrierServiceStateTracker.this.handlePrefNetworkModeChanged();
    }
  };
  private int mPreviousSubId = -1;
  private ServiceStateTracker mSST;
  
  public CarrierServiceStateTracker(Phone paramPhone, ServiceStateTracker paramServiceStateTracker)
  {
    mPhone = paramPhone;
    mSST = paramServiceStateTracker;
    paramPhone.getContext().registerReceiver(mBroadcastReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    SubscriptionManager.from(mPhone.getContext()).addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener(getLooper())
    {
      public void onSubscriptionsChanged()
      {
        int i = mPhone.getSubId();
        if (mPreviousSubId != i)
        {
          CarrierServiceStateTracker.access$102(CarrierServiceStateTracker.this, i);
          CarrierServiceStateTracker.this.registerPrefNetworkModeObserver();
        }
      }
    });
    registerNotificationTypes();
    registerPrefNetworkModeObserver();
  }
  
  private void evaluateSendingMessageOrCancelNotification(NotificationType paramNotificationType)
  {
    Object localObject;
    if (evaluateSendingMessage(paramNotificationType))
    {
      localObject = obtainMessage(paramNotificationType.getTypeId(), null);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("starting timer for notifications.");
      localStringBuilder.append(paramNotificationType.getTypeId());
      Rlog.i("CSST", localStringBuilder.toString());
      sendMessageDelayed((Message)localObject, getDelay(paramNotificationType));
    }
    else
    {
      cancelNotification(paramNotificationType.getTypeId());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("canceling notifications: ");
      ((StringBuilder)localObject).append(paramNotificationType.getTypeId());
      Rlog.i("CSST", ((StringBuilder)localObject).toString());
    }
  }
  
  private void handleConfigChanges()
  {
    Iterator localIterator = mNotificationTypeMap.entrySet().iterator();
    while (localIterator.hasNext()) {
      evaluateSendingMessageOrCancelNotification((NotificationType)((Map.Entry)localIterator.next()).getValue());
    }
  }
  
  private void handlePrefNetworkModeChanged()
  {
    NotificationType localNotificationType = (NotificationType)mNotificationTypeMap.get(Integer.valueOf(1000));
    if (localNotificationType != null) {
      evaluateSendingMessageOrCancelNotification(localNotificationType);
    }
  }
  
  private boolean isGlobalMode()
  {
    Object localObject = mPhone.getContext();
    boolean bool = true;
    try
    {
      localObject = ((Context)localObject).getContentResolver();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("preferred_network_mode");
      localStringBuilder.append(mPhone.getSubId());
      int i = Settings.Global.getInt((ContentResolver)localObject, localStringBuilder.toString(), Phone.PREFERRED_NT_MODE);
      if (i != 10) {
        bool = false;
      }
      return bool;
    }
    catch (Exception localException)
    {
      Rlog.e("CSST", "Unable to get PREFERRED_NETWORK_MODE.");
    }
    return true;
  }
  
  private boolean isPhoneRegisteredForWifiCalling()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("isPhoneRegisteredForWifiCalling: ");
    localStringBuilder.append(mPhone.isWifiCallingEnabled());
    Rlog.d("CSST", localStringBuilder.toString());
    return mPhone.isWifiCallingEnabled();
  }
  
  private boolean isPhoneStillRegistered()
  {
    ServiceState localServiceState = mSST.mSS;
    boolean bool1 = true;
    if (localServiceState == null) {
      return true;
    }
    boolean bool2 = bool1;
    if (mSST.mSS.getVoiceRegState() != 0) {
      if (mSST.mSS.getDataRegState() == 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean isPhoneVoiceRegistered()
  {
    ServiceState localServiceState = mSST.mSS;
    boolean bool = true;
    if (localServiceState == null) {
      return true;
    }
    if (mSST.mSS.getVoiceRegState() != 0) {
      bool = false;
    }
    return bool;
  }
  
  private void registerNotificationTypes()
  {
    mNotificationTypeMap.put(Integer.valueOf(1000), new PrefNetworkNotification(1000));
    mNotificationTypeMap.put(Integer.valueOf(1001), new EmergencyNetworkNotification(1001));
  }
  
  private void registerPrefNetworkModeObserver()
  {
    int i = mPhone.getSubId();
    unregisterPrefNetworkModeObserver();
    if (SubscriptionManager.isValidSubscriptionId(i))
    {
      ContentResolver localContentResolver = mPhone.getContext().getContentResolver();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("preferred_network_mode");
      localStringBuilder.append(i);
      localContentResolver.registerContentObserver(Settings.Global.getUriFor(localStringBuilder.toString()), true, mPrefNetworkModeObserver);
    }
  }
  
  private void unregisterPrefNetworkModeObserver()
  {
    mPhone.getContext().getContentResolver().unregisterContentObserver(mPrefNetworkModeObserver);
  }
  
  public void cancelNotification(int paramInt)
  {
    Context localContext = mPhone.getContext();
    removeMessages(paramInt);
    getNotificationManager(localContext).cancel(paramInt);
  }
  
  public void dispose()
  {
    unregisterPrefNetworkModeObserver();
  }
  
  @VisibleForTesting
  public boolean evaluateSendingMessage(NotificationType paramNotificationType)
  {
    return paramNotificationType.sendMessage();
  }
  
  @VisibleForTesting
  public ContentObserver getContentObserver()
  {
    return mPrefNetworkModeObserver;
  }
  
  @VisibleForTesting
  public int getDelay(NotificationType paramNotificationType)
  {
    return paramNotificationType.getDelay();
  }
  
  @VisibleForTesting
  public Notification.Builder getNotificationBuilder(NotificationType paramNotificationType)
  {
    return paramNotificationType.getNotificationBuilder();
  }
  
  @VisibleForTesting
  public NotificationManager getNotificationManager(Context paramContext)
  {
    return (NotificationManager)paramContext.getSystemService("notification");
  }
  
  @VisibleForTesting
  public Map<Integer, NotificationType> getNotificationTypeMap()
  {
    return mNotificationTypeMap;
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        break;
      case 1000: 
      case 1001: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sending notification after delay: ");
        localStringBuilder.append(what);
        Rlog.d("CSST", localStringBuilder.toString());
        paramMessage = (NotificationType)mNotificationTypeMap.get(Integer.valueOf(what));
        if (paramMessage != null) {
          sendNotification(paramMessage);
        }
        break;
      }
      break;
    case 101: 
    case 102: 
    case 103: 
    case 104: 
      handleConfigChanges();
    }
  }
  
  @VisibleForTesting
  public boolean isRadioOffOrAirplaneMode()
  {
    Context localContext = mPhone.getContext();
    boolean bool1 = true;
    try
    {
      int i = Settings.Global.getInt(localContext.getContentResolver(), "airplane_mode_on", 0);
      boolean bool2 = bool1;
      if (mSST.isRadioOn()) {
        if (i != 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
    catch (Exception localException)
    {
      Rlog.e("CSST", "Unable to get AIRPLACE_MODE_ON.");
    }
    return true;
  }
  
  @VisibleForTesting
  public void sendNotification(NotificationType paramNotificationType)
  {
    if (!evaluateSendingMessage(paramNotificationType)) {
      return;
    }
    Context localContext = mPhone.getContext();
    Notification.Builder localBuilder = getNotificationBuilder(paramNotificationType);
    localBuilder.setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(17301642).setColor(localContext.getResources().getColor(17170876));
    getNotificationManager(localContext).notify(paramNotificationType.getTypeId(), localBuilder.build());
  }
  
  public class EmergencyNetworkNotification
    implements CarrierServiceStateTracker.NotificationType
  {
    private int mDelay = -1;
    private final int mTypeId;
    
    EmergencyNetworkNotification(int paramInt)
    {
      mTypeId = paramInt;
    }
    
    public int getDelay()
    {
      return mDelay;
    }
    
    public Notification.Builder getNotificationBuilder()
    {
      Context localContext = mPhone.getContext();
      CharSequence localCharSequence1 = localContext.getText(17039404);
      CharSequence localCharSequence2 = localContext.getText(17039403);
      return new Notification.Builder(localContext).setContentTitle(localCharSequence1).setStyle(new Notification.BigTextStyle().bigText(localCharSequence2)).setContentText(localCharSequence2).setChannel("wfc");
    }
    
    public int getTypeId()
    {
      return mTypeId;
    }
    
    public boolean sendMessage()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("EmergencyNetworkNotification: sendMessage() w/values: ,");
      localStringBuilder.append(CarrierServiceStateTracker.this.isPhoneVoiceRegistered());
      localStringBuilder.append(",");
      localStringBuilder.append(mDelay);
      localStringBuilder.append(",");
      localStringBuilder.append(CarrierServiceStateTracker.this.isPhoneRegisteredForWifiCalling());
      localStringBuilder.append(",");
      localStringBuilder.append(mSST.isRadioOn());
      Rlog.i("CSST", localStringBuilder.toString());
      return (mDelay != -1) && (!CarrierServiceStateTracker.this.isPhoneVoiceRegistered()) && (CarrierServiceStateTracker.this.isPhoneRegisteredForWifiCalling());
    }
    
    public void setDelay(PersistableBundle paramPersistableBundle)
    {
      if (paramPersistableBundle == null)
      {
        Rlog.e("CSST", "bundle is null");
        return;
      }
      mDelay = paramPersistableBundle.getInt("emergency_notification_delay_int");
      paramPersistableBundle = new StringBuilder();
      paramPersistableBundle.append("reading time to delay notification emergency: ");
      paramPersistableBundle.append(mDelay);
      Rlog.i("CSST", paramPersistableBundle.toString());
    }
  }
  
  public static abstract interface NotificationType
  {
    public abstract int getDelay();
    
    public abstract Notification.Builder getNotificationBuilder();
    
    public abstract int getTypeId();
    
    public abstract boolean sendMessage();
    
    public abstract void setDelay(PersistableBundle paramPersistableBundle);
  }
  
  public class PrefNetworkNotification
    implements CarrierServiceStateTracker.NotificationType
  {
    private int mDelay = -1;
    private final int mTypeId;
    
    PrefNetworkNotification(int paramInt)
    {
      mTypeId = paramInt;
    }
    
    public int getDelay()
    {
      return mDelay;
    }
    
    public Notification.Builder getNotificationBuilder()
    {
      Context localContext = mPhone.getContext();
      Object localObject1 = new Intent("android.settings.DATA_ROAMING_SETTINGS");
      Object localObject2 = IExtTelephony.Stub.asInterface(ServiceManager.getService("extphone"));
      if (localObject2 != null) {
        try
        {
          if (((IExtTelephony)localObject2).isVendorApkAvailable("com.qualcomm.qti.networksetting"))
          {
            localObject2 = new android/content/ComponentName;
            ((ComponentName)localObject2).<init>("com.qualcomm.qti.networksetting", "com.qualcomm.qti.networksetting.MobileNetworkSettings");
            ((Intent)localObject1).setComponent((ComponentName)localObject2);
          }
        }
        catch (RemoteException localRemoteException)
        {
          break label91;
        }
      }
      Object localObject3 = new android/content/ComponentName;
      ((ComponentName)localObject3).<init>("com.android.phone", "com.android.phone.MobileNetworkSettings");
      ((Intent)localObject1).setComponent((ComponentName)localObject3);
      break label107;
      label91:
      ((Intent)localObject1).setComponent(new ComponentName("com.android.phone", "com.android.phone.MobileNetworkSettings"));
      label107:
      ((Intent)localObject1).putExtra("expandable", true);
      localObject3 = PendingIntent.getActivity(localContext, 0, (Intent)localObject1, 1073741824);
      CharSequence localCharSequence = localContext.getText(17039407);
      localObject1 = localContext.getText(17039406);
      return new Notification.Builder(localContext).setContentTitle(localCharSequence).setStyle(new Notification.BigTextStyle().bigText((CharSequence)localObject1)).setContentText((CharSequence)localObject1).setChannel("alert").setContentIntent((PendingIntent)localObject3);
    }
    
    public int getTypeId()
    {
      return mTypeId;
    }
    
    public boolean sendMessage()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PrefNetworkNotification: sendMessage() w/values: ,");
      localStringBuilder.append(CarrierServiceStateTracker.this.isPhoneStillRegistered());
      localStringBuilder.append(",");
      localStringBuilder.append(mDelay);
      localStringBuilder.append(",");
      localStringBuilder.append(CarrierServiceStateTracker.this.isGlobalMode());
      localStringBuilder.append(",");
      localStringBuilder.append(mSST.isRadioOn());
      Rlog.i("CSST", localStringBuilder.toString());
      return (mDelay != -1) && (!CarrierServiceStateTracker.this.isPhoneStillRegistered()) && (!CarrierServiceStateTracker.this.isGlobalMode()) && (!isRadioOffOrAirplaneMode());
    }
    
    public void setDelay(PersistableBundle paramPersistableBundle)
    {
      if (paramPersistableBundle == null)
      {
        Rlog.e("CSST", "bundle is null");
        return;
      }
      mDelay = paramPersistableBundle.getInt("network_notification_delay_int");
      paramPersistableBundle = new StringBuilder();
      paramPersistableBundle.append("reading time to delay notification pref network: ");
      paramPersistableBundle.append(mDelay);
      Rlog.i("CSST", paramPersistableBundle.toString());
    }
  }
}
