package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class BackgroundColorSpan
  extends CharacterStyle
  implements UpdateAppearance, ParcelableSpan
{
  private final int mColor;
  
  public BackgroundColorSpan(int paramInt)
  {
    mColor = paramInt;
  }
  
  public BackgroundColorSpan(Parcel paramParcel)
  {
    mColor = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getBackgroundColor()
  {
    return mColor;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 12;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    bgColor = mColor;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mColor);
  }
}
