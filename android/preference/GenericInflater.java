package android.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

abstract class GenericInflater<T, P extends Parent>
{
  private static final Class[] mConstructorSignature = { Context.class, AttributeSet.class };
  private static final HashMap sConstructorMap = new HashMap();
  private final boolean DEBUG = false;
  private final Object[] mConstructorArgs = new Object[2];
  protected final Context mContext;
  private String mDefaultPackage;
  private Factory<T> mFactory;
  private boolean mFactorySet;
  
  protected GenericInflater(Context paramContext)
  {
    mContext = paramContext;
  }
  
  protected GenericInflater(GenericInflater<T, P> paramGenericInflater, Context paramContext)
  {
    mContext = paramContext;
    mFactory = mFactory;
  }
  
  private final T createItemFromTag(XmlPullParser paramXmlPullParser, String paramString, AttributeSet paramAttributeSet)
  {
    try
    {
      if (mFactory == null) {
        localObject = null;
      } else {
        localObject = mFactory.onCreateItem(paramString, mContext, paramAttributeSet);
      }
      paramXmlPullParser = (XmlPullParser)localObject;
      if (localObject == null) {
        if (-1 == paramString.indexOf('.')) {
          paramXmlPullParser = onCreateItem(paramString, paramAttributeSet);
        } else {
          paramXmlPullParser = createItem(paramString, null, paramAttributeSet);
        }
      }
      return paramXmlPullParser;
    }
    catch (Exception paramXmlPullParser)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramAttributeSet.getPositionDescription());
      ((StringBuilder)localObject).append(": Error inflating class ");
      ((StringBuilder)localObject).append(paramString);
      paramString = new InflateException(((StringBuilder)localObject).toString());
      paramString.initCause(paramXmlPullParser);
      throw paramString;
    }
    catch (ClassNotFoundException paramXmlPullParser)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramAttributeSet.getPositionDescription());
      ((StringBuilder)localObject).append(": Error inflating class ");
      ((StringBuilder)localObject).append(paramString);
      paramString = new InflateException(((StringBuilder)localObject).toString());
      paramString.initCause(paramXmlPullParser);
      throw paramString;
    }
    catch (InflateException paramXmlPullParser)
    {
      throw paramXmlPullParser;
    }
  }
  
  private void rInflate(XmlPullParser paramXmlPullParser, T paramT, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (((j == 3) && (paramXmlPullParser.getDepth() <= i)) || (j == 1)) {
        break;
      }
      if ((j == 2) && (!onCreateCustomFromTag(paramXmlPullParser, paramT, paramAttributeSet)))
      {
        Object localObject = createItemFromTag(paramXmlPullParser, paramXmlPullParser.getName(), paramAttributeSet);
        ((Parent)paramT).addItemFromInflater(localObject);
        rInflate(paramXmlPullParser, localObject, paramAttributeSet);
      }
    }
  }
  
  public abstract GenericInflater cloneInContext(Context paramContext);
  
  public final T createItem(String paramString1, String paramString2, AttributeSet paramAttributeSet)
    throws ClassNotFoundException, InflateException
  {
    Object localObject1 = (Constructor)sConstructorMap.get(paramString1);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      Object localObject3 = localObject1;
      try
      {
        ClassLoader localClassLoader = mContext.getClassLoader();
        if (paramString2 != null)
        {
          localObject3 = localObject1;
          localObject2 = new java/lang/StringBuilder;
          localObject3 = localObject1;
          ((StringBuilder)localObject2).<init>();
          localObject3 = localObject1;
          ((StringBuilder)localObject2).append(paramString2);
          localObject3 = localObject1;
          ((StringBuilder)localObject2).append(paramString1);
          localObject3 = localObject1;
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        else
        {
          localObject2 = paramString1;
        }
        localObject3 = localObject1;
        localObject2 = localClassLoader.loadClass((String)localObject2).getConstructor(mConstructorSignature);
        localObject3 = localObject2;
        ((Constructor)localObject2).setAccessible(true);
        localObject3 = localObject2;
        sConstructorMap.put(paramString1, localObject2);
      }
      catch (Exception paramString1)
      {
        break label185;
      }
      catch (ClassNotFoundException paramString1)
      {
        break label244;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        break label246;
      }
    }
    Object localObject4 = localObject2;
    localObject1 = mConstructorArgs;
    localObject1[1] = paramAttributeSet;
    localObject4 = localObject2;
    localObject2 = ((Constructor)localObject2).newInstance((Object[])localObject1);
    return localObject2;
    label185:
    paramString2 = new StringBuilder();
    paramString2.append(paramAttributeSet.getPositionDescription());
    paramString2.append(": Error inflating class ");
    paramString2.append(localObject4.getClass().getName());
    paramString2 = new InflateException(paramString2.toString());
    paramString2.initCause(paramString1);
    throw paramString2;
    label244:
    throw paramString1;
    label246:
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append(paramAttributeSet.getPositionDescription());
    ((StringBuilder)localObject2).append(": Error inflating class ");
    if (paramString2 != null)
    {
      paramAttributeSet = new StringBuilder();
      paramAttributeSet.append(paramString2);
      paramAttributeSet.append(paramString1);
      paramString1 = paramAttributeSet.toString();
    }
    ((StringBuilder)localObject2).append(paramString1);
    paramString1 = new InflateException(((StringBuilder)localObject2).toString());
    paramString1.initCause(localObject4);
    throw paramString1;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public String getDefaultPackage()
  {
    return mDefaultPackage;
  }
  
  public final Factory<T> getFactory()
  {
    return mFactory;
  }
  
  public T inflate(int paramInt, P paramP)
  {
    boolean bool;
    if (paramP != null) {
      bool = true;
    } else {
      bool = false;
    }
    return inflate(paramInt, paramP, bool);
  }
  
  public T inflate(int paramInt, P paramP, boolean paramBoolean)
  {
    XmlResourceParser localXmlResourceParser = getContext().getResources().getXml(paramInt);
    try
    {
      paramP = inflate(localXmlResourceParser, paramP, paramBoolean);
      return paramP;
    }
    finally
    {
      localXmlResourceParser.close();
    }
  }
  
  public T inflate(XmlPullParser paramXmlPullParser, P paramP)
  {
    boolean bool;
    if (paramP != null) {
      bool = true;
    } else {
      bool = false;
    }
    return inflate(paramXmlPullParser, paramP, bool);
  }
  
  public T inflate(XmlPullParser paramXmlPullParser, P paramP, boolean paramBoolean)
  {
    synchronized (mConstructorArgs)
    {
      Object localObject = Xml.asAttributeSet(paramXmlPullParser);
      mConstructorArgs[0] = mContext;
      try
      {
        int i;
        do
        {
          i = paramXmlPullParser.next();
        } while ((i != 2) && (i != 1));
        if (i == 2)
        {
          paramP = onMergeRoots(paramP, paramBoolean, (Parent)createItemFromTag(paramXmlPullParser, paramXmlPullParser.getName(), (AttributeSet)localObject));
          rInflate(paramXmlPullParser, paramP, (AttributeSet)localObject);
          return paramP;
        }
        paramP = new android/view/InflateException;
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append(paramXmlPullParser.getPositionDescription());
        ((StringBuilder)localObject).append(": No start tag found!");
        paramP.<init>(((StringBuilder)localObject).toString());
        throw paramP;
      }
      catch (IOException localIOException)
      {
        InflateException localInflateException = new android/view/InflateException;
        paramP = new java/lang/StringBuilder;
        paramP.<init>();
        paramP.append(paramXmlPullParser.getPositionDescription());
        paramP.append(": ");
        paramP.append(localIOException.getMessage());
        localInflateException.<init>(paramP.toString());
        localInflateException.initCause(localIOException);
        throw localInflateException;
      }
      catch (XmlPullParserException paramP)
      {
        paramXmlPullParser = new android/view/InflateException;
        paramXmlPullParser.<init>(paramP.getMessage());
        paramXmlPullParser.initCause(paramP);
        throw paramXmlPullParser;
      }
      catch (InflateException paramXmlPullParser)
      {
        throw paramXmlPullParser;
      }
    }
  }
  
  protected boolean onCreateCustomFromTag(XmlPullParser paramXmlPullParser, T paramT, AttributeSet paramAttributeSet)
    throws XmlPullParserException
  {
    return false;
  }
  
  protected T onCreateItem(String paramString, AttributeSet paramAttributeSet)
    throws ClassNotFoundException
  {
    return createItem(paramString, mDefaultPackage, paramAttributeSet);
  }
  
  protected P onMergeRoots(P paramP1, boolean paramBoolean, P paramP2)
  {
    return paramP2;
  }
  
  public void setDefaultPackage(String paramString)
  {
    mDefaultPackage = paramString;
  }
  
  public void setFactory(Factory<T> paramFactory)
  {
    if (!mFactorySet)
    {
      if (paramFactory != null)
      {
        mFactorySet = true;
        if (mFactory == null) {
          mFactory = paramFactory;
        } else {
          mFactory = new FactoryMerger(paramFactory, mFactory);
        }
        return;
      }
      throw new NullPointerException("Given factory can not be null");
    }
    throw new IllegalStateException("A factory has already been set on this inflater");
  }
  
  public static abstract interface Factory<T>
  {
    public abstract T onCreateItem(String paramString, Context paramContext, AttributeSet paramAttributeSet);
  }
  
  private static class FactoryMerger<T>
    implements GenericInflater.Factory<T>
  {
    private final GenericInflater.Factory<T> mF1;
    private final GenericInflater.Factory<T> mF2;
    
    FactoryMerger(GenericInflater.Factory<T> paramFactory1, GenericInflater.Factory<T> paramFactory2)
    {
      mF1 = paramFactory1;
      mF2 = paramFactory2;
    }
    
    public T onCreateItem(String paramString, Context paramContext, AttributeSet paramAttributeSet)
    {
      Object localObject = mF1.onCreateItem(paramString, paramContext, paramAttributeSet);
      if (localObject != null) {
        return localObject;
      }
      return mF2.onCreateItem(paramString, paramContext, paramAttributeSet);
    }
  }
  
  public static abstract interface Parent<T>
  {
    public abstract void addItemFromInflater(T paramT);
  }
}
