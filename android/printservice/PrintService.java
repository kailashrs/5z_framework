package android.printservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.print.PrintJobInfo;
import android.print.PrinterId;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PrintService
  extends Service
{
  private static final boolean DEBUG = false;
  public static final String EXTRA_CAN_SELECT_PRINTER = "android.printservice.extra.CAN_SELECT_PRINTER";
  public static final String EXTRA_PRINTER_INFO = "android.intent.extra.print.EXTRA_PRINTER_INFO";
  public static final String EXTRA_PRINT_DOCUMENT_INFO = "android.printservice.extra.PRINT_DOCUMENT_INFO";
  public static final String EXTRA_PRINT_JOB_INFO = "android.intent.extra.print.PRINT_JOB_INFO";
  public static final String EXTRA_SELECT_PRINTER = "android.printservice.extra.SELECT_PRINTER";
  private static final String LOG_TAG = "PrintService";
  public static final String SERVICE_INTERFACE = "android.printservice.PrintService";
  public static final String SERVICE_META_DATA = "android.printservice";
  private IPrintServiceClient mClient;
  private PrinterDiscoverySession mDiscoverySession;
  private Handler mHandler;
  private int mLastSessionId = -1;
  
  public PrintService() {}
  
  static void throwIfNotCalledOnMainThread()
  {
    if (Looper.getMainLooper().isCurrentThread()) {
      return;
    }
    throw new IllegalAccessError("must be called from the main thread");
  }
  
  protected final void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new ServiceHandler(paramContext.getMainLooper());
  }
  
  public final PrinterId generatePrinterId(String paramString)
  {
    throwIfNotCalledOnMainThread();
    paramString = (String)Preconditions.checkNotNull(paramString, "localId cannot be null");
    return new PrinterId(new ComponentName(getPackageName(), getClass().getName()), paramString);
  }
  
  public final List<PrintJob> getActivePrintJobs()
  {
    
    if (mClient == null) {
      return Collections.emptyList();
    }
    Object localObject = null;
    try
    {
      List localList = mClient.getPrintJobInfos();
      if (localList != null)
      {
        int i = localList.size();
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>(i);
        for (int j = 0;; j++)
        {
          localObject = localArrayList;
          if (j >= i) {
            break;
          }
          localObject = new android/printservice/PrintJob;
          ((PrintJob)localObject).<init>(this, (PrintJobInfo)localList.get(j), mClient);
          localArrayList.add(localObject);
        }
      }
      if (localObject != null) {
        return localObject;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("PrintService", "Error calling getPrintJobs()", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new IPrintService.Stub()
    {
      public void createPrinterDiscoverySession()
      {
        mHandler.sendEmptyMessage(1);
      }
      
      public void destroyPrinterDiscoverySession()
      {
        mHandler.sendEmptyMessage(2);
      }
      
      public void onPrintJobQueued(PrintJobInfo paramAnonymousPrintJobInfo)
      {
        mHandler.obtainMessage(9, paramAnonymousPrintJobInfo).sendToTarget();
      }
      
      public void requestCancelPrintJob(PrintJobInfo paramAnonymousPrintJobInfo)
      {
        mHandler.obtainMessage(10, paramAnonymousPrintJobInfo).sendToTarget();
      }
      
      public void requestCustomPrinterIcon(PrinterId paramAnonymousPrinterId)
      {
        mHandler.obtainMessage(7, paramAnonymousPrinterId).sendToTarget();
      }
      
      public void setClient(IPrintServiceClient paramAnonymousIPrintServiceClient)
      {
        mHandler.obtainMessage(11, paramAnonymousIPrintServiceClient).sendToTarget();
      }
      
      public void startPrinterDiscovery(List<PrinterId> paramAnonymousList)
      {
        mHandler.obtainMessage(3, paramAnonymousList).sendToTarget();
      }
      
      public void startPrinterStateTracking(PrinterId paramAnonymousPrinterId)
      {
        mHandler.obtainMessage(6, paramAnonymousPrinterId).sendToTarget();
      }
      
      public void stopPrinterDiscovery()
      {
        mHandler.sendEmptyMessage(4);
      }
      
      public void stopPrinterStateTracking(PrinterId paramAnonymousPrinterId)
      {
        mHandler.obtainMessage(8, paramAnonymousPrinterId).sendToTarget();
      }
      
      public void validatePrinters(List<PrinterId> paramAnonymousList)
      {
        mHandler.obtainMessage(5, paramAnonymousList).sendToTarget();
      }
    };
  }
  
  protected void onConnected() {}
  
  protected abstract PrinterDiscoverySession onCreatePrinterDiscoverySession();
  
  protected void onDisconnected() {}
  
  protected abstract void onPrintJobQueued(PrintJob paramPrintJob);
  
  protected abstract void onRequestCancelPrintJob(PrintJob paramPrintJob);
  
  private final class ServiceHandler
    extends Handler
  {
    public static final int MSG_CREATE_PRINTER_DISCOVERY_SESSION = 1;
    public static final int MSG_DESTROY_PRINTER_DISCOVERY_SESSION = 2;
    public static final int MSG_ON_PRINTJOB_QUEUED = 9;
    public static final int MSG_ON_REQUEST_CANCEL_PRINTJOB = 10;
    public static final int MSG_REQUEST_CUSTOM_PRINTER_ICON = 7;
    public static final int MSG_SET_CLIENT = 11;
    public static final int MSG_START_PRINTER_DISCOVERY = 3;
    public static final int MSG_START_PRINTER_STATE_TRACKING = 6;
    public static final int MSG_STOP_PRINTER_DISCOVERY = 4;
    public static final int MSG_STOP_PRINTER_STATE_TRACKING = 8;
    public static final int MSG_VALIDATE_PRINTERS = 5;
    
    public ServiceHandler(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      switch (i)
      {
      default: 
        paramMessage = new StringBuilder();
        paramMessage.append("Unknown message: ");
        paramMessage.append(i);
        throw new IllegalArgumentException(paramMessage.toString());
      case 11: 
        PrintService.access$302(PrintService.this, (IPrintServiceClient)obj);
        if (mClient != null) {
          onConnected();
        } else {
          onDisconnected();
        }
        break;
      case 10: 
        paramMessage = (PrintJobInfo)obj;
        onRequestCancelPrintJob(new PrintJob(PrintService.this, paramMessage, mClient));
        break;
      case 9: 
        paramMessage = (PrintJobInfo)obj;
        onPrintJobQueued(new PrintJob(PrintService.this, paramMessage, mClient));
        break;
      case 8: 
        if (mDiscoverySession != null)
        {
          paramMessage = (PrinterId)obj;
          mDiscoverySession.stopPrinterStateTracking(paramMessage);
        }
        break;
      case 7: 
        if (mDiscoverySession != null)
        {
          paramMessage = (PrinterId)obj;
          mDiscoverySession.requestCustomPrinterIcon(paramMessage);
        }
        break;
      case 6: 
        if (mDiscoverySession != null)
        {
          paramMessage = (PrinterId)obj;
          mDiscoverySession.startPrinterStateTracking(paramMessage);
        }
        break;
      case 5: 
        if (mDiscoverySession != null)
        {
          paramMessage = (List)obj;
          mDiscoverySession.validatePrinters(paramMessage);
        }
        break;
      case 4: 
        if (mDiscoverySession != null) {
          mDiscoverySession.stopPrinterDiscovery();
        }
        break;
      case 3: 
        if (mDiscoverySession != null)
        {
          paramMessage = (ArrayList)obj;
          mDiscoverySession.startPrinterDiscovery(paramMessage);
        }
        break;
      case 2: 
        if (mDiscoverySession != null)
        {
          mDiscoverySession.destroy();
          PrintService.access$202(PrintService.this, null);
        }
        break;
      case 1: 
        paramMessage = onCreatePrinterDiscoverySession();
        if (paramMessage == null) {
          break label500;
        }
        if (paramMessage.getId() == mLastSessionId) {
          break label490;
        }
        PrintService.access$202(PrintService.this, paramMessage);
        PrintService.access$102(PrintService.this, paramMessage.getId());
        paramMessage.setObserver(mClient);
      }
      return;
      label490:
      throw new IllegalStateException("cannot reuse session instances");
      label500:
      throw new NullPointerException("session cannot be null");
    }
  }
}
