package com.google.android.mms.pdu;

import android.util.Log;
import com.google.android.mms.InvalidHeaderValueException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

public class PduParser
{
  private static final boolean DEBUG = false;
  private static final int END_STRING_FLAG = 0;
  private static final int LENGTH_QUOTE = 31;
  private static final boolean LOCAL_LOGV = false;
  private static final String LOG_TAG = "PduParser";
  private static final int LONG_INTEGER_LENGTH_MAX = 8;
  private static final int QUOTE = 127;
  private static final int QUOTED_STRING_FLAG = 34;
  private static final int SHORT_INTEGER_MAX = 127;
  private static final int SHORT_LENGTH_MAX = 30;
  private static final int TEXT_MAX = 127;
  private static final int TEXT_MIN = 32;
  private static final int THE_FIRST_PART = 0;
  private static final int THE_LAST_PART = 1;
  private static final int TYPE_QUOTED_STRING = 1;
  private static final int TYPE_TEXT_STRING = 0;
  private static final int TYPE_TOKEN_STRING = 2;
  private static byte[] mStartParam = null;
  private static byte[] mTypeParam = null;
  private PduBody mBody = null;
  private PduHeaders mHeaders = null;
  private final boolean mParseContentDisposition;
  private ByteArrayInputStream mPduDataStream = null;
  
  public PduParser(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    mPduDataStream = new ByteArrayInputStream(paramArrayOfByte);
    mParseContentDisposition = paramBoolean;
  }
  
  protected static boolean checkMandatoryHeader(PduHeaders paramPduHeaders)
  {
    if (paramPduHeaders == null) {
      return false;
    }
    int i = paramPduHeaders.getOctet(140);
    if (paramPduHeaders.getOctet(141) == 0) {
      return false;
    }
    switch (i)
    {
    default: 
      return false;
    case 136: 
      if (-1L == paramPduHeaders.getLongInteger(133)) {
        return false;
      }
      if (paramPduHeaders.getEncodedStringValue(137) == null) {
        return false;
      }
      if (paramPduHeaders.getTextString(139) == null) {
        return false;
      }
      if (paramPduHeaders.getOctet(155) == 0) {
        return false;
      }
      if (paramPduHeaders.getEncodedStringValues(151) == null) {
        return false;
      }
      break;
    case 135: 
      if (paramPduHeaders.getEncodedStringValue(137) == null) {
        return false;
      }
      if (paramPduHeaders.getTextString(139) == null) {
        return false;
      }
      if (paramPduHeaders.getOctet(155) == 0) {
        return false;
      }
      if (paramPduHeaders.getEncodedStringValues(151) == null) {
        return false;
      }
      break;
    case 134: 
      if (-1L == paramPduHeaders.getLongInteger(133)) {
        return false;
      }
      if (paramPduHeaders.getTextString(139) == null) {
        return false;
      }
      if (paramPduHeaders.getOctet(149) == 0) {
        return false;
      }
      if (paramPduHeaders.getEncodedStringValues(151) == null) {
        return false;
      }
      break;
    case 133: 
      if (paramPduHeaders.getTextString(152) == null) {
        return false;
      }
      break;
    case 132: 
      if (paramPduHeaders.getTextString(132) == null) {
        return false;
      }
      if (-1L == paramPduHeaders.getLongInteger(133)) {
        return false;
      }
      break;
    case 131: 
      if (paramPduHeaders.getOctet(149) == 0) {
        return false;
      }
      if (paramPduHeaders.getTextString(152) == null) {
        return false;
      }
      break;
    case 130: 
      if (paramPduHeaders.getTextString(131) == null) {
        return false;
      }
      if (-1L == paramPduHeaders.getLongInteger(136)) {
        return false;
      }
      if (paramPduHeaders.getTextString(138) == null) {
        return false;
      }
      if (-1L == paramPduHeaders.getLongInteger(142)) {
        return false;
      }
      if (paramPduHeaders.getTextString(152) == null) {
        return false;
      }
      break;
    case 129: 
      if (paramPduHeaders.getOctet(146) == 0) {
        return false;
      }
      if (paramPduHeaders.getTextString(152) == null) {
        return false;
      }
      break;
    case 128: 
      if (paramPduHeaders.getTextString(132) == null) {
        return false;
      }
      if (paramPduHeaders.getEncodedStringValue(137) == null) {
        return false;
      }
      if (paramPduHeaders.getTextString(152) == null) {
        return false;
      }
      break;
    }
    return true;
  }
  
