package android.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.util.Pools.SynchronizedPool;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.android.internal.R.styleable;
import java.util.ArrayList;

@RemoteViews.RemoteView
public class ProgressBar
  extends View
{
  private static final int MAX_LEVEL = 10000;
  private static final int PROGRESS_ANIM_DURATION = 80;
  private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
  private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;
  private final FloatProperty<ProgressBar> VISUAL_PROGRESS;
  private AccessibilityEventSender mAccessibilityEventSender;
  private boolean mAggregatedIsVisible;
  private AlphaAnimation mAnimation;
  private boolean mAttached;
  private int mBehavior;
  private Drawable mCurrentDrawable;
  private int mDuration;
  private boolean mHasAnimation;
  private boolean mInDrawing;
  private boolean mIndeterminate;
  private Drawable mIndeterminateDrawable;
  private Interpolator mInterpolator;
  private int mMax;
  int mMaxHeight;
  private boolean mMaxInitialized;
  int mMaxWidth;
  private int mMin;
  int mMinHeight;
  private boolean mMinInitialized;
  int mMinWidth;
  boolean mMirrorForRtl;
  private boolean mNoInvalidate;
  private boolean mOnlyIndeterminate;
  private int mProgress;
  private Drawable mProgressDrawable;
  private ProgressTintInfo mProgressTintInfo;
  private final ArrayList<RefreshData> mRefreshData;
  private boolean mRefreshIsPosted;
  private RefreshProgressRunnable mRefreshProgressRunnable;
  int mSampleWidth;
  private int mSecondaryProgress;
  private boolean mShouldStartAnimationDrawable;
  private Transformation mTransformation;
  private long mUiThreadId;
  private float mVisualProgress;
  
  public ProgressBar(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842871);
  }
  
  public ProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    boolean bool = false;
    mSampleWidth = 0;
    mMirrorForRtl = false;
    mRefreshData = new ArrayList();
    VISUAL_PROGRESS = new FloatProperty("visual_progress")
    {
      public Float get(ProgressBar paramAnonymousProgressBar)
      {
        return Float.valueOf(mVisualProgress);
      }
      
      public void setValue(ProgressBar paramAnonymousProgressBar, float paramAnonymousFloat)
      {
        paramAnonymousProgressBar.setVisualProgress(16908301, paramAnonymousFloat);
        ProgressBar.access$802(paramAnonymousProgressBar, paramAnonymousFloat);
      }
    };
    mUiThreadId = Thread.currentThread().getId();
    initProgressBar();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ProgressBar, paramInt1, paramInt2);
    mNoInvalidate = true;
    Drawable localDrawable = paramAttributeSet.getDrawable(8);
    if (localDrawable != null) {
      if (needsTileify(localDrawable)) {
        setProgressDrawableTiled(localDrawable);
      } else {
        setProgressDrawable(localDrawable);
      }
    }
    mDuration = paramAttributeSet.getInt(9, mDuration);
    mMinWidth = paramAttributeSet.getDimensionPixelSize(11, mMinWidth);
    mMaxWidth = paramAttributeSet.getDimensionPixelSize(0, mMaxWidth);
    mMinHeight = paramAttributeSet.getDimensionPixelSize(12, mMinHeight);
    mMaxHeight = paramAttributeSet.getDimensionPixelSize(1, mMaxHeight);
    mBehavior = paramAttributeSet.getInt(10, mBehavior);
    paramInt1 = paramAttributeSet.getResourceId(13, 17432587);
    if (paramInt1 > 0) {
      setInterpolator(paramContext, paramInt1);
    }
    setMin(paramAttributeSet.getInt(26, mMin));
    setMax(paramAttributeSet.getInt(2, mMax));
    setProgress(paramAttributeSet.getInt(3, mProgress));
    setSecondaryProgress(paramAttributeSet.getInt(4, mSecondaryProgress));
    paramContext = paramAttributeSet.getDrawable(7);
    if (paramContext != null) {
      if (needsTileify(paramContext)) {
        setIndeterminateDrawableTiled(paramContext);
      } else {
        setIndeterminateDrawable(paramContext);
      }
    }
    mOnlyIndeterminate = paramAttributeSet.getBoolean(6, mOnlyIndeterminate);
    mNoInvalidate = false;
    if ((!mOnlyIndeterminate) && (!paramAttributeSet.getBoolean(5, mIndeterminate))) {
      break label346;
    }
    bool = true;
    label346:
    setIndeterminate(bool);
    mMirrorForRtl = paramAttributeSet.getBoolean(15, mMirrorForRtl);
    if (paramAttributeSet.hasValue(17))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mProgressTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(17, -1), null);
      mProgressTintInfo.mHasProgressTintMode = true;
    }
    if (paramAttributeSet.hasValue(16))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mProgressTintList = paramAttributeSet.getColorStateList(16);
      mProgressTintInfo.mHasProgressTint = true;
    }
    if (paramAttributeSet.hasValue(19))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mProgressBackgroundTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(19, -1), null);
      mProgressTintInfo.mHasProgressBackgroundTintMode = true;
    }
    if (paramAttributeSet.hasValue(18))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mProgressBackgroundTintList = paramAttributeSet.getColorStateList(18);
      mProgressTintInfo.mHasProgressBackgroundTint = true;
    }
    if (paramAttributeSet.hasValue(21))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mSecondaryProgressTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(21, -1), null);
      mProgressTintInfo.mHasSecondaryProgressTintMode = true;
    }
    if (paramAttributeSet.hasValue(20))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mSecondaryProgressTintList = paramAttributeSet.getColorStateList(20);
      mProgressTintInfo.mHasSecondaryProgressTint = true;
    }
    if (paramAttributeSet.hasValue(23))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mIndeterminateTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(23, -1), null);
      mProgressTintInfo.mHasIndeterminateTintMode = true;
    }
    if (paramAttributeSet.hasValue(22))
    {
      if (mProgressTintInfo == null) {
        mProgressTintInfo = new ProgressTintInfo(null);
      }
      mProgressTintInfo.mIndeterminateTintList = paramAttributeSet.getColorStateList(22);
      mProgressTintInfo.mHasIndeterminateTint = true;
    }
    paramAttributeSet.recycle();
    applyProgressTints();
    applyIndeterminateTint();
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
  }
  
  private void applyIndeterminateTint()
  {
    if ((mIndeterminateDrawable != null) && (mProgressTintInfo != null))
    {
      ProgressTintInfo localProgressTintInfo = mProgressTintInfo;
      if ((mHasIndeterminateTint) || (mHasIndeterminateTintMode))
      {
        mIndeterminateDrawable = mIndeterminateDrawable.mutate();
        if (mHasIndeterminateTint) {
          mIndeterminateDrawable.setTintList(mIndeterminateTintList);
        }
        if (mHasIndeterminateTintMode) {
          mIndeterminateDrawable.setTintMode(mIndeterminateTintMode);
        }
        if (mIndeterminateDrawable.isStateful()) {
          mIndeterminateDrawable.setState(getDrawableState());
        }
      }
    }
  }
  
  private void applyPrimaryProgressTint()
  {
    if ((mProgressTintInfo.mHasProgressTint) || (mProgressTintInfo.mHasProgressTintMode))
    {
      Drawable localDrawable = getTintTarget(16908301, true);
      if (localDrawable != null)
      {
        if (mProgressTintInfo.mHasProgressTint) {
          localDrawable.setTintList(mProgressTintInfo.mProgressTintList);
        }
        if (mProgressTintInfo.mHasProgressTintMode) {
          localDrawable.setTintMode(mProgressTintInfo.mProgressTintMode);
        }
        if (localDrawable.isStateful()) {
          localDrawable.setState(getDrawableState());
        }
      }
    }
  }
  
  private void applyProgressBackgroundTint()
  {
    if ((mProgressTintInfo.mHasProgressBackgroundTint) || (mProgressTintInfo.mHasProgressBackgroundTintMode))
    {
      Drawable localDrawable = getTintTarget(16908288, false);
      if (localDrawable != null)
      {
        if (mProgressTintInfo.mHasProgressBackgroundTint) {
          localDrawable.setTintList(mProgressTintInfo.mProgressBackgroundTintList);
        }
        if (mProgressTintInfo.mHasProgressBackgroundTintMode) {
          localDrawable.setTintMode(mProgressTintInfo.mProgressBackgroundTintMode);
        }
        if (localDrawable.isStateful()) {
          localDrawable.setState(getDrawableState());
        }
      }
    }
  }
  
  private void applyProgressTints()
  {
    if ((mProgressDrawable != null) && (mProgressTintInfo != null))
    {
      applyPrimaryProgressTint();
      applyProgressBackgroundTint();
      applySecondaryProgressTint();
    }
  }
  
  private void applySecondaryProgressTint()
  {
    if ((mProgressTintInfo.mHasSecondaryProgressTint) || (mProgressTintInfo.mHasSecondaryProgressTintMode))
    {
      Drawable localDrawable = getTintTarget(16908303, false);
      if (localDrawable != null)
      {
        if (mProgressTintInfo.mHasSecondaryProgressTint) {
          localDrawable.setTintList(mProgressTintInfo.mSecondaryProgressTintList);
        }
        if (mProgressTintInfo.mHasSecondaryProgressTintMode) {
          localDrawable.setTintMode(mProgressTintInfo.mSecondaryProgressTintMode);
        }
        if (localDrawable.isStateful()) {
          localDrawable.setState(getDrawableState());
        }
      }
    }
  }
  
  private void doRefreshProgress(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    try
    {
      int i = mMax - mMin;
      float f;
      if (i > 0) {
        f = (paramInt2 - mMin) / i;
      } else {
        f = 0.0F;
      }
      if (paramInt1 == 16908301) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) && (paramBoolean3))
      {
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this, VISUAL_PROGRESS, new float[] { f });
        localObjectAnimator.setAutoCancel(true);
        localObjectAnimator.setDuration(80L);
        localObjectAnimator.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
        localObjectAnimator.start();
      }
      else
      {
        setVisualProgress(paramInt1, f);
      }
      if ((i != 0) && (paramBoolean2)) {
        onProgressRefresh(f, paramBoolean1, paramInt2);
      }
      return;
    }
    finally {}
  }
  
  private Drawable getTintTarget(int paramInt, boolean paramBoolean)
  {
    Object localObject = null;
    Drawable localDrawable1 = null;
    Drawable localDrawable2 = mProgressDrawable;
    if (localDrawable2 != null)
    {
      mProgressDrawable = localDrawable2.mutate();
      if ((localDrawable2 instanceof LayerDrawable)) {
        localDrawable1 = ((LayerDrawable)localDrawable2).findDrawableByLayerId(paramInt);
      }
      localObject = localDrawable1;
      if (paramBoolean)
      {
        localObject = localDrawable1;
        if (localDrawable1 == null) {
          localObject = localDrawable2;
        }
      }
    }
    return localObject;
  }
  
  private void initProgressBar()
  {
    mMin = 0;
    mMax = 100;
    mProgress = 0;
    mSecondaryProgress = 0;
    mIndeterminate = false;
    mOnlyIndeterminate = false;
    mDuration = 4000;
    mBehavior = 1;
    mMinWidth = 24;
    mMaxWidth = 48;
    mMinHeight = 24;
    mMaxHeight = 48;
  }
  
  private static boolean needsTileify(Drawable paramDrawable)
  {
    int i;
    int j;
    if ((paramDrawable instanceof LayerDrawable))
    {
      paramDrawable = (LayerDrawable)paramDrawable;
      i = paramDrawable.getNumberOfLayers();
      for (j = 0; j < i; j++) {
        if (needsTileify(paramDrawable.getDrawable(j))) {
          return true;
        }
      }
      return false;
    }
    if ((paramDrawable instanceof StateListDrawable))
    {
      paramDrawable = (StateListDrawable)paramDrawable;
      i = paramDrawable.getStateCount();
      for (j = 0; j < i; j++) {
        if (needsTileify(paramDrawable.getStateDrawable(j))) {
          return true;
        }
      }
      return false;
    }
    return (paramDrawable instanceof BitmapDrawable);
  }
  
  private void refreshProgress(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      if (mUiThreadId == Thread.currentThread().getId())
      {
        doRefreshProgress(paramInt1, paramInt2, paramBoolean1, true, paramBoolean2);
      }
      else
      {
        if (mRefreshProgressRunnable == null)
        {
          localObject1 = new android/widget/ProgressBar$RefreshProgressRunnable;
          ((RefreshProgressRunnable)localObject1).<init>(this, null);
          mRefreshProgressRunnable = ((RefreshProgressRunnable)localObject1);
        }
        Object localObject1 = RefreshData.obtain(paramInt1, paramInt2, paramBoolean1, paramBoolean2);
        mRefreshData.add(localObject1);
        if ((mAttached) && (!mRefreshIsPosted))
        {
          post(mRefreshProgressRunnable);
          mRefreshIsPosted = true;
        }
      }
      return;
    }
    finally {}
  }
  
  private void scheduleAccessibilityEventSender()
  {
    if (mAccessibilityEventSender == null) {
      mAccessibilityEventSender = new AccessibilityEventSender(null);
    } else {
      removeCallbacks(mAccessibilityEventSender);
    }
    postDelayed(mAccessibilityEventSender, 200L);
  }
  
  private void setVisualProgress(int paramInt, float paramFloat)
  {
    mVisualProgress = paramFloat;
    Drawable localDrawable1 = mCurrentDrawable;
    Drawable localDrawable2 = localDrawable1;
    if ((localDrawable1 instanceof LayerDrawable))
    {
      localDrawable1 = ((LayerDrawable)localDrawable1).findDrawableByLayerId(paramInt);
      localDrawable2 = localDrawable1;
      if (localDrawable1 == null) {
        localDrawable2 = mCurrentDrawable;
      }
    }
    if (localDrawable2 != null) {
      localDrawable2.setLevel((int)(10000.0F * paramFloat));
    } else {
      invalidate();
    }
    onVisualProgressChanged(paramInt, paramFloat);
  }
  
  private void swapCurrentDrawable(Drawable paramDrawable)
  {
    Drawable localDrawable = mCurrentDrawable;
    mCurrentDrawable = paramDrawable;
    if (localDrawable != mCurrentDrawable)
    {
      if (localDrawable != null) {
        localDrawable.setVisible(false, false);
      }
      if (mCurrentDrawable != null)
      {
        paramDrawable = mCurrentDrawable;
        boolean bool;
        if ((getWindowVisibility() == 0) && (isShown())) {
          bool = true;
        } else {
          bool = false;
        }
        paramDrawable.setVisible(bool, false);
      }
    }
  }
  
  private Drawable tileify(Drawable paramDrawable, boolean paramBoolean)
  {
    boolean bool = paramDrawable instanceof LayerDrawable;
    int i = 0;
    int j = 0;
    Object localObject;
    if (bool)
    {
      paramDrawable = (LayerDrawable)paramDrawable;
      int k = paramDrawable.getNumberOfLayers();
      localObject = new Drawable[k];
      for (i = 0; i < k; i++)
      {
        int m = paramDrawable.getId(i);
        Drawable localDrawable = paramDrawable.getDrawable(i);
        if ((m != 16908301) && (m != 16908303)) {
          paramBoolean = false;
        } else {
          paramBoolean = true;
        }
        localObject[i] = tileify(localDrawable, paramBoolean);
      }
      localObject = new LayerDrawable((Drawable[])localObject);
      for (i = j; i < k; i++)
      {
        ((LayerDrawable)localObject).setId(i, paramDrawable.getId(i));
        ((LayerDrawable)localObject).setLayerGravity(i, paramDrawable.getLayerGravity(i));
        ((LayerDrawable)localObject).setLayerWidth(i, paramDrawable.getLayerWidth(i));
        ((LayerDrawable)localObject).setLayerHeight(i, paramDrawable.getLayerHeight(i));
        ((LayerDrawable)localObject).setLayerInsetLeft(i, paramDrawable.getLayerInsetLeft(i));
        ((LayerDrawable)localObject).setLayerInsetRight(i, paramDrawable.getLayerInsetRight(i));
        ((LayerDrawable)localObject).setLayerInsetTop(i, paramDrawable.getLayerInsetTop(i));
        ((LayerDrawable)localObject).setLayerInsetBottom(i, paramDrawable.getLayerInsetBottom(i));
        ((LayerDrawable)localObject).setLayerInsetStart(i, paramDrawable.getLayerInsetStart(i));
        ((LayerDrawable)localObject).setLayerInsetEnd(i, paramDrawable.getLayerInsetEnd(i));
      }
      return localObject;
    }
    if ((paramDrawable instanceof StateListDrawable))
    {
      localObject = (StateListDrawable)paramDrawable;
      paramDrawable = new StateListDrawable();
      j = ((StateListDrawable)localObject).getStateCount();
      while (i < j)
      {
        paramDrawable.addState(((StateListDrawable)localObject).getStateSet(i), tileify(((StateListDrawable)localObject).getStateDrawable(i), paramBoolean));
        i++;
      }
      return paramDrawable;
    }
    if ((paramDrawable instanceof BitmapDrawable))
    {
      paramDrawable = (BitmapDrawable)paramDrawable.getConstantState().newDrawable(getResources());
      paramDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
      if (mSampleWidth <= 0) {
        mSampleWidth = paramDrawable.getIntrinsicWidth();
      }
      if (paramBoolean) {
        return new ClipDrawable(paramDrawable, 3, 1);
      }
      return paramDrawable;
    }
    return paramDrawable;
  }
  
  private Drawable tileifyIndeterminate(Drawable paramDrawable)
  {
    Object localObject = paramDrawable;
    if ((paramDrawable instanceof AnimationDrawable))
    {
      AnimationDrawable localAnimationDrawable = (AnimationDrawable)paramDrawable;
      int i = localAnimationDrawable.getNumberOfFrames();
      localObject = new AnimationDrawable();
      ((AnimationDrawable)localObject).setOneShot(localAnimationDrawable.isOneShot());
      for (int j = 0; j < i; j++)
      {
        paramDrawable = tileify(localAnimationDrawable.getFrame(j), true);
        paramDrawable.setLevel(10000);
        ((AnimationDrawable)localObject).addFrame(paramDrawable, localAnimationDrawable.getDuration(j));
      }
      ((AnimationDrawable)localObject).setLevel(10000);
    }
    return localObject;
  }
  
  private void updateDrawableBounds(int paramInt1, int paramInt2)
  {
    int i = paramInt1 - (mPaddingRight + mPaddingLeft);
    int j = paramInt2 - (mPaddingTop + mPaddingBottom);
    paramInt1 = i;
    paramInt2 = j;
    int k = 0;
    int m = 0;
    int n = paramInt1;
    int i1 = paramInt2;
    if (mIndeterminateDrawable != null)
    {
      int i2 = paramInt1;
      i1 = paramInt2;
      int i3 = k;
      n = m;
      if (mOnlyIndeterminate)
      {
        i2 = paramInt1;
        i1 = paramInt2;
        i3 = k;
        n = m;
        if (!(mIndeterminateDrawable instanceof AnimationDrawable))
        {
          i1 = mIndeterminateDrawable.getIntrinsicWidth();
          n = mIndeterminateDrawable.getIntrinsicHeight();
          float f1 = i1 / n;
          float f2 = i / j;
          i2 = paramInt1;
          i1 = paramInt2;
          i3 = k;
          n = m;
          if (f1 != f2) {
            if (f2 > f1)
            {
              paramInt1 = (int)(j * f1);
              n = (i - paramInt1) / 2;
              i2 = n + paramInt1;
              i1 = paramInt2;
              i3 = k;
            }
            else
            {
              paramInt2 = (int)(i * (1.0F / f1));
              i3 = (j - paramInt2) / 2;
              i1 = paramInt2 + i3;
              n = m;
              i2 = paramInt1;
            }
          }
        }
      }
      paramInt1 = i2;
      paramInt2 = n;
      if (isLayoutRtl())
      {
        paramInt1 = i2;
        paramInt2 = n;
        if (mMirrorForRtl)
        {
          paramInt2 = i - i2;
          paramInt1 = i - n;
        }
      }
      mIndeterminateDrawable.setBounds(paramInt2, i3, paramInt1, i1);
      n = paramInt1;
    }
    if (mProgressDrawable != null) {
      mProgressDrawable.setBounds(0, 0, n, i1);
    }
  }
  
  private void updateDrawableState()
  {
    int[] arrayOfInt = getDrawableState();
    boolean bool1 = false;
    Drawable localDrawable = mProgressDrawable;
    boolean bool2 = bool1;
    if (localDrawable != null)
    {
      bool2 = bool1;
      if (localDrawable.isStateful()) {
        bool2 = false | localDrawable.setState(arrayOfInt);
      }
    }
    localDrawable = mIndeterminateDrawable;
    bool1 = bool2;
    if (localDrawable != null)
    {
      bool1 = bool2;
      if (localDrawable.isStateful()) {
        bool1 = bool2 | localDrawable.setState(arrayOfInt);
      }
    }
    if (bool1) {
      invalidate();
    }
  }
  
  void drawTrack(Canvas paramCanvas)
  {
    Drawable localDrawable = mCurrentDrawable;
    if (localDrawable != null)
    {
      int i = paramCanvas.save();
      if ((isLayoutRtl()) && (mMirrorForRtl))
      {
        paramCanvas.translate(getWidth() - mPaddingRight, mPaddingTop);
        paramCanvas.scale(-1.0F, 1.0F);
      }
      else
      {
        paramCanvas.translate(mPaddingLeft, mPaddingTop);
      }
      long l = getDrawingTime();
      float f;
      if (mHasAnimation)
      {
        mAnimation.getTransformation(l, mTransformation);
        f = mTransformation.getAlpha();
      }
      try
      {
        mInDrawing = true;
        localDrawable.setLevel((int)(10000.0F * f));
        mInDrawing = false;
        postInvalidateOnAnimation();
      }
      finally
      {
        mInDrawing = false;
      }
      paramCanvas.restoreToCount(i);
      if ((mShouldStartAnimationDrawable) && ((localDrawable instanceof Animatable)))
      {
        ((Animatable)localDrawable).start();
        mShouldStartAnimationDrawable = false;
      }
    }
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mProgressDrawable != null) {
      mProgressDrawable.setHotspot(paramFloat1, paramFloat2);
    }
    if (mIndeterminateDrawable != null) {
      mIndeterminateDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    updateDrawableState();
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("progress:max", getMax());
    paramViewHierarchyEncoder.addProperty("progress:progress", getProgress());
    paramViewHierarchyEncoder.addProperty("progress:secondaryProgress", getSecondaryProgress());
    paramViewHierarchyEncoder.addProperty("progress:indeterminate", isIndeterminate());
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ProgressBar.class.getName();
  }
  
  Drawable getCurrentDrawable()
  {
    return mCurrentDrawable;
  }
  
  Shape getDrawableShape()
  {
    return new RoundRectShape(new float[] { 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F }, null, null);
  }
  
  public Drawable getIndeterminateDrawable()
  {
    return mIndeterminateDrawable;
  }
  
  public ColorStateList getIndeterminateTintList()
  {
    ColorStateList localColorStateList;
    if (mProgressTintInfo != null) {
      localColorStateList = mProgressTintInfo.mIndeterminateTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getIndeterminateTintMode()
  {
    PorterDuff.Mode localMode;
    if (mProgressTintInfo != null) {
      localMode = mProgressTintInfo.mIndeterminateTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  public Interpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  @ViewDebug.ExportedProperty(category="progress")
  public int getMax()
  {
    try
    {
      int i = mMax;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @ViewDebug.ExportedProperty(category="progress")
  public int getMin()
  {
    try
    {
      int i = mMin;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean getMirrorForRtl()
  {
    return mMirrorForRtl;
  }
  
  @ViewDebug.ExportedProperty(category="progress")
  public int getProgress()
  {
    try
    {
      int i;
      if (mIndeterminate) {
        i = 0;
      } else {
        i = mProgress;
      }
      return i;
    }
    finally {}
  }
  
  public ColorStateList getProgressBackgroundTintList()
  {
    ColorStateList localColorStateList;
    if (mProgressTintInfo != null) {
      localColorStateList = mProgressTintInfo.mProgressBackgroundTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getProgressBackgroundTintMode()
  {
    PorterDuff.Mode localMode;
    if (mProgressTintInfo != null) {
      localMode = mProgressTintInfo.mProgressBackgroundTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  public Drawable getProgressDrawable()
  {
    return mProgressDrawable;
  }
  
  public ColorStateList getProgressTintList()
  {
    ColorStateList localColorStateList;
    if (mProgressTintInfo != null) {
      localColorStateList = mProgressTintInfo.mProgressTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getProgressTintMode()
  {
    PorterDuff.Mode localMode;
    if (mProgressTintInfo != null) {
      localMode = mProgressTintInfo.mProgressTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  @ViewDebug.ExportedProperty(category="progress")
  public int getSecondaryProgress()
  {
    try
    {
      int i;
      if (mIndeterminate) {
        i = 0;
      } else {
        i = mSecondaryProgress;
      }
      return i;
    }
    finally {}
  }
  
  public ColorStateList getSecondaryProgressTintList()
  {
    ColorStateList localColorStateList;
    if (mProgressTintInfo != null) {
      localColorStateList = mProgressTintInfo.mSecondaryProgressTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getSecondaryProgressTintMode()
  {
    PorterDuff.Mode localMode;
    if (mProgressTintInfo != null) {
      localMode = mProgressTintInfo.mSecondaryProgressTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  public final void incrementProgressBy(int paramInt)
  {
    try
    {
      setProgress(mProgress + paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final void incrementSecondaryProgressBy(int paramInt)
  {
    try
    {
      setSecondaryProgress(mSecondaryProgress + paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (!mInDrawing) {
      if (verifyDrawable(paramDrawable))
      {
        paramDrawable = paramDrawable.getBounds();
        int i = mScrollX + mPaddingLeft;
        int j = mScrollY + mPaddingTop;
        invalidate(left + i, top + j, right + i, bottom + j);
      }
      else
      {
        super.invalidateDrawable(paramDrawable);
      }
    }
  }
  
  public boolean isAnimating()
  {
    boolean bool;
    if ((isIndeterminate()) && (getWindowVisibility() == 0) && (isShown())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="progress")
  public boolean isIndeterminate()
  {
    try
    {
      boolean bool = mIndeterminate;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mProgressDrawable != null) {
      mProgressDrawable.jumpToCurrentState();
    }
    if (mIndeterminateDrawable != null) {
      mIndeterminateDrawable.jumpToCurrentState();
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (mIndeterminate) {
      startAnimation();
    }
    if (mRefreshData != null) {
      try
      {
        int i = mRefreshData.size();
        for (int j = 0; j < i; j++)
        {
          RefreshData localRefreshData = (RefreshData)mRefreshData.get(j);
          doRefreshProgress(id, progress, fromUser, true, animate);
          localRefreshData.recycle();
        }
        mRefreshData.clear();
      }
      finally {}
    }
    mAttached = true;
  }
  
  protected void onDetachedFromWindow()
  {
    if (mIndeterminate) {
      stopAnimation();
    }
    if (mRefreshProgressRunnable != null)
    {
      removeCallbacks(mRefreshProgressRunnable);
      mRefreshIsPosted = false;
    }
    if (mAccessibilityEventSender != null) {
      removeCallbacks(mAccessibilityEventSender);
    }
    super.onDetachedFromWindow();
    mAttached = false;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    try
    {
      super.onDraw(paramCanvas);
      drawTrack(paramCanvas);
      return;
    }
    finally
    {
      paramCanvas = finally;
      throw paramCanvas;
    }
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    paramAccessibilityEvent.setItemCount(mMax - mMin);
    paramAccessibilityEvent.setCurrentItemIndex(mProgress);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (!isIndeterminate()) {
      paramAccessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(0, getMin(), getMax(), getProgress()));
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    try
    {
      Drawable localDrawable = mCurrentDrawable;
      if (localDrawable != null)
      {
        i = Math.max(mMinWidth, Math.min(mMaxWidth, localDrawable.getIntrinsicWidth()));
        j = Math.max(mMinHeight, Math.min(mMaxHeight, localDrawable.getIntrinsicHeight()));
      }
      updateDrawableState();
      int k = mPaddingLeft;
      int m = mPaddingRight;
      int n = mPaddingTop;
      int i1 = mPaddingBottom;
      setMeasuredDimension(resolveSizeAndState(i + (k + m), paramInt1, 0), resolveSizeAndState(j + (n + i1), paramInt2, 0));
      return;
    }
    finally {}
  }
  
  void onProgressRefresh(float paramFloat, boolean paramBoolean, int paramInt)
  {
    if (AccessibilityManager.getInstance(mContext).isEnabled()) {
      scheduleAccessibilityEventSender();
    }
  }
  
  public void onResolveDrawables(int paramInt)
  {
    Drawable localDrawable = mCurrentDrawable;
    if (localDrawable != null) {
      localDrawable.setLayoutDirection(paramInt);
    }
    if (mIndeterminateDrawable != null) {
      mIndeterminateDrawable.setLayoutDirection(paramInt);
    }
    if (mProgressDrawable != null) {
      mProgressDrawable.setLayoutDirection(paramInt);
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    setProgress(progress);
    setSecondaryProgress(secondaryProgress);
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    progress = mProgress;
    secondaryProgress = mSecondaryProgress;
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateDrawableBounds(paramInt1, paramInt2);
  }
  
  public void onVisibilityAggregated(boolean paramBoolean)
  {
    super.onVisibilityAggregated(paramBoolean);
    if (paramBoolean != mAggregatedIsVisible)
    {
      mAggregatedIsVisible = paramBoolean;
      if (mIndeterminate) {
        if (paramBoolean) {
          startAnimation();
        } else {
          stopAnimation();
        }
      }
      if (mCurrentDrawable != null) {
        mCurrentDrawable.setVisible(paramBoolean, false);
      }
    }
  }
  
  void onVisualProgressChanged(int paramInt, float paramFloat) {}
  
  public void postInvalidate()
  {
    if (!mNoInvalidate) {
      super.postInvalidate();
    }
  }
  
  @RemotableViewMethod
  public void setIndeterminate(boolean paramBoolean)
  {
    try
    {
      if (((!mOnlyIndeterminate) || (!mIndeterminate)) && (paramBoolean != mIndeterminate))
      {
        mIndeterminate = paramBoolean;
        if (paramBoolean)
        {
          swapCurrentDrawable(mIndeterminateDrawable);
          startAnimation();
        }
        else
        {
          swapCurrentDrawable(mProgressDrawable);
          stopAnimation();
        }
      }
      return;
    }
    finally {}
  }
  
  public void setIndeterminateDrawable(Drawable paramDrawable)
  {
    if (mIndeterminateDrawable != paramDrawable)
    {
      if (mIndeterminateDrawable != null)
      {
        mIndeterminateDrawable.setCallback(null);
        unscheduleDrawable(mIndeterminateDrawable);
      }
      mIndeterminateDrawable = paramDrawable;
      if (paramDrawable != null)
      {
        paramDrawable.setCallback(this);
        paramDrawable.setLayoutDirection(getLayoutDirection());
        if (paramDrawable.isStateful()) {
          paramDrawable.setState(getDrawableState());
        }
        applyIndeterminateTint();
      }
      if (mIndeterminate)
      {
        swapCurrentDrawable(paramDrawable);
        postInvalidate();
      }
    }
  }
  
  public void setIndeterminateDrawableTiled(Drawable paramDrawable)
  {
    Drawable localDrawable = paramDrawable;
    if (paramDrawable != null) {
      localDrawable = tileifyIndeterminate(paramDrawable);
    }
    setIndeterminateDrawable(localDrawable);
  }
  
  @RemotableViewMethod
  public void setIndeterminateTintList(ColorStateList paramColorStateList)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mIndeterminateTintList = paramColorStateList;
    mProgressTintInfo.mHasIndeterminateTint = true;
    applyIndeterminateTint();
  }
  
  public void setIndeterminateTintMode(PorterDuff.Mode paramMode)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mIndeterminateTintMode = paramMode;
    mProgressTintInfo.mHasIndeterminateTintMode = true;
    applyIndeterminateTint();
  }
  
  public void setInterpolator(Context paramContext, int paramInt)
  {
    setInterpolator(AnimationUtils.loadInterpolator(paramContext, paramInt));
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    mInterpolator = paramInterpolator;
  }
  
  @RemotableViewMethod
  public void setMax(int paramInt)
  {
    int i = paramInt;
    try
    {
      if (mMinInitialized)
      {
        i = paramInt;
        if (paramInt < mMin) {
          i = mMin;
        }
      }
      mMaxInitialized = true;
      if ((mMinInitialized) && (i != mMax))
      {
        mMax = i;
        postInvalidate();
        if (mProgress > i) {
          mProgress = i;
        }
        refreshProgress(16908301, mProgress, false, false);
      }
      else
      {
        mMax = i;
      }
      return;
    }
    finally {}
  }
  
  @RemotableViewMethod
  public void setMin(int paramInt)
  {
    int i = paramInt;
    try
    {
      if (mMaxInitialized)
      {
        i = paramInt;
        if (paramInt > mMax) {
          i = mMax;
        }
      }
      mMinInitialized = true;
      if ((mMaxInitialized) && (i != mMin))
      {
        mMin = i;
        postInvalidate();
        if (mProgress < i) {
          mProgress = i;
        }
        refreshProgress(16908301, mProgress, false, false);
      }
      else
      {
        mMin = i;
      }
      return;
    }
    finally {}
  }
  
  @RemotableViewMethod
  public void setProgress(int paramInt)
  {
    try
    {
      setProgressInternal(paramInt, false, false);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setProgress(int paramInt, boolean paramBoolean)
  {
    setProgressInternal(paramInt, false, paramBoolean);
  }
  
  @RemotableViewMethod
  public void setProgressBackgroundTintList(ColorStateList paramColorStateList)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mProgressBackgroundTintList = paramColorStateList;
    mProgressTintInfo.mHasProgressBackgroundTint = true;
    if (mProgressDrawable != null) {
      applyProgressBackgroundTint();
    }
  }
  
  public void setProgressBackgroundTintMode(PorterDuff.Mode paramMode)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mProgressBackgroundTintMode = paramMode;
    mProgressTintInfo.mHasProgressBackgroundTintMode = true;
    if (mProgressDrawable != null) {
      applyProgressBackgroundTint();
    }
  }
  
  public void setProgressDrawable(Drawable paramDrawable)
  {
    if (mProgressDrawable != paramDrawable)
    {
      if (mProgressDrawable != null)
      {
        mProgressDrawable.setCallback(null);
        unscheduleDrawable(mProgressDrawable);
      }
      mProgressDrawable = paramDrawable;
      if (paramDrawable != null)
      {
        paramDrawable.setCallback(this);
        paramDrawable.setLayoutDirection(getLayoutDirection());
        if (paramDrawable.isStateful()) {
          paramDrawable.setState(getDrawableState());
        }
        int i = paramDrawable.getMinimumHeight();
        if (mMaxHeight < i)
        {
          mMaxHeight = i;
          requestLayout();
        }
        applyProgressTints();
      }
      if (!mIndeterminate)
      {
        swapCurrentDrawable(paramDrawable);
        postInvalidate();
      }
      updateDrawableBounds(getWidth(), getHeight());
      updateDrawableState();
      doRefreshProgress(16908301, mProgress, false, false, false);
      doRefreshProgress(16908303, mSecondaryProgress, false, false, false);
    }
  }
  
  public void setProgressDrawableTiled(Drawable paramDrawable)
  {
    Drawable localDrawable = paramDrawable;
    if (paramDrawable != null) {
      localDrawable = tileify(paramDrawable, false);
    }
    setProgressDrawable(localDrawable);
  }
  
  @RemotableViewMethod
  boolean setProgressInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      boolean bool = mIndeterminate;
      if (bool) {
        return false;
      }
      int i = MathUtils.constrain(paramInt, mMin, mMax);
      paramInt = mProgress;
      if (i == paramInt) {
        return false;
      }
      mProgress = i;
      refreshProgress(16908301, mProgress, paramBoolean1, paramBoolean2);
      return true;
    }
    finally {}
  }
  
  @RemotableViewMethod
  public void setProgressTintList(ColorStateList paramColorStateList)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mProgressTintList = paramColorStateList;
    mProgressTintInfo.mHasProgressTint = true;
    if (mProgressDrawable != null) {
      applyPrimaryProgressTint();
    }
  }
  
  public void setProgressTintMode(PorterDuff.Mode paramMode)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mProgressTintMode = paramMode;
    mProgressTintInfo.mHasProgressTintMode = true;
    if (mProgressDrawable != null) {
      applyPrimaryProgressTint();
    }
  }
  
  @RemotableViewMethod
  public void setSecondaryProgress(int paramInt)
  {
    try
    {
      boolean bool = mIndeterminate;
      if (bool) {
        return;
      }
      int i = paramInt;
      if (paramInt < mMin) {
        i = mMin;
      }
      paramInt = i;
      if (i > mMax) {
        paramInt = mMax;
      }
      if (paramInt != mSecondaryProgress)
      {
        mSecondaryProgress = paramInt;
        refreshProgress(16908303, mSecondaryProgress, false, false);
      }
      return;
    }
    finally {}
  }
  
  public void setSecondaryProgressTintList(ColorStateList paramColorStateList)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mSecondaryProgressTintList = paramColorStateList;
    mProgressTintInfo.mHasSecondaryProgressTint = true;
    if (mProgressDrawable != null) {
      applySecondaryProgressTint();
    }
  }
  
  public void setSecondaryProgressTintMode(PorterDuff.Mode paramMode)
  {
    if (mProgressTintInfo == null) {
      mProgressTintInfo = new ProgressTintInfo(null);
    }
    mProgressTintInfo.mSecondaryProgressTintMode = paramMode;
    mProgressTintInfo.mHasSecondaryProgressTintMode = true;
    if (mProgressDrawable != null) {
      applySecondaryProgressTint();
    }
  }
  
  void startAnimation()
  {
    if ((getVisibility() == 0) && (getWindowVisibility() == 0))
    {
      if ((mIndeterminateDrawable instanceof Animatable))
      {
        mShouldStartAnimationDrawable = true;
        mHasAnimation = false;
      }
      else
      {
        mHasAnimation = true;
        if (mInterpolator == null) {
          mInterpolator = new LinearInterpolator();
        }
        if (mTransformation == null) {
          mTransformation = new Transformation();
        } else {
          mTransformation.clear();
        }
        if (mAnimation == null) {
          mAnimation = new AlphaAnimation(0.0F, 1.0F);
        } else {
          mAnimation.reset();
        }
        mAnimation.setRepeatMode(mBehavior);
        mAnimation.setRepeatCount(-1);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(mInterpolator);
        mAnimation.setStartTime(-1L);
      }
      postInvalidate();
      return;
    }
  }
  
  void stopAnimation()
  {
    mHasAnimation = false;
    if ((mIndeterminateDrawable instanceof Animatable))
    {
      ((Animatable)mIndeterminateDrawable).stop();
      mShouldStartAnimationDrawable = false;
    }
    postInvalidate();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((paramDrawable != mProgressDrawable) && (paramDrawable != mIndeterminateDrawable) && (!super.verifyDrawable(paramDrawable))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private class AccessibilityEventSender
    implements Runnable
  {
    private AccessibilityEventSender() {}
    
    public void run()
    {
      sendAccessibilityEvent(4);
    }
  }
  
  private static class ProgressTintInfo
  {
    boolean mHasIndeterminateTint;
    boolean mHasIndeterminateTintMode;
    boolean mHasProgressBackgroundTint;
    boolean mHasProgressBackgroundTintMode;
    boolean mHasProgressTint;
    boolean mHasProgressTintMode;
    boolean mHasSecondaryProgressTint;
    boolean mHasSecondaryProgressTintMode;
    ColorStateList mIndeterminateTintList;
    PorterDuff.Mode mIndeterminateTintMode;
    ColorStateList mProgressBackgroundTintList;
    PorterDuff.Mode mProgressBackgroundTintMode;
    ColorStateList mProgressTintList;
    PorterDuff.Mode mProgressTintMode;
    ColorStateList mSecondaryProgressTintList;
    PorterDuff.Mode mSecondaryProgressTintMode;
    
    private ProgressTintInfo() {}
  }
  
  private static class RefreshData
  {
    private static final int POOL_MAX = 24;
    private static final Pools.SynchronizedPool<RefreshData> sPool = new Pools.SynchronizedPool(24);
    public boolean animate;
    public boolean fromUser;
    public int id;
    public int progress;
    
    private RefreshData() {}
    
    public static RefreshData obtain(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      RefreshData localRefreshData1 = (RefreshData)sPool.acquire();
      RefreshData localRefreshData2 = localRefreshData1;
      if (localRefreshData1 == null) {
        localRefreshData2 = new RefreshData();
      }
      id = paramInt1;
      progress = paramInt2;
      fromUser = paramBoolean1;
      animate = paramBoolean2;
      return localRefreshData2;
    }
    
    public void recycle()
    {
      sPool.release(this);
    }
  }
  
  private class RefreshProgressRunnable
    implements Runnable
  {
    private RefreshProgressRunnable() {}
    
    public void run()
    {
      synchronized (ProgressBar.this)
      {
        int i = mRefreshData.size();
        for (int j = 0; j < i; j++)
        {
          ProgressBar.RefreshData localRefreshData = (ProgressBar.RefreshData)mRefreshData.get(j);
          ProgressBar.this.doRefreshProgress(id, progress, fromUser, true, animate);
          localRefreshData.recycle();
        }
        mRefreshData.clear();
        ProgressBar.access$302(ProgressBar.this, false);
        return;
      }
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ProgressBar.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgressBar.SavedState(paramAnonymousParcel, null);
      }
      
      public ProgressBar.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ProgressBar.SavedState[paramAnonymousInt];
      }
    };
    int progress;
    int secondaryProgress;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      progress = paramParcel.readInt();
      secondaryProgress = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(progress);
      paramParcel.writeInt(secondaryProgress);
    }
  }
}
