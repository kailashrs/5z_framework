package com.android.internal.colorextraction.types;

import android.app.WallpaperColors;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor.GradientColors;
import com.android.internal.graphics.ColorUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Tonal
  implements ExtractionType
{
  private static final boolean DEBUG = true;
  private static final float FIT_WEIGHT_H = 1.0F;
  private static final float FIT_WEIGHT_L = 10.0F;
  private static final float FIT_WEIGHT_S = 1.0F;
  public static final int MAIN_COLOR_DARK = -16777216;
  public static final int MAIN_COLOR_LIGHT = -2039584;
  private static final String TAG = "Tonal";
  public static final int THRESHOLD_COLOR_DARK = -14606047;
  public static final int THRESHOLD_COLOR_LIGHT = -2039584;
  private final ArrayList<ColorRange> mBlacklistedColors;
  private final TonalPalette mGreyPalette;
  private float[] mTmpHSL = new float[3];
  private final ArrayList<TonalPalette> mTonalPalettes;
  
  public Tonal(Context paramContext)
  {
    paramContext = new ConfigParser(paramContext);
    mTonalPalettes = paramContext.getTonalPalettes();
    mBlacklistedColors = paramContext.getBlacklistedColors();
    mGreyPalette = ((TonalPalette)mTonalPalettes.get(0));
    mTonalPalettes.remove(0);
  }
  
  public static void applyFallback(WallpaperColors paramWallpaperColors, ColorExtractor.GradientColors paramGradientColors)
  {
    boolean bool = true;
    if ((paramWallpaperColors == null) || ((paramWallpaperColors.getColorHints() & 0x1) == 0)) {
      bool = false;
    }
    int i;
    if (bool) {
      i = -2039584;
    } else {
      i = -16777216;
    }
    paramGradientColors.setMainColor(i);
    paramGradientColors.setSecondaryColor(i);
    paramGradientColors.setSupportsDarkText(bool);
  }
  
  private void applyFallback(WallpaperColors paramWallpaperColors, ColorExtractor.GradientColors paramGradientColors1, ColorExtractor.GradientColors paramGradientColors2, ColorExtractor.GradientColors paramGradientColors3)
  {
    applyFallback(paramWallpaperColors, paramGradientColors1);
    applyFallback(paramWallpaperColors, paramGradientColors2);
    applyFallback(paramWallpaperColors, paramGradientColors3);
  }
  
  private static int bestFit(TonalPalette paramTonalPalette, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    int i = -1;
    float f1 = Float.POSITIVE_INFINITY;
    int j = 0;
    while (j < h.length)
    {
      float f2 = Math.abs(paramFloat1 - h[j]) * 1.0F + 1.0F * Math.abs(paramFloat2 - s[j]) + 10.0F * Math.abs(paramFloat3 - l[j]);
      float f3 = f1;
      if (f2 < f1)
      {
        f3 = f2;
        i = j;
      }
      j++;
      f1 = f3;
    }
    return i;
  }
  
  private TonalPalette findTonalPalette(float paramFloat1, float paramFloat2)
  {
    if (paramFloat2 < 0.05F) {
      return mGreyPalette;
    }
    Object localObject1 = null;
    float f = Float.POSITIVE_INFINITY;
    int i = mTonalPalettes.size();
    int j = 0;
    Object localObject2;
    for (;;)
    {
      localObject2 = localObject1;
      if (j >= i) {
        break;
      }
      localObject2 = (TonalPalette)mTonalPalettes.get(j);
      if (((paramFloat1 >= minHue) && (paramFloat1 <= maxHue)) || ((maxHue > 1.0F) && (paramFloat1 >= 0.0F) && (paramFloat1 <= fract(maxHue))) || ((minHue < 0.0F) && (paramFloat1 >= fract(minHue)) && (paramFloat1 <= 1.0F))) {
        break;
      }
      if ((paramFloat1 <= minHue) && (minHue - paramFloat1 < f)) {}
      Object localObject3;
      for (paramFloat2 = minHue - paramFloat1;; paramFloat2 = fract(minHue) - paramFloat1)
      {
        localObject3 = localObject2;
        break;
        if ((paramFloat1 >= maxHue) && (paramFloat1 - maxHue < f))
        {
          localObject3 = localObject2;
          paramFloat2 = paramFloat1 - maxHue;
          break;
        }
        if ((maxHue > 1.0F) && (paramFloat1 >= fract(maxHue)) && (paramFloat1 - fract(maxHue) < f))
        {
          localObject3 = localObject2;
          paramFloat2 = paramFloat1 - fract(maxHue);
          break;
        }
        localObject3 = localObject1;
        paramFloat2 = f;
        if (minHue >= 0.0F) {
          break;
        }
        localObject3 = localObject1;
        paramFloat2 = f;
        if (paramFloat1 > fract(minHue)) {
          break;
        }
        localObject3 = localObject1;
        paramFloat2 = f;
        if (fract(minHue) - paramFloat1 >= f) {
          break;
        }
      }
      j++;
      localObject1 = localObject3;
      f = paramFloat2;
    }
    return localObject2;
  }
  
  private static float[] fit(float[] paramArrayOfFloat, float paramFloat1, int paramInt, float paramFloat2, float paramFloat3)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat.length];
    float f = paramArrayOfFloat[paramInt];
    for (paramInt = 0; paramInt < paramArrayOfFloat.length; paramInt++) {
      arrayOfFloat[paramInt] = MathUtils.constrain(paramArrayOfFloat[paramInt] + (paramFloat1 - f), paramFloat2, paramFloat3);
    }
    return arrayOfFloat;
  }
  
  private static float fract(float paramFloat)
  {
    return paramFloat - (float)Math.floor(paramFloat);
  }
  
  private int getColorInt(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    mTmpHSL[0] = (fract(paramArrayOfFloat1[paramInt]) * 360.0F);
    mTmpHSL[1] = paramArrayOfFloat2[paramInt];
    mTmpHSL[2] = paramArrayOfFloat3[paramInt];
    return ColorUtils.HSLToColor(mTmpHSL);
  }
  
  private boolean isBlacklisted(float[] paramArrayOfFloat)
  {
    for (int i = mBlacklistedColors.size() - 1; i >= 0; i--) {
      if (((ColorRange)mBlacklistedColors.get(i)).containsColor(paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2])) {
        return true;
      }
    }
    return false;
  }
  
  private boolean runTonalExtraction(WallpaperColors paramWallpaperColors, ColorExtractor.GradientColors paramGradientColors1, ColorExtractor.GradientColors paramGradientColors2, ColorExtractor.GradientColors paramGradientColors3)
  {
    if (paramWallpaperColors == null) {
      return false;
    }
    Object localObject1 = paramWallpaperColors.getMainColors();
    int i = ((List)localObject1).size();
    int j = paramWallpaperColors.getColorHints();
    boolean bool;
    if ((j & 0x1) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if ((j & 0x4) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    if (i == 0) {
      return false;
    }
    float[] arrayOfFloat = null;
    Object localObject2 = new float[3];
    int k = 0;
    paramWallpaperColors = (WallpaperColors)localObject1;
    while (k < i)
    {
      localObject1 = (Color)paramWallpaperColors.get(k);
      int m = ((Color)localObject1).toArgb();
      ColorUtils.RGBToHSL(Color.red(m), Color.green(m), Color.blue(m), (float[])localObject2);
      if ((j != 0) && (isBlacklisted((float[])localObject2)))
      {
        k++;
      }
      else
      {
        paramWallpaperColors = (WallpaperColors)localObject1;
        break label159;
      }
    }
    paramWallpaperColors = arrayOfFloat;
    label159:
    if (paramWallpaperColors == null) {
      return false;
    }
    j = paramWallpaperColors.toArgb();
    ColorUtils.RGBToHSL(Color.red(j), Color.green(j), Color.blue(j), (float[])localObject2);
    localObject2[0] /= 360.0F;
    localObject1 = findTonalPalette(localObject2[0], localObject2[1]);
    if (localObject1 == null)
    {
      Log.w("Tonal", "Could not find a tonal palette!");
      return false;
    }
    k = bestFit((TonalPalette)localObject1, localObject2[0], localObject2[1], localObject2[2]);
    if (k == -1)
    {
      Log.w("Tonal", "Could not find best fit!");
      return false;
    }
    arrayOfFloat = fit(h, localObject2[0], k, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    paramWallpaperColors = fit(s, localObject2[1], k, 0.0F, 1.0F);
    localObject1 = fit(l, localObject2[2], k, 0.0F, 1.0F);
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("Tonal Palette - index: ");
    ((StringBuilder)localObject2).append(k);
    ((StringBuilder)localObject2).append(". Main color: ");
    ((StringBuilder)localObject2).append(Integer.toHexString(getColorInt(k, arrayOfFloat, paramWallpaperColors, (float[])localObject1)));
    ((StringBuilder)localObject2).append("\nColors: ");
    localObject2 = new StringBuilder(((StringBuilder)localObject2).toString());
    for (j = 0; j < arrayOfFloat.length; j++)
    {
      ((StringBuilder)localObject2).append(Integer.toHexString(getColorInt(j, arrayOfFloat, paramWallpaperColors, (float[])localObject1)));
      if (j < arrayOfFloat.length - 1) {
        ((StringBuilder)localObject2).append(", ");
      }
    }
    Log.d("Tonal", ((StringBuilder)localObject2).toString());
    j = getColorInt(k, arrayOfFloat, paramWallpaperColors, (float[])localObject1);
    ColorUtils.colorToHSL(j, mTmpHSL);
    float f = mTmpHSL[2];
    ColorUtils.colorToHSL(-2039584, mTmpHSL);
    if (f > mTmpHSL[2]) {
      return false;
    }
    ColorUtils.colorToHSL(-14606047, mTmpHSL);
    if (f < mTmpHSL[2]) {
      return false;
    }
    paramGradientColors1.setMainColor(j);
    paramGradientColors1.setSecondaryColor(j);
    if (bool) {
      j = arrayOfFloat.length - 1;
    }
    for (;;)
    {
      break;
      if (k < 2) {
        j = 0;
      } else {
        j = Math.min(k, 3);
      }
    }
    j = getColorInt(j, arrayOfFloat, paramWallpaperColors, (float[])localObject1);
    paramGradientColors2.setMainColor(j);
    paramGradientColors2.setSecondaryColor(j);
    if (bool) {
      j = arrayOfFloat.length - 1;
    }
    for (;;)
    {
      break;
      if (k < 2) {
        j = 0;
      } else {
        j = 2;
      }
    }
    j = getColorInt(j, arrayOfFloat, paramWallpaperColors, (float[])localObject1);
    paramGradientColors3.setMainColor(j);
    paramGradientColors3.setSecondaryColor(j);
    paramGradientColors1.setSupportsDarkText(bool);
    paramGradientColors2.setSupportsDarkText(bool);
    paramGradientColors3.setSupportsDarkText(bool);
    paramWallpaperColors = new StringBuilder();
    paramWallpaperColors.append("Gradients: \n\tNormal ");
    paramWallpaperColors.append(paramGradientColors1);
    paramWallpaperColors.append("\n\tDark ");
    paramWallpaperColors.append(paramGradientColors2);
    paramWallpaperColors.append("\n\tExtra dark: ");
    paramWallpaperColors.append(paramGradientColors3);
    Log.d("Tonal", paramWallpaperColors.toString());
    return true;
  }
  
  public void extractInto(WallpaperColors paramWallpaperColors, ColorExtractor.GradientColors paramGradientColors1, ColorExtractor.GradientColors paramGradientColors2, ColorExtractor.GradientColors paramGradientColors3)
  {
    if (!runTonalExtraction(paramWallpaperColors, paramGradientColors1, paramGradientColors2, paramGradientColors3)) {
      applyFallback(paramWallpaperColors, paramGradientColors1, paramGradientColors2, paramGradientColors3);
    }
  }
  
  @VisibleForTesting
  public List<ColorRange> getBlacklistedColors()
  {
    return mBlacklistedColors;
  }
  
  @VisibleForTesting
  public static class ColorRange
  {
    private Range<Float> mHue;
    private Range<Float> mLightness;
    private Range<Float> mSaturation;
    
    public ColorRange(Range<Float> paramRange1, Range<Float> paramRange2, Range<Float> paramRange3)
    {
      mHue = paramRange1;
      mSaturation = paramRange2;
      mLightness = paramRange3;
    }
    
    public boolean containsColor(float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (!mHue.contains(Float.valueOf(paramFloat1))) {
        return false;
      }
      if (!mSaturation.contains(Float.valueOf(paramFloat2))) {
        return false;
      }
      return mLightness.contains(Float.valueOf(paramFloat3));
    }
    
    public float[] getCenter()
    {
      return new float[] { ((Float)mHue.getLower()).floatValue() + (((Float)mHue.getUpper()).floatValue() - ((Float)mHue.getLower()).floatValue()) / 2.0F, ((Float)mSaturation.getLower()).floatValue() + (((Float)mSaturation.getUpper()).floatValue() - ((Float)mSaturation.getLower()).floatValue()) / 2.0F, ((Float)mLightness.getLower()).floatValue() + (((Float)mLightness.getUpper()).floatValue() - ((Float)mLightness.getLower()).floatValue()) / 2.0F };
    }
    
    public String toString()
    {
      return String.format("H: %s, S: %s, L %s", new Object[] { mHue, mSaturation, mLightness });
    }
  }
  
  @VisibleForTesting
  public static class ConfigParser
  {
    private final ArrayList<Tonal.ColorRange> mBlacklistedColors = new ArrayList();
    private final ArrayList<Tonal.TonalPalette> mTonalPalettes = new ArrayList();
    
    public ConfigParser(Context paramContext)
    {
      try
      {
        paramContext = paramContext.getResources().getXml(18284550);
        for (int i = paramContext.getEventType(); i != 1; i = paramContext.next()) {
          if ((i != 0) && (i != 3))
          {
            Object localObject;
            if (i == 2)
            {
              localObject = paramContext.getName();
              if (((String)localObject).equals("palettes")) {
                parsePalettes(paramContext);
              } else if (((String)localObject).equals("blacklist")) {
                parseBlacklist(paramContext);
              }
            }
            else
            {
              XmlPullParserException localXmlPullParserException = new org/xmlpull/v1/XmlPullParserException;
              localObject = new java/lang/StringBuilder;
              ((StringBuilder)localObject).<init>();
              ((StringBuilder)localObject).append("Invalid XML event ");
              ((StringBuilder)localObject).append(i);
              ((StringBuilder)localObject).append(" - ");
              ((StringBuilder)localObject).append(paramContext.getName());
              localXmlPullParserException.<init>(((StringBuilder)localObject).toString(), paramContext, null);
              throw localXmlPullParserException;
            }
          }
        }
        return;
      }
      catch (XmlPullParserException|IOException paramContext)
      {
        throw new RuntimeException(paramContext);
      }
    }
    
    private void parseBlacklist(XmlPullParser paramXmlPullParser)
      throws XmlPullParserException, IOException
    {
      paramXmlPullParser.require(2, null, "blacklist");
      while (paramXmlPullParser.next() != 3) {
        if (paramXmlPullParser.getEventType() == 2)
        {
          String str = paramXmlPullParser.getName();
          if (str.equals("range"))
          {
            mBlacklistedColors.add(readRange(paramXmlPullParser));
            paramXmlPullParser.next();
          }
          else
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Invalid tag: ");
            localStringBuilder.append(str);
            throw new XmlPullParserException(localStringBuilder.toString(), paramXmlPullParser, null);
          }
        }
      }
    }
    
    private void parsePalettes(XmlPullParser paramXmlPullParser)
      throws XmlPullParserException, IOException
    {
      paramXmlPullParser.require(2, null, "palettes");
      while (paramXmlPullParser.next() != 3) {
        if (paramXmlPullParser.getEventType() == 2)
        {
          String str = paramXmlPullParser.getName();
          if (str.equals("palette"))
          {
            mTonalPalettes.add(readPalette(paramXmlPullParser));
            paramXmlPullParser.next();
          }
          else
          {
            paramXmlPullParser = new StringBuilder();
            paramXmlPullParser.append("Invalid tag: ");
            paramXmlPullParser.append(str);
            throw new XmlPullParserException(paramXmlPullParser.toString());
          }
        }
      }
    }
    
    private float[] readFloatArray(String paramString)
      throws IOException, XmlPullParserException
    {
      paramString = paramString.replaceAll(" ", "").replaceAll("\n", "").split(",");
      float[] arrayOfFloat = new float[paramString.length];
      for (int i = 0; i < paramString.length; i++) {
        arrayOfFloat[i] = Float.parseFloat(paramString[i]);
      }
      return arrayOfFloat;
    }
    
    private Tonal.TonalPalette readPalette(XmlPullParser paramXmlPullParser)
      throws XmlPullParserException, IOException
    {
      paramXmlPullParser.require(2, null, "palette");
      float[] arrayOfFloat1 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "h"));
      float[] arrayOfFloat2 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "s"));
      float[] arrayOfFloat3 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "l"));
      if ((arrayOfFloat1 != null) && (arrayOfFloat2 != null) && (arrayOfFloat3 != null)) {
        return new Tonal.TonalPalette(arrayOfFloat1, arrayOfFloat2, arrayOfFloat3);
      }
      throw new XmlPullParserException("Incomplete range tag.", paramXmlPullParser, null);
    }
    
    private Tonal.ColorRange readRange(XmlPullParser paramXmlPullParser)
      throws XmlPullParserException, IOException
    {
      paramXmlPullParser.require(2, null, "range");
      float[] arrayOfFloat1 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "h"));
      float[] arrayOfFloat2 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "s"));
      float[] arrayOfFloat3 = readFloatArray(paramXmlPullParser.getAttributeValue(null, "l"));
      if ((arrayOfFloat1 != null) && (arrayOfFloat2 != null) && (arrayOfFloat3 != null)) {
        return new Tonal.ColorRange(new Range(Float.valueOf(arrayOfFloat1[0]), Float.valueOf(arrayOfFloat1[1])), new Range(Float.valueOf(arrayOfFloat2[0]), Float.valueOf(arrayOfFloat2[1])), new Range(Float.valueOf(arrayOfFloat3[0]), Float.valueOf(arrayOfFloat3[1])));
      }
      throw new XmlPullParserException("Incomplete range tag.", paramXmlPullParser, null);
    }
    
    public ArrayList<Tonal.ColorRange> getBlacklistedColors()
    {
      return mBlacklistedColors;
    }
    
    public ArrayList<Tonal.TonalPalette> getTonalPalettes()
    {
      return mTonalPalettes;
    }
  }
  
  @VisibleForTesting
  public static class TonalPalette
  {
    public final float[] h;
    public final float[] l;
    public final float maxHue;
    public final float minHue;
    public final float[] s;
    
    TonalPalette(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
    {
      if ((paramArrayOfFloat1.length == paramArrayOfFloat2.length) && (paramArrayOfFloat2.length == paramArrayOfFloat3.length))
      {
        h = paramArrayOfFloat1;
        s = paramArrayOfFloat2;
        l = paramArrayOfFloat3;
        float f1 = Float.POSITIVE_INFINITY;
        float f2 = Float.NEGATIVE_INFINITY;
        int i = paramArrayOfFloat1.length;
        for (int j = 0; j < i; j++)
        {
          float f3 = paramArrayOfFloat1[j];
          f1 = Math.min(f3, f1);
          f2 = Math.max(f3, f2);
        }
        minHue = f1;
        maxHue = f2;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("All arrays should have the same size. h: ");
      localStringBuilder.append(Arrays.toString(paramArrayOfFloat1));
      localStringBuilder.append(" s: ");
      localStringBuilder.append(Arrays.toString(paramArrayOfFloat2));
      localStringBuilder.append(" l: ");
      localStringBuilder.append(Arrays.toString(paramArrayOfFloat3));
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
}
