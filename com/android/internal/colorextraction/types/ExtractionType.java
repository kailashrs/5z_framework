package com.android.internal.colorextraction.types;

import android.app.WallpaperColors;
import com.android.internal.colorextraction.ColorExtractor.GradientColors;

public abstract interface ExtractionType
{
  public abstract void extractInto(WallpaperColors paramWallpaperColors, ColorExtractor.GradientColors paramGradientColors1, ColorExtractor.GradientColors paramGradientColors2, ColorExtractor.GradientColors paramGradientColors3);
}
