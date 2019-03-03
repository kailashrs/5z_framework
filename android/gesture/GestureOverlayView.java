package android.gesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.android.internal.R.styleable;
import java.util.ArrayList;

public class GestureOverlayView
  extends FrameLayout
{
  private static final boolean DITHER_FLAG = true;
  private static final int FADE_ANIMATION_RATE = 16;
  private static final boolean GESTURE_RENDERING_ANTIALIAS = true;
  public static final int GESTURE_STROKE_TYPE_MULTIPLE = 1;
  public static final int GESTURE_STROKE_TYPE_SINGLE = 0;
  public static final int ORIENTATION_HORIZONTAL = 0;
  public static final int ORIENTATION_VERTICAL = 1;
  private int mCertainGestureColor = 65280;
  private int mCurrentColor;
  private Gesture mCurrentGesture;
  private float mCurveEndX;
  private float mCurveEndY;
  private long mFadeDuration = 150L;
  private boolean mFadeEnabled = true;
  private long mFadeOffset = 420L;
  private float mFadingAlpha = 1.0F;
  private boolean mFadingHasStarted;
  private final FadeOutRunnable mFadingOut = new FadeOutRunnable(null);
  private long mFadingStart;
  private final Paint mGesturePaint = new Paint();
  private float mGestureStrokeAngleThreshold = 40.0F;
  private float mGestureStrokeLengthThreshold = 50.0F;
  private float mGestureStrokeSquarenessTreshold = 0.275F;
  private int mGestureStrokeType = 0;
  private float mGestureStrokeWidth = 12.0F;
  private boolean mGestureVisible = true;
  private boolean mHandleGestureActions;
  private boolean mInterceptEvents = true;
  private final AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
  private final Rect mInvalidRect = new Rect();
  private int mInvalidateExtraBorder = 10;
  private boolean mIsFadingOut = false;
  private boolean mIsGesturing = false;
  private boolean mIsListeningForGestures;
  private final ArrayList<OnGestureListener> mOnGestureListeners = new ArrayList();
  private final ArrayList<OnGesturePerformedListener> mOnGesturePerformedListeners = new ArrayList();
  private final ArrayList<OnGesturingListener> mOnGesturingListeners = new ArrayList();
  private int mOrientation = 1;
  private final Path mPath = new Path();
  private boolean mPreviousWasGesturing = false;
  private boolean mResetGesture;
  private final ArrayList<GesturePoint> mStrokeBuffer = new ArrayList(100);
  private float mTotalLength;
  private int mUncertainGestureColor = 1224736512;
  private float mX;
  private float mY;
  
  public GestureOverlayView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public GestureOverlayView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 17891401);
  }
  
  public GestureOverlayView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public GestureOverlayView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.GestureOverlayView, paramInt1, paramInt2);
    mGestureStrokeWidth = paramContext.getFloat(1, mGestureStrokeWidth);
    mInvalidateExtraBorder = Math.max(1, (int)mGestureStrokeWidth - 1);
    mCertainGestureColor = paramContext.getColor(2, mCertainGestureColor);
    mUncertainGestureColor = paramContext.getColor(3, mUncertainGestureColor);
    mFadeDuration = paramContext.getInt(5, (int)mFadeDuration);
    mFadeOffset = paramContext.getInt(4, (int)mFadeOffset);
    mGestureStrokeType = paramContext.getInt(6, mGestureStrokeType);
    mGestureStrokeLengthThreshold = paramContext.getFloat(7, mGestureStrokeLengthThreshold);
    mGestureStrokeAngleThreshold = paramContext.getFloat(9, mGestureStrokeAngleThreshold);
    mGestureStrokeSquarenessTreshold = paramContext.getFloat(8, mGestureStrokeSquarenessTreshold);
    mInterceptEvents = paramContext.getBoolean(10, mInterceptEvents);
    mFadeEnabled = paramContext.getBoolean(11, mFadeEnabled);
    mOrientation = paramContext.getInt(0, mOrientation);
    paramContext.recycle();
    init();
  }
  
  private void cancelGesture(MotionEvent paramMotionEvent)
  {
    ArrayList localArrayList = mOnGestureListeners;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((OnGestureListener)localArrayList.get(j)).onGestureCancelled(this, paramMotionEvent);
    }
    clear(false);
  }
  
  private void clear(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    setPaintAlpha(255);
    removeCallbacks(mFadingOut);
    mResetGesture = false;
    mFadingOut.fireActionPerformed = paramBoolean2;
    mFadingOut.resetMultipleStrokes = false;
    if ((paramBoolean1) && (mCurrentGesture != null))
    {
      mFadingAlpha = 1.0F;
      mIsFadingOut = true;
      mFadingHasStarted = false;
      mFadingStart = (AnimationUtils.currentAnimationTimeMillis() + mFadeOffset);
      postDelayed(mFadingOut, mFadeOffset);
    }
    else
    {
      mFadingAlpha = 1.0F;
      mIsFadingOut = false;
      mFadingHasStarted = false;
      if (paramBoolean3)
      {
        mCurrentGesture = null;
        mPath.rewind();
        invalidate();
      }
      else if (paramBoolean2)
      {
        postDelayed(mFadingOut, mFadeOffset);
      }
      else if (mGestureStrokeType == 1)
      {
        mFadingOut.resetMultipleStrokes = true;
        postDelayed(mFadingOut, mFadeOffset);
      }
      else
      {
        mCurrentGesture = null;
        mPath.rewind();
        invalidate();
      }
    }
  }
  
  private void fireOnGesturePerformed()
  {
    ArrayList localArrayList = mOnGesturePerformedListeners;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((OnGesturePerformedListener)localArrayList.get(j)).onGesturePerformed(this, mCurrentGesture);
    }
  }
  
  private void init()
  {
    setWillNotDraw(false);
    Paint localPaint = mGesturePaint;
    localPaint.setAntiAlias(true);
    localPaint.setColor(mCertainGestureColor);
    localPaint.setStyle(Paint.Style.STROKE);
    localPaint.setStrokeJoin(Paint.Join.ROUND);
    localPaint.setStrokeCap(Paint.Cap.ROUND);
    localPaint.setStrokeWidth(mGestureStrokeWidth);
    localPaint.setDither(true);
    mCurrentColor = mCertainGestureColor;
    setPaintAlpha(255);
  }
  
  private boolean processEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default: 
      break;
    case 3: 
      if (mIsListeningForGestures)
      {
        touchUp(paramMotionEvent, true);
        invalidate();
        return true;
      }
      break;
    case 2: 
      if (mIsListeningForGestures)
      {
        paramMotionEvent = touchMove(paramMotionEvent);
        if (paramMotionEvent != null) {
          invalidate(paramMotionEvent);
        }
        return true;
      }
      break;
    case 1: 
      if (mIsListeningForGestures)
      {
        touchUp(paramMotionEvent, false);
        invalidate();
        return true;
      }
      break;
    case 0: 
      touchDown(paramMotionEvent);
      invalidate();
      return true;
    }
    return false;
  }
  
  private void setCurrentColor(int paramInt)
  {
    mCurrentColor = paramInt;
    if (mFadingHasStarted) {
      setPaintAlpha((int)(255.0F * mFadingAlpha));
    } else {
      setPaintAlpha(255);
    }
    invalidate();
  }
  
  private void setPaintAlpha(int paramInt)
  {
    int i = mCurrentColor;
    mGesturePaint.setColor(mCurrentColor << 8 >>> 8 | (i >>> 24) * (paramInt + (paramInt >> 7)) >> 8 << 24);
  }
  
  private void touchDown(MotionEvent paramMotionEvent)
  {
    mIsListeningForGestures = true;
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    mX = f1;
    mY = f2;
    mTotalLength = 0.0F;
    int i = 0;
    mIsGesturing = false;
    if ((mGestureStrokeType != 0) && (!mResetGesture))
    {
      if (((mCurrentGesture == null) || (mCurrentGesture.getStrokesCount() == 0)) && (mHandleGestureActions)) {
        setCurrentColor(mUncertainGestureColor);
      }
    }
    else
    {
      if (mHandleGestureActions) {
        setCurrentColor(mUncertainGestureColor);
      }
      mResetGesture = false;
      mCurrentGesture = null;
      mPath.rewind();
    }
    if (mFadingHasStarted)
    {
      cancelClearAnimation();
    }
    else if (mIsFadingOut)
    {
      setPaintAlpha(255);
      mIsFadingOut = false;
      mFadingHasStarted = false;
      removeCallbacks(mFadingOut);
    }
    if (mCurrentGesture == null) {
      mCurrentGesture = new Gesture();
    }
    mStrokeBuffer.add(new GesturePoint(f1, f2, paramMotionEvent.getEventTime()));
    mPath.moveTo(f1, f2);
    int j = mInvalidateExtraBorder;
    mInvalidRect.set((int)f1 - j, (int)f2 - j, (int)f1 + j, (int)f2 + j);
    mCurveEndX = f1;
    mCurveEndY = f2;
    ArrayList localArrayList = mOnGestureListeners;
    j = localArrayList.size();
    while (i < j)
    {
      ((OnGestureListener)localArrayList.get(i)).onGestureStarted(this, paramMotionEvent);
      i++;
    }
  }
  
  private Rect touchMove(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    float f3 = mX;
    float f4 = mY;
    float f5 = Math.abs(f1 - f3);
    float f6 = Math.abs(f2 - f4);
    Rect localRect;
    if ((f5 < 3.0F) && (f6 < 3.0F))
    {
      localRect = null;
    }
    else
    {
      localRect = mInvalidRect;
      int i = mInvalidateExtraBorder;
      localRect.set((int)mCurveEndX - i, (int)mCurveEndY - i, (int)mCurveEndX + i, (int)mCurveEndY + i);
      float f7 = (f1 + f3) / 2.0F;
      mCurveEndX = f7;
      float f8 = (f2 + f4) / 2.0F;
      mCurveEndY = f8;
      mPath.quadTo(f3, f4, f7, f8);
      localRect.union((int)f3 - i, (int)f4 - i, (int)f3 + i, (int)f4 + i);
      localRect.union((int)f7 - i, (int)f8 - i, (int)f7 + i, (int)f8 + i);
      mX = f1;
      mY = f2;
      mStrokeBuffer.add(new GesturePoint(f1, f2, paramMotionEvent.getEventTime()));
      if ((mHandleGestureActions) && (!mIsGesturing))
      {
        mTotalLength += (float)Math.hypot(f5, f6);
        if (mTotalLength > mGestureStrokeLengthThreshold)
        {
          localObject = GestureUtils.computeOrientedBoundingBox(mStrokeBuffer);
          f7 = Math.abs(orientation);
          f8 = f7;
          if (f7 > 90.0F) {
            f8 = 180.0F - f7;
          }
          if ((squareness > mGestureStrokeSquarenessTreshold) || (mOrientation == 1 ? f8 < mGestureStrokeAngleThreshold : f8 > mGestureStrokeAngleThreshold))
          {
            mIsGesturing = true;
            setCurrentColor(mCertainGestureColor);
            localObject = mOnGesturingListeners;
            j = ((ArrayList)localObject).size();
            for (i = 0; i < j; i++) {
              ((OnGesturingListener)((ArrayList)localObject).get(i)).onGesturingStarted(this);
            }
          }
        }
      }
      Object localObject = mOnGestureListeners;
      int j = ((ArrayList)localObject).size();
      for (i = 0; i < j; i++) {
        ((OnGestureListener)((ArrayList)localObject).get(i)).onGesture(this, paramMotionEvent);
      }
    }
    return localRect;
  }
  
  private void touchUp(MotionEvent paramMotionEvent, boolean paramBoolean)
  {
    int i = 0;
    mIsListeningForGestures = false;
    if (mCurrentGesture != null)
    {
      mCurrentGesture.addStroke(new GestureStroke(mStrokeBuffer));
      if (!paramBoolean)
      {
        ArrayList localArrayList = mOnGestureListeners;
        j = localArrayList.size();
        for (k = 0; k < j; k++) {
          ((OnGestureListener)localArrayList.get(k)).onGestureEnded(this, paramMotionEvent);
        }
        paramBoolean = mHandleGestureActions;
        boolean bool = true;
        if ((paramBoolean) && (mFadeEnabled)) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        if ((!mHandleGestureActions) || (!mIsGesturing)) {
          bool = false;
        }
        clear(paramBoolean, bool, false);
      }
      else
      {
        cancelGesture(paramMotionEvent);
      }
    }
    else
    {
      cancelGesture(paramMotionEvent);
    }
    mStrokeBuffer.clear();
    mPreviousWasGesturing = mIsGesturing;
    mIsGesturing = false;
    paramMotionEvent = mOnGesturingListeners;
    int j = paramMotionEvent.size();
    for (int k = i; k < j; k++) {
      ((OnGesturingListener)paramMotionEvent.get(k)).onGesturingEnded(this);
    }
  }
  
  public void addOnGestureListener(OnGestureListener paramOnGestureListener)
  {
    mOnGestureListeners.add(paramOnGestureListener);
  }
  
  public void addOnGesturePerformedListener(OnGesturePerformedListener paramOnGesturePerformedListener)
  {
    mOnGesturePerformedListeners.add(paramOnGesturePerformedListener);
    if (mOnGesturePerformedListeners.size() > 0) {
      mHandleGestureActions = true;
    }
  }
  
  public void addOnGesturingListener(OnGesturingListener paramOnGesturingListener)
  {
    mOnGesturingListeners.add(paramOnGesturingListener);
  }
  
  public void cancelClearAnimation()
  {
    setPaintAlpha(255);
    mIsFadingOut = false;
    mFadingHasStarted = false;
    removeCallbacks(mFadingOut);
    mPath.rewind();
    mCurrentGesture = null;
  }
  
  public void cancelGesture()
  {
    int i = 0;
    mIsListeningForGestures = false;
    mCurrentGesture.addStroke(new GestureStroke(mStrokeBuffer));
    long l = SystemClock.uptimeMillis();
    Object localObject = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
    ArrayList localArrayList = mOnGestureListeners;
    int j = localArrayList.size();
    for (int k = 0; k < j; k++) {
      ((OnGestureListener)localArrayList.get(k)).onGestureCancelled(this, (MotionEvent)localObject);
    }
    ((MotionEvent)localObject).recycle();
    clear(false);
    mIsGesturing = false;
    mPreviousWasGesturing = false;
    mStrokeBuffer.clear();
    localObject = mOnGesturingListeners;
    j = ((ArrayList)localObject).size();
    for (k = i; k < j; k++) {
      ((OnGesturingListener)((ArrayList)localObject).get(k)).onGesturingEnded(this);
    }
  }
  
  public void clear(boolean paramBoolean)
  {
    clear(paramBoolean, false, true);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (isEnabled())
    {
      int i;
      if (((mIsGesturing) || ((mCurrentGesture != null) && (mCurrentGesture.getStrokesCount() > 0) && (mPreviousWasGesturing))) && (mInterceptEvents)) {
        i = 1;
      } else {
        i = 0;
      }
      processEvent(paramMotionEvent);
      if (i != 0) {
        paramMotionEvent.setAction(3);
      }
      super.dispatchTouchEvent(paramMotionEvent);
      return true;
    }
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if ((mCurrentGesture != null) && (mGestureVisible)) {
      paramCanvas.drawPath(mPath, mGesturePaint);
    }
  }
  
  public ArrayList<GesturePoint> getCurrentStroke()
  {
    return mStrokeBuffer;
  }
  
  public long getFadeOffset()
  {
    return mFadeOffset;
  }
  
  public Gesture getGesture()
  {
    return mCurrentGesture;
  }
  
  public int getGestureColor()
  {
    return mCertainGestureColor;
  }
  
  public Paint getGesturePaint()
  {
    return mGesturePaint;
  }
  
  public Path getGesturePath()
  {
    return mPath;
  }
  
  public Path getGesturePath(Path paramPath)
  {
    paramPath.set(mPath);
    return paramPath;
  }
  
  public float getGestureStrokeAngleThreshold()
  {
    return mGestureStrokeAngleThreshold;
  }
  
  public float getGestureStrokeLengthThreshold()
  {
    return mGestureStrokeLengthThreshold;
  }
  
  public float getGestureStrokeSquarenessTreshold()
  {
    return mGestureStrokeSquarenessTreshold;
  }
  
  public int getGestureStrokeType()
  {
    return mGestureStrokeType;
  }
  
  public float getGestureStrokeWidth()
  {
    return mGestureStrokeWidth;
  }
  
  public int getOrientation()
  {
    return mOrientation;
  }
  
  public int getUncertainGestureColor()
  {
    return mUncertainGestureColor;
  }
  
  public boolean isEventsInterceptionEnabled()
  {
    return mInterceptEvents;
  }
  
  public boolean isFadeEnabled()
  {
    return mFadeEnabled;
  }
  
  public boolean isGestureVisible()
  {
    return mGestureVisible;
  }
  
  public boolean isGesturing()
  {
    return mIsGesturing;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    cancelClearAnimation();
  }
  
  public void removeAllOnGestureListeners()
  {
    mOnGestureListeners.clear();
  }
  
  public void removeAllOnGesturePerformedListeners()
  {
    mOnGesturePerformedListeners.clear();
    mHandleGestureActions = false;
  }
  
  public void removeAllOnGesturingListeners()
  {
    mOnGesturingListeners.clear();
  }
  
  public void removeOnGestureListener(OnGestureListener paramOnGestureListener)
  {
    mOnGestureListeners.remove(paramOnGestureListener);
  }
  
  public void removeOnGesturePerformedListener(OnGesturePerformedListener paramOnGesturePerformedListener)
  {
    mOnGesturePerformedListeners.remove(paramOnGesturePerformedListener);
    if (mOnGesturePerformedListeners.size() <= 0) {
      mHandleGestureActions = false;
    }
  }
  
  public void removeOnGesturingListener(OnGesturingListener paramOnGesturingListener)
  {
    mOnGesturingListeners.remove(paramOnGesturingListener);
  }
  
  public void setEventsInterceptionEnabled(boolean paramBoolean)
  {
    mInterceptEvents = paramBoolean;
  }
  
  public void setFadeEnabled(boolean paramBoolean)
  {
    mFadeEnabled = paramBoolean;
  }
  
  public void setFadeOffset(long paramLong)
  {
    mFadeOffset = paramLong;
  }
  
  public void setGesture(Gesture paramGesture)
  {
    if (mCurrentGesture != null) {
      clear(false);
    }
    setCurrentColor(mCertainGestureColor);
    mCurrentGesture = paramGesture;
    paramGesture = mCurrentGesture.toPath();
    RectF localRectF = new RectF();
    paramGesture.computeBounds(localRectF, true);
    mPath.rewind();
    mPath.addPath(paramGesture, -left + (getWidth() - localRectF.width()) / 2.0F, -top + (getHeight() - localRectF.height()) / 2.0F);
    mResetGesture = true;
    invalidate();
  }
  
  public void setGestureColor(int paramInt)
  {
    mCertainGestureColor = paramInt;
  }
  
  public void setGestureStrokeAngleThreshold(float paramFloat)
  {
    mGestureStrokeAngleThreshold = paramFloat;
  }
  
  public void setGestureStrokeLengthThreshold(float paramFloat)
  {
    mGestureStrokeLengthThreshold = paramFloat;
  }
  
  public void setGestureStrokeSquarenessTreshold(float paramFloat)
  {
    mGestureStrokeSquarenessTreshold = paramFloat;
  }
  
  public void setGestureStrokeType(int paramInt)
  {
    mGestureStrokeType = paramInt;
  }
  
  public void setGestureStrokeWidth(float paramFloat)
  {
    mGestureStrokeWidth = paramFloat;
    mInvalidateExtraBorder = Math.max(1, (int)paramFloat - 1);
    mGesturePaint.setStrokeWidth(paramFloat);
  }
  
  public void setGestureVisible(boolean paramBoolean)
  {
    mGestureVisible = paramBoolean;
  }
  
  public void setOrientation(int paramInt)
  {
    mOrientation = paramInt;
  }
  
  public void setUncertainGestureColor(int paramInt)
  {
    mUncertainGestureColor = paramInt;
  }
  
  private class FadeOutRunnable
    implements Runnable
  {
    boolean fireActionPerformed;
    boolean resetMultipleStrokes;
    
    private FadeOutRunnable() {}
    
    public void run()
    {
      if (mIsFadingOut)
      {
        long l = AnimationUtils.currentAnimationTimeMillis() - mFadingStart;
        if (l > mFadeDuration)
        {
          if (fireActionPerformed) {
            GestureOverlayView.this.fireOnGesturePerformed();
          }
          GestureOverlayView.access$502(GestureOverlayView.this, false);
          GestureOverlayView.access$102(GestureOverlayView.this, false);
          GestureOverlayView.access$602(GestureOverlayView.this, false);
          mPath.rewind();
          GestureOverlayView.access$802(GestureOverlayView.this, null);
          GestureOverlayView.this.setPaintAlpha(255);
        }
        else
        {
          GestureOverlayView.access$602(GestureOverlayView.this, true);
          float f = Math.max(0.0F, Math.min(1.0F, (float)l / (float)mFadeDuration));
          GestureOverlayView.access$1002(GestureOverlayView.this, 1.0F - mInterpolator.getInterpolation(f));
          GestureOverlayView.this.setPaintAlpha((int)(255.0F * mFadingAlpha));
          postDelayed(this, 16L);
        }
      }
      else if (resetMultipleStrokes)
      {
        GestureOverlayView.access$1202(GestureOverlayView.this, true);
      }
      else
      {
        GestureOverlayView.this.fireOnGesturePerformed();
        GestureOverlayView.access$602(GestureOverlayView.this, false);
        mPath.rewind();
        GestureOverlayView.access$802(GestureOverlayView.this, null);
        GestureOverlayView.access$502(GestureOverlayView.this, false);
        GestureOverlayView.this.setPaintAlpha(255);
      }
      invalidate();
    }
  }
  
  public static abstract interface OnGestureListener
  {
    public abstract void onGesture(GestureOverlayView paramGestureOverlayView, MotionEvent paramMotionEvent);
    
    public abstract void onGestureCancelled(GestureOverlayView paramGestureOverlayView, MotionEvent paramMotionEvent);
    
    public abstract void onGestureEnded(GestureOverlayView paramGestureOverlayView, MotionEvent paramMotionEvent);
    
    public abstract void onGestureStarted(GestureOverlayView paramGestureOverlayView, MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnGesturePerformedListener
  {
    public abstract void onGesturePerformed(GestureOverlayView paramGestureOverlayView, Gesture paramGesture);
  }
  
  public static abstract interface OnGesturingListener
  {
    public abstract void onGesturingEnded(GestureOverlayView paramGestureOverlayView);
    
    public abstract void onGesturingStarted(GestureOverlayView paramGestureOverlayView);
  }
}
