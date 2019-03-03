package android.print;

import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.os.Message;
import android.printservice.PrintServiceInfo;
import com.android.internal.util.Preconditions;
import java.util.List;

public class PrintServicesLoader
  extends Loader<List<PrintServiceInfo>>
{
  private final Handler mHandler = new MyHandler();
  private PrintManager.PrintServicesChangeListener mListener;
  private final PrintManager mPrintManager;
  private final int mSelectionFlags;
  
  public PrintServicesLoader(PrintManager paramPrintManager, Context paramContext, int paramInt)
  {
    super((Context)Preconditions.checkNotNull(paramContext));
    mPrintManager = ((PrintManager)Preconditions.checkNotNull(paramPrintManager));
    mSelectionFlags = Preconditions.checkFlagsArgument(paramInt, 3);
  }
  
  private void queueNewResult()
  {
    Message localMessage = mHandler.obtainMessage(0);
    obj = mPrintManager.getPrintServices(mSelectionFlags);
    mHandler.sendMessage(localMessage);
  }
  
  protected void onForceLoad()
  {
    queueNewResult();
  }
  
  protected void onReset()
  {
    onStopLoading();
  }
  
  protected void onStartLoading()
  {
    mListener = new PrintManager.PrintServicesChangeListener()
    {
      public void onPrintServicesChanged()
      {
        PrintServicesLoader.this.queueNewResult();
      }
    };
    mPrintManager.addPrintServicesChangeListener(mListener, null);
    deliverResult(mPrintManager.getPrintServices(mSelectionFlags));
  }
  
  protected void onStopLoading()
  {
    if (mListener != null)
    {
      mPrintManager.removePrintServicesChangeListener(mListener);
      mListener = null;
    }
    mHandler.removeMessages(0);
  }
  
  private class MyHandler
    extends Handler
  {
    public MyHandler()
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (isStarted()) {
        deliverResult((List)obj);
      }
    }
  }
}
