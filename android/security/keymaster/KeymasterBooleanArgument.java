package android.security.keymaster;

import android.os.Parcel;

class KeymasterBooleanArgument
  extends KeymasterArgument
{
  public final boolean value = true;
  
  public KeymasterBooleanArgument(int paramInt)
  {
    super(paramInt);
    if (KeymasterDefs.getTagType(paramInt) == 1879048192) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad bool tag ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public KeymasterBooleanArgument(int paramInt, Parcel paramParcel)
  {
    super(paramInt);
  }
  
  public void writeValue(Parcel paramParcel) {}
}
