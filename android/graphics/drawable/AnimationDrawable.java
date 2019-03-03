package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationDrawable
  extends DrawableContainer
  implements Runnable, Animatable
{
  private boolean mAnimating;
  private AnimationState mAnimationState;
  private int mCurFrame = 0;
  private boolean mMutated;
  private boolean mRunning;
  
  public AnimationDrawable()
  {
    this(null, null);
  }
  
  private AnimationDrawable(AnimationState paramAnimationState, Resources paramResources)
  {
    setConstantState(new AnimationState(paramAnimationState, this, paramResources));
    if (paramAnimationState != null) {
      setFrame(0, true, false);
    }
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        return;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        return;
      }
      if ((j == 2) && (k <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimationDrawableItem);
        k = ((TypedArray)localObject).getInt(0, -1);
        if (k < 0) {
          break;
        }
        Drawable localDrawable = ((TypedArray)localObject).getDrawable(1);
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
        mAnimationState.addFrame((Drawable)localObject, k);
        if (localObject != null) {
          ((Drawable)localObject).setCallback(this);
        }
      }
    }
    paramResources = new StringBuilder();
    paramResources.append(paramXmlPullParser.getPositionDescription());
    paramResources.append(": <item> tag requires a 'duration' attribute");
    throw new XmlPullParserException(paramResources.toString());
  }
  
  private void nextFrame(boolean paramBoolean)
  {
    int i = mCurFrame;
    boolean bool = true;
    int j = i + 1;
    int k = mAnimationState.getChildCount();
    if ((mAnimationState.mOneShot) && (j >= k - 1)) {
      i = 1;
    } else {
      i = 0;
    }
    int m = j;
    if (!mAnimationState.mOneShot)
    {
      m = j;
      if (j >= k) {
        m = 0;
      }
    }
    if (i != 0) {
      bool = false;
    }
    setFrame(m, paramBoolean, bool);
  }
  
  private void setFrame(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramInt >= mAnimationState.getChildCount()) {
      return;
    }
    mAnimating = paramBoolean2;
    mCurFrame = paramInt;
    selectDrawable(paramInt);
    if ((paramBoolean1) || (paramBoolean2)) {
      unscheduleSelf(this);
    }
    if (paramBoolean2)
    {
      mCurFrame = paramInt;
      mRunning = true;
      scheduleSelf(this, SystemClock.uptimeMillis() + mAnimationState.mDurations[paramInt]);
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    mAnimationState.mVariablePadding = paramTypedArray.getBoolean(1, mAnimationState.mVariablePadding);
    AnimationState.access$002(mAnimationState, paramTypedArray.getBoolean(2, mAnimationState.mOneShot));
  }
  
  public void addFrame(Drawable paramDrawable, int paramInt)
  {
    mAnimationState.addFrame(paramDrawable, paramInt);
    if (!mRunning) {
      setFrame(0, true, false);
    }
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  AnimationState cloneConstantState()
  {
    return new AnimationState(mAnimationState, this, null);
  }
  
  public int getDuration(int paramInt)
  {
    return mAnimationState.mDurations[paramInt];
  }
  
  public Drawable getFrame(int paramInt)
  {
    return mAnimationState.getChild(paramInt);
  }
  
  public int getNumberOfFrames()
  {
    return mAnimationState.getChildCount();
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimationDrawable);
    super.inflateWithAttributes(paramResources, paramXmlPullParser, localTypedArray, 0);
    updateStateFromTypedArray(localTypedArray);
    updateDensity(paramResources);
    localTypedArray.recycle();
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    setFrame(0, true, false);
  }
  
  public boolean isOneShot()
  {
    return mAnimationState.mOneShot;
  }
  
  public boolean isRunning()
  {
    return mRunning;
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mAnimationState.mutate();
      mMutated = true;
    }
    return this;
  }
  
  public void run()
  {
    nextFrame(false);
  }
  
  protected void setConstantState(DrawableContainer.DrawableContainerState paramDrawableContainerState)
  {
    super.setConstantState(paramDrawableContainerState);
    if ((paramDrawableContainerState instanceof AnimationState)) {
      mAnimationState = ((AnimationState)paramDrawableContainerState);
    }
  }
  
  public void setOneShot(boolean paramBoolean)
  {
    AnimationState.access$002(mAnimationState, paramBoolean);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if (paramBoolean1)
    {
      if ((paramBoolean2) || (bool))
      {
        int i = 0;
        int j;
        if ((!paramBoolean2) && ((mRunning) || (mAnimationState.mOneShot)) && (mCurFrame < mAnimationState.getChildCount())) {
          j = 0;
        } else {
          j = 1;
        }
        if (j != 0) {
          j = i;
        } else {
          j = mCurFrame;
        }
        setFrame(j, true, mAnimating);
      }
    }
    else {
      unscheduleSelf(this);
    }
    return bool;
  }
  
  public void start()
  {
    boolean bool = true;
    mAnimating = true;
    if (!isRunning())
    {
      if ((mAnimationState.getChildCount() <= 1) && (mAnimationState.mOneShot)) {
        bool = false;
      }
      setFrame(0, false, bool);
    }
  }
  
  public void stop()
  {
    mAnimating = false;
    if (isRunning())
    {
      mCurFrame = 0;
      unscheduleSelf(this);
    }
  }
  
  public void unscheduleSelf(Runnable paramRunnable)
  {
    mRunning = false;
    super.unscheduleSelf(paramRunnable);
  }
  
  private static final class AnimationState
    extends DrawableContainer.DrawableContainerState
  {
    private int[] mDurations;
    private boolean mOneShot = false;
    
    AnimationState(AnimationState paramAnimationState, AnimationDrawable paramAnimationDrawable, Resources paramResources)
    {
      super(paramAnimationDrawable, paramResources);
      if (paramAnimationState != null)
      {
        mDurations = mDurations;
        mOneShot = mOneShot;
      }
      else
      {
        mDurations = new int[getCapacity()];
        mOneShot = false;
      }
    }
    
    private void mutate()
    {
      mDurations = ((int[])mDurations.clone());
    }
    
    public void addFrame(Drawable paramDrawable, int paramInt)
    {
      int i = super.addChild(paramDrawable);
      mDurations[i] = paramInt;
    }
    
    public void growArray(int paramInt1, int paramInt2)
    {
      super.growArray(paramInt1, paramInt2);
      int[] arrayOfInt = new int[paramInt2];
      System.arraycopy(mDurations, 0, arrayOfInt, 0, paramInt1);
      mDurations = arrayOfInt;
    }
    
    public Drawable newDrawable()
    {
      return new AnimationDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AnimationDrawable(this, paramResources, null);
    }
  }
}
