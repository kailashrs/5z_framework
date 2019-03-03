package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.TelephonyComponentFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class UiccCard
{
  protected static final boolean DBG = true;
  public static final String EXTRA_ICC_CARD_ADDED = "com.android.internal.telephony.uicc.ICC_CARD_ADDED";
  protected static final String LOG_TAG = "UiccCard";
  protected String mCardId;
  private IccCardStatus.CardState mCardState;
  private CommandsInterface mCi;
  private Context mContext;
  private String mIccid;
  private final Object mLock;
  private final int mPhoneId;
  private UiccProfile mUiccProfile;
  
  public UiccCard(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus, int paramInt, Object paramObject)
  {
    log("Creating");
    mCardState = mCardState;
    mPhoneId = paramInt;
    mLock = paramObject;
    update(paramContext, paramCommandsInterface, paramIccCardStatus);
  }
  
  private void log(String paramString)
  {
    Rlog.d("UiccCard", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("UiccCard", paramString);
  }
  
  @Deprecated
  public boolean areCarrierPriviligeRulesLoaded()
  {
    if (mUiccProfile != null) {
      return mUiccProfile.areCarrierPriviligeRulesLoaded();
    }
    return false;
  }
  
  public void dispose()
  {
    synchronized (mLock)
    {
      log("Disposing card");
      if (mUiccProfile != null) {
        mUiccProfile.dispose();
      }
      mUiccProfile = null;
      return;
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("UiccCard:");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCi=");
    localStringBuilder.append(mCi);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCardState=");
    localStringBuilder.append(mCardState);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.println();
    if (mUiccProfile != null) {
      mUiccProfile.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  protected void finalize()
  {
    log("UiccCard finalized");
  }
  
  @Deprecated
  public UiccCardApplication getApplication(int paramInt)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        UiccCardApplication localUiccCardApplication = mUiccProfile.getApplication(paramInt);
        return localUiccCardApplication;
      }
      return null;
    }
  }
  
  @Deprecated
  public UiccCardApplication getApplicationByType(int paramInt)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        UiccCardApplication localUiccCardApplication = mUiccProfile.getApplicationByType(paramInt);
        return localUiccCardApplication;
      }
      return null;
    }
  }
  
  @Deprecated
  public UiccCardApplication getApplicationIndex(int paramInt)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        UiccCardApplication localUiccCardApplication = mUiccProfile.getApplicationIndex(paramInt);
        return localUiccCardApplication;
      }
      return null;
    }
  }
  
  public String getCardId()
  {
    if (mCardId != null) {
      return mCardId;
    }
    if (mUiccProfile != null) {
      return mUiccProfile.getIccId();
    }
    return null;
  }
  
  public IccCardStatus.CardState getCardState()
  {
    synchronized (mLock)
    {
      IccCardStatus.CardState localCardState = mCardState;
      return localCardState;
    }
  }
  
  @Deprecated
  public List<String> getCarrierPackageNamesForIntent(PackageManager paramPackageManager, Intent paramIntent)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getCarrierPackageNamesForIntent(paramPackageManager, paramIntent);
    }
    return null;
  }
  
  @Deprecated
  public int getCarrierPrivilegeStatus(PackageInfo paramPackageInfo)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getCarrierPrivilegeStatus(paramPackageInfo);
    }
    return -1;
  }
  
  @Deprecated
  public int getCarrierPrivilegeStatus(PackageManager paramPackageManager, String paramString)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getCarrierPrivilegeStatus(paramPackageManager, paramString);
    }
    return -1;
  }
  
  @Deprecated
  public int getCarrierPrivilegeStatus(Signature paramSignature, String paramString)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getCarrierPrivilegeStatus(paramSignature, paramString);
    }
    return -1;
  }
  
  @Deprecated
  public int getCarrierPrivilegeStatusForCurrentTransaction(PackageManager paramPackageManager)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getCarrierPrivilegeStatusForCurrentTransaction(paramPackageManager);
    }
    return -1;
  }
  
  public String getIccId()
  {
    if (mIccid != null) {
      return mIccid;
    }
    if (mUiccProfile != null) {
      return mUiccProfile.getIccId();
    }
    return null;
  }
  
  @Deprecated
  public int getNumApplications()
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getNumApplications();
    }
    return 0;
  }
  
  @Deprecated
  public String getOperatorBrandOverride()
  {
    if (mUiccProfile != null) {
      return mUiccProfile.getOperatorBrandOverride();
    }
    return null;
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public int getSimType()
  {
    int i = 1;
    int k;
    for (int j = 0;; j++)
    {
      k = i;
      if (j >= getNumApplications()) {
        break;
      }
      if (getApplicationIndex(j).getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM)
      {
        k = 2;
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("[CSIM] Phone[");
        localStringBuilder.append(mPhoneId);
        localStringBuilder.append("], App[");
        localStringBuilder.append(j);
        localStringBuilder.append("] is CDMA.");
        log(localStringBuilder.toString());
        break;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[CSIM] curPhoneType = ");
    localStringBuilder.append(k);
    log(localStringBuilder.toString());
    return k;
  }
  
  public UiccProfile getUiccProfile()
  {
    return mUiccProfile;
  }
  
  @Deprecated
  public IccCardStatus.PinState getUniversalPinState()
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        localPinState = mUiccProfile.getUniversalPinState();
        return localPinState;
      }
      IccCardStatus.PinState localPinState = IccCardStatus.PinState.PINSTATE_UNKNOWN;
      return localPinState;
    }
  }
  
  @Deprecated
  public boolean hasCarrierPrivilegeRules()
  {
    if (mUiccProfile != null) {
      return mUiccProfile.hasCarrierPrivilegeRules();
    }
    return false;
  }
  
  @Deprecated
  public void iccCloseLogicalChannel(int paramInt, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.iccCloseLogicalChannel(paramInt, paramMessage);
    } else {
      loge("iccCloseLogicalChannel Failed!");
    }
  }
  
  @Deprecated
  public void iccExchangeSimIO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.iccExchangeSimIO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString, paramMessage);
    } else {
      loge("iccExchangeSimIO Failed!");
    }
  }
  
  @Deprecated
  public void iccOpenLogicalChannel(String paramString, int paramInt, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.iccOpenLogicalChannel(paramString, paramInt, paramMessage);
    } else {
      loge("iccOpenLogicalChannel Failed!");
    }
  }
  
  @Deprecated
  public void iccTransmitApduBasicChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.iccTransmitApduBasicChannel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString, paramMessage);
    } else {
      loge("iccTransmitApduBasicChannel Failed!");
    }
  }
  
  @Deprecated
  public void iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.iccTransmitApduLogicalChannel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramString, paramMessage);
    } else {
      loge("iccTransmitApduLogicalChannel Failed!");
    }
  }
  
  @Deprecated
  public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType paramAppType)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        boolean bool = mUiccProfile.isApplicationOnIcc(paramAppType);
        return bool;
      }
      return false;
    }
  }
  
  @Deprecated
  public void registerForCarrierPrivilegeRulesLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null) {
        mUiccProfile.registerForCarrierPrivilegeRulesLoaded(paramHandler, paramInt, paramObject);
      } else {
        loge("registerForCarrierPrivilegeRulesLoaded Failed!");
      }
      return;
    }
  }
  
  @Deprecated
  public boolean resetAppWithAid(String paramString)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null)
      {
        boolean bool = mUiccProfile.resetAppWithAid(paramString);
        return bool;
      }
      return false;
    }
  }
  
  @Deprecated
  public void sendEnvelopeWithStatus(String paramString, Message paramMessage)
  {
    if (mUiccProfile != null) {
      mUiccProfile.sendEnvelopeWithStatus(paramString, paramMessage);
    } else {
      loge("sendEnvelopeWithStatus Failed!");
    }
  }
  
  @Deprecated
  public boolean setOperatorBrandOverride(String paramString)
  {
    if (mUiccProfile != null) {
      return mUiccProfile.setOperatorBrandOverride(paramString);
    }
    return false;
  }
  
  @Deprecated
  public void unregisterForCarrierPrivilegeRulesLoaded(Handler paramHandler)
  {
    synchronized (mLock)
    {
      if (mUiccProfile != null) {
        mUiccProfile.unregisterForCarrierPrivilegeRulesLoaded(paramHandler);
      } else {
        loge("unregisterForCarrierPrivilegeRulesLoaded Failed!");
      }
      return;
    }
  }
  
  public void update(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus)
  {
    synchronized (mLock)
    {
      mCardState = mCardState;
      mContext = paramContext;
      mCi = paramCommandsInterface;
      mIccid = iccid;
      updateCardId();
      if (mCardState != IccCardStatus.CardState.CARDSTATE_ABSENT)
      {
        if (mUiccProfile == null) {
          mUiccProfile = TelephonyComponentFactory.getInstance().makeUiccProfile(mContext, mCi, paramIccCardStatus, mPhoneId, this, mLock);
        } else {
          mUiccProfile.update(mContext, mCi, paramIccCardStatus);
        }
        return;
      }
      paramContext = new java/lang/RuntimeException;
      paramContext.<init>("Card state is absent when updating!");
      throw paramContext;
    }
  }
  
  protected void updateCardId()
  {
    mCardId = mIccid;
  }
}
