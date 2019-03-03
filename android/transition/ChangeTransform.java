package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatArrayEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.GhostView;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.util.Map;

public class ChangeTransform
  extends Transition
{
  private static final Property<PathAnimatorMatrix, float[]> NON_TRANSLATIONS_PROPERTY = new Property([F.class, "nonTranslations")
  {
    public float[] get(ChangeTransform.PathAnimatorMatrix paramAnonymousPathAnimatorMatrix)
    {
      return null;
    }
    
    public void set(ChangeTransform.PathAnimatorMatrix paramAnonymousPathAnimatorMatrix, float[] paramAnonymousArrayOfFloat)
    {
      paramAnonymousPathAnimatorMatrix.setValues(paramAnonymousArrayOfFloat);
    }
  };
  private static final String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";
  private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";
  private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
  private static final String PROPNAME_PARENT = "android:changeTransform:parent";
  private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
  private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
  private static final String TAG = "ChangeTransform";
  private static final Property<PathAnimatorMatrix, PointF> TRANSLATIONS_PROPERTY = new Property(PointF.class, "translations")
  {
    public PointF get(ChangeTransform.PathAnimatorMatrix paramAnonymousPathAnimatorMatrix)
    {
      return null;
    }
    
    public void set(ChangeTransform.PathAnimatorMatrix paramAnonymousPathAnimatorMatrix, PointF paramAnonymousPointF)
    {
      paramAnonymousPathAnimatorMatrix.setTranslation(paramAnonymousPointF);
    }
  };
  private static final String[] sTransitionProperties = { "android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix" };
  private boolean mReparent = true;
  private Matrix mTempMatrix = new Matrix();
  private boolean mUseOverlay = true;
  
  public ChangeTransform() {}
  
  public ChangeTransform(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ChangeTransform);
    mUseOverlay = paramContext.getBoolean(1, true);
    mReparent = paramContext.getBoolean(0, true);
    paramContext.recycle();
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    if (localView.getVisibility() == 8) {
      return;
    }
    values.put("android:changeTransform:parent", localView.getParent());
    Object localObject = new Transforms(localView);
    values.put("android:changeTransform:transforms", localObject);
    localObject = localView.getMatrix();
    if ((localObject != null) && (!((Matrix)localObject).isIdentity())) {
      localObject = new Matrix((Matrix)localObject);
    } else {
      localObject = null;
    }
    values.put("android:changeTransform:matrix", localObject);
    if (mReparent)
    {
      localObject = new Matrix();
      ViewGroup localViewGroup = (ViewGroup)localView.getParent();
      localViewGroup.transformMatrixToGlobal((Matrix)localObject);
      ((Matrix)localObject).preTranslate(-localViewGroup.getScrollX(), -localViewGroup.getScrollY());
      values.put("android:changeTransform:parentMatrix", localObject);
      values.put("android:changeTransform:intermediateMatrix", localView.getTag(16909499));
      values.put("android:changeTransform:intermediateParentMatrix", localView.getTag(16909221));
    }
  }
  
  private void createGhostView(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    View localView = view;
    Object localObject = new Matrix((Matrix)values.get("android:changeTransform:parentMatrix"));
    paramViewGroup.transformMatrixToLocal((Matrix)localObject);
    localObject = GhostView.addGhost(localView, paramViewGroup, (Matrix)localObject);
    for (paramViewGroup = this; mParent != null; paramViewGroup = mParent) {}
    paramViewGroup.addListener(new GhostListener(localView, view, (GhostView)localObject));
    if (view != view) {
      view.setTransitionAlpha(0.0F);
    }
    localView.setTransitionAlpha(1.0F);
  }
  
  private ObjectAnimator createTransformAnimator(final TransitionValues paramTransitionValues1, final TransitionValues paramTransitionValues2, final boolean paramBoolean)
  {
    paramTransitionValues1 = (Matrix)values.get("android:changeTransform:matrix");
    Object localObject1 = (Matrix)values.get("android:changeTransform:matrix");
    Object localObject2 = paramTransitionValues1;
    if (paramTransitionValues1 == null) {
      localObject2 = Matrix.IDENTITY_MATRIX;
    }
    paramTransitionValues1 = (TransitionValues)localObject1;
    if (localObject1 == null) {
      paramTransitionValues1 = Matrix.IDENTITY_MATRIX;
    }
    if (((Matrix)localObject2).equals(paramTransitionValues1)) {
      return null;
    }
    localObject1 = (Transforms)values.get("android:changeTransform:transforms");
    paramTransitionValues2 = view;
    setIdentityTransforms(paramTransitionValues2);
    Object localObject3 = new float[9];
    ((Matrix)localObject2).getValues((float[])localObject3);
    float[] arrayOfFloat = new float[9];
    paramTransitionValues1.getValues(arrayOfFloat);
    localObject2 = new PathAnimatorMatrix(paramTransitionValues2, (float[])localObject3);
    Object localObject4 = PropertyValuesHolder.ofObject(NON_TRANSLATIONS_PROPERTY, new FloatArrayEvaluator(new float[9]), new float[][] { localObject3, arrayOfFloat });
    localObject3 = getPathMotion().getPath(localObject3[2], localObject3[5], arrayOfFloat[2], arrayOfFloat[5]);
    localObject4 = ObjectAnimator.ofPropertyValuesHolder(localObject2, new PropertyValuesHolder[] { localObject4, PropertyValuesHolder.ofObject(TRANSLATIONS_PROPERTY, null, (Path)localObject3) });
    paramTransitionValues1 = new AnimatorListenerAdapter()
    {
      private boolean mIsCanceled;
      private Matrix mTempMatrix = new Matrix();
      
      private void setCurrentMatrix(Matrix paramAnonymousMatrix)
      {
        mTempMatrix.set(paramAnonymousMatrix);
        paramTransitionValues2.setTagInternal(16909499, mTempMatrix);
        val$transforms.restore(paramTransitionValues2);
      }
      
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
        mIsCanceled = true;
      }
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (!mIsCanceled) {
          if ((paramBoolean) && (mUseOverlay))
          {
            setCurrentMatrix(paramTransitionValues1);
          }
          else
          {
            paramTransitionValues2.setTagInternal(16909499, null);
            paramTransitionValues2.setTagInternal(16909221, null);
          }
        }
        paramTransitionValues2.setAnimationMatrix(null);
        val$transforms.restore(paramTransitionValues2);
      }
      
      public void onAnimationPause(Animator paramAnonymousAnimator)
      {
        setCurrentMatrix(val$pathAnimatorMatrix.getMatrix());
      }
      
      public void onAnimationResume(Animator paramAnonymousAnimator)
      {
        ChangeTransform.setIdentityTransforms(paramTransitionValues2);
      }
    };
    ((ObjectAnimator)localObject4).addListener(paramTransitionValues1);
    ((ObjectAnimator)localObject4).addPauseListener(paramTransitionValues1);
    return localObject4;
  }
  
  private boolean parentsMatch(ViewGroup paramViewGroup1, ViewGroup paramViewGroup2)
  {
    boolean bool1 = false;
    boolean bool2 = isValidTarget(paramViewGroup1);
    boolean bool3 = false;
    boolean bool4 = false;
    if ((bool2) && (isValidTarget(paramViewGroup2)))
    {
      paramViewGroup1 = getMatchedTransitionValues(paramViewGroup1, true);
      if (paramViewGroup1 != null)
      {
        bool1 = bool4;
        if (paramViewGroup2 == view) {
          bool1 = true;
        }
      }
    }
    else
    {
      bool1 = bool3;
      if (paramViewGroup1 == paramViewGroup2) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  private static void setIdentityTransforms(View paramView)
  {
    setTransforms(paramView, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
  }
  
  private void setMatricesForParent(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    Matrix localMatrix1 = (Matrix)values.get("android:changeTransform:parentMatrix");
    view.setTagInternal(16909221, localMatrix1);
    Matrix localMatrix2 = mTempMatrix;
    localMatrix2.reset();
    localMatrix1.invert(localMatrix2);
    localMatrix1 = (Matrix)values.get("android:changeTransform:matrix");
    paramTransitionValues2 = localMatrix1;
    if (localMatrix1 == null)
    {
      paramTransitionValues2 = new Matrix();
      values.put("android:changeTransform:matrix", paramTransitionValues2);
    }
    paramTransitionValues2.postConcat((Matrix)values.get("android:changeTransform:parentMatrix"));
    paramTransitionValues2.postConcat(localMatrix2);
  }
  
  private static void setTransforms(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
  {
    paramView.setTranslationX(paramFloat1);
    paramView.setTranslationY(paramFloat2);
    paramView.setTranslationZ(paramFloat3);
    paramView.setScaleX(paramFloat4);
    paramView.setScaleY(paramFloat5);
    paramView.setRotationX(paramFloat6);
    paramView.setRotationY(paramFloat7);
    paramView.setRotation(paramFloat8);
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
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null) && (values.containsKey("android:changeTransform:parent")) && (values.containsKey("android:changeTransform:parent")))
    {
      ViewGroup localViewGroup = (ViewGroup)values.get("android:changeTransform:parent");
      Object localObject = (ViewGroup)values.get("android:changeTransform:parent");
      boolean bool;
      if ((mReparent) && (!parentsMatch(localViewGroup, (ViewGroup)localObject))) {
        bool = true;
      } else {
        bool = false;
      }
      localObject = (Matrix)values.get("android:changeTransform:intermediateMatrix");
      if (localObject != null) {
        values.put("android:changeTransform:matrix", localObject);
      }
      localObject = (Matrix)values.get("android:changeTransform:intermediateParentMatrix");
      if (localObject != null) {
        values.put("android:changeTransform:parentMatrix", localObject);
      }
      if (bool) {
        setMatricesForParent(paramTransitionValues1, paramTransitionValues2);
      }
      localObject = createTransformAnimator(paramTransitionValues1, paramTransitionValues2, bool);
      if ((bool) && (localObject != null) && (mUseOverlay)) {
        createGhostView(paramViewGroup, paramTransitionValues1, paramTransitionValues2);
      }
      return localObject;
    }
    return null;
  }
  
  public boolean getReparent()
  {
    return mReparent;
  }
  
  public boolean getReparentWithOverlay()
  {
    return mUseOverlay;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
  
  public void setReparent(boolean paramBoolean)
  {
    mReparent = paramBoolean;
  }
  
  public void setReparentWithOverlay(boolean paramBoolean)
  {
    mUseOverlay = paramBoolean;
  }
  
  private static class GhostListener
    extends TransitionListenerAdapter
  {
    private GhostView mGhostView;
    private View mStartView;
    private View mView;
    
    public GhostListener(View paramView1, View paramView2, GhostView paramGhostView)
    {
      mView = paramView1;
      mStartView = paramView2;
      mGhostView = paramGhostView;
    }
    
    public void onTransitionEnd(Transition paramTransition)
    {
      paramTransition.removeListener(this);
      GhostView.removeGhost(mView);
      mView.setTagInternal(16909499, null);
      mView.setTagInternal(16909221, null);
      mStartView.setTransitionAlpha(1.0F);
    }
    
    public void onTransitionPause(Transition paramTransition)
    {
      mGhostView.setVisibility(4);
    }
    
    public void onTransitionResume(Transition paramTransition)
    {
      mGhostView.setVisibility(0);
    }
  }
  
  private static class PathAnimatorMatrix
  {
    private final Matrix mMatrix = new Matrix();
    private float mTranslationX;
    private float mTranslationY;
    private final float[] mValues;
    private final View mView;
    
    public PathAnimatorMatrix(View paramView, float[] paramArrayOfFloat)
    {
      mView = paramView;
      mValues = ((float[])paramArrayOfFloat.clone());
      mTranslationX = mValues[2];
      mTranslationY = mValues[5];
      setAnimationMatrix();
    }
    
    private void setAnimationMatrix()
    {
      mValues[2] = mTranslationX;
      mValues[5] = mTranslationY;
      mMatrix.setValues(mValues);
      mView.setAnimationMatrix(mMatrix);
    }
    
    public Matrix getMatrix()
    {
      return mMatrix;
    }
    
    public void setTranslation(PointF paramPointF)
    {
      mTranslationX = x;
      mTranslationY = y;
      setAnimationMatrix();
    }
    
    public void setValues(float[] paramArrayOfFloat)
    {
      System.arraycopy(paramArrayOfFloat, 0, mValues, 0, paramArrayOfFloat.length);
      setAnimationMatrix();
    }
  }
  
  private static class Transforms
  {
    public final float rotationX;
    public final float rotationY;
    public final float rotationZ;
    public final float scaleX;
    public final float scaleY;
    public final float translationX;
    public final float translationY;
    public final float translationZ;
    
    public Transforms(View paramView)
    {
      translationX = paramView.getTranslationX();
      translationY = paramView.getTranslationY();
      translationZ = paramView.getTranslationZ();
      scaleX = paramView.getScaleX();
      scaleY = paramView.getScaleY();
      rotationX = paramView.getRotationX();
      rotationY = paramView.getRotationY();
      rotationZ = paramView.getRotation();
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Transforms;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (Transforms)paramObject;
      bool1 = bool2;
      if (translationX == translationX)
      {
        bool1 = bool2;
        if (translationY == translationY)
        {
          bool1 = bool2;
          if (translationZ == translationZ)
          {
            bool1 = bool2;
            if (scaleX == scaleX)
            {
              bool1 = bool2;
              if (scaleY == scaleY)
              {
                bool1 = bool2;
                if (rotationX == rotationX)
                {
                  bool1 = bool2;
                  if (rotationY == rotationY)
                  {
                    bool1 = bool2;
                    if (rotationZ == rotationZ) {
                      bool1 = true;
                    }
                  }
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    
    public void restore(View paramView)
    {
      ChangeTransform.setTransforms(paramView, translationX, translationY, translationZ, scaleX, scaleY, rotationX, rotationY, rotationZ);
    }
  }
}