  private static int checkPartPosition(PduPart paramPduPart)
  {
    if ((mTypeParam == null) && (mStartParam == null)) {
      return 1;
    }
    if (mStartParam != null)
    {
      paramPduPart = paramPduPart.getContentId();
      if ((paramPduPart != null) && (true == Arrays.equals(mStartParam, paramPduPart))) {
        return 0;
      }
      return 1;
    }
    if (mTypeParam != null)
    {
      paramPduPart = paramPduPart.getContentType();
      if ((paramPduPart != null) && (true == Arrays.equals(mTypeParam, paramPduPart))) {
        return 0;
      }
    }
    return 1;
  }
  
  protected static int extractByteValue(ByteArrayInputStream paramByteArrayInputStream)
  {
    int i = paramByteArrayInputStream.read();
    return i & 0xFF;
  }
  
  protected static byte[] getWapString(ByteArrayInputStream paramByteArrayInputStream, int paramInt)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = paramByteArrayInputStream.read(); (-1 != i) && (i != 0); i = paramByteArrayInputStream.read()) {
      if (paramInt == 2)
      {
        if (isTokenCharacter(i)) {
          localByteArrayOutputStream.write(i);
        }
      }
      else if (isText(i)) {
        localByteArrayOutputStream.write(i);
      }
    }
    if (localByteArrayOutputStream.size() > 0) {
      return localByteArrayOutputStream.toByteArray();
    }
    return null;
  }
  
  protected static boolean isText(int paramInt)
  {
    if (((paramInt >= 32) && (paramInt <= 126)) || ((paramInt >= 128) && (paramInt <= 255))) {
      return true;
    }
    if (paramInt != 13) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  protected static boolean isTokenCharacter(int paramInt)
  {
    if ((paramInt >= 33) && (paramInt <= 126))
    {
      if ((paramInt != 34) && (paramInt != 44) && (paramInt != 47) && (paramInt != 123) && (paramInt != 125)) {
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            switch (paramInt)
            {
            default: 
              return true;
            }
            break;
          }
          break;
        }
      }
      return false;
    }
    return false;
  }
  
  private static void log(String paramString) {}
  
  protected static byte[] parseContentType(ByteArrayInputStream paramByteArrayInputStream, HashMap<Integer, Object> paramHashMap)
  {
    paramByteArrayInputStream.mark(1);
    int i = paramByteArrayInputStream.read();
    paramByteArrayInputStream.reset();
    i &= 0xFF;
    byte[] arrayOfByte;
    if (i < 32)
    {
      i = parseValueLength(paramByteArrayInputStream);
      int j = paramByteArrayInputStream.available();
      paramByteArrayInputStream.mark(1);
      int k = paramByteArrayInputStream.read();
      paramByteArrayInputStream.reset();
      k &= 0xFF;
      if ((k >= 32) && (k <= 127))
      {
        arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
      }
      else
      {
        if (k <= 127) {
          break label176;
        }
        k = parseShortInteger(paramByteArrayInputStream);
        if (k < PduContentTypes.contentTypes.length)
        {
          arrayOfByte = PduContentTypes.contentTypes[k].getBytes();
        }
        else
        {
          paramByteArrayInputStream.reset();
          arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
        }
      }
      i -= j - paramByteArrayInputStream.available();
      if (i > 0) {
        parseContentTypeParams(paramByteArrayInputStream, paramHashMap, Integer.valueOf(i));
      }
      if (i < 0)
      {
        Log.e("PduParser", "Corrupt MMS message");
        return PduContentTypes.contentTypes[0].getBytes();
      }
      break label222;
      label176:
      Log.e("PduParser", "Corrupt content-type");
      return PduContentTypes.contentTypes[0].getBytes();
    }
    else if (i <= 127)
    {
      arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
    }
    else
    {
      arrayOfByte = PduContentTypes.contentTypes[parseShortInteger(paramByteArrayInputStream)].getBytes();
    }
    label222:
    return arrayOfByte;
  }
  
  protected static void parseContentTypeParams(ByteArrayInputStream paramByteArrayInputStream, HashMap<Integer, Object> paramHashMap, Integer paramInteger)
  {
    int i = paramByteArrayInputStream.available();
    int j = paramInteger.intValue();
    while (j > 0)
    {
      int k = paramByteArrayInputStream.read();
      j--;
      byte[] arrayOfByte;
      if (k != 129)
      {
        if (k != 131)
        {
          if ((k != 133) && (k != 151)) {
            if (k == 153) {}
          }
          switch (k)
          {
          default: 
            if (-1 == skipWapValue(paramByteArrayInputStream, j)) {
              Log.e("PduParser", "Corrupt Content-Type");
            } else {
              j = 0;
            }
            break;
          case 138: 
            arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
            if ((arrayOfByte != null) && (paramHashMap != null)) {
              paramHashMap.put(Integer.valueOf(153), arrayOfByte);
            }
            j = paramByteArrayInputStream.available();
            j = paramInteger.intValue() - (i - j);
            break label210;
            arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
            if ((arrayOfByte != null) && (paramHashMap != null)) {
              paramHashMap.put(Integer.valueOf(151), arrayOfByte);
            }
            j = paramByteArrayInputStream.available();
            j = paramInteger.intValue() - (i - j);
            label210:
            break;
          }
        }
        else
        {
          paramByteArrayInputStream.mark(1);
          j = extractByteValue(paramByteArrayInputStream);
          paramByteArrayInputStream.reset();
          if (j > 127)
          {
            j = parseShortInteger(paramByteArrayInputStream);
            if (j < PduContentTypes.contentTypes.length) {
              paramHashMap.put(Integer.valueOf(131), PduContentTypes.contentTypes[j].getBytes());
            }
          }
          else
          {
            arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
            if ((arrayOfByte != null) && (paramHashMap != null)) {
              paramHashMap.put(Integer.valueOf(131), arrayOfByte);
            }
          }
          j = paramByteArrayInputStream.available();
        }
      }
      else {
        for (j = paramInteger.intValue() - (i - j);; j = paramInteger.intValue() - (i - j))
        {
          break;
          paramByteArrayInputStream.mark(1);
          j = extractByteValue(paramByteArrayInputStream);
          paramByteArrayInputStream.reset();
          if (((j > 32) && (j < 127)) || (j == 0))
          {
            arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
            try
            {
              String str = new java/lang/String;
              str.<init>(arrayOfByte);
              paramHashMap.put(Integer.valueOf(129), Integer.valueOf(CharacterSets.getMibEnumValue(str)));
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException)
            {
              Log.e("PduParser", Arrays.toString(arrayOfByte), localUnsupportedEncodingException);
              paramHashMap.put(Integer.valueOf(129), Integer.valueOf(0));
            }
          }
          else
          {
            j = (int)parseIntegerValue(paramByteArrayInputStream);
            if (paramHashMap != null) {
              paramHashMap.put(Integer.valueOf(129), Integer.valueOf(j));
            }
          }
          j = paramByteArrayInputStream.available();
        }
      }
    }
    if (j != 0) {
      Log.e("PduParser", "Corrupt Content-Type");
    }
  }
  
  protected static EncodedStringValue parseEncodedStringValue(ByteArrayInputStream paramByteArrayInputStream)
  {
    paramByteArrayInputStream.mark(1);
    int i = 0;
    int j = paramByteArrayInputStream.read();
    j &= 0xFF;
    if (j == 0) {
      return new EncodedStringValue("");
    }
    paramByteArrayInputStream.reset();
    if (j < 32)
    {
      parseValueLength(paramByteArrayInputStream);
      i = parseShortInteger(paramByteArrayInputStream);
    }
    paramByteArrayInputStream = parseWapString(paramByteArrayInputStream, 0);
    if (i != 0) {
      try
      {
        paramByteArrayInputStream = new EncodedStringValue(i, paramByteArrayInputStream);
      }
      catch (Exception paramByteArrayInputStream)
      {
        break label90;
      }
    } else {
      paramByteArrayInputStream = new EncodedStringValue(paramByteArrayInputStream);
    }
    return paramByteArrayInputStream;
    label90:
    return null;
  }
  
  protected static long parseIntegerValue(ByteArrayInputStream paramByteArrayInputStream)
  {
    paramByteArrayInputStream.mark(1);
    int i = paramByteArrayInputStream.read();
    paramByteArrayInputStream.reset();
    if (i > 127) {
      return parseShortInteger(paramByteArrayInputStream);
    }
    return parseLongInteger(paramByteArrayInputStream);
  }
  
  protected static long parseLongInteger(ByteArrayInputStream paramByteArrayInputStream)
  {
    int i = paramByteArrayInputStream.read();
    int j = i & 0xFF;
    if (j <= 8)
    {
      long l = 0L;
      for (i = 0; i < j; i++)
      {
        int k = paramByteArrayInputStream.read();
        l = (l << 8) + (k & 0xFF);
      }
      return l;
    }
    throw new RuntimeException("Octet count greater than 8 and I can't represent that!");
  }
  
  protected static int parseShortInteger(ByteArrayInputStream paramByteArrayInputStream)
  {
    int i = paramByteArrayInputStream.read();
    return i & 0x7F;
  }
  
  protected static int parseUnsignedInt(ByteArrayInputStream paramByteArrayInputStream)
  {
    int i = 0;
    int j = paramByteArrayInputStream.read();
    int k = j;
    if (j == -1) {
      return j;
    }
    while ((k & 0x80) != 0)
    {
      i = i << 7 | k & 0x7F;
      j = paramByteArrayInputStream.read();
      k = j;
      if (j == -1) {
        return j;
      }
    }
    return i << 7 | k & 0x7F;
  }
  
  protected static int parseValueLength(ByteArrayInputStream paramByteArrayInputStream)
  {
    int i = paramByteArrayInputStream.read();
    i &= 0xFF;
    if (i <= 30) {
      return i;
    }
    if (i == 31) {
      return parseUnsignedInt(paramByteArrayInputStream);
    }
    throw new RuntimeException("Value length > LENGTH_QUOTE!");
  }
  
  protected static byte[] parseWapString(ByteArrayInputStream paramByteArrayInputStream, int paramInt)
  {
    paramByteArrayInputStream.mark(1);
    int i = paramByteArrayInputStream.read();
    if ((1 == paramInt) && (34 == i)) {
      paramByteArrayInputStream.mark(1);
    } else if ((paramInt == 0) && (127 == i)) {
      paramByteArrayInputStream.mark(1);
    } else {
      paramByteArrayInputStream.reset();
    }
    return getWapString(paramByteArrayInputStream, paramInt);
  }
  
  protected static int skipWapValue(ByteArrayInputStream paramByteArrayInputStream, int paramInt)
  {
    int i = paramByteArrayInputStream.read(new byte[paramInt], 0, paramInt);
    if (i < paramInt) {
      return -1;
    }
    return i;
  }
  
  public GenericPdu parse()
  {
    if (mPduDataStream == null) {
      return null;
    }
    mHeaders = parseHeaders(mPduDataStream);
    if (mHeaders == null) {
      return null;
    }
    int i = mHeaders.getOctet(140);
    if (!checkMandatoryHeader(mHeaders))
    {
      log("check mandatory headers failed!");
      return null;
    }
    if ((128 == i) || (132 == i))
    {
      mBody = parseParts(mPduDataStream);
      if (mBody == null) {
        return null;
      }
    }
    switch (i)
    {
    default: 
      log("Parser doesn't support this message type in this version!");
      return null;
    case 136: 
      return new ReadOrigInd(mHeaders);
    case 135: 
      return new ReadRecInd(mHeaders);
    case 134: 
      return new DeliveryInd(mHeaders);
    case 133: 
      return new AcknowledgeInd(mHeaders);
    case 132: 
      RetrieveConf localRetrieveConf = new RetrieveConf(mHeaders, mBody);
      Object localObject = localRetrieveConf.getContentType();
      if (localObject == null) {
        return null;
      }
      localObject = new String((byte[])localObject);
      if ((!((String)localObject).equals("application/vnd.wap.multipart.mixed")) && (!((String)localObject).equals("application/vnd.wap.multipart.related")) && (!((String)localObject).equals("application/vnd.wap.multipart.alternative")))
      {
        if (((String)localObject).equals("application/vnd.wap.multipart.alternative"))
        {
          localObject = mBody.getPart(0);
          mBody.removeAll();
          mBody.addPart(0, (PduPart)localObject);
          return localRetrieveConf;
        }
        return null;
      }
      return localRetrieveConf;
    case 131: 
      return new NotifyRespInd(mHeaders);
    case 130: 
      return new NotificationInd(mHeaders);
    case 129: 
      return new SendConf(mHeaders);
    }
    return new SendReq(mHeaders, mBody);
  }
  
  protected PduHeaders parseHeaders(ByteArrayInputStream paramByteArrayInputStream)
  {
    if (paramByteArrayInputStream == null) {
      return null;
    }
    int i = 1;
    PduHeaders localPduHeaders = new PduHeaders();
    while ((i != 0) && (paramByteArrayInputStream.available() > 0))
    {
      paramByteArrayInputStream.mark(1);
      int j = extractByteValue(paramByteArrayInputStream);
      if ((j >= 32) && (j <= 127))
      {
        paramByteArrayInputStream.reset();
        parseWapString(paramByteArrayInputStream, 0);
      }
      else
      {
        int k;
        label957:
        label1168:
        label1200:
        label1206:
        Object localObject4;
        Object localObject5;
        label1416:
        Object localObject2;
        switch (j)
        {
        case 168: 
        case 174: 
        case 176: 
        default: 
          log("Unknown header");
          k = i;
          break;
        case 178: 
          parseContentType(paramByteArrayInputStream, null);
          k = i;
          break;
        case 173: 
        case 175: 
        case 179: 
          try
          {
            localPduHeaders.setLongInteger(parseIntegerValue(paramByteArrayInputStream), j);
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Long-Integer header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 170: 
        case 172: 
          parseValueLength(paramByteArrayInputStream);
          extractByteValue(paramByteArrayInputStream);
          try
          {
            parseIntegerValue(paramByteArrayInputStream);
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append(" is not Integer-Value");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 164: 
          parseValueLength(paramByteArrayInputStream);
          extractByteValue(paramByteArrayInputStream);
          parseEncodedStringValue(paramByteArrayInputStream);
          k = i;
          break;
        case 161: 
          parseValueLength(paramByteArrayInputStream);
          try
          {
            parseIntegerValue(paramByteArrayInputStream);
            try
            {
              localPduHeaders.setLongInteger(parseLongInteger(paramByteArrayInputStream), 161);
              k = i;
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Long-Integer header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            parseValueLength(paramByteArrayInputStream);
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append(" is not Integer-Value");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 160: 
          try
          {
            parseIntegerValue(paramByteArrayInputStream);
            EncodedStringValue localEncodedStringValue1 = parseEncodedStringValue(paramByteArrayInputStream);
            k = i;
            if (localEncodedStringValue1 != null)
            {
              try
              {
                localPduHeaders.setEncodedStringValue(localEncodedStringValue1, 160);
                k = i;
              }
              catch (RuntimeException paramByteArrayInputStream)
              {
                paramByteArrayInputStream = new StringBuilder();
                paramByteArrayInputStream.append(j);
                paramByteArrayInputStream.append("is not Encoded-String-Value header field!");
                log(paramByteArrayInputStream.toString());
                return null;
              }
              catch (NullPointerException localNullPointerException1)
              {
                for (;;)
                {
                  log("null pointer error!");
                }
              }
              localEncodedStringValue2 = parseEncodedStringValue(paramByteArrayInputStream);
            }
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append(" is not Integer-Value");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 147: 
        case 150: 
        case 154: 
        case 166: 
        case 181: 
        case 182: 
          EncodedStringValue localEncodedStringValue2;
          k = i;
          if (localEncodedStringValue2 != null) {
            try
            {
              localPduHeaders.setEncodedStringValue(localEncodedStringValue2, j);
              k = i;
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Encoded-String-Value header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            catch (NullPointerException localNullPointerException2)
            {
              for (;;)
              {
                log("null pointer error!");
              }
            }
          }
          break;
        case 141: 
          k = parseShortInteger(paramByteArrayInputStream);
          try
          {
            localPduHeaders.setOctet(k, 141);
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Octet header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
          catch (InvalidHeaderValueException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append("Set invalid Octet value: ");
            paramByteArrayInputStream.append(k);
            paramByteArrayInputStream.append(" into the header filed: ");
            paramByteArrayInputStream.append(j);
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 140: 
          k = extractByteValue(paramByteArrayInputStream);
          switch (k)
          {
          }
          try
          {
            localPduHeaders.setOctet(k, j);
            break label957;
            return null;
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Octet header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
          catch (InvalidHeaderValueException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append("Set invalid Octet value: ");
            paramByteArrayInputStream.append(k);
            paramByteArrayInputStream.append(" into the header filed: ");
            paramByteArrayInputStream.append(j);
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 138: 
          paramByteArrayInputStream.mark(1);
          k = extractByteValue(paramByteArrayInputStream);
          if (k >= 128)
          {
            if (128 == k)
            {
              try
              {
                localPduHeaders.setTextString("personal".getBytes(), 138);
              }
              catch (RuntimeException paramByteArrayInputStream)
              {
                break label1168;
              }
              catch (NullPointerException localNullPointerException3)
              {
                break label1200;
              }
            }
            else
            {
              if (129 == k)
              {
                localPduHeaders.setTextString("advertisement".getBytes(), 138);
                break label1206;
              }
              if (130 == k)
              {
                localPduHeaders.setTextString("informational".getBytes(), 138);
                break label1206;
              }
              if (131 != k) {
                break label1206;
              }
              localPduHeaders.setTextString("auto".getBytes(), 138);
              break label1206;
            }
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Text-String header field!");
            log(paramByteArrayInputStream.toString());
            return null;
            log("null pointer error!");
            k = i;
          }
          else
          {
            paramByteArrayInputStream.reset();
            byte[] arrayOfByte = parseWapString(paramByteArrayInputStream, 0);
            if (arrayOfByte != null) {
              try
              {
                localPduHeaders.setTextString(arrayOfByte, 138);
              }
              catch (RuntimeException paramByteArrayInputStream)
              {
                paramByteArrayInputStream = new StringBuilder();
                paramByteArrayInputStream.append(j);
                paramByteArrayInputStream.append("is not Text-String header field!");
                log(paramByteArrayInputStream.toString());
                return null;
              }
              catch (NullPointerException localNullPointerException4)
              {
                for (;;)
                {
                  log("null pointer error!");
                }
              }
            }
            k = i;
          }
          break;
        case 137: 
          parseValueLength(paramByteArrayInputStream);
          Object localObject1;
          if (128 == extractByteValue(paramByteArrayInputStream))
          {
            localObject4 = parseEncodedStringValue(paramByteArrayInputStream);
            localObject1 = localObject4;
            if (localObject4 == null) {
              break label1416;
            }
            localObject1 = ((EncodedStringValue)localObject4).getTextString();
            if (localObject1 != null)
            {
              localObject5 = new String((byte[])localObject1);
              k = ((String)localObject5).indexOf("/");
              localObject1 = localObject5;
              if (k > 0) {
                localObject1 = ((String)localObject5).substring(0, k);
              }
              try
              {
                ((EncodedStringValue)localObject4).setTextString(((String)localObject1).getBytes());
              }
              catch (NullPointerException paramByteArrayInputStream)
              {
                log("null pointer error!");
                return null;
              }
            }
            localObject1 = localObject4;
          }
          try
          {
            localObject1 = new EncodedStringValue("insert-address-token".getBytes());
            try
            {
              localPduHeaders.setEncodedStringValue((EncodedStringValue)localObject1, 137);
              k = i;
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Encoded-String-Value header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            catch (NullPointerException localNullPointerException5)
            {
              for (;;)
              {
                log("null pointer error!");
              }
            }
            parseValueLength(paramByteArrayInputStream);
          }
          catch (NullPointerException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Encoded-String-Value header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 135: 
        case 136: 
        case 157: 
          k = extractByteValue(paramByteArrayInputStream);
          try
          {
            long l1 = parseLongInteger(paramByteArrayInputStream);
            long l2 = l1;
            if (129 == k) {
              l2 = l1 + System.currentTimeMillis() / 1000L;
            }
            try
            {
              localPduHeaders.setLongInteger(l2, j);
              k = i;
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Long-Integer header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            k = extractByteValue(paramByteArrayInputStream);
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Long-Integer header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 134: 
        case 143: 
        case 144: 
        case 145: 
        case 146: 
        case 148: 
        case 149: 
        case 153: 
        case 155: 
        case 156: 
        case 162: 
        case 163: 
        case 165: 
        case 167: 
        case 169: 
        case 171: 
        case 177: 
        case 180: 
        case 186: 
        case 187: 
        case 188: 
        case 191: 
          try
          {
            localPduHeaders.setOctet(k, j);
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Octet header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
          catch (InvalidHeaderValueException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append("Set invalid Octet value: ");
            paramByteArrayInputStream.append(k);
            paramByteArrayInputStream.append(" into the header filed: ");
            paramByteArrayInputStream.append(j);
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 133: 
        case 142: 
        case 159: 
          try
          {
            localPduHeaders.setLongInteger(parseLongInteger(paramByteArrayInputStream), j);
            k = i;
          }
          catch (RuntimeException paramByteArrayInputStream)
          {
            paramByteArrayInputStream = new StringBuilder();
            paramByteArrayInputStream.append(j);
            paramByteArrayInputStream.append("is not Long-Integer header field!");
            log(paramByteArrayInputStream.toString());
            return null;
          }
        case 132: 
          localObject2 = new HashMap();
          localObject4 = parseContentType(paramByteArrayInputStream, (HashMap)localObject2);
          if (localObject4 != null) {
            try
            {
              localPduHeaders.setTextString((byte[])localObject4, 132);
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Text-String header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            catch (NullPointerException localNullPointerException8)
            {
              for (;;)
              {
                log("null pointer error!");
              }
            }
          }
          mStartParam = (byte[])((HashMap)localObject2).get(Integer.valueOf(153));
          mTypeParam = (byte[])((HashMap)localObject2).get(Integer.valueOf(131));
          k = 0;
          break;
        case 131: 
        case 139: 
        case 152: 
        case 158: 
        case 183: 
        case 184: 
        case 185: 
        case 189: 
        case 190: 
          localObject2 = parseWapString(paramByteArrayInputStream, 0);
          k = i;
          if (localObject2 != null) {
            try
            {
              localPduHeaders.setTextString((byte[])localObject2, j);
              k = i;
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Text-String header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            catch (NullPointerException localNullPointerException6)
            {
              for (;;)
              {
                log("null pointer error!");
              }
            }
          }
          break;
        case 129: 
        case 130: 
        case 151: 
          localObject5 = parseEncodedStringValue(paramByteArrayInputStream);
          k = i;
          if (localObject5 != null)
          {
            Object localObject3 = ((EncodedStringValue)localObject5).getTextString();
            if (localObject3 != null)
            {
              String str = new String((byte[])localObject3);
              k = str.indexOf("/");
              localObject3 = str;
              if (k > 0) {
                localObject3 = str.substring(0, k);
              }
              try
              {
                ((EncodedStringValue)localObject5).setTextString(((String)localObject3).getBytes());
              }
              catch (NullPointerException paramByteArrayInputStream)
              {
                log("null pointer error!");
                return null;
              }
            }
            try
            {
              localPduHeaders.appendEncodedStringValue((EncodedStringValue)localObject5, j);
            }
            catch (RuntimeException paramByteArrayInputStream)
            {
              paramByteArrayInputStream = new StringBuilder();
              paramByteArrayInputStream.append(j);
              paramByteArrayInputStream.append("is not Encoded-String-Value header field!");
              log(paramByteArrayInputStream.toString());
              return null;
            }
            catch (NullPointerException localNullPointerException7)
            {
              for (;;)
              {
                log("null pointer error!");
              }
            }
            k = i;
          }
          break;
        }
        i = k;
      }
    }
    return localPduHeaders;
  }
  
  protected boolean parsePartHeaders(ByteArrayInputStream paramByteArrayInputStream, PduPart paramPduPart, int paramInt)
  {
    int i = paramByteArrayInputStream.available();
    int j = paramInt;
    label424:
    while (j > 0)
    {
      int k = paramByteArrayInputStream.read();
      j--;
      byte[] arrayOfByte1;
      if (k > 127)
      {
        if (k != 142)
        {
          if (k != 174) {
            if (k != 192)
            {
              if (k != 197)
              {
                if (-1 == skipWapValue(paramByteArrayInputStream, j))
                {
                  Log.e("PduParser", "Corrupt Part headers");
                  return false;
                }
                j = 0;
                break label424;
              }
            }
            else
            {
              arrayOfByte1 = parseWapString(paramByteArrayInputStream, 1);
              if (arrayOfByte1 != null) {
                paramPduPart.setContentId(arrayOfByte1);
              }
              j = paramInt - (i - paramByteArrayInputStream.available());
              break label424;
            }
          }
          if (mParseContentDisposition)
          {
            j = parseValueLength(paramByteArrayInputStream);
            paramByteArrayInputStream.mark(1);
            k = paramByteArrayInputStream.available();
            int m = paramByteArrayInputStream.read();
            if (m == 128)
            {
              paramPduPart.setContentDisposition(PduPart.DISPOSITION_FROM_DATA);
            }
            else if (m == 129)
            {
              paramPduPart.setContentDisposition(PduPart.DISPOSITION_ATTACHMENT);
            }
            else if (m == 130)
            {
              paramPduPart.setContentDisposition(PduPart.DISPOSITION_INLINE);
            }
            else
            {
              paramByteArrayInputStream.reset();
              paramPduPart.setContentDisposition(parseWapString(paramByteArrayInputStream, 0));
            }
            if (k - paramByteArrayInputStream.available() < j)
            {
              if (paramByteArrayInputStream.read() == 152) {
                paramPduPart.setFilename(parseWapString(paramByteArrayInputStream, 0));
              }
              m = paramByteArrayInputStream.available();
              if (k - m < j)
              {
                j -= k - m;
                paramByteArrayInputStream.read(new byte[j], 0, j);
              }
            }
            j = paramInt - (i - paramByteArrayInputStream.available());
          }
        }
        else
        {
          arrayOfByte1 = parseWapString(paramByteArrayInputStream, 0);
          if (arrayOfByte1 != null) {
            paramPduPart.setContentLocation(arrayOfByte1);
          }
          j = paramInt - (i - paramByteArrayInputStream.available());
        }
      }
      else if ((k >= 32) && (k <= 127))
      {
        arrayOfByte1 = parseWapString(paramByteArrayInputStream, 0);
        byte[] arrayOfByte2 = parseWapString(paramByteArrayInputStream, 0);
        if (true == "Content-Transfer-Encoding".equalsIgnoreCase(new String(arrayOfByte1))) {
          paramPduPart.setContentTransferEncoding(arrayOfByte2);
        }
        j = paramInt - (i - paramByteArrayInputStream.available());
      }
      else
      {
        if (-1 == skipWapValue(paramByteArrayInputStream, j))
        {
          Log.e("PduParser", "Corrupt Part headers");
          return false;
        }
        j = 0;
      }
    }
    if (j != 0)
    {
      Log.e("PduParser", "Corrupt Part headers");
      return false;
    }
    return true;
  }
  
  protected PduBody parseParts(ByteArrayInputStream paramByteArrayInputStream)
  {
    Object localObject1 = null;
    if (paramByteArrayInputStream == null) {
      return null;
    }
    int i = parseUnsignedInt(paramByteArrayInputStream);
    PduBody localPduBody = new PduBody();
    int j = 0;
    int k = 0;
    for (;;)
    {
      Object localObject2 = this;
      if (k >= i) {
        break;
      }
      int m = parseUnsignedInt(paramByteArrayInputStream);
      int n = parseUnsignedInt(paramByteArrayInputStream);
      PduPart localPduPart = new PduPart();
      int i1 = paramByteArrayInputStream.available();
      if (i1 <= 0) {
        return localObject1;
      }
      Object localObject3 = new HashMap();
      byte[] arrayOfByte = parseContentType(paramByteArrayInputStream, (HashMap)localObject3);
      if (arrayOfByte != null) {
        localPduPart.setContentType(arrayOfByte);
      } else {
        localPduPart.setContentType(PduContentTypes.contentTypes[j].getBytes());
      }
      arrayOfByte = (byte[])((HashMap)localObject3).get(Integer.valueOf(151));
      if (arrayOfByte != null) {
        localPduPart.setName(arrayOfByte);
      }
      localObject3 = (Integer)((HashMap)localObject3).get(Integer.valueOf(129));
      if (localObject3 != null) {
        localPduPart.setCharset(((Integer)localObject3).intValue());
      }
      j = m - (i1 - paramByteArrayInputStream.available());
      if (j > 0)
      {
        if (!((PduParser)localObject2).parsePartHeaders(paramByteArrayInputStream, localPduPart, j)) {
          return localObject1;
        }
      }
      else if (j < 0) {
        return localObject1;
      }
      if ((localPduPart.getContentLocation() == null) && (localPduPart.getName() == null) && (localPduPart.getFilename() == null) && (localPduPart.getContentId() == null)) {
        localPduPart.setContentLocation(Long.toOctalString(System.currentTimeMillis()).getBytes());
      }
      if (n > 0)
      {
        localObject3 = new byte[n];
        localObject1 = new String(localPduPart.getContentType());
        paramByteArrayInputStream.read((byte[])localObject3, 0, n);
        if (((String)localObject1).equalsIgnoreCase("application/vnd.wap.multipart.alternative"))
        {
          localObject1 = ((PduParser)localObject2).parseParts(new ByteArrayInputStream((byte[])localObject3)).getPart(0);
        }
        else
        {
          localObject1 = localPduPart.getContentTransferEncoding();
          if (localObject1 != null)
          {
            localObject2 = new String((byte[])localObject1);
            if (((String)localObject2).equalsIgnoreCase("base64"))
            {
              localObject1 = Base64.decodeBase64((byte[])localObject3);
            }
            else
            {
              localObject1 = localObject3;
              if (((String)localObject2).equalsIgnoreCase("quoted-printable")) {
                localObject1 = QuotedPrintable.decodeQuotedPrintable((byte[])localObject3);
              }
            }
          }
          else
          {
            localObject1 = localObject3;
          }
          if (localObject1 == null)
          {
            log("Decode part data error!");
            return null;
          }
          localPduPart.setData((byte[])localObject1);
          localObject1 = localPduPart;
        }
      }
      else
      {
        localObject1 = localPduPart;
      }
      if (checkPartPosition((PduPart)localObject1) == 0) {
        localPduBody.addPart(0, (PduPart)localObject1);
      } else {
        localPduBody.addPart((PduPart)localObject1);
      }
      k++;
      j = 0;
      localObject1 = null;
    }
    return localPduBody;
  }
}
