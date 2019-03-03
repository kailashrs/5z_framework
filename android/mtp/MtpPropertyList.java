package android.mtp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class MtpPropertyList
{
  private int mCode;
  private List<Integer> mDataTypes;
  private List<Long> mLongValues;
  private List<Integer> mObjectHandles;
  private List<Integer> mPropertyCodes;
  private List<String> mStringValues;
  
  public MtpPropertyList(int paramInt)
  {
    mCode = paramInt;
    mObjectHandles = new ArrayList();
    mPropertyCodes = new ArrayList();
    mDataTypes = new ArrayList();
    mLongValues = new ArrayList();
    mStringValues = new ArrayList();
  }
  
  public void append(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    mObjectHandles.add(Integer.valueOf(paramInt1));
    mPropertyCodes.add(Integer.valueOf(paramInt2));
    mDataTypes.add(Integer.valueOf(paramInt3));
    mLongValues.add(Long.valueOf(paramLong));
    mStringValues.add(null);
  }
  
  public void append(int paramInt1, int paramInt2, String paramString)
  {
    mObjectHandles.add(Integer.valueOf(paramInt1));
    mPropertyCodes.add(Integer.valueOf(paramInt2));
    mDataTypes.add(Integer.valueOf(65535));
    mStringValues.add(paramString);
    mLongValues.add(Long.valueOf(0L));
  }
  
  public int getCode()
  {
    return mCode;
  }
  
  public int getCount()
  {
    return mObjectHandles.size();
  }
  
  public int[] getDataTypes()
  {
    return mDataTypes.stream().mapToInt(_..Lambda.MtpPropertyList.UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
  }
  
  public long[] getLongValues()
  {
    return mLongValues.stream().mapToLong(_..Lambda.MtpPropertyList.ELHKvd8JMVRD8rbALqYPKbDX2mM.INSTANCE).toArray();
  }
  
  public int[] getObjectHandles()
  {
    return mObjectHandles.stream().mapToInt(_..Lambda.MtpPropertyList.UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
  }
  
  public int[] getPropertyCodes()
  {
    return mPropertyCodes.stream().mapToInt(_..Lambda.MtpPropertyList.UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
  }
  
  public String[] getStringValues()
  {
    return (String[])mStringValues.toArray(new String[0]);
  }
}
