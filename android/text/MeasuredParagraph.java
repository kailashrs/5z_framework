package android.text;

import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.util.Pools.SynchronizedPool;
import dalvik.annotation.optimization.CriticalNative;
import java.util.Arrays;
import libcore.util.NativeAllocationRegistry;

public class MeasuredParagraph
{
  private static final char OBJECT_REPLACEMENT_CHARACTER = '￼';
  private static final Pools.SynchronizedPool<MeasuredParagraph> sPool = new Pools.SynchronizedPool(1);
  private static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(MeasuredParagraph.class.getClassLoader(), nGetReleaseFunc(), 1024L);
  private Paint.FontMetricsInt mCachedFm;
  private TextPaint mCachedPaint = new TextPaint();
  private char[] mCopiedBuffer;
  private AutoGrowArray.IntArray mFontMetrics = new AutoGrowArray.IntArray(16);
  private AutoGrowArray.ByteArray mLevels = new AutoGrowArray.ByteArray();
  private boolean mLtrWithoutBidi;
  private Runnable mNativeObjectCleaner;
  private long mNativePtr = 0L;
  private int mParaDir;
  private AutoGrowArray.IntArray mSpanEndCache = new AutoGrowArray.IntArray(4);
  private Spanned mSpanned;
  private int mTextLength;
  private int mTextStart;
  private float mWholeWidth;
  private AutoGrowArray.FloatArray mWidths = new AutoGrowArray.FloatArray();
  
  private MeasuredParagraph() {}
  
  private void applyMetricsAffectingSpan(TextPaint paramTextPaint, MetricAffectingSpan[] paramArrayOfMetricAffectingSpan, int paramInt1, int paramInt2, long paramLong)
  {
    mCachedPaint.set(paramTextPaint);
    paramTextPaint = mCachedPaint;
    int i = 0;
    baselineShift = 0;
    int j;
    if (paramLong != 0L) {
      j = 1;
    } else {
      j = 0;
    }
    if ((j != 0) && (mCachedFm == null)) {
      mCachedFm = new Paint.FontMetricsInt();
    }
    Object localObject = null;
    paramTextPaint = null;
    if (paramArrayOfMetricAffectingSpan != null) {
      for (;;)
      {
        localObject = paramTextPaint;
        if (i >= paramArrayOfMetricAffectingSpan.length) {
          break;
        }
        localObject = paramArrayOfMetricAffectingSpan[i];
        if ((localObject instanceof ReplacementSpan)) {
          paramTextPaint = (ReplacementSpan)localObject;
        } else {
          ((MetricAffectingSpan)localObject).updateMeasureState(mCachedPaint);
        }
        i++;
      }
    }
    paramInt1 -= mTextStart;
    paramInt2 -= mTextStart;
    if (paramLong != 0L) {
      mCachedPaint.getFontMetricsInt(mCachedFm);
    }
    if (localObject != null) {
      applyReplacementRun((ReplacementSpan)localObject, paramInt1, paramInt2, paramLong);
    } else {
      applyStyleRun(paramInt1, paramInt2, paramLong);
    }
    if (j != 0)
    {
      if (mCachedPaint.baselineShift < 0)
      {
        paramTextPaint = mCachedFm;
        ascent += mCachedPaint.baselineShift;
        paramTextPaint = mCachedFm;
        top += mCachedPaint.baselineShift;
      }
      else
      {
        paramTextPaint = mCachedFm;
        descent += mCachedPaint.baselineShift;
        paramTextPaint = mCachedFm;
        bottom += mCachedPaint.baselineShift;
      }
      mFontMetrics.append(mCachedFm.top);
      mFontMetrics.append(mCachedFm.bottom);
      mFontMetrics.append(mCachedFm.ascent);
      mFontMetrics.append(mCachedFm.descent);
    }
  }
  
  private void applyReplacementRun(ReplacementSpan paramReplacementSpan, int paramInt1, int paramInt2, long paramLong)
  {
    float f = paramReplacementSpan.getSize(mCachedPaint, mSpanned, paramInt1 + mTextStart, paramInt2 + mTextStart, mCachedFm);
    if (paramLong == 0L)
    {
      mWidths.set(paramInt1, f);
      if (paramInt2 > paramInt1 + 1) {
        Arrays.fill(mWidths.getRawArray(), paramInt1 + 1, paramInt2, 0.0F);
      }
      mWholeWidth += f;
    }
    else
    {
      nAddReplacementRun(paramLong, mCachedPaint.getNativeInstance(), paramInt1, paramInt2, f);
    }
  }
  
