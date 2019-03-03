package com.android.internal.telephony.cat;

import java.util.Iterator;
import java.util.List;

class BerTlv
{
  public static final int BER_EVENT_DOWNLOAD_TAG = 214;
  public static final int BER_MENU_SELECTION_TAG = 211;
  public static final int BER_PROACTIVE_COMMAND_TAG = 208;
  public static final int BER_UNKNOWN_TAG = 0;
  private List<ComprehensionTlv> mCompTlvs = null;
  private boolean mLengthValid = true;
  private int mTag = 0;
  
  private BerTlv(int paramInt, List<ComprehensionTlv> paramList, boolean paramBoolean)
  {
    mTag = paramInt;
    mCompTlvs = paramList;
    mLengthValid = paramBoolean;
  }
  
  public static BerTlv decode(byte[] paramArrayOfByte)
    throws ResultException
  {
    int i = paramArrayOfByte.length;
    int j = 0;
    boolean bool1 = true;
    boolean bool2 = true;
    int k = 0 + 1;
    int m = paramArrayOfByte[0];
    int n = m & 0xFF;
    int i1;
    if (n == 208)
    {
      m = k + 1;
      k = paramArrayOfByte[k];
      i1 = k & 0xFF;
      if (i1 < 128)
      {
        k = m;
        m = i1;
      }
      else
      {
        if (i1 != 129) {
          break label204;
        }
        k = m + 1;
        m = paramArrayOfByte[m] & 0xFF;
        if (m < 128) {
          break label114;
        }
      }
      i1 = n;
      break label327;
      label114:
      Object localObject1;
      try
      {
        localObject1 = new com/android/internal/telephony/cat/ResultException;
        paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("length < 0x80 length=");
        ((StringBuilder)localObject2).append(Integer.toHexString(0));
        ((StringBuilder)localObject2).append(" curIndex=");
        ((StringBuilder)localObject2).append(k);
        ((StringBuilder)localObject2).append(" endIndex=");
        ((StringBuilder)localObject2).append(i);
        ((ResultException)localObject1).<init>(paramArrayOfByte, ((StringBuilder)localObject2).toString());
        throw ((Throwable)localObject1);
      }
      catch (ResultException paramArrayOfByte)
      {
        break label541;
      }
      catch (IndexOutOfBoundsException paramArrayOfByte) {}
      try
      {
        label204:
        localObject2 = new com/android/internal/telephony/cat/ResultException;
        localObject1 = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append("Expected first byte to be length or a length tag and < 0x81 byte= ");
        paramArrayOfByte.append(Integer.toHexString(i1));
        paramArrayOfByte.append(" curIndex=");
        paramArrayOfByte.append(m);
        paramArrayOfByte.append(" endIndex=");
        paramArrayOfByte.append(i);
        ((ResultException)localObject2).<init>((ResultCode)localObject1, paramArrayOfByte.toString());
        throw ((Throwable)localObject2);
      }
      catch (ResultException paramArrayOfByte) {}catch (IndexOutOfBoundsException paramArrayOfByte)
      {
        k = m;
      }
    }
    try
    {
      int i2 = ComprehensionTlvTag.COMMAND_DETAILS.value();
      i1 = n;
      m = j;
      if (i2 == (n & 0xFF7F))
      {
        i1 = 0;
        k = 0;
        m = j;
      }
      label327:
      if (i - k >= m)
      {
        localObject2 = ComprehensionTlv.decodeMany(paramArrayOfByte, k);
        if (i1 == 208)
        {
          k = 0;
          paramArrayOfByte = ((List)localObject2).iterator();
          for (;;)
          {
            bool1 = bool2;
            if (!paramArrayOfByte.hasNext()) {
              break label444;
            }
            n = ((ComprehensionTlv)paramArrayOfByte.next()).getLength();
            if ((n >= 128) && (n <= 255))
            {
              k += n + 3;
            }
            else
            {
              if ((n < 0) || (n >= 128)) {
                break;
              }
              k += n + 2;
            }
          }
          bool1 = false;
          label444:
          if (m != k) {
            bool1 = false;
          }
        }
        return new BerTlv(i1, (List)localObject2, bool1);
      }
      paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Command had extra data endIndex=");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(" curIndex=");
      ((StringBuilder)localObject2).append(k);
      ((StringBuilder)localObject2).append(" length=");
      ((StringBuilder)localObject2).append(m);
      throw new ResultException(paramArrayOfByte, ((StringBuilder)localObject2).toString());
    }
    catch (ResultException paramArrayOfByte)
    {
      label541:
      throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, paramArrayOfByte.explanation());
    }
    catch (IndexOutOfBoundsException paramArrayOfByte) {}
    Object localObject2 = ResultCode.REQUIRED_VALUES_MISSING;
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("IndexOutOfBoundsException  curIndex=");
    paramArrayOfByte.append(k);
    paramArrayOfByte.append(" endIndex=");
    paramArrayOfByte.append(i);
    throw new ResultException((ResultCode)localObject2, paramArrayOfByte.toString());
  }
  
  public List<ComprehensionTlv> getComprehensionTlvs()
  {
    return mCompTlvs;
  }
  
  public int getTag()
  {
    return mTag;
  }
  
  public boolean isLengthValid()
  {
    return mLengthValid;
  }
}
