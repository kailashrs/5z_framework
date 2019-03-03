package android.print;

import android.annotation.SystemApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.printservice.PrintServiceInfo;
import android.printservice.recommendation.IRecommendationsChangeListener.Stub;
import android.printservice.recommendation.RecommendationInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import libcore.io.IoUtils;

public final class PrintManager
{
  public static final String ACTION_PRINT_DIALOG = "android.print.PRINT_DIALOG";
  public static final int ALL_SERVICES = 3;
  public static final int APP_ID_ANY = -2;
  private static final boolean DEBUG = false;
  public static final int DISABLED_SERVICES = 2;
  @SystemApi
  public static final int ENABLED_SERVICES = 1;
  public static final String EXTRA_PRINT_DIALOG_INTENT = "android.print.intent.extra.EXTRA_PRINT_DIALOG_INTENT";
  public static final String EXTRA_PRINT_DOCUMENT_ADAPTER = "android.print.intent.extra.EXTRA_PRINT_DOCUMENT_ADAPTER";
  public static final String EXTRA_PRINT_JOB = "android.print.intent.extra.EXTRA_PRINT_JOB";
  private static final String LOG_TAG = "PrintManager";
  private static final int MSG_NOTIFY_PRINT_JOB_STATE_CHANGED = 1;
  public static final String PRINT_SPOOLER_PACKAGE_NAME = "com.android.printspooler";
  private final int mAppId;
  private final Context mContext;
  private final Handler mHandler;
  private Map<PrintJobStateChangeListener, PrintJobStateChangeListenerWrapper> mPrintJobStateChangeListeners;
  private Map<PrintServiceRecommendationsChangeListener, PrintServiceRecommendationsChangeListenerWrapper> mPrintServiceRecommendationsChangeListeners;
  private Map<PrintServicesChangeListener, PrintServicesChangeListenerWrapper> mPrintServicesChangeListeners;
  private final IPrintManager mService;
  private final int mUserId;
  
