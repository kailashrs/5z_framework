package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class ForegroundColorSpan
  extends CharacterStyle
  implements UpdateAppearance, ParcelableSpan
{
  private final int mColor;
  
  public ForegroundColorSpan(int paramInt)
  {
    mColor = paramInt;
  }
  
  public ForegroundColorSpan(Parcel paramParcel)
  {
    mColor = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getForegroundColor()
  {
    return mColor;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 2;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setColor(mColor);
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
