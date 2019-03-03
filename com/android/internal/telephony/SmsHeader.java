package com.android.internal.telephony;

import com.android.internal.util.HexDump;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SmsHeader
{
  public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_16_BIT = 5;
  public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_8_BIT = 4;
  public static final int ELT_ID_CHARACTER_SIZE_WVG_OBJECT = 25;
  public static final int ELT_ID_COMPRESSION_CONTROL = 22;
  public static final int ELT_ID_CONCATENATED_16_BIT_REFERENCE = 8;
  public static final int ELT_ID_CONCATENATED_8_BIT_REFERENCE = 0;
  public static final int ELT_ID_ENHANCED_VOICE_MAIL_INFORMATION = 35;
  public static final int ELT_ID_EXTENDED_OBJECT = 20;
  public static final int ELT_ID_EXTENDED_OBJECT_DATA_REQUEST_CMD = 26;
  public static final int ELT_ID_HYPERLINK_FORMAT_ELEMENT = 33;
  public static final int ELT_ID_LARGE_ANIMATION = 14;
  public static final int ELT_ID_LARGE_PICTURE = 16;
  public static final int ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT = 37;
  public static final int ELT_ID_NATIONAL_LANGUAGE_SINGLE_SHIFT = 36;
  public static final int ELT_ID_OBJECT_DISTR_INDICATOR = 23;
  public static final int ELT_ID_PREDEFINED_ANIMATION = 13;
  public static final int ELT_ID_PREDEFINED_SOUND = 11;
  public static final int ELT_ID_REPLY_ADDRESS_ELEMENT = 34;
  public static final int ELT_ID_REUSED_EXTENDED_OBJECT = 21;
  public static final int ELT_ID_RFC_822_EMAIL_HEADER = 32;
  public static final int ELT_ID_SMALL_ANIMATION = 15;
  public static final int ELT_ID_SMALL_PICTURE = 17;
  public static final int ELT_ID_SMSC_CONTROL_PARAMS = 6;
  public static final int ELT_ID_SPECIAL_SMS_MESSAGE_INDICATION = 1;
  public static final int ELT_ID_STANDARD_WVG_OBJECT = 24;
  public static final int ELT_ID_TEXT_FORMATTING = 10;
  public static final int ELT_ID_UDH_SOURCE_INDICATION = 7;
  public static final int ELT_ID_USER_DEFINED_SOUND = 12;
  public static final int ELT_ID_USER_PROMPT_INDICATOR = 19;
  public static final int ELT_ID_VARIABLE_PICTURE = 18;
  public static final int ELT_ID_WIRELESS_CTRL_MSG_PROTOCOL = 9;
  public static final int PORT_WAP_PUSH = 2948;
  public static final int PORT_WAP_WSP = 9200;
  public ConcatRef concatRef;
  public int languageShiftTable;
  public int languageTable;
  public ArrayList<MiscElt> miscEltList = new ArrayList();
  public PortAddrs portAddrs;
  public ArrayList<SpecialSmsMsg> specialSmsMsgList = new ArrayList();
  
  public SmsHeader() {}
  
  public static SmsHeader fromByteArray(byte[] paramArrayOfByte)
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    paramArrayOfByte = new SmsHeader();
    while (localByteArrayInputStream.available() > 0)
    {
      int i = localByteArrayInputStream.read();
      int j = localByteArrayInputStream.read();
      Object localObject;
      switch (i)
      {
      default: 
        localObject = new MiscElt();
        id = i;
        data = new byte[j];
        localByteArrayInputStream.read(data, 0, j);
        miscEltList.add(localObject);
        break;
      case 37: 
        languageTable = localByteArrayInputStream.read();
        break;
      case 36: 
        languageShiftTable = localByteArrayInputStream.read();
        break;
      case 8: 
        localObject = new ConcatRef();
        refNumber = (localByteArrayInputStream.read() << 8 | localByteArrayInputStream.read());
        msgCount = localByteArrayInputStream.read();
        seqNumber = localByteArrayInputStream.read();
        isEightBits = false;
        if ((msgCount != 0) && (seqNumber != 0) && (seqNumber <= msgCount)) {
          concatRef = ((ConcatRef)localObject);
        }
        break;
      case 5: 
        localObject = new PortAddrs();
        destPort = (localByteArrayInputStream.read() << 8 | localByteArrayInputStream.read());
        origPort = (localByteArrayInputStream.read() << 8 | localByteArrayInputStream.read());
        areEightBits = false;
        portAddrs = ((PortAddrs)localObject);
        break;
      case 4: 
        localObject = new PortAddrs();
        destPort = localByteArrayInputStream.read();
        origPort = localByteArrayInputStream.read();
        areEightBits = true;
        portAddrs = ((PortAddrs)localObject);
        break;
      case 1: 
        localObject = new SpecialSmsMsg();
        msgIndType = localByteArrayInputStream.read();
        msgCount = localByteArrayInputStream.read();
        specialSmsMsgList.add(localObject);
        break;
      case 0: 
        localObject = new ConcatRef();
        refNumber = localByteArrayInputStream.read();
        msgCount = localByteArrayInputStream.read();
        seqNumber = localByteArrayInputStream.read();
        isEightBits = true;
        if ((msgCount != 0) && (seqNumber != 0) && (seqNumber <= msgCount)) {
          concatRef = ((ConcatRef)localObject);
        }
        break;
      }
    }
    return paramArrayOfByte;
  }
  
  public static byte[] toByteArray(SmsHeader paramSmsHeader)
  {
    if ((portAddrs == null) && (concatRef == null) && (specialSmsMsgList.isEmpty()) && (miscEltList.isEmpty()) && (languageShiftTable == 0) && (languageTable == 0)) {
      return null;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(140);
    Object localObject = concatRef;
    if (localObject != null)
    {
      if (isEightBits)
      {
        localByteArrayOutputStream.write(0);
        localByteArrayOutputStream.write(3);
        localByteArrayOutputStream.write(refNumber);
      }
      else
      {
        localByteArrayOutputStream.write(8);
        localByteArrayOutputStream.write(4);
        localByteArrayOutputStream.write(refNumber >>> 8);
        localByteArrayOutputStream.write(refNumber & 0xFF);
      }
      localByteArrayOutputStream.write(msgCount);
      localByteArrayOutputStream.write(seqNumber);
    }
    localObject = portAddrs;
    if (localObject != null) {
      if (areEightBits)
      {
        localByteArrayOutputStream.write(4);
        localByteArrayOutputStream.write(2);
        localByteArrayOutputStream.write(destPort);
        localByteArrayOutputStream.write(origPort);
      }
      else
      {
        localByteArrayOutputStream.write(5);
        localByteArrayOutputStream.write(4);
        localByteArrayOutputStream.write(destPort >>> 8);
        localByteArrayOutputStream.write(destPort & 0xFF);
        localByteArrayOutputStream.write(origPort >>> 8);
        localByteArrayOutputStream.write(origPort & 0xFF);
      }
    }
    if (languageShiftTable != 0)
    {
      localByteArrayOutputStream.write(36);
      localByteArrayOutputStream.write(1);
      localByteArrayOutputStream.write(languageShiftTable);
    }
    if (languageTable != 0)
    {
      localByteArrayOutputStream.write(37);
      localByteArrayOutputStream.write(1);
      localByteArrayOutputStream.write(languageTable);
    }
    localObject = specialSmsMsgList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      SpecialSmsMsg localSpecialSmsMsg = (SpecialSmsMsg)((Iterator)localObject).next();
      localByteArrayOutputStream.write(1);
      localByteArrayOutputStream.write(2);
      localByteArrayOutputStream.write(msgIndType & 0xFF);
      localByteArrayOutputStream.write(msgCount & 0xFF);
    }
    paramSmsHeader = miscEltList.iterator();
    while (paramSmsHeader.hasNext())
    {
      localObject = (MiscElt)paramSmsHeader.next();
      localByteArrayOutputStream.write(id);
      localByteArrayOutputStream.write(data.length);
      localByteArrayOutputStream.write(data, 0, data.length);
    }
    return localByteArrayOutputStream.toByteArray();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("UserDataHeader ");
    localStringBuilder1.append("{ ConcatRef ");
    if (concatRef == null)
    {
      localStringBuilder1.append("unset");
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("{ refNumber=");
      ((StringBuilder)localObject1).append(concatRef.refNumber);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", msgCount=");
      ((StringBuilder)localObject1).append(concatRef.msgCount);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", seqNumber=");
      ((StringBuilder)localObject1).append(concatRef.seqNumber);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", isEightBits=");
      ((StringBuilder)localObject1).append(concatRef.isEightBits);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localStringBuilder1.append(" }");
    }
    localStringBuilder1.append(", PortAddrs ");
    if (portAddrs == null)
    {
      localStringBuilder1.append("unset");
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("{ destPort=");
      ((StringBuilder)localObject1).append(portAddrs.destPort);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", origPort=");
      ((StringBuilder)localObject1).append(portAddrs.origPort);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", areEightBits=");
      ((StringBuilder)localObject1).append(portAddrs.areEightBits);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
      localStringBuilder1.append(" }");
    }
    if (languageShiftTable != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", languageShiftTable=");
      ((StringBuilder)localObject1).append(languageShiftTable);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
    }
    if (languageTable != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(", languageTable=");
      ((StringBuilder)localObject1).append(languageTable);
      localStringBuilder1.append(((StringBuilder)localObject1).toString());
    }
    Object localObject1 = specialSmsMsgList.iterator();
    StringBuilder localStringBuilder2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (SpecialSmsMsg)((Iterator)localObject1).next();
      localStringBuilder1.append(", SpecialSmsMsg ");
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("{ msgIndType=");
      localStringBuilder2.append(msgIndType);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", msgCount=");
      localStringBuilder2.append(msgCount);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append(" }");
    }
    Object localObject2 = miscEltList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (MiscElt)((Iterator)localObject2).next();
      localStringBuilder1.append(", MiscElt ");
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("{ id=");
      localStringBuilder2.append(id);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", length=");
      localStringBuilder2.append(data.length);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", data=");
      localStringBuilder2.append(HexDump.toHexString(data));
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder1.append(" }");
    }
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
  
  public static class ConcatRef
  {
    public boolean isEightBits;
    public int msgCount;
    public int refNumber;
    public int seqNumber;
    
    public ConcatRef() {}
  }
  
  public static class MiscElt
  {
    public byte[] data;
    public int id;
    
    public MiscElt() {}
  }
  
  public static class PortAddrs
  {
    public boolean areEightBits;
    public int destPort;
    public int origPort;
    
    public PortAddrs() {}
  }
  
  public static class SpecialSmsMsg
  {
    public int msgCount;
    public int msgIndType;
    
    public SpecialSmsMsg() {}
  }
}
