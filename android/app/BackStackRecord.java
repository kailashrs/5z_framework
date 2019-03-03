package android.app;

import android.content.Context;
import android.util.Log;
import android.util.LogWriter;
import android.view.View;
import com.android.internal.util.FastPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord
  extends FragmentTransaction
  implements FragmentManager.BackStackEntry, FragmentManagerImpl.OpGenerator
{
  static final int OP_ADD = 1;
  static final int OP_ATTACH = 7;
  static final int OP_DETACH = 6;
  static final int OP_HIDE = 4;
  static final int OP_NULL = 0;
  static final int OP_REMOVE = 3;
  static final int OP_REPLACE = 2;
  static final int OP_SET_PRIMARY_NAV = 8;
  static final int OP_SHOW = 5;
  static final int OP_UNSET_PRIMARY_NAV = 9;
  static final String TAG = "FragmentManager";
  boolean mAddToBackStack;
  boolean mAllowAddToBackStack;
  int mBreadCrumbShortTitleRes;
  CharSequence mBreadCrumbShortTitleText;
  int mBreadCrumbTitleRes;
  CharSequence mBreadCrumbTitleText;
  ArrayList<Runnable> mCommitRunnables;
  boolean mCommitted;
  int mEnterAnim;
  int mExitAnim;
  int mIndex;
  final FragmentManagerImpl mManager;
  String mName;
  ArrayList<Op> mOps = new ArrayList();
  int mPopEnterAnim;
  int mPopExitAnim;
  boolean mReorderingAllowed;
  ArrayList<String> mSharedElementSourceNames;
  ArrayList<String> mSharedElementTargetNames;
  int mTransition;
  int mTransitionStyle;
  
  public BackStackRecord(FragmentManagerImpl paramFragmentManagerImpl)
  {
    boolean bool = true;
    mAllowAddToBackStack = true;
    mIndex = -1;
    mManager = paramFragmentManagerImpl;
    if (mManager.getTargetSdk() <= 25) {
      bool = false;
    }
    mReorderingAllowed = bool;
  }
  
  private void doAddOp(int paramInt1, Fragment paramFragment, String paramString, int paramInt2)
  {
    Object localObject;
    if (mManager.getTargetSdk() > 25)
    {
      localObject = paramFragment.getClass();
      int i = ((Class)localObject).getModifiers();
      if ((((Class)localObject).isAnonymousClass()) || (!Modifier.isPublic(i)) || ((((Class)localObject).isMemberClass()) && (!Modifier.isStatic(i))))
      {
        paramFragment = new StringBuilder();
        paramFragment.append("Fragment ");
        paramFragment.append(((Class)localObject).getCanonicalName());
        paramFragment.append(" must be a public static class to be  properly recreated from instance state.");
        throw new IllegalStateException(paramFragment.toString());
      }
    }
    mFragmentManager = mManager;
    if (paramString != null)
    {
      if ((mTag != null) && (!paramString.equals(mTag)))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Can't change tag of fragment ");
        ((StringBuilder)localObject).append(paramFragment);
        ((StringBuilder)localObject).append(": was ");
        ((StringBuilder)localObject).append(mTag);
        ((StringBuilder)localObject).append(" now ");
        ((StringBuilder)localObject).append(paramString);
        throw new IllegalStateException(((StringBuilder)localObject).toString());
      }
      mTag = paramString;
    }
    if (paramInt1 != 0) {
      if (paramInt1 != -1)
      {
        if ((mFragmentId != 0) && (mFragmentId != paramInt1))
        {
          paramString = new StringBuilder();
          paramString.append("Can't change container ID of fragment ");
          paramString.append(paramFragment);
          paramString.append(": was ");
          paramString.append(mFragmentId);
          paramString.append(" now ");
          paramString.append(paramInt1);
          throw new IllegalStateException(paramString.toString());
        }
        mFragmentId = paramInt1;
        mContainerId = paramInt1;
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Can't add fragment ");
        ((StringBuilder)localObject).append(paramFragment);
        ((StringBuilder)localObject).append(" with tag ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" to container view with no id");
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
    }
    addOp(new Op(paramInt2, paramFragment));
  }
  
  private static boolean isFragmentPostponed(Op paramOp)
  {
    paramOp = fragment;
    boolean bool;
    if ((paramOp != null) && (mAdded) && (mView != null) && (!mDetached) && (!mHidden) && (paramOp.isPostponed())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public FragmentTransaction add(int paramInt, Fragment paramFragment)
  {
    doAddOp(paramInt, paramFragment, null, 1);
    return this;
  }
  
  public FragmentTransaction add(int paramInt, Fragment paramFragment, String paramString)
  {
    doAddOp(paramInt, paramFragment, paramString, 1);
    return this;
  }
  
  public FragmentTransaction add(Fragment paramFragment, String paramString)
  {
    doAddOp(0, paramFragment, paramString, 1);
    return this;
  }
  
  void addOp(Op paramOp)
  {
    mOps.add(paramOp);
    enterAnim = mEnterAnim;
    exitAnim = mExitAnim;
    popEnterAnim = mPopEnterAnim;
    popExitAnim = mPopExitAnim;
  }
  
  public FragmentTransaction addSharedElement(View paramView, String paramString)
  {
    paramView = paramView.getTransitionName();
    if (paramView != null)
    {
      if (mSharedElementSourceNames == null)
      {
        mSharedElementSourceNames = new ArrayList();
        mSharedElementTargetNames = new ArrayList();
      }
      else
      {
        if (mSharedElementTargetNames.contains(paramString)) {
          break label125;
        }
        if (mSharedElementSourceNames.contains(paramView)) {
          break label83;
        }
      }
      mSharedElementSourceNames.add(paramView);
      mSharedElementTargetNames.add(paramString);
      return this;
      label83:
      paramString = new StringBuilder();
      paramString.append("A shared element with the source name '");
      paramString.append(paramView);
      paramString.append(" has already been added to the transaction.");
      throw new IllegalArgumentException(paramString.toString());
      label125:
      paramView = new StringBuilder();
      paramView.append("A shared element with the target name '");
      paramView.append(paramString);
      paramView.append("' has already been added to the transaction.");
      throw new IllegalArgumentException(paramView.toString());
    }
    throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
  }
  
  public FragmentTransaction addToBackStack(String paramString)
  {
    if (mAllowAddToBackStack)
    {
      mAddToBackStack = true;
      mName = paramString;
      return this;
    }
    throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
  }
  
  public FragmentTransaction attach(Fragment paramFragment)
  {
    addOp(new Op(7, paramFragment));
    return this;
  }
  
  void bumpBackStackNesting(int paramInt)
  {
    if (!mAddToBackStack) {
      return;
    }
    Object localObject1;
    if (FragmentManagerImpl.DEBUG)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Bump nesting in ");
      ((StringBuilder)localObject1).append(this);
      ((StringBuilder)localObject1).append(" by ");
      ((StringBuilder)localObject1).append(paramInt);
      Log.v("FragmentManager", ((StringBuilder)localObject1).toString());
    }
    int i = mOps.size();
    for (int j = 0; j < i; j++)
    {
      localObject1 = (Op)mOps.get(j);
      if (fragment != null)
      {
        Object localObject2 = fragment;
        mBackStackNesting += paramInt;
        if (FragmentManagerImpl.DEBUG)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Bump nesting of ");
          ((StringBuilder)localObject2).append(fragment);
          ((StringBuilder)localObject2).append(" to ");
          ((StringBuilder)localObject2).append(fragment.mBackStackNesting);
          Log.v("FragmentManager", ((StringBuilder)localObject2).toString());
        }
      }
    }
  }
  
  public int commit()
  {
    return commitInternal(false);
  }
  
  public int commitAllowingStateLoss()
  {
    return commitInternal(true);
  }
  
  int commitInternal(boolean paramBoolean)
  {
    if (!mCommitted)
    {
      if (FragmentManagerImpl.DEBUG)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Commit: ");
        ((StringBuilder)localObject).append(this);
        Log.v("FragmentManager", ((StringBuilder)localObject).toString());
        localObject = new FastPrintWriter(new LogWriter(2, "FragmentManager"), false, 1024);
        dump("  ", null, (PrintWriter)localObject, null);
        ((PrintWriter)localObject).flush();
      }
      mCommitted = true;
      if (mAddToBackStack) {
        mIndex = mManager.allocBackStackIndex(this);
      } else {
        mIndex = -1;
      }
      mManager.enqueueAction(this, paramBoolean);
      return mIndex;
    }
    throw new IllegalStateException("commit already called");
  }
  
  public void commitNow()
  {
    disallowAddToBackStack();
    mManager.execSingleAction(this, false);
  }
  
  public void commitNowAllowingStateLoss()
  {
    disallowAddToBackStack();
    mManager.execSingleAction(this, true);
  }
  
  public FragmentTransaction detach(Fragment paramFragment)
  {
    addOp(new Op(6, paramFragment));
    return this;
  }
  
  public FragmentTransaction disallowAddToBackStack()
  {
    if (!mAddToBackStack)
    {
      mAllowAddToBackStack = false;
      return this;
    }
    throw new IllegalStateException("This transaction is already being added to the back stack");
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    dump(paramString, paramPrintWriter, true);
  }
  
  void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mName=");
      paramPrintWriter.print(mName);
      paramPrintWriter.print(" mIndex=");
      paramPrintWriter.print(mIndex);
      paramPrintWriter.print(" mCommitted=");
      paramPrintWriter.println(mCommitted);
      if (mTransition != 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mTransition=#");
        paramPrintWriter.print(Integer.toHexString(mTransition));
        paramPrintWriter.print(" mTransitionStyle=#");
        paramPrintWriter.println(Integer.toHexString(mTransitionStyle));
      }
      if ((mEnterAnim != 0) || (mExitAnim != 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mEnterAnim=#");
        paramPrintWriter.print(Integer.toHexString(mEnterAnim));
        paramPrintWriter.print(" mExitAnim=#");
        paramPrintWriter.println(Integer.toHexString(mExitAnim));
      }
      if ((mPopEnterAnim != 0) || (mPopExitAnim != 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mPopEnterAnim=#");
        paramPrintWriter.print(Integer.toHexString(mPopEnterAnim));
        paramPrintWriter.print(" mPopExitAnim=#");
        paramPrintWriter.println(Integer.toHexString(mPopExitAnim));
      }
      if ((mBreadCrumbTitleRes != 0) || (mBreadCrumbTitleText != null))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbTitleRes=#");
        paramPrintWriter.print(Integer.toHexString(mBreadCrumbTitleRes));
        paramPrintWriter.print(" mBreadCrumbTitleText=");
        paramPrintWriter.println(mBreadCrumbTitleText);
      }
      if ((mBreadCrumbShortTitleRes != 0) || (mBreadCrumbShortTitleText != null))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbShortTitleRes=#");
        paramPrintWriter.print(Integer.toHexString(mBreadCrumbShortTitleRes));
        paramPrintWriter.print(" mBreadCrumbShortTitleText=");
        paramPrintWriter.println(mBreadCrumbShortTitleText);
      }
    }
    if (!mOps.isEmpty())
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Operations:");
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("    ");
      String str = ((StringBuilder)localObject).toString();
      int i = mOps.size();
      for (int j = 0; j < i; j++)
      {
        Op localOp = (Op)mOps.get(j);
        switch (cmd)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("cmd=");
          ((StringBuilder)localObject).append(cmd);
          localObject = ((StringBuilder)localObject).toString();
          break;
        case 9: 
          localObject = "UNSET_PRIMARY_NAV";
          break;
        case 8: 
          localObject = "SET_PRIMARY_NAV";
          break;
        case 7: 
          localObject = "ATTACH";
          break;
        case 6: 
          localObject = "DETACH";
          break;
        case 5: 
          localObject = "SHOW";
          break;
        case 4: 
          localObject = "HIDE";
          break;
        case 3: 
          localObject = "REMOVE";
          break;
        case 2: 
          localObject = "REPLACE";
          break;
        case 1: 
          localObject = "ADD";
          break;
        case 0: 
          localObject = "NULL";
        }
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  Op #");
        paramPrintWriter.print(j);
        paramPrintWriter.print(": ");
        paramPrintWriter.print((String)localObject);
        paramPrintWriter.print(" ");
        paramPrintWriter.println(fragment);
        if (paramBoolean)
        {
          if ((enterAnim != 0) || (exitAnim != 0))
          {
            paramPrintWriter.print(str);
            paramPrintWriter.print("enterAnim=#");
            paramPrintWriter.print(Integer.toHexString(enterAnim));
            paramPrintWriter.print(" exitAnim=#");
            paramPrintWriter.println(Integer.toHexString(exitAnim));
          }
          if ((popEnterAnim != 0) || (popExitAnim != 0))
          {
            paramPrintWriter.print(str);
            paramPrintWriter.print("popEnterAnim=#");
            paramPrintWriter.print(Integer.toHexString(popEnterAnim));
            paramPrintWriter.print(" popExitAnim=#");
            paramPrintWriter.println(Integer.toHexString(popExitAnim));
          }
        }
      }
    }
  }
  
  void executeOps()
  {
    int i = mOps.size();
    for (int j = 0; j < i; j++)
    {
      Op localOp = (Op)mOps.get(j);
      Object localObject = fragment;
      if (localObject != null) {
        ((Fragment)localObject).setNextTransition(mTransition, mTransitionStyle);
      }
      int k = cmd;
      if (k != 1)
      {
        switch (k)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown cmd: ");
          ((StringBuilder)localObject).append(cmd);
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        case 9: 
          mManager.setPrimaryNavigationFragment(null);
          break;
        case 8: 
          mManager.setPrimaryNavigationFragment((Fragment)localObject);
          break;
        case 7: 
          ((Fragment)localObject).setNextAnim(enterAnim);
          mManager.attachFragment((Fragment)localObject);
          break;
        case 6: 
          ((Fragment)localObject).setNextAnim(exitAnim);
          mManager.detachFragment((Fragment)localObject);
          break;
        case 5: 
          ((Fragment)localObject).setNextAnim(enterAnim);
          mManager.showFragment((Fragment)localObject);
          break;
        case 4: 
          ((Fragment)localObject).setNextAnim(exitAnim);
          mManager.hideFragment((Fragment)localObject);
          break;
        case 3: 
          ((Fragment)localObject).setNextAnim(exitAnim);
          mManager.removeFragment((Fragment)localObject);
          break;
        }
      }
      else
      {
        ((Fragment)localObject).setNextAnim(enterAnim);
        mManager.addFragment((Fragment)localObject, false);
      }
      if ((!mReorderingAllowed) && (cmd != 1) && (localObject != null)) {
        mManager.moveFragmentToExpectedState((Fragment)localObject);
      }
    }
    if (!mReorderingAllowed) {
      mManager.moveToState(mManager.mCurState, true);
    }
  }
  
  void executePopOps(boolean paramBoolean)
  {
    for (int i = mOps.size() - 1; i >= 0; i--)
    {
      Op localOp = (Op)mOps.get(i);
      Object localObject = fragment;
      if (localObject != null) {
        ((Fragment)localObject).setNextTransition(FragmentManagerImpl.reverseTransit(mTransition), mTransitionStyle);
      }
      int j = cmd;
      if (j != 1)
      {
        switch (j)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown cmd: ");
          ((StringBuilder)localObject).append(cmd);
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        case 9: 
          mManager.setPrimaryNavigationFragment((Fragment)localObject);
          break;
        case 8: 
          mManager.setPrimaryNavigationFragment(null);
          break;
        case 7: 
          ((Fragment)localObject).setNextAnim(popExitAnim);
          mManager.detachFragment((Fragment)localObject);
          break;
        case 6: 
          ((Fragment)localObject).setNextAnim(popEnterAnim);
          mManager.attachFragment((Fragment)localObject);
          break;
        case 5: 
          ((Fragment)localObject).setNextAnim(popExitAnim);
          mManager.hideFragment((Fragment)localObject);
          break;
        case 4: 
          ((Fragment)localObject).setNextAnim(popEnterAnim);
          mManager.showFragment((Fragment)localObject);
          break;
        case 3: 
          ((Fragment)localObject).setNextAnim(popEnterAnim);
          mManager.addFragment((Fragment)localObject, false);
          break;
        }
      }
      else
      {
        ((Fragment)localObject).setNextAnim(popExitAnim);
        mManager.removeFragment((Fragment)localObject);
      }
      if ((!mReorderingAllowed) && (cmd != 3) && (localObject != null)) {
        mManager.moveFragmentToExpectedState((Fragment)localObject);
      }
    }
    if ((!mReorderingAllowed) && (paramBoolean)) {
      mManager.moveToState(mManager.mCurState, true);
    }
  }
  
  Fragment expandOps(ArrayList<Fragment> paramArrayList, Fragment paramFragment)
  {
    int i = 0;
    for (Fragment localFragment1 = paramFragment; i < mOps.size(); localFragment1 = paramFragment)
    {
      Op localOp = (Op)mOps.get(i);
      int j;
      switch (cmd)
      {
      case 4: 
      case 5: 
      default: 
        j = i;
        paramFragment = localFragment1;
        break;
      case 8: 
        mOps.add(i, new Op(9, localFragment1));
        j = i + 1;
        paramFragment = fragment;
        break;
      case 3: 
      case 6: 
        paramArrayList.remove(fragment);
        j = i;
        paramFragment = localFragment1;
        if (fragment == localFragment1)
        {
          mOps.add(i, new Op(9, fragment));
          j = i + 1;
          paramFragment = null;
        }
        break;
      case 2: 
        Fragment localFragment2 = fragment;
        int k = mContainerId;
        int m = 0;
        j = paramArrayList.size() - 1;
        for (paramFragment = localFragment1; j >= 0; paramFragment = localFragment1)
        {
          Fragment localFragment3 = (Fragment)paramArrayList.get(j);
          int n = i;
          int i1 = m;
          localFragment1 = paramFragment;
          if (mContainerId == k) {
            if (localFragment3 == localFragment2)
            {
              i1 = 1;
              n = i;
              localFragment1 = paramFragment;
            }
            else
            {
              i1 = i;
              localFragment1 = paramFragment;
              if (localFragment3 == paramFragment)
              {
                mOps.add(i, new Op(9, localFragment3));
                i1 = i + 1;
                localFragment1 = null;
              }
              paramFragment = new Op(3, localFragment3);
              enterAnim = enterAnim;
              popEnterAnim = popEnterAnim;
              exitAnim = exitAnim;
              popExitAnim = popExitAnim;
              mOps.add(i1, paramFragment);
              paramArrayList.remove(localFragment3);
              n = i1 + 1;
              i1 = m;
            }
          }
          j--;
          i = n;
          m = i1;
        }
        if (m != 0)
        {
          mOps.remove(i);
          i--;
        }
        else
        {
          cmd = 1;
          paramArrayList.add(localFragment2);
        }
        j = i;
        break;
      case 1: 
      case 7: 
        paramArrayList.add(fragment);
        paramFragment = localFragment1;
        j = i;
      }
      i = j + 1;
    }
    return localFragment1;
  }
  
  public boolean generateOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1)
  {
    if (FragmentManagerImpl.DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Run: ");
      localStringBuilder.append(this);
      Log.v("FragmentManager", localStringBuilder.toString());
    }
    paramArrayList.add(this);
    paramArrayList1.add(Boolean.valueOf(false));
    if (mAddToBackStack) {
      mManager.addBackStackState(this);
    }
    return true;
  }
  
  public CharSequence getBreadCrumbShortTitle()
  {
    if ((mBreadCrumbShortTitleRes != 0) && (mManager.mHost != null)) {
      return mManager.mHost.getContext().getText(mBreadCrumbShortTitleRes);
    }
    return mBreadCrumbShortTitleText;
  }
  
  public int getBreadCrumbShortTitleRes()
  {
    return mBreadCrumbShortTitleRes;
  }
  
  public CharSequence getBreadCrumbTitle()
  {
    if ((mBreadCrumbTitleRes != 0) && (mManager.mHost != null)) {
      return mManager.mHost.getContext().getText(mBreadCrumbTitleRes);
    }
    return mBreadCrumbTitleText;
  }
  
  public int getBreadCrumbTitleRes()
  {
    return mBreadCrumbTitleRes;
  }
  
  public int getId()
  {
    return mIndex;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getTransition()
  {
    return mTransition;
  }
  
  public int getTransitionStyle()
  {
    return mTransitionStyle;
  }
  
  public FragmentTransaction hide(Fragment paramFragment)
  {
    addOp(new Op(4, paramFragment));
    return this;
  }
  
  boolean interactsWith(int paramInt)
  {
    int i = mOps.size();
    for (int j = 0; j < i; j++)
    {
      Op localOp = (Op)mOps.get(j);
      int k;
      if (fragment != null) {
        k = fragment.mContainerId;
      } else {
        k = 0;
      }
      if ((k != 0) && (k == paramInt)) {
        return true;
      }
    }
    return false;
  }
  
  boolean interactsWith(ArrayList<BackStackRecord> paramArrayList, int paramInt1, int paramInt2)
  {
    if (paramInt2 == paramInt1) {
      return false;
    }
    int i = mOps.size();
    int j = -1;
    int k = 0;
    while (k < i)
    {
      Object localObject = (Op)mOps.get(k);
      int m;
      if (fragment != null) {
        m = fragment.mContainerId;
      } else {
        m = 0;
      }
      int n = j;
      if (m != 0)
      {
        n = j;
        if (m != j)
        {
          j = m;
          for (int i1 = paramInt1;; i1++)
          {
            n = j;
            if (i1 >= paramInt2) {
              break;
            }
            localObject = (BackStackRecord)paramArrayList.get(i1);
            int i2 = mOps.size();
            for (n = 0; n < i2; n++)
            {
              Op localOp = (Op)mOps.get(n);
              int i3;
              if (fragment != null) {
                i3 = fragment.mContainerId;
              } else {
                i3 = 0;
              }
              if (i3 == m) {
                return true;
              }
            }
          }
        }
      }
      k++;
      j = n;
    }
    return false;
  }
  
  public boolean isAddToBackStackAllowed()
  {
    return mAllowAddToBackStack;
  }
  
  public boolean isEmpty()
  {
    return mOps.isEmpty();
  }
  
  boolean isPostponed()
  {
    for (int i = 0; i < mOps.size(); i++) {
      if (isFragmentPostponed((Op)mOps.get(i))) {
        return true;
      }
    }
    return false;
  }
  
  public FragmentTransaction remove(Fragment paramFragment)
  {
    addOp(new Op(3, paramFragment));
    return this;
  }
  
  public FragmentTransaction replace(int paramInt, Fragment paramFragment)
  {
    return replace(paramInt, paramFragment, null);
  }
  
  public FragmentTransaction replace(int paramInt, Fragment paramFragment, String paramString)
  {
    if (paramInt != 0)
    {
      doAddOp(paramInt, paramFragment, paramString, 2);
      return this;
    }
    throw new IllegalArgumentException("Must use non-zero containerViewId");
  }
  
  public FragmentTransaction runOnCommit(Runnable paramRunnable)
  {
    if (paramRunnable != null)
    {
      disallowAddToBackStack();
      if (mCommitRunnables == null) {
        mCommitRunnables = new ArrayList();
      }
      mCommitRunnables.add(paramRunnable);
      return this;
    }
    throw new IllegalArgumentException("runnable cannot be null");
  }
  
  public void runOnCommitRunnables()
  {
    if (mCommitRunnables != null)
    {
      int i = 0;
      int j = mCommitRunnables.size();
      while (i < j)
      {
        ((Runnable)mCommitRunnables.get(i)).run();
        i++;
      }
      mCommitRunnables = null;
    }
  }
  
  public FragmentTransaction setBreadCrumbShortTitle(int paramInt)
  {
    mBreadCrumbShortTitleRes = paramInt;
    mBreadCrumbShortTitleText = null;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbShortTitle(CharSequence paramCharSequence)
  {
    mBreadCrumbShortTitleRes = 0;
    mBreadCrumbShortTitleText = paramCharSequence;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbTitle(int paramInt)
  {
    mBreadCrumbTitleRes = paramInt;
    mBreadCrumbTitleText = null;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbTitle(CharSequence paramCharSequence)
  {
    mBreadCrumbTitleRes = 0;
    mBreadCrumbTitleText = paramCharSequence;
    return this;
  }
  
  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2)
  {
    return setCustomAnimations(paramInt1, paramInt2, 0, 0);
  }
  
  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mEnterAnim = paramInt1;
    mExitAnim = paramInt2;
    mPopEnterAnim = paramInt3;
    mPopExitAnim = paramInt4;
    return this;
  }
  
  void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener paramOnStartEnterTransitionListener)
  {
    for (int i = 0; i < mOps.size(); i++)
    {
      Op localOp = (Op)mOps.get(i);
      if (isFragmentPostponed(localOp)) {
        fragment.setOnStartEnterTransitionListener(paramOnStartEnterTransitionListener);
      }
    }
  }
  
  public FragmentTransaction setPrimaryNavigationFragment(Fragment paramFragment)
  {
    addOp(new Op(8, paramFragment));
    return this;
  }
  
  public FragmentTransaction setReorderingAllowed(boolean paramBoolean)
  {
    mReorderingAllowed = paramBoolean;
    return this;
  }
  
  public FragmentTransaction setTransition(int paramInt)
  {
    mTransition = paramInt;
    return this;
  }
  
  public FragmentTransaction setTransitionStyle(int paramInt)
  {
    mTransitionStyle = paramInt;
    return this;
  }
  
  public FragmentTransaction show(Fragment paramFragment)
  {
    addOp(new Op(5, paramFragment));
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("BackStackEntry{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    if (mIndex >= 0)
    {
      localStringBuilder.append(" #");
      localStringBuilder.append(mIndex);
    }
    if (mName != null)
    {
      localStringBuilder.append(" ");
      localStringBuilder.append(mName);
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  void trackAddedFragmentsInPop(ArrayList<Fragment> paramArrayList)
  {
    for (int i = 0; i < mOps.size(); i++)
    {
      Op localOp = (Op)mOps.get(i);
      int j = cmd;
      if (j != 1)
      {
        if (j != 3) {}
        switch (j)
        {
        default: 
          break;
        case 6: 
          paramArrayList.add(fragment);
          break;
        }
      }
      else
      {
        paramArrayList.remove(fragment);
      }
    }
  }
  
  static final class Op
  {
    int cmd;
    int enterAnim;
    int exitAnim;
    Fragment fragment;
    int popEnterAnim;
    int popExitAnim;
    
    Op() {}
    
    Op(int paramInt, Fragment paramFragment)
    {
      cmd = paramInt;
      fragment = paramFragment;
    }
  }
}
