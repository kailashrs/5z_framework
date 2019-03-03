package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.IWindow;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IntPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AccessibilityManager
{
  public static final String ACTION_CHOOSE_ACCESSIBILITY_BUTTON = "com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON";
  public static final int AUTOCLICK_DELAY_DEFAULT = 600;
  public static final int DALTONIZER_CORRECT_DEUTERANOMALY = 12;
  public static final int DALTONIZER_DISABLED = -1;
  public static final int DALTONIZER_SIMULATE_MONOCHROMACY = 0;
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "AccessibilityManager";
  public static final int STATE_FLAG_ACCESSIBILITY_ENABLED = 1;
  public static final int STATE_FLAG_HIGH_TEXT_CONTRAST_ENABLED = 4;
  public static final int STATE_FLAG_TOUCH_EXPLORATION_ENABLED = 2;
  private static AccessibilityManager sInstance;
  static final Object sInstanceSync = new Object();
  AccessibilityPolicy mAccessibilityPolicy;
  private final ArrayMap<AccessibilityStateChangeListener, Handler> mAccessibilityStateChangeListeners = new ArrayMap();
  final Handler.Callback mCallback = new MyCallback(null);
  private final IAccessibilityManagerClient.Stub mClient = new IAccessibilityManagerClient.Stub()
  {
    public void notifyServicesStateChanged()
    {
      synchronized (mLock)
      {
        if (mServicesStateChangeListeners.isEmpty()) {
          return;
        }
        ArrayMap localArrayMap = new android/util/ArrayMap;
        localArrayMap.<init>(mServicesStateChangeListeners);
        int i = localArrayMap.size();
        for (int j = 0; j < i; j++)
        {
          ??? = (AccessibilityManager.AccessibilityServicesStateChangeListener)mServicesStateChangeListeners.keyAt(j);
          ((Handler)mServicesStateChangeListeners.valueAt(j)).post(new _..Lambda.AccessibilityManager.1.o7fCplskH9NlBwJvkl6NoZ0L_BA(this, (AccessibilityManager.AccessibilityServicesStateChangeListener)???));
        }
        return;
      }
    }
    
    public void setRelevantEventTypes(int paramAnonymousInt)
    {
      mRelevantEventTypes = paramAnonymousInt;
    }
    
    public void setState(int paramAnonymousInt)
    {
      mHandler.obtainMessage(1, paramAnonymousInt, 0).sendToTarget();
    }
  };
  final Handler mHandler;
  private final ArrayMap<HighTextContrastChangeListener, Handler> mHighTextContrastStateChangeListeners = new ArrayMap();
  boolean mIsEnabled;
  boolean mIsHighTextContrastEnabled;
  boolean mIsTouchExplorationEnabled;
  private final Object mLock = new Object();
  int mRelevantEventTypes = -1;
  private SparseArray<List<AccessibilityRequestPreparer>> mRequestPreparerLists;
  private IAccessibilityManager mService;
  private final ArrayMap<AccessibilityServicesStateChangeListener, Handler> mServicesStateChangeListeners = new ArrayMap();
  private final ArrayMap<TouchExplorationStateChangeListener, Handler> mTouchExplorationStateChangeListeners = new ArrayMap();
  final int mUserId;
  
  public AccessibilityManager(Context arg1, IAccessibilityManager paramIAccessibilityManager, int paramInt)
  {
    mHandler = new Handler(???.getMainLooper(), mCallback);
    mUserId = paramInt;
    synchronized (mLock)
    {
      tryConnectToServiceLocked(paramIAccessibilityManager);
      return;
    }
  }
  
  public AccessibilityManager(Handler arg1, IAccessibilityManager paramIAccessibilityManager, int paramInt)
  {
    mHandler = ???;
    mUserId = paramInt;
    synchronized (mLock)
    {
      tryConnectToServiceLocked(paramIAccessibilityManager);
      return;
    }
  }
  
  public static AccessibilityManager getInstance(Context paramContext)
  {
    synchronized (sInstanceSync)
    {
      if (sInstance == null)
      {
        int i;
        if ((Binder.getCallingUid() != 1000) && (paramContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") != 0) && (paramContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") != 0)) {
          i = paramContext.getUserId();
        } else {
          i = -2;
        }
        AccessibilityManager localAccessibilityManager = new android/view/accessibility/AccessibilityManager;
        localAccessibilityManager.<init>(paramContext, null, i);
        sInstance = localAccessibilityManager;
      }
      return sInstance;
    }
  }
  
  private IAccessibilityManager getServiceLocked()
  {
    if (mService == null) {
      tryConnectToServiceLocked(null);
    }
    return mService;
  }
  
  public static boolean isAccessibilityButtonSupported()
  {
    return Resources.getSystem().getBoolean(17957026);
  }
  
  private void notifyAccessibilityStateChanged()
  {
    synchronized (mLock)
    {
      if (mAccessibilityStateChangeListeners.isEmpty()) {
        return;
      }
      boolean bool = isEnabled();
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>(mAccessibilityStateChangeListeners);
      int i = localArrayMap.size();
      for (int j = 0; j < i; j++)
      {
        ??? = (AccessibilityStateChangeListener)localArrayMap.keyAt(j);
        ((Handler)localArrayMap.valueAt(j)).post(new _..Lambda.AccessibilityManager.yzw5NYY7_MfAQ9gLy3mVllchaXo((AccessibilityStateChangeListener)???, bool));
      }
      return;
    }
  }
  
  private void notifyHighTextContrastStateChanged()
  {
    synchronized (mLock)
    {
      if (mHighTextContrastStateChangeListeners.isEmpty()) {
        return;
      }
      boolean bool = mIsHighTextContrastEnabled;
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>(mHighTextContrastStateChangeListeners);
      int i = localArrayMap.size();
      for (int j = 0; j < i; j++)
      {
        ??? = (HighTextContrastChangeListener)localArrayMap.keyAt(j);
        ((Handler)localArrayMap.valueAt(j)).post(new _..Lambda.AccessibilityManager.4M6GrmFiqsRwVzn352N10DcU6RM((HighTextContrastChangeListener)???, bool));
      }
      return;
    }
  }
  
  private void notifyTouchExplorationStateChanged()
  {
    synchronized (mLock)
    {
      if (mTouchExplorationStateChangeListeners.isEmpty()) {
        return;
      }
      boolean bool = mIsTouchExplorationEnabled;
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>(mTouchExplorationStateChangeListeners);
      int i = localArrayMap.size();
      for (int j = 0; j < i; j++)
      {
        ??? = (TouchExplorationStateChangeListener)localArrayMap.keyAt(j);
        ((Handler)localArrayMap.valueAt(j)).post(new _..Lambda.AccessibilityManager.a0OtrjOl35tiW2vwyvAmY6_LiLI((TouchExplorationStateChangeListener)???, bool));
      }
      return;
    }
  }
  
  private void setStateLocked(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3;
    if ((paramInt & 0x2) != 0) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    if ((paramInt & 0x4) != 0) {
      bool1 = true;
    }
    boolean bool4 = isEnabled();
    boolean bool5 = mIsTouchExplorationEnabled;
    boolean bool6 = mIsHighTextContrastEnabled;
    mIsEnabled = bool2;
    mIsTouchExplorationEnabled = bool3;
    mIsHighTextContrastEnabled = bool1;
    if (bool4 != isEnabled()) {
      notifyAccessibilityStateChanged();
    }
    if (bool5 != bool3) {
      notifyTouchExplorationStateChanged();
    }
    if (bool6 != bool1) {
      notifyHighTextContrastStateChanged();
    }
  }
  
  private void tryConnectToServiceLocked(IAccessibilityManager paramIAccessibilityManager)
  {
    IAccessibilityManager localIAccessibilityManager = paramIAccessibilityManager;
    if (paramIAccessibilityManager == null)
    {
      paramIAccessibilityManager = ServiceManager.getService("accessibility");
      if (paramIAccessibilityManager == null) {
        return;
      }
      localIAccessibilityManager = IAccessibilityManager.Stub.asInterface(paramIAccessibilityManager);
    }
    try
    {
      long l = localIAccessibilityManager.addClient(mClient, mUserId);
      setStateLocked(IntPair.first(l));
      mRelevantEventTypes = IntPair.second(l);
      mService = localIAccessibilityManager;
    }
    catch (RemoteException paramIAccessibilityManager)
    {
      Log.e("AccessibilityManager", "AccessibilityManagerService is dead", paramIAccessibilityManager);
    }
  }
  
  public int addAccessibilityInteractionConnection(IWindow paramIWindow, String paramString, IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection)
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return -1;
      }
      int i = mUserId;
      try
      {
        i = localIAccessibilityManager.addAccessibilityInteractionConnection(paramIWindow, paramIAccessibilityInteractionConnection, paramString, i);
        return i;
      }
      catch (RemoteException paramIWindow)
      {
        Log.e("AccessibilityManager", "Error while adding an accessibility interaction connection. ", paramIWindow);
        return -1;
      }
    }
  }
  
  public void addAccessibilityRequestPreparer(AccessibilityRequestPreparer paramAccessibilityRequestPreparer)
  {
    if (mRequestPreparerLists == null) {
      mRequestPreparerLists = new SparseArray(1);
    }
    int i = paramAccessibilityRequestPreparer.getView().getAccessibilityViewId();
    List localList = (List)mRequestPreparerLists.get(i);
    Object localObject = localList;
    if (localList == null)
    {
      localObject = new ArrayList(1);
      mRequestPreparerLists.put(i, localObject);
    }
    ((List)localObject).add(paramAccessibilityRequestPreparer);
  }
  
  public void addAccessibilityServicesStateChangeListener(AccessibilityServicesStateChangeListener paramAccessibilityServicesStateChangeListener, Handler paramHandler)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = mServicesStateChangeListeners;
      if (paramHandler == null) {
        paramHandler = mHandler;
      }
      localArrayMap.put(paramAccessibilityServicesStateChangeListener, paramHandler);
      return;
    }
  }
  
  public void addAccessibilityStateChangeListener(AccessibilityStateChangeListener paramAccessibilityStateChangeListener, Handler paramHandler)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = mAccessibilityStateChangeListeners;
      if (paramHandler == null) {
        paramHandler = mHandler;
      }
      localArrayMap.put(paramAccessibilityStateChangeListener, paramHandler);
      return;
    }
  }
  
  public boolean addAccessibilityStateChangeListener(AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
  {
    addAccessibilityStateChangeListener(paramAccessibilityStateChangeListener, null);
    return true;
  }
  
  public void addHighTextContrastStateChangeListener(HighTextContrastChangeListener paramHighTextContrastChangeListener, Handler paramHandler)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = mHighTextContrastStateChangeListeners;
      if (paramHandler == null) {
        paramHandler = mHandler;
      }
      localArrayMap.put(paramHighTextContrastChangeListener, paramHandler);
      return;
    }
  }
  
  public void addTouchExplorationStateChangeListener(TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener, Handler paramHandler)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = mTouchExplorationStateChangeListeners;
      if (paramHandler == null) {
        paramHandler = mHandler;
      }
      localArrayMap.put(paramTouchExplorationStateChangeListener, paramHandler);
      return;
    }
  }
  
  public boolean addTouchExplorationStateChangeListener(TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
  {
    addTouchExplorationStateChangeListener(paramTouchExplorationStateChangeListener, null);
    return true;
  }
  
  @Deprecated
  public List<ServiceInfo> getAccessibilityServiceList()
  {
    List localList = getInstalledAccessibilityServiceList();
    ArrayList localArrayList = new ArrayList();
    int i = localList.size();
    for (int j = 0; j < i; j++) {
      localArrayList.add(getgetResolveInfoserviceInfo);
    }
    return Collections.unmodifiableList(localArrayList);
  }
  
  @VisibleForTesting
  public Handler.Callback getCallback()
  {
    return mCallback;
  }
  
  public IAccessibilityManagerClient getClient()
  {
    return mClient;
  }
  
  public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int paramInt)
  {
    synchronized (mLock)
    {
      Object localObject2 = getServiceLocked();
      if (localObject2 == null)
      {
        localObject2 = Collections.emptyList();
        return localObject2;
      }
      int i = mUserId;
      ??? = null;
      try
      {
        localObject2 = ((IAccessibilityManager)localObject2).getEnabledAccessibilityServiceList(paramInt, i);
        ??? = localObject2;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error while obtaining the installed AccessibilityServices. ", localRemoteException);
      }
      Object localObject3 = ???;
      if (mAccessibilityPolicy != null) {
        localObject3 = mAccessibilityPolicy.getEnabledAccessibilityServiceList(paramInt, ???);
      }
      if (localObject3 != null) {
        return Collections.unmodifiableList((List)localObject3);
      }
      return Collections.emptyList();
    }
  }
  
  public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList()
  {
    synchronized (mLock)
    {
      Object localObject2 = getServiceLocked();
      if (localObject2 == null)
      {
        localObject2 = Collections.emptyList();
        return localObject2;
      }
      int i = mUserId;
      ??? = null;
      try
      {
        localObject2 = ((IAccessibilityManager)localObject2).getInstalledAccessibilityServiceList(i);
        ??? = localObject2;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error while obtaining the installed AccessibilityServices. ", localRemoteException);
      }
      Object localObject3 = ???;
      if (mAccessibilityPolicy != null) {
        localObject3 = mAccessibilityPolicy.getInstalledAccessibilityServiceList(???);
      }
      if (localObject3 != null) {
        return Collections.unmodifiableList((List)localObject3);
      }
      return Collections.emptyList();
    }
  }
  
  public AccessibilityServiceInfo getInstalledServiceInfoWithComponentName(ComponentName paramComponentName)
  {
    List localList = getInstalledAccessibilityServiceList();
    if ((localList != null) && (paramComponentName != null))
    {
      for (int i = 0; i < localList.size(); i++) {
        if (paramComponentName.equals(((AccessibilityServiceInfo)localList.get(i)).getComponentName())) {
          return (AccessibilityServiceInfo)localList.get(i);
        }
      }
      return null;
    }
    return null;
  }
  
  public List<AccessibilityRequestPreparer> getRequestPreparersForAccessibilityId(int paramInt)
  {
    if (mRequestPreparerLists == null) {
      return null;
    }
    return (List)mRequestPreparerLists.get(paramInt);
  }
  
  public void interrupt()
  {
    synchronized (mLock)
    {
      Object localObject2 = getServiceLocked();
      if (localObject2 == null) {
        return;
      }
      if (!isEnabled())
      {
        if (Looper.myLooper() != Looper.getMainLooper())
        {
          Log.e("AccessibilityManager", "Interrupt called with accessibility disabled");
          return;
        }
        localObject2 = new java/lang/IllegalStateException;
        ((IllegalStateException)localObject2).<init>("Accessibility off. Did you forget to check that?");
        throw ((Throwable)localObject2);
      }
      int i = mUserId;
      try
      {
        ((IAccessibilityManager)localObject2).interrupt(i);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error while requesting interrupt from all services. ", localRemoteException);
      }
      return;
    }
  }
  
  public boolean isAccessibilityVolumeStreamActive()
  {
    List localList = getEnabledAccessibilityServiceList(-1);
    for (int i = 0; i < localList.size(); i++) {
      if ((getflags & 0x80) != 0) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isEnabled()
  {
    synchronized (mLock)
    {
      boolean bool;
      if ((!mIsEnabled) && ((mAccessibilityPolicy == null) || (!mAccessibilityPolicy.isEnabled(mIsEnabled)))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
  }
  
  public boolean isHighTextContrastEnabled()
  {
    synchronized (mLock)
    {
      if (getServiceLocked() == null) {
        return false;
      }
      boolean bool = mIsHighTextContrastEnabled;
      return bool;
    }
  }
  
  public boolean isTouchExplorationEnabled()
  {
    synchronized (mLock)
    {
      if (getServiceLocked() == null) {
        return false;
      }
      boolean bool = mIsTouchExplorationEnabled;
      return bool;
    }
  }
  
  public void notifyAccessibilityButtonClicked()
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return;
      }
      try
      {
        localIAccessibilityManager.notifyAccessibilityButtonClicked();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error while dispatching accessibility button click", localRemoteException);
      }
      return;
    }
  }
  
  public void notifyAccessibilityButtonVisibilityChanged(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return;
      }
      try
      {
        localIAccessibilityManager.notifyAccessibilityButtonVisibilityChanged(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error while dispatching accessibility button visibility change", localRemoteException);
      }
      return;
    }
  }
  
  public void performAccessibilityShortcut()
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return;
      }
      try
      {
        localIAccessibilityManager.performAccessibilityShortcut();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("AccessibilityManager", "Error performing accessibility shortcut. ", localRemoteException);
      }
      return;
    }
  }
  
  public void removeAccessibilityInteractionConnection(IWindow paramIWindow)
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return;
      }
      try
      {
        localIAccessibilityManager.removeAccessibilityInteractionConnection(paramIWindow);
      }
      catch (RemoteException paramIWindow)
      {
        Log.e("AccessibilityManager", "Error while removing an accessibility interaction connection. ", paramIWindow);
      }
      return;
    }
  }
  
  public void removeAccessibilityRequestPreparer(AccessibilityRequestPreparer paramAccessibilityRequestPreparer)
  {
    if (mRequestPreparerLists == null) {
      return;
    }
    int i = paramAccessibilityRequestPreparer.getView().getAccessibilityViewId();
    List localList = (List)mRequestPreparerLists.get(i);
    if (localList != null)
    {
      localList.remove(paramAccessibilityRequestPreparer);
      if (localList.isEmpty()) {
        mRequestPreparerLists.remove(i);
      }
    }
  }
  
  public void removeAccessibilityServicesStateChangeListener(AccessibilityServicesStateChangeListener paramAccessibilityServicesStateChangeListener)
  {
    synchronized (mLock)
    {
      mServicesStateChangeListeners.remove(paramAccessibilityServicesStateChangeListener);
      return;
    }
  }
  
  public boolean removeAccessibilityStateChangeListener(AccessibilityStateChangeListener paramAccessibilityStateChangeListener)
  {
    synchronized (mLock)
    {
      int i = mAccessibilityStateChangeListeners.indexOfKey(paramAccessibilityStateChangeListener);
      mAccessibilityStateChangeListeners.remove(paramAccessibilityStateChangeListener);
      boolean bool;
      if (i >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public void removeHighTextContrastStateChangeListener(HighTextContrastChangeListener paramHighTextContrastChangeListener)
  {
    synchronized (mLock)
    {
      mHighTextContrastStateChangeListeners.remove(paramHighTextContrastChangeListener);
      return;
    }
  }
  
  public boolean removeTouchExplorationStateChangeListener(TouchExplorationStateChangeListener paramTouchExplorationStateChangeListener)
  {
    synchronized (mLock)
    {
      int i = mTouchExplorationStateChangeListeners.indexOfKey(paramTouchExplorationStateChangeListener);
      mTouchExplorationStateChangeListeners.remove(paramTouchExplorationStateChangeListener);
      boolean bool;
      if (i >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  /* Error */
  public void sendAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 91	android/view/accessibility/AccessibilityManager:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: invokespecial 293	android/view/accessibility/AccessibilityManager:getServiceLocked	()Landroid/view/accessibility/IAccessibilityManager;
    //   11: astore_3
    //   12: aload_3
    //   13: ifnonnull +6 -> 19
    //   16: aload_2
    //   17: monitorexit
    //   18: return
    //   19: aload_1
    //   20: invokestatic 503	android/os/SystemClock:uptimeMillis	()J
    //   23: invokevirtual 509	android/view/accessibility/AccessibilityEvent:setEventTime	(J)V
    //   26: aload_0
    //   27: getfield 398	android/view/accessibility/AccessibilityManager:mAccessibilityPolicy	Landroid/view/accessibility/AccessibilityManager$AccessibilityPolicy;
    //   30: ifnull +35 -> 65
    //   33: aload_0
    //   34: getfield 398	android/view/accessibility/AccessibilityManager:mAccessibilityPolicy	Landroid/view/accessibility/AccessibilityManager$AccessibilityPolicy;
    //   37: aload_1
    //   38: aload_0
    //   39: getfield 244	android/view/accessibility/AccessibilityManager:mIsEnabled	Z
    //   42: aload_0
    //   43: getfield 93	android/view/accessibility/AccessibilityManager:mRelevantEventTypes	I
    //   46: invokeinterface 513 4 0
    //   51: astore 4
    //   53: aload 4
    //   55: astore 5
    //   57: aload 4
    //   59: ifnonnull +9 -> 68
    //   62: aload_2
    //   63: monitorexit
    //   64: return
    //   65: aload_1
    //   66: astore 5
    //   68: aload_0
    //   69: invokevirtual 207	android/view/accessibility/AccessibilityManager:isEnabled	()Z
    //   72: ifne +37 -> 109
    //   75: invokestatic 426	android/os/Looper:myLooper	()Landroid/os/Looper;
    //   78: invokestatic 427	android/os/Looper:getMainLooper	()Landroid/os/Looper;
    //   81: if_acmpeq +15 -> 96
    //   84: ldc 42
    //   86: ldc_w 515
    //   89: invokestatic 432	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   92: pop
    //   93: aload_2
    //   94: monitorexit
    //   95: return
    //   96: new 434	java/lang/IllegalStateException
    //   99: astore_1
    //   100: aload_1
    //   101: ldc_w 436
    //   104: invokespecial 439	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   107: aload_1
    //   108: athrow
    //   109: aload 5
    //   111: invokevirtual 518	android/view/accessibility/AccessibilityEvent:getEventType	()I
    //   114: aload_0
    //   115: getfield 93	android/view/accessibility/AccessibilityManager:mRelevantEventTypes	I
    //   118: iand
    //   119: ifne +6 -> 125
    //   122: aload_2
    //   123: monitorexit
    //   124: return
    //   125: aload_0
    //   126: getfield 129	android/view/accessibility/AccessibilityManager:mUserId	I
    //   129: istore 6
    //   131: aload_2
    //   132: monitorexit
    //   133: invokestatic 521	android/os/Binder:clearCallingIdentity	()J
    //   136: lstore 7
    //   138: aload_3
    //   139: aload 5
    //   141: iload 6
    //   143: invokeinterface 524 3 0
    //   148: lload 7
    //   150: invokestatic 527	android/os/Binder:restoreCallingIdentity	(J)V
    //   153: aload_1
    //   154: aload 5
    //   156: if_acmpeq +7 -> 163
    //   159: aload_1
    //   160: invokevirtual 530	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   163: aload 5
    //   165: invokevirtual 530	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   168: goto +76 -> 244
    //   171: astore 4
    //   173: lload 7
    //   175: invokestatic 527	android/os/Binder:restoreCallingIdentity	(J)V
    //   178: aload 4
    //   180: athrow
    //   181: astore 4
    //   183: goto +62 -> 245
    //   186: astore_2
    //   187: new 532	java/lang/StringBuilder
    //   190: astore 4
    //   192: aload 4
    //   194: invokespecial 533	java/lang/StringBuilder:<init>	()V
    //   197: aload 4
    //   199: ldc_w 535
    //   202: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   205: pop
    //   206: aload 4
    //   208: aload 5
    //   210: invokevirtual 542	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   213: pop
    //   214: aload 4
    //   216: ldc_w 544
    //   219: invokevirtual 539	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: ldc 42
    //   225: aload 4
    //   227: invokevirtual 548	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   230: aload_2
    //   231: invokestatic 289	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   234: pop
    //   235: aload_1
    //   236: aload 5
    //   238: if_acmpeq -75 -> 163
    //   241: goto -82 -> 159
    //   244: return
    //   245: aload_1
    //   246: aload 5
    //   248: if_acmpeq +7 -> 255
    //   251: aload_1
    //   252: invokevirtual 530	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   255: aload 5
    //   257: invokevirtual 530	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   260: aload 4
    //   262: athrow
    //   263: astore_1
    //   264: aload_2
    //   265: monitorexit
    //   266: aload_1
    //   267: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	268	0	this	AccessibilityManager
    //   0	268	1	paramAccessibilityEvent	AccessibilityEvent
    //   4	128	2	localObject1	Object
    //   186	79	2	localRemoteException	RemoteException
    //   11	128	3	localIAccessibilityManager	IAccessibilityManager
    //   51	7	4	localAccessibilityEvent1	AccessibilityEvent
    //   171	8	4	localObject2	Object
    //   181	1	4	localObject3	Object
    //   190	71	4	localStringBuilder	StringBuilder
    //   55	201	5	localAccessibilityEvent2	AccessibilityEvent
    //   129	13	6	i	int
    //   136	38	7	l	long
    // Exception table:
    //   from	to	target	type
    //   138	148	171	finally
    //   133	138	181	finally
    //   148	153	181	finally
    //   173	181	181	finally
    //   187	235	181	finally
    //   133	138	186	android/os/RemoteException
    //   148	153	186	android/os/RemoteException
    //   173	181	186	android/os/RemoteException
    //   7	12	263	finally
    //   16	18	263	finally
    //   19	53	263	finally
    //   62	64	263	finally
    //   68	95	263	finally
    //   96	109	263	finally
    //   109	124	263	finally
    //   125	133	263	finally
    //   264	266	263	finally
  }
  
  public boolean sendFingerprintGesture(int paramInt)
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return false;
      }
      try
      {
        boolean bool = localIAccessibilityManager.sendFingerprintGesture(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        return false;
      }
    }
  }
  
  public void setAccessibilityPolicy(AccessibilityPolicy paramAccessibilityPolicy)
  {
    synchronized (mLock)
    {
      mAccessibilityPolicy = paramAccessibilityPolicy;
      return;
    }
  }
  
  public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection)
  {
    synchronized (mLock)
    {
      IAccessibilityManager localIAccessibilityManager = getServiceLocked();
      if (localIAccessibilityManager == null) {
        return;
      }
      try
      {
        localIAccessibilityManager.setPictureInPictureActionReplacingConnection(paramIAccessibilityInteractionConnection);
      }
      catch (RemoteException paramIAccessibilityInteractionConnection)
      {
        Log.e("AccessibilityManager", "Error setting picture in picture action replacement", paramIAccessibilityInteractionConnection);
      }
      return;
    }
  }
  
  public static abstract interface AccessibilityPolicy
  {
    public abstract List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int paramInt, List<AccessibilityServiceInfo> paramList);
    
    public abstract List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(List<AccessibilityServiceInfo> paramList);
    
    public abstract int getRelevantEventTypes(int paramInt);
    
    public abstract boolean isEnabled(boolean paramBoolean);
    
    public abstract AccessibilityEvent onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean, int paramInt);
  }
  
  public static abstract interface AccessibilityServicesStateChangeListener
  {
    public abstract void onAccessibilityServicesStateChanged(AccessibilityManager paramAccessibilityManager);
  }
  
  public static abstract interface AccessibilityStateChangeListener
  {
    public abstract void onAccessibilityStateChanged(boolean paramBoolean);
  }
  
  public static abstract interface HighTextContrastChangeListener
  {
    public abstract void onHighTextContrastStateChanged(boolean paramBoolean);
  }
  
  private final class MyCallback
    implements Handler.Callback
  {
    public static final int MSG_SET_STATE = 1;
    
    private MyCallback() {}
    
    public boolean handleMessage(Message arg1)
    {
      int i;
      if (what == 1) {
        i = arg1;
      }
      synchronized (mLock)
      {
        AccessibilityManager.this.setStateLocked(i);
        return true;
      }
    }
  }
  
  public static abstract interface TouchExplorationStateChangeListener
  {
    public abstract void onTouchExplorationStateChanged(boolean paramBoolean);
  }
}