  private void applyStyleRun(int paramInt1, int paramInt2, long paramLong)
  {
    int i;
    int j;
    int k;
    if (mLtrWithoutBidi)
    {
      if (paramLong == 0L) {
        mWholeWidth += mCachedPaint.getTextRunAdvances(mCopiedBuffer, paramInt1, paramInt2 - paramInt1, paramInt1, paramInt2 - paramInt1, false, mWidths.getRawArray(), paramInt1);
      } else {
        nAddStyleRun(paramLong, mCachedPaint.getNativeInstance(), paramInt1, paramInt2, false);
      }
    }
    else
    {
      i = mLevels.get(paramInt1);
      j = paramInt1 + 1;
      k = paramInt1;
    }
    for (paramInt1 = j;; paramInt1++)
    {
      if ((paramInt1 != paramInt2) && (mLevels.get(paramInt1) == i)) {
        continue;
      }
      if ((i & 0x1) != 0) {}
      for (boolean bool = true;; bool = false) {
        break;
      }
      if (paramLong == 0L)
      {
        i = paramInt1 - k;
        mWholeWidth += mCachedPaint.getTextRunAdvances(mCopiedBuffer, k, i, k, i, bool, mWidths.getRawArray(), k);
      }
      else
      {
        nAddStyleRun(paramLong, mCachedPaint.getNativeInstance(), k, paramInt1, bool);
      }
      i = paramInt1;
      if (i == paramInt2) {
        return;
      }
      k = i;
      i = mLevels.get(i);
    }
  }
  
  private void bindNativeObject(long paramLong)
  {
    mNativePtr = paramLong;
    mNativeObjectCleaner = sRegistry.registerNativeAllocation(this, paramLong);
  }
  
  public static MeasuredParagraph buildForBidi(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextDirectionHeuristic paramTextDirectionHeuristic, MeasuredParagraph paramMeasuredParagraph)
  {
    if (paramMeasuredParagraph == null) {
      paramMeasuredParagraph = obtain();
    }
    paramMeasuredParagraph.resetAndAnalyzeBidi(paramCharSequence, paramInt1, paramInt2, paramTextDirectionHeuristic);
    return paramMeasuredParagraph;
  }
  
  public static MeasuredParagraph buildForMeasurement(TextPaint paramTextPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, TextDirectionHeuristic paramTextDirectionHeuristic, MeasuredParagraph paramMeasuredParagraph)
  {
    if (paramMeasuredParagraph == null) {
      paramMeasuredParagraph = obtain();
    }
    paramMeasuredParagraph.resetAndAnalyzeBidi(paramCharSequence, paramInt1, paramInt2, paramTextDirectionHeuristic);
    mWidths.resize(mTextLength);
    if (mTextLength == 0) {
      return paramMeasuredParagraph;
    }
    if (mSpanned == null) {
      paramMeasuredParagraph.applyMetricsAffectingSpan(paramTextPaint, null, paramInt1, paramInt2, 0L);
    } else {
      while (paramInt1 < paramInt2)
      {
        int i = mSpanned.nextSpanTransition(paramInt1, paramInt2, MetricAffectingSpan.class);
        paramMeasuredParagraph.applyMetricsAffectingSpan(paramTextPaint, (MetricAffectingSpan[])TextUtils.removeEmptySpans((MetricAffectingSpan[])mSpanned.getSpans(paramInt1, i, MetricAffectingSpan.class), mSpanned, MetricAffectingSpan.class), paramInt1, i, 0L);
        paramInt1 = i;
      }
    }
    return paramMeasuredParagraph;
  }
  
