package android.security.keymaster;

import android.os.Parcel;

class KeymasterLongArgument
  extends KeymasterArgument
{
  public final long value;
  
  public KeymasterLongArgument(int paramInt, long paramLong)
  {
    super(paramInt);
    int i = KeymasterDefs.getTagType(paramInt);
    if ((i != -1610612736) && (i != 1342177280))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad long tag ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    value = paramLong;
  }
  
  public KeymasterLongArgument(int paramInt, Parcel paramParcel)
  {
    super(paramInt);
    value = paramParcel.readLong();
  }
  
  public void writeValue(Parcel paramParcel)
  {
    paramParcel.writeLong(value);
  }
}
