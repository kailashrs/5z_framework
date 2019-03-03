package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class KeymasterArguments
  implements Parcelable
{
  public static final Parcelable.Creator<KeymasterArguments> CREATOR = new Parcelable.Creator()
  {
    public KeymasterArguments createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeymasterArguments(paramAnonymousParcel, null);
    }
    
    public KeymasterArguments[] newArray(int paramAnonymousInt)
    {
      return new KeymasterArguments[paramAnonymousInt];
    }
  };
  public static final long UINT32_MAX_VALUE = 4294967295L;
  private static final long UINT32_RANGE = 4294967296L;
  public static final BigInteger UINT64_MAX_VALUE;
  private static final BigInteger UINT64_RANGE = BigInteger.ONE.shiftLeft(64);
  private List<KeymasterArgument> mArguments;
  
  static
  {
    UINT64_MAX_VALUE = UINT64_RANGE.subtract(BigInteger.ONE);
  }
  
  public KeymasterArguments()
  {
    mArguments = new ArrayList();
  }
  
  private KeymasterArguments(Parcel paramParcel)
  {
    mArguments = paramParcel.createTypedArrayList(KeymasterArgument.CREATOR);
  }
  
  private void addEnumTag(int paramInt1, int paramInt2)
  {
    mArguments.add(new KeymasterIntArgument(paramInt1, paramInt2));
  }
  
  private void addLongTag(int paramInt, BigInteger paramBigInteger)
  {
    if ((paramBigInteger.signum() != -1) && (paramBigInteger.compareTo(UINT64_MAX_VALUE) <= 0))
    {
      mArguments.add(new KeymasterLongArgument(paramInt, paramBigInteger.longValue()));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Long tag value out of range: ");
    localStringBuilder.append(paramBigInteger);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private KeymasterArgument getArgumentByTag(int paramInt)
  {
    Iterator localIterator = mArguments.iterator();
    while (localIterator.hasNext())
    {
      KeymasterArgument localKeymasterArgument = (KeymasterArgument)localIterator.next();
      if (tag == paramInt) {
        return localKeymasterArgument;
      }
    }
    return null;
  }
  
  private int getEnumTagValue(KeymasterArgument paramKeymasterArgument)
  {
    return value;
  }
  
  private BigInteger getLongTagValue(KeymasterArgument paramKeymasterArgument)
  {
    return toUint64(value);
  }
  
  public static BigInteger toUint64(long paramLong)
  {
    if (paramLong >= 0L) {
      return BigInteger.valueOf(paramLong);
    }
    return BigInteger.valueOf(paramLong).add(UINT64_RANGE);
  }
  
  public void addBoolean(int paramInt)
  {
    if (KeymasterDefs.getTagType(paramInt) == 1879048192)
    {
      mArguments.add(new KeymasterBooleanArgument(paramInt));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Not a boolean tag: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void addBytes(int paramInt, byte[] paramArrayOfByte)
  {
    if (KeymasterDefs.getTagType(paramInt) == -1879048192)
    {
      if (paramArrayOfByte != null)
      {
        mArguments.add(new KeymasterBlobArgument(paramInt, paramArrayOfByte));
        return;
      }
      throw new NullPointerException("value == nulll");
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Not a bytes tag: ");
    paramArrayOfByte.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public void addDate(int paramInt, Date paramDate)
  {
    if (KeymasterDefs.getTagType(paramInt) == 1610612736)
    {
      if (paramDate != null)
      {
        if (paramDate.getTime() >= 0L)
        {
          mArguments.add(new KeymasterDateArgument(paramInt, paramDate));
          return;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Date tag value out of range: ");
        localStringBuilder.append(paramDate);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      throw new NullPointerException("value == nulll");
    }
    paramDate = new StringBuilder();
    paramDate.append("Not a date tag: ");
    paramDate.append(paramInt);
    throw new IllegalArgumentException(paramDate.toString());
  }
  
  public void addDateIfNotNull(int paramInt, Date paramDate)
  {
    if (KeymasterDefs.getTagType(paramInt) == 1610612736)
    {
      if (paramDate != null) {
        addDate(paramInt, paramDate);
      }
      return;
    }
    paramDate = new StringBuilder();
    paramDate.append("Not a date tag: ");
    paramDate.append(paramInt);
    throw new IllegalArgumentException(paramDate.toString());
  }
  
  public void addEnum(int paramInt1, int paramInt2)
  {
    int i = KeymasterDefs.getTagType(paramInt1);
    if ((i != 268435456) && (i != 536870912))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Not an enum or repeating enum tag: ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    addEnumTag(paramInt1, paramInt2);
  }
  
  public void addEnums(int paramInt, int... paramVarArgs)
  {
    if (KeymasterDefs.getTagType(paramInt) == 536870912)
    {
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++) {
        addEnumTag(paramInt, paramVarArgs[j]);
      }
      return;
    }
    paramVarArgs = new StringBuilder();
    paramVarArgs.append("Not a repeating enum tag: ");
    paramVarArgs.append(paramInt);
    throw new IllegalArgumentException(paramVarArgs.toString());
  }
  
  public void addUnsignedInt(int paramInt, long paramLong)
  {
    int i = KeymasterDefs.getTagType(paramInt);
    if ((i != 805306368) && (i != 1073741824))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Not an int or repeating int tag: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if ((paramLong >= 0L) && (paramLong <= 4294967295L))
    {
      mArguments.add(new KeymasterIntArgument(paramInt, (int)paramLong));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Int tag value out of range: ");
    localStringBuilder.append(paramLong);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void addUnsignedLong(int paramInt, BigInteger paramBigInteger)
  {
    int i = KeymasterDefs.getTagType(paramInt);
    if ((i != 1342177280) && (i != -1610612736))
    {
      paramBigInteger = new StringBuilder();
      paramBigInteger.append("Not a long or repeating long tag: ");
      paramBigInteger.append(paramInt);
      throw new IllegalArgumentException(paramBigInteger.toString());
    }
    addLongTag(paramInt, paramBigInteger);
  }
  
  public boolean containsTag(int paramInt)
  {
    boolean bool;
    if (getArgumentByTag(paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getBoolean(int paramInt)
  {
    if (KeymasterDefs.getTagType(paramInt) == 1879048192) {
      return getArgumentByTag(paramInt) != null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Not a boolean tag: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public byte[] getBytes(int paramInt, byte[] paramArrayOfByte)
  {
    if (KeymasterDefs.getTagType(paramInt) == -1879048192)
    {
      KeymasterArgument localKeymasterArgument = getArgumentByTag(paramInt);
      if (localKeymasterArgument == null) {
        return paramArrayOfByte;
      }
      return blob;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Not a bytes tag: ");
    paramArrayOfByte.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public Date getDate(int paramInt, Date paramDate)
  {
    if (KeymasterDefs.getTagType(paramInt) == 1610612736)
    {
      KeymasterArgument localKeymasterArgument = getArgumentByTag(paramInt);
      if (localKeymasterArgument == null) {
        return paramDate;
      }
      paramDate = date;
      if (paramDate.getTime() >= 0L) {
        return paramDate;
      }
      paramDate = new StringBuilder();
      paramDate.append("Tag value too large. Tag: ");
      paramDate.append(paramInt);
      throw new IllegalArgumentException(paramDate.toString());
    }
    paramDate = new StringBuilder();
    paramDate.append("Tag is not a date type: ");
    paramDate.append(paramInt);
    throw new IllegalArgumentException(paramDate.toString());
  }
  
  public int getEnum(int paramInt1, int paramInt2)
  {
    if (KeymasterDefs.getTagType(paramInt1) == 268435456)
    {
      localObject = getArgumentByTag(paramInt1);
      if (localObject == null) {
        return paramInt2;
      }
      return getEnumTagValue((KeymasterArgument)localObject);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Not an enum tag: ");
    ((StringBuilder)localObject).append(paramInt1);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public List<Integer> getEnums(int paramInt)
  {
    if (KeymasterDefs.getTagType(paramInt) == 536870912)
    {
      ArrayList localArrayList = new ArrayList();
      localObject = mArguments.iterator();
      while (((Iterator)localObject).hasNext())
      {
        KeymasterArgument localKeymasterArgument = (KeymasterArgument)((Iterator)localObject).next();
        if (tag == paramInt) {
          localArrayList.add(Integer.valueOf(getEnumTagValue(localKeymasterArgument)));
        }
      }
      return localArrayList;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Not a repeating enum tag: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public long getUnsignedInt(int paramInt, long paramLong)
  {
    if (KeymasterDefs.getTagType(paramInt) == 805306368)
    {
      localObject = getArgumentByTag(paramInt);
      if (localObject == null) {
        return paramLong;
      }
      return value & 0xFFFFFFFF;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Not an int tag: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public List<BigInteger> getUnsignedLongs(int paramInt)
  {
    if (KeymasterDefs.getTagType(paramInt) == -1610612736)
    {
      localObject = new ArrayList();
      Iterator localIterator = mArguments.iterator();
      while (localIterator.hasNext())
      {
        KeymasterArgument localKeymasterArgument = (KeymasterArgument)localIterator.next();
        if (tag == paramInt) {
          ((List)localObject).add(getLongTagValue(localKeymasterArgument));
        }
      }
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Tag is not a repeating long: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    paramParcel.readTypedList(mArguments, KeymasterArgument.CREATOR);
  }
  
  public int size()
  {
    return mArguments.size();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(mArguments);
  }
}
