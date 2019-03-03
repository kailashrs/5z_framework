package android.app;

import android.animation.Animator;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.UserHandle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Deprecated
public class Fragment
  implements ComponentCallbacks2, View.OnCreateContextMenuListener
{
  static final int ACTIVITY_CREATED = 2;
  static final int CREATED = 1;
  static final int INITIALIZING = 0;
  static final int INVALID_STATE = -1;
  static final int RESUMED = 5;
  static final int STARTED = 4;
  static final int STOPPED = 3;
  private static final Transition USE_DEFAULT_TRANSITION = new TransitionSet();
  private static final ArrayMap<String, Class<?>> sClassMap = new ArrayMap();
  boolean mAdded;
  AnimationInfo mAnimationInfo;
  Bundle mArguments;
  int mBackStackNesting;
  boolean mCalled;
  boolean mCheckedForLoaderManager;
  FragmentManagerImpl mChildFragmentManager;
  FragmentManagerNonConfig mChildNonConfig;
  ViewGroup mContainer;
  int mContainerId;
  boolean mDeferStart;
  boolean mDetached;
  int mFragmentId;
  FragmentManagerImpl mFragmentManager;
  boolean mFromLayout;
  boolean mHasMenu;
  boolean mHidden;
  boolean mHiddenChanged;
  FragmentHostCallback mHost;
  boolean mInLayout;
  int mIndex = -1;
  boolean mIsCreated;
  boolean mIsNewlyAdded;
  LayoutInflater mLayoutInflater;
  LoaderManagerImpl mLoaderManager;
  boolean mLoadersStarted;
  boolean mMenuVisible = true;
  Fragment mParentFragment;
  boolean mPerformedCreateView;
  boolean mRemoving;
  boolean mRestored;
  boolean mRetainInstance;
  boolean mRetaining;
  Bundle mSavedFragmentState;
  SparseArray<Parcelable> mSavedViewState;
  int mState = 0;
  String mTag;
  Fragment mTarget;
  int mTargetIndex = -1;
  int mTargetRequestCode;
  boolean mUserVisibleHint = true;
  View mView;
  String mWho;
  
  public Fragment() {}
  
  private void callStartTransitionListener()
  {
    OnStartEnterTransitionListener localOnStartEnterTransitionListener;
    if (mAnimationInfo == null)
    {
      localOnStartEnterTransitionListener = null;
    }
    else
    {
      mAnimationInfo.mEnterTransitionPostponed = false;
      localOnStartEnterTransitionListener = mAnimationInfo.mStartEnterTransitionListener;
      mAnimationInfo.mStartEnterTransitionListener = null;
    }
    if (localOnStartEnterTransitionListener != null) {
      localOnStartEnterTransitionListener.onStartEnterTransition();
    }
  }
  
  private AnimationInfo ensureAnimationInfo()
  {
    if (mAnimationInfo == null) {
      mAnimationInfo = new AnimationInfo();
    }
    return mAnimationInfo;
  }
  
  public static Fragment instantiate(Context paramContext, String paramString)
  {
    return instantiate(paramContext, paramString, null);
  }
  
  public static Fragment instantiate(Context paramContext, String paramString, Bundle paramBundle)
  {
    try
    {
      Class localClass = (Class)sClassMap.get(paramString);
      Object localObject = localClass;
      if (localClass == null)
      {
        localObject = paramContext.getClassLoader().loadClass(paramString);
        if (Fragment.class.isAssignableFrom((Class)localObject))
        {
          sClassMap.put(paramString, localObject);
        }
        else
        {
          paramContext = new android/app/Fragment$InstantiationException;
          paramBundle = new java/lang/StringBuilder;
          paramBundle.<init>();
          paramBundle.append("Trying to instantiate a class ");
          paramBundle.append(paramString);
          paramBundle.append(" that is not a Fragment");
          localObject = paramBundle.toString();
          paramBundle = new java/lang/ClassCastException;
          paramBundle.<init>();
          paramContext.<init>((String)localObject, paramBundle);
          throw paramContext;
        }
      }
      paramContext = (Fragment)((Class)localObject).getConstructor(new Class[0]).newInstance(new Object[0]);
      if (paramBundle != null)
      {
        paramBundle.setClassLoader(paramContext.getClass().getClassLoader());
        paramContext.setArguments(paramBundle);
      }
      return paramContext;
    }
    catch (InvocationTargetException paramContext)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to instantiate fragment ");
      paramBundle.append(paramString);
      paramBundle.append(": calling Fragment constructor caused an exception");
      throw new InstantiationException(paramBundle.toString(), paramContext);
    }
    catch (NoSuchMethodException paramContext)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to instantiate fragment ");
      paramBundle.append(paramString);
      paramBundle.append(": could not find Fragment constructor");
      throw new InstantiationException(paramBundle.toString(), paramContext);
    }
    catch (IllegalAccessException paramContext)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to instantiate fragment ");
      paramBundle.append(paramString);
      paramBundle.append(": make sure class name exists, is public, and has an empty constructor that is public");
      throw new InstantiationException(paramBundle.toString(), paramContext);
    }
    catch (InstantiationException paramBundle)
    {
      paramContext = new StringBuilder();
      paramContext.append("Unable to instantiate fragment ");
      paramContext.append(paramString);
      paramContext.append(": make sure class name exists, is public, and has an empty constructor that is public");
      throw new InstantiationException(paramContext.toString(), paramBundle);
    }
    catch (ClassNotFoundException paramContext)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("Unable to instantiate fragment ");
      paramBundle.append(paramString);
      paramBundle.append(": make sure class name exists, is public, and has an empty constructor that is public");
      throw new InstantiationException(paramBundle.toString(), paramContext);
    }
  }
  
  private static Transition loadTransition(Context paramContext, TypedArray paramTypedArray, Transition paramTransition1, Transition paramTransition2, int paramInt)
  {
    if (paramTransition1 != paramTransition2) {
      return paramTransition1;
    }
    paramInt = paramTypedArray.getResourceId(paramInt, 0);
    paramTypedArray = paramTransition2;
    if (paramInt != 0)
    {
      paramTypedArray = paramTransition2;
      if (paramInt != 17760256)
      {
        paramContext = TransitionInflater.from(paramContext).inflateTransition(paramInt);
        paramTypedArray = paramContext;
        if ((paramContext instanceof TransitionSet))
        {
          paramTypedArray = paramContext;
          if (((TransitionSet)paramContext).getTransitionCount() == 0) {
            paramTypedArray = null;
          }
        }
      }
    }
    return paramTypedArray;
  }
  
  private boolean shouldChangeTransition(Transition paramTransition1, Transition paramTransition2)
  {
    boolean bool = true;
    if (paramTransition1 == paramTransition2)
    {
      if (mAnimationInfo == null) {
        bool = false;
      }
      return bool;
    }
    return true;
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mFragmentId=#");
    paramPrintWriter.print(Integer.toHexString(mFragmentId));
    paramPrintWriter.print(" mContainerId=#");
    paramPrintWriter.print(Integer.toHexString(mContainerId));
    paramPrintWriter.print(" mTag=");
    paramPrintWriter.println(mTag);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mState=");
    paramPrintWriter.print(mState);
    paramPrintWriter.print(" mIndex=");
    paramPrintWriter.print(mIndex);
    paramPrintWriter.print(" mWho=");
    paramPrintWriter.print(mWho);
    paramPrintWriter.print(" mBackStackNesting=");
    paramPrintWriter.println(mBackStackNesting);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mAdded=");
    paramPrintWriter.print(mAdded);
    paramPrintWriter.print(" mRemoving=");
    paramPrintWriter.print(mRemoving);
    paramPrintWriter.print(" mFromLayout=");
    paramPrintWriter.print(mFromLayout);
    paramPrintWriter.print(" mInLayout=");
    paramPrintWriter.println(mInLayout);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mHidden=");
    paramPrintWriter.print(mHidden);
    paramPrintWriter.print(" mDetached=");
    paramPrintWriter.print(mDetached);
    paramPrintWriter.print(" mMenuVisible=");
    paramPrintWriter.print(mMenuVisible);
    paramPrintWriter.print(" mHasMenu=");
    paramPrintWriter.println(mHasMenu);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mRetainInstance=");
    paramPrintWriter.print(mRetainInstance);
    paramPrintWriter.print(" mRetaining=");
    paramPrintWriter.print(mRetaining);
    paramPrintWriter.print(" mUserVisibleHint=");
    paramPrintWriter.println(mUserVisibleHint);
    if (mFragmentManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mFragmentManager=");
      paramPrintWriter.println(mFragmentManager);
    }
    if (mHost != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mHost=");
      paramPrintWriter.println(mHost);
    }
    if (mParentFragment != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mParentFragment=");
      paramPrintWriter.println(mParentFragment);
    }
    if (mArguments != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mArguments=");
      paramPrintWriter.println(mArguments);
    }
    if (mSavedFragmentState != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedFragmentState=");
      paramPrintWriter.println(mSavedFragmentState);
    }
    if (mSavedViewState != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedViewState=");
      paramPrintWriter.println(mSavedViewState);
    }
    if (mTarget != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTarget=");
      paramPrintWriter.print(mTarget);
      paramPrintWriter.print(" mTargetRequestCode=");
      paramPrintWriter.println(mTargetRequestCode);
    }
    if (getNextAnim() != 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mNextAnim=");
      paramPrintWriter.println(getNextAnim());
    }
    if (mContainer != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mContainer=");
      paramPrintWriter.println(mContainer);
    }
    if (mView != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mView=");
      paramPrintWriter.println(mView);
    }
    if (getAnimatingAway() != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mAnimatingAway=");
      paramPrintWriter.println(getAnimatingAway());
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStateAfterAnimating=");
      paramPrintWriter.println(getStateAfterAnimating());
    }
    Object localObject1;
    Object localObject2;
    if (mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Loader Manager:");
      localObject1 = mLoaderManager;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  ");
      ((LoaderManagerImpl)localObject1).dump(((StringBuilder)localObject2).toString(), paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    if (mChildFragmentManager != null)
    {
      paramPrintWriter.print(paramString);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Child ");
      ((StringBuilder)localObject2).append(mChildFragmentManager);
      ((StringBuilder)localObject2).append(":");
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = mChildFragmentManager;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  ");
      ((FragmentManagerImpl)localObject2).dump(((StringBuilder)localObject1).toString(), paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  public final boolean equals(Object paramObject)
  {
    return super.equals(paramObject);
  }
  
  Fragment findFragmentByWho(String paramString)
  {
    if (paramString.equals(mWho)) {
      return this;
    }
    if (mChildFragmentManager != null) {
      return mChildFragmentManager.findFragmentByWho(paramString);
    }
    return null;
  }
  
  public final Activity getActivity()
  {
    Activity localActivity;
    if (mHost == null) {
      localActivity = null;
    } else {
      localActivity = mHost.getActivity();
    }
    return localActivity;
  }
  
  public boolean getAllowEnterTransitionOverlap()
  {
    boolean bool;
    if ((mAnimationInfo != null) && (mAnimationInfo.mAllowEnterTransitionOverlap != null)) {
      bool = mAnimationInfo.mAllowEnterTransitionOverlap.booleanValue();
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean getAllowReturnTransitionOverlap()
  {
    boolean bool;
    if ((mAnimationInfo != null) && (mAnimationInfo.mAllowReturnTransitionOverlap != null)) {
      bool = mAnimationInfo.mAllowReturnTransitionOverlap.booleanValue();
    } else {
      bool = true;
    }
    return bool;
  }
  
  Animator getAnimatingAway()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    return mAnimationInfo.mAnimatingAway;
  }
  
  public final Bundle getArguments()
  {
    return mArguments;
  }
  
  public final FragmentManager getChildFragmentManager()
  {
    if (mChildFragmentManager == null)
    {
      instantiateChildFragmentManager();
      if (mState >= 5) {
        mChildFragmentManager.dispatchResume();
      } else if (mState >= 4) {
        mChildFragmentManager.dispatchStart();
      } else if (mState >= 2) {
        mChildFragmentManager.dispatchActivityCreated();
      } else if (mState >= 1) {
        mChildFragmentManager.dispatchCreate();
      }
    }
    return mChildFragmentManager;
  }
  
  public Context getContext()
  {
    Context localContext;
    if (mHost == null) {
      localContext = null;
    } else {
      localContext = mHost.getContext();
    }
    return localContext;
  }
  
  public Transition getEnterTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    return mAnimationInfo.mEnterTransition;
  }
  
  SharedElementCallback getEnterTransitionCallback()
  {
    if (mAnimationInfo == null) {
      return SharedElementCallback.NULL_CALLBACK;
    }
    return mAnimationInfo.mEnterTransitionCallback;
  }
  
  public Transition getExitTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    return mAnimationInfo.mExitTransition;
  }
  
  SharedElementCallback getExitTransitionCallback()
  {
    if (mAnimationInfo == null) {
      return SharedElementCallback.NULL_CALLBACK;
    }
    return mAnimationInfo.mExitTransitionCallback;
  }
  
  public final FragmentManager getFragmentManager()
  {
    return mFragmentManager;
  }
  
  public final Object getHost()
  {
    Object localObject;
    if (mHost == null) {
      localObject = null;
    } else {
      localObject = mHost.onGetHost();
    }
    return localObject;
  }
  
  public final int getId()
  {
    return mFragmentId;
  }
  
  public final LayoutInflater getLayoutInflater()
  {
    if (mLayoutInflater == null) {
      return performGetLayoutInflater(null);
    }
    return mLayoutInflater;
  }
  
  @Deprecated
  public LoaderManager getLoaderManager()
  {
    if (mLoaderManager != null) {
      return mLoaderManager;
    }
    if (mHost != null)
    {
      mCheckedForLoaderManager = true;
      mLoaderManager = mHost.getLoaderManager(mWho, mLoadersStarted, true);
      return mLoaderManager;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" not attached to Activity");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  int getNextAnim()
  {
    if (mAnimationInfo == null) {
      return 0;
    }
    return mAnimationInfo.mNextAnim;
  }
  
  int getNextTransition()
  {
    if (mAnimationInfo == null) {
      return 0;
    }
    return mAnimationInfo.mNextTransition;
  }
  
  int getNextTransitionStyle()
  {
    if (mAnimationInfo == null) {
      return 0;
    }
    return mAnimationInfo.mNextTransitionStyle;
  }
  
  public final Fragment getParentFragment()
  {
    return mParentFragment;
  }
  
  public Transition getReenterTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    Transition localTransition;
    if (mAnimationInfo.mReenterTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getExitTransition();
    } else {
      localTransition = mAnimationInfo.mReenterTransition;
    }
    return localTransition;
  }
  
  public final Resources getResources()
  {
    if (mHost != null) {
      return mHost.getContext().getResources();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" not attached to Activity");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public final boolean getRetainInstance()
  {
    return mRetainInstance;
  }
  
  public Transition getReturnTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    Transition localTransition;
    if (mAnimationInfo.mReturnTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getEnterTransition();
    } else {
      localTransition = mAnimationInfo.mReturnTransition;
    }
    return localTransition;
  }
  
  public Transition getSharedElementEnterTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    return mAnimationInfo.mSharedElementEnterTransition;
  }
  
  public Transition getSharedElementReturnTransition()
  {
    if (mAnimationInfo == null) {
      return null;
    }
    Transition localTransition;
    if (mAnimationInfo.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getSharedElementEnterTransition();
    } else {
      localTransition = mAnimationInfo.mSharedElementReturnTransition;
    }
    return localTransition;
  }
  
  int getStateAfterAnimating()
  {
    if (mAnimationInfo == null) {
      return 0;
    }
    return mAnimationInfo.mStateAfterAnimating;
  }
  
  public final String getString(int paramInt)
  {
    return getResources().getString(paramInt);
  }
  
  public final String getString(int paramInt, Object... paramVarArgs)
  {
    return getResources().getString(paramInt, paramVarArgs);
  }
  
  public final String getTag()
  {
    return mTag;
  }
  
  public final Fragment getTargetFragment()
  {
    return mTarget;
  }
  
  public final int getTargetRequestCode()
  {
    return mTargetRequestCode;
  }
  
  public final CharSequence getText(int paramInt)
  {
    return getResources().getText(paramInt);
  }
  
  public boolean getUserVisibleHint()
  {
    return mUserVisibleHint;
  }
  
  public View getView()
  {
    return mView;
  }
  
  public final int hashCode()
  {
    return super.hashCode();
  }
  
  void initState()
  {
    mIndex = -1;
    mWho = null;
    mAdded = false;
    mRemoving = false;
    mFromLayout = false;
    mInLayout = false;
    mRestored = false;
    mBackStackNesting = 0;
    mFragmentManager = null;
    mChildFragmentManager = null;
    mHost = null;
    mFragmentId = 0;
    mContainerId = 0;
    mTag = null;
    mHidden = false;
    mDetached = false;
    mRetaining = false;
    mLoaderManager = null;
    mLoadersStarted = false;
    mCheckedForLoaderManager = false;
  }
  
  void instantiateChildFragmentManager()
  {
    mChildFragmentManager = new FragmentManagerImpl();
    mChildFragmentManager.attachController(mHost, new FragmentContainer()
    {
      public <T extends View> T onFindViewById(int paramAnonymousInt)
      {
        if (mView != null) {
          return mView.findViewById(paramAnonymousInt);
        }
        throw new IllegalStateException("Fragment does not have a view");
      }
      
      public boolean onHasView()
      {
        boolean bool;
        if (mView != null) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
    }, this);
  }
  
  public final boolean isAdded()
  {
    boolean bool;
    if ((mHost != null) && (mAdded)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isDetached()
  {
    return mDetached;
  }
  
  public final boolean isHidden()
  {
    return mHidden;
  }
  
  boolean isHideReplaced()
  {
    if (mAnimationInfo == null) {
      return false;
    }
    return mAnimationInfo.mIsHideReplaced;
  }
  
  final boolean isInBackStack()
  {
    boolean bool;
    if (mBackStackNesting > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isInLayout()
  {
    return mInLayout;
  }
  
  boolean isPostponed()
  {
    if (mAnimationInfo == null) {
      return false;
    }
    return mAnimationInfo.mEnterTransitionPostponed;
  }
  
  public final boolean isRemoving()
  {
    return mRemoving;
  }
  
  public final boolean isResumed()
  {
    boolean bool;
    if (mState >= 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isStateSaved()
  {
    if (mFragmentManager == null) {
      return false;
    }
    return mFragmentManager.isStateSaved();
  }
  
  public final boolean isVisible()
  {
    boolean bool;
    if ((isAdded()) && (!isHidden()) && (mView != null) && (mView.getWindowToken() != null) && (mView.getVisibility() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void noteStateNotSaved()
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.noteStateNotSaved();
    }
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    mCalled = true;
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {}
  
  @Deprecated
  public void onAttach(Activity paramActivity)
  {
    mCalled = true;
  }
  
  public void onAttach(Context paramContext)
  {
    mCalled = true;
    if (mHost == null) {
      paramContext = null;
    } else {
      paramContext = mHost.getActivity();
    }
    if (paramContext != null)
    {
      mCalled = false;
      onAttach(paramContext);
    }
  }
  
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    mCalled = true;
  }
  
  public boolean onContextItemSelected(MenuItem paramMenuItem)
  {
    return false;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    mCalled = true;
    Context localContext = getContext();
    int i;
    if (localContext != null) {
      i = getApplicationInfotargetSdkVersion;
    } else {
      i = 0;
    }
    if (i >= 24)
    {
      restoreChildFragmentState(paramBundle, true);
      if ((mChildFragmentManager != null) && (!mChildFragmentManager.isStateAtLeast(1))) {
        mChildFragmentManager.dispatchCreate();
      }
    }
  }
  
  public Animator onCreateAnimator(int paramInt1, boolean paramBoolean, int paramInt2)
  {
    return null;
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    getActivity().onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {}
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return null;
  }
  
  public void onDestroy()
  {
    mCalled = true;
    if (!mCheckedForLoaderManager)
    {
      mCheckedForLoaderManager = true;
      mLoaderManager = mHost.getLoaderManager(mWho, mLoadersStarted, false);
    }
    if (mLoaderManager != null) {
      mLoaderManager.doDestroy();
    }
  }
  
  public void onDestroyOptionsMenu() {}
  
  public void onDestroyView()
  {
    mCalled = true;
  }
  
  public void onDetach()
  {
    mCalled = true;
  }
  
  public LayoutInflater onGetLayoutInflater(Bundle paramBundle)
  {
    if (mHost != null)
    {
      paramBundle = mHost.onGetLayoutInflater();
      if (mHost.onUseFragmentManagerInflaterFactory())
      {
        getChildFragmentManager();
        paramBundle.setPrivateFactory(mChildFragmentManager.getLayoutInflaterFactory());
      }
      return paramBundle;
    }
    throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
  }
  
  public void onHiddenChanged(boolean paramBoolean) {}
  
  @Deprecated
  public void onInflate(Activity paramActivity, AttributeSet paramAttributeSet, Bundle paramBundle)
  {
    mCalled = true;
  }
  
  public void onInflate(Context paramContext, AttributeSet paramAttributeSet, Bundle paramBundle)
  {
    onInflate(paramAttributeSet, paramBundle);
    mCalled = true;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Fragment);
    Transition localTransition = getEnterTransition();
    Object localObject = null;
    setEnterTransition(loadTransition(paramContext, localTypedArray, localTransition, null, 4));
    setReturnTransition(loadTransition(paramContext, localTypedArray, getReturnTransition(), USE_DEFAULT_TRANSITION, 6));
    setExitTransition(loadTransition(paramContext, localTypedArray, getExitTransition(), null, 3));
    setReenterTransition(loadTransition(paramContext, localTypedArray, getReenterTransition(), USE_DEFAULT_TRANSITION, 8));
    setSharedElementEnterTransition(loadTransition(paramContext, localTypedArray, getSharedElementEnterTransition(), null, 5));
    setSharedElementReturnTransition(loadTransition(paramContext, localTypedArray, getSharedElementReturnTransition(), USE_DEFAULT_TRANSITION, 7));
    int i;
    int j;
    if (mAnimationInfo == null)
    {
      i = 0;
      j = 0;
    }
    else
    {
      if (mAnimationInfo.mAllowEnterTransitionOverlap != null) {
        i = 1;
      } else {
        i = 0;
      }
      if (mAnimationInfo.mAllowReturnTransitionOverlap != null) {
        j = 1;
      } else {
        j = 0;
      }
    }
    if (i == 0) {
      setAllowEnterTransitionOverlap(localTypedArray.getBoolean(9, true));
    }
    if (j == 0) {
      setAllowReturnTransitionOverlap(localTypedArray.getBoolean(10, true));
    }
    localTypedArray.recycle();
    if (mHost == null) {
      paramContext = localObject;
    } else {
      paramContext = mHost.getActivity();
    }
    if (paramContext != null)
    {
      mCalled = false;
      onInflate(paramContext, paramAttributeSet, paramBundle);
    }
  }
  
  @Deprecated
  public void onInflate(AttributeSet paramAttributeSet, Bundle paramBundle)
  {
    mCalled = true;
  }
  
  public void onLowMemory()
  {
    mCalled = true;
  }
  
  @Deprecated
  public void onMultiWindowModeChanged(boolean paramBoolean) {}
  
  public void onMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onMultiWindowModeChanged(paramBoolean);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    return false;
  }
  
  public void onOptionsMenuClosed(Menu paramMenu) {}
  
  public void onPause()
  {
    mCalled = true;
  }
  
  @Deprecated
  public void onPictureInPictureModeChanged(boolean paramBoolean) {}
  
  public void onPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onPictureInPictureModeChanged(paramBoolean);
  }
  
  public void onPrepareOptionsMenu(Menu paramMenu) {}
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {}
  
  public void onResume()
  {
    mCalled = true;
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStart()
  {
    mCalled = true;
    if (!mLoadersStarted)
    {
      mLoadersStarted = true;
      if (!mCheckedForLoaderManager)
      {
        mCheckedForLoaderManager = true;
        mLoaderManager = mHost.getLoaderManager(mWho, mLoadersStarted, false);
      }
      else if (mLoaderManager != null)
      {
        mLoaderManager.doStart();
      }
    }
  }
  
  public void onStop()
  {
    mCalled = true;
  }
  
  public void onTrimMemory(int paramInt)
  {
    mCalled = true;
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle) {}
  
  public void onViewStateRestored(Bundle paramBundle)
  {
    mCalled = true;
  }
  
  void performActivityCreated(Bundle paramBundle)
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.noteStateNotSaved();
    }
    mState = 2;
    mCalled = false;
    onActivityCreated(paramBundle);
    if (mCalled)
    {
      if (mChildFragmentManager != null) {
        mChildFragmentManager.dispatchActivityCreated();
      }
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Fragment ");
    paramBundle.append(this);
    paramBundle.append(" did not call through to super.onActivityCreated()");
    throw new SuperNotCalledException(paramBundle.toString());
  }
  
  void performConfigurationChanged(Configuration paramConfiguration)
  {
    onConfigurationChanged(paramConfiguration);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchConfigurationChanged(paramConfiguration);
    }
  }
  
  boolean performContextItemSelected(MenuItem paramMenuItem)
  {
    if (!mHidden)
    {
      if (onContextItemSelected(paramMenuItem)) {
        return true;
      }
      if ((mChildFragmentManager != null) && (mChildFragmentManager.dispatchContextItemSelected(paramMenuItem))) {
        return true;
      }
    }
    return false;
  }
  
  void performCreate(Bundle paramBundle)
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.noteStateNotSaved();
    }
    mState = 1;
    mCalled = false;
    onCreate(paramBundle);
    mIsCreated = true;
    if (mCalled)
    {
      Context localContext = getContext();
      int i;
      if (localContext != null) {
        i = getApplicationInfotargetSdkVersion;
      } else {
        i = 0;
      }
      if (i < 24) {
        restoreChildFragmentState(paramBundle, false);
      }
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Fragment ");
    paramBundle.append(this);
    paramBundle.append(" did not call through to super.onCreate()");
    throw new SuperNotCalledException(paramBundle.toString());
  }
  
  boolean performCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!mHidden)
    {
      boolean bool3 = bool2;
      if (mHasMenu)
      {
        bool3 = bool2;
        if (mMenuVisible)
        {
          bool3 = true;
          onCreateOptionsMenu(paramMenu, paramMenuInflater);
        }
      }
      bool1 = bool3;
      if (mChildFragmentManager != null) {
        bool1 = bool3 | mChildFragmentManager.dispatchCreateOptionsMenu(paramMenu, paramMenuInflater);
      }
    }
    return bool1;
  }
  
  View performCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.noteStateNotSaved();
    }
    mPerformedCreateView = true;
    return onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }
  
  void performDestroy()
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchDestroy();
    }
    mState = 0;
    mCalled = false;
    mIsCreated = false;
    onDestroy();
    if (mCalled)
    {
      mChildFragmentManager = null;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onDestroy()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  void performDestroyView()
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchDestroyView();
    }
    mState = 1;
    mCalled = false;
    onDestroyView();
    if (mCalled)
    {
      if (mLoaderManager != null) {
        mLoaderManager.doReportNextStart();
      }
      mPerformedCreateView = false;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onDestroyView()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  void performDetach()
  {
    mCalled = false;
    onDetach();
    mLayoutInflater = null;
    if (mCalled)
    {
      if (mChildFragmentManager != null) {
        if (mRetaining)
        {
          mChildFragmentManager.dispatchDestroy();
          mChildFragmentManager = null;
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Child FragmentManager of ");
          localStringBuilder.append(this);
          localStringBuilder.append(" was not  destroyed and this fragment is not retaining instance");
          throw new IllegalStateException(localStringBuilder.toString());
        }
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onDetach()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  LayoutInflater performGetLayoutInflater(Bundle paramBundle)
  {
    mLayoutInflater = onGetLayoutInflater(paramBundle);
    return mLayoutInflater;
  }
  
  void performLowMemory()
  {
    onLowMemory();
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchLowMemory();
    }
  }
  
  @Deprecated
  void performMultiWindowModeChanged(boolean paramBoolean)
  {
    onMultiWindowModeChanged(paramBoolean);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchMultiWindowModeChanged(paramBoolean);
    }
  }
  
  void performMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onMultiWindowModeChanged(paramBoolean, paramConfiguration);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchMultiWindowModeChanged(paramBoolean, paramConfiguration);
    }
  }
  
  boolean performOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (!mHidden)
    {
      if ((mHasMenu) && (mMenuVisible) && (onOptionsItemSelected(paramMenuItem))) {
        return true;
      }
      if ((mChildFragmentManager != null) && (mChildFragmentManager.dispatchOptionsItemSelected(paramMenuItem))) {
        return true;
      }
    }
    return false;
  }
  
  void performOptionsMenuClosed(Menu paramMenu)
  {
    if (!mHidden)
    {
      if ((mHasMenu) && (mMenuVisible)) {
        onOptionsMenuClosed(paramMenu);
      }
      if (mChildFragmentManager != null) {
        mChildFragmentManager.dispatchOptionsMenuClosed(paramMenu);
      }
    }
  }
  
  void performPause()
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchPause();
    }
    mState = 4;
    mCalled = false;
    onPause();
    if (mCalled) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onPause()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  @Deprecated
  void performPictureInPictureModeChanged(boolean paramBoolean)
  {
    onPictureInPictureModeChanged(paramBoolean);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchPictureInPictureModeChanged(paramBoolean);
    }
  }
  
  void performPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onPictureInPictureModeChanged(paramBoolean, paramConfiguration);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchPictureInPictureModeChanged(paramBoolean, paramConfiguration);
    }
  }
  
  boolean performPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!mHidden)
    {
      boolean bool3 = bool2;
      if (mHasMenu)
      {
        bool3 = bool2;
        if (mMenuVisible)
        {
          bool3 = true;
          onPrepareOptionsMenu(paramMenu);
        }
      }
      bool1 = bool3;
      if (mChildFragmentManager != null) {
        bool1 = bool3 | mChildFragmentManager.dispatchPrepareOptionsMenu(paramMenu);
      }
    }
    return bool1;
  }
  
  void performResume()
  {
    if (mChildFragmentManager != null)
    {
      mChildFragmentManager.noteStateNotSaved();
      mChildFragmentManager.execPendingActions();
    }
    mState = 5;
    mCalled = false;
    onResume();
    if (mCalled)
    {
      if (mChildFragmentManager != null)
      {
        mChildFragmentManager.dispatchResume();
        mChildFragmentManager.execPendingActions();
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onResume()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  void performSaveInstanceState(Bundle paramBundle)
  {
    onSaveInstanceState(paramBundle);
    if (mChildFragmentManager != null)
    {
      Parcelable localParcelable = mChildFragmentManager.saveAllState();
      if (localParcelable != null) {
        paramBundle.putParcelable("android:fragments", localParcelable);
      }
    }
  }
  
  void performStart()
  {
    if (mChildFragmentManager != null)
    {
      mChildFragmentManager.noteStateNotSaved();
      mChildFragmentManager.execPendingActions();
    }
    mState = 4;
    mCalled = false;
    onStart();
    if (mCalled)
    {
      if (mChildFragmentManager != null) {
        mChildFragmentManager.dispatchStart();
      }
      if (mLoaderManager != null) {
        mLoaderManager.doReportStart();
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onStart()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  void performStop()
  {
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchStop();
    }
    mState = 3;
    mCalled = false;
    onStop();
    if (mCalled)
    {
      if (mLoadersStarted)
      {
        mLoadersStarted = false;
        if (!mCheckedForLoaderManager)
        {
          mCheckedForLoaderManager = true;
          mLoaderManager = mHost.getLoaderManager(mWho, mLoadersStarted, false);
        }
        if (mLoaderManager != null) {
          if (mHost.getRetainLoaders()) {
            mLoaderManager.doRetain();
          } else {
            mLoaderManager.doStop();
          }
        }
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Fragment ");
    localStringBuilder.append(this);
    localStringBuilder.append(" did not call through to super.onStop()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  void performTrimMemory(int paramInt)
  {
    onTrimMemory(paramInt);
    if (mChildFragmentManager != null) {
      mChildFragmentManager.dispatchTrimMemory(paramInt);
    }
  }
  
  public void postponeEnterTransition()
  {
    ensureAnimationInfomEnterTransitionPostponed = true;
  }
  
  public void registerForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(this);
  }
  
  public final void requestPermissions(String[] paramArrayOfString, int paramInt)
  {
    if (mHost != null)
    {
      mHost.onRequestPermissionsFromFragment(this, paramArrayOfString, paramInt);
      return;
    }
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Fragment ");
    paramArrayOfString.append(this);
    paramArrayOfString.append(" not attached to Activity");
    throw new IllegalStateException(paramArrayOfString.toString());
  }
  
  void restoreChildFragmentState(Bundle paramBundle, boolean paramBoolean)
  {
    if (paramBundle != null)
    {
      Parcelable localParcelable = paramBundle.getParcelable("android:fragments");
      if (localParcelable != null)
      {
        if (mChildFragmentManager == null) {
          instantiateChildFragmentManager();
        }
        FragmentManagerImpl localFragmentManagerImpl = mChildFragmentManager;
        if (paramBoolean) {
          paramBundle = mChildNonConfig;
        } else {
          paramBundle = null;
        }
        localFragmentManagerImpl.restoreAllState(localParcelable, paramBundle);
        mChildNonConfig = null;
        mChildFragmentManager.dispatchCreate();
      }
    }
  }
  
  final void restoreViewState(Bundle paramBundle)
  {
    if (mSavedViewState != null)
    {
      mView.restoreHierarchyState(mSavedViewState);
      mSavedViewState = null;
    }
    mCalled = false;
    onViewStateRestored(paramBundle);
    if (mCalled) {
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Fragment ");
    paramBundle.append(this);
    paramBundle.append(" did not call through to super.onViewStateRestored()");
    throw new SuperNotCalledException(paramBundle.toString());
  }
  
  public void setAllowEnterTransitionOverlap(boolean paramBoolean)
  {
    AnimationInfo.access$002(ensureAnimationInfo(), Boolean.valueOf(paramBoolean));
  }
  
  public void setAllowReturnTransitionOverlap(boolean paramBoolean)
  {
    AnimationInfo.access$102(ensureAnimationInfo(), Boolean.valueOf(paramBoolean));
  }
  
  void setAnimatingAway(Animator paramAnimator)
  {
    ensureAnimationInfomAnimatingAway = paramAnimator;
  }
  
  public void setArguments(Bundle paramBundle)
  {
    if ((mIndex >= 0) && (isStateSaved())) {
      throw new IllegalStateException("Fragment already active");
    }
    mArguments = paramBundle;
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback localSharedElementCallback = paramSharedElementCallback;
    if (paramSharedElementCallback == null)
    {
      if (mAnimationInfo == null) {
        return;
      }
      localSharedElementCallback = SharedElementCallback.NULL_CALLBACK;
    }
    ensureAnimationInfomEnterTransitionCallback = localSharedElementCallback;
  }
  
  public void setEnterTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, null)) {
      AnimationInfo.access$202(ensureAnimationInfo(), paramTransition);
    }
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback localSharedElementCallback = paramSharedElementCallback;
    if (paramSharedElementCallback == null)
    {
      if (mAnimationInfo == null) {
        return;
      }
      localSharedElementCallback = SharedElementCallback.NULL_CALLBACK;
    }
    ensureAnimationInfomExitTransitionCallback = localSharedElementCallback;
  }
  
  public void setExitTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, null)) {
      AnimationInfo.access$402(ensureAnimationInfo(), paramTransition);
    }
  }
  
  public void setHasOptionsMenu(boolean paramBoolean)
  {
    if (mHasMenu != paramBoolean)
    {
      mHasMenu = paramBoolean;
      if ((isAdded()) && (!isHidden())) {
        mFragmentManager.invalidateOptionsMenu();
      }
    }
  }
  
  void setHideReplaced(boolean paramBoolean)
  {
    ensureAnimationInfomIsHideReplaced = paramBoolean;
  }
  
  final void setIndex(int paramInt, Fragment paramFragment)
  {
    mIndex = paramInt;
    if (paramFragment != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mWho);
      localStringBuilder.append(":");
      localStringBuilder.append(mIndex);
      mWho = localStringBuilder.toString();
    }
    else
    {
      paramFragment = new StringBuilder();
      paramFragment.append("android:fragment:");
      paramFragment.append(mIndex);
      mWho = paramFragment.toString();
    }
  }
  
  public void setInitialSavedState(SavedState paramSavedState)
  {
    if (mIndex < 0)
    {
      if ((paramSavedState != null) && (mState != null)) {
        paramSavedState = mState;
      } else {
        paramSavedState = null;
      }
      mSavedFragmentState = paramSavedState;
      return;
    }
    throw new IllegalStateException("Fragment already active");
  }
  
  public void setMenuVisibility(boolean paramBoolean)
  {
    if (mMenuVisible != paramBoolean)
    {
      mMenuVisible = paramBoolean;
      if ((mHasMenu) && (isAdded()) && (!isHidden())) {
        mFragmentManager.invalidateOptionsMenu();
      }
    }
  }
  
  void setNextAnim(int paramInt)
  {
    if ((mAnimationInfo == null) && (paramInt == 0)) {
      return;
    }
    ensureAnimationInfomNextAnim = paramInt;
  }
  
  void setNextTransition(int paramInt1, int paramInt2)
  {
    if ((mAnimationInfo == null) && (paramInt1 == 0) && (paramInt2 == 0)) {
      return;
    }
    ensureAnimationInfo();
    mAnimationInfo.mNextTransition = paramInt1;
    mAnimationInfo.mNextTransitionStyle = paramInt2;
  }
  
  void setOnStartEnterTransitionListener(OnStartEnterTransitionListener paramOnStartEnterTransitionListener)
  {
    ensureAnimationInfo();
    if (paramOnStartEnterTransitionListener == mAnimationInfo.mStartEnterTransitionListener) {
      return;
    }
    if ((paramOnStartEnterTransitionListener != null) && (mAnimationInfo.mStartEnterTransitionListener != null))
    {
      paramOnStartEnterTransitionListener = new StringBuilder();
      paramOnStartEnterTransitionListener.append("Trying to set a replacement startPostponedEnterTransition on ");
      paramOnStartEnterTransitionListener.append(this);
      throw new IllegalStateException(paramOnStartEnterTransitionListener.toString());
    }
    if (mAnimationInfo.mEnterTransitionPostponed) {
      mAnimationInfo.mStartEnterTransitionListener = paramOnStartEnterTransitionListener;
    }
    if (paramOnStartEnterTransitionListener != null) {
      paramOnStartEnterTransitionListener.startListening();
    }
  }
  
  public void setReenterTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, USE_DEFAULT_TRANSITION)) {
      AnimationInfo.access$502(ensureAnimationInfo(), paramTransition);
    }
  }
  
  public void setRetainInstance(boolean paramBoolean)
  {
    mRetainInstance = paramBoolean;
  }
  
  public void setReturnTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, USE_DEFAULT_TRANSITION)) {
      AnimationInfo.access$302(ensureAnimationInfo(), paramTransition);
    }
  }
  
  public void setSharedElementEnterTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, null)) {
      AnimationInfo.access$602(ensureAnimationInfo(), paramTransition);
    }
  }
  
  public void setSharedElementReturnTransition(Transition paramTransition)
  {
    if (shouldChangeTransition(paramTransition, USE_DEFAULT_TRANSITION)) {
      AnimationInfo.access$702(ensureAnimationInfo(), paramTransition);
    }
  }
  
  void setStateAfterAnimating(int paramInt)
  {
    ensureAnimationInfomStateAfterAnimating = paramInt;
  }
  
  public void setTargetFragment(Fragment paramFragment, int paramInt)
  {
    FragmentManager localFragmentManager = getFragmentManager();
    if (paramFragment != null) {
      localObject = paramFragment.getFragmentManager();
    } else {
      localObject = null;
    }
    if ((localFragmentManager != null) && (localObject != null) && (localFragmentManager != localObject))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Fragment ");
      ((StringBuilder)localObject).append(paramFragment);
      ((StringBuilder)localObject).append(" must share the same FragmentManager to be set as a target fragment");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = paramFragment;
    while (localObject != null) {
      if (localObject != this)
      {
        localObject = ((Fragment)localObject).getTargetFragment();
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Setting ");
        ((StringBuilder)localObject).append(paramFragment);
        ((StringBuilder)localObject).append(" as the target of ");
        ((StringBuilder)localObject).append(this);
        ((StringBuilder)localObject).append(" would create a target cycle");
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
    }
    mTarget = paramFragment;
    mTargetRequestCode = paramInt;
  }
  
  public void setUserVisibleHint(boolean paramBoolean)
  {
    int i = 0;
    Context localContext1 = getContext();
    Context localContext2 = localContext1;
    if (mFragmentManager != null)
    {
      localContext2 = localContext1;
      if (mFragmentManager.mHost != null) {
        localContext2 = mFragmentManager.mHost.getContext();
      }
    }
    boolean bool1 = false;
    if (localContext2 != null) {
      if (getApplicationInfotargetSdkVersion <= 23) {
        i = 1;
      } else {
        i = 0;
      }
    }
    if (i != 0)
    {
      if ((!mUserVisibleHint) && (paramBoolean) && (mState < 4) && (mFragmentManager != null)) {
        i = 1;
      } else {
        i = 0;
      }
    }
    else if ((!mUserVisibleHint) && (paramBoolean) && (mState < 4) && (mFragmentManager != null) && (isAdded())) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      mFragmentManager.performPendingDeferredStart(this);
    }
    mUserVisibleHint = paramBoolean;
    boolean bool2 = bool1;
    if (mState < 4)
    {
      bool2 = bool1;
      if (!paramBoolean) {
        bool2 = true;
      }
    }
    mDeferStart = bool2;
  }
  
  public boolean shouldShowRequestPermissionRationale(String paramString)
  {
    if (mHost != null) {
      return mHost.getContext().getPackageManager().shouldShowRequestPermissionRationale(paramString);
    }
    return false;
  }
  
  public void startActivity(Intent paramIntent)
  {
    startActivity(paramIntent, null);
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle)
  {
    if (mHost != null)
    {
      if (paramBundle != null) {
        mHost.onStartActivityFromFragment(this, paramIntent, -1, paramBundle);
      } else {
        mHost.onStartActivityFromFragment(this, paramIntent, -1, null);
      }
      return;
    }
    paramIntent = new StringBuilder();
    paramIntent.append("Fragment ");
    paramIntent.append(this);
    paramIntent.append(" not attached to Activity");
    throw new IllegalStateException(paramIntent.toString());
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    startActivityForResult(paramIntent, paramInt, null);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    if (mHost != null)
    {
      mHost.onStartActivityFromFragment(this, paramIntent, paramInt, paramBundle);
      return;
    }
    paramIntent = new StringBuilder();
    paramIntent.append("Fragment ");
    paramIntent.append(this);
    paramIntent.append(" not attached to Activity");
    throw new IllegalStateException(paramIntent.toString());
  }
  
  public void startActivityForResultAsUser(Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (mHost != null)
    {
      mHost.onStartActivityAsUserFromFragment(this, paramIntent, paramInt, paramBundle, paramUserHandle);
      return;
    }
    paramIntent = new StringBuilder();
    paramIntent.append("Fragment ");
    paramIntent.append(this);
    paramIntent.append(" not attached to Activity");
    throw new IllegalStateException(paramIntent.toString());
  }
  
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    if (mHost != null)
    {
      mHost.onStartIntentSenderFromFragment(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      return;
    }
    paramIntentSender = new StringBuilder();
    paramIntentSender.append("Fragment ");
    paramIntentSender.append(this);
    paramIntentSender.append(" not attached to Activity");
    throw new IllegalStateException(paramIntentSender.toString());
  }
  
  public void startPostponedEnterTransition()
  {
    if ((mFragmentManager != null) && (mFragmentManager.mHost != null))
    {
      if (Looper.myLooper() != mFragmentManager.mHost.getHandler().getLooper()) {
        mFragmentManager.mHost.getHandler().postAtFrontOfQueue(new _..Lambda.Fragment.m7ODa2MK0_rf4XCEL5JOn14G0h8(this));
      } else {
        callStartTransitionListener();
      }
    }
    else {
      ensureAnimationInfomEnterTransitionPostponed = false;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    DebugUtils.buildShortClassTag(this, localStringBuilder);
    if (mIndex >= 0)
    {
      localStringBuilder.append(" #");
      localStringBuilder.append(mIndex);
    }
    if (mFragmentId != 0)
    {
      localStringBuilder.append(" id=0x");
      localStringBuilder.append(Integer.toHexString(mFragmentId));
    }
    if (mTag != null)
    {
      localStringBuilder.append(" ");
      localStringBuilder.append(mTag);
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void unregisterForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(null);
  }
  
  static class AnimationInfo
  {
    private Boolean mAllowEnterTransitionOverlap;
    private Boolean mAllowReturnTransitionOverlap;
    Animator mAnimatingAway;
    private Transition mEnterTransition = null;
    SharedElementCallback mEnterTransitionCallback = SharedElementCallback.NULL_CALLBACK;
    boolean mEnterTransitionPostponed;
    private Transition mExitTransition = null;
    SharedElementCallback mExitTransitionCallback = SharedElementCallback.NULL_CALLBACK;
    boolean mIsHideReplaced;
    int mNextAnim;
    int mNextTransition;
    int mNextTransitionStyle;
    private Transition mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
    private Transition mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
    private Transition mSharedElementEnterTransition = null;
    private Transition mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
    Fragment.OnStartEnterTransitionListener mStartEnterTransitionListener;
    int mStateAfterAnimating;
    
    AnimationInfo() {}
  }
  
  @Deprecated
  public static class InstantiationException
    extends AndroidRuntimeException
  {
    public InstantiationException(String paramString, Exception paramException)
    {
      super(paramException);
    }
  }
  
  static abstract interface OnStartEnterTransitionListener
  {
    public abstract void onStartEnterTransition();
    
    public abstract void startListening();
  }
  
  @Deprecated
  public static class SavedState
    implements Parcelable
  {
    public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator()
    {
      public Fragment.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Fragment.SavedState(paramAnonymousParcel, null);
      }
      
      public Fragment.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new Fragment.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public Fragment.SavedState[] newArray(int paramAnonymousInt)
      {
        return new Fragment.SavedState[paramAnonymousInt];
      }
    };
    final Bundle mState;
    
    SavedState(Bundle paramBundle)
    {
      mState = paramBundle;
    }
    
    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      mState = paramParcel.readBundle();
      if ((paramClassLoader != null) && (mState != null)) {
        mState.setClassLoader(paramClassLoader);
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeBundle(mState);
    }
  }
}
