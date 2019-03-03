package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AnswerToReset
{
  private static final int B2_MASK = 2;
  private static final int B7_MASK = 64;
  public static final byte DIRECT_CONVENTION = 59;
  public static final byte EUICC_SUPPORTED = -126;
  private static final int EXTENDED_APDU_INDEX = 2;
  public static final int INTERFACE_BYTES_MASK = 240;
  public static final byte INVERSE_CONVENTION = 63;
  private static final String TAG = "AnswerToReset";
  private static final int TAG_CARD_CAPABILITIES = 7;
  public static final int TA_MASK = 16;
  public static final int TB_MASK = 32;
  public static final int TC_MASK = 64;
  public static final int TD_MASK = 128;
  public static final int T_MASK = 15;
  public static final int T_VALUE_FOR_GLOBAL_INTERFACE = 15;
  private static final boolean VDBG = false;
  private Byte mCheckByte;
  private byte mFormatByte;
  private HistoricalBytes mHistoricalBytes;
  private ArrayList<InterfaceByte> mInterfaceBytes = new ArrayList();
  private boolean mIsDirectConvention;
  private boolean mIsEuiccSupported;
  private boolean mOnlyTEqualsZero = true;
  
  private AnswerToReset() {}
  
  private static String byteToStringHex(Byte paramByte)
  {
    if (paramByte == null) {
      paramByte = null;
    } else {
      paramByte = IccUtils.byteToHex(paramByte.byteValue());
    }
    return paramByte;
  }
  
  private void checkIsEuiccSupported()
  {
    for (int i = 0; i < mInterfaceBytes.size() - 1; i++) {
      if ((((InterfaceByte)mInterfaceBytes.get(i)).getTD() != null) && ((((InterfaceByte)mInterfaceBytes.get(i)).getTD().byteValue() & 0xF) == 15) && (((InterfaceByte)mInterfaceBytes.get(i + 1)).getTB() != null) && (((InterfaceByte)mInterfaceBytes.get(i + 1)).getTB().byteValue() == -126))
      {
        mIsEuiccSupported = true;
        return;
      }
    }
  }
  
  private static void log(String paramString)
  {
    Rlog.d("AnswerToReset", paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e("AnswerToReset", paramString);
  }
  
  public static AnswerToReset parseAtr(String paramString)
  {
    AnswerToReset localAnswerToReset = new AnswerToReset();
    if (localAnswerToReset.parseAtrString(paramString)) {
      return localAnswerToReset;
    }
    return null;
  }
  
  private boolean parseAtrString(String paramString)
  {
    if (paramString == null)
    {
      loge("The input ATR string can not be null");
      return false;
    }
    if (paramString.length() % 2 != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("The length of input ATR string ");
      ((StringBuilder)localObject).append(paramString.length());
      ((StringBuilder)localObject).append(" is not even.");
      loge(((StringBuilder)localObject).toString());
      return false;
    }
    if (paramString.length() < 4)
    {
      loge("Valid ATR string must at least contains TS and T0.");
      return false;
    }
    Object localObject = IccUtils.hexStringToBytes(paramString);
    if (localObject == null) {
      return false;
    }
    int i = parseConventionByte((byte[])localObject, 0);
    if (i == -1) {
      return false;
    }
    i = parseFormatByte((byte[])localObject, i);
    if (i == -1) {
      return false;
    }
    i = parseInterfaceBytes((byte[])localObject, i);
    if (i == -1) {
      return false;
    }
    i = parseHistoricalBytes((byte[])localObject, i);
    if (i == -1) {
      return false;
    }
    i = parseCheckBytes((byte[])localObject, i);
    if (i == -1) {
      return false;
    }
    if (i != localObject.length)
    {
      loge("Unexpected bytes after the check byte.");
      return false;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Successfully parsed the ATR string ");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" into ");
    ((StringBuilder)localObject).append(toString());
    log(((StringBuilder)localObject).toString());
    checkIsEuiccSupported();
    return true;
  }
  
  private int parseCheckBytes(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt < paramArrayOfByte.length)
    {
      mCheckByte = Byte.valueOf(paramArrayOfByte[paramInt]);
      paramInt++;
    }
    else
    {
      if (!mOnlyTEqualsZero)
      {
        loge("Check byte must be present because T equals to values other than 0.");
        return -1;
      }
      log("Check byte can be absent because T=0.");
    }
    return paramInt;
  }
  
  private int parseConventionByte(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt >= paramArrayOfByte.length)
    {
      loge("Failed to read the convention byte.");
      return -1;
    }
    byte b = paramArrayOfByte[paramInt];
    if (b == 59)
    {
      mIsDirectConvention = true;
    }
    else
    {
      if (b != 63) {
        break label46;
      }
      mIsDirectConvention = false;
    }
    return paramInt + 1;
    label46:
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Unrecognized convention byte ");
    paramArrayOfByte.append(IccUtils.byteToHex(b));
    loge(paramArrayOfByte.toString());
    return -1;
  }
  
  private int parseFormatByte(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt >= paramArrayOfByte.length)
    {
      loge("Failed to read the format byte.");
      return -1;
    }
    mFormatByte = ((byte)paramArrayOfByte[paramInt]);
    return paramInt + 1;
  }
  
  private int parseHistoricalBytes(byte[] paramArrayOfByte, int paramInt)
  {
    int i = mFormatByte & 0xF;
    if (i + paramInt > paramArrayOfByte.length)
    {
      loge("Failed to read the historical bytes.");
      return -1;
    }
    if (i > 0) {
      mHistoricalBytes = HistoricalBytes.parseHistoricalBytes(paramArrayOfByte, paramInt, i);
    }
    return paramInt + i;
  }
  
  private int parseInterfaceBytes(byte[] paramArrayOfByte, int paramInt)
  {
    int i = mFormatByte;
    for (;;)
    {
      Object localObject;
      if ((i & 0xF0) != 0)
      {
        localObject = new InterfaceByte(null);
        int j = paramInt;
        if ((i & 0x10) != 0)
        {
          if (paramInt >= paramArrayOfByte.length)
          {
            loge("Failed to read the byte for TA.");
            return -1;
          }
          ((InterfaceByte)localObject).setTA(Byte.valueOf(paramArrayOfByte[paramInt]));
          j = paramInt + 1;
        }
        paramInt = j;
        if ((i & 0x20) != 0)
        {
          if (j >= paramArrayOfByte.length)
          {
            loge("Failed to read the byte for TB.");
            return -1;
          }
          ((InterfaceByte)localObject).setTB(Byte.valueOf(paramArrayOfByte[j]));
          paramInt = j + 1;
        }
        j = paramInt;
        if ((i & 0x40) != 0)
        {
          if (paramInt >= paramArrayOfByte.length)
          {
            loge("Failed to read the byte for TC.");
            return -1;
          }
          ((InterfaceByte)localObject).setTC(Byte.valueOf(paramArrayOfByte[paramInt]));
          j = paramInt + 1;
        }
        paramInt = j;
        if ((i & 0x80) != 0)
        {
          if (j >= paramArrayOfByte.length)
          {
            loge("Failed to read the byte for TD.");
            return -1;
          }
          ((InterfaceByte)localObject).setTD(Byte.valueOf(paramArrayOfByte[j]));
          paramInt = j + 1;
        }
        mInterfaceBytes.add(localObject);
        localObject = ((InterfaceByte)localObject).getTD();
        if (localObject != null) {}
      }
      else
      {
        return paramInt;
      }
      i = ((Byte)localObject).byteValue();
      if ((i & 0xF) != 0) {
        mOnlyTEqualsZero = false;
      }
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("AnswerToReset:");
    paramPrintWriter.println(toString());
    paramPrintWriter.flush();
  }
  
  public Byte getCheckByte()
  {
    return mCheckByte;
  }
  
  public byte getConventionByte()
  {
    byte b1;
    byte b2;
    if (mIsDirectConvention)
    {
      b1 = 59;
      b2 = b1;
    }
    else
    {
      b1 = 63;
      b2 = b1;
    }
    return b2;
  }
  
  public byte getFormatByte()
  {
    return mFormatByte;
  }
  
  public HistoricalBytes getHistoricalBytes()
  {
    return mHistoricalBytes;
  }
  
  public List<InterfaceByte> getInterfaceBytes()
  {
    return mInterfaceBytes;
  }
  
  public boolean isEuiccSupported()
  {
    return mIsEuiccSupported;
  }
  
  public boolean isExtendedApduSupported()
  {
    Object localObject = mHistoricalBytes;
    boolean bool1 = false;
    boolean bool2 = false;
    if (localObject == null) {
      return false;
    }
    localObject = mHistoricalBytes.getValue(7);
    if ((localObject != null) && (localObject.length >= 3))
    {
      if (mIsDirectConvention)
      {
        if ((localObject[2] & 0x40) > 0) {
          bool2 = true;
        }
        return bool2;
      }
      bool2 = bool1;
      if ((localObject[2] & 0x2) > 0) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("AnswerToReset:{");
    localStringBuffer.append("mConventionByte=");
    localStringBuffer.append(IccUtils.byteToHex(getConventionByte()));
    localStringBuffer.append(",");
    localStringBuffer.append("mFormatByte=");
    localStringBuffer.append(byteToStringHex(Byte.valueOf(mFormatByte)));
    localStringBuffer.append(",");
    localStringBuffer.append("mInterfaceBytes={");
    Object localObject = mInterfaceBytes.iterator();
    while (((Iterator)localObject).hasNext()) {
      localStringBuffer.append(((InterfaceByte)((Iterator)localObject).next()).toString());
    }
    localStringBuffer.append("},");
    localStringBuffer.append("mHistoricalBytes={");
    if (mHistoricalBytes != null)
    {
      localObject = mHistoricalBytes.getRawData();
      int i = localObject.length;
      for (int j = 0; j < i; j++)
      {
        localStringBuffer.append(IccUtils.byteToHex(localObject[j]));
        localStringBuffer.append(",");
      }
    }
    localStringBuffer.append("},");
    localStringBuffer.append("mCheckByte=");
    localStringBuffer.append(byteToStringHex(mCheckByte));
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public static class HistoricalBytes
  {
    private static final int LENGTH_MASK = 15;
    private static final int TAG_MASK = 240;
    private final byte mCategory;
    private final ArrayMap<Integer, byte[]> mNodes;
    private final byte[] mRawData;
    
    private HistoricalBytes(byte[] paramArrayOfByte, ArrayMap<Integer, byte[]> paramArrayMap, byte paramByte)
    {
      mRawData = paramArrayOfByte;
      mNodes = paramArrayMap;
      mCategory = ((byte)paramByte);
    }
    
    private static HistoricalBytes parseHistoricalBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if ((paramInt2 > 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        ArrayMap localArrayMap = new ArrayMap();
        for (int i = paramInt1 + 1; (i < paramInt1 + paramInt2) && (i > 0); i = parseLtvNode(i, localArrayMap, paramArrayOfByte, paramInt1 + paramInt2 - 1)) {}
        if (i < 0) {
          return null;
        }
        byte[] arrayOfByte = new byte[paramInt2];
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
        return new HistoricalBytes(arrayOfByte, localArrayMap, arrayOfByte[0]);
      }
      return null;
    }
    
    private static int parseLtvNode(int paramInt1, ArrayMap<Integer, byte[]> paramArrayMap, byte[] paramArrayOfByte, int paramInt2)
    {
      if (paramInt1 > paramInt2) {
        return -1;
      }
      int i = paramArrayOfByte[paramInt1];
      int j = paramInt1 + 1;
      paramInt1 = paramArrayOfByte[paramInt1] & 0xF;
      if ((j + paramInt1 <= paramInt2 + 1) && (paramInt1 != 0))
      {
        byte[] arrayOfByte = new byte[paramInt1];
        System.arraycopy(paramArrayOfByte, j, arrayOfByte, 0, paramInt1);
        paramArrayMap.put(Integer.valueOf((i & 0xF0) >> 4), arrayOfByte);
        return j + paramInt1;
      }
      return -1;
    }
    
    public byte getCategory()
    {
      return mCategory;
    }
    
    public byte[] getRawData()
    {
      return mRawData;
    }
    
    public byte[] getValue(int paramInt)
    {
      return (byte[])mNodes.get(Integer.valueOf(paramInt));
    }
  }
  
  public static class InterfaceByte
  {
    private Byte mTA;
    private Byte mTB;
    private Byte mTC;
    private Byte mTD;
    
    private InterfaceByte()
    {
      mTA = null;
      mTB = null;
      mTC = null;
      mTD = null;
    }
    
    @VisibleForTesting
    public InterfaceByte(Byte paramByte1, Byte paramByte2, Byte paramByte3, Byte paramByte4)
    {
      mTA = paramByte1;
      mTB = paramByte2;
      mTC = paramByte3;
      mTD = paramByte4;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (InterfaceByte)paramObject;
        if ((!Objects.equals(mTA, paramObject.getTA())) || (!Objects.equals(mTB, paramObject.getTB())) || (!Objects.equals(mTC, paramObject.getTC())) || (!Objects.equals(mTD, paramObject.getTD()))) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public Byte getTA()
    {
      return mTA;
    }
    
    public Byte getTB()
    {
      return mTB;
    }
    
    public Byte getTC()
    {
      return mTC;
    }
    
    public Byte getTD()
    {
      return mTD;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mTA, mTB, mTC, mTD });
    }
    
    public void setTA(Byte paramByte)
    {
      mTA = paramByte;
    }
    
    public void setTB(Byte paramByte)
    {
      mTB = paramByte;
    }
    
    public void setTC(Byte paramByte)
    {
      mTC = paramByte;
    }
    
    public void setTD(Byte paramByte)
    {
      mTD = paramByte;
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("{");
      localStringBuffer.append("TA=");
      localStringBuffer.append(AnswerToReset.byteToStringHex(mTA));
      localStringBuffer.append(",");
      localStringBuffer.append("TB=");
      localStringBuffer.append(AnswerToReset.byteToStringHex(mTB));
      localStringBuffer.append(",");
      localStringBuffer.append("TC=");
      localStringBuffer.append(AnswerToReset.byteToStringHex(mTC));
      localStringBuffer.append(",");
      localStringBuffer.append("TD=");
      localStringBuffer.append(AnswerToReset.byteToStringHex(mTD));
      localStringBuffer.append("}");
      return localStringBuffer.toString();
    }
  }
}
