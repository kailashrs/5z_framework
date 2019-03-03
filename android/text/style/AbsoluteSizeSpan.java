package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;

public class AbsoluteSizeSpan
  extends MetricAffectingSpan
  implements ParcelableSpan
{
  private final boolean mDip;
  private final int mSize;
  
  public AbsoluteSizeSpan(int paramInt)
  {
    this(paramInt, false);
  }
  
  public AbsoluteSizeSpan(int paramInt, boolean paramBoolean)
  {
    mSize = paramInt;
    mDip = paramBoolean;
  }
  
  public AbsoluteSizeSpan(Parcel paramParcel)
  {
    mSize = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mDip = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getDip()
  {
    return mDip;
  }
  
  public int getSize()
  {
    return mSize;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 16;
  }
  
  public void updateDrawState(TextPaint paramTextPaint)
  {
    if (mDip) {
      paramTextPaint.setTextSize(mSize * density);
    } else {
      paramTextPaint.setTextSize(mSize);
    }
  }
  
  public void updateMeasureState(TextPaint paramTextPaint)
  {
    if (mDip) {
      paramTextPaint.setTextSize(mSize * density);
    } else {
      paramTextPaint.setTextSize(mSize);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSize);
    paramParcel.writeInt(mDip);
  }
}
