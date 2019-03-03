package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.android.internal.R.styleable;
import java.util.Random;

public class GridLayoutAnimationController
  extends LayoutAnimationController
{
  public static final int DIRECTION_BOTTOM_TO_TOP = 2;
  public static final int DIRECTION_HORIZONTAL_MASK = 1;
  public static final int DIRECTION_LEFT_TO_RIGHT = 0;
  public static final int DIRECTION_RIGHT_TO_LEFT = 1;
  public static final int DIRECTION_TOP_TO_BOTTOM = 0;
  public static final int DIRECTION_VERTICAL_MASK = 2;
  public static final int PRIORITY_COLUMN = 1;
  public static final int PRIORITY_NONE = 0;
  public static final int PRIORITY_ROW = 2;
  private float mColumnDelay;
  private int mDirection;
  private int mDirectionPriority;
  private float mRowDelay;
  
  public GridLayoutAnimationController(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.GridLayoutAnimation);
    mColumnDelay = parseValuepeekValue0value;
    mRowDelay = parseValuepeekValue1value;
    mDirection = paramContext.getInt(2, 0);
    mDirectionPriority = paramContext.getInt(3, 0);
    paramContext.recycle();
  }
  
  public GridLayoutAnimationController(Animation paramAnimation)
  {
    this(paramAnimation, 0.5F, 0.5F);
  }
  
  public GridLayoutAnimationController(Animation paramAnimation, float paramFloat1, float paramFloat2)
  {
    super(paramAnimation);
    mColumnDelay = paramFloat1;
    mRowDelay = paramFloat2;
  }
  
  private int getTransformedColumnIndex(AnimationParameters paramAnimationParameters)
  {
    int i;
    switch (getOrder())
    {
    default: 
      i = column;
      break;
    case 2: 
      if (mRandomizer == null) {
        mRandomizer = new Random();
      }
      i = (int)(columnsCount * mRandomizer.nextFloat());
      break;
    case 1: 
      i = columnsCount - 1 - column;
    }
    int j = i;
    if ((mDirection & 0x1) == 1) {
      j = columnsCount - 1 - i;
    }
    return j;
  }
  
  private int getTransformedRowIndex(AnimationParameters paramAnimationParameters)
  {
    int i;
    switch (getOrder())
    {
    default: 
      i = row;
      break;
    case 2: 
      if (mRandomizer == null) {
        mRandomizer = new Random();
      }
      i = (int)(rowsCount * mRandomizer.nextFloat());
      break;
    case 1: 
      i = rowsCount - 1 - row;
    }
    int j = i;
    if ((mDirection & 0x2) == 2) {
      j = rowsCount - 1 - i;
    }
    return j;
  }
  
  public float getColumnDelay()
  {
    return mColumnDelay;
  }
  
  protected long getDelayForView(View paramView)
  {
    paramView = (AnimationParameters)getLayoutParamslayoutAnimationParameters;
    if (paramView == null) {
      return 0L;
    }
    int i = getTransformedColumnIndex(paramView);
    int j = getTransformedRowIndex(paramView);
    int k = rowsCount;
    int m = columnsCount;
    long l = mAnimation.getDuration();
    float f1 = mColumnDelay * (float)l;
    float f2 = mRowDelay * (float)l;
    if (mInterpolator == null) {
      mInterpolator = new LinearInterpolator();
    }
    switch (mDirectionPriority)
    {
    default: 
      l = (i * f1 + j * f2);
      f1 = m * f1 + k * f2;
      break;
    case 2: 
      l = (i * f1 + j * m * f1);
      f1 = m * f1 + k * m * f1;
      break;
    case 1: 
      l = (j * f2 + i * k * f2);
      f1 = k * f2 + m * k * f2;
    }
    f2 = (float)l / f1;
    return (mInterpolator.getInterpolation(f2) * f1);
  }
  
  public int getDirection()
  {
    return mDirection;
  }
  
  public int getDirectionPriority()
  {
    return mDirectionPriority;
  }
  
  public float getRowDelay()
  {
    return mRowDelay;
  }
  
  public void setColumnDelay(float paramFloat)
  {
    mColumnDelay = paramFloat;
  }
  
  public void setDirection(int paramInt)
  {
    mDirection = paramInt;
  }
  
  public void setDirectionPriority(int paramInt)
  {
    mDirectionPriority = paramInt;
  }
  
  public void setRowDelay(float paramFloat)
  {
    mRowDelay = paramFloat;
  }
  
  public boolean willOverlap()
  {
    boolean bool;
    if ((mColumnDelay >= 1.0F) && (mRowDelay >= 1.0F)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static class AnimationParameters
    extends LayoutAnimationController.AnimationParameters
  {
    public int column;
    public int columnsCount;
    public int row;
    public int rowsCount;
    
    public AnimationParameters() {}
  }
}
