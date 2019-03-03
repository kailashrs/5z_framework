package android.text.style;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class StyleSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final int mStyle;
  
  public StyleSpan(int paramInt)
  {
    mStyle = paramInt;
  }
  
  public StyleSpan(Parcel paramParcel)
  {
    mStyle = paramParcel.readInt();
  }
  
  private static void apply(Paint paramPaint, int paramInt)
  {
    Typeface localTypeface1 = paramPaint.getTypeface();
    int i;
    if (localTypeface1 == null) {
      i = 0;
    } else {
      i = localTypeface1.getStyle();
    }
    i |= paramInt;
    Typeface localTypeface2 = localTypeface1;
    if (paramPaint.getTextSkewX() == -0.25F)
    {
      localTypeface2 = localTypeface1;
      if (paramInt == 1)
      {
        localTypeface2 = localTypeface1;
        if (localTypeface1 == Typeface.defaultFromStyle(2)) {
          localTypeface2 = Typeface.defaultFromStyle(1);
        }
      }
    }
    if (localTypeface2 == null) {
      localTypeface2 = Typeface.defaultFromStyle(i);
    } else {
      localTypeface2 = Typeface.create(localTypeface2, i);
    }
    paramInt = localTypeface2.getStyle() & i;
    if ((paramInt & 0x1) != 0) {
      paramPaint.setFakeBoldText(true);
    }
    if ((paramInt & 0x2) != 0) {
      paramPaint.setTextSkewX(-0.25F);
    }
    paramPaint.setTypeface(localTypeface2);
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
    return 7;
  }
  
  public int getStyle()
  {
    return mStyle;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    apply(paramTextPaint, mStyle);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    apply(paramTextPaint, mStyle);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStyle);
  }
}
