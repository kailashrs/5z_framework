package com.android.internal.telephony.cdma;

import android.content.res.Resources;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.PortAddrs;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.BearerData.TimeStamp;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.BitwiseInputStream.AccessException;
import com.android.internal.util.HexDump;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SmsMessage
  extends SmsMessageBase
{
  private static final byte BEARER_DATA = 8;
  private static final byte BEARER_REPLY_OPTION = 6;
  private static final byte CAUSE_CODES = 7;
  private static final byte DESTINATION_ADDRESS = 4;
  private static final byte DESTINATION_SUB_ADDRESS = 5;
  private static final String LOGGABLE_TAG = "CDMA:SMS";
  static final String LOG_TAG = "SmsMessage";
  private static final byte ORIGINATING_ADDRESS = 2;
  private static final byte ORIGINATING_SUB_ADDRESS = 3;
  private static final int PRIORITY_EMERGENCY = 3;
  private static final int PRIORITY_INTERACTIVE = 1;
  private static final int PRIORITY_NORMAL = 0;
  private static final int PRIORITY_URGENT = 2;
  private static final int RETURN_ACK = 1;
  private static final int RETURN_NO_ACK = 0;
  private static final byte SERVICE_CATEGORY = 1;
  private static final byte TELESERVICE_IDENTIFIER = 0;
  private static final boolean VDBG = false;
  private BearerData mBearerData;
  private SmsEnvelope mEnvelope;
  private int status;
  
  public SmsMessage() {}
  
  public SmsMessage(SmsAddress paramSmsAddress, SmsEnvelope paramSmsEnvelope)
  {
    mOriginatingAddress = paramSmsAddress;
    mEnvelope = paramSmsEnvelope;
    createPdu();
  }
  
  public static GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str = null;
    if (Resources.getSystem().getBoolean(17957035)) {
      str = Sms7BitEncodingTranslator.translate(paramCharSequence);
    }
    Object localObject = str;
    if (TextUtils.isEmpty(str)) {
      localObject = paramCharSequence;
    }
    return BearerData.calcTextEncodingDetails((CharSequence)localObject, paramBoolean1, paramBoolean2);
  }
  
  public static byte convertDtmfToAscii(byte paramByte)
  {
    byte b;
    switch (paramByte)
    {
    default: 
      paramByte = 32;
      b = paramByte;
      break;
    case 15: 
      paramByte = 67;
      b = paramByte;
      break;
    case 14: 
      paramByte = 66;
      b = paramByte;
      break;
    case 13: 
      paramByte = 65;
      b = paramByte;
      break;
    case 12: 
      paramByte = 35;
      b = paramByte;
      break;
    case 11: 
      paramByte = 42;
      b = paramByte;
      break;
    case 10: 
      paramByte = 48;
      b = paramByte;
      break;
    case 9: 
      paramByte = 57;
      b = paramByte;
      break;
    case 8: 
      paramByte = 56;
      b = paramByte;
      break;
    case 7: 
      paramByte = 55;
      b = paramByte;
      break;
    case 6: 
      paramByte = 54;
      b = paramByte;
      break;
    case 5: 
      paramByte = 53;
      b = paramByte;
      break;
    case 4: 
      paramByte = 52;
      b = paramByte;
      break;
    case 3: 
      paramByte = 51;
      b = paramByte;
      break;
    case 2: 
      paramByte = 50;
      b = paramByte;
      break;
    case 1: 
      paramByte = 49;
      b = paramByte;
      break;
    case 0: 
      paramByte = 68;
      b = paramByte;
    }
    return b;
  }
  
  public static SmsMessage createFromEfRecord(int paramInt, byte[] paramArrayOfByte)
  {
    try
    {
      SmsMessage localSmsMessage = new com/android/internal/telephony/cdma/SmsMessage;
      localSmsMessage.<init>();
      mIndexOnIcc = paramInt;
      if ((paramArrayOfByte[0] & 0x1) == 0)
      {
        Rlog.w("SmsMessage", "SMS parsing failed: Trying to parse a free record");
        return null;
      }
      mStatusOnIcc = (paramArrayOfByte[0] & 0x7);
      paramInt = paramArrayOfByte[1] & 0xFF;
      byte[] arrayOfByte = new byte[paramInt];
      System.arraycopy(paramArrayOfByte, 2, arrayOfByte, 0, paramInt);
      localSmsMessage.parsePduFromEfRecord(arrayOfByte);
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
    SmsMessage localSmsMessage = new SmsMessage();
    try
    {
      localSmsMessage.parsePdu(paramArrayOfByte);
      return localSmsMessage;
    }
    catch (OutOfMemoryError paramArrayOfByte)
    {
      Log.e("SmsMessage", "SMS PDU parsing failed with out of memory: ", paramArrayOfByte);
      return null;
    }
    catch (RuntimeException paramArrayOfByte)
    {
      Rlog.e("SmsMessage", "SMS PDU parsing failed: ", paramArrayOfByte);
    }
    return null;
  }
  
  public static int getNextMessageId()
  {
    try
    {
      int i = SystemProperties.getInt("persist.radio.cdma.msgid", 1);
      Object localObject1 = Integer.toString(i % 65535 + 1);
      try
      {
        SystemProperties.set("persist.radio.cdma.msgid", (String)localObject1);
        if (Rlog.isLoggable("CDMA:SMS", 2))
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("next persist.radio.cdma.msgid = ");
          localStringBuilder.append((String)localObject1);
          Rlog.d("SmsMessage", localStringBuilder.toString());
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("readback gets ");
          localStringBuilder.append(SystemProperties.get("persist.radio.cdma.msgid"));
          Rlog.d("SmsMessage", localStringBuilder.toString());
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("set nextMessage ID failed: ");
        ((StringBuilder)localObject1).append(localRuntimeException);
        Rlog.e("SmsMessage", ((StringBuilder)localObject1).toString());
      }
      return i;
    }
    finally {}
  }
  
  public static SubmitPdu getSubmitPdu(String paramString, UserData paramUserData, boolean paramBoolean)
  {
    return privateGetSubmitPdu(paramString, paramBoolean, paramUserData);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString, UserData paramUserData, boolean paramBoolean, int paramInt)
  {
    return privateGetSubmitPdu(paramString, paramBoolean, paramUserData, paramInt);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    Object localObject = new SmsHeader.PortAddrs();
    destPort = paramInt;
    origPort = 0;
    areEightBits = false;
    paramString1 = new SmsHeader();
    portAddrs = ((SmsHeader.PortAddrs)localObject);
    localObject = new UserData();
    userDataHeader = paramString1;
    msgEncoding = 0;
    msgEncodingSet = true;
    payload = paramArrayOfByte;
    return privateGetSubmitPdu(paramString2, paramBoolean, (UserData)localObject);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader)
  {
    return getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramSmsHeader, -1);
  }
  
  public static SubmitPdu getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt)
  {
    if ((paramString3 != null) && (paramString2 != null))
    {
      paramString1 = new UserData();
      payloadStr = paramString3;
      userDataHeader = paramSmsHeader;
      return privateGetSubmitPdu(paramString2, paramBoolean, paramString1, paramInt);
    }
    return null;
  }
  
  public static int getTPLayerLengthForPDU(String paramString)
  {
    Rlog.w("SmsMessage", "getTPLayerLengthForPDU: is not supported in CDMA mode.");
    return 0;
  }
  
  private void parsePdu(byte[] paramArrayOfByte)
  {
    Object localObject1 = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
    SmsEnvelope localSmsEnvelope = new SmsEnvelope();
    CdmaSmsAddress localCdmaSmsAddress = new CdmaSmsAddress();
    CdmaSmsSubaddress localCdmaSmsSubaddress = new CdmaSmsSubaddress();
    try
    {
      messageType = ((DataInputStream)localObject1).readInt();
      teleService = ((DataInputStream)localObject1).readInt();
      serviceCategory = ((DataInputStream)localObject1).readInt();
      digitMode = ((DataInputStream)localObject1).readByte();
      numberMode = ((DataInputStream)localObject1).readByte();
      ton = ((DataInputStream)localObject1).readByte();
      numberPlan = ((DataInputStream)localObject1).readByte();
      int i = ((DataInputStream)localObject1).readUnsignedByte();
      numberOfDigits = i;
      Object localObject2;
      if (i <= paramArrayOfByte.length)
      {
        origBytes = new byte[i];
        ((DataInputStream)localObject1).read(origBytes, 0, i);
        bearerReply = ((DataInputStream)localObject1).readInt();
        replySeqNo = ((DataInputStream)localObject1).readByte();
        errorClass = ((DataInputStream)localObject1).readByte();
        causeCode = ((DataInputStream)localObject1).readByte();
        i = ((DataInputStream)localObject1).readInt();
        if (i <= paramArrayOfByte.length)
        {
          bearerData = new byte[i];
          ((DataInputStream)localObject1).read(bearerData, 0, i);
          ((DataInputStream)localObject1).close();
        }
        else
        {
          localObject2 = new java/lang/RuntimeException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("createFromPdu: Invalid pdu, bearerDataLength ");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" > pdu len ");
          ((StringBuilder)localObject1).append(paramArrayOfByte.length);
          ((RuntimeException)localObject2).<init>(((StringBuilder)localObject1).toString());
          throw ((Throwable)localObject2);
        }
      }
      else
      {
        localObject1 = new java/lang/RuntimeException;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("createFromPdu: Invalid pdu, addr.numberOfDigits ");
        ((StringBuilder)localObject2).append(i);
        ((StringBuilder)localObject2).append(" > pdu len ");
        ((StringBuilder)localObject2).append(paramArrayOfByte.length);
        ((RuntimeException)localObject1).<init>(((StringBuilder)localObject2).toString());
        throw ((Throwable)localObject1);
      }
    }
    catch (Exception localException)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("createFromPdu: conversion from byte array to object failed: ");
      ((StringBuilder)localObject1).append(localException);
      Rlog.e("SmsMessage", ((StringBuilder)localObject1).toString());
      mOriginatingAddress = localCdmaSmsAddress;
      origAddress = localCdmaSmsAddress;
      origSubaddress = localCdmaSmsSubaddress;
      mEnvelope = localSmsEnvelope;
      mPdu = paramArrayOfByte;
      parseSms();
      return;
    }
    catch (IOException localIOException)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("createFromPdu: conversion from byte array to object failed: ");
      paramArrayOfByte.append(localIOException);
      throw new RuntimeException(paramArrayOfByte.toString(), localIOException);
    }
  }
  
  private void parsePduFromEfRecord(byte[] paramArrayOfByte)
  {
    Object localObject1 = new ByteArrayInputStream(paramArrayOfByte);
    Object localObject2 = new DataInputStream((InputStream)localObject1);
    SmsEnvelope localSmsEnvelope = new SmsEnvelope();
    CdmaSmsAddress localCdmaSmsAddress = new CdmaSmsAddress();
    CdmaSmsSubaddress localCdmaSmsSubaddress = new CdmaSmsSubaddress();
    try
    {
      messageType = ((DataInputStream)localObject2).readByte();
      while (((DataInputStream)localObject2).available() > 0)
      {
        int i = ((DataInputStream)localObject2).readByte();
        int j = ((DataInputStream)localObject2).readUnsignedByte();
        Object localObject3 = new byte[j];
        int k = 0;
        BitwiseInputStream localBitwiseInputStream;
        switch (i)
        {
        default: 
          localObject2 = new java/lang/Exception;
          break;
        case 8: 
          ((DataInputStream)localObject2).read((byte[])localObject3, 0, j);
          bearerData = ((byte[])localObject3);
          break;
        case 7: 
          ((DataInputStream)localObject2).read((byte[])localObject3, 0, j);
          localBitwiseInputStream = new com/android/internal/util/BitwiseInputStream;
          localBitwiseInputStream.<init>((byte[])localObject3);
          replySeqNo = ((byte)localBitwiseInputStream.readByteArray(6)[0]);
          errorClass = ((byte)localBitwiseInputStream.readByteArray(2)[0]);
          if (errorClass != 0) {
            causeCode = ((byte)localBitwiseInputStream.readByteArray(8)[0]);
          }
          break;
        case 6: 
          ((DataInputStream)localObject2).read((byte[])localObject3, 0, j);
          localBitwiseInputStream = new com/android/internal/util/BitwiseInputStream;
          localBitwiseInputStream.<init>((byte[])localObject3);
          bearerReply = localBitwiseInputStream.read(6);
          break;
        case 3: 
        case 5: 
          ((DataInputStream)localObject2).read((byte[])localObject3, 0, j);
          localBitwiseInputStream = new com/android/internal/util/BitwiseInputStream;
          localBitwiseInputStream.<init>((byte[])localObject3);
          type = localBitwiseInputStream.read(3);
          odd = ((byte)localBitwiseInputStream.readByteArray(1)[0]);
          j = localBitwiseInputStream.read(8);
          localObject3 = new byte[j];
          while (k < j)
          {
            localObject3[k] = convertDtmfToAscii((byte)(localBitwiseInputStream.read(4) & 0xFF));
            k++;
          }
          origBytes = ((byte[])localObject3);
          break;
        case 2: 
        case 4: 
          ((DataInputStream)localObject2).read((byte[])localObject3, 0, j);
          localBitwiseInputStream = new com/android/internal/util/BitwiseInputStream;
          localBitwiseInputStream.<init>((byte[])localObject3);
          digitMode = localBitwiseInputStream.read(1);
          numberMode = localBitwiseInputStream.read(1);
          k = 0;
          if (digitMode == 1)
          {
            j = localBitwiseInputStream.read(3);
            ton = j;
            k = j;
            if (numberMode == 0)
            {
              numberPlan = localBitwiseInputStream.read(4);
              k = j;
            }
          }
          numberOfDigits = localBitwiseInputStream.read(8);
          localObject3 = new byte[numberOfDigits];
          if (digitMode == 0) {
            for (k = 0; k < numberOfDigits; k++) {
              localObject3[k] = convertDtmfToAscii((byte)(0xF & localBitwiseInputStream.read(4)));
            }
          }
          if (digitMode == 1)
          {
            if (numberMode == 0) {
              for (k = 0; k < numberOfDigits; k++) {
                localObject3[k] = ((byte)(byte)(0xFF & localBitwiseInputStream.read(8)));
              }
            }
            if (numberMode == 1)
            {
              if (k == 2) {
                Rlog.e("SmsMessage", "TODO: Originating Addr is email id");
              } else {
                Rlog.e("SmsMessage", "TODO: Originating Addr is data network address");
              }
            }
            else {
              Rlog.e("SmsMessage", "Originating Addr is of incorrect type");
            }
          }
          else
          {
            Rlog.e("SmsMessage", "Incorrect Digit mode");
          }
          origBytes = ((byte[])localObject3);
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("Originating Addr=");
          ((StringBuilder)localObject3).append(localCdmaSmsAddress.toString());
          Rlog.i("SmsMessage", ((StringBuilder)localObject3).toString());
          if (i == 4)
          {
            destAddress = localCdmaSmsAddress;
            mRecipientAddress = localCdmaSmsAddress;
          }
          break;
        case 1: 
          serviceCategory = ((DataInputStream)localObject2).readUnsignedShort();
          break;
        case 0: 
          teleService = ((DataInputStream)localObject2).readUnsignedShort();
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("teleservice = ");
          ((StringBuilder)localObject3).append(teleService);
          Rlog.i("SmsMessage", ((StringBuilder)localObject3).toString());
        }
        continue;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("unsupported parameterId (");
        ((StringBuilder)localObject1).append(i);
        ((StringBuilder)localObject1).append(")");
        ((Exception)localObject2).<init>(((StringBuilder)localObject1).toString());
        throw ((Throwable)localObject2);
      }
      ((ByteArrayInputStream)localObject1).close();
      ((DataInputStream)localObject2).close();
    }
    catch (Exception localException)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("parsePduFromEfRecord: conversion from pdu to SmsMessage failed");
      ((StringBuilder)localObject2).append(localException);
      Rlog.e("SmsMessage", ((StringBuilder)localObject2).toString());
    }
    mOriginatingAddress = localCdmaSmsAddress;
    origAddress = localCdmaSmsAddress;
    origSubaddress = localCdmaSmsSubaddress;
    mEnvelope = localSmsEnvelope;
    mPdu = paramArrayOfByte;
    parseSms();
  }
  
  private static SubmitPdu privateGetSubmitPdu(String paramString, boolean paramBoolean, UserData paramUserData)
  {
    return privateGetSubmitPdu(paramString, paramBoolean, paramUserData, -1);
  }
  
  private static SubmitPdu privateGetSubmitPdu(String paramString, boolean paramBoolean, UserData paramUserData, int paramInt)
  {
    CdmaSmsAddress localCdmaSmsAddress = CdmaSmsAddress.parse(PhoneNumberUtils.cdmaCheckAndProcessPlusCodeForSms(paramString));
    if (localCdmaSmsAddress == null) {
      return null;
    }
    Object localObject1 = new BearerData();
    messageType = 2;
    messageId = getNextMessageId();
    deliveryAckReq = paramBoolean;
    userAckReq = false;
    readAckReq = false;
    reportReq = false;
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      priorityIndicatorSet = true;
      priority = paramInt;
    }
    userData = paramUserData;
    paramString = BearerData.encode((BearerData)localObject1);
    if (Rlog.isLoggable("CDMA:SMS", 2))
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("MO (encoded) BearerData = ");
      ((StringBuilder)localObject2).append(localObject1);
      Rlog.d("SmsMessage", ((StringBuilder)localObject2).toString());
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("MO raw BearerData = '");
      ((StringBuilder)localObject2).append(HexDump.toHexString(paramString));
      ((StringBuilder)localObject2).append("'");
      Rlog.d("SmsMessage", ((StringBuilder)localObject2).toString());
    }
    if (paramString == null) {
      return null;
    }
    if ((hasUserDataHeader) && (msgEncoding != 2)) {
      paramInt = 4101;
    } else {
      paramInt = 4098;
    }
    Object localObject2 = new SmsEnvelope();
    messageType = 0;
    teleService = paramInt;
    destAddress = localCdmaSmsAddress;
    bearerReply = 1;
    bearerData = paramString;
    try
    {
      paramUserData = new java/io/ByteArrayOutputStream;
      paramUserData.<init>(100);
      localObject1 = new java/io/DataOutputStream;
      ((DataOutputStream)localObject1).<init>(paramUserData);
      ((DataOutputStream)localObject1).writeInt(teleService);
      ((DataOutputStream)localObject1).writeInt(0);
      ((DataOutputStream)localObject1).writeInt(0);
      ((DataOutputStream)localObject1).write(digitMode);
      ((DataOutputStream)localObject1).write(numberMode);
      ((DataOutputStream)localObject1).write(ton);
      ((DataOutputStream)localObject1).write(numberPlan);
      ((DataOutputStream)localObject1).write(numberOfDigits);
      ((DataOutputStream)localObject1).write(origBytes, 0, origBytes.length);
      ((DataOutputStream)localObject1).write(0);
      ((DataOutputStream)localObject1).write(0);
      ((DataOutputStream)localObject1).write(0);
      ((DataOutputStream)localObject1).write(paramString.length);
      ((DataOutputStream)localObject1).write(paramString, 0, paramString.length);
      ((DataOutputStream)localObject1).close();
      paramString = new com/android/internal/telephony/cdma/SmsMessage$SubmitPdu;
      paramString.<init>();
      encodedMessage = paramUserData.toByteArray();
      encodedScAddress = null;
      return paramString;
    }
    catch (IOException paramUserData)
    {
      paramString = new StringBuilder();
      paramString.append("creating SubmitPdu failed: ");
      paramString.append(paramUserData);
      Rlog.e("SmsMessage", paramString.toString());
    }
    return null;
  }
  
  public void createPdu()
  {
    Object localObject = mEnvelope;
    CdmaSmsAddress localCdmaSmsAddress = origAddress;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(100);
    DataOutputStream localDataOutputStream = new DataOutputStream(new BufferedOutputStream(localByteArrayOutputStream));
    try
    {
      localDataOutputStream.writeInt(messageType);
      localDataOutputStream.writeInt(teleService);
      localDataOutputStream.writeInt(serviceCategory);
      localDataOutputStream.writeByte(digitMode);
      localDataOutputStream.writeByte(numberMode);
      localDataOutputStream.writeByte(ton);
      localDataOutputStream.writeByte(numberPlan);
      localDataOutputStream.writeByte(numberOfDigits);
      localDataOutputStream.write(origBytes, 0, origBytes.length);
      localDataOutputStream.writeInt(bearerReply);
      localDataOutputStream.writeByte(replySeqNo);
      localDataOutputStream.writeByte(errorClass);
      localDataOutputStream.writeByte(causeCode);
      localDataOutputStream.writeInt(bearerData.length);
      localDataOutputStream.write(bearerData, 0, bearerData.length);
      localDataOutputStream.close();
      mPdu = localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("createPdu: conversion from object to byte array failed: ");
      ((StringBuilder)localObject).append(localIOException);
      Rlog.e("SmsMessage", ((StringBuilder)localObject).toString());
    }
  }
  
  public byte[] getIncomingSmsFingerprint()
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(mEnvelope.serviceCategory);
    localByteArrayOutputStream.write(mEnvelope.teleService);
    localByteArrayOutputStream.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
    localByteArrayOutputStream.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);
    if ((mEnvelope.origSubaddress != null) && (mEnvelope.origSubaddress.origBytes != null)) {
      localByteArrayOutputStream.write(mEnvelope.origSubaddress.origBytes, 0, mEnvelope.origSubaddress.origBytes.length);
    }
    return localByteArrayOutputStream.toByteArray();
  }
  
  public SmsConstants.MessageClass getMessageClass()
  {
    if (mBearerData.displayMode == 0) {
      return SmsConstants.MessageClass.CLASS_0;
    }
    return SmsConstants.MessageClass.UNKNOWN;
  }
  
  public int getMessageType()
  {
    if (mEnvelope.serviceCategory != 0) {
      return 1;
    }
    return 0;
  }
  
  public int getNumOfVoicemails()
  {
    return mBearerData.numberOfMessages;
  }
  
  public int getProtocolIdentifier()
  {
    Rlog.w("SmsMessage", "getProtocolIdentifier: is not supported in CDMA mode.");
    return 0;
  }
  
  public ArrayList<CdmaSmsCbProgramData> getSmsCbProgramData()
  {
    return mBearerData.serviceCategoryProgramData;
  }
  
  public int getStatus()
  {
    return status << 16;
  }
  
  public int getTeleService()
  {
    return mEnvelope.teleService;
  }
  
  public boolean isCphsMwiMessage()
  {
    Rlog.w("SmsMessage", "isCphsMwiMessage: is not supported in CDMA mode.");
    return false;
  }
  
  public boolean isMWIClearMessage()
  {
    boolean bool;
    if ((mBearerData != null) && (mBearerData.numberOfMessages == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMWISetMessage()
  {
    boolean bool;
    if ((mBearerData != null) && (mBearerData.numberOfMessages > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMwiDontStore()
  {
    boolean bool;
    if ((mBearerData != null) && (mBearerData.numberOfMessages > 0) && (mBearerData.userData == null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReplace()
  {
    Rlog.w("SmsMessage", "isReplace: is not supported in CDMA mode.");
    return false;
  }
  
  public boolean isReplyPathPresent()
  {
    Rlog.w("SmsMessage", "isReplyPathPresent: is not supported in CDMA mode.");
    return false;
  }
  
  public boolean isStatusReportMessage()
  {
    boolean bool;
    if (mBearerData.messageType == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public SmsCbMessage parseBroadcastSms()
  {
    BearerData localBearerData = BearerData.decode(mEnvelope.bearerData, mEnvelope.serviceCategory);
    if (localBearerData == null)
    {
      Rlog.w("SmsMessage", "BearerData.decode() returned null");
      return null;
    }
    if (Rlog.isLoggable("CDMA:SMS", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("MT raw BearerData = ");
      ((StringBuilder)localObject).append(HexDump.toHexString(mEnvelope.bearerData));
      Rlog.d("SmsMessage", ((StringBuilder)localObject).toString());
    }
    Object localObject = new SmsCbLocation(TelephonyManager.getDefault().getNetworkOperator());
    return new SmsCbMessage(2, 1, messageId, (SmsCbLocation)localObject, mEnvelope.serviceCategory, localBearerData.getLanguage(), userData.payloadStr, priority, null, cmasWarningInfo);
  }
  
  public void parseSms()
  {
    if (mEnvelope.teleService == 262144)
    {
      mBearerData = new BearerData();
      if (mEnvelope.bearerData != null) {
        mBearerData.numberOfMessages = (mEnvelope.bearerData[0] & 0xFF);
      }
      return;
    }
    mBearerData = BearerData.decode(mEnvelope.bearerData);
    Object localObject1;
    if (Rlog.isLoggable("CDMA:SMS", 2))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("MT raw BearerData = '");
      ((StringBuilder)localObject1).append(HexDump.toHexString(mEnvelope.bearerData));
      ((StringBuilder)localObject1).append("'");
      Rlog.d("SmsMessage", ((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("MT (decoded) BearerData = ");
      ((StringBuilder)localObject1).append(mBearerData);
      Rlog.d("SmsMessage", ((StringBuilder)localObject1).toString());
    }
    mMessageRef = mBearerData.messageId;
    if (mBearerData.userData != null)
    {
      mUserData = mBearerData.userData.payload;
      mUserDataHeader = mBearerData.userData.userDataHeader;
      mMessageBody = mBearerData.userData.payloadStr;
    }
    Object localObject2;
    if (mOriginatingAddress != null)
    {
      mOriginatingAddress.address = new String(mOriginatingAddress.origBytes);
      if ((mOriginatingAddress.ton == 1) && (mOriginatingAddress.address.charAt(0) != '+'))
      {
        localObject2 = mOriginatingAddress;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("+");
        ((StringBuilder)localObject1).append(mOriginatingAddress.address);
        address = ((StringBuilder)localObject1).toString();
      }
    }
    if (mBearerData.msgCenterTimeStamp != null) {
      mScTimeMillis = mBearerData.msgCenterTimeStamp.toMillis(true);
    }
    if (mBearerData.messageType == 4)
    {
      if (!mBearerData.messageStatusSet)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("DELIVERY_ACK message without msgStatus (");
        if (mUserData == null) {
          localObject1 = "also missing";
        } else {
          localObject1 = "does have";
        }
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" userData).");
        Rlog.d("SmsMessage", ((StringBuilder)localObject2).toString());
        status = 0;
      }
      else
      {
        status = (mBearerData.errorClass << 8);
        status |= mBearerData.messageStatus;
      }
    }
    else if ((mBearerData.messageType != 1) && (mBearerData.messageType != 2))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Unsupported message type: ");
      ((StringBuilder)localObject1).append(mBearerData.messageType);
      throw new RuntimeException(((StringBuilder)localObject1).toString());
    }
    if (mMessageBody != null) {
      parseMessageBody();
    } else {
      localObject1 = mUserData;
    }
  }
  
  protected boolean processCdmaCTWdpHeader(SmsMessage paramSmsMessage)
  {
    boolean bool1 = false;
    boolean bool2;
    try
    {
      BitwiseInputStream localBitwiseInputStream = new com/android/internal/util/BitwiseInputStream;
      localBitwiseInputStream.<init>(paramSmsMessage.getUserData());
      if (localBitwiseInputStream.read(8) != 0)
      {
        Rlog.e("SmsMessage", "Invalid WDP SubparameterId");
        return false;
      }
      if (localBitwiseInputStream.read(8) != 3)
      {
        Rlog.e("SmsMessage", "Invalid WDP subparameter length");
        return false;
      }
      mBearerData.messageType = localBitwiseInputStream.read(4);
      int i = localBitwiseInputStream.read(8) << 8 | localBitwiseInputStream.read(8);
      BearerData localBearerData = mBearerData;
      bool2 = true;
      if (localBitwiseInputStream.read(1) != 1) {
        bool2 = false;
      }
      hasUserDataHeader = bool2;
      if (mBearerData.hasUserDataHeader)
      {
        Rlog.e("SmsMessage", "Invalid WDP UserData header value");
        return false;
      }
      localBitwiseInputStream.skip(3);
      mBearerData.messageId = i;
      mMessageRef = i;
      localBitwiseInputStream.read(8);
      i = localBitwiseInputStream.read(8);
      mBearerData.userData.msgEncoding = localBitwiseInputStream.read(5);
      if (mBearerData.userData.msgEncoding != 0)
      {
        Rlog.e("SmsMessage", "Invalid WDP encoding");
        return false;
      }
      mBearerData.userData.numFields = localBitwiseInputStream.read(8);
      i = i * 8 - (5 + 8);
      int j = 8 * mBearerData.userData.numFields;
      if (j < i) {
        i = j;
      }
      mBearerData.userData.payload = localBitwiseInputStream.readByteArray(i);
      mUserData = mBearerData.userData.payload;
      bool2 = true;
    }
    catch (BitwiseInputStream.AccessException localAccessException)
    {
      paramSmsMessage = new StringBuilder();
      paramSmsMessage.append("CT WDP Header decode failed: ");
      paramSmsMessage.append(localAccessException);
      Rlog.e("SmsMessage", paramSmsMessage.toString());
      bool2 = bool1;
    }
    return bool2;
  }
  
  public static class SubmitPdu
    extends SmsMessageBase.SubmitPduBase
  {
    public SubmitPdu() {}
  }
}
