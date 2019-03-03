package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PlmnTableAsus;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class IccRecords
  extends Handler
  implements IccConstants
{
  public static final int CALL_FORWARDING_STATUS_DISABLED = 0;
  public static final int CALL_FORWARDING_STATUS_ENABLED = 1;
  public static final int CALL_FORWARDING_STATUS_UNKNOWN = -1;
  protected static final boolean DBG = true;
  public static final int DEFAULT_VOICE_MESSAGE_COUNT = -2;
  private static final int EVENT_AKA_AUTHENTICATE_DONE = 90;
  protected static final int EVENT_APP_READY = 1;
  public static final int EVENT_CFI = 1;
  public static final int EVENT_GET_ICC_RECORD_DONE = 100;
  protected static final int EVENT_GET_SMS_RECORD_SIZE_DONE = 28;
  public static final int EVENT_MWI = 0;
  public static final int EVENT_REFRESH = 31;
  public static final int EVENT_SPN = 2;
  protected static final int HANDLER_ACTION_BASE = 1238272;
  protected static final int HANDLER_ACTION_NONE = 1238272;
  protected static final int HANDLER_ACTION_SEND_RESPONSE = 1238273;
  protected static final int LOCKED_RECORDS_REQ_REASON_LOCKED = 1;
  protected static final int LOCKED_RECORDS_REQ_REASON_NETWORK_LOCKED = 2;
  protected static final int LOCKED_RECORDS_REQ_REASON_NONE = 0;
  public static final int SPN_RULE_SHOW_PLMN = 2;
  public static final int SPN_RULE_SHOW_SPN = 1;
  protected static final int UNINITIALIZED = -1;
  protected static final int UNKNOWN = 0;
  public static final int UNKNOWN_VOICE_MESSAGE_COUNT = -1;
  protected static final boolean VDBG = false;
  protected static AtomicInteger sNextRequestId = new AtomicInteger(1);
  private IccIoResult auth_rsp;
  protected AdnRecordCache mAdnCache;
  CarrierTestOverride mCarrierTestOverride;
  protected CommandsInterface mCi;
  protected Context mContext;
  protected AtomicBoolean mDestroyed = new AtomicBoolean(false);
  protected String[] mEhplmns;
  protected IccFileHandler mFh;
  protected String[] mFplmns;
  protected String mFullIccId;
  protected String mGid1;
  protected String mGid2;
  protected PlmnActRecord[] mHplmnActRecords;
  protected String mIccId;
  protected String mImsi;
  protected RegistrantList mImsiReadyRegistrants = new RegistrantList();
  protected boolean mIsVoiceMailFixed = false;
  protected AtomicBoolean mLoaded = new AtomicBoolean(false);
  private final Object mLock = new Object();
  protected RegistrantList mLockedRecordsLoadedRegistrants = new RegistrantList();
  protected int mLockedRecordsReqReason = 0;
  protected int mMailboxIndex = 0;
  protected int mMncLength = -1;
  protected String mMsisdn = null;
  protected String mMsisdnTag = null;
  protected RegistrantList mNetworkLockedRecordsLoadedRegistrants = new RegistrantList();
  protected RegistrantList mNetworkSelectionModeAutomaticRegistrants = new RegistrantList();
  protected String mNewMsisdn = null;
  protected String mNewMsisdnTag = null;
  protected RegistrantList mNewSmsRegistrants = new RegistrantList();
  protected String mNewVoiceMailNum = null;
  protected String mNewVoiceMailTag = null;
  protected PlmnActRecord[] mOplmnActRecords;
  protected UiccCardApplication mParentApp;
  protected final HashMap<Integer, Message> mPendingResponses = new HashMap();
  protected PlmnActRecord[] mPlmnActRecords;
  protected String mPnnHomeName;
  protected String mPrefLang;
  protected RegistrantList mRecordsEventsRegistrants = new RegistrantList();
  protected RegistrantList mRecordsLoadedRegistrants = new RegistrantList();
  protected RegistrantList mRecordsOverrideRegistrants = new RegistrantList();
  protected boolean mRecordsRequested = false;
  protected int mRecordsToLoad;
  protected int mSmsCountOnIcc = -1;
  private String mSpn;
  protected RegistrantList mSpnUpdatedRegistrants = new RegistrantList();
  protected TelephonyManager mTelephonyManager;
  protected String mVoiceMailNum = null;
  protected String mVoiceMailTag = null;
  
  public IccRecords(UiccCardApplication paramUiccCardApplication, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    mContext = paramContext;
    mCi = paramCommandsInterface;
    mFh = paramUiccCardApplication.getIccFileHandler();
    mParentApp = paramUiccCardApplication;
    mTelephonyManager = ((TelephonyManager)mContext.getSystemService("phone"));
    mCarrierTestOverride = new CarrierTestOverride();
    mCi.registerForIccRefresh(this, 31, null);
  }
  
  protected static String findBestLanguage(byte[] paramArrayOfByte, String[] paramArrayOfString)
    throws UnsupportedEncodingException
  {
    if ((paramArrayOfByte != null) && (paramArrayOfString != null))
    {
      for (int i = 0; i + 1 < paramArrayOfByte.length; i += 2)
      {
        String str = new String(paramArrayOfByte, i, 2, "ISO-8859-1");
        for (int j = 0; j < paramArrayOfString.length; j++) {
          if ((paramArrayOfString[j] != null) && (paramArrayOfString[j].length() >= 2) && (paramArrayOfString[j].substring(0, 2).equalsIgnoreCase(str))) {
            return str;
          }
        }
      }
      return null;
    }
    return null;
  }
  
  public void dispose()
  {
    mDestroyed.set(true);
    auth_rsp = null;
    synchronized (mLock)
    {
      mLock.notifyAll();
      mCi.unregisterForIccRefresh(this);
      mParentApp = null;
      mFh = null;
      mCi = null;
      mContext = null;
      if (mAdnCache != null) {
        mAdnCache.reset();
      }
      mLoaded.set(false);
      return;
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("IccRecords: ");
    paramFileDescriptor.append(this);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDestroyed=");
    paramFileDescriptor.append(mDestroyed);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCi=");
    paramFileDescriptor.append(mCi);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mFh=");
    paramFileDescriptor.append(mFh);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mParentApp=");
    paramFileDescriptor.append(mParentApp);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" recordsLoadedRegistrants: size=");
    paramFileDescriptor.append(mRecordsLoadedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (int i = 0; i < mRecordsLoadedRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  recordsLoadedRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mRecordsLoadedRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mLockedRecordsLoadedRegistrants: size=");
    paramFileDescriptor.append(mLockedRecordsLoadedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mLockedRecordsLoadedRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mLockedRecordsLoadedRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mLockedRecordsLoadedRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNetworkLockedRecordsLoadedRegistrants: size=");
    paramFileDescriptor.append(mNetworkLockedRecordsLoadedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mNetworkLockedRecordsLoadedRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mLockedRecordsLoadedRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mNetworkLockedRecordsLoadedRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mImsiReadyRegistrants: size=");
    paramFileDescriptor.append(mImsiReadyRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mImsiReadyRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mImsiReadyRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mImsiReadyRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mRecordsEventsRegistrants: size=");
    paramFileDescriptor.append(mRecordsEventsRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mRecordsEventsRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mRecordsEventsRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mRecordsEventsRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNewSmsRegistrants: size=");
    paramFileDescriptor.append(mNewSmsRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mNewSmsRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mNewSmsRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mNewSmsRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNetworkSelectionModeAutomaticRegistrants: size=");
    paramFileDescriptor.append(mNetworkSelectionModeAutomaticRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mNetworkSelectionModeAutomaticRegistrants.size(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mNetworkSelectionModeAutomaticRegistrants[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mNetworkSelectionModeAutomaticRegistrants.get(i)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mRecordsRequested=");
    paramFileDescriptor.append(mRecordsRequested);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mLockedRecordsReqReason=");
    paramFileDescriptor.append(mLockedRecordsReqReason);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mRecordsToLoad=");
    paramFileDescriptor.append(mRecordsToLoad);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mRdnCache=");
    paramFileDescriptor.append(mAdnCache);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramArrayOfString = SubscriptionInfo.givePrintableIccid(mFullIccId);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" iccid=");
    paramFileDescriptor.append(paramArrayOfString);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMsisdn=");
    paramFileDescriptor.append(Rlog.pii(false, mMsisdn));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMsisdnTag=");
    paramFileDescriptor.append(mMsisdnTag);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mVoiceMailNum=");
    paramFileDescriptor.append(Rlog.pii(false, mVoiceMailNum));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mVoiceMailTag=");
    paramFileDescriptor.append(mVoiceMailTag);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNewVoiceMailNum=");
    paramFileDescriptor.append(Rlog.pii(false, mNewVoiceMailNum));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNewVoiceMailTag=");
    paramFileDescriptor.append(mNewVoiceMailTag);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIsVoiceMailFixed=");
    paramFileDescriptor.append(mIsVoiceMailFixed);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append(" mImsi=");
    if (mImsi != null)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(mImsi.substring(0, 6));
      paramFileDescriptor.append(Rlog.pii(false, mImsi.substring(6)));
      paramFileDescriptor = paramFileDescriptor.toString();
    }
    else
    {
      paramFileDescriptor = "null";
    }
    paramArrayOfString.append(paramFileDescriptor);
    paramPrintWriter.println(paramArrayOfString.toString());
    if (mCarrierTestOverride.isInTestMode())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mFakeImsi=");
      paramFileDescriptor.append(mCarrierTestOverride.getFakeIMSI());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMncLength=");
    paramFileDescriptor.append(mMncLength);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMailboxIndex=");
    paramFileDescriptor.append(mMailboxIndex);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSpn=");
    paramFileDescriptor.append(mSpn);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (mCarrierTestOverride.isInTestMode())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mFakeSpn=");
      paramFileDescriptor.append(mCarrierTestOverride.getFakeSpn());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramPrintWriter.flush();
  }
  
  public AdnRecordCache getAdnCache()
  {
    return mAdnCache;
  }
  
  public abstract int getDisplayRule(ServiceState paramServiceState);
  
  public String getFullIccId()
  {
    return mFullIccId;
  }
  
  public String getGid1()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakeGid1() != null)) {
      return mCarrierTestOverride.getFakeGid1();
    }
    return mGid1;
  }
  
  public String getGid2()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakeGid2() != null)) {
      return mCarrierTestOverride.getFakeGid2();
    }
    return mGid2;
  }
  
  public String getIMSI()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakeIMSI() != null)) {
      return mCarrierTestOverride.getFakeIMSI();
    }
    return mImsi;
  }
  
  public String getIccId()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakeIccid() != null)) {
      return mCarrierTestOverride.getFakeIccid();
    }
    return mIccId;
  }
  
  public String getIccSimChallengeResponse(int paramInt, String paramString)
  {
    log("getIccSimChallengeResponse:");
    try
    {
      synchronized (mLock)
      {
        CommandsInterface localCommandsInterface = mCi;
        UiccCardApplication localUiccCardApplication = mParentApp;
        if ((localCommandsInterface != null) && (localUiccCardApplication != null))
        {
          localCommandsInterface.requestIccSimAuthentication(paramInt, paramString, localUiccCardApplication.getAid(), obtainMessage(90));
          try
          {
            mLock.wait();
            if (auth_rsp == null)
            {
              loge("getIccSimChallengeResponse: No authentication response");
              return null;
            }
            log("getIccSimChallengeResponse: return auth_rsp");
            return Base64.encodeToString(auth_rsp.payload, 2);
          }
          catch (InterruptedException paramString)
          {
            loge("getIccSimChallengeResponse: Fail, interrupted while trying to request Icc Sim Auth");
            return null;
          }
        }
        loge("getIccSimChallengeResponse: Fail, ci or parentApp is null");
        return null;
      }
      return null;
    }
    catch (Exception paramString)
    {
      loge("getIccSimChallengeResponse: Fail while trying to request Icc Sim Auth");
    }
  }
  
  public IsimRecords getIsimRecords()
  {
    return null;
  }
  
  protected boolean getLockedRecordsLoaded()
  {
    int i = mRecordsToLoad;
    boolean bool = true;
    if ((i != 0) || (mLockedRecordsReqReason != 1)) {
      bool = false;
    }
    return bool;
  }
  
  public String getMsisdnAlphaTag()
  {
    return mMsisdnTag;
  }
  
  public String getMsisdnNumber()
  {
    return mMsisdn;
  }
  
  public String getNAI()
  {
    return null;
  }
  
  protected boolean getNetworkLockedRecordsLoaded()
  {
    boolean bool;
    if ((mRecordsToLoad == 0) && (mLockedRecordsReqReason == 2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getOperatorNumeric()
  {
    return null;
  }
  
  public String getPnnHomeName()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakePnnHomeName() != null)) {
      return mCarrierTestOverride.getFakePnnHomeName();
    }
    return mPnnHomeName;
  }
  
  public boolean getRecordsLoaded()
  {
    boolean bool;
    if ((mRecordsToLoad == 0) && (mRecordsRequested)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getServiceProviderName()
  {
    if ((mCarrierTestOverride.isInTestMode()) && (mCarrierTestOverride.getFakeSpn() != null)) {
      return mCarrierTestOverride.getFakeSpn();
    }
    Object localObject1 = mSpn;
    Object localObject2 = PlmnTableAsus.GetCustomPlmn(getOperatorNumeric(), mSpn);
    if (!TextUtils.isEmpty((CharSequence)localObject2)) {
      localObject1 = localObject2;
    }
    localObject2 = mParentApp;
    if (localObject2 != null)
    {
      localObject2 = ((UiccCardApplication)localObject2).getUiccProfile();
      if (localObject2 != null)
      {
        localObject2 = ((UiccProfile)localObject2).getOperatorBrandOverride();
        if (localObject2 != null)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("getServiceProviderName: override, providerName=");
          localStringBuilder.append((String)localObject1);
          log(localStringBuilder.toString());
          localObject1 = localObject2;
        }
        else
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("getServiceProviderName: no brandOverride, providerName=");
          ((StringBuilder)localObject2).append((String)localObject1);
          log(((StringBuilder)localObject2).toString());
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("getServiceProviderName: card is null, providerName=");
        ((StringBuilder)localObject2).append((String)localObject1);
        log(((StringBuilder)localObject2).toString());
      }
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("getServiceProviderName: mParentApp is null, providerName=");
      ((StringBuilder)localObject2).append((String)localObject1);
      log(((StringBuilder)localObject2).toString());
    }
    return localObject1;
  }
  
  public String getSimLanguage()
  {
    return mPrefLang;
  }
  
  public int getSmsCapacityOnIcc()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getSmsCapacityOnIcc: ");
    localStringBuilder.append(mSmsCountOnIcc);
    log(localStringBuilder.toString());
    return mSmsCountOnIcc;
  }
  
  public UsimServiceTable getUsimServiceTable()
  {
    return null;
  }
  
  public int getVoiceCallForwardingFlag()
  {
    return -1;
  }
  
  public String getVoiceMailAlphaTag()
  {
    return mVoiceMailTag;
  }
  
  public String getVoiceMailNumber()
  {
    return mVoiceMailNum;
  }
  
  public abstract int getVoiceMessageCount();
  
  protected abstract void handleFileUpdate(int paramInt);
  
  /* Error */
  public void handleMessage(Message paramMessage)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 575	android/os/Message:what	I
    //   4: istore_2
    //   5: iload_2
    //   6: bipush 28
    //   8: if_icmpeq +449 -> 457
    //   11: iload_2
    //   12: bipush 31
    //   14: if_icmpeq +367 -> 381
    //   17: iload_2
    //   18: bipush 90
    //   20: if_icmpeq +184 -> 204
    //   23: iload_2
    //   24: bipush 100
    //   26: if_icmpeq +11 -> 37
    //   29: aload_0
    //   30: aload_1
    //   31: invokespecial 577	android/os/Handler:handleMessage	(Landroid/os/Message;)V
    //   34: goto +603 -> 637
    //   37: aload_1
    //   38: getfield 580	android/os/Message:obj	Ljava/lang/Object;
    //   41: checkcast 582	android/os/AsyncResult
    //   44: astore_1
    //   45: aload_1
    //   46: getfield 585	android/os/AsyncResult:userObj	Ljava/lang/Object;
    //   49: checkcast 8	com/android/internal/telephony/uicc/IccRecords$IccRecordLoaded
    //   52: astore_3
    //   53: new 281	java/lang/StringBuilder
    //   56: astore 4
    //   58: aload 4
    //   60: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   63: aload 4
    //   65: aload_3
    //   66: invokeinterface 588 1 0
    //   71: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload 4
    //   77: ldc_w 590
    //   80: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_0
    //   85: aload 4
    //   87: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   90: invokevirtual 465	com/android/internal/telephony/uicc/IccRecords:log	(Ljava/lang/String;)V
    //   93: aload_1
    //   94: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   97: ifnull +44 -> 141
    //   100: new 281	java/lang/StringBuilder
    //   103: astore 4
    //   105: aload 4
    //   107: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   110: aload 4
    //   112: ldc_w 596
    //   115: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload 4
    //   121: aload_1
    //   122: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   125: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_0
    //   130: aload 4
    //   132: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   135: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   138: goto +53 -> 191
    //   141: aload_3
    //   142: aload_1
    //   143: invokeinterface 600 2 0
    //   148: goto +43 -> 191
    //   151: astore_1
    //   152: goto +46 -> 198
    //   155: astore_1
    //   156: new 281	java/lang/StringBuilder
    //   159: astore 4
    //   161: aload 4
    //   163: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   166: aload 4
    //   168: ldc_w 602
    //   171: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload 4
    //   177: aload_1
    //   178: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   181: pop
    //   182: aload_0
    //   183: aload 4
    //   185: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   191: aload_0
    //   192: invokevirtual 604	com/android/internal/telephony/uicc/IccRecords:onRecordLoaded	()V
    //   195: goto +442 -> 637
    //   198: aload_0
    //   199: invokevirtual 604	com/android/internal/telephony/uicc/IccRecords:onRecordLoaded	()V
    //   202: aload_1
    //   203: athrow
    //   204: aload_1
    //   205: getfield 580	android/os/Message:obj	Ljava/lang/Object;
    //   208: checkcast 582	android/os/AsyncResult
    //   211: astore 4
    //   213: aload_0
    //   214: aconst_null
    //   215: putfield 263	com/android/internal/telephony/uicc/IccRecords:auth_rsp	Lcom/android/internal/telephony/uicc/IccIoResult;
    //   218: aload_0
    //   219: ldc_w 605
    //   222: invokevirtual 465	com/android/internal/telephony/uicc/IccRecords:log	(Ljava/lang/String;)V
    //   225: aload 4
    //   227: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   230: ifnull +40 -> 270
    //   233: new 281	java/lang/StringBuilder
    //   236: dup
    //   237: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   240: astore_1
    //   241: aload_1
    //   242: ldc_w 607
    //   245: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: aload_1
    //   250: aload 4
    //   252: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   255: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   258: pop
    //   259: aload_0
    //   260: aload_1
    //   261: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   267: goto +86 -> 353
    //   270: aload_0
    //   271: aload 4
    //   273: getfield 610	android/os/AsyncResult:result	Ljava/lang/Object;
    //   276: checkcast 488	com/android/internal/telephony/uicc/IccIoResult
    //   279: putfield 263	com/android/internal/telephony/uicc/IccRecords:auth_rsp	Lcom/android/internal/telephony/uicc/IccIoResult;
    //   282: new 281	java/lang/StringBuilder
    //   285: astore_1
    //   286: aload_1
    //   287: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   290: aload_1
    //   291: ldc_w 612
    //   294: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   297: pop
    //   298: aload_1
    //   299: aload_0
    //   300: getfield 263	com/android/internal/telephony/uicc/IccRecords:auth_rsp	Lcom/android/internal/telephony/uicc/IccIoResult;
    //   303: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   306: pop
    //   307: aload_0
    //   308: aload_1
    //   309: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   312: invokevirtual 465	com/android/internal/telephony/uicc/IccRecords:log	(Ljava/lang/String;)V
    //   315: goto +38 -> 353
    //   318: astore_1
    //   319: new 281	java/lang/StringBuilder
    //   322: dup
    //   323: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   326: astore 4
    //   328: aload 4
    //   330: ldc_w 614
    //   333: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: pop
    //   337: aload 4
    //   339: aload_1
    //   340: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   343: pop
    //   344: aload_0
    //   345: aload 4
    //   347: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   350: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   353: aload_0
    //   354: getfield 191	com/android/internal/telephony/uicc/IccRecords:mLock	Ljava/lang/Object;
    //   357: astore 4
    //   359: aload 4
    //   361: monitorenter
    //   362: aload_0
    //   363: getfield 191	com/android/internal/telephony/uicc/IccRecords:mLock	Ljava/lang/Object;
    //   366: invokevirtual 266	java/lang/Object:notifyAll	()V
    //   369: aload 4
    //   371: monitorexit
    //   372: goto +265 -> 637
    //   375: astore_1
    //   376: aload 4
    //   378: monitorexit
    //   379: aload_1
    //   380: athrow
    //   381: aload_1
    //   382: getfield 580	android/os/Message:obj	Ljava/lang/Object;
    //   385: checkcast 582	android/os/AsyncResult
    //   388: astore_1
    //   389: aload_0
    //   390: ldc_w 616
    //   393: invokevirtual 465	com/android/internal/telephony/uicc/IccRecords:log	(Ljava/lang/String;)V
    //   396: aload_1
    //   397: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   400: ifnonnull +17 -> 417
    //   403: aload_0
    //   404: aload_1
    //   405: getfield 610	android/os/AsyncResult:result	Ljava/lang/Object;
    //   408: checkcast 618	com/android/internal/telephony/uicc/IccRefreshResponse
    //   411: invokevirtual 622	com/android/internal/telephony/uicc/IccRecords:handleRefresh	(Lcom/android/internal/telephony/uicc/IccRefreshResponse;)V
    //   414: goto +223 -> 637
    //   417: new 281	java/lang/StringBuilder
    //   420: dup
    //   421: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   424: astore 4
    //   426: aload 4
    //   428: ldc_w 624
    //   431: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   434: pop
    //   435: aload 4
    //   437: aload_1
    //   438: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   441: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   444: pop
    //   445: aload_0
    //   446: aload 4
    //   448: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   451: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   454: goto +183 -> 637
    //   457: aload_1
    //   458: getfield 580	android/os/Message:obj	Ljava/lang/Object;
    //   461: checkcast 582	android/os/AsyncResult
    //   464: astore_1
    //   465: aload_1
    //   466: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   469: ifnull +43 -> 512
    //   472: new 281	java/lang/StringBuilder
    //   475: dup
    //   476: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   479: astore 4
    //   481: aload 4
    //   483: ldc_w 626
    //   486: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   489: pop
    //   490: aload 4
    //   492: aload_1
    //   493: getfield 594	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   496: invokevirtual 291	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   499: pop
    //   500: aload_0
    //   501: aload 4
    //   503: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   506: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   509: goto +128 -> 637
    //   512: aload_1
    //   513: getfield 610	android/os/AsyncResult:result	Ljava/lang/Object;
    //   516: checkcast 628	[I
    //   519: astore 4
    //   521: aload_0
    //   522: aload 4
    //   524: iconst_2
    //   525: iaload
    //   526: putfield 186	com/android/internal/telephony/uicc/IccRecords:mSmsCountOnIcc	I
    //   529: new 281	java/lang/StringBuilder
    //   532: astore_1
    //   533: aload_1
    //   534: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   537: aload_1
    //   538: ldc_w 630
    //   541: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   544: pop
    //   545: aload_1
    //   546: aload 4
    //   548: iconst_0
    //   549: iaload
    //   550: invokevirtual 317	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   553: pop
    //   554: aload_1
    //   555: ldc_w 632
    //   558: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   561: pop
    //   562: aload_1
    //   563: aload 4
    //   565: iconst_1
    //   566: iaload
    //   567: invokevirtual 317	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   570: pop
    //   571: aload_1
    //   572: ldc_w 634
    //   575: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   578: pop
    //   579: aload_1
    //   580: aload 4
    //   582: iconst_2
    //   583: iaload
    //   584: invokevirtual 317	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   587: pop
    //   588: aload_0
    //   589: aload_1
    //   590: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   593: invokevirtual 465	com/android/internal/telephony/uicc/IccRecords:log	(Ljava/lang/String;)V
    //   596: goto +41 -> 637
    //   599: astore_1
    //   600: new 281	java/lang/StringBuilder
    //   603: dup
    //   604: invokespecial 282	java/lang/StringBuilder:<init>	()V
    //   607: astore 4
    //   609: aload 4
    //   611: ldc_w 636
    //   614: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   617: pop
    //   618: aload 4
    //   620: aload_1
    //   621: invokevirtual 637	java/lang/ArrayIndexOutOfBoundsException:toString	()Ljava/lang/String;
    //   624: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   627: pop
    //   628: aload_0
    //   629: aload 4
    //   631: invokevirtual 295	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   634: invokevirtual 484	com/android/internal/telephony/uicc/IccRecords:loge	(Ljava/lang/String;)V
    //   637: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	638	0	this	IccRecords
    //   0	638	1	paramMessage	Message
    //   4	23	2	i	int
    //   52	90	3	localIccRecordLoaded	IccRecordLoaded
    // Exception table:
    //   from	to	target	type
    //   37	138	151	finally
    //   141	148	151	finally
    //   156	191	151	finally
    //   37	138	155	java/lang/RuntimeException
    //   141	148	155	java/lang/RuntimeException
    //   270	315	318	java/lang/Exception
    //   362	372	375	finally
    //   376	379	375	finally
    //   521	596	599	java/lang/ArrayIndexOutOfBoundsException
  }
  
  protected void handleRefresh(IccRefreshResponse paramIccRefreshResponse)
  {
    if (paramIccRefreshResponse == null)
    {
      log("handleRefresh received without input");
      return;
    }
    if ((!TextUtils.isEmpty(aid)) && (!aid.equals(mParentApp.getAid()))) {
      return;
    }
    if (refreshResult != 0)
    {
      log("handleRefresh with unknown operation");
    }
    else
    {
      log("handleRefresh with SIM_FILE_UPDATED");
      handleFileUpdate(efId);
    }
  }
  
  public boolean isCspPlmnEnabled()
  {
    return false;
  }
  
  public boolean isLoaded()
  {
    return mLoaded.get();
  }
  
  public boolean isProvisioned()
  {
    return true;
  }
  
  protected abstract void log(String paramString);
  
  protected abstract void loge(String paramString);
  
  protected abstract void onAllRecordsLoaded();
  
  public abstract void onReady();
  
  protected abstract void onRecordLoaded();
  
  public abstract void onRefresh(boolean paramBoolean, int[] paramArrayOfInt);
  
  public void registerForImsiReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mImsiReadyRegistrants.add(paramHandler);
    if (getIMSI() != null) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void registerForLockedRecordsLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mLockedRecordsLoadedRegistrants.add(paramHandler);
    if (getLockedRecordsLoaded()) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void registerForNetworkLockedRecordsLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNetworkLockedRecordsLoadedRegistrants.add(paramHandler);
    if (getNetworkLockedRecordsLoaded()) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void registerForNetworkSelectionModeAutomatic(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNetworkSelectionModeAutomaticRegistrants.add(paramHandler);
  }
  
  public void registerForNewSms(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNewSmsRegistrants.add(paramHandler);
  }
  
  public void registerForRecordsEvents(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRecordsEventsRegistrants.add(paramHandler);
    paramHandler.notifyResult(Integer.valueOf(0));
    paramHandler.notifyResult(Integer.valueOf(1));
  }
  
  public void registerForRecordsLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRecordsLoadedRegistrants.add(paramHandler);
    if (getRecordsLoaded()) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void registerForRecordsOverride(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRecordsOverrideRegistrants.add(paramHandler);
    if (getRecordsLoaded()) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void registerForSpnUpdate(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (mDestroyed.get()) {
      return;
    }
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mSpnUpdatedRegistrants.add(paramHandler);
    if (!TextUtils.isEmpty(mSpn)) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public Message retrievePendingResponseMessage(Integer paramInteger)
  {
    synchronized (mPendingResponses)
    {
      paramInteger = (Message)mPendingResponses.remove(paramInteger);
      return paramInteger;
    }
  }
  
  public void setCarrierTestOverride(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    mCarrierTestOverride.override(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7);
    mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), paramString7);
    mTelephonyManager.setSimOperatorNumericForPhone(mParentApp.getPhoneId(), paramString1);
    mRecordsOverrideRegistrants.notifyRegistrants();
  }
  
  public void setImsi(String paramString)
  {
    mImsi = paramString;
    mImsiReadyRegistrants.notifyRegistrants();
  }
  
  public void setMsisdnNumber(String paramString1, String paramString2, Message paramMessage)
  {
    loge("setMsisdn() should not be invoked on base IccRecords");
    forMessageexception = new IccIoResult(106, 130, (byte[])null).getException();
    paramMessage.sendToTarget();
  }
  
  protected void setServiceProviderName(String paramString)
  {
    if (!TextUtils.equals(mSpn, paramString))
    {
      mSpnUpdatedRegistrants.notifyRegistrants();
      Object localObject = paramString;
      if (PlmnTableAsus.containInXLS(paramString) >= 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("[APLMN] spn change from ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" to ");
        ((StringBuilder)localObject).append("XL");
        log(((StringBuilder)localObject).toString());
        localObject = "XL";
      }
      mSpn = ((String)localObject);
    }
  }
  
  protected void setSimLanguage(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    String[] arrayOfString = mContext.getAssets().getLocales();
    try
    {
      mPrefLang = findBestLanguage(paramArrayOfByte1, arrayOfString);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to parse EF-LI: ");
      localStringBuilder.append(Arrays.toString(paramArrayOfByte1));
      log(localStringBuilder.toString());
    }
    if (mPrefLang == null) {
      try
      {
        mPrefLang = findBestLanguage(paramArrayOfByte2, arrayOfString);
      }
      catch (UnsupportedEncodingException paramArrayOfByte2)
      {
        paramArrayOfByte2 = new StringBuilder();
        paramArrayOfByte2.append("Unable to parse EF-PL: ");
        paramArrayOfByte2.append(Arrays.toString(paramArrayOfByte1));
        log(paramArrayOfByte2.toString());
      }
    }
  }
  
  protected void setSystemProperty(String paramString1, String paramString2)
  {
    TelephonyManager.getDefault();
    TelephonyManager.setTelephonyProperty(mParentApp.getPhoneId(), paramString1, paramString2);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[key, value]=");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(", ");
    localStringBuilder.append(paramString2);
    log(localStringBuilder.toString());
  }
  
  public void setVoiceCallForwardingFlag(int paramInt, boolean paramBoolean, String paramString) {}
  
  public abstract void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage);
  
  public abstract void setVoiceMessageWaiting(int paramInt1, int paramInt2);
  
  public int storePendingResponseMessage(Message paramMessage)
  {
    int i = sNextRequestId.getAndIncrement();
    synchronized (mPendingResponses)
    {
      mPendingResponses.put(Integer.valueOf(i), paramMessage);
      return i;
    }
  }
  
  public String toString()
  {
    Object localObject = SubscriptionInfo.givePrintableIccid(mFullIccId);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mDestroyed=");
    localStringBuilder.append(mDestroyed);
    localStringBuilder.append(" mContext=");
    localStringBuilder.append(mContext);
    localStringBuilder.append(" mCi=");
    localStringBuilder.append(mCi);
    localStringBuilder.append(" mFh=");
    localStringBuilder.append(mFh);
    localStringBuilder.append(" mParentApp=");
    localStringBuilder.append(mParentApp);
    localStringBuilder.append(" recordsToLoad=");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" adnCache=");
    localStringBuilder.append(mAdnCache);
    localStringBuilder.append(" recordsRequested=");
    localStringBuilder.append(mRecordsRequested);
    localStringBuilder.append(" lockedRecordsReqReason=");
    localStringBuilder.append(mLockedRecordsReqReason);
    localStringBuilder.append(" iccid=");
    localStringBuilder.append((String)localObject);
    if (mCarrierTestOverride.isInTestMode())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("mFakeIccid=");
      ((StringBuilder)localObject).append(mCarrierTestOverride.getFakeIccid());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" msisdnTag=");
    localStringBuilder.append(mMsisdnTag);
    localStringBuilder.append(" voiceMailNum=");
    localStringBuilder.append(Rlog.pii(false, mVoiceMailNum));
    localStringBuilder.append(" voiceMailTag=");
    localStringBuilder.append(mVoiceMailTag);
    localStringBuilder.append(" voiceMailNum=");
    localStringBuilder.append(Rlog.pii(false, mNewVoiceMailNum));
    localStringBuilder.append(" newVoiceMailTag=");
    localStringBuilder.append(mNewVoiceMailTag);
    localStringBuilder.append(" isVoiceMailFixed=");
    localStringBuilder.append(mIsVoiceMailFixed);
    localStringBuilder.append(" mImsi=");
    if (mImsi != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mImsi.substring(0, 6));
      ((StringBuilder)localObject).append(Rlog.pii(false, mImsi.substring(6)));
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "null";
    }
    localStringBuilder.append((String)localObject);
    if (mCarrierTestOverride.isInTestMode())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" mFakeImsi=");
      ((StringBuilder)localObject).append(mCarrierTestOverride.getFakeIMSI());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" mncLength=");
    localStringBuilder.append(mMncLength);
    localStringBuilder.append(" mailboxIndex=");
    localStringBuilder.append(mMailboxIndex);
    localStringBuilder.append(" spn=");
    localStringBuilder.append(mSpn);
    if (mCarrierTestOverride.isInTestMode())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" mFakeSpn=");
      ((StringBuilder)localObject).append(mCarrierTestOverride.getFakeSpn());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    return localStringBuilder.toString();
  }
  
  public void unregisterForImsiReady(Handler paramHandler)
  {
    mImsiReadyRegistrants.remove(paramHandler);
  }
  
  public void unregisterForLockedRecordsLoaded(Handler paramHandler)
  {
    mLockedRecordsLoadedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNetworkLockedRecordsLoaded(Handler paramHandler)
  {
    mNetworkLockedRecordsLoadedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNetworkSelectionModeAutomatic(Handler paramHandler)
  {
    mNetworkSelectionModeAutomaticRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNewSms(Handler paramHandler)
  {
    mNewSmsRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRecordsEvents(Handler paramHandler)
  {
    mRecordsEventsRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRecordsLoaded(Handler paramHandler)
  {
    mRecordsLoadedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRecordsOverride(Handler paramHandler)
  {
    mRecordsOverrideRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSpnUpdate(Handler paramHandler)
  {
    mSpnUpdatedRegistrants.remove(paramHandler);
  }
  
  public static abstract interface IccRecordLoaded
  {
    public abstract String getEfName();
    
    public abstract void onRecordLoaded(AsyncResult paramAsyncResult);
  }
}
