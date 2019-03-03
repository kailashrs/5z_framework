package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.util.LocalLog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.telephony.SubscriptionInfoUpdater;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UiccController
  extends Handler
{
  public static final int APP_FAM_3GPP = 1;
  public static final int APP_FAM_3GPP2 = 2;
  public static final int APP_FAM_IMS = 3;
  private static final boolean DBG = true;
  private static final int EVENT_GET_ICC_STATUS_DONE = 3;
  private static final int EVENT_GET_SLOT_STATUS_DONE = 4;
  private static final int EVENT_ICC_STATUS_CHANGED = 1;
  private static final int EVENT_RADIO_AVAILABLE = 6;
  private static final int EVENT_RADIO_ON = 5;
  private static final int EVENT_RADIO_UNAVAILABLE = 7;
  private static final int EVENT_SIM_REFRESH = 8;
  private static final int EVENT_SLOT_STATUS_CHANGED = 2;
  public static final int INVALID_SLOT_ID = -1;
  private static final String LOG_TAG = "UiccController";
  private static final boolean VDBG = false;
  private static UiccController mInstance;
  private static final Object mLock = new Object();
  private static ArrayList<IccSlotStatus> sLastSlotStatus;
  static LocalLog sLocalLog = new LocalLog(100);
  private CommandsInterface[] mCis;
  private Context mContext;
  protected RegistrantList mIccChangedRegistrants = new RegistrantList();
  private boolean mIsSlotStatusSupported = true;
  private UiccStateChangedLauncher mLauncher;
  private int[] mPhoneIdToSlotId;
  private RadioConfig mRadioConfig;
  private UiccSlot[] mUiccSlots;
  
  private UiccController(Context paramContext, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    log("Creating UiccController");
    mContext = paramContext;
    mCis = paramArrayOfCommandsInterface;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("config_num_physical_slots = ");
    ((StringBuilder)localObject).append(paramContext.getResources().getInteger(17694853));
    localObject = ((StringBuilder)localObject).toString();
    log((String)localObject);
    sLocalLog.log((String)localObject);
    int i = paramContext.getResources().getInteger(17694853);
    int j = i;
    if (i < mCis.length) {
      j = mCis.length;
    }
    mUiccSlots = new UiccSlot[j];
    mPhoneIdToSlotId = new int[paramArrayOfCommandsInterface.length];
    Arrays.fill(mPhoneIdToSlotId, -1);
    mRadioConfig = RadioConfig.getInstance(mContext);
    mRadioConfig.registerForSimSlotStatusChanged(this, 2, null);
    for (j = 0; j < mCis.length; j++)
    {
      mCis[j].registerForIccStatusChanged(this, 1, Integer.valueOf(j));
      mCis[j].registerForAvailable(this, 6, Integer.valueOf(j));
      mCis[j].registerForOn(this, 5, Integer.valueOf(j));
      mCis[j].registerForNotAvailable(this, 7, Integer.valueOf(j));
      mCis[j].registerForIccRefresh(this, 8, Integer.valueOf(j));
    }
    mLauncher = new UiccStateChangedLauncher(paramContext, this);
  }
  
  private Integer getCiIndex(Message paramMessage)
  {
    Integer localInteger1 = new Integer(0);
    Integer localInteger2 = localInteger1;
    if (paramMessage != null) {
      if ((obj != null) && ((obj instanceof Integer)))
      {
        localInteger2 = (Integer)obj;
      }
      else
      {
        localInteger2 = localInteger1;
        if (obj != null)
        {
          localInteger2 = localInteger1;
          if ((obj instanceof AsyncResult))
          {
            paramMessage = (AsyncResult)obj;
            localInteger2 = localInteger1;
            if (userObj != null)
            {
              localInteger2 = localInteger1;
              if ((userObj instanceof Integer)) {
                localInteger2 = (Integer)userObj;
              }
            }
          }
        }
      }
    }
    return localInteger2;
  }
  
  public static UiccController getInstance()
  {
    synchronized (mLock)
    {
      if (mInstance != null)
      {
        localObject2 = mInstance;
        return localObject2;
      }
      Object localObject2 = new java/lang/RuntimeException;
      ((RuntimeException)localObject2).<init>("UiccController.getInstance can't be called before make()");
      throw ((Throwable)localObject2);
    }
  }
  
  private int getSlotIdFromPhoneId(int paramInt)
  {
    return mPhoneIdToSlotId[paramInt];
  }
  
  private boolean isValidPhoneIndex(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getPhoneCount())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isValidSlotIndex(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < mUiccSlots.length)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void log(String paramString)
  {
    Rlog.d("UiccController", paramString);
  }
  
  private void logPhoneIdToSlotIdMapping()
  {
    log("mPhoneIdToSlotId mapping:");
    for (int i = 0; i < mPhoneIdToSlotId.length; i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("    phoneId ");
      localStringBuilder.append(i);
      localStringBuilder.append(" slotId ");
      localStringBuilder.append(mPhoneIdToSlotId[i]);
      log(localStringBuilder.toString());
    }
  }
  
  public static UiccController make(Context paramContext, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    synchronized (mLock)
    {
      if (mInstance == null)
      {
        UiccController localUiccController = new com/android/internal/telephony/uicc/UiccController;
        localUiccController.<init>(paramContext, paramArrayOfCommandsInterface);
        mInstance = localUiccController;
        paramContext = mInstance;
        return paramContext;
      }
      paramContext = new java/lang/RuntimeException;
      paramContext.<init>("UiccController.make() should only be called once");
      throw paramContext;
    }
  }
  
  private void onGetIccCardStatusDone(AsyncResult paramAsyncResult, Integer paramInteger)
  {
    try
    {
      if (exception != null)
      {
        Rlog.e("UiccController", "Error getting ICC status. RIL_REQUEST_GET_ICC_STATUS should never return an error", exception);
        return;
      }
      if (!isValidPhoneIndex(paramInteger.intValue()))
      {
        paramAsyncResult = new java/lang/StringBuilder;
        paramAsyncResult.<init>();
        paramAsyncResult.append("onGetIccCardStatusDone: invalid index : ");
        paramAsyncResult.append(paramInteger);
        Rlog.e("UiccController", paramAsyncResult.toString());
        return;
      }
      paramAsyncResult = (IccCardStatus)result;
      Object localObject1 = sLocalLog;
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("onGetIccCardStatusDone: phoneId ");
      ((StringBuilder)localObject2).append(paramInteger);
      ((StringBuilder)localObject2).append(" IccCardStatus: ");
      ((StringBuilder)localObject2).append(paramAsyncResult);
      ((LocalLog)localObject1).log(((StringBuilder)localObject2).toString());
      int i = physicalSlotIndex;
      int j = i;
      if (i == -1) {
        j = paramInteger.intValue();
      }
      mPhoneIdToSlotId[paramInteger.intValue()] = j;
      if (mUiccSlots[j] == null)
      {
        localObject2 = mUiccSlots;
        localObject1 = new com/android/internal/telephony/uicc/UiccSlot;
        ((UiccSlot)localObject1).<init>(mContext, true);
        localObject2[j] = localObject1;
      }
      mUiccSlots[j].update(mCis[paramInteger.intValue()], paramAsyncResult, paramInteger.intValue());
      log("Notifying IccChangedRegistrants");
      paramAsyncResult = mIccChangedRegistrants;
      localObject1 = new android/os/AsyncResult;
      ((AsyncResult)localObject1).<init>(null, paramInteger, null);
      paramAsyncResult.notifyRegistrants((AsyncResult)localObject1);
      return;
    }
    finally {}
  }
  
  private void onGetSlotStatusDone(AsyncResult paramAsyncResult)
  {
    try
    {
      boolean bool = mIsSlotStatusSupported;
      if (!bool) {
        return;
      }
      Object localObject1 = exception;
      int i = 0;
      if (localObject1 != null)
      {
        if (((localObject1 instanceof CommandException)) && (((CommandException)localObject1).getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED))
        {
          log("onGetSlotStatusDone: request not supported; marking mIsSlotStatusSupported to false");
          sLocalLog.log("onGetSlotStatusDone: request not supported; marking mIsSlotStatusSupported to false");
          mIsSlotStatusSupported = false;
        }
        else
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Unexpected error getting slot status: ");
          ((StringBuilder)localObject1).append(exception);
          paramAsyncResult = ((StringBuilder)localObject1).toString();
          Rlog.e("UiccController", paramAsyncResult);
          sLocalLog.log(paramAsyncResult);
        }
        return;
      }
      Object localObject2 = (ArrayList)result;
      if (!slotStatusChanged((ArrayList)localObject2))
      {
        log("onGetSlotStatusDone: No change in slot status");
        return;
      }
      sLastSlotStatus = (ArrayList)localObject2;
      int j = 0;
      int k = 0;
      int m;
      while (k < ((ArrayList)localObject2).size())
      {
        localObject1 = (IccSlotStatus)((ArrayList)localObject2).get(k);
        if (slotState == IccSlotStatus.SlotState.SLOTSTATE_ACTIVE) {
          bool = true;
        } else {
          bool = false;
        }
        m = j;
        if (bool)
        {
          m = j + 1;
          if (isValidPhoneIndex(logicalSlotIndex))
          {
            mPhoneIdToSlotId[logicalSlotIndex] = k;
          }
          else
          {
            localObject2 = new java/lang/RuntimeException;
            paramAsyncResult = new java/lang/StringBuilder;
            paramAsyncResult.<init>();
            paramAsyncResult.append("Logical slot index ");
            paramAsyncResult.append(logicalSlotIndex);
            paramAsyncResult.append(" invalid for physical slot ");
            paramAsyncResult.append(k);
            ((RuntimeException)localObject2).<init>(paramAsyncResult.toString());
            throw ((Throwable)localObject2);
          }
        }
        if (mUiccSlots[k] == null)
        {
          localObject3 = mUiccSlots;
          paramAsyncResult = new com/android/internal/telephony/uicc/UiccSlot;
          paramAsyncResult.<init>(mContext, bool);
          localObject3[k] = paramAsyncResult;
        }
        Object localObject3 = mUiccSlots[k];
        if (bool) {
          paramAsyncResult = mCis[logicalSlotIndex];
        } else {
          paramAsyncResult = null;
        }
        ((UiccSlot)localObject3).update(paramAsyncResult, (IccSlotStatus)localObject1);
        k++;
        j = m;
      }
      if (j == mPhoneIdToSlotId.length)
      {
        localObject1 = new java/util/HashSet;
        ((HashSet)localObject1).<init>();
        paramAsyncResult = mPhoneIdToSlotId;
        m = paramAsyncResult.length;
        k = i;
        while (k < m)
        {
          j = paramAsyncResult[k];
          if (!((Set)localObject1).contains(Integer.valueOf(j)))
          {
            ((Set)localObject1).add(Integer.valueOf(j));
            k++;
          }
          else
          {
            paramAsyncResult = new java/lang/RuntimeException;
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("slotId ");
            ((StringBuilder)localObject1).append(j);
            ((StringBuilder)localObject1).append(" mapped to multiple phoneIds");
            paramAsyncResult.<init>(((StringBuilder)localObject1).toString());
            throw paramAsyncResult;
          }
        }
        paramAsyncResult = new android/content/Intent;
        paramAsyncResult.<init>("android.telephony.action.SIM_SLOT_STATUS_CHANGED");
        paramAsyncResult.addFlags(67108864);
        paramAsyncResult.addFlags(16777216);
        mContext.sendBroadcast(paramAsyncResult, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        return;
      }
      paramAsyncResult = new java/lang/RuntimeException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Number of active slots ");
      ((StringBuilder)localObject1).append(j);
      ((StringBuilder)localObject1).append(" does not match the expected value ");
      ((StringBuilder)localObject1).append(mPhoneIdToSlotId.length);
      paramAsyncResult.<init>(((StringBuilder)localObject1).toString());
      throw paramAsyncResult;
    }
    finally {}
  }
  
  private void onSimRefresh(AsyncResult paramAsyncResult, Integer paramInteger)
  {
    if (exception != null)
    {
      paramInteger = new StringBuilder();
      paramInteger.append("onSimRefresh: Sim REFRESH with exception: ");
      paramInteger.append(exception);
      Rlog.e("UiccController", paramInteger.toString());
      return;
    }
    if (!isValidPhoneIndex(paramInteger.intValue()))
    {
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("onSimRefresh: invalid index : ");
      paramAsyncResult.append(paramInteger);
      Rlog.e("UiccController", paramAsyncResult.toString());
      return;
    }
    paramAsyncResult = (IccRefreshResponse)result;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("onSimRefresh: ");
    ((StringBuilder)localObject).append(paramAsyncResult);
    log(((StringBuilder)localObject).toString());
    localObject = sLocalLog;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onSimRefresh: ");
    localStringBuilder.append(paramAsyncResult);
    ((LocalLog)localObject).log(localStringBuilder.toString());
    if (paramAsyncResult == null)
    {
      Rlog.e("UiccController", "onSimRefresh: received without input");
      return;
    }
    localObject = getUiccCardForPhone(paramInteger.intValue());
    if (localObject == null)
    {
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("onSimRefresh: refresh on null card : ");
      paramAsyncResult.append(paramInteger);
      Rlog.e("UiccController", paramAsyncResult.toString());
      return;
    }
    switch (refreshResult)
    {
    default: 
      return;
    }
    boolean bool = ((UiccCard)localObject).resetAppWithAid(aid);
    if ((bool) && (refreshResult == 2))
    {
      ((CarrierConfigManager)mContext.getSystemService("carrier_config")).updateConfigForPhoneId(paramInteger.intValue(), "UNKNOWN");
      if (mContext.getResources().getBoolean(17957014)) {
        mCis[paramInteger.intValue()].setRadioPower(false, null);
      }
    }
    mCis[paramInteger.intValue()].getIccCardStatus(obtainMessage(3, paramInteger));
  }
  
  private boolean slotStatusChanged(ArrayList<IccSlotStatus> paramArrayList)
  {
    if ((sLastSlotStatus != null) && (sLastSlotStatus.size() == paramArrayList.size()))
    {
      paramArrayList = paramArrayList.iterator();
      while (paramArrayList.hasNext())
      {
        IccSlotStatus localIccSlotStatus = (IccSlotStatus)paramArrayList.next();
        if (!sLastSlotStatus.contains(localIccSlotStatus)) {
          return true;
        }
      }
      return false;
    }
    return true;
  }
  
  static void updateInternalIccState(String paramString1, String paramString2, int paramInt)
  {
    SubscriptionInfoUpdater localSubscriptionInfoUpdater = PhoneFactory.getSubscriptionInfoUpdater();
    if (localSubscriptionInfoUpdater != null) {
      localSubscriptionInfoUpdater.updateInternalIccState(paramString1, paramString2, paramInt);
    } else {
      Rlog.e("UiccController", "subInfoUpdate is null.");
    }
  }
  
  public void addCardLog(String paramString)
  {
    sLocalLog.log(paramString);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UiccController: ");
    localStringBuilder.append(this);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mContext=");
    localStringBuilder.append(mContext);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mInstance=");
    localStringBuilder.append(mInstance);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mIccChangedRegistrants: size=");
    localStringBuilder.append(mIccChangedRegistrants.size());
    paramPrintWriter.println(localStringBuilder.toString());
    int i = 0;
    for (int j = 0; j < mIccChangedRegistrants.size(); j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("  mIccChangedRegistrants[");
      localStringBuilder.append(j);
      localStringBuilder.append("]=");
      localStringBuilder.append(((Registrant)mIccChangedRegistrants.get(j)).getHandler());
      paramPrintWriter.println(localStringBuilder.toString());
    }
    paramPrintWriter.println();
    paramPrintWriter.flush();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mUiccSlots: size=");
    localStringBuilder.append(mUiccSlots.length);
    paramPrintWriter.println(localStringBuilder.toString());
    for (j = i; j < mUiccSlots.length; j++) {
      if (mUiccSlots[j] == null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("  mUiccSlots[");
        localStringBuilder.append(j);
        localStringBuilder.append("]=null");
        paramPrintWriter.println(localStringBuilder.toString());
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("  mUiccSlots[");
        localStringBuilder.append(j);
        localStringBuilder.append("]=");
        localStringBuilder.append(mUiccSlots[j]);
        paramPrintWriter.println(localStringBuilder.toString());
        mUiccSlots[j].dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    paramPrintWriter.println(" sLocalLog= ");
    sLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public IccFileHandler getIccFileHandler(int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      Object localObject2 = getUiccCardApplication(paramInt1, paramInt2);
      if (localObject2 != null)
      {
        localObject2 = ((UiccCardApplication)localObject2).getIccFileHandler();
        return localObject2;
      }
      return null;
    }
  }
  
  public IccRecords getIccRecords(int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      Object localObject2 = getUiccCardApplication(paramInt1, paramInt2);
      if (localObject2 != null)
      {
        localObject2 = ((UiccCardApplication)localObject2).getIccRecords();
        return localObject2;
      }
      return null;
    }
  }
  
  public UiccCard getUiccCard(int paramInt)
  {
    synchronized (mLock)
    {
      UiccCard localUiccCard = getUiccCardForPhone(paramInt);
      return localUiccCard;
    }
  }
  
  public UiccCardApplication getUiccCardApplication(int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      Object localObject2 = getUiccCardForPhone(paramInt1);
      if (localObject2 != null)
      {
        localObject2 = ((UiccCard)localObject2).getApplication(paramInt2);
        return localObject2;
      }
      return null;
    }
  }
  
  public UiccCard getUiccCardForPhone(int paramInt)
  {
    synchronized (mLock)
    {
      if (isValidPhoneIndex(paramInt))
      {
        Object localObject2 = getUiccSlotForPhone(paramInt);
        if (localObject2 != null)
        {
          localObject2 = ((UiccSlot)localObject2).getUiccCard();
          return localObject2;
        }
      }
      return null;
    }
  }
  
  public UiccCard getUiccCardForSlot(int paramInt)
  {
    synchronized (mLock)
    {
      Object localObject2 = getUiccSlot(paramInt);
      if (localObject2 != null)
      {
        localObject2 = ((UiccSlot)localObject2).getUiccCard();
        return localObject2;
      }
      return null;
    }
  }
  
  public UiccProfile getUiccProfileForPhone(int paramInt)
  {
    synchronized (mLock)
    {
      boolean bool = isValidPhoneIndex(paramInt);
      UiccProfile localUiccProfile = null;
      if (bool)
      {
        UiccCard localUiccCard = getUiccCardForPhone(paramInt);
        if (localUiccCard != null) {
          localUiccProfile = localUiccCard.getUiccProfile();
        }
        return localUiccProfile;
      }
      return null;
    }
  }
  
  public UiccSlot getUiccSlot(int paramInt)
  {
    synchronized (mLock)
    {
      if (isValidSlotIndex(paramInt))
      {
        UiccSlot localUiccSlot = mUiccSlots[paramInt];
        return localUiccSlot;
      }
      return null;
    }
  }
  
  public int getUiccSlotForCardId(String paramString)
  {
    Object localObject = mLock;
    int i = 0;
    int j = 0;
    try
    {
      while (j < mUiccSlots.length)
      {
        if (mUiccSlots[j] != null)
        {
          UiccCard localUiccCard = mUiccSlots[j].getUiccCard();
          if ((localUiccCard != null) && (paramString.equals(localUiccCard.getCardId()))) {
            return j;
          }
        }
        j++;
      }
      for (j = i; j < mUiccSlots.length; j++) {
        if ((mUiccSlots[j] != null) && (paramString.equals(mUiccSlots[j].getIccId()))) {
          return j;
        }
      }
      return -1;
    }
    finally {}
  }
  
  public UiccSlot getUiccSlotForPhone(int paramInt)
  {
    synchronized (mLock)
    {
      if (isValidPhoneIndex(paramInt))
      {
        paramInt = getSlotIdFromPhoneId(paramInt);
        if (isValidSlotIndex(paramInt))
        {
          UiccSlot localUiccSlot = mUiccSlots[paramInt];
          return localUiccSlot;
        }
      }
      return null;
    }
  }
  
  public UiccSlot[] getUiccSlots()
  {
    synchronized (mLock)
    {
      UiccSlot[] arrayOfUiccSlot = mUiccSlots;
      return arrayOfUiccSlot;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    synchronized (mLock)
    {
      Object localObject2 = getCiIndex(paramMessage);
      if ((((Integer)localObject2).intValue() >= 0) && (((Integer)localObject2).intValue() < mCis.length))
      {
        LocalLog localLocalLog = sLocalLog;
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append("handleMessage: Received ");
        ((StringBuilder)localObject3).append(what);
        ((StringBuilder)localObject3).append(" for phoneId ");
        ((StringBuilder)localObject3).append(localObject2);
        localLocalLog.log(((StringBuilder)localObject3).toString());
        localObject3 = (AsyncResult)obj;
        switch (what)
        {
        default: 
          break;
        case 8: 
          log("Received EVENT_SIM_REFRESH");
          onSimRefresh((AsyncResult)localObject3, (Integer)localObject2);
          break;
        case 7: 
          log("EVENT_RADIO_UNAVAILABLE, dispose card");
          paramMessage = getUiccSlotForPhone(((Integer)localObject2).intValue());
          if (paramMessage != null) {
            paramMessage.onRadioStateUnavailable();
          }
          paramMessage = mIccChangedRegistrants;
          localObject3 = new android/os/AsyncResult;
          ((AsyncResult)localObject3).<init>(null, localObject2, null);
          paramMessage.notifyRegistrants((AsyncResult)localObject3);
          break;
        case 6: 
          log("Received EVENT_RADIO_AVAILABLE/EVENT_RADIO_ON, calling getIccCardStatus");
          mCis[localObject2.intValue()].getIccCardStatus(obtainMessage(3, localObject2));
          if (((Integer)localObject2).intValue() != 0) {
            break label466;
          }
          log("Received EVENT_RADIO_AVAILABLE/EVENT_RADIO_ON for phoneId 0, calling getIccSlotsStatus");
          mRadioConfig.getSimSlotsStatus(obtainMessage(4, localObject2));
          break;
        case 5: 
          log("Received EVENT_RADIO_ON");
          mCis[localObject2.intValue()].getIccCardStatus(obtainMessage(3, localObject2));
          paramMessage = getUiccProfileForPhone(((Integer)localObject2).intValue());
          if (paramMessage == null) {
            break label466;
          }
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("[ASIM] IccCardProxy Radio ON, Curr State: ");
          ((StringBuilder)localObject2).append(paramMessage.getState());
          ((StringBuilder)localObject2).append(",case 2 (APM)");
          log(((StringBuilder)localObject2).toString());
          paramMessage.updateRadioOn();
          break;
        case 3: 
          log("Received EVENT_GET_ICC_STATUS_DONE");
          onGetIccCardStatusDone((AsyncResult)localObject3, (Integer)localObject2);
          break;
        case 2: 
        case 4: 
          log("Received EVENT_SLOT_STATUS_CHANGED or EVENT_GET_SLOT_STATUS_DONE");
          onGetSlotStatusDone((AsyncResult)localObject3);
          break;
        case 1: 
          log("Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
          mCis[localObject2.intValue()].getIccCardStatus(obtainMessage(3, localObject2));
          break;
        }
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append(" Unknown Event ");
        ((StringBuilder)localObject2).append(what);
        Rlog.e("UiccController", ((StringBuilder)localObject2).toString());
        label466:
        return;
      }
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("Invalid phoneId : ");
      ((StringBuilder)localObject3).append(localObject2);
      ((StringBuilder)localObject3).append(" received with event ");
      ((StringBuilder)localObject3).append(what);
      Rlog.e("UiccController", ((StringBuilder)localObject3).toString());
      return;
    }
  }
  
  public void registerForIccChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mIccChangedRegistrants.add(localRegistrant);
      localRegistrant.notifyRegistrant();
      return;
    }
  }
  
  public void switchSlots(int[] paramArrayOfInt, Message paramMessage)
  {
    mRadioConfig.setSimSlotsMapping(paramArrayOfInt, paramMessage);
  }
  
  public void unregisterForIccChanged(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mIccChangedRegistrants.remove(paramHandler);
      return;
    }
  }
}
