package android.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LongSparseLongArray;
import android.util.SparseIntArray;
import android.util.StateSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawable
  extends StateListDrawable
{
  private static final String ELEMENT_ITEM = "item";
  private static final String ELEMENT_TRANSITION = "transition";
  private static final String LOGTAG = AnimatedStateListDrawable.class.getSimpleName();
  private boolean mMutated;
  private AnimatedStateListState mState;
  private Transition mTransition;
  private int mTransitionFromIndex = -1;
  private int mTransitionToIndex = -1;
  
  public AnimatedStateListDrawable()
  {
    this(null, null);
  }
  
  private AnimatedStateListDrawable(AnimatedStateListState paramAnimatedStateListState, Resources paramResources)
  {
    super(null);
    setConstantState(new AnimatedStateListState(paramAnimatedStateListState, this, paramResources));
    onStateChange(getState());
    jumpToCurrentState();
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
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
      if ((j == 2) && (k <= i)) {
        if (paramXmlPullParser.getName().equals("item")) {
          parseItem(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        } else if (paramXmlPullParser.getName().equals("transition")) {
          parseTransition(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        }
      }
    }
  }
  
  private void init()
  {
    onStateChange(getState());
  }
  
  private int parseItem(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawableItem);
    int i = ((TypedArray)localObject).getResourceId(0, 0);
    Drawable localDrawable = ((TypedArray)localObject).getDrawable(1);
    ((TypedArray)localObject).recycle();
    int[] arrayOfInt = extractStateSet(paramAttributeSet);
    localObject = localDrawable;
    if (localDrawable == null)
    {
      int j;
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
    return mState.addStateSet(arrayOfInt, (Drawable)localObject, i);
  }
  
  private int parseTransition(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawableTransition);
    int i = ((TypedArray)localObject).getResourceId(2, 0);
    int j = ((TypedArray)localObject).getResourceId(1, 0);
    boolean bool = ((TypedArray)localObject).getBoolean(3, false);
    Drawable localDrawable = ((TypedArray)localObject).getDrawable(0);
    ((TypedArray)localObject).recycle();
    localObject = localDrawable;
    if (localDrawable == null)
    {
      int k;
      do
      {
        k = paramXmlPullParser.next();
      } while (k == 4);
      if (k == 2)
      {
        localObject = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      }
      else
      {
        paramResources = new StringBuilder();
        paramResources.append(paramXmlPullParser.getPositionDescription());
        paramResources.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
        throw new XmlPullParserException(paramResources.toString());
      }
    }
    return mState.addTransition(i, j, (Drawable)localObject, bool);
  }
  
  private boolean selectTransition(int paramInt)
  {
    Object localObject = mTransition;
    int i;
    if (localObject != null)
    {
      if (paramInt == mTransitionToIndex) {
        return true;
      }
      if ((paramInt == mTransitionFromIndex) && (((Transition)localObject).canReverse()))
      {
        ((Transition)localObject).reverse();
        mTransitionToIndex = mTransitionFromIndex;
        mTransitionFromIndex = paramInt;
        return true;
      }
      i = mTransitionToIndex;
      ((Transition)localObject).stop();
    }
    else
    {
      i = getCurrentIndex();
    }
    mTransition = null;
    mTransitionFromIndex = -1;
    mTransitionToIndex = -1;
    localObject = mState;
    int j = ((AnimatedStateListState)localObject).getKeyframeIdAt(i);
    int k = ((AnimatedStateListState)localObject).getKeyframeIdAt(paramInt);
    if ((k != 0) && (j != 0))
    {
      int m = ((AnimatedStateListState)localObject).indexOfTransition(j, k);
      if (m < 0) {
        return false;
      }
      boolean bool1 = ((AnimatedStateListState)localObject).transitionHasReversibleFlag(j, k);
      selectDrawable(m);
      Drawable localDrawable = getCurrent();
      boolean bool2;
      if ((localDrawable instanceof AnimationDrawable))
      {
        bool2 = ((AnimatedStateListState)localObject).isTransitionReversed(j, k);
        localObject = new AnimationDrawableTransition((AnimationDrawable)localDrawable, bool2, bool1);
      }
      else if ((localDrawable instanceof AnimatedVectorDrawable))
      {
        bool2 = ((AnimatedStateListState)localObject).isTransitionReversed(j, k);
        localObject = new AnimatedVectorDrawableTransition((AnimatedVectorDrawable)localDrawable, bool2, bool1);
      }
      else
      {
        if (!(localDrawable instanceof Animatable)) {
          break label275;
        }
        localObject = new AnimatableTransition((Animatable)localDrawable);
      }
      ((Transition)localObject).start();
      mTransition = ((Transition)localObject);
      mTransitionFromIndex = i;
      mTransitionToIndex = paramInt;
      return true;
      label275:
      return false;
    }
    return false;
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    AnimatedStateListState localAnimatedStateListState = mState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mAnimThemeAttrs = paramTypedArray.extractThemeAttrs();
    localAnimatedStateListState.setVariablePadding(paramTypedArray.getBoolean(2, mVariablePadding));
    localAnimatedStateListState.setConstantSize(paramTypedArray.getBoolean(3, mConstantSize));
    localAnimatedStateListState.setEnterFadeDuration(paramTypedArray.getInt(4, mEnterFadeDuration));
    localAnimatedStateListState.setExitFadeDuration(paramTypedArray.getInt(5, mExitFadeDuration));
    setDither(paramTypedArray.getBoolean(0, mDither));
    setAutoMirrored(paramTypedArray.getBoolean(6, mAutoMirrored));
  }
  
  public void addState(int[] paramArrayOfInt, Drawable paramDrawable, int paramInt)
  {
    if (paramDrawable != null)
    {
      mState.addStateSet(paramArrayOfInt, paramDrawable, paramInt);
      onStateChange(getState());
      return;
    }
    throw new IllegalArgumentException("Drawable must not be null");
  }
  
  public <T extends Drawable,  extends Animatable> void addTransition(int paramInt1, int paramInt2, T paramT, boolean paramBoolean)
  {
    if (paramT != null)
    {
      mState.addTransition(paramInt1, paramInt2, paramT, paramBoolean);
      return;
    }
    throw new IllegalArgumentException("Transition drawable must not be null");
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    AnimatedStateListState localAnimatedStateListState = mState;
    if ((localAnimatedStateListState != null) && (mAnimThemeAttrs != null))
    {
      paramTheme = paramTheme.resolveAttributes(mAnimThemeAttrs, R.styleable.AnimatedRotateDrawable);
      updateStateFromTypedArray(paramTheme);
      paramTheme.recycle();
      init();
      return;
    }
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  AnimatedStateListState cloneConstantState()
  {
    return new AnimatedStateListState(mState, this, null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedStateListDrawable);
    super.inflateWithAttributes(paramResources, paramXmlPullParser, localTypedArray, 1);
    updateStateFromTypedArray(localTypedArray);
    updateDensity(paramResources);
    localTypedArray.recycle();
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    init();
  }
  
  public boolean isStateful()
  {
    return true;
  }
  
  public void jumpToCurrentState()
  {
    super.jumpToCurrentState();
    if (mTransition != null)
    {
      mTransition.stop();
      mTransition = null;
      selectDrawable(mTransitionToIndex);
      mTransitionToIndex = -1;
      mTransitionFromIndex = -1;
    }
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mState.mutate();
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    int i = mState.indexOfKeyframe(paramArrayOfInt);
    boolean bool1;
    if ((i != getCurrentIndex()) && ((selectTransition(i)) || (selectDrawable(i)))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    Drawable localDrawable = getCurrent();
    boolean bool2 = bool1;
    if (localDrawable != null) {
      bool2 = bool1 | localDrawable.setState(paramArrayOfInt);
    }
    return bool2;
  }
  
  protected void setConstantState(DrawableContainer.DrawableContainerState paramDrawableContainerState)
  {
    super.setConstantState(paramDrawableContainerState);
    if ((paramDrawableContainerState instanceof AnimatedStateListState)) {
      mState = ((AnimatedStateListState)paramDrawableContainerState);
    }
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if ((mTransition != null) && ((bool) || (paramBoolean2))) {
      if (paramBoolean1) {
        mTransition.start();
      } else {
        jumpToCurrentState();
      }
    }
    return bool;
  }
  
  private static class AnimatableTransition
    extends AnimatedStateListDrawable.Transition
  {
    private final Animatable mA;
    
    public AnimatableTransition(Animatable paramAnimatable)
    {
      super();
      mA = paramAnimatable;
    }
    
    public void start()
    {
      mA.start();
    }
    
    public void stop()
    {
      mA.stop();
    }
  }
  
  static class AnimatedStateListState
    extends StateListDrawable.StateListState
  {
    private static final long REVERSED_BIT = 4294967296L;
    private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
    int[] mAnimThemeAttrs;
    SparseIntArray mStateIds;
    LongSparseLongArray mTransitions;
    
    AnimatedStateListState(AnimatedStateListState paramAnimatedStateListState, AnimatedStateListDrawable paramAnimatedStateListDrawable, Resources paramResources)
    {
      super(paramAnimatedStateListDrawable, paramResources);
      if (paramAnimatedStateListState != null)
      {
        mAnimThemeAttrs = mAnimThemeAttrs;
        mTransitions = mTransitions;
        mStateIds = mStateIds;
      }
      else
      {
        mTransitions = new LongSparseLongArray();
        mStateIds = new SparseIntArray();
      }
    }
    
    private static long generateTransitionKey(int paramInt1, int paramInt2)
    {
      return paramInt1 << 32 | paramInt2;
    }
    
    int addStateSet(int[] paramArrayOfInt, Drawable paramDrawable, int paramInt)
    {
      int i = super.addStateSet(paramArrayOfInt, paramDrawable);
      mStateIds.put(i, paramInt);
      return i;
    }
    
    int addTransition(int paramInt1, int paramInt2, Drawable paramDrawable, boolean paramBoolean)
    {
      int i = super.addChild(paramDrawable);
      long l1 = generateTransitionKey(paramInt1, paramInt2);
      long l2 = 0L;
      if (paramBoolean) {
        l2 = 8589934592L;
      }
      mTransitions.append(l1, i | l2);
      if (paramBoolean)
      {
        l1 = generateTransitionKey(paramInt2, paramInt1);
        mTransitions.append(l1, i | 0x100000000 | l2);
      }
      return i;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mAnimThemeAttrs == null) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    int getKeyframeIdAt(int paramInt)
    {
      int i = 0;
      if (paramInt < 0) {
        paramInt = i;
      } else {
        paramInt = mStateIds.get(paramInt, 0);
      }
      return paramInt;
    }
    
    int indexOfKeyframe(int[] paramArrayOfInt)
    {
      int i = super.indexOfStateSet(paramArrayOfInt);
      if (i >= 0) {
        return i;
      }
      return super.indexOfStateSet(StateSet.WILD_CARD);
    }
    
    int indexOfTransition(int paramInt1, int paramInt2)
    {
      long l = generateTransitionKey(paramInt1, paramInt2);
      return (int)mTransitions.get(l, -1L);
    }
    
    boolean isTransitionReversed(int paramInt1, int paramInt2)
    {
      long l = generateTransitionKey(paramInt1, paramInt2);
      boolean bool;
      if ((mTransitions.get(l, -1L) & 0x100000000) != 0L) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void mutate()
    {
      mTransitions = mTransitions.clone();
      mStateIds = mStateIds.clone();
    }
    
    public Drawable newDrawable()
    {
      return new AnimatedStateListDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AnimatedStateListDrawable(this, paramResources, null);
    }
    
    boolean transitionHasReversibleFlag(int paramInt1, int paramInt2)
    {
      long l = generateTransitionKey(paramInt1, paramInt2);
      boolean bool;
      if ((mTransitions.get(l, -1L) & 0x200000000) != 0L) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  private static class AnimatedVectorDrawableTransition
    extends AnimatedStateListDrawable.Transition
  {
    private final AnimatedVectorDrawable mAvd;
    private final boolean mHasReversibleFlag;
    private final boolean mReversed;
    
    public AnimatedVectorDrawableTransition(AnimatedVectorDrawable paramAnimatedVectorDrawable, boolean paramBoolean1, boolean paramBoolean2)
    {
      super();
      mAvd = paramAnimatedVectorDrawable;
      mReversed = paramBoolean1;
      mHasReversibleFlag = paramBoolean2;
    }
    
    public boolean canReverse()
    {
      boolean bool;
      if ((mAvd.canReverse()) && (mHasReversibleFlag)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void reverse()
    {
      if (canReverse()) {
        mAvd.reverse();
      } else {
        Log.w(AnimatedStateListDrawable.LOGTAG, "Can't reverse, either the reversible is set to false, or the AnimatedVectorDrawable can't reverse");
      }
    }
    
    public void start()
    {
      if (mReversed) {
        reverse();
      } else {
        mAvd.start();
      }
    }
    
    public void stop()
    {
      mAvd.stop();
    }
  }
  
  private static class AnimationDrawableTransition
    extends AnimatedStateListDrawable.Transition
  {
    private final ObjectAnimator mAnim;
    private final boolean mHasReversibleFlag;
    
    public AnimationDrawableTransition(AnimationDrawable paramAnimationDrawable, boolean paramBoolean1, boolean paramBoolean2)
    {
      super();
      int i = paramAnimationDrawable.getNumberOfFrames();
      int j;
      if (paramBoolean1) {
        j = i - 1;
      } else {
        j = 0;
      }
      if (paramBoolean1) {
        i = 0;
      } else {
        i--;
      }
      AnimatedStateListDrawable.FrameInterpolator localFrameInterpolator = new AnimatedStateListDrawable.FrameInterpolator(paramAnimationDrawable, paramBoolean1);
      paramAnimationDrawable = ObjectAnimator.ofInt(paramAnimationDrawable, "currentIndex", new int[] { j, i });
      paramAnimationDrawable.setAutoCancel(true);
      paramAnimationDrawable.setDuration(localFrameInterpolator.getTotalDuration());
      paramAnimationDrawable.setInterpolator(localFrameInterpolator);
      mHasReversibleFlag = paramBoolean2;
      mAnim = paramAnimationDrawable;
    }
    
    public boolean canReverse()
    {
      return mHasReversibleFlag;
    }
    
    public void reverse()
    {
      mAnim.reverse();
    }
    
    public void start()
    {
      mAnim.start();
    }
    
    public void stop()
    {
      mAnim.cancel();
    }
  }
  
  private static class FrameInterpolator
    implements TimeInterpolator
  {
    private int[] mFrameTimes;
    private int mFrames;
    private int mTotalDuration;
    
    public FrameInterpolator(AnimationDrawable paramAnimationDrawable, boolean paramBoolean)
    {
      updateFrames(paramAnimationDrawable, paramBoolean);
    }
    
    public float getInterpolation(float paramFloat)
    {
      int i = (int)(mTotalDuration * paramFloat + 0.5F);
      int j = mFrames;
      int[] arrayOfInt = mFrameTimes;
      for (int k = 0; (k < j) && (i >= arrayOfInt[k]); k++) {
        i -= arrayOfInt[k];
      }
      if (k < j) {
        paramFloat = i / mTotalDuration;
      } else {
        paramFloat = 0.0F;
      }
      return k / j + paramFloat;
    }
    
    public int getTotalDuration()
    {
      return mTotalDuration;
    }
    
    public int updateFrames(AnimationDrawable paramAnimationDrawable, boolean paramBoolean)
    {
      int i = paramAnimationDrawable.getNumberOfFrames();
      mFrames = i;
      if ((mFrameTimes == null) || (mFrameTimes.length < i)) {
        mFrameTimes = new int[i];
      }
      int[] arrayOfInt = mFrameTimes;
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        if (paramBoolean) {
          m = i - k - 1;
        } else {
          m = k;
        }
        int m = paramAnimationDrawable.getDuration(m);
        arrayOfInt[k] = m;
        j += m;
      }
      mTotalDuration = j;
      return j;
    }
  }
  
  private static abstract class Transition
  {
    private Transition() {}
    
    public boolean canReverse()
    {
      return false;
    }
    
    public void reverse() {}
    
    public abstract void start();
    
    public abstract void stop();
  }
}