  /* Error */
  public static MeasuredParagraph buildForStaticLayout(TextPaint paramTextPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, TextDirectionHeuristic paramTextDirectionHeuristic, boolean paramBoolean1, boolean paramBoolean2, MeasuredParagraph paramMeasuredParagraph)
  {
    // Byte code:
    //   0: aload 7
    //   2: ifnonnull +11 -> 13
    //   5: invokestatic 203	android/text/MeasuredParagraph:obtain	()Landroid/text/MeasuredParagraph;
    //   8: astore 7
    //   10: goto +3 -> 13
    //   13: aload 7
    //   15: aload_1
    //   16: iload_2
    //   17: iload_3
    //   18: aload 4
    //   20: invokespecial 207	android/text/MeasuredParagraph:resetAndAnalyzeBidi	(Ljava/lang/CharSequence;IILandroid/text/TextDirectionHeuristic;)V
    //   23: aload 7
    //   25: getfield 211	android/text/MeasuredParagraph:mTextLength	I
    //   28: ifne +43 -> 71
    //   31: invokestatic 239	android/text/MeasuredParagraph:nInitBuilder	()J
    //   34: lstore 8
    //   36: aload 7
    //   38: lload 8
    //   40: aload 7
    //   42: getfield 177	android/text/MeasuredParagraph:mCopiedBuffer	[C
    //   45: iload 5
    //   47: iload 6
    //   49: invokestatic 243	android/text/MeasuredParagraph:nBuildNativeMeasuredParagraph	(J[CZZ)J
    //   52: invokespecial 245	android/text/MeasuredParagraph:bindNativeObject	(J)V
    //   55: lload 8
    //   57: invokestatic 248	android/text/MeasuredParagraph:nFreeBuilder	(J)V
    //   60: aload 7
    //   62: areturn
    //   63: astore_0
    //   64: lload 8
    //   66: invokestatic 248	android/text/MeasuredParagraph:nFreeBuilder	(J)V
    //   69: aload_0
    //   70: athrow
    //   71: invokestatic 239	android/text/MeasuredParagraph:nInitBuilder	()J
    //   74: lstore 8
    //   76: aload 7
    //   78: getfield 147	android/text/MeasuredParagraph:mSpanned	Landroid/text/Spanned;
    //   81: astore_1
    //   82: aload_1
    //   83: ifnonnull +30 -> 113
    //   86: aload 7
    //   88: aload_0
    //   89: aconst_null
    //   90: iload_2
    //   91: iload_3
    //   92: lload 8
    //   94: invokespecial 216	android/text/MeasuredParagraph:applyMetricsAffectingSpan	(Landroid/text/TextPaint;[Landroid/text/style/MetricAffectingSpan;IIJ)V
    //   97: aload 7
    //   99: getfield 86	android/text/MeasuredParagraph:mSpanEndCache	Landroid/text/AutoGrowArray$IntArray;
    //   102: iload_3
    //   103: invokevirtual 145	android/text/AutoGrowArray$IntArray:append	(I)V
    //   106: goto +94 -> 200
    //   109: astore_0
    //   110: goto +128 -> 238
    //   113: iload_2
    //   114: istore 10
    //   116: iload 10
    //   118: iload_3
    //   119: if_icmpge +81 -> 200
    //   122: aload 7
    //   124: getfield 147	android/text/MeasuredParagraph:mSpanned	Landroid/text/Spanned;
    //   127: iload 10
    //   129: iload_3
    //   130: ldc 113
    //   132: invokeinterface 222 4 0
    //   137: istore 11
    //   139: aload 7
    //   141: getfield 147	android/text/MeasuredParagraph:mSpanned	Landroid/text/Spanned;
    //   144: iload 10
    //   146: iload 11
    //   148: ldc 113
    //   150: invokeinterface 226 4 0
    //   155: checkcast 228	[Landroid/text/style/MetricAffectingSpan;
    //   158: aload 7
    //   160: getfield 147	android/text/MeasuredParagraph:mSpanned	Landroid/text/Spanned;
    //   163: ldc 113
    //   165: invokestatic 234	android/text/TextUtils:removeEmptySpans	([Ljava/lang/Object;Landroid/text/Spanned;Ljava/lang/Class;)[Ljava/lang/Object;
    //   168: checkcast 228	[Landroid/text/style/MetricAffectingSpan;
    //   171: astore_1
    //   172: iload 11
    //   174: istore_2
    //   175: aload 7
    //   177: aload_0
    //   178: aload_1
    //   179: iload 10
    //   181: iload 11
    //   183: lload 8
    //   185: invokespecial 216	android/text/MeasuredParagraph:applyMetricsAffectingSpan	(Landroid/text/TextPaint;[Landroid/text/style/MetricAffectingSpan;IIJ)V
    //   188: aload 7
    //   190: getfield 86	android/text/MeasuredParagraph:mSpanEndCache	Landroid/text/AutoGrowArray$IntArray;
    //   193: iload_2
    //   194: invokevirtual 145	android/text/AutoGrowArray$IntArray:append	(I)V
    //   197: goto -84 -> 113
    //   200: aload 7
    //   202: getfield 177	android/text/MeasuredParagraph:mCopiedBuffer	[C
    //   205: astore_0
    //   206: aload 7
    //   208: lload 8
    //   210: aload_0
    //   211: iload 5
    //   213: iload 6
    //   215: invokestatic 243	android/text/MeasuredParagraph:nBuildNativeMeasuredParagraph	(J[CZZ)J
    //   218: invokespecial 245	android/text/MeasuredParagraph:bindNativeObject	(J)V
    //   221: lload 8
    //   223: invokestatic 248	android/text/MeasuredParagraph:nFreeBuilder	(J)V
    //   226: aload 7
    //   228: areturn
    //   229: astore_0
    //   230: goto +8 -> 238
    //   233: astore_0
    //   234: goto +4 -> 238
    //   237: astore_0
    //   238: lload 8
    //   240: invokestatic 248	android/text/MeasuredParagraph:nFreeBuilder	(J)V
    //   243: aload_0
    //   244: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	245	0	paramTextPaint	TextPaint
    //   0	245	1	paramCharSequence	CharSequence
    //   0	245	2	paramInt1	int
    //   0	245	3	paramInt2	int
    //   0	245	4	paramTextDirectionHeuristic	TextDirectionHeuristic
    //   0	245	5	paramBoolean1	boolean
    //   0	245	6	paramBoolean2	boolean
    //   0	245	7	paramMeasuredParagraph	MeasuredParagraph
    //   34	205	8	l	long
    //   114	66	10	i	int
    //   137	45	11	j	int
    // Exception table:
    //   from	to	target	type
    //   36	55	63	finally
    //   86	106	109	finally
    //   122	172	109	finally
    //   175	197	109	finally
    //   206	221	229	finally
    //   200	206	233	finally
    //   76	82	237	finally
  }
  
