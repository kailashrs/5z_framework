package android.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class TransitionUtils
{
  private static int MAX_IMAGE_SIZE = 1048576;
  
  public TransitionUtils() {}
  
  public static View copyViewImage(ViewGroup paramViewGroup, View paramView1, View paramView2)
  {
    Matrix localMatrix = new Matrix();
    localMatrix.setTranslate(-paramView2.getScrollX(), -paramView2.getScrollY());
    paramView1.transformMatrixToGlobal(localMatrix);
    paramViewGroup.transformMatrixToLocal(localMatrix);
    RectF localRectF = new RectF(0.0F, 0.0F, paramView1.getWidth(), paramView1.getHeight());
    localMatrix.mapRect(localRectF);
    int i = Math.round(left);
    int j = Math.round(top);
    int k = Math.round(right);
    int m = Math.round(bottom);
    paramView2 = new ImageView(paramView1.getContext());
    paramView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
    paramViewGroup = createViewBitmap(paramView1, localMatrix, localRectF, paramViewGroup);
    if (paramViewGroup != null) {
      paramView2.setImageBitmap(paramViewGroup);
    }
    paramView2.measure(View.MeasureSpec.makeMeasureSpec(k - i, 1073741824), View.MeasureSpec.makeMeasureSpec(m - j, 1073741824));
    paramView2.layout(i, j, k, m);
    return paramView2;
  }
  
  public static Bitmap createDrawableBitmap(Drawable paramDrawable, View paramView)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if ((i > 0) && (j > 0))
    {
      float f = Math.min(1.0F, MAX_IMAGE_SIZE / (i * j));
      if (((paramDrawable instanceof BitmapDrawable)) && (f == 1.0F)) {
        return ((BitmapDrawable)paramDrawable).getBitmap();
      }
      int k = (int)(i * f);
      int m = (int)(j * f);
      paramView = new Picture();
      Canvas localCanvas = paramView.beginRecording(i, j);
      Rect localRect = paramDrawable.getBounds();
      j = left;
      i = top;
      int n = right;
      int i1 = bottom;
      paramDrawable.setBounds(0, 0, k, m);
      paramDrawable.draw(localCanvas);
      paramDrawable.setBounds(j, i, n, i1);
      paramView.endRecording();
      return Bitmap.createBitmap(paramView);
    }
    return null;
  }
  
  public static Bitmap createViewBitmap(View paramView, Matrix paramMatrix, RectF paramRectF, ViewGroup paramViewGroup)
  {
    boolean bool = paramView.isAttachedToWindow() ^ true;
    ViewGroup localViewGroup = null;
    int i = 0;
    if (bool) {
      if ((paramViewGroup != null) && (paramViewGroup.isAttachedToWindow()))
      {
        localViewGroup = (ViewGroup)paramView.getParent();
        i = localViewGroup.indexOfChild(paramView);
        paramViewGroup.getOverlay().add(paramView);
      }
      else
      {
        return null;
      }
    }
    Object localObject1 = null;
    int j = Math.round(paramRectF.width());
    int k = Math.round(paramRectF.height());
    Object localObject2 = localObject1;
    if (j > 0)
    {
      localObject2 = localObject1;
      if (k > 0)
      {
        float f = Math.min(1.0F, MAX_IMAGE_SIZE / (j * k));
        j = (int)(j * f);
        k = (int)(k * f);
        paramMatrix.postTranslate(-left, -top);
        paramMatrix.postScale(f, f);
        localObject2 = new Picture();
        paramRectF = ((Picture)localObject2).beginRecording(j, k);
        paramRectF.concat(paramMatrix);
        paramView.draw(paramRectF);
        ((Picture)localObject2).endRecording();
        localObject2 = Bitmap.createBitmap((Picture)localObject2);
      }
    }
    if (bool)
    {
      paramViewGroup.getOverlay().remove(paramView);
      localViewGroup.addView(paramView, i);
    }
    return localObject2;
  }
  
  static Animator mergeAnimators(Animator paramAnimator1, Animator paramAnimator2)
  {
    if (paramAnimator1 == null) {
      return paramAnimator2;
    }
    if (paramAnimator2 == null) {
      return paramAnimator1;
    }
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.playTogether(new Animator[] { paramAnimator1, paramAnimator2 });
    return localAnimatorSet;
  }
  
  public static Transition mergeTransitions(Transition... paramVarArgs)
  {
    int i = 0;
    int j = -1;
    int k = 0;
    int m = 0;
    while (m < paramVarArgs.length)
    {
      int n = k;
      if (paramVarArgs[m] != null)
      {
        n = k + 1;
        j = m;
      }
      m++;
      k = n;
    }
    if (k == 0) {
      return null;
    }
    if (k == 1) {
      return paramVarArgs[j];
    }
    TransitionSet localTransitionSet = new TransitionSet();
    for (m = i; m < paramVarArgs.length; m++) {
      if (paramVarArgs[m] != null) {
        localTransitionSet.addTransition(paramVarArgs[m]);
      }
    }
    return localTransitionSet;
  }
  
  public static class MatrixEvaluator
    implements TypeEvaluator<Matrix>
  {
    float[] mTempEndValues = new float[9];
    Matrix mTempMatrix = new Matrix();
    float[] mTempStartValues = new float[9];
    
    public MatrixEvaluator() {}
    
    public Matrix evaluate(float paramFloat, Matrix paramMatrix1, Matrix paramMatrix2)
    {
      paramMatrix1.getValues(mTempStartValues);
      paramMatrix2.getValues(mTempEndValues);
      for (int i = 0; i < 9; i++)
      {
        float f1 = mTempEndValues[i];
        float f2 = mTempStartValues[i];
        mTempEndValues[i] = (mTempStartValues[i] + paramFloat * (f1 - f2));
      }
      mTempMatrix.setValues(mTempEndValues);
      return mTempMatrix;
    }
  }
}
