package com.android.internal.telephony.cat;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

abstract class ValueParser
{
  ValueParser() {}
  
  static String retrieveAlphaId(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    byte[] arrayOfByte = null;
    if (paramComprehensionTlv != null)
    {
      arrayOfByte = paramComprehensionTlv.getRawValue();
      int i = paramComprehensionTlv.getValueIndex();
      int j = paramComprehensionTlv.getLength();
      if (j != 0) {
        try
        {
          paramComprehensionTlv = IccUtils.adnStringFieldToString(arrayOfByte, i, j);
          return paramComprehensionTlv;
        }
        catch (IndexOutOfBoundsException paramComprehensionTlv)
        {
          throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
      }
      paramComprehensionTlv = new StringBuilder();
      paramComprehensionTlv.append("Alpha Id length=");
      paramComprehensionTlv.append(j);
      CatLog.d("ValueParser", paramComprehensionTlv.toString());
      return null;
    }
    paramComprehensionTlv = Resources.getSystem();
    boolean bool;
    try
    {
      bool = paramComprehensionTlv.getBoolean(17957038);
    }
    catch (Resources.NotFoundException paramComprehensionTlv)
    {
      bool = false;
    }
    if (bool) {
      paramComprehensionTlv = arrayOfByte;
    } else {
      paramComprehensionTlv = "Default Message";
    }
    return paramComprehensionTlv;
  }
  
  static CommandDetails retrieveCommandDetails(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    CommandDetails localCommandDetails = new CommandDetails();
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    try
    {
      compRequired = paramComprehensionTlv.isComprehensionRequired();
      commandNumber = (arrayOfByte[i] & 0xFF);
      typeOfCommand = (arrayOfByte[(i + 1)] & 0xFF);
      commandQualifier = (arrayOfByte[(i + 2)] & 0xFF);
      return localCommandDetails;
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
  }
  
  static DeviceIdentities retrieveDeviceIdentities(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    DeviceIdentities localDeviceIdentities = new DeviceIdentities();
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    try
    {
      sourceId = (arrayOfByte[i] & 0xFF);
      destinationId = (arrayOfByte[(i + 1)] & 0xFF);
      return localDeviceIdentities;
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
    }
  }
  
  static Duration retrieveDuration(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    Object localObject = Duration.TimeUnit.SECOND;
    localObject = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    try
    {
      paramComprehensionTlv = Duration.TimeUnit.values()[(localObject[i] & 0xFF)];
      i = localObject[(i + 1)];
      return new Duration(i & 0xFF, paramComprehensionTlv);
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
  }
  
  static IconId retrieveIconId(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    IconId localIconId = new IconId();
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    boolean bool;
    if ((arrayOfByte[i] & 0xFF) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    try
    {
      selfExplanatory = bool;
      recordNumber = (arrayOfByte[(i + 1)] & 0xFF);
      return localIconId;
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
  }
  
  static Item retrieveItem(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    Object localObject = null;
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    int j = paramComprehensionTlv.getLength();
    paramComprehensionTlv = localObject;
    if (j != 0) {
      try
      {
        paramComprehensionTlv = new Item(arrayOfByte[i] & 0xFF, IccUtils.adnStringFieldToString(arrayOfByte, i + 1, j - 1));
      }
      catch (IndexOutOfBoundsException paramComprehensionTlv)
      {
        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
      }
    }
    return paramComprehensionTlv;
  }
  
  static int retrieveItemId(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    i = arrayOfByte[i];
    return i & 0xFF;
  }
  
  static ItemsIconId retrieveItemsIconId(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    CatLog.d("ValueParser", "retrieveItemsIconId:");
    ItemsIconId localItemsIconId = new ItemsIconId();
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    int j = paramComprehensionTlv.getLength();
    boolean bool = true;
    int k = j - 1;
    recordNumbers = new int[k];
    j = i + 1;
    int m = arrayOfByte[i];
    i = 0;
    if ((m & 0xFF) != 0) {
      bool = false;
    }
    try
    {
      selfExplanatory = bool;
      for (;;)
      {
        m = i;
        if (m >= k) {
          break;
        }
        paramComprehensionTlv = recordNumbers;
        i = m + 1;
        paramComprehensionTlv[m] = arrayOfByte[j];
        j++;
      }
      return localItemsIconId;
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
  }
  
  static List<TextAttribute> retrieveTextAttribute(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    ArrayList localArrayList = new ArrayList();
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    int j = paramComprehensionTlv.getLength();
    if (j != 0)
    {
      int k = j / 4;
      j = 0;
      while (j < k)
      {
        int m = arrayOfByte[i];
        int n = arrayOfByte[(i + 1)];
        int i1 = arrayOfByte[(i + 2)] & 0xFF;
        int i2 = arrayOfByte[(i + 3)];
        try
        {
          TextAlignment localTextAlignment = TextAlignment.fromInt(i1 & 0x3);
          Object localObject = FontSize.fromInt(i1 >> 2 & 0x3);
          paramComprehensionTlv = (ComprehensionTlv)localObject;
          if (localObject == null) {
            paramComprehensionTlv = FontSize.NORMAL;
          }
          boolean bool1 = true;
          boolean bool2;
          if ((i1 & 0x10) != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          boolean bool3;
          if ((i1 & 0x20) != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          boolean bool4;
          if ((i1 & 0x40) != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if ((i1 & 0x80) == 0) {
            bool1 = false;
          }
          localObject = TextColor.fromInt(i2 & 0xFF);
          TextAttribute localTextAttribute = new com/android/internal/telephony/cat/TextAttribute;
          localTextAttribute.<init>(m & 0xFF, n & 0xFF, localTextAlignment, paramComprehensionTlv, bool2, bool3, bool4, bool1, (TextColor)localObject);
          localArrayList.add(localTextAttribute);
          j++;
          i += 4;
        }
        catch (IndexOutOfBoundsException paramComprehensionTlv)
        {
          throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
      }
      return localArrayList;
    }
    return null;
  }
  
  static String retrieveTextString(ComprehensionTlv paramComprehensionTlv)
    throws ResultException
  {
    byte[] arrayOfByte = paramComprehensionTlv.getRawValue();
    int i = paramComprehensionTlv.getValueIndex();
    int j = paramComprehensionTlv.getLength();
    if (j == 0) {
      return null;
    }
    int k = j - 1;
    j = (byte)(arrayOfByte[i] & 0xC);
    if (j == 0) {}
    try
    {
      paramComprehensionTlv = GsmAlphabet.gsm7BitPackedToString(arrayOfByte, i + 1, k * 8 / 7);
      break label97;
      if (j == 4)
      {
        paramComprehensionTlv = GsmAlphabet.gsm8BitUnpackedToString(arrayOfByte, i + 1, k);
      }
      else
      {
        if (j != 8) {
          break label99;
        }
        paramComprehensionTlv = new String(arrayOfByte, i + 1, k, "UTF-16");
      }
      label97:
      return paramComprehensionTlv;
      label99:
      paramComprehensionTlv = new com/android/internal/telephony/cat/ResultException;
      paramComprehensionTlv.<init>(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
      throw paramComprehensionTlv;
    }
    catch (UnsupportedEncodingException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
    catch (IndexOutOfBoundsException paramComprehensionTlv)
    {
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
    }
  }
}
