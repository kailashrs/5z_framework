package android.view;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import java.util.ArrayList;

public final class WindowManagerGlobal
{
  public static final int ADD_APP_EXITING = -4;
  public static final int ADD_BAD_APP_TOKEN = -1;
  public static final int ADD_BAD_SUBWINDOW_TOKEN = -2;
  public static final int ADD_DUPLICATE_ADD = -5;
  public static final int ADD_FLAG_ALWAYS_CONSUME_NAV_BAR = 4;
  public static final int ADD_FLAG_APP_VISIBLE = 2;
  public static final int ADD_FLAG_IN_TOUCH_MODE = 1;
  public static final int ADD_INVALID_DISPLAY = -9;
  public static final int ADD_INVALID_TYPE = -10;
  public static final int ADD_MULTIPLE_SINGLETON = -7;
  public static final int ADD_NOT_APP_TOKEN = -3;
  public static final int ADD_OKAY = 0;
  public static final int ADD_PERMISSION_DENIED = -8;
  public static final int ADD_STARTING_NOT_NEEDED = -6;
  public static final int RELAYOUT_DEFER_SURFACE_DESTROY = 2;
  public static final int RELAYOUT_INSETS_PENDING = 1;
  public static final int RELAYOUT_RES_CONSUME_ALWAYS_NAV_BAR = 64;
  public static final int RELAYOUT_RES_DRAG_RESIZING_DOCKED = 8;
  public static final int RELAYOUT_RES_DRAG_RESIZING_FREEFORM = 16;
  public static final int RELAYOUT_RES_FIRST_TIME = 2;
  public static final int RELAYOUT_RES_IN_TOUCH_MODE = 1;
  public static final int RELAYOUT_RES_SURFACE_CHANGED = 4;
  public static final int RELAYOUT_RES_SURFACE_RESIZED = 32;
  private static final String TAG = "WindowManager";
  private static WindowManagerGlobal sDefaultWindowManager;
  private static IWindowManager sWindowManagerService;
  private static IWindowSession sWindowSession;
  private final ArraySet<View> mDyingViews = new ArraySet();
  private final Object mLock = new Object();
  private final ArrayList<WindowManager.LayoutParams> mParams = new ArrayList();
  private final ArrayList<ViewRootImpl> mRoots = new ArrayList();
  private Runnable mSystemPropertyUpdater;
  private final ArrayList<View> mViews = new ArrayList();
  
  private WindowManagerGlobal() {}
  
  private void doTrimForeground()
  {
    int i = 0;
    synchronized (mLock)
    {
      for (int j = mRoots.size() - 1; j >= 0; j--)
      {
        ViewRootImpl localViewRootImpl = (ViewRootImpl)mRoots.get(j);
        if ((mView != null) && (localViewRootImpl.getHostVisibility() == 0) && (mAttachInfo.mThreadedRenderer != null)) {
          i = 1;
        } else {
          localViewRootImpl.destroyHardwareResources();
        }
      }
      if (i == 0) {
        ThreadedRenderer.trimMemory(80);
      }
      return;
    }
  }
  
