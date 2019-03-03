package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;

public class PhoneSubInfoController
  extends IPhoneSubInfo.Stub
{
  private static final boolean DBG = true;
  private static final String TAG = "PhoneSubInfoController";
  private static final boolean VDBG = false;
  private final AppOpsManager mAppOps;
  private final Context mContext;
  private final Phone[] mPhone;
  
  public PhoneSubInfoController(Context paramContext, Phone[] paramArrayOfPhone)
  {
    mPhone = paramArrayOfPhone;
    if (ServiceManager.getService("iphonesubinfo") == null) {
      ServiceManager.addService("iphonesubinfo", this);
    }
    mContext = paramContext;
    mAppOps = ((AppOpsManager)mContext.getSystemService("appops"));
  }
  
  private void enforceModifyPermission()
  {
    mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "Requires MODIFY_PHONE_STATE");
  }
  
  private void enforcePrivilegedPermissionOrCarrierPrivilege(int paramInt, String paramString)
  {
    if (mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") == 0) {
      return;
    }
    TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(paramInt, paramString);
  }
  
  private int getDefaultSubscription()
  {
    return PhoneFactory.getDefaultSubscription();
  }
  
  private Phone getPhone(int paramInt)
  {
    int i = SubscriptionManager.getPhoneId(paramInt);
    paramInt = i;
    if (!SubscriptionManager.isValidPhoneId(i)) {
      paramInt = 0;
    }
    return mPhone[paramInt];
  }
  
  private void log(String paramString)
  {
    Rlog.d("PhoneSubInfoController", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("PhoneSubInfoController", paramString);
  }
  
  public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt1, int paramInt2, String paramString)
  {
    Phone localPhone = getPhone(paramInt1);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt1, paramString, "getCarrierInfoForImsiEncryption")) {
        return null;
      }
      return localPhone.getCarrierInfoForImsiEncryption(paramInt2);
    }
    paramString = new StringBuilder();
    paramString.append("getCarrierInfoForImsiEncryption phone is null for Subscription:");
    paramString.append(paramInt1);
    loge(paramString.toString());
    return null;
  }
  
  public String getCompleteVoiceMailNumber()
  {
    return getCompleteVoiceMailNumberForSubscriber(getDefaultSubscription());
  }
  
  public String getCompleteVoiceMailNumberForSubscriber(int paramInt)
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.CALL_PRIVILEGED", "Requires CALL_PRIVILEGED");
      return ((Phone)localObject).getVoiceMailNumber();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getCompleteVoiceMailNumber phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String getDeviceId(String paramString)
  {
    return getDeviceIdForPhone(SubscriptionManager.getPhoneId(getDefaultSubscription()), paramString);
  }
  
  public String getDeviceIdForPhone(int paramInt, String paramString)
  {
    int i = paramInt;
    if (!SubscriptionManager.isValidPhoneId(paramInt)) {
      i = 0;
    }
    Phone localPhone = mPhone[i];
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, localPhone.getSubId(), paramString, "getDeviceId")) {
        return null;
      }
      return localPhone.getDeviceId();
    }
    paramString = new StringBuilder();
    paramString.append("getDeviceIdForPhone phone ");
    paramString.append(i);
    paramString.append(" is null");
    loge(paramString.toString());
    return null;
  }
  
  public String getDeviceSvn(String paramString)
  {
    return getDeviceSvnUsingSubId(getDefaultSubscription(), paramString);
  }
  
  public String getDeviceSvnUsingSubId(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getDeviceSvn")) {
        return null;
      }
      return localPhone.getDeviceSvn();
    }
    loge("getDeviceSvn phone is null");
    return null;
  }
  
  public String getGroupIdLevel1ForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getGroupIdLevel1")) {
        return null;
      }
      return localPhone.getGroupIdLevel1();
    }
    paramString = new StringBuilder();
    paramString.append("getGroupIdLevel1 phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getIccSerialNumber(String paramString)
  {
    return getIccSerialNumberForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getIccSerialNumberForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getIccSerialNumber")) {
        return null;
      }
      return localPhone.getIccSerialNumber();
    }
    paramString = new StringBuilder();
    paramString.append("getIccSerialNumber phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getIccSimChallengeResponse(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException
  {
    enforcePrivilegedPermissionOrCarrierPrivilege(paramInt1, "getIccSimChallengeResponse");
    Object localObject = getPhone(paramInt1).getUiccCard();
    if (localObject == null)
    {
      loge("getIccSimChallengeResponse() UiccCard is null");
      return null;
    }
    UiccCardApplication localUiccCardApplication = ((UiccCard)localObject).getApplicationByType(paramInt2);
    if (localUiccCardApplication == null)
    {
      paramString = new StringBuilder();
      paramString.append("getIccSimChallengeResponse() no app with specified type -- ");
      paramString.append(paramInt2);
      loge(paramString.toString());
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIccSimChallengeResponse() found app ");
    ((StringBuilder)localObject).append(localUiccCardApplication.getAid());
    ((StringBuilder)localObject).append(" specified type -- ");
    ((StringBuilder)localObject).append(paramInt2);
    loge(((StringBuilder)localObject).toString());
    if ((paramInt3 != 128) && (paramInt3 != 129))
    {
      paramString = new StringBuilder();
      paramString.append("getIccSimChallengeResponse() unsupported authType: ");
      paramString.append(paramInt3);
      loge(paramString.toString());
      return null;
    }
    return localUiccCardApplication.getIccRecords().getIccSimChallengeResponse(paramInt3, paramString);
  }
  
  public String getImeiForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getImei")) {
        return null;
      }
      return localPhone.getImei();
    }
    paramString = new StringBuilder();
    paramString.append("getDeviceId phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getIsimDomain(int paramInt)
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
      localObject = ((Phone)localObject).getIsimRecords();
      if (localObject != null) {
        return ((IsimRecords)localObject).getIsimDomain();
      }
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIsimDomain phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String getIsimImpi(int paramInt)
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
      localObject = ((Phone)localObject).getIsimRecords();
      if (localObject != null) {
        return ((IsimRecords)localObject).getIsimImpi();
      }
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIsimImpi phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String[] getIsimImpu(int paramInt)
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
      localObject = ((Phone)localObject).getIsimRecords();
      if (localObject != null) {
        return ((IsimRecords)localObject).getIsimImpu();
      }
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIsimImpu phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String getIsimIst(int paramInt)
    throws RemoteException
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
      localObject = ((Phone)localObject).getIsimRecords();
      if (localObject != null) {
        return ((IsimRecords)localObject).getIsimIst();
      }
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIsimIst phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String[] getIsimPcscf(int paramInt)
    throws RemoteException
  {
    Object localObject = getPhone(paramInt);
    if (localObject != null)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
      localObject = ((Phone)localObject).getIsimRecords();
      if (localObject != null) {
        return ((IsimRecords)localObject).getIsimPcscf();
      }
      return null;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getIsimPcscf phone is null for Subscription:");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  public String getLine1AlphaTag(String paramString)
  {
    return getLine1AlphaTagForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getLine1AlphaTagForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getLine1AlphaTag")) {
        return null;
      }
      return localPhone.getLine1AlphaTag();
    }
    paramString = new StringBuilder();
    paramString.append("getLine1AlphaTag phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getLine1Number(String paramString)
  {
    return getLine1NumberForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getLine1NumberForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(mContext, paramInt, paramString, "getLine1Number")) {
        return null;
      }
      return localPhone.getLine1Number();
    }
    paramString = new StringBuilder();
    paramString.append("getLine1Number phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getMsisdn(String paramString)
  {
    return getMsisdnForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getMsisdnForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getMsisdn")) {
        return null;
      }
      return localPhone.getMsisdn();
    }
    paramString = new StringBuilder();
    paramString.append("getMsisdn phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getNaiForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getNai")) {
        return null;
      }
      return localPhone.getNai();
    }
    paramString = new StringBuilder();
    paramString.append("getNai phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getSubscriberId(String paramString)
  {
    return getSubscriberIdForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getSubscriberIdForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getSubscriberId")) {
        return null;
      }
      return localPhone.getSubscriberId();
    }
    paramString = new StringBuilder();
    paramString.append("getSubscriberId phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getVoiceMailAlphaTag(String paramString)
  {
    return getVoiceMailAlphaTagForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getVoiceMailAlphaTagForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getVoiceMailAlphaTag")) {
        return null;
      }
      return localPhone.getVoiceMailAlphaTag();
    }
    paramString = new StringBuilder();
    paramString.append("getVoiceMailAlphaTag phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public String getVoiceMailNumber(String paramString)
  {
    return getVoiceMailNumberForSubscriber(getDefaultSubscription(), paramString);
  }
  
  public String getVoiceMailNumberForSubscriber(int paramInt, String paramString)
  {
    Phone localPhone = getPhone(paramInt);
    if (localPhone != null)
    {
      if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getVoiceMailNumber")) {
        return null;
      }
      return PhoneNumberUtils.extractNetworkPortion(localPhone.getVoiceMailNumber());
    }
    paramString = new StringBuilder();
    paramString.append("getVoiceMailNumber phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
    return null;
  }
  
  public void resetCarrierKeysForImsiEncryption(int paramInt, String paramString)
  {
    paramString = getPhone(paramInt);
    if (paramString != null)
    {
      enforceModifyPermission();
      paramString.resetCarrierKeysForImsiEncryption();
      return;
    }
    paramString = new StringBuilder();
    paramString.append("resetCarrierKeysForImsiEncryption phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
  }
  
  public void setCarrierInfoForImsiEncryption(int paramInt, String paramString, ImsiEncryptionInfo paramImsiEncryptionInfo)
  {
    paramString = getPhone(paramInt);
    if (paramString != null)
    {
      enforceModifyPermission();
      paramString.setCarrierInfoForImsiEncryption(paramImsiEncryptionInfo);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("setCarrierInfoForImsiEncryption phone is null for Subscription:");
    paramString.append(paramInt);
    loge(paramString.toString());
  }
}
