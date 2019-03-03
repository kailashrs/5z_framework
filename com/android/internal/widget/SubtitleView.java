package com.android.internal.widget;

import android.R.styleable;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.CaptioningManager.CaptionStyle;

public class SubtitleView
  extends View
{
  private static final int COLOR_BEVEL_DARK = Integer.MIN_VALUE;
  private static final int COLOR_BEVEL_LIGHT = -2130706433;
  private static final float INNER_PADDING_RATIO = 0.125F;
  private Layout.Alignment mAlignment;
  private int mBackgroundColor;
  private final float mCornerRadius;
  private int mEdgeColor;
  private int mEdgeType;
  private int mForegroundColor;
  private boolean mHasMeasurements;
  private int mInnerPaddingX;
  private int mLastMeasuredWidth;
  private StaticLayout mLayout;
  private final RectF mLineBounds = new RectF();
  private final float mOutlineWidth;
  private Paint mPaint;
  private final float mShadowOffsetX;
  private final float mShadowOffsetY;
  private final float mShadowRadius;
  private float mSpacingAdd = 0.0F;
  private float mSpacingMult = 1.0F;
  private final SpannableStringBuilder mText = new SpannableStringBuilder();
  private TextPaint mTextPaint;
  
  public SubtitleView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SubtitleView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SubtitleView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SubtitleView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet);
    int i = 0;
    mInnerPaddingX = 0;
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TextView, paramInt1, paramInt2);
    paramContext = "";
    paramInt2 = 15;
    int j = paramAttributeSet.getIndexCount();
    for (paramInt1 = i; paramInt1 < j; paramInt1++)
    {
      i = paramAttributeSet.getIndex(paramInt1);
      if (i != 0)
      {
        if (i != 18) {
          switch (i)
          {
          default: 
            break;
          case 54: 
            mSpacingMult = paramAttributeSet.getFloat(i, mSpacingMult);
            break;
          case 53: 
            mSpacingAdd = paramAttributeSet.getDimensionPixelSize(i, (int)mSpacingAdd);
            break;
          }
        } else {
          paramContext = paramAttributeSet.getText(i);
        }
      }
      else {
        paramInt2 = paramAttributeSet.getDimensionPixelSize(i, paramInt2);
      }
    }
    paramAttributeSet = getContext().getResources();
    mCornerRadius = paramAttributeSet.getDimensionPixelSize(17105431);
    mOutlineWidth = paramAttributeSet.getDimensionPixelSize(17105432);
    mShadowRadius = paramAttributeSet.getDimensionPixelSize(17105434);
    mShadowOffsetX = paramAttributeSet.getDimensionPixelSize(17105433);
    mShadowOffsetY = mShadowOffsetX;
    mTextPaint = new TextPaint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setSubpixelText(true);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    setText(paramContext);
    setTextSize(paramInt2);
  }
  
  private boolean computeMeasurements(int paramInt)
  {
    if ((mHasMeasurements) && (paramInt == mLastMeasuredWidth)) {
      return true;
    }
    paramInt -= mPaddingLeft + mPaddingRight + mInnerPaddingX * 2;
    if (paramInt <= 0) {
      return false;
    }
    mHasMeasurements = true;
    mLastMeasuredWidth = paramInt;
    mLayout = StaticLayout.Builder.obtain(mText, 0, mText.length(), mTextPaint, paramInt).setAlignment(mAlignment).setLineSpacing(mSpacingAdd, mSpacingMult).setUseLineSpacingFromFallbacks(true).build();
    return true;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    StaticLayout localStaticLayout = mLayout;
    if (localStaticLayout == null) {
      return;
    }
    int i = paramCanvas.save();
    int j = mInnerPaddingX;
    paramCanvas.translate(mPaddingLeft + j, mPaddingTop);
    int k = localStaticLayout.getLineCount();
    TextPaint localTextPaint = mTextPaint;
    Paint localPaint = mPaint;
    RectF localRectF = mLineBounds;
    float f2;
    if (Color.alpha(mBackgroundColor) > 0)
    {
      float f1 = mCornerRadius;
      f2 = localStaticLayout.getLineTop(0);
      localPaint.setColor(mBackgroundColor);
      localPaint.setStyle(Paint.Style.FILL);
      for (m = 0; m < k; m++)
      {
        left = (localStaticLayout.getLineLeft(m) - j);
        right = (localStaticLayout.getLineRight(m) + j);
        top = f2;
        bottom = localStaticLayout.getLineBottom(m);
        f2 = bottom;
        paramCanvas.drawRoundRect(localRectF, f1, f1, localPaint);
      }
    }
    int m = mEdgeType;
    j = 1;
    if (m == 1)
    {
      localTextPaint.setStrokeJoin(Paint.Join.ROUND);
      localTextPaint.setStrokeWidth(mOutlineWidth);
      localTextPaint.setColor(mEdgeColor);
      localTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
      for (m = 0; m < k; m++) {
        localStaticLayout.drawText(paramCanvas, m, m);
      }
    }
    if (m == 2) {
      localTextPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mEdgeColor);
    }
    for (;;)
    {
      break;
      if ((m == 3) || (m == 4))
      {
        if (m != 3) {
          j = 0;
        }
        m = -1;
        int n;
        if (j != 0) {
          n = -1;
        } else {
          n = mEdgeColor;
        }
        if (j != 0) {
          m = mEdgeColor;
        }
        f2 = mShadowRadius / 2.0F;
        localTextPaint.setColor(mForegroundColor);
        localTextPaint.setStyle(Paint.Style.FILL);
        localTextPaint.setShadowLayer(mShadowRadius, -f2, -f2, n);
        for (j = 0; j < k; j++) {
          localStaticLayout.drawText(paramCanvas, j, j);
        }
        localTextPaint.setShadowLayer(mShadowRadius, f2, f2, m);
      }
    }
    localTextPaint.setColor(mForegroundColor);
    localTextPaint.setStyle(Paint.Style.FILL);
    for (m = 0; m < k; m++) {
      localStaticLayout.drawText(paramCanvas, m, m);
    }
    localTextPaint.setShadowLayer(0.0F, 0.0F, 0.0F, 0);
    paramCanvas.restoreToCount(i);
  }
  
  public void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    computeMeasurements(paramInt3 - paramInt1);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (computeMeasurements(View.MeasureSpec.getSize(paramInt1)))
    {
      StaticLayout localStaticLayout = mLayout;
      paramInt1 = mPaddingLeft;
      int i = mPaddingRight;
      paramInt2 = mInnerPaddingX;
      setMeasuredDimension(localStaticLayout.getWidth() + (paramInt1 + i + paramInt2 * 2), localStaticLayout.getHeight() + mPaddingTop + mPaddingBottom);
    }
    else
    {
      setMeasuredDimension(16777216, 16777216);
    }
  }
  
  public void setAlignment(Layout.Alignment paramAlignment)
  {
    if (mAlignment != paramAlignment)
    {
      mAlignment = paramAlignment;
      mHasMeasurements = false;
      requestLayout();
      invalidate();
    }
  }
  
  public void setBackgroundColor(int paramInt)
  {
    mBackgroundColor = paramInt;
    invalidate();
  }
  
  public void setEdgeColor(int paramInt)
  {
    mEdgeColor = paramInt;
    invalidate();
  }
  
  public void setEdgeType(int paramInt)
  {
    mEdgeType = paramInt;
    invalidate();
  }
  
  public void setForegroundColor(int paramInt)
  {
    mForegroundColor = paramInt;
    invalidate();
  }
  
  public void setStyle(int paramInt)
  {
    Object localObject = mContext.getContentResolver();
    if (paramInt == -1) {
      localObject = CaptioningManager.CaptionStyle.getCustomStyle((ContentResolver)localObject);
    } else {
      localObject = CaptioningManager.CaptionStyle.PRESETS[paramInt];
    }
    CaptioningManager.CaptionStyle localCaptionStyle = CaptioningManager.CaptionStyle.DEFAULT;
    if (((CaptioningManager.CaptionStyle)localObject).hasForegroundColor()) {
      paramInt = foregroundColor;
    } else {
      paramInt = foregroundColor;
    }
    mForegroundColor = paramInt;
    if (((CaptioningManager.CaptionStyle)localObject).hasBackgroundColor()) {
      paramInt = backgroundColor;
    } else {
      paramInt = backgroundColor;
    }
    mBackgroundColor = paramInt;
    if (((CaptioningManager.CaptionStyle)localObject).hasEdgeType()) {
      paramInt = edgeType;
    } else {
      paramInt = edgeType;
    }
    mEdgeType = paramInt;
    if (((CaptioningManager.CaptionStyle)localObject).hasEdgeColor()) {
      paramInt = edgeColor;
    } else {
      paramInt = edgeColor;
    }
    mEdgeColor = paramInt;
    mHasMeasurements = false;
    setTypeface(((CaptioningManager.CaptionStyle)localObject).getTypeface());
    requestLayout();
  }
  
  public void setText(int paramInt)
  {
    setText(getContext().getText(paramInt));
  }
  
  public void setText(CharSequence paramCharSequence)
  {
    mText.clear();
    mText.append(paramCharSequence);
    mHasMeasurements = false;
    requestLayout();
    invalidate();
  }
  
  public void setTextSize(float paramFloat)
  {
    if (mTextPaint.getTextSize() != paramFloat)
    {
      mTextPaint.setTextSize(paramFloat);
      mInnerPaddingX = ((int)(0.125F * paramFloat + 0.5F));
      mHasMeasurements = false;
      requestLayout();
      invalidate();
    }
  }
  
  public void setTypeface(Typeface paramTypeface)
  {
    if (mTextPaint.getTypeface() != paramTypeface)
    {
      mTextPaint.setTypeface(paramTypeface);
      mHasMeasurements = false;
      requestLayout();
      invalidate();
    }
  }
}