  public PrintManager(Context paramContext, IPrintManager paramIPrintManager, int paramInt1, int paramInt2)
  {
    mContext = paramContext;
    mService = paramIPrintManager;
    mUserId = paramInt1;
    mAppId = paramInt2;
    mHandler = new Handler(paramContext.getMainLooper(), null, false)
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        if (what == 1)
        {
          paramAnonymousMessage = (SomeArgs)obj;
          PrintManager.PrintJobStateChangeListener localPrintJobStateChangeListener = ((PrintManager.PrintJobStateChangeListenerWrapper)arg1).getListener();
          if (localPrintJobStateChangeListener != null) {
            localPrintJobStateChangeListener.onPrintJobStateChanged((PrintJobId)arg2);
          }
          paramAnonymousMessage.recycle();
        }
      }
    };
  }
  
  public void addPrintJobStateChangeListener(PrintJobStateChangeListener paramPrintJobStateChangeListener)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintJobStateChangeListeners == null) {
      mPrintJobStateChangeListeners = new ArrayMap();
    }
    PrintJobStateChangeListenerWrapper localPrintJobStateChangeListenerWrapper = new PrintJobStateChangeListenerWrapper(paramPrintJobStateChangeListener, mHandler);
    try
    {
      mService.addPrintJobStateChangeListener(localPrintJobStateChangeListenerWrapper, mAppId, mUserId);
      mPrintJobStateChangeListeners.put(paramPrintJobStateChangeListener, localPrintJobStateChangeListenerWrapper);
      return;
    }
    catch (RemoteException paramPrintJobStateChangeListener)
    {
      throw paramPrintJobStateChangeListener.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void addPrintServiceRecommendationsChangeListener(PrintServiceRecommendationsChangeListener paramPrintServiceRecommendationsChangeListener, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramPrintServiceRecommendationsChangeListener);
    Handler localHandler = paramHandler;
    if (paramHandler == null) {
      localHandler = mHandler;
    }
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintServiceRecommendationsChangeListeners == null) {
      mPrintServiceRecommendationsChangeListeners = new ArrayMap();
    }
    paramHandler = new PrintServiceRecommendationsChangeListenerWrapper(paramPrintServiceRecommendationsChangeListener, localHandler);
    try
    {
      mService.addPrintServiceRecommendationsChangeListener(paramHandler, mUserId);
      mPrintServiceRecommendationsChangeListeners.put(paramPrintServiceRecommendationsChangeListener, paramHandler);
      return;
    }
    catch (RemoteException paramPrintServiceRecommendationsChangeListener)
    {
      throw paramPrintServiceRecommendationsChangeListener.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void addPrintServicesChangeListener(PrintServicesChangeListener paramPrintServicesChangeListener, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramPrintServicesChangeListener);
    Handler localHandler = paramHandler;
    if (paramHandler == null) {
      localHandler = mHandler;
    }
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintServicesChangeListeners == null) {
      mPrintServicesChangeListeners = new ArrayMap();
    }
    paramHandler = new PrintServicesChangeListenerWrapper(paramPrintServicesChangeListener, localHandler);
    try
    {
      mService.addPrintServicesChangeListener(paramHandler, mUserId);
      mPrintServicesChangeListeners.put(paramPrintServicesChangeListener, paramHandler);
      return;
    }
    catch (RemoteException paramPrintServicesChangeListener)
    {
      throw paramPrintServicesChangeListener.rethrowFromSystemServer();
    }
  }
  
  void cancelPrintJob(PrintJobId paramPrintJobId)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    try
    {
      mService.cancelPrintJob(paramPrintJobId, mAppId, mUserId);
      return;
    }
    catch (RemoteException paramPrintJobId)
    {
      throw paramPrintJobId.rethrowFromSystemServer();
    }
  }
  
  public PrinterDiscoverySession createPrinterDiscoverySession()
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return null;
    }
    return new PrinterDiscoverySession(mService, mContext, mUserId);
  }
  
  public Icon getCustomPrinterIcon(PrinterId paramPrinterId)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return null;
    }
    try
    {
      paramPrinterId = mService.getCustomPrinterIcon(paramPrinterId, mUserId);
      return paramPrinterId;
    }
    catch (RemoteException paramPrinterId)
    {
      throw paramPrinterId.rethrowFromSystemServer();
    }
  }
  
  public PrintManager getGlobalPrintManagerForUser(int paramInt)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return null;
    }
    return new PrintManager(mContext, mService, paramInt, -2);
  }
  
  public PrintJob getPrintJob(PrintJobId paramPrintJobId)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return null;
    }
    try
    {
      paramPrintJobId = mService.getPrintJobInfo(paramPrintJobId, mAppId, mUserId);
      if (paramPrintJobId != null)
      {
        paramPrintJobId = new PrintJob(paramPrintJobId, this);
        return paramPrintJobId;
      }
      return null;
    }
    catch (RemoteException paramPrintJobId)
    {
      throw paramPrintJobId.rethrowFromSystemServer();
    }
  }
  
  PrintJobInfo getPrintJobInfo(PrintJobId paramPrintJobId)
  {
    try
    {
      paramPrintJobId = mService.getPrintJobInfo(paramPrintJobId, mAppId, mUserId);
      return paramPrintJobId;
    }
    catch (RemoteException paramPrintJobId)
    {
      throw paramPrintJobId.rethrowFromSystemServer();
    }
  }
  
  public List<PrintJob> getPrintJobs()
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return Collections.emptyList();
    }
    try
    {
      List localList = mService.getPrintJobInfos(mAppId, mUserId);
      if (localList == null) {
        return Collections.emptyList();
      }
      int i = localList.size();
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(i);
      for (int j = 0; j < i; j++)
      {
        PrintJob localPrintJob = new android/print/PrintJob;
        localPrintJob.<init>((PrintJobInfo)localList.get(j), this);
        localArrayList.add(localPrintJob);
      }
      return localArrayList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<RecommendationInfo> getPrintServiceRecommendations()
  {
    try
    {
      List localList = mService.getPrintServiceRecommendations(mUserId);
      if (localList != null) {
        return localList;
      }
      return Collections.emptyList();
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<PrintServiceInfo> getPrintServices(int paramInt)
  {
    Preconditions.checkFlagsArgument(paramInt, 3);
    try
    {
      List localList = mService.getPrintServices(paramInt, mUserId);
      if (localList != null) {
        return localList;
      }
      return Collections.emptyList();
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public PrintJob print(String paramString, PrintDocumentAdapter paramPrintDocumentAdapter, PrintAttributes paramPrintAttributes)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return null;
    }
    if ((mContext instanceof Activity))
    {
      if (!TextUtils.isEmpty(paramString))
      {
        if (paramPrintDocumentAdapter != null)
        {
          paramPrintDocumentAdapter = new PrintDocumentAdapterDelegate((Activity)mContext, paramPrintDocumentAdapter);
          try
          {
            paramPrintDocumentAdapter = mService.print(paramString, paramPrintDocumentAdapter, paramPrintAttributes, mContext.getPackageName(), mAppId, mUserId);
            if (paramPrintDocumentAdapter != null)
            {
              paramString = (PrintJobInfo)paramPrintDocumentAdapter.getParcelable("android.print.intent.extra.EXTRA_PRINT_JOB");
              paramPrintDocumentAdapter = (IntentSender)paramPrintDocumentAdapter.getParcelable("android.print.intent.extra.EXTRA_PRINT_DIALOG_INTENT");
              if ((paramString != null) && (paramPrintDocumentAdapter != null)) {
                try
                {
                  mContext.startIntentSender(paramPrintDocumentAdapter, null, 0, 0, 0);
                  paramString = new PrintJob(paramString, this);
                  return paramString;
                }
                catch (IntentSender.SendIntentException paramString)
                {
                  Log.e("PrintManager", "Couldn't start print job config activity.", paramString);
                }
              } else {
                return null;
              }
            }
            return null;
          }
          catch (RemoteException paramString)
          {
            throw paramString.rethrowFromSystemServer();
          }
        }
        throw new IllegalArgumentException("documentAdapter cannot be null");
      }
      throw new IllegalArgumentException("printJobName cannot be empty");
    }
    throw new IllegalStateException("Can print only from an activity");
  }
  
  public void removePrintJobStateChangeListener(PrintJobStateChangeListener paramPrintJobStateChangeListener)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintJobStateChangeListeners == null) {
      return;
    }
    paramPrintJobStateChangeListener = (PrintJobStateChangeListenerWrapper)mPrintJobStateChangeListeners.remove(paramPrintJobStateChangeListener);
    if (paramPrintJobStateChangeListener == null) {
      return;
    }
    if (mPrintJobStateChangeListeners.isEmpty()) {
      mPrintJobStateChangeListeners = null;
    }
    paramPrintJobStateChangeListener.destroy();
    try
    {
      mService.removePrintJobStateChangeListener(paramPrintJobStateChangeListener, mUserId);
      return;
    }
    catch (RemoteException paramPrintJobStateChangeListener)
    {
      throw paramPrintJobStateChangeListener.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void removePrintServiceRecommendationsChangeListener(PrintServiceRecommendationsChangeListener paramPrintServiceRecommendationsChangeListener)
  {
    Preconditions.checkNotNull(paramPrintServiceRecommendationsChangeListener);
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintServiceRecommendationsChangeListeners == null) {
      return;
    }
    paramPrintServiceRecommendationsChangeListener = (PrintServiceRecommendationsChangeListenerWrapper)mPrintServiceRecommendationsChangeListeners.remove(paramPrintServiceRecommendationsChangeListener);
    if (paramPrintServiceRecommendationsChangeListener == null) {
      return;
    }
    if (mPrintServiceRecommendationsChangeListeners.isEmpty()) {
      mPrintServiceRecommendationsChangeListeners = null;
    }
    paramPrintServiceRecommendationsChangeListener.destroy();
    try
    {
      mService.removePrintServiceRecommendationsChangeListener(paramPrintServiceRecommendationsChangeListener, mUserId);
      return;
    }
    catch (RemoteException paramPrintServiceRecommendationsChangeListener)
    {
      throw paramPrintServiceRecommendationsChangeListener.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void removePrintServicesChangeListener(PrintServicesChangeListener paramPrintServicesChangeListener)
  {
    Preconditions.checkNotNull(paramPrintServicesChangeListener);
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    if (mPrintServicesChangeListeners == null) {
      return;
    }
    paramPrintServicesChangeListener = (PrintServicesChangeListenerWrapper)mPrintServicesChangeListeners.remove(paramPrintServicesChangeListener);
    if (paramPrintServicesChangeListener == null) {
      return;
    }
    if (mPrintServicesChangeListeners.isEmpty()) {
      mPrintServicesChangeListeners = null;
    }
    paramPrintServicesChangeListener.destroy();
    try
    {
      mService.removePrintServicesChangeListener(paramPrintServicesChangeListener, mUserId);
    }
    catch (RemoteException paramPrintServicesChangeListener)
    {
      Log.e("PrintManager", "Error removing print services change listener", paramPrintServicesChangeListener);
    }
  }
  
  void restartPrintJob(PrintJobId paramPrintJobId)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    try
    {
      mService.restartPrintJob(paramPrintJobId, mAppId, mUserId);
      return;
    }
    catch (RemoteException paramPrintJobId)
    {
      throw paramPrintJobId.rethrowFromSystemServer();
    }
  }
  
  public void setPrintServiceEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    if (mService == null)
    {
      Log.w("PrintManager", "Feature android.software.print not available");
      return;
    }
    try
    {
      mService.setPrintServiceEnabled(paramComponentName, paramBoolean, mUserId);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error enabling or disabling ");
      localStringBuilder.append(paramComponentName);
      Log.e("PrintManager", localStringBuilder.toString(), localRemoteException);
    }
  }
  
  public static final class PrintDocumentAdapterDelegate
    extends IPrintDocumentAdapter.Stub
    implements Application.ActivityLifecycleCallbacks
  {
    private Activity mActivity;
    private PrintDocumentAdapter mDocumentAdapter;
    private Handler mHandler;
    private final Object mLock = new Object();
    private IPrintDocumentAdapterObserver mObserver;
    private DestroyableCallback mPendingCallback;
    
    public PrintDocumentAdapterDelegate(Activity paramActivity, PrintDocumentAdapter paramPrintDocumentAdapter)
    {
      if (!paramActivity.isFinishing())
      {
        mActivity = paramActivity;
        mDocumentAdapter = paramPrintDocumentAdapter;
        mHandler = new MyHandler(mActivity.getMainLooper());
        mActivity.getApplication().registerActivityLifecycleCallbacks(this);
        return;
      }
      throw new IllegalStateException("Cannot start printing for finishing activity");
    }
    
    private void destroyLocked()
    {
      mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
      mActivity = null;
      mDocumentAdapter = null;
      mHandler.removeMessages(1);
      mHandler.removeMessages(2);
      mHandler.removeMessages(3);
      mHandler.removeMessages(4);
      mHandler = null;
      mObserver = null;
      if (mPendingCallback != null)
      {
        mPendingCallback.destroy();
        mPendingCallback = null;
      }
    }
    
    private boolean isDestroyedLocked()
    {
      boolean bool;
      if (mActivity == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void finish()
    {
      synchronized (mLock)
      {
        if (!isDestroyedLocked()) {
          mHandler.obtainMessage(4, mDocumentAdapter).sendToTarget();
        }
        return;
      }
    }
    
    public void kill(String paramString)
    {
      synchronized (mLock)
      {
        if (!isDestroyedLocked()) {
          mHandler.obtainMessage(5, paramString).sendToTarget();
        }
        return;
      }
    }
    
    public void layout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, ILayoutResultCallback paramILayoutResultCallback, Bundle paramBundle, int paramInt)
    {
      Object localObject1 = CancellationSignal.createTransport();
      try
      {
        paramILayoutResultCallback.onLayoutStarted((ICancellationSignal)localObject1, paramInt);
        synchronized (mLock)
        {
          if (isDestroyedLocked()) {
            return;
          }
          CancellationSignal localCancellationSignal = CancellationSignal.fromTransport((ICancellationSignal)localObject1);
          localObject1 = SomeArgs.obtain();
          arg1 = mDocumentAdapter;
          arg2 = paramPrintAttributes1;
          arg3 = paramPrintAttributes2;
          arg4 = localCancellationSignal;
          paramPrintAttributes1 = new android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback;
          paramPrintAttributes1.<init>(this, paramILayoutResultCallback, paramInt);
          arg5 = paramPrintAttributes1;
          arg6 = paramBundle;
          mHandler.obtainMessage(2, localObject1).sendToTarget();
          return;
        }
        return;
      }
      catch (RemoteException paramPrintAttributes1)
      {
        Log.e("PrintManager", "Error notifying for layout start", paramPrintAttributes1);
      }
    }
    
    public void onActivityCreated(Activity paramActivity, Bundle paramBundle) {}
    
    public void onActivityDestroyed(Activity paramActivity)
    {
      IPrintDocumentAdapterObserver localIPrintDocumentAdapterObserver = null;
      synchronized (mLock)
      {
        if (paramActivity == mActivity)
        {
          localIPrintDocumentAdapterObserver = mObserver;
          destroyLocked();
        }
        if (localIPrintDocumentAdapterObserver != null) {
          try
          {
            localIPrintDocumentAdapterObserver.onDestroy();
          }
          catch (RemoteException paramActivity)
          {
            Log.e("PrintManager", "Error announcing destroyed state", paramActivity);
          }
        }
        return;
      }
    }
    
    public void onActivityPaused(Activity paramActivity) {}
    
    public void onActivityResumed(Activity paramActivity) {}
    
    public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle) {}
    
    public void onActivityStarted(Activity paramActivity) {}
    
    public void onActivityStopped(Activity paramActivity) {}
    
    public void setObserver(IPrintDocumentAdapterObserver paramIPrintDocumentAdapterObserver)
    {
      synchronized (mLock)
      {
        mObserver = paramIPrintDocumentAdapterObserver;
        boolean bool = isDestroyedLocked();
        if ((bool) && (paramIPrintDocumentAdapterObserver != null)) {
          try
          {
            paramIPrintDocumentAdapterObserver.onDestroy();
          }
          catch (RemoteException paramIPrintDocumentAdapterObserver)
          {
            Log.e("PrintManager", "Error announcing destroyed state", paramIPrintDocumentAdapterObserver);
          }
        }
        return;
      }
    }
    
    public void start()
    {
      synchronized (mLock)
      {
        if (!isDestroyedLocked()) {
          mHandler.obtainMessage(1, mDocumentAdapter).sendToTarget();
        }
        return;
      }
    }
    
    public void write(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, IWriteResultCallback paramIWriteResultCallback, int paramInt)
    {
      Object localObject1 = CancellationSignal.createTransport();
      try
      {
        paramIWriteResultCallback.onWriteStarted((ICancellationSignal)localObject1, paramInt);
        synchronized (mLock)
        {
          if (isDestroyedLocked()) {
            return;
          }
          CancellationSignal localCancellationSignal = CancellationSignal.fromTransport((ICancellationSignal)localObject1);
          localObject1 = SomeArgs.obtain();
          arg1 = mDocumentAdapter;
          arg2 = paramArrayOfPageRange;
          arg3 = paramParcelFileDescriptor;
          arg4 = localCancellationSignal;
          paramArrayOfPageRange = new android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback;
          paramArrayOfPageRange.<init>(this, paramIWriteResultCallback, paramParcelFileDescriptor, paramInt);
          arg5 = paramArrayOfPageRange;
          mHandler.obtainMessage(3, localObject1).sendToTarget();
          return;
        }
        return;
      }
      catch (RemoteException paramArrayOfPageRange)
      {
        Log.e("PrintManager", "Error notifying for write start", paramArrayOfPageRange);
      }
    }
    
    private static abstract interface DestroyableCallback
    {
      public abstract void destroy();
    }
    
    private final class MyHandler
      extends Handler
    {
      public static final int MSG_ON_FINISH = 4;
      public static final int MSG_ON_KILL = 5;
      public static final int MSG_ON_LAYOUT = 2;
      public static final int MSG_ON_START = 1;
      public static final int MSG_ON_WRITE = 3;
      
      public MyHandler(Looper paramLooper)
      {
        super(null, true);
      }
      
      public void handleMessage(Message arg1)
      {
        Object localObject3;
        Object localObject4;
        Object localObject5;
        Object localObject2;
        Object localObject6;
        switch (what)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown message: ");
          localStringBuilder.append(what);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 5: 
          throw new RuntimeException((String)obj);
        case 4: 
          ((PrintDocumentAdapter)obj).onFinish();
          synchronized (mLock)
          {
            PrintManager.PrintDocumentAdapterDelegate.this.destroyLocked();
          }
        case 3: 
          ??? = (SomeArgs)obj;
          localObject3 = (PrintDocumentAdapter)arg1;
          localObject4 = (PageRange[])arg2;
          localObject5 = (ParcelFileDescriptor)arg3;
          localObject2 = (CancellationSignal)arg4;
          localObject6 = (PrintDocumentAdapter.WriteResultCallback)arg5;
          ???.recycle();
          ((PrintDocumentAdapter)localObject3).onWrite((PageRange[])localObject4, (ParcelFileDescriptor)localObject5, (CancellationSignal)localObject2, (PrintDocumentAdapter.WriteResultCallback)localObject6);
          break;
        case 2: 
          localObject4 = (SomeArgs)obj;
          PrintDocumentAdapter localPrintDocumentAdapter = (PrintDocumentAdapter)arg1;
          localObject2 = (PrintAttributes)arg2;
          localObject5 = (PrintAttributes)arg3;
          localObject6 = (CancellationSignal)arg4;
          ??? = (PrintDocumentAdapter.LayoutResultCallback)arg5;
          localObject3 = (Bundle)arg6;
          ((SomeArgs)localObject4).recycle();
          localPrintDocumentAdapter.onLayout((PrintAttributes)localObject2, (PrintAttributes)localObject5, (CancellationSignal)localObject6, ???, (Bundle)localObject3);
          break;
        case 1: 
          ((PrintDocumentAdapter)obj).onStart();
        }
      }
    }
    
    private final class MyLayoutResultCallback
      extends PrintDocumentAdapter.LayoutResultCallback
      implements PrintManager.PrintDocumentAdapterDelegate.DestroyableCallback
    {
      private ILayoutResultCallback mCallback;
      private final int mSequence;
      
      public MyLayoutResultCallback(ILayoutResultCallback paramILayoutResultCallback, int paramInt)
      {
        mCallback = paramILayoutResultCallback;
        mSequence = paramInt;
      }
      
      public void destroy()
      {
        synchronized (mLock)
        {
          mCallback = null;
          PrintManager.PrintDocumentAdapterDelegate.access$202(PrintManager.PrintDocumentAdapterDelegate.this, null);
          return;
        }
      }
      
      /* Error */
      public void onLayoutCancelled()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 22	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:this$0	Landroid/print/PrintManager$PrintDocumentAdapterDelegate;
        //   4: invokestatic 35	android/print/PrintManager$PrintDocumentAdapterDelegate:access$000	(Landroid/print/PrintManager$PrintDocumentAdapterDelegate;)Ljava/lang/Object;
        //   7: astore_1
        //   8: aload_1
        //   9: monitorenter
        //   10: aload_0
        //   11: getfield 27	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:mCallback	Landroid/print/ILayoutResultCallback;
        //   14: astore_2
        //   15: aload_1
        //   16: monitorexit
        //   17: aload_2
        //   18: ifnonnull +12 -> 30
        //   21: ldc 44
        //   23: ldc 46
        //   25: invokestatic 52	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   28: pop
        //   29: return
        //   30: aload_2
        //   31: aload_0
        //   32: getfield 29	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:mSequence	I
        //   35: invokeinterface 58 2 0
        //   40: aload_0
        //   41: invokevirtual 60	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:destroy	()V
        //   44: goto +20 -> 64
        //   47: astore_1
        //   48: goto +17 -> 65
        //   51: astore_1
        //   52: ldc 44
        //   54: ldc 62
        //   56: aload_1
        //   57: invokestatic 65	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   60: pop
        //   61: goto -21 -> 40
        //   64: return
        //   65: aload_0
        //   66: invokevirtual 60	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:destroy	()V
        //   69: aload_1
        //   70: athrow
        //   71: astore_2
        //   72: aload_1
        //   73: monitorexit
        //   74: aload_2
        //   75: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	76	0	this	MyLayoutResultCallback
        //   7	9	1	localObject1	Object
        //   47	1	1	localObject2	Object
        //   51	22	1	localRemoteException	RemoteException
        //   14	17	2	localILayoutResultCallback	ILayoutResultCallback
        //   71	4	2	localObject3	Object
        // Exception table:
        //   from	to	target	type
        //   30	40	47	finally
        //   52	61	47	finally
        //   30	40	51	android/os/RemoteException
        //   10	17	71	finally
        //   72	74	71	finally
      }
      
      /* Error */
      public void onLayoutFailed(CharSequence paramCharSequence)
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 22	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:this$0	Landroid/print/PrintManager$PrintDocumentAdapterDelegate;
        //   4: invokestatic 35	android/print/PrintManager$PrintDocumentAdapterDelegate:access$000	(Landroid/print/PrintManager$PrintDocumentAdapterDelegate;)Ljava/lang/Object;
        //   7: astore_2
        //   8: aload_2
        //   9: monitorenter
        //   10: aload_0
        //   11: getfield 27	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:mCallback	Landroid/print/ILayoutResultCallback;
        //   14: astore_3
        //   15: aload_2
        //   16: monitorexit
        //   17: aload_3
        //   18: ifnonnull +12 -> 30
        //   21: ldc 44
        //   23: ldc 46
        //   25: invokestatic 52	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   28: pop
        //   29: return
        //   30: aload_3
        //   31: aload_1
        //   32: aload_0
        //   33: getfield 29	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:mSequence	I
        //   36: invokeinterface 70 3 0
        //   41: aload_0
        //   42: invokevirtual 60	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:destroy	()V
        //   45: goto +20 -> 65
        //   48: astore_1
        //   49: goto +17 -> 66
        //   52: astore_1
        //   53: ldc 44
        //   55: ldc 62
        //   57: aload_1
        //   58: invokestatic 65	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   61: pop
        //   62: goto -21 -> 41
        //   65: return
        //   66: aload_0
        //   67: invokevirtual 60	android/print/PrintManager$PrintDocumentAdapterDelegate$MyLayoutResultCallback:destroy	()V
        //   70: aload_1
        //   71: athrow
        //   72: astore_1
        //   73: aload_2
        //   74: monitorexit
        //   75: aload_1
        //   76: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	77	0	this	MyLayoutResultCallback
        //   0	77	1	paramCharSequence	CharSequence
        //   7	67	2	localObject	Object
        //   14	17	3	localILayoutResultCallback	ILayoutResultCallback
        // Exception table:
        //   from	to	target	type
        //   30	41	48	finally
        //   53	62	48	finally
        //   30	41	52	android/os/RemoteException
        //   10	17	72	finally
        //   73	75	72	finally
      }
      
      public void onLayoutFinished(PrintDocumentInfo paramPrintDocumentInfo, boolean paramBoolean)
      {
        synchronized (mLock)
        {
          ILayoutResultCallback localILayoutResultCallback = mCallback;
          if (localILayoutResultCallback == null)
          {
            Log.e("PrintManager", "PrintDocumentAdapter is destroyed. Did you finish the printing activity before print completion or did you invoke a callback after finish?");
            return;
          }
          if (paramPrintDocumentInfo != null) {}
          try
          {
            try
            {
              localILayoutResultCallback.onLayoutFinished(paramPrintDocumentInfo, paramBoolean, mSequence);
            }
            catch (RemoteException paramPrintDocumentInfo)
            {
              Log.e("PrintManager", "Error calling onLayoutFinished", paramPrintDocumentInfo);
            }
            return;
          }
          finally
          {
            destroy();
          }
          paramPrintDocumentInfo = new java/lang/NullPointerException;
          paramPrintDocumentInfo.<init>("document info cannot be null");
          throw paramPrintDocumentInfo;
        }
      }
    }
    
    private final class MyWriteResultCallback
      extends PrintDocumentAdapter.WriteResultCallback
      implements PrintManager.PrintDocumentAdapterDelegate.DestroyableCallback
    {
      private IWriteResultCallback mCallback;
      private ParcelFileDescriptor mFd;
      private final int mSequence;
      
      public MyWriteResultCallback(IWriteResultCallback paramIWriteResultCallback, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
      {
        mFd = paramParcelFileDescriptor;
        mSequence = paramInt;
        mCallback = paramIWriteResultCallback;
      }
      
      public void destroy()
      {
        synchronized (mLock)
        {
          IoUtils.closeQuietly(mFd);
          mCallback = null;
          mFd = null;
          PrintManager.PrintDocumentAdapterDelegate.access$202(PrintManager.PrintDocumentAdapterDelegate.this, null);
          return;
        }
      }
      
      /* Error */
      public void onWriteCancelled()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 24	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:this$0	Landroid/print/PrintManager$PrintDocumentAdapterDelegate;
        //   4: invokestatic 39	android/print/PrintManager$PrintDocumentAdapterDelegate:access$000	(Landroid/print/PrintManager$PrintDocumentAdapterDelegate;)Ljava/lang/Object;
        //   7: astore_1
        //   8: aload_1
        //   9: monitorenter
        //   10: aload_0
        //   11: getfield 33	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:mCallback	Landroid/print/IWriteResultCallback;
        //   14: astore_2
        //   15: aload_1
        //   16: monitorexit
        //   17: aload_2
        //   18: ifnonnull +12 -> 30
        //   21: ldc 54
        //   23: ldc 56
        //   25: invokestatic 62	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   28: pop
        //   29: return
        //   30: aload_2
        //   31: aload_0
        //   32: getfield 31	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:mSequence	I
        //   35: invokeinterface 68 2 0
        //   40: aload_0
        //   41: invokevirtual 70	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:destroy	()V
        //   44: goto +20 -> 64
        //   47: astore_1
        //   48: goto +17 -> 65
        //   51: astore_1
        //   52: ldc 54
        //   54: ldc 72
        //   56: aload_1
        //   57: invokestatic 75	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   60: pop
        //   61: goto -21 -> 40
        //   64: return
        //   65: aload_0
        //   66: invokevirtual 70	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:destroy	()V
        //   69: aload_1
        //   70: athrow
        //   71: astore_2
        //   72: aload_1
        //   73: monitorexit
        //   74: aload_2
        //   75: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	76	0	this	MyWriteResultCallback
        //   7	9	1	localObject1	Object
        //   47	1	1	localObject2	Object
        //   51	22	1	localRemoteException	RemoteException
        //   14	17	2	localIWriteResultCallback	IWriteResultCallback
        //   71	4	2	localObject3	Object
        // Exception table:
        //   from	to	target	type
        //   30	40	47	finally
        //   52	61	47	finally
        //   30	40	51	android/os/RemoteException
        //   10	17	71	finally
        //   72	74	71	finally
      }
      
      /* Error */
      public void onWriteFailed(CharSequence paramCharSequence)
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 24	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:this$0	Landroid/print/PrintManager$PrintDocumentAdapterDelegate;
        //   4: invokestatic 39	android/print/PrintManager$PrintDocumentAdapterDelegate:access$000	(Landroid/print/PrintManager$PrintDocumentAdapterDelegate;)Ljava/lang/Object;
        //   7: astore_2
        //   8: aload_2
        //   9: monitorenter
        //   10: aload_0
        //   11: getfield 33	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:mCallback	Landroid/print/IWriteResultCallback;
        //   14: astore_3
        //   15: aload_2
        //   16: monitorexit
        //   17: aload_3
        //   18: ifnonnull +12 -> 30
        //   21: ldc 54
        //   23: ldc 56
        //   25: invokestatic 62	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   28: pop
        //   29: return
        //   30: aload_3
        //   31: aload_1
        //   32: aload_0
        //   33: getfield 31	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:mSequence	I
        //   36: invokeinterface 80 3 0
        //   41: aload_0
        //   42: invokevirtual 70	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:destroy	()V
        //   45: goto +20 -> 65
        //   48: astore_1
        //   49: goto +17 -> 66
        //   52: astore_1
        //   53: ldc 54
        //   55: ldc 82
        //   57: aload_1
        //   58: invokestatic 75	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   61: pop
        //   62: goto -21 -> 41
        //   65: return
        //   66: aload_0
        //   67: invokevirtual 70	android/print/PrintManager$PrintDocumentAdapterDelegate$MyWriteResultCallback:destroy	()V
        //   70: aload_1
        //   71: athrow
        //   72: astore_1
        //   73: aload_2
        //   74: monitorexit
        //   75: aload_1
        //   76: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	77	0	this	MyWriteResultCallback
        //   0	77	1	paramCharSequence	CharSequence
        //   7	67	2	localObject	Object
        //   14	17	3	localIWriteResultCallback	IWriteResultCallback
        // Exception table:
        //   from	to	target	type
        //   30	41	48	finally
        //   53	62	48	finally
        //   30	41	52	android/os/RemoteException
        //   10	17	72	finally
        //   73	75	72	finally
      }
      
      public void onWriteFinished(PageRange[] paramArrayOfPageRange)
      {
        synchronized (mLock)
        {
          IWriteResultCallback localIWriteResultCallback = mCallback;
          if (localIWriteResultCallback == null)
          {
            Log.e("PrintManager", "PrintDocumentAdapter is destroyed. Did you finish the printing activity before print completion or did you invoke a callback after finish?");
            return;
          }
          if (paramArrayOfPageRange != null) {}
          try
          {
            int i = paramArrayOfPageRange.length;
            if (i != 0)
            {
              try
              {
                localIWriteResultCallback.onWriteFinished(paramArrayOfPageRange, mSequence);
              }
              catch (RemoteException paramArrayOfPageRange)
              {
                Log.e("PrintManager", "Error calling onWriteFinished", paramArrayOfPageRange);
              }
              return;
            }
            paramArrayOfPageRange = new java/lang/IllegalArgumentException;
            paramArrayOfPageRange.<init>("pages cannot be empty");
            throw paramArrayOfPageRange;
          }
          finally
          {
            destroy();
          }
          paramArrayOfPageRange = new java/lang/IllegalArgumentException;
          paramArrayOfPageRange.<init>("pages cannot be null");
          throw paramArrayOfPageRange;
        }
      }
    }
  }
  
  public static abstract interface PrintJobStateChangeListener
  {
    public abstract void onPrintJobStateChanged(PrintJobId paramPrintJobId);
  }
  
  public static final class PrintJobStateChangeListenerWrapper
    extends IPrintJobStateChangeListener.Stub
  {
    private final WeakReference<Handler> mWeakHandler;
    private final WeakReference<PrintManager.PrintJobStateChangeListener> mWeakListener;
    
    public PrintJobStateChangeListenerWrapper(PrintManager.PrintJobStateChangeListener paramPrintJobStateChangeListener, Handler paramHandler)
    {
      mWeakListener = new WeakReference(paramPrintJobStateChangeListener);
      mWeakHandler = new WeakReference(paramHandler);
    }
    
    public void destroy()
    {
      mWeakListener.clear();
    }
    
    public PrintManager.PrintJobStateChangeListener getListener()
    {
      return (PrintManager.PrintJobStateChangeListener)mWeakListener.get();
    }
    
    public void onPrintJobStateChanged(PrintJobId paramPrintJobId)
    {
      Handler localHandler = (Handler)mWeakHandler.get();
      Object localObject = (PrintManager.PrintJobStateChangeListener)mWeakListener.get();
      if ((localHandler != null) && (localObject != null))
      {
        localObject = SomeArgs.obtain();
        arg1 = this;
        arg2 = paramPrintJobId;
        localHandler.obtainMessage(1, localObject).sendToTarget();
      }
    }
  }
  
  @SystemApi
  public static abstract interface PrintServiceRecommendationsChangeListener
  {
    public abstract void onPrintServiceRecommendationsChanged();
  }
  
  public static final class PrintServiceRecommendationsChangeListenerWrapper
    extends IRecommendationsChangeListener.Stub
  {
    private final WeakReference<Handler> mWeakHandler;
    private final WeakReference<PrintManager.PrintServiceRecommendationsChangeListener> mWeakListener;
    
    public PrintServiceRecommendationsChangeListenerWrapper(PrintManager.PrintServiceRecommendationsChangeListener paramPrintServiceRecommendationsChangeListener, Handler paramHandler)
    {
      mWeakListener = new WeakReference(paramPrintServiceRecommendationsChangeListener);
      mWeakHandler = new WeakReference(paramHandler);
    }
    
    public void destroy()
    {
      mWeakListener.clear();
    }
    
    public void onRecommendationsChanged()
    {
      Handler localHandler = (Handler)mWeakHandler.get();
      PrintManager.PrintServiceRecommendationsChangeListener localPrintServiceRecommendationsChangeListener = (PrintManager.PrintServiceRecommendationsChangeListener)mWeakListener.get();
      if ((localHandler != null) && (localPrintServiceRecommendationsChangeListener != null))
      {
        Objects.requireNonNull(localPrintServiceRecommendationsChangeListener);
        localHandler.post(new _..Lambda.KZ41E_yXUNYMY9k_Xeus1UG_cS8(localPrintServiceRecommendationsChangeListener));
      }
    }
  }
  
  @SystemApi
  public static abstract interface PrintServicesChangeListener
  {
    public abstract void onPrintServicesChanged();
  }
  
  public static final class PrintServicesChangeListenerWrapper
    extends IPrintServicesChangeListener.Stub
  {
    private final WeakReference<Handler> mWeakHandler;
    private final WeakReference<PrintManager.PrintServicesChangeListener> mWeakListener;
    
    public PrintServicesChangeListenerWrapper(PrintManager.PrintServicesChangeListener paramPrintServicesChangeListener, Handler paramHandler)
    {
      mWeakListener = new WeakReference(paramPrintServicesChangeListener);
      mWeakHandler = new WeakReference(paramHandler);
    }
    
    public void destroy()
    {
      mWeakListener.clear();
    }
    
    public void onPrintServicesChanged()
    {
      Handler localHandler = (Handler)mWeakHandler.get();
      PrintManager.PrintServicesChangeListener localPrintServicesChangeListener = (PrintManager.PrintServicesChangeListener)mWeakListener.get();
      if ((localHandler != null) && (localPrintServicesChangeListener != null))
      {
        Objects.requireNonNull(localPrintServicesChangeListener);
        localHandler.post(new _..Lambda.c2Elb5E1w2yc6lr236iX_RUAL5Q(localPrintServicesChangeListener));
      }
    }
  }
}
