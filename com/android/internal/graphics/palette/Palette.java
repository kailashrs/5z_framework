package com.android.internal.graphics.palette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseBooleanArray;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Palette
{
  static final int DEFAULT_CALCULATE_NUMBER_COLORS = 16;
  static final Filter DEFAULT_FILTER = new Filter()
  {
    private static final float BLACK_MAX_LIGHTNESS = 0.05F;
    private static final float WHITE_MIN_LIGHTNESS = 0.95F;
    
    private boolean isBlack(float[] paramAnonymousArrayOfFloat)
    {
      boolean bool;
      if (paramAnonymousArrayOfFloat[2] <= 0.05F) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isNearRedILine(float[] paramAnonymousArrayOfFloat)
    {
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (paramAnonymousArrayOfFloat[0] >= 10.0F)
      {
        bool2 = bool1;
        if (paramAnonymousArrayOfFloat[0] <= 37.0F)
        {
          bool2 = bool1;
          if (paramAnonymousArrayOfFloat[1] <= 0.82F) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    
    private boolean isWhite(float[] paramAnonymousArrayOfFloat)
    {
      boolean bool;
      if (paramAnonymousArrayOfFloat[2] >= 0.95F) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isAllowed(int paramAnonymousInt, float[] paramAnonymousArrayOfFloat)
    {
      boolean bool;
      if ((!isWhite(paramAnonymousArrayOfFloat)) && (!isBlack(paramAnonymousArrayOfFloat)) && (!isNearRedILine(paramAnonymousArrayOfFloat))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  };
  static final int DEFAULT_RESIZE_BITMAP_AREA = 12544;
  static final String LOG_TAG = "Palette";
  static final boolean LOG_TIMINGS = false;
  static final float MIN_CONTRAST_BODY_TEXT = 4.5F;
  static final float MIN_CONTRAST_TITLE_TEXT = 3.0F;
  private final Swatch mDominantSwatch;
  private final Map<Target, Swatch> mSelectedSwatches;
  private final List<Swatch> mSwatches;
  private final List<Target> mTargets;
  private final SparseBooleanArray mUsedColors;
  
  Palette(List<Swatch> paramList, List<Target> paramList1)
  {
    mSwatches = paramList;
    mTargets = paramList1;
    mUsedColors = new SparseBooleanArray();
    mSelectedSwatches = new ArrayMap();
    mDominantSwatch = findDominantSwatch();
  }
  
  private static float[] copyHslValues(Swatch paramSwatch)
  {
    float[] arrayOfFloat = new float[3];
    System.arraycopy(paramSwatch.getHsl(), 0, arrayOfFloat, 0, 3);
    return arrayOfFloat;
  }
  
  private Swatch findDominantSwatch()
  {
    int i = Integer.MIN_VALUE;
    Object localObject = null;
    int j = 0;
    int k = mSwatches.size();
    while (j < k)
    {
      Swatch localSwatch = (Swatch)mSwatches.get(j);
      int m = i;
      if (localSwatch.getPopulation() > i)
      {
        localObject = localSwatch;
        m = localSwatch.getPopulation();
      }
      j++;
      i = m;
    }
    return localObject;
  }
  
  public static Builder from(Bitmap paramBitmap)
  {
    return new Builder(paramBitmap);
  }
  
  public static Palette from(List<Swatch> paramList)
  {
    return new Builder(paramList).generate();
  }
  
  @Deprecated
  public static Palette generate(Bitmap paramBitmap)
  {
    return from(paramBitmap).generate();
  }
  
  @Deprecated
  public static Palette generate(Bitmap paramBitmap, int paramInt)
  {
    return from(paramBitmap).maximumColorCount(paramInt).generate();
  }
  
  @Deprecated
  public static AsyncTask<Bitmap, Void, Palette> generateAsync(Bitmap paramBitmap, int paramInt, PaletteAsyncListener paramPaletteAsyncListener)
  {
    return from(paramBitmap).maximumColorCount(paramInt).generate(paramPaletteAsyncListener);
  }
  
  @Deprecated
  public static AsyncTask<Bitmap, Void, Palette> generateAsync(Bitmap paramBitmap, PaletteAsyncListener paramPaletteAsyncListener)
  {
    return from(paramBitmap).generate(paramPaletteAsyncListener);
  }
  
  private float generateScore(Swatch paramSwatch, Target paramTarget)
  {
    float[] arrayOfFloat = paramSwatch.getHsl();
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    int i;
    if (mDominantSwatch != null) {
      i = mDominantSwatch.getPopulation();
    } else {
      i = 1;
    }
    if (paramTarget.getSaturationWeight() > 0.0F) {
      f1 = paramTarget.getSaturationWeight() * (1.0F - Math.abs(arrayOfFloat[1] - paramTarget.getTargetSaturation()));
    }
    if (paramTarget.getLightnessWeight() > 0.0F) {
      f2 = paramTarget.getLightnessWeight() * (1.0F - Math.abs(arrayOfFloat[2] - paramTarget.getTargetLightness()));
    }
    if (paramTarget.getPopulationWeight() > 0.0F) {
      f3 = paramTarget.getPopulationWeight() * (paramSwatch.getPopulation() / i);
    }
    return f1 + f2 + f3;
  }
  
  private Swatch generateScoredTarget(Target paramTarget)
  {
    Swatch localSwatch = getMaxScoredSwatchForTarget(paramTarget);
    if ((localSwatch != null) && (paramTarget.isExclusive())) {
      mUsedColors.append(localSwatch.getRgb(), true);
    }
    return localSwatch;
  }
  
  private Swatch getMaxScoredSwatchForTarget(Target paramTarget)
  {
    float f1 = 0.0F;
    Object localObject1 = null;
    int i = 0;
    int j = mSwatches.size();
    while (i < j)
    {
      Swatch localSwatch = (Swatch)mSwatches.get(i);
      float f2 = f1;
      Object localObject2 = localObject1;
      if (shouldBeScoredForTarget(localSwatch, paramTarget))
      {
        float f3 = generateScore(localSwatch, paramTarget);
        if (localObject1 != null)
        {
          f2 = f1;
          localObject2 = localObject1;
          if (f3 <= f1) {}
        }
        else
        {
          localObject2 = localSwatch;
          f2 = f3;
        }
      }
      i++;
      f1 = f2;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  private boolean shouldBeScoredForTarget(Swatch paramSwatch, Target paramTarget)
  {
    float[] arrayOfFloat = paramSwatch.getHsl();
    boolean bool = true;
    if ((arrayOfFloat[1] < paramTarget.getMinimumSaturation()) || (arrayOfFloat[1] > paramTarget.getMaximumSaturation()) || (arrayOfFloat[2] < paramTarget.getMinimumLightness()) || (arrayOfFloat[2] > paramTarget.getMaximumLightness()) || (mUsedColors.get(paramSwatch.getRgb()))) {
      bool = false;
    }
    return bool;
  }
  
  void generate()
  {
    int i = 0;
    int j = mTargets.size();
    while (i < j)
    {
      Target localTarget = (Target)mTargets.get(i);
      localTarget.normalizeWeights();
      mSelectedSwatches.put(localTarget, generateScoredTarget(localTarget));
      i++;
    }
    mUsedColors.clear();
  }
  
  public int getColorForTarget(Target paramTarget, int paramInt)
  {
    paramTarget = getSwatchForTarget(paramTarget);
    if (paramTarget != null) {
      paramInt = paramTarget.getRgb();
    }
    return paramInt;
  }
  
  public int getDarkMutedColor(int paramInt)
  {
    return getColorForTarget(Target.DARK_MUTED, paramInt);
  }
  
  public Swatch getDarkMutedSwatch()
  {
    return getSwatchForTarget(Target.DARK_MUTED);
  }
  
  public int getDarkVibrantColor(int paramInt)
  {
    return getColorForTarget(Target.DARK_VIBRANT, paramInt);
  }
  
  public Swatch getDarkVibrantSwatch()
  {
    return getSwatchForTarget(Target.DARK_VIBRANT);
  }
  
  public int getDominantColor(int paramInt)
  {
    if (mDominantSwatch != null) {
      paramInt = mDominantSwatch.getRgb();
    }
    return paramInt;
  }
  
  public Swatch getDominantSwatch()
  {
    return mDominantSwatch;
  }
  
  public int getLightMutedColor(int paramInt)
  {
    return getColorForTarget(Target.LIGHT_MUTED, paramInt);
  }
  
  public Swatch getLightMutedSwatch()
  {
    return getSwatchForTarget(Target.LIGHT_MUTED);
  }
  
  public int getLightVibrantColor(int paramInt)
  {
    return getColorForTarget(Target.LIGHT_VIBRANT, paramInt);
  }
  
  public Swatch getLightVibrantSwatch()
  {
    return getSwatchForTarget(Target.LIGHT_VIBRANT);
  }
  
  public int getMutedColor(int paramInt)
  {
    return getColorForTarget(Target.MUTED, paramInt);
  }
  
  public Swatch getMutedSwatch()
  {
    return getSwatchForTarget(Target.MUTED);
  }
  
  public Swatch getSwatchForTarget(Target paramTarget)
  {
    return (Swatch)mSelectedSwatches.get(paramTarget);
  }
  
  public List<Swatch> getSwatches()
  {
    return Collections.unmodifiableList(mSwatches);
  }
  
  public List<Target> getTargets()
  {
    return Collections.unmodifiableList(mTargets);
  }
  
  public int getVibrantColor(int paramInt)
  {
    return getColorForTarget(Target.VIBRANT, paramInt);
  }
  
  public Swatch getVibrantSwatch()
  {
    return getSwatchForTarget(Target.VIBRANT);
  }
  
  public static final class Builder
  {
    private final Bitmap mBitmap;
    private final List<Palette.Filter> mFilters = new ArrayList();
    private int mMaxColors = 16;
    private Quantizer mQuantizer;
    private Rect mRegion;
    private int mResizeArea = 12544;
    private int mResizeMaxDimension = -1;
    private final List<Palette.Swatch> mSwatches;
    private final List<Target> mTargets = new ArrayList();
    
    public Builder(Bitmap paramBitmap)
    {
      if ((paramBitmap != null) && (!paramBitmap.isRecycled()))
      {
        mFilters.add(Palette.DEFAULT_FILTER);
        mBitmap = paramBitmap;
        mSwatches = null;
        mTargets.add(Target.LIGHT_VIBRANT);
        mTargets.add(Target.VIBRANT);
        mTargets.add(Target.DARK_VIBRANT);
        mTargets.add(Target.LIGHT_MUTED);
        mTargets.add(Target.MUTED);
        mTargets.add(Target.DARK_MUTED);
        return;
      }
      throw new IllegalArgumentException("Bitmap is not valid");
    }
    
    public Builder(List<Palette.Swatch> paramList)
    {
      if ((paramList != null) && (!paramList.isEmpty()))
      {
        mFilters.add(Palette.DEFAULT_FILTER);
        mSwatches = paramList;
        mBitmap = null;
        return;
      }
      throw new IllegalArgumentException("List of Swatches is not valid");
    }
    
    private int[] getPixelsFromBitmap(Bitmap paramBitmap)
    {
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      int[] arrayOfInt = new int[i * j];
      paramBitmap.getPixels(arrayOfInt, 0, i, 0, 0, i, j);
      if (mRegion == null) {
        return arrayOfInt;
      }
      int k = mRegion.width();
      int m = mRegion.height();
      paramBitmap = new int[k * m];
      for (j = 0; j < m; j++) {
        System.arraycopy(arrayOfInt, (mRegion.top + j) * i + mRegion.left, paramBitmap, j * k, k);
      }
      return paramBitmap;
    }
    
    private Bitmap scaleBitmapDown(Bitmap paramBitmap)
    {
      double d1 = -1.0D;
      int i;
      double d2;
      if (mResizeArea > 0)
      {
        i = paramBitmap.getWidth() * paramBitmap.getHeight();
        d2 = d1;
        if (i > mResizeArea) {
          d2 = Math.sqrt(mResizeArea / i);
        }
      }
      else
      {
        d2 = d1;
        if (mResizeMaxDimension > 0)
        {
          i = Math.max(paramBitmap.getWidth(), paramBitmap.getHeight());
          d2 = d1;
          if (i > mResizeMaxDimension) {
            d2 = mResizeMaxDimension / i;
          }
        }
      }
      if (d2 <= 0.0D) {
        return paramBitmap;
      }
      return Bitmap.createScaledBitmap(paramBitmap, (int)Math.ceil(paramBitmap.getWidth() * d2), (int)Math.ceil(paramBitmap.getHeight() * d2), false);
    }
    
    public Builder addFilter(Palette.Filter paramFilter)
    {
      if (paramFilter != null) {
        mFilters.add(paramFilter);
      }
      return this;
    }
    
    public Builder addTarget(Target paramTarget)
    {
      if (!mTargets.contains(paramTarget)) {
        mTargets.add(paramTarget);
      }
      return this;
    }
    
    public Builder clearFilters()
    {
      mFilters.clear();
      return this;
    }
    
    public Builder clearRegion()
    {
      mRegion = null;
      return this;
    }
    
    public Builder clearTargets()
    {
      if (mTargets != null) {
        mTargets.clear();
      }
      return this;
    }
    
    public AsyncTask<Bitmap, Void, Palette> generate(final Palette.PaletteAsyncListener paramPaletteAsyncListener)
    {
      if (paramPaletteAsyncListener != null) {
        new AsyncTask()
        {
          protected Palette doInBackground(Bitmap... paramAnonymousVarArgs)
          {
            try
            {
              paramAnonymousVarArgs = generate();
              return paramAnonymousVarArgs;
            }
            catch (Exception paramAnonymousVarArgs)
            {
              Log.e("Palette", "Exception thrown during async generate", paramAnonymousVarArgs);
            }
            return null;
          }
          
          protected void onPostExecute(Palette paramAnonymousPalette)
          {
            paramPaletteAsyncListener.onGenerated(paramAnonymousPalette);
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Bitmap[] { mBitmap });
      }
      throw new IllegalArgumentException("listener can not be null");
    }
    
    public Palette generate()
    {
      if (mBitmap != null)
      {
        Bitmap localBitmap = scaleBitmapDown(mBitmap);
        if (0 != 0) {
          throw new NullPointerException();
        }
        localObject = mRegion;
        if ((localBitmap != mBitmap) && (localObject != null))
        {
          double d = localBitmap.getWidth() / mBitmap.getWidth();
          left = ((int)Math.floor(left * d));
          top = ((int)Math.floor(top * d));
          right = Math.min((int)Math.ceil(right * d), localBitmap.getWidth());
          bottom = Math.min((int)Math.ceil(bottom * d), localBitmap.getHeight());
        }
        if (mQuantizer == null) {
          mQuantizer = new ColorCutQuantizer();
        }
        Quantizer localQuantizer = mQuantizer;
        int[] arrayOfInt = getPixelsFromBitmap(localBitmap);
        int i = mMaxColors;
        if (mFilters.isEmpty()) {
          localObject = null;
        } else {
          localObject = (Palette.Filter[])mFilters.toArray(new Palette.Filter[mFilters.size()]);
        }
        localQuantizer.quantize(arrayOfInt, i, (Palette.Filter[])localObject);
        if (localBitmap != mBitmap) {
          localBitmap.recycle();
        }
        localObject = mQuantizer.getQuantizedColors();
        if (0 != 0) {
          throw new NullPointerException();
        }
      }
      else
      {
        localObject = mSwatches;
      }
      Object localObject = new Palette((List)localObject, mTargets);
      ((Palette)localObject).generate();
      if (0 != 0) {
        throw new NullPointerException();
      }
      return localObject;
    }
    
    public Builder maximumColorCount(int paramInt)
    {
      mMaxColors = paramInt;
      return this;
    }
    
    public Builder resizeBitmapArea(int paramInt)
    {
      mResizeArea = paramInt;
      mResizeMaxDimension = -1;
      return this;
    }
    
    @Deprecated
    public Builder resizeBitmapSize(int paramInt)
    {
      mResizeMaxDimension = paramInt;
      mResizeArea = -1;
      return this;
    }
    
    public Builder setQuantizer(Quantizer paramQuantizer)
    {
      mQuantizer = paramQuantizer;
      return this;
    }
    
    public Builder setRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (mBitmap != null)
      {
        if (mRegion == null) {
          mRegion = new Rect();
        }
        mRegion.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        if (!mRegion.intersect(paramInt1, paramInt2, paramInt3, paramInt4)) {
          throw new IllegalArgumentException("The given region must intersect with the Bitmap's dimensions.");
        }
      }
      return this;
    }
  }
  
  public static abstract interface Filter
  {
    public abstract boolean isAllowed(int paramInt, float[] paramArrayOfFloat);
  }
  
  public static abstract interface PaletteAsyncListener
  {
    public abstract void onGenerated(Palette paramPalette);
  }
  
  public static final class Swatch
  {
    private final int mBlue;
    private int mBodyTextColor;
    private boolean mGeneratedTextColors;
    private final int mGreen;
    private float[] mHsl;
    private final int mPopulation;
    private final int mRed;
    private final int mRgb;
    private int mTitleTextColor;
    
    public Swatch(int paramInt1, int paramInt2)
    {
      mRed = Color.red(paramInt1);
      mGreen = Color.green(paramInt1);
      mBlue = Color.blue(paramInt1);
      mRgb = paramInt1;
      mPopulation = paramInt2;
    }
    
    Swatch(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      mRed = paramInt1;
      mGreen = paramInt2;
      mBlue = paramInt3;
      mRgb = Color.rgb(paramInt1, paramInt2, paramInt3);
      mPopulation = paramInt4;
    }
    
    Swatch(float[] paramArrayOfFloat, int paramInt)
    {
      this(ColorUtils.HSLToColor(paramArrayOfFloat), paramInt);
      mHsl = paramArrayOfFloat;
    }
    
    private void ensureTextColorsGenerated()
    {
      if (!mGeneratedTextColors)
      {
        int i = ColorUtils.calculateMinimumAlpha(-1, mRgb, 4.5F);
        int j = ColorUtils.calculateMinimumAlpha(-1, mRgb, 3.0F);
        if ((i != -1) && (j != -1))
        {
          mBodyTextColor = ColorUtils.setAlphaComponent(-1, i);
          mTitleTextColor = ColorUtils.setAlphaComponent(-1, j);
          mGeneratedTextColors = true;
          return;
        }
        int k = ColorUtils.calculateMinimumAlpha(-16777216, mRgb, 4.5F);
        int m = ColorUtils.calculateMinimumAlpha(-16777216, mRgb, 3.0F);
        if ((k != -1) && (m != -1))
        {
          mBodyTextColor = ColorUtils.setAlphaComponent(-16777216, k);
          mTitleTextColor = ColorUtils.setAlphaComponent(-16777216, m);
          mGeneratedTextColors = true;
          return;
        }
        if (i != -1) {
          i = ColorUtils.setAlphaComponent(-1, i);
        } else {
          i = ColorUtils.setAlphaComponent(-16777216, k);
        }
        mBodyTextColor = i;
        if (j != -1) {
          i = ColorUtils.setAlphaComponent(-1, j);
        } else {
          i = ColorUtils.setAlphaComponent(-16777216, m);
        }
        mTitleTextColor = i;
        mGeneratedTextColors = true;
      }
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (Swatch)paramObject;
        if ((mPopulation != mPopulation) || (mRgb != mRgb)) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public int getBodyTextColor()
    {
      ensureTextColorsGenerated();
      return mBodyTextColor;
    }
    
    public float[] getHsl()
    {
      if (mHsl == null) {
        mHsl = new float[3];
      }
      ColorUtils.RGBToHSL(mRed, mGreen, mBlue, mHsl);
      return mHsl;
    }
    
    public int getPopulation()
    {
      return mPopulation;
    }
    
    public int getRgb()
    {
      return mRgb;
    }
    
    public int getTitleTextColor()
    {
      ensureTextColorsGenerated();
      return mTitleTextColor;
    }
    
    public int hashCode()
    {
      return 31 * mRgb + mPopulation;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(getClass().getSimpleName());
      localStringBuilder.append(" [RGB: #");
      localStringBuilder.append(Integer.toHexString(getRgb()));
      localStringBuilder.append(']');
      localStringBuilder.append(" [HSL: ");
      localStringBuilder.append(Arrays.toString(getHsl()));
      localStringBuilder.append(']');
      localStringBuilder.append(" [Population: ");
      localStringBuilder.append(mPopulation);
      localStringBuilder.append(']');
      localStringBuilder.append(" [Title Text: #");
      localStringBuilder.append(Integer.toHexString(getTitleTextColor()));
      localStringBuilder.append(']');
      localStringBuilder.append(" [Body Text: #");
      localStringBuilder.append(Integer.toHexString(getBodyTextColor()));
      localStringBuilder.append(']');
      return localStringBuilder.toString();
    }
  }
}
