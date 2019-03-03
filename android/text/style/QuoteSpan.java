package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;

public class QuoteSpan
  implements LeadingMarginSpan, ParcelableSpan
{
  public static final int STANDARD_COLOR = -16776961;
  public static final int STANDARD_GAP_WIDTH_PX = 2;
  public static final int STANDARD_STRIPE_WIDTH_PX = 2;
  private final int mColor;
  private final int mGapWidth;
  private final int mStripeWidth;
  
  public QuoteSpan()
  {
    this(-16776961, 2, 2);
  }
  
  public QuoteSpan(int paramInt)
  {
    this(paramInt, 2, 2);
  }
  
  public QuoteSpan(int paramInt1, int paramInt2, int paramInt3)
  {
    mColor = paramInt1;
    mStripeWidth = paramInt2;
    mGapWidth = paramInt3;
  }
  
  public QuoteSpan(Parcel paramParcel)
  {
    mColor = paramParcel.readInt();
    mStripeWidth = paramParcel.readInt();
    mGapWidth = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void drawLeadingMargin(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, boolean paramBoolean, Layout paramLayout)
  {
    paramCharSequence = paramPaint.getStyle();
    paramInt4 = paramPaint.getColor();
    paramPaint.setStyle(Paint.Style.FILL);
    paramPaint.setColor(mColor);
    paramCanvas.drawRect(paramInt1, paramInt3, mStripeWidth * paramInt2 + paramInt1, paramInt5, paramPaint);
    paramPaint.setStyle(paramCharSequence);
    paramPaint.setColor(paramInt4);
  }
  
  public int getColor()
  {
    return mColor;
  }
  
  public int getGapWidth()
  {
    return mGapWidth;
  }
  
  public int getLeadingMargin(boolean paramBoolean)
  {
    return mStripeWidth + mGapWidth;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 9;
  }
  
  public int getStripeWidth()
  {
    return mStripeWidth;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mColor);
    paramParcel.writeInt(mStripeWidth);
    paramParcel.writeInt(mGapWidth);
  }
}
