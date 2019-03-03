package android.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.PathParser;
import android.util.PathParser.PathData;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import android.view.animation.AnimationUtils;
import android.view.animation.BaseInterpolator;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflater
{
  private static final boolean DBG_ANIMATOR_INFLATER = false;
  private static final int SEQUENTIALLY = 1;
  private static final String TAG = "AnimatorInflater";
  private static final int TOGETHER = 0;
  private static final int VALUE_TYPE_COLOR = 3;
  private static final int VALUE_TYPE_FLOAT = 0;
  private static final int VALUE_TYPE_INT = 1;
  private static final int VALUE_TYPE_PATH = 2;
  private static final int VALUE_TYPE_UNDEFINED = 4;
  private static final TypedValue sTmpTypedValue = new TypedValue();
  
  public AnimatorInflater() {}
  
  private static Animator createAnimatorFromXml(Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, float paramFloat)
    throws XmlPullParserException, IOException
  {
    return createAnimatorFromXml(paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser), null, 0, paramFloat);
  }
  
  private static Animator createAnimatorFromXml(Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, AnimatorSet paramAnimatorSet, int paramInt, float paramFloat)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    int i = paramXmlPullParser.getDepth();
    Object localObject2 = null;
    for (;;)
    {
      Object localObject3 = paramAttributeSet;
      int j = paramXmlPullParser.next();
      if ((j == 3) && (paramXmlPullParser.getDepth() <= i)) {}
      while (j == 1) {
        break;
      }
      if (j == 2)
      {
        Object localObject4 = paramXmlPullParser.getName();
        j = 0;
        if (((String)localObject4).equals("objectAnimator")) {
          localObject1 = loadObjectAnimator(paramResources, paramTheme, (AttributeSet)localObject3, paramFloat);
        }
        for (;;)
        {
          break;
          if (((String)localObject4).equals("animator"))
          {
            localObject1 = loadAnimator(paramResources, paramTheme, (AttributeSet)localObject3, null, paramFloat);
          }
          else if (((String)localObject4).equals("set"))
          {
            localObject4 = new AnimatorSet();
            if (paramTheme != null) {
              localObject1 = paramTheme.obtainStyledAttributes((AttributeSet)localObject3, R.styleable.AnimatorSet, 0, 0);
            } else {
              localObject1 = paramResources.obtainAttributes((AttributeSet)localObject3, R.styleable.AnimatorSet);
            }
            ((Animator)localObject4).appendChangingConfigurations(((TypedArray)localObject1).getChangingConfigurations());
            int k = ((TypedArray)localObject1).getInt(0, 0);
            createAnimatorFromXml(paramResources, paramTheme, paramXmlPullParser, (AttributeSet)localObject3, (AnimatorSet)localObject4, k, paramFloat);
            ((TypedArray)localObject1).recycle();
            localObject1 = localObject4;
          }
          else
          {
            if (!((String)localObject4).equals("propertyValuesHolder")) {
              break label326;
            }
            localObject3 = loadValues(paramResources, paramTheme, paramXmlPullParser, Xml.asAttributeSet(paramXmlPullParser));
            if ((localObject3 != null) && (localObject1 != null) && ((localObject1 instanceof ValueAnimator))) {
              ((ValueAnimator)localObject1).setValues((PropertyValuesHolder[])localObject3);
            }
            j = 1;
          }
        }
        localObject3 = localObject2;
        if (paramAnimatorSet != null)
        {
          localObject3 = localObject2;
          if (j == 0)
          {
            localObject3 = localObject2;
            if (localObject2 == null) {
              localObject3 = new ArrayList();
            }
            ((ArrayList)localObject3).add(localObject1);
          }
        }
        localObject2 = localObject3;
      }
    }
    label326:
    paramResources = new StringBuilder();
    paramResources.append("Unknown animator name: ");
    paramResources.append(paramXmlPullParser.getName());
    throw new RuntimeException(paramResources.toString());
    if ((paramAnimatorSet != null) && (localObject2 != null))
    {
      paramResources = new Animator[localObject2.size()];
      i = 0;
      paramTheme = localObject2.iterator();
      while (paramTheme.hasNext())
      {
        paramResources[i] = ((Animator)paramTheme.next());
        i++;
      }
      if (paramInt == 0) {
        paramAnimatorSet.playTogether(paramResources);
      } else {
        paramAnimatorSet.playSequentially(paramResources);
      }
    }
    return localObject1;
  }
  
  private static Keyframe createNewKeyframe(Keyframe paramKeyframe, float paramFloat)
  {
    if (paramKeyframe.getType() == Float.TYPE) {
      paramKeyframe = Keyframe.ofFloat(paramFloat);
    } else if (paramKeyframe.getType() == Integer.TYPE) {
      paramKeyframe = Keyframe.ofInt(paramFloat);
    } else {
      paramKeyframe = Keyframe.ofObject(paramFloat);
    }
    return paramKeyframe;
  }
  
  private static StateListAnimator createStateListAnimatorFromXml(Context paramContext, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws IOException, XmlPullParserException
  {
    StateListAnimator localStateListAnimator = new StateListAnimator();
    for (;;)
    {
      switch (paramXmlPullParser.next())
      {
      default: 
        break;
      case 2: 
        if ("item".equals(paramXmlPullParser.getName()))
        {
          int i = paramXmlPullParser.getAttributeCount();
          int[] arrayOfInt = new int[i];
          int j = 0;
          Animator localAnimator1 = null;
          for (int k = 0; k < i; k++)
          {
            int m = paramAttributeSet.getAttributeNameResource(k);
            if (m == 16843213)
            {
              localAnimator1 = loadAnimator(paramContext, paramAttributeSet.getAttributeResourceValue(k, 0));
            }
            else
            {
              if (!paramAttributeSet.getAttributeBooleanValue(k, false)) {
                m = -m;
              }
              arrayOfInt[j] = m;
              j++;
            }
          }
          Animator localAnimator2 = localAnimator1;
          if (localAnimator1 == null) {
            localAnimator2 = createAnimatorFromXml(paramContext.getResources(), paramContext.getTheme(), paramXmlPullParser, 1.0F);
          }
          if (localAnimator2 != null) {
            localStateListAnimator.addState(StateSet.trimStateSet(arrayOfInt, j), localAnimator2);
          } else {
            throw new Resources.NotFoundException("animation state item must have a valid animation");
          }
        }
        break;
      case 1: 
      case 3: 
        return localStateListAnimator;
      }
    }
  }
  
  private static void distributeKeyframes(Keyframe[] paramArrayOfKeyframe, float paramFloat, int paramInt1, int paramInt2)
  {
    paramFloat /= (paramInt2 - paramInt1 + 2);
    while (paramInt1 <= paramInt2)
    {
      paramArrayOfKeyframe[paramInt1].setFraction(paramArrayOfKeyframe[(paramInt1 - 1)].getFraction() + paramFloat);
      paramInt1++;
    }
  }
  
  private static void dumpKeyframes(Object[] paramArrayOfObject, String paramString)
  {
    if ((paramArrayOfObject != null) && (paramArrayOfObject.length != 0))
    {
      Log.d("AnimatorInflater", paramString);
      int i = paramArrayOfObject.length;
      for (int j = 0; j < i; j++)
      {
        Keyframe localKeyframe = (Keyframe)paramArrayOfObject[j];
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Keyframe ");
        localStringBuilder.append(j);
        localStringBuilder.append(": fraction ");
        if (localKeyframe.getFraction() < 0.0F) {
          paramString = "null";
        } else {
          paramString = Float.valueOf(localKeyframe.getFraction());
        }
        localStringBuilder.append(paramString);
        localStringBuilder.append(", , value : ");
        if (localKeyframe.hasValue()) {
          paramString = localKeyframe.getValue();
        } else {
          paramString = "null";
        }
        localStringBuilder.append(paramString);
        Log.d("AnimatorInflater", localStringBuilder.toString());
      }
      return;
    }
  }
  
  private static int getChangingConfigs(Resources paramResources, int paramInt)
  {
    synchronized (sTmpTypedValue)
    {
      paramResources.getValue(paramInt, sTmpTypedValue, true);
      paramInt = sTmpTypedValuechangingConfigurations;
      return paramInt;
    }
  }
  
  private static PropertyValuesHolder getPVH(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    Object localObject = paramTypedArray.peekValue(paramInt2);
    int i;
    if (localObject != null) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if (i != 0) {
      j = type;
    } else {
      j = 0;
    }
    localObject = paramTypedArray.peekValue(paramInt3);
    int k;
    if (localObject != null) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if (k != 0) {
      m = type;
    } else {
      m = 0;
    }
    if (paramInt1 == 4)
    {
      if (((i != 0) && (isColorType(j))) || ((k != 0) && (isColorType(m)))) {}
      for (paramInt1 = 3;; paramInt1 = 0) {
        break;
      }
    }
    int n;
    if (paramInt1 == 0) {
      n = 1;
    } else {
      n = 0;
    }
    if (paramInt1 == 2)
    {
      String str1 = paramTypedArray.getString(paramInt2);
      String str2 = paramTypedArray.getString(paramInt3);
      if (str1 == null) {
        paramTypedArray = null;
      } else {
        paramTypedArray = new PathParser.PathData(str1);
      }
      if (str2 == null) {
        localObject = null;
      } else {
        localObject = new PathParser.PathData(str2);
      }
      if ((paramTypedArray == null) && (localObject == null)) {
        break label360;
      }
      if (paramTypedArray != null)
      {
        PathDataEvaluator localPathDataEvaluator = new PathDataEvaluator(null);
        if (localObject != null)
        {
          if (PathParser.canMorph(paramTypedArray, (PathParser.PathData)localObject))
          {
            paramTypedArray = PropertyValuesHolder.ofObject(paramString, localPathDataEvaluator, new Object[] { paramTypedArray, localObject });
          }
          else
          {
            paramTypedArray = new StringBuilder();
            paramTypedArray.append(" Can't morph from ");
            paramTypedArray.append(str1);
            paramTypedArray.append(" to ");
            paramTypedArray.append(str2);
            throw new InflateException(paramTypedArray.toString());
          }
        }
        else {
          paramTypedArray = PropertyValuesHolder.ofObject(paramString, localPathDataEvaluator, new Object[] { paramTypedArray });
        }
      }
      else if (localObject != null)
      {
        paramTypedArray = PropertyValuesHolder.ofObject(paramString, new PathDataEvaluator(null), new Object[] { localObject });
      }
      else
      {
        label360:
        paramTypedArray = null;
      }
      paramString = paramTypedArray;
    }
    else
    {
      localObject = null;
      if (paramInt1 == 3) {
        localObject = ArgbEvaluator.getInstance();
      }
      if (n != 0)
      {
        float f1;
        if (i != 0)
        {
          if (j == 5) {
            f1 = paramTypedArray.getDimension(paramInt2, 0.0F);
          } else {
            f1 = paramTypedArray.getFloat(paramInt2, 0.0F);
          }
          if (k != 0)
          {
            float f2;
            if (m == 5) {
              f2 = paramTypedArray.getDimension(paramInt3, 0.0F);
            } else {
              f2 = paramTypedArray.getFloat(paramInt3, 0.0F);
            }
            paramTypedArray = PropertyValuesHolder.ofFloat(paramString, new float[] { f1, f2 });
          }
          else
          {
            paramTypedArray = PropertyValuesHolder.ofFloat(paramString, new float[] { f1 });
          }
        }
        else
        {
          if (m == 5) {
            f1 = paramTypedArray.getDimension(paramInt3, 0.0F);
          } else {
            f1 = paramTypedArray.getFloat(paramInt3, 0.0F);
          }
          paramTypedArray = PropertyValuesHolder.ofFloat(paramString, new float[] { f1 });
        }
      }
      else if (i != 0)
      {
        if (j == 5) {
          paramInt1 = (int)paramTypedArray.getDimension(paramInt2, 0.0F);
        } else if (isColorType(j)) {
          paramInt1 = paramTypedArray.getColor(paramInt2, 0);
        } else {
          paramInt1 = paramTypedArray.getInt(paramInt2, 0);
        }
        if (k != 0)
        {
          if (m == 5) {
            paramInt2 = (int)paramTypedArray.getDimension(paramInt3, 0.0F);
          } else if (isColorType(m)) {
            paramInt2 = paramTypedArray.getColor(paramInt3, 0);
          } else {
            paramInt2 = paramTypedArray.getInt(paramInt3, 0);
          }
          paramTypedArray = PropertyValuesHolder.ofInt(paramString, new int[] { paramInt1, paramInt2 });
        }
        else
        {
          paramTypedArray = PropertyValuesHolder.ofInt(paramString, new int[] { paramInt1 });
        }
      }
      else if (k != 0)
      {
        if (m == 5) {
          paramInt1 = (int)paramTypedArray.getDimension(paramInt3, 0.0F);
        } else if (isColorType(m)) {
          paramInt1 = paramTypedArray.getColor(paramInt3, 0);
        } else {
          paramInt1 = paramTypedArray.getInt(paramInt3, 0);
        }
        paramTypedArray = PropertyValuesHolder.ofInt(paramString, new int[] { paramInt1 });
      }
      else
      {
        paramTypedArray = null;
      }
      paramString = paramTypedArray;
      if (paramTypedArray != null)
      {
        paramString = paramTypedArray;
        if (localObject != null)
        {
          paramTypedArray.setEvaluator((TypeEvaluator)localObject);
          paramString = paramTypedArray;
        }
      }
    }
    return paramString;
  }
  
  private static int inferValueTypeFromValues(TypedArray paramTypedArray, int paramInt1, int paramInt2)
  {
    TypedValue localTypedValue = paramTypedArray.peekValue(paramInt1);
    int i = 1;
    int j = 0;
    if (localTypedValue != null) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    int k;
    if (paramInt1 != 0) {
      k = type;
    } else {
      k = 0;
    }
    paramTypedArray = paramTypedArray.peekValue(paramInt2);
    if (paramTypedArray != null) {
      paramInt2 = i;
    } else {
      paramInt2 = 0;
    }
    if (paramInt2 != 0) {
      i = type;
    } else {
      i = 0;
    }
    if (((paramInt1 != 0) && (isColorType(k))) || ((paramInt2 != 0) && (isColorType(i)))) {
      paramInt1 = 3;
    } else {
      paramInt1 = j;
    }
    return paramInt1;
  }
  
  private static int inferValueTypeOfKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet)
  {
    int i = 0;
    if (paramTheme != null) {
      paramResources = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.Keyframe, 0, 0);
    } else {
      paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.Keyframe);
    }
    paramTheme = paramResources.peekValue(0);
    int j;
    if (paramTheme != null) {
      j = 1;
    } else {
      j = 0;
    }
    if ((j != 0) && (isColorType(type))) {
      j = 3;
    } else {
      j = i;
    }
    paramResources.recycle();
    return j;
  }
  
  private static boolean isColorType(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 28) && (paramInt <= 31)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static Animator loadAnimator(Context paramContext, int paramInt)
    throws Resources.NotFoundException
  {
    return loadAnimator(paramContext.getResources(), paramContext.getTheme(), paramInt);
  }
  
  public static Animator loadAnimator(Resources paramResources, Resources.Theme paramTheme, int paramInt)
    throws Resources.NotFoundException
  {
    return loadAnimator(paramResources, paramTheme, paramInt, 1.0F);
  }
  
  /* Error */
  public static Animator loadAnimator(Resources paramResources, Resources.Theme paramTheme, int paramInt, float paramFloat)
    throws Resources.NotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 402	android/content/res/Resources:getAnimatorCache	()Landroid/content/res/ConfigurationBoundResourceCache;
    //   4: astore 4
    //   6: aload 4
    //   8: iload_2
    //   9: i2l
    //   10: aload_0
    //   11: aload_1
    //   12: invokevirtual 407	android/content/res/ConfigurationBoundResourceCache:getInstance	(JLandroid/content/res/Resources;Landroid/content/res/Resources$Theme;)Ljava/lang/Object;
    //   15: checkcast 118	android/animation/Animator
    //   18: astore 5
    //   20: aload 5
    //   22: ifnull +6 -> 28
    //   25: aload 5
    //   27: areturn
    //   28: aconst_null
    //   29: astore 6
    //   31: aconst_null
    //   32: astore 7
    //   34: aconst_null
    //   35: astore 5
    //   37: aload_0
    //   38: iload_2
    //   39: invokevirtual 411	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   42: astore 8
    //   44: aload 8
    //   46: astore 5
    //   48: aload 8
    //   50: astore 6
    //   52: aload 8
    //   54: astore 7
    //   56: aload_0
    //   57: aload_1
    //   58: aload 8
    //   60: fload_3
    //   61: invokestatic 253	android/animation/AnimatorInflater:createAnimatorFromXml	(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;Lorg/xmlpull/v1/XmlPullParser;F)Landroid/animation/Animator;
    //   64: astore 9
    //   66: aload 9
    //   68: astore 5
    //   70: aload 9
    //   72: ifnull +101 -> 173
    //   75: aload 8
    //   77: astore 5
    //   79: aload 8
    //   81: astore 6
    //   83: aload 8
    //   85: astore 7
    //   87: aload 9
    //   89: aload_0
    //   90: iload_2
    //   91: invokestatic 413	android/animation/AnimatorInflater:getChangingConfigs	(Landroid/content/res/Resources;I)I
    //   94: invokevirtual 122	android/animation/Animator:appendChangingConfigurations	(I)V
    //   97: aload 8
    //   99: astore 5
    //   101: aload 8
    //   103: astore 6
    //   105: aload 8
    //   107: astore 7
    //   109: aload 9
    //   111: invokevirtual 417	android/animation/Animator:createConstantState	()Landroid/content/res/ConstantState;
    //   114: astore 10
    //   116: aload 9
    //   118: astore 5
    //   120: aload 10
    //   122: ifnull +51 -> 173
    //   125: aload 8
    //   127: astore 5
    //   129: aload 8
    //   131: astore 6
    //   133: aload 8
    //   135: astore 7
    //   137: aload 4
    //   139: iload_2
    //   140: i2l
    //   141: aload_1
    //   142: aload 10
    //   144: invokevirtual 421	android/content/res/ConfigurationBoundResourceCache:put	(JLandroid/content/res/Resources$Theme;Ljava/lang/Object;)V
    //   147: aload 8
    //   149: astore 5
    //   151: aload 8
    //   153: astore 6
    //   155: aload 8
    //   157: astore 7
    //   159: aload 10
    //   161: aload_0
    //   162: aload_1
    //   163: invokevirtual 427	android/content/res/ConstantState:newInstance	(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;)Ljava/lang/Object;
    //   166: checkcast 118	android/animation/Animator
    //   169: astore_0
    //   170: aload_0
    //   171: astore 5
    //   173: aload 8
    //   175: ifnull +10 -> 185
    //   178: aload 8
    //   180: invokeinterface 432 1 0
    //   185: aload 5
    //   187: areturn
    //   188: astore_0
    //   189: goto +168 -> 357
    //   192: astore_1
    //   193: aload 6
    //   195: astore 5
    //   197: new 265	android/content/res/Resources$NotFoundException
    //   200: astore_0
    //   201: aload 6
    //   203: astore 5
    //   205: new 149	java/lang/StringBuilder
    //   208: astore 7
    //   210: aload 6
    //   212: astore 5
    //   214: aload 7
    //   216: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   219: aload 6
    //   221: astore 5
    //   223: aload 7
    //   225: ldc_w 434
    //   228: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload 6
    //   234: astore 5
    //   236: aload 7
    //   238: iload_2
    //   239: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   242: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload 6
    //   248: astore 5
    //   250: aload_0
    //   251: aload 7
    //   253: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   256: invokespecial 268	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   259: aload 6
    //   261: astore 5
    //   263: aload_0
    //   264: aload_1
    //   265: invokevirtual 441	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   268: pop
    //   269: aload 6
    //   271: astore 5
    //   273: aload_0
    //   274: athrow
    //   275: astore_1
    //   276: aload 7
    //   278: astore 5
    //   280: new 265	android/content/res/Resources$NotFoundException
    //   283: astore 6
    //   285: aload 7
    //   287: astore 5
    //   289: new 149	java/lang/StringBuilder
    //   292: astore_0
    //   293: aload 7
    //   295: astore 5
    //   297: aload_0
    //   298: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   301: aload 7
    //   303: astore 5
    //   305: aload_0
    //   306: ldc_w 434
    //   309: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   312: pop
    //   313: aload 7
    //   315: astore 5
    //   317: aload_0
    //   318: iload_2
    //   319: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   322: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   325: pop
    //   326: aload 7
    //   328: astore 5
    //   330: aload 6
    //   332: aload_0
    //   333: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   336: invokespecial 268	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   339: aload 7
    //   341: astore 5
    //   343: aload 6
    //   345: aload_1
    //   346: invokevirtual 441	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   349: pop
    //   350: aload 7
    //   352: astore 5
    //   354: aload 6
    //   356: athrow
    //   357: aload 5
    //   359: ifnull +10 -> 369
    //   362: aload 5
    //   364: invokeinterface 432 1 0
    //   369: aload_0
    //   370: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	371	0	paramResources	Resources
    //   0	371	1	paramTheme	Resources.Theme
    //   0	371	2	paramInt	int
    //   0	371	3	paramFloat	float
    //   4	134	4	localConfigurationBoundResourceCache	android.content.res.ConfigurationBoundResourceCache
    //   18	345	5	localObject1	Object
    //   29	326	6	localObject2	Object
    //   32	319	7	localObject3	Object
    //   42	137	8	localXmlResourceParser	android.content.res.XmlResourceParser
    //   64	53	9	localAnimator	Animator
    //   114	46	10	localConstantState	android.content.res.ConstantState
    // Exception table:
    //   from	to	target	type
    //   37	44	188	finally
    //   56	66	188	finally
    //   87	97	188	finally
    //   109	116	188	finally
    //   137	147	188	finally
    //   159	170	188	finally
    //   197	201	188	finally
    //   205	210	188	finally
    //   214	219	188	finally
    //   223	232	188	finally
    //   236	246	188	finally
    //   250	259	188	finally
    //   263	269	188	finally
    //   273	275	188	finally
    //   280	285	188	finally
    //   289	293	188	finally
    //   297	301	188	finally
    //   305	313	188	finally
    //   317	326	188	finally
    //   330	339	188	finally
    //   343	350	188	finally
    //   354	357	188	finally
    //   37	44	192	java/io/IOException
    //   56	66	192	java/io/IOException
    //   87	97	192	java/io/IOException
    //   109	116	192	java/io/IOException
    //   137	147	192	java/io/IOException
    //   159	170	192	java/io/IOException
    //   37	44	275	org/xmlpull/v1/XmlPullParserException
    //   56	66	275	org/xmlpull/v1/XmlPullParserException
    //   87	97	275	org/xmlpull/v1/XmlPullParserException
    //   109	116	275	org/xmlpull/v1/XmlPullParserException
    //   137	147	275	org/xmlpull/v1/XmlPullParserException
    //   159	170	275	org/xmlpull/v1/XmlPullParserException
  }
  
  private static ValueAnimator loadAnimator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, ValueAnimator paramValueAnimator, float paramFloat)
    throws Resources.NotFoundException
  {
    AttributeSet localAttributeSet = null;
    TypedArray localTypedArray;
    if (paramTheme != null) {
      localTypedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.Animator, 0, 0);
    } else {
      localTypedArray = paramResources.obtainAttributes(paramAttributeSet, R.styleable.Animator);
    }
    if (paramValueAnimator != null)
    {
      if (paramTheme != null) {
        paramAttributeSet = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.PropertyAnimator, 0, 0);
      } else {
        paramAttributeSet = paramResources.obtainAttributes(paramAttributeSet, R.styleable.PropertyAnimator);
      }
      paramValueAnimator.appendChangingConfigurations(paramAttributeSet.getChangingConfigurations());
      localAttributeSet = paramAttributeSet;
    }
    paramAttributeSet = paramValueAnimator;
    if (paramValueAnimator == null) {
      paramAttributeSet = new ValueAnimator();
    }
    paramAttributeSet.appendChangingConfigurations(localTypedArray.getChangingConfigurations());
    parseAnimatorFromTypeArray(paramAttributeSet, localTypedArray, localAttributeSet, paramFloat);
    int i = localTypedArray.getResourceId(0, 0);
    if (i > 0)
    {
      paramResources = AnimationUtils.loadInterpolator(paramResources, paramTheme, i);
      if ((paramResources instanceof BaseInterpolator)) {
        paramAttributeSet.appendChangingConfigurations(((BaseInterpolator)paramResources).getChangingConfiguration());
      }
      paramAttributeSet.setInterpolator(paramResources);
    }
    localTypedArray.recycle();
    if (localAttributeSet != null) {
      localAttributeSet.recycle();
    }
    return paramAttributeSet;
  }
  
  private static Keyframe loadKeyframe(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int paramInt)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray;
    if (paramTheme != null) {
      localTypedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.Keyframe, 0, 0);
    } else {
      localTypedArray = paramResources.obtainAttributes(paramAttributeSet, R.styleable.Keyframe);
    }
    paramAttributeSet = null;
    float f = localTypedArray.getFloat(3, -1.0F);
    TypedValue localTypedValue = localTypedArray.peekValue(0);
    int i;
    if (localTypedValue != null) {
      i = 1;
    } else {
      i = 0;
    }
    int j = paramInt;
    if (paramInt == 4) {
      if ((i != 0) && (isColorType(type))) {
        j = 3;
      } else {
        j = 0;
      }
    }
    if (i != 0)
    {
      if (j != 3) {
        switch (j)
        {
        default: 
          break;
        case 0: 
          paramAttributeSet = Keyframe.ofFloat(f, localTypedArray.getFloat(0, 0.0F));
          break;
        }
      } else {
        paramAttributeSet = Keyframe.ofInt(f, localTypedArray.getInt(0, 0));
      }
    }
    else if (j == 0) {
      paramAttributeSet = Keyframe.ofFloat(f);
    } else {
      paramAttributeSet = Keyframe.ofInt(f);
    }
    paramInt = localTypedArray.getResourceId(1, 0);
    if (paramInt > 0) {
      paramAttributeSet.setInterpolator(AnimationUtils.loadInterpolator(paramResources, paramTheme, paramInt));
    }
    localTypedArray.recycle();
    return paramAttributeSet;
  }
  
  private static ObjectAnimator loadObjectAnimator(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, float paramFloat)
    throws Resources.NotFoundException
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    loadAnimator(paramResources, paramTheme, paramAttributeSet, localObjectAnimator, paramFloat);
    return localObjectAnimator;
  }
  
  private static PropertyValuesHolder loadPvh(Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, String paramString, int paramInt)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    int i;
    for (;;)
    {
      i = paramXmlPullParser.next();
      if ((i == 3) || (i == 1)) {
        break;
      }
      Object localObject3 = localObject2;
      i = paramInt;
      if (paramXmlPullParser.getName().equals("keyframe"))
      {
        i = paramInt;
        if (paramInt == 4) {
          i = inferValueTypeOfKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser));
        }
        Keyframe localKeyframe = loadKeyframe(paramResources, paramTheme, Xml.asAttributeSet(paramXmlPullParser), i);
        localObject3 = localObject2;
        if (localKeyframe != null)
        {
          localObject3 = localObject2;
          if (localObject2 == null) {
            localObject3 = new ArrayList();
          }
          ((ArrayList)localObject3).add(localKeyframe);
        }
        paramXmlPullParser.next();
      }
      localObject2 = localObject3;
      paramInt = i;
    }
    if (localObject2 != null)
    {
      i = localObject2.size();
      int j = i;
      if (i > 0)
      {
        int k = 0;
        paramTheme = (Keyframe)localObject2.get(0);
        paramResources = (Keyframe)localObject2.get(j - 1);
        float f1 = paramResources.getFraction();
        float f2 = 0.0F;
        i = j;
        if (f1 < 1.0F) {
          if (f1 < 0.0F)
          {
            paramResources.setFraction(1.0F);
            i = j;
          }
          else
          {
            localObject2.add(localObject2.size(), createNewKeyframe(paramResources, 1.0F));
            i = j + 1;
          }
        }
        f1 = paramTheme.getFraction();
        int m = i;
        if (f1 != 0.0F) {
          if (f1 < 0.0F)
          {
            paramTheme.setFraction(0.0F);
            m = i;
          }
          else
          {
            localObject2.add(0, createNewKeyframe(paramTheme, 0.0F));
            m = i + 1;
          }
        }
        paramResources = new Keyframe[m];
        localObject2.toArray(paramResources);
        for (i = k; i < m; i++)
        {
          paramTheme = paramResources[i];
          if (paramTheme.getFraction() < f2) {
            if (i == 0)
            {
              paramTheme.setFraction(f2);
            }
            else if (i == m - 1)
            {
              paramTheme.setFraction(1.0F);
              f2 = 0.0F;
            }
            else
            {
              j = i + 1;
              k = i;
              while ((j < m - 1) && (paramResources[j].getFraction() < 0.0F))
              {
                k = j;
                j++;
              }
              f2 = 0.0F;
              distributeKeyframes(paramResources, paramResources[(k + 1)].getFraction() - paramResources[(i - 1)].getFraction(), i, k);
            }
          }
        }
        paramTheme = PropertyValuesHolder.ofKeyframe(paramString, paramResources);
        paramResources = paramTheme;
        if (paramInt != 3) {
          return paramResources;
        }
        paramTheme.setEvaluator(ArgbEvaluator.getInstance());
        return paramTheme;
      }
    }
    paramResources = localObject1;
    return paramResources;
  }
  
  /* Error */
  public static StateListAnimator loadStateListAnimator(Context paramContext, int paramInt)
    throws Resources.NotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 247	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   4: astore_2
    //   5: aload_2
    //   6: invokevirtual 516	android/content/res/Resources:getStateListAnimatorCache	()Landroid/content/res/ConfigurationBoundResourceCache;
    //   9: astore_3
    //   10: aload_0
    //   11: invokevirtual 251	android/content/Context:getTheme	()Landroid/content/res/Resources$Theme;
    //   14: astore 4
    //   16: aload_3
    //   17: iload_1
    //   18: i2l
    //   19: aload_2
    //   20: aload 4
    //   22: invokevirtual 407	android/content/res/ConfigurationBoundResourceCache:getInstance	(JLandroid/content/res/Resources;Landroid/content/res/Resources$Theme;)Ljava/lang/Object;
    //   25: checkcast 218	android/animation/StateListAnimator
    //   28: astore 5
    //   30: aload 5
    //   32: ifnull +6 -> 38
    //   35: aload 5
    //   37: areturn
    //   38: aconst_null
    //   39: astore 6
    //   41: aconst_null
    //   42: astore 7
    //   44: aconst_null
    //   45: astore 5
    //   47: aload_2
    //   48: iload_1
    //   49: invokevirtual 411	android/content/res/Resources:getAnimation	(I)Landroid/content/res/XmlResourceParser;
    //   52: astore 8
    //   54: aload 8
    //   56: astore 5
    //   58: aload 8
    //   60: astore 6
    //   62: aload 8
    //   64: astore 7
    //   66: aload_0
    //   67: aload 8
    //   69: aload 8
    //   71: invokestatic 53	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   74: invokestatic 518	android/animation/AnimatorInflater:createStateListAnimatorFromXml	(Landroid/content/Context;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;)Landroid/animation/StateListAnimator;
    //   77: astore 9
    //   79: aload 9
    //   81: astore_0
    //   82: aload 9
    //   84: ifnull +98 -> 182
    //   87: aload 8
    //   89: astore 5
    //   91: aload 8
    //   93: astore 6
    //   95: aload 8
    //   97: astore 7
    //   99: aload 9
    //   101: aload_2
    //   102: iload_1
    //   103: invokestatic 413	android/animation/AnimatorInflater:getChangingConfigs	(Landroid/content/res/Resources;I)I
    //   106: invokevirtual 519	android/animation/StateListAnimator:appendChangingConfigurations	(I)V
    //   109: aload 8
    //   111: astore 5
    //   113: aload 8
    //   115: astore 6
    //   117: aload 8
    //   119: astore 7
    //   121: aload 9
    //   123: invokevirtual 520	android/animation/StateListAnimator:createConstantState	()Landroid/content/res/ConstantState;
    //   126: astore 10
    //   128: aload 9
    //   130: astore_0
    //   131: aload 10
    //   133: ifnull +49 -> 182
    //   136: aload 8
    //   138: astore 5
    //   140: aload 8
    //   142: astore 6
    //   144: aload 8
    //   146: astore 7
    //   148: aload_3
    //   149: iload_1
    //   150: i2l
    //   151: aload 4
    //   153: aload 10
    //   155: invokevirtual 421	android/content/res/ConfigurationBoundResourceCache:put	(JLandroid/content/res/Resources$Theme;Ljava/lang/Object;)V
    //   158: aload 8
    //   160: astore 5
    //   162: aload 8
    //   164: astore 6
    //   166: aload 8
    //   168: astore 7
    //   170: aload 10
    //   172: aload_2
    //   173: aload 4
    //   175: invokevirtual 427	android/content/res/ConstantState:newInstance	(Landroid/content/res/Resources;Landroid/content/res/Resources$Theme;)Ljava/lang/Object;
    //   178: checkcast 218	android/animation/StateListAnimator
    //   181: astore_0
    //   182: aload 8
    //   184: ifnull +10 -> 194
    //   187: aload 8
    //   189: invokeinterface 432 1 0
    //   194: aload_0
    //   195: areturn
    //   196: astore_0
    //   197: goto +174 -> 371
    //   200: astore 7
    //   202: aload 6
    //   204: astore 5
    //   206: new 265	android/content/res/Resources$NotFoundException
    //   209: astore 8
    //   211: aload 6
    //   213: astore 5
    //   215: new 149	java/lang/StringBuilder
    //   218: astore_0
    //   219: aload 6
    //   221: astore 5
    //   223: aload_0
    //   224: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   227: aload 6
    //   229: astore 5
    //   231: aload_0
    //   232: ldc_w 522
    //   235: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: pop
    //   239: aload 6
    //   241: astore 5
    //   243: aload_0
    //   244: iload_1
    //   245: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   248: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload 6
    //   254: astore 5
    //   256: aload 8
    //   258: aload_0
    //   259: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   262: invokespecial 268	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   265: aload 6
    //   267: astore 5
    //   269: aload 8
    //   271: aload 7
    //   273: invokevirtual 441	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   276: pop
    //   277: aload 6
    //   279: astore 5
    //   281: aload 8
    //   283: athrow
    //   284: astore_0
    //   285: aload 7
    //   287: astore 5
    //   289: new 265	android/content/res/Resources$NotFoundException
    //   292: astore 8
    //   294: aload 7
    //   296: astore 5
    //   298: new 149	java/lang/StringBuilder
    //   301: astore 6
    //   303: aload 7
    //   305: astore 5
    //   307: aload 6
    //   309: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   312: aload 7
    //   314: astore 5
    //   316: aload 6
    //   318: ldc_w 522
    //   321: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   324: pop
    //   325: aload 7
    //   327: astore 5
    //   329: aload 6
    //   331: iload_1
    //   332: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   335: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   338: pop
    //   339: aload 7
    //   341: astore 5
    //   343: aload 8
    //   345: aload 6
    //   347: invokevirtual 161	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   350: invokespecial 268	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   353: aload 7
    //   355: astore 5
    //   357: aload 8
    //   359: aload_0
    //   360: invokevirtual 441	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   363: pop
    //   364: aload 7
    //   366: astore 5
    //   368: aload 8
    //   370: athrow
    //   371: aload 5
    //   373: ifnull +10 -> 383
    //   376: aload 5
    //   378: invokeinterface 432 1 0
    //   383: aload_0
    //   384: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	385	0	paramContext	Context
    //   0	385	1	paramInt	int
    //   4	169	2	localResources	Resources
    //   9	140	3	localConfigurationBoundResourceCache	android.content.res.ConfigurationBoundResourceCache
    //   14	160	4	localTheme	Resources.Theme
    //   28	349	5	localObject1	Object
    //   39	307	6	localObject2	Object
    //   42	127	7	localObject3	Object
    //   200	165	7	localIOException	IOException
    //   52	317	8	localObject4	Object
    //   77	52	9	localStateListAnimator	StateListAnimator
    //   126	45	10	localConstantState	android.content.res.ConstantState
    // Exception table:
    //   from	to	target	type
    //   47	54	196	finally
    //   66	79	196	finally
    //   99	109	196	finally
    //   121	128	196	finally
    //   148	158	196	finally
    //   170	182	196	finally
    //   206	211	196	finally
    //   215	219	196	finally
    //   223	227	196	finally
    //   231	239	196	finally
    //   243	252	196	finally
    //   256	265	196	finally
    //   269	277	196	finally
    //   281	284	196	finally
    //   289	294	196	finally
    //   298	303	196	finally
    //   307	312	196	finally
    //   316	325	196	finally
    //   329	339	196	finally
    //   343	353	196	finally
    //   357	364	196	finally
    //   368	371	196	finally
    //   47	54	200	java/io/IOException
    //   66	79	200	java/io/IOException
    //   99	109	200	java/io/IOException
    //   121	128	200	java/io/IOException
    //   148	158	200	java/io/IOException
    //   170	182	200	java/io/IOException
    //   47	54	284	org/xmlpull/v1/XmlPullParserException
    //   66	79	284	org/xmlpull/v1/XmlPullParserException
    //   99	109	284	org/xmlpull/v1/XmlPullParserException
    //   121	128	284	org/xmlpull/v1/XmlPullParserException
    //   148	158	284	org/xmlpull/v1/XmlPullParserException
    //   170	182	284	org/xmlpull/v1/XmlPullParserException
  }
  
  private static PropertyValuesHolder[] loadValues(Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    int i;
    int j;
    for (;;)
    {
      i = paramXmlPullParser.getEventType();
      j = 0;
      if ((i == 3) || (i == 1)) {
        break;
      }
      if (i != 2)
      {
        paramXmlPullParser.next();
      }
      else
      {
        Object localObject2 = localObject1;
        if (paramXmlPullParser.getName().equals("propertyValuesHolder"))
        {
          TypedArray localTypedArray;
          if (paramTheme != null) {
            localTypedArray = paramTheme.obtainStyledAttributes(paramAttributeSet, R.styleable.PropertyValuesHolder, 0, 0);
          } else {
            localTypedArray = paramResources.obtainAttributes(paramAttributeSet, R.styleable.PropertyValuesHolder);
          }
          String str = localTypedArray.getString(3);
          j = localTypedArray.getInt(2, 4);
          localObject2 = loadPvh(paramResources, paramTheme, paramXmlPullParser, str, j);
          Object localObject3 = localObject2;
          if (localObject2 == null) {
            localObject3 = getPVH(localTypedArray, j, 0, 1, str);
          }
          localObject2 = localObject1;
          if (localObject3 != null)
          {
            localObject2 = localObject1;
            if (localObject1 == null) {
              localObject2 = new ArrayList();
            }
            ((ArrayList)localObject2).add(localObject3);
          }
          localTypedArray.recycle();
        }
        paramXmlPullParser.next();
        localObject1 = localObject2;
      }
    }
    paramResources = null;
    if (localObject1 != null)
    {
      i = localObject1.size();
      paramTheme = new PropertyValuesHolder[i];
      for (;;)
      {
        paramResources = paramTheme;
        if (j >= i) {
          break;
        }
        paramTheme[j] = ((PropertyValuesHolder)localObject1.get(j));
        j++;
      }
    }
    return paramResources;
  }
  
  private static void parseAnimatorFromTypeArray(ValueAnimator paramValueAnimator, TypedArray paramTypedArray1, TypedArray paramTypedArray2, float paramFloat)
  {
    long l1 = paramTypedArray1.getInt(1, 300);
    long l2 = paramTypedArray1.getInt(2, 0);
    int i = paramTypedArray1.getInt(7, 4);
    int j = i;
    if (i == 4) {
      j = inferValueTypeFromValues(paramTypedArray1, 5, 6);
    }
    PropertyValuesHolder localPropertyValuesHolder = getPVH(paramTypedArray1, j, 5, 6, "");
    if (localPropertyValuesHolder != null) {
      paramValueAnimator.setValues(new PropertyValuesHolder[] { localPropertyValuesHolder });
    }
    paramValueAnimator.setDuration(l1);
    paramValueAnimator.setStartDelay(l2);
    if (paramTypedArray1.hasValue(3)) {
      paramValueAnimator.setRepeatCount(paramTypedArray1.getInt(3, 0));
    }
    if (paramTypedArray1.hasValue(4)) {
      paramValueAnimator.setRepeatMode(paramTypedArray1.getInt(4, 1));
    }
    if (paramTypedArray2 != null) {
      setupObjectAnimator(paramValueAnimator, paramTypedArray2, j, paramFloat);
    }
  }
  
  private static TypeEvaluator setupAnimatorForPath(ValueAnimator paramValueAnimator, TypedArray paramTypedArray)
  {
    Object localObject = null;
    String str1 = paramTypedArray.getString(5);
    String str2 = paramTypedArray.getString(6);
    PathParser.PathData localPathData1;
    if (str1 == null) {
      localPathData1 = null;
    } else {
      localPathData1 = new PathParser.PathData(str1);
    }
    PathParser.PathData localPathData2;
    if (str2 == null) {
      localPathData2 = null;
    } else {
      localPathData2 = new PathParser.PathData(str2);
    }
    if (localPathData1 != null)
    {
      if (localPathData2 != null)
      {
        paramValueAnimator.setObjectValues(new Object[] { localPathData1, localPathData2 });
        if (!PathParser.canMorph(localPathData1, localPathData2))
        {
          paramValueAnimator = new StringBuilder();
          paramValueAnimator.append(paramTypedArray.getPositionDescription());
          paramValueAnimator.append(" Can't morph from ");
          paramValueAnimator.append(str1);
          paramValueAnimator.append(" to ");
          paramValueAnimator.append(str2);
          throw new InflateException(paramValueAnimator.toString());
        }
      }
      else
      {
        paramValueAnimator.setObjectValues(new Object[] { localPathData1 });
      }
      paramTypedArray = new PathDataEvaluator(null);
    }
    else
    {
      paramTypedArray = localObject;
      if (localPathData2 != null)
      {
        paramValueAnimator.setObjectValues(new Object[] { localPathData2 });
        paramTypedArray = new PathDataEvaluator(null);
      }
    }
    return paramTypedArray;
  }
  
  private static void setupObjectAnimator(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, int paramInt, float paramFloat)
  {
    ObjectAnimator localObjectAnimator = (ObjectAnimator)paramValueAnimator;
    paramValueAnimator = paramTypedArray.getString(1);
    if (paramValueAnimator != null)
    {
      String str1 = paramTypedArray.getString(2);
      String str2 = paramTypedArray.getString(3);
      int i;
      if (paramInt != 2)
      {
        i = paramInt;
        if (paramInt != 4) {}
      }
      else
      {
        i = 0;
      }
      if ((str1 == null) && (str2 == null))
      {
        paramValueAnimator = new StringBuilder();
        paramValueAnimator.append(paramTypedArray.getPositionDescription());
        paramValueAnimator.append(" propertyXName or propertyYName is needed for PathData");
        throw new InflateException(paramValueAnimator.toString());
      }
      paramValueAnimator = KeyframeSet.ofPath(PathParser.createPathFromPathData(paramValueAnimator), 0.5F * paramFloat);
      if (i == 0)
      {
        paramTypedArray = paramValueAnimator.createXFloatKeyframes();
        paramValueAnimator = paramValueAnimator.createYFloatKeyframes();
      }
      else
      {
        paramTypedArray = paramValueAnimator.createXIntKeyframes();
        paramValueAnimator = paramValueAnimator.createYIntKeyframes();
      }
      PropertyValuesHolder localPropertyValuesHolder = null;
      Object localObject = null;
      if (str1 != null) {
        localPropertyValuesHolder = PropertyValuesHolder.ofKeyframes(str1, paramTypedArray);
      }
      paramTypedArray = localObject;
      if (str2 != null) {
        paramTypedArray = PropertyValuesHolder.ofKeyframes(str2, paramValueAnimator);
      }
      if (localPropertyValuesHolder == null) {
        localObjectAnimator.setValues(new PropertyValuesHolder[] { paramTypedArray });
      } else if (paramTypedArray == null) {
        localObjectAnimator.setValues(new PropertyValuesHolder[] { localPropertyValuesHolder });
      } else {
        localObjectAnimator.setValues(new PropertyValuesHolder[] { localPropertyValuesHolder, paramTypedArray });
      }
    }
    else
    {
      localObjectAnimator.setPropertyName(paramTypedArray.getString(0));
    }
  }
  
  private static void setupValues(ValueAnimator paramValueAnimator, TypedArray paramTypedArray, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, boolean paramBoolean3, int paramInt2)
  {
    if (paramBoolean1)
    {
      float f1;
      if (paramBoolean2)
      {
        if (paramInt1 == 5) {
          f1 = paramTypedArray.getDimension(5, 0.0F);
        } else {
          f1 = paramTypedArray.getFloat(5, 0.0F);
        }
        if (paramBoolean3)
        {
          float f2;
          if (paramInt2 == 5) {
            f2 = paramTypedArray.getDimension(6, 0.0F);
          } else {
            f2 = paramTypedArray.getFloat(6, 0.0F);
          }
          paramValueAnimator.setFloatValues(new float[] { f1, f2 });
        }
        else
        {
          paramValueAnimator.setFloatValues(new float[] { f1 });
        }
      }
      else
      {
        if (paramInt2 == 5) {
          f1 = paramTypedArray.getDimension(6, 0.0F);
        } else {
          f1 = paramTypedArray.getFloat(6, 0.0F);
        }
        paramValueAnimator.setFloatValues(new float[] { f1 });
      }
    }
    else if (paramBoolean2)
    {
      if (paramInt1 == 5) {}
      for (paramInt1 = (int)paramTypedArray.getDimension(5, 0.0F);; paramInt1 = paramTypedArray.getColor(5, 0))
      {
        i = paramInt1;
        break label195;
        if (!isColorType(paramInt1)) {
          break;
        }
      }
      int i = paramTypedArray.getInt(5, 0);
      label195:
      if (paramBoolean3)
      {
        if (paramInt2 == 5) {
          paramInt1 = (int)paramTypedArray.getDimension(6, 0.0F);
        }
        for (;;)
        {
          break;
          if (isColorType(paramInt2)) {
            paramInt1 = paramTypedArray.getColor(6, 0);
          } else {
            paramInt1 = paramTypedArray.getInt(6, 0);
          }
        }
        paramValueAnimator.setIntValues(new int[] { i, paramInt1 });
      }
      else
      {
        paramValueAnimator.setIntValues(new int[] { i });
      }
    }
    else if (paramBoolean3)
    {
      if (paramInt2 == 5) {
        paramInt1 = (int)paramTypedArray.getDimension(6, 0.0F);
      }
      for (;;)
      {
        break;
        if (isColorType(paramInt2)) {
          paramInt1 = paramTypedArray.getColor(6, 0);
        } else {
          paramInt1 = paramTypedArray.getInt(6, 0);
        }
      }
      paramValueAnimator.setIntValues(new int[] { paramInt1 });
    }
  }
  
  private static class PathDataEvaluator
    implements TypeEvaluator<PathParser.PathData>
  {
    private final PathParser.PathData mPathData = new PathParser.PathData();
    
    private PathDataEvaluator() {}
    
    public PathParser.PathData evaluate(float paramFloat, PathParser.PathData paramPathData1, PathParser.PathData paramPathData2)
    {
      if (PathParser.interpolatePathData(mPathData, paramPathData1, paramPathData2, paramFloat)) {
        return mPathData;
      }
      throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
    }
  }
}
