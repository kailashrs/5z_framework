package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;

public class BulletSpan
  implements LeadingMarginSpan, ParcelableSpan
{
  private static final int STANDARD_BULLET_RADIUS = 4;
  private static final int STANDARD_COLOR = 0;
  public static final int STANDARD_GAP_WIDTH = 2;
  private Path mBulletPath = null;
  private final int mBulletRadius;
  private final int mColor;
  private final int mGapWidth;
  private final boolean mWantColor;
  
  public BulletSpan()
  {
    this(2, 0, false, 4);
  }
  
  public BulletSpan(int paramInt)
  {
    this(paramInt, 0, false, 4);
  }
  
  public BulletSpan(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, true, 4);
  }
  
  public BulletSpan(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramInt2, true, paramInt3);
  }
  
  private BulletSpan(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    mGapWidth = paramInt1;
    mBulletRadius = paramInt3;
    mColor = paramInt2;
    mWantColor = paramBoolean;
  }
  
  public BulletSpan(Parcel paramParcel)
  {
    mGapWidth = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mWantColor = bool;
    mColor = paramParcel.readInt();
    mBulletRadius = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void drawLeadingMargin(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, boolean paramBoolean, Layout paramLayout)
  {
    if (((Spanned)paramCharSequence).getSpanStart(this) == paramInt6)
    {
      paramCharSequence = paramPaint.getStyle();
      paramInt4 = 0;
      if (mWantColor)
      {
        paramInt4 = paramPaint.getColor();
        paramPaint.setColor(mColor);
      }
      paramPaint.setStyle(Paint.Style.FILL);
      if (paramLayout != null) {
        paramInt5 -= paramLayout.getLineExtra(paramLayout.getLineForOffset(paramInt6));
      }
      float f1 = (paramInt3 + paramInt5) / 2.0F;
      float f2 = paramInt1 + mBulletRadius * paramInt2;
      if (paramCanvas.isHardwareAccelerated())
      {
        if (mBulletPath == null)
        {
          mBulletPath = new Path();
          mBulletPath.addCircle(0.0F, 0.0F, mBulletRadius, Path.Direction.CW);
        }
        paramCanvas.save();
        paramCanvas.translate(f2, f1);
        paramCanvas.drawPath(mBulletPath, paramPaint);
        paramCanvas.restore();
      }
      else
      {
        paramCanvas.drawCircle(f2, f1, mBulletRadius, paramPaint);
      }
      if (mWantColor) {
        paramPaint.setColor(paramInt4);
      }
      paramPaint.setStyle(paramCharSequence);
    }
  }
  
  public int getBulletRadius()
  {
    return mBulletRadius;
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
    return 2 * mBulletRadius + mGapWidth;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 8;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mGapWidth);
    paramParcel.writeInt(mWantColor);
    paramParcel.writeInt(mColor);
    paramParcel.writeInt(mBulletRadius);
  }
}
