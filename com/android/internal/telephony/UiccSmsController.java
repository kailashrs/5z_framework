package com.android.internal.telephony;

import android.app.ActivityThread;
import android.app.Application;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import java.util.Iterator;
import java.util.List;

public class UiccSmsController
  extends ISmsBaseImpl
{
  static final String LOG_TAG = "RIL_UiccSmsController";
  
  protected UiccSmsController()
  {
    if (ServiceManager.getService("isms") == null) {
      ServiceManager.addService("isms", this);
    }
  }
  
  private IccSmsInterfaceManager getIccSmsInterfaceManager(int paramInt)
  {
    return getPhone(paramInt).getIccSmsInterfaceManager();
  }
  
  private Phone getPhone(int paramInt)
  {
    Phone localPhone1 = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(paramInt));
    Phone localPhone2 = localPhone1;
    if (localPhone1 == null) {
      localPhone2 = PhoneFactory.getDefaultPhone();
    }
    return localPhone2;
  }
  
  private void sendErrorInPendingIntent(PendingIntent paramPendingIntent, int paramInt)
  {
    if (paramPendingIntent != null) {
      try
      {
        paramPendingIntent.send(paramInt);
      }
      catch (PendingIntent.CanceledException paramPendingIntent) {}
    }
  }
  
  private void sendErrorInPendingIntents(List<PendingIntent> paramList, int paramInt)
  {
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      sendErrorInPendingIntent((PendingIntent)paramList.next(), paramInt);
    }
  }
  
  public boolean copyMessageToIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.copyMessageToIccEf(paramString, paramInt2, paramArrayOfByte1, paramArrayOfByte2);
    }
    paramString = new StringBuilder();
    paramString.append("copyMessageToIccEfForSubscriber iccSmsIntMgr is null for Subscription: ");
    paramString.append(paramInt1);
    Rlog.e("RIL_UiccSmsController", paramString.toString());
    return false;
  }
  
  public String createAppSpecificSmsToken(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException
  {
    return getPhone(paramInt).getAppSmsManager().createAppSpecificSmsToken(paramString, paramPendingIntent);
  }
  
  public boolean disableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    return disableCellBroadcastRangeForSubscriber(paramInt1, paramInt2, paramInt2, paramInt3);
  }
  
  public boolean disableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException
  {
    Object localObject = getIccSmsInterfaceManager(paramInt1);
    if (localObject != null) {
      return ((IccSmsInterfaceManager)localObject).disableCellBroadcastRange(paramInt2, paramInt3, paramInt4);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("disableCellBroadcastRangeForSubscriber iccSmsIntMgr is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt1);
    Rlog.e("RIL_UiccSmsController", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public boolean enableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    return enableCellBroadcastRangeForSubscriber(paramInt1, paramInt2, paramInt2, paramInt3);
  }
  
  public boolean enableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException
  {
    Object localObject = getIccSmsInterfaceManager(paramInt1);
    if (localObject != null) {
      return ((IccSmsInterfaceManager)localObject).enableCellBroadcastRange(paramInt2, paramInt3, paramInt4);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("enableCellBroadcastRangeForSubscriber iccSmsIntMgr is null for Subscription: ");
    ((StringBuilder)localObject).append(paramInt1);
    Rlog.e("RIL_UiccSmsController", ((StringBuilder)localObject).toString());
    return false;
  }
  
  public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int paramInt, String paramString)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.getAllMessagesFromIccEf(paramString);
    }
    paramString = new StringBuilder();
    paramString.append("getAllMessagesFromIccEfForSubscriber iccSmsIntMgr is null for Subscription: ");
    paramString.append(paramInt);
    Rlog.e("RIL_UiccSmsController", paramString.toString());
    return null;
  }
  
  public String getImsSmsFormatForSubscriber(int paramInt)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.getImsSmsFormat();
    }
    Rlog.e("RIL_UiccSmsController", "getImsSmsFormatForSubscriber iccSmsIntMgr is null");
    return null;
  }
  
  public int getPreferredSmsSubscription()
    throws RemoteException
  {
    return SubscriptionController.getInstance().getDefaultSmsSubId();
  }
  
  public int getPremiumSmsPermission(String paramString)
    throws RemoteException
  {
    return getPremiumSmsPermissionForSubscriber(getPreferredSmsSubscription(), paramString);
  }
  
  public int getPremiumSmsPermissionForSubscriber(int paramInt, String paramString)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.getPremiumSmsPermission(paramString);
    }
    Rlog.e("RIL_UiccSmsController", "getPremiumSmsPermissionForSubscriber iccSmsIntMgr is null");
    return 0;
  }
  
  public int getSmsCapacityOnIccForSubscriber(int paramInt)
    throws RemoteException
  {
    Object localObject = getIccSmsInterfaceManager(paramInt);
    if (localObject != null) {
      return ((IccSmsInterfaceManager)localObject).getSmsCapacityOnIcc();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("iccSmsIntMgr is null for  subId: ");
    ((StringBuilder)localObject).append(paramInt);
    Rlog.e("RIL_UiccSmsController", ((StringBuilder)localObject).toString());
    return -1;
  }
  
  public void injectSmsPduForSubscriber(int paramInt, byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.injectSmsPdu(paramArrayOfByte, paramString, paramPendingIntent);
    }
    else
    {
      Rlog.e("RIL_UiccSmsController", "injectSmsPduForSubscriber iccSmsIntMgr is null");
      sendErrorInPendingIntent(paramPendingIntent, 2);
    }
  }
  
  public boolean isImsSmsSupportedForSubscriber(int paramInt)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.isImsSmsSupported();
    }
    Rlog.e("RIL_UiccSmsController", "isImsSmsSupportedForSubscriber iccSmsIntMgr is null");
    return false;
  }
  
  public boolean isSMSPromptEnabled()
    throws RemoteException
  {
    return PhoneFactory.isSMSPromptEnabled();
  }
  
  public boolean isSmsSimPickActivityNeeded(int paramInt)
    throws RemoteException
  {
    Object localObject1 = ActivityThread.currentApplication().getApplicationContext();
    TelephonyManager localTelephonyManager = (TelephonyManager)((Context)localObject1).getSystemService("phone");
    long l = Binder.clearCallingIdentity();
    try
    {
      List localList = SubscriptionManager.from((Context)localObject1).getActiveSubscriptionInfoList();
      Binder.restoreCallingIdentity(l);
      if (localList != null)
      {
        int i = localList.size();
        for (int j = 0; j < i; j++)
        {
          localObject1 = (SubscriptionInfo)localList.get(j);
          if ((localObject1 != null) && (((SubscriptionInfo)localObject1).getSubscriptionId() == paramInt)) {
            return false;
          }
        }
        if ((i > 0) && (localTelephonyManager.getSimCount() > 1)) {
          return true;
        }
      }
      return false;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void sendDataForSubscriber(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendData(paramString1, paramString2, paramString3, paramInt2, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendDataForSubscriber iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt1);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
      sendErrorInPendingIntent(paramPendingIntent1, 1);
    }
  }
  
  public void sendDataForSubscriberWithSelfPermissions(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendDataWithSelfPermissions(paramString1, paramString2, paramString3, paramInt2, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendText iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt1);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
    }
  }
  
  public void sendMultipartText(String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
    throws RemoteException
  {
    sendMultipartTextForSubscriber(getPreferredSmsSubscription(), paramString1, paramString2, paramString3, paramList, paramList1, paramList2, true);
  }
  
  public void sendMultipartTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendMultipartText(paramString1, paramString2, paramString3, paramList, paramList1, paramList2, paramBoolean);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendMultipartTextForSubscriber iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
      sendErrorInPendingIntents(paramList1, 1);
    }
  }
  
  public void sendMultipartTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendMultipartTextWithOptions(paramString1, paramString2, paramString3, paramList, paramList1, paramList2, paramBoolean1, paramInt2, paramBoolean2, paramInt3);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendMultipartTextWithOptions iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt1);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
    }
  }
  
  public void sendStoredMultipartText(int paramInt, String paramString1, Uri paramUri, String paramString2, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendStoredMultipartText(paramString1, paramUri, paramString2, paramList1, paramList2);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendStoredMultipartText iccSmsIntMgr is null for subscription: ");
      paramString1.append(paramInt);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
      sendErrorInPendingIntents(paramList1, 1);
    }
  }
  
  public void sendStoredText(int paramInt, String paramString1, Uri paramUri, String paramString2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendStoredText(paramString1, paramUri, paramString2, paramPendingIntent1, paramPendingIntent2);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendStoredText iccSmsIntMgr is null for subscription: ");
      paramString1.append(paramInt);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
      sendErrorInPendingIntent(paramPendingIntent1, 1);
    }
  }
  
  public void sendText(String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    sendTextForSubscriber(getPreferredSmsSubscription(), paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, true);
  }
  
  public void sendTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendText(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendTextForSubscriber iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
      sendErrorInPendingIntent(paramPendingIntent1, 1);
    }
  }
  
  public void sendTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendTextWithOptions(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean1, paramInt2, paramBoolean2, paramInt3);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendTextWithOptions iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt1);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
    }
  }
  
  public void sendTextForSubscriberWithSelfPermissions(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt);
    if (localIccSmsInterfaceManager != null)
    {
      localIccSmsInterfaceManager.sendTextWithSelfPermissions(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean);
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("sendText iccSmsIntMgr is null for Subscription: ");
      paramString1.append(paramInt);
      Rlog.e("RIL_UiccSmsController", paramString1.toString());
    }
  }
  
  public void setPremiumSmsPermission(String paramString, int paramInt)
    throws RemoteException
  {
    setPremiumSmsPermissionForSubscriber(getPreferredSmsSubscription(), paramString, paramInt);
  }
  
  public void setPremiumSmsPermissionForSubscriber(int paramInt1, String paramString, int paramInt2)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null) {
      localIccSmsInterfaceManager.setPremiumSmsPermission(paramString, paramInt2);
    } else {
      Rlog.e("RIL_UiccSmsController", "setPremiumSmsPermissionForSubscriber iccSmsIntMgr is null");
    }
  }
  
  public boolean updateMessageOnIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException
  {
    IccSmsInterfaceManager localIccSmsInterfaceManager = getIccSmsInterfaceManager(paramInt1);
    if (localIccSmsInterfaceManager != null) {
      return localIccSmsInterfaceManager.updateMessageOnIccEf(paramString, paramInt2, paramInt3, paramArrayOfByte);
    }
    paramString = new StringBuilder();
    paramString.append("updateMessageOnIccEfForSubscriber iccSmsIntMgr is null for Subscription: ");
    paramString.append(paramInt1);
    Rlog.e("RIL_UiccSmsController", paramString.toString());
    return false;
  }
}
