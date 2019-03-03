package android.security.keymaster;

import android.os.Parcel;

class KeymasterBlobArgument
  extends KeymasterArgument
{
  public final byte[] blob;
  
  public KeymasterBlobArgument(int paramInt, Parcel paramParcel)
  {
    super(paramInt);
    blob = paramParcel.createByteArray();
  }
  
  public KeymasterBlobArgument(int paramInt, byte[] paramArrayOfByte)
  {
    super(paramInt);
    int i = KeymasterDefs.getTagType(paramInt);
    if ((i != Integer.MIN_VALUE) && (i != -1879048192))
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Bad blob tag ");
      paramArrayOfByte.append(paramInt);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    blob = paramArrayOfByte;
  }
  
  public void writeValue(Parcel paramParcel)
  {
    paramParcel.writeByteArray(blob);
  }
}
