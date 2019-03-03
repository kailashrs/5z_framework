package com.android.internal.telephony;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.IDeviceIdleController;
import android.os.IDeviceIdleController.Stub;
import android.os.Looper;
import android.os.ServiceManager;
import android.telephony.Rlog;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.EriManager;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.imsphone.ImsExternalCallTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccProfile;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;

public class TelephonyComponentFactory
{
  protected static String LOG_TAG = "TelephonyComponentFactory";
  private static TelephonyComponentFactory sInstance;
  
  public TelephonyComponentFactory() {}
  
  public static TelephonyComponentFactory getInstance()
  {
    if (sInstance == null)
    {
      Object localObject1 = "com.qualcomm.qti.internal.telephony.QtiTelephonyComponentFactory";
      Object localObject2 = new PathClassLoader("/system/framework/qti-telephony-common.jar", ClassLoader.getSystemClassLoader());
      String str = LOG_TAG;
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("classLoader = ");
      ((StringBuilder)localObject3).append(localObject2);
      Rlog.d(str, ((StringBuilder)localObject3).toString());
      if ("com.qualcomm.qti.internal.telephony.QtiTelephonyComponentFactory".length() == 0)
      {
        Rlog.d(LOG_TAG, "no customized TelephonyPlugin available, fallback to default");
        localObject1 = "com.android.internal.telephony.TelephonyComponentFactory";
      }
      try
      {
        localObject1 = Class.forName((String)localObject1, false, (ClassLoader)localObject2);
        localObject3 = LOG_TAG;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("cls = ");
        ((StringBuilder)localObject2).append(localObject1);
        Rlog.d((String)localObject3, ((StringBuilder)localObject2).toString());
        localObject1 = ((Class)localObject1).getConstructor(new Class[0]);
        localObject3 = LOG_TAG;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("constructor method = ");
        ((StringBuilder)localObject2).append(localObject1);
        Rlog.d((String)localObject3, ((StringBuilder)localObject2).toString());
        sInstance = (TelephonyComponentFactory)((Constructor)localObject1).newInstance(new Object[0]);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        Rlog.e(LOG_TAG, "Error loading TelephonyComponentFactory");
        sInstance = new TelephonyComponentFactory();
      }
      catch (NoClassDefFoundError localNoClassDefFoundError)
      {
        localNoClassDefFoundError.printStackTrace();
        Rlog.e(LOG_TAG, "error loading TelephonyComponentFactory");
        sInstance = new TelephonyComponentFactory();
      }
    }
    return sInstance;
  }
  
  public CdmaSubscriptionSourceManager getCdmaSubscriptionSourceManagerInstance(Context paramContext, CommandsInterface paramCommandsInterface, Handler paramHandler, int paramInt, Object paramObject)
  {
    Rlog.d(LOG_TAG, "getCdmaSubscriptionSourceManagerInstance");
    return CdmaSubscriptionSourceManager.getInstance(paramContext, paramCommandsInterface, paramHandler, paramInt, paramObject);
  }
  
  public IDeviceIdleController getIDeviceIdleController()
  {
    Rlog.d(LOG_TAG, "getIDeviceIdleController");
    return IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
  }
  
  public SubscriptionController initSubscriptionController(Context paramContext, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    Rlog.d(LOG_TAG, "initSubscriptionController");
    return SubscriptionController.init(paramContext, paramArrayOfCommandsInterface);
  }
  
  public AppSmsManager makeAppSmsManager(Context paramContext)
  {
    return new AppSmsManager(paramContext);
  }
  
  public CarrierActionAgent makeCarrierActionAgent(Phone paramPhone)
  {
    return new CarrierActionAgent(paramPhone);
  }
  
  public CarrierIdentifier makeCarrierIdentifier(Phone paramPhone)
  {
    return new CarrierIdentifier(paramPhone);
  }
  
  public CarrierSignalAgent makeCarrierSignalAgent(Phone paramPhone)
  {
    return new CarrierSignalAgent(paramPhone);
  }
  
  public DcTracker makeDcTracker(Phone paramPhone)
  {
    Rlog.d(LOG_TAG, "makeDcTracker");
    return new DcTracker(paramPhone, 1);
  }
  
  public DeviceStateMonitor makeDeviceStateMonitor(Phone paramPhone)
  {
    return new DeviceStateMonitor(paramPhone);
  }
  
  public EriManager makeEriManager(Phone paramPhone, Context paramContext, int paramInt)
  {
    Rlog.d(LOG_TAG, "makeEriManager");
    return new EriManager(paramPhone, paramContext, paramInt);
  }
  
  public void makeExtTelephonyClasses(Context paramContext, Phone[] paramArrayOfPhone, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    Rlog.d(LOG_TAG, "makeExtTelephonyClasses");
  }
  
  public GsmCdmaCallTracker makeGsmCdmaCallTracker(GsmCdmaPhone paramGsmCdmaPhone)
  {
    Rlog.d(LOG_TAG, "makeGsmCdmaCallTracker");
    return new GsmCdmaCallTracker(paramGsmCdmaPhone);
  }
  
  public IccPhoneBookInterfaceManager makeIccPhoneBookInterfaceManager(Phone paramPhone)
  {
    Rlog.d(LOG_TAG, "makeIccPhoneBookInterfaceManager");
    return new IccPhoneBookInterfaceManager(paramPhone);
  }
  
