package android.printservice;

import android.content.pm.ParceledListSlice;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class PrinterDiscoverySession
{
  private static final String LOG_TAG = "PrinterDiscoverySession";
  private static int sIdCounter = 0;
  private final int mId;
  private boolean mIsDestroyed;
  private boolean mIsDiscoveryStarted;
  private ArrayMap<PrinterId, PrinterInfo> mLastSentPrinters;
  private IPrintServiceClient mObserver;
  private final ArrayMap<PrinterId, PrinterInfo> mPrinters = new ArrayMap();
  private final List<PrinterId> mTrackedPrinters = new ArrayList();
  
  public PrinterDiscoverySession()
  {
    int i = sIdCounter;
    sIdCounter = i + 1;
    mId = i;
  }
  
  private void sendOutOfDiscoveryPeriodPrinterChanges()
  {
    if ((mLastSentPrinters != null) && (!mLastSentPrinters.isEmpty()))
    {
      Object localObject1 = null;
      Object localObject3 = mPrinters.values().iterator();
      PrinterInfo localPrinterInfo1;
      Object localObject4;
      while (((Iterator)localObject3).hasNext())
      {
        localPrinterInfo1 = (PrinterInfo)((Iterator)localObject3).next();
        PrinterInfo localPrinterInfo2 = (PrinterInfo)mLastSentPrinters.get(localPrinterInfo1.getId());
        if (localPrinterInfo2 != null)
        {
          localObject4 = localObject1;
          if (localPrinterInfo2.equals(localPrinterInfo1)) {}
        }
        else
        {
          localObject4 = localObject1;
          if (localObject1 == null) {
            localObject4 = new ArrayList();
          }
          ((List)localObject4).add(localPrinterInfo1);
        }
        localObject1 = localObject4;
      }
      if (localObject1 != null) {
        try
        {
          localObject4 = mObserver;
          localObject3 = new android/content/pm/ParceledListSlice;
          ((ParceledListSlice)localObject3).<init>(localObject1);
          ((IPrintServiceClient)localObject4).onPrintersAdded((ParceledListSlice)localObject3);
        }
        catch (RemoteException localRemoteException1)
        {
          Log.e("PrinterDiscoverySession", "Error sending added printers", localRemoteException1);
        }
      }
      Object localObject2 = null;
      localObject3 = mLastSentPrinters.values().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localPrinterInfo1 = (PrinterInfo)((Iterator)localObject3).next();
        localObject4 = localObject2;
        if (!mPrinters.containsKey(localPrinterInfo1.getId()))
        {
          localObject4 = localObject2;
          if (localObject2 == null) {
            localObject4 = new ArrayList();
          }
          ((List)localObject4).add(localPrinterInfo1.getId());
        }
        localObject2 = localObject4;
      }
      if (localObject2 != null) {
        try
        {
          localObject3 = mObserver;
          localObject4 = new android/content/pm/ParceledListSlice;
          ((ParceledListSlice)localObject4).<init>(localObject2);
          ((IPrintServiceClient)localObject3).onPrintersRemoved((ParceledListSlice)localObject4);
        }
        catch (RemoteException localRemoteException2)
        {
          Log.e("PrinterDiscoverySession", "Error sending removed printers", localRemoteException2);
        }
      }
      mLastSentPrinters = null;
      return;
    }
    mLastSentPrinters = null;
  }
  
  public final void addPrinters(List<PrinterInfo> paramList)
  {
    
    if (mIsDestroyed)
    {
      Log.w("PrinterDiscoverySession", "Not adding printers - session destroyed.");
      return;
    }
    boolean bool = mIsDiscoveryStarted;
    int i = 0;
    int j = 0;
    Object localObject1;
    if (bool)
    {
      localObject1 = null;
      i = paramList.size();
      Object localObject2;
      while (j < i)
      {
        PrinterInfo localPrinterInfo1 = (PrinterInfo)paramList.get(j);
        PrinterInfo localPrinterInfo2 = (PrinterInfo)mPrinters.put(localPrinterInfo1.getId(), localPrinterInfo1);
        if (localPrinterInfo2 != null)
        {
          localObject2 = localObject1;
          if (localPrinterInfo2.equals(localPrinterInfo1)) {}
        }
        else
        {
          localObject2 = localObject1;
          if (localObject1 == null) {
            localObject2 = new ArrayList();
          }
          ((List)localObject2).add(localPrinterInfo1);
        }
        j++;
        localObject1 = localObject2;
      }
      if (localObject1 != null) {
        try
        {
          localObject2 = mObserver;
          paramList = new android/content/pm/ParceledListSlice;
          paramList.<init>((List)localObject1);
          ((IPrintServiceClient)localObject2).onPrintersAdded(paramList);
        }
        catch (RemoteException paramList)
        {
          Log.e("PrinterDiscoverySession", "Error sending added printers", paramList);
        }
      }
    }
    else
    {
      if (mLastSentPrinters == null) {
        mLastSentPrinters = new ArrayMap(mPrinters);
      }
      int k = paramList.size();
      for (j = i; j < k; j++)
      {
        localObject1 = (PrinterInfo)paramList.get(j);
        if (mPrinters.get(((PrinterInfo)localObject1).getId()) == null) {
          mPrinters.put(((PrinterInfo)localObject1).getId(), localObject1);
        }
      }
    }
  }
  
  void destroy()
  {
    if (!mIsDestroyed)
    {
      mIsDestroyed = true;
      mIsDiscoveryStarted = false;
      mPrinters.clear();
      mLastSentPrinters = null;
      mObserver = null;
      onDestroy();
    }
  }
  
  int getId()
  {
    return mId;
  }
  
  public final List<PrinterInfo> getPrinters()
  {
    
    if (mIsDestroyed) {
      return Collections.emptyList();
    }
    return new ArrayList(mPrinters.values());
  }
  
  public final List<PrinterId> getTrackedPrinters()
  {
    
    if (mIsDestroyed) {
      return Collections.emptyList();
    }
    return new ArrayList(mTrackedPrinters);
  }
  
  public final boolean isDestroyed()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return mIsDestroyed;
  }
  
  public final boolean isPrinterDiscoveryStarted()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return mIsDiscoveryStarted;
  }
  
  public abstract void onDestroy();
  
  public void onRequestCustomPrinterIcon(PrinterId paramPrinterId, CancellationSignal paramCancellationSignal, CustomPrinterIconCallback paramCustomPrinterIconCallback) {}
  
  public abstract void onStartPrinterDiscovery(List<PrinterId> paramList);
  
  public abstract void onStartPrinterStateTracking(PrinterId paramPrinterId);
  
  public abstract void onStopPrinterDiscovery();
  
  public abstract void onStopPrinterStateTracking(PrinterId paramPrinterId);
  
  public abstract void onValidatePrinters(List<PrinterId> paramList);
  
  public final void removePrinters(List<PrinterId> paramList)
  {
    
    if (mIsDestroyed)
    {
      Log.w("PrinterDiscoverySession", "Not removing printers - session destroyed.");
      return;
    }
    boolean bool = mIsDiscoveryStarted;
    int i = 0;
    int j = 0;
    Object localObject1;
    if (bool)
    {
      localObject1 = new ArrayList();
      i = paramList.size();
      Object localObject2;
      while (j < i)
      {
        localObject2 = (PrinterId)paramList.get(j);
        if (mPrinters.remove(localObject2) != null) {
          ((List)localObject1).add(localObject2);
        }
        j++;
      }
      if (!((List)localObject1).isEmpty()) {
        try
        {
          localObject2 = mObserver;
          paramList = new android/content/pm/ParceledListSlice;
          paramList.<init>((List)localObject1);
          ((IPrintServiceClient)localObject2).onPrintersRemoved(paramList);
        }
        catch (RemoteException paramList)
        {
          Log.e("PrinterDiscoverySession", "Error sending removed printers", paramList);
        }
      }
    }
    else
    {
      if (mLastSentPrinters == null) {
        mLastSentPrinters = new ArrayMap(mPrinters);
      }
      int k = paramList.size();
      for (j = i; j < k; j++)
      {
        localObject1 = (PrinterId)paramList.get(j);
        mPrinters.remove(localObject1);
      }
    }
  }
  
  void requestCustomPrinterIcon(PrinterId paramPrinterId)
  {
    if ((!mIsDestroyed) && (mObserver != null))
    {
      CustomPrinterIconCallback localCustomPrinterIconCallback = new CustomPrinterIconCallback(paramPrinterId, mObserver);
      onRequestCustomPrinterIcon(paramPrinterId, new CancellationSignal(), localCustomPrinterIconCallback);
    }
  }
  
  void setObserver(IPrintServiceClient paramIPrintServiceClient)
  {
    mObserver = paramIPrintServiceClient;
    if (!mPrinters.isEmpty()) {
      try
      {
        paramIPrintServiceClient = mObserver;
        ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
        localParceledListSlice.<init>(getPrinters());
        paramIPrintServiceClient.onPrintersAdded(localParceledListSlice);
      }
      catch (RemoteException paramIPrintServiceClient)
      {
        Log.e("PrinterDiscoverySession", "Error sending added printers", paramIPrintServiceClient);
      }
    }
  }
  
  void startPrinterDiscovery(List<PrinterId> paramList)
  {
    if (!mIsDestroyed)
    {
      mIsDiscoveryStarted = true;
      sendOutOfDiscoveryPeriodPrinterChanges();
      Object localObject = paramList;
      if (paramList == null) {
        localObject = Collections.emptyList();
      }
      onStartPrinterDiscovery((List)localObject);
    }
  }
  
  void startPrinterStateTracking(PrinterId paramPrinterId)
  {
    if ((!mIsDestroyed) && (mObserver != null) && (!mTrackedPrinters.contains(paramPrinterId)))
    {
      mTrackedPrinters.add(paramPrinterId);
      onStartPrinterStateTracking(paramPrinterId);
    }
  }
  
  void stopPrinterDiscovery()
  {
    if (!mIsDestroyed)
    {
      mIsDiscoveryStarted = false;
      onStopPrinterDiscovery();
    }
  }
  
  void stopPrinterStateTracking(PrinterId paramPrinterId)
  {
    if ((!mIsDestroyed) && (mObserver != null) && (mTrackedPrinters.remove(paramPrinterId))) {
      onStopPrinterStateTracking(paramPrinterId);
    }
  }
  
  void validatePrinters(List<PrinterId> paramList)
  {
    if ((!mIsDestroyed) && (mObserver != null)) {
      onValidatePrinters(paramList);
    }
  }
}