  private static native void nAddReplacementRun(long paramLong1, long paramLong2, int paramInt1, int paramInt2, float paramFloat);
  
  private static native void nAddStyleRun(long paramLong1, long paramLong2, int paramInt1, int paramInt2, boolean paramBoolean);
  
  private static native long nBuildNativeMeasuredParagraph(long paramLong, char[] paramArrayOfChar, boolean paramBoolean1, boolean paramBoolean2);
  
  private static native void nFreeBuilder(long paramLong);
  
  private static native void nGetBounds(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2, Rect paramRect);
  
  @CriticalNative
  private static native int nGetMemoryUsage(long paramLong);
  
  @CriticalNative
  private static native long nGetReleaseFunc();
  
  @CriticalNative
  private static native float nGetWidth(long paramLong, int paramInt1, int paramInt2);
  
  private static native long nInitBuilder();
  
  private static MeasuredParagraph obtain()
  {
    MeasuredParagraph localMeasuredParagraph = (MeasuredParagraph)sPool.acquire();
    if (localMeasuredParagraph == null) {
      localMeasuredParagraph = new MeasuredParagraph();
    }
    return localMeasuredParagraph;
  }
  
  private void reset()
  {
    mSpanned = null;
    mCopiedBuffer = null;
    mWholeWidth = 0.0F;
    mLevels.clear();
    mWidths.clear();
    mFontMetrics.clear();
    mSpanEndCache.clear();
    unbindNativeObject();
  }
  
  private void resetAndAnalyzeBidi(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextDirectionHeuristic paramTextDirectionHeuristic)
  {
    reset();
    Spanned localSpanned;
    if ((paramCharSequence instanceof Spanned)) {
      localSpanned = (Spanned)paramCharSequence;
    } else {
      localSpanned = null;
    }
    mSpanned = localSpanned;
    mTextStart = paramInt1;
    mTextLength = (paramInt2 - paramInt1);
    if ((mCopiedBuffer == null) || (mCopiedBuffer.length != mTextLength)) {
      mCopiedBuffer = new char[mTextLength];
    }
    TextUtils.getChars(paramCharSequence, paramInt1, paramInt2, mCopiedBuffer, 0);
    if (mSpanned != null)
    {
      paramCharSequence = (ReplacementSpan[])mSpanned.getSpans(paramInt1, paramInt2, ReplacementSpan.class);
      for (paramInt2 = 0; paramInt2 < paramCharSequence.length; paramInt2++)
      {
        int i = mSpanned.getSpanStart(paramCharSequence[paramInt2]) - paramInt1;
        int j = mSpanned.getSpanEnd(paramCharSequence[paramInt2]) - paramInt1;
        int k = i;
        if (i < 0) {
          k = 0;
        }
        i = j;
        if (j > mTextLength) {
          i = mTextLength;
        }
        Arrays.fill(mCopiedBuffer, k, i, 65532);
      }
    }
    paramCharSequence = TextDirectionHeuristics.LTR;
    paramInt1 = 1;
    if (((paramTextDirectionHeuristic == paramCharSequence) || (paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) || (paramTextDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR)) && (TextUtils.doesNotNeedBidi(mCopiedBuffer, 0, mTextLength)))
    {
      mLevels.clear();
      mParaDir = 1;
      mLtrWithoutBidi = true;
    }
    else
    {
      if (paramTextDirectionHeuristic == TextDirectionHeuristics.LTR) {
        paramInt1 = 1;
      }
      for (;;)
      {
        break;
        if (paramTextDirectionHeuristic == TextDirectionHeuristics.RTL) {
          paramInt1 = -1;
        } else if (paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
          paramInt1 = 2;
        } else if (paramTextDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
          paramInt1 = -2;
        } else if (paramTextDirectionHeuristic.isRtl(mCopiedBuffer, 0, mTextLength)) {
          paramInt1 = -1;
        }
      }
      mLevels.resize(mTextLength);
      mParaDir = AndroidBidi.bidi(paramInt1, mCopiedBuffer, mLevels.getRawArray());
      mLtrWithoutBidi = false;
    }
  }
  