  public IccSmsInterfaceManager makeIccSmsInterfaceManager(Phone paramPhone)
  {
    Rlog.d(LOG_TAG, "makeIccSmsInterfaceManager");
    return new IccSmsInterfaceManager(paramPhone);
  }
  
  public ImsExternalCallTracker makeImsExternalCallTracker(ImsPhone paramImsPhone)
  {
    return new ImsExternalCallTracker(paramImsPhone);
  }
  
  public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone paramImsPhone)
  {
    Rlog.d(LOG_TAG, "makeImsPhoneCallTracker");
    return new ImsPhoneCallTracker(paramImsPhone);
  }
  
  public InboundSmsTracker makeInboundSmsTracker(Cursor paramCursor, boolean paramBoolean)
  {
    Rlog.d(LOG_TAG, "makeInboundSmsTracker");
    return new InboundSmsTracker(paramCursor, paramBoolean);
  }
  
  public InboundSmsTracker makeInboundSmsTracker(byte[] paramArrayOfByte, long paramLong, int paramInt1, boolean paramBoolean1, String paramString1, String paramString2, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean2, String paramString3)
  {
    Rlog.d(LOG_TAG, "makeInboundSmsTracker");
    return new InboundSmsTracker(paramArrayOfByte, paramLong, paramInt1, paramBoolean1, paramString1, paramString2, paramInt2, paramInt3, paramInt4, paramBoolean2, paramString3);
  }
  
  public InboundSmsTracker makeInboundSmsTracker(byte[] paramArrayOfByte, long paramLong, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2, String paramString3)
  {
    Rlog.d(LOG_TAG, "makeInboundSmsTracker");
    return new InboundSmsTracker(paramArrayOfByte, paramLong, paramInt, paramBoolean1, paramBoolean2, paramString1, paramString2, paramString3);
  }
  
  public LocaleTracker makeLocaleTracker(Phone paramPhone, Looper paramLooper)
  {
    return new LocaleTracker(paramPhone, paramLooper);
  }
  
  public NitzStateMachine makeNitzStateMachine(GsmCdmaPhone paramGsmCdmaPhone)
  {
    return new NitzStateMachine(paramGsmCdmaPhone);
  }
  
  public Phone makePhone(Context paramContext, CommandsInterface paramCommandsInterface, PhoneNotifier paramPhoneNotifier, int paramInt1, int paramInt2, TelephonyComponentFactory paramTelephonyComponentFactory)
  {
    Rlog.d(LOG_TAG, "makePhone");
    return new GsmCdmaPhone(paramContext, paramCommandsInterface, paramPhoneNotifier, paramInt1, paramInt2, paramTelephonyComponentFactory);
  }
  
  public PhoneSwitcher makePhoneSwitcher(int paramInt1, int paramInt2, Context paramContext, SubscriptionController paramSubscriptionController, Looper paramLooper, ITelephonyRegistry paramITelephonyRegistry, CommandsInterface[] paramArrayOfCommandsInterface, Phone[] paramArrayOfPhone)
  {
    Rlog.d(LOG_TAG, "makePhoneSwitcher");
    return new PhoneSwitcher(paramInt1, paramInt2, paramContext, paramSubscriptionController, paramLooper, paramITelephonyRegistry, paramArrayOfCommandsInterface, paramArrayOfPhone);
  }
  
  public RIL makeRIL(Context paramContext, int paramInt1, int paramInt2, Integer paramInteger)
  {
    Rlog.d(LOG_TAG, "makeRIL");
    return new RIL(paramContext, paramInt1, paramInt2, paramInteger);
  }
  
  public ServiceStateTracker makeServiceStateTracker(GsmCdmaPhone paramGsmCdmaPhone, CommandsInterface paramCommandsInterface)
  {
    Rlog.d(LOG_TAG, "makeServiceStateTracker");
    return new ServiceStateTracker(paramGsmCdmaPhone, paramCommandsInterface);
  }
  
  public SimActivationTracker makeSimActivationTracker(Phone paramPhone)
  {
    return new SimActivationTracker(paramPhone);
  }
  
  public SmsStorageMonitor makeSmsStorageMonitor(Phone paramPhone)
  {
    Rlog.d(LOG_TAG, "makeSmsStorageMonitor");
    return new SmsStorageMonitor(paramPhone);
  }
  
  public SmsUsageMonitor makeSmsUsageMonitor(Context paramContext)
  {
    Rlog.d(LOG_TAG, "makeSmsUsageMonitor");
    return new SmsUsageMonitor(paramContext);
  }
  
  public SubscriptionInfoUpdater makeSubscriptionInfoUpdater(Looper paramLooper, Context paramContext, Phone[] paramArrayOfPhone, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    Rlog.d(LOG_TAG, "makeSubscriptionInfoUpdater");
    return new SubscriptionInfoUpdater(paramLooper, paramContext, paramArrayOfPhone, paramArrayOfCommandsInterface);
  }
  
  public UiccProfile makeUiccProfile(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus, int paramInt, UiccCard paramUiccCard, Object paramObject)
  {
    return new UiccProfile(paramContext, paramCommandsInterface, paramIccCardStatus, paramInt, paramUiccCard, paramObject);
  }
  
  public WspTypeDecoder makeWspTypeDecoder(byte[] paramArrayOfByte)
  {
    Rlog.d(LOG_TAG, "makeWspTypeDecoder");
    return new WspTypeDecoder(paramArrayOfByte);
  }
}
