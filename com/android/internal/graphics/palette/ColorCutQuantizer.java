package com.android.internal.graphics.palette;

import android.graphics.Color;
import android.util.TimingLogger;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

final class ColorCutQuantizer
  implements Quantizer
{
  static final int COMPONENT_BLUE = -1;
  static final int COMPONENT_GREEN = -2;
  static final int COMPONENT_RED = -3;
  private static final String LOG_TAG = "ColorCutQuantizer";
  private static final boolean LOG_TIMINGS = false;
  private static final int QUANTIZE_WORD_MASK = 31;
  private static final int QUANTIZE_WORD_WIDTH = 5;
  private static final Comparator<Vbox> VBOX_COMPARATOR_VOLUME = new Comparator()
  {
    public int compare(ColorCutQuantizer.Vbox paramAnonymousVbox1, ColorCutQuantizer.Vbox paramAnonymousVbox2)
    {
      return paramAnonymousVbox2.getVolume() - paramAnonymousVbox1.getVolume();
    }
  };
  int[] mColors;
  Palette.Filter[] mFilters;
  int[] mHistogram;
  List<Palette.Swatch> mQuantizedColors;
  private final float[] mTempHsl = new float[3];
  TimingLogger mTimingLogger;
  
  ColorCutQuantizer() {}
  
  private static int approximateToRgb888(int paramInt)
  {
    return approximateToRgb888(quantizedRed(paramInt), quantizedGreen(paramInt), quantizedBlue(paramInt));
  }
  
  static int approximateToRgb888(int paramInt1, int paramInt2, int paramInt3)
  {
    return Color.rgb(modifyWordWidth(paramInt1, 5, 8), modifyWordWidth(paramInt2, 5, 8), modifyWordWidth(paramInt3, 5, 8));
  }
  
  private List<Palette.Swatch> generateAverageColors(Collection<Vbox> paramCollection)
  {
    ArrayList localArrayList = new ArrayList(paramCollection.size());
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      paramCollection = ((Vbox)localIterator.next()).getAverageColor();
      if (!shouldIgnoreColor(paramCollection)) {
        localArrayList.add(paramCollection);
      }
    }
    return localArrayList;
  }
  
  static void modifySignificantOctet(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt1)
    {
    default: 
      break;
    case -1: 
      paramInt1 = paramInt2;
    case -2: 
      while (paramInt1 <= paramInt3)
      {
        paramInt2 = paramArrayOfInt[paramInt1];
        paramArrayOfInt[paramInt1] = (quantizedBlue(paramInt2) << 10 | quantizedGreen(paramInt2) << 5 | quantizedRed(paramInt2));
        paramInt1++;
        continue;
        for (paramInt1 = paramInt2; paramInt1 <= paramInt3; paramInt1++)
        {
          paramInt2 = paramArrayOfInt[paramInt1];
          paramArrayOfInt[paramInt1] = (quantizedGreen(paramInt2) << 10 | quantizedRed(paramInt2) << 5 | quantizedBlue(paramInt2));
        }
      }
    }
  }
  
  private static int modifyWordWidth(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 > paramInt2) {
      paramInt1 <<= paramInt3 - paramInt2;
    } else {
      paramInt1 >>= paramInt2 - paramInt3;
    }
    return paramInt1 & (1 << paramInt3) - 1;
  }
  
  private static int quantizeFromRgb888(int paramInt)
  {
    return modifyWordWidth(Color.red(paramInt), 8, 5) << 10 | modifyWordWidth(Color.green(paramInt), 8, 5) << 5 | modifyWordWidth(Color.blue(paramInt), 8, 5);
  }
  
  private List<Palette.Swatch> quantizePixels(int paramInt)
  {
    PriorityQueue localPriorityQueue = new PriorityQueue(paramInt, VBOX_COMPARATOR_VOLUME);
    localPriorityQueue.offer(new Vbox(0, mColors.length - 1));
    splitBoxes(localPriorityQueue, paramInt);
    return generateAverageColors(localPriorityQueue);
  }
  
  static int quantizedBlue(int paramInt)
  {
    return paramInt & 0x1F;
  }
  
  static int quantizedGreen(int paramInt)
  {
    return paramInt >> 5 & 0x1F;
  }
  
  static int quantizedRed(int paramInt)
  {
    return paramInt >> 10 & 0x1F;
  }
  
  private boolean shouldIgnoreColor(int paramInt)
  {
    paramInt = approximateToRgb888(paramInt);
    ColorUtils.colorToHSL(paramInt, mTempHsl);
    return shouldIgnoreColor(paramInt, mTempHsl);
  }
  
  private boolean shouldIgnoreColor(int paramInt, float[] paramArrayOfFloat)
  {
    if ((mFilters != null) && (mFilters.length > 0))
    {
      int i = 0;
      int j = mFilters.length;
      while (i < j)
      {
        if (!mFilters[i].isAllowed(paramInt, paramArrayOfFloat)) {
          return true;
        }
        i++;
      }
    }
    return false;
  }
  
  private boolean shouldIgnoreColor(Palette.Swatch paramSwatch)
  {
    return shouldIgnoreColor(paramSwatch.getRgb(), paramSwatch.getHsl());
  }
  
  private void splitBoxes(PriorityQueue<Vbox> paramPriorityQueue, int paramInt)
  {
    while (paramPriorityQueue.size() < paramInt)
    {
      Vbox localVbox = (Vbox)paramPriorityQueue.poll();
      if ((localVbox != null) && (localVbox.canSplit()))
      {
        paramPriorityQueue.offer(localVbox.splitBox());
        paramPriorityQueue.offer(localVbox);
      }
      else {}
    }
  }
  
  public List<Palette.Swatch> getQuantizedColors()
  {
    return mQuantizedColors;
  }
  
  public void quantize(int[] paramArrayOfInt, int paramInt, Palette.Filter[] paramArrayOfFilter)
  {
    mTimingLogger = null;
    mFilters = paramArrayOfFilter;
    paramArrayOfFilter = new int[32768];
    mHistogram = paramArrayOfFilter;
    int i = 0;
    for (int j = 0; j < paramArrayOfInt.length; j++)
    {
      k = quantizeFromRgb888(paramArrayOfInt[j]);
      paramArrayOfInt[j] = k;
      paramArrayOfFilter[k] += 1;
    }
    j = 0;
    int k = 0;
    while (k < paramArrayOfFilter.length)
    {
      if ((paramArrayOfFilter[k] > 0) && (shouldIgnoreColor(k))) {
        paramArrayOfFilter[k] = 0;
      }
      m = j;
      if (paramArrayOfFilter[k] > 0) {
        m = j + 1;
      }
      k++;
      j = m;
    }
    paramArrayOfInt = new int[j];
    mColors = paramArrayOfInt;
    int n = 0;
    int m = 0;
    while (m < paramArrayOfFilter.length)
    {
      k = n;
      if (paramArrayOfFilter[m] > 0)
      {
        paramArrayOfInt[n] = m;
        k = n + 1;
      }
      m++;
      n = k;
    }
    if (j <= paramInt)
    {
      mQuantizedColors = new ArrayList();
      j = paramArrayOfInt.length;
      for (paramInt = i; paramInt < j; paramInt++)
      {
        k = paramArrayOfInt[paramInt];
        mQuantizedColors.add(new Palette.Swatch(approximateToRgb888(k), paramArrayOfFilter[k]));
      }
    }
    mQuantizedColors = quantizePixels(paramInt);
  }
  
  private class Vbox
  {
    private int mLowerIndex;
    private int mMaxBlue;
    private int mMaxGreen;
    private int mMaxRed;
    private int mMinBlue;
    private int mMinGreen;
    private int mMinRed;
    private int mPopulation;
    private int mUpperIndex;
    
    Vbox(int paramInt1, int paramInt2)
    {
      mLowerIndex = paramInt1;
      mUpperIndex = paramInt2;
      fitBox();
    }
    
    final boolean canSplit()
    {
      int i = getColorCount();
      boolean bool = true;
      if (i <= 1) {
        bool = false;
      }
      return bool;
    }
    
    final int findSplitPoint()
    {
      int i = getLongestColorDimension();
      int[] arrayOfInt1 = mColors;
      int[] arrayOfInt2 = mHistogram;
      ColorCutQuantizer.modifySignificantOctet(arrayOfInt1, i, mLowerIndex, mUpperIndex);
      Arrays.sort(arrayOfInt1, mLowerIndex, mUpperIndex + 1);
      ColorCutQuantizer.modifySignificantOctet(arrayOfInt1, i, mLowerIndex, mUpperIndex);
      int j = mPopulation / 2;
      int k = mLowerIndex;
      i = 0;
      while (k <= mUpperIndex)
      {
        i += arrayOfInt2[arrayOfInt1[k]];
        if (i >= j) {
          return Math.min(mUpperIndex - 1, k);
        }
        k++;
      }
      return mLowerIndex;
    }
    
    final void fitBox()
    {
      int[] arrayOfInt1 = mColors;
      int[] arrayOfInt2 = mHistogram;
      int i = Integer.MAX_VALUE;
      int j = Integer.MAX_VALUE;
      int k = Integer.MAX_VALUE;
      int m = Integer.MIN_VALUE;
      int n = Integer.MIN_VALUE;
      int i1 = Integer.MIN_VALUE;
      int i2 = 0;
      int i3 = mLowerIndex;
      while (i3 <= mUpperIndex)
      {
        int i4 = arrayOfInt1[i3];
        int i5 = i2 + arrayOfInt2[i4];
        int i6 = ColorCutQuantizer.quantizedRed(i4);
        int i7 = ColorCutQuantizer.quantizedGreen(i4);
        i4 = ColorCutQuantizer.quantizedBlue(i4);
        i2 = m;
        if (i6 > m) {
          i2 = i6;
        }
        m = i;
        if (i6 < i) {
          m = i6;
        }
        i6 = i1;
        if (i7 > i1) {
          i6 = i7;
        }
        i1 = k;
        if (i7 < k) {
          i1 = i7;
        }
        i7 = n;
        if (i4 > n) {
          i7 = i4;
        }
        n = j;
        if (i4 < j) {
          n = i4;
        }
        i3++;
        i = m;
        j = n;
        k = i1;
        m = i2;
        n = i7;
        i1 = i6;
        i2 = i5;
      }
      mMinRed = i;
      mMaxRed = m;
      mMinGreen = k;
      mMaxGreen = i1;
      mMinBlue = j;
      mMaxBlue = n;
      mPopulation = i2;
    }
    
    final Palette.Swatch getAverageColor()
    {
      int[] arrayOfInt1 = mColors;
      int[] arrayOfInt2 = mHistogram;
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      for (int n = mLowerIndex; n <= mUpperIndex; n++)
      {
        int i1 = arrayOfInt1[n];
        int i2 = arrayOfInt2[i1];
        m += i2;
        i += ColorCutQuantizer.quantizedRed(i1) * i2;
        j += ColorCutQuantizer.quantizedGreen(i1) * i2;
        k += ColorCutQuantizer.quantizedBlue(i1) * i2;
      }
      return new Palette.Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(i / m), Math.round(j / m), Math.round(k / m)), m);
    }
    
    final int getColorCount()
    {
      return 1 + mUpperIndex - mLowerIndex;
    }
    
    final int getLongestColorDimension()
    {
      int i = mMaxRed - mMinRed;
      int j = mMaxGreen - mMinGreen;
      int k = mMaxBlue - mMinBlue;
      if ((i >= j) && (i >= k)) {
        return -3;
      }
      if ((j >= i) && (j >= k)) {
        return -2;
      }
      return -1;
    }
    
    final int getVolume()
    {
      return (mMaxRed - mMinRed + 1) * (mMaxGreen - mMinGreen + 1) * (mMaxBlue - mMinBlue + 1);
    }
    
    final Vbox splitBox()
    {
      if (canSplit())
      {
        int i = findSplitPoint();
        Vbox localVbox = new Vbox(ColorCutQuantizer.this, i + 1, mUpperIndex);
        mUpperIndex = i;
        fitBox();
        return localVbox;
      }
      throw new IllegalStateException("Can not split a box with only 1 color");
    }
  }
}
