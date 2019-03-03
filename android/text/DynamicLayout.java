package android.text;

import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateLayout;
import android.util.ArraySet;
import android.util.Pools.SynchronizedPool;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.ref.WeakReference;

public class DynamicLayout
  extends Layout
{
  private static final int BLOCK_MINIMUM_CHARACTER_LENGTH = 400;
  private static final int COLUMNS_ELLIPSIZE = 7;
  private static final int COLUMNS_NORMAL = 5;
  private static final int DESCENT = 2;
  private static final int DIR = 0;
  private static final int DIR_SHIFT = 30;
  private static final int ELLIPSIS_COUNT = 6;
  private static final int ELLIPSIS_START = 5;
  private static final int ELLIPSIS_UNDEFINED = Integer.MIN_VALUE;
  private static final int EXTRA = 3;
  private static final int HYPHEN = 4;
  private static final int HYPHEN_MASK = 255;
  public static final int INVALID_BLOCK_INDEX = -1;
  private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM = 4;
  private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM_MASK = 256;
  private static final int PRIORITY = 128;
  private static final int START = 0;
  private static final int START_MASK = 536870911;
  private static final int TAB = 0;
  private static final int TAB_MASK = 536870912;
  private static final int TOP = 1;
  private static StaticLayout.Builder sBuilder = null;
  private static final Object[] sLock = new Object[0];
  private static StaticLayout sStaticLayout = null;
  private CharSequence mBase;
  private int[] mBlockEndLines;
  private int[] mBlockIndices;
  private ArraySet<Integer> mBlocksAlwaysNeedToBeRedrawn;
  private int mBottomPadding;
  private int mBreakStrategy;
  private CharSequence mDisplay;
  private boolean mEllipsize;
  private TextUtils.TruncateAt mEllipsizeAt;
  private int mEllipsizedWidth;
  private boolean mFallbackLineSpacing;
  private int mHyphenationFrequency;
  private boolean mIncludePad;
  private int mIndexFirstChangedBlock;
  private PackedIntVector mInts;
  private int mJustificationMode;
  private int mNumberOfBlocks;
  private PackedObjectVector<Layout.Directions> mObjects;
  private Rect mTempRect = new Rect();
  private int mTopPadding;
  private ChangeWatcher mWatcher;
  
  private DynamicLayout(Builder paramBuilder)
  {
    super(createEllipsizer(mEllipsize, mDisplay), mPaint, mWidth, mAlignment, mTextDir, mSpacingMult, mSpacingAdd);
    mDisplay = mDisplay;
    mIncludePad = mIncludePad;
    mBreakStrategy = mBreakStrategy;
    mJustificationMode = mJustificationMode;
    mHyphenationFrequency = mHyphenationFrequency;
    generate(paramBuilder);
  }
  
  @Deprecated
  public DynamicLayout(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this(paramCharSequence, paramCharSequence, paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2, paramBoolean);
  }
  
  @Deprecated
  public DynamicLayout(CharSequence paramCharSequence1, CharSequence paramCharSequence2, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this(paramCharSequence1, paramCharSequence2, paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2, paramBoolean, null, 0);
  }
  
  @Deprecated
  public DynamicLayout(CharSequence paramCharSequence1, CharSequence paramCharSequence2, TextPaint paramTextPaint, int paramInt1, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt2)
  {
    this(paramCharSequence1, paramCharSequence2, paramTextPaint, paramInt1, paramAlignment, TextDirectionHeuristics.FIRSTSTRONG_LTR, paramFloat1, paramFloat2, paramBoolean, 0, 0, 0, paramTruncateAt, paramInt2);
  }
  
  @Deprecated
  public DynamicLayout(CharSequence paramCharSequence1, CharSequence paramCharSequence2, TextPaint paramTextPaint, int paramInt1, Layout.Alignment paramAlignment, TextDirectionHeuristic paramTextDirectionHeuristic, float paramFloat1, float paramFloat2, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, TextUtils.TruncateAt paramTruncateAt, int paramInt5)
  {
    super(createEllipsizer(paramTruncateAt, paramCharSequence2), paramTextPaint, paramInt1, paramAlignment, paramTextDirectionHeuristic, paramFloat1, paramFloat2);
    paramCharSequence1 = Builder.obtain(paramCharSequence1, paramTextPaint, paramInt1).setAlignment(paramAlignment).setTextDirection(paramTextDirectionHeuristic).setLineSpacing(paramFloat2, paramFloat1).setEllipsizedWidth(paramInt5).setEllipsize(paramTruncateAt);
    mDisplay = paramCharSequence2;
    mIncludePad = paramBoolean;
    mBreakStrategy = paramInt2;
    mJustificationMode = paramInt4;
    mHyphenationFrequency = paramInt3;
    generate(paramCharSequence1);
    Builder.recycle(paramCharSequence1);
  }
  
  private void addBlockAtOffset(int paramInt)
  {
    paramInt = getLineForOffset(paramInt);
    if (mBlockEndLines == null)
    {
      mBlockEndLines = ArrayUtils.newUnpaddedIntArray(1);
      mBlockEndLines[mNumberOfBlocks] = paramInt;
      updateAlwaysNeedsToBeRedrawn(mNumberOfBlocks);
      mNumberOfBlocks += 1;
      return;
    }
    if (paramInt > mBlockEndLines[(mNumberOfBlocks - 1)])
    {
      mBlockEndLines = GrowingArrayUtils.append(mBlockEndLines, mNumberOfBlocks, paramInt);
      updateAlwaysNeedsToBeRedrawn(mNumberOfBlocks);
      mNumberOfBlocks += 1;
    }
  }
  
  private boolean contentMayProtrudeFromLineTopOrBottom(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    boolean bool1 = paramCharSequence instanceof Spanned;
    boolean bool2 = true;
    if ((bool1) && (((ReplacementSpan[])((Spanned)paramCharSequence).getSpans(paramInt1, paramInt2, ReplacementSpan.class)).length > 0)) {
      return true;
    }
    TextPaint localTextPaint = getPaint();
    if ((paramCharSequence instanceof PrecomputedText)) {
      ((PrecomputedText)paramCharSequence).getBounds(paramInt1, paramInt2, mTempRect);
    } else {
      localTextPaint.getTextBounds(paramCharSequence, paramInt1, paramInt2, mTempRect);
    }
    paramCharSequence = localTextPaint.getFontMetricsInt();
    bool1 = bool2;
    if (mTempRect.top >= top) {
      if (mTempRect.bottom > bottom) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  private void createBlocks()
  {
    int i = 400;
    int j = 0;
    mNumberOfBlocks = 0;
    CharSequence localCharSequence = mDisplay;
    for (;;)
    {
      i = TextUtils.indexOf(localCharSequence, '\n', i);
      if (i < 0)
      {
        addBlockAtOffset(localCharSequence.length());
        mBlockIndices = new int[mBlockEndLines.length];
        for (i = j; i < mBlockEndLines.length; i++) {
          mBlockIndices[i] = -1;
        }
        return;
      }
      addBlockAtOffset(i);
      i += 400;
    }
  }
  
  private static CharSequence createEllipsizer(TextUtils.TruncateAt paramTruncateAt, CharSequence paramCharSequence)
  {
    if (paramTruncateAt == null) {
      return paramCharSequence;
    }
    if ((paramCharSequence instanceof Spanned)) {
      return new Layout.SpannedEllipsizer(paramCharSequence);
    }
    return new Layout.Ellipsizer(paramCharSequence);
  }
  
  private void generate(Builder paramBuilder)
  {
    mBase = mBase;
    mFallbackLineSpacing = mFallbackLineSpacing;
    Object localObject;
    if (mEllipsize != null)
    {
      mInts = new PackedIntVector(7);
      mEllipsizedWidth = mEllipsizedWidth;
      mEllipsizeAt = mEllipsize;
      localObject = (Layout.Ellipsizer)getText();
      mLayout = this;
      mWidth = mEllipsizedWidth;
      mMethod = mEllipsize;
      mEllipsize = true;
    }
    else
    {
      mInts = new PackedIntVector(5);
      mEllipsizedWidth = mWidth;
      mEllipsizeAt = null;
    }
    mObjects = new PackedObjectVector(1);
    if (mEllipsize != null)
    {
      localObject = new int[7];
      localObject[5] = Integer.MIN_VALUE;
    }
    else
    {
      localObject = new int[5];
    }
    Layout.Directions localDirections = DIRS_ALL_LEFT_TO_RIGHT;
    Paint.FontMetricsInt localFontMetricsInt = mFontMetricsInt;
    mPaint.getFontMetricsInt(localFontMetricsInt);
    int i = ascent;
    int j = descent;
    localObject[0] = 1073741824;
    localObject[1] = 0;
    localObject[2] = j;
    mInts.insertAt(0, (int[])localObject);
    localObject[1] = (j - i);
    mInts.insertAt(1, (int[])localObject);
    mObjects.insertAt(0, new Layout.Directions[] { localDirections });
    j = mBase.length();
    reflow(mBase, 0, 0, j);
    if ((mBase instanceof Spannable))
    {
      if (mWatcher == null) {
        mWatcher = new ChangeWatcher(this);
      }
      paramBuilder = (Spannable)mBase;
      localObject = (ChangeWatcher[])paramBuilder.getSpans(0, j, ChangeWatcher.class);
      for (i = 0; i < localObject.length; i++) {
        paramBuilder.removeSpan(localObject[i]);
      }
      paramBuilder.setSpan(mWatcher, 0, j, 8388626);
    }
  }
  
  private boolean getContentMayProtrudeFromTopOrBottom(int paramInt)
  {
    boolean bool;
    if ((mInts.getValue(paramInt, 4) & 0x100) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void updateAlwaysNeedsToBeRedrawn(int paramInt)
  {
    int i;
    if (paramInt == 0) {
      i = 0;
    } else {
      i = mBlockEndLines[(paramInt - 1)] + 1;
    }
    int j = mBlockEndLines[paramInt];
    while (i <= j)
    {
      if (getContentMayProtrudeFromTopOrBottom(i))
      {
        if (mBlocksAlwaysNeedToBeRedrawn == null) {
          mBlocksAlwaysNeedToBeRedrawn = new ArraySet();
        }
        mBlocksAlwaysNeedToBeRedrawn.add(Integer.valueOf(paramInt));
        return;
      }
      i++;
    }
    if (mBlocksAlwaysNeedToBeRedrawn != null) {
      mBlocksAlwaysNeedToBeRedrawn.remove(Integer.valueOf(paramInt));
    }
  }
  
  public int[] getBlockEndLines()
  {
    return mBlockEndLines;
  }
  
  public int getBlockIndex(int paramInt)
  {
    return mBlockIndices[paramInt];
  }
  
  public int[] getBlockIndices()
  {
    return mBlockIndices;
  }
  
  public ArraySet<Integer> getBlocksAlwaysNeedToBeRedrawn()
  {
    return mBlocksAlwaysNeedToBeRedrawn;
  }
  
  public int getBottomPadding()
  {
    return mBottomPadding;
  }
  
  public int getEllipsisCount(int paramInt)
  {
    if (mEllipsizeAt == null) {
      return 0;
    }
    return mInts.getValue(paramInt, 6);
  }
  
  public int getEllipsisStart(int paramInt)
  {
    if (mEllipsizeAt == null) {
      return 0;
    }
    return mInts.getValue(paramInt, 5);
  }
  
  public int getEllipsizedWidth()
  {
    return mEllipsizedWidth;
  }
  
  public int getHyphen(int paramInt)
  {
    return mInts.getValue(paramInt, 4) & 0xFF;
  }
  
  public int getIndexFirstChangedBlock()
  {
    return mIndexFirstChangedBlock;
  }
  
  public boolean getLineContainsTab(int paramInt)
  {
    PackedIntVector localPackedIntVector = mInts;
    boolean bool = false;
    if ((localPackedIntVector.getValue(paramInt, 0) & 0x20000000) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public int getLineCount()
  {
    return mInts.size() - 1;
  }
  
  public int getLineDescent(int paramInt)
  {
    return mInts.getValue(paramInt, 2);
  }
  
  public final Layout.Directions getLineDirections(int paramInt)
  {
    return (Layout.Directions)mObjects.getValue(paramInt, 0);
  }
  
  public int getLineExtra(int paramInt)
  {
    return mInts.getValue(paramInt, 3);
  }
  
  public int getLineStart(int paramInt)
  {
    return mInts.getValue(paramInt, 0) & 0x1FFFFFFF;
  }
  
  public int getLineTop(int paramInt)
  {
    return mInts.getValue(paramInt, 1);
  }
  
  public int getNumberOfBlocks()
  {
    return mNumberOfBlocks;
  }
  
  public int getParagraphDirection(int paramInt)
  {
    return mInts.getValue(paramInt, 0) >> 30;
  }
  
  public int getTopPadding()
  {
    return mTopPadding;
  }
  
  /* Error */
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void reflow(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield 317	android/text/DynamicLayout:mBase	Ljava/lang/CharSequence;
    //   5: if_acmpeq +4 -> 9
    //   8: return
    //   9: aload_0
    //   10: getfield 148	android/text/DynamicLayout:mDisplay	Ljava/lang/CharSequence;
    //   13: astore 5
    //   15: aload 5
    //   17: invokeinterface 300 1 0
    //   22: istore 6
    //   24: aload 5
    //   26: bipush 10
    //   28: iload_2
    //   29: iconst_1
    //   30: isub
    //   31: invokestatic 478	android/text/TextUtils:lastIndexOf	(Ljava/lang/CharSequence;CI)I
    //   34: istore 7
    //   36: iload 7
    //   38: ifge +9 -> 47
    //   41: iconst_0
    //   42: istore 7
    //   44: goto +6 -> 50
    //   47: iinc 7 1
    //   50: iload_2
    //   51: iload 7
    //   53: isub
    //   54: istore 7
    //   56: iload 4
    //   58: iload 7
    //   60: iadd
    //   61: istore 8
    //   63: iload_2
    //   64: iload 7
    //   66: isub
    //   67: istore_2
    //   68: aload 5
    //   70: bipush 10
    //   72: iload_2
    //   73: iload 8
    //   75: iadd
    //   76: invokestatic 294	android/text/TextUtils:indexOf	(Ljava/lang/CharSequence;CI)I
    //   79: istore 4
    //   81: iload 4
    //   83: ifge +10 -> 93
    //   86: iload 6
    //   88: istore 4
    //   90: goto +6 -> 96
    //   93: iinc 4 1
    //   96: iload 4
    //   98: iload_2
    //   99: iload 8
    //   101: iadd
    //   102: isub
    //   103: istore 9
    //   105: iload_3
    //   106: iload 7
    //   108: iadd
    //   109: iload 9
    //   111: iadd
    //   112: istore 4
    //   114: iload 8
    //   116: iload 9
    //   118: iadd
    //   119: istore_3
    //   120: aload 5
    //   122: instanceof 249
    //   125: ifeq +207 -> 332
    //   128: aload 5
    //   130: checkcast 249	android/text/Spanned
    //   133: astore_1
    //   134: iconst_0
    //   135: istore 8
    //   137: aload_1
    //   138: iload_2
    //   139: iload_2
    //   140: iload_3
    //   141: iadd
    //   142: ldc_w 480
    //   145: invokeinterface 255 4 0
    //   150: astore 10
    //   152: iload 4
    //   154: istore 7
    //   156: iload_2
    //   157: istore 4
    //   159: iconst_0
    //   160: istore 11
    //   162: iload 7
    //   164: istore_2
    //   165: iload 11
    //   167: aload 10
    //   169: arraylength
    //   170: if_icmpge +131 -> 301
    //   173: aload_1
    //   174: aload 10
    //   176: iload 11
    //   178: aaload
    //   179: invokeinterface 484 2 0
    //   184: istore 12
    //   186: aload_1
    //   187: aload 10
    //   189: iload 11
    //   191: aaload
    //   192: invokeinterface 487 2 0
    //   197: istore 13
    //   199: iload 4
    //   201: istore 14
    //   203: iload_3
    //   204: istore 7
    //   206: iload 8
    //   208: istore 9
    //   210: iload_2
    //   211: istore 8
    //   213: iload 12
    //   215: iload 4
    //   217: if_icmpge +32 -> 249
    //   220: iconst_1
    //   221: istore 9
    //   223: iload 4
    //   225: iload 12
    //   227: isub
    //   228: istore 14
    //   230: iload_2
    //   231: iload 14
    //   233: iadd
    //   234: istore 8
    //   236: iload_3
    //   237: iload 14
    //   239: iadd
    //   240: istore 7
    //   242: iload 4
    //   244: iload 14
    //   246: isub
    //   247: istore 14
    //   249: iload 7
    //   251: istore_3
    //   252: iload 8
    //   254: istore_2
    //   255: iload 13
    //   257: iload 14
    //   259: iload 7
    //   261: iadd
    //   262: if_icmple +25 -> 287
    //   265: iload 13
    //   267: iload 14
    //   269: iload 7
    //   271: iadd
    //   272: isub
    //   273: istore_3
    //   274: iload 8
    //   276: iload_3
    //   277: iadd
    //   278: istore_2
    //   279: iload 7
    //   281: iload_3
    //   282: iadd
    //   283: istore_3
    //   284: iconst_1
    //   285: istore 9
    //   287: iinc 11 1
    //   290: iload 14
    //   292: istore 4
    //   294: iload 9
    //   296: istore 8
    //   298: goto -133 -> 165
    //   301: iload 8
    //   303: ifne +16 -> 319
    //   306: iload 4
    //   308: istore 7
    //   310: iload_3
    //   311: istore 8
    //   313: iload_2
    //   314: istore 13
    //   316: goto +26 -> 342
    //   319: iload 4
    //   321: istore 7
    //   323: iload_2
    //   324: istore 4
    //   326: iload 7
    //   328: istore_2
    //   329: goto -195 -> 134
    //   332: iload 4
    //   334: istore 13
    //   336: iload_3
    //   337: istore 8
    //   339: iload_2
    //   340: istore 7
    //   342: aload_0
    //   343: iload 7
    //   345: invokevirtual 226	android/text/DynamicLayout:getLineForOffset	(I)I
    //   348: istore 15
    //   350: aload_0
    //   351: iload 15
    //   353: invokevirtual 489	android/text/DynamicLayout:getLineTop	(I)I
    //   356: istore 16
    //   358: aload_0
    //   359: iload 7
    //   361: iload 13
    //   363: iadd
    //   364: invokevirtual 226	android/text/DynamicLayout:getLineForOffset	(I)I
    //   367: istore 9
    //   369: iload 7
    //   371: iload 8
    //   373: iadd
    //   374: iload 6
    //   376: if_icmpne +9 -> 385
    //   379: aload_0
    //   380: invokevirtual 491	android/text/DynamicLayout:getLineCount	()I
    //   383: istore 9
    //   385: aload_0
    //   386: iload 9
    //   388: invokevirtual 489	android/text/DynamicLayout:getLineTop	(I)I
    //   391: istore 17
    //   393: iload 9
    //   395: aload_0
    //   396: invokevirtual 491	android/text/DynamicLayout:getLineCount	()I
    //   399: if_icmpne +9 -> 408
    //   402: iconst_1
    //   403: istore 12
    //   405: goto +6 -> 411
    //   408: iconst_0
    //   409: istore 12
    //   411: getstatic 99	android/text/DynamicLayout:sLock	[Ljava/lang/Object;
    //   414: astore 18
    //   416: aload 18
    //   418: monitorenter
    //   419: getstatic 93	android/text/DynamicLayout:sStaticLayout	Landroid/text/StaticLayout;
    //   422: astore_1
    //   423: getstatic 95	android/text/DynamicLayout:sBuilder	Landroid/text/StaticLayout$Builder;
    //   426: astore 10
    //   428: aconst_null
    //   429: putstatic 93	android/text/DynamicLayout:sStaticLayout	Landroid/text/StaticLayout;
    //   432: aconst_null
    //   433: putstatic 95	android/text/DynamicLayout:sBuilder	Landroid/text/StaticLayout$Builder;
    //   436: aload 18
    //   438: monitorexit
    //   439: aload_1
    //   440: ifnonnull +37 -> 477
    //   443: new 493	android/text/StaticLayout
    //   446: dup
    //   447: aconst_null
    //   448: invokespecial 494	android/text/StaticLayout:<init>	(Ljava/lang/CharSequence;)V
    //   451: astore_1
    //   452: aload 5
    //   454: iload 7
    //   456: iload 7
    //   458: iload 8
    //   460: iadd
    //   461: aload_0
    //   462: invokevirtual 261	android/text/DynamicLayout:getPaint	()Landroid/text/TextPaint;
    //   465: aload_0
    //   466: invokevirtual 497	android/text/DynamicLayout:getWidth	()I
    //   469: invokestatic 502	android/text/StaticLayout$Builder:obtain	(Ljava/lang/CharSequence;IILandroid/text/TextPaint;I)Landroid/text/StaticLayout$Builder;
    //   472: astore 10
    //   474: goto +3 -> 477
    //   477: aload 10
    //   479: aload 5
    //   481: iload 7
    //   483: iload 7
    //   485: iload 8
    //   487: iadd
    //   488: invokevirtual 506	android/text/StaticLayout$Builder:setText	(Ljava/lang/CharSequence;II)Landroid/text/StaticLayout$Builder;
    //   491: aload_0
    //   492: invokevirtual 261	android/text/DynamicLayout:getPaint	()Landroid/text/TextPaint;
    //   495: invokevirtual 510	android/text/StaticLayout$Builder:setPaint	(Landroid/text/TextPaint;)Landroid/text/StaticLayout$Builder;
    //   498: aload_0
    //   499: invokevirtual 497	android/text/DynamicLayout:getWidth	()I
    //   502: invokevirtual 514	android/text/StaticLayout$Builder:setWidth	(I)Landroid/text/StaticLayout$Builder;
    //   505: aload_0
    //   506: invokevirtual 518	android/text/DynamicLayout:getTextDirectionHeuristic	()Landroid/text/TextDirectionHeuristic;
    //   509: invokevirtual 521	android/text/StaticLayout$Builder:setTextDirection	(Landroid/text/TextDirectionHeuristic;)Landroid/text/StaticLayout$Builder;
    //   512: aload_0
    //   513: invokevirtual 525	android/text/DynamicLayout:getSpacingAdd	()F
    //   516: aload_0
    //   517: invokevirtual 528	android/text/DynamicLayout:getSpacingMultiplier	()F
    //   520: invokevirtual 531	android/text/StaticLayout$Builder:setLineSpacing	(FF)Landroid/text/StaticLayout$Builder;
    //   523: aload_0
    //   524: getfield 322	android/text/DynamicLayout:mFallbackLineSpacing	Z
    //   527: invokevirtual 535	android/text/StaticLayout$Builder:setUseLineSpacingFromFallbacks	(Z)Landroid/text/StaticLayout$Builder;
    //   530: aload_0
    //   531: getfield 333	android/text/DynamicLayout:mEllipsizedWidth	I
    //   534: invokevirtual 537	android/text/StaticLayout$Builder:setEllipsizedWidth	(I)Landroid/text/StaticLayout$Builder;
    //   537: aload_0
    //   538: getfield 335	android/text/DynamicLayout:mEllipsizeAt	Landroid/text/TextUtils$TruncateAt;
    //   541: invokevirtual 540	android/text/StaticLayout$Builder:setEllipsize	(Landroid/text/TextUtils$TruncateAt;)Landroid/text/StaticLayout$Builder;
    //   544: aload_0
    //   545: getfield 159	android/text/DynamicLayout:mBreakStrategy	I
    //   548: invokevirtual 543	android/text/StaticLayout$Builder:setBreakStrategy	(I)Landroid/text/StaticLayout$Builder;
    //   551: aload_0
    //   552: getfield 169	android/text/DynamicLayout:mHyphenationFrequency	I
    //   555: invokevirtual 546	android/text/StaticLayout$Builder:setHyphenationFrequency	(I)Landroid/text/StaticLayout$Builder;
    //   558: aload_0
    //   559: getfield 164	android/text/DynamicLayout:mJustificationMode	I
    //   562: invokevirtual 549	android/text/StaticLayout$Builder:setJustificationMode	(I)Landroid/text/StaticLayout$Builder;
    //   565: astore 18
    //   567: iload 12
    //   569: ifne +9 -> 578
    //   572: iconst_1
    //   573: istore 19
    //   575: goto +6 -> 581
    //   578: iconst_0
    //   579: istore 19
    //   581: aload 18
    //   583: iload 19
    //   585: invokevirtual 552	android/text/StaticLayout$Builder:setAddLastLineLineSpacing	(Z)Landroid/text/StaticLayout$Builder;
    //   588: pop
    //   589: aload_1
    //   590: aload 10
    //   592: iconst_0
    //   593: iconst_1
    //   594: invokevirtual 555	android/text/StaticLayout:generate	(Landroid/text/StaticLayout$Builder;ZZ)V
    //   597: aload_1
    //   598: invokevirtual 556	android/text/StaticLayout:getLineCount	()I
    //   601: istore_2
    //   602: iload_2
    //   603: istore 11
    //   605: iload 7
    //   607: iload 8
    //   609: iadd
    //   610: iload 6
    //   612: if_icmpeq +26 -> 638
    //   615: iload_2
    //   616: istore 11
    //   618: aload_1
    //   619: iload_2
    //   620: iconst_1
    //   621: isub
    //   622: invokevirtual 558	android/text/StaticLayout:getLineStart	(I)I
    //   625: iload 7
    //   627: iload 8
    //   629: iadd
    //   630: if_icmpne +8 -> 638
    //   633: iload_2
    //   634: iconst_1
    //   635: isub
    //   636: istore 11
    //   638: aload_0
    //   639: getfield 328	android/text/DynamicLayout:mInts	Landroid/text/PackedIntVector;
    //   642: iload 15
    //   644: iload 9
    //   646: iload 15
    //   648: isub
    //   649: invokevirtual 562	android/text/PackedIntVector:deleteAt	(II)V
    //   652: aload_0
    //   653: getfield 356	android/text/DynamicLayout:mObjects	Landroid/text/PackedObjectVector;
    //   656: iload 15
    //   658: iload 9
    //   660: iload 15
    //   662: isub
    //   663: invokevirtual 563	android/text/PackedObjectVector:deleteAt	(II)V
    //   666: aload_1
    //   667: iload 11
    //   669: invokevirtual 564	android/text/StaticLayout:getLineTop	(I)I
    //   672: istore 4
    //   674: iconst_0
    //   675: istore 14
    //   677: iconst_0
    //   678: istore 6
    //   680: iload 4
    //   682: istore_3
    //   683: iload 14
    //   685: istore_2
    //   686: aload_0
    //   687: getfield 154	android/text/DynamicLayout:mIncludePad	Z
    //   690: ifeq +29 -> 719
    //   693: iload 4
    //   695: istore_3
    //   696: iload 14
    //   698: istore_2
    //   699: iload 15
    //   701: ifne +18 -> 719
    //   704: aload_1
    //   705: invokevirtual 566	android/text/StaticLayout:getTopPadding	()I
    //   708: istore_2
    //   709: aload_0
    //   710: iload_2
    //   711: putfield 471	android/text/DynamicLayout:mTopPadding	I
    //   714: iload 4
    //   716: iload_2
    //   717: isub
    //   718: istore_3
    //   719: iload_3
    //   720: istore 4
    //   722: iload 6
    //   724: istore 14
    //   726: aload_0
    //   727: getfield 154	android/text/DynamicLayout:mIncludePad	Z
    //   730: ifeq +33 -> 763
    //   733: iload_3
    //   734: istore 4
    //   736: iload 6
    //   738: istore 14
    //   740: iload 12
    //   742: ifeq +21 -> 763
    //   745: aload_1
    //   746: invokevirtual 568	android/text/StaticLayout:getBottomPadding	()I
    //   749: istore 14
    //   751: aload_0
    //   752: iload 14
    //   754: putfield 445	android/text/DynamicLayout:mBottomPadding	I
    //   757: iload_3
    //   758: iload 14
    //   760: iadd
    //   761: istore 4
    //   763: aload_0
    //   764: getfield 328	android/text/DynamicLayout:mInts	Landroid/text/PackedIntVector;
    //   767: iload 15
    //   769: iconst_0
    //   770: iload 8
    //   772: iload 13
    //   774: isub
    //   775: invokevirtual 572	android/text/PackedIntVector:adjustValuesBelow	(III)V
    //   778: aload_0
    //   779: getfield 328	android/text/DynamicLayout:mInts	Landroid/text/PackedIntVector;
    //   782: iload 15
    //   784: iconst_1
    //   785: iload 16
    //   787: iload 17
    //   789: isub
    //   790: iload 4
    //   792: iadd
    //   793: invokevirtual 572	android/text/PackedIntVector:adjustValuesBelow	(III)V
    //   796: aload_0
    //   797: getfield 351	android/text/DynamicLayout:mEllipsize	Z
    //   800: ifeq +18 -> 818
    //   803: bipush 7
    //   805: newarray int
    //   807: astore 18
    //   809: aload 18
    //   811: iconst_5
    //   812: ldc 30
    //   814: iastore
    //   815: goto +8 -> 823
    //   818: iconst_5
    //   819: newarray int
    //   821: astore 18
    //   823: iconst_1
    //   824: anewarray 382	android/text/Layout$Directions
    //   827: astore 20
    //   829: iconst_0
    //   830: istore 13
    //   832: iload 16
    //   834: istore_3
    //   835: iload 13
    //   837: iload 11
    //   839: if_icmpge +301 -> 1140
    //   842: aload_1
    //   843: iload 13
    //   845: invokevirtual 558	android/text/StaticLayout:getLineStart	(I)I
    //   848: istore 16
    //   850: aload 18
    //   852: iconst_0
    //   853: iload 16
    //   855: iastore
    //   856: aload 18
    //   858: iconst_0
    //   859: aload 18
    //   861: iconst_0
    //   862: iaload
    //   863: aload_1
    //   864: iload 13
    //   866: invokevirtual 574	android/text/StaticLayout:getParagraphDirection	(I)I
    //   869: bipush 30
    //   871: ishl
    //   872: ior
    //   873: iastore
    //   874: aload 18
    //   876: iconst_0
    //   877: iaload
    //   878: istore 6
    //   880: aload_1
    //   881: iload 13
    //   883: invokevirtual 576	android/text/StaticLayout:getLineContainsTab	(I)Z
    //   886: ifeq +10 -> 896
    //   889: ldc 49
    //   891: istore 12
    //   893: goto +6 -> 899
    //   896: iconst_0
    //   897: istore 12
    //   899: aload 18
    //   901: iconst_0
    //   902: iload 6
    //   904: iload 12
    //   906: ior
    //   907: iastore
    //   908: aload_1
    //   909: iload 13
    //   911: invokevirtual 564	android/text/StaticLayout:getLineTop	(I)I
    //   914: iload_3
    //   915: iadd
    //   916: istore 6
    //   918: iload 6
    //   920: istore 12
    //   922: iload 13
    //   924: ifle +9 -> 933
    //   927: iload 6
    //   929: iload_2
    //   930: isub
    //   931: istore 12
    //   933: aload 18
    //   935: iconst_1
    //   936: iload 12
    //   938: iastore
    //   939: aload_1
    //   940: iload 13
    //   942: invokevirtual 578	android/text/StaticLayout:getLineDescent	(I)I
    //   945: istore 6
    //   947: iload 6
    //   949: istore 12
    //   951: iload 13
    //   953: iload 11
    //   955: iconst_1
    //   956: isub
    //   957: if_icmpne +10 -> 967
    //   960: iload 6
    //   962: iload 14
    //   964: iadd
    //   965: istore 12
    //   967: aload 18
    //   969: iconst_2
    //   970: iload 12
    //   972: iastore
    //   973: aload 18
    //   975: iconst_3
    //   976: aload_1
    //   977: iload 13
    //   979: invokevirtual 580	android/text/StaticLayout:getLineExtra	(I)I
    //   982: iastore
    //   983: aload 20
    //   985: iconst_0
    //   986: aload_1
    //   987: iload 13
    //   989: invokevirtual 582	android/text/StaticLayout:getLineDirections	(I)Landroid/text/Layout$Directions;
    //   992: aastore
    //   993: iload 13
    //   995: iload 11
    //   997: iconst_1
    //   998: isub
    //   999: if_icmpne +13 -> 1012
    //   1002: iload 7
    //   1004: iload 8
    //   1006: iadd
    //   1007: istore 12
    //   1009: goto +13 -> 1022
    //   1012: aload_1
    //   1013: iload 13
    //   1015: iconst_1
    //   1016: iadd
    //   1017: invokevirtual 558	android/text/StaticLayout:getLineStart	(I)I
    //   1020: istore 12
    //   1022: aload 18
    //   1024: iconst_4
    //   1025: aload_1
    //   1026: iload 13
    //   1028: invokevirtual 584	android/text/StaticLayout:getHyphen	(I)I
    //   1031: sipush 255
    //   1034: iand
    //   1035: iastore
    //   1036: aload 18
    //   1038: iconst_4
    //   1039: iaload
    //   1040: istore 6
    //   1042: aload_0
    //   1043: aload 5
    //   1045: iload 16
    //   1047: iload 12
    //   1049: invokespecial 586	android/text/DynamicLayout:contentMayProtrudeFromLineTopOrBottom	(Ljava/lang/CharSequence;II)Z
    //   1052: ifeq +11 -> 1063
    //   1055: sipush 256
    //   1058: istore 12
    //   1060: goto +6 -> 1066
    //   1063: iconst_0
    //   1064: istore 12
    //   1066: aload 18
    //   1068: iconst_4
    //   1069: iload 6
    //   1071: iload 12
    //   1073: ior
    //   1074: iastore
    //   1075: aload_0
    //   1076: getfield 351	android/text/DynamicLayout:mEllipsize	Z
    //   1079: ifeq +27 -> 1106
    //   1082: aload 18
    //   1084: iconst_5
    //   1085: aload_1
    //   1086: iload 13
    //   1088: invokevirtual 588	android/text/StaticLayout:getEllipsisStart	(I)I
    //   1091: iastore
    //   1092: aload 18
    //   1094: bipush 6
    //   1096: aload_1
    //   1097: iload 13
    //   1099: invokevirtual 590	android/text/StaticLayout:getEllipsisCount	(I)I
    //   1102: iastore
    //   1103: goto +3 -> 1106
    //   1106: aload_0
    //   1107: getfield 328	android/text/DynamicLayout:mInts	Landroid/text/PackedIntVector;
    //   1110: iload 15
    //   1112: iload 13
    //   1114: iadd
    //   1115: aload 18
    //   1117: invokevirtual 380	android/text/PackedIntVector:insertAt	(I[I)V
    //   1120: aload_0
    //   1121: getfield 356	android/text/DynamicLayout:mObjects	Landroid/text/PackedObjectVector;
    //   1124: iload 15
    //   1126: iload 13
    //   1128: iadd
    //   1129: aload 20
    //   1131: invokevirtual 385	android/text/PackedObjectVector:insertAt	(I[Ljava/lang/Object;)V
    //   1134: iinc 13 1
    //   1137: goto -302 -> 835
    //   1140: aload_0
    //   1141: iload 15
    //   1143: iload 9
    //   1145: iconst_1
    //   1146: isub
    //   1147: iload 11
    //   1149: invokevirtual 593	android/text/DynamicLayout:updateBlocks	(III)V
    //   1152: aload 10
    //   1154: invokevirtual 596	android/text/StaticLayout$Builder:finish	()V
    //   1157: getstatic 99	android/text/DynamicLayout:sLock	[Ljava/lang/Object;
    //   1160: astore 18
    //   1162: aload 18
    //   1164: monitorenter
    //   1165: aload_1
    //   1166: putstatic 93	android/text/DynamicLayout:sStaticLayout	Landroid/text/StaticLayout;
    //   1169: aload 10
    //   1171: putstatic 95	android/text/DynamicLayout:sBuilder	Landroid/text/StaticLayout$Builder;
    //   1174: aload 18
    //   1176: monitorexit
    //   1177: return
    //   1178: astore_1
    //   1179: aload 18
    //   1181: monitorexit
    //   1182: aload_1
    //   1183: athrow
    //   1184: astore_1
    //   1185: goto +4 -> 1189
    //   1188: astore_1
    //   1189: aload 18
    //   1191: monitorexit
    //   1192: aload_1
    //   1193: athrow
    //   1194: astore_1
    //   1195: goto -6 -> 1189
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1198	0	this	DynamicLayout
    //   0	1198	1	paramCharSequence	CharSequence
    //   0	1198	2	paramInt1	int
    //   0	1198	3	paramInt2	int
    //   0	1198	4	paramInt3	int
    //   13	1031	5	localCharSequence	CharSequence
    //   22	1052	6	i	int
    //   34	973	7	j	int
    //   61	946	8	k	int
    //   103	1044	9	m	int
    //   150	1020	10	localObject1	Object
    //   160	988	11	n	int
    //   184	890	12	i1	int
    //   197	938	13	i2	int
    //   201	764	14	i3	int
    //   348	794	15	i4	int
    //   356	690	16	i5	int
    //   391	399	17	i6	int
    //   414	776	18	localObject2	Object
    //   573	11	19	bool	boolean
    //   827	303	20	arrayOfDirections	Layout.Directions[]
    // Exception table:
    //   from	to	target	type
    //   1165	1177	1178	finally
    //   1179	1182	1178	finally
    //   428	439	1184	finally
    //   419	428	1188	finally
    //   1189	1192	1194	finally
  }
  
  public void setBlockIndex(int paramInt1, int paramInt2)
  {
    mBlockIndices[paramInt1] = paramInt2;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void setBlocksDataForTest(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
  {
    mBlockEndLines = new int[paramArrayOfInt1.length];
    mBlockIndices = new int[paramArrayOfInt2.length];
    System.arraycopy(paramArrayOfInt1, 0, mBlockEndLines, 0, paramArrayOfInt1.length);
    System.arraycopy(paramArrayOfInt2, 0, mBlockIndices, 0, paramArrayOfInt2.length);
    mNumberOfBlocks = paramInt1;
    while (mInts.size() < paramInt2) {
      mInts.insertAt(mInts.size(), new int[5]);
    }
  }
  
  public void setIndexFirstChangedBlock(int paramInt)
  {
    mIndexFirstChangedBlock = paramInt;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public void updateBlocks(int paramInt1, int paramInt2, int paramInt3)
  {
    if (mBlockEndLines == null)
    {
      createBlocks();
      return;
    }
    int i = -1;
    int j = -1;
    for (int k = 0;; k++)
    {
      m = i;
      if (k >= mNumberOfBlocks) {
        break;
      }
      if (mBlockEndLines[k] >= paramInt1)
      {
        m = k;
        break;
      }
    }
    for (k = m;; k++)
    {
      n = j;
      if (k >= mNumberOfBlocks) {
        break;
      }
      if (mBlockEndLines[k] >= paramInt2)
      {
        n = k;
        break;
      }
    }
    int i1 = mBlockEndLines[n];
    if (m == 0) {
      k = 0;
    } else {
      k = mBlockEndLines[(m - 1)] + 1;
    }
    if (paramInt1 > k) {
      i = 1;
    } else {
      i = 0;
    }
    int i2;
    if (paramInt3 > 0) {
      i2 = 1;
    } else {
      i2 = 0;
    }
    int i3;
    if (paramInt2 < mBlockEndLines[n]) {
      i3 = 1;
    } else {
      i3 = 0;
    }
    j = 0;
    if (i != 0) {
      j = 0 + 1;
    }
    k = j;
    if (i2 != 0) {
      k = j + 1;
    }
    j = k;
    if (i3 != 0) {
      j = k + 1;
    }
    int i4 = n - m + 1;
    int i5 = mNumberOfBlocks + j - i4;
    if (i5 == 0)
    {
      mBlockEndLines[0] = 0;
      mBlockIndices[0] = -1;
      mNumberOfBlocks = 1;
      return;
    }
    Object localObject1;
    Object localObject2;
    if (i5 > mBlockEndLines.length)
    {
      localObject1 = ArrayUtils.newUnpaddedIntArray(Math.max(mBlockEndLines.length * 2, i5));
      localObject2 = new int[localObject1.length];
      System.arraycopy(mBlockEndLines, 0, localObject1, 0, m);
      System.arraycopy(mBlockIndices, 0, localObject2, 0, m);
      System.arraycopy(mBlockEndLines, n + 1, localObject1, m + j, mNumberOfBlocks - n - 1);
      System.arraycopy(mBlockIndices, n + 1, localObject2, m + j, mNumberOfBlocks - n - 1);
      mBlockEndLines = ((int[])localObject1);
      mBlockIndices = ((int[])localObject2);
    }
    else if (j + i4 != 0)
    {
      System.arraycopy(mBlockEndLines, n + 1, mBlockEndLines, m + j, mNumberOfBlocks - n - 1);
      System.arraycopy(mBlockIndices, n + 1, mBlockIndices, m + j, mNumberOfBlocks - n - 1);
    }
    if ((j + i4 != 0) && (mBlocksAlwaysNeedToBeRedrawn != null))
    {
      localObject1 = new ArraySet();
      for (k = 0; k < mBlocksAlwaysNeedToBeRedrawn.size(); k++)
      {
        localObject2 = (Integer)mBlocksAlwaysNeedToBeRedrawn.valueAt(k);
        if (((Integer)localObject2).intValue() < m) {
          ((ArraySet)localObject1).add(localObject2);
        }
        if (((Integer)localObject2).intValue() > n) {
          ((ArraySet)localObject1).add(Integer.valueOf(((Integer)localObject2).intValue() + (j - i4)));
        }
      }
      mBlocksAlwaysNeedToBeRedrawn = ((ArraySet)localObject1);
    }
    mNumberOfBlocks = i5;
    int n = paramInt3 - (paramInt2 - paramInt1 + 1);
    if (n != 0)
    {
      paramInt2 = m + j;
      for (j = paramInt2;; j++)
      {
        k = paramInt2;
        if (j >= mNumberOfBlocks) {
          break;
        }
        localObject1 = mBlockEndLines;
        localObject1[j] += n;
      }
    }
    k = mNumberOfBlocks;
    mIndexFirstChangedBlock = Math.min(mIndexFirstChangedBlock, k);
    paramInt2 = m;
    if (i != 0)
    {
      mBlockEndLines[m] = (paramInt1 - 1);
      updateAlwaysNeedsToBeRedrawn(m);
      mBlockIndices[m] = -1;
      paramInt2 = m + 1;
    }
    int m = paramInt2;
    if (i2 != 0)
    {
      mBlockEndLines[paramInt2] = (paramInt1 + paramInt3 - 1);
      updateAlwaysNeedsToBeRedrawn(paramInt2);
      mBlockIndices[paramInt2] = -1;
      m = paramInt2 + 1;
    }
    if (i3 != 0)
    {
      mBlockEndLines[m] = (i1 + n);
      updateAlwaysNeedsToBeRedrawn(m);
      mBlockIndices[m] = -1;
    }
  }
  
  public static final class Builder
  {
    private static final Pools.SynchronizedPool<Builder> sPool = new Pools.SynchronizedPool(3);
    private Layout.Alignment mAlignment;
    private CharSequence mBase;
    private int mBreakStrategy;
    private CharSequence mDisplay;
    private TextUtils.TruncateAt mEllipsize;
    private int mEllipsizedWidth;
    private boolean mFallbackLineSpacing;
    private final Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
    private int mHyphenationFrequency;
    private boolean mIncludePad;
    private int mJustificationMode;
    private TextPaint mPaint;
    private float mSpacingAdd;
    private float mSpacingMult;
    private TextDirectionHeuristic mTextDir;
    private int mWidth;
    
    private Builder() {}
    
    public static Builder obtain(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt)
    {
      Builder localBuilder1 = (Builder)sPool.acquire();
      Builder localBuilder2 = localBuilder1;
      if (localBuilder1 == null) {
        localBuilder2 = new Builder();
      }
      mBase = paramCharSequence;
      mDisplay = paramCharSequence;
      mPaint = paramTextPaint;
      mWidth = paramInt;
      mAlignment = Layout.Alignment.ALIGN_NORMAL;
      mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
      mSpacingMult = 1.0F;
      mSpacingAdd = 0.0F;
      mIncludePad = true;
      mFallbackLineSpacing = false;
      mEllipsizedWidth = paramInt;
      mEllipsize = null;
      mBreakStrategy = 0;
      mHyphenationFrequency = 0;
      mJustificationMode = 0;
      return localBuilder2;
    }
    
    private static void recycle(Builder paramBuilder)
    {
      mBase = null;
      mDisplay = null;
      mPaint = null;
      sPool.release(paramBuilder);
    }
    
    public DynamicLayout build()
    {
      DynamicLayout localDynamicLayout = new DynamicLayout(this, null);
      recycle(this);
      return localDynamicLayout;
    }
    
    public Builder setAlignment(Layout.Alignment paramAlignment)
    {
      mAlignment = paramAlignment;
      return this;
    }
    
    public Builder setBreakStrategy(int paramInt)
    {
      mBreakStrategy = paramInt;
      return this;
    }
    
    public Builder setDisplayText(CharSequence paramCharSequence)
    {
      mDisplay = paramCharSequence;
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
  }
  
  private static class ChangeWatcher
    implements TextWatcher, SpanWatcher
  {
    private WeakReference<DynamicLayout> mLayout;
    
    public ChangeWatcher(DynamicLayout paramDynamicLayout)
    {
      mLayout = new WeakReference(paramDynamicLayout);
    }
    
    private void reflow(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      DynamicLayout localDynamicLayout = (DynamicLayout)mLayout.get();
      if (localDynamicLayout != null) {
        localDynamicLayout.reflow(paramCharSequence, paramInt1, paramInt2, paramInt3);
      } else if ((paramCharSequence instanceof Spannable)) {
        ((Spannable)paramCharSequence).removeSpan(this);
      }
    }
    
    public void afterTextChanged(Editable paramEditable) {}
    
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onSpanAdded(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2)
    {
      if ((paramObject instanceof UpdateLayout)) {
        reflow(paramSpannable, paramInt1, paramInt2 - paramInt1, paramInt2 - paramInt1);
      }
    }
    
    public void onSpanChanged(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if ((paramObject instanceof UpdateLayout))
      {
        int i = paramInt1;
        if (paramInt1 > paramInt2) {
          i = 0;
        }
        reflow(paramSpannable, i, paramInt2 - i, paramInt2 - i);
        reflow(paramSpannable, paramInt3, paramInt4 - paramInt3, paramInt4 - paramInt3);
      }
    }
    
    public void onSpanRemoved(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2)
    {
      if ((paramObject instanceof UpdateLayout)) {
        reflow(paramSpannable, paramInt1, paramInt2 - paramInt1, paramInt2 - paramInt1);
      }
    }
    
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      reflow(paramCharSequence, paramInt1, paramInt2, paramInt3);
    }
  }
}
