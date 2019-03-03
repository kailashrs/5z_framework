package android.view.animation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.List;

public class AnimationSet
  extends Animation
{
  private static final int PROPERTY_CHANGE_BOUNDS_MASK = 128;
  private static final int PROPERTY_DURATION_MASK = 32;
  private static final int PROPERTY_FILL_AFTER_MASK = 1;
  private static final int PROPERTY_FILL_BEFORE_MASK = 2;
  private static final int PROPERTY_MORPH_MATRIX_MASK = 64;
  private static final int PROPERTY_REPEAT_MODE_MASK = 4;
  private static final int PROPERTY_SHARE_INTERPOLATOR_MASK = 16;
  private static final int PROPERTY_START_OFFSET_MASK = 8;
  private ArrayList<Animation> mAnimations = new ArrayList();
  private boolean mDirty;
  private int mFlags = 0;
  private boolean mHasAlpha;
  private long mLastEnd;
  private long[] mStoredOffsets;
  private Transformation mTempTransformation = new Transformation();
  
  public AnimationSet(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AnimationSet);
    setFlag(16, paramAttributeSet.getBoolean(1, true));
    init();
    if (getApplicationInfotargetSdkVersion >= 14)
    {
      if (paramAttributeSet.hasValue(0)) {
        mFlags |= 0x20;
      }
      if (paramAttributeSet.hasValue(2)) {
        mFlags = (0x2 | mFlags);
      }
      if (paramAttributeSet.hasValue(3)) {
        mFlags |= 0x1;
      }
      if (paramAttributeSet.hasValue(5)) {
        mFlags |= 0x4;
      }
      if (paramAttributeSet.hasValue(4)) {
        mFlags |= 0x8;
      }
    }
    paramAttributeSet.recycle();
  }
  
  public AnimationSet(boolean paramBoolean)
  {
    setFlag(16, paramBoolean);
    init();
  }
  
  private void init()
  {
    mStartTime = 0L;
  }
  
  private void setFlag(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= paramInt;
    } else {
      mFlags &= paramInt;
    }
  }
  
  public void addAnimation(Animation paramAnimation)
  {
    mAnimations.add(paramAnimation);
    int i = mFlags;
    int j = 0;
    if ((i & 0x40) == 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) && (paramAnimation.willChangeTransformationMatrix())) {
      mFlags |= 0x40;
    }
    i = j;
    if ((mFlags & 0x80) == 0) {
      i = 1;
    }
    if ((i != 0) && (paramAnimation.willChangeBounds())) {
      mFlags |= 0x80;
    }
    if ((mFlags & 0x20) == 32)
    {
      mLastEnd = (mStartOffset + mDuration);
    }
    else if (mAnimations.size() == 1)
    {
      mDuration = (paramAnimation.getStartOffset() + paramAnimation.getDuration());
      mLastEnd = (mStartOffset + mDuration);
    }
    else
    {
      mLastEnd = Math.max(mLastEnd, mStartOffset + paramAnimation.getStartOffset() + paramAnimation.getDuration());
      mDuration = (mLastEnd - mStartOffset);
    }
    mDirty = true;
  }
  
  protected AnimationSet clone()
    throws CloneNotSupportedException
  {
    AnimationSet localAnimationSet = (AnimationSet)super.clone();
    mTempTransformation = new Transformation();
    mAnimations = new ArrayList();
    int i = mAnimations.size();
    ArrayList localArrayList = mAnimations;
    for (int j = 0; j < i; j++) {
      mAnimations.add(((Animation)localArrayList.get(j)).clone());
    }
    return localAnimationSet;
  }
  
  public long computeDurationHint()
  {
    long l1 = 0L;
    int i = mAnimations.size();
    ArrayList localArrayList = mAnimations;
    i--;
    while (i >= 0)
    {
      long l2 = ((Animation)localArrayList.get(i)).computeDurationHint();
      long l3 = l1;
      if (l2 > l1) {
        l3 = l2;
      }
      i--;
      l1 = l3;
    }
    return l1;
  }
  
  public List<Animation> getAnimations()
  {
    return mAnimations;
  }
  
  public long getDuration()
  {
    ArrayList localArrayList = mAnimations;
    int i = localArrayList.size();
    long l1 = 0L;
    int j = mFlags;
    int k = 0;
    if ((j & 0x20) == 32) {
      j = 1;
    } else {
      j = 0;
    }
    long l2;
    if (j != 0) {
      l2 = mDuration;
    } else {
      for (j = k;; j++)
      {
        l2 = l1;
        if (j >= i) {
          break;
        }
        l1 = Math.max(l1, ((Animation)localArrayList.get(j)).getDuration());
      }
    }
    return l2;
  }
  
  public long getStartTime()
  {
    long l = Long.MAX_VALUE;
    int i = mAnimations.size();
    ArrayList localArrayList = mAnimations;
    for (int j = 0; j < i; j++) {
      l = Math.min(l, ((Animation)localArrayList.get(j)).getStartTime());
    }
    return l;
  }
  
  public boolean getTransformation(long paramLong, Transformation paramTransformation)
  {
    int i = mAnimations.size();
    ArrayList localArrayList = mAnimations;
    Transformation localTransformation = mTempTransformation;
    boolean bool1 = false;
    int j = 0;
    boolean bool2 = true;
    paramTransformation.clear();
    i--;
    while (i >= 0)
    {
      Animation localAnimation = (Animation)localArrayList.get(i);
      localTransformation.clear();
      boolean bool3 = localAnimation.getTransformation(paramLong, localTransformation, getScaleFactor());
      boolean bool4 = false;
      if ((!bool3) && (!bool1)) {
        bool3 = false;
      } else {
        bool3 = true;
      }
      bool1 = bool3;
      paramTransformation.compose(localTransformation);
      if ((j == 0) && (!localAnimation.hasStarted())) {
        j = 0;
      } else {
        j = 1;
      }
      bool3 = bool4;
      if (localAnimation.hasEnded())
      {
        bool3 = bool4;
        if (bool2) {
          bool3 = true;
        }
      }
      i--;
      bool2 = bool3;
    }
    if ((j != 0) && (!mStarted))
    {
      if (mListener != null) {
        mListener.onAnimationStart(this);
      }
      mStarted = true;
    }
    if (bool2 != mEnded)
    {
      if (mListener != null) {
        mListener.onAnimationEnd(this);
      }
      mEnded = bool2;
    }
    return bool1;
  }
  
  public boolean hasAlpha()
  {
    if (mDirty)
    {
      int i = 0;
      mHasAlpha = false;
      mDirty = false;
      int j = mAnimations.size();
      ArrayList localArrayList = mAnimations;
      while (i < j)
      {
        if (((Animation)localArrayList.get(i)).hasAlpha())
        {
          mHasAlpha = true;
          break;
        }
        i++;
      }
    }
    return mHasAlpha;
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = mFlags;
    int j = 1;
    if ((i & 0x20) == 32) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if ((mFlags & 0x1) == 1) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((mFlags & 0x2) == 2) {
      m = 1;
    } else {
      m = 0;
    }
    int n;
    if ((mFlags & 0x4) == 4) {
      n = 1;
    } else {
      n = 0;
    }
    int i1;
    if ((mFlags & 0x10) == 16) {
      i1 = 1;
    } else {
      i1 = 0;
    }
    if ((mFlags & 0x8) != 8) {
      j = 0;
    }
    if (i1 != 0) {
      ensureInterpolator();
    }
    ArrayList localArrayList = mAnimations;
    int i2 = localArrayList.size();
    long l1 = mDuration;
    boolean bool1 = mFillAfter;
    boolean bool2 = mFillBefore;
    int i3 = mRepeatMode;
    Interpolator localInterpolator = mInterpolator;
    long l2 = mStartOffset;
    Object localObject1 = mStoredOffsets;
    Object localObject2;
    if (j != 0)
    {
      if (localObject1 != null)
      {
        localObject2 = localObject1;
        if (localObject1.length == i2) {}
      }
      else
      {
        localObject2 = new long[i2];
        mStoredOffsets = ((long[])localObject2);
      }
    }
    else
    {
      localObject2 = localObject1;
      if (localObject1 != null)
      {
        mStoredOffsets = null;
        localObject2 = null;
      }
    }
    for (int i4 = 0; i4 < i2; i4++)
    {
      localObject1 = (Animation)localArrayList.get(i4);
      if (i != 0) {
        ((Animation)localObject1).setDuration(l1);
      }
      if (k != 0) {
        ((Animation)localObject1).setFillAfter(bool1);
      }
      if (m != 0) {
        ((Animation)localObject1).setFillBefore(bool2);
      }
      if (n != 0) {
        ((Animation)localObject1).setRepeatMode(i3);
      }
      if (i1 != 0) {
        ((Animation)localObject1).setInterpolator(localInterpolator);
      }
      if (j != 0)
      {
        long l3 = ((Animation)localObject1).getStartOffset();
        ((Animation)localObject1).setStartOffset(l3 + l2);
        localObject2[i4] = l3;
      }
      ((Animation)localObject1).initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void initializeInvalidateRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = mPreviousRegion;
    ((RectF)localObject).set(paramInt1, paramInt2, paramInt3, paramInt4);
    ((RectF)localObject).inset(-1.0F, -1.0F);
    if (mFillBefore)
    {
      paramInt1 = mAnimations.size();
      ArrayList localArrayList = mAnimations;
      Transformation localTransformation1 = mTempTransformation;
      Transformation localTransformation2 = mPreviousTransformation;
      paramInt1--;
      while (paramInt1 >= 0)
      {
        Animation localAnimation = (Animation)localArrayList.get(paramInt1);
        if ((!localAnimation.isFillEnabled()) || (localAnimation.getFillBefore()) || (localAnimation.getStartOffset() == 0L))
        {
          localTransformation1.clear();
          localObject = mInterpolator;
          float f = 0.0F;
          if (localObject != null) {
            f = ((Interpolator)localObject).getInterpolation(0.0F);
          }
          localAnimation.applyTransformation(f, localTransformation1);
          localTransformation2.compose(localTransformation1);
        }
        paramInt1--;
      }
    }
  }
  
  public void reset()
  {
    super.reset();
    restoreChildrenStartOffset();
  }
  
  void restoreChildrenStartOffset()
  {
    long[] arrayOfLong = mStoredOffsets;
    if (arrayOfLong == null) {
      return;
    }
    ArrayList localArrayList = mAnimations;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((Animation)localArrayList.get(j)).setStartOffset(arrayOfLong[j]);
    }
  }
  
  public void restrictDuration(long paramLong)
  {
    super.restrictDuration(paramLong);
    ArrayList localArrayList = mAnimations;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((Animation)localArrayList.get(j)).restrictDuration(paramLong);
    }
  }
  
  public void scaleCurrentDuration(float paramFloat)
  {
    ArrayList localArrayList = mAnimations;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((Animation)localArrayList.get(j)).scaleCurrentDuration(paramFloat);
    }
  }
  
  public void setDuration(long paramLong)
  {
    mFlags |= 0x20;
    super.setDuration(paramLong);
    mLastEnd = (mStartOffset + mDuration);
  }
  
  public void setFillAfter(boolean paramBoolean)
  {
    mFlags |= 0x1;
    super.setFillAfter(paramBoolean);
  }
  
  public void setFillBefore(boolean paramBoolean)
  {
    mFlags |= 0x2;
    super.setFillBefore(paramBoolean);
  }
  
  public void setRepeatMode(int paramInt)
  {
    mFlags |= 0x4;
    super.setRepeatMode(paramInt);
  }
  
  public void setStartOffset(long paramLong)
  {
    mFlags |= 0x8;
    super.setStartOffset(paramLong);
  }
  
  public void setStartTime(long paramLong)
  {
    super.setStartTime(paramLong);
    int i = mAnimations.size();
    ArrayList localArrayList = mAnimations;
    for (int j = 0; j < i; j++) {
      ((Animation)localArrayList.get(j)).setStartTime(paramLong);
    }
  }
  
  public boolean willChangeBounds()
  {
    boolean bool;
    if ((mFlags & 0x80) == 128) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean willChangeTransformationMatrix()
  {
    boolean bool;
    if ((mFlags & 0x40) == 64) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
