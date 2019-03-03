package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class StrikethroughSpan
  extends CharacterStyle
  implements UpdateAppearance, ParcelableSpan
{
  public StrikethroughSpan() {}
  
  public StrikethroughSpan(Parcel paramParcel) {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 5;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setStrikeThruText(true);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt) {}
}
