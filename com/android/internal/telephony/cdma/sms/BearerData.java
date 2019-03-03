package com.android.internal.telephony.cdma.sms;

import android.content.res.Resources;
import android.telephony.Rlog;
import android.telephony.SmsCbCmasInfo;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.telephony.cdma.CdmaSmsCbProgramResults;
import android.text.format.Time;
import android.util.SparseIntArray;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.BitwiseInputStream.AccessException;
import com.android.internal.util.BitwiseOutputStream;
import com.android.internal.util.BitwiseOutputStream.AccessException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;

public final class BearerData
{
  public static final int ALERT_DEFAULT = 0;
  public static final int ALERT_HIGH_PRIO = 3;
  public static final int ALERT_LOW_PRIO = 1;
  public static final int ALERT_MEDIUM_PRIO = 2;
  public static final int DISPLAY_MODE_DEFAULT = 1;
  public static final int DISPLAY_MODE_IMMEDIATE = 0;
  public static final int DISPLAY_MODE_USER = 2;
  public static final int ERROR_NONE = 0;
  public static final int ERROR_PERMANENT = 3;
  public static final int ERROR_TEMPORARY = 2;
  public static final int ERROR_UNDEFINED = 255;
  public static final int LANGUAGE_CHINESE = 6;
  public static final int LANGUAGE_ENGLISH = 1;
  public static final int LANGUAGE_FRENCH = 2;
  public static final int LANGUAGE_HEBREW = 7;
  public static final int LANGUAGE_JAPANESE = 4;
  public static final int LANGUAGE_KOREAN = 5;
  public static final int LANGUAGE_SPANISH = 3;
  public static final int LANGUAGE_UNKNOWN = 0;
  private static final String LOG_TAG = "BearerData";
  public static final int MESSAGE_TYPE_CANCELLATION = 3;
  public static final int MESSAGE_TYPE_DELIVER = 1;
  public static final int MESSAGE_TYPE_DELIVERY_ACK = 4;
  public static final int MESSAGE_TYPE_DELIVER_REPORT = 7;
  public static final int MESSAGE_TYPE_READ_ACK = 6;
  public static final int MESSAGE_TYPE_SUBMIT = 2;
  public static final int MESSAGE_TYPE_SUBMIT_REPORT = 8;
  public static final int MESSAGE_TYPE_USER_ACK = 5;
  public static final int PRIORITY_EMERGENCY = 3;
  public static final int PRIORITY_INTERACTIVE = 1;
  public static final int PRIORITY_NORMAL = 0;
  public static final int PRIORITY_URGENT = 2;
  public static final int PRIVACY_CONFIDENTIAL = 2;
  public static final int PRIVACY_NOT_RESTRICTED = 0;
  public static final int PRIVACY_RESTRICTED = 1;
  public static final int PRIVACY_SECRET = 3;
  public static final int RELATIVE_TIME_DAYS_LIMIT = 196;
  public static final int RELATIVE_TIME_HOURS_LIMIT = 167;
  public static final int RELATIVE_TIME_INDEFINITE = 245;
  public static final int RELATIVE_TIME_MINS_LIMIT = 143;
  public static final int RELATIVE_TIME_MOBILE_INACTIVE = 247;
  public static final int RELATIVE_TIME_NOW = 246;
  public static final int RELATIVE_TIME_RESERVED = 248;
  public static final int RELATIVE_TIME_WEEKS_LIMIT = 244;
  public static final int STATUS_ACCEPTED = 0;
  public static final int STATUS_BLOCKED_DESTINATION = 7;
  public static final int STATUS_CANCELLED = 3;
  public static final int STATUS_CANCEL_FAILED = 6;
  public static final int STATUS_DELIVERED = 2;
  public static final int STATUS_DEPOSITED_TO_INTERNET = 1;
  public static final int STATUS_DUPLICATE_MESSAGE = 9;
  public static final int STATUS_INVALID_DESTINATION = 10;
  public static final int STATUS_MESSAGE_EXPIRED = 13;
  public static final int STATUS_NETWORK_CONGESTION = 4;
  public static final int STATUS_NETWORK_ERROR = 5;
  public static final int STATUS_TEXT_TOO_LONG = 8;
  public static final int STATUS_UNDEFINED = 255;
  public static final int STATUS_UNKNOWN_ERROR = 31;
  private static final byte SUBPARAM_ALERT_ON_MESSAGE_DELIVERY = 12;
  private static final byte SUBPARAM_CALLBACK_NUMBER = 14;
  private static final byte SUBPARAM_DEFERRED_DELIVERY_TIME_ABSOLUTE = 6;
  private static final byte SUBPARAM_DEFERRED_DELIVERY_TIME_RELATIVE = 7;
  private static final byte SUBPARAM_ID_LAST_DEFINED = 23;
  private static final byte SUBPARAM_LANGUAGE_INDICATOR = 13;
  private static final byte SUBPARAM_MESSAGE_CENTER_TIME_STAMP = 3;
  private static final byte SUBPARAM_MESSAGE_DEPOSIT_INDEX = 17;
  private static final byte SUBPARAM_MESSAGE_DISPLAY_MODE = 15;
  private static final byte SUBPARAM_MESSAGE_IDENTIFIER = 0;
  private static final byte SUBPARAM_MESSAGE_STATUS = 20;
  private static final byte SUBPARAM_NUMBER_OF_MESSAGES = 11;
  private static final byte SUBPARAM_PRIORITY_INDICATOR = 8;
  private static final byte SUBPARAM_PRIVACY_INDICATOR = 9;
  private static final byte SUBPARAM_REPLY_OPTION = 10;
  private static final byte SUBPARAM_SERVICE_CATEGORY_PROGRAM_DATA = 18;
  private static final byte SUBPARAM_SERVICE_CATEGORY_PROGRAM_RESULTS = 19;
  private static final byte SUBPARAM_USER_DATA = 1;
  private static final byte SUBPARAM_USER_RESPONSE_CODE = 2;
  private static final byte SUBPARAM_VALIDITY_PERIOD_ABSOLUTE = 4;
  private static final byte SUBPARAM_VALIDITY_PERIOD_RELATIVE = 5;
  public int alert = 0;
  public boolean alertIndicatorSet = false;
  public CdmaSmsAddress callbackNumber;
  public SmsCbCmasInfo cmasWarningInfo;
  public TimeStamp deferredDeliveryTimeAbsolute;
  public int deferredDeliveryTimeRelative;
  public boolean deferredDeliveryTimeRelativeSet;
  public boolean deliveryAckReq;
  public int depositIndex;
  public int displayMode = 1;
  public boolean displayModeSet = false;
  public int errorClass = 255;
  public boolean hasUserDataHeader;
  public int language = 0;
  public boolean languageIndicatorSet = false;
  public int messageId;
  public int messageStatus = 255;
  public boolean messageStatusSet = false;
  public int messageType;
  public TimeStamp msgCenterTimeStamp;
  public int numberOfMessages;
  public int priority = 0;
  public boolean priorityIndicatorSet = false;
  public int privacy = 0;
  public boolean privacyIndicatorSet = false;
  public boolean readAckReq;
  public boolean reportReq;
  public ArrayList<CdmaSmsCbProgramData> serviceCategoryProgramData;
  public ArrayList<CdmaSmsCbProgramResults> serviceCategoryProgramResults;
  public boolean userAckReq;
  public UserData userData;
  public int userResponseCode;
  public boolean userResponseCodeSet = false;
  public TimeStamp validityPeriodAbsolute;
  public int validityPeriodRelative;
  public boolean validityPeriodRelativeSet;
  
  public BearerData() {}
  
