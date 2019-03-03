package android.printservice;

import android.graphics.drawable.Icon;
import android.os.RemoteException;
import android.print.PrinterId;
import android.util.Log;

public final class CustomPrinterIconCallback
{
  private static final String LOG_TAG = "CustomPrinterIconCB";
  private final IPrintServiceClient mObserver;
  private final PrinterId mPrinterId;
  
  CustomPrinterIconCallback(PrinterId paramPrinterId, IPrintServiceClient paramIPrintServiceClient)
  {
    mPrinterId = paramPrinterId;
    mObserver = paramIPrintServiceClient;
  }
  
  public boolean onCustomPrinterIconLoaded(Icon paramIcon)
  {
    try
    {
      mObserver.onCustomPrinterIconLoaded(mPrinterId, paramIcon);
      return true;
    }
    catch (RemoteException paramIcon)
    {
      Log.e("CustomPrinterIconCB", "Could not update icon", paramIcon);
    }
    return false;
  }
}
