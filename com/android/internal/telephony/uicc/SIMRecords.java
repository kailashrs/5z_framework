package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RegistrantList;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.gsm.SimTlv;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class SIMRecords
  extends IccRecords
{
  static final int CFF_LINE1_MASK = 15;
  static final int CFF_LINE1_RESET = 240;
  static final int CFF_UNCONDITIONAL_ACTIVE = 10;
  static final int CFF_UNCONDITIONAL_DEACTIVE = 5;
  private static final int CFIS_ADN_CAPABILITY_ID_OFFSET = 14;
  private static final int CFIS_ADN_EXTENSION_ID_OFFSET = 15;
  private static final int CFIS_BCD_NUMBER_LENGTH_OFFSET = 2;
  private static final int CFIS_TON_NPI_OFFSET = 3;
  private static final int CPHS_SST_MBN_ENABLED = 48;
  private static final int CPHS_SST_MBN_MASK = 48;
  private static final boolean CRASH_RIL = false;
  private static final int EVENT_APP_LOCKED = 258;
  private static final int EVENT_APP_NETWORK_LOCKED = 259;
  private static final int EVENT_GET_AD_DONE = 9;
  private static final int EVENT_GET_ALL_SMS_DONE = 18;
  private static final int EVENT_GET_CFF_DONE = 24;
  private static final int EVENT_GET_CFIS_DONE = 32;
  private static final int EVENT_GET_CPHS_MAILBOX_DONE = 11;
  private static final int EVENT_GET_CSP_CPHS_DONE = 33;
  private static final int EVENT_GET_EHPLMN_DONE = 40;
  private static final int EVENT_GET_FPLMN_DONE = 41;
  private static final int EVENT_GET_GID1_DONE = 34;
  private static final int EVENT_GET_GID2_DONE = 36;
  private static final int EVENT_GET_HPLMN_W_ACT_DONE = 39;
  private static final int EVENT_GET_ICCID_DONE = 4;
  private static final int EVENT_GET_IMSI_DONE = 3;
  private static final int EVENT_GET_INFO_CPHS_DONE = 26;
  private static final int EVENT_GET_MBDN_DONE = 6;
  private static final int EVENT_GET_MBI_DONE = 5;
  private static final int EVENT_GET_MSISDN_DONE = 10;
  private static final int EVENT_GET_MWIS_DONE = 7;
  private static final int EVENT_GET_OPLMN_W_ACT_DONE = 38;
  private static final int EVENT_GET_PLMN_W_ACT_DONE = 37;
  private static final int EVENT_GET_PNN_DONE = 15;
  private static final int EVENT_GET_SMS_DONE = 22;
  private static final int EVENT_GET_SPDI_DONE = 13;
  private static final int EVENT_GET_SPN_DONE = 12;
  private static final int EVENT_GET_SST_DONE = 17;
  private static final int EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE = 8;
  private static final int EVENT_MARK_SMS_READ_DONE = 19;
  private static final int EVENT_SET_CPHS_MAILBOX_DONE = 25;
  private static final int EVENT_SET_MBDN_DONE = 20;
  private static final int EVENT_SET_MSISDN_DONE = 30;
  private static final int EVENT_SMS_ON_SIM = 21;
  private static final int EVENT_UPDATE_DONE = 14;
  protected static final String LOG_TAG = "SIMRecords";
  private static final String[] MCCMNC_CODES_HAVING_3DIGITS_MNC = { "302370", "302720", "310260", "405025", "405026", "405027", "405028", "405029", "405030", "405031", "405032", "405033", "405034", "405035", "405036", "405037", "405038", "405039", "405040", "405041", "405042", "405043", "405044", "405045", "405046", "405047", "405750", "405751", "405752", "405753", "405754", "405755", "405756", "405799", "405800", "405801", "405802", "405803", "405804", "405805", "405806", "405807", "405808", "405809", "405810", "405811", "405812", "405813", "405814", "405815", "405816", "405817", "405818", "405819", "405820", "405821", "405822", "405823", "405824", "405825", "405826", "405827", "405828", "405829", "405830", "405831", "405832", "405833", "405834", "405835", "405836", "405837", "405838", "405839", "405840", "405841", "405842", "405843", "405844", "405845", "405846", "405847", "405848", "405849", "405850", "405851", "405852", "405853", "405854", "405855", "405856", "405857", "405858", "405859", "405860", "405861", "405862", "405863", "405864", "405865", "405866", "405867", "405868", "405869", "405870", "405871", "405872", "405873", "405874", "405875", "405876", "405877", "405878", "405879", "405880", "405881", "405882", "405883", "405884", "405885", "405886", "405908", "405909", "405910", "405911", "405912", "405913", "405914", "405915", "405916", "405917", "405918", "405919", "405920", "405921", "405922", "405923", "405924", "405925", "405926", "405927", "405928", "405929", "405930", "405931", "405932", "502142", "502143", "502145", "502146", "502147", "502148" };
  private static final int SIM_RECORD_EVENT_BASE = 0;
  private static final int SYSTEM_EVENT_BASE = 256;
  static final int TAG_FULL_NETWORK_NAME = 67;
  static final int TAG_SHORT_NETWORK_NAME = 69;
  static final int TAG_SPDI = 163;
  static final int TAG_SPDI_PLMN_LIST = 128;
  private static final boolean VDBG = false;
  private int mCallForwardingStatus;
  private byte[] mCphsInfo = null;
  boolean mCspPlmnEnabled = true;
  byte[] mEfCPHS_MWI = null;
  byte[] mEfCff = null;
  byte[] mEfCfis = null;
  byte[] mEfLi = null;
  byte[] mEfMWIS = null;
  byte[] mEfPl = null;
  ArrayList<String> mSpdiNetworks = null;
  int mSpnDisplayCondition;
  private GetSpnFsmState mSpnState;
  UsimServiceTable mUsimServiceTable;
  VoiceMailConstants mVmConfig = new VoiceMailConstants();
  
  public SIMRecords(UiccCardApplication paramUiccCardApplication, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramContext, paramCommandsInterface);
    mAdnCache = new AdnRecordCache(mFh);
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mRecordsToLoad = 0;
    mCi.setOnSmsOnSim(this, 21, null);
    resetRecords();
    mParentApp.registerForReady(this, 1, null);
    mParentApp.registerForLocked(this, 258, null);
    mParentApp.registerForNetworkLocked(this, 259, null);
    paramUiccCardApplication = new StringBuilder();
    paramUiccCardApplication.append("SIMRecords X ctor this=");
    paramUiccCardApplication.append(this);
    log(paramUiccCardApplication.toString());
  }
  
  private int dispatchGsmMessage(SmsMessage paramSmsMessage)
  {
    mNewSmsRegistrants.notifyResult(paramSmsMessage);
    return 0;
  }
  
  private int getExtFromEf(int paramInt)
  {
    if (paramInt != 28480) {
      paramInt = 28490;
    } else if (mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) {
      paramInt = 28494;
    } else {
      paramInt = 28490;
    }
    return paramInt;
  }
  
  private void getSpnFsm(boolean paramBoolean, AsyncResult paramAsyncResult)
  {
    if (paramBoolean) {
      if ((mSpnState != GetSpnFsmState.READ_SPN_3GPP) && (mSpnState != GetSpnFsmState.READ_SPN_CPHS) && (mSpnState != GetSpnFsmState.READ_SPN_SHORT_CPHS) && (mSpnState != GetSpnFsmState.INIT))
      {
        mSpnState = GetSpnFsmState.INIT;
      }
      else
      {
        mSpnState = GetSpnFsmState.INIT;
        return;
      }
    }
    Object localObject;
    switch (1.$SwitchMap$com$android$internal$telephony$uicc$SIMRecords$GetSpnFsmState[mSpnState.ordinal()])
    {
    default: 
      mSpnState = GetSpnFsmState.IDLE;
      break;
    case 4: 
      if ((paramAsyncResult != null) && (exception == null))
      {
        paramAsyncResult = (byte[])result;
        setServiceProviderName(IccUtils.adnStringFieldToString(paramAsyncResult, 0, paramAsyncResult.length));
        localObject = getServiceProviderName();
        if ((localObject != null) && (((String)localObject).length() != 0))
        {
          mSpnDisplayCondition = 2;
          paramAsyncResult = new StringBuilder();
          paramAsyncResult.append("Load EF_SPN_SHORT_CPHS: ");
          paramAsyncResult.append((String)localObject);
          log(paramAsyncResult.toString());
          mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), (String)localObject);
        }
        else
        {
          log("No SPN loaded in either CHPS or 3GPP");
        }
      }
      else
      {
        setServiceProviderName(null);
        log("No SPN loaded in either CHPS or 3GPP");
      }
      mSpnState = GetSpnFsmState.IDLE;
      break;
    case 3: 
      if ((paramAsyncResult != null) && (exception == null))
      {
        paramAsyncResult = (byte[])result;
        setServiceProviderName(IccUtils.adnStringFieldToString(paramAsyncResult, 0, paramAsyncResult.length));
        localObject = getServiceProviderName();
        if ((localObject != null) && (((String)localObject).length() != 0))
        {
          mSpnDisplayCondition = 2;
          paramAsyncResult = new StringBuilder();
          paramAsyncResult.append("Load EF_SPN_CPHS: ");
          paramAsyncResult.append((String)localObject);
          log(paramAsyncResult.toString());
          mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), (String)localObject);
          mSpnState = GetSpnFsmState.IDLE;
        }
        else
        {
          mSpnState = GetSpnFsmState.READ_SPN_SHORT_CPHS;
        }
      }
      else
      {
        mSpnState = GetSpnFsmState.READ_SPN_SHORT_CPHS;
      }
      if (mSpnState == GetSpnFsmState.READ_SPN_SHORT_CPHS)
      {
        mFh.loadEFTransparent(28440, obtainMessage(12));
        mRecordsToLoad += 1;
      }
      break;
    case 2: 
      if ((paramAsyncResult != null) && (exception == null))
      {
        localObject = (byte[])result;
        mSpnDisplayCondition = (0xFF & localObject[0]);
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("[APLMN] mSpnDisplayCondition: ");
        paramAsyncResult.append(mSpnDisplayCondition);
        log(paramAsyncResult.toString());
        setServiceProviderName(IccUtils.adnStringFieldToString((byte[])localObject, 1, localObject.length - 1));
        paramAsyncResult = getServiceProviderName();
        if ((paramAsyncResult != null) && (paramAsyncResult.length() != 0))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Load EF_SPN: ");
          ((StringBuilder)localObject).append(paramAsyncResult);
          ((StringBuilder)localObject).append(" spnDisplayCondition: ");
          ((StringBuilder)localObject).append(mSpnDisplayCondition);
          log(((StringBuilder)localObject).toString());
          mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), paramAsyncResult);
          localObject = mTelephonyManager;
          TelephonyManager.setTelephonyProperty(mParentApp.getPhoneId(), "gsm.sim.spn", paramAsyncResult);
          mSpnState = GetSpnFsmState.IDLE;
        }
        else
        {
          mSpnState = GetSpnFsmState.READ_SPN_CPHS;
        }
      }
      else
      {
        mSpnState = GetSpnFsmState.READ_SPN_CPHS;
      }
      if (mSpnState == GetSpnFsmState.READ_SPN_CPHS)
      {
        mFh.loadEFTransparent(28436, obtainMessage(12));
        mRecordsToLoad += 1;
        mSpnDisplayCondition = -1;
      }
      break;
    case 1: 
      setServiceProviderName(null);
      mFh.loadEFTransparent(28486, obtainMessage(12));
      mRecordsToLoad += 1;
      mSpnState = GetSpnFsmState.READ_SPN_3GPP;
    }
  }
  
  private void handleEfCspData(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length / 2;
    mCspPlmnEnabled = true;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfByte[(2 * j)] == -64)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("[CSP] found ValueAddedServicesGroup, value ");
        localStringBuilder.append(paramArrayOfByte[(2 * j + 1)]);
        log(localStringBuilder.toString());
        if ((paramArrayOfByte[(2 * j + 1)] & 0x80) == 128)
        {
          mCspPlmnEnabled = true;
        }
        else
        {
          mCspPlmnEnabled = false;
          log("[CSP] Set Automatic Network Selection");
          mNetworkSelectionModeAutomaticRegistrants.notifyRegistrants();
        }
        return;
      }
    }
    log("[CSP] Value Added Service Group (0xC0), not found!");
  }
  
  private void handleSms(byte[] paramArrayOfByte)
  {
    Object localObject;
    if (paramArrayOfByte[0] != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("status : ");
      ((StringBuilder)localObject).append(paramArrayOfByte[0]);
      Rlog.d("ENF", ((StringBuilder)localObject).toString());
    }
    if (paramArrayOfByte[0] == 3)
    {
      int i = paramArrayOfByte.length;
      localObject = new byte[i - 1];
      System.arraycopy(paramArrayOfByte, 1, (byte[])localObject, 0, i - 1);
      dispatchGsmMessage(SmsMessage.createFromPdu((byte[])localObject, "3gpp"));
    }
  }
  
  private void handleSmses(ArrayList<byte[]> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      byte[] arrayOfByte = (byte[])paramArrayList.get(j);
      Object localObject;
      if (arrayOfByte[0] != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("status ");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(arrayOfByte[0]);
        Rlog.i("ENF", ((StringBuilder)localObject).toString());
      }
      if (arrayOfByte[0] == 3)
      {
        int k = arrayOfByte.length;
        localObject = new byte[k - 1];
        System.arraycopy(arrayOfByte, 1, (byte[])localObject, 0, k - 1);
        dispatchGsmMessage(SmsMessage.createFromPdu((byte[])localObject, "3gpp"));
        arrayOfByte[0] = ((byte)1);
      }
    }
  }
  
  private boolean isCphsMailboxEnabled()
  {
    byte[] arrayOfByte = mCphsInfo;
    boolean bool = false;
    if (arrayOfByte == null) {
      return false;
    }
    if ((mCphsInfo[1] & 0x30) == 48) {
      bool = true;
    }
    return bool;
  }
  
  private boolean isOnMatchingPlmn(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.equals(getOperatorNumeric())) {
      return true;
    }
    if (mSpdiNetworks != null)
    {
      Iterator localIterator = mSpdiNetworks.iterator();
      while (localIterator.hasNext()) {
        if (paramString.equals((String)localIterator.next())) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void loadCallForwardingRecords()
  {
    mRecordsRequested = true;
    mFh.loadEFLinearFixed(28619, 1, obtainMessage(32));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28435, obtainMessage(24));
    mRecordsToLoad += 1;
  }
  
  private void loadEfLiAndEfPl()
  {
    if (mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM)
    {
      mFh.loadEFTransparent(28421, obtainMessage(100, new EfUsimLiLoaded(null)));
      mRecordsToLoad += 1;
      mFh.loadEFTransparent(12037, obtainMessage(100, new EfPlLoaded(null)));
      mRecordsToLoad += 1;
    }
  }
  
  private void onLocked(int paramInt)
  {
    log("only fetch EF_LI, EF_PL and EF_ICCID in locked state");
    if (paramInt == 258) {
      paramInt = 1;
    } else {
      paramInt = 2;
    }
    mLockedRecordsReqReason = paramInt;
    loadEfLiAndEfPl();
    mFh.loadEFTransparent(12258, obtainMessage(4));
    mRecordsToLoad += 1;
  }
  
  private void onLockedAllRecordsLoaded()
  {
    setSimLanguageFromEF();
    if (mLockedRecordsReqReason == 1)
    {
      mLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    else if (mLockedRecordsReqReason == 2)
    {
      mNetworkLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onLockedAllRecordsLoaded: unexpected mLockedRecordsReqReason ");
      localStringBuilder.append(mLockedRecordsReqReason);
      loge(localStringBuilder.toString());
    }
  }
  
  private String[] parseBcdPlmnList(byte[] paramArrayOfByte, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Received ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" PLMNs, raw=");
    localStringBuilder.append(IccUtils.bytesToHexString(paramArrayOfByte));
    log(localStringBuilder.toString());
    if ((paramArrayOfByte.length != 0) && (paramArrayOfByte.length % 3 == 0))
    {
      int i = paramArrayOfByte.length / 3;
      int j = 0;
      paramString = new String[i];
      int k = 0;
      while (k < i)
      {
        paramString[j] = IccUtils.bcdPlmnToString(paramArrayOfByte, k * 3);
        int m = j;
        if (!TextUtils.isEmpty(paramString[j])) {
          m = j + 1;
        }
        k++;
        j = m;
      }
      return (String[])Arrays.copyOf(paramString, j);
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Received invalid ");
    paramArrayOfByte.append(paramString);
    paramArrayOfByte.append(" PLMN list");
    loge(paramArrayOfByte.toString());
    return null;
  }
  
  private void parseEfSpdi(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    int j = 0;
    Object localObject = new SimTlv(paramArrayOfByte, 0, i);
    String str = null;
    for (;;)
    {
      paramArrayOfByte = str;
      if (!((SimTlv)localObject).isValidObject()) {
        break;
      }
      paramArrayOfByte = (byte[])localObject;
      if (((SimTlv)localObject).getTag() == 163) {
        paramArrayOfByte = new SimTlv(((SimTlv)localObject).getData(), 0, ((SimTlv)localObject).getData().length);
      }
      if (paramArrayOfByte.getTag() == 128)
      {
        paramArrayOfByte = paramArrayOfByte.getData();
        break;
      }
      paramArrayOfByte.nextObject();
      localObject = paramArrayOfByte;
    }
    if (paramArrayOfByte == null) {
      return;
    }
    mSpdiNetworks = new ArrayList(paramArrayOfByte.length / 3);
    while (j + 2 < paramArrayOfByte.length)
    {
      str = IccUtils.bcdPlmnToString(paramArrayOfByte, j);
      if ((str != null) && (str.length() >= 5))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("EF_SPDI network: ");
        ((StringBuilder)localObject).append(str);
        log(((StringBuilder)localObject).toString());
        mSpdiNetworks.add(str);
      }
      j += 3;
    }
  }
  
  private void setSimLanguageFromEF()
  {
    if (Resources.getSystem().getBoolean(17957072)) {
      setSimLanguage(mEfLi, mEfPl);
    } else {
      log("Not using EF LI/EF PL");
    }
  }
  
  private void setVoiceCallForwardingFlagFromSimRecords()
  {
    boolean bool = validEfCfis(mEfCfis);
    int i = 1;
    StringBuilder localStringBuilder;
    if (bool)
    {
      mCallForwardingStatus = (mEfCfis[1] & 0x1);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("EF_CFIS: callForwardingEnabled=");
      localStringBuilder.append(mCallForwardingStatus);
      log(localStringBuilder.toString());
    }
    else if (mEfCff != null)
    {
      if ((mEfCff[0] & 0xF) != 10) {
        i = 0;
      }
      mCallForwardingStatus = i;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("EF_CFF: callForwardingEnabled=");
      localStringBuilder.append(mCallForwardingStatus);
      log(localStringBuilder.toString());
    }
    else
    {
      mCallForwardingStatus = -1;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("EF_CFIS and EF_CFF not valid. callForwardingEnabled=");
      localStringBuilder.append(mCallForwardingStatus);
      log(localStringBuilder.toString());
    }
  }
  
  private void setVoiceMailByCountry(String paramString)
  {
    if (mVmConfig.containsCarrier(paramString))
    {
      mIsVoiceMailFixed = true;
      mVoiceMailNum = mVmConfig.getVoiceMailNumber(paramString);
      mVoiceMailTag = mVmConfig.getVoiceMailTag(paramString);
    }
  }
  
  private boolean useRoamingFromServiceState()
  {
    Object localObject = (CarrierConfigManager)mContext.getSystemService("carrier_config");
    if (localObject != null)
    {
      localObject = ((CarrierConfigManager)localObject).getConfigForSubId(SubscriptionController.getInstance().getSubIdUsingPhoneId(mParentApp.getPhoneId()));
      if ((localObject != null) && (((PersistableBundle)localObject).getBoolean("spn_display_rule_use_roaming_from_service_state_bool"))) {
        return true;
      }
    }
    return false;
  }
  
  private boolean validEfCfis(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      if ((paramArrayOfByte[0] < 1) || (paramArrayOfByte[0] > 4))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("MSP byte: ");
        localStringBuilder.append(paramArrayOfByte[0]);
        localStringBuilder.append(" is not between 1 and 4");
        logw(localStringBuilder.toString(), null);
      }
      int i = paramArrayOfByte.length;
      for (int j = 0; j < i; j++) {
        if (paramArrayOfByte[j] != -1) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void dispose()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Disposing SIMRecords this=");
    localStringBuilder.append(this);
    log(localStringBuilder.toString());
    mCi.unSetOnSmsOnSim(this);
    mParentApp.unregisterForReady(this);
    mParentApp.unregisterForLocked(this);
    mParentApp.unregisterForNetworkLocked(this);
    resetRecords();
    super.dispose();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SIMRecords: ");
    localStringBuilder.append(this);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.println(" extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mVmConfig=");
    paramFileDescriptor.append(mVmConfig);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCallForwardingStatus=");
    paramFileDescriptor.append(mCallForwardingStatus);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSpnState=");
    paramFileDescriptor.append(mSpnState);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCphsInfo=");
    paramFileDescriptor.append(mCphsInfo);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCspPlmnEnabled=");
    paramFileDescriptor.append(mCspPlmnEnabled);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEfMWIS[]=");
    paramFileDescriptor.append(Arrays.toString(mEfMWIS));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEfCPHS_MWI[]=");
    paramFileDescriptor.append(Arrays.toString(mEfCPHS_MWI));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEfCff[]=");
    paramFileDescriptor.append(Arrays.toString(mEfCff));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEfCfis[]=");
    paramFileDescriptor.append(Arrays.toString(mEfCfis));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSpnDisplayCondition=");
    paramFileDescriptor.append(mSpnDisplayCondition);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSpdiNetworks[]=");
    paramFileDescriptor.append(mSpdiNetworks);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mUsimServiceTable=");
    paramFileDescriptor.append(mUsimServiceTable);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mGid1=");
    paramFileDescriptor.append(mGid1);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (mCarrierTestOverride.isInTestMode())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mFakeGid1=");
      paramFileDescriptor.append(mCarrierTestOverride.getFakeGid1());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mGid2=");
    paramFileDescriptor.append(mGid2);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (mCarrierTestOverride.isInTestMode())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mFakeGid2=");
      paramFileDescriptor.append(mCarrierTestOverride.getFakeGid2());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPnnHomeName=");
    paramFileDescriptor.append(mPnnHomeName);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (mCarrierTestOverride.isInTestMode())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mFakePnnHomeName=");
      paramFileDescriptor.append(mCarrierTestOverride.getFakePnnHomeName());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPlmnActRecords[]=");
    paramFileDescriptor.append(Arrays.toString(mPlmnActRecords));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mOplmnActRecords[]=");
    paramFileDescriptor.append(Arrays.toString(mOplmnActRecords));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mHplmnActRecords[]=");
    paramFileDescriptor.append(Arrays.toString(mHplmnActRecords));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mFplmns[]=");
    paramFileDescriptor.append(Arrays.toString(mFplmns));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEhplmns[]=");
    paramFileDescriptor.append(Arrays.toString(mEhplmns));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.flush();
  }
  
  protected void fetchSimRecords()
  {
    mRecordsRequested = true;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("fetchSimRecords ");
    localStringBuilder.append(mRecordsToLoad);
    log(localStringBuilder.toString());
    mCi.getIMSIForApp(mParentApp.getAid(), obtainMessage(3));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(12258, obtainMessage(4));
    mRecordsToLoad += 1;
    new AdnRecordLoader(mFh).loadFromEF(28480, getExtFromEf(28480), 1, obtainMessage(10));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixed(28617, 1, obtainMessage(5));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28589, obtainMessage(9));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixed(28618, 1, obtainMessage(7));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28433, obtainMessage(8));
    mRecordsToLoad += 1;
    loadCallForwardingRecords();
    getSpnFsm(true, null);
    mFh.loadEFTransparent(28621, obtainMessage(13));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixed(28613, 1, obtainMessage(15));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28472, obtainMessage(17));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28438, obtainMessage(26));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28437, obtainMessage(33));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28478, obtainMessage(34));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28479, obtainMessage(36));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28512, obtainMessage(37));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28513, obtainMessage(38));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28514, obtainMessage(39));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28633, obtainMessage(40));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28539, obtainMessage(41, 1238272, -1));
    mRecordsToLoad += 1;
    loadEfLiAndEfPl();
    mFh.getEFLinearRecordSize(28476, obtainMessage(28));
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("fetchSimRecords ");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" requested: ");
    localStringBuilder.append(mRecordsRequested);
    log(localStringBuilder.toString());
  }
  
  protected void finalize()
  {
    log("finalized");
  }
  
  public int getDisplayRule(ServiceState paramServiceState)
  {
    String str = paramServiceState.getOperatorNumeric();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[APLMN] param plmn: ");
    localStringBuilder.append(str);
    localStringBuilder.append(", spn: ");
    localStringBuilder.append(getServiceProviderName());
    localStringBuilder.append(", mSpnDisplayCondition: ");
    localStringBuilder.append(mSpnDisplayCondition);
    localStringBuilder.append(", isOnMatchingPlmn: ");
    localStringBuilder.append(isOnMatchingPlmn(str));
    log(localStringBuilder.toString());
    int i;
    if ((mParentApp != null) && (mParentApp.getUiccProfile() != null) && (mParentApp.getUiccProfile().getOperatorBrandOverride() != null)) {
      i = 2;
    } else if ((!TextUtils.isEmpty(getServiceProviderName())) && (mSpnDisplayCondition != -1) && (!TextUtils.isEmpty(str)))
    {
      if (useRoamingFromServiceState() ? !paramServiceState.getRoaming() : isOnMatchingPlmn(paramServiceState.getOperatorNumeric()))
      {
        i = 1;
        if ((mSpnDisplayCondition & 0x1) == 1) {
          i = 0x1 | 0x2;
        }
      }
      else
      {
        i = 2;
        if ((mSpnDisplayCondition & 0x2) == 0) {
          i = 0x2 | 0x1;
        }
      }
    }
    else {
      i = 2;
    }
    return i;
  }
  
  public void getForbiddenPlmns(Message paramMessage)
  {
    int i = storePendingResponseMessage(paramMessage);
    mFh.loadEFTransparent(28539, obtainMessage(41, 1238273, i));
  }
  
  public String getMsisdnAlphaTag()
  {
    return mMsisdnTag;
  }
  
  public String getMsisdnNumber()
  {
    return mMsisdn;
  }
  
  public String getOperatorNumeric()
  {
    String str = getIMSI();
    if (str == null)
    {
      log("getOperatorNumeric: IMSI == null");
      return null;
    }
    if ((mMncLength != -1) && (mMncLength != 0))
    {
      if (str.length() >= mMncLength + 3) {
        return str.substring(0, 3 + mMncLength);
      }
      return null;
    }
    log("getSIMOperatorNumeric: bad mncLength");
    return null;
  }
  
  public UsimServiceTable getUsimServiceTable()
  {
    return mUsimServiceTable;
  }
  
  public int getVoiceCallForwardingFlag()
  {
    return mCallForwardingStatus;
  }
  
  public String getVoiceMailAlphaTag()
  {
    return mVoiceMailTag;
  }
  
  public String getVoiceMailNumber()
  {
    return mVoiceMailNum;
  }
  
  public int getVoiceMessageCount()
  {
    int i = -2;
    Object localObject = mEfMWIS;
    int j = 0;
    int k;
    if (localObject != null)
    {
      i = j;
      if ((mEfMWIS[0] & 0x1) != 0) {
        i = 1;
      }
      k = mEfMWIS[1] & 0xFF;
      j = k;
      if (i != 0) {
        if (k != 0)
        {
          j = k;
          if (k != 255) {}
        }
        else
        {
          j = -1;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" VoiceMessageCount from SIM MWIS = ");
      ((StringBuilder)localObject).append(j);
      log(((StringBuilder)localObject).toString());
    }
    else
    {
      j = i;
      if (mEfCPHS_MWI != null)
      {
        k = mEfCPHS_MWI[0] & 0xF;
        if (k == 10)
        {
          j = -1;
        }
        else
        {
          j = i;
          if (k == 5) {
            j = 0;
          }
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(" VoiceMessageCount from SIM CPHS = ");
        ((StringBuilder)localObject).append(j);
        log(((StringBuilder)localObject).toString());
      }
    }
    return j;
  }
  
  protected void handleFileUpdate(int paramInt)
  {
    if (paramInt != 28435) {
      if (paramInt != 28437)
      {
        if (paramInt != 28439)
        {
          if (paramInt != 28475)
          {
            if (paramInt != 28480)
            {
              if (paramInt != 28615)
              {
                if (paramInt != 28619)
                {
                  mAdnCache.reset();
                  fetchSimRecords();
                  return;
                }
              }
              else
              {
                mRecordsToLoad += 1;
                new AdnRecordLoader(mFh).loadFromEF(28615, 28616, mMailboxIndex, obtainMessage(6));
                return;
              }
            }
            else
            {
              mRecordsToLoad += 1;
              log("SIM Refresh called for EF_MSISDN");
              new AdnRecordLoader(mFh).loadFromEF(28480, getExtFromEf(28480), 1, obtainMessage(10));
              return;
            }
          }
          else
          {
            log("SIM Refresh called for EF_FDN");
            mParentApp.queryFdn();
            mAdnCache.reset();
            return;
          }
        }
        else
        {
          mRecordsToLoad += 1;
          new AdnRecordLoader(mFh).loadFromEF(28439, 28490, 1, obtainMessage(11));
          return;
        }
      }
      else
      {
        mRecordsToLoad += 1;
        log("[CSP] SIM Refresh for EF_CSP_CPHS");
        mFh.loadEFTransparent(28437, obtainMessage(33));
        return;
      }
    }
    log("SIM Refresh called for EF_CFIS or EF_CFF_CPHS");
    loadCallForwardingRecords();
  }
  
  /* Error */
  public void handleMessage(Message paramMessage)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: iconst_0
    //   5: istore 4
    //   7: aload_0
    //   8: getfield 1208	com/android/internal/telephony/uicc/SIMRecords:mDestroyed	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   11: invokevirtual 1212	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   14: ifeq +66 -> 80
    //   17: new 529	java/lang/StringBuilder
    //   20: dup
    //   21: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   24: astore 5
    //   26: aload 5
    //   28: ldc_w 1214
    //   31: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: aload 5
    //   37: aload_1
    //   38: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload 5
    //   44: ldc_w 1216
    //   47: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload 5
    //   53: aload_1
    //   54: getfield 1221	android/os/Message:what	I
    //   57: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload 5
    //   63: ldc_w 1223
    //   66: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_0
    //   71: aload 5
    //   73: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   76: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   79: return
    //   80: iload_2
    //   81: istore 6
    //   83: iload_3
    //   84: istore 7
    //   86: aload_1
    //   87: getfield 1221	android/os/Message:what	I
    //   90: istore 8
    //   92: iload 8
    //   94: iconst_1
    //   95: if_icmpeq +11673 -> 11768
    //   98: iload 8
    //   100: bipush 30
    //   102: if_icmpeq +11528 -> 11630
    //   105: iload 8
    //   107: tableswitch	default:+65->172, 3:+10484->10591, 4:+10314->10421, 5:+9986->10093, 6:+9485->9592, 7:+9235->9342, 8:+9016->9123, 9:+4609->4716, 10:+4434->4541, 11:+9485->9592, 12:+4410->4517, 13:+4337->4444, 14:+4282->4389, 15:+4054->4161
    //   172: iload 8
    //   174: tableswitch	default:+38->212, 17:+3820->3994, 18:+3759->3933, 19:+3681->3855, 20:+3165->3339, 21:+2896->3070, 22:+2763->2937
    //   212: iload 8
    //   214: tableswitch	default:+26->240, 24:+2565->2779, 25:+2354->2568, 26:+2222->2436
    //   240: iload 8
    //   242: tableswitch	default:+26->268, 32:+2036->2278, 33:+1823->2065, 34:+1559->1801
    //   268: iload 8
    //   270: tableswitch	default:+38->308, 36:+1267->1537, 37:+1042->1312, 38:+817->1087, 39:+521->791, 40:+361->631, 41:+101->371
    //   308: iload 8
    //   310: tableswitch	default:+22->332, 258:+40->350, 259:+40->350
    //   332: iload_2
    //   333: istore 6
    //   335: iload_3
    //   336: istore 7
    //   338: aload_0
    //   339: aload_1
    //   340: invokespecial 1225	com/android/internal/telephony/uicc/IccRecords:handleMessage	(Landroid/os/Message;)V
    //   343: iload 4
    //   345: istore 8
    //   347: goto +11435 -> 11782
    //   350: iload_2
    //   351: istore 6
    //   353: iload_3
    //   354: istore 7
    //   356: aload_0
    //   357: aload_1
    //   358: getfield 1221	android/os/Message:what	I
    //   361: invokespecial 1227	com/android/internal/telephony/uicc/SIMRecords:onLocked	(I)V
    //   364: iload 4
    //   366: istore 8
    //   368: goto +11414 -> 11782
    //   371: iconst_1
    //   372: istore 4
    //   374: iconst_1
    //   375: istore_2
    //   376: iconst_1
    //   377: istore 8
    //   379: iload 4
    //   381: istore 6
    //   383: iload_2
    //   384: istore 7
    //   386: aload_1
    //   387: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   390: checkcast 600	android/os/AsyncResult
    //   393: astore 5
    //   395: iload 4
    //   397: istore 6
    //   399: iload_2
    //   400: istore 7
    //   402: aload 5
    //   404: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   407: checkcast 609	[B
    //   410: astore 9
    //   412: iload 4
    //   414: istore 6
    //   416: iload_2
    //   417: istore 7
    //   419: aload 5
    //   421: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   424: ifnonnull +135 -> 559
    //   427: aload 9
    //   429: ifnonnull +6 -> 435
    //   432: goto +127 -> 559
    //   435: iload 4
    //   437: istore 6
    //   439: iload_2
    //   440: istore 7
    //   442: aload_0
    //   443: aload_0
    //   444: aload 9
    //   446: ldc_w 1232
    //   449: invokespecial 1234	com/android/internal/telephony/uicc/SIMRecords:parseBcdPlmnList	([BLjava/lang/String;)[Ljava/lang/String;
    //   452: putfield 1069	com/android/internal/telephony/uicc/SIMRecords:mFplmns	[Ljava/lang/String;
    //   455: iload 4
    //   457: istore 6
    //   459: iload_2
    //   460: istore 7
    //   462: aload_1
    //   463: getfield 1237	android/os/Message:arg1	I
    //   466: ldc_w 1148
    //   469: if_icmpne +11313 -> 11782
    //   472: iconst_0
    //   473: istore 4
    //   475: iconst_0
    //   476: istore_2
    //   477: iconst_0
    //   478: istore 8
    //   480: iload 4
    //   482: istore 6
    //   484: iload_2
    //   485: istore 7
    //   487: aload_0
    //   488: aload_1
    //   489: getfield 1240	android/os/Message:arg2	I
    //   492: invokestatic 1246	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   495: invokevirtual 1250	com/android/internal/telephony/uicc/SIMRecords:retrievePendingResponseMessage	(Ljava/lang/Integer;)Landroid/os/Message;
    //   498: astore_1
    //   499: aload_1
    //   500: ifnull +42 -> 542
    //   503: iload 4
    //   505: istore 6
    //   507: iload_2
    //   508: istore 7
    //   510: aload_1
    //   511: aload_0
    //   512: getfield 1069	com/android/internal/telephony/uicc/SIMRecords:mFplmns	[Ljava/lang/String;
    //   515: aload_0
    //   516: getfield 1069	com/android/internal/telephony/uicc/SIMRecords:mFplmns	[Ljava/lang/String;
    //   519: arraylength
    //   520: invokestatic 821	java/util/Arrays:copyOf	([Ljava/lang/Object;I)[Ljava/lang/Object;
    //   523: aconst_null
    //   524: invokestatic 1254	android/os/AsyncResult:forMessage	(Landroid/os/Message;Ljava/lang/Object;Ljava/lang/Throwable;)Landroid/os/AsyncResult;
    //   527: pop
    //   528: iload 4
    //   530: istore 6
    //   532: iload_2
    //   533: istore 7
    //   535: aload_1
    //   536: invokevirtual 1257	android/os/Message:sendToTarget	()V
    //   539: goto +11243 -> 11782
    //   542: iload 4
    //   544: istore 6
    //   546: iload_2
    //   547: istore 7
    //   549: aload_0
    //   550: ldc_w 1259
    //   553: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   556: goto +11226 -> 11782
    //   559: iload 4
    //   561: istore 6
    //   563: iload_2
    //   564: istore 7
    //   566: new 529	java/lang/StringBuilder
    //   569: astore_1
    //   570: iload 4
    //   572: istore 6
    //   574: iload_2
    //   575: istore 7
    //   577: aload_1
    //   578: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   581: iload 4
    //   583: istore 6
    //   585: iload_2
    //   586: istore 7
    //   588: aload_1
    //   589: ldc_w 1261
    //   592: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   595: pop
    //   596: iload 4
    //   598: istore 6
    //   600: iload_2
    //   601: istore 7
    //   603: aload_1
    //   604: aload 5
    //   606: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   609: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   612: pop
    //   613: iload 4
    //   615: istore 6
    //   617: iload_2
    //   618: istore 7
    //   620: aload_0
    //   621: aload_1
    //   622: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   625: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   628: goto +11154 -> 11782
    //   631: iconst_1
    //   632: istore 4
    //   634: iconst_1
    //   635: istore_2
    //   636: iconst_1
    //   637: istore 8
    //   639: iload 4
    //   641: istore 6
    //   643: iload_2
    //   644: istore 7
    //   646: aload_1
    //   647: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   650: checkcast 600	android/os/AsyncResult
    //   653: astore_1
    //   654: iload 4
    //   656: istore 6
    //   658: iload_2
    //   659: istore 7
    //   661: aload_1
    //   662: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   665: checkcast 609	[B
    //   668: astore 5
    //   670: iload 4
    //   672: istore 6
    //   674: iload_2
    //   675: istore 7
    //   677: aload_1
    //   678: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   681: ifnonnull +34 -> 715
    //   684: aload 5
    //   686: ifnonnull +6 -> 692
    //   689: goto +26 -> 715
    //   692: iload 4
    //   694: istore 6
    //   696: iload_2
    //   697: istore 7
    //   699: aload_0
    //   700: aload_0
    //   701: aload 5
    //   703: ldc_w 1263
    //   706: invokespecial 1234	com/android/internal/telephony/uicc/SIMRecords:parseBcdPlmnList	([BLjava/lang/String;)[Ljava/lang/String;
    //   709: putfield 1074	com/android/internal/telephony/uicc/SIMRecords:mEhplmns	[Ljava/lang/String;
    //   712: goto +11070 -> 11782
    //   715: iload 4
    //   717: istore 6
    //   719: iload_2
    //   720: istore 7
    //   722: new 529	java/lang/StringBuilder
    //   725: astore 5
    //   727: iload 4
    //   729: istore 6
    //   731: iload_2
    //   732: istore 7
    //   734: aload 5
    //   736: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   739: iload 4
    //   741: istore 6
    //   743: iload_2
    //   744: istore 7
    //   746: aload 5
    //   748: ldc_w 1265
    //   751: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   754: pop
    //   755: iload 4
    //   757: istore 6
    //   759: iload_2
    //   760: istore 7
    //   762: aload 5
    //   764: aload_1
    //   765: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   768: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   771: pop
    //   772: iload 4
    //   774: istore 6
    //   776: iload_2
    //   777: istore 7
    //   779: aload_0
    //   780: aload 5
    //   782: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   785: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   788: goto +10994 -> 11782
    //   791: iconst_1
    //   792: istore 4
    //   794: iconst_1
    //   795: istore_2
    //   796: iconst_1
    //   797: istore 8
    //   799: iload 4
    //   801: istore 6
    //   803: iload_2
    //   804: istore 7
    //   806: aload_1
    //   807: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   810: checkcast 600	android/os/AsyncResult
    //   813: astore 5
    //   815: iload 4
    //   817: istore 6
    //   819: iload_2
    //   820: istore 7
    //   822: aload 5
    //   824: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   827: checkcast 609	[B
    //   830: astore_1
    //   831: iload 4
    //   833: istore 6
    //   835: iload_2
    //   836: istore 7
    //   838: aload 5
    //   840: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   843: ifnonnull +172 -> 1015
    //   846: aload_1
    //   847: ifnonnull +6 -> 853
    //   850: goto +165 -> 1015
    //   853: iload 4
    //   855: istore 6
    //   857: iload_2
    //   858: istore 7
    //   860: new 529	java/lang/StringBuilder
    //   863: astore 5
    //   865: iload 4
    //   867: istore 6
    //   869: iload_2
    //   870: istore 7
    //   872: aload 5
    //   874: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   877: iload 4
    //   879: istore 6
    //   881: iload_2
    //   882: istore 7
    //   884: aload 5
    //   886: ldc_w 1267
    //   889: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   892: pop
    //   893: iload 4
    //   895: istore 6
    //   897: iload_2
    //   898: istore 7
    //   900: aload 5
    //   902: aload_1
    //   903: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   906: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   909: pop
    //   910: iload 4
    //   912: istore 6
    //   914: iload_2
    //   915: istore 7
    //   917: aload_0
    //   918: aload 5
    //   920: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   923: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   926: iload 4
    //   928: istore 6
    //   930: iload_2
    //   931: istore 7
    //   933: aload_0
    //   934: aload_1
    //   935: invokestatic 1273	com/android/internal/telephony/uicc/PlmnActRecord:getRecords	([B)[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   938: putfield 1064	com/android/internal/telephony/uicc/SIMRecords:mHplmnActRecords	[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   941: iload 4
    //   943: istore 6
    //   945: iload_2
    //   946: istore 7
    //   948: new 529	java/lang/StringBuilder
    //   951: astore_1
    //   952: iload 4
    //   954: istore 6
    //   956: iload_2
    //   957: istore 7
    //   959: aload_1
    //   960: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   963: iload 4
    //   965: istore 6
    //   967: iload_2
    //   968: istore 7
    //   970: aload_1
    //   971: ldc_w 1275
    //   974: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   977: pop
    //   978: iload 4
    //   980: istore 6
    //   982: iload_2
    //   983: istore 7
    //   985: aload_1
    //   986: aload_0
    //   987: getfield 1064	com/android/internal/telephony/uicc/SIMRecords:mHplmnActRecords	[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   990: invokestatic 1054	java/util/Arrays:toString	([Ljava/lang/Object;)Ljava/lang/String;
    //   993: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   996: pop
    //   997: iload 4
    //   999: istore 6
    //   1001: iload_2
    //   1002: istore 7
    //   1004: aload_0
    //   1005: aload_1
    //   1006: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1009: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   1012: goto +10770 -> 11782
    //   1015: iload 4
    //   1017: istore 6
    //   1019: iload_2
    //   1020: istore 7
    //   1022: new 529	java/lang/StringBuilder
    //   1025: astore_1
    //   1026: iload 4
    //   1028: istore 6
    //   1030: iload_2
    //   1031: istore 7
    //   1033: aload_1
    //   1034: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1037: iload 4
    //   1039: istore 6
    //   1041: iload_2
    //   1042: istore 7
    //   1044: aload_1
    //   1045: ldc_w 1277
    //   1048: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1051: pop
    //   1052: iload 4
    //   1054: istore 6
    //   1056: iload_2
    //   1057: istore 7
    //   1059: aload_1
    //   1060: aload 5
    //   1062: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1065: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1068: pop
    //   1069: iload 4
    //   1071: istore 6
    //   1073: iload_2
    //   1074: istore 7
    //   1076: aload_0
    //   1077: aload_1
    //   1078: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1081: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   1084: goto +10698 -> 11782
    //   1087: iconst_1
    //   1088: istore 4
    //   1090: iconst_1
    //   1091: istore_2
    //   1092: iconst_1
    //   1093: istore 8
    //   1095: iload 4
    //   1097: istore 6
    //   1099: iload_2
    //   1100: istore 7
    //   1102: aload_1
    //   1103: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   1106: checkcast 600	android/os/AsyncResult
    //   1109: astore_1
    //   1110: iload 4
    //   1112: istore 6
    //   1114: iload_2
    //   1115: istore 7
    //   1117: aload_1
    //   1118: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   1121: checkcast 609	[B
    //   1124: astore 5
    //   1126: iload 4
    //   1128: istore 6
    //   1130: iload_2
    //   1131: istore 7
    //   1133: aload_1
    //   1134: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1137: ifnonnull +99 -> 1236
    //   1140: aload 5
    //   1142: ifnonnull +6 -> 1148
    //   1145: goto +91 -> 1236
    //   1148: iload 4
    //   1150: istore 6
    //   1152: iload_2
    //   1153: istore 7
    //   1155: new 529	java/lang/StringBuilder
    //   1158: astore_1
    //   1159: iload 4
    //   1161: istore 6
    //   1163: iload_2
    //   1164: istore 7
    //   1166: aload_1
    //   1167: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1170: iload 4
    //   1172: istore 6
    //   1174: iload_2
    //   1175: istore 7
    //   1177: aload_1
    //   1178: ldc_w 1267
    //   1181: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1184: pop
    //   1185: iload 4
    //   1187: istore 6
    //   1189: iload_2
    //   1190: istore 7
    //   1192: aload_1
    //   1193: aload 5
    //   1195: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   1198: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1201: pop
    //   1202: iload 4
    //   1204: istore 6
    //   1206: iload_2
    //   1207: istore 7
    //   1209: aload_0
    //   1210: aload_1
    //   1211: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1214: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   1217: iload 4
    //   1219: istore 6
    //   1221: iload_2
    //   1222: istore 7
    //   1224: aload_0
    //   1225: aload 5
    //   1227: invokestatic 1273	com/android/internal/telephony/uicc/PlmnActRecord:getRecords	([B)[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   1230: putfield 1059	com/android/internal/telephony/uicc/SIMRecords:mOplmnActRecords	[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   1233: goto +10549 -> 11782
    //   1236: iload 4
    //   1238: istore 6
    //   1240: iload_2
    //   1241: istore 7
    //   1243: new 529	java/lang/StringBuilder
    //   1246: astore 5
    //   1248: iload 4
    //   1250: istore 6
    //   1252: iload_2
    //   1253: istore 7
    //   1255: aload 5
    //   1257: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1260: iload 4
    //   1262: istore 6
    //   1264: iload_2
    //   1265: istore 7
    //   1267: aload 5
    //   1269: ldc_w 1279
    //   1272: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1275: pop
    //   1276: iload 4
    //   1278: istore 6
    //   1280: iload_2
    //   1281: istore 7
    //   1283: aload 5
    //   1285: aload_1
    //   1286: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1289: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1292: pop
    //   1293: iload 4
    //   1295: istore 6
    //   1297: iload_2
    //   1298: istore 7
    //   1300: aload_0
    //   1301: aload 5
    //   1303: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1306: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   1309: goto +10473 -> 11782
    //   1312: iconst_1
    //   1313: istore 4
    //   1315: iconst_1
    //   1316: istore_2
    //   1317: iconst_1
    //   1318: istore 8
    //   1320: iload 4
    //   1322: istore 6
    //   1324: iload_2
    //   1325: istore 7
    //   1327: aload_1
    //   1328: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   1331: checkcast 600	android/os/AsyncResult
    //   1334: astore_1
    //   1335: iload 4
    //   1337: istore 6
    //   1339: iload_2
    //   1340: istore 7
    //   1342: aload_1
    //   1343: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   1346: checkcast 609	[B
    //   1349: astore 5
    //   1351: iload 4
    //   1353: istore 6
    //   1355: iload_2
    //   1356: istore 7
    //   1358: aload_1
    //   1359: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1362: ifnonnull +99 -> 1461
    //   1365: aload 5
    //   1367: ifnonnull +6 -> 1373
    //   1370: goto +91 -> 1461
    //   1373: iload 4
    //   1375: istore 6
    //   1377: iload_2
    //   1378: istore 7
    //   1380: new 529	java/lang/StringBuilder
    //   1383: astore_1
    //   1384: iload 4
    //   1386: istore 6
    //   1388: iload_2
    //   1389: istore 7
    //   1391: aload_1
    //   1392: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1395: iload 4
    //   1397: istore 6
    //   1399: iload_2
    //   1400: istore 7
    //   1402: aload_1
    //   1403: ldc_w 1267
    //   1406: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1409: pop
    //   1410: iload 4
    //   1412: istore 6
    //   1414: iload_2
    //   1415: istore 7
    //   1417: aload_1
    //   1418: aload 5
    //   1420: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   1423: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1426: pop
    //   1427: iload 4
    //   1429: istore 6
    //   1431: iload_2
    //   1432: istore 7
    //   1434: aload_0
    //   1435: aload_1
    //   1436: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1439: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   1442: iload 4
    //   1444: istore 6
    //   1446: iload_2
    //   1447: istore 7
    //   1449: aload_0
    //   1450: aload 5
    //   1452: invokestatic 1273	com/android/internal/telephony/uicc/PlmnActRecord:getRecords	([B)[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   1455: putfield 1051	com/android/internal/telephony/uicc/SIMRecords:mPlmnActRecords	[Lcom/android/internal/telephony/uicc/PlmnActRecord;
    //   1458: goto +10324 -> 11782
    //   1461: iload 4
    //   1463: istore 6
    //   1465: iload_2
    //   1466: istore 7
    //   1468: new 529	java/lang/StringBuilder
    //   1471: astore 5
    //   1473: iload 4
    //   1475: istore 6
    //   1477: iload_2
    //   1478: istore 7
    //   1480: aload 5
    //   1482: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1485: iload 4
    //   1487: istore 6
    //   1489: iload_2
    //   1490: istore 7
    //   1492: aload 5
    //   1494: ldc_w 1281
    //   1497: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1500: pop
    //   1501: iload 4
    //   1503: istore 6
    //   1505: iload_2
    //   1506: istore 7
    //   1508: aload 5
    //   1510: aload_1
    //   1511: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1514: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1517: pop
    //   1518: iload 4
    //   1520: istore 6
    //   1522: iload_2
    //   1523: istore 7
    //   1525: aload_0
    //   1526: aload 5
    //   1528: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1531: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   1534: goto +10248 -> 11782
    //   1537: iconst_1
    //   1538: istore 4
    //   1540: iconst_1
    //   1541: istore_2
    //   1542: iconst_1
    //   1543: istore 8
    //   1545: iload 4
    //   1547: istore 6
    //   1549: iload_2
    //   1550: istore 7
    //   1552: aload_1
    //   1553: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   1556: checkcast 600	android/os/AsyncResult
    //   1559: astore_1
    //   1560: iload 4
    //   1562: istore 6
    //   1564: iload_2
    //   1565: istore 7
    //   1567: aload_1
    //   1568: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   1571: checkcast 609	[B
    //   1574: astore 5
    //   1576: iload 4
    //   1578: istore 6
    //   1580: iload_2
    //   1581: istore 7
    //   1583: aload_1
    //   1584: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1587: ifnull +91 -> 1678
    //   1590: iload 4
    //   1592: istore 6
    //   1594: iload_2
    //   1595: istore 7
    //   1597: new 529	java/lang/StringBuilder
    //   1600: astore 5
    //   1602: iload 4
    //   1604: istore 6
    //   1606: iload_2
    //   1607: istore 7
    //   1609: aload 5
    //   1611: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1614: iload 4
    //   1616: istore 6
    //   1618: iload_2
    //   1619: istore 7
    //   1621: aload 5
    //   1623: ldc_w 1283
    //   1626: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1629: pop
    //   1630: iload 4
    //   1632: istore 6
    //   1634: iload_2
    //   1635: istore 7
    //   1637: aload 5
    //   1639: aload_1
    //   1640: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1643: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1646: pop
    //   1647: iload 4
    //   1649: istore 6
    //   1651: iload_2
    //   1652: istore 7
    //   1654: aload_0
    //   1655: aload 5
    //   1657: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1660: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   1663: iload 4
    //   1665: istore 6
    //   1667: iload_2
    //   1668: istore 7
    //   1670: aload_0
    //   1671: aconst_null
    //   1672: putfield 1030	com/android/internal/telephony/uicc/SIMRecords:mGid2	Ljava/lang/String;
    //   1675: goto +10107 -> 11782
    //   1678: iload 4
    //   1680: istore 6
    //   1682: iload_2
    //   1683: istore 7
    //   1685: aload_0
    //   1686: aload 5
    //   1688: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   1691: putfield 1030	com/android/internal/telephony/uicc/SIMRecords:mGid2	Ljava/lang/String;
    //   1694: iload 4
    //   1696: istore 6
    //   1698: iload_2
    //   1699: istore 7
    //   1701: aload_0
    //   1702: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   1705: astore_1
    //   1706: iload 4
    //   1708: istore 6
    //   1710: iload_2
    //   1711: istore 7
    //   1713: aload_0
    //   1714: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   1717: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   1720: ldc_w 1285
    //   1723: aload_0
    //   1724: getfield 1011	com/android/internal/telephony/uicc/SIMRecords:mGid1	Ljava/lang/String;
    //   1727: invokestatic 670	android/telephony/TelephonyManager:setTelephonyProperty	(ILjava/lang/String;Ljava/lang/String;)V
    //   1730: iload 4
    //   1732: istore 6
    //   1734: iload_2
    //   1735: istore 7
    //   1737: new 529	java/lang/StringBuilder
    //   1740: astore_1
    //   1741: iload 4
    //   1743: istore 6
    //   1745: iload_2
    //   1746: istore 7
    //   1748: aload_1
    //   1749: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1752: iload 4
    //   1754: istore 6
    //   1756: iload_2
    //   1757: istore 7
    //   1759: aload_1
    //   1760: ldc_w 1287
    //   1763: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1766: pop
    //   1767: iload 4
    //   1769: istore 6
    //   1771: iload_2
    //   1772: istore 7
    //   1774: aload_1
    //   1775: aload_0
    //   1776: getfield 1030	com/android/internal/telephony/uicc/SIMRecords:mGid2	Ljava/lang/String;
    //   1779: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1782: pop
    //   1783: iload 4
    //   1785: istore 6
    //   1787: iload_2
    //   1788: istore 7
    //   1790: aload_0
    //   1791: aload_1
    //   1792: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1795: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   1798: goto +9984 -> 11782
    //   1801: iconst_1
    //   1802: istore 4
    //   1804: iconst_1
    //   1805: istore_2
    //   1806: iconst_1
    //   1807: istore 8
    //   1809: iload 4
    //   1811: istore 6
    //   1813: iload_2
    //   1814: istore 7
    //   1816: aload_1
    //   1817: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   1820: checkcast 600	android/os/AsyncResult
    //   1823: astore_1
    //   1824: iload 4
    //   1826: istore 6
    //   1828: iload_2
    //   1829: istore 7
    //   1831: aload_1
    //   1832: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   1835: checkcast 609	[B
    //   1838: astore 5
    //   1840: iload 4
    //   1842: istore 6
    //   1844: iload_2
    //   1845: istore 7
    //   1847: aload_1
    //   1848: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1851: ifnull +91 -> 1942
    //   1854: iload 4
    //   1856: istore 6
    //   1858: iload_2
    //   1859: istore 7
    //   1861: new 529	java/lang/StringBuilder
    //   1864: astore 5
    //   1866: iload 4
    //   1868: istore 6
    //   1870: iload_2
    //   1871: istore 7
    //   1873: aload 5
    //   1875: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   1878: iload 4
    //   1880: istore 6
    //   1882: iload_2
    //   1883: istore 7
    //   1885: aload 5
    //   1887: ldc_w 1289
    //   1890: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1893: pop
    //   1894: iload 4
    //   1896: istore 6
    //   1898: iload_2
    //   1899: istore 7
    //   1901: aload 5
    //   1903: aload_1
    //   1904: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   1907: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1910: pop
    //   1911: iload 4
    //   1913: istore 6
    //   1915: iload_2
    //   1916: istore 7
    //   1918: aload_0
    //   1919: aload 5
    //   1921: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1924: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   1927: iload 4
    //   1929: istore 6
    //   1931: iload_2
    //   1932: istore 7
    //   1934: aload_0
    //   1935: aconst_null
    //   1936: putfield 1011	com/android/internal/telephony/uicc/SIMRecords:mGid1	Ljava/lang/String;
    //   1939: goto +9843 -> 11782
    //   1942: iload 4
    //   1944: istore 6
    //   1946: iload_2
    //   1947: istore 7
    //   1949: aload_0
    //   1950: aload 5
    //   1952: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   1955: putfield 1011	com/android/internal/telephony/uicc/SIMRecords:mGid1	Ljava/lang/String;
    //   1958: iload 4
    //   1960: istore 6
    //   1962: iload_2
    //   1963: istore 7
    //   1965: aload_0
    //   1966: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   1969: astore_1
    //   1970: iload 4
    //   1972: istore 6
    //   1974: iload_2
    //   1975: istore 7
    //   1977: aload_0
    //   1978: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   1981: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   1984: ldc_w 1291
    //   1987: aload_0
    //   1988: getfield 1011	com/android/internal/telephony/uicc/SIMRecords:mGid1	Ljava/lang/String;
    //   1991: invokestatic 670	android/telephony/TelephonyManager:setTelephonyProperty	(ILjava/lang/String;Ljava/lang/String;)V
    //   1994: iload 4
    //   1996: istore 6
    //   1998: iload_2
    //   1999: istore 7
    //   2001: new 529	java/lang/StringBuilder
    //   2004: astore_1
    //   2005: iload 4
    //   2007: istore 6
    //   2009: iload_2
    //   2010: istore 7
    //   2012: aload_1
    //   2013: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2016: iload 4
    //   2018: istore 6
    //   2020: iload_2
    //   2021: istore 7
    //   2023: aload_1
    //   2024: ldc_w 1293
    //   2027: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2030: pop
    //   2031: iload 4
    //   2033: istore 6
    //   2035: iload_2
    //   2036: istore 7
    //   2038: aload_1
    //   2039: aload_0
    //   2040: getfield 1011	com/android/internal/telephony/uicc/SIMRecords:mGid1	Ljava/lang/String;
    //   2043: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2046: pop
    //   2047: iload 4
    //   2049: istore 6
    //   2051: iload_2
    //   2052: istore 7
    //   2054: aload_0
    //   2055: aload_1
    //   2056: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2059: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2062: goto +9720 -> 11782
    //   2065: iconst_1
    //   2066: istore 4
    //   2068: iconst_1
    //   2069: istore_2
    //   2070: iconst_1
    //   2071: istore 8
    //   2073: iload 4
    //   2075: istore 6
    //   2077: iload_2
    //   2078: istore 7
    //   2080: aload_1
    //   2081: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2084: checkcast 600	android/os/AsyncResult
    //   2087: astore 5
    //   2089: iload 4
    //   2091: istore 6
    //   2093: iload_2
    //   2094: istore 7
    //   2096: aload 5
    //   2098: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2101: ifnull +75 -> 2176
    //   2104: iload 4
    //   2106: istore 6
    //   2108: iload_2
    //   2109: istore 7
    //   2111: new 529	java/lang/StringBuilder
    //   2114: astore_1
    //   2115: iload 4
    //   2117: istore 6
    //   2119: iload_2
    //   2120: istore 7
    //   2122: aload_1
    //   2123: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2126: iload 4
    //   2128: istore 6
    //   2130: iload_2
    //   2131: istore 7
    //   2133: aload_1
    //   2134: ldc_w 1295
    //   2137: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2140: pop
    //   2141: iload 4
    //   2143: istore 6
    //   2145: iload_2
    //   2146: istore 7
    //   2148: aload_1
    //   2149: aload 5
    //   2151: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2154: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2157: pop
    //   2158: iload 4
    //   2160: istore 6
    //   2162: iload_2
    //   2163: istore 7
    //   2165: aload_0
    //   2166: aload_1
    //   2167: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2170: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   2173: goto +9609 -> 11782
    //   2176: iload 4
    //   2178: istore 6
    //   2180: iload_2
    //   2181: istore 7
    //   2183: aload 5
    //   2185: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   2188: checkcast 609	[B
    //   2191: astore 5
    //   2193: iload 4
    //   2195: istore 6
    //   2197: iload_2
    //   2198: istore 7
    //   2200: new 529	java/lang/StringBuilder
    //   2203: astore_1
    //   2204: iload 4
    //   2206: istore 6
    //   2208: iload_2
    //   2209: istore 7
    //   2211: aload_1
    //   2212: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2215: iload 4
    //   2217: istore 6
    //   2219: iload_2
    //   2220: istore 7
    //   2222: aload_1
    //   2223: ldc_w 1297
    //   2226: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2229: pop
    //   2230: iload 4
    //   2232: istore 6
    //   2234: iload_2
    //   2235: istore 7
    //   2237: aload_1
    //   2238: aload 5
    //   2240: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   2243: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2246: pop
    //   2247: iload 4
    //   2249: istore 6
    //   2251: iload_2
    //   2252: istore 7
    //   2254: aload_0
    //   2255: aload_1
    //   2256: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2259: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2262: iload 4
    //   2264: istore 6
    //   2266: iload_2
    //   2267: istore 7
    //   2269: aload_0
    //   2270: aload 5
    //   2272: invokespecial 1299	com/android/internal/telephony/uicc/SIMRecords:handleEfCspData	([B)V
    //   2275: goto +9507 -> 11782
    //   2278: iconst_1
    //   2279: istore 4
    //   2281: iconst_1
    //   2282: istore_2
    //   2283: iconst_1
    //   2284: istore 8
    //   2286: iload 4
    //   2288: istore 6
    //   2290: iload_2
    //   2291: istore 7
    //   2293: aload_1
    //   2294: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2297: checkcast 600	android/os/AsyncResult
    //   2300: astore 5
    //   2302: iload 4
    //   2304: istore 6
    //   2306: iload_2
    //   2307: istore 7
    //   2309: aload 5
    //   2311: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   2314: checkcast 609	[B
    //   2317: astore_1
    //   2318: iload 4
    //   2320: istore 6
    //   2322: iload_2
    //   2323: istore 7
    //   2325: aload 5
    //   2327: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2330: ifnull +18 -> 2348
    //   2333: iload 4
    //   2335: istore 6
    //   2337: iload_2
    //   2338: istore 7
    //   2340: aload_0
    //   2341: aconst_null
    //   2342: putfield 465	com/android/internal/telephony/uicc/SIMRecords:mEfCfis	[B
    //   2345: goto +9437 -> 11782
    //   2348: iload 4
    //   2350: istore 6
    //   2352: iload_2
    //   2353: istore 7
    //   2355: new 529	java/lang/StringBuilder
    //   2358: astore 5
    //   2360: iload 4
    //   2362: istore 6
    //   2364: iload_2
    //   2365: istore 7
    //   2367: aload 5
    //   2369: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2372: iload 4
    //   2374: istore 6
    //   2376: iload_2
    //   2377: istore 7
    //   2379: aload 5
    //   2381: ldc_w 1301
    //   2384: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2387: pop
    //   2388: iload 4
    //   2390: istore 6
    //   2392: iload_2
    //   2393: istore 7
    //   2395: aload 5
    //   2397: aload_1
    //   2398: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   2401: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2404: pop
    //   2405: iload 4
    //   2407: istore 6
    //   2409: iload_2
    //   2410: istore 7
    //   2412: aload_0
    //   2413: aload 5
    //   2415: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2418: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2421: iload 4
    //   2423: istore 6
    //   2425: iload_2
    //   2426: istore 7
    //   2428: aload_0
    //   2429: aload_1
    //   2430: putfield 465	com/android/internal/telephony/uicc/SIMRecords:mEfCfis	[B
    //   2433: goto +9349 -> 11782
    //   2436: iconst_1
    //   2437: istore 4
    //   2439: iconst_1
    //   2440: istore_2
    //   2441: iconst_1
    //   2442: istore 8
    //   2444: iload 4
    //   2446: istore 6
    //   2448: iload_2
    //   2449: istore 7
    //   2451: aload_1
    //   2452: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2455: checkcast 600	android/os/AsyncResult
    //   2458: astore_1
    //   2459: iload 4
    //   2461: istore 6
    //   2463: iload_2
    //   2464: istore 7
    //   2466: aload_1
    //   2467: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2470: ifnull +6 -> 2476
    //   2473: goto +9309 -> 11782
    //   2476: iload 4
    //   2478: istore 6
    //   2480: iload_2
    //   2481: istore 7
    //   2483: aload_0
    //   2484: aload_1
    //   2485: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   2488: checkcast 609	[B
    //   2491: putfield 455	com/android/internal/telephony/uicc/SIMRecords:mCphsInfo	[B
    //   2494: iload 4
    //   2496: istore 6
    //   2498: iload_2
    //   2499: istore 7
    //   2501: new 529	java/lang/StringBuilder
    //   2504: astore_1
    //   2505: iload 4
    //   2507: istore 6
    //   2509: iload_2
    //   2510: istore 7
    //   2512: aload_1
    //   2513: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2516: iload 4
    //   2518: istore 6
    //   2520: iload_2
    //   2521: istore 7
    //   2523: aload_1
    //   2524: ldc_w 1303
    //   2527: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2530: pop
    //   2531: iload 4
    //   2533: istore 6
    //   2535: iload_2
    //   2536: istore 7
    //   2538: aload_1
    //   2539: aload_0
    //   2540: getfield 455	com/android/internal/telephony/uicc/SIMRecords:mCphsInfo	[B
    //   2543: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   2546: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2549: pop
    //   2550: iload 4
    //   2552: istore 6
    //   2554: iload_2
    //   2555: istore 7
    //   2557: aload_0
    //   2558: aload_1
    //   2559: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2562: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2565: goto +9217 -> 11782
    //   2568: iconst_0
    //   2569: istore_2
    //   2570: iconst_0
    //   2571: istore_3
    //   2572: iconst_0
    //   2573: istore 4
    //   2575: iload_2
    //   2576: istore 6
    //   2578: iload_3
    //   2579: istore 7
    //   2581: aload_1
    //   2582: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2585: checkcast 600	android/os/AsyncResult
    //   2588: astore 5
    //   2590: iload_2
    //   2591: istore 6
    //   2593: iload_3
    //   2594: istore 7
    //   2596: aload 5
    //   2598: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2601: ifnonnull +34 -> 2635
    //   2604: iload_2
    //   2605: istore 6
    //   2607: iload_3
    //   2608: istore 7
    //   2610: aload_0
    //   2611: aload_0
    //   2612: getfield 1306	com/android/internal/telephony/uicc/SIMRecords:mNewVoiceMailNum	Ljava/lang/String;
    //   2615: putfield 896	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailNum	Ljava/lang/String;
    //   2618: iload_2
    //   2619: istore 6
    //   2621: iload_3
    //   2622: istore 7
    //   2624: aload_0
    //   2625: aload_0
    //   2626: getfield 1309	com/android/internal/telephony/uicc/SIMRecords:mNewVoiceMailTag	Ljava/lang/String;
    //   2629: putfield 902	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailTag	Ljava/lang/String;
    //   2632: goto +67 -> 2699
    //   2635: iload_2
    //   2636: istore 6
    //   2638: iload_3
    //   2639: istore 7
    //   2641: new 529	java/lang/StringBuilder
    //   2644: astore_1
    //   2645: iload_2
    //   2646: istore 6
    //   2648: iload_3
    //   2649: istore 7
    //   2651: aload_1
    //   2652: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2655: iload_2
    //   2656: istore 6
    //   2658: iload_3
    //   2659: istore 7
    //   2661: aload_1
    //   2662: ldc_w 1311
    //   2665: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2668: pop
    //   2669: iload_2
    //   2670: istore 6
    //   2672: iload_3
    //   2673: istore 7
    //   2675: aload_1
    //   2676: aload 5
    //   2678: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2681: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2684: pop
    //   2685: iload_2
    //   2686: istore 6
    //   2688: iload_3
    //   2689: istore 7
    //   2691: aload_0
    //   2692: aload_1
    //   2693: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2696: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2699: iload 4
    //   2701: istore 8
    //   2703: iload_2
    //   2704: istore 6
    //   2706: iload_3
    //   2707: istore 7
    //   2709: aload 5
    //   2711: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   2714: ifnull +9068 -> 11782
    //   2717: iload_2
    //   2718: istore 6
    //   2720: iload_3
    //   2721: istore 7
    //   2723: aload_0
    //   2724: ldc_w 1316
    //   2727: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2730: iload_2
    //   2731: istore 6
    //   2733: iload_3
    //   2734: istore 7
    //   2736: aload 5
    //   2738: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   2741: checkcast 1218	android/os/Message
    //   2744: invokestatic 1319	android/os/AsyncResult:forMessage	(Landroid/os/Message;)Landroid/os/AsyncResult;
    //   2747: aload 5
    //   2749: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2752: putfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2755: iload_2
    //   2756: istore 6
    //   2758: iload_3
    //   2759: istore 7
    //   2761: aload 5
    //   2763: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   2766: checkcast 1218	android/os/Message
    //   2769: invokevirtual 1257	android/os/Message:sendToTarget	()V
    //   2772: iload 4
    //   2774: istore 8
    //   2776: goto +9006 -> 11782
    //   2779: iconst_1
    //   2780: istore 4
    //   2782: iconst_1
    //   2783: istore_2
    //   2784: iconst_1
    //   2785: istore 8
    //   2787: iload 4
    //   2789: istore 6
    //   2791: iload_2
    //   2792: istore 7
    //   2794: aload_1
    //   2795: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2798: checkcast 600	android/os/AsyncResult
    //   2801: astore 5
    //   2803: iload 4
    //   2805: istore 6
    //   2807: iload_2
    //   2808: istore 7
    //   2810: aload 5
    //   2812: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   2815: checkcast 609	[B
    //   2818: astore_1
    //   2819: iload 4
    //   2821: istore 6
    //   2823: iload_2
    //   2824: istore 7
    //   2826: aload 5
    //   2828: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2831: ifnull +18 -> 2849
    //   2834: iload 4
    //   2836: istore 6
    //   2838: iload_2
    //   2839: istore 7
    //   2841: aload_0
    //   2842: aconst_null
    //   2843: putfield 463	com/android/internal/telephony/uicc/SIMRecords:mEfCff	[B
    //   2846: goto +8936 -> 11782
    //   2849: iload 4
    //   2851: istore 6
    //   2853: iload_2
    //   2854: istore 7
    //   2856: new 529	java/lang/StringBuilder
    //   2859: astore 5
    //   2861: iload 4
    //   2863: istore 6
    //   2865: iload_2
    //   2866: istore 7
    //   2868: aload 5
    //   2870: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   2873: iload 4
    //   2875: istore 6
    //   2877: iload_2
    //   2878: istore 7
    //   2880: aload 5
    //   2882: ldc_w 1321
    //   2885: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2888: pop
    //   2889: iload 4
    //   2891: istore 6
    //   2893: iload_2
    //   2894: istore 7
    //   2896: aload 5
    //   2898: aload_1
    //   2899: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   2902: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2905: pop
    //   2906: iload 4
    //   2908: istore 6
    //   2910: iload_2
    //   2911: istore 7
    //   2913: aload_0
    //   2914: aload 5
    //   2916: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2919: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   2922: iload 4
    //   2924: istore 6
    //   2926: iload_2
    //   2927: istore 7
    //   2929: aload_0
    //   2930: aload_1
    //   2931: putfield 463	com/android/internal/telephony/uicc/SIMRecords:mEfCff	[B
    //   2934: goto +8848 -> 11782
    //   2937: iconst_0
    //   2938: istore 4
    //   2940: iconst_0
    //   2941: istore_2
    //   2942: iconst_0
    //   2943: istore 8
    //   2945: iload 4
    //   2947: istore 6
    //   2949: iload_2
    //   2950: istore 7
    //   2952: aload_1
    //   2953: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   2956: checkcast 600	android/os/AsyncResult
    //   2959: astore 5
    //   2961: iload 4
    //   2963: istore 6
    //   2965: iload_2
    //   2966: istore 7
    //   2968: aload 5
    //   2970: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   2973: ifnonnull +25 -> 2998
    //   2976: iload 4
    //   2978: istore 6
    //   2980: iload_2
    //   2981: istore 7
    //   2983: aload_0
    //   2984: aload 5
    //   2986: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   2989: checkcast 609	[B
    //   2992: invokespecial 1323	com/android/internal/telephony/uicc/SIMRecords:handleSms	([B)V
    //   2995: goto +8787 -> 11782
    //   2998: iload 4
    //   3000: istore 6
    //   3002: iload_2
    //   3003: istore 7
    //   3005: new 529	java/lang/StringBuilder
    //   3008: astore_1
    //   3009: iload 4
    //   3011: istore 6
    //   3013: iload_2
    //   3014: istore 7
    //   3016: aload_1
    //   3017: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   3020: iload 4
    //   3022: istore 6
    //   3024: iload_2
    //   3025: istore 7
    //   3027: aload_1
    //   3028: ldc_w 1325
    //   3031: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3034: pop
    //   3035: iload 4
    //   3037: istore 6
    //   3039: iload_2
    //   3040: istore 7
    //   3042: aload_1
    //   3043: aload 5
    //   3045: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3048: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3051: pop
    //   3052: iload 4
    //   3054: istore 6
    //   3056: iload_2
    //   3057: istore 7
    //   3059: aload_0
    //   3060: aload_1
    //   3061: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3064: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   3067: goto +8715 -> 11782
    //   3070: iconst_0
    //   3071: istore 4
    //   3073: iconst_0
    //   3074: istore_2
    //   3075: iconst_0
    //   3076: istore 8
    //   3078: iload 4
    //   3080: istore 6
    //   3082: iload_2
    //   3083: istore 7
    //   3085: aload_1
    //   3086: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   3089: checkcast 600	android/os/AsyncResult
    //   3092: astore 5
    //   3094: iload 4
    //   3096: istore 6
    //   3098: iload_2
    //   3099: istore 7
    //   3101: aload 5
    //   3103: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   3106: checkcast 1242	java/lang/Integer
    //   3109: astore_1
    //   3110: iload 4
    //   3112: istore 6
    //   3114: iload_2
    //   3115: istore 7
    //   3117: aload 5
    //   3119: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3122: ifnonnull +110 -> 3232
    //   3125: aload_1
    //   3126: ifnonnull +6 -> 3132
    //   3129: goto +103 -> 3232
    //   3132: iload 4
    //   3134: istore 6
    //   3136: iload_2
    //   3137: istore 7
    //   3139: new 529	java/lang/StringBuilder
    //   3142: astore 5
    //   3144: iload 4
    //   3146: istore 6
    //   3148: iload_2
    //   3149: istore 7
    //   3151: aload 5
    //   3153: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   3156: iload 4
    //   3158: istore 6
    //   3160: iload_2
    //   3161: istore 7
    //   3163: aload 5
    //   3165: ldc_w 1327
    //   3168: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3171: pop
    //   3172: iload 4
    //   3174: istore 6
    //   3176: iload_2
    //   3177: istore 7
    //   3179: aload 5
    //   3181: aload_1
    //   3182: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3185: pop
    //   3186: iload 4
    //   3188: istore 6
    //   3190: iload_2
    //   3191: istore 7
    //   3193: aload_0
    //   3194: aload 5
    //   3196: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3199: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   3202: iload 4
    //   3204: istore 6
    //   3206: iload_2
    //   3207: istore 7
    //   3209: aload_0
    //   3210: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   3213: sipush 28476
    //   3216: aload_1
    //   3217: invokevirtual 1330	java/lang/Integer:intValue	()I
    //   3220: aload_0
    //   3221: bipush 22
    //   3223: invokevirtual 649	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(I)Landroid/os/Message;
    //   3226: invokevirtual 760	com/android/internal/telephony/uicc/IccFileHandler:loadEFLinearFixed	(IILandroid/os/Message;)V
    //   3229: goto +8553 -> 11782
    //   3232: iload 4
    //   3234: istore 6
    //   3236: iload_2
    //   3237: istore 7
    //   3239: new 529	java/lang/StringBuilder
    //   3242: astore 9
    //   3244: iload 4
    //   3246: istore 6
    //   3248: iload_2
    //   3249: istore 7
    //   3251: aload 9
    //   3253: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   3256: iload 4
    //   3258: istore 6
    //   3260: iload_2
    //   3261: istore 7
    //   3263: aload 9
    //   3265: ldc_w 1332
    //   3268: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3271: pop
    //   3272: iload 4
    //   3274: istore 6
    //   3276: iload_2
    //   3277: istore 7
    //   3279: aload 9
    //   3281: aload 5
    //   3283: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3286: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3289: pop
    //   3290: iload 4
    //   3292: istore 6
    //   3294: iload_2
    //   3295: istore 7
    //   3297: aload 9
    //   3299: ldc_w 1334
    //   3302: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3305: pop
    //   3306: iload 4
    //   3308: istore 6
    //   3310: iload_2
    //   3311: istore 7
    //   3313: aload 9
    //   3315: aload_1
    //   3316: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3319: pop
    //   3320: iload 4
    //   3322: istore 6
    //   3324: iload_2
    //   3325: istore 7
    //   3327: aload_0
    //   3328: aload 9
    //   3330: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3333: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   3336: goto +8446 -> 11782
    //   3339: iconst_0
    //   3340: istore_2
    //   3341: iconst_0
    //   3342: istore_3
    //   3343: iconst_0
    //   3344: istore 4
    //   3346: iload_2
    //   3347: istore 6
    //   3349: iload_3
    //   3350: istore 7
    //   3352: aload_1
    //   3353: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   3356: checkcast 600	android/os/AsyncResult
    //   3359: astore 9
    //   3361: iload_2
    //   3362: istore 6
    //   3364: iload_3
    //   3365: istore 7
    //   3367: new 529	java/lang/StringBuilder
    //   3370: astore_1
    //   3371: iload_2
    //   3372: istore 6
    //   3374: iload_3
    //   3375: istore 7
    //   3377: aload_1
    //   3378: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   3381: iload_2
    //   3382: istore 6
    //   3384: iload_3
    //   3385: istore 7
    //   3387: aload_1
    //   3388: ldc_w 1336
    //   3391: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3394: pop
    //   3395: iload_2
    //   3396: istore 6
    //   3398: iload_3
    //   3399: istore 7
    //   3401: aload_1
    //   3402: aload 9
    //   3404: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3407: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3410: pop
    //   3411: iload_2
    //   3412: istore 6
    //   3414: iload_3
    //   3415: istore 7
    //   3417: aload_0
    //   3418: aload_1
    //   3419: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3422: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   3425: iload_2
    //   3426: istore 6
    //   3428: iload_3
    //   3429: istore 7
    //   3431: aload 9
    //   3433: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3436: ifnonnull +31 -> 3467
    //   3439: iload_2
    //   3440: istore 6
    //   3442: iload_3
    //   3443: istore 7
    //   3445: aload_0
    //   3446: aload_0
    //   3447: getfield 1306	com/android/internal/telephony/uicc/SIMRecords:mNewVoiceMailNum	Ljava/lang/String;
    //   3450: putfield 896	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailNum	Ljava/lang/String;
    //   3453: iload_2
    //   3454: istore 6
    //   3456: iload_3
    //   3457: istore 7
    //   3459: aload_0
    //   3460: aload_0
    //   3461: getfield 1309	com/android/internal/telephony/uicc/SIMRecords:mNewVoiceMailTag	Ljava/lang/String;
    //   3464: putfield 902	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailTag	Ljava/lang/String;
    //   3467: iload_2
    //   3468: istore 6
    //   3470: iload_3
    //   3471: istore 7
    //   3473: aload_0
    //   3474: invokespecial 1338	com/android/internal/telephony/uicc/SIMRecords:isCphsMailboxEnabled	()Z
    //   3477: ifeq +197 -> 3674
    //   3480: iload_2
    //   3481: istore 6
    //   3483: iload_3
    //   3484: istore 7
    //   3486: new 1340	com/android/internal/telephony/uicc/AdnRecord
    //   3489: astore 10
    //   3491: iload_2
    //   3492: istore 6
    //   3494: iload_3
    //   3495: istore 7
    //   3497: aload 10
    //   3499: aload_0
    //   3500: getfield 902	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailTag	Ljava/lang/String;
    //   3503: aload_0
    //   3504: getfield 896	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailNum	Ljava/lang/String;
    //   3507: invokespecial 1343	com/android/internal/telephony/uicc/AdnRecord:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   3510: iload_2
    //   3511: istore 6
    //   3513: iload_3
    //   3514: istore 7
    //   3516: aload 9
    //   3518: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3521: checkcast 1218	android/os/Message
    //   3524: astore 5
    //   3526: aload 5
    //   3528: astore_1
    //   3529: iload_2
    //   3530: istore 6
    //   3532: iload_3
    //   3533: istore 7
    //   3535: aload 9
    //   3537: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3540: ifnonnull +73 -> 3613
    //   3543: aload 5
    //   3545: astore_1
    //   3546: iload_2
    //   3547: istore 6
    //   3549: iload_3
    //   3550: istore 7
    //   3552: aload 9
    //   3554: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3557: ifnull +56 -> 3613
    //   3560: iload_2
    //   3561: istore 6
    //   3563: iload_3
    //   3564: istore 7
    //   3566: aload 9
    //   3568: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3571: checkcast 1218	android/os/Message
    //   3574: invokestatic 1319	android/os/AsyncResult:forMessage	(Landroid/os/Message;)Landroid/os/AsyncResult;
    //   3577: aconst_null
    //   3578: putfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3581: iload_2
    //   3582: istore 6
    //   3584: iload_3
    //   3585: istore 7
    //   3587: aload 9
    //   3589: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3592: checkcast 1218	android/os/Message
    //   3595: invokevirtual 1257	android/os/Message:sendToTarget	()V
    //   3598: iload_2
    //   3599: istore 6
    //   3601: iload_3
    //   3602: istore 7
    //   3604: aload_0
    //   3605: ldc_w 1345
    //   3608: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   3611: aconst_null
    //   3612: astore_1
    //   3613: iload_2
    //   3614: istore 6
    //   3616: iload_3
    //   3617: istore 7
    //   3619: new 1089	com/android/internal/telephony/uicc/AdnRecordLoader
    //   3622: astore 5
    //   3624: iload_2
    //   3625: istore 6
    //   3627: iload_3
    //   3628: istore 7
    //   3630: aload 5
    //   3632: aload_0
    //   3633: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   3636: invokespecial 1090	com/android/internal/telephony/uicc/AdnRecordLoader:<init>	(Lcom/android/internal/telephony/uicc/IccFileHandler;)V
    //   3639: iload_2
    //   3640: istore 6
    //   3642: iload_3
    //   3643: istore 7
    //   3645: aload 5
    //   3647: aload 10
    //   3649: sipush 28439
    //   3652: sipush 28490
    //   3655: iconst_1
    //   3656: aconst_null
    //   3657: aload_0
    //   3658: bipush 25
    //   3660: aload_1
    //   3661: invokevirtual 767	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(ILjava/lang/Object;)Landroid/os/Message;
    //   3664: invokevirtual 1349	com/android/internal/telephony/uicc/AdnRecordLoader:updateEF	(Lcom/android/internal/telephony/uicc/AdnRecord;IIILjava/lang/String;Landroid/os/Message;)V
    //   3667: iload 4
    //   3669: istore 8
    //   3671: goto +8111 -> 11782
    //   3674: iload 4
    //   3676: istore 8
    //   3678: iload_2
    //   3679: istore 6
    //   3681: iload_3
    //   3682: istore 7
    //   3684: aload 9
    //   3686: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3689: ifnull +8093 -> 11782
    //   3692: iload_2
    //   3693: istore 6
    //   3695: iload_3
    //   3696: istore 7
    //   3698: aload_0
    //   3699: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   3702: ldc_w 909
    //   3705: invokevirtual 915	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   3708: checkcast 917	android/telephony/CarrierConfigManager
    //   3711: astore_1
    //   3712: iload_2
    //   3713: istore 6
    //   3715: iload_3
    //   3716: istore 7
    //   3718: aload 9
    //   3720: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3723: ifnull +83 -> 3806
    //   3726: aload_1
    //   3727: ifnull +79 -> 3806
    //   3730: iload_2
    //   3731: istore 6
    //   3733: iload_3
    //   3734: istore 7
    //   3736: aload_1
    //   3737: invokevirtual 1353	android/telephony/CarrierConfigManager:getConfig	()Landroid/os/PersistableBundle;
    //   3740: ldc_w 1355
    //   3743: invokevirtual 936	android/os/PersistableBundle:getBoolean	(Ljava/lang/String;)Z
    //   3746: ifeq +60 -> 3806
    //   3749: iload_2
    //   3750: istore 6
    //   3752: iload_3
    //   3753: istore 7
    //   3755: aload 9
    //   3757: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3760: checkcast 1218	android/os/Message
    //   3763: invokestatic 1319	android/os/AsyncResult:forMessage	(Landroid/os/Message;)Landroid/os/AsyncResult;
    //   3766: astore 5
    //   3768: iload_2
    //   3769: istore 6
    //   3771: iload_3
    //   3772: istore 7
    //   3774: new 1357	com/android/internal/telephony/uicc/IccVmNotSupportedException
    //   3777: astore_1
    //   3778: iload_2
    //   3779: istore 6
    //   3781: iload_3
    //   3782: istore 7
    //   3784: aload_1
    //   3785: ldc_w 1359
    //   3788: invokespecial 1361	com/android/internal/telephony/uicc/IccVmNotSupportedException:<init>	(Ljava/lang/String;)V
    //   3791: iload_2
    //   3792: istore 6
    //   3794: iload_3
    //   3795: istore 7
    //   3797: aload 5
    //   3799: aload_1
    //   3800: putfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3803: goto +28 -> 3831
    //   3806: iload_2
    //   3807: istore 6
    //   3809: iload_3
    //   3810: istore 7
    //   3812: aload 9
    //   3814: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3817: checkcast 1218	android/os/Message
    //   3820: invokestatic 1319	android/os/AsyncResult:forMessage	(Landroid/os/Message;)Landroid/os/AsyncResult;
    //   3823: aload 9
    //   3825: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3828: putfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3831: iload_2
    //   3832: istore 6
    //   3834: iload_3
    //   3835: istore 7
    //   3837: aload 9
    //   3839: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   3842: checkcast 1218	android/os/Message
    //   3845: invokevirtual 1257	android/os/Message:sendToTarget	()V
    //   3848: iload 4
    //   3850: istore 8
    //   3852: goto +7930 -> 11782
    //   3855: iload_2
    //   3856: istore 6
    //   3858: iload_3
    //   3859: istore 7
    //   3861: new 529	java/lang/StringBuilder
    //   3864: astore 5
    //   3866: iload_2
    //   3867: istore 6
    //   3869: iload_3
    //   3870: istore 7
    //   3872: aload 5
    //   3874: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   3877: iload_2
    //   3878: istore 6
    //   3880: iload_3
    //   3881: istore 7
    //   3883: aload 5
    //   3885: ldc_w 1363
    //   3888: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3891: pop
    //   3892: iload_2
    //   3893: istore 6
    //   3895: iload_3
    //   3896: istore 7
    //   3898: aload 5
    //   3900: aload_1
    //   3901: getfield 1237	android/os/Message:arg1	I
    //   3904: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   3907: pop
    //   3908: iload_2
    //   3909: istore 6
    //   3911: iload_3
    //   3912: istore 7
    //   3914: ldc_w 689
    //   3917: aload 5
    //   3919: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3922: invokestatic 729	android/telephony/Rlog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   3925: pop
    //   3926: iload 4
    //   3928: istore 8
    //   3930: goto +7852 -> 11782
    //   3933: iconst_1
    //   3934: istore 4
    //   3936: iconst_1
    //   3937: istore_2
    //   3938: iconst_1
    //   3939: istore 8
    //   3941: iload 4
    //   3943: istore 6
    //   3945: iload_2
    //   3946: istore 7
    //   3948: aload_1
    //   3949: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   3952: checkcast 600	android/os/AsyncResult
    //   3955: astore_1
    //   3956: iload 4
    //   3958: istore 6
    //   3960: iload_2
    //   3961: istore 7
    //   3963: aload_1
    //   3964: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   3967: ifnull +6 -> 3973
    //   3970: goto +7812 -> 11782
    //   3973: iload 4
    //   3975: istore 6
    //   3977: iload_2
    //   3978: istore 7
    //   3980: aload_0
    //   3981: aload_1
    //   3982: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   3985: checkcast 715	java/util/ArrayList
    //   3988: invokespecial 1365	com/android/internal/telephony/uicc/SIMRecords:handleSmses	(Ljava/util/ArrayList;)V
    //   3991: goto +7791 -> 11782
    //   3994: iconst_1
    //   3995: istore 4
    //   3997: iconst_1
    //   3998: istore_2
    //   3999: iconst_1
    //   4000: istore 8
    //   4002: iload 4
    //   4004: istore 6
    //   4006: iload_2
    //   4007: istore 7
    //   4009: aload_1
    //   4010: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4013: checkcast 600	android/os/AsyncResult
    //   4016: astore 5
    //   4018: iload 4
    //   4020: istore 6
    //   4022: iload_2
    //   4023: istore 7
    //   4025: aload 5
    //   4027: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   4030: checkcast 609	[B
    //   4033: astore_1
    //   4034: iload 4
    //   4036: istore 6
    //   4038: iload_2
    //   4039: istore 7
    //   4041: aload 5
    //   4043: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4046: ifnull +6 -> 4052
    //   4049: goto +7733 -> 11782
    //   4052: iload 4
    //   4054: istore 6
    //   4056: iload_2
    //   4057: istore 7
    //   4059: new 1367	com/android/internal/telephony/uicc/UsimServiceTable
    //   4062: astore 5
    //   4064: iload 4
    //   4066: istore 6
    //   4068: iload_2
    //   4069: istore 7
    //   4071: aload 5
    //   4073: aload_1
    //   4074: invokespecial 1369	com/android/internal/telephony/uicc/UsimServiceTable:<init>	([B)V
    //   4077: iload 4
    //   4079: istore 6
    //   4081: iload_2
    //   4082: istore 7
    //   4084: aload_0
    //   4085: aload 5
    //   4087: putfield 1006	com/android/internal/telephony/uicc/SIMRecords:mUsimServiceTable	Lcom/android/internal/telephony/uicc/UsimServiceTable;
    //   4090: iload 4
    //   4092: istore 6
    //   4094: iload_2
    //   4095: istore 7
    //   4097: new 529	java/lang/StringBuilder
    //   4100: astore_1
    //   4101: iload 4
    //   4103: istore 6
    //   4105: iload_2
    //   4106: istore 7
    //   4108: aload_1
    //   4109: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4112: iload 4
    //   4114: istore 6
    //   4116: iload_2
    //   4117: istore 7
    //   4119: aload_1
    //   4120: ldc_w 1371
    //   4123: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4126: pop
    //   4127: iload 4
    //   4129: istore 6
    //   4131: iload_2
    //   4132: istore 7
    //   4134: aload_1
    //   4135: aload_0
    //   4136: getfield 1006	com/android/internal/telephony/uicc/SIMRecords:mUsimServiceTable	Lcom/android/internal/telephony/uicc/UsimServiceTable;
    //   4139: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   4142: pop
    //   4143: iload 4
    //   4145: istore 6
    //   4147: iload_2
    //   4148: istore 7
    //   4150: aload_0
    //   4151: aload_1
    //   4152: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4155: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   4158: goto +7624 -> 11782
    //   4161: iconst_1
    //   4162: istore_2
    //   4163: iconst_1
    //   4164: istore_3
    //   4165: iconst_1
    //   4166: istore 4
    //   4168: iload_2
    //   4169: istore 6
    //   4171: iload_3
    //   4172: istore 7
    //   4174: aload_1
    //   4175: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4178: checkcast 600	android/os/AsyncResult
    //   4181: astore 5
    //   4183: iload_2
    //   4184: istore 6
    //   4186: iload_3
    //   4187: istore 7
    //   4189: aload 5
    //   4191: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   4194: checkcast 609	[B
    //   4197: astore_1
    //   4198: iload_2
    //   4199: istore 6
    //   4201: iload_3
    //   4202: istore 7
    //   4204: aload 5
    //   4206: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4209: ifnull +10 -> 4219
    //   4212: iload 4
    //   4214: istore 8
    //   4216: goto +7566 -> 11782
    //   4219: iload_2
    //   4220: istore 6
    //   4222: iload_3
    //   4223: istore 7
    //   4225: new 829	com/android/internal/telephony/gsm/SimTlv
    //   4228: astore 5
    //   4230: iload_2
    //   4231: istore 6
    //   4233: iload_3
    //   4234: istore 7
    //   4236: aload 5
    //   4238: aload_1
    //   4239: iconst_0
    //   4240: aload_1
    //   4241: arraylength
    //   4242: invokespecial 832	com/android/internal/telephony/gsm/SimTlv:<init>	([BII)V
    //   4245: iload 4
    //   4247: istore 8
    //   4249: iload_2
    //   4250: istore 6
    //   4252: iload_3
    //   4253: istore 7
    //   4255: aload 5
    //   4257: invokevirtual 835	com/android/internal/telephony/gsm/SimTlv:isValidObject	()Z
    //   4260: ifeq +7522 -> 11782
    //   4263: iload_2
    //   4264: istore 6
    //   4266: iload_3
    //   4267: istore 7
    //   4269: aload 5
    //   4271: invokevirtual 838	com/android/internal/telephony/gsm/SimTlv:getTag	()I
    //   4274: bipush 67
    //   4276: if_icmpne +98 -> 4374
    //   4279: iload_2
    //   4280: istore 6
    //   4282: iload_3
    //   4283: istore 7
    //   4285: aload_0
    //   4286: aload 5
    //   4288: invokevirtual 842	com/android/internal/telephony/gsm/SimTlv:getData	()[B
    //   4291: iconst_0
    //   4292: aload 5
    //   4294: invokevirtual 842	com/android/internal/telephony/gsm/SimTlv:getData	()[B
    //   4297: arraylength
    //   4298: invokestatic 1374	com/android/internal/telephony/uicc/IccUtils:networkNameToString	([BII)Ljava/lang/String;
    //   4301: putfield 1040	com/android/internal/telephony/uicc/SIMRecords:mPnnHomeName	Ljava/lang/String;
    //   4304: iload_2
    //   4305: istore 6
    //   4307: iload_3
    //   4308: istore 7
    //   4310: new 529	java/lang/StringBuilder
    //   4313: astore_1
    //   4314: iload_2
    //   4315: istore 6
    //   4317: iload_3
    //   4318: istore 7
    //   4320: aload_1
    //   4321: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4324: iload_2
    //   4325: istore 6
    //   4327: iload_3
    //   4328: istore 7
    //   4330: aload_1
    //   4331: ldc_w 1376
    //   4334: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4337: pop
    //   4338: iload_2
    //   4339: istore 6
    //   4341: iload_3
    //   4342: istore 7
    //   4344: aload_1
    //   4345: aload_0
    //   4346: getfield 1040	com/android/internal/telephony/uicc/SIMRecords:mPnnHomeName	Ljava/lang/String;
    //   4349: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4352: pop
    //   4353: iload_2
    //   4354: istore 6
    //   4356: iload_3
    //   4357: istore 7
    //   4359: aload_0
    //   4360: aload_1
    //   4361: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4364: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   4367: iload 4
    //   4369: istore 8
    //   4371: goto +7411 -> 11782
    //   4374: iload_2
    //   4375: istore 6
    //   4377: iload_3
    //   4378: istore 7
    //   4380: aload 5
    //   4382: invokevirtual 845	com/android/internal/telephony/gsm/SimTlv:nextObject	()Z
    //   4385: pop
    //   4386: goto -141 -> 4245
    //   4389: iload_2
    //   4390: istore 6
    //   4392: iload_3
    //   4393: istore 7
    //   4395: aload_1
    //   4396: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4399: checkcast 600	android/os/AsyncResult
    //   4402: astore_1
    //   4403: iload 4
    //   4405: istore 8
    //   4407: iload_2
    //   4408: istore 6
    //   4410: iload_3
    //   4411: istore 7
    //   4413: aload_1
    //   4414: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4417: ifnull +7365 -> 11782
    //   4420: iload_2
    //   4421: istore 6
    //   4423: iload_3
    //   4424: istore 7
    //   4426: aload_0
    //   4427: ldc_w 1378
    //   4430: aload_1
    //   4431: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4434: invokevirtual 944	com/android/internal/telephony/uicc/SIMRecords:logw	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   4437: iload 4
    //   4439: istore 8
    //   4441: goto +7341 -> 11782
    //   4444: iconst_1
    //   4445: istore 4
    //   4447: iconst_1
    //   4448: istore_2
    //   4449: iconst_1
    //   4450: istore 8
    //   4452: iload 4
    //   4454: istore 6
    //   4456: iload_2
    //   4457: istore 7
    //   4459: aload_1
    //   4460: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4463: checkcast 600	android/os/AsyncResult
    //   4466: astore 5
    //   4468: iload 4
    //   4470: istore 6
    //   4472: iload_2
    //   4473: istore 7
    //   4475: aload 5
    //   4477: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   4480: checkcast 609	[B
    //   4483: astore_1
    //   4484: iload 4
    //   4486: istore 6
    //   4488: iload_2
    //   4489: istore 7
    //   4491: aload 5
    //   4493: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4496: ifnull +6 -> 4502
    //   4499: goto +7283 -> 11782
    //   4502: iload 4
    //   4504: istore 6
    //   4506: iload_2
    //   4507: istore 7
    //   4509: aload_0
    //   4510: aload_1
    //   4511: invokespecial 1380	com/android/internal/telephony/uicc/SIMRecords:parseEfSpdi	([B)V
    //   4514: goto +7268 -> 11782
    //   4517: iconst_1
    //   4518: istore 6
    //   4520: iconst_1
    //   4521: istore 7
    //   4523: iconst_1
    //   4524: istore 8
    //   4526: aload_0
    //   4527: iconst_0
    //   4528: aload_1
    //   4529: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4532: checkcast 600	android/os/AsyncResult
    //   4535: invokespecial 1100	com/android/internal/telephony/uicc/SIMRecords:getSpnFsm	(ZLandroid/os/AsyncResult;)V
    //   4538: goto +7244 -> 11782
    //   4541: iconst_1
    //   4542: istore 4
    //   4544: iconst_1
    //   4545: istore_2
    //   4546: iconst_1
    //   4547: istore 8
    //   4549: iload 4
    //   4551: istore 6
    //   4553: iload_2
    //   4554: istore 7
    //   4556: aload_1
    //   4557: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4560: checkcast 600	android/os/AsyncResult
    //   4563: astore_1
    //   4564: iload 4
    //   4566: istore 6
    //   4568: iload_2
    //   4569: istore 7
    //   4571: aload_1
    //   4572: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4575: ifnull +20 -> 4595
    //   4578: iload 4
    //   4580: istore 6
    //   4582: iload_2
    //   4583: istore 7
    //   4585: aload_0
    //   4586: ldc_w 1382
    //   4589: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   4592: goto +7190 -> 11782
    //   4595: iload 4
    //   4597: istore 6
    //   4599: iload_2
    //   4600: istore 7
    //   4602: aload_1
    //   4603: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   4606: checkcast 1340	com/android/internal/telephony/uicc/AdnRecord
    //   4609: astore_1
    //   4610: iload 4
    //   4612: istore 6
    //   4614: iload_2
    //   4615: istore 7
    //   4617: aload_0
    //   4618: aload_1
    //   4619: invokevirtual 1385	com/android/internal/telephony/uicc/AdnRecord:getNumber	()Ljava/lang/String;
    //   4622: putfield 1156	com/android/internal/telephony/uicc/SIMRecords:mMsisdn	Ljava/lang/String;
    //   4625: iload 4
    //   4627: istore 6
    //   4629: iload_2
    //   4630: istore 7
    //   4632: aload_0
    //   4633: aload_1
    //   4634: invokevirtual 1388	com/android/internal/telephony/uicc/AdnRecord:getAlphaTag	()Ljava/lang/String;
    //   4637: putfield 1152	com/android/internal/telephony/uicc/SIMRecords:mMsisdnTag	Ljava/lang/String;
    //   4640: iload 4
    //   4642: istore 6
    //   4644: iload_2
    //   4645: istore 7
    //   4647: new 529	java/lang/StringBuilder
    //   4650: astore_1
    //   4651: iload 4
    //   4653: istore 6
    //   4655: iload_2
    //   4656: istore 7
    //   4658: aload_1
    //   4659: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4662: iload 4
    //   4664: istore 6
    //   4666: iload_2
    //   4667: istore 7
    //   4669: aload_1
    //   4670: ldc_w 1390
    //   4673: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4676: pop
    //   4677: iload 4
    //   4679: istore 6
    //   4681: iload_2
    //   4682: istore 7
    //   4684: aload_1
    //   4685: ldc 104
    //   4687: aload_0
    //   4688: getfield 1156	com/android/internal/telephony/uicc/SIMRecords:mMsisdn	Ljava/lang/String;
    //   4691: invokestatic 1394	android/telephony/Rlog:pii	(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
    //   4694: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4697: pop
    //   4698: iload 4
    //   4700: istore 6
    //   4702: iload_2
    //   4703: istore 7
    //   4705: aload_0
    //   4706: aload_1
    //   4707: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4710: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   4713: goto +7069 -> 11782
    //   4716: iconst_1
    //   4717: istore 4
    //   4719: iconst_1
    //   4720: istore_2
    //   4721: iconst_1
    //   4722: istore_3
    //   4723: aload_0
    //   4724: getfield 1015	com/android/internal/telephony/uicc/SIMRecords:mCarrierTestOverride	Lcom/android/internal/telephony/uicc/CarrierTestOverride;
    //   4727: invokevirtual 1020	com/android/internal/telephony/uicc/CarrierTestOverride:isInTestMode	()Z
    //   4730: ifeq +109 -> 4839
    //   4733: aload_0
    //   4734: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   4737: ifnull +102 -> 4839
    //   4740: aload_0
    //   4741: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   4744: astore_1
    //   4745: aload_0
    //   4746: aload_1
    //   4747: iconst_0
    //   4748: iconst_3
    //   4749: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   4752: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   4755: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   4758: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4761: new 529	java/lang/StringBuilder
    //   4764: astore_1
    //   4765: aload_1
    //   4766: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4769: aload_1
    //   4770: ldc_w 1405
    //   4773: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4776: pop
    //   4777: aload_1
    //   4778: aload_0
    //   4779: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4782: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   4785: pop
    //   4786: aload_0
    //   4787: aload_1
    //   4788: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4791: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   4794: goto +42 -> 4836
    //   4797: astore_1
    //   4798: aload_0
    //   4799: iconst_0
    //   4800: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4803: new 529	java/lang/StringBuilder
    //   4806: astore_1
    //   4807: aload_1
    //   4808: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4811: aload_1
    //   4812: ldc_w 1407
    //   4815: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4818: pop
    //   4819: aload_1
    //   4820: aload_0
    //   4821: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4824: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   4827: pop
    //   4828: aload_0
    //   4829: aload_1
    //   4830: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4833: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   4836: goto +2557 -> 7393
    //   4839: aload_1
    //   4840: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   4843: checkcast 600	android/os/AsyncResult
    //   4846: astore 5
    //   4848: aload 5
    //   4850: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   4853: checkcast 609	[B
    //   4856: astore_1
    //   4857: aload 5
    //   4859: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   4862: astore 5
    //   4864: aload 5
    //   4866: ifnull +820 -> 5686
    //   4869: iload 4
    //   4871: istore 6
    //   4873: iload_2
    //   4874: istore 7
    //   4876: aload_0
    //   4877: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   4880: astore 5
    //   4882: iload 4
    //   4884: istore 6
    //   4886: iload_2
    //   4887: istore 7
    //   4889: aload_0
    //   4890: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4893: iconst_m1
    //   4894: if_icmpeq +32 -> 4926
    //   4897: iload 4
    //   4899: istore 6
    //   4901: iload_2
    //   4902: istore 7
    //   4904: aload_0
    //   4905: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4908: ifeq +18 -> 4926
    //   4911: iload 4
    //   4913: istore 6
    //   4915: iload_2
    //   4916: istore 7
    //   4918: aload_0
    //   4919: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   4922: iconst_2
    //   4923: if_icmpne +253 -> 5176
    //   4926: aload 5
    //   4928: ifnull +248 -> 5176
    //   4931: iload 4
    //   4933: istore 6
    //   4935: iload_2
    //   4936: istore 7
    //   4938: aload 5
    //   4940: invokevirtual 624	java/lang/String:length	()I
    //   4943: bipush 6
    //   4945: if_icmplt +231 -> 5176
    //   4948: iload 4
    //   4950: istore 6
    //   4952: iload_2
    //   4953: istore 7
    //   4955: aload 5
    //   4957: iconst_0
    //   4958: bipush 6
    //   4960: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   4963: astore_1
    //   4964: iload 4
    //   4966: istore 6
    //   4968: iload_2
    //   4969: istore 7
    //   4971: new 529	java/lang/StringBuilder
    //   4974: astore 9
    //   4976: iload 4
    //   4978: istore 6
    //   4980: iload_2
    //   4981: istore 7
    //   4983: aload 9
    //   4985: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   4988: iload 4
    //   4990: istore 6
    //   4992: iload_2
    //   4993: istore 7
    //   4995: aload 9
    //   4997: ldc_w 1409
    //   5000: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5003: pop
    //   5004: iload 4
    //   5006: istore 6
    //   5008: iload_2
    //   5009: istore 7
    //   5011: aload 9
    //   5013: aload_1
    //   5014: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5017: pop
    //   5018: iload 4
    //   5020: istore 6
    //   5022: iload_2
    //   5023: istore 7
    //   5025: aload_0
    //   5026: aload 9
    //   5028: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5031: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5034: iload 4
    //   5036: istore 6
    //   5038: iload_2
    //   5039: istore 7
    //   5041: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   5044: astore 9
    //   5046: iload 4
    //   5048: istore 6
    //   5050: iload_2
    //   5051: istore 7
    //   5053: aload 9
    //   5055: arraylength
    //   5056: istore 11
    //   5058: iconst_0
    //   5059: istore 8
    //   5061: iload 8
    //   5063: iload 11
    //   5065: if_icmpge +111 -> 5176
    //   5068: iload 4
    //   5070: istore 6
    //   5072: iload_2
    //   5073: istore 7
    //   5075: aload 9
    //   5077: iload 8
    //   5079: aaload
    //   5080: aload_1
    //   5081: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   5084: ifeq +86 -> 5170
    //   5087: iload 4
    //   5089: istore 6
    //   5091: iload_2
    //   5092: istore 7
    //   5094: aload_0
    //   5095: iconst_3
    //   5096: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5099: iload 4
    //   5101: istore 6
    //   5103: iload_2
    //   5104: istore 7
    //   5106: new 529	java/lang/StringBuilder
    //   5109: astore_1
    //   5110: iload 4
    //   5112: istore 6
    //   5114: iload_2
    //   5115: istore 7
    //   5117: aload_1
    //   5118: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5121: iload 4
    //   5123: istore 6
    //   5125: iload_2
    //   5126: istore 7
    //   5128: aload_1
    //   5129: ldc_w 1411
    //   5132: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5135: pop
    //   5136: iload 4
    //   5138: istore 6
    //   5140: iload_2
    //   5141: istore 7
    //   5143: aload_1
    //   5144: aload_0
    //   5145: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5148: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   5151: pop
    //   5152: iload 4
    //   5154: istore 6
    //   5156: iload_2
    //   5157: istore 7
    //   5159: aload_0
    //   5160: aload_1
    //   5161: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5164: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5167: goto +9 -> 5176
    //   5170: iinc 8 1
    //   5173: goto -112 -> 5061
    //   5176: iload 4
    //   5178: istore 6
    //   5180: iload_2
    //   5181: istore 7
    //   5183: aload_0
    //   5184: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5187: ifeq +22 -> 5209
    //   5190: iload 4
    //   5192: istore 6
    //   5194: iload_2
    //   5195: istore 7
    //   5197: aload_0
    //   5198: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5201: istore 8
    //   5203: iload 8
    //   5205: iconst_m1
    //   5206: if_icmpne +267 -> 5473
    //   5209: aload 5
    //   5211: ifnull +182 -> 5393
    //   5214: iload 4
    //   5216: istore 6
    //   5218: iload_2
    //   5219: istore 7
    //   5221: aload_0
    //   5222: aload 5
    //   5224: iconst_0
    //   5225: iconst_3
    //   5226: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   5229: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   5232: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   5235: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5238: iload 4
    //   5240: istore 6
    //   5242: iload_2
    //   5243: istore 7
    //   5245: new 529	java/lang/StringBuilder
    //   5248: astore_1
    //   5249: iload 4
    //   5251: istore 6
    //   5253: iload_2
    //   5254: istore 7
    //   5256: aload_1
    //   5257: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5260: iload 4
    //   5262: istore 6
    //   5264: iload_2
    //   5265: istore 7
    //   5267: aload_1
    //   5268: ldc_w 1413
    //   5271: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5274: pop
    //   5275: iload 4
    //   5277: istore 6
    //   5279: iload_2
    //   5280: istore 7
    //   5282: aload_1
    //   5283: aload_0
    //   5284: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5287: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   5290: pop
    //   5291: iload 4
    //   5293: istore 6
    //   5295: iload_2
    //   5296: istore 7
    //   5298: aload_0
    //   5299: aload_1
    //   5300: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5303: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5306: goto +84 -> 5390
    //   5309: astore_1
    //   5310: iload 4
    //   5312: istore 6
    //   5314: iload_2
    //   5315: istore 7
    //   5317: aload_0
    //   5318: iconst_0
    //   5319: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5322: iload 4
    //   5324: istore 6
    //   5326: iload_2
    //   5327: istore 7
    //   5329: new 529	java/lang/StringBuilder
    //   5332: astore_1
    //   5333: iload 4
    //   5335: istore 6
    //   5337: iload_2
    //   5338: istore 7
    //   5340: aload_1
    //   5341: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5344: iload 4
    //   5346: istore 6
    //   5348: iload_2
    //   5349: istore 7
    //   5351: aload_1
    //   5352: ldc_w 1415
    //   5355: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5358: pop
    //   5359: iload 4
    //   5361: istore 6
    //   5363: iload_2
    //   5364: istore 7
    //   5366: aload_1
    //   5367: aload_0
    //   5368: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5371: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   5374: pop
    //   5375: iload 4
    //   5377: istore 6
    //   5379: iload_2
    //   5380: istore 7
    //   5382: aload_0
    //   5383: aload_1
    //   5384: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5387: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   5390: goto +83 -> 5473
    //   5393: iload 4
    //   5395: istore 6
    //   5397: iload_2
    //   5398: istore 7
    //   5400: aload_0
    //   5401: iconst_0
    //   5402: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5405: iload 4
    //   5407: istore 6
    //   5409: iload_2
    //   5410: istore 7
    //   5412: new 529	java/lang/StringBuilder
    //   5415: astore_1
    //   5416: iload 4
    //   5418: istore 6
    //   5420: iload_2
    //   5421: istore 7
    //   5423: aload_1
    //   5424: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5427: iload 4
    //   5429: istore 6
    //   5431: iload_2
    //   5432: istore 7
    //   5434: aload_1
    //   5435: ldc_w 1417
    //   5438: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5441: pop
    //   5442: iload 4
    //   5444: istore 6
    //   5446: iload_2
    //   5447: istore 7
    //   5449: aload_1
    //   5450: aload_0
    //   5451: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5454: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   5457: pop
    //   5458: iload 4
    //   5460: istore 6
    //   5462: iload_2
    //   5463: istore 7
    //   5465: aload_0
    //   5466: aload_1
    //   5467: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5470: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5473: iload_3
    //   5474: istore 8
    //   5476: aload 5
    //   5478: ifnull +6304 -> 11782
    //   5481: iload_3
    //   5482: istore 8
    //   5484: iload 4
    //   5486: istore 6
    //   5488: iload_2
    //   5489: istore 7
    //   5491: aload_0
    //   5492: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5495: ifeq +6287 -> 11782
    //   5498: iload_3
    //   5499: istore 8
    //   5501: iload 4
    //   5503: istore 6
    //   5505: iload_2
    //   5506: istore 7
    //   5508: aload 5
    //   5510: invokevirtual 624	java/lang/String:length	()I
    //   5513: aload_0
    //   5514: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5517: iconst_3
    //   5518: iadd
    //   5519: if_icmplt +6263 -> 11782
    //   5522: iload 4
    //   5524: istore 6
    //   5526: iload_2
    //   5527: istore 7
    //   5529: aload_0
    //   5530: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   5533: aload_0
    //   5534: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   5537: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   5540: aload_0
    //   5541: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   5544: iconst_0
    //   5545: aload_0
    //   5546: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5549: iconst_3
    //   5550: iadd
    //   5551: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   5554: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   5557: iload 4
    //   5559: istore 6
    //   5561: iload_2
    //   5562: istore 7
    //   5564: new 529	java/lang/StringBuilder
    //   5567: astore_1
    //   5568: iload 4
    //   5570: istore 6
    //   5572: iload_2
    //   5573: istore 7
    //   5575: aload_1
    //   5576: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5579: iload 4
    //   5581: istore 6
    //   5583: iload_2
    //   5584: istore 7
    //   5586: aload_1
    //   5587: ldc_w 1425
    //   5590: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5593: pop
    //   5594: iload 4
    //   5596: istore 6
    //   5598: iload_2
    //   5599: istore 7
    //   5601: aload_1
    //   5602: aload 5
    //   5604: iconst_0
    //   5605: aload_0
    //   5606: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5609: iconst_3
    //   5610: iadd
    //   5611: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   5614: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5617: pop
    //   5618: iload 4
    //   5620: istore 6
    //   5622: iload_2
    //   5623: istore 7
    //   5625: aload_0
    //   5626: aload_1
    //   5627: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5630: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5633: iload 4
    //   5635: istore 6
    //   5637: iload_2
    //   5638: istore 7
    //   5640: aload_0
    //   5641: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   5644: astore_1
    //   5645: iload 4
    //   5647: istore 6
    //   5649: iload_2
    //   5650: istore 7
    //   5652: aload 5
    //   5654: iconst_0
    //   5655: iconst_3
    //   5656: aload_0
    //   5657: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5660: iadd
    //   5661: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   5664: astore 5
    //   5666: iload 4
    //   5668: istore 6
    //   5670: iload_2
    //   5671: istore 7
    //   5673: aload_1
    //   5674: aload 5
    //   5676: iconst_0
    //   5677: invokestatic 1429	com/android/internal/telephony/MccTable:updateMccMncConfiguration	(Landroid/content/Context;Ljava/lang/String;Z)V
    //   5680: iload_3
    //   5681: istore 8
    //   5683: goto +6099 -> 11782
    //   5686: new 529	java/lang/StringBuilder
    //   5689: astore 5
    //   5691: aload 5
    //   5693: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5696: aload 5
    //   5698: ldc_w 1431
    //   5701: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5704: pop
    //   5705: aload 5
    //   5707: aload_1
    //   5708: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   5711: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5714: pop
    //   5715: aload_0
    //   5716: aload 5
    //   5718: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5721: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5724: aload_1
    //   5725: arraylength
    //   5726: iconst_3
    //   5727: if_icmpge +810 -> 6537
    //   5730: aload_0
    //   5731: ldc_w 1433
    //   5734: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5737: iload 4
    //   5739: istore 6
    //   5741: iload_2
    //   5742: istore 7
    //   5744: aload_0
    //   5745: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   5748: astore 5
    //   5750: iload 4
    //   5752: istore 6
    //   5754: iload_2
    //   5755: istore 7
    //   5757: aload_0
    //   5758: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5761: iconst_m1
    //   5762: if_icmpeq +32 -> 5794
    //   5765: iload 4
    //   5767: istore 6
    //   5769: iload_2
    //   5770: istore 7
    //   5772: aload_0
    //   5773: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5776: ifeq +18 -> 5794
    //   5779: iload 4
    //   5781: istore 6
    //   5783: iload_2
    //   5784: istore 7
    //   5786: aload_0
    //   5787: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5790: iconst_2
    //   5791: if_icmpne +253 -> 6044
    //   5794: aload 5
    //   5796: ifnull +248 -> 6044
    //   5799: iload 4
    //   5801: istore 6
    //   5803: iload_2
    //   5804: istore 7
    //   5806: aload 5
    //   5808: invokevirtual 624	java/lang/String:length	()I
    //   5811: bipush 6
    //   5813: if_icmplt +231 -> 6044
    //   5816: iload 4
    //   5818: istore 6
    //   5820: iload_2
    //   5821: istore 7
    //   5823: aload 5
    //   5825: iconst_0
    //   5826: bipush 6
    //   5828: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   5831: astore_1
    //   5832: iload 4
    //   5834: istore 6
    //   5836: iload_2
    //   5837: istore 7
    //   5839: new 529	java/lang/StringBuilder
    //   5842: astore 9
    //   5844: iload 4
    //   5846: istore 6
    //   5848: iload_2
    //   5849: istore 7
    //   5851: aload 9
    //   5853: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5856: iload 4
    //   5858: istore 6
    //   5860: iload_2
    //   5861: istore 7
    //   5863: aload 9
    //   5865: ldc_w 1409
    //   5868: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5871: pop
    //   5872: iload 4
    //   5874: istore 6
    //   5876: iload_2
    //   5877: istore 7
    //   5879: aload 9
    //   5881: aload_1
    //   5882: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5885: pop
    //   5886: iload 4
    //   5888: istore 6
    //   5890: iload_2
    //   5891: istore 7
    //   5893: aload_0
    //   5894: aload 9
    //   5896: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5899: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   5902: iload 4
    //   5904: istore 6
    //   5906: iload_2
    //   5907: istore 7
    //   5909: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   5912: astore 9
    //   5914: iload 4
    //   5916: istore 6
    //   5918: iload_2
    //   5919: istore 7
    //   5921: aload 9
    //   5923: arraylength
    //   5924: istore 11
    //   5926: iconst_0
    //   5927: istore 8
    //   5929: iload 8
    //   5931: iload 11
    //   5933: if_icmpge +111 -> 6044
    //   5936: iload 4
    //   5938: istore 6
    //   5940: iload_2
    //   5941: istore 7
    //   5943: aload 9
    //   5945: iload 8
    //   5947: aaload
    //   5948: aload_1
    //   5949: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   5952: ifeq +86 -> 6038
    //   5955: iload 4
    //   5957: istore 6
    //   5959: iload_2
    //   5960: istore 7
    //   5962: aload_0
    //   5963: iconst_3
    //   5964: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   5967: iload 4
    //   5969: istore 6
    //   5971: iload_2
    //   5972: istore 7
    //   5974: new 529	java/lang/StringBuilder
    //   5977: astore_1
    //   5978: iload 4
    //   5980: istore 6
    //   5982: iload_2
    //   5983: istore 7
    //   5985: aload_1
    //   5986: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   5989: iload 4
    //   5991: istore 6
    //   5993: iload_2
    //   5994: istore 7
    //   5996: aload_1
    //   5997: ldc_w 1411
    //   6000: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6003: pop
    //   6004: iload 4
    //   6006: istore 6
    //   6008: iload_2
    //   6009: istore 7
    //   6011: aload_1
    //   6012: aload_0
    //   6013: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6016: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6019: pop
    //   6020: iload 4
    //   6022: istore 6
    //   6024: iload_2
    //   6025: istore 7
    //   6027: aload_0
    //   6028: aload_1
    //   6029: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6032: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6035: goto +9 -> 6044
    //   6038: iinc 8 1
    //   6041: goto -112 -> 5929
    //   6044: iload 4
    //   6046: istore 6
    //   6048: iload_2
    //   6049: istore 7
    //   6051: aload_0
    //   6052: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6055: ifeq +22 -> 6077
    //   6058: iload 4
    //   6060: istore 6
    //   6062: iload_2
    //   6063: istore 7
    //   6065: aload_0
    //   6066: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6069: istore 8
    //   6071: iload 8
    //   6073: iconst_m1
    //   6074: if_icmpne +267 -> 6341
    //   6077: aload 5
    //   6079: ifnull +182 -> 6261
    //   6082: iload 4
    //   6084: istore 6
    //   6086: iload_2
    //   6087: istore 7
    //   6089: aload_0
    //   6090: aload 5
    //   6092: iconst_0
    //   6093: iconst_3
    //   6094: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6097: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   6100: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   6103: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6106: iload 4
    //   6108: istore 6
    //   6110: iload_2
    //   6111: istore 7
    //   6113: new 529	java/lang/StringBuilder
    //   6116: astore_1
    //   6117: iload 4
    //   6119: istore 6
    //   6121: iload_2
    //   6122: istore 7
    //   6124: aload_1
    //   6125: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6128: iload 4
    //   6130: istore 6
    //   6132: iload_2
    //   6133: istore 7
    //   6135: aload_1
    //   6136: ldc_w 1413
    //   6139: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6142: pop
    //   6143: iload 4
    //   6145: istore 6
    //   6147: iload_2
    //   6148: istore 7
    //   6150: aload_1
    //   6151: aload_0
    //   6152: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6155: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6158: pop
    //   6159: iload 4
    //   6161: istore 6
    //   6163: iload_2
    //   6164: istore 7
    //   6166: aload_0
    //   6167: aload_1
    //   6168: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6171: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6174: goto +84 -> 6258
    //   6177: astore_1
    //   6178: iload 4
    //   6180: istore 6
    //   6182: iload_2
    //   6183: istore 7
    //   6185: aload_0
    //   6186: iconst_0
    //   6187: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6190: iload 4
    //   6192: istore 6
    //   6194: iload_2
    //   6195: istore 7
    //   6197: new 529	java/lang/StringBuilder
    //   6200: astore_1
    //   6201: iload 4
    //   6203: istore 6
    //   6205: iload_2
    //   6206: istore 7
    //   6208: aload_1
    //   6209: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6212: iload 4
    //   6214: istore 6
    //   6216: iload_2
    //   6217: istore 7
    //   6219: aload_1
    //   6220: ldc_w 1415
    //   6223: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6226: pop
    //   6227: iload 4
    //   6229: istore 6
    //   6231: iload_2
    //   6232: istore 7
    //   6234: aload_1
    //   6235: aload_0
    //   6236: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6239: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6242: pop
    //   6243: iload 4
    //   6245: istore 6
    //   6247: iload_2
    //   6248: istore 7
    //   6250: aload_0
    //   6251: aload_1
    //   6252: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6255: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   6258: goto +83 -> 6341
    //   6261: iload 4
    //   6263: istore 6
    //   6265: iload_2
    //   6266: istore 7
    //   6268: aload_0
    //   6269: iconst_0
    //   6270: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6273: iload 4
    //   6275: istore 6
    //   6277: iload_2
    //   6278: istore 7
    //   6280: new 529	java/lang/StringBuilder
    //   6283: astore_1
    //   6284: iload 4
    //   6286: istore 6
    //   6288: iload_2
    //   6289: istore 7
    //   6291: aload_1
    //   6292: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6295: iload 4
    //   6297: istore 6
    //   6299: iload_2
    //   6300: istore 7
    //   6302: aload_1
    //   6303: ldc_w 1417
    //   6306: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6309: pop
    //   6310: iload 4
    //   6312: istore 6
    //   6314: iload_2
    //   6315: istore 7
    //   6317: aload_1
    //   6318: aload_0
    //   6319: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6322: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6325: pop
    //   6326: iload 4
    //   6328: istore 6
    //   6330: iload_2
    //   6331: istore 7
    //   6333: aload_0
    //   6334: aload_1
    //   6335: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6338: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6341: iload_3
    //   6342: istore 8
    //   6344: aload 5
    //   6346: ifnull +5436 -> 11782
    //   6349: iload_3
    //   6350: istore 8
    //   6352: iload 4
    //   6354: istore 6
    //   6356: iload_2
    //   6357: istore 7
    //   6359: aload_0
    //   6360: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6363: ifeq +5419 -> 11782
    //   6366: iload_3
    //   6367: istore 8
    //   6369: iload 4
    //   6371: istore 6
    //   6373: iload_2
    //   6374: istore 7
    //   6376: aload 5
    //   6378: invokevirtual 624	java/lang/String:length	()I
    //   6381: aload_0
    //   6382: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6385: iconst_3
    //   6386: iadd
    //   6387: if_icmplt +5395 -> 11782
    //   6390: iload 4
    //   6392: istore 6
    //   6394: iload_2
    //   6395: istore 7
    //   6397: aload_0
    //   6398: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   6401: aload_0
    //   6402: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   6405: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   6408: aload_0
    //   6409: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   6412: iconst_0
    //   6413: aload_0
    //   6414: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6417: iconst_3
    //   6418: iadd
    //   6419: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6422: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   6425: iload 4
    //   6427: istore 6
    //   6429: iload_2
    //   6430: istore 7
    //   6432: new 529	java/lang/StringBuilder
    //   6435: astore_1
    //   6436: iload 4
    //   6438: istore 6
    //   6440: iload_2
    //   6441: istore 7
    //   6443: aload_1
    //   6444: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6447: iload 4
    //   6449: istore 6
    //   6451: iload_2
    //   6452: istore 7
    //   6454: aload_1
    //   6455: ldc_w 1425
    //   6458: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6461: pop
    //   6462: iload 4
    //   6464: istore 6
    //   6466: iload_2
    //   6467: istore 7
    //   6469: aload_1
    //   6470: aload 5
    //   6472: iconst_0
    //   6473: aload_0
    //   6474: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6477: iconst_3
    //   6478: iadd
    //   6479: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6482: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6485: pop
    //   6486: iload 4
    //   6488: istore 6
    //   6490: iload_2
    //   6491: istore 7
    //   6493: aload_0
    //   6494: aload_1
    //   6495: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6498: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6501: iload 4
    //   6503: istore 6
    //   6505: iload_2
    //   6506: istore 7
    //   6508: aload_0
    //   6509: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   6512: astore_1
    //   6513: iload 4
    //   6515: istore 6
    //   6517: iload_2
    //   6518: istore 7
    //   6520: aload 5
    //   6522: iconst_0
    //   6523: iconst_3
    //   6524: aload_0
    //   6525: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6528: iadd
    //   6529: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6532: astore 5
    //   6534: goto -868 -> 5666
    //   6537: aload_1
    //   6538: arraylength
    //   6539: iconst_3
    //   6540: if_icmpne +810 -> 7350
    //   6543: aload_0
    //   6544: ldc_w 1435
    //   6547: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6550: iload 4
    //   6552: istore 6
    //   6554: iload_2
    //   6555: istore 7
    //   6557: aload_0
    //   6558: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   6561: astore 5
    //   6563: iload 4
    //   6565: istore 6
    //   6567: iload_2
    //   6568: istore 7
    //   6570: aload_0
    //   6571: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6574: iconst_m1
    //   6575: if_icmpeq +32 -> 6607
    //   6578: iload 4
    //   6580: istore 6
    //   6582: iload_2
    //   6583: istore 7
    //   6585: aload_0
    //   6586: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6589: ifeq +18 -> 6607
    //   6592: iload 4
    //   6594: istore 6
    //   6596: iload_2
    //   6597: istore 7
    //   6599: aload_0
    //   6600: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6603: iconst_2
    //   6604: if_icmpne +253 -> 6857
    //   6607: aload 5
    //   6609: ifnull +248 -> 6857
    //   6612: iload 4
    //   6614: istore 6
    //   6616: iload_2
    //   6617: istore 7
    //   6619: aload 5
    //   6621: invokevirtual 624	java/lang/String:length	()I
    //   6624: bipush 6
    //   6626: if_icmplt +231 -> 6857
    //   6629: iload 4
    //   6631: istore 6
    //   6633: iload_2
    //   6634: istore 7
    //   6636: aload 5
    //   6638: iconst_0
    //   6639: bipush 6
    //   6641: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6644: astore_1
    //   6645: iload 4
    //   6647: istore 6
    //   6649: iload_2
    //   6650: istore 7
    //   6652: new 529	java/lang/StringBuilder
    //   6655: astore 9
    //   6657: iload 4
    //   6659: istore 6
    //   6661: iload_2
    //   6662: istore 7
    //   6664: aload 9
    //   6666: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6669: iload 4
    //   6671: istore 6
    //   6673: iload_2
    //   6674: istore 7
    //   6676: aload 9
    //   6678: ldc_w 1409
    //   6681: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6684: pop
    //   6685: iload 4
    //   6687: istore 6
    //   6689: iload_2
    //   6690: istore 7
    //   6692: aload 9
    //   6694: aload_1
    //   6695: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6698: pop
    //   6699: iload 4
    //   6701: istore 6
    //   6703: iload_2
    //   6704: istore 7
    //   6706: aload_0
    //   6707: aload 9
    //   6709: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6712: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6715: iload 4
    //   6717: istore 6
    //   6719: iload_2
    //   6720: istore 7
    //   6722: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   6725: astore 9
    //   6727: iload 4
    //   6729: istore 6
    //   6731: iload_2
    //   6732: istore 7
    //   6734: aload 9
    //   6736: arraylength
    //   6737: istore 11
    //   6739: iconst_0
    //   6740: istore 8
    //   6742: iload 8
    //   6744: iload 11
    //   6746: if_icmpge +111 -> 6857
    //   6749: iload 4
    //   6751: istore 6
    //   6753: iload_2
    //   6754: istore 7
    //   6756: aload 9
    //   6758: iload 8
    //   6760: aaload
    //   6761: aload_1
    //   6762: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   6765: ifeq +86 -> 6851
    //   6768: iload 4
    //   6770: istore 6
    //   6772: iload_2
    //   6773: istore 7
    //   6775: aload_0
    //   6776: iconst_3
    //   6777: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6780: iload 4
    //   6782: istore 6
    //   6784: iload_2
    //   6785: istore 7
    //   6787: new 529	java/lang/StringBuilder
    //   6790: astore_1
    //   6791: iload 4
    //   6793: istore 6
    //   6795: iload_2
    //   6796: istore 7
    //   6798: aload_1
    //   6799: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6802: iload 4
    //   6804: istore 6
    //   6806: iload_2
    //   6807: istore 7
    //   6809: aload_1
    //   6810: ldc_w 1411
    //   6813: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6816: pop
    //   6817: iload 4
    //   6819: istore 6
    //   6821: iload_2
    //   6822: istore 7
    //   6824: aload_1
    //   6825: aload_0
    //   6826: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6829: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6832: pop
    //   6833: iload 4
    //   6835: istore 6
    //   6837: iload_2
    //   6838: istore 7
    //   6840: aload_0
    //   6841: aload_1
    //   6842: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6845: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6848: goto +9 -> 6857
    //   6851: iinc 8 1
    //   6854: goto -112 -> 6742
    //   6857: iload 4
    //   6859: istore 6
    //   6861: iload_2
    //   6862: istore 7
    //   6864: aload_0
    //   6865: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6868: ifeq +22 -> 6890
    //   6871: iload 4
    //   6873: istore 6
    //   6875: iload_2
    //   6876: istore 7
    //   6878: aload_0
    //   6879: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6882: istore 8
    //   6884: iload 8
    //   6886: iconst_m1
    //   6887: if_icmpne +267 -> 7154
    //   6890: aload 5
    //   6892: ifnull +182 -> 7074
    //   6895: iload 4
    //   6897: istore 6
    //   6899: iload_2
    //   6900: istore 7
    //   6902: aload_0
    //   6903: aload 5
    //   6905: iconst_0
    //   6906: iconst_3
    //   6907: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   6910: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   6913: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   6916: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6919: iload 4
    //   6921: istore 6
    //   6923: iload_2
    //   6924: istore 7
    //   6926: new 529	java/lang/StringBuilder
    //   6929: astore_1
    //   6930: iload 4
    //   6932: istore 6
    //   6934: iload_2
    //   6935: istore 7
    //   6937: aload_1
    //   6938: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   6941: iload 4
    //   6943: istore 6
    //   6945: iload_2
    //   6946: istore 7
    //   6948: aload_1
    //   6949: ldc_w 1413
    //   6952: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6955: pop
    //   6956: iload 4
    //   6958: istore 6
    //   6960: iload_2
    //   6961: istore 7
    //   6963: aload_1
    //   6964: aload_0
    //   6965: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   6968: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   6971: pop
    //   6972: iload 4
    //   6974: istore 6
    //   6976: iload_2
    //   6977: istore 7
    //   6979: aload_0
    //   6980: aload_1
    //   6981: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6984: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   6987: goto +84 -> 7071
    //   6990: astore_1
    //   6991: iload 4
    //   6993: istore 6
    //   6995: iload_2
    //   6996: istore 7
    //   6998: aload_0
    //   6999: iconst_0
    //   7000: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7003: iload 4
    //   7005: istore 6
    //   7007: iload_2
    //   7008: istore 7
    //   7010: new 529	java/lang/StringBuilder
    //   7013: astore_1
    //   7014: iload 4
    //   7016: istore 6
    //   7018: iload_2
    //   7019: istore 7
    //   7021: aload_1
    //   7022: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7025: iload 4
    //   7027: istore 6
    //   7029: iload_2
    //   7030: istore 7
    //   7032: aload_1
    //   7033: ldc_w 1415
    //   7036: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7039: pop
    //   7040: iload 4
    //   7042: istore 6
    //   7044: iload_2
    //   7045: istore 7
    //   7047: aload_1
    //   7048: aload_0
    //   7049: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7052: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7055: pop
    //   7056: iload 4
    //   7058: istore 6
    //   7060: iload_2
    //   7061: istore 7
    //   7063: aload_0
    //   7064: aload_1
    //   7065: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7068: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   7071: goto +83 -> 7154
    //   7074: iload 4
    //   7076: istore 6
    //   7078: iload_2
    //   7079: istore 7
    //   7081: aload_0
    //   7082: iconst_0
    //   7083: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7086: iload 4
    //   7088: istore 6
    //   7090: iload_2
    //   7091: istore 7
    //   7093: new 529	java/lang/StringBuilder
    //   7096: astore_1
    //   7097: iload 4
    //   7099: istore 6
    //   7101: iload_2
    //   7102: istore 7
    //   7104: aload_1
    //   7105: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7108: iload 4
    //   7110: istore 6
    //   7112: iload_2
    //   7113: istore 7
    //   7115: aload_1
    //   7116: ldc_w 1417
    //   7119: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7122: pop
    //   7123: iload 4
    //   7125: istore 6
    //   7127: iload_2
    //   7128: istore 7
    //   7130: aload_1
    //   7131: aload_0
    //   7132: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7135: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7138: pop
    //   7139: iload 4
    //   7141: istore 6
    //   7143: iload_2
    //   7144: istore 7
    //   7146: aload_0
    //   7147: aload_1
    //   7148: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7151: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7154: iload_3
    //   7155: istore 8
    //   7157: aload 5
    //   7159: ifnull +4623 -> 11782
    //   7162: iload_3
    //   7163: istore 8
    //   7165: iload 4
    //   7167: istore 6
    //   7169: iload_2
    //   7170: istore 7
    //   7172: aload_0
    //   7173: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7176: ifeq +4606 -> 11782
    //   7179: iload_3
    //   7180: istore 8
    //   7182: iload 4
    //   7184: istore 6
    //   7186: iload_2
    //   7187: istore 7
    //   7189: aload 5
    //   7191: invokevirtual 624	java/lang/String:length	()I
    //   7194: aload_0
    //   7195: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7198: iconst_3
    //   7199: iadd
    //   7200: if_icmplt +4582 -> 11782
    //   7203: iload 4
    //   7205: istore 6
    //   7207: iload_2
    //   7208: istore 7
    //   7210: aload_0
    //   7211: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   7214: aload_0
    //   7215: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   7218: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   7221: aload_0
    //   7222: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   7225: iconst_0
    //   7226: aload_0
    //   7227: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7230: iconst_3
    //   7231: iadd
    //   7232: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   7235: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   7238: iload 4
    //   7240: istore 6
    //   7242: iload_2
    //   7243: istore 7
    //   7245: new 529	java/lang/StringBuilder
    //   7248: astore_1
    //   7249: iload 4
    //   7251: istore 6
    //   7253: iload_2
    //   7254: istore 7
    //   7256: aload_1
    //   7257: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7260: iload 4
    //   7262: istore 6
    //   7264: iload_2
    //   7265: istore 7
    //   7267: aload_1
    //   7268: ldc_w 1425
    //   7271: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7274: pop
    //   7275: iload 4
    //   7277: istore 6
    //   7279: iload_2
    //   7280: istore 7
    //   7282: aload_1
    //   7283: aload 5
    //   7285: iconst_0
    //   7286: aload_0
    //   7287: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7290: iconst_3
    //   7291: iadd
    //   7292: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   7295: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7298: pop
    //   7299: iload 4
    //   7301: istore 6
    //   7303: iload_2
    //   7304: istore 7
    //   7306: aload_0
    //   7307: aload_1
    //   7308: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7311: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7314: iload 4
    //   7316: istore 6
    //   7318: iload_2
    //   7319: istore 7
    //   7321: aload_0
    //   7322: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   7325: astore_1
    //   7326: iload 4
    //   7328: istore 6
    //   7330: iload_2
    //   7331: istore 7
    //   7333: aload 5
    //   7335: iconst_0
    //   7336: iconst_3
    //   7337: aload_0
    //   7338: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7341: iadd
    //   7342: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   7345: astore 5
    //   7347: goto -1681 -> 5666
    //   7350: aload_0
    //   7351: aload_1
    //   7352: iconst_3
    //   7353: baload
    //   7354: bipush 15
    //   7356: iand
    //   7357: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7360: new 529	java/lang/StringBuilder
    //   7363: astore_1
    //   7364: aload_1
    //   7365: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7368: aload_1
    //   7369: ldc_w 1437
    //   7372: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7375: pop
    //   7376: aload_1
    //   7377: aload_0
    //   7378: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7381: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7384: pop
    //   7385: aload_0
    //   7386: aload_1
    //   7387: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7390: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7393: aload_0
    //   7394: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7397: bipush 15
    //   7399: if_icmpne +44 -> 7443
    //   7402: aload_0
    //   7403: iconst_0
    //   7404: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7407: new 529	java/lang/StringBuilder
    //   7410: astore_1
    //   7411: aload_1
    //   7412: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7415: aload_1
    //   7416: ldc_w 1439
    //   7419: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7422: pop
    //   7423: aload_1
    //   7424: aload_0
    //   7425: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7428: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7431: pop
    //   7432: aload_0
    //   7433: aload_1
    //   7434: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7437: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7440: goto +57 -> 7497
    //   7443: aload_0
    //   7444: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7447: iconst_2
    //   7448: if_icmpeq +49 -> 7497
    //   7451: aload_0
    //   7452: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7455: iconst_3
    //   7456: if_icmpeq +41 -> 7497
    //   7459: aload_0
    //   7460: iconst_m1
    //   7461: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7464: new 529	java/lang/StringBuilder
    //   7467: astore_1
    //   7468: aload_1
    //   7469: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7472: aload_1
    //   7473: ldc_w 1439
    //   7476: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7479: pop
    //   7480: aload_1
    //   7481: aload_0
    //   7482: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7485: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7488: pop
    //   7489: aload_0
    //   7490: aload_1
    //   7491: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7494: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7497: iload 4
    //   7499: istore 6
    //   7501: iload_2
    //   7502: istore 7
    //   7504: aload_0
    //   7505: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   7508: astore_1
    //   7509: iload 4
    //   7511: istore 6
    //   7513: iload_2
    //   7514: istore 7
    //   7516: aload_0
    //   7517: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7520: iconst_m1
    //   7521: if_icmpeq +32 -> 7553
    //   7524: iload 4
    //   7526: istore 6
    //   7528: iload_2
    //   7529: istore 7
    //   7531: aload_0
    //   7532: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7535: ifeq +18 -> 7553
    //   7538: iload 4
    //   7540: istore 6
    //   7542: iload_2
    //   7543: istore 7
    //   7545: aload_0
    //   7546: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7549: iconst_2
    //   7550: if_icmpne +258 -> 7808
    //   7553: aload_1
    //   7554: ifnull +254 -> 7808
    //   7557: iload 4
    //   7559: istore 6
    //   7561: iload_2
    //   7562: istore 7
    //   7564: aload_1
    //   7565: invokevirtual 624	java/lang/String:length	()I
    //   7568: bipush 6
    //   7570: if_icmplt +238 -> 7808
    //   7573: iload 4
    //   7575: istore 6
    //   7577: iload_2
    //   7578: istore 7
    //   7580: aload_1
    //   7581: iconst_0
    //   7582: bipush 6
    //   7584: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   7587: astore 5
    //   7589: iload 4
    //   7591: istore 6
    //   7593: iload_2
    //   7594: istore 7
    //   7596: new 529	java/lang/StringBuilder
    //   7599: astore 9
    //   7601: iload 4
    //   7603: istore 6
    //   7605: iload_2
    //   7606: istore 7
    //   7608: aload 9
    //   7610: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7613: iload 4
    //   7615: istore 6
    //   7617: iload_2
    //   7618: istore 7
    //   7620: aload 9
    //   7622: ldc_w 1409
    //   7625: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7628: pop
    //   7629: iload 4
    //   7631: istore 6
    //   7633: iload_2
    //   7634: istore 7
    //   7636: aload 9
    //   7638: aload 5
    //   7640: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7643: pop
    //   7644: iload 4
    //   7646: istore 6
    //   7648: iload_2
    //   7649: istore 7
    //   7651: aload_0
    //   7652: aload 9
    //   7654: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7657: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7660: iload 4
    //   7662: istore 6
    //   7664: iload_2
    //   7665: istore 7
    //   7667: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   7670: astore 9
    //   7672: iload 4
    //   7674: istore 6
    //   7676: iload_2
    //   7677: istore 7
    //   7679: aload 9
    //   7681: arraylength
    //   7682: istore 11
    //   7684: iconst_0
    //   7685: istore 8
    //   7687: iload 8
    //   7689: iload 11
    //   7691: if_icmpge +117 -> 7808
    //   7694: iload 4
    //   7696: istore 6
    //   7698: iload_2
    //   7699: istore 7
    //   7701: aload 9
    //   7703: iload 8
    //   7705: aaload
    //   7706: aload 5
    //   7708: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   7711: ifeq +91 -> 7802
    //   7714: iload 4
    //   7716: istore 6
    //   7718: iload_2
    //   7719: istore 7
    //   7721: aload_0
    //   7722: iconst_3
    //   7723: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7726: iload 4
    //   7728: istore 6
    //   7730: iload_2
    //   7731: istore 7
    //   7733: new 529	java/lang/StringBuilder
    //   7736: astore 5
    //   7738: iload 4
    //   7740: istore 6
    //   7742: iload_2
    //   7743: istore 7
    //   7745: aload 5
    //   7747: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7750: iload 4
    //   7752: istore 6
    //   7754: iload_2
    //   7755: istore 7
    //   7757: aload 5
    //   7759: ldc_w 1411
    //   7762: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7765: pop
    //   7766: iload 4
    //   7768: istore 6
    //   7770: iload_2
    //   7771: istore 7
    //   7773: aload 5
    //   7775: aload_0
    //   7776: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7779: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7782: pop
    //   7783: iload 4
    //   7785: istore 6
    //   7787: iload_2
    //   7788: istore 7
    //   7790: aload_0
    //   7791: aload 5
    //   7793: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7796: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7799: goto +9 -> 7808
    //   7802: iinc 8 1
    //   7805: goto -118 -> 7687
    //   7808: iload 4
    //   7810: istore 6
    //   7812: iload_2
    //   7813: istore 7
    //   7815: aload_0
    //   7816: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7819: ifeq +22 -> 7841
    //   7822: iload 4
    //   7824: istore 6
    //   7826: iload_2
    //   7827: istore 7
    //   7829: aload_0
    //   7830: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7833: istore 8
    //   7835: iload 8
    //   7837: iconst_m1
    //   7838: if_icmpne +281 -> 8119
    //   7841: aload_1
    //   7842: ifnull +192 -> 8034
    //   7845: iload 4
    //   7847: istore 6
    //   7849: iload_2
    //   7850: istore 7
    //   7852: aload_0
    //   7853: aload_1
    //   7854: iconst_0
    //   7855: iconst_3
    //   7856: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   7859: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   7862: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   7865: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7868: iload 4
    //   7870: istore 6
    //   7872: iload_2
    //   7873: istore 7
    //   7875: new 529	java/lang/StringBuilder
    //   7878: astore 5
    //   7880: iload 4
    //   7882: istore 6
    //   7884: iload_2
    //   7885: istore 7
    //   7887: aload 5
    //   7889: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7892: iload 4
    //   7894: istore 6
    //   7896: iload_2
    //   7897: istore 7
    //   7899: aload 5
    //   7901: ldc_w 1413
    //   7904: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7907: pop
    //   7908: iload 4
    //   7910: istore 6
    //   7912: iload_2
    //   7913: istore 7
    //   7915: aload 5
    //   7917: aload_0
    //   7918: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7921: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7924: pop
    //   7925: iload 4
    //   7927: istore 6
    //   7929: iload_2
    //   7930: istore 7
    //   7932: aload_0
    //   7933: aload 5
    //   7935: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7938: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   7941: goto +90 -> 8031
    //   7944: astore 5
    //   7946: iload 4
    //   7948: istore 6
    //   7950: iload_2
    //   7951: istore 7
    //   7953: aload_0
    //   7954: iconst_0
    //   7955: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   7958: iload 4
    //   7960: istore 6
    //   7962: iload_2
    //   7963: istore 7
    //   7965: new 529	java/lang/StringBuilder
    //   7968: astore 5
    //   7970: iload 4
    //   7972: istore 6
    //   7974: iload_2
    //   7975: istore 7
    //   7977: aload 5
    //   7979: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   7982: iload 4
    //   7984: istore 6
    //   7986: iload_2
    //   7987: istore 7
    //   7989: aload 5
    //   7991: ldc_w 1415
    //   7994: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7997: pop
    //   7998: iload 4
    //   8000: istore 6
    //   8002: iload_2
    //   8003: istore 7
    //   8005: aload 5
    //   8007: aload_0
    //   8008: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8011: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8014: pop
    //   8015: iload 4
    //   8017: istore 6
    //   8019: iload_2
    //   8020: istore 7
    //   8022: aload_0
    //   8023: aload 5
    //   8025: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8028: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   8031: goto +88 -> 8119
    //   8034: iload 4
    //   8036: istore 6
    //   8038: iload_2
    //   8039: istore 7
    //   8041: aload_0
    //   8042: iconst_0
    //   8043: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8046: iload 4
    //   8048: istore 6
    //   8050: iload_2
    //   8051: istore 7
    //   8053: new 529	java/lang/StringBuilder
    //   8056: astore 5
    //   8058: iload 4
    //   8060: istore 6
    //   8062: iload_2
    //   8063: istore 7
    //   8065: aload 5
    //   8067: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8070: iload 4
    //   8072: istore 6
    //   8074: iload_2
    //   8075: istore 7
    //   8077: aload 5
    //   8079: ldc_w 1417
    //   8082: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8085: pop
    //   8086: iload 4
    //   8088: istore 6
    //   8090: iload_2
    //   8091: istore 7
    //   8093: aload 5
    //   8095: aload_0
    //   8096: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8099: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8102: pop
    //   8103: iload 4
    //   8105: istore 6
    //   8107: iload_2
    //   8108: istore 7
    //   8110: aload_0
    //   8111: aload 5
    //   8113: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8116: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8119: aload_1
    //   8120: ifnull +178 -> 8298
    //   8123: iload 4
    //   8125: istore 6
    //   8127: iload_2
    //   8128: istore 7
    //   8130: aload_0
    //   8131: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8134: ifeq +164 -> 8298
    //   8137: iload 4
    //   8139: istore 6
    //   8141: iload_2
    //   8142: istore 7
    //   8144: aload_1
    //   8145: invokevirtual 624	java/lang/String:length	()I
    //   8148: aload_0
    //   8149: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8152: iconst_3
    //   8153: iadd
    //   8154: if_icmplt +144 -> 8298
    //   8157: iload 4
    //   8159: istore 6
    //   8161: iload_2
    //   8162: istore 7
    //   8164: aload_0
    //   8165: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   8168: aload_0
    //   8169: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   8172: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   8175: aload_0
    //   8176: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   8179: iconst_0
    //   8180: aload_0
    //   8181: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8184: iconst_3
    //   8185: iadd
    //   8186: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   8189: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   8192: iload 4
    //   8194: istore 6
    //   8196: iload_2
    //   8197: istore 7
    //   8199: new 529	java/lang/StringBuilder
    //   8202: astore 5
    //   8204: iload 4
    //   8206: istore 6
    //   8208: iload_2
    //   8209: istore 7
    //   8211: aload 5
    //   8213: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8216: iload 4
    //   8218: istore 6
    //   8220: iload_2
    //   8221: istore 7
    //   8223: aload 5
    //   8225: ldc_w 1425
    //   8228: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8231: pop
    //   8232: iload 4
    //   8234: istore 6
    //   8236: iload_2
    //   8237: istore 7
    //   8239: aload 5
    //   8241: aload_1
    //   8242: iconst_0
    //   8243: aload_0
    //   8244: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8247: iconst_3
    //   8248: iadd
    //   8249: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   8252: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8255: pop
    //   8256: iload 4
    //   8258: istore 6
    //   8260: iload_2
    //   8261: istore 7
    //   8263: aload_0
    //   8264: aload 5
    //   8266: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8269: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8272: iload 4
    //   8274: istore 6
    //   8276: iload_2
    //   8277: istore 7
    //   8279: aload_0
    //   8280: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   8283: aload_1
    //   8284: iconst_0
    //   8285: iconst_3
    //   8286: aload_0
    //   8287: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8290: iadd
    //   8291: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   8294: iconst_0
    //   8295: invokestatic 1429	com/android/internal/telephony/MccTable:updateMccMncConfiguration	(Landroid/content/Context;Ljava/lang/String;Z)V
    //   8298: iload_3
    //   8299: istore 8
    //   8301: goto +3481 -> 11782
    //   8304: astore_1
    //   8305: iload 4
    //   8307: istore 6
    //   8309: iload_2
    //   8310: istore 7
    //   8312: aload_0
    //   8313: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   8316: astore 5
    //   8318: iload 4
    //   8320: istore 6
    //   8322: iload_2
    //   8323: istore 7
    //   8325: aload_0
    //   8326: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8329: iconst_m1
    //   8330: if_icmpeq +32 -> 8362
    //   8333: iload 4
    //   8335: istore 6
    //   8337: iload_2
    //   8338: istore 7
    //   8340: aload_0
    //   8341: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8344: ifeq +18 -> 8362
    //   8347: iload 4
    //   8349: istore 6
    //   8351: iload_2
    //   8352: istore 7
    //   8354: aload_0
    //   8355: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8358: iconst_2
    //   8359: if_icmpne +259 -> 8618
    //   8362: aload 5
    //   8364: ifnull +254 -> 8618
    //   8367: iload 4
    //   8369: istore 6
    //   8371: iload_2
    //   8372: istore 7
    //   8374: aload 5
    //   8376: invokevirtual 624	java/lang/String:length	()I
    //   8379: bipush 6
    //   8381: if_icmplt +237 -> 8618
    //   8384: iload 4
    //   8386: istore 6
    //   8388: iload_2
    //   8389: istore 7
    //   8391: aload 5
    //   8393: iconst_0
    //   8394: bipush 6
    //   8396: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   8399: astore 9
    //   8401: iload 4
    //   8403: istore 6
    //   8405: iload_2
    //   8406: istore 7
    //   8408: new 529	java/lang/StringBuilder
    //   8411: astore 10
    //   8413: iload 4
    //   8415: istore 6
    //   8417: iload_2
    //   8418: istore 7
    //   8420: aload 10
    //   8422: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8425: iload 4
    //   8427: istore 6
    //   8429: iload_2
    //   8430: istore 7
    //   8432: aload 10
    //   8434: ldc_w 1409
    //   8437: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8440: pop
    //   8441: iload 4
    //   8443: istore 6
    //   8445: iload_2
    //   8446: istore 7
    //   8448: aload 10
    //   8450: aload 9
    //   8452: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8455: pop
    //   8456: iload 4
    //   8458: istore 6
    //   8460: iload_2
    //   8461: istore 7
    //   8463: aload_0
    //   8464: aload 10
    //   8466: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8469: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8472: iload 4
    //   8474: istore 6
    //   8476: iload_2
    //   8477: istore 7
    //   8479: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   8482: astore 10
    //   8484: iload 4
    //   8486: istore 6
    //   8488: iload_2
    //   8489: istore 7
    //   8491: aload 10
    //   8493: arraylength
    //   8494: istore_3
    //   8495: iconst_0
    //   8496: istore 8
    //   8498: iload 8
    //   8500: iload_3
    //   8501: if_icmpge +117 -> 8618
    //   8504: iload 4
    //   8506: istore 6
    //   8508: iload_2
    //   8509: istore 7
    //   8511: aload 10
    //   8513: iload 8
    //   8515: aaload
    //   8516: aload 9
    //   8518: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   8521: ifeq +91 -> 8612
    //   8524: iload 4
    //   8526: istore 6
    //   8528: iload_2
    //   8529: istore 7
    //   8531: aload_0
    //   8532: iconst_3
    //   8533: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8536: iload 4
    //   8538: istore 6
    //   8540: iload_2
    //   8541: istore 7
    //   8543: new 529	java/lang/StringBuilder
    //   8546: astore 9
    //   8548: iload 4
    //   8550: istore 6
    //   8552: iload_2
    //   8553: istore 7
    //   8555: aload 9
    //   8557: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8560: iload 4
    //   8562: istore 6
    //   8564: iload_2
    //   8565: istore 7
    //   8567: aload 9
    //   8569: ldc_w 1411
    //   8572: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8575: pop
    //   8576: iload 4
    //   8578: istore 6
    //   8580: iload_2
    //   8581: istore 7
    //   8583: aload 9
    //   8585: aload_0
    //   8586: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8589: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8592: pop
    //   8593: iload 4
    //   8595: istore 6
    //   8597: iload_2
    //   8598: istore 7
    //   8600: aload_0
    //   8601: aload 9
    //   8603: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8606: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8609: goto +9 -> 8618
    //   8612: iinc 8 1
    //   8615: goto -117 -> 8498
    //   8618: iload 4
    //   8620: istore 6
    //   8622: iload_2
    //   8623: istore 7
    //   8625: aload_0
    //   8626: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8629: ifeq +22 -> 8651
    //   8632: iload 4
    //   8634: istore 6
    //   8636: iload_2
    //   8637: istore 7
    //   8639: aload_0
    //   8640: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8643: istore 8
    //   8645: iload 8
    //   8647: iconst_m1
    //   8648: if_icmpne +283 -> 8931
    //   8651: aload 5
    //   8653: ifnull +193 -> 8846
    //   8656: iload 4
    //   8658: istore 6
    //   8660: iload_2
    //   8661: istore 7
    //   8663: aload_0
    //   8664: aload 5
    //   8666: iconst_0
    //   8667: iconst_3
    //   8668: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   8671: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   8674: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   8677: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8680: iload 4
    //   8682: istore 6
    //   8684: iload_2
    //   8685: istore 7
    //   8687: new 529	java/lang/StringBuilder
    //   8690: astore 9
    //   8692: iload 4
    //   8694: istore 6
    //   8696: iload_2
    //   8697: istore 7
    //   8699: aload 9
    //   8701: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8704: iload 4
    //   8706: istore 6
    //   8708: iload_2
    //   8709: istore 7
    //   8711: aload 9
    //   8713: ldc_w 1413
    //   8716: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8719: pop
    //   8720: iload 4
    //   8722: istore 6
    //   8724: iload_2
    //   8725: istore 7
    //   8727: aload 9
    //   8729: aload_0
    //   8730: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8733: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8736: pop
    //   8737: iload 4
    //   8739: istore 6
    //   8741: iload_2
    //   8742: istore 7
    //   8744: aload_0
    //   8745: aload 9
    //   8747: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8750: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8753: goto +90 -> 8843
    //   8756: astore 9
    //   8758: iload 4
    //   8760: istore 6
    //   8762: iload_2
    //   8763: istore 7
    //   8765: aload_0
    //   8766: iconst_0
    //   8767: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8770: iload 4
    //   8772: istore 6
    //   8774: iload_2
    //   8775: istore 7
    //   8777: new 529	java/lang/StringBuilder
    //   8780: astore 9
    //   8782: iload 4
    //   8784: istore 6
    //   8786: iload_2
    //   8787: istore 7
    //   8789: aload 9
    //   8791: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8794: iload 4
    //   8796: istore 6
    //   8798: iload_2
    //   8799: istore 7
    //   8801: aload 9
    //   8803: ldc_w 1415
    //   8806: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8809: pop
    //   8810: iload 4
    //   8812: istore 6
    //   8814: iload_2
    //   8815: istore 7
    //   8817: aload 9
    //   8819: aload_0
    //   8820: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8823: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8826: pop
    //   8827: iload 4
    //   8829: istore 6
    //   8831: iload_2
    //   8832: istore 7
    //   8834: aload_0
    //   8835: aload 9
    //   8837: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8840: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   8843: goto +88 -> 8931
    //   8846: iload 4
    //   8848: istore 6
    //   8850: iload_2
    //   8851: istore 7
    //   8853: aload_0
    //   8854: iconst_0
    //   8855: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8858: iload 4
    //   8860: istore 6
    //   8862: iload_2
    //   8863: istore 7
    //   8865: new 529	java/lang/StringBuilder
    //   8868: astore 9
    //   8870: iload 4
    //   8872: istore 6
    //   8874: iload_2
    //   8875: istore 7
    //   8877: aload 9
    //   8879: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   8882: iload 4
    //   8884: istore 6
    //   8886: iload_2
    //   8887: istore 7
    //   8889: aload 9
    //   8891: ldc_w 1417
    //   8894: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8897: pop
    //   8898: iload 4
    //   8900: istore 6
    //   8902: iload_2
    //   8903: istore 7
    //   8905: aload 9
    //   8907: aload_0
    //   8908: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8911: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   8914: pop
    //   8915: iload 4
    //   8917: istore 6
    //   8919: iload_2
    //   8920: istore 7
    //   8922: aload_0
    //   8923: aload 9
    //   8925: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8928: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   8931: aload 5
    //   8933: ifnull +181 -> 9114
    //   8936: iload 4
    //   8938: istore 6
    //   8940: iload_2
    //   8941: istore 7
    //   8943: aload_0
    //   8944: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8947: ifeq +167 -> 9114
    //   8950: iload 4
    //   8952: istore 6
    //   8954: iload_2
    //   8955: istore 7
    //   8957: aload 5
    //   8959: invokevirtual 624	java/lang/String:length	()I
    //   8962: aload_0
    //   8963: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8966: iconst_3
    //   8967: iadd
    //   8968: if_icmplt +146 -> 9114
    //   8971: iload 4
    //   8973: istore 6
    //   8975: iload_2
    //   8976: istore 7
    //   8978: aload_0
    //   8979: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   8982: aload_0
    //   8983: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   8986: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   8989: aload_0
    //   8990: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   8993: iconst_0
    //   8994: aload_0
    //   8995: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   8998: iconst_3
    //   8999: iadd
    //   9000: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   9003: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   9006: iload 4
    //   9008: istore 6
    //   9010: iload_2
    //   9011: istore 7
    //   9013: new 529	java/lang/StringBuilder
    //   9016: astore 9
    //   9018: iload 4
    //   9020: istore 6
    //   9022: iload_2
    //   9023: istore 7
    //   9025: aload 9
    //   9027: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9030: iload 4
    //   9032: istore 6
    //   9034: iload_2
    //   9035: istore 7
    //   9037: aload 9
    //   9039: ldc_w 1425
    //   9042: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9045: pop
    //   9046: iload 4
    //   9048: istore 6
    //   9050: iload_2
    //   9051: istore 7
    //   9053: aload 9
    //   9055: aload 5
    //   9057: iconst_0
    //   9058: aload_0
    //   9059: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   9062: iconst_3
    //   9063: iadd
    //   9064: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   9067: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9070: pop
    //   9071: iload 4
    //   9073: istore 6
    //   9075: iload_2
    //   9076: istore 7
    //   9078: aload_0
    //   9079: aload 9
    //   9081: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9084: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9087: iload 4
    //   9089: istore 6
    //   9091: iload_2
    //   9092: istore 7
    //   9094: aload_0
    //   9095: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   9098: aload 5
    //   9100: iconst_0
    //   9101: iconst_3
    //   9102: aload_0
    //   9103: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   9106: iadd
    //   9107: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   9110: iconst_0
    //   9111: invokestatic 1429	com/android/internal/telephony/MccTable:updateMccMncConfiguration	(Landroid/content/Context;Ljava/lang/String;Z)V
    //   9114: iload 4
    //   9116: istore 6
    //   9118: iload_2
    //   9119: istore 7
    //   9121: aload_1
    //   9122: athrow
    //   9123: iconst_1
    //   9124: istore 4
    //   9126: iconst_1
    //   9127: istore_2
    //   9128: iconst_1
    //   9129: istore 8
    //   9131: iload 4
    //   9133: istore 6
    //   9135: iload_2
    //   9136: istore 7
    //   9138: aload_1
    //   9139: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   9142: checkcast 600	android/os/AsyncResult
    //   9145: astore_1
    //   9146: iload 4
    //   9148: istore 6
    //   9150: iload_2
    //   9151: istore 7
    //   9153: aload_1
    //   9154: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   9157: checkcast 609	[B
    //   9160: astore 5
    //   9162: iload 4
    //   9164: istore 6
    //   9166: iload_2
    //   9167: istore 7
    //   9169: new 529	java/lang/StringBuilder
    //   9172: astore 9
    //   9174: iload 4
    //   9176: istore 6
    //   9178: iload_2
    //   9179: istore 7
    //   9181: aload 9
    //   9183: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9186: iload 4
    //   9188: istore 6
    //   9190: iload_2
    //   9191: istore 7
    //   9193: aload 9
    //   9195: ldc_w 1441
    //   9198: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9201: pop
    //   9202: iload 4
    //   9204: istore 6
    //   9206: iload_2
    //   9207: istore 7
    //   9209: aload 9
    //   9211: aload 5
    //   9213: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   9216: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9219: pop
    //   9220: iload 4
    //   9222: istore 6
    //   9224: iload_2
    //   9225: istore 7
    //   9227: aload_0
    //   9228: aload 9
    //   9230: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9233: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9236: iload 4
    //   9238: istore 6
    //   9240: iload_2
    //   9241: istore 7
    //   9243: aload_1
    //   9244: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   9247: ifnull +79 -> 9326
    //   9250: iload 4
    //   9252: istore 6
    //   9254: iload_2
    //   9255: istore 7
    //   9257: new 529	java/lang/StringBuilder
    //   9260: astore 5
    //   9262: iload 4
    //   9264: istore 6
    //   9266: iload_2
    //   9267: istore 7
    //   9269: aload 5
    //   9271: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9274: iload 4
    //   9276: istore 6
    //   9278: iload_2
    //   9279: istore 7
    //   9281: aload 5
    //   9283: ldc_w 1443
    //   9286: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9289: pop
    //   9290: iload 4
    //   9292: istore 6
    //   9294: iload_2
    //   9295: istore 7
    //   9297: aload 5
    //   9299: aload_1
    //   9300: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   9303: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   9306: pop
    //   9307: iload 4
    //   9309: istore 6
    //   9311: iload_2
    //   9312: istore 7
    //   9314: aload_0
    //   9315: aload 5
    //   9317: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9320: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9323: goto +2459 -> 11782
    //   9326: iload 4
    //   9328: istore 6
    //   9330: iload_2
    //   9331: istore 7
    //   9333: aload_0
    //   9334: aload 5
    //   9336: putfield 461	com/android/internal/telephony/uicc/SIMRecords:mEfCPHS_MWI	[B
    //   9339: goto +2443 -> 11782
    //   9342: iconst_1
    //   9343: istore 4
    //   9345: iconst_1
    //   9346: istore_2
    //   9347: iconst_1
    //   9348: istore 8
    //   9350: iload 4
    //   9352: istore 6
    //   9354: iload_2
    //   9355: istore 7
    //   9357: aload_1
    //   9358: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   9361: checkcast 600	android/os/AsyncResult
    //   9364: astore_1
    //   9365: iload 4
    //   9367: istore 6
    //   9369: iload_2
    //   9370: istore 7
    //   9372: aload_1
    //   9373: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   9376: checkcast 609	[B
    //   9379: astore 5
    //   9381: iload 4
    //   9383: istore 6
    //   9385: iload_2
    //   9386: istore 7
    //   9388: new 529	java/lang/StringBuilder
    //   9391: astore 9
    //   9393: iload 4
    //   9395: istore 6
    //   9397: iload_2
    //   9398: istore 7
    //   9400: aload 9
    //   9402: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9405: iload 4
    //   9407: istore 6
    //   9409: iload_2
    //   9410: istore 7
    //   9412: aload 9
    //   9414: ldc_w 1445
    //   9417: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9420: pop
    //   9421: iload 4
    //   9423: istore 6
    //   9425: iload_2
    //   9426: istore 7
    //   9428: aload 9
    //   9430: aload 5
    //   9432: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   9435: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9438: pop
    //   9439: iload 4
    //   9441: istore 6
    //   9443: iload_2
    //   9444: istore 7
    //   9446: aload_0
    //   9447: aload 9
    //   9449: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9452: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9455: iload 4
    //   9457: istore 6
    //   9459: iload_2
    //   9460: istore 7
    //   9462: aload_1
    //   9463: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   9466: ifnull +79 -> 9545
    //   9469: iload 4
    //   9471: istore 6
    //   9473: iload_2
    //   9474: istore 7
    //   9476: new 529	java/lang/StringBuilder
    //   9479: astore 5
    //   9481: iload 4
    //   9483: istore 6
    //   9485: iload_2
    //   9486: istore 7
    //   9488: aload 5
    //   9490: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9493: iload 4
    //   9495: istore 6
    //   9497: iload_2
    //   9498: istore 7
    //   9500: aload 5
    //   9502: ldc_w 1447
    //   9505: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9508: pop
    //   9509: iload 4
    //   9511: istore 6
    //   9513: iload_2
    //   9514: istore 7
    //   9516: aload 5
    //   9518: aload_1
    //   9519: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   9522: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   9525: pop
    //   9526: iload 4
    //   9528: istore 6
    //   9530: iload_2
    //   9531: istore 7
    //   9533: aload_0
    //   9534: aload 5
    //   9536: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9539: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9542: goto +2240 -> 11782
    //   9545: aload 5
    //   9547: iconst_0
    //   9548: baload
    //   9549: sipush 255
    //   9552: iand
    //   9553: sipush 255
    //   9556: if_icmpne +20 -> 9576
    //   9559: iload 4
    //   9561: istore 6
    //   9563: iload_2
    //   9564: istore 7
    //   9566: aload_0
    //   9567: ldc_w 1449
    //   9570: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9573: goto +2209 -> 11782
    //   9576: iload 4
    //   9578: istore 6
    //   9580: iload_2
    //   9581: istore 7
    //   9583: aload_0
    //   9584: aload 5
    //   9586: putfield 459	com/android/internal/telephony/uicc/SIMRecords:mEfMWIS	[B
    //   9589: goto +2193 -> 11782
    //   9592: iload_2
    //   9593: istore 6
    //   9595: iload_3
    //   9596: istore 7
    //   9598: aload_0
    //   9599: aconst_null
    //   9600: putfield 896	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailNum	Ljava/lang/String;
    //   9603: iload_2
    //   9604: istore 6
    //   9606: iload_3
    //   9607: istore 7
    //   9609: aload_0
    //   9610: aconst_null
    //   9611: putfield 902	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailTag	Ljava/lang/String;
    //   9614: iconst_1
    //   9615: istore_2
    //   9616: iconst_1
    //   9617: istore_3
    //   9618: iconst_1
    //   9619: istore 4
    //   9621: iload_2
    //   9622: istore 6
    //   9624: iload_3
    //   9625: istore 7
    //   9627: aload_1
    //   9628: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   9631: checkcast 600	android/os/AsyncResult
    //   9634: astore 5
    //   9636: iload_2
    //   9637: istore 6
    //   9639: iload_3
    //   9640: istore 7
    //   9642: aload 5
    //   9644: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   9647: ifnull +186 -> 9833
    //   9650: iload_2
    //   9651: istore 6
    //   9653: iload_3
    //   9654: istore 7
    //   9656: new 529	java/lang/StringBuilder
    //   9659: astore 9
    //   9661: iload_2
    //   9662: istore 6
    //   9664: iload_3
    //   9665: istore 7
    //   9667: aload 9
    //   9669: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9672: iload_2
    //   9673: istore 6
    //   9675: iload_3
    //   9676: istore 7
    //   9678: aload 9
    //   9680: ldc_w 1451
    //   9683: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9686: pop
    //   9687: iload_2
    //   9688: istore 6
    //   9690: iload_3
    //   9691: istore 7
    //   9693: aload_1
    //   9694: getfield 1221	android/os/Message:what	I
    //   9697: bipush 11
    //   9699: if_icmpne +11 -> 9710
    //   9702: ldc_w 1453
    //   9705: astore 5
    //   9707: goto +8 -> 9715
    //   9710: ldc_w 1455
    //   9713: astore 5
    //   9715: iload_2
    //   9716: istore 6
    //   9718: iload_3
    //   9719: istore 7
    //   9721: aload 9
    //   9723: aload 5
    //   9725: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9728: pop
    //   9729: iload_2
    //   9730: istore 6
    //   9732: iload_3
    //   9733: istore 7
    //   9735: aload_0
    //   9736: aload 9
    //   9738: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9741: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9744: iload 4
    //   9746: istore 8
    //   9748: iload_2
    //   9749: istore 6
    //   9751: iload_3
    //   9752: istore 7
    //   9754: aload_1
    //   9755: getfield 1221	android/os/Message:what	I
    //   9758: bipush 6
    //   9760: if_icmpne +2022 -> 11782
    //   9763: iload_2
    //   9764: istore 6
    //   9766: iload_3
    //   9767: istore 7
    //   9769: aload_0
    //   9770: aload_0
    //   9771: getfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   9774: iconst_1
    //   9775: iadd
    //   9776: putfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   9779: iload_2
    //   9780: istore 6
    //   9782: iload_3
    //   9783: istore 7
    //   9785: new 1089	com/android/internal/telephony/uicc/AdnRecordLoader
    //   9788: astore_1
    //   9789: iload_2
    //   9790: istore 6
    //   9792: iload_3
    //   9793: istore 7
    //   9795: aload_1
    //   9796: aload_0
    //   9797: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   9800: invokespecial 1090	com/android/internal/telephony/uicc/AdnRecordLoader:<init>	(Lcom/android/internal/telephony/uicc/IccFileHandler;)V
    //   9803: iload_2
    //   9804: istore 6
    //   9806: iload_3
    //   9807: istore 7
    //   9809: aload_1
    //   9810: sipush 28439
    //   9813: sipush 28490
    //   9816: iconst_1
    //   9817: aload_0
    //   9818: bipush 11
    //   9820: invokevirtual 649	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(I)Landroid/os/Message;
    //   9823: invokevirtual 1096	com/android/internal/telephony/uicc/AdnRecordLoader:loadFromEF	(IIILandroid/os/Message;)V
    //   9826: iload 4
    //   9828: istore 8
    //   9830: goto +1952 -> 11782
    //   9833: iload_2
    //   9834: istore 6
    //   9836: iload_3
    //   9837: istore 7
    //   9839: aload 5
    //   9841: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   9844: checkcast 1340	com/android/internal/telephony/uicc/AdnRecord
    //   9847: astore 10
    //   9849: iload_2
    //   9850: istore 6
    //   9852: iload_3
    //   9853: istore 7
    //   9855: new 529	java/lang/StringBuilder
    //   9858: astore 9
    //   9860: iload_2
    //   9861: istore 6
    //   9863: iload_3
    //   9864: istore 7
    //   9866: aload 9
    //   9868: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   9871: iload_2
    //   9872: istore 6
    //   9874: iload_3
    //   9875: istore 7
    //   9877: aload 9
    //   9879: ldc_w 1457
    //   9882: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9885: pop
    //   9886: iload_2
    //   9887: istore 6
    //   9889: iload_3
    //   9890: istore 7
    //   9892: aload 9
    //   9894: aload 10
    //   9896: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   9899: pop
    //   9900: iload_2
    //   9901: istore 6
    //   9903: iload_3
    //   9904: istore 7
    //   9906: aload_1
    //   9907: getfield 1221	android/os/Message:what	I
    //   9910: bipush 11
    //   9912: if_icmpne +11 -> 9923
    //   9915: ldc_w 1459
    //   9918: astore 5
    //   9920: goto +8 -> 9928
    //   9923: ldc_w 1461
    //   9926: astore 5
    //   9928: iload_2
    //   9929: istore 6
    //   9931: iload_3
    //   9932: istore 7
    //   9934: aload 9
    //   9936: aload 5
    //   9938: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9941: pop
    //   9942: iload_2
    //   9943: istore 6
    //   9945: iload_3
    //   9946: istore 7
    //   9948: aload_0
    //   9949: aload 9
    //   9951: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9954: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   9957: iload_2
    //   9958: istore 6
    //   9960: iload_3
    //   9961: istore 7
    //   9963: aload 10
    //   9965: invokevirtual 1463	com/android/internal/telephony/uicc/AdnRecord:isEmpty	()Z
    //   9968: ifeq +88 -> 10056
    //   9971: iload_2
    //   9972: istore 6
    //   9974: iload_3
    //   9975: istore 7
    //   9977: aload_1
    //   9978: getfield 1221	android/os/Message:what	I
    //   9981: bipush 6
    //   9983: if_icmpne +73 -> 10056
    //   9986: iload_2
    //   9987: istore 6
    //   9989: iload_3
    //   9990: istore 7
    //   9992: aload_0
    //   9993: aload_0
    //   9994: getfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   9997: iconst_1
    //   9998: iadd
    //   9999: putfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   10002: iload_2
    //   10003: istore 6
    //   10005: iload_3
    //   10006: istore 7
    //   10008: new 1089	com/android/internal/telephony/uicc/AdnRecordLoader
    //   10011: astore_1
    //   10012: iload_2
    //   10013: istore 6
    //   10015: iload_3
    //   10016: istore 7
    //   10018: aload_1
    //   10019: aload_0
    //   10020: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   10023: invokespecial 1090	com/android/internal/telephony/uicc/AdnRecordLoader:<init>	(Lcom/android/internal/telephony/uicc/IccFileHandler;)V
    //   10026: iload_2
    //   10027: istore 6
    //   10029: iload_3
    //   10030: istore 7
    //   10032: aload_1
    //   10033: sipush 28439
    //   10036: sipush 28490
    //   10039: iconst_1
    //   10040: aload_0
    //   10041: bipush 11
    //   10043: invokevirtual 649	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(I)Landroid/os/Message;
    //   10046: invokevirtual 1096	com/android/internal/telephony/uicc/AdnRecordLoader:loadFromEF	(IIILandroid/os/Message;)V
    //   10049: iload 4
    //   10051: istore 8
    //   10053: goto +1729 -> 11782
    //   10056: iload_2
    //   10057: istore 6
    //   10059: iload_3
    //   10060: istore 7
    //   10062: aload_0
    //   10063: aload 10
    //   10065: invokevirtual 1385	com/android/internal/telephony/uicc/AdnRecord:getNumber	()Ljava/lang/String;
    //   10068: putfield 896	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailNum	Ljava/lang/String;
    //   10071: iload_2
    //   10072: istore 6
    //   10074: iload_3
    //   10075: istore 7
    //   10077: aload_0
    //   10078: aload 10
    //   10080: invokevirtual 1388	com/android/internal/telephony/uicc/AdnRecord:getAlphaTag	()Ljava/lang/String;
    //   10083: putfield 902	com/android/internal/telephony/uicc/SIMRecords:mVoiceMailTag	Ljava/lang/String;
    //   10086: iload 4
    //   10088: istore 8
    //   10090: goto +1692 -> 11782
    //   10093: iconst_1
    //   10094: istore_2
    //   10095: iconst_1
    //   10096: istore_3
    //   10097: iconst_1
    //   10098: istore 4
    //   10100: iload_2
    //   10101: istore 6
    //   10103: iload_3
    //   10104: istore 7
    //   10106: aload_1
    //   10107: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   10110: checkcast 600	android/os/AsyncResult
    //   10113: astore 5
    //   10115: iload_2
    //   10116: istore 6
    //   10118: iload_3
    //   10119: istore 7
    //   10121: aload 5
    //   10123: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   10126: checkcast 609	[B
    //   10129: astore_1
    //   10130: iconst_0
    //   10131: istore 11
    //   10133: iload 11
    //   10135: istore 8
    //   10137: iload_2
    //   10138: istore 6
    //   10140: iload_3
    //   10141: istore 7
    //   10143: aload 5
    //   10145: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   10148: ifnonnull +141 -> 10289
    //   10151: iload_2
    //   10152: istore 6
    //   10154: iload_3
    //   10155: istore 7
    //   10157: new 529	java/lang/StringBuilder
    //   10160: astore 5
    //   10162: iload_2
    //   10163: istore 6
    //   10165: iload_3
    //   10166: istore 7
    //   10168: aload 5
    //   10170: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10173: iload_2
    //   10174: istore 6
    //   10176: iload_3
    //   10177: istore 7
    //   10179: aload 5
    //   10181: ldc_w 1465
    //   10184: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10187: pop
    //   10188: iload_2
    //   10189: istore 6
    //   10191: iload_3
    //   10192: istore 7
    //   10194: aload 5
    //   10196: aload_1
    //   10197: invokestatic 805	com/android/internal/telephony/uicc/IccUtils:bytesToHexString	([B)Ljava/lang/String;
    //   10200: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10203: pop
    //   10204: iload_2
    //   10205: istore 6
    //   10207: iload_3
    //   10208: istore 7
    //   10210: aload_0
    //   10211: aload 5
    //   10213: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10216: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   10219: iload_2
    //   10220: istore 6
    //   10222: iload_3
    //   10223: istore 7
    //   10225: aload_0
    //   10226: aload_1
    //   10227: iconst_0
    //   10228: baload
    //   10229: sipush 255
    //   10232: iand
    //   10233: putfield 1188	com/android/internal/telephony/uicc/SIMRecords:mMailboxIndex	I
    //   10236: iload 11
    //   10238: istore 8
    //   10240: iload_2
    //   10241: istore 6
    //   10243: iload_3
    //   10244: istore 7
    //   10246: aload_0
    //   10247: getfield 1188	com/android/internal/telephony/uicc/SIMRecords:mMailboxIndex	I
    //   10250: ifeq +39 -> 10289
    //   10253: iload 11
    //   10255: istore 8
    //   10257: iload_2
    //   10258: istore 6
    //   10260: iload_3
    //   10261: istore 7
    //   10263: aload_0
    //   10264: getfield 1188	com/android/internal/telephony/uicc/SIMRecords:mMailboxIndex	I
    //   10267: sipush 255
    //   10270: if_icmpeq +19 -> 10289
    //   10273: iload_2
    //   10274: istore 6
    //   10276: iload_3
    //   10277: istore 7
    //   10279: aload_0
    //   10280: ldc_w 1467
    //   10283: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   10286: iconst_1
    //   10287: istore 8
    //   10289: iload_2
    //   10290: istore 6
    //   10292: iload_3
    //   10293: istore 7
    //   10295: aload_0
    //   10296: aload_0
    //   10297: getfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   10300: iconst_1
    //   10301: iadd
    //   10302: putfield 499	com/android/internal/telephony/uicc/SIMRecords:mRecordsToLoad	I
    //   10305: iload 8
    //   10307: ifeq +60 -> 10367
    //   10310: iload_2
    //   10311: istore 6
    //   10313: iload_3
    //   10314: istore 7
    //   10316: new 1089	com/android/internal/telephony/uicc/AdnRecordLoader
    //   10319: astore_1
    //   10320: iload_2
    //   10321: istore 6
    //   10323: iload_3
    //   10324: istore 7
    //   10326: aload_1
    //   10327: aload_0
    //   10328: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   10331: invokespecial 1090	com/android/internal/telephony/uicc/AdnRecordLoader:<init>	(Lcom/android/internal/telephony/uicc/IccFileHandler;)V
    //   10334: iload_2
    //   10335: istore 6
    //   10337: iload_3
    //   10338: istore 7
    //   10340: aload_1
    //   10341: sipush 28615
    //   10344: sipush 28616
    //   10347: aload_0
    //   10348: getfield 1188	com/android/internal/telephony/uicc/SIMRecords:mMailboxIndex	I
    //   10351: aload_0
    //   10352: bipush 6
    //   10354: invokevirtual 649	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(I)Landroid/os/Message;
    //   10357: invokevirtual 1096	com/android/internal/telephony/uicc/AdnRecordLoader:loadFromEF	(IIILandroid/os/Message;)V
    //   10360: iload 4
    //   10362: istore 8
    //   10364: goto +1418 -> 11782
    //   10367: iload_2
    //   10368: istore 6
    //   10370: iload_3
    //   10371: istore 7
    //   10373: new 1089	com/android/internal/telephony/uicc/AdnRecordLoader
    //   10376: astore_1
    //   10377: iload_2
    //   10378: istore 6
    //   10380: iload_3
    //   10381: istore 7
    //   10383: aload_1
    //   10384: aload_0
    //   10385: getfield 477	com/android/internal/telephony/uicc/SIMRecords:mFh	Lcom/android/internal/telephony/uicc/IccFileHandler;
    //   10388: invokespecial 1090	com/android/internal/telephony/uicc/AdnRecordLoader:<init>	(Lcom/android/internal/telephony/uicc/IccFileHandler;)V
    //   10391: iload_2
    //   10392: istore 6
    //   10394: iload_3
    //   10395: istore 7
    //   10397: aload_1
    //   10398: sipush 28439
    //   10401: sipush 28490
    //   10404: iconst_1
    //   10405: aload_0
    //   10406: bipush 11
    //   10408: invokevirtual 649	com/android/internal/telephony/uicc/SIMRecords:obtainMessage	(I)Landroid/os/Message;
    //   10411: invokevirtual 1096	com/android/internal/telephony/uicc/AdnRecordLoader:loadFromEF	(IIILandroid/os/Message;)V
    //   10414: iload 4
    //   10416: istore 8
    //   10418: goto +1364 -> 11782
    //   10421: iconst_1
    //   10422: istore 4
    //   10424: iconst_1
    //   10425: istore_2
    //   10426: iconst_1
    //   10427: istore 8
    //   10429: iload 4
    //   10431: istore 6
    //   10433: iload_2
    //   10434: istore 7
    //   10436: aload_1
    //   10437: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   10440: checkcast 600	android/os/AsyncResult
    //   10443: astore_1
    //   10444: iload 4
    //   10446: istore 6
    //   10448: iload_2
    //   10449: istore 7
    //   10451: aload_1
    //   10452: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   10455: checkcast 609	[B
    //   10458: astore 5
    //   10460: iload 4
    //   10462: istore 6
    //   10464: iload_2
    //   10465: istore 7
    //   10467: aload_1
    //   10468: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   10471: ifnull +6 -> 10477
    //   10474: goto +1308 -> 11782
    //   10477: iload 4
    //   10479: istore 6
    //   10481: iload_2
    //   10482: istore 7
    //   10484: aload_0
    //   10485: aload 5
    //   10487: iconst_0
    //   10488: aload 5
    //   10490: arraylength
    //   10491: invokestatic 1470	com/android/internal/telephony/uicc/IccUtils:bcdToString	([BII)Ljava/lang/String;
    //   10494: putfield 1473	com/android/internal/telephony/uicc/SIMRecords:mIccId	Ljava/lang/String;
    //   10497: iload 4
    //   10499: istore 6
    //   10501: iload_2
    //   10502: istore 7
    //   10504: aload_0
    //   10505: aload 5
    //   10507: iconst_0
    //   10508: aload 5
    //   10510: arraylength
    //   10511: invokestatic 1476	com/android/internal/telephony/uicc/IccUtils:bchToString	([BII)Ljava/lang/String;
    //   10514: putfield 1479	com/android/internal/telephony/uicc/SIMRecords:mFullIccId	Ljava/lang/String;
    //   10517: iload 4
    //   10519: istore 6
    //   10521: iload_2
    //   10522: istore 7
    //   10524: new 529	java/lang/StringBuilder
    //   10527: astore_1
    //   10528: iload 4
    //   10530: istore 6
    //   10532: iload_2
    //   10533: istore 7
    //   10535: aload_1
    //   10536: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10539: iload 4
    //   10541: istore 6
    //   10543: iload_2
    //   10544: istore 7
    //   10546: aload_1
    //   10547: ldc_w 1481
    //   10550: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10553: pop
    //   10554: iload 4
    //   10556: istore 6
    //   10558: iload_2
    //   10559: istore 7
    //   10561: aload_1
    //   10562: aload_0
    //   10563: getfield 1479	com/android/internal/telephony/uicc/SIMRecords:mFullIccId	Ljava/lang/String;
    //   10566: invokestatic 1486	android/telephony/SubscriptionInfo:givePrintableIccid	(Ljava/lang/String;)Ljava/lang/String;
    //   10569: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10572: pop
    //   10573: iload 4
    //   10575: istore 6
    //   10577: iload_2
    //   10578: istore 7
    //   10580: aload_0
    //   10581: aload_1
    //   10582: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10585: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   10588: goto +1194 -> 11782
    //   10591: iconst_1
    //   10592: istore_2
    //   10593: iconst_1
    //   10594: istore_3
    //   10595: iconst_1
    //   10596: istore 4
    //   10598: iload_2
    //   10599: istore 6
    //   10601: iload_3
    //   10602: istore 7
    //   10604: aload_1
    //   10605: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   10608: checkcast 600	android/os/AsyncResult
    //   10611: astore_1
    //   10612: iload_2
    //   10613: istore 6
    //   10615: iload_3
    //   10616: istore 7
    //   10618: aload_1
    //   10619: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   10622: ifnull +78 -> 10700
    //   10625: iload_2
    //   10626: istore 6
    //   10628: iload_3
    //   10629: istore 7
    //   10631: new 529	java/lang/StringBuilder
    //   10634: astore 5
    //   10636: iload_2
    //   10637: istore 6
    //   10639: iload_3
    //   10640: istore 7
    //   10642: aload 5
    //   10644: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10647: iload_2
    //   10648: istore 6
    //   10650: iload_3
    //   10651: istore 7
    //   10653: aload 5
    //   10655: ldc_w 1488
    //   10658: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10661: pop
    //   10662: iload_2
    //   10663: istore 6
    //   10665: iload_3
    //   10666: istore 7
    //   10668: aload 5
    //   10670: aload_1
    //   10671: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   10674: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   10677: pop
    //   10678: iload_2
    //   10679: istore 6
    //   10681: iload_3
    //   10682: istore 7
    //   10684: aload_0
    //   10685: aload 5
    //   10687: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10690: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   10693: iload 4
    //   10695: istore 8
    //   10697: goto +1085 -> 11782
    //   10700: iload_2
    //   10701: istore 6
    //   10703: iload_3
    //   10704: istore 7
    //   10706: aload_0
    //   10707: aload_1
    //   10708: getfield 608	android/os/AsyncResult:result	Ljava/lang/Object;
    //   10711: checkcast 142	java/lang/String
    //   10714: putfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10717: iload_2
    //   10718: istore 6
    //   10720: iload_3
    //   10721: istore 7
    //   10723: aload_0
    //   10724: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10727: ifnull +113 -> 10840
    //   10730: iload_2
    //   10731: istore 6
    //   10733: iload_3
    //   10734: istore 7
    //   10736: aload_0
    //   10737: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10740: invokevirtual 624	java/lang/String:length	()I
    //   10743: bipush 6
    //   10745: if_icmplt +21 -> 10766
    //   10748: iload_2
    //   10749: istore 6
    //   10751: iload_3
    //   10752: istore 7
    //   10754: aload_0
    //   10755: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10758: invokevirtual 624	java/lang/String:length	()I
    //   10761: bipush 15
    //   10763: if_icmple +77 -> 10840
    //   10766: iload_2
    //   10767: istore 6
    //   10769: iload_3
    //   10770: istore 7
    //   10772: new 529	java/lang/StringBuilder
    //   10775: astore_1
    //   10776: iload_2
    //   10777: istore 6
    //   10779: iload_3
    //   10780: istore 7
    //   10782: aload_1
    //   10783: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10786: iload_2
    //   10787: istore 6
    //   10789: iload_3
    //   10790: istore 7
    //   10792: aload_1
    //   10793: ldc_w 1490
    //   10796: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10799: pop
    //   10800: iload_2
    //   10801: istore 6
    //   10803: iload_3
    //   10804: istore 7
    //   10806: aload_1
    //   10807: aload_0
    //   10808: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10811: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10814: pop
    //   10815: iload_2
    //   10816: istore 6
    //   10818: iload_3
    //   10819: istore 7
    //   10821: aload_0
    //   10822: aload_1
    //   10823: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10826: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   10829: iload_2
    //   10830: istore 6
    //   10832: iload_3
    //   10833: istore 7
    //   10835: aload_0
    //   10836: aconst_null
    //   10837: putfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10840: iload_2
    //   10841: istore 6
    //   10843: iload_3
    //   10844: istore 7
    //   10846: new 529	java/lang/StringBuilder
    //   10849: astore_1
    //   10850: iload_2
    //   10851: istore 6
    //   10853: iload_3
    //   10854: istore 7
    //   10856: aload_1
    //   10857: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10860: iload_2
    //   10861: istore 6
    //   10863: iload_3
    //   10864: istore 7
    //   10866: aload_1
    //   10867: ldc_w 1492
    //   10870: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10873: pop
    //   10874: iload_2
    //   10875: istore 6
    //   10877: iload_3
    //   10878: istore 7
    //   10880: aload_1
    //   10881: aload_0
    //   10882: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   10885: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   10888: pop
    //   10889: iload_2
    //   10890: istore 6
    //   10892: iload_3
    //   10893: istore 7
    //   10895: aload_0
    //   10896: aload_1
    //   10897: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10900: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   10903: iload_2
    //   10904: istore 6
    //   10906: iload_3
    //   10907: istore 7
    //   10909: aload_0
    //   10910: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10913: ifnull +115 -> 11028
    //   10916: iload_2
    //   10917: istore 6
    //   10919: iload_3
    //   10920: istore 7
    //   10922: aload_0
    //   10923: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10926: invokevirtual 624	java/lang/String:length	()I
    //   10929: bipush 6
    //   10931: if_icmplt +97 -> 11028
    //   10934: iload_2
    //   10935: istore 6
    //   10937: iload_3
    //   10938: istore 7
    //   10940: new 529	java/lang/StringBuilder
    //   10943: astore_1
    //   10944: iload_2
    //   10945: istore 6
    //   10947: iload_3
    //   10948: istore 7
    //   10950: aload_1
    //   10951: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   10954: iload_2
    //   10955: istore 6
    //   10957: iload_3
    //   10958: istore 7
    //   10960: aload_1
    //   10961: ldc_w 1494
    //   10964: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10967: pop
    //   10968: iload_2
    //   10969: istore 6
    //   10971: iload_3
    //   10972: istore 7
    //   10974: aload_1
    //   10975: aload_0
    //   10976: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   10979: iconst_0
    //   10980: bipush 6
    //   10982: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   10985: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10988: pop
    //   10989: iload_2
    //   10990: istore 6
    //   10992: iload_3
    //   10993: istore 7
    //   10995: aload_1
    //   10996: ldc 104
    //   10998: aload_0
    //   10999: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   11002: bipush 6
    //   11004: invokevirtual 1497	java/lang/String:substring	(I)Ljava/lang/String;
    //   11007: invokestatic 1394	android/telephony/Rlog:pii	(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
    //   11010: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11013: pop
    //   11014: iload_2
    //   11015: istore 6
    //   11017: iload_3
    //   11018: istore 7
    //   11020: aload_0
    //   11021: aload_1
    //   11022: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   11025: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   11028: iload_2
    //   11029: istore 6
    //   11031: iload_3
    //   11032: istore 7
    //   11034: aload_0
    //   11035: invokevirtual 1159	com/android/internal/telephony/uicc/SIMRecords:getIMSI	()Ljava/lang/String;
    //   11038: astore_1
    //   11039: iload_2
    //   11040: istore 6
    //   11042: iload_3
    //   11043: istore 7
    //   11045: aload_0
    //   11046: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11049: ifeq +17 -> 11066
    //   11052: iload_2
    //   11053: istore 6
    //   11055: iload_3
    //   11056: istore 7
    //   11058: aload_0
    //   11059: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11062: iconst_2
    //   11063: if_icmpne +176 -> 11239
    //   11066: aload_1
    //   11067: ifnull +172 -> 11239
    //   11070: iload_2
    //   11071: istore 6
    //   11073: iload_3
    //   11074: istore 7
    //   11076: aload_1
    //   11077: invokevirtual 624	java/lang/String:length	()I
    //   11080: bipush 6
    //   11082: if_icmplt +157 -> 11239
    //   11085: iload_2
    //   11086: istore 6
    //   11088: iload_3
    //   11089: istore 7
    //   11091: aload_1
    //   11092: iconst_0
    //   11093: bipush 6
    //   11095: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   11098: astore 9
    //   11100: iload_2
    //   11101: istore 6
    //   11103: iload_3
    //   11104: istore 7
    //   11106: getstatic 448	com/android/internal/telephony/uicc/SIMRecords:MCCMNC_CODES_HAVING_3DIGITS_MNC	[Ljava/lang/String;
    //   11109: astore 5
    //   11111: iload_2
    //   11112: istore 6
    //   11114: iload_3
    //   11115: istore 7
    //   11117: aload 5
    //   11119: arraylength
    //   11120: istore 11
    //   11122: iconst_0
    //   11123: istore 8
    //   11125: iload 8
    //   11127: iload 11
    //   11129: if_icmpge +110 -> 11239
    //   11132: iload_2
    //   11133: istore 6
    //   11135: iload_3
    //   11136: istore 7
    //   11138: aload 5
    //   11140: iload 8
    //   11142: aaload
    //   11143: aload 9
    //   11145: invokevirtual 742	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   11148: ifeq +85 -> 11233
    //   11151: iload_2
    //   11152: istore 6
    //   11154: iload_3
    //   11155: istore 7
    //   11157: aload_0
    //   11158: iconst_3
    //   11159: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11162: iload_2
    //   11163: istore 6
    //   11165: iload_3
    //   11166: istore 7
    //   11168: new 529	java/lang/StringBuilder
    //   11171: astore 5
    //   11173: iload_2
    //   11174: istore 6
    //   11176: iload_3
    //   11177: istore 7
    //   11179: aload 5
    //   11181: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   11184: iload_2
    //   11185: istore 6
    //   11187: iload_3
    //   11188: istore 7
    //   11190: aload 5
    //   11192: ldc_w 1499
    //   11195: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11198: pop
    //   11199: iload_2
    //   11200: istore 6
    //   11202: iload_3
    //   11203: istore 7
    //   11205: aload 5
    //   11207: aload_0
    //   11208: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11211: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   11214: pop
    //   11215: iload_2
    //   11216: istore 6
    //   11218: iload_3
    //   11219: istore 7
    //   11221: aload_0
    //   11222: aload 5
    //   11224: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   11227: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   11230: goto +9 -> 11239
    //   11233: iinc 8 1
    //   11236: goto -111 -> 11125
    //   11239: iload_2
    //   11240: istore 6
    //   11242: iload_3
    //   11243: istore 7
    //   11245: aload_0
    //   11246: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11249: istore 8
    //   11251: iload 8
    //   11253: ifne +177 -> 11430
    //   11256: iload_2
    //   11257: istore 6
    //   11259: iload_3
    //   11260: istore 7
    //   11262: aload_0
    //   11263: aload_1
    //   11264: iconst_0
    //   11265: iconst_3
    //   11266: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   11269: invokestatic 1398	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   11272: invokestatic 1403	com/android/internal/telephony/MccTable:smallestDigitsMccForMnc	(I)I
    //   11275: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11278: iload_2
    //   11279: istore 6
    //   11281: iload_3
    //   11282: istore 7
    //   11284: new 529	java/lang/StringBuilder
    //   11287: astore 5
    //   11289: iload_2
    //   11290: istore 6
    //   11292: iload_3
    //   11293: istore 7
    //   11295: aload 5
    //   11297: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   11300: iload_2
    //   11301: istore 6
    //   11303: iload_3
    //   11304: istore 7
    //   11306: aload 5
    //   11308: ldc_w 1501
    //   11311: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11314: pop
    //   11315: iload_2
    //   11316: istore 6
    //   11318: iload_3
    //   11319: istore 7
    //   11321: aload 5
    //   11323: aload_0
    //   11324: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11327: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   11330: pop
    //   11331: iload_2
    //   11332: istore 6
    //   11334: iload_3
    //   11335: istore 7
    //   11337: aload_0
    //   11338: aload 5
    //   11340: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   11343: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   11346: goto +84 -> 11430
    //   11349: astore 5
    //   11351: iload_2
    //   11352: istore 6
    //   11354: iload_3
    //   11355: istore 7
    //   11357: aload_0
    //   11358: iconst_0
    //   11359: putfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11362: iload_2
    //   11363: istore 6
    //   11365: iload_3
    //   11366: istore 7
    //   11368: new 529	java/lang/StringBuilder
    //   11371: astore 5
    //   11373: iload_2
    //   11374: istore 6
    //   11376: iload_3
    //   11377: istore 7
    //   11379: aload 5
    //   11381: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   11384: iload_2
    //   11385: istore 6
    //   11387: iload_3
    //   11388: istore 7
    //   11390: aload 5
    //   11392: ldc_w 1503
    //   11395: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11398: pop
    //   11399: iload_2
    //   11400: istore 6
    //   11402: iload_3
    //   11403: istore 7
    //   11405: aload 5
    //   11407: aload_0
    //   11408: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11411: invokevirtual 660	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   11414: pop
    //   11415: iload_2
    //   11416: istore 6
    //   11418: iload_3
    //   11419: istore 7
    //   11421: aload_0
    //   11422: aload 5
    //   11424: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   11427: invokevirtual 795	com/android/internal/telephony/uicc/SIMRecords:loge	(Ljava/lang/String;)V
    //   11430: iload_2
    //   11431: istore 6
    //   11433: iload_3
    //   11434: istore 7
    //   11436: aload_0
    //   11437: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11440: ifeq +170 -> 11610
    //   11443: iload_2
    //   11444: istore 6
    //   11446: iload_3
    //   11447: istore 7
    //   11449: aload_0
    //   11450: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11453: iconst_m1
    //   11454: if_icmpeq +156 -> 11610
    //   11457: iload_2
    //   11458: istore 6
    //   11460: iload_3
    //   11461: istore 7
    //   11463: aload_1
    //   11464: invokevirtual 624	java/lang/String:length	()I
    //   11467: aload_0
    //   11468: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11471: iconst_3
    //   11472: iadd
    //   11473: if_icmplt +137 -> 11610
    //   11476: iload_2
    //   11477: istore 6
    //   11479: iload_3
    //   11480: istore 7
    //   11482: aload_0
    //   11483: getfield 632	com/android/internal/telephony/uicc/SIMRecords:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   11486: aload_0
    //   11487: getfield 516	com/android/internal/telephony/uicc/SIMRecords:mParentApp	Lcom/android/internal/telephony/uicc/UiccCardApplication;
    //   11490: invokevirtual 635	com/android/internal/telephony/uicc/UiccCardApplication:getPhoneId	()I
    //   11493: aload_0
    //   11494: getfield 1420	com/android/internal/telephony/uicc/SIMRecords:mImsi	Ljava/lang/String;
    //   11497: iconst_0
    //   11498: aload_0
    //   11499: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11502: iconst_3
    //   11503: iadd
    //   11504: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   11507: invokevirtual 1423	android/telephony/TelephonyManager:setSimOperatorNumericForPhone	(ILjava/lang/String;)V
    //   11510: iload_2
    //   11511: istore 6
    //   11513: iload_3
    //   11514: istore 7
    //   11516: new 529	java/lang/StringBuilder
    //   11519: astore 5
    //   11521: iload_2
    //   11522: istore 6
    //   11524: iload_3
    //   11525: istore 7
    //   11527: aload 5
    //   11529: invokespecial 530	java/lang/StringBuilder:<init>	()V
    //   11532: iload_2
    //   11533: istore 6
    //   11535: iload_3
    //   11536: istore 7
    //   11538: aload 5
    //   11540: ldc_w 1425
    //   11543: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11546: pop
    //   11547: iload_2
    //   11548: istore 6
    //   11550: iload_3
    //   11551: istore 7
    //   11553: aload 5
    //   11555: aload_1
    //   11556: iconst_0
    //   11557: aload_0
    //   11558: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11561: iconst_3
    //   11562: iadd
    //   11563: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   11566: invokevirtual 536	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11569: pop
    //   11570: iload_2
    //   11571: istore 6
    //   11573: iload_3
    //   11574: istore 7
    //   11576: aload_0
    //   11577: aload 5
    //   11579: invokevirtual 543	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   11582: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   11585: iload_2
    //   11586: istore 6
    //   11588: iload_3
    //   11589: istore 7
    //   11591: aload_0
    //   11592: getfield 907	com/android/internal/telephony/uicc/SIMRecords:mContext	Landroid/content/Context;
    //   11595: aload_1
    //   11596: iconst_0
    //   11597: iconst_3
    //   11598: aload_0
    //   11599: getfield 1164	com/android/internal/telephony/uicc/SIMRecords:mMncLength	I
    //   11602: iadd
    //   11603: invokevirtual 1168	java/lang/String:substring	(II)Ljava/lang/String;
    //   11606: iconst_0
    //   11607: invokestatic 1429	com/android/internal/telephony/MccTable:updateMccMncConfiguration	(Landroid/content/Context;Ljava/lang/String;Z)V
    //   11610: iload_2
    //   11611: istore 6
    //   11613: iload_3
    //   11614: istore 7
    //   11616: aload_0
    //   11617: getfield 1506	com/android/internal/telephony/uicc/SIMRecords:mImsiReadyRegistrants	Landroid/os/RegistrantList;
    //   11620: invokevirtual 682	android/os/RegistrantList:notifyRegistrants	()V
    //   11623: iload 4
    //   11625: istore 8
    //   11627: goto +155 -> 11782
    //   11630: iconst_0
    //   11631: istore_2
    //   11632: iconst_0
    //   11633: istore_3
    //   11634: iconst_0
    //   11635: istore 4
    //   11637: iload_2
    //   11638: istore 6
    //   11640: iload_3
    //   11641: istore 7
    //   11643: aload_1
    //   11644: getfield 1230	android/os/Message:obj	Ljava/lang/Object;
    //   11647: checkcast 600	android/os/AsyncResult
    //   11650: astore_1
    //   11651: iload_2
    //   11652: istore 6
    //   11654: iload_3
    //   11655: istore 7
    //   11657: aload_1
    //   11658: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   11661: ifnonnull +44 -> 11705
    //   11664: iload_2
    //   11665: istore 6
    //   11667: iload_3
    //   11668: istore 7
    //   11670: aload_0
    //   11671: aload_0
    //   11672: getfield 1509	com/android/internal/telephony/uicc/SIMRecords:mNewMsisdn	Ljava/lang/String;
    //   11675: putfield 1156	com/android/internal/telephony/uicc/SIMRecords:mMsisdn	Ljava/lang/String;
    //   11678: iload_2
    //   11679: istore 6
    //   11681: iload_3
    //   11682: istore 7
    //   11684: aload_0
    //   11685: aload_0
    //   11686: getfield 1512	com/android/internal/telephony/uicc/SIMRecords:mNewMsisdnTag	Ljava/lang/String;
    //   11689: putfield 1152	com/android/internal/telephony/uicc/SIMRecords:mMsisdnTag	Ljava/lang/String;
    //   11692: iload_2
    //   11693: istore 6
    //   11695: iload_3
    //   11696: istore 7
    //   11698: aload_0
    //   11699: ldc_w 1514
    //   11702: invokevirtual 547	com/android/internal/telephony/uicc/SIMRecords:log	(Ljava/lang/String;)V
    //   11705: iload 4
    //   11707: istore 8
    //   11709: iload_2
    //   11710: istore 6
    //   11712: iload_3
    //   11713: istore 7
    //   11715: aload_1
    //   11716: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   11719: ifnull +63 -> 11782
    //   11722: iload_2
    //   11723: istore 6
    //   11725: iload_3
    //   11726: istore 7
    //   11728: aload_1
    //   11729: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   11732: checkcast 1218	android/os/Message
    //   11735: invokestatic 1319	android/os/AsyncResult:forMessage	(Landroid/os/Message;)Landroid/os/AsyncResult;
    //   11738: aload_1
    //   11739: getfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   11742: putfield 604	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   11745: iload_2
    //   11746: istore 6
    //   11748: iload_3
    //   11749: istore 7
    //   11751: aload_1
    //   11752: getfield 1314	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   11755: checkcast 1218	android/os/Message
    //   11758: invokevirtual 1257	android/os/Message:sendToTarget	()V
    //   11761: iload 4
    //   11763: istore 8
    //   11765: goto +17 -> 11782
    //   11768: iload_2
    //   11769: istore 6
    //   11771: iload_3
    //   11772: istore 7
    //   11774: aload_0
    //   11775: invokevirtual 1517	com/android/internal/telephony/uicc/SIMRecords:onReady	()V
    //   11778: iload 4
    //   11780: istore 8
    //   11782: iload 8
    //   11784: ifeq +35 -> 11819
    //   11787: aload_0
    //   11788: invokevirtual 1520	com/android/internal/telephony/uicc/SIMRecords:onRecordLoaded	()V
    //   11791: goto +28 -> 11819
    //   11794: astore_1
    //   11795: goto +25 -> 11820
    //   11798: astore_1
    //   11799: iload 7
    //   11801: istore 6
    //   11803: aload_0
    //   11804: ldc_w 1522
    //   11807: aload_1
    //   11808: invokevirtual 944	com/android/internal/telephony/uicc/SIMRecords:logw	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   11811: iload 7
    //   11813: ifeq +6 -> 11819
    //   11816: goto -29 -> 11787
    //   11819: return
    //   11820: iload 6
    //   11822: ifeq +7 -> 11829
    //   11825: aload_0
    //   11826: invokevirtual 1520	com/android/internal/telephony/uicc/SIMRecords:onRecordLoaded	()V
    //   11829: aload_1
    //   11830: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	11831	0	this	SIMRecords
    //   0	11831	1	paramMessage	Message
    //   1	11768	2	i	int
    //   3	11769	3	j	int
    //   5	11774	4	k	int
    //   24	7910	5	localObject1	Object
    //   7944	1	5	localNumberFormatException1	NumberFormatException
    //   7968	3371	5	localObject2	Object
    //   11349	1	5	localNumberFormatException2	NumberFormatException
    //   11371	207	5	localStringBuilder	StringBuilder
    //   81	11740	6	m	int
    //   84	11728	7	n	int
    //   90	11693	8	i1	int
    //   410	8336	9	localObject3	Object
    //   8756	1	9	localNumberFormatException3	NumberFormatException
    //   8780	2364	9	localObject4	Object
    //   3489	6590	10	localObject5	Object
    //   5056	6074	11	i2	int
    // Exception table:
    //   from	to	target	type
    //   4745	4794	4797	java/lang/NumberFormatException
    //   5221	5238	5309	java/lang/NumberFormatException
    //   5245	5249	5309	java/lang/NumberFormatException
    //   5256	5260	5309	java/lang/NumberFormatException
    //   5267	5275	5309	java/lang/NumberFormatException
    //   5282	5291	5309	java/lang/NumberFormatException
    //   5298	5306	5309	java/lang/NumberFormatException
    //   6089	6106	6177	java/lang/NumberFormatException
    //   6113	6117	6177	java/lang/NumberFormatException
    //   6124	6128	6177	java/lang/NumberFormatException
    //   6135	6143	6177	java/lang/NumberFormatException
    //   6150	6159	6177	java/lang/NumberFormatException
    //   6166	6174	6177	java/lang/NumberFormatException
    //   6902	6919	6990	java/lang/NumberFormatException
    //   6926	6930	6990	java/lang/NumberFormatException
    //   6937	6941	6990	java/lang/NumberFormatException
    //   6948	6956	6990	java/lang/NumberFormatException
    //   6963	6972	6990	java/lang/NumberFormatException
    //   6979	6987	6990	java/lang/NumberFormatException
    //   7852	7868	7944	java/lang/NumberFormatException
    //   7875	7880	7944	java/lang/NumberFormatException
    //   7887	7892	7944	java/lang/NumberFormatException
    //   7899	7908	7944	java/lang/NumberFormatException
    //   7915	7925	7944	java/lang/NumberFormatException
    //   7932	7941	7944	java/lang/NumberFormatException
    //   4723	4745	8304	finally
    //   4745	4794	8304	finally
    //   4798	4836	8304	finally
    //   4839	4864	8304	finally
    //   5686	5737	8304	finally
    //   6537	6550	8304	finally
    //   7350	7393	8304	finally
    //   7393	7440	8304	finally
    //   7443	7497	8304	finally
    //   8663	8680	8756	java/lang/NumberFormatException
    //   8687	8692	8756	java/lang/NumberFormatException
    //   8699	8704	8756	java/lang/NumberFormatException
    //   8711	8720	8756	java/lang/NumberFormatException
    //   8727	8737	8756	java/lang/NumberFormatException
    //   8744	8753	8756	java/lang/NumberFormatException
    //   11262	11278	11349	java/lang/NumberFormatException
    //   11284	11289	11349	java/lang/NumberFormatException
    //   11295	11300	11349	java/lang/NumberFormatException
    //   11306	11315	11349	java/lang/NumberFormatException
    //   11321	11331	11349	java/lang/NumberFormatException
    //   11337	11346	11349	java/lang/NumberFormatException
    //   86	92	11794	finally
    //   338	343	11794	finally
    //   356	364	11794	finally
    //   386	395	11794	finally
    //   402	412	11794	finally
    //   419	427	11794	finally
    //   442	455	11794	finally
    //   462	472	11794	finally
    //   487	499	11794	finally
    //   510	528	11794	finally
    //   535	539	11794	finally
    //   549	556	11794	finally
    //   566	570	11794	finally
    //   577	581	11794	finally
    //   588	596	11794	finally
    //   603	613	11794	finally
    //   620	628	11794	finally
    //   646	654	11794	finally
    //   661	670	11794	finally
    //   677	684	11794	finally
    //   699	712	11794	finally
    //   722	727	11794	finally
    //   734	739	11794	finally
    //   746	755	11794	finally
    //   762	772	11794	finally
    //   779	788	11794	finally
    //   806	815	11794	finally
    //   822	831	11794	finally
    //   838	846	11794	finally
    //   860	865	11794	finally
    //   872	877	11794	finally
    //   884	893	11794	finally
    //   900	910	11794	finally
    //   917	926	11794	finally
    //   933	941	11794	finally
    //   948	952	11794	finally
    //   959	963	11794	finally
    //   970	978	11794	finally
    //   985	997	11794	finally
    //   1004	1012	11794	finally
    //   1022	1026	11794	finally
    //   1033	1037	11794	finally
    //   1044	1052	11794	finally
    //   1059	1069	11794	finally
    //   1076	1084	11794	finally
    //   1102	1110	11794	finally
    //   1117	1126	11794	finally
    //   1133	1140	11794	finally
    //   1155	1159	11794	finally
    //   1166	1170	11794	finally
    //   1177	1185	11794	finally
    //   1192	1202	11794	finally
    //   1209	1217	11794	finally
    //   1224	1233	11794	finally
    //   1243	1248	11794	finally
    //   1255	1260	11794	finally
    //   1267	1276	11794	finally
    //   1283	1293	11794	finally
    //   1300	1309	11794	finally
    //   1327	1335	11794	finally
    //   1342	1351	11794	finally
    //   1358	1365	11794	finally
    //   1380	1384	11794	finally
    //   1391	1395	11794	finally
    //   1402	1410	11794	finally
    //   1417	1427	11794	finally
    //   1434	1442	11794	finally
    //   1449	1458	11794	finally
    //   1468	1473	11794	finally
    //   1480	1485	11794	finally
    //   1492	1501	11794	finally
    //   1508	1518	11794	finally
    //   1525	1534	11794	finally
    //   1552	1560	11794	finally
    //   1567	1576	11794	finally
    //   1583	1590	11794	finally
    //   1597	1602	11794	finally
    //   1609	1614	11794	finally
    //   1621	1630	11794	finally
    //   1637	1647	11794	finally
    //   1654	1663	11794	finally
    //   1670	1675	11794	finally
    //   1685	1694	11794	finally
    //   1701	1706	11794	finally
    //   1713	1730	11794	finally
    //   1737	1741	11794	finally
    //   1748	1752	11794	finally
    //   1759	1767	11794	finally
    //   1774	1783	11794	finally
    //   1790	1798	11794	finally
    //   1816	1824	11794	finally
    //   1831	1840	11794	finally
    //   1847	1854	11794	finally
    //   1861	1866	11794	finally
    //   1873	1878	11794	finally
    //   1885	1894	11794	finally
    //   1901	1911	11794	finally
    //   1918	1927	11794	finally
    //   1934	1939	11794	finally
    //   1949	1958	11794	finally
    //   1965	1970	11794	finally
    //   1977	1994	11794	finally
    //   2001	2005	11794	finally
    //   2012	2016	11794	finally
    //   2023	2031	11794	finally
    //   2038	2047	11794	finally
    //   2054	2062	11794	finally
    //   2080	2089	11794	finally
    //   2096	2104	11794	finally
    //   2111	2115	11794	finally
    //   2122	2126	11794	finally
    //   2133	2141	11794	finally
    //   2148	2158	11794	finally
    //   2165	2173	11794	finally
    //   2183	2193	11794	finally
    //   2200	2204	11794	finally
    //   2211	2215	11794	finally
    //   2222	2230	11794	finally
    //   2237	2247	11794	finally
    //   2254	2262	11794	finally
    //   2269	2275	11794	finally
    //   2293	2302	11794	finally
    //   2309	2318	11794	finally
    //   2325	2333	11794	finally
    //   2340	2345	11794	finally
    //   2355	2360	11794	finally
    //   2367	2372	11794	finally
    //   2379	2388	11794	finally
    //   2395	2405	11794	finally
    //   2412	2421	11794	finally
    //   2428	2433	11794	finally
    //   2451	2459	11794	finally
    //   2466	2473	11794	finally
    //   2483	2494	11794	finally
    //   2501	2505	11794	finally
    //   2512	2516	11794	finally
    //   2523	2531	11794	finally
    //   2538	2550	11794	finally
    //   2557	2565	11794	finally
    //   2581	2590	11794	finally
    //   2596	2604	11794	finally
    //   2610	2618	11794	finally
    //   2624	2632	11794	finally
    //   2641	2645	11794	finally
    //   2651	2655	11794	finally
    //   2661	2669	11794	finally
    //   2675	2685	11794	finally
    //   2691	2699	11794	finally
    //   2709	2717	11794	finally
    //   2723	2730	11794	finally
    //   2736	2755	11794	finally
    //   2761	2772	11794	finally
    //   2794	2803	11794	finally
    //   2810	2819	11794	finally
    //   2826	2834	11794	finally
    //   2841	2846	11794	finally
    //   2856	2861	11794	finally
    //   2868	2873	11794	finally
    //   2880	2889	11794	finally
    //   2896	2906	11794	finally
    //   2913	2922	11794	finally
    //   2929	2934	11794	finally
    //   2952	2961	11794	finally
    //   2968	2976	11794	finally
    //   2983	2995	11794	finally
    //   3005	3009	11794	finally
    //   3016	3020	11794	finally
    //   3027	3035	11794	finally
    //   3042	3052	11794	finally
    //   3059	3067	11794	finally
    //   3085	3094	11794	finally
    //   3101	3110	11794	finally
    //   3117	3125	11794	finally
    //   3139	3144	11794	finally
    //   3151	3156	11794	finally
    //   3163	3172	11794	finally
    //   3179	3186	11794	finally
    //   3193	3202	11794	finally
    //   3209	3229	11794	finally
    //   3239	3244	11794	finally
    //   3251	3256	11794	finally
    //   3263	3272	11794	finally
    //   3279	3290	11794	finally
    //   3297	3306	11794	finally
    //   3313	3320	11794	finally
    //   3327	3336	11794	finally
    //   3352	3361	11794	finally
    //   3367	3371	11794	finally
    //   3377	3381	11794	finally
    //   3387	3395	11794	finally
    //   3401	3411	11794	finally
    //   3417	3425	11794	finally
    //   3431	3439	11794	finally
    //   3445	3453	11794	finally
    //   3459	3467	11794	finally
    //   3473	3480	11794	finally
    //   3486	3491	11794	finally
    //   3497	3510	11794	finally
    //   3516	3526	11794	finally
    //   3535	3543	11794	finally
    //   3552	3560	11794	finally
    //   3566	3581	11794	finally
    //   3587	3598	11794	finally
    //   3604	3611	11794	finally
    //   3619	3624	11794	finally
    //   3630	3639	11794	finally
    //   3645	3667	11794	finally
    //   3684	3692	11794	finally
    //   3698	3712	11794	finally
    //   3718	3726	11794	finally
    //   3736	3749	11794	finally
    //   3755	3768	11794	finally
    //   3774	3778	11794	finally
    //   3784	3791	11794	finally
    //   3797	3803	11794	finally
    //   3812	3831	11794	finally
    //   3837	3848	11794	finally
    //   3861	3866	11794	finally
    //   3872	3877	11794	finally
    //   3883	3892	11794	finally
    //   3898	3908	11794	finally
    //   3914	3926	11794	finally
    //   3948	3956	11794	finally
    //   3963	3970	11794	finally
    //   3980	3991	11794	finally
    //   4009	4018	11794	finally
    //   4025	4034	11794	finally
    //   4041	4049	11794	finally
    //   4059	4064	11794	finally
    //   4071	4077	11794	finally
    //   4084	4090	11794	finally
    //   4097	4101	11794	finally
    //   4108	4112	11794	finally
    //   4119	4127	11794	finally
    //   4134	4143	11794	finally
    //   4150	4158	11794	finally
    //   4174	4183	11794	finally
    //   4189	4198	11794	finally
    //   4204	4212	11794	finally
    //   4225	4230	11794	finally
    //   4236	4245	11794	finally
    //   4255	4263	11794	finally
    //   4269	4279	11794	finally
    //   4285	4304	11794	finally
    //   4310	4314	11794	finally
    //   4320	4324	11794	finally
    //   4330	4338	11794	finally
    //   4344	4353	11794	finally
    //   4359	4367	11794	finally
    //   4380	4386	11794	finally
    //   4395	4403	11794	finally
    //   4413	4420	11794	finally
    //   4426	4437	11794	finally
    //   4459	4468	11794	finally
    //   4475	4484	11794	finally
    //   4491	4499	11794	finally
    //   4509	4514	11794	finally
    //   4526	4538	11794	finally
    //   4556	4564	11794	finally
    //   4571	4578	11794	finally
    //   4585	4592	11794	finally
    //   4602	4610	11794	finally
    //   4617	4625	11794	finally
    //   4632	4640	11794	finally
    //   4647	4651	11794	finally
    //   4658	4662	11794	finally
    //   4669	4677	11794	finally
    //   4684	4698	11794	finally
    //   4705	4713	11794	finally
    //   4876	4882	11794	finally
    //   4889	4897	11794	finally
    //   4904	4911	11794	finally
    //   4918	4926	11794	finally
    //   4938	4948	11794	finally
    //   4955	4964	11794	finally
    //   4971	4976	11794	finally
    //   4983	4988	11794	finally
    //   4995	5004	11794	finally
    //   5011	5018	11794	finally
    //   5025	5034	11794	finally
    //   5041	5046	11794	finally
    //   5053	5058	11794	finally
    //   5075	5087	11794	finally
    //   5094	5099	11794	finally
    //   5106	5110	11794	finally
    //   5117	5121	11794	finally
    //   5128	5136	11794	finally
    //   5143	5152	11794	finally
    //   5159	5167	11794	finally
    //   5183	5190	11794	finally
    //   5197	5203	11794	finally
    //   5221	5238	11794	finally
    //   5245	5249	11794	finally
    //   5256	5260	11794	finally
    //   5267	5275	11794	finally
    //   5282	5291	11794	finally
    //   5298	5306	11794	finally
    //   5317	5322	11794	finally
    //   5329	5333	11794	finally
    //   5340	5344	11794	finally
    //   5351	5359	11794	finally
    //   5366	5375	11794	finally
    //   5382	5390	11794	finally
    //   5400	5405	11794	finally
    //   5412	5416	11794	finally
    //   5423	5427	11794	finally
    //   5434	5442	11794	finally
    //   5449	5458	11794	finally
    //   5465	5473	11794	finally
    //   5491	5498	11794	finally
    //   5508	5522	11794	finally
    //   5529	5557	11794	finally
    //   5564	5568	11794	finally
    //   5575	5579	11794	finally
    //   5586	5594	11794	finally
    //   5601	5618	11794	finally
    //   5625	5633	11794	finally
    //   5640	5645	11794	finally
    //   5652	5666	11794	finally
    //   5673	5680	11794	finally
    //   5744	5750	11794	finally
    //   5757	5765	11794	finally
    //   5772	5779	11794	finally
    //   5786	5794	11794	finally
    //   5806	5816	11794	finally
    //   5823	5832	11794	finally
    //   5839	5844	11794	finally
    //   5851	5856	11794	finally
    //   5863	5872	11794	finally
    //   5879	5886	11794	finally
    //   5893	5902	11794	finally
    //   5909	5914	11794	finally
    //   5921	5926	11794	finally
    //   5943	5955	11794	finally
    //   5962	5967	11794	finally
    //   5974	5978	11794	finally
    //   5985	5989	11794	finally
    //   5996	6004	11794	finally
    //   6011	6020	11794	finally
    //   6027	6035	11794	finally
    //   6051	6058	11794	finally
    //   6065	6071	11794	finally
    //   6089	6106	11794	finally
    //   6113	6117	11794	finally
    //   6124	6128	11794	finally
    //   6135	6143	11794	finally
    //   6150	6159	11794	finally
    //   6166	6174	11794	finally
    //   6185	6190	11794	finally
    //   6197	6201	11794	finally
    //   6208	6212	11794	finally
    //   6219	6227	11794	finally
    //   6234	6243	11794	finally
    //   6250	6258	11794	finally
    //   6268	6273	11794	finally
    //   6280	6284	11794	finally
    //   6291	6295	11794	finally
    //   6302	6310	11794	finally
    //   6317	6326	11794	finally
    //   6333	6341	11794	finally
    //   6359	6366	11794	finally
    //   6376	6390	11794	finally
    //   6397	6425	11794	finally
    //   6432	6436	11794	finally
    //   6443	6447	11794	finally
    //   6454	6462	11794	finally
    //   6469	6486	11794	finally
    //   6493	6501	11794	finally
    //   6508	6513	11794	finally
    //   6520	6534	11794	finally
    //   6557	6563	11794	finally
    //   6570	6578	11794	finally
    //   6585	6592	11794	finally
    //   6599	6607	11794	finally
    //   6619	6629	11794	finally
    //   6636	6645	11794	finally
    //   6652	6657	11794	finally
    //   6664	6669	11794	finally
    //   6676	6685	11794	finally
    //   6692	6699	11794	finally
    //   6706	6715	11794	finally
    //   6722	6727	11794	finally
    //   6734	6739	11794	finally
    //   6756	6768	11794	finally
    //   6775	6780	11794	finally
    //   6787	6791	11794	finally
    //   6798	6802	11794	finally
    //   6809	6817	11794	finally
    //   6824	6833	11794	finally
    //   6840	6848	11794	finally
    //   6864	6871	11794	finally
    //   6878	6884	11794	finally
    //   6902	6919	11794	finally
    //   6926	6930	11794	finally
    //   6937	6941	11794	finally
    //   6948	6956	11794	finally
    //   6963	6972	11794	finally
    //   6979	6987	11794	finally
    //   6998	7003	11794	finally
    //   7010	7014	11794	finally
    //   7021	7025	11794	finally
    //   7032	7040	11794	finally
    //   7047	7056	11794	finally
    //   7063	7071	11794	finally
    //   7081	7086	11794	finally
    //   7093	7097	11794	finally
    //   7104	7108	11794	finally
    //   7115	7123	11794	finally
    //   7130	7139	11794	finally
    //   7146	7154	11794	finally
    //   7172	7179	11794	finally
    //   7189	7203	11794	finally
    //   7210	7238	11794	finally
    //   7245	7249	11794	finally
    //   7256	7260	11794	finally
    //   7267	7275	11794	finally
    //   7282	7299	11794	finally
    //   7306	7314	11794	finally
    //   7321	7326	11794	finally
    //   7333	7347	11794	finally
    //   7504	7509	11794	finally
    //   7516	7524	11794	finally
    //   7531	7538	11794	finally
    //   7545	7553	11794	finally
    //   7564	7573	11794	finally
    //   7580	7589	11794	finally
    //   7596	7601	11794	finally
    //   7608	7613	11794	finally
    //   7620	7629	11794	finally
    //   7636	7644	11794	finally
    //   7651	7660	11794	finally
    //   7667	7672	11794	finally
    //   7679	7684	11794	finally
    //   7701	7714	11794	finally
    //   7721	7726	11794	finally
    //   7733	7738	11794	finally
    //   7745	7750	11794	finally
    //   7757	7766	11794	finally
    //   7773	7783	11794	finally
    //   7790	7799	11794	finally
    //   7815	7822	11794	finally
    //   7829	7835	11794	finally
    //   7852	7868	11794	finally
    //   7875	7880	11794	finally
    //   7887	7892	11794	finally
    //   7899	7908	11794	finally
    //   7915	7925	11794	finally
    //   7932	7941	11794	finally
    //   7953	7958	11794	finally
    //   7965	7970	11794	finally
    //   7977	7982	11794	finally
    //   7989	7998	11794	finally
    //   8005	8015	11794	finally
    //   8022	8031	11794	finally
    //   8041	8046	11794	finally
    //   8053	8058	11794	finally
    //   8065	8070	11794	finally
    //   8077	8086	11794	finally
    //   8093	8103	11794	finally
    //   8110	8119	11794	finally
    //   8130	8137	11794	finally
    //   8144	8157	11794	finally
    //   8164	8192	11794	finally
    //   8199	8204	11794	finally
    //   8211	8216	11794	finally
    //   8223	8232	11794	finally
    //   8239	8256	11794	finally
    //   8263	8272	11794	finally
    //   8279	8298	11794	finally
    //   8312	8318	11794	finally
    //   8325	8333	11794	finally
    //   8340	8347	11794	finally
    //   8354	8362	11794	finally
    //   8374	8384	11794	finally
    //   8391	8401	11794	finally
    //   8408	8413	11794	finally
    //   8420	8425	11794	finally
    //   8432	8441	11794	finally
    //   8448	8456	11794	finally
    //   8463	8472	11794	finally
    //   8479	8484	11794	finally
    //   8491	8495	11794	finally
    //   8511	8524	11794	finally
    //   8531	8536	11794	finally
    //   8543	8548	11794	finally
    //   8555	8560	11794	finally
    //   8567	8576	11794	finally
    //   8583	8593	11794	finally
    //   8600	8609	11794	finally
    //   8625	8632	11794	finally
    //   8639	8645	11794	finally
    //   8663	8680	11794	finally
    //   8687	8692	11794	finally
    //   8699	8704	11794	finally
    //   8711	8720	11794	finally
    //   8727	8737	11794	finally
    //   8744	8753	11794	finally
    //   8765	8770	11794	finally
    //   8777	8782	11794	finally
    //   8789	8794	11794	finally
    //   8801	8810	11794	finally
    //   8817	8827	11794	finally
    //   8834	8843	11794	finally
    //   8853	8858	11794	finally
    //   8865	8870	11794	finally
    //   8877	8882	11794	finally
    //   8889	8898	11794	finally
    //   8905	8915	11794	finally
    //   8922	8931	11794	finally
    //   8943	8950	11794	finally
    //   8957	8971	11794	finally
    //   8978	9006	11794	finally
    //   9013	9018	11794	finally
    //   9025	9030	11794	finally
    //   9037	9046	11794	finally
    //   9053	9071	11794	finally
    //   9078	9087	11794	finally
    //   9094	9114	11794	finally
    //   9121	9123	11794	finally
    //   9138	9146	11794	finally
    //   9153	9162	11794	finally
    //   9169	9174	11794	finally
    //   9181	9186	11794	finally
    //   9193	9202	11794	finally
    //   9209	9220	11794	finally
    //   9227	9236	11794	finally
    //   9243	9250	11794	finally
    //   9257	9262	11794	finally
    //   9269	9274	11794	finally
    //   9281	9290	11794	finally
    //   9297	9307	11794	finally
    //   9314	9323	11794	finally
    //   9333	9339	11794	finally
    //   9357	9365	11794	finally
    //   9372	9381	11794	finally
    //   9388	9393	11794	finally
    //   9400	9405	11794	finally
    //   9412	9421	11794	finally
    //   9428	9439	11794	finally
    //   9446	9455	11794	finally
    //   9462	9469	11794	finally
    //   9476	9481	11794	finally
    //   9488	9493	11794	finally
    //   9500	9509	11794	finally
    //   9516	9526	11794	finally
    //   9533	9542	11794	finally
    //   9566	9573	11794	finally
    //   9583	9589	11794	finally
    //   9598	9603	11794	finally
    //   9609	9614	11794	finally
    //   9627	9636	11794	finally
    //   9642	9650	11794	finally
    //   9656	9661	11794	finally
    //   9667	9672	11794	finally
    //   9678	9687	11794	finally
    //   9693	9702	11794	finally
    //   9721	9729	11794	finally
    //   9735	9744	11794	finally
    //   9754	9763	11794	finally
    //   9769	9779	11794	finally
    //   9785	9789	11794	finally
    //   9795	9803	11794	finally
    //   9809	9826	11794	finally
    //   9839	9849	11794	finally
    //   9855	9860	11794	finally
    //   9866	9871	11794	finally
    //   9877	9886	11794	finally
    //   9892	9900	11794	finally
    //   9906	9915	11794	finally
    //   9934	9942	11794	finally
    //   9948	9957	11794	finally
    //   9963	9971	11794	finally
    //   9977	9986	11794	finally
    //   9992	10002	11794	finally
    //   10008	10012	11794	finally
    //   10018	10026	11794	finally
    //   10032	10049	11794	finally
    //   10062	10071	11794	finally
    //   10077	10086	11794	finally
    //   10106	10115	11794	finally
    //   10121	10130	11794	finally
    //   10143	10151	11794	finally
    //   10157	10162	11794	finally
    //   10168	10173	11794	finally
    //   10179	10188	11794	finally
    //   10194	10204	11794	finally
    //   10210	10219	11794	finally
    //   10225	10236	11794	finally
    //   10246	10253	11794	finally
    //   10263	10273	11794	finally
    //   10279	10286	11794	finally
    //   10295	10305	11794	finally
    //   10316	10320	11794	finally
    //   10326	10334	11794	finally
    //   10340	10360	11794	finally
    //   10373	10377	11794	finally
    //   10383	10391	11794	finally
    //   10397	10414	11794	finally
    //   10436	10444	11794	finally
    //   10451	10460	11794	finally
    //   10467	10474	11794	finally
    //   10484	10497	11794	finally
    //   10504	10517	11794	finally
    //   10524	10528	11794	finally
    //   10535	10539	11794	finally
    //   10546	10554	11794	finally
    //   10561	10573	11794	finally
    //   10580	10588	11794	finally
    //   10604	10612	11794	finally
    //   10618	10625	11794	finally
    //   10631	10636	11794	finally
    //   10642	10647	11794	finally
    //   10653	10662	11794	finally
    //   10668	10678	11794	finally
    //   10684	10693	11794	finally
    //   10706	10717	11794	finally
    //   10723	10730	11794	finally
    //   10736	10748	11794	finally
    //   10754	10766	11794	finally
    //   10772	10776	11794	finally
    //   10782	10786	11794	finally
    //   10792	10800	11794	finally
    //   10806	10815	11794	finally
    //   10821	10829	11794	finally
    //   10835	10840	11794	finally
    //   10846	10850	11794	finally
    //   10856	10860	11794	finally
    //   10866	10874	11794	finally
    //   10880	10889	11794	finally
    //   10895	10903	11794	finally
    //   10909	10916	11794	finally
    //   10922	10934	11794	finally
    //   10940	10944	11794	finally
    //   10950	10954	11794	finally
    //   10960	10968	11794	finally
    //   10974	10989	11794	finally
    //   10995	11014	11794	finally
    //   11020	11028	11794	finally
    //   11034	11039	11794	finally
    //   11045	11052	11794	finally
    //   11058	11066	11794	finally
    //   11076	11085	11794	finally
    //   11091	11100	11794	finally
    //   11106	11111	11794	finally
    //   11117	11122	11794	finally
    //   11138	11151	11794	finally
    //   11157	11162	11794	finally
    //   11168	11173	11794	finally
    //   11179	11184	11794	finally
    //   11190	11199	11794	finally
    //   11205	11215	11794	finally
    //   11221	11230	11794	finally
    //   11245	11251	11794	finally
    //   11262	11278	11794	finally
    //   11284	11289	11794	finally
    //   11295	11300	11794	finally
    //   11306	11315	11794	finally
    //   11321	11331	11794	finally
    //   11337	11346	11794	finally
    //   11357	11362	11794	finally
    //   11368	11373	11794	finally
    //   11379	11384	11794	finally
    //   11390	11399	11794	finally
    //   11405	11415	11794	finally
    //   11421	11430	11794	finally
    //   11436	11443	11794	finally
    //   11449	11457	11794	finally
    //   11463	11476	11794	finally
    //   11482	11510	11794	finally
    //   11516	11521	11794	finally
    //   11527	11532	11794	finally
    //   11538	11547	11794	finally
    //   11553	11570	11794	finally
    //   11576	11585	11794	finally
    //   11591	11610	11794	finally
    //   11616	11623	11794	finally
    //   11643	11651	11794	finally
    //   11657	11664	11794	finally
    //   11670	11678	11794	finally
    //   11684	11692	11794	finally
    //   11698	11705	11794	finally
    //   11715	11722	11794	finally
    //   11728	11745	11794	finally
    //   11751	11761	11794	finally
    //   11774	11778	11794	finally
    //   11803	11811	11794	finally
    //   86	92	11798	java/lang/RuntimeException
    //   338	343	11798	java/lang/RuntimeException
    //   356	364	11798	java/lang/RuntimeException
    //   386	395	11798	java/lang/RuntimeException
    //   402	412	11798	java/lang/RuntimeException
    //   419	427	11798	java/lang/RuntimeException
    //   442	455	11798	java/lang/RuntimeException
    //   462	472	11798	java/lang/RuntimeException
    //   487	499	11798	java/lang/RuntimeException
    //   510	528	11798	java/lang/RuntimeException
    //   535	539	11798	java/lang/RuntimeException
    //   549	556	11798	java/lang/RuntimeException
    //   566	570	11798	java/lang/RuntimeException
    //   577	581	11798	java/lang/RuntimeException
    //   588	596	11798	java/lang/RuntimeException
    //   603	613	11798	java/lang/RuntimeException
    //   620	628	11798	java/lang/RuntimeException
    //   646	654	11798	java/lang/RuntimeException
    //   661	670	11798	java/lang/RuntimeException
    //   677	684	11798	java/lang/RuntimeException
    //   699	712	11798	java/lang/RuntimeException
    //   722	727	11798	java/lang/RuntimeException
    //   734	739	11798	java/lang/RuntimeException
    //   746	755	11798	java/lang/RuntimeException
    //   762	772	11798	java/lang/RuntimeException
    //   779	788	11798	java/lang/RuntimeException
    //   806	815	11798	java/lang/RuntimeException
    //   822	831	11798	java/lang/RuntimeException
    //   838	846	11798	java/lang/RuntimeException
    //   860	865	11798	java/lang/RuntimeException
    //   872	877	11798	java/lang/RuntimeException
    //   884	893	11798	java/lang/RuntimeException
    //   900	910	11798	java/lang/RuntimeException
    //   917	926	11798	java/lang/RuntimeException
    //   933	941	11798	java/lang/RuntimeException
    //   948	952	11798	java/lang/RuntimeException
    //   959	963	11798	java/lang/RuntimeException
    //   970	978	11798	java/lang/RuntimeException
    //   985	997	11798	java/lang/RuntimeException
    //   1004	1012	11798	java/lang/RuntimeException
    //   1022	1026	11798	java/lang/RuntimeException
    //   1033	1037	11798	java/lang/RuntimeException
    //   1044	1052	11798	java/lang/RuntimeException
    //   1059	1069	11798	java/lang/RuntimeException
    //   1076	1084	11798	java/lang/RuntimeException
    //   1102	1110	11798	java/lang/RuntimeException
    //   1117	1126	11798	java/lang/RuntimeException
    //   1133	1140	11798	java/lang/RuntimeException
    //   1155	1159	11798	java/lang/RuntimeException
    //   1166	1170	11798	java/lang/RuntimeException
    //   1177	1185	11798	java/lang/RuntimeException
    //   1192	1202	11798	java/lang/RuntimeException
    //   1209	1217	11798	java/lang/RuntimeException
    //   1224	1233	11798	java/lang/RuntimeException
    //   1243	1248	11798	java/lang/RuntimeException
    //   1255	1260	11798	java/lang/RuntimeException
    //   1267	1276	11798	java/lang/RuntimeException
    //   1283	1293	11798	java/lang/RuntimeException
    //   1300	1309	11798	java/lang/RuntimeException
    //   1327	1335	11798	java/lang/RuntimeException
    //   1342	1351	11798	java/lang/RuntimeException
    //   1358	1365	11798	java/lang/RuntimeException
    //   1380	1384	11798	java/lang/RuntimeException
    //   1391	1395	11798	java/lang/RuntimeException
    //   1402	1410	11798	java/lang/RuntimeException
    //   1417	1427	11798	java/lang/RuntimeException
    //   1434	1442	11798	java/lang/RuntimeException
    //   1449	1458	11798	java/lang/RuntimeException
    //   1468	1473	11798	java/lang/RuntimeException
    //   1480	1485	11798	java/lang/RuntimeException
    //   1492	1501	11798	java/lang/RuntimeException
    //   1508	1518	11798	java/lang/RuntimeException
    //   1525	1534	11798	java/lang/RuntimeException
    //   1552	1560	11798	java/lang/RuntimeException
    //   1567	1576	11798	java/lang/RuntimeException
    //   1583	1590	11798	java/lang/RuntimeException
    //   1597	1602	11798	java/lang/RuntimeException
    //   1609	1614	11798	java/lang/RuntimeException
    //   1621	1630	11798	java/lang/RuntimeException
    //   1637	1647	11798	java/lang/RuntimeException
    //   1654	1663	11798	java/lang/RuntimeException
    //   1670	1675	11798	java/lang/RuntimeException
    //   1685	1694	11798	java/lang/RuntimeException
    //   1701	1706	11798	java/lang/RuntimeException
    //   1713	1730	11798	java/lang/RuntimeException
    //   1737	1741	11798	java/lang/RuntimeException
    //   1748	1752	11798	java/lang/RuntimeException
    //   1759	1767	11798	java/lang/RuntimeException
    //   1774	1783	11798	java/lang/RuntimeException
    //   1790	1798	11798	java/lang/RuntimeException
    //   1816	1824	11798	java/lang/RuntimeException
    //   1831	1840	11798	java/lang/RuntimeException
    //   1847	1854	11798	java/lang/RuntimeException
    //   1861	1866	11798	java/lang/RuntimeException
    //   1873	1878	11798	java/lang/RuntimeException
    //   1885	1894	11798	java/lang/RuntimeException
    //   1901	1911	11798	java/lang/RuntimeException
    //   1918	1927	11798	java/lang/RuntimeException
    //   1934	1939	11798	java/lang/RuntimeException
    //   1949	1958	11798	java/lang/RuntimeException
    //   1965	1970	11798	java/lang/RuntimeException
    //   1977	1994	11798	java/lang/RuntimeException
    //   2001	2005	11798	java/lang/RuntimeException
    //   2012	2016	11798	java/lang/RuntimeException
    //   2023	2031	11798	java/lang/RuntimeException
    //   2038	2047	11798	java/lang/RuntimeException
    //   2054	2062	11798	java/lang/RuntimeException
    //   2080	2089	11798	java/lang/RuntimeException
    //   2096	2104	11798	java/lang/RuntimeException
    //   2111	2115	11798	java/lang/RuntimeException
    //   2122	2126	11798	java/lang/RuntimeException
    //   2133	2141	11798	java/lang/RuntimeException
    //   2148	2158	11798	java/lang/RuntimeException
    //   2165	2173	11798	java/lang/RuntimeException
    //   2183	2193	11798	java/lang/RuntimeException
    //   2200	2204	11798	java/lang/RuntimeException
    //   2211	2215	11798	java/lang/RuntimeException
    //   2222	2230	11798	java/lang/RuntimeException
    //   2237	2247	11798	java/lang/RuntimeException
    //   2254	2262	11798	java/lang/RuntimeException
    //   2269	2275	11798	java/lang/RuntimeException
    //   2293	2302	11798	java/lang/RuntimeException
    //   2309	2318	11798	java/lang/RuntimeException
    //   2325	2333	11798	java/lang/RuntimeException
    //   2340	2345	11798	java/lang/RuntimeException
    //   2355	2360	11798	java/lang/RuntimeException
    //   2367	2372	11798	java/lang/RuntimeException
    //   2379	2388	11798	java/lang/RuntimeException
    //   2395	2405	11798	java/lang/RuntimeException
    //   2412	2421	11798	java/lang/RuntimeException
    //   2428	2433	11798	java/lang/RuntimeException
    //   2451	2459	11798	java/lang/RuntimeException
    //   2466	2473	11798	java/lang/RuntimeException
    //   2483	2494	11798	java/lang/RuntimeException
    //   2501	2505	11798	java/lang/RuntimeException
    //   2512	2516	11798	java/lang/RuntimeException
    //   2523	2531	11798	java/lang/RuntimeException
    //   2538	2550	11798	java/lang/RuntimeException
    //   2557	2565	11798	java/lang/RuntimeException
    //   2581	2590	11798	java/lang/RuntimeException
    //   2596	2604	11798	java/lang/RuntimeException
    //   2610	2618	11798	java/lang/RuntimeException
    //   2624	2632	11798	java/lang/RuntimeException
    //   2641	2645	11798	java/lang/RuntimeException
    //   2651	2655	11798	java/lang/RuntimeException
    //   2661	2669	11798	java/lang/RuntimeException
    //   2675	2685	11798	java/lang/RuntimeException
    //   2691	2699	11798	java/lang/RuntimeException
    //   2709	2717	11798	java/lang/RuntimeException
    //   2723	2730	11798	java/lang/RuntimeException
    //   2736	2755	11798	java/lang/RuntimeException
    //   2761	2772	11798	java/lang/RuntimeException
    //   2794	2803	11798	java/lang/RuntimeException
    //   2810	2819	11798	java/lang/RuntimeException
    //   2826	2834	11798	java/lang/RuntimeException
    //   2841	2846	11798	java/lang/RuntimeException
    //   2856	2861	11798	java/lang/RuntimeException
    //   2868	2873	11798	java/lang/RuntimeException
    //   2880	2889	11798	java/lang/RuntimeException
    //   2896	2906	11798	java/lang/RuntimeException
    //   2913	2922	11798	java/lang/RuntimeException
    //   2929	2934	11798	java/lang/RuntimeException
    //   2952	2961	11798	java/lang/RuntimeException
    //   2968	2976	11798	java/lang/RuntimeException
    //   2983	2995	11798	java/lang/RuntimeException
    //   3005	3009	11798	java/lang/RuntimeException
    //   3016	3020	11798	java/lang/RuntimeException
    //   3027	3035	11798	java/lang/RuntimeException
    //   3042	3052	11798	java/lang/RuntimeException
    //   3059	3067	11798	java/lang/RuntimeException
    //   3085	3094	11798	java/lang/RuntimeException
    //   3101	3110	11798	java/lang/RuntimeException
    //   3117	3125	11798	java/lang/RuntimeException
    //   3139	3144	11798	java/lang/RuntimeException
    //   3151	3156	11798	java/lang/RuntimeException
    //   3163	3172	11798	java/lang/RuntimeException
    //   3179	3186	11798	java/lang/RuntimeException
    //   3193	3202	11798	java/lang/RuntimeException
    //   3209	3229	11798	java/lang/RuntimeException
    //   3239	3244	11798	java/lang/RuntimeException
    //   3251	3256	11798	java/lang/RuntimeException
    //   3263	3272	11798	java/lang/RuntimeException
    //   3279	3290	11798	java/lang/RuntimeException
    //   3297	3306	11798	java/lang/RuntimeException
    //   3313	3320	11798	java/lang/RuntimeException
    //   3327	3336	11798	java/lang/RuntimeException
    //   3352	3361	11798	java/lang/RuntimeException
    //   3367	3371	11798	java/lang/RuntimeException
    //   3377	3381	11798	java/lang/RuntimeException
    //   3387	3395	11798	java/lang/RuntimeException
    //   3401	3411	11798	java/lang/RuntimeException
    //   3417	3425	11798	java/lang/RuntimeException
    //   3431	3439	11798	java/lang/RuntimeException
    //   3445	3453	11798	java/lang/RuntimeException
    //   3459	3467	11798	java/lang/RuntimeException
    //   3473	3480	11798	java/lang/RuntimeException
    //   3486	3491	11798	java/lang/RuntimeException
    //   3497	3510	11798	java/lang/RuntimeException
    //   3516	3526	11798	java/lang/RuntimeException
    //   3535	3543	11798	java/lang/RuntimeException
    //   3552	3560	11798	java/lang/RuntimeException
    //   3566	3581	11798	java/lang/RuntimeException
    //   3587	3598	11798	java/lang/RuntimeException
    //   3604	3611	11798	java/lang/RuntimeException
    //   3619	3624	11798	java/lang/RuntimeException
    //   3630	3639	11798	java/lang/RuntimeException
    //   3645	3667	11798	java/lang/RuntimeException
    //   3684	3692	11798	java/lang/RuntimeException
    //   3698	3712	11798	java/lang/RuntimeException
    //   3718	3726	11798	java/lang/RuntimeException
    //   3736	3749	11798	java/lang/RuntimeException
    //   3755	3768	11798	java/lang/RuntimeException
    //   3774	3778	11798	java/lang/RuntimeException
    //   3784	3791	11798	java/lang/RuntimeException
    //   3797	3803	11798	java/lang/RuntimeException
    //   3812	3831	11798	java/lang/RuntimeException
    //   3837	3848	11798	java/lang/RuntimeException
    //   3861	3866	11798	java/lang/RuntimeException
    //   3872	3877	11798	java/lang/RuntimeException
    //   3883	3892	11798	java/lang/RuntimeException
    //   3898	3908	11798	java/lang/RuntimeException
    //   3914	3926	11798	java/lang/RuntimeException
    //   3948	3956	11798	java/lang/RuntimeException
    //   3963	3970	11798	java/lang/RuntimeException
    //   3980	3991	11798	java/lang/RuntimeException
    //   4009	4018	11798	java/lang/RuntimeException
    //   4025	4034	11798	java/lang/RuntimeException
    //   4041	4049	11798	java/lang/RuntimeException
    //   4059	4064	11798	java/lang/RuntimeException
    //   4071	4077	11798	java/lang/RuntimeException
    //   4084	4090	11798	java/lang/RuntimeException
    //   4097	4101	11798	java/lang/RuntimeException
    //   4108	4112	11798	java/lang/RuntimeException
    //   4119	4127	11798	java/lang/RuntimeException
    //   4134	4143	11798	java/lang/RuntimeException
    //   4150	4158	11798	java/lang/RuntimeException
    //   4174	4183	11798	java/lang/RuntimeException
    //   4189	4198	11798	java/lang/RuntimeException
    //   4204	4212	11798	java/lang/RuntimeException
    //   4225	4230	11798	java/lang/RuntimeException
    //   4236	4245	11798	java/lang/RuntimeException
    //   4255	4263	11798	java/lang/RuntimeException
    //   4269	4279	11798	java/lang/RuntimeException
    //   4285	4304	11798	java/lang/RuntimeException
    //   4310	4314	11798	java/lang/RuntimeException
    //   4320	4324	11798	java/lang/RuntimeException
    //   4330	4338	11798	java/lang/RuntimeException
    //   4344	4353	11798	java/lang/RuntimeException
    //   4359	4367	11798	java/lang/RuntimeException
    //   4380	4386	11798	java/lang/RuntimeException
    //   4395	4403	11798	java/lang/RuntimeException
    //   4413	4420	11798	java/lang/RuntimeException
    //   4426	4437	11798	java/lang/RuntimeException
    //   4459	4468	11798	java/lang/RuntimeException
    //   4475	4484	11798	java/lang/RuntimeException
    //   4491	4499	11798	java/lang/RuntimeException
    //   4509	4514	11798	java/lang/RuntimeException
    //   4526	4538	11798	java/lang/RuntimeException
    //   4556	4564	11798	java/lang/RuntimeException
    //   4571	4578	11798	java/lang/RuntimeException
    //   4585	4592	11798	java/lang/RuntimeException
    //   4602	4610	11798	java/lang/RuntimeException
    //   4617	4625	11798	java/lang/RuntimeException
    //   4632	4640	11798	java/lang/RuntimeException
    //   4647	4651	11798	java/lang/RuntimeException
    //   4658	4662	11798	java/lang/RuntimeException
    //   4669	4677	11798	java/lang/RuntimeException
    //   4684	4698	11798	java/lang/RuntimeException
    //   4705	4713	11798	java/lang/RuntimeException
    //   4876	4882	11798	java/lang/RuntimeException
    //   4889	4897	11798	java/lang/RuntimeException
    //   4904	4911	11798	java/lang/RuntimeException
    //   4918	4926	11798	java/lang/RuntimeException
    //   4938	4948	11798	java/lang/RuntimeException
    //   4955	4964	11798	java/lang/RuntimeException
    //   4971	4976	11798	java/lang/RuntimeException
    //   4983	4988	11798	java/lang/RuntimeException
    //   4995	5004	11798	java/lang/RuntimeException
    //   5011	5018	11798	java/lang/RuntimeException
    //   5025	5034	11798	java/lang/RuntimeException
    //   5041	5046	11798	java/lang/RuntimeException
    //   5053	5058	11798	java/lang/RuntimeException
    //   5075	5087	11798	java/lang/RuntimeException
    //   5094	5099	11798	java/lang/RuntimeException
    //   5106	5110	11798	java/lang/RuntimeException
    //   5117	5121	11798	java/lang/RuntimeException
    //   5128	5136	11798	java/lang/RuntimeException
    //   5143	5152	11798	java/lang/RuntimeException
    //   5159	5167	11798	java/lang/RuntimeException
    //   5183	5190	11798	java/lang/RuntimeException
    //   5197	5203	11798	java/lang/RuntimeException
    //   5221	5238	11798	java/lang/RuntimeException
    //   5245	5249	11798	java/lang/RuntimeException
    //   5256	5260	11798	java/lang/RuntimeException
    //   5267	5275	11798	java/lang/RuntimeException
    //   5282	5291	11798	java/lang/RuntimeException
    //   5298	5306	11798	java/lang/RuntimeException
    //   5317	5322	11798	java/lang/RuntimeException
    //   5329	5333	11798	java/lang/RuntimeException
    //   5340	5344	11798	java/lang/RuntimeException
    //   5351	5359	11798	java/lang/RuntimeException
    //   5366	5375	11798	java/lang/RuntimeException
    //   5382	5390	11798	java/lang/RuntimeException
    //   5400	5405	11798	java/lang/RuntimeException
    //   5412	5416	11798	java/lang/RuntimeException
    //   5423	5427	11798	java/lang/RuntimeException
    //   5434	5442	11798	java/lang/RuntimeException
    //   5449	5458	11798	java/lang/RuntimeException
    //   5465	5473	11798	java/lang/RuntimeException
    //   5491	5498	11798	java/lang/RuntimeException
    //   5508	5522	11798	java/lang/RuntimeException
    //   5529	5557	11798	java/lang/RuntimeException
    //   5564	5568	11798	java/lang/RuntimeException
    //   5575	5579	11798	java/lang/RuntimeException
    //   5586	5594	11798	java/lang/RuntimeException
    //   5601	5618	11798	java/lang/RuntimeException
    //   5625	5633	11798	java/lang/RuntimeException
    //   5640	5645	11798	java/lang/RuntimeException
    //   5652	5666	11798	java/lang/RuntimeException
    //   5673	5680	11798	java/lang/RuntimeException
    //   5744	5750	11798	java/lang/RuntimeException
    //   5757	5765	11798	java/lang/RuntimeException
    //   5772	5779	11798	java/lang/RuntimeException
    //   5786	5794	11798	java/lang/RuntimeException
    //   5806	5816	11798	java/lang/RuntimeException
    //   5823	5832	11798	java/lang/RuntimeException
    //   5839	5844	11798	java/lang/RuntimeException
    //   5851	5856	11798	java/lang/RuntimeException
    //   5863	5872	11798	java/lang/RuntimeException
    //   5879	5886	11798	java/lang/RuntimeException
    //   5893	5902	11798	java/lang/RuntimeException
    //   5909	5914	11798	java/lang/RuntimeException
    //   5921	5926	11798	java/lang/RuntimeException
    //   5943	5955	11798	java/lang/RuntimeException
    //   5962	5967	11798	java/lang/RuntimeException
    //   5974	5978	11798	java/lang/RuntimeException
    //   5985	5989	11798	java/lang/RuntimeException
    //   5996	6004	11798	java/lang/RuntimeException
    //   6011	6020	11798	java/lang/RuntimeException
    //   6027	6035	11798	java/lang/RuntimeException
    //   6051	6058	11798	java/lang/RuntimeException
    //   6065	6071	11798	java/lang/RuntimeException
    //   6089	6106	11798	java/lang/RuntimeException
    //   6113	6117	11798	java/lang/RuntimeException
    //   6124	6128	11798	java/lang/RuntimeException
    //   6135	6143	11798	java/lang/RuntimeException
    //   6150	6159	11798	java/lang/RuntimeException
    //   6166	6174	11798	java/lang/RuntimeException
    //   6185	6190	11798	java/lang/RuntimeException
    //   6197	6201	11798	java/lang/RuntimeException
    //   6208	6212	11798	java/lang/RuntimeException
    //   6219	6227	11798	java/lang/RuntimeException
    //   6234	6243	11798	java/lang/RuntimeException
    //   6250	6258	11798	java/lang/RuntimeException
    //   6268	6273	11798	java/lang/RuntimeException
    //   6280	6284	11798	java/lang/RuntimeException
    //   6291	6295	11798	java/lang/RuntimeException
    //   6302	6310	11798	java/lang/RuntimeException
    //   6317	6326	11798	java/lang/RuntimeException
    //   6333	6341	11798	java/lang/RuntimeException
    //   6359	6366	11798	java/lang/RuntimeException
    //   6376	6390	11798	java/lang/RuntimeException
    //   6397	6425	11798	java/lang/RuntimeException
    //   6432	6436	11798	java/lang/RuntimeException
    //   6443	6447	11798	java/lang/RuntimeException
    //   6454	6462	11798	java/lang/RuntimeException
    //   6469	6486	11798	java/lang/RuntimeException
    //   6493	6501	11798	java/lang/RuntimeException
    //   6508	6513	11798	java/lang/RuntimeException
    //   6520	6534	11798	java/lang/RuntimeException
    //   6557	6563	11798	java/lang/RuntimeException
    //   6570	6578	11798	java/lang/RuntimeException
    //   6585	6592	11798	java/lang/RuntimeException
    //   6599	6607	11798	java/lang/RuntimeException
    //   6619	6629	11798	java/lang/RuntimeException
    //   6636	6645	11798	java/lang/RuntimeException
    //   6652	6657	11798	java/lang/RuntimeException
    //   6664	6669	11798	java/lang/RuntimeException
    //   6676	6685	11798	java/lang/RuntimeException
    //   6692	6699	11798	java/lang/RuntimeException
    //   6706	6715	11798	java/lang/RuntimeException
    //   6722	6727	11798	java/lang/RuntimeException
    //   6734	6739	11798	java/lang/RuntimeException
    //   6756	6768	11798	java/lang/RuntimeException
    //   6775	6780	11798	java/lang/RuntimeException
    //   6787	6791	11798	java/lang/RuntimeException
    //   6798	6802	11798	java/lang/RuntimeException
    //   6809	6817	11798	java/lang/RuntimeException
    //   6824	6833	11798	java/lang/RuntimeException
    //   6840	6848	11798	java/lang/RuntimeException
    //   6864	6871	11798	java/lang/RuntimeException
    //   6878	6884	11798	java/lang/RuntimeException
    //   6902	6919	11798	java/lang/RuntimeException
    //   6926	6930	11798	java/lang/RuntimeException
    //   6937	6941	11798	java/lang/RuntimeException
    //   6948	6956	11798	java/lang/RuntimeException
    //   6963	6972	11798	java/lang/RuntimeException
    //   6979	6987	11798	java/lang/RuntimeException
    //   6998	7003	11798	java/lang/RuntimeException
    //   7010	7014	11798	java/lang/RuntimeException
    //   7021	7025	11798	java/lang/RuntimeException
    //   7032	7040	11798	java/lang/RuntimeException
    //   7047	7056	11798	java/lang/RuntimeException
    //   7063	7071	11798	java/lang/RuntimeException
    //   7081	7086	11798	java/lang/RuntimeException
    //   7093	7097	11798	java/lang/RuntimeException
    //   7104	7108	11798	java/lang/RuntimeException
    //   7115	7123	11798	java/lang/RuntimeException
    //   7130	7139	11798	java/lang/RuntimeException
    //   7146	7154	11798	java/lang/RuntimeException
    //   7172	7179	11798	java/lang/RuntimeException
    //   7189	7203	11798	java/lang/RuntimeException
    //   7210	7238	11798	java/lang/RuntimeException
    //   7245	7249	11798	java/lang/RuntimeException
    //   7256	7260	11798	java/lang/RuntimeException
    //   7267	7275	11798	java/lang/RuntimeException
    //   7282	7299	11798	java/lang/RuntimeException
    //   7306	7314	11798	java/lang/RuntimeException
    //   7321	7326	11798	java/lang/RuntimeException
    //   7333	7347	11798	java/lang/RuntimeException
    //   7504	7509	11798	java/lang/RuntimeException
    //   7516	7524	11798	java/lang/RuntimeException
    //   7531	7538	11798	java/lang/RuntimeException
    //   7545	7553	11798	java/lang/RuntimeException
    //   7564	7573	11798	java/lang/RuntimeException
    //   7580	7589	11798	java/lang/RuntimeException
    //   7596	7601	11798	java/lang/RuntimeException
    //   7608	7613	11798	java/lang/RuntimeException
    //   7620	7629	11798	java/lang/RuntimeException
    //   7636	7644	11798	java/lang/RuntimeException
    //   7651	7660	11798	java/lang/RuntimeException
    //   7667	7672	11798	java/lang/RuntimeException
    //   7679	7684	11798	java/lang/RuntimeException
    //   7701	7714	11798	java/lang/RuntimeException
    //   7721	7726	11798	java/lang/RuntimeException
    //   7733	7738	11798	java/lang/RuntimeException
    //   7745	7750	11798	java/lang/RuntimeException
    //   7757	7766	11798	java/lang/RuntimeException
    //   7773	7783	11798	java/lang/RuntimeException
    //   7790	7799	11798	java/lang/RuntimeException
    //   7815	7822	11798	java/lang/RuntimeException
    //   7829	7835	11798	java/lang/RuntimeException
    //   7852	7868	11798	java/lang/RuntimeException
    //   7875	7880	11798	java/lang/RuntimeException
    //   7887	7892	11798	java/lang/RuntimeException
    //   7899	7908	11798	java/lang/RuntimeException
    //   7915	7925	11798	java/lang/RuntimeException
    //   7932	7941	11798	java/lang/RuntimeException
    //   7953	7958	11798	java/lang/RuntimeException
    //   7965	7970	11798	java/lang/RuntimeException
    //   7977	7982	11798	java/lang/RuntimeException
    //   7989	7998	11798	java/lang/RuntimeException
    //   8005	8015	11798	java/lang/RuntimeException
    //   8022	8031	11798	java/lang/RuntimeException
    //   8041	8046	11798	java/lang/RuntimeException
    //   8053	8058	11798	java/lang/RuntimeException
    //   8065	8070	11798	java/lang/RuntimeException
    //   8077	8086	11798	java/lang/RuntimeException
    //   8093	8103	11798	java/lang/RuntimeException
    //   8110	8119	11798	java/lang/RuntimeException
    //   8130	8137	11798	java/lang/RuntimeException
    //   8144	8157	11798	java/lang/RuntimeException
    //   8164	8192	11798	java/lang/RuntimeException
    //   8199	8204	11798	java/lang/RuntimeException
    //   8211	8216	11798	java/lang/RuntimeException
    //   8223	8232	11798	java/lang/RuntimeException
    //   8239	8256	11798	java/lang/RuntimeException
    //   8263	8272	11798	java/lang/RuntimeException
    //   8279	8298	11798	java/lang/RuntimeException
    //   8312	8318	11798	java/lang/RuntimeException
    //   8325	8333	11798	java/lang/RuntimeException
    //   8340	8347	11798	java/lang/RuntimeException
    //   8354	8362	11798	java/lang/RuntimeException
    //   8374	8384	11798	java/lang/RuntimeException
    //   8391	8401	11798	java/lang/RuntimeException
    //   8408	8413	11798	java/lang/RuntimeException
    //   8420	8425	11798	java/lang/RuntimeException
    //   8432	8441	11798	java/lang/RuntimeException
    //   8448	8456	11798	java/lang/RuntimeException
    //   8463	8472	11798	java/lang/RuntimeException
    //   8479	8484	11798	java/lang/RuntimeException
    //   8491	8495	11798	java/lang/RuntimeException
    //   8511	8524	11798	java/lang/RuntimeException
    //   8531	8536	11798	java/lang/RuntimeException
    //   8543	8548	11798	java/lang/RuntimeException
    //   8555	8560	11798	java/lang/RuntimeException
    //   8567	8576	11798	java/lang/RuntimeException
    //   8583	8593	11798	java/lang/RuntimeException
    //   8600	8609	11798	java/lang/RuntimeException
    //   8625	8632	11798	java/lang/RuntimeException
    //   8639	8645	11798	java/lang/RuntimeException
    //   8663	8680	11798	java/lang/RuntimeException
    //   8687	8692	11798	java/lang/RuntimeException
    //   8699	8704	11798	java/lang/RuntimeException
    //   8711	8720	11798	java/lang/RuntimeException
    //   8727	8737	11798	java/lang/RuntimeException
    //   8744	8753	11798	java/lang/RuntimeException
    //   8765	8770	11798	java/lang/RuntimeException
    //   8777	8782	11798	java/lang/RuntimeException
    //   8789	8794	11798	java/lang/RuntimeException
    //   8801	8810	11798	java/lang/RuntimeException
    //   8817	8827	11798	java/lang/RuntimeException
    //   8834	8843	11798	java/lang/RuntimeException
    //   8853	8858	11798	java/lang/RuntimeException
    //   8865	8870	11798	java/lang/RuntimeException
    //   8877	8882	11798	java/lang/RuntimeException
    //   8889	8898	11798	java/lang/RuntimeException
    //   8905	8915	11798	java/lang/RuntimeException
    //   8922	8931	11798	java/lang/RuntimeException
    //   8943	8950	11798	java/lang/RuntimeException
    //   8957	8971	11798	java/lang/RuntimeException
    //   8978	9006	11798	java/lang/RuntimeException
    //   9013	9018	11798	java/lang/RuntimeException
    //   9025	9030	11798	java/lang/RuntimeException
    //   9037	9046	11798	java/lang/RuntimeException
    //   9053	9071	11798	java/lang/RuntimeException
    //   9078	9087	11798	java/lang/RuntimeException
    //   9094	9114	11798	java/lang/RuntimeException
    //   9121	9123	11798	java/lang/RuntimeException
    //   9138	9146	11798	java/lang/RuntimeException
    //   9153	9162	11798	java/lang/RuntimeException
    //   9169	9174	11798	java/lang/RuntimeException
    //   9181	9186	11798	java/lang/RuntimeException
    //   9193	9202	11798	java/lang/RuntimeException
    //   9209	9220	11798	java/lang/RuntimeException
    //   9227	9236	11798	java/lang/RuntimeException
    //   9243	9250	11798	java/lang/RuntimeException
    //   9257	9262	11798	java/lang/RuntimeException
    //   9269	9274	11798	java/lang/RuntimeException
    //   9281	9290	11798	java/lang/RuntimeException
    //   9297	9307	11798	java/lang/RuntimeException
    //   9314	9323	11798	java/lang/RuntimeException
    //   9333	9339	11798	java/lang/RuntimeException
    //   9357	9365	11798	java/lang/RuntimeException
    //   9372	9381	11798	java/lang/RuntimeException
    //   9388	9393	11798	java/lang/RuntimeException
    //   9400	9405	11798	java/lang/RuntimeException
    //   9412	9421	11798	java/lang/RuntimeException
    //   9428	9439	11798	java/lang/RuntimeException
    //   9446	9455	11798	java/lang/RuntimeException
    //   9462	9469	11798	java/lang/RuntimeException
    //   9476	9481	11798	java/lang/RuntimeException
    //   9488	9493	11798	java/lang/RuntimeException
    //   9500	9509	11798	java/lang/RuntimeException
    //   9516	9526	11798	java/lang/RuntimeException
    //   9533	9542	11798	java/lang/RuntimeException
    //   9566	9573	11798	java/lang/RuntimeException
    //   9583	9589	11798	java/lang/RuntimeException
    //   9598	9603	11798	java/lang/RuntimeException
    //   9609	9614	11798	java/lang/RuntimeException
    //   9627	9636	11798	java/lang/RuntimeException
    //   9642	9650	11798	java/lang/RuntimeException
    //   9656	9661	11798	java/lang/RuntimeException
    //   9667	9672	11798	java/lang/RuntimeException
    //   9678	9687	11798	java/lang/RuntimeException
    //   9693	9702	11798	java/lang/RuntimeException
    //   9721	9729	11798	java/lang/RuntimeException
    //   9735	9744	11798	java/lang/RuntimeException
    //   9754	9763	11798	java/lang/RuntimeException
    //   9769	9779	11798	java/lang/RuntimeException
    //   9785	9789	11798	java/lang/RuntimeException
    //   9795	9803	11798	java/lang/RuntimeException
    //   9809	9826	11798	java/lang/RuntimeException
    //   9839	9849	11798	java/lang/RuntimeException
    //   9855	9860	11798	java/lang/RuntimeException
    //   9866	9871	11798	java/lang/RuntimeException
    //   9877	9886	11798	java/lang/RuntimeException
    //   9892	9900	11798	java/lang/RuntimeException
    //   9906	9915	11798	java/lang/RuntimeException
    //   9934	9942	11798	java/lang/RuntimeException
    //   9948	9957	11798	java/lang/RuntimeException
    //   9963	9971	11798	java/lang/RuntimeException
    //   9977	9986	11798	java/lang/RuntimeException
    //   9992	10002	11798	java/lang/RuntimeException
    //   10008	10012	11798	java/lang/RuntimeException
    //   10018	10026	11798	java/lang/RuntimeException
    //   10032	10049	11798	java/lang/RuntimeException
    //   10062	10071	11798	java/lang/RuntimeException
    //   10077	10086	11798	java/lang/RuntimeException
    //   10106	10115	11798	java/lang/RuntimeException
    //   10121	10130	11798	java/lang/RuntimeException
    //   10143	10151	11798	java/lang/RuntimeException
    //   10157	10162	11798	java/lang/RuntimeException
    //   10168	10173	11798	java/lang/RuntimeException
    //   10179	10188	11798	java/lang/RuntimeException
    //   10194	10204	11798	java/lang/RuntimeException
    //   10210	10219	11798	java/lang/RuntimeException
    //   10225	10236	11798	java/lang/RuntimeException
    //   10246	10253	11798	java/lang/RuntimeException
    //   10263	10273	11798	java/lang/RuntimeException
    //   10279	10286	11798	java/lang/RuntimeException
    //   10295	10305	11798	java/lang/RuntimeException
    //   10316	10320	11798	java/lang/RuntimeException
    //   10326	10334	11798	java/lang/RuntimeException
    //   10340	10360	11798	java/lang/RuntimeException
    //   10373	10377	11798	java/lang/RuntimeException
    //   10383	10391	11798	java/lang/RuntimeException
    //   10397	10414	11798	java/lang/RuntimeException
    //   10436	10444	11798	java/lang/RuntimeException
    //   10451	10460	11798	java/lang/RuntimeException
    //   10467	10474	11798	java/lang/RuntimeException
    //   10484	10497	11798	java/lang/RuntimeException
    //   10504	10517	11798	java/lang/RuntimeException
    //   10524	10528	11798	java/lang/RuntimeException
    //   10535	10539	11798	java/lang/RuntimeException
    //   10546	10554	11798	java/lang/RuntimeException
    //   10561	10573	11798	java/lang/RuntimeException
    //   10580	10588	11798	java/lang/RuntimeException
    //   10604	10612	11798	java/lang/RuntimeException
    //   10618	10625	11798	java/lang/RuntimeException
    //   10631	10636	11798	java/lang/RuntimeException
    //   10642	10647	11798	java/lang/RuntimeException
    //   10653	10662	11798	java/lang/RuntimeException
    //   10668	10678	11798	java/lang/RuntimeException
    //   10684	10693	11798	java/lang/RuntimeException
    //   10706	10717	11798	java/lang/RuntimeException
    //   10723	10730	11798	java/lang/RuntimeException
    //   10736	10748	11798	java/lang/RuntimeException
    //   10754	10766	11798	java/lang/RuntimeException
    //   10772	10776	11798	java/lang/RuntimeException
    //   10782	10786	11798	java/lang/RuntimeException
    //   10792	10800	11798	java/lang/RuntimeException
    //   10806	10815	11798	java/lang/RuntimeException
    //   10821	10829	11798	java/lang/RuntimeException
    //   10835	10840	11798	java/lang/RuntimeException
    //   10846	10850	11798	java/lang/RuntimeException
    //   10856	10860	11798	java/lang/RuntimeException
    //   10866	10874	11798	java/lang/RuntimeException
    //   10880	10889	11798	java/lang/RuntimeException
    //   10895	10903	11798	java/lang/RuntimeException
    //   10909	10916	11798	java/lang/RuntimeException
    //   10922	10934	11798	java/lang/RuntimeException
    //   10940	10944	11798	java/lang/RuntimeException
    //   10950	10954	11798	java/lang/RuntimeException
    //   10960	10968	11798	java/lang/RuntimeException
    //   10974	10989	11798	java/lang/RuntimeException
    //   10995	11014	11798	java/lang/RuntimeException
    //   11020	11028	11798	java/lang/RuntimeException
    //   11034	11039	11798	java/lang/RuntimeException
    //   11045	11052	11798	java/lang/RuntimeException
    //   11058	11066	11798	java/lang/RuntimeException
    //   11076	11085	11798	java/lang/RuntimeException
    //   11091	11100	11798	java/lang/RuntimeException
    //   11106	11111	11798	java/lang/RuntimeException
    //   11117	11122	11798	java/lang/RuntimeException
    //   11138	11151	11798	java/lang/RuntimeException
    //   11157	11162	11798	java/lang/RuntimeException
    //   11168	11173	11798	java/lang/RuntimeException
    //   11179	11184	11798	java/lang/RuntimeException
    //   11190	11199	11798	java/lang/RuntimeException
    //   11205	11215	11798	java/lang/RuntimeException
    //   11221	11230	11798	java/lang/RuntimeException
    //   11245	11251	11798	java/lang/RuntimeException
    //   11262	11278	11798	java/lang/RuntimeException
    //   11284	11289	11798	java/lang/RuntimeException
    //   11295	11300	11798	java/lang/RuntimeException
    //   11306	11315	11798	java/lang/RuntimeException
    //   11321	11331	11798	java/lang/RuntimeException
    //   11337	11346	11798	java/lang/RuntimeException
    //   11357	11362	11798	java/lang/RuntimeException
    //   11368	11373	11798	java/lang/RuntimeException
    //   11379	11384	11798	java/lang/RuntimeException
    //   11390	11399	11798	java/lang/RuntimeException
    //   11405	11415	11798	java/lang/RuntimeException
    //   11421	11430	11798	java/lang/RuntimeException
    //   11436	11443	11798	java/lang/RuntimeException
    //   11449	11457	11798	java/lang/RuntimeException
    //   11463	11476	11798	java/lang/RuntimeException
    //   11482	11510	11798	java/lang/RuntimeException
    //   11516	11521	11798	java/lang/RuntimeException
    //   11527	11532	11798	java/lang/RuntimeException
    //   11538	11547	11798	java/lang/RuntimeException
    //   11553	11570	11798	java/lang/RuntimeException
    //   11576	11585	11798	java/lang/RuntimeException
    //   11591	11610	11798	java/lang/RuntimeException
    //   11616	11623	11798	java/lang/RuntimeException
    //   11643	11651	11798	java/lang/RuntimeException
    //   11657	11664	11798	java/lang/RuntimeException
    //   11670	11678	11798	java/lang/RuntimeException
    //   11684	11692	11798	java/lang/RuntimeException
    //   11698	11705	11798	java/lang/RuntimeException
    //   11715	11722	11798	java/lang/RuntimeException
    //   11728	11745	11798	java/lang/RuntimeException
    //   11751	11761	11798	java/lang/RuntimeException
    //   11774	11778	11798	java/lang/RuntimeException
  }
  
  public boolean isCspPlmnEnabled()
  {
    return mCspPlmnEnabled;
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[SIMRecords] ");
    localStringBuilder.append(paramString);
    Rlog.d("SIMRecords", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[SIMRecords] ");
    localStringBuilder.append(paramString);
    Rlog.e("SIMRecords", localStringBuilder.toString());
  }
  
  protected void logv(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[SIMRecords] ");
    localStringBuilder.append(paramString);
    Rlog.v("SIMRecords", localStringBuilder.toString());
  }
  
  protected void logw(String paramString, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[SIMRecords] ");
    localStringBuilder.append(paramString);
    Rlog.w("SIMRecords", localStringBuilder.toString(), paramThrowable);
  }
  
  protected void onAllRecordsLoaded()
  {
    log("record load complete");
    setSimLanguageFromEF();
    setVoiceCallForwardingFlagFromSimRecords();
    String str = getOperatorNumeric();
    if (!TextUtils.isEmpty(str))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("onAllRecordsLoaded set 'gsm.sim.operator.numeric' to operator='");
      ((StringBuilder)localObject1).append(str);
      ((StringBuilder)localObject1).append("'");
      log(((StringBuilder)localObject1).toString());
      mTelephonyManager.setSimOperatorNumericForPhone(mParentApp.getPhoneId(), str);
    }
    else
    {
      log("onAllRecordsLoaded empty 'gsm.sim.operator.numeric' skipping");
    }
    Object localObject2 = getIMSI();
    if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (((String)localObject2).length() >= 3))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("onAllRecordsLoaded set mcc imsi");
      ((StringBuilder)localObject1).append("");
      log(((StringBuilder)localObject1).toString());
      mTelephonyManager.setSimCountryIsoForPhone(mParentApp.getPhoneId(), MccTable.countryCodeForMcc(Integer.parseInt(((String)localObject2).substring(0, 3))));
    }
    else
    {
      log("onAllRecordsLoaded empty imsi skipping setting mcc");
    }
    setVoiceMailByCountry(str);
    Object localObject1 = getServiceProviderName();
    if (("46689".equals(str)) && (!TextUtils.isEmpty((CharSequence)localObject1)))
    {
      str = new String("T Star");
      setServiceProviderName(str);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("[APLMN] onAllRecordsLoaded oldSpn: ");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(", newSpn: ");
      ((StringBuilder)localObject2).append(str);
      log(((StringBuilder)localObject2).toString());
    }
    mLoaded.set(true);
    mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
  }
  
  public void onReady()
  {
    fetchSimRecords();
  }
  
  protected void onRecordLoaded()
  {
    mRecordsToLoad -= 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onRecordLoaded ");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" requested: ");
    localStringBuilder.append(mRecordsRequested);
    log(localStringBuilder.toString());
    if (getRecordsLoaded()) {
      onAllRecordsLoaded();
    } else if ((!getLockedRecordsLoaded()) && (!getNetworkLockedRecordsLoaded()))
    {
      if (mRecordsToLoad < 0)
      {
        loge("recordsToLoad <0, programmer error suspected");
        mRecordsToLoad = 0;
      }
    }
    else {
      onLockedAllRecordsLoaded();
    }
  }
  
  public void onRefresh(boolean paramBoolean, int[] paramArrayOfInt)
  {
    if (paramBoolean) {
      fetchSimRecords();
    }
  }
  
  protected void resetRecords()
  {
    mImsi = null;
    mMsisdn = null;
    mVoiceMailNum = null;
    mMncLength = -1;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setting0 mMncLength");
    ((StringBuilder)localObject).append(mMncLength);
    log(((StringBuilder)localObject).toString());
    mIccId = null;
    mFullIccId = null;
    mSpnDisplayCondition = -1;
    mEfMWIS = null;
    mEfCPHS_MWI = null;
    mSpdiNetworks = null;
    mPnnHomeName = null;
    mGid1 = null;
    mGid2 = null;
    mPlmnActRecords = null;
    mOplmnActRecords = null;
    mHplmnActRecords = null;
    mFplmns = null;
    mEhplmns = null;
    mAdnCache.reset();
    log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("update icc_operator_numeric=");
    ((StringBuilder)localObject).append(null);
    log(((StringBuilder)localObject).toString());
    mTelephonyManager.setSimOperatorNumericForPhone(mParentApp.getPhoneId(), "");
    mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), "");
    mTelephonyManager.setSimCountryIsoForPhone(mParentApp.getPhoneId(), "");
    localObject = mTelephonyManager;
    TelephonyManager.setTelephonyProperty(mParentApp.getPhoneId(), "gsm.sim.spn", "");
    localObject = mTelephonyManager;
    TelephonyManager.setTelephonyProperty(mParentApp.getPhoneId(), "gsm.sim.gid1", "");
    localObject = mTelephonyManager;
    TelephonyManager.setTelephonyProperty(mParentApp.getPhoneId(), "gsm.sim.gid2", "");
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mLoaded.set(false);
  }
  
  public void setMsisdnNumber(String paramString1, String paramString2, Message paramMessage)
  {
    mNewMsisdn = paramString2;
    mNewMsisdnTag = paramString1;
    paramString1 = new StringBuilder();
    paramString1.append("Set MSISDN: ");
    paramString1.append(mNewMsisdnTag);
    paramString1.append(" ");
    paramString1.append(Rlog.pii("SIMRecords", mNewMsisdn));
    log(paramString1.toString());
    paramString1 = new AdnRecord(mNewMsisdnTag, mNewMsisdn);
    new AdnRecordLoader(mFh).updateEF(paramString1, 28480, getExtFromEf(28480), 1, null, obtainMessage(30, paramMessage));
  }
  
  public void setVoiceCallForwardingFlag(int paramInt, boolean paramBoolean, String paramString)
  {
    if (paramInt != 1) {
      return;
    }
    if (paramBoolean) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    mCallForwardingStatus = paramInt;
    mRecordsEventsRegistrants.notifyResult(Integer.valueOf(1));
    try
    {
      if (validEfCfis(mEfCfis))
      {
        if (paramBoolean)
        {
          localObject = mEfCfis;
          localObject[1] = ((byte)(byte)(localObject[1] | 0x1));
        }
        else
        {
          localObject = mEfCfis;
          localObject[1] = ((byte)(byte)(localObject[1] & 0xFE));
        }
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("setVoiceCallForwardingFlag: enable=");
        ((StringBuilder)localObject).append(paramBoolean);
        ((StringBuilder)localObject).append(" mEfCfis=");
        ((StringBuilder)localObject).append(IccUtils.bytesToHexString(mEfCfis));
        log(((StringBuilder)localObject).toString());
        if ((paramBoolean) && (!TextUtils.isEmpty(paramString)))
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("EF_CFIS: updating cf number, ");
          ((StringBuilder)localObject).append(Rlog.pii("SIMRecords", paramString));
          logv(((StringBuilder)localObject).toString());
          paramString = PhoneNumberUtils.numberToCalledPartyBCD(paramString, 1);
          System.arraycopy(paramString, 0, mEfCfis, 3, paramString.length);
          mEfCfis[2] = ((byte)(byte)paramString.length);
          mEfCfis[14] = ((byte)-1);
          mEfCfis[15] = ((byte)-1);
        }
        mFh.updateEFLinearFixed(28619, 1, mEfCfis, null, obtainMessage(14, Integer.valueOf(28619)));
      }
      else
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("setVoiceCallForwardingFlag: ignoring enable=");
        paramString.append(paramBoolean);
        paramString.append(" invalid mEfCfis=");
        paramString.append(IccUtils.bytesToHexString(mEfCfis));
        log(paramString.toString());
      }
      if (mEfCff != null)
      {
        if (paramBoolean) {
          mEfCff[0] = ((byte)(byte)(mEfCff[0] & 0xF0 | 0xA));
        } else {
          mEfCff[0] = ((byte)(byte)(mEfCff[0] & 0xF0 | 0x5));
        }
        mFh.updateEFTransparent(28435, mEfCff, obtainMessage(14, Integer.valueOf(28435)));
      }
    }
    catch (ArrayIndexOutOfBoundsException paramString)
    {
      logw("Error saving call forwarding flag to SIM. Probably malformed SIM record", paramString);
    }
  }
  
  public void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage)
  {
    if (mIsVoiceMailFixed)
    {
      forMessageexception = new IccVmFixedException("Voicemail number is fixed by operator");
      paramMessage.sendToTarget();
      return;
    }
    mNewVoiceMailNum = paramString2;
    mNewVoiceMailTag = paramString1;
    paramString1 = new AdnRecord(mNewVoiceMailTag, mNewVoiceMailNum);
    if ((mMailboxIndex != 0) && (mMailboxIndex != 255))
    {
      new AdnRecordLoader(mFh).updateEF(paramString1, 28615, 28616, mMailboxIndex, null, obtainMessage(20, paramMessage));
    }
    else if (isCphsMailboxEnabled())
    {
      new AdnRecordLoader(mFh).updateEF(paramString1, 28439, 28490, 1, null, obtainMessage(25, paramMessage));
    }
    else
    {
      forMessageexception = new IccVmNotSupportedException("Update SIM voice mailbox error");
      paramMessage.sendToTarget();
    }
  }
  
  public void setVoiceMessageWaiting(int paramInt1, int paramInt2)
  {
    if (paramInt1 != 1) {
      return;
    }
    try
    {
      byte[] arrayOfByte;
      int i;
      if (mEfMWIS != null)
      {
        arrayOfByte = mEfMWIS;
        i = mEfMWIS[0];
        if (paramInt2 == 0) {
          paramInt1 = 0;
        } else {
          paramInt1 = 1;
        }
        arrayOfByte[0] = ((byte)(byte)(i & 0xFE | paramInt1));
        if (paramInt2 < 0) {
          mEfMWIS[1] = ((byte)0);
        } else {
          mEfMWIS[1] = ((byte)(byte)paramInt2);
        }
        mFh.updateEFLinearFixed(28618, 1, mEfMWIS, null, obtainMessage(14, 28618, 0));
      }
      if (mEfCPHS_MWI != null)
      {
        arrayOfByte = mEfCPHS_MWI;
        i = mEfCPHS_MWI[0];
        if (paramInt2 == 0) {
          paramInt1 = 5;
        } else {
          paramInt1 = 10;
        }
        arrayOfByte[0] = ((byte)(byte)(i & 0xF0 | paramInt1));
        mFh.updateEFTransparent(28433, mEfCPHS_MWI, obtainMessage(14, Integer.valueOf(28433)));
      }
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      logw("Error saving voice mail state to SIM. Probably malformed SIM record", localArrayIndexOutOfBoundsException);
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SimRecords: ");
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" mVmConfig");
    localStringBuilder.append(mVmConfig);
    localStringBuilder.append(" callForwardingEnabled=");
    localStringBuilder.append(mCallForwardingStatus);
    localStringBuilder.append(" spnState=");
    localStringBuilder.append(mSpnState);
    localStringBuilder.append(" mCphsInfo=");
    localStringBuilder.append(mCphsInfo);
    localStringBuilder.append(" mCspPlmnEnabled=");
    localStringBuilder.append(mCspPlmnEnabled);
    localStringBuilder.append(" efMWIS=");
    localStringBuilder.append(mEfMWIS);
    localStringBuilder.append(" efCPHS_MWI=");
    localStringBuilder.append(mEfCPHS_MWI);
    localStringBuilder.append(" mEfCff=");
    localStringBuilder.append(mEfCff);
    localStringBuilder.append(" mEfCfis=");
    localStringBuilder.append(mEfCfis);
    localStringBuilder.append(" getOperatorNumeric=");
    localStringBuilder.append(getOperatorNumeric());
    return localStringBuilder.toString();
  }
  
  private class EfPlLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfPlLoaded() {}
    
    public String getEfName()
    {
      return "EF_PL";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      mEfPl = ((byte[])result);
      SIMRecords localSIMRecords = SIMRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("EF_PL=");
      paramAsyncResult.append(IccUtils.bytesToHexString(mEfPl));
      localSIMRecords.log(paramAsyncResult.toString());
    }
  }
  
  private class EfUsimLiLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfUsimLiLoaded() {}
    
    public String getEfName()
    {
      return "EF_LI";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      mEfLi = ((byte[])result);
      SIMRecords localSIMRecords = SIMRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("EF_LI=");
      paramAsyncResult.append(IccUtils.bytesToHexString(mEfLi));
      localSIMRecords.log(paramAsyncResult.toString());
    }
  }
  
  private static enum GetSpnFsmState
  {
    IDLE,  INIT,  READ_SPN_3GPP,  READ_SPN_CPHS,  READ_SPN_SHORT_CPHS;
    
    private GetSpnFsmState() {}
  }
}
