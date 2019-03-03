package com.android.internal.widget;

import android.content.Context;
import android.text.BoringLayout.Metrics;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View.MeasureSpec;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

@RemoteViews.RemoteView
public class ImageFloatingTextView
  extends TextView
{
  private int mImageEndMargin;
  private int mIndentLines;
  private int mLayoutMaxLines = -1;
  private int mMaxLinesForHeight = -1;
  private int mResolvedDirection = -1;
  
  public ImageFloatingTextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ImageFloatingTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ImageFloatingTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ImageFloatingTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected Layout makeSingleLayout(int paramInt1, BoringLayout.Metrics paramMetrics, int paramInt2, Layout.Alignment paramAlignment, boolean paramBoolean1, TextUtils.TruncateAt paramTruncateAt, boolean paramBoolean2)
  {
    TransformationMethod localTransformationMethod = getTransformationMethod();
    Object localObject = getText();
    paramMetrics = (BoringLayout.Metrics)localObject;
    if (localTransformationMethod != null) {
      paramMetrics = localTransformationMethod.getTransformation((CharSequence)localObject, this);
    }
    if (paramMetrics == null) {
      paramMetrics = "";
    }
    int i = paramMetrics.length();
    localObject = getPaint();
    int j = 0;
    localObject = StaticLayout.Builder.obtain(paramMetrics, 0, i, (TextPaint)localObject, paramInt1).setAlignment(paramAlignment).setTextDirection(getTextDirectionHeuristic()).setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier()).setIncludePad(getIncludeFontPadding()).setUseLineSpacingFromFallbacks(true).setBreakStrategy(1).setHyphenationFrequency(2);
    if (mMaxLinesForHeight > 0) {
      paramInt1 = mMaxLinesForHeight;
    } else if (getMaxLines() >= 0) {
      paramInt1 = getMaxLines();
    } else {
      paramInt1 = Integer.MAX_VALUE;
    }
    ((StaticLayout.Builder)localObject).setMaxLines(paramInt1);
    mLayoutMaxLines = paramInt1;
    if (paramBoolean1) {
      ((StaticLayout.Builder)localObject).setEllipsize(paramTruncateAt).setEllipsizedWidth(paramInt2);
    }
    paramMetrics = null;
    if (mIndentLines > 0)
    {
      paramAlignment = new int[mIndentLines + 1];
      for (paramInt1 = j;; paramInt1++)
      {
        paramMetrics = paramAlignment;
        if (paramInt1 >= mIndentLines) {
          break;
        }
        paramAlignment[paramInt1] = mImageEndMargin;
      }
    }
    if (mResolvedDirection == 1) {
      ((StaticLayout.Builder)localObject).setIndents(paramMetrics, null);
    } else {
      ((StaticLayout.Builder)localObject).setIndents(null, paramMetrics);
    }
    return ((StaticLayout.Builder)localObject).build();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt2) - mPaddingTop - mPaddingBottom;
    if ((getLayout() != null) && (getLayout().getHeight() != i))
    {
      mMaxLinesForHeight = -1;
      nullLayouts();
    }
    super.onMeasure(paramInt1, paramInt2);
    Layout localLayout = getLayout();
    if (localLayout.getHeight() > i)
    {
      for (int j = localLayout.getLineCount() - 1; (j > 1) && (localLayout.getLineBottom(j - 1) > i); j--) {}
      i = j;
      if (getMaxLines() > 0) {
        i = Math.min(getMaxLines(), j);
      }
      if (i != mLayoutMaxLines)
      {
        mMaxLinesForHeight = i;
        nullLayouts();
        super.onMeasure(paramInt1, paramInt2);
      }
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    if ((paramInt != mResolvedDirection) && (isLayoutDirectionResolved()))
    {
      mResolvedDirection = paramInt;
      if (mIndentLines > 0)
      {
        nullLayouts();
        requestLayout();
      }
    }
  }
  
  @RemotableViewMethod
  public void setHasImage(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 2;
    } else {
      i = 0;
    }
    setNumIndentLines(i);
  }
  
  @RemotableViewMethod
  public void setImageEndMargin(int paramInt)
  {
    mImageEndMargin = paramInt;
  }
  
  public boolean setNumIndentLines(int paramInt)
  {
    if (mIndentLines != paramInt)
    {
      mIndentLines = paramInt;
      nullLayouts();
      requestLayout();
      return true;
    }
    return false;
  }
}
