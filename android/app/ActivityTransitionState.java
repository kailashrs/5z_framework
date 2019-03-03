package android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.internal.view.OneShotPreDrawListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class ActivityTransitionState
{
  private static final String ENTERING_SHARED_ELEMENTS = "android:enteringSharedElements";
  private static final String EXITING_MAPPED_FROM = "android:exitingMappedFrom";
  private static final String EXITING_MAPPED_TO = "android:exitingMappedTo";
  private ExitTransitionCoordinator mCalledExitCoordinator;
  private ActivityOptions mEnterActivityOptions;
  private EnterTransitionCoordinator mEnterTransitionCoordinator;
  private ArrayList<String> mEnteringNames;
  private SparseArray<WeakReference<ExitTransitionCoordinator>> mExitTransitionCoordinators;
  private int mExitTransitionCoordinatorsKey = 1;
  private ArrayList<String> mExitingFrom;
  private ArrayList<String> mExitingTo;
  private ArrayList<View> mExitingToView;
  private boolean mHasExited;
  private boolean mIsEnterPostponed;
  private boolean mIsEnterTriggered;
  private ExitTransitionCoordinator mReturnExitCoordinator;
  
  public ActivityTransitionState() {}
  
  private void restoreExitedViews()
  {
    if (mCalledExitCoordinator != null)
    {
      mCalledExitCoordinator.resetViews();
      mCalledExitCoordinator = null;
    }
  }
  
  private void restoreReenteringViews()
  {
    if ((mEnterTransitionCoordinator != null) && (mEnterTransitionCoordinator.isReturning()) && (!mEnterTransitionCoordinator.isCrossTask()))
    {
      mEnterTransitionCoordinator.forceViewsToAppear();
      mExitingFrom = null;
      mExitingTo = null;
      mExitingToView = null;
    }
  }
  
  private void startEnter()
  {
    if (mEnterTransitionCoordinator.isReturning())
    {
      if (mExitingToView != null) {
        mEnterTransitionCoordinator.viewInstancesReady(mExitingFrom, mExitingTo, mExitingToView);
      } else {
        mEnterTransitionCoordinator.namedViewsReady(mExitingFrom, mExitingTo);
      }
    }
    else
    {
      mEnterTransitionCoordinator.namedViewsReady(null, null);
      mEnteringNames = mEnterTransitionCoordinator.getAllSharedElementNames();
    }
    mExitingFrom = null;
    mExitingTo = null;
    mExitingToView = null;
    mEnterActivityOptions = null;
  }
  
  public int addExitTransitionCoordinator(ExitTransitionCoordinator paramExitTransitionCoordinator)
  {
    if (mExitTransitionCoordinators == null) {
      mExitTransitionCoordinators = new SparseArray();
    }
    paramExitTransitionCoordinator = new WeakReference(paramExitTransitionCoordinator);
    for (int i = mExitTransitionCoordinators.size() - 1; i >= 0; i--) {
      if (((WeakReference)mExitTransitionCoordinators.valueAt(i)).get() == null) {
        mExitTransitionCoordinators.removeAt(i);
      }
    }
    i = mExitTransitionCoordinatorsKey;
    mExitTransitionCoordinatorsKey = (i + 1);
    mExitTransitionCoordinators.append(i, paramExitTransitionCoordinator);
    return i;
  }
  
  public void clear()
  {
    mEnteringNames = null;
    mExitingFrom = null;
    mExitingTo = null;
    mExitingToView = null;
    mCalledExitCoordinator = null;
    mEnterTransitionCoordinator = null;
    mEnterActivityOptions = null;
    mExitTransitionCoordinators = null;
  }
  
  public void enterReady(Activity paramActivity)
  {
    if ((mEnterActivityOptions != null) && (!mIsEnterTriggered))
    {
      mIsEnterTriggered = true;
      mHasExited = false;
      ArrayList localArrayList = mEnterActivityOptions.getSharedElementNames();
      ResultReceiver localResultReceiver = mEnterActivityOptions.getResultReceiver();
      if (mEnterActivityOptions.isReturning())
      {
        restoreExitedViews();
        paramActivity.getWindow().getDecorView().setVisibility(0);
      }
      mEnterTransitionCoordinator = new EnterTransitionCoordinator(paramActivity, localResultReceiver, localArrayList, mEnterActivityOptions.isReturning(), mEnterActivityOptions.isCrossTask());
      if (mEnterActivityOptions.isCrossTask())
      {
        mExitingFrom = new ArrayList(mEnterActivityOptions.getSharedElementNames());
        mExitingTo = new ArrayList(mEnterActivityOptions.getSharedElementNames());
      }
      if (!mIsEnterPostponed) {
        startEnter();
      }
      return;
    }
  }
  
  public boolean isTransitionRunning()
  {
    if ((mEnterTransitionCoordinator != null) && (mEnterTransitionCoordinator.isTransitionRunning())) {
      return true;
    }
    if ((mCalledExitCoordinator != null) && (mCalledExitCoordinator.isTransitionRunning())) {
      return true;
    }
    return (mReturnExitCoordinator != null) && (mReturnExitCoordinator.isTransitionRunning());
  }
  
  public void onResume(Activity paramActivity, boolean paramBoolean)
  {
    if ((!paramBoolean) && (mEnterTransitionCoordinator != null))
    {
      mHandler.postDelayed(new Runnable()
      {
        public void run()
        {
          if ((mEnterTransitionCoordinator == null) || (mEnterTransitionCoordinator.isWaitingForRemoteExit()))
          {
            ActivityTransitionState.this.restoreExitedViews();
            ActivityTransitionState.this.restoreReenteringViews();
          }
        }
      }, 1000L);
    }
    else
    {
      restoreExitedViews();
      restoreReenteringViews();
    }
  }
  
  public void onStop()
  {
    restoreExitedViews();
    if (mEnterTransitionCoordinator != null)
    {
      mEnterTransitionCoordinator.stop();
      mEnterTransitionCoordinator = null;
    }
    if (mReturnExitCoordinator != null)
    {
      mReturnExitCoordinator.stop();
      mReturnExitCoordinator = null;
    }
  }
  
  public void postponeEnterTransition()
  {
    mIsEnterPostponed = true;
  }
  
  public void readState(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      if ((mEnterTransitionCoordinator == null) || (mEnterTransitionCoordinator.isReturning())) {
        mEnteringNames = paramBundle.getStringArrayList("android:enteringSharedElements");
      }
      if (mEnterTransitionCoordinator == null)
      {
        mExitingFrom = paramBundle.getStringArrayList("android:exitingMappedFrom");
        mExitingTo = paramBundle.getStringArrayList("android:exitingMappedTo");
      }
    }
  }
  
  public void saveState(Bundle paramBundle)
  {
    if (mEnteringNames != null) {
      paramBundle.putStringArrayList("android:enteringSharedElements", mEnteringNames);
    }
    if (mExitingFrom != null)
    {
      paramBundle.putStringArrayList("android:exitingMappedFrom", mExitingFrom);
      paramBundle.putStringArrayList("android:exitingMappedTo", mExitingTo);
    }
  }
  
  public void setEnterActivityOptions(Activity paramActivity, ActivityOptions paramActivityOptions)
  {
    Window localWindow = paramActivity.getWindow();
    if (localWindow == null) {
      return;
    }
    localWindow.getDecorView();
    if ((localWindow.hasFeature(13)) && (paramActivityOptions != null) && (mEnterActivityOptions == null) && (mEnterTransitionCoordinator == null) && (paramActivityOptions.getAnimationType() == 5))
    {
      mEnterActivityOptions = paramActivityOptions;
      mIsEnterTriggered = false;
      if (mEnterActivityOptions.isReturning())
      {
        restoreExitedViews();
        int i = mEnterActivityOptions.getResultCode();
        if (i != 0)
        {
          paramActivityOptions = mEnterActivityOptions.getResultData();
          if (paramActivityOptions != null) {
            paramActivityOptions.setExtrasClassLoader(paramActivity.getClassLoader());
          }
          paramActivity.onActivityReenter(i, paramActivityOptions);
        }
      }
    }
  }
  
  public boolean startExitBackTransition(Activity paramActivity)
  {
    if ((mEnteringNames != null) && (mCalledExitCoordinator == null))
    {
      if (!mHasExited)
      {
        mHasExited = true;
        Object localObject1 = null;
        Object localObject2 = null;
        int i = 0;
        if (mEnterTransitionCoordinator != null)
        {
          Transition localTransition = mEnterTransitionCoordinator.getEnterViewsTransition();
          ViewGroup localViewGroup = mEnterTransitionCoordinator.getDecor();
          boolean bool = mEnterTransitionCoordinator.cancelEnter();
          mEnterTransitionCoordinator = null;
          localObject1 = localTransition;
          localObject2 = localViewGroup;
          i = bool;
          if (localTransition != null)
          {
            localObject1 = localTransition;
            localObject2 = localViewGroup;
            i = bool;
            if (localViewGroup != null)
            {
              localTransition.pause(localViewGroup);
              i = bool;
              localObject2 = localViewGroup;
              localObject1 = localTransition;
            }
          }
        }
        mReturnExitCoordinator = new ExitTransitionCoordinator(paramActivity, paramActivity.getWindow(), mEnterTransitionListener, mEnteringNames, null, null, true);
        if ((localObject1 != null) && (localObject2 != null)) {
          localObject1.resume(localObject2);
        }
        if ((i != 0) && (localObject2 != null)) {
          OneShotPreDrawListener.add(localObject2, new _..Lambda.ActivityTransitionState.yioLR6wQWjZ9DcWK5bibElIbsXc(this, paramActivity));
        } else {
          mReturnExitCoordinator.startExit(mResultCode, mResultData);
        }
      }
      return true;
    }
    return false;
  }
  
  public void startExitOutTransition(Activity paramActivity, Bundle paramBundle)
  {
    mEnterTransitionCoordinator = null;
    if ((paramActivity.getWindow().hasFeature(13)) && (mExitTransitionCoordinators != null))
    {
      paramActivity = new ActivityOptions(paramBundle);
      if (paramActivity.getAnimationType() == 5)
      {
        int i = paramActivity.getExitCoordinatorKey();
        i = mExitTransitionCoordinators.indexOfKey(i);
        if (i >= 0)
        {
          mCalledExitCoordinator = ((ExitTransitionCoordinator)((WeakReference)mExitTransitionCoordinators.valueAt(i)).get());
          mExitTransitionCoordinators.removeAt(i);
          if (mCalledExitCoordinator != null)
          {
            mExitingFrom = mCalledExitCoordinator.getAcceptedNames();
            mExitingTo = mCalledExitCoordinator.getMappedNames();
            mExitingToView = mCalledExitCoordinator.copyMappedViews();
            mCalledExitCoordinator.startExit();
          }
        }
      }
      return;
    }
  }
  
  public void startPostponedEnterTransition()
  {
    if (mIsEnterPostponed)
    {
      mIsEnterPostponed = false;
      if (mEnterTransitionCoordinator != null) {
        startEnter();
      }
    }
  }
}
