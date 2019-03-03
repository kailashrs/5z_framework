package android.app;

import android.annotation.SystemApi;
import android.app.assist.AssistContent;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.KeyboardShortcutGroup;
import android.view.KeyboardShortcutInfo;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.RemoteAnimationDefinition;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.OnWindowDismissedCallback;
import android.view.Window.WindowControllerCallback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillManager.AutofillClient;
import android.view.autofill.AutofillPopupWindow;
import android.view.autofill.Helper;
import android.view.autofill.IAutofillWindowPresenter;
import android.widget.Toast;
import android.widget.Toolbar;
import com.android.internal.R.styleable;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.ToolbarActionBar;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.PhoneWindow;
import dalvik.system.VMRuntime;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Activity
  extends ContextThemeWrapper
  implements LayoutInflater.Factory2, Window.Callback, KeyEvent.Callback, View.OnCreateContextMenuListener, ComponentCallbacks2, Window.OnWindowDismissedCallback, Window.WindowControllerCallback, AutofillManager.AutofillClient
{
  private static final String AUTOFILL_RESET_NEEDED = "@android:autofillResetNeeded";
  private static final String AUTO_FILL_AUTH_WHO_PREFIX = "@android:autoFillAuth:";
  private static final boolean DEBUG = false;
  private static final boolean DEBUG_LIFECYCLE = false;
  public static final int DEFAULT_KEYS_DIALER = 1;
  public static final int DEFAULT_KEYS_DISABLE = 0;
  public static final int DEFAULT_KEYS_SEARCH_GLOBAL = 4;
  public static final int DEFAULT_KEYS_SEARCH_LOCAL = 3;
  public static final int DEFAULT_KEYS_SHORTCUT = 2;
  public static final int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
  public static final int FINISH_TASK_WITH_ACTIVITY = 2;
  public static final int FINISH_TASK_WITH_ROOT_ACTIVITY = 1;
  protected static final int[] FOCUSED_STATE_SET = { 16842908 };
  static final String FRAGMENTS_TAG = "android:fragments";
  private static final String HAS_CURENT_PERMISSIONS_REQUEST_KEY = "android:hasCurrentPermissionsRequest";
  private static final String KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME = "com.android.systemui";
  private static final String LAST_AUTOFILL_ID = "android:lastAutofillId";
  private static final int LOG_AM_ON_ACTIVITY_RESULT_CALLED = 30062;
  private static final int LOG_AM_ON_CREATE_CALLED = 30057;
  private static final int LOG_AM_ON_DESTROY_CALLED = 30060;
  private static final int LOG_AM_ON_PAUSE_CALLED = 30021;
  private static final int LOG_AM_ON_RESTART_CALLED = 30058;
  private static final int LOG_AM_ON_RESUME_CALLED = 30022;
  private static final int LOG_AM_ON_START_CALLED = 30059;
  private static final int LOG_AM_ON_STOP_CALLED = 30049;
  private static final String REQUEST_PERMISSIONS_WHO_PREFIX = "@android:requestPermissions:";
  public static final int RESULT_CANCELED = 0;
  public static final int RESULT_FIRST_USER = 1;
  public static final int RESULT_OK = -1;
  private static final String SAVED_DIALOGS_TAG = "android:savedDialogs";
  private static final String SAVED_DIALOG_ARGS_KEY_PREFIX = "android:dialog_args_";
  private static final String SAVED_DIALOG_IDS_KEY = "android:savedDialogIds";
  private static final String SAVED_DIALOG_KEY_PREFIX = "android:dialog_";
  private static final String TAG = "Activity";
  private static final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
  ActionBar mActionBar = null;
  private int mActionModeTypeStarting = 0;
  ActivityInfo mActivityInfo;
  ActivityTransitionState mActivityTransitionState = new ActivityTransitionState();
  private Application mApplication;
  private boolean mAutoFillIgnoreFirstResumePause;
  private boolean mAutoFillResetNeeded;
  private AutofillManager mAutofillManager;
  private AutofillPopupWindow mAutofillPopupWindow;
  boolean mCalled;
  private boolean mCanEnterPictureInPicture = false;
  private boolean mChangeCanvasToTranslucent;
  boolean mChangingConfigurations = false;
  private ComponentName mComponent;
  int mConfigChangeFlags;
  Configuration mCurrentConfig;
  View mDecor = null;
  private int mDefaultKeyMode = 0;
  private SpannableStringBuilder mDefaultKeySsb = null;
  private boolean mDestroyed;
  private boolean mDoReportFullyDrawn = true;
  String mEmbeddedID;
  private boolean mEnableDefaultActionBarUp;
  SharedElementCallback mEnterTransitionListener = SharedElementCallback.NULL_CALLBACK;
  SharedElementCallback mExitTransitionListener = SharedElementCallback.NULL_CALLBACK;
  boolean mFinished;
  final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
  final Handler mHandler = new Handler();
  private boolean mHasCurrentPermissionsRequest;
  private int mIdent;
  private final Object mInstanceTracker = StrictMode.trackActivity(this);
  private Instrumentation mInstrumentation;
  Intent mIntent;
  private int mLastAutofillId = 1073741823;
  NonConfigurationInstances mLastNonConfigurationInstances;
  ActivityThread mMainThread;
  @GuardedBy("mManagedCursors")
  private final ArrayList<ManagedCursor> mManagedCursors = new ArrayList();
  private SparseArray<ManagedDialog> mManagedDialogs;
  private MenuInflater mMenuInflater;
  Activity mParent;
  String mReferrer;
  private boolean mRestoredFromBundle;
  @GuardedBy("this")
  int mResultCode = 0;
  @GuardedBy("this")
  Intent mResultData = null;
  boolean mResumed;
  private SearchEvent mSearchEvent;
  private SearchManager mSearchManager;
  boolean mStartedActivity;
  boolean mStopped;
  private ActivityManager.TaskDescription mTaskDescription = new ActivityManager.TaskDescription();
  boolean mTemporaryPause = false;
  private CharSequence mTitle;
  private int mTitleColor = 0;
  private boolean mTitleReady = false;
  private IBinder mToken;
  private TranslucentConversionListener mTranslucentCallback;
  private Thread mUiThread;
  boolean mVisibleFromClient = true;
  boolean mVisibleFromServer = false;
  private VoiceInteractor mVoiceInteractor;
  private Window mWindow;
  boolean mWindowAdded = false;
  private WindowManager mWindowManager;
  
  public Activity() {}
  
  private void cancelInputsAndStartExitTransition(Bundle paramBundle)
  {
    View localView;
    if (mWindow != null) {
      localView = mWindow.peekDecorView();
    } else {
      localView = null;
    }
    if (localView != null) {
      localView.cancelPendingInputEvents();
    }
    if ((paramBundle != null) && (!isTopOfTask())) {
      mActivityTransitionState.startExitOutTransition(this, paramBundle);
    }
  }
  
  private Dialog createDialog(Integer paramInteger, Bundle paramBundle1, Bundle paramBundle2)
  {
    paramInteger = onCreateDialog(paramInteger.intValue(), paramBundle2);
    if (paramInteger == null) {
      return null;
    }
    paramInteger.dispatchOnCreate(paramBundle1);
    return paramInteger;
  }
  
  private boolean deviceSupportsPictureInPictureMode()
  {
    return getPackageManager().hasSystemFeature("android.software.picture_in_picture");
  }
  
  private void dispatchRequestPermissionsResult(int paramInt, Intent paramIntent)
  {
    mHasCurrentPermissionsRequest = false;
    String[] arrayOfString;
    if (paramIntent != null) {
      arrayOfString = paramIntent.getStringArrayExtra("android.content.pm.extra.REQUEST_PERMISSIONS_NAMES");
    } else {
      arrayOfString = new String[0];
    }
    if (paramIntent != null) {
      paramIntent = paramIntent.getIntArrayExtra("android.content.pm.extra.REQUEST_PERMISSIONS_RESULTS");
    } else {
      paramIntent = new int[0];
    }
    onRequestPermissionsResult(paramInt, arrayOfString, paramIntent);
  }
  
  private void dispatchRequestPermissionsResultToFragment(int paramInt, Intent paramIntent, Fragment paramFragment)
  {
    String[] arrayOfString;
    if (paramIntent != null) {
      arrayOfString = paramIntent.getStringArrayExtra("android.content.pm.extra.REQUEST_PERMISSIONS_NAMES");
    } else {
      arrayOfString = new String[0];
    }
    if (paramIntent != null) {
      paramIntent = paramIntent.getIntArrayExtra("android.content.pm.extra.REQUEST_PERMISSIONS_RESULTS");
    } else {
      paramIntent = new int[0];
    }
    paramFragment.onRequestPermissionsResult(paramInt, arrayOfString, paramIntent);
  }
  
  private void enableAutofillCompatibilityIfNeeded()
  {
    if (isAutofillCompatibilityEnabled())
    {
      AutofillManager localAutofillManager = (AutofillManager)getSystemService(AutofillManager.class);
      if (localAutofillManager != null) {
        localAutofillManager.enableCompatibilityMode();
      }
    }
  }
  
  private void ensureSearchManager()
  {
    if (mSearchManager != null) {
      return;
    }
    try
    {
      SearchManager localSearchManager = new android/app/SearchManager;
      localSearchManager.<init>(this, null);
      mSearchManager = localSearchManager;
      return;
    }
    catch (ServiceManager.ServiceNotFoundException localServiceNotFoundException)
    {
      throw new IllegalStateException(localServiceNotFoundException);
    }
  }
  
  private void finish(int paramInt)
  {
    if (mParent == null) {
      label63:
      try
      {
        int i = mResultCode;
        Intent localIntent = mResultData;
        if (localIntent != null) {
          try
          {
            localIntent.prepareToLeaveProcess(this);
          }
          catch (RemoteException localRemoteException)
          {
            break label63;
          }
        }
        if (ActivityManager.getService().finishActivity(mToken, i, localRemoteException, paramInt)) {
          mFinished = true;
        }
      }
      finally {}
    }
    mParent.finishFromChild(this);
    if ((mIntent != null) && (mIntent.hasExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN"))) {
      getAutofillManager().onPendingSaveUi(2, mIntent.getIBinderExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN"));
    }
  }
  
  private AutofillManager getAutofillManager()
  {
    if (mAutofillManager == null) {
      mAutofillManager = ((AutofillManager)getSystemService(AutofillManager.class));
    }
    return mAutofillManager;
  }
  
  private static native String getDlWarning();
  
  private void initWindowDecorActionBar()
  {
    Window localWindow = getWindow();
    localWindow.getDecorView();
    if ((!isChild()) && (localWindow.hasFeature(8)) && (mActionBar == null))
    {
      mActionBar = new WindowDecorActionBar(this);
      mActionBar.setDefaultDisplayHomeAsUpEnabled(mEnableDefaultActionBarUp);
      mWindow.setDefaultIcon(mActivityInfo.getIconResource());
      mWindow.setDefaultLogo(mActivityInfo.getLogoResource());
      return;
    }
  }
  
  private boolean isTopOfTask()
  {
    if ((mToken != null) && (mWindow != null)) {
      try
      {
        boolean bool = ActivityManager.getService().isTopOfTask(getActivityToken());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        return false;
      }
    }
    return false;
  }
  
  private IllegalArgumentException missingDialog(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("no dialog with id ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" was ever shown via Activity#showDialog");
    return new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void restoreHasCurrentPermissionRequest(Bundle paramBundle)
  {
    if (paramBundle != null) {
      mHasCurrentPermissionsRequest = paramBundle.getBoolean("android:hasCurrentPermissionsRequest", false);
    }
  }
  
  private void restoreManagedDialogs(Bundle paramBundle)
  {
    paramBundle = paramBundle.getBundle("android:savedDialogs");
    if (paramBundle == null) {
      return;
    }
    int[] arrayOfInt = paramBundle.getIntArray("android:savedDialogIds");
    int i = arrayOfInt.length;
    mManagedDialogs = new SparseArray(i);
    for (int j = 0; j < i; j++)
    {
      Integer localInteger = Integer.valueOf(arrayOfInt[j]);
      Bundle localBundle = paramBundle.getBundle(savedDialogKeyFor(localInteger.intValue()));
      if (localBundle != null)
      {
        ManagedDialog localManagedDialog = new ManagedDialog(null);
        mArgs = paramBundle.getBundle(savedDialogArgsKeyFor(localInteger.intValue()));
        mDialog = createDialog(localInteger, localBundle, mArgs);
        if (mDialog != null)
        {
          mManagedDialogs.put(localInteger.intValue(), localManagedDialog);
          onPrepareDialog(localInteger.intValue(), mDialog, mArgs);
          mDialog.onRestoreInstanceState(localBundle);
        }
      }
    }
  }
  
  private void saveManagedDialogs(Bundle paramBundle)
  {
    if (mManagedDialogs == null) {
      return;
    }
    int i = mManagedDialogs.size();
    if (i == 0) {
      return;
    }
    Bundle localBundle = new Bundle();
    int[] arrayOfInt = new int[mManagedDialogs.size()];
    for (int j = 0; j < i; j++)
    {
      int k = mManagedDialogs.keyAt(j);
      arrayOfInt[j] = k;
      ManagedDialog localManagedDialog = (ManagedDialog)mManagedDialogs.valueAt(j);
      localBundle.putBundle(savedDialogKeyFor(k), mDialog.onSaveInstanceState());
      if (mArgs != null) {
        localBundle.putBundle(savedDialogArgsKeyFor(k), mArgs);
      }
    }
    localBundle.putIntArray("android:savedDialogIds", arrayOfInt);
    paramBundle.putBundle("android:savedDialogs", localBundle);
  }
  
  private static String savedDialogArgsKeyFor(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("android:dialog_args_");
    localStringBuilder.append(paramInt);
    return localStringBuilder.toString();
  }
  
  private static String savedDialogKeyFor(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("android:dialog_");
    localStringBuilder.append(paramInt);
    return localStringBuilder.toString();
  }
  
  private void startIntentSenderForResultInner(IntentSender paramIntentSender, String paramString, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    String str = null;
    if (paramIntent != null) {
      try
      {
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(this);
        str = paramIntent.resolveTypeIfNeeded(getContentResolver());
      }
      catch (RemoteException paramIntentSender)
      {
        break label138;
      }
    }
    IActivityManager localIActivityManager = ActivityManager.getService();
    ActivityThread.ApplicationThread localApplicationThread = mMainThread.getApplicationThread();
    IIntentSender localIIntentSender;
    if (paramIntentSender != null) {
      localIIntentSender = paramIntentSender.getTarget();
    } else {
      localIIntentSender = null;
    }
    if (paramIntentSender != null) {
      paramIntentSender = paramIntentSender.getWhitelistToken();
    } else {
      paramIntentSender = null;
    }
    paramInt2 = localIActivityManager.startActivityIntentSender(localApplicationThread, localIIntentSender, paramIntentSender, paramIntent, str, mToken, paramString, paramInt1, paramInt2, paramInt3, paramBundle);
    if (paramInt2 != -96)
    {
      Instrumentation.checkStartActivityResult(paramInt2, null);
    }
    else
    {
      paramIntentSender = new android/content/IntentSender$SendIntentException;
      paramIntentSender.<init>();
      throw paramIntentSender;
    }
    label138:
    if (paramInt1 >= 0) {
      mStartedActivity = true;
    }
  }
  
  private void storeHasCurrentPermissionRequest(Bundle paramBundle)
  {
    if ((paramBundle != null) && (mHasCurrentPermissionsRequest)) {
      paramBundle.putBoolean("android:hasCurrentPermissionsRequest", true);
    }
  }
  
  private Bundle transferSpringboardActivityOptions(Bundle paramBundle)
  {
    if ((paramBundle == null) && (mWindow != null) && (!mWindow.isActive()))
    {
      ActivityOptions localActivityOptions = getActivityOptions();
      if ((localActivityOptions != null) && (localActivityOptions.getAnimationType() == 5)) {
        return localActivityOptions.toBundle();
      }
    }
    return paramBundle;
  }
  
  private void writeEventLog(int paramInt, String paramString)
  {
    EventLog.writeEvent(paramInt, new Object[] { Integer.valueOf(UserHandle.myUserId()), getComponentName().getClassName(), paramString });
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    getWindow().addContentView(paramView, paramLayoutParams);
    initWindowDecorActionBar();
  }
  
  final void attach(Context paramContext, ActivityThread paramActivityThread, Instrumentation paramInstrumentation, IBinder paramIBinder, int paramInt, Application paramApplication, Intent paramIntent, ActivityInfo paramActivityInfo, CharSequence paramCharSequence, Activity paramActivity, String paramString1, NonConfigurationInstances paramNonConfigurationInstances, Configuration paramConfiguration, String paramString2, IVoiceInteractor paramIVoiceInteractor, Window paramWindow, ViewRootImpl.ActivityConfigCallback paramActivityConfigCallback)
  {
    attachBaseContext(paramContext);
    mFragments.attachHost(null);
    mWindow = new PhoneWindow(this, paramWindow, paramActivityConfigCallback);
    mWindow.setWindowControllerCallback(this);
    mWindow.setCallback(this);
    mWindow.setOnWindowDismissedCallback(this);
    mWindow.getLayoutInflater().setPrivateFactory(this);
    if (softInputMode != 0) {
      mWindow.setSoftInputMode(softInputMode);
    }
    if (uiOptions != 0) {
      mWindow.setUiOptions(uiOptions);
    }
    mUiThread = Thread.currentThread();
    mMainThread = paramActivityThread;
    mInstrumentation = paramInstrumentation;
    mToken = paramIBinder;
    mIdent = paramInt;
    mApplication = paramApplication;
    mIntent = paramIntent;
    mReferrer = paramString2;
    mComponent = paramIntent.getComponent();
    mActivityInfo = paramActivityInfo;
    mTitle = paramCharSequence;
    mParent = paramActivity;
    mEmbeddedID = paramString1;
    mLastNonConfigurationInstances = paramNonConfigurationInstances;
    if (paramIVoiceInteractor != null) {
      if (paramNonConfigurationInstances != null) {
        mVoiceInteractor = voiceInteractor;
      } else {
        mVoiceInteractor = new VoiceInteractor(paramIVoiceInteractor, this, this, Looper.myLooper());
      }
    }
    paramActivityThread = mWindow;
    paramIBinder = (WindowManager)paramContext.getSystemService("window");
    paramInstrumentation = mToken;
    paramContext = mComponent.flattenToString();
    boolean bool;
    if ((flags & 0x200) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    paramActivityThread.setWindowManager(paramIBinder, paramInstrumentation, paramContext, bool);
    if (mParent != null) {
      mWindow.setContainer(mParent.getWindow());
    }
    mWindowManager = mWindow.getWindowManager();
    mCurrentConfig = paramConfiguration;
    mWindow.setColorMode(colorMode);
    setAutofillCompatibilityEnabled(paramApplication.isAutofillCompatibilityEnabled());
    enableAutofillCompatibilityIfNeeded();
  }
  
  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    if (paramContext != null) {
      paramContext.setAutofillClient(this);
    }
  }
  
  public final void autofillClientAuthenticate(int paramInt, IntentSender paramIntentSender, Intent paramIntent)
  {
    try
    {
      startIntentSenderForResultInner(paramIntentSender, "@android:autoFillAuth:", paramInt, paramIntent, 0, 0, null);
    }
    catch (IntentSender.SendIntentException localSendIntentException)
    {
      paramIntent = new StringBuilder();
      paramIntent.append("authenticate() failed for intent:");
      paramIntent.append(paramIntentSender);
      Log.e("Activity", paramIntent.toString(), localSendIntentException);
    }
  }
  
  public final void autofillClientDispatchUnhandledKey(View paramView, KeyEvent paramKeyEvent)
  {
    paramView = paramView.getViewRootImpl();
    if (paramView != null) {
      paramView.dispatchKeyFromAutofill(paramKeyEvent);
    }
  }
  
  public final View autofillClientFindViewByAccessibilityIdTraversal(int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
    for (int i = 0; i < localArrayList.size(); i++)
    {
      View localView = ((ViewRootImpl)localArrayList.get(i)).getView();
      if ((localView != null) && (localView.getAccessibilityWindowId() == paramInt2))
      {
        localView = localView.findViewByAccessibilityIdTraversal(paramInt1);
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  public final View autofillClientFindViewByAutofillIdTraversal(AutofillId paramAutofillId)
  {
    ArrayList localArrayList = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
    for (int i = 0; i < localArrayList.size(); i++)
    {
      View localView = ((ViewRootImpl)localArrayList.get(i)).getView();
      if (localView != null)
      {
        localView = localView.findViewByAutofillIdTraversal(paramAutofillId.getViewId());
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  public final View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] paramArrayOfAutofillId)
  {
    View[] arrayOfView = new View[paramArrayOfAutofillId.length];
    ArrayList localArrayList = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
    for (int i = 0; i < localArrayList.size(); i++)
    {
      View localView = ((ViewRootImpl)localArrayList.get(i)).getView();
      if (localView != null)
      {
        int j = paramArrayOfAutofillId.length;
        for (int k = 0; k < j; k++) {
          if (arrayOfView[k] == null) {
            arrayOfView[k] = localView.findViewByAutofillIdTraversal(paramArrayOfAutofillId[k].getViewId());
          }
        }
      }
    }
    return arrayOfView;
  }
  
  public final IBinder autofillClientGetActivityToken()
  {
    return getActivityToken();
  }
  
  public final ComponentName autofillClientGetComponentName()
  {
    return getComponentName();
  }
  
  public AutofillId autofillClientGetNextAutofillId()
  {
    return new AutofillId(getNextAutofillId());
  }
  
  public final boolean[] autofillClientGetViewVisibility(AutofillId[] paramArrayOfAutofillId)
  {
    int i = paramArrayOfAutofillId.length;
    boolean[] arrayOfBoolean = new boolean[i];
    for (int j = 0; j < i; j++)
    {
      AutofillId localAutofillId = paramArrayOfAutofillId[j];
      View localView = autofillClientFindViewByAutofillIdTraversal(localAutofillId);
      if (localView != null) {
        if (!localAutofillId.isVirtual()) {
          arrayOfBoolean[j] = localView.isVisibleToUser();
        } else {
          arrayOfBoolean[j] = localView.isVisibleToUserForAutofill(localAutofillId.getVirtualChildId());
        }
      }
    }
    if (Helper.sVerbose)
    {
      paramArrayOfAutofillId = new StringBuilder();
      paramArrayOfAutofillId.append("autofillClientGetViewVisibility(): ");
      paramArrayOfAutofillId.append(Arrays.toString(arrayOfBoolean));
      Log.v("Activity", paramArrayOfAutofillId.toString());
    }
    return arrayOfBoolean;
  }
  
  public final boolean autofillClientIsCompatibilityModeEnabled()
  {
    return isAutofillCompatibilityEnabled();
  }
  
  public final boolean autofillClientIsFillUiShowing()
  {
    boolean bool;
    if ((mAutofillPopupWindow != null) && (mAutofillPopupWindow.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean autofillClientIsVisibleForAutofill()
  {
    return mStopped ^ true;
  }
  
  public final boolean autofillClientRequestHideFillUi()
  {
    if (mAutofillPopupWindow == null) {
      return false;
    }
    mAutofillPopupWindow.dismiss();
    mAutofillPopupWindow = null;
    return true;
  }
  
  public final boolean autofillClientRequestShowFillUi(View paramView, int paramInt1, int paramInt2, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter)
  {
    boolean bool;
    if (mAutofillPopupWindow == null)
    {
      bool = false;
      mAutofillPopupWindow = new AutofillPopupWindow(paramIAutofillWindowPresenter);
    }
    else
    {
      bool = mAutofillPopupWindow.isShowing();
    }
    mAutofillPopupWindow.update(paramView, 0, 0, paramInt1, paramInt2, paramRect);
    if ((!bool) && (mAutofillPopupWindow.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final void autofillClientResetableStateAvailable()
  {
    mAutoFillResetNeeded = true;
  }
  
  public final void autofillClientRunOnUiThread(Runnable paramRunnable)
  {
    runOnUiThread(paramRunnable);
  }
  
  public boolean canStartActivityForResult()
  {
    return true;
  }
  
  public void closeContextMenu()
  {
    if (mWindow.hasFeature(6)) {
      mWindow.closePanel(6);
    }
  }
  
  public void closeOptionsMenu()
  {
    if ((mWindow.hasFeature(0)) && ((mActionBar == null) || (!mActionBar.closeOptionsMenu()))) {
      mWindow.closePanel(0);
    }
  }
  
  @SystemApi
  public void convertFromTranslucent()
  {
    try
    {
      mTranslucentCallback = null;
      if (ActivityManager.getService().convertFromTranslucent(mToken)) {
        WindowManagerGlobal.getInstance().changeCanvasOpacity(mToken, true);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  @SystemApi
  public boolean convertToTranslucent(TranslucentConversionListener paramTranslucentConversionListener, ActivityOptions paramActivityOptions)
  {
    boolean bool = false;
    try
    {
      mTranslucentCallback = paramTranslucentConversionListener;
      IActivityManager localIActivityManager = ActivityManager.getService();
      IBinder localIBinder = mToken;
      if (paramActivityOptions == null) {
        paramTranslucentConversionListener = null;
      } else {
        paramTranslucentConversionListener = paramActivityOptions.toBundle();
      }
      mChangeCanvasToTranslucent = localIActivityManager.convertToTranslucent(localIBinder, paramTranslucentConversionListener);
      WindowManagerGlobal.getInstance().changeCanvasOpacity(mToken, false);
      bool = true;
    }
    catch (RemoteException paramTranslucentConversionListener)
    {
      mChangeCanvasToTranslucent = false;
    }
    if ((!mChangeCanvasToTranslucent) && (mTranslucentCallback != null)) {
      mTranslucentCallback.onTranslucentConversionComplete(bool);
    }
    return mChangeCanvasToTranslucent;
  }
  
  public PendingIntent createPendingResult(int paramInt1, Intent paramIntent, int paramInt2)
  {
    String str1 = getPackageName();
    Object localObject1 = null;
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      IActivityManager localIActivityManager = ActivityManager.getService();
      if (mParent == null) {
        localObject2 = mToken;
      } else {
        localObject2 = mParent.mToken;
      }
      String str2 = mEmbeddedID;
      int i = getUserId();
      Object localObject2 = localIActivityManager.getIntentSender(3, str1, (IBinder)localObject2, str2, paramInt1, new Intent[] { paramIntent }, null, paramInt2, null, i);
      paramIntent = localObject1;
      if (localObject2 != null) {
        paramIntent = new PendingIntent((IIntentSender)localObject2);
      }
      return paramIntent;
    }
    catch (RemoteException paramIntent) {}
    return null;
  }
  
  @Deprecated
  public final void dismissDialog(int paramInt)
  {
    if (mManagedDialogs != null)
    {
      ManagedDialog localManagedDialog = (ManagedDialog)mManagedDialogs.get(paramInt);
      if (localManagedDialog != null)
      {
        mDialog.dismiss();
        return;
      }
      throw missingDialog(paramInt);
    }
    throw missingDialog(paramInt);
  }
  
  public final void dismissKeyboardShortcutsHelper()
  {
    Intent localIntent = new Intent("com.android.intent.action.DISMISS_KEYBOARD_SHORTCUTS");
    localIntent.setPackage("com.android.systemui");
    sendBroadcastAsUser(localIntent, UserHandle.SYSTEM);
  }
  
  void dispatchActivityResult(String paramString1, int paramInt1, int paramInt2, Intent paramIntent, String paramString2)
  {
    mFragments.noteStateNotSaved();
    if (paramString1 == null)
    {
      onActivityResult(paramInt1, paramInt2, paramIntent);
    }
    else if (paramString1.startsWith("@android:requestPermissions:"))
    {
      paramString1 = paramString1.substring("@android:requestPermissions:".length());
      if (TextUtils.isEmpty(paramString1))
      {
        dispatchRequestPermissionsResult(paramInt1, paramIntent);
      }
      else
      {
        paramString1 = mFragments.findFragmentByWho(paramString1);
        if (paramString1 != null) {
          dispatchRequestPermissionsResultToFragment(paramInt1, paramIntent, paramString1);
        }
      }
    }
    else if (paramString1.startsWith("@android:view:"))
    {
      Iterator localIterator = WindowManagerGlobal.getInstance().getRootViews(getActivityToken()).iterator();
      while (localIterator.hasNext())
      {
        ViewRootImpl localViewRootImpl = (ViewRootImpl)localIterator.next();
        if ((localViewRootImpl.getView() != null) && (localViewRootImpl.getView().dispatchActivityResult(paramString1, paramInt1, paramInt2, paramIntent))) {
          return;
        }
      }
    }
    else if (paramString1.startsWith("@android:autoFillAuth:"))
    {
      if (paramInt2 == -1) {
        paramString1 = paramIntent;
      } else {
        paramString1 = null;
      }
      getAutofillManager().onAuthenticationResult(paramInt1, paramString1, getCurrentFocus());
    }
    else
    {
      paramString1 = mFragments.findFragmentByWho(paramString1);
      if (paramString1 != null) {
        paramString1.onActivityResult(paramInt1, paramInt2, paramIntent);
      }
    }
    writeEventLog(30062, paramString2);
  }
  
  public void dispatchEnterAnimationComplete()
  {
    onEnterAnimationComplete();
    if ((getWindow() != null) && (getWindow().getDecorView() != null)) {
      getWindow().getDecorView().getViewTreeObserver().dispatchOnEnterAnimationComplete();
    }
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    onUserInteraction();
    if (getWindow().superDispatchGenericMotionEvent(paramMotionEvent)) {
      return true;
    }
    return onGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    onUserInteraction();
    if ((paramKeyEvent.getKeyCode() == 82) && (mActionBar != null) && (mActionBar.onMenuKeyEvent(paramKeyEvent))) {
      return true;
    }
    Window localWindow = getWindow();
    if (localWindow.superDispatchKeyEvent(paramKeyEvent)) {
      return true;
    }
    View localView = mDecor;
    Object localObject = localView;
    if (localView == null) {
      localObject = localWindow.getDecorView();
    }
    if (localObject != null) {
      localObject = ((View)localObject).getKeyDispatcherState();
    } else {
      localObject = null;
    }
    return paramKeyEvent.dispatch(this, (KeyEvent.DispatcherState)localObject, this);
  }
  
  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    onUserInteraction();
    if (getWindow().superDispatchKeyShortcutEvent(paramKeyEvent)) {
      return true;
    }
    return onKeyShortcut(paramKeyEvent.getKeyCode(), paramKeyEvent);
  }
  
  void dispatchMovedToDisplay(int paramInt, Configuration paramConfiguration)
  {
    updateDisplay(paramInt);
    onMovedToDisplay(paramInt, paramConfiguration);
  }
  
  final void dispatchMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    mFragments.dispatchMultiWindowModeChanged(paramBoolean, paramConfiguration);
    if (mWindow != null) {
      mWindow.onMultiWindowModeChanged();
    }
    onMultiWindowModeChanged(paramBoolean, paramConfiguration);
  }
  
  final void dispatchPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    mFragments.dispatchPictureInPictureModeChanged(paramBoolean, paramConfiguration);
    if (mWindow != null) {
      mWindow.onPictureInPictureModeChanged(paramBoolean);
    }
    onPictureInPictureModeChanged(paramBoolean, paramConfiguration);
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    paramAccessibilityEvent.setClassName(getClass().getName());
    paramAccessibilityEvent.setPackageName(getPackageName());
    Object localObject = getWindow().getAttributes();
    boolean bool;
    if ((width == -1) && (height == -1)) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityEvent.setFullScreen(bool);
    localObject = getTitle();
    if (!TextUtils.isEmpty((CharSequence)localObject)) {
      paramAccessibilityEvent.getText().add(localObject);
    }
    return true;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0) {
      onUserInteraction();
    }
    if (getWindow().superDispatchTouchEvent(paramMotionEvent)) {
      return true;
    }
    return onTouchEvent(paramMotionEvent);
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    onUserInteraction();
    if (getWindow().superDispatchTrackballEvent(paramMotionEvent)) {
      return true;
    }
    return onTrackballEvent(paramMotionEvent);
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    dumpInner(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  void dumpInner(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local Activity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("  ");
    localObject = ((StringBuilder)localObject).toString();
    paramPrintWriter.print((String)localObject);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(mStopped);
    paramPrintWriter.print(" mFinished=");
    paramPrintWriter.println(mFinished);
    paramPrintWriter.print((String)localObject);
    paramPrintWriter.print("mChangingConfigurations=");
    paramPrintWriter.println(mChangingConfigurations);
    paramPrintWriter.print((String)localObject);
    paramPrintWriter.print("mCurrentConfig=");
    paramPrintWriter.println(mCurrentConfig);
    mFragments.dumpLoaders((String)localObject, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    mFragments.getFragmentManager().dump((String)localObject, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    if (mVoiceInteractor != null) {
      mVoiceInteractor.dump((String)localObject, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    if ((getWindow() != null) && (getWindow().peekDecorView() != null) && (getWindow().peekDecorView().getViewRootImpl() != null)) {
      getWindow().peekDecorView().getViewRootImpl().dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    mHandler.getLooper().dump(new PrintWriterPrinter(paramPrintWriter), paramString);
    paramFileDescriptor = getAutofillManager();
    if (paramFileDescriptor != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Autofill Compat Mode: ");
      paramPrintWriter.println(isAutofillCompatibilityEnabled());
      paramFileDescriptor.dump(paramString, paramPrintWriter);
    }
    else
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("No AutofillManager");
    }
    ResourcesManager.getInstance().dump(paramString, paramPrintWriter);
  }
  
  @Deprecated
  public void enterPictureInPictureMode()
  {
    enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
  }
  
  @Deprecated
  public boolean enterPictureInPictureMode(PictureInPictureArgs paramPictureInPictureArgs)
  {
    return enterPictureInPictureMode(PictureInPictureArgs.convert(paramPictureInPictureArgs));
  }
  
  public boolean enterPictureInPictureMode(PictureInPictureParams paramPictureInPictureParams)
  {
    try
    {
      if (!deviceSupportsPictureInPictureMode()) {
        return false;
      }
      if (paramPictureInPictureParams != null)
      {
        if (mCanEnterPictureInPicture) {
          return ActivityManagerNative.getDefault().enterPictureInPictureMode(mToken, paramPictureInPictureParams);
        }
        paramPictureInPictureParams = new java/lang/IllegalStateException;
        paramPictureInPictureParams.<init>("Activity must be resumed to enter picture-in-picture");
        throw paramPictureInPictureParams;
      }
      paramPictureInPictureParams = new java/lang/IllegalArgumentException;
      paramPictureInPictureParams.<init>("Expected non-null picture-in-picture params");
      throw paramPictureInPictureParams;
    }
    catch (RemoteException paramPictureInPictureParams) {}
    return false;
  }
  
  public void enterPictureInPictureModeIfPossible()
  {
    if (mActivityInfo.supportsPictureInPicture()) {
      enterPictureInPictureMode();
    }
  }
  
  public void exitFreeformMode()
    throws RemoteException
  {
    ActivityManager.getService().exitFreeformMode(mToken);
  }
  
  public <T extends View> T findViewById(int paramInt)
  {
    return getWindow().findViewById(paramInt);
  }
  
  public void finish()
  {
    finish(0);
  }
  
  public void finishActivity(int paramInt)
  {
    if (mParent == null) {
      try
      {
        ActivityManager.getService().finishSubActivity(mToken, mEmbeddedID, paramInt);
      }
      catch (RemoteException localRemoteException) {}
    } else {
      mParent.finishActivityFromChild(this, paramInt);
    }
  }
  
  public void finishActivityFromChild(Activity paramActivity, int paramInt)
  {
    try
    {
      ActivityManager.getService().finishSubActivity(mToken, mEmbeddedID, paramInt);
    }
    catch (RemoteException paramActivity) {}
  }
  
  public void finishAffinity()
  {
    if (mParent == null)
    {
      if ((mResultCode == 0) && (mResultData == null))
      {
        try
        {
          if (ActivityManager.getService().finishActivityAffinity(mToken)) {
            mFinished = true;
          }
        }
        catch (RemoteException localRemoteException) {}
        return;
      }
      throw new IllegalStateException("Can not be called to deliver a result");
    }
    throw new IllegalStateException("Can not be called from an embedded activity");
  }
  
  public void finishAfterTransition()
  {
    if (!mActivityTransitionState.startExitBackTransition(this)) {
      finish();
    }
  }
  
  public void finishAndRemoveTask()
  {
    finish(1);
  }
  
  public void finishFromChild(Activity paramActivity)
  {
    finish();
  }
  
  public ActionBar getActionBar()
  {
    initWindowDecorActionBar();
    return mActionBar;
  }
  
  ActivityOptions getActivityOptions()
  {
    try
    {
      ActivityOptions localActivityOptions = ActivityOptions.fromBundle(ActivityManager.getService().getActivityOptions(mToken));
      return localActivityOptions;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  @VisibleForTesting
  public final ActivityThread getActivityThread()
  {
    return mMainThread;
  }
  
  public final IBinder getActivityToken()
  {
    IBinder localIBinder;
    if (mParent != null) {
      localIBinder = mParent.getActivityToken();
    } else {
      localIBinder = mToken;
    }
    return localIBinder;
  }
  
  public final Application getApplication()
  {
    return mApplication;
  }
  
  public final AutofillManager.AutofillClient getAutofillClient()
  {
    return this;
  }
  
  public ComponentName getCallingActivity()
  {
    try
    {
      ComponentName localComponentName = ActivityManager.getService().getCallingActivity(mToken);
      return localComponentName;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public String getCallingPackage()
  {
    try
    {
      String str = ActivityManager.getService().getCallingPackage(mToken);
      return str;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public int getChangingConfigurations()
  {
    return mConfigChangeFlags;
  }
  
  public ComponentName getComponentName()
  {
    return mComponent;
  }
  
  public Scene getContentScene()
  {
    return getWindow().getContentScene();
  }
  
  public TransitionManager getContentTransitionManager()
  {
    return getWindow().getTransitionManager();
  }
  
  public View getCurrentFocus()
  {
    View localView;
    if (mWindow != null) {
      localView = mWindow.getCurrentFocus();
    } else {
      localView = null;
    }
    return localView;
  }
  
  @Deprecated
  public FragmentManager getFragmentManager()
  {
    return mFragments.getFragmentManager();
  }
  
  public Intent getIntent()
  {
    return mIntent;
  }
  
  HashMap<String, Object> getLastNonConfigurationChildInstances()
  {
    HashMap localHashMap;
    if (mLastNonConfigurationInstances != null) {
      localHashMap = mLastNonConfigurationInstances.children;
    } else {
      localHashMap = null;
    }
    return localHashMap;
  }
  
  public Object getLastNonConfigurationInstance()
  {
    Object localObject;
    if (mLastNonConfigurationInstances != null) {
      localObject = mLastNonConfigurationInstances.activity;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public LayoutInflater getLayoutInflater()
  {
    return getWindow().getLayoutInflater();
  }
  
  @Deprecated
  public LoaderManager getLoaderManager()
  {
    return mFragments.getLoaderManager();
  }
  
  public String getLocalClassName()
  {
    String str1 = getPackageName();
    String str2 = mComponent.getClassName();
    int i = str1.length();
    if ((str2.startsWith(str1)) && (str2.length() > i) && (str2.charAt(i) == '.')) {
      return str2.substring(i + 1);
    }
    return str2;
  }
  
  public int getMaxNumPictureInPictureActions()
  {
    try
    {
      int i = ActivityManagerNative.getDefault().getMaxNumPictureInPictureActions(mToken);
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return 0;
  }
  
  public final MediaController getMediaController()
  {
    return getWindow().getMediaController();
  }
  
  public MenuInflater getMenuInflater()
  {
    if (mMenuInflater == null)
    {
      initWindowDecorActionBar();
      if (mActionBar != null) {
        mMenuInflater = new MenuInflater(mActionBar.getThemedContext(), this);
      } else {
        mMenuInflater = new MenuInflater(this);
      }
    }
    return mMenuInflater;
  }
  
  public int getNextAutofillId()
  {
    if (mLastAutofillId == 2147483646) {
      mLastAutofillId = 1073741823;
    }
    mLastAutofillId += 1;
    return mLastAutofillId;
  }
  
  public final Activity getParent()
  {
    return mParent;
  }
  
  public Intent getParentActivityIntent()
  {
    String str = mActivityInfo.parentActivityName;
    if (TextUtils.isEmpty(str)) {
      return null;
    }
    Object localObject = new ComponentName(this, str);
    try
    {
      if (getPackageManagergetActivityInfo0parentActivityName == null)
      {
        localObject = Intent.makeMainActivity((ComponentName)localObject);
      }
      else
      {
        Intent localIntent = new android/content/Intent;
        localIntent.<init>();
        localObject = localIntent.setComponent((ComponentName)localObject);
      }
      return localObject;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getParentActivityIntent: bad parentActivityName '");
      localStringBuilder.append(str);
      localStringBuilder.append("' in manifest");
      Log.e("Activity", localStringBuilder.toString());
    }
    return null;
  }
  
  public SharedPreferences getPreferences(int paramInt)
  {
    return getSharedPreferences(getLocalClassName(), paramInt);
  }
  
  public Uri getReferrer()
  {
    Object localObject = getIntent();
    try
    {
      Uri localUri = (Uri)((Intent)localObject).getParcelableExtra("android.intent.extra.REFERRER");
      if (localUri != null) {
        return localUri;
      }
      localObject = ((Intent)localObject).getStringExtra("android.intent.extra.REFERRER_NAME");
      if (localObject != null)
      {
        localObject = Uri.parse((String)localObject);
        return localObject;
      }
    }
    catch (BadParcelableException localBadParcelableException)
    {
      Log.w("Activity", "Cannot read referrer from intent; intent extras contain unknown custom Parcelable objects");
    }
    if (mReferrer != null) {
      return new Uri.Builder().scheme("android-app").authority(mReferrer).build();
    }
    return null;
  }
  
  public int getRequestedOrientation()
  {
    if (mParent == null) {
      try
      {
        int i = ActivityManager.getService().getRequestedOrientation(mToken);
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        return -1;
      }
    }
    return mParent.getRequestedOrientation();
  }
  
  public final SearchEvent getSearchEvent()
  {
    return mSearchEvent;
  }
  
  public Object getSystemService(String paramString)
  {
    if (getBaseContext() != null)
    {
      if ("window".equals(paramString)) {
        return mWindowManager;
      }
      if ("search".equals(paramString))
      {
        ensureSearchManager();
        return mSearchManager;
      }
      return super.getSystemService(paramString);
    }
    throw new IllegalStateException("System services not available to Activities before onCreate()");
  }
  
  public int getTaskId()
  {
    try
    {
      int i = ActivityManager.getService().getTaskForActivity(mToken, false);
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return -1;
  }
  
  public final CharSequence getTitle()
  {
    return mTitle;
  }
  
  public final int getTitleColor()
  {
    return mTitleColor;
  }
  
  public VoiceInteractor getVoiceInteractor()
  {
    return mVoiceInteractor;
  }
  
  public final int getVolumeControlStream()
  {
    return getWindow().getVolumeControlStream();
  }
  
  public Window getWindow()
  {
    return mWindow;
  }
  
  public WindowManager getWindowManager()
  {
    return mWindowManager;
  }
  
  public boolean hasWindowFocus()
  {
    Object localObject = getWindow();
    if (localObject != null)
    {
      localObject = ((Window)localObject).getDecorView();
      if (localObject != null) {
        return ((View)localObject).hasWindowFocus();
      }
    }
    return false;
  }
  
  public void invalidateOptionsMenu()
  {
    if ((mWindow.hasFeature(0)) && ((mActionBar == null) || (!mActionBar.invalidateOptionsMenu()))) {
      mWindow.invalidatePanelMenu(0);
    }
  }
  
  public boolean isActivityTransitionRunning()
  {
    return mActivityTransitionState.isTransitionRunning();
  }
  
  @SystemApi
  @Deprecated
  public boolean isBackgroundVisibleBehind()
  {
    return false;
  }
  
  public boolean isChangingConfigurations()
  {
    return mChangingConfigurations;
  }
  
  public final boolean isChild()
  {
    boolean bool;
    if (mParent != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDestroyed()
  {
    return mDestroyed;
  }
  
  public final boolean isDisablingEnterExitEventForAutofill()
  {
    boolean bool;
    if ((!mAutoFillIgnoreFirstResumePause) && (mResumed)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isFinishing()
  {
    return mFinished;
  }
  
  public boolean isImmersive()
  {
    try
    {
      boolean bool = ActivityManager.getService().isImmersive(mToken);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isInMultiWindowMode()
  {
    try
    {
      boolean bool = ActivityManager.getService().isInMultiWindowMode(mToken);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isInPictureInPictureMode()
  {
    try
    {
      boolean bool = ActivityManager.getService().isInPictureInPictureMode(mToken);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isLocalVoiceInteractionSupported()
  {
    try
    {
      boolean bool = ActivityManager.getService().supportsLocalVoiceInteraction();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isOverlayWithDecorCaptionEnabled()
  {
    return mWindow.isOverlayWithDecorCaptionEnabled();
  }
  
  public final boolean isResumed()
  {
    return mResumed;
  }
  
  public boolean isTaskRoot()
  {
    boolean bool = false;
    try
    {
      int i = ActivityManager.getService().getTaskForActivity(mToken, true);
      if (i >= 0) {
        bool = true;
      }
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isVoiceInteraction()
  {
    boolean bool;
    if (mVoiceInteractor != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVoiceInteractionRoot()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    try
    {
      if (mVoiceInteractor != null)
      {
        boolean bool3 = ActivityManager.getService().isRootVoiceInteraction(mToken);
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  void makeVisible()
  {
    if (!mWindowAdded)
    {
      getWindowManager().addView(mDecor, getWindow().getAttributes());
      mWindowAdded = true;
    }
    mDecor.setVisibility(0);
  }
  
  @Deprecated
  public final Cursor managedQuery(Uri paramUri, String[] paramArrayOfString, String paramString1, String paramString2)
  {
    paramUri = getContentResolver().query(paramUri, paramArrayOfString, paramString1, null, paramString2);
    if (paramUri != null) {
      startManagingCursor(paramUri);
    }
    return paramUri;
  }
  
  @Deprecated
  public final Cursor managedQuery(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    paramUri = getContentResolver().query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    if (paramUri != null) {
      startManagingCursor(paramUri);
    }
    return paramUri;
  }
  
  public boolean moveTaskToBack(boolean paramBoolean)
  {
    try
    {
      paramBoolean = ActivityManager.getService().moveActivityTaskToBack(mToken, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean navigateUpTo(Intent paramIntent)
  {
    if (mParent == null)
    {
      ComponentName localComponentName1 = paramIntent.getComponent();
      ComponentName localComponentName2 = localComponentName1;
      Intent localIntent = paramIntent;
      if (localComponentName1 == null)
      {
        localComponentName2 = paramIntent.resolveActivity(getPackageManager());
        if (localComponentName2 == null) {
          return false;
        }
        localIntent = new Intent(paramIntent);
        localIntent.setComponent(localComponentName2);
      }
      try
      {
        int i = mResultCode;
        paramIntent = mResultData;
        if (paramIntent != null) {
          paramIntent.prepareToLeaveProcess(this);
        }
        try
        {
          localIntent.prepareToLeaveProcess(this);
          boolean bool = ActivityManager.getService().navigateUpTo(mToken, localIntent, i, paramIntent);
          return bool;
        }
        catch (RemoteException paramIntent)
        {
          return false;
        }
        return mParent.navigateUpToFromChild(this, paramIntent);
      }
      finally {}
    }
  }
  
  public boolean navigateUpToFromChild(Activity paramActivity, Intent paramIntent)
  {
    return navigateUpTo(paramIntent);
  }
  
  public void onActionModeFinished(ActionMode paramActionMode) {}
  
  public void onActionModeStarted(ActionMode paramActionMode) {}
  
  public void onActivityReenter(int paramInt, Intent paramIntent) {}
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {}
  
  protected void onApplyThemeResource(Resources.Theme paramTheme, int paramInt, boolean paramBoolean)
  {
    if (mParent == null)
    {
      super.onApplyThemeResource(paramTheme, paramInt, paramBoolean);
    }
    else
    {
      try
      {
        paramTheme.setTo(mParent.getTheme());
      }
      catch (Exception localException) {}
      paramTheme.applyStyle(paramInt, false);
    }
    paramTheme = paramTheme.obtainStyledAttributes(R.styleable.ActivityTaskDescription);
    if (mTaskDescription.getPrimaryColor() == 0)
    {
      paramInt = paramTheme.getColor(1, 0);
      if ((paramInt != 0) && (Color.alpha(paramInt) == 255)) {
        mTaskDescription.setPrimaryColor(paramInt);
      }
    }
    paramInt = paramTheme.getColor(0, 0);
    if ((paramInt != 0) && (Color.alpha(paramInt) == 255)) {
      mTaskDescription.setBackgroundColor(paramInt);
    }
    paramInt = paramTheme.getColor(2, 0);
    if (paramInt != 0) {
      mTaskDescription.setStatusBarColor(paramInt);
    }
    paramInt = paramTheme.getColor(3, 0);
    if (paramInt != 0) {
      mTaskDescription.setNavigationBarColor(paramInt);
    }
    paramTheme.recycle();
    setTaskDescription(mTaskDescription);
  }
  
  @Deprecated
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onAttachedToWindow() {}
  
  public void onBackPressed()
  {
    if ((mActionBar != null) && (mActionBar.collapseActionView())) {
      return;
    }
    FragmentManager localFragmentManager = mFragments.getFragmentManager();
    if ((localFragmentManager.isStateSaved()) || (!localFragmentManager.popBackStackImmediate())) {
      finishAfterTransition();
    }
  }
  
  @SystemApi
  @Deprecated
  public void onBackgroundVisibleBehindChanged(boolean paramBoolean) {}
  
  protected void onChildTitleChanged(Activity paramActivity, CharSequence paramCharSequence) {}
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    mCalled = true;
    int i = getResources().getConfiguration().diff(paramConfiguration);
    Context localContext = getApplicationContext();
    if (localContext != null)
    {
      if ((i & 0x10000000) == 268435456) {
        Typeface.SetAppTypeFace(localContext, localContext.getPackageName());
      }
    }
    else if ((i & 0x10000000) == 268435456) {
      Typeface.SetAppTypeFace(localContext, "android");
    }
    mFragments.dispatchConfigurationChanged(paramConfiguration);
    if (mWindow != null) {
      mWindow.onConfigurationChanged(paramConfiguration);
    }
    if (mActionBar != null) {
      mActionBar.onConfigurationChanged(paramConfiguration);
    }
  }
  
  public void onContentChanged() {}
  
  public boolean onContextItemSelected(MenuItem paramMenuItem)
  {
    if (mParent != null) {
      return mParent.onContextItemSelected(paramMenuItem);
    }
    return false;
  }
  
  public void onContextMenuClosed(Menu paramMenu)
  {
    if (mParent != null) {
      mParent.onContextMenuClosed(paramMenu);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    Object localObject = getApplicationContext();
    if (localObject != null) {
      Typeface.SetAppTypeFace((Context)localObject, ((Context)localObject).getPackageName());
    } else {
      Typeface.SetAppTypeFace((Context)localObject, "android");
    }
    if (mLastNonConfigurationInstances != null) {
      mFragments.restoreLoaderNonConfig(mLastNonConfigurationInstances.loaders);
    }
    if (mActivityInfo.parentActivityName != null) {
      if (mActionBar == null) {
        mEnableDefaultActionBarUp = true;
      } else {
        mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
      }
    }
    boolean bool = false;
    if (paramBundle != null)
    {
      mAutoFillResetNeeded = paramBundle.getBoolean("@android:autofillResetNeeded", false);
      mLastAutofillId = paramBundle.getInt("android:lastAutofillId", 1073741823);
      if (mAutoFillResetNeeded) {
        getAutofillManager().onCreate(paramBundle);
      }
      Parcelable localParcelable = paramBundle.getParcelable("android:fragments");
      FragmentController localFragmentController = mFragments;
      if (mLastNonConfigurationInstances != null) {
        localObject = mLastNonConfigurationInstances.fragments;
      } else {
        localObject = null;
      }
      localFragmentController.restoreAllState(localParcelable, (FragmentManagerNonConfig)localObject);
    }
    mFragments.dispatchCreate();
    getApplication().dispatchActivityCreated(this, paramBundle);
    if (mVoiceInteractor != null) {
      mVoiceInteractor.attachActivity(this);
    }
    if (paramBundle != null) {
      bool = true;
    }
    mRestoredFromBundle = bool;
    mCalled = true;
  }
  
  public void onCreate(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    onCreate(paramBundle);
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {}
  
  public CharSequence onCreateDescription()
  {
    return null;
  }
  
  @Deprecated
  protected Dialog onCreateDialog(int paramInt)
  {
    return null;
  }
  
  @Deprecated
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle)
  {
    return onCreateDialog(paramInt);
  }
  
  public void onCreateNavigateUpTaskStack(TaskStackBuilder paramTaskStackBuilder)
  {
    paramTaskStackBuilder.addParentStack(this);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (mParent != null) {
      return mParent.onCreateOptionsMenu(paramMenu);
    }
    return true;
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    if (paramInt == 0) {
      return onCreateOptionsMenu(paramMenu) | mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
    }
    return false;
  }
  
  public View onCreatePanelView(int paramInt)
  {
    return null;
  }
  
  @Deprecated
  public boolean onCreateThumbnail(Bitmap paramBitmap, Canvas paramCanvas)
  {
    return false;
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    if (!"fragment".equals(paramString)) {
      return onCreateView(paramString, paramContext, paramAttributeSet);
    }
    return mFragments.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return null;
  }
  
  protected void onDestroy()
  {
    mCalled = true;
    ??? = mManagedDialogs;
    int i = 0;
    int j;
    int k;
    if (??? != null)
    {
      j = mManagedDialogs.size();
      for (k = 0; k < j; k++)
      {
        ??? = (ManagedDialog)mManagedDialogs.valueAt(k);
        if (mDialog.isShowing()) {
          mDialog.dismiss();
        }
      }
      mManagedDialogs = null;
    }
    synchronized (mManagedCursors)
    {
      j = mManagedCursors.size();
      for (k = i; k < j; k++)
      {
        ManagedCursor localManagedCursor = (ManagedCursor)mManagedCursors.get(k);
        if (localManagedCursor != null) {
          mCursor.close();
        }
      }
      mManagedCursors.clear();
      if (mSearchManager != null) {
        mSearchManager.stopSearch();
      }
      if (mActionBar != null) {
        mActionBar.onDestroy();
      }
      getApplication().dispatchActivityDestroyed(this);
      return;
    }
  }
  
  public void onDetachedFromWindow() {}
  
  public void onEnterAnimationComplete() {}
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      if (getApplicationInfotargetSdkVersion >= 5) {
        paramKeyEvent.startTracking();
      } else {
        onBackPressed();
      }
      return true;
    }
    if (mDefaultKeyMode == 0) {
      return false;
    }
    Object localObject;
    if (mDefaultKeyMode == 2)
    {
      localObject = getWindow();
      return (((Window)localObject).hasFeature(0)) && (((Window)localObject).performPanelShortcut(0, paramInt, paramKeyEvent, 2));
    }
    if (paramInt == 61) {
      return false;
    }
    int i = 0;
    boolean bool2;
    if ((paramKeyEvent.getRepeatCount() == 0) && (!paramKeyEvent.isSystem()))
    {
      boolean bool1 = TextKeyListener.getInstance().onKeyDown(null, mDefaultKeySsb, paramInt, paramKeyEvent);
      paramInt = i;
      bool2 = bool1;
      if (bool1)
      {
        paramInt = i;
        bool2 = bool1;
        if (mDefaultKeySsb.length() > 0)
        {
          localObject = mDefaultKeySsb.toString();
          paramInt = 1;
          i = mDefaultKeyMode;
          if (i != 1)
          {
            switch (i)
            {
            default: 
              bool2 = bool1;
              break;
            case 4: 
              startSearch((String)localObject, false, null, true);
              bool2 = bool1;
              break;
            case 3: 
              startSearch((String)localObject, false, null, false);
              bool2 = bool1;
              break;
            }
          }
          else
          {
            paramKeyEvent = new StringBuilder();
            paramKeyEvent.append("tel:");
            paramKeyEvent.append((String)localObject);
            paramKeyEvent = new Intent("android.intent.action.DIAL", Uri.parse(paramKeyEvent.toString()));
            paramKeyEvent.addFlags(268435456);
            startActivity(paramKeyEvent);
            bool2 = bool1;
          }
        }
      }
    }
    else
    {
      paramInt = 1;
      bool2 = false;
    }
    if (paramInt != 0)
    {
      mDefaultKeySsb.clear();
      mDefaultKeySsb.clearSpans();
      Selection.setSelection(mDefaultKeySsb, 0);
    }
    return bool2;
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent)
  {
    ActionBar localActionBar = getActionBar();
    boolean bool;
    if ((localActionBar != null) && (localActionBar.onKeyShortcut(paramInt, paramKeyEvent))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((getApplicationInfotargetSdkVersion >= 5) && (paramInt == 4) && (paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
    {
      onBackPressed();
      return true;
    }
    return false;
  }
  
  public void onLocalVoiceInteractionStarted() {}
  
  public void onLocalVoiceInteractionStopped() {}
  
  public void onLowMemory()
  {
    mCalled = true;
    mFragments.dispatchLowMemory();
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    CharSequence localCharSequence = paramMenuItem.getTitleCondensed();
    if (paramInt != 0)
    {
      if (paramInt != 6) {
        return false;
      }
      if (localCharSequence != null) {
        EventLog.writeEvent(50000, new Object[] { Integer.valueOf(1), localCharSequence.toString() });
      }
      if (onContextItemSelected(paramMenuItem)) {
        return true;
      }
      return mFragments.dispatchContextItemSelected(paramMenuItem);
    }
    if (localCharSequence != null) {
      EventLog.writeEvent(50000, new Object[] { Integer.valueOf(0), localCharSequence.toString() });
    }
    if (onOptionsItemSelected(paramMenuItem)) {
      return true;
    }
    if (mFragments.dispatchOptionsItemSelected(paramMenuItem)) {
      return true;
    }
    if ((paramMenuItem.getItemId() == 16908332) && (mActionBar != null) && ((mActionBar.getDisplayOptions() & 0x4) != 0))
    {
      if (mParent == null) {
        return onNavigateUp();
      }
      return mParent.onNavigateUpFromChild(this);
    }
    return false;
  }
  
  public boolean onMenuOpened(int paramInt, Menu paramMenu)
  {
    if (paramInt == 8)
    {
      initWindowDecorActionBar();
      if (mActionBar != null) {
        mActionBar.dispatchMenuVisibilityChanged(true);
      } else {
        Log.e("Activity", "Tried to open action bar menu with no action bar");
      }
    }
    return true;
  }
  
  public void onMovedToDisplay(int paramInt, Configuration paramConfiguration) {}
  
  @Deprecated
  public void onMultiWindowModeChanged(boolean paramBoolean) {}
  
  public void onMultiWindowModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onMultiWindowModeChanged(paramBoolean);
  }
  
  public boolean onNavigateUp()
  {
    Object localObject = getParentActivityIntent();
    if (localObject != null)
    {
      if (mActivityInfo.taskAffinity == null)
      {
        finish();
      }
      else if (shouldUpRecreateTask((Intent)localObject))
      {
        localObject = TaskStackBuilder.create(this);
        onCreateNavigateUpTaskStack((TaskStackBuilder)localObject);
        onPrepareNavigateUpTaskStack((TaskStackBuilder)localObject);
        ((TaskStackBuilder)localObject).startActivities();
        if ((mResultCode == 0) && (mResultData == null))
        {
          finishAffinity();
        }
        else
        {
          Log.i("Activity", "onNavigateUp only finishing topmost activity to return a result");
          finish();
        }
      }
      else
      {
        navigateUpTo((Intent)localObject);
      }
      return true;
    }
    return false;
  }
  
  public boolean onNavigateUpFromChild(Activity paramActivity)
  {
    return onNavigateUp();
  }
  
  public void onNewActivityOptions(ActivityOptions paramActivityOptions)
  {
    mActivityTransitionState.setEnterActivityOptions(this, paramActivityOptions);
    if (!mStopped) {
      mActivityTransitionState.enterReady(this);
    }
  }
  
  protected void onNewIntent(Intent paramIntent) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (mParent != null) {
      return mParent.onOptionsItemSelected(paramMenuItem);
    }
    return false;
  }
  
  public void onOptionsMenuClosed(Menu paramMenu)
  {
    if (mParent != null) {
      mParent.onOptionsMenuClosed(paramMenu);
    }
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu)
  {
    if (paramInt != 0)
    {
      if (paramInt != 6)
      {
        if (paramInt == 8)
        {
          initWindowDecorActionBar();
          mActionBar.dispatchMenuVisibilityChanged(false);
        }
      }
      else {
        onContextMenuClosed(paramMenu);
      }
    }
    else
    {
      mFragments.dispatchOptionsMenuClosed(paramMenu);
      onOptionsMenuClosed(paramMenu);
    }
  }
  
  protected void onPause()
  {
    getApplication().dispatchActivityPaused(this);
    if (mAutoFillResetNeeded) {
      if (!mAutoFillIgnoreFirstResumePause)
      {
        View localView = getCurrentFocus();
        if ((localView != null) && (localView.canNotifyAutofillEnterExitEvent())) {
          getAutofillManager().notifyViewExited(localView);
        }
      }
      else
      {
        mAutoFillIgnoreFirstResumePause = false;
      }
    }
    mCalled = true;
  }
  
  @Deprecated
  public void onPictureInPictureModeChanged(boolean paramBoolean) {}
  
  public void onPictureInPictureModeChanged(boolean paramBoolean, Configuration paramConfiguration)
  {
    onPictureInPictureModeChanged(paramBoolean);
  }
  
  protected void onPostCreate(Bundle paramBundle)
  {
    if (!isChild())
    {
      mTitleReady = true;
      onTitleChanged(getTitle(), getTitleColor());
    }
    mCalled = true;
  }
  
  public void onPostCreate(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    onPostCreate(paramBundle);
  }
  
  protected void onPostResume()
  {
    Window localWindow = getWindow();
    if (localWindow != null) {
      localWindow.makeActive();
    }
    if (mActionBar != null) {
      mActionBar.setShowHideAnimationEnabled(true);
    }
    mCalled = true;
  }
  
  @Deprecated
  protected void onPrepareDialog(int paramInt, Dialog paramDialog)
  {
    paramDialog.setOwnerActivity(this);
  }
  
  @Deprecated
  protected void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle)
  {
    onPrepareDialog(paramInt, paramDialog);
  }
  
  public void onPrepareNavigateUpTaskStack(TaskStackBuilder paramTaskStackBuilder) {}
  
  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    if (mParent != null) {
      return mParent.onPrepareOptionsMenu(paramMenu);
    }
    return true;
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    if ((paramInt == 0) && (paramMenu != null)) {
      return onPrepareOptionsMenu(paramMenu) | mFragments.dispatchPrepareOptionsMenu(paramMenu);
    }
    return true;
  }
  
  public void onProvideAssistContent(AssistContent paramAssistContent) {}
  
  public void onProvideAssistData(Bundle paramBundle) {}
  
  public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> paramList, Menu paramMenu, int paramInt)
  {
    if (paramMenu == null) {
      return;
    }
    Object localObject1 = null;
    int i = paramMenu.size();
    paramInt = 0;
    while (paramInt < i)
    {
      Object localObject2 = paramMenu.getItem(paramInt);
      CharSequence localCharSequence = ((MenuItem)localObject2).getTitle();
      char c = ((MenuItem)localObject2).getAlphabeticShortcut();
      int j = ((MenuItem)localObject2).getAlphabeticModifiers();
      localObject2 = localObject1;
      if (localCharSequence != null)
      {
        localObject2 = localObject1;
        if (c != 0)
        {
          localObject2 = localObject1;
          if (localObject1 == null)
          {
            int k = mApplication.getApplicationInfo().labelRes;
            if (k != 0) {
              localObject1 = getString(k);
            } else {
              localObject1 = null;
            }
            localObject2 = new KeyboardShortcutGroup((CharSequence)localObject1);
          }
          ((KeyboardShortcutGroup)localObject2).addItem(new KeyboardShortcutInfo(localCharSequence, c, j));
        }
      }
      paramInt++;
      localObject1 = localObject2;
    }
    if (localObject1 != null) {
      paramList.add(localObject1);
    }
  }
  
  public Uri onProvideReferrer()
  {
    return null;
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {}
  
  protected void onRestart()
  {
    mCalled = true;
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    if (mWindow != null)
    {
      paramBundle = paramBundle.getBundle("android:viewHierarchyState");
      if (paramBundle != null) {
        mWindow.restoreHierarchyState(paramBundle);
      }
    }
  }
  
  public void onRestoreInstanceState(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    if (paramBundle != null) {
      onRestoreInstanceState(paramBundle);
    }
  }
  
  protected void onResume()
  {
    getApplication().dispatchActivityResumed(this);
    mActivityTransitionState.onResume(this, isTopOfTask());
    if ((mAutoFillResetNeeded) && (!mAutoFillIgnoreFirstResumePause))
    {
      View localView = getCurrentFocus();
      if ((localView != null) && (localView.canNotifyAutofillEnterExitEvent())) {
        getAutofillManager().notifyViewEntered(localView);
      }
    }
    mCalled = true;
  }
  
  HashMap<String, Object> onRetainNonConfigurationChildInstances()
  {
    return null;
  }
  
  public Object onRetainNonConfigurationInstance()
  {
    return null;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putBundle("android:viewHierarchyState", mWindow.saveHierarchyState());
    paramBundle.putInt("android:lastAutofillId", mLastAutofillId);
    Parcelable localParcelable = mFragments.saveAllState();
    if (localParcelable != null) {
      paramBundle.putParcelable("android:fragments", localParcelable);
    }
    if (mAutoFillResetNeeded)
    {
      paramBundle.putBoolean("@android:autofillResetNeeded", true);
      getAutofillManager().onSaveInstanceState(paramBundle);
    }
    getApplication().dispatchActivitySaveInstanceState(this, paramBundle);
  }
  
  public void onSaveInstanceState(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    onSaveInstanceState(paramBundle);
  }
  
  public boolean onSearchRequested()
  {
    int i = getResourcesgetConfigurationuiMode & 0xF;
    if ((i != 4) && (i != 6))
    {
      startSearch(null, false, null, false);
      return true;
    }
    return false;
  }
  
  public boolean onSearchRequested(SearchEvent paramSearchEvent)
  {
    mSearchEvent = paramSearchEvent;
    boolean bool = onSearchRequested();
    mSearchEvent = null;
    return bool;
  }
  
  protected void onStart()
  {
    mCalled = true;
    mFragments.doLoaderStart();
    getApplication().dispatchActivityStarted(this);
    if (mAutoFillResetNeeded) {
      getAutofillManager().onVisibleForAutofill();
    }
  }
  
  public void onStateNotSaved() {}
  
  protected void onStop()
  {
    if (mActionBar != null) {
      mActionBar.setShowHideAnimationEnabled(false);
    }
    mActivityTransitionState.onStop();
    getApplication().dispatchActivityStopped(this);
    mTranslucentCallback = null;
    mCalled = true;
    if (mAutoFillResetNeeded) {
      getAutofillManager().onInvisibleForAutofill();
    }
    if (isFinishing()) {
      if (mAutoFillResetNeeded) {
        getAutofillManager().onActivityFinishing();
      } else if ((mIntent != null) && (mIntent.hasExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN"))) {
        getAutofillManager().onPendingSaveUi(1, mIntent.getIBinderExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN"));
      }
    }
  }
  
  protected void onTitleChanged(CharSequence paramCharSequence, int paramInt)
  {
    if (mTitleReady)
    {
      Window localWindow = getWindow();
      if (localWindow != null)
      {
        localWindow.setTitle(paramCharSequence);
        if (paramInt != 0) {
          localWindow.setTitleColor(paramInt);
        }
      }
      if (mActionBar != null) {
        mActionBar.setWindowTitle(paramCharSequence);
      }
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mWindow.shouldCloseOnTouch(this, paramMotionEvent))
    {
      finish();
      return true;
    }
    return false;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  void onTranslucentConversionComplete(boolean paramBoolean)
  {
    if (mTranslucentCallback != null)
    {
      mTranslucentCallback.onTranslucentConversionComplete(paramBoolean);
      mTranslucentCallback = null;
    }
    if (mChangeCanvasToTranslucent) {
      WindowManagerGlobal.getInstance().changeCanvasOpacity(mToken, false);
    }
  }
  
  public void onTrimMemory(int paramInt)
  {
    mCalled = true;
    mFragments.dispatchTrimMemory(paramInt);
  }
  
  public void onUserInteraction() {}
  
  protected void onUserLeaveHint() {}
  
  @Deprecated
  public void onVisibleBehindCanceled()
  {
    mCalled = true;
  }
  
  public void onWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams)
  {
    if (mParent == null)
    {
      View localView = mDecor;
      if ((localView != null) && (localView.getParent() != null)) {
        getWindowManager().updateViewLayout(localView, paramLayoutParams);
      }
    }
  }
  
  public void onWindowDismissed(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1) {
      i = 2;
    } else {
      i = 0;
    }
    finish(i);
    if (paramBoolean2) {
      overridePendingTransition(0, 0);
    }
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {}
  
  public ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback)
  {
    if (mActionModeTypeStarting == 0)
    {
      initWindowDecorActionBar();
      if (mActionBar != null) {
        return mActionBar.startActionMode(paramCallback);
      }
    }
    return null;
  }
  
  public ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback, int paramInt)
  {
    try
    {
      mActionModeTypeStarting = paramInt;
      paramCallback = onWindowStartingActionMode(paramCallback);
      return paramCallback;
    }
    finally
    {
      mActionModeTypeStarting = 0;
    }
  }
  
  public void openContextMenu(View paramView)
  {
    paramView.showContextMenu();
  }
  
  public void openOptionsMenu()
  {
    if ((mWindow.hasFeature(0)) && ((mActionBar == null) || (!mActionBar.openOptionsMenu()))) {
      mWindow.openPanel(0, null);
    }
  }
  
  public void overridePendingTransition(int paramInt1, int paramInt2)
  {
    try
    {
      ActivityManager.getService().overridePendingTransition(mToken, getPackageName(), paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  final void performCreate(Bundle paramBundle)
  {
    performCreate(paramBundle, null);
  }
  
  final void performCreate(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    mCanEnterPictureInPicture = true;
    restoreHasCurrentPermissionRequest(paramBundle);
    if (paramPersistableBundle != null) {
      onCreate(paramBundle, paramPersistableBundle);
    } else {
      onCreate(paramBundle);
    }
    writeEventLog(30057, "performCreate");
    mActivityTransitionState.readState(paramBundle);
    mVisibleFromClient = (true ^ mWindow.getWindowStyle().getBoolean(10, false));
    mFragments.dispatchActivityCreated();
    mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
  }
  
  final void performDestroy()
  {
    mDestroyed = true;
    mWindow.destroy();
    mFragments.dispatchDestroy();
    onDestroy();
    writeEventLog(30060, "performDestroy");
    mFragments.doLoaderDestroy();
    if (mVoiceInteractor != null) {
      mVoiceInteractor.detachActivity();
    }
  }
  
  final void performNewIntent(Intent paramIntent)
  {
    mCanEnterPictureInPicture = true;
    onNewIntent(paramIntent);
  }
  
  final void performPause()
  {
    mDoReportFullyDrawn = false;
    mFragments.dispatchPause();
    mCalled = false;
    onPause();
    writeEventLog(30021, "performPause");
    mResumed = false;
    if ((!mCalled) && (getApplicationInfotargetSdkVersion >= 9))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Activity ");
      localStringBuilder.append(mComponent.toShortString());
      localStringBuilder.append(" did not call through to super.onPause()");
      throw new SuperNotCalledException(localStringBuilder.toString());
    }
  }
  
  final void performRestart(boolean paramBoolean, String paramString)
  {
    mCanEnterPictureInPicture = true;
    mFragments.noteStateNotSaved();
    if ((mToken != null) && (mParent == null)) {
      WindowManagerGlobal.getInstance().setStoppedState(mToken, false);
    }
    if (mStopped)
    {
      mStopped = false;
      synchronized (mManagedCursors)
      {
        int i = mManagedCursors.size();
        for (int j = 0; j < i; j++)
        {
          ManagedCursor localManagedCursor = (ManagedCursor)mManagedCursors.get(j);
          if ((mReleased) || (mUpdated))
          {
            if ((!mCursor.requery()) && (getApplicationInfotargetSdkVersion >= 14))
            {
              paramString = new java/lang/IllegalStateException;
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("trying to requery an already closed cursor  ");
              localStringBuilder.append(mCursor);
              paramString.<init>(localStringBuilder.toString());
              throw paramString;
            }
            ManagedCursor.access$202(localManagedCursor, false);
            ManagedCursor.access$302(localManagedCursor, false);
          }
        }
        mCalled = false;
        mInstrumentation.callActivityOnRestart(this);
        writeEventLog(30058, paramString);
        if (mCalled)
        {
          if (paramBoolean) {
            performStart(paramString);
          }
        }
        else
        {
          paramString = new StringBuilder();
          paramString.append("Activity ");
          paramString.append(mComponent.toShortString());
          paramString.append(" did not call through to super.onRestart()");
          throw new SuperNotCalledException(paramString.toString());
        }
      }
    }
  }
  
  final void performRestoreInstanceState(Bundle paramBundle)
  {
    onRestoreInstanceState(paramBundle);
    restoreManagedDialogs(paramBundle);
  }
  
  final void performRestoreInstanceState(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    onRestoreInstanceState(paramBundle, paramPersistableBundle);
    if (paramBundle != null) {
      restoreManagedDialogs(paramBundle);
    }
  }
  
  final void performResume(boolean paramBoolean, String paramString)
  {
    performRestart(true, paramString);
    mFragments.execPendingActions();
    mLastNonConfigurationInstances = null;
    if (mAutoFillResetNeeded)
    {
      mAutoFillIgnoreFirstResumePause = paramBoolean;
      paramBoolean = mAutoFillIgnoreFirstResumePause;
    }
    mCalled = false;
    mInstrumentation.callActivityOnResume(this);
    writeEventLog(30022, paramString);
    if (mCalled)
    {
      if ((!mVisibleFromClient) && (!mFinished))
      {
        Log.w("Activity", "An activity without a UI must call finish() before onResume() completes");
        if (getApplicationInfotargetSdkVersion > 22)
        {
          paramString = new StringBuilder();
          paramString.append("Activity ");
          paramString.append(mComponent.toShortString());
          paramString.append(" did not call finish() prior to onResume() completing");
          throw new IllegalStateException(paramString.toString());
        }
      }
      mCalled = false;
      mFragments.dispatchResume();
      mFragments.execPendingActions();
      onPostResume();
      if (mCalled) {
        return;
      }
      paramString = new StringBuilder();
      paramString.append("Activity ");
      paramString.append(mComponent.toShortString());
      paramString.append(" did not call through to super.onPostResume()");
      throw new SuperNotCalledException(paramString.toString());
    }
    paramString = new StringBuilder();
    paramString.append("Activity ");
    paramString.append(mComponent.toShortString());
    paramString.append(" did not call through to super.onResume()");
    throw new SuperNotCalledException(paramString.toString());
  }
  
  final void performSaveInstanceState(Bundle paramBundle)
  {
    onSaveInstanceState(paramBundle);
    saveManagedDialogs(paramBundle);
    mActivityTransitionState.saveState(paramBundle);
    storeHasCurrentPermissionRequest(paramBundle);
  }
  
  final void performSaveInstanceState(Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    onSaveInstanceState(paramBundle, paramPersistableBundle);
    saveManagedDialogs(paramBundle);
    storeHasCurrentPermissionRequest(paramBundle);
  }
  
  final void performStart(String paramString)
  {
    mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
    mFragments.noteStateNotSaved();
    mCalled = false;
    mFragments.execPendingActions();
    mInstrumentation.callActivityOnStart(this);
    writeEventLog(30059, paramString);
    if (mCalled)
    {
      mFragments.dispatchStart();
      mFragments.reportLoaderStart();
      int i;
      if ((mApplication.getApplicationInfo().flags & 0x2) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (SystemProperties.getInt("ro.bionic.ld.warning", 0) == 1) {
        j = 1;
      } else {
        j = 0;
      }
      String str;
      if ((i != 0) || (j != 0))
      {
        str = getDlWarning();
        if (str != null)
        {
          paramString = getApplicationInfo().loadLabel(getPackageManager()).toString();
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Detected problems with app native libraries\n(please consult log for detail):\n");
          localStringBuilder.append(str);
          str = localStringBuilder.toString();
          if (i != 0)
          {
            new AlertDialog.Builder(this).setTitle(paramString).setMessage(str).setPositiveButton(17039370, null).setCancelable(false).show();
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append(paramString);
            localStringBuilder.append("\n");
            localStringBuilder.append(str);
            Toast.makeText(this, localStringBuilder.toString(), 1).show();
          }
        }
      }
      if (SystemProperties.getInt("ro.art.hiddenapi.warning", 0) == 1) {
        j = 1;
      } else {
        j = 0;
      }
      if (((i != 0) || (j != 0)) && (!mMainThread.mHiddenApiWarningShown) && (VMRuntime.getRuntime().hasUsedHiddenApi()))
      {
        mMainThread.mHiddenApiWarningShown = true;
        str = getApplicationInfo().loadLabel(getPackageManager()).toString();
        if (i != 0)
        {
          new AlertDialog.Builder(this).setTitle(str).setMessage("Detected problems with API compatibility\n(visit g.co/dev/appcompat for more info)").setPositiveButton(17039370, null).setCancelable(false).show();
        }
        else
        {
          paramString = new StringBuilder();
          paramString.append(str);
          paramString.append("\n");
          paramString.append("Detected problems with API compatibility\n(visit g.co/dev/appcompat for more info)");
          Toast.makeText(this, paramString.toString(), 1).show();
        }
      }
      mActivityTransitionState.enterReady(this);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Activity ");
    paramString.append(mComponent.toShortString());
    paramString.append(" did not call through to super.onStart()");
    throw new SuperNotCalledException(paramString.toString());
  }
  
  final void performStop(boolean paramBoolean, String arg2)
  {
    mDoReportFullyDrawn = false;
    mFragments.doLoaderStop(mChangingConfigurations);
    mCanEnterPictureInPicture = false;
    if (!mStopped)
    {
      if (mWindow != null) {
        mWindow.closeAllPanels();
      }
      if ((!paramBoolean) && (mToken != null) && (mParent == null)) {
        WindowManagerGlobal.getInstance().setStoppedState(mToken, true);
      }
      mFragments.dispatchStop();
      mCalled = false;
      mInstrumentation.callActivityOnStop(this);
      writeEventLog(30049, ???);
      if (mCalled) {
        synchronized (mManagedCursors)
        {
          int i = mManagedCursors.size();
          for (int j = 0; j < i; j++)
          {
            ManagedCursor localManagedCursor = (ManagedCursor)mManagedCursors.get(j);
            if (!mReleased)
            {
              mCursor.deactivate();
              ManagedCursor.access$202(localManagedCursor, true);
            }
          }
          mStopped = true;
        }
      }
      ??? = new StringBuilder();
      ???.append("Activity ");
      ???.append(mComponent.toShortString());
      ???.append(" did not call through to super.onStop()");
      throw new SuperNotCalledException(???.toString());
    }
    mResumed = false;
  }
  
  final void performUserLeaving()
  {
    onUserInteraction();
    onUserLeaveHint();
  }
  
  public void postponeEnterTransition()
  {
    mActivityTransitionState.postponeEnterTransition();
  }
  
  public void recreate()
  {
    if (mParent == null)
    {
      if (Looper.myLooper() == mMainThread.getLooper())
      {
        mMainThread.scheduleRelaunchActivity(mToken);
        return;
      }
      throw new IllegalStateException("Must be called from main thread");
    }
    throw new IllegalStateException("Can only be called on top-level activity");
  }
  
  public void registerForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(this);
  }
  
  public void registerRemoteAnimations(RemoteAnimationDefinition paramRemoteAnimationDefinition)
  {
    try
    {
      ActivityManager.getService().registerRemoteAnimations(mToken, paramRemoteAnimationDefinition);
    }
    catch (RemoteException paramRemoteAnimationDefinition)
    {
      Log.e("Activity", "Failed to call registerRemoteAnimations", paramRemoteAnimationDefinition);
    }
  }
  
  public boolean releaseInstance()
  {
    try
    {
      boolean bool = ActivityManager.getService().releaseActivityInstance(mToken);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  @Deprecated
  public final void removeDialog(int paramInt)
  {
    if (mManagedDialogs != null)
    {
      ManagedDialog localManagedDialog = (ManagedDialog)mManagedDialogs.get(paramInt);
      if (localManagedDialog != null)
      {
        mDialog.dismiss();
        mManagedDialogs.remove(paramInt);
      }
    }
  }
  
  public void reportFullyDrawn()
  {
    if (mDoReportFullyDrawn)
    {
      mDoReportFullyDrawn = false;
      try
      {
        ActivityManager.getService().reportActivityFullyDrawn(mToken, mRestoredFromBundle);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public DragAndDropPermissions requestDragAndDropPermissions(DragEvent paramDragEvent)
  {
    paramDragEvent = DragAndDropPermissions.obtain(paramDragEvent);
    if ((paramDragEvent != null) && (paramDragEvent.take(getActivityToken()))) {
      return paramDragEvent;
    }
    return null;
  }
  
  public final void requestPermissions(String[] paramArrayOfString, int paramInt)
  {
    if (paramInt >= 0)
    {
      if (mHasCurrentPermissionsRequest)
      {
        Log.w("Activity", "Can request only one set of permissions at a time");
        onRequestPermissionsResult(paramInt, new String[0], new int[0]);
        return;
      }
      startActivityForResult("@android:requestPermissions:", getPackageManager().buildRequestPermissionsIntent(paramArrayOfString), paramInt, null);
      mHasCurrentPermissionsRequest = true;
      return;
    }
    throw new IllegalArgumentException("requestCode should be >= 0");
  }
  
  public final void requestShowKeyboardShortcuts()
  {
    Intent localIntent = new Intent("com.android.intent.action.SHOW_KEYBOARD_SHORTCUTS");
    localIntent.setPackage("com.android.systemui");
    sendBroadcastAsUser(localIntent, UserHandle.SYSTEM);
  }
  
  @Deprecated
  public boolean requestVisibleBehind(boolean paramBoolean)
  {
    return false;
  }
  
  public final boolean requestWindowFeature(int paramInt)
  {
    return getWindow().requestFeature(paramInt);
  }
  
  public final <T extends View> T requireViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    if (localView != null) {
      return localView;
    }
    throw new IllegalArgumentException("ID does not reference a View inside this Activity");
  }
  
  NonConfigurationInstances retainNonConfigurationInstances()
  {
    Object localObject = onRetainNonConfigurationInstance();
    HashMap localHashMap = onRetainNonConfigurationChildInstances();
    FragmentManagerNonConfig localFragmentManagerNonConfig = mFragments.retainNestedNonConfig();
    mFragments.doLoaderStart();
    mFragments.doLoaderStop(true);
    ArrayMap localArrayMap = mFragments.retainLoaderNonConfig();
    if ((localObject == null) && (localHashMap == null) && (localFragmentManagerNonConfig == null) && (localArrayMap == null) && (mVoiceInteractor == null)) {
      return null;
    }
    NonConfigurationInstances localNonConfigurationInstances = new NonConfigurationInstances();
    activity = localObject;
    children = localHashMap;
    fragments = localFragmentManagerNonConfig;
    loaders = localArrayMap;
    if (mVoiceInteractor != null)
    {
      mVoiceInteractor.retainInstance();
      voiceInteractor = mVoiceInteractor;
    }
    return localNonConfigurationInstances;
  }
  
  public final void runOnUiThread(Runnable paramRunnable)
  {
    if (Thread.currentThread() != mUiThread) {
      mHandler.post(paramRunnable);
    } else {
      paramRunnable.run();
    }
  }
  
  public void setActionBar(Toolbar paramToolbar)
  {
    ActionBar localActionBar = getActionBar();
    if (!(localActionBar instanceof WindowDecorActionBar))
    {
      mMenuInflater = null;
      if (localActionBar != null) {
        localActionBar.onDestroy();
      }
      if (paramToolbar != null)
      {
        paramToolbar = new ToolbarActionBar(paramToolbar, getTitle(), this);
        mActionBar = paramToolbar;
        mWindow.setCallback(paramToolbar.getWrappedWindowCallback());
      }
      else
      {
        mActionBar = null;
        mWindow.setCallback(this);
      }
      invalidateOptionsMenu();
      return;
    }
    throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set android:windowActionBar to false in your theme to use a Toolbar instead.");
  }
  
  public void setContentTransitionManager(TransitionManager paramTransitionManager)
  {
    getWindow().setTransitionManager(paramTransitionManager);
  }
  
  public void setContentView(int paramInt)
  {
    getWindow().setContentView(paramInt);
    initWindowDecorActionBar();
  }
  
  public void setContentView(View paramView)
  {
    getWindow().setContentView(paramView);
    initWindowDecorActionBar();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    getWindow().setContentView(paramView, paramLayoutParams);
    initWindowDecorActionBar();
  }
  
  public final void setDefaultKeyMode(int paramInt)
  {
    mDefaultKeyMode = paramInt;
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException();
    case 1: 
    case 3: 
    case 4: 
      mDefaultKeySsb = new SpannableStringBuilder();
      Selection.setSelection(mDefaultKeySsb, 0);
      break;
    case 0: 
    case 2: 
      mDefaultKeySsb = null;
    }
  }
  
  public void setDisablePreviewScreenshots(boolean paramBoolean)
  {
    try
    {
      ActivityManager.getService().setDisablePreviewScreenshots(mToken, paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("Activity", "Failed to call setDisablePreviewScreenshots", localRemoteException);
    }
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback localSharedElementCallback = paramSharedElementCallback;
    if (paramSharedElementCallback == null) {
      localSharedElementCallback = SharedElementCallback.NULL_CALLBACK;
    }
    mEnterTransitionListener = localSharedElementCallback;
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback)
  {
    SharedElementCallback localSharedElementCallback = paramSharedElementCallback;
    if (paramSharedElementCallback == null) {
      localSharedElementCallback = SharedElementCallback.NULL_CALLBACK;
    }
    mExitTransitionListener = localSharedElementCallback;
  }
  
  public final void setFeatureDrawable(int paramInt, Drawable paramDrawable)
  {
    getWindow().setFeatureDrawable(paramInt, paramDrawable);
  }
  
  public final void setFeatureDrawableAlpha(int paramInt1, int paramInt2)
  {
    getWindow().setFeatureDrawableAlpha(paramInt1, paramInt2);
  }
  
  public final void setFeatureDrawableResource(int paramInt1, int paramInt2)
  {
    getWindow().setFeatureDrawableResource(paramInt1, paramInt2);
  }
  
  public final void setFeatureDrawableUri(int paramInt, Uri paramUri)
  {
    getWindow().setFeatureDrawableUri(paramInt, paramUri);
  }
  
  public void setFinishOnTouchOutside(boolean paramBoolean)
  {
    mWindow.setCloseOnTouchOutside(paramBoolean);
  }
  
  public void setImmersive(boolean paramBoolean)
  {
    try
    {
      ActivityManager.getService().setImmersive(mToken, paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setIntent(Intent paramIntent)
  {
    mIntent = paramIntent;
  }
  
  public final void setMediaController(MediaController paramMediaController)
  {
    getWindow().setMediaController(paramMediaController);
  }
  
  public void setOverlayWithDecorCaptionEnabled(boolean paramBoolean)
  {
    mWindow.setOverlayWithDecorCaptionEnabled(paramBoolean);
  }
  
  final void setParent(Activity paramActivity)
  {
    mParent = paramActivity;
  }
  
  @Deprecated
  public void setPersistent(boolean paramBoolean) {}
  
  @Deprecated
  public void setPictureInPictureArgs(PictureInPictureArgs paramPictureInPictureArgs)
  {
    setPictureInPictureParams(PictureInPictureArgs.convert(paramPictureInPictureArgs));
  }
  
  public void setPictureInPictureParams(PictureInPictureParams paramPictureInPictureParams)
  {
    try
    {
      if (!deviceSupportsPictureInPictureMode()) {
        return;
      }
      if (paramPictureInPictureParams != null)
      {
        ActivityManagerNative.getDefault().setPictureInPictureParams(mToken, paramPictureInPictureParams);
      }
      else
      {
        paramPictureInPictureParams = new java/lang/IllegalArgumentException;
        paramPictureInPictureParams.<init>("Expected non-null picture-in-picture params");
        throw paramPictureInPictureParams;
      }
    }
    catch (RemoteException paramPictureInPictureParams) {}
  }
  
  @Deprecated
  public final void setProgress(int paramInt)
  {
    getWindow().setFeatureInt(2, paramInt + 0);
  }
  
  @Deprecated
  public final void setProgressBarIndeterminate(boolean paramBoolean)
  {
    Window localWindow = getWindow();
    int i;
    if (paramBoolean) {
      i = -3;
    } else {
      i = -4;
    }
    localWindow.setFeatureInt(2, i);
  }
  
  @Deprecated
  public final void setProgressBarIndeterminateVisibility(boolean paramBoolean)
  {
    Window localWindow = getWindow();
    int i;
    if (paramBoolean) {
      i = -1;
    } else {
      i = -2;
    }
    localWindow.setFeatureInt(5, i);
  }
  
  @Deprecated
  public final void setProgressBarVisibility(boolean paramBoolean)
  {
    Window localWindow = getWindow();
    int i;
    if (paramBoolean) {
      i = -1;
    } else {
      i = -2;
    }
    localWindow.setFeatureInt(2, i);
  }
  
  public void setRequestedOrientation(int paramInt)
  {
    if (mParent == null) {
      try
      {
        ActivityManager.getService().setRequestedOrientation(mToken, paramInt);
      }
      catch (RemoteException localRemoteException) {}
    } else {
      mParent.setRequestedOrientation(paramInt);
    }
  }
  
  public final void setResult(int paramInt)
  {
    try
    {
      mResultCode = paramInt;
      mResultData = null;
      return;
    }
    finally {}
  }
  
  public final void setResult(int paramInt, Intent paramIntent)
  {
    try
    {
      mResultCode = paramInt;
      mResultData = paramIntent;
      return;
    }
    finally {}
  }
  
  @Deprecated
  public final void setSecondaryProgress(int paramInt)
  {
    getWindow().setFeatureInt(2, paramInt + 20000);
  }
  
  public void setShowWhenLocked(boolean paramBoolean)
  {
    try
    {
      ActivityManager.getService().setShowWhenLocked(mToken, paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("Activity", "Failed to call setShowWhenLocked", localRemoteException);
    }
  }
  
  public void setTaskDescription(ActivityManager.TaskDescription paramTaskDescription)
  {
    if (mTaskDescription != paramTaskDescription)
    {
      mTaskDescription.copyFromPreserveHiddenFields(paramTaskDescription);
      if ((paramTaskDescription.getIconFilename() == null) && (paramTaskDescription.getIcon() != null))
      {
        int i = ActivityManager.getLauncherLargeIconSizeInner(this);
        paramTaskDescription = Bitmap.createScaledBitmap(paramTaskDescription.getIcon(), i, i, true);
        mTaskDescription.setIcon(paramTaskDescription);
      }
    }
    try
    {
      ActivityManager.getService().setTaskDescription(mToken, mTaskDescription);
    }
    catch (RemoteException paramTaskDescription) {}
  }
  
  public void setTheme(int paramInt)
  {
    super.setTheme(paramInt);
    mWindow.setTheme(paramInt);
  }
  
  public void setTitle(int paramInt)
  {
    setTitle(getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    onTitleChanged(paramCharSequence, mTitleColor);
    if (mParent != null) {
      mParent.onChildTitleChanged(this, paramCharSequence);
    }
  }
  
  @Deprecated
  public void setTitleColor(int paramInt)
  {
    mTitleColor = paramInt;
    onTitleChanged(mTitle, paramInt);
  }
  
  public void setTurnScreenOn(boolean paramBoolean)
  {
    try
    {
      ActivityManager.getService().setTurnScreenOn(mToken, paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("Activity", "Failed to call setTurnScreenOn", localRemoteException);
    }
  }
  
  public void setVisible(boolean paramBoolean)
  {
    if (mVisibleFromClient != paramBoolean)
    {
      mVisibleFromClient = paramBoolean;
      if (mVisibleFromServer) {
        if (paramBoolean) {
          makeVisible();
        } else {
          mDecor.setVisibility(4);
        }
      }
    }
  }
  
  void setVoiceInteractor(IVoiceInteractor paramIVoiceInteractor)
  {
    if (mVoiceInteractor != null) {
      for (VoiceInteractor.Request localRequest : mVoiceInteractor.getActiveRequests())
      {
        localRequest.cancel();
        localRequest.clear();
      }
    }
    if (paramIVoiceInteractor == null) {
      mVoiceInteractor = null;
    } else {
      mVoiceInteractor = new VoiceInteractor(paramIVoiceInteractor, this, this, Looper.myLooper());
    }
  }
  
  public final void setVolumeControlStream(int paramInt)
  {
    getWindow().setVolumeControlStream(paramInt);
  }
  
  public void setVrModeEnabled(boolean paramBoolean, ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      if (ActivityManager.getService().setVrMode(mToken, paramBoolean, paramComponentName) != 0)
      {
        PackageManager.NameNotFoundException localNameNotFoundException = new android/content/pm/PackageManager$NameNotFoundException;
        localNameNotFoundException.<init>(paramComponentName.flattenToString());
        throw localNameNotFoundException;
      }
    }
    catch (RemoteException paramComponentName) {}
  }
  
  public boolean shouldShowRequestPermissionRationale(String paramString)
  {
    return getPackageManager().shouldShowRequestPermissionRationale(paramString);
  }
  
  public boolean shouldUpRecreateTask(Intent paramIntent)
  {
    try
    {
      PackageManager localPackageManager = getPackageManager();
      ComponentName localComponentName1 = paramIntent.getComponent();
      ComponentName localComponentName2 = localComponentName1;
      if (localComponentName1 == null) {
        localComponentName2 = paramIntent.resolveActivity(localPackageManager);
      }
      paramIntent = localPackageManager.getActivityInfo(localComponentName2, 0);
      if (taskAffinity == null) {
        return false;
      }
      boolean bool = ActivityManager.getService().shouldUpRecreateTask(mToken, taskAffinity);
      return bool;
    }
    catch (PackageManager.NameNotFoundException paramIntent)
    {
      return false;
    }
    catch (RemoteException paramIntent) {}
    return false;
  }
  
  public boolean showAssist(Bundle paramBundle)
  {
    try
    {
      boolean bool = ActivityManager.getService().showAssistFromActivity(mToken, paramBundle);
      return bool;
    }
    catch (RemoteException paramBundle) {}
    return false;
  }
  
  @Deprecated
  public final void showDialog(int paramInt)
  {
    showDialog(paramInt, null);
  }
  
  @Deprecated
  public final boolean showDialog(int paramInt, Bundle paramBundle)
  {
    if (mManagedDialogs == null) {
      mManagedDialogs = new SparseArray();
    }
    ManagedDialog localManagedDialog1 = (ManagedDialog)mManagedDialogs.get(paramInt);
    ManagedDialog localManagedDialog2 = localManagedDialog1;
    if (localManagedDialog1 == null)
    {
      localManagedDialog2 = new ManagedDialog(null);
      mDialog = createDialog(Integer.valueOf(paramInt), null, paramBundle);
      if (mDialog == null) {
        return false;
      }
      mManagedDialogs.put(paramInt, localManagedDialog2);
    }
    mArgs = paramBundle;
    onPrepareDialog(paramInt, mDialog, paramBundle);
    mDialog.show();
    return true;
  }
  
  public void showLockTaskEscapeMessage()
  {
    try
    {
      ActivityManager.getService().showLockTaskEscapeMessage(mToken);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback)
  {
    return mWindow.getDecorView().startActionMode(paramCallback);
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback, int paramInt)
  {
    return mWindow.getDecorView().startActionMode(paramCallback, paramInt);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent)
  {
    startActivities(paramArrayOfIntent, null);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    mInstrumentation.execStartActivities(this, mMainThread.getApplicationThread(), mToken, this, paramArrayOfIntent, paramBundle);
  }
  
  public void startActivity(Intent paramIntent)
  {
    startActivity(paramIntent, null);
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle)
  {
    if (paramBundle != null) {
      startActivityForResult(paramIntent, -1, paramBundle);
    } else {
      startActivityForResult(paramIntent, -1);
    }
  }
  
  public void startActivityAsCaller(Intent paramIntent, Bundle paramBundle, boolean paramBoolean, int paramInt)
  {
    if (mParent == null)
    {
      paramBundle = transferSpringboardActivityOptions(paramBundle);
      paramIntent = mInstrumentation.execStartActivityAsCaller(this, mMainThread.getApplicationThread(), mToken, this, paramIntent, -1, paramBundle, paramBoolean, paramInt);
      if (paramIntent != null) {
        mMainThread.sendActivityResult(mToken, mEmbeddedID, -1, paramIntent.getResultCode(), paramIntent.getResultData());
      }
      cancelInputsAndStartExitTransition(paramBundle);
      return;
    }
    throw new RuntimeException("Can't be called from a child");
  }
  
  public void startActivityAsUser(Intent paramIntent, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (mParent == null)
    {
      paramBundle = transferSpringboardActivityOptions(paramBundle);
      paramIntent = mInstrumentation.execStartActivity(this, mMainThread.getApplicationThread(), mToken, mEmbeddedID, paramIntent, -1, paramBundle, paramUserHandle);
      if (paramIntent != null) {
        mMainThread.sendActivityResult(mToken, mEmbeddedID, -1, paramIntent.getResultCode(), paramIntent.getResultData());
      }
      cancelInputsAndStartExitTransition(paramBundle);
      return;
    }
    throw new RuntimeException("Can't be called from a child");
  }
  
  public void startActivityAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    startActivityAsUser(paramIntent, null, paramUserHandle);
  }
  
  public void startActivityAsUserFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    startActivityForResultAsUser(paramIntent, mWho, paramInt, paramBundle, paramUserHandle);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    startActivityForResult(paramIntent, paramInt, null);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    if (mParent == null)
    {
      paramBundle = transferSpringboardActivityOptions(paramBundle);
      paramIntent = mInstrumentation.execStartActivity(this, mMainThread.getApplicationThread(), mToken, this, paramIntent, paramInt, paramBundle);
      if (paramIntent != null) {
        mMainThread.sendActivityResult(mToken, mEmbeddedID, paramInt, paramIntent.getResultCode(), paramIntent.getResultData());
      }
      if (paramInt >= 0) {
        mStartedActivity = true;
      }
      cancelInputsAndStartExitTransition(paramBundle);
    }
    else if (paramBundle != null)
    {
      mParent.startActivityFromChild(this, paramIntent, paramInt, paramBundle);
    }
    else
    {
      mParent.startActivityFromChild(this, paramIntent, paramInt);
    }
  }
  
  public void startActivityForResult(String paramString, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    Uri localUri = onProvideReferrer();
    if (localUri != null) {
      paramIntent.putExtra("android.intent.extra.REFERRER", localUri);
    }
    paramBundle = transferSpringboardActivityOptions(paramBundle);
    paramIntent = mInstrumentation.execStartActivity(this, mMainThread.getApplicationThread(), mToken, paramString, paramIntent, paramInt, paramBundle);
    if (paramIntent != null) {
      mMainThread.sendActivityResult(mToken, paramString, paramInt, paramIntent.getResultCode(), paramIntent.getResultData());
    }
    cancelInputsAndStartExitTransition(paramBundle);
  }
  
  public void startActivityForResultAsUser(Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    startActivityForResultAsUser(paramIntent, mEmbeddedID, paramInt, paramBundle, paramUserHandle);
  }
  
  public void startActivityForResultAsUser(Intent paramIntent, int paramInt, UserHandle paramUserHandle)
  {
    startActivityForResultAsUser(paramIntent, paramInt, null, paramUserHandle);
  }
  
  public void startActivityForResultAsUser(Intent paramIntent, String paramString, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if (mParent == null)
    {
      paramBundle = transferSpringboardActivityOptions(paramBundle);
      paramIntent = mInstrumentation.execStartActivity(this, mMainThread.getApplicationThread(), mToken, paramString, paramIntent, paramInt, paramBundle, paramUserHandle);
      if (paramIntent != null) {
        mMainThread.sendActivityResult(mToken, mEmbeddedID, paramInt, paramIntent.getResultCode(), paramIntent.getResultData());
      }
      if (paramInt >= 0) {
        mStartedActivity = true;
      }
      cancelInputsAndStartExitTransition(paramBundle);
      return;
    }
    throw new RuntimeException("Can't be called from a child");
  }
  
  public void startActivityFromChild(Activity paramActivity, Intent paramIntent, int paramInt)
  {
    startActivityFromChild(paramActivity, paramIntent, paramInt, null);
  }
  
  public void startActivityFromChild(Activity paramActivity, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    paramBundle = transferSpringboardActivityOptions(paramBundle);
    paramIntent = mInstrumentation.execStartActivity(this, mMainThread.getApplicationThread(), mToken, paramActivity, paramIntent, paramInt, paramBundle);
    if (paramIntent != null) {
      mMainThread.sendActivityResult(mToken, mEmbeddedID, paramInt, paramIntent.getResultCode(), paramIntent.getResultData());
    }
    cancelInputsAndStartExitTransition(paramBundle);
  }
  
  @Deprecated
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    startActivityFromFragment(paramFragment, paramIntent, paramInt, null);
  }
  
  @Deprecated
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    startActivityForResult(mWho, paramIntent, paramInt, paramBundle);
  }
  
  public boolean startActivityIfNeeded(Intent paramIntent, int paramInt)
  {
    return startActivityIfNeeded(paramIntent, paramInt, null);
  }
  
  public boolean startActivityIfNeeded(Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    if (mParent == null)
    {
      boolean bool = true;
      int i = 1;
      try
      {
        Uri localUri = onProvideReferrer();
        if (localUri != null) {
          paramIntent.putExtra("android.intent.extra.REFERRER", localUri);
        }
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(this);
        int j = ActivityManager.getService().startActivity(mMainThread.getApplicationThread(), getBasePackageName(), paramIntent, paramIntent.resolveTypeIfNeeded(getContentResolver()), mToken, mEmbeddedID, paramInt, 1, null, paramBundle);
        i = j;
      }
      catch (RemoteException paramBundle) {}
      Instrumentation.checkStartActivityResult(i, paramIntent);
      if (paramInt >= 0) {
        mStartedActivity = true;
      }
      if (i == 1) {
        bool = false;
      }
      return bool;
    }
    throw new UnsupportedOperationException("startActivityIfNeeded can only be called from a top-level activity");
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3)
    throws IntentSender.SendIntentException
  {
    startIntentSender(paramIntentSender, paramIntent, paramInt1, paramInt2, paramInt3, null);
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    if (paramBundle != null) {
      startIntentSenderForResult(paramIntentSender, -1, paramIntent, paramInt1, paramInt2, paramInt3, paramBundle);
    } else {
      startIntentSenderForResult(paramIntentSender, -1, paramIntent, paramInt1, paramInt2, paramInt3);
    }
  }
  
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4)
    throws IntentSender.SendIntentException
  {
    startIntentSenderForResult(paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, null);
  }
  
  public void startIntentSenderForResult(IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    if (mParent == null) {
      startIntentSenderForResultInner(paramIntentSender, mEmbeddedID, paramInt1, paramIntent, paramInt2, paramInt3, paramBundle);
    } else if (paramBundle != null) {
      mParent.startIntentSenderFromChild(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
    } else {
      mParent.startIntentSenderFromChild(this, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void startIntentSenderFromChild(Activity paramActivity, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4)
    throws IntentSender.SendIntentException
  {
    startIntentSenderFromChild(paramActivity, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, null);
  }
  
  public void startIntentSenderFromChild(Activity paramActivity, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    startIntentSenderForResultInner(paramIntentSender, mEmbeddedID, paramInt1, paramIntent, paramInt2, paramInt3, paramBundle);
  }
  
  public void startIntentSenderFromChildFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    startIntentSenderForResultInner(paramIntentSender, mWho, paramInt1, paramIntent, paramInt2, paramInt3, paramBundle);
  }
  
  public void startLocalVoiceInteraction(Bundle paramBundle)
  {
    try
    {
      ActivityManager.getService().startLocalVoiceInteraction(mToken, paramBundle);
    }
    catch (RemoteException paramBundle) {}
  }
  
  public void startLockTask()
  {
    try
    {
      ActivityManager.getService().startLockTaskModeByToken(mToken);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  @Deprecated
  public void startManagingCursor(Cursor paramCursor)
  {
    synchronized (mManagedCursors)
    {
      ArrayList localArrayList2 = mManagedCursors;
      ManagedCursor localManagedCursor = new android/app/Activity$ManagedCursor;
      localManagedCursor.<init>(paramCursor);
      localArrayList2.add(localManagedCursor);
      return;
    }
  }
  
  public boolean startNextMatchingActivity(Intent paramIntent)
  {
    return startNextMatchingActivity(paramIntent, null);
  }
  
  public boolean startNextMatchingActivity(Intent paramIntent, Bundle paramBundle)
  {
    if (mParent == null) {
      try
      {
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(this);
        boolean bool = ActivityManager.getService().startNextMatchingActivity(mToken, paramIntent, paramBundle);
        return bool;
      }
      catch (RemoteException paramIntent)
      {
        return false;
      }
    }
    throw new UnsupportedOperationException("startNextMatchingActivity can only be called from a top-level activity");
  }
  
  public void startPostponedEnterTransition()
  {
    mActivityTransitionState.startPostponedEnterTransition();
  }
  
  public void startSearch(String paramString, boolean paramBoolean1, Bundle paramBundle, boolean paramBoolean2)
  {
    ensureSearchManager();
    mSearchManager.startSearch(paramString, paramBoolean1, getComponentName(), paramBundle, paramBoolean2);
  }
  
  public void stopLocalVoiceInteraction()
  {
    try
    {
      ActivityManager.getService().stopLocalVoiceInteraction(mToken);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void stopLockTask()
  {
    try
    {
      ActivityManager.getService().stopLockTaskModeByToken(mToken);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  @Deprecated
  public void stopManagingCursor(Cursor paramCursor)
  {
    synchronized (mManagedCursors)
    {
      int i = mManagedCursors.size();
      for (int j = 0; j < i; j++) {
        if (mManagedCursors.get(j)).mCursor == paramCursor)
        {
          mManagedCursors.remove(j);
          break;
        }
      }
      return;
    }
  }
  
  public void takeKeyEvents(boolean paramBoolean)
  {
    getWindow().takeKeyEvents(paramBoolean);
  }
  
  public void triggerSearch(String paramString, Bundle paramBundle)
  {
    ensureSearchManager();
    mSearchManager.triggerSearch(paramString, getComponentName(), paramBundle);
  }
  
  public void unregisterForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(null);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface DefaultKeyMode {}
  
  class HostCallbacks
    extends FragmentHostCallback<Activity>
  {
    public HostCallbacks()
    {
      super();
    }
    
    public void onAttachFragment(Fragment paramFragment)
    {
      Activity.this.onAttachFragment(paramFragment);
    }
    
    public void onDump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    
    public <T extends View> T onFindViewById(int paramInt)
    {
      return findViewById(paramInt);
    }
    
    public Activity onGetHost()
    {
      return Activity.this;
    }
    
    public LayoutInflater onGetLayoutInflater()
    {
      LayoutInflater localLayoutInflater = getLayoutInflater();
      if (onUseFragmentManagerInflaterFactory()) {
        return localLayoutInflater.cloneInContext(Activity.this);
      }
      return localLayoutInflater;
    }
    
    public int onGetWindowAnimations()
    {
      Window localWindow = getWindow();
      int i;
      if (localWindow == null) {
        i = 0;
      } else {
        i = getAttributeswindowAnimations;
      }
      return i;
    }
    
    public boolean onHasView()
    {
      Window localWindow = getWindow();
      boolean bool;
      if ((localWindow != null) && (localWindow.peekDecorView() != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean onHasWindowAnimations()
    {
      boolean bool;
      if (getWindow() != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onInvalidateOptionsMenu()
    {
      invalidateOptionsMenu();
    }
    
    public void onRequestPermissionsFromFragment(Fragment paramFragment, String[] paramArrayOfString, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("@android:requestPermissions:");
      localStringBuilder.append(mWho);
      paramFragment = localStringBuilder.toString();
      paramArrayOfString = getPackageManager().buildRequestPermissionsIntent(paramArrayOfString);
      startActivityForResult(paramFragment, paramArrayOfString, paramInt, null);
    }
    
    public boolean onShouldSaveFragmentState(Fragment paramFragment)
    {
      return isFinishing() ^ true;
    }
    
    public void onStartActivityAsUserFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
    {
      startActivityAsUserFromFragment(paramFragment, paramIntent, paramInt, paramBundle, paramUserHandle);
    }
    
    public void onStartActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt, Bundle paramBundle)
    {
      startActivityFromFragment(paramFragment, paramIntent, paramInt, paramBundle);
    }
    
    public void onStartIntentSenderFromFragment(Fragment paramFragment, IntentSender paramIntentSender, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3, int paramInt4, Bundle paramBundle)
      throws IntentSender.SendIntentException
    {
      if (mParent == null) {
        Activity.this.startIntentSenderForResultInner(paramIntentSender, mWho, paramInt1, paramIntent, paramInt2, paramInt3, paramBundle);
      } else if (paramBundle != null) {
        mParent.startIntentSenderFromChildFragment(paramFragment, paramIntentSender, paramInt1, paramIntent, paramInt2, paramInt3, paramInt4, paramBundle);
      }
    }
    
    public boolean onUseFragmentManagerInflaterFactory()
    {
      boolean bool;
      if (getApplicationInfo().targetSdkVersion >= 21) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  private static final class ManagedCursor
  {
    private final Cursor mCursor;
    private boolean mReleased;
    private boolean mUpdated;
    
    ManagedCursor(Cursor paramCursor)
    {
      mCursor = paramCursor;
      mReleased = false;
      mUpdated = false;
    }
  }
  
  private static class ManagedDialog
  {
    Bundle mArgs;
    Dialog mDialog;
    
    private ManagedDialog() {}
  }
  
  static final class NonConfigurationInstances
  {
    Object activity;
    HashMap<String, Object> children;
    FragmentManagerNonConfig fragments;
    ArrayMap<String, LoaderManager> loaders;
    VoiceInteractor voiceInteractor;
    
    NonConfigurationInstances() {}
  }
  
  @SystemApi
  public static abstract interface TranslucentConversionListener
  {
    public abstract void onTranslucentConversionComplete(boolean paramBoolean);
  }
}
