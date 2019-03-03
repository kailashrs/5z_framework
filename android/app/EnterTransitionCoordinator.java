package android.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import com.android.internal.view.OneShotPreDrawListener;
import java.util.ArrayList;

class EnterTransitionCoordinator
  extends ActivityTransitionCoordinator
{
  private static final int MIN_ANIMATION_FRAMES = 2;
  private static final String TAG = "EnterTransitionCoordinator";
  private Activity mActivity;
  private boolean mAreViewsReady;
  private ObjectAnimator mBackgroundAnimator;
  private Transition mEnterViewsTransition;
  private boolean mHasStopped;
  private boolean mIsCanceled;
  private final boolean mIsCrossTask;
  private boolean mIsExitTransitionComplete;
  private boolean mIsReadyForTransition;
  private boolean mIsViewsTransitionStarted;
  private Drawable mReplacedBackground;
  private boolean mSharedElementTransitionStarted;
  private Bundle mSharedElementsBundle;
  private OneShotPreDrawListener mViewsReadyListener;
  private boolean mWasOpaque;
  
  public EnterTransitionCoordinator(final Activity paramActivity, final ResultReceiver paramResultReceiver, ArrayList<String> paramArrayList, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(localWindow, paramArrayList, getListener(paramActivity, bool), paramBoolean1);
    mActivity = paramActivity;
    mIsCrossTask = paramBoolean2;
    setResultReceiver(paramResultReceiver);
    prepareEnter();
    paramActivity = new Bundle();
    paramActivity.putParcelable("android:remoteReceiver", this);
    mResultReceiver.send(100, paramActivity);
    paramActivity = getDecor();
    if (paramActivity != null)
    {
      paramResultReceiver = paramActivity.getViewTreeObserver();
      paramResultReceiver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
      {
        public boolean onPreDraw()
        {
          if (mIsReadyForTransition) {
            if (paramResultReceiver.isAlive()) {
              paramResultReceiver.removeOnPreDrawListener(this);
            } else {
              paramActivity.getViewTreeObserver().removeOnPreDrawListener(this);
            }
          }
          return false;
        }
      });
    }
  }
  
  private boolean allowOverlappingTransitions()
  {
    boolean bool;
    if (mIsReturning) {
      bool = getWindow().getAllowReturnTransitionOverlap();
    } else {
      bool = getWindow().getAllowEnterTransitionOverlap();
    }
    return bool;
  }
  
  private Transition beginTransition(ViewGroup paramViewGroup, boolean paramBoolean1, boolean paramBoolean2)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramBoolean2)
    {
      if (!mSharedElementNames.isEmpty()) {
        localObject2 = configureTransition(getSharedElementTransition(), false);
      }
      if (localObject2 == null)
      {
        sharedElementTransitionStarted();
        sharedElementTransitionComplete();
        localObject1 = localObject2;
      }
      else
      {
        ((Transition)localObject2).addListener(new TransitionListenerAdapter()
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            paramAnonymousTransition.removeListener(this);
            sharedElementTransitionComplete();
          }
          
          public void onTransitionStart(Transition paramAnonymousTransition)
          {
            EnterTransitionCoordinator.this.sharedElementTransitionStarted();
          }
        });
        localObject1 = localObject2;
      }
    }
    localObject2 = null;
    Object localObject3 = null;
    if (paramBoolean1)
    {
      mIsViewsTransitionStarted = true;
      localObject2 = localObject3;
      if (mTransitioningViews != null)
      {
        localObject2 = localObject3;
        if (!mTransitioningViews.isEmpty()) {
          localObject2 = configureTransition(getViewsTransition(), true);
        }
      }
      if (localObject2 == null) {
        viewsTransitionComplete();
      } else {
        ((Transition)localObject2).addListener(new ActivityTransitionCoordinator.ContinueTransitionListener(mTransitioningViews)
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            EnterTransitionCoordinator.access$302(EnterTransitionCoordinator.this, null);
            paramAnonymousTransition.removeListener(this);
            viewsTransitionComplete();
            super.onTransitionEnd(paramAnonymousTransition);
          }
          
          public void onTransitionStart(Transition paramAnonymousTransition)
          {
            EnterTransitionCoordinator.access$302(EnterTransitionCoordinator.this, paramAnonymousTransition);
            if (val$transitioningViews != null) {
              showViews(val$transitioningViews, false);
            }
            super.onTransitionStart(paramAnonymousTransition);
          }
        });
      }
    }
    localObject2 = mergeTransitions(localObject1, (Transition)localObject2);
    if (localObject2 != null)
    {
      ((Transition)localObject2).addListener(new ActivityTransitionCoordinator.ContinueTransitionListener(this));
      if (paramBoolean1) {
        setTransitioningViewsVisiblity(4, false);
      }
      TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)localObject2);
      if (paramBoolean1) {
        setTransitioningViewsVisiblity(0, false);
      }
      paramViewGroup.invalidate();
    }
    else
    {
      transitionStarted();
    }
    return localObject2;
  }
  
  private void cancel()
  {
    if (!mIsCanceled)
    {
      mIsCanceled = true;
      if ((getViewsTransition() != null) && (!mIsViewsTransitionStarted))
      {
        if (mTransitioningViews != null) {
          mTransitioningViews.addAll(mSharedElements);
        }
      }
      else {
        showViews(mSharedElements, true);
      }
      moveSharedElementsFromOverlay();
      mSharedElementNames.clear();
      mSharedElements.clear();
      mAllSharedElementNames.clear();
      startSharedElementTransition(null);
      onRemoteExitTransitionComplete();
    }
  }
  
  private static SharedElementCallback getListener(Activity paramActivity, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramActivity = mExitTransitionListener;
    } else {
      paramActivity = mEnterTransitionListener;
    }
    return paramActivity;
  }
  
  private void makeOpaque()
  {
    if ((!mHasStopped) && (mActivity != null))
    {
      if (mWasOpaque) {
        mActivity.convertFromTranslucent();
      }
      mActivity = null;
    }
  }
  
  private ArrayMap<String, View> mapNamedElements(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2)
  {
    ArrayMap localArrayMap = new ArrayMap();
    Object localObject1 = getDecor();
    if (localObject1 != null) {
      ((ViewGroup)localObject1).findNamedViews(localArrayMap);
    }
    if (paramArrayList1 != null) {
      for (int i = 0; i < paramArrayList2.size(); i++)
      {
        Object localObject2 = (String)paramArrayList2.get(i);
        localObject1 = (String)paramArrayList1.get(i);
        if ((localObject2 != null) && (!((String)localObject2).equals(localObject1)))
        {
          localObject2 = (View)localArrayMap.get(localObject2);
          if (localObject2 != null) {
            localArrayMap.put(localObject1, localObject2);
          }
        }
      }
    }
    return localArrayMap;
  }
  
  private void onTakeSharedElements()
  {
    if ((mIsReadyForTransition) && (mSharedElementsBundle != null))
    {
      Object localObject = mSharedElementsBundle;
      mSharedElementsBundle = null;
      localObject = new SharedElementCallback.OnSharedElementsReadyListener()
      {
        public void onSharedElementsReady()
        {
          ViewGroup localViewGroup = getDecor();
          if (localViewGroup != null)
          {
            OneShotPreDrawListener.add(localViewGroup, false, new _..Lambda.EnterTransitionCoordinator.3.I_t9rJUkrW7bwRLQtTrE8DgvPZs(this, val$sharedElementState));
            localViewGroup.invalidate();
          }
        }
      };
      if (mListener == null) {
        ((SharedElementCallback.OnSharedElementsReadyListener)localObject).onSharedElementsReady();
      } else {
        mListener.onSharedElementsArrived(mSharedElementNames, mSharedElements, (SharedElementCallback.OnSharedElementsReadyListener)localObject);
      }
      return;
    }
  }
  
  private static void removeNullViews(ArrayList<View> paramArrayList)
  {
    if (paramArrayList != null) {
      for (int i = paramArrayList.size() - 1; i >= 0; i--) {
        if (paramArrayList.get(i) == null) {
          paramArrayList.remove(i);
        }
      }
    }
  }
  
  private void requestLayoutForSharedElements()
  {
    int i = mSharedElements.size();
    for (int j = 0; j < i; j++) {
      ((View)mSharedElements.get(j)).requestLayout();
    }
  }
  
  private void sendSharedElementDestination()
  {
    Object localObject = getDecor();
    int i;
    if ((allowOverlappingTransitions()) && (getEnterViewsTransition() != null)) {
      i = 0;
    }
    for (;;)
    {
      break;
      if (localObject == null)
      {
        i = 1;
      }
      else
      {
        boolean bool = ((View)localObject).isLayoutRequested() ^ true;
        i = bool;
        if (bool) {
          for (int j = 0;; j++)
          {
            i = bool;
            if (j >= mSharedElements.size()) {
              break;
            }
            if (((View)mSharedElements.get(j)).isLayoutRequested())
            {
              i = 0;
              break;
            }
          }
        }
      }
    }
    if (i != 0)
    {
      localObject = captureSharedElementState();
      moveSharedElementsToOverlay();
      mResultReceiver.send(107, (Bundle)localObject);
    }
    else if (localObject != null)
    {
      OneShotPreDrawListener.add((View)localObject, new _..Lambda.EnterTransitionCoordinator.dV8bqDBqB_WsCnMyvajWuP4ArwA(this));
    }
    if (allowOverlappingTransitions()) {
      startEnterTransitionOnly();
    }
  }
  
  private void sharedElementTransitionStarted()
  {
    mSharedElementTransitionStarted = true;
    if (mIsExitTransitionComplete) {
      send(104, null);
    }
  }
  
  private void startEnterTransition(Transition paramTransition)
  {
    Object localObject = getDecor();
    if ((!mIsReturning) && (localObject != null))
    {
      localObject = ((ViewGroup)localObject).getBackground();
      if (localObject != null)
      {
        paramTransition = ((Drawable)localObject).mutate();
        getWindow().setBackgroundDrawable(paramTransition);
        mBackgroundAnimator = ObjectAnimator.ofInt(paramTransition, "alpha", new int[] { 255 });
        mBackgroundAnimator.setDuration(getFadeDuration());
        mBackgroundAnimator.addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            EnterTransitionCoordinator.this.makeOpaque();
            backgroundAnimatorComplete();
          }
        });
        mBackgroundAnimator.start();
      }
      else if (paramTransition != null)
      {
        paramTransition.addListener(new TransitionListenerAdapter()
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            paramAnonymousTransition.removeListener(this);
            EnterTransitionCoordinator.this.makeOpaque();
          }
        });
        backgroundAnimatorComplete();
      }
      else
      {
        makeOpaque();
        backgroundAnimatorComplete();
      }
    }
    else
    {
      backgroundAnimatorComplete();
    }
  }
  
  private void startEnterTransitionOnly()
  {
    startTransition(new Runnable()
    {
      public void run()
      {
        Object localObject = getDecor();
        if (localObject != null)
        {
          localObject = EnterTransitionCoordinator.this.beginTransition((ViewGroup)localObject, true, false);
          EnterTransitionCoordinator.this.startEnterTransition((Transition)localObject);
        }
      }
    });
  }
  
  private void startRejectedAnimations(final ArrayList<View> paramArrayList)
  {
    if ((paramArrayList != null) && (!paramArrayList.isEmpty()))
    {
      final ViewGroup localViewGroup = getDecor();
      if (localViewGroup != null)
      {
        ViewGroupOverlay localViewGroupOverlay = localViewGroup.getOverlay();
        Object localObject = null;
        int i = paramArrayList.size();
        for (int j = 0; j < i; j++)
        {
          localObject = (View)paramArrayList.get(j);
          localViewGroupOverlay.add((View)localObject);
          localObject = ObjectAnimator.ofFloat(localObject, View.ALPHA, new float[] { 1.0F, 0.0F });
          ((ObjectAnimator)localObject).start();
        }
        ((ObjectAnimator)localObject).addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            paramAnonymousAnimator = localViewGroup.getOverlay();
            int i = paramArrayList.size();
            for (int j = 0; j < i; j++) {
              paramAnonymousAnimator.remove((View)paramArrayList.get(j));
            }
          }
        });
      }
      return;
    }
  }
  
  private void startSharedElementTransition(Bundle paramBundle)
  {
    ViewGroup localViewGroup = getDecor();
    if (localViewGroup == null) {
      return;
    }
    ArrayList localArrayList1 = new ArrayList(mAllSharedElementNames);
    localArrayList1.removeAll(mSharedElementNames);
    localArrayList1 = createSnapshots(paramBundle, localArrayList1);
    if (mListener != null) {
      mListener.onRejectSharedElements(localArrayList1);
    }
    removeNullViews(localArrayList1);
    startRejectedAnimations(localArrayList1);
    ArrayList localArrayList2 = createSnapshots(paramBundle, mSharedElementNames);
    localArrayList1 = mSharedElements;
    boolean bool = true;
    showViews(localArrayList1, true);
    scheduleSetSharedElementEnd(localArrayList2);
    localArrayList1 = setSharedElementState(paramBundle, localArrayList2);
    requestLayoutForSharedElements();
    if ((!allowOverlappingTransitions()) || (mIsReturning)) {
      bool = false;
    }
    setGhostVisibility(4);
    scheduleGhostVisibilityChange(4);
    pauseInput();
    paramBundle = beginTransition(localViewGroup, bool, true);
    scheduleGhostVisibilityChange(0);
    setGhostVisibility(0);
    if (bool) {
      startEnterTransition(paramBundle);
    }
    setOriginalSharedElementState(mSharedElements, localArrayList1);
    if (mResultReceiver != null) {
      localViewGroup.postOnAnimation(new Runnable()
      {
        int mAnimations;
        
        public void run()
        {
          int i = mAnimations;
          mAnimations = (i + 1);
          if (i < 2)
          {
            ViewGroup localViewGroup = getDecor();
            if (localViewGroup != null) {
              localViewGroup.postOnAnimation(this);
            }
          }
          else if (mResultReceiver != null)
          {
            mResultReceiver.send(101, null);
            mResultReceiver = null;
          }
        }
      });
    }
  }
  
  private void triggerViewsReady(ArrayMap<String, View> paramArrayMap)
  {
    if (mAreViewsReady) {
      return;
    }
    mAreViewsReady = true;
    ViewGroup localViewGroup = getDecor();
    if ((localViewGroup != null) && ((!localViewGroup.isAttachedToWindow()) || ((!paramArrayMap.isEmpty()) && (((View)paramArrayMap.valueAt(0)).isLayoutRequested()))))
    {
      mViewsReadyListener = OneShotPreDrawListener.add(localViewGroup, new _..Lambda.EnterTransitionCoordinator.wYWFlx9zS3bxJYkN44Bpwx_EKis(this, paramArrayMap));
      localViewGroup.invalidate();
    }
    else
    {
      viewsReady(paramArrayMap);
    }
  }
  
  public boolean cancelEnter()
  {
    setGhostVisibility(4);
    mHasStopped = true;
    mIsCanceled = true;
    clearState();
    return super.cancelPendingTransitions();
  }
  
  protected void clearState()
  {
    mSharedElementsBundle = null;
    mEnterViewsTransition = null;
    mResultReceiver = null;
    if (mBackgroundAnimator != null)
    {
      mBackgroundAnimator.cancel();
      mBackgroundAnimator = null;
    }
    super.clearState();
  }
  
  public void forceViewsToAppear()
  {
    if (!mIsReturning) {
      return;
    }
    if (!mIsReadyForTransition)
    {
      mIsReadyForTransition = true;
      if ((getDecor() != null) && (mViewsReadyListener != null))
      {
        mViewsReadyListener.removeListener();
        mViewsReadyListener = null;
      }
      showViews(mTransitioningViews, true);
      setTransitioningViewsVisiblity(0, true);
      mSharedElements.clear();
      mAllSharedElementNames.clear();
      mTransitioningViews.clear();
      mIsReadyForTransition = true;
      viewsTransitionComplete();
      sharedElementTransitionComplete();
    }
    else
    {
      if (!mSharedElementTransitionStarted)
      {
        moveSharedElementsFromOverlay();
        mSharedElementTransitionStarted = true;
        showViews(mSharedElements, true);
        mSharedElements.clear();
        sharedElementTransitionComplete();
      }
      if (!mIsViewsTransitionStarted)
      {
        mIsViewsTransitionStarted = true;
        showViews(mTransitioningViews, true);
        setTransitioningViewsVisiblity(0, true);
        mTransitioningViews.clear();
        viewsTransitionComplete();
      }
      cancelPendingTransitions();
    }
    mAreViewsReady = true;
    if (mResultReceiver != null)
    {
      mResultReceiver.send(106, null);
      mResultReceiver = null;
    }
  }
  
  public Transition getEnterViewsTransition()
  {
    return mEnterViewsTransition;
  }
  
  protected Transition getSharedElementTransition()
  {
    Window localWindow = getWindow();
    if (localWindow == null) {
      return null;
    }
    if (mIsReturning) {
      return localWindow.getSharedElementReenterTransition();
    }
    return localWindow.getSharedElementEnterTransition();
  }
  
  protected Transition getViewsTransition()
  {
    Window localWindow = getWindow();
    if (localWindow == null) {
      return null;
    }
    if (mIsReturning) {
      return localWindow.getReenterTransition();
    }
    return localWindow.getEnterTransition();
  }
  
  boolean isCrossTask()
  {
    return mIsCrossTask;
  }
  
  public boolean isReturning()
  {
    return mIsReturning;
  }
  
  public boolean isWaitingForRemoteExit()
  {
    boolean bool;
    if ((mIsReturning) && (mResultReceiver != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void namedViewsReady(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2)
  {
    triggerViewsReady(mapNamedElements(paramArrayList1, paramArrayList2));
  }
  
  protected void onReceiveResult(int paramInt, Bundle paramBundle)
  {
    switch (paramInt)
    {
    case 105: 
    default: 
      break;
    case 106: 
      cancel();
      break;
    case 104: 
      if (!mIsCanceled)
      {
        mIsExitTransitionComplete = true;
        if (mSharedElementTransitionStarted) {
          onRemoteExitTransitionComplete();
        }
      }
      break;
    case 103: 
      if (!mIsCanceled)
      {
        mSharedElementsBundle = paramBundle;
        onTakeSharedElements();
      }
      break;
    }
  }
  
  protected void onRemoteExitTransitionComplete()
  {
    if (!allowOverlappingTransitions()) {
      startEnterTransitionOnly();
    }
  }
  
  protected void onTransitionsComplete()
  {
    moveSharedElementsFromOverlay();
    ViewGroup localViewGroup = getDecor();
    if (localViewGroup != null)
    {
      localViewGroup.sendAccessibilityEvent(2048);
      Window localWindow = getWindow();
      if ((localWindow != null) && (mReplacedBackground == localViewGroup.getBackground())) {
        localWindow.setBackgroundDrawable(null);
      }
    }
  }
  
  protected void prepareEnter()
  {
    Object localObject = getDecor();
    if ((mActivity != null) && (localObject != null))
    {
      if (!isCrossTask()) {
        mActivity.overridePendingTransition(0, 0);
      }
      if (!mIsReturning)
      {
        mWasOpaque = mActivity.convertToTranslucent(null, null);
        localObject = ((ViewGroup)localObject).getBackground();
        if (localObject == null)
        {
          localObject = new ColorDrawable(0);
          mReplacedBackground = ((Drawable)localObject);
        }
        else
        {
          getWindow().setBackgroundDrawable(null);
          localObject = ((Drawable)localObject).mutate();
          ((Drawable)localObject).setAlpha(0);
        }
        getWindow().setBackgroundDrawable((Drawable)localObject);
      }
      else
      {
        mActivity = null;
      }
      return;
    }
  }
  
  public void stop()
  {
    if (mBackgroundAnimator != null)
    {
      mBackgroundAnimator.end();
      mBackgroundAnimator = null;
    }
    else if (mWasOpaque)
    {
      Object localObject = getDecor();
      if (localObject != null)
      {
        localObject = ((ViewGroup)localObject).getBackground();
        if (localObject != null) {
          ((Drawable)localObject).setAlpha(1);
        }
      }
    }
    makeOpaque();
    mIsCanceled = true;
    mResultReceiver = null;
    mActivity = null;
    moveSharedElementsFromOverlay();
    if (mTransitioningViews != null)
    {
      showViews(mTransitioningViews, true);
      setTransitioningViewsVisiblity(0, true);
    }
    showViews(mSharedElements, true);
    clearState();
  }
  
  public void viewInstancesReady(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2, ArrayList<View> paramArrayList)
  {
    int i = 0;
    for (int j = 0;; j++)
    {
      k = i;
      if (j >= paramArrayList.size()) {
        break label70;
      }
      View localView = (View)paramArrayList.get(j);
      if ((!TextUtils.equals(localView.getTransitionName(), (CharSequence)paramArrayList2.get(j))) || (!localView.isAttachedToWindow())) {
        break;
      }
    }
    int k = 1;
    label70:
    if (k != 0) {
      triggerViewsReady(mapNamedElements(paramArrayList1, paramArrayList2));
    } else {
      triggerViewsReady(mapSharedElements(paramArrayList1, paramArrayList));
    }
  }
  
  protected void viewsReady(ArrayMap<String, View> paramArrayMap)
  {
    super.viewsReady(paramArrayMap);
    mIsReadyForTransition = true;
    hideViews(mSharedElements);
    paramArrayMap = getViewsTransition();
    if ((paramArrayMap != null) && (mTransitioningViews != null))
    {
      removeExcludedViews(paramArrayMap, mTransitioningViews);
      stripOffscreenViews();
      hideViews(mTransitioningViews);
    }
    if (mIsReturning) {
      sendSharedElementDestination();
    } else {
      moveSharedElementsToOverlay();
    }
    if (mSharedElementsBundle != null) {
      onTakeSharedElements();
    }
  }
}
