package android.text;

import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public class TextLine
{
  private static final boolean DEBUG = false;
  private static final int TAB_INCREMENT = 20;
  private static final TextLine[] sCached = new TextLine[3];
  private final TextPaint mActivePaint = new TextPaint();
  private float mAddedWidth;
  private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet(CharacterStyle.class);
  private char[] mChars;
  private boolean mCharsValid;
  private PrecomputedText mComputed;
  private final DecorationInfo mDecorationInfo = new DecorationInfo(null);
  private final ArrayList<DecorationInfo> mDecorations = new ArrayList();
  private int mDir;
  private Layout.Directions mDirections;
  private boolean mHasTabs;
  private int mLen;
  private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet(MetricAffectingSpan.class);
  private TextPaint mPaint;
  private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet(ReplacementSpan.class);
  private Spanned mSpanned;
  private int mStart;
  private Layout.TabStops mTabs;
  private CharSequence mText;
  private final TextPaint mWorkPaint = new TextPaint();
  
  public TextLine() {}
  
  private int adjustHyphenEdit(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt3;
    paramInt3 = i;
    if (paramInt1 > 0) {
      paramInt3 = i & 0xFFFFFFE7;
    }
    paramInt1 = paramInt3;
    if (paramInt2 < mLen) {
      paramInt1 = paramInt3 & 0xFFFFFFF8;
    }
    return paramInt1;
  }
  
  private int countStretchableSpaces(int paramInt1, int paramInt2)
  {
    int k;
    for (int i = 0; paramInt1 < paramInt2; i = k)
    {
      int j;
      if (mCharsValid) {
        j = mChars[paramInt1];
      } else {
        j = mText.charAt(mStart + paramInt1);
      }
      k = i;
      if (isStretchableWhitespace(j)) {
        k = i + 1;
      }
      paramInt1++;
    }
    return i;
  }
  
  private float drawRun(Canvas paramCanvas, int paramInt1, int paramInt2, boolean paramBoolean1, float paramFloat, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2)
  {
    int i = mDir;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    if (bool == paramBoolean1)
    {
      float f = -measureRun(paramInt1, paramInt2, paramInt2, paramBoolean1, null);
      handleRun(paramInt1, paramInt2, paramInt2, paramBoolean1, paramCanvas, paramFloat + f, paramInt3, paramInt4, paramInt5, null, false);
      return f;
    }
    return handleRun(paramInt1, paramInt2, paramInt2, paramBoolean1, paramCanvas, paramFloat, paramInt3, paramInt4, paramInt5, null, paramBoolean2);
  }
  
  private static void drawStroke(TextPaint paramTextPaint, Canvas paramCanvas, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    paramFloat1 = paramFloat5 + baselineShift + paramFloat1;
    int i = paramTextPaint.getColor();
    Paint.Style localStyle = paramTextPaint.getStyle();
    boolean bool = paramTextPaint.isAntiAlias();
    paramTextPaint.setStyle(Paint.Style.FILL);
    paramTextPaint.setAntiAlias(true);
    paramTextPaint.setColor(paramInt);
    paramCanvas.drawRect(paramFloat3, paramFloat1, paramFloat4, paramFloat1 + paramFloat2, paramTextPaint);
    paramTextPaint.setStyle(localStyle);
    paramTextPaint.setColor(i);
    paramTextPaint.setAntiAlias(bool);
  }
  
  private void drawTextRun(Canvas paramCanvas, TextPaint paramTextPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, float paramFloat, int paramInt5)
  {
    if (mCharsValid)
    {
      paramCanvas.drawTextRun(mChars, paramInt1, paramInt2 - paramInt1, paramInt3, paramInt4 - paramInt3, paramFloat, paramInt5, paramBoolean, paramTextPaint);
    }
    else
    {
      int i = mStart;
      paramCanvas.drawTextRun(mText, i + paramInt1, i + paramInt2, i + paramInt3, i + paramInt4, paramFloat, paramInt5, paramBoolean, paramTextPaint);
    }
  }
  
  private static void expandMetricsFromPaint(Paint.FontMetricsInt paramFontMetricsInt, TextPaint paramTextPaint)
  {
    int i = top;
    int j = ascent;
    int k = descent;
    int m = bottom;
    int n = leading;
    paramTextPaint.getFontMetricsInt(paramFontMetricsInt);
    updateMetrics(paramFontMetricsInt, i, j, k, m, n);
  }
  
  private void extractDecorationInfo(TextPaint paramTextPaint, DecorationInfo paramDecorationInfo)
  {
    isStrikeThruText = paramTextPaint.isStrikeThruText();
    if (isStrikeThruText) {
      paramTextPaint.setStrikeThruText(false);
    }
    isUnderlineText = paramTextPaint.isUnderlineText();
    if (isUnderlineText) {
      paramTextPaint.setUnderlineText(false);
    }
    underlineColor = underlineColor;
    underlineThickness = underlineThickness;
    paramTextPaint.setUnderlineText(0, 0.0F);
  }
  
  private int getOffsetBeforeAfter(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, int paramInt4, boolean paramBoolean2)
  {
    if (paramInt1 >= 0)
    {
      int i = 0;
      if (paramBoolean2) {
        paramInt1 = mLen;
      } else {
        paramInt1 = 0;
      }
      if (paramInt4 != paramInt1)
      {
        TextPaint localTextPaint = mWorkPaint;
        localTextPaint.set(mPaint);
        localTextPaint.setWordSpacing(mAddedWidth);
        paramInt1 = paramInt2;
        int j;
        int k;
        if (mSpanned == null)
        {
          paramInt2 = paramInt1;
          paramInt1 = paramInt3;
        }
        else
        {
          if (paramBoolean2) {
            j = paramInt4 + 1;
          } else {
            j = paramInt4;
          }
          k = mStart;
        }
        for (;;)
        {
          paramInt2 = mSpanned.nextSpanTransition(mStart + paramInt1, k + paramInt3, MetricAffectingSpan.class) - mStart;
          if (paramInt2 >= j)
          {
            MetricAffectingSpan[] arrayOfMetricAffectingSpan = (MetricAffectingSpan[])TextUtils.removeEmptySpans((MetricAffectingSpan[])mSpanned.getSpans(mStart + paramInt1, mStart + paramInt2, MetricAffectingSpan.class), mSpanned, MetricAffectingSpan.class);
            if (arrayOfMetricAffectingSpan.length > 0)
            {
              ReplacementSpan localReplacementSpan = null;
              for (paramInt3 = 0; paramInt3 < arrayOfMetricAffectingSpan.length; paramInt3++)
              {
                MetricAffectingSpan localMetricAffectingSpan = arrayOfMetricAffectingSpan[paramInt3];
                if ((localMetricAffectingSpan instanceof ReplacementSpan)) {
                  localReplacementSpan = (ReplacementSpan)localMetricAffectingSpan;
                } else {
                  localMetricAffectingSpan.updateMeasureState(localTextPaint);
                }
              }
              if (localReplacementSpan != null)
              {
                if (paramBoolean2) {
                  paramInt1 = paramInt2;
                }
                return paramInt1;
              }
            }
            paramInt3 = paramInt1;
            paramInt1 = paramInt2;
            paramInt2 = paramInt3;
            if (paramBoolean2) {}
            for (paramInt3 = i;; paramInt3 = 2) {
              break;
            }
            if (mCharsValid) {
              return localTextPaint.getTextRunCursor(mChars, paramInt2, paramInt1 - paramInt2, paramBoolean1, paramInt4, paramInt3);
            }
            return localTextPaint.getTextRunCursor(mText, mStart + paramInt2, mStart + paramInt1, paramBoolean1, mStart + paramInt4, paramInt3) - mStart;
          }
          paramInt1 = paramInt2;
        }
      }
    }
    if (paramBoolean2) {
      return TextUtils.getOffsetAfter(mText, mStart + paramInt4) - mStart;
    }
    return TextUtils.getOffsetBefore(mText, mStart + paramInt4) - mStart;
  }
  
  private float getRunAdvance(TextPaint paramTextPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
  {
    if (mCharsValid) {
      return paramTextPaint.getRunAdvance(mChars, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean, paramInt5);
    }
    int i = mStart;
    if (mComputed == null) {
      return paramTextPaint.getRunAdvance(mText, i + paramInt1, i + paramInt2, i + paramInt3, i + paramInt4, paramBoolean, i + paramInt5);
    }
    return mComputed.getWidth(paramInt1 + i, paramInt2 + i);
  }
  
  private float handleReplacement(ReplacementSpan paramReplacementSpan, TextPaint paramTextPaint, int paramInt1, int paramInt2, boolean paramBoolean1, Canvas paramCanvas, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint.FontMetricsInt paramFontMetricsInt, boolean paramBoolean2)
  {
    float f1 = 0.0F;
    int i = mStart + paramInt1;
    int j = mStart + paramInt2;
    float f2;
    if (!paramBoolean2)
    {
      f2 = f1;
      if (paramCanvas != null)
      {
        f2 = f1;
        if (paramBoolean1) {
          break label49;
        }
      }
    }
    for (;;)
    {
      break;
      label49:
      int k = 0;
      paramInt2 = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      if (paramFontMetricsInt != null) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      }
      if (paramInt1 != 0)
      {
        k = top;
        paramInt2 = ascent;
        m = descent;
        n = bottom;
        i1 = leading;
      }
      f1 = paramReplacementSpan.getSize(paramTextPaint, mText, i, j, paramFontMetricsInt);
      f2 = f1;
      if (paramInt1 != 0)
      {
        updateMetrics(paramFontMetricsInt, k, paramInt2, m, n, i1);
        f2 = f1;
      }
    }
    if (paramCanvas != null)
    {
      if (paramBoolean1) {
        paramFloat -= f2;
      }
      paramReplacementSpan.draw(paramCanvas, mText, i, j, paramFloat, paramInt3, paramInt4, paramInt5, paramTextPaint);
    }
    paramFloat = f2;
    if (paramBoolean1) {
      paramFloat = -paramFloat;
    }
    return paramFloat;
  }
  
  private float handleRun(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, Canvas paramCanvas, float paramFloat, int paramInt4, int paramInt5, int paramInt6, Paint.FontMetricsInt paramFontMetricsInt, boolean paramBoolean2)
  {
    if ((paramInt2 >= paramInt1) && (paramInt2 <= paramInt3))
    {
      if (paramInt1 == paramInt2)
      {
        paramCanvas = mWorkPaint;
        paramCanvas.set(mPaint);
        if (paramFontMetricsInt != null) {
          expandMetricsFromPaint(paramFontMetricsInt, paramCanvas);
        }
        return 0.0F;
      }
      int i;
      if (mSpanned == null)
      {
        i = 0;
      }
      else
      {
        mMetricAffectingSpanSpanSet.init(mSpanned, mStart + paramInt1, mStart + paramInt3);
        mCharacterStyleSpanSet.init(mSpanned, mStart + paramInt1, mStart + paramInt3);
        if ((mMetricAffectingSpanSpanSet.numberOfSpans == 0) && (mCharacterStyleSpanSet.numberOfSpans == 0)) {
          i = 0;
        } else {
          i = 1;
        }
      }
      Object localObject1;
      if (i == 0)
      {
        localObject1 = mWorkPaint;
        ((TextPaint)localObject1).set(mPaint);
        ((TextPaint)localObject1).setHyphenEdit(adjustHyphenEdit(paramInt1, paramInt3, ((TextPaint)localObject1).getHyphenEdit()));
        return handleText((TextPaint)localObject1, paramInt1, paramInt3, paramInt1, paramInt3, paramBoolean1, paramCanvas, paramFloat, paramInt4, paramInt5, paramInt6, paramFontMetricsInt, paramBoolean2, paramInt2, null);
      }
      float f = paramFloat;
      while (paramInt1 < paramInt2)
      {
        TextPaint localTextPaint = mWorkPaint;
        localTextPaint.set(mPaint);
        int j = mMetricAffectingSpanSpanSet.getNextTransition(mStart + paramInt1, mStart + paramInt3) - mStart;
        int k = Math.min(j, paramInt2);
        localObject1 = null;
        i = 0;
        Object localObject2;
        while (i < mMetricAffectingSpanSpanSet.numberOfSpans)
        {
          localObject2 = localObject1;
          if (mMetricAffectingSpanSpanSet.spanStarts[i] < mStart + k) {
            if (mMetricAffectingSpanSpanSet.spanEnds[i] <= mStart + paramInt1)
            {
              localObject2 = localObject1;
            }
            else
            {
              localObject2 = ((MetricAffectingSpan[])mMetricAffectingSpanSpanSet.spans)[i];
              if ((localObject2 instanceof ReplacementSpan))
              {
                localObject2 = (ReplacementSpan)localObject2;
              }
              else
              {
                ((MetricAffectingSpan)localObject2).updateDrawState(localTextPaint);
                localObject2 = localObject1;
              }
            }
          }
          i++;
          localObject1 = localObject2;
        }
        boolean bool;
        if (localObject1 != null)
        {
          if ((!paramBoolean2) && (k >= paramInt2)) {
            bool = false;
          } else {
            bool = true;
          }
          f += handleReplacement((ReplacementSpan)localObject1, localTextPaint, paramInt1, k, paramBoolean1, paramCanvas, f, paramInt4, paramInt5, paramInt6, paramFontMetricsInt, bool);
        }
        else
        {
          i = k;
          int m = j;
          localObject1 = localTextPaint;
          localObject2 = this;
          localTextPaint = mActivePaint;
          localTextPaint.set(mPaint);
          k = paramInt1;
          DecorationInfo localDecorationInfo = mDecorationInfo;
          mDecorations.clear();
          int n = i;
          int i1 = k;
          int i2 = i;
          int i3 = paramInt1;
          i = i1;
          paramInt1 = k;
          while (paramInt1 < i2)
          {
            i1 = mCharacterStyleSpanSet.getNextTransition(mStart + paramInt1, mStart + m) - mStart;
            int i4 = Math.min(i1, i2);
            ((TextPaint)localObject1).set(mPaint);
            for (k = 0; k < mCharacterStyleSpanSet.numberOfSpans; k++) {
              if ((mCharacterStyleSpanSet.spanStarts[k] < mStart + i4) && (mCharacterStyleSpanSet.spanEnds[k] > mStart + paramInt1)) {
                ((CharacterStyle[])mCharacterStyleSpanSet.spans)[k].updateDrawState((TextPaint)localObject1);
              }
            }
            ((TextLine)localObject2).extractDecorationInfo((TextPaint)localObject1, localDecorationInfo);
            if (paramInt1 == i3)
            {
              localTextPaint.set((TextPaint)localObject1);
            }
            else if (!((TextPaint)localObject1).hasEqualAttributes(localTextPaint))
            {
              localTextPaint.setHyphenEdit(((TextLine)localObject2).adjustHyphenEdit(i, n, mPaint.getHyphenEdit()));
              if ((!paramBoolean2) && (n >= paramInt2)) {
                bool = false;
              } else {
                bool = true;
              }
              f += ((TextLine)localObject2).handleText(localTextPaint, i, n, i3, m, paramBoolean1, paramCanvas, f, paramInt4, paramInt5, paramInt6, paramFontMetricsInt, bool, Math.min(n, i2), mDecorations);
              i = paramInt1;
              localObject2 = localObject1;
              localTextPaint.set((TextPaint)localObject2);
              localObject1 = this;
              mDecorations.clear();
              break label826;
            }
            Object localObject3 = localObject2;
            localObject2 = localObject1;
            localObject1 = localObject3;
            label826:
            k = i1;
            n = k;
            if (localDecorationInfo.hasDecoration())
            {
              localObject3 = localDecorationInfo.copyInfo();
              start = paramInt1;
              end = k;
              mDecorations.add(localObject3);
            }
            paramInt1 = k;
            localObject3 = localObject2;
            localObject2 = localObject1;
            localObject1 = localObject3;
          }
          localTextPaint.setHyphenEdit(((TextLine)localObject2).adjustHyphenEdit(i, n, mPaint.getHyphenEdit()));
          if ((!paramBoolean2) && (n >= paramInt2)) {
            bool = false;
          } else {
            bool = true;
          }
          f += ((TextLine)localObject2).handleText(localTextPaint, i, n, i3, m, paramBoolean1, paramCanvas, f, paramInt4, paramInt5, paramInt6, paramFontMetricsInt, bool, Math.min(n, i2), mDecorations);
        }
        paramInt1 = j;
      }
      return f - paramFloat;
    }
    paramCanvas = new StringBuilder();
    paramCanvas.append("measureLimit (");
    paramCanvas.append(paramInt2);
    paramCanvas.append(") is out of start (");
    paramCanvas.append(paramInt1);
    paramCanvas.append(") and limit (");
    paramCanvas.append(paramInt3);
    paramCanvas.append(") bounds");
    throw new IndexOutOfBoundsException(paramCanvas.toString());
  }
  
  private float handleText(TextPaint paramTextPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, Canvas paramCanvas, float paramFloat, int paramInt5, int paramInt6, int paramInt7, Paint.FontMetricsInt paramFontMetricsInt, boolean paramBoolean2, int paramInt8, ArrayList<DecorationInfo> paramArrayList)
  {
    int i = paramInt1;
    Object localObject = this;
    paramTextPaint.setWordSpacing(mAddedWidth);
    if (paramFontMetricsInt != null) {
      expandMetricsFromPaint(paramFontMetricsInt, paramTextPaint);
    }
    int j = paramInt2;
    if (j == i) {
      return 0.0F;
    }
    float f1 = 0.0F;
    int k = 0;
    int m;
    if (paramArrayList == null) {
      m = 0;
    } else {
      m = paramArrayList.size();
    }
    if ((!paramBoolean2) && ((paramCanvas == null) || ((bgColor == 0) && (m == 0) && (!paramBoolean1)))) {
      break label115;
    }
    f1 = ((TextLine)localObject).getRunAdvance(paramTextPaint, i, j, paramInt3, paramInt4, paramBoolean1, paramInt8);
    label115:
    if (paramCanvas != null)
    {
      float f2;
      if (paramBoolean1) {
        f2 = paramFloat - f1;
      }
      for (float f3 = paramFloat;; f3 = paramFloat + f1)
      {
        break;
        f2 = paramFloat;
      }
      if (bgColor != 0)
      {
        i = paramTextPaint.getColor();
        paramFontMetricsInt = paramTextPaint.getStyle();
        paramTextPaint.setColor(bgColor);
        paramTextPaint.setStyle(Paint.Style.FILL);
        paramCanvas.drawRect(f2, paramInt5, f3, paramInt7, paramTextPaint);
        paramTextPaint.setStyle(paramFontMetricsInt);
        paramTextPaint.setColor(i);
      }
      paramFloat = f1;
      i = m;
      i = paramInt6;
      if (m != 0)
      {
        paramInt7 = k;
        paramInt5 = paramInt6;
        paramInt6 = m;
        for (;;)
        {
          m = paramInt1;
          paramFontMetricsInt = this;
          k = paramInt2;
          paramFloat = f1;
          i = paramInt6;
          i = paramInt5;
          if (paramInt7 >= paramInt6) {
            break;
          }
          localObject = (DecorationInfo)paramArrayList.get(paramInt7);
          i = Math.max(start, m);
          j = Math.min(end, paramInt8);
          float f4 = paramFontMetricsInt.getRunAdvance(paramTextPaint, m, k, paramInt3, paramInt4, paramBoolean1, i);
          float f5 = paramFontMetricsInt.getRunAdvance(paramTextPaint, m, k, paramInt3, paramInt4, paramBoolean1, j);
          if (paramBoolean1) {
            paramFloat = f3 - f5;
          }
          for (f5 = f3 - f4;; f5 = f2 + f5)
          {
            break;
            paramFloat = f2 + f4;
          }
          if (underlineColor != 0) {
            drawStroke(paramTextPaint, paramCanvas, underlineColor, paramTextPaint.getUnderlinePosition(), underlineThickness, paramFloat, f5, paramInt5);
          }
          if (isUnderlineText)
          {
            f4 = Math.max(paramTextPaint.getUnderlineThickness(), 1.0F);
            drawStroke(paramTextPaint, paramCanvas, paramTextPaint.getColor(), paramTextPaint.getUnderlinePosition(), f4, paramFloat, f5, paramInt5);
          }
          if (isStrikeThruText)
          {
            f4 = Math.max(paramTextPaint.getStrikeThruThickness(), 1.0F);
            drawStroke(paramTextPaint, paramCanvas, paramTextPaint.getColor(), paramTextPaint.getStrikeThruPosition(), f4, paramFloat, f5, paramInt5);
          }
          paramInt7++;
        }
      }
      paramInt5 = baselineShift;
      drawTextRun(paramCanvas, paramTextPaint, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean1, f2, i + paramInt5);
    }
    else
    {
      paramFloat = f1;
    }
    if (paramBoolean1) {
      paramFloat = -paramFloat;
    }
    return paramFloat;
  }
  
  public static boolean isLineEndSpace(char paramChar)
  {
    boolean bool;
    if ((paramChar != ' ') && (paramChar != '\t') && (paramChar != ' ') && ((' ' > paramChar) || (paramChar > ' ') || (paramChar == ' ')) && (paramChar != ' ') && (paramChar != '　')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isStretchableWhitespace(int paramInt)
  {
    boolean bool;
    if (paramInt == 32) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private float measureRun(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, Paint.FontMetricsInt paramFontMetricsInt)
  {
    return handleRun(paramInt1, paramInt2, paramInt3, paramBoolean, null, 0.0F, 0, 0, 0, paramFontMetricsInt, true);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static TextLine obtain()
  {
    synchronized (sCached)
    {
      int i = sCached.length;
      int j;
      do
      {
        j = i - 1;
        if (j < 0) {
          break;
        }
        i = j;
      } while (sCached[j] == null);
      TextLine localTextLine = sCached[j];
      sCached[j] = null;
      return localTextLine;
      return new TextLine();
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public static TextLine recycle(TextLine paramTextLine)
  {
    mText = null;
    mPaint = null;
    mDirections = null;
    mSpanned = null;
    mTabs = null;
    mChars = null;
    mComputed = null;
    mMetricAffectingSpanSpanSet.recycle();
    mCharacterStyleSpanSet.recycle();
    mReplacementSpanSpanSet.recycle();
    TextLine[] arrayOfTextLine = sCached;
    int i = 0;
    try
    {
      while (i < sCached.length)
      {
        if (sCached[i] == null)
        {
          sCached[i] = paramTextLine;
          break;
        }
        i++;
      }
      return null;
    }
    finally {}
  }
  
  static void updateMetrics(Paint.FontMetricsInt paramFontMetricsInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    top = Math.min(top, paramInt1);
    ascent = Math.min(ascent, paramInt2);
    descent = Math.max(descent, paramInt3);
    bottom = Math.max(bottom, paramInt4);
    leading = Math.max(leading, paramInt5);
  }
  
  void draw(Canvas paramCanvas, float paramFloat, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!mHasTabs)
    {
      if (mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT)
      {
        drawRun(paramCanvas, 0, mLen, false, paramFloat, paramInt1, paramInt2, paramInt3, false);
        return;
      }
      if (mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT)
      {
        drawRun(paramCanvas, 0, mLen, true, paramFloat, paramInt1, paramInt2, paramInt3, false);
        return;
      }
    }
    int[] arrayOfInt = mDirections.mDirections;
    int i = arrayOfInt.length;
    float f1 = 0.0F;
    for (int j = 0; j < arrayOfInt.length; j += 2)
    {
      int k = arrayOfInt[j];
      int m = (arrayOfInt[(j + 1)] & 0x3FFFFFF) + k;
      int n = m;
      if (m > mLen) {
        n = mLen;
      }
      boolean bool1;
      if ((arrayOfInt[(j + 1)] & 0x4000000) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if (mHasTabs) {
        m = k;
      } else {
        m = n;
      }
      int i1 = k;
      int i2 = n;
      for (n = m; n <= i2; n = k + 1)
      {
        k = 0;
        m = k;
        if (mHasTabs)
        {
          m = k;
          if (n < i2)
          {
            k = mChars[n];
            m = k;
            if (k >= 55296)
            {
              m = k;
              if (k < 56320)
              {
                m = k;
                if (n + 1 < i2)
                {
                  k = Character.codePointAt(mChars, n);
                  m = k;
                  if (k > 65535)
                  {
                    k = n + 1;
                    break label321;
                  }
                }
              }
            }
          }
        }
        if (n != i2)
        {
          k = n;
          if (m != 9) {
            label321:
            continue;
          }
        }
        boolean bool2;
        if ((j == i - 2) && (n == mLen)) {
          bool2 = false;
        } else {
          bool2 = true;
        }
        float f2 = f1 + drawRun(paramCanvas, i1, n, bool1, paramFloat + f1, paramInt1, paramInt2, paramInt3, bool2);
        f1 = f2;
        if (m == 9) {
          f1 = mDir * nextTab(mDir * f2);
        }
        i1 = n + 1;
        k = n;
      }
    }
  }
  
  int getOffsetToLeftRightOf(int paramInt, boolean paramBoolean)
  {
    int i = mLen;
    boolean bool1;
    if (mDir == -1) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    int[] arrayOfInt1 = mDirections.mDirections;
    boolean bool2 = false;
    if (paramInt == 0) {}
    for (paramInt = -2;; paramInt = arrayOfInt1.length)
    {
      j = 0;
      k = -1;
      m = paramInt;
      paramInt = k;
      break label472;
      if (paramInt != i) {
        break;
      }
    }
    int n = i;
    int j = 0;
    int i1;
    for (int k = 0; k < arrayOfInt1.length; k += 2)
    {
      j = 0 + arrayOfInt1[k];
      if (paramInt >= j)
      {
        m = (arrayOfInt1[(k + 1)] & 0x3FFFFFF) + j;
        n = m;
        if (m > i) {
          n = i;
        }
        if (paramInt < n)
        {
          i1 = arrayOfInt1[(k + 1)] >>> 26 & 0x3F;
          if (paramInt == j)
          {
            int i2 = paramInt - 1;
            i3 = 0;
            m = j;
            while (i3 < arrayOfInt1.length)
            {
              int i4 = arrayOfInt1[i3] + 0;
              if (i2 >= i4)
              {
                int i5 = i4 + (arrayOfInt1[(i3 + 1)] & 0x3FFFFFF);
                j = i5;
                if (i5 > i) {
                  j = i;
                }
                if (i2 < j)
                {
                  i5 = arrayOfInt1[(i3 + 1)] >>> 26 & 0x3F;
                  if (i5 < i1)
                  {
                    k = i3;
                    m = i5;
                    i3 = i4;
                    n = j;
                    bool2 = true;
                    j = i3;
                    break label290;
                  }
                }
              }
              i3 += 2;
            }
            j = m;
            m = i1;
            label290:
            i3 = m;
            m = k;
            k = i3;
            break label342;
          }
          bool2 = false;
          m = k;
          k = i1;
          break label342;
        }
      }
    }
    int i3 = 0;
    int m = k;
    bool2 = false;
    k = i3;
    label342:
    boolean bool3;
    if (m != arrayOfInt1.length)
    {
      if ((k & 0x1) != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      boolean bool4;
      if (paramBoolean == bool3) {
        bool4 = true;
      } else {
        bool4 = false;
      }
      if (bool4) {
        i3 = n;
      } else {
        i3 = j;
      }
      if ((paramInt == i3) && (bool4 == bool2)) {
        break label466;
      }
      i3 = getOffsetBeforeAfter(m, j, n, bool3, paramInt, bool4);
      if (bool4) {
        paramInt = n;
      } else {
        paramInt = j;
      }
      if (i3 != paramInt) {
        return i3;
      }
      paramInt = i3;
      j = k;
      break label472;
    }
    label466:
    paramInt = -1;
    for (j = k;; j = i1)
    {
      label472:
      int[] arrayOfInt2 = arrayOfInt1;
      if (paramBoolean == bool1) {
        k = 1;
      } else {
        k = 0;
      }
      if (k != 0) {
        n = 2;
      } else {
        n = -2;
      }
      i3 = m + n;
      if ((i3 < 0) || (i3 >= arrayOfInt2.length)) {
        break label707;
      }
      m = 0 + arrayOfInt2[i3];
      n = m + (arrayOfInt2[(i3 + 1)] & 0x3FFFFFF);
      k = n;
      if (n > i) {
        k = i;
      }
      i1 = arrayOfInt2[(i3 + 1)] >>> 26 & 0x3F;
      if ((i1 & 0x1) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (paramBoolean == bool2) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      if (paramInt != -1) {
        break;
      }
      if (bool3) {
        paramInt = m;
      } else {
        paramInt = k;
      }
      paramInt = getOffsetBeforeAfter(i3, m, k, bool2, paramInt, bool3);
      if (!bool3) {
        k = m;
      }
      n = paramInt;
      if (paramInt != k) {
        break label759;
      }
      m = i3;
    }
    n = paramInt;
    if (i1 < j)
    {
      if (bool3) {
        paramInt = m;
      } else {
        paramInt = k;
      }
      n = paramInt;
      break label759;
      label707:
      n = -1;
      if (paramInt == -1)
      {
        paramInt = n;
        if (k != 0) {
          paramInt = mLen + 1;
        }
        n = paramInt;
      }
      else
      {
        n = paramInt;
        if (paramInt <= i)
        {
          if (k != 0) {
            paramInt = i;
          } else {
            paramInt = 0;
          }
          n = paramInt;
        }
      }
    }
    label759:
    return n;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void justify(float paramFloat)
  {
    for (int i = mLen; (i > 0) && (isLineEndSpace(mText.charAt(mStart + i - 1))); i--) {}
    int j = countStretchableSpaces(0, i);
    if (j == 0) {
      return;
    }
    mAddedWidth = ((paramFloat - Math.abs(measure(i, false, null))) / j);
  }
  
  float measure(int paramInt, boolean paramBoolean, Paint.FontMetricsInt paramFontMetricsInt)
  {
    int i;
    if (paramBoolean) {
      i = paramInt - 1;
    } else {
      i = paramInt;
    }
    if (i < 0) {
      return 0.0F;
    }
    float f1 = 0.0F;
    if (!mHasTabs)
    {
      if (mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
        return measureRun(0, paramInt, mLen, false, paramFontMetricsInt);
      }
      if (mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
        return measureRun(0, paramInt, mLen, true, paramFontMetricsInt);
      }
    }
    char[] arrayOfChar = mChars;
    int[] arrayOfInt = mDirections.mDirections;
    for (int j = 0; j < arrayOfInt.length; j += 2)
    {
      int k = arrayOfInt[j];
      int m = (arrayOfInt[(j + 1)] & 0x3FFFFFF) + k;
      int n = m;
      if (m > mLen) {
        n = mLen;
      }
      if ((arrayOfInt[(j + 1)] & 0x4000000) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      if (mHasTabs) {
        m = k;
      } else {
        m = n;
      }
      int i1 = k;
      int i2 = n;
      int i3;
      for (n = m;; n = i3 + 1)
      {
        k = n;
        if (k > i2) {
          break;
        }
        i3 = 0;
        m = i3;
        if (mHasTabs)
        {
          m = i3;
          if (k < i2)
          {
            i3 = arrayOfChar[k];
            m = i3;
            if (i3 >= 55296)
            {
              m = i3;
              if (i3 < 56320)
              {
                m = i3;
                if (k + 1 < i2)
                {
                  i3 = Character.codePointAt(arrayOfChar, k);
                  m = i3;
                  if (i3 > 65535)
                  {
                    i3 = k + 1;
                    break label328;
                  }
                }
              }
            }
          }
        }
        if (k != i2)
        {
          i3 = k;
          if (m != 9) {
            label328:
            continue;
          }
        }
        if ((i >= i1) && (i < k)) {
          i3 = 1;
        } else {
          i3 = 0;
        }
        if (mDir == -1) {
          bool = true;
        } else {
          bool = false;
        }
        int i4;
        if (bool == paramBoolean) {
          i4 = 1;
        } else {
          i4 = 0;
        }
        if ((i3 != 0) && (i4 != 0)) {
          return f1 + measureRun(i1, paramInt, k, paramBoolean, paramFontMetricsInt);
        }
        boolean bool = paramBoolean;
        float f2 = measureRun(i1, k, k, bool, paramFontMetricsInt);
        if (i4 == 0) {
          f2 = -f2;
        }
        f1 += f2;
        if (i3 != 0) {
          return f1 + measureRun(i1, paramInt, k, bool, null);
        }
        if (m == 9)
        {
          if (paramInt == k) {
            return f1;
          }
          f1 = mDir * nextTab(mDir * f1);
          if (i == k) {
            return f1;
          }
        }
        i1 = n + 1;
        i3 = n;
      }
    }
    return f1;
  }
  
  float[] measureAllOffsets(boolean[] paramArrayOfBoolean, Paint.FontMetricsInt paramFontMetricsInt)
  {
    int i = mLen;
    boolean bool1 = true;
    float[] arrayOfFloat = new float[i + 1];
    int[] arrayOfInt1 = new int[mLen + 1];
    int j = 0;
    int k = 0;
    int m;
    for (i = 0; i < arrayOfInt1.length; i++)
    {
      if (paramArrayOfBoolean[i] != 0) {
        m = i - 1;
      } else {
        m = i;
      }
      arrayOfInt1[i] = m;
    }
    if (arrayOfInt1[0] < 0) {
      arrayOfFloat[0] = 0.0F;
    }
    float f1 = 0.0F;
    if (!mHasTabs)
    {
      if (mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT)
      {
        for (i = k; i <= mLen; i++) {
          arrayOfFloat[i] = measureRun(0, i, mLen, false, paramFontMetricsInt);
        }
        return arrayOfFloat;
      }
      if (mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT)
      {
        for (i = j; i <= mLen; i++) {
          arrayOfFloat[i] = measureRun(0, i, mLen, true, paramFontMetricsInt);
        }
        return arrayOfFloat;
      }
    }
    char[] arrayOfChar = mChars;
    int[] arrayOfInt2 = mDirections.mDirections;
    for (int n = 0; n < arrayOfInt2.length; n += 2)
    {
      m = arrayOfInt2[n];
      k = (arrayOfInt2[(n + 1)] & 0x3FFFFFF) + m;
      i = k;
      if (k > mLen) {
        i = mLen;
      }
      boolean bool2;
      if ((arrayOfInt2[(n + 1)] & 0x4000000) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      if (mHasTabs) {
        k = m;
      } else {
        k = i;
      }
      int i1 = i;
      i = k;
      while (i <= i1)
      {
        j = 0;
        k = j;
        if (mHasTabs)
        {
          k = j;
          if (i < i1)
          {
            j = arrayOfChar[i];
            k = j;
            if (j >= 55296)
            {
              k = j;
              if (j < 56320)
              {
                k = j;
                if (i + 1 < i1)
                {
                  j = Character.codePointAt(arrayOfChar, i);
                  k = j;
                  if (j > 65535)
                  {
                    j = i + 1;
                    break label415;
                  }
                }
              }
            }
          }
        }
        if (i != i1)
        {
          j = i;
          if (k != 9) {
            label415:
            break label666;
          }
        }
        boolean bool3;
        if (mDir == -1) {
          bool3 = bool1;
        } else {
          bool3 = false;
        }
        if (bool3 != bool2) {
          bool1 = false;
        }
        int i2 = i;
        float f2 = measureRun(m, i, i, bool2, paramFontMetricsInt);
        if (bool1) {
          f3 = f2;
        } else {
          f3 = -f2;
        }
        float f3 = f1 + f3;
        if (!bool1) {
          f1 = f3;
        }
        if (bool1) {
          paramArrayOfBoolean = paramFontMetricsInt;
        } else {
          paramArrayOfBoolean = null;
        }
        j = m;
        i = i2;
        while ((j <= i) && (j <= mLen))
        {
          if ((arrayOfInt1[j] >= m) && (arrayOfInt1[j] < i)) {
            arrayOfFloat[j] = (f1 + measureRun(m, j, i, bool2, paramArrayOfBoolean));
          }
          j++;
        }
        if (k == 9)
        {
          m = i;
          if (arrayOfInt1[m] == m) {
            arrayOfFloat[m] = f3;
          }
          f1 = mDir * nextTab(mDir * f3);
          if (arrayOfInt1[(m + 1)] == m) {
            arrayOfFloat[(m + 1)] = f1;
          }
        }
        else
        {
          f1 = f3;
        }
        m = i + 1;
        j = i;
        label666:
        i = j + 1;
        bool1 = true;
      }
    }
    if (arrayOfInt1[mLen] == mLen) {
      arrayOfFloat[mLen] = f1;
    }
    return arrayOfFloat;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public float metrics(Paint.FontMetricsInt paramFontMetricsInt)
  {
    return measure(mLen, false, paramFontMetricsInt);
  }
  
  float nextTab(float paramFloat)
  {
    if (mTabs != null) {
      return mTabs.nextTab(paramFloat);
    }
    return Layout.TabStops.nextDefaultStop(paramFloat, 20);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void set(TextPaint paramTextPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, Layout.Directions paramDirections, boolean paramBoolean, Layout.TabStops paramTabStops)
  {
    mPaint = paramTextPaint;
    mText = paramCharSequence;
    mStart = paramInt1;
    mLen = (paramInt2 - paramInt1);
    mDir = paramInt3;
    mDirections = paramDirections;
    if (mDirections != null)
    {
      mHasTabs = paramBoolean;
      mSpanned = null;
      paramInt3 = 0;
      if ((paramCharSequence instanceof Spanned))
      {
        mSpanned = ((Spanned)paramCharSequence);
        mReplacementSpanSpanSet.init(mSpanned, paramInt1, paramInt2);
        if (mReplacementSpanSpanSet.numberOfSpans > 0) {
          paramInt3 = 1;
        } else {
          paramInt3 = 0;
        }
      }
      mComputed = null;
      if ((paramCharSequence instanceof PrecomputedText))
      {
        mComputed = ((PrecomputedText)paramCharSequence);
        if (!mComputed.getParams().getTextPaint().equalsForTextMeasurement(paramTextPaint)) {
          mComputed = null;
        }
      }
      if ((paramInt3 == 0) && (!paramBoolean) && (paramDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT)) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      }
      mCharsValid = paramBoolean;
      if (mCharsValid)
      {
        if ((mChars == null) || (mChars.length < mLen)) {
          mChars = ArrayUtils.newUnpaddedCharArray(mLen);
        }
        TextUtils.getChars(paramCharSequence, paramInt1, paramInt2, mChars, 0);
        if (paramInt3 != 0)
        {
          paramTextPaint = mChars;
          int i;
          for (paramInt3 = paramInt1; paramInt3 < paramInt2; paramInt3 = i)
          {
            i = mReplacementSpanSpanSet.getNextTransition(paramInt3, paramInt2);
            if (mReplacementSpanSpanSet.hasSpansIntersecting(paramInt3, i))
            {
              paramTextPaint[(paramInt3 - paramInt1)] = ((char)65532);
              for (paramInt3 = paramInt3 - paramInt1 + 1; paramInt3 < i - paramInt1; paramInt3++) {
                paramTextPaint[paramInt3] = ((char)65279);
              }
            }
          }
        }
      }
      mTabs = paramTabStops;
      mAddedWidth = 0.0F;
      return;
    }
    throw new IllegalArgumentException("Directions cannot be null");
  }
  
  private static final class DecorationInfo
  {
    public int end = -1;
    public boolean isStrikeThruText;
    public boolean isUnderlineText;
    public int start = -1;
    public int underlineColor;
    public float underlineThickness;
    
    private DecorationInfo() {}
    
    public DecorationInfo copyInfo()
    {
      DecorationInfo localDecorationInfo = new DecorationInfo();
      isStrikeThruText = isStrikeThruText;
      isUnderlineText = isUnderlineText;
      underlineColor = underlineColor;
      underlineThickness = underlineThickness;
      return localDecorationInfo;
    }
    
    public boolean hasDecoration()
    {
      boolean bool;
      if ((!isStrikeThruText) && (!isUnderlineText) && (underlineColor == 0)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
  }
}
