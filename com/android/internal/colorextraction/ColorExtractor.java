package com.android.internal.colorextraction;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.app.WallpaperManager.OnColorsChangedListener;
import android.content.Context;
import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.types.ExtractionType;
import com.android.internal.colorextraction.types.Tonal;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ColorExtractor
  implements WallpaperManager.OnColorsChangedListener
{
  private static final boolean DEBUG = false;
  private static final String TAG = "ColorExtractor";
  public static final int TYPE_DARK = 1;
  public static final int TYPE_EXTRA_DARK = 2;
  public static final int TYPE_NORMAL = 0;
  private static final int[] sGradientTypes = { 0, 1, 2 };
  private final Context mContext;
  private final ExtractionType mExtractionType;
  protected final SparseArray<GradientColors[]> mGradientColors;
  protected WallpaperColors mLockColors;
  private final ArrayList<WeakReference<OnColorsChangedListener>> mOnColorsChangedListeners;
  protected WallpaperColors mSystemColors;
  
  public ColorExtractor(Context paramContext)
  {
    this(paramContext, new Tonal(paramContext));
  }
  
  @VisibleForTesting
  public ColorExtractor(Context paramContext, ExtractionType paramExtractionType)
  {
    mContext = paramContext;
    mExtractionType = paramExtractionType;
    mGradientColors = new SparseArray();
    paramContext = new int[2];
    Context tmp30_29 = paramContext;
    tmp30_29[0] = 2;
    Context tmp34_30 = tmp30_29;
    tmp34_30[1] = 1;
    tmp34_30;
    int i = paramContext.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramContext[j];
      paramExtractionType = new GradientColors[sGradientTypes.length];
      mGradientColors.append(k, paramExtractionType);
      localObject = sGradientTypes;
      int m = localObject.length;
      for (k = 0; k < m; k++) {
        paramExtractionType[localObject[k]] = new GradientColors();
      }
    }
    mOnColorsChangedListeners = new ArrayList();
    Object localObject = (GradientColors[])mGradientColors.get(1);
    paramExtractionType = (GradientColors[])mGradientColors.get(2);
    paramContext = (WallpaperManager)mContext.getSystemService(WallpaperManager.class);
    if (paramContext == null)
    {
      Log.w("ColorExtractor", "Can't listen to color changes!");
    }
    else
    {
      paramContext.addOnColorsChangedListener(this, null);
      Trace.beginSection("ColorExtractor#getWallpaperColors");
      mSystemColors = paramContext.getWallpaperColors(1);
      mLockColors = paramContext.getWallpaperColors(2);
      Trace.endSection();
    }
    extractInto(mSystemColors, localObject[0], localObject[1], localObject[2]);
    extractInto(mLockColors, paramExtractionType[0], paramExtractionType[1], paramExtractionType[2]);
  }
  
  private void extractInto(WallpaperColors paramWallpaperColors, GradientColors paramGradientColors1, GradientColors paramGradientColors2, GradientColors paramGradientColors3)
  {
    mExtractionType.extractInto(paramWallpaperColors, paramGradientColors1, paramGradientColors2, paramGradientColors3);
  }
  
  public void addOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener)
  {
    mOnColorsChangedListeners.add(new WeakReference(paramOnColorsChangedListener));
  }
  
  public void destroy()
  {
    WallpaperManager localWallpaperManager = (WallpaperManager)mContext.getSystemService(WallpaperManager.class);
    if (localWallpaperManager != null) {
      localWallpaperManager.removeOnColorsChangedListener(this);
    }
  }
  
  public GradientColors getColors(int paramInt)
  {
    return getColors(paramInt, 1);
  }
  
  public GradientColors getColors(int paramInt1, int paramInt2)
  {
    if ((paramInt2 != 0) && (paramInt2 != 1) && (paramInt2 != 2)) {
      throw new IllegalArgumentException("type should be TYPE_NORMAL, TYPE_DARK or TYPE_EXTRA_DARK");
    }
    if ((paramInt1 != 2) && (paramInt1 != 1)) {
      throw new IllegalArgumentException("which should be FLAG_SYSTEM or FLAG_NORMAL");
    }
    return ((GradientColors[])mGradientColors.get(paramInt1))[paramInt2];
  }
  
  public WallpaperColors getWallpaperColors(int paramInt)
  {
    if (paramInt == 2) {
      return mLockColors;
    }
    if (paramInt == 1) {
      return mSystemColors;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid value for which: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void onColorsChanged(WallpaperColors paramWallpaperColors, int paramInt)
  {
    int i = 0;
    GradientColors[] arrayOfGradientColors;
    if ((paramInt & 0x2) != 0)
    {
      mLockColors = paramWallpaperColors;
      arrayOfGradientColors = (GradientColors[])mGradientColors.get(2);
      extractInto(paramWallpaperColors, arrayOfGradientColors[0], arrayOfGradientColors[1], arrayOfGradientColors[2]);
      i = 1;
    }
    if ((paramInt & 0x1) != 0)
    {
      mSystemColors = paramWallpaperColors;
      arrayOfGradientColors = (GradientColors[])mGradientColors.get(1);
      extractInto(paramWallpaperColors, arrayOfGradientColors[0], arrayOfGradientColors[1], arrayOfGradientColors[2]);
      i = 1;
    }
    if (i != 0) {
      triggerColorsChanged(paramInt);
    }
  }
  
  public void removeOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener)
  {
    ArrayList localArrayList = new ArrayList(mOnColorsChangedListeners);
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      WeakReference localWeakReference = (WeakReference)localArrayList.get(j);
      if (localWeakReference.get() == paramOnColorsChangedListener)
      {
        mOnColorsChangedListeners.remove(localWeakReference);
        break;
      }
    }
  }
  
  protected void triggerColorsChanged(int paramInt)
  {
    ArrayList localArrayList = new ArrayList(mOnColorsChangedListeners);
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      WeakReference localWeakReference = (WeakReference)localArrayList.get(j);
      OnColorsChangedListener localOnColorsChangedListener = (OnColorsChangedListener)localWeakReference.get();
      if (localOnColorsChangedListener == null) {
        mOnColorsChangedListeners.remove(localWeakReference);
      } else {
        localOnColorsChangedListener.onColorsChanged(this, paramInt);
      }
    }
  }
  
  public static class GradientColors
  {
    private int mMainColor;
    private int mSecondaryColor;
    private boolean mSupportsDarkText;
    
    public GradientColors() {}
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = false;
      if ((paramObject != null) && (paramObject.getClass() == getClass()))
      {
        paramObject = (GradientColors)paramObject;
        boolean bool2 = bool1;
        if (mMainColor == mMainColor)
        {
          bool2 = bool1;
          if (mSecondaryColor == mSecondaryColor)
          {
            bool2 = bool1;
            if (mSupportsDarkText == mSupportsDarkText) {
              bool2 = true;
            }
          }
        }
        return bool2;
      }
      return false;
    }
    
    public int getMainColor()
    {
      return mMainColor;
    }
    
    public int getSecondaryColor()
    {
      return mSecondaryColor;
    }
    
    public int hashCode()
    {
      return 31 * (31 * mMainColor + mSecondaryColor) + (mSupportsDarkText ^ true);
    }
    
    public void set(GradientColors paramGradientColors)
    {
      mMainColor = mMainColor;
      mSecondaryColor = mSecondaryColor;
      mSupportsDarkText = mSupportsDarkText;
    }
    
    public void setMainColor(int paramInt)
    {
      mMainColor = paramInt;
    }
    
    public void setSecondaryColor(int paramInt)
    {
      mSecondaryColor = paramInt;
    }
    
    public void setSupportsDarkText(boolean paramBoolean)
    {
      mSupportsDarkText = paramBoolean;
    }
    
    public boolean supportsDarkText()
    {
      return mSupportsDarkText;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GradientColors(");
      localStringBuilder.append(Integer.toHexString(mMainColor));
      localStringBuilder.append(", ");
      localStringBuilder.append(Integer.toHexString(mSecondaryColor));
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface OnColorsChangedListener
  {
    public abstract void onColorsChanged(ColorExtractor paramColorExtractor, int paramInt);
  }
}
