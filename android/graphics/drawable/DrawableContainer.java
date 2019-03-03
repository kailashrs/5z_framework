package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.SparseArray;

public class DrawableContainer
  extends Drawable
  implements Drawable.Callback
{
  private static final boolean DEBUG = false;
  private static final boolean DEFAULT_DITHER = true;
  private static final String TAG = "DrawableContainer";
  private int mAlpha = 255;
  private Runnable mAnimationRunnable;
  private BlockInvalidateCallback mBlockInvalidateCallback;
  private int mCurIndex = -1;
  private Drawable mCurrDrawable;
  private DrawableContainerState mDrawableContainerState;
  private long mEnterAnimationEnd;
  private long mExitAnimationEnd;
  private boolean mHasAlpha;
  private Rect mHotspotBounds;
  private Drawable mLastDrawable;
  private int mLastIndex = -1;
  private boolean mMutated;
  
  public DrawableContainer() {}
  
  private void initializeDrawableForDisplay(Drawable paramDrawable)
  {
    if (mBlockInvalidateCallback == null) {
      mBlockInvalidateCallback = new BlockInvalidateCallback(null);
    }
    paramDrawable.setCallback(mBlockInvalidateCallback.wrap(paramDrawable.getCallback()));
    try
    {
      if ((mDrawableContainerState.mEnterFadeDuration <= 0) && (mHasAlpha)) {
        paramDrawable.setAlpha(mAlpha);
      }
      if (mDrawableContainerState.mHasColorFilter)
      {
        paramDrawable.setColorFilter(mDrawableContainerState.mColorFilter);
      }
      else
      {
        if (mDrawableContainerState.mHasTintList) {
          paramDrawable.setTintList(mDrawableContainerState.mTintList);
        }
        if (mDrawableContainerState.mHasTintMode) {
          paramDrawable.setTintMode(mDrawableContainerState.mTintMode);
        }
      }
      paramDrawable.setVisible(isVisible(), true);
      paramDrawable.setDither(mDrawableContainerState.mDither);
      paramDrawable.setState(getState());
      paramDrawable.setLevel(getLevel());
      paramDrawable.setBounds(getBounds());
      paramDrawable.setLayoutDirection(getLayoutDirection());
      paramDrawable.setAutoMirrored(mDrawableContainerState.mAutoMirrored);
      Rect localRect = mHotspotBounds;
      if (localRect != null) {
        paramDrawable.setHotspotBounds(left, top, right, bottom);
      }
      return;
    }
    finally
    {
      paramDrawable.setCallback(mBlockInvalidateCallback.unwrap());
    }
  }
  
  private boolean needsMirroring()
  {
    boolean bool1 = isAutoMirrored();
    boolean bool2 = true;
    if ((!bool1) || (getLayoutDirection() != 1)) {
      bool2 = false;
    }
    return bool2;
  }
  
  void animate(boolean paramBoolean)
  {
    mHasAlpha = true;
    long l = SystemClock.uptimeMillis();
    int i = 0;
    int j;
    if (mCurrDrawable != null)
    {
      j = i;
      if (mEnterAnimationEnd != 0L) {
        if (mEnterAnimationEnd <= l)
        {
          mCurrDrawable.setAlpha(mAlpha);
          mEnterAnimationEnd = 0L;
          j = i;
        }
        else
        {
          j = (int)((mEnterAnimationEnd - l) * 255L) / mDrawableContainerState.mEnterFadeDuration;
          mCurrDrawable.setAlpha((255 - j) * mAlpha / 255);
          j = 1;
        }
      }
    }
    else
    {
      mEnterAnimationEnd = 0L;
      j = i;
    }
    if (mLastDrawable != null)
    {
      i = j;
      if (mExitAnimationEnd != 0L) {
        if (mExitAnimationEnd <= l)
        {
          mLastDrawable.setVisible(false, false);
          mLastDrawable = null;
          mLastIndex = -1;
          mExitAnimationEnd = 0L;
          i = j;
        }
        else
        {
          j = (int)((mExitAnimationEnd - l) * 255L) / mDrawableContainerState.mExitFadeDuration;
          mLastDrawable.setAlpha(mAlpha * j / 255);
          i = 1;
        }
      }
    }
    else
    {
      mExitAnimationEnd = 0L;
      i = j;
    }
    if ((paramBoolean) && (i != 0)) {
      scheduleSelf(mAnimationRunnable, 16L + l);
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    mDrawableContainerState.applyTheme(paramTheme);
  }
  
  public boolean canApplyTheme()
  {
    return mDrawableContainerState.canApplyTheme();
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mDrawableContainerState.clearMutated();
    mMutated = false;
  }
  
  DrawableContainerState cloneConstantState()
  {
    return mDrawableContainerState;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mCurrDrawable != null) {
      mCurrDrawable.draw(paramCanvas);
    }
    if (mLastDrawable != null) {
      mLastDrawable.draw(paramCanvas);
    }
  }
  
  public int getAlpha()
  {
    return mAlpha;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mDrawableContainerState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    if (mDrawableContainerState.canConstantState())
    {
      mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
      return mDrawableContainerState;
    }
    return null;
  }
  
  public Drawable getCurrent()
  {
    return mCurrDrawable;
  }
  
  public int getCurrentIndex()
  {
    return mCurIndex;
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    if (mHotspotBounds != null) {
      paramRect.set(mHotspotBounds);
    } else {
      super.getHotspotBounds(paramRect);
    }
  }
  
  public int getIntrinsicHeight()
  {
    if (mDrawableContainerState.isConstantSize()) {
      return mDrawableContainerState.getConstantHeight();
    }
    int i;
    if (mCurrDrawable != null) {
      i = mCurrDrawable.getIntrinsicHeight();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getIntrinsicWidth()
  {
    if (mDrawableContainerState.isConstantSize()) {
      return mDrawableContainerState.getConstantWidth();
    }
    int i;
    if (mCurrDrawable != null) {
      i = mCurrDrawable.getIntrinsicWidth();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getMinimumHeight()
  {
    if (mDrawableContainerState.isConstantSize()) {
      return mDrawableContainerState.getConstantMinimumHeight();
    }
    int i;
    if (mCurrDrawable != null) {
      i = mCurrDrawable.getMinimumHeight();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getMinimumWidth()
  {
    if (mDrawableContainerState.isConstantSize()) {
      return mDrawableContainerState.getConstantMinimumWidth();
    }
    int i;
    if (mCurrDrawable != null) {
      i = mCurrDrawable.getMinimumWidth();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getOpacity()
  {
    int i;
    if ((mCurrDrawable != null) && (mCurrDrawable.isVisible())) {
      i = mDrawableContainerState.getOpacity();
    } else {
      i = -2;
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    if (mCurrDrawable != null) {
      return mCurrDrawable.getOpticalInsets();
    }
    return Insets.NONE;
  }
  
  public void getOutline(Outline paramOutline)
  {
    if (mCurrDrawable != null) {
      mCurrDrawable.getOutline(paramOutline);
    }
  }
  
  public boolean getPadding(Rect paramRect)
  {
    Rect localRect = mDrawableContainerState.getConstantPadding();
    if (localRect != null)
    {
      paramRect.set(localRect);
      if ((left | top | bottom | right) != 0) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else
    {
      if (mCurrDrawable == null) {
        break label68;
      }
      bool = mCurrDrawable.getPadding(paramRect);
    }
    break label74;
    label68:
    boolean bool = super.getPadding(paramRect);
    label74:
    if (needsMirroring())
    {
      int i = left;
      left = right;
      right = i;
    }
    return bool;
  }
  
  public boolean hasFocusStateSpecified()
  {
    if (mCurrDrawable != null) {
      return mCurrDrawable.hasFocusStateSpecified();
    }
    if (mLastDrawable != null) {
      return mLastDrawable.hasFocusStateSpecified();
    }
    return false;
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (mDrawableContainerState != null) {
      mDrawableContainerState.invalidateCache();
    }
    if ((paramDrawable == mCurrDrawable) && (getCallback() != null)) {
      getCallback().invalidateDrawable(this);
    }
  }
  
  public boolean isAutoMirrored()
  {
    return mDrawableContainerState.mAutoMirrored;
  }
  
  public boolean isStateful()
  {
    return mDrawableContainerState.isStateful();
  }
  
  public void jumpToCurrentState()
  {
    int i = 0;
    if (mLastDrawable != null)
    {
      mLastDrawable.jumpToCurrentState();
      mLastDrawable = null;
      mLastIndex = -1;
      i = 1;
    }
    if (mCurrDrawable != null)
    {
      mCurrDrawable.jumpToCurrentState();
      if (mHasAlpha) {
        mCurrDrawable.setAlpha(mAlpha);
      }
    }
    if (mExitAnimationEnd != 0L)
    {
      mExitAnimationEnd = 0L;
      i = 1;
    }
    if (mEnterAnimationEnd != 0L)
    {
      mEnterAnimationEnd = 0L;
      i = 1;
    }
    if (i != 0) {
      invalidateSelf();
    }
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      DrawableContainerState localDrawableContainerState = cloneConstantState();
      localDrawableContainerState.mutate();
      setConstantState(localDrawableContainerState);
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    if (mLastDrawable != null) {
      mLastDrawable.setBounds(paramRect);
    }
    if (mCurrDrawable != null) {
      mCurrDrawable.setBounds(paramRect);
    }
  }
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    return mDrawableContainerState.setLayoutDirection(paramInt, getCurrentIndex());
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    if (mLastDrawable != null) {
      return mLastDrawable.setLevel(paramInt);
    }
    if (mCurrDrawable != null) {
      return mCurrDrawable.setLevel(paramInt);
    }
    return false;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    if (mLastDrawable != null) {
      return mLastDrawable.setState(paramArrayOfInt);
    }
    if (mCurrDrawable != null) {
      return mCurrDrawable.setState(paramArrayOfInt);
    }
    return false;
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    if ((paramDrawable == mCurrDrawable) && (getCallback() != null)) {
      getCallback().scheduleDrawable(this, paramRunnable, paramLong);
    }
  }
  
  public boolean selectDrawable(int paramInt)
  {
    if (paramInt == mCurIndex) {
      return false;
    }
    long l = SystemClock.uptimeMillis();
    if (mDrawableContainerState.mExitFadeDuration > 0)
    {
      if (mLastDrawable != null) {
        mLastDrawable.setVisible(false, false);
      }
      if (mCurrDrawable != null)
      {
        mLastDrawable = mCurrDrawable;
        mLastIndex = mCurIndex;
        mExitAnimationEnd = (mDrawableContainerState.mExitFadeDuration + l);
      }
      else
      {
        mLastDrawable = null;
        mLastIndex = -1;
        mExitAnimationEnd = 0L;
      }
    }
    else if (mCurrDrawable != null)
    {
      mCurrDrawable.setVisible(false, false);
    }
    if ((paramInt >= 0) && (paramInt < mDrawableContainerState.mNumChildren))
    {
      Drawable localDrawable = mDrawableContainerState.getChild(paramInt);
      mCurrDrawable = localDrawable;
      mCurIndex = paramInt;
      if (localDrawable != null)
      {
        if (mDrawableContainerState.mEnterFadeDuration > 0) {
          mEnterAnimationEnd = (mDrawableContainerState.mEnterFadeDuration + l);
        }
        initializeDrawableForDisplay(localDrawable);
      }
    }
    else
    {
      mCurrDrawable = null;
      mCurIndex = -1;
    }
    if ((mEnterAnimationEnd != 0L) || (mExitAnimationEnd != 0L))
    {
      if (mAnimationRunnable == null) {
        mAnimationRunnable = new Runnable()
        {
          public void run()
          {
            animate(true);
            invalidateSelf();
          }
        };
      } else {
        unscheduleSelf(mAnimationRunnable);
      }
      animate(true);
    }
    invalidateSelf();
    return true;
  }
  
  public void setAlpha(int paramInt)
  {
    if ((!mHasAlpha) || (mAlpha != paramInt))
    {
      mHasAlpha = true;
      mAlpha = paramInt;
      if (mCurrDrawable != null) {
        if (mEnterAnimationEnd == 0L) {
          mCurrDrawable.setAlpha(paramInt);
        } else {
          animate(false);
        }
      }
    }
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (mDrawableContainerState.mAutoMirrored != paramBoolean)
    {
      mDrawableContainerState.mAutoMirrored = paramBoolean;
      if (mCurrDrawable != null) {
        mCurrDrawable.setAutoMirrored(mDrawableContainerState.mAutoMirrored);
      }
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mDrawableContainerState.mHasColorFilter = true;
    if (mDrawableContainerState.mColorFilter != paramColorFilter)
    {
      mDrawableContainerState.mColorFilter = paramColorFilter;
      if (mCurrDrawable != null) {
        mCurrDrawable.setColorFilter(paramColorFilter);
      }
    }
  }
  
  protected void setConstantState(DrawableContainerState paramDrawableContainerState)
  {
    mDrawableContainerState = paramDrawableContainerState;
    if (mCurIndex >= 0)
    {
      mCurrDrawable = paramDrawableContainerState.getChild(mCurIndex);
      if (mCurrDrawable != null) {
        initializeDrawableForDisplay(mCurrDrawable);
      }
    }
    mLastIndex = -1;
    mLastDrawable = null;
  }
  
  public void setCurrentIndex(int paramInt)
  {
    selectDrawable(paramInt);
  }
  
  public void setDither(boolean paramBoolean)
  {
    if (mDrawableContainerState.mDither != paramBoolean)
    {
      mDrawableContainerState.mDither = paramBoolean;
      if (mCurrDrawable != null) {
        mCurrDrawable.setDither(mDrawableContainerState.mDither);
      }
    }
  }
  
  public void setEnterFadeDuration(int paramInt)
  {
    mDrawableContainerState.mEnterFadeDuration = paramInt;
  }
  
  public void setExitFadeDuration(int paramInt)
  {
    mDrawableContainerState.mExitFadeDuration = paramInt;
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    if (mCurrDrawable != null) {
      mCurrDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mHotspotBounds == null) {
      mHotspotBounds = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      mHotspotBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    if (mCurrDrawable != null) {
      mCurrDrawable.setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    mDrawableContainerState.mHasTintList = true;
    if (mDrawableContainerState.mTintList != paramColorStateList)
    {
      mDrawableContainerState.mTintList = paramColorStateList;
      if (mCurrDrawable != null) {
        mCurrDrawable.setTintList(paramColorStateList);
      }
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    mDrawableContainerState.mHasTintMode = true;
    if (mDrawableContainerState.mTintMode != paramMode)
    {
      mDrawableContainerState.mTintMode = paramMode;
      if (mCurrDrawable != null) {
        mCurrDrawable.setTintMode(paramMode);
      }
    }
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if (mLastDrawable != null) {
      mLastDrawable.setVisible(paramBoolean1, paramBoolean2);
    }
    if (mCurrDrawable != null) {
      mCurrDrawable.setVisible(paramBoolean1, paramBoolean2);
    }
    return bool;
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    if ((paramDrawable == mCurrDrawable) && (getCallback() != null)) {
      getCallback().unscheduleDrawable(this, paramRunnable);
    }
  }
  
  protected final void updateDensity(Resources paramResources)
  {
    mDrawableContainerState.updateDensity(paramResources);
  }
  
  private static class BlockInvalidateCallback
    implements Drawable.Callback
  {
    private Drawable.Callback mCallback;
    
    private BlockInvalidateCallback() {}
    
    public void invalidateDrawable(Drawable paramDrawable) {}
    
    public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
    {
      if (mCallback != null) {
        mCallback.scheduleDrawable(paramDrawable, paramRunnable, paramLong);
      }
    }
    
    public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
    {
      if (mCallback != null) {
        mCallback.unscheduleDrawable(paramDrawable, paramRunnable);
      }
    }
    
    public Drawable.Callback unwrap()
    {
      Drawable.Callback localCallback = mCallback;
      mCallback = null;
      return localCallback;
    }
    
    public BlockInvalidateCallback wrap(Drawable.Callback paramCallback)
    {
      mCallback = paramCallback;
      return this;
    }
  }
  
  public static abstract class DrawableContainerState
    extends Drawable.ConstantState
  {
    boolean mAutoMirrored;
    boolean mCanConstantState;
    int mChangingConfigurations;
    boolean mCheckedConstantSize;
    boolean mCheckedConstantState;
    boolean mCheckedOpacity;
    boolean mCheckedPadding;
    boolean mCheckedStateful;
    int mChildrenChangingConfigurations;
    ColorFilter mColorFilter;
    int mConstantHeight;
    int mConstantMinimumHeight;
    int mConstantMinimumWidth;
    Rect mConstantPadding;
    boolean mConstantSize;
    int mConstantWidth;
    int mDensity = 160;
    boolean mDither;
    SparseArray<Drawable.ConstantState> mDrawableFutures;
    Drawable[] mDrawables;
    int mEnterFadeDuration;
    int mExitFadeDuration;
    boolean mHasColorFilter;
    boolean mHasTintList;
    boolean mHasTintMode;
    int mLayoutDirection;
    boolean mMutated;
    int mNumChildren;
    int mOpacity;
    final DrawableContainer mOwner;
    Resources mSourceRes;
    boolean mStateful;
    ColorStateList mTintList;
    PorterDuff.Mode mTintMode;
    boolean mVariablePadding;
    
    protected DrawableContainerState(DrawableContainerState paramDrawableContainerState, DrawableContainer paramDrawableContainer, Resources paramResources)
    {
      int i = 0;
      mVariablePadding = false;
      mConstantSize = false;
      mDither = true;
      mEnterFadeDuration = 0;
      mExitFadeDuration = 0;
      mOwner = paramDrawableContainer;
      if (paramResources != null) {
        paramDrawableContainer = paramResources;
      } else if (paramDrawableContainerState != null) {
        paramDrawableContainer = mSourceRes;
      } else {
        paramDrawableContainer = null;
      }
      mSourceRes = paramDrawableContainer;
      int j;
      if (paramDrawableContainerState != null) {
        j = mDensity;
      } else {
        j = 0;
      }
      mDensity = Drawable.resolveDensity(paramResources, j);
      if (paramDrawableContainerState != null)
      {
        mChangingConfigurations = mChangingConfigurations;
        mChildrenChangingConfigurations = mChildrenChangingConfigurations;
        mCheckedConstantState = true;
        mCanConstantState = true;
        mVariablePadding = mVariablePadding;
        mConstantSize = mConstantSize;
        mDither = mDither;
        mMutated = mMutated;
        mLayoutDirection = mLayoutDirection;
        mEnterFadeDuration = mEnterFadeDuration;
        mExitFadeDuration = mExitFadeDuration;
        mAutoMirrored = mAutoMirrored;
        mColorFilter = mColorFilter;
        mHasColorFilter = mHasColorFilter;
        mTintList = mTintList;
        mTintMode = mTintMode;
        mHasTintList = mHasTintList;
        mHasTintMode = mHasTintMode;
        if (mDensity == mDensity)
        {
          if (mCheckedPadding)
          {
            mConstantPadding = new Rect(mConstantPadding);
            mCheckedPadding = true;
          }
          if (mCheckedConstantSize)
          {
            mConstantWidth = mConstantWidth;
            mConstantHeight = mConstantHeight;
            mConstantMinimumWidth = mConstantMinimumWidth;
            mConstantMinimumHeight = mConstantMinimumHeight;
            mCheckedConstantSize = true;
          }
        }
        if (mCheckedOpacity)
        {
          mOpacity = mOpacity;
          mCheckedOpacity = true;
        }
        if (mCheckedStateful)
        {
          mStateful = mStateful;
          mCheckedStateful = true;
        }
        paramDrawableContainer = mDrawables;
        mDrawables = new Drawable[paramDrawableContainer.length];
        mNumChildren = mNumChildren;
        paramDrawableContainerState = mDrawableFutures;
        if (paramDrawableContainerState != null) {
          mDrawableFutures = paramDrawableContainerState.clone();
        } else {
          mDrawableFutures = new SparseArray(mNumChildren);
        }
        int k = mNumChildren;
        for (j = i; j < k; j++) {
          if (paramDrawableContainer[j] != null)
          {
            paramDrawableContainerState = paramDrawableContainer[j].getConstantState();
            if (paramDrawableContainerState != null) {
              mDrawableFutures.put(j, paramDrawableContainerState);
            } else {
              mDrawables[j] = paramDrawableContainer[j];
            }
          }
        }
      }
      else
      {
        mDrawables = new Drawable[10];
        mNumChildren = 0;
      }
    }
    
    private void createAllFutures()
    {
      if (mDrawableFutures != null)
      {
        int i = mDrawableFutures.size();
        for (int j = 0; j < i; j++)
        {
          int k = mDrawableFutures.keyAt(j);
          Drawable.ConstantState localConstantState = (Drawable.ConstantState)mDrawableFutures.valueAt(j);
          mDrawables[k] = prepareDrawable(localConstantState.newDrawable(mSourceRes));
        }
        mDrawableFutures = null;
      }
    }
    
    private void mutate()
    {
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      for (int j = 0; j < i; j++) {
        if (arrayOfDrawable[j] != null) {
          arrayOfDrawable[j].mutate();
        }
      }
      mMutated = true;
    }
    
    private Drawable prepareDrawable(Drawable paramDrawable)
    {
      paramDrawable.setLayoutDirection(mLayoutDirection);
      paramDrawable = paramDrawable.mutate();
      paramDrawable.setCallback(mOwner);
      return paramDrawable;
    }
    
    public final int addChild(Drawable paramDrawable)
    {
      int i = mNumChildren;
      if (i >= mDrawables.length) {
        growArray(i, i + 10);
      }
      paramDrawable.mutate();
      paramDrawable.setVisible(false, true);
      paramDrawable.setCallback(mOwner);
      mDrawables[i] = paramDrawable;
      mNumChildren += 1;
      mChildrenChangingConfigurations |= paramDrawable.getChangingConfigurations();
      invalidateCache();
      mConstantPadding = null;
      mCheckedPadding = false;
      mCheckedConstantSize = false;
      mCheckedConstantState = false;
      return i;
    }
    
    final void applyTheme(Resources.Theme paramTheme)
    {
      if (paramTheme != null)
      {
        createAllFutures();
        int i = mNumChildren;
        Drawable[] arrayOfDrawable = mDrawables;
        for (int j = 0; j < i; j++) {
          if ((arrayOfDrawable[j] != null) && (arrayOfDrawable[j].canApplyTheme()))
          {
            arrayOfDrawable[j].applyTheme(paramTheme);
            mChildrenChangingConfigurations |= arrayOfDrawable[j].getChangingConfigurations();
          }
        }
        updateDensity(paramTheme.getResources());
      }
    }
    
    public boolean canApplyTheme()
    {
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      for (int j = 0; j < i; j++)
      {
        Object localObject = arrayOfDrawable[j];
        if (localObject != null)
        {
          if (((Drawable)localObject).canApplyTheme()) {
            return true;
          }
        }
        else
        {
          localObject = (Drawable.ConstantState)mDrawableFutures.get(j);
          if ((localObject != null) && (((Drawable.ConstantState)localObject).canApplyTheme())) {
            return true;
          }
        }
      }
      return false;
    }
    
    public boolean canConstantState()
    {
      try
      {
        if (mCheckedConstantState)
        {
          boolean bool = mCanConstantState;
          return bool;
        }
        createAllFutures();
        mCheckedConstantState = true;
        int i = mNumChildren;
        Drawable[] arrayOfDrawable = mDrawables;
        for (int j = 0; j < i; j++) {
          if (arrayOfDrawable[j].getConstantState() == null)
          {
            mCanConstantState = false;
            return false;
          }
        }
        mCanConstantState = true;
        return true;
      }
      finally {}
    }
    
    final void clearMutated()
    {
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      for (int j = 0; j < i; j++) {
        if (arrayOfDrawable[j] != null) {
          arrayOfDrawable[j].clearMutated();
        }
      }
      mMutated = false;
    }
    
    protected void computeConstantSize()
    {
      mCheckedConstantSize = true;
      createAllFutures();
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      mConstantHeight = -1;
      mConstantWidth = -1;
      int j = 0;
      mConstantMinimumHeight = 0;
      mConstantMinimumWidth = 0;
      while (j < i)
      {
        Drawable localDrawable = arrayOfDrawable[j];
        int k = localDrawable.getIntrinsicWidth();
        if (k > mConstantWidth) {
          mConstantWidth = k;
        }
        k = localDrawable.getIntrinsicHeight();
        if (k > mConstantHeight) {
          mConstantHeight = k;
        }
        k = localDrawable.getMinimumWidth();
        if (k > mConstantMinimumWidth) {
          mConstantMinimumWidth = k;
        }
        k = localDrawable.getMinimumHeight();
        if (k > mConstantMinimumHeight) {
          mConstantMinimumHeight = k;
        }
        j++;
      }
    }
    
    final int getCapacity()
    {
      return mDrawables.length;
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConfigurations | mChildrenChangingConfigurations;
    }
    
    public final Drawable getChild(int paramInt)
    {
      Drawable localDrawable = mDrawables[paramInt];
      if (localDrawable != null) {
        return localDrawable;
      }
      if (mDrawableFutures != null)
      {
        int i = mDrawableFutures.indexOfKey(paramInt);
        if (i >= 0)
        {
          localDrawable = prepareDrawable(((Drawable.ConstantState)mDrawableFutures.valueAt(i)).newDrawable(mSourceRes));
          mDrawables[paramInt] = localDrawable;
          mDrawableFutures.removeAt(i);
          if (mDrawableFutures.size() == 0) {
            mDrawableFutures = null;
          }
          return localDrawable;
        }
      }
      return null;
    }
    
    public final int getChildCount()
    {
      return mNumChildren;
    }
    
    public final Drawable[] getChildren()
    {
      createAllFutures();
      return mDrawables;
    }
    
    public final int getConstantHeight()
    {
      if (!mCheckedConstantSize) {
        computeConstantSize();
      }
      return mConstantHeight;
    }
    
    public final int getConstantMinimumHeight()
    {
      if (!mCheckedConstantSize) {
        computeConstantSize();
      }
      return mConstantMinimumHeight;
    }
    
    public final int getConstantMinimumWidth()
    {
      if (!mCheckedConstantSize) {
        computeConstantSize();
      }
      return mConstantMinimumWidth;
    }
    
    public final Rect getConstantPadding()
    {
      if (mVariablePadding) {
        return null;
      }
      if ((mConstantPadding == null) && (!mCheckedPadding))
      {
        createAllFutures();
        Rect localRect = new Rect();
        int i = mNumChildren;
        Drawable[] arrayOfDrawable = mDrawables;
        Object localObject1 = null;
        int j = 0;
        while (j < i)
        {
          Object localObject2 = localObject1;
          if (arrayOfDrawable[j].getPadding(localRect))
          {
            Object localObject3 = localObject1;
            if (localObject1 == null) {
              localObject3 = new Rect(0, 0, 0, 0);
            }
            if (left > left) {
              left = left;
            }
            if (top > top) {
              top = top;
            }
            if (right > right) {
              right = right;
            }
            localObject2 = localObject3;
            if (bottom > bottom)
            {
              bottom = bottom;
              localObject2 = localObject3;
            }
          }
          j++;
          localObject1 = localObject2;
        }
        mCheckedPadding = true;
        mConstantPadding = localObject1;
        return localObject1;
      }
      return mConstantPadding;
    }
    
    public final int getConstantWidth()
    {
      if (!mCheckedConstantSize) {
        computeConstantSize();
      }
      return mConstantWidth;
    }
    
    public final int getEnterFadeDuration()
    {
      return mEnterFadeDuration;
    }
    
    public final int getExitFadeDuration()
    {
      return mExitFadeDuration;
    }
    
    public final int getOpacity()
    {
      if (mCheckedOpacity) {
        return mOpacity;
      }
      createAllFutures();
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      int j;
      if (i > 0) {
        j = arrayOfDrawable[0].getOpacity();
      } else {
        j = -2;
      }
      for (int k = 1; k < i; k++) {
        j = Drawable.resolveOpacity(j, arrayOfDrawable[k].getOpacity());
      }
      mOpacity = j;
      mCheckedOpacity = true;
      return j;
    }
    
    public void growArray(int paramInt1, int paramInt2)
    {
      Drawable[] arrayOfDrawable = new Drawable[paramInt2];
      System.arraycopy(mDrawables, 0, arrayOfDrawable, 0, paramInt1);
      mDrawables = arrayOfDrawable;
    }
    
    void invalidateCache()
    {
      mCheckedOpacity = false;
      mCheckedStateful = false;
    }
    
    public final boolean isConstantSize()
    {
      return mConstantSize;
    }
    
    public final boolean isStateful()
    {
      if (mCheckedStateful) {
        return mStateful;
      }
      createAllFutures();
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      boolean bool1 = false;
      boolean bool2;
      for (int j = 0;; j++)
      {
        bool2 = bool1;
        if (j >= i) {
          break;
        }
        if (arrayOfDrawable[j].isStateful())
        {
          bool2 = true;
          break;
        }
      }
      mStateful = bool2;
      mCheckedStateful = true;
      return bool2;
    }
    
    public final void setConstantSize(boolean paramBoolean)
    {
      mConstantSize = paramBoolean;
    }
    
    public final void setEnterFadeDuration(int paramInt)
    {
      mEnterFadeDuration = paramInt;
    }
    
    public final void setExitFadeDuration(int paramInt)
    {
      mExitFadeDuration = paramInt;
    }
    
    final boolean setLayoutDirection(int paramInt1, int paramInt2)
    {
      boolean bool1 = false;
      int i = mNumChildren;
      Drawable[] arrayOfDrawable = mDrawables;
      int j = 0;
      while (j < i)
      {
        boolean bool2 = bool1;
        if (arrayOfDrawable[j] != null)
        {
          boolean bool3 = arrayOfDrawable[j].setLayoutDirection(paramInt1);
          bool2 = bool1;
          if (j == paramInt2) {
            bool2 = bool3;
          }
        }
        j++;
        bool1 = bool2;
      }
      mLayoutDirection = paramInt1;
      return bool1;
    }
    
    public final void setVariablePadding(boolean paramBoolean)
    {
      mVariablePadding = paramBoolean;
    }
    
    final void updateDensity(Resources paramResources)
    {
      if (paramResources != null)
      {
        mSourceRes = paramResources;
        int i = Drawable.resolveDensity(paramResources, mDensity);
        int j = mDensity;
        mDensity = i;
        if (j != i)
        {
          mCheckedConstantSize = false;
          mCheckedPadding = false;
        }
      }
    }
  }
}
