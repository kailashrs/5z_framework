package android.view.autofill;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.service.autofill.FillEventHistory;
import android.service.autofill.UserData;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityPolicy;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
import sun.misc.Cleaner;

public final class AutofillManager
{
  public static final int ACTION_START_SESSION = 1;
  public static final int ACTION_VALUE_CHANGED = 4;
  public static final int ACTION_VIEW_ENTERED = 2;
  public static final int ACTION_VIEW_EXITED = 3;
  private static final int AUTHENTICATION_ID_DATASET_ID_MASK = 65535;
  private static final int AUTHENTICATION_ID_DATASET_ID_SHIFT = 16;
  public static final int AUTHENTICATION_ID_DATASET_ID_UNDEFINED = 65535;
  public static final String EXTRA_ASSIST_STRUCTURE = "android.view.autofill.extra.ASSIST_STRUCTURE";
  public static final String EXTRA_AUTHENTICATION_RESULT = "android.view.autofill.extra.AUTHENTICATION_RESULT";
  public static final String EXTRA_CLIENT_STATE = "android.view.autofill.extra.CLIENT_STATE";
  public static final String EXTRA_RESTORE_SESSION_TOKEN = "android.view.autofill.extra.RESTORE_SESSION_TOKEN";
  public static final int FC_SERVICE_TIMEOUT = 5000;
  public static final int FLAG_ADD_CLIENT_DEBUG = 2;
  public static final int FLAG_ADD_CLIENT_ENABLED = 1;
  public static final int FLAG_ADD_CLIENT_VERBOSE = 4;
  private static final String LAST_AUTOFILLED_DATA_TAG = "android:lastAutoFilledData";
  public static final int NO_SESSION = Integer.MIN_VALUE;
  public static final int PENDING_UI_OPERATION_CANCEL = 1;
  public static final int PENDING_UI_OPERATION_RESTORE = 2;
  private static final String SESSION_ID_TAG = "android:sessionId";
  public static final int SET_STATE_FLAG_DEBUG = 8;
  public static final int SET_STATE_FLAG_ENABLED = 1;
  public static final int SET_STATE_FLAG_RESET_CLIENT = 4;
  public static final int SET_STATE_FLAG_RESET_SESSION = 2;
  public static final int SET_STATE_FLAG_VERBOSE = 16;
  public static final int STATE_ACTIVE = 1;
  public static final int STATE_DISABLED_BY_SERVICE = 4;
  public static final int STATE_FINISHED = 2;
  public static final int STATE_SHOWING_SAVE_UI = 3;
  private static final String STATE_TAG = "android:state";
  public static final int STATE_UNKNOWN = 0;
  public static final int STATE_UNKNOWN_COMPAT_MODE = 5;
  private static final String TAG = "AutofillManager";
  @GuardedBy("mLock")
  private AutofillCallback mCallback;
  @GuardedBy("mLock")
  private CompatibilityBridge mCompatibilityBridge;
  private final Context mContext;
  @GuardedBy("mLock")
  private boolean mEnabled;
  @GuardedBy("mLock")
  private ArraySet<AutofillId> mEnteredIds;
  @GuardedBy("mLock")
  private ArraySet<AutofillId> mFillableIds;
  private AutofillId mIdShownFillUi;
  @GuardedBy("mLock")
  private ParcelableMap mLastAutofilledData;
  private final Object mLock = new Object();
  private final MetricsLogger mMetricsLogger = new MetricsLogger();
  @GuardedBy("mLock")
  private boolean mOnInvisibleCalled;
  @GuardedBy("mLock")
  private boolean mSaveOnFinish;
  @GuardedBy("mLock")
  private AutofillId mSaveTriggerId;
  private final IAutoFillManager mService;
  @GuardedBy("mLock")
  private IAutoFillManagerClient mServiceClient;
  @GuardedBy("mLock")
  private Cleaner mServiceClientCleaner;
  @GuardedBy("mLock")
  private int mSessionId = Integer.MIN_VALUE;
  @GuardedBy("mLock")
  private int mState = 0;
  @GuardedBy("mLock")
  private TrackedViews mTrackedViews;
  
