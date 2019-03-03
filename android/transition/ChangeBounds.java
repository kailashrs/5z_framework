package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.RectEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.internal.R.styleable;
import java.util.Map;

public class ChangeBounds
  extends Transition
{
  private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
  private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
  private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
  private static final String LOG_TAG = "ChangeBounds";
  private static final Property<View, PointF> POSITION_PROPERTY = new Property(PointF.class, "position")
  {
    public PointF get(View paramAnonymousView)
    {
      return null;
    }
    
    public void set(View paramAnonymousView, PointF paramAnonymousPointF)
    {
      int i = Math.round(x);
      int j = Math.round(y);
      paramAnonymousView.setLeftTopRightBottom(i, j, paramAnonymousView.getWidth() + i, paramAnonymousView.getHeight() + j);
    }
  };
  private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
  private static final String PROPNAME_CLIP = "android:changeBounds:clip";
  private static final String PROPNAME_PARENT = "android:changeBounds:parent";
  private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
  private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
  private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
  private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
  private static RectEvaluator sRectEvaluator = new RectEvaluator();
  private static final String[] sTransitionProperties = { "android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY" };
  boolean mReparent = false;
  boolean mResizeClip = false;
  int[] tempLocation = new int[2];
  
  static
  {
    DRAWABLE_ORIGIN_PROPERTY = new Property(PointF.class, "boundsOrigin")
    {
      private Rect mBounds = new Rect();
      
      public PointF get(Drawable paramAnonymousDrawable)
      {
        paramAnonymousDrawable.copyBounds(mBounds);
        return new PointF(mBounds.left, mBounds.top);
      }
      
      public void set(Drawable paramAnonymousDrawable, PointF paramAnonymousPointF)
      {
        paramAnonymousDrawable.copyBounds(mBounds);
        mBounds.offsetTo(Math.round(x), Math.round(y));
        paramAnonymousDrawable.setBounds(mBounds);
      }
    };
    TOP_LEFT_PROPERTY = new Property(PointF.class, "topLeft")
    {
      public PointF get(ChangeBounds.ViewBounds paramAnonymousViewBounds)
      {
        return null;
      }
      
      public void set(ChangeBounds.ViewBounds paramAnonymousViewBounds, PointF paramAnonymousPointF)
      {
        paramAnonymousViewBounds.setTopLeft(paramAnonymousPointF);
      }
    };
    BOTTOM_RIGHT_PROPERTY = new Property(PointF.class, "bottomRight")
    {
      public PointF get(ChangeBounds.ViewBounds paramAnonymousViewBounds)
      {
        return null;
      }
      
      public void set(ChangeBounds.ViewBounds paramAnonymousViewBounds, PointF paramAnonymousPointF)
      {
        paramAnonymousViewBounds.setBottomRight(paramAnonymousPointF);
      }
    };
    BOTTOM_RIGHT_ONLY_PROPERTY = new Property(PointF.class, "bottomRight")
    {
      public PointF get(View paramAnonymousView)
      {
        return null;
      }
      
      public void set(View paramAnonymousView, PointF paramAnonymousPointF)
      {
        paramAnonymousView.setLeftTopRightBottom(paramAnonymousView.getLeft(), paramAnonymousView.getTop(), Math.round(x), Math.round(y));
      }
    };
    TOP_LEFT_ONLY_PROPERTY = new Property(PointF.class, "topLeft")
    {
      public PointF get(View paramAnonymousView)
      {
        return null;
      }
      
      public void set(View paramAnonymousView, PointF paramAnonymousPointF)
      {
        paramAnonymousView.setLeftTopRightBottom(Math.round(x), Math.round(y), paramAnonymousView.getRight(), paramAnonymousView.getBottom());
      }
    };
  }
  
  public ChangeBounds() {}
  
  public ChangeBounds(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ChangeBounds);
    boolean bool = paramContext.getBoolean(0, false);
    paramContext.recycle();
    setResizeClip(bool);
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    if ((localView.isLaidOut()) || (localView.getWidth() != 0) || (localView.getHeight() != 0))
    {
      values.put("android:changeBounds:bounds", new Rect(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom()));
      values.put("android:changeBounds:parent", view.getParent());
      if (mReparent)
      {
        view.getLocationInWindow(tempLocation);
        values.put("android:changeBounds:windowX", Integer.valueOf(tempLocation[0]));
        values.put("android:changeBounds:windowY", Integer.valueOf(tempLocation[1]));
      }
      if (mResizeClip) {
        values.put("android:changeBounds:clip", localView.getClipBounds());
      }
    }
  }
  
  private boolean parentMatches(View paramView1, View paramView2)
  {
    boolean bool1 = true;
    if (mReparent)
    {
      boolean bool2 = true;
      bool1 = true;
      TransitionValues localTransitionValues = getMatchedTransitionValues(paramView1, true);
      if (localTransitionValues == null)
      {
        if (paramView1 != paramView2) {
          bool1 = false;
        }
      }
      else if (paramView2 == view) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(final ViewGroup paramViewGroup, final TransitionValues paramTransitionValues1, final TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 != null) && (paramTransitionValues2 != null))
    {
      Object localObject1 = values;
      Object localObject2 = values;
      localObject1 = (ViewGroup)((Map)localObject1).get("android:changeBounds:parent");
      localObject2 = (ViewGroup)((Map)localObject2).get("android:changeBounds:parent");
      if ((localObject1 != null) && (localObject2 != null))
      {
        final View localView = view;
        final int i3;
        int i8;
        int i9;
        int i10;
        if (parentMatches((View)localObject1, (View)localObject2))
        {
          localObject1 = (Rect)values.get("android:changeBounds:bounds");
          paramViewGroup = (Rect)values.get("android:changeBounds:bounds");
          i = left;
          final int j = left;
          int k = top;
          final int m = top;
          int n = right;
          final int i1 = right;
          int i2 = bottom;
          i3 = bottom;
          i4 = n - i;
          int i5 = i2 - k;
          int i6 = i1 - j;
          int i7 = i3 - m;
          localObject1 = (Rect)values.get("android:changeBounds:clip");
          paramViewGroup = (Rect)values.get("android:changeBounds:clip");
          i8 = 0;
          i9 = 0;
          if ((i4 == 0) || (i5 == 0))
          {
            i10 = i8;
            if (i6 != 0)
            {
              i10 = i8;
              if (i7 == 0) {}
            }
          }
          else
          {
            if ((i != j) || (k != m)) {
              i9 = 0 + 1;
            }
            if (n == i1)
            {
              i10 = i9;
              if (i2 == i3) {}
            }
            else
            {
              i10 = i9 + 1;
            }
          }
          if ((localObject1 == null) || (((Rect)localObject1).equals(paramViewGroup)))
          {
            i9 = i10;
            if (localObject1 == null)
            {
              i9 = i10;
              if (paramViewGroup == null) {}
            }
          }
          else
          {
            i9 = i10 + 1;
          }
          if (i9 > 0)
          {
            if ((localView.getParent() instanceof ViewGroup))
            {
              paramTransitionValues2 = (ViewGroup)localView.getParent();
              paramTransitionValues2.suppressLayout(true);
              paramTransitionValues1 = this;
              paramTransitionValues1.addListener(new TransitionListenerAdapter()
              {
                boolean mCanceled = false;
                
                public void onTransitionCancel(Transition paramAnonymousTransition)
                {
                  paramTransitionValues2.suppressLayout(false);
                  mCanceled = true;
                }
                
                public void onTransitionEnd(Transition paramAnonymousTransition)
                {
                  if (!mCanceled) {
                    paramTransitionValues2.suppressLayout(false);
                  }
                  paramAnonymousTransition.removeListener(this);
                }
                
                public void onTransitionPause(Transition paramAnonymousTransition)
                {
                  paramTransitionValues2.suppressLayout(false);
                }
                
                public void onTransitionResume(Transition paramAnonymousTransition)
                {
                  paramTransitionValues2.suppressLayout(true);
                }
              });
            }
            localObject2 = this;
            if (!mResizeClip)
            {
              localView.setLeftTopRightBottom(i, k, n, i2);
              if (i9 == 2)
              {
                if ((i4 == i6) && (i5 == i7))
                {
                  paramViewGroup = getPathMotion().getPath(i, k, j, m);
                  paramViewGroup = ObjectAnimator.ofObject(localView, POSITION_PROPERTY, null, paramViewGroup);
                }
                else
                {
                  paramTransitionValues1 = new ViewBounds(localView);
                  paramViewGroup = getPathMotion().getPath(i, k, j, m);
                  paramTransitionValues2 = ObjectAnimator.ofObject(paramTransitionValues1, TOP_LEFT_PROPERTY, null, paramViewGroup);
                  paramViewGroup = getPathMotion().getPath(n, i2, i1, i3);
                  localObject1 = ObjectAnimator.ofObject(paramTransitionValues1, BOTTOM_RIGHT_PROPERTY, null, paramViewGroup);
                  paramViewGroup = new AnimatorSet();
                  paramViewGroup.playTogether(new Animator[] { paramTransitionValues2, localObject1 });
                  paramViewGroup.addListener(new AnimatorListenerAdapter()
                  {
                    private ChangeBounds.ViewBounds mViewBounds = paramTransitionValues1;
                  });
                }
              }
              else if ((i == j) && (k == m))
              {
                paramViewGroup = getPathMotion().getPath(n, i2, i1, i3);
                paramViewGroup = ObjectAnimator.ofObject(localView, BOTTOM_RIGHT_ONLY_PROPERTY, null, paramViewGroup);
              }
              else
              {
                paramViewGroup = getPathMotion().getPath(i, k, j, m);
                paramViewGroup = ObjectAnimator.ofObject(localView, TOP_LEFT_ONLY_PROPERTY, null, paramViewGroup);
              }
            }
            else
            {
              localView.setLeftTopRightBottom(i, k, i + Math.max(i4, i6), k + Math.max(i5, i7));
              paramTransitionValues1 = null;
              if ((i == j) && (k == m)) {}
              for (;;)
              {
                break;
                paramTransitionValues1 = getPathMotion().getPath(i, k, j, m);
                paramTransitionValues1 = ObjectAnimator.ofObject(localView, POSITION_PROPERTY, null, paramTransitionValues1);
              }
              if (localObject1 == null) {
                paramTransitionValues2 = new Rect(0, 0, i4, i5);
              } else {
                paramTransitionValues2 = (TransitionValues)localObject1;
              }
              if (paramViewGroup == null) {
                localObject1 = new Rect(0, 0, i6, i7);
              } else {
                localObject1 = paramViewGroup;
              }
              if (!paramTransitionValues2.equals(localObject1))
              {
                localView.setClipBounds(paramTransitionValues2);
                paramTransitionValues2 = ObjectAnimator.ofObject(localView, "clipBounds", sRectEvaluator, new Object[] { paramTransitionValues2, localObject1 });
                paramTransitionValues2.addListener(new AnimatorListenerAdapter()
                {
                  private boolean mIsCanceled;
                  
                  public void onAnimationCancel(Animator paramAnonymousAnimator)
                  {
                    mIsCanceled = true;
                  }
                  
                  public void onAnimationEnd(Animator paramAnonymousAnimator)
                  {
                    if (!mIsCanceled)
                    {
                      localView.setClipBounds(paramViewGroup);
                      localView.setLeftTopRightBottom(j, m, i1, i3);
                    }
                  }
                });
                paramViewGroup = paramTransitionValues2;
              }
              else
              {
                paramViewGroup = null;
              }
              paramViewGroup = TransitionUtils.mergeAnimators(paramTransitionValues1, paramViewGroup);
            }
            return paramViewGroup;
          }
        }
        else
        {
          paramViewGroup.getLocationInWindow(tempLocation);
          i10 = ((Integer)values.get("android:changeBounds:windowX")).intValue() - tempLocation[0];
          i8 = ((Integer)values.get("android:changeBounds:windowY")).intValue() - tempLocation[1];
          i3 = ((Integer)values.get("android:changeBounds:windowX")).intValue() - tempLocation[0];
          i9 = ((Integer)values.get("android:changeBounds:windowY")).intValue() - tempLocation[1];
          if ((i10 != i3) || (i8 != i9)) {
            break label1010;
          }
        }
        return null;
        label1010:
        int i = localView.getWidth();
        int i4 = localView.getHeight();
        paramTransitionValues1 = Bitmap.createBitmap(i, i4, Bitmap.Config.ARGB_8888);
        localView.draw(new Canvas(paramTransitionValues1));
        paramTransitionValues1 = new BitmapDrawable(paramTransitionValues1);
        paramTransitionValues1.setBounds(i10, i8, i10 + i, i8 + i4);
        final float f = localView.getTransitionAlpha();
        localView.setTransitionAlpha(0.0F);
        paramViewGroup.getOverlay().add(paramTransitionValues1);
        paramTransitionValues2 = getPathMotion().getPath(i10, i8, i3, i9);
        paramTransitionValues2 = ObjectAnimator.ofPropertyValuesHolder(paramTransitionValues1, new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(DRAWABLE_ORIGIN_PROPERTY, null, paramTransitionValues2) });
        paramTransitionValues2.addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            paramViewGroup.getOverlay().remove(paramTransitionValues1);
            localView.setTransitionAlpha(f);
          }
        });
        return paramTransitionValues2;
      }
      return null;
    }
    return null;
  }
  
  public boolean getResizeClip()
  {
    return mResizeClip;
  }
  
  public String[] getTransitionProperties()
  {
    return sTransitionProperties;
  }
  
  @Deprecated
  public void setReparent(boolean paramBoolean)
  {
    mReparent = paramBoolean;
  }
  
  public void setResizeClip(boolean paramBoolean)
  {
    mResizeClip = paramBoolean;
  }
  
  private static class ViewBounds
  {
    private int mBottom;
    private int mBottomRightCalls;
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mTopLeftCalls;
    private View mView;
    
    public ViewBounds(View paramView)
    {
      mView = paramView;
    }
    
    private void setLeftTopRightBottom()
    {
      mView.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
      mTopLeftCalls = 0;
      mBottomRightCalls = 0;
    }
    
    public void setBottomRight(PointF paramPointF)
    {
      mRight = Math.round(x);
      mBottom = Math.round(y);
      mBottomRightCalls += 1;
      if (mTopLeftCalls == mBottomRightCalls) {
        setLeftTopRightBottom();
      }
    }
    
    public void setTopLeft(PointF paramPointF)
    {
      mLeft = Math.round(x);
      mTop = Math.round(y);
      mTopLeftCalls += 1;
      if (mTopLeftCalls == mBottomRightCalls) {
        setLeftTopRightBottom();
      }
    }
  }
}
