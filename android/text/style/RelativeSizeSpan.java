package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class RelativeSizeSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final float mProportion;
  
  public RelativeSizeSpan(float paramFloat)
  {
    mProportion = paramFloat;
  }
  
  public RelativeSizeSpan(Parcel paramParcel)
  {
    mProportion = paramParcel.readFloat();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float getSizeChange()
  {
    return mProportion;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 3;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setTextSize(paramTextPaint.getTextSize() * mProportion);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    paramTextPaint.setTextSize(paramTextPaint.getTextSize() * mProportion);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(mProportion);
  }
}
