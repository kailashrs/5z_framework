package com.android.internal.telephony;

import android.content.ContentValues;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.Rlog;
import com.android.internal.telephony.uicc.AdnRecord;
import java.util.List;

public class UiccPhoneBookController
  extends IIccPhoneBook.Stub
{
  private static final String TAG = "UiccPhoneBookController";
  private Phone[] mPhone;
  
  public UiccPhoneBookController(Phone[] paramArrayOfPhone)
  {
    if (ServiceManager.getService("simphonebook") == null) {
      ServiceManager.addService("simphonebook", this);
    }
    mPhone = paramArrayOfPhone;
  }
  
  private int getDefaultSubscription()
  {
    return PhoneFactory.getDefaultSubscription();
  }
  
  private IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager(int paramInt)
  {
    int i = SubscriptionController.getInstance().getPhoneId(paramInt);
    try
    {
      localObject = mPhone[i].getIccPhoneBookInterfaceManager();
      return localObject;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Exception is :");
      ((StringBuilder)localObject).append(localArrayIndexOutOfBoundsException.toString());
      ((StringBuilder)localObject).append(" For subscription :");
      ((StringBuilder)localObject).append(paramInt);
      Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
      localArrayIndexOutOfBoundsException.printStackTrace();
      return null;
    }
    catch (NullPointerException localNullPointerException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception is :");
      localStringBuilder.append(localNullPointerException.toString());
      localStringBuilder.append(" For subscription :");
      localStringBuilder.append(paramInt);
      Rlog.e("UiccPhoneBookController", localStringBuilder.toString());
      localNullPointerException.printStackTrace();
    }
    return null;
  }
  
  public int getADNReadyForSubscriber(int paramInt)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getSIMADNReadyState();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getADNReadyForSubscriber iccPbkIntMgrProxy is null for Subscription: ");
    ((StringBuilder)localObject).append(paramInt);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return 0;
  }
  
  public int[] getAdnRecordsCapacity()
    throws RemoteException
  {
    return getAdnRecordsCapacityForSubscriber(getDefaultSubscription());
  }
  
  public int[] getAdnRecordsCapacityForSubscriber(int paramInt)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getAdnRecordsCapacity();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAdnRecordsCapacity iccPbkIntMgr is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public List<AdnRecord> getAdnRecordsInEf(int paramInt)
    throws RemoteException
  {
    return getAdnRecordsInEfForSubscriber(getDefaultSubscription(), paramInt);
  }
  
  public List<AdnRecord> getAdnRecordsInEfForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt1);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getAdnRecordsInEf(paramInt2);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAdnRecordsInEf iccPbkIntMgr isnull for Subscription:");
    ((StringBuilder)localObject).append(paramInt1);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public int[] getAdnRecordsSize(int paramInt)
    throws RemoteException
  {
    return getAdnRecordsSizeForSubscriber(getDefaultSubscription(), paramInt);
  }
  
  public int[] getAdnRecordsSizeForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt1);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getAdnRecordsSize(paramInt2);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getAdnRecordsSize iccPbkIntMgr is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt1);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public int getRowIdForNewAddedRecord()
    throws RemoteException
  {
    return getRowIdForNewAddedRecordForSubscriber(getDefaultSubscription());
  }
  
  public int getRowIdForNewAddedRecordForSubscriber(int paramInt)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getRowIdForNewAddedRecord();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getRowIdForNewAddedRecord iccPbkIntMgr is null for Subscription: ");
    ((StringBuilder)localObject).append(paramInt);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return -1;
  }
  
  public int[] getSIMPhoneBookProperty(int paramInt)
    throws RemoteException
  {
    return getSIMPhoneBookPropertyForSubscriber(getDefaultSubscription(), paramInt);
  }
  
  public int[] getSIMPhoneBookPropertyForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException
  {
    Object localObject = getIccPhoneBookInterfaceManager(paramInt1);
    if (localObject != null) {
      return ((IccPhoneBookInterfaceManager)localObject).getSIMPhoneBookProperty(paramInt2);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getSIMPhoneBookProperty iccPbkIntMgrProxy is null for Subscription: ");
    ((StringBuilder)localObject).append(paramInt1);
    Rlog.e("UiccPhoneBookController", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public boolean updateAdnRecordsInEfByIndex(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, String paramString4)
    throws RemoteException
  {
    return updateAdnRecordsInEfByIndexForSubscriber(getDefaultSubscription(), paramInt1, paramString1, paramString2, paramString3, null, paramInt2, paramString4);
  }
  
  public boolean updateAdnRecordsInEfByIndexForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt3, String paramString5)
    throws RemoteException
  {
    IccPhoneBookInterfaceManager localIccPhoneBookInterfaceManager = getIccPhoneBookInterfaceManager(paramInt1);
    if (localIccPhoneBookInterfaceManager != null) {
      return localIccPhoneBookInterfaceManager.updateAdnRecordsInEfByIndex(paramInt2, paramString1, paramString2, paramString3, paramString4, paramInt3, paramString5);
    }
    paramString1 = new StringBuilder();
    paramString1.append("updateAdnRecordsInEfByIndex iccPbkIntMgr is null for Subscription:");
    paramString1.append(paramInt1);
    Rlog.e("UiccPhoneBookController", paramString1.toString());
    return false;
  }
  
  public boolean updateAdnRecordsInEfBySearch(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException
  {
    return updateAdnRecordsInEfBySearchForSubscriber(getDefaultSubscription(), paramInt, paramString1, paramString2, paramString3, paramString4, paramString5);
  }
  
  public boolean updateAdnRecordsInEfBySearchForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException
  {
    IccPhoneBookInterfaceManager localIccPhoneBookInterfaceManager = getIccPhoneBookInterfaceManager(paramInt1);
    if (localIccPhoneBookInterfaceManager != null) {
      return localIccPhoneBookInterfaceManager.updateAdnRecordsInEfBySearch(paramInt2, paramString1, paramString2, paramString3, paramString4, paramString5);
    }
    paramString1 = new StringBuilder();
    paramString1.append("updateAdnRecordsInEfBySearch iccPbkIntMgr is null for Subscription:");
    paramString1.append(paramInt1);
    Rlog.e("UiccPhoneBookController", paramString1.toString());
    return false;
  }
  
  public boolean updateAdnRecordsWithContentValuesInEfBySearch(int paramInt, ContentValues paramContentValues, String paramString)
    throws RemoteException
  {
    return updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(getDefaultSubscription(), paramInt, paramContentValues, paramString);
  }
  
  public boolean updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(int paramInt1, int paramInt2, ContentValues paramContentValues, String paramString)
    throws RemoteException
  {
    IccPhoneBookInterfaceManager localIccPhoneBookInterfaceManager = getIccPhoneBookInterfaceManager(paramInt1);
    if (localIccPhoneBookInterfaceManager != null) {
      return localIccPhoneBookInterfaceManager.updateAdnRecordsWithContentValuesInEfBySearch(paramInt2, paramContentValues, paramString);
    }
    paramContentValues = new StringBuilder();
    paramContentValues.append("updateAdnRecordsWithContentValuesInEfBySearchUsingSubId iccPbkIntMgr is null for Subscription:");
    paramContentValues.append(paramInt1);
    Rlog.e("UiccPhoneBookController", paramContentValues.toString());
    return false;
  }
}
