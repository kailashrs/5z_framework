package com.android.internal.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.VectorDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.Pair;
import java.util.Arrays;
import java.util.WeakHashMap;

public class NotificationColorUtil
{
  private static final boolean DEBUG = false;
  private static final String TAG = "NotificationColorUtil";
  private static NotificationColorUtil sInstance;
  private static final Object sLock = new Object();
  private final WeakHashMap<Bitmap, Pair<Boolean, Integer>> mGrayscaleBitmapCache = new WeakHashMap();
  private final int mGrayscaleIconMaxSize;
  private final ImageUtils mImageUtils = new ImageUtils();
  
  private NotificationColorUtil(Context paramContext)
  {
    mGrayscaleIconMaxSize = paramContext.getResources().getDimensionPixelSize(17104901);
  }
  
  public static double calculateContrast(int paramInt1, int paramInt2)
  {
    return ColorUtilsFromCompat.calculateContrast(paramInt1, paramInt2);
  }
  
  public static double calculateLuminance(int paramInt)
  {
    return ColorUtilsFromCompat.calculateLuminance(paramInt);
  }
  
  public static int changeColorLightness(int paramInt1, int paramInt2)
  {
    double[] arrayOfDouble = ColorUtilsFromCompat.getTempDouble3Array();
    ColorUtilsFromCompat.colorToLAB(paramInt1, arrayOfDouble);
    arrayOfDouble[0] = Math.max(Math.min(100.0D, arrayOfDouble[0] + paramInt2), 0.0D);
    return ColorUtilsFromCompat.LABToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static CharSequence clearColorSpans(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)paramCharSequence;
      int i = localSpanned.length();
      int j = 0;
      Object[] arrayOfObject = localSpanned.getSpans(0, i, Object.class);
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(localSpanned.toString());
      i = arrayOfObject.length;
      while (j < i)
      {
        Object localObject1 = arrayOfObject[j];
        Object localObject2 = localObject1;
        paramCharSequence = (CharSequence)localObject2;
        if ((localObject2 instanceof CharacterStyle)) {
          paramCharSequence = ((CharacterStyle)localObject1).getUnderlying();
        }
        if ((paramCharSequence instanceof TextAppearanceSpan))
        {
          localObject2 = (TextAppearanceSpan)paramCharSequence;
          if (((TextAppearanceSpan)localObject2).getTextColor() != null) {
            paramCharSequence = new TextAppearanceSpan(((TextAppearanceSpan)localObject2).getFamily(), ((TextAppearanceSpan)localObject2).getTextStyle(), ((TextAppearanceSpan)localObject2).getTextSize(), null, ((TextAppearanceSpan)localObject2).getLinkTextColor());
          }
        }
        else
        {
          if (((paramCharSequence instanceof ForegroundColorSpan)) || ((paramCharSequence instanceof BackgroundColorSpan))) {
            break label188;
          }
          paramCharSequence = localObject1;
        }
        localSpannableStringBuilder.setSpan(paramCharSequence, localSpanned.getSpanStart(localObject1), localSpanned.getSpanEnd(localObject1), localSpanned.getSpanFlags(localObject1));
        label188:
        j++;
      }
      return localSpannableStringBuilder;
    }
    return paramCharSequence;
  }
  
  public static int compositeColors(int paramInt1, int paramInt2)
  {
    return ColorUtilsFromCompat.compositeColors(paramInt1, paramInt2);
  }
  
  private static String contrastChange(int paramInt1, int paramInt2, int paramInt3)
  {
    return String.format("from %.2f:1 to %.2f:1", new Object[] { Double.valueOf(ColorUtilsFromCompat.calculateContrast(paramInt1, paramInt3)), Double.valueOf(ColorUtilsFromCompat.calculateContrast(paramInt2, paramInt3)) });
  }
  
  public static int ensureContrast(int paramInt1, int paramInt2, boolean paramBoolean, double paramDouble)
  {
    if (paramBoolean) {
      paramInt1 = findContrastColorAgainstDark(paramInt1, paramInt2, true, paramDouble);
    } else {
      paramInt1 = findContrastColor(paramInt1, paramInt2, true, paramDouble);
    }
    return paramInt1;
  }
  
  public static int ensureLargeTextContrast(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramInt1 = findContrastColorAgainstDark(paramInt1, paramInt2, true, 3.0D);
    } else {
      paramInt1 = findContrastColor(paramInt1, paramInt2, true, 3.0D);
    }
    return paramInt1;
  }
  
  public static int ensureTextBackgroundColor(int paramInt1, int paramInt2, int paramInt3)
  {
    return findContrastColor(findContrastColor(paramInt1, paramInt3, false, 3.0D), paramInt2, false, 4.5D);
  }
  
  public static int ensureTextContrast(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    return ensureContrast(paramInt1, paramInt2, paramBoolean, 4.5D);
  }
  
  public static int ensureTextContrastOnBlack(int paramInt)
  {
    return findContrastColorAgainstDark(paramInt, -16777216, true, 12.0D);
  }
  
  public static int findAlphaToMeetContrast(int paramInt1, int paramInt2, double paramDouble)
  {
    if (ColorUtilsFromCompat.calculateContrast(paramInt1, paramInt2) >= paramDouble) {
      return paramInt1;
    }
    int i = Color.alpha(paramInt1);
    int j = Color.red(paramInt1);
    int k = Color.green(paramInt1);
    int m = Color.blue(paramInt1);
    int n = 255;
    for (paramInt1 = 0; (paramInt1 < 15) && (n - i > 0); paramInt1++)
    {
      int i1 = (i + n) / 2;
      if (ColorUtilsFromCompat.calculateContrast(Color.argb(i1, j, k, m), paramInt2) > paramDouble) {
        n = i1;
      } else {
        i = i1;
      }
    }
    return Color.argb(n, j, k, m);
  }
  
  public static int findContrastColor(int paramInt1, int paramInt2, boolean paramBoolean, double paramDouble)
  {
    int i;
    if (paramBoolean) {
      i = paramInt1;
    } else {
      i = paramInt2;
    }
    if (!paramBoolean) {
      paramInt2 = paramInt1;
    }
    if (ColorUtilsFromCompat.calculateContrast(i, paramInt2) >= paramDouble) {
      return paramInt1;
    }
    double[] arrayOfDouble = new double[3];
    if (paramBoolean) {
      paramInt1 = i;
    } else {
      paramInt1 = paramInt2;
    }
    ColorUtilsFromCompat.colorToLAB(paramInt1, arrayOfDouble);
    double d1 = 0.0D;
    paramInt1 = 0;
    double d2 = arrayOfDouble[0];
    double d3 = arrayOfDouble[1];
    double d4 = arrayOfDouble[2];
    while ((paramInt1 < 15) && (d2 - d1 > 1.0E-5D))
    {
      double d5 = (d1 + d2) / 2.0D;
      if (paramBoolean) {
        i = ColorUtilsFromCompat.LABToColor(d5, d3, d4);
      } else {
        paramInt2 = ColorUtilsFromCompat.LABToColor(d5, d3, d4);
      }
      if (ColorUtilsFromCompat.calculateContrast(i, paramInt2) > paramDouble) {
        d1 = d5;
      } else {
        d2 = d5;
      }
      paramInt1++;
    }
    return ColorUtilsFromCompat.LABToColor(d1, d3, d4);
  }
  
  public static int findContrastColorAgainstDark(int paramInt1, int paramInt2, boolean paramBoolean, double paramDouble)
  {
    int i;
    if (paramBoolean) {
      i = paramInt1;
    } else {
      i = paramInt2;
    }
    if (!paramBoolean) {
      paramInt2 = paramInt1;
    }
    if (ColorUtilsFromCompat.calculateContrast(i, paramInt2) >= paramDouble) {
      return paramInt1;
    }
    float[] arrayOfFloat = new float[3];
    if (paramBoolean) {
      paramInt1 = i;
    } else {
      paramInt1 = paramInt2;
    }
    ColorUtilsFromCompat.colorToHSL(paramInt1, arrayOfFloat);
    float f1 = arrayOfFloat[2];
    float f2 = 1.0F;
    for (paramInt1 = 0; (paramInt1 < 15) && (f2 - f1 > 1.0E-5D); paramInt1++)
    {
      float f3 = (f1 + f2) / 2.0F;
      arrayOfFloat[2] = f3;
      if (paramBoolean) {
        i = ColorUtilsFromCompat.HSLToColor(arrayOfFloat);
      } else {
        paramInt2 = ColorUtilsFromCompat.HSLToColor(arrayOfFloat);
      }
      if (ColorUtilsFromCompat.calculateContrast(i, paramInt2) > paramDouble) {
        f2 = f3;
      } else {
        f1 = f3;
      }
    }
    if (paramBoolean) {
      paramInt1 = i;
    } else {
      paramInt1 = paramInt2;
    }
    return paramInt1;
  }
  
  public static NotificationColorUtil getInstance(Context paramContext)
  {
    synchronized (sLock)
    {
      if (sInstance == null)
      {
        NotificationColorUtil localNotificationColorUtil = new com/android/internal/util/NotificationColorUtil;
        localNotificationColorUtil.<init>(paramContext);
        sInstance = localNotificationColorUtil;
      }
      paramContext = sInstance;
      return paramContext;
    }
  }
  
  public static int getShiftedColor(int paramInt1, int paramInt2)
  {
    double[] arrayOfDouble = ColorUtilsFromCompat.getTempDouble3Array();
    ColorUtilsFromCompat.colorToLAB(paramInt1, arrayOfDouble);
    if (arrayOfDouble[0] >= 4.0D) {
      arrayOfDouble[0] = Math.max(0.0D, arrayOfDouble[0] - paramInt2);
    } else {
      arrayOfDouble[0] = Math.min(100.0D, arrayOfDouble[0] + paramInt2);
    }
    return ColorUtilsFromCompat.LABToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
  }
  
  public static boolean isColorLight(int paramInt)
  {
    boolean bool;
    if (calculateLuminance(paramInt) > 0.5D) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int processColor(int paramInt)
  {
    return Color.argb(Color.alpha(paramInt), 255 - Color.red(paramInt), 255 - Color.green(paramInt), 255 - Color.blue(paramInt));
  }
  
  private TextAppearanceSpan processTextAppearanceSpan(TextAppearanceSpan paramTextAppearanceSpan)
  {
    ColorStateList localColorStateList = paramTextAppearanceSpan.getTextColor();
    if (localColorStateList != null)
    {
      Object localObject1 = localColorStateList.getColors();
      int i = 0;
      int j = 0;
      while (j < localObject1.length)
      {
        Object localObject2 = localObject1;
        int k = i;
        if (ImageUtils.isGrayscale(localObject1[j]))
        {
          localObject2 = localObject1;
          if (i == 0) {
            localObject2 = Arrays.copyOf((int[])localObject1, localObject1.length);
          }
          localObject2[j] = processColor(localObject2[j]);
          k = 1;
        }
        j++;
        localObject1 = localObject2;
        i = k;
      }
      if (i != 0) {
        return new TextAppearanceSpan(paramTextAppearanceSpan.getFamily(), paramTextAppearanceSpan.getTextStyle(), paramTextAppearanceSpan.getTextSize(), new ColorStateList(localColorStateList.getStates(), (int[])localObject1), paramTextAppearanceSpan.getLinkTextColor());
      }
    }
    return paramTextAppearanceSpan;
  }
  
  public static int resolveAmbientColor(Context paramContext, int paramInt)
  {
    paramInt = ensureTextContrastOnBlack(resolveColor(paramContext, paramInt));
    return paramInt;
  }
  
  public static int resolveColor(Context paramContext, int paramInt)
  {
    if (paramInt == 0) {
      return paramContext.getColor(17170776);
    }
    return paramInt;
  }
  
  public static int resolveContrastColor(Context paramContext, int paramInt1, int paramInt2)
  {
    return resolveContrastColor(paramContext, paramInt1, paramInt2, false);
  }
  
  public static int resolveContrastColor(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    paramInt1 = ensureTextContrast(resolveColor(paramContext, paramInt1), paramInt2, paramBoolean);
    return paramInt1;
  }
  
  public static int resolveDefaultColor(Context paramContext, int paramInt)
  {
    if (shouldUseDark(paramInt)) {
      return paramContext.getColor(17170776);
    }
    return paramContext.getColor(17170775);
  }
  
  public static int resolvePrimaryColor(Context paramContext, int paramInt)
  {
    if (shouldUseDark(paramInt)) {
      return paramContext.getColor(17170779);
    }
    return paramContext.getColor(17170778);
  }
  
  public static int resolveSecondaryColor(Context paramContext, int paramInt)
  {
    if (shouldUseDark(paramInt)) {
      return paramContext.getColor(17170782);
    }
    return paramContext.getColor(17170781);
  }
  
  public static boolean satisfiesTextContrast(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (calculateContrast(paramInt2, paramInt1) >= 4.5D) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean shouldUseDark(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2;
    if (paramInt == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3 = bool2;
    if (!bool2)
    {
      bool2 = bool1;
      if (ColorUtilsFromCompat.calculateLuminance(paramInt) > 0.5D) {
        bool2 = true;
      }
      bool3 = bool2;
    }
    return bool3;
  }
  
  public CharSequence invertCharSequenceColors(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)paramCharSequence;
      int i = localSpanned.length();
      int j = 0;
      Object[] arrayOfObject = localSpanned.getSpans(0, i, Object.class);
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(localSpanned.toString());
      i = arrayOfObject.length;
      while (j < i)
      {
        paramCharSequence = arrayOfObject[j];
        Object localObject1 = paramCharSequence;
        Object localObject2 = localObject1;
        if ((localObject1 instanceof CharacterStyle)) {
          localObject2 = ((CharacterStyle)paramCharSequence).getUnderlying();
        }
        if ((localObject2 instanceof TextAppearanceSpan))
        {
          localObject1 = processTextAppearanceSpan((TextAppearanceSpan)paramCharSequence);
          if (localObject1 != localObject2) {
            localObject2 = localObject1;
          } else {
            localObject2 = paramCharSequence;
          }
        }
        else if ((localObject2 instanceof ForegroundColorSpan))
        {
          localObject2 = new ForegroundColorSpan(processColor(((ForegroundColorSpan)localObject2).getForegroundColor()));
        }
        else
        {
          localObject2 = paramCharSequence;
        }
        localSpannableStringBuilder.setSpan(localObject2, localSpanned.getSpanStart(paramCharSequence), localSpanned.getSpanEnd(paramCharSequence), localSpanned.getSpanFlags(paramCharSequence));
        j++;
      }
      return localSpannableStringBuilder;
    }
    return paramCharSequence;
  }
  
  public boolean isGrayscaleIcon(Context paramContext, int paramInt)
  {
    if (paramInt != 0) {
      try
      {
        boolean bool = isGrayscaleIcon(paramContext.getDrawable(paramInt));
        return bool;
      }
      catch (Resources.NotFoundException paramContext)
      {
        paramContext = new StringBuilder();
        paramContext.append("Drawable not found: ");
        paramContext.append(paramInt);
        Log.e("NotificationColorUtil", paramContext.toString());
        return false;
      }
    }
    return false;
  }
  
  public boolean isGrayscaleIcon(Context paramContext, Icon paramIcon)
  {
    if (paramIcon == null) {
      return false;
    }
    switch (paramIcon.getType())
    {
    default: 
      return false;
    case 2: 
      return isGrayscaleIcon(paramContext, paramIcon.getResId());
    }
    return isGrayscaleIcon(paramIcon.getBitmap());
  }
  
  public boolean isGrayscaleIcon(Bitmap paramBitmap)
  {
    if ((paramBitmap.getWidth() <= mGrayscaleIconMaxSize) && (paramBitmap.getHeight() <= mGrayscaleIconMaxSize)) {
      synchronized (sLock)
      {
        Pair localPair = (Pair)mGrayscaleBitmapCache.get(paramBitmap);
        boolean bool;
        if ((localPair != null) && (((Integer)second).intValue() == paramBitmap.getGenerationId()))
        {
          bool = ((Boolean)first).booleanValue();
          return bool;
        }
        synchronized (mImageUtils)
        {
          bool = mImageUtils.isGrayscale(paramBitmap);
          int i = paramBitmap.getGenerationId();
          synchronized (sLock)
          {
            mGrayscaleBitmapCache.put(paramBitmap, Pair.create(Boolean.valueOf(bool), Integer.valueOf(i)));
            return bool;
          }
        }
      }
    }
    return false;
  }
  
  public boolean isGrayscaleIcon(Drawable paramDrawable)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramDrawable == null) {
      return false;
    }
    boolean bool3;
    if ((paramDrawable instanceof BitmapDrawable))
    {
      paramDrawable = (BitmapDrawable)paramDrawable;
      bool3 = bool2;
      if (paramDrawable.getBitmap() != null)
      {
        bool3 = bool2;
        if (isGrayscaleIcon(paramDrawable.getBitmap())) {
          bool3 = true;
        }
      }
      return bool3;
    }
    if ((paramDrawable instanceof AnimationDrawable))
    {
      paramDrawable = (AnimationDrawable)paramDrawable;
      bool3 = bool1;
      if (paramDrawable.getNumberOfFrames() > 0)
      {
        bool3 = bool1;
        if (isGrayscaleIcon(paramDrawable.getFrame(0))) {
          bool3 = true;
        }
      }
      return bool3;
    }
    return (paramDrawable instanceof VectorDrawable);
  }
  
  private static class ColorUtilsFromCompat
  {
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
    private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();
    private static final double XYZ_EPSILON = 0.008856D;
    private static final double XYZ_KAPPA = 903.3D;
    private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
    private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
    private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;
    
    private ColorUtilsFromCompat() {}
    
    public static int HSLToColor(float[] paramArrayOfFloat)
    {
      float f1 = paramArrayOfFloat[0];
      float f2 = paramArrayOfFloat[1];
      float f3 = paramArrayOfFloat[2];
      f2 = (1.0F - Math.abs(2.0F * f3 - 1.0F)) * f2;
      float f4 = f3 - 0.5F * f2;
      f3 = (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F)) * f2;
      int i = (int)f1 / 60;
      int j = 0;
      int k = 0;
      int m = 0;
      switch (i)
      {
      default: 
        break;
      case 5: 
      case 6: 
        j = Math.round((f2 + f4) * 255.0F);
        k = Math.round(255.0F * f4);
        m = Math.round(255.0F * (f3 + f4));
        break;
      case 4: 
        j = Math.round((f3 + f4) * 255.0F);
        k = Math.round(255.0F * f4);
        m = Math.round(255.0F * (f2 + f4));
        break;
      case 3: 
        j = Math.round(255.0F * f4);
        k = Math.round((f3 + f4) * 255.0F);
        m = Math.round(255.0F * (f2 + f4));
        break;
      case 2: 
        j = Math.round(255.0F * f4);
        k = Math.round((f2 + f4) * 255.0F);
        m = Math.round(255.0F * (f3 + f4));
        break;
      case 1: 
        j = Math.round((f3 + f4) * 255.0F);
        k = Math.round((f2 + f4) * 255.0F);
        m = Math.round(255.0F * f4);
        break;
      case 0: 
        j = Math.round((f2 + f4) * 255.0F);
        k = Math.round((f3 + f4) * 255.0F);
        m = Math.round(255.0F * f4);
      }
      return Color.rgb(constrain(j, 0, 255), constrain(k, 0, 255), constrain(m, 0, 255));
    }
    
    public static int LABToColor(double paramDouble1, double paramDouble2, double paramDouble3)
    {
      double[] arrayOfDouble = getTempDouble3Array();
      LABToXYZ(paramDouble1, paramDouble2, paramDouble3, arrayOfDouble);
      return XYZToColor(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2]);
    }
    
    public static void LABToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfDouble)
    {
      double d1 = (paramDouble1 + 16.0D) / 116.0D;
      double d2 = paramDouble2 / 500.0D + d1;
      double d3 = d1 - paramDouble3 / 200.0D;
      paramDouble2 = Math.pow(d2, 3.0D);
      if (paramDouble2 <= 0.008856D) {
        paramDouble2 = (116.0D * d2 - 16.0D) / 903.3D;
      }
      if (paramDouble1 > 7.9996247999999985D) {
        paramDouble1 = Math.pow(d1, 3.0D);
      } else {
        paramDouble1 /= 903.3D;
      }
      paramDouble3 = Math.pow(d3, 3.0D);
      if (paramDouble3 <= 0.008856D) {
        paramDouble3 = (116.0D * d3 - 16.0D) / 903.3D;
      }
      paramArrayOfDouble[0] = (95.047D * paramDouble2);
      paramArrayOfDouble[1] = (100.0D * paramDouble1);
      paramArrayOfDouble[2] = (108.883D * paramDouble3);
    }
    
    public static void RGBToHSL(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat)
    {
      float f1 = paramInt1 / 255.0F;
      float f2 = paramInt2 / 255.0F;
      float f3 = paramInt3 / 255.0F;
      float f4 = Math.max(f1, Math.max(f2, f3));
      float f5 = Math.min(f1, Math.min(f2, f3));
      float f6 = f4 - f5;
      float f7 = (f4 + f5) / 2.0F;
      if (f4 == f5)
      {
        f2 = 0.0F;
        f6 = 0.0F;
      }
      else
      {
        if (f4 == f1) {
          f2 = (f2 - f3) / f6 % 6.0F;
        }
        for (;;)
        {
          break;
          if (f4 == f2) {
            f2 = (f3 - f1) / f6 + 2.0F;
          } else {
            f2 = (f1 - f2) / f6 + 4.0F;
          }
        }
        f4 = f6 / (1.0F - Math.abs(2.0F * f7 - 1.0F));
        f6 = f2;
        f2 = f4;
      }
      f4 = 60.0F * f6 % 360.0F;
      f6 = f4;
      if (f4 < 0.0F) {
        f6 = f4 + 360.0F;
      }
      paramArrayOfFloat[0] = constrain(f6, 0.0F, 360.0F);
      paramArrayOfFloat[1] = constrain(f2, 0.0F, 1.0F);
      paramArrayOfFloat[2] = constrain(f7, 0.0F, 1.0F);
    }
    
    public static void RGBToLAB(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfDouble)
    {
      RGBToXYZ(paramInt1, paramInt2, paramInt3, paramArrayOfDouble);
      XYZToLAB(paramArrayOfDouble[0], paramArrayOfDouble[1], paramArrayOfDouble[2], paramArrayOfDouble);
    }
    
    public static void RGBToXYZ(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfDouble)
    {
      if (paramArrayOfDouble.length == 3)
      {
        double d1 = paramInt1 / 255.0D;
        if (d1 < 0.04045D) {
          d1 /= 12.92D;
        } else {
          d1 = Math.pow((d1 + 0.055D) / 1.055D, 2.4D);
        }
        double d2 = paramInt2 / 255.0D;
        if (d2 < 0.04045D) {
          d2 /= 12.92D;
        } else {
          d2 = Math.pow((d2 + 0.055D) / 1.055D, 2.4D);
        }
        double d3 = paramInt3 / 255.0D;
        if (d3 < 0.04045D) {
          d3 /= 12.92D;
        } else {
          d3 = Math.pow((0.055D + d3) / 1.055D, 2.4D);
        }
        paramArrayOfDouble[0] = ((0.4124D * d1 + 0.3576D * d2 + 0.1805D * d3) * 100.0D);
        paramArrayOfDouble[1] = ((0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3) * 100.0D);
        paramArrayOfDouble[2] = (100.0D * (0.0193D * d1 + 0.1192D * d2 + 0.9505D * d3));
        return;
      }
      throw new IllegalArgumentException("outXyz must have a length of 3.");
    }
    
    public static int XYZToColor(double paramDouble1, double paramDouble2, double paramDouble3)
    {
      double d1 = (3.2406D * paramDouble1 + -1.5372D * paramDouble2 + -0.4986D * paramDouble3) / 100.0D;
      double d2 = (-0.9689D * paramDouble1 + 1.8758D * paramDouble2 + 0.0415D * paramDouble3) / 100.0D;
      paramDouble3 = (0.0557D * paramDouble1 + -0.204D * paramDouble2 + 1.057D * paramDouble3) / 100.0D;
      if (d1 > 0.0031308D) {
        paramDouble1 = Math.pow(d1, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
        paramDouble1 = 12.92D * d1;
      }
      if (d2 > 0.0031308D) {
        paramDouble2 = Math.pow(d2, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
        paramDouble2 = 12.92D * d2;
      }
      if (paramDouble3 > 0.0031308D) {
        paramDouble3 = 1.055D * Math.pow(paramDouble3, 0.4166666666666667D) - 0.055D;
      } else {
        paramDouble3 = 12.92D * paramDouble3;
      }
      return Color.rgb(constrain((int)Math.round(paramDouble1 * 255.0D), 0, 255), constrain((int)Math.round(paramDouble2 * 255.0D), 0, 255), constrain((int)Math.round(255.0D * paramDouble3), 0, 255));
    }
    
    public static void XYZToLAB(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfDouble)
    {
      if (paramArrayOfDouble.length == 3)
      {
        paramDouble1 = pivotXyzComponent(paramDouble1 / 95.047D);
        paramDouble2 = pivotXyzComponent(paramDouble2 / 100.0D);
        paramDouble3 = pivotXyzComponent(paramDouble3 / 108.883D);
        paramArrayOfDouble[0] = Math.max(0.0D, 116.0D * paramDouble2 - 16.0D);
        paramArrayOfDouble[1] = (500.0D * (paramDouble1 - paramDouble2));
        paramArrayOfDouble[2] = (200.0D * (paramDouble2 - paramDouble3));
        return;
      }
      throw new IllegalArgumentException("outLab must have a length of 3.");
    }
    
    public static double calculateContrast(int paramInt1, int paramInt2)
    {
      if (Color.alpha(paramInt2) != 255)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("background can not be translucent: #");
        localStringBuilder.append(Integer.toHexString(paramInt2));
        Log.wtf("NotificationColorUtil", localStringBuilder.toString());
      }
      int i = paramInt1;
      if (Color.alpha(paramInt1) < 255) {
        i = compositeColors(paramInt1, paramInt2);
      }
      double d1 = calculateLuminance(i) + 0.05D;
      double d2 = calculateLuminance(paramInt2) + 0.05D;
      return Math.max(d1, d2) / Math.min(d1, d2);
    }
    
    public static double calculateLuminance(int paramInt)
    {
      double[] arrayOfDouble = getTempDouble3Array();
      colorToXYZ(paramInt, arrayOfDouble);
      return arrayOfDouble[1] / 100.0D;
    }
    
    public static void colorToHSL(int paramInt, float[] paramArrayOfFloat)
    {
      RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfFloat);
    }
    
    public static void colorToLAB(int paramInt, double[] paramArrayOfDouble)
    {
      RGBToLAB(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble);
    }
    
    public static void colorToXYZ(int paramInt, double[] paramArrayOfDouble)
    {
      RGBToXYZ(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOfDouble);
    }
    
    private static int compositeAlpha(int paramInt1, int paramInt2)
    {
      return 255 - (255 - paramInt2) * (255 - paramInt1) / 255;
    }
    
    public static int compositeColors(int paramInt1, int paramInt2)
    {
      int i = Color.alpha(paramInt2);
      int j = Color.alpha(paramInt1);
      int k = compositeAlpha(j, i);
      return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
    }
    
    private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      if (paramInt5 == 0) {
        return 0;
      }
      return (255 * paramInt1 * paramInt2 + paramInt3 * paramInt4 * (255 - paramInt2)) / (paramInt5 * 255);
    }
    
    private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
    {
      if (paramFloat1 < paramFloat2) {
        paramFloat1 = paramFloat2;
      } else if (paramFloat1 > paramFloat3) {
        paramFloat1 = paramFloat3;
      }
      return paramFloat1;
    }
    
    private static int constrain(int paramInt1, int paramInt2, int paramInt3)
    {
      if (paramInt1 < paramInt2) {
        paramInt1 = paramInt2;
      } else if (paramInt1 > paramInt3) {
        paramInt1 = paramInt3;
      }
      return paramInt1;
    }
    
    public static double[] getTempDouble3Array()
    {
      double[] arrayOfDouble1 = (double[])TEMP_ARRAY.get();
      double[] arrayOfDouble2 = arrayOfDouble1;
      if (arrayOfDouble1 == null)
      {
        arrayOfDouble2 = new double[3];
        TEMP_ARRAY.set(arrayOfDouble2);
      }
      return arrayOfDouble2;
    }
    
    private static double pivotXyzComponent(double paramDouble)
    {
      if (paramDouble > 0.008856D) {
        paramDouble = Math.pow(paramDouble, 0.3333333333333333D);
      } else {
        paramDouble = (903.3D * paramDouble + 16.0D) / 116.0D;
      }
      return paramDouble;
    }
  }
}
