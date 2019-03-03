package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Message;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.util.BitwiseInputStream;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class RuimRecords
  extends IccRecords
{
  private static final int CSIM_IMSI_MNC_LENGTH = 2;
  private static final int EVENT_APP_LOCKED = 32;
  private static final int EVENT_APP_NETWORK_LOCKED = 33;
  private static final int EVENT_GET_ALL_SMS_DONE = 18;
  private static final int EVENT_GET_CDMA_SUBSCRIPTION_DONE = 10;
  private static final int EVENT_GET_DEVICE_IDENTITY_DONE = 4;
  private static final int EVENT_GET_ICCID_DONE = 5;
  private static final int EVENT_GET_SMS_DONE = 22;
  private static final int EVENT_GET_SST_DONE = 17;
  private static final int EVENT_MARK_SMS_READ_DONE = 19;
  private static final int EVENT_SMS_ON_RUIM = 21;
  private static final int EVENT_UPDATE_DONE = 14;
  static final String LOG_TAG = "RuimRecords";
  boolean mCsimSpnDisplayCondition = false;
  private byte[] mEFli = null;
  private byte[] mEFpl = null;
  private String mHomeNetworkId;
  private String mHomeSystemId;
  private String mMdn;
  private String mMin;
  private String mMin2Min1;
  private String mMyMobileNumber;
  private String mNai;
  private boolean mOtaCommited = false;
  private String mPrlVersion;
  
  public RuimRecords(UiccCardApplication paramUiccCardApplication, Context paramContext, CommandsInterface paramCommandsInterface)
  {
    super(paramUiccCardApplication, paramContext, paramCommandsInterface);
    mAdnCache = new AdnRecordCache(mFh);
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mRecordsToLoad = 0;
    resetRecords();
    mParentApp.registerForReady(this, 1, null);
    mParentApp.registerForLocked(this, 32, null);
    mParentApp.registerForNetworkLocked(this, 33, null);
    paramUiccCardApplication = new StringBuilder();
    paramUiccCardApplication.append("RuimRecords X ctor this=");
    paramUiccCardApplication.append(this);
    log(paramUiccCardApplication.toString());
  }
  
  private String decodeImsi(byte[] paramArrayOfByte)
  {
    int i = decodeImsiDigits((paramArrayOfByte[9] & 0x3) << 8 | paramArrayOfByte[8] & 0xFF, 3);
    int j = decodeImsiDigits(paramArrayOfByte[6] & 0x7F, 2);
    int k = paramArrayOfByte[2];
    int m = paramArrayOfByte[1];
    int n = paramArrayOfByte[5];
    int i1 = paramArrayOfByte[4];
    int i2 = paramArrayOfByte[4] >> 2 & 0xF;
    int i3 = i2;
    if (i2 > 9) {
      i3 = 0;
    }
    i2 = paramArrayOfByte[4];
    int i4 = paramArrayOfByte[3];
    k = decodeImsiDigits(((k & 0x3) << 8) + (m & 0xFF), 3);
    i1 = decodeImsiDigits(((n & 0xFF) << 8 | i1 & 0xFF) >> 6, 3);
    i2 = decodeImsiDigits((i2 & 0x3) << 8 | i4 & 0xFF, 3);
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append(String.format(Locale.US, "%03d", new Object[] { Integer.valueOf(i) }));
    paramArrayOfByte.append(String.format(Locale.US, "%02d", new Object[] { Integer.valueOf(j) }));
    paramArrayOfByte.append(String.format(Locale.US, "%03d", new Object[] { Integer.valueOf(k) }));
    paramArrayOfByte.append(String.format(Locale.US, "%03d", new Object[] { Integer.valueOf(i1) }));
    paramArrayOfByte.append(String.format(Locale.US, "%d", new Object[] { Integer.valueOf(i3) }));
    paramArrayOfByte.append(String.format(Locale.US, "%03d", new Object[] { Integer.valueOf(i2) }));
    return paramArrayOfByte.toString();
  }
  
  private int decodeImsiDigits(int paramInt1, int paramInt2)
  {
    int i = 0;
    int k;
    for (int j = 0;; j++)
    {
      k = 1;
      if (j >= paramInt2) {
        break;
      }
      i = i * 10 + 1;
    }
    paramInt1 += i;
    i = 0;
    j = k;
    while (i < paramInt2)
    {
      if (paramInt1 / j % 10 == 0) {
        paramInt1 -= 10 * j;
      }
      j *= 10;
      i++;
    }
    return paramInt1;
  }
  
  private void fetchRuimRecords()
  {
    mRecordsRequested = true;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("fetchRuimRecords ");
    localStringBuilder.append(mRecordsToLoad);
    log(localStringBuilder.toString());
    mFh.loadEFTransparent(12258, obtainMessage(5));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(12037, obtainMessage(100, new EfPlLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28474, obtainMessage(100, new EfCsimLiLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28481, obtainMessage(100, new EfCsimSpnLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixed(28484, 1, obtainMessage(100, new EfCsimMdnLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28450, obtainMessage(100, new EfCsimImsimLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFLinearFixedAll(28456, obtainMessage(100, new EfCsimCdmaHomeLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28506, 4, obtainMessage(100, new EfCsimEprlLoaded(null)));
    mRecordsToLoad += 1;
    mFh.loadEFTransparent(28493, obtainMessage(100, new EfCsimMipUppLoaded(null)));
    mRecordsToLoad += 1;
    mFh.getEFLinearRecordSize(28476, obtainMessage(28));
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("fetchRuimRecords ");
    localStringBuilder.append(mRecordsToLoad);
    localStringBuilder.append(" requested: ");
    localStringBuilder.append(mRecordsRequested);
    log(localStringBuilder.toString());
  }
  
  private static String[] getAssetLanguages(Context paramContext)
  {
    paramContext = paramContext.getAssets().getLocales();
    String[] arrayOfString = new String[paramContext.length];
    for (int i = 0; i < paramContext.length; i++)
    {
      Object localObject = paramContext[i];
      int j = localObject.indexOf('-');
      if (j < 0) {
        arrayOfString[i] = localObject;
      } else {
        arrayOfString[i] = localObject.substring(0, j);
      }
    }
    return arrayOfString;
  }
  
  private void onGetCSimEprlDone(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = (byte[])result;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CSIM_EPRL=");
    localStringBuilder.append(IccUtils.bytesToHexString(paramAsyncResult));
    log(localStringBuilder.toString());
    if (paramAsyncResult.length > 3) {
      mPrlVersion = Integer.toString((paramAsyncResult[2] & 0xFF) << 8 | paramAsyncResult[3] & 0xFF);
    }
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("CSIM PRL version=");
    paramAsyncResult.append(mPrlVersion);
    log(paramAsyncResult.toString());
  }
  
  private void onLocked(int paramInt)
  {
    log("only fetch EF_ICCID in locked state");
    if (paramInt == 32) {
      paramInt = 1;
    } else {
      paramInt = 2;
    }
    mLockedRecordsReqReason = paramInt;
    mFh.loadEFTransparent(12258, obtainMessage(5));
    mRecordsToLoad += 1;
  }
  
  private void onLockedAllRecordsLoaded()
  {
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
  
  public void dispose()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Disposing RuimRecords ");
    localStringBuilder.append(this);
    log(localStringBuilder.toString());
    mParentApp.unregisterForReady(this);
    mParentApp.unregisterForLocked(this);
    mParentApp.unregisterForNetworkLocked(this);
    resetRecords();
    super.dispose();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RuimRecords: ");
    localStringBuilder.append(this);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.println(" extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mOtaCommited=");
    paramFileDescriptor.append(mOtaCommited);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMyMobileNumber=");
    paramFileDescriptor.append(mMyMobileNumber);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMin2Min1=");
    paramFileDescriptor.append(mMin2Min1);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPrlVersion=");
    paramFileDescriptor.append(mPrlVersion);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEFpl[]=");
    paramFileDescriptor.append(Arrays.toString(mEFpl));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEFli[]=");
    paramFileDescriptor.append(Arrays.toString(mEFli));
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCsimSpnDisplayCondition=");
    paramFileDescriptor.append(mCsimSpnDisplayCondition);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMdn=");
    paramFileDescriptor.append(mMdn);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mMin=");
    paramFileDescriptor.append(mMin);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mHomeSystemId=");
    paramFileDescriptor.append(mHomeSystemId);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mHomeNetworkId=");
    paramFileDescriptor.append(mHomeNetworkId);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.flush();
  }
  
  protected void finalize()
  {
    log("RuimRecords finalized");
  }
  
  public String getCdmaMin()
  {
    return mMin2Min1;
  }
  
  public boolean getCsimSpnDisplayCondition()
  {
    return mCsimSpnDisplayCondition;
  }
  
  public int getDisplayRule(ServiceState paramServiceState)
  {
    return 0;
  }
  
  public String getMdn()
  {
    return mMdn;
  }
  
  public String getMdnNumber()
  {
    return mMyMobileNumber;
  }
  
  public String getMin()
  {
    return mMin;
  }
  
  public String getNAI()
  {
    return mNai;
  }
  
  public String getNid()
  {
    return mHomeNetworkId;
  }
  
  public String getOperatorNumeric()
  {
    return getRUIMOperatorNumeric();
  }
  
  public String getPrlVersion()
  {
    return mPrlVersion;
  }
  
  public String getRUIMOperatorNumeric()
  {
    String str = getIMSI();
    if (str == null) {
      return null;
    }
    if ((mMncLength != -1) && (mMncLength != 0)) {
      return str.substring(0, 3 + mMncLength);
    }
    return str.substring(0, 5);
  }
  
  public String getSid()
  {
    return mHomeSystemId;
  }
  
  public int getVoiceMessageCount()
  {
    log("RuimRecords:getVoiceMessageCount - NOP for CDMA");
    return 0;
  }
  
  protected void handleFileUpdate(int paramInt)
  {
    mAdnCache.reset();
    fetchRuimRecords();
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
    //   8: getfield 434	com/android/internal/telephony/uicc/RuimRecords:mDestroyed	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   11: invokevirtual 439	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   14: ifeq +66 -> 80
    //   17: new 129	java/lang/StringBuilder
    //   20: dup
    //   21: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   24: astore 5
    //   26: aload 5
    //   28: ldc_w 441
    //   31: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: aload 5
    //   37: aload_1
    //   38: invokevirtual 140	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload 5
    //   44: ldc_w 443
    //   47: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload 5
    //   53: aload_1
    //   54: getfield 448	android/os/Message:what	I
    //   57: invokevirtual 222	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload 5
    //   63: ldc_w 450
    //   66: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_0
    //   71: aload 5
    //   73: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   76: invokevirtual 330	com/android/internal/telephony/uicc/RuimRecords:loge	(Ljava/lang/String;)V
    //   79: return
    //   80: iload_2
    //   81: istore 6
    //   83: iload_3
    //   84: istore 7
    //   86: aload_1
    //   87: getfield 448	android/os/Message:what	I
    //   90: lookupswitch	default:+106->196, 1:+676->766, 4:+656->746, 5:+488->578, 10:+299->389, 14:+242->332, 17:+222->312, 18:+145->235, 19:+145->235, 21:+145->235, 22:+145->235, 32:+124->214, 33:+124->214
    //   196: iload_2
    //   197: istore 6
    //   199: iload_3
    //   200: istore 7
    //   202: aload_0
    //   203: aload_1
    //   204: invokespecial 452	com/android/internal/telephony/uicc/IccRecords:handleMessage	(Landroid/os/Message;)V
    //   207: iload 4
    //   209: istore 8
    //   211: goto +569 -> 780
    //   214: iload_2
    //   215: istore 6
    //   217: iload_3
    //   218: istore 7
    //   220: aload_0
    //   221: aload_1
    //   222: getfield 448	android/os/Message:what	I
    //   225: invokespecial 454	com/android/internal/telephony/uicc/RuimRecords:onLocked	(I)V
    //   228: iload 4
    //   230: istore 8
    //   232: goto +548 -> 780
    //   235: iload_2
    //   236: istore 6
    //   238: iload_3
    //   239: istore 7
    //   241: new 129	java/lang/StringBuilder
    //   244: astore 5
    //   246: iload_2
    //   247: istore 6
    //   249: iload_3
    //   250: istore 7
    //   252: aload 5
    //   254: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   257: iload_2
    //   258: istore 6
    //   260: iload_3
    //   261: istore 7
    //   263: aload 5
    //   265: ldc_w 456
    //   268: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: pop
    //   272: iload_2
    //   273: istore 6
    //   275: iload_3
    //   276: istore 7
    //   278: aload 5
    //   280: aload_1
    //   281: getfield 448	android/os/Message:what	I
    //   284: invokevirtual 222	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   287: pop
    //   288: iload_2
    //   289: istore 6
    //   291: iload_3
    //   292: istore 7
    //   294: ldc 59
    //   296: aload 5
    //   298: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   301: invokestatic 462	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   304: pop
    //   305: iload 4
    //   307: istore 8
    //   309: goto +471 -> 780
    //   312: iload_2
    //   313: istore 6
    //   315: iload_3
    //   316: istore 7
    //   318: aload_0
    //   319: ldc_w 464
    //   322: invokevirtual 148	com/android/internal/telephony/uicc/RuimRecords:log	(Ljava/lang/String;)V
    //   325: iload 4
    //   327: istore 8
    //   329: goto +451 -> 780
    //   332: iload_2
    //   333: istore 6
    //   335: iload_3
    //   336: istore 7
    //   338: aload_1
    //   339: getfield 467	android/os/Message:obj	Ljava/lang/Object;
    //   342: checkcast 286	android/os/AsyncResult
    //   345: astore_1
    //   346: iload 4
    //   348: istore 8
    //   350: iload_2
    //   351: istore 6
    //   353: iload_3
    //   354: istore 7
    //   356: aload_1
    //   357: getfield 471	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   360: ifnull +420 -> 780
    //   363: iload_2
    //   364: istore 6
    //   366: iload_3
    //   367: istore 7
    //   369: ldc 59
    //   371: ldc_w 473
    //   374: aload_1
    //   375: getfield 471	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   378: invokestatic 477	android/telephony/Rlog:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   381: pop
    //   382: iload 4
    //   384: istore 8
    //   386: goto +394 -> 780
    //   389: iload_2
    //   390: istore 6
    //   392: iload_3
    //   393: istore 7
    //   395: aload_1
    //   396: getfield 467	android/os/Message:obj	Ljava/lang/Object;
    //   399: checkcast 286	android/os/AsyncResult
    //   402: astore 5
    //   404: iload_2
    //   405: istore 6
    //   407: iload_3
    //   408: istore 7
    //   410: aload 5
    //   412: getfield 290	android/os/AsyncResult:result	Ljava/lang/Object;
    //   415: checkcast 479	[Ljava/lang/String;
    //   418: astore_1
    //   419: iload_2
    //   420: istore 6
    //   422: iload_3
    //   423: istore 7
    //   425: aload 5
    //   427: getfield 471	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   430: ifnull +10 -> 440
    //   433: iload 4
    //   435: istore 8
    //   437: goto +343 -> 780
    //   440: iload_2
    //   441: istore 6
    //   443: iload_3
    //   444: istore 7
    //   446: aload_0
    //   447: aload_1
    //   448: iconst_0
    //   449: aaload
    //   450: putfield 364	com/android/internal/telephony/uicc/RuimRecords:mMyMobileNumber	Ljava/lang/String;
    //   453: iload_2
    //   454: istore 6
    //   456: iload_3
    //   457: istore 7
    //   459: aload_0
    //   460: aload_1
    //   461: iconst_3
    //   462: aaload
    //   463: putfield 368	com/android/internal/telephony/uicc/RuimRecords:mMin2Min1	Ljava/lang/String;
    //   466: iload_2
    //   467: istore 6
    //   469: iload_3
    //   470: istore 7
    //   472: aload_0
    //   473: aload_1
    //   474: iconst_4
    //   475: aaload
    //   476: putfield 303	com/android/internal/telephony/uicc/RuimRecords:mPrlVersion	Ljava/lang/String;
    //   479: iload_2
    //   480: istore 6
    //   482: iload_3
    //   483: istore 7
    //   485: new 129	java/lang/StringBuilder
    //   488: astore_1
    //   489: iload_2
    //   490: istore 6
    //   492: iload_3
    //   493: istore 7
    //   495: aload_1
    //   496: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   499: iload_2
    //   500: istore 6
    //   502: iload_3
    //   503: istore 7
    //   505: aload_1
    //   506: ldc_w 481
    //   509: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: iload_2
    //   514: istore 6
    //   516: iload_3
    //   517: istore 7
    //   519: aload_1
    //   520: aload_0
    //   521: getfield 364	com/android/internal/telephony/uicc/RuimRecords:mMyMobileNumber	Ljava/lang/String;
    //   524: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   527: pop
    //   528: iload_2
    //   529: istore 6
    //   531: iload_3
    //   532: istore 7
    //   534: aload_1
    //   535: ldc_w 483
    //   538: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   541: pop
    //   542: iload_2
    //   543: istore 6
    //   545: iload_3
    //   546: istore 7
    //   548: aload_1
    //   549: aload_0
    //   550: getfield 368	com/android/internal/telephony/uicc/RuimRecords:mMin2Min1	Ljava/lang/String;
    //   553: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: iload_2
    //   558: istore 6
    //   560: iload_3
    //   561: istore 7
    //   563: aload_0
    //   564: aload_1
    //   565: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   568: invokevirtual 148	com/android/internal/telephony/uicc/RuimRecords:log	(Ljava/lang/String;)V
    //   571: iload 4
    //   573: istore 8
    //   575: goto +205 -> 780
    //   578: iconst_1
    //   579: istore 4
    //   581: iconst_1
    //   582: istore_2
    //   583: iconst_1
    //   584: istore 8
    //   586: iload 4
    //   588: istore 6
    //   590: iload_2
    //   591: istore 7
    //   593: aload_1
    //   594: getfield 467	android/os/Message:obj	Ljava/lang/Object;
    //   597: checkcast 286	android/os/AsyncResult
    //   600: astore 5
    //   602: iload 4
    //   604: istore 6
    //   606: iload_2
    //   607: istore 7
    //   609: aload 5
    //   611: getfield 290	android/os/AsyncResult:result	Ljava/lang/Object;
    //   614: checkcast 291	[B
    //   617: astore_1
    //   618: iload 4
    //   620: istore 6
    //   622: iload_2
    //   623: istore 7
    //   625: aload 5
    //   627: getfield 471	android/os/AsyncResult:exception	Ljava/lang/Throwable;
    //   630: ifnull +6 -> 636
    //   633: goto +147 -> 780
    //   636: iload 4
    //   638: istore 6
    //   640: iload_2
    //   641: istore 7
    //   643: aload_0
    //   644: aload_1
    //   645: iconst_0
    //   646: aload_1
    //   647: arraylength
    //   648: invokestatic 487	com/android/internal/telephony/uicc/IccUtils:bcdToString	([BII)Ljava/lang/String;
    //   651: putfield 490	com/android/internal/telephony/uicc/RuimRecords:mIccId	Ljava/lang/String;
    //   654: iload 4
    //   656: istore 6
    //   658: iload_2
    //   659: istore 7
    //   661: aload_0
    //   662: aload_1
    //   663: iconst_0
    //   664: aload_1
    //   665: arraylength
    //   666: invokestatic 493	com/android/internal/telephony/uicc/IccUtils:bchToString	([BII)Ljava/lang/String;
    //   669: putfield 496	com/android/internal/telephony/uicc/RuimRecords:mFullIccId	Ljava/lang/String;
    //   672: iload 4
    //   674: istore 6
    //   676: iload_2
    //   677: istore 7
    //   679: new 129	java/lang/StringBuilder
    //   682: astore_1
    //   683: iload 4
    //   685: istore 6
    //   687: iload_2
    //   688: istore 7
    //   690: aload_1
    //   691: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   694: iload 4
    //   696: istore 6
    //   698: iload_2
    //   699: istore 7
    //   701: aload_1
    //   702: ldc_w 498
    //   705: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   708: pop
    //   709: iload 4
    //   711: istore 6
    //   713: iload_2
    //   714: istore 7
    //   716: aload_1
    //   717: aload_0
    //   718: getfield 496	com/android/internal/telephony/uicc/RuimRecords:mFullIccId	Ljava/lang/String;
    //   721: invokestatic 504	android/telephony/SubscriptionInfo:givePrintableIccid	(Ljava/lang/String;)Ljava/lang/String;
    //   724: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   727: pop
    //   728: iload 4
    //   730: istore 6
    //   732: iload_2
    //   733: istore 7
    //   735: aload_0
    //   736: aload_1
    //   737: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   740: invokevirtual 148	com/android/internal/telephony/uicc/RuimRecords:log	(Ljava/lang/String;)V
    //   743: goto +37 -> 780
    //   746: iload_2
    //   747: istore 6
    //   749: iload_3
    //   750: istore 7
    //   752: aload_0
    //   753: ldc_w 506
    //   756: invokevirtual 148	com/android/internal/telephony/uicc/RuimRecords:log	(Ljava/lang/String;)V
    //   759: iload 4
    //   761: istore 8
    //   763: goto +17 -> 780
    //   766: iload_2
    //   767: istore 6
    //   769: iload_3
    //   770: istore 7
    //   772: aload_0
    //   773: invokevirtual 509	com/android/internal/telephony/uicc/RuimRecords:onReady	()V
    //   776: iload 4
    //   778: istore 8
    //   780: iload 8
    //   782: ifeq +37 -> 819
    //   785: aload_0
    //   786: invokevirtual 512	com/android/internal/telephony/uicc/RuimRecords:onRecordLoaded	()V
    //   789: goto +30 -> 819
    //   792: astore_1
    //   793: goto +27 -> 820
    //   796: astore_1
    //   797: iload 7
    //   799: istore 6
    //   801: ldc 59
    //   803: ldc_w 514
    //   806: aload_1
    //   807: invokestatic 516	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   810: pop
    //   811: iload 7
    //   813: ifeq +6 -> 819
    //   816: goto -31 -> 785
    //   819: return
    //   820: iload 6
    //   822: ifeq +7 -> 829
    //   825: aload_0
    //   826: invokevirtual 512	com/android/internal/telephony/uicc/RuimRecords:onRecordLoaded	()V
    //   829: aload_1
    //   830: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	831	0	this	RuimRecords
    //   0	831	1	paramMessage	Message
    //   1	766	2	i	int
    //   3	767	3	j	int
    //   5	772	4	k	int
    //   24	602	5	localObject	Object
    //   81	740	6	m	int
    //   84	728	7	n	int
    //   209	572	8	i1	int
    // Exception table:
    //   from	to	target	type
    //   86	196	792	finally
    //   202	207	792	finally
    //   220	228	792	finally
    //   241	246	792	finally
    //   252	257	792	finally
    //   263	272	792	finally
    //   278	288	792	finally
    //   294	305	792	finally
    //   318	325	792	finally
    //   338	346	792	finally
    //   356	363	792	finally
    //   369	382	792	finally
    //   395	404	792	finally
    //   410	419	792	finally
    //   425	433	792	finally
    //   446	453	792	finally
    //   459	466	792	finally
    //   472	479	792	finally
    //   485	489	792	finally
    //   495	499	792	finally
    //   505	513	792	finally
    //   519	528	792	finally
    //   534	542	792	finally
    //   548	557	792	finally
    //   563	571	792	finally
    //   593	602	792	finally
    //   609	618	792	finally
    //   625	633	792	finally
    //   643	654	792	finally
    //   661	672	792	finally
    //   679	683	792	finally
    //   690	694	792	finally
    //   701	709	792	finally
    //   716	728	792	finally
    //   735	743	792	finally
    //   752	759	792	finally
    //   772	776	792	finally
    //   801	811	792	finally
    //   86	196	796	java/lang/RuntimeException
    //   202	207	796	java/lang/RuntimeException
    //   220	228	796	java/lang/RuntimeException
    //   241	246	796	java/lang/RuntimeException
    //   252	257	796	java/lang/RuntimeException
    //   263	272	796	java/lang/RuntimeException
    //   278	288	796	java/lang/RuntimeException
    //   294	305	796	java/lang/RuntimeException
    //   318	325	796	java/lang/RuntimeException
    //   338	346	796	java/lang/RuntimeException
    //   356	363	796	java/lang/RuntimeException
    //   369	382	796	java/lang/RuntimeException
    //   395	404	796	java/lang/RuntimeException
    //   410	419	796	java/lang/RuntimeException
    //   425	433	796	java/lang/RuntimeException
    //   446	453	796	java/lang/RuntimeException
    //   459	466	796	java/lang/RuntimeException
    //   472	479	796	java/lang/RuntimeException
    //   485	489	796	java/lang/RuntimeException
    //   495	499	796	java/lang/RuntimeException
    //   505	513	796	java/lang/RuntimeException
    //   519	528	796	java/lang/RuntimeException
    //   534	542	796	java/lang/RuntimeException
    //   548	557	796	java/lang/RuntimeException
    //   563	571	796	java/lang/RuntimeException
    //   593	602	796	java/lang/RuntimeException
    //   609	618	796	java/lang/RuntimeException
    //   625	633	796	java/lang/RuntimeException
    //   643	654	796	java/lang/RuntimeException
    //   661	672	796	java/lang/RuntimeException
    //   679	683	796	java/lang/RuntimeException
    //   690	694	796	java/lang/RuntimeException
    //   701	709	796	java/lang/RuntimeException
    //   716	728	796	java/lang/RuntimeException
    //   735	743	796	java/lang/RuntimeException
    //   752	759	796	java/lang/RuntimeException
    //   772	776	796	java/lang/RuntimeException
  }
  
  public boolean isProvisioned()
  {
    if (SystemProperties.getBoolean("persist.radio.test-csim", false)) {
      return true;
    }
    if (mParentApp == null) {
      return false;
    }
    return (mParentApp.getType() != IccCardApplicationStatus.AppType.APPTYPE_CSIM) || ((mMdn != null) && (mMin != null));
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[RuimRecords] ");
    localStringBuilder.append(paramString);
    Rlog.d("RuimRecords", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[RuimRecords] ");
    localStringBuilder.append(paramString);
    Rlog.e("RuimRecords", localStringBuilder.toString());
  }
  
  protected void onAllRecordsLoaded()
  {
    log("record load complete");
    if (Resources.getSystem().getBoolean(17957072)) {
      setSimLanguage(mEFli, mEFpl);
    }
    mLoaded.set(true);
    mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    if (!TextUtils.isEmpty(mMdn))
    {
      int i = mParentApp.getUiccProfile().getPhoneId();
      i = SubscriptionController.getInstance().getSubIdUsingPhoneId(i);
      if (SubscriptionManager.isValidSubscriptionId(i)) {
        SubscriptionManager.from(mContext).setDisplayNumber(mMdn, i);
      } else {
        log("Cannot call setDisplayNumber: invalid subId");
      }
    }
  }
  
  public void onReady()
  {
    fetchRuimRecords();
    mCi.getCDMASubscription(obtainMessage(10));
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
      fetchRuimRecords();
    }
  }
  
  protected void resetRecords()
  {
    mMncLength = -1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setting0 mMncLength");
    localStringBuilder.append(mMncLength);
    log(localStringBuilder.toString());
    mIccId = null;
    mFullIccId = null;
    mAdnCache.reset();
    mRecordsRequested = false;
    mLockedRecordsReqReason = 0;
    mLoaded.set(false);
  }
  
  public void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage)
  {
    forMessageexception = new IccException("setVoiceMailNumber not implemented");
    paramMessage.sendToTarget();
    loge("method setVoiceMailNumber is not implemented");
  }
  
  public void setVoiceMessageWaiting(int paramInt1, int paramInt2)
  {
    log("RuimRecords:setVoiceMessageWaiting - NOP for CDMA");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RuimRecords: ");
    localStringBuilder.append(super.toString());
    localStringBuilder.append(" m_ota_commited");
    localStringBuilder.append(mOtaCommited);
    localStringBuilder.append(" mMyMobileNumber=xxxx mMin2Min1=");
    localStringBuilder.append(mMin2Min1);
    localStringBuilder.append(" mPrlVersion=");
    localStringBuilder.append(mPrlVersion);
    localStringBuilder.append(" mEFpl=");
    localStringBuilder.append(mEFpl);
    localStringBuilder.append(" mEFli=");
    localStringBuilder.append(mEFli);
    localStringBuilder.append(" mCsimSpnDisplayCondition=");
    localStringBuilder.append(mCsimSpnDisplayCondition);
    localStringBuilder.append(" mMdn=");
    localStringBuilder.append(mMdn);
    localStringBuilder.append(" mMin=");
    localStringBuilder.append(mMin);
    localStringBuilder.append(" mHomeSystemId=");
    localStringBuilder.append(mHomeSystemId);
    localStringBuilder.append(" mHomeNetworkId=");
    localStringBuilder.append(mHomeNetworkId);
    return localStringBuilder.toString();
  }
  
  private class EfCsimCdmaHomeLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimCdmaHomeLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_CDMAHOME";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      Object localObject1 = (ArrayList)result;
      Object localObject2 = RuimRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("CSIM_CDMAHOME data size=");
      paramAsyncResult.append(((ArrayList)localObject1).size());
      ((RuimRecords)localObject2).log(paramAsyncResult.toString());
      if (((ArrayList)localObject1).isEmpty()) {
        return;
      }
      paramAsyncResult = new StringBuilder();
      localObject2 = new StringBuilder();
      Iterator localIterator = ((ArrayList)localObject1).iterator();
      while (localIterator.hasNext())
      {
        localObject1 = (byte[])localIterator.next();
        if (localObject1.length == 5)
        {
          int i = localObject1[1];
          int j = localObject1[0];
          int k = localObject1[3];
          int m = localObject1[2];
          paramAsyncResult.append((i & 0xFF) << 8 | j & 0xFF);
          paramAsyncResult.append(',');
          ((StringBuilder)localObject2).append((k & 0xFF) << 8 | m & 0xFF);
          ((StringBuilder)localObject2).append(',');
        }
      }
      paramAsyncResult.setLength(paramAsyncResult.length() - 1);
      ((StringBuilder)localObject2).setLength(((StringBuilder)localObject2).length() - 1);
      RuimRecords.access$502(RuimRecords.this, paramAsyncResult.toString());
      RuimRecords.access$602(RuimRecords.this, ((StringBuilder)localObject2).toString());
    }
  }
  
  private class EfCsimEprlLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimEprlLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_EPRL";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      RuimRecords.this.onGetCSimEprlDone(paramAsyncResult);
    }
  }
  
  private class EfCsimImsimLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimImsimLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_IMSIM";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (byte[])result;
      if ((paramAsyncResult != null) && (paramAsyncResult.length >= 10))
      {
        localObject1 = RuimRecords.this;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("CSIM_IMSIM=");
        ((StringBuilder)localObject2).append(IccUtils.bytesToHexString(paramAsyncResult));
        ((RuimRecords)localObject1).log(((StringBuilder)localObject2).toString());
        int i;
        if ((paramAsyncResult[7] & 0x80) == 128) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0)
        {
          mImsi = RuimRecords.this.decodeImsi(paramAsyncResult);
          if (mImsi != null) {
            RuimRecords.access$302(RuimRecords.this, mImsi.substring(5, 15));
          }
          paramAsyncResult = RuimRecords.this;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("IMSI: ");
          ((StringBuilder)localObject1).append(mImsi.substring(0, 5));
          ((StringBuilder)localObject1).append("xxxxxxxxx");
          paramAsyncResult.log(((StringBuilder)localObject1).toString());
        }
        else
        {
          log("IMSI not provisioned in card");
        }
        paramAsyncResult = getOperatorNumeric();
        if ((paramAsyncResult != null) && (paramAsyncResult.length() <= 6)) {
          MccTable.updateMccMncConfiguration(mContext, paramAsyncResult, false);
        }
        mImsiReadyRegistrants.notifyRegistrants();
        return;
      }
      Object localObject2 = RuimRecords.this;
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Invalid IMSI from EF_CSIM_IMSIM ");
      ((StringBuilder)localObject1).append(IccUtils.bytesToHexString(paramAsyncResult));
      ((RuimRecords)localObject2).log(((StringBuilder)localObject1).toString());
      mImsi = null;
      RuimRecords.access$302(RuimRecords.this, null);
    }
  }
  
  private class EfCsimLiLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimLiLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_LI";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      RuimRecords.access$102(RuimRecords.this, (byte[])result);
      for (int i = 0; i < mEFli.length; i += 2) {
        switch (mEFli[(i + 1)])
        {
        default: 
          mEFli[i] = ((byte)32);
          mEFli[(i + 1)] = ((byte)32);
          break;
        case 7: 
          mEFli[i] = ((byte)104);
          mEFli[(i + 1)] = ((byte)101);
          break;
        case 6: 
          mEFli[i] = ((byte)122);
          mEFli[(i + 1)] = ((byte)104);
          break;
        case 5: 
          mEFli[i] = ((byte)107);
          mEFli[(i + 1)] = ((byte)111);
          break;
        case 4: 
          mEFli[i] = ((byte)106);
          mEFli[(i + 1)] = ((byte)97);
          break;
        case 3: 
          mEFli[i] = ((byte)101);
          mEFli[(i + 1)] = ((byte)115);
          break;
        case 2: 
          mEFli[i] = ((byte)102);
          mEFli[(i + 1)] = ((byte)114);
          break;
        case 1: 
          mEFli[i] = ((byte)101);
          mEFli[(i + 1)] = ((byte)110);
        }
      }
      RuimRecords localRuimRecords = RuimRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("EF_LI=");
      paramAsyncResult.append(IccUtils.bytesToHexString(mEFli));
      localRuimRecords.log(paramAsyncResult.toString());
    }
  }
  
  private class EfCsimMdnLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimMdnLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_MDN";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      Object localObject = (byte[])result;
      paramAsyncResult = RuimRecords.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CSIM_MDN=");
      localStringBuilder.append(IccUtils.bytesToHexString((byte[])localObject));
      paramAsyncResult.log(localStringBuilder.toString());
      int i = localObject[0];
      RuimRecords.access$202(RuimRecords.this, IccUtils.cdmaBcdToString((byte[])localObject, 1, i & 0xF));
      localObject = RuimRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("CSIM MDN=");
      paramAsyncResult.append(mMdn);
      ((RuimRecords)localObject).log(paramAsyncResult.toString());
    }
  }
  
  private class EfCsimMipUppLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimMipUppLoaded() {}
    
    boolean checkLengthLegal(int paramInt1, int paramInt2)
    {
      if (paramInt1 < paramInt2)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("CSIM MIPUPP format error, length = ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append("expected length at least =");
        localStringBuilder.append(paramInt2);
        Log.e("RuimRecords", localStringBuilder.toString());
        return false;
      }
      return true;
    }
    
    public String getEfName()
    {
      return "EF_CSIM_MIPUPP";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (byte[])result;
      if (paramAsyncResult.length < 1)
      {
        Log.e("RuimRecords", "MIPUPP read error");
        return;
      }
      Object localObject = new BitwiseInputStream(paramAsyncResult);
      try
      {
        int i = ((BitwiseInputStream)localObject).read(8) << 3;
        if (!checkLengthLegal(i, 1)) {
          return;
        }
        int j = ((BitwiseInputStream)localObject).read(1);
        int k = i - 1;
        i = k;
        if (j == 1)
        {
          if (!checkLengthLegal(k, 11)) {
            return;
          }
          ((BitwiseInputStream)localObject).skip(11);
          i = k - 11;
        }
        if (!checkLengthLegal(i, 4)) {
          return;
        }
        int m = ((BitwiseInputStream)localObject).read(4);
        j = 0;
        i -= 4;
        for (k = 0; k < m; k++)
        {
          if (!checkLengthLegal(i, 4)) {
            return;
          }
          int n = ((BitwiseInputStream)localObject).read(4);
          i -= 4;
          if (!checkLengthLegal(i, 8)) {
            return;
          }
          int i1 = ((BitwiseInputStream)localObject).read(8);
          i -= 8;
          if (n == 0)
          {
            if (!checkLengthLegal(i, i1 << 3)) {
              return;
            }
            paramAsyncResult = new char[i1];
            for (i = j; i < i1; i++) {
              paramAsyncResult[i] = ((char)(char)(((BitwiseInputStream)localObject).read(8) & 0xFF));
            }
            RuimRecords localRuimRecords = RuimRecords.this;
            localObject = new java/lang/String;
            ((String)localObject).<init>(paramAsyncResult);
            RuimRecords.access$802(localRuimRecords, (String)localObject);
            if (Log.isLoggable("RuimRecords", 2))
            {
              paramAsyncResult = new java/lang/StringBuilder;
              paramAsyncResult.<init>();
              paramAsyncResult.append("MIPUPP Nai = ");
              paramAsyncResult.append(mNai);
              Log.v("RuimRecords", paramAsyncResult.toString());
            }
            return;
          }
          if (!checkLengthLegal(i, (i1 << 3) + 102)) {
            return;
          }
          ((BitwiseInputStream)localObject).skip((i1 << 3) + 101);
          n = ((BitwiseInputStream)localObject).read(1);
          i1 = i - ((i1 << 3) + 102);
          i = i1;
          if (n == 1)
          {
            if (!checkLengthLegal(i1, 32)) {
              return;
            }
            ((BitwiseInputStream)localObject).skip(32);
            i = i1 - 32;
          }
          if (!checkLengthLegal(i, 5)) {
            return;
          }
          ((BitwiseInputStream)localObject).skip(4);
          n = ((BitwiseInputStream)localObject).read(1);
          i1 = i - 4 - 1;
          i = i1;
          if (n == 1)
          {
            if (!checkLengthLegal(i1, 32)) {
              return;
            }
            ((BitwiseInputStream)localObject).skip(32);
            i = i1 - 32;
          }
        }
        return;
      }
      catch (Exception paramAsyncResult)
      {
        Log.e("RuimRecords", "MIPUPP read Exception error!");
      }
    }
  }
  
  private class EfCsimSpnLoaded
    implements IccRecords.IccRecordLoaded
  {
    private EfCsimSpnLoaded() {}
    
    public String getEfName()
    {
      return "EF_CSIM_SPN";
    }
    
    public void onRecordLoaded(AsyncResult paramAsyncResult)
    {
      byte[] arrayOfByte = (byte[])result;
      paramAsyncResult = RuimRecords.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("CSIM_SPN=");
      ((StringBuilder)localObject2).append(IccUtils.bytesToHexString(arrayOfByte));
      paramAsyncResult.log(((StringBuilder)localObject2).toString());
      paramAsyncResult = RuimRecords.this;
      boolean bool;
      if ((arrayOfByte[0] & 0x1) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mCsimSpnDisplayCondition = bool;
      int i = arrayOfByte[1];
      int j = arrayOfByte[2];
      j = 32;
      paramAsyncResult = new byte[32];
      if (arrayOfByte.length - 3 < 32) {
        j = arrayOfByte.length - 3;
      }
      System.arraycopy(arrayOfByte, 3, paramAsyncResult, 0, j);
      for (j = 0; (j < paramAsyncResult.length) && ((paramAsyncResult[j] & 0xFF) != 255); j++) {}
      if (j == 0)
      {
        setServiceProviderName("");
        return;
      }
      if (i != 0)
      {
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            try
            {
              log("SPN encoding not supported");
            }
            catch (Exception localException)
            {
              break label416;
            }
          }
        case 4: 
          localObject1 = RuimRecords.this;
          localObject2 = new java/lang/String;
          ((String)localObject2).<init>(paramAsyncResult, 0, j, "utf-16");
          ((RuimRecords)localObject1).setServiceProviderName((String)localObject2);
          break;
        case 3: 
          setServiceProviderName(GsmAlphabet.gsm7BitPackedToString(paramAsyncResult, 0, j * 8 / 7));
          break;
        case 2: 
          String str = new java/lang/String;
          str.<init>(paramAsyncResult, 0, j, "US-ASCII");
          if (TextUtils.isPrintableAsciiOnly(str))
          {
            setServiceProviderName(str);
          }
          else
          {
            localObject2 = RuimRecords.this;
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Some corruption in SPN decoding = ");
            ((StringBuilder)localObject1).append(str);
            ((RuimRecords)localObject2).log(((StringBuilder)localObject1).toString());
            log("Using ENCODING_GSM_7BIT_ALPHABET scheme...");
            setServiceProviderName(GsmAlphabet.gsm7BitPackedToString(paramAsyncResult, 0, j * 8 / 7));
          }
          break;
        }
      }
      else
      {
        localObject1 = RuimRecords.this;
        localObject2 = new java/lang/String;
        ((String)localObject2).<init>(paramAsyncResult, 0, j, "ISO-8859-1");
        ((RuimRecords)localObject1).setServiceProviderName((String)localObject2);
      }
      break label450;
      label416:
      localObject2 = RuimRecords.this;
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("spn decode error: ");
      paramAsyncResult.append(localObject1);
      ((RuimRecords)localObject2).log(paramAsyncResult.toString());
      label450:
      paramAsyncResult = RuimRecords.this;
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("spn=");
      ((StringBuilder)localObject1).append(getServiceProviderName());
      paramAsyncResult.log(((StringBuilder)localObject1).toString());
      paramAsyncResult = RuimRecords.this;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("spnCondition=");
      ((StringBuilder)localObject1).append(mCsimSpnDisplayCondition);
      paramAsyncResult.log(((StringBuilder)localObject1).toString());
      mTelephonyManager.setSimOperatorNameForPhone(mParentApp.getPhoneId(), getServiceProviderName());
    }
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
      RuimRecords.access$002(RuimRecords.this, (byte[])result);
      paramAsyncResult = RuimRecords.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("EF_PL=");
      localStringBuilder.append(IccUtils.bytesToHexString(mEFpl));
      paramAsyncResult.log(localStringBuilder.toString());
    }
  }
}
