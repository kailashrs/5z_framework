package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.Op;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

final class SmartSelectSprite
{
  private static final int CORNER_DURATION = 50;
  private static final int EXPAND_DURATION = 300;
  static final Comparator<RectF> RECTANGLE_COMPARATOR = Comparator.comparingDouble(_..Lambda.SmartSelectSprite.c8eqlh2kO_X0luLU2BexwK921WA.INSTANCE).thenComparingDouble(_..Lambda.SmartSelectSprite.mdkXIT1_UNlJQMaziE_E815aIKE.INSTANCE);
  private Animator mActiveAnimator = null;
  private final Interpolator mCornerInterpolator;
  private Drawable mExistingDrawable = null;
  private RectangleList mExistingRectangleList = null;
  private final Interpolator mExpandInterpolator;
  private final int mFillColor;
  private final Runnable mInvalidator;
  
  SmartSelectSprite(Context paramContext, int paramInt, Runnable paramRunnable)
  {
    mExpandInterpolator = AnimationUtils.loadInterpolator(paramContext, 17563661);
    mCornerInterpolator = AnimationUtils.loadInterpolator(paramContext, 17563663);
    mFillColor = paramInt;
    mInvalidator = ((Runnable)Preconditions.checkNotNull(paramRunnable));
  }
  
