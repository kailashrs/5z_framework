package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtils
{
  private static final int SEQUENTIALLY = 1;
  private static final int TOGETHER = 0;
  private static ThreadLocal<AnimationState> sAnimationState = new ThreadLocal()
  {
    protected AnimationUtils.AnimationState initialValue()
    {
      return new AnimationUtils.AnimationState(null);
    }
  };
  
  public AnimationUtils() {}
  
  private static Animation createAnimationFromXml(Context paramContext, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    return createAnimationFromXml(paramContext, paramXmlPullParser, null, Xml.asAttributeSet(paramXmlPullParser));
  }
  
  private static Animation createAnimationFromXml(Context paramContext, XmlPullParser paramXmlPullParser, AnimationSet paramAnimationSet, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    Object localObject = null;
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        break label263;
      }
      if (j == 2)
      {
        localObject = paramXmlPullParser.getName();
        if (((String)localObject).equals("set"))
        {
          localObject = new AnimationSet(paramContext, paramAttributeSet);
          createAnimationFromXml(paramContext, paramXmlPullParser, (AnimationSet)localObject, paramAttributeSet);
        }
        else if (((String)localObject).equals("alpha"))
        {
          localObject = new AlphaAnimation(paramContext, paramAttributeSet);
        }
        else if (((String)localObject).equals("scale"))
        {
          localObject = new ScaleAnimation(paramContext, paramAttributeSet);
        }
        else if (((String)localObject).equals("rotate"))
        {
          localObject = new RotateAnimation(paramContext, paramAttributeSet);
        }
        else if (((String)localObject).equals("translate"))
        {
          localObject = new TranslateAnimation(paramContext, paramAttributeSet);
        }
        else
        {
          if (!((String)localObject).equals("cliprect")) {
            break;
          }
          localObject = new ClipRectAnimation(paramContext, paramAttributeSet);
        }
        if (paramAnimationSet != null) {
          paramAnimationSet.addAnimation((Animation)localObject);
        }
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("Unknown animation name: ");
    paramContext.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramContext.toString());
    label263:
    return localObject;
  }
  
  private static Interpolator createInterpolatorFromXml(Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    Object localObject = null;
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        return localObject;
      }
      if (j == 2)
      {
        localObject = Xml.asAttributeSet(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str.equals("linearInterpolator"))
        {
          localObject = new LinearInterpolator();
        }
        else if (str.equals("accelerateInterpolator"))
        {
          localObject = new AccelerateInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("decelerateInterpolator"))
        {
          localObject = new DecelerateInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("accelerateDecelerateInterpolator"))
        {
          localObject = new AccelerateDecelerateInterpolator();
        }
        else if (str.equals("cycleInterpolator"))
        {
          localObject = new CycleInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("anticipateInterpolator"))
        {
          localObject = new AnticipateInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("overshootInterpolator"))
        {
          localObject = new OvershootInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("anticipateOvershootInterpolator"))
        {
          localObject = new AnticipateOvershootInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
        else if (str.equals("bounceInterpolator"))
        {
          localObject = new BounceInterpolator();
        }
        else
        {
          if (!str.equals("pathInterpolator")) {
            break;
          }
          localObject = new PathInterpolator(paramResources, paramTheme, (AttributeSet)localObject);
        }
      }
    }
    paramResources = new StringBuilder();
    paramResources.append("Unknown interpolator name: ");
    paramResources.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramResources.toString());
    return localObject;
  }
  
  private static LayoutAnimationController createLayoutAnimationFromXml(Context paramContext, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    return createLayoutAnimationFromXml(paramContext, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
  }
  
  private static LayoutAnimationController createLayoutAnimationFromXml(Context paramContext, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    Object localObject = null;
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        return localObject;
      }
      if (j == 2)
      {
        localObject = paramXmlPullParser.getName();
        if ("layoutAnimation".equals(localObject))
        {
          localObject = new LayoutAnimationController(paramContext, paramAttributeSet);
        }
        else
        {
          if (!"gridLayoutAnimation".equals(localObject)) {
            break;
          }
          localObject = new GridLayoutAnimationController(paramContext, paramAttributeSet);
        }
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("Unknown layout animation name: ");
    paramContext.append((String)localObject);
    throw new RuntimeException(paramContext.toString());
    return localObject;
  }
  
  public static long currentAnimationTimeMillis()
  {
    AnimationState localAnimationState = (AnimationState)sAnimationState.get();
    if (animationClockLocked) {
      return Math.max(currentVsyncTimeMillis, lastReportedTimeMillis);
    }
    lastReportedTimeMillis = SystemClock.uptimeMillis();
    return lastReportedTimeMillis;
  }
  
  /* Error */
  public static Animation loadAnimation(Context paramContext, int paramInt)
    throws android.content.res.Resources.NotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload_0
    //   8: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   11: iload_1
    //   12: invokevirtual 233	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   15: astore 5
    //   17: aload 5
    //   19: astore 4
    //   21: aload 5
    //   23: astore_2
    //   24: aload 5
    //   26: astore_3
    //   27: aload_0
    //   28: aload 5
    //   30: invokestatic 235	android/view/animation/AnimationUtils:createAnimationFromXml	(Landroid/content/Context;Lorg/xmlpull/v1/XmlPullParser;)Landroid/view/animation/Animation;
    //   33: astore_0
    //   34: aload 5
    //   36: ifnull +10 -> 46
    //   39: aload 5
    //   41: invokeinterface 240 1 0
    //   46: aload_0
    //   47: areturn
    //   48: astore_0
    //   49: goto +145 -> 194
    //   52: astore 5
    //   54: aload_2
    //   55: astore 4
    //   57: new 221	android/content/res/Resources$NotFoundException
    //   60: astore_3
    //   61: aload_2
    //   62: astore 4
    //   64: new 99	java/lang/StringBuilder
    //   67: astore_0
    //   68: aload_2
    //   69: astore 4
    //   71: aload_0
    //   72: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   75: aload_2
    //   76: astore 4
    //   78: aload_0
    //   79: ldc -14
    //   81: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: aload_2
    //   86: astore 4
    //   88: aload_0
    //   89: iload_1
    //   90: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   93: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload_2
    //   98: astore 4
    //   100: aload_3
    //   101: aload_0
    //   102: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   105: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   108: aload_2
    //   109: astore 4
    //   111: aload_3
    //   112: aload 5
    //   114: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   117: pop
    //   118: aload_2
    //   119: astore 4
    //   121: aload_3
    //   122: athrow
    //   123: astore 5
    //   125: aload_3
    //   126: astore 4
    //   128: new 221	android/content/res/Resources$NotFoundException
    //   131: astore_0
    //   132: aload_3
    //   133: astore 4
    //   135: new 99	java/lang/StringBuilder
    //   138: astore_2
    //   139: aload_3
    //   140: astore 4
    //   142: aload_2
    //   143: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   146: aload_3
    //   147: astore 4
    //   149: aload_2
    //   150: ldc -14
    //   152: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload_3
    //   157: astore 4
    //   159: aload_2
    //   160: iload_1
    //   161: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   164: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   167: pop
    //   168: aload_3
    //   169: astore 4
    //   171: aload_0
    //   172: aload_2
    //   173: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   179: aload_3
    //   180: astore 4
    //   182: aload_0
    //   183: aload 5
    //   185: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   188: pop
    //   189: aload_3
    //   190: astore 4
    //   192: aload_0
    //   193: athrow
    //   194: aload 4
    //   196: ifnull +10 -> 206
    //   199: aload 4
    //   201: invokeinterface 240 1 0
    //   206: aload_0
    //   207: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	208	0	paramContext	Context
    //   0	208	1	paramInt	int
    //   1	172	2	localObject1	Object
    //   3	187	3	localObject2	Object
    //   5	195	4	localObject3	Object
    //   15	25	5	localXmlResourceParser	android.content.res.XmlResourceParser
    //   52	61	5	localIOException	IOException
    //   123	61	5	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   7	17	48	finally
    //   27	34	48	finally
    //   57	61	48	finally
    //   64	68	48	finally
    //   71	75	48	finally
    //   78	85	48	finally
    //   88	97	48	finally
    //   100	108	48	finally
    //   111	118	48	finally
    //   121	123	48	finally
    //   128	132	48	finally
    //   135	139	48	finally
    //   142	146	48	finally
    //   149	156	48	finally
    //   159	168	48	finally
    //   171	179	48	finally
    //   182	189	48	finally
    //   192	194	48	finally
    //   7	17	52	java/io/IOException
    //   27	34	52	java/io/IOException
    //   7	17	123	org/xmlpull/v1/XmlPullParserException
    //   27	34	123	org/xmlpull/v1/XmlPullParserException
  }
  
  /* Error */
  public static Interpolator loadInterpolator(Context paramContext, int paramInt)
    throws android.content.res.Resources.NotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload_0
    //   8: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   11: iload_1
    //   12: invokevirtual 233	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   15: astore 5
    //   17: aload 5
    //   19: astore 4
    //   21: aload 5
    //   23: astore_2
    //   24: aload 5
    //   26: astore_3
    //   27: aload_0
    //   28: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   31: aload_0
    //   32: invokevirtual 259	android/content/Context:getTheme	()Landroid/content/res/Resources$Theme;
    //   35: aload 5
    //   37: invokestatic 261	android/view/animation/AnimationUtils:createInterpolatorFromXml	(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;)Landroid/view/animation/Interpolator;
    //   40: astore_0
    //   41: aload 5
    //   43: ifnull +10 -> 53
    //   46: aload 5
    //   48: invokeinterface 240 1 0
    //   53: aload_0
    //   54: areturn
    //   55: astore_0
    //   56: goto +150 -> 206
    //   59: astore_0
    //   60: aload_2
    //   61: astore 4
    //   63: new 221	android/content/res/Resources$NotFoundException
    //   66: astore_3
    //   67: aload_2
    //   68: astore 4
    //   70: new 99	java/lang/StringBuilder
    //   73: astore 5
    //   75: aload_2
    //   76: astore 4
    //   78: aload 5
    //   80: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   83: aload_2
    //   84: astore 4
    //   86: aload 5
    //   88: ldc -14
    //   90: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: aload_2
    //   95: astore 4
    //   97: aload 5
    //   99: iload_1
    //   100: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   103: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: aload_2
    //   108: astore 4
    //   110: aload_3
    //   111: aload 5
    //   113: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   116: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   119: aload_2
    //   120: astore 4
    //   122: aload_3
    //   123: aload_0
    //   124: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   127: pop
    //   128: aload_2
    //   129: astore 4
    //   131: aload_3
    //   132: athrow
    //   133: astore_2
    //   134: aload_3
    //   135: astore 4
    //   137: new 221	android/content/res/Resources$NotFoundException
    //   140: astore 5
    //   142: aload_3
    //   143: astore 4
    //   145: new 99	java/lang/StringBuilder
    //   148: astore_0
    //   149: aload_3
    //   150: astore 4
    //   152: aload_0
    //   153: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   156: aload_3
    //   157: astore 4
    //   159: aload_0
    //   160: ldc -14
    //   162: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload_3
    //   167: astore 4
    //   169: aload_0
    //   170: iload_1
    //   171: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   174: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: pop
    //   178: aload_3
    //   179: astore 4
    //   181: aload 5
    //   183: aload_0
    //   184: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   187: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   190: aload_3
    //   191: astore 4
    //   193: aload 5
    //   195: aload_2
    //   196: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   199: pop
    //   200: aload_3
    //   201: astore 4
    //   203: aload 5
    //   205: athrow
    //   206: aload 4
    //   208: ifnull +10 -> 218
    //   211: aload 4
    //   213: invokeinterface 240 1 0
    //   218: aload_0
    //   219: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	220	0	paramContext	Context
    //   0	220	1	paramInt	int
    //   1	128	2	localObject1	Object
    //   133	63	2	localXmlPullParserException	XmlPullParserException
    //   3	198	3	localObject2	Object
    //   5	207	4	localObject3	Object
    //   15	189	5	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   7	17	55	finally
    //   27	41	55	finally
    //   63	67	55	finally
    //   70	75	55	finally
    //   78	83	55	finally
    //   86	94	55	finally
    //   97	107	55	finally
    //   110	119	55	finally
    //   122	128	55	finally
    //   131	133	55	finally
    //   137	142	55	finally
    //   145	149	55	finally
    //   152	156	55	finally
    //   159	166	55	finally
    //   169	178	55	finally
    //   181	190	55	finally
    //   193	200	55	finally
    //   203	206	55	finally
    //   7	17	59	java/io/IOException
    //   27	41	59	java/io/IOException
    //   7	17	133	org/xmlpull/v1/XmlPullParserException
    //   27	41	133	org/xmlpull/v1/XmlPullParserException
  }
  
  /* Error */
  public static Interpolator loadInterpolator(Resources paramResources, Resources.Theme paramTheme, int paramInt)
    throws android.content.res.Resources.NotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: aload_0
    //   9: iload_2
    //   10: invokevirtual 233	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   13: astore 6
    //   15: aload 6
    //   17: astore 5
    //   19: aload 6
    //   21: astore_3
    //   22: aload 6
    //   24: astore 4
    //   26: aload_0
    //   27: aload_1
    //   28: aload 6
    //   30: invokestatic 261	android/view/animation/AnimationUtils:createInterpolatorFromXml	(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;)Landroid/view/animation/Interpolator;
    //   33: astore_0
    //   34: aload 6
    //   36: ifnull +10 -> 46
    //   39: aload 6
    //   41: invokeinterface 240 1 0
    //   46: aload_0
    //   47: areturn
    //   48: astore_0
    //   49: goto +154 -> 203
    //   52: astore_1
    //   53: aload_3
    //   54: astore 5
    //   56: new 221	android/content/res/Resources$NotFoundException
    //   59: astore_0
    //   60: aload_3
    //   61: astore 5
    //   63: new 99	java/lang/StringBuilder
    //   66: astore 4
    //   68: aload_3
    //   69: astore 5
    //   71: aload 4
    //   73: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   76: aload_3
    //   77: astore 5
    //   79: aload 4
    //   81: ldc -14
    //   83: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_3
    //   88: astore 5
    //   90: aload 4
    //   92: iload_2
    //   93: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   96: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_3
    //   101: astore 5
    //   103: aload_0
    //   104: aload 4
    //   106: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   109: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   112: aload_3
    //   113: astore 5
    //   115: aload_0
    //   116: aload_1
    //   117: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   120: pop
    //   121: aload_3
    //   122: astore 5
    //   124: aload_0
    //   125: athrow
    //   126: astore_0
    //   127: aload 4
    //   129: astore 5
    //   131: new 221	android/content/res/Resources$NotFoundException
    //   134: astore_3
    //   135: aload 4
    //   137: astore 5
    //   139: new 99	java/lang/StringBuilder
    //   142: astore_1
    //   143: aload 4
    //   145: astore 5
    //   147: aload_1
    //   148: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   151: aload 4
    //   153: astore 5
    //   155: aload_1
    //   156: ldc -14
    //   158: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload 4
    //   164: astore 5
    //   166: aload_1
    //   167: iload_2
    //   168: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   171: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload 4
    //   177: astore 5
    //   179: aload_3
    //   180: aload_1
    //   181: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   187: aload 4
    //   189: astore 5
    //   191: aload_3
    //   192: aload_0
    //   193: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   196: pop
    //   197: aload 4
    //   199: astore 5
    //   201: aload_3
    //   202: athrow
    //   203: aload 5
    //   205: ifnull +10 -> 215
    //   208: aload 5
    //   210: invokeinterface 240 1 0
    //   215: aload_0
    //   216: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	217	0	paramResources	Resources
    //   0	217	1	paramTheme	Resources.Theme
    //   0	217	2	paramInt	int
    //   1	201	3	localObject1	Object
    //   3	195	4	localObject2	Object
    //   6	203	5	localObject3	Object
    //   13	27	6	localXmlResourceParser	android.content.res.XmlResourceParser
    // Exception table:
    //   from	to	target	type
    //   8	15	48	finally
    //   26	34	48	finally
    //   56	60	48	finally
    //   63	68	48	finally
    //   71	76	48	finally
    //   79	87	48	finally
    //   90	100	48	finally
    //   103	112	48	finally
    //   115	121	48	finally
    //   124	126	48	finally
    //   131	135	48	finally
    //   139	143	48	finally
    //   147	151	48	finally
    //   155	162	48	finally
    //   166	175	48	finally
    //   179	187	48	finally
    //   191	197	48	finally
    //   201	203	48	finally
    //   8	15	52	java/io/IOException
    //   26	34	52	java/io/IOException
    //   8	15	126	org/xmlpull/v1/XmlPullParserException
    //   26	34	126	org/xmlpull/v1/XmlPullParserException
  }
  
  /* Error */
  public static LayoutAnimationController loadLayoutAnimation(Context paramContext, int paramInt)
    throws android.content.res.Resources.NotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload_0
    //   8: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   11: iload_1
    //   12: invokevirtual 233	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   15: astore 5
    //   17: aload 5
    //   19: astore 4
    //   21: aload 5
    //   23: astore_2
    //   24: aload 5
    //   26: astore_3
    //   27: aload_0
    //   28: aload 5
    //   30: invokestatic 266	android/view/animation/AnimationUtils:createLayoutAnimationFromXml	(Landroid/content/Context;Lorg/xmlpull/v1/XmlPullParser;)Landroid/view/animation/LayoutAnimationController;
    //   33: astore_0
    //   34: aload 5
    //   36: ifnull +10 -> 46
    //   39: aload 5
    //   41: invokeinterface 240 1 0
    //   46: aload_0
    //   47: areturn
    //   48: astore_0
    //   49: goto +151 -> 200
    //   52: astore_3
    //   53: aload_2
    //   54: astore 4
    //   56: new 221	android/content/res/Resources$NotFoundException
    //   59: astore_0
    //   60: aload_2
    //   61: astore 4
    //   63: new 99	java/lang/StringBuilder
    //   66: astore 5
    //   68: aload_2
    //   69: astore 4
    //   71: aload 5
    //   73: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   76: aload_2
    //   77: astore 4
    //   79: aload 5
    //   81: ldc -14
    //   83: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_2
    //   88: astore 4
    //   90: aload 5
    //   92: iload_1
    //   93: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   96: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_2
    //   101: astore 4
    //   103: aload_0
    //   104: aload 5
    //   106: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   109: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   112: aload_2
    //   113: astore 4
    //   115: aload_0
    //   116: aload_3
    //   117: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   120: pop
    //   121: aload_2
    //   122: astore 4
    //   124: aload_0
    //   125: athrow
    //   126: astore_0
    //   127: aload_3
    //   128: astore 4
    //   130: new 221	android/content/res/Resources$NotFoundException
    //   133: astore_2
    //   134: aload_3
    //   135: astore 4
    //   137: new 99	java/lang/StringBuilder
    //   140: astore 5
    //   142: aload_3
    //   143: astore 4
    //   145: aload 5
    //   147: invokespecial 100	java/lang/StringBuilder:<init>	()V
    //   150: aload_3
    //   151: astore 4
    //   153: aload 5
    //   155: ldc -14
    //   157: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload_3
    //   162: astore 4
    //   164: aload 5
    //   166: iload_1
    //   167: invokestatic 248	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   170: invokevirtual 106	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload_3
    //   175: astore 4
    //   177: aload_2
    //   178: aload 5
    //   180: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   183: invokespecial 249	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   186: aload_3
    //   187: astore 4
    //   189: aload_2
    //   190: aload_0
    //   191: invokevirtual 253	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   194: pop
    //   195: aload_3
    //   196: astore 4
    //   198: aload_2
    //   199: athrow
    //   200: aload 4
    //   202: ifnull +10 -> 212
    //   205: aload 4
    //   207: invokeinterface 240 1 0
    //   212: aload_0
    //   213: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	214	0	paramContext	Context
    //   0	214	1	paramInt	int
    //   1	198	2	localObject1	Object
    //   3	24	3	localObject2	Object
    //   52	144	3	localIOException	IOException
    //   5	201	4	localObject3	Object
    //   15	164	5	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   7	17	48	finally
    //   27	34	48	finally
    //   56	60	48	finally
    //   63	68	48	finally
    //   71	76	48	finally
    //   79	87	48	finally
    //   90	100	48	finally
    //   103	112	48	finally
    //   115	121	48	finally
    //   124	126	48	finally
    //   130	134	48	finally
    //   137	142	48	finally
    //   145	150	48	finally
    //   153	161	48	finally
    //   164	174	48	finally
    //   177	186	48	finally
    //   189	195	48	finally
    //   198	200	48	finally
    //   7	17	52	java/io/IOException
    //   27	34	52	java/io/IOException
    //   7	17	126	org/xmlpull/v1/XmlPullParserException
    //   27	34	126	org/xmlpull/v1/XmlPullParserException
  }
  
  public static void lockAnimationClock(long paramLong)
  {
    AnimationState localAnimationState = (AnimationState)sAnimationState.get();
    animationClockLocked = true;
    currentVsyncTimeMillis = paramLong;
  }
  
  public static Animation makeInAnimation(Context paramContext, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramContext = loadAnimation(paramContext, 17432578);
    } else {
      paramContext = loadAnimation(paramContext, 17432721);
    }
    paramContext.setInterpolator(new DecelerateInterpolator());
    paramContext.setStartTime(currentAnimationTimeMillis());
    return paramContext;
  }
  
  public static Animation makeInChildBottomAnimation(Context paramContext)
  {
    paramContext = loadAnimation(paramContext, 17432718);
    paramContext.setInterpolator(new AccelerateInterpolator());
    paramContext.setStartTime(currentAnimationTimeMillis());
    return paramContext;
  }
  
  public static Animation makeOutAnimation(Context paramContext, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramContext = loadAnimation(paramContext, 17432579);
    } else {
      paramContext = loadAnimation(paramContext, 17432724);
    }
    paramContext.setInterpolator(new AccelerateInterpolator());
    paramContext.setStartTime(currentAnimationTimeMillis());
    return paramContext;
  }
  
  public static void unlockAnimationClock()
  {
    sAnimationStategetanimationClockLocked = false;
  }
  
  private static class AnimationState
  {
    boolean animationClockLocked;
    long currentVsyncTimeMillis;
    long lastReportedTimeMillis;
    
    private AnimationState() {}
  }
}
