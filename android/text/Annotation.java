package android.text;

import android.os.Parcel;

public class Annotation
  implements ParcelableSpan
{
  private final String mKey;
  private final String mValue;
  
  public Annotation(Parcel paramParcel)
  {
    mKey = paramParcel.readString();
    mValue = paramParcel.readString();
  }
  
  public Annotation(String paramString1, String paramString2)
  {
    mKey = paramString1;
    mValue = paramString2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getKey()
  {
    return mKey;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 18;
  }
  
  public String getValue()
  {
    return mValue;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mKey);
    paramParcel.writeString(mValue);
  }
}
