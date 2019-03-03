package android.animation;

import android.content.res.ConstantState;
import android.util.StateSet;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class StateListAnimator
  implements Cloneable
{
  private AnimatorListenerAdapter mAnimatorListener;
  private int mChangingConfigurations;
  private StateListAnimatorConstantState mConstantState;
  private Tuple mLastMatch = null;
  private Animator mRunningAnimator = null;
  private ArrayList<Tuple> mTuples = new ArrayList();
  private WeakReference<View> mViewRef;
  
  public StateListAnimator()
  {
    initAnimatorListener();
  }
  
  private void cancel()
  {
    if (mRunningAnimator != null)
    {
      mRunningAnimator.cancel();
      mRunningAnimator = null;
    }
  }
  
  private void clearTarget()
  {
    int i = mTuples.size();
    for (int j = 0; j < i; j++) {
      mTuples.get(j)).mAnimator.setTarget(null);
    }
    mViewRef = null;
    mLastMatch = null;
    mRunningAnimator = null;
  }
  
  private void initAnimatorListener()
  {
    mAnimatorListener = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        paramAnonymousAnimator.setTarget(null);
        if (mRunningAnimator == paramAnonymousAnimator) {
          StateListAnimator.access$002(StateListAnimator.this, null);
        }
      }
    };
  }
  
  private void start(Tuple paramTuple)
  {
    mAnimator.setTarget(getTarget());
    mRunningAnimator = mAnimator;
    mRunningAnimator.start();
  }
  
  public void addState(int[] paramArrayOfInt, Animator paramAnimator)
  {
    paramArrayOfInt = new Tuple(paramArrayOfInt, paramAnimator, null);
    mAnimator.addListener(mAnimatorListener);
    mTuples.add(paramArrayOfInt);
    mChangingConfigurations |= paramAnimator.getChangingConfigurations();
  }
  
  public void appendChangingConfigurations(int paramInt)
  {
    mChangingConfigurations |= paramInt;
  }
  
  public StateListAnimator clone()
  {
    try
    {
      StateListAnimator localStateListAnimator = (StateListAnimator)super.clone();
      Object localObject = new java/util/ArrayList;
      ((ArrayList)localObject).<init>(mTuples.size());
      mTuples = ((ArrayList)localObject);
      mLastMatch = null;
      mRunningAnimator = null;
      mViewRef = null;
      mAnimatorListener = null;
      localStateListAnimator.initAnimatorListener();
      int i = mTuples.size();
      for (int j = 0; j < i; j++)
      {
        Tuple localTuple = (Tuple)mTuples.get(j);
        localObject = mAnimator.clone();
        ((Animator)localObject).removeListener(mAnimatorListener);
        localStateListAnimator.addState(mSpecs, (Animator)localObject);
      }
      localStateListAnimator.setChangingConfigurations(getChangingConfigurations());
      return localStateListAnimator;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new AssertionError("cannot clone state list animator", localCloneNotSupportedException);
    }
  }
  
  public ConstantState<StateListAnimator> createConstantState()
  {
    return new StateListAnimatorConstantState(this);
  }
  
  public int getChangingConfigurations()
  {
    return mChangingConfigurations;
  }
  
  public Animator getRunningAnimator()
  {
    return mRunningAnimator;
  }
  
  public View getTarget()
  {
    View localView;
    if (mViewRef == null) {
      localView = null;
    } else {
      localView = (View)mViewRef.get();
    }
    return localView;
  }
  
  public ArrayList<Tuple> getTuples()
  {
    return mTuples;
  }
  
  public void jumpToCurrentState()
  {
    if (mRunningAnimator != null) {
      mRunningAnimator.end();
    }
  }
  
  public void setChangingConfigurations(int paramInt)
  {
    mChangingConfigurations = paramInt;
  }
  
  public void setState(int[] paramArrayOfInt)
  {
    Object localObject1 = null;
    int i = mTuples.size();
    Object localObject2;
    for (int j = 0;; j++)
    {
      localObject2 = localObject1;
      if (j >= i) {
        break;
      }
      localObject2 = (Tuple)mTuples.get(j);
      if (StateSet.stateSetMatches(mSpecs, paramArrayOfInt)) {
        break;
      }
    }
    if (localObject2 == mLastMatch) {
      return;
    }
    if (mLastMatch != null) {
      cancel();
    }
    mLastMatch = ((Tuple)localObject2);
    if (localObject2 != null) {
      start((Tuple)localObject2);
    }
  }
  
  public void setTarget(View paramView)
  {
    View localView = getTarget();
    if (localView == paramView) {
      return;
    }
    if (localView != null) {
      clearTarget();
    }
    if (paramView != null) {
      mViewRef = new WeakReference(paramView);
    }
  }
  
  private static class StateListAnimatorConstantState
    extends ConstantState<StateListAnimator>
  {
    final StateListAnimator mAnimator;
    int mChangingConf;
    
    public StateListAnimatorConstantState(StateListAnimator paramStateListAnimator)
    {
      mAnimator = paramStateListAnimator;
      StateListAnimator.access$202(mAnimator, this);
      mChangingConf = mAnimator.getChangingConfigurations();
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConf;
    }
    
    public StateListAnimator newInstance()
    {
      StateListAnimator localStateListAnimator = mAnimator.clone();
      StateListAnimator.access$202(localStateListAnimator, this);
      return localStateListAnimator;
    }
  }
  
  public static class Tuple
  {
    final Animator mAnimator;
    final int[] mSpecs;
    
    private Tuple(int[] paramArrayOfInt, Animator paramAnimator)
    {
      mSpecs = paramArrayOfInt;
      mAnimator = paramAnimator;
    }
    
    public Animator getAnimator()
    {
      return mAnimator;
    }
    
    public int[] getSpecs()
    {
      return mSpecs;
    }
  }
}