  private void unbindNativeObject()
  {
    if (mNativePtr != 0L)
    {
      mNativeObjectCleaner.run();
      mNativePtr = 0L;
    }
  }
  
  int breakText(int paramInt, boolean paramBoolean, float paramFloat)
  {
    float[] arrayOfFloat = mWidths.getRawArray();
    int j;
    if (paramBoolean)
    {
      for (i = 0;; i++)
      {
        j = i;
        if (i >= paramInt) {
          break;
        }
        paramFloat -= arrayOfFloat[i];
        if (paramFloat < 0.0F)
        {
          j = i;
          break;
        }
      }
      while ((j > 0) && (mCopiedBuffer[(j - 1)] == ' ')) {
        j--;
      }
      return j;
    }
    for (int i = paramInt - 1;; i--)
    {
      j = i;
      if (i < 0) {
        break;
      }
      paramFloat -= arrayOfFloat[i];
      if (paramFloat < 0.0F)
      {
        j = i;
        break;
      }
    }
    while ((j < paramInt - 1) && ((mCopiedBuffer[(j + 1)] == ' ') || (arrayOfFloat[(j + 1)] == 0.0F))) {
      j++;
    }
    return paramInt - j - 1;
  }
  
  public void getBounds(int paramInt1, int paramInt2, Rect paramRect)
  {
    nGetBounds(mNativePtr, mCopiedBuffer, paramInt1, paramInt2, paramRect);
  }
  
  public char[] getChars()
  {
    return mCopiedBuffer;
  }
  
  public Layout.Directions getDirections(int paramInt1, int paramInt2)
  {
    if (mLtrWithoutBidi) {
      return Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }
    return AndroidBidi.directions(mParaDir, mLevels.getRawArray(), paramInt1, mCopiedBuffer, paramInt1, paramInt2 - paramInt1);
  }
  
  public AutoGrowArray.IntArray getFontMetrics()
  {
    return mFontMetrics;
  }
  
  public int getMemoryUsage()
  {
    return nGetMemoryUsage(mNativePtr);
  }
  
  public long getNativePtr()
  {
    return mNativePtr;
  }
  
  public int getParagraphDir()
  {
    return mParaDir;
  }
  
  public AutoGrowArray.IntArray getSpanEndCache()
  {
    return mSpanEndCache;
  }
  
  public int getTextLength()
  {
    return mTextLength;
  }
  
  public float getWholeWidth()
  {
    return mWholeWidth;
  }
  
  public float getWidth(int paramInt1, int paramInt2)
  {
    if (mNativePtr == 0L)
    {
      float[] arrayOfFloat = mWidths.getRawArray();
      float f = 0.0F;
      while (paramInt1 < paramInt2)
      {
        f += arrayOfFloat[paramInt1];
        paramInt1++;
      }
      return f;
    }
    return nGetWidth(mNativePtr, paramInt1, paramInt2);
  }
  
  public AutoGrowArray.FloatArray getWidths()
  {
    return mWidths;
  }
  
  float measure(int paramInt1, int paramInt2)
  {
    float[] arrayOfFloat = mWidths.getRawArray();
    float f = 0.0F;
    while (paramInt1 < paramInt2)
    {
      f += arrayOfFloat[paramInt1];
      paramInt1++;
    }
    return f;
  }
  
  public void recycle()
  {
    release();
    sPool.release(this);
  }
  
  public void release()
  {
    reset();
    mLevels.clearWithReleasingLargeArray();
    mWidths.clearWithReleasingLargeArray();
    mFontMetrics.clearWithReleasingLargeArray();
    mSpanEndCache.clearWithReleasingLargeArray();
  }
}
