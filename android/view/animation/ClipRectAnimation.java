package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class ClipRectAnimation
  extends Animation
{
  private int mFromBottomType = 0;
  private float mFromBottomValue;
  private int mFromLeftType = 0;
  private float mFromLeftValue;
  protected final Rect mFromRect = new Rect();
  private int mFromRightType = 0;
  private float mFromRightValue;
  private int mFromTopType = 0;
  private float mFromTopValue;
  private int mToBottomType = 0;
  private float mToBottomValue;
  private int mToLeftType = 0;
  private float mToLeftValue;
  protected final Rect mToRect = new Rect();
  private int mToRightType = 0;
  private float mToRightValue;
  private int mToTopType = 0;
  private float mToTopValue;
  
  public ClipRectAnimation(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    this(new Rect(paramInt1, paramInt2, paramInt3, paramInt4), new Rect(paramInt5, paramInt6, paramInt7, paramInt8));
  }
  
  public ClipRectAnimation(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ClipRectAnimation);
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(1));
    mFromLeftType = type;
    mFromLeftValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(3));
    mFromTopType = type;
    mFromTopValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(2));
    mFromRightType = type;
    mFromRightValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(0));
    mFromBottomType = type;
    mFromBottomValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(5));
    mToLeftType = type;
    mToLeftValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(7));
    mToTopType = type;
    mToTopValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(6));
    mToRightType = type;
    mToRightValue = value;
    paramAttributeSet = Animation.Description.parseValue(paramContext.peekValue(4));
    mToBottomType = type;
    mToBottomValue = value;
    paramContext.recycle();
  }
  
  public ClipRectAnimation(Rect paramRect1, Rect paramRect2)
  {
    if ((paramRect1 != null) && (paramRect2 != null))
    {
      mFromLeftValue = left;
      mFromTopValue = top;
      mFromRightValue = right;
      mFromBottomValue = bottom;
      mToLeftValue = left;
      mToTopValue = top;
      mToRightValue = right;
      mToBottomValue = bottom;
      return;
    }
    throw new RuntimeException("Expected non-null animation clip rects");
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    paramTransformation.setClipRect(mFromRect.left + (int)((mToRect.left - mFromRect.left) * paramFloat), mFromRect.top + (int)((mToRect.top - mFromRect.top) * paramFloat), mFromRect.right + (int)((mToRect.right - mFromRect.right) * paramFloat), mFromRect.bottom + (int)((mToRect.bottom - mFromRect.bottom) * paramFloat));
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    mFromRect.set((int)resolveSize(mFromLeftType, mFromLeftValue, paramInt1, paramInt3), (int)resolveSize(mFromTopType, mFromTopValue, paramInt2, paramInt4), (int)resolveSize(mFromRightType, mFromRightValue, paramInt1, paramInt3), (int)resolveSize(mFromBottomType, mFromBottomValue, paramInt2, paramInt4));
    mToRect.set((int)resolveSize(mToLeftType, mToLeftValue, paramInt1, paramInt3), (int)resolveSize(mToTopType, mToTopValue, paramInt2, paramInt4), (int)resolveSize(mToRightType, mToRightValue, paramInt1, paramInt3), (int)resolveSize(mToBottomType, mToBottomValue, paramInt2, paramInt4));
  }
  
  public boolean willChangeTransformationMatrix()
  {
    return false;
  }
}
