package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class ScaleXSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final float mProportion;
  
  public ScaleXSpan(float paramFloat)
  {
    mProportion = paramFloat;
  }
  
  public ScaleXSpan(Parcel paramParcel)
  {
    mProportion = paramParcel.readFloat();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float getScaleX()
  {
    return mProportion;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 4;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    paramTextPaint.setTextScaleX(paramTextPaint.getTextScaleX() * mProportion);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    paramTextPaint.setTextScaleX(paramTextPaint.getTextScaleX() * mProportion);
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
