package android.content.res;

import android.animation.Animator;
import android.animation.StateListAnimator;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableInflater;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pools.SynchronizedPool;
import android.util.TypedValue;
import android.view.DisplayAdjustments;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R.styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;

public class Resources
{
  private static final int MIN_THEME_REFS_FLUSH_SIZE = 32;
  static final String TAG = "Resources";
  static Resources mSystem = null;
  private static final Object sSync = new Object();
  final ClassLoader mClassLoader;
  private DrawableInflater mDrawableInflater;
  private ResourcesImpl mResourcesImpl;
  private final ArrayList<WeakReference<Theme>> mThemeRefs = new ArrayList();
  private int mThemeRefsNextFlushSize = 32;
  private TypedValue mTmpValue = new TypedValue();
  private final Object mTmpValueLock = new Object();
  final Pools.SynchronizedPool<TypedArray> mTypedArrayPool = new Pools.SynchronizedPool(5);
  
  private Resources()
  {
    this(null);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localDisplayMetrics.setToDefaults();
    Configuration localConfiguration = new Configuration();
    localConfiguration.setToDefaults();
    mResourcesImpl = new ResourcesImpl(AssetManager.getSystem(), localDisplayMetrics, localConfiguration, new DisplayAdjustments());
  }
  
  @Deprecated
  public Resources(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration)
  {
    this(null);
    mResourcesImpl = new ResourcesImpl(paramAssetManager, paramDisplayMetrics, paramConfiguration, new DisplayAdjustments());
  }
  
  public Resources(ClassLoader paramClassLoader)
  {
    if (paramClassLoader == null) {
      paramClassLoader = ClassLoader.getSystemClassLoader();
    }
    mClassLoader = paramClassLoader;
  }
  
  public static Resources getSystem()
  {
    synchronized (sSync)
    {
      Resources localResources1 = mSystem;
      Resources localResources2 = localResources1;
      if (localResources1 == null)
      {
        localResources2 = new android/content/res/Resources;
        localResources2.<init>();
        mSystem = localResources2;
      }
      return localResources2;
    }
  }
  
