package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;

public abstract interface LeadingMarginSpan
  extends ParagraphStyle
{
  public abstract void drawLeadingMargin(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, boolean paramBoolean, Layout paramLayout);
  
  public abstract int getLeadingMargin(boolean paramBoolean);
  
  public static abstract interface LeadingMarginSpan2
    extends LeadingMarginSpan, WrapTogetherSpan
  {
    public abstract int getLeadingMarginLineCount();
  }
  
  public static class Standard
    implements LeadingMarginSpan, ParcelableSpan
  {
    private final int mFirst;
    private final int mRest;
    
    public Standard(int paramInt)
    {
      this(paramInt, paramInt);
    }
    
    public Standard(int paramInt1, int paramInt2)
    {
      mFirst = paramInt1;
      mRest = paramInt2;
    }
    
    public Standard(Parcel paramParcel)
    {
      mFirst = paramParcel.readInt();
      mRest = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void drawLeadingMargin(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, boolean paramBoolean, Layout paramLayout) {}
    
    public int getLeadingMargin(boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = mFirst;
      } else {
        i = mRest;
      }
      return i;
    }
    
    public int getSpanTypeId()
    {
      return getSpanTypeIdInternal();
    }
    
    public int getSpanTypeIdInternal()
    {
      return 10;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      writeToParcelInternal(paramParcel, paramInt);
    }
    
    public void writeToParcelInternal(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mFirst);
      paramParcel.writeInt(mRest);
    }
  }
}
