package android.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.GraphicBuffer;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.util.Pair;
import android.util.Slog;
import android.view.AppTransitionAnimationSpec;
import android.view.IAppTransitionAnimationSpecsFuture;
import android.view.IAppTransitionAnimationSpecsFuture.Stub;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.ArrayList;

public class ActivityOptions
{
  public static final int ANIM_CLIP_REVEAL = 11;
  public static final int ANIM_CUSTOM = 1;
  public static final int ANIM_CUSTOM_IN_PLACE = 10;
  public static final int ANIM_DEFAULT = 6;
  public static final int ANIM_LAUNCH_TASK_BEHIND = 7;
  public static final int ANIM_NONE = 0;
  public static final int ANIM_OPEN_CROSS_PROFILE_APPS = 12;
  public static final int ANIM_REMOTE_ANIMATION = 13;
  public static final int ANIM_SCALE_UP = 2;
  public static final int ANIM_SCENE_TRANSITION = 5;
  public static final int ANIM_THUMBNAIL_ASPECT_SCALE_DOWN = 9;
  public static final int ANIM_THUMBNAIL_ASPECT_SCALE_UP = 8;
  public static final int ANIM_THUMBNAIL_SCALE_DOWN = 4;
  public static final int ANIM_THUMBNAIL_SCALE_UP = 3;
  public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
  public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";
  private static final String KEY_ANIMATION_FINISHED_LISTENER = "android:activity.animationFinishedListener";
  public static final String KEY_ANIM_ENTER_RES_ID = "android:activity.animEnterRes";
  public static final String KEY_ANIM_EXIT_RES_ID = "android:activity.animExitRes";
  public static final String KEY_ANIM_HEIGHT = "android:activity.animHeight";
  public static final String KEY_ANIM_IN_PLACE_RES_ID = "android:activity.animInPlaceRes";
  private static final String KEY_ANIM_SPECS = "android:activity.animSpecs";
  public static final String KEY_ANIM_START_LISTENER = "android:activity.animStartListener";
  public static final String KEY_ANIM_START_X = "android:activity.animStartX";
  public static final String KEY_ANIM_START_Y = "android:activity.animStartY";
  public static final String KEY_ANIM_THUMBNAIL = "android:activity.animThumbnail";
  public static final String KEY_ANIM_TYPE = "android:activity.animType";
  public static final String KEY_ANIM_WIDTH = "android:activity.animWidth";
  private static final String KEY_AVOID_MOVE_TO_FRONT = "android.activity.avoidMoveToFront";
  private static final String KEY_DISALLOW_ENTER_PICTURE_IN_PICTURE_WHILE_LAUNCHING = "android:activity.disallowEnterPictureInPictureWhileLaunching";
  private static final String KEY_EXIT_COORDINATOR_INDEX = "android:activity.exitCoordinatorIndex";
  private static final String KEY_INSTANT_APP_VERIFICATION_BUNDLE = "android:instantapps.installerbundle";
  private static final String KEY_LAUNCH_ACTIVITY_TYPE = "android.activity.activityType";
  public static final String KEY_LAUNCH_BOUNDS = "android:activity.launchBounds";
  private static final String KEY_LAUNCH_DISPLAY_ID = "android.activity.launchDisplayId";
  private static final String KEY_LAUNCH_TASK_ID = "android.activity.launchTaskId";
  private static final String KEY_LAUNCH_WINDOWING_MODE = "android.activity.windowingMode";
  private static final String KEY_LOCK_TASK_MODE = "android:activity.lockTaskMode";
  public static final String KEY_PACKAGE_NAME = "android:activity.packageName";
  private static final String KEY_REMOTE_ANIMATION_ADAPTER = "android:activity.remoteAnimationAdapter";
  private static final String KEY_RESULT_CODE = "android:activity.resultCode";
  private static final String KEY_RESULT_DATA = "android:activity.resultData";
  private static final String KEY_ROTATION_ANIMATION_HINT = "android:activity.rotationAnimationHint";
  private static final String KEY_SPECS_FUTURE = "android:activity.specsFuture";
  private static final String KEY_SPLIT_SCREEN_CREATE_MODE = "android:activity.splitScreenCreateMode";
  private static final String KEY_TASK_OVERLAY = "android.activity.taskOverlay";
  private static final String KEY_TASK_OVERLAY_CAN_RESUME = "android.activity.taskOverlayCanResume";
  private static final String KEY_TRANSITION_COMPLETE_LISTENER = "android:activity.transitionCompleteListener";
  private static final String KEY_TRANSITION_IS_RETURNING = "android:activity.transitionIsReturning";
  private static final String KEY_TRANSITION_SHARED_ELEMENTS = "android:activity.sharedElementNames";
  private static final String KEY_USAGE_TIME_REPORT = "android:activity.usageTimeReport";
  private static final String TAG = "ActivityOptions";
  private AppTransitionAnimationSpec[] mAnimSpecs;
  private IRemoteCallback mAnimationFinishedListener;
  private IRemoteCallback mAnimationStartedListener;
  private int mAnimationType = 0;
  private Bundle mAppVerificationBundle;
  private boolean mAvoidMoveToFront;
  private int mCustomEnterResId;
  private int mCustomExitResId;
  private int mCustomInPlaceResId;
  private boolean mDisallowEnterPictureInPictureWhileLaunching;
  private int mExitCoordinatorIndex;
  private int mHeight;
  private boolean mIsReturning;
  @WindowConfiguration.ActivityType
  private int mLaunchActivityType = 0;
  private Rect mLaunchBounds;
  private int mLaunchDisplayId = -1;
  private int mLaunchTaskId = -1;
  @WindowConfiguration.WindowingMode
  private int mLaunchWindowingMode = 0;
  private boolean mLockTaskMode = false;
  private String mPackageName;
  private RemoteAnimationAdapter mRemoteAnimationAdapter;
  private int mResultCode;
  private Intent mResultData;
  private int mRotationAnimationHint = -1;
  private ArrayList<String> mSharedElementNames;
  private IAppTransitionAnimationSpecsFuture mSpecsFuture;
  private int mSplitScreenCreateMode = 0;
  private int mStartX;
  private int mStartY;
  private boolean mTaskOverlay;
  private boolean mTaskOverlayCanResume;
  private Bitmap mThumbnail;
  private ResultReceiver mTransitionReceiver;
  private PendingIntent mUsageTimeReport;
  private int mWidth;
  
