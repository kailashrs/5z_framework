package android.text;

import android.graphics.Rect;
import android.text.style.MetricAffectingSpan;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Objects;

public class PrecomputedText
  implements Spannable
{
  private static final char LINE_FEED = '\n';
  private final int mEnd;
  private final ParagraphInfo[] mParagraphInfo;
  private final Params mParams;
  private final int mStart;
  private final SpannableString mText;
  
  private PrecomputedText(CharSequence paramCharSequence, int paramInt1, int paramInt2, Params paramParams, ParagraphInfo[] paramArrayOfParagraphInfo)
  {
    mText = new SpannableString(paramCharSequence, true);
    mStart = paramInt1;
    mEnd = paramInt2;
    mParams = paramParams;
    mParagraphInfo = paramArrayOfParagraphInfo;
  }
  
  public static PrecomputedText create(CharSequence paramCharSequence, Params paramParams)
  {
    ParagraphInfo[] arrayOfParagraphInfo = createMeasuredParagraphs(paramCharSequence, paramParams, 0, paramCharSequence.length(), true);
    return new PrecomputedText(paramCharSequence, 0, paramCharSequence.length(), paramParams, arrayOfParagraphInfo);
  }
  
  public static ParagraphInfo[] createMeasuredParagraphs(CharSequence paramCharSequence, Params paramParams, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    Preconditions.checkNotNull(paramCharSequence);
    Preconditions.checkNotNull(paramParams);
    boolean bool;
    if ((paramParams.getBreakStrategy() != 0) && (paramParams.getHyphenationFrequency() != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    for (int i = paramInt1; i < paramInt2; i = paramInt1)
    {
      paramInt1 = TextUtils.indexOf(paramCharSequence, '\n', i, paramInt2);
      if (paramInt1 < 0) {}
      for (paramInt1 = paramInt2;; paramInt1++) {
        break;
      }
      localArrayList.add(new ParagraphInfo(paramInt1, MeasuredParagraph.buildForStaticLayout(paramParams.getTextPaint(), paramCharSequence, i, paramInt1, paramParams.getTextDirection(), bool, paramBoolean, null)));
    }
    return (ParagraphInfo[])localArrayList.toArray(new ParagraphInfo[localArrayList.size()]);
  }
  
  public boolean canUseMeasuredResult(int paramInt1, int paramInt2, TextDirectionHeuristic paramTextDirectionHeuristic, TextPaint paramTextPaint, int paramInt3, int paramInt4)
  {
    mParams.getTextPaint();
    boolean bool;
    if ((mStart == paramInt1) && (mEnd == paramInt2) && (mParams.isSameTextMetricsInternal(paramTextPaint, paramTextDirectionHeuristic, paramInt3, paramInt4))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public char charAt(int paramInt)
  {
    return mText.charAt(paramInt);
  }
  
  public int findParaIndex(int paramInt)
  {
    for (int i = 0; i < mParagraphInfo.length; i++) {
      if (paramInt < mParagraphInfo[i].paragraphEnd) {
        return i;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("pos must be less than ");
    localStringBuilder.append(mParagraphInfo[(mParagraphInfo.length - 1)].paragraphEnd);
    localStringBuilder.append(", gave ");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public void getBounds(int paramInt1, int paramInt2, Rect paramRect)
  {
    boolean bool1 = true;
    boolean bool2;
    if ((paramInt1 >= 0) && (paramInt1 <= mText.length())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "invalid start offset");
    if ((paramInt2 >= 0) && (paramInt2 <= mText.length())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "invalid end offset");
    if (paramInt1 <= paramInt2) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "start offset can not be larger than end offset");
    Preconditions.checkNotNull(paramRect);
    if (paramInt1 == paramInt2)
    {
      paramRect.set(0, 0, 0, 0);
      return;
    }
    int i = findParaIndex(paramInt1);
    int j = getParagraphStart(i);
    int k = getParagraphEnd(i);
    if ((paramInt1 >= j) && (k >= paramInt2))
    {
      getMeasuredParagraph(i).getBounds(paramInt1 - j, paramInt2 - j, paramRect);
      return;
    }
    paramRect = new StringBuilder();
    paramRect.append("Cannot measured across the paragraph:para: (");
    paramRect.append(j);
    paramRect.append(", ");
    paramRect.append(k);
    paramRect.append("), request: (");
    paramRect.append(paramInt1);
    paramRect.append(", ");
    paramRect.append(paramInt2);
    paramRect.append(")");
    throw new IllegalArgumentException(paramRect.toString());
  }
  
  public int getEnd()
  {
    return mEnd;
  }
  
  public MeasuredParagraph getMeasuredParagraph(int paramInt)
  {
    return mParagraphInfo[paramInt].measured;
  }
  
  public int getMemoryUsage()
  {
    int i = 0;
    for (int j = 0; j < getParagraphCount(); j++) {
      i += getMeasuredParagraph(j).getMemoryUsage();
    }
    return i;
  }
  
  public int getParagraphCount()
  {
    return mParagraphInfo.length;
  }
  
  public int getParagraphEnd(int paramInt)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, getParagraphCount(), "paraIndex");
    return mParagraphInfo[paramInt].paragraphEnd;
  }
  
  public ParagraphInfo[] getParagraphInfo()
  {
    return mParagraphInfo;
  }
  
  public int getParagraphStart(int paramInt)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, getParagraphCount(), "paraIndex");
    if (paramInt == 0) {
      paramInt = mStart;
    } else {
      paramInt = getParagraphEnd(paramInt - 1);
    }
    return paramInt;
  }
  
  public Params getParams()
  {
    return mParams;
  }
  
  public int getSpanEnd(Object paramObject)
  {
    return mText.getSpanEnd(paramObject);
  }
  
  public int getSpanFlags(Object paramObject)
  {
    return mText.getSpanFlags(paramObject);
  }
  
  public int getSpanStart(Object paramObject)
  {
    return mText.getSpanStart(paramObject);
  }
  
  public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass)
  {
    return mText.getSpans(paramInt1, paramInt2, paramClass);
  }
  
  public int getStart()
  {
    return mStart;
  }
  
  public CharSequence getText()
  {
    return mText;
  }
  
  public float getWidth(int paramInt1, int paramInt2)
  {
    boolean bool1 = false;
    if ((paramInt1 >= 0) && (paramInt1 <= mText.length())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "invalid start offset");
    if ((paramInt2 >= 0) && (paramInt2 <= mText.length())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "invalid end offset");
    boolean bool2 = bool1;
    if (paramInt1 <= paramInt2) {
      bool2 = true;
    }
    Preconditions.checkArgument(bool2, "start offset can not be larger than end offset");
    if (paramInt1 == paramInt2) {
      return 0.0F;
    }
    int i = findParaIndex(paramInt1);
    int j = getParagraphStart(i);
    int k = getParagraphEnd(i);
    if ((paramInt1 >= j) && (k >= paramInt2)) {
      return getMeasuredParagraph(i).getWidth(paramInt1 - j, paramInt2 - j);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cannot measured across the paragraph:para: (");
    localStringBuilder.append(j);
    localStringBuilder.append(", ");
    localStringBuilder.append(k);
    localStringBuilder.append("), request: (");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(", ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(")");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int length()
  {
    return mText.length();
  }
  
  public int nextSpanTransition(int paramInt1, int paramInt2, Class paramClass)
  {
    return mText.nextSpanTransition(paramInt1, paramInt2, paramClass);
  }
  
  public void removeSpan(Object paramObject)
  {
    if (!(paramObject instanceof MetricAffectingSpan))
    {
      mText.removeSpan(paramObject);
      return;
    }
    throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
  }
  
  public void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!(paramObject instanceof MetricAffectingSpan))
    {
      mText.setSpan(paramObject, paramInt1, paramInt2, paramInt3);
      return;
    }
    throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2)
  {
    return create(mText.subSequence(paramInt1, paramInt2), mParams);
  }
  
  public String toString()
  {
    return mText.toString();
  }
  
  public static class ParagraphInfo
  {
    public final MeasuredParagraph measured;
    public final int paragraphEnd;
    
    public ParagraphInfo(int paramInt, MeasuredParagraph paramMeasuredParagraph)
    {
      paragraphEnd = paramInt;
      measured = paramMeasuredParagraph;
    }
  }
  
  public static final class Params
  {
    private final int mBreakStrategy;
    private final int mHyphenationFrequency;
    private final TextPaint mPaint;
    private final TextDirectionHeuristic mTextDir;
    
    public Params(TextPaint paramTextPaint, TextDirectionHeuristic paramTextDirectionHeuristic, int paramInt1, int paramInt2)
    {
      mPaint = paramTextPaint;
      mTextDir = paramTextDirectionHeuristic;
      mBreakStrategy = paramInt1;
      mHyphenationFrequency = paramInt2;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if ((paramObject != null) && ((paramObject instanceof Params)))
      {
        paramObject = (Params)paramObject;
        return isSameTextMetricsInternal(mPaint, mTextDir, mBreakStrategy, mHyphenationFrequency);
      }
      return false;
    }
    
    public int getBreakStrategy()
    {
      return mBreakStrategy;
    }
    
    public int getHyphenationFrequency()
    {
      return mHyphenationFrequency;
    }
    
    public TextDirectionHeuristic getTextDirection()
    {
      return mTextDir;
    }
    
    public TextPaint getTextPaint()
    {
      return mPaint;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Float.valueOf(mPaint.getTextSize()), Float.valueOf(mPaint.getTextScaleX()), Float.valueOf(mPaint.getTextSkewX()), Float.valueOf(mPaint.getLetterSpacing()), Float.valueOf(mPaint.getWordSpacing()), Integer.valueOf(mPaint.getFlags()), mPaint.getTextLocales(), mPaint.getTypeface(), mPaint.getFontVariationSettings(), Boolean.valueOf(mPaint.isElegantTextHeight()), mTextDir, Integer.valueOf(mBreakStrategy), Integer.valueOf(mHyphenationFrequency) });
    }
    
    public boolean isSameTextMetricsInternal(TextPaint paramTextPaint, TextDirectionHeuristic paramTextDirectionHeuristic, int paramInt1, int paramInt2)
    {
      boolean bool;
      if ((mTextDir == paramTextDirectionHeuristic) && (mBreakStrategy == paramInt1) && (mHyphenationFrequency == paramInt2) && (mPaint.equalsForTextMeasurement(paramTextPaint))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{textSize=");
      localStringBuilder.append(mPaint.getTextSize());
      localStringBuilder.append(", textScaleX=");
      localStringBuilder.append(mPaint.getTextScaleX());
      localStringBuilder.append(", textSkewX=");
      localStringBuilder.append(mPaint.getTextSkewX());
      localStringBuilder.append(", letterSpacing=");
      localStringBuilder.append(mPaint.getLetterSpacing());
      localStringBuilder.append(", textLocale=");
      localStringBuilder.append(mPaint.getTextLocales());
      localStringBuilder.append(", typeface=");
      localStringBuilder.append(mPaint.getTypeface());
      localStringBuilder.append(", variationSettings=");
      localStringBuilder.append(mPaint.getFontVariationSettings());
      localStringBuilder.append(", elegantTextHeight=");
      localStringBuilder.append(mPaint.isElegantTextHeight());
      localStringBuilder.append(", textDir=");
      localStringBuilder.append(mTextDir);
      localStringBuilder.append(", breakStrategy=");
      localStringBuilder.append(mBreakStrategy);
      localStringBuilder.append(", hyphenationFrequency=");
      localStringBuilder.append(mHyphenationFrequency);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public static class Builder
    {
      private int mBreakStrategy = 1;
      private int mHyphenationFrequency = 1;
      private final TextPaint mPaint;
      private TextDirectionHeuristic mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
      
      public Builder(TextPaint paramTextPaint)
      {
        mPaint = paramTextPaint;
      }
      
      public PrecomputedText.Params build()
      {
        return new PrecomputedText.Params(mPaint, mTextDir, mBreakStrategy, mHyphenationFrequency);
      }
      
      public Builder setBreakStrategy(int paramInt)
      {
        mBreakStrategy = paramInt;
        return this;
      }
      
      public Builder setHyphenationFrequency(int paramInt)
      {
        mHyphenationFrequency = paramInt;
        return this;
      }
      
      public Builder setTextDirection(TextDirectionHeuristic paramTextDirectionHeuristic)
      {
        mTextDir = paramTextDirectionHeuristic;
        return this;
      }
    }
  }
}
