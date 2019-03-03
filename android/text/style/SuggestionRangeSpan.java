package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class SuggestionRangeSpan
  extends CharacterStyle
  implements ParcelableSpan
{
  private int mBackgroundColor;
  
  public SuggestionRangeSpan()
  {
    mBackgroundColor = 0;
  }
  
  public SuggestionRangeSpan(Parcel paramParcel)
  {
    mBackgroundColor = paramParcel.readInt();
  }
  
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
    return 21;
  }
  
  public void setBackgroundColor(int paramInt)
  {
    mBackgroundColor = paramInt;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    bgColor = mBackgroundColor;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mBackgroundColor);
  }
}
