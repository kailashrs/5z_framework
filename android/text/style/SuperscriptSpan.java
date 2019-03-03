package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class SuperscriptSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  public SuperscriptSpan() {}
  
  public SuperscriptSpan(Parcel paramParcel) {}
  
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
    return 14;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    baselineShift += (int)(paramTextPaint.ascent() / 2.0F);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    baselineShift += (int)(paramTextPaint.ascent() / 2.0F);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt) {}
}
