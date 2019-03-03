package android.media.effect;

public class EffectFactory
{
  public static final String EFFECT_AUTOFIX = "android.media.effect.effects.AutoFixEffect";
  public static final String EFFECT_BACKDROPPER = "android.media.effect.effects.BackDropperEffect";
  public static final String EFFECT_BITMAPOVERLAY = "android.media.effect.effects.BitmapOverlayEffect";
  public static final String EFFECT_BLACKWHITE = "android.media.effect.effects.BlackWhiteEffect";
  public static final String EFFECT_BRIGHTNESS = "android.media.effect.effects.BrightnessEffect";
  public static final String EFFECT_CONTRAST = "android.media.effect.effects.ContrastEffect";
  public static final String EFFECT_CROP = "android.media.effect.effects.CropEffect";
  public static final String EFFECT_CROSSPROCESS = "android.media.effect.effects.CrossProcessEffect";
  public static final String EFFECT_DOCUMENTARY = "android.media.effect.effects.DocumentaryEffect";
  public static final String EFFECT_DUOTONE = "android.media.effect.effects.DuotoneEffect";
  public static final String EFFECT_FILLLIGHT = "android.media.effect.effects.FillLightEffect";
  public static final String EFFECT_FISHEYE = "android.media.effect.effects.FisheyeEffect";
  public static final String EFFECT_FLIP = "android.media.effect.effects.FlipEffect";
  public static final String EFFECT_GRAIN = "android.media.effect.effects.GrainEffect";
  public static final String EFFECT_GRAYSCALE = "android.media.effect.effects.GrayscaleEffect";
  public static final String EFFECT_IDENTITY = "IdentityEffect";
  public static final String EFFECT_LOMOISH = "android.media.effect.effects.LomoishEffect";
  public static final String EFFECT_NEGATIVE = "android.media.effect.effects.NegativeEffect";
  private static final String[] EFFECT_PACKAGES = { "android.media.effect.effects.", "" };
  public static final String EFFECT_POSTERIZE = "android.media.effect.effects.PosterizeEffect";
  public static final String EFFECT_REDEYE = "android.media.effect.effects.RedEyeEffect";
  public static final String EFFECT_ROTATE = "android.media.effect.effects.RotateEffect";
  public static final String EFFECT_SATURATE = "android.media.effect.effects.SaturateEffect";
  public static final String EFFECT_SEPIA = "android.media.effect.effects.SepiaEffect";
  public static final String EFFECT_SHARPEN = "android.media.effect.effects.SharpenEffect";
  public static final String EFFECT_STRAIGHTEN = "android.media.effect.effects.StraightenEffect";
  public static final String EFFECT_TEMPERATURE = "android.media.effect.effects.ColorTemperatureEffect";
  public static final String EFFECT_TINT = "android.media.effect.effects.TintEffect";
  public static final String EFFECT_VIGNETTE = "android.media.effect.effects.VignetteEffect";
  private EffectContext mEffectContext;
  
  EffectFactory(EffectContext paramEffectContext)
  {
    mEffectContext = paramEffectContext;
  }
  
  private static Class getEffectClassByName(String paramString)
  {
    Object localObject1 = null;
    ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
    String[] arrayOfString = EFFECT_PACKAGES;
    int i = arrayOfString.length;
    int j = 0;
    Object localObject3;
    for (;;)
    {
      Object localObject2 = localObject1;
      if (j >= i) {
        break;
      }
      localObject2 = arrayOfString[j];
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append(paramString);
        localObject2 = localClassLoader.loadClass(localStringBuilder.toString());
        localObject1 = localObject2;
        localObject2 = localObject1;
        if (localObject1 != null) {
          localObject2 = localObject1;
        }
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localObject3 = localObject1;
        j++;
        localObject1 = localObject3;
      }
    }
    return localObject3;
  }
  
  /* Error */
  private Effect instantiateEffect(Class paramClass, String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -106
    //   3: invokevirtual 156	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   6: pop
    //   7: aload_1
    //   8: iconst_2
    //   9: anewarray 152	java/lang/Class
    //   12: dup
    //   13: iconst_0
    //   14: ldc -98
    //   16: aastore
    //   17: dup
    //   18: iconst_1
    //   19: ldc 97
    //   21: aastore
    //   22: invokevirtual 162	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   25: astore_3
    //   26: aload_3
    //   27: iconst_2
    //   28: anewarray 4	java/lang/Object
    //   31: dup
    //   32: iconst_0
    //   33: aload_0
    //   34: getfield 110	android/media/effect/EffectFactory:mEffectContext	Landroid/media/effect/EffectContext;
    //   37: aastore
    //   38: dup
    //   39: iconst_1
    //   40: aload_2
    //   41: aastore
    //   42: invokevirtual 168	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   45: checkcast 150	android/media/effect/Effect
    //   48: astore_2
    //   49: aload_2
    //   50: areturn
    //   51: astore_3
    //   52: new 126	java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   59: astore_2
    //   60: aload_2
    //   61: ldc -86
    //   63: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_2
    //   68: aload_1
    //   69: invokevirtual 173	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_2
    //   74: ldc -81
    //   76: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: new 177	java/lang/RuntimeException
    //   83: dup
    //   84: aload_2
    //   85: invokevirtual 135	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   88: aload_3
    //   89: invokespecial 180	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   92: athrow
    //   93: astore_2
    //   94: new 126	java/lang/StringBuilder
    //   97: dup
    //   98: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   101: astore_3
    //   102: aload_3
    //   103: ldc -74
    //   105: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_3
    //   110: aload_1
    //   111: invokevirtual 173	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload_3
    //   116: ldc -72
    //   118: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: new 177	java/lang/RuntimeException
    //   125: dup
    //   126: aload_3
    //   127: invokevirtual 135	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   130: aload_2
    //   131: invokespecial 180	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   134: athrow
    //   135: astore_3
    //   136: new 126	java/lang/StringBuilder
    //   139: dup
    //   140: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   143: astore_2
    //   144: aload_2
    //   145: ldc -70
    //   147: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_2
    //   152: aload_1
    //   153: invokevirtual 173	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload_2
    //   158: ldc -68
    //   160: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: new 190	java/lang/IllegalArgumentException
    //   167: dup
    //   168: aload_2
    //   169: invokevirtual 135	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: aload_3
    //   173: invokespecial 191	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   176: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	177	0	this	EffectFactory
    //   0	177	1	paramClass	Class
    //   0	177	2	paramString	String
    //   25	2	3	localConstructor	java.lang.reflect.Constructor
    //   51	38	3	localThrowable	Throwable
    //   101	26	3	localStringBuilder	StringBuilder
    //   135	38	3	localClassCastException	ClassCastException
    // Exception table:
    //   from	to	target	type
    //   26	49	51	java/lang/Throwable
    //   7	26	93	java/lang/NoSuchMethodException
    //   0	7	135	java/lang/ClassCastException
  }
  
  public static boolean isEffectSupported(String paramString)
  {
    boolean bool;
    if (getEffectClassByName(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Effect createEffect(String paramString)
  {
    Object localObject = getEffectClassByName(paramString);
    if (localObject != null) {
      return instantiateEffect((Class)localObject, paramString);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Cannot instantiate unknown effect '");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("'!");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
}
