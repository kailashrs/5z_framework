package android.app;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.util.LogWriter;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import com.android.internal.util.FastPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl
  extends FragmentManager
  implements LayoutInflater.Factory2
{
  static boolean DEBUG = false;
  static final String TAG = "FragmentManager";
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  static final String TARGET_STATE_TAG = "android:target_state";
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  static final String VIEW_STATE_TAG = "android:view_state";
  SparseArray<Fragment> mActive;
  final ArrayList<Fragment> mAdded = new ArrayList();
  boolean mAllowOldReentrantBehavior;
  ArrayList<Integer> mAvailBackStackIndices;
  ArrayList<BackStackRecord> mBackStack;
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  ArrayList<BackStackRecord> mBackStackIndices;
  FragmentContainer mContainer;
  ArrayList<Fragment> mCreatedMenus;
  int mCurState = 0;
  boolean mDestroyed;
  Runnable mExecCommit = new Runnable()
  {
    public void run()
    {
      execPendingActions();
    }
  };
  boolean mExecutingActions;
  boolean mHavePendingDeferredStart;
  FragmentHostCallback<?> mHost;
  final CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList();
  boolean mNeedMenuInvalidate;
  int mNextFragmentIndex = 0;
  String mNoTransactionsBecause;
  Fragment mParent;
  ArrayList<OpGenerator> mPendingActions;
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  Fragment mPrimaryNav;
  FragmentManagerNonConfig mSavedNonConfig;
  SparseArray<Parcelable> mStateArray = null;
  Bundle mStateBundle = null;
  boolean mStateSaved;
  ArrayList<Fragment> mTmpAddedFragments;
  ArrayList<Boolean> mTmpIsPop;
  ArrayList<BackStackRecord> mTmpRecords;
  
  FragmentManagerImpl() {}
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet)
  {
    if (mCurState < 1) {
      return;
    }
    int i = Math.min(mCurState, 4);
    int j = mAdded.size();
    for (int k = 0; k < j; k++)
    {
      Fragment localFragment = (Fragment)mAdded.get(k);
      if (mState < i)
      {
        moveToState(localFragment, i, localFragment.getNextAnim(), localFragment.getNextTransition(), false);
        if ((mView != null) && (!mHidden) && (mIsNewlyAdded)) {
          paramArraySet.add(localFragment);
        }
      }
    }
  }
  
  private void burpActive()
  {
    if (mActive != null) {
      for (int i = mActive.size() - 1; i >= 0; i--) {
        if (mActive.valueAt(i) == null) {
          mActive.delete(mActive.keyAt(i));
        }
      }
    }
  }
  
  private void checkStateLoss()
  {
    if (!mStateSaved)
    {
      if (mNoTransactionsBecause == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Can not perform this action inside of ");
      localStringBuilder.append(mNoTransactionsBecause);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec()
  {
    mExecutingActions = false;
    mTmpIsPop.clear();
    mTmpRecords.clear();
  }
  
  private void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    }
    Object localObject = new ArrayList(1);
    ArrayList localArrayList = new ArrayList(1);
    ((ArrayList)localObject).add(paramBackStackRecord);
    localArrayList.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2) {
      FragmentTransition.startTransitions(this, (ArrayList)localObject, localArrayList, 0, 1, true);
    }
    if (paramBoolean3) {
      moveToState(mCurState, true);
    }
    if (mActive != null)
    {
      int i = mActive.size();
      for (int j = 0; j < i; j++)
      {
        localObject = (Fragment)mActive.valueAt(j);
        if ((localObject != null) && (mView != null) && (mIsNewlyAdded) && (paramBackStackRecord.interactsWith(mContainerId))) {
          mIsNewlyAdded = false;
        }
      }
    }
  }
  
  private void dispatchMoveToState(int paramInt)
  {
    if (mAllowOldReentrantBehavior) {
      moveToState(paramInt, false);
    }
    try
    {
      mExecutingActions = true;
      moveToState(paramInt, false);
      mExecutingActions = false;
      execPendingActions();
      return;
    }
    finally
    {
      mExecutingActions = false;
    }
  }
  
  private void endAnimatingAwayFragments()
  {
    Object localObject = mActive;
    int i = 0;
    int j;
    if (localObject == null) {
      j = 0;
    } else {
      j = mActive.size();
    }
    while (i < j)
    {
      localObject = (Fragment)mActive.valueAt(i);
      if ((localObject != null) && (((Fragment)localObject).getAnimatingAway() != null)) {
        ((Fragment)localObject).getAnimatingAway().end();
      }
      i++;
    }
  }
  
  private void ensureExecReady(boolean paramBoolean)
  {
    if (!mExecutingActions)
    {
      if (Looper.myLooper() == mHost.getHandler().getLooper())
      {
        if (!paramBoolean) {
          checkStateLoss();
        }
        if (mTmpRecords == null)
        {
          mTmpRecords = new ArrayList();
          mTmpIsPop = new ArrayList();
        }
        mExecutingActions = true;
        try
        {
          executePostponedTransaction(null, null);
          return;
        }
        finally
        {
          mExecutingActions = false;
        }
      }
      throw new IllegalStateException("Must be called from main thread of fragment host");
    }
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2)
  {
    while (paramInt1 < paramInt2)
    {
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(paramInt1);
      boolean bool1 = ((Boolean)paramArrayList1.get(paramInt1)).booleanValue();
      boolean bool2 = true;
      if (bool1)
      {
        localBackStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1) {
          bool2 = false;
        }
        localBackStackRecord.executePopOps(bool2);
      }
      else
      {
        localBackStackRecord.bumpBackStackNesting(1);
        localBackStackRecord.executeOps();
      }
      paramInt1++;
    }
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2)
  {
    boolean bool = getmReorderingAllowed;
    if (mTmpAddedFragments == null) {
      mTmpAddedFragments = new ArrayList();
    } else {
      mTmpAddedFragments.clear();
    }
    mTmpAddedFragments.addAll(mAdded);
    Object localObject = getPrimaryNavigationFragment();
    int i = 0;
    int j = paramInt1;
    for (;;)
    {
      int k = 1;
      if (j >= paramInt2) {
        break;
      }
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(j);
      if (!((Boolean)paramArrayList1.get(j)).booleanValue()) {
        localObject = localBackStackRecord.expandOps(mTmpAddedFragments, (Fragment)localObject);
      } else {
        localBackStackRecord.trackAddedFragmentsInPop(mTmpAddedFragments);
      }
      m = k;
      if (i == 0) {
        if (mAddToBackStack) {
          m = k;
        } else {
          m = 0;
        }
      }
      j++;
      i = m;
    }
    mTmpAddedFragments.clear();
    if (!bool) {
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false);
    }
    executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    int m = paramInt2;
    if (bool)
    {
      localObject = new ArraySet();
      addAddedFragments((ArraySet)localObject);
      m = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, (ArraySet)localObject);
      makeRemovedFragmentsInvisible((ArraySet)localObject);
    }
    if ((m != paramInt1) && (bool))
    {
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, m, true);
      moveToState(mCurState, true);
    }
    while (paramInt1 < paramInt2)
    {
      localObject = (BackStackRecord)paramArrayList.get(paramInt1);
      if ((((Boolean)paramArrayList1.get(paramInt1)).booleanValue()) && (mIndex >= 0))
      {
        freeBackStackIndex(mIndex);
        mIndex = -1;
      }
      ((BackStackRecord)localObject).runOnCommitRunnables();
      paramInt1++;
    }
    if (i != 0) {
      reportBackStackChanged();
    }
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    if (mPostponedTransactions == null) {
      i = 0;
    } else {
      i = mPostponedTransactions.size();
    }
    int j = 0;
    int k = i;
    int i = j;
    while (i < k)
    {
      StartEnterTransitionListener localStartEnterTransitionListener = (StartEnterTransitionListener)mPostponedTransactions.get(i);
      int m;
      if ((paramArrayList != null) && (!mIsBack))
      {
        j = paramArrayList.indexOf(mRecord);
        if ((j != -1) && (((Boolean)paramArrayList1.get(j)).booleanValue()))
        {
          localStartEnterTransitionListener.cancelTransaction();
          m = i;
          j = k;
          break label224;
        }
      }
      if (!localStartEnterTransitionListener.isReady())
      {
        m = i;
        j = k;
        if (paramArrayList != null)
        {
          m = i;
          j = k;
          if (!mRecord.interactsWith(paramArrayList, 0, paramArrayList.size())) {}
        }
      }
      else
      {
        mPostponedTransactions.remove(i);
        m = i - 1;
        j = k - 1;
        if ((paramArrayList != null) && (!mIsBack))
        {
          i = paramArrayList.indexOf(mRecord);
          if ((i != -1) && (((Boolean)paramArrayList1.get(i)).booleanValue()))
          {
            localStartEnterTransitionListener.cancelTransaction();
            break label224;
          }
        }
        localStartEnterTransitionListener.completeTransaction();
      }
      label224:
      i = m + 1;
      k = j;
    }
  }
  
  private Fragment findFragmentUnder(Fragment paramFragment)
  {
    ViewGroup localViewGroup = mContainer;
    View localView = mView;
    if ((localViewGroup != null) && (localView != null))
    {
      for (int i = mAdded.indexOf(paramFragment) - 1; i >= 0; i--)
      {
        paramFragment = (Fragment)mAdded.get(i);
        if ((mContainer == localViewGroup) && (mView != null)) {
          return paramFragment;
        }
      }
      return null;
    }
    return null;
  }
  
  private void forcePostponedTransactions()
  {
    if (mPostponedTransactions != null) {
      while (!mPostponedTransactions.isEmpty()) {
        ((StartEnterTransitionListener)mPostponedTransactions.remove(0)).completeTransaction();
      }
    }
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    boolean bool = false;
    try
    {
      ArrayList localArrayList = mPendingActions;
      int i = 0;
      if ((localArrayList != null) && (mPendingActions.size() != 0))
      {
        int j = mPendingActions.size();
        while (i < j)
        {
          bool |= ((OpGenerator)mPendingActions.get(i)).generateOps(paramArrayList, paramArrayList1);
          i++;
        }
        mPendingActions.clear();
        mHost.getHandler().removeCallbacks(mExecCommit);
        return bool;
      }
      return false;
    }
    finally {}
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet)
  {
    int i = paramArraySet.size();
    for (int j = 0; j < i; j++)
    {
      Fragment localFragment = (Fragment)paramArraySet.valueAt(j);
      if (!mAdded) {
        localFragment.getView().setTransitionAlpha(0.0F);
      }
    }
  }
  
  static boolean modifiesAlpha(Animator paramAnimator)
  {
    if (paramAnimator == null) {
      return false;
    }
    int i;
    if ((paramAnimator instanceof ValueAnimator))
    {
      paramAnimator = ((ValueAnimator)paramAnimator).getValues();
      for (i = 0; i < paramAnimator.length; i++) {
        if ("alpha".equals(paramAnimator[i].getPropertyName())) {
          return true;
        }
      }
    }
    else if ((paramAnimator instanceof AnimatorSet))
    {
      paramAnimator = ((AnimatorSet)paramAnimator).getChildAnimations();
      for (i = 0; i < paramAnimator.size(); i++) {
        if (modifiesAlpha((Animator)paramAnimator.get(i))) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2)
  {
    execPendingActions();
    ensureExecReady(true);
    if ((mPrimaryNav != null) && (paramInt1 < 0) && (paramString == null))
    {
      FragmentManagerImpl localFragmentManagerImpl = mPrimaryNav.mChildFragmentManager;
      if ((localFragmentManagerImpl != null) && (localFragmentManagerImpl.popBackStackImmediate())) {
        return true;
      }
    }
    boolean bool = popBackStackState(mTmpRecords, mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      mExecutingActions = true;
    }
    try
    {
      removeRedundantOperationsAndExecute(mTmpRecords, mTmpIsPop);
      cleanupExec();
    }
    finally
    {
      cleanupExec();
    }
    burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet)
  {
    int i = paramInt2;
    int j = paramInt2 - 1;
    while (j >= paramInt1)
    {
      BackStackRecord localBackStackRecord = (BackStackRecord)paramArrayList.get(j);
      boolean bool = ((Boolean)paramArrayList1.get(j)).booleanValue();
      int k;
      if ((localBackStackRecord.isPostponed()) && (!localBackStackRecord.interactsWith(paramArrayList, j + 1, paramInt2))) {
        k = 1;
      } else {
        k = 0;
      }
      int m = i;
      if (k != 0)
      {
        if (mPostponedTransactions == null) {
          mPostponedTransactions = new ArrayList();
        }
        StartEnterTransitionListener localStartEnterTransitionListener = new StartEnterTransitionListener(localBackStackRecord, bool);
        mPostponedTransactions.add(localStartEnterTransitionListener);
        localBackStackRecord.setOnStartPostponedListener(localStartEnterTransitionListener);
        if (bool) {
          localBackStackRecord.executeOps();
        } else {
          localBackStackRecord.executePopOps(false);
        }
        m = i - 1;
        if (j != m)
        {
          paramArrayList.remove(j);
          paramArrayList.add(m, localBackStackRecord);
        }
        addAddedFragments(paramArraySet);
      }
      j--;
      i = m;
    }
    return i;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    if ((paramArrayList != null) && (!paramArrayList.isEmpty()))
    {
      if ((paramArrayList1 != null) && (paramArrayList.size() == paramArrayList1.size()))
      {
        executePostponedTransaction(paramArrayList, paramArrayList1);
        int i = paramArrayList.size();
        int j = 0;
        int k = 0;
        while (k < i)
        {
          int m = j;
          int n = k;
          if (!getmReorderingAllowed)
          {
            if (j != k) {
              executeOpsTogether(paramArrayList, paramArrayList1, j, k);
            }
            n = k + 1;
            m = n;
            if (((Boolean)paramArrayList1.get(k)).booleanValue()) {
              for (;;)
              {
                m = n;
                if (n >= i) {
                  break;
                }
                m = n;
                if (!((Boolean)paramArrayList1.get(n)).booleanValue()) {
                  break;
                }
                m = n;
                if (getmReorderingAllowed) {
                  break;
                }
                n++;
              }
            }
            executeOpsTogether(paramArrayList, paramArrayList1, k, m);
            k = m;
            n = m - 1;
            m = k;
          }
          k = n + 1;
          j = m;
        }
        if (j != i) {
          executeOpsTogether(paramArrayList, paramArrayList1, j, i);
        }
        return;
      }
      throw new IllegalStateException("Internal error with the back stack records");
    }
  }
  
  public static int reverseTransit(int paramInt)
  {
    int i = 0;
    if (paramInt != 4097)
    {
      if (paramInt != 4099)
      {
        if (paramInt != 8194) {
          paramInt = i;
        } else {
          paramInt = 4097;
        }
      }
      else {
        paramInt = 4099;
      }
    }
    else {
      paramInt = 8194;
    }
    return paramInt;
  }
  
  private void scheduleCommit()
  {
    try
    {
      ArrayList localArrayList = mPostponedTransactions;
      int i = 0;
      int j;
      if ((localArrayList != null) && (!mPostponedTransactions.isEmpty())) {
        j = 1;
      } else {
        j = 0;
      }
      int k = i;
      if (mPendingActions != null)
      {
        k = i;
        if (mPendingActions.size() == 1) {
          k = 1;
        }
      }
      if ((j != 0) || (k != 0))
      {
        mHost.getHandler().removeCallbacks(mExecCommit);
        mHost.getHandler().post(mExecCommit);
      }
      return;
    }
    finally {}
  }
  
  private void setHWLayerAnimListenerIfAlpha(View paramView, Animator paramAnimator)
  {
    if ((paramView != null) && (paramAnimator != null))
    {
      if (shouldRunOnHWLayer(paramView, paramAnimator)) {
        paramAnimator.addListener(new AnimateOnHWLayerIfNeededListener(paramView));
      }
      return;
    }
  }
  
  private static void setRetaining(FragmentManagerNonConfig paramFragmentManagerNonConfig)
  {
    if (paramFragmentManagerNonConfig == null) {
      return;
    }
    Object localObject = paramFragmentManagerNonConfig.getFragments();
    if (localObject != null)
    {
      localObject = ((List)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        nextmRetaining = true;
      }
    }
    paramFragmentManagerNonConfig = paramFragmentManagerNonConfig.getChildNonConfigs();
    if (paramFragmentManagerNonConfig != null)
    {
      paramFragmentManagerNonConfig = paramFragmentManagerNonConfig.iterator();
      while (paramFragmentManagerNonConfig.hasNext()) {
        setRetaining((FragmentManagerNonConfig)paramFragmentManagerNonConfig.next());
      }
    }
  }
  
  static boolean shouldRunOnHWLayer(View paramView, Animator paramAnimator)
  {
    boolean bool1 = false;
    if ((paramView != null) && (paramAnimator != null))
    {
      boolean bool2 = bool1;
      if (paramView.getLayerType() == 0)
      {
        bool2 = bool1;
        if (paramView.hasOverlappingRendering())
        {
          bool2 = bool1;
          if (modifiesAlpha(paramAnimator)) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  private void throwException(RuntimeException paramRuntimeException)
  {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(new LogWriter(6, "FragmentManager"), false, 1024);
    if (mHost != null)
    {
      Log.e("FragmentManager", "Activity state:");
      try
      {
        mHost.onDump("  ", null, localFastPrintWriter, new String[0]);
      }
      catch (Exception localException1)
      {
        localFastPrintWriter.flush();
        Log.e("FragmentManager", "Failed dumping state", localException1);
      }
    }
    else
    {
      Log.e("FragmentManager", "Fragment manager state:");
      try
      {
        dump("  ", null, localFastPrintWriter, new String[0]);
      }
      catch (Exception localException2)
      {
        localFastPrintWriter.flush();
        Log.e("FragmentManager", "Failed dumping state", localException2);
      }
    }
    localFastPrintWriter.flush();
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean)
  {
    int i = -1;
    if (paramInt != 4097)
    {
      if (paramInt != 4099)
      {
        if (paramInt != 8194) {
          paramInt = i;
        } else if (paramBoolean) {
          paramInt = 2;
        } else {
          paramInt = 3;
        }
      }
      else if (paramBoolean) {
        paramInt = 4;
      } else {
        paramInt = 5;
      }
    }
    else if (paramBoolean) {
      paramInt = 0;
    } else {
      paramInt = 1;
    }
    return paramInt;
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord)
  {
    if (mBackStack == null) {
      mBackStack = new ArrayList();
    }
    mBackStack.add(paramBackStackRecord);
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("add: ");
      ((StringBuilder)???).append(paramFragment);
      Log.v("FragmentManager", ((StringBuilder)???).toString());
    }
    makeActive(paramFragment);
    if (!mDetached)
    {
      if (!mAdded.contains(paramFragment)) {
        synchronized (mAdded)
        {
          mAdded.add(paramFragment);
          mAdded = true;
          mRemoving = false;
          if (mView == null) {
            mHiddenChanged = false;
          }
          if ((mHasMenu) && (mMenuVisible)) {
            mNeedMenuInvalidate = true;
          }
          if (!paramBoolean) {
            return;
          }
          moveToState(paramFragment);
        }
      }
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Fragment already added: ");
      ((StringBuilder)???).append(paramFragment);
      throw new IllegalStateException(((StringBuilder)???).toString());
    }
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (mBackStackChangeListeners == null) {
      mBackStackChangeListeners = new ArrayList();
    }
    mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord)
  {
    try
    {
      Object localObject;
      if ((mAvailBackStackIndices != null) && (mAvailBackStackIndices.size() > 0))
      {
        i = ((Integer)mAvailBackStackIndices.remove(mAvailBackStackIndices.size() - 1)).intValue();
        if (DEBUG)
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Adding back stack index ");
          ((StringBuilder)localObject).append(i);
          ((StringBuilder)localObject).append(" with ");
          ((StringBuilder)localObject).append(paramBackStackRecord);
          Log.v("FragmentManager", ((StringBuilder)localObject).toString());
        }
        mBackStackIndices.set(i, paramBackStackRecord);
        return i;
      }
      if (mBackStackIndices == null)
      {
        localObject = new java/util/ArrayList;
        ((ArrayList)localObject).<init>();
        mBackStackIndices = ((ArrayList)localObject);
      }
      int i = mBackStackIndices.size();
      if (DEBUG)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Setting back stack index ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" to ");
        ((StringBuilder)localObject).append(paramBackStackRecord);
        Log.v("FragmentManager", ((StringBuilder)localObject).toString());
      }
      mBackStackIndices.add(paramBackStackRecord);
      return i;
    }
    finally {}
  }
  
  public void attachController(FragmentHostCallback<?> paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment)
  {
    if (mHost == null)
    {
      mHost = paramFragmentHostCallback;
      mContainer = paramFragmentContainer;
      mParent = paramFragment;
      boolean bool;
      if (getTargetSdk() <= 25) {
        bool = true;
      } else {
        bool = false;
      }
      mAllowOldReentrantBehavior = bool;
      return;
    }
    throw new IllegalStateException("Already attached");
  }
  
  public void attachFragment(Fragment paramFragment)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("attach: ");
      ((StringBuilder)???).append(paramFragment);
      Log.v("FragmentManager", ((StringBuilder)???).toString());
    }
    if (mDetached)
    {
      mDetached = false;
      if (!mAdded)
      {
        if (!mAdded.contains(paramFragment))
        {
          if (DEBUG)
          {
            ??? = new StringBuilder();
            ((StringBuilder)???).append("add from attach: ");
            ((StringBuilder)???).append(paramFragment);
            Log.v("FragmentManager", ((StringBuilder)???).toString());
          }
          synchronized (mAdded)
          {
            mAdded.add(paramFragment);
            mAdded = true;
            if ((!mHasMenu) || (!mMenuVisible)) {
              return;
            }
            mNeedMenuInvalidate = true;
          }
        }
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Fragment already added: ");
        ((StringBuilder)???).append(paramFragment);
        throw new IllegalStateException(((StringBuilder)???).toString());
      }
    }
  }
  
  public FragmentTransaction beginTransaction()
  {
    return new BackStackRecord(this);
  }
  
  void completeShowHideFragment(Fragment paramFragment)
  {
    if (mView != null)
    {
      Animator localAnimator = loadAnimator(paramFragment, paramFragment.getNextTransition(), mHidden ^ true, paramFragment.getNextTransitionStyle());
      if (localAnimator != null)
      {
        localAnimator.setTarget(mView);
        if (mHidden)
        {
          if (paramFragment.isHideReplaced())
          {
            paramFragment.setHideReplaced(false);
          }
          else
          {
            final ViewGroup localViewGroup = mContainer;
            final View localView = mView;
            if (localViewGroup != null) {
              localViewGroup.startViewTransition(localView);
            }
            localAnimator.addListener(new AnimatorListenerAdapter()
            {
              public void onAnimationEnd(Animator paramAnonymousAnimator)
              {
                if (localViewGroup != null) {
                  localViewGroup.endViewTransition(localView);
                }
                paramAnonymousAnimator.removeListener(this);
                localView.setVisibility(8);
              }
            });
          }
        }
        else {
          mView.setVisibility(0);
        }
        setHWLayerAnimListenerIfAlpha(mView, localAnimator);
        localAnimator.start();
      }
      else
      {
        int i;
        if ((mHidden) && (!paramFragment.isHideReplaced())) {
          i = 8;
        } else {
          i = 0;
        }
        mView.setVisibility(i);
        if (paramFragment.isHideReplaced()) {
          paramFragment.setHideReplaced(false);
        }
      }
    }
    if ((mAdded) && (mHasMenu) && (mMenuVisible)) {
      mNeedMenuInvalidate = true;
    }
    mHiddenChanged = false;
    paramFragment.onHiddenChanged(mHidden);
  }
  
  public void detachFragment(Fragment paramFragment)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("detach: ");
      ((StringBuilder)???).append(paramFragment);
      Log.v("FragmentManager", ((StringBuilder)???).toString());
    }
    if (!mDetached)
    {
      mDetached = true;
      if (mAdded)
      {
        if (DEBUG)
        {
          ??? = new StringBuilder();
          ((StringBuilder)???).append("remove from detach: ");
          ((StringBuilder)???).append(paramFragment);
          Log.v("FragmentManager", ((StringBuilder)???).toString());
        }
        synchronized (mAdded)
        {
          mAdded.remove(paramFragment);
          if ((mHasMenu) && (mMenuVisible)) {
            mNeedMenuInvalidate = true;
          }
          mAdded = false;
        }
      }
    }
  }
  
  public void dispatchActivityCreated()
  {
    mStateSaved = false;
    dispatchMoveToState(2);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performConfigurationChanged(paramConfiguration);
      }
    }
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem)
  {
    if (mCurState < 1) {
      return false;
    }
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if ((localFragment != null) && (localFragment.performContextItemSelected(paramMenuItem))) {
        return true;
      }
    }
    return false;
  }
  
  public void dispatchCreate()
  {
    mStateSaved = false;
    dispatchMoveToState(1);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    int i = mCurState;
    int j = 0;
    if (i < 1) {
      return false;
    }
    Object localObject1 = null;
    boolean bool1 = false;
    i = 0;
    while (i < mAdded.size())
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      boolean bool2 = bool1;
      Object localObject2 = localObject1;
      if (localFragment != null)
      {
        bool2 = bool1;
        localObject2 = localObject1;
        if (localFragment.performCreateOptionsMenu(paramMenu, paramMenuInflater))
        {
          bool2 = true;
          localObject2 = localObject1;
          if (localObject1 == null) {
            localObject2 = new ArrayList();
          }
          ((ArrayList)localObject2).add(localFragment);
        }
      }
      i++;
      bool1 = bool2;
      localObject1 = localObject2;
    }
    if (mCreatedMenus != null) {
      for (i = j; i < mCreatedMenus.size(); i++)
      {
        paramMenu = (Fragment)mCreatedMenus.get(i);
        if ((localObject1 == null) || (!localObject1.contains(paramMenu))) {
          paramMenu.onDestroyOptionsMenu();
        }
      }
    }
    mCreatedMenus = localObject1;
    return bool1;
  }
  
  public void dispatchDestroy()
  {
    mDestroyed = true;
    execPendingActions();
    dispatchMoveToState(0);
    mHost = null;
    mContainer = null;
    mParent = null;
  }
  
  public void dispatchDestroyView()
  {
    dispatchMoveToState(1);
  }
  
  public void dispatchLowMemory()
  {
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performLowMemory();
      }
    }
  }
  
  @Deprecated
  public void dispatchMultiWindowModeChanged(boolean paramBoolean)
  {
    for (int i = mAdded.size() - 1; i >= 0; i--)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performMultiWindowModeChanged(paramBoolean);
      }
    }
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    for (int i = mAdded.size() - 1; i >= 0; i--)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performMultiWindowModeChanged(paramBoolean, paramConfiguration);
      }
    }
  }
  
  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentActivityCreated(this, paramFragment, paramBundle);
      }
    }
  }
  
  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentAttached(paramFragment, paramContext, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentAttached(this, paramFragment, paramContext);
      }
    }
  }
  
  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentCreated(paramFragment, paramBundle, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentCreated(this, paramFragment, paramBundle);
      }
    }
  }
  
  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentDestroyed(paramFragment, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentDestroyed(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentDetached(paramFragment, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentDetached(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentPaused(paramFragment, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentPaused(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentPreAttached(paramFragment, paramContext, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentPreAttached(this, paramFragment, paramContext);
      }
    }
  }
  
  void dispatchOnFragmentPreCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentPreCreated(paramFragment, paramBundle, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentPreCreated(this, paramFragment, paramBundle);
      }
    }
  }
  
  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentResumed(paramFragment, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentResumed(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentSaveInstanceState(this, paramFragment, paramBundle);
      }
    }
  }
  
  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean)
  {
    Object localObject;
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentStarted(paramFragment, true);
      }
    }
    Iterator localIterator = mLifecycleCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Pair)localIterator.next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentStarted(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentStopped(paramFragment, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentStopped(this, paramFragment);
      }
    }
  }
  
  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentViewCreated(this, paramFragment, paramView, paramBundle);
      }
    }
  }
  
  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean)
  {
    if (mParent != null)
    {
      localObject = mParent.getFragmentManager();
      if ((localObject instanceof FragmentManagerImpl)) {
        ((FragmentManagerImpl)localObject).dispatchOnFragmentViewDestroyed(paramFragment, true);
      }
    }
    Object localObject = mLifecycleCallbacks.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      if ((!paramBoolean) || (((Boolean)second).booleanValue())) {
        ((FragmentManager.FragmentLifecycleCallbacks)first).onFragmentViewDestroyed(this, paramFragment);
      }
    }
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (mCurState < 1) {
      return false;
    }
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if ((localFragment != null) && (localFragment.performOptionsItemSelected(paramMenuItem))) {
        return true;
      }
    }
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu)
  {
    if (mCurState < 1) {
      return;
    }
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performOptionsMenuClosed(paramMenu);
      }
    }
  }
  
  public void dispatchPause()
  {
    dispatchMoveToState(4);
  }
  
  @Deprecated
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean)
  {
    for (int i = mAdded.size() - 1; i >= 0; i--)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performPictureInPictureModeChanged(paramBoolean);
      }
    }
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    for (int i = mAdded.size() - 1; i >= 0; i--)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performPictureInPictureModeChanged(paramBoolean, paramConfiguration);
      }
    }
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu)
  {
    int i = mCurState;
    int j = 0;
    if (i < 1) {
      return false;
    }
    boolean bool2;
    for (boolean bool1 = false; j < mAdded.size(); bool1 = bool2)
    {
      Fragment localFragment = (Fragment)mAdded.get(j);
      bool2 = bool1;
      if (localFragment != null)
      {
        bool2 = bool1;
        if (localFragment.performPrepareOptionsMenu(paramMenu)) {
          bool2 = true;
        }
      }
      j++;
    }
    return bool1;
  }
  
  public void dispatchResume()
  {
    mStateSaved = false;
    dispatchMoveToState(5);
  }
  
  public void dispatchStart()
  {
    mStateSaved = false;
    dispatchMoveToState(4);
  }
  
  public void dispatchStop()
  {
    dispatchMoveToState(3);
  }
  
  public void dispatchTrimMemory(int paramInt)
  {
    for (int i = 0; i < mAdded.size(); i++)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.performTrimMemory(paramInt);
      }
    }
  }
  
  void doPendingDeferredStart()
  {
    if (mHavePendingDeferredStart)
    {
      boolean bool1 = false;
      int i = 0;
      while (i < mActive.size())
      {
        Fragment localFragment = (Fragment)mActive.valueAt(i);
        boolean bool2 = bool1;
        if (localFragment != null)
        {
          bool2 = bool1;
          if (mLoaderManager != null) {
            bool2 = bool1 | mLoaderManager.hasRunningLoaders();
          }
        }
        i++;
        bool1 = bool2;
      }
      if (!bool1)
      {
        mHavePendingDeferredStart = false;
        startPendingDeferredFragments();
      }
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("    ");
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = mActive;
    int i = 0;
    int k;
    if (localObject2 != null)
    {
      j = mActive.size();
      if (j > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("Active Fragments in ");
        paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
        paramPrintWriter.println(":");
        for (k = 0; k < j; k++)
        {
          localObject2 = (Fragment)mActive.valueAt(k);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(k);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(localObject2);
          if (localObject2 != null) {
            ((Fragment)localObject2).dump((String)localObject1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
          }
        }
      }
    }
    int j = mAdded.size();
    if (j > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Added Fragments:");
      for (k = 0; k < j; k++)
      {
        localObject2 = (Fragment)mAdded.get(k);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(k);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(((Fragment)localObject2).toString());
      }
    }
    if (mCreatedMenus != null)
    {
      j = mCreatedMenus.size();
      if (j > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Fragments Created Menus:");
        for (k = 0; k < j; k++)
        {
          localObject2 = (Fragment)mCreatedMenus.get(k);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(k);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(((Fragment)localObject2).toString());
        }
      }
    }
    if (mBackStack != null)
    {
      j = mBackStack.size();
      if (j > 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("Back Stack:");
        for (k = 0; k < j; k++)
        {
          localObject2 = (BackStackRecord)mBackStack.get(k);
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("  #");
          paramPrintWriter.print(k);
          paramPrintWriter.print(": ");
          paramPrintWriter.println(((BackStackRecord)localObject2).toString());
          ((BackStackRecord)localObject2).dump((String)localObject1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        }
      }
    }
    try
    {
      if (mBackStackIndices != null)
      {
        j = mBackStackIndices.size();
        if (j > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Back Stack Indices:");
          for (k = 0; k < j; k++)
          {
            paramFileDescriptor = (BackStackRecord)mBackStackIndices.get(k);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(k);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(paramFileDescriptor);
          }
        }
      }
      if ((mAvailBackStackIndices != null) && (mAvailBackStackIndices.size() > 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mAvailBackStackIndices: ");
        paramPrintWriter.println(Arrays.toString(mAvailBackStackIndices.toArray()));
      }
      if (mPendingActions != null)
      {
        j = mPendingActions.size();
        if (j > 0)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.println("Pending Actions:");
          for (k = i; k < j; k++)
          {
            paramFileDescriptor = (OpGenerator)mPendingActions.get(k);
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  #");
            paramPrintWriter.print(k);
            paramPrintWriter.print(": ");
            paramPrintWriter.println(paramFileDescriptor);
          }
        }
      }
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("FragmentManager misc state:");
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mHost=");
      paramPrintWriter.println(mHost);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mContainer=");
      paramPrintWriter.println(mContainer);
      if (mParent != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  mParent=");
        paramPrintWriter.println(mParent);
      }
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  mCurState=");
      paramPrintWriter.print(mCurState);
      paramPrintWriter.print(" mStateSaved=");
      paramPrintWriter.print(mStateSaved);
      paramPrintWriter.print(" mDestroyed=");
      paramPrintWriter.println(mDestroyed);
      if (mNeedMenuInvalidate)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  mNeedMenuInvalidate=");
        paramPrintWriter.println(mNeedMenuInvalidate);
      }
      if (mNoTransactionsBecause != null)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  mNoTransactionsBecause=");
        paramPrintWriter.println(mNoTransactionsBecause);
      }
      return;
    }
    finally {}
  }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean)
  {
    if (!paramBoolean) {
      checkStateLoss();
    }
    try
    {
      if ((!mDestroyed) && (mHost != null))
      {
        if (mPendingActions == null)
        {
          ArrayList localArrayList = new java/util/ArrayList;
          localArrayList.<init>();
          mPendingActions = localArrayList;
        }
        mPendingActions.add(paramOpGenerator);
        scheduleCommit();
        return;
      }
      if (paramBoolean) {
        return;
      }
      paramOpGenerator = new java/lang/IllegalStateException;
      paramOpGenerator.<init>("Activity has been destroyed");
      throw paramOpGenerator;
    }
    finally {}
  }
  
  void ensureInflatedFragmentView(Fragment paramFragment)
  {
    if ((mFromLayout) && (!mPerformedCreateView))
    {
      mView = paramFragment.performCreateView(paramFragment.performGetLayoutInflater(mSavedFragmentState), null, mSavedFragmentState);
      if (mView != null)
      {
        mView.setSaveFromParentEnabled(false);
        if (mHidden) {
          mView.setVisibility(8);
        }
        paramFragment.onViewCreated(mView, mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, mView, mSavedFragmentState, false);
      }
    }
  }
  
  public boolean execPendingActions()
  {
    ensureExecReady(true);
    boolean bool = false;
    for (;;)
    {
      if (generateOpsForPendingActions(mTmpRecords, mTmpIsPop)) {
        mExecutingActions = true;
      }
      try
      {
        removeRedundantOperationsAndExecute(mTmpRecords, mTmpIsPop);
        cleanupExec();
        bool = true;
      }
      finally
      {
        cleanupExec();
      }
    }
    burpActive();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean)
  {
    if ((paramBoolean) && ((mHost == null) || (mDestroyed))) {
      return;
    }
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(mTmpRecords, mTmpIsPop)) {
      mExecutingActions = true;
    }
    try
    {
      removeRedundantOperationsAndExecute(mTmpRecords, mTmpIsPop);
      cleanupExec();
    }
    finally
    {
      cleanupExec();
    }
    burpActive();
  }
  
  public boolean executePendingTransactions()
  {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  public Fragment findFragmentById(int paramInt)
  {
    Fragment localFragment;
    for (int i = mAdded.size() - 1; i >= 0; i--)
    {
      localFragment = (Fragment)mAdded.get(i);
      if ((localFragment != null) && (mFragmentId == paramInt)) {
        return localFragment;
      }
    }
    if (mActive != null) {
      for (i = mActive.size() - 1; i >= 0; i--)
      {
        localFragment = (Fragment)mActive.valueAt(i);
        if ((localFragment != null) && (mFragmentId == paramInt)) {
          return localFragment;
        }
      }
    }
    return null;
  }
  
  public Fragment findFragmentByTag(String paramString)
  {
    int i;
    Fragment localFragment;
    if (paramString != null) {
      for (i = mAdded.size() - 1; i >= 0; i--)
      {
        localFragment = (Fragment)mAdded.get(i);
        if ((localFragment != null) && (paramString.equals(mTag))) {
          return localFragment;
        }
      }
    }
    if ((mActive != null) && (paramString != null)) {
      for (i = mActive.size() - 1; i >= 0; i--)
      {
        localFragment = (Fragment)mActive.valueAt(i);
        if ((localFragment != null) && (paramString.equals(mTag))) {
          return localFragment;
        }
      }
    }
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString)
  {
    if ((mActive != null) && (paramString != null)) {
      for (int i = mActive.size() - 1; i >= 0; i--)
      {
        Fragment localFragment = (Fragment)mActive.valueAt(i);
        if (localFragment != null)
        {
          localFragment = localFragment.findFragmentByWho(paramString);
          if (localFragment != null) {
            return localFragment;
          }
        }
      }
    }
    return null;
  }
  
  public void freeBackStackIndex(int paramInt)
  {
    try
    {
      mBackStackIndices.set(paramInt, null);
      Object localObject1;
      if (mAvailBackStackIndices == null)
      {
        localObject1 = new java/util/ArrayList;
        ((ArrayList)localObject1).<init>();
        mAvailBackStackIndices = ((ArrayList)localObject1);
      }
      if (DEBUG)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Freeing back stack index ");
        ((StringBuilder)localObject1).append(paramInt);
        Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
      }
      mAvailBackStackIndices.add(Integer.valueOf(paramInt));
      return;
    }
    finally {}
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt)
  {
    return (FragmentManager.BackStackEntry)mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount()
  {
    int i;
    if (mBackStack != null) {
      i = mBackStack.size();
    } else {
      i = 0;
    }
    return i;
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString)
  {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1) {
      return null;
    }
    paramBundle = (Fragment)mActive.get(i);
    if (paramBundle == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Fragment no longer exists for key ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(": index ");
      localStringBuilder.append(i);
      throwException(new IllegalStateException(localStringBuilder.toString()));
    }
    return paramBundle;
  }
  
  public List<Fragment> getFragments()
  {
    if (mAdded.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    synchronized (mAdded)
    {
      List localList = (List)mAdded.clone();
      return localList;
    }
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory()
  {
    return this;
  }
  
  public Fragment getPrimaryNavigationFragment()
  {
    return mPrimaryNav;
  }
  
  int getTargetSdk()
  {
    if (mHost != null)
    {
      Object localObject = mHost.getContext();
      if (localObject != null)
      {
        localObject = ((Context)localObject).getApplicationInfo();
        if (localObject != null) {
          return targetSdkVersion;
        }
      }
    }
    return 0;
  }
  
  public void hideFragment(Fragment paramFragment)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("hide: ");
      localStringBuilder.append(paramFragment);
      Log.v("FragmentManager", localStringBuilder.toString());
    }
    if (!mHidden)
    {
      mHidden = true;
      mHiddenChanged = (true ^ mHiddenChanged);
    }
  }
  
  public void invalidateOptionsMenu()
  {
    if ((mHost != null) && (mCurState == 5)) {
      mHost.onInvalidateOptionsMenu();
    } else {
      mNeedMenuInvalidate = true;
    }
  }
  
  public boolean isDestroyed()
  {
    return mDestroyed;
  }
  
  boolean isStateAtLeast(int paramInt)
  {
    boolean bool;
    if (mCurState >= paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStateSaved()
  {
    return mStateSaved;
  }
  
  Animator loadAnimator(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    Animator localAnimator = paramFragment.onCreateAnimator(paramInt1, paramBoolean, paramFragment.getNextAnim());
    if (localAnimator != null) {
      return localAnimator;
    }
    if (paramFragment.getNextAnim() != 0)
    {
      paramFragment = AnimatorInflater.loadAnimator(mHost.getContext(), paramFragment.getNextAnim());
      if (paramFragment != null) {
        return paramFragment;
      }
    }
    if (paramInt1 == 0) {
      return null;
    }
    int i = transitToStyleIndex(paramInt1, paramBoolean);
    if (i < 0) {
      return null;
    }
    paramInt1 = paramInt2;
    if (paramInt2 == 0)
    {
      paramInt1 = paramInt2;
      if (mHost.onHasWindowAnimations()) {
        paramInt1 = mHost.onGetWindowAnimations();
      }
    }
    if (paramInt1 == 0) {
      return null;
    }
    paramFragment = mHost.getContext().obtainStyledAttributes(paramInt1, R.styleable.FragmentAnimation);
    paramInt1 = paramFragment.getResourceId(i, 0);
    paramFragment.recycle();
    if (paramInt1 == 0) {
      return null;
    }
    return AnimatorInflater.loadAnimator(mHost.getContext(), paramInt1);
  }
  
  void makeActive(Fragment paramFragment)
  {
    if (mIndex >= 0) {
      return;
    }
    int i = mNextFragmentIndex;
    mNextFragmentIndex = (i + 1);
    paramFragment.setIndex(i, mParent);
    if (mActive == null) {
      mActive = new SparseArray();
    }
    mActive.put(mIndex, paramFragment);
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Allocated fragment index ");
      localStringBuilder.append(paramFragment);
      Log.v("FragmentManager", localStringBuilder.toString());
    }
  }
  
  void makeInactive(Fragment paramFragment)
  {
    if (mIndex < 0) {
      return;
    }
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Freeing fragment index ");
      localStringBuilder.append(paramFragment);
      Log.v("FragmentManager", localStringBuilder.toString());
    }
    mActive.put(mIndex, null);
    mHost.inactivateFragment(mWho);
    paramFragment.initState();
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment)
  {
    if (paramFragment == null) {
      return;
    }
    int i = mCurState;
    int j = i;
    if (mRemoving) {
      if (paramFragment.isInBackStack()) {
        j = Math.min(i, 1);
      } else {
        j = Math.min(i, 0);
      }
    }
    moveToState(paramFragment, j, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
    if (mView != null)
    {
      Object localObject = findFragmentUnder(paramFragment);
      if (localObject != null)
      {
        View localView = mView;
        localObject = mContainer;
        j = ((ViewGroup)localObject).indexOfChild(localView);
        i = ((ViewGroup)localObject).indexOfChild(mView);
        if (i < j)
        {
          ((ViewGroup)localObject).removeViewAt(i);
          ((ViewGroup)localObject).addView(mView, j);
        }
      }
      if ((mIsNewlyAdded) && (mContainer != null))
      {
        mView.setTransitionAlpha(1.0F);
        mIsNewlyAdded = false;
        localObject = loadAnimator(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
        if (localObject != null)
        {
          ((Animator)localObject).setTarget(mView);
          setHWLayerAnimListenerIfAlpha(mView, (Animator)localObject);
          ((Animator)localObject).start();
        }
      }
    }
    if (mHiddenChanged) {
      completeShowHideFragment(paramFragment);
    }
  }
  
  void moveToState(int paramInt, boolean paramBoolean)
  {
    if ((mHost == null) && (paramInt != 0)) {
      throw new IllegalStateException("No activity");
    }
    if ((!paramBoolean) && (mCurState == paramInt)) {
      return;
    }
    mCurState = paramInt;
    if (mActive != null)
    {
      int i = mAdded.size();
      paramInt = 0;
      int j = 0;
      Fragment localFragment;
      while (j < i)
      {
        localFragment = (Fragment)mAdded.get(j);
        moveFragmentToExpectedState(localFragment);
        m = paramInt;
        if (mLoaderManager != null) {
          m = paramInt | mLoaderManager.hasRunningLoaders();
        }
        j++;
        paramInt = m;
      }
      i = mActive.size();
      j = 0;
      int m = paramInt;
      paramInt = j;
      while (paramInt < i)
      {
        localFragment = (Fragment)mActive.valueAt(paramInt);
        j = m;
        int k;
        if (localFragment != null) {
          if (!mRemoving)
          {
            j = m;
            if (!mDetached) {}
          }
          else
          {
            j = m;
            if (!mIsNewlyAdded)
            {
              moveFragmentToExpectedState(localFragment);
              j = m;
              if (mLoaderManager != null) {
                k = m | mLoaderManager.hasRunningLoaders();
              }
            }
          }
        }
        paramInt++;
        m = k;
      }
      if (m == 0) {
        startPendingDeferredFragments();
      }
      if ((mNeedMenuInvalidate) && (mHost != null) && (mCurState == 5))
      {
        mHost.onInvalidateOptionsMenu();
        mNeedMenuInvalidate = false;
      }
    }
  }
  
  void moveToState(Fragment paramFragment)
  {
    moveToState(paramFragment, mCurState, 0, 0, false);
  }
  
  void moveToState(final Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    boolean bool1 = DEBUG;
    boolean bool2 = mAdded;
    bool1 = true;
    if ((bool2) && (!mDetached)) {
      break label46;
    }
    int i = paramInt1;
    paramInt1 = i;
    if (i > 1) {
      paramInt1 = 1;
    }
    label46:
    i = paramInt1;
    if (mRemoving)
    {
      i = paramInt1;
      if (paramInt1 > mState) {
        if ((mState == 0) && (paramFragment.isInBackStack())) {
          i = 1;
        } else {
          i = mState;
        }
      }
    }
    paramInt1 = i;
    if (mDeferStart)
    {
      paramInt1 = i;
      if (mState < 4)
      {
        paramInt1 = i;
        if (i > 3) {
          paramInt1 = 3;
        }
      }
    }
    final ViewGroup localViewGroup;
    Object localObject2;
    Object localObject3;
    if (mState <= paramInt1)
    {
      if ((mFromLayout) && (!mInLayout)) {
        return;
      }
      if (paramFragment.getAnimatingAway() != null)
      {
        paramFragment.setAnimatingAway(null);
        moveToState(paramFragment, paramFragment.getStateAfterAnimating(), 0, 0, true);
      }
      int j = paramInt1;
      paramInt3 = paramInt1;
      i = paramInt1;
      paramInt2 = paramInt1;
      Object localObject1;
      switch (mState)
      {
      default: 
        i = paramInt1;
        break;
      case 0: 
        j = paramInt1;
        if (paramInt1 > 0)
        {
          if (DEBUG)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("moveto CREATED: ");
            ((StringBuilder)localObject1).append(paramFragment);
            Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
          }
          j = paramInt1;
          if (mSavedFragmentState != null)
          {
            mSavedViewState = mSavedFragmentState.getSparseParcelableArray("android:view_state");
            mTarget = getFragment(mSavedFragmentState, "android:target_state");
            if (mTarget != null) {
              mTargetRequestCode = mSavedFragmentState.getInt("android:target_req_state", 0);
            }
            mUserVisibleHint = mSavedFragmentState.getBoolean("android:user_visible_hint", true);
            j = paramInt1;
            if (!mUserVisibleHint)
            {
              mDeferStart = true;
              j = paramInt1;
              if (paramInt1 > 3) {
                j = 3;
              }
            }
          }
          mHost = mHost;
          mParentFragment = mParent;
          if (mParent != null) {
            localObject1 = mParent.mChildFragmentManager;
          } else {
            localObject1 = mHost.getFragmentManagerImpl();
          }
          mFragmentManager = ((FragmentManagerImpl)localObject1);
          if (mTarget != null) {
            if (mActive.get(mTarget.mIndex) == mTarget)
            {
              if (mTarget.mState < 1) {
                moveToState(mTarget, 1, 0, 0, true);
              }
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Fragment ");
              ((StringBuilder)localObject1).append(paramFragment);
              ((StringBuilder)localObject1).append(" declared target fragment ");
              ((StringBuilder)localObject1).append(mTarget);
              ((StringBuilder)localObject1).append(" that does not belong to this FragmentManager!");
              throw new IllegalStateException(((StringBuilder)localObject1).toString());
            }
          }
          dispatchOnFragmentPreAttached(paramFragment, mHost.getContext(), false);
          mCalled = false;
          paramFragment.onAttach(mHost.getContext());
          if (mCalled)
          {
            if (mParentFragment == null) {
              mHost.onAttachFragment(paramFragment);
            } else {
              mParentFragment.onAttachFragment(paramFragment);
            }
            dispatchOnFragmentAttached(paramFragment, mHost.getContext(), false);
            if (!mIsCreated)
            {
              dispatchOnFragmentPreCreated(paramFragment, mSavedFragmentState, false);
              paramFragment.performCreate(mSavedFragmentState);
              dispatchOnFragmentCreated(paramFragment, mSavedFragmentState, false);
            }
            else
            {
              paramFragment.restoreChildFragmentState(mSavedFragmentState, true);
              mState = 1;
            }
            mRetaining = false;
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Fragment ");
            ((StringBuilder)localObject1).append(paramFragment);
            ((StringBuilder)localObject1).append(" did not call through to super.onAttach()");
            throw new SuperNotCalledException(((StringBuilder)localObject1).toString());
          }
        }
      case 1: 
        ensureInflatedFragmentView(paramFragment);
        if (j > 1)
        {
          if (DEBUG)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("moveto ACTIVITY_CREATED: ");
            ((StringBuilder)localObject1).append(paramFragment);
            Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
          }
          if (!mFromLayout)
          {
            localObject1 = null;
            if (mContainerId != 0)
            {
              if (mContainerId == -1)
              {
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Cannot create fragment ");
                ((StringBuilder)localObject1).append(paramFragment);
                ((StringBuilder)localObject1).append(" for a container view with no id");
                throwException(new IllegalArgumentException(((StringBuilder)localObject1).toString()));
              }
              localViewGroup = (ViewGroup)mContainer.onFindViewById(mContainerId);
              if ((localViewGroup == null) && (!mRestored))
              {
                try
                {
                  localObject1 = paramFragment.getResources().getResourceName(mContainerId);
                }
                catch (Resources.NotFoundException localNotFoundException)
                {
                  localObject2 = "unknown";
                }
                localObject3 = new StringBuilder();
                ((StringBuilder)localObject3).append("No view found for id 0x");
                ((StringBuilder)localObject3).append(Integer.toHexString(mContainerId));
                ((StringBuilder)localObject3).append(" (");
                ((StringBuilder)localObject3).append((String)localObject2);
                ((StringBuilder)localObject3).append(") for fragment ");
                ((StringBuilder)localObject3).append(paramFragment);
                throwException(new IllegalArgumentException(((StringBuilder)localObject3).toString()));
              }
              localObject2 = localViewGroup;
            }
            mContainer = ((ViewGroup)localObject2);
            mView = paramFragment.performCreateView(paramFragment.performGetLayoutInflater(mSavedFragmentState), (ViewGroup)localObject2, mSavedFragmentState);
            if (mView != null)
            {
              mView.setSaveFromParentEnabled(false);
              if (localObject2 != null) {
                ((ViewGroup)localObject2).addView(mView);
              }
              if (mHidden) {
                mView.setVisibility(8);
              }
              paramFragment.onViewCreated(mView, mSavedFragmentState);
              dispatchOnFragmentViewCreated(paramFragment, mView, mSavedFragmentState, false);
              if ((mView.getVisibility() == 0) && (mContainer != null)) {
                paramBoolean = bool1;
              } else {
                paramBoolean = false;
              }
              mIsNewlyAdded = paramBoolean;
            }
          }
          paramFragment.performActivityCreated(mSavedFragmentState);
          dispatchOnFragmentActivityCreated(paramFragment, mSavedFragmentState, false);
          if (mView != null) {
            paramFragment.restoreViewState(mSavedFragmentState);
          }
          mSavedFragmentState = null;
        }
        paramInt3 = j;
      case 2: 
        i = paramInt3;
        if (paramInt3 > 2)
        {
          mState = 3;
          i = paramInt3;
        }
      case 3: 
        paramInt2 = i;
        if (i > 3)
        {
          if (DEBUG)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("moveto STARTED: ");
            ((StringBuilder)localObject2).append(paramFragment);
            Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
          }
          paramFragment.performStart();
          dispatchOnFragmentStarted(paramFragment, false);
          paramInt2 = i;
        }
        break;
      }
      i = paramInt2;
      if (paramInt2 > 4)
      {
        if (DEBUG)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("moveto RESUMED: ");
          ((StringBuilder)localObject2).append(paramFragment);
          Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
        }
        paramFragment.performResume();
        dispatchOnFragmentResumed(paramFragment, false);
        mSavedFragmentState = null;
        mSavedViewState = null;
        i = paramInt2;
      }
    }
    for (;;)
    {
      break;
      i = paramInt1;
      if (mState > paramInt1) {
        switch (mState)
        {
        default: 
          i = paramInt1;
          break;
        case 5: 
          if (paramInt1 < 5)
          {
            if (DEBUG)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("movefrom RESUMED: ");
              ((StringBuilder)localObject2).append(paramFragment);
              Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
            }
            paramFragment.performPause();
            dispatchOnFragmentPaused(paramFragment, false);
          }
        case 4: 
          if (paramInt1 < 4)
          {
            if (DEBUG)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("movefrom STARTED: ");
              ((StringBuilder)localObject2).append(paramFragment);
              Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
            }
            paramFragment.performStop();
            dispatchOnFragmentStopped(paramFragment, false);
          }
        case 2: 
        case 3: 
          if (paramInt1 < 2)
          {
            if (DEBUG)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("movefrom ACTIVITY_CREATED: ");
              ((StringBuilder)localObject2).append(paramFragment);
              Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
            }
            if ((mView != null) && (mHost.onShouldSaveFragmentState(paramFragment)) && (mSavedViewState == null)) {
              saveFragmentViewState(paramFragment);
            }
            paramFragment.performDestroyView();
            dispatchOnFragmentViewDestroyed(paramFragment, false);
            if ((mView != null) && (mContainer != null))
            {
              if (getTargetSdk() >= 26)
              {
                mView.clearAnimation();
                mContainer.endViewTransition(mView);
              }
              localObject2 = null;
              if ((mCurState > 0) && (!mDestroyed) && (mView.getVisibility() == 0) && (mView.getTransitionAlpha() > 0.0F)) {
                localObject2 = loadAnimator(paramFragment, paramInt2, false, paramInt3);
              }
              mView.setTransitionAlpha(1.0F);
              if (localObject2 != null)
              {
                localViewGroup = mContainer;
                localObject3 = mView;
                localViewGroup.startViewTransition((View)localObject3);
                paramFragment.setAnimatingAway((Animator)localObject2);
                paramFragment.setStateAfterAnimating(paramInt1);
                ((Animator)localObject2).addListener(new AnimatorListenerAdapter()
                {
                  public void onAnimationEnd(Animator paramAnonymousAnimator)
                  {
                    localViewGroup.endViewTransition(val$view);
                    paramAnonymousAnimator = paramFragment.getAnimatingAway();
                    paramFragment.setAnimatingAway(null);
                    if ((localViewGroup.indexOfChild(val$view) == -1) && (paramAnonymousAnimator != null)) {
                      moveToState(paramFragment, paramFragment.getStateAfterAnimating(), 0, 0, false);
                    }
                  }
                });
                ((Animator)localObject2).setTarget(mView);
                setHWLayerAnimListenerIfAlpha(mView, (Animator)localObject2);
                ((Animator)localObject2).start();
              }
              mContainer.removeView(mView);
            }
            mContainer = null;
            mView = null;
            mInLayout = false;
          }
          break;
        case 1: 
          i = paramInt1;
          if (paramInt1 < 1)
          {
            if ((mDestroyed) && (paramFragment.getAnimatingAway() != null))
            {
              localObject2 = paramFragment.getAnimatingAway();
              paramFragment.setAnimatingAway(null);
              ((Animator)localObject2).cancel();
            }
            if (paramFragment.getAnimatingAway() != null)
            {
              paramFragment.setStateAfterAnimating(paramInt1);
              i = 1;
            }
            else
            {
              if (DEBUG)
              {
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("movefrom CREATED: ");
                ((StringBuilder)localObject2).append(paramFragment);
                Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
              }
              if (!mRetaining)
              {
                paramFragment.performDestroy();
                dispatchOnFragmentDestroyed(paramFragment, false);
              }
              else
              {
                mState = 0;
              }
              paramFragment.performDetach();
              dispatchOnFragmentDetached(paramFragment, false);
              i = paramInt1;
              if (!paramBoolean) {
                if (!mRetaining)
                {
                  makeInactive(paramFragment);
                  i = paramInt1;
                }
                else
                {
                  mHost = null;
                  mParentFragment = null;
                  mFragmentManager = null;
                  i = paramInt1;
                }
              }
            }
          }
          break;
        }
      }
    }
    if (mState != i)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("moveToState: Fragment state for ");
      ((StringBuilder)localObject2).append(paramFragment);
      ((StringBuilder)localObject2).append(" not updated inline; expected state ");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(" found ");
      ((StringBuilder)localObject2).append(mState);
      Log.w("FragmentManager", ((StringBuilder)localObject2).toString());
      mState = i;
    }
  }
  
  public void noteStateNotSaved()
  {
    mSavedNonConfig = null;
    int i = 0;
    mStateSaved = false;
    int j = mAdded.size();
    while (i < j)
    {
      Fragment localFragment = (Fragment)mAdded.get(i);
      if (localFragment != null) {
        localFragment.noteStateNotSaved();
      }
      i++;
    }
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    if (!"fragment".equals(paramString)) {
      return null;
    }
    paramString = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Fragment);
    int i = 0;
    String str1 = paramString;
    if (paramString == null) {
      str1 = localTypedArray.getString(0);
    }
    int j = localTypedArray.getResourceId(1, -1);
    String str2 = localTypedArray.getString(2);
    localTypedArray.recycle();
    if (paramView != null) {
      i = paramView.getId();
    }
    if ((i == -1) && (j == -1) && (str2 == null))
    {
      paramView = new StringBuilder();
      paramView.append(paramAttributeSet.getPositionDescription());
      paramView.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
      paramView.append(str1);
      throw new IllegalArgumentException(paramView.toString());
    }
    if (j != -1) {
      paramView = findFragmentById(j);
    } else {
      paramView = null;
    }
    paramString = paramView;
    if (paramView == null)
    {
      paramString = paramView;
      if (str2 != null) {
        paramString = findFragmentByTag(str2);
      }
    }
    paramView = paramString;
    if (paramString == null)
    {
      paramView = paramString;
      if (i != -1) {
        paramView = findFragmentById(i);
      }
    }
    if (DEBUG)
    {
      paramString = new StringBuilder();
      paramString.append("onCreateView: id=0x");
      paramString.append(Integer.toHexString(j));
      paramString.append(" fname=");
      paramString.append(str1);
      paramString.append(" existing=");
      paramString.append(paramView);
      Log.v("FragmentManager", paramString.toString());
    }
    if (paramView == null)
    {
      paramView = mContainer.instantiate(paramContext, str1, null);
      mFromLayout = true;
      int k;
      if (j != 0) {
        k = j;
      } else {
        k = i;
      }
      mFragmentId = k;
      mContainerId = i;
      mTag = str2;
      mInLayout = true;
      mFragmentManager = this;
      mHost = mHost;
      paramView.onInflate(mHost.getContext(), paramAttributeSet, mSavedFragmentState);
      addFragment(paramView, true);
    }
    else
    {
      if (mInLayout) {
        break label545;
      }
      mInLayout = true;
      mHost = mHost;
      if (!mRetaining) {
        paramView.onInflate(mHost.getContext(), paramAttributeSet, mSavedFragmentState);
      }
    }
    if ((mCurState < 1) && (mFromLayout)) {
      moveToState(paramView, 1, 0, 0, false);
    } else {
      moveToState(paramView);
    }
    if (mView != null)
    {
      if (j != 0) {
        mView.setId(j);
      }
      if (mView.getTag() == null) {
        mView.setTag(str2);
      }
      return mView;
    }
    paramView = new StringBuilder();
    paramView.append("Fragment ");
    paramView.append(str1);
    paramView.append(" did not create a view.");
    throw new IllegalStateException(paramView.toString());
    label545:
    paramView = new StringBuilder();
    paramView.append(paramAttributeSet.getPositionDescription());
    paramView.append(": Duplicate id 0x");
    paramView.append(Integer.toHexString(j));
    paramView.append(", tag ");
    paramView.append(str2);
    paramView.append(", or parent id 0x");
    paramView.append(Integer.toHexString(i));
    paramView.append(" with another fragment for ");
    paramView.append(str1);
    throw new IllegalArgumentException(paramView.toString());
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return null;
  }
  
  public void performPendingDeferredStart(Fragment paramFragment)
  {
    if (mDeferStart)
    {
      if (mExecutingActions)
      {
        mHavePendingDeferredStart = true;
        return;
      }
      mDeferStart = false;
      moveToState(paramFragment, mCurState, 0, 0, false);
    }
  }
  
  public void popBackStack()
  {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 0)
    {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad id: ");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void popBackStack(String paramString, int paramInt)
  {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate()
  {
    checkStateLoss();
    return popBackStackImmediate(null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2)
  {
    checkStateLoss();
    if (paramInt1 >= 0) {
      return popBackStackImmediate(null, paramInt1, paramInt2);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad id: ");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt)
  {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2)
  {
    if (mBackStack == null) {
      return false;
    }
    if ((paramString == null) && (paramInt1 < 0) && ((paramInt2 & 0x1) == 0))
    {
      paramInt1 = mBackStack.size() - 1;
      if (paramInt1 < 0) {
        return false;
      }
      paramArrayList.add((BackStackRecord)mBackStack.remove(paramInt1));
      paramArrayList1.add(Boolean.valueOf(true));
    }
    else
    {
      int i = -1;
      if ((paramString != null) || (paramInt1 >= 0))
      {
        BackStackRecord localBackStackRecord;
        for (int j = mBackStack.size() - 1; j >= 0; j--)
        {
          localBackStackRecord = (BackStackRecord)mBackStack.get(j);
          if (((paramString != null) && (paramString.equals(localBackStackRecord.getName()))) || ((paramInt1 >= 0) && (paramInt1 == mIndex))) {
            break;
          }
        }
        if (j < 0) {
          return false;
        }
        i = j;
        if ((paramInt2 & 0x1) != 0) {
          for (paramInt2 = j - 1;; paramInt2--)
          {
            i = paramInt2;
            if (paramInt2 < 0) {
              break;
            }
            localBackStackRecord = (BackStackRecord)mBackStack.get(paramInt2);
            if ((paramString == null) || (!paramString.equals(localBackStackRecord.getName())))
            {
              i = paramInt2;
              if (paramInt1 < 0) {
                break;
              }
              i = paramInt2;
              if (paramInt1 != mIndex) {
                break;
              }
            }
          }
        }
      }
      if (i == mBackStack.size() - 1) {
        return false;
      }
      for (paramInt1 = mBackStack.size() - 1; paramInt1 > i; paramInt1--)
      {
        paramArrayList.add((BackStackRecord)mBackStack.remove(paramInt1));
        paramArrayList1.add(Boolean.valueOf(true));
      }
    }
    return true;
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment)
  {
    if (mIndex < 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Fragment ");
      localStringBuilder.append(paramFragment);
      localStringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(localStringBuilder.toString()));
    }
    paramBundle.putInt(paramString, mIndex);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean)
  {
    mLifecycleCallbacks.add(new Pair(paramFragmentLifecycleCallbacks, Boolean.valueOf(paramBoolean)));
  }
  
  public void removeFragment(Fragment paramFragment)
  {
    if (DEBUG)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("remove: ");
      ((StringBuilder)???).append(paramFragment);
      ((StringBuilder)???).append(" nesting=");
      ((StringBuilder)???).append(mBackStackNesting);
      Log.v("FragmentManager", ((StringBuilder)???).toString());
    }
    boolean bool = paramFragment.isInBackStack();
    if ((!mDetached) || ((bool ^ true))) {}
    synchronized (mAdded)
    {
      mAdded.remove(paramFragment);
      if ((mHasMenu) && (mMenuVisible)) {
        mNeedMenuInvalidate = true;
      }
      mAdded = false;
      mRemoving = true;
      return;
    }
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener)
  {
    if (mBackStackChangeListeners != null) {
      mBackStackChangeListeners.remove(paramOnBackStackChangedListener);
    }
  }
  
  void reportBackStackChanged()
  {
    if (mBackStackChangeListeners != null) {
      for (int i = 0; i < mBackStackChangeListeners.size(); i++) {
        ((FragmentManager.OnBackStackChangedListener)mBackStackChangeListeners.get(i)).onBackStackChanged();
      }
    }
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig arg2)
  {
    if (paramParcelable == null) {
      return;
    }
    FragmentManagerState localFragmentManagerState = (FragmentManagerState)paramParcelable;
    if (mActive == null) {
      return;
    }
    paramParcelable = null;
    Object localObject1;
    Object localObject2;
    int j;
    Object localObject3;
    if (??? != null)
    {
      localObject1 = ???.getFragments();
      localObject2 = ???.getChildNonConfigs();
      if (localObject1 != null) {
        i = ((List)localObject1).size();
      } else {
        i = 0;
      }
      for (j = 0;; j++)
      {
        paramParcelable = (Parcelable)localObject2;
        if (j >= i) {
          break;
        }
        paramParcelable = (Fragment)((List)localObject1).get(j);
        if (DEBUG)
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("restoreAllState: re-attaching retained ");
          ((StringBuilder)localObject3).append(paramParcelable);
          Log.v("FragmentManager", ((StringBuilder)localObject3).toString());
        }
        for (int k = 0; (k < mActive.length) && (mActive[k].mIndex != mIndex); k++) {}
        if (k == mActive.length)
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Could not find active fragment with index ");
          ((StringBuilder)localObject3).append(mIndex);
          throwException(new IllegalStateException(((StringBuilder)localObject3).toString()));
        }
        localObject3 = mActive[k];
        mInstance = paramParcelable;
        mSavedViewState = null;
        mBackStackNesting = 0;
        mInLayout = false;
        mAdded = false;
        mTarget = null;
        if (mSavedFragmentState != null)
        {
          mSavedFragmentState.setClassLoader(mHost.getContext().getClassLoader());
          mSavedViewState = mSavedFragmentState.getSparseParcelableArray("android:view_state");
          mSavedFragmentState = mSavedFragmentState;
        }
      }
    }
    mActive = new SparseArray(mActive.length);
    for (int i = 0; i < mActive.length; i++)
    {
      localObject3 = mActive[i];
      if (localObject3 != null)
      {
        localObject1 = null;
        localObject2 = localObject1;
        if (paramParcelable != null)
        {
          localObject2 = localObject1;
          if (i < paramParcelable.size()) {
            localObject2 = (FragmentManagerNonConfig)paramParcelable.get(i);
          }
        }
        localObject1 = ((FragmentState)localObject3).instantiate(mHost, mContainer, mParent, (FragmentManagerNonConfig)localObject2);
        if (DEBUG)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("restoreAllState: active #");
          ((StringBuilder)localObject2).append(i);
          ((StringBuilder)localObject2).append(": ");
          ((StringBuilder)localObject2).append(localObject1);
          Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
        }
        mActive.put(mIndex, localObject1);
        mInstance = null;
      }
    }
    if (??? != null)
    {
      ??? = ???.getFragments();
      if (??? != null) {
        i = ???.size();
      } else {
        i = 0;
      }
      for (j = 0; j < i; j++)
      {
        localObject2 = (Fragment)???.get(j);
        if (mTargetIndex >= 0)
        {
          mTarget = ((Fragment)mActive.get(mTargetIndex));
          if (mTarget == null)
          {
            paramParcelable = new StringBuilder();
            paramParcelable.append("Re-attaching retained fragment ");
            paramParcelable.append(localObject2);
            paramParcelable.append(" target no longer exists: ");
            paramParcelable.append(mTargetIndex);
            Log.w("FragmentManager", paramParcelable.toString());
            mTarget = null;
          }
        }
      }
    }
    mAdded.clear();
    if (mAdded != null)
    {
      i = 0;
      for (;;)
      {
        if (i >= mAdded.length) {
          break label848;
        }
        paramParcelable = (Fragment)mActive.get(mAdded[i]);
        if (paramParcelable == null)
        {
          ??? = new StringBuilder();
          ???.append("No instantiated fragment for index #");
          ???.append(mAdded[i]);
          throwException(new IllegalStateException(???.toString()));
        }
        mAdded = true;
        if (DEBUG)
        {
          ??? = new StringBuilder();
          ???.append("restoreAllState: added #");
          ???.append(i);
          ???.append(": ");
          ???.append(paramParcelable);
          Log.v("FragmentManager", ???.toString());
        }
        if (!mAdded.contains(paramParcelable)) {
          synchronized (mAdded)
          {
            mAdded.add(paramParcelable);
            i++;
          }
        }
      }
      throw new IllegalStateException("Already added!");
    }
    label848:
    if (mBackStack != null)
    {
      mBackStack = new ArrayList(mBackStack.length);
      for (i = 0; i < mBackStack.length; i++)
      {
        paramParcelable = mBackStack[i].instantiate(this);
        if (DEBUG)
        {
          ??? = new StringBuilder();
          ???.append("restoreAllState: back stack #");
          ???.append(i);
          ???.append(" (index ");
          ???.append(mIndex);
          ???.append("): ");
          ???.append(paramParcelable);
          Log.v("FragmentManager", ???.toString());
          ??? = new FastPrintWriter(new LogWriter(2, "FragmentManager"), false, 1024);
          paramParcelable.dump("  ", ???, false);
          ???.flush();
        }
        mBackStack.add(paramParcelable);
        if (mIndex >= 0) {
          setBackStackIndex(mIndex, paramParcelable);
        }
      }
    }
    mBackStack = null;
    if (mPrimaryNavActiveIndex >= 0) {
      mPrimaryNav = ((Fragment)mActive.get(mPrimaryNavActiveIndex));
    }
    mNextFragmentIndex = mNextFragmentIndex;
  }
  
  FragmentManagerNonConfig retainNonConfig()
  {
    setRetaining(mSavedNonConfig);
    return mSavedNonConfig;
  }
  
  Parcelable saveAllState()
  {
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    mStateSaved = true;
    mSavedNonConfig = null;
    if ((mActive != null) && (mActive.size() > 0))
    {
      int i = mActive.size();
      FragmentState[] arrayOfFragmentState = new FragmentState[i];
      int j = 0;
      int k = 0;
      for (int m = 0; m < i; m++)
      {
        localObject1 = (Fragment)mActive.valueAt(m);
        if (localObject1 != null)
        {
          if (mIndex < 0)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Failure saving state: active ");
            ((StringBuilder)localObject2).append(localObject1);
            ((StringBuilder)localObject2).append(" has cleared index: ");
            ((StringBuilder)localObject2).append(mIndex);
            throwException(new IllegalStateException(((StringBuilder)localObject2).toString()));
          }
          int n = 1;
          localObject2 = new FragmentState((Fragment)localObject1);
          arrayOfFragmentState[m] = localObject2;
          if ((mState > 0) && (mSavedFragmentState == null))
          {
            mSavedFragmentState = saveFragmentBasicState((Fragment)localObject1);
            if (mTarget != null)
            {
              if (mTarget.mIndex < 0)
              {
                localObject3 = new StringBuilder();
                ((StringBuilder)localObject3).append("Failure saving state: ");
                ((StringBuilder)localObject3).append(localObject1);
                ((StringBuilder)localObject3).append(" has target not in fragment manager: ");
                ((StringBuilder)localObject3).append(mTarget);
                throwException(new IllegalStateException(((StringBuilder)localObject3).toString()));
              }
              if (mSavedFragmentState == null) {
                mSavedFragmentState = new Bundle();
              }
              putFragment(mSavedFragmentState, "android:target_state", mTarget);
              if (mTargetRequestCode != 0) {
                mSavedFragmentState.putInt("android:target_req_state", mTargetRequestCode);
              }
            }
          }
          else
          {
            mSavedFragmentState = mSavedFragmentState;
          }
          k = n;
          if (DEBUG)
          {
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("Saved state of ");
            ((StringBuilder)localObject3).append(localObject1);
            ((StringBuilder)localObject3).append(": ");
            ((StringBuilder)localObject3).append(mSavedFragmentState);
            Log.v("FragmentManager", ((StringBuilder)localObject3).toString());
            k = n;
          }
        }
      }
      if (k == 0)
      {
        if (DEBUG) {
          Log.v("FragmentManager", "saveAllState: no fragments!");
        }
        return null;
      }
      Object localObject1 = null;
      Object localObject3 = null;
      m = mAdded.size();
      if (m > 0)
      {
        localObject2 = new int[m];
        for (k = 0;; k++)
        {
          localObject1 = localObject2;
          if (k >= m) {
            break;
          }
          localObject2[k] = mAdded.get(k)).mIndex;
          if (localObject2[k] < 0)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Failure saving state: active ");
            ((StringBuilder)localObject1).append(mAdded.get(k));
            ((StringBuilder)localObject1).append(" has cleared index: ");
            ((StringBuilder)localObject1).append(localObject2[k]);
            throwException(new IllegalStateException(((StringBuilder)localObject1).toString()));
          }
          if (DEBUG)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("saveAllState: adding fragment #");
            ((StringBuilder)localObject1).append(k);
            ((StringBuilder)localObject1).append(": ");
            ((StringBuilder)localObject1).append(mAdded.get(k));
            Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
          }
        }
      }
      Object localObject2 = localObject3;
      if (mBackStack != null)
      {
        m = mBackStack.size();
        localObject2 = localObject3;
        if (m > 0)
        {
          localObject3 = new BackStackState[m];
          for (k = j;; k++)
          {
            localObject2 = localObject3;
            if (k >= m) {
              break;
            }
            localObject3[k] = new BackStackState(this, (BackStackRecord)mBackStack.get(k));
            if (DEBUG)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("saveAllState: adding back stack #");
              ((StringBuilder)localObject2).append(k);
              ((StringBuilder)localObject2).append(": ");
              ((StringBuilder)localObject2).append(mBackStack.get(k));
              Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
            }
          }
        }
      }
      localObject3 = new FragmentManagerState();
      mActive = arrayOfFragmentState;
      mAdded = ((int[])localObject1);
      mBackStack = ((BackStackState[])localObject2);
      mNextFragmentIndex = mNextFragmentIndex;
      if (mPrimaryNav != null) {
        mPrimaryNavActiveIndex = mPrimaryNav.mIndex;
      }
      saveNonConfig();
      return localObject3;
    }
    return null;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment)
  {
    Object localObject1 = null;
    if (mStateBundle == null) {
      mStateBundle = new Bundle();
    }
    paramFragment.performSaveInstanceState(mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, mStateBundle, false);
    if (!mStateBundle.isEmpty())
    {
      localObject1 = mStateBundle;
      mStateBundle = null;
    }
    if (mView != null) {
      saveFragmentViewState(paramFragment);
    }
    Object localObject2 = localObject1;
    if (mSavedViewState != null)
    {
      localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new Bundle();
      }
      ((Bundle)localObject2).putSparseParcelableArray("android:view_state", mSavedViewState);
    }
    localObject1 = localObject2;
    if (!mUserVisibleHint)
    {
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = new Bundle();
      }
      ((Bundle)localObject1).putBoolean("android:user_visible_hint", mUserVisibleHint);
    }
    return localObject1;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment)
  {
    if (mIndex < 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Fragment ");
      localStringBuilder.append(paramFragment);
      localStringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(localStringBuilder.toString()));
    }
    int i = mState;
    StringBuilder localStringBuilder = null;
    if (i > 0)
    {
      Bundle localBundle = saveFragmentBasicState(paramFragment);
      paramFragment = localStringBuilder;
      if (localBundle != null) {
        paramFragment = new Fragment.SavedState(localBundle);
      }
      return paramFragment;
    }
    return null;
  }
  
  void saveFragmentViewState(Fragment paramFragment)
  {
    if (mView == null) {
      return;
    }
    if (mStateArray == null) {
      mStateArray = new SparseArray();
    } else {
      mStateArray.clear();
    }
    mView.saveHierarchyState(mStateArray);
    if (mStateArray.size() > 0)
    {
      mSavedViewState = mStateArray;
      mStateArray = null;
    }
  }
  
  void saveNonConfig()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (mActive != null)
    {
      localObject2 = null;
      localObject1 = null;
      int i = 0;
      while (i < mActive.size())
      {
        Object localObject3 = (Fragment)mActive.valueAt(i);
        Object localObject4 = localObject1;
        Object localObject5 = localObject2;
        if (localObject3 != null)
        {
          Object localObject6 = localObject1;
          int j;
          if (mRetainInstance)
          {
            localObject4 = localObject1;
            if (localObject1 == null) {
              localObject4 = new ArrayList();
            }
            ((ArrayList)localObject4).add(localObject3);
            if (mTarget != null) {
              j = mTarget.mIndex;
            } else {
              j = -1;
            }
            mTargetIndex = j;
            localObject6 = localObject4;
            if (DEBUG)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("retainNonConfig: keeping retained ");
              ((StringBuilder)localObject1).append(localObject3);
              Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
              localObject6 = localObject4;
            }
          }
          if (mChildFragmentManager != null)
          {
            mChildFragmentManager.saveNonConfig();
            localObject3 = mChildFragmentManager.mSavedNonConfig;
          }
          else
          {
            localObject3 = mChildNonConfig;
          }
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            localObject1 = localObject2;
            if (localObject3 != null)
            {
              localObject2 = new ArrayList(mActive.size());
              for (j = 0;; j++)
              {
                localObject1 = localObject2;
                if (j >= i) {
                  break;
                }
                ((ArrayList)localObject2).add(null);
              }
            }
          }
          localObject4 = localObject6;
          localObject5 = localObject1;
          if (localObject1 != null)
          {
            ((ArrayList)localObject1).add(localObject3);
            localObject5 = localObject1;
            localObject4 = localObject6;
          }
        }
        i++;
        localObject1 = localObject4;
        localObject2 = localObject5;
      }
    }
    if ((localObject1 == null) && (localObject2 == null)) {
      mSavedNonConfig = null;
    } else {
      mSavedNonConfig = new FragmentManagerNonConfig((List)localObject1, (List)localObject2);
    }
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord)
  {
    try
    {
      Object localObject;
      if (mBackStackIndices == null)
      {
        localObject = new java/util/ArrayList;
        ((ArrayList)localObject).<init>();
        mBackStackIndices = ((ArrayList)localObject);
      }
      int i = mBackStackIndices.size();
      int j = i;
      if (paramInt < i)
      {
        if (DEBUG)
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Setting back stack index ");
          ((StringBuilder)localObject).append(paramInt);
          ((StringBuilder)localObject).append(" to ");
          ((StringBuilder)localObject).append(paramBackStackRecord);
          Log.v("FragmentManager", ((StringBuilder)localObject).toString());
        }
        mBackStackIndices.set(paramInt, paramBackStackRecord);
      }
      else
      {
        while (j < paramInt)
        {
          mBackStackIndices.add(null);
          if (mAvailBackStackIndices == null)
          {
            localObject = new java/util/ArrayList;
            ((ArrayList)localObject).<init>();
            mAvailBackStackIndices = ((ArrayList)localObject);
          }
          if (DEBUG)
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("Adding available back stack index ");
            ((StringBuilder)localObject).append(j);
            Log.v("FragmentManager", ((StringBuilder)localObject).toString());
          }
          mAvailBackStackIndices.add(Integer.valueOf(j));
          j++;
        }
        if (DEBUG)
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Adding back stack index ");
          ((StringBuilder)localObject).append(paramInt);
          ((StringBuilder)localObject).append(" with ");
          ((StringBuilder)localObject).append(paramBackStackRecord);
          Log.v("FragmentManager", ((StringBuilder)localObject).toString());
        }
        mBackStackIndices.add(paramBackStackRecord);
      }
      return;
    }
    finally {}
  }
  
  public void setPrimaryNavigationFragment(Fragment paramFragment)
  {
    if ((paramFragment != null) && ((mActive.get(mIndex) != paramFragment) || ((mHost != null) && (paramFragment.getFragmentManager() != this))))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Fragment ");
      localStringBuilder.append(paramFragment);
      localStringBuilder.append(" is not an active fragment of FragmentManager ");
      localStringBuilder.append(this);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    mPrimaryNav = paramFragment;
  }
  
  public void showFragment(Fragment paramFragment)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("show: ");
      localStringBuilder.append(paramFragment);
      Log.v("FragmentManager", localStringBuilder.toString());
    }
    if (mHidden)
    {
      mHidden = false;
      mHiddenChanged ^= true;
    }
  }
  
  void startPendingDeferredFragments()
  {
    if (mActive == null) {
      return;
    }
    for (int i = 0; i < mActive.size(); i++)
    {
      Fragment localFragment = (Fragment)mActive.valueAt(i);
      if (localFragment != null) {
        performPendingDeferredStart(localFragment);
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("FragmentManager{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" in ");
    if (mParent != null) {
      DebugUtils.buildShortClassTag(mParent, localStringBuilder);
    } else {
      DebugUtils.buildShortClassTag(mHost, localStringBuilder);
    }
    localStringBuilder.append("}}");
    return localStringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks)
  {
    CopyOnWriteArrayList localCopyOnWriteArrayList = mLifecycleCallbacks;
    int i = 0;
    try
    {
      int j = mLifecycleCallbacks.size();
      while (i < j)
      {
        if (mLifecycleCallbacks.get(i)).first == paramFragmentLifecycleCallbacks)
        {
          mLifecycleCallbacks.remove(i);
          break;
        }
        i++;
      }
      return;
    }
    finally {}
  }
  
  static class AnimateOnHWLayerIfNeededListener
    implements Animator.AnimatorListener
  {
    private boolean mShouldRunOnHWLayer = false;
    private View mView;
    
    public AnimateOnHWLayerIfNeededListener(View paramView)
    {
      if (paramView == null) {
        return;
      }
      mView = paramView;
    }
    
    public void onAnimationCancel(Animator paramAnimator) {}
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (mShouldRunOnHWLayer) {
        mView.setLayerType(0, null);
      }
      mView = null;
      paramAnimator.removeListener(this);
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator)
    {
      mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(mView, paramAnimator);
      if (mShouldRunOnHWLayer) {
        mView.setLayerType(2, null);
      }
    }
  }
  
  static abstract interface OpGenerator
  {
    public abstract boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1);
  }
  
  private class PopBackStackState
    implements FragmentManagerImpl.OpGenerator
  {
    final int mFlags;
    final int mId;
    final String mName;
    
    public PopBackStackState(String paramString, int paramInt1, int paramInt2)
    {
      mName = paramString;
      mId = paramInt1;
      mFlags = paramInt2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
    {
      if ((mPrimaryNav != null) && (mId < 0) && (mName == null))
      {
        FragmentManagerImpl localFragmentManagerImpl = mPrimaryNav.mChildFragmentManager;
        if ((localFragmentManagerImpl != null) && (localFragmentManagerImpl.popBackStackImmediate())) {
          return false;
        }
      }
      return popBackStackState(paramArrayList, paramArrayList1, mName, mId, mFlags);
    }
  }
  
  static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener
  {
    private final boolean mIsBack;
    private int mNumPostponed;
    private final BackStackRecord mRecord;
    
    public StartEnterTransitionListener(BackStackRecord paramBackStackRecord, boolean paramBoolean)
    {
      mIsBack = paramBoolean;
      mRecord = paramBackStackRecord;
    }
    
    public void cancelTransaction()
    {
      mRecord.mManager.completeExecute(mRecord, mIsBack, false, false);
    }
    
    public void completeTransaction()
    {
      int i = mNumPostponed;
      boolean bool1 = false;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      }
      Object localObject1 = mRecord.mManager;
      int j = mAdded.size();
      for (int k = 0; k < j; k++)
      {
        localObject2 = (Fragment)mAdded.get(k);
        ((Fragment)localObject2).setOnStartEnterTransitionListener(null);
        if ((i != 0) && (((Fragment)localObject2).isPostponed())) {
          ((Fragment)localObject2).startPostponedEnterTransition();
        }
      }
      Object localObject2 = mRecord.mManager;
      localObject1 = mRecord;
      boolean bool2 = mIsBack;
      if (i == 0) {
        bool1 = true;
      }
      ((FragmentManagerImpl)localObject2).completeExecute((BackStackRecord)localObject1, bool2, bool1, true);
    }
    
    public boolean isReady()
    {
      boolean bool;
      if (mNumPostponed == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onStartEnterTransition()
    {
      mNumPostponed -= 1;
      if (mNumPostponed != 0) {
        return;
      }
      mRecord.mManager.scheduleCommit();
    }
    
    public void startListening()
    {
      mNumPostponed += 1;
    }
  }
}