  public static TypedArray obtainAttributes(Resources paramResources, Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    if (paramTheme == null) {
      return paramResources.obtainAttributes(paramAttributeSet, paramArrayOfInt);
    }
    return paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, 0, 0);
  }
  
  private TypedValue obtainTempTypedValue()
  {
    TypedValue localTypedValue = null;
    synchronized (mTmpValueLock)
    {
      if (mTmpValue != null)
      {
        localTypedValue = mTmpValue;
        mTmpValue = null;
      }
      if (localTypedValue == null) {
        return new TypedValue();
      }
      return localTypedValue;
    }
  }
  
  private void releaseTempTypedValue(TypedValue paramTypedValue)
  {
    synchronized (mTmpValueLock)
    {
      if (mTmpValue == null) {
        mTmpValue = paramTypedValue;
      }
      return;
    }
  }
  
  public static boolean resourceHasPackage(int paramInt)
  {
    boolean bool;
    if (paramInt >>> 24 != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int selectDefaultTheme(int paramInt1, int paramInt2)
  {
    return selectSystemTheme(paramInt1, paramInt2, 16973829, 16973931, 16974120, 16974143);
  }
  
  public static int selectSystemTheme(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (paramInt1 != 0) {
      return paramInt1;
    }
    if (paramInt2 < 11) {
      return paramInt3;
    }
    if (paramInt2 < 14) {
      return paramInt4;
    }
    if (paramInt2 < 24) {
      return paramInt5;
    }
    return paramInt6;
  }
  
  public static void updateSystemConfiguration(Configuration paramConfiguration, DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo)
  {
    if (mSystem != null) {
      mSystem.updateConfiguration(paramConfiguration, paramDisplayMetrics, paramCompatibilityInfo);
    }
  }
  
  @VisibleForTesting
  public int calcConfigChanges(Configuration paramConfiguration)
  {
    return mResourcesImpl.calcConfigChanges(paramConfiguration);
  }
  
  public final void finishPreloading()
  {
    mResourcesImpl.finishPreloading();
  }
  
  public final void flushLayoutCache()
  {
    mResourcesImpl.flushLayoutCache();
  }
  
  public XmlResourceParser getAnimation(int paramInt)
    throws Resources.NotFoundException
  {
    return loadXmlResourceParser(paramInt, "anim");
  }
  
  public ConfigurationBoundResourceCache<Animator> getAnimatorCache()
  {
    return mResourcesImpl.getAnimatorCache();
  }
  
  public final AssetManager getAssets()
  {
    return mResourcesImpl.getAssets();
  }
  
  public boolean getBoolean(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject1 = mResourcesImpl;
      boolean bool = true;
      ((ResourcesImpl)localObject1).getValue(paramInt, localTypedValue, true);
      if ((type >= 16) && (type <= 31))
      {
        paramInt = data;
        if (paramInt == 0) {
          bool = false;
        }
        return bool;
      }
      localObject1 = new android/content/res/Resources$NotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      localStringBuilder.append(" type #0x");
      localStringBuilder.append(Integer.toHexString(type));
      localStringBuilder.append(" is not valid");
      ((NotFoundException)localObject1).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject1);
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public ClassLoader getClassLoader()
  {
    return mClassLoader;
  }
  
  @Deprecated
  public int getColor(int paramInt)
    throws Resources.NotFoundException
  {
    return getColor(paramInt, null);
  }
  
  public int getColor(int paramInt, Theme paramTheme)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject = mResourcesImpl;
      ((ResourcesImpl)localObject).getValue(paramInt, localTypedValue, true);
      if ((type >= 16) && (type <= 31))
      {
        paramInt = data;
        return paramInt;
      }
      if (type == 3)
      {
        paramInt = ((ResourcesImpl)localObject).loadColorStateList(this, localTypedValue, paramInt, paramTheme).getDefaultColor();
        return paramInt;
      }
      paramTheme = new android/content/res/Resources$NotFoundException;
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Resource ID #0x");
      ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
      ((StringBuilder)localObject).append(" type #0x");
      ((StringBuilder)localObject).append(Integer.toHexString(type));
      ((StringBuilder)localObject).append(" is not valid");
      paramTheme.<init>(((StringBuilder)localObject).toString());
      throw paramTheme;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  @Deprecated
  public ColorStateList getColorStateList(int paramInt)
    throws Resources.NotFoundException
  {
    ColorStateList localColorStateList = getColorStateList(paramInt, null);
    if ((localColorStateList != null) && (localColorStateList.canApplyTheme()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ColorStateList ");
      localStringBuilder.append(getResourceName(paramInt));
      localStringBuilder.append(" has unresolved theme attributes! Consider using Resources.getColorStateList(int, Theme) or Context.getColorStateList(int).");
      Log.w("Resources", localStringBuilder.toString(), new RuntimeException());
    }
    return localColorStateList;
  }
  
  public ColorStateList getColorStateList(int paramInt, Theme paramTheme)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      ResourcesImpl localResourcesImpl = mResourcesImpl;
      localResourcesImpl.getValue(paramInt, localTypedValue, true);
      paramTheme = localResourcesImpl.loadColorStateList(this, localTypedValue, paramInt, paramTheme);
      return paramTheme;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public CompatibilityInfo getCompatibilityInfo()
  {
    return mResourcesImpl.getCompatibilityInfo();
  }
  
  public Configuration getConfiguration()
  {
    return mResourcesImpl.getConfiguration();
  }
  
  public float getDimension(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject1 = mResourcesImpl;
      ((ResourcesImpl)localObject1).getValue(paramInt, localTypedValue, true);
      if (type == 5)
      {
        float f = TypedValue.complexToDimension(data, ((ResourcesImpl)localObject1).getDisplayMetrics());
        return f;
      }
      NotFoundException localNotFoundException = new android/content/res/Resources$NotFoundException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Resource ID #0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramInt));
      ((StringBuilder)localObject1).append(" type #0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(type));
      ((StringBuilder)localObject1).append(" is not valid");
      localNotFoundException.<init>(((StringBuilder)localObject1).toString());
      throw localNotFoundException;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public int getDimensionPixelOffset(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject1 = mResourcesImpl;
      ((ResourcesImpl)localObject1).getValue(paramInt, localTypedValue, true);
      if (type == 5)
      {
        paramInt = TypedValue.complexToDimensionPixelOffset(data, ((ResourcesImpl)localObject1).getDisplayMetrics());
        return paramInt;
      }
      NotFoundException localNotFoundException = new android/content/res/Resources$NotFoundException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Resource ID #0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramInt));
      ((StringBuilder)localObject1).append(" type #0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(type));
      ((StringBuilder)localObject1).append(" is not valid");
      localNotFoundException.<init>(((StringBuilder)localObject1).toString());
      throw localNotFoundException;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public int getDimensionPixelSize(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject1 = mResourcesImpl;
      ((ResourcesImpl)localObject1).getValue(paramInt, localTypedValue, true);
      if (type == 5)
      {
        paramInt = TypedValue.complexToDimensionPixelSize(data, ((ResourcesImpl)localObject1).getDisplayMetrics());
        return paramInt;
      }
      localObject1 = new android/content/res/Resources$NotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      localStringBuilder.append(" type #0x");
      localStringBuilder.append(Integer.toHexString(type));
      localStringBuilder.append(" is not valid");
      ((NotFoundException)localObject1).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject1);
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public DisplayAdjustments getDisplayAdjustments()
  {
    return mResourcesImpl.getDisplayAdjustments();
  }
  
  public DisplayMetrics getDisplayMetrics()
  {
    return mResourcesImpl.getDisplayMetrics();
  }
  
  @Deprecated
  public Drawable getDrawable(int paramInt)
    throws Resources.NotFoundException
  {
    Drawable localDrawable = getDrawable(paramInt, null);
    if ((localDrawable != null) && (localDrawable.canApplyTheme()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Drawable ");
      localStringBuilder.append(getResourceName(paramInt));
      localStringBuilder.append(" has unresolved theme attributes! Consider using Resources.getDrawable(int, Theme) or Context.getDrawable(int).");
      Log.w("Resources", localStringBuilder.toString(), new RuntimeException());
    }
    return localDrawable;
  }
  
  public Drawable getDrawable(int paramInt, Theme paramTheme)
    throws Resources.NotFoundException
  {
    return getDrawableForDensity(paramInt, 0, paramTheme);
  }
  
  @Deprecated
  public Drawable getDrawableForDensity(int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return getDrawableForDensity(paramInt1, paramInt2, null);
  }
  
  public Drawable getDrawableForDensity(int paramInt1, int paramInt2, Theme paramTheme)
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      ResourcesImpl localResourcesImpl = mResourcesImpl;
      localResourcesImpl.getValueForDensity(paramInt1, paramInt2, localTypedValue, true);
      paramTheme = localResourcesImpl.loadDrawable(this, localTypedValue, paramInt1, paramInt2, paramTheme);
      return paramTheme;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public final DrawableInflater getDrawableInflater()
  {
    if (mDrawableInflater == null) {
      mDrawableInflater = new DrawableInflater(this, mClassLoader);
    }
    return mDrawableInflater;
  }
  
  public float getFloat(int paramInt)
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      mResourcesImpl.getValue(paramInt, localTypedValue, true);
      if (type == 4)
      {
        float f = localTypedValue.getFloat();
        return f;
      }
      NotFoundException localNotFoundException = new android/content/res/Resources$NotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      localStringBuilder.append(" type #0x");
      localStringBuilder.append(Integer.toHexString(type));
      localStringBuilder.append(" is not valid");
      localNotFoundException.<init>(localStringBuilder.toString());
      throw localNotFoundException;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public Typeface getFont(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject1 = obtainTempTypedValue();
    try
    {
      Object localObject2 = mResourcesImpl;
      ((ResourcesImpl)localObject2).getValue(paramInt, (TypedValue)localObject1, true);
      localObject2 = ((ResourcesImpl)localObject2).loadFont(this, (TypedValue)localObject1, paramInt);
      if (localObject2 != null)
      {
        releaseTempTypedValue((TypedValue)localObject1);
        return localObject2;
      }
      releaseTempTypedValue((TypedValue)localObject1);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Font resource ID #0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramInt));
      throw new NotFoundException(((StringBuilder)localObject1).toString());
    }
    finally
    {
      releaseTempTypedValue((TypedValue)localObject1);
    }
  }
  
  Typeface getFont(TypedValue paramTypedValue, int paramInt)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.loadFont(this, paramTypedValue, paramInt);
  }
  
  public float getFraction(int paramInt1, int paramInt2, int paramInt3)
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      mResourcesImpl.getValue(paramInt1, localTypedValue, true);
      if (type == 6)
      {
        float f = TypedValue.complexToFraction(data, paramInt2, paramInt3);
        return f;
      }
      NotFoundException localNotFoundException = new android/content/res/Resources$NotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt1));
      localStringBuilder.append(" type #0x");
      localStringBuilder.append(Integer.toHexString(type));
      localStringBuilder.append(" is not valid");
      localNotFoundException.<init>(localStringBuilder.toString());
      throw localNotFoundException;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public int getIdentifier(String paramString1, String paramString2, String paramString3)
  {
    return mResourcesImpl.getIdentifier(paramString1, paramString2, paramString3);
  }
  
  public ResourcesImpl getImpl()
  {
    return mResourcesImpl;
  }
  
  public int[] getIntArray(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mResourcesImpl.getAssets().getResourceIntArray(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Int array resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new NotFoundException(((StringBuilder)localObject).toString());
  }
  
  public int getInteger(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      mResourcesImpl.getValue(paramInt, localTypedValue, true);
      if ((type >= 16) && (type <= 31))
      {
        paramInt = data;
        return paramInt;
      }
      NotFoundException localNotFoundException = new android/content/res/Resources$NotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      localStringBuilder.append(" type #0x");
      localStringBuilder.append(Integer.toHexString(type));
      localStringBuilder.append(" is not valid");
      localNotFoundException.<init>(localStringBuilder.toString());
      throw localNotFoundException;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public XmlResourceParser getLayout(int paramInt)
    throws Resources.NotFoundException
  {
    return loadXmlResourceParser(paramInt, "layout");
  }
  
  public Movie getMovie(int paramInt)
    throws Resources.NotFoundException
  {
    InputStream localInputStream = openRawResource(paramInt);
    Movie localMovie = Movie.decodeStream(localInputStream);
    try
    {
      localInputStream.close();
    }
    catch (IOException localIOException) {}
    return localMovie;
  }
  
  public LongSparseArray<Drawable.ConstantState> getPreloadedDrawables()
  {
    return mResourcesImpl.getPreloadedDrawables();
  }
  
  public String getQuantityString(int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return getQuantityText(paramInt1, paramInt2).toString();
  }
  
  public String getQuantityString(int paramInt1, int paramInt2, Object... paramVarArgs)
    throws Resources.NotFoundException
  {
    String str = getQuantityText(paramInt1, paramInt2).toString();
    return String.format(mResourcesImpl.getConfiguration().getLocales().get(0), str, paramVarArgs);
  }
  
  public CharSequence getQuantityText(int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.getQuantityText(paramInt1, paramInt2);
  }
  
  public String getResourceEntryName(int paramInt)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.getResourceEntryName(paramInt);
  }
  
  public String getResourceName(int paramInt)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.getResourceName(paramInt);
  }
  
  public String getResourcePackageName(int paramInt)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.getResourcePackageName(paramInt);
  }
  
  public String getResourceTypeName(int paramInt)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.getResourceTypeName(paramInt);
  }
  
  public Configuration[] getSizeConfigurations()
  {
    return mResourcesImpl.getSizeConfigurations();
  }
  
  public ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache()
  {
    return mResourcesImpl.getStateListAnimatorCache();
  }
  
  public String getString(int paramInt)
    throws Resources.NotFoundException
  {
    return getText(paramInt).toString();
  }
  
  public String getString(int paramInt, Object... paramVarArgs)
    throws Resources.NotFoundException
  {
    String str = getString(paramInt);
    return String.format(mResourcesImpl.getConfiguration().getLocales().get(0), str, paramVarArgs);
  }
  
  public String[] getStringArray(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mResourcesImpl.getAssets().getResourceStringArray(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("String array resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new NotFoundException(((StringBuilder)localObject).toString());
  }
  
  public CharSequence getText(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mResourcesImpl.getAssets().getResourceText(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("String resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new NotFoundException(((StringBuilder)localObject).toString());
  }
  
  public CharSequence getText(int paramInt, CharSequence paramCharSequence)
  {
    CharSequence localCharSequence;
    if (paramInt != 0) {
      localCharSequence = mResourcesImpl.getAssets().getResourceText(paramInt);
    } else {
      localCharSequence = null;
    }
    if (localCharSequence != null) {
      paramCharSequence = localCharSequence;
    }
    return paramCharSequence;
  }
  
  public CharSequence[] getTextArray(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mResourcesImpl.getAssets().getResourceTextArray(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Text array resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new NotFoundException(((StringBuilder)localObject).toString());
  }
  
  public void getValue(int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    mResourcesImpl.getValue(paramInt, paramTypedValue, paramBoolean);
  }
  
  public void getValue(String paramString, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    mResourcesImpl.getValue(paramString, paramTypedValue, paramBoolean);
  }
  
  public void getValueForDensity(int paramInt1, int paramInt2, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    mResourcesImpl.getValueForDensity(paramInt1, paramInt2, paramTypedValue, paramBoolean);
  }
  
  public XmlResourceParser getXml(int paramInt)
    throws Resources.NotFoundException
  {
    return loadXmlResourceParser(paramInt, "xml");
  }
  
  ColorStateList loadColorStateList(TypedValue paramTypedValue, int paramInt, Theme paramTheme)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.loadColorStateList(this, paramTypedValue, paramInt, paramTheme);
  }
  
  public ComplexColor loadComplexColor(TypedValue paramTypedValue, int paramInt, Theme paramTheme)
  {
    return mResourcesImpl.loadComplexColor(this, paramTypedValue, paramInt, paramTheme);
  }
  
  Drawable loadDrawable(TypedValue paramTypedValue, int paramInt1, int paramInt2, Theme paramTheme)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.loadDrawable(this, paramTypedValue, paramInt1, paramInt2, paramTheme);
  }
  
  XmlResourceParser loadXmlResourceParser(int paramInt, String paramString)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      Object localObject = mResourcesImpl;
      ((ResourcesImpl)localObject).getValue(paramInt, localTypedValue, true);
      if (type == 3)
      {
        paramString = ((ResourcesImpl)localObject).loadXmlResourceParser(string.toString(), paramInt, assetCookie, paramString);
        return paramString;
      }
      paramString = new android/content/res/Resources$NotFoundException;
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Resource ID #0x");
      ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
      ((StringBuilder)localObject).append(" type #0x");
      ((StringBuilder)localObject).append(Integer.toHexString(type));
      ((StringBuilder)localObject).append(" is not valid");
      paramString.<init>(((StringBuilder)localObject).toString());
      throw paramString;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  XmlResourceParser loadXmlResourceParser(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.loadXmlResourceParser(paramString1, paramInt1, paramInt2, paramString2);
  }
  
  public final Theme newTheme()
  {
    Theme localTheme = new Theme(null);
    localTheme.setImpl(mResourcesImpl.newThemeImpl());
    synchronized (mThemeRefs)
    {
      ArrayList localArrayList2 = mThemeRefs;
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(localTheme);
      localArrayList2.add(localWeakReference);
      if (mThemeRefs.size() > mThemeRefsNextFlushSize)
      {
        mThemeRefs.removeIf(_..Lambda.Resources.4msWUw7LKsgLexLZjIfWa4oguq4.INSTANCE);
        mThemeRefsNextFlushSize = Math.max(32, 2 * mThemeRefs.size());
      }
      return localTheme;
    }
  }
  
  public TypedArray obtainAttributes(AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    TypedArray localTypedArray = TypedArray.obtain(this, paramArrayOfInt.length);
    paramAttributeSet = (XmlBlock.Parser)paramAttributeSet;
    mResourcesImpl.getAssets().retrieveAttributes(paramAttributeSet, paramArrayOfInt, mData, mIndices);
    mXml = paramAttributeSet;
    return localTypedArray;
  }
  
  public TypedArray obtainTypedArray(int paramInt)
    throws Resources.NotFoundException
  {
    ResourcesImpl localResourcesImpl = mResourcesImpl;
    int i = localResourcesImpl.getAssets().getResourceArraySize(paramInt);
    if (i >= 0)
    {
      localObject = TypedArray.obtain(this, i);
      mLength = localResourcesImpl.getAssets().getResourceArray(paramInt, mData);
      mIndices[0] = 0;
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Array resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new NotFoundException(((StringBuilder)localObject).toString());
  }
  
  public InputStream openRawResource(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      InputStream localInputStream = openRawResource(paramInt, localTypedValue);
      return localInputStream;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public InputStream openRawResource(int paramInt, TypedValue paramTypedValue)
    throws Resources.NotFoundException
  {
    return mResourcesImpl.openRawResource(paramInt, paramTypedValue);
  }
  
  public AssetFileDescriptor openRawResourceFd(int paramInt)
    throws Resources.NotFoundException
  {
    TypedValue localTypedValue = obtainTempTypedValue();
    try
    {
      AssetFileDescriptor localAssetFileDescriptor = mResourcesImpl.openRawResourceFd(paramInt, localTypedValue);
      return localAssetFileDescriptor;
    }
    finally
    {
      releaseTempTypedValue(localTypedValue);
    }
  }
  
  public void parseBundleExtra(String paramString, AttributeSet paramAttributeSet, Bundle paramBundle)
    throws XmlPullParserException
  {
    TypedArray localTypedArray = obtainAttributes(paramAttributeSet, R.styleable.Extra);
    boolean bool = false;
    String str = localTypedArray.getString(0);
    if (str != null)
    {
      TypedValue localTypedValue = localTypedArray.peekValue(1);
      if (localTypedValue != null)
      {
        if (type == 3)
        {
          paramBundle.putCharSequence(str, localTypedValue.coerceToString());
        }
        else if (type == 18)
        {
          if (data != 0) {
            bool = true;
          }
          paramBundle.putBoolean(str, bool);
        }
        else if ((type >= 16) && (type <= 31))
        {
          paramBundle.putInt(str, data);
        }
        else
        {
          if (type != 4) {
            break label154;
          }
          paramBundle.putFloat(str, localTypedValue.getFloat());
        }
        localTypedArray.recycle();
        return;
        label154:
        localTypedArray.recycle();
        paramBundle = new StringBuilder();
        paramBundle.append("<");
        paramBundle.append(paramString);
        paramBundle.append("> only supports string, integer, float, color, and boolean at ");
        paramBundle.append(paramAttributeSet.getPositionDescription());
        throw new XmlPullParserException(paramBundle.toString());
      }
      localTypedArray.recycle();
      paramBundle = new StringBuilder();
      paramBundle.append("<");
      paramBundle.append(paramString);
      paramBundle.append("> requires an android:value or android:resource attribute at ");
      paramBundle.append(paramAttributeSet.getPositionDescription());
      throw new XmlPullParserException(paramBundle.toString());
    }
    localTypedArray.recycle();
    paramBundle = new StringBuilder();
    paramBundle.append("<");
    paramBundle.append(paramString);
    paramBundle.append("> requires an android:name attribute at ");
    paramBundle.append(paramAttributeSet.getPositionDescription());
    throw new XmlPullParserException(paramBundle.toString());
  }
  
  public void parseBundleExtras(XmlResourceParser paramXmlResourceParser, Bundle paramBundle)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlResourceParser.getDepth();
    for (;;)
    {
      int j = paramXmlResourceParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlResourceParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4)) {
        if (paramXmlResourceParser.getName().equals("extra"))
        {
          parseBundleExtra("extra", paramXmlResourceParser, paramBundle);
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
        else
        {
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
    }
  }
  
  public void preloadFonts(int paramInt)
  {
    TypedArray localTypedArray = obtainTypedArray(paramInt);
    try
    {
      int i = localTypedArray.length();
      for (paramInt = 0; paramInt < i; paramInt++) {
        localTypedArray.getFont(paramInt);
      }
      return;
    }
    finally
    {
      localTypedArray.recycle();
    }
  }
  
  @VisibleForTesting
  public void setCompatibilityInfo(CompatibilityInfo paramCompatibilityInfo)
  {
    if (paramCompatibilityInfo != null) {
      mResourcesImpl.updateConfiguration(null, null, paramCompatibilityInfo);
    }
  }
  
  public void setImpl(ResourcesImpl paramResourcesImpl)
  {
    if (paramResourcesImpl == mResourcesImpl) {
      return;
    }
    mResourcesImpl = paramResourcesImpl;
    synchronized (mThemeRefs)
    {
      int i = mThemeRefs.size();
      for (int j = 0; j < i; j++)
      {
        paramResourcesImpl = (WeakReference)mThemeRefs.get(j);
        if (paramResourcesImpl != null) {
          paramResourcesImpl = (Theme)paramResourcesImpl.get();
        } else {
          paramResourcesImpl = null;
        }
        if (paramResourcesImpl != null) {
          paramResourcesImpl.setImpl(mResourcesImpl.newThemeImpl(paramResourcesImpl.getKey()));
        }
      }
      return;
    }
  }
  
  public final void startPreloading()
  {
    mResourcesImpl.startPreloading();
  }
  
  @Deprecated
  public void updateConfiguration(Configuration paramConfiguration, DisplayMetrics paramDisplayMetrics)
  {
    updateConfiguration(paramConfiguration, paramDisplayMetrics, null);
  }
  
  public void updateConfiguration(Configuration paramConfiguration, DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo)
  {
    mResourcesImpl.updateConfiguration(paramConfiguration, paramDisplayMetrics, paramCompatibilityInfo);
  }
  
  public static class NotFoundException
    extends RuntimeException
  {
    public NotFoundException() {}
    
    public NotFoundException(String paramString)
    {
      super();
    }
    
    public NotFoundException(String paramString, Exception paramException)
    {
      super(paramException);
    }
  }
  
  public final class Theme
  {
    private ResourcesImpl.ThemeImpl mThemeImpl;
    
    private Theme() {}
    
    private String getResourceNameFromHexString(String paramString)
    {
      return getResourceName(Integer.parseInt(paramString, 16));
    }
    
    public void applyStyle(int paramInt, boolean paramBoolean)
    {
      mThemeImpl.applyStyle(paramInt, paramBoolean);
    }
    
    public void dump(int paramInt, String paramString1, String paramString2)
    {
      mThemeImpl.dump(paramInt, paramString1, paramString2);
    }
    
    public void encode(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      paramViewHierarchyEncoder.beginObject(this);
      String[] arrayOfString = getTheme();
      for (int i = 0; i < arrayOfString.length; i += 2) {
        paramViewHierarchyEncoder.addProperty(arrayOfString[i], arrayOfString[(i + 1)]);
      }
      paramViewHierarchyEncoder.endObject();
    }
    
    public int[] getAllAttributes()
    {
      return mThemeImpl.getAllAttributes();
    }
    
    int getAppliedStyleResId()
    {
      return mThemeImpl.getAppliedStyleResId();
    }
    
    public int getChangingConfigurations()
    {
      return mThemeImpl.getChangingConfigurations();
    }
    
    public Drawable getDrawable(int paramInt)
      throws Resources.NotFoundException
    {
      return getDrawable(paramInt, this);
    }
    
    public Resources.ThemeKey getKey()
    {
      return mThemeImpl.getKey();
    }
    
    long getNativeTheme()
    {
      return mThemeImpl.getNativeTheme();
    }
    
    public Resources getResources()
    {
      return Resources.this;
    }
    
    @ViewDebug.ExportedProperty(category="theme", hasAdjacentMapping=true)
    public String[] getTheme()
    {
      return mThemeImpl.getTheme();
    }
    
    public TypedArray obtainStyledAttributes(int paramInt, int[] paramArrayOfInt)
      throws Resources.NotFoundException
    {
      return mThemeImpl.obtainStyledAttributes(this, null, paramArrayOfInt, 0, paramInt);
    }
    
    public TypedArray obtainStyledAttributes(AttributeSet paramAttributeSet, int[] paramArrayOfInt, int paramInt1, int paramInt2)
    {
      return mThemeImpl.obtainStyledAttributes(this, paramAttributeSet, paramArrayOfInt, paramInt1, paramInt2);
    }
    
    public TypedArray obtainStyledAttributes(int[] paramArrayOfInt)
    {
      return mThemeImpl.obtainStyledAttributes(this, null, paramArrayOfInt, 0, 0);
    }
    
    public void rebase()
    {
      mThemeImpl.rebase();
    }
    
    public boolean resolveAttribute(int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
    {
      return mThemeImpl.resolveAttribute(paramInt, paramTypedValue, paramBoolean);
    }
    
    public TypedArray resolveAttributes(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      return mThemeImpl.resolveAttributes(this, paramArrayOfInt1, paramArrayOfInt2);
    }
    
    void setImpl(ResourcesImpl.ThemeImpl paramThemeImpl)
    {
      mThemeImpl = paramThemeImpl;
    }
    
    public void setTo(Theme paramTheme)
    {
      mThemeImpl.setTo(mThemeImpl);
    }
  }
  
  static class ThemeKey
    implements Cloneable
  {
    int mCount;
    boolean[] mForce;
    private int mHashCode = 0;
    int[] mResId;
    
    ThemeKey() {}
    
    public void append(int paramInt, boolean paramBoolean)
    {
      if (mResId == null) {
        mResId = new int[4];
      }
      if (mForce == null) {
        mForce = new boolean[4];
      }
      mResId = GrowingArrayUtils.append(mResId, mCount, paramInt);
      mForce = GrowingArrayUtils.append(mForce, mCount, paramBoolean);
      mCount += 1;
      mHashCode = (31 * (mHashCode * 31 + paramInt) + paramBoolean);
    }
    
    public ThemeKey clone()
    {
      ThemeKey localThemeKey = new ThemeKey();
      mResId = mResId;
      mForce = mForce;
      mCount = mCount;
      mHashCode = mHashCode;
      return localThemeKey;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()) && (hashCode() == paramObject.hashCode()))
      {
        paramObject = (ThemeKey)paramObject;
        if (mCount != mCount) {
          return false;
        }
        int i = mCount;
        int j = 0;
        while (j < i) {
          if ((mResId[j] == mResId[j]) && (mForce[j] == mForce[j])) {
            j++;
          } else {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    public int hashCode()
    {
      return mHashCode;
    }
    
    public void setTo(ThemeKey paramThemeKey)
    {
      Object localObject1 = mResId;
      Object localObject2 = null;
      if (localObject1 == null) {
        localObject1 = null;
      } else {
        localObject1 = (int[])mResId.clone();
      }
      mResId = ((int[])localObject1);
      if (mForce == null) {
        localObject1 = localObject2;
      } else {
        localObject1 = (boolean[])mForce.clone();
      }
      mForce = ((boolean[])localObject1);
      mCount = mCount;
    }
  }
}
