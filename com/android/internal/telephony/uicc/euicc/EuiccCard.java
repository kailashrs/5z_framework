package com.android.internal.telephony.uicc.euicc;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Registrant;
import android.os.RegistrantList;
import android.service.carrier.CarrierIdentifier;
import android.service.euicc.EuiccProfileInfo;
import android.service.euicc.EuiccProfileInfo.Builder;
import android.telephony.Rlog;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.EuiccNotification;
import android.telephony.euicc.EuiccRulesAuthTable;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.asn1.Asn1Decoder;
import com.android.internal.telephony.uicc.asn1.Asn1Node;
import com.android.internal.telephony.uicc.asn1.Asn1Node.Builder;
import com.android.internal.telephony.uicc.asn1.InvalidAsn1DataException;
import com.android.internal.telephony.uicc.asn1.TagNotFoundException;
import com.android.internal.telephony.uicc.euicc.apdu.ApduSender;
import com.android.internal.telephony.uicc.euicc.apdu.RequestBuilder;
import com.android.internal.telephony.uicc.euicc.apdu.RequestProvider;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultHelper;
import java.util.Arrays;
import java.util.List;

public class EuiccCard
  extends UiccCard
{
  private static final int APDU_ERROR_SIM_REFRESH = 28416;
  private static final int CODE_NOTHING_TO_DELETE = 1;
  private static final int CODE_NO_RESULT_AVAILABLE = 1;
  private static final int CODE_OK = 0;
  private static final int CODE_PROFILE_NOT_IN_EXPECTED_STATE = 2;
  private static final boolean DBG = true;
  private static final String DEV_CAP_CDMA_1X = "cdma1x";
  private static final String DEV_CAP_CRL = "crl";
  private static final String DEV_CAP_EHRPD = "ehrpd";
  private static final String DEV_CAP_EUTRAN = "eutran";
  private static final String DEV_CAP_GSM = "gsm";
  private static final String DEV_CAP_HRPD = "hrpd";
  private static final String DEV_CAP_NFC = "nfc";
  private static final String DEV_CAP_UTRAN = "utran";
  private static final int ICCID_LENGTH = 20;
  private static final String ISD_R_AID = "A0000005591010FFFFFFFF8900000100";
  private static final String LOG_TAG = "EuiccCard";
  private static final EuiccSpecVersion SGP_2_0 = new EuiccSpecVersion(2, 0, 0);
  private final ApduSender mApduSender;
  private volatile String mEid;
  private RegistrantList mEidReadyRegistrants;
  private final Object mLock = new Object();
  private EuiccSpecVersion mSpecVersion;
  
  public EuiccCard(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus, int paramInt, Object paramObject)
  {
    super(paramContext, paramCommandsInterface, paramIccCardStatus, paramInt, paramObject);
    mApduSender = new ApduSender(paramCommandsInterface, "A0000005591010FFFFFFFF8900000100", false);
    loadEidAndNotifyRegistrants();
  }
  
  private static CarrierIdentifier buildCarrierIdentifier(Asn1Node paramAsn1Node)
    throws InvalidAsn1DataException, TagNotFoundException
  {
    String str1 = null;
    if (paramAsn1Node.hasChild(129, new int[0])) {
      str1 = IccUtils.bytesToHexString(paramAsn1Node.getChild(129, new int[0]).asBytes());
    }
    String str2 = null;
    if (paramAsn1Node.hasChild(130, new int[0])) {
      str2 = IccUtils.bytesToHexString(paramAsn1Node.getChild(130, new int[0]).asBytes());
    }
    return new CarrierIdentifier(paramAsn1Node.getChild(128, new int[0]).asBytes(), str1, str2);
  }
  
  private static void buildProfile(Asn1Node paramAsn1Node, EuiccProfileInfo.Builder paramBuilder)
    throws TagNotFoundException, InvalidAsn1DataException
  {
    if (paramAsn1Node.hasChild(144, new int[0])) {
      paramBuilder.setNickname(paramAsn1Node.getChild(144, new int[0]).asString());
    }
    if (paramAsn1Node.hasChild(145, new int[0])) {
      paramBuilder.setServiceProviderName(paramAsn1Node.getChild(145, new int[0]).asString());
    }
    if (paramAsn1Node.hasChild(146, new int[0])) {
      paramBuilder.setProfileName(paramAsn1Node.getChild(146, new int[0]).asString());
    }
    if (paramAsn1Node.hasChild(183, new int[0])) {
      paramBuilder.setCarrierIdentifier(buildCarrierIdentifier(paramAsn1Node.getChild(183, new int[0])));
    }
    if (paramAsn1Node.hasChild(40816, new int[0])) {
      paramBuilder.setState(paramAsn1Node.getChild(40816, new int[0]).asInteger());
    } else {
      paramBuilder.setState(0);
    }
    if (paramAsn1Node.hasChild(149, new int[0])) {
      paramBuilder.setProfileClass(paramAsn1Node.getChild(149, new int[0]).asInteger());
    } else {
      paramBuilder.setProfileClass(2);
    }
    if (paramAsn1Node.hasChild(153, new int[0])) {
      paramBuilder.setPolicyRules(paramAsn1Node.getChild(153, new int[0]).asBits());
    }
    if (paramAsn1Node.hasChild(49014, new int[0]))
    {
      UiccAccessRule[] arrayOfUiccAccessRule = buildUiccAccessRule(paramAsn1Node.getChild(49014, new int[0]).getChildren(226));
      paramAsn1Node = null;
      if (arrayOfUiccAccessRule != null) {
        paramAsn1Node = Arrays.asList(arrayOfUiccAccessRule);
      }
      paramBuilder.setUiccAccessRule(paramAsn1Node);
    }
  }
  
  private static UiccAccessRule[] buildUiccAccessRule(List<Asn1Node> paramList)
    throws InvalidAsn1DataException, TagNotFoundException
  {
    if (paramList.isEmpty()) {
      return null;
    }
    int i = paramList.size();
    UiccAccessRule[] arrayOfUiccAccessRule = new UiccAccessRule[i];
    for (int j = 0; j < i; j++)
    {
      Asn1Node localAsn1Node1 = (Asn1Node)paramList.get(j);
      Asn1Node localAsn1Node2 = localAsn1Node1.getChild(225, new int[0]);
      byte[] arrayOfByte = localAsn1Node2.getChild(193, new int[0]).asBytes();
      String str = null;
      if (localAsn1Node2.hasChild(202, new int[0])) {
        str = localAsn1Node2.getChild(202, new int[0]).asString();
      }
      long l = 0L;
      if (localAsn1Node1.hasChild(227, new int[] { 219 })) {
        l = localAsn1Node1.getChild(227, new int[] { 219 }).asRawLong();
      }
      arrayOfUiccAccessRule[j] = new UiccAccessRule(arrayOfByte, str, l);
    }
    return arrayOfUiccAccessRule;
  }
  
  private static EuiccNotification createNotification(Asn1Node paramAsn1Node)
    throws TagNotFoundException, InvalidAsn1DataException
  {
    Asn1Node localAsn1Node;
    if (paramAsn1Node.getTag() == 48943) {
      localAsn1Node = paramAsn1Node;
    }
    for (;;)
    {
      break;
      if (paramAsn1Node.getTag() == 48951) {
        localAsn1Node = paramAsn1Node.getChild(48935, new int[] { 48943 });
      } else {
        localAsn1Node = paramAsn1Node.getChild(48943, new int[0]);
      }
    }
    int i = localAsn1Node.getChild(128, new int[0]).asInteger();
    String str = localAsn1Node.getChild(12, new int[0]).asString();
    int j = localAsn1Node.getChild(129, new int[0]).asBits();
    if (paramAsn1Node.getTag() == 48943) {
      paramAsn1Node = null;
    } else {
      paramAsn1Node = paramAsn1Node.toBytes();
    }
    return new EuiccNotification(i, str, j, paramAsn1Node);
  }
  
  private EuiccSpecVersion getOrExtractSpecVersion(byte[] arg1)
  {
    if (mSpecVersion != null) {
      return mSpecVersion;
    }
    EuiccSpecVersion localEuiccSpecVersion1 = EuiccSpecVersion.fromOpenChannelResponse(???);
    if (localEuiccSpecVersion1 != null) {
      synchronized (mLock)
      {
        if (mSpecVersion == null) {
          mSpecVersion = localEuiccSpecVersion1;
        }
      }
    }
    return localEuiccSpecVersion2;
  }
  
  private static void logd(String paramString)
  {
    Rlog.d("EuiccCard", paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e("EuiccCard", paramString);
  }
  
  private static void loge(String paramString, Throwable paramThrowable)
  {
    Rlog.e("EuiccCard", paramString, paramThrowable);
  }
  
  private static void logi(String paramString)
  {
    Rlog.i("EuiccCard", paramString);
  }
  
  private RequestProvider newRequestProvider(ApduRequestBuilder paramApduRequestBuilder)
  {
    return new _..Lambda.EuiccCard.7vWsDgJ3RMY6kHsGeBw_CxIKViI(this, paramApduRequestBuilder);
  }
  
  private static String padTrailingFs(String paramString)
  {
    Object localObject = paramString;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject = paramString;
      if (paramString.length() < 20)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(new String(new char[20 - paramString.length()]).replace('\000', 'F'));
        localObject = ((StringBuilder)localObject).toString();
      }
    }
    return localObject;
  }
  
  private static Asn1Node parseResponse(byte[] paramArrayOfByte)
    throws EuiccCardException, InvalidAsn1DataException
  {
    paramArrayOfByte = new Asn1Decoder(paramArrayOfByte);
    if (paramArrayOfByte.hasNextNode()) {
      return paramArrayOfByte.nextNode();
    }
    throw new EuiccCardException("Empty response", null);
  }
  
  private static Asn1Node parseResponseAndCheckSimpleError(byte[] paramArrayOfByte, int paramInt)
    throws EuiccCardException, InvalidAsn1DataException, TagNotFoundException
  {
    paramArrayOfByte = parseResponse(paramArrayOfByte);
    if (!paramArrayOfByte.hasChild(129, new int[0])) {
      return paramArrayOfByte;
    }
    throw new EuiccCardErrorException(paramInt, paramArrayOfByte.getChild(129, new int[0]).asInteger());
  }
  
  private static int parseSimpleResult(byte[] paramArrayOfByte)
    throws EuiccCardException, TagNotFoundException, InvalidAsn1DataException
  {
    return parseResponse(paramArrayOfByte).getChild(128, new int[0]).asInteger();
  }
  
  private <T> void sendApdu(RequestProvider paramRequestProvider, final ApduResponseHandler<T> paramApduResponseHandler, final ApduExceptionHandler paramApduExceptionHandler, final AsyncResultCallback<T> paramAsyncResultCallback, Handler paramHandler)
  {
    mApduSender.send(paramRequestProvider, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        paramApduExceptionHandler.handleException(paramAnonymousThrowable);
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramAsyncResultCallback.onResult(paramApduResponseHandler.handleResult(paramAnonymousArrayOfByte));
        }
        catch (InvalidAsn1DataException|TagNotFoundException localInvalidAsn1DataException)
        {
          AsyncResultCallback localAsyncResultCallback = paramAsyncResultCallback;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Cannot parse response: ");
          localStringBuilder.append(IccUtils.bytesToHexString(paramAnonymousArrayOfByte));
          localAsyncResultCallback.onException(new EuiccCardException(localStringBuilder.toString(), localInvalidAsn1DataException));
        }
        catch (EuiccCardException paramAnonymousArrayOfByte)
        {
          paramAsyncResultCallback.onException(paramAnonymousArrayOfByte);
        }
      }
    }, paramHandler);
  }
  
  private <T> void sendApdu(RequestProvider paramRequestProvider, ApduResponseHandler<T> paramApduResponseHandler, AsyncResultCallback<T> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(paramRequestProvider, paramApduResponseHandler, new _..Lambda.EuiccCard.L4YPgLjdI8c0_VHmXQ199X1DICE(paramAsyncResultCallback), paramAsyncResultCallback, paramHandler);
  }
  
  private void sendApduWithSimResetErrorWorkaround(RequestProvider paramRequestProvider, ApduResponseHandler<Void> paramApduResponseHandler, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(paramRequestProvider, paramApduResponseHandler, new _..Lambda.EuiccCard.jFzxc6nh3bkdLVyMXzM3mlnBqrA(paramAsyncResultCallback), paramAsyncResultCallback, paramHandler);
  }
  
  private static String stripTrailingFs(byte[] paramArrayOfByte)
  {
    return IccUtils.stripTrailingFs(IccUtils.bchToString(paramArrayOfByte, 0, paramArrayOfByte.length));
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void addDeviceCapability(Asn1Node.Builder paramBuilder, String paramString)
  {
    Object localObject = paramString.split(",");
    int i = localObject.length;
    int j = 2;
    if (i != 2)
    {
      paramBuilder = new StringBuilder();
      paramBuilder.append("Invalid device capability item: ");
      paramBuilder.append(Arrays.toString((Object[])localObject));
      loge(paramBuilder.toString());
      return;
    }
    paramString = localObject[0].trim();
    try
    {
      i = Integer.parseInt(localObject[1].trim());
      localObject = new byte[3];
      localObject[0] = Integer.valueOf(i).byteValue();
      localObject[1] = ((byte)0);
      localObject[2] = ((byte)0);
      switch (paramString.hashCode())
      {
      default: 
        break;
      case 111620384: 
        if (paramString.equals("utran")) {
          j = 1;
        }
        break;
      case 96487011: 
        if (paramString.equals("ehrpd")) {
          j = 4;
        }
        break;
      case 3211390: 
        if (paramString.equals("hrpd")) {
          j = 3;
        }
        break;
      case 108971: 
        if (paramString.equals("nfc")) {
          j = 6;
        }
        break;
      case 102657: 
        if (paramString.equals("gsm")) {
          j = 0;
        }
        break;
      case 98781: 
        if (paramString.equals("crl")) {
          j = 7;
        }
        break;
      case -1291802661: 
        if (paramString.equals("eutran")) {
          j = 5;
        }
        break;
      case -1364987172: 
        if (paramString.equals("cdma1x")) {
          break label301;
        }
      }
      j = -1;
      switch (j)
      {
      default: 
        paramBuilder = new StringBuilder();
        paramBuilder.append("Invalid device capability name: ");
        paramBuilder.append(paramString);
        loge(paramBuilder.toString());
        break;
      case 7: 
        paramBuilder.addChildAsBytes(135, (byte[])localObject);
        break;
      case 6: 
        paramBuilder.addChildAsBytes(134, (byte[])localObject);
        break;
      case 5: 
        paramBuilder.addChildAsBytes(133, (byte[])localObject);
        break;
      case 4: 
        paramBuilder.addChildAsBytes(132, (byte[])localObject);
        break;
      case 3: 
        paramBuilder.addChildAsBytes(131, (byte[])localObject);
        break;
      case 2: 
        paramBuilder.addChildAsBytes(130, (byte[])localObject);
        break;
      case 1: 
        paramBuilder.addChildAsBytes(129, (byte[])localObject);
        break;
      case 0: 
        label301:
        paramBuilder.addChildAsBytes(128, (byte[])localObject);
      }
      return;
    }
    catch (NumberFormatException paramBuilder)
    {
      loge("Invalid device capability version number.", paramBuilder);
    }
  }
  
  public void authenticateServer(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.dXiSnJocvC7r6HwRUJlZI7Qnleo(this, paramString, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4)), _..Lambda.EuiccCard.MRlmz2j6osUyi5hGvD3j9D4Tsrg.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void cancelSession(byte[] paramArrayOfByte, int paramInt, AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.mf0dWT4hLdKlsLFFHVfdGFxHyX0(paramArrayOfByte, paramInt)), _..Lambda.EuiccCard.ItNER0v0H8BgPYIx3JhINdI9slE.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void deleteProfile(String paramString, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.MoRNAw8O6kYG_c2AJkozlJwO2NM(paramString)), _..Lambda.EuiccCard.6M0Cvkh43ith8i9YF2YZNZ_YvOM.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void disableProfile(String paramString, boolean paramBoolean, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApduWithSimResetErrorWorkaround(newRequestProvider(new _..Lambda.EuiccCard.0N6_V0pqmnTfKxVMU5IUj_svXDA(paramString, paramBoolean)), new _..Lambda.EuiccCard.Rc41c7zRLip3RrHuKqZ_Sv7h8wI(paramString), paramAsyncResultCallback, paramHandler);
  }
  
  public void getAllProfiles(AsyncResultCallback<EuiccProfileInfo[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.toN63DWLt72dzp0WCl28UOMSmzE.INSTANCE), _..Lambda.EuiccCard.B99bQ_FkeD9OwB8_qTcKScitlrM.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void getDefaultSmdpAddress(AsyncResultCallback<String> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.3LRPBN7jGieBA4qKqsiYoON1xT0.INSTANCE), _..Lambda.EuiccCard.Qej04bOzl5rj_T7NIjvbnJX7b2s.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  protected byte[] getDeviceId()
  {
    byte[] arrayOfByte = new byte[8];
    Phone localPhone = PhoneFactory.getPhone(getPhoneId());
    if (localPhone != null) {
      IccUtils.bcdToBytes(localPhone.getDeviceId(), arrayOfByte);
    }
    return arrayOfByte;
  }
  
  public String getEid()
  {
    return mEid;
  }
  
  public void getEid(AsyncResultCallback<String> paramAsyncResultCallback, Handler paramHandler)
  {
    if (mEid != null)
    {
      AsyncResultHelper.returnResult(mEid, paramAsyncResultCallback, paramHandler);
      return;
    }
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.HBn5KBGylwjLqIEm3rBhXnUU_8U.INSTANCE), new _..Lambda.EuiccCard.okradEAowCk8rNBK1OaJIA6l6eA(this), paramAsyncResultCallback, paramHandler);
  }
  
  public void getEuiccChallenge(AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.8wofF_Li1V6a8rJQc_M2IGeJ26E.INSTANCE), _..Lambda.EuiccCard.AGpR_ArLREPF7xVOCf0sgHwbDtA.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void getEuiccInfo1(AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.WE7TDTe507w4dBh1UvCgBgp3xVk.INSTANCE), _..Lambda.EuiccCard.hCCBghNOkOgvjeYe8LWQml6I9Ow.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void getEuiccInfo2(AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.UxQlywWQ3cqQ7G7vS2KuMEwtYro.INSTANCE), _..Lambda.EuiccCard.X8OWFy8Bi7TMh117x6vCBqzSqVY.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public final void getProfile(String paramString, AsyncResultCallback<EuiccProfileInfo> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.QGtQZCF6KEnI_x59_tp1eo8mWew(paramString)), _..Lambda.EuiccCard.TTvsStUIyUFrPpvGTlsjBCy3NyM.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  protected Resources getResources()
  {
    return Resources.getSystem();
  }
  
  public void getRulesAuthTable(AsyncResultCallback<EuiccRulesAuthTable> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.AWltG4uFbHn2Xq7ZPpU3U1qOqVM.INSTANCE), _..Lambda.EuiccCard.IMmMA3gSh1g8aaHsYtCih61EKmo.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void getSmdsAddress(AsyncResultCallback<String> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.tPSWjOKtm9yQg21kHmLX49PPf_4.INSTANCE), _..Lambda.EuiccCard.u2_6zCuoZP9CLxIS2g4BREHHECI.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void getSpecVersion(AsyncResultCallback<EuiccSpecVersion> paramAsyncResultCallback, Handler paramHandler)
  {
    if (mSpecVersion != null)
    {
      AsyncResultHelper.returnResult(mSpecVersion, paramAsyncResultCallback, paramHandler);
      return;
    }
    sendApdu(newRequestProvider(_..Lambda.EuiccCard.HgCDP54gCppk81aqhuCG0YGJWEc.INSTANCE), new _..Lambda.EuiccCard.U1ORE3W_o_HdXWc6N59UnRQmLQI(this), paramAsyncResultCallback, paramHandler);
  }
  
  public void listNotifications(int paramInt, AsyncResultCallback<EuiccNotification[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.Ktn9yHrkajo1XOdBNZaiNkYG4vA(paramInt)), _..Lambda.EuiccCard.nNX2R6O4UzJoFix96ifwgIx0npQ.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void loadBoundProfilePackage(byte[] paramArrayOfByte, AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.XDNTzAU_9I92HztVAJQr4NXR3DU(paramArrayOfByte)), _..Lambda.EuiccCard.g0LHcTcRLtF0WE8Tyv2BvipGgrM.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  protected void loadEidAndNotifyRegistrants()
  {
    Handler localHandler = new Handler();
    getEid(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        if (mEidReadyRegistrants != null) {
          mEidReadyRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
        }
        EuiccCard.access$102(EuiccCard.this, "");
        EuiccCard.access$202(EuiccCard.this, "");
        Rlog.e("EuiccCard", "Failed loading eid", paramAnonymousThrowable);
      }
      
      public void onResult(String paramAnonymousString)
      {
        if (mEidReadyRegistrants != null) {
          mEidReadyRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
        }
      }
    }, localHandler);
  }
  
  public void prepareDownload(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, AsyncResultCallback<byte[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.5wK_r0z9fLtA1ZRVlbk3WfOYXJI(paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte1, paramArrayOfByte4)), _..Lambda.EuiccCard.v0S5B6MBAksDVSST9c1nk2Movvk.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void registerForEidReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    if (mEid != null)
    {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
    else
    {
      if (mEidReadyRegistrants == null) {
        mEidReadyRegistrants = new RegistrantList();
      }
      mEidReadyRegistrants.add(paramHandler);
    }
  }
  
  public void removeNotificationFromList(int paramInt, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.7VXOA_y5ZAskOFBWhqVLPntT7K0(paramInt)), _..Lambda.EuiccCard.7T_o46uJcfxyJtjGMX_0X0kMk84.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void resetMemory(int paramInt, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApduWithSimResetErrorWorkaround(newRequestProvider(new _..Lambda.EuiccCard.Wx9UmYdMwRy23Rf6Vd7b2aSx6S8(paramInt)), _..Lambda.EuiccCard.0NUjmK32_r6146hGb0RCJUAfiOg.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void retrieveNotification(int paramInt, AsyncResultCallback<EuiccNotification> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.KOXfsx_q_NGiOmoDdBfBkea98mo(paramInt)), _..Lambda.EuiccCard.ICeXAGilnO8X0GNWbK6HW7brq_s.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void retrieveNotificationList(int paramInt, AsyncResultCallback<EuiccNotification[]> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.w7krlzKo4ZhEQOPUsWoy_EH6S6w(paramInt)), _..Lambda.EuiccCard.47aYJh9ifWZ2OFC8SQNq0T5EePE.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void setDefaultSmdpAddress(String paramString, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard.FbRMt6fKnYLkYt6oi5qhs1ZyEvc(paramString)), _..Lambda.EuiccCard.wgj93ukgzqjttFzrDLqGFk_Sd5A.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void setNickname(String paramString1, String paramString2, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApdu(newRequestProvider(new _..Lambda.EuiccCard._VOB5FQfE7RUMgpmr8bK_j3CsUA(paramString1, paramString2)), _..Lambda.EuiccCard.4gL9ssytVrnit44qHJ_7_Uy6ZOQ.INSTANCE, paramAsyncResultCallback, paramHandler);
  }
  
  public void switchToProfile(String paramString, boolean paramBoolean, AsyncResultCallback<Void> paramAsyncResultCallback, Handler paramHandler)
  {
    sendApduWithSimResetErrorWorkaround(newRequestProvider(new _..Lambda.EuiccCard.AYHfF2w_VlO00s9p_djcPJl_1no(paramString, paramBoolean)), new _..Lambda.EuiccCard.fcz5l0a6JlSxs8MXCst7wXG4bUc(paramString), paramAsyncResultCallback, paramHandler);
  }
  
  public void unregisterForEidReady(Handler paramHandler)
  {
    if (mEidReadyRegistrants != null) {
      mEidReadyRegistrants.remove(paramHandler);
    }
  }
  
  protected void updateCardId()
  {
    if (TextUtils.isEmpty(mEid)) {
      super.updateCardId();
    } else {
      mCardId = mEid;
    }
  }
  
  private static abstract interface ApduExceptionHandler
  {
    public abstract void handleException(Throwable paramThrowable);
  }
  
  private static abstract interface ApduRequestBuilder
  {
    public abstract void build(RequestBuilder paramRequestBuilder)
      throws EuiccCardException, TagNotFoundException, InvalidAsn1DataException;
  }
  
  private static abstract interface ApduResponseHandler<T>
  {
    public abstract T handleResult(byte[] paramArrayOfByte)
      throws EuiccCardException, TagNotFoundException, InvalidAsn1DataException;
  }
}
