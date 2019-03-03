package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.content.PackageMonitor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class CarrierServiceBindHelper
{
  private static final int EVENT_PERFORM_IMMEDIATE_UNBIND = 1;
  private static final int EVENT_REBIND = 0;
  private static final String LOG_TAG = "CarrierSvcBindHelper";
  private static final int UNBIND_DELAY_MILLIS = 30000;
  private AppBinding[] mBindings;
  private Context mContext;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("mHandler: ");
      ((StringBuilder)localObject).append(what);
      CarrierServiceBindHelper.log(((StringBuilder)localObject).toString());
      switch (what)
      {
      default: 
        break;
      case 1: 
        CarrierServiceBindHelper.AppBinding.access$300((CarrierServiceBindHelper.AppBinding)obj);
        break;
      case 0: 
        localObject = (CarrierServiceBindHelper.AppBinding)obj;
        paramAnonymousMessage = new StringBuilder();
        paramAnonymousMessage.append("Rebinding if necessary for phoneId: ");
        paramAnonymousMessage.append(((CarrierServiceBindHelper.AppBinding)localObject).getPhoneId());
        CarrierServiceBindHelper.log(paramAnonymousMessage.toString());
        ((CarrierServiceBindHelper.AppBinding)localObject).rebind();
      }
    }
  };
  private String[] mLastSimState;
  private final PackageMonitor mPackageMonitor = new CarrierServicePackageMonitor(null);
  private BroadcastReceiver mUserUnlockedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      paramAnonymousIntent = new StringBuilder();
      paramAnonymousIntent.append("Received ");
      paramAnonymousIntent.append(paramAnonymousContext);
      CarrierServiceBindHelper.log(paramAnonymousIntent.toString());
      if ("android.intent.action.USER_UNLOCKED".equals(paramAnonymousContext)) {
        for (int i = 0; i < mBindings.length; i++) {
          mBindings[i].rebind();
        }
      }
    }
  };
  
  public CarrierServiceBindHelper(Context paramContext)
  {
    mContext = paramContext;
    int i = TelephonyManager.from(paramContext).getPhoneCount();
    mBindings = new AppBinding[i];
    mLastSimState = new String[i];
    for (int j = 0; j < i; j++) {
      mBindings[j] = new AppBinding(j);
    }
    mPackageMonitor.register(paramContext, mHandler.getLooper(), UserHandle.ALL, false);
    mContext.registerReceiverAsUser(mUserUnlockedReceiver, UserHandle.SYSTEM, new IntentFilter("android.intent.action.USER_UNLOCKED"), null, mHandler);
  }
  
  private static void log(String paramString)
  {
    Log.d("CarrierSvcBindHelper", paramString);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("CarrierServiceBindHelper:");
    AppBinding[] arrayOfAppBinding = mBindings;
    int i = arrayOfAppBinding.length;
    for (int j = 0; j < i; j++) {
      arrayOfAppBinding[j].dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  void updateForPhoneId(int paramInt, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("update binding for phoneId: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" simState: ");
    localStringBuilder.append(paramString);
    log(localStringBuilder.toString());
    if (!SubscriptionManager.isValidPhoneId(paramInt)) {
      return;
    }
    if ((!TextUtils.isEmpty(paramString)) && (paramInt < mLastSimState.length))
    {
      if (paramString.equals(mLastSimState[paramInt])) {
        return;
      }
      mLastSimState[paramInt] = paramString;
      mHandler.sendMessage(mHandler.obtainMessage(0, mBindings[paramInt]));
      return;
    }
  }
  
  private class AppBinding
  {
    private int bindCount;
    private String carrierPackage;
    private String carrierServiceClass;
    private CarrierServiceBindHelper.CarrierServiceConnection connection;
    private long lastBindStartMillis;
    private long lastUnbindMillis;
    private long mUnbindScheduledUptimeMillis = -1L;
    private int phoneId;
    private int unbindCount;
    
    public AppBinding(int paramInt)
    {
      phoneId = paramInt;
    }
    
    private void cancelScheduledUnbind()
    {
      mHandler.removeMessages(1);
      mUnbindScheduledUptimeMillis = -1L;
    }
    
    private void performImmediateUnbind()
    {
      unbindCount += 1;
      lastUnbindMillis = System.currentTimeMillis();
      carrierPackage = null;
      carrierServiceClass = null;
      CarrierServiceBindHelper.log("Unbinding from carrier app");
      mContext.unbindService(connection);
      connection = null;
      mUnbindScheduledUptimeMillis = -1L;
    }
    
    public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("Carrier app binding for phone ");
      paramFileDescriptor.append(phoneId);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  connection: ");
      paramFileDescriptor.append(connection);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  bindCount: ");
      paramFileDescriptor.append(bindCount);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  lastBindStartMillis: ");
      paramFileDescriptor.append(lastBindStartMillis);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  unbindCount: ");
      paramFileDescriptor.append(unbindCount);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  lastUnbindMillis: ");
      paramFileDescriptor.append(lastUnbindMillis);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mUnbindScheduledUptimeMillis: ");
      paramFileDescriptor.append(mUnbindScheduledUptimeMillis);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramPrintWriter.println();
    }
    
    public String getPackage()
    {
      return carrierPackage;
    }
    
    public int getPhoneId()
    {
      return phoneId;
    }
    
    void rebind()
    {
      Object localObject1 = TelephonyManager.from(mContext).getCarrierPackageNamesForIntentAndPhone(new Intent("android.service.carrier.CarrierService"), phoneId);
      if ((localObject1 != null) && (((List)localObject1).size() > 0))
      {
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Found carrier app: ");
        ((StringBuilder)localObject2).append(localObject1);
        CarrierServiceBindHelper.log(((StringBuilder)localObject2).toString());
        String str = (String)((List)localObject1).get(0);
        if (!TextUtils.equals(carrierPackage, str)) {
          unbind(true);
        }
        Intent localIntent = new Intent("android.service.carrier.CarrierService");
        localIntent.setPackage(str);
        ResolveInfo localResolveInfo = mContext.getPackageManager().resolveService(localIntent, 128);
        localObject1 = null;
        localObject2 = null;
        if (localResolveInfo != null)
        {
          localObject1 = serviceInfo.metaData;
          localObject2 = localResolveInfo.getComponentInfo().getComponentName().getClassName();
        }
        if ((localObject1 != null) && (((Bundle)localObject1).getBoolean("android.service.carrier.LONG_LIVED_BINDING", false)))
        {
          if (!TextUtils.equals(carrierServiceClass, (CharSequence)localObject2))
          {
            unbind(true);
          }
          else if (connection != null)
          {
            cancelScheduledUnbind();
            return;
          }
          carrierPackage = str;
          carrierServiceClass = ((String)localObject2);
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Binding to ");
          ((StringBuilder)localObject2).append(carrierPackage);
          ((StringBuilder)localObject2).append(" for phone ");
          ((StringBuilder)localObject2).append(phoneId);
          CarrierServiceBindHelper.log(((StringBuilder)localObject2).toString());
          bindCount += 1;
          lastBindStartMillis = System.currentTimeMillis();
          connection = new CarrierServiceBindHelper.CarrierServiceConnection(CarrierServiceBindHelper.this, null);
          try
          {
            if (mContext.bindServiceAsUser(localIntent, connection, 67108865, mHandler, Process.myUserHandle())) {
              return;
            }
            localObject2 = "bindService returned false";
          }
          catch (SecurityException localSecurityException)
          {
            localObject3 = localSecurityException.getMessage();
          }
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unable to bind to ");
          ((StringBuilder)localObject1).append(carrierPackage);
          ((StringBuilder)localObject1).append(" for phone ");
          ((StringBuilder)localObject1).append(phoneId);
          ((StringBuilder)localObject1).append(". Error: ");
          ((StringBuilder)localObject1).append((String)localObject3);
          CarrierServiceBindHelper.log(((StringBuilder)localObject1).toString());
          unbind(true);
          return;
        }
        CarrierServiceBindHelper.log("Carrier app does not want a long lived binding");
        unbind(true);
        return;
      }
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("No carrier app for: ");
      ((StringBuilder)localObject3).append(phoneId);
      CarrierServiceBindHelper.log(((StringBuilder)localObject3).toString());
      unbind(false);
    }
    
    void unbind(boolean paramBoolean)
    {
      if (connection == null) {
        return;
      }
      if ((!paramBoolean) && (CarrierServiceBindHelper.CarrierServiceConnection.access$700(connection)))
      {
        if (mUnbindScheduledUptimeMillis == -1L)
        {
          mUnbindScheduledUptimeMillis = (30000L + SystemClock.uptimeMillis());
          CarrierServiceBindHelper.log("Scheduling unbind in 30000 millis");
          mHandler.sendMessageAtTime(mHandler.obtainMessage(1, this), mUnbindScheduledUptimeMillis);
        }
      }
      else
      {
        cancelScheduledUnbind();
        performImmediateUnbind();
      }
    }
  }
  
  private class CarrierServiceConnection
    implements ServiceConnection
  {
    private boolean connected;
    
    private CarrierServiceConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      paramIBinder = new StringBuilder();
      paramIBinder.append("Connected to carrier app: ");
      paramIBinder.append(paramComponentName.flattenToString());
      CarrierServiceBindHelper.log(paramIBinder.toString());
      connected = true;
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Disconnected from carrier app: ");
      localStringBuilder.append(paramComponentName.flattenToString());
      CarrierServiceBindHelper.log(localStringBuilder.toString());
      connected = false;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CarrierServiceConnection[connected=");
      localStringBuilder.append(connected);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
  
  private class CarrierServicePackageMonitor
    extends PackageMonitor
  {
    private CarrierServicePackageMonitor() {}
    
    private void evaluateBinding(String paramString, boolean paramBoolean)
    {
      for (CarrierServiceBindHelper.AppBinding localAppBinding : mBindings)
      {
        String str = localAppBinding.getPackage();
        boolean bool = paramString.equals(str);
        if (bool)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(paramString);
          localStringBuilder.append(" changed and corresponds to a phone. Rebinding.");
          CarrierServiceBindHelper.log(localStringBuilder.toString());
        }
        if ((str == null) || (bool))
        {
          if (paramBoolean) {
            localAppBinding.unbind(true);
          }
          localAppBinding.rebind();
        }
      }
    }
    
    public boolean onHandleForceStop(Intent paramIntent, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++) {
          evaluateBinding(paramArrayOfString[j], true);
        }
      }
      return super.onHandleForceStop(paramIntent, paramArrayOfString, paramInt, paramBoolean);
    }
    
    public void onPackageAdded(String paramString, int paramInt)
    {
      evaluateBinding(paramString, true);
    }
    
    public void onPackageModified(String paramString)
    {
      evaluateBinding(paramString, false);
    }
    
    public void onPackageRemoved(String paramString, int paramInt)
    {
      evaluateBinding(paramString, true);
    }
    
    public void onPackageUpdateFinished(String paramString, int paramInt)
    {
      evaluateBinding(paramString, true);
    }
  }
}
