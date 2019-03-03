package android.graphics.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.PropertyValuesHolder.PropertyValues;
import android.animation.PropertyValuesHolder.PropertyValues.DataSource;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.ActivityThread;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.LongArray;
import android.util.PathParser.PathData;
import android.util.Property;
import android.view.Choreographer;
import android.view.DisplayListCanvas;
import android.view.RenderNode;
import android.view.RenderNodeAnimatorSetHelper;
import com.android.internal.R.styleable;
import com.android.internal.util.VirtualRefBasePtr;
import dalvik.annotation.optimization.FastNative;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawable
  extends Drawable
  implements Animatable2
{
  private static final String ANIMATED_VECTOR = "animated-vector";
  private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
  private static final String LOGTAG = "AnimatedVectorDrawable";
  private static final String TARGET = "target";
  private AnimatedVectorDrawableState mAnimatedVectorState;
  private ArrayList<Animatable2.AnimationCallback> mAnimationCallbacks = null;
  private Animator.AnimatorListener mAnimatorListener = null;
  private VectorDrawableAnimator mAnimatorSet;
  private AnimatorSet mAnimatorSetFromXml = null;
  private final Drawable.Callback mCallback = new Drawable.Callback()
  {
    public void invalidateDrawable(Drawable paramAnonymousDrawable)
    {
      invalidateSelf();
    }
    
    public void scheduleDrawable(Drawable paramAnonymousDrawable, Runnable paramAnonymousRunnable, long paramAnonymousLong)
    {
      scheduleSelf(paramAnonymousRunnable, paramAnonymousLong);
    }
    
    public void unscheduleDrawable(Drawable paramAnonymousDrawable, Runnable paramAnonymousRunnable)
    {
      unscheduleSelf(paramAnonymousRunnable);
    }
  };
  private boolean mMutated;
  private Resources mRes;
  
  public AnimatedVectorDrawable()
  {
    this(null, null);
  }
  
  private AnimatedVectorDrawable(AnimatedVectorDrawableState paramAnimatedVectorDrawableState, Resources paramResources)
  {
    mAnimatedVectorState = new AnimatedVectorDrawableState(paramAnimatedVectorDrawableState, mCallback, paramResources);
    mAnimatorSet = new VectorDrawableAnimatorRT(this);
    mRes = paramResources;
  }
  
  private static boolean containsSameValueType(PropertyValuesHolder paramPropertyValuesHolder, Property paramProperty)
  {
    paramPropertyValuesHolder = paramPropertyValuesHolder.getValueType();
    paramProperty = paramProperty.getType();
    Class localClass = Float.TYPE;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if ((paramPropertyValuesHolder != localClass) && (paramPropertyValuesHolder != Float.class))
    {
      if ((paramPropertyValuesHolder != Integer.TYPE) && (paramPropertyValuesHolder != Integer.class))
      {
        if (paramPropertyValuesHolder == paramProperty) {
          bool3 = true;
        }
        return bool3;
      }
      if ((paramProperty != Integer.TYPE) && (paramProperty != Integer.class)) {
        bool3 = bool1;
      } else {
        bool3 = true;
      }
      return bool3;
    }
    if ((paramProperty != Float.TYPE) && (paramProperty != Float.class)) {
      bool3 = bool2;
    } else {
      bool3 = true;
    }
    return bool3;
  }
  
  private void ensureAnimatorSet()
  {
    if (mAnimatorSetFromXml == null)
    {
      mAnimatorSetFromXml = new AnimatorSet();
      mAnimatedVectorState.prepareLocalAnimators(mAnimatorSetFromXml, mRes);
      mAnimatorSet.init(mAnimatorSetFromXml);
      mRes = null;
    }
  }
  
  private void fallbackOntoUI()
  {
    if ((mAnimatorSet instanceof VectorDrawableAnimatorRT))
    {
      VectorDrawableAnimatorRT localVectorDrawableAnimatorRT = (VectorDrawableAnimatorRT)mAnimatorSet;
      mAnimatorSet = new VectorDrawableAnimatorUI(this);
      if (mAnimatorSetFromXml != null) {
        mAnimatorSet.init(mAnimatorSetFromXml);
      }
      if (mListener != null) {
        mAnimatorSet.setListener(mListener);
      }
      localVectorDrawableAnimatorRT.transferPendingActions(mAnimatorSet);
    }
  }
  
  private static native void nAddAnimator(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, int paramInt1, int paramInt2);
  
  private static native long nCreateAnimatorSet();
  
  @FastNative
  private static native long nCreateGroupPropertyHolder(long paramLong, int paramInt, float paramFloat1, float paramFloat2);
  
  @FastNative
  private static native long nCreatePathColorPropertyHolder(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  @FastNative
  private static native long nCreatePathDataPropertyHolder(long paramLong1, long paramLong2, long paramLong3);
  
  @FastNative
  private static native long nCreatePathPropertyHolder(long paramLong, int paramInt, float paramFloat1, float paramFloat2);
  
  @FastNative
  private static native long nCreateRootAlphaPropertyHolder(long paramLong, float paramFloat1, float paramFloat2);
  
  @FastNative
  private static native void nEnd(long paramLong);
  
  @FastNative
  private static native void nReset(long paramLong);
  
  private static native void nReverse(long paramLong, VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT, int paramInt);
  
  private static native void nSetPropertyHolderData(long paramLong, float[] paramArrayOfFloat, int paramInt);
  
  private static native void nSetPropertyHolderData(long paramLong, int[] paramArrayOfInt, int paramInt);
  
  private static native void nSetVectorDrawableTarget(long paramLong1, long paramLong2);
  
  private static native void nStart(long paramLong, VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT, int paramInt);
  
  private void removeAnimatorSetListener()
  {
    if (mAnimatorListener != null)
    {
      mAnimatorSet.removeListener(mAnimatorListener);
      mAnimatorListener = null;
    }
  }
  
  private static boolean shouldIgnoreInvalidAnimation()
  {
    Application localApplication = ActivityThread.currentApplication();
    if ((localApplication != null) && (localApplication.getApplicationInfo() != null)) {
      return getApplicationInfotargetSdkVersion < 24;
    }
    return true;
  }
  
  private static void updateAnimatorProperty(Animator paramAnimator, String paramString, VectorDrawable paramVectorDrawable, boolean paramBoolean)
  {
    if ((paramAnimator instanceof ObjectAnimator))
    {
      PropertyValuesHolder[] arrayOfPropertyValuesHolder = ((ObjectAnimator)paramAnimator).getValues();
      for (int i = 0; i < arrayOfPropertyValuesHolder.length; i++)
      {
        PropertyValuesHolder localPropertyValuesHolder = arrayOfPropertyValuesHolder[i];
        String str = localPropertyValuesHolder.getPropertyName();
        Object localObject = paramVectorDrawable.getTargetByName(paramString);
        paramAnimator = null;
        if ((localObject instanceof VectorDrawable.VObject)) {
          paramAnimator = ((VectorDrawable.VObject)localObject).getProperty(str);
        } else if ((localObject instanceof VectorDrawable.VectorDrawableState)) {
          paramAnimator = ((VectorDrawable.VectorDrawableState)localObject).getProperty(str);
        }
        if (paramAnimator != null) {
          if (containsSameValueType(localPropertyValuesHolder, paramAnimator))
          {
            localPropertyValuesHolder.setProperty(paramAnimator);
          }
          else if (!paramBoolean)
          {
            paramString = new StringBuilder();
            paramString.append("Wrong valueType for Property: ");
            paramString.append(str);
            paramString.append(".  Expected type: ");
            paramString.append(paramAnimator.getType().toString());
            paramString.append(". Actual type defined in resources: ");
            paramString.append(localPropertyValuesHolder.getValueType().toString());
            throw new RuntimeException(paramString.toString());
          }
        }
      }
    }
    else if ((paramAnimator instanceof AnimatorSet))
    {
      paramAnimator = ((AnimatorSet)paramAnimator).getChildAnimations().iterator();
      while (paramAnimator.hasNext()) {
        updateAnimatorProperty((Animator)paramAnimator.next(), paramString, paramVectorDrawable, paramBoolean);
      }
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    VectorDrawable localVectorDrawable = mAnimatedVectorState.mVectorDrawable;
    if ((localVectorDrawable != null) && (localVectorDrawable.canApplyTheme())) {
      localVectorDrawable.applyTheme(paramTheme);
    }
    if (paramTheme != null) {
      mAnimatedVectorState.inflatePendingAnimators(paramTheme.getResources(), paramTheme);
    }
    if (mAnimatedVectorState.mPendingAnims == null) {
      mRes = null;
    }
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if (((mAnimatedVectorState != null) && (mAnimatedVectorState.canApplyTheme())) || (super.canApplyTheme())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canReverse()
  {
    return mAnimatorSet.canReverse();
  }
  
  public void clearAnimationCallbacks()
  {
    removeAnimatorSetListener();
    if (mAnimationCallbacks == null) {
      return;
    }
    mAnimationCallbacks.clear();
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    if (mAnimatedVectorState.mVectorDrawable != null) {
      mAnimatedVectorState.mVectorDrawable.clearMutated();
    }
    mMutated = false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if ((!paramCanvas.isHardwareAccelerated()) && ((mAnimatorSet instanceof VectorDrawableAnimatorRT)) && (!mAnimatorSet.isRunning()) && (mAnimatorSet).mPendingAnimationActions.size() > 0)) {
      fallbackOntoUI();
    }
    mAnimatorSet.onDraw(paramCanvas);
    mAnimatedVectorState.mVectorDrawable.draw(paramCanvas);
  }
  
  public void forceAnimationOnUI()
  {
    if ((mAnimatorSet instanceof VectorDrawableAnimatorRT)) {
      if (!((VectorDrawableAnimatorRT)mAnimatorSet).isRunning()) {
        fallbackOntoUI();
      } else {
        throw new UnsupportedOperationException("Cannot force Animated Vector Drawable to run on UI thread when the animation has started on RenderThread.");
      }
    }
  }
  
  public int getAlpha()
  {
    return mAnimatedVectorState.mVectorDrawable.getAlpha();
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mAnimatedVectorState.getChangingConfigurations();
  }
  
  public ColorFilter getColorFilter()
  {
    return mAnimatedVectorState.mVectorDrawable.getColorFilter();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    mAnimatedVectorState.mChangingConfigurations = getChangingConfigurations();
    return mAnimatedVectorState;
  }
  
  public int getIntrinsicHeight()
  {
    return mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
  }
  
  public int getIntrinsicWidth()
  {
    return mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public Insets getOpticalInsets()
  {
    return mAnimatedVectorState.mVectorDrawable.getOpticalInsets();
  }
  
  public void getOutline(Outline paramOutline)
  {
    mAnimatedVectorState.mVectorDrawable.getOutline(paramOutline);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AnimatedVectorDrawableState localAnimatedVectorDrawableState = mAnimatedVectorState;
    int i = paramXmlPullParser.getEventType();
    float f1 = 1.0F;
    int j = paramXmlPullParser.getDepth();
    Object localObject1;
    for (;;)
    {
      localObject1 = null;
      if ((i == 1) || ((paramXmlPullParser.getDepth() < j + 1) && (i == 3))) {
        break;
      }
      float f2 = f1;
      if (i == 2)
      {
        localObject1 = paramXmlPullParser.getName();
        Object localObject2;
        if ("animated-vector".equals(localObject1))
        {
          localObject1 = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedVectorDrawable);
          i = ((TypedArray)localObject1).getResourceId(0, 0);
          if (i != 0)
          {
            localObject2 = (VectorDrawable)paramResources.getDrawable(i, paramTheme).mutate();
            ((VectorDrawable)localObject2).setAllowCaching(false);
            ((VectorDrawable)localObject2).setCallback(mCallback);
            f1 = ((VectorDrawable)localObject2).getPixelSize();
            if (mVectorDrawable != null) {
              mVectorDrawable.setCallback(null);
            }
            mVectorDrawable = ((VectorDrawable)localObject2);
          }
          ((TypedArray)localObject1).recycle();
          f2 = f1;
        }
        else
        {
          f2 = f1;
          if ("target".equals(localObject1))
          {
            localObject1 = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedVectorDrawableTarget);
            String str = ((TypedArray)localObject1).getString(0);
            i = ((TypedArray)localObject1).getResourceId(1, 0);
            if (i != 0) {
              if (paramTheme != null)
              {
                localObject2 = AnimatorInflater.loadAnimator(paramResources, paramTheme, i, f1);
                updateAnimatorProperty((Animator)localObject2, str, mVectorDrawable, mShouldIgnoreInvalidAnim);
                localAnimatedVectorDrawableState.addTargetAnimator(str, (Animator)localObject2);
              }
              else
              {
                localAnimatedVectorDrawableState.addPendingAnimator(i, f1, str);
              }
            }
            ((TypedArray)localObject1).recycle();
            f2 = f1;
          }
        }
      }
      i = paramXmlPullParser.next();
      f1 = f2;
    }
    if (mPendingAnims == null) {
      paramResources = (Resources)localObject1;
    }
    mRes = paramResources;
  }
  
  public boolean isRunning()
  {
    return mAnimatorSet.isRunning();
  }
  
  public boolean isStateful()
  {
    return mAnimatedVectorState.mVectorDrawable.isStateful();
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mAnimatedVectorState = new AnimatedVectorDrawableState(mAnimatedVectorState, mCallback, mRes);
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    mAnimatedVectorState.mVectorDrawable.setBounds(paramRect);
  }
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    return mAnimatedVectorState.mVectorDrawable.setLayoutDirection(paramInt);
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    return mAnimatedVectorState.mVectorDrawable.setLevel(paramInt);
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    return mAnimatedVectorState.mVectorDrawable.setState(paramArrayOfInt);
  }
  
  public void registerAnimationCallback(Animatable2.AnimationCallback paramAnimationCallback)
  {
    if (paramAnimationCallback == null) {
      return;
    }
    if (mAnimationCallbacks == null) {
      mAnimationCallbacks = new ArrayList();
    }
    mAnimationCallbacks.add(paramAnimationCallback);
    if (mAnimatorListener == null) {
      mAnimatorListener = new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          paramAnonymousAnimator = new ArrayList(mAnimationCallbacks);
          int i = paramAnonymousAnimator.size();
          for (int j = 0; j < i; j++) {
            ((Animatable2.AnimationCallback)paramAnonymousAnimator.get(j)).onAnimationEnd(AnimatedVectorDrawable.this);
          }
        }
        
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          paramAnonymousAnimator = new ArrayList(mAnimationCallbacks);
          int i = paramAnonymousAnimator.size();
          for (int j = 0; j < i; j++) {
            ((Animatable2.AnimationCallback)paramAnonymousAnimator.get(j)).onAnimationStart(AnimatedVectorDrawable.this);
          }
        }
      };
    }
    mAnimatorSet.setListener(mAnimatorListener);
  }
  
  public void reset()
  {
    ensureAnimatorSet();
    mAnimatorSet.reset();
  }
  
  public void reverse()
  {
    ensureAnimatorSet();
    if (!canReverse())
    {
      Log.w("AnimatedVectorDrawable", "AnimatedVectorDrawable can't reverse()");
      return;
    }
    mAnimatorSet.reverse();
  }
  
  public void setAlpha(int paramInt)
  {
    mAnimatedVectorState.mVectorDrawable.setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mAnimatedVectorState.mVectorDrawable.setColorFilter(paramColorFilter);
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    mAnimatedVectorState.mVectorDrawable.setHotspot(paramFloat1, paramFloat2);
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mAnimatedVectorState.mVectorDrawable.setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    mAnimatedVectorState.mVectorDrawable.setTintList(paramColorStateList);
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    mAnimatedVectorState.mVectorDrawable.setTintMode(paramMode);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((mAnimatorSet.isInfinite()) && (mAnimatorSet.isStarted())) {
      if (paramBoolean1) {
        mAnimatorSet.resume();
      } else {
        mAnimatorSet.pause();
      }
    }
    mAnimatedVectorState.mVectorDrawable.setVisible(paramBoolean1, paramBoolean2);
    return super.setVisible(paramBoolean1, paramBoolean2);
  }
  
  public void start()
  {
    ensureAnimatorSet();
    mAnimatorSet.start();
  }
  
  public void stop()
  {
    mAnimatorSet.end();
  }
  
  public boolean unregisterAnimationCallback(Animatable2.AnimationCallback paramAnimationCallback)
  {
    if ((mAnimationCallbacks != null) && (paramAnimationCallback != null))
    {
      boolean bool = mAnimationCallbacks.remove(paramAnimationCallback);
      if (mAnimationCallbacks.size() == 0) {
        removeAnimatorSetListener();
      }
      return bool;
    }
    return false;
  }
  
  private static class AnimatedVectorDrawableState
    extends Drawable.ConstantState
  {
    ArrayList<Animator> mAnimators;
    int mChangingConfigurations;
    ArrayList<PendingAnimator> mPendingAnims;
    private final boolean mShouldIgnoreInvalidAnim = AnimatedVectorDrawable.access$400();
    ArrayMap<Animator, String> mTargetNameMap;
    VectorDrawable mVectorDrawable;
    
    public AnimatedVectorDrawableState(AnimatedVectorDrawableState paramAnimatedVectorDrawableState, Drawable.Callback paramCallback, Resources paramResources)
    {
      if (paramAnimatedVectorDrawableState != null)
      {
        mChangingConfigurations = mChangingConfigurations;
        if (mVectorDrawable != null)
        {
          Drawable.ConstantState localConstantState = mVectorDrawable.getConstantState();
          if (paramResources != null) {
            mVectorDrawable = ((VectorDrawable)localConstantState.newDrawable(paramResources));
          } else {
            mVectorDrawable = ((VectorDrawable)localConstantState.newDrawable());
          }
          mVectorDrawable = ((VectorDrawable)mVectorDrawable.mutate());
          mVectorDrawable.setCallback(paramCallback);
          mVectorDrawable.setLayoutDirection(mVectorDrawable.getLayoutDirection());
          mVectorDrawable.setBounds(mVectorDrawable.getBounds());
          mVectorDrawable.setAllowCaching(false);
        }
        if (mAnimators != null) {
          mAnimators = new ArrayList(mAnimators);
        }
        if (mTargetNameMap != null) {
          mTargetNameMap = new ArrayMap(mTargetNameMap);
        }
        if (mPendingAnims != null) {
          mPendingAnims = new ArrayList(mPendingAnims);
        }
      }
      else
      {
        mVectorDrawable = new VectorDrawable();
      }
    }
    
    private Animator prepareLocalAnimator(int paramInt)
    {
      Object localObject1 = (Animator)mAnimators.get(paramInt);
      Object localObject2 = ((Animator)localObject1).clone();
      String str = (String)mTargetNameMap.get(localObject1);
      localObject1 = mVectorDrawable.getTargetByName(str);
      if (!mShouldIgnoreInvalidAnim) {
        if (localObject1 != null)
        {
          if ((!(localObject1 instanceof VectorDrawable.VectorDrawableState)) && (!(localObject1 instanceof VectorDrawable.VObject)))
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Target should be either VGroup, VPath, or ConstantState, ");
            ((StringBuilder)localObject2).append(localObject1.getClass());
            ((StringBuilder)localObject2).append(" is not supported");
            throw new UnsupportedOperationException(((StringBuilder)localObject2).toString());
          }
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Target with the name \"");
          ((StringBuilder)localObject1).append(str);
          ((StringBuilder)localObject1).append("\" cannot be found in the VectorDrawable to be animated.");
          throw new IllegalStateException(((StringBuilder)localObject1).toString());
        }
      }
      ((Animator)localObject2).setTarget(localObject1);
      return localObject2;
    }
    
    public void addPendingAnimator(int paramInt, float paramFloat, String paramString)
    {
      if (mPendingAnims == null) {
        mPendingAnims = new ArrayList(1);
      }
      mPendingAnims.add(new PendingAnimator(paramInt, paramFloat, paramString));
    }
    
    public void addTargetAnimator(String paramString, Animator paramAnimator)
    {
      if (mAnimators == null)
      {
        mAnimators = new ArrayList(1);
        mTargetNameMap = new ArrayMap(1);
      }
      mAnimators.add(paramAnimator);
      mTargetNameMap.put(paramAnimator, paramString);
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if (((mVectorDrawable == null) || (!mVectorDrawable.canApplyTheme())) && (mPendingAnims == null) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConfigurations;
    }
    
    public void inflatePendingAnimators(Resources paramResources, Resources.Theme paramTheme)
    {
      ArrayList localArrayList = mPendingAnims;
      if (localArrayList != null)
      {
        mPendingAnims = null;
        int i = 0;
        int j = localArrayList.size();
        while (i < j)
        {
          PendingAnimator localPendingAnimator = (PendingAnimator)localArrayList.get(i);
          Animator localAnimator = localPendingAnimator.newInstance(paramResources, paramTheme);
          AnimatedVectorDrawable.updateAnimatorProperty(localAnimator, target, mVectorDrawable, mShouldIgnoreInvalidAnim);
          addTargetAnimator(target, localAnimator);
          i++;
        }
      }
    }
    
    public Drawable newDrawable()
    {
      return new AnimatedVectorDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AnimatedVectorDrawable(this, paramResources, null);
    }
    
    public void prepareLocalAnimators(AnimatorSet paramAnimatorSet, Resources paramResources)
    {
      if (mPendingAnims != null)
      {
        if (paramResources != null) {
          inflatePendingAnimators(paramResources, null);
        } else {
          Log.e("AnimatedVectorDrawable", "Failed to load animators. Either the AnimatedVectorDrawable must be created using a Resources object or applyTheme() must be called with a non-null Theme object.");
        }
        mPendingAnims = null;
      }
      int i;
      if (mAnimators == null) {
        i = 0;
      } else {
        i = mAnimators.size();
      }
      if (i > 0)
      {
        paramAnimatorSet = paramAnimatorSet.play(prepareLocalAnimator(0));
        for (int j = 1; j < i; j++) {
          paramAnimatorSet.with(prepareLocalAnimator(j));
        }
      }
    }
    
    private static class PendingAnimator
    {
      public final int animResId;
      public final float pathErrorScale;
      public final String target;
      
      public PendingAnimator(int paramInt, float paramFloat, String paramString)
      {
        animResId = paramInt;
        pathErrorScale = paramFloat;
        target = paramString;
      }
      
      public Animator newInstance(Resources paramResources, Resources.Theme paramTheme)
      {
        return AnimatorInflater.loadAnimator(paramResources, paramTheme, animResId, pathErrorScale);
      }
    }
  }
  
  private static abstract interface VectorDrawableAnimator
  {
    public abstract boolean canReverse();
    
    public abstract void end();
    
    public abstract void init(AnimatorSet paramAnimatorSet);
    
    public abstract boolean isInfinite();
    
    public abstract boolean isRunning();
    
    public abstract boolean isStarted();
    
    public abstract void onDraw(Canvas paramCanvas);
    
    public abstract void pause();
    
    public abstract void removeListener(Animator.AnimatorListener paramAnimatorListener);
    
    public abstract void reset();
    
    public abstract void resume();
    
    public abstract void reverse();
    
    public abstract void setListener(Animator.AnimatorListener paramAnimatorListener);
    
    public abstract void start();
  }
  
  public static class VectorDrawableAnimatorRT
    implements AnimatedVectorDrawable.VectorDrawableAnimator
  {
    private static final int END_ANIMATION = 4;
    private static final int MAX_SAMPLE_POINTS = 300;
    private static final int RESET_ANIMATION = 3;
    private static final int REVERSE_ANIMATION = 2;
    private static final int START_ANIMATION = 1;
    private boolean mContainsSequentialAnimators = false;
    private final AnimatedVectorDrawable mDrawable;
    private boolean mInitialized = false;
    private boolean mIsInfinite = false;
    private boolean mIsReversible = false;
    private int mLastListenerId = 0;
    private WeakReference<RenderNode> mLastSeenTarget = null;
    private Animator.AnimatorListener mListener = null;
    private final IntArray mPendingAnimationActions = new IntArray();
    private long mSetPtr = 0L;
    private final VirtualRefBasePtr mSetRefBasePtr;
    private final LongArray mStartDelays = new LongArray();
    private boolean mStarted = false;
    private PropertyValuesHolder.PropertyValues mTmpValues = new PropertyValuesHolder.PropertyValues();
    
    VectorDrawableAnimatorRT(AnimatedVectorDrawable paramAnimatedVectorDrawable)
    {
      mDrawable = paramAnimatedVectorDrawable;
      mSetPtr = AnimatedVectorDrawable.access$800();
      mSetRefBasePtr = new VirtualRefBasePtr(mSetPtr);
    }
    
    private void addPendingAction(int paramInt)
    {
      invalidateOwningView();
      mPendingAnimationActions.add(paramInt);
    }
    
    private static void callOnFinished(VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT, int paramInt)
    {
      paramVectorDrawableAnimatorRT.onAnimationEnd(paramInt);
    }
    
    private static float[] createFloatDataPoints(PropertyValuesHolder.PropertyValues.DataSource paramDataSource, long paramLong)
    {
      int i = getFrameCount(paramLong);
      float[] arrayOfFloat = new float[i];
      float f = i - 1;
      for (int j = 0; j < i; j++) {
        arrayOfFloat[j] = ((Float)paramDataSource.getValueAtFraction(j / f)).floatValue();
      }
      return arrayOfFloat;
    }
    
    private static int[] createIntDataPoints(PropertyValuesHolder.PropertyValues.DataSource paramDataSource, long paramLong)
    {
      int i = getFrameCount(paramLong);
      int[] arrayOfInt = new int[i];
      float f = i - 1;
      for (int j = 0; j < i; j++) {
        arrayOfInt[j] = ((Integer)paramDataSource.getValueAtFraction(j / f)).intValue();
      }
      return arrayOfInt;
    }
    
    private void createNativeChildAnimator(long paramLong1, long paramLong2, ObjectAnimator paramObjectAnimator)
    {
      long l1 = paramObjectAnimator.getDuration();
      int i = paramObjectAnimator.getRepeatCount();
      long l2 = paramObjectAnimator.getStartDelay();
      TimeInterpolator localTimeInterpolator = paramObjectAnimator.getInterpolator();
      long l3 = RenderNodeAnimatorSetHelper.createNativeInterpolator(localTimeInterpolator, l1);
      paramLong2 = ((float)(paramLong2 + l2) * ValueAnimator.getDurationScale());
      l1 = ((float)l1 * ValueAnimator.getDurationScale());
      mStartDelays.add(paramLong2);
      AnimatedVectorDrawable.nAddAnimator(mSetPtr, paramLong1, l3, paramLong2, l1, i, paramObjectAnimator.getRepeatMode());
    }
    
    private void createRTAnimator(ObjectAnimator paramObjectAnimator, long paramLong)
    {
      PropertyValuesHolder[] arrayOfPropertyValuesHolder = paramObjectAnimator.getValues();
      Object localObject = paramObjectAnimator.getTarget();
      if ((localObject instanceof VectorDrawable.VGroup))
      {
        createRTAnimatorForGroup(arrayOfPropertyValuesHolder, paramObjectAnimator, (VectorDrawable.VGroup)localObject, paramLong);
      }
      else
      {
        if ((localObject instanceof VectorDrawable.VPath))
        {
          for (int i = 0;; i++)
          {
            if (i >= arrayOfPropertyValuesHolder.length) {
              return;
            }
            arrayOfPropertyValuesHolder[i].getPropertyValues(mTmpValues);
            if (((mTmpValues.endValue instanceof PathParser.PathData)) && (mTmpValues.propertyName.equals("pathData"))) {
              createRTAnimatorForPath(paramObjectAnimator, (VectorDrawable.VPath)localObject, paramLong);
            } else if ((localObject instanceof VectorDrawable.VFullPath)) {
              createRTAnimatorForFullPath(paramObjectAnimator, (VectorDrawable.VFullPath)localObject, paramLong);
            } else {
              if (!access$900mDrawable).mShouldIgnoreInvalidAnim) {
                break;
              }
            }
          }
          throw new IllegalArgumentException("ClipPath only supports PathData property");
        }
        if ((localObject instanceof VectorDrawable.VectorDrawableState)) {
          createRTAnimatorForRootGroup(arrayOfPropertyValuesHolder, paramObjectAnimator, (VectorDrawable.VectorDrawableState)localObject, paramLong);
        }
      }
    }
    
    private void createRTAnimatorForFullPath(ObjectAnimator paramObjectAnimator, VectorDrawable.VFullPath paramVFullPath, long paramLong)
    {
      int i = paramVFullPath.getPropertyIndex(mTmpValues.propertyName);
      long l1 = paramVFullPath.getNativePtr();
      long l2;
      if ((mTmpValues.type != Float.class) && (mTmpValues.type != Float.TYPE))
      {
        if ((mTmpValues.type != Integer.class) && (mTmpValues.type != Integer.TYPE))
        {
          if (access$900mDrawable).mShouldIgnoreInvalidAnim) {
            return;
          }
          paramObjectAnimator = new StringBuilder();
          paramObjectAnimator.append("Unsupported type: ");
          paramObjectAnimator.append(mTmpValues.type);
          paramObjectAnimator.append(". Only float, int or PathData value is supported for Paths.");
          throw new UnsupportedOperationException(paramObjectAnimator.toString());
        }
        l2 = AnimatedVectorDrawable.nCreatePathColorPropertyHolder(l1, i, ((Integer)mTmpValues.startValue).intValue(), ((Integer)mTmpValues.endValue).intValue());
        l1 = l2;
        if (mTmpValues.dataSource != null)
        {
          paramVFullPath = createIntDataPoints(mTmpValues.dataSource, paramObjectAnimator.getDuration());
          AnimatedVectorDrawable.nSetPropertyHolderData(l2, paramVFullPath, paramVFullPath.length);
          l1 = l2;
        }
      }
      else
      {
        if (i < 0)
        {
          if (access$900mDrawable).mShouldIgnoreInvalidAnim) {
            return;
          }
          paramObjectAnimator = new StringBuilder();
          paramObjectAnimator.append("Property: ");
          paramObjectAnimator.append(mTmpValues.propertyName);
          paramObjectAnimator.append(" is not supported for FullPath");
          throw new IllegalArgumentException(paramObjectAnimator.toString());
        }
        l2 = AnimatedVectorDrawable.nCreatePathPropertyHolder(l1, i, ((Float)mTmpValues.startValue).floatValue(), ((Float)mTmpValues.endValue).floatValue());
        l1 = l2;
        if (mTmpValues.dataSource != null)
        {
          paramVFullPath = createFloatDataPoints(mTmpValues.dataSource, paramObjectAnimator.getDuration());
          AnimatedVectorDrawable.nSetPropertyHolderData(l2, paramVFullPath, paramVFullPath.length);
          l1 = l2;
        }
      }
      createNativeChildAnimator(l1, paramLong, paramObjectAnimator);
    }
    
    private void createRTAnimatorForGroup(PropertyValuesHolder[] paramArrayOfPropertyValuesHolder, ObjectAnimator paramObjectAnimator, VectorDrawable.VGroup paramVGroup, long paramLong)
    {
      long l1 = paramVGroup.getNativePtr();
      for (int i = 0; i < paramArrayOfPropertyValuesHolder.length; i++)
      {
        paramArrayOfPropertyValuesHolder[i].getPropertyValues(mTmpValues);
        int j = VectorDrawable.VGroup.getPropertyIndex(mTmpValues.propertyName);
        if (((mTmpValues.type == Float.class) || (mTmpValues.type == Float.TYPE)) && (j >= 0))
        {
          long l2 = AnimatedVectorDrawable.nCreateGroupPropertyHolder(l1, j, ((Float)mTmpValues.startValue).floatValue(), ((Float)mTmpValues.endValue).floatValue());
          if (mTmpValues.dataSource != null)
          {
            paramVGroup = createFloatDataPoints(mTmpValues.dataSource, paramObjectAnimator.getDuration());
            AnimatedVectorDrawable.nSetPropertyHolderData(l2, paramVGroup, paramVGroup.length);
          }
          createNativeChildAnimator(l2, paramLong, paramObjectAnimator);
        }
      }
    }
    
    private void createRTAnimatorForPath(ObjectAnimator paramObjectAnimator, VectorDrawable.VPath paramVPath, long paramLong)
    {
      createNativeChildAnimator(AnimatedVectorDrawable.nCreatePathDataPropertyHolder(paramVPath.getNativePtr(), ((PathParser.PathData)mTmpValues.startValue).getNativePtr(), ((PathParser.PathData)mTmpValues.endValue).getNativePtr()), paramLong, paramObjectAnimator);
    }
    
    private void createRTAnimatorForRootGroup(PropertyValuesHolder[] paramArrayOfPropertyValuesHolder, ObjectAnimator paramObjectAnimator, VectorDrawable.VectorDrawableState paramVectorDrawableState, long paramLong)
    {
      long l = paramVectorDrawableState.getNativeRenderer();
      if (!paramObjectAnimator.getPropertyName().equals("alpha"))
      {
        if (access$900mDrawable).mShouldIgnoreInvalidAnim) {
          return;
        }
        throw new UnsupportedOperationException("Only alpha is supported for root group");
      }
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3;
      for (int i = 0;; i++)
      {
        paramVectorDrawableState = localObject1;
        localObject3 = localObject2;
        if (i >= paramArrayOfPropertyValuesHolder.length) {
          break;
        }
        paramArrayOfPropertyValuesHolder[i].getPropertyValues(mTmpValues);
        if (mTmpValues.propertyName.equals("alpha"))
        {
          paramVectorDrawableState = (Float)mTmpValues.startValue;
          localObject3 = (Float)mTmpValues.endValue;
          break;
        }
      }
      if ((paramVectorDrawableState == null) && (localObject3 == null))
      {
        if (access$900mDrawable).mShouldIgnoreInvalidAnim) {
          return;
        }
        throw new UnsupportedOperationException("No alpha values are specified");
      }
      l = AnimatedVectorDrawable.nCreateRootAlphaPropertyHolder(l, paramVectorDrawableState.floatValue(), ((Float)localObject3).floatValue());
      if (mTmpValues.dataSource != null)
      {
        paramArrayOfPropertyValuesHolder = createFloatDataPoints(mTmpValues.dataSource, paramObjectAnimator.getDuration());
        AnimatedVectorDrawable.nSetPropertyHolderData(l, paramArrayOfPropertyValuesHolder, paramArrayOfPropertyValuesHolder.length);
      }
      createNativeChildAnimator(l, paramLong, paramObjectAnimator);
    }
    
    private void endAnimation()
    {
      AnimatedVectorDrawable.nEnd(mSetPtr);
      invalidateOwningView();
    }
    
    private static int getFrameCount(long paramLong)
    {
      int i = (int)(Choreographer.getInstance().getFrameIntervalNanos() / 1000000L);
      int j = Math.max(2, (int)Math.ceil(paramLong / i));
      i = j;
      if (j > 300)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Duration for the animation is too long :");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(", the animation will subsample the keyframe or path data.");
        Log.w("AnimatedVectorDrawable", localStringBuilder.toString());
        i = 300;
      }
      return i;
    }
    
    private void handlePendingAction(int paramInt)
    {
      if (paramInt == 1)
      {
        startAnimation();
      }
      else if (paramInt == 2)
      {
        reverseAnimation();
      }
      else if (paramInt == 3)
      {
        resetAnimation();
      }
      else
      {
        if (paramInt != 4) {
          break label46;
        }
        endAnimation();
      }
      return;
      label46:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Animation action ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("is not supported");
      throw new UnsupportedOperationException(localStringBuilder.toString());
    }
    
    private void invalidateOwningView()
    {
      mDrawable.invalidateSelf();
    }
    
    private void onAnimationEnd(int paramInt)
    {
      if (paramInt != mLastListenerId) {
        return;
      }
      mStarted = false;
      invalidateOwningView();
      if (mListener != null) {
        mListener.onAnimationEnd(null);
      }
    }
    
    private void parseAnimatorSet(AnimatorSet paramAnimatorSet, long paramLong)
    {
      ArrayList localArrayList = paramAnimatorSet.getChildAnimations();
      boolean bool = paramAnimatorSet.shouldPlayTogether();
      int i = 0;
      while (i < localArrayList.size())
      {
        paramAnimatorSet = (Animator)localArrayList.get(i);
        if ((paramAnimatorSet instanceof AnimatorSet)) {
          parseAnimatorSet((AnimatorSet)paramAnimatorSet, paramLong);
        } else if ((paramAnimatorSet instanceof ObjectAnimator)) {
          createRTAnimator((ObjectAnimator)paramAnimatorSet, paramLong);
        }
        long l = paramLong;
        if (!bool)
        {
          l = paramLong + paramAnimatorSet.getTotalDuration();
          mContainsSequentialAnimators = true;
        }
        i++;
        paramLong = l;
      }
    }
    
    private void resetAnimation()
    {
      AnimatedVectorDrawable.nReset(mSetPtr);
      invalidateOwningView();
    }
    
    private void reverseAnimation()
    {
      mStarted = true;
      long l = mSetPtr;
      int i = mLastListenerId + 1;
      mLastListenerId = i;
      AnimatedVectorDrawable.nReverse(l, this, i);
      invalidateOwningView();
      if (mListener != null) {
        mListener.onAnimationStart(null);
      }
    }
    
    private void startAnimation()
    {
      mStarted = true;
      long l = mSetPtr;
      int i = mLastListenerId + 1;
      mLastListenerId = i;
      AnimatedVectorDrawable.nStart(l, this, i);
      invalidateOwningView();
      if (mListener != null) {
        mListener.onAnimationStart(null);
      }
    }
    
    private void transferPendingActions(AnimatedVectorDrawable.VectorDrawableAnimator paramVectorDrawableAnimator)
    {
      int i = 0;
      while (i < mPendingAnimationActions.size())
      {
        int j = mPendingAnimationActions.get(i);
        if (j == 1)
        {
          paramVectorDrawableAnimator.start();
        }
        else if (j == 4)
        {
          paramVectorDrawableAnimator.end();
        }
        else if (j == 2)
        {
          paramVectorDrawableAnimator.reverse();
        }
        else
        {
          if (j != 3) {
            break label81;
          }
          paramVectorDrawableAnimator.reset();
        }
        i++;
        continue;
        label81:
        paramVectorDrawableAnimator = new StringBuilder();
        paramVectorDrawableAnimator.append("Animation action ");
        paramVectorDrawableAnimator.append(j);
        paramVectorDrawableAnimator.append("is not supported");
        throw new UnsupportedOperationException(paramVectorDrawableAnimator.toString());
      }
      mPendingAnimationActions.clear();
    }
    
    private boolean useLastSeenTarget()
    {
      if (mLastSeenTarget != null) {
        return useTarget((RenderNode)mLastSeenTarget.get());
      }
      return false;
    }
    
    private boolean useTarget(RenderNode paramRenderNode)
    {
      if ((paramRenderNode != null) && (paramRenderNode.isAttached()))
      {
        paramRenderNode.registerVectorDrawableAnimator(this);
        return true;
      }
      return false;
    }
    
    public boolean canReverse()
    {
      return mIsReversible;
    }
    
    public void end()
    {
      if (!mInitialized) {
        return;
      }
      if (useLastSeenTarget()) {
        endAnimation();
      } else {
        addPendingAction(4);
      }
    }
    
    public long getAnimatorNativePtr()
    {
      return mSetPtr;
    }
    
    public void init(AnimatorSet paramAnimatorSet)
    {
      if (!mInitialized)
      {
        parseAnimatorSet(paramAnimatorSet, 0L);
        long l = mDrawable.mAnimatedVectorState.mVectorDrawable.getNativeTree();
        AnimatedVectorDrawable.nSetVectorDrawableTarget(mSetPtr, l);
        mInitialized = true;
        boolean bool;
        if (paramAnimatorSet.getTotalDuration() == -1L) {
          bool = true;
        } else {
          bool = false;
        }
        mIsInfinite = bool;
        mIsReversible = true;
        if (mContainsSequentialAnimators) {
          mIsReversible = false;
        } else {
          for (int i = 0; i < mStartDelays.size(); i++) {
            if (mStartDelays.get(i) > 0L)
            {
              mIsReversible = false;
              return;
            }
          }
        }
        return;
      }
      throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
    }
    
    public boolean isInfinite()
    {
      return mIsInfinite;
    }
    
    public boolean isRunning()
    {
      if (!mInitialized) {
        return false;
      }
      return mStarted;
    }
    
    public boolean isStarted()
    {
      return mStarted;
    }
    
    public void onDraw(Canvas paramCanvas)
    {
      if (paramCanvas.isHardwareAccelerated()) {
        recordLastSeenTarget((DisplayListCanvas)paramCanvas);
      }
    }
    
    public void pause() {}
    
    protected void recordLastSeenTarget(DisplayListCanvas paramDisplayListCanvas)
    {
      paramDisplayListCanvas = RenderNodeAnimatorSetHelper.getTarget(paramDisplayListCanvas);
      mLastSeenTarget = new WeakReference(paramDisplayListCanvas);
      if (((mInitialized) || (mPendingAnimationActions.size() > 0)) && (useTarget(paramDisplayListCanvas)))
      {
        for (int i = 0; i < mPendingAnimationActions.size(); i++) {
          handlePendingAction(mPendingAnimationActions.get(i));
        }
        mPendingAnimationActions.clear();
      }
    }
    
    public void removeListener(Animator.AnimatorListener paramAnimatorListener)
    {
      mListener = null;
    }
    
    public void reset()
    {
      if (!mInitialized) {
        return;
      }
      if (useLastSeenTarget()) {
        resetAnimation();
      } else {
        addPendingAction(3);
      }
    }
    
    public void resume() {}
    
    public void reverse()
    {
      if ((mIsReversible) && (mInitialized))
      {
        if (useLastSeenTarget()) {
          reverseAnimation();
        } else {
          addPendingAction(2);
        }
        return;
      }
    }
    
    public void setListener(Animator.AnimatorListener paramAnimatorListener)
    {
      mListener = paramAnimatorListener;
    }
    
    public void start()
    {
      if (!mInitialized) {
        return;
      }
      if (useLastSeenTarget()) {
        startAnimation();
      } else {
        addPendingAction(1);
      }
    }
  }
  
  private static class VectorDrawableAnimatorUI
    implements AnimatedVectorDrawable.VectorDrawableAnimator
  {
    private final Drawable mDrawable;
    private boolean mIsInfinite = false;
    private ArrayList<Animator.AnimatorListener> mListenerArray = null;
    private AnimatorSet mSet = null;
    
    VectorDrawableAnimatorUI(AnimatedVectorDrawable paramAnimatedVectorDrawable)
    {
      mDrawable = paramAnimatedVectorDrawable;
    }
    
    private void invalidateOwningView()
    {
      mDrawable.invalidateSelf();
    }
    
    public boolean canReverse()
    {
      boolean bool;
      if ((mSet != null) && (mSet.canReverse())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void end()
    {
      if (mSet == null) {
        return;
      }
      mSet.end();
    }
    
    public void init(AnimatorSet paramAnimatorSet)
    {
      if (mSet == null)
      {
        mSet = paramAnimatorSet.clone();
        long l = mSet.getTotalDuration();
        int i = 0;
        boolean bool;
        if (l == -1L) {
          bool = true;
        } else {
          bool = false;
        }
        mIsInfinite = bool;
        if ((mListenerArray != null) && (!mListenerArray.isEmpty()))
        {
          while (i < mListenerArray.size())
          {
            mSet.addListener((Animator.AnimatorListener)mListenerArray.get(i));
            i++;
          }
          mListenerArray.clear();
          mListenerArray = null;
        }
        return;
      }
      throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
    }
    
    public boolean isInfinite()
    {
      return mIsInfinite;
    }
    
    public boolean isRunning()
    {
      boolean bool;
      if ((mSet != null) && (mSet.isRunning())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isStarted()
    {
      boolean bool;
      if ((mSet != null) && (mSet.isStarted())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onDraw(Canvas paramCanvas)
    {
      if ((mSet != null) && (mSet.isStarted())) {
        invalidateOwningView();
      }
    }
    
    public void pause()
    {
      if (mSet == null) {
        return;
      }
      mSet.pause();
    }
    
    public void removeListener(Animator.AnimatorListener paramAnimatorListener)
    {
      if (mSet == null)
      {
        if (mListenerArray == null) {
          return;
        }
        mListenerArray.remove(paramAnimatorListener);
      }
      else
      {
        mSet.removeListener(paramAnimatorListener);
      }
    }
    
    public void reset()
    {
      if (mSet == null) {
        return;
      }
      start();
      mSet.cancel();
    }
    
    public void resume()
    {
      if (mSet == null) {
        return;
      }
      mSet.resume();
    }
    
    public void reverse()
    {
      if (mSet == null) {
        return;
      }
      mSet.reverse();
      invalidateOwningView();
    }
    
    public void setListener(Animator.AnimatorListener paramAnimatorListener)
    {
      if (mSet == null)
      {
        if (mListenerArray == null) {
          mListenerArray = new ArrayList();
        }
        mListenerArray.add(paramAnimatorListener);
      }
      else
      {
        mSet.addListener(paramAnimatorListener);
      }
    }
    
    public void start()
    {
      if ((mSet != null) && (!mSet.isStarted()))
      {
        mSet.start();
        invalidateOwningView();
        return;
      }
    }
  }
}
