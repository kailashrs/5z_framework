package com.android.internal.graphics.drawable;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationScaleListDrawable
  extends DrawableContainer
  implements Animatable
{
  private static final String TAG = "AnimationScaleListDrawable";
  private AnimationScaleListState mAnimationScaleListState;
  private boolean mMutated;
  
  public AnimationScaleListDrawable()
  {
    this(null, null);
  }
  
  private AnimationScaleListDrawable(AnimationScaleListState paramAnimationScaleListState, Resources paramResources)
  {
    setConstantState(new AnimationScaleListState(paramAnimationScaleListState, this, paramResources));
    onStateChange(getState());
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AnimationScaleListState localAnimationScaleListState = mAnimationScaleListState;
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        break;
      }
      if ((j == 2) && (k <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimationScaleListDrawableItem);
        Drawable localDrawable = ((TypedArray)localObject).getDrawable(0);
        ((TypedArray)localObject).recycle();
        localObject = localDrawable;
        if (localDrawable == null)
        {
          do
          {
            j = paramXmlPullParser.next();
          } while (j == 4);
          if (j == 2)
          {
            localObject = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
          }
          else
          {
            paramResources = new StringBuilder();
            paramResources.append(paramXmlPullParser.getPositionDescription());
            paramResources.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
            throw new XmlPullParserException(paramResources.toString());
          }
        }
        localAnimationScaleListState.addDrawable((Drawable)localObject);
      }
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    onStateChange(getState());
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimationScaleListDrawable);
    updateDensity(paramResources);
    localTypedArray.recycle();
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    onStateChange(getState());
  }
  
  public boolean isRunning()
  {
    boolean bool1 = false;
    Drawable localDrawable = getCurrent();
    boolean bool2 = bool1;
    if (localDrawable != null)
    {
      bool2 = bool1;
      if ((localDrawable instanceof Animatable)) {
        bool2 = ((Animatable)localDrawable).isRunning();
      }
    }
    return bool2;
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mAnimationScaleListState.mutate();
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool = super.onStateChange(paramArrayOfInt);
    if ((!selectDrawable(mAnimationScaleListState.getCurrentDrawableIndexBasedOnScale())) && (!bool)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected void setConstantState(DrawableContainer.DrawableContainerState paramDrawableContainerState)
  {
    super.setConstantState(paramDrawableContainerState);
    if ((paramDrawableContainerState instanceof AnimationScaleListState)) {
      mAnimationScaleListState = ((AnimationScaleListState)paramDrawableContainerState);
    }
  }
  
  public void start()
  {
    Drawable localDrawable = getCurrent();
    if ((localDrawable != null) && ((localDrawable instanceof Animatable))) {
      ((Animatable)localDrawable).start();
    }
  }
  
  public void stop()
  {
    Drawable localDrawable = getCurrent();
    if ((localDrawable != null) && ((localDrawable instanceof Animatable))) {
      ((Animatable)localDrawable).stop();
    }
  }
  
  static class AnimationScaleListState
    extends DrawableContainer.DrawableContainerState
  {
    int mAnimatableDrawableIndex = -1;
    int mStaticDrawableIndex = -1;
    int[] mThemeAttrs = null;
    
    AnimationScaleListState(AnimationScaleListState paramAnimationScaleListState, AnimationScaleListDrawable paramAnimationScaleListDrawable, Resources paramResources)
    {
      super(paramAnimationScaleListDrawable, paramResources);
      if (paramAnimationScaleListState != null)
      {
        mThemeAttrs = mThemeAttrs;
        mStaticDrawableIndex = mStaticDrawableIndex;
        mAnimatableDrawableIndex = mAnimatableDrawableIndex;
      }
    }
    
    int addDrawable(Drawable paramDrawable)
    {
      int i = addChild(paramDrawable);
      if ((paramDrawable instanceof Animatable)) {
        mAnimatableDrawableIndex = i;
      } else {
        mStaticDrawableIndex = i;
      }
      return i;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int getCurrentDrawableIndexBasedOnScale()
    {
      if (ValueAnimator.getDurationScale() == 0.0F) {
        return mStaticDrawableIndex;
      }
      return mAnimatableDrawableIndex;
    }
    
    void mutate()
    {
      int[] arrayOfInt;
      if (mThemeAttrs != null) {
        arrayOfInt = (int[])mThemeAttrs.clone();
      } else {
        arrayOfInt = null;
      }
      mThemeAttrs = arrayOfInt;
    }
    
    public Drawable newDrawable()
    {
      return new AnimationScaleListDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AnimationScaleListDrawable(this, paramResources, null);
    }
  }
}
