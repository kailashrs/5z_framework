package android.text;

import android.graphics.Paint.FontMetricsInt;
import android.text.style.LineHeightSpan;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.util.Pools.SynchronizedPool;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;

public class StaticLayout
  extends Layout
{
  private static final char CHAR_NEW_LINE = '\n';
  private static final int COLUMNS_ELLIPSIZE = 7;
  private static final int COLUMNS_NORMAL = 5;
  private static final int DEFAULT_MAX_LINE_HEIGHT = -1;
  private static final int DESCENT = 2;
  private static final int DIR = 0;
  private static final int DIR_SHIFT = 30;
  private static final int ELLIPSIS_COUNT = 6;
  private static final int ELLIPSIS_START = 5;
  private static final int EXTRA = 3;
  private static final double EXTRA_ROUNDING = 0.5D;
  private static final int HYPHEN = 4;
  private static final int HYPHEN_MASK = 255;
  private static final int START = 0;
  private static final int START_MASK = 536870911;
  private static final int TAB = 0;
  private static final int TAB_INCREMENT = 20;
  private static final int TAB_MASK = 536870912;
  static final String TAG = "StaticLayout";
  private static final int TOP = 1;
  private int mBottomPadding;
  private int mColumns;
  private boolean mEllipsized;
  private int mEllipsizedWidth;
  private int[] mLeftIndents;
  private int[] mLeftPaddings;
  private int mLineCount;
  private Layout.Directions[] mLineDirections;
  private int[] mLines;
  private int mMaxLineHeight = -1;
  private int mMaximumVisibleLineCount = Integer.MAX_VALUE;
  private int[] mRightIndents;
  private int[] mRightPaddings;
  private int mTopPadding;
  
  private StaticLayout(Builder paramBuilder)
  {
    super((CharSequence)localObject, mPaint, mWidth, mAlignment, mTextDir, mSpacingMult, mSpacingAdd);
    if (mEllipsize != null)
    {
      localObject = (Layout.Ellipsizer)getText();
      mLayout = this;
      mWidth = mEllipsizedWidth;
      mMethod = mEllipsize;
      mEllipsizedWidth = mEllipsizedWidth;
      mColumns = 7;
    }
    else
    {
      mColumns = 5;
      mEllipsizedWidth = mWidth;
    }
    mLineDirections = ((Layout.Directions[])ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2));
    mLines = ArrayUtils.newUnpaddedIntArray(2 * mColumns);
    mMaximumVisibleLineCount = mMaxLines;
    mLeftIndents = mLeftIndents;
    mRightIndents = mRightIndents;
    mLeftPaddings = mLeftPaddings;
    mRightPaddings = mRightPaddings;
    setJustificationMode(mJustificationMode);
    generate(paramBuilder, mIncludePad, mIncludePad);
  }
  
  StaticLayout(CharSequence paramCharSequence)
  {
    super(paramCharSequence, null, 0, null, 0.0F, 0.0F);
    mColumns = 7;
    mLineDirections = ((Layout.Directions[])ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2));
    mLines = ArrayUtils.newUnpaddedIntArray(2 * mColumns);
  }
  
  @Deprecated
  public StaticLayout(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextPaint paramTextPaint, int paramInt3, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this(paramCharSequence, paramInt1, paramInt2, paramTextPaint, paramInt3, paramAlignment, paramFloat1, paramFloat2, paramBoolean, null, 0);
  }
  
  @Deprecated
  public StaticLayout(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextPaint paramTextPaint, int paramInt3, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt4)
  {
    this(paramCharSequence, paramInt1, paramInt2, paramTextPaint, paramInt3, paramAlignment, TextDirectionHeuristics.FIRSTSTRONG_LTR, paramFloat1, paramFloat2, paramBoolean, paramTruncateAt, paramInt4, Integer.MAX_VALUE);
  }
  
  @Deprecated
  public StaticLayout(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextPaint paramTextPaint, int paramInt3, Layout.Alignment paramAlignment, TextDirectionHeuristic paramTextDirectionHeuristic, float paramFloat1, float paramFloat2, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt4, int paramInt5)
  {
    super((CharSequence)localObject, paramTextPaint, paramInt3, paramAlignment, paramTextDirectionHeuristic, paramFloat1, paramFloat2);
    paramCharSequence = Builder.obtain(paramCharSequence, paramInt1, paramInt2, paramTextPaint, paramInt3).setAlignment(paramAlignment).setTextDirection(paramTextDirectionHeuristic).setLineSpacing(paramFloat2, paramFloat1).setIncludePad(paramBoolean).setEllipsizedWidth(paramInt4).setEllipsize(paramTruncateAt).setMaxLines(paramInt5);
    if (paramTruncateAt != null)
    {
      paramTextPaint = (Layout.Ellipsizer)getText();
      mLayout = this;
      mWidth = paramInt4;
      mMethod = paramTruncateAt;
      mEllipsizedWidth = paramInt4;
      mColumns = 7;
    }
    else
    {
      mColumns = 5;
      mEllipsizedWidth = paramInt3;
    }
    mLineDirections = ((Layout.Directions[])ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2));
    mLines = ArrayUtils.newUnpaddedIntArray(2 * mColumns);
    mMaximumVisibleLineCount = paramInt5;
    generate(paramCharSequence, mIncludePad, mIncludePad);
    Builder.recycle(paramCharSequence);
  }
  
  @Deprecated
  public StaticLayout(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this(paramCharSequence, 0, paramCharSequence.length(), paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2, paramBoolean);
  }
  
  private void calculateEllipsis(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, float paramFloat1, TextUtils.TruncateAt paramTruncateAt, int paramInt4, float paramFloat2, TextPaint paramTextPaint, boolean paramBoolean)
  {
    float f1 = paramFloat1 - getTotalInsets(paramInt4);
    if ((paramFloat2 <= f1) && (!paramBoolean))
    {
      mLines[(mColumns * paramInt4 + 5)] = 0;
      mLines[(mColumns * paramInt4 + 6)] = 0;
      return;
    }
    float f2 = paramTextPaint.measureText(TextUtils.getEllipsisString(paramTruncateAt));
    int i = 0;
    int j = 0;
    int k = paramInt2 - paramInt1;
    if (paramTruncateAt == TextUtils.TruncateAt.START)
    {
      if (mMaximumVisibleLineCount == 1)
      {
        paramFloat1 = 0.0F;
        for (paramInt2 = k;; paramInt2--)
        {
          j = paramInt2;
          if (paramInt2 <= 0) {
            break;
          }
          paramFloat2 = paramArrayOfFloat[(paramInt2 - 1 + paramInt1 - paramInt3)];
          if (paramFloat2 + paramFloat1 + f2 > f1) {
            for (;;)
            {
              j = paramInt2;
              if (paramInt2 >= k) {
                break;
              }
              j = paramInt2;
              if (paramArrayOfFloat[(paramInt2 + paramInt1 - paramInt3)] != 0.0F) {
                break;
              }
              paramInt2++;
            }
          }
          paramFloat1 += paramFloat2;
        }
        paramInt1 = 0;
        paramInt2 = j;
      }
      else
      {
        paramInt1 = i;
        paramInt2 = j;
        if (Log.isLoggable("StaticLayout", 5))
        {
          Log.w("StaticLayout", "Start Ellipsis only supported with one line");
          paramInt1 = i;
          paramInt2 = j;
        }
      }
    }
    else if ((paramTruncateAt != TextUtils.TruncateAt.END) && (paramTruncateAt != TextUtils.TruncateAt.MARQUEE) && (paramTruncateAt != TextUtils.TruncateAt.END_SMALL))
    {
      if (mMaximumVisibleLineCount == 1)
      {
        paramFloat1 = 0.0F;
        paramInt2 = k;
        paramFloat2 = (f1 - f2) / 2.0F;
        float f3;
        while (paramInt2 > 0)
        {
          f3 = paramArrayOfFloat[(paramInt2 - 1 + paramInt1 - paramInt3)];
          if (f3 + paramFloat1 > paramFloat2) {
            for (;;)
            {
              j = paramInt2;
              if (paramInt2 >= k) {
                break;
              }
              j = paramInt2;
              if (paramArrayOfFloat[(paramInt2 + paramInt1 - paramInt3)] != 0.0F) {
                break;
              }
              paramInt2++;
            }
          }
          paramFloat1 += f3;
          paramInt2--;
        }
        j = paramInt2;
        paramFloat2 = 0.0F;
        for (paramInt2 = 0; paramInt2 < j; paramInt2++)
        {
          f3 = paramArrayOfFloat[(paramInt2 + paramInt1 - paramInt3)];
          if (f3 + paramFloat2 > f1 - f2 - paramFloat1) {
            break;
          }
          paramFloat2 += f3;
        }
        paramInt1 = paramInt2;
        paramInt2 = j - paramInt2;
      }
      else
      {
        paramInt1 = i;
        paramInt2 = j;
        if (Log.isLoggable("StaticLayout", 5))
        {
          Log.w("StaticLayout", "Middle Ellipsis only supported with one line");
          paramInt1 = i;
          paramInt2 = j;
        }
      }
    }
    else
    {
      paramFloat1 = 0.0F;
      for (paramInt2 = 0; paramInt2 < k; paramInt2++)
      {
        paramFloat2 = paramArrayOfFloat[(paramInt2 + paramInt1 - paramInt3)];
        if (paramFloat2 + paramFloat1 + f2 > f1) {
          break;
        }
        paramFloat1 += paramFloat2;
      }
      paramInt1 = paramInt2;
      paramInt2 = k - paramInt2;
      if ((paramBoolean) && (paramInt2 == 0) && (k > 0))
      {
        paramInt1 = k - 1;
        paramInt2 = 1;
      }
    }
    mEllipsized = true;
    mLines[(mColumns * paramInt4 + 5)] = paramInt1;
    mLines[(mColumns * paramInt4 + 6)] = paramInt2;
  }
  
  private float getTotalInsets(int paramInt)
  {
    int i = 0;
    if (mLeftIndents != null) {
      i = mLeftIndents[Math.min(paramInt, mLeftIndents.length - 1)];
    }
    int j = i;
    if (mRightIndents != null) {
      j = i + mRightIndents[Math.min(paramInt, mRightIndents.length - 1)];
    }
    return j;
  }
  
  private static native int nComputeLineBreaks(long paramLong1, char[] paramArrayOfChar, long paramLong2, int paramInt1, float paramFloat1, int paramInt2, float paramFloat2, int[] paramArrayOfInt1, int paramInt3, int paramInt4, LineBreaks paramLineBreaks, int paramInt5, int[] paramArrayOfInt2, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, int[] paramArrayOfInt3, float[] paramArrayOfFloat4);
  
  @CriticalNative
  private static native void nFinish(long paramLong);
  
  @FastNative
  private static native long nInit(int paramInt1, int paramInt2, boolean paramBoolean, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);
  
  private int out(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat1, float paramFloat2, LineHeightSpan[] paramArrayOfLineHeightSpan, int[] paramArrayOfInt, Paint.FontMetricsInt paramFontMetricsInt, int paramInt8, boolean paramBoolean1, MeasuredParagraph paramMeasuredParagraph, int paramInt9, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, char[] paramArrayOfChar, float[] paramArrayOfFloat, int paramInt10, TextUtils.TruncateAt paramTruncateAt, float paramFloat3, float paramFloat4, TextPaint paramTextPaint, boolean paramBoolean5)
  {
    int i = mLineCount;
    int j = i * mColumns;
    int k = j + mColumns + 1;
    Object localObject = mLines;
    int m = paramMeasuredParagraph.getParagraphDir();
    paramArrayOfChar = (char[])localObject;
    if (k >= localObject.length)
    {
      paramArrayOfChar = ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(k));
      System.arraycopy(localObject, 0, paramArrayOfChar, 0, localObject.length);
      mLines = paramArrayOfChar;
    }
    if (i >= mLineDirections.length)
    {
      localObject = (Layout.Directions[])ArrayUtils.newUnpaddedArray(Layout.Directions.class, GrowingArrayUtils.growSize(i));
      System.arraycopy(mLineDirections, 0, localObject, 0, mLineDirections.length);
      mLineDirections = ((Layout.Directions[])localObject);
    }
    if (paramArrayOfLineHeightSpan != null)
    {
      ascent = paramInt3;
      descent = paramInt4;
      top = paramInt5;
      bottom = paramInt6;
      paramInt4 = 0;
      paramInt3 = i;
      paramInt5 = k;
      while (paramInt4 < paramArrayOfLineHeightSpan.length)
      {
        if ((paramArrayOfLineHeightSpan[paramInt4] instanceof LineHeightSpan.WithDensity)) {
          ((LineHeightSpan.WithDensity)paramArrayOfLineHeightSpan[paramInt4]).chooseHeight(paramCharSequence, paramInt1, paramInt2, paramArrayOfInt[paramInt4], paramInt7, paramFontMetricsInt, paramTextPaint);
        } else {
          paramArrayOfLineHeightSpan[paramInt4].chooseHeight(paramCharSequence, paramInt1, paramInt2, paramArrayOfInt[paramInt4], paramInt7, paramFontMetricsInt);
        }
        paramInt4++;
      }
      k = ascent;
      paramInt5 = descent;
      paramInt6 = top;
      paramInt4 = bottom;
      i = paramInt3;
      paramInt3 = paramInt4;
    }
    else
    {
      k = paramInt3;
      paramInt3 = paramInt6;
      paramInt6 = paramInt5;
      paramInt5 = paramInt4;
    }
    int n;
    if (i == 0) {
      n = 1;
    } else {
      n = 0;
    }
    int i1;
    if (i + 1 == mMaximumVisibleLineCount) {
      i1 = 1;
    } else {
      i1 = 0;
    }
    if (paramTruncateAt != null)
    {
      boolean bool;
      if ((paramBoolean5) && (mLineCount + 1 == mMaximumVisibleLineCount)) {
        bool = true;
      } else {
        bool = false;
      }
      if (((mMaximumVisibleLineCount == 1) && (paramBoolean5)) || (((n != 0) && (!paramBoolean5) && (paramTruncateAt != TextUtils.TruncateAt.MARQUEE)) || ((n == 0) && ((i1 != 0) || (!paramBoolean5)) && (paramTruncateAt == TextUtils.TruncateAt.END)))) {
        paramInt4 = 1;
      } else {
        paramInt4 = 0;
      }
      if (paramInt4 != 0)
      {
        paramInt4 = 1;
        calculateEllipsis(paramInt1, paramInt2, paramArrayOfFloat, paramInt10, paramFloat3, paramTruncateAt, i, paramFloat4, paramTextPaint, bool);
      }
      else
      {
        paramInt4 = 1;
      }
    }
    else
    {
      paramInt4 = 1;
    }
    int i2 = paramInt10;
    if (mEllipsized)
    {
      paramInt9 = 1;
    }
    else
    {
      if ((i2 == paramInt9) || (paramInt9 <= 0) || (paramCharSequence.charAt(paramInt9 - 1) != '\n')) {
        paramInt4 = 0;
      }
      if ((paramInt2 == paramInt9) && (paramInt4 == 0)) {}
      while ((paramInt1 == paramInt9) && (paramInt4 != 0))
      {
        paramInt9 = 1;
        break;
      }
      paramInt9 = 0;
    }
    paramInt10 = k;
    if (n != 0)
    {
      if (paramBoolean3) {
        mTopPadding = (paramInt6 - k);
      }
      paramInt10 = k;
      if (paramBoolean2) {
        paramInt10 = paramInt6;
      }
    }
    paramInt4 = paramInt5;
    if (paramInt9 != 0)
    {
      if (paramBoolean3) {
        mBottomPadding = (paramInt3 - paramInt5);
      }
      paramInt4 = paramInt5;
      if (paramBoolean2) {
        paramInt4 = paramInt3;
      }
    }
    if (paramBoolean1)
    {
      if ((!paramBoolean4) && (paramInt9 != 0)) {
        break label700;
      }
      double d = (paramInt4 - paramInt10) * (paramFloat1 - 1.0F) + paramFloat2;
      if (d >= 0.0D) {
        paramInt5 = (int)(0.5D + d);
      } else {
        paramInt5 = -(int)(-d + 0.5D);
      }
      break label703;
    }
    label700:
    paramInt5 = 0;
    label703:
    paramArrayOfChar[(j + 0)] = paramInt1;
    paramArrayOfChar[(j + 1)] = paramInt7;
    paramArrayOfChar[(j + 2)] = (paramInt4 + paramInt5);
    paramArrayOfChar[(j + 3)] = paramInt5;
    if ((!mEllipsized) && (i1 != 0))
    {
      if (!paramBoolean2) {
        paramInt3 = paramInt4;
      }
      mMaxLineHeight = (paramInt7 + (paramInt3 - paramInt10));
    }
    paramInt3 = paramInt7 + (paramInt4 - paramInt10 + paramInt5);
    paramArrayOfChar[(j + mColumns + 0)] = paramInt2;
    paramArrayOfChar[(j + mColumns + 1)] = paramInt3;
    paramInt4 = j + 0;
    paramArrayOfChar[paramInt4] |= paramInt8 & 0x20000000;
    paramArrayOfChar[(j + 4)] = paramInt8;
    paramInt4 = j + 0;
    paramArrayOfChar[paramInt4] |= m << 30;
    mLineDirections[i] = paramMeasuredParagraph.getDirections(paramInt1 - i2, paramInt2 - i2);
    mLineCount += 1;
    return paramInt3;
  }
  
  /* Error */
  void generate(Builder paramBuilder, boolean paramBoolean1, boolean paramBoolean2)
  {
    // Byte code:
    //   0: aload_0
    //   1: astore 4
    //   3: aload_1
    //   4: invokestatic 82	android/text/StaticLayout$Builder:access$400	(Landroid/text/StaticLayout$Builder;)Ljava/lang/CharSequence;
    //   7: astore 5
    //   9: aload_1
    //   10: invokestatic 390	android/text/StaticLayout$Builder:access$1800	(Landroid/text/StaticLayout$Builder;)I
    //   13: istore 6
    //   15: aload_1
    //   16: invokestatic 393	android/text/StaticLayout$Builder:access$1900	(Landroid/text/StaticLayout$Builder;)I
    //   19: istore 7
    //   21: aload_1
    //   22: invokestatic 96	android/text/StaticLayout$Builder:access$500	(Landroid/text/StaticLayout$Builder;)Landroid/text/TextPaint;
    //   25: astore 8
    //   27: aload_1
    //   28: invokestatic 100	android/text/StaticLayout$Builder:access$600	(Landroid/text/StaticLayout$Builder;)I
    //   31: istore 9
    //   33: aload_1
    //   34: invokestatic 108	android/text/StaticLayout$Builder:access$800	(Landroid/text/StaticLayout$Builder;)Landroid/text/TextDirectionHeuristic;
    //   37: astore 10
    //   39: aload_1
    //   40: invokestatic 396	android/text/StaticLayout$Builder:access$2000	(Landroid/text/StaticLayout$Builder;)Z
    //   43: istore 11
    //   45: aload_1
    //   46: invokestatic 112	android/text/StaticLayout$Builder:access$900	(Landroid/text/StaticLayout$Builder;)F
    //   49: fstore 12
    //   51: aload_1
    //   52: invokestatic 115	android/text/StaticLayout$Builder:access$1000	(Landroid/text/StaticLayout$Builder;)F
    //   55: fstore 13
    //   57: aload_1
    //   58: invokestatic 134	android/text/StaticLayout$Builder:access$1100	(Landroid/text/StaticLayout$Builder;)I
    //   61: i2f
    //   62: fstore 14
    //   64: aload_1
    //   65: invokestatic 78	android/text/StaticLayout$Builder:access$300	(Landroid/text/StaticLayout$Builder;)Landroid/text/TextUtils$TruncateAt;
    //   68: astore 15
    //   70: aload_1
    //   71: invokestatic 399	android/text/StaticLayout$Builder:access$2100	(Landroid/text/StaticLayout$Builder;)Z
    //   74: istore 16
    //   76: new 11	android/text/StaticLayout$LineBreaks
    //   79: dup
    //   80: invokespecial 402	android/text/StaticLayout$LineBreaks:<init>	()V
    //   83: astore 17
    //   85: new 404	android/text/AutoGrowArray$FloatArray
    //   88: dup
    //   89: invokespecial 405	android/text/AutoGrowArray$FloatArray:<init>	()V
    //   92: astore 18
    //   94: aload 4
    //   96: iconst_0
    //   97: putfield 331	android/text/StaticLayout:mLineCount	I
    //   100: aload 4
    //   102: iconst_0
    //   103: putfield 313	android/text/StaticLayout:mEllipsized	Z
    //   106: aload 4
    //   108: getfield 123	android/text/StaticLayout:mMaximumVisibleLineCount	I
    //   111: iconst_1
    //   112: if_icmpge +9 -> 121
    //   115: iconst_0
    //   116: istore 19
    //   118: goto +6 -> 124
    //   121: iconst_m1
    //   122: istore 19
    //   124: aload 4
    //   126: iload 19
    //   128: putfield 120	android/text/StaticLayout:mMaxLineHeight	I
    //   131: fload 12
    //   133: fconst_1
    //   134: fcmpl
    //   135: ifne +19 -> 154
    //   138: fload 13
    //   140: fconst_0
    //   141: fcmpl
    //   142: ifeq +6 -> 148
    //   145: goto +9 -> 154
    //   148: iconst_0
    //   149: istore 20
    //   151: goto +6 -> 157
    //   154: iconst_1
    //   155: istore 20
    //   157: aload_1
    //   158: invokestatic 409	android/text/StaticLayout$Builder:access$2200	(Landroid/text/StaticLayout$Builder;)Landroid/graphics/Paint$FontMetricsInt;
    //   161: astore 21
    //   163: aload 4
    //   165: getfield 171	android/text/StaticLayout:mLeftIndents	[I
    //   168: ifnonnull +20 -> 188
    //   171: aload 4
    //   173: getfield 176	android/text/StaticLayout:mRightIndents	[I
    //   176: ifnull +6 -> 182
    //   179: goto +9 -> 188
    //   182: aconst_null
    //   183: astore 22
    //   185: goto +122 -> 307
    //   188: aload 4
    //   190: getfield 171	android/text/StaticLayout:mLeftIndents	[I
    //   193: ifnonnull +9 -> 202
    //   196: iconst_0
    //   197: istore 23
    //   199: goto +11 -> 210
    //   202: aload 4
    //   204: getfield 171	android/text/StaticLayout:mLeftIndents	[I
    //   207: arraylength
    //   208: istore 23
    //   210: aload 4
    //   212: getfield 176	android/text/StaticLayout:mRightIndents	[I
    //   215: ifnonnull +9 -> 224
    //   218: iconst_0
    //   219: istore 19
    //   221: goto +11 -> 232
    //   224: aload 4
    //   226: getfield 176	android/text/StaticLayout:mRightIndents	[I
    //   229: arraylength
    //   230: istore 19
    //   232: iload 23
    //   234: iload 19
    //   236: invokestatic 412	java/lang/Math:max	(II)I
    //   239: newarray int
    //   241: astore 22
    //   243: iconst_0
    //   244: istore 24
    //   246: iload 24
    //   248: iload 23
    //   250: if_icmpge +22 -> 272
    //   253: aload 22
    //   255: iload 24
    //   257: aload 4
    //   259: getfield 171	android/text/StaticLayout:mLeftIndents	[I
    //   262: iload 24
    //   264: iaload
    //   265: iastore
    //   266: iinc 24 1
    //   269: goto -23 -> 246
    //   272: iconst_0
    //   273: istore 23
    //   275: iload 23
    //   277: iload 19
    //   279: if_icmpge +28 -> 307
    //   282: aload 22
    //   284: iload 23
    //   286: aload 22
    //   288: iload 23
    //   290: iaload
    //   291: aload 4
    //   293: getfield 176	android/text/StaticLayout:mRightIndents	[I
    //   296: iload 23
    //   298: iaload
    //   299: iadd
    //   300: iastore
    //   301: iinc 23 1
    //   304: goto -29 -> 275
    //   307: aload_1
    //   308: invokestatic 415	android/text/StaticLayout$Builder:access$2300	(Landroid/text/StaticLayout$Builder;)I
    //   311: istore 23
    //   313: aload_1
    //   314: invokestatic 418	android/text/StaticLayout$Builder:access$2400	(Landroid/text/StaticLayout$Builder;)I
    //   317: istore 19
    //   319: aload_1
    //   320: invokestatic 189	android/text/StaticLayout$Builder:access$1700	(Landroid/text/StaticLayout$Builder;)I
    //   323: ifeq +9 -> 332
    //   326: iconst_1
    //   327: istore 25
    //   329: goto +6 -> 335
    //   332: iconst_0
    //   333: istore 25
    //   335: iload 23
    //   337: iload 19
    //   339: iload 25
    //   341: aload 22
    //   343: aload 4
    //   345: getfield 181	android/text/StaticLayout:mLeftPaddings	[I
    //   348: aload 4
    //   350: getfield 186	android/text/StaticLayout:mRightPaddings	[I
    //   353: invokestatic 420	android/text/StaticLayout:nInit	(IIZ[I[I[I)J
    //   356: lstore 26
    //   358: aload 5
    //   360: instanceof 84
    //   363: ifeq +13 -> 376
    //   366: aload 5
    //   368: checkcast 84	android/text/Spanned
    //   371: astore 22
    //   373: goto +6 -> 379
    //   376: aconst_null
    //   377: astore 22
    //   379: aload 5
    //   381: instanceof 422
    //   384: ifeq +44 -> 428
    //   387: aload 5
    //   389: checkcast 422	android/text/PrecomputedText
    //   392: astore 28
    //   394: aload 28
    //   396: iload 6
    //   398: iload 7
    //   400: aload 10
    //   402: aload 8
    //   404: aload_1
    //   405: invokestatic 415	android/text/StaticLayout$Builder:access$2300	(Landroid/text/StaticLayout$Builder;)I
    //   408: aload_1
    //   409: invokestatic 418	android/text/StaticLayout$Builder:access$2400	(Landroid/text/StaticLayout$Builder;)I
    //   412: invokevirtual 426	android/text/PrecomputedText:canUseMeasuredResult	(IILandroid/text/TextDirectionHeuristic;Landroid/text/TextPaint;II)Z
    //   415: ifeq +13 -> 428
    //   418: aload 28
    //   420: invokevirtual 430	android/text/PrecomputedText:getParagraphInfo	()[Landroid/text/PrecomputedText$ParagraphInfo;
    //   423: astore 28
    //   425: goto +6 -> 431
    //   428: aconst_null
    //   429: astore 28
    //   431: aload 22
    //   433: astore 29
    //   435: aload 28
    //   437: astore 22
    //   439: aload 28
    //   441: ifnonnull +34 -> 475
    //   444: aload 5
    //   446: new 432	android/text/PrecomputedText$Params
    //   449: dup
    //   450: aload 8
    //   452: aload 10
    //   454: aload_1
    //   455: invokestatic 415	android/text/StaticLayout$Builder:access$2300	(Landroid/text/StaticLayout$Builder;)I
    //   458: aload_1
    //   459: invokestatic 418	android/text/StaticLayout$Builder:access$2400	(Landroid/text/StaticLayout$Builder;)I
    //   462: invokespecial 435	android/text/PrecomputedText$Params:<init>	(Landroid/text/TextPaint;Landroid/text/TextDirectionHeuristic;II)V
    //   465: iload 6
    //   467: iload 7
    //   469: iconst_0
    //   470: invokestatic 439	android/text/PrecomputedText:createMeasuredParagraphs	(Ljava/lang/CharSequence;Landroid/text/PrecomputedText$Params;IIZ)[Landroid/text/PrecomputedText$ParagraphInfo;
    //   473: astore 22
    //   475: aload 22
    //   477: astore 30
    //   479: iconst_0
    //   480: istore 23
    //   482: aconst_null
    //   483: astore_1
    //   484: iconst_0
    //   485: istore 31
    //   487: aload 15
    //   489: astore 22
    //   491: aload 5
    //   493: astore 28
    //   495: iload 6
    //   497: istore 19
    //   499: aload 8
    //   501: astore 15
    //   503: iload 23
    //   505: istore 6
    //   507: aload 30
    //   509: arraylength
    //   510: istore 23
    //   512: iload 31
    //   514: iload 23
    //   516: if_icmpge +2645 -> 3161
    //   519: iload 31
    //   521: ifne +10 -> 531
    //   524: iload 19
    //   526: istore 24
    //   528: goto +15 -> 543
    //   531: aload 30
    //   533: iload 31
    //   535: iconst_1
    //   536: isub
    //   537: aaload
    //   538: getfield 444	android/text/PrecomputedText$ParagraphInfo:paragraphEnd	I
    //   541: istore 24
    //   543: aload 30
    //   545: iload 31
    //   547: aaload
    //   548: getfield 444	android/text/PrecomputedText$ParagraphInfo:paragraphEnd	I
    //   551: istore 32
    //   553: iload 9
    //   555: istore 33
    //   557: iload 9
    //   559: istore 23
    //   561: aload 29
    //   563: ifnull +337 -> 900
    //   566: aload 29
    //   568: iload 24
    //   570: iload 32
    //   572: ldc_w 446
    //   575: invokestatic 450	android/text/StaticLayout:getParagraphSpans	(Landroid/text/Spanned;IILjava/lang/Class;)[Ljava/lang/Object;
    //   578: checkcast 452	[Landroid/text/style/LeadingMarginSpan;
    //   581: astore 5
    //   583: iconst_1
    //   584: istore 34
    //   586: iconst_0
    //   587: istore 35
    //   589: aload 15
    //   591: astore 8
    //   593: iload 35
    //   595: aload 5
    //   597: arraylength
    //   598: if_icmpge +112 -> 710
    //   601: aload 5
    //   603: iload 35
    //   605: aaload
    //   606: astore 36
    //   608: aload 5
    //   610: iload 35
    //   612: aaload
    //   613: astore 15
    //   615: aload_1
    //   616: astore 37
    //   618: iload 33
    //   620: aload 15
    //   622: iconst_1
    //   623: invokeinterface 456 2 0
    //   628: isub
    //   629: istore 33
    //   631: aload_1
    //   632: astore 37
    //   634: iload 23
    //   636: aload 5
    //   638: iload 35
    //   640: aaload
    //   641: iconst_0
    //   642: invokeinterface 456 2 0
    //   647: isub
    //   648: istore 38
    //   650: iload 34
    //   652: istore 23
    //   654: aload_1
    //   655: astore 37
    //   657: aload 36
    //   659: instanceof 458
    //   662: ifeq +30 -> 692
    //   665: aload_1
    //   666: astore 37
    //   668: aload 36
    //   670: checkcast 458	android/text/style/LeadingMarginSpan$LeadingMarginSpan2
    //   673: astore 15
    //   675: aload_1
    //   676: astore 37
    //   678: iload 34
    //   680: aload 15
    //   682: invokeinterface 461 1 0
    //   687: invokestatic 412	java/lang/Math:max	(II)I
    //   690: istore 23
    //   692: iinc 35 1
    //   695: aload 8
    //   697: astore 15
    //   699: iload 23
    //   701: istore 34
    //   703: iload 38
    //   705: istore 23
    //   707: goto -118 -> 589
    //   710: iload 19
    //   712: istore 35
    //   714: aload 28
    //   716: astore 36
    //   718: aload_1
    //   719: astore 37
    //   721: aload 29
    //   723: iload 24
    //   725: iload 32
    //   727: ldc_w 370
    //   730: invokestatic 450	android/text/StaticLayout:getParagraphSpans	(Landroid/text/Spanned;IILjava/lang/Class;)[Ljava/lang/Object;
    //   733: checkcast 463	[Landroid/text/style/LineHeightSpan;
    //   736: astore 39
    //   738: aload_1
    //   739: astore 37
    //   741: aload 39
    //   743: arraylength
    //   744: ifne +27 -> 771
    //   747: aconst_null
    //   748: astore 5
    //   750: iload 23
    //   752: istore 19
    //   754: aload 5
    //   756: astore 37
    //   758: aload_1
    //   759: astore 5
    //   761: iload 35
    //   763: istore 23
    //   765: aload 36
    //   767: astore_1
    //   768: goto +164 -> 932
    //   771: aload_1
    //   772: ifnull +17 -> 789
    //   775: aload_1
    //   776: astore 15
    //   778: aload_1
    //   779: astore 37
    //   781: aload_1
    //   782: arraylength
    //   783: aload 39
    //   785: arraylength
    //   786: if_icmpge +14 -> 800
    //   789: aload_1
    //   790: astore 37
    //   792: aload 39
    //   794: arraylength
    //   795: invokestatic 160	com/android/internal/util/ArrayUtils:newUnpaddedIntArray	(I)[I
    //   798: astore 15
    //   800: iconst_0
    //   801: istore 38
    //   803: aload 15
    //   805: astore_1
    //   806: aload 39
    //   808: astore 5
    //   810: aload 15
    //   812: astore 37
    //   814: iload 38
    //   816: aload 39
    //   818: arraylength
    //   819: if_icmpge -69 -> 750
    //   822: aload 15
    //   824: astore 37
    //   826: aload 29
    //   828: aload 39
    //   830: iload 38
    //   832: aaload
    //   833: invokeinterface 467 2 0
    //   838: istore 40
    //   840: iload 40
    //   842: iload 24
    //   844: if_icmpge +27 -> 871
    //   847: aload 15
    //   849: astore 37
    //   851: aload 15
    //   853: iload 38
    //   855: aload 4
    //   857: aload 4
    //   859: iload 40
    //   861: invokevirtual 470	android/text/StaticLayout:getLineForOffset	(I)I
    //   864: invokevirtual 473	android/text/StaticLayout:getLineTop	(I)I
    //   867: iastore
    //   868: goto +10 -> 878
    //   871: aload 15
    //   873: iload 38
    //   875: iload 6
    //   877: iastore
    //   878: iinc 38 1
    //   881: goto -78 -> 803
    //   884: astore_1
    //   885: iload 19
    //   887: istore 23
    //   889: goto +137 -> 1026
    //   892: astore_1
    //   893: goto +2457 -> 3350
    //   896: astore_1
    //   897: goto +2453 -> 3350
    //   900: iload 19
    //   902: istore 34
    //   904: aload_1
    //   905: astore 5
    //   907: iconst_1
    //   908: istore 35
    //   910: iload 23
    //   912: istore 19
    //   914: aconst_null
    //   915: astore 37
    //   917: aload 28
    //   919: astore_1
    //   920: iload 34
    //   922: istore 23
    //   924: aload 15
    //   926: astore 8
    //   928: iload 35
    //   930: istore 34
    //   932: aload 10
    //   934: astore 36
    //   936: iconst_0
    //   937: istore 25
    //   939: aload 29
    //   941: ifnull +88 -> 1029
    //   944: aload 29
    //   946: iload 24
    //   948: iload 32
    //   950: ldc_w 475
    //   953: invokestatic 450	android/text/StaticLayout:getParagraphSpans	(Landroid/text/Spanned;IILjava/lang/Class;)[Ljava/lang/Object;
    //   956: checkcast 477	[Landroid/text/style/TabStopSpan;
    //   959: astore 28
    //   961: aload 28
    //   963: arraylength
    //   964: ifle +65 -> 1029
    //   967: aload 28
    //   969: arraylength
    //   970: newarray int
    //   972: astore 10
    //   974: iconst_0
    //   975: istore 35
    //   977: iload 35
    //   979: aload 28
    //   981: arraylength
    //   982: if_icmpge +24 -> 1006
    //   985: aload 10
    //   987: iload 35
    //   989: aload 28
    //   991: iload 35
    //   993: aaload
    //   994: invokeinterface 480 1 0
    //   999: iastore
    //   1000: iinc 35 1
    //   1003: goto -26 -> 977
    //   1006: aload 10
    //   1008: iconst_0
    //   1009: aload 10
    //   1011: arraylength
    //   1012: invokestatic 486	java/util/Arrays:sort	([III)V
    //   1015: goto +17 -> 1032
    //   1018: astore 22
    //   1020: aload_1
    //   1021: astore 28
    //   1023: aload 22
    //   1025: astore_1
    //   1026: goto +2324 -> 3350
    //   1029: aconst_null
    //   1030: astore 10
    //   1032: aload 30
    //   1034: iload 31
    //   1036: aaload
    //   1037: getfield 490	android/text/PrecomputedText$ParagraphInfo:measured	Landroid/text/MeasuredParagraph;
    //   1040: astore 41
    //   1042: aload 41
    //   1044: invokevirtual 494	android/text/MeasuredParagraph:getChars	()[C
    //   1047: astore 42
    //   1049: aload 41
    //   1051: invokevirtual 498	android/text/MeasuredParagraph:getSpanEndCache	()Landroid/text/AutoGrowArray$IntArray;
    //   1054: invokevirtual 504	android/text/AutoGrowArray$IntArray:getRawArray	()[I
    //   1057: astore 43
    //   1059: aload 41
    //   1061: invokevirtual 507	android/text/MeasuredParagraph:getFontMetrics	()Landroid/text/AutoGrowArray$IntArray;
    //   1064: invokevirtual 504	android/text/AutoGrowArray$IntArray:getRawArray	()[I
    //   1067: astore 44
    //   1069: aload 18
    //   1071: aload 42
    //   1073: arraylength
    //   1074: invokevirtual 510	android/text/AutoGrowArray$FloatArray:resize	(I)V
    //   1077: aload 41
    //   1079: invokevirtual 514	android/text/MeasuredParagraph:getNativePtr	()J
    //   1082: lstore 45
    //   1084: iload 33
    //   1086: i2f
    //   1087: fstore 47
    //   1089: iload 19
    //   1091: i2f
    //   1092: fstore 48
    //   1094: aload 4
    //   1096: getfield 331	android/text/StaticLayout:mLineCount	I
    //   1099: istore 35
    //   1101: iload 31
    //   1103: istore 38
    //   1105: aload 17
    //   1107: astore 39
    //   1109: aload 39
    //   1111: getfield 517	android/text/StaticLayout$LineBreaks:breaks	[I
    //   1114: arraylength
    //   1115: istore 31
    //   1117: aload 30
    //   1119: astore 15
    //   1121: aload 39
    //   1123: getfield 517	android/text/StaticLayout$LineBreaks:breaks	[I
    //   1126: astore 30
    //   1128: aload 29
    //   1130: astore 28
    //   1132: aload 39
    //   1134: getfield 521	android/text/StaticLayout$LineBreaks:widths	[F
    //   1137: astore 29
    //   1139: aload 41
    //   1141: astore 17
    //   1143: aload 39
    //   1145: getfield 524	android/text/StaticLayout$LineBreaks:ascents	[F
    //   1148: astore 41
    //   1150: iload 19
    //   1152: istore 40
    //   1154: lload 26
    //   1156: aload 42
    //   1158: lload 45
    //   1160: iload 32
    //   1162: iload 24
    //   1164: isub
    //   1165: fload 47
    //   1167: iload 34
    //   1169: fload 48
    //   1171: aload 10
    //   1173: bipush 20
    //   1175: iload 35
    //   1177: aload 39
    //   1179: iload 31
    //   1181: aload 30
    //   1183: aload 29
    //   1185: aload 41
    //   1187: aload 39
    //   1189: getfield 527	android/text/StaticLayout$LineBreaks:descents	[F
    //   1192: aload 39
    //   1194: getfield 530	android/text/StaticLayout$LineBreaks:flags	[I
    //   1197: aload 18
    //   1199: invokevirtual 533	android/text/AutoGrowArray$FloatArray:getRawArray	()[F
    //   1202: invokestatic 535	android/text/StaticLayout:nComputeLineBreaks	(J[CJIFIF[IIILandroid/text/StaticLayout$LineBreaks;I[I[F[F[F[I[F)I
    //   1205: istore 35
    //   1207: aload 39
    //   1209: getfield 517	android/text/StaticLayout$LineBreaks:breaks	[I
    //   1212: astore 49
    //   1214: aload 39
    //   1216: getfield 521	android/text/StaticLayout$LineBreaks:widths	[F
    //   1219: astore 50
    //   1221: aload 39
    //   1223: getfield 524	android/text/StaticLayout$LineBreaks:ascents	[F
    //   1226: astore 41
    //   1228: aload 39
    //   1230: getfield 527	android/text/StaticLayout$LineBreaks:descents	[F
    //   1233: astore 51
    //   1235: aload 39
    //   1237: getfield 530	android/text/StaticLayout$LineBreaks:flags	[I
    //   1240: astore 52
    //   1242: aload 4
    //   1244: getfield 123	android/text/StaticLayout:mMaximumVisibleLineCount	I
    //   1247: istore 31
    //   1249: aload 4
    //   1251: getfield 331	android/text/StaticLayout:mLineCount	I
    //   1254: istore 19
    //   1256: iload 31
    //   1258: iload 19
    //   1260: isub
    //   1261: istore 53
    //   1263: aload 22
    //   1265: ifnull +53 -> 1318
    //   1268: getstatic 303	android/text/TextUtils$TruncateAt:END	Landroid/text/TextUtils$TruncateAt;
    //   1271: astore 29
    //   1273: aload 22
    //   1275: astore 10
    //   1277: aload 10
    //   1279: aload 29
    //   1281: if_acmpeq +27 -> 1308
    //   1284: aload 4
    //   1286: getfield 123	android/text/StaticLayout:mMaximumVisibleLineCount	I
    //   1289: iconst_1
    //   1290: if_icmpne +28 -> 1318
    //   1293: aload 10
    //   1295: getstatic 306	android/text/TextUtils$TruncateAt:MARQUEE	Landroid/text/TextUtils$TruncateAt;
    //   1298: if_acmpeq +20 -> 1318
    //   1301: goto +7 -> 1308
    //   1304: astore_1
    //   1305: goto +2045 -> 3350
    //   1308: iconst_1
    //   1309: istore 19
    //   1311: goto +10 -> 1321
    //   1314: astore_1
    //   1315: goto +2035 -> 3350
    //   1318: iconst_0
    //   1319: istore 19
    //   1321: aload 22
    //   1323: astore 30
    //   1325: iload 35
    //   1327: istore 31
    //   1329: iload 53
    //   1331: ifle +175 -> 1506
    //   1334: iload 35
    //   1336: istore 31
    //   1338: iload 53
    //   1340: iload 35
    //   1342: if_icmpge +164 -> 1506
    //   1345: iload 35
    //   1347: istore 31
    //   1349: iload 19
    //   1351: ifeq +155 -> 1506
    //   1354: fconst_0
    //   1355: fstore 47
    //   1357: iconst_0
    //   1358: istore 34
    //   1360: iload 53
    //   1362: iconst_1
    //   1363: isub
    //   1364: istore 31
    //   1366: iload 31
    //   1368: iload 35
    //   1370: if_icmpge +100 -> 1470
    //   1373: iload 31
    //   1375: iload 35
    //   1377: iconst_1
    //   1378: isub
    //   1379: if_icmpne +16 -> 1395
    //   1382: fload 47
    //   1384: aload 50
    //   1386: iload 31
    //   1388: faload
    //   1389: fadd
    //   1390: fstore 48
    //   1392: goto +55 -> 1447
    //   1395: iload 31
    //   1397: ifne +9 -> 1406
    //   1400: iconst_0
    //   1401: istore 19
    //   1403: goto +12 -> 1415
    //   1406: aload 49
    //   1408: iload 31
    //   1410: iconst_1
    //   1411: isub
    //   1412: iaload
    //   1413: istore 19
    //   1415: fload 47
    //   1417: fstore 48
    //   1419: iload 19
    //   1421: aload 49
    //   1423: iload 31
    //   1425: iaload
    //   1426: if_icmpge +21 -> 1447
    //   1429: fload 47
    //   1431: aload 18
    //   1433: iload 19
    //   1435: invokevirtual 538	android/text/AutoGrowArray$FloatArray:get	(I)F
    //   1438: fadd
    //   1439: fstore 47
    //   1441: iinc 19 1
    //   1444: goto -29 -> 1415
    //   1447: iload 34
    //   1449: aload 52
    //   1451: iload 31
    //   1453: iaload
    //   1454: ldc 49
    //   1456: iand
    //   1457: ior
    //   1458: istore 34
    //   1460: iinc 31 1
    //   1463: fload 48
    //   1465: fstore 47
    //   1467: goto -101 -> 1366
    //   1470: aload 49
    //   1472: iload 53
    //   1474: iconst_1
    //   1475: isub
    //   1476: aload 49
    //   1478: iload 35
    //   1480: iconst_1
    //   1481: isub
    //   1482: iaload
    //   1483: iastore
    //   1484: aload 50
    //   1486: iload 53
    //   1488: iconst_1
    //   1489: isub
    //   1490: fload 47
    //   1492: fastore
    //   1493: aload 52
    //   1495: iload 53
    //   1497: iconst_1
    //   1498: isub
    //   1499: iload 34
    //   1501: iastore
    //   1502: iload 53
    //   1504: istore 31
    //   1506: iload 24
    //   1508: istore 35
    //   1510: iconst_0
    //   1511: istore 54
    //   1513: iconst_0
    //   1514: istore 55
    //   1516: iconst_0
    //   1517: istore 56
    //   1519: iconst_0
    //   1520: istore 57
    //   1522: iconst_0
    //   1523: istore 58
    //   1525: iconst_0
    //   1526: istore 59
    //   1528: iconst_0
    //   1529: istore 60
    //   1531: iload 35
    //   1533: istore 61
    //   1535: iload 33
    //   1537: istore 62
    //   1539: aload_1
    //   1540: astore 10
    //   1542: aload 36
    //   1544: astore 29
    //   1546: aload 39
    //   1548: astore 22
    //   1550: iload 7
    //   1552: istore 19
    //   1554: aload 42
    //   1556: astore 36
    //   1558: aload 18
    //   1560: astore_1
    //   1561: iload 32
    //   1563: istore 34
    //   1565: iload 59
    //   1567: istore 7
    //   1569: iload 60
    //   1571: istore 32
    //   1573: iload 24
    //   1575: istore 33
    //   1577: iload 35
    //   1579: iload 34
    //   1581: if_icmpge +1472 -> 3053
    //   1584: aload 43
    //   1586: iload 54
    //   1588: iaload
    //   1589: istore 63
    //   1591: iload 61
    //   1593: istore 60
    //   1595: aload 44
    //   1597: iload 56
    //   1599: iconst_4
    //   1600: imul
    //   1601: iconst_0
    //   1602: iadd
    //   1603: iaload
    //   1604: istore 24
    //   1606: aload 21
    //   1608: astore 18
    //   1610: aload_1
    //   1611: astore 39
    //   1613: aload 18
    //   1615: astore 39
    //   1617: iload 19
    //   1619: istore 59
    //   1621: aload 22
    //   1623: astore 39
    //   1625: aload 4
    //   1627: astore 39
    //   1629: lload 26
    //   1631: lstore 45
    //   1633: fload 14
    //   1635: fstore 47
    //   1637: aload 29
    //   1639: astore 39
    //   1641: aload 8
    //   1643: astore 39
    //   1645: iload 23
    //   1647: istore 59
    //   1649: aload 10
    //   1651: astore 39
    //   1653: aload 15
    //   1655: astore 39
    //   1657: aload 28
    //   1659: astore 39
    //   1661: aload 30
    //   1663: astore 39
    //   1665: aload 18
    //   1667: iload 24
    //   1669: putfield 359	android/graphics/Paint$FontMetricsInt:top	I
    //   1672: aload_1
    //   1673: astore 39
    //   1675: aload 18
    //   1677: astore 39
    //   1679: iload 19
    //   1681: istore 59
    //   1683: aload 22
    //   1685: astore 39
    //   1687: aload 4
    //   1689: astore 39
    //   1691: lload 26
    //   1693: lstore 45
    //   1695: fload 14
    //   1697: fstore 47
    //   1699: aload 29
    //   1701: astore 39
    //   1703: aload 8
    //   1705: astore 39
    //   1707: iload 23
    //   1709: istore 59
    //   1711: aload 10
    //   1713: astore 39
    //   1715: aload 15
    //   1717: astore 39
    //   1719: aload 28
    //   1721: astore 39
    //   1723: aload 30
    //   1725: astore 39
    //   1727: aload 18
    //   1729: aload 44
    //   1731: iload 56
    //   1733: iconst_4
    //   1734: imul
    //   1735: iconst_1
    //   1736: iadd
    //   1737: iaload
    //   1738: putfield 362	android/graphics/Paint$FontMetricsInt:bottom	I
    //   1741: aload_1
    //   1742: astore 39
    //   1744: aload 18
    //   1746: astore 39
    //   1748: iload 19
    //   1750: istore 59
    //   1752: aload 22
    //   1754: astore 39
    //   1756: aload 4
    //   1758: astore 39
    //   1760: lload 26
    //   1762: lstore 45
    //   1764: fload 14
    //   1766: fstore 47
    //   1768: aload 29
    //   1770: astore 39
    //   1772: aload 8
    //   1774: astore 39
    //   1776: iload 23
    //   1778: istore 59
    //   1780: aload 10
    //   1782: astore 39
    //   1784: aload 15
    //   1786: astore 39
    //   1788: aload 28
    //   1790: astore 39
    //   1792: aload 30
    //   1794: astore 39
    //   1796: aload 18
    //   1798: aload 44
    //   1800: iload 56
    //   1802: iconst_4
    //   1803: imul
    //   1804: iconst_2
    //   1805: iadd
    //   1806: iaload
    //   1807: putfield 353	android/graphics/Paint$FontMetricsInt:ascent	I
    //   1810: aload_1
    //   1811: astore 39
    //   1813: aload 18
    //   1815: astore 39
    //   1817: iload 19
    //   1819: istore 59
    //   1821: aload 22
    //   1823: astore 39
    //   1825: aload 4
    //   1827: astore 39
    //   1829: lload 26
    //   1831: lstore 45
    //   1833: fload 14
    //   1835: fstore 47
    //   1837: aload 29
    //   1839: astore 39
    //   1841: aload 8
    //   1843: astore 39
    //   1845: iload 23
    //   1847: istore 59
    //   1849: aload 10
    //   1851: astore 39
    //   1853: aload 15
    //   1855: astore 39
    //   1857: aload 28
    //   1859: astore 39
    //   1861: aload 30
    //   1863: astore 39
    //   1865: aload 18
    //   1867: aload 44
    //   1869: iload 56
    //   1871: iconst_4
    //   1872: imul
    //   1873: iconst_3
    //   1874: iadd
    //   1875: iaload
    //   1876: putfield 356	android/graphics/Paint$FontMetricsInt:descent	I
    //   1879: aload_1
    //   1880: astore 39
    //   1882: aload 18
    //   1884: astore 39
    //   1886: iload 19
    //   1888: istore 59
    //   1890: aload 22
    //   1892: astore 39
    //   1894: aload 4
    //   1896: astore 39
    //   1898: lload 26
    //   1900: lstore 45
    //   1902: fload 14
    //   1904: fstore 47
    //   1906: aload 29
    //   1908: astore 39
    //   1910: aload 8
    //   1912: astore 39
    //   1914: iload 23
    //   1916: istore 59
    //   1918: aload 10
    //   1920: astore 39
    //   1922: aload 15
    //   1924: astore 39
    //   1926: aload 28
    //   1928: astore 39
    //   1930: aload 30
    //   1932: astore 39
    //   1934: aload 18
    //   1936: getfield 359	android/graphics/Paint$FontMetricsInt:top	I
    //   1939: istore 24
    //   1941: iload 32
    //   1943: istore 61
    //   1945: iload 24
    //   1947: iload 32
    //   1949: if_icmpge +76 -> 2025
    //   1952: aload_1
    //   1953: astore 39
    //   1955: aload 18
    //   1957: astore 39
    //   1959: iload 19
    //   1961: istore 59
    //   1963: aload 22
    //   1965: astore 39
    //   1967: aload 4
    //   1969: astore 39
    //   1971: lload 26
    //   1973: lstore 45
    //   1975: fload 14
    //   1977: fstore 47
    //   1979: aload 29
    //   1981: astore 39
    //   1983: aload 8
    //   1985: astore 39
    //   1987: iload 23
    //   1989: istore 59
    //   1991: aload 10
    //   1993: astore 39
    //   1995: aload 15
    //   1997: astore 39
    //   1999: aload 28
    //   2001: astore 39
    //   2003: aload 30
    //   2005: astore 39
    //   2007: aload 18
    //   2009: getfield 359	android/graphics/Paint$FontMetricsInt:top	I
    //   2012: istore 61
    //   2014: goto +11 -> 2025
    //   2017: astore_1
    //   2018: lload 45
    //   2020: lstore 26
    //   2022: goto +1328 -> 3350
    //   2025: aload_1
    //   2026: astore 39
    //   2028: aload 18
    //   2030: astore 39
    //   2032: iload 19
    //   2034: istore 59
    //   2036: aload 22
    //   2038: astore 39
    //   2040: aload 4
    //   2042: astore 39
    //   2044: lload 26
    //   2046: lstore 45
    //   2048: fload 14
    //   2050: fstore 47
    //   2052: aload 29
    //   2054: astore 39
    //   2056: aload 8
    //   2058: astore 39
    //   2060: iload 23
    //   2062: istore 59
    //   2064: aload 10
    //   2066: astore 39
    //   2068: aload 15
    //   2070: astore 39
    //   2072: aload 28
    //   2074: astore 39
    //   2076: aload 30
    //   2078: astore 39
    //   2080: aload 18
    //   2082: getfield 353	android/graphics/Paint$FontMetricsInt:ascent	I
    //   2085: istore 32
    //   2087: iload 58
    //   2089: istore 24
    //   2091: iload 32
    //   2093: iload 58
    //   2095: if_icmpge +65 -> 2160
    //   2098: aload_1
    //   2099: astore 39
    //   2101: aload 18
    //   2103: astore 39
    //   2105: iload 19
    //   2107: istore 59
    //   2109: aload 22
    //   2111: astore 39
    //   2113: aload 4
    //   2115: astore 39
    //   2117: lload 26
    //   2119: lstore 45
    //   2121: fload 14
    //   2123: fstore 47
    //   2125: aload 29
    //   2127: astore 39
    //   2129: aload 8
    //   2131: astore 39
    //   2133: iload 23
    //   2135: istore 59
    //   2137: aload 10
    //   2139: astore 39
    //   2141: aload 15
    //   2143: astore 39
    //   2145: aload 28
    //   2147: astore 39
    //   2149: aload 30
    //   2151: astore 39
    //   2153: aload 18
    //   2155: getfield 353	android/graphics/Paint$FontMetricsInt:ascent	I
    //   2158: istore 24
    //   2160: aload_1
    //   2161: astore 39
    //   2163: aload 18
    //   2165: astore 39
    //   2167: iload 19
    //   2169: istore 59
    //   2171: aload 22
    //   2173: astore 39
    //   2175: aload 4
    //   2177: astore 39
    //   2179: lload 26
    //   2181: lstore 45
    //   2183: fload 14
    //   2185: fstore 47
    //   2187: aload 29
    //   2189: astore 39
    //   2191: aload 8
    //   2193: astore 39
    //   2195: iload 23
    //   2197: istore 59
    //   2199: aload 10
    //   2201: astore 39
    //   2203: aload 15
    //   2205: astore 39
    //   2207: aload 28
    //   2209: astore 39
    //   2211: aload 30
    //   2213: astore 39
    //   2215: aload 18
    //   2217: getfield 356	android/graphics/Paint$FontMetricsInt:descent	I
    //   2220: istore 32
    //   2222: iload 57
    //   2224: istore 58
    //   2226: iload 32
    //   2228: iload 57
    //   2230: if_icmple +65 -> 2295
    //   2233: aload_1
    //   2234: astore 39
    //   2236: aload 18
    //   2238: astore 39
    //   2240: iload 19
    //   2242: istore 59
    //   2244: aload 22
    //   2246: astore 39
    //   2248: aload 4
    //   2250: astore 39
    //   2252: lload 26
    //   2254: lstore 45
    //   2256: fload 14
    //   2258: fstore 47
    //   2260: aload 29
    //   2262: astore 39
    //   2264: aload 8
    //   2266: astore 39
    //   2268: iload 23
    //   2270: istore 59
    //   2272: aload 10
    //   2274: astore 39
    //   2276: aload 15
    //   2278: astore 39
    //   2280: aload 28
    //   2282: astore 39
    //   2284: aload 30
    //   2286: astore 39
    //   2288: aload 18
    //   2290: getfield 356	android/graphics/Paint$FontMetricsInt:descent	I
    //   2293: istore 58
    //   2295: aload_1
    //   2296: astore 39
    //   2298: aload 18
    //   2300: astore 39
    //   2302: iload 19
    //   2304: istore 59
    //   2306: aload 22
    //   2308: astore 39
    //   2310: aload 4
    //   2312: astore 39
    //   2314: lload 26
    //   2316: lstore 45
    //   2318: fload 14
    //   2320: fstore 47
    //   2322: aload 29
    //   2324: astore 39
    //   2326: aload 8
    //   2328: astore 39
    //   2330: iload 23
    //   2332: istore 59
    //   2334: aload 10
    //   2336: astore 39
    //   2338: aload 15
    //   2340: astore 39
    //   2342: aload 28
    //   2344: astore 39
    //   2346: aload 30
    //   2348: astore 39
    //   2350: aload 18
    //   2352: getfield 362	android/graphics/Paint$FontMetricsInt:bottom	I
    //   2355: istore 32
    //   2357: iload 7
    //   2359: istore 57
    //   2361: iload 32
    //   2363: iload 7
    //   2365: if_icmple +65 -> 2430
    //   2368: aload_1
    //   2369: astore 39
    //   2371: aload 18
    //   2373: astore 39
    //   2375: iload 19
    //   2377: istore 59
    //   2379: aload 22
    //   2381: astore 39
    //   2383: aload 4
    //   2385: astore 39
    //   2387: lload 26
    //   2389: lstore 45
    //   2391: fload 14
    //   2393: fstore 47
    //   2395: aload 29
    //   2397: astore 39
    //   2399: aload 8
    //   2401: astore 39
    //   2403: iload 23
    //   2405: istore 59
    //   2407: aload 10
    //   2409: astore 39
    //   2411: aload 15
    //   2413: astore 39
    //   2415: aload 28
    //   2417: astore 39
    //   2419: aload 30
    //   2421: astore 39
    //   2423: aload 18
    //   2425: getfield 362	android/graphics/Paint$FontMetricsInt:bottom	I
    //   2428: istore 57
    //   2430: iload 55
    //   2432: istore 7
    //   2434: iload 7
    //   2436: iload 31
    //   2438: if_icmpge +26 -> 2464
    //   2441: aload 49
    //   2443: iload 7
    //   2445: iaload
    //   2446: istore 55
    //   2448: iload 33
    //   2450: iload 55
    //   2452: iadd
    //   2453: iload 35
    //   2455: if_icmpge +9 -> 2464
    //   2458: iinc 7 1
    //   2461: goto -27 -> 2434
    //   2464: iload 61
    //   2466: istore 32
    //   2468: iload 57
    //   2470: istore 59
    //   2472: iload 58
    //   2474: istore 61
    //   2476: iload 60
    //   2478: istore 58
    //   2480: iload 63
    //   2482: istore 57
    //   2484: aload 28
    //   2486: astore 21
    //   2488: iload 38
    //   2490: istore 55
    //   2492: iload 62
    //   2494: istore 38
    //   2496: iload 6
    //   2498: istore 60
    //   2500: iload 59
    //   2502: istore 62
    //   2504: aload 22
    //   2506: astore 28
    //   2508: iload 7
    //   2510: istore 6
    //   2512: aload 18
    //   2514: astore 22
    //   2516: iload 24
    //   2518: istore 7
    //   2520: iload 61
    //   2522: istore 24
    //   2524: iload 6
    //   2526: iload 31
    //   2528: if_icmpge +448 -> 2976
    //   2531: aload 49
    //   2533: iload 6
    //   2535: iaload
    //   2536: iload 33
    //   2538: iadd
    //   2539: iload 57
    //   2541: if_icmpgt +432 -> 2973
    //   2544: aload 49
    //   2546: iload 6
    //   2548: iaload
    //   2549: istore 61
    //   2551: iload 61
    //   2553: iload 33
    //   2555: iadd
    //   2556: istore 61
    //   2558: iload 61
    //   2560: iload 19
    //   2562: if_icmpge +9 -> 2571
    //   2565: iconst_1
    //   2566: istore 25
    //   2568: goto +6 -> 2574
    //   2571: iconst_0
    //   2572: istore 25
    //   2574: iload 11
    //   2576: ifeq +76 -> 2652
    //   2579: aload_1
    //   2580: astore 39
    //   2582: aload 22
    //   2584: astore 39
    //   2586: iload 19
    //   2588: istore 59
    //   2590: aload 28
    //   2592: astore 39
    //   2594: aload 4
    //   2596: astore 39
    //   2598: lload 26
    //   2600: lstore 45
    //   2602: fload 14
    //   2604: fstore 47
    //   2606: aload 29
    //   2608: astore 39
    //   2610: aload 8
    //   2612: astore 39
    //   2614: iload 23
    //   2616: istore 59
    //   2618: aload 10
    //   2620: astore 39
    //   2622: aload 15
    //   2624: astore 39
    //   2626: aload 21
    //   2628: astore 39
    //   2630: aload 30
    //   2632: astore 39
    //   2634: iload 7
    //   2636: aload 41
    //   2638: iload 6
    //   2640: faload
    //   2641: invokestatic 542	java/lang/Math:round	(F)I
    //   2644: invokestatic 319	java/lang/Math:min	(II)I
    //   2647: istore 7
    //   2649: goto +3 -> 2652
    //   2652: iload 11
    //   2654: ifeq +76 -> 2730
    //   2657: aload_1
    //   2658: astore 39
    //   2660: aload 22
    //   2662: astore 39
    //   2664: iload 19
    //   2666: istore 59
    //   2668: aload 28
    //   2670: astore 39
    //   2672: aload 4
    //   2674: astore 39
    //   2676: lload 26
    //   2678: lstore 45
    //   2680: fload 14
    //   2682: fstore 47
    //   2684: aload 29
    //   2686: astore 39
    //   2688: aload 8
    //   2690: astore 39
    //   2692: iload 23
    //   2694: istore 59
    //   2696: aload 10
    //   2698: astore 39
    //   2700: aload 15
    //   2702: astore 39
    //   2704: aload 21
    //   2706: astore 39
    //   2708: aload 30
    //   2710: astore 39
    //   2712: iload 24
    //   2714: aload 51
    //   2716: iload 6
    //   2718: faload
    //   2719: invokestatic 542	java/lang/Math:round	(F)I
    //   2722: invokestatic 412	java/lang/Math:max	(II)I
    //   2725: istore 24
    //   2727: goto +3 -> 2730
    //   2730: aload 52
    //   2732: iload 6
    //   2734: iaload
    //   2735: istore 63
    //   2737: aload_1
    //   2738: astore 39
    //   2740: aload 22
    //   2742: astore 39
    //   2744: iload 19
    //   2746: istore 59
    //   2748: aload 28
    //   2750: astore 39
    //   2752: aload 4
    //   2754: astore 39
    //   2756: lload 26
    //   2758: lstore 45
    //   2760: fload 14
    //   2762: fstore 47
    //   2764: aload 29
    //   2766: astore 39
    //   2768: aload 8
    //   2770: astore 39
    //   2772: iload 23
    //   2774: istore 59
    //   2776: aload 10
    //   2778: astore 39
    //   2780: aload 15
    //   2782: astore 39
    //   2784: aload 21
    //   2786: astore 39
    //   2788: aload 30
    //   2790: astore 39
    //   2792: aload_1
    //   2793: invokevirtual 533	android/text/AutoGrowArray$FloatArray:getRawArray	()[F
    //   2796: astore 18
    //   2798: aload 50
    //   2800: iload 6
    //   2802: faload
    //   2803: fstore 47
    //   2805: aload 4
    //   2807: aload 10
    //   2809: iload 58
    //   2811: iload 61
    //   2813: iload 7
    //   2815: iload 24
    //   2817: iload 32
    //   2819: iload 62
    //   2821: iload 60
    //   2823: fload 12
    //   2825: fload 13
    //   2827: aload 37
    //   2829: aload 5
    //   2831: aload 22
    //   2833: iload 63
    //   2835: iload 20
    //   2837: aload 17
    //   2839: iload 19
    //   2841: iload_2
    //   2842: iload_3
    //   2843: iload 16
    //   2845: aload 36
    //   2847: aload 18
    //   2849: iload 33
    //   2851: aload 30
    //   2853: fload 14
    //   2855: fload 47
    //   2857: aload 8
    //   2859: iload 25
    //   2861: invokespecial 544	android/text/StaticLayout:out	(Ljava/lang/CharSequence;IIIIIIIFF[Landroid/text/style/LineHeightSpan;[ILandroid/graphics/Paint$FontMetricsInt;IZLandroid/text/MeasuredParagraph;IZZZ[C[FILandroid/text/TextUtils$TruncateAt;FFLandroid/text/TextPaint;Z)I
    //   2864: istore 60
    //   2866: iload 61
    //   2868: iload 57
    //   2870: if_icmpge +42 -> 2912
    //   2873: aload 22
    //   2875: astore 4
    //   2877: aload 4
    //   2879: getfield 359	android/graphics/Paint$FontMetricsInt:top	I
    //   2882: istore 32
    //   2884: aload 4
    //   2886: getfield 362	android/graphics/Paint$FontMetricsInt:bottom	I
    //   2889: istore 62
    //   2891: aload 4
    //   2893: getfield 353	android/graphics/Paint$FontMetricsInt:ascent	I
    //   2896: istore 7
    //   2898: aload 4
    //   2900: getfield 356	android/graphics/Paint$FontMetricsInt:descent	I
    //   2903: istore 24
    //   2905: goto +19 -> 2924
    //   2908: astore_1
    //   2909: goto +441 -> 3350
    //   2912: iconst_0
    //   2913: istore 24
    //   2915: iconst_0
    //   2916: istore 7
    //   2918: iconst_0
    //   2919: istore 62
    //   2921: iconst_0
    //   2922: istore 32
    //   2924: iload 61
    //   2926: istore 58
    //   2928: iinc 6 1
    //   2931: aload_0
    //   2932: getfield 331	android/text/StaticLayout:mLineCount	I
    //   2935: aload_0
    //   2936: getfield 123	android/text/StaticLayout:mMaximumVisibleLineCount	I
    //   2939: if_icmplt +20 -> 2959
    //   2942: aload_0
    //   2943: getfield 313	android/text/StaticLayout:mEllipsized	Z
    //   2946: istore 25
    //   2948: iload 25
    //   2950: ifeq +9 -> 2959
    //   2953: lload 26
    //   2955: invokestatic 546	android/text/StaticLayout:nFinish	(J)V
    //   2958: return
    //   2959: aload_0
    //   2960: astore 4
    //   2962: goto -438 -> 2524
    //   2965: astore_1
    //   2966: goto +384 -> 3350
    //   2969: astore_1
    //   2970: goto +380 -> 3350
    //   2973: goto +3 -> 2976
    //   2976: iload 57
    //   2978: istore 35
    //   2980: iinc 54 1
    //   2983: iinc 56 1
    //   2986: iload 55
    //   2988: istore 59
    //   2990: iload 7
    //   2992: istore 55
    //   2994: aload 21
    //   2996: astore 18
    //   2998: aload 22
    //   3000: astore 21
    //   3002: iload 58
    //   3004: istore 61
    //   3006: iload 62
    //   3008: istore 7
    //   3010: iload 55
    //   3012: istore 58
    //   3014: iload 24
    //   3016: istore 57
    //   3018: aload 28
    //   3020: astore 22
    //   3022: iload 6
    //   3024: istore 55
    //   3026: iload 60
    //   3028: istore 6
    //   3030: iload 38
    //   3032: istore 62
    //   3034: iload 59
    //   3036: istore 38
    //   3038: aload 18
    //   3040: astore 28
    //   3042: goto -1465 -> 1577
    //   3045: astore_1
    //   3046: lload 45
    //   3048: lstore 26
    //   3050: goto +300 -> 3350
    //   3053: iload 34
    //   3055: iload 19
    //   3057: if_icmpne +9 -> 3066
    //   3060: aload 5
    //   3062: astore_1
    //   3063: goto +122 -> 3185
    //   3066: iload 19
    //   3068: istore 7
    //   3070: aload 5
    //   3072: astore 18
    //   3074: aload 30
    //   3076: astore 5
    //   3078: aload_1
    //   3079: astore 36
    //   3081: aload 22
    //   3083: astore 17
    //   3085: aload 29
    //   3087: astore 37
    //   3089: iload 23
    //   3091: istore 19
    //   3093: aload 10
    //   3095: astore 22
    //   3097: iload 38
    //   3099: iconst_1
    //   3100: iadd
    //   3101: istore 31
    //   3103: aload 18
    //   3105: astore_1
    //   3106: aload 15
    //   3108: astore 30
    //   3110: aload 36
    //   3112: astore 18
    //   3114: aload 28
    //   3116: astore 29
    //   3118: aload 37
    //   3120: astore 10
    //   3122: aload 8
    //   3124: astore 15
    //   3126: aload 22
    //   3128: astore 28
    //   3130: aload 5
    //   3132: astore 22
    //   3134: goto -2627 -> 507
    //   3137: astore_1
    //   3138: goto +212 -> 3350
    //   3141: astore_1
    //   3142: goto +208 -> 3350
    //   3145: astore_1
    //   3146: goto +204 -> 3350
    //   3149: astore_1
    //   3150: goto +200 -> 3350
    //   3153: astore_1
    //   3154: goto +196 -> 3350
    //   3157: astore_1
    //   3158: goto +192 -> 3350
    //   3161: aload 10
    //   3163: astore 29
    //   3165: aload 28
    //   3167: astore 10
    //   3169: iload 19
    //   3171: istore 23
    //   3173: aload 15
    //   3175: astore 8
    //   3177: aload 22
    //   3179: astore 30
    //   3181: iload 7
    //   3183: istore 19
    //   3185: iload 19
    //   3187: iload 23
    //   3189: if_icmpeq +33 -> 3222
    //   3192: aload 10
    //   3194: iload 19
    //   3196: iconst_1
    //   3197: isub
    //   3198: invokeinterface 379 2 0
    //   3203: istore 24
    //   3205: iload 24
    //   3207: bipush 10
    //   3209: if_icmpne +6 -> 3215
    //   3212: goto +10 -> 3222
    //   3215: goto +124 -> 3339
    //   3218: astore_1
    //   3219: goto +131 -> 3350
    //   3222: aload 4
    //   3224: getfield 331	android/text/StaticLayout:mLineCount	I
    //   3227: istore 24
    //   3229: aload 4
    //   3231: getfield 123	android/text/StaticLayout:mMaximumVisibleLineCount	I
    //   3234: istore 7
    //   3236: iload 24
    //   3238: iload 7
    //   3240: if_icmpge +99 -> 3339
    //   3243: aload 10
    //   3245: iload 19
    //   3247: iload 19
    //   3249: aload 29
    //   3251: aconst_null
    //   3252: invokestatic 550	android/text/MeasuredParagraph:buildForBidi	(Ljava/lang/CharSequence;IILandroid/text/TextDirectionHeuristic;Landroid/text/MeasuredParagraph;)Landroid/text/MeasuredParagraph;
    //   3255: astore_1
    //   3256: aload 8
    //   3258: aload 21
    //   3260: invokevirtual 554	android/text/TextPaint:getFontMetricsInt	(Landroid/graphics/Paint$FontMetricsInt;)I
    //   3263: pop
    //   3264: aload 4
    //   3266: aload 10
    //   3268: iload 19
    //   3270: iload 19
    //   3272: aload 21
    //   3274: getfield 353	android/graphics/Paint$FontMetricsInt:ascent	I
    //   3277: aload 21
    //   3279: getfield 356	android/graphics/Paint$FontMetricsInt:descent	I
    //   3282: aload 21
    //   3284: getfield 359	android/graphics/Paint$FontMetricsInt:top	I
    //   3287: aload 21
    //   3289: getfield 362	android/graphics/Paint$FontMetricsInt:bottom	I
    //   3292: iload 6
    //   3294: fload 12
    //   3296: fload 13
    //   3298: aconst_null
    //   3299: aconst_null
    //   3300: aload 21
    //   3302: iconst_0
    //   3303: iload 20
    //   3305: aload_1
    //   3306: iload 19
    //   3308: iload_2
    //   3309: iload_3
    //   3310: iload 16
    //   3312: aconst_null
    //   3313: aconst_null
    //   3314: iload 23
    //   3316: aload 30
    //   3318: fload 14
    //   3320: fconst_0
    //   3321: aload 8
    //   3323: iconst_0
    //   3324: invokespecial 544	android/text/StaticLayout:out	(Ljava/lang/CharSequence;IIIIIIIFF[Landroid/text/style/LineHeightSpan;[ILandroid/graphics/Paint$FontMetricsInt;IZLandroid/text/MeasuredParagraph;IZZZ[C[FILandroid/text/TextUtils$TruncateAt;FFLandroid/text/TextPaint;Z)I
    //   3327: pop
    //   3328: goto +11 -> 3339
    //   3331: astore_1
    //   3332: goto +18 -> 3350
    //   3335: astore_1
    //   3336: goto +14 -> 3350
    //   3339: lload 26
    //   3341: invokestatic 546	android/text/StaticLayout:nFinish	(J)V
    //   3344: return
    //   3345: astore_1
    //   3346: goto +4 -> 3350
    //   3349: astore_1
    //   3350: lload 26
    //   3352: invokestatic 546	android/text/StaticLayout:nFinish	(J)V
    //   3355: aload_1
    //   3356: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	3357	0	this	StaticLayout
    //   0	3357	1	paramBuilder	Builder
    //   0	3357	2	paramBoolean1	boolean
    //   0	3357	3	paramBoolean2	boolean
    //   1	3264	4	localObject1	Object
    //   7	3124	5	localObject2	Object
    //   13	3280	6	i	int
    //   19	3222	7	j	int
    //   25	3297	8	localObject3	Object
    //   31	527	9	k	int
    //   37	3230	10	localObject4	Object
    //   43	2610	11	bool1	boolean
    //   49	3246	12	f1	float
    //   55	3242	13	f2	float
    //   62	3257	14	f3	float
    //   68	3106	15	localObject5	Object
    //   74	3237	16	bool2	boolean
    //   83	3001	17	localObject6	Object
    //   92	3021	18	localObject7	Object
    //   116	3191	19	m	int
    //   149	3155	20	bool3	boolean
    //   161	3140	21	localObject8	Object
    //   183	307	22	localObject9	Object
    //   1018	304	22	localObject10	Object
    //   1548	1630	22	localObject11	Object
    //   197	3118	23	n	int
    //   244	2997	24	i1	int
    //   327	2622	25	bool4	boolean
    //   356	2995	26	l1	long
    //   392	2774	28	localObject12	Object
    //   433	2817	29	localObject13	Object
    //   477	2840	30	localObject14	Object
    //   485	2617	31	i2	int
    //   551	2372	32	i3	int
    //   555	2295	33	i4	int
    //   584	2474	34	i5	int
    //   587	2392	35	i6	int
    //   606	2505	36	localObject15	Object
    //   616	2503	37	localObject16	Object
    //   648	2453	38	i7	int
    //   736	2055	39	localObject17	Object
    //   838	315	40	i8	int
    //   1040	1597	41	localObject18	Object
    //   1047	508	42	arrayOfChar	char[]
    //   1057	528	43	arrayOfInt1	int[]
    //   1067	801	44	arrayOfInt2	int[]
    //   1082	1965	45	l2	long
    //   1087	1769	47	f4	float
    //   1092	372	48	f5	float
    //   1212	1333	49	arrayOfInt3	int[]
    //   1219	1580	50	arrayOfFloat1	float[]
    //   1233	1482	51	arrayOfFloat2	float[]
    //   1240	1491	52	arrayOfInt4	int[]
    //   1261	242	53	i9	int
    //   1511	1470	54	i10	int
    //   1514	1511	55	i11	int
    //   1517	1467	56	i12	int
    //   1520	1497	57	i13	int
    //   1523	1490	58	i14	int
    //   1526	1509	59	i15	int
    //   1529	1498	60	i16	int
    //   1533	1472	61	i17	int
    //   1537	1496	62	i18	int
    //   1589	1245	63	i19	int
    // Exception table:
    //   from	to	target	type
    //   618	631	884	finally
    //   634	650	884	finally
    //   657	665	884	finally
    //   668	675	884	finally
    //   678	692	884	finally
    //   721	738	884	finally
    //   741	747	884	finally
    //   781	789	884	finally
    //   792	800	884	finally
    //   814	822	884	finally
    //   826	840	884	finally
    //   851	868	884	finally
    //   593	601	892	finally
    //   566	583	896	finally
    //   944	974	1018	finally
    //   977	1000	1018	finally
    //   1006	1015	1018	finally
    //   1284	1301	1304	finally
    //   1429	1441	1304	finally
    //   1268	1273	1314	finally
    //   2007	2014	2017	finally
    //   2153	2160	2017	finally
    //   2288	2295	2017	finally
    //   2423	2430	2017	finally
    //   2634	2649	2017	finally
    //   2712	2727	2017	finally
    //   2877	2905	2908	finally
    //   2931	2948	2965	finally
    //   2805	2866	2969	finally
    //   1665	1672	3045	finally
    //   1727	1741	3045	finally
    //   1796	1810	3045	finally
    //   1865	1879	3045	finally
    //   1934	1941	3045	finally
    //   2080	2087	3045	finally
    //   2215	2222	3045	finally
    //   2350	2357	3045	finally
    //   2792	2798	3045	finally
    //   1132	1139	3137	finally
    //   1143	1150	3137	finally
    //   1154	1256	3137	finally
    //   1121	1128	3141	finally
    //   1109	1117	3145	finally
    //   1094	1101	3149	finally
    //   1032	1077	3153	finally
    //   1077	1084	3153	finally
    //   531	543	3157	finally
    //   543	553	3157	finally
    //   3192	3205	3218	finally
    //   3256	3328	3331	finally
    //   3243	3256	3335	finally
    //   3222	3236	3345	finally
    //   507	512	3349	finally
  }
  
  public int getBottomPadding()
  {
    return mBottomPadding;
  }
  
  public int getEllipsisCount(int paramInt)
  {
    if (mColumns < 7) {
      return 0;
    }
    return mLines[(mColumns * paramInt + 6)];
  }
  
  public int getEllipsisStart(int paramInt)
  {
    if (mColumns < 7) {
      return 0;
    }
    return mLines[(mColumns * paramInt + 5)];
  }
  
  public int getEllipsizedWidth()
  {
    return mEllipsizedWidth;
  }
  
  public int getHeight(boolean paramBoolean)
  {
    if ((paramBoolean) && (mLineCount >= mMaximumVisibleLineCount) && (mMaxLineHeight == -1) && (Log.isLoggable("StaticLayout", 5)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("maxLineHeight should not be -1.  maxLines:");
      localStringBuilder.append(mMaximumVisibleLineCount);
      localStringBuilder.append(" lineCount:");
      localStringBuilder.append(mLineCount);
      Log.w("StaticLayout", localStringBuilder.toString());
    }
    int i;
    if ((paramBoolean) && (mLineCount >= mMaximumVisibleLineCount) && (mMaxLineHeight != -1)) {
      i = mMaxLineHeight;
    } else {
      i = super.getHeight();
    }
    return i;
  }
  
  public int getHyphen(int paramInt)
  {
    return mLines[(mColumns * paramInt + 4)] & 0xFF;
  }
  
  public int getIndentAdjust(int paramInt, Layout.Alignment paramAlignment)
  {
    if (paramAlignment == Layout.Alignment.ALIGN_LEFT)
    {
      if (mLeftIndents == null) {
        return 0;
      }
      return mLeftIndents[Math.min(paramInt, mLeftIndents.length - 1)];
    }
    if (paramAlignment == Layout.Alignment.ALIGN_RIGHT)
    {
      if (mRightIndents == null) {
        return 0;
      }
      return -mRightIndents[Math.min(paramInt, mRightIndents.length - 1)];
    }
    if (paramAlignment == Layout.Alignment.ALIGN_CENTER)
    {
      int i = 0;
      if (mLeftIndents != null) {
        i = mLeftIndents[Math.min(paramInt, mLeftIndents.length - 1)];
      }
      int j = 0;
      if (mRightIndents != null) {
        j = mRightIndents[Math.min(paramInt, mRightIndents.length - 1)];
      }
      return i - j >> 1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unhandled alignment ");
    localStringBuilder.append(paramAlignment);
    throw new AssertionError(localStringBuilder.toString());
  }
  
  public boolean getLineContainsTab(int paramInt)
  {
    int[] arrayOfInt = mLines;
    int i = mColumns;
    boolean bool = false;
    if ((arrayOfInt[(i * paramInt + 0)] & 0x20000000) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public int getLineCount()
  {
    return mLineCount;
  }
  
  public int getLineDescent(int paramInt)
  {
    return mLines[(mColumns * paramInt + 2)];
  }
  
  public final Layout.Directions getLineDirections(int paramInt)
  {
    if (paramInt <= getLineCount()) {
      return mLineDirections[paramInt];
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int getLineExtra(int paramInt)
  {
    return mLines[(mColumns * paramInt + 3)];
  }
  
  public int getLineForVertical(int paramInt)
  {
    int i = mLineCount;
    int j = -1;
    int[] arrayOfInt = mLines;
    while (i - j > 1)
    {
      int k = i + j >> 1;
      if (arrayOfInt[(mColumns * k + 1)] > paramInt) {
        i = k;
      } else {
        j = k;
      }
    }
    if (j < 0) {
      return 0;
    }
    return j;
  }
  
  public int getLineStart(int paramInt)
  {
    return mLines[(mColumns * paramInt + 0)] & 0x1FFFFFFF;
  }
  
  public int getLineTop(int paramInt)
  {
    return mLines[(mColumns * paramInt + 1)];
  }
  
  public int getParagraphDirection(int paramInt)
  {
    return mLines[(mColumns * paramInt + 0)] >> 30;
  }
  
  public int getTopPadding()
  {
    return mTopPadding;
  }
  
  public static final class Builder
  {
    private static final Pools.SynchronizedPool<Builder> sPool = new Pools.SynchronizedPool(3);
    private boolean mAddLastLineLineSpacing;
    private Layout.Alignment mAlignment;
    private int mBreakStrategy;
    private TextUtils.TruncateAt mEllipsize;
    private int mEllipsizedWidth;
    private int mEnd;
    private boolean mFallbackLineSpacing;
    private final Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
    private int mHyphenationFrequency;
    private boolean mIncludePad;
    private int mJustificationMode;
    private int[] mLeftIndents;
    private int[] mLeftPaddings;
    private int mMaxLines;
    private TextPaint mPaint;
    private int[] mRightIndents;
    private int[] mRightPaddings;
    private float mSpacingAdd;
    private float mSpacingMult;
    private int mStart;
    private CharSequence mText;
    private TextDirectionHeuristic mTextDir;
    private int mWidth;
    
    private Builder() {}
    
    public static Builder obtain(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextPaint paramTextPaint, int paramInt3)
    {
      Builder localBuilder1 = (Builder)sPool.acquire();
      Builder localBuilder2 = localBuilder1;
      if (localBuilder1 == null) {
        localBuilder2 = new Builder();
      }
      mText = paramCharSequence;
      mStart = paramInt1;
      mEnd = paramInt2;
      mPaint = paramTextPaint;
      mWidth = paramInt3;
      mAlignment = Layout.Alignment.ALIGN_NORMAL;
      mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
      mSpacingMult = 1.0F;
      mSpacingAdd = 0.0F;
      mIncludePad = true;
      mFallbackLineSpacing = false;
      mEllipsizedWidth = paramInt3;
      mEllipsize = null;
      mMaxLines = Integer.MAX_VALUE;
      mBreakStrategy = 0;
      mHyphenationFrequency = 0;
      mJustificationMode = 0;
      return localBuilder2;
    }
    
    private static void recycle(Builder paramBuilder)
    {
      mPaint = null;
      mText = null;
      mLeftIndents = null;
      mRightIndents = null;
      mLeftPaddings = null;
      mRightPaddings = null;
      sPool.release(paramBuilder);
    }
    
    public StaticLayout build()
    {
      StaticLayout localStaticLayout = new StaticLayout(this, null);
      recycle(this);
      return localStaticLayout;
    }
    
    void finish()
    {
      mText = null;
      mPaint = null;
      mLeftIndents = null;
      mRightIndents = null;
      mLeftPaddings = null;
      mRightPaddings = null;
    }
    
    Builder setAddLastLineLineSpacing(boolean paramBoolean)
    {
      mAddLastLineLineSpacing = paramBoolean;
      return this;
    }
    
    public Builder setAlignment(Layout.Alignment paramAlignment)
    {
      mAlignment = paramAlignment;
      return this;
    }
    
    public Builder setAvailablePaddings(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      mLeftPaddings = paramArrayOfInt1;
      mRightPaddings = paramArrayOfInt2;
      return this;
    }
    
    public Builder setBreakStrategy(int paramInt)
    {
      mBreakStrategy = paramInt;
      return this;
    }
    
    public Builder setEllipsize(TextUtils.TruncateAt paramTruncateAt)
    {
      mEllipsize = paramTruncateAt;
      return this;
    }
    
    public Builder setEllipsizedWidth(int paramInt)
    {
      mEllipsizedWidth = paramInt;
      return this;
    }
    
    public Builder setHyphenationFrequency(int paramInt)
    {
      mHyphenationFrequency = paramInt;
      return this;
    }
    
    public Builder setIncludePad(boolean paramBoolean)
    {
      mIncludePad = paramBoolean;
      return this;
    }
    
    public Builder setIndents(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      mLeftIndents = paramArrayOfInt1;
      mRightIndents = paramArrayOfInt2;
      return this;
    }
    
    public Builder setJustificationMode(int paramInt)
    {
      mJustificationMode = paramInt;
      return this;
    }
    
    public Builder setLineSpacing(float paramFloat1, float paramFloat2)
    {
      mSpacingAdd = paramFloat1;
      mSpacingMult = paramFloat2;
      return this;
    }
    
    public Builder setMaxLines(int paramInt)
    {
      mMaxLines = paramInt;
      return this;
    }
    
    public Builder setPaint(TextPaint paramTextPaint)
    {
      mPaint = paramTextPaint;
      return this;
    }
    
    public Builder setText(CharSequence paramCharSequence)
    {
      return setText(paramCharSequence, 0, paramCharSequence.length());
    }
    
    public Builder setText(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      mText = paramCharSequence;
      mStart = paramInt1;
      mEnd = paramInt2;
      return this;
    }
    
    public Builder setTextDirection(TextDirectionHeuristic paramTextDirectionHeuristic)
    {
      mTextDir = paramTextDirectionHeuristic;
      return this;
    }
    
    public Builder setUseLineSpacingFromFallbacks(boolean paramBoolean)
    {
      mFallbackLineSpacing = paramBoolean;
      return this;
    }
    
    public Builder setWidth(int paramInt)
    {
      mWidth = paramInt;
      if (mEllipsize == null) {
        mEllipsizedWidth = paramInt;
      }
      return this;
    }
  }
  
  static class LineBreaks
  {
    private static final int INITIAL_SIZE = 16;
    public float[] ascents = new float[16];
    public int[] breaks = new int[16];
    public float[] descents = new float[16];
    public int[] flags = new int[16];
    public float[] widths = new float[16];
    
    LineBreaks() {}
  }
}
