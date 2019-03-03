package android.text.style;

import android.graphics.LeakyTypefaceStorage;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class TypefaceSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final String mFamily;
  private final Typeface mTypeface;
  
  public TypefaceSpan(Typeface paramTypeface)
  {
    this(null, paramTypeface);
  }
  
  public TypefaceSpan(Parcel paramParcel)
  {
    mFamily = paramParcel.readString();
    mTypeface = LeakyTypefaceStorage.readTypefaceFromParcel(paramParcel);
  }
  
  public TypefaceSpan(String paramString)
  {
    this(paramString, null);
  }
  
  private TypefaceSpan(String paramString, Typeface paramTypeface)
  {
    mFamily = paramString;
    mTypeface = paramTypeface;
  }
  
  private void applyFontFamily(Paint paramPaint, String paramString)
  {
    Typeface localTypeface = paramPaint.getTypeface();
    if (localTypeface == null) {
      i = 0;
    } else {
      i = localTypeface.getStyle();
    }
    paramString = Typeface.create(paramString, i);
    int i = paramString.getStyle() & i;
    if ((i & 0x1) != 0) {
      paramPaint.setFakeBoldText(true);
    }
    if ((i & 0x2) != 0) {
      paramPaint.setTextSkewX(-0.25F);
    }
    paramPaint.setTypeface(paramString);
  }
  
  private void updateTypeface(Paint paramPaint)
  {
    if (mTypeface != null) {
      paramPaint.setTypeface(mTypeface);
    } else if (mFamily != null) {
      applyFontFamily(paramPaint, mFamily);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getFamily()
  {
    return mFamily;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 13;
  }
  
  public Typeface getTypeface()
  {
    return mTypeface;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    updateTypeface(paramTextPaint);
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    updateTypeface(paramTextPaint);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mFamily);
    LeakyTypefaceStorage.writeTypefaceToParcel(mTypeface, paramParcel);
  }
}
