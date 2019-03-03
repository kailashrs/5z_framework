package android.security.keymaster;

import android.os.Parcel;

class KeymasterIntArgument
  extends KeymasterArgument
{
  public final int value;
  
  public KeymasterIntArgument(int paramInt1, int paramInt2)
  {
    super(paramInt1);
    int i = KeymasterDefs.getTagType(paramInt1);
    if ((i != 268435456) && (i != 536870912) && (i != 805306368) && (i != 1073741824))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad int tag ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    value = paramInt2;
  }
  
  public KeymasterIntArgument(int paramInt, Parcel paramParcel)
  {
    super(paramInt);
    value = paramParcel.readInt();
  }
  
  public void writeValue(Parcel paramParcel)
  {
    paramParcel.writeInt(value);
  }
}
