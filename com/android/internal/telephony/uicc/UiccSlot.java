package com.android.internal.telephony.uicc;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.view.Window;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class UiccSlot
  extends Handler
{
  private static final String ACTION_ASUS_CARRIER_LABEL = "com.asus.intent.action.AsusCarrierLabel";
  private static final String ACTION_ASUS_SIM_STATE_CHANGED = "com.asus.intent.action.AsusSimStateChanged";
  private static final boolean DBG = true;
  private static final int EVENT_CARD_ADDED = 14;
  private static final int EVENT_CARD_REMOVED = 13;
  public static final String EXTRA_ICC_CARD_ADDED = "com.android.internal.telephony.uicc.ICC_CARD_ADDED";
  public static final int INVALID_PHONE_ID = -1;
  private static final String TAG = "UiccSlot";
  private boolean mActive;
  private AnswerToReset mAtr;
  private IccCardStatus.CardState mCardState;
  private CommandsInterface mCi;
  private Context mContext;
  private String mIccId;
  private boolean mIsEuicc;
  private CommandsInterface.RadioState mLastRadioState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
  private final Object mLock = new Object();
  private int mPhoneId = -1;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("com.asus.intent.action.AsusCarrierLabel".equals(paramAnonymousIntent.getAction()))
      {
        UiccSlot.this.log("[SystemUI] get ACTION_ASUS_CARRIER_LABEL action from systemui crash");
        Intent localIntent = new Intent("com.asus.intent.action.AsusSimStateChanged");
        Object localObject1 = null;
        paramAnonymousIntent = null;
        if (mUiccCard != null) {
          paramAnonymousContext = mUiccCard.getUiccProfile();
        } else {
          paramAnonymousContext = null;
        }
        if (getCardState() == IccCardStatus.CardState.CARDSTATE_ABSENT)
        {
          paramAnonymousContext = "ABSENT";
          paramAnonymousIntent = null;
        }
        else if (paramAnonymousContext != null)
        {
          localObject1 = UiccProfile.getIccStateIntentString(paramAnonymousContext.getState());
          paramAnonymousIntent = UiccProfile.getIccStateReason(paramAnonymousContext.getState());
          paramAnonymousContext = (Context)localObject1;
        }
        else
        {
          localObject2 = UiccSlot.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("[SystemUI] mPhoneId: ");
          localStringBuilder.append(mPhoneId);
          localStringBuilder.append(", profile: ");
          localStringBuilder.append(paramAnonymousContext);
          ((UiccSlot)localObject2).log(localStringBuilder.toString());
          paramAnonymousContext = (Context)localObject1;
        }
        Object localObject2 = UiccSlot.this;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("[SystemUI] Crash, then restart to get sim[");
        ((StringBuilder)localObject1).append(mPhoneId);
        ((StringBuilder)localObject1).append("] State: ");
        ((StringBuilder)localObject1).append(paramAnonymousContext);
        ((StringBuilder)localObject1).append(" reason: ");
        ((StringBuilder)localObject1).append(paramAnonymousIntent);
        ((UiccSlot)localObject2).log(((StringBuilder)localObject1).toString());
        localIntent.addFlags(67108864);
        localIntent.putExtra("phoneName", "Phone");
        localIntent.putExtra("ss", paramAnonymousContext);
        localIntent.putExtra("reason", paramAnonymousIntent);
        SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, mPhoneId);
        localObject1 = UiccSlot.this;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("[SystemUI] broadcast intent ACTION_ASUS_SIM_STATE_CHANGED ");
        ((StringBuilder)localObject2).append(paramAnonymousContext);
        ((StringBuilder)localObject2).append(" reason: ");
        ((StringBuilder)localObject2).append(paramAnonymousIntent);
        ((StringBuilder)localObject2).append(" for mPhoneId: ");
        ((StringBuilder)localObject2).append(mPhoneId);
        ((UiccSlot)localObject1).log(((StringBuilder)localObject2).toString());
        ActivityManager.broadcastStickyIntent(localIntent, -1);
      }
    }
  };
  private boolean mStateIsUnknown = true;
  private UiccCard mUiccCard;
  
  public UiccSlot(Context paramContext, boolean paramBoolean)
  {
    log("Creating");
    mContext = paramContext;
    mActive = paramBoolean;
    mCardState = null;
    paramContext = new IntentFilter("com.asus.intent.action.AsusCarrierLabel");
    log("[SystemUI] ACTION_ASUS_CARRIER_LABEL action registed.");
    mContext.registerReceiver(mReceiver, paramContext);
  }
  
  private boolean absentStateUpdateNeeded(IccCardStatus.CardState paramCardState)
  {
    boolean bool;
    if (((paramCardState != IccCardStatus.CardState.CARDSTATE_ABSENT) || (mUiccCard != null)) && (mCardState == IccCardStatus.CardState.CARDSTATE_ABSENT)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void checkIsEuiccSupported()
  {
    if ((mAtr != null) && (mAtr.isEuiccSupported())) {
      mIsEuicc = true;
    } else {
      mIsEuicc = false;
    }
  }
  
  private void log(String paramString)
  {
    Rlog.d("UiccSlot", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("UiccSlot", paramString);
  }
  
  private void nullifyUiccCard(boolean paramBoolean)
  {
    mStateIsUnknown = paramBoolean;
    mUiccCard = null;
  }
  
  private void onIccSwap(boolean paramBoolean)
  {
    if (mContext.getResources().getBoolean(17956987))
    {
      log("onIccSwap: isHotSwapSupported is true, don't prompt for rebooting");
      return;
    }
    log("onIccSwap: isHotSwapSupported is false, prompt for rebooting");
    promptForRestart(paramBoolean);
  }
  
  private void parseAtr(String paramString)
  {
    mAtr = AnswerToReset.parseAtr(paramString);
    if (mAtr == null) {
      return;
    }
    checkIsEuiccSupported();
  }
  
  private void promptForRestart(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      String str1 = mContext.getResources().getString(17039723);
      if (str1 != null)
      {
        localObject2 = new android/content/Intent;
        ((Intent)localObject2).<init>();
        localObject2 = ((Intent)localObject2).setComponent(ComponentName.unflattenFromString(str1)).addFlags(268435456).putExtra("com.android.internal.telephony.uicc.ICC_CARD_ADDED", paramBoolean);
        try
        {
          mContext.startActivity((Intent)localObject2);
          return;
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Unable to find ICC hotswap prompt for restart activity: ");
          ((StringBuilder)localObject2).append(localActivityNotFoundException);
          loge(((StringBuilder)localObject2).toString());
        }
      }
      DialogInterface.OnClickListener local2 = new com/android/internal/telephony/uicc/UiccSlot$2;
      local2.<init>(this);
      Object localObject4 = Resources.getSystem();
      if (paramBoolean) {
        localObject2 = ((Resources)localObject4).getString(17041013);
      } else {
        localObject2 = ((Resources)localObject4).getString(17041016);
      }
      String str2;
      if (paramBoolean) {
        str2 = ((Resources)localObject4).getString(17041012);
      } else {
        str2 = ((Resources)localObject4).getString(17041015);
      }
      String str3 = ((Resources)localObject4).getString(17041017);
      localObject4 = new android/app/AlertDialog$Builder;
      ((AlertDialog.Builder)localObject4).<init>(mContext);
      Object localObject2 = ((AlertDialog.Builder)localObject4).setTitle((CharSequence)localObject2).setMessage(str2).setPositiveButton(str3, local2).create();
      ((AlertDialog)localObject2).getWindow().setType(2003);
      ((AlertDialog)localObject2).show();
      return;
    }
  }
  
  private void updateCardStateAbsent()
  {
    CommandsInterface.RadioState localRadioState;
    if (mCi == null) {
      localRadioState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
    } else {
      localRadioState = mCi.getRadioState();
    }
    if ((localRadioState == CommandsInterface.RadioState.RADIO_ON) && (mLastRadioState == CommandsInterface.RadioState.RADIO_ON))
    {
      log("update: notify card removed");
      sendMessage(obtainMessage(13, null));
    }
    UiccController.updateInternalIccState("ABSENT", null, mPhoneId);
    if (mUiccCard != null) {
      mUiccCard.dispose();
    }
    nullifyUiccCard(false);
    mLastRadioState = localRadioState;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("UiccSlot:");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCi=");
    localStringBuilder.append(mCi);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mActive=");
    localStringBuilder.append(mActive);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mLastRadioState=");
    localStringBuilder.append(mLastRadioState);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCardState=");
    localStringBuilder.append(mCardState);
    paramPrintWriter.println(localStringBuilder.toString());
    if (mUiccCard != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" mUiccCard=");
      localStringBuilder.append(mUiccCard);
      paramPrintWriter.println(localStringBuilder.toString());
      mUiccCard.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    else
    {
      paramPrintWriter.println(" mUiccCard=null");
    }
    paramPrintWriter.println();
    paramPrintWriter.flush();
    paramPrintWriter.flush();
  }
  
  protected void finalize()
  {
    log("UiccSlot finalized");
  }
  
  public IccCardStatus.CardState getCardState()
  {
    synchronized (mLock)
    {
      if (mCardState == null)
      {
        localCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        return localCardState;
      }
      IccCardStatus.CardState localCardState = mCardState;
      return localCardState;
    }
  }
  
  public String getIccId()
  {
    if (mIccId != null) {
      return mIccId;
    }
    if (mUiccCard != null) {
      return mUiccCard.getIccId();
    }
    return null;
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public UiccCard getUiccCard()
  {
    synchronized (mLock)
    {
      UiccCard localUiccCard = mUiccCard;
      return localUiccCard;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown Event ");
      localStringBuilder.append(what);
      loge(localStringBuilder.toString());
      break;
    case 14: 
      onIccSwap(true);
      break;
    case 13: 
      onIccSwap(false);
    }
  }
  
  public boolean isActive()
  {
    return mActive;
  }
  
  public boolean isEuicc()
  {
    return mIsEuicc;
  }
  
  public boolean isExtendedApduSupported()
  {
    boolean bool;
    if ((mAtr != null) && (mAtr.isExtendedApduSupported())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStateUnknown()
  {
    boolean bool;
    if (((mCardState == null) || (mCardState == IccCardStatus.CardState.CARDSTATE_ABSENT)) && (mStateIsUnknown)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onRadioStateUnavailable()
  {
    if (mUiccCard != null) {
      mUiccCard.dispose();
    }
    nullifyUiccCard(true);
    if (mPhoneId != -1) {
      UiccController.updateInternalIccState("UNKNOWN", null, mPhoneId);
    }
    mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
    mLastRadioState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
  }
  
  public void update(CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus, int paramInt)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("cardStatus update: ");
    ((StringBuilder)???).append(paramIccCardStatus.toString());
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      Object localObject2 = mCardState;
      mCardState = mCardState;
      mIccId = iccid;
      mPhoneId = paramInt;
      parseAtr(atr);
      mCi = paramCommandsInterface;
      paramCommandsInterface = mCi.getRadioState();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("update: radioState=");
      localStringBuilder.append(paramCommandsInterface);
      localStringBuilder.append(" mLastRadioState=");
      localStringBuilder.append(mLastRadioState);
      log(localStringBuilder.toString());
      if (absentStateUpdateNeeded((IccCardStatus.CardState)localObject2))
      {
        updateCardStateAbsent();
      }
      else if (((localObject2 == null) || (localObject2 == IccCardStatus.CardState.CARDSTATE_ABSENT) || (mUiccCard == null)) && (mCardState != IccCardStatus.CardState.CARDSTATE_ABSENT))
      {
        if ((paramCommandsInterface == CommandsInterface.RadioState.RADIO_ON) && (mLastRadioState == CommandsInterface.RadioState.RADIO_ON))
        {
          log("update: notify card added");
          sendMessage(obtainMessage(14, null));
        }
        if (mUiccCard != null)
        {
          loge("update: mUiccCard != null when card was present; disposing it now");
          mUiccCard.dispose();
        }
        if (!mIsEuicc)
        {
          localObject2 = new com/android/internal/telephony/uicc/UiccCard;
          ((UiccCard)localObject2).<init>(mContext, mCi, paramIccCardStatus, mPhoneId, mLock);
          mUiccCard = ((UiccCard)localObject2);
        }
        else
        {
          localObject2 = new com/android/internal/telephony/uicc/euicc/EuiccCard;
          ((EuiccCard)localObject2).<init>(mContext, mCi, paramIccCardStatus, paramInt, mLock);
          mUiccCard = ((UiccCard)localObject2);
        }
      }
      else if (mUiccCard != null)
      {
        mUiccCard.update(mContext, mCi, paramIccCardStatus);
      }
      mLastRadioState = paramCommandsInterface;
      return;
    }
  }
  
  public void update(CommandsInterface paramCommandsInterface, IccSlotStatus paramIccSlotStatus)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("slotStatus update: ");
    ((StringBuilder)???).append(paramIccSlotStatus.toString());
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      IccCardStatus.CardState localCardState = mCardState;
      mCi = paramCommandsInterface;
      parseAtr(atr);
      mCardState = cardState;
      mIccId = iccid;
      if (slotState == IccSlotStatus.SlotState.SLOTSTATE_INACTIVE)
      {
        if (mActive)
        {
          mActive = false;
          mLastRadioState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
          mPhoneId = -1;
          if (mUiccCard != null) {
            mUiccCard.dispose();
          }
          nullifyUiccCard(true);
        }
      }
      else
      {
        mActive = true;
        mPhoneId = logicalSlotIndex;
        if (absentStateUpdateNeeded(localCardState)) {
          updateCardStateAbsent();
        }
      }
      return;
    }
  }
}