  private ActivityOptions() {}
  
  public ActivityOptions(Bundle paramBundle)
  {
    paramBundle.setDefusable(true);
    mPackageName = paramBundle.getString("android:activity.packageName");
    try
    {
      mUsageTimeReport = ((PendingIntent)paramBundle.getParcelable("android:activity.usageTimeReport"));
    }
    catch (RuntimeException localRuntimeException)
    {
      Slog.w("ActivityOptions", localRuntimeException);
    }
    mLaunchBounds = ((Rect)paramBundle.getParcelable("android:activity.launchBounds"));
    mAnimationType = paramBundle.getInt("android:activity.animType");
    Object localObject;
    switch (mAnimationType)
    {
    case 6: 
    case 7: 
    default: 
      break;
    case 10: 
      mCustomInPlaceResId = paramBundle.getInt("android:activity.animInPlaceRes", 0);
      break;
    case 5: 
      mTransitionReceiver = ((ResultReceiver)paramBundle.getParcelable("android:activity.transitionCompleteListener"));
      mIsReturning = paramBundle.getBoolean("android:activity.transitionIsReturning", false);
      mSharedElementNames = paramBundle.getStringArrayList("android:activity.sharedElementNames");
      mResultData = ((Intent)paramBundle.getParcelable("android:activity.resultData"));
      mResultCode = paramBundle.getInt("android:activity.resultCode");
      mExitCoordinatorIndex = paramBundle.getInt("android:activity.exitCoordinatorIndex");
      break;
    case 3: 
    case 4: 
    case 8: 
    case 9: 
      localObject = (GraphicBuffer)paramBundle.getParcelable("android:activity.animThumbnail");
      if (localObject != null) {
        mThumbnail = Bitmap.createHardwareBitmap((GraphicBuffer)localObject);
      }
      mStartX = paramBundle.getInt("android:activity.animStartX", 0);
      mStartY = paramBundle.getInt("android:activity.animStartY", 0);
      mWidth = paramBundle.getInt("android:activity.animWidth", 0);
      mHeight = paramBundle.getInt("android:activity.animHeight", 0);
      mAnimationStartedListener = IRemoteCallback.Stub.asInterface(paramBundle.getBinder("android:activity.animStartListener"));
      break;
    case 2: 
    case 11: 
      mStartX = paramBundle.getInt("android:activity.animStartX", 0);
      mStartY = paramBundle.getInt("android:activity.animStartY", 0);
      mWidth = paramBundle.getInt("android:activity.animWidth", 0);
      mHeight = paramBundle.getInt("android:activity.animHeight", 0);
      break;
    case 1: 
      mCustomEnterResId = paramBundle.getInt("android:activity.animEnterRes", 0);
      mCustomExitResId = paramBundle.getInt("android:activity.animExitRes", 0);
      mAnimationStartedListener = IRemoteCallback.Stub.asInterface(paramBundle.getBinder("android:activity.animStartListener"));
    }
    mLockTaskMode = paramBundle.getBoolean("android:activity.lockTaskMode", false);
    mLaunchDisplayId = paramBundle.getInt("android.activity.launchDisplayId", -1);
    mLaunchWindowingMode = paramBundle.getInt("android.activity.windowingMode", 0);
    mLaunchActivityType = paramBundle.getInt("android.activity.activityType", 0);
    mLaunchTaskId = paramBundle.getInt("android.activity.launchTaskId", -1);
    mTaskOverlay = paramBundle.getBoolean("android.activity.taskOverlay", false);
    mTaskOverlayCanResume = paramBundle.getBoolean("android.activity.taskOverlayCanResume", false);
    mAvoidMoveToFront = paramBundle.getBoolean("android.activity.avoidMoveToFront", false);
    mSplitScreenCreateMode = paramBundle.getInt("android:activity.splitScreenCreateMode", 0);
    mDisallowEnterPictureInPictureWhileLaunching = paramBundle.getBoolean("android:activity.disallowEnterPictureInPictureWhileLaunching", false);
    if (paramBundle.containsKey("android:activity.animSpecs"))
    {
      localObject = paramBundle.getParcelableArray("android:activity.animSpecs");
      mAnimSpecs = new AppTransitionAnimationSpec[localObject.length];
      for (int i = localObject.length - 1; i >= 0; i--) {
        mAnimSpecs[i] = ((AppTransitionAnimationSpec)localObject[i]);
      }
    }
    if (paramBundle.containsKey("android:activity.animationFinishedListener")) {
      mAnimationFinishedListener = IRemoteCallback.Stub.asInterface(paramBundle.getBinder("android:activity.animationFinishedListener"));
    }
    mRotationAnimationHint = paramBundle.getInt("android:activity.rotationAnimationHint");
    mAppVerificationBundle = paramBundle.getBundle("android:instantapps.installerbundle");
    if (paramBundle.containsKey("android:activity.specsFuture")) {
      mSpecsFuture = IAppTransitionAnimationSpecsFuture.Stub.asInterface(paramBundle.getBinder("android:activity.specsFuture"));
    }
    mRemoteAnimationAdapter = ((RemoteAnimationAdapter)paramBundle.getParcelable("android:activity.remoteAnimationAdapter"));
  }
  
