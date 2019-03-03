package com.android.internal.telephony.gsm;

import android.content.res.Resources;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.text.format.Time;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.PortAddrs;
import com.android.internal.telephony.SmsHeader.SpecialSmsMsg;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

public class SmsMessage
  extends SmsMessageBase
{
  private static final int INVALID_VALIDITY_PERIOD = -1;
  static final String LOG_TAG = "SmsMessage";
  private static final int VALIDITY_PERIOD_FORMAT_ABSOLUTE = 3;
  private static final int VALIDITY_PERIOD_FORMAT_ENHANCED = 1;
  private static final int VALIDITY_PERIOD_FORMAT_NONE = 0;
  private static final int VALIDITY_PERIOD_FORMAT_RELATIVE = 2;
  private static final int VALIDITY_PERIOD_MAX = 635040;
  private static final int VALIDITY_PERIOD_MIN = 5;
  private static final boolean VDBG = false;
  private int mDataCodingScheme;
  private boolean mIsStatusReportMessage = false;
  private int mMti;
  private int mProtocolIdentifier;
  private boolean mReplyPathPresent = false;
  private int mStatus;
  private int mVoiceMailCount = 0;
  private SmsConstants.MessageClass messageClass;
  
  public SmsMessage() {}
  
  public static GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean)
  {
    String str = null;
    if (Resources.getSystem().getBoolean(17957035)) {
      str = Sms7BitEncodingTranslator.translate(paramCharSequence);
    }
    Object localObject = str;
    if (TextUtils.isEmpty(str)) {
      localObject = paramCharSequence;
    }
    paramCharSequence = GsmAlphabet.countGsmSeptets((CharSequence)localObject, paramBoolean);
    if (paramCharSequence == null) {
      return SmsMessageBase.calcUnicodeEncodingDetails((CharSequence)localObject);
    }
    return paramCharSequence;
  }
  
  public static SmsMessage createFromEfRecord(int paramInt, byte[] paramArrayOfByte)
  {
    try
    {
      SmsMessage localSmsMessage = new com/android/internal/telephony/gsm/SmsMessage;
      localSmsMessage.<init>();
      mIndexOnIcc = paramInt;
      if ((paramArrayOfByte[0] & 0x1) == 0)
      {
        Rlog.w("SmsMessage", "SMS parsing failed: Trying to parse a free record");
        return null;
      }
      mStatusOnIcc = (paramArrayOfByte[0] & 0x7);
      paramInt = paramArrayOfByte.length - 1;
      byte[] arrayOfByte = new byte[paramInt];
      System.arraycopy(paramArrayOfByte, 1, arrayOfByte, 0, paramInt);
      localSmsMessage.parsePdu(arrayOfByte);
      return localSmsMessage;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "SMS PDU parsing failed: ", paramArrayOfByte);
    }
    return null;
  }
  
  public static SmsMessage createFromPdu(byte[] paramArrayOfByte)
  {
    try
    {
      SmsMessage localSmsMessage = new com/android/internal/telephony/gsm/SmsMessage;
      localSmsMessage.<init>();
      localSmsMessage.parsePdu(paramArrayOfByte);
      return localSmsMessage;
    }
    catch (OutOfMemoryError paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "SMS PDU parsing failed with out of memory: ", paramArrayOfByte);
      return null;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "SMS PDU parsing failed: ", paramArrayOfByte);
    }
    return null;
  }
  
  private static byte[] encodeUCS2(String paramString, byte[] paramArrayOfByte)
    throws UnsupportedEncodingException
  {
    paramString = paramString.getBytes("utf-16be");
    if (paramArrayOfByte != null)
    {
      byte[] arrayOfByte = new byte[paramArrayOfByte.length + paramString.length + 1];
      arrayOfByte[0] = ((byte)(byte)paramArrayOfByte.length);
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 1, paramArrayOfByte.length);
      System.arraycopy(paramString, 0, arrayOfByte, paramArrayOfByte.length + 1, paramString.length);
      paramString = arrayOfByte;
    }
    paramArrayOfByte = new byte[paramString.length + 1];
    paramArrayOfByte[0] = ((byte)(byte)(paramString.length & 0xFF));
    System.arraycopy(paramString, 0, paramArrayOfByte, 1, paramString.length);
    return paramArrayOfByte;
  }
  
  public static int getRelativeValidityPeriod(int paramInt)
  {
    int i = -1;
    if ((paramInt >= 5) && (paramInt <= 635040))
    {
      if (paramInt <= 720) {
        i = paramInt / 5 - 1;
      } else if (paramInt <= 1440) {
        i = (paramInt - 720) / 30 + 143;
      } else if (paramInt <= 43200) {
        i = paramInt / 1440 + 166;
      } else if (paramInt <= 635040) {
        i = paramInt / 10080 + 192;
      }
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid Validity Period");
    localStringBuilder.append(paramInt);
    Rlog.e("SmsMessage", localStringBuilder.toString());
    return -1;
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    Object localObject1 = new SmsHeader.PortAddrs();
    destPort = paramInt;
    origPort = 0;
    areEightBits = false;
    Object localObject2 = new SmsHeader();
    portAddrs = ((SmsHeader.PortAddrs)localObject1);
    localObject1 = SmsHeader.toByteArray((SmsHeader)localObject2);
    if (paramArrayOfByte.length + localObject1.length + 1 > 140)
    {
      paramString1 = new StringBuilder();
      paramString1.append("SMS data message may only contain ");
      paramString1.append(140 - localObject1.length - 1);
      paramString1.append(" bytes");
      Rlog.e("SmsMessage", paramString1.toString());
      return null;
    }
    localObject2 = new SubmitPdu();
    paramString1 = getSubmitPduHead(paramString1, paramString2, (byte)65, paramBoolean, (SubmitPdu)localObject2);
    if (paramString1 == null) {
      return localObject2;
    }
    paramString1.write(4);
    paramString1.write(paramArrayOfByte.length + localObject1.length + 1);
    paramString1.write(localObject1.length);
    paramString1.write((byte[])localObject1, 0, localObject1.length);
    paramString1.write(paramArrayOfByte, 0, paramArrayOfByte.length);
    encodedMessage = paramString1.toByteArray();
    return localObject2;
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, null);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
  {
    return getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, null, 0, 0, 0, paramInt);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    return getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramArrayOfByte, 0, 0, 0);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    return getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramArrayOfByte, paramInt1, paramInt2, paramInt3, -1);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramString3 != null) && (paramString2 != null))
    {
      if (paramInt1 == 0)
      {
        localObject = calculateLength(paramString3, false);
        paramInt1 = codeUnitSize;
        paramInt2 = languageTable;
        paramInt3 = languageShiftTable;
        if ((paramInt1 == 1) && ((paramInt2 != 0) || (paramInt3 != 0))) {
          if (paramArrayOfByte != null)
          {
            localObject = SmsHeader.fromByteArray(paramArrayOfByte);
            if ((languageTable == paramInt2) && (languageShiftTable == paramInt3)) {
              break label209;
            }
            paramArrayOfByte = new StringBuilder();
            paramArrayOfByte.append("Updating language table in SMS header: ");
            paramArrayOfByte.append(languageTable);
            paramArrayOfByte.append(" -> ");
            paramArrayOfByte.append(paramInt2);
            paramArrayOfByte.append(", ");
            paramArrayOfByte.append(languageShiftTable);
            paramArrayOfByte.append(" -> ");
            paramArrayOfByte.append(paramInt3);
            Rlog.w("SmsMessage", paramArrayOfByte.toString());
            languageTable = paramInt2;
            languageShiftTable = paramInt3;
            paramArrayOfByte = SmsHeader.toByteArray((SmsHeader)localObject);
          }
          else
          {
            label209:
            paramArrayOfByte = new SmsHeader();
            languageTable = paramInt2;
            languageShiftTable = paramInt3;
            paramArrayOfByte = SmsHeader.toByteArray(paramArrayOfByte);
          }
        }
      }
      Object localObject = new SubmitPdu();
      int i = 0;
      int j = getRelativeValidityPeriod(paramInt4);
      paramInt4 = i;
      if (j >= 0) {
        paramInt4 = 2;
      }
      if (paramArrayOfByte != null) {
        i = 64;
      } else {
        i = 0;
      }
      paramString2 = getSubmitPduHead(paramString1, paramString2, (byte)(paramInt4 << 3 | 0x1 | i), paramBoolean, (SubmitPdu)localObject);
      if (paramString2 == null) {
        return localObject;
      }
      if (paramInt1 == 1) {
        try
        {
          paramString1 = GsmAlphabet.stringToGsm7BitPackedWithHeader(paramString3, paramArrayOfByte, paramInt2, paramInt3);
        }
        catch (EncodeException paramString1)
        {
          break label367;
        }
      }
      try
      {
        paramString1 = encodeUCS2(paramString3, paramArrayOfByte);
      }
      catch (UnsupportedEncodingException paramString1)
      {
        Rlog.e("SmsMessage", "Implausible UnsupportedEncodingException ", paramString1);
        return null;
      }
      try
      {
        label367:
        paramString1 = encodeUCS2(paramString3, paramArrayOfByte);
        paramInt1 = 3;
        if (paramInt1 == 1)
        {
          if ((0xFF & paramString1[0]) > 160)
          {
            paramString2 = new StringBuilder();
            paramString2.append("Message too long (");
            paramString2.append(0xFF & paramString1[0]);
            paramString2.append(" septets)");
            Rlog.e("SmsMessage", paramString2.toString());
            return null;
          }
          paramString2.write(0);
        }
        else
        {
          if ((paramString1[0] & 0xFF) > 140)
          {
            paramString2 = new StringBuilder();
            paramString2.append("Message too long (");
            paramString2.append(paramString1[0] & 0xFF);
            paramString2.append(" bytes)");
            Rlog.e("SmsMessage", paramString2.toString());
            return null;
          }
          paramString2.write(8);
        }
        if (paramInt4 == 2) {
          paramString2.write(j);
        }
        paramString2.write(paramString1, 0, paramString1.length);
        encodedMessage = paramString2.toByteArray();
        return localObject;
      }
      catch (UnsupportedEncodingException paramString1)
      {
        Rlog.e("SmsMessage", "Implausible UnsupportedEncodingException ", paramString1);
        return null;
      }
    }
    return null;
  }
  
  private static ByteArrayOutputStream getSubmitPduHead(String paramString1, String paramString2, byte paramByte, boolean paramBoolean, SubmitPdu paramSubmitPdu)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(180);
    if (paramString1 == null) {
      encodedScAddress = null;
    } else {
      encodedScAddress = PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(paramString1);
    }
    int i = paramByte;
    if (paramBoolean) {
      i = (byte)(paramByte | 0x20);
    }
    localByteArrayOutputStream.write(i);
    localByteArrayOutputStream.write(0);
    paramString1 = PhoneNumberUtils.networkPortionToCalledPartyBCD(paramString2);
    if (paramString1 == null) {
      return null;
    }
    i = paramString1.length;
    paramByte = 1;
    if ((paramString1[(paramString1.length - 1)] & 0xF0) != 240) {
      paramByte = 0;
    }
    localByteArrayOutputStream.write((i - 1) * 2 - paramByte);
    localByteArrayOutputStream.write(paramString1, 0, paramString1.length);
    localByteArrayOutputStream.write(0);
    return localByteArrayOutputStream;
  }
  
  public static int getTPLayerLengthForPDU(String paramString)
  {
    return paramString.length() / 2 - Integer.parseInt(paramString.substring(0, 2), 16) - 1;
  }
  
  public static SmsMessage newFromCDS(byte[] paramArrayOfByte)
  {
    try
    {
      SmsMessage localSmsMessage = new com/android/internal/telephony/gsm/SmsMessage;
      localSmsMessage.<init>();
      localSmsMessage.parsePdu(paramArrayOfByte);
      return localSmsMessage;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "CDS SMS PDU parsing failed: ", paramArrayOfByte);
    }
    return null;
  }
  
  public static SmsMessage newFromCMT(byte[] paramArrayOfByte)
  {
    try
    {
      SmsMessage localSmsMessage = new com/android/internal/telephony/gsm/SmsMessage;
      localSmsMessage.<init>();
      localSmsMessage.parsePdu(paramArrayOfByte);
      return localSmsMessage;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "SMS PDU parsing failed: ", paramArrayOfByte);
    }
    return null;
  }
  
  private void parsePdu(byte[] paramArrayOfByte)
  {
    mPdu = paramArrayOfByte;
    PduParser localPduParser = new PduParser(paramArrayOfByte);
    mScAddress = localPduParser.getSCAddress();
    paramArrayOfByte = mScAddress;
    int i = localPduParser.getByte();
    mMti = (i & 0x3);
    switch (mMti)
    {
    default: 
      throw new RuntimeException("Unsupported message type");
    case 2: 
      parseSmsStatusReport(localPduParser, i);
      break;
    case 1: 
      parseSmsSubmit(localPduParser, i);
      break;
    case 0: 
    case 3: 
      parseSmsDeliver(localPduParser, i);
    }
  }
  
  private void parseSmsDeliver(PduParser paramPduParser, int paramInt)
  {
    boolean bool1 = false;
    if ((paramInt & 0x80) == 128) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mReplyPathPresent = bool2;
    mOriginatingAddress = paramPduParser.getAddress();
    SmsAddress localSmsAddress = mOriginatingAddress;
    mProtocolIdentifier = paramPduParser.getByte();
    mDataCodingScheme = paramPduParser.getByte();
    mScTimeMillis = paramPduParser.getSCTimestampMillis();
    boolean bool2 = bool1;
    if ((paramInt & 0x40) == 64) {
      bool2 = true;
    }
    parseUserData(paramPduParser, bool2);
  }
  
  private void parseSmsStatusReport(PduParser paramPduParser, int paramInt)
  {
    boolean bool = true;
    mIsStatusReportMessage = true;
    mMessageRef = paramPduParser.getByte();
    mRecipientAddress = paramPduParser.getAddress();
    mScTimeMillis = paramPduParser.getSCTimestampMillis();
    paramPduParser.getSCTimestampMillis();
    mStatus = paramPduParser.getByte();
    if (paramPduParser.moreDataPresent())
    {
      int i = paramPduParser.getByte();
      for (int j = i; (j & 0x80) != 0; j = paramPduParser.getByte()) {}
      if ((i & 0x78) == 0)
      {
        if ((i & 0x1) != 0) {
          mProtocolIdentifier = paramPduParser.getByte();
        }
        if ((i & 0x2) != 0) {
          mDataCodingScheme = paramPduParser.getByte();
        }
        if ((i & 0x4) != 0)
        {
          if ((paramInt & 0x40) != 64) {
            bool = false;
          }
          parseUserData(paramPduParser, bool);
        }
      }
    }
  }
  
  private void parseSmsSubmit(PduParser paramPduParser, int paramInt)
  {
    boolean bool1 = false;
    if ((paramInt & 0x80) == 128) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mReplyPathPresent = bool2;
    mMessageRef = paramPduParser.getByte();
    mRecipientAddress = paramPduParser.getAddress();
    SmsAddress localSmsAddress = mRecipientAddress;
    mProtocolIdentifier = paramPduParser.getByte();
    mDataCodingScheme = paramPduParser.getByte();
    int i = paramInt >> 3 & 0x3;
    if (i == 0) {
      i = 0;
    } else if (2 == i) {
      i = 1;
    }
    for (i = 7; i > 0; i--) {
      paramPduParser.getByte();
    }
    boolean bool2 = bool1;
    if ((paramInt & 0x40) == 64) {
      bool2 = true;
    }
    parseUserData(paramPduParser, bool2);
  }
  
  private void parseUserData(PduParser paramPduParser, boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = mDataCodingScheme;
    int n = 0;
    int i1;
    Object localObject1;
    boolean bool1;
    if ((m & 0x80) == 0)
    {
      if ((mDataCodingScheme & 0x20) != 0) {
        i1 = 1;
      } else {
        i1 = 0;
      }
      if ((mDataCodingScheme & 0x10) != 0) {
        m = 1;
      } else {
        m = 0;
      }
      i = m;
      if (i1 != 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("4 - Unsupported SMS data coding scheme (compression) ");
        ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
        Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
        m = j;
      }
      else
      {
        switch (mDataCodingScheme >> 2 & 0x3)
        {
        default: 
          m = k;
          break;
        case 2: 
          m = 3;
          break;
        case 1: 
          if (Resources.getSystem().getBoolean(17957034)) {
            m = 2;
          }
          break;
        case 3: 
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("1 - Unsupported SMS data coding scheme ");
          ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
          Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
          m = 2;
          break;
        }
        m = 1;
      }
    }
    else if ((mDataCodingScheme & 0xF0) == 240)
    {
      i = 1;
      if ((mDataCodingScheme & 0x4) == 0) {
        m = 1;
      } else {
        m = 2;
      }
    }
    else if (((mDataCodingScheme & 0xF0) != 192) && ((mDataCodingScheme & 0xF0) != 208) && ((mDataCodingScheme & 0xF0) != 224))
    {
      if ((mDataCodingScheme & 0xC0) == 128)
      {
        if (mDataCodingScheme == 132)
        {
          m = 4;
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("5 - Unsupported SMS data coding scheme ");
          ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
          Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
          m = j;
        }
      }
      else
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("3 - Unsupported SMS data coding scheme ");
        ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
        Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
        m = j;
      }
    }
    else
    {
      if ((mDataCodingScheme & 0xF0) == 224) {
        m = 3;
      } else {
        m = 1;
      }
      if ((mDataCodingScheme & 0x8) == 8) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if ((mDataCodingScheme & 0x3) == 0)
      {
        mIsMwi = true;
        mMwiSense = bool1;
        boolean bool2;
        if ((mDataCodingScheme & 0xF0) == 192) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mMwiDontStore = bool2;
        if (bool1 == true) {
          mVoiceMailCount = -1;
        } else {
          mVoiceMailCount = 0;
        }
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("MWI in DCS for Vmail. DCS = ");
        ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
        ((StringBuilder)localObject1).append(" Dont store = ");
        ((StringBuilder)localObject1).append(mMwiDontStore);
        ((StringBuilder)localObject1).append(" vmail count = ");
        ((StringBuilder)localObject1).append(mVoiceMailCount);
        Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
      }
      else
      {
        mIsMwi = false;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("MWI in DCS for fax/email/other: ");
        ((StringBuilder)localObject1).append(mDataCodingScheme & 0xFF);
        Rlog.w("SmsMessage", ((StringBuilder)localObject1).toString());
      }
    }
    if (m == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    j = paramPduParser.constructUserData(paramBoolean, bool1);
    mUserData = paramPduParser.getUserData();
    mUserDataHeader = paramPduParser.getUserDataHeader();
    if ((paramBoolean) && (mUserDataHeader.specialSmsMsgList.size() != 0))
    {
      localObject1 = mUserDataHeader.specialSmsMsgList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        Object localObject2 = (SmsHeader.SpecialSmsMsg)((Iterator)localObject1).next();
        i1 = msgIndType & 0xFF;
        if ((i1 != 0) && (i1 != 128))
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("TP_UDH fax/email/extended msg/multisubscriber profile. Msg Ind = ");
          ((StringBuilder)localObject2).append(i1);
          Rlog.w("SmsMessage", ((StringBuilder)localObject2).toString());
        }
        else
        {
          mIsMwi = true;
          if (i1 == 128) {
            mMwiDontStore = false;
          }
          for (;;)
          {
            break;
            if (!mMwiDontStore)
            {
              if ((mDataCodingScheme & 0xF0) != 208) {
                if ((mDataCodingScheme & 0xF0) != 224) {
                  break label928;
                }
              }
              if ((mDataCodingScheme & 0x3) != 0) {
                label928:
                mMwiDontStore = true;
              }
            }
          }
          mVoiceMailCount = (msgCount & 0xFF);
          if (mVoiceMailCount > 0) {
            mMwiSense = true;
          } else {
            mMwiSense = false;
          }
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("MWI in TP-UDH for Vmail. Msg Ind = ");
          ((StringBuilder)localObject2).append(i1);
          ((StringBuilder)localObject2).append(" Dont store = ");
          ((StringBuilder)localObject2).append(mMwiDontStore);
          ((StringBuilder)localObject2).append(" Vmail count = ");
          ((StringBuilder)localObject2).append(mVoiceMailCount);
          Rlog.w("SmsMessage", ((StringBuilder)localObject2).toString());
        }
      }
    }
    switch (m)
    {
    default: 
      break;
    case 4: 
      mMessageBody = paramPduParser.getUserDataKSC5601(j);
      break;
    case 3: 
      mMessageBody = paramPduParser.getUserDataUCS2(j);
      break;
    case 2: 
      if (Resources.getSystem().getBoolean(17957034)) {
        mMessageBody = paramPduParser.getUserDataGSM8bit(j);
      } else {
        mMessageBody = null;
      }
      break;
    case 1: 
      if (paramBoolean) {
        m = mUserDataHeader.languageTable;
      } else {
        m = 0;
      }
      i1 = n;
      if (paramBoolean) {
        i1 = mUserDataHeader.languageShiftTable;
      }
      mMessageBody = paramPduParser.getUserDataGSM7Bit(j, m, i1);
      break;
    case 0: 
      mMessageBody = null;
    }
    if (mMessageBody != null) {
      parseMessageBody();
    }
    if (i == 0) {
      messageClass = SmsConstants.MessageClass.UNKNOWN;
    } else {
      switch (mDataCodingScheme & 0x3)
      {
      default: 
        break;
      case 3: 
        messageClass = SmsConstants.MessageClass.CLASS_3;
        break;
      case 2: 
        messageClass = SmsConstants.MessageClass.CLASS_2;
        break;
      case 1: 
        messageClass = SmsConstants.MessageClass.CLASS_1;
        break;
      case 0: 
        messageClass = SmsConstants.MessageClass.CLASS_0;
      }
    }
  }
  
  int getDataCodingScheme()
  {
    return mDataCodingScheme;
  }
  
  public SmsConstants.MessageClass getMessageClass()
  {
    return messageClass;
  }
  
  public int getNumOfVoicemails()
  {
    if ((!mIsMwi) && (isCphsMwiMessage()))
    {
      if ((mOriginatingAddress != null) && (((GsmSmsAddress)mOriginatingAddress).isCphsVoiceMessageSet())) {
        mVoiceMailCount = 255;
      } else {
        mVoiceMailCount = 0;
      }
      Rlog.v("SmsMessage", "CPHS voice mail message");
    }
    return mVoiceMailCount;
  }
  
  public int getProtocolIdentifier()
  {
    return mProtocolIdentifier;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public boolean isCphsMwiMessage()
  {
    boolean bool;
    if ((!((GsmSmsAddress)mOriginatingAddress).isCphsVoiceMessageClear()) && (!((GsmSmsAddress)mOriginatingAddress).isCphsVoiceMessageSet())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isMWIClearMessage()
  {
    boolean bool1 = mIsMwi;
    boolean bool2 = true;
    if ((bool1) && (!mMwiSense)) {
      return true;
    }
    if ((mOriginatingAddress == null) || (!((GsmSmsAddress)mOriginatingAddress).isCphsVoiceMessageClear())) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean isMWISetMessage()
  {
    boolean bool1 = mIsMwi;
    boolean bool2 = true;
    if ((bool1) && (mMwiSense)) {
      return true;
    }
    if ((mOriginatingAddress == null) || (!((GsmSmsAddress)mOriginatingAddress).isCphsVoiceMessageSet())) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean isMwiDontStore()
  {
    if ((mIsMwi) && (mMwiDontStore)) {
      return true;
    }
    return (isCphsMwiMessage()) && (" ".equals(getMessageBody()));
  }
  
  public boolean isReplace()
  {
    boolean bool;
    if (((mProtocolIdentifier & 0xC0) == 64) && ((mProtocolIdentifier & 0x3F) > 0) && ((mProtocolIdentifier & 0x3F) < 8)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReplyPathPresent()
  {
    return mReplyPathPresent;
  }
  
  public boolean isStatusReportMessage()
  {
    return mIsStatusReportMessage;
  }
  
  public boolean isTypeZero()
  {
    boolean bool;
    if (mProtocolIdentifier == 64) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isUsimDataDownload()
  {
    boolean bool;
    if ((messageClass == SmsConstants.MessageClass.CLASS_2) && ((mProtocolIdentifier == 127) || (mProtocolIdentifier == 124))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static class PduParser
  {
    int mCur;
    byte[] mPdu;
    byte[] mUserData;
    SmsHeader mUserDataHeader;
    int mUserDataSeptetPadding;
    
    PduParser(byte[] paramArrayOfByte)
    {
      mPdu = paramArrayOfByte;
      mCur = 0;
      mUserDataSeptetPadding = 0;
    }
    
    int constructUserData(boolean paramBoolean1, boolean paramBoolean2)
    {
      int i = mCur;
      byte[] arrayOfByte = mPdu;
      int j = i + 1;
      int k = arrayOfByte[i] & 0xFF;
      int m = 0;
      i = 0;
      int n = 0;
      int i1 = j;
      if (paramBoolean1)
      {
        arrayOfByte = mPdu;
        int i2 = j + 1;
        i1 = arrayOfByte[j] & 0xFF;
        arrayOfByte = new byte[i1];
        System.arraycopy(mPdu, i2, arrayOfByte, 0, i1);
        mUserDataHeader = SmsHeader.fromByteArray(arrayOfByte);
        j = (i1 + 1) * 8;
        m = j / 7;
        if (j % 7 > 0) {
          i = 1;
        } else {
          i = 0;
        }
        m += i;
        mUserDataSeptetPadding = (m * 7 - j);
        j = i2 + i1;
        i = i1;
        i1 = j;
      }
      if (paramBoolean2)
      {
        i = mPdu.length - i1;
      }
      else
      {
        if (paramBoolean1) {
          i++;
        } else {
          i = 0;
        }
        j = k - i;
        i = j;
        if (j < 0) {
          i = 0;
        }
      }
      mUserData = new byte[i];
      System.arraycopy(mPdu, i1, mUserData, 0, mUserData.length);
      mCur = i1;
      if (paramBoolean2)
      {
        i = k - m;
        if (i < 0) {
          i = n;
        }
        return i;
      }
      return mUserData.length;
    }
    
    GsmSmsAddress getAddress()
    {
      int i = 2 + ((mPdu[mCur] & 0xFF) + 1) / 2;
      try
      {
        GsmSmsAddress localGsmSmsAddress = new GsmSmsAddress(mPdu, mCur, i);
        mCur += i;
        return localGsmSmsAddress;
      }
      catch (ParseException localParseException)
      {
        throw new RuntimeException(localParseException.getMessage());
      }
    }
    
    int getByte()
    {
      byte[] arrayOfByte = mPdu;
      int i = mCur;
      mCur = (i + 1);
      return arrayOfByte[i] & 0xFF;
    }
    
    String getSCAddress()
    {
      int i = getByte();
      String str1;
      String str2;
      if (i == 0) {
        str1 = null;
      } else {
        try
        {
          str1 = PhoneNumberUtils.calledPartyBCDToString(mPdu, mCur, i, 2);
        }
        catch (RuntimeException localRuntimeException)
        {
          Rlog.d("SmsMessage", "invalid SC address: ", localRuntimeException);
          str2 = null;
        }
      }
      mCur += i;
      return str2;
    }
    
    long getSCTimestampMillis()
    {
      Object localObject = mPdu;
      int i = mCur;
      mCur = (i + 1);
      int j = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int k = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int m = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int n = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int i1 = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int i2 = IccUtils.gsmBcdByteToInt(localObject[i]);
      localObject = mPdu;
      i = mCur;
      mCur = (i + 1);
      int i3 = localObject[i];
      i = IccUtils.gsmBcdByteToInt((byte)(i3 & 0xFFFFFFF7));
      if ((i3 & 0x8) != 0) {
        i = -i;
      }
      localObject = new Time("UTC");
      if (j >= 90) {
        j += 1900;
      } else {
        j += 2000;
      }
      year = j;
      month = (k - 1);
      monthDay = m;
      hour = n;
      minute = i1;
      second = i2;
      return ((Time)localObject).toMillis(true) - i * 15 * 60 * 1000;
    }
    
    byte[] getUserData()
    {
      return mUserData;
    }
    
    String getUserDataGSM7Bit(int paramInt1, int paramInt2, int paramInt3)
    {
      String str = GsmAlphabet.gsm7BitPackedToString(mPdu, mCur, paramInt1, mUserDataSeptetPadding, paramInt2, paramInt3);
      mCur += paramInt1 * 7 / 8;
      return str;
    }
    
    String getUserDataGSM8bit(int paramInt)
    {
      String str = GsmAlphabet.gsm8BitUnpackedToString(mPdu, mCur, paramInt);
      mCur += paramInt;
      return str;
    }
    
    SmsHeader getUserDataHeader()
    {
      return mUserDataHeader;
    }
    
    String getUserDataKSC5601(int paramInt)
    {
      String str2;
      try
      {
        String str1 = new java/lang/String;
        str1.<init>(mPdu, mCur, paramInt, "KSC5601");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Rlog.e("SmsMessage", "implausible UnsupportedEncodingException", localUnsupportedEncodingException);
        str2 = "";
      }
      mCur += paramInt;
      return str2;
    }
    
    String getUserDataUCS2(int paramInt)
    {
      String str2;
      try
      {
        String str1 = new java/lang/String;
        str1.<init>(mPdu, mCur, paramInt, "utf-16");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Rlog.e("SmsMessage", "implausible UnsupportedEncodingException", localUnsupportedEncodingException);
        str2 = "";
      }
      mCur += paramInt;
      return str2;
    }
    
    boolean moreDataPresent()
    {
      boolean bool;
      if (mPdu.length > mCur) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static class SubmitPdu
    extends SmsMessageBase.SubmitPduBase
  {
    public SubmitPdu() {}
  }
}
