package android.print;

import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PrinterDiscoverySession
{
  private static final String LOG_TAG = "PrinterDiscoverySession";
  private static final int MSG_PRINTERS_ADDED = 1;
  private static final int MSG_PRINTERS_REMOVED = 2;
  private final Handler mHandler;
  private boolean mIsPrinterDiscoveryStarted;
  private OnPrintersChangeListener mListener;
  private IPrinterDiscoveryObserver mObserver;
  private final IPrintManager mPrintManager;
  private final LinkedHashMap<PrinterId, PrinterInfo> mPrinters = new LinkedHashMap();
  private final int mUserId;
  
  PrinterDiscoverySession(IPrintManager paramIPrintManager, Context paramContext, int paramInt)
  {
    mPrintManager = paramIPrintManager;
    mUserId = paramInt;
    mHandler = new SessionHandler(paramContext.getMainLooper());
    mObserver = new PrinterDiscoveryObserver(this);
    try
    {
      mPrintManager.createPrinterDiscoverySession(mObserver, mUserId);
    }
    catch (RemoteException paramIPrintManager)
    {
      Log.e("PrinterDiscoverySession", "Error creating printer discovery session", paramIPrintManager);
    }
  }
  
  /* Error */
  private void destroyNoCheck()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 99	android/print/PrinterDiscoverySession:stopPrinterDiscovery	()V
    //   4: aload_0
    //   5: getfield 50	android/print/PrinterDiscoverySession:mPrintManager	Landroid/print/IPrintManager;
    //   8: aload_0
    //   9: getfield 68	android/print/PrinterDiscoverySession:mObserver	Landroid/print/IPrinterDiscoveryObserver;
    //   12: aload_0
    //   13: getfield 52	android/print/PrinterDiscoverySession:mUserId	I
    //   16: invokeinterface 102 3 0
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield 68	android/print/PrinterDiscoverySession:mObserver	Landroid/print/IPrinterDiscoveryObserver;
    //   26: aload_0
    //   27: getfield 48	android/print/PrinterDiscoverySession:mPrinters	Ljava/util/LinkedHashMap;
    //   30: invokevirtual 105	java/util/LinkedHashMap:clear	()V
    //   33: goto +20 -> 53
    //   36: astore_1
    //   37: goto +17 -> 54
    //   40: astore_1
    //   41: ldc 17
    //   43: ldc 107
    //   45: aload_1
    //   46: invokestatic 82	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   49: pop
    //   50: goto -29 -> 21
    //   53: return
    //   54: aload_0
    //   55: aconst_null
    //   56: putfield 68	android/print/PrinterDiscoverySession:mObserver	Landroid/print/IPrinterDiscoveryObserver;
    //   59: aload_0
    //   60: getfield 48	android/print/PrinterDiscoverySession:mPrinters	Ljava/util/LinkedHashMap;
    //   63: invokevirtual 105	java/util/LinkedHashMap:clear	()V
    //   66: aload_1
    //   67: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	68	0	this	PrinterDiscoverySession
    //   36	1	1	localObject	Object
    //   40	27	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   4	21	36	finally
    //   41	50	36	finally
    //   4	21	40	android/os/RemoteException
  }
  
  private void handlePrintersAdded(List<PrinterInfo> paramList)
  {
    if (isDestroyed()) {
      return;
    }
    boolean bool = mPrinters.isEmpty();
    int i = 0;
    int j = 0;
    if (bool)
    {
      i = paramList.size();
      while (j < i)
      {
        localObject1 = (PrinterInfo)paramList.get(j);
        mPrinters.put(((PrinterInfo)localObject1).getId(), localObject1);
        j++;
      }
      notifyOnPrintersChanged();
      return;
    }
    Object localObject1 = new ArrayMap();
    int k = paramList.size();
    Object localObject2;
    for (j = i; j < k; j++)
    {
      localObject2 = (PrinterInfo)paramList.get(j);
      ((ArrayMap)localObject1).put(((PrinterInfo)localObject2).getId(), localObject2);
    }
    Iterator localIterator = mPrinters.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject2 = (PrinterId)localIterator.next();
      paramList = (PrinterInfo)((ArrayMap)localObject1).remove(localObject2);
      if (paramList != null) {
        mPrinters.put(localObject2, paramList);
      }
    }
    mPrinters.putAll((Map)localObject1);
    notifyOnPrintersChanged();
  }
  
  private void handlePrintersRemoved(List<PrinterId> paramList)
  {
    if (isDestroyed()) {
      return;
    }
    int i = 0;
    int j = paramList.size();
    for (int k = 0; k < j; k++)
    {
      PrinterId localPrinterId = (PrinterId)paramList.get(k);
      if (mPrinters.remove(localPrinterId) != null) {
        i = 1;
      }
    }
    if (i != 0) {
      notifyOnPrintersChanged();
    }
  }
  
  private boolean isDestroyedNoCheck()
  {
    boolean bool;
    if (mObserver == null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void notifyOnPrintersChanged()
  {
    if (mListener != null) {
      mListener.onPrintersChanged();
    }
  }
  
  private static void throwIfNotCalledOnMainThread()
  {
    if (Looper.getMainLooper().isCurrentThread()) {
      return;
    }
    throw new IllegalAccessError("must be called from the main thread");
  }
  
  public final void destroy()
  {
    if (isDestroyed()) {
      Log.w("PrinterDiscoverySession", "Ignoring destroy - session destroyed");
    }
    destroyNoCheck();
  }
  
  protected final void finalize()
    throws Throwable
  {
    if (!isDestroyedNoCheck())
    {
      Log.e("PrinterDiscoverySession", "Destroying leaked printer discovery session");
      destroyNoCheck();
    }
    super.finalize();
  }
  
  public final List<PrinterInfo> getPrinters()
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring get printers - session destroyed");
      return Collections.emptyList();
    }
    return new ArrayList(mPrinters.values());
  }
  
  public final boolean isDestroyed()
  {
    throwIfNotCalledOnMainThread();
    return isDestroyedNoCheck();
  }
  
  public final boolean isPrinterDiscoveryStarted()
  {
    throwIfNotCalledOnMainThread();
    return mIsPrinterDiscoveryStarted;
  }
  
  public final void setOnPrintersChangeListener(OnPrintersChangeListener paramOnPrintersChangeListener)
  {
    throwIfNotCalledOnMainThread();
    mListener = paramOnPrintersChangeListener;
  }
  
  public final void startPrinterDiscovery(List<PrinterId> paramList)
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring start printers discovery - session destroyed");
      return;
    }
    if (!mIsPrinterDiscoveryStarted)
    {
      mIsPrinterDiscoveryStarted = true;
      try
      {
        mPrintManager.startPrinterDiscovery(mObserver, paramList, mUserId);
      }
      catch (RemoteException paramList)
      {
        Log.e("PrinterDiscoverySession", "Error starting printer discovery", paramList);
      }
    }
  }
  
  public final void startPrinterStateTracking(PrinterId paramPrinterId)
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring start printer state tracking - session destroyed");
      return;
    }
    try
    {
      mPrintManager.startPrinterStateTracking(paramPrinterId, mUserId);
    }
    catch (RemoteException paramPrinterId)
    {
      Log.e("PrinterDiscoverySession", "Error starting printer state tracking", paramPrinterId);
    }
  }
  
  public final void stopPrinterDiscovery()
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring stop printers discovery - session destroyed");
      return;
    }
    if (mIsPrinterDiscoveryStarted)
    {
      mIsPrinterDiscoveryStarted = false;
      try
      {
        mPrintManager.stopPrinterDiscovery(mObserver, mUserId);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("PrinterDiscoverySession", "Error stopping printer discovery", localRemoteException);
      }
    }
  }
  
  public final void stopPrinterStateTracking(PrinterId paramPrinterId)
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring stop printer state tracking - session destroyed");
      return;
    }
    try
    {
      mPrintManager.stopPrinterStateTracking(paramPrinterId, mUserId);
    }
    catch (RemoteException paramPrinterId)
    {
      Log.e("PrinterDiscoverySession", "Error stopping printer state tracking", paramPrinterId);
    }
  }
  
  public final void validatePrinters(List<PrinterId> paramList)
  {
    if (isDestroyed())
    {
      Log.w("PrinterDiscoverySession", "Ignoring validate printers - session destroyed");
      return;
    }
    try
    {
      mPrintManager.validatePrinters(paramList, mUserId);
    }
    catch (RemoteException paramList)
    {
      Log.e("PrinterDiscoverySession", "Error validating printers", paramList);
    }
  }
  
  public static abstract interface OnPrintersChangeListener
  {
    public abstract void onPrintersChanged();
  }
  
  public static final class PrinterDiscoveryObserver
    extends IPrinterDiscoveryObserver.Stub
  {
    private final WeakReference<PrinterDiscoverySession> mWeakSession;
    
    public PrinterDiscoveryObserver(PrinterDiscoverySession paramPrinterDiscoverySession)
    {
      mWeakSession = new WeakReference(paramPrinterDiscoverySession);
    }
    
    public void onPrintersAdded(ParceledListSlice paramParceledListSlice)
    {
      PrinterDiscoverySession localPrinterDiscoverySession = (PrinterDiscoverySession)mWeakSession.get();
      if (localPrinterDiscoverySession != null) {
        mHandler.obtainMessage(1, paramParceledListSlice.getList()).sendToTarget();
      }
    }
    
    public void onPrintersRemoved(ParceledListSlice paramParceledListSlice)
    {
      PrinterDiscoverySession localPrinterDiscoverySession = (PrinterDiscoverySession)mWeakSession.get();
      if (localPrinterDiscoverySession != null) {
        mHandler.obtainMessage(2, paramParceledListSlice.getList()).sendToTarget();
      }
    }
  }
  
  private final class SessionHandler
    extends Handler
  {
    public SessionHandler(Looper paramLooper)
    {
      super(null, false);
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 2: 
        paramMessage = (List)obj;
        PrinterDiscoverySession.this.handlePrintersRemoved(paramMessage);
        break;
      case 1: 
        paramMessage = (List)obj;
        PrinterDiscoverySession.this.handlePrintersAdded(paramMessage);
      }
    }
  }
}