  public static GsmAlphabet.TextEncodingDetails calcTextEncodingDetails(CharSequence paramCharSequence, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = countAsciiSeptets(paramCharSequence, paramBoolean1);
    Object localObject;
    if ((i != -1) && (i <= 160))
    {
      localObject = new GsmAlphabet.TextEncodingDetails();
      msgCount = 1;
      codeUnitCount = i;
      codeUnitsRemaining = (160 - i);
      codeUnitSize = 1;
    }
    else
    {
      GsmAlphabet.TextEncodingDetails localTextEncodingDetails = SmsMessage.calculateLength(paramCharSequence, paramBoolean1);
      localObject = localTextEncodingDetails;
      if (msgCount == 1)
      {
        localObject = localTextEncodingDetails;
        if (codeUnitSize == 1)
        {
          localObject = localTextEncodingDetails;
          if (paramBoolean2) {
            return SmsMessageBase.calcUnicodeEncodingDetails(paramCharSequence);
          }
        }
      }
    }
    return localObject;
  }
  
  private static int countAsciiSeptets(CharSequence paramCharSequence, boolean paramBoolean)
  {
    int i = paramCharSequence.length();
    if (paramBoolean) {
      return i;
    }
    for (int j = 0; j < i; j++) {
      if (UserData.charToAscii.get(paramCharSequence.charAt(j), -1) == -1) {
        return -1;
      }
    }
    return i;
  }
  
  public static BearerData decode(byte[] paramArrayOfByte)
  {
    return decode(paramArrayOfByte, 0);
  }
  
