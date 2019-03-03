package android.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionSet
  extends Transition
{
  public static final int ORDERING_SEQUENTIAL = 1;
  public static final int ORDERING_TOGETHER = 0;
  int mCurrentListeners;
  private boolean mPlayTogether = true;
  boolean mStarted = false;
  ArrayList<Transition> mTransitions = new ArrayList();
  
  public TransitionSet() {}
  
  public TransitionSet(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TransitionSet);
    setOrdering(paramContext.getInt(0, 0));
    paramContext.recycle();
  }
  
  private void setupStartEndListeners()
  {
    TransitionSetListener localTransitionSetListener = new TransitionSetListener(this);
    Iterator localIterator = mTransitions.iterator();
    while (localIterator.hasNext()) {
      ((Transition)localIterator.next()).addListener(localTransitionSetListener);
    }
    mCurrentListeners = mTransitions.size();
  }
  
  public TransitionSet addListener(Transition.TransitionListener paramTransitionListener)
  {
    return (TransitionSet)super.addListener(paramTransitionListener);
  }
  
  public TransitionSet addTarget(int paramInt)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).addTarget(paramInt);
    }
    return (TransitionSet)super.addTarget(paramInt);
  }
  
  public TransitionSet addTarget(View paramView)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).addTarget(paramView);
    }
    return (TransitionSet)super.addTarget(paramView);
  }
  
  public TransitionSet addTarget(Class paramClass)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).addTarget(paramClass);
    }
    return (TransitionSet)super.addTarget(paramClass);
  }
  
  public TransitionSet addTarget(String paramString)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).addTarget(paramString);
    }
    return (TransitionSet)super.addTarget(paramString);
  }
  
  public TransitionSet addTransition(Transition paramTransition)
  {
    if (paramTransition != null)
    {
      mTransitions.add(paramTransition);
      mParent = this;
      if (mDuration >= 0L) {
        paramTransition.setDuration(mDuration);
      }
    }
    return this;
  }
  
  protected void cancel()
  {
    super.cancel();
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).cancel();
    }
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    if (isValidTarget(view))
    {
      Iterator localIterator = mTransitions.iterator();
      while (localIterator.hasNext())
      {
        Transition localTransition = (Transition)localIterator.next();
        if (localTransition.isValidTarget(view))
        {
          localTransition.captureEndValues(paramTransitionValues);
          targetedTransitions.add(localTransition);
        }
      }
    }
  }
  
  void capturePropagationValues(TransitionValues paramTransitionValues)
  {
    super.capturePropagationValues(paramTransitionValues);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).capturePropagationValues(paramTransitionValues);
    }
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    if (isValidTarget(view))
    {
      Iterator localIterator = mTransitions.iterator();
      while (localIterator.hasNext())
      {
        Transition localTransition = (Transition)localIterator.next();
        if (localTransition.isValidTarget(view))
        {
          localTransition.captureStartValues(paramTransitionValues);
          targetedTransitions.add(localTransition);
        }
      }
    }
  }
  
  public TransitionSet clone()
  {
    TransitionSet localTransitionSet = (TransitionSet)super.clone();
    mTransitions = new ArrayList();
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      localTransitionSet.addTransition(((Transition)mTransitions.get(j)).clone());
    }
    return localTransitionSet;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2)
  {
    long l1 = getStartDelay();
    int i = mTransitions.size();
    for (int j = 0; j < i; j++)
    {
      Transition localTransition = (Transition)mTransitions.get(j);
      if ((l1 > 0L) && ((mPlayTogether) || (j == 0)))
      {
        long l2 = localTransition.getStartDelay();
        if (l2 > 0L) {
          localTransition.setStartDelay(l1 + l2);
        } else {
          localTransition.setStartDelay(l1);
        }
      }
      localTransition.createAnimators(paramViewGroup, paramTransitionValuesMaps1, paramTransitionValuesMaps2, paramArrayList1, paramArrayList2);
    }
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).excludeTarget(paramInt, paramBoolean);
    }
    return super.excludeTarget(paramInt, paramBoolean);
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).excludeTarget(paramView, paramBoolean);
    }
    return super.excludeTarget(paramView, paramBoolean);
  }
  
  public Transition excludeTarget(Class paramClass, boolean paramBoolean)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).excludeTarget(paramClass, paramBoolean);
    }
    return super.excludeTarget(paramClass, paramBoolean);
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).excludeTarget(paramString, paramBoolean);
    }
    return super.excludeTarget(paramString, paramBoolean);
  }
  
  void forceToEnd(ViewGroup paramViewGroup)
  {
    super.forceToEnd(paramViewGroup);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).forceToEnd(paramViewGroup);
    }
  }
  
  public int getOrdering()
  {
    return mPlayTogether ^ true;
  }
  
  public Transition getTransitionAt(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mTransitions.size())) {
      return (Transition)mTransitions.get(paramInt);
    }
    return null;
  }
  
  public int getTransitionCount()
  {
    return mTransitions.size();
  }
  
  public void pause(View paramView)
  {
    super.pause(paramView);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).pause(paramView);
    }
  }
  
  public TransitionSet removeListener(Transition.TransitionListener paramTransitionListener)
  {
    return (TransitionSet)super.removeListener(paramTransitionListener);
  }
  
  public TransitionSet removeTarget(int paramInt)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).removeTarget(paramInt);
    }
    return (TransitionSet)super.removeTarget(paramInt);
  }
  
  public TransitionSet removeTarget(View paramView)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).removeTarget(paramView);
    }
    return (TransitionSet)super.removeTarget(paramView);
  }
  
  public TransitionSet removeTarget(Class paramClass)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).removeTarget(paramClass);
    }
    return (TransitionSet)super.removeTarget(paramClass);
  }
  
  public TransitionSet removeTarget(String paramString)
  {
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).removeTarget(paramString);
    }
    return (TransitionSet)super.removeTarget(paramString);
  }
  
  public TransitionSet removeTransition(Transition paramTransition)
  {
    mTransitions.remove(paramTransition);
    mParent = null;
    return this;
  }
  
  public void resume(View paramView)
  {
    super.resume(paramView);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).resume(paramView);
    }
  }
  
  protected void runAnimators()
  {
    if (mTransitions.isEmpty())
    {
      start();
      end();
      return;
    }
    setupStartEndListeners();
    int i = mTransitions.size();
    boolean bool = mPlayTogether;
    int j = 0;
    if (!bool)
    {
      for (j = 1; j < i; j++) {
        ((Transition)mTransitions.get(j - 1)).addListener(new TransitionListenerAdapter()
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            val$nextTransition.runAnimators();
            paramAnonymousTransition.removeListener(this);
          }
        });
      }
      Transition localTransition = (Transition)mTransitions.get(0);
      if (localTransition != null) {
        localTransition.runAnimators();
      }
    }
    else
    {
      while (j < i)
      {
        ((Transition)mTransitions.get(j)).runAnimators();
        j++;
      }
    }
  }
  
  void setCanRemoveViews(boolean paramBoolean)
  {
    super.setCanRemoveViews(paramBoolean);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).setCanRemoveViews(paramBoolean);
    }
  }
  
  public TransitionSet setDuration(long paramLong)
  {
    super.setDuration(paramLong);
    if ((mDuration >= 0L) && (mTransitions != null))
    {
      int i = mTransitions.size();
      for (int j = 0; j < i; j++) {
        ((Transition)mTransitions.get(j)).setDuration(paramLong);
      }
    }
    return this;
  }
  
  public void setEpicenterCallback(Transition.EpicenterCallback paramEpicenterCallback)
  {
    super.setEpicenterCallback(paramEpicenterCallback);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).setEpicenterCallback(paramEpicenterCallback);
    }
  }
  
  public TransitionSet setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    return (TransitionSet)super.setInterpolator(paramTimeInterpolator);
  }
  
  public TransitionSet setOrdering(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid parameter for TransitionSet ordering: ");
      localStringBuilder.append(paramInt);
      throw new AndroidRuntimeException(localStringBuilder.toString());
    case 1: 
      mPlayTogether = false;
      break;
    case 0: 
      mPlayTogether = true;
    }
    return this;
  }
  
  public void setPathMotion(PathMotion paramPathMotion)
  {
    super.setPathMotion(paramPathMotion);
    for (int i = 0; i < mTransitions.size(); i++) {
      ((Transition)mTransitions.get(i)).setPathMotion(paramPathMotion);
    }
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation)
  {
    super.setPropagation(paramTransitionPropagation);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).setPropagation(paramTransitionPropagation);
    }
  }
  
  TransitionSet setSceneRoot(ViewGroup paramViewGroup)
  {
    super.setSceneRoot(paramViewGroup);
    int i = mTransitions.size();
    for (int j = 0; j < i; j++) {
      ((Transition)mTransitions.get(j)).setSceneRoot(paramViewGroup);
    }
    return this;
  }
  
  public TransitionSet setStartDelay(long paramLong)
  {
    return (TransitionSet)super.setStartDelay(paramLong);
  }
  
  String toString(String paramString)
  {
    Object localObject = super.toString(paramString);
    for (int i = 0; i < mTransitions.size(); i++)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append((String)localObject);
      localStringBuilder1.append("\n");
      localObject = (Transition)mTransitions.get(i);
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(paramString);
      localStringBuilder2.append("  ");
      localStringBuilder1.append(((Transition)localObject).toString(localStringBuilder2.toString()));
      localObject = localStringBuilder1.toString();
    }
    return localObject;
  }
  
  static class TransitionSetListener
    extends TransitionListenerAdapter
  {
    TransitionSet mTransitionSet;
    
    TransitionSetListener(TransitionSet paramTransitionSet)
    {
      mTransitionSet = paramTransitionSet;
    }
    
    public void onTransitionEnd(Transition paramTransition)
    {
      TransitionSet localTransitionSet = mTransitionSet;
      mCurrentListeners -= 1;
      if (mTransitionSet.mCurrentListeners == 0)
      {
        mTransitionSet.mStarted = false;
        mTransitionSet.end();
      }
      paramTransition.removeListener(this);
    }
    
    public void onTransitionStart(Transition paramTransition)
    {
      if (!mTransitionSet.mStarted)
      {
        mTransitionSet.start();
        mTransitionSet.mStarted = true;
      }
    }
  }
}
