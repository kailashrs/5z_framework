package android.print;

import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.os.Message;
import android.printservice.recommendation.RecommendationInfo;
import com.android.internal.util.Preconditions;
import java.util.List;

public class PrintServiceRecommendationsLoader
  extends Loader<List<RecommendationInfo>>
{
  private final Handler mHandler = new MyHandler();
  private PrintManager.PrintServiceRecommendationsChangeListener mListener;
  private final PrintManager mPrintManager;
  
  public PrintServiceRecommendationsLoader(PrintManager paramPrintManager, Context paramContext)
  {
    super((Context)Preconditions.checkNotNull(paramContext));
    mPrintManager = ((PrintManager)Preconditions.checkNotNull(paramPrintManager));
  }
  
  private void queueNewResult()
  {
    Message localMessage = mHandler.obtainMessage(0);
    obj = mPrintManager.getPrintServiceRecommendations();
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
    mListener = new PrintManager.PrintServiceRecommendationsChangeListener()
    {
      public void onPrintServiceRecommendationsChanged()
      {
        PrintServiceRecommendationsLoader.this.queueNewResult();
      }
    };
    mPrintManager.addPrintServiceRecommendationsChangeListener(mListener, null);
    deliverResult(mPrintManager.getPrintServiceRecommendations());
  }
  
  protected void onStopLoading()
  {
    if (mListener != null)
    {
      mPrintManager.removePrintServiceRecommendationsChangeListener(mListener);
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