  private int findViewLocked(View paramView, boolean paramBoolean)
  {
    int i = mViews.indexOf(paramView);
    if ((paramBoolean) && (i < 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("View=");
      localStringBuilder.append(paramView);
      localStringBuilder.append(" not attached to window manager");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return i;
  }
  
  public static WindowManagerGlobal getInstance()
  {
    try
    {
      if (sDefaultWindowManager == null)
      {
        localWindowManagerGlobal = new android/view/WindowManagerGlobal;
        localWindowManagerGlobal.<init>();
        sDefaultWindowManager = localWindowManagerGlobal;
      }
      WindowManagerGlobal localWindowManagerGlobal = sDefaultWindowManager;
      return localWindowManagerGlobal;
    }
    finally {}
  }
  
  public static IWindowManager getWindowManagerService()
  {
    try
    {
      if (sWindowManagerService == null)
      {
        sWindowManagerService = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
        try
        {
          if (sWindowManagerService != null) {
            ValueAnimator.setDurationScale(sWindowManagerService.getCurrentAnimatorScale());
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      IWindowManager localIWindowManager = sWindowManagerService;
      return localIWindowManager;
    }
    finally {}
  }
  
  private static String getWindowName(ViewRootImpl paramViewRootImpl)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mWindowAttributes.getTitle());
    localStringBuilder.append("/");
    localStringBuilder.append(paramViewRootImpl.getClass().getName());
    localStringBuilder.append('@');
    localStringBuilder.append(Integer.toHexString(paramViewRootImpl.hashCode()));
    return localStringBuilder.toString();
  }
  
  public static IWindowSession getWindowSession()
  {
    try
    {
      Object localObject1 = sWindowSession;
      if (localObject1 == null) {
        try
        {
          InputMethodManager localInputMethodManager = InputMethodManager.getInstance();
          IWindowManager localIWindowManager = getWindowManagerService();
          localObject1 = new android/view/WindowManagerGlobal$1;
          ((1)localObject1).<init>();
          sWindowSession = localIWindowManager.openSession((IWindowSessionCallback)localObject1, localInputMethodManager.getClient(), localInputMethodManager.getInputContext());
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      IWindowSession localIWindowSession = sWindowSession;
      return localIWindowSession;
    }
    finally {}
  }
  
  public static void initialize()
  {
    getWindowManagerService();
  }
  
  public static IWindowSession peekWindowSession()
  {
    try
    {
      IWindowSession localIWindowSession = sWindowSession;
      return localIWindowSession;
    }
    finally {}
  }
  
  private void removeViewLocked(int paramInt, boolean paramBoolean)
  {
    ViewRootImpl localViewRootImpl = (ViewRootImpl)mRoots.get(paramInt);
    View localView = localViewRootImpl.getView();
    if (localView != null)
    {
      InputMethodManager localInputMethodManager = InputMethodManager.getInstance();
      if (localInputMethodManager != null) {
        localInputMethodManager.windowDismissed(((View)mViews.get(paramInt)).getWindowToken());
      }
    }
    paramBoolean = localViewRootImpl.die(paramBoolean);
    if (localView != null)
    {
      localView.assignParent(null);
      if (paramBoolean) {
        mDyingViews.add(localView);
      }
    }
  }
  
  public static boolean shouldDestroyEglContext(int paramInt)
  {
    if (paramInt >= 80) {
      return true;
    }
    if ((paramInt >= 60) && (!ActivityManager.isHighEndGfx())) {
      return true;
    }
    return (paramInt >= 60) && (!ActivityManager.isAsusLowRamDeviceStatic());
  }
  
  public static void trimForeground()
  {
    if ((ThreadedRenderer.sTrimForeground) && (ThreadedRenderer.isAvailable())) {
      getInstance().doTrimForeground();
    }
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams, Display paramDisplay, Window paramWindow)
  {
    if (paramView != null)
    {
      if (paramDisplay != null)
      {
        if ((paramLayoutParams instanceof WindowManager.LayoutParams))
        {
          WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)paramLayoutParams;
          if (paramWindow != null)
          {
            paramWindow.adjustLayoutParamsForSubWindow(localLayoutParams);
          }
          else
          {
            paramLayoutParams = paramView.getContext();
            if ((paramLayoutParams != null) && ((getApplicationInfoflags & 0x20000000) != 0)) {
              flags |= 0x1000000;
            }
          }
          Object localObject1 = null;
          paramLayoutParams = null;
          synchronized (mLock)
          {
            if (mSystemPropertyUpdater == null)
            {
              paramWindow = new android/view/WindowManagerGlobal$2;
              paramWindow.<init>(this);
              mSystemPropertyUpdater = paramWindow;
              SystemProperties.addChangeCallback(mSystemPropertyUpdater);
            }
            int i = 0;
            int j = findViewLocked(paramView, false);
            if (j >= 0) {
              if (mDyingViews.contains(paramView))
              {
                ((ViewRootImpl)mRoots.get(j)).doDie();
              }
              else
              {
                paramDisplay = new java/lang/IllegalStateException;
                paramLayoutParams = new java/lang/StringBuilder;
                paramLayoutParams.<init>();
                paramLayoutParams.append("View ");
                paramLayoutParams.append(paramView);
                paramLayoutParams.append(" has already been added to the window manager.");
                paramDisplay.<init>(paramLayoutParams.toString());
                throw paramDisplay;
              }
            }
            paramWindow = localObject1;
            if (type >= 1000)
            {
              paramWindow = localObject1;
              if (type <= 1999)
              {
                int k = mViews.size();
                for (;;)
                {
                  paramWindow = paramLayoutParams;
                  if (i >= k) {
                    break;
                  }
                  if (mRoots.get(i)).mWindow.asBinder() == token) {
                    paramLayoutParams = (View)mViews.get(i);
                  }
                  i++;
                }
              }
            }
            paramLayoutParams = new android/view/ViewRootImpl;
            paramLayoutParams.<init>(paramView.getContext(), paramDisplay);
            paramView.setLayoutParams(localLayoutParams);
            mViews.add(paramView);
            mRoots.add(paramLayoutParams);
            mParams.add(localLayoutParams);
            try
            {
              paramLayoutParams.setView(paramView, localLayoutParams, paramWindow);
              return;
            }
            catch (RuntimeException paramView)
            {
              if (j >= 0) {
                removeViewLocked(j, true);
              }
              throw paramView;
            }
          }
        }
        throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
      }
      throw new IllegalArgumentException("display must not be null");
    }
    throw new IllegalArgumentException("view must not be null");
  }
  
  public void changeCanvasOpacity(IBinder paramIBinder, boolean paramBoolean)
  {
    if (paramIBinder == null) {
      return;
    }
    synchronized (mLock)
    {
      for (int i = mParams.size() - 1; i >= 0; i--) {
        if (mParams.get(i)).token == paramIBinder)
        {
          ((ViewRootImpl)mRoots.get(i)).changeCanvasOpacity(paramBoolean);
          return;
        }
      }
      return;
    }
  }
  
  public void closeAll(IBinder paramIBinder, String paramString1, String paramString2)
  {
    closeAllExceptView(paramIBinder, null, paramString1, paramString2);
  }
  
  public void closeAllExceptView(IBinder paramIBinder, View paramView, String paramString1, String paramString2)
  {
    synchronized (mLock)
    {
      int i = mViews.size();
      for (int j = 0; j < i; j++) {
        if (((paramView == null) || (mViews.get(j) != paramView)) && ((paramIBinder == null) || (mParams.get(j)).token == paramIBinder)))
        {
          ViewRootImpl localViewRootImpl = (ViewRootImpl)mRoots.get(j);
          if (paramString1 != null)
          {
            WindowLeaked localWindowLeaked = new android/view/WindowLeaked;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append(paramString2);
            localStringBuilder.append(" ");
            localStringBuilder.append(paramString1);
            localStringBuilder.append(" has leaked window ");
            localStringBuilder.append(localViewRootImpl.getView());
            localStringBuilder.append(" that was originally added here");
            localWindowLeaked.<init>(localStringBuilder.toString());
            localWindowLeaked.setStackTrace(localViewRootImpl.getLocation().getStackTrace());
            Log.e("WindowManager", "", localWindowLeaked);
          }
          removeViewLocked(j, false);
        }
      }
      return;
    }
  }
  
  void doRemoveView(ViewRootImpl paramViewRootImpl)
  {
    synchronized (mLock)
    {
      int i = mRoots.indexOf(paramViewRootImpl);
      if (i >= 0)
      {
        mRoots.remove(i);
        mParams.remove(i);
        paramViewRootImpl = (View)mViews.remove(i);
        mDyingViews.remove(paramViewRootImpl);
      }
      if ((ThreadedRenderer.sTrimForeground) && (ThreadedRenderer.isAvailable())) {
        doTrimForeground();
      }
      return;
    }
  }
  
  /* Error */
  public void dumpGfxInfo(java.io.FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
  {
    // Byte code:
    //   0: new 459	com/android/internal/util/FastPrintWriter
    //   3: dup
    //   4: new 461	java/io/FileOutputStream
    //   7: dup
    //   8: aload_1
    //   9: invokespecial 464	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   12: invokespecial 467	com/android/internal/util/FastPrintWriter:<init>	(Ljava/io/OutputStream;)V
    //   15: astore_3
    //   16: aload_0
    //   17: getfield 80	android/view/WindowManagerGlobal:mLock	Ljava/lang/Object;
    //   20: astore 4
    //   22: aload 4
    //   24: monitorenter
    //   25: aload_0
    //   26: getfield 85	android/view/WindowManagerGlobal:mViews	Ljava/util/ArrayList;
    //   29: invokevirtual 104	java/util/ArrayList:size	()I
    //   32: istore 5
    //   34: aload_3
    //   35: ldc_w 469
    //   38: invokevirtual 474	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   41: iconst_0
    //   42: istore 6
    //   44: iload 6
    //   46: iload 5
    //   48: if_icmpge +83 -> 131
    //   51: aload_0
    //   52: getfield 87	android/view/WindowManagerGlobal:mRoots	Ljava/util/ArrayList;
    //   55: iload 6
    //   57: invokevirtual 108	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   60: checkcast 110	android/view/ViewRootImpl
    //   63: astore 7
    //   65: aload_3
    //   66: ldc_w 476
    //   69: iconst_2
    //   70: anewarray 4	java/lang/Object
    //   73: dup
    //   74: iconst_0
    //   75: aload 7
    //   77: invokestatic 478	android/view/WindowManagerGlobal:getWindowName	(Landroid/view/ViewRootImpl;)Ljava/lang/String;
    //   80: aastore
    //   81: dup
    //   82: iconst_1
    //   83: aload 7
    //   85: invokevirtual 117	android/view/ViewRootImpl:getHostVisibility	()I
    //   88: invokestatic 482	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   91: aastore
    //   92: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   95: pop
    //   96: aload 7
    //   98: invokevirtual 273	android/view/ViewRootImpl:getView	()Landroid/view/View;
    //   101: getfield 487	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   104: getfield 127	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   107: astore 7
    //   109: aload 7
    //   111: ifnull +14 -> 125
    //   114: aload 7
    //   116: aload_3
    //   117: aload_1
    //   118: aload_2
    //   119: invokevirtual 490	android/view/ThreadedRenderer:dumpGfxInfo	(Ljava/io/PrintWriter;Ljava/io/FileDescriptor;[Ljava/lang/String;)V
    //   122: goto +3 -> 125
    //   125: iinc 6 1
    //   128: goto -84 -> 44
    //   131: aload_3
    //   132: ldc_w 492
    //   135: invokevirtual 474	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   138: iconst_2
    //   139: newarray int
    //   141: astore_2
    //   142: iconst_0
    //   143: istore 6
    //   145: iconst_0
    //   146: istore 8
    //   148: iconst_0
    //   149: istore 9
    //   151: iload 9
    //   153: iload 5
    //   155: if_icmpge +97 -> 252
    //   158: aload_0
    //   159: getfield 87	android/view/WindowManagerGlobal:mRoots	Ljava/util/ArrayList;
    //   162: iload 9
    //   164: invokevirtual 108	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   167: checkcast 110	android/view/ViewRootImpl
    //   170: astore_1
    //   171: aload_1
    //   172: aload_2
    //   173: invokevirtual 495	android/view/ViewRootImpl:dumpGfxInfo	([I)V
    //   176: aload_3
    //   177: ldc_w 497
    //   180: iconst_3
    //   181: anewarray 4	java/lang/Object
    //   184: dup
    //   185: iconst_0
    //   186: aload_1
    //   187: invokestatic 478	android/view/WindowManagerGlobal:getWindowName	(Landroid/view/ViewRootImpl;)Ljava/lang/String;
    //   190: aastore
    //   191: dup
    //   192: iconst_1
    //   193: aload_2
    //   194: iconst_0
    //   195: iaload
    //   196: invokestatic 482	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   199: aastore
    //   200: dup
    //   201: iconst_2
    //   202: aload_2
    //   203: iconst_1
    //   204: iaload
    //   205: i2f
    //   206: ldc_w 498
    //   209: fdiv
    //   210: invokestatic 503	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   213: aastore
    //   214: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   217: pop
    //   218: aload_3
    //   219: ldc_w 505
    //   222: iconst_0
    //   223: anewarray 4	java/lang/Object
    //   226: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   229: pop
    //   230: iload 8
    //   232: aload_2
    //   233: iconst_0
    //   234: iaload
    //   235: iadd
    //   236: istore 8
    //   238: iload 6
    //   240: aload_2
    //   241: iconst_1
    //   242: iaload
    //   243: iadd
    //   244: istore 6
    //   246: iinc 9 1
    //   249: goto -98 -> 151
    //   252: aload_3
    //   253: ldc_w 507
    //   256: iconst_1
    //   257: anewarray 4	java/lang/Object
    //   260: dup
    //   261: iconst_0
    //   262: iload 5
    //   264: invokestatic 482	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   267: aastore
    //   268: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   271: pop
    //   272: aload_3
    //   273: ldc_w 509
    //   276: iconst_1
    //   277: anewarray 4	java/lang/Object
    //   280: dup
    //   281: iconst_0
    //   282: iload 8
    //   284: invokestatic 482	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   287: aastore
    //   288: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   291: pop
    //   292: aload_3
    //   293: ldc_w 511
    //   296: iconst_1
    //   297: anewarray 4	java/lang/Object
    //   300: dup
    //   301: iconst_0
    //   302: iload 6
    //   304: i2f
    //   305: ldc_w 498
    //   308: fdiv
    //   309: invokestatic 503	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   312: aastore
    //   313: invokevirtual 486	java/io/PrintWriter:printf	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintWriter;
    //   316: pop
    //   317: aload 4
    //   319: monitorexit
    //   320: aload_3
    //   321: invokevirtual 514	java/io/PrintWriter:flush	()V
    //   324: return
    //   325: astore_1
    //   326: aload 4
    //   328: monitorexit
    //   329: aload_1
    //   330: athrow
    //   331: astore_1
    //   332: goto +8 -> 340
    //   335: astore_1
    //   336: goto -10 -> 326
    //   339: astore_1
    //   340: aload_3
    //   341: invokevirtual 514	java/io/PrintWriter:flush	()V
    //   344: aload_1
    //   345: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	346	0	this	WindowManagerGlobal
    //   0	346	1	paramFileDescriptor	java.io.FileDescriptor
    //   0	346	2	paramArrayOfString	String[]
    //   15	326	3	localFastPrintWriter	com.android.internal.util.FastPrintWriter
    //   32	231	5	i	int
    //   42	261	6	j	int
    //   63	52	7	localObject2	Object
    //   146	137	8	k	int
    //   149	98	9	m	int
    // Exception table:
    //   from	to	target	type
    //   25	41	325	finally
    //   51	96	325	finally
    //   96	109	325	finally
    //   329	331	331	finally
    //   114	122	335	finally
    //   131	142	335	finally
    //   158	230	335	finally
    //   252	320	335	finally
    //   326	329	335	finally
    //   16	25	339	finally
  }
  
  public View getRootView(String paramString)
  {
    synchronized (mLock)
    {
      for (int i = mRoots.size() - 1; i >= 0; i--)
      {
        ViewRootImpl localViewRootImpl = (ViewRootImpl)mRoots.get(i);
        if (paramString.equals(getWindowName(localViewRootImpl)))
        {
          paramString = localViewRootImpl.getView();
          return paramString;
        }
      }
      return null;
    }
  }
  
  public ArrayList<ViewRootImpl> getRootViews(IBinder paramIBinder)
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (mLock)
    {
      int i = mRoots.size();
      for (int j = 0; j < i; j++)
      {
        WindowManager.LayoutParams localLayoutParams1 = (WindowManager.LayoutParams)mParams.get(j);
        if (token != null) {
          if (token != paramIBinder)
          {
            int k = 0;
            int m = k;
            if (type >= 1000)
            {
              m = k;
              if (type <= 1999) {
                for (int n = 0;; n++)
                {
                  m = k;
                  if (n >= i) {
                    break;
                  }
                  View localView = (View)mViews.get(n);
                  WindowManager.LayoutParams localLayoutParams2 = (WindowManager.LayoutParams)mParams.get(n);
                  if ((token == localView.getWindowToken()) && (token == paramIBinder))
                  {
                    m = 1;
                    break;
                  }
                }
              }
            }
            if (m == 0) {}
          }
          else
          {
            localArrayList.add((ViewRootImpl)mRoots.get(j));
          }
        }
      }
      return localArrayList;
    }
  }
  
  public String[] getViewRootNames()
  {
    synchronized (mLock)
    {
      int i = mRoots.size();
      String[] arrayOfString = new String[i];
      for (int j = 0; j < i; j++) {
        arrayOfString[j] = getWindowName((ViewRootImpl)mRoots.get(j));
      }
      return arrayOfString;
    }
  }
  
  public View getWindowView(IBinder paramIBinder)
  {
    synchronized (mLock)
    {
      int i = mViews.size();
      for (int j = 0; j < i; j++)
      {
        View localView = (View)mViews.get(j);
        if (localView.getWindowToken() == paramIBinder) {
          return localView;
        }
      }
      return null;
    }
  }
  
  public void removeView(View paramView, boolean paramBoolean)
  {
    if (paramView != null) {
      synchronized (mLock)
      {
        int i = findViewLocked(paramView, true);
        View localView = ((ViewRootImpl)mRoots.get(i)).getView();
        removeViewLocked(i, paramBoolean);
        if (localView == paramView) {
          return;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Calling with view ");
        localStringBuilder.append(paramView);
        localStringBuilder.append(" but the ViewAncestor is attached to ");
        localStringBuilder.append(localView);
        localIllegalStateException.<init>(localStringBuilder.toString());
        throw localIllegalStateException;
      }
    }
    throw new IllegalArgumentException("view must not be null");
  }
  
  public void reportNewConfiguration(Configuration paramConfiguration)
  {
    synchronized (mLock)
    {
      int i = mViews.size();
      Configuration localConfiguration = new android/content/res/Configuration;
      localConfiguration.<init>(paramConfiguration);
      for (int j = 0; j < i; j++) {
        ((ViewRootImpl)mRoots.get(j)).requestUpdateConfiguration(localConfiguration);
      }
      return;
    }
  }
  
  public void setStoppedState(IBinder paramIBinder, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      for (int i = mViews.size() - 1; i >= 0; i--) {
        if ((paramIBinder == null) || (mParams.get(i)).token == paramIBinder))
        {
          ViewRootImpl localViewRootImpl = (ViewRootImpl)mRoots.get(i);
          localViewRootImpl.setWindowStopped(paramBoolean);
          setStoppedState(mAttachInfo.mWindowToken, paramBoolean);
        }
      }
      return;
    }
  }
  
  public void trimMemory(int paramInt)
  {
    if (ThreadedRenderer.isAvailable())
    {
      int i = paramInt;
      if (shouldDestroyEglContext(paramInt)) {
        synchronized (mLock)
        {
          for (paramInt = mRoots.size() - 1; paramInt >= 0; paramInt--) {
            ((ViewRootImpl)mRoots.get(paramInt)).destroyHardwareResources();
          }
          i = 80;
        }
      }
      ThreadedRenderer.trimMemory(i);
      if (ThreadedRenderer.sTrimForeground) {
        doTrimForeground();
      }
    }
  }
  
  public void updateViewLayout(View paramView, ViewGroup.LayoutParams arg2)
  {
    if (paramView != null)
    {
      if ((??? instanceof WindowManager.LayoutParams))
      {
        WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)???;
        paramView.setLayoutParams(localLayoutParams);
        synchronized (mLock)
        {
          int i = findViewLocked(paramView, true);
          paramView = (ViewRootImpl)mRoots.get(i);
          mParams.remove(i);
          mParams.add(i, localLayoutParams);
          paramView.setLayoutParams(localLayoutParams, false);
          return;
        }
      }
      throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
    }
    throw new IllegalArgumentException("view must not be null");
  }
}