  private static boolean contains(RectF paramRectF, PointF paramPointF)
  {
    float f1 = x;
    float f2 = y;
    boolean bool;
    if ((f1 >= left) && (f1 <= right) && (f2 >= top) && (f2 <= bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private Animator createAnimator(RectangleList paramRectangleList, float paramFloat1, float paramFloat2, List<Animator> paramList, ValueAnimator.AnimatorUpdateListener paramAnimatorUpdateListener, Runnable paramRunnable)
  {
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramRectangleList, "rightBoundary", new float[] { paramFloat2, paramRectangleList.getTotalWidth() });
    paramRectangleList = ObjectAnimator.ofFloat(paramRectangleList, "leftBoundary", new float[] { paramFloat1, 0.0F });
    localObjectAnimator.setDuration(300L);
    paramRectangleList.setDuration(300L);
    localObjectAnimator.addUpdateListener(paramAnimatorUpdateListener);
    paramRectangleList.addUpdateListener(paramAnimatorUpdateListener);
    localObjectAnimator.setInterpolator(mExpandInterpolator);
    paramRectangleList.setInterpolator(mExpandInterpolator);
    paramAnimatorUpdateListener = new AnimatorSet();
    paramAnimatorUpdateListener.playTogether(paramList);
    paramList = new AnimatorSet();
    paramList.playTogether(new Animator[] { paramRectangleList, localObjectAnimator });
    paramRectangleList = new AnimatorSet();
    paramRectangleList.playSequentially(new Animator[] { paramList, paramAnimatorUpdateListener });
    setUpAnimatorListener(paramRectangleList, paramRunnable);
    return paramRectangleList;
  }
  
  private ObjectAnimator createCornerAnimator(RoundedRectangleShape paramRoundedRectangleShape, ValueAnimator.AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    paramRoundedRectangleShape = ObjectAnimator.ofFloat(paramRoundedRectangleShape, "roundRatio", new float[] { paramRoundedRectangleShape.getRoundRatio(), 0.0F });
    paramRoundedRectangleShape.setDuration(50L);
    paramRoundedRectangleShape.addUpdateListener(paramAnimatorUpdateListener);
    paramRoundedRectangleShape.setInterpolator(mCornerInterpolator);
    return paramRoundedRectangleShape;
  }
  
  private static int[] generateDirections(RectangleWithTextSelectionLayout paramRectangleWithTextSelectionLayout, List<RectangleWithTextSelectionLayout> paramList)
  {
    int[] arrayOfInt = new int[paramList.size()];
    int i = paramList.indexOf(paramRectangleWithTextSelectionLayout);
    for (int j = 0; j < i - 1; j++) {
      arrayOfInt[j] = -1;
    }
    if (paramList.size() == 1) {
      arrayOfInt[i] = 0;
    } else if (i == 0) {
      arrayOfInt[i] = -1;
    } else if (i == paramList.size() - 1) {
      arrayOfInt[i] = 1;
    } else {
      arrayOfInt[i] = 0;
    }
    for (j = i + 1; j < arrayOfInt.length; j++) {
      arrayOfInt[j] = 1;
    }
    return arrayOfInt;
  }
  
  private void removeExistingDrawables()
  {
    mExistingDrawable = null;
    mExistingRectangleList = null;
    mInvalidator.run();
  }
  
  private void setUpAnimatorListener(Animator paramAnimator, final Runnable paramRunnable)
  {
    paramAnimator.addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator) {}
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        mExistingRectangleList.setDisplayType(1);
        mInvalidator.run();
        paramRunnable.run();
      }
      
      public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
      
      public void onAnimationStart(Animator paramAnonymousAnimator) {}
    });
  }
  
  public void cancelAnimation()
  {
    if (mActiveAnimator != null)
    {
      mActiveAnimator.cancel();
      mActiveAnimator = null;
      removeExistingDrawables();
    }
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mExistingDrawable != null) {
      mExistingDrawable.draw(paramCanvas);
    }
  }
  
  public boolean isAnimationActive()
  {
    boolean bool;
    if ((mActiveAnimator != null) && (mActiveAnimator.isRunning())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void startAnimation(PointF paramPointF, List<RectangleWithTextSelectionLayout> paramList, Runnable paramRunnable)
  {
    cancelAnimation();
    _..Lambda.SmartSelectSprite.2pck5xTffRWoiD4l_tkO_IIf5iM local2pck5xTffRWoiD4l_tkO_IIf5iM = new _..Lambda.SmartSelectSprite.2pck5xTffRWoiD4l_tkO_IIf5iM(this);
    int i = paramList.size();
    ArrayList localArrayList1 = new ArrayList(i);
    ArrayList localArrayList2 = new ArrayList(i);
    RectF localRectF1 = null;
    int j = 0;
    Iterator localIterator = paramList.iterator();
    Object localObject;
    for (;;)
    {
      localObject = localRectF1;
      if (!localIterator.hasNext()) {
        break;
      }
      localObject = (RectangleWithTextSelectionLayout)localIterator.next();
      RectF localRectF2 = ((RectangleWithTextSelectionLayout)localObject).getRectangle();
      if (contains(localRectF2, paramPointF)) {
        break;
      }
      j = (int)(j + localRectF2.width());
    }
    if (localObject != null)
    {
      int k = (int)(j + (x - getRectangleleft));
      paramPointF = generateDirections((RectangleWithTextSelectionLayout)localObject, paramList);
      for (j = 0; j < i; j++)
      {
        localObject = (RectangleWithTextSelectionLayout)paramList.get(j);
        localRectF1 = ((RectangleWithTextSelectionLayout)localObject).getRectangle();
        int m = paramPointF[j];
        boolean bool;
        if (((RectangleWithTextSelectionLayout)localObject).getTextSelectionLayout() == 0) {
          bool = true;
        } else {
          bool = false;
        }
        localObject = new RoundedRectangleShape(localRectF1, m, bool, null);
        localArrayList2.add(createCornerAnimator((RoundedRectangleShape)localObject, local2pck5xTffRWoiD4l_tkO_IIf5iM));
        localArrayList1.add(localObject);
      }
      localObject = new RectangleList(localArrayList1, null);
      paramPointF = new ShapeDrawable((Shape)localObject);
      paramList = paramPointF.getPaint();
      paramList.setColor(mFillColor);
      paramList.setStyle(Paint.Style.FILL);
      mExistingRectangleList = ((RectangleList)localObject);
      mExistingDrawable = paramPointF;
      mActiveAnimator = createAnimator((RectangleList)localObject, k, k, localArrayList2, local2pck5xTffRWoiD4l_tkO_IIf5iM, paramRunnable);
      mActiveAnimator.start();
      return;
    }
    throw new IllegalArgumentException("Center point is not inside any of the rectangles!");
  }
  
  private static final class RectangleList
    extends Shape
  {
    private static final String PROPERTY_LEFT_BOUNDARY = "leftBoundary";
    private static final String PROPERTY_RIGHT_BOUNDARY = "rightBoundary";
    private int mDisplayType = 0;
    private final Path mOutlinePolygonPath;
    private final List<SmartSelectSprite.RoundedRectangleShape> mRectangles;
    private final List<SmartSelectSprite.RoundedRectangleShape> mReversedRectangles;
    
    private RectangleList(List<SmartSelectSprite.RoundedRectangleShape> paramList)
    {
      mRectangles = new ArrayList(paramList);
      mReversedRectangles = new ArrayList(paramList);
      Collections.reverse(mReversedRectangles);
      mOutlinePolygonPath = generateOutlinePolygonPath(paramList);
    }
    
    private void drawPolygon(Canvas paramCanvas, Paint paramPaint)
    {
      paramCanvas.drawPath(mOutlinePolygonPath, paramPaint);
    }
    
    private void drawRectangles(Canvas paramCanvas, Paint paramPaint)
    {
      Iterator localIterator = mRectangles.iterator();
      while (localIterator.hasNext()) {
        ((SmartSelectSprite.RoundedRectangleShape)localIterator.next()).draw(paramCanvas, paramPaint);
      }
    }
    
    private static Path generateOutlinePolygonPath(List<SmartSelectSprite.RoundedRectangleShape> paramList)
    {
      Path localPath1 = new Path();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        paramList = (SmartSelectSprite.RoundedRectangleShape)localIterator.next();
        Path localPath2 = new Path();
        localPath2.addRect(SmartSelectSprite.RoundedRectangleShape.access$300(paramList), Path.Direction.CW);
        localPath1.op(localPath2, Path.Op.UNION);
      }
      return localPath1;
    }
    
    private int getTotalWidth()
    {
      int i = 0;
      Iterator localIterator = mRectangles.iterator();
      while (localIterator.hasNext())
      {
        SmartSelectSprite.RoundedRectangleShape localRoundedRectangleShape = (SmartSelectSprite.RoundedRectangleShape)localIterator.next();
        i = (int)(i + SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape));
      }
      return i;
    }
    
    private void setLeftBoundary(float paramFloat)
    {
      float f1 = getTotalWidth();
      Iterator localIterator = mReversedRectangles.iterator();
      while (localIterator.hasNext())
      {
        SmartSelectSprite.RoundedRectangleShape localRoundedRectangleShape = (SmartSelectSprite.RoundedRectangleShape)localIterator.next();
        float f2 = f1 - SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape);
        if (paramFloat < f2) {
          SmartSelectSprite.RoundedRectangleShape.access$100(localRoundedRectangleShape, 0.0F);
        } else if (paramFloat > f1) {
          SmartSelectSprite.RoundedRectangleShape.access$100(localRoundedRectangleShape, SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape));
        } else {
          SmartSelectSprite.RoundedRectangleShape.access$100(localRoundedRectangleShape, SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape) - f1 + paramFloat);
        }
        f1 = f2;
      }
    }
    
    private void setRightBoundary(float paramFloat)
    {
      float f1 = 0.0F;
      Iterator localIterator = mRectangles.iterator();
      while (localIterator.hasNext())
      {
        SmartSelectSprite.RoundedRectangleShape localRoundedRectangleShape = (SmartSelectSprite.RoundedRectangleShape)localIterator.next();
        float f2 = SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape) + f1;
        if (f2 < paramFloat) {
          SmartSelectSprite.RoundedRectangleShape.access$200(localRoundedRectangleShape, SmartSelectSprite.RoundedRectangleShape.access$000(localRoundedRectangleShape));
        } else if (f1 > paramFloat) {
          SmartSelectSprite.RoundedRectangleShape.access$200(localRoundedRectangleShape, 0.0F);
        } else {
          SmartSelectSprite.RoundedRectangleShape.access$200(localRoundedRectangleShape, paramFloat - f1);
        }
        f1 = f2;
      }
    }
    
    public void draw(Canvas paramCanvas, Paint paramPaint)
    {
      if (mDisplayType == 1) {
        drawPolygon(paramCanvas, paramPaint);
      } else {
        drawRectangles(paramCanvas, paramPaint);
      }
    }
    
    void setDisplayType(int paramInt)
    {
      mDisplayType = paramInt;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private static @interface DisplayType
    {
      public static final int POLYGON = 1;
      public static final int RECTANGLES = 0;
    }
  }
  
  static final class RectangleWithTextSelectionLayout
  {
    private final RectF mRectangle;
    private final int mTextSelectionLayout;
    
    RectangleWithTextSelectionLayout(RectF paramRectF, int paramInt)
    {
      mRectangle = ((RectF)Preconditions.checkNotNull(paramRectF));
      mTextSelectionLayout = paramInt;
    }
    
    public RectF getRectangle()
    {
      return mRectangle;
    }
    
    public int getTextSelectionLayout()
    {
      return mTextSelectionLayout;
    }
  }
  
  private static final class RoundedRectangleShape
    extends Shape
  {
    private static final String PROPERTY_ROUND_RATIO = "roundRatio";
    private final RectF mBoundingRectangle;
    private final float mBoundingWidth;
    private final Path mClipPath = new Path();
    private final RectF mDrawRect = new RectF();
    private final int mExpansionDirection;
    private final boolean mInverted;
    private float mLeftBoundary = 0.0F;
    private float mRightBoundary = 0.0F;
    private float mRoundRatio = 1.0F;
    
    private RoundedRectangleShape(RectF paramRectF, int paramInt, boolean paramBoolean)
    {
      mBoundingRectangle = new RectF(paramRectF);
      mBoundingWidth = paramRectF.width();
      boolean bool;
      if ((paramBoolean) && (paramInt != 0)) {
        bool = true;
      } else {
        bool = false;
      }
      mInverted = bool;
      if (paramBoolean) {
        mExpansionDirection = invert(paramInt);
      } else {
        mExpansionDirection = paramInt;
      }
      if (paramRectF.height() > paramRectF.width()) {
        setRoundRatio(0.0F);
      } else {
        setRoundRatio(1.0F);
      }
    }
    
    private float getAdjustedCornerRadius()
    {
      return getCornerRadius() * mRoundRatio;
    }
    
    private float getBoundingWidth()
    {
      return (int)(mBoundingRectangle.width() + getCornerRadius());
    }
    
    private float getCornerRadius()
    {
      return Math.min(mBoundingRectangle.width(), mBoundingRectangle.height());
    }
    
    private static int invert(int paramInt)
    {
      return paramInt * -1;
    }
    
    private void setEndBoundary(float paramFloat)
    {
      if (mInverted) {
        mLeftBoundary = (mBoundingWidth - paramFloat);
      } else {
        mRightBoundary = paramFloat;
      }
    }
    
    private void setStartBoundary(float paramFloat)
    {
      if (mInverted) {
        mRightBoundary = (mBoundingWidth - paramFloat);
      } else {
        mLeftBoundary = paramFloat;
      }
    }
    
    public void draw(Canvas paramCanvas, Paint paramPaint)
    {
      if (mLeftBoundary == mRightBoundary) {
        return;
      }
      float f1 = getCornerRadius();
      float f2 = getAdjustedCornerRadius();
      mDrawRect.set(mBoundingRectangle);
      mDrawRect.left = (mBoundingRectangle.left + mLeftBoundary - f1 / 2.0F);
      mDrawRect.right = (mBoundingRectangle.left + mRightBoundary + f1 / 2.0F);
      paramCanvas.save();
      mClipPath.reset();
      mClipPath.addRoundRect(mDrawRect, f2, f2, Path.Direction.CW);
      paramCanvas.clipPath(mClipPath);
      paramCanvas.drawRect(mBoundingRectangle, paramPaint);
      paramCanvas.restore();
    }
    
    float getRoundRatio()
    {
      return mRoundRatio;
    }
    
    void setRoundRatio(float paramFloat)
    {
      mRoundRatio = paramFloat;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private static @interface ExpansionDirection
    {
      public static final int CENTER = 0;
      public static final int LEFT = -1;
      public static final int RIGHT = 1;
    }
  }
}
