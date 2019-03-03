package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.Map;

public class ChangeImageTransform
  extends Transition
{
  private static Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY = new Property(Matrix.class, "animatedTransform")
  {
    public Matrix get(ImageView paramAnonymousImageView)
    {
      return null;
    }
    
    public void set(ImageView paramAnonymousImageView, Matrix paramAnonymousMatrix)
    {
      paramAnonymousImageView.animateTransform(paramAnonymousMatrix);
    }
  };
  private static TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR;
  private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";
  private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
  private static final String TAG = "ChangeImageTransform";
  private static final String[] sTransitionProperties = { "android:changeImageTransform:matrix", "android:changeImageTransform:bounds" };
  
  static
  {
    NULL_MATRIX_EVALUATOR = new TypeEvaluator()
    {
      public Matrix evaluate(float paramAnonymousFloat, Matrix paramAnonymousMatrix1, Matrix paramAnonymousMatrix2)
      {
        return null;
      }
    };
  }
  
  public ChangeImageTransform() {}
  
  public ChangeImageTransform(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    if (((localView instanceof ImageView)) && (localView.getVisibility() == 0))
    {
      Object localObject = (ImageView)localView;
      Drawable localDrawable = ((ImageView)localObject).getDrawable();
      if (localDrawable == null) {
        return;
      }
      Map localMap = values;
      paramTransitionValues = new Rect(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
      localMap.put("android:changeImageTransform:bounds", paramTransitionValues);
      if (((ImageView)localObject).getScaleType() == ImageView.ScaleType.FIT_XY)
      {
        localObject = ((ImageView)localObject).getImageMatrix();
        if (!((Matrix)localObject).isIdentity())
        {
          paramTransitionValues = new Matrix((Matrix)localObject);
        }
        else
        {
          int i = localDrawable.getIntrinsicWidth();
          int j = localDrawable.getIntrinsicHeight();
          if ((i > 0) && (j > 0))
          {
            float f1 = paramTransitionValues.width() / i;
            float f2 = paramTransitionValues.height() / j;
            paramTransitionValues = new Matrix();
            paramTransitionValues.setScale(f1, f2);
          }
          else
          {
            paramTransitionValues = null;
          }
        }
      }
      else
      {
        paramTransitionValues = new Matrix(((ImageView)localObject).getImageMatrix());
      }
      localMap.put("android:changeImageTransform:matrix", paramTransitionValues);
      return;
    }
  }
  
  private ObjectAnimator createMatrixAnimator(ImageView paramImageView, Matrix paramMatrix1, Matrix paramMatrix2)
  {
    return ObjectAnimator.ofObject(paramImageView, ANIMATED_TRANSFORM_PROPERTY, new TransitionUtils.MatrixEvaluator(), new Matrix[] { paramMatrix1, paramMatrix2 });
  }
  
  private ObjectAnimator createNullAnimator(ImageView paramImageView)
  {
    return ObjectAnimator.ofObject(paramImageView, ANIMATED_TRANSFORM_PROPERTY, NULL_MATRIX_EVALUATOR, new Matrix[] { null, null });
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      Rect localRect = (Rect)values.get("android:changeImageTransform:bounds");
      paramViewGroup = (Rect)values.get("android:changeImageTransform:bounds");
      if ((localRect != null) && (paramViewGroup != null))
      {
        paramTransitionValues1 = (Matrix)values.get("android:changeImageTransform:matrix");
        Matrix localMatrix = (Matrix)values.get("android:changeImageTransform:matrix");
        if (((paramTransitionValues1 == null) && (localMatrix == null)) || ((paramTransitionValues1 != null) && (paramTransitionValues1.equals(localMatrix)))) {
          i = 1;
        } else {
          i = 0;
        }
        if ((localRect.equals(paramViewGroup)) && (i != 0)) {
          return null;
        }
        paramTransitionValues2 = (ImageView)view;
        paramViewGroup = paramTransitionValues2.getDrawable();
        int j = paramViewGroup.getIntrinsicWidth();
        int i = paramViewGroup.getIntrinsicHeight();
        if ((j != 0) && (i != 0))
        {
          paramViewGroup = paramTransitionValues1;
          if (paramTransitionValues1 == null) {
            paramViewGroup = Matrix.IDENTITY_MATRIX;
          }
          paramTransitionValues1 = localMatrix;
          if (localMatrix == null) {
            paramTransitionValues1 = Matrix.IDENTITY_MATRIX;
          }
          ANIMATED_TRANSFORM_PROPERTY.set(paramTransitionValues2, paramViewGroup);
          paramViewGroup = createMatrixAnimator(paramTransitionValues2, paramViewGroup, paramTransitionValues1);
        }
        else
        {
          paramViewGroup = createNullAnimator(paramTransitionValues2);
        }
        return paramViewGroup;
      }
      return null;
    }
    return null;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
}
