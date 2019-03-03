package android.animation;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LayoutTransition
{
  private static TimeInterpolator ACCEL_DECEL_INTERPOLATOR;
  public static final int APPEARING = 2;
  public static final int CHANGE_APPEARING = 0;
  public static final int CHANGE_DISAPPEARING = 1;
  public static final int CHANGING = 4;
  private static TimeInterpolator DECEL_INTERPOLATOR;
  private static long DEFAULT_DURATION = 300L;
  public static final int DISAPPEARING = 3;
  private static final int FLAG_APPEARING = 1;
  private static final int FLAG_CHANGE_APPEARING = 4;
  private static final int FLAG_CHANGE_DISAPPEARING = 8;
  private static final int FLAG_CHANGING = 16;
  private static final int FLAG_DISAPPEARING = 2;
  private static ObjectAnimator defaultChange;
  private static ObjectAnimator defaultChangeIn;
  private static ObjectAnimator defaultChangeOut;
  private static ObjectAnimator defaultFadeIn;
  private static ObjectAnimator defaultFadeOut;
  private static TimeInterpolator sAppearingInterpolator;
  private static TimeInterpolator sChangingAppearingInterpolator = DECEL_INTERPOLATOR;
  private static TimeInterpolator sChangingDisappearingInterpolator = DECEL_INTERPOLATOR;
  private static TimeInterpolator sChangingInterpolator = DECEL_INTERPOLATOR;
  private static TimeInterpolator sDisappearingInterpolator;
  private final LinkedHashMap<View, Animator> currentAppearingAnimations = new LinkedHashMap();
  private final LinkedHashMap<View, Animator> currentChangingAnimations = new LinkedHashMap();
  private final LinkedHashMap<View, Animator> currentDisappearingAnimations = new LinkedHashMap();
  private final HashMap<View, View.OnLayoutChangeListener> layoutChangeListenerMap = new HashMap();
  private boolean mAnimateParentHierarchy = true;
  private Animator mAppearingAnim = null;
  private long mAppearingDelay = DEFAULT_DURATION;
  private long mAppearingDuration = DEFAULT_DURATION;
  private TimeInterpolator mAppearingInterpolator = sAppearingInterpolator;
  private Animator mChangingAnim = null;
  private Animator mChangingAppearingAnim = null;
  private long mChangingAppearingDelay = 0L;
  private long mChangingAppearingDuration = DEFAULT_DURATION;
  private TimeInterpolator mChangingAppearingInterpolator = sChangingAppearingInterpolator;
  private long mChangingAppearingStagger = 0L;
  private long mChangingDelay = 0L;
  private Animator mChangingDisappearingAnim = null;
  private long mChangingDisappearingDelay = DEFAULT_DURATION;
  private long mChangingDisappearingDuration = DEFAULT_DURATION;
  private TimeInterpolator mChangingDisappearingInterpolator = sChangingDisappearingInterpolator;
  private long mChangingDisappearingStagger = 0L;
  private long mChangingDuration = DEFAULT_DURATION;
  private TimeInterpolator mChangingInterpolator = sChangingInterpolator;
  private long mChangingStagger = 0L;
  private Animator mDisappearingAnim = null;
  private long mDisappearingDelay = 0L;
  private long mDisappearingDuration = DEFAULT_DURATION;
  private TimeInterpolator mDisappearingInterpolator = sDisappearingInterpolator;
  private ArrayList<TransitionListener> mListeners;
  private int mTransitionTypes = 15;
  private final HashMap<View, Animator> pendingAnimations = new HashMap();
  private long staggerDelay;
  
  static
  {
    ACCEL_DECEL_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    DECEL_INTERPOLATOR = new DecelerateInterpolator();
    sAppearingInterpolator = ACCEL_DECEL_INTERPOLATOR;
    sDisappearingInterpolator = ACCEL_DECEL_INTERPOLATOR;
  }
  
  public LayoutTransition()
  {
    if (defaultChangeIn == null)
    {
      defaultChangeIn = ObjectAnimator.ofPropertyValuesHolder(null, new PropertyValuesHolder[] { PropertyValuesHolder.ofInt("left", new int[] { 0, 1 }), PropertyValuesHolder.ofInt("top", new int[] { 0, 1 }), PropertyValuesHolder.ofInt("right", new int[] { 0, 1 }), PropertyValuesHolder.ofInt("bottom", new int[] { 0, 1 }), PropertyValuesHolder.ofInt("scrollX", new int[] { 0, 1 }), PropertyValuesHolder.ofInt("scrollY", new int[] { 0, 1 }) });
      defaultChangeIn.setDuration(DEFAULT_DURATION);
      defaultChangeIn.setStartDelay(mChangingAppearingDelay);
      defaultChangeIn.setInterpolator(mChangingAppearingInterpolator);
      defaultChangeOut = defaultChangeIn.clone();
      defaultChangeOut.setStartDelay(mChangingDisappearingDelay);
      defaultChangeOut.setInterpolator(mChangingDisappearingInterpolator);
      defaultChange = defaultChangeIn.clone();
      defaultChange.setStartDelay(mChangingDelay);
      defaultChange.setInterpolator(mChangingInterpolator);
      defaultFadeIn = ObjectAnimator.ofFloat(null, "alpha", new float[] { 0.0F, 1.0F });
      defaultFadeIn.setDuration(DEFAULT_DURATION);
      defaultFadeIn.setStartDelay(mAppearingDelay);
      defaultFadeIn.setInterpolator(mAppearingInterpolator);
      defaultFadeOut = ObjectAnimator.ofFloat(null, "alpha", new float[] { 1.0F, 0.0F });
      defaultFadeOut.setDuration(DEFAULT_DURATION);
      defaultFadeOut.setStartDelay(mDisappearingDelay);
      defaultFadeOut.setInterpolator(mDisappearingInterpolator);
    }
    mChangingAppearingAnim = defaultChangeIn;
    mChangingDisappearingAnim = defaultChangeOut;
    mChangingAnim = defaultChange;
    mAppearingAnim = defaultFadeIn;
    mDisappearingAnim = defaultFadeOut;
  }
  
  private void addChild(ViewGroup paramViewGroup, View paramView, boolean paramBoolean)
  {
    if (paramViewGroup.getWindowVisibility() != 0) {
      return;
    }
    if ((mTransitionTypes & 0x1) == 1) {
      cancel(3);
    }
    if ((paramBoolean) && ((mTransitionTypes & 0x4) == 4))
    {
      cancel(0);
      cancel(4);
    }
    if ((hasListeners()) && ((mTransitionTypes & 0x1) == 1))
    {
      Iterator localIterator = ((ArrayList)mListeners.clone()).iterator();
      while (localIterator.hasNext()) {
        ((TransitionListener)localIterator.next()).startTransition(this, paramViewGroup, paramView, 2);
      }
    }
    if ((paramBoolean) && ((mTransitionTypes & 0x4) == 4)) {
      runChangeTransition(paramViewGroup, paramView, 2);
    }
    if ((mTransitionTypes & 0x1) == 1) {
      runAppearingTransition(paramViewGroup, paramView);
    }
  }
  
  private boolean hasListeners()
  {
    boolean bool;
    if ((mListeners != null) && (mListeners.size() > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void removeChild(ViewGroup paramViewGroup, View paramView, boolean paramBoolean)
  {
    if (paramViewGroup.getWindowVisibility() != 0) {
      return;
    }
    if ((mTransitionTypes & 0x2) == 2) {
      cancel(2);
    }
    if ((paramBoolean) && ((mTransitionTypes & 0x8) == 8))
    {
      cancel(1);
      cancel(4);
    }
    if ((hasListeners()) && ((mTransitionTypes & 0x2) == 2))
    {
      Iterator localIterator = ((ArrayList)mListeners.clone()).iterator();
      while (localIterator.hasNext()) {
        ((TransitionListener)localIterator.next()).startTransition(this, paramViewGroup, paramView, 3);
      }
    }
    if ((paramBoolean) && ((mTransitionTypes & 0x8) == 8)) {
      runChangeTransition(paramViewGroup, paramView, 3);
    }
    if ((mTransitionTypes & 0x2) == 2) {
      runDisappearingTransition(paramViewGroup, paramView);
    }
  }
  
  private void runAppearingTransition(final ViewGroup paramViewGroup, final View paramView)
  {
    Object localObject = (Animator)currentDisappearingAnimations.get(paramView);
    if (localObject != null) {
      ((Animator)localObject).cancel();
    }
    if (mAppearingAnim == null)
    {
      if (hasListeners())
      {
        localObject = ((ArrayList)mListeners.clone()).iterator();
        while (((Iterator)localObject).hasNext()) {
          ((TransitionListener)((Iterator)localObject).next()).endTransition(this, paramViewGroup, paramView, 2);
        }
      }
      return;
    }
    localObject = mAppearingAnim.clone();
    ((Animator)localObject).setTarget(paramView);
    ((Animator)localObject).setStartDelay(mAppearingDelay);
    ((Animator)localObject).setDuration(mAppearingDuration);
    if (mAppearingInterpolator != sAppearingInterpolator) {
      ((Animator)localObject).setInterpolator(mAppearingInterpolator);
    }
    if ((localObject instanceof ObjectAnimator)) {
      ((ObjectAnimator)localObject).setCurrentPlayTime(0L);
    }
    ((Animator)localObject).addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        currentAppearingAnimations.remove(paramView);
        if (LayoutTransition.this.hasListeners())
        {
          paramAnonymousAnimator = ((ArrayList)mListeners.clone()).iterator();
          while (paramAnonymousAnimator.hasNext()) {
            ((LayoutTransition.TransitionListener)paramAnonymousAnimator.next()).endTransition(LayoutTransition.this, paramViewGroup, paramView, 2);
          }
        }
      }
    });
    currentAppearingAnimations.put(paramView, localObject);
    ((Animator)localObject).start();
  }
  
  private void runChangeTransition(ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    ObjectAnimator localObjectAnimator;
    long l;
    switch (paramInt)
    {
    default: 
      localObject1 = null;
      localObjectAnimator = null;
      l = 0L;
      break;
    case 4: 
      localObject2 = mChangingAnim;
      l = mChangingDuration;
      localObjectAnimator = defaultChange;
      break;
    case 3: 
      localObject2 = mChangingDisappearingAnim;
      l = mChangingDisappearingDuration;
      localObjectAnimator = defaultChangeOut;
      break;
    case 2: 
      localObject2 = mChangingAppearingAnim;
      l = mChangingAppearingDuration;
      localObjectAnimator = defaultChangeIn;
    }
    Object localObject1 = localObject2;
    if (localObject1 == null) {
      return;
    }
    staggerDelay = 0L;
    Object localObject2 = paramViewGroup.getViewTreeObserver();
    if (!((ViewTreeObserver)localObject2).isAlive()) {
      return;
    }
    int i = paramViewGroup.getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = paramViewGroup.getChildAt(j);
      if (localView != paramView) {
        setupChangeAnimation(paramViewGroup, paramInt, (Animator)localObject1, l, localView);
      }
    }
    j = i;
    localObject1 = localObject2;
    if (mAnimateParentHierarchy)
    {
      localObject1 = paramViewGroup;
      paramView = (View)localObject2;
      localObject2 = localObject1;
      for (;;)
      {
        j = i;
        localObject1 = paramView;
        if (localObject2 == null) {
          break;
        }
        localObject1 = ((ViewGroup)localObject2).getParent();
        if ((localObject1 instanceof ViewGroup))
        {
          setupChangeAnimation((ViewGroup)localObject1, paramInt, localObjectAnimator, l, (View)localObject2);
          localObject2 = (ViewGroup)localObject1;
        }
        else
        {
          localObject2 = null;
        }
      }
    }
    paramView = new CleanupCallback(layoutChangeListenerMap, paramViewGroup);
    ((ViewTreeObserver)localObject1).addOnPreDrawListener(paramView);
    paramViewGroup.addOnAttachStateChangeListener(paramView);
  }
  
  private void runDisappearingTransition(final ViewGroup paramViewGroup, final View paramView)
  {
    Object localObject = (Animator)currentAppearingAnimations.get(paramView);
    if (localObject != null) {
      ((Animator)localObject).cancel();
    }
    if (mDisappearingAnim == null)
    {
      if (hasListeners())
      {
        localObject = ((ArrayList)mListeners.clone()).iterator();
        while (((Iterator)localObject).hasNext()) {
          ((TransitionListener)((Iterator)localObject).next()).endTransition(this, paramViewGroup, paramView, 3);
        }
      }
      return;
    }
    localObject = mDisappearingAnim.clone();
    ((Animator)localObject).setStartDelay(mDisappearingDelay);
    ((Animator)localObject).setDuration(mDisappearingDuration);
    if (mDisappearingInterpolator != sDisappearingInterpolator) {
      ((Animator)localObject).setInterpolator(mDisappearingInterpolator);
    }
    ((Animator)localObject).setTarget(paramView);
    ((Animator)localObject).addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        currentDisappearingAnimations.remove(paramView);
        paramView.setAlpha(val$preAnimAlpha);
        if (LayoutTransition.this.hasListeners())
        {
          paramAnonymousAnimator = ((ArrayList)mListeners.clone()).iterator();
          while (paramAnonymousAnimator.hasNext()) {
            ((LayoutTransition.TransitionListener)paramAnonymousAnimator.next()).endTransition(LayoutTransition.this, paramViewGroup, paramView, 3);
          }
        }
      }
    });
    if ((localObject instanceof ObjectAnimator)) {
      ((ObjectAnimator)localObject).setCurrentPlayTime(0L);
    }
    currentDisappearingAnimations.put(paramView, localObject);
    ((Animator)localObject).start();
  }
  
  private void setupChangeAnimation(final ViewGroup paramViewGroup, final int paramInt, final Animator paramAnimator, final long paramLong, final View paramView)
  {
    if (layoutChangeListenerMap.get(paramView) != null) {
      return;
    }
    if ((paramView.getWidth() == 0) && (paramView.getHeight() == 0)) {
      return;
    }
    paramAnimator = paramAnimator.clone();
    paramAnimator.setTarget(paramView);
    paramAnimator.setupStartValues();
    Object localObject = (Animator)pendingAnimations.get(paramView);
    if (localObject != null)
    {
      ((Animator)localObject).cancel();
      pendingAnimations.remove(paramView);
    }
    pendingAnimations.put(paramView, paramAnimator);
    localObject = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F }).setDuration(paramLong + 100L);
    ((ValueAnimator)localObject).addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        pendingAnimations.remove(paramView);
      }
    });
    ((ValueAnimator)localObject).start();
    localObject = new View.OnLayoutChangeListener()
    {
      public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
      {
        paramAnimator.setupEndValues();
        if ((paramAnimator instanceof ValueAnimator))
        {
          paramAnonymousInt1 = 0;
          paramAnonymousView = ((ValueAnimator)paramAnimator).getValues();
          for (paramAnonymousInt2 = 0; paramAnonymousInt2 < paramAnonymousView.length; paramAnonymousInt2++)
          {
            KeyframeSet localKeyframeSet = paramAnonymousView[paramAnonymousInt2];
            if ((mKeyframes instanceof KeyframeSet))
            {
              localKeyframeSet = (KeyframeSet)mKeyframes;
              if ((mFirstKeyframe == null) || (mLastKeyframe == null) || (!mFirstKeyframe.getValue().equals(mLastKeyframe.getValue()))) {
                paramAnonymousInt1 = 1;
              }
            }
            else if (!mKeyframes.getValue(0.0F).equals(mKeyframes.getValue(1.0F)))
            {
              paramAnonymousInt1 = 1;
            }
          }
          if (paramAnonymousInt1 == 0) {
            return;
          }
        }
        long l1 = 0L;
        long l2;
        switch (paramInt)
        {
        default: 
          break;
        case 4: 
          l2 = mChangingDelay + staggerDelay;
          LayoutTransition.access$214(LayoutTransition.this, mChangingStagger);
          l1 = l2;
          if (mChangingInterpolator != LayoutTransition.sChangingInterpolator)
          {
            paramAnimator.setInterpolator(mChangingInterpolator);
            l1 = l2;
          }
          break;
        case 3: 
          l2 = mChangingDisappearingDelay + staggerDelay;
          LayoutTransition.access$214(LayoutTransition.this, mChangingDisappearingStagger);
          l1 = l2;
          if (mChangingDisappearingInterpolator != LayoutTransition.sChangingDisappearingInterpolator)
          {
            paramAnimator.setInterpolator(mChangingDisappearingInterpolator);
            l1 = l2;
          }
          break;
        case 2: 
          l2 = mChangingAppearingDelay + staggerDelay;
          LayoutTransition.access$214(LayoutTransition.this, mChangingAppearingStagger);
          l1 = l2;
          if (mChangingAppearingInterpolator != LayoutTransition.sChangingAppearingInterpolator)
          {
            paramAnimator.setInterpolator(mChangingAppearingInterpolator);
            l1 = l2;
          }
          break;
        }
        paramAnimator.setStartDelay(l1);
        paramAnimator.setDuration(paramLong);
        paramAnonymousView = (Animator)currentChangingAnimations.get(paramViewGroup);
        if (paramAnonymousView != null) {
          paramAnonymousView.cancel();
        }
        if ((Animator)pendingAnimations.get(paramViewGroup) != null) {
          pendingAnimations.remove(paramViewGroup);
        }
        currentChangingAnimations.put(paramViewGroup, paramAnimator);
        val$parent.requestTransitionStart(LayoutTransition.this);
        paramViewGroup.removeOnLayoutChangeListener(this);
        layoutChangeListenerMap.remove(paramViewGroup);
      }
    };
    paramAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator)
      {
        paramView.removeOnLayoutChangeListener(val$listener);
        layoutChangeListenerMap.remove(paramView);
      }
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        currentChangingAnimations.remove(paramView);
        if (LayoutTransition.this.hasListeners())
        {
          Iterator localIterator = ((ArrayList)mListeners.clone()).iterator();
          while (localIterator.hasNext())
          {
            LayoutTransition.TransitionListener localTransitionListener = (LayoutTransition.TransitionListener)localIterator.next();
            paramAnonymousAnimator = LayoutTransition.this;
            ViewGroup localViewGroup = paramViewGroup;
            View localView = paramView;
            int i;
            if (paramInt == 2) {
              i = 0;
            } else if (paramInt == 3) {
              i = 1;
            } else {
              i = 4;
            }
            localTransitionListener.endTransition(paramAnonymousAnimator, localViewGroup, localView, i);
          }
        }
      }
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        if (LayoutTransition.this.hasListeners())
        {
          Iterator localIterator = ((ArrayList)mListeners.clone()).iterator();
          while (localIterator.hasNext())
          {
            paramAnonymousAnimator = (LayoutTransition.TransitionListener)localIterator.next();
            LayoutTransition localLayoutTransition = LayoutTransition.this;
            ViewGroup localViewGroup = paramViewGroup;
            View localView = paramView;
            int i;
            if (paramInt == 2) {
              i = 0;
            } else if (paramInt == 3) {
              i = 1;
            } else {
              i = 4;
            }
            paramAnonymousAnimator.startTransition(localLayoutTransition, localViewGroup, localView, i);
          }
        }
      }
    });
    paramView.addOnLayoutChangeListener((View.OnLayoutChangeListener)localObject);
    layoutChangeListenerMap.put(paramView, localObject);
  }
  
  public void addChild(ViewGroup paramViewGroup, View paramView)
  {
    addChild(paramViewGroup, paramView, true);
  }
  
  public void addTransitionListener(TransitionListener paramTransitionListener)
  {
    if (mListeners == null) {
      mListeners = new ArrayList();
    }
    mListeners.add(paramTransitionListener);
  }
  
  public void cancel()
  {
    Iterator localIterator;
    if (currentChangingAnimations.size() > 0)
    {
      localIterator = ((LinkedHashMap)currentChangingAnimations.clone()).values().iterator();
      while (localIterator.hasNext()) {
        ((Animator)localIterator.next()).cancel();
      }
      currentChangingAnimations.clear();
    }
    if (currentAppearingAnimations.size() > 0)
    {
      localIterator = ((LinkedHashMap)currentAppearingAnimations.clone()).values().iterator();
      while (localIterator.hasNext()) {
        ((Animator)localIterator.next()).end();
      }
      currentAppearingAnimations.clear();
    }
    if (currentDisappearingAnimations.size() > 0)
    {
      localIterator = ((LinkedHashMap)currentDisappearingAnimations.clone()).values().iterator();
      while (localIterator.hasNext()) {
        ((Animator)localIterator.next()).end();
      }
      currentDisappearingAnimations.clear();
    }
  }
  
  public void cancel(int paramInt)
  {
    Iterator localIterator;
    switch (paramInt)
    {
    default: 
      break;
    case 3: 
      if (currentDisappearingAnimations.size() > 0)
      {
        localIterator = ((LinkedHashMap)currentDisappearingAnimations.clone()).values().iterator();
        while (localIterator.hasNext()) {
          ((Animator)localIterator.next()).end();
        }
        currentDisappearingAnimations.clear();
      }
      break;
    case 2: 
      if (currentAppearingAnimations.size() > 0)
      {
        localIterator = ((LinkedHashMap)currentAppearingAnimations.clone()).values().iterator();
        while (localIterator.hasNext()) {
          ((Animator)localIterator.next()).end();
        }
        currentAppearingAnimations.clear();
      }
      break;
    case 0: 
    case 1: 
    case 4: 
      if (currentChangingAnimations.size() > 0)
      {
        localIterator = ((LinkedHashMap)currentChangingAnimations.clone()).values().iterator();
        while (localIterator.hasNext()) {
          ((Animator)localIterator.next()).cancel();
        }
        currentChangingAnimations.clear();
      }
      break;
    }
  }
  
  public void disableTransitionType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mTransitionTypes &= 0xFFFFFFEF;
      break;
    case 3: 
      mTransitionTypes &= 0xFFFFFFFD;
      break;
    case 2: 
      mTransitionTypes &= 0xFFFFFFFE;
      break;
    case 1: 
      mTransitionTypes &= 0xFFFFFFF7;
      break;
    case 0: 
      mTransitionTypes &= 0xFFFFFFFB;
    }
  }
  
  public void enableTransitionType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mTransitionTypes |= 0x10;
      break;
    case 3: 
      mTransitionTypes |= 0x2;
      break;
    case 2: 
      mTransitionTypes |= 0x1;
      break;
    case 1: 
      mTransitionTypes |= 0x8;
      break;
    case 0: 
      mTransitionTypes |= 0x4;
    }
  }
  
  public void endChangingAnimations()
  {
    Iterator localIterator = ((LinkedHashMap)currentChangingAnimations.clone()).values().iterator();
    while (localIterator.hasNext())
    {
      Animator localAnimator = (Animator)localIterator.next();
      localAnimator.start();
      localAnimator.end();
    }
    currentChangingAnimations.clear();
  }
  
  public Animator getAnimator(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 4: 
      return mChangingAnim;
    case 3: 
      return mDisappearingAnim;
    case 2: 
      return mAppearingAnim;
    case 1: 
      return mChangingDisappearingAnim;
    }
    return mChangingAppearingAnim;
  }
  
  public long getDuration(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0L;
    case 4: 
      return mChangingDuration;
    case 3: 
      return mDisappearingDuration;
    case 2: 
      return mAppearingDuration;
    case 1: 
      return mChangingDisappearingDuration;
    }
    return mChangingAppearingDuration;
  }
  
  public TimeInterpolator getInterpolator(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 4: 
      return mChangingInterpolator;
    case 3: 
      return mDisappearingInterpolator;
    case 2: 
      return mAppearingInterpolator;
    case 1: 
      return mChangingDisappearingInterpolator;
    }
    return mChangingAppearingInterpolator;
  }
  
  public long getStagger(int paramInt)
  {
    if (paramInt != 4)
    {
      switch (paramInt)
      {
      default: 
        return 0L;
      case 1: 
        return mChangingDisappearingStagger;
      }
      return mChangingAppearingStagger;
    }
    return mChangingStagger;
  }
  
  public long getStartDelay(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0L;
    case 4: 
      return mChangingDelay;
    case 3: 
      return mDisappearingDelay;
    case 2: 
      return mAppearingDelay;
    case 1: 
      return mChangingDisappearingDelay;
    }
    return mChangingAppearingDelay;
  }
  
  public List<TransitionListener> getTransitionListeners()
  {
    return mListeners;
  }
  
  @Deprecated
  public void hideChild(ViewGroup paramViewGroup, View paramView)
  {
    removeChild(paramViewGroup, paramView, true);
  }
  
  public void hideChild(ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    boolean bool;
    if (paramInt == 8) {
      bool = true;
    } else {
      bool = false;
    }
    removeChild(paramViewGroup, paramView, bool);
  }
  
  public boolean isChangingLayout()
  {
    boolean bool;
    if (currentChangingAnimations.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRunning()
  {
    boolean bool;
    if ((currentChangingAnimations.size() <= 0) && (currentAppearingAnimations.size() <= 0) && (currentDisappearingAnimations.size() <= 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isTransitionTypeEnabled(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    switch (paramInt)
    {
    default: 
      return false;
    case 4: 
      if ((mTransitionTypes & 0x10) == 16) {
        bool5 = true;
      }
      return bool5;
    case 3: 
      bool5 = bool1;
      if ((mTransitionTypes & 0x2) == 2) {
        bool5 = true;
      }
      return bool5;
    case 2: 
      bool5 = bool2;
      if ((mTransitionTypes & 0x1) == 1) {
        bool5 = true;
      }
      return bool5;
    case 1: 
      bool5 = bool3;
      if ((mTransitionTypes & 0x8) == 8) {
        bool5 = true;
      }
      return bool5;
    }
    bool5 = bool4;
    if ((mTransitionTypes & 0x4) == 4) {
      bool5 = true;
    }
    return bool5;
  }
  
  public void layoutChange(ViewGroup paramViewGroup)
  {
    if (paramViewGroup.getWindowVisibility() != 0) {
      return;
    }
    if (((mTransitionTypes & 0x10) == 16) && (!isRunning())) {
      runChangeTransition(paramViewGroup, null, 4);
    }
  }
  
  public void removeChild(ViewGroup paramViewGroup, View paramView)
  {
    removeChild(paramViewGroup, paramView, true);
  }
  
  public void removeTransitionListener(TransitionListener paramTransitionListener)
  {
    if (mListeners == null) {
      return;
    }
    mListeners.remove(paramTransitionListener);
  }
  
  public void setAnimateParentHierarchy(boolean paramBoolean)
  {
    mAnimateParentHierarchy = paramBoolean;
  }
  
  public void setAnimator(int paramInt, Animator paramAnimator)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mChangingAnim = paramAnimator;
      break;
    case 3: 
      mDisappearingAnim = paramAnimator;
      break;
    case 2: 
      mAppearingAnim = paramAnimator;
      break;
    case 1: 
      mChangingDisappearingAnim = paramAnimator;
      break;
    case 0: 
      mChangingAppearingAnim = paramAnimator;
    }
  }
  
  public void setDuration(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mChangingDuration = paramLong;
      break;
    case 3: 
      mDisappearingDuration = paramLong;
      break;
    case 2: 
      mAppearingDuration = paramLong;
      break;
    case 1: 
      mChangingDisappearingDuration = paramLong;
      break;
    case 0: 
      mChangingAppearingDuration = paramLong;
    }
  }
  
  public void setDuration(long paramLong)
  {
    mChangingAppearingDuration = paramLong;
    mChangingDisappearingDuration = paramLong;
    mChangingDuration = paramLong;
    mAppearingDuration = paramLong;
    mDisappearingDuration = paramLong;
  }
  
  public void setInterpolator(int paramInt, TimeInterpolator paramTimeInterpolator)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mChangingInterpolator = paramTimeInterpolator;
      break;
    case 3: 
      mDisappearingInterpolator = paramTimeInterpolator;
      break;
    case 2: 
      mAppearingInterpolator = paramTimeInterpolator;
      break;
    case 1: 
      mChangingDisappearingInterpolator = paramTimeInterpolator;
      break;
    case 0: 
      mChangingAppearingInterpolator = paramTimeInterpolator;
    }
  }
  
  public void setStagger(int paramInt, long paramLong)
  {
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        break;
      case 1: 
        mChangingDisappearingStagger = paramLong;
        break;
      case 0: 
        mChangingAppearingStagger = paramLong;
        break;
      }
    } else {
      mChangingStagger = paramLong;
    }
  }
  
  public void setStartDelay(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      mChangingDelay = paramLong;
      break;
    case 3: 
      mDisappearingDelay = paramLong;
      break;
    case 2: 
      mAppearingDelay = paramLong;
      break;
    case 1: 
      mChangingDisappearingDelay = paramLong;
      break;
    case 0: 
      mChangingAppearingDelay = paramLong;
    }
  }
  
  @Deprecated
  public void showChild(ViewGroup paramViewGroup, View paramView)
  {
    addChild(paramViewGroup, paramView, true);
  }
  
  public void showChild(ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    boolean bool;
    if (paramInt == 8) {
      bool = true;
    } else {
      bool = false;
    }
    addChild(paramViewGroup, paramView, bool);
  }
  
  public void startChangingAnimations()
  {
    Iterator localIterator = ((LinkedHashMap)currentChangingAnimations.clone()).values().iterator();
    while (localIterator.hasNext())
    {
      Animator localAnimator = (Animator)localIterator.next();
      if ((localAnimator instanceof ObjectAnimator)) {
        ((ObjectAnimator)localAnimator).setCurrentPlayTime(0L);
      }
      localAnimator.start();
    }
  }
  
  private static final class CleanupCallback
    implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener
  {
    final Map<View, View.OnLayoutChangeListener> layoutChangeListenerMap;
    final ViewGroup parent;
    
    CleanupCallback(Map<View, View.OnLayoutChangeListener> paramMap, ViewGroup paramViewGroup)
    {
      layoutChangeListenerMap = paramMap;
      parent = paramViewGroup;
    }
    
    private void cleanup()
    {
      parent.getViewTreeObserver().removeOnPreDrawListener(this);
      parent.removeOnAttachStateChangeListener(this);
      if (layoutChangeListenerMap.size() > 0)
      {
        Iterator localIterator = layoutChangeListenerMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          View localView = (View)localIterator.next();
          localView.removeOnLayoutChangeListener((View.OnLayoutChangeListener)layoutChangeListenerMap.get(localView));
        }
        layoutChangeListenerMap.clear();
      }
    }
    
    public boolean onPreDraw()
    {
      cleanup();
      return true;
    }
    
    public void onViewAttachedToWindow(View paramView) {}
    
    public void onViewDetachedFromWindow(View paramView)
    {
      cleanup();
    }
  }
  
  public static abstract interface TransitionListener
  {
    public abstract void endTransition(LayoutTransition paramLayoutTransition, ViewGroup paramViewGroup, View paramView, int paramInt);
    
    public abstract void startTransition(LayoutTransition paramLayoutTransition, ViewGroup paramViewGroup, View paramView, int paramInt);
  }
}
