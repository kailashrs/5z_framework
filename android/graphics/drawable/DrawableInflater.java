package android.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableInflater
{
  private static final HashMap<String, Constructor<? extends Drawable>> CONSTRUCTOR_MAP = new HashMap();
  private final ClassLoader mClassLoader;
  private final Resources mRes;
  
  public DrawableInflater(Resources paramResources, ClassLoader paramClassLoader)
  {
    mRes = paramResources;
    mClassLoader = paramClassLoader;
  }
  
  /* Error */
  private Drawable inflateFromClass(String paramString)
  {
    // Byte code:
    //   0: getstatic 20	android/graphics/drawable/DrawableInflater:CONSTRUCTOR_MAP	Ljava/util/HashMap;
    //   3: astore_2
    //   4: aload_2
    //   5: monitorenter
    //   6: getstatic 20	android/graphics/drawable/DrawableInflater:CONSTRUCTOR_MAP	Ljava/util/HashMap;
    //   9: aload_1
    //   10: invokevirtual 41	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   13: checkcast 43	java/lang/reflect/Constructor
    //   16: astore_3
    //   17: aload_3
    //   18: astore 4
    //   20: aload_3
    //   21: ifnonnull +35 -> 56
    //   24: aload_0
    //   25: getfield 27	android/graphics/drawable/DrawableInflater:mClassLoader	Ljava/lang/ClassLoader;
    //   28: aload_1
    //   29: invokevirtual 49	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   32: ldc 51
    //   34: invokevirtual 57	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   37: iconst_0
    //   38: anewarray 53	java/lang/Class
    //   41: invokevirtual 61	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   44: astore 4
    //   46: getstatic 20	android/graphics/drawable/DrawableInflater:CONSTRUCTOR_MAP	Ljava/util/HashMap;
    //   49: aload_1
    //   50: aload 4
    //   52: invokevirtual 65	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   55: pop
    //   56: aload_2
    //   57: monitorexit
    //   58: aload 4
    //   60: iconst_0
    //   61: anewarray 4	java/lang/Object
    //   64: invokevirtual 69	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   67: checkcast 51	android/graphics/drawable/Drawable
    //   70: astore 4
    //   72: aload 4
    //   74: areturn
    //   75: astore 4
    //   77: aload_2
    //   78: monitorexit
    //   79: aload 4
    //   81: athrow
    //   82: astore 4
    //   84: new 71	java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   91: astore_3
    //   92: aload_3
    //   93: ldc 74
    //   95: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload_3
    //   100: aload_1
    //   101: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: new 80	android/view/InflateException
    //   108: dup
    //   109: aload_3
    //   110: invokevirtual 84	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   113: invokespecial 87	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   116: astore_1
    //   117: aload_1
    //   118: aload 4
    //   120: invokevirtual 91	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   123: pop
    //   124: aload_1
    //   125: athrow
    //   126: astore 4
    //   128: new 71	java/lang/StringBuilder
    //   131: dup
    //   132: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   135: astore_3
    //   136: aload_3
    //   137: ldc 93
    //   139: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload_3
    //   144: aload_1
    //   145: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: new 80	android/view/InflateException
    //   152: dup
    //   153: aload_3
    //   154: invokevirtual 84	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   157: invokespecial 87	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   160: astore_1
    //   161: aload_1
    //   162: aload 4
    //   164: invokevirtual 91	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   167: pop
    //   168: aload_1
    //   169: athrow
    //   170: astore 4
    //   172: new 71	java/lang/StringBuilder
    //   175: dup
    //   176: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   179: astore_3
    //   180: aload_3
    //   181: ldc 95
    //   183: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_3
    //   188: aload_1
    //   189: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: new 80	android/view/InflateException
    //   196: dup
    //   197: aload_3
    //   198: invokevirtual 84	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   201: invokespecial 87	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   204: astore_1
    //   205: aload_1
    //   206: aload 4
    //   208: invokevirtual 91	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   211: pop
    //   212: aload_1
    //   213: athrow
    //   214: astore 4
    //   216: new 71	java/lang/StringBuilder
    //   219: dup
    //   220: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   223: astore_3
    //   224: aload_3
    //   225: ldc 74
    //   227: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: pop
    //   231: aload_3
    //   232: aload_1
    //   233: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: pop
    //   237: new 80	android/view/InflateException
    //   240: dup
    //   241: aload_3
    //   242: invokevirtual 84	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   245: invokespecial 87	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   248: astore_1
    //   249: aload_1
    //   250: aload 4
    //   252: invokevirtual 91	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   255: pop
    //   256: aload_1
    //   257: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	258	0	this	DrawableInflater
    //   0	258	1	paramString	String
    //   16	226	3	localObject1	Object
    //   18	55	4	localObject2	Object
    //   75	5	4	localObject3	Object
    //   82	37	4	localException	Exception
    //   126	37	4	localClassNotFoundException	ClassNotFoundException
    //   170	37	4	localClassCastException	ClassCastException
    //   214	37	4	localNoSuchMethodException	NoSuchMethodException
    // Exception table:
    //   from	to	target	type
    //   6	17	75	finally
    //   24	56	75	finally
    //   56	58	75	finally
    //   77	79	75	finally
    //   0	6	82	java/lang/Exception
    //   58	72	82	java/lang/Exception
    //   79	82	82	java/lang/Exception
    //   0	6	126	java/lang/ClassNotFoundException
    //   58	72	126	java/lang/ClassNotFoundException
    //   79	82	126	java/lang/ClassNotFoundException
    //   0	6	170	java/lang/ClassCastException
    //   58	72	170	java/lang/ClassCastException
    //   79	82	170	java/lang/ClassCastException
    //   0	6	214	java/lang/NoSuchMethodException
    //   58	72	214	java/lang/NoSuchMethodException
    //   79	82	214	java/lang/NoSuchMethodException
  }
  
  private Drawable inflateFromTag(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 2118620333: 
      if (paramString.equals("animated-vector")) {
        i = 10;
      }
      break;
    case 2013827269: 
      if (paramString.equals("animated-rotate")) {
        i = 14;
      }
      break;
    case 1442046129: 
      if (paramString.equals("animated-image")) {
        i = 19;
      }
      break;
    case 1191572447: 
      if (paramString.equals("selector")) {
        i = 0;
      }
      break;
    case 160680263: 
      if (paramString.equals("level-list")) {
        i = 2;
      }
      break;
    case 109399969: 
      if (paramString.equals("shape")) {
        i = 8;
      }
      break;
    case 109250890: 
      if (paramString.equals("scale")) {
        i = 11;
      }
      break;
    case 100360477: 
      if (paramString.equals("inset")) {
        i = 16;
      }
      break;
    case 94842723: 
      if (paramString.equals("color")) {
        i = 7;
      }
      break;
    case 3056464: 
      if (paramString.equals("clip")) {
        i = 12;
      }
      break;
    case -94197862: 
      if (paramString.equals("layer-list")) {
        i = 3;
      }
      break;
    case -510364471: 
      if (paramString.equals("animated-selector")) {
        i = 1;
      }
      break;
    case -820387517: 
      if (paramString.equals("vector")) {
        i = 9;
      }
      break;
    case -925180581: 
      if (paramString.equals("rotate")) {
        i = 13;
      }
      break;
    case -930826704: 
      if (paramString.equals("ripple")) {
        i = 5;
      }
      break;
    case -1388777169: 
      if (paramString.equals("bitmap")) {
        i = 17;
      }
      break;
    case -1493546681: 
      if (paramString.equals("animation-list")) {
        i = 15;
      }
      break;
    case -1671889043: 
      if (paramString.equals("nine-patch")) {
        i = 18;
      }
      break;
    case -1724158635: 
      if (paramString.equals("transition")) {
        i = 4;
      }
      break;
    case -2024464016: 
      if (paramString.equals("adaptive-icon")) {
        i = 6;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return null;
    case 19: 
      return new AnimatedImageDrawable();
    case 18: 
      return new NinePatchDrawable();
    case 17: 
      return new BitmapDrawable();
    case 16: 
      return new InsetDrawable();
    case 15: 
      return new AnimationDrawable();
    case 14: 
      return new AnimatedRotateDrawable();
    case 13: 
      return new RotateDrawable();
    case 12: 
      return new ClipDrawable();
    case 11: 
      return new ScaleDrawable();
    case 10: 
      return new AnimatedVectorDrawable();
    case 9: 
      return new VectorDrawable();
    case 8: 
      return new GradientDrawable();
    case 7: 
      return new ColorDrawable();
    case 6: 
      return new AdaptiveIconDrawable();
    case 5: 
      return new RippleDrawable();
    case 4: 
      return new TransitionDrawable();
    case 3: 
      return new LayerDrawable();
    case 2: 
      return new LevelListDrawable();
    case 1: 
      return new AnimatedStateListDrawable();
    }
    return new StateListDrawable();
  }
  
  public static Drawable loadDrawable(Context paramContext, int paramInt)
  {
    return loadDrawable(paramContext.getResources(), paramContext.getTheme(), paramInt);
  }
  
  public static Drawable loadDrawable(Resources paramResources, Resources.Theme paramTheme, int paramInt)
  {
    return paramResources.getDrawable(paramInt, paramTheme);
  }
  
  public Drawable inflateFromXml(String paramString, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    return inflateFromXmlForDensity(paramString, paramXmlPullParser, paramAttributeSet, 0, paramTheme);
  }
  
  Drawable inflateFromXmlForDensity(String paramString, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, int paramInt, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    String str = paramString;
    if (paramString.equals("drawable"))
    {
      str = paramAttributeSet.getAttributeValue(null, "class");
      if (str == null) {
        throw new InflateException("<drawable> tag must specify class attribute");
      }
    }
    Drawable localDrawable = inflateFromTag(str);
    paramString = localDrawable;
    if (localDrawable == null) {
      paramString = inflateFromClass(str);
    }
    paramString.setSrcDensityOverride(paramInt);
    paramString.inflate(mRes, paramXmlPullParser, paramAttributeSet, paramTheme);
    return paramString;
  }
}
