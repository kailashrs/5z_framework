package com.android.internal.telephony.cat;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.LocaleList;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.uicc.IccCardStatus.CardState;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

public class CatService
  extends Handler
  implements AppInterface
{
  private static final boolean DBG = false;
  private static final int DEV_ID_DISPLAY = 2;
  private static final int DEV_ID_KEYPAD = 1;
  private static final int DEV_ID_NETWORK = 131;
  private static final int DEV_ID_TERMINAL = 130;
  private static final int DEV_ID_UICC = 129;
  protected static final int MSG_ID_ALPHA_NOTIFY = 9;
  protected static final int MSG_ID_CALL_SETUP = 4;
  protected static final int MSG_ID_EVENT_NOTIFY = 3;
  protected static final int MSG_ID_ICC_CHANGED = 8;
  private static final int MSG_ID_ICC_RECORDS_LOADED = 20;
  private static final int MSG_ID_ICC_REFRESH = 30;
  protected static final int MSG_ID_PROACTIVE_COMMAND = 2;
  static final int MSG_ID_REFRESH = 5;
  static final int MSG_ID_RESPONSE = 6;
  static final int MSG_ID_RIL_MSG_DECODED = 10;
  protected static final int MSG_ID_SESSION_END = 1;
  static final int MSG_ID_SIM_READY = 7;
  static final String STK_DEFAULT = "Default Message";
  private static IccRecords mIccRecords;
  private static UiccCardApplication mUiccApplication;
  private static CatService[] sInstance = null;
  private static final Object sInstanceLock = new Object();
  private IccCardStatus.CardState mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
  private CommandsInterface mCmdIf;
  private Context mContext;
  private CatCmdMessage mCurrntCmd = null;
  private final EnvelopeHandler mHandler = new EnvelopeHandler(null);
  private HandlerThread mHandlerThread;
  private CatCmdMessage mMenuCmd = null;
  private RilMessageDecoder mMsgDecoder = null;
  private int mSlotId;
  private boolean mStkAppInstalled = false;
  private UiccController mUiccController;
  
  private CatService(CommandsInterface paramCommandsInterface, UiccCardApplication paramUiccCardApplication, IccRecords paramIccRecords, Context paramContext, IccFileHandler paramIccFileHandler, UiccProfile paramUiccProfile, int paramInt)
  {
    if ((paramCommandsInterface != null) && (paramUiccCardApplication != null) && (paramIccRecords != null) && (paramContext != null) && (paramIccFileHandler != null) && (paramUiccProfile != null))
    {
      mCmdIf = paramCommandsInterface;
      mContext = paramContext;
      mSlotId = paramInt;
      paramCommandsInterface = new StringBuilder();
      paramCommandsInterface.append("Cat Telephony service");
      paramCommandsInterface.append(paramInt);
      mHandlerThread = new HandlerThread(paramCommandsInterface.toString());
      mHandlerThread.start();
      mMsgDecoder = RilMessageDecoder.getInstance(this, paramIccFileHandler, paramInt);
      if (mMsgDecoder == null)
      {
        CatLog.d(this, "Null RilMessageDecoder instance");
        return;
      }
      mMsgDecoder.start();
      mCmdIf.setOnCatSessionEnd(this, 1, null);
      mCmdIf.setOnCatProactiveCmd(this, 2, null);
      mCmdIf.setOnCatEvent(this, 3, null);
      mCmdIf.setOnCatCallSetUp(this, 4, null);
      mCmdIf.registerForIccRefresh(this, 30, null);
      mCmdIf.setOnCatCcAlphaNotify(this, 9, null);
      mIccRecords = paramIccRecords;
      mUiccApplication = paramUiccCardApplication;
      mIccRecords.registerForRecordsLoaded(this, 20, null);
      paramCommandsInterface = new StringBuilder();
      paramCommandsInterface.append("registerForRecordsLoaded slotid=");
      paramCommandsInterface.append(mSlotId);
      paramCommandsInterface.append(" instance:");
      paramCommandsInterface.append(this);
      CatLog.d(this, paramCommandsInterface.toString());
      mUiccController = UiccController.getInstance();
      mUiccController.registerForIccChanged(this, 8, null);
      mStkAppInstalled = isStkAppInstalled();
      paramCommandsInterface = new StringBuilder();
      paramCommandsInterface.append("Running CAT service on Slotid: ");
      paramCommandsInterface.append(mSlotId);
      paramCommandsInterface.append(". STK app installed:");
      paramCommandsInterface.append(mStkAppInstalled);
      CatLog.d(this, paramCommandsInterface.toString());
      return;
    }
    throw new NullPointerException("Service: Input parameters must not be null");
  }
  
  private void broadcastAlphaMessage(String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Broadcasting CAT Alpha message from card: ");
    ((StringBuilder)localObject).append(paramString);
    CatLog.d(this, ((StringBuilder)localObject).toString());
    localObject = new Intent("com.android.internal.stk.alpha_notify");
    ((Intent)localObject).addFlags(268435456);
    ((Intent)localObject).putExtra("alpha_string", paramString);
    ((Intent)localObject).putExtra("SLOT_ID", mSlotId);
    ((Intent)localObject).setComponent(AppInterface.getDefaultSTKApplication());
    mContext.sendBroadcast((Intent)localObject, "android.permission.RECEIVE_STK_COMMANDS");
  }
  
  private void broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState paramCardState, IccRefreshResponse paramIccRefreshResponse)
  {
    Intent localIntent = new Intent("com.android.internal.stk.icc_status_change");
    localIntent.addFlags(268435456);
    boolean bool;
    if (paramCardState == IccCardStatus.CardState.CARDSTATE_PRESENT) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramIccRefreshResponse != null)
    {
      localIntent.putExtra("refresh_result", refreshResult);
      localIntent.putExtra("aid", aid);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Sending IccResult with Result: ");
      localStringBuilder.append(refreshResult);
      localStringBuilder.append(" aid: ");
      localStringBuilder.append(aid);
      CatLog.d(this, localStringBuilder.toString());
    }
    localIntent.putExtra("card_status", bool);
    localIntent.setComponent(AppInterface.getDefaultSTKApplication());
    localIntent.putExtra("SLOT_ID", mSlotId);
    paramIccRefreshResponse = new StringBuilder();
    paramIccRefreshResponse.append("Sending Card Status: ");
    paramIccRefreshResponse.append(paramCardState);
    paramIccRefreshResponse.append(" cardPresent: ");
    paramIccRefreshResponse.append(bool);
    paramIccRefreshResponse.append("SLOT_ID: ");
    paramIccRefreshResponse.append(mSlotId);
    CatLog.d(this, paramIccRefreshResponse.toString());
    mContext.sendBroadcast(localIntent, "android.permission.RECEIVE_STK_COMMANDS");
  }
  
  private void broadcastCatCmdIntent(CatCmdMessage paramCatCmdMessage)
  {
    Intent localIntent = new Intent("com.android.internal.stk.command");
    localIntent.addFlags(268435456);
    localIntent.putExtra("STK CMD", paramCatCmdMessage);
    localIntent.putExtra("SLOT_ID", mSlotId);
    localIntent.setComponent(AppInterface.getDefaultSTKApplication());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Sending CmdMsg: ");
    localStringBuilder.append(paramCatCmdMessage);
    localStringBuilder.append(" on slotid:");
    localStringBuilder.append(mSlotId);
    CatLog.d(this, localStringBuilder.toString());
    mContext.sendBroadcast(localIntent, "android.permission.RECEIVE_STK_COMMANDS");
  }
  
  private void changeLanguage(String paramString)
    throws RemoteException
  {
    IActivityManager localIActivityManager = ActivityManagerNative.getDefault();
    Configuration localConfiguration = localIActivityManager.getConfiguration();
    localConfiguration.setLocales(new LocaleList(new Locale(paramString), LocaleList.getDefault()));
    userSetLocale = true;
    localIActivityManager.updatePersistentConfiguration(localConfiguration);
    BackupManager.dataChanged("com.android.providers.settings");
  }
  
  private void encodeOptionalTags(CommandDetails paramCommandDetails, ResultCode paramResultCode, Input paramInput, ByteArrayOutputStream paramByteArrayOutputStream)
  {
    AppInterface.CommandType localCommandType = AppInterface.CommandType.fromInt(typeOfCommand);
    if (localCommandType != null)
    {
      int i = 1.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[localCommandType.ordinal()];
      if (i != 6) {
        switch (i)
        {
        default: 
          paramResultCode = new StringBuilder();
          paramResultCode.append("encodeOptionalTags() Unsupported Cmd details=");
          paramResultCode.append(paramCommandDetails);
          CatLog.d(this, paramResultCode.toString());
          break;
        case 9: 
        case 10: 
          if ((paramResultCode.value() != ResultCode.NO_RESPONSE_FROM_USER.value()) || (paramInput == null) || (duration == null)) {
            break;
          }
          getInKeyResponse(paramByteArrayOutputStream, paramInput);
          break;
        }
      } else if ((commandQualifier == 4) && (paramResultCode.value() == ResultCode.OK.value())) {
        getPliResponse(paramByteArrayOutputStream);
      }
    }
    else
    {
      paramResultCode = new StringBuilder();
      paramResultCode.append("encodeOptionalTags() bad Cmd details=");
      paramResultCode.append(paramCommandDetails);
      CatLog.d(this, paramResultCode.toString());
    }
  }
  
  private void eventDownload(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    Object localObject = new ByteArrayOutputStream();
    ((ByteArrayOutputStream)localObject).write(214);
    int i = 0;
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0x80 | ComprehensionTlvTag.EVENT_LIST.value());
    ((ByteArrayOutputStream)localObject).write(1);
    ((ByteArrayOutputStream)localObject).write(paramInt1);
    ((ByteArrayOutputStream)localObject).write(0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value());
    ((ByteArrayOutputStream)localObject).write(2);
    ((ByteArrayOutputStream)localObject).write(paramInt2);
    ((ByteArrayOutputStream)localObject).write(paramInt3);
    if (paramInt1 != 7)
    {
      switch (paramInt1)
      {
      default: 
        break;
      case 5: 
        CatLog.d(sInstance, " Sending Idle Screen Available event download to ICC");
        break;
      case 4: 
        break;
      }
    }
    else
    {
      CatLog.d(sInstance, " Sending Language Selection event download to ICC");
      ((ByteArrayOutputStream)localObject).write(0x80 | ComprehensionTlvTag.LANGUAGE.value());
      ((ByteArrayOutputStream)localObject).write(2);
    }
    if (paramArrayOfByte != null)
    {
      paramInt2 = paramArrayOfByte.length;
      for (paramInt1 = i; paramInt1 < paramInt2; paramInt1++) {
        ((ByteArrayOutputStream)localObject).write(paramArrayOfByte[paramInt1]);
      }
    }
    paramArrayOfByte = ((ByteArrayOutputStream)localObject).toByteArray();
    paramArrayOfByte[1] = ((byte)(byte)(paramArrayOfByte.length - 2));
    paramArrayOfByte = IccUtils.bytesToHexString(paramArrayOfByte);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("ENVELOPE COMMAND: ");
    ((StringBuilder)localObject).append(paramArrayOfByte);
    CatLog.d(this, ((StringBuilder)localObject).toString());
    mCmdIf.sendEnvelope(paramArrayOfByte, null);
  }
  
  private void getInKeyResponse(ByteArrayOutputStream paramByteArrayOutputStream, Input paramInput)
  {
    paramByteArrayOutputStream.write(ComprehensionTlvTag.DURATION.value());
    paramByteArrayOutputStream.write(2);
    Duration.TimeUnit localTimeUnit = duration.timeUnit;
    paramByteArrayOutputStream.write(Duration.TimeUnit.SECOND.value());
    paramByteArrayOutputStream.write(duration.timeInterval);
  }
  
  public static AppInterface getInstance()
  {
    int i = 0;
    SubscriptionController localSubscriptionController = SubscriptionController.getInstance();
    if (localSubscriptionController != null) {
      i = localSubscriptionController.getSlotIndex(localSubscriptionController.getDefaultSubId());
    }
    return getInstance(null, null, null, i);
  }
  
  public static AppInterface getInstance(int paramInt)
  {
    return getInstance(null, null, null, paramInt);
  }
  
  public static CatService getInstance(CommandsInterface paramCommandsInterface, Context paramContext, UiccProfile paramUiccProfile, int paramInt)
  {
    Object localObject1 = null;
    CatService[] arrayOfCatService = null;
    CatService localCatService = null;
    int i = 0;
    Object localObject2 = arrayOfCatService;
    Object localObject3 = localCatService;
    if (paramUiccProfile != null)
    {
      ??? = paramUiccProfile.getApplicationIndex(0);
      localObject1 = ???;
      localObject2 = arrayOfCatService;
      localObject3 = localCatService;
      if (??? != null)
      {
        localObject2 = ((UiccCardApplication)???).getIccFileHandler();
        localObject3 = ((UiccCardApplication)???).getIccRecords();
        localObject1 = ???;
      }
    }
    synchronized (sInstanceLock)
    {
      if (sInstance == null)
      {
        int j = TelephonyManager.getDefault().getSimCount();
        sInstance = new CatService[j];
        while (i < j)
        {
          sInstance[i] = null;
          i++;
        }
      }
      if (sInstance[paramInt] == null)
      {
        if ((paramCommandsInterface != null) && (localObject1 != null) && (localObject3 != null) && (paramContext != null) && (localObject2 != null) && (paramUiccProfile != null))
        {
          arrayOfCatService = sInstance;
          localCatService = new com/android/internal/telephony/cat/CatService;
          localCatService.<init>(paramCommandsInterface, localObject1, (IccRecords)localObject3, paramContext, (IccFileHandler)localObject2, paramUiccProfile, paramInt);
          arrayOfCatService[paramInt] = localCatService;
        }
        else
        {
          return null;
        }
      }
      else if ((localObject3 != null) && (mIccRecords != localObject3))
      {
        if (mIccRecords != null) {
          mIccRecords.unregisterForRecordsLoaded(sInstance[paramInt]);
        }
        mIccRecords = (IccRecords)localObject3;
        mUiccApplication = localObject1;
        mIccRecords.registerForRecordsLoaded(sInstance[paramInt], 20, null);
        paramCommandsInterface = sInstance[paramInt];
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("registerForRecordsLoaded slotid=");
        paramContext.append(paramInt);
        paramContext.append(" instance:");
        paramContext.append(sInstance[paramInt]);
        CatLog.d(paramCommandsInterface, paramContext.toString());
      }
      paramCommandsInterface = sInstance[paramInt];
      return paramCommandsInterface;
    }
  }
  
  private void getPliResponse(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    String str = Locale.getDefault().getLanguage();
    if (str != null)
    {
      paramByteArrayOutputStream.write(ComprehensionTlvTag.LANGUAGE.value());
      ResponseData.writeLength(paramByteArrayOutputStream, str.length());
      paramByteArrayOutputStream.write(str.getBytes(), 0, str.length());
    }
  }
  
  private void handleCmdResponse(CatResponseMessage paramCatResponseMessage)
  {
    if (!validateResponse(paramCatResponseMessage)) {
      return;
    }
    Object localObject1 = null;
    int i = 0;
    CommandDetails localCommandDetails = paramCatResponseMessage.getCmdDetails();
    Object localObject2 = AppInterface.CommandType.fromInt(typeOfCommand);
    switch (1.$SwitchMap$com$android$internal$telephony$cat$ResultCode[mResCode.ordinal()])
    {
    default: 
      return;
    case 16: 
      if (localObject2 == AppInterface.CommandType.SET_UP_CALL)
      {
        mCmdIf.handleCallSetupRequestFromSim(false, null);
        mCurrntCmd = null;
        return;
      }
    case 17: 
      localObject1 = null;
    }
    boolean bool;
    for (;;)
    {
      break label430;
      if ((localObject2 != AppInterface.CommandType.SET_UP_CALL) && (localObject2 != AppInterface.CommandType.OPEN_CHANNEL)) {
        break;
      }
      mCmdIf.handleCallSetupRequestFromSim(false, null);
      mCurrntCmd = null;
      return;
      for (;;)
      {
        break;
        i = 1;
      }
      int j = 1.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[localObject2.ordinal()];
      if (j == 5) {
        break label502;
      }
      if ((j == 17) || (j == 19)) {
        break label482;
      }
      bool = true;
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          break;
        case 9: 
        case 10: 
          localObject2 = mCurrntCmd.geInput();
          if (!yesNo)
          {
            if (i == 0) {
              localObject1 = new GetInkeyInputResponseData(mUsersInput, ucs2, packed);
            }
          }
          else {
            localObject1 = new GetInkeyInputResponseData(mUsersYesNoSelection);
          }
          break;
        case 8: 
          localObject1 = new SelectItemResponseData(mUsersMenuSelection);
          break;
        case 7: 
          if (mResCode == ResultCode.LAUNCH_BROWSER_ERROR)
          {
            paramCatResponseMessage.setAdditionalInfo(4);
          }
          else
          {
            mIncludeAdditionalInfo = false;
            mAdditionalInfo = 0;
          }
          break;
        }
        break;
      case 2: 
        if (mResCode == ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS)
        {
          paramCatResponseMessage.setAdditionalInfo(1);
        }
        else
        {
          mIncludeAdditionalInfo = false;
          mAdditionalInfo = 0;
        }
        break;
      }
    }
    label430:
    sendTerminalResponse(localCommandDetails, mResCode, mIncludeAdditionalInfo, mAdditionalInfo, (ResponseData)localObject1);
    mCurrntCmd = null;
    return;
    if (mResCode != ResultCode.HELP_INFO_REQUIRED) {
      bool = false;
    }
    sendMenuSelection(mUsersMenuSelection, bool);
    return;
    label482:
    mCmdIf.handleCallSetupRequestFromSim(mUsersConfirm, null);
    mCurrntCmd = null;
    return;
    label502:
    if (5 == mEventValue) {
      eventDownload(mEventValue, 2, 129, mAddedInfo, false);
    } else {
      eventDownload(mEventValue, 130, 129, mAddedInfo, false);
    }
  }
  
  private void handleCommand(CommandParams paramCommandParams, boolean paramBoolean)
  {
    CatLog.d(this, paramCommandParams.getCommandType().name());
    if ((paramBoolean) && (mUiccController != null))
    {
      UiccController localUiccController = mUiccController;
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("ProactiveCommand mSlotId=");
      ((StringBuilder)localObject3).append(mSlotId);
      ((StringBuilder)localObject3).append(" cmdParams=");
      ((StringBuilder)localObject3).append(paramCommandParams);
      localUiccController.addCardLog(((StringBuilder)localObject3).toString());
    }
    Object localObject3 = new CatCmdMessage(paramCommandParams);
    Object localObject1;
    Object localObject2;
    switch (1.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[paramCommandParams.getCommandType().ordinal()])
    {
    default: 
      CatLog.d(this, "Unsupported command");
      return;
    case 19: 
    case 20: 
    case 21: 
    case 22: 
      BIPClientParams localBIPClientParams = (BIPClientParams)paramCommandParams;
      boolean bool;
      try
      {
        bool = mContext.getResources().getBoolean(17957038);
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        bool = false;
      }
      if ((mTextMsg.text == null) && ((mHasAlphaId) || (bool)))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("cmd ");
        ((StringBuilder)localObject1).append(paramCommandParams.getCommandType());
        ((StringBuilder)localObject1).append(" with null alpha id");
        CatLog.d(this, ((StringBuilder)localObject1).toString());
        if (paramBoolean) {
          sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, null);
        } else if (paramCommandParams.getCommandType() == AppInterface.CommandType.OPEN_CHANNEL) {
          mCmdIf.handleCallSetupRequestFromSim(true, null);
        }
        return;
      }
      if (!mStkAppInstalled)
      {
        CatLog.d(this, "No STK application found.");
        if (paramBoolean)
        {
          sendTerminalResponse(mCmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY, false, 0, null);
          return;
        }
      }
      if ((paramBoolean) && ((paramCommandParams.getCommandType() == AppInterface.CommandType.CLOSE_CHANNEL) || (paramCommandParams.getCommandType() == AppInterface.CommandType.RECEIVE_DATA) || (paramCommandParams.getCommandType() == AppInterface.CommandType.SEND_DATA))) {
        sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, null);
      }
      break;
    case 18: 
      localObject3 = mLanguage;
      localObject1 = ResultCode.OK;
      if ((localObject3 != null) && (((String)localObject3).length() > 0)) {
        try
        {
          changeLanguage((String)localObject3);
        }
        catch (RemoteException localRemoteException)
        {
          localObject2 = ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS;
        }
      }
      sendTerminalResponse(mCmdDet, (ResultCode)localObject2, false, 0, null);
      return;
    case 17: 
      if ((mConfirmMsg.text != null) && (mConfirmMsg.text.equals("Default Message")))
      {
        localObject2 = mContext.getText(17039418);
        mConfirmMsg.text = ((CharSequence)localObject2).toString();
      }
      break;
    case 16: 
      break;
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
      if ((mTextMsg.text != null) && (mTextMsg.text.equals("Default Message")))
      {
        localObject2 = mContext.getText(17040968);
        mTextMsg.text = ((CharSequence)localObject2).toString();
      }
      break;
    case 8: 
    case 9: 
    case 10: 
      break;
    case 7: 
      if ((mConfirmMsg.text != null) && (mConfirmMsg.text.equals("Default Message")))
      {
        localObject2 = mContext.getText(17040230);
        mConfirmMsg.text = ((CharSequence)localObject2).toString();
      }
      break;
    case 6: 
      switch (mCmdDet.commandQualifier)
      {
      default: 
        sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, null);
        break;
      case 4: 
        localObject2 = new LanguageResponseData(Locale.getDefault().getLanguage());
        sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, (ResponseData)localObject2);
        break;
      case 3: 
        localObject2 = new DTTZResponseData(null);
        sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, (ResponseData)localObject2);
      }
      return;
    case 5: 
      if (isSupportedSetupEventCommand((CatCmdMessage)localObject3)) {
        sendTerminalResponse(mCmdDet, ResultCode.OK, false, 0, null);
      } else {
        sendTerminalResponse(mCmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY, false, 0, null);
      }
      break;
    case 4: 
      if (mLoadIconFailed) {}
      for (localObject2 = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;; localObject2 = ResultCode.OK) {
        break;
      }
      sendTerminalResponse(mCmdDet, (ResultCode)localObject2, false, 0, null);
      break;
    case 3: 
      CatLog.d(this, "Pass Refresh to Stk app");
      break;
    case 2: 
      break;
    case 1: 
      if (removeMenu(((CatCmdMessage)localObject3).getMenu())) {
        mMenuCmd = null;
      } else {
        mMenuCmd = ((CatCmdMessage)localObject3);
      }
      if (mLoadIconFailed) {}
      for (localObject2 = ResultCode.PRFRMD_ICON_NOT_DISPLAYED;; localObject2 = ResultCode.OK) {
        break;
      }
      sendTerminalResponse(mCmdDet, (ResultCode)localObject2, false, 0, null);
    }
    mCurrntCmd = ((CatCmdMessage)localObject3);
    broadcastCatCmdIntent((CatCmdMessage)localObject3);
  }
  
  private void handleEnvelopeResponse(Message paramMessage)
  {
    paramMessage = (AsyncResult)obj;
    StringBuilder localStringBuilder;
    if (exception == null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleEnvelopeResponse result=");
      localStringBuilder.append(result);
      CatLog.d(this, localStringBuilder.toString());
      if ("144".equals(result))
      {
        CatLog.d(this, "handleEnvelopeResponse no data, handle here,handleSessionEnd");
        handleSessionEnd();
      }
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleEnvelopeResponse exception=");
      localStringBuilder.append(exception);
      CatLog.d(this, localStringBuilder.toString());
    }
  }
  
  private void handleRilMsg(RilMessage paramRilMessage)
  {
    if (paramRilMessage == null) {
      return;
    }
    int i = mId;
    if (i != 5)
    {
      switch (i)
      {
      default: 
        break;
      case 3: 
        if (mResCode != ResultCode.OK) {
          break;
        }
        paramRilMessage = (CommandParams)mData;
        if (paramRilMessage == null) {
          break;
        }
        handleCommand(paramRilMessage, false);
        break;
      case 2: 
        try
        {
          CommandParams localCommandParams = (CommandParams)mData;
          if (localCommandParams == null) {
            break;
          }
          if (mResCode == ResultCode.OK) {
            handleCommand(localCommandParams, true);
          } else {
            sendTerminalResponse(mCmdDet, mResCode, false, 0, null);
          }
        }
        catch (ClassCastException paramRilMessage)
        {
          CatLog.d(this, "Fail to parse proactive command");
          if (mCurrntCmd != null) {
            sendTerminalResponse(mCurrntCmd.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
          }
        }
      case 1: 
        handleSessionEnd();
        break;
      }
    }
    else
    {
      paramRilMessage = (CommandParams)mData;
      if (paramRilMessage != null) {
        handleCommand(paramRilMessage, false);
      }
    }
  }
  
  private void handleSessionEnd()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("SESSION END on ");
    ((StringBuilder)localObject).append(mSlotId);
    CatLog.d(this, ((StringBuilder)localObject).toString());
    mCurrntCmd = mMenuCmd;
    localObject = new Intent("com.android.internal.stk.session_end");
    ((Intent)localObject).putExtra("SLOT_ID", mSlotId);
    ((Intent)localObject).setComponent(AppInterface.getDefaultSTKApplication());
    ((Intent)localObject).addFlags(268435456);
    mContext.sendBroadcast((Intent)localObject, "android.permission.RECEIVE_STK_COMMANDS");
  }
  
  private boolean isStkAppInstalled()
  {
    Intent localIntent = new Intent("com.android.internal.stk.command");
    Object localObject = mContext.getPackageManager();
    localObject = ((PackageManager)localObject).queryBroadcastReceivers(localIntent, 128);
    boolean bool = false;
    int i;
    if (localObject == null) {
      i = 0;
    } else {
      i = ((List)localObject).size();
    }
    if (i > 0) {
      bool = true;
    }
    return bool;
  }
  
  private boolean isSupportedSetupEventCommand(CatCmdMessage paramCatCmdMessage)
  {
    boolean bool = true;
    for (int k : getSetEventListeventList)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Event: ");
      localStringBuilder.append(k);
      CatLog.d(this, localStringBuilder.toString());
      if (k != 7) {
        switch (k)
        {
        default: 
          bool = false;
        }
      }
    }
    return bool;
  }
  
  private boolean removeMenu(Menu paramMenu)
  {
    try
    {
      if (items.size() == 1)
      {
        paramMenu = items.get(0);
        if (paramMenu == null) {
          return true;
        }
      }
      return false;
    }
    catch (NullPointerException paramMenu)
    {
      CatLog.d(this, "Unable to get Menu's items size");
    }
    return true;
  }
  
  private void sendMenuSelection(int paramInt, boolean paramBoolean)
  {
    Object localObject = new ByteArrayOutputStream();
    ((ByteArrayOutputStream)localObject).write(211);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value());
    ((ByteArrayOutputStream)localObject).write(2);
    ((ByteArrayOutputStream)localObject).write(1);
    ((ByteArrayOutputStream)localObject).write(129);
    ((ByteArrayOutputStream)localObject).write(0x80 | ComprehensionTlvTag.ITEM_ID.value());
    ((ByteArrayOutputStream)localObject).write(1);
    ((ByteArrayOutputStream)localObject).write(paramInt);
    if (paramBoolean)
    {
      ((ByteArrayOutputStream)localObject).write(ComprehensionTlvTag.HELP_REQUEST.value());
      ((ByteArrayOutputStream)localObject).write(0);
    }
    localObject = ((ByteArrayOutputStream)localObject).toByteArray();
    localObject[1] = ((byte)(byte)(localObject.length - 2));
    localObject = IccUtils.bytesToHexString((byte[])localObject);
    mCmdIf.sendEnvelope((String)localObject, mHandler.obtainMessage(0));
  }
  
  private void sendTerminalResponse(CommandDetails paramCommandDetails, ResultCode paramResultCode, boolean paramBoolean, int paramInt, ResponseData paramResponseData)
  {
    if (paramCommandDetails == null) {
      return;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    Input localInput = null;
    if (mCurrntCmd != null) {
      localInput = mCurrntCmd.geInput();
    }
    int i = ComprehensionTlvTag.COMMAND_DETAILS.value();
    int j = i;
    if (compRequired) {
      j = i | 0x80;
    }
    localByteArrayOutputStream.write(j);
    localByteArrayOutputStream.write(3);
    localByteArrayOutputStream.write(commandNumber);
    localByteArrayOutputStream.write(typeOfCommand);
    localByteArrayOutputStream.write(commandQualifier);
    localByteArrayOutputStream.write(ComprehensionTlvTag.DEVICE_IDENTITIES.value());
    i = 2;
    localByteArrayOutputStream.write(2);
    localByteArrayOutputStream.write(130);
    localByteArrayOutputStream.write(129);
    int k = ComprehensionTlvTag.RESULT.value();
    j = k;
    if (compRequired) {
      j = k | 0x80;
    }
    localByteArrayOutputStream.write(j);
    if (paramBoolean) {
      j = i;
    } else {
      j = 1;
    }
    localByteArrayOutputStream.write(j);
    localByteArrayOutputStream.write(paramResultCode.value());
    if (paramBoolean) {
      localByteArrayOutputStream.write(paramInt);
    }
    if (paramResponseData != null) {
      paramResponseData.format(localByteArrayOutputStream);
    } else {
      encodeOptionalTags(paramCommandDetails, paramResultCode, localInput, localByteArrayOutputStream);
    }
    paramCommandDetails = IccUtils.bytesToHexString(localByteArrayOutputStream.toByteArray());
    mCmdIf.sendTerminalResponse(paramCommandDetails, null);
  }
  
  private boolean validateResponse(CatResponseMessage paramCatResponseMessage)
  {
    boolean bool = false;
    if ((mCmdDet.typeOfCommand != AppInterface.CommandType.SET_UP_EVENT_LIST.value()) && (mCmdDet.typeOfCommand != AppInterface.CommandType.SET_UP_MENU.value()))
    {
      if (mCurrntCmd != null)
      {
        bool = mCmdDet.compareTo(mCurrntCmd.mCmdDet);
        paramCatResponseMessage = new StringBuilder();
        paramCatResponseMessage.append("isResponse for last valid cmd: ");
        paramCatResponseMessage.append(bool);
        CatLog.d(this, paramCatResponseMessage.toString());
      }
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CmdType: ");
      localStringBuilder.append(mCmdDet.typeOfCommand);
      CatLog.d(this, localStringBuilder.toString());
      bool = true;
    }
    return bool;
  }
  
  public void dispose()
  {
    synchronized (sInstanceLock)
    {
      CatLog.d(this, "Disposing CatService object");
      mIccRecords.unregisterForRecordsLoaded(this);
      broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState.CARDSTATE_ABSENT, null);
      mCmdIf.unSetOnCatSessionEnd(this);
      mCmdIf.unSetOnCatProactiveCmd(this);
      mCmdIf.unSetOnCatEvent(this);
      mCmdIf.unSetOnCatCallSetUp(this);
      mCmdIf.unSetOnCatCcAlphaNotify(this);
      mCmdIf.unregisterForIccRefresh(this);
      if (mUiccController != null)
      {
        mUiccController.unregisterForIccChanged(this);
        mUiccController = null;
      }
      mMsgDecoder.dispose();
      mMsgDecoder = null;
      mHandlerThread.quit();
      mHandlerThread = null;
      removeCallbacksAndMessages(null);
      if (sInstance != null) {
        if (SubscriptionManager.isValidSlotIndex(mSlotId))
        {
          sInstance[mSlotId] = null;
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("error: invaild slot id: ");
          localStringBuilder.append(mSlotId);
          CatLog.d(this, localStringBuilder.toString());
        }
      }
      return;
    }
  }
  
  protected void finalize()
  {
    CatLog.d(this, "Service finalized");
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("handleMessage[");
    ((StringBuilder)localObject1).append(what);
    ((StringBuilder)localObject1).append("]");
    CatLog.d(this, ((StringBuilder)localObject1).toString());
    int i = what;
    if (i != 20) {
      if (i != 30)
      {
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unrecognized CAT command: ");
            ((StringBuilder)localObject1).append(what);
            throw new AssertionError(((StringBuilder)localObject1).toString());
          case 10: 
            handleRilMsg((RilMessage)obj);
            break;
          case 9: 
            CatLog.d(this, "Received CAT CC Alpha message from card");
            if (obj != null)
            {
              paramMessage = (AsyncResult)obj;
              if ((paramMessage != null) && (result != null)) {
                broadcastAlphaMessage((String)result);
              } else {
                CatLog.d(this, "CAT Alpha message: ar.result is null");
              }
            }
            else
            {
              CatLog.d(this, "CAT Alpha message: msg.obj is null");
            }
            break;
          case 8: 
            CatLog.d(this, "MSG_ID_ICC_CHANGED");
            updateIccAvailability();
          }
          break;
        case 6: 
          handleCmdResponse((CatResponseMessage)obj);
          break;
        case 4: 
          mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(what, null));
          break;
        case 1: 
        case 2: 
        case 3: 
        case 5: 
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("ril message arrived,slotid:");
          ((StringBuilder)localObject1).append(mSlotId);
          CatLog.d(this, ((StringBuilder)localObject1).toString());
          Object localObject2 = null;
          localObject1 = localObject2;
          if (obj != null)
          {
            AsyncResult localAsyncResult = (AsyncResult)obj;
            localObject1 = localObject2;
            if (localAsyncResult != null)
            {
              localObject1 = localObject2;
              if (result != null) {
                try
                {
                  localObject1 = (String)result;
                }
                catch (ClassCastException paramMessage)
                {
                  break;
                }
              }
            }
          }
          mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(what, (String)localObject1));
          break;
        }
      }
      else if (obj != null)
      {
        localObject1 = (AsyncResult)obj;
        if ((localObject1 != null) && (result != null))
        {
          broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState.CARDSTATE_PRESENT, (IccRefreshResponse)result);
        }
        else
        {
          paramMessage = new StringBuilder();
          paramMessage.append("Icc REFRESH with exception: ");
          paramMessage.append(exception);
          CatLog.d(this, paramMessage.toString());
        }
      }
      else
      {
        CatLog.d(this, "IccRefresh Message is null");
      }
    }
  }
  
  public void onCmdResponse(CatResponseMessage paramCatResponseMessage)
  {
    if (paramCatResponseMessage == null) {
      return;
    }
    try
    {
      obtainMessage(6, paramCatResponseMessage).sendToTarget();
      return;
    }
    finally
    {
      paramCatResponseMessage = finally;
      throw paramCatResponseMessage;
    }
  }
  
  public void update(CommandsInterface paramCommandsInterface, Context paramContext, UiccProfile paramUiccProfile)
  {
    paramContext = null;
    Object localObject = null;
    paramCommandsInterface = localObject;
    if (paramUiccProfile != null)
    {
      paramUiccProfile = paramUiccProfile.getApplicationIndex(0);
      paramContext = paramUiccProfile;
      paramCommandsInterface = localObject;
      if (paramUiccProfile != null)
      {
        paramCommandsInterface = paramUiccProfile.getIccRecords();
        paramContext = paramUiccProfile;
      }
    }
    paramUiccProfile = sInstanceLock;
    if (paramCommandsInterface != null) {
      try
      {
        if (mIccRecords != paramCommandsInterface)
        {
          if (mIccRecords != null) {
            mIccRecords.unregisterForRecordsLoaded(this);
          }
          CatLog.d(this, "Reinitialize the Service with SIMRecords and UiccCardApplication");
          mIccRecords = paramCommandsInterface;
          mUiccApplication = paramContext;
          mIccRecords.registerForRecordsLoaded(this, 20, null);
          paramCommandsInterface = new java/lang/StringBuilder;
          paramCommandsInterface.<init>();
          paramCommandsInterface.append("registerForRecordsLoaded slotid=");
          paramCommandsInterface.append(mSlotId);
          paramCommandsInterface.append(" instance:");
          paramCommandsInterface.append(this);
          CatLog.d(this, paramCommandsInterface.toString());
        }
      }
      finally
      {
        break label144;
      }
    }
    return;
    label144:
    throw paramCommandsInterface;
  }
  
  void updateIccAvailability()
  {
    if (mUiccController == null) {
      return;
    }
    IccCardStatus.CardState localCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
    Object localObject = mUiccController.getUiccCard(mSlotId);
    if (localObject != null) {
      localCardState = ((UiccCard)localObject).getCardState();
    }
    localObject = mCardState;
    mCardState = localCardState;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("New Card State = ");
    localStringBuilder.append(localCardState);
    localStringBuilder.append(" Old Card State = ");
    localStringBuilder.append(localObject);
    CatLog.d(this, localStringBuilder.toString());
    if ((localObject == IccCardStatus.CardState.CARDSTATE_PRESENT) && (localCardState != IccCardStatus.CardState.CARDSTATE_PRESENT)) {
      broadcastCardStateAndIccRefreshResp(localCardState, null);
    } else if ((localObject != IccCardStatus.CardState.CARDSTATE_PRESENT) && (localCardState == IccCardStatus.CardState.CARDSTATE_PRESENT)) {
      mCmdIf.reportStkServiceIsRunning(null);
    }
  }
  
  private class EnvelopeHandler
    extends Handler
  {
    public static final String ENVELOPE_NO_DATA = "144";
    public static final int MESSAGE_ENVELOPE_RESPONSE = 0;
    
    private EnvelopeHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 0) {
        CatService.this.handleEnvelopeResponse(paramMessage);
      }
    }
  }
}