  public static BearerData decode(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      Object localObject = new com/android/internal/util/BitwiseInputStream;
      ((BitwiseInputStream)localObject).<init>(paramArrayOfByte);
      paramArrayOfByte = new com/android/internal/telephony/cdma/sms/BearerData;
      paramArrayOfByte.<init>();
      int m;
      for (int i = 0; ((BitwiseInputStream)localObject).available() > 0; i = m)
      {
        int j = ((BitwiseInputStream)localObject).read(8);
        int k = 1 << j;
        if (((i & k) != 0) && (j >= 0) && (j <= 23))
        {
          localObject = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
          paramArrayOfByte = new java/lang/StringBuilder;
          paramArrayOfByte.<init>();
          paramArrayOfByte.append("illegal duplicate subparameter (");
          paramArrayOfByte.append(j);
          paramArrayOfByte.append(")");
          ((CodingException)localObject).<init>(paramArrayOfByte.toString());
          throw ((Throwable)localObject);
        }
        boolean bool;
        switch (j)
        {
        case 16: 
        case 19: 
        default: 
          bool = decodeReserved(paramArrayOfByte, (BitwiseInputStream)localObject, j);
          break;
        case 20: 
          bool = decodeMsgStatus(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 18: 
          bool = decodeServiceCategoryProgramData(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 17: 
          bool = decodeDepositIndex(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 15: 
          bool = decodeDisplayMode(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 14: 
          bool = decodeCallbackNumber(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 13: 
          bool = decodeLanguageIndicator(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 12: 
          bool = decodeMsgDeliveryAlert(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 11: 
          bool = decodeMsgCount(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 10: 
          bool = decodeReplyOption(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 9: 
          bool = decodePrivacyIndicator(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 8: 
          bool = decodePriorityIndicator(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 7: 
          bool = decodeDeferredDeliveryRel(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 6: 
          bool = decodeDeferredDeliveryAbs(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 5: 
          bool = decodeValidityRel(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 4: 
          bool = decodeValidityAbs(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 3: 
          bool = decodeMsgCenterTimeStamp(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 2: 
          bool = decodeUserResponseCode(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 1: 
          bool = decodeUserData(paramArrayOfByte, (BitwiseInputStream)localObject);
          break;
        case 0: 
          bool = decodeMessageId(paramArrayOfByte, (BitwiseInputStream)localObject);
        }
        m = i;
        if (bool)
        {
          m = i;
          if (j >= 0)
          {
            m = i;
            if (j <= 23) {
              m = i | k;
            }
          }
        }
      }
      if ((i & 0x1) != 0)
      {
        if (userData != null) {
          if (isCmasAlertCategory(paramInt))
          {
            decodeCmasUserData(paramArrayOfByte, paramInt);
          }
          else if (userData.msgEncoding == 1)
          {
            if ((i ^ 0x1 ^ 0x2) != 0)
            {
              localObject = new java/lang/StringBuilder;
              ((StringBuilder)localObject).<init>();
              ((StringBuilder)localObject).append("IS-91 must occur without extra subparams (");
              ((StringBuilder)localObject).append(i);
              ((StringBuilder)localObject).append(")");
              Rlog.e("BearerData", ((StringBuilder)localObject).toString());
            }
            decodeIs91(paramArrayOfByte);
          }
          else
          {
            decodeUserDataPayload(userData, hasUserDataHeader);
          }
        }
        return paramArrayOfByte;
      }
      paramArrayOfByte = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
      paramArrayOfByte.<init>("missing MESSAGE_IDENTIFIER subparam");
      throw paramArrayOfByte;
    }
    catch (CodingException localCodingException)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("BearerData decode failed: ");
      paramArrayOfByte.append(localCodingException);
      Rlog.e("BearerData", paramArrayOfByte.toString());
    }
    catch (BitwiseInputStream.AccessException localAccessException)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("BearerData decode failed: ");
      paramArrayOfByte.append(localAccessException);
      Rlog.e("BearerData", paramArrayOfByte.toString());
    }
    return null;
  }
  
  private static String decode7bitAscii(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    int i = paramInt1 * 8;
    try
    {
      paramInt1 = (i + 6) / 7;
      paramInt2 -= paramInt1;
      Object localObject1 = new java/lang/StringBuffer;
      ((StringBuffer)localObject1).<init>(paramInt2);
      localObject2 = new com/android/internal/util/BitwiseInputStream;
      ((BitwiseInputStream)localObject2).<init>(paramArrayOfByte);
      int j = paramInt1 * 7 + paramInt2 * 7;
      if (((BitwiseInputStream)localObject2).available() >= j)
      {
        ((BitwiseInputStream)localObject2).skip(i + (paramInt1 * 7 - i));
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        {
          i = ((BitwiseInputStream)localObject2).read(7);
          if ((i >= 32) && (i <= UserData.ASCII_MAP_MAX_INDEX)) {
            ((StringBuffer)localObject1).append(UserData.ASCII_MAP[(i - 32)]);
          } else if (i == 10) {
            ((StringBuffer)localObject1).append('\n');
          } else if (i == 13) {
            ((StringBuffer)localObject1).append('\r');
          } else {
            ((StringBuffer)localObject1).append(' ');
          }
        }
        return ((StringBuffer)localObject1).toString();
      }
      paramArrayOfByte = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("insufficient data (wanted ");
      ((StringBuilder)localObject1).append(j);
      ((StringBuilder)localObject1).append(" bits, but only have ");
      ((StringBuilder)localObject1).append(((BitwiseInputStream)localObject2).available());
      ((StringBuilder)localObject1).append(")");
      paramArrayOfByte.<init>(((StringBuilder)localObject1).toString());
      throw paramArrayOfByte;
    }
    catch (BitwiseInputStream.AccessException paramArrayOfByte)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("7bit ASCII decode failed: ");
      ((StringBuilder)localObject2).append(paramArrayOfByte);
      throw new CodingException(((StringBuilder)localObject2).toString());
    }
  }
  
  private static String decode7bitGsm(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    int i = paramInt1 * 8;
    int j = (i + 6) / 7;
    paramArrayOfByte = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, paramInt1, paramInt2 - j, j * 7 - i, 0, 0);
    if (paramArrayOfByte != null) {
      return paramArrayOfByte;
    }
    throw new CodingException("7bit GSM decoding failed");
  }
  
  private static boolean decodeCallbackNumber(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    int i = paramBitwiseInputStream.read(8) * 8;
    if (i < 8)
    {
      paramBitwiseInputStream.skip(i);
      return false;
    }
    CdmaSmsAddress localCdmaSmsAddress = new CdmaSmsAddress();
    digitMode = paramBitwiseInputStream.read(1);
    int j = 4;
    int k = 1;
    if (digitMode == 1)
    {
      ton = paramBitwiseInputStream.read(3);
      numberPlan = paramBitwiseInputStream.read(4);
      j = 8;
      k = (byte)(1 + 7);
    }
    numberOfDigits = paramBitwiseInputStream.read(8);
    k = i - (byte)(k + 8);
    i = numberOfDigits * j;
    j = k - i;
    if (k >= i)
    {
      origBytes = paramBitwiseInputStream.readByteArray(i);
      paramBitwiseInputStream.skip(j);
      decodeSmsAddress(localCdmaSmsAddress);
      callbackNumber = localCdmaSmsAddress;
      return true;
    }
    paramBearerData = new StringBuilder();
    paramBearerData.append("CALLBACK_NUMBER subparam encoding size error (remainingBits + ");
    paramBearerData.append(k);
    paramBearerData.append(", dataBits + ");
    paramBearerData.append(i);
    paramBearerData.append(", paddingBits + ");
    paramBearerData.append(j);
    paramBearerData.append(")");
    throw new CodingException(paramBearerData.toString());
  }
  
  private static String decodeCharset(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws BearerData.CodingException
  {
    int i;
    StringBuilder localStringBuilder;
    if (paramInt2 >= 0)
    {
      i = paramInt2;
      if (paramInt2 * paramInt3 + paramInt1 <= paramArrayOfByte.length) {}
    }
    else
    {
      i = (paramArrayOfByte.length - paramInt1 - paramInt1 % paramInt3) / paramInt3;
      if (i < 0) {
        break label193;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" decode error: offset = ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" numFields = ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" data.length = ");
      localStringBuilder.append(paramArrayOfByte.length);
      localStringBuilder.append(" maxNumFields = ");
      localStringBuilder.append(i);
      Rlog.e("BearerData", localStringBuilder.toString());
    }
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, paramInt1, i * paramInt3, paramString);
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" decode failed: ");
      localStringBuilder.append(paramArrayOfByte);
      throw new CodingException(localStringBuilder.toString());
    }
    label193:
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append(paramString);
    paramArrayOfByte.append(" decode failed: offset out of range");
    throw new CodingException(paramArrayOfByte.toString());
  }
  
  private static void decodeCmasUserData(BearerData paramBearerData, int paramInt)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    BitwiseInputStream localBitwiseInputStream = new BitwiseInputStream(userData.payload);
    if (localBitwiseInputStream.available() >= 8)
    {
      int i = localBitwiseInputStream.read(8);
      if (i == 0)
      {
        int j = serviceCategoryToCmasMessageClass(paramInt);
        int k = -1;
        int m = -1;
        int n = -1;
        i = -1;
        int i1 = -1;
        while (localBitwiseInputStream.available() >= 16)
        {
          paramInt = localBitwiseInputStream.read(8);
          int i2 = localBitwiseInputStream.read(8);
          Object localObject;
          switch (paramInt)
          {
          default: 
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("skipping unsupported CMAS record type ");
            ((StringBuilder)localObject).append(paramInt);
            Rlog.w("BearerData", ((StringBuilder)localObject).toString());
            localBitwiseInputStream.skip(i2 * 8);
            break;
          case 1: 
            k = localBitwiseInputStream.read(8);
            m = localBitwiseInputStream.read(8);
            n = localBitwiseInputStream.read(4);
            i = localBitwiseInputStream.read(4);
            i1 = localBitwiseInputStream.read(4);
            localBitwiseInputStream.skip(i2 * 8 - 28);
            break;
          case 0: 
            localObject = new UserData();
            msgEncoding = localBitwiseInputStream.read(5);
            msgEncodingSet = true;
            msgType = 0;
            paramInt = msgEncoding;
            if (paramInt != 0) {
              switch (paramInt)
              {
              default: 
                switch (paramInt)
                {
                default: 
                  paramInt = 0;
                }
                break;
              case 4: 
                paramInt = (i2 - 1) / 2;
                break;
              case 2: 
              case 3: 
                paramInt = (i2 * 8 - 5) / 7;
                break;
              }
            } else {
              paramInt = i2 - 1;
            }
            numFields = paramInt;
            payload = localBitwiseInputStream.readByteArray(i2 * 8 - 5);
            decodeUserDataPayload((UserData)localObject, false);
            userData = ((UserData)localObject);
          }
        }
        cmasWarningInfo = new SmsCbCmasInfo(j, k, m, n, i, i1);
        return;
      }
      paramBearerData = new StringBuilder();
      paramBearerData.append("unsupported CMAE_protocol_version ");
      paramBearerData.append(i);
      throw new CodingException(paramBearerData.toString());
    }
    throw new CodingException("emergency CB with no CMAE_protocol_version");
  }
  
  private static boolean decodeDeferredDeliveryAbs(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 48)
    {
      j = i - 48;
      bool = true;
      deferredDeliveryTimeAbsolute = TimeStamp.fromByteArray(paramBitwiseInputStream.readByteArray(48));
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DEFERRED_DELIVERY_TIME_ABSOLUTE decode ");
      if (bool) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool;
  }
  
  private static boolean decodeDeferredDeliveryRel(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      validityPeriodRelative = paramBitwiseInputStream.read(8);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DEFERRED_DELIVERY_TIME_RELATIVE decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    validityPeriodRelativeSet = bool;
    return bool;
  }
  
  private static boolean decodeDepositIndex(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 16)
    {
      j = i - 16;
      bool = true;
      i = paramBitwiseInputStream.read(8);
      depositIndex = (paramBitwiseInputStream.read(8) | i << 8);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MESSAGE_DEPOSIT_INDEX decode ");
      if (bool) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool;
  }
  
  private static boolean decodeDisplayMode(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      displayMode = paramBitwiseInputStream.read(2);
      paramBitwiseInputStream.skip(6);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DISPLAY_MODE decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    displayModeSet = bool;
    return bool;
  }
  
  private static String decodeDtmfSmsAddress(byte[] paramArrayOfByte, int paramInt)
    throws BearerData.CodingException
  {
    StringBuffer localStringBuffer = new StringBuffer(paramInt);
    int i = 0;
    while (i < paramInt)
    {
      int j = 0xF & paramArrayOfByte[(i / 2)] >>> 4 - i % 2 * 4;
      if ((j >= 1) && (j <= 9))
      {
        localStringBuffer.append(Integer.toString(j, 10));
      }
      else if (j == 10)
      {
        localStringBuffer.append('0');
      }
      else if (j == 11)
      {
        localStringBuffer.append('*');
      }
      else
      {
        if (j != 12) {
          break label116;
        }
        localStringBuffer.append('#');
      }
      i++;
      continue;
      label116:
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("invalid SMS address DTMF code (");
      paramArrayOfByte.append(j);
      paramArrayOfByte.append(")");
      throw new CodingException(paramArrayOfByte.toString());
    }
    return localStringBuffer.toString();
  }
  
  private static String decodeGsmDcs(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws BearerData.CodingException
  {
    if ((paramInt3 & 0xC0) == 0)
    {
      switch (paramInt3 >> 2 & 0x3)
      {
      default: 
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("unsupported user msgType encoding (");
        paramArrayOfByte.append(paramInt3);
        paramArrayOfByte.append(")");
        throw new CodingException(paramArrayOfByte.toString());
      case 2: 
        return decodeUtf16(paramArrayOfByte, paramInt1, paramInt2);
      case 1: 
        return decodeUtf8(paramArrayOfByte, paramInt1, paramInt2);
      }
      return decode7bitGsm(paramArrayOfByte, paramInt1, paramInt2);
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("unsupported coding group (");
    paramArrayOfByte.append(paramInt3);
    paramArrayOfByte.append(")");
    throw new CodingException(paramArrayOfByte.toString());
  }
  
  private static void decodeIs91(BearerData paramBearerData)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    switch (userData.msgType)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unsupported IS-91 message type (");
      localStringBuilder.append(userData.msgType);
      localStringBuilder.append(")");
      throw new CodingException(localStringBuilder.toString());
    case 132: 
      decodeIs91Cli(paramBearerData);
      break;
    case 131: 
    case 133: 
      decodeIs91ShortMessage(paramBearerData);
      break;
    case 130: 
      decodeIs91VoicemailStatus(paramBearerData);
    }
  }
  
  private static void decodeIs91Cli(BearerData paramBearerData)
    throws BearerData.CodingException
  {
    int i = new BitwiseInputStream(userData.payload).available() / 4;
    int j = userData.numFields;
    if ((i <= 14) && (i >= 3) && (i >= j))
    {
      CdmaSmsAddress localCdmaSmsAddress = new CdmaSmsAddress();
      digitMode = 0;
      origBytes = userData.payload;
      numberOfDigits = ((byte)j);
      decodeSmsAddress(localCdmaSmsAddress);
      callbackNumber = localCdmaSmsAddress;
      return;
    }
    throw new CodingException("IS-91 voicemail status decoding failed");
  }
  
  private static void decodeIs91ShortMessage(BearerData paramBearerData)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    BitwiseInputStream localBitwiseInputStream = new BitwiseInputStream(userData.payload);
    int i = localBitwiseInputStream.available() / 6;
    int j = userData.numFields;
    if ((j <= 14) && (i >= j))
    {
      StringBuffer localStringBuffer = new StringBuffer(i);
      for (i = 0; i < j; i++) {
        localStringBuffer.append(UserData.ASCII_MAP[localBitwiseInputStream.read(6)]);
      }
      userData.payloadStr = localStringBuffer.toString();
      return;
    }
    throw new CodingException("IS-91 short message decoding failed");
  }
  
  private static void decodeIs91VoicemailStatus(BearerData paramBearerData)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    BitwiseInputStream localBitwiseInputStream = new BitwiseInputStream(userData.payload);
    int i = localBitwiseInputStream.available() / 6;
    int j = userData.numFields;
    if ((i <= 14) && (i >= 3) && (i >= j)) {
      try
      {
        Object localObject = new java/lang/StringBuffer;
        ((StringBuffer)localObject).<init>(i);
        while (localBitwiseInputStream.available() >= 6) {
          ((StringBuffer)localObject).append(UserData.ASCII_MAP[localBitwiseInputStream.read(6)]);
        }
        localObject = ((StringBuffer)localObject).toString();
        numberOfMessages = Integer.parseInt(((String)localObject).substring(0, 2));
        char c = ((String)localObject).charAt(2);
        if (c == ' ')
        {
          priority = 0;
        }
        else
        {
          if (c != '!') {
            break label164;
          }
          priority = 2;
        }
        priorityIndicatorSet = true;
        userData.payloadStr = ((String)localObject).substring(3, j - 3);
        return;
        label164:
        localObject = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
        paramBearerData = new java/lang/StringBuilder;
        paramBearerData.<init>();
        paramBearerData.append("IS-91 voicemail status decoding failed: illegal priority setting (");
        paramBearerData.append(c);
        paramBearerData.append(")");
        ((CodingException)localObject).<init>(paramBearerData.toString());
        throw ((Throwable)localObject);
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        paramBearerData = new StringBuilder();
        paramBearerData.append("IS-91 voicemail status decoding failed: ");
        paramBearerData.append(localIndexOutOfBoundsException);
        throw new CodingException(paramBearerData.toString());
      }
      catch (NumberFormatException localNumberFormatException)
      {
        paramBearerData = new StringBuilder();
        paramBearerData.append("IS-91 voicemail status decoding failed: ");
        paramBearerData.append(localNumberFormatException);
        throw new CodingException(paramBearerData.toString());
      }
    }
    throw new CodingException("IS-91 voicemail status decoding failed");
  }
  
  private static boolean decodeLanguageIndicator(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      language = paramBitwiseInputStream.read(8);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("LANGUAGE_INDICATOR decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    languageIndicatorSet = bool;
    return bool;
  }
  
  private static String decodeLatin(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    return decodeCharset(paramArrayOfByte, paramInt1, paramInt2, 1, "ISO-8859-1");
  }
  
  private static boolean decodeMessageId(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool1 = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 24)
    {
      j = i - 24;
      boolean bool2 = true;
      messageType = paramBitwiseInputStream.read(4);
      messageId = (paramBitwiseInputStream.read(8) << 8);
      i = messageId;
      messageId = (paramBitwiseInputStream.read(8) | i);
      bool1 = true;
      if (paramBitwiseInputStream.read(1) != 1) {
        bool1 = false;
      }
      hasUserDataHeader = bool1;
      paramBitwiseInputStream.skip(3);
      bool1 = bool2;
    }
    if ((!bool1) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MESSAGE_IDENTIFIER decode ");
      if (bool1) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool1;
  }
  
  private static boolean decodeMsgCenterTimeStamp(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 48)
    {
      j = i - 48;
      bool = true;
      msgCenterTimeStamp = TimeStamp.fromByteArray(paramBitwiseInputStream.readByteArray(48));
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MESSAGE_CENTER_TIME_STAMP decode ");
      if (bool) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool;
  }
  
  private static boolean decodeMsgCount(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      numberOfMessages = IccUtils.cdmaBcdByteToInt((byte)paramBitwiseInputStream.read(8));
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("NUMBER_OF_MESSAGES decode ");
      if (bool) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool;
  }
  
  private static boolean decodeMsgDeliveryAlert(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      alert = paramBitwiseInputStream.read(2);
      paramBitwiseInputStream.skip(6);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ALERT_ON_MESSAGE_DELIVERY decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    alertIndicatorSet = bool;
    return bool;
  }
  
  private static boolean decodeMsgStatus(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      errorClass = paramBitwiseInputStream.read(2);
      messageStatus = paramBitwiseInputStream.read(6);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MESSAGE_STATUS decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    messageStatusSet = bool;
    return bool;
  }
  
  private static boolean decodePriorityIndicator(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      priority = paramBitwiseInputStream.read(2);
      paramBitwiseInputStream.skip(6);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PRIORITY_INDICATOR decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    priorityIndicatorSet = bool;
    return bool;
  }
  
  private static boolean decodePrivacyIndicator(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      privacy = paramBitwiseInputStream.read(2);
      paramBitwiseInputStream.skip(6);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PRIVACY_INDICATOR decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    privacyIndicatorSet = bool;
    return bool;
  }
  
  private static boolean decodeReplyOption(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool1 = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      boolean bool2 = true;
      boolean bool3 = true;
      if (paramBitwiseInputStream.read(1) == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      userAckReq = bool1;
      if (paramBitwiseInputStream.read(1) == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      deliveryAckReq = bool1;
      if (paramBitwiseInputStream.read(1) == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      readAckReq = bool1;
      if (paramBitwiseInputStream.read(1) == 1) {
        bool1 = bool3;
      } else {
        bool1 = false;
      }
      reportReq = bool1;
      paramBitwiseInputStream.skip(4);
      bool1 = bool2;
    }
    if ((!bool1) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("REPLY_OPTION decode ");
      if (bool1) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool1;
  }
  
  private static boolean decodeReserved(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream, int paramInt)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8);
    int j = i * 8;
    if (j <= paramBitwiseInputStream.available())
    {
      bool = true;
      paramBitwiseInputStream.skip(j);
    }
    paramBitwiseInputStream = new StringBuilder();
    paramBitwiseInputStream.append("RESERVED bearer data subparameter ");
    paramBitwiseInputStream.append(paramInt);
    paramBitwiseInputStream.append(" decode ");
    if (bool) {
      paramBearerData = "succeeded";
    } else {
      paramBearerData = "failed";
    }
    paramBitwiseInputStream.append(paramBearerData);
    paramBitwiseInputStream.append(" (param bits = ");
    paramBitwiseInputStream.append(j);
    paramBitwiseInputStream.append(")");
    Rlog.d("BearerData", paramBitwiseInputStream.toString());
    if (bool) {
      return bool;
    }
    paramBearerData = new StringBuilder();
    paramBearerData.append("RESERVED bearer data subparameter ");
    paramBearerData.append(paramInt);
    paramBearerData.append(" had invalid SUBPARAM_LEN ");
    paramBearerData.append(i);
    throw new CodingException(paramBearerData.toString());
  }
  
  private static boolean decodeServiceCategoryProgramData(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException, BearerData.CodingException
  {
    if (paramBitwiseInputStream.available() >= 13)
    {
      int i = paramBitwiseInputStream.read(8);
      int j = paramBitwiseInputStream.read(5);
      i = i * 8 - 5;
      if (paramBitwiseInputStream.available() >= i)
      {
        ArrayList localArrayList = new ArrayList();
        boolean bool = false;
        Object localObject;
        while (i >= 48)
        {
          int k = paramBitwiseInputStream.read(4);
          int m = paramBitwiseInputStream.read(8);
          int n = paramBitwiseInputStream.read(8);
          int i1 = paramBitwiseInputStream.read(8);
          int i2 = paramBitwiseInputStream.read(8);
          int i3 = paramBitwiseInputStream.read(4);
          int i4 = paramBitwiseInputStream.read(8);
          int i5 = i - 48;
          i = getBitsForNumFields(j, i4);
          if (i5 >= i)
          {
            localObject = new UserData();
            msgEncoding = j;
            msgEncodingSet = true;
            numFields = i4;
            payload = paramBitwiseInputStream.readByteArray(i);
            i = i5 - i;
            decodeUserDataPayload((UserData)localObject, false);
            localArrayList.add(new CdmaSmsCbProgramData(k, m << 8 | n, i1, i2, i3, payloadStr));
            bool = true;
          }
          else
          {
            paramBearerData = new StringBuilder();
            paramBearerData.append("category name is ");
            paramBearerData.append(i);
            paramBearerData.append(" bits in length, but there are only ");
            paramBearerData.append(i5);
            paramBearerData.append(" bits available");
            throw new CodingException(paramBearerData.toString());
          }
        }
        if ((!bool) || (i > 0))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("SERVICE_CATEGORY_PROGRAM_DATA decode ");
          if (bool) {
            localObject = "succeeded";
          } else {
            localObject = "failed";
          }
          localStringBuilder.append((String)localObject);
          localStringBuilder.append(" (extra bits = ");
          localStringBuilder.append(i);
          localStringBuilder.append(')');
          Rlog.d("BearerData", localStringBuilder.toString());
        }
        paramBitwiseInputStream.skip(i);
        serviceCategoryProgramData = localArrayList;
        return bool;
      }
      paramBearerData = new StringBuilder();
      paramBearerData.append("SERVICE_CATEGORY_PROGRAM_DATA decode failed: only ");
      paramBearerData.append(paramBitwiseInputStream.available());
      paramBearerData.append(" bits available (");
      paramBearerData.append(i);
      paramBearerData.append(" bits expected)");
      throw new CodingException(paramBearerData.toString());
    }
    paramBearerData = new StringBuilder();
    paramBearerData.append("SERVICE_CATEGORY_PROGRAM_DATA decode failed: only ");
    paramBearerData.append(paramBitwiseInputStream.available());
    paramBearerData.append(" bits available");
    throw new CodingException(paramBearerData.toString());
  }
  
  private static String decodeShiftJis(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    return decodeCharset(paramArrayOfByte, paramInt1, paramInt2, 1, "Shift_JIS");
  }
  
  private static void decodeSmsAddress(CdmaSmsAddress paramCdmaSmsAddress)
    throws BearerData.CodingException
  {
    if (digitMode == 1) {
      try
      {
        String str = new java/lang/String;
        str.<init>(origBytes, 0, origBytes.length, "US-ASCII");
        address = str;
      }
      catch (UnsupportedEncodingException paramCdmaSmsAddress)
      {
        throw new CodingException("invalid SMS address ASCII code");
      }
    } else {
      address = decodeDtmfSmsAddress(origBytes, numberOfDigits);
    }
  }
  
  private static boolean decodeUserData(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    int i = paramBitwiseInputStream.read(8);
    userData = new UserData();
    userData.msgEncoding = paramBitwiseInputStream.read(5);
    userData.msgEncodingSet = true;
    userData.msgType = 0;
    int j = 5;
    if ((userData.msgEncoding == 1) || (userData.msgEncoding == 10))
    {
      userData.msgType = paramBitwiseInputStream.read(8);
      j = 5 + 8;
    }
    userData.numFields = paramBitwiseInputStream.read(8);
    userData.payload = paramBitwiseInputStream.readByteArray(i * 8 - (j + 8));
    return true;
  }
  
  private static void decodeUserDataPayload(UserData paramUserData, boolean paramBoolean)
    throws BearerData.CodingException
  {
    int i = 0;
    int j;
    Object localObject;
    if (paramBoolean)
    {
      j = payload[0] & 0xFF;
      i = 0 + (j + 1);
      localObject = new byte[j];
      System.arraycopy(payload, 1, (byte[])localObject, 0, j);
      userDataHeader = SmsHeader.fromByteArray((byte[])localObject);
    }
    switch (msgEncoding)
    {
    case 1: 
    case 6: 
    case 7: 
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("unsupported user data encoding (");
      ((StringBuilder)localObject).append(msgEncoding);
      ((StringBuilder)localObject).append(")");
      throw new CodingException(((StringBuilder)localObject).toString());
    case 10: 
      payloadStr = decodeGsmDcs(payload, i, numFields, msgType);
      break;
    case 9: 
      payloadStr = decode7bitGsm(payload, i, numFields);
      break;
    case 8: 
      payloadStr = decodeLatin(payload, i, numFields);
      break;
    case 5: 
      payloadStr = decodeShiftJis(payload, i, numFields);
      break;
    case 4: 
      payloadStr = decodeUtf16(payload, i, numFields);
      break;
    case 2: 
    case 3: 
      payloadStr = decode7bitAscii(payload, i, numFields);
      break;
    case 0: 
      paramBoolean = Resources.getSystem().getBoolean(17957036);
      localObject = new byte[numFields];
      if (numFields < payload.length) {
        j = numFields;
      } else {
        j = payload.length;
      }
      System.arraycopy(payload, 0, (byte[])localObject, 0, j);
      payload = ((byte[])localObject);
      if (!paramBoolean) {
        payloadStr = decodeLatin(payload, i, numFields);
      } else {
        payloadStr = decodeUtf8(payload, i, numFields);
      }
      break;
    }
  }
  
  private static boolean decodeUserResponseCode(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      userResponseCode = paramBitwiseInputStream.read(8);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("USER_RESPONSE_CODE decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    userResponseCodeSet = bool;
    return bool;
  }
  
  private static String decodeUtf16(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    return decodeCharset(paramArrayOfByte, paramInt1, paramInt2 - (paramInt1 + paramInt1 % 2) / 2, 2, "utf-16be");
  }
  
  private static String decodeUtf8(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    return decodeCharset(paramArrayOfByte, paramInt1, paramInt2, 1, "UTF-8");
  }
  
  private static boolean decodeValidityAbs(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 48)
    {
      j = i - 48;
      bool = true;
      validityPeriodAbsolute = TimeStamp.fromByteArray(paramBitwiseInputStream.readByteArray(48));
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("VALIDITY_PERIOD_ABSOLUTE decode ");
      if (bool) {
        paramBearerData = "succeeded";
      } else {
        paramBearerData = "failed";
      }
      localStringBuilder.append(paramBearerData);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    return bool;
  }
  
  private static boolean decodeValidityRel(BearerData paramBearerData, BitwiseInputStream paramBitwiseInputStream)
    throws BitwiseInputStream.AccessException
  {
    boolean bool = false;
    int i = paramBitwiseInputStream.read(8) * 8;
    int j = i;
    if (i >= 8)
    {
      j = i - 8;
      bool = true;
      deferredDeliveryTimeRelative = paramBitwiseInputStream.read(8);
    }
    if ((!bool) || (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("VALIDITY_PERIOD_RELATIVE decode ");
      String str;
      if (bool) {
        str = "succeeded";
      } else {
        str = "failed";
      }
      localStringBuilder.append(str);
      localStringBuilder.append(" (extra bits = ");
      localStringBuilder.append(j);
      localStringBuilder.append(")");
      Rlog.d("BearerData", localStringBuilder.toString());
    }
    paramBitwiseInputStream.skip(j);
    deferredDeliveryTimeRelativeSet = bool;
    return bool;
  }
  
  public static byte[] encode(BearerData paramBearerData)
  {
    boolean bool;
    if ((userData != null) && (userData.userDataHeader != null)) {
      bool = true;
    } else {
      bool = false;
    }
    hasUserDataHeader = bool;
    try
    {
      localObject = new com/android/internal/util/BitwiseOutputStream;
      ((BitwiseOutputStream)localObject).<init>(200);
      ((BitwiseOutputStream)localObject).write(8, 0);
      encodeMessageId(paramBearerData, (BitwiseOutputStream)localObject);
      if (userData != null)
      {
        ((BitwiseOutputStream)localObject).write(8, 1);
        encodeUserData(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (callbackNumber != null)
      {
        ((BitwiseOutputStream)localObject).write(8, 14);
        encodeCallbackNumber(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if ((userAckReq) || (deliveryAckReq) || (readAckReq) || (reportReq))
      {
        ((BitwiseOutputStream)localObject).write(8, 10);
        encodeReplyOption(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (numberOfMessages != 0)
      {
        ((BitwiseOutputStream)localObject).write(8, 11);
        encodeMsgCount(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (validityPeriodRelativeSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 5);
        encodeValidityPeriodRel(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (privacyIndicatorSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 9);
        encodePrivacyIndicator(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (languageIndicatorSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 13);
        encodeLanguageIndicator(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (displayModeSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 15);
        encodeDisplayMode(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (priorityIndicatorSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 8);
        encodePriorityIndicator(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (alertIndicatorSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 12);
        encodeMsgDeliveryAlert(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (messageStatusSet)
      {
        ((BitwiseOutputStream)localObject).write(8, 20);
        encodeMsgStatus(paramBearerData, (BitwiseOutputStream)localObject);
      }
      if (serviceCategoryProgramResults != null)
      {
        ((BitwiseOutputStream)localObject).write(8, 19);
        encodeScpResults(paramBearerData, (BitwiseOutputStream)localObject);
      }
      paramBearerData = ((BitwiseOutputStream)localObject).toByteArray();
      return paramBearerData;
    }
    catch (CodingException paramBearerData)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("BearerData encode failed: ");
      ((StringBuilder)localObject).append(paramBearerData);
      Rlog.e("BearerData", ((StringBuilder)localObject).toString());
    }
    catch (BitwiseOutputStream.AccessException localAccessException)
    {
      paramBearerData = new StringBuilder();
      paramBearerData.append("BearerData encode failed: ");
      paramBearerData.append(localAccessException);
      Rlog.e("BearerData", paramBearerData.toString());
    }
    return null;
  }
  
  private static void encode16bitEms(UserData paramUserData, byte[] paramArrayOfByte)
    throws BearerData.CodingException
  {
    byte[] arrayOfByte = encodeUtf16(payloadStr);
    int i = paramArrayOfByte.length + 1;
    int j = (i + 1) / 2;
    int k = arrayOfByte.length / 2;
    msgEncoding = 4;
    msgEncodingSet = true;
    numFields = (j + k);
    payload = new byte[numFields * 2];
    payload[0] = ((byte)(byte)paramArrayOfByte.length);
    System.arraycopy(paramArrayOfByte, 0, payload, 1, paramArrayOfByte.length);
    System.arraycopy(arrayOfByte, 0, payload, i, arrayOfByte.length);
  }
  
  private static byte[] encode7bitAscii(String paramString, boolean paramBoolean)
    throws BearerData.CodingException
  {
    try
    {
      localObject = new com/android/internal/util/BitwiseOutputStream;
      ((BitwiseOutputStream)localObject).<init>(paramString.length());
      int i = paramString.length();
      for (int j = 0; j < i; j++)
      {
        int k = UserData.charToAscii.get(paramString.charAt(j), -1);
        if (k == -1)
        {
          if (paramBoolean)
          {
            ((BitwiseOutputStream)localObject).write(7, 32);
          }
          else
          {
            CodingException localCodingException = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("cannot ASCII encode (");
            ((StringBuilder)localObject).append(paramString.charAt(j));
            ((StringBuilder)localObject).append(")");
            localCodingException.<init>(((StringBuilder)localObject).toString());
            throw localCodingException;
          }
        }
        else {
          ((BitwiseOutputStream)localObject).write(7, k);
        }
      }
      paramString = ((BitwiseOutputStream)localObject).toByteArray();
      return paramString;
    }
    catch (BitwiseOutputStream.AccessException paramString)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("7bit ASCII encode failed: ");
      ((StringBuilder)localObject).append(paramString);
      throw new CodingException(((StringBuilder)localObject).toString());
    }
  }
  
  private static void encode7bitAsciiEms(UserData paramUserData, byte[] paramArrayOfByte, boolean paramBoolean)
    throws BearerData.CodingException
  {
    try
    {
      Rlog.d("BearerData", "encode7bitAsciiEms");
      int i = paramArrayOfByte.length + 1;
      int j = (i * 8 + 6) / 7;
      int k = j * 7 - i * 8;
      Object localObject = payloadStr;
      int m = ((String)localObject).length();
      BitwiseOutputStream localBitwiseOutputStream = new com/android/internal/util/BitwiseOutputStream;
      if (k > 0) {
        n = 1;
      } else {
        n = 0;
      }
      localBitwiseOutputStream.<init>(n + m);
      localBitwiseOutputStream.write(k, 0);
      for (int n = 0; n < m; n++)
      {
        k = UserData.charToAscii.get(((String)localObject).charAt(n), -1);
        if (k == -1)
        {
          if (paramBoolean)
          {
            localBitwiseOutputStream.write(7, 32);
          }
          else
          {
            paramUserData = new com/android/internal/telephony/cdma/sms/BearerData$CodingException;
            paramArrayOfByte = new java/lang/StringBuilder;
            paramArrayOfByte.<init>();
            paramArrayOfByte.append("cannot ASCII encode (");
            paramArrayOfByte.append(((String)localObject).charAt(n));
            paramArrayOfByte.append(")");
            paramUserData.<init>(paramArrayOfByte.toString());
            throw paramUserData;
          }
        }
        else {
          localBitwiseOutputStream.write(7, k);
        }
      }
      localObject = localBitwiseOutputStream.toByteArray();
      msgEncoding = 2;
      msgEncodingSet = true;
      numFields = (payloadStr.length() + j);
      payload = new byte[localObject.length + i];
      payload[0] = ((byte)(byte)paramArrayOfByte.length);
      System.arraycopy(paramArrayOfByte, 0, payload, 1, paramArrayOfByte.length);
      System.arraycopy((byte[])localObject, 0, payload, i, localObject.length);
      return;
    }
    catch (BitwiseOutputStream.AccessException paramUserData)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("7bit ASCII encode failed: ");
      paramArrayOfByte.append(paramUserData);
      throw new CodingException(paramArrayOfByte.toString());
    }
  }
  
  private static void encode7bitEms(UserData paramUserData, byte[] paramArrayOfByte, boolean paramBoolean)
    throws BearerData.CodingException
  {
    int i = ((paramArrayOfByte.length + 1) * 8 + 6) / 7;
    Gsm7bitCodingResult localGsm7bitCodingResult = encode7bitGsm(payloadStr, i, paramBoolean);
    msgEncoding = 9;
    msgEncodingSet = true;
    numFields = septets;
    payload = data;
    payload[0] = ((byte)(byte)paramArrayOfByte.length);
    System.arraycopy(paramArrayOfByte, 0, payload, 1, paramArrayOfByte.length);
  }
  
  private static Gsm7bitCodingResult encode7bitGsm(String paramString, int paramInt, boolean paramBoolean)
    throws BearerData.CodingException
  {
    try
    {
      paramString = GsmAlphabet.stringToGsm7BitPacked(paramString, paramInt, paramBoolean ^ true, 0, 0);
      localObject = new com/android/internal/telephony/cdma/sms/BearerData$Gsm7bitCodingResult;
      ((Gsm7bitCodingResult)localObject).<init>(null);
      data = new byte[paramString.length - 1];
      System.arraycopy(paramString, 1, data, 0, paramString.length - 1);
      septets = (paramString[0] & 0xFF);
      return localObject;
    }
    catch (EncodeException paramString)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("7bit GSM encode failed: ");
      ((StringBuilder)localObject).append(paramString);
      throw new CodingException(((StringBuilder)localObject).toString());
    }
  }
  
  private static void encodeCallbackNumber(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException, BearerData.CodingException
  {
    paramBearerData = callbackNumber;
    encodeCdmaSmsAddress(paramBearerData);
    int i = 9;
    if (digitMode == 1) {
      i = 9 + 7;
    }
    for (int j = numberOfDigits * 8;; j = numberOfDigits * 4) {
      break;
    }
    int k = i + j;
    int m = k / 8;
    if (k % 8 > 0) {
      i = 1;
    } else {
      i = 0;
    }
    i = m + i;
    k = i * 8 - k;
    paramBitwiseOutputStream.write(8, i);
    paramBitwiseOutputStream.write(1, digitMode);
    if (digitMode == 1)
    {
      paramBitwiseOutputStream.write(3, ton);
      paramBitwiseOutputStream.write(4, numberPlan);
    }
    paramBitwiseOutputStream.write(8, numberOfDigits);
    paramBitwiseOutputStream.writeByteArray(j, origBytes);
    if (k > 0) {
      paramBitwiseOutputStream.write(k, 0);
    }
  }
  
  private static void encodeCdmaSmsAddress(CdmaSmsAddress paramCdmaSmsAddress)
    throws BearerData.CodingException
  {
    if (digitMode == 1) {
      try
      {
        origBytes = address.getBytes("US-ASCII");
      }
      catch (UnsupportedEncodingException paramCdmaSmsAddress)
      {
        throw new CodingException("invalid SMS address, cannot convert to ASCII");
      }
    } else {
      origBytes = encodeDtmfSmsAddress(address);
    }
  }
  
  private static void encodeDisplayMode(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(2, displayMode);
    paramBitwiseOutputStream.skip(6);
  }
  
  private static byte[] encodeDtmfSmsAddress(String paramString)
  {
    int i = paramString.length();
    int j = i * 4;
    int k = j / 8;
    int m = 0;
    if (j % 8 > 0) {
      j = 1;
    } else {
      j = 0;
    }
    byte[] arrayOfByte = new byte[k + j];
    while (m < i)
    {
      j = paramString.charAt(m);
      if ((j >= 49) && (j <= 57))
      {
        j -= 48;
      }
      else if (j == 48)
      {
        j = 10;
      }
      else if (j == 42)
      {
        j = 11;
      }
      else
      {
        if (j != 35) {
          break label134;
        }
        j = 12;
      }
      k = m / 2;
      arrayOfByte[k] = ((byte)(byte)(arrayOfByte[k] | j << 4 - m % 2 * 4));
      m++;
      continue;
      label134:
      return null;
    }
    return arrayOfByte;
  }
  
  private static void encodeEmsUserDataPayload(UserData paramUserData)
    throws BearerData.CodingException
  {
    byte[] arrayOfByte = SmsHeader.toByteArray(userDataHeader);
    if (msgEncodingSet)
    {
      if (msgEncoding == 9)
      {
        encode7bitEms(paramUserData, arrayOfByte, true);
      }
      else if (msgEncoding == 4)
      {
        encode16bitEms(paramUserData, arrayOfByte);
      }
      else if (msgEncoding == 2)
      {
        encode7bitAsciiEms(paramUserData, arrayOfByte, true);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("unsupported EMS user data encoding (");
        localStringBuilder.append(msgEncoding);
        localStringBuilder.append(")");
        throw new CodingException(localStringBuilder.toString());
      }
    }
    else {
      try
      {
        encode7bitEms(paramUserData, arrayOfByte, false);
      }
      catch (CodingException localCodingException)
      {
        encode16bitEms(paramUserData, arrayOfByte);
      }
    }
  }
  
  private static void encodeLanguageIndicator(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(8, language);
  }
  
  private static void encodeMessageId(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 3);
    paramBitwiseOutputStream.write(4, messageType);
    paramBitwiseOutputStream.write(8, messageId >> 8);
    paramBitwiseOutputStream.write(8, messageId);
    paramBitwiseOutputStream.write(1, hasUserDataHeader);
    paramBitwiseOutputStream.skip(3);
  }
  
  private static void encodeMsgCount(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(8, numberOfMessages);
  }
  
  private static void encodeMsgDeliveryAlert(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(2, alert);
    paramBitwiseOutputStream.skip(6);
  }
  
  private static void encodeMsgStatus(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(2, errorClass);
    paramBitwiseOutputStream.write(6, messageStatus);
  }
  
  private static void encodePriorityIndicator(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(2, priority);
    paramBitwiseOutputStream.skip(6);
  }
  
  private static void encodePrivacyIndicator(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(2, privacy);
    paramBitwiseOutputStream.skip(6);
  }
  
  private static void encodeReplyOption(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(1, userAckReq);
    paramBitwiseOutputStream.write(1, deliveryAckReq);
    paramBitwiseOutputStream.write(1, readAckReq);
    paramBitwiseOutputStream.write(1, reportReq);
    paramBitwiseOutputStream.write(4, 0);
  }
  
  private static void encodeScpResults(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBearerData = serviceCategoryProgramResults;
    paramBitwiseOutputStream.write(8, paramBearerData.size() * 4);
    paramBearerData = paramBearerData.iterator();
    while (paramBearerData.hasNext())
    {
      CdmaSmsCbProgramResults localCdmaSmsCbProgramResults = (CdmaSmsCbProgramResults)paramBearerData.next();
      int i = localCdmaSmsCbProgramResults.getCategory();
      paramBitwiseOutputStream.write(8, i >> 8);
      paramBitwiseOutputStream.write(8, i);
      paramBitwiseOutputStream.write(8, localCdmaSmsCbProgramResults.getLanguage());
      paramBitwiseOutputStream.write(4, localCdmaSmsCbProgramResults.getCategoryResult());
      paramBitwiseOutputStream.skip(4);
    }
  }
  
  private static byte[] encodeShiftJis(String paramString)
    throws BearerData.CodingException
  {
    try
    {
      paramString = paramString.getBytes("Shift_JIS");
      return paramString;
    }
    catch (UnsupportedEncodingException paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Shift-JIS encode failed: ");
      localStringBuilder.append(paramString);
      throw new CodingException(localStringBuilder.toString());
    }
  }
  
  private static void encodeUserData(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException, BearerData.CodingException
  {
    encodeUserDataPayload(userData);
    boolean bool;
    if (userData.userDataHeader != null) {
      bool = true;
    } else {
      bool = false;
    }
    hasUserDataHeader = bool;
    if (userData.payload.length <= 140)
    {
      int i = userData.payload.length * 8 - userData.paddingBits;
      int j = i + 13;
      if (userData.msgEncoding != 1)
      {
        k = j;
        if (userData.msgEncoding != 10) {}
      }
      else
      {
        k = j + 8;
      }
      int m = k / 8;
      if (k % 8 > 0) {
        j = 1;
      } else {
        j = 0;
      }
      j = m + j;
      int k = j * 8 - k;
      paramBitwiseOutputStream.write(8, j);
      paramBitwiseOutputStream.write(5, userData.msgEncoding);
      if ((userData.msgEncoding == 1) || (userData.msgEncoding == 10)) {
        paramBitwiseOutputStream.write(8, userData.msgType);
      }
      paramBitwiseOutputStream.write(8, userData.numFields);
      paramBitwiseOutputStream.writeByteArray(i, userData.payload);
      if (k > 0) {
        paramBitwiseOutputStream.write(k, 0);
      }
      return;
    }
    paramBitwiseOutputStream = new StringBuilder();
    paramBitwiseOutputStream.append("encoded user data too large (");
    paramBitwiseOutputStream.append(userData.payload.length);
    paramBitwiseOutputStream.append(" > ");
    paramBitwiseOutputStream.append(140);
    paramBitwiseOutputStream.append(" bytes)");
    throw new CodingException(paramBitwiseOutputStream.toString());
  }
  
  private static void encodeUserDataPayload(UserData paramUserData)
    throws BearerData.CodingException
  {
    if ((payloadStr == null) && (msgEncoding != 0))
    {
      Rlog.e("BearerData", "user data with null payloadStr");
      payloadStr = "";
    }
    if (userDataHeader != null)
    {
      encodeEmsUserDataPayload(paramUserData);
      return;
    }
    if (msgEncodingSet)
    {
      if (msgEncoding == 0)
      {
        if (payload == null)
        {
          Rlog.e("BearerData", "user data with octet encoding but null payload");
          payload = new byte[0];
          numFields = 0;
        }
        else
        {
          numFields = payload.length;
        }
      }
      else
      {
        if (payloadStr == null)
        {
          Rlog.e("BearerData", "non-octet user data with null payloadStr");
          payloadStr = "";
        }
        Object localObject;
        if (msgEncoding == 9)
        {
          localObject = encode7bitGsm(payloadStr, 0, true);
          payload = data;
          numFields = septets;
        }
        else if (msgEncoding == 2)
        {
          payload = encode7bitAscii(payloadStr, true);
          numFields = payloadStr.length();
        }
        else if (msgEncoding == 4)
        {
          payload = encodeUtf16(payloadStr);
          numFields = payloadStr.length();
        }
        else if (msgEncoding == 5)
        {
          payload = encodeShiftJis(payloadStr);
          numFields = payload.length;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unsupported user data encoding (");
          ((StringBuilder)localObject).append(msgEncoding);
          ((StringBuilder)localObject).append(")");
          throw new CodingException(((StringBuilder)localObject).toString());
        }
      }
    }
    else
    {
      try
      {
        payload = encode7bitAscii(payloadStr, false);
        msgEncoding = 2;
      }
      catch (CodingException localCodingException)
      {
        payload = encodeUtf16(payloadStr);
        msgEncoding = 4;
      }
      numFields = payloadStr.length();
      msgEncodingSet = true;
    }
  }
  
  private static byte[] encodeUtf16(String paramString)
    throws BearerData.CodingException
  {
    try
    {
      paramString = paramString.getBytes("utf-16be");
      return paramString;
    }
    catch (UnsupportedEncodingException paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UTF-16 encode failed: ");
      localStringBuilder.append(paramString);
      throw new CodingException(localStringBuilder.toString());
    }
  }
  
  private static void encodeValidityPeriodRel(BearerData paramBearerData, BitwiseOutputStream paramBitwiseOutputStream)
    throws BitwiseOutputStream.AccessException
  {
    paramBitwiseOutputStream.write(8, 1);
    paramBitwiseOutputStream.write(8, validityPeriodRelative);
  }
  
  private static int getBitsForNumFields(int paramInt1, int paramInt2)
    throws BearerData.CodingException
  {
    if (paramInt1 != 0) {
      switch (paramInt1)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("unsupported message encoding (");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(')');
        throw new CodingException(localStringBuilder.toString());
      case 4: 
        return paramInt2 * 16;
      case 2: 
      case 3: 
      case 9: 
        return paramInt2 * 7;
      }
    }
    return paramInt2 * 8;
  }
  
  private static String getLanguageCodeForValue(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 7: 
      return "he";
    case 6: 
      return "zh";
    case 5: 
      return "ko";
    case 4: 
      return "ja";
    case 3: 
      return "es";
    case 2: 
      return "fr";
    }
    return "en";
  }
  
  private static boolean isCmasAlertCategory(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 4096) && (paramInt <= 4351)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static int serviceCategoryToCmasMessageClass(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 4100: 
      return 4;
    case 4099: 
      return 3;
    case 4098: 
      return 2;
    case 4097: 
      return 1;
    }
    return 0;
  }
  
  public String getLanguage()
  {
    return getLanguageCodeForValue(language);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("BearerData ");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("{ messageType=");
    ((StringBuilder)localObject).append(messageType);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", messageId=");
    ((StringBuilder)localObject).append(messageId);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", priority=");
    if (priorityIndicatorSet) {
      localObject = Integer.valueOf(priority);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", privacy=");
    if (privacyIndicatorSet) {
      localObject = Integer.valueOf(privacy);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", alert=");
    if (alertIndicatorSet) {
      localObject = Integer.valueOf(alert);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", displayMode=");
    if (displayModeSet) {
      localObject = Integer.valueOf(displayMode);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", language=");
    if (languageIndicatorSet) {
      localObject = Integer.valueOf(language);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", errorClass=");
    if (messageStatusSet) {
      localObject = Integer.valueOf(errorClass);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", msgStatus=");
    if (messageStatusSet) {
      localObject = Integer.valueOf(messageStatus);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", msgCenterTimeStamp=");
    if (msgCenterTimeStamp != null) {
      localObject = msgCenterTimeStamp;
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", validityPeriodAbsolute=");
    if (validityPeriodAbsolute != null) {
      localObject = validityPeriodAbsolute;
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", validityPeriodRelative=");
    if (validityPeriodRelativeSet) {
      localObject = Integer.valueOf(validityPeriodRelative);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", deferredDeliveryTimeAbsolute=");
    if (deferredDeliveryTimeAbsolute != null) {
      localObject = deferredDeliveryTimeAbsolute;
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", deferredDeliveryTimeRelative=");
    if (deferredDeliveryTimeRelativeSet) {
      localObject = Integer.valueOf(deferredDeliveryTimeRelative);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", userAckReq=");
    ((StringBuilder)localObject).append(userAckReq);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", deliveryAckReq=");
    ((StringBuilder)localObject).append(deliveryAckReq);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", readAckReq=");
    ((StringBuilder)localObject).append(readAckReq);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", reportReq=");
    ((StringBuilder)localObject).append(reportReq);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", numberOfMessages=");
    ((StringBuilder)localObject).append(numberOfMessages);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", callbackNumber=");
    ((StringBuilder)localObject).append(Rlog.pii("BearerData", callbackNumber));
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", depositIndex=");
    ((StringBuilder)localObject).append(depositIndex);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", hasUserDataHeader=");
    ((StringBuilder)localObject).append(hasUserDataHeader);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", userData=");
    ((StringBuilder)localObject).append(userData);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
  
  private static class CodingException
    extends Exception
  {
    public CodingException(String paramString)
    {
      super();
    }
  }
  
  private static class Gsm7bitCodingResult
  {
    byte[] data;
    int septets;
    
    private Gsm7bitCodingResult() {}
  }
  
  public static class TimeStamp
    extends Time
  {
    public TimeStamp()
    {
      super();
    }
    
    public static TimeStamp fromByteArray(byte[] paramArrayOfByte)
    {
      TimeStamp localTimeStamp = new TimeStamp();
      int i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[0]);
      if ((i <= 99) && (i >= 0))
      {
        if (i >= 96) {
          i += 1900;
        } else {
          i += 2000;
        }
        year = i;
        i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[1]);
        if ((i >= 1) && (i <= 12))
        {
          month = (i - 1);
          i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[2]);
          if ((i >= 1) && (i <= 31))
          {
            monthDay = i;
            i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[3]);
            if ((i >= 0) && (i <= 23))
            {
              hour = i;
              i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[4]);
              if ((i >= 0) && (i <= 59))
              {
                minute = i;
                i = IccUtils.cdmaBcdByteToInt(paramArrayOfByte[5]);
                if ((i >= 0) && (i <= 59))
                {
                  second = i;
                  return localTimeStamp;
                }
                return null;
              }
              return null;
            }
            return null;
          }
          return null;
        }
        return null;
      }
      return null;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("TimeStamp ");
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("{ year=");
      localStringBuilder2.append(year);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", month=");
      localStringBuilder2.append(month);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", day=");
      localStringBuilder2.append(monthDay);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", hour=");
      localStringBuilder2.append(hour);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", minute=");
      localStringBuilder2.append(minute);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", second=");
      localStringBuilder2.append(second);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append(" }");
      return localStringBuilder1.toString();
    }
  }
}
