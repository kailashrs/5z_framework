package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.RemoteException;
import java.util.List;

public class ISmsBaseImpl
  extends ISms.Stub
{
  public ISmsBaseImpl() {}
  
  public boolean copyMessageToIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public String createAppSpecificSmsToken(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean disableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean disableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean enableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean enableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int paramInt, String paramString)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public String getImsSmsFormatForSubscriber(int paramInt)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public int getPreferredSmsSubscription()
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public int getPremiumSmsPermission(String paramString)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public int getPremiumSmsPermissionForSubscriber(int paramInt, String paramString)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public int getSmsCapacityOnIccForSubscriber(int paramInt)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void injectSmsPduForSubscriber(int paramInt, byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean isImsSmsSupportedForSubscriber(int paramInt)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean isSMSPromptEnabled()
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean isSmsSimPickActivityNeeded(int paramInt)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendDataForSubscriber(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendDataForSubscriberWithSelfPermissions(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendMultipartTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendMultipartTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendStoredMultipartText(int paramInt, String paramString1, Uri paramUri, String paramString2, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendStoredText(int paramInt, String paramString1, Uri paramUri, String paramString2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void sendTextForSubscriberWithSelfPermissions(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void setPremiumSmsPermission(String paramString, int paramInt)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public void setPremiumSmsPermissionForSubscriber(int paramInt1, String paramString, int paramInt2)
    throws RemoteException
  {
    throw new RemoteException();
  }
  
  public boolean updateMessageOnIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException
  {
    throw new RemoteException();
  }
}
