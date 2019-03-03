package android.transition;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater
{
  private static final Class<?>[] sConstructorSignature = { Context.class, AttributeSet.class };
  private static final ArrayMap<String, Constructor> sConstructors = new ArrayMap();
  private Context mContext;
  
  private TransitionInflater(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private Object createCustom(AttributeSet paramAttributeSet, Class paramClass, String paramString)
  {
    String str = paramAttributeSet.getAttributeValue(null, "class");
    if (str != null) {
      try
      {
        synchronized (sConstructors)
        {
          Constructor localConstructor = (Constructor)sConstructors.get(str);
          paramString = localConstructor;
          if (localConstructor == null)
          {
            Class localClass = mContext.getClassLoader().loadClass(str).asSubclass(paramClass);
            paramString = localConstructor;
            if (localClass != null)
            {
              paramString = localClass.getConstructor(sConstructorSignature);
              paramString.setAccessible(true);
              sConstructors.put(str, paramString);
            }
          }
          paramAttributeSet = paramString.newInstance(new Object[] { mContext, paramAttributeSet });
          return paramAttributeSet;
        }
        paramAttributeSet = new StringBuilder();
      }
      catch (IllegalAccessException paramAttributeSet)
      {
        paramString = new StringBuilder();
        paramString.append("Could not instantiate ");
        paramString.append(paramClass);
        paramString.append(" class ");
        paramString.append(str);
        throw new InflateException(paramString.toString(), paramAttributeSet);
      }
      catch (NoSuchMethodException paramAttributeSet)
      {
        paramString = new StringBuilder();
        paramString.append("Could not instantiate ");
        paramString.append(paramClass);
        paramString.append(" class ");
        paramString.append(str);
        throw new InflateException(paramString.toString(), paramAttributeSet);
      }
      catch (InvocationTargetException paramAttributeSet)
      {
        paramString = new StringBuilder();
        paramString.append("Could not instantiate ");
        paramString.append(paramClass);
        paramString.append(" class ");
        paramString.append(str);
        throw new InflateException(paramString.toString(), paramAttributeSet);
      }
      catch (ClassNotFoundException paramString)
      {
        paramAttributeSet = new StringBuilder();
        paramAttributeSet.append("Could not instantiate ");
        paramAttributeSet.append(paramClass);
        paramAttributeSet.append(" class ");
        paramAttributeSet.append(str);
        throw new InflateException(paramAttributeSet.toString(), paramString);
      }
      catch (InstantiationException paramString)
      {
        paramAttributeSet = new StringBuilder();
        paramAttributeSet.append("Could not instantiate ");
        paramAttributeSet.append(paramClass);
        paramAttributeSet.append(" class ");
        paramAttributeSet.append(str);
        throw new InflateException(paramAttributeSet.toString(), paramString);
      }
    }
    paramAttributeSet.append(paramString);
    paramAttributeSet.append(" tag must have a 'class' attribute");
    throw new InflateException(paramAttributeSet.toString());
  }
  
  private Transition createTransitionFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    int i = paramXmlPullParser.getDepth();
    TransitionSet localTransitionSet;
    if ((paramTransition instanceof TransitionSet)) {
      localTransitionSet = (TransitionSet)paramTransition;
    } else {
      localTransitionSet = null;
    }
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        break label618;
      }
      if (j == 2)
      {
        Object localObject2 = paramXmlPullParser.getName();
        if ("fade".equals(localObject2))
        {
          localObject1 = new Fade(mContext, paramAttributeSet);
        }
        else if ("changeBounds".equals(localObject2))
        {
          localObject1 = new ChangeBounds(mContext, paramAttributeSet);
        }
        else if ("slide".equals(localObject2))
        {
          localObject1 = new Slide(mContext, paramAttributeSet);
        }
        else if ("explode".equals(localObject2))
        {
          localObject1 = new Explode(mContext, paramAttributeSet);
        }
        else if ("changeImageTransform".equals(localObject2))
        {
          localObject1 = new ChangeImageTransform(mContext, paramAttributeSet);
        }
        else if ("changeTransform".equals(localObject2))
        {
          localObject1 = new ChangeTransform(mContext, paramAttributeSet);
        }
        else if ("changeClipBounds".equals(localObject2))
        {
          localObject1 = new ChangeClipBounds(mContext, paramAttributeSet);
        }
        else if ("autoTransition".equals(localObject2))
        {
          localObject1 = new AutoTransition(mContext, paramAttributeSet);
        }
        else if ("recolor".equals(localObject2))
        {
          localObject1 = new Recolor(mContext, paramAttributeSet);
        }
        else if ("changeScroll".equals(localObject2))
        {
          localObject1 = new ChangeScroll(mContext, paramAttributeSet);
        }
        else if ("transitionSet".equals(localObject2))
        {
          localObject1 = new TransitionSet(mContext, paramAttributeSet);
        }
        else if ("transition".equals(localObject2))
        {
          localObject1 = (Transition)createCustom(paramAttributeSet, Transition.class, "transition");
        }
        else if ("targets".equals(localObject2))
        {
          getTargetIds(paramXmlPullParser, paramAttributeSet, paramTransition);
        }
        else if ("arcMotion".equals(localObject2))
        {
          paramTransition.setPathMotion(new ArcMotion(mContext, paramAttributeSet));
        }
        else if ("pathMotion".equals(localObject2))
        {
          paramTransition.setPathMotion((PathMotion)createCustom(paramAttributeSet, PathMotion.class, "pathMotion"));
        }
        else
        {
          if (!"patternPathMotion".equals(localObject2)) {
            break;
          }
          paramTransition.setPathMotion(new PatternPathMotion(mContext, paramAttributeSet));
        }
        localObject2 = localObject1;
        if (localObject1 != null)
        {
          if (!paramXmlPullParser.isEmptyElementTag()) {
            createTransitionFromXml(paramXmlPullParser, paramAttributeSet, (Transition)localObject1);
          }
          if (localTransitionSet != null)
          {
            localTransitionSet.addTransition((Transition)localObject1);
            localObject2 = null;
          }
          else if (paramTransition == null)
          {
            localObject2 = localObject1;
          }
          else
          {
            throw new InflateException("Could not add transition to another transition.");
          }
        }
        localObject1 = localObject2;
      }
    }
    paramAttributeSet = new StringBuilder();
    paramAttributeSet.append("Unknown scene name: ");
    paramAttributeSet.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramAttributeSet.toString());
    label618:
    return localObject1;
  }
  
  private TransitionManager createTransitionManagerFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, ViewGroup paramViewGroup)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    TransitionManager localTransitionManager = null;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        break label145;
      }
      if (j == 2)
      {
        String str = paramXmlPullParser.getName();
        if (str.equals("transitionManager"))
        {
          localTransitionManager = new TransitionManager();
        }
        else
        {
          if ((!str.equals("transition")) || (localTransitionManager == null)) {
            break;
          }
          loadTransition(paramAttributeSet, paramViewGroup, localTransitionManager);
        }
      }
    }
    paramAttributeSet = new StringBuilder();
    paramAttributeSet.append("Unknown scene name: ");
    paramAttributeSet.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramAttributeSet.toString());
    label145:
    return localTransitionManager;
  }
  
  public static TransitionInflater from(Context paramContext)
  {
    return new TransitionInflater(paramContext);
  }
  
  private void getTargetIds(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    TypedArray localTypedArray;
    Object localObject1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        return;
      }
      if (j == 2)
      {
        if (!paramXmlPullParser.getName().equals("target")) {
          break label291;
        }
        localTypedArray = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TransitionTarget);
        j = localTypedArray.getResourceId(1, 0);
        if (j != 0)
        {
          paramTransition.addTarget(j);
        }
        else
        {
          j = localTypedArray.getResourceId(2, 0);
          if (j != 0)
          {
            paramTransition.excludeTarget(j, true);
          }
          else
          {
            localObject1 = localTypedArray.getString(4);
            if (localObject1 != null)
            {
              paramTransition.addTarget((String)localObject1);
            }
            else
            {
              localObject1 = localTypedArray.getString(5);
              if (localObject1 != null)
              {
                paramTransition.excludeTarget((String)localObject1, true);
              }
              else
              {
                Object localObject2 = localTypedArray.getString(3);
                if (localObject2 != null)
                {
                  localObject1 = localObject2;
                  try
                  {
                    paramTransition.excludeTarget(Class.forName((String)localObject2), true);
                  }
                  catch (ClassNotFoundException paramXmlPullParser)
                  {
                    break;
                  }
                }
                else
                {
                  localObject1 = localObject2;
                  String str = localTypedArray.getString(0);
                  localObject2 = str;
                  if (str != null)
                  {
                    localObject1 = localObject2;
                    paramTransition.addTarget(Class.forName((String)localObject2));
                  }
                }
              }
            }
          }
        }
        localTypedArray.recycle();
      }
    }
    localTypedArray.recycle();
    paramAttributeSet = new StringBuilder();
    paramAttributeSet.append("Could not create ");
    paramAttributeSet.append((String)localObject1);
    throw new RuntimeException(paramAttributeSet.toString(), paramXmlPullParser);
    label291:
    paramAttributeSet = new StringBuilder();
    paramAttributeSet.append("Unknown scene name: ");
    paramAttributeSet.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramAttributeSet.toString());
  }
  
  private void loadTransition(AttributeSet paramAttributeSet, ViewGroup paramViewGroup, TransitionManager paramTransitionManager)
    throws Resources.NotFoundException
  {
    TypedArray localTypedArray = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TransitionManager);
    int i = localTypedArray.getResourceId(2, -1);
    int j = localTypedArray.getResourceId(0, -1);
    Transition localTransition = null;
    if (j < 0) {
      paramAttributeSet = null;
    } else {
      paramAttributeSet = Scene.getSceneForLayout(paramViewGroup, j, mContext);
    }
    j = localTypedArray.getResourceId(1, -1);
    if (j < 0) {
      paramViewGroup = localTransition;
    } else {
      paramViewGroup = Scene.getSceneForLayout(paramViewGroup, j, mContext);
    }
    if (i >= 0)
    {
      localTransition = inflateTransition(i);
      if (localTransition != null) {
        if (paramViewGroup != null)
        {
          if (paramAttributeSet == null) {
            paramTransitionManager.setTransition(paramViewGroup, localTransition);
          } else {
            paramTransitionManager.setTransition(paramAttributeSet, paramViewGroup, localTransition);
          }
        }
        else
        {
          paramAttributeSet = new StringBuilder();
          paramAttributeSet.append("No toScene for transition ID ");
          paramAttributeSet.append(i);
          throw new RuntimeException(paramAttributeSet.toString());
        }
      }
    }
    localTypedArray.recycle();
  }
  
  /* Error */
  public Transition inflateTransition(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 34	android/transition/TransitionInflater:mContext	Landroid/content/Context;
    //   4: invokevirtual 341	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   7: iload_1
    //   8: invokevirtual 347	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   11: astore_2
    //   12: aload_0
    //   13: aload_2
    //   14: aload_2
    //   15: invokestatic 353	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   18: aconst_null
    //   19: invokespecial 233	android/transition/TransitionInflater:createTransitionFromXml	(Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/transition/Transition;)Landroid/transition/Transition;
    //   22: astore_3
    //   23: aload_2
    //   24: invokeinterface 358 1 0
    //   29: aload_3
    //   30: areturn
    //   31: astore_3
    //   32: goto +95 -> 127
    //   35: astore_3
    //   36: new 104	android/view/InflateException
    //   39: astore 4
    //   41: new 90	java/lang/StringBuilder
    //   44: astore 5
    //   46: aload 5
    //   48: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   51: aload 5
    //   53: aload_2
    //   54: invokeinterface 361 1 0
    //   59: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload 5
    //   65: ldc_w 363
    //   68: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload 5
    //   74: aload_3
    //   75: invokevirtual 366	java/io/IOException:getMessage	()Ljava/lang/String;
    //   78: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: aload 4
    //   84: aload 5
    //   86: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   89: invokespecial 116	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   92: aload 4
    //   94: aload_3
    //   95: invokevirtual 370	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   98: pop
    //   99: aload 4
    //   101: athrow
    //   102: astore_3
    //   103: new 104	android/view/InflateException
    //   106: astore 5
    //   108: aload 5
    //   110: aload_3
    //   111: invokevirtual 371	org/xmlpull/v1/XmlPullParserException:getMessage	()Ljava/lang/String;
    //   114: invokespecial 116	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   117: aload 5
    //   119: aload_3
    //   120: invokevirtual 370	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   123: pop
    //   124: aload 5
    //   126: athrow
    //   127: aload_2
    //   128: invokeinterface 358 1 0
    //   133: aload_3
    //   134: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	135	0	this	TransitionInflater
    //   0	135	1	paramInt	int
    //   11	117	2	localXmlResourceParser	android.content.res.XmlResourceParser
    //   22	8	3	localTransition	Transition
    //   31	1	3	localObject1	Object
    //   35	60	3	localIOException	IOException
    //   102	32	3	localXmlPullParserException	XmlPullParserException
    //   39	61	4	localInflateException	InflateException
    //   44	81	5	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   12	23	31	finally
    //   36	102	31	finally
    //   103	127	31	finally
    //   12	23	35	java/io/IOException
    //   12	23	102	org/xmlpull/v1/XmlPullParserException
  }
  
  /* Error */
  public TransitionManager inflateTransitionManager(int paramInt, ViewGroup paramViewGroup)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 34	android/transition/TransitionInflater:mContext	Landroid/content/Context;
    //   4: invokevirtual 341	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   7: iload_1
    //   8: invokevirtual 347	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   11: astore_3
    //   12: aload_0
    //   13: aload_3
    //   14: aload_3
    //   15: invokestatic 353	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   18: aload_2
    //   19: invokespecial 375	android/transition/TransitionInflater:createTransitionManagerFromXml	(Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/view/ViewGroup;)Landroid/transition/TransitionManager;
    //   22: astore_2
    //   23: aload_3
    //   24: invokeinterface 358 1 0
    //   29: aload_2
    //   30: areturn
    //   31: astore_2
    //   32: goto +91 -> 123
    //   35: astore 4
    //   37: new 104	android/view/InflateException
    //   40: astore 5
    //   42: new 90	java/lang/StringBuilder
    //   45: astore_2
    //   46: aload_2
    //   47: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   50: aload_2
    //   51: aload_3
    //   52: invokeinterface 361 1 0
    //   57: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload_2
    //   62: ldc_w 363
    //   65: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload_2
    //   70: aload 4
    //   72: invokevirtual 366	java/io/IOException:getMessage	()Ljava/lang/String;
    //   75: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: aload 5
    //   81: aload_2
    //   82: invokevirtual 108	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   85: invokespecial 116	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   88: aload 5
    //   90: aload 4
    //   92: invokevirtual 370	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   95: pop
    //   96: aload 5
    //   98: athrow
    //   99: astore 4
    //   101: new 104	android/view/InflateException
    //   104: astore_2
    //   105: aload_2
    //   106: aload 4
    //   108: invokevirtual 371	org/xmlpull/v1/XmlPullParserException:getMessage	()Ljava/lang/String;
    //   111: invokespecial 116	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   114: aload_2
    //   115: aload 4
    //   117: invokevirtual 370	android/view/InflateException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   120: pop
    //   121: aload_2
    //   122: athrow
    //   123: aload_3
    //   124: invokeinterface 358 1 0
    //   129: aload_2
    //   130: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	131	0	this	TransitionInflater
    //   0	131	1	paramInt	int
    //   0	131	2	paramViewGroup	ViewGroup
    //   11	113	3	localXmlResourceParser	android.content.res.XmlResourceParser
    //   35	56	4	localIOException	IOException
    //   99	17	4	localXmlPullParserException	XmlPullParserException
    //   40	57	5	localInflateException	InflateException
    // Exception table:
    //   from	to	target	type
    //   12	23	31	finally
    //   37	99	31	finally
    //   101	123	31	finally
    //   12	23	35	java/io/IOException
    //   12	23	99	org/xmlpull/v1/XmlPullParserException
  }
}
