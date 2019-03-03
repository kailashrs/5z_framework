package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.LocalServerSocket;
import android.os.Looper;
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.util.LocalLog;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.dataconnection.TelephonyNetworkFactory;
import com.android.internal.telephony.euicc.EuiccCardController;
import com.android.internal.telephony.euicc.EuiccController;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.imsphone.ImsPhoneFactory;
import com.android.internal.telephony.sip.SipPhone;
import com.android.internal.telephony.sip.SipPhoneFactory;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PhoneFactory
{
  static final boolean DBG = false;
  static final String LOG_TAG = "PhoneFactory";
  public static final int MAX_ACTIVE_PHONES = 1;
  static final int SOCKET_OPEN_MAX_RETRY = 3;
  static final int SOCKET_OPEN_RETRY_MILLIS = 2000;
  private static CommandsInterface sCommandsInterface;
  private static CommandsInterface[] sCommandsInterfaces;
  private static Context sContext;
  private static EuiccCardController sEuiccCardController;
  private static EuiccController sEuiccController;
  private static ImsResolver sImsResolver;
  private static IntentBroadcaster sIntentBroadcaster;
  private static final HashMap<String, LocalLog> sLocalLogs = new HashMap();
  static final Object sLockProxyPhones = new Object();
  private static boolean sMadeDefaults;
  private static NotificationChannelController sNotificationChannelController;
  private static Phone sPhone;
  private static PhoneNotifier sPhoneNotifier;
  private static PhoneSwitcher sPhoneSwitcher;
  private static Phone[] sPhones = null;
  private static ProxyController sProxyController;
  private static SubscriptionInfoUpdater sSubInfoRecordUpdater;
  private static SubscriptionMonitor sSubscriptionMonitor;
  private static TelephonyNetworkFactory[] sTelephonyNetworkFactories;
  private static UiccController sUiccController;
  
  static
  {
    sPhone = null;
    sCommandsInterfaces = null;
    sCommandsInterface = null;
    sSubInfoRecordUpdater = null;
    sMadeDefaults = false;
  }
  
  public PhoneFactory() {}
  
  public static void addLocalLog(String paramString, int paramInt)
  {
    synchronized (sLocalLogs)
    {
      if (!sLocalLogs.containsKey(paramString))
      {
        localObject1 = sLocalLogs;
        localObject2 = new android/util/LocalLog;
        ((LocalLog)localObject2).<init>(paramInt);
        ((HashMap)localObject1).put(paramString, localObject2);
        return;
      }
      Object localObject1 = new java/lang/IllegalArgumentException;
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("key ");
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append(" already present");
      ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString());
      throw ((Throwable)localObject1);
    }
  }
  
  public static int calculatePreferredNetworkType(Context paramContext, int paramInt)
  {
    int i = RILConstants.PREFERRED_NETWORK_MODE;
    int j = SubscriptionController.getInstance().getPhoneId(paramInt);
    try
    {
      paramInt = TelephonyManager.getIntAtIndex(paramContext.getContentResolver(), "preferred_network_mode", j);
    }
    catch (Settings.SettingNotFoundException paramContext)
    {
      Rlog.e("PhoneFactory", "Settings Exception Reading Value At Index for Settings.Global.PREFERRED_NETWORK_MODE");
      paramInt = i;
    }
    paramContext = new StringBuilder();
    paramContext.append("[ABSP][calculatePreferredNetworkType] phoneId = ");
    paramContext.append(j);
    paramContext.append(" networkType = ");
    paramContext.append(paramInt);
    Rlog.d("PhoneFactory", paramContext.toString());
    return paramInt;
  }
  
  public static void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.println("PhoneFactory:");
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" sMadeDefaults=");
    ((StringBuilder)localObject1).append(sMadeDefaults);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    sPhoneSwitcher.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.println();
    localObject1 = getPhones();
    Object localObject3;
    for (int i = 0; i < localObject1.length; i++)
    {
      paramPrintWriter.increaseIndent();
      UiccProfile localUiccProfile = localObject1[i];
      try
      {
        localUiccProfile.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        paramPrintWriter.flush();
        paramPrintWriter.println("++++++++++++++++++++++++++++++++");
        sTelephonyNetworkFactories[i].dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        paramPrintWriter.flush();
        paramPrintWriter.println("++++++++++++++++++++++++++++++++");
        try
        {
          localUiccProfile = (UiccProfile)localUiccProfile.getIccCard();
          if (localUiccProfile != null) {
            localUiccProfile.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
          }
        }
        catch (Exception localException6)
        {
          localException6.printStackTrace();
        }
        paramPrintWriter.flush();
        paramPrintWriter.decreaseIndent();
        paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      }
      catch (Exception localException7)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Telephony DebugService: Could not get Phone[");
        ((StringBuilder)localObject3).append(i);
        ((StringBuilder)localObject3).append("] e=");
        ((StringBuilder)localObject3).append(localException7);
        paramPrintWriter.println(((StringBuilder)localObject3).toString());
      }
    }
    paramPrintWriter.println("SubscriptionMonitor:");
    paramPrintWriter.increaseIndent();
    try
    {
      sSubscriptionMonitor.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    paramPrintWriter.println("UiccController:");
    paramPrintWriter.increaseIndent();
    try
    {
      sUiccController.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
    paramPrintWriter.flush();
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    if (sEuiccController != null)
    {
      paramPrintWriter.println("EuiccController:");
      paramPrintWriter.increaseIndent();
      try
      {
        sEuiccController.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        sEuiccCardController.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException3)
      {
        localException3.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.decreaseIndent();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    paramPrintWriter.println("SubscriptionController:");
    paramPrintWriter.increaseIndent();
    try
    {
      SubscriptionController.getInstance().dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    catch (Exception localException4)
    {
      localException4.printStackTrace();
    }
    paramPrintWriter.flush();
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    paramPrintWriter.println("SubInfoRecordUpdater:");
    paramPrintWriter.increaseIndent();
    try
    {
      sSubInfoRecordUpdater.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    catch (Exception localException5)
    {
      localException5.printStackTrace();
    }
    paramPrintWriter.flush();
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    paramPrintWriter.println("LocalLogs:");
    paramPrintWriter.increaseIndent();
    synchronized (sLocalLogs)
    {
      Iterator localIterator = sLocalLogs.keySet().iterator();
      while (localIterator.hasNext())
      {
        localObject3 = (String)localIterator.next();
        paramPrintWriter.println((String)localObject3);
        paramPrintWriter.increaseIndent();
        ((LocalLog)sLocalLogs.get(localObject3)).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        paramPrintWriter.decreaseIndent();
      }
      paramPrintWriter.flush();
      paramPrintWriter.decreaseIndent();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      paramPrintWriter.println("SharedPreferences:");
      paramPrintWriter.increaseIndent();
      try
      {
        if (sContext != null)
        {
          ??? = PreferenceManager.getDefaultSharedPreferences(sContext).getAll();
          paramFileDescriptor = ((Map)???).keySet().iterator();
          while (paramFileDescriptor.hasNext())
          {
            localObject3 = paramFileDescriptor.next();
            paramArrayOfString = new java/lang/StringBuilder;
            paramArrayOfString.<init>();
            paramArrayOfString.append(localObject3);
            paramArrayOfString.append(" : ");
            paramArrayOfString.append(((Map)???).get(localObject3));
            paramPrintWriter.println(paramArrayOfString.toString());
          }
        }
      }
      catch (Exception paramFileDescriptor)
      {
        paramFileDescriptor.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.decreaseIndent();
      return;
    }
  }
  
  public static Phone getDefaultPhone()
  {
    synchronized (sLockProxyPhones)
    {
      if (sMadeDefaults)
      {
        localObject2 = sPhone;
        return localObject2;
      }
      Object localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("Default phones haven't been made yet!");
      throw ((Throwable)localObject2);
    }
  }
  
  public static int getDefaultSubscription()
  {
    return SubscriptionController.getInstance().getDefaultSubId();
  }
  
  public static ImsResolver getImsResolver()
  {
    return sImsResolver;
  }
  
  public static Phone getPhone(int paramInt)
  {
    synchronized (sLockProxyPhones)
    {
      if (sMadeDefaults)
      {
        if (paramInt == Integer.MAX_VALUE) {
          localObject2 = sPhone;
        } else if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getPhoneCount())) {
          localObject2 = sPhones[paramInt];
        } else {
          localObject2 = null;
        }
        return localObject2;
      }
      Object localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("Default phones haven't been made yet!");
      throw ((Throwable)localObject2);
    }
  }
  
  public static Phone[] getPhones()
  {
    synchronized (sLockProxyPhones)
    {
      if (sMadeDefaults)
      {
        localObject2 = sPhones;
        return localObject2;
      }
      Object localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("Default phones haven't been made yet!");
      throw ((Throwable)localObject2);
    }
  }
  
  public static SubscriptionInfoUpdater getSubscriptionInfoUpdater()
  {
    return sSubInfoRecordUpdater;
  }
  
  public static boolean isSMSPromptEnabled()
  {
    boolean bool = false;
    int i = 0;
    try
    {
      int j = Settings.Global.getInt(sContext.getContentResolver(), "multi_sim_sms_prompt");
      i = j;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
      Rlog.e("PhoneFactory", "Settings Exception Reading Dual Sim SMS Prompt Values");
    }
    if (i != 0) {
      bool = true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SMS Prompt option:");
    localStringBuilder.append(bool);
    Rlog.d("PhoneFactory", localStringBuilder.toString());
    return bool;
  }
  
  public static void localLog(String paramString1, String paramString2)
  {
    synchronized (sLocalLogs)
    {
      if (sLocalLogs.containsKey(paramString1))
      {
        ((LocalLog)sLocalLogs.get(paramString1)).log(paramString2);
        return;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      paramString2 = new java/lang/StringBuilder;
      paramString2.<init>();
      paramString2.append("key ");
      paramString2.append(paramString1);
      paramString2.append(" not found");
      localIllegalArgumentException.<init>(paramString2.toString());
      throw localIllegalArgumentException;
    }
  }
  
  public static void makeDefaultPhone(Context paramContext)
  {
    synchronized (sLockProxyPhones)
    {
      if (!sMadeDefaults)
      {
        sContext = paramContext;
        TelephonyDevController.create();
        int k;
        for (int i = 0;; i = k)
        {
          int j = 0;
          k = i + 1;
          try
          {
            new LocalServerSocket("com.android.internal.telephony");
            i = j;
          }
          catch (IOException localIOException)
          {
            i = 1;
          }
          if (i == 0)
          {
            Object localObject2 = new com/android/internal/telephony/DefaultPhoneNotifier;
            ((DefaultPhoneNotifier)localObject2).<init>();
            sPhoneNotifier = (PhoneNotifier)localObject2;
            TelephonyComponentFactory localTelephonyComponentFactory = TelephonyComponentFactory.getInstance();
            k = CdmaSubscriptionSourceManager.getDefault(paramContext);
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("Cdma Subscription set to ");
            ((StringBuilder)localObject2).append(k);
            Rlog.i("PhoneFactory", ((StringBuilder)localObject2).toString());
            j = TelephonyManager.getDefault().getPhoneCount();
            boolean bool = sContext.getResources().getBoolean(17956946);
            Object localObject3 = sContext.getResources().getString(17039725);
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("ImsResolver: defaultImsPackage: ");
            ((StringBuilder)localObject2).append((String)localObject3);
            Rlog.i("PhoneFactory", ((StringBuilder)localObject2).toString());
            localObject2 = new com/android/internal/telephony/ims/ImsResolver;
            ((ImsResolver)localObject2).<init>(sContext, (String)localObject3, j, bool);
            sImsResolver = (ImsResolver)localObject2;
            sImsResolver.initPopulateCacheAndStartBind();
            Object localObject4 = new int[j];
            sPhones = new Phone[j];
            sCommandsInterfaces = new RIL[j];
            sTelephonyNetworkFactories = new TelephonyNetworkFactory[j];
            for (i = 0; i < j; i++)
            {
              localObject4[i] = RILConstants.PREFERRED_NETWORK_MODE;
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("Network Mode set to ");
              ((StringBuilder)localObject2).append(Integer.toString(localObject4[i]));
              Rlog.i("PhoneFactory", ((StringBuilder)localObject2).toString());
              sCommandsInterfaces[i] = localTelephonyComponentFactory.makeRIL(paramContext, localObject4[i], k, Integer.valueOf(i));
            }
            Rlog.i("PhoneFactory", "Creating SubscriptionController");
            localTelephonyComponentFactory.initSubscriptionController(paramContext, sCommandsInterfaces);
            sUiccController = UiccController.make(paramContext, sCommandsInterfaces);
            if (paramContext.getPackageManager().hasSystemFeature("android.hardware.telephony.euicc"))
            {
              sEuiccController = EuiccController.init(paramContext);
              sEuiccCardController = EuiccCardController.init(paramContext);
            }
            for (i = 0; i < j; i++)
            {
              localObject2 = null;
              int m = TelephonyManager.getPhoneType(localObject4[i]);
              if (m == 1)
              {
                localObject2 = localTelephonyComponentFactory.makePhone(paramContext, sCommandsInterfaces[i], sPhoneNotifier, i, 1, localTelephonyComponentFactory);
              }
              else
              {
                k = i;
                if (m == 2) {
                  localObject2 = localTelephonyComponentFactory.makePhone(paramContext, sCommandsInterfaces[k], sPhoneNotifier, k, 6, localTelephonyComponentFactory);
                }
              }
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("Creating Phone with type = ");
              localStringBuilder.append(m);
              localStringBuilder.append(" sub = ");
              localStringBuilder.append(i);
              Rlog.i("PhoneFactory", localStringBuilder.toString());
              sPhones[i] = localObject2;
            }
            for (i = 0; i < j; i++) {
              for (k = 0; k < j; k++) {
                if (i != k) {
                  sPhones[i].bridgeTheOtherPhone(sPhones[k]);
                }
              }
            }
            localObject2 = sPhones;
            k = 0;
            sPhone = localObject2[0];
            sCommandsInterface = sCommandsInterfaces[0];
            localObject3 = SmsApplication.getDefaultSmsApplication(paramContext, true);
            localObject2 = "NONE";
            if (localObject3 != null) {
              localObject2 = ((ComponentName)localObject3).getPackageName();
            }
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("defaultSmsApplication: ");
            ((StringBuilder)localObject3).append((String)localObject2);
            Rlog.i("PhoneFactory", ((StringBuilder)localObject3).toString());
            SmsApplication.initSmsPackageMonitor(paramContext);
            sMadeDefaults = true;
            Rlog.i("PhoneFactory", "Creating SubInfoRecordUpdater ");
            sSubInfoRecordUpdater = localTelephonyComponentFactory.makeSubscriptionInfoUpdater(BackgroundThread.get().getLooper(), paramContext, sPhones, sCommandsInterfaces);
            SubscriptionController.getInstance().updatePhonesAvailability(sPhones);
            for (i = 0; i < j; i++) {
              sPhones[i].startMonitoringImsService();
            }
            localObject3 = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            localObject2 = SubscriptionController.getInstance();
            localObject4 = new com/android/internal/telephony/SubscriptionMonitor;
            ((SubscriptionMonitor)localObject4).<init>((ITelephonyRegistry)localObject3, sContext, (SubscriptionController)localObject2, j);
            sSubscriptionMonitor = (SubscriptionMonitor)localObject4;
            sPhoneSwitcher = localTelephonyComponentFactory.makePhoneSwitcher(1, j, sContext, (SubscriptionController)localObject2, Looper.myLooper(), (ITelephonyRegistry)localObject3, sCommandsInterfaces, sPhones);
            sProxyController = ProxyController.getInstance(paramContext, sPhones, sUiccController, sCommandsInterfaces, sPhoneSwitcher);
            sIntentBroadcaster = IntentBroadcaster.getInstance(paramContext);
            localObject3 = new com/android/internal/telephony/util/NotificationChannelController;
            ((NotificationChannelController)localObject3).<init>(paramContext);
            sNotificationChannelController = (NotificationChannelController)localObject3;
            sTelephonyNetworkFactories = new TelephonyNetworkFactory[j];
            for (i = k; i < j; i++) {
              sTelephonyNetworkFactories[i] = new TelephonyNetworkFactory(sPhoneSwitcher, (SubscriptionController)localObject2, sSubscriptionMonitor, Looper.myLooper(), sContext, i, sPhonesmDcTracker);
            }
            localTelephonyComponentFactory.makeExtTelephonyClasses(paramContext, sPhones, sCommandsInterfaces);
            break label945;
          }
          if (k > 3) {
            break;
          }
          try
          {
            Thread.sleep(2000L);
          }
          catch (InterruptedException localInterruptedException) {}
        }
        paramContext = new java/lang/RuntimeException;
        paramContext.<init>("PhoneFactory probably already running");
        throw paramContext;
      }
      label945:
      return;
    }
  }
  
  public static void makeDefaultPhones(Context paramContext)
  {
    makeDefaultPhone(paramContext);
  }
  
  public static Phone makeImsPhone(PhoneNotifier paramPhoneNotifier, Phone paramPhone)
  {
    return ImsPhoneFactory.makePhone(sContext, paramPhoneNotifier, paramPhone);
  }
  
  public static SipPhone makeSipPhone(String paramString)
  {
    return SipPhoneFactory.makePhone(paramString, sContext, sPhoneNotifier);
  }
  
  public static void requestEmbeddedSubscriptionInfoListRefresh(Runnable paramRunnable)
  {
    sSubInfoRecordUpdater.requestEmbeddedSubscriptionInfoListRefresh(paramRunnable);
  }
}
