package android.media;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

class Cea608CCWidget
  extends ClosedCaptionWidget
  implements Cea608CCParser.DisplayListener
{
  private static final String mDummyText = "1234567890123456789012345678901234";
  private static final Rect mTextBounds = new Rect();
  
  public Cea608CCWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Cea608CCWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Cea608CCWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Cea608CCWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public ClosedCaptionWidget.ClosedCaptionLayout createCaptionLayout(Context paramContext)
  {
    return new CCLayout(paramContext);
  }
  
  public CaptioningManager.CaptionStyle getCaptionStyle()
  {
    return mCaptionStyle;
  }
  
  public void onDisplayChanged(SpannableStringBuilder[] paramArrayOfSpannableStringBuilder)
  {
    ((CCLayout)mClosedCaptionLayout).update(paramArrayOfSpannableStringBuilder);
    if (mListener != null) {
      mListener.onChanged(this);
    }
  }
  
  private static class CCLayout
    extends LinearLayout
    implements ClosedCaptionWidget.ClosedCaptionLayout
  {
    private static final int MAX_ROWS = 15;
    private static final float SAFE_AREA_RATIO = 0.9F;
    private final Cea608CCWidget.CCLineBox[] mLineBoxes = new Cea608CCWidget.CCLineBox[15];
    
    CCLayout(Context paramContext)
    {
      super();
      setGravity(8388611);
      setOrientation(1);
      for (int i = 0; i < 15; i++)
      {
        mLineBoxes[i] = new Cea608CCWidget.CCLineBox(getContext());
        addView(mLineBoxes[i], -2, -2);
      }
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      paramInt3 -= paramInt1;
      paramInt4 -= paramInt2;
      if (paramInt3 * 3 >= paramInt4 * 4)
      {
        paramInt2 = paramInt4 * 4 / 3;
        paramInt1 = paramInt4;
      }
      else
      {
        paramInt2 = paramInt3;
        paramInt1 = paramInt3 * 3 / 4;
      }
      paramInt2 = (int)(paramInt2 * 0.9F);
      int i = (int)(paramInt1 * 0.9F);
      paramInt3 = (paramInt3 - paramInt2) / 2;
      paramInt4 = (paramInt4 - i) / 2;
      for (paramInt1 = 0; paramInt1 < 15; paramInt1++) {
        mLineBoxes[paramInt1].layout(paramInt3, i * paramInt1 / 15 + paramInt4, paramInt3 + paramInt2, paramInt4 + (paramInt1 + 1) * i / 15);
      }
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      super.onMeasure(paramInt1, paramInt2);
      paramInt2 = getMeasuredWidth();
      paramInt1 = getMeasuredHeight();
      if (paramInt2 * 3 >= paramInt1 * 4) {
        paramInt2 = paramInt1 * 4 / 3;
      } else {
        paramInt1 = paramInt2 * 3 / 4;
      }
      int i = (int)(paramInt2 * 0.9F);
      paramInt2 = View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * 0.9F) / 15, 1073741824);
      i = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
      for (paramInt1 = 0; paramInt1 < 15; paramInt1++) {
        mLineBoxes[paramInt1].measure(i, paramInt2);
      }
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      for (int i = 0; i < 15; i++) {
        mLineBoxes[i].setCaptionStyle(paramCaptionStyle);
      }
    }
    
    public void setFontScale(float paramFloat) {}
    
    void update(SpannableStringBuilder[] paramArrayOfSpannableStringBuilder)
    {
      for (int i = 0; i < 15; i++) {
        if (paramArrayOfSpannableStringBuilder[i] != null)
        {
          mLineBoxes[i].setText(paramArrayOfSpannableStringBuilder[i], TextView.BufferType.SPANNABLE);
          mLineBoxes[i].setVisibility(0);
        }
        else
        {
          mLineBoxes[i].setVisibility(4);
        }
      }
    }
  }
  
  private static class CCLineBox
    extends TextView
  {
    private static final float EDGE_OUTLINE_RATIO = 0.1F;
    private static final float EDGE_SHADOW_RATIO = 0.05F;
    private static final float FONT_PADDING_RATIO = 0.75F;
    private int mBgColor = -16777216;
    private int mEdgeColor = 0;
    private int mEdgeType = 0;
    private float mOutlineWidth;
    private float mShadowOffset;
    private float mShadowRadius;
    private int mTextColor = -1;
    
    CCLineBox(Context paramContext)
    {
      super();
      setGravity(17);
      setBackgroundColor(0);
      setTextColor(-1);
      setTypeface(Typeface.MONOSPACE);
      setVisibility(4);
      paramContext = getContext().getResources();
      mOutlineWidth = paramContext.getDimensionPixelSize(17105432);
      mShadowRadius = paramContext.getDimensionPixelSize(17105434);
      mShadowOffset = paramContext.getDimensionPixelSize(17105433);
    }
    
    private void drawEdgeOutline(Canvas paramCanvas)
    {
      TextPaint localTextPaint = getPaint();
      Paint.Style localStyle = localTextPaint.getStyle();
      Paint.Join localJoin = localTextPaint.getStrokeJoin();
      float f = localTextPaint.getStrokeWidth();
      setTextColor(mEdgeColor);
      localTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
      localTextPaint.setStrokeJoin(Paint.Join.ROUND);
      localTextPaint.setStrokeWidth(mOutlineWidth);
      super.onDraw(paramCanvas);
      setTextColor(mTextColor);
      localTextPaint.setStyle(localStyle);
      localTextPaint.setStrokeJoin(localJoin);
      localTextPaint.setStrokeWidth(f);
      setBackgroundSpans(0);
      super.onDraw(paramCanvas);
      setBackgroundSpans(mBgColor);
    }
    
    private void drawEdgeRaisedOrDepressed(Canvas paramCanvas)
    {
      TextPaint localTextPaint = getPaint();
      Paint.Style localStyle = localTextPaint.getStyle();
      localTextPaint.setStyle(Paint.Style.FILL);
      int i;
      if (mEdgeType == 3) {
        i = 1;
      } else {
        i = 0;
      }
      int j = -1;
      int k;
      if (i != 0) {
        k = -1;
      } else {
        k = mEdgeColor;
      }
      if (i != 0) {
        j = mEdgeColor;
      }
      float f = mShadowRadius / 2.0F;
      setShadowLayer(mShadowRadius, -f, -f, k);
      super.onDraw(paramCanvas);
      setBackgroundSpans(0);
      setShadowLayer(mShadowRadius, f, f, j);
      super.onDraw(paramCanvas);
      localTextPaint.setStyle(localStyle);
      setBackgroundSpans(mBgColor);
    }
    
    private void setBackgroundSpans(int paramInt)
    {
      Object localObject = getText();
      if ((localObject instanceof Spannable))
      {
        localObject = (Spannable)localObject;
        int i = ((Spannable)localObject).length();
        int j = 0;
        localObject = (Cea608CCParser.MutableBackgroundColorSpan[])((Spannable)localObject).getSpans(0, i, Cea608CCParser.MutableBackgroundColorSpan.class);
        while (j < localObject.length)
        {
          localObject[j].setBackgroundColor(paramInt);
          j++;
        }
      }
    }
    
    protected void onDraw(Canvas paramCanvas)
    {
      if ((mEdgeType != -1) && (mEdgeType != 0) && (mEdgeType != 2))
      {
        if (mEdgeType == 1) {
          drawEdgeOutline(paramCanvas);
        } else {
          drawEdgeRaisedOrDepressed(paramCanvas);
        }
        return;
      }
      super.onDraw(paramCanvas);
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      float f = View.MeasureSpec.getSize(paramInt2) * 0.75F;
      setTextSize(0, f);
      mOutlineWidth = (0.1F * f + 1.0F);
      mShadowRadius = (0.05F * f + 1.0F);
      mShadowOffset = mShadowRadius;
      setScaleX(1.0F);
      getPaint().getTextBounds("1234567890123456789012345678901234", 0, "1234567890123456789012345678901234".length(), Cea608CCWidget.mTextBounds);
      f = Cea608CCWidget.mTextBounds.width();
      setScaleX(View.MeasureSpec.getSize(paramInt1) / f);
      super.onMeasure(paramInt1, paramInt2);
    }
    
    void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      mTextColor = foregroundColor;
      mBgColor = backgroundColor;
      mEdgeType = edgeType;
      mEdgeColor = edgeColor;
      setTextColor(mTextColor);
      if (mEdgeType == 2) {
        setShadowLayer(mShadowRadius, mShadowOffset, mShadowOffset, mEdgeColor);
      } else {
        setShadowLayer(0.0F, 0.0F, 0.0F, 0);
      }
      invalidate();
    }
  }
}