  public AutofillManager(Context paramContext, IAutoFillManager paramIAutoFillManager)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext, "context cannot be null"));
    mService = paramIAutoFillManager;
  }
  
  @GuardedBy("mLock")
  private void addEnteredIdLocked(AutofillId paramAutofillId)
  {
    if (mEnteredIds == null) {
      mEnteredIds = new ArraySet(1);
    }
    mEnteredIds.add(paramAutofillId);
  }
  
  private void authenticate(int paramInt1, int paramInt2, IntentSender paramIntentSender, Intent paramIntent)
  {
    synchronized (mLock)
    {
      if (paramInt1 == mSessionId)
      {
        AutofillClient localAutofillClient = getClient();
        if (localAutofillClient != null)
        {
          mOnInvisibleCalled = false;
          localAutofillClient.autofillClientAuthenticate(paramInt2, paramIntentSender, paramIntent);
        }
      }
      return;
    }
  }
  
  private void autofill(int paramInt, List<AutofillId> paramList, List<AutofillValue> paramList1)
  {
    int i;
    Object localObject2;
    View[] arrayOfView;
    Object localObject3;
    int j;
    synchronized (mLock)
    {
      if (paramInt != mSessionId) {
        return;
      }
      AutofillClient localAutofillClient = getClient();
      if (localAutofillClient == null) {
        return;
      }
      i = paramList.size();
      localObject2 = null;
      arrayOfView = localAutofillClient.autofillClientFindViewsByAutofillIdTraversal(Helper.toArray(paramList));
      localObject3 = null;
      paramInt = 0;
      j = 0;
      if (j >= i) {}
    }
    try
    {
      localAutofillId = (AutofillId)paramList.get(j);
    }
    finally
    {
      try
      {
        AutofillId localAutofillId;
        AutofillValue localAutofillValue = (AutofillValue)paramList1.get(j);
        localAutofillId.getViewId();
        View localView = arrayOfView[j];
        Object localObject4;
        if (localView == null)
        {
          localObject4 = new java/lang/StringBuilder;
          ((StringBuilder)localObject4).<init>();
          ((StringBuilder)localObject4).append("autofill(): no View with id ");
          ((StringBuilder)localObject4).append(localAutofillId);
          Log.d("AutofillManager", ((StringBuilder)localObject4).toString());
          localObject4 = localObject3;
          if (localObject3 == null)
          {
            localObject4 = new java/util/ArrayList;
            ((ArrayList)localObject4).<init>();
          }
          ((ArrayList)localObject4).add(localAutofillId);
          localObject3 = localObject4;
        }
        else if (localAutofillId.isVirtual())
        {
          localObject4 = localObject2;
          if (localObject2 == null)
          {
            localObject4 = new android/util/ArrayMap;
            ((ArrayMap)localObject4).<init>(1);
          }
          SparseArray localSparseArray = (SparseArray)((ArrayMap)localObject4).get(localView);
          localObject2 = localSparseArray;
          if (localSparseArray == null)
          {
            localObject2 = new android/util/SparseArray;
            ((SparseArray)localObject2).<init>(5);
            ((ArrayMap)localObject4).put(localView, localObject2);
          }
          ((SparseArray)localObject2).put(localAutofillId.getVirtualChildId(), localAutofillValue);
          localObject2 = localObject4;
        }
        else
        {
          if (mLastAutofilledData == null)
          {
            localObject4 = new android/view/autofill/ParcelableMap;
            ((ParcelableMap)localObject4).<init>(i - j);
            mLastAutofilledData = ((ParcelableMap)localObject4);
          }
          mLastAutofilledData.put(localAutofillId, localAutofillValue);
          localView.autofill(localAutofillValue);
          setAutofilledIfValuesIs(localView, localAutofillValue);
          paramInt++;
        }
        j++;
      }
      finally
      {
        int k;
        label528:
        for (;;) {}
      }
      paramList = finally;
      break label528;
      if (localObject3 != null)
      {
        if (Helper.sVerbose)
        {
          paramList = new java/lang/StringBuilder;
          paramList.<init>();
          paramList.append("autofill(): total failed views: ");
          paramList.append(localObject3);
          Log.v("AutofillManager", paramList.toString());
        }
        try
        {
          mService.setAutofillFailure(mSessionId, localObject3, mContext.getUserId());
        }
        catch (RemoteException paramList)
        {
          paramList.rethrowFromSystemServer();
        }
      }
      k = paramInt;
      if (localObject2 != null) {
        for (j = 0;; j++)
        {
          k = paramInt;
          if (j >= ((ArrayMap)localObject2).size()) {
            break;
          }
          paramList = (View)((ArrayMap)localObject2).keyAt(j);
          paramList1 = (SparseArray)((ArrayMap)localObject2).valueAt(j);
          paramList.autofill(paramList1);
          paramInt += paramList1.size();
        }
      }
      mMetricsLogger.write(newLog(913).addTaggedData(914, Integer.valueOf(i)).addTaggedData(915, Integer.valueOf(k)));
      return;
      paramList = finally;
      throw paramList;
    }
  }
  
  @GuardedBy("mLock")
  private void cancelLocked()
  {
    if ((!mEnabled) && (!isActiveLocked())) {
      return;
    }
    cancelSessionLocked();
  }
  
  @GuardedBy("mLock")
  private void cancelSessionLocked()
  {
    if (Helper.sVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("cancelSessionLocked(): ");
      localStringBuilder.append(getStateAsStringLocked());
      Log.v("AutofillManager", localStringBuilder.toString());
    }
    if (!isActiveLocked()) {
      return;
    }
    try
    {
      mService.cancelSession(mSessionId, mContext.getUserId());
      resetSessionLocked(true);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @GuardedBy("mLock")
  private void commitLocked()
  {
    if ((!mEnabled) && (!isActiveLocked())) {
      return;
    }
    finishSessionLocked();
  }
  
  private void dispatchUnhandledKey(int paramInt, AutofillId arg2, KeyEvent paramKeyEvent)
  {
    View localView = findView(???);
    if (localView == null) {
      return;
    }
    synchronized (mLock)
    {
      if (mSessionId == paramInt)
      {
        AutofillClient localAutofillClient = getClient();
        if (localAutofillClient != null) {
          localAutofillClient.autofillClientDispatchUnhandledKey(localView, paramKeyEvent);
        }
      }
      return;
    }
  }
  
  @GuardedBy("mLock")
  private void ensureServiceClientAddedIfNeededLocked()
  {
    if (getClient() == null) {
      return;
    }
    if (mServiceClient == null)
    {
      mServiceClient = new AutofillManagerClient(this);
      try
      {
        int i = mContext.getUserId();
        int j = mService.addClient(mServiceClient, i);
        boolean bool1 = false;
        if ((j & 0x1) != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mEnabled = bool2;
        if ((j & 0x2) != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        Helper.sDebug = bool2;
        boolean bool2 = bool1;
        if ((j & 0x4) != 0) {
          bool2 = true;
        }
        Helper.sVerbose = bool2;
        IAutoFillManager localIAutoFillManager = mService;
        IAutoFillManagerClient localIAutoFillManagerClient = mServiceClient;
        _..Lambda.AutofillManager.V76JiQu509LCUz3_ckpb_nB3JhA localV76JiQu509LCUz3_ckpb_nB3JhA = new android/view/autofill/_$$Lambda$AutofillManager$V76JiQu509LCUz3_ckpb_nB3JhA;
        localV76JiQu509LCUz3_ckpb_nB3JhA.<init>(localIAutoFillManager, localIAutoFillManagerClient, i);
        mServiceClientCleaner = Cleaner.create(this, localV76JiQu509LCUz3_ckpb_nB3JhA);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  private View findView(AutofillId paramAutofillId)
  {
    AutofillClient localAutofillClient = getClient();
    if (localAutofillClient != null) {
      return localAutofillClient.autofillClientFindViewByAutofillIdTraversal(paramAutofillId);
    }
    return null;
  }
  
  @GuardedBy("mLock")
  private void finishSessionLocked()
  {
    if (Helper.sVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("finishSessionLocked(): ");
      localStringBuilder.append(getStateAsStringLocked());
      Log.v("AutofillManager", localStringBuilder.toString());
    }
    if (!isActiveLocked()) {
      return;
    }
    try
    {
      mService.finishSession(mSessionId, mContext.getUserId());
      resetSessionLocked(true);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private static AutofillId getAutofillId(View paramView, int paramInt)
  {
    return new AutofillId(paramView.getAutofillViewId(), paramInt);
  }
  
  private AutofillClient getClient()
  {
    AutofillClient localAutofillClient = mContext.getAutofillClient();
    if ((localAutofillClient == null) && (Helper.sDebug))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("No AutofillClient for ");
      localStringBuilder.append(mContext.getPackageName());
      localStringBuilder.append(" on context ");
      localStringBuilder.append(mContext);
      Log.d("AutofillManager", localStringBuilder.toString());
    }
    return localAutofillClient;
  }
  
  public static int getDatasetIdFromAuthenticationId(int paramInt)
  {
    return 0xFFFF & paramInt;
  }
  
  public static int getRequestIdFromAuthenticationId(int paramInt)
  {
    return paramInt >> 16;
  }
  
  private static String getStateAsString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("INVALID:");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 5: 
      return "UNKNOWN_COMPAT_MODE";
    case 4: 
      return "DISABLED_BY_SERVICE";
    case 3: 
      return "SHOWING_SAVE_UI";
    case 2: 
      return "FINISHED";
    case 1: 
      return "ACTIVE";
    }
    return "UNKNOWN";
  }
  
  @GuardedBy("mLock")
  private String getStateAsStringLocked()
  {
    return getStateAsString(mState);
  }
  
  @GuardedBy("mLock")
  private boolean isActiveLocked()
  {
    int i = mState;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isClientDisablingEnterExitEvent()
  {
    AutofillClient localAutofillClient = getClient();
    boolean bool;
    if ((localAutofillClient != null) && (localAutofillClient.isDisablingEnterExitEventForAutofill())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isClientVisibleForAutofillLocked()
  {
    AutofillClient localAutofillClient = getClient();
    boolean bool;
    if ((localAutofillClient != null) && (localAutofillClient.autofillClientIsVisibleForAutofill())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @GuardedBy("mLock")
  private boolean isDisabledByServiceLocked()
  {
    boolean bool;
    if (mState == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @GuardedBy("mLock")
  private boolean isFinishedLocked()
  {
    boolean bool;
    if (mState == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int makeAuthenticationId(int paramInt1, int paramInt2)
  {
    return paramInt1 << 16 | 0xFFFF & paramInt2;
  }
  
  private LogMaker newLog(int paramInt)
  {
    LogMaker localLogMaker = new LogMaker(paramInt).addTaggedData(1456, Integer.valueOf(mSessionId));
    if (isCompatibilityModeEnabledLocked()) {
      localLogMaker.addTaggedData(1414, Integer.valueOf(1));
    }
    AutofillClient localAutofillClient = getClient();
    if (localAutofillClient == null) {
      localLogMaker.setPackageName(mContext.getPackageName());
    } else {
      localLogMaker.setComponentName(localAutofillClient.autofillClientGetComponentName());
    }
    return localLogMaker;
  }
  
  private void notifyNoFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2)
  {
    if (Helper.sVerbose)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("notifyNoFillUi(): sessionId=");
      ((StringBuilder)localObject1).append(paramInt1);
      ((StringBuilder)localObject1).append(", autofillId=");
      ((StringBuilder)localObject1).append(paramAutofillId);
      ((StringBuilder)localObject1).append(", sessionFinishedState=");
      ((StringBuilder)localObject1).append(paramInt2);
      Log.v("AutofillManager", ((StringBuilder)localObject1).toString());
    }
    View localView = findView(paramAutofillId);
    if (localView == null) {
      return;
    }
    Object localObject2 = null;
    Object localObject3 = mLock;
    Object localObject1 = localObject2;
    try
    {
      if (mSessionId == paramInt1)
      {
        localObject1 = localObject2;
        if (getClient() != null) {
          localObject1 = mCallback;
        }
      }
      if (localObject1 != null) {
        if (paramAutofillId.isVirtual()) {
          ((AutofillCallback)localObject1).onAutofillEvent(localView, paramAutofillId.getVirtualChildId(), 3);
        } else {
          ((AutofillCallback)localObject1).onAutofillEvent(localView, 3);
        }
      }
      if (paramInt2 != 0) {
        setSessionFinished(paramInt2);
      }
      return;
    }
    finally {}
  }
  
  private void notifyViewClicked(AutofillId paramAutofillId)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    if (Helper.sVerbose)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("notifyViewClicked(): id=");
      ((StringBuilder)???).append(paramAutofillId);
      ((StringBuilder)???).append(", trigger=");
      ((StringBuilder)???).append(mSaveTriggerId);
      Log.v("AutofillManager", ((StringBuilder)???).toString());
    }
    synchronized (mLock)
    {
      if ((mEnabled) && (isActiveLocked()))
      {
        if ((mSaveTriggerId != null) && (mSaveTriggerId.equals(paramAutofillId)))
        {
          if (Helper.sDebug)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("triggering commit by click of ");
            localStringBuilder.append(paramAutofillId);
            Log.d("AutofillManager", localStringBuilder.toString());
          }
          commitLocked();
          mMetricsLogger.write(newLog(1229));
        }
        return;
      }
      return;
    }
  }
  
  private void notifyViewEntered(View paramView, int paramInt)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      AutofillCallback localAutofillCallback = notifyViewEnteredLocked(paramView, paramInt);
      if (localAutofillCallback != null) {
        mCallback.onAutofillEvent(paramView, 3);
      }
      return;
    }
  }
  
  private void notifyViewEntered(View paramView, int paramInt1, Rect paramRect, int paramInt2)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      paramRect = notifyViewEnteredLocked(paramView, paramInt1, paramRect, paramInt2);
      if (paramRect != null) {
        paramRect.onAutofillEvent(paramView, paramInt1, 3);
      }
      return;
    }
  }
  
  @GuardedBy("mLock")
  private AutofillCallback notifyViewEnteredLocked(View paramView, int paramInt)
  {
    AutofillId localAutofillId = paramView.getAutofillId();
    if (shouldIgnoreViewEnteredLocked(localAutofillId, paramInt)) {
      return null;
    }
    Object localObject1 = null;
    ensureServiceClientAddedIfNeededLocked();
    Object localObject2;
    if (!mEnabled)
    {
      localObject2 = localObject1;
      if (mCallback != null) {
        localObject2 = mCallback;
      }
    }
    else
    {
      localObject2 = localObject1;
      if (!isClientDisablingEnterExitEvent())
      {
        paramView = paramView.getAutofillValue();
        if (!isActiveLocked()) {
          startSessionLocked(localAutofillId, null, paramView, paramInt);
        } else {
          updateSessionLocked(localAutofillId, null, paramView, 2, paramInt);
        }
        addEnteredIdLocked(localAutofillId);
        localObject2 = localObject1;
      }
    }
    return localObject2;
  }
  
  @GuardedBy("mLock")
  private AutofillCallback notifyViewEnteredLocked(View paramView, int paramInt1, Rect paramRect, int paramInt2)
  {
    AutofillId localAutofillId = getAutofillId(paramView, paramInt1);
    Object localObject = null;
    if (shouldIgnoreViewEnteredLocked(localAutofillId, paramInt2)) {
      return null;
    }
    ensureServiceClientAddedIfNeededLocked();
    if (!mEnabled)
    {
      paramView = localObject;
      if (mCallback != null) {
        paramView = mCallback;
      }
    }
    else
    {
      paramView = localObject;
      if (!isClientDisablingEnterExitEvent())
      {
        if (!isActiveLocked()) {
          startSessionLocked(localAutofillId, paramRect, null, paramInt2);
        } else {
          updateSessionLocked(localAutofillId, paramRect, null, 2, paramInt2);
        }
        addEnteredIdLocked(localAutofillId);
        paramView = localObject;
      }
    }
    return paramView;
  }
  
  @GuardedBy("mLock")
  private void notifyViewExitedLocked(View paramView, int paramInt)
  {
    ensureServiceClientAddedIfNeededLocked();
    if ((mEnabled) && (isActiveLocked()) && (!isClientDisablingEnterExitEvent())) {
      updateSessionLocked(getAutofillId(paramView, paramInt), null, null, 3, 0);
    }
  }
  
  private void notifyViewVisibilityChangedInternal(View paramView, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    synchronized (mLock)
    {
      if ((mEnabled) && (isActiveLocked()))
      {
        AutofillId localAutofillId;
        if (paramBoolean2) {
          localAutofillId = getAutofillId(paramView, paramInt);
        } else {
          localAutofillId = paramView.getAutofillId();
        }
        StringBuilder localStringBuilder;
        if (Helper.sVerbose)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("visibility changed for ");
          localStringBuilder.append(localAutofillId);
          localStringBuilder.append(": ");
          localStringBuilder.append(paramBoolean1);
          Log.v("AutofillManager", localStringBuilder.toString());
        }
        if ((!paramBoolean1) && (mFillableIds != null) && (mFillableIds.contains(localAutofillId)))
        {
          if (Helper.sDebug)
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Hidding UI when view ");
            localStringBuilder.append(localAutofillId);
            localStringBuilder.append(" became invisible");
            Log.d("AutofillManager", localStringBuilder.toString());
          }
          requestHideFillUi(localAutofillId, paramView);
        }
        if (mTrackedViews != null)
        {
          mTrackedViews.notifyViewVisibilityChangedLocked(localAutofillId, paramBoolean1);
        }
        else if (Helper.sVerbose)
        {
          paramView = new java/lang/StringBuilder;
          paramView.<init>();
          paramView.append("Ignoring visibility change on ");
          paramView.append(localAutofillId);
          paramView.append(": no tracked views");
          Log.v("AutofillManager", paramView.toString());
        }
      }
      return;
    }
  }
  
  private void post(Runnable paramRunnable)
  {
    AutofillClient localAutofillClient = getClient();
    if (localAutofillClient == null)
    {
      if (Helper.sVerbose) {
        Log.v("AutofillManager", "ignoring post() because client is null");
      }
      return;
    }
    localAutofillClient.autofillClientRunOnUiThread(paramRunnable);
  }
  
  private void requestHideFillUi(AutofillId paramAutofillId, View paramView)
  {
    Object localObject1 = null;
    synchronized (mLock)
    {
      AutofillClient localAutofillClient = getClient();
      Object localObject3 = localObject1;
      if (localAutofillClient != null)
      {
        localObject3 = localObject1;
        if (localAutofillClient.autofillClientRequestHideFillUi())
        {
          mIdShownFillUi = null;
          localObject3 = mCallback;
        }
      }
      if (localObject3 != null) {
        if (paramAutofillId.isVirtual()) {
          ((AutofillCallback)localObject3).onAutofillEvent(paramView, paramAutofillId.getVirtualChildId(), 2);
        } else {
          ((AutofillCallback)localObject3).onAutofillEvent(paramView, 2);
        }
      }
      return;
    }
  }
  
  private void requestHideFillUi(AutofillId paramAutofillId, boolean paramBoolean)
  {
    View localView;
    if (paramAutofillId == null) {
      localView = null;
    } else {
      localView = findView(paramAutofillId);
    }
    if (Helper.sVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("requestHideFillUi(");
      localStringBuilder.append(paramAutofillId);
      localStringBuilder.append("): anchor = ");
      localStringBuilder.append(localView);
      Log.v("AutofillManager", localStringBuilder.toString());
    }
    if (localView == null)
    {
      if (paramBoolean)
      {
        paramAutofillId = getClient();
        if (paramAutofillId != null) {
          paramAutofillId.autofillClientRequestHideFillUi();
        }
      }
      return;
    }
    requestHideFillUi(paramAutofillId, localView);
  }
  
  private void requestShowFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2, int paramInt3, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter)
  {
    View localView = findView(paramAutofillId);
    if (localView == null) {
      return;
    }
    Object localObject1 = null;
    Object localObject3;
    synchronized (mLock)
    {
      int i = mSessionId;
      localObject3 = localObject1;
      if (i != paramInt1) {}
    }
    try
    {
      AutofillClient localAutofillClient = getClient();
      localObject3 = localObject1;
      if (localAutofillClient != null)
      {
        localObject3 = localObject1;
        if (localAutofillClient.autofillClientRequestShowFillUi(localView, paramInt2, paramInt3, paramRect, paramIAutofillWindowPresenter))
        {
          localObject3 = mCallback;
          mIdShownFillUi = paramAutofillId;
        }
      }
      if (localObject3 != null) {
        if (paramAutofillId.isVirtual()) {
          ((AutofillCallback)localObject3).onAutofillEvent(localView, paramAutofillId.getVirtualChildId(), 1);
        } else {
          ((AutofillCallback)localObject3).onAutofillEvent(localView, 1);
        }
      }
      return;
    }
    finally
    {
      for (;;) {}
    }
    paramAutofillId = finally;
    throw paramAutofillId;
  }
  
  @GuardedBy("mLock")
  private void resetSessionLocked(boolean paramBoolean)
  {
    mSessionId = Integer.MIN_VALUE;
    mState = 0;
    mTrackedViews = null;
    mFillableIds = null;
    mSaveTriggerId = null;
    mIdShownFillUi = null;
    if (paramBoolean) {
      mEnteredIds = null;
    }
  }
  
  private void setAutofilledIfValuesIs(View paramView, AutofillValue paramAutofillValue)
  {
    if (Objects.equals(paramView.getAutofillValue(), paramAutofillValue)) {
      synchronized (mLock)
      {
        if (mLastAutofilledData == null)
        {
          ParcelableMap localParcelableMap = new android/view/autofill/ParcelableMap;
          localParcelableMap.<init>(1);
          mLastAutofilledData = localParcelableMap;
        }
        mLastAutofilledData.put(paramView.getAutofillId(), paramAutofillValue);
        paramView.setAutofilled(true);
      }
    }
  }
  
  private void setNotifyOnClickLocked(AutofillId paramAutofillId, boolean paramBoolean)
  {
    Object localObject = findView(paramAutofillId);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setNotifyOnClick(): invalid id: ");
      ((StringBuilder)localObject).append(paramAutofillId);
      Log.w("AutofillManager", ((StringBuilder)localObject).toString());
      return;
    }
    ((View)localObject).setNotifyAutofillManagerOnClick(paramBoolean);
  }
  
  private void setSaveUiState(int paramInt, boolean paramBoolean)
  {
    if (Helper.sDebug)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("setSaveUiState(");
      ((StringBuilder)???).append(paramInt);
      ((StringBuilder)???).append("): ");
      ((StringBuilder)???).append(paramBoolean);
      Log.d("AutofillManager", ((StringBuilder)???).toString());
    }
    synchronized (mLock)
    {
      if (mSessionId != Integer.MIN_VALUE)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("setSaveUiState(");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(", ");
        localStringBuilder.append(paramBoolean);
        localStringBuilder.append(") called on existing session ");
        localStringBuilder.append(mSessionId);
        localStringBuilder.append("; cancelling it");
        Log.w("AutofillManager", localStringBuilder.toString());
        cancelSessionLocked();
      }
      if (paramBoolean)
      {
        mSessionId = paramInt;
        mState = 3;
      }
      else
      {
        mSessionId = Integer.MIN_VALUE;
        mState = 0;
      }
      return;
    }
  }
  
  private void setSessionFinished(int paramInt)
  {
    synchronized (mLock)
    {
      if (Helper.sVerbose)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("setSessionFinished(): from ");
        localStringBuilder.append(getStateAsStringLocked());
        localStringBuilder.append(" to ");
        localStringBuilder.append(getStateAsString(paramInt));
        Log.v("AutofillManager", localStringBuilder.toString());
      }
      if (paramInt == 5)
      {
        resetSessionLocked(true);
        mState = 0;
      }
      else
      {
        resetSessionLocked(false);
        mState = paramInt;
      }
      return;
    }
  }
  
  private void setState(int paramInt)
  {
    if (Helper.sVerbose)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("setState(");
      ((StringBuilder)localObject1).append(paramInt);
      ((StringBuilder)localObject1).append(")");
      Log.v("AutofillManager", ((StringBuilder)localObject1).toString());
    }
    Object localObject1 = mLock;
    boolean bool1 = false;
    boolean bool2;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    try
    {
      mEnabled = bool2;
      if ((!mEnabled) || ((paramInt & 0x2) != 0)) {
        resetSessionLocked(true);
      }
      if ((paramInt & 0x4) != 0)
      {
        mServiceClient = null;
        if (mServiceClientCleaner != null)
        {
          mServiceClientCleaner.clean();
          mServiceClientCleaner = null;
        }
      }
      if ((paramInt & 0x8) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Helper.sDebug = bool2;
      bool2 = bool1;
      if ((paramInt & 0x10) != 0) {
        bool2 = true;
      }
      Helper.sVerbose = bool2;
      return;
    }
    finally {}
  }
  
  private void setTrackedViews(int paramInt, AutofillId[] paramArrayOfAutofillId1, boolean paramBoolean1, boolean paramBoolean2, AutofillId[] paramArrayOfAutofillId2, AutofillId paramAutofillId)
  {
    synchronized (mLock)
    {
      if ((mEnabled) && (mSessionId == paramInt))
      {
        if (paramBoolean1)
        {
          TrackedViews localTrackedViews = new android/view/autofill/AutofillManager$TrackedViews;
          localTrackedViews.<init>(this, paramArrayOfAutofillId1);
          mTrackedViews = localTrackedViews;
        }
        else
        {
          mTrackedViews = null;
        }
        mSaveOnFinish = paramBoolean2;
        if (paramArrayOfAutofillId2 != null)
        {
          if (mFillableIds == null)
          {
            paramArrayOfAutofillId1 = new android/util/ArraySet;
            paramArrayOfAutofillId1.<init>(paramArrayOfAutofillId2.length);
            mFillableIds = paramArrayOfAutofillId1;
          }
          int i = paramArrayOfAutofillId2.length;
          for (paramInt = 0; paramInt < i; paramInt++)
          {
            paramArrayOfAutofillId1 = paramArrayOfAutofillId2[paramInt];
            mFillableIds.add(paramArrayOfAutofillId1);
          }
          if (Helper.sVerbose)
          {
            paramArrayOfAutofillId1 = new java/lang/StringBuilder;
            paramArrayOfAutofillId1.<init>();
            paramArrayOfAutofillId1.append("setTrackedViews(): fillableIds=");
            paramArrayOfAutofillId1.append(paramArrayOfAutofillId2);
            paramArrayOfAutofillId1.append(", mFillableIds");
            paramArrayOfAutofillId1.append(mFillableIds);
            Log.v("AutofillManager", paramArrayOfAutofillId1.toString());
          }
        }
        if ((mSaveTriggerId != null) && (!mSaveTriggerId.equals(paramAutofillId))) {
          setNotifyOnClickLocked(mSaveTriggerId, false);
        }
        if ((paramAutofillId != null) && (!paramAutofillId.equals(mSaveTriggerId)))
        {
          mSaveTriggerId = paramAutofillId;
          setNotifyOnClickLocked(mSaveTriggerId, true);
        }
      }
      return;
    }
  }
  
  @GuardedBy("mLock")
  private boolean shouldIgnoreViewEnteredLocked(AutofillId paramAutofillId, int paramInt)
  {
    StringBuilder localStringBuilder;
    if (isDisabledByServiceLocked())
    {
      if (Helper.sVerbose)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("ignoring notifyViewEntered(flags=");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(", view=");
        localStringBuilder.append(paramAutofillId);
        localStringBuilder.append(") on state ");
        localStringBuilder.append(getStateAsStringLocked());
        localStringBuilder.append(" because disabled by svc");
        Log.v("AutofillManager", localStringBuilder.toString());
      }
      return true;
    }
    if ((isFinishedLocked()) && ((paramInt & 0x1) == 0) && (mEnteredIds != null) && (mEnteredIds.contains(paramAutofillId)))
    {
      if (Helper.sVerbose)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("ignoring notifyViewEntered(flags=");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(", view=");
        localStringBuilder.append(paramAutofillId);
        localStringBuilder.append(") on state ");
        localStringBuilder.append(getStateAsStringLocked());
        localStringBuilder.append(" because view was already entered: ");
        localStringBuilder.append(mEnteredIds);
        Log.v("AutofillManager", localStringBuilder.toString());
      }
      return true;
    }
    if (Helper.sVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("not ignoring notifyViewEntered(flags=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(", view=");
      localStringBuilder.append(paramAutofillId);
      localStringBuilder.append(", state ");
      localStringBuilder.append(getStateAsStringLocked());
      localStringBuilder.append(", enteredIds=");
      localStringBuilder.append(mEnteredIds);
      Log.v("AutofillManager", localStringBuilder.toString());
    }
    return false;
  }
  
  @GuardedBy("mLock")
  private void startSessionLocked(AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt)
  {
    Object localObject;
    if (Helper.sVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("startSessionLocked(): id=");
      ((StringBuilder)localObject).append(paramAutofillId);
      ((StringBuilder)localObject).append(", bounds=");
      ((StringBuilder)localObject).append(paramRect);
      ((StringBuilder)localObject).append(", value=");
      ((StringBuilder)localObject).append(paramAutofillValue);
      ((StringBuilder)localObject).append(", flags=");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(", state=");
      ((StringBuilder)localObject).append(getStateAsStringLocked());
      ((StringBuilder)localObject).append(", compatMode=");
      ((StringBuilder)localObject).append(isCompatibilityModeEnabledLocked());
      ((StringBuilder)localObject).append(", enteredIds=");
      ((StringBuilder)localObject).append(mEnteredIds);
      Log.v("AutofillManager", ((StringBuilder)localObject).toString());
    }
    if ((mState != 0) && (!isFinishedLocked()) && ((paramInt & 0x1) == 0))
    {
      if (Helper.sVerbose)
      {
        paramRect = new StringBuilder();
        paramRect.append("not automatically starting session for ");
        paramRect.append(paramAutofillId);
        paramRect.append(" on state ");
        paramRect.append(getStateAsStringLocked());
        paramRect.append(" and flags ");
        paramRect.append(paramInt);
        Log.v("AutofillManager", paramRect.toString());
      }
      return;
    }
    try
    {
      AutofillClient localAutofillClient = getClient();
      if (localAutofillClient == null) {
        return;
      }
      IAutoFillManager localIAutoFillManager = mService;
      IBinder localIBinder = localAutofillClient.autofillClientGetActivityToken();
      localObject = mServiceClient.asBinder();
      int i = mContext.getUserId();
      boolean bool;
      if (mCallback != null) {
        bool = true;
      } else {
        bool = false;
      }
      mSessionId = localIAutoFillManager.startSession(localIBinder, (IBinder)localObject, paramAutofillId, paramRect, paramAutofillValue, i, bool, paramInt, localAutofillClient.autofillClientGetComponentName(), isCompatibilityModeEnabledLocked());
      if (mSessionId != Integer.MIN_VALUE) {
        mState = 1;
      }
      localAutofillClient.autofillClientResetableStateAvailable();
      return;
    }
    catch (RemoteException paramAutofillId)
    {
      throw paramAutofillId.rethrowFromSystemServer();
    }
  }
  
  @GuardedBy("mLock")
  private void updateSessionLocked(AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt1, int paramInt2)
  {
    Object localObject;
    if (Helper.sVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateSessionLocked(): id=");
      ((StringBuilder)localObject).append(paramAutofillId);
      ((StringBuilder)localObject).append(", bounds=");
      ((StringBuilder)localObject).append(paramRect);
      ((StringBuilder)localObject).append(", value=");
      ((StringBuilder)localObject).append(paramAutofillValue);
      ((StringBuilder)localObject).append(", action=");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(", flags=");
      ((StringBuilder)localObject).append(paramInt2);
      Log.v("AutofillManager", ((StringBuilder)localObject).toString());
    }
    int i;
    if ((paramInt2 & 0x1) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      try
      {
        localObject = getClient();
        if (localObject == null) {
          return;
        }
        IAutoFillManager localIAutoFillManager = mService;
        IBinder localIBinder1 = ((AutofillClient)localObject).autofillClientGetActivityToken();
        IBinder localIBinder2 = mServiceClient.asBinder();
        i = mContext.getUserId();
        boolean bool;
        if (mCallback != null) {
          bool = true;
        } else {
          bool = false;
        }
        paramInt1 = localIAutoFillManager.updateOrRestartSession(localIBinder1, localIBinder2, paramAutofillId, paramRect, paramAutofillValue, i, bool, paramInt2, ((AutofillClient)localObject).autofillClientGetComponentName(), mSessionId, paramInt1, isCompatibilityModeEnabledLocked());
        if (paramInt1 != mSessionId)
        {
          if (Helper.sDebug)
          {
            paramAutofillId = new java/lang/StringBuilder;
            paramAutofillId.<init>();
            paramAutofillId.append("Session restarted: ");
            paramAutofillId.append(mSessionId);
            paramAutofillId.append("=>");
            paramAutofillId.append(paramInt1);
            Log.d("AutofillManager", paramAutofillId.toString());
          }
          mSessionId = paramInt1;
          if (mSessionId == Integer.MIN_VALUE) {
            paramInt1 = 0;
          } else {
            paramInt1 = 1;
          }
          mState = paramInt1;
          ((AutofillClient)localObject).autofillClientResetableStateAvailable();
        }
      }
      catch (RemoteException paramAutofillId)
      {
        break label371;
      }
    }
    mService.updateSession(mSessionId, paramAutofillId, paramRect, paramAutofillValue, paramInt1, paramInt2, mContext.getUserId());
    return;
    label371:
    throw paramAutofillId.rethrowFromSystemServer();
  }
  
  public void cancel()
  {
    if (Helper.sVerbose) {
      Log.v("AutofillManager", "cancel() called by app");
    }
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      cancelLocked();
      return;
    }
  }
  
  public void commit()
  {
    if (!hasAutofillFeature()) {
      return;
    }
    if (Helper.sVerbose) {
      Log.v("AutofillManager", "commit() called by app");
    }
    synchronized (mLock)
    {
      commitLocked();
      return;
    }
  }
  
  public void disableAutofillServices()
  {
    if (!hasAutofillFeature()) {
      return;
    }
    try
    {
      mService.disableOwnedAutofillServices(mContext.getUserId());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void disableOwnedAutofillServices()
  {
    disableAutofillServices();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("AutofillManager:");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("  ");
    paramString = ((StringBuilder)localObject).toString();
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("sessionId: ");
    paramPrintWriter.println(mSessionId);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("state: ");
    paramPrintWriter.println(getStateAsStringLocked());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("context: ");
    paramPrintWriter.println(mContext);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("client: ");
    paramPrintWriter.println(getClient());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("enabled: ");
    paramPrintWriter.println(mEnabled);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("hasService: ");
    localObject = mService;
    boolean bool1 = false;
    if (localObject != null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    paramPrintWriter.println(bool2);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("hasCallback: ");
    boolean bool2 = bool1;
    if (mCallback != null) {
      bool2 = true;
    }
    paramPrintWriter.println(bool2);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("onInvisibleCalled ");
    paramPrintWriter.println(mOnInvisibleCalled);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("last autofilled data: ");
    paramPrintWriter.println(mLastAutofilledData);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("tracked views: ");
    if (mTrackedViews == null)
    {
      paramPrintWriter.println("null");
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("  ");
      localObject = ((StringBuilder)localObject).toString();
      paramPrintWriter.println();
      paramPrintWriter.print((String)localObject);
      paramPrintWriter.print("visible:");
      paramPrintWriter.println(mTrackedViews.mVisibleTrackedIds);
      paramPrintWriter.print((String)localObject);
      paramPrintWriter.print("invisible:");
      paramPrintWriter.println(mTrackedViews.mInvisibleTrackedIds);
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("fillable ids: ");
    paramPrintWriter.println(mFillableIds);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("entered ids: ");
    paramPrintWriter.println(mEnteredIds);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("save trigger id: ");
    paramPrintWriter.println(mSaveTriggerId);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("save on finish(): ");
    paramPrintWriter.println(mSaveOnFinish);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("compat mode enabled: ");
    paramPrintWriter.println(isCompatibilityModeEnabledLocked());
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("debug: ");
    paramPrintWriter.print(Helper.sDebug);
    paramPrintWriter.print(" verbose: ");
    paramPrintWriter.println(Helper.sVerbose);
  }
  
  public void enableCompatibilityMode()
  {
    synchronized (mLock)
    {
      CompatibilityBridge localCompatibilityBridge = new android/view/autofill/AutofillManager$CompatibilityBridge;
      localCompatibilityBridge.<init>(this);
      mCompatibilityBridge = localCompatibilityBridge;
      return;
    }
  }
  
  public ComponentName getAutofillServiceComponentName()
  {
    if (mService == null) {
      return null;
    }
    try
    {
      ComponentName localComponentName = mService.getAutofillServiceComponentName();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<String> getAvailableFieldClassificationAlgorithms()
  {
    try
    {
      Object localObject = mService.getAvailableFieldClassificationAlgorithms();
      if (localObject != null) {
        localObject = Arrays.asList((Object[])localObject);
      } else {
        localObject = Collections.emptyList();
      }
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return null;
  }
  
  public String getDefaultFieldClassificationAlgorithm()
  {
    try
    {
      String str = mService.getDefaultFieldClassificationAlgorithm();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return null;
  }
  
  public FillEventHistory getFillEventHistory()
  {
    try
    {
      FillEventHistory localFillEventHistory = mService.getFillEventHistory();
      return localFillEventHistory;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return null;
  }
  
  public AutofillId getNextAutofillId()
  {
    AutofillClient localAutofillClient = getClient();
    if (localAutofillClient == null) {
      return null;
    }
    AutofillId localAutofillId = localAutofillClient.autofillClientGetNextAutofillId();
    if ((localAutofillId == null) && (Helper.sDebug))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getNextAutofillId(): client ");
      localStringBuilder.append(localAutofillClient);
      localStringBuilder.append(" returned null");
      Log.d("AutofillManager", localStringBuilder.toString());
    }
    return localAutofillId;
  }
  
  public UserData getUserData()
  {
    try
    {
      UserData localUserData = mService.getUserData();
      return localUserData;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return null;
  }
  
  public String getUserDataId()
  {
    try
    {
      String str = mService.getUserDataId();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return null;
  }
  
  public boolean hasAutofillFeature()
  {
    boolean bool;
    if (mService != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasEnabledAutofillServices()
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.isServiceEnabled(mContext.getUserId(), mContext.getPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAutofillSupported()
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.isServiceSupported(mContext.getUserId());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAutofillUiShowing()
  {
    AutofillClient localAutofillClient = mContext.getAutofillClient();
    boolean bool;
    if ((localAutofillClient != null) && (localAutofillClient.autofillClientIsFillUiShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @GuardedBy("mLock")
  public boolean isCompatibilityModeEnabledLocked()
  {
    boolean bool;
    if (mCompatibilityBridge != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    if (!hasAutofillFeature()) {
      return false;
    }
    synchronized (mLock)
    {
      if (isDisabledByServiceLocked()) {
        return false;
      }
      ensureServiceClientAddedIfNeededLocked();
      boolean bool = mEnabled;
      return bool;
    }
  }
  
  public boolean isFieldClassificationEnabled()
  {
    try
    {
      boolean bool = mService.isFieldClassificationEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return false;
  }
  
  public void notifyValueChanged(View paramView)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    Object localObject1 = null;
    int i = 0;
    AutofillValue localAutofillValue = null;
    synchronized (mLock)
    {
      if (mLastAutofilledData == null)
      {
        paramView.setAutofilled(false);
      }
      else
      {
        localObject1 = paramView.getAutofillId();
        if (mLastAutofilledData.containsKey(localObject1))
        {
          localAutofillValue = paramView.getAutofillValue();
          i = 1;
          if (Objects.equals(mLastAutofilledData.get(localObject1), localAutofillValue))
          {
            paramView.setAutofilled(true);
          }
          else
          {
            paramView.setAutofilled(false);
            mLastAutofilledData.remove(localObject1);
          }
        }
        else
        {
          paramView.setAutofilled(false);
        }
      }
      if ((mEnabled) && (isActiveLocked()))
      {
        Object localObject3 = localObject1;
        if (localObject1 == null) {
          localObject3 = paramView.getAutofillId();
        }
        if (i == 0) {
          localAutofillValue = paramView.getAutofillValue();
        }
        updateSessionLocked((AutofillId)localObject3, null, localAutofillValue, 4, 0);
        return;
      }
      if (Helper.sVerbose)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("notifyValueChanged(");
        ((StringBuilder)localObject1).append(paramView.getAutofillId());
        ((StringBuilder)localObject1).append("): ignoring on state ");
        ((StringBuilder)localObject1).append(getStateAsStringLocked());
        Log.v("AutofillManager", ((StringBuilder)localObject1).toString());
      }
      return;
    }
  }
  
  public void notifyValueChanged(View paramView, int paramInt, AutofillValue paramAutofillValue)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      if ((mEnabled) && (isActiveLocked()))
      {
        updateSessionLocked(getAutofillId(paramView, paramInt), null, paramAutofillValue, 4, 0);
        return;
      }
      if (Helper.sVerbose)
      {
        paramAutofillValue = new java/lang/StringBuilder;
        paramAutofillValue.<init>();
        paramAutofillValue.append("notifyValueChanged(");
        paramAutofillValue.append(paramView.getAutofillId());
        paramAutofillValue.append(":");
        paramAutofillValue.append(paramInt);
        paramAutofillValue.append("): ignoring on state ");
        paramAutofillValue.append(getStateAsStringLocked());
        Log.v("AutofillManager", paramAutofillValue.toString());
      }
      return;
    }
  }
  
  public void notifyViewClicked(View paramView)
  {
    notifyViewClicked(paramView.getAutofillId());
  }
  
  public void notifyViewClicked(View paramView, int paramInt)
  {
    notifyViewClicked(getAutofillId(paramView, paramInt));
  }
  
  public void notifyViewEntered(View paramView)
  {
    notifyViewEntered(paramView, 0);
  }
  
  public void notifyViewEntered(View paramView, int paramInt, Rect paramRect)
  {
    notifyViewEntered(paramView, paramInt, paramRect, 0);
  }
  
  public void notifyViewExited(View paramView)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      notifyViewExitedLocked(paramView);
      return;
    }
  }
  
  public void notifyViewExited(View paramView, int paramInt)
  {
    if (Helper.sVerbose)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("notifyViewExited(");
      ((StringBuilder)???).append(paramView.getAutofillId());
      ((StringBuilder)???).append(", ");
      ((StringBuilder)???).append(paramInt);
      Log.v("AutofillManager", ((StringBuilder)???).toString());
    }
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      notifyViewExitedLocked(paramView, paramInt);
      return;
    }
  }
  
  @GuardedBy("mLock")
  void notifyViewExitedLocked(View paramView)
  {
    ensureServiceClientAddedIfNeededLocked();
    if ((mEnabled) && (isActiveLocked()) && (!isClientDisablingEnterExitEvent())) {
      updateSessionLocked(paramView.getAutofillId(), null, null, 3, 0);
    }
  }
  
  public void notifyViewVisibilityChanged(View paramView, int paramInt, boolean paramBoolean)
  {
    notifyViewVisibilityChangedInternal(paramView, paramInt, paramBoolean, true);
  }
  
  public void notifyViewVisibilityChanged(View paramView, boolean paramBoolean)
  {
    notifyViewVisibilityChangedInternal(paramView, 0, paramBoolean, false);
  }
  
  public void onActivityFinishing()
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      if (mSaveOnFinish)
      {
        if (Helper.sDebug) {
          Log.d("AutofillManager", "onActivityFinishing(): calling commitLocked()");
        }
        commitLocked();
      }
      else
      {
        if (Helper.sDebug) {
          Log.d("AutofillManager", "onActivityFinishing(): calling cancelLocked()");
        }
        cancelLocked();
      }
      return;
    }
  }
  
  public void onAuthenticationResult(int paramInt, Intent paramIntent, View paramView)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    if (Helper.sDebug)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onAuthenticationResult(): d=");
      ((StringBuilder)???).append(paramIntent);
      Log.d("AutofillManager", ((StringBuilder)???).toString());
    }
    synchronized (mLock)
    {
      if (!isActiveLocked()) {
        return;
      }
      if ((!mOnInvisibleCalled) && (paramView != null) && (paramView.canNotifyAutofillEnterExitEvent()))
      {
        notifyViewExitedLocked(paramView);
        notifyViewEnteredLocked(paramView, 0);
      }
      if (paramIntent == null) {
        return;
      }
      Parcelable localParcelable = paramIntent.getParcelableExtra("android.view.autofill.extra.AUTHENTICATION_RESULT");
      paramView = new android/os/Bundle;
      paramView.<init>();
      paramView.putParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT", localParcelable);
      paramIntent = paramIntent.getBundleExtra("android.view.autofill.extra.CLIENT_STATE");
      if (paramIntent != null) {
        paramView.putBundle("android.view.autofill.extra.CLIENT_STATE", paramIntent);
      }
      try
      {
        mService.setAuthenticationResult(paramView, mSessionId, paramInt, mContext.getUserId());
      }
      catch (RemoteException paramIntent)
      {
        Log.e("AutofillManager", "Error delivering authentication result", paramIntent);
      }
      return;
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      mLastAutofilledData = ((ParcelableMap)paramBundle.getParcelable("android:lastAutoFilledData"));
      if (isActiveLocked())
      {
        Log.w("AutofillManager", "New session was started before onCreate()");
        return;
      }
      mSessionId = paramBundle.getInt("android:sessionId", Integer.MIN_VALUE);
      mState = paramBundle.getInt("android:state", 0);
      if (mSessionId != Integer.MIN_VALUE)
      {
        ensureServiceClientAddedIfNeededLocked();
        paramBundle = getClient();
        if (paramBundle != null) {
          try
          {
            if (!mService.restoreSession(mSessionId, paramBundle.autofillClientGetActivityToken(), mServiceClient.asBinder()))
            {
              paramBundle = new java/lang/StringBuilder;
              paramBundle.<init>();
              paramBundle.append("Session ");
              paramBundle.append(mSessionId);
              paramBundle.append(" could not be restored");
              Log.w("AutofillManager", paramBundle.toString());
              mSessionId = Integer.MIN_VALUE;
              mState = 0;
            }
            else
            {
              if (Helper.sDebug)
              {
                StringBuilder localStringBuilder = new java/lang/StringBuilder;
                localStringBuilder.<init>();
                localStringBuilder.append("session ");
                localStringBuilder.append(mSessionId);
                localStringBuilder.append(" was restored");
                Log.d("AutofillManager", localStringBuilder.toString());
              }
              paramBundle.autofillClientResetableStateAvailable();
            }
          }
          catch (RemoteException paramBundle)
          {
            Log.e("AutofillManager", "Could not figure out if there was an autofill session", paramBundle);
          }
        }
      }
      return;
    }
  }
  
  public void onInvisibleForAutofill()
  {
    synchronized (mLock)
    {
      mOnInvisibleCalled = true;
      return;
    }
  }
  
  public void onPendingSaveUi(int paramInt, IBinder paramIBinder)
  {
    if (Helper.sVerbose)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onPendingSaveUi(");
      ((StringBuilder)???).append(paramInt);
      ((StringBuilder)???).append("): ");
      ((StringBuilder)???).append(paramIBinder);
      Log.v("AutofillManager", ((StringBuilder)???).toString());
    }
    try
    {
      synchronized (mLock)
      {
        mService.onPendingSaveUi(paramInt, paramIBinder);
      }
    }
    catch (RemoteException paramIBinder)
    {
      paramIBinder.rethrowFromSystemServer();
      return;
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    synchronized (mLock)
    {
      if (mSessionId != Integer.MIN_VALUE) {
        paramBundle.putInt("android:sessionId", mSessionId);
      }
      if (mState != 0) {
        paramBundle.putInt("android:state", mState);
      }
      if (mLastAutofilledData != null) {
        paramBundle.putParcelable("android:lastAutoFilledData", mLastAutofilledData);
      }
      return;
    }
  }
  
  public void onVisibleForAutofill()
  {
    Choreographer.getInstance().postCallback(3, new _..Lambda.AutofillManager.YfpJNFodEuj5lbXfPlc77fsEvC8(this), null);
  }
  
  /* Error */
  public void registerCallback(AutofillCallback paramAutofillCallback)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 563	android/view/autofill/AutofillManager:hasAutofillFeature	()Z
    //   4: ifne +4 -> 8
    //   7: return
    //   8: aload_0
    //   9: getfield 131	android/view/autofill/AutofillManager:mLock	Ljava/lang/Object;
    //   12: astore_2
    //   13: aload_2
    //   14: monitorenter
    //   15: aload_1
    //   16: ifnonnull +10 -> 26
    //   19: aload_2
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: goto +59 -> 82
    //   26: aload_0
    //   27: getfield 552	android/view/autofill/AutofillManager:mCallback	Landroid/view/autofill/AutofillManager$AutofillCallback;
    //   30: ifnull +8 -> 38
    //   33: iconst_1
    //   34: istore_3
    //   35: goto +5 -> 40
    //   38: iconst_0
    //   39: istore_3
    //   40: aload_0
    //   41: aload_1
    //   42: putfield 552	android/view/autofill/AutofillManager:mCallback	Landroid/view/autofill/AutofillManager$AutofillCallback;
    //   45: iload_3
    //   46: ifne +33 -> 79
    //   49: aload_0
    //   50: getfield 149	android/view/autofill/AutofillManager:mService	Landroid/view/autofill/IAutoFillManager;
    //   53: aload_0
    //   54: getfield 133	android/view/autofill/AutofillManager:mSessionId	I
    //   57: aload_0
    //   58: getfield 147	android/view/autofill/AutofillManager:mContext	Landroid/content/Context;
    //   61: invokevirtual 360	android/content/Context:getUserId	()I
    //   64: iconst_1
    //   65: invokeinterface 1064 4 0
    //   70: goto +9 -> 79
    //   73: astore_1
    //   74: aload_1
    //   75: invokevirtual 370	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   78: athrow
    //   79: aload_2
    //   80: monitorexit
    //   81: return
    //   82: aload_2
    //   83: monitorexit
    //   84: aload_1
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	AutofillManager
    //   0	86	1	paramAutofillCallback	AutofillCallback
    //   12	71	2	localObject	Object
    //   34	12	3	i	int
    // Exception table:
    //   from	to	target	type
    //   19	21	22	finally
    //   26	33	22	finally
    //   40	45	22	finally
    //   49	70	22	finally
    //   74	79	22	finally
    //   79	81	22	finally
    //   82	84	22	finally
    //   49	70	73	android/os/RemoteException
  }
  
  public void requestAutofill(View paramView)
  {
    notifyViewEntered(paramView, 1);
  }
  
  public void requestAutofill(View paramView, int paramInt, Rect paramRect)
  {
    notifyViewEntered(paramView, paramInt, paramRect, 1);
  }
  
  public void requestHideFillUi()
  {
    requestHideFillUi(mIdShownFillUi, true);
  }
  
  public void setUserData(UserData paramUserData)
  {
    try
    {
      mService.setUserData(paramUserData);
    }
    catch (RemoteException paramUserData)
    {
      paramUserData.rethrowFromSystemServer();
    }
  }
  
  public void unregisterCallback(AutofillCallback paramAutofillCallback)
  {
    if (!hasAutofillFeature()) {
      return;
    }
    Object localObject = mLock;
    if (paramAutofillCallback != null) {
      try
      {
        if ((mCallback != null) && (paramAutofillCallback == mCallback))
        {
          mCallback = null;
          try
          {
            mService.setHasCallback(mSessionId, mContext.getUserId(), false);
            return;
          }
          catch (RemoteException paramAutofillCallback)
          {
            throw paramAutofillCallback.rethrowFromSystemServer();
          }
        }
      }
      finally
      {
        break label79;
      }
    }
    return;
    label79:
    throw paramAutofillCallback;
  }
  
  public static abstract class AutofillCallback
  {
    public static final int EVENT_INPUT_HIDDEN = 2;
    public static final int EVENT_INPUT_SHOWN = 1;
    public static final int EVENT_INPUT_UNAVAILABLE = 3;
    
    public AutofillCallback() {}
    
    public void onAutofillEvent(View paramView, int paramInt) {}
    
    public void onAutofillEvent(View paramView, int paramInt1, int paramInt2) {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface AutofillEventType {}
  }
  
  public static abstract interface AutofillClient
  {
    public abstract void autofillClientAuthenticate(int paramInt, IntentSender paramIntentSender, Intent paramIntent);
    
    public abstract void autofillClientDispatchUnhandledKey(View paramView, KeyEvent paramKeyEvent);
    
    public abstract View autofillClientFindViewByAccessibilityIdTraversal(int paramInt1, int paramInt2);
    
    public abstract View autofillClientFindViewByAutofillIdTraversal(AutofillId paramAutofillId);
    
    public abstract View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] paramArrayOfAutofillId);
    
    public abstract IBinder autofillClientGetActivityToken();
    
    public abstract ComponentName autofillClientGetComponentName();
    
    public abstract AutofillId autofillClientGetNextAutofillId();
    
    public abstract boolean[] autofillClientGetViewVisibility(AutofillId[] paramArrayOfAutofillId);
    
    public abstract boolean autofillClientIsCompatibilityModeEnabled();
    
    public abstract boolean autofillClientIsFillUiShowing();
    
    public abstract boolean autofillClientIsVisibleForAutofill();
    
    public abstract boolean autofillClientRequestHideFillUi();
    
    public abstract boolean autofillClientRequestShowFillUi(View paramView, int paramInt1, int paramInt2, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter);
    
    public abstract void autofillClientResetableStateAvailable();
    
    public abstract void autofillClientRunOnUiThread(Runnable paramRunnable);
    
    public abstract boolean isDisablingEnterExitEventForAutofill();
  }
  
  private static final class AutofillManagerClient
    extends IAutoFillManagerClient.Stub
  {
    private final WeakReference<AutofillManager> mAfm;
    
    AutofillManagerClient(AutofillManager paramAutofillManager)
    {
      mAfm = new WeakReference(paramAutofillManager);
    }
    
    public void authenticate(int paramInt1, int paramInt2, IntentSender paramIntentSender, Intent paramIntent)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.qyxZ4PACUgHFGSvMBHzgwjJ3yns(localAutofillManager, paramInt1, paramInt2, paramIntentSender, paramIntent));
      }
    }
    
    public void autofill(int paramInt, List<AutofillId> paramList, List<AutofillValue> paramList1)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.1jAzMluMSJksx55SMUQn4BKB2Ng(localAutofillManager, paramInt, paramList, paramList1));
      }
    }
    
    public void dispatchUnhandledKey(int paramInt, AutofillId paramAutofillId, KeyEvent paramKeyEvent)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.xqXjXW0fvc8JdYR5fgGKw9lJc3I(localAutofillManager, paramInt, paramAutofillId, paramKeyEvent));
      }
    }
    
    public void notifyNoFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.K79QnIPRaZuikYDQdsLcIUBhqiI(localAutofillManager, paramInt1, paramAutofillId, paramInt2));
      }
    }
    
    public void requestHideFillUi(int paramInt, AutofillId paramAutofillId)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.dCTetwfU0gT1ZrSzZGZiGStXlOY(localAutofillManager, paramAutofillId));
      }
    }
    
    public void requestShowFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2, int paramInt3, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.kRL9XILLc2XNr90gxVDACLzcyqc(localAutofillManager, paramInt1, paramAutofillId, paramInt2, paramInt3, paramRect, paramIAutofillWindowPresenter));
      }
    }
    
    public void setSaveUiState(int paramInt, boolean paramBoolean)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.QIW_100CKwHzdHffwaus9KOEHCA(localAutofillManager, paramInt, paramBoolean));
      }
    }
    
    public void setSessionFinished(int paramInt)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.V_s28jF4_S72cRk4llkGpwbJnmk(localAutofillManager, paramInt));
      }
    }
    
    public void setState(int paramInt)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.qH36EJk2Hkdja9ZZmTxqYPyr0YA(localAutofillManager, paramInt));
      }
    }
    
    public void setTrackedViews(int paramInt, AutofillId[] paramArrayOfAutofillId1, boolean paramBoolean1, boolean paramBoolean2, AutofillId[] paramArrayOfAutofillId2, AutofillId paramAutofillId)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.BPlC2x7GLNHFS92rPUSzbcpFhUc(localAutofillManager, paramInt, paramArrayOfAutofillId1, paramBoolean1, paramBoolean2, paramArrayOfAutofillId2, paramAutofillId));
      }
    }
    
    public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent)
    {
      AutofillManager localAutofillManager = (AutofillManager)mAfm.get();
      if (localAutofillManager != null) {
        localAutofillManager.post(new _..Lambda.AutofillManager.AutofillManagerClient.pM5e3ez5KTBdZt4d8qLEERBUSiU(localAutofillManager, paramIntentSender, paramIntent));
      }
    }
  }
  
  private final class CompatibilityBridge
    implements AccessibilityManager.AccessibilityPolicy
  {
    @GuardedBy("mLock")
    AccessibilityServiceInfo mCompatServiceInfo;
    @GuardedBy("mLock")
    private final Rect mFocusedBounds = new Rect();
    @GuardedBy("mLock")
    private long mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
    @GuardedBy("mLock")
    private int mFocusedWindowId = -1;
    @GuardedBy("mLock")
    private final Rect mTempBounds = new Rect();
    
    CompatibilityBridge()
    {
      AccessibilityManager.getInstance(mContext).setAccessibilityPolicy(this);
    }
    
    private View findViewByAccessibilityId(int paramInt, long paramLong)
    {
      AutofillManager.AutofillClient localAutofillClient = AutofillManager.this.getClient();
      if (localAutofillClient == null) {
        return null;
      }
      return localAutofillClient.autofillClientFindViewByAccessibilityIdTraversal(AccessibilityNodeInfo.getAccessibilityViewId(paramLong), paramInt);
    }
    
    private AccessibilityNodeInfo findVirtualNodeByAccessibilityId(View paramView, int paramInt)
    {
      paramView = paramView.getAccessibilityNodeProvider();
      if (paramView == null) {
        return null;
      }
      return paramView.createAccessibilityNodeInfo(paramInt);
    }
    
    private AccessibilityServiceInfo getCompatServiceInfo()
    {
      synchronized (mLock)
      {
        if (mCompatServiceInfo != null)
        {
          localObject2 = mCompatServiceInfo;
          return localObject2;
        }
        Object localObject2 = new android/content/Intent;
        ((Intent)localObject2).<init>();
        Object localObject4 = new android/content/ComponentName;
        ((ComponentName)localObject4).<init>("android", "com.android.server.autofill.AutofillCompatAccessibilityService");
        ((Intent)localObject2).setComponent((ComponentName)localObject4);
        ResolveInfo localResolveInfo = mContext.getPackageManager().resolveService((Intent)localObject2, 1048704);
        try
        {
          localObject4 = new android/accessibilityservice/AccessibilityServiceInfo;
          ((AccessibilityServiceInfo)localObject4).<init>(localResolveInfo, mContext);
          mCompatServiceInfo = ((AccessibilityServiceInfo)localObject4);
          localObject2 = mCompatServiceInfo;
          return localObject2;
        }
        catch (XmlPullParserException|IOException localXmlPullParserException)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Cannot find compat autofill service:");
          localStringBuilder.append(localObject2);
          Log.e("AutofillManager", localStringBuilder.toString());
          localObject2 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject2).<init>("Cannot find compat autofill service");
          throw ((Throwable)localObject2);
        }
      }
    }
    
    private boolean isVirtualNode(int paramInt)
    {
      boolean bool;
      if ((paramInt != -1) && (paramInt != Integer.MAX_VALUE)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void notifyValueChanged(int paramInt, long paramLong)
    {
      int i = AccessibilityNodeInfo.getVirtualDescendantId(paramLong);
      if (!isVirtualNode(i)) {
        return;
      }
      View localView = findViewByAccessibilityId(paramInt, paramLong);
      if (localView == null) {
        return;
      }
      AccessibilityNodeInfo localAccessibilityNodeInfo = findVirtualNodeByAccessibilityId(localView, i);
      if (localAccessibilityNodeInfo == null) {
        return;
      }
      notifyValueChanged(localView, i, AutofillValue.forText(localAccessibilityNodeInfo.getText()));
    }
    
    private void notifyViewClicked(int paramInt, long paramLong)
    {
      int i = AccessibilityNodeInfo.getVirtualDescendantId(paramLong);
      if (!isVirtualNode(i)) {
        return;
      }
      View localView = findViewByAccessibilityId(paramInt, paramLong);
      if (localView == null) {
        return;
      }
      if (findVirtualNodeByAccessibilityId(localView, i) == null) {
        return;
      }
      notifyViewClicked(localView, i);
    }
    
    private boolean notifyViewEntered(int paramInt, long paramLong, Rect paramRect)
    {
      int i = AccessibilityNodeInfo.getVirtualDescendantId(paramLong);
      if (!isVirtualNode(i)) {
        return false;
      }
      View localView = findViewByAccessibilityId(paramInt, paramLong);
      if (localView == null) {
        return false;
      }
      AccessibilityNodeInfo localAccessibilityNodeInfo = findVirtualNodeByAccessibilityId(localView, i);
      if (localAccessibilityNodeInfo == null) {
        return false;
      }
      if (!localAccessibilityNodeInfo.isEditable()) {
        return false;
      }
      Rect localRect = mTempBounds;
      localAccessibilityNodeInfo.getBoundsInScreen(localRect);
      if (localRect.equals(paramRect)) {
        return false;
      }
      paramRect.set(localRect);
      notifyViewEntered(localView, i, localRect);
      return true;
    }
    
    private void notifyViewExited(int paramInt, long paramLong)
    {
      int i = AccessibilityNodeInfo.getVirtualDescendantId(paramLong);
      if (!isVirtualNode(i)) {
        return;
      }
      View localView = findViewByAccessibilityId(paramInt, paramLong);
      if (localView == null) {
        return;
      }
      notifyViewExited(localView, i);
    }
    
    @GuardedBy("mLock")
    private void updateTrackedViewsLocked()
    {
      if (mTrackedViews != null) {
        mTrackedViews.onVisibleForAutofillChangedLocked();
      }
    }
    
    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int paramInt, List<AccessibilityServiceInfo> paramList)
    {
      Object localObject = paramList;
      if (paramList == null) {
        localObject = new ArrayList();
      }
      ((List)localObject).add(getCompatServiceInfo());
      return localObject;
    }
    
    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(List<AccessibilityServiceInfo> paramList)
    {
      Object localObject = paramList;
      if (paramList == null) {
        localObject = new ArrayList();
      }
      ((List)localObject).add(getCompatServiceInfo());
      return localObject;
    }
    
    public int getRelevantEventTypes(int paramInt)
    {
      return paramInt | 0x8 | 0x10 | 0x1 | 0x800;
    }
    
    public boolean isEnabled(boolean paramBoolean)
    {
      return true;
    }
    
    public AccessibilityEvent onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean, int paramInt)
    {
      paramInt = paramAccessibilityEvent.getEventType();
      if (paramInt != 1)
      {
        if (paramInt != 8)
        {
          if (paramInt != 16)
          {
            if (paramInt != 2048) {
              break label329;
            }
            AutofillManager.AutofillClient localAutofillClient = AutofillManager.this.getClient();
            if (localAutofillClient == null) {
              break label329;
            }
            synchronized (mLock)
            {
              if (localAutofillClient.autofillClientIsFillUiShowing()) {
                notifyViewEntered(mFocusedWindowId, mFocusedNodeId, mFocusedBounds);
              }
              updateTrackedViewsLocked();
            }
          }
          synchronized (mLock)
          {
            if ((mFocusedWindowId == paramAccessibilityEvent.getWindowId()) && (mFocusedNodeId == paramAccessibilityEvent.getSourceNodeId())) {
              notifyValueChanged(paramAccessibilityEvent.getWindowId(), paramAccessibilityEvent.getSourceNodeId());
            }
          }
        }
        synchronized (mLock)
        {
          if ((mFocusedWindowId == paramAccessibilityEvent.getWindowId()) && (mFocusedNodeId == paramAccessibilityEvent.getSourceNodeId())) {
            return paramAccessibilityEvent;
          }
          if ((mFocusedWindowId != -1) && (mFocusedNodeId != AccessibilityNodeInfo.UNDEFINED_NODE_ID))
          {
            notifyViewExited(mFocusedWindowId, mFocusedNodeId);
            mFocusedWindowId = -1;
            mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            mFocusedBounds.set(0, 0, 0, 0);
          }
          paramInt = paramAccessibilityEvent.getWindowId();
          long l = paramAccessibilityEvent.getSourceNodeId();
          if (notifyViewEntered(paramInt, l, mFocusedBounds))
          {
            mFocusedWindowId = paramInt;
            mFocusedNodeId = l;
          }
        }
      }
      synchronized (mLock)
      {
        notifyViewClicked(paramAccessibilityEvent.getWindowId(), paramAccessibilityEvent.getSourceNodeId());
        label329:
        if (!paramBoolean) {
          paramAccessibilityEvent = null;
        }
        return paramAccessibilityEvent;
      }
    }
  }
  
  private class TrackedViews
  {
    private ArraySet<AutofillId> mInvisibleTrackedIds;
    private ArraySet<AutofillId> mVisibleTrackedIds;
    
    TrackedViews(AutofillId[] paramArrayOfAutofillId)
    {
      Object localObject = AutofillManager.this.getClient();
      if ((!ArrayUtils.isEmpty(paramArrayOfAutofillId)) && (localObject != null))
      {
        if (((AutofillManager.AutofillClient)localObject).autofillClientIsVisibleForAutofill())
        {
          if (Helper.sVerbose) {
            Log.v("AutofillManager", "client is visible, check tracked ids");
          }
          localObject = ((AutofillManager.AutofillClient)localObject).autofillClientGetViewVisibility(paramArrayOfAutofillId);
        }
        else
        {
          localObject = new boolean[paramArrayOfAutofillId.length];
        }
        int i = paramArrayOfAutofillId.length;
        for (int j = 0; j < i; j++)
        {
          AutofillId localAutofillId = paramArrayOfAutofillId[j];
          if (localObject[j] != 0) {
            mVisibleTrackedIds = addToSet(mVisibleTrackedIds, localAutofillId);
          } else {
            mInvisibleTrackedIds = addToSet(mInvisibleTrackedIds, localAutofillId);
          }
        }
      }
      if (Helper.sVerbose)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("TrackedViews(trackedIds=");
        ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfAutofillId));
        ((StringBuilder)localObject).append("):  mVisibleTrackedIds=");
        ((StringBuilder)localObject).append(mVisibleTrackedIds);
        ((StringBuilder)localObject).append(" mInvisibleTrackedIds=");
        ((StringBuilder)localObject).append(mInvisibleTrackedIds);
        Log.v("AutofillManager", ((StringBuilder)localObject).toString());
      }
      if (mVisibleTrackedIds == null) {
        AutofillManager.this.finishSessionLocked();
      }
    }
    
    private <T> ArraySet<T> addToSet(ArraySet<T> paramArraySet, T paramT)
    {
      Object localObject = paramArraySet;
      if (paramArraySet == null) {
        localObject = new ArraySet(1);
      }
      ((ArraySet)localObject).add(paramT);
      return localObject;
    }
    
    private <T> boolean isInSet(ArraySet<T> paramArraySet, T paramT)
    {
      boolean bool;
      if ((paramArraySet != null) && (paramArraySet.contains(paramT))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private <T> ArraySet<T> removeFromSet(ArraySet<T> paramArraySet, T paramT)
    {
      if (paramArraySet == null) {
        return null;
      }
      paramArraySet.remove(paramT);
      if (paramArraySet.isEmpty()) {
        return null;
      }
      return paramArraySet;
    }
    
    @GuardedBy("mLock")
    void notifyViewVisibilityChangedLocked(AutofillId paramAutofillId, boolean paramBoolean)
    {
      if (Helper.sDebug)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("notifyViewVisibilityChangedLocked(): id=");
        localStringBuilder.append(paramAutofillId);
        localStringBuilder.append(" isVisible=");
        localStringBuilder.append(paramBoolean);
        Log.d("AutofillManager", localStringBuilder.toString());
      }
      if (AutofillManager.this.isClientVisibleForAutofillLocked()) {
        if (paramBoolean)
        {
          if (isInSet(mInvisibleTrackedIds, paramAutofillId))
          {
            mInvisibleTrackedIds = removeFromSet(mInvisibleTrackedIds, paramAutofillId);
            mVisibleTrackedIds = addToSet(mVisibleTrackedIds, paramAutofillId);
          }
        }
        else if (isInSet(mVisibleTrackedIds, paramAutofillId))
        {
          mVisibleTrackedIds = removeFromSet(mVisibleTrackedIds, paramAutofillId);
          mInvisibleTrackedIds = addToSet(mInvisibleTrackedIds, paramAutofillId);
        }
      }
      if (mVisibleTrackedIds == null)
      {
        if (Helper.sVerbose)
        {
          paramAutofillId = new StringBuilder();
          paramAutofillId.append("No more visible ids. Invisibile = ");
          paramAutofillId.append(mInvisibleTrackedIds);
          Log.v("AutofillManager", paramAutofillId.toString());
        }
        AutofillManager.this.finishSessionLocked();
      }
    }
    
    @GuardedBy("mLock")
    void onVisibleForAutofillChangedLocked()
    {
      Object localObject1 = AutofillManager.this.getClient();
      Object localObject2 = null;
      Object localObject3 = null;
      if (localObject1 != null)
      {
        if (Helper.sVerbose)
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("onVisibleForAutofillChangedLocked(): inv= ");
          ((StringBuilder)localObject4).append(mInvisibleTrackedIds);
          ((StringBuilder)localObject4).append(" vis=");
          ((StringBuilder)localObject4).append(mVisibleTrackedIds);
          Log.v("AutofillManager", ((StringBuilder)localObject4).toString());
        }
        Object localObject4 = mInvisibleTrackedIds;
        int i = 0;
        Object localObject5;
        Object localObject6;
        int j;
        int k;
        if (localObject4 != null)
        {
          localObject5 = new ArrayList(mInvisibleTrackedIds);
          localObject6 = ((AutofillManager.AutofillClient)localObject1).autofillClientGetViewVisibility(Helper.toArray((Collection)localObject5));
          j = ((ArrayList)localObject5).size();
          localObject2 = null;
          localObject3 = null;
          k = 0;
          while (k < j)
          {
            AutofillId localAutofillId = (AutofillId)((ArrayList)localObject5).get(k);
            if (localObject6[k] != 0)
            {
              localObject7 = addToSet((ArraySet)localObject3, localAutofillId);
              localObject3 = localObject7;
              localObject4 = localObject2;
              if (Helper.sDebug)
              {
                localObject3 = new StringBuilder();
                ((StringBuilder)localObject3).append("onVisibleForAutofill() ");
                ((StringBuilder)localObject3).append(localAutofillId);
                ((StringBuilder)localObject3).append(" became visible");
                Log.d("AutofillManager", ((StringBuilder)localObject3).toString());
                localObject3 = localObject7;
                localObject4 = localObject2;
              }
            }
            else
            {
              localObject4 = addToSet((ArraySet)localObject2, localAutofillId);
            }
            k++;
            localObject2 = localObject4;
          }
          localObject4 = localObject3;
          localObject3 = localObject2;
          localObject2 = localObject4;
        }
        localObject4 = localObject2;
        Object localObject7 = localObject3;
        if (mVisibleTrackedIds != null)
        {
          localObject6 = new ArrayList(mVisibleTrackedIds);
          localObject1 = ((AutofillManager.AutofillClient)localObject1).autofillClientGetViewVisibility(Helper.toArray((Collection)localObject6));
          j = ((ArrayList)localObject6).size();
          k = i;
          for (;;)
          {
            localObject4 = localObject2;
            localObject7 = localObject3;
            if (k >= j) {
              break;
            }
            localObject5 = (AutofillId)((ArrayList)localObject6).get(k);
            if (localObject1[k] != 0)
            {
              localObject4 = addToSet((ArraySet)localObject2, localObject5);
            }
            else
            {
              localObject7 = addToSet((ArraySet)localObject3, localObject5);
              localObject4 = localObject2;
              localObject3 = localObject7;
              if (Helper.sDebug)
              {
                localObject3 = new StringBuilder();
                ((StringBuilder)localObject3).append("onVisibleForAutofill() ");
                ((StringBuilder)localObject3).append(localObject5);
                ((StringBuilder)localObject3).append(" became invisible");
                Log.d("AutofillManager", ((StringBuilder)localObject3).toString());
                localObject3 = localObject7;
                localObject4 = localObject2;
              }
            }
            k++;
            localObject2 = localObject4;
          }
        }
        mInvisibleTrackedIds = ((ArraySet)localObject7);
        mVisibleTrackedIds = ((ArraySet)localObject4);
      }
      if (mVisibleTrackedIds == null)
      {
        if (Helper.sVerbose) {
          Log.v("AutofillManager", "onVisibleForAutofillChangedLocked(): no more visible ids");
        }
        AutofillManager.this.finishSessionLocked();
      }
    }
  }
}