  public static void abort(ActivityOptions paramActivityOptions)
  {
    if (paramActivityOptions != null) {
      paramActivityOptions.abort();
    }
  }
  
  public static ActivityOptions fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null) {
      paramBundle = new ActivityOptions(paramBundle);
    } else {
      paramBundle = null;
    }
    return paramBundle;
  }
  
  private static ActivityOptions makeAspectScaledThumbnailAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Handler paramHandler, OnAnimationStartedListener paramOnAnimationStartedListener, boolean paramBoolean)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramView.getContext().getPackageName();
    int i;
    if (paramBoolean) {
      i = 8;
    } else {
      i = 9;
    }
    mAnimationType = i;
    mThumbnail = paramBitmap;
    paramBitmap = new int[2];
    paramView.getLocationOnScreen(paramBitmap);
    mStartX = (paramBitmap[0] + paramInt1);
    mStartY = (paramBitmap[1] + paramInt2);
    mWidth = paramInt3;
    mHeight = paramInt4;
    localActivityOptions.setOnAnimationStartedListener(paramHandler, paramOnAnimationStartedListener);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeBasic()
  {
    return new ActivityOptions();
  }
  
  public static ActivityOptions makeClipRevealAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mAnimationType = 11;
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    mStartX = (arrayOfInt[0] + paramInt1);
    mStartY = (arrayOfInt[1] + paramInt2);
    mWidth = paramInt3;
    mHeight = paramInt4;
    return localActivityOptions;
  }
  
  public static ActivityOptions makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2)
  {
    return makeCustomAnimation(paramContext, paramInt1, paramInt2, null, null);
  }
  
  public static ActivityOptions makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2, Handler paramHandler, OnAnimationStartedListener paramOnAnimationStartedListener)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramContext.getPackageName();
    mAnimationType = 1;
    mCustomEnterResId = paramInt1;
    mCustomExitResId = paramInt2;
    localActivityOptions.setOnAnimationStartedListener(paramHandler, paramOnAnimationStartedListener);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeCustomInPlaceAnimation(Context paramContext, int paramInt)
  {
    if (paramInt != 0)
    {
      ActivityOptions localActivityOptions = new ActivityOptions();
      mPackageName = paramContext.getPackageName();
      mAnimationType = 10;
      mCustomInPlaceResId = paramInt;
      return localActivityOptions;
    }
    throw new RuntimeException("You must specify a valid animation.");
  }
  
  public static ActivityOptions makeMultiThumbFutureAspectScaleAnimation(Context paramContext, Handler paramHandler, IAppTransitionAnimationSpecsFuture paramIAppTransitionAnimationSpecsFuture, OnAnimationStartedListener paramOnAnimationStartedListener, boolean paramBoolean)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramContext.getPackageName();
    int i;
    if (paramBoolean) {
      i = 8;
    } else {
      i = 9;
    }
    mAnimationType = i;
    mSpecsFuture = paramIAppTransitionAnimationSpecsFuture;
    localActivityOptions.setOnAnimationStartedListener(paramHandler, paramOnAnimationStartedListener);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeOpenCrossProfileAppsAnimation()
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mAnimationType = 12;
    return localActivityOptions;
  }
  
  public static ActivityOptions makeRemoteAnimation(RemoteAnimationAdapter paramRemoteAnimationAdapter)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mRemoteAnimationAdapter = paramRemoteAnimationAdapter;
    mAnimationType = 13;
    return localActivityOptions;
  }
  
  public static ActivityOptions makeScaleUpAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramView.getContext().getPackageName();
    mAnimationType = 2;
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    mStartX = (arrayOfInt[0] + paramInt1);
    mStartY = (arrayOfInt[1] + paramInt2);
    mWidth = paramInt3;
    mHeight = paramInt4;
    return localActivityOptions;
  }
  
  static ActivityOptions makeSceneTransitionAnimation(Activity paramActivity, ExitTransitionCoordinator paramExitTransitionCoordinator, ArrayList<String> paramArrayList, int paramInt, Intent paramIntent)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mAnimationType = 5;
    mSharedElementNames = paramArrayList;
    mTransitionReceiver = paramExitTransitionCoordinator;
    mIsReturning = true;
    mResultCode = paramInt;
    mResultData = paramIntent;
    mExitCoordinatorIndex = mActivityTransitionState.addExitTransitionCoordinator(paramExitTransitionCoordinator);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeSceneTransitionAnimation(Activity paramActivity, View paramView, String paramString)
  {
    return makeSceneTransitionAnimation(paramActivity, new Pair[] { Pair.create(paramView, paramString) });
  }
  
  @SafeVarargs
  public static ActivityOptions makeSceneTransitionAnimation(Activity paramActivity, Pair<View, String>... paramVarArgs)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    makeSceneTransitionAnimation(paramActivity, paramActivity.getWindow(), localActivityOptions, mExitTransitionListener, paramVarArgs);
    return localActivityOptions;
  }
  
  static ExitTransitionCoordinator makeSceneTransitionAnimation(Activity paramActivity, Window paramWindow, ActivityOptions paramActivityOptions, SharedElementCallback paramSharedElementCallback, Pair<View, String>[] paramArrayOfPair)
  {
    if (!paramWindow.hasFeature(13))
    {
      mAnimationType = 6;
      return null;
    }
    mAnimationType = 5;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    boolean bool = false;
    if (paramArrayOfPair != null)
    {
      int i = 0;
      while (i < paramArrayOfPair.length)
      {
        Pair<View, String> localPair = paramArrayOfPair[i];
        String str = (String)second;
        if (str != null)
        {
          localArrayList1.add(str);
          if ((View)first != null)
          {
            localArrayList2.add((View)first);
            i++;
          }
          else
          {
            throw new IllegalArgumentException("Shared element must not be null");
          }
        }
        else
        {
          throw new IllegalArgumentException("Shared element name must not be null");
        }
      }
    }
    paramWindow = new ExitTransitionCoordinator(paramActivity, paramWindow, paramSharedElementCallback, localArrayList1, localArrayList1, localArrayList2, false);
    mTransitionReceiver = paramWindow;
    mSharedElementNames = localArrayList1;
    if (paramActivity == null) {
      bool = true;
    }
    mIsReturning = bool;
    if (paramActivity == null) {
      mExitCoordinatorIndex = -1;
    } else {
      mExitCoordinatorIndex = mActivityTransitionState.addExitTransitionCoordinator(paramWindow);
    }
    return paramWindow;
  }
  
  public static ActivityOptions makeTaskLaunchBehind()
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mAnimationType = 7;
    return localActivityOptions;
  }
  
  private static ActivityOptions makeThumbnailAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2, OnAnimationStartedListener paramOnAnimationStartedListener, boolean paramBoolean)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramView.getContext().getPackageName();
    int i;
    if (paramBoolean) {
      i = 3;
    } else {
      i = 4;
    }
    mAnimationType = i;
    mThumbnail = paramBitmap;
    paramBitmap = new int[2];
    paramView.getLocationOnScreen(paramBitmap);
    mStartX = (paramBitmap[0] + paramInt1);
    mStartY = (paramBitmap[1] + paramInt2);
    localActivityOptions.setOnAnimationStartedListener(paramView.getHandler(), paramOnAnimationStartedListener);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Handler paramHandler, OnAnimationStartedListener paramOnAnimationStartedListener)
  {
    return makeAspectScaledThumbnailAnimation(paramView, paramBitmap, paramInt1, paramInt2, paramInt3, paramInt4, paramHandler, paramOnAnimationStartedListener, false);
  }
  
  public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View paramView, AppTransitionAnimationSpec[] paramArrayOfAppTransitionAnimationSpec, Handler paramHandler, OnAnimationStartedListener paramOnAnimationStartedListener, OnAnimationFinishedListener paramOnAnimationFinishedListener)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    mPackageName = paramView.getContext().getPackageName();
    mAnimationType = 9;
    mAnimSpecs = paramArrayOfAppTransitionAnimationSpec;
    localActivityOptions.setOnAnimationStartedListener(paramHandler, paramOnAnimationStartedListener);
    localActivityOptions.setOnAnimationFinishedListener(paramHandler, paramOnAnimationFinishedListener);
    return localActivityOptions;
  }
  
  public static ActivityOptions makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    return makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2, null);
  }
  
  private static ActivityOptions makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2, OnAnimationStartedListener paramOnAnimationStartedListener)
  {
    return makeThumbnailAnimation(paramView, paramBitmap, paramInt1, paramInt2, paramOnAnimationStartedListener, true);
  }
  
  private void setOnAnimationFinishedListener(final Handler paramHandler, final OnAnimationFinishedListener paramOnAnimationFinishedListener)
  {
    if (paramOnAnimationFinishedListener != null) {
      mAnimationFinishedListener = new IRemoteCallback.Stub()
      {
        public void sendResult(Bundle paramAnonymousBundle)
          throws RemoteException
        {
          paramHandler.post(new Runnable()
          {
            public void run()
            {
              val$listener.onAnimationFinished();
            }
          });
        }
      };
    }
  }
  
  private void setOnAnimationStartedListener(final Handler paramHandler, final OnAnimationStartedListener paramOnAnimationStartedListener)
  {
    if (paramOnAnimationStartedListener != null) {
      mAnimationStartedListener = new IRemoteCallback.Stub()
      {
        public void sendResult(Bundle paramAnonymousBundle)
          throws RemoteException
        {
          paramHandler.post(new Runnable()
          {
            public void run()
            {
              val$listener.onAnimationStarted();
            }
          });
        }
      };
    }
  }
  
  @SafeVarargs
  public static ActivityOptions startSharedElementAnimation(Window paramWindow, Pair<View, String>... paramVarArgs)
  {
    ActivityOptions localActivityOptions = new ActivityOptions();
    if (paramWindow.getDecorView() == null) {
      return localActivityOptions;
    }
    paramVarArgs = makeSceneTransitionAnimation(null, paramWindow, localActivityOptions, null, paramVarArgs);
    if (paramVarArgs != null)
    {
      paramVarArgs.setHideSharedElementsCallback(new HideWindowListener(paramWindow, paramVarArgs));
      paramVarArgs.startExit();
    }
    return localActivityOptions;
  }
  
  public static void stopSharedElementAnimation(Window paramWindow)
  {
    View localView = paramWindow.getDecorView();
    if (localView == null) {
      return;
    }
    paramWindow = (ExitTransitionCoordinator)localView.getTag(16908876);
    if (paramWindow != null)
    {
      paramWindow.cancelPendingTransitions();
      localView.setTagInternal(16908876, null);
      TransitionManager.endTransitions((ViewGroup)localView);
      paramWindow.resetViews();
      paramWindow.clearState();
      localView.setVisibility(0);
    }
  }
  
  public void abort()
  {
    if (mAnimationStartedListener != null) {
      try
      {
        mAnimationStartedListener.sendResult(null);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public boolean canTaskOverlayResume()
  {
    return mTaskOverlayCanResume;
  }
  
  public boolean disallowEnterPictureInPictureWhileLaunching()
  {
    return mDisallowEnterPictureInPictureWhileLaunching;
  }
  
  public ActivityOptions forTargetActivity()
  {
    if (mAnimationType == 5)
    {
      ActivityOptions localActivityOptions = new ActivityOptions();
      localActivityOptions.update(this);
      return localActivityOptions;
    }
    return null;
  }
  
  public AppTransitionAnimationSpec[] getAnimSpecs()
  {
    return mAnimSpecs;
  }
  
  public IRemoteCallback getAnimationFinishedListener()
  {
    return mAnimationFinishedListener;
  }
  
  public int getAnimationType()
  {
    return mAnimationType;
  }
  
  public boolean getAvoidMoveToFront()
  {
    return mAvoidMoveToFront;
  }
  
  public int getCustomEnterResId()
  {
    return mCustomEnterResId;
  }
  
  public int getCustomExitResId()
  {
    return mCustomExitResId;
  }
  
  public int getCustomInPlaceResId()
  {
    return mCustomInPlaceResId;
  }
  
  public int getExitCoordinatorKey()
  {
    return mExitCoordinatorIndex;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getLaunchActivityType()
  {
    return mLaunchActivityType;
  }
  
  public Rect getLaunchBounds()
  {
    return mLaunchBounds;
  }
  
  public int getLaunchDisplayId()
  {
    return mLaunchDisplayId;
  }
  
  public boolean getLaunchTaskBehind()
  {
    boolean bool;
    if (mAnimationType == 7) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getLaunchTaskId()
  {
    return mLaunchTaskId;
  }
  
  public int getLaunchWindowingMode()
  {
    return mLaunchWindowingMode;
  }
  
  public boolean getLockTaskMode()
  {
    return mLockTaskMode;
  }
  
  public IRemoteCallback getOnAnimationStartListener()
  {
    return mAnimationStartedListener;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public RemoteAnimationAdapter getRemoteAnimationAdapter()
  {
    return mRemoteAnimationAdapter;
  }
  
  public int getResultCode()
  {
    return mResultCode;
  }
  
  public Intent getResultData()
  {
    return mResultData;
  }
  
  public ResultReceiver getResultReceiver()
  {
    return mTransitionReceiver;
  }
  
  public int getRotationAnimationHint()
  {
    return mRotationAnimationHint;
  }
  
  public ArrayList<String> getSharedElementNames()
  {
    return mSharedElementNames;
  }
  
  public IAppTransitionAnimationSpecsFuture getSpecsFuture()
  {
    return mSpecsFuture;
  }
  
  public int getSplitScreenCreateMode()
  {
    return mSplitScreenCreateMode;
  }
  
  public int getStartX()
  {
    return mStartX;
  }
  
  public int getStartY()
  {
    return mStartY;
  }
  
  public boolean getTaskOverlay()
  {
    return mTaskOverlay;
  }
  
  public GraphicBuffer getThumbnail()
  {
    GraphicBuffer localGraphicBuffer;
    if (mThumbnail != null) {
      localGraphicBuffer = mThumbnail.createGraphicBufferHandle();
    } else {
      localGraphicBuffer = null;
    }
    return localGraphicBuffer;
  }
  
  public PendingIntent getUsageTimeReport()
  {
    return mUsageTimeReport;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  boolean isCrossTask()
  {
    boolean bool;
    if (mExitCoordinatorIndex < 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReturning()
  {
    return mIsReturning;
  }
  
  public Bundle popAppVerificationBundle()
  {
    Bundle localBundle = mAppVerificationBundle;
    mAppVerificationBundle = null;
    return localBundle;
  }
  
  public void requestUsageTimeReport(PendingIntent paramPendingIntent)
  {
    mUsageTimeReport = paramPendingIntent;
  }
  
  public ActivityOptions setAppVerificationBundle(Bundle paramBundle)
  {
    mAppVerificationBundle = paramBundle;
    return this;
  }
  
  public void setAvoidMoveToFront()
  {
    mAvoidMoveToFront = true;
  }
  
  public void setDisallowEnterPictureInPictureWhileLaunching(boolean paramBoolean)
  {
    mDisallowEnterPictureInPictureWhileLaunching = paramBoolean;
  }
  
  public void setLaunchActivityType(int paramInt)
  {
    mLaunchActivityType = paramInt;
  }
  
  public ActivityOptions setLaunchBounds(Rect paramRect)
  {
    if (paramRect != null) {
      paramRect = new Rect(paramRect);
    } else {
      paramRect = null;
    }
    mLaunchBounds = paramRect;
    return this;
  }
  
  public ActivityOptions setLaunchDisplayId(int paramInt)
  {
    mLaunchDisplayId = paramInt;
    return this;
  }
  
  public void setLaunchTaskId(int paramInt)
  {
    mLaunchTaskId = paramInt;
  }
  
  public void setLaunchWindowingMode(int paramInt)
  {
    mLaunchWindowingMode = paramInt;
  }
  
  public ActivityOptions setLockTaskEnabled(boolean paramBoolean)
  {
    mLockTaskMode = paramBoolean;
    return this;
  }
  
  public void setRemoteAnimationAdapter(RemoteAnimationAdapter paramRemoteAnimationAdapter)
  {
    mRemoteAnimationAdapter = paramRemoteAnimationAdapter;
  }
  
  public void setRotationAnimationHint(int paramInt)
  {
    mRotationAnimationHint = paramInt;
  }
  
  public void setSplitScreenCreateMode(int paramInt)
  {
    mSplitScreenCreateMode = paramInt;
  }
  
  public void setTaskOverlay(boolean paramBoolean1, boolean paramBoolean2)
  {
    mTaskOverlay = paramBoolean1;
    mTaskOverlayCanResume = paramBoolean2;
  }
  
  public Bundle toBundle()
  {
    Bundle localBundle = new Bundle();
    if (mPackageName != null) {
      localBundle.putString("android:activity.packageName", mPackageName);
    }
    if (mLaunchBounds != null) {
      localBundle.putParcelable("android:activity.launchBounds", mLaunchBounds);
    }
    localBundle.putInt("android:activity.animType", mAnimationType);
    if (mUsageTimeReport != null) {
      localBundle.putParcelable("android:activity.usageTimeReport", mUsageTimeReport);
    }
    int i = mAnimationType;
    Bitmap localBitmap = null;
    Object localObject = null;
    switch (i)
    {
    case 6: 
    case 7: 
    default: 
      break;
    case 10: 
      localBundle.putInt("android:activity.animInPlaceRes", mCustomInPlaceResId);
      break;
    case 5: 
      if (mTransitionReceiver != null) {
        localBundle.putParcelable("android:activity.transitionCompleteListener", mTransitionReceiver);
      }
      localBundle.putBoolean("android:activity.transitionIsReturning", mIsReturning);
      localBundle.putStringArrayList("android:activity.sharedElementNames", mSharedElementNames);
      localBundle.putParcelable("android:activity.resultData", mResultData);
      localBundle.putInt("android:activity.resultCode", mResultCode);
      localBundle.putInt("android:activity.exitCoordinatorIndex", mExitCoordinatorIndex);
      break;
    case 3: 
    case 4: 
    case 8: 
    case 9: 
      if (mThumbnail != null)
      {
        localBitmap = mThumbnail.copy(Bitmap.Config.HARDWARE, false);
        if (localBitmap != null) {
          localBundle.putParcelable("android:activity.animThumbnail", localBitmap.createGraphicBufferHandle());
        } else {
          Slog.w("ActivityOptions", "Failed to copy thumbnail");
        }
      }
      localBundle.putInt("android:activity.animStartX", mStartX);
      localBundle.putInt("android:activity.animStartY", mStartY);
      localBundle.putInt("android:activity.animWidth", mWidth);
      localBundle.putInt("android:activity.animHeight", mHeight);
      if (mAnimationStartedListener != null) {
        localObject = mAnimationStartedListener.asBinder();
      }
      localBundle.putBinder("android:activity.animStartListener", (IBinder)localObject);
      break;
    case 2: 
    case 11: 
      localBundle.putInt("android:activity.animStartX", mStartX);
      localBundle.putInt("android:activity.animStartY", mStartY);
      localBundle.putInt("android:activity.animWidth", mWidth);
      localBundle.putInt("android:activity.animHeight", mHeight);
      break;
    case 1: 
      localBundle.putInt("android:activity.animEnterRes", mCustomEnterResId);
      localBundle.putInt("android:activity.animExitRes", mCustomExitResId);
      localObject = localBitmap;
      if (mAnimationStartedListener != null) {
        localObject = mAnimationStartedListener.asBinder();
      }
      localBundle.putBinder("android:activity.animStartListener", (IBinder)localObject);
    }
    localBundle.putBoolean("android:activity.lockTaskMode", mLockTaskMode);
    localBundle.putInt("android.activity.launchDisplayId", mLaunchDisplayId);
    localBundle.putInt("android.activity.windowingMode", mLaunchWindowingMode);
    localBundle.putInt("android.activity.activityType", mLaunchActivityType);
    localBundle.putInt("android.activity.launchTaskId", mLaunchTaskId);
    localBundle.putBoolean("android.activity.taskOverlay", mTaskOverlay);
    localBundle.putBoolean("android.activity.taskOverlayCanResume", mTaskOverlayCanResume);
    localBundle.putBoolean("android.activity.avoidMoveToFront", mAvoidMoveToFront);
    localBundle.putInt("android:activity.splitScreenCreateMode", mSplitScreenCreateMode);
    localBundle.putBoolean("android:activity.disallowEnterPictureInPictureWhileLaunching", mDisallowEnterPictureInPictureWhileLaunching);
    if (mAnimSpecs != null) {
      localBundle.putParcelableArray("android:activity.animSpecs", mAnimSpecs);
    }
    if (mAnimationFinishedListener != null) {
      localBundle.putBinder("android:activity.animationFinishedListener", mAnimationFinishedListener.asBinder());
    }
    if (mSpecsFuture != null) {
      localBundle.putBinder("android:activity.specsFuture", mSpecsFuture.asBinder());
    }
    localBundle.putInt("android:activity.rotationAnimationHint", mRotationAnimationHint);
    if (mAppVerificationBundle != null) {
      localBundle.putBundle("android:instantapps.installerbundle", mAppVerificationBundle);
    }
    if (mRemoteAnimationAdapter != null) {
      localBundle.putParcelable("android:activity.remoteAnimationAdapter", mRemoteAnimationAdapter);
    }
    return localBundle;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ActivityOptions(");
    localStringBuilder.append(hashCode());
    localStringBuilder.append("), mPackageName=");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append(", mAnimationType=");
    localStringBuilder.append(mAnimationType);
    localStringBuilder.append(", mStartX=");
    localStringBuilder.append(mStartX);
    localStringBuilder.append(", mStartY=");
    localStringBuilder.append(mStartY);
    localStringBuilder.append(", mWidth=");
    localStringBuilder.append(mWidth);
    localStringBuilder.append(", mHeight=");
    localStringBuilder.append(mHeight);
    return localStringBuilder.toString();
  }
  
  public void update(ActivityOptions paramActivityOptions)
  {
    if (mPackageName != null) {
      mPackageName = mPackageName;
    }
    mUsageTimeReport = mUsageTimeReport;
    mTransitionReceiver = null;
    mSharedElementNames = null;
    mIsReturning = false;
    mResultData = null;
    mResultCode = 0;
    mExitCoordinatorIndex = 0;
    mAnimationType = mAnimationType;
    switch (mAnimationType)
    {
    case 6: 
    case 7: 
    default: 
      break;
    case 10: 
      mCustomInPlaceResId = mCustomInPlaceResId;
      break;
    case 5: 
      mTransitionReceiver = mTransitionReceiver;
      mSharedElementNames = mSharedElementNames;
      mIsReturning = mIsReturning;
      mThumbnail = null;
      mAnimationStartedListener = null;
      mResultData = mResultData;
      mResultCode = mResultCode;
      mExitCoordinatorIndex = mExitCoordinatorIndex;
      break;
    case 3: 
    case 4: 
    case 8: 
    case 9: 
      mThumbnail = mThumbnail;
      mStartX = mStartX;
      mStartY = mStartY;
      mWidth = mWidth;
      mHeight = mHeight;
      if (mAnimationStartedListener != null) {
        try
        {
          mAnimationStartedListener.sendResult(null);
        }
        catch (RemoteException localRemoteException1) {}
      }
      mAnimationStartedListener = mAnimationStartedListener;
      break;
    case 2: 
      mStartX = mStartX;
      mStartY = mStartY;
      mWidth = mWidth;
      mHeight = mHeight;
      if (mAnimationStartedListener != null) {
        try
        {
          mAnimationStartedListener.sendResult(null);
        }
        catch (RemoteException localRemoteException2) {}
      }
      mAnimationStartedListener = null;
      break;
    case 1: 
      mCustomEnterResId = mCustomEnterResId;
      mCustomExitResId = mCustomExitResId;
      mThumbnail = null;
      if (mAnimationStartedListener != null) {
        try
        {
          mAnimationStartedListener.sendResult(null);
        }
        catch (RemoteException localRemoteException3) {}
      }
      mAnimationStartedListener = mAnimationStartedListener;
    }
    mLockTaskMode = mLockTaskMode;
    mAnimSpecs = mAnimSpecs;
    mAnimationFinishedListener = mAnimationFinishedListener;
    mSpecsFuture = mSpecsFuture;
    mRemoteAnimationAdapter = mRemoteAnimationAdapter;
  }
  
  private static class HideWindowListener
    extends TransitionListenerAdapter
    implements ExitTransitionCoordinator.HideSharedElementsCallback
  {
    private final ExitTransitionCoordinator mExit;
    private boolean mSharedElementHidden;
    private ArrayList<View> mSharedElements;
    private boolean mTransitionEnded;
    private final boolean mWaitingForTransition;
    private final Window mWindow;
    
    public HideWindowListener(Window paramWindow, ExitTransitionCoordinator paramExitTransitionCoordinator)
    {
      mWindow = paramWindow;
      mExit = paramExitTransitionCoordinator;
      mSharedElements = new ArrayList(mSharedElements);
      paramWindow = mWindow.getExitTransition();
      if (paramWindow != null)
      {
        paramWindow.addListener(this);
        mWaitingForTransition = true;
      }
      else
      {
        mWaitingForTransition = false;
      }
      paramWindow = mWindow.getDecorView();
      if (paramWindow != null) {
        if (paramWindow.getTag(16908876) == null) {
          paramWindow.setTagInternal(16908876, paramExitTransitionCoordinator);
        } else {
          throw new IllegalStateException("Cannot start a transition while one is running");
        }
      }
    }
    
    private void hideWhenDone()
    {
      if ((mSharedElementHidden) && ((!mWaitingForTransition) || (mTransitionEnded)))
      {
        mExit.resetViews();
        int i = mSharedElements.size();
        for (int j = 0; j < i; j++) {
          ((View)mSharedElements.get(j)).requestLayout();
        }
        View localView = mWindow.getDecorView();
        if (localView != null)
        {
          localView.setTagInternal(16908876, null);
          localView.setVisibility(8);
        }
      }
    }
    
    public void hideSharedElements()
    {
      mSharedElementHidden = true;
      hideWhenDone();
    }
    
    public void onTransitionEnd(Transition paramTransition)
    {
      mTransitionEnded = true;
      hideWhenDone();
      paramTransition.removeListener(this);
    }
  }
  
  public static abstract interface OnAnimationFinishedListener
  {
    public abstract void onAnimationFinished();
  }
  
  public static abstract interface OnAnimationStartedListener
  {
    public abstract void onAnimationStarted();
  }
}
